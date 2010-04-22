package br.uff.ic.transformador.core.sintaxe.java;

/**
 *
 * @author robertowm
 */
public class SintaxeJava {

    public static Classe getJavaClass() {
        return new Classe();
    }

    public static Interface getJavaInterface() {
        return new Interface();
    }

    public static Atributo getJavaAttribute() {
        return new Atributo();
    }

    public static Metodo getJavaMethod() {
        return new Metodo();
    }

    public static Parametro getJavaParameter() {
        return new Parametro();
    }

    public static Construtor getJavaConstructor() {
        return new Construtor();
    }
}
