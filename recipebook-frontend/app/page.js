"use client";

import { useEffect, useState } from "react";

const apiUrl = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

export default function Home() {
    const [recipes, setRecipes] = useState([]);

    const [name, setName] = useState("");
    const [author, setAuthor] = useState("");
    const [ingredientFields, setIngredientFields] = useState([
        { name: "", amount: "", unit: "" },
    ]);

    // Загрузить все рецепты
    const loadRecipes = async () => {
        const res = await fetch(`${apiUrl}/recipes`);
        const data = await res.json();
        setRecipes(data);
    };

    // Выполняется один раз при открытии страницы
    useEffect(() => {
        loadRecipes();
    }, []);

    const changeIngredient = (index, field, value) => {
        const updatedIngredients = [...ingredientFields];
        updatedIngredients[index][field] = value;
        setIngredientFields(updatedIngredients);
    };

    const addIngredientField = () => {
        setIngredientFields([...ingredientFields, { name: "", amount: "", unit: "" }]);
    };

    const removeIngredientField = (index) => {
        setIngredientFields(ingredientFields.filter((_, i) => i !== index));
    };

    // Добавить рецепт
    const addRecipe = async () => {
        const res = await fetch(`${apiUrl}/recipes`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                name,
                author,
                ingredients: ingredientFields.map((ingredient) => ({
                    name: ingredient.name,
                    amount: Number(ingredient.amount),
                    unit: ingredient.unit,
                })),
            }),
        });

        console.log("POST status:", res.status);

        if (!res.ok) {
            const errorText = await res.text();
            console.error("POST error:", errorText);
            return;
        }

        setName("");
        setAuthor("");
        setIngredientFields([{ name: "", amount: "", unit: "" }]);

        await loadRecipes();
    };

    // Удалить рецепт
    const deleteRecipe = async (id) => {
        await fetch(`${apiUrl}/recipes/${id}`, {
            method: "DELETE",
        });

        loadRecipes();
    };

    return (
        <main className="page">
            <div className="container">
                <header className="header">
                    <h1>🍳 RecipeBook</h1>
                    <p>Save recipes with ingredients, amounts and units</p>
                </header>

                <section className="card">
                    <h2>Add new recipe</h2>

                    <div className="form">
                        <input
                            type="text"
                            placeholder="Recipe name"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                        />

                        <input
                            type="text"
                            placeholder="Author"
                            value={author}
                            onChange={(e) => setAuthor(e.target.value)}
                        />

                        <h3>Ingredients</h3>

                        {ingredientFields.map((ingredient, index) => (
                            <div className="ingredient-row" key={index}>
                                <input
                                    type="text"
                                    placeholder="Ingredient name"
                                    value={ingredient.name}
                                    onChange={(e) =>
                                        changeIngredient(index, "name", e.target.value)
                                    }
                                />

                                <input
                                    type="number"
                                    placeholder="Amount"
                                    value={ingredient.amount}
                                    onChange={(e) =>
                                        changeIngredient(index, "amount", e.target.value)
                                    }
                                />

                                <input
                                    type="text"
                                    placeholder="Unit"
                                    value={ingredient.unit}
                                    onChange={(e) =>
                                        changeIngredient(index, "unit", e.target.value)
                                    }
                                />

                                <button
                                    className="delete-button"
                                    onClick={() => removeIngredientField(index)}
                                >
                                    X
                                </button>
                            </div>
                        ))}

                        <button className="secondary-button" onClick={addIngredientField}>
                            + Add ingredient
                        </button>

                        <button className="add-button" onClick={addRecipe}>
                            Add Recipe
                        </button>
                    </div>
                </section>

                {recipes.length === 0 ? (
                    <div className="card empty">
                        No recipes yet. Add your first recipe!
                    </div>
                ) : (
                    <section className="recipes-grid">
                        {recipes.map((recipe) => (
                            <article className="recipe-card" key={recipe.id}>
                                <h3>{recipe.name}</h3>

                                <p>
                                    <span className="label">Author:</span> {recipe.author}
                                </p>

                                <p>
                                    <span className="label">Created:</span>{" "}
                                    {recipe.createdAt
                                        ? new Date(recipe.createdAt).toLocaleString()
                                        : "No date"}
                                </p>

                                <h4>Ingredients</h4>

                                <ul>
                                    {recipe.ingredients?.map((ingredient) => (
                                        <li key={ingredient.id}>
                                            {ingredient.name} — {ingredient.amount} {ingredient.unit}
                                        </li>
                                    ))}
                                </ul>

                                <button
                                    className="delete-button"
                                    onClick={() => deleteRecipe(recipe.id)}
                                >
                                    Delete
                                </button>
                            </article>
                        ))}
                    </section>
                )}
            </div>
        </main>
    );
}