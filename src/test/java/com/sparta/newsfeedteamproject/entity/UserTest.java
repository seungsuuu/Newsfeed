package com.sparta.newsfeedteamproject.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

class UserTest {

    User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    @DisplayName("User Entity 생성자 Test")
    void constructorUser() {

        // given
        String username = "tmdtn01345";
        String password = "@Tmdtn01345";
        String name = "김승수";
        String email = "snol4331@gmail.com";
        String userInfo = "안녕하세요";
        Status status = Status.UNAUTHORIZED;
        LocalDateTime statusModTime = LocalDateTime.now();

        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setEmail(email);
        user.setUserInfo(userInfo);
        user.setStatus(status);
        user.setStatusModTime(statusModTime);

        // when
        User constructorUser = new User(username, password, name, email, userInfo, status, statusModTime);

        // then
        assertEquals(user.getUsername(), constructorUser.getUsername());
        assertEquals(user.getPassword(), constructorUser.getPassword());
        assertEquals(user.getName(), constructorUser.getName());
        assertEquals(user.getEmail(), constructorUser.getEmail());
        assertEquals(user.getUserInfo(), constructorUser.getUserInfo());
        assertEquals(user.getStatus(), constructorUser.getStatus());
        assertEquals(user.getStatusModTime(), constructorUser.getStatusModTime());
    }

    @Test
    @DisplayName("User Entity deleteRefreshToken() Test")
    void deleteRefreshToken() {

        // given
        String username = "tmdtn01345";
        String password = "@Tmdtn01345";
        String name = "김승수";
        String email = "snol4331@gmail.com";
        String userInfo = "안녕하세요";
        Status status = Status.UNAUTHORIZED;
        LocalDateTime statusModTime = LocalDateTime.now();
        String refreshToken = "asdf1234";

        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setEmail(email);
        user.setUserInfo(userInfo);
        user.setStatus(status);
        user.setStatusModTime(statusModTime);
        user.setRefreshToken(refreshToken);

        // when
        user.deleteRefreshToken();

        // then
        assertNotNull(user.getRefreshToken());
        assertNotEquals(refreshToken, user.getRefreshToken());
        assertEquals("", user.getRefreshToken());
    }

    @Test
    @DisplayName("User Entity update() Test")
    void update() {

    }

    @Test
    @DisplayName("User Entity updateRefreshToken() Test")
    void updateRefreshToken() {

    }
}