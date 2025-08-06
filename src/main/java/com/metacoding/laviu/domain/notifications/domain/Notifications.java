package com.metacoding.laviu.domain.notifications.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Table(name = "notifications_tb")
@Entity
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 기본생성자 사용금지
    protected Notifications() {
    }
}
