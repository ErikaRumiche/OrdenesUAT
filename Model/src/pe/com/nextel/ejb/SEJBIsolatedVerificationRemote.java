package pe.com.nextel.ejb;

import pe.com.nextel.bean.IsolatedVerifConfigBean;
import pe.com.nextel.bean.IsolatedVerificationBean;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface SEJBIsolatedVerificationRemote extends EJBObject {

    public List<IsolatedVerifConfigBean> getViaConfig(String strCustomerId) throws SQLException, RemoteException, Exception;

    public List<IsolatedVerificationBean> getViaCustomer(Integer customerId, Integer verificationId, Integer orderId, Integer specificationId) throws SQLException, RemoteException, Exception;

    public void updViaCustomer(Integer npverificationid, String nptypetransaction, Integer nptransaction, String npmodifiedby) throws SQLException, RemoteException, Exception;

    }
