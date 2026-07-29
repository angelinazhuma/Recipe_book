package com.example.demo.Service;

import com.example.demo.Model.Ingredient;
import com.example.demo.Model.Recipe;
import com.example.demo.Repository.RecipeRepository;
import com.example.demo.dto.IngredientDTO;
import com.example.demo.dto.RecipeRequestDTO;
import com.example.demo.dto.RecipeResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository repository;

    // get all recipes
    public List<RecipeResponseDTO> getAllRecipes() {
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // get 1 recipe by id
    public Optional<RecipeResponseDTO> getRecipeById(Long id) {
        return repository.findById(id)
                .map(this::toResponseDTO);
    }

    // save new recipe
    public RecipeResponseDTO saveRecipe(RecipeRequestDTO dto) {
        Recipe recipe = toEntity(dto);

        Recipe savedRecipe = repository.save(recipe);

        return toResponseDTO(savedRecipe);
    }

    public void deleteRecipe(Long id) {
        repository.deleteById(id);
    }

    // recipeRequestDTO to Recipe
    // makes dto to recipe and ingredient
    private Recipe toEntity(RecipeRequestDTO dto) {
        Recipe recipe = new Recipe();

        recipe.setName(dto.getName());
        recipe.setAuthor(dto.getAuthor());
        recipe.setRecipeDescription(dto.getRecipeDescription());

        List<Ingredient> ingredients = dto.getIngredients()
                .stream()
                .map(ingredientDTO -> {
                    Ingredient ingredient = new Ingredient();

                    ingredient.setName(ingredientDTO.getName());
                    ingredient.setAmount(ingredientDTO.getAmount());
                    ingredient.setUnit(ingredientDTO.getUnit());

                    // connection ingredient with recipe
                    ingredient.setRecipe(recipe);

                    return ingredient;
                })
                .toList();

        recipe.setIngredients(ingredients);

        return recipe;
    }

    // recipe to recipeResponseDTO
    private RecipeResponseDTO toResponseDTO(Recipe recipe) {
        RecipeResponseDTO dto = new RecipeResponseDTO();

        dto.setId(recipe.getId());
        dto.setName(recipe.getName());
        dto.setAuthor(recipe.getAuthor());
        dto.setRecipeDescription(recipe.getRecipeDescription());
        dto.setCreatedAt(recipe.getCreatedAt());

        List<IngredientDTO> ingredients = recipe.getIngredients()
                .stream()
                .map(ingredient -> {
                    IngredientDTO ingredientDTO = new IngredientDTO();

                    ingredientDTO.setName(ingredient.getName());
                    ingredientDTO.setAmount(ingredient.getAmount());
                    ingredientDTO.setUnit(ingredient.getUnit());

                    return ingredientDTO;
                })
                .toList();

        dto.setIngredients(ingredients);

        return dto;
    }
}