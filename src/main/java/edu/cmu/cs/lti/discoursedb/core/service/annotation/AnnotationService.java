package edu.cmu.cs.lti.discoursedb.core.service.annotation;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import edu.cmu.cs.lti.discoursedb.core.model.TimedAnnotatableBaseEntity;
import edu.cmu.cs.lti.discoursedb.core.model.TimedAnnotatableBaseEntityWithSource;
import edu.cmu.cs.lti.discoursedb.core.model.annotation.AnnotationInstance;
import edu.cmu.cs.lti.discoursedb.core.model.annotation.AnnotationType;
import edu.cmu.cs.lti.discoursedb.core.model.annotation.Annotations;
import edu.cmu.cs.lti.discoursedb.core.model.annotation.Feature;
import edu.cmu.cs.lti.discoursedb.core.model.annotation.FeatureType;
import edu.cmu.cs.lti.discoursedb.core.repository.annotation.AnnotationInstanceRepository;
import edu.cmu.cs.lti.discoursedb.core.repository.annotation.AnnotationTypeRepository;
import edu.cmu.cs.lti.discoursedb.core.repository.annotation.AnnotationsRepository;
import edu.cmu.cs.lti.discoursedb.core.repository.annotation.FeatureRepository;
import edu.cmu.cs.lti.discoursedb.core.repository.annotation.FeatureTypeRepository;

@Transactional(propagation= Propagation.REQUIRED, readOnly=false)
@Service
public class AnnotationService {

	@Autowired private AnnotationInstanceRepository annoInstanceRepo;
	@Autowired private AnnotationTypeRepository annoTypeRepo;
	@Autowired private AnnotationsRepository annoRepo;
	@Autowired private FeatureRepository featureRepo;
	@Autowired private FeatureTypeRepository featureTypeRepo;
	
	/**
	 * Retrieves all annotations for the given entity.
	 * 
	 * This is a convenience method. 
	 * It actually just retrieves content from the entity object, but it performs additional null checks on the annotation aggregate.
	 * 
	 * @param entity the entity to retrieve the datasource for
	 * @return a DataSourceInstance with the given descriptor for the given entity
	 */
	@Transactional(propagation= Propagation.REQUIRED, readOnly=true)
	public <T extends TimedAnnotatableBaseEntityWithSource> Set<AnnotationInstance> findAnnotations(T entity) {
		Assert.notNull(entity);		
		Annotations annos = entity.getAnnotations();
		return annos==null?new HashSet<AnnotationInstance>():annos.getAnnotations();
	}

	/**
	 * Retrieves all annotations for the given entity.
	 * 
	 * This is a convenience method. 
	 * It actually just retrieves content from the entity object, but it performs additional null checks on the annotation aggregate.
	 * 
	 * @param entity the entity to retrieve the datasource for
	 * @return a DataSourceInstance with the given descriptor for the given entity
	 */
	@Transactional(propagation= Propagation.REQUIRED, readOnly=true)
	public <T extends TimedAnnotatableBaseEntity> Set<AnnotationInstance> findAnnotations(T entity) {
		Assert.notNull(entity);		
		Annotations annos = entity.getAnnotations();
		return annos==null?new HashSet<AnnotationInstance>():annos.getAnnotations();
	}
	
	/**
	 * Creates a new AnnotationInstance and associates it with an AnnotationType that matches the provided String.
	 * If an annotationtype with the provided String already exists, it will be reused.
	 * 
	 * @param type
	 *            the value for the AnnotationType
	 * @return a new empty AnnotationInstance that is already saved to the db and
	 *         connected with its requested type
	 */
	public AnnotationInstance createTypedAnnotation(String type){
		Assert.hasText(type);
		
		Optional<AnnotationType> existingAnnoType = annoTypeRepo.findOneByType(type);
		AnnotationType annoType = null;
		if(existingAnnoType.isPresent()){
			annoType = existingAnnoType.get();
		}else{
			annoType = new AnnotationType();
			annoType.setType(type);
			annoType= annoTypeRepo.save(annoType);
		}

		AnnotationInstance annotation = new AnnotationInstance();
		annotation.setType(annoType);
		return annoInstanceRepo.save(annotation);
	}	
	
	/**
	 * Creates a new Feature with the provided value.
	 * No type is assigned.
	 * 
	 * @param value
	 *            the feature value
	 * @return a new empty Feature that is already saved to the db and
	 *         connected with its requested type
	 */
	public Feature createFeature(String value){
		Assert.hasText(value);
		
		Feature feature = new Feature();		
		feature.setValue(value);
		return featureRepo.save(feature);
	}	
	
	/**
	 * Creates a new Feature and associates it with a FeatureType that matches the provided String.
	 * If an FeatureType with the provided String already exists, it will be reused.
	 * 
	 * @param value
	 *            the feature value
	 * @param type
	 *            the value for the FeatureType
	 * @return a new empty Feature that is already saved to the db and
	 *         connected with its requested type
	 */
	public Feature createTypedFeature(String value, String type){
		Assert.hasText(value);
		Assert.hasText(type);
		
		Feature feature = createTypedFeature(type);		
		feature.setValue(value);		
		return featureRepo.save(feature);
	}	

	/**
	 * Creates a new Feature and associates it with a FeatureType that matches the provided String.
	 * If an FeatureType with the provided String already exists, it will be reused.
	 * 
	 * @param type
	 *            the value for the FeatureType
	 * @return a new empty Feature that is already saved to the db and
	 *         connected with its requested type
	 */
	public Feature createTypedFeature(String type){
		Assert.hasText(type);
		
		Optional<FeatureType> existingFeatureType = featureTypeRepo.findOneByType(type);
		FeatureType featureType = null;
		if(existingFeatureType.isPresent()){
			featureType = existingFeatureType.get();
		}else{
			featureType = new FeatureType();
			featureType.setType(type);
			featureType= featureTypeRepo.save(featureType);
		}

		Feature feature = new Feature();
		feature.setType(featureType);
		return featureRepo.save(feature);
	}	
	
	/**
	 * Adds a new annotation instance to the provided entity.<br/>
	 * 
	 * @param entity
	 *            the entity to add a new source to
	 * @param annotation
	 *            the annotation instance to add to the entity
	 */
	public <T extends TimedAnnotatableBaseEntity> void addAnnotation(T entity, AnnotationInstance annotation) {		
		Assert.notNull(entity);
		Assert.notNull(annotation);

		//the annotations aggregate is a proxy for the entity
		//all annotation instances are connected to the aggregate which is finally connected to the annotated entity
		Annotations annoAggregate = entity.getAnnotations();
		if (annoAggregate == null) {
			annoAggregate=annoRepo.save(new Annotations());
			entity.setAnnotations(annoAggregate);
		}
		annotation.setAnnotationAggregate(annoAggregate);
		annotation = annoInstanceRepo.save(annotation);
		annoAggregate.addAnnotation(annotation);
	}
	
	/**
	 * Adds a new annotation instance to the provided entity.
	 * 
	 * @param annotation
	 *            the annotation to which the feature should be added
	 * @param feature
	 *            the new feature to add
	 */
	public void addFeature(AnnotationInstance annotation, Feature feature) {		
		Assert.notNull(annotation);
		Assert.notNull(feature);		
		feature.setAnnotation(annotation);
		annotation.addFeature(feature);
	}

}