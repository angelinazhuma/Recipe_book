package com.example.demo.Controller;

import com.example.demo.Model.Recipe;
import com.example.demo.Service.RecipeService;
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
@CrossOrigin(origins = "http://localhost:3000")
public class RecipeController {

    @Autowired // спринг автоматически созжает обьет recipeservice и помещает его сюда
    private RecipeService service;

    // Получить все рецепты
    @GetMapping // получение всез рецептов
    public List<Recipe> getAllRecipes() {
        return service.getAllRecipes();
    }

    // Получить рецепт по id
    @GetMapping("/{id}") // получение рецепта по id
    public Optional<Recipe> getRecipeById(@PathVariable Long id) {
        return service.getRecipeById(id);
    }

    // Добавить новый рецепт
    @PostMapping // создание рецепта
    public Recipe saveRecipe(@RequestBody Recipe recipe) {
        return service.saveRecipe(recipe);
    }

    // Удалить рецепт
    @DeleteMapping("/{id}")
    public void deleteRecipe(@PathVariable Long id) {
        service.deleteRecipe(id);
    }

}