package com.example.demo.Controller;

import com.example.demo.Service.RecipeService;
import com.example.demo.dto.RecipeRequestDTO;
import com.example.demo.dto.RecipeResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// controller gets http-requests and calls service methods


@RestController
@RequestMapping("/recipes")
@CrossOrigin(origins = "http://localhost:3000")
public class RecipeController {

    @Autowired // spring automatically makes recipeService object and places it here
    private RecipeService service;

    // GET all recipes
    @GetMapping
    public List<RecipeResponseDTO> getAllRecipes() {
        return service.getAllRecipes();
    }
    // GET recipe by id
    @GetMapping("/{id}") // получение рецепта по id
    public Optional<RecipeResponseDTO> getRecipeById(@PathVariable Long id) {
        return service.getRecipeById(id);
    }

    // ADD new recipe
    @PostMapping
    public RecipeResponseDTO saveRecipe(
            @Valid @RequestBody RecipeRequestDTO recipeDTO
    ) {
        return service.saveRecipe(recipeDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteRecipe(@PathVariable Long id) {
        service.deleteRecipe(id);
    }

}