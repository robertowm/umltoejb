package br.uff.ic.transformador.core.sintaxe.java;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author robertowm
 */
public class Metodo implements NaoPersistente {

    private String visibilidade;
    private boolean estatico;
    private boolean overridable;
    private String retorno;
    private String nome;
    private List<Parametro> parametros;
    private List<String> excecoes;
    private String[] codigo;

    protected Metodo() {
    }

    protected Metodo(String visibilidade, String tipo, String nome) {
        this.visibilidade = visibilidade;
        this.retorno = tipo;
        this.nome = nome;
    }

    public String getVisibilidade() {
        return this.visibilidade;
    }

    public Metodo setVisibilidade(String visibilidade) {
        this.visibilidade = visibilidade;
        return this;
    }

    public boolean getEstatico() {
        return estatico;
    }

    public Metodo setEstatico(boolean estatico) {
        this.estatico = estatico;
        return this;
    }

    public boolean getOverridable() {
        return overridable;
    }

    public Metodo setOverridable(boolean overridable) {
        this.overridable = overridable;
        return this;
    }

    public String getRetorno() {
        return retorno;
    }

    public Metodo setRetorno(String retorno) {
        this.retorno = retorno;
        return this;
    }

    public String getNome() {
        return this.nome;
    }

    public Metodo setNome(String nome) {
        return setNome(nome, false);
    }

    public Metodo setNome(String nome, boolean lowerFirstLetter) {
        this.nome = (lowerFirstLetter) ? lowerFirstLetter(nome) : nome;
        return this;
    }

    protected String lowerFirstLetter(String s) {
        return s.replaceFirst(s.substring(0,1), s.substring(0,1).toLowerCase());
    }

    public List<Parametro> getParametros() {
        return parametros;
    }

    public Metodo addParametro(Parametro parametro) {
        if (parametros == null) {
            parametros = new ArrayList<Parametro>();
        }
        parametros.add(parametro);
        return this;
    }

    public Metodo addParametro(String tipo, String nome) {
        if (parametros == null) {
            parametros = new ArrayList<Parametro>();
        }
        parametros.add(new Parametro(tipo, nome));
        return this;
    }

    public String[] getCodigo() {
        return codigo;
    }

    public Metodo setCodigo(String[] codigo) {
        this.codigo = codigo;
        return this;
    }

    public List<String> getExcecoes() {
        return excecoes;
    }

    public Metodo addExcecao(String excecao) {
        if (excecoes == null) {
            excecoes = new ArrayList<String>();
        }
        excecoes.add(excecao);
        return this;
    }

    @Override
    public StringBuffer serialize() {
        StringBuffer sb = new StringBuffer();
        sb.append("\t");
        if (visibilidade != null && !"".equals(visibilidade.trim())) {
            sb.append(visibilidade + " ");
        }
        if (estatico) {

        }
        if (!overridable) {

        }

        sb.append(retorno + " " + nome);

        sb.append("(");
        if (parametros != null && !parametros.isEmpty()) {
            for (int i = 0; i < parametros.size() - 1; i++) {
                sb.append(parametros.get(i).serialize() + ", ");
            }
            sb.append(parametros.get(parametros.size() - 1).serialize());
        }
        sb.append(")");

        if(excecoes != null && !excecoes.isEmpty()) {
            sb.append(" throws ");
            for (int i = 0; i < excecoes.size() - 1; i++) {
                sb.append(excecoes.get(i) + ", ");
            }
            sb.append(excecoes.get(excecoes.size() - 1));
        }

        if (codigo != null && codigo.length > 0) {
            sb.append(" {\n");
            for (int i = 0; i < codigo.length; i++) {
                sb.append("\t\t" + codigo[i] + "\n");
            }
            sb.append("\t}");
        } else {
            sb.append(";");
        }
        return sb;
    }
}
