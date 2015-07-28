package edu.cmu.cs.lti.discoursedb.core.model.annotation;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import edu.cmu.cs.lti.discoursedb.core.model.system.DiscourseDB;

@Entity
@SelectBeforeUpdate 
@DynamicUpdate
@DynamicInsert
@Table(name="annotation_instance")
public class AnnotationInstance implements Serializable{

	private static final long serialVersionUID = 6699029374236146557L;
    
    private long id;
	
	private int beginOffset;
	
	private int endOffset;
	
	private String coveredText;
	
	private Timestamp startTime;
	
	private Timestamp endTime;	
	
	private AnnotationType type;	
	
	private DiscourseDB discoursedb;
	
	private Annotations annotationAggregate;
	
	private Set<Feature> features = new HashSet<Feature>();
	
	public AnnotationInstance(){}

	@Id
	@Column(name="id_annotation_instance", nullable=false)
    @GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name="begin_offset")
	public int getBeginOffset() {
		return beginOffset;
	}

	public void setBeginOffset(int beginOffset) {
		this.beginOffset = beginOffset;
	}

	@Column(name="end_offset")
	public int getEndOffset() {
		return endOffset;
	}

	public void setEndOffset(int endOffset) {
		this.endOffset = endOffset;
	}

	@Column(name="covered_text")
	public String getCoveredText() {
		return coveredText;
	}

	public void setCoveredText(String coveredText) {
		this.coveredText = coveredText;
	}

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

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "id_annotation_type")
	public AnnotationType getType() {
		return type;
	}

	public void setType(AnnotationType type) {
		this.type = type;
	}

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "id_discoursedb")
	public DiscourseDB getDiscoursedb() {
		return discoursedb;
	}

	public void setDiscoursedb(DiscourseDB discoursedb) {
		this.discoursedb = discoursedb;
	}

	@OneToMany(fetch=FetchType.LAZY,cascade=CascadeType.ALL,mappedBy="id_feature")
	public Set<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(Set<Feature> features) {
		this.features = features;
	}

	@ManyToOne(cascade=CascadeType.ALL) 
	@JoinColumn(name = "id_annotation")
	public Annotations getAnnotationAggregate() {
		return annotationAggregate;
	}

	public void setAnnotationAggregate(Annotations annotationAggregate) {
		this.annotationAggregate = annotationAggregate;
	}
	
	
	
}