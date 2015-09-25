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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import edu.cmu.cs.lti.discoursedb.core.model.TimedAnnotatableBaseEntityWithSource;
import edu.cmu.cs.lti.discoursedb.core.model.user.ContributionInteraction;

@Entity
@SelectBeforeUpdate 
@DynamicUpdate
@DynamicInsert
@Table(name="contribution")
public class Contribution extends TimedAnnotatableBaseEntityWithSource implements Serializable {

	private static final long serialVersionUID = -2489956863731652149L;

	private long id;
	
	private Content firstRevision;
	
	private Content currentRevision;
	
	private int upvotes;

	private int downvotes;

	private ContributionType type;
	
	private Set<DiscoursePartContribution> contributionPartOfDiscourseParts = new HashSet<DiscoursePartContribution>();

	private Set<ContributionAudience> contributionAudiences = new HashSet<ContributionAudience>();
	
	private Set<ContributionContext> contributionContexts = new HashSet<ContributionContext>();
	
	private Set<DiscourseRelation> sourceOfDiscourseRelations = new HashSet<DiscourseRelation>();

	private Set<DiscourseRelation> targetOfDiscourseRelations = new HashSet<DiscourseRelation>();
	
	private Set<ContributionInteraction> contributionInteractions = new HashSet<ContributionInteraction>();

	public Contribution(){}

	@Id
	@Column(name="id_contribution", nullable=false)
    @GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@OneToOne(cascade=CascadeType.ALL) 
	@JoinColumn(name = "fk_first_revision")
	public Content getFirstRevision() {
		return firstRevision;
	}

	public void setFirstRevision(Content firstRevision) {
		this.firstRevision = firstRevision;
	}

	@OneToOne(cascade=CascadeType.ALL) 
	@JoinColumn(name = "fk_current_revision")
	public Content getCurrentRevision() {
		return currentRevision;
	}

	public void setCurrentRevision(Content currentRevision) {
		this.currentRevision = currentRevision;
	}

	@ManyToOne(cascade=CascadeType.ALL) 
	@JoinColumn(name = "fk_contribution_type")
	public ContributionType getType() {
		return type;
	}

	public void setType(ContributionType type) {
		this.type = type;
	}

    @OneToMany(mappedBy = "contribution")
	public Set<DiscoursePartContribution> getContributionPartOfDiscourseParts() {
		return contributionPartOfDiscourseParts;
	}
    
	public void setContributionPartOfDiscourseParts(Set<DiscoursePartContribution> contributionPartOfDiscourseParts) {
		this.contributionPartOfDiscourseParts = contributionPartOfDiscourseParts;
	}

    public void addContributionPartOfDiscourseParts(DiscoursePartContribution discoursePartContribution){
    	contributionPartOfDiscourseParts.add(discoursePartContribution);
    }

    @OneToMany(mappedBy = "contribution")
	public Set<ContributionAudience> getContributionAudiences() {
		return contributionAudiences;
	}

	public void setContributionAudiences(Set<ContributionAudience> contributionAudiences) {
		this.contributionAudiences = contributionAudiences;
	}

	public void addContributionAudiences(ContributionAudience contributionAudience) {
		this.contributionAudiences.add(contributionAudience);
	}

    @OneToMany(mappedBy = "contribution")
	public Set<ContributionContext> getContributionContexts() {
		return contributionContexts;
	}

	public void setContributionContexts(Set<ContributionContext> contributionContexts) {
		this.contributionContexts = contributionContexts;
	}

	public void addContributionContexts(ContributionContext contributionContext) {
		this.contributionContexts.add(contributionContext);
	}

    @OneToMany(mappedBy="source")
	public Set<DiscourseRelation> getSourceOfDiscourseRelations() {
		return sourceOfDiscourseRelations;
	}

	public void setSourceOfDiscourseRelations(Set<DiscourseRelation> sourceOfDiscourseRelations) {
		this.sourceOfDiscourseRelations = sourceOfDiscourseRelations;
	}

	public void addSourceOfDiscourseRelations(DiscourseRelation sourceOfDiscourseRelation) {
		this.sourceOfDiscourseRelations.add(sourceOfDiscourseRelation);
	}

    @OneToMany(mappedBy="target")
	public Set<DiscourseRelation> getTargetOfDiscourseRelations() {
		return targetOfDiscourseRelations;
	}

	public void setTargetOfDiscourseRelations(Set<DiscourseRelation> targetOfDiscourseRelations) {
		this.targetOfDiscourseRelations = targetOfDiscourseRelations;
	}

	public void addTargetOfDiscourseRelations(DiscourseRelation targetOfDiscourseRelation) {
		this.targetOfDiscourseRelations.add(targetOfDiscourseRelation);
	}
	
    @OneToMany(mappedBy = "contribution")
	public Set<ContributionInteraction> getContributionInteractions() {
		return contributionInteractions;
	}

	public void setContributionInteractions(Set<ContributionInteraction> contributionInteractions) {
		this.contributionInteractions = contributionInteractions;
	}
	
	
	public int getUpvotes() {
		return upvotes;
	}
	
	public void setUpvotes(int upvotes) {
		this.upvotes = upvotes;
	}
	
	public int getDownvotes() {
		return downvotes;
	}
	
	public void setDownvotes(int downvotes) {
		this.downvotes = downvotes;
	}

}