package br.uff.ic.mda.transformador;

import core.XEOS;

public class DominioEjb extends Dominio {

    public DominioEjb(XEOS ieos) {
        super(ieos);
    }

    @Override
    protected void insertMetamodelStructure() throws Exception {
        /* classes */
        this.ieos.insertClass("EJBModelElement");
        this.ieos.insertClass("EJBClassifier");
        this.ieos.insertClass("EJBTyped");
        this.ieos.insertClass("EJBDataSchema");
        this.ieos.insertClass("EJBDataType");
        this.ieos.insertClass("EJBClass");
        this.ieos.insertClass("EJBDataSchemaElement");
        this.ieos.insertClass("EJBKeyClass");
        this.ieos.insertClass("EJBComponent");
        this.ieos.insertClass("EJBDataClass");
        this.ieos.insertClass("EJBSessionComponent");
        this.ieos.insertClass("EJBDataAssociation");
        this.ieos.insertClass("EJBEntityComponent");
        this.ieos.insertClass("Table");
        this.ieos.insertClass("EJBFeature");
        this.ieos.insertClass("EJBAssociationEnd");
        this.ieos.insertClass("EJBAttribute");
        this.ieos.insertClass("BusinessMethod");
        this.ieos.insertClass("EJBServingAttribute");
        this.ieos.insertClass("EJBParameter");


        /* generalizations */
        //resources
        this.ieos.insertGeneralization("EJBClassifier", "EJBModelElement");
        this.ieos.insertGeneralization("EJBTyped", "EJBModelElement");
        this.ieos.insertGeneralization("Table", "EJBModelElement");
        this.ieos.insertGeneralization("EJBDataSchema", "EJBModelElement");
        this.ieos.insertGeneralization("EJBDataSchemaElement", "EJBModelElement");
        this.ieos.insertGeneralization("EJBDataType", "EJBClassifier");
        this.ieos.insertGeneralization("EJBClass", "EJBClassifier");
        this.ieos.insertGeneralization("EJBComponent", "EJBClass");
        this.ieos.insertGeneralization("EJBKeyClass", "EJBClass");
        this.ieos.insertGeneralization("EJBDataClass", "EJBClass");
        this.ieos.insertGeneralization("EJBSessionComponent", "EJBComponent");
        this.ieos.insertGeneralization("EJBEntityComponent", "EJBComponent");
        this.ieos.insertGeneralization("EJBDataClass", "EJBDataSchemaElement");
        this.ieos.insertGeneralization("EJBDataAssociation", "EJBDataSchemaElement");
        this.ieos.insertGeneralization("EJBFeature", "EJBTyped");
        this.ieos.insertGeneralization("EJBParameter", "EJBTyped");
        this.ieos.insertGeneralization("BusinessMethod", "EJBFeature");
        this.ieos.insertGeneralization("EJBAttribute", "EJBFeature");
        this.ieos.insertGeneralization("EJBAssociationEnd", "EJBFeature");
        this.ieos.insertGeneralization("EJBServingAttribute", "EJBAssociationEnd");

        /* associations */
        this.ieos.insertAssociation("EJBClassifier", "type", "1", "*", "typed", "EJBTyped");
        this.ieos.insertAssociation("EJBDataSchema", "package", "1", "*", "element", "EJBDataSchemaElement");
        this.ieos.insertAssociation("EJBClass", "class", "1", "0..*", "feature", "EJBFeature");
        this.ieos.insertAssociation("EJBEntityComponent", "entityComp", "1", "1..*", "usedTable", "Table");
        this.ieos.insertAssociation("EJBDataAssociation", "association", "0..1", "2", "associationEnds", "EJBAssociationEnd");
        this.ieos.insertAssociation("BusinessMethod", "operation", "1", "0..*", "parameter", "EJBParameter");

        /* attributes */
//        this.ieos.insertAttribute("EJBClassifier", "nameClassifier", "String");
//        this.ieos.insertAttribute("EJBTyped", "nameTyped", "String");
//        this.ieos.insertAttribute("EJBDataSchema", "nameDataSchema", "String");
//        this.ieos.insertAttribute("Table", "nameTable", "String");
//        this.ieos.insertAttribute("EJBDataAssociation", "nameAssociation", "String");
        this.ieos.insertAttribute("EJBModelElement", "name", "String");
        this.ieos.insertAttribute("EJBAssociationEnd", "lower", "String");
        this.ieos.insertAttribute("EJBAssociationEnd", "upper", "String");
        this.ieos.insertAttribute("EJBAssociationEnd", "composition", "Boolean");
        this.ieos.insertAttribute("EJBAttribute", "visibility", "String");
    }

