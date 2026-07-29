package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RecipeResponseDTO {

    private Long id;
    private String name;
    private String author;
    private String recipeDescription;
    private LocalDateTime createdAt;
    private List<IngredientDTO> ingredients;

    public RecipeResponseDTO() {
    }

    public RecipeResponseDTO(
            Long id,
            String name,
            String author,
            String recipeDescription,
            LocalDateTime createdAt,
            List<IngredientDTO> ingredients
    ) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.recipeDescription = recipeDescription;
        this.createdAt = createdAt;
        this.ingredients = ingredients;
    }

}