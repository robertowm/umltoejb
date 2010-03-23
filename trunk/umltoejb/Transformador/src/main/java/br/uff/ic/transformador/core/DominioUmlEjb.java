package br.uff.ic.transformador.core;

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

        this.ieos.insertAssociation("Class", "class", "1", "1", "transformer", "UMLClassToEJBKeyClass");
        this.ieos.insertAssociation("EJBAttribute", "id", "1", "1", "transformer", "UMLClassToEJBKeyClass");
        this.ieos.insertAssociation("EJBKeyClass", "keyClass", "1", "1", "transformer", "UMLClassToEJBKeyClass");

        this.ieos.insertAttribute("UMLClassToEJBKeyClass", "name", "String");

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

    public boolean insertUMLClassToEJBKeyClass(String nameUMLClass, String nameEJBKeyClass, String nameEJBAttribute) {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a class: the UML diagram must be created");
            return false;
        }
        try {
            String name = nameUMLClass + "To" + nameEJBKeyClass;
            this.ieos.insertObject("UMLClassToEJBKeyClass", name);
            this.ieos.insertValue("UMLClassToEJBKeyClass", "name", name, name);

            this.ieos.insertLink("Class", nameUMLClass, "class", "transformer", name, "UMLClassToEJBKeyClass");
            this.ieos.insertLink("EJBKeyClass", nameEJBKeyClass, "keyClass", "transformer", name, "UMLClassToEJBKeyClass");
            this.ieos.insertLink("EJBAttribute", nameEJBAttribute, "id", "transformer", name, "UMLClassToEJBKeyClass");
        } catch (Exception e) {
            logger.error("Error when insert a class: " + e.getMessage());
            return false;
        }
        return true;
    }
}