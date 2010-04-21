package br.uff.ic.mda.transformador;

import br.uff.ic.transformador.core.sintaxe.java.Atributo;
import br.uff.ic.transformador.core.sintaxe.java.Classe;
import br.uff.ic.transformador.core.sintaxe.java.Construtor;
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
        gerarEJBDataClass();

    }

    private String converterTipoEjbParaJava(String tipoEjb) {
        if ("EJBInteger".equals(tipoEjb)) {
            return "Integer";
        } else if ("EJBDouble".equals(tipoEjb)) {
            return "Double";
        } else if ("EJBString".equals(tipoEjb)) {
            return "String";
        } else if ("EJBDate".equals(tipoEjb)) {
            return "Date";
        } else if ("EJBBoolean".equals(tipoEjb)) {
            return "Boolean";
        } else {
            return tipoEjb;
        }
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

                classe.addConstrutor(new Construtor(classe.getNome(), codigo.toString(), classe.getAtributos()));

                classe.persiste();
            } catch (Exception ex) {
                System.out.println("-------------------------------------------");
                ex.printStackTrace();
                System.out.println("-------------------------------------------");
            }
        }
    }

    private void gerarEJBDataClass() throws Exception {
        String query = this.dominio.query("EJBDataClass.allInstances()");
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

                classe.addConstrutor(new Construtor(classe.getNome(), codigo.toString(), classe.getAtributos()));

                classe.persiste();
            } catch (Exception ex) {
                System.out.println("-------------------------------------------");
                ex.printStackTrace();
                System.out.println("-------------------------------------------");
            }
        }
    }
}
