package br.uff.ic.mda.transformer;

import core.XEOS;

public class EjbDomain extends Domain {

    public EjbDomain(XEOS ieos) throws Exception {
        super(ieos);
    }

    @Override
    public void insertMetamodelClasses() throws Exception {
        this.ieos.insertClass("EJBModelElement");
        this.ieos.insertClass("EJBClassifier");
        this.ieos.insertClass("EJBTyped");
        this.ieos.insertClass("EJBDataSchema");
        this.ieos.insertClass("EJBDataType");
        this.ieos.insertClass("EJBSet");
        this.ieos.insertClass("EJBClass");
        this.ieos.insertClass("EJBDataSchemaElement");
        this.ieos.insertClass("EJBKeyClass");
        this.ieos.insertClass("EJBComponent");
        this.ieos.insertClass("EJBDataClass");
        this.ieos.insertClass("EJBSessionComponent");
        this.ieos.insertClass("EJBDataAssociation");
        this.ieos.insertClass("EJBEntityComponent");
        this.ieos.insertClass("Table");
        this.ieos.insertClass("EJBFeature");
        this.ieos.insertClass("EJBAssociationEnd");
        this.ieos.insertClass("EJBAttribute");
        this.ieos.insertClass("BusinessMethod");
        this.ieos.insertClass("EJBServingAttribute");
        this.ieos.insertClass("EJBParameter");

        this.ieos.insertGeneralization("EJBClassifier", "EJBModelElement");
        this.ieos.insertGeneralization("EJBTyped", "EJBModelElement");
        this.ieos.insertGeneralization("Table", "EJBModelElement");
        this.ieos.insertGeneralization("EJBDataSchema", "EJBModelElement");
        this.ieos.insertGeneralization("EJBDataSchemaElement", "EJBModelElement");
        this.ieos.insertGeneralization("EJBDataType", "EJBClassifier");
        this.ieos.insertGeneralization("EJBSet", "EJBDataType");
        this.ieos.insertGeneralization("EJBClass", "EJBClassifier");
        this.ieos.insertGeneralization("EJBComponent", "EJBClass");
        this.ieos.insertGeneralization("EJBKeyClass", "EJBClass");
        this.ieos.insertGeneralization("EJBDataClass", "EJBClass");
        this.ieos.insertGeneralization("EJBSessionComponent", "EJBComponent");
        this.ieos.insertGeneralization("EJBEntityComponent", "EJBComponent");
        this.ieos.insertGeneralization("EJBDataClass", "EJBDataSchemaElement");
        this.ieos.insertGeneralization("EJBDataAssociation", "EJBDataSchemaElement");
        this.ieos.insertGeneralization("EJBFeature", "EJBTyped");
        this.ieos.insertGeneralization("EJBParameter", "EJBTyped");
        this.ieos.insertGeneralization("BusinessMethod", "EJBFeature");
        this.ieos.insertGeneralization("EJBAttribute", "EJBFeature");
        this.ieos.insertGeneralization("EJBAssociationEnd", "EJBFeature");
        this.ieos.insertGeneralization("EJBServingAttribute", "EJBAssociationEnd");
    }

    @Override
    public void insertMetamodelAssociations() throws Exception {
        this.ieos.insertAssociation("EJBClassifier", "type", "1", "*", "typed", "EJBTyped");
        this.ieos.insertAssociation("EJBDataSchema", "package", "1", "*", "element", "EJBDataSchemaElement");
        this.ieos.insertAssociation("EJBSet", "set", "0..*", "1", "elementType", "EJBClassifier");
        this.ieos.insertAssociation("EJBClass", "class", "1", "0..*", "feature", "EJBFeature");
        this.ieos.insertAssociation("EJBEntityComponent", "entityComp", "1", "1..*", "usedTable", "Table");
        this.ieos.insertAssociation("EJBDataAssociation", "association", "0..1", "2", "associationEnds", "EJBAssociationEnd");
        this.ieos.insertAssociation("BusinessMethod", "operation", "1", "0..*", "parameter", "EJBParameter");
    }

