package br.uff.ic.mda.transformer;

/**
 *
 * @author robertowm
 */
public class UmlEjbTransformer extends Transformer<UmlDomain, EjbDomain, UmlEjbDomain> {

    public UmlEjbTransformer(UmlDomain dUml, EjbDomain dEjb, UmlEjbDomain dUmlEjb) {
        super(dUml, dEjb, dUmlEjb);
    }

    private String findEjbDataTypeBasedonUmlId(String umlId) throws Exception {
        String[] result = null;

        // Procura por um DataType -> EjbDataType
        // Regra Minha
        result = processQueryResult(joinedDomain.query("UMLDataTypeToEJBDataType.allInstances()->select(a | a.dataType->exists(dt : DataType | dt = " + umlId + "))"));
        if (result != null && result.length > 0 && !"".equals(result[0])) {
            return processQueryResult(joinedDomain.query(result[0] + ".ejbDataType"))[0];
        }
        // Procura por um UMLSet -> EJBSet
        result = processQueryResult(joinedDomain.query("UMLSetToEJBSet.allInstances()->select(a | a.umlSet->exists(s : UMLSet | s = " + umlId + "))"));
        if (result != null && result.length > 0 && !"".equals(result[0])) {
            return processQueryResult(joinedDomain.query(result[0] + ".ejbSet"))[0];
        }
        return null;
    }

    private String findEjbDataClassBasedonUmlId(String umlId) throws Exception {
        String[] result = null;

        // Procura por um Class -> EJBDataClass
        // Regra 3
        result = processQueryResult(joinedDomain.query("UMLClassToEJBEntityComponent.allInstances()->select(a | a.class->includes(" + umlId + "))"));
        if (result != null && result.length > 0 && !"".equals(result[0])) {
            return processQueryResult(joinedDomain.query(result[0] + ".dataClass"))[0];
        }
        // Regra 4
        result = processQueryResult(joinedDomain.query("UMLClassToEJBDataClass.allInstances()->select(a | a.class->includes(" + umlId + "))"));
        if (result != null && result.length > 0 && !"".equals(result[0])) {
            return processQueryResult(joinedDomain.query(result[0] + ".ejbDataClass"))[0];
        }
        // Regra 6
        result = processQueryResult(joinedDomain.query("UMLAssociationClassToEJBDataClass.allInstances()->select(a | a.associationClass->includes(" + umlId + "))"));
        if (result != null && result.length > 0 && !"".equals(result[0])) {
            return processQueryResult(joinedDomain.query(result[0] + ".ejbDataClass"))[0];
        }

        return null;
    }

