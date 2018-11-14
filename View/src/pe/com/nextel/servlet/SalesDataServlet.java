package pe.com.nextel.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pe.com.nextel.bean.ItemBean;
import pe.com.nextel.bean.PortalSessionBean;
import pe.com.nextel.service.SessionService;


public class SalesDataServlet extends GenericServlet {

   private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

   public void init(ServletConfig config) throws ServletException {
      super.init(config);
   }
   
   public void doPost(HttpServletRequest request, HttpServletResponse response) 
              throws ServletException, IOException {response.setContentType(CONTENT_TYPE);
      doGet(request,response);
   }
   
   
   public void doGet(HttpServletRequest request, 
      HttpServletResponse response) throws ServletException, IOException {response.setContentType(CONTENT_TYPE);
      PrintWriter out = response.getWriter();        
      String strMyaction = request.getParameter("myaction");
      System.out.println("llego al servlet-->"+strMyaction);
   
      if ( strMyaction != null ) {
      
         if (strMyaction.equals("SetSalesData")){
          doSetSalesData(request, response);
         }else if (strMyaction.equals("DeleteData")){
          doDeleteData(request, response);
         }
         
      }
   }
   
  
  /************************************************************************************************************************************
   * Motivo: Guarda las aplicaciones de Venta Data
   * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
   * <br>Fecha: 10/05/2010
   * @param     request     
   * @param     response             
   ************************************************************************************************************************************/         
  public void doSetSalesData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{    
   
    String strMessage = null;  
    String strOrderId = null;
    String strDivisionId=null;   
    String strCustomerId=null;
    String strSalesDataId=null;  
    String strSpecificationId = null;
    ItemBean objItemBean=null;
    PortalSessionBean objPortalSesBean = null;
    String strLogin = null; 
    String strStatus = null;
    String strOrigen = null;
   

    String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
  
    try{             
      objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
      if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
        throw new Exception("La sesión del usuario ha expirado por favor vuelva a registrarse. Inicie Login");
        
     objHashMap = getParameterNames(request);
     HashMap hshRetorno = objSalesDataService.setSalesData(objHashMap,objPortalSesBean);  
     strMessage     = (String)hshRetorno.get("strMessage");
     strOrderId       = (String)hshRetorno.get("strOrderId");
     strLogin       = (String)hshRetorno.get("strLogin");
     strCustomerId  = (String)hshRetorno.get("strCustomerId");
     strDivisionId  = (String)hshRetorno.get("strDivisionId");
     strSpecificationId  = (String)hshRetorno.get("strSpecificationId");
     strSalesDataId = (String)hshRetorno.get("strSalesDataId");
     strStatus      = (String)hshRetorno.get("strStatus");
     strOrigen       = (String)hshRetorno.get("strOrigen");
     objItemBean    = (ItemBean)hshRetorno.get("objItemBean");
     
    
      
     System.out.println("--------------INICIO SalesDataServlet.java: loadInsertSalesData-------------");
     System.out.println("strMessage-->"+strMessage);
     System.out.println("strOrderId-->"+strOrderId);
     System.out.println("strCustomerId-->"+strCustomerId);
     System.out.println("strDivisionId-->"+strDivisionId);
     System.out.println("strSalesDataId-->"+strSalesDataId);
     System.out.println("strSessionId-->"+strSessionId);
     System.out.println("strStatus-->"+strStatus);
     System.out.println("strOrigen-->"+strOrigen);
     System.out.println("--------------FIN SalesDataServlet.java: loadInsertSalesData-------------");
  
    }catch(Exception e){
         e.printStackTrace();
         strMessage = e.getMessage();
    }   
      
     request.setAttribute("objItemBean",objItemBean);
     RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICSECTIONSALESDATA/SalesDataPages/ResultSalesData.jsp?sParametro=¿nCustomerId="+strCustomerId+"|nhdnSessionId="+strSessionId+"|pOrderId="+strOrderId+"|pSalesDataId="+strSalesDataId+"|nDivisionId="+strDivisionId+"|nSpecificationId="+strSpecificationId+"|nStatus="+strStatus+"|nLogin="+strLogin+"&sMensaje="+strMessage+"&nOrigen="+strOrigen);
	   rd.forward(request,response);
   }   
   
  /**************************************************************************************************************************************
   * @see GenericServlet#executeDefault(HttpServletRequest, HttpServletResponse)
  ***************************************************************************************************************************************/
  public void executeDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                                                                                                IOException {
        try {
            logger.debug("Antes de hacer el sendRedirect");
            response.sendRedirect("/portal/page/portal/orders/RETAIL_NEW");
        } catch (Exception e) {
            logger.error(formatException(e));
        }
  } 
  
  
  
   /************************************************************************************************************************************
   * Motivo: Elimina un registro de Venta Data
   * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
   * <br>Fecha: 10/05/2010
   * @param     request     
   * @param     response             
   ************************************************************************************************************************************/  
   public void doDeleteData(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{    
          
      String strMessage = null; 
      String strIndId=null;
	  
      String strCustomerId=null;
      String strOrderId=null;
      String strSalesDataId=null;
      String strSpecificationId=null;
      String strLogin = null;
      String strDivisionId = null;
      String strStatus = null;
      
      String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
	 
      try{
        objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
        if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
          throw new Exception("La sesión del usuario ha expirado por favor vuelva a registrarse. Inicie Login");
          
        strLogin=objPortalSesBean.getLogin();  
         
         objHashMap =null; 
         objHashMap = getParameterNames(request);         
         HashMap hshRetorno = objSalesDataService.delSalesData(objHashMap);             
         strIndId=(String)hshRetorno.get("strIndId");      
         strMessage=  (String)hshRetorno.get("strMessage");
    
         strCustomerId=(request.getParameter("pCustomerId")==null?"0":request.getParameter("pCustomerId"));
         strOrderId=(request.getParameter("pOrderId")==null?"0":request.getParameter("pOrderId"));
         strSalesDataId=(request.getParameter("nDataId")==null?"0":request.getParameter("nDataId"));
         strSpecificationId=(request.getParameter("pSpecificationId")==null?"0":request.getParameter("pSpecificationId"));
         strDivisionId=(request.getParameter("pDivisionId")==null?"0":request.getParameter("pDivisionId"));
        
        if(logger.isDebugEnabled()){
          logger.debug(" -------------------- INICIO SalesDataServlet.java / doDeleteData ---------------------- ");			
          logger.debug("strOrderId-->"+strOrderId);
          logger.debug("strCustomerId-->"+strCustomerId);
          logger.debug("strSalesDataId-->"+strSalesDataId);
          logger.debug("strMessage-->"+strMessage);
          logger.debug(" -------------------- FIN SalesDataServlet.java / doDeleteData ---------------------- ");
      }
		 
      }catch(Exception e){
         e.printStackTrace();
         strMessage = e.getMessage();
      } 
	  RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICSECTIONSALESDATA/SalesDataPages/ResultSalesData.jsp?sParametro=¿nCustomerId="+strCustomerId+"|nhdnSessionId="+strSessionId+"|pOrderId="+strOrderId+"|pSalesDataId="+strSalesDataId+"|nDivisionId="+strDivisionId+"|nSpecificationId="+strSpecificationId+"|nStatus="+strStatus+"|nLogin="+strLogin+"&sMensaje="+strMessage+"&nOrigen=2&nInd="+ strIndId);
    rd.forward(request,response);  
   }    
   
  
 


}