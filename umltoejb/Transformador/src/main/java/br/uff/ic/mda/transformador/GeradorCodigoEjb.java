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
                classe.setCaminhoDiretorio("codigo_gerado/");
                classe.setCaminhoPacote("app");

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
            // Criando o EJBObject
                Interface ejbObject = SintaxeJava.getJavaInterface();
                ejbObject.setCaminhoDiretorio("codigo_gerado/");
                ejbObject.setCaminhoPacote("app");

                ejbObject.addImport("java.rmi.*");
                ejbObject.addImport("javax.naming.*");
                ejbObject.addImport("javax.ejb.*");
                ejbObject.addImport("java.util.*");
                ejbObject.setNome(tratarResultadoQuery(this.dominio.query(id + ".entityComponent.name"))[0]);
                ejbObject.addNomeClasseQueEstende("EJBObject");
                ejbObject.setVisibilidade("public");

                // Insere getter e setter para o DataClass
                String idDataClass = this.tratarResultadoQuery(this.dominio.query(id + ".dataClass"))[0];
                String nomeDataClass = converterTipoEjbParaJava(idDataClass);
                Metodo metodoGet = SintaxeJava.getJavaMethod();
                metodoGet.setNome("get" + nomeDataClass).setRetorno(nomeDataClass).setVisibilidade("public").addExcecao("RemoteException").setAbstrato(true);
                ejbObject.addMetodo(metodoGet);

                Metodo metodoSet = SintaxeJava.getJavaMethod();
                metodoSet.setNome("set" + nomeDataClass).setRetorno("void").setVisibilidade("public").addParametro(nomeDataClass, "update").addExcecao("NamingException").addExcecao("FinderException").addExcecao("CreateException").addExcecao("RemoteException").setAbstrato(true);
                ejbObject.addMetodo(metodoSet);

                String[] idsBusinessMethod = tratarResultadoQuery(this.dominio.query(id + ".entityComponent->asOrderedSet()->first().feature->select(f : EJBFeature | f.oclIsTypeOf(BusinessMethod))"));
                for (String idBusinessMethod : idsBusinessMethod) {
                    Metodo metodo = SintaxeJava.getJavaMethod();
                    metodo.setVisibilidade("public").setRetorno(converterTipoEjbParaJava(tratarResultadoQuery(this.dominio.query(idBusinessMethod + ".type"))[0])).setNome(this.dominio.query(idBusinessMethod + ".name").replace("'", "")).addExcecao("RemoteException").setAbstrato(true);

                    String[] idsParametro = tratarResultadoQuery(this.dominio.query(idBusinessMethod + ".parameter"));
                    for (String idParametro : idsParametro) {
                        Parametro parametro = SintaxeJava.getJavaParameter();
                        parametro.setNome(this.dominio.query(idParametro + ".name").replace("'", ""), true).setTipo(converterTipoEjbParaJava(tratarResultadoQuery(this.dominio.query(idParametro + ".type"))[0]));
                        metodo.addParametro(parametro);
                    }
                    ejbObject.addMetodo(metodo);
                }

                ejbObject.persiste();

            // Criando o EJBHome

                Interface ejbHome = SintaxeJava.getJavaInterface();
                ejbHome.setCaminhoDiretorio("codigo_gerado/");
                ejbHome.setCaminhoPacote("app");

                ejbHome.addImport("java.rmi.*");
                ejbHome.addImport("javax.naming.*");
                ejbHome.addImport("javax.ejb.*");
                ejbHome.addImport("java.util.*");
                ejbHome.setNome(tratarResultadoQuery(this.dominio.query(id + ".entityComponent.name"))[0] + "Home");
                ejbHome.addNomeClasseQueEstende("EJBHome");
                ejbHome.setVisibilidade("public");

                String idPK = tratarResultadoQuery(this.dominio.query("UMLClassToEJBKeyClass.allInstances()->select(a | a.class = " + id + ".class).keyClass"))[0];
                String nomePK = this.dominio.query(idPK + ".name").replace("'", "");
                // Insere create(...)
                Metodo create = SintaxeJava.getJavaMethod();
                create.setNome("create").setVisibilidade("public").setRetorno(ejbObject.getNome()).setAbstrato(true).addExcecao("CreateException").addExcecao("RemoteException");
                // Adicionar parametros
                // 1) Relacoes do DataClass
