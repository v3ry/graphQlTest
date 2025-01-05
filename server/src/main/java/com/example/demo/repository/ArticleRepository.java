package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
}