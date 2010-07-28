package br.uff.ic.mda.transformer.core.syntax.java;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author robertowm
 */
public class Method implements Nonpersistent {

    private String visibility = "public";
    private boolean staticMethod = false;
    private boolean overridable = true;
    private boolean abstractMethod = false;
    private String returnType = "void";
    private String name;
    private List<Parameter> parameters;
    private List<String> exceptions;
    private String[] code;

    protected Method() {
    }

    protected Method(String visibility, String returnType, String name) {
        this.visibility = visibility;
        this.returnType = returnType;
        this.name = name;
    }

    public String getVisibility() {
        return this.visibility;
    }

    public Method setVisibility(String visibility) {
        this.visibility = visibility;
        return this;
    }

    public boolean isStaticMethod() {
        return staticMethod;
    }

    public Method setStaticMethod(boolean staticMethod) {
        this.staticMethod = staticMethod;
        return this;
    }

    public boolean isOverridable() {
        return overridable;
    }

    public Method setOverridable(boolean overridable) {
        this.overridable = overridable;
        return this;
    }

    public boolean isAbstractMethod() {
        return abstractMethod;
    }

    public Method setAbstractMethod(boolean abstractMethod) {
        this.abstractMethod = abstractMethod;
        return this;
    }

    public String getReturnType() {
        return returnType;
    }

    public Method setReturnType(String returnType) {
        this.returnType = returnType;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Method setName(String name) {
        return setName(name, false);
    }

    public Method setName(String name, boolean lowerFirstLetter) {
        this.name = (lowerFirstLetter) ? lowerFirstLetter(name) : name;
        return this;
    }

    protected String lowerFirstLetter(String s) {
        return s.replaceFirst(s.substring(0, 1), s.substring(0, 1).toLowerCase());
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public Method addParameter(Parameter parameter) {
        if (parameters == null) {
            parameters = new ArrayList<Parameter>();
        }
        parameters.add(parameter);
        return this;
    }

    public Method addParameter(String type, String name) {
        if (parameters == null) {
            parameters = new ArrayList<Parameter>();
        }
        parameters.add(new Parameter(type, name));
        return this;
    }

    public String[] getCode() {
        return code;
    }

    public Method setCode(String[] code) {
        this.code = code;
        return this;
    }

    public List<String> getExceptions() {
        return exceptions;
    }

    public Method addException(String exception) {
        if (exceptions == null) {
            exceptions = new ArrayList<String>();
        }
        exceptions.add(exception);
        return this;
    }

    @Override
    public StringBuffer serialize() {
        StringBuffer sb = new StringBuffer();
        sb.append("\t");
        if (visibility != null && !"".equals(visibility.trim())) {
            sb.append(visibility + " ");
        }
        if (staticMethod) {
            sb.append("static ");
        }
        if (!overridable) {
            sb.append("final ");
        }
        if (abstractMethod) {
            sb.append("abstract ");
        }

        sb.append(returnType + " " + name);

        sb.append("(");
        if (parameters != null && !parameters.isEmpty()) {
            for (int i = 0; i < parameters.size() - 1; i++) {
                sb.append(parameters.get(i).serialize() + ", ");
            }
            sb.append(parameters.get(parameters.size() - 1).serialize());
        }
        sb.append(")");

        if (exceptions != null && !exceptions.isEmpty()) {
            sb.append(" throws ");
            for (int i = 0; i < exceptions.size() - 1; i++) {
                sb.append(exceptions.get(i) + ", ");
            }
            sb.append(exceptions.get(exceptions.size() - 1));
        }

        if (abstractMethod) {
            sb.append(";\n");
        } else {
            sb.append(" {\n");
            if (code != null) {
                for (int i = 0; i < code.length; i++) {
                    sb.append("\t\t" + code[i] + "\n");
                }
            }
            sb.append("\t}");
        }
        return sb;
    }
}
