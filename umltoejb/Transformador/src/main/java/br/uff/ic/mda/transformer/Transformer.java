package br.uff.ic.mda.transformer;

/**
 *
 * @author robertowm
 */
public abstract class Transformer<SourceDomain extends Domain, TargetDomain extends Domain, IntermediateDomain extends Domain> {
    
    protected SourceDomain sourceDomain;
    protected TargetDomain targetDomain;
    protected IntermediateDomain intermediateDomain;

    public Transformer(SourceDomain sourceDomain, TargetDomain targetDomain, IntermediateDomain intermediateDomain) {
        this.sourceDomain = sourceDomain;
        this.targetDomain = targetDomain;
        this.intermediateDomain = intermediateDomain;
    }

    public boolean validaDominios(SourceDomain sourceDomain, TargetDomain targetDomain, IntermediateDomain intermediateDomain) {
        return this.sourceDomain == sourceDomain && this.targetDomain == targetDomain && this.intermediateDomain == intermediateDomain;
    }

    public abstract void transform() throws Exception;

    protected String lowerFirstLetter(String s) {
        return s.replaceFirst(s.substring(0,1), s.substring(0,1).toLowerCase());
    }

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
