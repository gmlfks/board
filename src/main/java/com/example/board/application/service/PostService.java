package com.example.board.application.service;

import com.example.board.application.dto.PostDto;
import com.example.board.application.dto.UserDto;
import com.example.board.domain.Post;
import com.example.board.domain.User;
import com.example.board.repository.PostRepository;
import com.example.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /* 게시글 작성 */
    @Transactional
    public Long writePost(String nickname , PostDto.Request postDto) {
        User user = userRepository.findByNickname(nickname);

        postDto.setUser(user);
        log.info("PostService writePost 실행");
        Post savePost = postDto.toEntity();
        postRepository.save(savePost);
        return savePost.getId();
    }

    /* 게시글 목록 조회 */
    // 근데 이거 main 페이지에 다 띄울 건데 굳이 필요없을 듯.
    public List<Post> viewPosts() {
        return postRepository.findAll();
    }


    /* 게시글 조회 */
    @Transactional(readOnly = true)
    public PostDto.Response viewOnePost(Long post_id) {
        Post findPost = postRepository.findById(post_id).orElseThrow(()
        -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id: "+post_id));

        return new PostDto.Response(findPost);
    }

    /* 게시글 수정 */
    @Transactional
    public void updatePost(Long post_id, PostDto.Request dto) {
        Post post = postRepository.findById(post_id).orElseThrow(()
        -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id : "+post_id));

        post.update(dto.getTitle(), dto.getContent());
    }

    /* 게시글 삭제 */
    @Transactional
    public void deletePost(Long post_id) {
        Post findPost = postRepository.findById(post_id).orElseThrow(()
        -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id : "+post_id));
        postRepository.delete(findPost);
    }

    /* 게시글 view 카운팅 */
    @Transactional
    public int updateView(Long post_id) {
        return postRepository.updateView(post_id);
    }

    /* Paging and Sorting */
    @Transactional(readOnly = true)
    public Page<Post> pageList(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    /* Search */
    @Transactional(readOnly = true)
    public Page<Post> search(String keyword, Pageable pageable) {
        Page<Post> postList = postRepository.findByTitleContaining(keyword, pageable);
        return postList;
    }



}