//                for (String idAssociationEnd : tratarResultadoQuery(this.dominio.query(idDataClass + ".feature->select(f : EJBFeature | f.oclIsTypeOf(EJBAssociationEnd))"))) {
//                    if (!"1".equals(this.dominio.query(idAssociationEnd + ".upper").replace("'", ""))) {
//                        boolean isDataClass = "true".equals(this.dominio.query(idAssociationEnd + ".type->asOrderedSet()->first().oclIsKindOf(EJBDataClass)"));
//                        create.addParametro("List<" + (isDataClass ? tratarResultadoQuery(this.dominio.query(idAssociationEnd + ".type.name"))[0] + "DataObject" : tratarResultadoQuery(this.dominio.query(idAssociationEnd + ".type.name"))[0]) + ">", this.dominio.query(idAssociationEnd + ".name").replace("'", ""));
//                    } else {
//                        create.addParametro(tratarResultadoQuery(this.dominio.query(idAssociationEnd + ".type.name"))[0], this.dominio.query(idAssociationEnd + ".name").replace("'", ""));
//                    }
//                }
//                // 2) Atributos do DataClass
//                for (String idAttribute : tratarResultadoQuery(this.dominio.query(idDataClass + ".feature->select(f : EJBFeature | f.oclIsTypeOf(EJBAttribute))"))) {
//                    create.addParametro(converterTipoEjbParaJava(tratarResultadoQuery(this.dominio.query(idAttribute + ".type"))[0]), this.dominio.query(idAttribute + ".name").replace("'", ""));
//                }
                create.addParametro(nomeDataClass, "newObject");
                ejbHome.addMetodo(create);

                // Insere findByPrimaryKey(PK)
                Metodo findByPK = SintaxeJava.getJavaMethod();
                findByPK.setNome("findByPrimaryKey").setVisibilidade("public").setRetorno(ejbObject.getNome()).setAbstrato(true).addExcecao("FinderException").addExcecao("RemoteException");
                // Adiciona parametro (PK)
                findByPK.addParametro(nomePK, "primaryKey");
                ejbHome.addMetodo(findByPK);

                ejbHome.persiste();

            // Criar o EntityBean correspondente
                Classe classe = SintaxeJava.getJavaClass();
                classe.setCaminhoDiretorio("codigo_gerado/");
                classe.setCaminhoPacote("app");

