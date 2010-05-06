package br.uff.ic.mda.transformer;

import core.XEOS;

public class UmlEjbDomain extends Domain {

    public UmlEjbDomain(XEOS ieos) throws Exception {
        super(ieos);
    }

    @Override
    public void createSpecificationOfCurrentDiagram() throws Exception {
    }

    @Override
    public void insertMetamodelInvariants() throws Exception {
        logger.debug("Inserting Uml MetaModel Invariants");

        // Todo DataType precisa de um correspondente EJBDataType
        this.insertInvariant("everyDataClassMustHaveEJBDataType", "DataType.allInstances()->select(dt : DataType | dt.oclIsTypeOf(DataType))->forAll(dt : DataType | dt.transformerToEjbDataType->notEmpty())");
        // Todo UMLSet precisa de um correspondente EJBSet
        this.insertInvariant("everyUmlSetMustHaveEjbSet", "UMLSet.allInstances()->forAll(s : UMLSet | s.transformerToSet->notEmpty())");
        // Toda Class e AssociationClass precisa de uma EJBKeyClass
        this.insertInvariant("everyClassMustHaveEJBKeyClass", "Class.allInstances()->excluding(NULL_CLASS)->forAll(c : Class | c.transformerToClass->notEmpty())");
        this.insertInvariant("everyAssociationClassMustHaveEJBKeyClass", "AssociationClass.allInstances()->forAll(ac : AssociationClass | ac.transformerToAssociationClass->notEmpty())");
        // Toda Class e AssociationClass precisa de uma EJBDataClass
        this.insertInvariant("everyClassMustHaveEJBDataClass", "Class.allInstances()->excluding(NULL_CLASS)->forAll(c : Class | c.transformerToEjbDataClass->notEmpty() or c.transformerToEntityComponent->notEmpty())");
        this.insertInvariant("everyAssociationClassMustHaveEJBDataClass", "AssociationClass.allInstances()->forAll(ac : AssociationClass | ac.transformerToEjbDataClassfromAssociationClass->notEmpty())");
        // Toda Class que eh OuterMostContainer precisa de uma EJBEntityComponent
        this.insertInvariant("everyOuterMostClassmusthaveEJBEntityComponent", "Class.allInstances()->excluding(NULL_CLASS)->select(c : Class | c.isOuterMostContainer())->forAll(c : Class | c.transformerToEntityComponent->notEmpty())");
    }

    @Override
    public boolean insertMetamodelOperations() throws Exception {
        logger.debug("Inserting Uml MetaModel Operations");
        return true;
    }

    @Override
    public void insertMetamodelClasses() throws Exception {
        this.ieos.insertClass("UMLDataTypeToEJBDataType");                      // Regra Minha
        this.ieos.insertClass("UMLClassToEJBKeyClass");                         // Regra 1
        this.ieos.insertClass("UMLAssociationClassToEJBKeyClass");              // Regra 2
        this.ieos.insertClass("UMLOperationToBusinessMethod");                  // Regra 12
        this.ieos.insertClass("UMLParameterToEJBParameter");                    // Regra 13
        this.ieos.insertClass("UMLAttributeToEJBAttribute");                    // Regra 7
        this.ieos.insertClass("UMLClassToEJBDataClass");                        // Regra 4
        this.ieos.insertClass("UMLAssociationToEJBDataAssociation");            // Regra 5
        this.ieos.insertClass("UMLAssociationClassToEJBDataClass");             // Regra 6
        this.ieos.insertClass("UMLAssociationEndToEJBDataEndusingRule8");       // Regra 8
        this.ieos.insertClass("UMLAssociationEndToEJBDataEndusingRule9");       // Regra 9
        this.ieos.insertClass("UMLAssociationEndEmEJBAssociationusingRule10");  // Regra 10
        this.ieos.insertClass("UMLAssociationEndEmEJBAssociationusingRule11");  // Regra 11
        this.ieos.insertClass("UMLClassToEJBEntityComponent");                  // Regra 3
        this.ieos.insertClass("UMLSetToEJBSet");                          // Minha regra
    }

