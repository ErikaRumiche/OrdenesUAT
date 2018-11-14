<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.*"%>
<%@ page import="pe.com.nextel.util.*" %>
<%@ page import="pe.com.nextel.bean.BudgetBean"%>
<%@ page import="pe.com.nextel.service.EditOrderService"%>
<%
String strStatus = null;
Hashtable hshtInputEditSection = (Hashtable)request.getAttribute("hshtInputEditSection");
strStatus = (String)hshtInputEditSection.get("strStatus");
if(Constante.INBOX_PRESUPUESTO.equals(strStatus)){
//if("TIENDA01".equals(strStatus)){
String strOrderId = null;
String strMessage = null;
String strRejectdescription = null;
Map budgetReasonMap = null;
List budgetReasonList = null;
BudgetBean budget = null;
BudgetBean budgetBean = new BudgetBean();
EditOrderService objEditOrderService= new EditOrderService();

strOrderId = (String)hshtInputEditSection.get("strOrderId");
budgetBean.setNpgeneratorid(MiUtil.parseLong(strOrderId));
budgetBean.setNpgeneratortype(Constante.GENERATOR_TYPE_ORDER);
budgetReasonMap = (HashMap)objEditOrderService.doGetBudgetReasons();
strMessage = (String)budgetReasonMap.get(Constante.MESSAGE_OUTPUT);
if(strMessage != null){
  throw new Exception(strMessage);
}
budgetReasonList = (ArrayList)budgetReasonMap.get("budgetReasonList");
budgetReasonMap = null;
budgetReasonMap = (HashMap)objEditOrderService.doGetLastReasonDescription(budgetBean);
strMessage = (String)budgetReasonMap.get(Constante.MESSAGE_OUTPUT);
if(strMessage != null){
  throw new Exception(strMessage);
}
strRejectdescription = (String)budgetReasonMap.get("strRejectdescription");
%>
<script language="javascript" defer>
  function fxhideReasonRejection(){
    var tabMotivoRechazo = document.getElementById("tabMotivoRechazo");
    var cmbAction = document.getElementById("cmbAction");
    if("BAGLOCK"==cmbAction.value){
        tabMotivoRechazo.style.display="block"
    }else{
      tabMotivoRechazo.style.display="none"
    }
  }
  function fxValidateReasonRejection(){
    var motivoRech = document.getElementById("motivoRech");
    var cmbAction = document.getElementById("cmbAction");
    var budgetReasonId = document.getElementById("budgetReasonId");
    if("BAGLOCK"==cmbAction.value){
        if(budgetReasonId.value==""){
          alert("El campo Motivo Rechazo debe estar seleccionado");
          budgetReasonId.focus();
          return false;
        }
        if(motivoRech.value==""){
          alert("Debe ingresar la del descripción del Motivo Rechazo");
          motivoRech.focus();
          return false;
        }
    }else{
      budgetReasonId.value="";
      motivoRech.value="";
    }
    return true;
  }
</script>
<table id="tabMotivoRechazo" style="display: none;" border="0" cellspacing="0" cellpadding="0" width="100%">
<tr><td>
<table border="0" cellspacing="0" cellpadding="0" width="15%">
<tr class="PortletHeaderColor"> 
      <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
      <td class="SubSectionTitle" align="left" valign="top">Motivo Rechazo</td>
      <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"></td>
</tr>
</table>
<table border="0" cellspacing="0" cellpadding="0" class="RegionBorder">
  <tr>
    <td valign="top" align="right">
      <table cellspacing="1" cellpadding="1" align="center" width="100%">
        <tr>
          <td class="CellLabel" width="20%" >
            &nbsp;Motivo Rechazo
          </td>
          <td class="CellContent" align="left" >
            <select name="budgetReasonId" id="budgetReasonId" >
              <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
              <%
                if ( budgetReasonList != null && budgetReasonList.size() > 0 ){
                    for(int i=0; i<budgetReasonList.size();i++){
                    budget = (BudgetBean)budgetReasonList.get(i);
              %>
                    <option value="<%=budget.getNpBudgetReasonId()%>"><%=budget.getNpdescriptionReason()%></option>
              <%
                    }
                }
              %>
            </select>
          </td>
        </tr>
        <tr>
          <td colspan="2" >
          <%
            if(strRejectdescription!=null){
          %>
              <textarea name="motivoRech" id="motivoRech" cols="137" rows="5"><%=strRejectdescription%></textarea>
          <%     
            }else{
          %>
             <textarea name="motivoRech" id="motivoRech" cols="137" rows="5"></textarea>
           <%     
            }
          %>
          </td>
        </tr>
      </table>
    </td>
	</tr>
</table>
</td></tr>
</table>
<%
}
%>