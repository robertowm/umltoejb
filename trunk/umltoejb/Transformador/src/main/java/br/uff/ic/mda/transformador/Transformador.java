package br.uff.ic.mda.transformador;

/**
 *
 * @author robertowm
 */
public abstract class Transformador<DomOr extends Dominio, DomDest extends Dominio, DomJuncao extends Dominio> {
    
    protected DomOr origem;
    protected DomDest destino;
    protected DomJuncao juncao;

    public Transformador(DomOr origem, DomDest destino, DomJuncao juncao) {
        this.origem = origem;
        this.destino = destino;
        this.juncao = juncao;
    }

    public boolean validaDominios(DomOr origem, DomDest destino, DomJuncao juncao) {
        return this.origem == origem && this.destino == destino && this.juncao == juncao;
    }

    public abstract void transform() throws Exception;

    protected String lowerFirstLetter(String s) {
        return s.replaceFirst(s.substring(0,1), s.substring(0,1).toLowerCase());
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
        return resultado.substring(inicio+1, fim).replace("{","").replace("}","").replace(" ", "").replace("'","").split(",");
    }

}
