package com.kkobugi.puremarket.giveaway.entity;

import com.kkobugi.puremarket.common.BaseEntity;
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

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;
    private String giveawayImage;
}
