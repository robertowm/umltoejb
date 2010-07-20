package br.uff.ic.mda.xmigui;

//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.io.Writer;
import javax.swing.JFrame;
//import org.apache.log4j.BasicConfigurator;
//import org.apache.log4j.PatternLayout;
//import org.apache.log4j.WriterAppender;

/**
 *
 * @author robertowm
 */
public class Main {


    public static void main(String[] args) throws Exception {
//        BasicConfigurator.configure();
//        BasicConfigurator.configure(new WriterAppender(new PatternLayout(), new OutputStreamWriter(new FileOutputStream("log.txt"))));

        JFrame frame = new PrincipalJFrame();
        frame.setVisible(true);
    }
}
