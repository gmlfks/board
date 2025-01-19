package com.example.board.controller;

import com.example.board.application.dto.CommentDto;
import com.example.board.application.dto.UserDto;
import com.example.board.application.security.auth.LoginUser;
import com.example.board.application.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /* 댓글 작성 */
    @PostMapping("/post/{id}/comment")
    public ResponseEntity<Long> save(@PathVariable Long id, @RequestBody CommentDto.Request dto,
                                     @LoginUser UserDto.Response userSessionDto) {
        return ResponseEntity.ok(commentService.writeComment(id, userSessionDto.getNickname(), dto));
    }

    /* 댓글 조회 */
    @GetMapping("/post/{id}/comment")
    public List<CommentDto.Response> read(@PathVariable Long id) {
        return commentService.readComments(id);
    }

    /* 댓글 수정 */
    @PostMapping("/post/{postId}/comment/{id}")
    public ResponseEntity<Long> update(@PathVariable Long postId, @PathVariable Long id, @RequestBody CommentDto.Request dto) {
        commentService.updateComment(postId,id,dto);
        return ResponseEntity.ok(id);
    }

    /* 댓글 삭제 */
    @DeleteMapping("/post/{postId}/comment/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long postId, @PathVariable Long id) {
        commentService.deleteComment(postId,id);
        return ResponseEntity.ok(id);
    }


}
