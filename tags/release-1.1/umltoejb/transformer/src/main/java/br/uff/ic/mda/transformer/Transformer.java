package br.uff.ic.mda.transformer;

/**
 * Represents the transformer from a souce domain to a target domain
 * @author robertowm
 */
public abstract class Transformer<SourceDomain extends Domain, TargetDomain extends Domain, JoinedDomain extends Domain> {
    
    protected SourceDomain sourceDomain;
    protected TargetDomain targetDomain;
    protected JoinedDomain joinedDomain;

    /**
     * Constructor
     * @param sourceDomain  source domain of the transformer
     * @param targetDomain  target domain of the transformer
     * @param joinedDomain  joined domain of the transformer
     */
    public Transformer(SourceDomain sourceDomain, TargetDomain targetDomain, JoinedDomain joinedDomain) {
        this.sourceDomain = sourceDomain;
        this.targetDomain = targetDomain;
        this.joinedDomain = joinedDomain;
    }

    /**
     * Validade if the domains in the parameters are equals to this instance's domains
     * @param sourceDomain  source domain to validate
     * @param targetDomain  target domain to validate
     * @param joinedDomain  joined domain to validate
     * @return
     */
    public boolean validateDomains(SourceDomain sourceDomain, TargetDomain targetDomain, JoinedDomain joinedDomain) {
        return this.sourceDomain == sourceDomain && this.targetDomain == targetDomain && this.joinedDomain == joinedDomain;
    }

    /**
     * Do the transformation, applying the transformation rules
     * @throws Exception
     */
    public abstract void transform() throws Exception;

    /**
     * Lower the first letter of a given string
     * @param string    given string to lower the first letter
     * @return  a string with the first letter lowered
     */
    protected String lowerFirstLetter(String string) {
        return string.replaceFirst(string.substring(0,1), string.substring(0,1).toLowerCase());
    }

    /**
     * Process the result of a XEOS's query and transform it in a array of strings
     * @param result    the result of a XEOS's query
     * @return  an array with the result of a XEOS's query
     */
    protected String[] processQueryResult(String result) {
        result = result.replace("\n", "");
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
        if (processedResult != null && processedResult.length == 1 && "".equals(processedResult[0])) {
            return new String[0];
        }
        return processedResult;
    }
}
