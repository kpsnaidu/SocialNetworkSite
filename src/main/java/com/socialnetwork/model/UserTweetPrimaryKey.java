package com.socialnetwork.model;

import java.io.Serializable;


public class UserTweetPrimaryKey implements Serializable {

    private String userName;
    private String message;

    public UserTweetPrimaryKey() {
    }

    public UserTweetPrimaryKey(String userName, String message) {
        this.userName = userName;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserTweetPrimaryKey that = (UserTweetPrimaryKey) o;

        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        return message != null ? message.equals(that.message) : that.message == null;

    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
