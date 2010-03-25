/*
 * PrincipalJFrame.java
 *
 * Created on 15/12/2009, 21:55:28
 */
package br.uff.ic.interfacegrafica.interfacegrafica;

import br.uff.ic.mda.transformador.DominioEjb;
import br.uff.ic.mda.transformador.DominioUml;
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

    private DominioUml aUml;
    private DominioEjb aEjb;
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

        aUml.insertClass("Customer_ID", "Customer");
        aUml.insertClass("BreakfastOrder_ID", "BreakfastOrder");
        aUml.insertClass("Breakfast_ID", "Breakfast");
        aUml.insertClass("StandardBreakfast_ID", "StandardBreakfast");
        aUml.insertClass("Comestible_ID", "Comestible");

        aUml.insertAttribute("accountNumberC_ID", "accountNumberC", "public", "Integer", "Customer_ID");
        aUml.insertAttribute("addressC_ID", "addressC", "public", "String", "Customer_ID");
        aUml.insertAttribute("deliveryAddressBO_ID", "deliveryAddressBO", "public", "String", "BreakfastOrder_ID");
        aUml.insertAttribute("deliveryDateBO_ID", "deliveryDateBO", "public", "Date", "BreakfastOrder_ID");
        aUml.insertAttribute("deliveryTimeBO_ID", "deliveryTimeBO", "public", "Date", "BreakfastOrder_ID");
        aUml.insertAttribute("discountBO_ID", "discountBO", "public", "Double", "BreakfastOrder_ID");
        aUml.insertAttribute("orderDateBO_ID", "orderDateBO", "public", "Date", "BreakfastOrder_ID");
        aUml.insertAttribute("numberB_ID", "numberB", "public", "Integer", "Breakfast_ID");
        aUml.insertAttribute("nameSB_ID", "nameSB", "public", "String", "StandardBreakfast_ID");
        aUml.insertAttribute("priceSB_ID", "priceSB", "public", "Real", "StandardBreakfast_ID");
        aUml.insertAttribute("styleSB_ID", "styleSB", "public", "String", "StandardBreakfast_ID");
        aUml.insertAttribute("nameCo_ID", "nameCo", "public", "String", "Comestible_ID");
        aUml.insertAttribute("minimalQuantityCo_ID", "minimalQuantityCo", "public", "Integer", "Comestible_ID");
        aUml.insertAttribute("priceComestibleCo_ID", "priceComestibleCo", "public", "Double", "Comestible_ID");
        aUml.insertAttribute("transportFormCo_ID", "transportFormCo", "public", "String", "Comestible_ID");

        aUml.insertAssociationEnd("customerBO_ID", "customerBO", "public", "Customer_ID", "1", "1", false, "Customer_ID");
        aUml.insertAssociationEnd("ordersC_ID", "ordersC", "public", "BreakfastOrder_ID", "1", "*", false, "BreakfastOrder_ID");
        aUml.insertLinksBetweenAssociationEnds("ordersC_ID", "customerBO_ID");
        aUml.insertAssociation("Customer_BreakfastOrder_ID", "Customer_BreakfastOrder", "customerBO_ID", "ordersC_ID");

        aUml.insertAssociationEnd("orderB_ID", "orderB", "public", "BreakfastOrder_ID", "1", "1", true, "BreakfastOrder_ID");
        aUml.insertAssociationEnd("breakfastsBO_ID", "breakfastsBO", "public", "Breakfast_ID", "1", "*", false, "Breakfast_ID");
        aUml.insertLinksBetweenAssociationEnds("orderB_ID", "breakfastsBO_ID");
        aUml.insertAssociation("BreakfastOrder_Breakfast_ID", "BreakfastOrder_Breakfast", "orderB_ID", "breakfastsBO_ID");

        aUml.insertAssociationEnd("breakfastsSB_ID", "breakfastsSB", "public", "Breakfast_ID", "0", "*", false, "Breakfast_ID");
        aUml.insertAssociationEnd("standardB_ID", "standardB", "public", "StandardBreakfast_ID", "1", "1", false, "StandardBreakfast_ID");
        aUml.insertLinksBetweenAssociationEnds("breakfastsSB_ID", "standardB_ID");
        aUml.insertAssociation("Breakfast_StandardBreakfast_ID", "Breakfast_StandardBreakfast", "breakfastsSB_ID", "standardB_ID");

        aUml.insertAssociationEnd("standardsC_ID", "standardsC", "public", "StandardBreakfast_ID", "0", "*", false, "StandardBreakfast_ID");
        aUml.insertAssociationEnd("comestibleSB_ID", "comestibleSB", "public", "Comestible_ID", "1", "*", false, "Comestible_ID");
        aUml.insertLinksBetweenAssociationEnds("standardsPC_ID", "comestiblePC_ID");
        aUml.insertAssociationClass("PartClass_ID", "PartClass", "standardsC_ID", "comestibleSB_ID");
        aUml.insertAttribute("quantityPC_ID", "quantityPC", "public", "Integer", "PartClass_ID");

        aUml.insertAssociationEnd("breakfastsC_ID", "breakfastsC", "public", "Breakfast_ID", "0", "*", false, "Breakfast_ID");
        aUml.insertAssociationEnd("comestibleItemB_ID", "comestibleItemB", "public", "Comestible_ID", "0", "*", false, "Comestible_ID");
        aUml.insertLinksBetweenAssociationEnds("breakfastsC_ID", "comestibleItemB_ID");
        aUml.insertAssociationClass("ChangeClass_ID", "ChangeClass", "breakfastsC_ID", "comestibleItemB_ID");
        aUml.insertAttribute("quantityCC_ID", "quantityCC", "public", "Integer", "ChangeClass_ID");

        aUml.insertOperation("createOrder_ID", "createOrder", "public", "Boolean", "Customer_ID");
        aUml.insertOperation("calculatePrice_ID", "calculatePrice", "public", "Double", "BreakfastOrder_ID");

