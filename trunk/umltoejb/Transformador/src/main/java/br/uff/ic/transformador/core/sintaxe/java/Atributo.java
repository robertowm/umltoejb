package br.uff.ic.transformador.core.sintaxe.java;

/**
 *
 * @author robertowm
 */
public class Atributo implements NaoPersistente {

    private String visibilidade;
    private boolean estatico;
    private boolean constante;
    private String tipo;
    private String nome;
    private String valor;

    protected Atributo() {
        this(null, false, false, null, null, null);
    }

    protected Atributo(String visibilidade, String tipo, String nome) {
        this(visibilidade, false, false, tipo, nome, null);
    }

    protected Atributo(String visibilidade, String tipo, String nome, String valor) {
        this(visibilidade, false, false, tipo, nome, valor);
    }

    protected Atributo(String visibilidade, boolean estatico, boolean constante, String tipo, String nome, String valor) {
        this.visibilidade = visibilidade;
        this.estatico = estatico;
        this.constante = constante;
        this.tipo = tipo;
        this.nome = nome;
        this.valor = valor;
    }

    public String getVisibilidade() {
        return this.visibilidade;
    }

    public Atributo setVisibilidade(String visibilidade) {
        this.visibilidade = visibilidade;
        return this;
    }

    public boolean isEstatico() {
        return this.estatico;
    }

    public Atributo setEstatico(boolean estatico) {
        this.estatico = estatico;
        return this;
    }

    public boolean isConstante() {
        return this.constante;
    }

    public Atributo setConstante(boolean constante) {
        this.constante = constante;
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
        return setNome(nome, false);
    }

    public Atributo setNome(String nome, boolean lowerFirstLetter) {
        this.nome = (lowerFirstLetter) ? lowerFirstLetter(nome) : nome;
        return this;
    }

    public String getValor() {
        return this.valor;
    }

    public Atributo setValor(String valor) {
        this.valor = valor;
        return this;
    }

    protected String lowerFirstLetter(String s) {
        return s.replaceFirst(s.substring(0,1), s.substring(0,1).toLowerCase());
    }

    @Override
    public StringBuffer serialize() {
        StringBuffer sb = new StringBuffer();
        sb.append("\t");
        if (visibilidade != null && !"".equals(visibilidade.trim())) {
            sb.append(visibilidade + " ");
        }
        if (estatico) {
            sb.append("static ");
        }
        if (constante) {
            sb.append("final ");
        }
        sb.append(tipo + " " + nome);
        if (valor != null) {
            sb.append(" = " + valor);
        }
        sb.append(";");
        return sb;
    }

}
