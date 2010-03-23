package br.uff.ic.transformador.core;

/**
 *
 * @author robertowm
 */
public class ContratoTransformacao
        <   DomOr       extends Dominio,
            DomDes      extends Dominio,
            DomJuncao   extends Dominio,
            Trans       extends Transformador,
            GerCod      extends GeradorCodigo   > {

    protected DomOr     dominioOrigem;
    protected DomDes    dominioDestino;
    protected DomJuncao dominioJuncao;
    protected Trans     transformador;
    protected GerCod    geradorCodigo;


    public ContratoTransformacao(DomOr dominioOrigem, DomDes dominioDestino, DomJuncao dominioJuncao, Trans transformador, GerCod geradorCodigo) {
        this.dominioOrigem  = dominioOrigem;
        this.dominioDestino = dominioDestino;
        this.dominioJuncao  = dominioJuncao;
        this.transformador  = transformador;
        this.geradorCodigo  = geradorCodigo;
    }

    protected boolean valida() {
        return transformador.validaDominios(dominioOrigem, dominioDestino, dominioJuncao)
                && geradorCodigo.validaDominio(dominioDestino);
    }

    public void transformar() throws Exception {
        if (!valida()) {
            throw new Exception("Dominios fornecidos não equivalem aos utilizados na transformacao e na geração de código.");
        }

        transformador.transform();
        // Falta a parte dos validadores
        geradorCodigo.generate();
    }
}
