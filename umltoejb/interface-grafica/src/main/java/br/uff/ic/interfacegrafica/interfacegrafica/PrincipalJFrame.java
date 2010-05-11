/*
 * PrincipalJFrame.java
 *
 * Created on 15/12/2009, 21:55:28
 */
package br.uff.ic.interfacegrafica.interfacegrafica;

import br.uff.mda.secumltoaac.parser.xmiparser.XMIParser;
import br.uff.mda.secumltoaac.parser.xmiparser.uml.UMLAssociation;
import br.uff.mda.secumltoaac.parser.xmiparser.uml.UMLAssociationEnd;
import br.uff.mda.secumltoaac.parser.xmiparser.uml.UMLAttribute;
import br.uff.mda.secumltoaac.parser.xmiparser.uml.UMLClass;
import br.uff.mda.secumltoaac.parser.xmiparser.uml.UMLOperation;
import br.uff.ic.mda.transformer.TransformationContract;
import br.uff.ic.mda.transformer.EjbDomain;
import br.uff.ic.mda.transformer.UmlDomain;
import br.uff.ic.mda.transformer.UmlEjbDomain;
import br.uff.ic.mda.transformer.EjbCodeGenerator;
import br.uff.ic.mda.transformer.UmlEjbTransformer;
import br.uff.mda.secumltoaac.parser.xmiparser.uml.UMLElement;
import core.XEOS;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author robertowm
 */
public class PrincipalJFrame extends javax.swing.JFrame {

    private UmlDomain aUml;
    private EjbDomain aEjb;
    private XEOS xeos;

