<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="pe.com.nextel.bean.*"%>

<%@page import="java.util.Hashtable"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="pe.com.nextel.service.NewOrderService" %>
<%@page import="pe.com.nextel.service.BillingAccountService" %>
<%@page import="pe.com.nextel.util.MiUtil" %>
<%@page import="pe.com.nextel.util.Constante" %>
<%@page import="pe.com.nextel.bean.BillingAccountBean" %>
<%@page import="pe.com.nextel.bean.ItemBean" %>
<%@page import="org.apache.commons.lang.StringUtils" %>
<%@page import="pe.com.nextel.service.CustomerService" %>

    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXNew.js"></script>

<%  
   String strMessage=(request.getParameter("sMensaje")==null?"":request.getParameter("sMensaje"));
   String strParametros=(request.getParameter("sParametro")==null?"":request.getParameter("sParametro"));  
   int iOrigen=(request.getParameter("nOrigen")==null?0:Integer.parseInt(request.getParameter("nOrigen"))); //0:Insercion 1: Edicion  
   String strPage=(request.getParameter("sPage")==null?"SalesData":request.getParameter("sPage")); 
   strParametros=strParametros.replace('¿','?');  
   strParametros=strParametros.replace('|','&'); 
   
   String strSessionId = (MiUtil.getParameterCadenaURL(strParametros,"nhdnSessionId")==null?"":MiUtil.getParameterCadenaURL(strParametros,"nhdnSessionId"));
   String strOrderId = (MiUtil.getParameterCadenaURL(strParametros,"pOrderId")==null?"":MiUtil.getParameterCadenaURL(strParametros,"pOrderId"));
   String strCustomerId = (MiUtil.getParameterCadenaURL(strParametros,"nCustomerId")==null?"":MiUtil.getParameterCadenaURL(strParametros,"nCustomerId"));
   String strSalesDataId = (MiUtil.getParameterCadenaURL(strParametros,"pSalesDataId")==null?"":MiUtil.getParameterCadenaURL(strParametros,"pSalesDataId"));
   String strSpecificationId = (MiUtil.getParameterCadenaURL(strParametros,"nSpecificationId")==null?"":MiUtil.getParameterCadenaURL(strParametros,"nSpecificationId"));
   String strDivisionId = (MiUtil.getParameterCadenaURL(strParametros,"nDivisionId")==null?"":MiUtil.getParameterCadenaURL(strParametros,"nDivisionId"));
   String strLogin = (MiUtil.getParameterCadenaURL(strParametros,"nLogin")==null?"":MiUtil.getParameterCadenaURL(strParametros,"nLogin"));
   String strStatus = (MiUtil.getParameterCadenaURL(strParametros,"nStatus")==null?"":MiUtil.getParameterCadenaURL(strParametros,"nStatus"));
   

   String strSubSiteUrl=request.getContextPath()+ "/DYNAMICSECTION/DYNAMICSECTIONSALESDATA/SalesDataPages/SalesDataNew.jsp?nCustomerId="+strCustomerId+"&nDivisionId="+strDivisionId+"&pSpecificationId="+strSpecificationId+"&hdnSessionId="+strSessionId+"&nOrderId="+strOrderId;   
   String strURLButom=Constante.PATH_APPORDER_SERVER+ "/Bottom.html";
   
   if ("null".equals(strMessage))
      strMessage=null;
   else if (strMessage.startsWith("dbmessage"))
      strMessage=strMessage.substring(9,strMessage.length());  
 
   System.out.println(" -------------------- INICIO ResultSalesData.java  ---------------------- ");
   System.out.println("strParametros-->"+strParametros);   
   System.out.println("strMessage-->"+strMessage);   
   System.out.println("strOrderId-->"+strOrderId);   
   
   System.out.println("iOrigen-->"+iOrigen);      
   System.out.println("strPage-->"+strPage);        
   System.out.println("nCustomerId-> "+strCustomerId);	
   System.out.println("strSalesDataId-> "+strSalesDataId);	
   System.out.println("strLogin-> "+strLogin);
   System.out.println(" -------------------- FIN ResultSalesData.java ---------------------- ");    

   out.println("<script language='JavaScript' src='"+ Constante.PATH_APPORDER_SERVER + "/Resource/library.js'></script>");
   out.println("<script language=\"javascript\"> ");
  
   if (strPage.equals("SalesData")){
      if (strMessage!=null){ // El proceso tuvo errores
         if (iOrigen==0){ //insercion    
            out.println(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");                               
            out.println(" parent.bottomFrame.location.replace('"+strURLButom+"'); ");  
         }else if (iOrigen==1){
            out.println(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");                               
            out.println(" parent.bottomFrame.location.replace('"+strURLButom+"'); ");  
         }else if (iOrigen==2){       
            out.println(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");                               
            out.println(" parent.bottomFrame.location.replace('"+strURLButom+"'); ");  
         }
      }else{
         if (iOrigen==0){ //insercion
            out.println("alert('Se guardó correctamente la Aplicación '); ");
            ItemBean objItemBean=(ItemBean)request.getAttribute("objItemBean");
            
            String strProductName=MiUtil.getString(objItemBean.getNpproductname());
            String strSolutionName=MiUtil.getString(objItemBean.getNpproductlinename());
            
            if ( (strStatus.equals("u"))|| (strStatus.equals("s"))) strStatus = Constante.SITE_STATUS_NUEVO;
            out.println("parent.opener.fxAddSalesData('"+strSalesDataId+"','"+strProductName+"','"+strSolutionName+"','"+strStatus+"','"+strLogin+"');");
            out.println("parent.mainFrame.location.replace('"+strSubSiteUrl+"');");
            out.println("location.replace('"+strURLButom+"');");             
         }else if (iOrigen==1){ //edición
            out.println("alert('La aplicación se actualizó correctamente '); ");
            ItemBean objItemBean=(ItemBean)request.getAttribute("objItemBean");
            
            String strProductName=MiUtil.getString(objItemBean.getNpproductname());
            String strSolutionName=MiUtil.getString(objItemBean.getNpproductlinename());
            if ( (strStatus.equals("u"))|| (strStatus.equals("s"))) strStatus = Constante.SITE_STATUS_NUEVO;

            out.println("parent.opener.fxUpdSalesData('"+strSalesDataId+"','"+strProductName+"','"+strSolutionName+"','"+strStatus+"','"+strLogin+"');");
            out.println("parent.mainFrame.location.replace('"+strSubSiteUrl+"');");
            out.println("location.replace('"+strURLButom+"');");             
         }else if (iOrigen==2){ // borrado
            int iInd=(request.getParameter("nInd")==null?0:Integer.parseInt(request.getParameter("nInd")));            
            int iCount=(request.getParameter("nCount")==null?0:Integer.parseInt(request.getParameter("nCount")));            
            System.out.println("iInd-->"+iInd+" iCount"+iCount);        
            out.println("var table = parent.mainFrame.document.all ? parent.mainFrame.document.all['tabSalesData']:parent.mainFrame.document.getElementById('tabSalesData');");                   
            out.println("table.deleteRow("+iInd+"); ");
            out.println("parent.mainFrame.fxRenumeric('tabSalesData',"+(iCount-1)+");");
            out.println("parent.mainFrame.fxDicreseContadorData();");
            
            out.println("location.replace('"+strURLButom+"');"); 
         }     
      }
   }
   
   	out.println("</script>"); 
  
%>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>ResultSalesData</title>
  </head>
  <body></body>
</html>