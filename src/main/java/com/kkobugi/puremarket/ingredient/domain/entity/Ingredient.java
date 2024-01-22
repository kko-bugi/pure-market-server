package com.kkobugi.puremarket.ingredient.domain.entity;

import com.kkobugi.puremarket.common.BaseEntity;
import com.kkobugi.puremarket.recipe.domain.entity.Recipe;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
public class Ingredient extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ingredientIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "recipeIdx")
    private Recipe recipe;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private String ingredientType;
}
