package br.uff.ic.transformador.core;

import core.XEOS;

public class AnalisadorUmlTeste extends AnalisadorUml {

    public AnalisadorUmlTeste(XEOS ieos) {
        super(ieos);
    }

    @Override
    protected boolean insertMetamodelOperations() throws Exception {
        logger.debug("Inserting Uml MetaModel Operations");
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
    protected void insertMetamodelStructure() throws Exception {
        logger.debug("Inserting Uml MetaModel Structure");

        this.ieos.insertClass("Classifier");
        this.ieos.insertClass("Typed");
//        this.ieos.insertClass("DataType");
//        this.ieos.insertClass("Set");
        this.ieos.insertClass("Class");
//        this.ieos.insertClass("AssociationClass");
        this.ieos.insertClass("Feature");
        this.ieos.insertClass("AssociationEnd");
        this.ieos.insertClass("Association");
//        this.ieos.insertClass("Attribute");
//        this.ieos.insertClass("Operation");
//        this.ieos.insertClass("Parameter");

//        this.ieos.insertGeneralization("DataType", "Classifier");
//        this.ieos.insertGeneralization("Set", "DataType");
//        this.ieos.insertGeneralization("Class", "Classifier");
//        this.ieos.insertGeneralization("AssociationClass", "Classifier");
        this.ieos.insertGeneralization("Feature", "Typed");
        this.ieos.insertGeneralization("AssociationEnd", "Feature");
//        this.ieos.insertGeneralization("Attribute", "Feature");
//        this.ieos.insertGeneralization("Operation", "Feature");
//        this.ieos.insertGeneralization("Parameter", "Typed");

//        this.ieos.insertAssociation("Set", "set", "0..*", "1", "elementType", "Classifier");
//        this.ieos.insertAssociation("Classifier", "class", "0..1", "*", "types", "Typed");
//        this.ieos.insertAssociation("AssociationEnd", "otherEnd", "1..*", "1..*", "others", "AssociationEnd");
        this.ieos.insertAssociation("AssociationEnd", "end", "0..*", "1", "association", "Association");
        // Agregações (colocar invariantes) [Class <>- Feature][Operation <>- Parameter]
//        this.ieos.insertAssociation("Class", "class", "0..1", "*", "feature", "Feature");
//        this.ieos.insertAssociation("AssociationClass", "associationClass", "0..1", "*", "feature", "Feature");
//        this.ieos.insertAssociation("Operation", "operation", "1", "*", "parameter", "Parameter");
        // Fim agregações

        this.ieos.insertAttribute("Classifier", "nameClassifier", "String");
        this.ieos.insertAttribute("Typed", "nameTyped", "String");
//        this.ieos.insertAttribute("Feature", "visibility", "String"); // ### Tipo VisibilityKind
//        this.ieos.insertAttribute("AssociationEnd", "lower", "String"); // ### Tipo Lowerbound
//        this.ieos.insertAttribute("AssociationEnd", "upper", "String"); // ### Tipo Upperbound
//        this.ieos.insertAttribute("AssociationEnd", "composition", "Boolean");
//        this.ieos.insertAttribute("Attribute", "type", "String");
//        this.ieos.insertAttribute("Operation", "returnType", "String");

    }

    // Metodos exclusivos deste metamodelo
    @Override
    public boolean insertOperationOCL(String contextClass, String nameOperation,
            String returnType, String bodyOperation, Object[] params) {
        boolean result;
//        try {
//            result = this.ieos.insertOperation(contextClass, nameOperation, returnType, bodyOperation, params);
//        } catch (XEOSException e) {
//            logger.error("Error inserting operation: " + e.getMessage());
//            logger.error(e.toString());
//            result = false;
//        }
//        return result;
        return true;

    }

    @Override
    public boolean insertClass(String name) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a class: the UML diagram must be created");
            return false;
        }
        if (name == null) {
            name = "null";
        }
        try {
            // Criando o objeto
            this.ieos.insertObject("Class", name);
            // Colocando o nome (Classifier) do objeto no campo 'name'
            this.ieos.insertValue("Class", "nameClassifier", name, name);
        } catch (Exception e) {
            logger.error("Error when insert a class: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean insertAssociationClass(String name) {
//        if (this.ieos.getActualState() != 3) {
//            logger.error("Error when insert an association class: the UML diagram must be created");
//            return false;
//        }
//        if (name == null) {
//            name = "null";
//        }
//        try {
//            // Criando o objeto
//            this.ieos.insertObject("AssociationClass", name);
//            // Colocando o nome (Classifier) do objeto no campo 'name'
//            this.ieos.insertValue("AssociationClass", "nameClassifier", name, name);
//        } catch (Exception e) {
//            logger.error("Error when insert an association class: " + e.getMessage());
//            return false;
//        }
        return true;
    }

    @Override
    public boolean insertSet(String name, String elementType) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a set: the UML diagram must be created");
            return false;
        }
////        if (name == null) {
////            name = "OCLVoid";
////        }
////        if (elementType == null) {
////            elementType = "OCLVoid";
////        }
//        try {
//            // Criando o objeto
//            this.ieos.insertObject("Set", name);
//            // Colocando o nome (Classifier) do objeto no campo 'name'
//            this.ieos.insertValue("Set", "nameClassifier", name, name);
//            // Colocando o link com o Classifier correto
//            this.ieos.insertLink("Set", name, "", "elementType", elementType, "Classifier");
//        } catch (Exception e) {
//            logger.error("Error when insert an association class: " + e.getMessage());
//            return false;
//        }
        return true;
    }

    @Override
    public boolean insertAttribute(String name, String visibility, String type, String className, boolean isClass) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an attribute: the UML diagram must be created");
            return false;
        }
