<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="pe.com.nextel.bean.AddressObjectBean"%>
<%@ page import="pe.com.nextel.service.CustomerService"%>

<%
    //EFH001 Añadido Código para control de sesión.
    System.out.println("--------- CustomerAddressDelivery.jsp ---------");
    String urlIngreso = request.getRequestURL()+(request.getQueryString() != null ? "?" + request.getQueryString() : "");
    String jspIngreso = "CustomerAddressDelivery.jsp";
    String ipIngreso =  request.getRemoteAddr();
    String  contexto = request.getContextPath();
    //FIN
  int intObjectId = 0;
  String strObjectType = "";

  intObjectId   = MiUtil.parseInt(request.getParameter("intObjectId"));
  strObjectType = request.getParameter("strObjectType");
   
  String strGeneratorType = request.getParameter("generatorType")==null?"":request.getParameter("generatorType");
  String strRegionId      = request.getParameter("regionAddressId")==null?"":request.getParameter("regionAddressId");
  
  CustomerService objCustomerService = new CustomerService();
%>

<html>
  <head><title>Listado de Direcciones</title></head>
  <link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">
  
  <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
  <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
  <script language="javascript">
      //EFH001 Añadido Código para control de sesión.
      if (parent.opener==undefined||parent.opener== null){

          window.location.replace("<%=contexto%>"+"/htdocs/jp_session_validate/Session_Validate.jsp"+"?urlIngreso="+"<%=urlIngreso%>"+"&jspIngreso="+"<%=jspIngreso%>"+"&ipIngreso="+"<%=ipIngreso%>");
      }
      //FIN
     parent.document.title = "Portal Nextel";
     window.focus();

     function fxCerrar(){
        parent.close();
     }
         
     function fxAdressElec(ind) {        
        frmCurrent = document.frmdatoslist;
        form = parent.opener.parent.mainFrame.document.frmdatos;
        
        if(frmCurrent.hdnDeliveryAddress[ind].value != "null"){
           form.txtDirEntrega.value        = frmCurrent.hdnDeliveryAddress[ind].value;
           form.hdnDeliveryAddress.value   = frmCurrent.hdnDeliveryAddress[ind].value;
        }
        else{
           form.txtDirEntrega.value    = "";
           form.hdnDeliveryAddress.value   = "";
        }        
        if (frmCurrent.hdnDeliveryCity[ind].value != "null"){         
           form.txtCity.value              = frmCurrent.hdnDeliveryCity[ind].value;
           form.hdnDeliveryCity.value      = frmCurrent.hdnDeliveryCity[ind].value;
           form.hdnDeliveryCityId.value    = frmCurrent.hdnDeliveryCityId[ind].value;
        }
        else{           
           form.txtCity.value = "";
           form.hdnDeliveryCity.value      = "";
        }
        if (frmCurrent.hdnDeliveryProvince[ind].value != "null"){
           form.txtProvince.value           = frmCurrent.hdnDeliveryProvince[ind].value;
           form.hdnDeliveryProvince.value   = frmCurrent.hdnDeliveryProvince[ind].value;
           form.hdnDeliveryProvinceId.value = frmCurrent.hdnDeliveryProvinceId[ind].value;
        }
        else{
           form.txtProvince.value = "";
           form.hdnDeliveryProvince.value  = "";
        }
        if (frmCurrent.hdnDeliveryState[ind].value != "null"){
           form.txtState.value             = frmCurrent.hdnDeliveryState[ind].value;
           form.hdnDeliveryState.value     = frmCurrent.hdnDeliveryState[ind].value;  
           form.hdnDeliveryStateId.value   = frmCurrent.hdnDeliveryStateId[ind].value;  
        }
        else{
           form.txtState.value = "";
           form.hdnDeliveryState.value     = "";  
        }        
                        
        //Region
        form.txtRegionSite.value        = frmCurrent.hdnDeliveryState[ind].value;                
        
        //parent.opener.parent.mainFrame.dirEntregaDiv.display = '';
  
        parent.close();
     }
  </script>

      <table border="0" width="100%" cellpadding="0" cellspacing="1" class="RegionBorder">
         <form name="frmdatoslist" id="frmdatoslist" method="post" target="mainFrame">
         <input type=hidden name='hdnDeliveryAddress'>
         <input type=hidden name='hdnDeliveryCity'>
         <input type=hidden name='hdnDeliveryProvince'>
         <input type=hidden name='hdnDeliveryState'>
         
         <input type=hidden name='hdnDeliveryCityId' >
         <input type=hidden name='hdnDeliveryProvinceId'>
         <input type=hidden name='hdnDeliveryStateId'>
         
            <tr>
            <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
               <tr class="PortletHeaderColor">
                  <td class="LeftCurve" valign="top" align="left" width="10">&nbsp;&nbsp;</td>
                  <td class="PortletHeaderColor" align="left" valign="top">
                     <font class="PortletHeaderText">Listado de Direcciones</font></td>
                  <td align="right" class="RightCurve" width="10%" >&nbsp;&nbsp;</td>
               </tr>
            </table>
            </tr>
            <tr>
               <table border="1" width="100%" cellpadding="2" cellspacing="0" class="RegionBorder" align="center" >
                  <tr>
                     <td width="100%"class="RegionHeaderColor">
                  <table border="0" width="95%" cellpadding="0" cellspacing="1" class="RegionBorder" align="center">
                           <!-- Listado Resumen de Servicios -->
                           <tr>
                              <td align="center" class="ListRow0"><b>&nbsp;&nbsp;</b></td>
                              <td align="center" class="ListRow0"><b>&nbsp;Dirección&nbsp;</b></td>
                              <td align="center" class="ListRow0"><b>&nbsp;Distrito&nbsp;</b></td>
                              <td align="center" class="ListRow0"><b>&nbsp;Departamento&nbsp;</b></td>
                              <td align="center" class="ListRow0"><b>&nbsp;Provincia&nbsp;</b></td>
                              
                           </tr>
                     <%
                     ArrayList listAddress = null;
                     HashMap objHashMap = null;
                     objHashMap = objCustomerService.CustomerDAOgetAddressDelivery(intObjectId,strObjectType,strGeneratorType,strRegionId);
                     
                     if( objHashMap.get("strMessage") != null ){
                     %>
                     <script>
                     var message = escape2("<%=objHashMap.get("strMessage")%>");
                     alert(message)</script>
                     <%}else{
                     listAddress = (ArrayList)objHashMap.get("objArrayList");
                     }
                     
                     if ( listAddress!=null && listAddress.size() > 0 ) {
                     int wni = 0;
                     String wv_color = "";
                         
                     for( int j=0; j<listAddress.size();j++ ){ 
                        wni+=1;
                        wv_color = (wni%2)==0?"ListRow0":"ListRow1";
                        AddressObjectBean objAddressObjectBean = (AddressObjectBean)listAddress.get(j);
                    %>
                    <tr align="center">
                        <td align="center" class="<%=wv_color%>"><textarea name="txtAddress" class="hidden"><%=objAddressObjectBean.getSwaddress1()%></textarea>
                        <a href="javascript:fxAdressElec('<%=wni%>')"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" width="15" height="15" border="0"></a><%=wni%>
                        </td>
                        <td align="center" class="<%=wv_color%>">&nbsp;<%=objAddressObjectBean.getSwaddress1()==null?"":objAddressObjectBean.getSwaddress1()%>&nbsp;</td>                        
                        <td align="center" class="<%=wv_color%>">&nbsp;<%=objAddressObjectBean.getSwcity()==null?"":objAddressObjectBean.getSwcity()%>&nbsp;</td>
                        <td align="center" class="<%=wv_color%>">&nbsp;<%=objAddressObjectBean.getSwstate()==null?"":objAddressObjectBean.getSwstate()%>&nbsp;</td>
                        <td align="center" class="<%=wv_color%>">&nbsp;<%=objAddressObjectBean.getSwprovince()==null?"":objAddressObjectBean.getSwprovince()%>&nbsp;</td>
      
                       <input type=hidden name='hdnDeliveryAddress' value="<%=objAddressObjectBean.getSwaddress1()%>">
                       <input type=hidden name='hdnDeliveryCity' value="<%=objAddressObjectBean.getSwcity()%>">
                       <input type=hidden name='hdnDeliveryProvince' value="<%=objAddressObjectBean.getSwprovince()%>">
                       <input type=hidden name='hdnDeliveryState' value="<%=objAddressObjectBean.getSwstate()%>">
                       
                       <input type=hidden name='hdnDeliveryCityId' value="<%=objAddressObjectBean.getNpdistritoid()%>"> 
                       <input type=hidden name='hdnDeliveryProvinceId' value="<%=objAddressObjectBean.getNpprovinciaid()%>">
                       <input type=hidden name='hdnDeliveryStateId' value="<%=objAddressObjectBean.getNpdepartamentoid()%>">
                    </tr>
                    <%}//Fin de For%>
                    <%}else {//Fin del if%>
                    
                    <tr>
                        <td align="center" class="ListRow0" colspan="3">No se encontraron resultados</td>       
                    </tr>
                    
                    <%}//Fin del else%>
                    
                        </table>
                        <br/>
                       
                        <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
                           <tr align="center">
                              <td width="45%">&nbsp;</td>                  
                              <td width="10%">                    
                                 <input type="button" name="btnCerrar" value="Cerrar" onclick="javascript:fxCerrar();">
                              </td>
                              <td width="45%">&nbsp;</td>
                           </tr>
                        </table>
                     </td>
                  </tr>
               </table>
            </tr>    
            </form>
         </table>
</html>
