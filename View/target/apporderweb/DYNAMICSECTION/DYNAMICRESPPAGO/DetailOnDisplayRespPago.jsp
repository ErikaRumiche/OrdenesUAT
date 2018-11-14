<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.bean.CustomerBean"%>
<%@ page import="pe.com.nextel.bean.PortalSessionBean"%>
<%@ page import="pe.com.nextel.bean.OrderBean"%>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="pe.com.nextel.service.CustomerService"%>
<%@ page import="pe.com.nextel.service.EditOrderService"%>
<%@ page import="pe.com.nextel.service.SessionService"%>
<%@ page import="pe.com.nextel.service.SiteService"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%  
try{   
  Hashtable hshParam=(Hashtable)request.getAttribute("hshtInputDetailSection");  
  long lCustomerId=MiUtil.parseLong((String)hshParam.get("strCustomerId"));   
  long lOrderId=MiUtil.parseLong((String)hshParam.get("strOrderId"));
  long lSiteId=MiUtil.parseLong((String)hshParam.get("strSiteId"));    
  String strSessionId=MiUtil.getString((String)hshParam.get("strSessionId"));
  String strSpecificationId=(String)hshParam.get("strSpecificationId"); // CEM - COR0354
  
  int iUserId=0;
  int iAppId=0;
  long lGeneratorId=0;
  HashMap hshData=null;
  HashMap hshOrder=null;
  HashMap hshSites=null;
  HashMap hshDataOrder=null;
  //OrderBean objOrderBean=null;
  CustomerBean objCustomerBean=null;
  ArrayList arrSite=null;    
  ArrayList arrNewSite=null;    
  String strMessage=null;    
  EditOrderService objOrderService=new EditOrderService();
  CustomerService objCustomerS=new CustomerService();
  GeneralService objGeneralService=new GeneralService();  
  OrderBean objOrderBean     =null;

  System.out.println("[DetailOnDisplayRespPago]Sesión a consultar : " + strSessionId);
  PortalSessionBean objSessionBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
  System.out.println("[DetailOnDisplayRespPago]Sesión a consultar : " + objSessionBean);
  if( objSessionBean == null ){
   throw new Exception("No se encontró la sesión del usuario");
  }
   
  iUserId=objSessionBean.getUserid();   
  iAppId=objSessionBean.getAppId();
  System.out.println("[DetailOnDisplayRespPago]");
  // Obtiene GeneratorType, GeneratorId y ProviderGroup de la Orden
  System.out.println("lOrderId: "+lOrderId);
  hshDataOrder = objOrderService.getOrder(lOrderId);
  strMessage=(String)hshDataOrder.get("strMessage");   
  if (strMessage!=null)
     throw new Exception(strMessage);
  objOrderBean=(OrderBean)hshDataOrder.get("objResume"); 
  
  String strGeneratorType    = null; 
  long lngProviderGrpId    = 0;
  long lGenerId = 0;
  String strFlagAddRP = "0";
  int  iPermission = 0;
  
  
  //Valida si la Categoría es de tipo GROSS
  HashMap hshDataGross = new HashMap();
	hshDataGross = objGeneralService.getDescriptionByValue(strSpecificationId,Constante.ST_ORDENES_GROSS);
  String strResultGross = (String) hshDataGross.get("strDescription");   
  System.out.println("[NewOnDisplayRespPago][strResultGross]"+strResultGross);
  
  // Valida si la Categoría es un Serv. Adic (2016)
  HashMap hshDataServAdic = new HashMap();
	hshDataServAdic = objGeneralService.getDescriptionByValue(strSpecificationId,Constante.ST_ORDENES_SERV_ADIC);
  String strResultServAdic = (String) hshDataServAdic.get("strDescription");   
  System.out.println("[NewOnDisplayRespPago][strResultServAdic]"+strResultServAdic);
  
  
  lngProviderGrpId = objOrderBean.getNpProviderGrpId();
  strGeneratorType  = objOrderBean.getNpGeneratorType();
  lGenerId = objOrderBean.getNpGeneratorId();      
  
  if (strResultGross != null && strResultGross.equals("1")){
     if( MiUtil.getString(strGeneratorType).equals(Constante.GENERATOR_TYPE_OPP) || MiUtil.getString(strGeneratorType).equals(Constante.GENERATOR_TYPE_OPP_PORTAB) ){
        // Se valida si el origen de la oportunidad es Banda Ancha      
        hshData = (new GeneralService()).getNpopportunitytypeid(lGenerId);
        strMessage = (String)hshData.get("strMessage");
        if ( strMessage != null ) throw new Exception(strMessage);  
           int iResult = MiUtil.parseInt((String)hshData.get("iReturnValue"));                       
           strFlagAddRP = "1";            
           int iSalesID = MiUtil.parseInt((String)hshData.get("iSalesId"));              
           iPermission = (new GeneralService()).getValidateAuthorization(lCustomerId, iResult, iSalesID);                                    
        }
     else{
        if( MiUtil.getString(strGeneratorType).equals(Constante.GENERATOR_TYPE_INC) ){//Si el origen es un Incidente            
           hshData=objGeneralService.getRol(Constante.SCRN_OPTTO_NEW_SITE, iUserId, iAppId);  
           strMessage=(String)hshData.get("strMessage");
              if (strMessage!=null)
                 throw new Exception(strMessage); 
           iPermission  = MiUtil.parseInt((String)hshData.get("iRetorno"));   
        }
     }   
  }
  else{//Órdenes Serv. Adicionales: no se requiere validar autorizaciones o permisos a los roles de trabajo      
     if(strResultServAdic != null && strResultServAdic.equals("1")){
        iPermission = 1;
     }      
  }
  
  System.out.println("------ INCIO DE DetailOnDisplayResPago.jsp -------");
  System.out.println("iPermission-->"+iPermission);
  System.out.println("lCustomerId--> "+lCustomerId);
  System.out.println("lOrderId--> "+lOrderId);   
  System.out.println("lSiteId--> "+lSiteId);      
  System.out.println("strSpecificationId--> "+strSpecificationId); 
  System.out.println("strSessionId--> "+strSessionId);   
  System.out.println("iUserId--> "+iUserId);       
  System.out.println("------ FIN DE DetailOnDisplayResPago.jsp -------");
   
   /*Se obvia la validación anterior. Los usuarios si podrán ver esta sección en modo consulta
    Consultar con JGalindo
   */
   iPermission = 1;
   
   if (iPermission == 1){

   //if (lSiteId==0){ //todos pueden ver el portlet de responsables de pago en modo solo lectura
   
      /*hshData=objCustomerS.getCustomerSitesList(lCustomerId,lOrderId,lGeneratorId,Constante.SITE_STATUS_ACTIVO);   
      strMessage=(String)hshData.get("strMessage");
      if (strMessage!=null)      
         throw new Exception(strMessage);       
      arrSite=(ArrayList)hshData.get("arrCustomerSiteList");*/
      
      hshData=objCustomerS.getCustomerSitesList(lCustomerId,lOrderId,lGeneratorId,Constante.SITE_STATUS_UNKNOWN);   
      strMessage=(String)hshData.get("strMessage");
      if (strMessage!=null)      
         throw new Exception(strMessage);       
      arrNewSite=(ArrayList)hshData.get("arrCustomerSiteList");      
   
      //Rutas      
      String strRutaContext=request.getContextPath();          
      String strURLSiteView = strRutaContext+"/DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/SiteGeneralView.jsp"; 
      String strURLOrderServlet =strRutaContext+"/editordersevlet";      
      String strURLGeneralPage=strRutaContext+"/GENERALPAGE/GeneralFrame.jsp";   
%>


<table border="0" cellspacing="0" cellpadding="0" width="65%">
<tr>
   <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="10">&nbsp;&nbsp;</td>
   <td class="SubSectionTitle" align="left" valign="top">&nbsp;&nbsp;Responsable de Pago</td>
   <td class="SubSectionTitleRightCurve"  valign="top" align="right" width="11">&nbsp;&nbsp;</td>
   <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>            
</table>    
<table border="0"  width="65%"  class="RegionBorder">
<tr>
   <td>             
      <table align=center width="100%" border="0" id="tabSite" name="tabSite" cellpadding="0" cellspacing="1" class="CTable">
      <tr align="center">                    
         <td class="CellLabel" width="5%">#</td>         
         <td class="CellLabel">Responsable Pagos</td>         
         <td class="CellLabel">C&oacute;digo BSCS</td>
         <td class="CellLabel">Regi&oacute;n</td>
         <td class="CellLabel">Estado</td>
         <td class="CellLabel">Creado<br>Por</td>                           
      </tr>    
      <%-- 
      for(int i=0; i<arrSite.size();i++) {                                  
         hshSites = (HashMap)arrSite.get(i);         
      %>
      <tr id="site<%=i+1%>">       
         <td class="CellContent"><%=i+1%></td> 
         <td class="CellContent"><%=MiUtil.getString((String)hshSites.get("swsitename"))%></td>
         <td  class="CellContent"><%=MiUtil.getString((String)hshSites.get("npcodbscs"))%></td>
         <td  class="CellContent"><%=MiUtil.getString((String)hshSites.get("regionname"))%></td>
         <td  class="CellContent"><%=MiUtil.getString((String)hshSites.get("status"))%></td>
         <td  class="CellContent"><%=MiUtil.getString((String)hshSites.get("createdby"))%></td>      
      </tr>
      <%} --%>
   
   <%  
      int ind=0;//arrSite.size();
      for(int i=0; i<arrNewSite.size();i++) {                                  
         hshSites = (HashMap)arrNewSite.get(i);             
   %>
      <tr id="site<%=ind+i+1%>">            
         <td class="CellContent"><%=ind+i+1%></td> 
         <td class="CellContent">
         <a href="javascript:fxShowUnkSite(<%=MiUtil.getIfNotEmpty((String)hshSites.get("swsiteid"))%>);">             
         <%=MiUtil.getString((String)hshSites.get("swsitename"))%>
         </a>
         </td>
         <td  class="CellContent"><%=MiUtil.getString((String)hshSites.get("npcodbscs"))%></td>
         <td  class="CellContent"><%=MiUtil.getString((String)hshSites.get("regionname"))%></td>
         <td  class="CellContent"><%=Constante.SITE_STATUS_NUEVO%></td>
         <td  class="CellContent"><%=MiUtil.getString((String)hshSites.get("createdby"))%></td>  
      </tr>   
      <%}
         //int iTotal=arrSite.size() + arrNewSite.size();
         int iTotal=arrNewSite.size();
      %>
      <input type="hidden" name="hdnCountSite" value="<%=iTotal%>">
      </table>        
   </td>
</tr>
</table>
<br>
<script language='javascript'>
var contador=0;   
var flgShowNewIcon=0;
var flagOPP = 0;
function fxSiteOnLoad(){      
   form = document.frmdatos;  
   contador=parseInt(form.hdnCountSite.value);      
}
   
function fxShowUnkSite(cod){
   var parametros = "?sUrl=<%=strURLSiteView%>"+"¿nOrderId=<%=lOrderId%>|nCustomerId=<%=lCustomerId%>|nSiteId="+cod+"|hdnSessionId=<%=strSessionId%>|pSpecificationId=<%=strSpecificationId%>";   //CEM - COR0354                          
   var Url= "<%=strURLGeneralPage%>"+parametros;              
   window.open(Url, "Responsable_Pago","status=yes, location=0, width=750, height=500, left=100, top=100, scrollbars=no, screenX=50, screenY=100");
}

</script>
</html>

<%
   }else{
%>
<table border="0" cellspacing="0" cellpadding="0" width="65%">  
<script defer>  
  function fxSiteOnLoad(){
  
   }
</script>   
</table>
<%   
   }
}catch(Exception ex){
   ex.getCause();
   ex.printStackTrace();
   System.out.println("Error try catch-->"+ex.getMessage()); 
%>
<script>
   alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}
%> 