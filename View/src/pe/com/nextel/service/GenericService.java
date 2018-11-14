package pe.com.nextel.service;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.HashMap;

import org.apache.log4j.Logger;

import pe.com.nextel.util.Constante;
import pe.com.nextel.util.GenericObject;


/**
 * Motivo: Clase Genérica, del cual deben heredar todos los demás Services.
 * Están definidos los atributos referentes a los DAOs (los cuales
 * deben ser reemplazados por las invocaciones a los EJBs).
 * Contiene la definición del Log4J para todas las clases que hereden de ésta.
 * Se definen como Abstracta para que no se creen instancias de ella.
 * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
 * <br>Fecha: 26/08/2007
 * @see GenericObject
 */
public abstract class GenericService {

    //Definición del Log4J para todas las clases que hereden de GenericService.
    protected static Logger logger = Logger.getLogger(GenericService.class);
    
    /**
     * 
     * @see pe.com.nextel.util.GenericObject#formatException(Throwable)
     */
    protected static String formatException(Throwable t) {
        return GenericObject.formatException(t);
    }
	
	protected void manageCatch(HashMap hshDataMap, Throwable t) {
		if(t instanceof SQLException) {
			logger.error(formatException((SQLException) t));
			System.out.println(formatException((SQLException) t));
			hshDataMap.put(Constante.MESSAGE_OUTPUT, ((SQLException) t).getMessage());
		}
		else if(t instanceof RemoteException) {
			logger.error(formatException((RemoteException) t));
			logger.error(formatException(((RemoteException) t).getCause()));
			System.out.println(formatException((RemoteException) t));
			hshDataMap.put(Constante.MESSAGE_OUTPUT, (((RemoteException) t).getCause()).getMessage());
		}
		else if(t instanceof Exception) {
			logger.error(formatException((Exception) t));
			System.out.println(formatException((Exception) t));
			hshDataMap.put(Constante.MESSAGE_OUTPUT, ((Exception) t).getMessage());
		}
		else {
			logger.error(formatException(t));
			hshDataMap.put(Constante.MESSAGE_OUTPUT, t.getMessage()); 
		}
	}
}
