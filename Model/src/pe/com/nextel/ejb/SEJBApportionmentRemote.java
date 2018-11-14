package pe.com.nextel.ejb;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.HashMap;

import javax.ejb.EJBObject;

import pe.com.nextel.bean.RequestApportionmentBean;

/**
 * Created by JCURI on 11/07/2017.
 */
public interface SEJBApportionmentRemote extends EJBObject {

    public HashMap getCalculatePayment(RequestApportionmentBean request) throws SQLException, Exception, RemoteException;
    
}