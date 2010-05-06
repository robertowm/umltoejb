package app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author robertowm
 */
public class MyDatabase {

    private Hashtable<UserKey, UserDataObject> userTable;
    private Hashtable<ArticleKey, ArticleDataObject> articleTable;
    private Hashtable<CommentKey, CommentDataObject> commentTable;
    private static MyDatabase instance;

    private MyDatabase() {
        userTable = new Hashtable<UserKey, UserDataObject>();
        articleTable = new Hashtable<ArticleKey, ArticleDataObject>();
        commentTable = new Hashtable<CommentKey, CommentDataObject>();

        UserDataObject admin = new UserDataObject(new UserKey(1));
        admin.setEmail("admin@admin.com");
        admin.setFirstName("Admin");
        admin.setLastName("Administrator");
        admin.setLogin("admin");
        admin.setNickName("adm");
        admin.setPassword("admin");
        try {
            this.create(admin);
        } catch (Exception ex) {
        }
    }

    public static synchronized MyDatabase instance() {
        if (instance == null) {
            instance = new MyDatabase();
        }
        return instance;
    }

    // Table User
    public UserKey getNextUserKey() {
        Integer id = 0;
        for (UserDataObject userDataObject : userTable.values()) {
            if (id < userDataObject.getUserKey().userID) {
                id = userDataObject.getUserKey().userID;
            }
        }
        return new UserKey(id + 1);
    }

    public void create(UserDataObject object) throws Exception {
        object.setUserKey(getNextUserKey());
        this.create(object.getUserKey(), object);
    }

    public void create(UserKey key, UserDataObject object) throws Exception {
        if (userTable.containsKey(key)) {
            throw new Exception("Key " + key + " already exists in User's table.");
        }
        userTable.put(key, object);
    }

    public UserDataObject retrieve(UserKey key) throws Exception {
        UserDataObject result = userTable.get(key);
        if (result == null) {
            throw new Exception("Key " + key + " does not exist in User's table.");
        }
        return result;
    }

    public void update(UserKey key, UserDataObject object) throws Exception {
        if (!userTable.containsKey(key)) {
            throw new Exception("Key " + key + " does not exist in User's table.");
        }
        userTable.put(key, object);
    }

    public UserDataObject delete(UserKey key) throws Exception {
        UserDataObject result = userTable.remove(key);
        if (result == null) {
            throw new Exception("Key " + key + " does not exist in User's table.");
        }
        return result;
    }

    public Collection<UserDataObject> retrieveAllUsers() {
        return userTable.values();
    }

    // Table Article
    public ArticleKey getNextArticleKey() {
        Integer id = 0;
        for (ArticleDataObject articleDataObject : articleTable.values()) {
            if (id < articleDataObject.getArticleKey().articleID) {
                id = articleDataObject.getArticleKey().articleID;
            }
        }
        return new ArticleKey(id + 1);
    }

    public void create(ArticleDataObject object) throws Exception {
        object.setArticleKey(getNextArticleKey());
        this.create(object.getArticleKey(), object);
    }

    public void create(ArticleKey key, ArticleDataObject object) throws Exception {
        if (articleTable.containsKey(key)) {
            throw new Exception("Key " + key + " already exists in Article's table.");
        }
        articleTable.put(key, object);
    }

    public ArticleDataObject retrieve(ArticleKey key) throws Exception {
        ArticleDataObject result = articleTable.get(key);
        if (result == null) {
            throw new Exception("Key " + key + " does not exist in Article's table.");
        }
        if (result.getComments() == null) {
            for (CommentDataObject commentDataObject : retrieveManyByArticleKey(key)) {
                result.addComments(commentDataObject);
            }
        } else {
            result.getComments().clear();
            result.getComments().addAll(retrieveManyByArticleKey(key));
        }
        return result;
    }

    public void update(ArticleKey key, ArticleDataObject object) throws Exception {
        if (!articleTable.containsKey(key)) {
            throw new Exception("Key " + key + " does not exist in Article's table.");
        }
        articleTable.put(key, object);
    }

    public ArticleDataObject delete(ArticleKey key) throws Exception {
        ArticleDataObject result = articleTable.remove(key);
        if (result == null) {
            throw new Exception("Key " + key + " does not exist in Article's table.");
        }
        return result;
    }

    public Collection<ArticleDataObject> retrieveAllArticles() {
        return articleTable.values();
    }

    // Table Comment
    public CommentKey getNextCommentKey() {
        Integer id = 0;
        for (CommentDataObject commentDataObject : commentTable.values()) {
            if (id < commentDataObject.getCommentKey().commentID) {
                id = commentDataObject.getCommentKey().commentID;
            }
        }
        return new CommentKey(id + 1);
    }

    public void create(CommentDataObject object) throws Exception {
        object.setCommentKey(getNextCommentKey());
        this.create(object.getCommentKey(), object);
    }

    public void create(CommentKey key, CommentDataObject object) throws Exception {
        if (commentTable.containsKey(key)) {
            throw new Exception("Key " + key + " already exists in Comment's table.");
        }
        commentTable.put(key, object);
    }

    public CommentDataObject retrieve(CommentKey key) throws Exception {
        CommentDataObject result = commentTable.get(key);
        if (result == null) {
            throw new Exception("Key " + key + " does not exist in Comment's table.");
        }
        return result;
    }

    public void update(CommentKey key, CommentDataObject object) throws Exception {
        if (!commentTable.containsKey(key)) {
            throw new Exception("Key " + key + " does not exist in Comment's table.");
        }
        commentTable.put(key, object);
    }

    public CommentDataObject delete(CommentKey key) throws Exception {
        CommentDataObject result = commentTable.remove(key);
        if (result == null) {
            throw new Exception("Key " + key + " does not exist in Comment's table.");
        }
        return result;
    }

    public Collection<CommentDataObject> retrieveAllComments() {
        return commentTable.values();
    }

    public Collection<CommentDataObject> retrieveManyByArticleKey(ArticleKey articleKey) {
        List<CommentDataObject> ret = new ArrayList<CommentDataObject>();
        for (CommentDataObject comment : commentTable.values()) {
            if (comment.getArticle() != null && comment.getArticle().getArticleKey().equals(articleKey)) {
                ret.add(comment);
            }
        }
        return ret;
    }
}
