<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="pe.com.nextel.bean.CreditEvaluationBean" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%  
  CreditEvaluationBean objCreditEvaluationBean   = (CreditEvaluationBean)request.getAttribute("objCreditEvaluationBean");
  ArrayList arrRuleResult = (ArrayList)request.getAttribute("arrRuleResult");
  String strFlagValidateCredit = (String)request.getAttribute("strFlagValidateCredit");
  HashMap hshRuleResult = null;
  boolean displayCredit = false;
  if (arrRuleResult != null ){
    System.out.println("arrRuleResult.size() "+arrRuleResult.size());
    System.out.println("objCreditEvaluationBean.getnpGuarantee() "+objCreditEvaluationBean.getnpGuarantee());    
    if (objCreditEvaluationBean.getnpGuarantee()>0 || arrRuleResult.size()>0)
      displayCredit = true;
    else
      displayCredit = false;
  }
    %>
      <script language="javascript" defer>
        fxShowCreditEvaluation(<%=displayCredit%>);
      </script>
    <%
  if (arrRuleResult != null ){
    for (int i =0; i<arrRuleResult.size();i++) {
      hshRuleResult = (HashMap)arrRuleResult.get(i);
    %>
      <script defer>
        fxSetRuleResult('<%=MiUtil.getString((String)hshRuleResult.get("npdescription"))%>','<%=MiUtil.getString((String)hshRuleResult.get("npcomments"))%>');
      </script>
    <%
    }
  }
  %>
	<script language="javascript">
    //onload = fxShowCreditEvaluation;
    
    function fxShowCreditEvaluation(display){      
      if(display){
        parent.mainFrame.frmdatos.flagEvaluationCredit.value = '1';
        parent.mainFrame.frmdatos.flagActionCredit.value = '';
        parent.mainFrame.Section_EvaluacionAutomatica.style.display = 'block';
      
        for (z=parent.mainFrame.rule_result.rows.length;z>1;z--){ 
          parent.mainFrame.rule_result.deleteRow(z-1);
        }
        
        fxSetGuarantee();
      }else{
        parent.mainFrame.frmdatos.flagActionCredit.value = 'ACEPTAR';
        parent.mainFrame.frmdatos.flagEvaluationCredit.value = '1';
        parent.mainFrame.Section_EvaluacionAutomatica.style.display = 'none';
        parent.mainFrame.fxValidateCreditEvaluation();
      }
    }
    
    function fxSetGuarantee(){   
      var form      = parent.mainFrame.frmdatos;      
      form.txtGuarantee.value = '<%=objCreditEvaluationBean.getnpGuarantee()%>';
      form.txtGuaranteeCurrency.value = '<%=objCreditEvaluationBean.getnpGuaranteeCurrency()%>';
      form.hdnValidateCredit.value = '<%=strFlagValidateCredit%>';
      parent.mainFrame.fxValidateCreditEvaluation();
    }
    
    function fxSetRuleResult(strCondicion, strComentario){        
      row = parent.mainFrame.rule_result.insertRow(-1);
      
      col = row.insertCell(-1);
      col.className = "CellContent";
      col.align     = "left";
      col.innerHTML = strCondicion;
      
      col = row.insertCell(-1);
      col.className = "CellContent";
      col.align     = "left";
      col.innerHTML = strComentario;
     }
  </script>