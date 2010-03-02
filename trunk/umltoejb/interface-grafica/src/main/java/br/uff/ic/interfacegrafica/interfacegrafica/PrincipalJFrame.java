/*
 * PrincipalJFrame.java
 *
 * Created on 15/12/2009, 21:55:28
 */
package br.uff.ic.interfacegrafica.interfacegrafica;

import br.uff.ic.transformador.core.AnalisadorEjb;
import br.uff.ic.transformador.core.AnalisadorUml;
import core.XEOS;
import core.exception.XEOSException;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author robertowm
 */
public class PrincipalJFrame extends javax.swing.JFrame {

//    private AnalisadorUmlTeste xess;
    private AnalisadorUml aUml;
    private AnalisadorEjb aEjb;
    private XEOS xeos;

    private void criarDiagramaObjetosEjb() throws Exception {
        aEjb.createSpecificationOfCurrentDiagram();

        aEjb.insertEJBDataSchema("standardBreakfast");
        aEjb.insertEJBDataSchema("comestible");

        aEjb.insertEJBDataClass("StandardBreakfast", "standardBreakfastEDS");
        aEjb.insertEJBDataClass("Part", "standardBreakfastEDS");
        aEjb.insertEJBDataClass("Comestible", "comestibleEDS");

        aEjb.insertEJBKeyClass("StandardBreakfast");
        aEjb.insertEJBKeyClass("Part");
        aEjb.insertEJBKeyClass("Comestible");

        aEjb.insertEJBEntityComponent("StandardBreakfast");
        aEjb.insertEJBEntityComponent("Comestible");

        aEjb.insertEJBAssociationEnd("standardBreakfast", "1", "1", false, "StandardBreakfastEDC");
        aEjb.insertEJBAssociationEnd("part", "0", "*", false, "PartEDC");
        aEjb.insertEJBDataAssociation("standardBreakfastPart", "standardBreakfastEDS", "standardBreakfastEAE", "partEAE");

        aEjb.insertEJBAssociationEnd("comestibleKey", "1", "1", false, "ComestibleEKC");
        aEjb.insertEJBAssociationEnd("parts", "0", "*", false, "PartEDC");
        aEjb.insertEJBDataAssociation("partComestibleKey", "standardBreakfastEDS", "comestibleKeyEAE", "partsEAE");

        aEjb.insertEJBServingAttribute("standardBreakfastData", "1", "1", false, "StandardBreakfastEDC");
        aEjb.insertEJBServingAttribute("standardBreakfastEntity", "0", "*", false, "StandardBreakfastEEC");
        aEjb.insertEJBDataAssociation("standardBreakfast", "standardBreakfastEDS", "standardBreakfastDataESA", "standardBreakfastEntityESA");

        aEjb.insertEJBServingAttribute("comestibleData", "1", "1", false, "ComestibleEDC");
        aEjb.insertEJBServingAttribute("comestibleEntity", "0", "*", false, "ComestibleEEC");
        aEjb.insertEJBDataAssociation("comestibleAssociation", "standardBreakfastEDS", "comestibleDataESA", "comestibleEntityESA");

        aEjb.insertEJBAttribute("standardBreakfastID", "private", "int", "StandardBreakfastEDC");
        aEjb.insertEJBAttribute("name", "private", "String", "StandardBreakfastEDC");
        aEjb.insertEJBAttribute("price", "private", "float", "StandardBreakfastEDC");
        aEjb.insertEJBAttribute("style", "private", "String", "StandardBreakfastEDC");
        aEjb.insertEJBAttribute("quantity", "private", "int", "PartEDC");
        aEjb.insertEJBAttribute("standardBreakfastID", "private", "int", "StandardBreakfastEKC");
        aEjb.insertEJBAttribute("standardBreakfastID", "private", "int", "PartEKC");
        aEjb.insertEJBAttribute("comestibleID", "private", "int", "PartEKC");
        aEjb.insertEJBAttribute("comestibleID", "private", "int", "ComestibleEKC");
        aEjb.insertEJBAttribute("comestibleID", "private", "int", "ComestibleEDC");
        aEjb.insertEJBAttribute("name", "private", "String", "ComestibleEDC");
        aEjb.insertEJBAttribute("minimalQuantity", "private", "int", "ComestibleEDC");
        aEjb.insertEJBAttribute("price", "private", "float", "ComestibleEDC");
        aEjb.insertEJBAttribute("transportForm", "private", "String", "ComestibleEDC");
    }

