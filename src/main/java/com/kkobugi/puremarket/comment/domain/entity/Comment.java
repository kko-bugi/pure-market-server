package com.kkobugi.puremarket.comment.domain.entity;

import com.kkobugi.puremarket.common.BaseEntity;
import com.kkobugi.puremarket.giveaway.domain.entity.Giveaway;
import com.kkobugi.puremarket.produce.domain.entity.Produce;
import com.kkobugi.puremarket.recipe.domain.entity.Recipe;
import com.kkobugi.puremarket.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "userIdx")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe")
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produce")
    private Produce produce;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giveaway")
    private Giveaway giveaway;

    @Column(nullable = false)
    private String content;

    @Builder
    public Comment(User user, Produce produce, Giveaway giveaway, Recipe recipe, String content) {
        this.user = user;
        this.produce = produce;
        this.giveaway = giveaway;
        this.recipe = recipe;
        this.content = content;
    }
}
