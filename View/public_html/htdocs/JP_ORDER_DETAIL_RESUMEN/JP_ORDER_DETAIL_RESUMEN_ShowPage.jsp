<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil" %>
<%@ page import="oracle.portal.provider.v2.ProviderUser" %>
<%@ page import="oracle.portal.provider.v2.url.UrlUtils" %>
<%@ page import="pe.com.nextel.service.SessionService" %>
<%@ page import="pe.com.nextel.service.CustomerService" %>
<%@ page import="pe.com.nextel.service.EditOrderService" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="pe.com.nextel.bean.*" %>
<%@ page import="pe.com.nextel.util.*" %>
<%@page import="pe.com.nextel.exception.SessionException" %>
<%@ page import="java.util.*" %>                
<% 
try{
   String strSessionId1 = "";
   
try {
    PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    ProviderUser objetoUsuario1 = pReq.getUser();
    strSessionId1=objetoUsuario1.getPortalSessionId();
    System.out.println("Sesión capturada  : " + objetoUsuario1.getName() + " - " + strSessionId1 );
} catch(Exception e) {
    System.out.println("Portler Not Found : " + e.getClass() + " - " + e.getMessage() );
    out.println("Portlet JP_ORDER_SEARCHShowPage Not Found");
    return;
}
   
   //strSession="98102396";
   int iUserId=0;
   int iAppId=0;
   String strUser=null;
   String strLogin=null;
      
   
    //strSessionId1 = "98102396";
    System.out.println("Sesión capturada después del resuest: " + strSessionId1);
    PortalSessionBean portalSessionBean = (PortalSessionBean) SessionService.getUserSession(strSessionId1);
    if (portalSessionBean == null) {
        System.out.println("No se encontró la sesión de Java ->" + strSessionId1);
        throw new SessionException("La sesión finalizó");
    }
   iUserId=portalSessionBean.getUserid();
   iAppId= portalSessionBean.getAppId();
   strUser=portalSessionBean.getNom_user();
   strLogin=portalSessionBean.getLogin();   
   
   //Parametros       
   String strOrderId=(request.getParameter("an_nporderid")==null?"0":request.getParameter("an_nporderid"));
   System.out.println("strOrderId-->"+strOrderId);   
   long lOrderId=Long.parseLong(strOrderId);
   long lCustomerId=0;
   String strSubCategory=null;
   int iSubCategory=0;
   
   HashMap hshData=null;
   HashMap hshOrder=null;
   String strMessage=null;   
   EditOrderService objOrderService=new EditOrderService();
   CustomerService objCustomerService=new CustomerService();
   GeneralService objGeneralService= new GeneralService();
   OrderBean objOrderBean=null;
   CustomerBean objCustomerBean=null;   
   
   //Obteniendo los datos de la Orden  
   hshOrder=(HashMap)objOrderService.getOrder(lOrderId);
   strMessage=(String)hshOrder.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);   
   
   objOrderBean=(OrderBean)hshOrder.get("objResume");  
   lCustomerId =objOrderBean.getCsbCustomer().getSwCustomerId();
   
   if (objOrderBean!=null){      
      hshData=objCustomerService.getCustomerData(lCustomerId);
      strMessage=(String)hshData.get("strMessage");
      if (strMessage!=null)
         throw new Exception(strMessage); 
      
      objCustomerBean=(CustomerBean)hshData.get("objCustomerBean");      
   }else{
      objOrderBean=new OrderBean();
      objCustomerBean=new CustomerBean();
   }
   
   //Sección de Valoración del cliente    
   hshData=objGeneralService.getCustomerValue(lCustomerId);
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);     

   int iCompanyValue=MiUtil.parseInt((String)hshData.get("iRetorno"));   
   
   //Verificando acceso para ver detalle de valoracion de cliente
   hshData=objGeneralService.getRol(Constante.SCRN_OPTTO_IMGCATEG, iUserId, iAppId);  
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);     
   
   int iFlaggersup=MiUtil.parseInt((String)hshData.get("iRetorno"));      

   /*MVERAE : Obtiene los formatos relacionados con la categoria de la orden */
   HashMap hshFormats=null;
   FormatBean formatBean = null;
   ArrayList arrFormats = new ArrayList();
   hshFormats = objGeneralService.getFormatBySpecification(objOrderBean.getNpSpecificationId());
   
   strMessage=(String)hshFormats.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);
   
   arrFormats = (ArrayList)hshFormats.get("objArrayList");
   /* -----------------------------------------------------------   */
   
   String strRutaContext=request.getContextPath();
   
   /*FNAJARRO : Obtiene ruta del servidor para los formatos relacionados con la categoria de la orden */
   HashMap objHashMap=objGeneralService.getNameServerReport(Constante.FORM,Constante.URL_SERVER_SERVLET);
   strMessage=(String)objHashMap.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);
   String strNameServerReport=(String)objHashMap.get("strNameServerReport");
   System.out.println("Nombre del Servidor: "+strNameServerReport);
   
   