    @Override
    public void insertMetamodelAssociations() throws Exception {
        // Associacoes UMLDataTypeToEJBDataType
        this.ieos.insertAssociation("DataType", "dataType", "1", "0..1", "transformerToEjbDataType", "UMLDataTypeToEJBDataType");
        this.ieos.insertAssociation("EJBDataType", "ejbDataType", "1", "0..1", "transformerToEjbDataType", "UMLDataTypeToEJBDataType");

        // Associacoes UMLClassToEJBKeyClass
        this.ieos.insertAssociation("Class", "class", "1", "0..1", "transformerToClass", "UMLClassToEJBKeyClass");
        this.ieos.insertAssociation("EJBAttribute", "id", "1", "0..1", "transformerToClass", "UMLClassToEJBKeyClass");
        this.ieos.insertAssociation("EJBKeyClass", "keyClass", "1", "0..1", "transformerToClass", "UMLClassToEJBKeyClass");

        // Associacoes UMLAssociationClassToEJBKeyClass
        this.ieos.insertAssociation("AssociationClass", "associationClass", "1", "0..1", "transformerToAssociationClass", "UMLAssociationClassToEJBKeyClass");
        this.ieos.insertAssociation("EJBAttribute", "id", "2", "0..1", "transformerToAssociationClass", "UMLAssociationClassToEJBKeyClass");
        this.ieos.insertAssociation("EJBKeyClass", "keyClass", "1", "0..1", "transformerToAssociationClass", "UMLAssociationClassToEJBKeyClass");

        // Associacoes UMLOperationToBusinessMethod
        this.ieos.insertAssociation("Operation", "operation", "1", "0..1", "transformerToBusinessMethod", "UMLOperationToBusinessMethod");
        this.ieos.insertAssociation("BusinessMethod", "businessMethod", "1", "0..1", "transformerToBusinessMethod", "UMLOperationToBusinessMethod");

        // Associacoes UMLParameterToEJBParameter
        this.ieos.insertAssociation("Parameter", "parameter", "1", "0..1", "transformerToEjbParameter", "UMLParameterToEJBParameter");
        this.ieos.insertAssociation("EJBParameter", "ejbParameter", "1", "0..1", "transformerToEjbParameter", "UMLParameterToEJBParameter");

        // Associacoes UMLAttributeToEJBAttribute
        this.ieos.insertAssociation("Attribute", "attribute", "1", "0..1", "transformerToEjbAttribute", "UMLAttributeToEJBAttribute");
        this.ieos.insertAssociation("EJBAttribute", "ejbAttribute", "1", "0..1", "transformerToEjbAttribute", "UMLAttributeToEJBAttribute");

        // Associacoes UMLClassToEJBDataClass
        this.ieos.insertAssociation("Class", "class", "1", "0..1", "transformerToEjbDataClass", "UMLClassToEJBDataClass");
        this.ieos.insertAssociation("EJBDataClass", "ejbDataClass", "1", "0..1", "transformerToEjbDataClass", "UMLClassToEJBDataClass");

        // Associacoes UMLAssociationToEJBDataAssociation
        this.ieos.insertAssociation("Association", "association", "1", "0..1", "transformerToEjbDataAssociationusingRule5", "UMLAssociationToEJBDataAssociation");
        this.ieos.insertAssociation("EJBDataAssociation", "ejbDataAssociation", "1", "0..1", "transformerToEjbDataAssociationusingRule5", "UMLAssociationToEJBDataAssociation");

        // Associacoes UMLAssociationClassToEJBDataClass
        this.ieos.insertAssociation("AssociationClass", "associationClass", "1", "0..1", "transformerToEjbDataClassfromAssociationClass", "UMLAssociationClassToEJBDataClass");
        this.ieos.insertAssociation("EJBDataClass", "ejbDataClass", "1", "0..1", "transformerToEjbDataClassfromAssociationClass", "UMLAssociationClassToEJBDataClass");

        // Associacoes UMLAssociationEndToEJBDataEndusingRule8
        this.ieos.insertAssociation("AssociationEnd", "associationEnd", "1", "0..1", "transformerToEjbAssociationEndusingRule8", "UMLAssociationEndToEJBDataEndusingRule8");
        this.ieos.insertAssociation("EJBAssociationEnd", "ejbAssociationEnd", "1", "0..1", "transformerToEjbAssociationEndusingRule8", "UMLAssociationEndToEJBDataEndusingRule8");

        // Associacoes UMLAssociationEndToEJBDataEndusingRule9
        this.ieos.insertAssociation("AssociationEnd", "associationEnd", "1", "0..1", "transformerToEjbAssociationEndusingRule9", "UMLAssociationEndToEJBDataEndusingRule9");
        this.ieos.insertAssociation("EJBAssociationEnd", "ejbAssociationEnd", "1", "0..1", "transformerToEjbAssociationEndusingRule9", "UMLAssociationEndToEJBDataEndusingRule9");

        // Associacoes UMLAssociationEndEmEJBAssociationusingRule10
        this.ieos.insertAssociation("AssociationEnd", "associationEnd", "1", "0..1", "transformerToEjbDataAssociationusingRule10", "UMLAssociationEndEmEJBAssociationusingRule10");
        this.ieos.insertAssociation("EJBDataAssociation", "ejbDataAssociation", "1", "0..1", "transformerToEjbDataAssociationusingRule10", "UMLAssociationEndEmEJBAssociationusingRule10");
        this.ieos.insertAssociation("EJBAssociationEnd", "ejbAssociationEnd1", "1", "0..1", "transformerToEjbDataAssociationusingRule10_1", "UMLAssociationEndEmEJBAssociationusingRule10");
        this.ieos.insertAssociation("EJBAssociationEnd", "ejbAssociationEnd2", "1", "0..1", "transformerToEjbDataAssociationusingRule10_2", "UMLAssociationEndEmEJBAssociationusingRule10");

        // Associacoes UMLAssociationEndEmEJBAssociationusingRule11
        this.ieos.insertAssociation("AssociationEnd", "associationEnd", "1", "0..1", "transformerToEjbDataAssociationusingRule11", "UMLAssociationEndEmEJBAssociationusingRule11");
        this.ieos.insertAssociation("EJBAssociationEnd", "ejbAssociationEnd2", "1", "0..1", "transformerToEjbDataAssociationusingRule11", "UMLAssociationEndEmEJBAssociationusingRule11");

        // Associacoes UMLClassToEJBEntityComponent
        this.ieos.insertAssociation("Class", "class", "1", "0..1", "transformerToEntityComponent", "UMLClassToEJBEntityComponent");
        this.ieos.insertAssociation("EJBEntityComponent", "entityComponent", "1", "0..1", "transformerToEntityComponent", "UMLClassToEJBEntityComponent");
        this.ieos.insertAssociation("EJBDataClass", "dataClass", "1", "0..1", "transformerToEntityComponent", "UMLClassToEJBEntityComponent");
        this.ieos.insertAssociation("EJBDataSchema", "dataSchema", "1", "0..1", "transformerToEntityComponent", "UMLClassToEJBEntityComponent");
        this.ieos.insertAssociation("EJBServingAttribute", "servingAttribute", "1", "0..1", "transformerToEntityComponent", "UMLClassToEJBEntityComponent");

        // Associacoes UMLSetToEJBSet
        this.ieos.insertAssociation("UMLSet", "umlSet", "1", "0..1", "transformerToSet", "UMLSetToEJBSet");
        this.ieos.insertAssociation("EJBSet", "ejbSet", "1", "0..1", "transformerToSet", "UMLSetToEJBSet");
    }

