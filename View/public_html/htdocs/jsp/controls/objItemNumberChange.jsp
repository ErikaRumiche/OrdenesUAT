<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.*" %>
<% 
String nameHtmlItem = (String)request.getParameter("nameObjectHtml");
String specificationId = (String)request.getParameter("strSpecificationId");
%>
<!--Developer: RHuacani ASISTP - 24/10/2010-->
 
<script>
function fxLoadPopUpChange(){
<!--Developer: PBasualdo - 12/11/2010-->
var planid="";
var product="";
try
{
  planid=document.getElementById("txt_ItemNewPlantarifarioId").value;
}catch(e)
{
  planid=document.getElementById("cmb_ItemPlanTarifario").value;
}
//BEGIN PBASUALDO 20101206
if(planid=="")
{
  alert("Debe seleccionar un plan tarifario");
  return;
}

try{ product=document.getElementById("cmb_ItemProducto").value; }catch(e){}

if(product=="")
{
  alert("Debe seleccionar un producto");
  return;
}
//END PBASUALDO 20101206
var v=window.open("<%=request.getContextPath()%>/htdocs/jsp/controls/POPUP/PopUpNumberReserve.jsp?htx_patron=-1&htx_popUpType=1&htx_planid="+planid+"&htx_productid="+product,"PopUpNumberReserve","width=480,height=310,scrollbars=no,menubar=no,url=0,toolbar=no,top=100,left=200"); 
v.focus();
}

</script>

  <td class="CellLabel" >
  &nbsp; &nbsp; &nbsp;Nuevo Numero
  </td>
  <td class="CellContent">
     <input type="text"  name="<%=nameHtmlItem%>" size="20" value=""/>
    &nbsp;
    <%   if((Constante.SPEC_CAMBIO_NUMERO+"").equals(specificationId)){  %>
      <a href="javascript:fxLoadPopUpChange();">Reservar Numero</a>      
    <%} %>
  </td>

