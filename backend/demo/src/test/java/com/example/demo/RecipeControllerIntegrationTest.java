package com.example.demo;

import com.example.demo.Controller.RecipeController;
import com.example.demo.Service.RecipeService;
import com.example.demo.dto.IngredientDTO;
import com.example.demo.dto.RecipeRequestDTO;
import com.example.demo.dto.RecipeResponseDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeController.class)
class RecipeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecipeService service;

    /*
     * GET /recipes
     */
    @Test
    void getAllRecipesReturnsRecipes() throws Exception {
        RecipeResponseDTO recipe = createRecipeResponseDTO(
                1L,
                "Pizza"
        );

        Mockito.when(service.getAllRecipes())
                .thenReturn(List.of(recipe));

        mockMvc.perform(get("/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Pizza"))
                .andExpect(jsonPath("$[0].author").value("Angelina"))
                .andExpect(jsonPath("$[0].recipeDescription")
                        .value("Test description"))
                .andExpect(jsonPath("$[0].ingredients[0].name")
                        .value("Flour"));

        verify(service).getAllRecipes();
    }

    // db does not have recipes test
    @Test
    void getAllRecipesReturnsEmptyList() throws Exception {
        Mockito.when(service.getAllRecipes())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/recipes"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(service).getAllRecipes();
    }

   // GET recipes

    // recipe with id is not found
    @Test
    void getRecipeByIdReturnsRecipeWhenRecipeExists() throws Exception {
        RecipeResponseDTO recipe = createRecipeResponseDTO(
                25L,
                "Soup"
        );

        Mockito.when(service.getRecipeById(25L))
                .thenReturn(Optional.of(recipe));

        mockMvc.perform(get("/recipes/25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(25))
                .andExpect(jsonPath("$.name").value("Soup"))
                .andExpect(jsonPath("$.author").value("Angelina"));

        verify(service).getRecipeById(25L);
    }

    // recipe with id 0 is not found
    @Test
    void getRecipeByIdReturnsNotFoundWhenRecipeDoesNotExist()
            throws Exception {

        Mockito.when(service.getRecipeById(0L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/recipes/0"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));

        verify(service).getRecipeById(0L);
    }

    // POST recipes

    // correct data is provided
    @Test
    void addRecipeReturnsCreatedRecipe() throws Exception {
        RecipeResponseDTO savedRecipe = createRecipeResponseDTO(
                1L,
                "Pasta"
        );

        Mockito.when(service.saveRecipe(any(RecipeRequestDTO.class)))
                .thenReturn(savedRecipe);

        mockMvc.perform(post("/recipes")
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "Pasta",
                                  "author": "Angelina",
                                  "recipeDescription": "Cook the pasta",
                                  "ingredients": [
                                    {
                                      "name": "Pasta",
                                      "amount": 200,
                                      "unit": "g"
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Pasta"))
                .andExpect(jsonPath("$.author").value("Angelina"))
                .andExpect(jsonPath("$.ingredients[0].name")
                        .value("Flour"));

        verify(service).saveRecipe(any(RecipeRequestDTO.class));
    }

    // send empty blanks
    @Test
    void addRecipeReturnsBadRequestWhenFieldsAreEmpty()
            throws Exception {

        mockMvc.perform(post("/recipes")
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "",
                                  "author": "",
                                  "recipeDescription": "",
                                  "ingredients": []
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .saveRecipe(any(RecipeRequestDTO.class));
    }

    // ingredients totally are not appeared
    @Test
    void addRecipeReturnsBadRequestWhenIngredientsAreMissing()
            throws Exception {

        mockMvc.perform(post("/recipes")
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "Pasta",
                                  "author": "Angelina",
                                  "recipeDescription": "Cook the pasta"
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .saveRecipe(any(RecipeRequestDTO.class));
    }

    // amount has wrong type
    @Test
    void addRecipeReturnsBadRequestWhenAmountHasInvalidType()
            throws Exception {

        mockMvc.perform(post("/recipes")
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "Pasta",
                                  "author": "Angelina",
                                  "recipeDescription": "Cook the pasta",
                                  "ingredients": [
                                    {
                                      "name": "Pasta",
                                      "amount": "not-a-number",
                                      "unit": "g"
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .saveRecipe(any(RecipeRequestDTO.class));
    }

    // DELETE recipes

    // typical: recipe is deleted
    @Test
    void deleteRecipeDeletesRecipeById() throws Exception {
        mockMvc.perform(delete("/recipes/25"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(service).deleteRecipe(25L);
    }

    // controller gives id 0 to service
    @Test
    void deleteRecipePassesZeroIdToService() throws Exception {
        mockMvc.perform(delete("/recipes/0"))
                .andExpect(status().isNoContent());

        verify(service).deleteRecipe(0L);
    }

    // helpful method which makes ready RecipeResponseDTo, to not write every time same code

    private RecipeResponseDTO createRecipeResponseDTO(
            Long id,
            String name
    ) {
        IngredientDTO ingredient = new IngredientDTO();
        ingredient.setName("Flour");
        ingredient.setAmount(200.0);
        ingredient.setUnit("g");

        RecipeResponseDTO recipe = new RecipeResponseDTO();
        recipe.setId(id);
        recipe.setName(name);
        recipe.setAuthor("Angelina");
        recipe.setRecipeDescription("Test description");
        recipe.setCreatedAt(LocalDateTime.of(
                2026, 7, 28, 12, 0
        ));
        recipe.setIngredients(List.of(ingredient));

        return recipe;
    }
}