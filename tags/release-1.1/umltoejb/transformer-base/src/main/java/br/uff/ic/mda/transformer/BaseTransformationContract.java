package br.uff.ic.mda.transformer;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTransformationContract {

    /* Attributes */
    protected BaseDomain input;
    protected BaseDomain output;
    protected MergedDomain merged;
    protected List<Validator> validators;

    /* Methods */
    public abstract Boolean transform();
    public abstract Boolean validate();

    /* Methods for the list 'validators' */
    public boolean addValidator(Validator validator) {
        if (this.validators == null) {
            this.validators = new ArrayList<Validator>();
        }
        return this.validators.add(validator);
    }

    public boolean removeValidator(Validator validator) {
        if (this.validators == null) {
            return false;
        }
        return this.validators.remove(validator);
    }

    /* Getters and Setters */
    public BaseDomain getInput() {
        return this.input;
    }

    public void setInput(BaseDomain input) {
        this.input = input;
    }

    public BaseDomain getOutput() {
        return this.output;
    }

    public void setOutput(BaseDomain output) {
        this.output = output;
    }

    public MergedDomain getMerged() {
        return this.merged;
    }

    public void setMerged(MergedDomain merged) {
        this.merged = merged;
    }

    public List<Validator> getValidators() {
        return this.validators;
    }

    public void setValidators(List<Validator> validators) {
        this.validators = validators;
    }
}