    private void criarDiagramaObjetosUml() throws Exception {
        aUml.createSpecificationOfCurrentDiagram();

        aUml.insertClass("Customer");
        aUml.insertClass("BreakfastOrder");
        aUml.insertClass("Breakfast");
        aUml.insertClass("StandardBreakfast");
        aUml.insertClass("Comestible");
        aUml.insertAssociationClass("PartClass");
        aUml.insertAssociationClass("ChangeClass");

        aUml.insertAttribute("accountNumberC", "public", "Integer", "Customer", true);
        aUml.insertAttribute("addressC", "public", "String", "Customer", true);
        aUml.insertAttribute("deliveryAddressBO", "public", "String", "BreakfastOrder", true);
        aUml.insertAttribute("deliveryDateBO", "public", "Date", "BreakfastOrder", true);
        aUml.insertAttribute("deliveryTimeBO", "public", "Date", "BreakfastOrder", true);
        aUml.insertAttribute("discountBO", "public", "Double", "BreakfastOrder", true);
        aUml.insertAttribute("orderDateBO", "public", "Date", "BreakfastOrder", true);
        aUml.insertAttribute("numberB", "public", "Integer", "Breakfast", true);
        aUml.insertAttribute("nameSB", "public", "String", "StandardBreakfast", true);
        aUml.insertAttribute("priceSB", "public", "Real", "StandardBreakfast", true);
        aUml.insertAttribute("styleSB", "public", "String", "StandardBreakfast", true);
        aUml.insertAttribute("nameCo", "public", "String", "Comestible", true);
        aUml.insertAttribute("minimalQuantityCo", "public", "Integer", "Comestible", true);
        aUml.insertAttribute("priceComestibleCo", "public", "Double", "Comestible", true);
        aUml.insertAttribute("transportFormCo", "public", "String", "Comestible", true);
        aUml.insertAttribute("quantityPC", "public", "Integer", "PartClass", false);
        aUml.insertAttribute("quantityCC", "public", "Integer", "ChangeClass", false);

        aUml.insertAssociationEnd("customerBO", "public", "Customer", "1", "1", false, "Customer", true);
        aUml.insertAssociationEnd("ordersC", "public", "BreakfastOrder", "1", "*", false, "BreakfastOrder", true);
        aUml.insertLinksBetweenAssociationEnds("ordersC", "customerBO");
        aUml.insertAssociation("Customer_BreakfastOrder", "customerBO", "ordersC");
//
        aUml.insertAssociationEnd("orderB", "public", "BreakfastOrder", "1", "1", false, "BreakfastOrder", true);
        aUml.insertAssociationEnd("breakfastsBO", "public", "Breakfast", "1", "*", false, "Breakfast", true);
        aUml.insertLinksBetweenAssociationEnds("orderB", "breakfastsBO");
        aUml.insertAssociation("BreakfastOrder_Breakfast", "orderB", "breakfastsBO");
//
        aUml.insertAssociationEnd("breakfastsSB", "public", "Breakfast", "0", "*", false, "Breakfast", true);
        aUml.insertAssociationEnd("standardB", "public", "StandardBreakfast", "1", "1", false, "StandardBreakfast", true);
        aUml.insertLinksBetweenAssociationEnds("breakfastsSB", "standardB");
        aUml.insertAssociation("Breakfast_StandardBreakfast", "breakfastsSB", "standardB");
//
        aUml.insertAssociationEnd("standardsPC", "public", "StandardBreakfast", "0", "*", false, "StandardBreakfast", true);
        aUml.insertAssociationEnd("comestiblePC", "public", "Comestible", "1", "*", false, "Comestible", true);
        aUml.insertAssociationEnd("partPC", "public", "PartClass", "1", "1", true, "PartClass", false);
        aUml.insertLinksBetweenAssociationEnds("standardsPC", "comestiblePC");
        aUml.insertLinksBetweenAssociationEnds("standardsPC", "partPC");
        aUml.insertLinksBetweenAssociationEnds("comestiblePC", "partPC");
        aUml.insertAssociation("StandardBreakfast_Part_Comestible", "standardsPC", "comestiblePC", "partPC");

        aUml.insertAssociationEnd("breakfastsCC", "public", "Breakfast", "0", "*", false, "Breakfast", true);
        aUml.insertAssociationEnd("comestibleItemCC", "public", "Comestible", "0", "*", false, "Comestible", true);
        aUml.insertAssociationEnd("changeCC", "public", "ChangeClass", "1", "1", true, "ChangeClass", false);
        aUml.insertLinksBetweenAssociationEnds("breakfastsCC", "changeCC");
        aUml.insertLinksBetweenAssociationEnds("breakfastsCC", "comestibleItemCC");
        aUml.insertLinksBetweenAssociationEnds("changeCC", "comestibleItemCC");
        aUml.insertAssociation("Breakfast_Change_Comestible", "breakfastsCC", "comestibleItemCC", "changeCC");

        aUml.insertOperation("createOrder", "public", "Boolean", "Customer", true);
        aUml.insertOperation("calculatePrice", "public", "Double", "BreakfastOrder", true);

        aUml.insertOperationOCL("Set(Feature)", "getAssociationEndsWithComposition", "Set(Feature)",
            "self->select(f : Feature | f.oclIsKindOf(AssociationEnd))" +
                        "->select(f : Feature | f.oclAsType(AssociationEnd).composition = true)"
            , new Object[0]);

        aUml.insertOperationOCL("Class", "getAllContained", "Set(Class)",
            "if contained->includes(self) then " +
                "contained->asSet() " +
            "else " +
                "let allContained = contained->including(self)->asSet() in " +
                "let acc = contained->including(self)->asSet() in " +
                "acc->union(" +
                "self.feature" +
                        ".getAssociationEndsWithComposition()" +
                        "->collect(f : Feature | f.class.oclAsType(Class))" +
                        "->asSet()" +
                        ")" +
                            " endif "
            , new Object[]{new String[]{"contained", "Set(Class)"}});
//        Object[] params = new Object[]{new String[]{"contained", "Set(Class)"}};
//        aUml.insertOperationOCL("Class", "getAllContained", "Set(Class)",
//            "if contained->includes(self) then " +
//                "contained->asSet() " +
//            "else " +
//                "let allContained = contained->including(self)->asSet() " +
//                    "in self.feature" +
//                        "->select(f : Feature | f.oclIsKindOf(AssociationEnd))" +
//                        "->select(f : Feature | f.oclAsType(AssociationEnd).composition = true)" +
//                        "->collect(f : Feature | f.class.oclAsType(Class))" +
//                        "->asSet()" +
//                        "->iterate( cc : Class ; acc : Set(Class) = allContained->asSet() " +
//                            "| allContained->union(cc.getAllContained(allContained->asSet())))" +
//                            "->asSet() endif "
//            , params);

        aUml.insertOperationOCL("Class", "emptySet", "Set(Class)",
            "Class.allInstances()->select(c|false) ", new Object[0]);

        aUml.insertOperationOCL("Class", "getOuterMostContainerFromClass", "Classifier",
                "if " +
                "self.feature" +
                    "->select(f : Feature | f.oclIsKindOf(AssociationEnd))" +
                    "->exists(f : Feature | f.oclAsType(AssociationEnd).otherEnd" +
                    "->exists(oe : AssociationEnd | oe.composition = true))" +
                " then " +
                    "Classifier.allInstances()->select(cl | cl.oclIsKindOf(Class) and " +
                        "self.feature->select(f : Feature | f.oclIsKindOf(AssociationEnd))" +
                                    "->select(f : Feature | f.oclAsType(AssociationEnd).otherEnd->exists(oe : AssociationEnd | oe.composition = true))" +
                        ".classifier->includes(cl))->asOrderedSet()->first().oclAsType(Class).getOuterMostContainerFromClass()" +
                " else " +
                    "self" +
                " endif"
                    , new Object[0]);

//        aUml.insertOperationOCL("Association", "getOuterMostContainerFromAssociation", "Boolean",
//                "let var = Classifier.allInstances()" +
//                    "->select(cl : Classifier | (cl.oclIsKindOf(Class) or cl.oclIsKindOf(AssociationClass)) " +
//                    "and self.associationEnds->select(end : AssociationEnd |" +
//                        "end.otherEnd->exists(ae :AssociationEnd | ae.composition = true)).classifier->includes(cl)) in " +
//                        "if var->asOrderedSet()->first().oclIsKindOf(Class) then " +
//                            //"var->asOrderedSet()->first().oclAsType(Class).getOuterMostContainerFromClass() " +
//                            "true " +
//                        "else " +
////                        "var->asOrderedSet()->first().oclAsType(AssociationClass).getOuterMostContainerFromAssociationClass() " +
//                        "false " +
//                        "endif"
//                        , new Object[0]);

//        aUml.insertOperationOCL("AssociationClass", "getOuterMostContainerFromAssociationClass", "Classifier",
//                "if self.feature->select(f : Feature | f.oclIsKindOf(AssociationEnd))->size() = 1 then " +
//                "let var = " +
//                "Classifier.allInstances()" +
//                    "->select(cl : Classifier | cl.oclIsKindOf(Class)" +
//                    "and self.feature->select(f : Feature | f.oclIsKindOf(AssociationEnd))->asOrderedSet()->first().classifier->includes(cl))" +
//                    "->union(" +
//                        "Classifier.allInstances()" +
//                            "->select(cl : Classifier | cl.oclIsKindOf(AssociationClass) " +
//                            "and self.feature->select(f : Feature | f.oclIsKindOf(AssociationEnd))->asOrderedSet()->first().classifier->includes(cl))" +
//                    ") " +
//                    "in if var->asOrderedSet()->first().oclIsKindOf(Class) then var" +
//                    "->asOrderedSet()->first().oclAsType(Class).getOuterMostContainerFromClass()" +
//                    " else var->asOrderedSet()->first().oclAsType(AssociationClass).getOuterMostContainerFromAssociationClass() endif" +
//                        " else self endif", new Object[0]);
    }

    /** Creates new form PrincipalJFrame */
    public PrincipalJFrame() {
        initComponents();

        xeos = new XEOS();
        try {
            xeos.createClassDiagram();

            aUml = new AnalisadorUml(xeos);
            aEjb = new AnalisadorEjb(xeos);

            xeos.closeClassDiagram();
        } catch (XEOSException ex) {
            ex.printStackTrace();
            System.exit(0);
        }

        try {
            xeos.createObjectDiagram();

            criarDiagramaObjetosUml();
            criarDiagramaObjetosEjb();

            xeos.closeObjectDiagram();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }

//        System.out.println(" - Invariantes UML est√£o OK? " + aUml.checkAllInvariants());
//        System.out.println(" - Invariantes EJB est√£o OK? " + aEjb.checkAllInvariants());

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

        jLabel3.setText("Instituto de Computação - UFF");

        jLabel4.setText("Disciplina: Modelagem e Validação");

        jLabel5.setText("Sistema de queries em relação ao meta-modelo UML com o diagrama apresentado no livro MDA Explained");

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

            public void run() {
                new PrincipalJFrame().setVisible(true);
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
