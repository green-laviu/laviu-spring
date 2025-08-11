package com.metacoding.laviu.domain.chatmessages.domain;

import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.users.domain.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Getter
@Table(name = "chat_messages_tb")
@Entity
public class ChatMessages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Streams stream;
    @ManyToOne
    private Users user;

    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;


    protected ChatMessages() {

    }
    @Builder
    public ChatMessages(Integer id, Streams stream, Users user, String content) {
        this.id = id; // 보통은 null로 두고 DB가 생성. 테스트 더미면 세팅 가능.
        this.stream = stream;
        this.user   = user;
        this.content = content;
        // createdAt은 @CreationTimestamp가 저장 시점에 자동 세팅
    }
}
