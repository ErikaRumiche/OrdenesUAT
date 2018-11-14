<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>


<table border="0" cellspacing="1" cellpadding="2" width="100%" >
   <tr align="center"> 
     <td>
      <input type="hidden" name="v_saveOption" value="0"> 
      <input type="hidden" name="flgSave" value="0">
      <input type=button name="btnUpdOrder" value="Grabar" disabled="disabled" onClick="this.form.v_saveOption.value=1;fxDoSubmit('updateOrden')">
      <input type=button name="btnUpdOrderInbox" value="Grabar e ir al Inbox" disabled="disabled" onclick="this.form.v_saveOption.value=2;fxDoSubmit('updateOrden')">
     </td>
   </tr> 
</table>

</form>
<script >

function fxLoadEditPage(){
  fxOnLoadSections();
  document.frmdatos.btnUpdOrder.disabled = false;    //PM0010311 - OTrillo 24/09/2015
  document.frmdatos.btnUpdOrderInbox.disabled = false;   //PM0010311 - OTrillo 24/09/2015

  fxIndiceItemDevice();
}
onload=fxLoadEditPage;
</script>