package br.uff.ic.mda.transformador;

import core.XEOS;
import core.exception.XEOSException;

public class DominioUml extends Dominio {

    private int count;

    public DominioUml(XEOS ieos) {
        super(ieos);
        count = 0;
    }

    @Override
    public void createSpecificationOfCurrentDiagram() throws Exception {
        this.ieos.insertObject("Classifier", "NULL_CLASSIFIER");
        this.ieos.insertObject("Class", "NULL_CLASS");
        this.ieos.insertObject("Operation", "NULL_OPERATION");
        this.ieos.insertObject("Association", "NULL_ASSOCIATION");

        this.ieos.insertObject("DataType", "Integer");
        this.ieos.insertValue("DataType", "name", "Integer", "Integer");
        this.ieos.insertObject("DataType", "Double");
        this.ieos.insertValue("DataType", "name", "Double", "Double");
        this.ieos.insertObject("DataType", "Real");
        this.ieos.insertValue("DataType", "name", "Real", "Real");
        this.ieos.insertObject("DataType", "String");
        this.ieos.insertValue("DataType", "name", "String", "String");
        this.ieos.insertObject("DataType", "Date");
        this.ieos.insertValue("DataType", "name", "Date", "Date");
        this.ieos.insertObject("DataType", "Boolean");
        this.ieos.insertValue("DataType", "name", "Boolean", "Boolean");
    }

    @Override
    protected void insertMetamodelInvariants() throws Exception {
        logger.debug("Inserting Uml MetaModel Invariants");

        // Classes associativas nao podem ter um AssociationEnd vinculado a ela (devido a ser uma Association) que possua composition = true
        this.insertInvariant("notExistsCompositionAssociationEndAssignedToAssociationClass",
                "AssociationClass.allInstances()->forAll(ac : AssociationClass | ac.associationEnds->forAll(ae : AssociationEnd | ae.composition = false))");

        /*  VER NA DOCUMENTACAO DO UML NO SITE DA OMG  */
        // Classes associativas nao podem estar associadas a um AssociationEnd (devido a ser uma Class)
        this.insertInvariant("associationClassCannotBeTypedByAssociationEnd",
                "AssociationEnd.allInstances()->forAll(ae : AssociationEnd | ae.classifier->forAll(c : Classifier | not c.oclIsKindOf(AssociationClass)))");

        // ModelElement precisa de 'name' preenchido, exceto Association  e AssociationEnd
        this.insertInvariant("restrictionRequiredFieldNameToModelElement",
                //                "ModelElement.allInstances()->forAll(me : ModelElement | me.name <> '')");
                "ModelElement.allInstances()->forAll(me : ModelElement | me.name <> '' or me.oclIsKindOf(Association) or me.oclIsKindOf(AssociationEnd))");

        // Todo Conjunto precisa ter um Classifier
        this.insertInvariant("restrictionRequiredAssociationConjuntoToClassifier",
                "Conjunto.allInstances()->forAll(c : Conjunto | c.elementType <> NULL_CLASSIFIER)");

        // Todo Feature precisa ter uma Class
        this.insertInvariant("restrictionRequiredAssociationFeatureToClass",
                "Feature.allInstances()->forAll( f : Feature | not f.class->includes(NULL_CLASS))");

        // Todo Parameter precisa ter uma Operation
        this.insertInvariant("restrictionRequiredAssociationParameterToOperation",
                "Parameter.allInstances()->forAll(p : Parameter | not p.operation->includes(NULL_OPERATION))");

        // Todo AssociationEnd precisa ter uma Association
        this.insertInvariant("restrictionRequiredAssociationAssociationEndToAssociation",
                //                "AssociationEnd.allInstances()->forAll(ae : AssociationEnd | not ae.association->includes(NULL_ASSOCIATION))");
                "AssociationEnd.allInstances().association->forAll(a : Association | a <> NULL_ASSOCIATION)");

        // Todo Association precisa ter, no minimo, 1 AssociationEnd
        this.insertInvariant("restrictionRequiredMinimunTwoAssociationEndPerPerson",
                "Association.allInstances()->excluding(NULL_ASSOCIATION)->forAll( a : Association | a.associationEnds->size() >= 1)");

    }

