package br.uff.ic.transformador.core.sintaxe.java;

/**
 *
 * @author robertowm
 */
public class Atributo implements NaoPersistente {

    private String visibilidade;
    private String tipo;
    private String nome;

    protected Atributo() {
    }

    protected Atributo(String visibilidade, String tipo, String nome) {
        this.visibilidade = visibilidade;
        this.tipo = tipo;
        this.nome = nome;
    }

    public String getVisibilidade() {
        return this.visibilidade;
    }

    public Atributo setVisibilidade(String visibilidade) {
        this.visibilidade = visibilidade;
        return this;
    }

    public String getTipo() {
        return this.tipo;
    }

    public Atributo setTipo(String tipo) {
        this.tipo = tipo;
        return this;
    }

    public String getNome() {
        return this.nome;
    }

    public Atributo setNome(String nome) {
        this.nome = nome;
        return this;
    }

    protected String lowerFirstLetter(String s) {
        return s.replaceFirst(s.substring(0,1), s.substring(0,1).toLowerCase());
    }

    @Override
    public StringBuffer serialize() {
        StringBuffer sb = new StringBuffer();
        if (visibilidade != null && "".equals(visibilidade.trim())) {
            sb.append(visibilidade + " ");
        }
        sb.append(tipo + " " + lowerFirstLetter(nome) + ";");
        return sb;
    }

}
