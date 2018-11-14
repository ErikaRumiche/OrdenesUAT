package pe.com.nextel.ejb;

import org.apache.log4j.Logger;
import pe.com.nextel.bean.PopulateCenterBean;
import pe.com.nextel.dao.CustomerDAO;
import pe.com.nextel.dao.DigitalDocumentDAO;
import pe.com.nextel.dao.PopulateCenterDAO;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Created by JCASTILLO on 04/04/2017.
 * [PRY-0787]
 */
public class SEJBPopulateCenterBean implements SessionBean {

    private SessionContext _context;
    private DigitalDocumentDAO digitalDocumentDAO = null;
    private CustomerDAO customerDAO = null;
    private PopulateCenterDAO populateCenterDAO = null;
    private static final Logger logger = Logger.getLogger(SEJBPopulateCenterBean.class);

    public void ejbCreate() {
        digitalDocumentDAO = new DigitalDocumentDAO();
        customerDAO= new CustomerDAO();
        populateCenterDAO= new PopulateCenterDAO();
    }

    public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {
        _context = ctx;
    }

    public void ejbRemove() throws EJBException, RemoteException {
        System.out.println("[SEJBDigitalDocumentBean][ejbRemove()]");
    }

    public void ejbActivate() throws EJBException, RemoteException {
        System.out.println("[SEJBDigitalDocumentBean][ejbActivate()]");
    }

    public void ejbPassivate() throws EJBException, RemoteException {
        System.out.println("[SEJBGestionDocumentoDigitalesBean][ejbPassivate()]");
    }

    public HashMap savePopulateCenter(PopulateCenterBean populateCenterBean,String user) throws Exception {
        return populateCenterDAO.savePopulateCenter(populateCenterBean,user);
    }

    public HashMap getPopulateCenter(Long orderId) throws Exception {
        return populateCenterDAO.getPopulateCenter(orderId);
    }

    public HashMap getPopulateCenterDetail(Long orderId) throws Exception {
        return populateCenterDAO.getPopulateCenterDetail(orderId);
    }

}
