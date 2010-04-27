package br.uff.ic.mda.transformer;

import core.XEOS;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author robertowm
 */
public abstract class Domain {

    protected Dictionary<String, String> invariants;
    protected XEOS ieos;
    protected static Logger logger = LoggerFactory.getLogger(UmlDomain.class);

    public Domain(XEOS ieos) throws Exception {
        this.invariants = new Hashtable<String, String>();
        this.ieos = ieos;
        initializeMetamodel();
    }

    public final boolean checkAllInvariants() {
        boolean result = true;
        try {
            result = this.checkInvariants();

            System.out.println("===========================================");
            System.out.println("Check all invariants results:");
            if (result) {
                System.out.println("All invariants passed");
            } else {
                System.out.println("Some invariants has failed.");
            }
            logger.info("===========================================");
        } catch (Exception e) {
            System.out.println("===========================================");
            System.out.println("Check invariants");
            System.out.println("Exception message: " + e.getMessage());
            System.out.println("===========================================");
            return false;
        }
        return result;
//        boolean result = true;
//
//        try {
//            result = this.checkInvariants();
//
//            logger.info("===========================================");
//            logger.info("Check all invariants results:");
//            if (result) {
//                logger.info("All invariants passed");
//            } else {
//                logger.error("Some invariants has failed.");
//            }
//            logger.info("===========================================");
//        } catch (Exception e) {
//            logger.error("===========================================");
//            logger.error("Check invariants");
//            logger.error("Exception message: " + e.getMessage());
//            logger.error("===========================================");
//            return false;
//        }
//        return result;
    }

    protected final boolean checkInvariants() {
        boolean result = true;
        String msgResult = "\r\n";

        ArrayList<String> failedInvs = new ArrayList<String>();

        Enumeration<String> invariantsQueries;
        Enumeration<String> invariantsQueriesNames;

        msgResult += "===========================================" + "\r\n";
        msgResult += "Checking all invariants...\r\n";

        for (invariantsQueries = invariants.elements(), invariantsQueriesNames = invariants.keys(); invariantsQueries.hasMoreElements();) {
            String queryToExec = invariantsQueries.nextElement();
            String invName = invariantsQueriesNames.nextElement();

            msgResult += "inv: " + invName;

            try {
                String queryResult = this.query(queryToExec);

                msgResult += " -- result: " + queryResult + "\r\n";

                if (logger.isDebugEnabled()) {
                    msgResult += "query: " + queryToExec + "\r\n" + "====" + "\r\n";
                }

                if (queryResult.compareTo("false") == 0) {
                    result = false;
                    failedInvs.add(invName);
                }
            } catch (Exception e) {
                msgResult += "ERROR: " + e.getMessage();
            }
        }

        System.out.println("===========================================");
        System.out.println("Check invariants");
        System.out.println(msgResult);
        System.out.println("===========================================");

        System.out.println("===========================================");
        System.out.println("Check invariants results");
        if (result) {
            System.out.println("All invariants passed");
        } else {
            System.out.println("Some invariants has failed.");
            System.out.println("The following invariants failed:");
            for (String failedInv : failedInvs) {
                System.out.println(failedInv + " " + (String) invariants.get(failedInv));
            }
        }
        System.out.println("===========================================");
//        logger.info("===========================================");
//        logger.info("Check invariants");
//        logger.info(msgResult);
//        logger.info("===========================================");
//
//        logger.info("===========================================");
//        logger.info("Check invariants results");
//        if (result) {
//            logger.info("All invariants passed");
//        } else {
//            logger.error("Some invariants has failed.");
//            logger.info("The following invariants failed:");
//            for (String failedInv : failedInvs) {
//                logger.info(failedInv + " " + (String) invariants.get(failedInv));
//            }
//        }
//        logger.info("===========================================");

        return result;
    }

    public abstract void createSpecificationOfCurrentDiagram() throws Exception;

    public final void exit() {
        this.ieos.exit();
    }

    public final String getVersion() {
        return this.ieos.getVersion();
    }

    protected final void initializeMetamodel() throws Exception {
        logger.debug("Initializing Diagram...");
//        insertMetamodelStructure();
//        insertMetamodelOperations();
//        insertMetamodelInvariants();
    }

    protected final boolean insertInvariant(String invName, String invBody) {
        try {
            this.invariants.put(invName, invBody);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public abstract void insertMetamodelInvariants() throws Exception;

    public abstract boolean insertMetamodelOperations() throws Exception;

    protected final void insertMetamodelStructure() throws Exception {
//        insertMetamodelClasses();
//        insertMetamodelAssociations();
//        insertMetamodelAttributes();
    }

    public abstract void insertMetamodelClasses() throws Exception;
    public abstract void insertMetamodelAssociations() throws Exception;
    public abstract void insertMetamodelAttributes() throws Exception;

    public final String query(String query) throws Exception {
        return this.ieos.query(query);
    }

    protected final void queryIntoAFile(String result) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("result.txt"));
            out.write(result);
            out.close();
        } catch (IOException localIOException) {
        }
    }

    protected final void queryIntoAFile(String result, String nameFile) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(nameFile));
            out.write(result);
            out.close();
        } catch (IOException localIOException) {
        }
    }

    protected final String transformQuery(String result) {
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

    protected final String transformTime(long time) {
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
}
