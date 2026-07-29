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
    void deleteRecipeDeletesRecipe() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/recipes/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(service).deleteRecipe(1L);
    }
}