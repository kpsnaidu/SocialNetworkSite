package com.socialnetwork.model;


import java.io.Serializable;

public class UserFollowsPrimaryKey implements Serializable {

    private String followedUserName;
    private String followingUserName;

    public UserFollowsPrimaryKey() {
    }

    public UserFollowsPrimaryKey(String followedUserName, String followingUserName) {
        this.followedUserName = followedUserName;
        this.followingUserName = followingUserName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserFollowsPrimaryKey that = (UserFollowsPrimaryKey) o;

        if (followedUserName != null ? !followedUserName.equals(that.followedUserName) : that.followedUserName != null)
            return false;
        return followingUserName != null ? followingUserName.equals(that.followingUserName) : that.followingUserName == null;

    }

    @Override
    public int hashCode() {
        int result = followedUserName != null ? followedUserName.hashCode() : 0;
        result = 31 * result + (followingUserName != null ? followingUserName.hashCode() : 0);
        return result;
    }
}