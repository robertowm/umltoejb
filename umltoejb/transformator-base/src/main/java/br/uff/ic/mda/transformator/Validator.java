package br.uff.ic.mda.transformator;

public abstract class Validator {

    /* Attributes */
    protected DomainValidator domainValidator;

    /* Methods */
    public abstract boolean validate();

    /* Getters and Setters */
    public DomainValidator getDomainValidator() {
        return this.domainValidator;
    }

    public void setDomainValidator(DomainValidator domainValidator) {
        this.domainValidator = domainValidator;
    }

}
