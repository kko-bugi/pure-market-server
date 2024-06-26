package com.kkobugi.puremarket.recipe.domain.entity;

import com.kkobugi.puremarket.common.BaseEntity;
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

    @Builder
    public RecipeDescription(Recipe recipe, Integer orderNumber, String description) {
        this.recipe = recipe;
        this.orderNumber = orderNumber;
        this.description = description;
    }

    public void delete() {
        this.setStatus(INACTIVE);
    }
}