%>  

<TABLE BORDER="0" class="PortletHeaderColor" CELLSPACING="0" CELLPADDING="0" WIDTH="100%">
<TR class="PortletHeaderColor">
   <TD class="LeftCurve" valign="top" align=LEFT width="10" height="10" NOWRAP="">&nbsp;&nbsp;</TD>
   <TD class="PortletHeaderColor" NOWRAP="" align=LEFT valign="top" width="100%">
   <FONT class="PortletHeaderText">Orden > Detalle</FONT>:</TD>
   <TD align="RIGHT" class="PortletHeaderColor" NOWRAP="" valign="top">&nbsp;</TD>
   <TD align="right" class="RightCurve" width="10" NOWRAP="">&nbsp;&nbsp;</TD>
</TR>
</TABLE>
<table border="0" cellspacing="1" cellpadding="2" width="100%" class="RegionBorder">
<TR>
   <TD ALIGN="CENTER" CLASS="CellLabel">Pedido ID</TD>
   <TD ALIGN="CENTER" CLASS="CellLabel">N° Solicitud</TD>
   <TD align="left">
      <TABLE width="100%" border="0" cellspacing="0" cellpadding="0" >
      <TR>
         <TD ALIGN="LEFT" CLASS="CellLabel">&nbsp;Raz&oacute;n Social</TD>                  
         <script>
            function fxValidateRol(v_customerid){
               var v_flag = "<%=iFlaggersup%>";
               if (v_flag == 1){
                  url = "/portal/pls/portal/WEBSALES.NPSL_CUSTOMER_EXT_PL_PKG.PL_CUST_VALUE_DETAIL?an_customerid=" + v_customerid;
                  url = "/portal/pls/portal/WEBSALES.NPSL_GENERAL_PL_PKG.WINDOW_FRAME?av_title="+escape("Valorización > Segmentación Detalle")+"&av_url="+escape2(url);
                  WinAsist = window.open(url,"WinAsist","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=no,screenX=100,top=80,left=100,screenY=80,width=750,height=350,modal=yes");
               }else{
                  return;
               }
            }
            
            function fxCleanImgCustValue() {
               divCustValue.innerHTML = "<img src='<%=Constante.PATH_APPORDER_SERVER%>/images/CustValue/CustValue.gif' border=0 align='absbottom'>" ;
            }
         </script>                
         <% if (lCustomerId == 0 ){%>         
         <td align="right" valign="bottom" width="60" class="CellContent">
            <div id="divCustValue" name="divCustValue" style="display:inline">
            <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/CustValue/CustValue.gif" border="0" align="absbottom">
            </div>
         </td>
         <%}
         else{
            if (strMessage !=null && !strMessage.equals(""))
               throw new Exception("Error en PL_CUST_VALUE_IMG:"+strMessage);    
    
        if(iFlaggersup==1){
          if (iCompanyValue!=0){
                  %>
                    <td align="right" valign="bottom" width="60" class="CellLabel">
                      <div id="divCustValue" name="divCustValue" style="display:inline">
                      <a href="javascript:fxValidateRol('<%=lCustomerId%>')">
                      <img align="absbottom" src="<%=Constante.PATH_APPORDER_SERVER%>/images/CustValue/CustValue<%=iCompanyValue%>.gif" border="0">
                      </a>
                      </div>
                    </td>
            <%
          }else{
            %>
              <td align="right" valign="bottom" width="60" class="CellLabel">
                <div id="divCustValue" name="divCustValue" style="display:inline">
                  <img align="absbottom" src="<%=Constante.PATH_APPORDER_SERVER%>/images/CustValue/CustValue1.gif" border="0">
                </div>
              </td>
         <%   }
          }else {
            if (iCompanyValue!=0){
         %>
         <td align="right" valign="bottom" width="60" class="CellLabel">
            <div id="divCustValue" name="divCustValue" style="display:inline">
            <img align="absbottom" src="<%=Constante.PATH_APPORDER_SERVER%>/images/CustValue/CustValue<%=iCompanyValue%>.gif" border="0">
            </div>
         </td>
         <%
            }else{
         %>
         <td align="right" valign="bottom" width="60" class="CellLabel">
            <div id="divCustValue" name="divCustValue" style="display:inline">
            <img align="absbottom" src="<%=Constante.PATH_APPORDER_SERVER%>/images/CustValue/CustValue1.gif" border="0">
            </div>
         </td>
         <%            
            }
         }
      }
         %>
      </TR>
      </TABLE>
   </TD>
   <TD ALIGN="CENTER" CLASS="CellLabel">Telef&oacute;nico</TD>
   <TD ALIGN="CENTER" CLASS="CellLabel">Categor&iacute;a</TD>
   <TD ALIGN="CENTER" CLASS="CellLabel">Sub Categor&iacute;a</TD>
   <TD ALIGN="CENTER" CLASS="CellLabel">Fecha</TD>
