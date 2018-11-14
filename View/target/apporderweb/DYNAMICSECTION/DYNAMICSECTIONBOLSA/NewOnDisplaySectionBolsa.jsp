<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.*" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pe.com.nextel.service.CustomerService"%>
<%
CustomerService   objCustomerService   = new CustomerService();
  String customerId = request.getParameter("txtCompanyId");
  String strResponsablePagoBolsa = request.getParameter("cmbResponsablePago");
  HashMap  objHashMap =  objCustomerService.getCustomerSites(MiUtil.parseLong(customerId),null);
  if( objHashMap.get("strMessage")!=null ) throw new Exception((String)objHashMap.get("strMessage"));
  
  ArrayList lista_sites = (ArrayList)objHashMap.get("objArrayList");
%>
<table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
	<tr>
		<td align="left">&nbsp;
		</td>
	</tr>
</table>
<table border="0" cellspacing="0" cellpadding="0" width="100%" class="RegionBorder"> 
<tr>
<td>
<table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
	<tr>
		<td align="center">
			<table border="0" cellspacing="1" cellpadding="1" align="center" class="BannerSecondaryLink" width="100%">
				<tr>
					<td class="CellLabel" align="center" width="20%">Responsable de Pago Bolsa</td>
					<td class="CellLabel" align="center">&nbsp;</td>
					<td class="CellLabel" align="center">&nbsp;</td>
					<td class="CellLabel" align="center">&nbsp;</td>
					<td class="CellLabel" align="center">&nbsp;</td>
					<td class="CellLabel" align="center">&nbsp;</td>
					<td class="CellLabel"></td>
				</tr>
				<tr>
					<td class="CellContent">
          <select name="cmbResponsablePagoBolsa" onchange="fxCargarRespomsablePago();" > 
            <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
            <% 
            if ( lista_sites != null && lista_sites.size() > 0 ){
            
               for( int i=0; i<lista_sites.size();i++ ){ 
                    HashMap   hsMapLista = (HashMap)lista_sites.get(i);
            %>
              <%             
                if(strResponsablePagoBolsa.equals(hsMapLista.get("wn_swsiteid"))){
              %>
                  <option selected="selected" value='<%=hsMapLista.get("wn_swsiteid")%>'>
                           <%=hsMapLista.get("wv_swsitename").toString()+" "+hsMapLista.get("wv_npcodbscs")%>
                  </option>
              <%             
                }else{
              %>
                  <option value='<%=hsMapLista.get("wn_swsiteid")%>'>
                           <%=hsMapLista.get("wv_swsitename").toString()+" "+hsMapLista.get("wv_npcodbscs")%>
                  </option>
              <%             
                }
              %>
            <%             
                }
            }
            %>
                         
          </select>
					</td>
					<td class="CellContent">&nbsp;
					</td>
					<td class="CellContent">&nbsp;
					</td>
					<td class="CellContent">&nbsp;
					</td>
					<td class="CellContent">&nbsp;
					</td>
					<td class="CellContent">&nbsp;
					</td>
					<td class="CellContent">
					</td>
				</tr>
				<tr>
			</table>
		</td>
	</tr>
</table>
</td>
</tr>
</table>
<script type="text/javascript" defer="defer">
function fxSectionBolsaCreacionValidate(){
  var responsablePagoBolsa = document.frmdatos.cmbResponsablePagoBolsa;
  var responsablePago = document.frmdatos.cmbResponsablePago;
  if((responsablePagoBolsa.value=="" && responsablePago.value!="")||(responsablePagoBolsa.value!="" && responsablePago.value=="")){
    alert("Debe seleccionar el responsable de pago bolsa");
    responsablePagoBolsa.focus();
    return false;
  }
  return true; 
} 
function fxCargarRespomsablePago(){
  var responsablePagoBolsa = document.frmdatos.cmbResponsablePagoBolsa;
  var responsablePago = document.frmdatos.cmbResponsablePago;
  var existe = true;
    for (i = 0; i < responsablePago.length; i++) {
      if (responsablePago[i].value == responsablePagoBolsa.value) {
         existe = false;
         break;
      }   
    }
    if(existe){
      AddNewOption(responsablePago, responsablePagoBolsa.value,responsablePagoBolsa.options[responsablePagoBolsa.selectedIndex].text);
    }
  responsablePago.value=responsablePagoBolsa.value;
}
</script>