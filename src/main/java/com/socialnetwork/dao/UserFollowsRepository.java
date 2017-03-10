package com.socialnetwork.dao;


import com.socialnetwork.model.UserFollows;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserFollowsRepository extends JpaRepository<UserFollows, String> {

    @Query("select followingUserName from UserFollows where followedUserName = ?1")
    List<String> findFollowingUsers(String userName);

}
