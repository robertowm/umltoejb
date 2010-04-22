package br.uff.ic.transformador.core.sintaxe.java;

/**
 *
 * @author robertowm
 */
public class Parametro implements NaoPersistente {

    private String tipo;
    private String nome;

    protected Parametro() {
        this(null, null);
    }

    protected Parametro(String tipo, String nome) {
        this.tipo = tipo;
        this.nome = nome;
    }

    public String getTipo() {
        return this.tipo;
    }

    public Parametro setTipo(String tipo) {
        this.tipo = tipo;
        return this;
    }

    public String getNome() {
        return this.nome;
    }

    public Parametro setNome(String nome) {
        return setNome(nome, false);
    }

    public Parametro setNome(String nome, boolean lowerFirstLetter) {
        this.nome = (lowerFirstLetter) ? lowerFirstLetter(nome) : nome;
        return this;
    }

    protected String lowerFirstLetter(String s) {
        return s.replaceFirst(s.substring(0,1), s.substring(0,1).toLowerCase());
    }

    @Override
    public StringBuffer serialize() {
        StringBuffer sb = new StringBuffer();
        sb.append(tipo + " " + nome);
        return sb;
    }

}