    @Override
    protected boolean insertMetamodelOperations() throws Exception {
        logger.debug("Inserting Uml MetaModel Operations");
        boolean result = true;

        Object[] params;
        String[] param;

        result &= this.ieos.insertOperation("Class", "getAllContained", "Set(Class)",
            "if contained->includes(self) then " +
                "contained->asSet() " +
            "else " +
                "self.feature" +
                    "->select(f : Feature | f.oclIsKindOf(AssociationEnd))" +
                    "->collect(f : Feature | f.oclAsType(AssociationEnd))" +
                    "->select(ae : AssociationEnd | ae.composition = true)" +
                    "->collect(ae : AssociationEnd | ae.classifier)" +
                    "->select(c : Classifier | c.oclIsKindOf(Class))" +
                    "->collect(c : Classifier| c.oclAsType(Class))" +
                    "->iterate( cc : Class ; acc : Set(Class) = allContained " +
                        "| allContained->union(cc.getAllContained(allContained, allContained->including(cc))))" +
            " endif "
            , new Object[]{new String[]{"contained","Set(Class)"}, new String[]{"allContained","Set(Class)"}});

        result &= this.ieos.insertOperation("Class", "emptySet", "Set(Class)",
            "Class.allInstances()->select(c|false) ", new Object[0]);

        result &= this.ieos.insertOperation("Class", "getOuterMostContainer", "Class",
                "if " +
                    "self.feature" +
                        "->select(f : Feature | f.oclIsKindOf(AssociationEnd))" +
                        "->collect(f : Feature | f.oclAsType(AssociationEnd))" +
                        "->exists(ae : AssociationEnd | ae.otherEnd->exists(oe : AssociationEnd | oe.composition = true))" +
                " then " +
                    "self.feature" +
                        "->select(f : Feature | f.oclIsKindOf(AssociationEnd))" +
                        "->collect(f : Feature | f.oclAsType(AssociationEnd))" +
                        "->select(ae : AssociationEnd | ae.otherEnd->exists(oe : AssociationEnd | oe.composition = true))" +
                        ".classifier" +
                        "->select(c : Classifier | c.oclIsKindOf(Class))" +
                        "->collect(c : Classifier | c.oclAsType(Class))" +
                        "->asOrderedSet()" +
                        "->first()" +
                        ".getOuterMostContainer()" +
                " else " +
                    "self" +
                " endif"
                    , new Object[0]);

        result &= this.ieos.insertOperation("Association", "getOuterMostContainerFromAssociation", "Class",
                "self.associationEnds" +
                    "->select(ae : AssociationEnd | ae.otherEnd->exists(oe : AssociationEnd | oe.composition = true))" +
                    "->asOrderedSet()" +
                    "->first()" +
                    ".classifier" +
                    "->select(c : Classifier | c.oclIsKindOf(Class))" +
                    "->collect(c : Classifier | c.oclAsType(Class))" +
                    "->asOrderedSet()" +
                    "->first()" +
                    ".getOuterMostContainer()"
                        , new Object[0]);

        result &= this.ieos.insertOperation("AssociationClass", "getOuterMostContainerFromAssociationClass", "Class",
                "if " +
                    "self.associationEnds->size() = 1 " +
                "then " +
                    "self.associationEnds" +
                        "->asOrderedSet()" +
                        "->first()" +
                        ".class" +
                        ".getOuterMostContainer()" +
                        "->asOrderedSet()" +
                        "->first() " +
                "else " +
                    "self " +
                "endif", new Object[0]);

        if (!result) {
            throw new Exception("It was not possible to insert all the operations in the UML model");
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
        this.ieos.insertClass("Conjunto");
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
        this.ieos.insertGeneralization("Conjunto", "DataType");
        this.ieos.insertGeneralization("Class", "Classifier");
        this.ieos.insertGeneralization("Interface", "Classifier");
        this.ieos.insertGeneralization("AssociationClass", "Class");
        this.ieos.insertGeneralization("AssociationClass", "Association");
        this.ieos.insertGeneralization("Feature", "Typed");
        this.ieos.insertGeneralization("AssociationEnd", "Feature");
        this.ieos.insertGeneralization("Attribute", "Feature");
        this.ieos.insertGeneralization("Operation", "Feature");
        this.ieos.insertGeneralization("Parameter", "Typed");

        // Pesquisar com calma o erro das ordens!
        this.ieos.insertAssociation("AssociationEnd", "otherEnd", "1..*", "1..*", "others", "AssociationEnd");
        this.ieos.insertAssociation("AssociationEnd", "associationEnds", "1..*", "1", "association", "Association");
        this.ieos.insertAssociation("Operation", "operation", "1", "*", "parameter", "Parameter");
        this.ieos.insertAssociation("Class", "class", "0..1", "*", "feature", "Feature");
        this.ieos.insertAssociation("Class", "classes", "*", "*", "implementedInterfaces", "Interface");
        this.ieos.insertAssociation("Classifier", "classifier", "0..1", "*", "types", "Typed");
        this.ieos.insertAssociation("Conjunto", "setA", "0..*", "1", "elementType", "Classifier");

        this.ieos.insertAttribute("ModelElement", "name", "String");
        this.ieos.insertAttribute("Feature", "visibility", "String"); // ### Tipo VisibilityKind
        this.ieos.insertAttribute("AssociationEnd", "lower", "String"); // ### Tipo Lowerbound
        this.ieos.insertAttribute("AssociationEnd", "upper", "String"); // ### Tipo Upperbound
        this.ieos.insertAttribute("AssociationEnd", "composition", "Boolean");
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

    public boolean insertClass(String id, String name) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a class: the UML diagram must be created");
            return false;
        }
        try {
            // Criando o objeto
            this.ieos.insertObject("Class", id);
            // Colocando o nome (Classifier) do objeto no campo 'name'
            this.ieos.insertValue("Class", "name", id, name == null ? "" : name);
        } catch (Exception e) {
            logger.error("Error when insert a class: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertAssociationClass(String id, String name, String... ends) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an association class: the UML diagram must be created");
            return false;
        }
        try {
            // Criando o objeto
            this.ieos.insertObject("AssociationClass", id);
            // Colocando o nome (Classifier) do objeto no campo 'name'
            this.ieos.insertValue("AssociationClass", "name", id, name == null ? "" : name);

            for (String end : ends) {
                this.ieos.insertLink("AssociationEnd", end, "associationEnds", "association", id, "AssociationClass");
            }
        } catch (Exception e) {
            logger.error("Error when insert an association class: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertSet(String id, String name, String elementType) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a set: the UML diagram must be created");
            return false;
        }
        try {
            // Criando o objeto
            this.ieos.insertObject("Conjunto", id);
            // Colocando o nome (Classifier) do objeto no campo 'name'
            this.ieos.insertValue("Conjunto", "name", id, name == null ? "" : name);
            // Colocando o link com o Classifier correto
            this.ieos.insertLink("Conjunto", id, "setA", "elementType", elementType, "Classifier");
        } catch (Exception e) {
            logger.error("Error when insert an association class: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertAttribute(String id, String name, String visibility, String type, String classId) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an attribute: the UML diagram must be created");
            return false;
        }
        try {
            // Criando o objeto
            this.ieos.insertObject("Attribute", id);
            // Colocando o nome (Typed) do objeto no campo 'name'
            this.ieos.insertValue("Attribute", "name", id, name == null ? "" : name);
            // Colocando a visibilidade (Feature) do objeto no campo 'visibility'
            this.ieos.insertValue("Attribute", "visibility", id, visibility == null ? "" : visibility);

            this.ieos.insertLink("Attribute", id, "types", "classifier", type, "Classifier");
            this.ieos.insertLink("Attribute", id, "feature", "class", classId, "Class");

        } catch (Exception e) {
            logger.error("Error when insert an operation: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertOperation(String id, String name, String visibility, String returnType, String classId) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an operation: the UML diagram must be created");
            return false;
        }
        try {
            // Criando o objeto
            this.ieos.insertObject("Operation", id);
            // Colocando o nome (Typed) do objeto no campo 'name'
            this.ieos.insertValue("Operation", "name", id, name == null ? "" : name);
            // Colocando a visibilidade (Feature) do objeto no campo 'visibility'
            this.ieos.insertValue("Operation", "visibility", id, visibility == null ? "" : visibility);

            this.ieos.insertLink("Operation", id, "types", "classifier", returnType, "Classifier");
            this.ieos.insertLink("Operation", id, "feature", "class", classId, "Class");

        } catch (Exception e) {
            logger.error("Error when insert an operation: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertParameter(String id, String name, String type, String operationId) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a parameter: the UML diagram must be created");
            return false;
        }
        try {
            // Criando o objeto
            this.ieos.insertObject("Parameter", id);
            // Colocando o nome (Typed) do objeto no campo 'name'
            this.ieos.insertValue("Parameter", "name", id, name == null ? "" : name);

            this.ieos.insertLink("Parameter", id, "types", "classifier", type, "Classifier");
            this.ieos.insertLink("Parameter", id, "parameter", "operation", operationId, "Operation");

        } catch (Exception e) {
            logger.error("Error when insert a parameter: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertAssociationEnd(String id, String name, String visibility, String type, String lower, String upper, Boolean composition, String classId) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an associationEnd: the UML diagram must be created");
            return false;
        }
        try {
            // Criando o objeto
            this.ieos.insertObject("AssociationEnd", id);
            // Colocando o nome (Typed) do objeto no campo 'name'
            this.ieos.insertValue("AssociationEnd", "name", id, name == null ? "" : name);

            this.ieos.insertLink("AssociationEnd", id, "types", "classifier", type, "Classifier");

            // Colocando a visibilidade (Feature) do objeto no campo 'visibility'
            this.ieos.insertValue("AssociationEnd", "visibility", id, visibility == null ? "" : visibility);
            // Colocando o campo 'lower' do objeto
            this.ieos.insertValue("AssociationEnd", "lower", id, lower == null ? "" : lower);
            // Colocando o campo 'upper' do objeto
            this.ieos.insertValue("AssociationEnd", "upper", id, upper == null ? "" : upper);
            // Colocando o campo 'composition' do objeto
            this.ieos.insertValue("AssociationEnd", "composition", id, composition == true ? "true" : "false");

            this.ieos.insertLink("AssociationEnd", id, "feature", "class", classId, "Class");

        } catch (Exception e) {
            logger.error("Error when insert an associationEnd: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertAssociation(String id, String name, String... associationEndIds) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an association: the UML diagram must be created");
            return false;
        }
        try {
            if (associationEndIds.length < 2) {
                throw new Exception("Association can't have less than 2 AssociationEnd");
            }

            // Criando o objeto
            this.ieos.insertObject("Association", id);

            // Faz a relação entre este objeto e os fins de associação, que tem o minimo de 2
            for (String associationEndId : associationEndIds) {
                this.ieos.insertLink("AssociationEnd", associationEndId, "associationEnds", "association", id, "Association");
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
            this.ieos.insertLink("AssociationEnd", otherEnd, "others", "otherEnd", associationEnd, "AssociationEnd");
        } catch (Exception e) {
            logger.error("Error when insert a link between associationEnds: " + e.getMessage());
            return false;
        }
        return true;
    }
}
