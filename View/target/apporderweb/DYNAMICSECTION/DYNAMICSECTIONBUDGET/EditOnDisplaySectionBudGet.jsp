<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.*"%>
<%@ page import="pe.com.nextel.util.*" %>
<%@ page import="pe.com.nextel.bean.BudgetBean"%>
<%@ page import="pe.com.nextel.service.EditOrderService"%>
<%
String strStatus = null;
String strOrderId = null;
Hashtable hshtInputEditSection = (Hashtable)request.getAttribute("hshtInputEditSection");
strStatus = (String)hshtInputEditSection.get("strStatus");
strOrderId = (String)hshtInputEditSection.get("strOrderId");
if(Constante.INBOX_PRESUPUESTO.equals(strStatus)){
EditOrderService objEditOrderService= new EditOrderService();
BudgetBean budget = new BudgetBean();
budget.setNpgeneratorid(MiUtil.parseLong(strOrderId));
budget.setNpgeneratortype(Constante.GENERATOR_TYPE_ORDER);
List budgetsCommercialList = null;
List budgetsReserveList = null;
Map budgetsMap = null;
String strMessage = null;

budgetsMap = (HashMap)objEditOrderService.budgetsAvailableChannels("Comercial",budget);
strMessage = (String)budgetsMap.get(Constante.MESSAGE_OUTPUT);
if(strMessage != null){
  throw new Exception(strMessage);
}
budgetsCommercialList = (List)budgetsMap.get("budgetsCommercialList");

budgetsMap = (HashMap)objEditOrderService.budgetsAvailableChannels("Reserva",budget);
strMessage = (String)budgetsMap.get(Constante.MESSAGE_OUTPUT);
if(strMessage != null){
  throw new Exception(strMessage);
}
budgetsReserveList = (List)budgetsMap.get("budgetsReserveList");
%>
<table border="0" cellspacing="0" cellpadding="0" width="15%">
<tr class="PortletHeaderColor"> 
      <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
      <td class="SubSectionTitle" align="left" valign="top">Presupuesto</td>
      <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"></td>
</tr>
</table>
<table border="0" cellspacing="0" cellpadding="0" class="RegionBorder">
	<tr>
		<td align="center" width="80%">
			<table border="0" cellspacing="1" cellpadding="1" align="center" width="100%">
				<%
          if(budgetsCommercialList!=null && budgetsCommercialList.size()>0){
        %>
            <tr>
              <td class="CellLabel" align="center" width="20%">Tipo de Presupuesto</td>
              <td class="CellLabel" align="center">Canal Solicitante</td>
              <td class="CellLabel" align="center">Monto Presupuesto</td>
              <td class="CellLabel" align="center">Monto Utilizado</td>
              <td class="CellLabel" align="center">Saldo Disponible</td>
              <td class="CellLabel" align="center">Afectaci&oacute;n</td>
            </tr>
        <%
            for(int i=0; i<budgetsCommercialList.size(); i++){
                BudgetBean budgetBean = (BudgetBean)budgetsCommercialList.get(i);
        %>
            <tr>
              <td class="CellContent"><%=budgetBean.getNpDescription()%></td>
              <td class="CellContent"><%=budgetBean.getNpName()%></td>
              <td class="CellContent"><%=MiUtil.formatDecimal(budgetBean.getNpBudgetAmount())%></td>
              <td class="CellContent"><%=MiUtil.formatDecimal(budgetBean.getNpConsumedAmount())%></td>
              <td class="CellContent"><%=MiUtil.formatDecimal(budgetBean.getResidue())%></td>
              <td class="CellContent"><%=MiUtil.formatDecimal(budgetBean.getPresupuesto())%></td>
            </tr>
        <%
            }
          }else{
        %>
            <tr>
              <td colspan="6" class="CellContent">Orden no cuenta con disponibilidad de presupuestos de canales comerciales</td>
            </tr>
        <%
          }
        %>
			</table>
		</td>
	</tr>
  <tr>
		<td valign="top" align="center">
			<table border="0" cellspacing="1" cellpadding="1" align="center" width="100%">
        <%
          if(budgetsReserveList!=null && budgetsReserveList.size()>0){
        %>
            <tr>
              <td class="CellLabel" colspan="4" align="center" width="20%">Canal de Reserva</td>
            </tr>
            <tr>
              <td class="CellLabel" align="center" width="20%">Tipo de Presupuesto</td>
              <td class="CellLabel" align="center">Monto Presupuesto</td>
              <td class="CellLabel" align="center">Monto Utilizado</td>
              <td class="CellLabel" align="center">Saldo Disponible</td>
            </tr>
        <%
            for(int i=0; i<budgetsReserveList.size(); i++){
                BudgetBean budBean = (BudgetBean)budgetsReserveList.get(i);       %>
            <tr>
              <td class="CellContent"><%=budBean.getNpDescription()%></td>
              <td class="CellContent"><%=MiUtil.formatDecimal(budBean.getNpBudgetAmount())%></td>
              <td class="CellContent"><%=MiUtil.formatDecimal(budBean.getNpConsumedAmount())%></td>
              <td class="CellContent"><%=MiUtil.formatDecimal(budBean.getResidue())%></td>
            </tr>
        <%
            }
          }else{
        %>
            <tr>
              <td colspan="6" class="CellContent">Orden no cuenta con disponibilidad de presupuestos de canales de reserva</td>
            </tr>
        <%
          }
        %>
			</table>
		</td>
	</tr>
</table>
<br>
<%
}
%>