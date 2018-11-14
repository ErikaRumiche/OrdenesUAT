package pe.com.nextel.ejb;

import java.sql.SQLException;

import java.util.HashMap;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import pe.com.nextel.bean.PortalSessionBean;
import pe.com.nextel.dao.PortalSessionDAO;
import pe.com.nextel.dao.UsuarioDAO;


public class SEJBSessionBean implements SessionBean {
  private SessionContext _context;
  public HashMap objHashMap;
  UsuarioDAO objUsuarioDAO = null;

  public void ejbCreate() { 
      objHashMap  = new HashMap();
      System.out.println("El EJB de Sesión dio inicio");
  }

  public void setSessionContext(SessionContext context) throws EJBException {
      _context = context;
  }

  public void ejbRemove() throws EJBException {
      System.out.println("[SEJBSessionBean][ejbRemove()]");
  }

  public void ejbActivate() throws EJBException {
      System.out.println("[SEJBSessionBean][ejbActivate()]");
  }

  public void ejbPassivate() throws EJBException {
      System.out.println("[SEJBSessionBean][ejbPassivate()]");
  }
  /*
  public void addUserSession(PortalSessionBean sessionBean) throws Exception, SQLException{
      System.out.println("Registramos al usuario : " + sessionBean.getLogin() + " " + sessionBean.getNom_user());
      objHashMap.put(sessionBean.getSessionID(),sessionBean);
      System.out.println("Cantidad de usuarios registrados : " + objHashMap.size() );
  }
  */
  /*
  public void addUserSession(PortalSessionBean sessionBean) throws Exception, SQLException{
      System.out.println("Registramos al usuario : " + sessionBean.getLogin() + " " + sessionBean.getNom_user());
      //System.out.println("Cantidad de usuarios registrados : " + objHashMap.size() );
      PortalSessionDAO objPortalSessionDAO = new PortalSessionDAO();
      HashMap objHashMap = objPortalSessionDAO.doSaveSessionUser(sessionBean);
      if( objHashMap!= null && objHashMap.get("strMessage")!= null)
        System.out.println("El registro de Session no se realizó con éxito [Usuario="+sessionBean.getLogin()+"] : " + objHashMap.get("strMessage"));
  }*/
  
  public HashMap addUserSession(PortalSessionBean sessionBean) throws Exception, SQLException{
      System.out.println("Registramos al usuario : " + sessionBean.getLogin() + " " + sessionBean.getNom_user());
      //System.out.println("Cantidad de usuarios registrados : " + objHashMap.size() );
      PortalSessionDAO objPortalSessionDAO = new PortalSessionDAO();
      return objPortalSessionDAO.doSaveSessionUser(sessionBean);
  }
  /*
  public synchronized PortalSessionBean getUserSession(String portalID) throws Exception, SQLException{
      System.out.println("getUserSession " + portalID);
      PortalSessionBean objSessionBean = (PortalSessionBean)objHashMap.get(portalID);            
      return objSessionBean;
  }*/
  
  public PortalSessionBean getUserSession(String portalID) throws Exception, SQLException{
      PortalSessionDAO objPortalSessionDAO = new PortalSessionDAO();
      PortalSessionBean objSessionBean = null;
      HashMap objHashMap = objPortalSessionDAO.getUserSession(portalID);
      if( objHashMap!= null && objHashMap.get("strMessage")!= null){
        System.out.println("La obtención de la sesión no se realizó con éxito : " + objHashMap.get("strMessage"));
        return null;
      }else{
        objSessionBean  = (PortalSessionBean)objHashMap.get("objPortalSessionBean");
        return objSessionBean;  
      }
      
  }
    
  public void setUserSession(PortalSessionBean sessionBean) {
    objHashMap.put(sessionBean.getSessionID(),sessionBean);
  }

/*
  public PortalSessionBean PortalSessionDAOubicar(String v_phone, int n_rolid)  {
    PortalSessionBean s = new PortalSessionBean();
    //System.out.println("--------------SEJBSessionBean.PortalSessionDAOubicar--------------");
    try {
      PortalSessionDAO.ubicar(v_phone, n_rolid, s);
      //System.out.println(BeanUtils.describe(s));
    }catch (Exception e) {
      e.printStackTrace();
    }
    return s;
  }*/

  public HashMap getSessionId() throws Exception, SQLException  {
      objUsuarioDAO = new UsuarioDAO();
      return objUsuarioDAO.getSessionId();
  }
  
  public int getUsersConnected() throws Exception, SQLException{
    return objHashMap.size();
  }

  public int getSecureRol(int intSecureId) throws  Exception, SQLException{
    PortalSessionDAO objPortalSessionDAO = new PortalSessionDAO();
    return objPortalSessionDAO.getSecureRol(intSecureId);
  }
  
  //AGAMARRA
  public HashMap getUserApp(String strLogin) throws Exception, SQLException{
    PortalSessionDAO objPortalSessionDAO = new PortalSessionDAO();
    return objPortalSessionDAO.getUserApp(strLogin);
  }
  
  //AGAMARRA
  public int getUserId(String strLogin) throws Exception, SQLException{
    PortalSessionDAO objPortalSessionDAO = new PortalSessionDAO();
    return objPortalSessionDAO.getUserId(strLogin);
  }
  
  //AGAMARRA
  public int getProviderId(int wn_userid) throws Exception, SQLException{
    PortalSessionDAO objPortalSessionDAO = new PortalSessionDAO();
    return objPortalSessionDAO.getProviderId(wn_userid);
  }
  
  //AGAMARRA
  public HashMap getPositionList(int wn_swprovidergrpid) throws Exception, SQLException{
    PortalSessionDAO objPortalSessionDAO = new PortalSessionDAO();
    return objPortalSessionDAO.getPositionList(wn_swprovidergrpid);
  }
  
    //AGAMARRA
  public PortalSessionBean PortalSessionDAOubicar(String v_phone, int n_rolid, int intSalesstructid){
    PortalSessionBean s = new PortalSessionBean();
    //System.out.println("--------------SEJBSessionBean.PortalSessionDAOubicar--------------");
    try {
      PortalSessionDAO.ubicar(v_phone, n_rolid, intSalesstructid, s);
      //System.out.println(BeanUtils.describe(s));
    }catch (Exception e) {
      e.printStackTrace();
    }
    return s;
  }
  
    /** 
    * Motivo: Verifica si el building pertenece a una Tienda Expres
    * <br>Realizado por: <a href="mailto:joel.ramirez@hp.com.pe">Joel Ramirez</a>
    * <br>Fecha: 23/05/2014
    */ 
    public int getExistTiendaExpress(int lbuildingID) throws SQLException, Exception{
        PortalSessionDAO objPortalSessionDAO = new PortalSessionDAO();
        return objPortalSessionDAO.getExistTiendaExpress(lbuildingID);
    }
    
    /** 
    * Motivo: Validar si la subcategoria contempla pago al contado
    * <br>Realizado por: JLIMAYMANTA
    * <br>Fecha: 01/07/2014
    */ 
    public int doValidateSubcategoria(int subcategoriaid) throws SQLException, Exception{
        PortalSessionDAO objPortalSessionDAO = new PortalSessionDAO();
        return objPortalSessionDAO.doValidateSubcategoria(subcategoriaid);
    }
}
