package br.uff.ic.transformador.core;

import core.XEOS;

public class DominioEjb extends Dominio {

    public DominioEjb(XEOS ieos) {
        super(ieos);
    }

    @Override
    protected void insertMetamodelStructure() throws Exception {
        /* classes */
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
        this.ieos.insertAttribute("EJBClassifier", "nameClassifier", "String");
        this.ieos.insertAttribute("EJBTyped", "nameTyped", "String");
        this.ieos.insertAttribute("EJBDataSchema", "nameDataSchema", "String");
        this.ieos.insertAttribute("Table", "nameTable", "String");
        this.ieos.insertAttribute("EJBAssociationEnd", "lower", "String");
        this.ieos.insertAttribute("EJBAssociationEnd", "upper", "String");
        this.ieos.insertAttribute("EJBAssociationEnd", "composition", "String");
        this.ieos.insertAttribute("EJBDataAssociation", "nameAssociation", "String");
        this.ieos.insertAttribute("EJBAttribute", "visibility", "String");
        this.ieos.insertAttribute("EJBAttribute", "type", "String");
    }

    protected void insertMetamodelInvariants() throws Exception {
//        // Todo EJBDataAssociation deve ter ou 2 EJBAssociationEnds ou 2 EJBServingAttributes com um deles pertencendo a uma classe diferente de EJBDataClass e o outro pertencendo a umEJBDataClass
//        this.insertInvariant("restrictionEJBDataAssociationAndEJBAssociationEnd", "EJBDataAssociation.allInstances()->forAll(da : EJBDataAssociation | da.associationEnds->forAll(ae : EJBAssociationEnd | not ae.oclIsKindOf(EJBServingAttribute)) or (da.associationEnds->forAll(ae : EJBAssociationEnd | ae.oclIsKindOf(EJBServingAttribute)) and da.associationEnds->exists(ae : EJBAssociationEnd | ae.class.oclIsKindOf(EJBDataClass)) and da.associationEnds->exists(ae : EJBAssociationEnd | not ae.class.oclIsKindOf(EJBDataClass))))");

        this.insertInvariant("restrictionEJBServingAttributebelongstoEJBEntityComponent", "EJBServingAttribute.allInstances()->forAll(sa | sa.class.oclIsKindOf(EJBEntityComponent))");

        // Toda EJBDataSchemaElement precisa ter um EJBDataSchema
        this.insertInvariant("compositionEJBDataSchemaElementEJBDataSchema", "EJBDataSchemaElement.allInstances().package->forAll(p|p <> nullDS)");

        // Toda EJBFeature precisa ter uma EJBClass
        this.insertInvariant("compositionEJBFeatureEJBClass", "EJBFeature.allInstances().class->forAll(c | c <> nullClassifier)");

        // Toda EJBParameter precisa ter um BusinessMethod
        this.insertInvariant("compositionEJBParameterBusinessMethod", "EJBParameter.allInstances().operation->forAll(o | o <> nullBM)");

        // Toda EJBAssociationEnd precisa que o campo 'composition' esteja preenchido
        this.insertInvariant("restrictionRequiredFieldCompositionEJBAssociationEnd", "EJBAssociationEnd.allInstances()->forAll(ae|ae.composition <> 'null')");

        // Toda EJBClassifier precisa que o campo 'name' esteja preenchido
        this.insertInvariant("restrictionRequiredFieldNameEJBClassifier", "EJBClassifier.allInstances()->forAll(c|c.nameClassifier <> 'null')");

        // Toda EJBTyped precisa que o campo 'name' esteja preenchido
        this.insertInvariant("restrictionRequiredFieldNameEJBTyped", "EJBTyped.allInstances()->forAll(t|t.nameTyped <> 'null')");
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
        this.ieos.insertObject("EJBDataSchemaElement", "nullDSE");
        this.ieos.insertObject("EJBDataSchema", "nullDS");
        this.ieos.insertObject("EJBClassifier", "nullClassifier");
        this.ieos.insertValue("EJBClassifier", "nameClassifier", "nullClassifier", "nullClassifier");
        this.ieos.insertObject("EJBTyped", "nullTyped");
        this.ieos.insertValue("EJBTyped", "nameTyped", "nullTyped", "nullTyped");
        this.ieos.insertObject("BusinessMethod", "nullBM");
        this.ieos.insertValue("BusinessMethod", "nameTyped", "nullBM", "nullBM");
    }

