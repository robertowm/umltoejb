package br.uff.ic.mda.transformer;

/**
 * Represents the transformation contract.
 * @author robertowm
 */
public class TransformationContract {

    protected Domain sourceDomain;
    protected Domain targetDomain;
    protected Domain joinedDomain;
    protected Transformer transformer;
    protected CodeGenerator codeGenerator;

    /**
     * Constructor
     * @param sourceDomain      source domain used in the transformation
     * @param targetDomain      target domain used in the transformation
     * @param joinedDomain      joined domain used in the transformation
     * @param transformer       the transformer used in the transformation
     * @param codeGenerator     the code generator used in the transformation
     */
    public TransformationContract(
            Domain sourceDomain,
            Domain targetDomain,
            Domain joinedDomain,
            Transformer transformer,
            CodeGenerator codeGenerator) {

        this.sourceDomain = sourceDomain;
        this.targetDomain = targetDomain;
        this.joinedDomain = joinedDomain;
        this.transformer = transformer;
        this.codeGenerator = codeGenerator;
    }

    /**
     * Validade if the transformer and the code generator uses the domains
     * defined by the constructor (source, target and joined)
     * @return if the domains is OK
     */
    protected boolean validate() {
        return transformer.validateDomains(sourceDomain, targetDomain, joinedDomain)
                && codeGenerator.validateDomain(targetDomain);
    }

    /**
     * Transform the source domain in the target domain and generate the source
     * code using the concept of transformation contract.
     * @throws Exception
     */
    public void transform() throws Exception {
        if (!validate()) {
            throw new Exception("The provided domain did not correspond with those used in the transformer and code generator.");
        }

        if (sourceDomain.checkAllInvariants()) {
            transformer.transform();
        } else {
            throw new Exception("The source domain invariants did not passed.");
        }

        if (joinedDomain.checkAllInvariants()) {
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
