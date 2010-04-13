package br.uff.ic.mda.transformador;

/**
 *
 * @author robertowm
 */
public class TransformadorUmlEjb extends Transformador<DominioUml, DominioEjb, DominioUmlEjb> {

    public TransformadorUmlEjb(DominioUml dUml, DominioEjb dEjb, DominioUmlEjb dUmlEjb) {
        super(dUml, dEjb, dUmlEjb);
    }

    @Override
    public void transform() throws Exception {
        transformaUmlClassEmEJBKeyClass();
        transformaUmlAssociationClassEmEJBKeyClass();
//        transformaUMLClassEmEJBEntityComponent();
    }

    private void transformaUmlClassEmEJBKeyClass() throws Exception {
        String[] idsUmlClass = tratarResultadoQuery(origem.query("Class.allInstances()->select(c : Class | not c.oclIsKindOf(AssociationClass))"));

        for (String id : idsUmlClass) {
            String nome = origem.query(id + ".name").replace("'","");
            if (!nome.equals("null")) {
                criaEjbKeyClassDeUmlClass(id, nome);
            }
        }
    }

    private void criaEjbKeyClassDeUmlClass(String umlClassId, String umlClassName) {
        String ejbKeyClassName = umlClassName + "PK";
        String ejbKeyClassId = ejbKeyClassName + System.nanoTime();
        String ejbAttributeName = umlClassName + "ID";
        String ejbAttributeId = ejbAttributeName + System.nanoTime();
        destino.insertEJBKeyClass(ejbKeyClassId, ejbKeyClassName);
        destino.insertEJBAttribute(ejbAttributeId, ejbAttributeName, "public", "EJBInteger", ejbKeyClassId);

        juncao.insertUMLClassToEJBKeyClass(umlClassId, ejbKeyClassId, ejbAttributeId);
    }

    private void transformaUmlAssociationClassEmEJBKeyClass() throws Exception {
        String[] idsUmlClass = tratarResultadoQuery(origem.query("AssociationClass.allInstances()"));

        for (String id : idsUmlClass) {
            String nome = origem.query(id + ".name").replace("'","");
            if (!nome.equals("null")) {
                criaEjbKeyClassDeUmlAssociationClass(id, nome);
            }
        }
    }

    private void criaEjbKeyClassDeUmlAssociationClass(String umlAssociationClassId, String umlAssociationClassName) throws Exception {
        String ejbKeyClassName = umlAssociationClassName + "PK";
        String ejbKeyClassId = ejbKeyClassName + System.nanoTime();
        destino.insertEJBKeyClass(ejbKeyClassId, ejbKeyClassName);

        String id = origem.query(umlAssociationClassId + ".associationEnds->asOrderedSet()->first()").replace("'", "");

        String firstName = origem.query(id + ".name").replace("'", "") + "ID";
        String firstId = firstName + System.nanoTime();
        destino.insertEJBAttribute(firstId, firstName, "public", "EJBInteger", ejbKeyClassId);

        String otherName = origem.query(id + ".otherEnd.name->asOrderedSet()->first()").replace("'", "") + "ID";
        String otherId = otherName + System.nanoTime();
        destino.insertEJBAttribute(otherId, otherName, "public", "EJBInteger", ejbKeyClassId);

        juncao.insertUMLAssociationClassToEJBKeyClass(umlAssociationClassId, ejbKeyClassId, firstId, otherId);
    }

    private void transformaUMLClassEmEJBEntityComponent() throws Exception {
        String[] idsUMLClass = tratarResultadoQuery(origem.query("Class.allInstances()->select(c : Class | not c.feature->select(f : Feature | f.oclIsKindOf(AssociationEnd))->collect(ft : Feature | ft.oclAsType(AssociationEnd))->exists(ae : AssociationEnd | ae.composition = true))"));

        for (String id : idsUMLClass) {
            String nome = origem.query(id + ".name").replace("'", "");
            if (!nome.equals("null")) {
                criarEjbEntityComponentDeUmlClass(id, nome);
            }
        }
    }

    private void criarEjbEntityComponentDeUmlClass(String id, String nome) throws Exception {
        String[] resultado;
        // EJBEntityComponent
        String ejbEntityComponentName = nome;
        String ejbEntityComponentId = ejbEntityComponentName + System.nanoTime();
        resultado = tratarResultadoQuery(origem.query(""));
        for (String idOperation : resultado) {
            // Insere operacao no EJBEntityComponent
        }
        // INSERIR EJB


        // EJBDataClass
        String ejbDataClassName = nome;
        String ejbDataClassId = ejbDataClassName + System.nanoTime();
        // INSERIR EJB


        // EJBDataSchema
        String ejbDataSchemaName = nome;
        String ejbDataSchemaId = ejbDataSchemaName + System.nanoTime();
        // INSERIR EJB


        // EJBServingAttribute
        String ejbServingAttributeName = nome;
        String ejbServingAttributeId = ejbServingAttributeName + System.nanoTime();
        // INSERIR EJB

        // INSERIR JUNCAO
    }
}