package br.uff.ic.mda.transformer;

/**
 *
 * @author robertowm
 */
public class TransformationContract {

    protected Domain sourceDomain;
    protected Domain targetDomain;
    protected Domain intermediateDomain;
    protected Transformer transformer;
    protected CodeGenerator codeGenerator;

    public TransformationContract(
            Domain sourceDomain,
            Domain targetDomain,
            Domain intermediateDomain,
            Transformer transformer,
            CodeGenerator codeGenerator) {

        this.sourceDomain = sourceDomain;
        this.targetDomain = targetDomain;
        this.intermediateDomain = intermediateDomain;
        this.transformer = transformer;
        this.codeGenerator = codeGenerator;
    }

    protected boolean validate() {
        return transformer.validaDominios(sourceDomain, targetDomain, intermediateDomain)
                && codeGenerator.validateDomain(targetDomain);
    }

    public void transform() throws Exception {
        if (!validate()) {
            throw new Exception("The provided domain did not correspond with those used in the transformer and code generator.");
        }

        if (sourceDomain.checkAllInvariants()) {
            transformer.transform();
        } else {
            throw new Exception("The source domain invariants did not passed.");
        }

        if (intermediateDomain.checkAllInvariants()) {
            if (targetDomain.checkAllInvariants()) {
                codeGenerator.generate();
            } else {
                throw new Exception("The target domain invariants did not passed.");
            }
        } else {
            throw new Exception("The intermediate domain invariants did not passed.");
        }
    }
}
