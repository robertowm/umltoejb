package br.uff.ic.mda.transformador;

import br.uff.ic.transformador.core.sintaxe.java.Atributo;
import br.uff.ic.transformador.core.sintaxe.java.Classe;
import br.uff.ic.transformador.core.sintaxe.java.Construtor;
import br.uff.ic.transformador.core.sintaxe.java.Interface;
import br.uff.ic.transformador.core.sintaxe.java.Metodo;
import br.uff.ic.transformador.core.sintaxe.java.Parametro;
import br.uff.ic.transformador.core.sintaxe.java.SintaxeJava;

/**
 *
 * @author robertowm
 */
public class GeradorCodigoEjb extends GeradorCodigo<DominioEjb> {

    public GeradorCodigoEjb(DominioEjb dominio, String caminho) {
        super(dominio, caminho);
    }

    @Override
    public void generate() throws Exception {
        gerarEJBKeyClass();
        gerarEJBEntityComponent();
        gerarEJBDataClass();

    }

    private String converterTipoEjbParaJava(String tipoEjb) throws Exception {
        if ("EJBInteger".equals(tipoEjb)) {
            return "Integer";
        }
        if ("EJBDouble".equals(tipoEjb)) {
            return "Double";
        }
        if ("EJBString".equals(tipoEjb)) {
            return "String";
        }
        if ("EJBDate".equals(tipoEjb)) {
            return "Date";
        }
        if ("EJBBoolean".equals(tipoEjb)) {
            return "Boolean";
        }
        if ("true".equals(this.dominio.query(tipoEjb + ".oclIsTypeOf(EJBDataClass)").replace("'", ""))) {
            return this.dominio.query(tipoEjb + ".name").replace("'", "") + "DataObject";
        }
        return this.dominio.query(tipoEjb + ".name").replace("'", "");
    }

    private void gerarEJBKeyClass() throws Exception {
        String query = this.dominio.query("EJBKeyClass.allInstances()");
        String[] ids = this.tratarResultadoQuery(query);

        for (String id : ids) {
            try {
                String idClasse = id.replace("'", "").trim();

                Classe classe = SintaxeJava.getJavaClass();

                query = idClasse + ".name";

                classe.setNome(this.dominio.query(query).replace("'", ""));
                classe.addNomeClasseQueImplementa("java.io.Serializable");
                classe.addConstrutor(new Construtor(classe.getNome(), ""));
                classe.setVisibilidade("public");

                query = "EJBAttribute.allInstances()->select(attr | attr.class->includes(" + idClasse + "))->asSet()";
                String[] idsAttr = this.tratarResultadoQuery(this.dominio.query(query));

                for (String idAttr : idsAttr) {
                    System.out.println("idAttr = " + idAttr);
                    String idAtributo = idAttr.replace("'", "").trim();
                    Atributo atributo = SintaxeJava.getJavaAttribute();
//                atributo.setVisibilidade(this.dominio.query(idAttr + ".visibility").replace("'", ""));
                    atributo.setVisibilidade("public");
                    query = idAtributo + ".type->asOrderedSet()->first().name";
//                    query = idAtributo + ".type.name";
                    atributo.setTipo(converterTipoEjbParaJava(tratarResultadoQuery(this.dominio.query(query))[0]));
                    query = idAtributo + ".name";
                    atributo.setNome(tratarResultadoQuery(this.dominio.query(query))[0], true);

                    classe.addAtributo(atributo);
                }

                StringBuffer codigo = new StringBuffer();
                for (Atributo atributo : classe.getAtributos()) {
                    codigo.append("this." + atributo.getNome() + " = " + atributo.getNome() + ";\n");
                }

                Construtor construtor = SintaxeJava.getJavaConstructor().setNomeClasse(classe.getNome()).setCodigo(codigo.toString());
                for (Atributo atributo : classe.getAtributos()) {
                    construtor.addParametro(SintaxeJava.getJavaParameter().setNome(atributo.getNome()).setTipo(atributo.getTipo()));
                }
                classe.addConstrutor(construtor);

                classe.persiste();
            } catch (Exception ex) {
                System.out.println("-------------------------------------------");
                ex.printStackTrace();
                System.out.println("-------------------------------------------");
            }
        }
    }

