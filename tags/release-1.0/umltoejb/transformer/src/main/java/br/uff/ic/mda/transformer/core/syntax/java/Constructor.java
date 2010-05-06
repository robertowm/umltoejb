package br.uff.ic.mda.transformer.core.syntax.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constructor {

    private List<Parameter> parameters;
    private String code;
    private String className;

    public Constructor() {
    }

    public Constructor(String className, String code, Parameter... parameters) {
        this(className, code, Arrays.asList(parameters));
    }

    public Constructor(String className, String code, List<Parameter> parameters) {
        this.className = className;
        this.code = code;
        this.parameters = parameters;
    }

    public List<Parameter> getParameters() {
        return this.parameters;
    }

    public Constructor addParameter(Parameter parameter) {
        if (parameters == null) {
            parameters = new ArrayList<Parameter>();
        }
        parameters.add(parameter);
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public Constructor setCode(String code) {
        this.code = code;
        return this;
    }

    public String getClassName() {
        return this.className;
    }

    public Constructor setClassName(String className) {
        this.className = className;
        return this;
    }

    public StringBuffer serialize() {
        StringBuffer sb = new StringBuffer();
        sb.append("\tpublic " + className);
        if (parameters != null && !parameters.isEmpty()) {
            sb.append("(");
            for (int i = 0; i < parameters.size() - 1; i++) {
                sb.append(parameters.get(i).getType() + " " + parameters.get(i).getName() + ", ");
            }
            sb.append(parameters.get(parameters.size() - 1).getType() + " " + parameters.get(parameters.size() - 1).getName() + ")");
        } else {
            sb.append("()");
        }
        sb.append(" {\n");
        if (code != null && !code.trim().isEmpty()) {
            for (String string : code.split("\n")) {
                sb.append("\t\t" + string + "\n");
            }
        }
        sb.append("\t}\n");
        return sb;
    }
}
