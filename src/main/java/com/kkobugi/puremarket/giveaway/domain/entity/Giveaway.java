package com.kkobugi.puremarket.giveaway.domain.entity;

import com.kkobugi.puremarket.common.BaseEntity;
import com.kkobugi.puremarket.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import static com.kkobugi.puremarket.common.constants.Constant.Giveaway.DONE;
import static com.kkobugi.puremarket.common.constants.Constant.Giveaway.GIVEAWAY;
import static com.kkobugi.puremarket.common.constants.Constant.INACTIVE;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Giveaway extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long GiveawayIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "userIdx")
    private User user;

    @Column(nullable = false, length = 32)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String giveawayImage;

    @Builder
    public Giveaway(User user, String title, String content, String giveawayImage) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.giveawayImage = giveawayImage;
    }

    public void changeStatus(String currentStatus) {
        if (currentStatus.equals(GIVEAWAY)) this.setStatus(DONE);
        else if (currentStatus.equals(DONE)) this.setStatus(GIVEAWAY);
    }

    public void delete() {
        this.setStatus(INACTIVE);
    }
    public void modifyTitle(String title) { this.title = title; }
    public void modifyContent(String content) { this.content = content; }
    public void modifyImage(String imageUrl) { this.giveawayImage = imageUrl; }
}
