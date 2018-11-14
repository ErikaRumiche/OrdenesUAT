package pe.com.nextel.ejb;

import pe.com.nextel.bean.IdentityVerificationDetailBean;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface SEJBIdentificationRemote extends EJBObject {

    public IdentityVerificationDetailBean getIdentityVerificationDetail(long lOrderId) throws SQLException, RemoteException, Exception;

}