//        if (type == null) {
//            type = "null";
//        }
//        try {
//            // Criando o objeto
//            this.ieos.insertObject("Attribute", name);
//            // Colocando o nome (Typed) do objeto no campo 'name'
//            this.ieos.insertValue("Attribute", "nameTyped", name, type);
//            // Colocando a visibilidade (Feature) do objeto no campo 'visibility'
//            this.ieos.insertValue("Attribute", "visibility", name, visibility);
////             // Colocando o tipo do objeto no campo 'type'
//            this.ieos.insertValue("Attribute", "type", name, type);
//
//            // Faz a relação (composição) deste objeto com uma classe ou classe associativa
//            if (isClass) {
//                this.ieos.insertLink("Attribute", name, "feature", "class", className, "Class");
//                //Novo
//                this.ieos.insertLink("Attribute", name, "feature", "associationClass", "nullAC", "AssociationClass");
////                this.ieos.insertLink("Attribute", name, "feature", "associationClass", "null", "OCLVoid");
//            } else {
//                this.ieos.insertLink("Attribute", name, "feature", "associationClass", className, "AssociationClass");
//                //Novo
//                this.ieos.insertLink("Attribute", name, "feature", "class", "nullC", "Class");
////                this.ieos.insertLink("Attribute", name, "feature", "class", "null", "OCLVoid");
//            }
//        } catch (Exception e) {
//            logger.error("Error when insert an operation: " + e.getMessage());
//            return false;
//        }
        return true;
    }

    @Override
    public boolean insertOperation(String name, String visibility, String returnType, String className, boolean isClass) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an operation: the UML diagram must be created");
            return false;
        }
//        if (returnType == null) {
//            returnType = "null";
//        }
//        try {
//            // Criando o objeto
//            this.ieos.insertObject("Operation", name);
//            // Colocando o nome (Typed) do objeto no campo 'name'
//            this.ieos.insertValue("Operation", "nameTyped", name, returnType);
//            // Colocando a visibilidade (Feature) do objeto no campo 'visibility'
//            this.ieos.insertValue("Operation", "visibility", name, visibility);
////             // Colocando o tipo de retorno do objeto no campo 'returnType'
//            this.ieos.insertValue("Operation", "returnType", name, returnType);
//
//            // Faz a relação (composição) deste objeto com uma classe ou classe associativa
//            if (isClass) {
//                this.ieos.insertLink("Operation", name, "feature", "class", className, "Class");
//                //Novo
//                this.ieos.insertLink("Operation", name, "feature", "associationClass", "nullAC", "AssociationClass");
////                this.ieos.insertLink("Operation", name, "feature", "associationClass", "null", "OCLVoid");
//            } else {
//                this.ieos.insertLink("Operation", name, "feature", "associationClass", className, "AssociationClass");
//                //Novo
//                this.ieos.insertLink("Operation", name, "feature", "class", "nullC", "Class");
////                this.ieos.insertLink("Operation", name, "feature", "class", "null", "OCLVoid");
//            }
//        } catch (Exception e) {
//            logger.error("Error when insert an operation: " + e.getMessage());
//            return false;
//        }
        return true;
    }

    @Override
    public boolean insertParameter(String name, String type, String operationName) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a parameter: the UML diagram must be created");
            return false;
        }