    @Override
    public void insertMetamodelAttributes() throws Exception {
        this.ieos.insertAttribute("UMLDataTypeToEJBDataType", "name", "String");
        this.ieos.insertAttribute("UMLClassToEJBKeyClass", "name", "String");
        this.ieos.insertAttribute("UMLAssociationClassToEJBKeyClass", "name", "String");
        this.ieos.insertAttribute("UMLOperationToBusinessMethod", "name", "String");
        this.ieos.insertAttribute("UMLParameterToEJBParameter", "name", "String");
        this.ieos.insertAttribute("UMLAttributeToEJBAttribute", "name", "String");
        this.ieos.insertAttribute("UMLClassToEJBDataClass", "name", "String");
        this.ieos.insertAttribute("UMLAssociationToEJBDataAssociation", "name", "String");
        this.ieos.insertAttribute("UMLAssociationEndToEJBDataEndusingRule8", "name", "String");
        this.ieos.insertAttribute("UMLAssociationEndToEJBDataEndusingRule9", "name", "String");
        this.ieos.insertAttribute("UMLAssociationEndEmEJBAssociationusingRule10", "name", "String");
        this.ieos.insertAttribute("UMLAssociationEndEmEJBAssociationusingRule11", "name", "String");
        this.ieos.insertAttribute("UMLClassToEJBEntityComponent", "name", "String");
        this.ieos.insertAttribute("UMLAssociationClassToEJBDataClass", "name", "String");
        this.ieos.insertAttribute("UMLSetToEJBSet", "name", "String");
    }

