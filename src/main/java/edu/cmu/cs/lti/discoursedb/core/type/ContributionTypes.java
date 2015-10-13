package edu.cmu.cs.lti.discoursedb.core.type;

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
 * A regular tweet.
 */
TWEET,
/**
 * A ProSolo Goal Note
 */
GOAL_NOTE

}
