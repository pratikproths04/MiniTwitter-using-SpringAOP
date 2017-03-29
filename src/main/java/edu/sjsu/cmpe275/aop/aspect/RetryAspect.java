package edu.sjsu.cmpe275.aop.aspect;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import edu.sjsu.cmpe275.aop.TweetStatsImpl;

import org.aspectj.lang.annotation.Around;
@Component
@Aspect
@Order(1)
public class RetryAspect {
	/** 
	* This is the method declares around advice which I would like to execute for all methods
	* of TweetServiceImpl class. It checks for network failure and adds the feature to automatically 
	* retry for up to two times for a network failure (indicated by an IOException). (Please note the 
	* two retries are in addition to the original failed invocation). 
	* 
	* For tweet method, it checks before calling method that message length is less than or equal to 
	* 140 characters and then check if it is longest or not then it calls the tweet method then it checks if 
	* there is an IOException in tweet method if so retry for two more times. If IOException still persist 
	* give a confirmation message to user and reject that instruction as after two retries the exception 
	* still persist. If within two retries the exception gets resolved consider that instruction and then 
	* there is a normal flow of execution. If there is an illegalArgumentException reject due to any 
	* instruction, reject that instruction. 
	* 
	* For follow method once the method is being called then it checks if there is an IOException in follow
	* method if so retry for two more times. If IOException still persist give a confirmation message to user
	* and reject that instruction as after two retries the exception still persist. If within two retries the
	* exception gets resolved consider that instruction and then there is a normal flow of execution. If it 
	* encounters any other exception then set a flag and then perform the required operations to get correct
	* results even if it fails.
	*/
	@Around("within(edu.sjsu.cmpe275.aop.TweetServiceImpl) && args(param1, param2)")
	public void commonAroundAdvice(ProceedingJoinPoint proceedingJoinPoint, String param1, String param2){
		//System.out.println("Inside commonAroundAdvice");
		/*
		 * Executes below if statement before calling the tweet method. 
		 */
		if(proceedingJoinPoint.getSignature().getName().toString().equalsIgnoreCase("tweet")){
			if (param2.length() <= 140)
			{
				/*
				 * Checks for longest tweet message length. 
				 */
				if (param2.length() > TweetStatsImpl.longestTweetLength)
					TweetStatsImpl.longestTweetLength = param2.length();
			}
		}
		try{
			/*
			 * Below statement calls the method on which this advice is declared. 
			 */
			proceedingJoinPoint.proceed();
			if(proceedingJoinPoint.getSignature().getName().toString().equalsIgnoreCase("tweet")){
				if (param2.length() <= 140)
				{
					TreeMap<String, Integer> mostActiveUserMap = TweetStatsImpl.mostActiveUserMap;
					mostActiveUserMap.put(param1, param2.length());
				}
			}
		}catch(IllegalArgumentException iae){
			System.out.println("Found IllegalArgumentException!");
			TweetStatsImpl.illegalArgumentExceptionFlag = true;
		}catch(IOException ioe){
			System.out.println("Found IO Exception!");
			TweetStatsImpl.retryCounter = 2;
			while (TweetStatsImpl.retryFlag)
			{
				if (TweetStatsImpl.retryCounter > 0){
					try {
						System.out.println("Now retrying calling tweet method only for "+TweetStatsImpl.retryCounter+" time.");
						/*
						 * Below statement calls the method on which this advice is declared while retrying. 
						 */
						proceedingJoinPoint.proceed();
						TweetStatsImpl.retryFlag = false;
						/*
						 * Executes below statements after calling tweet method if there are no exceptions found. 
						 */
						if(proceedingJoinPoint.getSignature().getName().toString().equalsIgnoreCase("tweet")){
							if (param2.length() <= 140)
							{
								TreeMap<String, Integer> mostActiveUserMap = TweetStatsImpl.mostActiveUserMap;
								mostActiveUserMap.put(param1, param2.length());
							}
						}
					}catch(IllegalArgumentException iae){
						TweetStatsImpl.illegalArgumentExceptionFlag = true;
					}catch (IOException e1) {
						System.out.println("Found IO Exception!");
						TweetStatsImpl.retryCounter--;
						if(TweetStatsImpl.retryCounter == 0)
							TweetStatsImpl.confirmedIOExceptionFlag = true;
					}catch(Throwable ex){
						/*
						 * To display exception name apart from IOException if there is any other exception exception
						 * found in follow method while retrying.  
						 */
						if(proceedingJoinPoint.getSignature().getName().toString().equalsIgnoreCase("follow")){
							System.out.println("New Exception found "+ex.toString()+" in follow method while retrying.");
							TweetStatsImpl.noOtherExceptionFoundInFollowFlag = false;
						}
					}finally{
						/*
						 * To display the message once it is confirmed that IOException still persist after two retries
						 * in tweet method. 
						 */
						if(proceedingJoinPoint.getSignature().getName().toString().equalsIgnoreCase("tweet")){
							if(TweetStatsImpl.retryCounter == 0){
								if(TweetStatsImpl.confirmedIOExceptionFlag){
									System.out.println();
									System.out.println("#######################################################################");
						        	System.out.println("Confirmed on both retry found IOException!!!");
						        	System.out.println("Leads to not considering the current tweet instruction for execution.");
						        	System.out.println("#######################################################################");
						        	System.out.println();
						        	TweetStatsImpl.confirmedIOExceptionFlag = false;
								}
							}
						}
					}
				}
				else
				{
					TweetStatsImpl.retryFlag = false;		
				}
			}TweetStatsImpl.retryFlag = true;
		}catch(Throwable ex){
			/*
			 * To display exception name apart from IOException if there is any other exception exception
			 * found in follow method.  
			 */
			if(proceedingJoinPoint.getSignature().getName().toString().equalsIgnoreCase("follow")){
				System.out.println("New Exception found "+ex.toString()+" in follow method!!");
				TweetStatsImpl.noOtherExceptionFoundInFollowFlag = false;
			}
		}finally{
			/*
			 * Display it when IllegalArgumentException occurs.
			 */
			if(proceedingJoinPoint.getSignature().getName().toString().equalsIgnoreCase("tweet")){
				if(TweetStatsImpl.illegalArgumentExceptionFlag){
					System.out.println();
					System.out.println("######################################################################");
					System.out.println("Illegal Argument Exception found!");
		        	System.out.println("Tweet message length exceeds 140 characters!!");
		        	System.out.println("Leads to not considering the current tweet instruction for execution.");
		        	System.out.println("######################################################################");
		        	System.out.println();
				}	
			}
			
			if(proceedingJoinPoint.getSignature().getName().toString().equalsIgnoreCase("follow")){
				/*
				 * To display the message once it is confirmed that IOException still persist after two retries
				 * in follow method. 
				 */
				if(TweetStatsImpl.confirmedIOExceptionFlag){
					System.out.println();
					System.out.println("#######################################################################");
		        	System.out.println("Confirmed on both retry found IOException!!!");
		        	System.out.println("Leads to not considering the current follow instruction for execution.");
		        	System.out.println("#######################################################################");
		        	System.out.println();
		        	TweetStatsImpl.confirmedIOExceptionFlag = false;
				}else if(!TweetStatsImpl.noOtherExceptionFoundInFollowFlag){
					/*
					 * If there is any other error found in follow method then also perform the required operation,
					 * to get the correct result even if it fails due to some error found. 
					 */
					Set<String> usersFollowerSet;
					TreeMap<String, Set<String>> usersFollowerMap = TweetStatsImpl.usersFollowerMap;
					if (usersFollowerMap.containsKey(param1))
						usersFollowerSet = usersFollowerMap.get(param1);
					else
						usersFollowerSet = new HashSet<String>();
					usersFollowerSet.add(param2);
					usersFollowerMap.put(param1, usersFollowerSet);
				}
			}
		}
	}	
}