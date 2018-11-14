package pe.com.nextel.ejb;

import java.sql.SQLException;

import java.util.HashMap;

import javax.ejb.EJBLocalObject;

import pe.com.nextel.bean.PortalSessionBean;


public interface SEJBSessionLocal extends EJBLocalObject {
 
  HashMap addUserSession(PortalSessionBean sessionBean) throws Exception, SQLException;

  PortalSessionBean getUserSession(String portalID) throws Exception, SQLException;
  
  void setUserSession(PortalSessionBean sessionBean);

  PortalSessionBean PortalSessionDAOubicar(String v_phone, int n_rolid);

  HashMap getSessionId() throws Exception, SQLException;

  int getUsersConnected() throws Exception, SQLException;
    
    
}