    // Métodos específicos do metamodelo
    public boolean insertEJBDataSchema(String name) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBDataSchema: the security diagram must be created");
            return false;
        }

        try {
            this.ieos.insertObject("EJBDataSchema", name + "EDS");
            this.ieos.insertValue("EJBDataSchema", "nameDataSchema", name + "EDS", name);
        } catch (Exception e) {
            logger.error("Error when insert an EJBDataSchema: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertEJBDataType(String name) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBDataType: the security diagram must be created");
            return false;
        }

        try {
            String myName = null;
            if (name == null) {
                myName = "null";
            } else {
                myName = name;
            }
            this.ieos.insertObject("EJBDataType", myName + "EDT");
            this.ieos.insertValue("EJBDataType", "nameClassifier", myName + "EDT", myName);
        } catch (Exception e) {
            logger.error("Error when insert an EJBDataType: " + e.getMessage());
            return false;
        }
        return true;
    }

    //OK
    public boolean insertEJBKeyClass(String name) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBKeyClass: the security diagram must be created");
            return false;
        }

        try {
            String myName = null;
            if (name == null) {
                myName = "null";
            } else {
                myName = name;
            }

            this.ieos.insertObject("EJBKeyClass", myName);
            this.ieos.insertValue("EJBKeyClass", "nameClassifier", myName, myName);
        } catch (Exception e) {
            logger.error("Error when insert an EJBKeyClass: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertEJBDataClass(String name, String ejbDataSchemaName) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBDataClass: the security diagram must be created");
            return false;
        }
        String dataSchemaName = null;
        if (ejbDataSchemaName == null) {
            dataSchemaName = "nullDS";
        } else {
            dataSchemaName = ejbDataSchemaName;
        }

        try {
            String myName = null;
            if (name == null) {
                myName = "null";
            } else {
                myName = name;
            }
            this.ieos.insertObject("EJBDataClass", myName + "EDC");
            this.ieos.insertValue("EJBDataClass", "nameClassifier", myName + "EDC", myName);

            this.ieos.insertLink("EJBDataSchema", dataSchemaName, "package", "element", myName + "EDC", "EJBDataClass");
        } catch (Exception e) {
            logger.error("Error when insert an EJBDataClass: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertEJBDataAssociation(String name, String ejbDataSchemaName, String... ejbAssociationEnds) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBDataAssociation: the security diagram must be created");
            return false;
        }

        try {
            this.ieos.insertObject("EJBDataAssociation", name + "EDA");

            String dataSchemaName = null;
            if (ejbDataSchemaName == null) {
                dataSchemaName = "nullDS";
            } else {
                dataSchemaName = ejbDataSchemaName;
            }
            this.ieos.insertLink("EJBDataSchema", dataSchemaName, "package", "element", name + "EDA", "EJBDataAssociation");
            for (String ejbAssociationEnd : ejbAssociationEnds) {
                this.ieos.insertLink("EJBDataAssociation", name + "EDA", "association", "associationEnds", ejbAssociationEnd, "EJBAssociationEnd");
            }
        } catch (Exception e) {
            logger.error("Error when insert an EJBDataAssociation: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertEJBSessionComponent(String name) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBSessionComponent: the security diagram must be created");
            return false;
        }

        try {
            String myName = null;
            if (name == null) {
                myName = "null";
            } else {
                myName = name;
            }
            this.ieos.insertObject("EJBSessionComponent", myName + "ESC");
            this.ieos.insertValue("EJBSessionComponent", "nameClassifier", myName + "ESC", myName);
        } catch (Exception e) {
            logger.error("Error when insert an EJBSessionComponent: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertEJBEntityComponent(String name, String... tableNames) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBEntityComponent: the security diagram must be created");
            return false;
        }

        try {
            String myName = null;
            if (name == null) {
                myName = "null";
            } else {
                myName = name;
            }
            this.ieos.insertObject("EJBEntityComponent", myName + "EEC");
            this.ieos.insertValue("EJBEntityComponent", "nameClassifier", myName + "EEC", myName);

            for (String tableName : tableNames) {
                this.ieos.insertLink("EJBEntityComponent", myName + "EEC", "", "usedTable", tableName, "Table");
            }
        } catch (Exception e) {
            logger.error("Error when insert an EJBEntityComponent: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertTable(String name) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a Table: the security diagram must be created");
            return false;
        }

        try {
            this.ieos.insertObject("Table", name + "T");
            this.ieos.insertValue("Table", "nameClassifier", name + "T", name);
        } catch (Exception e) {
            logger.error("Error when insert a Table: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertBusinessMethod(String name, String ejbClassName) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a BusinessMethod: the security diagram must be created");
            return false;
        }

        try {
            String myName = null;
            if (name == null) {
                myName = "null";
            } else {
                myName = name;
            }

            this.ieos.insertObject("BusinessMethod", myName + "BM");
            this.ieos.insertValue("BusinessMethod", "nameTyped", myName + "BM", myName);

            String className = null;
            if (ejbClassName == null) {
                className = "nullClassifier";
            } else {
                className = ejbClassName;
            }
            this.ieos.insertLink("BusinessMethod", myName + "BM", "feature", "class", className, "EJBClass");
        } catch (Exception e) {
            logger.error("Error when insert a BusinessMethod: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertEJBParameter(String name, String businessMethodName) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBParameter: the security diagram must be created");
            return false;
        }

        try {
            this.ieos.insertObject("EJBParameter", name + "EP");
            this.ieos.insertValue("EJBParameter", "nameTyped", name + "EP", name);

            this.ieos.insertLink("BusinessMethod", businessMethodName, "operation", "parameter", name + "EP", "EJBParameter");
        } catch (Exception e) {
            logger.error("Error when insert an EJBParameter: " + e.getMessage());
            return false;
        }
        return true;
    }

    //OK
    public boolean insertEJBAttribute(String name, String visibility, String type, String ejbClassName) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBAttribute: the security diagram must be created");
            return false;
        }

        try {
            String myName = null;
            if (name == null) {
                myName = "null";
            } else {
                myName = name;
            }

            this.ieos.insertObject("EJBAttribute", myName);
            this.ieos.insertValue("EJBAttribute", "nameTyped", myName, myName);
            this.ieos.insertValue("EJBAttribute", "visibility", myName, visibility);
            this.ieos.insertValue("EJBAttribute", "type", myName, type);

            String className = null;
            if (ejbClassName == null) {
                className = "nullClassifier";
            } else {
                className = ejbClassName;
            }
            this.ieos.insertLink("EJBAttribute", myName, "feature", "class", className, "EJBClass");
        } catch (Exception e) {
            logger.error("Error when insert an EJBAttribute: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertEJBAssociationEnd(String name, String lower, String upper, Boolean composition, String ejbClassName) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBAssociationEnd: the security diagram must be created");
            return false;
        }

        try {
            String myName = null;
            if (name == null) {
                myName = "null";
            } else {
                myName = name;
            }

            this.ieos.insertObject("EJBAssociationEnd", myName + "EAE");
            this.ieos.insertValue("EJBAssociationEnd", "nameTyped", myName + "EAE", name);
            this.ieos.insertValue("EJBAssociationEnd", "lower", myName + "EAE", lower);
            this.ieos.insertValue("EJBAssociationEnd", "upper", myName + "EAE", upper);
            String compositionString = null;

            if (composition == null) {
                compositionString = "null";
            } else if (composition) {
                compositionString = "true";
            } else {
                compositionString = "false";
            }
            this.ieos.insertValue("EJBAssociationEnd", "composition", myName + "EAE", compositionString);

            String className = null;
            if (ejbClassName == null) {
                className = "nullClassifier";
            } else {
                className = ejbClassName;
            }
            this.ieos.insertLink("EJBAssociationEnd", myName + "EAE", "feature", "class", className, "EJBClass");
        } catch (Exception e) {
            logger.error("Error when insert an EJBAssociationEnd: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertEJBServingAttribute(String name, String lower, String upper, Boolean composition, String ejbClassName) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBServingAttribute: the security diagram must be created");
            return false;
        }

        try {
            String myName = null;
            if (name == null) {
                myName = "null";
            } else {
                myName = name;
            }

            this.ieos.insertObject("EJBServingAttribute", myName + "ESA");
            this.ieos.insertValue("EJBServingAttribute", "nameTyped", myName + "ESA", myName);
            this.ieos.insertValue("EJBServingAttribute", "lower", myName + "ESA", lower);
            this.ieos.insertValue("EJBServingAttribute", "upper", myName + "ESA", upper);

            String compositionString = null;
            if (composition == null) {
                compositionString = "null";
            } else if (composition) {
                compositionString = "true";
            } else {
                compositionString = "false";
            }
            this.ieos.insertValue("EJBServingAttribute", "composition", myName + "ESA", compositionString);

            String className = null;
            if (ejbClassName == null) {
                className = "nullClassifier";
            } else {
                className = ejbClassName;
            }
            this.ieos.insertLink("EJBServingAttribute", myName + "ESA", "feature", "class", className, "EJBClass");
        } catch (Exception e) {
            logger.error("Error when insert an EJBServingAttribute: " + e.getMessage());
            return false;
        }
        return true;
    }
}
