package edu.cmu.cs.lti.discoursedb.core.service.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import edu.cmu.cs.lti.discoursedb.core.model.macro.Contribution;
import edu.cmu.cs.lti.discoursedb.core.model.macro.Discourse;
import edu.cmu.cs.lti.discoursedb.core.model.macro.DiscoursePart;
import edu.cmu.cs.lti.discoursedb.core.model.system.DataSourceInstance;
import edu.cmu.cs.lti.discoursedb.core.model.user.ContributionInteraction;
import edu.cmu.cs.lti.discoursedb.core.model.user.ContributionInteractionType;
import edu.cmu.cs.lti.discoursedb.core.model.user.DiscoursePartInteraction;
import edu.cmu.cs.lti.discoursedb.core.model.user.DiscoursePartInteractionType;
import edu.cmu.cs.lti.discoursedb.core.model.user.User;
import edu.cmu.cs.lti.discoursedb.core.model.user.UserRelation;
import edu.cmu.cs.lti.discoursedb.core.model.user.UserRelationType;
import edu.cmu.cs.lti.discoursedb.core.repository.user.ContributionInteractionRepository;
import edu.cmu.cs.lti.discoursedb.core.repository.user.ContributionInteractionTypeRepository;
import edu.cmu.cs.lti.discoursedb.core.repository.user.DiscoursePartInteractionRepository;
import edu.cmu.cs.lti.discoursedb.core.repository.user.DiscoursePartInteractionTypeRepository;
import edu.cmu.cs.lti.discoursedb.core.repository.user.UserRelationRepository;
import edu.cmu.cs.lti.discoursedb.core.repository.user.UserRelationTypeRepository;
import edu.cmu.cs.lti.discoursedb.core.repository.user.UserRepository;
import edu.cmu.cs.lti.discoursedb.core.service.system.DataSourceService;
import edu.cmu.cs.lti.discoursedb.core.type.ContributionInteractionTypes;
import edu.cmu.cs.lti.discoursedb.core.type.DataSourceTypes;
import edu.cmu.cs.lti.discoursedb.core.type.DiscoursePartInteractionTypes;
import edu.cmu.cs.lti.discoursedb.core.type.UserRelationTypes;

