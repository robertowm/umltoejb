package br.uff.ic.transformer.core.syntax.java;

import br.uff.ic.transformer.core.util.XFile;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author robertowm
 */
public class Class implements Persistent {

    private String directoryPath;
    private String packagePath;
    private String name;
    private String visibility;
    private boolean abstractClass = false;
    private String classNameThatExtends;
    private List<String> classNamesThatImplements;
    private List<Attribute> attributes;
    private List<String> importPaths;
    private List<Constructor> constructors;
    private List<Method> methods;

    protected Class() {
    }

    @Override
    public void persist() {
        String path = directoryPath;
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

        if (abstractClass) {
            sourceCode.append("abstract ");
        }

        sourceCode.append("class " + name);

        if (classNameThatExtends != null && !"".equals(classNameThatExtends.trim())) {
            sourceCode.append(" extends " + classNameThatExtends);
        }

        if (classNamesThatImplements != null && !classNamesThatImplements.isEmpty()) {
            sourceCode.append(" implements ");
            for (int i = 0; i < classNamesThatImplements.size() - 1; i++) {
                sourceCode.append(classNamesThatImplements.get(i) + ", ");
            }
            sourceCode.append(classNamesThatImplements.get(classNamesThatImplements.size() - 1));
        }

        sourceCode.append(" {\n\n");

        if (attributes != null) {
            for (Attribute atributo : attributes) {
                sourceCode.append(atributo.serialize() + "\n");
            }
            sourceCode.append("\n");
        }


        if (constructors != null) {
            for (Constructor construtor : constructors) {
                sourceCode.append(construtor.serialize() + "\n");
            }
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

    public Class setName(String name) {
        this.name = name;
        return this;
    }

    public String getPackagePath() {
        return this.packagePath;
    }

    public Class setPackagePath(String packagePath) {
        this.packagePath = packagePath;
        return this;
    }

    public String getDirectoryPath() {
        return this.directoryPath;
    }

    public Class setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
        if (this.directoryPath.charAt(this.directoryPath.length() - 1) != '/') {
            this.directoryPath += "/";
        }
        return this;
    }

    public boolean isAbstractClass() {
        return this.abstractClass;
    }

    public Class setAbstractClass(boolean abstractClass) {
        this.abstractClass = abstractClass;
        return this;
    }

    public String getVisibility() {
        return this.visibility;
    }

    public Class setVisibility(String visibility) {
        this.visibility = visibility;
        return this;
    }

    public String getClassNameThatExtends() {
        return this.classNameThatExtends;
    }

    public Class setClassNameThatExtends(String classNameThatExtends) {
        this.classNameThatExtends = classNameThatExtends;
        return this;
    }

    public List<String> getClassNamesThatImplements() {
        return this.classNamesThatImplements;
    }

    public Class addClassNamesThatImplements(String className) {
        if (classNamesThatImplements == null) {
            classNamesThatImplements = new ArrayList<String>();
        }
        classNamesThatImplements.add(className);
        return this;
    }

    public List<Attribute> getAttributes() {
        return this.attributes;
    }

    public Class addAttribute(Attribute attribute) {
        if (attributes == null) {
            attributes = new ArrayList<Attribute>();
        }
        attributes.add(attribute);
        return this;
    }

    public List<String> getImportPaths() {
        return this.importPaths;
    }

    public Class addImportPath(String importPath) {
        if (importPaths == null) {
            importPaths = new ArrayList<String>();
        }
        importPaths.add(importPath);
        return this;
    }

    public List<Constructor> getConstructors() {
        return this.constructors;
    }

    public Class addConstructor(Constructor constructor) {
        if (constructors == null) {
            constructors = new ArrayList<Constructor>();
        }
        constructors.add(constructor);
        return this;
    }

    public List<Method> getMethods() {
        return this.methods;
    }

    public Class addMethod(Method method) {
        if (methods == null) {
            methods = new ArrayList<Method>();
        }
        methods.add(method);
        return this;
    }
}