    // FAZER
    protected void insertMetamodelInvariants() throws Exception {
        // Todo EJBServingAttribute pertence a um EJBEntityComponent (VALIDAR)
        this.insertInvariant("restrictionEJBServingAttributebelongstoEJBEntityComponent", "EJBServingAttribute.allInstances()->collect(sa : EJBServingAttribute | sa.class)->forAll(c : EJBClass | c.oclIsKindOf(EJBEntityComponent))");

        // Toda EJBDataSchemaElement precisa ter um EJBDataSchema
        this.insertInvariant("compositionEJBDataSchemaElementEJBDataSchema", "EJBDataSchemaElement.allInstances()->collect(dse : EJBDataSchemaElement | dse.package)->forAll(p : EJBDataSchema | p <> NULL_EJBDS)");

        // Toda EJBFeature precisa ter uma EJBClass
        this.insertInvariant("compositionEJBFeatureEJBClass", "EJBFeature.allInstances()->collect(f : EJBFeature | f.class)->forAll(c : EJBClassifier | c <> NULL_EJBC)");

        // Toda EJBParameter precisa ter um BusinessMethod
        this.insertInvariant("compositionEJBParameterBusinessMethod", "EJBParameter.allInstances()->collect(p : EJBParameter | p.operation)->forAll(o : BusinessMethod | o <> NULL_BM)");

        // Toda EJBTyped precisa ter um EJBClassifier
        this.insertInvariant("compositionEJBTypedEJBClassifier", "EJBTyped.allInstances()->collect(t : EJBTyped | t.type)->forAll(c : EJBClassifier | c <> NULL_EJBC)");

        // Toda EJBEntityComponent precisa ter, no minimo, um Table
        this.insertInvariant("compositionEJBEntityComponentTable", "EJBEntityComponent.allInstances()->forAll(ec : EJBEntityComponent | ec.usedTable->size() >= 1)");

        // Toda EJBDataAssociation precisa ter dois EJBAssociationEnd
        this.insertInvariant("compositionEJBDataAssociationEJBAssociation", "EJBDataAssociation.allInstances()->forAll(da : EJBDataAssociation | da.associationEnds->size() = 2)");

        // Toda EJBModelElement precisa que o campo 'name' esteja preenchido, exceto EJBAssociation e EJBAssociationEnd
        this.insertInvariant("restrictionRequiredFieldNameEJBModelElement", "EJBModelElement.allInstances()->forAll(me : EJBModelElement | me.name <> '' or me.oclIsKindOf(EJBDataAssociation) or me.oclIsKindOf(EJBAssociationEnd))");
    }

    @Override
    protected boolean insertMetamodelOperations() throws Exception {
        logger.debug("Inserting Ejb MetaModel Operations");
        boolean result = true;

        Object[] params;
        String[] param;

//        result &= this.insertOperationOCL("ClasseColocadaNoSistema", "NomeDoMetodo", "Retorno", "CodigoOCL", new Object[0]);

        if (!result) {
            throw new Exception("It was not possible to insert all the operations in the SecureUML model");
        }

        return result;
    }