////////////////////// AGUARDANDO RESOLUCAO DOS PROBLEMAS //////////////////////
        /* Versao do Livro (Correta com o uso do 'let') */
//        aUml.insertOperationOCL("Class", "getAllContained", "Set(Class)",
//            "if contained->includes(self) then " +
//                "contained->asSet() " +
//            "else " +
//                "let allContained = contained->including(self) " +
//                    "in self.feature" +
//                        "->select(f : Feature | f.oclIsKindOf(AssociationEnd))" +
//                        "->select(f : Feature | f.oclAsType(AssociationEnd).composition = true)" +
//                        "->collect(f : Feature | f.classifier)" +
//                        "->iterate( cc : Classifier ; acc : Set(Classifier) = allContained " +
//                            "| allContained->union(cc.getAllContained(allContained)))" +
//            " endif "
//            , new Object[]{new String[]{"contained","Set(Class)"}});
        // MUDANÇA: Indo para o otherEnd!
        aUml.insertOperationOCL("Class", "getAllContained", "Set(Class)",
            "if contained->includes(self) then " +
                "contained->asSet() " +
            "else " +
                "self.feature" +
                    "->select(f : Feature | f.oclIsKindOf(AssociationEnd))" +
                    "->collect(f : Feature | f.oclAsType(AssociationEnd))" +
                    "->select(ae : AssociationEnd | ae.composition = true)" +
                    ".otherEnd" +
                    "->collect(ae : AssociationEnd | ae.classifier)" +
                    "->select(c : Classifier | c.oclIsKindOf(Class))" +
                    "->collect(c : Classifier| c.oclAsType(Class))" +
                    "->iterate( cc : Class ; acc : Set(Class) = allContained " +
                        "| allContained->union(cc.getAllContained(allContained, allContained->including(cc))))" +
            " endif "
            , new Object[]{new String[]{"contained","Set(Class)"}, new String[]{"allContained","Set(Class)"}});


