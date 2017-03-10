package com.socialnetwork.model;

import javax.persistence.*;

@Entity
@Table(name = "userfollows")
@IdClass(UserFollowsPrimaryKey.class)
public class UserFollows {

    @Id
    @Column(name = "followed_user_name")
    String followedUserName;
    @Id
    @Column(name = "following_user_name")
    String followingUserName;

    public UserFollows() {
    }

    public UserFollows(String followedUserName, String followingUserName) {
        this.followedUserName = followedUserName;
        this.followingUserName = followingUserName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserFollows that = (UserFollows) o;

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