    @Override
    public void insertMetamodelAttributes() throws Exception {
        this.ieos.insertAttribute("EJBModelElement", "name", "String");
        this.ieos.insertAttribute("EJBAssociationEnd", "lower", "String");
        this.ieos.insertAttribute("EJBAssociationEnd", "upper", "String");
        this.ieos.insertAttribute("EJBAssociationEnd", "composition", "Boolean");
        this.ieos.insertAttribute("EJBAttribute", "visibility", "String");
    }

    @Override
    public void insertMetamodelInvariants() throws Exception {
        // Todo EJBServingAttribute pertence a um EJBEntityComponent
        this.insertInvariant("everyEJBServingAttributebelongstoEJBEntityComponent", "EJBServingAttribute.allInstances()->collect(sa : EJBServingAttribute | sa.class)->forAll(c : EJBClass | c.oclIsKindOf(EJBEntityComponent))");

        // Toda EJBDataSchemaElement precisa ter um EJBDataSchema
        this.insertInvariant("compositionEJBDataSchemaElementEJBDataSchema", "EJBDataSchemaElement.allInstances().package->forAll(p : EJBDataSchema | p <> NULL_EJBDS)");

        // Toda EJBFeature precisa ter uma EJBClass
        this.insertInvariant("compositionEJBFeatureEJBClass", "EJBFeature.allInstances()->collect(f : EJBFeature | f.class)->forAll(c : EJBClassifier | c <> NULL_EJBC)");

        // Toda EJBParameter precisa ter um BusinessMethod
        this.insertInvariant("compositionEJBParameterBusinessMethod", "EJBParameter.allInstances()->collect(p : EJBParameter | p.operation)->forAll(o : BusinessMethod | o <> NULL_BM)");

        // Toda EJBTyped precisa ter um EJBClassifier
        this.insertInvariant("compositionEJBTypedEJBClassifier", "EJBTyped.allInstances()->collect(t : EJBTyped | t.type)->forAll(c : EJBClassifier | c <> NULL_EJBC)");

        // Toda EJBEntityComponent precisa ter, no minimo, um Table
//        this.insertInvariant("compositionEJBEntityComponentTable", "EJBEntityComponent.allInstances()->forAll(ec : EJBEntityComponent | ec.usedTable->size() >= 1)");

        // Toda EJBDataAssociation precisa ter dois EJBAssociationEnd
        this.insertInvariant("compositionEJBDataAssociationEJBAssociation", "EJBDataAssociation.allInstances()->forAll(da : EJBDataAssociation | da.associationEnds->size() = 2)");

        // Toda EJBModelElement precisa que o campo 'name' esteja preenchido, exceto EJBAssociation e EJBAssociationEnd
        this.insertInvariant("restrictionRequiredFieldNameEJBModelElement", "EJBModelElement.allInstances()->forAll(me : EJBModelElement | me.name <> '' or me.oclIsKindOf(EJBDataAssociation) or me.oclIsKindOf(EJBAssociationEnd))");

        // Todo BusinessMethod pertencente a um EJBEntityComponent que comeca seu nome com 'find' deve comecar tambem 'findAll', 'findBy', 'findOne' ou 'findMany'
        this.insertInvariant("restrictionMethodNametoEJBEntityComponent", "BusinessMethod.allInstances()->select(bm : BusinessMethod | bm.class->exists(c : EJBClass | c.oclIsTypeOf(EJBEntityComponent)))->select(bm : BusinessMethod | bm.name.size() >= 4 and bm.name.substring(1, 4) = 'find')->forAll(bm : BusinessMethod | bm.name.size() >= 7 and (if bm.name.size() = 7 then bm.name = 'findAll' else bm.name.substring(1, 6) = 'findBy' or bm.name.substring(1, 7) = 'findOne' or bm.name.substring(1, 8) = 'findMany' endif))");

        // Todo EJBSet precisa ter um tipo
        this.insertInvariant("everyEJBSetmusthaveatype", "EJBSet.allInstances()->forAll(s : EJBSet | s.elementType <> NULL_EJBC)");

        // ********** Novos invariantes **********
        // Todo metodo que comea com 'remove' tem que ser um remove method
        this.insertInvariant("prefixRemovemustbeaRemoveMethod", "BusinessMethod.allInstances()->forAll(bm | if bm.name.size() >= 6 then (if bm.name.substring(1, 6) = 'remove' then bm.isRemoveMethod() else true endif) else true endif)");

        // Todo metodo que comea com 'create' tem que ser um create method
        this.insertInvariant("prefixCreatemustbeaCreateMethod", "BusinessMethod.allInstances()->forAll(bm | if bm.name.size() >= 6 then (if bm.name.substring(1, 6) = 'create' then bm.isCreateMethod() else true endif) else true endif)");

        // Todo metodo que comea com 'find' tem que ser um finder method
        this.insertInvariant("prefixFindmustbeaFinderMethod", "BusinessMethod.allInstances()->forAll(bm | if bm.name.size() >= 4 then (if bm.name.substring(1, 4) = 'find' then bm.isFinderMethod() else true endif) else true endif)");

        // Nenhum metodo pode comear com 'ejb'
        this.insertInvariant("prefixEjbisforbidden", "BusinessMethod.allInstances()->forAll(bm | if bm.name.size() >= 3 then bm.name.substring(1, 3) <> 'ejb' else true endif)");

        // Todos os metodos precisar ser Remove, Create, Finder ou Home methods.
        this.insertInvariant("allowedTypesofMethods", "BusinessMethod.allInstances()->forAll(bm | bm.isRemoveMethod() or bm.isFinderMethod() or bm.isCreateMethod() or bm.isHomeMethod() or bm.name = 'null')");

        // N‹o pode ter BusinessMethods em uma mesma EJBClass com mesmo nome
//        this.insertInvariant("onlyOneBusinessMethodNameperEJBClass", "EJBClass.allInstances()->forAll(c:EJBClass | c.feature->select(f:EJBFeature | f.oclIsTypeOf(BusinessMethod))->forAll(a | c.feature->select(f|f.oclIsTypeOf(BusinessMethod))->size() - 1 = c.feature->select(f|f.oclIsTypeOf(BusinessMethod))->excluding(a)->size()))");
        this.insertInvariant("onlyOneBusinessMethodNameperEJBClass", "EJBClass.allInstances()->forAll(c:EJBClass |c.feature->select(f|f.oclIsTypeOf(BusinessMethod))->collect(f|f.oclAsType(BusinessMethod))->forAll(a| c.feature->select(f|f.oclIsTypeOf(BusinessMethod))->select(f| f.name = a.name and f.oclAsType(BusinessMethod).parameter->size() = a.oclAsType(BusinessMethod).parameter->size() and f.oclAsType(BusinessMethod).parameter->forAll(p|a.oclAsType(BusinessMethod).parameter->includes(p)))->size() = 1))");

        // N‹o pode ter EJBAttribute e EJBAssociationEnd em uma mesma EJBClass com mesmo nome
//        this.insertInvariant("onlyOneAttrOrAsscEndNameperEJBClass", "EJBClass.allInstances()->forAll(c:EJBClass | c.feature->select(f|f.oclIsTypeOf(EJBAttribute) or f.oclIsKindOf(EJBAssociationEnd))->forAll(a | c.feature->select(f|f.oclIsTypeOf(EJBAttribute) or f.oclIsKindOf(EJBAssociationEnd))->size() - 1 = c.feature->select(f|f.oclIsTypeOf(EJBAttribute) or f.oclIsKindOf(EJBAssociationEnd))->excluding(a)->size()))");
        this.insertInvariant("onlyOneAttrOrAsscEndNameperEJBClass", "EJBClass.allInstances()->forAll(c:EJBClass | c.feature->select(f|f.oclIsTypeOf(EJBAttribute) or f.oclIsKindOf(EJBAssociationEnd))->forAll(a | c.feature->select(f|f.oclIsTypeOf(EJBAttribute) or f.oclIsKindOf(EJBAssociationEnd))->select(f|f.name = a.name)->size() = 1))");
      
    }

