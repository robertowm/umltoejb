package br.uff.ic.mda.transformador;

/**
 *
 * @author robertowm
 */
public class TransformadorUmlEjb extends Transformador<DominioUml, DominioEjb, DominioUmlEjb> {

    public TransformadorUmlEjb(DominioUml dUml, DominioEjb dEjb, DominioUmlEjb dUmlEjb) {
        super(dUml, dEjb, dUmlEjb);
    }

    private String findEjbDataTypeBasedonUmlId(String umlId) throws Exception {
        String[] resultado = null;

        // Procura por um DataType -> EjbDataType
        // Regra Minha
        resultado = tratarResultadoQuery(juncao.query("UMLDataTypeToEJBDataType.allInstances()->select(a | a.dataType->exists(dt : DataType | dt = " + umlId + "))"));
        if (resultado != null && resultado.length > 0 && !"".equals(resultado[0])) {
            return tratarResultadoQuery(juncao.query(resultado[0] + ".ejbDataType"))[0];
        }
        return null;
    }

    private String findEjbDataClassBasedonUmlId(String umlId) throws Exception {
        String[] resultado = null;

        // Procura por um Class -> EJBDataClass
        // Regra 3
        resultado = tratarResultadoQuery(juncao.query("UMLClassToEJBEntityComponent.allInstances()->select(a | a.class->includes(" + umlId + "))"));
        if (resultado != null && resultado.length > 0 && !"".equals(resultado[0])) {
            return tratarResultadoQuery(juncao.query(resultado[0] + ".dataClass"))[0];
        }
        // Regra 4
        resultado = tratarResultadoQuery(juncao.query("UMLClassToEJBDataClass.allInstances()->select(a | a.class->includes(" + umlId + "))"));
        if (resultado != null && resultado.length > 0 && !"".equals(resultado[0])) {
            return tratarResultadoQuery(juncao.query(resultado[0] + ".ejbDataClass"))[0];
        }
        // Regra 6
        resultado = tratarResultadoQuery(juncao.query("UMLAssociationClassToEJBDataClass.allInstances()->select(a | a.associationClass->includes(" + umlId + "))"));
        if (resultado != null && resultado.length > 0 && !"".equals(resultado[0])) {
            return tratarResultadoQuery(juncao.query(resultado[0] + ".ejbDataClass"))[0];
        }

        return null;
    }

    private String findEjbKeyClassBasedonUmlId(String umlId) throws Exception {
        String[] resultado = null;
        // Procura por um Class -> EJBKeyClass
        // Regra 1
        resultado = tratarResultadoQuery(juncao.query("UMLClassToEJBKeyClass.allInstances()->select(a | a.class->exists(c : Class | c = " + umlId + "))"));
        if (resultado != null && resultado.length > 0 && !"".equals(resultado[0])) {
            return tratarResultadoQuery(juncao.query(resultado[0] + ".keyClass"))[0];
        }
        // Regra 2
        resultado = tratarResultadoQuery(juncao.query("UMLAssociationClassToEJBKeyClass.allInstances()->select(a | a.associationClass->exists(ac : AssociationClass | ac = " + umlId + "))"));
        if (resultado != null && resultado.length > 0 && !"".equals(resultado[0])) {
            return tratarResultadoQuery(juncao.query(resultado[0] + ".keyClass"))[0];
        }

        return null;
    }

    private String findEjbIdBasedonUmlId(String umlId) throws Exception {
        String r;
        try {
            r = findEjbDataTypeBasedonUmlId(umlId);
        } catch (Exception ex) {
            r = null;
        }
        if (r == null) {
            try {
                r = findEjbDataClassBasedonUmlId(umlId);
            } catch (Exception ex) {
                r = null;
            }
            if (r == null) {
                try {
                    r = findEjbKeyClassBasedonUmlId(umlId);
                } catch (Exception ex) {
                    r = null;
                }
                if (r == null) {
                    // Nao encontrou -> Dispara excecao
                    throw new Exception("Can't find correspondent EJB object to UML object[id: " + umlId + "]");
                }
            }
        }
        return r;
    }

    private String findEJBAssociationEndBasedonUmlAssociationEnd(String idAssociationEnd) throws Exception {
        String[] resultado = null;

        // Regra 8
        resultado = tratarResultadoQuery(juncao.query("UMLAssociationEndToEJBDataEndpelaRegra8.allInstances()->select(a | a.associationEnd->exists(ae : AssociationEnd | ae = " + idAssociationEnd + "))"));
        if (resultado != null && resultado.length > 0 && !"".equals(resultado[0])) {
            return tratarResultadoQuery(juncao.query(resultado[0] + ".ejbAssociationEnd"))[0];
        }
        // Regra 9
        resultado = tratarResultadoQuery(juncao.query("UMLAssociationEndToEJBDataEndpelaRegra9.allInstances()->select(a | a.associationEnd->exists(ae : AssociationEnd | ae = " + idAssociationEnd + "))"));
        if (resultado != null && resultado.length > 0 && !"".equals(resultado[0])) {
            return tratarResultadoQuery(juncao.query(resultado[0] + ".ejbAssociationEnd"))[0];
        }
        // Regra 10
        resultado = tratarResultadoQuery(juncao.query("UMLAssociationEndEmEJBAssociationpelaRegra10.allInstances()->select(a | a.associationEnd->exists(ae : AssociationEnd | ae = " + idAssociationEnd + "))"));
        if (resultado != null && resultado.length > 0 && !"".equals(resultado[0])) {
            return tratarResultadoQuery(juncao.query(resultado[0] + ".ejbAssociationEnd2"))[0];
//            return tratarResultadoQuery(juncao.query(resultado[0] + ".ejbAssociationEnd1"))[0];
        }
        // Regra 11
        resultado = tratarResultadoQuery(juncao.query("UMLAssociationEndEmEJBAssociationpelaRegra11.allInstances()->select(a | a.associationEnd->exists(ae : AssociationEnd | ae = " + idAssociationEnd + "))"));
        if (resultado != null && resultado.length > 0 && !"".equals(resultado[0])) {
            return tratarResultadoQuery(juncao.query(resultado[0] + ".ejbAssociationEnd2"))[0];
        }

        // Nao encontrou -> Dispara excecao
        throw new Exception("Can't find correspondent EJBAssociationEnd to AsociationEnd[id: " + idAssociationEnd + "]");
    }

