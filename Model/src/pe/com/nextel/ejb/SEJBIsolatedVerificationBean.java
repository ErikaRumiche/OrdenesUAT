package pe.com.nextel.ejb;

import pe.com.nextel.bean.IsolatedVerifConfigBean;
import pe.com.nextel.bean.IsolatedVerificationBean;
import pe.com.nextel.dao.IsolatedVerificationDAO;
import pe.com.nextel.util.GenericObject;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.List;

public class SEJBIsolatedVerificationBean extends GenericObject implements SessionBean {

    private SessionContext _context;
    private IsolatedVerificationDAO objIsolatedVerificationDAO = null;

    public void setSessionContext(SessionContext context) throws EJBException {
        _context = context;
    }

    public void ejbCreate() throws EJBException {
        System.out.println("[SEJBIdentificationBean][ejbCreate()]");
        objIsolatedVerificationDAO = new IsolatedVerificationDAO();
    }

    public void ejbRemove() throws EJBException {
        System.out.println("[SEJBIdentificationBean][ejbRemove()]");
    }

    public void ejbActivate() throws EJBException {
        System.out.println("[SEJBIdentificationBean][ejbActivate()]");
    }

    public void ejbPassivate() throws EJBException {
        System.out.println("[SEJBIdentificationBean][ejbPassivate()]");
    }

    public List<IsolatedVerifConfigBean> getViaConfig(String strCustomerId) {
        List<IsolatedVerifConfigBean> specLst = null;
        try{
            specLst = objIsolatedVerificationDAO.getViaConfig(strCustomerId);
        } catch (Exception e){
            System.out.println("Exception: " + e);
            specLst = new ArrayList<IsolatedVerifConfigBean>();
        }
        return specLst;
    }

    public List<IsolatedVerificationBean> getViaCustomer(Integer customerId, Integer verificationId, Integer orderId, Integer specificationId) {
        List<IsolatedVerificationBean> listIsolatedVerificationBean = null;
        try{
            listIsolatedVerificationBean = objIsolatedVerificationDAO.getViaCustomer(customerId, verificationId, orderId, specificationId);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Exception: " + e);
            listIsolatedVerificationBean = new ArrayList<IsolatedVerificationBean>();
        }
        return listIsolatedVerificationBean;
    }

    public void updViaCustomer(Integer npverificationid, String nptypetransaction, Integer nptransaction, String npmodifiedby) {
        try{
            objIsolatedVerificationDAO.updViaCustomer(npverificationid, nptypetransaction, nptransaction, npmodifiedby);
        } catch (Exception e){
            System.out.println("Exception: " + e);
        }
    }
}
