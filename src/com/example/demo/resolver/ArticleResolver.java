package com.example.demo.resolver;

public class ArticleResolver {
    private ArticleRepository articleRepo;
    
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
