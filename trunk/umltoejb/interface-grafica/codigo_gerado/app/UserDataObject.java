package app;

import java.util.*;

public class UserDataObject implements java.io.Serializable {

	private UserKey key;
	private List<ArticleKey> articles;
	private String firstName;
	private String lastName;
	private String nickName;
	private String login;
	private String password;
	private String email;

	public UserDataObject() {
	}

	public UserDataObject(UserKey key) {
		this.key = key;
	}

	public UserKey getUserKey() {
		return key;
	}

	public void setUserKey(UserKey key) {
		this.key = key;
	}

	public List<ArticleKey> getArticles() {
		return articles;
	}

	public void addArticles(ArticleKey articles) {
		if (this.articles == null) {
			this.articles = new ArrayList<ArticleKey>();
		}
		this.articles.add(articles);
	}

	public void removeArticles(ArticleKey articles) {
		this.articles.remove(articles);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


}
