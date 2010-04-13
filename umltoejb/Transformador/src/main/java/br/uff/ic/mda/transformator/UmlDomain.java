package br.uff.ic.mda.transformator;

import java.util.List;

public class UmlDomain extends Domain {

    /* Constructors */
    public UmlDomain(ModelPersistence modelPersistence) {
        super(modelPersistence);
    }

    public UmlDomain(ModelPersistence modelPersistence, List<DomainValidator> domainValidators) {
        super(modelPersistence, domainValidators);
    }

    /* Methods (Override) */
    @Override
    public String print() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean initializeMetamodelStructure() throws Exception {
        this.logger.debug("Inserting Uml MetaModel Structure");

        boolean result = true;

        /* Classes */
        result &= this.modelPersistence.insertClass("ModelElement");
        result &= this.modelPersistence.insertClass("Classifier");
        result &= this.modelPersistence.insertClass("Typed");
        result &= this.modelPersistence.insertClass("DataType");
        result &= this.modelPersistence.insertClass("MySet");
        result &= this.modelPersistence.insertClass("Class");
        result &= this.modelPersistence.insertClass("Interface");
        result &= this.modelPersistence.insertClass("AssociationClass");
        result &= this.modelPersistence.insertClass("Feature");
        result &= this.modelPersistence.insertClass("AssociationEnd");
        result &= this.modelPersistence.insertClass("Association");
        result &= this.modelPersistence.insertClass("Attribute");
        result &= this.modelPersistence.insertClass("Operation");
        result &= this.modelPersistence.insertClass("Parameter");

        /* Generalizations */
        result &= this.modelPersistence.insertGeneralization("Association", "ModelElement");
        result &= this.modelPersistence.insertGeneralization("Classifier", "ModelElement");
        result &= this.modelPersistence.insertGeneralization("Typed", "ModelElement");
        result &= this.modelPersistence.insertGeneralization("DataType", "Classifier");
        result &= this.modelPersistence.insertGeneralization("MySet", "DataType");
        result &= this.modelPersistence.insertGeneralization("Class", "Classifier");
        result &= this.modelPersistence.insertGeneralization("Interface", "Classifier");
        result &= this.modelPersistence.insertGeneralization("AssociationClass", "Class");
        result &= this.modelPersistence.insertGeneralization("AssociationClass", "Association");
        result &= this.modelPersistence.insertGeneralization("Feature", "Typed");
        result &= this.modelPersistence.insertGeneralization("AssociationEnd", "Feature");
        result &= this.modelPersistence.insertGeneralization("Attribute", "Feature");
        result &= this.modelPersistence.insertGeneralization("Operation", "Feature");
        result &= this.modelPersistence.insertGeneralization("Parameter", "Typed");

        /* Associations */
        result &= this.modelPersistence.insertAssociation("MySet", "set", "0..*", "1", "elementType", "Classifier");
        result &= this.modelPersistence.insertAssociation("Classifier", "classifier", "0..1", "*", "types", "Typed");
        result &= this.modelPersistence.insertAssociation("Class", "classes", "*", "*", "implementedInterfaces", "Interface");
        result &= this.modelPersistence.insertAssociation("AssociationEnd", "otherEnd", "1..*", "1..*", "others", "AssociationEnd");
        result &= this.modelPersistence.insertAssociation("AssociationEnd", "associationEnds", "2..*", "1", "association", "Association");

        /* Aggregations */
        result &= this.modelPersistence.insertAssociation("Class", "class", "0..1", "*", "feature", "Feature");
        result &= this.modelPersistence.insertAssociation("Operation", "operation", "1", "*", "parameter", "Parameter");

        /* Attributes */
        result &= this.modelPersistence.insertAttribute("ModelElement", "name", "String");
        result &= this.modelPersistence.insertAttribute("Feature", "visibility", "String"); // ### Tipo VisibilityKind
        result &= this.modelPersistence.insertAttribute("AssociationEnd", "lower", "String"); // ### Tipo Lowerbound
        result &= this.modelPersistence.insertAttribute("AssociationEnd", "upper", "String"); // ### Tipo Upperbound
        result &= this.modelPersistence.insertAttribute("AssociationEnd", "composition", "Boolean");

        return result;
    }

    /* New methods (inserting instances of UML's metamodel)*/
    public boolean insertClass(String classId, String className) throws Exception {
        boolean result = true;

        result &= this.modelPersistence.insertObject("Class", classId);
        result &= this.modelPersistence.insertValue("Class", "name", classId, className);

        return result;
    }

