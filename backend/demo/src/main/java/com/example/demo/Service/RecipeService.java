package com.example.demo.Service;

import com.example.demo.Model.Recipe;
import com.example.demo.Repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.Model.Ingredient;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {
    @Autowired
    private RecipeRepository repository;

    public List<Recipe> getAllRecipes() {
        return repository.findAll();
    }
    public Optional<Recipe> getRecipeById(Long id) {
        return repository.findById(id);
    }

    public Recipe saveRecipe(Recipe recipe) {
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredient.setRecipe(recipe);
        }
        return repository.save(recipe);
    }

    public void deleteRecipe(Long id) {
        repository.deleteById(id);
    }
}