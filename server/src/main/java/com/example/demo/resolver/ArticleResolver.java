package com.example.demo.resolver;

import com.example.demo.model.Article;
import com.example.demo.repository.ArticleRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Component
@Transactional
public class ArticleResolver {
    private final ArticleRepository articleRepo;

    public ArticleResolver(ArticleRepository articleRepo) {
        this.articleRepo = articleRepo;
    }
    
    @QueryMapping
    public List<Article> articles() {
        return articleRepo.findAll();
    }
    
    @MutationMapping
    public Article createArticle(@Argument String title, @Argument String content) {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        return articleRepo.save(article);
    }
    
    @MutationMapping
    public Article updateArticle(@Argument Long id, @Argument String title, @Argument String content) {
        Article article = articleRepo.findById(id).orElseThrow();
        article.setTitle(title);
        article.setContent(content);
        return articleRepo.save(article);
    }
    
    @MutationMapping
    public Boolean deleteArticle(@Argument Long id) {
        articleRepo.deleteById(id);
        return true;
    }
}