    private void gerarEJBEntityComponent() throws Exception {
        String[] ids = tratarResultadoQuery(this.dominio.query("UMLClassToEJBEntityComponent.allInstances()").replace("\n", ""));

        for (String id : ids) {
            try {

                Interface interf = SintaxeJava.getJavaInterface();

                interf.addImport("java.rmi.*");
                interf.addImport("javax.naming.*");
                interf.addImport("javax.ejb.*");
                interf.setNome(tratarResultadoQuery(this.dominio.query(id + ".entityComponent.name"))[0]);
                interf.addNomeClasseQueEstende("EJBObject");
                interf.setVisibilidade("public");

                // Insere getter e setter para o DataClass
                String idDataClass = this.tratarResultadoQuery(this.dominio.query(id + ".dataClass"))[0];
                String nomeDataClass = this.dominio.query(idDataClass + ".name").replace("'", "");
                Metodo metodoGet = SintaxeJava.getJavaMethod();
                metodoGet.setNome("get" + nomeDataClass).setRetorno(nomeDataClass + "DataObject").setVisibilidade("public").addExcecao("RemoteException");
                interf.addMetodo(metodoGet);

                Metodo metodoSet = SintaxeJava.getJavaMethod();
                metodoSet.setNome("set" + nomeDataClass).setRetorno("void").setVisibilidade("public").addParametro(nomeDataClass + "DataObject", "update").addExcecao("NamingException").addExcecao("FinderException").addExcecao("CreateException").addExcecao("RemoteException");
                interf.addMetodo(metodoSet);

                String[] idsBusinessMethod = tratarResultadoQuery(this.dominio.query(id + ".entityComponent->asOrderedSet()->first().feature->select(f : EJBFeature | f.oclIsTypeOf(BusinessMethod))"));
                for (String idBusinessMethod : idsBusinessMethod) {
                    Metodo metodo = SintaxeJava.getJavaMethod();
                    metodo.setVisibilidade("public").setRetorno(converterTipoEjbParaJava(tratarResultadoQuery(this.dominio.query(idBusinessMethod + ".type"))[0])).setNome(this.dominio.query(idBusinessMethod + ".name").replace("'", "")).addExcecao("RemoteException");

                    String[] idsParametro = tratarResultadoQuery(this.dominio.query(idBusinessMethod + ".parameter"));
                    for (String idParametro : idsParametro) {
                        Parametro parametro = SintaxeJava.getJavaParameter();
                        parametro.setNome(this.dominio.query(idParametro + ".name").replace("'", ""), true).setTipo(converterTipoEjbParaJava(tratarResultadoQuery(this.dominio.query(idParametro + ".type"))[0]));
                        metodo.addParametro(parametro);
                    }
                    interf.addMetodo(metodo);
                }

                interf.persiste();
            } catch (Exception ex) {
                System.out.println("-------------------------------------------");
                ex.printStackTrace();
                System.out.println("-------------------------------------------");
            }
        }
    }

