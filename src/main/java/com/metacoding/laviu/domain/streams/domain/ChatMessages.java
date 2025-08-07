package com.metacoding.laviu.domain.streams.domain;

import com.metacoding.laviu.domain.users.domain.Users;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
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
}
