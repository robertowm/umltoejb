package br.uff.ic.transformador.core.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;


public class Arquivo{
    
    private FileInputStream stream;
    private InputStreamReader streamReader;
    private BufferedReader reader;
    private String nome;
    private FileOutputStream fileOut ;
    private OutputStreamWriter saida ;
    private BufferedWriter intercalado;
    private boolean aberto = false;
    private boolean fechado =false;
    private boolean append = false;
    
    public Arquivo(String nomeArquivo) {
        this.nome = nomeArquivo;        
    }        
    
    private void abreArquivo(){
        try {
            stream = new FileInputStream(this.nome);
            streamReader = new InputStreamReader(stream, Charset.forName("ISO-8859-1"));
            reader = new BufferedReader(streamReader);
            aberto = true;
            fechado = false;
        } catch (FileNotFoundException e) {
            System.err.println("Erro no Arquivo do tipo : "+e.getMessage()+"  Classe "+ this.getClass().getName());
        }
    }
    
    private void fechaArquivo() {
        try {
            reader.close();
            streamReader.close();
            stream.close();
            aberto = false;
            fechado = true;            
        } catch (FileNotFoundException e) {
            System.err.println("Erro no Arquivo do tipo : "+e.getMessage()+"  Classe "+ this.getClass().getName());
        } catch (IOException e) {
            System.err.println("Erro no Arquivo do tipo : "+e.getMessage()+"  Classe "+ this.getClass().getName());
        }
    }
    
    private BufferedWriter criaSaidaAppend(boolean append) {
        File file = new File(nome);
        if (this.nome.contains("/")) {
            File f = new File(this.nome.substring(0, this.nome.lastIndexOf("/")));
            f.mkdirs();
        }
        try {
            fileOut = new FileOutputStream(file, append);
            saida = new OutputStreamWriter(fileOut);
            intercalado = new BufferedWriter(saida);
            aberto = true;
            append = true;
        } catch (IOException ex) {
            System.err.println("Erro no Arquivo do tipo : "+ex.getMessage()+"  Classe "+ this.getClass().getName());
            ex.printStackTrace();
        }
        return intercalado;
        
    }
    
    private BufferedWriter criaArqSaida(){
        return criaSaidaAppend(false);
    }
    
    public void fechaArqSaida(){
        try {
            if (!aberto) return;
            intercalado.close();
            aberto = false;            
        } catch (FileNotFoundException e) {
            System.err.println("Erro no Arquivo do tipo : "+e.getMessage()+"  Classe "+ this.getClass().getName());
        } catch (IOException e) {
            System.err.println("Erro no Arquivo do tipo : "+e.getMessage()+"  Classe "+ this.getClass().getName());
        }
    }    
    
    public void append(String string){
        try {
            this.criaSaidaAppend(true);
            intercalado.write(string);
            intercalado.newLine();
            this.fechaArqSaida();            
        } catch (IOException e) {
            System.err.println("Erro no Arquivo do tipo : "+e.getMessage()+"  Classe "+ this.getClass().getName());
        }
        
    }

    public void substituteFor(StringBuffer sb){
        try {
            this.criaSaidaAppend(false);
            intercalado.write(sb.toString());
            intercalado.newLine();
            this.fechaArqSaida();
        } catch (IOException e) {
            System.err.println("Erro no Arquivo do tipo : "+e.getMessage()+"  Classe "+ this.getClass().getName());
        }

    }
    
    public void gravaLinha(String linha){
        
        try {
            if(!aberto) this.criaArqSaida();
            intercalado.write(linha);
            intercalado.newLine();
        } catch (IOException e) {
            System.err.println("Erro no Arquivo do tipo : "+e.getMessage()+"  Classe "+ this.getClass().getName());
        }
    }
    
    public void deletaArquivo(){
        if (!new File(nome).delete())
            System.err.println("Erro no Arquivo do tipo : "+"  Classe "+ this.getClass().getName());
    }
    
    public String leLinha(){              
        if(!aberto) abreArquivo();   
        String temp = null;
        try {
            temp = reader.readLine();            
        } catch (IOException erro) {
            System.err.println("Erro no Arquivo do tipo : "+erro.getMessage()+"  Classe "+ this.getClass().getName());
        }
         if ( (temp==null) && !fechado) fechaArquivo();
        return temp;
    }
    
    public String getNome(){
        return nome;
    }    
}