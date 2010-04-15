package br.uff.ic.mda.transformador;

/**
 *
 * @author robertowm
 */
public class TransformadorUmlEjb extends Transformador<DominioUml, DominioEjb, DominioUmlEjb> {

    public TransformadorUmlEjb(DominioUml dUml, DominioEjb dEjb, DominioUmlEjb dUmlEjb) {
        super(dUml, dEjb, dUmlEjb);
    }

    public String transformaIdUmlParaEjb(String id) {
        // Transf. DataType
        if ("Integer".equals(id)) {
            return "EJBInteger";
        } else if ("Double".equals(id)) {
            return "EJBDouble";
        } else if ("Real".equals(id)) {
            return "EJBReal";
        } else if ("String".equals(id)) {
            return "EJBString";
        } else if ("Date".equals(id)) {
            return "EJBDate";
        } else if ("Boolean".equals(id)) {
            return "EJBBoolean";
        } else {
            return id;
        }
    }

    public String transformaIdUmlParaEjb(String id, String tipoEJB) throws Exception {
        String novoId = transformaIdUmlParaEjb(id);
        if (!novoId.equals(id)) {
            return novoId;
        }

        String nameUml = origem.query(id + ".name").replace("'", "");
        String[] ids = tratarResultadoQuery(destino.query(tipoEJB + ".allInstances()->select(a | a.name = '" + nameUml + "')"));
        if (ids == null || ids.length == 0) {
            return id;
        } else {
            return ids[0];
        }
    }

    @Override
    public void transform() throws Exception {
        transformaUmlClassEmEJBKeyClass();
        transformaUmlAssociationClassEmEJBKeyClass();
        transformaUMLClassEmEJBEntityComponent();
    }

    private void transformaUmlClassEmEJBKeyClass() throws Exception {
        String[] idsUmlClass = tratarResultadoQuery(origem.query("Class.allInstances()->select(c : Class | not c.oclIsKindOf(AssociationClass))"));

        for (String id : idsUmlClass) {
            String nome = origem.query(id + ".name").replace("'", "");
            if (!nome.equals("null")) {
                criaEjbKeyClassDeUmlClass(id, nome);
            }
        }
    }

    private void criaEjbKeyClassDeUmlClass(String umlClassId, String umlClassName) throws Exception {
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
            String nome = origem.query(id + ".name").replace("'", "");
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
        destino.insertEJBEntityComponent(ejbEntityComponentId, ejbEntityComponentName);

        resultado = tratarResultadoQuery(origem.query(id + ".getAllContained(" + id + ".emptySet(), " + id + ".emptySet()->including(" + id + ")).feature->select(f : Feature | f.oclIsKindOf(Operation))"));
        for (String idOperation : resultado) {
            // Insere operacao no EJBEntityComponent
            String operationName = origem.query(idOperation + ".name").replace("'", "");
            String operationId = operationName + System.nanoTime();
            String operationTypeId = transformaIdUmlParaEjb(tratarResultadoQuery(origem.query(idOperation + ".classifier"))[0], "EJBDataClass");
            destino.insertBusinessMethod(operationId, operationName, operationTypeId, ejbEntityComponentId);

            // Insere os parametros da Operation
            for (String idParameter : tratarResultadoQuery(origem.query(idOperation + ".parameter"))) {
                String parameterName = origem.query(idParameter + ".name").replace("'", "");
                String parameterId = parameterName + System.nanoTime();
                String parameterTypeId = transformaIdUmlParaEjb(tratarResultadoQuery(origem.query(idParameter + ".classifier"))[0], "EJBDataClass");
                destino.insertEJBParameter(parameterId, parameterName, parameterTypeId, operationId);
            }
        }

        // Table
        resultado = tratarResultadoQuery(origem.query(id + ".getAllContained(" + id + ".emptySet(), " + id + ".emptySet()->including(" + id + "))"));
        for (String idClass : resultado) {
            String tableName = origem.query(idClass + ".name").replace("'", "");
            String tableId = tableName + System.nanoTime();
            destino.insertTable(tableId, tableName, ejbEntityComponentId);
        }

        // EJBDataSchema
        String ejbDataSchemaName = nome;
        String ejbDataSchemaId = ejbDataSchemaName + System.nanoTime();
        destino.insertEJBDataSchema(ejbDataSchemaId, ejbDataSchemaName);

        // EJBDataClass
        String ejbDataClassName = nome;
        String ejbDataClassId = ejbDataClassName + System.nanoTime();
        destino.insertEJBDataClass(ejbDataClassId, ejbDataClassName, ejbDataSchemaId);
        // - Inserindo Attribute
        resultado = tratarResultadoQuery(origem.query(id + ".feature->collect( t | t.oclAsType(Feature))->select(f : Feature | f.oclIsKindOf(Attribute))"));
        for (String idAttribute : resultado) {
            String attributeName = origem.query(idAttribute + ".name").replace("'", "");
            String attributeId = attributeName + System.nanoTime();
            String attributeVisibility = origem.query(idAttribute + ".visibility").replace("'", "");
            String attributeTypeId = transformaIdUmlParaEjb(tratarResultadoQuery(origem.query(idAttribute + ".classifier"))[0], "EJBDataClass");
            destino.insertEJBAttribute(attributeId, attributeName, attributeVisibility, attributeTypeId, ejbDataClassId);
        }
        // - Inserindo AssociationEnd
        resultado = tratarResultadoQuery(origem.query(id + ".feature->collect( t | t.oclAsType(Feature))->select(f : Feature | f.oclIsKindOf(AssociationEnd))"));
        for (String idAssociationEnd : resultado) {
            String associationEndName = origem.query(idAssociationEnd + ".name").replace("'", "");
            String associationEndId = associationEndName + System.nanoTime();
            String associationEndLower = origem.query(idAssociationEnd + ".lower").replace("'", "");
            String associationEndUpper = origem.query(idAssociationEnd + ".upper").replace("'", "");
            Boolean associationEndComposition = "true".equals(origem.query(idAssociationEnd + ".composition").replace("'", ""));
            String associationEndTypeId = transformaIdUmlParaEjb(tratarResultadoQuery(origem.query(idAssociationEnd + ".classifier"))[0], "EJBDataClass");
            destino.insertEJBAssociationEnd(associationEndId, associationEndName, associationEndLower, associationEndUpper, associationEndComposition, associationEndTypeId, ejbDataClassId);
        }

        // EJBServingAttribute
        String ejbServingAttributeName = nome;
        String ejbServingAttributeId = ejbServingAttributeName + System.nanoTime();
        String ejbServingAttributeLower = "1";
        String ejbServingAttributeUpper = "1";
        Boolean ejbServingAttributeComposition = false;
        destino.insertEJBServingAttribute(ejbServingAttributeId, ejbServingAttributeName, ejbServingAttributeLower, ejbServingAttributeUpper, ejbServingAttributeComposition, ejbDataClassId, ejbEntityComponentId);

        // Juncao
        juncao.insertUMLClassToEJBEntityComponent(id, ejbEntityComponentId, ejbDataClassId, ejbDataSchemaId, ejbServingAttributeId);
    }
}
