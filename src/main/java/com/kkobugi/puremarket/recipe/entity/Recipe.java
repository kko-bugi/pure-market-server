package com.kkobugi.puremarket.recipe.entity;

import com.kkobugi.puremarket.common.BaseEntity;
import com.kkobugi.puremarket.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
public class Recipe extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeIdx;

    @ManyToOne
    @JoinColumn(nullable = false, name = "userIdx")
    private User user;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private String content;
    private String recipeImage;
}