</TR>
<%if (objOrderBean!= null){%>
<TR>
   <TD ALIGN="CENTER" CLASS="CellContent"><%=(objOrderBean.getNpOrderId()==0?"":objOrderBean.getNpOrderId()+"")%></TD>
   <TD ALIGN="CENTER" CLASS="CellContent"><%=MiUtil.getString(objOrderBean.getNpOrderCode())%></TD>
   <TD ALIGN="LEFT" CLASS="CellContent">&nbsp;<a href="javascript:getCustDetail('<%=lCustomerId%>','<%= iUserId %>')"><%=MiUtil.getString(objCustomerBean.getSwName())%></a></TD>
   <TD ALIGN="CENTER" CLASS="CellContent"><%=MiUtil.getString(objCustomerBean.getSwMainPhone())%></TD>
   <TD ALIGN="CENTER" CLASS="CellContent"><%=MiUtil.getString(objOrderBean.getNpType())%></TD>
   <TD ALIGN="CENTER" CLASS="CellContent"><%=MiUtil.getString(objOrderBean.getNpSpecification())%></TD>
   <TD ALIGN="CENTER" CLASS="CellContent"><%=MiUtil.toFecha(objOrderBean.getNpCreatedDate())%></TD>
</TR>
<%}%>
<% 
   hshData=objGeneralService.getRol(Constante.SCRN_OPTTO_EXCEL, iUserId, iAppId);  
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);     
   
   int iAccesoVerExcel=MiUtil.parseInt((String)hshData.get("iRetorno"));

   hshOrder=objOrderService.getOrderScreenField(lOrderId,Constante.PAGE_ORDER_DETAIL);
   strMessage=(String)hshOrder.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);  
   
   HashMap hshScreenField= (HashMap)hshOrder.get("hshData");   
      
   if (iAccesoVerExcel == 1 && !"Disabled".equals(MiUtil.getString((String)hshScreenField.get("excelformat")))) { 
      strSubCategory=MiUtil.getString(objOrderBean.getNpSpecification());
      iSubCategory=objOrderBean.getNpSpecificationId();
%>

<TR>
  <TD colspan="7">                  
    <table border="0" cellspacing="0" cellpadding="0">
      <tr class="PortletHeaderColor">
         <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="16"></td>
         <td class="SubSectionTitle" align="LEFT" valign="top"> Formatos </td>
         <td class="SubSectionTitleRightCurve" valign="top" align="right" width="12"></td>
      </tr>
    </table>
    <table border="0" cellspacing="1" cellpadding="0" width="100%" class="RegionBorder">
      <tr class="CellContent">
        <td>&nbsp;&nbsp;</td>
        <%
          Iterator it = arrFormats.iterator();
          if(arrFormats !=null && arrFormats.size()>0){
            while(it.hasNext()){
              formatBean = new FormatBean();
              formatBean = (FormatBean)it.next();
              %>
              <td><a href="javascript:fxShowOrderFormat(<%=formatBean.getNpformid()%>,'<%=formatBean.getNpformname().trim()%>','<%=formatBean.getNpprogramname()%>','<%=formatBean.getNptemplatename()%>',<%=strOrderId%>,<%=objOrderBean.getNpSpecificationId()%>,'<%=strLogin%>','<%=strUser%>');"><%=formatBean.getNpformname()%></a></td>
              <td>&nbsp;&nbsp;</td>
              <%
            }
          }else{
        %>
          <td>No hay formatos para la especificación</td>
        <%}%>
      </tr>
    </table>
   </TD>
</TR>

<script type="text/javascript">
   function fxVerExcel(pedidoId,category,subCategory){                  
      <% if (iSubCategory == 2049 || iSubCategory == 2013 ) { %>
            url = "<%=strRutaContext%>/PAGEEDIT/ValidatePrePaId.jsp?an_nporderid="+pedidoId+"&av_category="+escape2(category)+ "&av_subcategory="+escape2(subCategory)+"&hdnSession=<%=strSessionId1%>";
            WinAsist = window.open(url,"FormularioOrden","toolbar=no,location=0,directories=no,status=no,menubar=no,scrollbars=no,resizable=no,screenX=200,top=100,left=200,screenY=100,width=250,height=200,modal=yes");
      <%} else { %>           
            url = "<%=strNameServerReport%>an_orderid="+pedidoId+"&av_type="+escape2(category)+"&an_npspecificationid=<%=objOrderBean.getNpSpecificationId()%>&av_specification="+escape2(subCategory)+"&av_login="+escape2('<%=strLogin%>')+"&av_nom_user="+escape2('<%=strUser%>')+"&an_appid=<%=iAppId%>";   
            WinAsist = window.open(url,"FormularioOrden","toolbar=yes,location=0,directories=no,status=yes,menubar=yes,scrollbars=no,resizable=yes,screenX=100,top=80,left=100,screenY=80,width=800,height=550,modal=yes");
      <%}%>
   }
</script>
<%}%>
</TABLE>
<script DEFER>
      function fxShowOrderFormat(v_npformid,v_npformname,v_npprogramname,v_nptemplatename,v_nporderId,v_npspecificationid,v_Login,v_nom_user){
         var url ="<%=strNameServerReport%>"
                  +"an_npformid="+v_npformid
                  +"&av_npformname="+v_npformname
                  +"&av_npprogramname="+v_npprogramname
                  +"&av_nptemplatename="+v_nptemplatename
                  +"&an_nporderId="+v_nporderId
                  +"&an_npspecificationid="+v_npspecificationid
                  +"&av_nplogin="+v_Login
                  +"&av_nom_user="+v_nom_user;

         //jsMC_win(url,700,500);
         window.open(url,"FormatoIncidente","toolbar=yes,location=0,directories=no,status=yes,menubar=yes,scrollbars=no,resizable=yes,screenX=100,top=80,left=100,screenY=80,width=800,height=550,modal=yes");

      }
      </script>
