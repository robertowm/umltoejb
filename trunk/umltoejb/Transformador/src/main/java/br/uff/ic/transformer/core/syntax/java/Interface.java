package br.uff.ic.transformer.core.syntax.java;

import br.uff.ic.transformer.core.util.XFile;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author robertowm
 */
public class Interface implements Persistent {

    private String directoryPath;
    private String packagePath;
    private String name;
    private String visibility;
    private List<String> classNamesThatExtends;
    private List<Attribute> constants;
    private List<String> importPaths;
    private List<Method> methods;

    protected Interface() {
    }

    @Override
    public void persist() {
        String path = "";
        if (directoryPath != null && !"".equals(directoryPath.trim())) {
            path += directoryPath;
        }
        if (packagePath != null && !"".equals(packagePath.trim())) {
            path += packagePath.replace(".", "/") + "/";
        }
        XFile output = new XFile(path + name + ".java");

        StringBuffer sourceCode = new StringBuffer();

        if (packagePath != null && !"".equals(packagePath.trim())) {
            sourceCode.append("package " + packagePath + ";\n\n");
        }

        if (importPaths != null && !importPaths.isEmpty()) {
            for (String string : importPaths) {
                sourceCode.append("import " + string + ";\n");
            }
            sourceCode.append("\n");
        }

        if (visibility != null && !"".equals(visibility.trim())) {
            sourceCode.append(visibility + " ");
        }

        sourceCode.append("interface " + name);

        if (classNamesThatExtends != null && !classNamesThatExtends.isEmpty()) {
            sourceCode.append(" extends ");
            for (int i = 0; i < classNamesThatExtends.size() - 1; i++) {
                sourceCode.append(classNamesThatExtends.get(i) + ", ");
            }
            sourceCode.append(classNamesThatExtends.get(classNamesThatExtends.size() - 1));
        }

        sourceCode.append(" {\n\n");

        if (constants != null) {
            for (Attribute atributo : constants) {
                sourceCode.append(atributo.serialize() + "\n");
            }
            sourceCode.append("\n");
        }

        if (methods != null) {
            for (Method metodo : methods) {
                sourceCode.append(metodo.serialize() + "\n\n");
            }
        }

        sourceCode.append("\n}");

        output.substituteFor(sourceCode);
    }

    public String getName() {
        return this.name;
    }

    public Interface setName(String name) {
        this.name = name;
        return this;
    }

    public String getPackagePath() {
        return this.packagePath;
    }

    public Interface setPackagePath(String packagePath) {
        this.packagePath = packagePath;
        return this;
    }

    public String getDirectoryPath() {
        return this.directoryPath;
    }

    public Interface setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
        return this;
    }

    public String getVisibility() {
        return this.visibility;
    }

    public Interface setVisibility(String visibility) {
        this.visibility = visibility;
        return this;
    }

    public List<String> getClassNamesThatExtends() {
        return this.classNamesThatExtends;
    }

    public Interface addClassNamesThatExtends(String className) {
        if (classNamesThatExtends == null) {
            classNamesThatExtends = new ArrayList<String>();
        }
        classNamesThatExtends.add(className);
        return this;
    }

    public List<Attribute> getConstants() {
        return this.constants;
    }

    public Interface addConstant(Attribute constant) {
        if (constants == null) {
            constants = new ArrayList<Attribute>();
        }
        constant.setVisibility("public");
        constants.add(constant);
        return this;
    }

    public List<String> getImportPaths() {
        return this.importPaths;
    }

    public Interface addImportPath(String importPath) {
        if (importPaths == null) {
            importPaths = new ArrayList<String>();
        }
        importPaths.add(importPath);
        return this;
    }

    public List<Method> getMethods() {
        return this.methods;
    }

    public Interface addMethod(Method method) {
        if (methods == null) {
            methods = new ArrayList<Method>();
        }
        methods.add(method);
        return this;
    }

    public Interface addMethod(String type, String name, Parameter... parameters) {
        Method metodo = new Method().setVisibility("public").setReturnType(type).setName(name, true);
        for (Parameter parametro : parameters) {
            metodo.addParameter(parametro);
        }
        return this.addMethod(metodo);
    }
}
