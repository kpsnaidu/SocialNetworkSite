package com.socialnetwork.dao;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.socialnetwork.model.UserTweet;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.h2.tools.RunScript;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JPAPersistenceContext.class})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("classpath:database/dataset/UserTweetData.xml")
public class UserTweetDAOTest {

    private static final String H2_DB_DRIVER="org.h2.Driver";
    private static final String H2_DB_URL="jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String H2_DB_USERNAME="sa";
    private static final String H2_DB_PASSWORD="";

    @Autowired (required = true)
    UserTweetDAO userTweetDAO;

    @BeforeClass
    public static void setUpClass() throws Exception {
        final JdbcDatabaseTester databaseTester = new JdbcDatabaseTester(
                H2_DB_DRIVER, H2_DB_URL, H2_DB_USERNAME, H2_DB_PASSWORD);
        final IDatabaseConnection connection = databaseTester.getConnection();

        try (Connection conn = connection.getConnection();
             InputStreamReader in = new InputStreamReader(
                     UserTweetDAOTest.class
                             .getResourceAsStream("/database/scripts/usertweet_h2_db.sql"))) {
            RunScript.execute(conn, in);
        }
    }

    @Test
    public void shouldSaveAndRetrieveTheUserTweetsSuccessfully() {
        userTweetDAO.saveUserTweet(new UserTweet("user1", "message1", new Date()));
        userTweetDAO.saveUserTweet(new UserTweet("user1", "message2", new Date()));

        List<String> tweets = userTweetDAO.findTweetsByUser("user1");
        assertEquals(2, tweets.size());

        assertEquals("message2", tweets.get(0));
        assertEquals("message1", tweets.get(1));

        assertTrue(userTweetDAO.isUserExists("user1"));
    }

    @Test
    public void shouldSaveTheUserFollowingInfoAndRetrieveTheTweetsByFollowingUsersSuccessfully() {
        userTweetDAO.saveUserFollowingInfo("testuser", "testuser1");
        userTweetDAO.saveUserFollowingInfo("testuser", "testuser2");

        Map<String, List<String>> tweetsByFollowingUsers = userTweetDAO.findTweetsByFollowingUsersFor("testuser");

        assertEquals(2, tweetsByFollowingUsers.size());

        List<String> testuser1tweets = tweetsByFollowingUsers.get("testuser1");
        assertEquals(2, testuser1tweets.size());
        assertEquals("hello12", testuser1tweets.get(0));
        assertEquals("hello11", testuser1tweets.get(1));

        List<String> testuser2tweets = tweetsByFollowingUsers.get("testuser2");
        assertEquals(3, testuser2tweets.size());
        assertEquals("hello23", testuser2tweets.get(0));
        assertEquals("hello22", testuser2tweets.get(1));
        assertEquals("hello21", testuser2tweets.get(2));

    }



}