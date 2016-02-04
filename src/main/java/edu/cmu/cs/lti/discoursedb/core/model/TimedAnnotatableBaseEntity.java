package edu.cmu.cs.lti.discoursedb.core.model;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.springframework.data.rest.core.annotation.RestResource;

import edu.cmu.cs.lti.discoursedb.core.model.annotation.Annotations;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Adds basic fields to entities that keep track of their lifetime (Version,
 * entity creation date, start date, end date)
 * 
 * @author Oliver Ferschke
 *
 */
@Data
@EqualsAndHashCode(callSuper=true, exclude={"annotations"})
@ToString(callSuper=true, exclude={"annotations"})
@MappedSuperclass
public abstract class TimedAnnotatableBaseEntity extends TimedBaseEntity{
	
	@RestResource(rel="annotationAggregate",path="annotationAggregate")
	@ManyToOne(cascade={CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.DETACH}) 
	@JoinColumn(name = "fk_annotation")
	private Annotations annotations;
}
