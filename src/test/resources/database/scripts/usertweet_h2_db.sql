drop table if exists usertweet;
create table if not exists usertweet (
	user_name varchar(50) NOT NULL,
    message varchar(140) NOT NULL,
    message_post_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

drop table if exists userfollows;
create table if not exists userfollows (
	followed_user_name varchar(50) NOT NULL,
    following_user_name varchar(50) NOT NULL
);