    private String findEjbKeyClassBasedonUmlId(String umlId) throws Exception {
        String[] result = null;
        // Procura por um Class -> EJBKeyClass
        // Regra 1
        result = processQueryResult(joinedDomain.query("UMLClassToEJBKeyClass.allInstances()->select(a | a.class->exists(c : Class | c = " + umlId + "))"));
        if (result != null && result.length > 0 && !"".equals(result[0])) {
            return processQueryResult(joinedDomain.query(result[0] + ".keyClass"))[0];
        }
        // Regra 2
        result = processQueryResult(joinedDomain.query("UMLAssociationClassToEJBKeyClass.allInstances()->select(a | a.associationClass->exists(ac : AssociationClass | ac = " + umlId + "))"));
        if (result != null && result.length > 0 && !"".equals(result[0])) {
            return processQueryResult(joinedDomain.query(result[0] + ".keyClass"))[0];
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

    private String findEJBAssociationEndBasedonUmlAssociationEnd(String associationEndId) throws Exception {
        String[] result = null;

        // Regra 8
        result = processQueryResult(joinedDomain.query("UMLAssociationEndToEJBDataEndusingRule8.allInstances()->select(a | a.associationEnd->exists(ae : AssociationEnd | ae = " + associationEndId + "))"));
        if (result != null && result.length > 0 && !"".equals(result[0])) {
            return processQueryResult(joinedDomain.query(result[0] + ".ejbAssociationEnd"))[0];
        }
        // Regra 9
        result = processQueryResult(joinedDomain.query("UMLAssociationEndToEJBDataEndusingRule9.allInstances()->select(a | a.associationEnd->exists(ae : AssociationEnd | ae = " + associationEndId + "))"));
        if (result != null && result.length > 0 && !"".equals(result[0])) {
            return processQueryResult(joinedDomain.query(result[0] + ".ejbAssociationEnd"))[0];
        }
        // Regra 10
        result = processQueryResult(joinedDomain.query("UMLAssociationEndEmEJBAssociationusingRule10.allInstances()->select(a | a.associationEnd->exists(ae : AssociationEnd | ae = " + associationEndId + "))"));
        if (result != null && result.length > 0 && !"".equals(result[0])) {
            return processQueryResult(joinedDomain.query(result[0] + ".ejbAssociationEnd2"))[0];
//            return tratarResultadoQuery(juncao.query(resultado[0] + ".ejbAssociationEnd1"))[0];
        }
        // Regra 11
        result = processQueryResult(joinedDomain.query("UMLAssociationEndEmEJBAssociationusingRule11.allInstances()->select(a | a.associationEnd->exists(ae : AssociationEnd | ae = " + associationEndId + "))"));
        if (result != null && result.length > 0 && !"".equals(result[0])) {
            return processQueryResult(joinedDomain.query(result[0] + ".ejbAssociationEnd2"))[0];
        }

        // Nao encontrou -> Dispara excecao
        throw new Exception("Can't find correspondent EJBAssociationEnd to AsociationEnd[id: " + associationEndId + "]");
    }

    @Override
    public void transform() throws Exception {
        // DataType -> EJBDataType
        transformUMLDataTypetoEJBDataType();

        // Cria elementos e linka (Caso unico: Class e AssociationClass -> EJBKeyClass)
        transformUMLClasstoEJBKeyClass();                 // Regra 1
        transformUMLAssociationClasstoEJBKeyClass();      // Regra 2

        // Cria objetos
        transformUMLClasstoEJBEntityComponent();          // Regra 3
        transformUMLClasstoEJBDataClass();                // Regra 4
        transformUMLAssociationtoEJBDataAssociation();    // Regra 5
        transformUMLAssociationClasstoEJBDataClass();     // Regra 6
        transformaUMLAttributetoEJBAttribute();            // Regra 7
        transformRule8UMLAssociationEndtoEJBAssociationEnd(); // Regra 8
        transformRule9UMLAssociationEndtoEJBAssociationEnd(); // Regra 9
        transformRule10UMLAssociationEndtoEJBAssociation();   // Regra 10
        transformRule11UMLAssociationEndtoEJBAssociation();   // Regra 11
        transformRuleUMLOperationtoBusinessMethod();          // Regra 12
        transformRuleUMLParametertoEJBParameter();            // Regra 13
        transformUMLSettoEJBSet();                            // Minha regra

        // Cria links
        linkUMLClasstoEJBEntityComponent();                // Regra 3
        linkUMLAssociationtoEJBDataAssociation();          // Regra 5
        linkUMLClasstoEJBDataClass();                      // Regra 4
        linkUMLAssociationClasstoEJBDataClass();           // Regra 6
        linkUMLAttributetoEJBAttribute();                  // Regra 7
        linkRule8UMLAssociationEndtoEJBAssociationEnd();       // Regra 8
        linkRule9UMLAssociationEndtoEJBAssociationEnd();       // Regra 9
        linkRule10UMLAssociationEndtoEJBAssociation();         // Regra 10
        linkRule11UMLAssociationEndtoEJBAssociation();         // Regra 11
        linkRuleUMLOperationtoBusinessMethod();                // Regra 12
        linkUMLParametertoEJBParameter();                  // Regra 13
        linkUMLSettoEJBSet();

    }

    //--------------------------------------------------------------------------
    // REGRA SOBRE TIPOS DE DADOS - Completo (Cria e linka)
    private void transformUMLDataTypetoEJBDataType() throws Exception {
        // "Integer" -> "EJBInteger"
        targetDomain.insertEJBDataType("EJBInteger", "EJBInteger");
        joinedDomain.insertUMLDataTypeToEJBDataType("UMLInteger", "EJBInteger");

        // "Double" -> "EJBDouble"
        targetDomain.insertEJBDataType("EJBDouble", "EJBDouble");
        joinedDomain.insertUMLDataTypeToEJBDataType("UMLDouble", "EJBDouble");

        // "Real" -> "EJBDouble"
        joinedDomain.insertUMLDataTypeToEJBDataType("UMLReal", "EJBDouble");

        // "String" -> "EJBString"
        targetDomain.insertEJBDataType("EJBString", "EJBString");
        joinedDomain.insertUMLDataTypeToEJBDataType("UMLString", "EJBString");

        // "Date" -> "EJBDate"
        targetDomain.insertEJBDataType("EJBDate", "EJBDate");
        joinedDomain.insertUMLDataTypeToEJBDataType("UMLDate", "EJBDate");

        // "Boolean" -> "EJBBoolean"
        targetDomain.insertEJBDataType("EJBBoolean", "EJBBoolean");
        joinedDomain.insertUMLDataTypeToEJBDataType("UMLBoolean", "EJBBoolean");
    }
    //--------------------------------------------------------------------------
    // REGRA 01 - Completo (Cria e linka)
    private void transformUMLClasstoEJBKeyClass() throws Exception {
        String[] ids = processQueryResult(sourceDomain.query("Class.allInstances()->select(c : Class | c.oclIsTypeOf(Class))"));

        for (String id : ids) {
            String name = sourceDomain.query(id + ".name").replace("'", "");
            if (!"NULL_CLASS".equals(id)) {
                createEJBKeyClassfromUMLClass(id, name);
            }
        }
    }
    private void createEJBKeyClassfromUMLClass(String umlClassId, String umlClassName) throws Exception {
        String ejbKeyClassName = umlClassName + "Key";
        String ejbKeyClassId = ejbKeyClassName + System.nanoTime();
        String ejbAttributeName = umlClassName + "ID";
        String ejbAttributeId = ejbAttributeName + System.nanoTime();
        targetDomain.insertEJBKeyClass(ejbKeyClassId, ejbKeyClassName);
        targetDomain.insertEJBAttribute(ejbAttributeId, ejbAttributeName, "public", "EJBInteger", ejbKeyClassId);

        joinedDomain.insertUMLClassToEJBKeyClass(umlClassId, ejbKeyClassId, ejbAttributeId);
    }
    //--------------------------------------------------------------------------
    // REGRA 02 - Completo (Cria e linka)
    private void transformUMLAssociationClasstoEJBKeyClass() throws Exception {
        String[] ids = processQueryResult(sourceDomain.query("AssociationClass.allInstances()"));

        for (String id : ids) {
            String name = sourceDomain.query(id + ".name").replace("'", "");
            createEJBKeyClassfromUMLAssociationClass(id, name);
        }
    }
    private void createEJBKeyClassfromUMLAssociationClass(String umlAssociationClassId, String umlAssociationClassName) throws Exception {
        String ejbKeyClassName = umlAssociationClassName + "Key";
        String ejbKeyClassId = ejbKeyClassName + System.nanoTime();
        targetDomain.insertEJBKeyClass(ejbKeyClassId, ejbKeyClassName);

        String id = sourceDomain.query(umlAssociationClassId + ".associationEnds->asOrderedSet()->first()").replace("'", "");

        String firstName = processQueryResult(sourceDomain.query(id + ".classifier.name"))[0] + "ID";
        String firstId = firstName + System.nanoTime();
        targetDomain.insertEJBAttribute(firstId, firstName, "public", "EJBInteger", ejbKeyClassId);

        String otherId = null;
        String[] result = processQueryResult(sourceDomain.query(id + ".otherEnd.classifier.name"));
        if (result != null && result.length > 0 && !"".equals(result[0])) {
            String otherName = result[0] + "ID";
            otherId = otherName + System.nanoTime();
            targetDomain.insertEJBAttribute(otherId, otherName, "public", "EJBInteger", ejbKeyClassId);
        } else {
            String otherName = processQueryResult(sourceDomain.query(id + ".class.name"))[0] + "ID";
            otherId = otherName + System.nanoTime();
            targetDomain.insertEJBAttribute(otherId, otherName, "public", "EJBInteger", ejbKeyClassId);
        }
        joinedDomain.insertUMLAssociationClassToEJBKeyClass(umlAssociationClassId, ejbKeyClassId, firstId, otherId);
    }
    //--------------------------------------------------------------------------
    // REGRA 3
    // Criacao
    private void transformUMLClasstoEJBEntityComponent() throws Exception {
        String[] ids = processQueryResult(sourceDomain.query("Class.allInstances()->select(c : Class | c.oclIsTypeOf(Class))->select(c : Class | not c.feature->select(f : Feature | f.oclIsKindOf(AssociationEnd))->collect(ft : Feature | ft.oclAsType(AssociationEnd))->exists(ae : AssociationEnd | ae.composition = true))"));

        for (String id : ids) {
            String name = sourceDomain.query(id + ".name").replace("'", "");
            if (!"NULL_CLASS".equals(id)) {
                createEjbEntityComponentfromUmlClass(id, name);
            }
        }
    }
    private void createEjbEntityComponentfromUmlClass(String classId, String className) throws Exception {
        // Cria EJBEntityComponent
        String ejbEntityComponentName = className;
        String ejbEntityComponentId = ejbEntityComponentName + System.nanoTime();
        targetDomain.insertEJBEntityComponentStub(ejbEntityComponentId, ejbEntityComponentName);

        // Cria EJBDataSchema
        String ejbDataSchemaName = className;
        String ejbDataSchemaId = ejbDataSchemaName + System.nanoTime();
        targetDomain.insertEJBDataSchemaStub(ejbDataSchemaId, ejbDataSchemaName);

        // Cria EJBDataClass
        String ejbDataClassName = className;
        String ejbDataClassId = ejbDataClassName + System.nanoTime();
        targetDomain.insertEJBDataClassStub(ejbDataClassId, ejbDataClassName);

        // Cria EJBServingAttribute
        String ejbServingAttributeName = className;
        String ejbServingAttributeId = ejbServingAttributeName + System.nanoTime();
        String ejbServingAttributeLower = "1";
        String ejbServingAttributeUpper = "1";
        Boolean ejbServingAttributeComposition = false;
        targetDomain.insertEJBServingAttributeStub(ejbServingAttributeId, ejbServingAttributeName, ejbServingAttributeLower, ejbServingAttributeUpper, ejbServingAttributeComposition);

        // Cria Juncao
        joinedDomain.insertUMLClassToEJBEntityComponent(classId, ejbEntityComponentId, ejbDataClassId, ejbDataSchemaId, ejbServingAttributeId);
    }
    // Linkagem
    private void linkUMLClasstoEJBEntityComponent() throws Exception {
        String[] ids = processQueryResult(sourceDomain.query("UMLClassToEJBEntityComponent.allInstances()"));

        for (String id : ids) {
            if (id != null && !"".equals(id)) {
                linkEjbEntityComponentfromUmlClass(id);
            }
        }
    }
    private void linkEjbEntityComponentfromUmlClass(String id) throws Exception {
        String umlClassId = processQueryResult(joinedDomain.query(id + ".class"))[0];
        String ejbEntityComponentId = processQueryResult(joinedDomain.query(id + ".entityComponent"))[0];
        String ejbDataClassId = processQueryResult(joinedDomain.query(id + ".dataClass"))[0];
        String ejbDataSchemaId = processQueryResult(joinedDomain.query(id + ".dataSchema"))[0];
        String ejbServingAttributeId = processQueryResult(joinedDomain.query(id + ".servingAttribute"))[0];

        // servingAttribute.class = entityComponent
        targetDomain.insertEJBServingAttributeClassLink(ejbServingAttributeId, ejbEntityComponentId);
        // servingAttribute.type = rootDataClass
        targetDomain.insertEJBServingAttributeTypeLink(ejbServingAttributeId, ejbDataClassId);
        // rootDataClass.package = dataSchema
        targetDomain.insertEJBDataClassSchemaLink(ejbDataClassId, ejbDataSchemaId);

        // class.getAllContained(Set(UML::Class){}).operations() <~> entityComponent.feature
        // Apesar da query estar correta e otimizada, o xeos nao retorna os resultados corretos quando recupera as features e no conjunto de classes possui uma AssociationClass
//        String[] idsUmlOperations = tratarResultadoQuery(origem.query(idUmlClass + ".getAllContained().feature->select(f : Feature | f.oclIsKindOf(Operation))"));
        String[] umlOperationsIds = processQueryResult(sourceDomain.query("Operation.allInstances()->select(op : Operation | op.class->notEmpty())->select(op : Operation | " + umlClassId + ".getAllContained()->includes(op.class->asOrderedSet()->first().oclAsType(Class)))"));
        if (umlOperationsIds != null && umlOperationsIds.length > 0 && !"".equals(umlOperationsIds[0])) {
            for (String operationId : umlOperationsIds) {
                String businessMethodId = processQueryResult(joinedDomain.query(operationId + ".transformerToBusinessMethod.businessMethod"))[0];
                targetDomain.insertBusinessMethodClassLink(businessMethodId, ejbEntityComponentId);
            }
        }
        // class.feature->select(oclKindOf(Attribute) or oclKindOf(AssociationEnd)) <~> rootDataClass.feature
        // Separando...
        //   1) class.feature->select(oclKindOf(Attribute)) <~> rootDataClass.feature
        String[] umlAttributesIds = processQueryResult(sourceDomain.query(umlClassId + ".feature->select(f | f.oclIsKindOf(Attribute))"));
        if (umlAttributesIds != null && umlAttributesIds.length > 0 && !"".equals(umlAttributesIds[0])) {
            for (String attributeId : umlAttributesIds) {
                String ejbAttributeId = processQueryResult(joinedDomain.query(attributeId + ".transformerToEjbAttribute.ejbAttribute"))[0];
                targetDomain.insertEJBAttributeClassLink(ejbAttributeId, ejbDataClassId);
            }
        }
        //   2) class.feature->select(oclKindOf(AssociationEnd)) <~> rootDataClass.feature
        String[] umlAssociationEndsIds = processQueryResult(sourceDomain.query(umlClassId + ".feature->select(f | f.oclIsKindOf(AssociationEnd))"));
        if (umlAssociationEndsIds != null && umlAssociationEndsIds.length > 0 && !"".equals(umlAssociationEndsIds[0])) {
            for (String associationEndId : umlAssociationEndsIds) {
                String ejbAssociationEndId = findEJBAssociationEndBasedonUmlAssociationEnd(associationEndId);
                targetDomain.insertEJBAssociationEndClassLink(ejbAssociationEndId, ejbDataClassId);
            }
        }
        // class.getAllContained(Set(UML::Class){}) <~> entityComponent.usedTable
        // --- Nao fiz pois nao tem transformacao para Table
    }
    //--------------------------------------------------------------------------
    // REGRA 4
    // Criacao
    private void transformUMLClasstoEJBDataClass() throws Exception {
        String[] ids = processQueryResult(sourceDomain.query("Class.allInstances()->select(c : Class | c.oclIsTypeOf(Class))->select(c : Class | c.feature->select(f : Feature | f.oclIsKindOf(AssociationEnd))->collect(ft : Feature | ft.oclAsType(AssociationEnd))->exists(ae : AssociationEnd | ae.composition = true))"));
        for (String id : ids) {
            String name = sourceDomain.query(id + ".name").replace("'", "");
            if (!"NULL_CLASS".equals(id)) {
                createEJBDataClassfromClass(id, name);
            }
        }
    }
    private void createEJBDataClassfromClass(String classId, String className) throws Exception {
        String ejbDataClassName = className;
        String ejbDataClassId = ejbDataClassName + System.nanoTime();
        targetDomain.insertEJBDataClassStub(ejbDataClassId, ejbDataClassName);

        joinedDomain.insertUMLClassToEJBDataClass(classId, ejbDataClassId);
    }
    // Linkagem
    private void linkUMLClasstoEJBDataClass() throws Exception {
        String[] ids = processQueryResult(sourceDomain.query("UMLClassToEJBDataClass.allInstances()"));
        for (String id : ids) {
            if (id != null && !"".equals(id)) {
                linkEJBDataClassfromClass(id);
            }
        }
    }
    private void linkEJBDataClassfromClass(String id) throws Exception {
        String umlClassId = processQueryResult(joinedDomain.query(id + ".class"))[0];
        String ejbDataClassId = processQueryResult(joinedDomain.query(id + ".ejbDataClass"))[0];

        // class.feature->select(oclKindOf(Attribute) or oclKindOf(AssociationEnd)) <~> nonRootDataClass.feature
        // Separando...
        //   1) class.feature->select(oclKindOf(Attribute)) <~> nonRootDataClass.feature
        String[] umlAttributesIds = processQueryResult(sourceDomain.query(umlClassId + ".feature->select(f : Feature | f.oclIsKindOf(Attribute))"));
        if (umlAttributesIds != null && umlAttributesIds.length > 0 && !"".equals(umlAttributesIds[0])) {
            for (String attributeId : umlAttributesIds) {
                String ejbAttributeId = processQueryResult(joinedDomain.query(attributeId + ".transformerToEjbAttribute.ejbAttribute"))[0];
                targetDomain.insertEJBAttributeClassLink(ejbAttributeId, ejbDataClassId);
            }
        }
        //   2) class.feature->select(oclKindOf(AssociationEnd)) <~> nonRootDataClass.feature
        String[] umlAssociationEndsIds = processQueryResult(sourceDomain.query(umlClassId + ".feature->select(f : Feature | f.oclIsKindOf(AssociationEnd))"));
        if (umlAssociationEndsIds != null && umlAssociationEndsIds.length > 0 && !"".equals(umlAssociationEndsIds[0])) {
            for (String associationEndId : umlAssociationEndsIds) {
                String ejbAssociationEndId = findEJBAssociationEndBasedonUmlAssociationEnd(associationEndId);
                targetDomain.insertEJBAssociationEndClassLink(ejbAssociationEndId, ejbDataClassId);
            }
        }
        // class.getOuterMostContainer() <~> nonRootClass.package
        String ejbDataSchemaId = processQueryResult(joinedDomain.query("UMLClassToEJBEntityComponent.allInstances()->select(a | a.class->includes(" + umlClassId + ".getOuterMostContainer()))->collect(a | a.dataSchema)"))[0];
        targetDomain.insertEJBDataClassSchemaLink(ejbDataClassId, ejbDataSchemaId);
    }
    //--------------------------------------------------------------------------
    // REGRA 5
    // Criacao
    private void transformUMLAssociationtoEJBDataAssociation() throws Exception {
        String[] ids = processQueryResult(sourceDomain.query("Association.allInstances()->select(a : Association | a.oclIsTypeOf(Association))->select(a : Association | a.associationEnds->exists(ae : AssociationEnd | ae.composition = true))"));

        for (String id : ids) {
            String name = sourceDomain.query(id + ".name").replace("'", "");
            if (!"NULL_ASSOCIATION".equals(id)) {
                createEJBDataAssociationfromAssociation(id, name);
            }
        }
    }
    private void createEJBDataAssociationfromAssociation(String id, String name) throws Exception {
        String ejbDataAssociationName = name;
        String ejbDataAssociationId = ejbDataAssociationName + System.nanoTime();
        targetDomain.insertEJBDataAssociationStub(ejbDataAssociationId, ejbDataAssociationName);

        joinedDomain.insertUMLAssociationToEJBDataAssociation(id, ejbDataAssociationId);
    }
    // Linkagem
    private void linkUMLAssociationtoEJBDataAssociation() throws Exception {
        String[] ids = processQueryResult(sourceDomain.query("UMLAssociationToEJBDataAssociation.allInstances()"));

        for (String id : ids) {
            if (id != null && !"".equals(id)) {
                linkEJBDataAssociationfromAssociation(id);
            }
        }
    }
    private void linkEJBDataAssociationfromAssociation(String id) throws Exception {
        String umlAssociationId = processQueryResult(joinedDomain.query(id + ".association"))[0];
        String[] umlAssociationEndsIds = processQueryResult(sourceDomain.query(umlAssociationId + ".associationEnds"));

        String ejbDataAssociationId = processQueryResult(joinedDomain.query(id + ".ejbDataAssociation"))[0];

        // assoc.end <~> dataAssoc.end
        if (umlAssociationEndsIds != null && umlAssociationEndsIds.length > 0 && !"".equals(umlAssociationEndsIds[0])) {
            for (String associationEndId : umlAssociationEndsIds) {
                String ejbAssociationEndId = findEJBAssociationEndBasedonUmlAssociationEnd(associationEndId);
                targetDomain.insertEJBDataAssociationEndLinks(ejbDataAssociationId, ejbAssociationEndId);
            }
        }
        // assoc.getOuterMostContainer() <~> dataAssoc.package
        String ejbDataSchemaId = processQueryResult(joinedDomain.query("UMLClassToEJBEntityComponent.allInstances()->select(a | a.class->includes(" + umlAssociationId + ".getOuterMostContainerFromAssociation())).dataSchema"))[0];
        targetDomain.insertEJBDataAssociationSchemaLink(ejbDataAssociationId, ejbDataSchemaId);
    }
    //--------------------------------------------------------------------------
    // REGRA 6
    // Criacao
    private void transformUMLAssociationClasstoEJBDataClass() throws Exception {
        String[] ids = processQueryResult(sourceDomain.query("AssociationClass.allInstances()"));

        for (String id : ids) {
            String name = sourceDomain.query(id + ".name").replace("'", "");
            createEJBDataClassfromAssociationClass(id, name);
        }
    }
    private void createEJBDataClassfromAssociationClass(String id, String name) throws Exception {
        String ejbDataClassName = name;
        String ejbDataClassId = ejbDataClassName + System.nanoTime();
        targetDomain.insertEJBDataClassStub(ejbDataClassId, ejbDataClassName);

        joinedDomain.insertUMLAssociationClassToEJBDataClass(id, ejbDataClassId);
    }
    // Linkagem
    private void linkUMLAssociationClasstoEJBDataClass() throws Exception {
        String[] ids = processQueryResult(sourceDomain.query("UMLAssociationClassToEJBDataClass.allInstances()"));
        for (String id : ids) {
            if (id != null && !"".equals(id)) {
                linkEJBDataClassfromAssociationClass(id);
            }
        }
    }
    private void linkEJBDataClassfromAssociationClass(String id) throws Exception {
        String umlAssociationClassId = processQueryResult(joinedDomain.query(id + ".associationClass"))[0];
        String ejbDataClassId = processQueryResult(joinedDomain.query(id + ".ejbDataClass"))[0];

        // associationClass.feature->select(oclKindOf(Attribute) or oclKindOf(AssociationEnd)) <~> nonRootDataClass.feature
        // Separando...
        //   1) associationClass.feature->select(oclKindOf(Attribute)) <~> nonRootDataClass.feature
        String[] umlAttributesIds = processQueryResult(sourceDomain.query(umlAssociationClassId + ".feature->select(f | f.oclAsType(Feature).oclIsKindOf(Attribute))"));
        if (umlAttributesIds != null && umlAttributesIds.length > 0 && !"".equals(umlAttributesIds[0])) {
            for (String attributeId : umlAttributesIds) {
                String ejbAttributeId = processQueryResult(joinedDomain.query(attributeId + ".transformerToEjbAttribute.ejbAttribute"))[0];
                targetDomain.insertEJBAttributeClassLink(ejbAttributeId, ejbDataClassId);
            }
        }
        //   2) associationClass.feature->select(oclKindOf(AssociationEnd)) <~> nonRootDataClass.feature
        String[] umlAssociationEndsIds = processQueryResult(sourceDomain.query(umlAssociationClassId + ".feature->select(f | f.oclAsType(Feature).oclIsKindOf(AssociationEnd))"));
        if (umlAssociationEndsIds != null && umlAssociationEndsIds.length > 0 && !"".equals(umlAssociationEndsIds[0])) {
            for (String associationEndId : umlAssociationEndsIds) {
                String ejbAssociationEndId = findEJBAssociationEndBasedonUmlAssociationEnd(associationEndId);
                targetDomain.insertEJBAssociationEndClassLink(ejbAssociationEndId, ejbDataClassId);
            }
        }
        // associationClass.getOuterMostContainer() <~> nonRootClass.package
        String ejbDataSchemaId = processQueryResult(joinedDomain.query("UMLClassToEJBEntityComponent.allInstances()->select(a | a.class->includes(" + umlAssociationClassId + ".getOuterMostContainer())).dataSchema"))[0];
        targetDomain.insertEJBDataClassSchemaLink(ejbDataClassId, ejbDataSchemaId);
    }
    //--------------------------------------------------------------------------
    // REGRA 7
    // Criacao
    private void transformaUMLAttributetoEJBAttribute() throws Exception {
        String[] ids = processQueryResult(sourceDomain.query("Attribute.allInstances()"));

        for (String id : ids) {
            String name = sourceDomain.query(id + ".name").replace("'", "");
            createEJBAttributefromAttribute(id, name);
        }
    }
    private void createEJBAttributefromAttribute(String id, String name) throws Exception {
        String ejbAttributeName = name;
        String ejbAttributeId = ejbAttributeName + System.nanoTime();
        String ejbAttributeVisibility = sourceDomain.query(id + ".visibility").replace("'", "");
        targetDomain.insertEJBAttributeStub(ejbAttributeId, ejbAttributeName, ejbAttributeVisibility);

        joinedDomain.insertUMLAttributeToEJBAttribute(id, ejbAttributeId);
    }
    // Linkagem
    private void linkUMLAttributetoEJBAttribute() throws Exception {
        String[] ids = processQueryResult(sourceDomain.query("UMLAttributeToEJBAttribute.allInstances()"));

        for (String id : ids) {
            if (id != null && !"".equals(id)) {
                linkEJBAttributefromAttribute(id);
            }
        }
    }
    private void linkEJBAttributefromAttribute(String id) throws Exception {
        String umlAttributeId = processQueryResult(joinedDomain.query(id + ".attribute"))[0];
        String umlAttributeTypeId = processQueryResult(sourceDomain.query(umlAttributeId + ".classifier"))[0];

        String ejbAttributeId = processQueryResult(joinedDomain.query(id + ".ejbAttribute"))[0];

        // umlAttribute.type <~> ejbAttribute.type
        String ejbAttributeTypeId = findEjbIdBasedonUmlId(umlAttributeTypeId);
        targetDomain.insertEJBAttributeTypeLink(ejbAttributeId, ejbAttributeTypeId);
    }
    //--------------------------------------------------------------------------
    // REGRA 8
    // Criacao
    private void transformRule8UMLAssociationEndtoEJBAssociationEnd() throws Exception {
        String[] ids = processQueryResult(sourceDomain.query("AssociationEnd.allInstances()->select(ae : AssociationEnd | ae.association.oclIsTypeOf(Association))->select(ae : AssociationEnd | ae.class.getOuterMostContainer() = ae.classifier.oclAsType(Class).getOuterMostContainer())"));

        for (String id : ids) {
            String name = sourceDomain.query(id + ".name").replace("'", "");
            createEJBAssociationEndfromAssociationEndusingRule8(id, name);
        }
    }
    private void createEJBAssociationEndfromAssociationEndusingRule8(String id, String name) throws Exception {
        String ejbAssociationEndName = name;
        String ejbAssociationEndId = ejbAssociationEndName + System.nanoTime();
        String ejbAssociationEndUpper = sourceDomain.query(id + ".upper").replace("'", "");
        String ejbAssociationEndLower = sourceDomain.query(id + ".lower").replace("'", "");
        Boolean ejbAssociationEndComposition = "true".equals(sourceDomain.query(id + ".composition").replace("'", "")) ? true : false;
        targetDomain.insertEJBAssociationEndStub(ejbAssociationEndId, ejbAssociationEndName, ejbAssociationEndLower, ejbAssociationEndUpper, ejbAssociationEndComposition);

        joinedDomain.insertUMLAssociationEndToEJBDataEndusingRule8(id, ejbAssociationEndId);
    }
    // Linkagem
    private void linkRule8UMLAssociationEndtoEJBAssociationEnd() throws Exception {
        String[] ids = processQueryResult(sourceDomain.query("UMLAssociationEndToEJBDataEndusingRule8.allInstances()"));

        for (String id : ids) {
            if (id != null && !"".equals(id)) {
                linkEJBAssociationEndfromAssociationEndusingRule8(id);
            }
        }
    }
    private void linkEJBAssociationEndfromAssociationEndusingRule8(String id) throws Exception {
        String umlAssociationEndId = processQueryResult(joinedDomain.query(id + ".associationEnd"))[0];
        String ejbAssociationEndId = processQueryResult(joinedDomain.query(id + ".ejbAssociationEnd"))[0];

        // umlAssociationEnd.type <~> ejbAssociationEnd.type.oclAsType(EJB::EJBDataClass)
        String umlAssociationEndTypeId = processQueryResult(sourceDomain.query(umlAssociationEndId + ".classifier"))[0];
        String ejbAssociationEndTypeId = findEjbDataClassBasedonUmlId(umlAssociationEndTypeId);
        targetDomain.insertEJBAssociationEndTypeLink(ejbAssociationEndId, ejbAssociationEndTypeId);
    }
    //--------------------------------------------------------------------------
    // REGRA 9
    // Criacao
    private void transformRule9UMLAssociationEndtoEJBAssociationEnd() throws Exception {
        String[] ids = processQueryResult(sourceDomain.query("AssociationEnd.allInstances()->select(ae : AssociationEnd | ae.association.oclIsTypeOf(Association))->select(ae : AssociationEnd | not (ae.class.getOuterMostContainer() = ae.classifier.oclAsType(Class).getOuterMostContainer()))"));

        for (String id : ids) {
            String name = sourceDomain.query(id + ".name").replace("'", "");
            createEJBAssociationEndfromAssociationEndusingRule9(id, name);
        }
    }
    private void createEJBAssociationEndfromAssociationEndusingRule9(String id, String name) throws Exception {
        String ejbAssociationEndName = name;
        String ejbAssociationEndId = ejbAssociationEndName + System.nanoTime();
        String ejbAssociationEndUpper = sourceDomain.query(id + ".upper").replace("'", "");
        String ejbAssociationEndLower = sourceDomain.query(id + ".lower").replace("'", "");
        Boolean ejbAssociationEndComposition = "true".equals(sourceDomain.query(id + ".composition").replace("'", "")) ? true : false;
        targetDomain.insertEJBAssociationEndStub(ejbAssociationEndId, ejbAssociationEndName, ejbAssociationEndLower, ejbAssociationEndUpper, ejbAssociationEndComposition);

        joinedDomain.insertUMLAssociationEndToEJBDataEndusingRule9(id, ejbAssociationEndId);
    }
    // Linkagem
    private void linkRule9UMLAssociationEndtoEJBAssociationEnd() throws Exception {
        String[] ids = processQueryResult(sourceDomain.query("UMLAssociationEndToEJBDataEndusingRule9.allInstances()"));

        for (String id : ids) {
            if (id != null && !"".equals(id)) {
                linkEJBAssociationEndfromAssociationEndusingRule9(id);
            }
        }
    }
    private void linkEJBAssociationEndfromAssociationEndusingRule9(String id) throws Exception {
        String umlAssociationEndId = processQueryResult(joinedDomain.query(id + ".associationEnd"))[0];
        String ejbAssociationEndId = processQueryResult(joinedDomain.query(id + ".ejbAssociationEnd"))[0];

        // umlAssociationEnd.type <~> ejbAssociationEnd.type.oclAsType(EJB::EJBKeyClass)
        String umlAssociationEndTypeId = processQueryResult(sourceDomain.query(umlAssociationEndId + ".classifier"))[0];
        String ejbAssociationEndTypeId = findEjbKeyClassBasedonUmlId(umlAssociationEndTypeId);
        targetDomain.insertEJBAssociationEndTypeLink(ejbAssociationEndId, ejbAssociationEndTypeId);
    }
    //--------------------------------------------------------------------------
    // REGRA 10
    // Criacao
    private void transformRule10UMLAssociationEndtoEJBAssociation() throws Exception {
        String[] ids = processQueryResult(sourceDomain.query("AssociationEnd.allInstances()->select(ae : AssociationEnd | ae.upper <> '1')->select(ae : AssociationEnd | ae.association.oclIsTypeOf(AssociationClass))->select(ae : AssociationEnd | ae.association.oclAsType(AssociationClass).getOuterMostContainerFromAssociationClass() = ae.classifier->asOrderedSet()->first().oclAsType(Class).getOuterMostContainer())"));

        for (String id : ids) {
            String name = sourceDomain.query(id + ".name").replace("'", "");
            createEJBAssociationEndfromAssociationusingRule10(id, name);
        }
    }
    private void createEJBAssociationEndfromAssociationusingRule10(String id, String name) throws Exception {
        // Cria EJBAssociation
        String ejbAssociationName = "";
        String ejbAssociationId = ejbAssociationName + System.nanoTime();
        targetDomain.insertEJBDataAssociationStub(ejbAssociationId, ejbAssociationName);

        // Cria EJBAssociationEnd 1
        String ejbAssociationEnd1Name = sourceDomain.query(id + ".association.name").replace("'", "");
        String ejbAssociationEnd1Id = ejbAssociationEnd1Name + System.nanoTime();
        String ejbAssociationEnd1Lower = "0";
        String ejbAssociationEnd1Upper = "*";
        Boolean ejbAssociationEnd1Composition = false;
        targetDomain.insertEJBAssociationEndStub(ejbAssociationEnd1Id, ejbAssociationEnd1Name, ejbAssociationEnd1Lower, ejbAssociationEnd1Upper, ejbAssociationEnd1Composition);

        // Cria EJBAssociationEnd 2
        String ejbAssociationEnd2Name = processQueryResult(sourceDomain.query(id + ".classifier.name"))[0];
        String ejbAssociationEnd2Id = ejbAssociationEnd2Name + System.nanoTime();
        String ejbAssociationEnd2Lower = "1";
        String ejbAssociationEnd2Upper = "1";
        Boolean ejbAssociationEnd2Composition = "true".equals(sourceDomain.query(id + ".composition").replace("'", "")) ? true : false;
        targetDomain.insertEJBAssociationEndStub(ejbAssociationEnd2Id, ejbAssociationEnd2Name, ejbAssociationEnd2Lower, ejbAssociationEnd2Upper, ejbAssociationEnd2Composition);

        joinedDomain.insertUMLAssociationEndEmEJBAssociationusingRule10(id, ejbAssociationId, ejbAssociationEnd1Id, ejbAssociationEnd2Id);
    }
    // Linkagem
    private void linkRule10UMLAssociationEndtoEJBAssociation() throws Exception {
        String[] ids = processQueryResult(joinedDomain.query("UMLAssociationEndEmEJBAssociationusingRule10.allInstances()"));

        for (String id : ids) {
            if (id != null && !"".equals(id)) {
                linkEJBAssociationEndfromAssociationusingRule10(id);
            }
        }
    }
    private void linkEJBAssociationEndfromAssociationusingRule10(String id) throws Exception {
        String umlAssociationEndId = processQueryResult(joinedDomain.query(id + ".associationEnd"))[0];
        String ejbDataAssociationId = processQueryResult(joinedDomain.query(id + ".ejbDataAssociation"))[0];
        String ejbAssociationEnd1Id = processQueryResult(joinedDomain.query(id + ".ejbAssociationEnd1"))[0];
        String ejbAssociationEnd2Id = processQueryResult(joinedDomain.query(id + ".ejbAssociationEnd2"))[0];

        // ejbAssociationEnd1.association = ejbAssociation && ejbAssociationEnd2.association = ejbAssociation
        targetDomain.insertEJBDataAssociationEndLinks(ejbDataAssociationId, ejbAssociationEnd1Id, ejbAssociationEnd2Id);

        // umlAssociationEnd.type <~> ejbAssociationEnd2.type.oclAsType(EJB::EJBDataClass)
        String umlAssociationEndTypeId = processQueryResult(sourceDomain.query(umlAssociationEndId + ".classifier"))[0];
        String ejbDataClassId = findEjbDataClassBasedonUmlId(umlAssociationEndTypeId);
        targetDomain.insertEJBAssociationEndTypeLink(ejbAssociationEnd2Id, ejbDataClassId);
        // umlAssociationEnd.type <~> ejbAssociationEnd1.class.oclAsType(EJB::EJBDataClass)
        targetDomain.insertEJBAssociationEndClassLink(ejbAssociationEnd1Id, ejbDataClassId);

        // umlAssociationEnd.association <~> ejbAssociationEnd1.type.oclAsType(EJB::EJBDataClass)
        String umlAssociationClassId = processQueryResult(sourceDomain.query(umlAssociationEndId + ".association"))[0];
        ejbDataClassId = findEjbDataClassBasedonUmlId(umlAssociationClassId);
        targetDomain.insertEJBAssociationEndTypeLink(ejbAssociationEnd1Id, ejbDataClassId);
        // umlAssociationEnd.association <~> ejbAssociationEnd2.class.oclAsType(EJB::EJBDataClass)
        targetDomain.insertEJBAssociationEndClassLink(ejbAssociationEnd2Id, ejbDataClassId);
    }
    //--------------------------------------------------------------------------
    // REGRA 11
    // Criacao
    private void transformRule11UMLAssociationEndtoEJBAssociation() throws Exception {
        String[] ids = processQueryResult(sourceDomain.query("AssociationEnd.allInstances()->select(ae : AssociationEnd | ae.upper <> '1')->select(ae : AssociationEnd | ae.association.oclIsTypeOf(AssociationClass))->select(ae : AssociationEnd | ae.association.oclAsType(AssociationClass).getOuterMostContainerFromAssociationClass() <> ae.classifier->asOrderedSet()->first().oclAsType(Class).getOuterMostContainer())"));

        for (String id : ids) {
            String name = sourceDomain.query(id + ".name").replace("'", "");
            createEJBAssociationEndfromAssociationusingRule11(id, name);
        }
    }
    private void createEJBAssociationEndfromAssociationusingRule11(String id, String name) throws Exception {
        // Cria EJBAssociationEnd 2
        String ejbAssociationEnd2Name = processQueryResult(sourceDomain.query(id + ".classifier.name"))[0];
        String ejbAssociationEnd2Id = ejbAssociationEnd2Name + System.nanoTime();
        String ejbAssociationEnd2Lower = "1";
        String ejbAssociationEnd2Upper = "1";
        Boolean ejbAssociationEnd2Composition = "true".equals(sourceDomain.query(id + ".composition").replace("'", "")) ? true : false;
        targetDomain.insertEJBAssociationEndStub(ejbAssociationEnd2Id, ejbAssociationEnd2Name, ejbAssociationEnd2Lower, ejbAssociationEnd2Upper, ejbAssociationEnd2Composition);

        joinedDomain.insertUMLAssociationEndEmEJBAssociationusingRule11(id, ejbAssociationEnd2Id);
    }
    // Linkagem
    private void linkRule11UMLAssociationEndtoEJBAssociation() throws Exception {
        String[] ids = processQueryResult(sourceDomain.query("UMLAssociationEndEmEJBAssociationusingRule11.allInstances()"));
        for (String id : ids) {
            if (id != null && !"".equals(id)) {
                linkEJBAssociationEndfromAssociationusingRule11(id);
            }
        }
    }
    private void linkEJBAssociationEndfromAssociationusingRule11(String id) throws Exception {
        String umlAssociationEndId = processQueryResult(joinedDomain.query(id + ".associationEnd"))[0];
        String ejbAssociationEndId = processQueryResult(joinedDomain.query(id + ".ejbAssociationEnd2"))[0];

        // umlAssociationEnd.type <~> ejbAssociationEnd2.type.oclAsType(EJB::EJBKeyClass)
        String umlAssociationTypeId = processQueryResult(sourceDomain.query(umlAssociationEndId + ".classifier"))[0];
        targetDomain.insertEJBAssociationEndTypeLink(ejbAssociationEndId, findEjbKeyClassBasedonUmlId(umlAssociationTypeId));

        // umlAssociationEnd.association <~> ejbAssociationEnd2.class.oclAsType(EJB::EJBDataClass)
        String umlAssociationClassId = processQueryResult(sourceDomain.query(umlAssociationEndId + ".association"))[0];
        targetDomain.insertEJBAssociationEndClassLink(ejbAssociationEndId, findEjbDataClassBasedonUmlId(umlAssociationClassId));
    }
    //--------------------------------------------------------------------------
    // REGRA 12
    // Criacao
    private void transformRuleUMLOperationtoBusinessMethod() throws Exception {
        String[] ids = processQueryResult(sourceDomain.query("Operation.allInstances()"));

        for (String id : ids) {
            String name = sourceDomain.query(id + ".name").replace("'", "");
            if (!"NULL_OPERATION".equals(id)) {
                createBusinessMethodfromOperation(id, name);
            }
        }
    }
    private void createBusinessMethodfromOperation(String id, String name) throws Exception {
        String businessMethodName = name;
        String businessMethodId = name + System.nanoTime();

        targetDomain.insertBusinessMethodStub(businessMethodId, businessMethodName);

        joinedDomain.insertUMLOperationToBusinessMethod(id, businessMethodId);
    }
    // Linkagem
    private void linkRuleUMLOperationtoBusinessMethod() throws Exception {
        String[] ids = processQueryResult(sourceDomain.query("UMLOperationToBusinessMethod.allInstances()"));

        for (String id : ids) {
            if (id != null && !"".equals(id)) {
                linkBusinessMethodfromOperation(id);
            }
        }
    }
    private void linkBusinessMethodfromOperation(String id) throws Exception {
        String umlOperationId = processQueryResult(joinedDomain.query(id + ".operation"))[0];
        String[] umlOperationParameterIds = processQueryResult(sourceDomain.query(umlOperationId + ".parameter"));
        String umlOperationTypeId = processQueryResult(sourceDomain.query(umlOperationId + ".classifier"))[0];

        String businessMethodId = processQueryResult(joinedDomain.query(id + ".businessMethod").replace("'", ""))[0];

        // umlOperation.type <~> businessMethod.type        !!! Nao esta no livro !!!
        String businessMethodTypeId = findEjbIdBasedonUmlId(umlOperationTypeId);
        targetDomain.insertBusinessMethodTypeLink(businessMethodId, businessMethodTypeId);

        // umlOperation.parameter <~> businessMethod.parameter
        if (umlOperationParameterIds != null && umlOperationParameterIds.length > 0 && !"".equals(umlOperationParameterIds[0])) {
            for (String parameterId : umlOperationParameterIds) {
                String ejbParameterId = processQueryResult(sourceDomain.query(parameterId + ".transformerToEjbParameter.ejbParameter"))[0];
                targetDomain.insertEJBParameterBusinessMethodLink(ejbParameterId, businessMethodId);
            }
        }
    }
    //--------------------------------------------------------------------------
    // REGRA 13
    // Criacao
    private void transformRuleUMLParametertoEJBParameter() throws Exception {
        String[] ids = processQueryResult(sourceDomain.query("Parameter.allInstances()"));

        for (String id : ids) {
            String name = sourceDomain.query(id + ".name").replace("'", "");
            createEJBParameterfromParameter(id, name);
        }
    }
    private void createEJBParameterfromParameter(String id, String name) throws Exception {
        String ejbParameterName = name;
        String ejbParameterId = ejbParameterName + System.nanoTime();

        targetDomain.insertEJBParameterStub(ejbParameterId, ejbParameterName);

        joinedDomain.insertUMLParameterToEJBParameter(id, ejbParameterId);
    }
    // Linkagem
    private void linkUMLParametertoEJBParameter() throws Exception {
        String[] ids = processQueryResult(joinedDomain.query("UMLParameterToEJBParameter.allInstances()"));
        for (String id : ids) {
            if (id != null && !"".equals(id)) {
                linkEJBParameterfromParameter(id);
            }
        }
    }
    private void linkEJBParameterfromParameter(String id) throws Exception {
        String umlParameterId = processQueryResult(joinedDomain.query(id + ".parameter"))[0];
        String umlParameterTypeId = processQueryResult(sourceDomain.query(umlParameterId + ".classifier"))[0];

        String ejbParameterId = processQueryResult(joinedDomain.query(id + ".ejbParameter"))[0];

        // umlParameter.type <~> ejbParameter.type
        String ejbParameterTypeId = findEjbIdBasedonUmlId(umlParameterTypeId);
        targetDomain.insertEJBParameterTypeLink(ejbParameterId, ejbParameterTypeId);
    }
    //--------------------------------------------------------------------------
    // MINHA REGRA
    // Criacao
    private void transformUMLSettoEJBSet() throws Exception {
        String[] ids = processQueryResult(sourceDomain.query("UMLSet.allInstances()"));

        for (String id : ids) {
            String name = sourceDomain.query(id + ".name").replace("'", "");
            createEJBSetfromUMLSet(id, name);
        }
    }
    private void createEJBSetfromUMLSet(String id, String name) throws Exception {
        String ejbSetName = name;
        String ejbSetId = ejbSetName + System.nanoTime();

        targetDomain.insertEJBSetStub(ejbSetId, ejbSetName);

        joinedDomain.insertUMLSetToEJBSet(id, ejbSetId);
    }
    // Linkagem
    private void linkUMLSettoEJBSet() throws Exception {
        String[] ids = processQueryResult(joinedDomain.query("UMLSetToEJBSet.allInstances()"));
        for (String id : ids) {
            if (id != null && !"".equals(id)) {
                linkEJBSetfromUMLSet(id);
            }
        }
    }
    private void linkEJBSetfromUMLSet(String id) throws Exception {
        String umlSetId = processQueryResult(joinedDomain.query(id + ".umlSet"))[0];
        String umlSetTypeId = processQueryResult(sourceDomain.query(umlSetId + ".elementType"))[0];

        String ejbSetId = processQueryResult(joinedDomain.query(id + ".ejbSet"))[0];

        // umlSet.type <~> ejbSet.type
        String ejbSetTypeId = findEjbDataClassBasedonUmlId(umlSetTypeId);
        targetDomain.insertEJBSetTypeLink(ejbSetId, ejbSetTypeId);
    }
}
