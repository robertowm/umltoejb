package br.uff.ic.mda.transformer;

import core.XEOS;
import core.exception.XEOSException;

public class UmlDomain extends Domain {

    public UmlDomain(XEOS ieos) throws Exception {
        super(ieos);
    }

    @Override
    public void createSpecificationOfCurrentDiagram() throws Exception {
        this.ieos.insertObject("Classifier", "NULL_CLASSIFIER");
        this.ieos.insertObject("Class", "NULL_CLASS");
        this.ieos.insertObject("Operation", "NULL_OPERATION");
        this.ieos.insertObject("Association", "NULL_ASSOCIATION");

        this.ieos.insertObject("DataType", "UMLInteger");
        this.ieos.insertValue("DataType", "name", "UMLInteger", "Integer");
        this.ieos.insertObject("DataType", "UMLDouble");
        this.ieos.insertValue("DataType", "name", "UMLDouble", "Double");
        this.ieos.insertObject("DataType", "UMLReal");
        this.ieos.insertValue("DataType", "name", "UMLReal", "Real");
        this.ieos.insertObject("DataType", "UMLString");
        this.ieos.insertValue("DataType", "name", "UMLString", "String");
        this.ieos.insertObject("DataType", "UMLDate");
        this.ieos.insertValue("DataType", "name", "UMLDate", "Date");
        this.ieos.insertObject("DataType", "UMLBoolean");
        this.ieos.insertValue("DataType", "name", "UMLBoolean", "Boolean");
    }

    @Override
    public void insertMetamodelInvariants() throws Exception {
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
                "Feature.allInstances()->forAll(f : Feature | not f.class->includes(NULL_CLASS))");

        // Todo Parameter precisa ter uma Operation
        this.insertInvariant("restrictionRequiredAssociationParameterToOperation",
                "Parameter.allInstances()->forAll(p : Parameter | not p.operation->includes(NULL_OPERATION))");

        // Todo AssociationEnd precisa ter uma Association
        this.insertInvariant("restrictionRequiredAssociationAssociationEndToAssociation",
                //                "AssociationEnd.allInstances()->forAll(ae : AssociationEnd | not ae.association->includes(NULL_ASSOCIATION))");
                "AssociationEnd.allInstances().association->forAll(a : Association | a <> NULL_ASSOCIATION)");

        // Todo Association precisa ter, no minimo, 1 AssociationEnd
        this.insertInvariant("restrictionMinimunOneAssociationEndPerAssociation",
                "Association.allInstances()->excluding(NULL_ASSOCIATION)->forAll(a : Association | a.associationEnds->size() >= 1)");

