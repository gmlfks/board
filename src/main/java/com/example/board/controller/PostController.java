package com.example.board.controller;

import com.example.board.application.dto.PostDto;
import com.example.board.application.dto.UserDto;
import com.example.board.application.security.auth.LoginUser;
import com.example.board.application.service.PostService;
import com.example.board.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * CRUD 기능 Controller
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    /*  게시글 작성 Post - 1 */
    @PostMapping("/post")
    public ResponseEntity write(@RequestBody PostDto.Request dto, @LoginUser UserDto.Response userDto) {

        return ResponseEntity.ok(postService.writePost(userDto.getNickname(),dto));
    }

    /*  게시글 전체 보여주기 */
    @GetMapping("/post/postList")
    public ResponseEntity viewPosts() {
        return ResponseEntity.ok(postService.viewPosts());
    }

    /*  게시글 하나 상세 보기 */
    @GetMapping("/post/{id}")
    public ResponseEntity read(@PathVariable Long id) {
        return ResponseEntity.ok(postService.viewOnePost(id));
    }

    /*  게시글 수정 */
    @PostMapping("/post/update/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody PostDto.Request dto) {
        postService.updatePost(id, dto);
        return ResponseEntity.ok(id);
    }

    /*  게시글 삭제 */
    @PostMapping("/post/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok(id);
    }


}
