package edu.cmu.cs.lti.discoursedb.core.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.CreationTimestamp;

import edu.cmu.cs.lti.discoursedb.core.model.annotation.Annotations;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

/**
 * Adds basic fields to entities that keep track of their lifetime (Version,
 * entity creation date, start date, end date)
 * 
 * @author Oliver Ferschke
 *
 */
@Data
@EqualsAndHashCode(exclude={"annotations"})
@ToString(exclude={"annotations"})
@MappedSuperclass
public abstract class TimedAnnotatableBaseEntity{

	@Version
	@Setter(AccessLevel.PRIVATE) 
	private Long version;	
	
	@CreationTimestamp
	@Column(name = "created")
	@Setter(AccessLevel.PRIVATE) 
	private Date createDate;
	
	@Column(name = "start_time")
	@Temporal(TemporalType.TIMESTAMP)
    private Date startTime;   

	@Column(name = "end_time")
	@Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
	
	@ManyToOne(cascade=CascadeType.ALL) 
	@JoinColumn(name = "fk_annotation")
	private Annotations annotations;
}
