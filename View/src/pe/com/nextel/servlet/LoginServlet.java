package pe.com.nextel.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.HashMap;

import javax.naming.Context;

import javax.rmi.PortableRemoteObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pe.com.nextel.bean.PortalSessionBean;
import pe.com.nextel.ejb.SEJBSessionRemote;
import pe.com.nextel.ejb.SEJBSessionRemoteHome;
import pe.com.nextel.service.SessionService;
import pe.com.nextel.util.MiUtil;


public class LoginServlet extends HttpServlet {

  private static final String CONTENT_TYPE = 
  "text/html; charset=UTF-8";
  
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    try {
      Context context = MiUtil.getInitialContext();
      SEJBSessionRemoteHome home = (SEJBSessionRemoteHome)PortableRemoteObject.narrow(context.lookup("SEJBSession"), SEJBSessionRemoteHome.class);
      SEJBSessionRemote ejbSession = home.create();
      //System.out.println("Se creó el EJB GLOBAL SERVLET");
      context.rebind("SessionID", ejbSession);
    }catch (Exception ex) {
      System.out.println("Falló ");
      ex.printStackTrace();
    }     
  }

  public void doGet(HttpServletRequest request, 
                    HttpServletResponse response) throws ServletException, 
                                                         IOException {

  }
  
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  
  String strAction = MiUtil.getString((String)request.getParameter("strAction"));
  if( strAction.equals("loadApplicationByRol") ) {
    showMenuByUser(request,response);
  }else{
    RequestDispatcher dispatcher = null;
    String messageError = null;
    try {
      dispatcher = getServletContext().getRequestDispatcher("/applicationSelect.jsp");
      dispatcher.forward(request,response);
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }
  
  }
  
  public void showMenuByUser(HttpServletRequest request, HttpServletResponse response){
  
    response.setContentType(CONTENT_TYPE);
    PrintWriter out = null;
  try{
    
    out = response.getWriter();
    out.println("<script>");

    String sessionID = "998102396";
  
    String strUsuario  = (String)request.getParameter("paramLogin");
    String strRolId    = (String)request.getParameter("an_rolid");//("paramRolId");

    PortalSessionBean portalUser = new PortalSessionBean();
    SessionService    objSessionService = new SessionService();
    
    /*portalUser = objSessionService.getSessionData(strUsuario, MiUtil.parseInt(strRolId));*/
    
    /**---Cambio de Prueba Start --**/
    
    int     intApplicationId  = -1;
    /**Se adiciona el parámetro secureId**/
    String  strSecureId     = "2866";//MiUtil.getString(request.getParameter("secureId"));
    
    System.out.println("Portlet JP_SESSION_ShowPage - Nextel : " + strUsuario+ "| strRolId : " + strRolId + "| strSecureId : " + strSecureId + "|");
    strSecureId="";
    if( !strSecureId.equals("") ){
      intApplicationId = objSessionService.getSecureRol(MiUtil.parseInt(strSecureId));
      System.out.println("Se ingresó mediante un token : Usuario Nextel : " +strUsuario + " SecureId : " + strSecureId + " Application : " + intApplicationId);
    }else{
      System.out.println("No se ingresó mediante un token : Usuario Nextel : " +strUsuario + " SecureId : " + strSecureId + " RolId : " + strRolId);
      intApplicationId  = MiUtil.parseInt(strRolId);
    }
    
    //Nuevo parámetro para Jerarquía de Ventas
    String strSalesstructid  =  MiUtil.getString(request.getParameter("cmb_npsalesstructid"));
    request.getSession().putValue("npsalesstructid", strSalesstructid);
    System.out.println("strSalesstructid="+strSalesstructid);
    
    int intSalesstructid = strSalesstructid.length()==0?0:Integer.parseInt(strSalesstructid);
    System.out.println("intSalesstructid="+intSalesstructid);
    
    //Se agrega el parámetro "wn_salesstructid"
    //portalUser = objSessionService.getSessionData(strUsuario, intApplicationId);
    portalUser = objSessionService.getSessionData(strUsuario, intApplicationId, intSalesstructid);
    
    /**---Cambio de Prueba End --**/
  
    System.out.println("Mensaje después de Loguearse " + (String)portalUser.getMessage());
    
    if( portalUser.getMessage() != null ){
      throw new Exception(portalUser.getMessage());
    }else{
      portalUser.setSessionID(sessionID); 
            
      //Nuevo parámetro para Jerarquía de Ventas
      String strSwprovidergrpid  =  portalUser.getProviderGrpId()+"";//  MiUtil.getString(request.getParameter("swprovidergrpid"));
      request.getSession().putValue("swprovidergrpid", strSwprovidergrpid);
      System.out.println("strSwprovidergrpid="+strSwprovidergrpid);
      //portalUser.setProviderGrpId(strSwprovidergrpid.length()==0?0:Integer.parseInt(strSwprovidergrpid));
      //portalUser.setSalesStructId(intSalesstructid);
      
      /**Simulamos el HttpSession session = getSession(true);
       * session.setAttribute("Valor","objValor")**/
      String strError = "Hola a todos JPSESSION";
      HashMap objHashSession = objSessionService.addUserSessionIni(portalUser);
      
      if( objHashSession.get("strMessage")!= null )
        throw new Exception((String)objHashSession.get("strMessage"));
      
      out.println("location.replace('menu.html');");
      //out.println("location.replace('index.html');");
    }
    
      out.println("</script>");
      
    }catch(Exception ex){
      ex.printStackTrace();
      out.println("alert('Hubieron errores al obtener la sesión :  " + MiUtil.getMessageClean(ex.getMessage()) + "');");
      //out.println("parent.mainFrame.location.replace('login.jsp');");
      out.println("location.replace('login.jsp');");
      out.println("</script>");
    }finally{
      out.close();
    }
  }
  
  
  
                                                         
  /*
  public void doPost(HttpServletRequest request, 
                     HttpServletResponse response) throws ServletException, 
                                                          IOException {
    response.setContentType(CONTENT_TYPE);
    
    PrintWriter out = response.getWriter();
    out.println("<script>");

    String sessionID = "98102396";//pSession.getId();

    String strUsuario  = (String)request.getParameter("ssousername");
    String strPassword = (String)request.getParameter("password");

    PortalSessionBean portalUser = new PortalSessionBean();
    
    //Atencion al Cliente
    //portalUser = SessionService.PortalSessionDAOubicar(usuario, 27);
    
    //Fullfilment
    //portalUser = SessionService.PortalSessionDAOubicar(usuario, 26);
    
    //Ventas
    portalUser = SessionService.PortalSessionDAOubicar(strUsuario, MiUtil.parseInt(strPassword));
    
    System.out.println("Mensaje después de Loguearse " + (String)portalUser.getMessage());
    
    if( portalUser.getMessage() != null ){
    out.println("alert('Hubieron errores al obtener la sesión :  " + portalUser.getMessage() + "');");
    out.println("</script>");
    out.close();
    }else{
    portalUser.setSessionID(sessionID); 
    
    //*Simulamos el HttpSession session = getSession(true);
    //* session.setAttribute("Valor","objValor")
    SessionService.addUserSessionIni(portalUser);
    
    out.println("location.replace('menu.html');");
    //out.println("location.replace('index.html');");
    out.println("</script>");
    out.close();
    }
  }*/

}