    @Override
    public boolean insertMetamodelOperations() throws Exception {
        logger.debug("Inserting Ejb MetaModel Operations");
        boolean result = true;

        Object[] params;
        String[] param;

        // isCreateMethod -> verifica se um mŽtodo Ž um Create Method
        // *** verifica apenas o nome pois o retorno n‹o Ž poss’vel de saber com base na abstra‹o do metamodelo ***
        result &= ieos.insertOperation("BusinessMethod", "isCreateMethod", "Boolean", "if self.name.size() >= 6 then self.name.substring(1, 6) = 'create' and self.type->forAll(a | if a.oclIsTypeOf(EJBDataClass) then true else (if a.oclIsTypeOf(EJBSet) then a.oclAsType(EJBSet).elementType.oclIsTypeOf(EJBDataClass) else false endif) endif ) else false endif", new Object[0]);

        // isFinderMethod -> verifica se um mŽtodo Ž um Finder Method
        // *** verifica apenas o nome pois o retorno n‹o Ž poss’vel de saber com base na abstra‹o do metamodelo ***
        result &= ieos.insertOperation("BusinessMethod", "isFinderMethod", "Boolean", "if self.name.size() >= 4 then self.name.substring(1, 4) = 'find' and self.type->forAll(a | if a.oclIsTypeOf(EJBDataClass) then true else (if a.oclIsTypeOf(EJBSet) then a.oclAsType(EJBSet).elementType.oclIsTypeOf(EJBDataClass) else false endif) endif ) else false endif", new Object[0]);

        // isRemoveMethod -> verifica se um mŽtodo Ž um Remove Method
        result &= ieos.insertOperation("BusinessMethod", "isRemoveMethod", "Boolean", "if self.name.size() >= 6 then self.name.substring(1, 6) = 'remove' and self.type->forAll(a | a.name = 'null') and self.parameter->notEmpty() else false endif", new Object[0]);

        // isHomeMethod -> verifica se um mŽtodo Ž um Home Method
        result &= ieos.insertOperation("BusinessMethod", "isHomeMethod", "Boolean", "if self.name.size() >= 6 then self.name.substring(1, 6) <> 'remove' and self.name.substring(1, 6) <> 'create' else true endif and if self.name.size() >= 4 then self.name.substring(1,4) <> 'find' else true endif and self.parameter.type->forAll(p | if p.oclIsTypeOf(EJBDataType) then true else (if p.oclIsTypeOf(EJBSet) then p.oclAsType(EJBSet).elementType.oclIsTypeOf(EJBDataType) else false endif) endif)", new Object[0]);

        if (!result) {
            throw new Exception("It was not possible to insert all the operations in the EJB model");
        }

        return result;
    }

