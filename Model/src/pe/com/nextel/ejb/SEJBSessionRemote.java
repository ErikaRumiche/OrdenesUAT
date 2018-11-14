package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.HashMap;

import javax.ejb.EJBObject;

import pe.com.nextel.bean.PortalSessionBean;


public interface SEJBSessionRemote extends EJBObject {
    
  HashMap addUserSession(PortalSessionBean sessionBean) throws Exception, SQLException, RemoteException;

  PortalSessionBean getUserSession(String portalID) throws Exception, SQLException, RemoteException;
  
  void setUserSession(PortalSessionBean sessionBean) throws Exception, SQLException, RemoteException;

  //PortalSessionBean PortalSessionDAOubicar(String v_phone, int n_rolid) throws Exception, SQLException, RemoteException;
  
  //AGAMARRA 08/07/2009
  PortalSessionBean PortalSessionDAOubicar(String v_phone, int n_rolid, int intSalesstructid) throws Exception, SQLException, RemoteException;

  HashMap getSessionId() throws Exception, RemoteException, SQLException;

  int getUsersConnected() throws Exception, RemoteException, SQLException;

  int getSecureRol(int intSecureId) throws  Exception, RemoteException, SQLException;
  
  //AGAMARRA
  HashMap getUserApp(String strLogin) throws  Exception, RemoteException, SQLException;
  
  //AGAMARRA
  int getUserId(String strLogin) throws  Exception, RemoteException, SQLException;
  
  //AGAMARRA
  int getProviderId(int wn_userid) throws  Exception, RemoteException, SQLException;
  
  //AGAMARRA
  HashMap getPositionList(int wn_swprovidergrpid) throws  Exception, RemoteException, SQLException;
  
    /** 
    * Motivo: Verifica si el building pertenece a una Tienda Expres
    * <br>Realizado por: <a href="mailto:joel.ramirez@hp.com.pe">Joel Ramirez</a>
    * <br>Fecha: 23/05/2014
    */ 
    public int getExistTiendaExpress(int lbuildingID) throws SQLException, Exception; //TIENDA EXPRESS
    
    /** 
    * Motivo: Validar si la subcategoria contempla pago al contado
    * <br>Realizado por: JLIMAYMANTA
    * <br>Fecha: 01/07/2014
    */ 
    public int doValidateSubcategoria(int subcategoriaid) throws SQLException, Exception; //TIENDA EXPRESS
}