@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private DataSourceService dataSourceService;

	@Autowired
	private UserRelationRepository userRelationRepo;

	@Autowired
	private UserRelationTypeRepository userRelationTypeRepo;
	
	@Autowired
	private ContributionInteractionRepository contribInteractionRepo;
	
	@Autowired
	private DiscoursePartInteractionRepository discoursePartInteractionRepo;
	
	@Autowired
	private ContributionInteractionTypeRepository contribInteractionTypeRepo;

	@Autowired
	private DiscoursePartInteractionTypeRepository discoursePartInteractionTypeRepo;

    public Optional<User> findUserByDiscourseAndSourceIdAndSourceType(Discourse discourse, String sourceId, DataSourceTypes type) {
		return Optional.ofNullable(userRepo.findOne(
						UserPredicates.hasDiscourse(discourse).and(
						UserPredicates.hasSourceId(sourceId)).and(
						UserPredicates.hasDataSourceType(type))));
    }

    public Optional<User> findUserByDiscourseAndSourceIdAndDataSet(Discourse discourse, String sourceId, String dataSetName) {
		return Optional.ofNullable(userRepo.findOne(
						UserPredicates.hasDiscourse(discourse).and(
						UserPredicates.hasSourceId(sourceId)).and(
						UserPredicates.hasDataSet(dataSetName))));
    }

    public Optional<User> findUserByDiscourseAndSourceId(Discourse discourse, String sourceId) {
		return Optional.ofNullable(userRepo.findOne(
						UserPredicates.hasDiscourse(discourse).and(
						UserPredicates.hasSourceId(sourceId))));
    }

    public Optional<User> findUserBySourceIdAndUsername(String sourceId, String username) {
		return Optional.ofNullable(userRepo.findOne(
						UserPredicates.hasSourceId(sourceId).and(
						UserPredicates.hasUserName(username))));
    }

    public Iterable<User> findUsersBySourceId(String sourceId) {
		return userRepo.findAll(UserPredicates.hasSourceId(sourceId));
    }
	
	/**
	 * Returns a User object with the given username and a given discourse if it
	 * exists or creates a new User entity with that username and that discourse.
	 * 
	 * @param discourse
	 *            the discourse in which the user was active
	 * @param username
	 *            username of the requested user
	 * @return the User object with the given username - either retrieved or
	 *         newly created
	 */
	public User createOrGetUser(Discourse discourse, String username) {
		Optional<User> existingUser = Optional.ofNullable(userRepo.findOne(
				UserPredicates.hasDiscourse(discourse).and(
				UserPredicates.hasUserName(username))));	
		User curUser;
		if (existingUser.isPresent()) {
			return existingUser.get();
		} else {
			curUser = new User(discourse);
			curUser.setUsername(username);
			return save(curUser);			
		}
	}

	/**
	 * Returns a User object with the given source id and username if it exists
	 * or creates a new User entity with that username, stores it in the
	 * database and returns the entity object.
	 * 
	 * @param sourceId
	 *            the user id assigned by the source system
	 * @param sourceDescriptor
	 *            defines where the sourceId is defined in the source data (e.g. "user.id" if it is a user table with field id)
	 * @param username
	 *            the username of the requested user
	 * @return the User object with the given username and source id- either retrieved or
	 *         newly created
	 */
	public User createOrGetUser(Discourse discourse, String username, String sourceId, String sourceIdDescriptor, DataSourceTypes dataSourceType, String dataSetName) {
		Optional<User> existingUser = findUserByDiscourseAndSourceIdAndDataSet(discourse, sourceId, dataSetName);
		User curUser;
		if (existingUser.isPresent()) {
			curUser = existingUser.get();
		} else {
			curUser = new User(discourse);
			curUser.setUsername(username);
			curUser = userRepo.save(curUser);
			dataSourceService.addSource(curUser, new DataSourceInstance(sourceId,sourceIdDescriptor,dataSourceType,dataSetName));	
		}
		return curUser;
	}

	/**
	 * Creates a new ContributionInteraction of the provided type and applies
	 * it to the provided user and contribution. A connection to a content
	 * entity is optional and is not established by this method. This is
	 * necessary e.g. for REVERT interactions.
	 * 
	 * @param user
	 *            the user to interact with the provided contribution
	 * @param contrib
	 *            the contribution the provided user interacts with
	 * @param type
	 *            the type of the interaction
	 * @return the ContributionInteraction object after being saved to the
	 *         database. If it already existed, the existing entity will be retrieved and returned.
	 */
	public ContributionInteraction createContributionInteraction(User user, Contribution contrib, ContributionInteractionTypes type) {

		//Retrieve type or create if it doesn't exist in db
		ContributionInteractionType contribInteractionType =null;
		Optional<ContributionInteractionType> existingContribInteractionType = contribInteractionTypeRepo.findOneByType(type.name());
		if(existingContribInteractionType.isPresent()){
			contribInteractionType=existingContribInteractionType.get();
		}else{
			contribInteractionType = new ContributionInteractionType();
			contribInteractionType.setType(type.name());
			contribInteractionTypeRepo.save(contribInteractionType);			
		}

		//Retrieve ContributionInteraction or create if it doesn't exist in db
		Optional<ContributionInteraction> existingContribInteraction =  contribInteractionRepo.findOneByUserAndContributionAndType(user, contrib, contribInteractionType);		
		if(existingContribInteraction.isPresent()){
			return existingContribInteraction.get();
		}else{
			ContributionInteraction contribInteraction = new ContributionInteraction();
			contribInteraction.setContribution(contrib);
			contribInteraction.setUser(user);
			contribInteraction.setType(contribInteractionType);
			return contribInteractionRepo.save(contribInteraction);			
		}
	}
	
	/**
	 * Creates a new DiscoursePartInteraction of the provided type and applies
	 * it to the provided user and discoursepart. 
	 * 
	 * @param user
	 *            the user to interact with the provided contribution
	 * @param dp
	 *            the discoursepart the provided user interacts with
	 * @param type
	 *            the type of the interaction
	 * @return the DiscoursePartInteraction object after being saved to the
	 *         database. If it already existed, the existing entity will be retrieved and returned.
	 */
	public DiscoursePartInteraction createDiscoursePartInteraction(User user, DiscoursePart dp, DiscoursePartInteractionTypes type) {

		//Retrieve type or create if it doesn't exist in db
		DiscoursePartInteractionType dpInteractionType =null;
		Optional<DiscoursePartInteractionType> existingDpInteractionType = discoursePartInteractionTypeRepo.findOneByType(type.name());
		if(existingDpInteractionType.isPresent()){
			dpInteractionType=existingDpInteractionType.get();
		}else{
			dpInteractionType = new DiscoursePartInteractionType();
			dpInteractionType.setType(type.name());
			discoursePartInteractionTypeRepo.save(dpInteractionType);			
		}

		//Retrieve ContributionInteraction or create if it doesn't exist in db
		Optional<DiscoursePartInteraction> existingDPInteraction =  discoursePartInteractionRepo.findOneByUserAndDiscoursePartAndType(user, dp, dpInteractionType);		
		if(existingDPInteraction.isPresent()){
			return existingDPInteraction.get();
		}else{
			DiscoursePartInteraction dpInteraction = new DiscoursePartInteraction();
			dpInteraction.setDiscoursePart(dp);
			dpInteraction.setUser(user);
			dpInteraction.setType(dpInteractionType);
			return discoursePartInteractionRepo.save(dpInteraction);			
		}
	}

	/**
	 * Creates a new UserRelation of the provided type and applies
	 * it to the provided source and target user. 
	 * 
	 * @param sourceUser
	 *            the user that establishes the relation (e.g. follower))
	 * @param targetUser
	 *            the user that is the target of the relation (e.g. followee))
	 * @param type
	 *            the type of the relation
	 * @return the UserRelation object after being saved to the
	 *         database. If it already existed, the existing entity will be retrieved and returned.
	 */
	public UserRelation createUserRelation(User sourceUser, User targetUser, UserRelationTypes type) {

		//Retrieve type or create if it doesn't exist in db
		UserRelationType userRelationType =null;
		Optional<UserRelationType> existingUserRelationType = userRelationTypeRepo.findOneByType(type.name());
		if(existingUserRelationType.isPresent()){
			userRelationType=existingUserRelationType.get();
		}else{
			userRelationType = new UserRelationType();
			userRelationType.setType(type.name());
			userRelationTypeRepo.save(userRelationType);			
		}

		//Retrieve UserRelation or create if it doesn't exist in db
		Optional<UserRelation> existingUserRelation =  userRelationRepo.findOneBySourceAndTargetAndType(sourceUser,targetUser, userRelationType);		
		if(existingUserRelation.isPresent()){
			return existingUserRelation.get();
		}else{
			UserRelation userRelation = new UserRelation();
			userRelation.setSource(sourceUser);
			userRelation.setTarget(targetUser);
			userRelation.setType(userRelationType);
			return userRelationRepo.save(userRelation);			
		}
	}
	
	/**
	 * This method is a convenience method to build a single real name from a first and a last name (each of which might be empty)
	 * 
	 * Sets the real name of the given user based on a given first and last name.
	 * Either first or last name (or both) may be empty - the real name is assembled accordingly.
	 * If a realname was already set before, no operation is performed. The name is not updated.
	 * 
	 * 
	 * @param user the user to update
	 * @param firstName the first name of the user
	 * @param lastName the last name of the user
	 * @return the user with updated or unchanged realname
	 */
	public User setRealname(User user, String firstName, String lastName){
		if(firstName==null)firstName="";
		if(lastName==null)lastName="";
		
		if(user.getRealname()==null||user.getRealname().isEmpty()){
			if(firstName.isEmpty()){
				if(!lastName.isEmpty()){
					user.setRealname(lastName);
				}
			}else{
				if(lastName.isEmpty()){
					user.setRealname(firstName);
				}else{
					user.setRealname(firstName+" "+lastName);
				}						
			}						
			return save(user);
		}
		return user;
	}
	
	
	
	
	/**
	 * Calls the save method of the user repository, saves the provided User entity and returns it after the save process
	 * @param user the user entity to save
	 * @return the user entity after the save process
	 */
	public User save(User user){
		return userRepo.save(user);
	}

	/**
	 * Removes user from all their discourses and then deletes it by calling
	 * the delete method in the user repository.
	 *  
	 * @param user the user entity to delete
	 */
	public void delete(User user){				
		for(Discourse d:user.getDiscourses()){
			user.removeDiscourse(d);
		}
		userRepo.delete(user);
	}

}
