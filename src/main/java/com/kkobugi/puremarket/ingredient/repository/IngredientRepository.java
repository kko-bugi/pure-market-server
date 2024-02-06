package com.kkobugi.puremarket.ingredient.repository;

import com.kkobugi.puremarket.common.enums.IngredientType;
import com.kkobugi.puremarket.ingredient.domain.entity.Ingredient;
import com.kkobugi.puremarket.recipe.domain.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByRecipeAndIngredientTypeAndStatusEqualsOrderByCreatedDateDesc(Recipe recipe, IngredientType ingredientType, String status);
}