    @Override
    public void createSpecificationOfCurrentDiagram() throws Exception {
        this.ieos.insertObject("EJBDataSchemaElement", "NULL_EJBDSE");
        this.ieos.insertObject("EJBDataSchema", "NULL_EJBDS");
        this.ieos.insertObject("EJBClassifier", "NULL_EJBC");
        this.ieos.insertObject("EJBTyped", "NULL_EJBT");
        this.ieos.insertObject("BusinessMethod", "NULL_BM");

        this.ieos.insertObject("EJBDataType", "EJBInteger");
        this.ieos.insertValue("EJBDataType", "name", "EJBInteger", "Integer");
    }

    // Metodos especificos do metamodelo
    public boolean insertEJBDataSchema(String id, String name) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBDataSchema: the security diagram must be created");
            return false;
        }

        try {
            this.ieos.insertObject("EJBDataSchema", id);
            this.ieos.insertValue("EJBDataSchema", "name", id, name == null ? "" : name);
        } catch (Exception e) {
            logger.error("Error when insert an EJBDataSchema: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertEJBDataType(String id, String name) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBDataType: the security diagram must be created");
            return false;
        }

        try {
            this.ieos.insertObject("EJBDataType", id);
            this.ieos.insertValue("EJBDataType", "name", id, name == null ? "" : name);
        } catch (Exception e) {
            logger.error("Error when insert an EJBDataType: " + e.getMessage());
            return false;
        }
        return true;
    }

    //OK
    public boolean insertEJBKeyClass(String id, String name) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBKeyClass: the security diagram must be created");
            return false;
        }

        try {
            this.ieos.insertObject("EJBKeyClass", id);
            this.ieos.insertValue("EJBKeyClass", "name", id, name == null ? "" : name);
        } catch (Exception e) {
            logger.error("Error when insert an EJBKeyClass: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertEJBDataClass(String id, String name, String ejbDataSchemaId) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBDataClass: the security diagram must be created");
            return false;
        }
        try {
            this.ieos.insertObject("EJBDataClass", id);
            this.ieos.insertValue("EJBDataClass", "name", id, name == null ? "" : name);

            this.ieos.insertLink("EJBDataSchema", ejbDataSchemaId, "package", "element", id, "EJBDataClass");
        } catch (Exception e) {
            logger.error("Error when insert an EJBDataClass: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertEJBDataAssociation(String id, String name, String ejbDataSchemaId, String... ejbAssociationEnds) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBDataAssociation: the security diagram must be created");
            return false;
        }

        try {
            this.ieos.insertObject("EJBDataAssociation", id);
            this.ieos.insertValue("EJBDataAssociation", "name", id, name == null ? "" : name);
            this.ieos.insertLink("EJBDataSchema", ejbDataSchemaId, "package", "element", id, "EJBDataAssociation");

            for (String ejbAssociationEnd : ejbAssociationEnds) {
                this.ieos.insertLink("EJBDataAssociation", id, "association", "associationEnds", ejbAssociationEnd, "EJBAssociationEnd");
            }
        } catch (Exception e) {
            logger.error("Error when insert an EJBDataAssociation: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertEJBSessionComponent(String id, String name) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBSessionComponent: the security diagram must be created");
            return false;
        }

        try {
            this.ieos.insertObject("EJBSessionComponent", id);
            this.ieos.insertValue("EJBSessionComponent", "name", id, name == null ? "" : name);
        } catch (Exception e) {
            logger.error("Error when insert an EJBSessionComponent: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertEJBEntityComponent(String id, String name, String... tableIds) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBEntityComponent: the security diagram must be created");
            return false;
        }

        try {
            this.ieos.insertObject("EJBEntityComponent", id);
            this.ieos.insertValue("EJBEntityComponent", "name", id, name == null ? "" : name);

            for (String tableId : tableIds) {
                this.ieos.insertLink("EJBEntityComponent", id, "", "usedTable", tableId, "Table");
            }
        } catch (Exception e) {
            logger.error("Error when insert an EJBEntityComponent: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertTable(String id, String name) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a Table: the security diagram must be created");
            return false;
        }

        try {
            this.ieos.insertObject("Table", id);
            this.ieos.insertValue("Table", "name", id, name == null ? "" : name);
        } catch (Exception e) {
            logger.error("Error when insert a Table: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertBusinessMethod(String id, String name, String typeId, String ejbClassId) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a BusinessMethod: the security diagram must be created");
            return false;
        }

        try {
            this.ieos.insertObject("BusinessMethod", id);
            this.ieos.insertValue("BusinessMethod", "name", id, name == null ? "" : name);

            this.ieos.insertLink("BusinessMethod", id, "typed", "type", typeId, "EJBClassifier");
            this.ieos.insertLink("BusinessMethod", id, "feature", "class", ejbClassId, "EJBClass");
        } catch (Exception e) {
            logger.error("Error when insert a BusinessMethod: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertEJBParameter(String id, String name, String typeId, String businessMethodId) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBParameter: the security diagram must be created");
            return false;
        }

        try {
            this.ieos.insertObject("EJBParameter", id);
            this.ieos.insertValue("EJBParameter", "name", id, name == null ? "" : name);

            this.ieos.insertLink("EJBParameter", id, "typed", "type", typeId, "EJBClassifier");
            this.ieos.insertLink("BusinessMethod", businessMethodId, "operation", "parameter", id, "EJBParameter");
        } catch (Exception e) {
            logger.error("Error when insert an EJBParameter: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertEJBAttribute(String id, String name, String visibility, String typeId, String ejbClassId) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBAttribute: the security diagram must be created");
            return false;
        }

        try {
            this.ieos.insertObject("EJBAttribute", id);
            this.ieos.insertValue("EJBAttribute", "name", id, name == null ? "" : name);
            this.ieos.insertValue("EJBAttribute", "visibility", id, visibility == null ? "" : visibility);
            
            this.ieos.insertLink("EJBAttribute", id, "typed", "type", typeId, "EJBClassifier");
            this.ieos.insertLink("EJBAttribute", id, "feature", "class", ejbClassId, "EJBClass");
        } catch (Exception e) {
            logger.error("Error when insert an EJBAttribute: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertEJBAssociationEnd(String id, String name, String lower, String upper, Boolean composition, String typeId, String ejbClassId) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBAssociationEnd: the security diagram must be created");
            return false;
        }

        try {
            this.ieos.insertObject("EJBAssociationEnd", id);
            this.ieos.insertValue("EJBAssociationEnd", "name", id, name == null ? "" : name);
            this.ieos.insertValue("EJBAssociationEnd", "lower", id, lower == null ? "" : lower);
            this.ieos.insertValue("EJBAssociationEnd", "upper", id, upper == null ? "" : upper);
            this.ieos.insertValue("EJBAssociationEnd", "composition", id, composition == true ? "true" : "false" );

            this.ieos.insertLink("EJBAssociationEnd", id, "typed", "type", typeId, "EJBClassifier");
            this.ieos.insertLink("EJBAssociationEnd", id, "feature", "class", ejbClassId, "EJBClass");
        } catch (Exception e) {
            logger.error("Error when insert an EJBAssociationEnd: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertEJBServingAttribute(String id, String name, String lower, String upper, Boolean composition, String typeId, String ejbClassId) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBServingAttribute: the security diagram must be created");
            return false;
        }

        try {
            this.ieos.insertObject("EJBServingAttribute", id);
            this.ieos.insertValue("EJBServingAttribute", "name", id, name == null ? "" : name);
            this.ieos.insertValue("EJBServingAttribute", "lower", id, lower == null ? "" : lower);
            this.ieos.insertValue("EJBServingAttribute", "upper", id, upper == null ? "" : upper);
            this.ieos.insertValue("EJBServingAttribute", "composition", id, composition == true ? "true" : "false");

            this.ieos.insertLink("EJBServingAttribute", id, "typed", "type", typeId, "EJBClassifier");
            this.ieos.insertLink("EJBServingAttribute", id, "feature", "class", ejbClassId, "EJBClass");
        } catch (Exception e) {
            logger.error("Error when insert an EJBServingAttribute: " + e.getMessage());
            return false;
        }
        return true;
    }
}
