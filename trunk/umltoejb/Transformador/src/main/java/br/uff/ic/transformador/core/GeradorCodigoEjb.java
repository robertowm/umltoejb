package br.uff.ic.transformador.core;

import br.uff.ic.transformador.core.sintaxe.java.Atributo;
import br.uff.ic.transformador.core.sintaxe.java.Classe;
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
        String[] ids = this.tratarResultadoQuery(this.dominio.query("EJBKeyClass.allInstances()"));

        for (String id : ids) {
            Classe classe = SintaxeJava.getJavaClass();
            classe.setNome(this.dominio.query(id + ".nameClassifier").replace("'", ""));

            String[] idsAttr = this.tratarResultadoQuery(this.dominio.query("EJBAttribute.allInstances()->select(attr | attr.class->includes(" + id + "))"));
            for (String idAttr : idsAttr) {
                Atributo atributo = SintaxeJava.getJavaAttribute();
                atributo.setVisibilidade(this.dominio.query(idAttr + ".visibility").replace("'", ""));
                atributo.setTipo(this.dominio.query(idAttr + ".type").replace("'", ""));
                atributo.setNome(this.dominio.query(idAttr + ".nameTyped").replace("'", ""));

                classe.addAtributo(atributo);
            }

            classe.persiste();
        }
    }
}
