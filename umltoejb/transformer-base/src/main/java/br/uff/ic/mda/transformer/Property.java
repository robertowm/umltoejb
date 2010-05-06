package br.uff.ic.mda.transformer;

public class Property {

    /* Attributes */
    protected String key;
    protected String value;

    /* Constructors */
    public Property() {
        this(null, null);
    }

    public Property(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /* Getters and Setters */
    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /* Others methods (equals, hashCode and toString) */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Property)) {
            return false;
        }

        Property property = (Property) obj;
        if (((this.key == null && property.key == null) || (this.key != null && this.key.equals(property.key)))
                && ((this.value == null && property.value == null) || (this.value != null && this.value.equals(property.value)))) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.key != null ? this.key.hashCode() : 0);
        hash = 97 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return String.format("Key:%s  Value:%s", this.key, this.value);
    }
}
