package com.example.demo.Repository;

import com.example.demo.Model.Recipe; // имопртируем класс рецепты чтобы репозиторий понимал с чем работает
import org.springframework.data.jpa.repository.JpaRepository; //благодаря этому сразу получаем метожы
import org.springframework.stereotype.Repository;

// repository works with database, spring automatically makes all methods for saving and deleting recipes


@Repository // this tells spring that this class works with db
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}

//