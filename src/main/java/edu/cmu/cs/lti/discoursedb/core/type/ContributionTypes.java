package edu.cmu.cs.lti.discoursedb.core.type;

/**
 * Defines possible values for the type field in ContributionType entities.
 * 
 * @author Oliver Ferschke
 */
public enum ContributionTypes {
/**
 * A post that starts a new thread
 */
THREAD_STARTER,
/**
 * A post that is part of a thread or a comment to a previous reply but not a thread starter.
 */
POST,
/**
 * An image posted in a bazaar chat
 */
BAZAAR_IMAGE,
/**
 * A private message, e.g. in a bazaar chat
 */
PRIVATE_MESSAGE,
/**
 * A regular tweet.
 */
TWEET,
/**
 * A ProSolo Goal Note
 */
GOAL_NOTE,
/**
 * A ProSolo NodeSocialActivity
 */
NODE_ACTIVITY,
/**
 * A ProSolo Node Comment
 */
NODE_COMMENT,
/**
 * A ProSolo NodeSocialActivity associated with a LearningGoal node
 */
LEARNING_GOAL,
/**
 * A ProSolo NodeSocialActivity associated with a Competence node
 */
COMPETENCE,
/**
 * A ProSolo NodeSocialActivity associated with a TargetLearningGoal node
 */
TARGET_LEARNING_GOAL,
/**
 * A ProSolo NodeSocialActivity associated with a TargetCompetence node
 */
TARGET_COMPETENCE,
/**
 * A ProSolo NodeSocialActivity associated with a ResourceActivity node
 */
RESOURCE_ACTIVITY,
/**
 * A ProSolo NodeSocialActivity associated with a TargetActivity node
 */
TARGET_ACTIVITY,
/**
 * A ProSolo NodeSocialActivity associated with a UploadAssignmentActivity node
 */
UPLOAD_ASSIGNMENT_ACTIVITY,
/**
 * A ProSolo SocialActivityComment
 */
SOCIAL_ACTIVITY_COMMENT



}
