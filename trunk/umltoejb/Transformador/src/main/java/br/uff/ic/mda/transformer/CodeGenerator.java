package br.uff.ic.mda.transformer;

/**
 *
 * @author robertowm
 */
public abstract class CodeGenerator<T extends Domain> {

    protected T domain;
    protected String path;

    public CodeGenerator(T domain, String path) {
        this.domain = domain;
        this.path = path;
    }

    public abstract void generate() throws Exception;

    public boolean validateDomain(T domain) {
        return this.domain == domain;
    }

    protected String lowerFirstLetter(String s) {
        return s.replaceFirst(s.substring(0,1), s.substring(0,1).toLowerCase());
    }

    protected String upperFirstLetter(String s) {
        return s.replaceFirst(s.substring(0,1), s.substring(0,1).toUpperCase());
    }

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
