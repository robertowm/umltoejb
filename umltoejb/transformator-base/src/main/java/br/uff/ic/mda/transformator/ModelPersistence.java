package br.uff.ic.mda.transformator;

public interface ModelPersistence {

    /* Methods */
    public abstract String query(String query) throws Exception;

    /* Methods for declare the metamodel */
    public abstract boolean insertClass(String className) throws Exception;
    public abstract boolean insertGeneralization(String subClass, String superClass) throws Exception;
    public abstract boolean insertAssociation(String class1Name, String role1Name, String multiplicity1, String multiplicity2, String role2Name, String class2Name) throws Exception;
    public abstract boolean insertAttribute(String className, String attributeName, String typeName) throws Exception;

    /* Methods for declare an instance of the metamodel */
    public abstract boolean insertObject(String className, String objectName) throws Exception;
    public abstract boolean insertValue(String className, String attributeName, String objectName, String value) throws Exception;
    public abstract boolean insertLink(String class1Name, String object1Name, String role1Name, String role2Name, String object2Name, String class2Name) throws Exception;

}