    @Override
    public void transform() throws Exception {
        // DataType -> EJBDataType
        transformaUMLDataTypeEmEJBDataType();

        // Cria elementos e linka (Caso unico: Class e AssociationClass -> EJBKeyClass)
        transformaRegraUMLClassEmEJBKeyClass();                 // Regra 1
        transformaRegraUMLAssociationClassEmEJBKeyClass();      // Regra 2

        // Cria objetos
        transformaRegraUMLClassEmEJBEntityComponent();          // Regra 3
        transformaRegraUMLClassEmEJBDataClass();                // Regra 4
        transformaRegraUMLAssociationEmEJBDataAssociation();    // Regra 5
        transformaRegraUMLAssociationClassEmEJBDataClass();     // Regra 6
        transformaRegraUMLAttributeEmEJBAttribute();            // Regra 7
        transformaRegra8UMLAssociationEndEmEJBAssociationEnd(); // Regra 8
        transformaRegra9UMLAssociationEndEmEJBAssociationEnd(); // Regra 9
        transformaRegra10UMLAssociationEndEmEJBAssociation();   // Regra 10
        transformaRegra11UMLAssociationEndEmEJBAssociation();   // Regra 11
        transformaRegraUMLOperationEmBusinessMethod();          // Regra 12
        transformaRegraUMLParameterEmEJBParameter();            // Regra 13

        // Cria links
        linkRegraUMLClassEmEJBEntityComponent();                // Regra 3
        linkRegraUMLAssociationEmEJBDataAssociation();          // Regra 5
        linkRegraUMLClassEmEJBDataClass();                      // Regra 4
        linkRegraUMLAssociationClassEmEJBDataClass();           // Regra 6
        linkRegraUMLAttributeEmEJBAttribute();                  // Regra 7
        linkRegra8UMLAssociationEndEmEJBAssociationEnd();       // Regra 8
        linkRegra9UMLAssociationEndEmEJBAssociationEnd();       // Regra 9
        linkRegra10UMLAssociationEndEmEJBAssociation();         // Regra 10
        linkRegra11UMLAssociationEndEmEJBAssociation();         // Regra 11
        linkRegraUMLOperationEmBusinessMethod();                // Regra 12
        linkRegraUMLParameterEmEJBParameter();                  // Regra 13

    }

