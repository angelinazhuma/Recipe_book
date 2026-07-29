"use client";

import { useEffect, useState } from "react";

import {
    deleteRecipe,
    getAllRecipes,
} from "../features/services/recipeService";

import RecipeList from "../features/components/RecipeList";

export default function ViewRecipesPage() {
    const [recipes, setRecipes] = useState([]);

    const loadRecipes = async () => {
        const data = await getAllRecipes();
        setRecipes(data);

    };

    useEffect(() => {
        loadRecipes();
    }, []);

    const handleDelete = async (id) => {


            await deleteRecipe(id);

            setRecipes((currentRecipes) =>
                currentRecipes.filter(
                    (recipe) => recipe.id !== id
                )
            );

    };


    return (

            <RecipeList
                recipes={recipes}
                onDelete={handleDelete}
        />
    );
}