package com.example.demo.Model;

// описывает рецепт, соотвествует таблице в базе данных,
// спринг бут и jta используют этот класс для соханения и получения рецептов из базы данных
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.NotBlank;


@Entity //говорит йаве что класс связан с таблицей в базе
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "Recipe name is required")
    private String name;

    @NotBlank(message = "Author name is required")
    private String author;

    @NotBlank(message = "Recipe Description name is required")
    private String recipeDescription;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    // один рецепт, может содержать много ингредиентов, связб описнаан в классе ингелридент, операци с рецептом применятеся к из ингрединетам, удаленные з списка ингредиенты удаляеются из бд
    private List<Ingredient> ingredients = new ArrayList<>();

    public Recipe() {
        this.createdAt = LocalDateTime.now();

    }

    public Recipe(String name, String author, String recipeDescription, List<Ingredient> ingredients) {
        this.name = name;
        this.author = author;
        this.recipeDescription = recipeDescription;
        this.ingredients = ingredients;
        this.createdAt = LocalDateTime.now();
    }

    public String getRecipeDescription() {
        return recipeDescription;
    }

    public void setRecipeDescription(String recipeDescription) {
        this.recipeDescription = recipeDescription;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setName(String name) {
        this.name = name;
    }


}