package br.uff.ic.transformador.core;

/**
 *
 * @author robertowm
 */
public class TransformadorUmlEjb extends Transformador<DominioUmlLivro, DominioEjb, DominioUmlEjb> {

    public TransformadorUmlEjb(DominioUmlLivro dUml, DominioEjb dEjb, DominioUmlEjb dUmlEjb) {
        super(dUml, dEjb, dUmlEjb);
    }

    @Override
    public void transform() throws Exception {
        transformaUmlClassEmEJBKeyClass();
//        transformaUmlAssociationClassEmEJBKeyClass();
    }

    private void transformaUmlClassEmEJBKeyClass() throws Exception {
        String[] nomesUmlClass = tratarResultadoQuery(origem.query("Class.allInstances()"));

        for (String nome : nomesUmlClass) {
            if (!nome.equals("nullC")) {
                criaEjbKeyClassDeUmlClass(nome);
            }
        }
    }

    private void criaEjbKeyClassDeUmlClass(String umlClassName) {
        String ejbKeyClassName = umlClassName + "Key";
        String ejbAttributeName = umlClassName + "ID";
        destino.insertEJBKeyClass(ejbKeyClassName);
        destino.insertEJBAttribute(ejbAttributeName, "private", "int", ejbKeyClassName);
    }

//    private void transformaUmlAssociationClassEmEJBKeyClass() throws Exception {
//        String[] nomesUmlClass = tratarResultadoQuery(aUml.query("AssociationClass.allInstances()"));
//
//        for (String nome : nomesUmlClass) {
//            if (!nome.equals("null")) {
//                criaEjbKeyClassDeUmlAssociationClass(nome);
//            }
//        }
//    }
//
//    private void criaEjbKeyClassDeUmlAssociationClass(String umlAssociationClassName) throws Exception {
//        Class ejbKeyClass = new Class();
//        ejbKeyClass.name = umlAssociationClassName + "Key";
//        String[] nomesClasses = tratarResultadoQuery(aUml.query(""));
//
//        for (String nomeClasse : nomesClasses) {
//            ejbKeyClass.attributes.add(new Attribute(lowerFirstLetter(nomeClasse) + "ID"));
//        }
//
//        classes.add(ejbKeyClass);
//    }
}