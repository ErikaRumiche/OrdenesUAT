<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="pe.com.nextel.bean.BillingAccountBean"%>
<%@page import="pe.com.nextel.bean.ProductLineBean"%>
<%@page import="pe.com.nextel.service.NewOrderService"%>
<%@page import="pe.com.nextel.util.MiUtil"%>
<%@page import="pe.com.nextel.util.Constante"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.math.BigDecimal"%>

<%
  ArrayList objArrBillAccExtLst   = (ArrayList)request.getAttribute("objArrBillAccExtLst");
  ArrayList objArrBillAccSolcLst  = (ArrayList)request.getAttribute("objArrBillAccSolcLst");
  String    strIndex              = (String)request.getParameter("strIndex");
  String    strPage               = MiUtil.getString((String)request.getParameter("strPage"));
  System.out.print("strPage : " + strPage);  
  String    strNameObject         = MiUtil.getString((String)request.getParameter("strNameObject"));
  
  request.removeAttribute("strIndex");
  request.removeAttribute("objArrBillAccExtLst");
  request.removeAttribute("objArrBillAccSolcLst");
  request.removeAttribute("strPage");
%>

<script>
    <%if(strPage.equals("loadAssigment")){%>
      eval('parent.mainFrame.DeleteSelectOptions(parent.mainFrame.frmdatos.<%=strNameObject%>)');
    <%}else{%>
      parent.mainFrame.DeleteSelectOptions(parent.mainFrame.frmdatos.cmbNewFactura[<%=strIndex%>]);
    <%}%>
  <%
    if( objArrBillAccExtLst != null ){
     BillingAccountBean objBillingAccountBean =  null;
      System.out.println("Site objArrBillAccExtLst : " + objArrBillAccExtLst.size() );
      for(int i=0; i<objArrBillAccExtLst.size(); i++ ) {
          objBillingAccountBean = new BillingAccountBean();
          objBillingAccountBean = (BillingAccountBean)objArrBillAccExtLst.get(i);
  %>
      <%if(strPage.equals("loadAssigment")){%>
        eval("parent.mainFrame.AddNewOption(parent.mainFrame.frmdatos.<%=strNameObject%>,'<%=objBillingAccountBean.getNpBillaccountNewId()%>','Existente - <%=MiUtil.getString(objBillingAccountBean.getNpBillacCName())%>')");
      <%}else{%>
        parent.mainFrame.AddNewOption(parent.mainFrame.frmdatos.cmbNewFactura[<%=strIndex%>],"<%=objBillingAccountBean.getNpBillaccountNewId()%>","Existente - <%=MiUtil.getString(objBillingAccountBean.getNpBillacCName())%>");
      <%}%>
    <%}%>
  <%}%>
  
  <%
    if( objArrBillAccSolcLst != null ){
     BillingAccountBean objBillingAccountBean =  null;
      System.out.println("Site objArrBillAccSolcLst : " + objArrBillAccSolcLst.size() );
      for(int i=0; i<objArrBillAccSolcLst.size(); i++ ) {
          objBillingAccountBean = new BillingAccountBean();
          objBillingAccountBean = (BillingAccountBean)objArrBillAccSolcLst.get(i);
  %>
      <%if(strPage.equals("loadAssigment")){%>
        eval("parent.mainFrame.AddNewOption(parent.mainFrame.frmdatos.<%=strNameObject%>,'<%=objBillingAccountBean.getNpBillaccountNewId()%>','Solicitado - <%=MiUtil.getString(objBillingAccountBean.getNpBillacCName())%>')");
      <%}else{%>
        parent.mainFrame.AddNewOption(parent.mainFrame.frmdatos.cmbNewFactura[<%=strIndex%>],"<%=objBillingAccountBean.getNpBillaccountNewId()%>","Solicitado - <%=MiUtil.getString(objBillingAccountBean.getNpBillacCName())%>");
      <%}%>
    <%}%>
  <%}%>
  
  
    location.replace('<%=Constante.PATH_APPORDER_SERVER%>/Bottom.html');
</script>

