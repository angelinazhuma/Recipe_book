package com.example.demo;

import com.example.demo.Model.Ingredient;
import com.example.demo.Model.Recipe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IngredientModelTest {

    @Test
    void ingredientGetterAndSetterWork() {
        Ingredient ingredient= new Ingredient();
        Recipe recipe = new Recipe();

        ingredient.setId(1L);
        ingredient.setName("Salt");
        ingredient.setAmount(2.0);
        ingredient.setUnit("teaspoon");
        ingredient.setRecipe(recipe);


        Assertions.assertEquals(1L, ingredient.getId());
        Assertions.assertEquals("Salt", ingredient.getName());
        Assertions.assertEquals(2.0, ingredient.getAmount());
        Assertions.assertEquals("teaspoon", ingredient.getUnit());
        Assertions.assertEquals(recipe, ingredient.getRecipe());
    }


    @Test
    void ingredientConstructorWorks() {
        Ingredient ingredient =
                new Ingredient("Flour", 300.0, "grams");

        Assertions.assertEquals("Flour", ingredient.getName());
        Assertions.assertEquals(300.0, ingredient.getAmount());
        Assertions.assertEquals("grams", ingredient.getUnit());

        Assertions.assertNull(ingredient.getId());
        Assertions.assertNull(ingredient.getRecipe());
    }
}