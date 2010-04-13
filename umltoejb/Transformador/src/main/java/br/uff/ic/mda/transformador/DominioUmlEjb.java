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

        this.ieos.insertAssociation("Class", "class", "1", "1", "transformerToClass", "UMLClassToEJBKeyClass");
        this.ieos.insertAssociation("EJBAttribute", "id", "1", "1", "transformerToClassToClass", "UMLClassToEJBKeyClass");
        this.ieos.insertAssociation("EJBKeyClass", "keyClass", "1", "1", "transformerToClass", "UMLClassToEJBKeyClass");

        this.ieos.insertAssociation("AssociationClass", "class", "1", "1", "transformerToAssociationClass", "UMLAssociationClassToEJBKeyClass");
        this.ieos.insertAssociation("EJBAttribute", "id", "2", "1", "transformerToAssociationClass", "UMLAssociationClassToEJBKeyClass");
        this.ieos.insertAssociation("EJBKeyClass", "keyClass", "1", "1", "transformerToAssociationClass", "UMLAssociationClassToEJBKeyClass");

        this.ieos.insertAttribute("UMLClassToEJBKeyClass", "name", "String");
        this.ieos.insertAttribute("UMLAssociationClassToEJBKeyClass", "name", "String");

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

    public boolean insertUMLClassToEJBKeyClass(String idUMLClass, String idEJBKeyClass, String idEJBAttribute) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a class: the UML diagram must be created");
            return false;
        }
        try {
            String name = idUMLClass + "To" + idEJBKeyClass;
            this.ieos.insertObject("UMLClassToEJBKeyClass", name);
            this.ieos.insertValue("UMLClassToEJBKeyClass", "name", name, name);

            this.ieos.insertLink("Class", idUMLClass, "class", "transformer", name, "UMLClassToEJBKeyClass");
            this.ieos.insertLink("EJBKeyClass", idEJBKeyClass, "keyClass", "transformer", name, "UMLClassToEJBKeyClass");
            this.ieos.insertLink("EJBAttribute", idEJBAttribute, "id", "transformer", name, "UMLClassToEJBKeyClass");
        } catch (Exception e) {
            logger.error("Error when insert a class: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertUMLAssociationClassToEJBKeyClass(String idUMLClass, String idEJBKeyClass, String... idsEJBAttribute) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a class: the UML diagram must be created");
            return false;
        }
        try {
            String name = idUMLClass + "To" + idEJBKeyClass;
            this.ieos.insertObject("UMLClassToEJBKeyClass", name);
            this.ieos.insertValue("UMLClassToEJBKeyClass", "name", name, name);

            this.ieos.insertLink("Class", idUMLClass, "class", "transformer", name, "UMLClassToEJBKeyClass");
            this.ieos.insertLink("EJBKeyClass", idEJBKeyClass, "keyClass", "transformer", name, "UMLClassToEJBKeyClass");
            for (String id : idsEJBAttribute) {
                this.ieos.insertLink("EJBAttribute", id, "id", "transformer", name, "UMLClassToEJBKeyClass");
            }
        } catch (Exception e) {
            logger.error("Error when insert a class: " + e.getMessage());
            return false;
        }
        return true;
    }
}