package com.example.demo.Model

// описывает рецепт, соотвествует таблице в базе данных,
// спринг бут и jta используют этот класс для соханения и получения рецептов из базы данных

@Entity //говорит йаве что класс связан с таблицей в базе
@Table(name = "recipes")
public class Recipe {
    @Id
    @GenerateValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private string name;
    private string author;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    private String ingredients;

    public Recipe() {

    }

    public Recipe(String name, String author, String ingredients) {
        this.name = name;
        this.author = author;
        this.ingredients = ingredients;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getIngredients() {
        return ingredients;
    }

    public string getAuthor() {
        return author;
    }

    public string getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setAuthor(string author) {
        this.author = author;
    }

    public void setName(string name) {
        this.name = name;
    }
}