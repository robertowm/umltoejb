package br.uff.ic.mda.transformer;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Domain {

    /* Attributes */
    protected List<DomainValidator> domainValidators;
    protected ModelPersistence modelPersistence;
    protected Logger logger;

    /* Constructors */
    public Domain(ModelPersistence modelPersistence) {
        this(modelPersistence, null);
    }

    public Domain(ModelPersistence modelPersistence, List<DomainValidator> domainValidators) {
        this.modelPersistence = modelPersistence;
        this.domainValidators = domainValidators;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    /* Methods */
    public String query(String query) throws Exception {
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
    public abstract boolean initializeMetamodelStructure() throws Exception;

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