        // Nao pode ter ciclos na hierarquia de heranca de classes
        this.insertInvariant("noCyclesinClassHierarchy",
                "Class.allInstances()->excluding(NULL_CLASS)->forAll(c : Class | c.inheritsFrom->forAll(r : Class | r.superPlus()->excludes(c)))");

    }

    @Override
    public boolean insertMetamodelOperations() throws Exception {
        logger.debug("Inserting Uml MetaModel Operations");
        boolean result = true;

        Object[] params;
        String[] param;

        result &= this.ieos.insertOperation("Class", "getAllContained", "Set(Class)",
                "Class.allInstances()->select(c | c.subindo(c.emptySet())->includes(self))", new Object[0]);

        result &= this.ieos.insertOperation("Class", "subindo", "Set(Class)",
                "if self.oclIsTypeOf(Class) "
                + "then self.subindoClass(lista) "
                + "else "
                + "self.oclAsType(AssociationClass).subindoAssociationClass(lista) "
                + "endif", new Object[]{new String[]{"lista", "Set(Class)"}});

        result &= this.ieos.insertOperation("Class", "subindoClass", "Set(Class)",
                "if "
                + "self.feature"
                + "->select(f : Feature | f.oclIsKindOf(AssociationEnd))"
                + "->collect(f : Feature | f.oclAsType(AssociationEnd))"
                + "->exists(ae : AssociationEnd | ae.composition = true)"
                + " then "
                + "self.feature"
                + "->select(f : Feature | f.oclIsKindOf(AssociationEnd))"
                + "->collect(f : Feature | f.oclAsType(AssociationEnd))"
                + "->select(ae : AssociationEnd | ae.composition = true)"
                + ".classifier"
                + "->select(c : Classifier | c.oclIsKindOf(Class))"
                + "->collect(c : Classifier | c.oclAsType(Class))"
                + "->asOrderedSet()"
                + "->first()"
                + ".subindo(lista->including(self))"
                + " else "
                + "lista->including(self)"
                + " endif", new Object[]{new String[]{"lista", "Set(Class)"}});

        result &= this.ieos.insertOperation("AssociationClass", "subindoAssociationClass", "Set(Class)",
                "if "
                + "self.associationEnds->size() = 1 "
                + "then "
                + "self.associationEnds"
                + "->asOrderedSet()"
                + "->first()"
                + ".class"
                + "->asOrderedSet()"
                + "->first()"
                + ".subindo(lista->including(self))"
                + "else "
                + "lista->including(self) "
                + "endif", new Object[]{new String[]{"lista", "Set(Class)"}});

//        result &= this.ieos.insertOperation("Class", "getAllContained", "Set(Class)",
//                "if contained->includes(self) then "
//                + "contained->asSet() "
//                + "else "
//                + "self.feature"
//                + "->select(f : Feature | f.oclIsKindOf(AssociationEnd))"
//                + "->collect(f : Feature | f.oclAsType(AssociationEnd))"
////                + "->select(ae : AssociationEnd | ae.composition = true)"
//                + "->select(ae : AssociationEnd | ae.otherEnd->exists(oe : AssociationEnd | oe.composition = true))"
//                + "->collect(ae : AssociationEnd | ae.classifier)"
//                //Nova condicao - AssociationEnd que o Association eh um AssociationClass e o AssociationEnd eh direcionado
//                + "->union(self.feature->select(f : Feature | f.oclIsKindOf(AssociationEnd))->collect(f : Feature | f.oclAsType(AssociationEnd).association)->select(assoc : Association | assoc.oclIsTypeOf(AssociationClass))->collect(assoc : Association | assoc.oclAsType(AssociationClass))->select(ac : AssociationClass | ac.associationEnds->size() = 1)->collect(ac : AssociationClass | ac.oclAsType(Classifier)))"
//                + "->select(c : Classifier | c.oclIsKindOf(Class))"
//                + "->collect(c : Classifier| c.oclAsType(Class))"
//                + "->iterate( cc : Class ; acc : Set(Class) = allContained "
//                + "| acc->union(cc.oclAsType(Class).getAllContained(cc.emptySet(), cc.emptySet()->including(cc))))"
//                + " endif ", new Object[]{new String[]{"contained", "Set(Class)"}, new String[]{"allContained", "Set(Class)"}});
        result &= this.ieos.insertOperation("Class", "emptySet", "Set(Class)",
                "Class.allInstances()->select(c|false) ", new Object[0]);


        result &= this.ieos.insertOperation("Class", "getOuterMostContainer", "Class",
                "if self.oclIsTypeOf(Class) "
                + "then self.getOuterMostContainerFromClass() "
                + "else "
                + "self.oclAsType(AssociationClass).getOuterMostContainerFromAssociationClass() "
                + "endif", new Object[0]);

        result &= this.ieos.insertOperation("Class", "getOuterMostContainerFromClass", "Class",
                "if "
                + "self.feature"
                + "->select(f : Feature | f.oclIsKindOf(AssociationEnd))"
                + "->collect(f : Feature | f.oclAsType(AssociationEnd))"
                + "->exists(ae : AssociationEnd | ae.composition = true)"
                + " then "
                + "self.feature"
                + "->select(f : Feature | f.oclIsKindOf(AssociationEnd))"
                + "->collect(f : Feature | f.oclAsType(AssociationEnd))"
                + "->select(ae : AssociationEnd | ae.composition = true)"
                + ".classifier"
                + "->select(c : Classifier | c.oclIsKindOf(Class))"
                + "->collect(c : Classifier | c.oclAsType(Class))"
                + "->asOrderedSet()"
                + "->first()"
                + ".getOuterMostContainer()"
                + " else "
                + "self"
                + " endif", new Object[0]);

        result &= this.ieos.insertOperation("Association", "getOuterMostContainerFromAssociation", "Class",
                "if self.oclIsTypeOf(Association) then "
                + "self.associationEnds"
                + "->select(ae : AssociationEnd | ae.composition = true)"
                + "->asOrderedSet()"
                + "->first()"
                + ".classifier"
                + "->select(c : Classifier | c.oclIsKindOf(Class))"
                + "->collect(c : Classifier | c.oclAsType(Class))"
                + "->asOrderedSet()"
                + "->first()"
                + ".getOuterMostContainer() "
                + "else self.oclAsType(AssociationClass).getOuterMostContainerFromAssociationClass() endif", new Object[0]);

        result &= this.ieos.insertOperation("AssociationClass", "getOuterMostContainerFromAssociationClass", "Class",
                "if "
                + "self.associationEnds->size() = 1 "
                + "then "
                + "self.associationEnds"
                + "->asOrderedSet()"
                + "->first()"
                + ".class"
                + ".getOuterMostContainer()"
                + "->asOrderedSet()"
                + "->first() "
                + "else "
                + "self "
                + "endif", new Object[0]);

        result &= this.ieos.insertOperation("Class", "isOuterMostContainer", "Boolean",
                "self = self.getOuterMostContainer()", new Object[0]);

        result &= this.ieos.insertOperation("Class", "getAllOuterMostContainer", "Set(Class)",
                "Class.allInstances()->select(c : Class | c.isOuterMostContainer())->asSet()", new Object[0]);

        result &= this.ieos.insertOperation("Class", "superPlus", "Set(Class)",
                "self.superPlusOnSet(self.emptySet())", new Object[0]);

        // Versao que o Christiano mandou
//        result &= this.ieos.insertOperation("Class", "superPlusOnSet", "Set(Class)",
//                "if self.inheritsFrom->exists(r : Class | rs->excludes(r)) then "
//                    + "self.superPlusOnSet(rs->union(rs.inheritsFrom)->asSet())"
//                + " else "
//                    + "rs->including(self)"
//                + " endif", new Object[]{new String[]{"rs", "Set(Class)"}});
        result &= this.ieos.insertOperation("Class", "superPlusOnSet", "Set(Class)",
                "if self.inheritsFrom->notEmpty() and rs->excludes(self) then "
                + "self.inheritsFrom->collect(c : Class | c.superPlusOnSet(rs->including(self)))->flatten()->asSet()"
                + " else "
                + "rs->including(self)"
                + " endif", new Object[]{new String[]{"rs", "Set(Class)"}});

        if (!result) {
            throw new Exception("It was not possible to insert all the operations in the UML model");
        }

        return result;
    }

    @Override
    public void insertMetamodelClasses() throws Exception {
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
    }

    @Override
    public void insertMetamodelAssociations() throws Exception {
        this.ieos.insertAssociation("AssociationEnd", "otherEnd", "0..1", "0..*", "others", "AssociationEnd");
        this.ieos.insertAssociation("AssociationEnd", "associationEnds", "1..*", "1", "association", "Association");
        this.ieos.insertAssociation("Operation", "operation", "1", "*", "parameter", "Parameter");
        this.ieos.insertAssociation("Class", "class", "0..1", "*", "feature", "Feature");
        this.ieos.insertAssociation("Class", "classes", "*", "*", "implementedInterfaces", "Interface");
        this.ieos.insertAssociation("Class", "inheritsFrom", "*", "*", "inheritedBy", "Class");
        this.ieos.insertAssociation("Classifier", "classifier", "0..1", "*", "types", "Typed");
        this.ieos.insertAssociation("Conjunto", "setA", "0..*", "1", "elementType", "Classifier");
    }

    @Override
    public void insertMetamodelAttributes() throws Exception {
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

    // Specific methods of this domain
    // Class
    public boolean insertClass(String id, String name) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a class: the UML diagram must be created");
            return false;
        }
        this.ieos.insertObject("Class", id);
        this.ieos.insertValue("Class", "name", id, name == null ? "" : name);
        return true;
    }

    public boolean insertClassInheritance(String idPai, String idFilho) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a class: the UML diagram must be created");
            return false;
        }
        this.ieos.insertLink("Class", idPai, "inheritsFrom", "inheritedBy", idFilho, "Class");
        return true;
    }

    // AssociationClass
    public boolean insertAssociationClass(String id, String name, String... ends) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an association class: the UML diagram must be created");
            return false;
        }
        this.ieos.insertObject("AssociationClass", id);
        this.ieos.insertValue("AssociationClass", "name", id, name == null ? "" : name);
        for (String end : ends) {
            this.ieos.insertLink("AssociationEnd", end, "associationEnds", "association", id, "AssociationClass");
        }
        return true;
    }

    // Set
    public boolean insertSet(String id, String name, String elementType) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a set: the UML diagram must be created");
            return false;
        }
        this.ieos.insertObject("Conjunto", id);
        this.ieos.insertValue("Conjunto", "name", id, name == null ? "" : name);
        this.ieos.insertLink("Conjunto", id, "setA", "elementType", elementType, "Classifier");
        return true;
    }

    // Attribute
    public boolean insertAttribute(String id, String name, String visibility, String type, String classId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an attribute: the UML diagram must be created");
            return false;
        }
        this.ieos.insertObject("Attribute", id);
        this.ieos.insertValue("Attribute", "name", id, name == null ? "" : name);
        this.ieos.insertValue("Attribute", "visibility", id, visibility == null ? "" : visibility);
        this.ieos.insertLink("Attribute", id, "types", "classifier", type, "Classifier");
        this.ieos.insertLink("Attribute", id, "feature", "class", classId, "Class");
        return true;
    }

    // Operation
    public boolean insertOperation(String id, String name, String visibility, String returnType, String classId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an operation: the UML diagram must be created");
            return false;
        }
        this.ieos.insertObject("Operation", id);
        this.ieos.insertValue("Operation", "name", id, name == null ? "" : name);
        this.ieos.insertValue("Operation", "visibility", id, visibility == null ? "" : visibility);
        this.ieos.insertLink("Operation", id, "types", "classifier", returnType, "Classifier");
        this.ieos.insertLink("Operation", id, "feature", "class", classId, "Class");
        return true;
    }

    // Parameter
    public boolean insertParameter(String id, String name, String type, String operationId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a parameter: the UML diagram must be created");
            return false;
        }
        this.ieos.insertObject("Parameter", id);
        this.ieos.insertValue("Parameter", "name", id, name == null ? "" : name);
        this.ieos.insertLink("Parameter", id, "types", "classifier", type, "Classifier");
        this.ieos.insertLink("Parameter", id, "parameter", "operation", operationId, "Operation");
        return true;
    }

    // Association
    public boolean insertAssociation(String id, String name, String... associationEndIds) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an association: the UML diagram must be created");
            return false;
        }
        if (associationEndIds.length < 2) {
            throw new Exception("Association can't have less than 2 AssociationEnd");
        }
        this.ieos.insertObject("Association", id);
        this.ieos.insertValue("Association", "name", id, name == null ? "" : name);
        for (String associationEndId : associationEndIds) {
            this.ieos.insertLink("AssociationEnd", associationEndId, "associationEnds", "association", id, "Association");
        }
        return true;
    }

    // AssociationEnd
    public boolean insertAssociationEnd(String id, String name, String visibility, String type, String lower, String upper, Boolean composition, String classId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an associationEnd: the UML diagram must be created");
            return false;
        }
        this.ieos.insertObject("AssociationEnd", id);
        this.ieos.insertValue("AssociationEnd", "name", id, name == null ? "" : name);
        this.ieos.insertValue("AssociationEnd", "visibility", id, visibility == null ? "" : visibility);
        this.ieos.insertValue("AssociationEnd", "lower", id, lower == null ? "" : lower);
        this.ieos.insertValue("AssociationEnd", "upper", id, upper == null ? "" : upper);
        this.ieos.insertValue("AssociationEnd", "composition", id, composition == true ? "true" : "false");
        this.ieos.insertLink("AssociationEnd", id, "types", "classifier", type, "Classifier");
        this.ieos.insertLink("AssociationEnd", id, "feature", "class", classId, "Class");

        return true;
    }
    public boolean insertLinksBetweenAssociationEnds(String associationEnd, String otherEnd) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a link between associationEnds: the UML diagram must be created");
            return false;
        }
        this.ieos.insertLink("AssociationEnd", associationEnd, "others", "otherEnd", otherEnd, "AssociationEnd");
        this.ieos.insertLink("AssociationEnd", otherEnd, "others", "otherEnd", associationEnd, "AssociationEnd");
        return true;
    }
}
