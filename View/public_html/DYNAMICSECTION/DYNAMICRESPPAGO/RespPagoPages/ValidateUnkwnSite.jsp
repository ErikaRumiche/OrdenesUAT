<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.bean.*"%>
<%@ page import="pe.com.nextel.service.*"%>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="java.util.*"%>
<%    
   SiteService objSiteS=new SiteService();        
   String strMsgValidacion=null;   
   String strUrlBottom=Constante.PATH_APPORDER_SERVER+"/Bottom.html";
   long lCustomerId=(request.getParameter("nCustomerId")==null?0:Long.parseLong(request.getParameter("nCustomerId")));
   long lUnKnowSiteId=(request.getParameter("nUnknwnsiteid")==null?0:Long.parseLong(request.getParameter("nUnknwnsiteid")));
%>
<html>
<head>
</head> 
<% 
   int iEntro=0;
   strMsgValidacion=objSiteS.doAddrContValidation(lCustomerId,lUnKnowSiteId);
   //NPSL_NEW_GENERAL_PKG.SP_ADDR_CONT_VALIDATION(pn_customerid, pn_unknwnsiteid, wv_message);            
   if (strMsgValidacion!=null){
      strMsgValidacion=strMsgValidacion+"Ingrese los datos necesarios.";                          
%>   
   <script defer="defer">
      alert("<%=MiUtil.getMessageClean(strMsgValidacion)%>");            
   </script>   
<%}else{%>
   <script>       
       parent.close();
   </script>        
<%}
%>        

   <script>       
       parent.bottomFrame.location.replace("<%=strUrlBottom%>");               
   </script>     
</html>
 