////        if (name == null) {
////            name = "OCLVoid";
////        }
////        if (type == null) {
////            type = "OCLVoid";
////        }
////        if (operationName == null) {
////            operationName = "OCLVoid";
////        }
//        try {
//            // Criando o objeto
//            this.ieos.insertObject("Parameter", name);
//            // Colocando o nome (Typed) do objeto no campo 'name'
//            this.ieos.insertValue("Parameter", "nameTyped", name, type);
//            // Faz a relação (composição) deste objeto com uma operacao
//            this.ieos.insertLink("Parameter", name, "parameter", "operation", operationName, "Operation");
//        } catch (Exception e) {
//            logger.error("Error when insert a parameter: " + e.getMessage());
//            return false;
//        }
        return true;
    }

    @Override
    public boolean insertAssociationEnd(String name, String visibility, String type, String lower, String upper, Boolean composition, String className, boolean isClass) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an associationEnd: the UML diagram must be created");
            return false;
        }
        try {
            // Criando o objeto
            this.ieos.insertObject("AssociationEnd", name);
            // Colocando o nome (Typed) do objeto no campo 'name'
            this.ieos.insertValue("AssociationEnd", "nameTyped", name, (type == null ? "OCLVoid" : type));
//            // Colocando a visibilidade (Feature) do objeto no campo 'visibility'
//            this.ieos.insertValue("AssociationEnd", "visibility", name, visibility);
//            // Colocando o campo 'lower' do objeto
//            this.ieos.insertValue("AssociationEnd", "lower", name, lower);
//            // Colocando o campo 'upper' do objeto
//            this.ieos.insertValue("AssociationEnd", "upper", name, upper);
//            // Colocando o campo 'composition' do objeto
//            String compositionString = null;
//            if (composition) {
//                compositionString = "true";
//            } else {
//                compositionString = "false";
//            }
//            this.ieos.insertValue("AssociationEnd", "composition", name, compositionString);
//
//            // Faz a relação (composição) deste objeto com uma classe ou classe associativa
//            if (isClass) {
//                this.ieos.insertLink("AssociationEnd", name, "feature", "class", className, "Class");
//                //Novo
//                this.ieos.insertLink("AssociationEnd", name, "feature", "associationClass", "nullAC", "AssociationClass");
////                this.ieos.insertLink("AssociationEnd", name, "feature", "associationClass", "null", "OCLVoid");
//            } else {
//                this.ieos.insertLink("AssociationEnd", name, "feature", "associationClass", className, "AssociationClass");
//                //Novo
//                this.ieos.insertLink("AssociationEnd", name, "feature", "class", "nullC", "Class");
////                this.ieos.insertLink("AssociationEnd", name, "feature", "class", "null", "OCLVoid");
//            }
        } catch (Exception e) {
            logger.error("Error when insert an associationEnd: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean insertAssociation(String name, String... associationEndsName) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an association: the UML diagram must be created");
            return false;
        }
//        if (name == null) {
//            name = "OCLVoid";
//        }
        try {
            if (associationEndsName.length < 2) {
                throw new Exception("Association can't have less than 2 AssociationEnd");
            }

            // Criando o objeto
            this.ieos.insertObject("Association", name);

            // Faz a relação entre este objeto e os fins de associação, que tem o minimo de 2
            for (String associationEndName : associationEndsName) {
//                this.ieos.insertLink("Association", name, "association", "end", associationEndName, "AssociationEnd");
                this.ieos.insertLink("AssociationEnd", associationEndName, "end", "association", name, "Association");
            }
        } catch (Exception e) {
            logger.error("Error when insert an association: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean insertLinksBetweenAssociationEnds(String associationEnd, String otherEnd) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a link between associationEnds: the UML diagram must be created");
            return false;
        }
//        if (associationEnd == null) {
//            associationEnd = "OCLVoid";
//        }
//        if (otherEnd == null) {
//            otherEnd = "OCLVoid";
//        }
        try {
            this.ieos.insertLink("AssociationEnd", associationEnd, "others", "otherEnd", otherEnd, "AssociationEnd");
//            this.ieos.insertLink("AssociationEnd", otherEnd, "", "otherEnd", associationEnd, "AssociationEnd");
        } catch (Exception e) {
            logger.error("Error when insert a link between associationEnds: " + e.getMessage());
            return false;
        }
        return true;
    }
}