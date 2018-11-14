package pe.com.nextel.ejb;

import pe.com.nextel.bean.BiometricValidationBean;
import pe.com.nextel.bean.IdentityVerificationDetailBean;
import pe.com.nextel.bean.NoBiometricValidationBean;
import pe.com.nextel.dao.IdentificationDAO;
import pe.com.nextel.util.GenericObject;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SEJBIdentificationBean extends GenericObject implements SessionBean {

    private SessionContext _context;
    private IdentificationDAO objIdentificationDAO = null;

    public void setSessionContext(SessionContext context) throws EJBException {
        _context = context;
    }

    public void ejbCreate() throws EJBException {
        System.out.println("[SEJBIdentificationBean][ejbCreate()]");
        objIdentificationDAO = new IdentificationDAO();
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

    public IdentityVerificationDetailBean getIdentityVerificationDetail(long lOrderId){
        IdentityVerificationDetailBean identityVerificationDetail = null;
        try{
            identityVerificationDetail = objIdentificationDAO.getIdentityVerificationDetail(lOrderId);
        }catch (Exception e){
            System.out.println("Exception: " + e);
            identityVerificationDetail = new IdentityVerificationDetailBean();
        }
        return identityVerificationDetail;
    }

}
