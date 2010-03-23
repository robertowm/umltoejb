package br.uff.ic.mda.transformator;

import java.util.ArrayList;
import java.util.List;

public abstract class Domain {

    /* Attributes */
    protected List<DomainValidator> domainValidators;
    protected ModelPersistence modelPersistence;


    /* Methods */
    public String query(String query) {
        return modelPersistence.query(query);
    }

    public Boolean validate() {
        Boolean result = true;
        for (DomainValidator domainValidator : domainValidators) {
            result &= domainValidator.getValidator().validate();
        }
        return result;
    }

    public abstract String print();

    /* Methods for the list 'domainValidators' */
    public boolean addDomainValidator(DomainValidator domainValidator) {
        if (this.domainValidators == null) {
            this.domainValidators = new ArrayList<DomainValidator>();
        }
        return this.domainValidators.add(domainValidator);
    }

    public boolean removeDomainValidator(DomainValidator domainValidator) {
        if (this.domainValidators == null) {
            return false;
        }
        return this.domainValidators.remove(domainValidator);
    }

    /* Getters and Setters */
    public List<DomainValidator> getDomainValidators() {
        return this.domainValidators;
    }

    public void setDomainValidators(List<DomainValidator> domainValidator) {
        this.domainValidators = domainValidator;
    }

    public ModelPersistence getModelPersistence() {
        return this.modelPersistence;
    }

    public void setModelPersistence(ModelPersistence modelPersistence) {
        this.modelPersistence = modelPersistence;
    }

}