    public boolean insertAssociationClass(String associationClassId, String associationClassName, String... associationEndIds) throws Exception {
        boolean result = true;

        result &= this.modelPersistence.insertObject("AssociationClass", associationClassId);
        result &= this.modelPersistence.insertValue("AssociationClass", "name", associationClassId, associationClassName);

        for (String associationEndId : associationEndIds) {
            result &= this.modelPersistence.insertLink("AssociationEnd", associationEndId, "associationEnds", "association", associationClassId, "AssociationClass");
        }

        return result;
    }

    public boolean insertSet(String setId, String setName, String elementTypeId) throws Exception {
        boolean result = true;

        result &= this.modelPersistence.insertObject("MySet", setId);
        result &= this.modelPersistence.insertValue("MySet", "name", setId, setName);
        result &= this.modelPersistence.insertLink("MySet", setId, "set", "elementType", elementTypeId, "Classifier");

        return result;
    }

    public boolean insertAttribute(String attributeId, String attributeName, String attributeVisibility, String attributeTypeId, String classId) throws Exception {
        boolean result = true;

        result &= this.modelPersistence.insertObject("Attribute", attributeId);
        result &= this.modelPersistence.insertValue("Attribute", "name", attributeId, attributeName);
        result &= this.modelPersistence.insertValue("Attribute", "visibility", attributeId, attributeVisibility);
        result &= this.modelPersistence.insertLink("Attribute", attributeId, "types", "classifier", attributeTypeId, "Classifier");
        result &= this.modelPersistence.insertLink("Attribute", attributeId, "feature", "class", classId, "Class");

        return result;
    }

    public boolean insertOperation(String operationId, String operationName, String operationVisibility, String operationReturnTypeId, String classId) throws Exception {
        boolean result = true;

        result &= this.modelPersistence.insertObject("Operation", operationId);
        result &= this.modelPersistence.insertValue("Operation", "name", operationId, operationName);
        result &= this.modelPersistence.insertValue("Operation", "visibility", operationId, operationVisibility);
        result &= this.modelPersistence.insertLink("Operation", operationId, "types", "classifier", operationReturnTypeId, "Classifier");
        result &= this.modelPersistence.insertLink("Operation", operationId, "feature", "class", classId, "Class");

        return result;
    }

    public boolean insertParameter(String parameterId, String parameterName, String parameterTypeId, String operationId) throws Exception {
        boolean result = true;

        result &= this.modelPersistence.insertObject("Parameter", parameterId);
        result &= this.modelPersistence.insertValue("Parameter", "name", parameterId, parameterName);
        result &= this.modelPersistence.insertLink("Parameter", parameterId, "types", "classifier", parameterTypeId, "Classifier");
        result &= this.modelPersistence.insertLink("Parameter", parameterId, "parameter", "operation", parameterId, "Operation");

        return result;
    }

    public boolean insertAssociationEnd(String associationEndId, String associationEndName, String associationEndVisibility, String associationEndType, String associationEndLower, String associationEndUpper, Boolean associationEndComposition, String classId) throws Exception {
        boolean result = true;

        result &= this.modelPersistence.insertObject("AssociationEnd", associationEndId);
        result &= this.modelPersistence.insertValue("AssociationEnd", "name", associationEndId, associationEndName);
        result &= this.modelPersistence.insertValue("AssociationEnd", "visibility", associationEndId, associationEndVisibility);
        result &= this.modelPersistence.insertValue("AssociationEnd", "lower", associationEndId, associationEndLower);
        result &= this.modelPersistence.insertValue("AssociationEnd", "upper", associationEndId, associationEndUpper);
        result &= this.modelPersistence.insertValue("AssociationEnd", "composition", associationEndId, (associationEndComposition ? "true" : "false"));
        result &= this.modelPersistence.insertLink("AssociationEnd", associationEndId, "types", "classifier", associationEndType, "Classifier");
        result &= this.modelPersistence.insertLink("AssociationEnd", associationEndId, "feature", "class", classId, "Class");

        return result;
    }

    public boolean insertAssociation(String associationId, String associationName, String... associationEndIds) throws Exception{
        if (associationEndIds.length < 2) {
            throw new Exception("Association can't have less than two AssociationEnds");
        }

        boolean result = true;

        result &= this.modelPersistence.insertObject("Association", associationId);
        result &= this.modelPersistence.insertValue("Association", "name", associationId, associationName);
        for (String associationEndId : associationEndIds) {
            result &= this.modelPersistence.insertLink("AssociationEnd", associationEndId, "associationEnds", "association", associationId, "Association");
        }

        return result;
    }

    public boolean insertLinksBetweenAssociationEnds(String associationEndId, String otherAssociationEndId) throws Exception {
        return this.modelPersistence.insertLink("AssociationEnd", associationEndId, "others", "otherEnd", otherAssociationEndId, "AssociationEnd");
    }
}
