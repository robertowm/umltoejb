package app;

import java.rmi.*;
import javax.naming.*;
import javax.ejb.*;
import java.util.*;

public interface Article extends EJBObject {

    public abstract ArticleDataObject getArticleDataObject() throws RemoteException;

    public abstract void setArticleDataObject(ArticleDataObject update) throws NamingException, FinderException, CreateException, RemoteException;

    public abstract Boolean commentArticle(String name, String email, String website, String text) throws RemoteException;
}
