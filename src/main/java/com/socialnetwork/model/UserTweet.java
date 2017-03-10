package com.socialnetwork.model;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "usertweet")
@IdClass(UserTweetPrimaryKey.class)
public class UserTweet {

    @Id
    @Column(name = "user_name")
    public String userName;

    @Id
    @Column(name = "message")
    public String message;

    @Column(name = "message_post_date")
    public Date messagePostDate;

    public UserTweet() {
    }

    public UserTweet(String userName, String message, Date messagePostDate) {
        this.userName = userName;
        this.message = message;
        this.messagePostDate = messagePostDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getMessagePostDate() {
        return messagePostDate;
    }

    public void setMessagePostDate(Date messagePostDate) {
        this.messagePostDate = messagePostDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserTweet userTweet = (UserTweet) o;

        if (userName != null ? !userName.equals(userTweet.userName) : userTweet.userName != null) return false;
        if (message != null ? !message.equals(userTweet.message) : userTweet.message != null) return false;
        return messagePostDate != null ? messagePostDate.equals(userTweet.messagePostDate) : userTweet.messagePostDate == null;

    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (messagePostDate != null ? messagePostDate.hashCode() : 0);
        return result;
    }
}
