package com.example.demo;

import com.example.demo.Model.Recipe; // имопртируем класс рецепты чтобы репозиторий понимал с чем работает
import org.springframework.data.jpa.repository.JpaRepository; //благодаря этому сразу получаем метожы
import org.springframework.stereotype.Repository;
// репозиторий отвечает за работу с базой данных, спринг автоматически создает все методы
// для сохранения и удаления и поиска рецептов

@Repository // сообщает спринг что это класс который работает с базой данных
public interface Repository extends JpaRepository<Recipe, Long> {
}

//