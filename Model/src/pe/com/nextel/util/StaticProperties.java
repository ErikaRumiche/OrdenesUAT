package pe.com.nextel.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by valencli on 23/01/15.
 */
public class StaticProperties {
    static private StaticProperties _instance = null;
    static public Properties props = new Properties();


    protected StaticProperties() throws IOException{
        InputStream fileInputStream = null;
        try{
            fileInputStream = new FileInputStream(new File(Constante.FILE_PROPERTIES)) ;
            props.load(fileInputStream);
        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

        }
    }

    static public StaticProperties instance(){
        try{
            if (_instance == null) {
                System.out.println("[StaticProperties] Se crea una nueva instancia");
                synchronized (StaticProperties.class) {
                    if (_instance == null) {
                        _instance = new StaticProperties();
                    }
                }
            }else{
                System.out.println("[StaticProperties] Se leen los valores ya cargados");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return _instance;
    }
}