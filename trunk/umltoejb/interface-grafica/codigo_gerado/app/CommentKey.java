package app;

public class CommentKey implements java.io.Serializable {

	public Integer commentID;

	public CommentKey() {
	}

	public CommentKey(Integer commentID) {
		this.commentID = commentID;
	}

	public boolean equals(Object obj) {
		if(!(obj instanceof CommentKey)) {
			return false;
		}
		CommentKey other = (CommentKey) obj;
		if (this.commentID.equals(other.commentID)) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		int hash = 3;
		hash = 19 * hash + (this.commentID != null ? this.commentID.hashCode() : 0);
		return hash;
	}


}
