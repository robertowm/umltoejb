/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.transformador.core;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author robertowm
 */
public class TransformadorUmlEjb {

    private AnalisadorUml aUml;
    private AnalisadorEjb aEjb;
    private List<Class> classes;

    public TransformadorUmlEjb(AnalisadorUml aUml, AnalisadorEjb aEjb) {
        this.aUml = aUml;
        this.aEjb = aEjb;
    }

    private String lowerFirstLetter(String s) {
        return s.replaceFirst(s.substring(0,1), s.substring(0,1).toLowerCase());
    }

    private String[] tratarResultadoQuery(String resultado) {
        int inicio = resultado.indexOf("{");
        int fim = resultado.indexOf("}");

        if (inicio < 0 || fim < 0) {
            return resultado.replace(" ", "").split(",");
        }
        return resultado.substring(inicio, fim + 1).replace(" ", "").split(",");
    }

    public void transform() throws Exception {
        classes = new ArrayList<Class>();

        transformaUmlClassEmEJBKeyClass();
        transformaUmlAssociationClassEmEJBKeyClass();
    }

    private void transformaUmlClassEmEJBKeyClass() throws Exception {
        String[] nomesUmlClass = tratarResultadoQuery(aUml.query("Class.allInstances()"));

        for (String nome : nomesUmlClass) {
            if (!nome.equals("null")) {
                criaEjbKeyClassDeUmlClass(nome);
            }
        }
    }

    private void criaEjbKeyClassDeUmlClass(String umlClassName) {
        Class ejbKeyClass = new Class();
        ejbKeyClass.name = umlClassName + "Key";
        ejbKeyClass.attributes.add(new Attribute(lowerFirstLetter(umlClassName) + "ID"));

        classes.add(ejbKeyClass);
    }

    private void transformaUmlAssociationClassEmEJBKeyClass() throws Exception {
        String[] nomesUmlClass = tratarResultadoQuery(aUml.query("AssociationClass.allInstances()"));

        for (String nome : nomesUmlClass) {
            if (!nome.equals("null")) {
                criaEjbKeyClassDeUmlAssociationClass(nome);
            }
        }
    }

    private void criaEjbKeyClassDeUmlAssociationClass(String umlAssociationClassName) throws Exception {
        Class ejbKeyClass = new Class();
        ejbKeyClass.name = umlAssociationClassName + "Key";
        String[] nomesClasses = tratarResultadoQuery(aUml.query(""));

        for (String nomeClasse : nomesClasses) {
            ejbKeyClass.attributes.add(new Attribute(lowerFirstLetter(nomeClasse) + "ID"));
        }

        classes.add(ejbKeyClass);
    }
}

class Class {

    String type;
    String name;
    List<Attribute> attributes;
    List<Method> methods;

    public Class() {
        this.attributes = new ArrayList<Attribute>();
        this.methods = new ArrayList<Method>();
    }
}

class Method {

    String returnType;
    String name;
    List<Parameter> parameters;
}

class Parameter {

    String name;
    String type;
}

class Attribute {

    String visibility;
    String name;
    String type;

    public Attribute(String name) {
        this.visibility = "private";
        this.type = "Integer";
        this.name = name;
    }

    public Attribute(String visibility, String name, String type) {
        this.visibility = visibility;
        this.name = name;
        this.type = type;
    }
}
