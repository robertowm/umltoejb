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

    }

    private void gerarEJBKeyClass() throws Exception {
        String query = this.dominio.query("EJBKeyClass.allInstances()");
        String[] ids = this.tratarResultadoQuery(query);
//        String[] ids = this.tratarResultadoQuery(this.dominio.query("EJBKeyClass.allInstances()"));

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
                    String idAtributo = idAttr.replace("'", "").trim();
                    Atributo atributo = SintaxeJava.getJavaAttribute();
//                atributo.setVisibilidade(this.dominio.query(idAttr + ".visibility").replace("'", ""));
                    atributo.setVisibilidade("public");
                    query = idAtributo + ".type->asOrderedSet()->first().name";
                    atributo.setTipo(this.dominio.query(query).replace("'",""));
                    query = idAtributo + ".name";
                    atributo.setNome(this.dominio.query(query).replace("'", ""), true);

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
//                System.out.println("[ERRO] Query n‹o foi executada: " + query);
                ex.printStackTrace();
                System.out.println("-------------------------------------------");
            }
        }
    }
}
