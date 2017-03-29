package edu.sjsu.cmpe275.aop;

import java.io.IOException;

public class TweetServiceImpl implements TweetService {

    private int i=0;

    public void tweet(String user, String message) throws IllegalArgumentException, IOException {
    	//System.out.println("Inside tweet");
    	//System.out.println("Length of Message by user "+user+" is "+message.length());
    	/*while(i<1){
    		i++;
    		throw new IOException();
    	}*/
    	if(message.length() > 140)
    		throw new IllegalArgumentException(); 
    }

    public void follow(String follower, String followee) throws IOException {
    	//System.out.println("Inside follow");
    	/*while(i<1){
    		i++;
    		throw new IOException();
    	}*/
    }

}