    public boolean insertOperationOCL(String contextClass, String nameOperation,
            String returnType, String bodyOperation, Object[] params) throws Exception {
        return this.ieos.insertOperation(contextClass, nameOperation, returnType, bodyOperation, params);

    }

    // Specific methods of this domain

    // UMLDataTypeToEJBDataType
    public boolean insertUMLDataTypeToEJBDataType(String umlDataTypeId, String ejbDataTypeId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a class: the UML diagram must be created");
            return false;
        }
        String name = umlDataTypeId + "To" + ejbDataTypeId;
        String id = name + System.nanoTime();
        this.ieos.insertObject("UMLDataTypeToEJBDataType", id);
        this.ieos.insertValue("UMLDataTypeToEJBDataType", "name", id, name);
        this.ieos.insertLink("DataType", umlDataTypeId, "dataType", "transformerToEjbDataType", id, "UMLDataTypeToEJBDataType");
        this.ieos.insertLink("EJBDataType", ejbDataTypeId, "ejbDataType", "transformerToEjbDataType", id, "UMLDataTypeToEJBDataType");
        return true;
    }

    // UMLClassToEJBKeyClass
    public boolean insertUMLClassToEJBKeyClass(String umlClassId, String ejbKeyClassId, String ejbAttributeId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a class: the UML diagram must be created");
            return false;
        }
        String name = umlClassId + "To" + ejbKeyClassId;
        String id = name + System.nanoTime();
        this.ieos.insertObject("UMLClassToEJBKeyClass", id);
        this.ieos.insertValue("UMLClassToEJBKeyClass", "name", id, name);
        this.ieos.insertLink("Class", umlClassId, "class", "transformerToClass", id, "UMLClassToEJBKeyClass");
        this.ieos.insertLink("EJBKeyClass", ejbKeyClassId, "keyClass", "transformerToClass", id, "UMLClassToEJBKeyClass");
        this.ieos.insertLink("EJBAttribute", ejbAttributeId, "id", "transformerToClass", id, "UMLClassToEJBKeyClass");
        return true;
    }

    // UMLAssociationClassToEJBKeyClass
    public boolean insertUMLAssociationClassToEJBKeyClass(String umlClassId, String ejbKeyClassId, String ejbAttribute1Id, String ejbAttribute2Id) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a class: the UML diagram must be created");
            return false;
        }
        String name = umlClassId + "To" + ejbKeyClassId;
        String id = name + System.nanoTime();
        this.ieos.insertObject("UMLAssociationClassToEJBKeyClass", id);
        this.ieos.insertValue("UMLAssociationClassToEJBKeyClass", "name", id, name);
        this.ieos.insertLink("AssociationClass", umlClassId, "associationClass", "transformerToAssociationClass", id, "UMLAssociationClassToEJBKeyClass");
        this.ieos.insertLink("EJBKeyClass", ejbKeyClassId, "keyClass", "transformerToAssociationClass", id, "UMLAssociationClassToEJBKeyClass");
        this.ieos.insertLink("EJBAttribute", ejbAttribute1Id, "id", "transformerToAssociationClass", id, "UMLAssociationClassToEJBKeyClass");
        this.ieos.insertLink("EJBAttribute", ejbAttribute2Id, "id", "transformerToAssociationClass", id, "UMLAssociationClassToEJBKeyClass");
        return true;
    }

    // UMLParameterToEJBParameter
    public boolean insertUMLParameterToEJBParameter(String umlParameterId, String ejbParameterId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a class: the UML diagram must be created");
            return false;
        }
        String name = umlParameterId + "To" + ejbParameterId;
        String id = name + System.nanoTime();
        this.ieos.insertObject("UMLParameterToEJBParameter", id);
        this.ieos.insertValue("UMLParameterToEJBParameter", "name", id, name);
        this.ieos.insertLink("Parameter", umlParameterId, "parameter", "transformerToEjbParameter", id, "UMLParameterToEJBParameter");
        this.ieos.insertLink("EJBParameter", ejbParameterId, "ejbParameter", "transformerToEjbParameter", id, "UMLParameterToEJBParameter");
        return true;
    }

    // UMLOperationToBusinessMethod
    public boolean insertUMLOperationToBusinessMethod(String umlOperationId, String businessMethodId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a class: the UML diagram must be created");
            return false;
        }
        String name = umlOperationId + "To" + businessMethodId;
        String id = name + System.nanoTime();
        this.ieos.insertObject("UMLOperationToBusinessMethod", id);
        this.ieos.insertValue("UMLOperationToBusinessMethod", "name", id, name);
        this.ieos.insertLink("Operation", umlOperationId, "operation", "transformerToBusinessMethod", id, "UMLOperationToBusinessMethod");
        this.ieos.insertLink("BusinessMethod", businessMethodId, "businessMethod", "transformerToBusinessMethod", id, "UMLOperationToBusinessMethod");
        return true;
    }

    // UMLAttributeToEJBAttribute
    public boolean insertUMLAttributeToEJBAttribute(String umlAttributeId, String ejbAttributeId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a class: the UML diagram must be created");
            return false;
        }
        String name = umlAttributeId + "To" + ejbAttributeId;
        String id = name + System.nanoTime();
        this.ieos.insertObject("UMLAttributeToEJBAttribute", id);
        this.ieos.insertValue("UMLAttributeToEJBAttribute", "name", id, name);
        this.ieos.insertLink("Attribute", umlAttributeId, "attribute", "transformerToEjbAttribute", id, "UMLAttributeToEJBAttribute");
        this.ieos.insertLink("EJBAttribute", ejbAttributeId, "ejbAttribute", "transformerToEjbAttribute", id, "UMLAttributeToEJBAttribute");
        return true;
    }

    // UMLClassToEJBDataClass
    public boolean insertUMLClassToEJBDataClass(String umlClassId, String ejbDataClassId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a class: the UML diagram must be created");
            return false;
        }
        String name = umlClassId + "To" + ejbDataClassId;
        String id = name + System.nanoTime();
        this.ieos.insertObject("UMLClassToEJBDataClass", id);
        this.ieos.insertValue("UMLClassToEJBDataClass", "name", id, name);
        this.ieos.insertLink("Class", umlClassId, "class", "transformerToEjbDataClass", id, "UMLClassToEJBDataClass");
        this.ieos.insertLink("EJBDataClass", ejbDataClassId, "ejbDataClass", "transformerToEjbDataClass", id, "UMLClassToEJBDataClass");
        return true;
    }

    // UMLAssociationToEJBDataAssociation
    public boolean insertUMLAssociationToEJBDataAssociation(String umlAssociationId, String ejbDataAssociationId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a class: the UML diagram must be created");
            return false;
        }
        String name = umlAssociationId + "To" + ejbDataAssociationId;
        String id = name + System.nanoTime();
        this.ieos.insertObject("UMLAssociationToEJBDataAssociation", id);
        this.ieos.insertValue("UMLAssociationToEJBDataAssociation", "name", id, name);
        this.ieos.insertLink("Association", umlAssociationId, "association", "transformerToEjbDataAssociationusingRule5", id, "UMLAssociationToEJBDataAssociation");
        this.ieos.insertLink("EJBDataAssociation", ejbDataAssociationId, "ejbDataAssociation", "transformerToEjbDataAssociationusingRule5", id, "UMLAssociationToEJBDataAssociation");
        return true;
    }

    // UMLAssociationClassToEJBDataClass
    public boolean insertUMLAssociationClassToEJBDataClass(String umlAssociationClassId, String ejbDataClassId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a class: the UML diagram must be created");
            return false;
        }

        String name = umlAssociationClassId + "To" + ejbDataClassId;
        String id = name + System.nanoTime();
        this.ieos.insertObject("UMLAssociationClassToEJBDataClass", id);
        this.ieos.insertValue("UMLAssociationClassToEJBDataClass", "name", id, name);
        this.ieos.insertLink("AssociationClass", umlAssociationClassId, "associationClass", "transformerToEjbDataClassfromAssociationClass", id, "UMLAssociationClassToEJBDataClass");
        this.ieos.insertLink("EJBDataClass", ejbDataClassId, "ejbDataClass", "transformerToEjbDataClassfromAssociationClass", id, "UMLAssociationClassToEJBDataClass");
        return true;
    }

    // UMLAssociationEndToEJBDataEndusingRule8
    public boolean insertUMLAssociationEndToEJBDataEndusingRule8(String umlAssociationEndId, String ejbAssociationEndId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a class: the UML diagram must be created");
            return false;
        }
        String name = umlAssociationEndId + "To" + ejbAssociationEndId;
        String id = name + System.nanoTime();
        this.ieos.insertObject("UMLAssociationEndToEJBDataEndusingRule8", id);
        this.ieos.insertValue("UMLAssociationEndToEJBDataEndusingRule8", "name", id, name);
        this.ieos.insertLink("AssociationEnd", umlAssociationEndId, "associationEnd", "transformerToEjbAssociationEndusingRule8", id, "UMLAssociationEndToEJBDataEndusingRule8");
        this.ieos.insertLink("EJBAssociationEnd", ejbAssociationEndId, "ejbAssociationEnd", "transformerToEjbAssociationEndusingRule8", id, "UMLAssociationEndToEJBDataEndusingRule8");
        return true;
    }

    // UMLAssociationEndToEJBDataEndusingRule9
    public boolean insertUMLAssociationEndToEJBDataEndusingRule9(String umlAssociationEndId, String ejbAssociationEndId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a class: the UML diagram must be created");
            return false;
        }
        String name = umlAssociationEndId + "To" + ejbAssociationEndId;
        String id = name + System.nanoTime();
        this.ieos.insertObject("UMLAssociationEndToEJBDataEndusingRule9", id);
        this.ieos.insertValue("UMLAssociationEndToEJBDataEndusingRule9", "name", id, name);
        this.ieos.insertLink("AssociationEnd", umlAssociationEndId, "associationEnd", "transformerToEjbAssociationEndusingRule9", id, "UMLAssociationEndToEJBDataEndusingRule9");
        this.ieos.insertLink("EJBAssociationEnd", ejbAssociationEndId, "ejbAssociationEnd", "transformerToEjbAssociationEndusingRule9", id, "UMLAssociationEndToEJBDataEndusingRule9");
        return true;
    }

    // UMLAssociationEndEmEJBAssociationusingRule10
    public boolean insertUMLAssociationEndEmEJBAssociationusingRule10(String umlAssociationEndId, String ejbAssociationId, String ejbAssociationEnd1Id, String ejbAssociationEnd2Id) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a class: the UML diagram must be created");
            return false;
        }
        String name = umlAssociationEndId + "To" + ejbAssociationId;
        String id = name + System.nanoTime();
        this.ieos.insertObject("UMLAssociationEndEmEJBAssociationusingRule10", id);
        this.ieos.insertValue("UMLAssociationEndEmEJBAssociationusingRule10", "name", id, name);
        this.ieos.insertLink("AssociationEnd", umlAssociationEndId, "associationEnd", "transformerToEjbDataAssociationusingRule10", id, "UMLAssociationEndEmEJBAssociationusingRule10");
        this.ieos.insertLink("EJBDataAssociation", ejbAssociationId, "ejbDataAssociation", "transformerToEjbDataAssociationusingRule10", id, "UMLAssociationEndEmEJBAssociationusingRule10");
        this.ieos.insertLink("EJBAssociationEnd", ejbAssociationEnd1Id, "ejbAssociationEnd1", "transformerToEjbDataAssociationusingRule10_1", id, "UMLAssociationEndEmEJBAssociationusingRule10");
        this.ieos.insertLink("EJBAssociationEnd", ejbAssociationEnd2Id, "ejbAssociationEnd2", "transformerToEjbDataAssociationusingRule10_2", id, "UMLAssociationEndEmEJBAssociationusingRule10");
        return true;
    }

    // UMLAssociationEndEmEJBAssociationusingRule11
    public boolean insertUMLAssociationEndEmEJBAssociationusingRule11(String umlAssociationEndId, String ejbAssociationEnd2Id) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a class: the UML diagram must be created");
            return false;
        }
        String name = umlAssociationEndId + "To" + ejbAssociationEnd2Id;
        String id = name + System.nanoTime();
        this.ieos.insertObject("UMLAssociationEndEmEJBAssociationusingRule11", id);
        this.ieos.insertValue("UMLAssociationEndEmEJBAssociationusingRule11", "name", id, name);
        this.ieos.insertLink("AssociationEnd", umlAssociationEndId, "associationEnd", "transformerToEjbDataAssociationusingRule11", id, "UMLAssociationEndEmEJBAssociationusingRule11");
        this.ieos.insertLink("EJBAssociationEnd", ejbAssociationEnd2Id, "ejbAssociationEnd2", "transformerToEjbDataAssociationusingRule11", id, "UMLAssociationEndEmEJBAssociationusingRule11");
        return true;
    }

    // UMLClassToEJBEntityComponent
    public boolean insertUMLClassToEJBEntityComponent(String umlClassId, String ejbEntityComponentId, String ejbDataClassId, String ejbDataSchemaId, String ejbServingAttributeId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a class: the UML diagram must be created");
            return false;
        }
        String name = umlClassId + "To" + ejbEntityComponentId;
        String id = name + System.nanoTime();
        this.ieos.insertObject("UMLClassToEJBEntityComponent", id);
        this.ieos.insertValue("UMLClassToEJBEntityComponent", "name", id, name);
        this.ieos.insertLink("Class", umlClassId, "class", "transformerToEntityComponent", id, "UMLClassToEJBEntityComponent");
        this.ieos.insertLink("EJBEntityComponent", ejbEntityComponentId, "entityComponent", "transformerToEntityComponent", id, "UMLClassToEJBEntityComponent");
        this.ieos.insertLink("EJBDataClass", ejbDataClassId, "dataClass", "transformerToEntityComponent", id, "UMLClassToEJBEntityComponent");
        this.ieos.insertLink("EJBDataSchema", ejbDataSchemaId, "dataSchema", "transformerToEntityComponent", id, "UMLClassToEJBEntityComponent");
        this.ieos.insertLink("EJBServingAttribute", ejbServingAttributeId, "servingAttribute", "transformerToEntityComponent", id, "UMLClassToEJBEntityComponent");
        return true;
    }

    // UMLSetToEJBSet
    public boolean insertUMLSetToEJBSet(String umlSetId, String ejbSetId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a class: the UML diagram must be created");
            return false;
        }
        String name = umlSetId + "To" + ejbSetId;
        String id = name + System.nanoTime();
        this.ieos.insertObject("UMLSetToEJBSet", id);
        this.ieos.insertValue("UMLSetToEJBSet", "name", id, name);
        this.ieos.insertLink("UMLSet", umlSetId, "umlSet", "transformerToSet", id, "UMLSetToEJBSet");
        this.ieos.insertLink("EJBSet", ejbSetId, "ejbSet", "transformerToSet", id, "UMLSetToEJBSet");
        return true;
    }
}