//                classe.addImport("java.rmi.*");
                classe.addImport("javax.naming.*");
                classe.addImport("javax.ejb.*");
                classe.addImport("java.util.*");
                classe.setNome(tratarResultadoQuery(this.dominio.query(id + ".entityComponent.name"))[0] + "Bean");
                classe.addNomeClasseQueImplementa("EntityBean");
                classe.setVisibilidade("public");

                // EJB stuff
                classe.addAtributo(SintaxeJava.getJavaAttribute().setNome("context").setTipo("EntityContext").setVisibilidade("private"));
                classe.addMetodo(SintaxeJava.getJavaMethod().setNome("setEntityContext").setVisibilidade("public").setRetorno("void").setCodigo(new String[]{"this.context = context;"}).addParametro("EntityContext", "context"));
                classe.addMetodo(SintaxeJava.getJavaMethod().setNome("unsetEntityContext").setVisibilidade("public").setRetorno("void").setCodigo(new String[]{"this.context = null;"}));
                classe.addMetodo(SintaxeJava.getJavaMethod().setNome("ejbActivate").setVisibilidade("public").setRetorno("void"));
                classe.addMetodo(SintaxeJava.getJavaMethod().setNome("ejbPassivate").setVisibilidade("public").setRetorno("void"));
                classe.addMetodo(SintaxeJava.getJavaMethod().setNome("ejbRemove").setVisibilidade("public").setRetorno("void"));
                classe.addMetodo(SintaxeJava.getJavaMethod().setNome("ejbLoad").setVisibilidade("public").setRetorno("void"));
                classe.addMetodo(SintaxeJava.getJavaMethod().setNome("ejbStore").setVisibilidade("public").setRetorno("void"));

                // Adicionando atributo 'key'
                Atributo key = SintaxeJava.getJavaAttribute();
                key.setNome("key").setVisibilidade("private").setTipo(nomeDataClass);
                classe.addAtributo(key);

                // Adicionando getter para 'key'
                Metodo getterKey = SintaxeJava.getJavaMethod();
                getterKey.setVisibilidade("public").setRetorno(nomeDataClass).setNome("get" + upperFirstLetter(nomeDataClass)).setCodigo(new String[]{"return key;"});
                classe.addMetodo(getterKey);

                // Adicionando setter para 'key'
                Metodo setterKey = SintaxeJava.getJavaMethod();
                setterKey.setVisibilidade("public").setRetorno("void").setNome("set" + upperFirstLetter(nomeDataClass)).addParametro(nomeDataClass, "newObj").setCodigo(new String[]{"this.key = newObj;"}).addExcecao("NamingException").addExcecao("FinderException").addExcecao("CreateException");
                classe.addMetodo(setterKey);

                // Adiciona ejbCreate
                classe.addMetodo(SintaxeJava.getJavaMethod().setNome("ejbCreate").setVisibilidade("public").setRetorno(nomePK).addExcecao("NamingException").addExcecao("FinderException").addExcecao("CreateException").setCodigo(new String[]{"if (newObj == null) {", "\tthrow new CreateException(\"The field 'newObj' must not be null\");", "}","","// TODO add additional validation code, throw CreateException if data is not valid", setterKey.getNome() + "(newObj);", "", "return null;"}).addParametro(nomeDataClass, "newObj"));

                // Adiciona ejbPostCreate (Ver se da pra automatizar)
                classe.addMetodo(SintaxeJava.getJavaMethod().setNome("ejbPostCreate").setVisibilidade("public").setRetorno("void").setCodigo(new String[]{"// TODO populate relationships here if appropriate"}).addParametro(nomeDataClass, "newObj"));

                // Adicionando construtor padrao
                classe.addConstrutor(SintaxeJava.getJavaConstructor().setNomeClasse(classe.getNome()));

                // Adicionando o metodo create
                Metodo createEB = SintaxeJava.getJavaMethod().setNome("create").setVisibilidade("public").setRetorno(nomePK).addExcecao("CreateException").setCodigo(new String[]{"// TODO - You must decide how your persistence will work.", "return null;"});
