package br.uff.ic.mda.transformer;

import br.uff.ic.mda.transformer.core.syntax.java.Attribute;
import br.uff.ic.mda.transformer.core.syntax.java.Class;
import br.uff.ic.mda.transformer.core.syntax.java.Constructor;
import br.uff.ic.mda.transformer.core.syntax.java.Interface;
import br.uff.ic.mda.transformer.core.syntax.java.Method;
import br.uff.ic.mda.transformer.core.syntax.java.Parameter;
import br.uff.ic.mda.transformer.core.syntax.java.JavaSyntax;

/**
 *
 * @author robertowm
 */
public class EjbCodeGenerator extends CodeGenerator<EjbDomain> {

    public EjbCodeGenerator(EjbDomain domain, String path) {
        super(domain, path);
    }

    @Override
    public void generate() throws Exception {
        generateEJBKeyClass();
        generateEJBEntityComponent();
        generateEJBDataClass();
    }

    private String convertEjbTypeToJavaType(String ejbType) throws Exception {
        if ("EJBInteger".equals(ejbType)) {
            return "Integer";
        }
        if ("EJBDouble".equals(ejbType)) {
            return "Double";
        }
        if ("EJBString".equals(ejbType)) {
            return "String";
        }
        if ("EJBDate".equals(ejbType)) {
            return "Date";
        }
        if ("EJBBoolean".equals(ejbType)) {
            return "Boolean";
        }
        if ("true".equals(this.domain.query(ejbType + ".oclIsTypeOf(EJBDataClass)").replace("'", ""))) {
            return this.domain.query(ejbType + ".name").replace("'", "") + "DataObject";
        }
        if ("true".equals(this.domain.query(ejbType + ".oclIsTypeOf(EJBSet)").replace("'", ""))) {
            return "Collection<" + processQueryResult(this.domain.query(ejbType + ".elementType.name"))[0] + ">";
        }
        return this.domain.query(ejbType + ".name").replace("'", "");
    }

