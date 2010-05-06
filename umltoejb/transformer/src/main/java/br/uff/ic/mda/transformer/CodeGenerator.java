package br.uff.ic.mda.transformer;

/**
 * Generate source code to a given domain
 * @author robertowm
 */
public abstract class CodeGenerator<T extends Domain> {

    protected T domain;
    protected String path;

    /**
     * Constructor
     * @param domain    domain utilized during the code generation
     * @param path      path that the files will be created
     */
    public CodeGenerator(T domain, String path) {
        this.domain = domain;
        this.path = path;
    }

    /**
     * Generate the source code
     * @throws Exception
     */
    public abstract void generate() throws Exception;

    /**
     * Validate if a given domain corresponds to this instance's domain
     * @param domain
     * @return if the given domain is the same used by this instance
     */
    public boolean validateDomain(T domain) {
        return this.domain == domain;
    }

    /**
     * Lower the first letter of a given string
     * @param string    given string to lower the first letter
     * @return  a string with the first letter lowered
     */
    protected String lowerFirstLetter(String string) {
        return string.replaceFirst(string.substring(0,1), string.substring(0,1).toLowerCase());
    }

    /**
     * Upper the first letter of a given string
     * @param string    given string to upper the first letter
     * @return  a string with the first letter uppered
     */
    protected String upperFirstLetter(String string) {
        return string.replaceFirst(string.substring(0,1), string.substring(0,1).toUpperCase());
    }

    /**
     * Process the result of a XEOS's query and transform it in a array of strings
     * @param result    the result of a XEOS's query
     * @return  an array with the result of a XEOS's query
     */
    protected String[] processQueryResult(String result) {
        int inicio = result.indexOf("{");
        int fim = result.indexOf("}");

        if (inicio < 0 && fim < 0) {
            return result.replace("{","").replace("}","").replace(" ", "").replace("'","").split(",");
        } else {
            if (inicio < 0) {
                return result.substring(0, fim).replace("{","").replace("}","").replace(" ", "").replace("'","").split(",");
            } else if (fim < 0) {
                return result.substring(inicio+1,result.length()).replace("{","").replace("}","").replace(" ", "").replace("'","").split(",");
            }
        }
        String[] processedResult = result.substring(inicio+1, fim).replace("{","").replace("}","").replace(" ", "").replace("'","").split(",");
        if (processedResult.length == 1 && "".equals(processedResult[0].trim())) {
            return new String[0];
        }
        return processedResult;
    }
}
