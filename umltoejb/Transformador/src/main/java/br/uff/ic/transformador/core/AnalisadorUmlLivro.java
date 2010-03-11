package br.uff.ic.transformador.core;

import core.XEOS;
import core.exception.XEOSException;

public class AnalisadorUmlLivro extends Analisador {

    private int count;

    public AnalisadorUmlLivro(XEOS ieos) {
        super(ieos);
        count = 0;
    }

    @Override
    public void createSpecificationOfCurrentDiagram() throws Exception {
        this.ieos.insertObject("Class", "nullC");
        this.ieos.insertValue("Class", "name", "nullC", "nullC");
    }

    @Override
    protected void insertMetamodelInvariants() throws Exception {
        logger.debug("Inserting Uml MetaModel Invariants");

//        // Classes associativas não podem ter um AssociationEnd vinculado a ela que possua composition = true
//        this.insertInvariant("notExistsCompositionAssociationEndAssignedToAssociationClass",
//                "AssociationClass.allInstances()->forAll(ac|ac.feature->select(f|f.oclIsTypeOf(AssociationEnd))->forAll(ae | ae.oclAsType(AssociationEnd).composition = false))");
//
//        // Restricao da composição (Class e AssociationClass)
//        this.insertInvariant("restrictionFeaturehasClassorAssociationClass",
//                "Feature.allInstances()->forAll(f|f.class = nullC xor f.associationClass = nullAC)");
//
//        // Attribute precisa tem p campo 'type' preenchido
//        this.insertInvariant("restrictionRequiredFieldTypeAttribute",
//                "Attribute.allInstances()->forAll(a|a.type <> 'null')");
//
//        // Operation precisa tem p campo 'returnType' preenchido
//        this.insertInvariant("restrictionRequiredFieldReturnTypeOperation",
//                "Operation.allInstances()->forAll(a|a.returnType <> 'null')");
//
//        // Typed precisa de 'name' preenchido
//        this.insertInvariant("restrictionRequiredFieldNameTyped",
//                "Typed.allInstances()->forAll(t|t.nameTyped <> 'null')");
//
//        // Classifier precisa de 'name' preenchido
//        this.insertInvariant("restrictionRequiredFieldNameClassifier",
//                "Classifier.allInstances()->forAll(c|c.nameClassifier <> 'null')");
    }

    @Override
    protected boolean insertMetamodelOperations() throws Exception {
        logger.debug("Inserting Uml MetaModel Operations");
        boolean result = true;

        Object[] params;
        String[] param;

        if (!result) {
            throw new Exception("It was not possible to insert all the operations in the SecureUML model");
        }

        return result;
    }

