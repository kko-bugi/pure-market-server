package com.kkobugi.puremarket.recipe.domain.entity;

import com.kkobugi.puremarket.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class RecipeDescription extends BaseEntity { // 레시피 상세(조리 순서)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeDescriptionIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "recipeIdx")
    private Recipe recipe;

    @Column(nullable = false)
    private Integer orderNumber;

    @Column(nullable = false)
    private String description;
}
