package com.sparta.newsfeedteamproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.newsfeedteamproject.config.SecurityConfig;
import com.sparta.newsfeedteamproject.dto.comment.CommentReqDto;
import com.sparta.newsfeedteamproject.entity.Status;
import com.sparta.newsfeedteamproject.entity.User;
import com.sparta.newsfeedteamproject.security.UserDetailsImpl;
import com.sparta.newsfeedteamproject.service.CommentService;
import com.sparta.newsfeedteamproject.service.FeedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {CommentController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                )
        }
)
class CommentMvcTest {

    private Principal mockPrincipal;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    CommentService commentService;
    @MockBean
    FeedService feedService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    private void mockUserSetup() {

        String username = "tmdtn01345";
        String password = "@Tmdtn01345";
        String email = "snol4331@gmail.com";
        String name = "김승수";
        String userInfo = "안녕하세요";
        Status status = Status.ACTIVATE;
        LocalDateTime statusModTime = LocalDateTime.now();
        User testUser = new User(username, password, name, email, userInfo, status, statusModTime);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, testUserDetails.getPassword(), testUserDetails.getAuthorities());
    }

    @Test
    @DisplayName("댓글 작성 요청")
    void createCommentTest() throws Exception {

        // given
        this.mockUserSetup();

        String contents = "댓글 작성입니다.";
        Long feedId = 1L;
        CommentReqDto reqDto = Mockito.mock(CommentReqDto.class);
        when(reqDto.getContents()).thenReturn(contents);
        String commentInfo = objectMapper.writeValueAsString(reqDto);

        // when
        ResultActions action = mvc.perform(
                post("/feeds/{feedId}/comments", feedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(commentInfo)
                        .principal(mockPrincipal)
        );

        // then
        action.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("댓글 수정 요청")
    void updateCommentTest() throws Exception {

        // given
        this.mockUserSetup();

        String updateContents = "댓글 수정입니다.";
        Long feedId = 1L;
        Long commentId = 1L;
        CommentReqDto reqDto = Mockito.mock(CommentReqDto.class);
        when(reqDto.getContents()).thenReturn(updateContents);
        String commentInfo = objectMapper.writeValueAsString(reqDto);

        // when
        ResultActions action = mvc.perform(
                put("/feeds/{feedId}/comments/{commentId}", feedId, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(commentInfo)
                        .principal(mockPrincipal)
        );

        // then
        action.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("댓글 조회 요청")
    void getCommentTest() throws Exception {

        // given
        Long feedId = 1L;
        Long commentId = 1L;

        // when
        ResultActions action = mvc.perform(get("/feeds/{feedId}/comments/{commentId}", feedId, commentId));

        // then
        action.andDo(print())
                .andExpect(status().isOk());
    }

}