    @Override
    protected void insertMetamodelStructure() throws Exception {
        logger.debug("Inserting Uml MetaModel Structure");

        this.ieos.insertClass("ModelElement");
        this.ieos.insertClass("Classifier");
        this.ieos.insertClass("Typed");
        this.ieos.insertClass("DataType");
        this.ieos.insertClass("MySet");
        this.ieos.insertClass("Class");
        this.ieos.insertClass("Interface");
        this.ieos.insertClass("AssociationClass");
        this.ieos.insertClass("Feature");
        this.ieos.insertClass("AssociationEnd");
        this.ieos.insertClass("Association");
        this.ieos.insertClass("Attribute");
        this.ieos.insertClass("Operation");
        this.ieos.insertClass("Parameter");

        this.ieos.insertGeneralization("Association", "ModelElement");
        this.ieos.insertGeneralization("Classifier", "ModelElement");
        this.ieos.insertGeneralization("Typed", "ModelElement");
        this.ieos.insertGeneralization("DataType", "Classifier");
        this.ieos.insertGeneralization("MySet", "DataType");
        this.ieos.insertGeneralization("Class", "Classifier");
        this.ieos.insertGeneralization("Interface", "Classifier");
        this.ieos.insertGeneralization("AssociationClass", "Class");
        this.ieos.insertGeneralization("AssociationClass", "Association");
        this.ieos.insertGeneralization("Feature", "Typed");
        this.ieos.insertGeneralization("AssociationEnd", "Feature");
        this.ieos.insertGeneralization("Attribute", "Feature");
        this.ieos.insertGeneralization("Operation", "Feature");
        this.ieos.insertGeneralization("Parameter", "Typed");

        this.ieos.insertAssociation("MySet", "setA", "0..*", "1", "elementType", "Classifier");
        this.ieos.insertAssociation("Classifier", "classifier", "0..1", "*", "types", "Typed");
        this.ieos.insertAssociation("Class", "classes", "*", "*", "implementedInterfaces", "Interface");
        this.ieos.insertAssociation("AssociationEnd", "otherEnd", "1..*", "1..*", "others", "AssociationEnd");
        this.ieos.insertAssociation("AssociationEnd", "associationEnds", "2..*", "1", "association", "Association");
        // Agregações (colocar invariantes) [Class <>- Feature][Operation <>- Parameter]
        this.ieos.insertAssociation("Class", "class", "0..1", "*", "feature", "Feature");
        this.ieos.insertAssociation("Operation", "operation", "1", "*", "parameter", "Parameter");
        // Fim agregações

        this.ieos.insertAttribute("ModelElement", "name", "String");
        this.ieos.insertAttribute("Feature", "visibility", "String"); // ### Tipo VisibilityKind
        this.ieos.insertAttribute("AssociationEnd", "lower", "String"); // ### Tipo Lowerbound
        this.ieos.insertAttribute("AssociationEnd", "upper", "String"); // ### Tipo Upperbound
        this.ieos.insertAttribute("AssociationEnd", "composition", "Boolean");
        this.ieos.insertAttribute("Attribute", "type", "String");
        this.ieos.insertAttribute("Operation", "returnType", "String");

    }

    // Metodos exclusivos deste metamodelo
    public boolean insertOperationOCL(String contextClass, String nameOperation,
            String returnType, String bodyOperation, Object[] params) {
        boolean result;
        try {
            result = this.ieos.insertOperation(contextClass, nameOperation, returnType, bodyOperation, params);
        } catch (XEOSException e) {
            logger.error("Error inserting operation: " + e.getMessage());
            logger.error(e.toString());
            result = false;
        }
        return result;

    }