    private void gerarEJBDataClass() throws Exception {
        String[] ids = tratarResultadoQuery(this.dominio.query("EJBDataClass.allInstances()").replace("\n", ""));

        for (String id : ids) {
            try {

                Classe classe = SintaxeJava.getJavaClass();

//                classe.addImport("java.rmi.*");
//                classe.addImport("javax.naming.*");
//                classe.addImport("javax.ejb.*");
                classe.addImport("java.util.*");
                classe.setNome(this.dominio.query(id + ".name").replace("'", "") + "DataObject");
                classe.setNomeClasseQueEstende("DataObject");
                classe.addNomeClasseQueImplementa("java.io.Serializable");
                classe.setVisibilidade("public");

                // Adicionando atributo 'key'
                String idUmlClass = this.dominio.query("Class.allInstances()->select(c | c.name = " + id + ".name)->asOrderedSet()->first()");
                String idKeyClass = tratarResultadoQuery(this.dominio.query("if " + idUmlClass + ".oclIsTypeOf(Class) then UMLClassToEJBKeyClass.allInstances()->select(a | a.class->includes(" + idUmlClass + ")).keyClass else UMLAssociationClassToEJBKeyClass.allInstances()->select(a | a.associationClass->includes(" + idUmlClass + ")).keyClass endif"))[0];
                String nomeKeyClass = this.dominio.query(idKeyClass + ".name").replace("'", "");
                Atributo key = SintaxeJava.getJavaAttribute().setNome("key").setTipo(this.dominio.query(idKeyClass + ".name").replace("'", "")).setVisibilidade("private");
                classe.addAtributo(key);

                // Adicionando construtor com o 'key' como parametro
                Construtor construtor = SintaxeJava.getJavaConstructor();
                construtor.setNomeClasse(classe.getNome()).addParametro(SintaxeJava.getJavaParameter().setNome("key").setTipo(this.dominio.query(idKeyClass + ".name").replace("'", "")));
                construtor.setCodigo("this.key = key;");
                classe.addConstrutor(construtor);

                // Adicionando getter para 'key'
                Metodo getterKey = SintaxeJava.getJavaMethod();
                getterKey.setVisibilidade("public").setRetorno(nomeKeyClass).setNome("get" + upperFirstLetter(nomeKeyClass)).setCodigo(new String[]{"return key;"});
                classe.addMetodo(getterKey);

                // Insere relacionamentos (AssociationEnd) com getters e setters
                String[] idsAssocEnds = null;
                // 1) upper = '1'
                idsAssocEnds = tratarResultadoQuery(this.dominio.query(id + ".feature->select(f : EJBFeature | f.oclIsTypeOf(EJBAssociationEnd))->collect(f : EJBFeature | f.oclAsType(EJBAssociationEnd))->select(ae : EJBAssociationEnd | ae.upper = '1')"));
                for (String idAssocEnd : idsAssocEnds) {
                    String nome = this.dominio.query(idAssocEnd + ".name").replace("'", "") + tratarResultadoQuery(this.dominio.query(idAssocEnd + ".type.name"))[0];
                    boolean isDataClass = "true".equals(this.dominio.query(idAssocEnd + ".type.oclIsTypeOf(EJBDataClass)").replace("'", ""));
                    String tipo = isDataClass ? tratarResultadoQuery(this.dominio.query(idAssocEnd + ".type.name"))[0] + "DataObject" : tratarResultadoQuery(this.dominio.query(idAssocEnd + ".type.name"))[0];
                    Atributo atrib = SintaxeJava.getJavaAttribute().setVisibilidade("private").setTipo(tipo).setNome(nome);
                    classe.addAtributo(atrib);

                    Metodo getter = SintaxeJava.getJavaMethod();
                    getter.setNome("get" + upperFirstLetter(nome)).setRetorno(tipo).setVisibilidade("public").setCodigo(new String[]{"return " + nome + ";"});
                    classe.addMetodo(getter);

                    Metodo setter = SintaxeJava.getJavaMethod();
                    setter.setNome("set" + upperFirstLetter(nome)).setRetorno("void").setVisibilidade("public").addParametro(tipo, nome).setCodigo(new String[]{"this. " + nome + " = " + nome + ";"});
                    classe.addMetodo(setter);

                }
                // 2) upper <> '1'
                idsAssocEnds = tratarResultadoQuery(this.dominio.query(id + ".feature->select(f : EJBFeature | f.oclIsTypeOf(EJBAssociationEnd))->collect(f : EJBFeature | f.oclAsType(EJBAssociationEnd))->select(ae : EJBAssociationEnd | ae.upper <> '1')"));
                for (String idAssocEnd : idsAssocEnds) {
                    String nomeSimples = this.dominio.query(idAssocEnd + ".name").replace("'", "");
                    String nomeCompleto = nomeSimples + tratarResultadoQuery(this.dominio.query(idAssocEnd + ".type.name"))[0];
                    String tipo = ("true".equals(this.dominio.query(idAssocEnd + ".type.oclIsTypeOf(EJBDataClass)").replace("'", "")) ? tratarResultadoQuery(this.dominio.query(idAssocEnd + ".type.name"))[0] + "DataObject" : tratarResultadoQuery(this.dominio.query(idAssocEnd + ".type.name"))[0]);
                    String tipoLista = "List<" + tipo + ">";
                    Atributo atrib = SintaxeJava.getJavaAttribute().setVisibilidade("private").setTipo(tipoLista).setNome(nomeCompleto);
                    classe.addAtributo(atrib);

                    Metodo getter = SintaxeJava.getJavaMethod();
                    getter.setNome("get" + upperFirstLetter(nomeSimples)).setRetorno(tipoLista).setVisibilidade("public").setCodigo(new String[]{"return " + nomeCompleto + ";"});
                    classe.addMetodo(getter);

                    Metodo add = SintaxeJava.getJavaMethod();
                    add.setNome("add" + upperFirstLetter(nomeSimples)).setRetorno("void").setVisibilidade("public").addParametro(tipo, nomeSimples).setCodigo(new String[]{"if (" + nomeCompleto + " == null) {", "\tthis." + nomeCompleto + " = new ArrayList<" + tipo + ">();", "}", "this. " + nomeCompleto + ".add(" + nomeSimples + ");"});
                    classe.addMetodo(add);

                    Metodo remove = SintaxeJava.getJavaMethod();
                    remove.setNome("remove" + upperFirstLetter(nomeSimples)).setRetorno("void").setVisibilidade("public").addParametro(tipo, nomeSimples).setCodigo(new String[]{"this. " + nomeCompleto + ".remove(" + nomeSimples + ");"});
                    classe.addMetodo(remove);
                }

                // Insere atributos (visibilidade = 'private') com getters e setters
                String[] idsAtributos = tratarResultadoQuery(this.dominio.query(id + ".feature->select(f : EJBFeature | f.oclIsTypeOf(EJBAttribute))"));
                for (String idAtributo : idsAtributos) {
                    String nomeAtributo = this.dominio.query(idAtributo + ".name").replace("'", "");
                    String tipoAtributo = converterTipoEjbParaJava(tratarResultadoQuery(this.dominio.query(idAtributo + ".type.name"))[0]);

                    Atributo atrib = SintaxeJava.getJavaAttribute();
                    atrib.setNome(nomeAtributo).setTipo(tipoAtributo).setVisibilidade("private");
                    classe.addAtributo(atrib);

                    Metodo getter = SintaxeJava.getJavaMethod();
                    getter.setNome("get" + upperFirstLetter(nomeAtributo)).setRetorno(tipoAtributo).setVisibilidade("public").setCodigo(new String[]{"return " + nomeAtributo + ";"});
                    classe.addMetodo(getter);

                    Metodo setter = SintaxeJava.getJavaMethod();
                    setter.setNome("set" + upperFirstLetter(nomeAtributo)).setRetorno("void").setVisibilidade("public").addParametro(tipoAtributo, nomeAtributo).setCodigo(new String[]{"this. " + nomeAtributo + " = " + nomeAtributo + ";"});
                    classe.addMetodo(setter);
                }
                classe.persiste();
            } catch (Exception ex) {
                System.out.println("-------------------------------------------");
                ex.printStackTrace();
                System.out.println("-------------------------------------------");
            }
        }
    }
}
