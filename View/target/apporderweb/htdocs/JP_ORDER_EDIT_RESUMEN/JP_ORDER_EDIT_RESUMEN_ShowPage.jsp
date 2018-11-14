<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil" %>
<%@ page import="oracle.portal.provider.v2.ProviderSession" %>
<%@ page import="oracle.portal.provider.v2.ProviderUser" %>
<%@ page import="oracle.portal.provider.v2.url.UrlUtils" %>
<%@ page import="pe.com.nextel.service.SessionService" %>
<%@ page import="pe.com.nextel.service.EditOrderService" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="pe.com.nextel.service.CustomerService" %>
<%@ page import="pe.com.nextel.bean.*" %>
<%@ page import="pe.com.nextel.util.*" %>
<%@page import="pe.com.nextel.exception.SessionException" %>
<%@ page import="java.util.*" %>
                
<% 
try{
   System.out.println("---------------------- INICIO JP_ORDER_EDIT_RESUME------------------"); 
   String strSession="";
   /* inicio - comentar para probar localmente*/
   
  try{
    PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    ProviderUser objetoUsuario1 = pReq.getUser();
    strSession=objetoUsuario1.getPortalSessionId();
    System.out.println("Sesión capturada  JP_ORDER_EDIT_RESUMEN_ShowPage : " + objetoUsuario1.getName() + " - " + strSession );
  }catch(Exception e){
    System.out.println("Portler Not Found : " + e.getClass() + " - " + e.getMessage() );
    out.println("Portlet JP_ORDER_EDIT_RESUMEN_ShowPage Not Found");
    return;
  }
  
  //strSession = "98102396";
  
  System.out.println("Sesión capturada después del resuest : " + strSession );
	PortalSessionBean objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSession);
	if(objPortalSesBean==null) {
    System.out.println("No se encontró la sesión de Java ->" + strSession);
		throw new SessionException("La sesión finalizó");
	}
  
  int iUserId;//psbSesion.getUserid();
   int iAppId;//psbSesion.getAppId(); 
   String strUser=null;
   String strLogin=null;      
   iUserId=objPortalSesBean.getUserid();
   iAppId= objPortalSesBean.getAppId();
   strUser=objPortalSesBean.getNom_user();
   strLogin=objPortalSesBean.getLogin();
   
   HashMap hshData=null;
   HashMap hshOrder = null;
   String strMessage=null;
   OrderBean objOrderBean=null;
   CustomerBean objCustomerBean=null;     
   EditOrderService objOrderService=new EditOrderService();
   CustomerService objCustomerService=new CustomerService();
   GeneralService objGeneralService= new GeneralService();
   
   //Parametros       
   String strOrderId=(request.getParameter("an_nporderid")==null?"0":request.getParameter("an_nporderid"));
   long lOrderId=Long.parseLong(strOrderId);
   long lCustomerId=0;
   String strSubCategory=null;
   int iSubCategory=0;
   
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
   
   hshData=objGeneralService.getCustomerValue(lCustomerId);
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);     

   int iCompanyValue=MiUtil.parseInt((String)hshData.get("iRetorno"));      
  
   hshData=objGeneralService.getRol(Constante.SCRN_OPTTO_IMGCATEG, iUserId, iAppId);  
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);     
   
   int iFlaggersup=MiUtil.parseInt((String)hshData.get("iRetorno"));

   String strURLAreaCode=request.getContextPath()+"/GENERALPAGE/AlertDniRucEquals.jsp";

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
   
   //Ruta
   String strRutaContext=request.getContextPath();
   
   /*FNAJARRO : Obtiene ruta del servidor para los formatos relacionados con la categoria de la orden */
   HashMap objHashMap=objGeneralService.getNameServerReport(Constante.FORM,Constante.URL_SERVER_SERVLET);
   strMessage=(String)objHashMap.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);
   String strNameServerReport=(String)objHashMap.get("strNameServerReport");
   System.out.println("Nombre del Servidor: "+strNameServerReport);
   
   System.out.println("---------------------- FIN JP_ORDER_EDIT_RESUME------------------");   
%>  
  <!-- Inicio JP_ORDER_EDIT_RESUMEN.jsp-->

