package com.example.demo;

import com.example.demo.Controller.RecipeController;
import com.example.demo.Model.Recipe;
import com.example.demo.Service.RecipeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;

@WebMvcTest(RecipeController.class)
class RecipeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecipeService service;

    @Test
    void getAllRecipesReturnsRecipes() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Pizza");
        recipe.setAuthor("Angelina");

        Mockito.when(service.getAllRecipes()).thenReturn(List.of(recipe));

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Pizza"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value("Angelina"));
    }

    @Test
    void getRecipeByIdReturnsRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Soup");
        recipe.setAuthor("Angelina");

        Mockito.when(service.getRecipeById(1L)).thenReturn(Optional.of(recipe));

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Soup"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("Angelina"));
    }

    @Test
    void addRecipeReturnsSavedRecipe() throws Exception {
        Recipe savedRecipe = new Recipe();
        savedRecipe.setId(1L);
        savedRecipe.setName("Pasta");
        savedRecipe.setAuthor("Angelina");

        Mockito.when(service.saveRecipe(org.mockito.ArgumentMatchers.any(Recipe.class)))
                .thenReturn(savedRecipe);

        mockMvc.perform(MockMvcRequestBuilders.post("/recipes")
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "Pasta",
                                  "author": "Angelina",
                                  "ingredients": []
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Pasta"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("Angelina"));
    }

    @Test
    void deleteRecipeDeletesRecipe() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/recipes/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(service).deleteRecipe(1L);
    }
}