package br.uff.ic.transformador.core.sintaxe.java;

import br.uff.ic.transformador.core.util.Arquivo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author robertowm
 */
public class Interface implements Persistente {

    private String caminhoPacote;
    private String nome;
    private String visibilidade;
    private List<String> nomesClassesEstende;
    private List<Atributo> constantes;
    private List<String> caminhosImport;
    private List<Metodo> metodos;

    protected Interface() {
    }

    @Override
    public void persiste() {
        Arquivo saida = new Arquivo(nome + ".java");

        StringBuffer sb = new StringBuffer();

        if (caminhoPacote != null && !"".equals(caminhoPacote.trim())) {
            sb.append("package " + caminhoPacote + ";\n\n");
        }

        if (caminhosImport != null && !caminhosImport.isEmpty()) {
            for (String string : caminhosImport) {
                sb.append("import " + string + ";\n");
            }
            sb.append("\n");
        }

        if (visibilidade != null && !"".equals(visibilidade.trim())) {
            sb.append(visibilidade + " ");
        }

        sb.append("interface " + nome);

        if (nomesClassesEstende != null && !nomesClassesEstende.isEmpty()) {
            sb.append(" extends ");
            for (int i = 0; i < nomesClassesEstende.size() - 1; i++) {
                sb.append(nomesClassesEstende.get(i) + ", ");
            }
            sb.append(nomesClassesEstende.get(nomesClassesEstende.size() - 1));
        }

        sb.append(" {\n\n");

        if (constantes != null) {
            for (Atributo atributo : constantes) {
                sb.append(atributo.serialize() + "\n");
            }
            sb.append("\n");
        }

        if (metodos != null) {
            for (Metodo metodo : metodos) {
                sb.append(metodo.serialize() + "\n\n");
            }
        }

        sb.append("\n}");

        saida.append(sb.toString());
    }

    public String getNome() {
        return this.nome;
    }

    public Interface setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public String getCaminhoPacote() {
        return this.caminhoPacote;
    }

    public Interface setCaminhoPacote(String caminhoPacote) {
        this.caminhoPacote = caminhoPacote;
        return this;
    }

    public String getVisibilidade() {
        return this.visibilidade;
    }

    public Interface setVisibilidade(String visibilidade) {
        this.visibilidade = visibilidade;
        return this;
    }

    public List<String> getNomesClasseQueEstende() {
        return this.nomesClassesEstende;
    }

    public Interface addNomeClasseQueEstende(String nomeClasse) {
        if (nomesClassesEstende == null) {
            nomesClassesEstende = new ArrayList<String>();
        }
        nomesClassesEstende.add(nomeClasse);
        return this;
    }

    public List<Atributo> getAtributos() {
        return this.constantes;
    }

    public Interface addAtributo(Atributo atributo) {
        if (constantes == null) {
            constantes = new ArrayList<Atributo>();
        }
        atributo.setVisibilidade("public");
        constantes.add(atributo);
        return this;
    }

    public List<String> getImports() {
        return this.caminhosImport;
    }

    public Interface addImport(String caminhoImport) {
        if (caminhosImport == null) {
            caminhosImport = new ArrayList<String>();
        }
        caminhosImport.add(caminhoImport);
        return this;
    }

    public List<Metodo> getMetodos() {
        return this.metodos;
    }

    public Interface addMetodo(Metodo metodo) {
        if (metodos == null) {
            metodos = new ArrayList<Metodo>();
        }
        metodos.add(metodo);
        return this;
    }

    public Interface addMetodo(String tipo, String nome, Parametro... parametros) {
        Metodo metodo = new Metodo().setVisibilidade("public").setRetorno(tipo).setNome(nome, true);
        for (Parametro parametro : parametros) {
            metodo.addParametro(parametro);
        }
        return this.addMetodo(metodo);


    }
}
