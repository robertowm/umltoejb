package br.uff.ic.transformador.core.sintaxe.java;

/**
 *
 * @author robertowm
 */
public class SintaxeJava {

    public static Classe getJavaClass() {
        return new Classe();
    }

    public static Atributo getJavaAttribute() {
        return new Atributo();
    }
}
