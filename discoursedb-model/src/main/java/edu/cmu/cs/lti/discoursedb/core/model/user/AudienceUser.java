package edu.cmu.cs.lti.discoursedb.core.model.user;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import edu.cmu.cs.lti.discoursedb.core.model.annotation.Annotations;

@Entity
@SelectBeforeUpdate 
@DynamicUpdate
@DynamicInsert
@Table(name="audience_has_user")
public class AudienceUser implements Serializable{
	
	private static final long serialVersionUID = -6740377434969542427L;

	private long id;
	
    private User user;
    
    private Audience audience;
    
    private Timestamp startTime;
    
    private Timestamp endTime;
	
    private Annotations annotations;
    
	public AudienceUser() {}
    
	@Column(name = "start_time")
	@Temporal(TemporalType.TIMESTAMP)
	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	@Column(name = "end_time")
	@Temporal(TemporalType.TIMESTAMP)
	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	@Id
	@Column(name="id_annotation", nullable=false)
    @GeneratedValue(strategy = GenerationType.AUTO)	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne(cascade=CascadeType.ALL) 
	@JoinColumn(name = "id_annotation")
	public Annotations getAnnotations() {
		return annotations;
	}

	public void setAnnotations(Annotations annotations) {
		this.annotations = annotations;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_audience")
	public Audience getAudience() {
		return audience;
	}

	public void setAudience(Audience audience) {
		this.audience = audience;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_user")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
    
	
}