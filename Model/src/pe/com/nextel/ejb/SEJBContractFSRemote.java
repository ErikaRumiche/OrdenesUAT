package pe.com.nextel.ejb;

import pe.com.nextel.bean.RequestContractFSBean;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Código: TDECONV003-8
 * <br>Realizado por: PZACARIAS
 * <br>Fecha: 18/06/2018
 */
public interface SEJBContractFSRemote extends EJBObject {

    public HashMap getSIM_MSISDN_FS(RequestContractFSBean request) throws SQLException, Exception, RemoteException;
    
}