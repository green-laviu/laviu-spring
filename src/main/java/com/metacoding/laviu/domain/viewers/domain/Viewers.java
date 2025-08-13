package com.metacoding.laviu.domain.viewers.domain;

import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.users.domain.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Table(name = "viewers_tb")
@Entity
public class Viewers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //Date part
    @CreationTimestamp
    private LocalDateTime connectedAt;

    //FK(Foreign Key) part
    @ManyToOne(fetch = FetchType.LAZY)
    private Streams stream;
    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    // 기본생성자 사용금지
    protected Viewers() {
    }

    // 빌더 추가
    @Builder
    public Viewers(Streams stream, Users user) {
        this.stream = stream;
        this.user = user;
    }

}
