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

// загружает только веб-часть приложения и тестирует RecipeController. база данных, репозитории и обычные сервисы при этом не запускаются.
@WebMvcTest(RecipeController.class)
class RecipeControllerIntegrationTest {

    // MockMvc позволяет отправлять тестовые HTTP-запросы без запуска настоящего веб-сервера.
    @Autowired
    private MockMvc mockMvc;

    // создаёт поддельный объект RecipeService, контроллер будет использовать этот mock вместо настоящего сервиса.
    @MockitoBean
    private RecipeService service;

    // проверяет GET /recipes — получение всех рецептов
    @Test
    void getAllRecipesReturnsRecipes() throws Exception {

        // создаём тестовый рецепт
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Pizza");
        recipe.setAuthor("Angelina");

        // указываем поведение mock-сервиса: когда контроллер вызовет getAllRecipes(),
        // сервис должен вернуть список с созданным рецептом.
        Mockito.when(service.getAllRecipes())
                .thenReturn(List.of(recipe));

        // выполняем GET-запрос по адресу /recipes.
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