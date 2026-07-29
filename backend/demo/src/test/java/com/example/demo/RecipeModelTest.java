package com.example.demo;

import com.example.demo.Model.Ingredient;
import com.example.demo.Model.Recipe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

class RecipeModelTest {
    @Test
    void recipeGetterAndSetterWork() {
        Recipe recipe = new Recipe();

        Ingredient ingredient = new Ingredient();
        List<Ingredient> ingredients = List.of(ingredient);

        recipe.setId(1L);
        recipe.setName("Pizza");
        recipe.setAuthor("Angelina");
        recipe.setRecipeDescription("Bake the pizza");
        recipe.setIngredients(ingredients);

        Assertions.assertEquals(1L, recipe.getId());
        Assertions.assertEquals("Pizza", recipe.getName());
        Assertions.assertEquals("Angelina", recipe.getAuthor());
        Assertions.assertEquals(
                "Bake the pizza",
                recipe.getRecipeDescription()
        );
        Assertions.assertEquals(ingredients, recipe.getIngredients());
    }
    @Test
    void recipeCreatedAtGetterAndSetterWork() {
        Recipe recipe = new Recipe();
        LocalDateTime createdAt = LocalDateTime.now();

        recipe.setCreatedAt(createdAt);

        Assertions.assertEquals(createdAt, recipe.getCreatedAt());
    }

}
