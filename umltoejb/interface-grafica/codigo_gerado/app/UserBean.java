package app;

import javax.naming.*;
import javax.ejb.*;
import java.util.*;

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

	public void ejbRemove() {
	}

	public void ejbLoad() {
	}

	public void ejbStore() {
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
		
		// TODO add additional validation code, throw CreateException if data is not valid
		setUserDataObject(newObj);
		
		return null;
	}

	public void ejbPostCreate(UserDataObject newObj) {
		// TODO populate relationships here if appropriate
	}

	public UserKey ejbFindByPrimaryKey(UserKey primaryKey) throws FinderException {
		// TODO - You must decide how your persistence will work.
		return null;
	}

	public UserKey ejbFindByLoginAndPassword() throws FinderException {
		// TODO
		return null;
	}

	public Boolean postNewArticle() {
		// TODO
		return null;
	}


}
