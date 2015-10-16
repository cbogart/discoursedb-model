package edu.cmu.cs.lti.discoursedb.core.service.macro;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import edu.cmu.cs.lti.discoursedb.core.model.macro.Contribution;
import edu.cmu.cs.lti.discoursedb.core.model.macro.DiscourseRelation;
import edu.cmu.cs.lti.discoursedb.core.model.macro.DiscourseRelationType;
import edu.cmu.cs.lti.discoursedb.core.repository.macro.DiscourseRelationRepository;
import edu.cmu.cs.lti.discoursedb.core.repository.macro.DiscourseRelationTypeRepository;
import edu.cmu.cs.lti.discoursedb.core.type.DiscourseRelationTypes;

@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
@Service
public class DiscourseRelationService {

	@Autowired private DiscourseRelationTypeRepository discRelationTypeRepo;
	@Autowired private DiscourseRelationRepository discourseRelationRepo;

	/**
	 * Creates a new DiscourseRelation of the given type between the two provided contributions.
	 * Depending on the type, the relation might be directed or not. This information should be given in the type definition.
	 * e.g. a REPLY relation would be interpreted as the target(child) being the reply to the source(parent).
	 * 
	 * If a DiscourseRelation of the given type already exists between the two contributions (taking into account the direction of the relation),
	 * then the existing relation is returned. 
	 * DiscourseDB does not enforce the uniqueness of these relations by default, but enforcing it in this service method will cater to most of the use cases we will see.
	 * 
	 * @param sourceContribution the source or parent contribution of the relation
	 * @param targetContribution the target or child contribution of the relation
	 * @param type the DiscourseRelationType
	 * @return a DiscourseRelation between the two provided contributions with the given type that has already been saved to the database 
	 */
	public DiscourseRelation createDiscourseRelation(Contribution sourceContribution, Contribution targetContribution, DiscourseRelationTypes type) {
		//Retrieve type or create if it doesn't exist in db
		DiscourseRelationType discourseRelationType = null;
		Optional<DiscourseRelationType> existingDiscourseRelationType = discRelationTypeRepo.findOneByType(type.name());
		if(existingDiscourseRelationType.isPresent()){
			discourseRelationType=existingDiscourseRelationType.get();
		}else{
			discourseRelationType = new DiscourseRelationType();
			discourseRelationType.setType(type.name());
			discRelationTypeRepo.save(discourseRelationType);			
		}
		
		//check if a relation of the given type already exists between the two contributions.
		Optional<DiscourseRelation> existingRelation = discourseRelationRepo.findOneBySourceAndTargetAndType(sourceContribution, targetContribution, discourseRelationType);
		if(existingRelation.isPresent()){
			return existingRelation.get();
		}
		
		//create, save and return the new relation
		DiscourseRelation newRelation = new DiscourseRelation();
		newRelation.setSource(sourceContribution);
		newRelation.setTarget(targetContribution);
		newRelation.setType(discourseRelationType);
		return discourseRelationRepo.save(newRelation);
	}

		
}