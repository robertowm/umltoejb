package br.uff.ic.transformador.core.sintaxe.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Construtor {

    private List<Atributo> parametros;
    private String codigo;
    private String nomeClasse;

    public Construtor(String nomeClasse, String codigo, Atributo... parametros) {
        this(nomeClasse, codigo, Arrays.asList(parametros));
    }

    public Construtor(String nomeClasse, String codigo, List<Atributo> atributos) {
        this.nomeClasse = nomeClasse;
        this.codigo = codigo;
        this.parametros = atributos;
    }

    public List<Atributo> getParametros() {
        return this.parametros;
    }

    public Construtor addParametro(Atributo parametro) {
        if (parametros == null) {
            parametros = new ArrayList<Atributo>();
        }
        parametros.add(parametro);
        return this;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public Construtor setCodigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    public String getNomeClasse() {
        return this.nomeClasse;
    }

    public Construtor setNomeClasse(String nomeClasse) {
        this.nomeClasse = nomeClasse;
        return this;
    }

    public StringBuffer serialize() {
        StringBuffer sb = new StringBuffer();
        sb.append("\tpublic " + nomeClasse);
        if (parametros != null && !parametros.isEmpty()) {
            sb.append("(");
            for (int i = 0; i < parametros.size() - 1; i++) {
                sb.append(parametros.get(i).getTipo() + " " + parametros.get(i).getNome() + ", ");
            }
            sb.append(parametros.get(parametros.size()-1).getTipo() + " " + parametros.get(parametros.size()-1).getNome() + ")");
        } else {
            sb.append("()");
        }
        sb.append(" {\n");
        for (String string : codigo.split("\n")) {
            sb.append("\t\t" + string + "\n");
        }
        sb.append("\t}\n");
        return sb;
    }
}
