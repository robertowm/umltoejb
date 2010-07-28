package br.uff.ic.mda.transformer;

import java.util.ArrayList;
import java.util.List;

public class DomainValidator {

    /* Attributes */
    protected BaseDomain domain;
    protected Validator validator;
    protected List<Property> properties;

    /* Methods for the list 'properties' */
    public boolean addProperty(Property property) {
        if (this.properties == null) {
            this.properties = new ArrayList<Property>();
        }
        return this.properties.add(property);
    }

    public boolean removeProperty(Property property) {
        if (this.properties == null) {
            return false;
        }
        return this.properties.remove(property);
    }

    /* Getters and Setters */
    public BaseDomain getDomain() {
        return this.domain;
    }

    public void setDomain(BaseDomain domain) {
        this.domain = domain;
    }

    public Validator getValidator() {
        return this.validator;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public List<Property> getProperties() {
        return this.properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}
