"use client";
// useState хранит данные компонента,
// useEffect выполняет код при загрузке страницы
import { useEffect, useState } from "react";

//используется локальный сервер Spring Boot
const apiUrl = "http://localhost:8080";

export default function Home() {

    // список рецептов, полученных с backend
    const [recipes, setRecipes] = useState([]);

    // поля формы нового рецепта
    const [name, setName] = useState("");
    const [author, setAuthor] = useState("");
    const [recipeDescription, setRecipeDescription] = useState("");

    // cписок полей ингредиентов, По умолчанию отображается одна пустая строка ингредиента.
    const [ingredientFields, setIngredientFields] = useState([
        { name: "", amount: "", unit: "" },
    ]);

    // загружает все рецепты с backend
    const loadRecipes = async () => {

        // отправляем GET-запрос на /recipes.
        const res = await fetch(`${apiUrl}/recipes`);

        // выводим HTTP-статус в консоль браузера
        console.log("GET status:", res.status);

        // преобразуем JSON-ответ в JavaScript-объект
        const data = await res.json();

        console.log("GET data:", data);

        // сохраняем полученные рецепты в состояние, после этого React обновляет страницу.
        setRecipes(data);
    };

    // выполняется один раз после открытия страницы
    useEffect(() => {
        loadRecipes();
    }, []);

    // изменяет одно поле определённого ингредиента
    const changeIngredient = (index, field, value) => {

        // создаём копию массива ингредиентов, чтобы не изменять state напрямую
        const updatedIngredients = [...ingredientFields];

        // изменяем нужное поле выбранного ингредиента, например: name, amount или unit
        updatedIngredients[index][field] = value;

        // сохраняем обновлённый массив
        setIngredientFields(updatedIngredients);
    };

    // добавляет новую пустую строку ингредиента
    const addIngredientField = () => {
        setIngredientFields([
            ...ingredientFields,
            { name: "", amount: "", unit: "" },
        ]);
    };

    // удаляет строку ингредиента по её индексу
    const removeIngredientField = (index) => {
        setIngredientFields(
            ingredientFields.filter((_, i) => i !== index)
        );
    };

    // добавляет новый рецепт.
    const addRecipe = async () => {

        // отправляем POST-запрос на backend
        const res = await fetch(`${apiUrl}/recipes`, {
            method: "POST",

            // cообщаем серверу, что отправляем JSON
            headers: {
                "Content-Type": "application/json",
            },

            // gреобразуем объект рецепта в JSON-строку
            body: JSON.stringify({
                name,
                author,
                recipeDescription,

                // gреобразуем введённые ингредиенты в формат, ожидаемый backend
                ingredients: ingredientFields.map((ingredient) => ({
                    name: ingredient.name,

                    // значение из input приходит как строка, поэтому преобразуем его в число.
                    amount: Number(ingredient.amount),

                    unit: ingredient.unit,
                })),
            }),
        });

        console.log("POST status:", res.status);

        // если backend вернул ошибку
        if (!res.ok) {
            const errorText = await res.text();
            console.error("POST error:", errorText);
            return;
        }

        // после успешного сохранения очищаем форму.
        setName("");
        setAuthor("");
        setRecipeDescription("");

        // возвращаем одну пустую строку ингредиента.
        setIngredientFields([
            { name: "", amount: "", unit: "" },
        ]);

        // повторно загружаем список чтобы новый рецепт появился на странице.
        await loadRecipes();
    };

    // удаляет рецепт по его id.
    const deleteRecipe = async (id) => {

        // отправляем DELETE-запрос.
        await fetch(`${apiUrl}/recipes/${id}`, {
            method: "DELETE",
        });

        // после удаления обновляем список рецептов
        await loadRecipes();
    };

    return (
        <main className="page">
            <div className="container">

                <header className="header">
                    <h1> RecipeBook</h1>
                    <p>
                        Save recipes with ingredients, amounts and units
                    </p>
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

                        <textarea
                            placeholder="Recipe Description"
                            value={recipeDescription}
                            rows={4}
                            onChange={(e) => {

                                // сохраняем введённый текст.
                                setRecipeDescription(e.target.value);

                                // автоматически изменяем высоту textarea в зависимости от количества текста.
                                e.target.style.height = "auto";
                                e.target.style.height =
                                    e.target.scrollHeight + "px";
                            }}
                        />

                        <h3>Ingredients</h3>

                        {ingredientFields.map((ingredient, index) => (
                            <div
                                className="ingredient-row"
                                key={index}
                            >
                                <input
                                    type="text"
                                    placeholder="Ingredient name"
                                    value={ingredient.name}
                                    onChange={(e) =>
                                        changeIngredient(
                                            index,
                                            "name",
                                            e.target.value
                                        )
                                    }
                                />

                                <input
                                    type="number"
                                    placeholder="Amount"
                                    value={ingredient.amount}
                                    onChange={(e) =>
                                        changeIngredient(
                                            index,
                                            "amount",
                                            e.target.value
                                        )
                                    }
                                />

                                <input
                                    type="text"
                                    placeholder="Unit"
                                    value={ingredient.unit}
                                    onChange={(e) =>
                                        changeIngredient(
                                            index,
                                            "unit",
                                            e.target.value
                                        )
                                    }
                                />

                                {/* Удаление строки ингредиента */}
                                <button
                                    className="delete-button"
                                    onClick={() =>
                                        removeIngredientField(index)
                                    }
                                >
                                    X
                                </button>
                            </div>
                        ))}

                        <button
                            className="secondary-button"
                            onClick={addIngredientField}
                        >
                            + Add ingredient
                        </button>

                        <button
                            className="add-button"
                            onClick={addRecipe}
                        >
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
                            <article
                                className="recipe-card"
                                key={recipe.id}
                            >
                                <h3>{recipe.name}</h3>

                                <p>
                                    <span className="label">
                                        Author:
                                    </span>{" "}
                                    {recipe.author}
                                </p>

                                <p className="recipe-description">
                                    <span className="label">
                                        Description:
                                    </span>{" "}
                                    {recipe.recipeDescription}
                                </p>

                                <p>
                                    <span className="label">
                                        Created:
                                    </span>{" "}
                                    {recipe.createdAt
                                        ? new Date(
                                            recipe.createdAt
                                        ).toLocaleString()
                                        : "No date"}
                                </p>

                                <h4>Ingredients</h4>

                                <ul>
                                    {recipe.ingredients?.map(
                                        (ingredient) => (
                                            <li key={ingredient.id}>
                                                {ingredient.name} —{" "}
                                                {ingredient.amount}{" "}
                                                {ingredient.unit}
                                            </li>
                                        )
                                    )}
                                </ul>

                                <button
                                    className="delete-button"
                                    onClick={() =>
                                        deleteRecipe(recipe.id)
                                    }
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