package br.uff.ic.mda.transformador;

/**
 *
 * @author robertowm
 */
public abstract class GeradorCodigo<T extends Dominio> {

    protected T dominio;
    protected String caminho;

    public GeradorCodigo(T dominio, String caminho) {
        this.dominio = dominio;
        this.caminho = caminho;
    }

    public abstract void generate() throws Exception;

    public boolean validaDominio(T dominio) {
        return this.dominio == dominio;
    }

    protected String lowerFirstLetter(String s) {
        return s.replaceFirst(s.substring(0,1), s.substring(0,1).toLowerCase());
    }

    protected String upperFirstLetter(String s) {
        return s.replaceFirst(s.substring(0,1), s.substring(0,1).toUpperCase());
    }

    protected String[] tratarResultadoQuery(String resultado) {
        int inicio = resultado.indexOf("{");
        int fim = resultado.indexOf("}");

        if (inicio < 0 && fim < 0) {
            return resultado.replace("{","").replace("}","").replace(" ", "").replace("'","").split(",");
        } else {
            if (inicio < 0) {
                return resultado.substring(0, fim).replace("{","").replace("}","").replace(" ", "").replace("'","").split(",");
            } else if (fim < 0) {
                return resultado.substring(inicio+1,resultado.length()).replace("{","").replace("}","").replace(" ", "").replace("'","").split(",");
            }
        }
        String[] result = resultado.substring(inicio+1, fim).replace("{","").replace("}","").replace(" ", "").replace("'","").split(",");
        if (result.length == 1 && "".equals(result[0].trim())) {
            return new String[0];
        }
        return result;
    }
}
