#Spring AOP Mini Twitter

The project implements the retry and stats concerns of a tweeting service through Aspect Oriented Programming (AOP).

The tweet service is defined as follows:

```java
package edu.sjsu.cmpe275.aop;

import java.io.IOException;

public interface TweetService {
   /**
    * @throws IllegalArgumentException if the message is more than 140 characters as measured by string length.
    * @throws IOException if there is a network failure
    */
   void tweet(String user, String message) throws IllegalArgumentException, IOException;
   /**
    * @throws IOException if there is a network failure
    */
   void follow(String follower, String followee) throws IOException;
}
```

Since network failure happens relatively frequently, tweets are automatically retried for up to two times for a network failure (indicated by an IOException).

Here is the TweetStats service:

```java
public interface TweetStats {
   
   /**
    * reset all the three measurements.
    */
   void resetStats();
   
   /**
    * @return the length of longest message successfully tweeted since the beginning or last reset. If no messages were successfully tweeted, return 0.
    */
   int getLengthOfLongestTweet();
   /**
    * @return the user who has attempted to follow the biggest number of different users since
    * the beginning or last reset. If there is a tie, return the 1st of such users based on
    * alphabetical order. Even if the follow action did not succeed, it still counts toward the stats.
    * If no users attempted to follow anybody, return null.  
    */
   String getMostActiveFollower();
   /**
    * The most productive user is determined by the total length of all the messages successfully tweeted since the beginning
    * or last reset. If there is a tie, return the 1st of such users based on alphabetical order. If no users successfully tweeted, return null.
    * @return the most productive user.
    */
   String getMostProductiveUser();
}
```

The two concerns are in the two files: 

RetryAspect.java and StatsAspect.java.

###Example Stats

The following examples are assuming stats are reset() before running every single example.

1. Tweet message as tweet(“foo”,”barbar”). Then getLengthOfLongestTweet() returns 6.
2. Alice follows Bob, Bob follows Charlie (but fails to do so), and Bob follows Alice. getMostActiveFollower() returns “Bob”.
3. Successfully tweet a message ("Alice","[any message <= 140 chars]"), then getMostProductiveUser() returns “Alice”.
