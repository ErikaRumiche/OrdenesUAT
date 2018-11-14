package pe.com.nextel.ejb;

import java.io.FileInputStream;

import java.sql.SQLException;

import java.util.HashMap;

import java.util.Properties;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;

import pe.com.nextel.dao.RetailDAO;
import pe.com.nextel.dao.SiteDAO;
import pe.com.nextel.form.RetailForm;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.GenericObject;

import pe.com.nextel.util.StaticProperties;

public class SEJBRetailBean extends GenericObject implements SessionBean {

	private SessionContext _context;
	private RetailDAO objRetailDAO = null;
   private SiteDAO objSiteDAO = null;
	private DataSource ds = null;
	
	public void ejbCreate() {
		objRetailDAO = new RetailDAO();
      objSiteDAO  = new SiteDAO();
		try {
			Context context = new InitialContext();
            StaticProperties singleton = StaticProperties.instance();
            Properties properties = singleton.props;
		  ds = (DataSource)context.lookup(properties.getProperty("JNDI.DATASOURCE"));
		} catch(Exception e) {
			logger.error(formatException(e));
		}
	}
	
	public void setSessionContext(SessionContext context) throws EJBException {
		_context = context;
	}

	public void ejbActivate() {
	}

	public void ejbPassivate() {
	}

	public void ejbRemove() {
	}

	public HashMap newOrderRetail(RetailForm retailForm) throws SQLException, Exception {
		return objRetailDAO.newOrderRetail(retailForm);
	}
   
   public String updPhoneItem(long lItemId,String strPhone) throws SQLException {
		return objRetailDAO.updPhoneItem(lItemId, strPhone);
	}
	
   public String updContractItemDev(long lItemDevId,long iCoId) throws SQLException {
		return objRetailDAO.updContractItemDev(lItemDevId, iCoId);
	}
   
   public int getSiteidByCodbscs(String strcodBscs) throws SQLException, Exception{
      return objSiteDAO.getSiteidByCodbscs(strcodBscs);
   }

}