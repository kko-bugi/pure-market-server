package com.kkobugi.puremarket.ingredient.domain.entity;

import com.kkobugi.puremarket.common.BaseEntity;
import com.kkobugi.puremarket.common.enums.IngredientType;
import com.kkobugi.puremarket.recipe.domain.entity.Recipe;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import static com.kkobugi.puremarket.common.constants.Constant.INACTIVE;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private IngredientType ingredientType;

    @Builder
    public Ingredient(Recipe recipe, String name, String quantity, IngredientType ingredientType) {
        this.recipe = recipe;
        this.name = name;
        this.quantity = quantity;
        this.ingredientType = ingredientType;
    }

    public void delete() {
        this.setStatus(INACTIVE);
    }
}
