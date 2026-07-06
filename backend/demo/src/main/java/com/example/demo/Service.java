package com.example.demo;

import com.example.demo.Model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Service {
    @Autowired
    private Repository repository;

    public List<Recipe> getAllRecipes() {
        return repository.findAll();
    }
    public Optional<Recipe> getRecipeById(Long id)> {
        return repository.findById(id);
    }

    public Recipe saveRecipe(Recipe recipe) {
        return repository.save(recipe);
    }

    public void deleteRecipe(Long id) {
        repository.deleteById(id);
    }
}