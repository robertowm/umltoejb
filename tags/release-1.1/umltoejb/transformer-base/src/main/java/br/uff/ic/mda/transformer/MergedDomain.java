package br.uff.ic.mda.transformer;

import java.util.ArrayList;
import java.util.List;

public abstract class MergedDomain extends BaseDomain {

    /* Attributes */
    protected List<BaseDomain> domains;

    /* Constructors */
    public MergedDomain(ModelPersistence modelPersistence) {
        super(modelPersistence);
    }

    public MergedDomain(ModelPersistence modelPersistence, List<DomainValidator> domainValidators) {
        super(modelPersistence, domainValidators);
    }

    /* Methods for the list 'domains' */
    public boolean addDomain(BaseDomain domain) {
        if (this.domains == null) {
            this.domains = new ArrayList<BaseDomain>();
        }
        return this.domains.add(domain);
    }

    public boolean removeDomain(BaseDomain domain) {
        if (this.domains == null) {
            return false;
        }
        return this.domains.remove(domain);
    }

    /* Getters and Setters */
    public List<BaseDomain> getDomains() {
        return this.domains;
    }

    public void setDomains(List<BaseDomain> domains) {
        this.domains = domains;
    }

}
