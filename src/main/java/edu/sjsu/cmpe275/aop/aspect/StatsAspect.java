package edu.sjsu.cmpe275.aop.aspect;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import edu.sjsu.cmpe275.aop.TweetStatsImpl;
@Component
@Aspect
@Order(0)
public class StatsAspect {
	/** 
	* This is the method declares after returning advice which I would like to execute after execution 
	* of follow method of TweetServiceImpl class successfully without any errors.  
	*/
	@AfterReturning("execution(* edu.sjsu.cmpe275.aop.TweetServiceImpl.follow(..)) && args(follower, followee)")
	public void followAfterAdvice(JoinPoint joinPoint, String follower, String followee){
		//System.out.println("Inside followAfterAdvice");
		/*
		 * If there is a normal flow of execution without any errors then executes below statements.
		 */
		Set<String> usersFollowerSet;
		TreeMap<String, Set<String>> usersFollowerMap = TweetStatsImpl.usersFollowerMap;
		if (usersFollowerMap.containsKey(follower))
			usersFollowerSet = usersFollowerMap.get(follower);
		else
			usersFollowerSet = new HashSet<String>();
		usersFollowerSet.add(followee);
		usersFollowerMap.put(follower, usersFollowerSet);
	}
	
}
