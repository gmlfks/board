package com.example.board.repository;

import com.example.board.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username); //Security

    /**
     * 소셜 로그인할 때 로직은 아직 설계하지 않겠음
     */
    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);
    User findByNickname(String nickname); // Post에서 nickname만 보이고 게시글 쓰니까

}
