package com.metacoding.laviu.domain.chatmessages.domain;

import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.users.domain.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "chat_messages_tb")
public class ChatMessages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String content;

    //data part
    @CreationTimestamp
    private LocalDateTime createdAt;

    //FK(Foreign Key)
    @ManyToOne
    private Streams stream;
    @ManyToOne
    private Users user;

    protected ChatMessages() {
    }

    @Builder
    public ChatMessages(Integer id, String content, Streams stream, Users user) {
        this.id = id;
        this.content = content;
        this.stream = stream;
        this.user = user;
    }
}
