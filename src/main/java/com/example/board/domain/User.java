package com.example.board.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder //객체 만들 수 있게 하는 어노테이션
public class User extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    //@Column(nullable = false, length=30, unique = true)
    private String username;

    //@Column(nullable = false, unique = true)
    private String nickname;

    //@Column(length=100)
    private String password;

    //@Column(nullable = false, length = 50, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    //@Column(nullable = false)
    private Role role;

    public void modify(String username, String email, String nickname) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;

    }

    public User updateModifiedDate() {
        this.onPreUpdate();
        return this;
    }

    public String getRoleValue() {
        return this.role.getValue();
    }
}
