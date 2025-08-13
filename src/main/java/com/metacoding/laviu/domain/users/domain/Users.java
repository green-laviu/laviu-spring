package com.metacoding.laviu.domain.users.domain;

import com.metacoding.laviu.domain.streams.domain.Streams;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Table(name = "users_tb")
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nickname;
    @Column(unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    private String profileImageUrl;
    private String bio;
    private String fcmToken;

    //Enum part
    @Enumerated(EnumType.STRING)
    private UsersProvider provider;
    @Enumerated(EnumType.STRING)
    private UsersType type;

    //Date part
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;

    //FK(Foreign Key) part
    @OneToMany(mappedBy = "follower")
    private List<Follows> followsList;
    @OneToMany(mappedBy = "following")
    private List<Follows> followingList;
    @OneToMany(mappedBy = "streams")
    private List<Streams> streamsList;

    // 기본생성자 사용금지
    protected Users() {
    }

    //  id만 받는 생성자 (연관관계 전용)
    public Users(Integer id) {
        this.id = id;
    }
}
