package app;

import java.rmi.*;
import javax.naming.*;
import javax.ejb.*;
import java.util.*;

public interface ArticleHome extends EJBHome {

    public abstract Article create(ArticleDataObject newObject) throws CreateException, RemoteException;

    public abstract Article findByPrimaryKey(ArticleKey primaryKey) throws FinderException, RemoteException;

    public abstract java.util.Collection<Article> findAll() throws RemoteException, FinderException;
}
