package com.example.demo;

import com.example.demo.Model.Ingredient;
import com.example.demo.Model.Recipe;
import com.example.demo.Repository.RecipeRepository;
import com.example.demo.Service.RecipeService;
import com.example.demo.dto.IngredientDTO;
import com.example.demo.dto.RecipeRequestDTO;
import com.example.demo.dto.RecipeResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository repository;

    @InjectMocks
    private RecipeService service;

    /*
     * getAllRecipes()
     */

    @Test
    void getAllRecipesReturnsRecipesFromRepository() {
        Recipe recipe = createRecipe(
                1L,
                "Sandwich",
                "John"
        );

        Mockito.when(repository.findAll())
                .thenReturn(List.of(recipe));

        List<RecipeResponseDTO> result = service.getAllRecipes();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals("Sandwich", result.get(0).getName());
        Assertions.assertEquals("John", result.get(0).getAuthor());
        Assertions.assertEquals(
                "Test description",
                result.get(0).getRecipeDescription()
        );
        Assertions.assertEquals(
                "Salt",
                result.get(0).getIngredients().get(0).getName()
        );

        Mockito.verify(repository).findAll();
    }

    @Test
    void getAllRecipesReturnsEmptyList() {
        Mockito.when(repository.findAll())
                .thenReturn(List.of());

        List<RecipeResponseDTO> result = service.getAllRecipes();

        Assertions.assertTrue(result.isEmpty());

        Mockito.verify(repository).findAll();
    }

    // GetRecipebyId()

    @Test
    void getRecipeByIdReturnsRecipeWhenRecipeExists() {
        Recipe recipe = createRecipe(
                25L,
                "Pizza",
                "Bob"
        );

        Mockito.when(repository.findById(25L))
                .thenReturn(Optional.of(recipe));

        Optional<RecipeResponseDTO> result =
                service.getRecipeById(25L);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(25L, result.get().getId());
        Assertions.assertEquals("Pizza", result.get().getName());
        Assertions.assertEquals("Bob", result.get().getAuthor());

        Mockito.verify(repository).findById(25L);
    }

    @Test
    void getRecipeByIdReturnsEmptyWhenRecipeNotFound() {
        Mockito.when(repository.findById(0L))
                .thenReturn(Optional.empty());

        Optional<RecipeResponseDTO> result =
                service.getRecipeById(0L);

        Assertions.assertTrue(result.isEmpty());

        Mockito.verify(repository).findById(0L);
    }

    // saveRecipe tests

    @Test
    void saveRecipeConvertsDtoAndSetsRecipeForIngredients() {
        RecipeRequestDTO requestDTO = createRequestDTO(
                "Pasta",
                "Angelina"
        );

        Mockito.when(repository.save(Mockito.any(Recipe.class)))
                .thenAnswer(invocation -> {
                    Recipe savedRecipe = invocation.getArgument(0);

                    savedRecipe.setId(1L);
                    savedRecipe.setCreatedAt(
                            LocalDateTime.of(
                                    2026,
                                    7,
                                    28,
                                    12,
                                    0
                            )
                    );

                    return savedRecipe;
                });

        RecipeResponseDTO result =
                service.saveRecipe(requestDTO);

        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("Pasta", result.getName());
        Assertions.assertEquals("Angelina", result.getAuthor());
        Assertions.assertEquals(
                "Test description",
                result.getRecipeDescription()
        );
        Assertions.assertEquals(1, result.getIngredients().size());
        Assertions.assertEquals(
                "Salt",
                result.getIngredients().get(0).getName()
        );

        Mockito.verify(repository)
                .save(Mockito.argThat(recipe ->
                        recipe.getName().equals("Pasta")
                                && recipe.getIngredients().size() == 1
                                && recipe.getIngredients()
                                .get(0)
                                .getRecipe() == recipe
                ));
    }

    @Test
    void saveRecipeReturnsRecipeWithRandomValues() {
        RecipeRequestDTO requestDTO = createRequestDTO(
                "Random soup",
                "Random author"
        );

        Mockito.when(repository.save(Mockito.any(Recipe.class)))
                .thenAnswer(invocation -> {
                    Recipe recipe = invocation.getArgument(0);
                    recipe.setId(99L);
                    return recipe;
                });

        RecipeResponseDTO result =
                service.saveRecipe(requestDTO);

        Assertions.assertEquals(99L, result.getId());
        Assertions.assertEquals("Random soup", result.getName());
        Assertions.assertEquals(
                "Random author",
                result.getAuthor()
        );

        Mockito.verify(repository)
                .save(Mockito.any(Recipe.class));
    }

    // deleteRecipe test method

    @Test
    void deleteRecipeCallsRepositoryWithRegularId() {
        service.deleteRecipe(25L);

        Mockito.verify(repository)
                .deleteById(25L);
    }

    @Test
    void deleteRecipeCallsRepositoryWithZeroId() {
        service.deleteRecipe(0L);

        Mockito.verify(repository)
                .deleteById(0L);
    }

    // Helpful methods to not write the same code all the time

    private Recipe createRecipe(
            Long id,
            String name,
            String author
    ) {
        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setName(name);
        recipe.setAuthor(author);
        recipe.setRecipeDescription("Test description");
        recipe.setCreatedAt(
                LocalDateTime.of(
                        2026,
                        7,
                        28,
                        12,
                        0
                )
        );

        Ingredient ingredient = new Ingredient();
        ingredient.setName("Salt");
        ingredient.setAmount(5.0);
        ingredient.setUnit("g");
        ingredient.setRecipe(recipe);

        recipe.setIngredients(List.of(ingredient));

        return recipe;
    }

    private RecipeRequestDTO createRequestDTO(
            String name,
            String author
    ) {
        IngredientDTO ingredientDTO = new IngredientDTO();
        ingredientDTO.setName("Salt");
        ingredientDTO.setAmount(5.0);
        ingredientDTO.setUnit("g");

        RecipeRequestDTO requestDTO = new RecipeRequestDTO();
        requestDTO.setName(name);
        requestDTO.setAuthor(author);
        requestDTO.setRecipeDescription("Test description");
        requestDTO.setIngredients(List.of(ingredientDTO));

        return requestDTO;
    }
}