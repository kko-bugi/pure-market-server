package com.kkobugi.puremarket.giveaway.domain.entity;

import com.kkobugi.puremarket.common.BaseEntity;
import com.kkobugi.puremarket.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
public class Giveaway extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long GiveawayIdx;

    @ManyToOne
    @JoinColumn(nullable = false, name = "userIdx")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;
    private String giveawayImage;
}