////////////////////////////////////// OK //////////////////////////////////////
        aUml.insertOperationOCL("Class", "emptySet", "Set(Class)",
            "Class.allInstances()->select(c|false) ", new Object[0]);

////////////////////// AGUARDANDO RESOLUCAO DOS PROBLEMAS //////////////////////
//*********************************** REVER ************************************
        // MUDANÇA: Indo para o otherEnd!
        aUml.insertOperationOCL("Class", "getOuterMostContainer", "Class",
                "if " +
                    "self.feature" +
                        "->select(f : Feature | f.oclIsKindOf(AssociationEnd))" +
                        "->collect(f : Feature | f.oclAsType(AssociationEnd))" +
                        "->exists(ae : AssociationEnd | ae.otherEnd->exists(oe : AssociationEnd | oe.composition = true))" +
                " then " +
                    "self.feature" +
                        "->select(f : Feature | f.oclIsKindOf(AssociationEnd))" +
                        "->collect(f : Feature | f.oclAsType(AssociationEnd))" +
                        "->select(ae : AssociationEnd | ae.otherEnd->exists(oe : AssociationEnd | oe.composition = true))" +
                        ".otherEnd" +
                        ".classifier" +
                        "->select(c : Classifier | c.oclIsKindOf(Class))" +
                        "->collect(c : Classifier | c.oclAsType(Class))" +
                        "->asOrderedSet()" +
                        "->first()" +
                        ".getOuterMostContainer()" +
                " else " +
                    "self" +
                " endif"
                    , new Object[0]);

////////////////////// AGUARDANDO RESOLUCAO DOS PROBLEMAS //////////////////////
//*********************************** REVER ************************************
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
        aUml.insertOperationOCL("Association", "getOuterMostContainer", "Class",
                "self.associationEnds" +
                    "->select(ae : AssociationEnd | ae.otherEnd->exists(oe : AssociationEnd | oe.composition = true))" +
                    "->asOrderedSet()" +
                    "->first()" +
                    ".classifier" +
                    "->select(c : Classifier | c.oclIsKindOf(Class))" +
                    "->collect(c : Classifier | c.oclAsType(Class))" +
                    "->asOrderedSet()" +
                    "->first()" +
                    ".getOuterMostContainer()"
                        , new Object[0]);

////////////////////// AGUARDANDO RESOLUCAO DOS PROBLEMAS //////////////////////
//*********************************** REVER ************************************
        // COM PROBLEMAS! Pelo metamodelo, esse caso nunca poderá acontecer, sempre retornando 'self'
        aUml.insertOperationOCL("AssociationClass", "getOuterMostContainerFromAssociationClass", "Classifier",
                "self", new Object[0]);
    }

    /** Creates new form PrincipalJFrame */
    public PrincipalJFrame() {
        initComponents();

        xeos = new XEOS();
//        DominioUmlEjb aJuncao = null;
        try {
            xeos.createClassDiagram();

            aUml = new DominioUml(xeos);
            aEjb = new DominioEjb(xeos);
//            aJuncao = new DominioUmlEjb(xeos);

            xeos.closeClassDiagram();
        } catch (XEOSException ex) {
            ex.printStackTrace();
            System.exit(0);
        }

        try {
            xeos.createObjectDiagram();

            criarDiagramaObjetosUml();
//            criarDiagramaObjetosEjb();

//            ContratoTransformacao ct = new ContratoTransformacao
//                    <DominioUml, DominioEjb, DominioUmlEjb, TransformadorUmlEjb, GeradorCodigoEjb>
//                    (aUml, aEjb, aJuncao, new TransformadorUmlEjb(aUml, aEjb, aJuncao), new GeradorCodigoEjb(aEjb,""));
//            ct.transformar();

            xeos.closeObjectDiagram();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }



        System.out.println(" - Invariantes UML estao OK? " + aUml.checkAllInvariants());
//        System.out.println(" - Invariantes EJB est√£o OK? " + aEjb.checkAllInvariants());
//        System.exit(0);

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

            @Override
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
