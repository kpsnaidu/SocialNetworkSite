Checkout or download the code locally.

go thru the tests and should be pretty much self explanatory and all the rest API are tested with mockMVC.

to run the applicaiton in JVM and verify the rest API thru the client such as postman or another application etc...

1. set up a database and create a test schema and then create 2 tables as mentioned in src/test/resources/database/scripts/usertweet_h2_db.sql
2. input that db properties in src/main/resources/usertweetservice.properties
        db.driver=<driver class name>
        db.url=<jdbc url>
        db.username=<username>
        db.password=<password>

go to project location in terminal and then run

 for build:  mvn clean install

 to deploy:  java -jar target/socialnetworksite-1.0-SNAPSHOT.jar

Below are the APIs to execute.

1. to save the user tweet message
 url:  http://localhost:8080/v1/tweetservice/usertweet
 method: post
 contenttype: application/json;charset=UTF-8
 body: {
       	"userName": "testuser",
       	"message": "hello post man",
       	"messagePostDate": "2017-03-10T15:00:00"
       }

similarly below rest API's can be tested.

to get messages posted by an user:  http://localhost:8080/v1/tweetservice/{user}/tweets
to get messages posted by the following users:  http://localhost:8080/v1/tweetservice/{user}/followingusertweets
to save user following info:  http://localhost:8080/v1/tweetservice//{followedUser}/follows/{followingUser}