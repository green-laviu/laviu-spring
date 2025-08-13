package com.metacoding.laviu.domain.users.domain;

import jakarta.persistence.*;
import lombok.Builder;
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
    private Boolean isNotificationsEnabled = true;

    //Date part
    @CreationTimestamp
    private LocalDateTime followedAt;

    //FK(Foreign Key) part
    @ManyToOne(fetch = FetchType.LAZY)
    private Users follower;
    @ManyToOne(fetch = FetchType.LAZY)
    private Users following;

    // 기본생성자 사용금지
    protected Follows() {
    }

    @Builder
    public Follows(Users follower, Users following) {
        this.follower = follower;
        this.following = following;
    }

    public void enableNotifications() {
        this.isNotificationsEnabled = true;
    }

    public void disableNotifications() {
        this.isNotificationsEnabled = false;
    }
}