    @Override
    public void createSpecificationOfCurrentDiagram() throws Exception {
        this.ieos.insertObject("EJBDataSchemaElement", "NULL_EJBDSE");
        this.ieos.insertValue("EJBDataSchemaElement", "name", "NULL_EJBDSE", "null");
        this.ieos.insertObject("EJBDataSchema", "NULL_EJBDS");
        this.ieos.insertValue("EJBDataSchema", "name", "NULL_EJBDS", "null");
        this.ieos.insertObject("EJBClassifier", "NULL_EJBC");
        this.ieos.insertValue("EJBClassifier", "name", "NULL_EJBC", "null");
        this.ieos.insertObject("EJBTyped", "NULL_EJBT");
        this.ieos.insertValue("EJBTyped", "name", "NULL_EJBT", "null");
        this.ieos.insertObject("BusinessMethod", "NULL_BM");
        this.ieos.insertValue("BusinessMethod", "name", "NULL_BM", "null");

        this.ieos.insertObject("EJBDataType", "EJBInteger");
        this.ieos.insertValue("EJBDataType", "name", "EJBInteger", "Integer");
        this.ieos.insertObject("EJBDataType", "EJBDouble");
        this.ieos.insertValue("EJBDataType", "name", "EJBDouble", "Double");
        this.ieos.insertObject("EJBDataType", "EJBString");
        this.ieos.insertValue("EJBDataType", "name", "EJBString", "String");
        this.ieos.insertObject("EJBDataType", "EJBDate");
        this.ieos.insertValue("EJBDataType", "name", "EJBDate", "Date");
        this.ieos.insertObject("EJBDataType", "EJBBoolean");
        this.ieos.insertValue("EJBDataType", "name", "EJBBoolean", "Boolean");
    }

