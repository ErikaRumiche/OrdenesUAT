<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.*" %>
<% //Developer: RHUACANI-ASISTP - 24/10/2010
String nameHtmlItem = (String)request.getParameter("nameObjectHtml");
String type_window =  MiUtil.getString((String)request.getAttribute("type_window"));
String strAssignedNumbers = "";
String[] arrayAssignedNumbers=null;
String[] arrayAssignedNumbersTemp=null;
String[] arrayAssignedNumbersValue=null;
String cadValue="";
cadValue="10000,20000";
int cantNum=0;
if( !"NEW".equals(type_window)){
String[] paramNpobjitemvalue      = request.getParameterValues("b");
String[] paramNpobjitemheaderid   = request.getParameterValues("a");
if(paramNpobjitemheaderid != null){
    for(int i=0;i<paramNpobjitemheaderid.length; i++){
       // Numeros Asignados
       if( paramNpobjitemheaderid[i].equals(Constante.ITEM_HEADER_ID_RESERVA_NUMEROS) ) {
          strAssignedNumbers = paramNpobjitemvalue[i];
          System.out.println("strAssignedNumbers ------------->" + strAssignedNumbers);
          arrayAssignedNumbersTemp=strAssignedNumbers.split(",");
          if(arrayAssignedNumbersTemp.length>3) {
             cantNum = arrayAssignedNumbersTemp.length/3;
             arrayAssignedNumbers = new String[cantNum];
             arrayAssignedNumbersValue = new String[cantNum];
             for (int j=0; j<cantNum; j++) {
                arrayAssignedNumbers[j]=arrayAssignedNumbersTemp[j*3];
                //arrayAssignedNumbersValue[j]=arrayAssignedNumbersTemp[j*3 + 1] + "," + arrayAssignedNumbersTemp[j*3 + 2];
             }
          }  else {
             arrayAssignedNumbers = new String[1];
             arrayAssignedNumbers[0] = arrayAssignedNumbersTemp[0];
             //arrayAssignedNumbersValue[0]=arrayAssignedNumbersTemp[1] + "," + arrayAssignedNumbersTemp[2];
          }
       }
    }   
  }
}
%>
<script>
function fxLoadPopUpOrders(){
var planid="";
try
{
  planid=document.getElementById("cmb_ItemPlanTarifario").value;
}catch(e)
{
  document.getElementById("txt_ItemNewPlantarifarioId").value;
}
//BEGIN PBASUALDO 20101206
if(planid=="")
{
  alert("Debe seleccionar un plan tarifario");
  return;
}

var v=window.open("<%=request.getContextPath()%>/htdocs/jsp/controls/POPUP/PopUpNumberReserve.jsp?htx_patron=-1&htx_popUpType=3&htx_planid="+planid,"PopUpNumberReserve","width=480,height=310,scrollbars=no,menubar=no,url=0,toolbar=no,top=100,left=200"); 
v.focus();
}

</script>
  
    <td class="CellLabel">&nbsp; &nbsp; &nbsp;Numeros Asignados</td>
    <td class="CellContent" >
      <select name="<%=nameHtmlItem%>" multiple>
        <%
          if(arrayAssignedNumbers!=null &&arrayAssignedNumbers.length>0 ){
            for(int i=0;i<arrayAssignedNumbers.length;i++){%>
            <option value=<%=cadValue%>>
                         <%=arrayAssignedNumbers[i]%>
           </option>
            
           <% }
          }
        %>
      </select>&nbsp;&nbsp;&nbsp;
        <a href="javascript:fxLoadPopUpOrders();">Reservar Números</a> 
    </td>
