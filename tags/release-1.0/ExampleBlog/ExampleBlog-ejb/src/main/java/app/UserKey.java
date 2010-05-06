package app;

public class UserKey implements java.io.Serializable {

    public Integer userID;

    public UserKey() {
    }

    public UserKey(Integer userID) {
        this.userID = userID;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof UserKey)) {
            return false;
        }
        UserKey other = (UserKey) obj;
        if (this.userID.equals(other.userID)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + (this.userID != null ? this.userID.hashCode() : 0);
        return hash;
    }
}
