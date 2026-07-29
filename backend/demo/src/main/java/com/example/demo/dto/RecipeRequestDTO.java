package com.example.demo.dto;

import com.example.demo.dto.IngredientDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RecipeRequestDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String author;

    @NotBlank
    private String recipeDescription;

    @Valid
    @NotEmpty
    private List<IngredientDTO> ingredients;

    public RecipeRequestDTO() {
    }

}