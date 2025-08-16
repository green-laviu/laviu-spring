package com.metacoding.laviu.domain.notifications.domain;

import com.metacoding.laviu.domain.users.domain.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Table(name = "notifications_tb")
@Entity
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private Integer relatedEntityId;
    private String content;
    @Column(nullable = false)
    private Boolean isRead = false;

    //Enum part
    @Enumerated(EnumType.STRING)
    private NotificationsType type;

    //Date part
    @CreationTimestamp
    private LocalDateTime createdAt;

    //FK(Foreign Key) part
    @ManyToOne
    private Users user;

    // 기본생성자 사용금지
    protected Notifications() {
    }

    @Builder
    public Notifications(Integer relatedEntityId, String content, NotificationsType type, Users user) {
        this.relatedEntityId = relatedEntityId;
        this.content = content;
        this.type = type;
        this.user = user;
    }
}
