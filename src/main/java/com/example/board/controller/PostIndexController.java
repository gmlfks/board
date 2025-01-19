package com.example.board.controller;

import com.example.board.application.dto.CommentDto;
import com.example.board.application.dto.PostDto;
import com.example.board.application.dto.UserDto;
import com.example.board.application.security.auth.LoginUser;
import com.example.board.application.service.PostService;
import com.example.board.domain.Post;
import com.example.board.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostIndexController {

    private final PostService postService;

    @GetMapping("/welcome")
    public String welcomePage(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
                              Pageable pageable, @LoginUser UserDto.Response user) {
        Page<Post> list = postService.pageList(pageable);

        if(user!=null) {
            model.addAttribute("user",user);
        }
        model.addAttribute("posts", list);
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("hasNext", list.hasNext());
        model.addAttribute("hasPrev", list.hasPrevious());

        return "main";
    }
    /*  게시글 작성 Post */
    @GetMapping("/post/write")
    public String write(@LoginUser UserDto.Response user, Model model) {
        if(user !=null) {
            model.addAttribute("user", user);
        }
        return "/post/post-write";
    }

    @GetMapping("/post/read/{id}")
    public String viewPost(@PathVariable Long id, @LoginUser UserDto.Response user, Model model) {

        PostDto.Response dto = postService.viewOnePost(id);
        List<CommentDto.Response> comments = dto.getComments();

        if(comments!=null && !comments.isEmpty()) {
            model.addAttribute("comments",comments);
        }

        if(user!=null) {
            model.addAttribute("user", user);

            if (dto.getUserId().equals(user.getId())) {
                model.addAttribute("writer", true);
            }

            if(comments.stream().anyMatch(s-> s.getUser_id().equals(user.getId()))) {
                model.addAttribute("isWriter",true);
            }
        }
            postService.updateView(id);
            model.addAttribute("post",dto);

        return "/post/post-read";
    }

    /*  게시글 수정 */
    @GetMapping("/post/postUpdate/{id}")
    public String update(@PathVariable Long id, Model model, @LoginUser UserDto.Response user) {
        PostDto.Response dto = postService.viewOnePost(id);
        if(user!=null) {
            model.addAttribute("user",user);
        }
        model.addAttribute("post",dto);

        return "/post/post-update";
    }

    /* 게시글 검색 */
    @GetMapping("/post/postSearch")
    public String search(String keyword, Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
    Pageable pageable, @LoginUser UserDto.Response user) {
        Page<Post> searchList = postService.search(keyword, pageable);

        if(user!=null) {
            model.addAttribute("user",user);
        }

        model.addAttribute("searchList",searchList);
        model.addAttribute("keyword",keyword);
        model.addAttribute("previous",pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("hasNext", searchList.hasNext());
        model.addAttribute("hasPrev", searchList.hasPrevious());

        return "/post/post-search";
    }

}
