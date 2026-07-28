package com.example.demo;

import com.example.demo.Model.Ingredient;
import com.example.demo.Model.Recipe;
import com.example.demo.Repository.RecipeRepository;
import com.example.demo.Service.RecipeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class) // испольщует мокито во время выполнения тестов
class RecipeServiceTest {

    @Mock
    private RecipeRepository repository; // создает ненастоящий репозиторий

    @InjectMocks
    private RecipeService service; // подставляет наш мок вместо настощего репозитория

    @Test
    void getAllRecipesReturnsRecipesFromRepository() {
        Recipe recipe = new Recipe(); //создали обьект
        recipe.setId(1L); // ввели названия
        recipe.setName("Sandwich");
        recipe.setAuthor("John");

        Mockito.when(repository.findAll()).thenReturn(List.of(recipe));
        // когда кто нибудь вызовет repository.findall, верни лист рецептов, то есть мокито подделывает базы данных

        List<Recipe> result = service.getAllRecipes();
        // вызывает метод, но настоязий репозиторй заменен мокито

        Assertions.assertEquals(1, result.size()); // проверяем результат (в списке один рецепт?)
        Assertions.assertEquals("Sandwich", result.get(0).getName());// проверяем название
        Assertions.assertEquals("John", result.get(0).getAuthor()); // проверяем автора
    }

    @Test
    void gerRecipeByIdReturnsRecipe() {
        Recipe recipe = new Recipe(); // создаем рецепт
        recipe.setId(1L);
        recipe.setName("Pizza");
        recipe.setAuthor("Bob");

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(recipe));

        Optional<Recipe> result = service.getRecipeById(1L);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Pizza", result.get().getName());
        Assertions.assertEquals("Bob", result.get().getAuthor());
    }

    @Test
    void getRecipeByIdReturnsEmptyWhenRecipeNotFound() {

        Mockito.when(repository.findById(5L)).thenReturn(Optional.empty());

        Optional<Recipe> result = service.getRecipeById(5L);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void saveRecipeSetsRecipeForIngredients() {

        Recipe recipe = new Recipe();

        Ingredient ingredient = new Ingredient();
        ingredient.setName("Salt");

        recipe.setIngredients(List.of(ingredient));

        Mockito.when(repository.save(recipe)).thenReturn(recipe);

        Recipe result = service.saveRecipe(recipe);

        Assertions.assertEquals(recipe, ingredient.getRecipe());
        Assertions.assertEquals(recipe, result);
    }


    @Test
    void deleteRecipeCallsRepository() {

        service.deleteRecipe(1L);

        Mockito.verify(repository).deleteById(1L);
    }

    @Test
    void getAllRecipesReturnsEmptyList() {

        Mockito.when(repository.findAll()).thenReturn(List.of());

        List<Recipe> result = service.getAllRecipes();

        Assertions.assertTrue(result.isEmpty());
    }

}