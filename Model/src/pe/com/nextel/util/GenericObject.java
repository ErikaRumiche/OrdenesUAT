package pe.com.nextel.util;

import java.lang.reflect.Field;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;


/**
 * Motivo: Clase Genérica, del cual deben heredar todos los Beans.
 * Contiene la definición del Log4J para todas las clases que hereden de ésta.
 * Tiene sobreescrito el método toString(), para mostrar los valores de los atributos.
 * Se definen como Abstracta para que no se creen instancias de ella.
 * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
 * <br>Fecha: 26/08/2007
 * @see Logger
 */
public abstract class GenericObject {

    private static String newLine = System.getProperty("line.separator");
    private static String tabSpace = "\t";

    //Definición del Log4J para todas las clases que hereden de GenericObject.
    protected static Logger logger = Logger.getLogger(GenericObject.class);

    protected GenericObject() {
    }

    /**
     * Motivo: Sobreescritura del método toString(), útil para visualizar el contenido de un Bean (Objeto).
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 29/08/2007
     * @return    String con el contenido de los atributos y valores formateados para un determinado objeto.
     */
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(this.getClass().getName());
        result.append(" Object {");
        result.append(newLine);
        Field[] fields = this.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            try {
                //if((field.getName().substring(0, 2).equals("sw") || field.getName().substring(0, 2).equals("np"))) {
                    result.append("    ");
                    result.append(field.getName());
                    result.append(": ");    
                //}
                //Requiere acceso a los atributos privados de las Clases Hijas.
                boolean flag = field.isAccessible();
                if (!flag) {
                    field.setAccessible(!flag);
                }
                if(field.getType().isArray()) {
                    result.append(ArrayUtils.toString(field.get(this)));
                }
                else {
                    //if((field.getName().substring(0, 2).equals("sw") || field.getName().substring(0, 2).equals("np"))) {
                        result.append(field.get(this));
                        result.append(newLine);
                    //}
                }
                field.setAccessible(flag);
            } catch (IllegalAccessException iae) {
                logger.error(formatException(iae));
            }
        }
        result.append("}");
        return result.toString();
    }

    /**
     * Motivo: Formatea una excepción, en base al getMessage() y getStackTrace()[]
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 29/08/2007
     * <br>Modificado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
     * <br>Se quitó la pila del error</br>
     * <br>Fecha: 29/08/2007
     * @param     e  Excepción genérica que será mostrada a través del Log4J.
     * @return    String que contiene la Excepción ya formateada.
     */
    public static String formatException(Throwable e) {
        StringBuffer sbuffer = new StringBuffer();
        try {
            sbuffer.append(newLine).append("Exception: ").append(e.getClass().getName()).append(newLine);
            sbuffer.append(tabSpace).append("Message: ").append(e.getMessage()).append(newLine);
            /*for (int i = 0; i < e.getStackTrace().length; i++)
                sbuffer.append(tabSpace).append(tabSpace).append(e.getStackTrace()[i]).append(newLine);
            */        
            e.printStackTrace();
        } catch (Exception exception) {
            formatException(exception);
        }
        return sbuffer.toString();
    }

}
