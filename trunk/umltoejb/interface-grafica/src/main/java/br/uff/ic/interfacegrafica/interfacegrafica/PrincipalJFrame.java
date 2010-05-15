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
import core.XEOS;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

/**
 *
 * @author robertowm
 */
public class PrincipalJFrame extends javax.swing.JFrame {

    private XEOS xeos;
    private UmlDomain umlDomain;
    private EjbDomain ejbDomain;
    private UmlEjbDomain joinedDomain;

    private void loadDiagram(String path) throws Exception {
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
            loadXMIDiagram(path);
            TransformationContract ct = new TransformationContract(umlDomain, ejbDomain, joinedDomain, new UmlEjbTransformer(umlDomain, ejbDomain, joinedDomain), new EjbCodeGenerator(ejbDomain, ""));
            ct.transform();
            xeos.closeObjectDiagram();
        }
    }

    private void loadXMIDiagram(String path) throws Exception {

        XMIParser parser = new XMIParser(path);

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
    private String processID(String oldID) {

        String newID = "ID";
        newID = newID.concat(oldID);
        newID = newID.replace("\\.", "");
        newID = newID.replace(":", "");
        newID = newID.replace("-", "");
        newID = newID.replace(" ", "");

        return newID;

    }

    private void loadBlogDiagram() throws Exception {
        umlDomain.insertClass("User_ID", "User");
        umlDomain.insertClass("Article_ID", "Article");
        umlDomain.insertClass("Comment_ID", "Comment");

        umlDomain.insertAttribute("firstName_u_ID", "firstName", "public", "UMLString", "User_ID");
        umlDomain.insertAttribute("lastName_u_ID", "lastName", "public", "UMLString", "User_ID");
        umlDomain.insertAttribute("nickName_u_ID", "nickName", "public", "UMLString", "User_ID");
        umlDomain.insertAttribute("login_u_ID", "login", "public", "UMLString", "User_ID");
        umlDomain.insertAttribute("password_u_ID", "password", "public", "UMLString", "User_ID");
        umlDomain.insertAttribute("email_u_ID", "email", "public", "UMLString", "User_ID");

        umlDomain.insertAttribute("title_a_ID", "title", "public", "UMLString", "Article_ID");
        umlDomain.insertAttribute("text_a_ID", "text", "public", "UMLString", "Article_ID");

        umlDomain.insertAttribute("name_c_ID", "name", "public", "UMLString", "Comment_ID");
        umlDomain.insertAttribute("email_c_ID", "email", "public", "UMLString", "Comment_ID");
        umlDomain.insertAttribute("website_c_ID", "website", "public", "UMLString", "Comment_ID");
        umlDomain.insertAttribute("text_c_ID", "text", "public", "UMLString", "Comment_ID");

        umlDomain.insertAssociationEnd("user_ua_ID", "user", "public", "User_ID", "1", "1", false, "Article_ID");
        umlDomain.insertAssociationEnd("article_ua_ID", "articles", "public", "Article_ID", "0", "*", false, "User_ID");
        umlDomain.insertLinksBetweenAssociationEnds("user_ua_ID", "article_ua_ID");
        umlDomain.insertAssociation("user_article_ID", "has", "user_ua_ID", "article_ua_ID");

        umlDomain.insertAssociationEnd("article_ac_ID", "article", "public", "Article_ID", "1", "1", true, "Comment_ID");
        umlDomain.insertAssociationEnd("comment_ac_ID", "comments", "public", "Comment_ID", "0", "*", false, "Article_ID");
        umlDomain.insertLinksBetweenAssociationEnds("article_ac_ID", "comment_ac_ID");
        umlDomain.insertAssociation("article_comment_ID", "has", "article_ac_ID", "comment_ac_ID");

        umlDomain.insertOperation("findByLoginAndPassword_ID", "findByLoginAndPassword", "public", "User_ID", "User_ID");
        umlDomain.insertParameter("flp_login_ID", "login", "UMLString", "findByLoginAndPassword_ID");
        umlDomain.insertParameter("flp_password_ID", "password", "UMLString", "findByLoginAndPassword_ID");

        umlDomain.insertOperation("postNewArticle_ID", "postNewArticle", "public", "UMLBoolean", "User_ID");
        umlDomain.insertParameter("pna_title_ID", "title", "UMLString", "postNewArticle_ID");
        umlDomain.insertParameter("pna_text_ID", "text", "UMLString", "postNewArticle_ID");

        umlDomain.insertOperation("commentArticle_ID", "commentArticle", "public", "UMLBoolean", "Article_ID");
        umlDomain.insertParameter("ca_name_ID", "name", "UMLString", "commentArticle_ID");
        umlDomain.insertParameter("ca_email_ID", "email", "UMLString", "commentArticle_ID");
        umlDomain.insertParameter("ca_website_ID", "website", "UMLString", "commentArticle_ID");
        umlDomain.insertParameter("ca_text_ID", "text", "UMLString", "commentArticle_ID");

        umlDomain.insertSet("setArticles_ID", "SetArticle", "Article_ID");
        umlDomain.insertOperation("findAllArticles_ID", "findAll", "public", "setArticles_ID", "Article_ID");
    }

    /** Creates new form PrincipalJFrame */
    public PrincipalJFrame() throws Exception {
        initComponents();
    }

    class ImagePanel extends JPanel {

        private BufferedImage image;

        public ImagePanel(String path) {
            try {
                image = ImageIO.read(getClass().getResource(path));
            } catch (Exception ex) {
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
        jPanel1 = new ImagePanel("img/logo.png");
        jButton3 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Execute query");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        jTextArea1.setLineWrap(true);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel1.setText("Query:");

        jLabel2.setText("Result:");

        jTextArea2.setColumns(20);
        jTextArea2.setEditable(false);
        jTextArea2.setRows(5);
        jTextArea2.setLineWrap(true);
        jScrollPane2.setViewportView(jTextArea2);

        jButton2.setText("Clear results");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setText("MDA@UFF - Instituto de Computação - UFF - Brazil");

        jLabel4.setText("XMIGui - Graphical tool using UMLtoEJB and XMIParser");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 96, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 64, Short.MAX_VALUE)
        );

        jButton3.setText("Load XMI");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel5.setText("No XMI in use");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5))
                            .addComponent(jLabel3))
                        .addGap(355, 355, 355))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(53, 53, 53)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jScrollPane1)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 676, Short.MAX_VALUE))))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton3)
                            .addComponent(jLabel5)))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton1)))
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            String query = jTextArea1.getText().replace("\n", "");
            jTextArea2.setText(xeos.query(query));
        } catch (Exception ex) {
            jTextArea2.setText("Error in the query : " + ex.getMessage());
        }
}//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jTextArea2.setText("");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        JFileChooser jfc = new JFileChooser();
        int resp = jfc.showOpenDialog(this);
        if (resp != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File xmiFile = jfc.getSelectedFile();
        try {
            loadDiagram(xmiFile.getAbsolutePath());
            jLabel5.setText("Loaded XMI file: " + xmiFile.getName());
            // Enable buttons
            jButton1.setEnabled(true);
            jButton2.setEnabled(true);
            // Clear and enable edit in 'query'
            jTextArea1.setText("");
            jTextArea1.setEditable(true);
            // Clear 'results'
            jTextArea2.setText("");
        } catch (Exception ex) {
            jLabel5.setText("No XMI in use");
            // Disable buttons
            jButton1.setEnabled(false);
            jButton2.setEnabled(false);
            // Clear and disable edit in 'query'
            jTextArea1.setText("");
            jTextArea1.setEditable(false);
            // Clear 'results'
            jTextArea2.setText("");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

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
    private javax.swing.JButton jButton3;
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
