<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>

<%@ page import="pe.com.nextel.service.SessionService"%>
<%@ page import="pe.com.nextel.service.CustomerService"%>

<%@ page import="pe.com.nextel.bean.PortalSessionBean"%>
<%@ page import="pe.com.nextel.bean.SubRegCustomerInfoBean"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="pe.com.nextel.util.Constante"%>

<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.HashMap"%>
<%  
 try{     
  //PARAMETRO del PAGE   
  Hashtable hshParam=(Hashtable)request.getAttribute("hshtInputEditSection");  
  if (hshParam==null) hshParam=new Hashtable();
  
  long lOrderId=MiUtil.parseLong((String)hshParam.get("strOrderId"));
  String strSessionId=MiUtil.getString((String)hshParam.get("strSessionId"));   
  
  System.out.println("[EditOnDisplayRegularizeCustomerInfo]Sesión a consultar : " + strSessionId);
  PortalSessionBean objSessionBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
  System.out.println("[EditOnDisplayRegularizeCustomerInfo]Sesión a consultar : " + objSessionBean);
  if( objSessionBean == null ){
    throw new Exception("No se encontró la sesión del usuario");
  }
  
  HashMap objData = new HashMap();
  CustomerService customerService = new CustomerService();
  SubRegCustomerInfoBean customerInfoBean = new SubRegCustomerInfoBean();
      
  objData = customerService.getSubRegOrder(lOrderId);
  
  if(objData.get(Constante.MESSAGE_OUTPUT)!=null){
    throw new Exception(objData.get(Constante.MESSAGE_OUTPUT).toString());
  }else{
    customerInfoBean = (SubRegCustomerInfoBean)objData.get("customerInfoBean");
  }
    
  System.out.println("------ INCIO DE EditOnDisplayRegularizeCustomerInfo.jsp -------");
  System.out.println("lOrderId--> "+lOrderId);
  System.out.println("strSessionId--> "+strSessionId);
  System.out.println("------ FIN DE EditOnDisplayRegularizeCustomerInfo.jsp -------");

%>
<br>
<script defer>
  function fxValidateSectionRegularize(){
      var form = document.frmdatos;
      
      if(form.txtNumeroTelefono.value==""){
        alert("Debe ingresar un numero de teléfono");
        return false;
      }else{
        if(!ContentOnlyNumber(form.txtNumeroTelefono.value)){
          alert("Número de Nextel no válido");
          form.txtNumeroTelefono.select();
          return false;
        }
      }
      
      if(form.txtRazonsocial.value==""){
        alert("Debe ingresar la razon social");
        return false;
      }    
      return true;
  }
  
  function fxSubRegOnLoad(){}
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
      <input type="text" name="txtNumeroTelefono" value="<%= MiUtil.getString(customerInfoBean.getStrPhone())%>" readOnly size="60" maxlength="40">
    </td>
  </tr>
  <tr>
    <td class="CellLabel" align="left">&nbsp;</td>
    <td class="CellLabel" align="left">Razón Social</td>
    <td class="CellContent" align="left">&nbsp;
      <input type="text" name="txtRazonsocial" value="<%= MiUtil.getString(customerInfoBean.getStrRazonSoc())%>" onchange="this.value=trim(this.value.toUpperCase())" size="60" maxlength="40">
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