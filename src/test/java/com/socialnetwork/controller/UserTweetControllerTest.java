package com.socialnetwork.controller;

import com.socialnetwork.model.UserTweet;
import com.socialnetwork.Application;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.h2.tools.RunScript;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class UserTweetControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;



    private static final String H2_DB_DRIVER="org.h2.Driver";
    private static final String H2_DB_URL="jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String H2_DB_USERNAME="sa";
    private static final String H2_DB_PASSWORD="";


    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.stream(converters)
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setUp() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        final JdbcDatabaseTester databaseTester = new JdbcDatabaseTester(
                H2_DB_DRIVER, H2_DB_URL, H2_DB_USERNAME, H2_DB_PASSWORD);
        final IDatabaseConnection connection = databaseTester.getConnection();

        try (Connection conn = connection.getConnection();
             InputStreamReader in = new InputStreamReader(
                     UserTweetControllerTest.class
                             .getResourceAsStream("/database/scripts/usertweet_h2_db.sql"))) {
            RunScript.execute(conn, in);
        }
    }


    @Test
    public void shouldPostUserTweetSuccessfully() throws Exception {

        mockMvc.perform(post("/v1/tweetservice/usertweet").content(this.json(new UserTweet("testuser","hello", new Date()))).contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
    }

    @Test
    public void shouldRetrieveUserTweetsInReverseChronologicalOrder() throws Exception {

        mockMvc.perform(post("/v1/tweetservice/usertweet").content(this.json(new UserTweet("testuser","hello1", new Date()))).contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        mockMvc.perform(post("/v1/tweetservice/usertweet").content(this.json(new UserTweet("testuser","hello2", new Date()))).contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        mockMvc.perform(get("/v1/tweetservice/testuser/tweets"))
                .andExpect(status().isOk()).andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$", Matchers.contains("hello2", "hello1")));

    }

    @Test
    public void shouldSaveUserFollowingInfoSuccessfully() throws Exception {

        mockMvc.perform(post("/v1/tweetservice/usertweet").content(this.json(new UserTweet("user1","hello from user1", new Date()))).contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
        mockMvc.perform(post("/v1/tweetservice/usertweet").content(this.json(new UserTweet("user2","hello from user2", new Date()))).contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        mockMvc.perform(post("/v1/tweetservice/user1/follows/user2"))
                .andExpect(status().is(200))
                .andExpect(content().string("success"));
    }

    @Test
    public void shouldThrow404IfFollowedUserNotYetPostedSingleMessageYet() throws Exception {

        mockMvc.perform(post("/v1/tweetservice/usertweet").content(this.json(new UserTweet("user2","hello from user2", new Date()))).contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        mockMvc.perform(post("/v1/tweetservice/user1/follows/user2"))
                .andExpect(status().is(404));
    }

    @Test
    public void shouldThrow404IfEitherOfFollowingOrFollowedUserNotYetPostedSingleMessageYet() throws Exception {

        mockMvc.perform(post("/v1/tweetservice/user1/follows/user2"))
                .andExpect(status().is(404));
    }

    @Test
    public void shouldRetrieveFollowingUserTweetsInReverseChronologicalOrder() throws Exception {

        // user tweets
        mockMvc.perform(post("/v1/tweetservice/usertweet").content(this.json(new UserTweet("user","hello", new Date()))).contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        mockMvc.perform(post("/v1/tweetservice/usertweet").content(this.json(new UserTweet("user1","hello1user1", new Date()))).contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        mockMvc.perform(post("/v1/tweetservice/usertweet").content(this.json(new UserTweet("user1","hello2user1", new Date()))).contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        mockMvc.perform(post("/v1/tweetservice/usertweet").content(this.json(new UserTweet("user2","hello1user2", new Date()))).contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        mockMvc.perform(post("/v1/tweetservice/usertweet").content(this.json(new UserTweet("user2","hello2user2", new Date()))).contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        //user follows user1 and user2
        mockMvc.perform(post("/v1/tweetservice/user/follows/user1"))
                .andExpect(status().is(200))
                .andExpect(content().string("success"));

        mockMvc.perform(post("/v1/tweetservice/user/follows/user2"))
                .andExpect(status().is(200))
                .andExpect(content().string("success"));

        // get tweets of following users i.e. user1 and user2
        mockMvc.perform(get("/v1/tweetservice/user/followingusertweets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.user1", Matchers.contains("hello2user1", "hello1user1")))
                .andExpect(jsonPath("$.user2", Matchers.contains("hello2user2", "hello1user2")));

    }


    private String json(Object input) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        mappingJackson2HttpMessageConverter.write(
                input, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}