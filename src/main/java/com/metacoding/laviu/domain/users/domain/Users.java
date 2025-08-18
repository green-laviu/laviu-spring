package com.metacoding.laviu.domain.users.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Getter
@Table(name = "users_tb")
@Entity
public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nickname;
    @Column(unique = true)
    private String email; // 로그인 부분의 id 로 사용
    @Column(nullable = false)
    private String password;
    private String profileImageUrl;
    private String bio;
    private String fcmToken;
    private String roles; // "USER,ADMIN"

    //Enum part
    @Enumerated(EnumType.STRING)
    private UsersProvider provider;

    //Date part
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;

    // 기본생성자 사용금지
    protected Users() {
    }

    @Builder
    public Users(Integer id, String nickname, String email, String password, String profileImageUrl, String bio, String fcmToken, String roles, UsersProvider provider, LocalDateTime lastLoginAt) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.bio = bio;
        this.fcmToken = fcmToken;
        this.roles = roles;
        this.provider = provider;
        this.lastLoginAt = lastLoginAt;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.roles == null || this.roles.isEmpty()) {
            return Collections.emptyList();
        }
        // "USER,ADMIN" 같은 문자열을 쉼표로 분리하고, 각 역할에 "ROLE_" 접두사를 붙여 SimpleGrantedAuthority 객체로 변환
        return Arrays.stream(this.roles.split(","))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.trim()))
                .toList();
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
