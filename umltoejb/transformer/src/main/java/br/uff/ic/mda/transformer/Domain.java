package br.uff.ic.mda.transformer;

import core.XEOS;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Domain represents the metamodel's domain
 * @author robertowm
 */
public abstract class Domain {

    protected Dictionary<String, String> invariants;
    protected XEOS ieos;
    protected static Logger logger = LoggerFactory.getLogger(UmlDomain.class);

    /**
     * Constructor
     *
     * @param ieos instance of XEOS
     * @throws Exception
     */
    public Domain(XEOS ieos) throws Exception {
        this.invariants = new Hashtable<String, String>();
        this.ieos = ieos;
        initializeMetamodel();
    }

    /**
     * Check all invariants of this metamodel and log the result
     *
     * @return if all invariants was checked and passed then 'true' else 'false'
     */
    public final boolean checkAllInvariants() {
        boolean result = true;

        try {
            result = this.checkInvariants();

            logger.info("===========================================");
            logger.info("Check all invariants results:");
            if (result) {
                logger.info("All invariants passed");
            } else {
                logger.error("Some invariants has failed.");
            }
            logger.info("===========================================");
        } catch (Exception e) {
            logger.error("===========================================");
            logger.error("Check invariants");
            logger.error("Exception message: " + e.getMessage());
            logger.error("===========================================");
            return false;
        }
        return result;
    }

    /**
     * Check invariants of this metamodel
     * @return if all invariants was checked and passed then 'true' else 'false'
     */
    protected final boolean checkInvariants() {
        boolean result = true;
        StringBuffer msgReturn = new StringBuffer();
        msgReturn.append("\r\n");

        ArrayList<String> failedInvs = new ArrayList<String>();

        Enumeration<String> invariantsQueries;
        Enumeration<String> invariantsQueriesNames;

        msgReturn.append("===========================================\r\n");
        msgReturn.append("Checking all invariants...\r\n");

        for (invariantsQueries = invariants.elements(), invariantsQueriesNames = invariants.keys(); invariantsQueries.hasMoreElements();) {
            String queryToExec = invariantsQueries.nextElement();
            String invName = invariantsQueriesNames.nextElement();

            msgReturn.append("inv: ");
            msgReturn.append(invName);

            try {
                String queryResult = this.query(queryToExec);

                msgReturn.append(" -- result: ");
                msgReturn.append(queryResult);
                msgReturn.append("\r\n");

                if (logger.isDebugEnabled()) {
                    msgReturn.append("query: ");
                    msgReturn.append(queryToExec);
                    msgReturn.append("\r\n====\r\n");
                }

                if (queryResult.compareTo("false") == 0) {
                    result = false;
                    failedInvs.add(invName);
                }
            } catch (Exception e) {
                msgReturn.append("ERROR: ");
                msgReturn.append(e.getMessage());
            }
        }

        logger.info("===========================================");
        logger.info("Check invariants");
        logger.info(msgReturn.toString());
        logger.info("===========================================");

        logger.info("===========================================");
        logger.info("Check invariants results");
        if (result) {
            logger.info("All invariants passed");
        } else {
            logger.error("Some invariants has failed.");
            logger.info("The following invariants failed:");
            for (String failedInv : failedInvs) {
                logger.info(failedInv + " " + (String) invariants.get(failedInv));
            }
        }
        logger.info("===========================================");

        return result;
    }

    /**
     * Create any specification of this metamodel before insert elements in the current diagram
     * @throws Exception
     */
    public abstract void createSpecificationOfCurrentDiagram() throws Exception;

    /**
     * Initialize the metamodel
     * @throws Exception
     */
    protected final void initializeMetamodel() throws Exception {
        logger.debug("Initializing Diagram...");
//        insertMetamodelStructure();
//        insertMetamodelOperations();
//        insertMetamodelInvariants();
    }

    /**
     * Insert an invariant to this metamodel
     * @param invName   invariant's name
     * @param invBody   invariant's body (code)
     * @return if the invariant was created
     */
    protected final boolean insertInvariant(String invName, String invBody) {
        try {
            this.invariants.put(invName, invBody);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Insert the metamodel's invariants in the XEOS
     * @throws Exception
     */
    public abstract void insertMetamodelInvariants() throws Exception;

    /**
     * Insert the metamodel's operations in the XEOS
     * @return
     * @throws Exception
     */
    public abstract boolean insertMetamodelOperations() throws Exception;

    /**
     * Insert the metamodel's structure in the XEOS
     * @throws Exception
     */
    protected final void insertMetamodelStructure() throws Exception {
//        insertMetamodelClasses();
//        insertMetamodelAssociations();
//        insertMetamodelAttributes();
    }

    /**
     * Insert the metamodel's classes in the XEOS
     * @throws Exception
     */
    public abstract void insertMetamodelClasses() throws Exception;
    /**
     * Insert the metamodel's association in the XEOS
     * @throws Exception
     */
    public abstract void insertMetamodelAssociations() throws Exception;
    /**
     * Insert the metamodel's attributes in the XEOS
     * @throws Exception
     */
    public abstract void insertMetamodelAttributes() throws Exception;

    /**
     * Query the current XEOS
     * @param query     the query to be executed on the XEOS
     * @return the result of the qiery
     * @throws Exception
     */
    public final String query(String query) throws Exception {
        return this.ieos.query(query);
    }
}
