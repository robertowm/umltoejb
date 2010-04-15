package br.uff.ic.mda.transformador;

import core.XEOS;
import core.exception.XEOSException;

public class DominioUmlEjb extends Dominio {

    public DominioUmlEjb(XEOS ieos) {
        super(ieos);
    }

    @Override
    public void createSpecificationOfCurrentDiagram() throws Exception {
    }

    @Override
    protected void insertMetamodelInvariants() throws Exception {
        logger.debug("Inserting Uml MetaModel Invariants");
    }

    @Override
    protected boolean insertMetamodelOperations() throws Exception {
        logger.debug("Inserting Uml MetaModel Operations");
        return true;
    }

    @Override
    protected void insertMetamodelStructure() throws Exception {
        logger.debug("Inserting Uml MetaModel Structure");

        this.ieos.insertClass("UMLClassToEJBKeyClass");
        this.ieos.insertClass("UMLAssociationClassToEJBKeyClass");
        this.ieos.insertClass("UMLClassToEJBEntityComponent");

        // Associacoes UMLClassToEJBEntityComponent
        this.ieos.insertAssociation("Class", "class", "1", "1", "transformerToClass", "UMLClassToEJBKeyClass");
        this.ieos.insertAssociation("EJBAttribute", "id", "1", "1", "transformerToClass", "UMLClassToEJBKeyClass");
        this.ieos.insertAssociation("EJBKeyClass", "keyClass", "1", "1", "transformerToClass", "UMLClassToEJBKeyClass");

        // Associacoes UMLAssociationClassToEJBKeyClass
        this.ieos.insertAssociation("AssociationClass", "class", "1", "1", "transformerToAssociationClass", "UMLAssociationClassToEJBKeyClass");
        this.ieos.insertAssociation("EJBAttribute", "id", "2", "1", "transformerToAssociationClass", "UMLAssociationClassToEJBKeyClass");
        this.ieos.insertAssociation("EJBKeyClass", "keyClass", "1", "1", "transformerToAssociationClass", "UMLAssociationClassToEJBKeyClass");

        // Associacoes UMLClassToEJBEntityComponent
        this.ieos.insertAssociation("Class", "class", "1", "1", "transformerToEntityComponent", "UMLClassToEJBEntityComponent");
        this.ieos.insertAssociation("EJBEntityComponent", "entityComponent", "1", "1", "transformerToEntityComponent", "UMLClassToEJBEntityComponent");
        this.ieos.insertAssociation("EJBDataClass", "dataClass", "1", "1", "transformerToEntityComponent", "UMLClassToEJBEntityComponent");
        this.ieos.insertAssociation("EJBDataSchema", "dataSchema", "1", "1", "transformerToEntityComponent", "UMLClassToEJBEntityComponent");
        this.ieos.insertAssociation("EJBServingAttribute", "servingAttribute", "1", "1", "transformerToEntityComponent", "UMLClassToEJBEntityComponent");

        // Atributos de todas as classes (name)
        this.ieos.insertAttribute("UMLClassToEJBKeyClass", "name", "String");
        this.ieos.insertAttribute("UMLAssociationClassToEJBKeyClass", "name", "String");
        this.ieos.insertAttribute("UMLClassToEJBEntityComponent", "name", "String");

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

    public boolean insertUMLClassToEJBKeyClass(String idUmlClass, String idEjbKeyClass, String idEjbAttribute) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a class: the UML diagram must be created");
            return false;
        }
        try {
            String name = idUmlClass + "To" + idEjbKeyClass;
            String id = name + System.nanoTime();
            this.ieos.insertObject("UMLClassToEJBKeyClass", id);
            this.ieos.insertValue("UMLClassToEJBKeyClass", "name", id, name);

            this.ieos.insertLink("Class", idUmlClass, "class", "transformerToClass", id, "UMLClassToEJBKeyClass");
            this.ieos.insertLink("EJBKeyClass", idEjbKeyClass, "keyClass", "transformerToClass", id, "UMLClassToEJBKeyClass");
            this.ieos.insertLink("EJBAttribute", idEjbAttribute, "id", "transformerToClass", id, "UMLClassToEJBKeyClass");
        } catch (Exception e) {
            logger.error("Error when insert a class: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertUMLAssociationClassToEJBKeyClass(String idUmlClass, String idEjbKeyClass, String... idsEjbAttribute) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a class: the UML diagram must be created");
            return false;
        }
        try {
            String name = idUmlClass + "To" + idEjbKeyClass;
            String id = name + System.nanoTime();
            this.ieos.insertObject("UMLClassToEJBKeyClass", id);
            this.ieos.insertValue("UMLClassToEJBKeyClass", "name", id, name);

            this.ieos.insertLink("Class", idUmlClass, "class", "transformerToAssociationClass", id, "UMLClassToEJBKeyClass");
            this.ieos.insertLink("EJBKeyClass", idEjbKeyClass, "keyClass", "transformerToAssociationClass", id, "UMLClassToEJBKeyClass");
            for (String idEjbAttribute : idsEjbAttribute) {
                this.ieos.insertLink("EJBAttribute", idEjbAttribute, "id", "transformerToAssociationClass", id, "UMLClassToEJBKeyClass");
            }
        } catch (Exception e) {
            logger.error("Error when insert a class: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertUMLClassToEJBEntityComponent(String idUmlClass, String idEjbEntityComponent, String idEjbDataClass, String idEjbDataSchema, String idEjbServingAttribute) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a class: the UML diagram must be created");
            return false;
        }
        try {
            String name = idUmlClass + "To" + idEjbEntityComponent;
            String id = name + System.nanoTime();
            this.ieos.insertObject("UMLClassToEJBEntityComponent", id);
            this.ieos.insertValue("UMLClassToEJBEntityComponent", "name", id, name);

            this.ieos.insertLink("Class", idUmlClass, "class", "transformerToEntityComponent", id, "UMLClassToEJBEntityComponent");
            this.ieos.insertLink("EJBEntityComponent", idEjbEntityComponent, "entityComponent", "transformerToEntityComponent", id, "UMLClassToEJBEntityComponent");
            this.ieos.insertLink("EJBDataClass", idEjbDataClass, "dataClass", "transformerToEntityComponent", id, "UMLClassToEJBEntityComponent");
            this.ieos.insertLink("EJBDataSchema", idEjbDataSchema, "dataSchema", "transformerToEntityComponent", id, "UMLClassToEJBEntityComponent");
            this.ieos.insertLink("EJBServingAttribute", idEjbServingAttribute, "servingAttribute", "transformerToEntityComponent", id, "UMLClassToEJBEntityComponent");
        } catch (Exception e) {
            logger.error("Error when insert a class: " + e.getMessage());
            return false;
        }
        return true;
    }
}