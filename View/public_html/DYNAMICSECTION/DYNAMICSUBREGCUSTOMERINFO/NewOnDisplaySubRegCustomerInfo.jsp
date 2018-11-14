<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>

<%@ page import="pe.com.nextel.service.SessionService"%>
<%@ page import="pe.com.nextel.service.CustomerService"%>

<%@ page import="pe.com.nextel.bean.PortalSessionBean"%>
<%@ page import="pe.com.nextel.bean.DominioBean"%>
<%@ page import="pe.com.nextel.bean.SubRegCustomerInfoBean"%>
<%@ page import="pe.com.nextel.bean.CustomerBean"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="pe.com.nextel.util.Constante"%>

<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Hashtable"%>

<%  
 try{     
  //PARAMETRO del PAGE   
  Hashtable hshParam=(Hashtable)request.getAttribute("hshtInputNewSection");  
  if (hshParam==null) hshParam=new Hashtable();
  
  long lOrderId=MiUtil.parseLong((String)hshParam.get("strOrderId"));
  String strSessionId=MiUtil.getString((String)hshParam.get("strSessionId"));   
  long lCustomerId=MiUtil.parseLong((String)hshParam.get("strCustomerId"));
  long lIncidentId=MiUtil.parseLong((String)hshParam.get("strGeneratorId"));
  
  
  System.out.println("[NewOnDisplaySubRegCustomerInfo]Sesión a consultar : " + strSessionId);
  PortalSessionBean objSessionBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
  System.out.println("[NewOnDisplaySubRegCustomerInfo]Sesión a consultar : " + objSessionBean);
  if( objSessionBean == null ){
    throw new Exception("No se encontró la sesión del usuario");
  }
  
  System.out.println("------ INCIO DE NewOnDisplaySubRegCustomerInfo.jsp -------");
  System.out.println("lCustomerId--> "+lCustomerId);
  System.out.println("lOrderId--> "+lOrderId);
  System.out.println("strSessionId--> "+strSessionId);
  System.out.println("lIncidentId--> "+lIncidentId);
  System.out.println("------ FIN DE NewOnDisplaySubRegCustomerInfo.jsp -------");
  
  String strReadOnly = "";
  HashMap objPhone = new HashMap();
  CustomerService customerService = new CustomerService();
  String strPhone = null;
  objPhone = customerService.getPhoneSubReg(lCustomerId,lIncidentId);
    
  if(objPhone.get(Constante.MESSAGE_OUTPUT)!=null){
    throw new Exception(objPhone.get(Constante.MESSAGE_OUTPUT).toString());
  }else{
    strPhone = (String)objPhone.get("strPhone");
    if(strPhone!=null){
      strReadOnly = "readOnly";
    }
  }

  
  

%>
<br>
<script>
  document.frmdatos.txtRazonsocial.value = document.frmdatos.txtCompany.value;
</script>
<table border="0" cellspacing="0" cellpadding="0" width="65%">
  <tr>
    <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="10">&nbsp;&nbsp;</td>
    <td class="SubSectionTitle" align="left" valign="top">&nbsp;&nbsp;Modificar Datos</td>
    <td class="SubSectionTitleRightCurve"  valign="top" align="right" width="11">&nbsp;&nbsp;</td>
  </tr>
</table>    
<table border="0"  width="65%"  class="RegionBorder">
  <tr>
    <td class="CellLabel" align="left">&nbsp;</td>
    <td class="CellLabel" align="left"><FONT color=red><B>*</B></FONT>&nbsp;Numero de Teléfono</td>
    <td class="CellContent" align="left">&nbsp;
      <input type="text" name="txtNumeroTelefono" value="<%= MiUtil.getString(strPhone)%>" onchange="this.value=trim(this.value.toUpperCase())" <%=strReadOnly%> size="60" maxlength="40">
    </td>
  </tr>
  <tr>
    <td class="CellLabel" align="left">&nbsp;</td>
    <td class="CellLabel" align="left">Razón Social</td>
    <td class="CellContent" align="left">&nbsp;
      <input type="text" name="txtRazonsocial" onchange="this.value=trim(this.value.toUpperCase())" size="60" maxlength="40">
    </td>
  </tr>
</table>
<%     
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