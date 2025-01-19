package com.example.board.application.service;

import com.example.board.application.dto.CommentDto;
import com.example.board.domain.Comment;
import com.example.board.domain.Post;
import com.example.board.domain.User;
import com.example.board.repository.CommentRepository;
import com.example.board.repository.PostRepository;
import com.example.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /* 댓글 작성 */
    @Transactional
    public Long writeComment(Long id, String nickname, CommentDto.Request dto) {
        User user = userRepository.findByNickname(nickname);
        Post post = postRepository.findById(id).orElseThrow(()
        -> new IllegalArgumentException("댓글 쓰기 실패 : 해당 게시글이 존재하지 않습니다."));

        dto.setUser(user);
        dto.setPost(post);

        Comment comment = dto.toEntity();
        commentRepository.save(comment);
        return comment.getId();
    }

    /* 댓글 조회 */
    @Transactional(readOnly = true)
    public List<CommentDto.Response> readComments(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()
        -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id: "+id));

        List<Comment> allComments = post.getComments();
        return allComments.stream().map(CommentDto.Response::new).collect(Collectors.toList());

    }

    /* 댓글 수정 */
    @Transactional
    public void updateComment(Long postId, Long id, CommentDto.Request dto) {
        Comment comment = commentRepository.findByPostIdAndId(postId, id).orElseThrow(()
        -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. id: "+id));

        comment.update(dto.getComments());
    }

    /* 댓글 삭제 */
    @Transactional
    public void deleteComment(Long postId, Long id) {
        Comment comment = commentRepository.findByPostIdAndId(postId, id).orElseThrow(()
        -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. id: "+id));

        commentRepository.delete(comment);
    }
}
