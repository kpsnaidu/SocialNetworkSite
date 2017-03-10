package com.socialnetwork.controller;

import com.socialnetwork.dao.UserTweetDAO;
import com.socialnetwork.exception.UserNotYetRegisteredException;
import com.socialnetwork.model.UserTweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/v1/tweetservice")
public class UserTweetController {

    @Autowired
    private UserTweetDAO userTweetDAO;

    @RequestMapping(value = "/usertweet" , method = RequestMethod.POST)
    public ResponseEntity saveUserTweet(@RequestBody UserTweet tweet){

        userTweetDAO.saveUserTweet(tweet);
        return ResponseEntity.ok("success");
    }

    @RequestMapping(value = "/{followedUser}/follows/{followingUser}" , method = RequestMethod.POST)
    public ResponseEntity saveUserFollowsInfo(@PathVariable("followedUser") String followedUser,
            @PathVariable("followingUser") String followingUser){

        boolean followedUserExists = userTweetDAO.isUserExists(followedUser);
        boolean followingUserExists = userTweetDAO.isUserExists(followingUser);

        if(followedUserExists && followingUserExists) {
            userTweetDAO.saveUserFollowingInfo(followedUser, followingUser);
            return ResponseEntity.ok("success");
        } else{
            throw new UserNotYetRegisteredException("Please post atleast one message to be user registered");
        }
    }


    @RequestMapping(value = "/{user}/tweets" , method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<String>> getUserTweets(@PathVariable("user") String userName){
        List<String> tweets = userTweetDAO.findTweetsByUser(userName);
        return ResponseEntity.ok(tweets);
    }

    @RequestMapping(value = "/{user}/followingusertweets" , method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Map<String, List<String>>> getFollowingUserTweets(@PathVariable("user") String userName){
        Map<String, List<String>> tweets = userTweetDAO.findTweetsByFollowingUsersFor(userName);
        return ResponseEntity.ok(tweets);
    }

}
