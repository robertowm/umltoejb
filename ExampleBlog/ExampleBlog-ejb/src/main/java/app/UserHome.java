package app;

import java.rmi.*;
import javax.naming.*;
import javax.ejb.*;
import java.util.*;

public interface UserHome extends EJBHome {

    public abstract User create(UserDataObject newObject) throws CreateException, RemoteException;

    public abstract User findByPrimaryKey(UserKey primaryKey) throws FinderException, RemoteException;

    public abstract User findByLoginAndPassword(String login, String password) throws RemoteException, FinderException;
}
