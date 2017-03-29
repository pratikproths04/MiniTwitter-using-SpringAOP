package edu.sjsu.cmpe275.aop;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class TweetStatsImpl implements TweetStats {
	/**
	 * Static variable to hold the longest tweet length. Static as it can be easily accessible 
	 * anywhere by using its class name without a need of creating objects.
	 */
	public static int longestTweetLength = 0;
	
   /**
	 * Static map to hold the user as a key and followers as values in set. Especially using
	 * TreeMap as to fetch user as a key in alphabetical order.
	 */
	public static TreeMap<String, Set<String>> usersFollowerMap = new TreeMap<String, Set<String>>();
	
   /**
	 * Static map to hold all the users as key and length of corresponding tweet message as values.
	 * Especially using TreeMap as to fetch user as a key in alphabetical order.
	 */
	public static TreeMap<String, Integer> mostActiveUserMap = new TreeMap<String, Integer>();
	
   /**
	 * Retry counter for retrying against network failure.
	 */
	public static int retryCounter = 2;
	
   /**
	 * retryFlag is to enable retry operations for network failure.
	 */
	public static boolean retryFlag = true;
	
   /**
	 * noOtherExceptionFoundInFollowFlag is to check if follow method has any exception other than IOException.
	 */
	public static boolean noOtherExceptionFoundInFollowFlag = true;
	
   /**
	 * confirmedIOExceptionFlag to check whether IOException still persist after two guaranteed retry.
	 */
	public static boolean confirmedIOExceptionFlag = false;
	
   /**
	 * IllegalArgumentExceptionFlag flag to check whether IllegalArgumentException persist or not.
	 */
	public static boolean illegalArgumentExceptionFlag = false;
	
   /**
	 * This function is used to reset all the static variable. It is clearing the
	 * maps and setting the longest tweet length equal to 0.
	 */
	public void resetStats() {
		//System.out.println("Inside resetStats");
		longestTweetLength = 0;
		usersFollowerMap.clear();
		mostActiveUserMap.clear();
	}
	
   /**
	 * @return the length of longest message attempted since the beginning or
	 * last reset. Can be more than 140. If no messages were successfully tweeted, 
	 * return 0.
	 */
	public int getLengthOfLongestTweet() {
		//System.out.println("Inside getLengthOfLongestTweet");
		return longestTweetLength;
	}
	
   /**
	 * @return the user followed by maximum number of different users since the beginning
	 *  or last reset. If there is a tie, return the 1st of such users based on alphabetical 
	 *  order. If the follow action did not succeed, then it does not count into the stats.
	 */
	public String getMostActiveFollower() {
		//System.out.println("Inside getMostActiveFollower");
		int countMaxUserFollowings = 0;
		String userMaxFollowed = null;
		if (usersFollowerMap.size() != 0)
		{
			for (Map.Entry<String, Set<String>> entry : usersFollowerMap.entrySet())
			{
				/*
				 * Comparing the count of followers of current user with max count followers of 
				 * any user value to find the most followed one.
				 */
				if (entry.getValue().size() > countMaxUserFollowings)
				{
					userMaxFollowed = entry.getKey();
					countMaxUserFollowings = entry.getValue().size();
				}
			}
		}
		return userMaxFollowed;
	}
	
   /**
	 * @return the most productive user determined by the longest length among all the
	 * messages successfully tweeted since the beginning or last reset. If there
	 * is a tie, return the 1st of such users based on alphabetical order.
	 */
	public String getMostProductiveUser() {
		//System.out.println("Inside getMostProductiveUser");
		int countMaxTweetLength = 0;
		String mostActiveUser = null;
		if (mostActiveUserMap.size() != 0)
		{
			for (Map.Entry<String, Integer> entry : mostActiveUserMap.entrySet())
			{
				String currentUser = entry.getKey();
				int currentSize = entry.getValue();
				/*
				 * Comparing the current values with active user value to find the most
				 * active one.
				 */
				if (currentSize > countMaxTweetLength)
				{
					mostActiveUser = currentUser;
					countMaxTweetLength = currentSize;
				}
			}
		}
		return mostActiveUser;
	}
}