//                for (String idAssociationEnd : tratarResultadoQuery(this.dominio.query(idDataClass + ".feature->select(f : EJBFeature | f.oclIsTypeOf(EJBAssociationEnd))"))) {
//                    if (!"1".equals(this.dominio.query(idAssociationEnd + ".upper").replace("'", ""))) {
//                        create.addParametro("List<" + tratarResultadoQuery(this.dominio.query(idAssociationEnd + ".type.name"))[0] + ">", this.dominio.query(idAssociationEnd + ".name").replace("'", ""));
//                    } else {
//                        create.addParametro(tratarResultadoQuery(this.dominio.query(idAssociationEnd + ".type.name"))[0], this.dominio.query(idAssociationEnd + ".name").replace("'", ""));
//                    }
//                }
//                // 2) Atributos do DataClass
//                for (String idAttribute : tratarResultadoQuery(this.dominio.query(idDataClass + ".feature->select(f : EJBFeature | f.oclIsTypeOf(EJBAttribute))"))) {
//                    createEB.addParametro(converterTipoEjbParaJava(tratarResultadoQuery(this.dominio.query(idAttribute + ".type"))[0]), this.dominio.query(idAttribute + ".name").replace("'", ""));
//                }
                classe.addMetodo(createEB);
                // Adicionando o metodo findByPrimaryKey
                classe.addMetodo(SintaxeJava.getJavaMethod().setNome("findByPrimaryKey").setVisibilidade("public").setRetorno(ejbObject.getNome()).addExcecao("FinderException").addParametro(nomePK, "primaryKey").setCodigo(new String[]{"// TODO - You must decide how your persistence will work.", "return null;"}));
                classe.addMetodo(SintaxeJava.getJavaMethod().setNome("ejbFindByPrimaryKey").setVisibilidade("public").setRetorno(nomePK).addExcecao("FinderException").addParametro(nomePK, "primaryKey").setCodigo(new String[]{"// TODO - You must decide how your persistence will work.", "return null;"}));

                // Adicionando metodos oriundos da transformacao
                for (String idBusinessMethod : idsBusinessMethod) {
                    Metodo metodo = SintaxeJava.getJavaMethod();
                    metodo.setVisibilidade("public")
                            .setRetorno(converterTipoEjbParaJava(tratarResultadoQuery(this.dominio.query(idBusinessMethod + ".type"))[0]))
                            .setNome(this.dominio.query(idBusinessMethod + ".name").replace("'", ""))
                            .setCodigo(new String[]{"// TODO", "return null;"});

                    String[] idsParametro = tratarResultadoQuery(this.dominio.query(idBusinessMethod + ".parameter"));
                    for (String idParametro : idsParametro) {
                        Parametro parametro = SintaxeJava.getJavaParameter();
                        parametro.setNome(this.dominio.query(idParametro + ".name").replace("'", ""), true).setTipo(converterTipoEjbParaJava(tratarResultadoQuery(this.dominio.query(idParametro + ".type"))[0]));
                        metodo.addParametro(parametro);
                    }
                    classe.addMetodo(metodo);
                }

                classe.persiste();
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
                classe.setCaminhoDiretorio("codigo_gerado/");
                classe.setCaminhoPacote("app");
                
//                classe.addImport("java.rmi.*");
//                classe.addImport("javax.naming.*");
//                classe.addImport("javax.ejb.*");
                classe.addImport("java.util.*");
                classe.setNome(this.dominio.query(id + ".name").replace("'", "") + "DataObject");
//                classe.addNomeClasseQueImplementa("DataObject");
                classe.setVisibilidade("public");