    //--------------------------------------------------------------------------
    // REGRA SOBRE TIPOS DE DADOS - Completo (Cria e linka)
    private void transformaUMLDataTypeEmEJBDataType() throws Exception {
        // "Integer" -> "EJBInteger"
        destino.insertEJBDataType("EJBInteger", "EJBInteger");
        juncao.insertUMLDataTypeToEJBDataType("UMLInteger", "EJBInteger");

        // "Double" -> "EJBDouble"
        destino.insertEJBDataType("EJBDouble", "EJBDouble");
        juncao.insertUMLDataTypeToEJBDataType("UMLDouble", "EJBDouble");

        // "Real" -> "EJBDouble"
        juncao.insertUMLDataTypeToEJBDataType("UMLReal", "EJBDouble");

        // "String" -> "EJBString"
        destino.insertEJBDataType("EJBString", "EJBString");
        juncao.insertUMLDataTypeToEJBDataType("UMLString", "EJBString");

        // "Date" -> "EJBDate"
        destino.insertEJBDataType("EJBDate", "EJBDate");
        juncao.insertUMLDataTypeToEJBDataType("UMLDate", "EJBDate");

        // "Boolean" -> "EJBBoolean"
        destino.insertEJBDataType("EJBBoolean", "EJBBoolean");
        juncao.insertUMLDataTypeToEJBDataType("UMLBoolean", "EJBBoolean");
    }
    //--------------------------------------------------------------------------
    // REGRA 01 - Completo (Cria e linka)
    private void transformaRegraUMLClassEmEJBKeyClass() throws Exception {
        String[] ids = tratarResultadoQuery(origem.query("Class.allInstances()->select(c : Class | c.oclIsTypeOf(Class))"));

        for (String id : ids) {
            String nome = origem.query(id + ".name").replace("'", "");
            if (!"NULL_CLASS".equals(id)) {
                criaEJBKeyClassDeUMLClass(id, nome);
            }
        }
    }
    private void criaEJBKeyClassDeUMLClass(String umlClassId, String umlClassName) throws Exception {
        String ejbKeyClassName = umlClassName + "Key";
        String ejbKeyClassId = ejbKeyClassName + System.nanoTime();
        String ejbAttributeName = umlClassName + "ID";
        String ejbAttributeId = ejbAttributeName + System.nanoTime();
        destino.insertEJBKeyClass(ejbKeyClassId, ejbKeyClassName);
        destino.insertEJBAttribute(ejbAttributeId, ejbAttributeName, "public", "EJBInteger", ejbKeyClassId);

        juncao.insertUMLClassToEJBKeyClass(umlClassId, ejbKeyClassId, ejbAttributeId);
    }
    //--------------------------------------------------------------------------
    // REGRA 02 - Completo (Cria e linka)
    private void transformaRegraUMLAssociationClassEmEJBKeyClass() throws Exception {
        String[] ids = tratarResultadoQuery(origem.query("AssociationClass.allInstances()"));

        for (String id : ids) {
            String nome = origem.query(id + ".name").replace("'", "");
            criaEJBKeyClassDeUMLAssociationClass(id, nome);
        }
    }
    private void criaEJBKeyClassDeUMLAssociationClass(String umlAssociationClassId, String umlAssociationClassName) throws Exception {
        String ejbKeyClassName = umlAssociationClassName + "Key";
        String ejbKeyClassId = ejbKeyClassName + System.nanoTime();
        destino.insertEJBKeyClass(ejbKeyClassId, ejbKeyClassName);

        String id = origem.query(umlAssociationClassId + ".associationEnds->asOrderedSet()->first()").replace("'", "");

        String firstName = tratarResultadoQuery(origem.query(id + ".classifier.name"))[0] + "ID";
        String firstId = firstName + System.nanoTime();
        destino.insertEJBAttribute(firstId, firstName, "public", "EJBInteger", ejbKeyClassId);

        String otherId = null;
        String[] resultado = tratarResultadoQuery(origem.query(id + ".otherEnd.classifier.name"));
        if (resultado != null && resultado.length > 0 && !"".equals(resultado[0])) {
            String otherName = resultado[0] + "ID";
            otherId = otherName + System.nanoTime();
            destino.insertEJBAttribute(otherId, otherName, "public", "EJBInteger", ejbKeyClassId);
        } else {
            String otherName = tratarResultadoQuery(origem.query(id + ".class.name"))[0] + "ID";
            otherId = otherName + System.nanoTime();
            destino.insertEJBAttribute(otherId, otherName, "public", "EJBInteger", ejbKeyClassId);
        }
        juncao.insertUMLAssociationClassToEJBKeyClass(umlAssociationClassId, ejbKeyClassId, firstId, otherId);
    }
    //--------------------------------------------------------------------------
    // REGRA 3
    // Criacao
    private void transformaRegraUMLClassEmEJBEntityComponent() throws Exception {
//        String[] ids = tratarResultadoQuery(origem.query("Class.allInstances()->select(c : Class | c.oclIsTypeOf(Class))->select(c : Class | not c.feature->select(f : Feature | f.oclIsKindOf(AssociationEnd))->collect(ft : Feature | ft.oclAsType(AssociationEnd))->exists(ae : AssociationEnd | ae.otherEnd->exists(oe : AssociationEnd | oe.composition = true)))"));
        String[] ids = tratarResultadoQuery(origem.query("Class.allInstances()->select(c : Class | c.oclIsTypeOf(Class))->select(c : Class | not c.feature->select(f : Feature | f.oclIsKindOf(AssociationEnd))->collect(ft : Feature | ft.oclAsType(AssociationEnd))->exists(ae : AssociationEnd | ae.composition = true))"));

        for (String id : ids) {
            String nome = origem.query(id + ".name").replace("'", "");
            if (!"NULL_CLASS".equals(id)) {
                criaEjbEntityComponentDeUmlClass(id, nome);
            }
        }
    }
    private void criaEjbEntityComponentDeUmlClass(String id, String nome) throws Exception {
        // Cria EJBEntityComponent
        String ejbEntityComponentName = nome;
        String ejbEntityComponentId = ejbEntityComponentName + System.nanoTime();
        destino.insertEJBEntityComponentStub(ejbEntityComponentId, ejbEntityComponentName);

        // Cria EJBDataSchema
        String ejbDataSchemaName = nome;
        String ejbDataSchemaId = ejbDataSchemaName + System.nanoTime();
        destino.insertEJBDataSchemaStub(ejbDataSchemaId, ejbDataSchemaName);

        // Cria EJBDataClass
        String ejbDataClassName = nome;
        String ejbDataClassId = ejbDataClassName + System.nanoTime();
        destino.insertEJBDataClassStub(ejbDataClassId, ejbDataClassName);

        // Cria EJBServingAttribute
        String ejbServingAttributeName = nome;
        String ejbServingAttributeId = ejbServingAttributeName + System.nanoTime();
        String ejbServingAttributeLower = "1";
        String ejbServingAttributeUpper = "1";
        Boolean ejbServingAttributeComposition = false;
        destino.insertEJBServingAttributeStub(ejbServingAttributeId, ejbServingAttributeName, ejbServingAttributeLower, ejbServingAttributeUpper, ejbServingAttributeComposition);

        // Cria Juncao
        juncao.insertUMLClassToEJBEntityComponent(id, ejbEntityComponentId, ejbDataClassId, ejbDataSchemaId, ejbServingAttributeId);
    }
    // Linkagem
    private void linkRegraUMLClassEmEJBEntityComponent() throws Exception {
        String[] ids = tratarResultadoQuery(origem.query("UMLClassToEJBEntityComponent.allInstances()"));

        for (String id : ids) {
            if (id != null && !"".equals(id)) {
                linkEjbEntityComponentDeUmlClass(id);
            }
        }
    }
    private void linkEjbEntityComponentDeUmlClass(String idJuncao) throws Exception {
        String idUmlClass = tratarResultadoQuery(juncao.query(idJuncao + ".class"))[0];
        String idEjbEntityComponent = tratarResultadoQuery(juncao.query(idJuncao + ".entityComponent"))[0];
        String idEjbDataClass = tratarResultadoQuery(juncao.query(idJuncao + ".dataClass"))[0];
        String idEjbDataSchema = tratarResultadoQuery(juncao.query(idJuncao + ".dataSchema"))[0];
        String idEjbServingAttribute = tratarResultadoQuery(juncao.query(idJuncao + ".servingAttribute"))[0];

        // servingAttribute.class = entityComponent
        destino.insertEJBServingAttributeClassLink(idEjbServingAttribute, idEjbEntityComponent);
        // servingAttribute.type = rootDataClass
        destino.insertEJBServingAttributeTypeLink(idEjbServingAttribute, idEjbDataClass);
        // rootDataClass.package = dataSchema
        destino.insertEJBDataClassSchemaLink(idEjbDataClass, idEjbDataSchema);

        // class.getAllContained(Set(UML::Class){}).operations() <~> entityComponent.feature
        String[] idsUmlOperations = tratarResultadoQuery(origem.query(idUmlClass + ".getAllContained().feature->select(f : Feature | f.oclIsKindOf(Operation))"));
        if (idsUmlOperations != null && idsUmlOperations.length > 0 && !"".equals(idsUmlOperations[0])) {
            for (String idOperation : idsUmlOperations) {
                String idBusinessMethod = tratarResultadoQuery(juncao.query(idOperation + ".transformerToBusinessMethod.businessMethod"))[0];
                destino.insertBusinessMethodClassLink(idBusinessMethod, idEjbEntityComponent);
            }
        }
        // class.feature->select(oclKindOf(Attribute) or oclKindOf(AssociationEnd)) <~> rootDataClass.feature
        // Separando...
        //   1) class.feature->select(oclKindOf(Attribute)) <~> rootDataClass.feature
        String[] idsUmlAttributes = tratarResultadoQuery(origem.query(idUmlClass + ".feature->select(f | f.oclIsKindOf(Attribute))"));
        if (idsUmlAttributes != null && idsUmlAttributes.length > 0 && !"".equals(idsUmlAttributes[0])) {
            for (String idAttribute : idsUmlAttributes) {
                String idEjbAttribute = tratarResultadoQuery(juncao.query(idAttribute + ".transformerToEjbAttribute.ejbAttribute"))[0];
                destino.insertEJBAttributeClassLink(idEjbAttribute, idEjbDataClass);
            }
        }
        //   2) class.feature->select(oclKindOf(AssociationEnd)) <~> rootDataClass.feature
        String[] idsUmlAssociationEnds = tratarResultadoQuery(origem.query(idUmlClass + ".feature->select(f | f.oclIsKindOf(AssociationEnd))"));
        if (idsUmlAssociationEnds != null && idsUmlAssociationEnds.length > 0 && !"".equals(idsUmlAssociationEnds[0])) {
            for (String idAssociationEnd : idsUmlAssociationEnds) {
                String idEjbAssociationEnd = findEJBAssociationEndBasedonUmlAssociationEnd(idAssociationEnd);
                destino.insertEJBAssociationEndClassLink(idEjbAssociationEnd, idEjbDataClass);
            }
        }
        // class.getAllContained(Set(UML::Class){}) <~> entityComponent.usedTable
        // --- Nao fiz pois nao tem transformacao para Table
    }
    //--------------------------------------------------------------------------
    // REGRA 4
    // Criacao
    private void transformaRegraUMLClassEmEJBDataClass() throws Exception {
//        String[] ids = tratarResultadoQuery(origem.query("Class.allInstances()->select(c : Class | c.oclIsTypeOf(Class))->select(c : Class | c.feature->select(f : Feature | f.oclIsTypeOf(AssociationEnd))->collect(f : Feature | f.oclAsType(AssociationEnd))->exists(ae : AssociationEnd | ae.otherEnd->exists(oe : AssociationEnd | oe.composition = true)))"));
        String[] ids = tratarResultadoQuery(origem.query("Class.allInstances()->select(c : Class | c.oclIsTypeOf(Class))->select(c : Class | c.feature->select(f : Feature | f.oclIsKindOf(AssociationEnd))->collect(ft : Feature | ft.oclAsType(AssociationEnd))->exists(ae : AssociationEnd | ae.composition = true))"));
        for (String id : ids) {
            String nome = origem.query(id + ".name").replace("'", "");
            if (!"NULL_CLASS".equals(id)) {
                criaEJBDataClassDeClass(id, nome);
            }
        }
    }
    private void criaEJBDataClassDeClass(String id, String nome) throws Exception {
        String ejbDataClassName = nome;
        String ejbDataClassId = ejbDataClassName + System.nanoTime();
        destino.insertEJBDataClassStub(ejbDataClassId, ejbDataClassName);

        juncao.insertUMLClassToEJBDataClass(id, ejbDataClassId);
    }
    // Linkagem
    private void linkRegraUMLClassEmEJBDataClass() throws Exception {
        String[] ids = tratarResultadoQuery(origem.query("UMLClassToEJBDataClass.allInstances()"));
        for (String id : ids) {
            if (id != null && !"".equals(id)) {
                linkEJBDataClassDeClass(id);
            }
        }
    }
    private void linkEJBDataClassDeClass(String idJuncao) throws Exception {
        String idUmlClass = tratarResultadoQuery(juncao.query(idJuncao + ".class"))[0];
        String idEjbDataClass = tratarResultadoQuery(juncao.query(idJuncao + ".ejbDataClass"))[0];

        // class.feature->select(oclKindOf(Attribute) or oclKindOf(AssociationEnd)) <~> nonRootDataClass.feature
        // Separando...
        //   1) class.feature->select(oclKindOf(Attribute)) <~> nonRootDataClass.feature
        String[] idsUmlAttributes = tratarResultadoQuery(origem.query(idUmlClass + ".feature->select(f : Feature | f.oclIsKindOf(Attribute))"));
        if (idsUmlAttributes != null && idsUmlAttributes.length > 0 && !"".equals(idsUmlAttributes[0])) {
            for (String idAttribute : idsUmlAttributes) {
                String idEjbAttribute = tratarResultadoQuery(juncao.query(idAttribute + ".transformerToEjbAttribute.ejbAttribute"))[0];
                destino.insertEJBAttributeClassLink(idEjbAttribute, idEjbDataClass);
            }
        }
        //   2) class.feature->select(oclKindOf(AssociationEnd)) <~> nonRootDataClass.feature
        String[] idsUmlAssociationEnds = tratarResultadoQuery(origem.query(idUmlClass + ".feature->select(f : Feature | f.oclIsKindOf(AssociationEnd))"));
        if (idsUmlAssociationEnds != null && idsUmlAssociationEnds.length > 0 && !"".equals(idsUmlAssociationEnds[0])) {
            for (String idAssociationEnd : idsUmlAssociationEnds) {
                String idEjbAssociationEnd = findEJBAssociationEndBasedonUmlAssociationEnd(idAssociationEnd);
                destino.insertEJBAssociationEndClassLink(idEjbAssociationEnd, idEjbDataClass);
            }
        }
        // class.getOuterMostContainer() <~> nonRootClass.package
        String idEjbDataSchema = tratarResultadoQuery(juncao.query("UMLClassToEJBEntityComponent.allInstances()->select(a | a.class->includes(" + idUmlClass + ".getOuterMostContainer()))->collect(a | a.dataSchema)"))[0];
        destino.insertEJBDataClassSchemaLink(idEjbDataClass, idEjbDataSchema);
    }
    //--------------------------------------------------------------------------
    // REGRA 5
    // Criacao
    private void transformaRegraUMLAssociationEmEJBDataAssociation() throws Exception {
        String[] ids = tratarResultadoQuery(origem.query("Association.allInstances()->select(a : Association | a.oclIsTypeOf(Association))->select(a : Association | a.associationEnds->exists(ae : AssociationEnd | ae.composition = true))"));

        for (String id : ids) {
            String nome = origem.query(id + ".name").replace("'", "");
            if (!"NULL_ASSOCIATION".equals(id)) {
                criaEJBDataAssociationDeAssociation(id, nome);
            }
        }
    }
    private void criaEJBDataAssociationDeAssociation(String id, String nome) throws Exception {
        String ejbDataAssociationName = nome;
        String ejbDataAssociationId = ejbDataAssociationName + System.nanoTime();
        destino.insertEJBDataAssociationStub(ejbDataAssociationId, ejbDataAssociationName);

        juncao.insertUMLAssociationToEJBDataAssociation(id, ejbDataAssociationId);
    }
    // Linkagem
    private void linkRegraUMLAssociationEmEJBDataAssociation() throws Exception {
        String[] ids = tratarResultadoQuery(origem.query("UMLAssociationToEJBDataAssociation.allInstances()"));

        for (String id : ids) {
            if (id != null && !"".equals(id)) {
                linkEJBDataAssociationDeAssociation(id);
            }
        }
    }
    private void linkEJBDataAssociationDeAssociation(String idJuncao) throws Exception {
        String idUmlAssociation = tratarResultadoQuery(juncao.query(idJuncao + ".association"))[0];
        String[] idsUmlAssociationEnds = tratarResultadoQuery(origem.query(idUmlAssociation + ".associationEnds"));

        String idEjbDataAssociation = tratarResultadoQuery(juncao.query(idJuncao + ".ejbDataAssociation"))[0];

        // assoc.end <~> dataAssoc.end
        if (idsUmlAssociationEnds != null && idsUmlAssociationEnds.length > 0 && !"".equals(idsUmlAssociationEnds[0])) {
            for (String idAssociationEnd : idsUmlAssociationEnds) {
                String idEjbAssociationEnd = findEJBAssociationEndBasedonUmlAssociationEnd(idAssociationEnd);
                destino.insertEJBDataAssociationEndLinks(idEjbDataAssociation, idEjbAssociationEnd);
            }
        }
        // assoc.getOuterMostContainer() <~> dataAssoc.package
        String idEjbDataSchema = tratarResultadoQuery(juncao.query("UMLClassToEJBEntityComponent.allInstances()->select(a | a.class->includes(" + idUmlAssociation + ".getOuterMostContainerFromAssociation())).dataSchema"))[0];
        destino.insertEJBDataAssociationSchemaLink(idEjbDataAssociation, idEjbDataSchema);
    }
    //--------------------------------------------------------------------------
    // REGRA 6
    // Criacao
    private void transformaRegraUMLAssociationClassEmEJBDataClass() throws Exception {
//        String[] ids = tratarResultadoQuery(origem.query("AssociationClass.allInstances()->select(ac | ac.associationEnds->exists(ae : AssociationEnd | ae.composition = true))"));
        String[] ids = tratarResultadoQuery(origem.query("AssociationClass.allInstances()"));

        for (String id : ids) {
            String nome = origem.query(id + ".name").replace("'", "");
            criaEJBDataClassDeAssociationClass(id, nome);
        }
    }
    private void criaEJBDataClassDeAssociationClass(String id, String nome) throws Exception {
        String ejbDataClassName = nome;
        String ejbDataClassId = ejbDataClassName + System.nanoTime();
        destino.insertEJBDataClassStub(ejbDataClassId, ejbDataClassName);

        juncao.insertUMLAssociationClassToEJBDataClass(id, ejbDataClassId);
    }
    // Linkagem
    private void linkRegraUMLAssociationClassEmEJBDataClass() throws Exception {
        String[] ids = tratarResultadoQuery(origem.query("UMLAssociationClassToEJBDataClass.allInstances()"));
        for (String id : ids) {
            if (id != null && !"".equals(id)) {
                linkEJBDataClassDeAssociationClass(id);
            }
        }
    }
    private void linkEJBDataClassDeAssociationClass(String idJuncao) throws Exception {
        String idUmlAssociationClass = tratarResultadoQuery(juncao.query(idJuncao + ".associationClass"))[0];
        String idEjbDataClass = tratarResultadoQuery(juncao.query(idJuncao + ".ejbDataClass"))[0];

        // associationClass.feature->select(oclKindOf(Attribute) or oclKindOf(AssociationEnd)) <~> nonRootDataClass.feature
        // Separando...
        //   1) associationClass.feature->select(oclKindOf(Attribute)) <~> nonRootDataClass.feature
        String[] idsUmlAttributes = tratarResultadoQuery(origem.query(idUmlAssociationClass + ".feature->select(f | f.oclAsType(Feature).oclIsKindOf(Attribute))"));
        if (idsUmlAttributes != null && idsUmlAttributes.length > 0 && !"".equals(idsUmlAttributes[0])) {
            for (String idAttribute : idsUmlAttributes) {
                String idEjbAttribute = tratarResultadoQuery(juncao.query(idAttribute + ".transformerToEjbAttribute.ejbAttribute"))[0];
                destino.insertEJBAttributeClassLink(idEjbAttribute, idEjbDataClass);
            }
        }
        //   2) associationClass.feature->select(oclKindOf(AssociationEnd)) <~> nonRootDataClass.feature
        String[] idsUmlAssociationEnds = tratarResultadoQuery(origem.query(idUmlAssociationClass + ".feature->select(f | f.oclAsType(Feature).oclIsKindOf(AssociationEnd))"));
        if (idsUmlAssociationEnds != null && idsUmlAssociationEnds.length > 0 && !"".equals(idsUmlAssociationEnds[0])) {
            for (String idAssociationEnd : idsUmlAssociationEnds) {
                String idEjbAssociationEnd = findEJBAssociationEndBasedonUmlAssociationEnd(idAssociationEnd);
                destino.insertEJBAssociationEndClassLink(idEjbAssociationEnd, idEjbDataClass);
            }
        }
        // associationClass.getOuterMostContainer() <~> nonRootClass.package
        String idEjbDataSchema = tratarResultadoQuery(juncao.query("UMLClassToEJBEntityComponent.allInstances()->select(a | a.class->includes(" + idUmlAssociationClass + ".getOuterMostContainer())).dataSchema"))[0];
        destino.insertEJBDataClassSchemaLink(idEjbDataClass, idEjbDataSchema);
    }
    //--------------------------------------------------------------------------
    // REGRA 7
    // Criacao
    private void transformaRegraUMLAttributeEmEJBAttribute() throws Exception {
        String[] ids = tratarResultadoQuery(origem.query("Attribute.allInstances()"));

        for (String id : ids) {
            String nome = origem.query(id + ".name").replace("'", "");
            criaEJBAttributeDeAttribute(id, nome);
        }
    }
    private void criaEJBAttributeDeAttribute(String id, String nome) throws Exception {
        String ejbAttributeName = nome;
        String ejbAttributeId = ejbAttributeName + System.nanoTime();
        String ejbAttributeVisibility = origem.query(id + ".visibility").replace("'", "");
        destino.insertEJBAttributeStub(ejbAttributeId, ejbAttributeName, ejbAttributeVisibility);

        juncao.insertUMLAttributeToEJBAttribute(id, ejbAttributeId);
    }
    // Linkagem
    private void linkRegraUMLAttributeEmEJBAttribute() throws Exception {
        String[] ids = tratarResultadoQuery(origem.query("UMLAttributeToEJBAttribute.allInstances()"));

        for (String id : ids) {
            if (id != null && !"".equals(id)) {
                linkEJBAttributeDeAttribute(id);
            }
        }
    }
    private void linkEJBAttributeDeAttribute(String idJuncao) throws Exception {
        String idUmlAttribute = tratarResultadoQuery(juncao.query(idJuncao + ".attribute"))[0];
        String umlAttributeTypeId = tratarResultadoQuery(origem.query(idUmlAttribute + ".classifier"))[0];

        String idEjbAttribute = tratarResultadoQuery(juncao.query(idJuncao + ".ejbAttribute"))[0];

        // umlAttribute.type <~> ejbAttribute.type
        String idEjbAttributeTypeId = findEjbIdBasedonUmlId(umlAttributeTypeId);
        destino.insertEJBAttributeTypeLink(idEjbAttribute, idEjbAttributeTypeId);
    }
    //--------------------------------------------------------------------------
    // REGRA 8
    // Criacao
    private void transformaRegra8UMLAssociationEndEmEJBAssociationEnd() throws Exception {
        String[] ids = tratarResultadoQuery(origem.query("AssociationEnd.allInstances()->select(ae : AssociationEnd | ae.association.oclIsTypeOf(Association))->select(ae : AssociationEnd | ae.class.getOuterMostContainer() = ae.classifier.oclAsType(Class).getOuterMostContainer())"));

        for (String id : ids) {
            String nome = origem.query(id + ".name").replace("'", "");
            criaEJBAssociationEndDeAssociationEndpelaRegra8(id, nome);
        }
    }
    private void criaEJBAssociationEndDeAssociationEndpelaRegra8(String id, String nome) throws Exception {
        String ejbAssociationEndName = nome;
        String ejbAssociationEndId = ejbAssociationEndName + System.nanoTime();
        String ejbAssociationEndUpper = origem.query(id + ".upper").replace("'", "");
        String ejbAssociationEndLower = origem.query(id + ".lower").replace("'", "");
        Boolean ejbAssociationEndComposition = "true".equals(origem.query(id + ".composition").replace("'", "")) ? true : false;
        destino.insertEJBAssociationEndStub(ejbAssociationEndId, ejbAssociationEndName, ejbAssociationEndLower, ejbAssociationEndUpper, ejbAssociationEndComposition);

        juncao.insertUMLAssociationEndToEJBDataEndpelaRegra8(id, ejbAssociationEndId);
    }
    // Linkagem
    private void linkRegra8UMLAssociationEndEmEJBAssociationEnd() throws Exception {
        String[] ids = tratarResultadoQuery(origem.query("UMLAssociationEndToEJBDataEndpelaRegra8.allInstances()"));

        for (String id : ids) {
            if (id != null && !"".equals(id)) {
                linkEJBAssociationEndDeAssociationEndpelaRegra8(id);
            }
        }
    }
    private void linkEJBAssociationEndDeAssociationEndpelaRegra8(String idJuncao) throws Exception {
        String idUmlAssociationEnd = tratarResultadoQuery(juncao.query(idJuncao + ".associationEnd"))[0];
        String idEjbAssociationEnd = tratarResultadoQuery(juncao.query(idJuncao + ".ejbAssociationEnd"))[0];

        // umlAssociationEnd.type <~> ejbAssociationEnd.type.oclAsType(EJB::EJBDataClass)
        String idUmlAssociationEndType = tratarResultadoQuery(origem.query(idUmlAssociationEnd + ".classifier"))[0];
        String idEjbAssociationEndType = findEjbDataClassBasedonUmlId(idUmlAssociationEndType);
        destino.insertEJBAssociationEndTypeLink(idEjbAssociationEnd, idEjbAssociationEndType);
    }
    //--------------------------------------------------------------------------
    // REGRA 9
    // Criacao
    private void transformaRegra9UMLAssociationEndEmEJBAssociationEnd() throws Exception {
        String[] ids = tratarResultadoQuery(origem.query("AssociationEnd.allInstances()->select(ae : AssociationEnd | ae.association.oclIsTypeOf(Association))->select(ae : AssociationEnd | not (ae.class.getOuterMostContainer() = ae.classifier.oclAsType(Class).getOuterMostContainer()))"));

        for (String id : ids) {
            String nome = origem.query(id + ".name").replace("'", "");
            criaEJBAssociationEndDeAssociationEndpelaRegra9(id, nome);
        }
    }
    private void criaEJBAssociationEndDeAssociationEndpelaRegra9(String id, String nome) throws Exception {
        String ejbAssociationEndName = nome;
        String ejbAssociationEndId = ejbAssociationEndName + System.nanoTime();
        String ejbAssociationEndUpper = origem.query(id + ".upper").replace("'", "");
        String ejbAssociationEndLower = origem.query(id + ".lower").replace("'", "");
        Boolean ejbAssociationEndComposition = "true".equals(origem.query(id + ".composition").replace("'", "")) ? true : false;
        destino.insertEJBAssociationEndStub(ejbAssociationEndId, ejbAssociationEndName, ejbAssociationEndLower, ejbAssociationEndUpper, ejbAssociationEndComposition);

        juncao.insertUMLAssociationEndToEJBDataEndpelaRegra9(id, ejbAssociationEndId);
    }
    // Linkagem
    private void linkRegra9UMLAssociationEndEmEJBAssociationEnd() throws Exception {
        String[] ids = tratarResultadoQuery(origem.query("UMLAssociationEndToEJBDataEndpelaRegra9.allInstances()"));

        for (String id : ids) {
            if (id != null && !"".equals(id)) {
                linkEJBAssociationEndDeAssociationEndpelaRegra9(id);
            }
        }
    }
    private void linkEJBAssociationEndDeAssociationEndpelaRegra9(String idJuncao) throws Exception {
        String idUmlAssociationEnd = tratarResultadoQuery(juncao.query(idJuncao + ".associationEnd"))[0];
        String idEjbAssociationEnd = tratarResultadoQuery(juncao.query(idJuncao + ".ejbAssociationEnd"))[0];

        // umlAssociationEnd.type <~> ejbAssociationEnd.type.oclAsType(EJB::EJBKeyClass)
        String idUmlAssociationEndType = tratarResultadoQuery(origem.query(idUmlAssociationEnd + ".classifier"))[0];
        String idEjbAssociationEndType = findEjbKeyClassBasedonUmlId(idUmlAssociationEndType);
        destino.insertEJBAssociationEndTypeLink(idEjbAssociationEnd, idEjbAssociationEndType);
    }
    //--------------------------------------------------------------------------
    // REGRA 10
    // Criacao
    private void transformaRegra10UMLAssociationEndEmEJBAssociation() throws Exception {
        String[] ids = tratarResultadoQuery(origem.query("AssociationEnd.allInstances()->select(ae : AssociationEnd | ae.upper <> '1')->select(ae : AssociationEnd | ae.association.oclIsTypeOf(AssociationClass))->select(ae : AssociationEnd | ae.association.oclAsType(AssociationClass).getOuterMostContainerFromAssociationClass() = ae.classifier->asOrderedSet()->first().oclAsType(Class).getOuterMostContainer())"));

        for (String id : ids) {
            String nome = origem.query(id + ".name").replace("'", "");
            criaEJBAssociationEndDeAssociationpelaRegra10(id, nome);
        }
    }
    private void criaEJBAssociationEndDeAssociationpelaRegra10(String id, String nome) throws Exception {
        // Cria EJBAssociation
        String ejbAssociationName = "";
        String ejbAssociationId = ejbAssociationName + System.nanoTime();
        destino.insertEJBDataAssociationStub(ejbAssociationId, ejbAssociationName);

        // Cria EJBAssociationEnd 1
        String ejbAssociationEnd1Name = origem.query(id + ".association.name").replace("'", "");
        String ejbAssociationEnd1Id = ejbAssociationEnd1Name + System.nanoTime();
        String ejbAssociationEnd1Lower = "0";
        String ejbAssociationEnd1Upper = "*";
        Boolean ejbAssociationEnd1Composition = false;
        destino.insertEJBAssociationEndStub(ejbAssociationEnd1Id, ejbAssociationEnd1Name, ejbAssociationEnd1Lower, ejbAssociationEnd1Upper, ejbAssociationEnd1Composition);

        // Cria EJBAssociationEnd 2
        String ejbAssociationEnd2Name = tratarResultadoQuery(origem.query(id + ".classifier.name"))[0];
        String ejbAssociationEnd2Id = ejbAssociationEnd2Name + System.nanoTime();
        String ejbAssociationEnd2Lower = "1";
        String ejbAssociationEnd2Upper = "1";
        Boolean ejbAssociationEnd2Composition = "true".equals(origem.query(id + ".composition").replace("'", "")) ? true : false;
        destino.insertEJBAssociationEndStub(ejbAssociationEnd2Id, ejbAssociationEnd2Name, ejbAssociationEnd2Lower, ejbAssociationEnd2Upper, ejbAssociationEnd2Composition);

        juncao.insertUMLAssociationEndEmEJBAssociationpelaRegra10(id, ejbAssociationId, ejbAssociationEnd1Id, ejbAssociationEnd2Id);
    }
    // Linkagem
    private void linkRegra10UMLAssociationEndEmEJBAssociation() throws Exception {
        String[] ids = tratarResultadoQuery(juncao.query("UMLAssociationEndEmEJBAssociationpelaRegra10.allInstances()"));

        for (String id : ids) {
            if (id != null && !"".equals(id)) {
                linkEJBAssociationEndDeAssociationpelaRegra10(id);
            }
        }
    }
    private void linkEJBAssociationEndDeAssociationpelaRegra10(String idJuncao) throws Exception {
        String idUmlAssociationEnd = tratarResultadoQuery(juncao.query(idJuncao + ".associationEnd"))[0];
        String idEjbDataAssociation = tratarResultadoQuery(juncao.query(idJuncao + ".ejbDataAssociation"))[0];
        String idEjbAssociationEnd1 = tratarResultadoQuery(juncao.query(idJuncao + ".ejbAssociationEnd1"))[0];
        String idEjbAssociationEnd2 = tratarResultadoQuery(juncao.query(idJuncao + ".ejbAssociationEnd2"))[0];

        // ejbAssociationEnd1.association = ejbAssociation && ejbAssociationEnd2.association = ejbAssociation
        destino.insertEJBDataAssociationEndLinks(idEjbDataAssociation, idEjbAssociationEnd1, idEjbAssociationEnd2);

        // umlAssociationEnd.type <~> ejbAssociationEnd2.type.oclAsType(EJB::EJBDataClass)
        String umlAssociationEndTypeId = tratarResultadoQuery(origem.query(idUmlAssociationEnd + ".classifier"))[0];
        String ejbDataClass = findEjbDataClassBasedonUmlId(umlAssociationEndTypeId);
        destino.insertEJBAssociationEndTypeLink(idEjbAssociationEnd2, ejbDataClass);
        // umlAssociationEnd.type <~> ejbAssociationEnd1.class.oclAsType(EJB::EJBDataClass)
        destino.insertEJBAssociationEndClassLink(idEjbAssociationEnd1, ejbDataClass);

        // umlAssociationEnd.association <~> ejbAssociationEnd1.type.oclAsType(EJB::EJBDataClass)
        String umlAssociationClassId = tratarResultadoQuery(origem.query(idUmlAssociationEnd + ".association"))[0];
        ejbDataClass = findEjbDataClassBasedonUmlId(umlAssociationClassId);
        destino.insertEJBAssociationEndTypeLink(idEjbAssociationEnd1, ejbDataClass);
        // umlAssociationEnd.association <~> ejbAssociationEnd2.class.oclAsType(EJB::EJBDataClass)
        destino.insertEJBAssociationEndClassLink(idEjbAssociationEnd2, ejbDataClass);
    }
    //--------------------------------------------------------------------------
    // REGRA 11
    // Criacao
    private void transformaRegra11UMLAssociationEndEmEJBAssociation() throws Exception {
        String[] ids = tratarResultadoQuery(origem.query("AssociationEnd.allInstances()->select(ae : AssociationEnd | ae.upper <> '1')->select(ae : AssociationEnd | ae.association.oclIsTypeOf(AssociationClass))->select(ae : AssociationEnd | ae.association.oclAsType(AssociationClass).getOuterMostContainerFromAssociationClass() <> ae.classifier->asOrderedSet()->first().oclAsType(Class).getOuterMostContainer())"));

        for (String id : ids) {
            String nome = origem.query(id + ".name").replace("'", "");
            criaEJBAssociationEndDeAssociationpelaRegra11(id, nome);
        }
    }
    private void criaEJBAssociationEndDeAssociationpelaRegra11(String id, String nome) throws Exception {
        // Cria EJBAssociationEnd 2
        String ejbAssociationEnd2Name = tratarResultadoQuery(origem.query(id + ".classifier.name"))[0];
        String ejbAssociationEnd2Id = ejbAssociationEnd2Name + System.nanoTime();
        String ejbAssociationEnd2Lower = "1";
        String ejbAssociationEnd2Upper = "1";
        Boolean ejbAssociationEnd2Composition = "true".equals(origem.query(id + ".composition").replace("'", "")) ? true : false;
        destino.insertEJBAssociationEndStub(ejbAssociationEnd2Id, ejbAssociationEnd2Name, ejbAssociationEnd2Lower, ejbAssociationEnd2Upper, ejbAssociationEnd2Composition);

        juncao.insertUMLAssociationEndEmEJBAssociationpelaRegra11(id, ejbAssociationEnd2Id);
    }
    // Linkagem
    private void linkRegra11UMLAssociationEndEmEJBAssociation() throws Exception {
        String[] ids = tratarResultadoQuery(origem.query("UMLAssociationEndEmEJBAssociationpelaRegra11.allInstances()"));
        for (String id : ids) {
            if (id != null && !"".equals(id)) {
                linkEJBAssociationEndDeAssociationpelaRegra11(id);
            }
        }
    }
    private void linkEJBAssociationEndDeAssociationpelaRegra11(String idJuncao) throws Exception {
        String idUmlAssociationEnd = tratarResultadoQuery(juncao.query(idJuncao + ".associationEnd"))[0];
        String idEjbAssociationEnd = tratarResultadoQuery(juncao.query(idJuncao + ".ejbAssociationEnd2"))[0];

        // umlAssociationEnd.type <~> ejbAssociationEnd2.type.oclAsType(EJB::EJBKeyClass)
        String umlAssociationTypeId = tratarResultadoQuery(origem.query(idUmlAssociationEnd + ".classifier"))[0];
        destino.insertEJBAssociationEndTypeLink(idEjbAssociationEnd, findEjbKeyClassBasedonUmlId(umlAssociationTypeId));

        // umlAssociationEnd.association <~> ejbAssociationEnd2.class.oclAsType(EJB::EJBDataClass)
        String umlAssociationClassId = tratarResultadoQuery(origem.query(idUmlAssociationEnd + ".association"))[0];
        destino.insertEJBAssociationEndClassLink(idEjbAssociationEnd, findEjbDataClassBasedonUmlId(umlAssociationClassId));
    }
    //--------------------------------------------------------------------------
    // REGRA 12
    // Criacao
    private void transformaRegraUMLOperationEmBusinessMethod() throws Exception {
        String[] ids = tratarResultadoQuery(origem.query("Operation.allInstances()"));

        for (String id : ids) {
            String nome = origem.query(id + ".name").replace("'", "");
            if (!"NULL_OPERATION".equals(id)) {
                criaBusinessMethodDeOperation(id, nome);
            }
        }
    }
    private void criaBusinessMethodDeOperation(String id, String nome) throws Exception {
        String businessMethodName = nome;
        String businessMethodId = nome + System.nanoTime();

        destino.insertBusinessMethodStub(businessMethodId, businessMethodName);

        juncao.insertUMLOperationToBusinessMethod(id, businessMethodId);
    }
    // Linkagem
    private void linkRegraUMLOperationEmBusinessMethod() throws Exception {
        String[] ids = tratarResultadoQuery(origem.query("UMLOperationToBusinessMethod.allInstances()"));

        for (String id : ids) {
            if (id != null && !"".equals(id)) {
                linkBusinessMethodDeOperation(id);
            }
        }
    }
    private void linkBusinessMethodDeOperation(String idJuncao) throws Exception {
        String idUmlOperation = tratarResultadoQuery(juncao.query(idJuncao + ".operation"))[0];
        String[] umlOperationParameterIds = tratarResultadoQuery(origem.query(idUmlOperation + ".parameter"));
        String umlOperationTypeId = tratarResultadoQuery(origem.query(idUmlOperation + ".classifier"))[0];

        String idBusinessMethod = tratarResultadoQuery(juncao.query(idJuncao + ".businessMethod").replace("'", ""))[0];

        // umlOperation.type <~> businessMethod.type        !!! Nao esta no livro !!!
        String idBusinessMethodTypeId = findEjbIdBasedonUmlId(umlOperationTypeId);
        destino.insertBusinessMethodTypeLink(idBusinessMethod, idBusinessMethodTypeId);

        // umlOperation.parameter <~> businessMethod.parameter
        if (umlOperationParameterIds != null && umlOperationParameterIds.length > 0 && !"".equals(umlOperationParameterIds[0])) {
            for (String idParameter : umlOperationParameterIds) {
                String idEjbParameter = tratarResultadoQuery(origem.query(idParameter + ".transformerToEjbParameter.ejbParameter"))[0];
                destino.insertEJBParameterBusinessMethodLink(idEjbParameter, idBusinessMethod);
            }
        }
    }
    //--------------------------------------------------------------------------
    // REGRA 13
    // Criacao
    private void transformaRegraUMLParameterEmEJBParameter() throws Exception {
        String[] ids = tratarResultadoQuery(origem.query("Parameter.allInstances()"));

        for (String id : ids) {
            String nome = origem.query(id + ".name").replace("'", "");
            criaEJBParameterDeParameter(id, nome);
        }
    }
    private void criaEJBParameterDeParameter(String id, String nome) throws Exception {
        String ejbParameterName = nome;
        String ejbParameterId = ejbParameterName + System.nanoTime();

        destino.insertEJBParameterStub(ejbParameterId, ejbParameterName);

        juncao.insertUMLParameterToEJBParameter(id, ejbParameterId);
    }
    // Linkagem
    private void linkRegraUMLParameterEmEJBParameter() throws Exception {
        String[] ids = tratarResultadoQuery(juncao.query("UMLParameterToEJBParameter.allInstances()"));
        for (String id : ids) {
            if (id != null && !"".equals(id)) {
                linkEJBParameterDeParameter(id);
            }
        }
    }
    private void linkEJBParameterDeParameter(String idJuncao) throws Exception {
        String idUmlParameter = juncao.query(idJuncao + ".parameter").replace("'", "");
        String umlParameterTypeId = origem.query(idUmlParameter + ".classifier");

        String idEjbParameter = juncao.query(idJuncao + ".ejbParameter").replace("'", "");

        // umlParameter.type <~> ejbParameter.type
        String idEjbParameterTypeId = findEjbIdBasedonUmlId(umlParameterTypeId);
        destino.insertEJBParameterTypeLink(idEjbParameter, idEjbParameterTypeId);
    }
}
