package com.sparta.newsfeedteamproject.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    User user;
    String username;
    String password;
    String name;
    String email;
    String userInfo;
    Status status;
    LocalDateTime statusModTime;

    @BeforeEach
    void setUp() {
        user = new User();

        username = "tmdtn01345";
        password = "@Tmdtn01345";
        name = "김승수";
        email = "snol4331@gmail.com";
        userInfo = "안녕하세요";
        status = Status.UNAUTHORIZED;
        statusModTime = LocalDateTime.now();

        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setEmail(email);
        user.setUserInfo(userInfo);
        user.setStatus(status);
        user.setStatusModTime(statusModTime);
    }

    @Test
    @DisplayName("User Entity 생성자 Test")
    void constructorUser() {

        // given

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
        String refreshToken = "asdf1234";
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
        // given
        String updateName = "김슨수";
        String updateUserInfo = "안녕하세요. 수정입니다.";
        String updatePassword = "@Tmdtn0134567";
        LocalDateTime updateModifiedAt = LocalDateTime.now();

        // when
        user.update(updateName, updateUserInfo, updatePassword, updateModifiedAt);

        // then
        assertEquals(updateName, user.getName());
        assertEquals(updateUserInfo, user.getUserInfo());
        assertEquals(updatePassword, user.getPassword());
        assertEquals(updateModifiedAt, user.getModifiedAt());
    }

    @Test
    @DisplayName("User Entity updateRefreshToken() Test")
    void updateRefreshToken() {
        // given
        String refreshToken = "asdf1234";
        user.setRefreshToken(refreshToken);
        String updateRefreshToken = "qwer5678";

        // when
        user.updateRefreshToken(updateRefreshToken);

        // then
        assertEquals(updateRefreshToken, user.getRefreshToken());
        assertNotEquals(refreshToken, user.getRefreshToken());
    }
}