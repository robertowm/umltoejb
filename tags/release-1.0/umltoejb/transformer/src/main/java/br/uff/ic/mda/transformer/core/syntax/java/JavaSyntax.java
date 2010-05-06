package br.uff.ic.mda.transformer.core.syntax.java;

/**
 *
 * @author robertowm
 */
public class JavaSyntax {

    public static Class getJavaClass() {
        return new Class();
    }

    public static Interface getJavaInterface() {
        return new Interface();
    }

    public static Attribute getJavaAttribute() {
        return new Attribute();
    }

    public static Method getJavaMethod() {
        return new Method();
    }

    public static Parameter getJavaParameter() {
        return new Parameter();
    }

    public static Constructor getJavaConstructor() {
        return new Constructor();
    }
}