    private void criarDiagramaObjetosEjb() throws Exception {
        aEjb.insertEJBDataSchema("standardBreakfast_DS_ID", "standardBreakfast");
        aEjb.insertEJBDataSchema("comestible_DS_ID", "comestible");

        aEjb.insertEJBDataClass("StandardBreakfast_DC_ID", "StandardBreakfast", "standardBreakfast_DS_ID");
        aEjb.insertEJBDataClass("Part_DC_ID", "Part", "standardBreakfast_DS_ID");
        aEjb.insertEJBDataClass("Comestible_DC_ID", "Comestible", "comestible_DS_ID");

        aEjb.insertEJBKeyClass("StandardBreakfast_KC_ID", "StandardBreakfast");
        aEjb.insertEJBKeyClass("Part_KC_ID", "Part");
        aEjb.insertEJBKeyClass("Comestible_KC_ID", "Comestible");

        aEjb.insertEJBEntityComponent("StandardBreakfast_EC_ID", "StandardBreakfast");
        aEjb.insertEJBEntityComponent("Comestible_EC_ID", "Comestible");

        aEjb.insertEJBAssociationEnd("standardBreakfast_AE_ID", "standardBreakfast", "1", "1", true, "StandardBreakfast_DC_ID", "StandardBreakfast_DC_ID");
        aEjb.insertEJBAssociationEnd("part_AE_ID", "part", "0", "*", false, "Part_DC_ID", "Part_DC_ID");
        aEjb.insertEJBDataAssociation("standardBreakfastPart_DA_ID", "standardBreakfastPart", "standardBreakfast_DS_ID", "standardBreakfast_AE_ID", "part_AE_ID");

        aEjb.insertEJBAssociationEnd("comestibleKey_AE_ID", "comestibleKey", "1", "1", false, "Comestible_KC_ID", "Comestible_KC_ID");
        aEjb.insertEJBAssociationEnd("parts_AE_ID", "parts", "0", "*", false, "Part_DC_ID", "Part_DC_ID");
        aEjb.insertEJBDataAssociation("partComestibleKey_DA_ID", "partComestibleKey", "standardBreakfast_DS_ID", "comestibleKey_AE_ID", "parts_AE_ID");

        aEjb.insertEJBServingAttribute("standardBreakfastData_SA_ID", "standardBreakfastData", "1", "1", false, "StandardBreakfast_DC_ID", "StandardBreakfast_DC_ID");
        aEjb.insertEJBServingAttribute("standardBreakfastEntity_SA_ID", "standardBreakfastEntity", "0", "*", false, "StandardBreakfast_EC_ID", "StandardBreakfast_EC_ID");
        aEjb.insertEJBDataAssociation("standardBreakfast_DA_ID", "standardBreakfast", "standardBreakfast_DS_ID", "standardBreakfastData_SA_ID", "standardBreakfastEntity_SA_ID");

        aEjb.insertEJBServingAttribute("comestibleData_SA_ID", "comestibleData", "1", "1", false, "Comestible_DC_ID", "Comestible_DC_ID");
        aEjb.insertEJBServingAttribute("comestibleEntity_SA_ID", "comestibleEntity", "0", "*", false, "Comestible_EC_ID", "Comestible_EC_ID");
        aEjb.insertEJBDataAssociation("comestibleAssociation_DA_ID", "comestibleAssociation", "standardBreakfast_DS_ID", "comestibleData_SA_ID", "comestibleEntity_SA_ID");

        aEjb.insertEJBAttribute("standardBreakfastID_A_ID", "standardBreakfastID", "private", "int", "StandardBreakfast_DC_ID");
        aEjb.insertEJBAttribute("name_A_ID", "name", "private", "String", "StandardBreakfast_DC_ID");
        aEjb.insertEJBAttribute("price_A_ID", "price", "private", "float", "StandardBreakfast_DC_ID");
        aEjb.insertEJBAttribute("style_A_ID", "style", "private", "String", "StandardBreakfast_DC_ID");
        aEjb.insertEJBAttribute("quantity_A_ID", "quantity", "private", "int", "Part_DC_ID");
        aEjb.insertEJBAttribute("standardBreakfastID_A_ID", "standardBreakfastID", "private", "int", "StandardBreakfast_KC_ID");
        aEjb.insertEJBAttribute("standardBreakfastID_A_ID", "standardBreakfastID", "private", "int", "Part_KC_ID");
        aEjb.insertEJBAttribute("comestibleID_A_ID", "comestibleID", "private", "int", "Part_KC_ID");
        aEjb.insertEJBAttribute("comestibleID_A_ID", "comestibleID", "private", "int", "Comestible_KC_ID");
        aEjb.insertEJBAttribute("comestibleID_A_ID", "comestibleID", "private", "int", "Comestible_DC_ID");
        aEjb.insertEJBAttribute("name_A_ID", "name", "private", "String", "Comestible_DC_ID");
        aEjb.insertEJBAttribute("minimalQuantity_A_ID", "minimalQuantity", "private", "int", "Comestible_DC_ID");
        aEjb.insertEJBAttribute("price_A_ID", "price", "private", "float", "Comestible_DC_ID");
        aEjb.insertEJBAttribute("transportForm_A_ID", "transportForm", "private", "String", "Comestible_DC_ID");
    }

//    private void criarDiagramaObjetosUml() throws Exception {
//        aUml.insertClass("Customer_ID", "Customer");
//        aUml.insertClass("BreakfastOrder_ID", "BreakfastOrder");
//        aUml.insertClass("Breakfast_ID", "Breakfast");
//        aUml.insertClass("StandardBreakfast_ID", "StandardBreakfast");
//        aUml.insertClass("Comestible_ID", "Comestible");
//
//        aUml.insertAttribute("accountNumberC_ID", "accountNumberC", "public", "Integer", "Customer_ID");
//        aUml.insertAttribute("addressC_ID", "addressC", "public", "String", "Customer_ID");
//        aUml.insertAttribute("deliveryAddressBO_ID", "deliveryAddressBO", "public", "String", "BreakfastOrder_ID");
//        aUml.insertAttribute("deliveryDateBO_ID", "deliveryDateBO", "public", "Date", "BreakfastOrder_ID");
//        aUml.insertAttribute("deliveryTimeBO_ID", "deliveryTimeBO", "public", "Date", "BreakfastOrder_ID");
//        aUml.insertAttribute("discountBO_ID", "discountBO", "public", "Double", "BreakfastOrder_ID");
//        aUml.insertAttribute("orderDateBO_ID", "orderDateBO", "public", "Date", "BreakfastOrder_ID");
//        aUml.insertAttribute("numberB_ID", "numberB", "public", "Integer", "Breakfast_ID");
//        aUml.insertAttribute("nameSB_ID", "nameSB", "public", "String", "StandardBreakfast_ID");
//        aUml.insertAttribute("priceSB_ID", "priceSB", "public", "Real", "StandardBreakfast_ID");
//        aUml.insertAttribute("styleSB_ID", "styleSB", "public", "String", "StandardBreakfast_ID");
//        aUml.insertAttribute("nameCo_ID", "nameCo", "public", "String", "Comestible_ID");
//        aUml.insertAttribute("minimalQuantityCo_ID", "minimalQuantityCo", "public", "Integer", "Comestible_ID");
//        aUml.insertAttribute("priceComestibleCo_ID", "priceComestibleCo", "public", "Double", "Comestible_ID");
//        aUml.insertAttribute("transportFormCo_ID", "transportFormCo", "public", "String", "Comestible_ID");
//
//        aUml.insertAssociationEnd("customerBO_ID", "customerBO", "public", "Customer_ID", "1", "1", false, "BreakfastOrder_ID");
//        aUml.insertAssociationEnd("ordersC_ID", "ordersC", "public", "BreakfastOrder_ID", "1", "*", false, "Customer_ID");
//        aUml.insertLinksBetweenAssociationEnds("ordersC_ID", "customerBO_ID");
//        aUml.insertAssociation("Customer_BreakfastOrder_ID", "Customer_BreakfastOrder", "customerBO_ID", "ordersC_ID");
//
//        aUml.insertAssociationEnd("orderB_ID", "orderB", "public", "BreakfastOrder_ID", "1", "1", true, "Breakfast_ID");
//        aUml.insertAssociationEnd("breakfastsBO_ID", "breakfastsBO", "public", "Breakfast_ID", "1", "*", false, "BreakfastOrder_ID");
//        aUml.insertLinksBetweenAssociationEnds("orderB_ID", "breakfastsBO_ID");
//        aUml.insertAssociation("BreakfastOrder_Breakfast_ID", "BreakfastOrder_Breakfast", "orderB_ID", "breakfastsBO_ID");
//
//        aUml.insertAssociationEnd("breakfastsSB_ID", "breakfastsSB", "public", "Breakfast_ID", "0", "*", false, "StandardBreakfast_ID");
//        aUml.insertAssociationEnd("standardB_ID", "standardB", "public", "StandardBreakfast_ID", "1", "1", false, "Breakfast_ID");
//        aUml.insertLinksBetweenAssociationEnds("breakfastsSB_ID", "standardB_ID");
//        aUml.insertAssociation("Breakfast_StandardBreakfast_ID", "Breakfast_StandardBreakfast", "breakfastsSB_ID", "standardB_ID");
//
//        aUml.insertAssociationEnd("standardsC_ID", "standardsC", "public", "StandardBreakfast_ID", "0", "*", false, "Comestible_ID");
//        aUml.insertAssociationEnd("comestibleSB_ID", "comestibleSB", "public", "Comestible_ID", "1", "*", false, "StandardBreakfast_ID");
//        aUml.insertLinksBetweenAssociationEnds("standardsC_ID", "comestibleSB_ID");
//        aUml.insertAssociationClass("PartClass_ID", "PartClass", "standardsC_ID", "comestibleSB_ID");
//        aUml.insertAttribute("quantityPC_ID", "quantityPC", "public", "Integer", "PartClass_ID");
//
//        aUml.insertAssociationEnd("breakfastsC_ID", "breakfastsC", "public", "Breakfast_ID", "0", "*", false, "Comestible_ID");
//        aUml.insertAssociationEnd("comestibleItemB_ID", "comestibleItemB", "public", "Comestible_ID", "0", "*", false, "Breakfast_ID");
//        aUml.insertLinksBetweenAssociationEnds("breakfastsC_ID", "comestibleItemB_ID");
//        aUml.insertAssociationClass("ChangeClass_ID", "ChangeClass", "breakfastsC_ID", "comestibleItemB_ID");
//        aUml.insertAttribute("quantityCC_ID", "quantityCC", "public", "Integer", "ChangeClass_ID");
//
//        aUml.insertOperation("createOrder_ID", "createOrder", "public", "Boolean", "Customer_ID");
//        aUml.insertOperation("calculatePrice_ID", "calculatePrice", "public", "Double", "BreakfastOrder_ID");
//    }
    private void criarDiagramaObjetosUml() throws Exception {


        XMIParser parser = new XMIParser("/home/edson/Desktop/UMLtoEJB/DiagramaExemplo.xmi");


        parser.parse();
        Set<Entry<String, UMLClass>> classSet = parser.getClasses().entrySet();
        Set<Entry<String, UMLAttribute>> attributeSet = parser.getAttributes().entrySet();





        //Inserting classes


        for (Entry<String, UMLClass> classEntry : classSet) {


            aUml.insertClass(processID(classEntry.getValue().getId()), classEntry.getValue().getName());
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

                aUml.insertAttribute(processID(attribute.getId()), attribute.getName(), attribute.getVisibility(), attributeType, processID(classEntry.getValue().getId()));
            }

        }



