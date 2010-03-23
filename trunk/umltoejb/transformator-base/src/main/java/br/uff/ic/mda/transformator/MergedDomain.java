package br.uff.ic.mda.transformator;

import java.util.ArrayList;
import java.util.List;

public abstract class MergedDomain extends Domain {

    /* Attributes */
    protected List<Domain> domains;

    /* Methods for the list 'domains' */
    public boolean addDomain(Domain domain) {
        if (this.domains == null) {
            this.domains = new ArrayList<Domain>();
        }
        return this.domains.add(domain);
    }

    public boolean removeDomain(Domain domain) {
        if (this.domains == null) {
            return false;
        }
        return this.domains.remove(domain);
    }

    /* Getters and Setters */
    public List<Domain> getDomains() {
        return this.domains;
    }

    public void setDomains(List<Domain> domains) {
        this.domains = domains;
    }

}
