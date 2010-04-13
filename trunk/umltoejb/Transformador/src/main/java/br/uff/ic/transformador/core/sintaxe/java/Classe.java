package br.uff.ic.transformador.core.sintaxe.java;

import br.uff.ic.transformador.core.util.Arquivo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author robertowm
 */
public class Classe implements Persistente {

    private String caminhoPacote;
    private String nome;
    private String visibilidade;
    private boolean abstrata = false;
    private String nomeClasseEstende;
    private List<String> nomesClassesImplementa;
    private List<Atributo> atributos;
    private List<String> caminhosImport;
    private List<Construtor> construtores;
//    private List<Metodo> metodos;

    protected Classe() {
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

        if (abstrata) {
            sb.append("abstract ");
        }

        sb.append("class " + nome);

        if (nomeClasseEstende != null && !"".equals(nomeClasseEstende.trim())) {
            sb.append(" extends " + nomeClasseEstende);
        }

        if (nomesClassesImplementa != null && !nomesClassesImplementa.isEmpty()) {
            sb.append(" implements ");
            for (int i = 0; i < nomesClassesImplementa.size() - 1; i++) {
                sb.append(nomesClassesImplementa.get(i) + ", ");
            }
            sb.append(nomesClassesImplementa.get(nomesClassesImplementa.size()-1));
        }

        sb.append(" {\n\n");

        if (atributos != null) {
            for (Atributo atributo : atributos) {
                sb.append(atributo.serialize() + "\n");
            }
            sb.append("\n");
        }


        if (construtores != null) {
            for (Construtor construtor : construtores) {
                sb.append(construtor.serialize() + "\n");
            }
        }

        sb.append("\n}");
        
        saida.append(sb.toString());
    }

    public String getNome() {
        return this.nome;
    }

    public Classe setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public String getCaminhoPacote() {
        return this.caminhoPacote;
    }

    public Classe setCaminhoPacote(String caminhoPacote) {
        this.caminhoPacote = caminhoPacote;
        return this;
    }

    public boolean isAbstrata() {
        return this.abstrata;
    }

    public Classe setAbstrata(boolean abstrata) {
        this.abstrata = abstrata;
        return this;
    }

    public String getVisibilidade() {
        return this.visibilidade;
    }

    public Classe setVisibilidade(String visibilidade) {
        this.visibilidade = visibilidade;
        return this;
    }

    public String getNomeClasseQueEstende() {
        return this.nomeClasseEstende;
    }

    public Classe setNomeClasseQueEstende(String nomeClasseEstende) {
        this.nomeClasseEstende = nomeClasseEstende;
        return this;
    }

    public List<String> getNomesClasseQueImplementa() {
        return this.nomesClassesImplementa;
    }

    public Classe addNomeClasseQueImplementa(String nomeClasse) {
        if (nomesClassesImplementa == null) {
            nomesClassesImplementa = new ArrayList<String>();
        }
        nomesClassesImplementa.add(nomeClasse);
        return this;
    }

    public List<Atributo> getAtributos() {
        return this.atributos;
    }

    public Classe addAtributo(Atributo atributo) {
        if (atributos == null) {
            atributos = new ArrayList<Atributo>();
        }
        atributos.add(atributo);
        return this;
    }

    public List<String> getImports() {
        return this.caminhosImport;
    }

    public Classe addImport(String caminhoImport) {
        if (caminhosImport == null) {
            caminhosImport = new ArrayList<String>();
        }
        caminhosImport.add(caminhoImport);
        return this;
    }

    public List<Construtor> getConstrutores() {
        return this.construtores;
    }

    public Classe addConstrutor(Construtor construtor) {
        if (construtores == null) {
            construtores = new ArrayList<Construtor>();
        }
        construtores.add(construtor);
        return this;
    }
}
