package br.uff.ic.mda.xmicommandline;

import br.uff.ic.mda.transformer.EjbCodeGenerator;
import br.uff.ic.mda.transformer.EjbDomain;
import br.uff.ic.mda.transformer.TransformationContract;
import br.uff.ic.mda.transformer.UmlDomain;
import br.uff.ic.mda.transformer.UmlEjbDomain;
import br.uff.ic.mda.transformer.UmlEjbTransformer;
import br.uff.mda.secumltoaac.parser.xmiparser.XMIParser;
import br.uff.mda.secumltoaac.parser.xmiparser.uml.UMLAssociation;
import br.uff.mda.secumltoaac.parser.xmiparser.uml.UMLAssociationEnd;
import br.uff.mda.secumltoaac.parser.xmiparser.uml.UMLAttribute;
import br.uff.mda.secumltoaac.parser.xmiparser.uml.UMLClass;
import br.uff.mda.secumltoaac.parser.xmiparser.uml.UMLOperation;
import core.XEOS;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class App {

    private static XEOS xeos;
    private static UmlDomain umlDomain;
    private static EjbDomain ejbDomain;
    private static UmlEjbDomain joinedDomain;

    public static void main(String[] args) throws Exception {
        if (args == null || args.length == 0 || args[0] == null || args[0].isEmpty()) {
            throw new Exception("Specify the XMI diagram by command line.");
        }
        xeos = new XEOS();

        {
            xeos.createClassDiagram();

            umlDomain = new UmlDomain(xeos);
            ejbDomain = new EjbDomain(xeos);
            joinedDomain = new UmlEjbDomain(xeos);

            umlDomain.insertMetamodelClasses();
            ejbDomain.insertMetamodelClasses();
            joinedDomain.insertMetamodelClasses();

            umlDomain.insertMetamodelAttributes();
            ejbDomain.insertMetamodelAttributes();
            joinedDomain.insertMetamodelAttributes();

            umlDomain.insertMetamodelAssociations();
            ejbDomain.insertMetamodelAssociations();
            joinedDomain.insertMetamodelAssociations();

            umlDomain.insertMetamodelOperations();
            ejbDomain.insertMetamodelOperations();
            joinedDomain.insertMetamodelOperations();

            umlDomain.insertMetamodelInvariants();
            ejbDomain.insertMetamodelInvariants();
            joinedDomain.insertMetamodelInvariants();

            xeos.closeClassDiagram();
        }

        {
            xeos.createObjectDiagram();

            umlDomain.createSpecificationOfCurrentDiagram();
            ejbDomain.createSpecificationOfCurrentDiagram();
            joinedDomain.createSpecificationOfCurrentDiagram();

            loadXMIDiagram(args[0]);

            TransformationContract ct = new TransformationContract(umlDomain, ejbDomain, joinedDomain, new UmlEjbTransformer(umlDomain, ejbDomain, joinedDomain), new EjbCodeGenerator(ejbDomain, ""));
            ct.transform();

            xeos.closeObjectDiagram();
        }
    }

    private static void loadXMIDiagram(String path) throws Exception {
        XMIParser parser = new XMIParser(path);
//        XMIParser parser = new XMIParser("/home/edson/Desktop/UMLtoEJB/DiagramaExemplo.xmi");

        parser.parse();
        Set<Entry<String, UMLClass>> classSet = parser.getClasses().entrySet();
        Set<Entry<String, UMLAttribute>> attributeSet = parser.getAttributes().entrySet();

        //Inserting classes
        for (Entry<String, UMLClass> classEntry : classSet) {
            umlDomain.insertClass(processID(classEntry.getValue().getId()), classEntry.getValue().getName());
        }

        //Inserting attributes
        for (Entry<String, UMLClass> classEntry : classSet) {
            ArrayList<UMLAttribute> attributeArray = classEntry.getValue().getAttributes();
            for (UMLAttribute attribute : attributeArray) {
                String attributeTypeID = null;
                String attributeTypeName = null;
                String attributeType = null;
                if (parser.getDataTypes().containsKey(attribute.getType())) {
                    attributeTypeID = parser.getDataTypes().get(attribute.getType()).getId();
                    attributeTypeName = parser.getDataTypes().get(attributeTypeID).getName();
                    if ("String".equals(attributeTypeName)) {
                        attributeType = "UML" + attributeTypeName;
                    } else {
                        attributeType = parser.getDataTypes().get(attributeTypeID).getId();
                    }
                } else {
                    throw new Exception("Attribute (" + attribute.getType() + ") type not found in class" + classEntry.getValue().getName() + " " + classEntry.getValue().getId());
                }
                umlDomain.insertAttribute(processID(attribute.getId()), attribute.getName(), attribute.getVisibility(), attributeType, processID(classEntry.getValue().getId()));
            }
        }

        //Inserting Association End
        Set<Entry<String, UMLAssociation>> associationSet = parser.getAssociationMap().entrySet();

        for (Entry<String, UMLAssociation> associationEntry : associationSet) {

            HashMap<String, UMLAssociationEnd> associationEndMap = associationEntry.getValue().getAssociationEndMap();

            if (associationEndMap.size() != 2) {
                throw new Exception("Error in associations end size for " + associationEntry.getValue().getId() + " association");
            }

            Set<Entry<String, UMLAssociationEnd>> associationEndSet = associationEndMap.entrySet();
            ArrayList<UMLAssociationEnd> associationEndArray = new ArrayList<UMLAssociationEnd>();

            for (Entry<String, UMLAssociationEnd> associationEnd : associationEndSet) {
                associationEndArray.add(associationEnd.getValue());
            }

            UMLAssociationEnd association2 = associationEndArray.get(0);
            UMLAssociationEnd association1 = associationEndArray.get(1);
            UMLClass class1 = parser.getClasses().get(association1.getParticipantClassId());
            UMLClass class2 = parser.getClasses().get(association2.getParticipantClassId());

            umlDomain.insertAssociationEnd(processID(association2.getId()), association2.getName(), association2.getVisibility(), processID(class2.getId()), String.valueOf(association2.getLower()), String.valueOf(association2.getUpper()), association2.isAggregation(), processID(class1.getId()));//Class Id ??
            umlDomain.insertAssociationEnd(processID(association1.getId()), association1.getName(), association1.getVisibility(), processID(class1.getId()), String.valueOf(association1.getLower()), String.valueOf(association1.getUpper()), association1.isAggregation(), processID(class2.getId()));
            umlDomain.insertLinksBetweenAssociationEnds(processID(association2.getId()), processID(association1.getId()));
            umlDomain.insertAssociation(processID(associationEntry.getValue().getId()), associationEntry.getValue().getName(), processID(association2.getId()), processID(association1.getId()));
        }

        //Inserting Methods
        for (Entry<String, UMLClass> classeEntry : classSet) {
            ArrayList<UMLOperation> operationArray = classeEntry.getValue().getOperations();
            for (UMLOperation operation : operationArray) {
                System.out.println("Tipo da operacao: " + operation.getType() + " nome: " + parser.getDataTypes().get(operation.getType()).getName());
                String operationType = parser.getDataTypes().get(operation.getType()).getName();
                if ("String".equals(operationType) || "Boolean".equals(operationType)) {
                    operationType = "UML" + operationType;
                } else if ("List".equals(operationType)) {
                    String setId = "ID" + System.nanoTime();
                    umlDomain.insertSet(setId, operationType, processID(classeEntry.getValue().getId()));
                    umlDomain.insertOperation(processID(operation.getId()), operation.getName(), operation.getVisibility(), setId, processID(classeEntry.getValue().getId()));
                    return;
                } else {
                    operationType = processID(operation.getType());
//                    operationType = operationType.replaceAll("\\.", "");
                }
                umlDomain.insertOperation(processID(operation.getId()), operation.getName(), operation.getVisibility(), operationType, processID(classeEntry.getValue().getId()));
            }
        }
    }

    /**
     * @param oldID
     * @return
     */
    private static String processID(String oldID) {
        String newID = "ID";
        newID = newID.concat(oldID);
        newID = newID.replace("\\.", "");
        newID = newID.replace(":", "");
        newID = newID.replace("-", "");
        newID = newID.replace(" ", "");

        return newID;
    }
}
