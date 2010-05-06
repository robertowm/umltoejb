package app;

public class ArticleKey implements java.io.Serializable {

    public Integer articleID;

    public ArticleKey() {
    }

    public ArticleKey(Integer articleID) {
        this.articleID = articleID;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ArticleKey)) {
            return false;
        }
        ArticleKey other = (ArticleKey) obj;
        if (this.articleID.equals(other.articleID)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + (this.articleID != null ? this.articleID.hashCode() : 0);
        return hash;
    }
}
