package app;

import java.io.Serializable;
import java.util.*;

public class CommentDataObject implements Serializable {

    private CommentKey key;
    private ArticleDataObject article;
    private String name;
    private String email;
    private String website;
    private String text;

    public CommentDataObject() {
    }

    public CommentDataObject(CommentKey key) {
        this.key = key;
    }

    public CommentKey getCommentKey() {
        return key;
    }

    public void setCommentKey(CommentKey key) {
        this.key = key;
    }

    public ArticleDataObject getArticle() {
        return article;
    }

    public void setArticle(ArticleDataObject article) {
        this.article = article;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
