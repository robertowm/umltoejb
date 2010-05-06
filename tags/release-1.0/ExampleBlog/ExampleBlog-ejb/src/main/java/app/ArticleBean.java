package app;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.*;
import javax.ejb.*;
import java.util.*;

public class ArticleBean implements EntityBean {

    private EntityContext context;
    private ArticleDataObject key;

    public ArticleBean() {
    }

    public void setEntityContext(EntityContext context) {
        this.context = context;
    }

    public void unsetEntityContext() {
        this.context = null;
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbRemove() throws RemoteException {
        try {
            MyDatabase.instance().delete(this.key.getArticleKey());
        } catch (Exception ex) {
            throw new RemoteException("Unable to delete '" + this.key + "'.");
        }
    }

    public void ejbLoad() throws RemoteException {
        ArticleKey pk = (ArticleKey) context.getPrimaryKey();
        try {
            this.key = MyDatabase.instance().retrieve(pk);
        } catch (Exception ex) {
            throw new RemoteException("Failed to find '" + pk + "' in Article's table.");
        }
    }

    public void ejbStore() throws RemoteException {
        try {
            MyDatabase.instance().update(this.key.getArticleKey(), this.key);
        } catch (Exception ex) {
            throw new RemoteException("Failed at 'ejbStore'.");
        }
    }

    public ArticleDataObject getArticleDataObject() {
        return key;
    }

    public void setArticleDataObject(ArticleDataObject newObj) throws NamingException, FinderException, CreateException {
        this.key = newObj;
    }

    public ArticleKey ejbCreate(ArticleDataObject newObj) throws NamingException, FinderException, CreateException {
        if (newObj == null) {
            throw new CreateException("The field 'newObj' must not be null");
        }
        try {
            MyDatabase.instance().create(newObj);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        setArticleDataObject(newObj);
        return newObj.getArticleKey();
    }

    public void ejbPostCreate(ArticleDataObject newObj) {
        // TODO populate relationships here if appropriate
    }

    public ArticleKey ejbFindByPrimaryKey(ArticleKey primaryKey) throws FinderException {
        try {
            return MyDatabase.instance().retrieve(primaryKey).getArticleKey();
        } catch (Exception ex) {
            throw new FinderException(ex.getMessage());
        }
    }

    public java.util.Collection<ArticleKey> ejbFindAll() throws FinderException {
        List<ArticleKey> list = new ArrayList<ArticleKey>();
        for (ArticleDataObject articleDataObject : MyDatabase.instance().retrieveAllArticles()) {
            list.add(articleDataObject.getArticleKey());
        }
        return list;
    }

    public Boolean commentArticle(String name, String email, String website, String text) {
        CommentDataObject comment = new CommentDataObject();
        comment.setName(name);
        comment.setEmail(email);
        comment.setWebsite(website);
        comment.setText(text);
        comment.setArticle(key);
        try {
            MyDatabase.instance().create(comment);
            setArticleDataObject(MyDatabase.instance().retrieve(this.key.getArticleKey()));
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
