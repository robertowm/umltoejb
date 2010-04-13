package br.uff.ic.mda.transformator;

import java.util.ArrayList;
import java.util.List;

public class DomainValidator {

    /* Attributes */
    protected Domain domain;
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
    public Domain getDomain() {
        return this.domain;
    }

    public void setDomain(Domain domain) {
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