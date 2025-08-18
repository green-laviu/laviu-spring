package com.metacoding.laviu.domain.viewers.domain;

import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.users.domain.Users;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Table(name = "viewer_sanctions_tb")
@Entity
public class ViewerSanctions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer offenseCount;
    private Boolean isActive;

    //Enum part
    @Enumerated(EnumType.STRING)
    private ViewerSanctionsType type;

    //Date part
    @CreationTimestamp
    private LocalDateTime createAt;

    //FK(Foreign Key) part
    @ManyToOne(fetch = FetchType.LAZY)
    private Streams stream;
    @ManyToOne(fetch = FetchType.LAZY)
    private Users streamer;
    @ManyToOne(fetch = FetchType.LAZY)
    private Users sanctionedUser;

    // 기본생성자 사용금지
    protected ViewerSanctions() {
    }
}