        //Inserting Association End
        processAssociations(parser);

        //Inserting Methods

        for (Entry<String, UMLClass> classeEntry : classSet) {

            ArrayList<UMLOperation> operationArray = classeEntry.getValue().getOperations();
            for (UMLOperation operation : operationArray) {
                System.out.println("Tipo da operacao: " + operation.getType() + " nome: " + parser.getDataTypes().get(operation.getType()).getName());
                String operationType = parser.getDataTypes().get(operation.getType()).getName();
                if ("String".equals(operationType) || "Boolean".equals(operationType)) {
                    operationType = "UML" + operationType;
                } else if ("List".equals(operationType)) {
                    String setId= "ID"+System.nanoTime();
                    aUml.insertSet(setId, operationType, processID(classeEntry.getValue().getId()));
                    aUml.insertOperation(processID(operation.getId()), operation.getName(), operation.getVisibility(), setId, processID(classeEntry.getValue().getId()));
                    return ;

                }else {
                    operationType = processID(operation.getType());
//                    operationType = operationType.replaceAll("\\.", "");
                }
                aUml.insertOperation(processID(operation.getId()), operation.getName(), operation.getVisibility(), operationType, processID(classeEntry.getValue().getId()));
            }
        }



    }

    private void processAssociations(XMIParser parser) throws Exception {
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



            aUml.insertAssociationEnd(processID(association2.getId()), association2.getName(), association2.getVisibility(), processID(class2.getId()), String.valueOf(association2.getLower()), String.valueOf(association2.getUpper()), association2.isAggregation(), processID(class1.getId()));//Class Id ??

            aUml.insertAssociationEnd(processID(association1.getId()), association1.getName(), association1.getVisibility(), processID(class1.getId()), String.valueOf(association1.getLower()), String.valueOf(association1.getUpper()), association1.isAggregation(), processID(class2.getId()));

            aUml.insertLinksBetweenAssociationEnds(processID(association2.getId()), processID(association1.getId()));

            aUml.insertAssociation(processID(associationEntry.getValue().getId()), associationEntry.getValue().getName(), processID(association2.getId()), processID(association1.getId()));



        }

    }

    /**
     * @deprecated
     * @param oldID
     * @return
     */
    private String processID(String oldID) {

        String newID = "ID";
        newID = newID.concat(oldID);
        newID = newID.replace("\\.", "");
        newID = newID.replace(":", "");
        newID = newID.replace("-", "");
        newID = newID.replace(" ", "");

        return newID;

    }

    private void criarDiagramaUmlArtigo() throws Exception {
        aUml.insertClass("User_ID", "User");
        aUml.insertClass("Article_ID", "Article");
        aUml.insertClass("Comment_ID", "Comment");

        aUml.insertAttribute("firstName_u_ID", "firstName", "public", "UMLString", "User_ID");
        aUml.insertAttribute("lastName_u_ID", "lastName", "public", "UMLString", "User_ID");
        aUml.insertAttribute("nickName_u_ID", "nickName", "public", "UMLString", "User_ID");
        aUml.insertAttribute("login_u_ID", "login", "public", "UMLString", "User_ID");
        aUml.insertAttribute("password_u_ID", "password", "public", "UMLString", "User_ID");
        aUml.insertAttribute("email_u_ID", "email", "public", "UMLString", "User_ID");

        aUml.insertAttribute("title_a_ID", "title", "public", "UMLString", "Article_ID");
        aUml.insertAttribute("text_a_ID", "text", "public", "UMLString", "Article_ID");

        aUml.insertAttribute("name_c_ID", "name", "public", "UMLString", "Comment_ID");
        aUml.insertAttribute("email_c_ID", "email", "public", "UMLString", "Comment_ID");
        aUml.insertAttribute("website_c_ID", "website", "public", "UMLString", "Comment_ID");
        aUml.insertAttribute("text_c_ID", "text", "public", "UMLString", "Comment_ID");

        aUml.insertAssociationEnd("user_ua_ID", "user", "public", "User_ID", "1", "1", false, "Article_ID");
        aUml.insertAssociationEnd("article_ua_ID", "articles", "public", "Article_ID", "0", "*", false, "User_ID");
        aUml.insertLinksBetweenAssociationEnds("user_ua_ID", "article_ua_ID");
        aUml.insertAssociation("user_article_ID", "has", "user_ua_ID", "article_ua_ID");

        aUml.insertAssociationEnd("article_ac_ID", "article", "public", "Article_ID", "1", "1", true, "Comment_ID");
        aUml.insertAssociationEnd("comment_ac_ID", "comments", "public", "Comment_ID", "0", "*", false, "Article_ID");
        aUml.insertLinksBetweenAssociationEnds("article_ac_ID", "comment_ac_ID");
        aUml.insertAssociation("article_comment_ID", "has", "article_ac_ID", "comment_ac_ID");

        aUml.insertOperation("findByLoginAndPassword_ID", "findByLoginAndPassword", "public", "User_ID", "User_ID");
        aUml.insertParameter("flp_login_ID", "login", "UMLString", "findByLoginAndPassword_ID");
        aUml.insertParameter("flp_password_ID", "password", "UMLString", "findByLoginAndPassword_ID");

        aUml.insertOperation("postNewArticle_ID", "postNewArticle", "public", "UMLBoolean", "User_ID");
        aUml.insertParameter("pna_title_ID", "title", "UMLString", "postNewArticle_ID");
        aUml.insertParameter("pna_text_ID", "text", "UMLString", "postNewArticle_ID");

        aUml.insertOperation("commentArticle_ID", "commentArticle", "public", "UMLBoolean", "Article_ID");
        aUml.insertParameter("ca_name_ID", "name", "UMLString", "commentArticle_ID");
        aUml.insertParameter("ca_email_ID", "email", "UMLString", "commentArticle_ID");
        aUml.insertParameter("ca_website_ID", "website", "UMLString", "commentArticle_ID");
        aUml.insertParameter("ca_text_ID", "text", "UMLString", "commentArticle_ID");

        aUml.insertSet("setArticles_ID", "SetArticle", "Article_ID");
        aUml.insertOperation("findAllArticles_ID", "findAll", "public", "setArticles_ID", "Article_ID");
    }

    /** Creates new form PrincipalJFrame */
    public PrincipalJFrame() throws Exception {
        initComponents();

        xeos = new XEOS();
        UmlEjbDomain aJuncao = null;

        xeos.createClassDiagram();

        aUml = new UmlDomain(xeos);
        aEjb = new EjbDomain(xeos);
        aJuncao = new UmlEjbDomain(xeos);

        aUml.insertMetamodelClasses();
        aEjb.insertMetamodelClasses();
        aJuncao.insertMetamodelClasses();

        aUml.insertMetamodelAttributes();
        aEjb.insertMetamodelAttributes();
        aJuncao.insertMetamodelAttributes();

        aUml.insertMetamodelAssociations();
        aEjb.insertMetamodelAssociations();
        aJuncao.insertMetamodelAssociations();

        aUml.insertMetamodelOperations();
        aEjb.insertMetamodelOperations();
        aJuncao.insertMetamodelOperations();

        aUml.insertMetamodelInvariants();
        aEjb.insertMetamodelInvariants();
        aJuncao.insertMetamodelInvariants();

        xeos.closeClassDiagram();




        xeos.createObjectDiagram();

        aUml.createSpecificationOfCurrentDiagram();
        aEjb.createSpecificationOfCurrentDiagram();
        aJuncao.createSpecificationOfCurrentDiagram();

        criarDiagramaObjetosUml();
//        criarDiagramaUmlArtigo();


        TransformationContract ct = new TransformationContract(aUml, aEjb, aJuncao, new UmlEjbTransformer(aUml, aEjb, aJuncao), new EjbCodeGenerator(aEjb, ""));
        ct.transform();

        xeos.closeObjectDiagram();
    }

    class ImagePanel extends JPanel {

        private BufferedImage image;

        public ImagePanel(String path) {
            try {
                image = ImageIO.read(getClass().getResource(path));
            } catch (Exception ex) {
                System.out.println("[ERRO] Erro ao abrir a imagem de caminho '" + path + "': " + ex.getMessage());
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters

        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel1 = new ImagePanel("img/logo.png");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setLineWrap(true);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel1.setText("Query:");

        jLabel2.setText("Resultado:");

        jTextArea2.setColumns(20);
        jTextArea2.setEditable(false);
        jTextArea2.setRows(5);
        jTextArea2.setLineWrap(true);
        jScrollPane2.setViewportView(jTextArea2);

        jButton2.setText("Limpar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setText("Instituto de Computa��o - UFF");

        jLabel4.setText("Disciplina: Modelagem e Valida��o");

        jLabel5.setText("Sistema de queries em rela��o ao meta-modelo UML com o diagrama apresentado no livro MDA Explained");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 77, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 64, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane1)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 676, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4)
                                .addComponent(jLabel3)
                                .addComponent(jLabel5)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addComponent(jLabel2)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            String query = jTextArea1.getText().replace("\n", "");
            jTextArea2.setText(xeos.query(query));
        } catch (Exception ex) {
            jTextArea2.setText("Erro ao executar query: " + ex.getMessage());
        }
}//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jTextArea1.setText("");
        jTextArea2.setText("");
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    new PrincipalJFrame().setVisible(true);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    Logger.getLogger(PrincipalJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    // End of variables declaration//GEN-END:variables
}