<script language="javascript">          
   function getCustomerDetail(customerId){		
      var param="?customerId=" + customerId;                  
      var url="/portal/page/portal/nextel/CUST_DETAIL"+param;
      WinAsist = window.open(url,"Cliente","toolbar=yes,location=0,directories=no,status=no,menubar=yes,scrollbars=yes,resizable=yes,screenX=800,top=800,left=200,screenY=300,width=500,height=400,modal=yes");
   }

   function fxGetCustomerDetail(customerId){
      var param="?customerId=" + customerId;                  
      var url="/portal/page/portal/nextel/CUST_DETAIL"+param;
      WinAsist = window.open(url,"Cliente","toolbar=yes,location=0,directories=no,status=no,menubar=yes,scrollbars=yes,resizable=yes,screenX=800,top=800,left=200,screenY=300,width=500,height=400,modal=yes");
   }	
</script>

<%}catch(SessionException se) {
     //se.printStackTrace();
     out.println("<script>alert('Su sesión ha finalizado. Ingrese nuevamente');</script>");
     String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";
     //out.print("<script>parent.mainFrame.location.replace('"+strUrl+"');</script>");
   }catch(Exception e) {
     String strMessageExceptionGeneralStart = "";
     strMessageExceptionGeneralStart = MiUtil.getMessageClean(e.getMessage());
     out.println("<script>alert('"+strMessageExceptionGeneralStart+"');</script>");   
   }
%>