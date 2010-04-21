package br.uff.ic.interfacegrafica.interfacegrafica;

import javax.swing.JFrame;
import org.apache.log4j.BasicConfigurator;

/**
 *
 * @author robertowm
 */
public class Main {


    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();

        JFrame frame = new PrincipalJFrame();
        frame.setVisible(true);
    }
}