<table BORDER="0" class="PortletHeaderColor" CELLSPACING="0" CELLPADDING="0" WIDTH="100%">
<TR class="PortletHeaderColor">
   <TD class="LeftCurve" valign="top" align=LEFT width="10" height="10" NOWRAP="">&nbsp;&nbsp;</TD>
   <TD class="PortletHeaderColor" NOWRAP="" align=LEFT valign="top" width="100%">
   <FONT class="PortletHeaderText">Orden > Detalle</FONT>
   &nbsp;&nbsp;&nbsp;</TD>
   <TD align="RIGHT" class="PortletHeaderColor" NOWRAP="" valign="top">&nbsp;
   </TD><TD align="right" class="RightCurve" width="10" NOWRAP="">&nbsp;&nbsp;</TD>
</TR>
</table>
<table border="0" cellspacing="1" cellpadding="2" width="100%" class="RegionBorder">

<TR>
   <TD ALIGN="CENTER" CLASS="CellLabel">Pedido ID</TD>
   <TD ALIGN="CENTER" CLASS="CellLabel">N° Solicitud</TD>
   <TD align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" >
      <TR>
         <TD ALIGN="LEFT" CLASS="CellLabel">&nbsp;Razón Social</TD>    
         <script>
            function fxValidateRol(ncustomerid){
               var v_flag = "<%=iFlaggersup%>";
               if (v_flag == 1){
                  url = "/portal/pls/portal/WEBSALES.NPSL_CUSTOMER_EXT_PL_PKG.PL_CUST_VALUE_DETAIL?an_customerid=" + v_customerid;
                  url = "/portal/pls/portal/websales.NPSL_GENERAL_PL_PKG.WINDOW_FRAME?av_title="+escape("Valorización > Segmentación Detalle")+"&av_url="+escape2(url);
                  WinAsist = window.open(url,"ValorCliente","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=no,screenX=100,top=80,left=100,screenY=80,width=750,height=350,modal=yes");
               }
               else{
                  return;
               }
            }
            
            
            function fxCleanImgCustValue() {
               divCustValue.innerHTML = "<img src='<%=Constante.PATH_APPORDER_SERVER%>/images/CustValue/CustValue.gif' border=0 align='texttop'>" ;
            }
         </script>                
         <% if (lCustomerId == 0){%>
         <td align="right" valign="bottom" width="60" class="CellContent">
            <div id="divCustValue" name="divCustValue" style="display:inline">
            <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/CustValue/CustValue.gif" border="0" align="texttop">
            </div>
         </td>
         <%}else{
            //llamada al api q devuelve la valoracion
            if(iFlaggersup==1){ // si tiene acceso
               if (iCompanyValue!=0){
            %>
            <td align="right" valign="bottom" width="60" class="CellLabel">
               <div id="divCustValue" name="divCustValue" style="display:inline">
               <a href="javascript:fxValidateRol('<%=lCustomerId%>')">
               <img align="texttop" src="<%=Constante.PATH_APPORDER_SERVER%>/images/CustValue/CustValue<%=iCompanyValue%>.gif" border="0">
               </a>
               </div>
            </td>
            <%
              }else{
            %>
            <td align="right" valign="bottom" width="60" class="CellLabel">
               <div id="divCustValue" name="divCustValue" style="display:inline">
               <img align="texttop" src="<%=Constante.PATH_APPORDER_SERVER%>/images/CustValue/CustValue1.gif" border="0">
               </div>
            </td>
            <%
               }
            }else {
              if (iCompanyValue!=0){
            %>
            <td align="right" valign="bottom" width="60" class="CellLabel">
               <div id="divCustValue" name="divCustValue" style="display:inline">
               <img align="texttop" src="<%=Constante.PATH_APPORDER_SERVER%>/images/CustValue/CustValue<%=iCompanyValue%>.gif" border="0">
               </div>
            </td>
            <%
            }else{
            %>
            <td align="right" valign="bottom" width="60" class="CellLabel">
               <div id="divCustValue" name="divCustValue" style="display:inline">
               <img align="texttop" src="<%=Constante.PATH_APPORDER_SERVER%>/images/CustValue/CustValue1.gif" border="0">
               </div>
            </td>
            <%
            }
      }
      }
         %>      
                      
      </TR>
      </table>
   </TD>
   <TD ALIGN="CENTER" CLASS="CellLabel">Tel&eacute;fono</TD>
   <TD ALIGN="CENTER" CLASS="CellLabel">Categor&iacute;a</TD>
   <TD ALIGN="CENTER" CLASS="CellLabel">Sub Categor&iacute;a</TD>
   <TD ALIGN="CENTER" CLASS="CellLabel">Fecha</TD>
