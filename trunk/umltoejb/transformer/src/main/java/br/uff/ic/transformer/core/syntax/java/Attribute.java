package br.uff.ic.transformer.core.syntax.java;

/**
 *
 * @author robertowm
 */
public class Attribute implements Nonpersistent {

    private String visibility;
    private boolean staticAttribute;
    private boolean constantAttribute;
    private String type;
    private String name;
    private String value;

    protected Attribute() {
        this(null, false, false, null, null, null);
    }

    protected Attribute(String visibility, String type, String name) {
        this(visibility, false, false, type, name, null);
    }

    protected Attribute(String visibility, String type, String name, String value) {
        this(visibility, false, false, type, name, value);
    }

    protected Attribute(String visibility, boolean staticAttribute, boolean constantAttribute, String type, String name, String value) {
        this.visibility = visibility;
        this.staticAttribute = staticAttribute;
        this.constantAttribute = constantAttribute;
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public String getVisibility() {
        return this.visibility;
    }

    public Attribute setVisibility(String visibility) {
        this.visibility = visibility;
        return this;
    }

    public boolean isStaticAttribute() {
        return this.staticAttribute;
    }

    public Attribute setStaticAttribute(boolean staticAttribute) {
        this.staticAttribute = staticAttribute;
        return this;
    }

    public boolean isConstantAttribute() {
        return this.constantAttribute;
    }

    public Attribute setConstantAttribute(boolean constantAttribute) {
        this.constantAttribute = constantAttribute;
        return this;
    }

    public String getType() {
        return this.type;
    }

    public Attribute setType(String type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Attribute setName(String name) {
        return setName(name, false);
    }

    public Attribute setName(String name, boolean lowerFirstLetter) {
        this.name = (lowerFirstLetter) ? lowerFirstLetter(name) : name;
        return this;
    }

    public String getValue() {
        return this.value;
    }

    public Attribute setValue(String value) {
        this.value = value;
        return this;
    }

    protected String lowerFirstLetter(String s) {
        return s.replaceFirst(s.substring(0,1), s.substring(0,1).toLowerCase());
    }

    @Override
    public StringBuffer serialize() {
        StringBuffer sb = new StringBuffer();
        sb.append("\t");
        if (visibility != null && !"".equals(visibility.trim())) {
            sb.append(visibility + " ");
        }
        if (staticAttribute) {
            sb.append("static ");
        }
        if (constantAttribute) {
            sb.append("final ");
        }
        sb.append(type + " " + name);
        if (value != null) {
            sb.append(" = " + value);
        }
        sb.append(";");
        return sb;
    }

}
