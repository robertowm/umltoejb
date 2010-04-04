package core;

import br.uff.ic.mda.transformator.ModelPersistence;
import core.exception.XEOSException;
import core.oclLex.interpreter.Interpreter;
import core.oclLex.lexer.Lexer;
import core.oclLex.node.Start;
import core.oclLex.parser.Parser;
import core.tree.Tree;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XEOS implements ModelPersistence {

    final Logger logger = LoggerFactory.getLogger(XEOS.class);
    private State state;
    protected Lexer lexer;
    protected Parser parser;
    protected Start ast;
    protected Interpreter interp;
    protected Evaluator evaluater;
    protected int actualState;
    protected String version;

    public XEOS() {
        //PropertyConfigurator.configure("xeoslog.properties");
        this.actualState = 0;
        this.version = "0.2";
    }

    public boolean createClassDiagram() throws XEOSException {
        if (this.actualState == 0) {
            this.state = new State();
            this.state.initialize();
            this.actualState = 1;
            return true;
        }
        // JOptionPane.showMessageDialog(null,
        // "Error when create the class diagram: the state must be initializated or finalized");
        // return false;
        logAndThrow("Error when create the class diagram: the state must be initializated or finalized");
        return false;
    }

    private void logAndThrow(String message) throws XEOSException {
        logAndThrow(message, null);
    }

    private void logAndThrow(String message, Throwable e) throws XEOSException {
        logger.error(message);
        throw new XEOSException(message, e);
    }

    public boolean closeClassDiagram() throws XEOSException {
        if (this.actualState == 1) {
            this.actualState = 2;
            this.state.applyInheritance();
            this.state.classDiagramToArray();
            try {
                this.evaluater = new Evaluator(this.state);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        // JOptionPane.showMessageDialog(null,
        // "Error when close the class diagram: the class diagram must be created");
        // return false;
        logAndThrow("Error when close the class diagram: the class diagram must be created");
        return false;
    }

    public boolean createObjectDiagram() throws XEOSException {
        if (this.actualState == 2) {
            this.actualState = 3;
            return true;
        }
        // JOptionPane.showMessageDialog(null,
        // "Error when create the object diagram: the class diagram must be closed");
        // return false;
        logAndThrow("Error when create the object diagram: the class diagram must be closed");
        return false;
    }

    public boolean closeObjectDiagram() throws Exception {
        boolean result = true;
        if (this.actualState == 3) {
            this.state.objectDiagramToArray();
            this.state.cleanVectorObjectDiagram();
//            this.evaluater.initializeCollections();
            this.evaluater.initializeObjectsAndEnumerations();
            this.evaluater.initializeOperations();
            this.actualState = 4;
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public boolean insertClass(String className) throws XEOSException {
        if (this.actualState != 1) {
            // JOptionPane.showMessageDialog(null,
            // "Error when insert a class: the class diagram must be created");
            // return false;
            logAndThrow("Error when insert a class: the class diagram must be created");
        }
        boolean successfully = true;
        try {
            this.state.addClass(className);
        } catch (Exception e) {
            // JOptionPane.showMessageDialog(null, "Error when insert a class: "
            // + e.getMessage());
            // successfully = false;
            logAndThrow("Error when insert a class: " + e.getMessage());
        }
        return successfully;
    }

    public boolean insertAttribute(String className, String attributeName,
            String typeName) throws XEOSException {
        if (this.actualState != 1) {
            // JOptionPane.showMessageDialog(null,
            // "Error when insert a attribute: the class diagram must be created");
            // return false;
            logAndThrow("Error when insert a attribute: the class diagram must be created");
        }
        boolean successfully = true;
        try {
            this.state.addAttribute(className, attributeName, typeName);
        } catch (Exception e) {
            // JOptionPane.showMessageDialog(null,
            // "Error when insert a attribute: " + e.getMessage());
            // successfully = false;
            logAndThrow("Error when insert a attribute: " + e.getMessage());
        }
        return successfully;
    }

    public boolean insertAssociation(String class1Name, String role1Name,
            String multiplicity1, String multiplicity2, String role2Name,
            String class2Name) throws XEOSException {
        if (this.actualState != 1) {
            // JOptionPane.showMessageDialog(null,
            // "Error when insert a association: the class diagram must be created");
            // return false;
            logAndThrow("Error when insert a association: the class diagram must be created");
        }
        boolean successfully = true;
        try {
            this.state.addAssociation(class1Name, class2Name, role1Name, role2Name, multiplicity1, multiplicity2);
        } catch (Exception e) {
            // JOptionPane.showMessageDialog(null,
            // "Error when insert a association: " + e.getMessage());
            // successfully = false;
            logAndThrow("Error when insert a association: " + e.getMessage());
        }
        return successfully;
    }

    public boolean insertGeneralization(String subClass, String superClass)
            throws XEOSException {
        if (this.actualState != 1) {
            // JOptionPane.showMessageDialog(null,
            // "Error when insert a generalization: the class diagram must be created");
            // return false;
            logAndThrow("Error when insert a generalization: the class diagram must be created");
        }
        boolean successfully = true;
        try {
            this.state.addGeneralization(subClass, superClass);
        } catch (Exception e) {
            // JOptionPane.showMessageDialog(null,
            // "Error when insert a generalization: " + e.getMessage());
            // successfully = false;
            logAndThrow("Error when insert a generalization: " + e.getMessage());
        }
        return successfully;
    }

    public boolean insertObject(String className, String objectName)
            throws XEOSException {
        if (this.actualState != 3) {
            // JOptionPane.showMessageDialog(null,
            // "Error when insert a object: the object diagram must be created");
            // return false;
            logAndThrow("Error when insert a object: the object diagram must be created");
        }
        boolean successfully = true;
        try {
            this.state.addObject(className, objectName);
        } catch (Exception e) {
            // JOptionPane.showMessageDialog(null,
            // "Error when insert a object: " + e.getMessage());
            // successfully = false;
            logAndThrow("Error when insert a object: " + e.getMessage());
        }
        return successfully;
    }

    public boolean insertValue(String className, String attributeName,
            String objectName, String value) throws XEOSException {
        if (this.actualState != 3) {
            // JOptionPane.showMessageDialog(null,
            // "Error when insert a value: the object diagram must be created");
            // return false;
            logAndThrow("Error when insert a value: the object diagram must be created");
        }
        boolean successfully = true;
        try {
            this.state.addValue(className, attributeName, objectName, value);
        } catch (Exception e) {
            // JOptionPane.showMessageDialog(null, "Error when insert a value: "
            // + e.getMessage());
            // successfully = false;
            logAndThrow("Error when insert a value: " + e.getMessage());
        }
        return successfully;
    }

    public boolean insertLink(String class1Name, String object1Name,
            String role1Name, String role2Name, String object2Name,
            String class2Name) throws XEOSException {
        if (this.actualState != 3) {
            // JOptionPane.showMessageDialog(null,
            // "Error when insert a link: the object diagram must be created");
            // return false;
            logAndThrow("Error when insert a link: the object diagram must be created");
        }
        boolean successfully = true;
        try {
            this.state.addLink(class1Name, object1Name, role1Name, class2Name, object2Name, role2Name);
        } catch (Exception e) {
            // JOptionPane.showMessageDialog(null, "Error when insert a link: "
            // + e.getMessage());
            // successfully = false;
            logAndThrow("Error when insert a link: " + e.getMessage());
        }
        return successfully;
    }

    public boolean deleteLink(String c1, String o1, String r1, String c2,
            String o2, String r2) throws XEOSException {
        if (this.actualState != 3) {
            // System.out.println("Error when insert a link: the object diagram must be created");
            // return false;
            logAndThrow("Error when insert a link: the object diagram must be created");
        }
        try {
            this.state.deleteLink(c1, o1, r1, c2, o2, r2);
        } catch (Exception e) {
            // System.out.println("Error deleting a link: " + e.getMessage());
            // e.printStackTrace();
            // return false;
            logAndThrow("Error deleting a link", e);
            return false;
        }
        return true;
    }

    public boolean insertOperation(String contextClass, String nameOperation, String returnType, String bodyOperation, Object[] params) throws XEOSException {
        HashMap oclOperations = this.state.getOclOperations();
        if (oclOperations.get(nameOperation) != null) {
            //JOptionPane.showMessageDialog(null, "There is yet an operation defined which its name is '" + nameOperation + "'");
            //return false;
            logAndThrow("There is yet an operation defined which its name is '" + nameOperation + "'");
        }

        try {
            this.lexer = new Lexer(new PushbackReader(new StringReader(bodyOperation), 1024));
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "Lexical error in : '" + nameOperation + "' operation " + e.getMessage());
            //return false;
            logAndThrow("Lexical error in : '" + nameOperation + "' operation " + e.getMessage(), e);
        }
        try {
            this.parser = new Parser(this.lexer);
            this.ast = this.parser.parse();
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "Parsing error in : '" + nameOperation + "' operation " + e.getMessage());
            //return false;
            logAndThrow("Parsing error in : '" + nameOperation + "' operation " + e.getMessage(), e);
        }

        try {
            this.interp = new Interpreter();
            this.interp.setDebug(1);
            this.ast.apply(this.interp);
            String interpreterError = this.interp.getError();
            if (interpreterError != null) {
                throw new Exception(interpreterError);
            }
        } catch (Exception interpreterError) {
            //JOptionPane.showMessageDialog(null, "Interpreter error in : '" + nameOperation + "' operation " + interpreterError.getMessage());
            //return false;
            logAndThrow("Interpreter error in : '" + nameOperation + "' operation " + interpreterError.getMessage(), interpreterError);
        }
        Tree operation = this.interp.getTree();

        this.state.addOperation(contextClass, nameOperation, returnType, operation, params);
        return true;
    }

    @Override
    public String query(String query) throws Exception {
        if (this.actualState >= 3) {
            try {
                this.lexer = new Lexer(new PushbackReader(new StringReader(query), 1024));
            } catch (Exception e) {
                //JOptionPane.showMessageDialog(null, "Lexical error: " + e.getMessage());
                //throw new Exception("Lexical error: " + e.getMessage());
                logAndThrow("Lexical error: " + e.getMessage(), e);
            }
            try {
                this.parser = new Parser(this.lexer);
                this.ast = this.parser.parse();
            } catch (Exception e) {
                //JOptionPane.showMessageDialog(null, "Parsing error: " + e.getMessage());
                //throw new Exception("Parsing error: " + e.getMessage());
                logAndThrow("Parsing error: " + e.getMessage(), e);
            }

            try {
                this.interp = new Interpreter();
                this.interp.setDebug(1);
                this.ast.apply(this.interp);
                String interpreterError = this.interp.getError();
                if (interpreterError != null) {
                    throw new Exception(interpreterError);
                }
            } catch (Exception interpreterError) {
                //JOptionPane.showMessageDialog(null, "Interpreter error: " + interpreterError.getMessage());
                //throw new Exception("Interpreter error: " + interpreterError.getMessage());
                logAndThrow("Interpreter error: " + interpreterError.getMessage());
            }

            if (this.actualState == 3) {
                this.state.objectDiagramToArray();
//                this.evaluater.initializeCollections();
                this.evaluater.initializeObjectsAndEnumerations();
                this.evaluater.initializeOperations();
            }

            try {
                long time = System.currentTimeMillis();
                this.evaluater.evaluate(this.interp.getTree());
                time = System.currentTimeMillis() - time;

                //System.out.println("Time to evaluate the query: " + transformTime(time));
                //System.out.println("Time to evaluate time1: " + transformTime(this.evaluater.getTime1()));
                //System.out.println("Time to evaluate time2: " + transformTime(this.evaluater.getTime2()));
                //System.out.println("Time to evaluate time3: " + transformTime(this.evaluater.getTime3()));
                //System.out.println("Time to evaluate time4: " + transformTime(this.evaluater.getTime4()));
                //System.out.println("Type of the query: " + this.evaluater.getType());
            } catch (Exception timeException) {
                //JOptionPane.showMessageDialog(null, "Error executing the query: " + timeException.getMessage());
                //System.out.println("Error executing the query: ");

                //timeException.printStackTrace();

                //throw new Exception("");
                timeException.printStackTrace();
                logAndThrow("Error executing the query: " + timeException.getMessage(), timeException);
            }
        } else {
            //JOptionPane.showMessageDialog(null, "The object diagram must be created");
            //throw new Exception("The object diagram must be created");
            logAndThrow("The object diagram must be created");
        }
        String result = this.evaluater.outputFormat(this.evaluater.getResultType(), this.evaluater.getResult());
        result = transformQuery(result);
        queryIntoAFile(result);
        return result;
    }

    public void exit() {
        this.actualState = 0;
        if (this.state != null) {
            this.state.exit();
        }
    }

    public String getVersion() {
        return this.version;
    }

    public int getActualState() {
        return this.actualState;
    }

    public Vector<String> getObjectNamesOfAClass(String className) throws XEOSException {
        int i;
        int j;
        int indexObject;
        Vector names = new Vector();

        int indexClass = this.state.findClass(className);
        if (indexClass == -1) {
            logAndThrow("The class '" + className + "' does not exists");
        }
        if (this.actualState == 4) {
            Object[] objectList = this.state.getAObjectList();
            Object[] vObjects = (Object[]) this.state.getAObjectIndexList()[indexClass];
            for (i = 0; i < vObjects.length; ++i) {
                Object[] subVObjects = (Object[]) vObjects[i];
                for (j = 0; j < subVObjects.length; ++j) {
                    indexObject = ((Integer) subVObjects[j]).intValue();
                    names.add((String) objectList[indexObject]);
                }
            }
        } else {
            Vector objectList = this.state.getObjectList();
            Vector vObjects = (Vector) this.state.getObjectIndexList().get(indexClass);
            for (i = 0; i < vObjects.size(); ++i) {
                Vector subVObjects = (Vector) vObjects.get(i);
                for (j = 0; j < subVObjects.size(); ++j) {
                    indexObject = ((Integer) subVObjects.get(j)).intValue();
                    names.add((String) objectList.get(indexObject));
                }
            }
        }
        return names;
    }

    public Object getValue(String className, String objectName,
            String attributeName) throws XEOSException {
        int indexObject;
        Object value = null;
        int indexClass = this.state.findClass(className);
        if (indexClass == -1) {
            logAndThrow("The class '" + className + "' does not exist");
        }
        Object[] attributeNamesList = this.state.getAAttributeNameList();
        Object[] attributeNameList = (Object[]) attributeNamesList[indexClass];
        int indexAttribute = this.state.find(attributeNameList, attributeName);

        if (this.actualState == 4) {
            indexObject = this.state.findObject(className, objectName);
            if (indexObject == -1) {
                logAndThrow("The object '" + objectName + "' does not exist");
            }
            Object[] valuesList = this.state.getAValueList();
            Object[] valueList = (Object[]) valuesList[indexObject];
            value = valueList[indexAttribute];
        } else {
            indexObject = this.state.findObjectV(className, objectName);
            if (indexObject == -1) {
                logAndThrow("The object '" + objectName + "' does not exist");
            }
            Vector valuesList = this.state.getValueList();
            Vector valueList = (Vector) valuesList.get(indexObject);
            value = valueList.get(indexAttribute);
        }
        return value;
    }

    /*
     * public void loadClassDiagramFromXMI() { new OpenFile(this); }
     */
    private String transformTime(long time) {
        String result = null;
        long rest = 0L;
        if (time < 1000L) {
            result = Long.toString(time) + " milliseconds";
        } else {
            rest = time % 1000L;
            time /= 1000L;
            if (time < 60L) {
                result = Long.toString(time) + " seconds, " + Long.toString(rest) + " milliseconds";
            } else {
                rest = time % 60L;
                time /= 60L;
                if (time < 60L) {
                    result = Long.toString(time) + " minutes, " + Long.toString(rest) + " seconds";
                } else {
                    rest = time % 60L;
                    time /= 60L;
                    result = Long.toString(time) + " hours, " + Long.toString(rest) + " minutes";
                }
            }
        }
        return result;
    }

    private void queryIntoAFile(String result) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("result.txt"));
            out.write(result);
            out.close();
        } catch (IOException localIOException) {
        }
    }

    private String transformQuery(String result) {
        int i = 150;
        while (i < result.length()) {
            if (i < result.length() - 1) {
                if (result.charAt(i - 1) == ',') {
                    result = result.substring(0, i) + '\n' + result.substring(i);
                    i += 150;
                } else {
                    ++i;
                }
            } else {
                ++i;
            }
        }
        return result;
    }
}
