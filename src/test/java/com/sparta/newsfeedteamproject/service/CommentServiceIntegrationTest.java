package com.sparta.newsfeedteamproject.service;

import com.sparta.newsfeedteamproject.dto.MessageResDto;
import com.sparta.newsfeedteamproject.dto.comment.CommentDelResDto;
import com.sparta.newsfeedteamproject.dto.comment.CommentReqDto;
import com.sparta.newsfeedteamproject.dto.comment.CommentResDto;
import com.sparta.newsfeedteamproject.entity.Feed;
import com.sparta.newsfeedteamproject.entity.User;
import com.sparta.newsfeedteamproject.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommentServiceIntegrationTest {

    @Autowired
    CommentService commentService;

    @Autowired
    FeedService feedService;

    @Autowired
    UserRepository userRepository;

    User user;
    Feed feed;
    CommentResDto createComment = null;
    String updateContents = "";


    @Test
    @Order(1)
    @DisplayName("댓글 생성(createComment) 테스트")
    void createCommentTest() {

        // given
        String contents = "댓글 작성입니다.";
        CommentReqDto requestDto = new CommentReqDto(contents);
        user = userRepository.findById(1L).orElse(null);
        feed = feedService.findFeed(1L);

        // when
        MessageResDto<CommentResDto> messageComment = commentService.createComment(feed.getId(), requestDto, user);
        CommentResDto comment = messageComment.getData();

        // then
        assertNotNull(comment.getId());
        assertEquals(contents, comment.getContents());
        assertEquals(user.getUsername(), comment.getUsername());
        assertEquals(feed.getId(), comment.getFeedId());
        createComment = comment;
    }

    @Test
    @Order(2)
    @DisplayName("댓글 변경(updateComment) 테스트")
    void updateCommentTest() {

        // given
        Long commentId = this.createComment.getId();
        String updateContents = "댓글 수정입니다.";
        CommentReqDto requestDto = new CommentReqDto(updateContents);

        // when
        MessageResDto<CommentResDto> messageComment = commentService.updateComment(feed.getId(), commentId, requestDto, user);
        CommentResDto comment = messageComment.getData();

        // then
        assertNotNull(comment.getId());
        assertEquals(this.createComment.getUsername(), comment.getUsername());
        assertEquals(this.createComment.getFeedId(), comment.getFeedId());
        assertEquals(updateContents, comment.getContents());
        this.updateContents = updateContents;
    }

    @Test
    @Order(3)
    @DisplayName("댓글 조회(getComment) 테스트")
    void getCommentTest() {

        // given
        Long commentId = this.createComment.getId();

        // when
        MessageResDto<CommentResDto> messageComment = commentService.getComment(feed.getId(), commentId);
        CommentResDto comment = messageComment.getData();

        // then
        assertNotNull(comment);
        assertEquals(this.createComment.getId(), comment.getId());
        assertEquals(this.createComment.getFeedId(), comment.getFeedId());
        assertEquals(this.createComment.getUsername(), comment.getUsername());

        // Order(2) 테스트에 의해 contents 내용이 정상적으로 업데이트 되었는지 검증
        assertEquals(this.updateContents, comment.getContents());
    }

    @Test
    @Order(4)
    @DisplayName("댓글 삭제(deleteComment) 테스트")
    void deleteCommentTest() {

        // given
        Long commentId = this.createComment.getId();

        // when
        MessageResDto<CommentDelResDto> messageComment = commentService.deleteComment(feed.getId(), commentId, user);
        CommentDelResDto comment = messageComment.getData();

        // then
        assertNotNull(comment);
        assertEquals(this.createComment.getId(), comment.getId());

        // 댓글 검색에 의해 comment 객체가 정상적으로 삭제 되었는지 검증
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> commentService.findComment(comment.getId()));
        assertEquals("해당 요소가 존재하지 않습니다.", exception.getMessage());
    }
}