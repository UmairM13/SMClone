package com.example.springfsd;

import org.springframework.jdbc.core.JdbcTemplate;

public class DatabaseInitializer {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void init(){
        createTables();
    }

    private void createTables(){
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS users (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "first_name VARCHAR(255)," +
                "last_name VARCHAR(255)," +
                "username VARCHAR(255) UNIQUE," +
                "password TEXT," +
                "salt TEXT," +
                "session_token TEXT)");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS posts(" +
                "post_id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "text TEXT," +
                "date_published DATETIME," +
                "author_id BIGINT," +
                "FOREIGN KEY(author_id) REFERENCES users(id))");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS likes (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "post_id BIGINT," +
                "user_id BIGINT," +
                "FOREIGN KEY(post_id) REFERENCES posts(post_id)," +
                "FOREIGN KEY(user_id) REFERENCES users(id))");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS followers (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "follower_id BIGINT," +
                "user_id BIGINT," +
                "FOREIGN KEY (user_id) REFERENCES users (id)," +
                "FOREIGN KEY (follower_id) REFERENCES users(id))");
    }
}
