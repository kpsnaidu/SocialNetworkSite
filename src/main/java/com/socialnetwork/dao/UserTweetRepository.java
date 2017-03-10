package com.socialnetwork.dao;

import com.socialnetwork.model.UserTweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface UserTweetRepository extends JpaRepository<UserTweet, String> {

    @Query("select message from UserTweet where userName = ?1 order by messagePostDate DESC")
    List<String> findUserTweets(String userName);
}