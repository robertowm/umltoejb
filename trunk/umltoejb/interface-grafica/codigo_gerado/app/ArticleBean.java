package app;

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

	public void ejbRemove() {
	}

	public void ejbLoad() {
	}

	public void ejbStore() {
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
		
		// TODO add additional validation code, throw CreateException if data is not valid
		setArticleDataObject(newObj);
		
		return null;
	}

	public void ejbPostCreate(ArticleDataObject newObj) {
		// TODO populate relationships here if appropriate
	}

	public ArticleKey ejbFindByPrimaryKey(ArticleKey primaryKey) throws FinderException {
		// TODO - You must decide how your persistence will work.
		return null;
	}

	public java.util.Collection<ArticleKey> ejbFindAll() throws FinderException {
		// TODO
		return null;
	}

	public Boolean commentArticle() {
		// TODO
		return null;
	}


}
