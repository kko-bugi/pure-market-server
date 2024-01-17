package com.kkobugi.puremarket.ingredient.entity;

import com.kkobugi.puremarket.common.BaseEntity;
import com.kkobugi.puremarket.recipe.entity.Recipe;
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

    @ManyToOne
    @JoinColumn(nullable = false, name = "recipeIdx")
    private Recipe recipe;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String quantity;
}