    public boolean insertClass(String name) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a class: the UML diagram must be created");
            return false;
        }
        try {
            // Criando o objeto
            this.ieos.insertObject("Class", name);
            // Colocando o nome (Classifier) do objeto no campo 'name'
            this.ieos.insertValue("Class", "name", name, name);
        } catch (Exception e) {
            logger.error("Error when insert a class: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertAssociationClass(String name, String ... ends) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an association class: the UML diagram must be created");
            return false;
        }
        try {
            // Criando o objeto
            this.ieos.insertObject("AssociationClass", name);
            // Colocando o nome (Classifier) do objeto no campo 'name'
            this.ieos.insertValue("AssociationClass", "name", name, name);

            for (String end : ends) {
                this.ieos.insertLink("AssociationEnd", end, "associationEnds", "association", name, "AssociationClass");
            }
        } catch (Exception e) {
            logger.error("Error when insert an association class: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertSet(String name, String elementType) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a set: the UML diagram must be created");
            return false;
        }
        try {
            // Criando o objeto
            this.ieos.insertObject("MySet", name);
            // Colocando o nome (Classifier) do objeto no campo 'name'
            this.ieos.insertValue("MySet", "name", name, name);
            // Colocando o link com o Classifier correto
            this.ieos.insertLink("MySet", name, "setA", "elementType", elementType, "Classifier");
        } catch (Exception e) {
            logger.error("Error when insert an association class: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertAttribute(String name, String visibility, String type, String className, boolean isClass) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an attribute: the UML diagram must be created");
            return false;
        }
        try {
            // Criando o objeto
            this.ieos.insertObject("Attribute", name);
            // Colocando o nome (Typed) do objeto no campo 'name'
            this.ieos.insertValue("Attribute", "name", name, type);
            // Colocando a visibilidade (Feature) do objeto no campo 'visibility'
            this.ieos.insertValue("Attribute", "visibility", name, visibility);
             // Colocando o tipo do objeto no campo 'type'
            this.ieos.insertValue("Attribute", "type", name, type);

            this.ieos.insertLink("Attribute", name, "feature", "class", className, "Class");

        } catch (Exception e) {
            logger.error("Error when insert an operation: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertOperation(String name, String visibility, String returnType, String className, boolean isClass) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an operation: the UML diagram must be created");
            return false;
        }
        try {
            // Criando o objeto
            this.ieos.insertObject("Operation", name);
            // Colocando o nome (Typed) do objeto no campo 'name'
            this.ieos.insertValue("Operation", "name", name, returnType);
            // Colocando a visibilidade (Feature) do objeto no campo 'visibility'
            this.ieos.insertValue("Operation", "visibility", name, visibility);
             // Colocando o tipo de retorno do objeto no campo 'returnType'
            this.ieos.insertValue("Operation", "returnType", name, returnType);

            this.ieos.insertLink("Operation", name, "feature", "class", className, "Class");

        } catch (Exception e) {
            logger.error("Error when insert an operation: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertParameter(String name, String type, String operationName) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a parameter: the UML diagram must be created");
            return false;
        }
        try {
            // Criando o objeto
            this.ieos.insertObject("Parameter", name);
            // Colocando o nome (Typed) do objeto no campo 'name'
            this.ieos.insertValue("Parameter", "name", name, type);
            // Faz a relação (composição) deste objeto com uma operacao
            this.ieos.insertLink("Parameter", name, "parameter", "operation", operationName, "Operation");

        } catch (Exception e) {
            logger.error("Error when insert a parameter: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertAssociationEnd(String name, String visibility, String type, String lower, String upper, Boolean composition, String className, boolean isClass) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an associationEnd: the UML diagram must be created");
            return false;
        }
        try {
            // Criando o objeto
            this.ieos.insertObject("AssociationEnd", name);
            // Colocando o nome (Typed) do objeto no campo 'name'
            this.ieos.insertValue("AssociationEnd", "name", name, name);
            
            this.ieos.insertLink("AssociationEnd", name, "types", "classifier", type, "Classifier");

            // Colocando a visibilidade (Feature) do objeto no campo 'visibility'
            this.ieos.insertValue("AssociationEnd", "visibility", name, visibility);
            // Colocando o campo 'lower' do objeto
            this.ieos.insertValue("AssociationEnd", "lower", name, lower);
            // Colocando o campo 'upper' do objeto
            this.ieos.insertValue("AssociationEnd", "upper", name, upper);
            // Colocando o campo 'composition' do objeto
            String compositionString = null;
            if (composition) {
                compositionString = "true";
            } else {
                compositionString = "false";
            }
            this.ieos.insertValue("AssociationEnd", "composition", name, compositionString);

            this.ieos.insertLink("AssociationEnd", name, "feature", "class", className, "Class");

        } catch (Exception e) {
            logger.error("Error when insert an associationEnd: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertAssociation(String name, String... associationEndsName) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an association: the UML diagram must be created");
            return false;
        }
        try {
            if (associationEndsName.length < 2) {
                throw new Exception("Association can't have less than 2 AssociationEnd");
            }

            // Criando o objeto
            this.ieos.insertObject("Association", name);

            // Faz a relação entre este objeto e os fins de associação, que tem o minimo de 2
            for (String associationEndName : associationEndsName) {
                this.ieos.insertLink("AssociationEnd", associationEndName, "associationEnds", "association", name, "Association");
            }
        } catch (Exception e) {
            logger.error("Error when insert an association: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertLinksBetweenAssociationEnds(String associationEnd, String otherEnd) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a link between associationEnds: the UML diagram must be created");
            return false;
        }
        try {
            this.ieos.insertLink("AssociationEnd", associationEnd, "others", "otherEnd", otherEnd, "AssociationEnd");
        } catch (Exception e) {
            logger.error("Error when insert a link between associationEnds: " + e.getMessage());
            return false;
        }
        return true;
    }
}