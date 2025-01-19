package com.example.board.application.dto;

import com.example.board.domain.Comment;
import com.example.board.domain.Post;
import com.example.board.domain.User;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private Long id;
        private String comments;
        private String createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        private String modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        private User user;
        private Post post;

        public Comment toEntity() {
            Comment comment = Comment.builder()
                    .id(id)
                    .comments(comments)
                    .createdDate(createdDate)
                    .modifiedDate(modifiedDate)
                    .user(user)
                    .post(post)
                    .build();
            return comment;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class Response {
        private Long id;
        private String comments;
        private String createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        private String modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        private String nickname;
        private Long user_id;
        private Long post_id;

        public Response(Comment comment) {
            this.id = comment.getId();
            this.comments = comment.getComments();
            this.createdDate = comment.getCreatedDate();
            this.modifiedDate = comment.getModifiedDate();
            this.nickname = comment.getUser().getNickname();
            this.user_id = comment.getUser().getId();
            this.post_id = comment.getPost().getId();
        }

    }
}
