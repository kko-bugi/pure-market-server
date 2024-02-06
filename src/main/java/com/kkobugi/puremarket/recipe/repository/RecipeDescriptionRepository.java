package com.kkobugi.puremarket.recipe.repository;

import com.kkobugi.puremarket.recipe.domain.entity.Recipe;
import com.kkobugi.puremarket.recipe.domain.entity.RecipeDescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeDescriptionRepository extends JpaRepository<RecipeDescription, Long> {
    List<RecipeDescription> findByRecipeAndStatusEqualsOrderByCreatedDateDesc(Recipe recipe, String status);
}
