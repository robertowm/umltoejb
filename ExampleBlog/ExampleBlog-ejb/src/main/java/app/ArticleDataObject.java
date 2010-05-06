package app;

import java.io.Serializable;
import java.util.*;

public class ArticleDataObject implements Serializable {

    private ArticleKey key;
    private UserKey user;
    private List<CommentDataObject> comments;
    private String title;
    private String text;

    public ArticleDataObject() {
    }

    public ArticleDataObject(ArticleKey key) {
        this.key = key;
    }

    public ArticleKey getArticleKey() {
        return key;
    }

    public void setArticleKey(ArticleKey key) {
        this.key = key;
    }

    public UserKey getUser() {
        return user;
    }

    public void setUser(UserKey user) {
        this.user = user;
    }

    public List<CommentDataObject> getComments() {
        return comments;
    }

    public void addComments(CommentDataObject comments) {
        if (this.comments == null) {
            this.comments = new ArrayList<CommentDataObject>();
        }
        this.comments.add(comments);
    }

    public void removeComments(CommentDataObject comments) {
        this.comments.remove(comments);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
