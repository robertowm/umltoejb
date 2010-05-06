package app;

import java.rmi.RemoteException;
import javax.naming.*;
import javax.ejb.*;

public class UserBean implements EntityBean {

    private EntityContext context;
    private UserDataObject key;

    public UserBean() {
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
            MyDatabase.instance().delete(this.key.getUserKey());
        } catch (Exception ex) {
            throw new RemoteException("Unable to delete '" + this.key + "'.");
        }
    }

    public void ejbLoad() throws RemoteException {
        UserKey pk = (UserKey) context.getPrimaryKey();
        try {
            this.key = MyDatabase.instance().retrieve(pk);
        } catch (Exception ex) {
            throw new RemoteException("Failed to find '" + pk.userID + "' in User's table.");
        }
    }

    public void ejbStore() throws RemoteException {
        try {
            MyDatabase.instance().update(this.key.getUserKey(), this.key);
        } catch (Exception ex) {
            try {
                if (this.key.getUserKey() == null) {
                    MyDatabase.instance().create(this.key);
                } else {
                    MyDatabase.instance().create(this.key.getUserKey(), this.key);
                }
            } catch (Exception ex1) {
                throw new RemoteException("Failed at 'ejbStore'.");
            }
        }
    }

    public UserDataObject getUserDataObject() {
        return key;
    }

    public void setUserDataObject(UserDataObject newObj) throws NamingException, FinderException, CreateException {
        this.key = newObj;
    }

    public UserKey ejbCreate(UserDataObject newObj) throws NamingException, FinderException, CreateException {
        if (newObj == null) {
            throw new CreateException("The field 'newObj' must not be null");
        }
        try {
            MyDatabase.instance().create(newObj);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        setUserDataObject(newObj);
        return newObj.getUserKey();
    }

    public void ejbPostCreate(UserDataObject newObj) {
        // TODO populate relationships here if appropriate
    }

    public UserKey ejbFindByPrimaryKey(UserKey primaryKey) throws FinderException {
        try {
            return MyDatabase.instance().retrieve(primaryKey).getUserKey();
        } catch (Exception ex) {
            throw new FinderException(ex.getMessage());
        }
    }

    public UserKey ejbFindByLoginAndPassword(String login, String password) throws FinderException {
        for (UserDataObject user : MyDatabase.instance().retrieveAllUsers()) {
            if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
                return user.getUserKey();
            }
        }
        throw new FinderException("Object nao encontrado.");
    }

    public Boolean postNewArticle(String title, String text) {
        ArticleDataObject article = new ArticleDataObject();

        article.setText(text);
        article.setTitle(title);
        article.setUser(this.key.getUserKey());

        try {
            MyDatabase.instance().create(article);
            this.key.addArticles(article.getArticleKey());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
