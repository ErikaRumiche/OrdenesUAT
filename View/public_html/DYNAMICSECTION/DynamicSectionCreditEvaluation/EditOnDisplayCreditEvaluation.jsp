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
  Hashtable hshParam=(Hashtable)request.getAttribute("hshtInputEditSection");  
  if (hshParam==null) hshParam=new Hashtable();
  long lOrderId=MiUtil.parseLong((String)hshParam.get("strOrderId"));
  String strStatus = (String)hshParam.get("strStatus");
  
  String strOrderId=(String)hshParam.get("strOrderId");
    
  String strSessionId=MiUtil.getString((String)hshParam.get("strSessionId"));   
    
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
  String strFlagAddRP = "0";
  int  iPermission = 0;   
  
    
  System.out.println("------ INCIO DE EditOnDisplayCreditEvaluation.jsp -------");
  System.out.println("iPermission-->"+iPermission);
  System.out.println("lOrderId--> "+lOrderId);
  System.out.println("strSessionId--> "+strSessionId);
  System.out.println("------ FIN DE EditOnDisplayCreditEvaluation.jsp -------");
  String displayCredit = "none";

  HashMap hshValidateCredit = new HashMap();
  hshValidateCredit = objCreditEvaluationService.doValidateCredit(lOrderId,"ORDER");
  if((String)hshValidateCredit.get("strMessage")!=null){
    throw new Exception(strMessage);
  }
  String strValidateCredit = (String)hshValidateCredit.get("flagValidateCredit");
  if(strValidateCredit.equals("1")) {
    hshData = (HashMap)objCreditEvaluationService.getCreditEvaluationData(lOrderId,"ORDER");
    strMessage=(String)hshData.get("strMessage");
    if (strMessage!=null)
      throw new Exception(strMessage);
    objCreditEvaluationBean = (CreditEvaluationBean)hshData.get("objCreditEvaluationBean");
    
    hshData = (HashMap)objCreditEvaluationService.getRuleResult(objCreditEvaluationBean.getnpCreditEvaluationId(),"20");
    strMessage=(String)hshData.get("strMessage");
    if (strMessage!=null)
      throw new Exception(strMessage);
    arrRuleResult=(ArrayList)hshData.get("arrRuleResult");
  
    if (objCreditEvaluationBean.getnpGuarantee()>0 || arrRuleResult.size()>0)
      displayCredit = "block";
    else
      displayCredit = "none";
  }

%>  
<input type="hidden" name="flagEvaluationCredit">
<input type="hidden" name="flagActionCredit">
<input type="hidden" name="cmbCreditAction"> <!-- campo dummy -->
<div id="Section_EvaluacionAutomatica" style="display:<%=displayCredit%>">
<table  border="0" cellspacing="0" cellpadding="0" width="65%" >
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
      <input type="text" name="txtGuarantee" size="12" style='text-align:left' style="border :0; background-color: #F5F5EB;" readonly value="<%=MiUtil.formatDecimal(objCreditEvaluationBean.getnpGuarantee())%>">
    </td>
  </tr>
  <tr>
    <td class="CellLabel" align="left">Moneda</td>
    <td class="CellContent" align="left">&nbsp;
      <input type="text" name="txtGuaranteeCurrency" size="12" style='text-align:left' style="border :0; background-color: #F5F5EB;" readonly value="<%=MiUtil.getString(objCreditEvaluationBean.getnpGuaranteeCurrency())%>">
    </td>
  </tr>
  <tr>
    <td colspan="2">
      <table align=center width="100%" border="0" id="rule_result" name="rule_result" cellpadding="0" cellspacing="1" class="CTable">
        <tr align="center">
          <td class="CellLabel">Condici&oacute;n</td>
          <td class="CellLabel">Comentario</td>
        </tr>
     
      <%
      int iTotal = 0;
      if (arrRuleResult != null ) {
        int ind=0;//arrSite.size();
        for(int i=0; i<arrRuleResult.size();i++) {                                  
           hshRuleResult = (HashMap)arrRuleResult.get(i);             
      %>
        <tr id="site<%=ind+i+1%>">        
          <td class="CellContent"><%=MiUtil.getString((String)hshRuleResult.get("npdescription"))%></td> 
          <td  class="CellContent"><%=MiUtil.getString((String)hshRuleResult.get("npcomments"))%></td>
        </tr>
      <%}
        iTotal=arrRuleResult.size();
      }
      %>
        <input type="hidden" name="hdnCountSite" value="<%=iTotal%>">             
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