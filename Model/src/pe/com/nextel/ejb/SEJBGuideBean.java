package pe.com.nextel.ejb;

import java.sql.SQLException;

import java.util.HashMap;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

import pe.com.nextel.bean.DeliveryOrderBean;
import pe.com.nextel.bean.EmailBean;
import pe.com.nextel.bean.FileDeliveryBean;
import pe.com.nextel.dao.GuideDAO;


public class SEJBGuideBean implements SessionBean {
  protected static Logger logger = Logger.getLogger(SEJBGuideBean.class);
  private SessionContext _context;
  GuideDAO objGuideDAO = null;
	
	public void ejbCreate() {
		objGuideDAO = new GuideDAO();
	}
	
	public void ejbActivate() {
	}

	public void ejbPassivate() {
	}

	public void ejbRemove() {
	}

	public void setSessionContext(SessionContext ctx) {
		_context = ctx;
	}
  
  public HashMap insertFileDelivery(FileDeliveryBean fileDeliveryBean) throws SQLException, Exception {
    if(logger.isDebugEnabled()) logger.debug("--Inicio--");
    return objGuideDAO.insertFileDelivery(fileDeliveryBean);
  }
  
  public HashMap validateGuide(FileDeliveryBean fileDeliveryBean) throws SQLException, Exception {
    if(logger.isDebugEnabled()) logger.debug("--Inicio--");
    return objGuideDAO.validateGuide(fileDeliveryBean);
  }
  
  public HashMap getFileDeliveryList(FileDeliveryBean fileDeliveryParam) throws SQLException, Exception {
    if(logger.isDebugEnabled()) logger.debug("--Inicio--");
    return objGuideDAO.getFileDeliveryList(fileDeliveryParam);
  }
  
  public HashMap getDeliveryOrderList(DeliveryOrderBean deliveryOrderParam, int iFlag) throws SQLException, Exception {
    if(logger.isDebugEnabled()) logger.debug("--Inicio--");
    return objGuideDAO.getDeliveryOrderList(deliveryOrderParam, iFlag);
  }
  
  public HashMap updateFileDelivery(FileDeliveryBean fileDeliveryBean) throws SQLException, Exception {
    if(logger.isDebugEnabled()) logger.debug("--Inicio--");
    return objGuideDAO.updateFileDelivery(fileDeliveryBean);
  }
  
  public HashMap updateDeliveryOrder(DeliveryOrderBean deliveryOrderBean) throws SQLException, Exception {
    if(logger.isDebugEnabled()) logger.debug("--Inicio--");
    return objGuideDAO.updateDeliveryOrder(deliveryOrderBean);
  }
  
  public HashMap insertWorkflowOrder(long lOrderId, String strAccion, String strInboxNew, String strFlagMigration, String strLogin) throws SQLException, Exception {
    if(logger.isDebugEnabled()) logger.debug("--Inicio--");
    return objGuideDAO.insertWorkflowOrder(lOrderId, strAccion, strInboxNew, strFlagMigration, strLogin);
  }
  
  public HashMap validateInvokationIP(String strIP, String strTransportista) throws SQLException, Exception {
    if(logger.isDebugEnabled()) logger.debug("--Inicio--");
    return objGuideDAO.validateInvokationIP(strIP, strTransportista);
  }
  
  public HashMap getIntervalInvokationJob() throws SQLException, Exception {
    if(logger.isDebugEnabled()) logger.debug("--Inicio--");
    return objGuideDAO.getIntervalInvokationJob();
  }
  
  public HashMap sendEmail(EmailBean emailBean) throws SQLException, Exception {
    if(logger.isDebugEnabled()) logger.debug("--Inicio--");
    return objGuideDAO.sendEmail(emailBean);
  }
    
}