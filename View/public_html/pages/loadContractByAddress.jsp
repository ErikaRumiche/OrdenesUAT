<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="pe.com.nextel.util.MiUtil"%>
<%@page import="pe.com.nextel.util.Constante"%>
<%
int  intAddressId = MiUtil.parseInt((String)request.getParameter("strAddressId"));
long lngCustomerId = MiUtil.parseLong((String)request.getParameter("strCustomerId"));
%>
<html>
<head><title>Contratos Relacionados de la Dirección de Origen</title></head>
<link type="text/css" rel="stylesheet"
      href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"/>
<style type="CDATA">
  .show   { display:inline}
  .hidden { display:none }
</style>
<form name="frmdatos">
   <div id="divServicios">
      <table class="RegionBorder">
         <tr>
            <td class="CellLabel" align="center">Contrato</td>
            <td class="CellLabel" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Plan. Tarifario&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
            <td class="CellLabel" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Servicio&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>Principal</td>
            <td class="CellLabel" align="center">Instalaciones<br>Compartidas</td>
         </tr>
         <tr>
            <td class="CellContent" align="center" colspan=4>
               <select name="cmbSelectedServices" size="6" multiple style="font-family: Courier New; font-size: 9 pt;" >
                  <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
               </select>
               <input type="hidden" name="hdnItemSelectService">
            </td>
         </tr>
      </table>
   </div>
   
</form>
</html>

<script DEFER>

  function fxLoadContractByAddress(){
    var url="/portal/pls/portal/WEBSALES.NPSL_CUSTOMER_ADDRESS_PL_PKG.PL_LOAD_CONTRACT_BY_ADDRESS?an_customer_id="+<%=lngCustomerId%>+"&an_address_id="+<%=intAddressId%>;
    parent.bottomFrame.location.replace(url);
  }
  
  onload = fxLoadContractByAddress;
</script>


