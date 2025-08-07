package com.metacoding.laviu.domain.notifications.domain;

import com.metacoding.laviu.domain.users.domain.Users;
import jakarta.persistence.*;
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
    private Integer relatedEntityId;
    private String content;
    @Column(columnDefinition = "boolean default false")
    private Boolean isRead;

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
}
