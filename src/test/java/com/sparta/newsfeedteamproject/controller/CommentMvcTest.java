package com.sparta.newsfeedteamproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.newsfeedteamproject.config.SecurityConfig;
import com.sparta.newsfeedteamproject.dto.MessageResDto;
import com.sparta.newsfeedteamproject.dto.comment.CommentReqDto;
import com.sparta.newsfeedteamproject.entity.Status;
import com.sparta.newsfeedteamproject.entity.User;
import com.sparta.newsfeedteamproject.security.UserDetailsImpl;
import com.sparta.newsfeedteamproject.service.CommentService;
import com.sparta.newsfeedteamproject.service.FeedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private MockMvc mvc;
    private Principal mockPrincipal;
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
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    @Test
    @DisplayName("댓글 작성 요청")
    void createCommentTest() throws Exception {

        // given
        this.mockUserSetup();
        Long feedId = 1L;
        String contents = "댓글 작성입니다.";
        CommentReqDto reqDto = new CommentReqDto(contents);
        String commentInfo = objectMapper.writeValueAsString(reqDto);

        given(commentService.createComment(feedId, reqDto, testUser))
                .willReturn(new MessageResDto<>(HttpStatus.OK.value(), "댓글 작성이 완료되었습니다!", reqDto));

        // when - then
        mvc.perform(post("/feeds/{feedId}/comments", feedId)
                        .content(commentInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
}