<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>

<%@ page import="pe.com.nextel.service.CreditEvaluationService"%>
<%@ page import="pe.com.nextel.bean.PortalSessionBean"%>
<%@ page import="pe.com.nextel.bean.CreditEvaluationBean"%>

<%@ page import="pe.com.nextel.service.SessionService"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%  
 try{     
  //PARAMETRO del PAGE   
  Hashtable hshParam=(Hashtable)request.getAttribute("hshtInputNewSection");  
  if (hshParam==null) hshParam=new Hashtable();
  long lOrderId=MiUtil.parseLong((String)hshParam.get("strOrderId"));
  
  String strOrderId=(String)hshParam.get("strOrderId");
    
  String strSessionId=MiUtil.getString((String)hshParam.get("strSessionId"));
  
  int iUserId=0;
  int iAppId=0;
  HashMap hshData = null;
  HashMap hshRuleResult = null;
  CreditEvaluationService objCreditEvaluationService = new CreditEvaluationService();
  CreditEvaluationBean objCreditEvaluationBean = new CreditEvaluationBean();  
  ArrayList arrRuleResult = null;
  String strMessage=null;   
  
  System.out.println("[EditOnDisplayCreditEvaluation]Sesión a consultar : " + strSessionId);
  PortalSessionBean objSessionBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
  System.out.println("[EditOnDisplayCreditEvaluation]Sesión a consultar : " + objSessionBean);
  if( objSessionBean == null ){
    throw new Exception("No se encontró la sesión del usuario");
  }
  
  iUserId=objSessionBean.getUserid();
  iAppId=objSessionBean.getAppId();
  
  String strFlagAddRP = "0";
  int  iPermission = 0;   
  
    
  System.out.println("------ INCIO DE NewOnDisplayCreditEvaluation.jsp -------");
  System.out.println("iPermission-->"+iPermission);
  System.out.println("lOrderId--> "+lOrderId);
  System.out.println("strSessionId--> "+strSessionId);
  System.out.println("iUserId--> "+iUserId);
  System.out.println("iUserId--> "+iAppId);
  System.out.println("------ FIN DE NewOnDisplayCreditEvaluation.jsp -------");
%>
<br>
<input type="hidden" name="flagEvaluationCredit">
<input type="hidden" name="flagActionCredit">
<input type="hidden" name="cmbCreditAction"> <!-- campo dummy -->
<div id="Section_EvaluacionAutomatica" style="display:none">
<table  border="0" cellspacing="0" cellpadding="0" width="65%">
  <tr>
    <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="10">&nbsp;&nbsp;</td>
    <td class="SubSectionTitle" align="left" valign="top">&nbsp;&nbsp;Evaluación Crediticia</td>
    <td class="SubSectionTitleRightCurve"  valign="top" align="right" width="11">&nbsp;&nbsp;</td>
  </tr>
</table>    
<table border="0"  width="65%"  class="RegionBorder">   
  <tr>
    <td class="CellLabel" align="left">Depósito en Garantía</td>
    <td class="CellContent" align="left">&nbsp;
      <input type="text" name="txtGuarantee" size="12" style='text-align:left' style="border :0; background-color: #F5F5EB;" readonly value="">
    </td>
  </tr>
  <tr>
    <td class="CellLabel" align="left">Moneda</td>
    <td class="CellContent" align="left">&nbsp;
      <input type="text" name="txtGuaranteeCurrency" size="12" style='text-align:left' style="border :0; background-color: #F5F5EB;" readonly value="">
    </td>
  </tr>
  <tr>
    <td colspan="2">
      <table align=center width="100%" border="0" id="rule_result" name="rule_result" cellpadding="0" cellspacing="1" class="CTable">
        <tr align="center">
          <td class="CellLabel">Condici&oacute;n</td>
          <td class="CellLabel">Comentario</td>
        </tr>        
      </table>  
    </td>
  </tr>
</table>
</div>
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