//                classe.setAbstrata(true);

                // Adicionando atributo 'key'
                String idUmlClass = this.dominio.query("Class.allInstances()->select(c | c.name = " + id + ".name)->asOrderedSet()->first()");
                String idKeyClass = tratarResultadoQuery(this.dominio.query("if " + idUmlClass + ".oclIsTypeOf(Class) then UMLClassToEJBKeyClass.allInstances()->select(a | a.class->includes(" + idUmlClass + ")).keyClass else UMLAssociationClassToEJBKeyClass.allInstances()->select(a | a.associationClass->includes(" + idUmlClass + ")).keyClass endif"))[0];
                String nomeKeyClass = this.dominio.query(idKeyClass + ".name").replace("'", "");
                Atributo key = SintaxeJava.getJavaAttribute().setNome("key").setTipo(this.dominio.query(idKeyClass + ".name").replace("'", "")).setVisibilidade("private");
                classe.addAtributo(key);

                // Adicionando construtor padrao
                classe.addConstrutor(SintaxeJava.getJavaConstructor().setNomeClasse(classe.getNome()));

                // Adicionando construtor com o 'key' como parametro
                Construtor construtor = SintaxeJava.getJavaConstructor();
                construtor.setNomeClasse(classe.getNome()).addParametro(SintaxeJava.getJavaParameter().setNome("key").setTipo(this.dominio.query(idKeyClass + ".name").replace("'", "")));
                construtor.setCodigo("this.key = key;");
                classe.addConstrutor(construtor);

                // Adicionando getter para 'key'
                Metodo getterKey = SintaxeJava.getJavaMethod();
                getterKey.setVisibilidade("public").setRetorno(nomeKeyClass).setNome("get" + upperFirstLetter(nomeKeyClass)).setCodigo(new String[]{"return key;"});
                classe.addMetodo(getterKey);

                // Adicionando setter para 'key'
                Metodo setterKey = SintaxeJava.getJavaMethod();
                setterKey.setVisibilidade("public").setRetorno("void").setNome("set" + upperFirstLetter(nomeKeyClass)).addParametro(nomeKeyClass, "key").setCodigo(new String[]{"this.key = key;"});
                classe.addMetodo(setterKey);

                // Insere relacionamentos (AssociationEnd) com getters e setters
                String[] idsAssocEnds = null;
                // 1) upper = '1'
                idsAssocEnds = tratarResultadoQuery(this.dominio.query(id + ".feature->select(f : EJBFeature | f.oclIsTypeOf(EJBAssociationEnd))->collect(f : EJBFeature | f.oclAsType(EJBAssociationEnd))->select(ae : EJBAssociationEnd | ae.upper = '1')"));
                for (String idAssocEnd : idsAssocEnds) {
//                    String nome = this.dominio.query(idAssocEnd + ".name").replace("'", "") + tratarResultadoQuery(this.dominio.query(idAssocEnd + ".type.name"))[0];
                    String nome = this.dominio.query(idAssocEnd + ".name").replace("'", "");
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
//                    String nomeCompleto = nomeSimples + tratarResultadoQuery(this.dominio.query(idAssocEnd + ".type.name"))[0];
                    String tipo = "true".equals(this.dominio.query(idAssocEnd + ".type->exists(c : EJBClassifier | c.oclIsTypeOf(EJBDataClass))").replace("'", "")) ? tratarResultadoQuery(this.dominio.query(idAssocEnd + ".type.name"))[0] + "DataObject" : tratarResultadoQuery(this.dominio.query(idAssocEnd + ".type.name"))[0];
                    String tipoLista = "List<" + tipo + ">";
//                    Atributo atrib = SintaxeJava.getJavaAttribute().setVisibilidade("private").setTipo(tipoLista).setNome(nomeCompleto);
                    Atributo atrib = SintaxeJava.getJavaAttribute().setVisibilidade("private").setTipo(tipoLista).setNome(nomeSimples);
                    classe.addAtributo(atrib);

                    Metodo getter = SintaxeJava.getJavaMethod();
//                    getter.setNome("get" + upperFirstLetter(nomeSimples)).setRetorno(tipoLista).setVisibilidade("public").setCodigo(new String[]{"return " + nomeCompleto + ";"});
                    getter.setNome("get" + upperFirstLetter(nomeSimples)).setRetorno(tipoLista).setVisibilidade("public").setCodigo(new String[]{"return " + nomeSimples + ";"});
                    classe.addMetodo(getter);

                    Metodo add = SintaxeJava.getJavaMethod();
//                    add.setNome("add" + upperFirstLetter(nomeSimples)).setRetorno("void").setVisibilidade("public").addParametro(tipo, nomeSimples).setCodigo(new String[]{"if (" + nomeCompleto + " == null) {", "\tthis." + nomeCompleto + " = new ArrayList<" + tipo + ">();", "}", "this. " + nomeCompleto + ".add(" + nomeSimples + ");"});
                    add.setNome("add" + upperFirstLetter(nomeSimples)).setRetorno("void").setVisibilidade("public").addParametro(tipo, nomeSimples).setCodigo(new String[]{"if (" + nomeSimples + " == null) {", "\tthis." + nomeSimples + " = new ArrayList<" + tipo + ">();", "}", "this. " + nomeSimples + ".add(" + nomeSimples + ");"});
                    classe.addMetodo(add);

                    Metodo remove = SintaxeJava.getJavaMethod();
//                    remove.setNome("remove" + upperFirstLetter(nomeSimples)).setRetorno("void").setVisibilidade("public").addParametro(tipo, nomeSimples).setCodigo(new String[]{"this. " + nomeCompleto + ".remove(" + nomeSimples + ");"});
                    remove.setNome("remove" + upperFirstLetter(nomeSimples)).setRetorno("void").setVisibilidade("public").addParametro(tipo, nomeSimples).setCodigo(new String[]{"this. " + nomeSimples + ".remove(" + nomeSimples + ");"});
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
