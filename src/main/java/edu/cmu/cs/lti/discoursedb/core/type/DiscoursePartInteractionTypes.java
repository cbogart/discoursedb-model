package edu.cmu.cs.lti.discoursedb.core.type;

/**
 * Defines possible values for the type field in DiscoursePartInteractionType entities.
 * 
 * @author Oliver Ferschke
 */
public enum DiscoursePartInteractionTypes {
	/**
	 * A user joins a conversation (e.g. chat room)
	 */
	JOIN,
	/**
	 * A user leaves a conversation (e.g. chat room)
	 */
	LEAVE,
	/**
	 * A user indicates they are ready to move on in a conversation (e.g. in an agent supported chat)
	 */
	READY,
	/**
	 *  * A user or group is not ready to move on with a conversation
	 */
	UNREADY,
    /**
	 * Watch a collaboration (e.g. a Github repository)
	 */
	WATCH,
	/**
	 * Unwatch a collaboration (e.g. a Github repository)
	 */
	UNWATCH,
	/**
	 * Create a collaboration (e.g. a Github repository)
	 */
	CREATE,
	/**
	 * Delete a collaboration (e.g. a Github repository)
	 */
	DELETE, 
	/**
	 * Make a personal copy of a collaboration (e.g. a Github repository)
	 */
	FORK_FROM

}

