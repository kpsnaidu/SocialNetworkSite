package com.socialnetwork.dao;


import com.socialnetwork.model.UserFollows;
import com.socialnetwork.model.UserTweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class UserTweetDAO {

    @Autowired
    private UserTweetRepository userTweetRepository;

    @Autowired
    private  UserFollowsRepository userFollowsRepository;


    public void saveUserTweet(UserTweet tweet) {
        userTweetRepository.save(tweet);
    }

    public List<String> findTweetsByUser(String userName) {
        return userTweetRepository.findUserTweets(userName);
    }

    public void saveUserFollowingInfo(String followedUser, String followingUser) {
        userFollowsRepository.save(new UserFollows(followedUser, followingUser));
    }


    public Map<String, List<String>> findTweetsByFollowingUsersFor(String userName) {
        Map<String, List<String>> followingUsersTweets = new HashMap<>();

        List<String> followingUsers = userFollowsRepository.findFollowingUsers(userName);
        followingUsers.forEach(user ->
            followingUsersTweets.put(user, findTweetsByUser(user))
        );
        return followingUsersTweets;
    }

    public boolean isUserExists(String userName) {
        List<String> userTweets = userTweetRepository.findUserTweets(userName);
        return userTweets.size() > 0;
    }
}
