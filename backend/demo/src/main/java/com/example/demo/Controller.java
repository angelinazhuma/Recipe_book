package com.example.demo;

import com.example.demo.Model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/*
 * Controller принимает HTTP-запросы
 * и вызывает методы Service.
 */

@RestController
@RequestMapping("/recipes")
public class Controller {

    @Autowired
    private Service service;

    // Получить все рецепты
    @GetMapping
    public List<Recipe> getAllRecipes() {
        return service.getAllRecipes();
    }

    // Получить рецепт по id
    @GetMapping("/{id}")
    public Optional<Recipe> getRecipeById(@PathVariable Long id) {
        return service.getRecipeById(id);
    }

    // Добавить новый рецепт
    @PostMapping
    public Recipe saveRecipe(@RequestBody Recipe recipe) {
        return service.saveRecipe(recipe);
    }

    // Удалить рецепт
    @DeleteMapping("/{id}")
    public void deleteRecipe(@PathVariable Long id) {
        service.deleteRecipe(id);
    }

}