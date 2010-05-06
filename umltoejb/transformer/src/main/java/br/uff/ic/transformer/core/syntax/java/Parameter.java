package br.uff.ic.transformer.core.syntax.java;

/**
 *
 * @author robertowm
 */
public class Parameter implements Nonpersistent {

    private String type;
    private String name;

    protected Parameter() {
        this(null, null);
    }

    protected Parameter(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public Parameter setType(String type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Parameter setName(String name) {
        return setName(name, false);
    }

    public Parameter setName(String name, boolean lowerFirstLetter) {
        this.name = (lowerFirstLetter) ? lowerFirstLetter(name) : name;
        return this;
    }

    protected String lowerFirstLetter(String s) {
        return s.replaceFirst(s.substring(0,1), s.substring(0,1).toLowerCase());
    }

    @Override
    public StringBuffer serialize() {
        StringBuffer sb = new StringBuffer();
        sb.append(type + " " + name);
        return sb;
    }

}