</TR>
   <%if (objOrderBean!= null){%>
<TR>
   <TD ALIGN="CENTER" CLASS="CellContent"><%=(objOrderBean.getNpOrderId()==0?"":objOrderBean.getNpOrderId()+"")%></TD>
   <TD id="tdOrderCode" ALIGN="CENTER" CLASS="CellContent"><%=MiUtil.getString(objOrderBean.getNpOrderCode())%></TD>
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

      hshOrder=objOrderService.getOrderScreenField(lOrderId,Constante.PAGE_ORDER_EDIT);
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
   <script type="text/javascript">
      function VerExcel(pedidoId,category,subCategory){                  
         <% if (iSubCategory == 2049 || iSubCategory == 2013 ) { %>
         url = "<%=strRutaContext%>/PAGEEDIT/ValidatePrePaId.jsp?an_nporderid="+pedidoId+"&av_category="+escape2(category)+ "&av_subcategory="+escape2(subCategory)+"&hdnSession=<%=strSession%>";
         WinAsist = window.open(url,"FormularioOrden","toolbar=no,location=0,directories=no,status=no,menubar=no,scrollbars=no,resizable=no,screenX=200,top=100,left=200,screenY=100,width=250,height=200,modal=yes");
         <%} else { %>           
         url = "<%=strNameServerReport%>an_orderid="+pedidoId+"&av_type="+escape2(category)+"&an_npspecificationid=<%=objOrderBean.getNpSpecificationId()%>&av_specification="+escape2(subCategory)+"&av_login="+escape2('<%=strLogin%>')+"&av_nom_user="+escape2('<%=strUser%>')+"&an_appid=<%=iAppId%>";
         WinAsist = window.open(url,"FormularioOrden","toolbar=yes,location=0,directories=no,status=yes,menubar=yes,scrollbars=no,resizable=yes,screenX=100,top=80,left=100,screenY=80,width=800,height=550,modal=yes");
         <%}%>
      }
   </script>
   <%}%>
</table>

<script language="javascript">          
   function getCustomerDetail(customerId){
      var param="?customerId=" + customerId;                  
      var url="/portal/page/portal/nextel/CUST_DETAIL"+param;
      WinAsist = window.open(url,"Cliente","toolbar=yes,location=0,directories=no,status=no,menubar=yes,scrollbars=yes,resizable=yes,screenX=800,top=800,left=200,screenY=300,width=500,height=400,modal=yes");
   }
   function getAlertDocumentEquals(strNumDoc,strTipoDoc){
      var param="?strNumDoc="+strNumDoc+"&strTipoDoc="+strTipoDoc;
      var url = "<%=strURLAreaCode%>"+param;
      WinAsist = window.open(url,"Cliente","toolbar=no,location=0,directories=no,status=no,menubar=no,scrollbars=no,resizable=yes,screenX=500,top=150,left=500,screenY=500,width=400,height=150,modal=yes");
   }   
</script>

<%}catch(SessionException se) {
    System.out.println("[JP_ORDER_EDIT_RESUMEN][SessionException] : " + se.getClass() + " - " + se.getMessage());
    se.printStackTrace();
    //out.println("<script>alert('Su sesión ha finalizado. Ingrese nuevamente');</script>");
    //String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";
     //out.print("<script>parent.mainFrame.location.replace('"+strUrl+"');</script>");
  }catch(Exception e) {
    String strMessageExceptionGeneralStart = "";
    strMessageExceptionGeneralStart = MiUtil.getMessageClean(e.getMessage());
    System.out.println("[JP_ORDER_EDIT_RESUMEN][Exception] : " + e.getClass() + " - " + e.getMessage());
    out.println("<script>alert('"+strMessageExceptionGeneralStart+"');</script>");   
  }
%>
<!-- Fin de JP_ORDER_RESUMEN.jsp -->