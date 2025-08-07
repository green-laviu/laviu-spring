package com.metacoding.laviu.domain.users.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Table(name = "follows_tb")
@Entity
public class Follows {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private Boolean notify = false;

    //Date part
    @CreationTimestamp
    private LocalDateTime followedAt;

    //FK(Foreign Key) part
    @ManyToOne
    private Users follower;
    @ManyToOne
    private Users following;

    // 기본생성자 사용금지
    protected Follows() {
    }
}
