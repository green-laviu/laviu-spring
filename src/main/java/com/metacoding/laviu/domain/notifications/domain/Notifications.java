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
    private Users user; // TODO 이거 방송을 하는 스트리머니까 나중에 식별 잘되게 이름 변경해야함

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

    public void markAsRead() {
        this.isRead = true;
    }
}