    // Specific methods of this domain

    // EJBDataSchema
    public boolean insertEJBDataSchema(String id, String name) throws Exception {
        return      insertEJBDataSchemaStub(id, name);
    }
    public boolean insertEJBDataSchemaStub(String id, String name) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBDataSchema: the diagram must be created");
            return false;
        }
        this.ieos.insertObject("EJBDataSchema", id);
        this.ieos.insertValue("EJBDataSchema", "name", id, name == null ? "" : name);
        return true;
    }

    // EJBDataType
    public boolean insertEJBDataType(String id, String name) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBDataType: the diagram must be created");
            return false;
        }

        this.ieos.insertObject("EJBDataType", id);
        this.ieos.insertValue("EJBDataType", "name", id, name == null ? "" : name);
        return true;
    }

    // EJBSet
    public boolean insertEJBSet(String id, String name, String idType) throws Exception {
        return      insertEJBSetStub(id, name)
                &&  insertEJBSetTypeLink(id, idType);
    }
    public boolean insertEJBSetStub(String id, String name) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBDataType: the diagram must be created");
            return false;
        }

        this.ieos.insertObject("EJBSet", id);
        this.ieos.insertValue("EJBSet", "name", id, name == null ? "" : name);
        return true;
    }
    public boolean insertEJBSetTypeLink(String id, String idType) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBDataType: the diagram must be created");
            return false;
        }
        this.ieos.insertLink("EJBSet", id, "set", "elementType", idType, "EJBClassifier");
        return true;
    }

    // EJBKeyClass
    public boolean insertEJBKeyClass(String id, String name) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBKeyClass: the diagram must be created");
            return false;
        }

        this.ieos.insertObject("EJBKeyClass", id);
        this.ieos.insertValue("EJBKeyClass", "name", id, name == null ? "" : name);
        return true;
    }

    //EJBDataClass
    public boolean insertEJBDataClass(String id, String name, String ejbDataSchemaId) throws Exception {
        return      insertEJBDataClassStub(id, name)
                &&  insertEJBDataClassSchemaLink(id, ejbDataSchemaId);
    }
    public boolean insertEJBDataClassStub(String id, String name) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBDataClass: the diagram must be created");
            return false;
        }
        this.ieos.insertObject("EJBDataClass", id);
        this.ieos.insertValue("EJBDataClass", "name", id, name == null ? "" : name);
        return true;
    }
    public boolean insertEJBDataClassSchemaLink(String id, String ejbDataSchemaId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBDataClass: the diagram must be created");
            return false;
        }
        this.ieos.insertLink("EJBDataSchema", ejbDataSchemaId, "package", "element", id, "EJBDataClass");
        return true;
    }

    // EJBDataAssociation
    public boolean insertEJBDataAssociation(String id, String name, String ejbDataSchemaId, String... ejbAssociationEnds) throws Exception {
        return      insertEJBDataAssociationStub(id, name)
                &&  insertEJBDataAssociationSchemaLink(id, ejbDataSchemaId)
                &&  insertEJBDataAssociationEndLinks(id, ejbAssociationEnds);
    }
    public boolean insertEJBDataAssociationStub(String id, String name) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBDataAssociation: the diagram must be created");
            return false;
        }
        this.ieos.insertObject("EJBDataAssociation", id);
        this.ieos.insertValue("EJBDataAssociation", "name", id, name == null ? "" : name);
        return true;
    }
    public boolean insertEJBDataAssociationSchemaLink(String id, String ejbDataSchemaId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBDataAssociation: the diagram must be created");
            return false;
        }
        this.ieos.insertLink("EJBDataSchema", ejbDataSchemaId, "package", "element", id, "EJBDataAssociation");
        return true;
    }
    public boolean insertEJBDataAssociationEndLinks(String id, String... ejbAssociationEnds) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBDataAssociation: the diagram must be created");
            return false;
        }
        for (String ejbAssociationEnd : ejbAssociationEnds) {
            this.ieos.insertLink("EJBDataAssociation", id, "association", "associationEnds", ejbAssociationEnd, "EJBAssociationEnd");
        }
        return true;
    }

    // EJBSessionComponent
    public boolean insertEJBSessionComponent(String id, String name) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBSessionComponent: the diagram must be created");
            return false;
        }

        this.ieos.insertObject("EJBSessionComponent", id);
        this.ieos.insertValue("EJBSessionComponent", "name", id, name == null ? "" : name);
        return true;
    }

    // EJBEntityComponent
    public boolean insertEJBEntityComponent(String id, String name) throws Exception {
        return      insertEJBEntityComponentStub(id, name);
    }
    public boolean insertEJBEntityComponentStub(String id, String name) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBEntityComponent: the diagram must be created");
            return false;
        }
        this.ieos.insertObject("EJBEntityComponent", id);
        this.ieos.insertValue("EJBEntityComponent", "name", id, name == null ? "" : name);
        return true;
    }

    // Table
    public boolean insertTable(String id, String name, String ejbEntityComponentId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a Table: the diagram must be created");
            return false;
        }

        this.ieos.insertObject("Table", id);
        this.ieos.insertValue("Table", "name", id, name == null ? "" : name);

        this.ieos.insertLink("EJBEntityComponent", ejbEntityComponentId, "entityComp", "usedTable", id, "Table");
        return true;
    }

    // BusinessMethod
    public boolean insertBusinessMethod(String id, String name, String typeId, String ejbClassId) throws Exception {
        return      insertBusinessMethodStub(id, name)
                &&  insertBusinessMethodTypeLink(id, typeId)
                &&  insertBusinessMethodClassLink(id, ejbClassId);
    }
    public boolean insertBusinessMethodStub(String id, String name) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a BusinessMethod: the diagram must be created");
            return false;
        }

        this.ieos.insertObject("BusinessMethod", id);
        this.ieos.insertValue("BusinessMethod", "name", id, name == null ? "" : name);

        return true;
    }
    public boolean insertBusinessMethodTypeLink(String id, String typeId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a BusinessMethod: the diagram must be created");
            return false;
        }

        this.ieos.insertLink("BusinessMethod", id, "typed", "type", typeId, "EJBClassifier");

        return true;
    }
    public boolean insertBusinessMethodClassLink(String id, String ejbClassId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert a BusinessMethod: the diagram must be created");
            return false;
        }

        this.ieos.insertLink("BusinessMethod", id, "feature", "class", ejbClassId, "EJBClass");

        return true;
    }

    // EJBParameter
    public boolean insertEJBParameter(String id, String name, String typeId, String businessMethodId) throws Exception {
        return      insertEJBParameterStub(id, name)
                &&  insertEJBParameterTypeLink(id, typeId)
                &&  insertEJBParameterBusinessMethodLink(id, businessMethodId);
    }
    public boolean insertEJBParameterStub(String id, String name) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBParameter: the diagram must be created");
            return false;
        }
        this.ieos.insertObject("EJBParameter", id);
        this.ieos.insertValue("EJBParameter", "name", id, name == null ? "" : name);
        return true;
    }
    public boolean insertEJBParameterTypeLink(String id, String typeId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBParameter: the diagram must be created");
            return false;
        }
        this.ieos.insertLink("EJBParameter", id, "typed", "type", typeId, "EJBClassifier");
        return true;
    }
    public boolean insertEJBParameterBusinessMethodLink(String id, String businessMethodId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBParameter: the diagram must be created");
            return false;
        }
        this.ieos.insertLink("BusinessMethod", businessMethodId, "operation", "parameter", id, "EJBParameter");
        return true;
    }

    // EJBAttribute
    public boolean insertEJBAttribute(String id, String name, String visibility, String typeId, String ejbClassId) throws Exception {
        return      insertEJBAttributeStub(id, name, visibility)
                &&  insertEJBAttributeTypeLink(id, typeId)
                &&  insertEJBAttributeClassLink(id, ejbClassId);
    }
    public boolean insertEJBAttributeStub(String id, String name, String visibility) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBAttribute: the diagram must be created");
            return false;
        }
        this.ieos.insertObject("EJBAttribute", id);
        this.ieos.insertValue("EJBAttribute", "name", id, name == null ? "" : name);
        this.ieos.insertValue("EJBAttribute", "visibility", id, visibility == null ? "" : visibility);
        return true;
    }
    public boolean insertEJBAttributeTypeLink(String id, String typeId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBAttribute: the diagram must be created");
            return false;
        }
        this.ieos.insertLink("EJBAttribute", id, "typed", "type", typeId, "EJBClassifier");
        return true;
    }
    public boolean insertEJBAttributeClassLink(String id, String ejbClassId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBAttribute: the diagram must be created");
            return false;
        }
        this.ieos.insertLink("EJBAttribute", id, "feature", "class", ejbClassId, "EJBClass");
        return true;
    }

    // EJBAssociationEnd
    public boolean insertEJBAssociationEnd(String id, String name, String lower, String upper, Boolean composition, String typeId, String ejbClassId) throws Exception {
        return      insertEJBAssociationEndStub(id, name, lower, upper, composition)
                &&  insertEJBAssociationEndTypeLink(id, typeId)
                &&  insertEJBAssociationEndClassLink(id, ejbClassId);
    }
    public boolean insertEJBAssociationEndStub(String id, String name, String lower, String upper, Boolean composition) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBAssociationEnd: the diagram must be created");
            return false;
        }

        this.ieos.insertObject("EJBAssociationEnd", id);
        this.ieos.insertValue("EJBAssociationEnd", "name", id, name == null ? "" : name);
        this.ieos.insertValue("EJBAssociationEnd", "lower", id, lower == null ? "" : lower);
        this.ieos.insertValue("EJBAssociationEnd", "upper", id, upper == null ? "" : upper);
        this.ieos.insertValue("EJBAssociationEnd", "composition", id, composition == true ? "true" : "false");

        return true;
    }
    public boolean insertEJBAssociationEndTypeLink(String id, String typeId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBAssociationEnd: the diagram must be created");
            return false;
        }

        this.ieos.insertLink("EJBAssociationEnd", id, "typed", "type", typeId, "EJBClassifier");

        return true;
    }
    public boolean insertEJBAssociationEndClassLink(String id, String ejbClassId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBAssociationEnd: the diagram must be created");
            return false;
        }

        this.ieos.insertLink("EJBAssociationEnd", id, "feature", "class", ejbClassId, "EJBClass");

        return true;
    }

    // EJBServingAttribute
    public boolean insertEJBServingAttribute(String id, String name, String lower, String upper, Boolean composition, String typeId, String ejbClassId) throws Exception {
        return      insertEJBServingAttributeStub(id, name, lower, upper, composition)
                &&  insertEJBServingAttributeTypeLink(id, typeId)
                &&  insertEJBServingAttributeClassLink(id, ejbClassId);
    }
    public boolean insertEJBServingAttributeStub(String id, String name, String lower, String upper, Boolean composition) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBServingAttribute: the diagram must be created");
            return false;
        }
        this.ieos.insertObject("EJBServingAttribute", id);
        this.ieos.insertValue("EJBServingAttribute", "name", id, name == null ? "" : name);
        this.ieos.insertValue("EJBServingAttribute", "lower", id, lower == null ? "" : lower);
        this.ieos.insertValue("EJBServingAttribute", "upper", id, upper == null ? "" : upper);
        this.ieos.insertValue("EJBServingAttribute", "composition", id, composition == true ? "true" : "false");
        return true;
    }
    public boolean insertEJBServingAttributeTypeLink(String id, String typeId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBServingAttribute: the diagram must be created");
            return false;
        }
        this.ieos.insertLink("EJBServingAttribute", id, "typed", "type", typeId, "EJBClassifier");
        return true;
    }
    public boolean insertEJBServingAttributeClassLink(String id, String ejbClassId) throws Exception {
        if (this.ieos.getActualState() != 3) {
            logger.error("Error when insert an EJBServingAttribute: the diagram must be created");
            return false;
        }
        this.ieos.insertLink("EJBServingAttribute", id, "feature", "class", ejbClassId, "EJBClass");
        return true;
    }
}
