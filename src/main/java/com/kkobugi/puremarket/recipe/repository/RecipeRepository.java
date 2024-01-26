package com.kkobugi.puremarket.recipe.repository;

import com.kkobugi.puremarket.recipe.domain.entity.Recipe;
import com.kkobugi.puremarket.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findTop4ByUserAndStatusEqualsOrderByCreatedDateDesc(User user, String status);
}
