package pe.com.portability.ejb;

import java.sql.SQLException;

import java.util.HashMap;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import pe.com.portability.dao.PortabilityGeneralDAO;
import pe.com.portability.dao.PortabilityItemDAO;
import pe.com.portability.dao.PortabilityOrderDAO;


public class SEJBPortabilityGeneralBean implements SessionBean 
{
  PortabilityOrderDAO objPortabilityDAO = null;
  PortabilityItemDAO objPortabilityItemDAO = null;
  PortabilityGeneralDAO  objPortabilityGeneralDAO = null;
  
  public void ejbCreate()
  {
    objPortabilityDAO	= new PortabilityOrderDAO();
    objPortabilityItemDAO = new PortabilityItemDAO();
    objPortabilityGeneralDAO = new PortabilityGeneralDAO();
    
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  public void ejbRemove()
  {
  }

  public void setSessionContext(SessionContext ctx)
  {
  }
  
   public HashMap getSectionDocumentValidate(String strNptable, String strNpDescripcion, String strNpvalue) throws  Exception, SQLException{
    return objPortabilityGeneralDAO.getSectionDocumentValidate(strNptable,strNpDescripcion,strNpvalue);
   }
}