    private void generateEJBKeyClass() throws Exception {
        String query = this.domain.query("EJBKeyClass.allInstances()");
        String[] ids = this.processQueryResult(query);

        for (String id : ids) {
            try {
                String idClass = id.replace("'", "").trim();

                Class keyClass = JavaSyntax.getJavaClass();
                keyClass.setDirectoryPath("codigo_gerado/");
                keyClass.setPackagePath("app");

                query = idClass + ".name";

                keyClass.setName(this.domain.query(query).replace("'", ""));
                keyClass.addClassNamesThatImplements("java.io.Serializable");
                keyClass.addConstructor(new Constructor(keyClass.getName(), ""));
                keyClass.setVisibility("public");

                query = "EJBAttribute.allInstances()->select(attr | attr.class->includes(" + idClass + "))->asSet()";
                String[] attributeIds = this.processQueryResult(this.domain.query(query));
                for (String attributeId : attributeIds) {
                    String idAtributo = attributeId.replace("'", "").trim();
                    Attribute attribute = JavaSyntax.getJavaAttribute();
                    attribute.setVisibility("public");
                    query = idAtributo + ".type->asOrderedSet()->first().name";
                    attribute.setType(convertEjbTypeToJavaType(processQueryResult(this.domain.query(query))[0]));
                    query = idAtributo + ".name";
                    attribute.setName(processQueryResult(this.domain.query(query))[0], true);

                    keyClass.addAttribute(attribute);
                }

                StringBuffer sourceCode = new StringBuffer();
                for (Attribute attribute : keyClass.getAttributes()) {
                    sourceCode.append("this." + attribute.getName() + " = " + attribute.getName() + ";\n");
                }

                Constructor constructor = JavaSyntax.getJavaConstructor().setClassName(keyClass.getName()).setCode(sourceCode.toString());
                for (Attribute attribute : keyClass.getAttributes()) {
                    constructor.addParameter(JavaSyntax.getJavaParameter().setName(attribute.getName()).setType(attribute.getType()));
                }
                keyClass.addConstructor(constructor);

                // Insere 'public boolean equals(Object obj)'
                Method equals = JavaSyntax.getJavaMethod().setVisibility("public").setReturnType("boolean").setName("equals").addParameter("Object", "obj");
                StringBuffer equalsCondition = new StringBuffer();
                for (int i = 0; i < keyClass.getAttributes().size() - 1; i++) {
                    Attribute attribute = keyClass.getAttributes().get(i);
                    equalsCondition.append("this." + attribute.getName() + ".equals(other." + attribute.getName() + ") and ");
                }
                Attribute lastKeyClassAttribute = keyClass.getAttributes().get(keyClass.getAttributes().size()-1);
                equalsCondition.append("this." + lastKeyClassAttribute.getName() + ".equals(other." + lastKeyClassAttribute.getName() + ")");
                equals.setCode(new String[]{
                    "if(!(obj instanceof " + keyClass.getName() + ")) {",
                    "\treturn false;",
                    "}",
                    keyClass.getName() + " other = (" + keyClass.getName() + ") obj;",
                    "if (" + equalsCondition + ") {",
                    "\treturn true;",
                    "}",
                    "return false;"
                });
                keyClass.addMethod(equals);

                // Insere 'public int hashCode()'
                Method hashCode = JavaSyntax.getJavaMethod().setVisibility("public").setReturnType("int").setName("hashCode");
                String[] hashCodeCode = new String[keyClass.getAttributes().size() + 2];
                int initialHashValue = 3;
                int otherHashValue = 19;
                hashCodeCode[0] = "int hash = " + initialHashValue + ";";
                hashCodeCode[hashCodeCode.length - 1] = "return hash;";
                for (int i = 0; i < keyClass.getAttributes().size(); i++) {
                    Attribute attribute = keyClass.getAttributes().get(i);
                    hashCodeCode[i + 1] = "hash = " + otherHashValue + " * hash + (this." + attribute.getName() + " != null ? this." + attribute.getName() + ".hashCode() : 0);";
                }
                hashCode.setCode(hashCodeCode);
                keyClass.addMethod(hashCode);

                keyClass.persist();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void generateEJBEntityComponent() throws Exception {
        String[] ids = processQueryResult(this.domain.query("UMLClassToEJBEntityComponent.allInstances()").replace("\n", ""));

        for (String id : ids) {
            try {
            // Criando o EJBObject
                Interface ejbObject = JavaSyntax.getJavaInterface();
                ejbObject.setDirectoryPath("codigo_gerado/");
                ejbObject.setPackagePath("app");

                ejbObject.addImportPath("java.rmi.*");
                ejbObject.addImportPath("javax.naming.*");
                ejbObject.addImportPath("javax.ejb.*");
                ejbObject.addImportPath("java.util.*");
                ejbObject.setName(processQueryResult(this.domain.query(id + ".entityComponent.name"))[0]);
                ejbObject.addClassNamesThatExtends("EJBObject");
                ejbObject.setVisibility("public");

                // Insere getter e setter para o DataClass
                String dataClassId = this.processQueryResult(this.domain.query(id + ".dataClass"))[0];
                String dataClassName = convertEjbTypeToJavaType(dataClassId);
                Method getterDataClass = JavaSyntax.getJavaMethod();
                getterDataClass.setName("get" + dataClassName).setReturnType(dataClassName).setVisibility("public").addException("RemoteException").setAbstractMethod(true);
                ejbObject.addMethod(getterDataClass);

                Method setterDataClass = JavaSyntax.getJavaMethod();
                setterDataClass.setName("set" + dataClassName).setReturnType("void").setVisibility("public").addParameter(dataClassName, "update").addException("NamingException").addException("FinderException").addException("CreateException").addException("RemoteException").setAbstractMethod(true);
                ejbObject.addMethod(setterDataClass);

                String[] businessMethodIds = processQueryResult(this.domain.query(id + ".entityComponent->asOrderedSet()->first().feature->select(f : EJBFeature | f.oclIsTypeOf(BusinessMethod))->collect(f : EJBFeature | f.oclAsType(BusinessMethod))->select(bm : BusinessMethod | bm.name.substring(1, 4) <> 'find')"));
                for (String businessMethodId : businessMethodIds) {
                    Method method = JavaSyntax.getJavaMethod();
                    method.setVisibility("public").setReturnType(convertEjbTypeToJavaType(processQueryResult(this.domain.query(businessMethodId + ".type"))[0])).setName(this.domain.query(businessMethodId + ".name").replace("'", "")).addException("RemoteException").setAbstractMethod(true);

                    String[] parameterIds = processQueryResult(this.domain.query(businessMethodId + ".parameter"));
                    for (String parameterId : parameterIds) {
                        Parameter parameter = JavaSyntax.getJavaParameter();
                        parameter.setName(this.domain.query(parameterId + ".name").replace("'", ""), true).setType(convertEjbTypeToJavaType(processQueryResult(this.domain.query(parameterId + ".type"))[0]));
                        method.addParameter(parameter);
                    }
                    ejbObject.addMethod(method);
                }

                ejbObject.persist();

            // Criando o EJBHome

                Interface ejbHome = JavaSyntax.getJavaInterface();
                ejbHome.setDirectoryPath("codigo_gerado/");
                ejbHome.setPackagePath("app");

                ejbHome.addImportPath("java.rmi.*");
                ejbHome.addImportPath("javax.naming.*");
                ejbHome.addImportPath("javax.ejb.*");
                ejbHome.addImportPath("java.util.*");
                ejbHome.setName(processQueryResult(this.domain.query(id + ".entityComponent.name"))[0] + "Home");
                ejbHome.addClassNamesThatExtends("EJBHome");
                ejbHome.setVisibility("public");

                String primaryKeyId = processQueryResult(this.domain.query("UMLClassToEJBKeyClass.allInstances()->select(a | a.class = " + id + ".class).keyClass"))[0];
                String primaryKeyName = this.domain.query(primaryKeyId + ".name").replace("'", "");
                // Insere create(...)
                Method createMethod = JavaSyntax.getJavaMethod();
                createMethod.setName("create").setVisibility("public").setReturnType(ejbObject.getName()).setAbstractMethod(true).addException("CreateException").addException("RemoteException");
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
                createMethod.addParameter(dataClassName, "newObject");
                ejbHome.addMethod(createMethod);

                // Insere findByPrimaryKey(PK)
                Method findByPrimaryKeyMethod = JavaSyntax.getJavaMethod();
                findByPrimaryKeyMethod.setName("findByPrimaryKey").setVisibility("public").setReturnType(ejbObject.getName()).setAbstractMethod(true).addException("FinderException").addException("RemoteException");
                // Adiciona parametro (PK)
                findByPrimaryKeyMethod.addParameter(primaryKeyName, "primaryKey");
                ejbHome.addMethod(findByPrimaryKeyMethod);

                // Insere os outros metodos tipo finder
                String[] finderIds = processQueryResult(this.domain.query(id + ".entityComponent->asOrderedSet()->first().feature->select(f : EJBFeature | f.oclIsTypeOf(BusinessMethod))->collect(f : EJBFeature | f.oclAsType(BusinessMethod))->select(bm : BusinessMethod | bm.name.substring(1, 4) = 'find')"));
                for (String finderId : finderIds) {
                    String finderName = this.domain.query(finderId + ".name").replace("'","");
                    Method method = JavaSyntax.getJavaMethod();
                    method.setVisibility("public").setName(finderName).addException("RemoteException").addException("FinderException").setAbstractMethod(true);

                    if (finderName.startsWith("findAll") || finderName.startsWith("findMany")) {
                        method.setReturnType("java.util.Collection<" + ejbObject.getName() + ">");
                    } else {
                        method.setReturnType(ejbObject.getName());
                    }

                    String[] parameterIds = processQueryResult(this.domain.query(finderId + ".parameter"));
                    for (String parameterId : parameterIds) {
                        Parameter parameter = JavaSyntax.getJavaParameter();
                        parameter.setName(this.domain.query(parameterId + ".name").replace("'", ""), true).setType(convertEjbTypeToJavaType(processQueryResult(this.domain.query(parameterId + ".type"))[0]));
                        method.addParameter(parameter);
                    }
                    ejbHome.addMethod(method);
                }



                ejbHome.persist();

            // Criar o EntityBean correspondente
                Class entityBean = JavaSyntax.getJavaClass();
                entityBean.setDirectoryPath("codigo_gerado/");
                entityBean.setPackagePath("app");

//                classe.addImport("java.rmi.*");
                entityBean.addImportPath("javax.naming.*");
                entityBean.addImportPath("javax.ejb.*");
                entityBean.addImportPath("java.util.*");
                entityBean.setName(processQueryResult(this.domain.query(id + ".entityComponent.name"))[0] + "Bean");
                entityBean.addClassNamesThatImplements("EntityBean");
                entityBean.setVisibility("public");

                // EJB stuff
                entityBean.addAttribute(JavaSyntax.getJavaAttribute().setName("context").setType("EntityContext").setVisibility("private"));
                entityBean.addMethod(JavaSyntax.getJavaMethod().setName("setEntityContext").setVisibility("public").setReturnType("void").setCode(new String[]{"this.context = context;"}).addParameter("EntityContext", "context"));
                entityBean.addMethod(JavaSyntax.getJavaMethod().setName("unsetEntityContext").setVisibility("public").setReturnType("void").setCode(new String[]{"this.context = null;"}));
                entityBean.addMethod(JavaSyntax.getJavaMethod().setName("ejbActivate").setVisibility("public").setReturnType("void"));
                entityBean.addMethod(JavaSyntax.getJavaMethod().setName("ejbPassivate").setVisibility("public").setReturnType("void"));
                entityBean.addMethod(JavaSyntax.getJavaMethod().setName("ejbRemove").setVisibility("public").setReturnType("void"));
                entityBean.addMethod(JavaSyntax.getJavaMethod().setName("ejbLoad").setVisibility("public").setReturnType("void"));
                entityBean.addMethod(JavaSyntax.getJavaMethod().setName("ejbStore").setVisibility("public").setReturnType("void"));

                // Adicionando atributo 'key'
                Attribute primaryKeyAttribute = JavaSyntax.getJavaAttribute();
                primaryKeyAttribute.setName("key").setVisibility("private").setType(dataClassName);
                entityBean.addAttribute(primaryKeyAttribute);

                // Adicionando getter para 'key'
                Method getterPrimaryKey = JavaSyntax.getJavaMethod();
                getterPrimaryKey.setVisibility("public").setReturnType(dataClassName).setName("get" + upperFirstLetter(dataClassName)).setCode(new String[]{"return key;"});
                entityBean.addMethod(getterPrimaryKey);

                // Adicionando setter para 'key'
                Method setterPrimaryKey = JavaSyntax.getJavaMethod();
                setterPrimaryKey.setVisibility("public").setReturnType("void").setName("set" + upperFirstLetter(dataClassName)).addParameter(dataClassName, "newObj").setCode(new String[]{"this.key = newObj;"}).addException("NamingException").addException("FinderException").addException("CreateException");
                entityBean.addMethod(setterPrimaryKey);

                // Adiciona ejbCreate
                entityBean.addMethod(JavaSyntax.getJavaMethod().setName("ejbCreate").setVisibility("public").setReturnType(primaryKeyName).addException("NamingException").addException("FinderException").addException("CreateException").setCode(new String[]{"if (newObj == null) {", "\tthrow new CreateException(\"The field 'newObj' must not be null\");", "}","","// TODO add additional validation code, throw CreateException if data is not valid", setterPrimaryKey.getName() + "(newObj);", "", "return null;"}).addParameter(dataClassName, "newObj"));

                // Adiciona ejbPostCreate (Ver se da pra automatizar)
                entityBean.addMethod(JavaSyntax.getJavaMethod().setName("ejbPostCreate").setVisibility("public").setReturnType("void").setCode(new String[]{"// TODO populate relationships here if appropriate"}).addParameter(dataClassName, "newObj"));

                // Adicionando construtor padrao
                entityBean.addConstructor(JavaSyntax.getJavaConstructor().setClassName(entityBean.getName()));

                // Adicionando o metodo create
//                Method createEntityBeanMethod = JavaSyntax.getJavaMethod().setName("create").setVisibility("public").setRetorno(primaryKeyName).addException("CreateException").setCode(new String[]{"// TODO - You must decide how your persistence will work.", "return null;"});
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
//                entityBean.addMethod(createEntityBeanMethod);
                // Adicionando o metodo findByPrimaryKey
//                entityBean.addMethod(JavaSyntax.getJavaMethod().setName("findByPrimaryKey").setVisibility("public").setRetorno(ejbObject.getName()).addException("FinderException").addParameter(primaryKeyName, "primaryKey").setCode(new String[]{"// TODO - You must decide how your persistence will work.", "return null;"}));
                entityBean.addMethod(JavaSyntax.getJavaMethod().setName("ejbFindByPrimaryKey").setVisibility("public").setReturnType(primaryKeyName).addException("FinderException").addParameter(primaryKeyName, "primaryKey").setCode(new String[]{"// TODO - You must decide how your persistence will work.", "return null;"}));

                // Adicionando metodos oriundos da transformacao
                for (String finderId : finderIds) {
                    String finderName = this.domain.query(finderId + ".name").replace("'", "");
                    Method method = JavaSyntax.getJavaMethod();
                    method.setVisibility("public");
                    if (finderName.startsWith("findAll") || finderName.startsWith("findMany")) {
                        method.setReturnType("java.util.Collection<" + primaryKeyName + ">");
                    } else {
                        method.setReturnType(primaryKeyName);
                    }
                    method.setName("ejb" + upperFirstLetter(finderName)).setCode(new String[]{"// TODO", "return null;"});
                    method.addException("FinderException");

                    String[] parameterIds = processQueryResult(this.domain.query(finderId + ".parameter"));
                    for (String parameterId : parameterIds) {
                        Parameter parameter = JavaSyntax.getJavaParameter();
                        parameter.setName(this.domain.query(parameterId + ".name").replace("'", ""), true).setType(convertEjbTypeToJavaType(processQueryResult(this.domain.query(parameterId + ".type"))[0]));
                        method.addParameter(parameter);
                    }
                    entityBean.addMethod(method);
                }

                for (String businessMethodId : businessMethodIds) {
                    Method method = JavaSyntax.getJavaMethod();
                    method.setVisibility("public")
                            .setReturnType(convertEjbTypeToJavaType(processQueryResult(this.domain.query(businessMethodId + ".type"))[0]))
                            .setName(this.domain.query(businessMethodId + ".name").replace("'", ""))
                            .setCode(new String[]{"// TODO", "return null;"});

                    String[] parameterIds = processQueryResult(this.domain.query(businessMethodId + ".parameter"));
                    for (String parameterId : parameterIds) {
                        Parameter parameter = JavaSyntax.getJavaParameter();
                        parameter.setName(this.domain.query(parameterId + ".name").replace("'", ""), true).setType(convertEjbTypeToJavaType(processQueryResult(this.domain.query(parameterId + ".type"))[0]));
                        method.addParameter(parameter);
                    }
                    entityBean.addMethod(method);
                }

                entityBean.persist();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void generateEJBDataClass() throws Exception {
        String[] ids = processQueryResult(this.domain.query("EJBDataClass.allInstances()").replace("\n", ""));

        for (String id : ids) {
            try {

                Class dataClass = JavaSyntax.getJavaClass();
                dataClass.setDirectoryPath("codigo_gerado/");
                dataClass.setPackagePath("app");
                
                dataClass.addImportPath("java.util.*");
                dataClass.setName(this.domain.query(id + ".name").replace("'", "") + "DataObject");
                dataClass.setVisibility("public");
                dataClass.addClassNamesThatImplements("java.io.Serializable");

                // Adicionando atributo 'key'
                String umlClassId = this.domain.query("Class.allInstances()->select(c | c.name = " + id + ".name)->asOrderedSet()->first()");
                String keyClassId    = processQueryResult(this.domain.query("if " + umlClassId + ".oclIsTypeOf(Class) then UMLClassToEJBKeyClass.allInstances()->select(a | a.class->includes(" + umlClassId + ")).keyClass else UMLAssociationClassToEJBKeyClass.allInstances()->select(a | a.associationClass->includes(" + umlClassId + ")).keyClass endif"))[0];
                String keyClassName = this.domain.query(keyClassId + ".name").replace("'", "");
                Attribute primaryKey = JavaSyntax.getJavaAttribute().setName("key").setType(keyClassName).setVisibility("private");
                dataClass.addAttribute(primaryKey);

                // Adicionando construtor padrao
                dataClass.addConstructor(JavaSyntax.getJavaConstructor().setClassName(dataClass.getName()));

                // Adicionando construtor com o 'key' como parametro
                Constructor constructor = JavaSyntax.getJavaConstructor();
                constructor.setClassName(dataClass.getName()).addParameter(JavaSyntax.getJavaParameter().setName("key").setType(this.domain.query(keyClassId + ".name").replace("'", "")));
                constructor.setCode("this.key = key;");
                dataClass.addConstructor(constructor);

                // Adicionando getter para 'key'
                Method getterPrimaryKey = JavaSyntax.getJavaMethod();
                getterPrimaryKey.setVisibility("public").setReturnType(keyClassName).setName("get" + upperFirstLetter(keyClassName)).setCode(new String[]{"return key;"});
                dataClass.addMethod(getterPrimaryKey);

                // Adicionando setter para 'key'
                Method setterPrimaryKey = JavaSyntax.getJavaMethod();
                setterPrimaryKey.setVisibility("public").setReturnType("void").setName("set" + upperFirstLetter(keyClassName)).addParameter(keyClassName, "key").setCode(new String[]{"this.key = key;"});
                dataClass.addMethod(setterPrimaryKey);

                // Insere relacionamentos (AssociationEnd) com getters e setters
                String[] associationEndIds = null;
                // 1) upper = '1'
                associationEndIds = processQueryResult(this.domain.query(id + ".feature->select(f : EJBFeature | f.oclIsTypeOf(EJBAssociationEnd))->collect(f : EJBFeature | f.oclAsType(EJBAssociationEnd))->select(ae : EJBAssociationEnd | ae.upper = '1')"));
                for (String associationEndId : associationEndIds) {
//                    String nome = this.dominio.query(idAssocEnd + ".name").replace("'", "") + tratarResultadoQuery(this.dominio.query(idAssocEnd + ".type.name"))[0];
                    String name = this.domain.query(associationEndId + ".name").replace("'", "");
                    boolean isDataClass = "true".equals(this.domain.query(associationEndId + ".type->asOrderedSet()->first().oclIsTypeOf(EJBDataClass)").replace("'", ""));
                    String type = isDataClass ? processQueryResult(this.domain.query(associationEndId + ".type.name"))[0] + "DataObject" : processQueryResult(this.domain.query(associationEndId + ".type.name"))[0];
                    Attribute attribute = JavaSyntax.getJavaAttribute().setVisibility("private").setType(type).setName(name);
                    dataClass.addAttribute(attribute);

                    Method getter = JavaSyntax.getJavaMethod();
                    getter.setName("get" + upperFirstLetter(name)).setReturnType(type).setVisibility("public").setCode(new String[]{"return " + name + ";"});
                    dataClass.addMethod(getter);

                    Method setter = JavaSyntax.getJavaMethod();
                    setter.setName("set" + upperFirstLetter(name)).setReturnType("void").setVisibility("public").addParameter(type, name).setCode(new String[]{"this." + name + " = " + name + ";"});
                    dataClass.addMethod(setter);

                }
                // 2) upper <> '1'
                associationEndIds = processQueryResult(this.domain.query(id + ".feature->select(f : EJBFeature | f.oclIsTypeOf(EJBAssociationEnd))->collect(f : EJBFeature | f.oclAsType(EJBAssociationEnd))->select(ae : EJBAssociationEnd | ae.upper <> '1')"));
                for (String associationEndId : associationEndIds) {
                    String simpleName = this.domain.query(associationEndId + ".name").replace("'", "");
//                    String nomeCompleto = nomeSimples + tratarResultadoQuery(this.dominio.query(idAssocEnd + ".type.name"))[0];
                    String type = "true".equals(this.domain.query(associationEndId + ".type->exists(c : EJBClassifier | c.oclIsTypeOf(EJBDataClass))").replace("'", "")) ? processQueryResult(this.domain.query(associationEndId + ".type.name"))[0] + "DataObject" : processQueryResult(this.domain.query(associationEndId + ".type.name"))[0];
                    String listType = "List<" + type + ">";
//                    Atributo atrib = SintaxeJava.getJavaAttribute().setVisibilidade("private").setTipo(tipoLista).setNome(nomeCompleto);
                    Attribute attribute = JavaSyntax.getJavaAttribute().setVisibility("private").setType(listType).setName(simpleName);
                    dataClass.addAttribute(attribute);

                    Method getter = JavaSyntax.getJavaMethod();
                    getter.setName("get" + upperFirstLetter(simpleName)).setReturnType(listType).setVisibility("public").setCode(new String[]{"return " + simpleName + ";"});
                    dataClass.addMethod(getter);

                    Method add = JavaSyntax.getJavaMethod();
                    add.setName("add" + upperFirstLetter(simpleName)).setReturnType("void").setVisibility("public").addParameter(type, simpleName).setCode(new String[]{"if (this." + simpleName + " == null) {", "\tthis." + simpleName + " = new ArrayList<" + type + ">();", "}", "this." + simpleName + ".add(" + simpleName + ");"});
                    dataClass.addMethod(add);

                    Method remove = JavaSyntax.getJavaMethod();
                    remove.setName("remove" + upperFirstLetter(simpleName)).setReturnType("void").setVisibility("public").addParameter(type, simpleName).setCode(new String[]{"this." + simpleName + ".remove(" + simpleName + ");"});
                    dataClass.addMethod(remove);
                }

                // Insere atributos (visibilidade = 'private') com getters e setters
                String[] attributeIds = processQueryResult(this.domain.query(id + ".feature->select(f : EJBFeature | f.oclIsTypeOf(EJBAttribute))"));
                for (String attributeId : attributeIds) {
                    String attributeName = this.domain.query(attributeId + ".name").replace("'", "");
                    String attributeType = convertEjbTypeToJavaType(processQueryResult(this.domain.query(attributeId + ".type.name"))[0]);

                    Attribute attribute = JavaSyntax.getJavaAttribute();
                    attribute.setName(attributeName).setType(attributeType).setVisibility("private");
                    dataClass.addAttribute(attribute);

                    Method getter = JavaSyntax.getJavaMethod();
                    getter.setName("get" + upperFirstLetter(attributeName)).setReturnType(attributeType).setVisibility("public").setCode(new String[]{"return " + attributeName + ";"});
                    dataClass.addMethod(getter);

                    Method setter = JavaSyntax.getJavaMethod();
                    setter.setName("set" + upperFirstLetter(attributeName)).setReturnType("void").setVisibility("public").addParameter(attributeType, attributeName).setCode(new String[]{"this." + attributeName + " = " + attributeName + ";"});
                    dataClass.addMethod(setter);
                }

                dataClass.persist();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
