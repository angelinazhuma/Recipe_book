"use client";
// useState хранит данные компонента,
// useEffect выполняет код при загрузке страницы
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

import { addNewRecipe } from "../services/recipeService";
import { createRecipeDto } from "../dto/createRecipeDto";
import { validateRecipe } from "../utils/recipeValidation";


export default function Form() {
    const router = useRouter();

    const [name, setName] = useState("");
    const [author, setAuthor] = useState("");
    const [recipeDescription, setRecipeDescription] = useState("");

    // cписок полей ингредиентов, По умолчанию отображается одна пустая строка ингредиента.
    const [ingredientFields, setIngredientFields] = useState([
        { name: "", amount: "", unit: "" },
    ]);

    const changeIngredient = (index, field, value) => {

        // создаём копию массива ингредиентов, чтобы не изменять state напрямую
        const updatedIngredients = [...ingredientFields];

        // изменяем нужное поле выбранного ингредиента, например: name, amount или unit
        updatedIngredients[index][field] = value;

        // сохраняем обновлённый массив
        setIngredientFields(updatedIngredients);
    };

    const MAX_INGREDIENTS = 10;

    const addIngredientField = () => {
        if (ingredientFields.length >= MAX_INGREDIENTS) {
            alert("You can add a maximum of 10 ingredients.");
            return;
        }

        setIngredientFields([
            ...ingredientFields,
            { name: "", amount: "", unit: "" },
        ]);


    };

    const removeIngredientField = (index) => {
        setIngredientFields(
            ingredientFields.filter((_, i) => i !== index)
        );
    };

    const addRecipe = async (event) => {
        event.preventDefault();

        const validationError = validateRecipe(
            name,
            author,
            recipeDescription,
            ingredientFields
        );

        if (validationError) {
            alert(validationError);
            return;
        }

        const recipeDto = createRecipeDto(
            name,
            author,
            recipeDescription,
            ingredientFields
        );

        try {
            await addNewRecipe(recipeDto);

            setName("");
            setAuthor("");
            setRecipeDescription("");
            setIngredientFields([
                {
                    name: "",
                    amount: "",
                    unit: "",
                },
            ]);

            router.push("/view");
        } catch (error) {
            alert(error.message);
        }
    };
    return (
        <section className="card">
            <h2>Add new recipe</h2>

            <form
                className="form"
                onSubmit={addRecipe}
            >
                <input
                    type="text"
                    placeholder="Recipe name"
                    value={name}
                    onChange={(e) =>
                        setName(e.target.value)
                    }
                />

                <input
                    type="text"
                    placeholder="Author"
                    value={author}
                    onChange={(e) =>
                        setAuthor(e.target.value)
                    }
                />

                <textarea
                    placeholder="Recipe Description"
                    value={recipeDescription}
                    rows={4}
                    onChange={(e) => {
                        setRecipeDescription(
                            e.target.value
                        );

                        e.target.style.height =
                            "auto";

                        e.target.style.height =
                            e.target.scrollHeight +
                            "px";
                    }}
                />

                <h3>Ingredients</h3>

                {ingredientFields.map(
                    (ingredient, index) => (
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
                                min="0.01"
                                step="0.01"
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

                            <button
                                type="button"
                                className="delete-button"
                                onClick={() =>
                                    removeIngredientField(
                                        index
                                    )
                                }
                            >
                                X
                            </button>
                        </div>
                    )
                )}

                {ingredientFields.length <
                    MAX_INGREDIENTS && (
                        <button
                            type="button"
                            className="secondary-button"
                            onClick={
                                addIngredientField
                            }
                        >
                            + Add ingredient
                        </button>
                    )}


                <button
                    type="submit"
                    className="add-button"

                >

                    "Add Recipe"
                </button>
            </form>
        </section>
    );
}