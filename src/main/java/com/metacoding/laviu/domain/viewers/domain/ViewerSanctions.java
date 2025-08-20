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
    private LocalDateTime createdAt;

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

    public ViewerSanctions(ViewerSanctionsType type, Streams stream, Users streamer, Users sanctionedUser) {
        this.type = type;
        this.stream = stream;
        this.streamer = streamer;
        this.sanctionedUser = sanctionedUser;
    }

    private void updateOffenseCount() {
        if (this.isActive) return;
        this.offenseCount = this.offenseCount + 1;
        if (this.offenseCount >= 3) {
            updateIsActive(true);
            this.type = ViewerSanctionsType.KICK;
        }
    }

    public void update(ViewerSanctionsType type) {
        this.type = type;
        if (this.type == ViewerSanctionsType.KICK) {
            this.isActive = true;
        }
        updateOffenseCount();
    }

    public void updateIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public ViewerSanctions(ViewerSanctions viewerSanctions) {
        this.offenseCount = viewerSanctions.getOffenseCount();
        this.isActive = viewerSanctions.getIsActive();
        this.type = viewerSanctions.getType();
        this.createdAt = viewerSanctions.getCreatedAt();
        this.stream = viewerSanctions.getStream();
        this.streamer = viewerSanctions.getStreamer();
        this.sanctionedUser = viewerSanctions.getSanctionedUser();
    }
}
