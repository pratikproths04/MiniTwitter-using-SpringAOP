package edu.sjsu.cmpe275.aop;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.sjsu.cmpe275.aop.TweetService;
import edu.sjsu.cmpe275.aop.TweetStats;

public class App {

	public static void main(String[] args) {
		//System.out.println("Inside main");
		System.out.println();
    	System.out.println("#################################################################");
    	System.out.println("\t CMPE 275 LAB1 Assignment- Spring AOP Application");
    	System.out.println("#################################################################");
    	System.out.println();
		ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
		TweetService tweetService = (TweetService) ctx.getBean("tweetService");
		TweetStats tweetStats = (TweetStats) ctx.getBean("tweetStats");
		try {
			
			tweetService.tweet("A", "Tweet1");
			tweetService.tweet("B", "Tweet1");
			tweetService.tweet("C", "Tweet1");
			tweetService.tweet("D", "Tweet1");
			tweetService.tweet("E", "Tweet1");
			tweetService.tweet("A", "Tweet2");
			tweetService.tweet("A", "Tweet3");
			tweetService.tweet("A", "Tweet4");
			tweetService.tweet("A", "Tweet5");
			tweetService.tweet("A", "Tweet6");
			tweetService.tweet("D", "This would be the longest 102 chars Tweet 111111111111111111111111111111111111111111111111111111111111");
		/*	tweetStats.resetStats();
			tweetService.tweet("B", "Tweet1");
			tweetService.tweet("D", "Tweet3");*/
			//tweetService.tweet("E", "This would be the longest 102 chars Tweet 111111111111111111111111111111111111111111111111111111111111");
			
			tweetService.follow("B", "A");
			tweetService.follow("B", "D");
			tweetService.follow("B", "E");
			tweetService.follow("A", "B");
			tweetService.follow("C", "B");
			tweetService.follow("D", "E");
			//tweetStats.resetStats();
			tweetService.follow("E", "B");
			tweetService.follow("D", "B");
			tweetService.follow("B", "C");
			tweetService.follow("D", "C");
			tweetService.follow("E", "C");
			tweetService.follow("A", "C");
			
			//tweetService.tweet("E", "This would be exceeding the limit and has 260 chars Tweet 1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");

			
			System.out.println();
        	System.out.println("#############################################################");
            System.out.println("\"Most Productive User\": " + tweetStats.getMostProductiveUser()+".");
            System.out.println("\"Most Followed User\": " + tweetStats.getMostActiveFollower()+".");
            System.out.println("\"Longest Tweet Message Length\": " + tweetStats.getLengthOfLongestTweet()+" characters.");
            System.out.println("#############################################################");
            System.out.println();
        }
		catch (IOException e) {
			/*System.out.println();
			System.out.println("############################################################################################");
        	System.out.println("Confirmed unavoidable "+e.toString()+" exception found!");
        	System.out.println("Already retried two times but it still exist which leads to absurdly aborting the entire application.");
        	System.out.println("############################################################################################");
        	System.out.println();*/
        }  
		catch (IllegalArgumentException e) {
			/*System.out.println();
			System.out.println("############################################################################################");
			System.out.println("Unavoidable Illegal Argument Exception found!");
        	System.out.println("Tweet message length exceeds 140 characters which leads to absurdly aborting the entire application.");
        	System.out.println("############################################################################################");
        	System.out.println();*/
        } 
		catch (Exception e) {
        	e.printStackTrace();
        }      
        finally{
        	((ClassPathXmlApplicationContext) ctx).close();
        }
    }
}
