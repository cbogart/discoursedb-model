package edu.cmu.cs.lti.discoursedb.core.model.macro;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import edu.cmu.cs.lti.discoursedb.core.model.BaseTypeEntity;

@Entity
@SelectBeforeUpdate 
@DynamicUpdate
@DynamicInsert
@Table(name="context_type")
public class ContextType extends BaseTypeEntity implements Serializable {

	private static final long serialVersionUID = 9191265196419948023L;

	private long id;
	
	private Set<Context> contexts = new HashSet<Context>();
	
	public ContextType(){}

	@Id
	@Column(name="id_content_type", nullable=false)
    @GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="type")
	public Set<Context> getContexts() {
		return contexts;
	}

	public void setContexts(Set<Context> contexts) {
		this.contexts = contexts;
	}
}