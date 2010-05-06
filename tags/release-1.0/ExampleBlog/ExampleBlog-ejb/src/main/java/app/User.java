package app;

import java.rmi.*;
import javax.naming.*;
import javax.ejb.*;
import java.util.*;

public interface User extends EJBObject {

    public abstract UserDataObject getUserDataObject() throws RemoteException;

    public abstract void setUserDataObject(UserDataObject update) throws NamingException, FinderException, CreateException, RemoteException;

    public abstract Boolean postNewArticle(String title, String text) throws RemoteException;
}
