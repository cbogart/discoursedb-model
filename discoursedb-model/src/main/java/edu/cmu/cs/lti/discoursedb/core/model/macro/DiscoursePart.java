package edu.cmu.cs.lti.discoursedb.core.model.macro;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import edu.cmu.cs.lti.discoursedb.core.model.TimedAnnotatableBaseEntityWithSource;

@Entity
@SelectBeforeUpdate 
@DynamicUpdate
@DynamicInsert
@Table(name="discourse_part")
public class DiscoursePart extends TimedAnnotatableBaseEntityWithSource implements Serializable{

	private static final long serialVersionUID = -7341483666466458901L;

	private long id;
	
	private String name;
	
	private DiscoursePartType type;
	
	private Set<DiscourseToDiscoursePart> discourseToDiscourseParts = new HashSet<DiscourseToDiscoursePart>();

	private Set<DiscoursePartContribution> discoursePartContributions = new HashSet<DiscoursePartContribution>();
	
	private Set<DiscoursePartRelation> sourceOfDiscoursePartRelations = new HashSet<DiscoursePartRelation>();

	private Set<DiscoursePartRelation> targetOfDiscoursePartRelations = new HashSet<DiscoursePartRelation>();
	
	public DiscoursePart(){}
	
	@Id
	@Column(name="id_discourse_part", nullable=false)
    @GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "fk_discourse_part_type")
	public DiscoursePartType getType() {
		return type;
	}

	public void setType(DiscoursePartType type) {
		this.type = type;
	}
	
    @OneToMany(mappedBy = "discoursePart")
	public Set<DiscourseToDiscoursePart> getDiscourseToDiscourseParts() {
		return discourseToDiscourseParts;
	}

    public void setDiscourseToDiscourseParts(Set<DiscourseToDiscoursePart> discourseToDiscourseParts) {
		this.discourseToDiscourseParts = discourseToDiscourseParts;
	}

    @OneToMany(mappedBy = "discoursePart")
	public Set<DiscoursePartContribution> getDiscoursePartContributions() {
		return discoursePartContributions;
	}

	public void setDiscoursePartContributions(Set<DiscoursePartContribution> discoursePartContributions) {
		this.discoursePartContributions = discoursePartContributions;
	}

	public void addDiscoursePartContribution(DiscoursePartContribution discoursePartContribution) {
		discoursePartContributions.add(discoursePartContribution);
	}

	
    @OneToMany(mappedBy="source")
	public Set<DiscoursePartRelation> getSourceOfDiscoursePartRelations() {
		return sourceOfDiscoursePartRelations;
	}

	public void setSourceOfDiscoursePartRelations(Set<DiscoursePartRelation> sourceOfDiscoursePartRelations) {
		this.sourceOfDiscoursePartRelations = sourceOfDiscoursePartRelations;
	}

    @OneToMany(mappedBy="target")
	public Set<DiscoursePartRelation> getTargetOfDiscoursePartRelations() {
		return targetOfDiscoursePartRelations;
	}

	public void setTargetOfDiscoursePartRelations(Set<DiscoursePartRelation> targetOfDiscoursePartRelations) {
		this.targetOfDiscoursePartRelations = targetOfDiscoursePartRelations;
	}
	
}
