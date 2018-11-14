<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="pe.com.nextel.util.*" %>

<%
  HashMap hRespPagoList      = (HashMap)request.getAttribute("hRespPagoList"); 
  ArrayList objRespPagoList = new ArrayList();
  if (hRespPagoList!=null){
   objRespPagoList =(ArrayList)hRespPagoList.get("objRespPagoList");  
  }
%>

    <script>

    function fxLoadRespPago(){      
      formCurrent = parent.mainFrame.frmdatos;
      parent.mainFrame.DeleteSelectOptions(formCurrent.cmb_ItemRespPago);      
      <%  
        if ( objRespPagoList != null && objRespPagoList.size() >0){            
            HashMap hsMapLista = new HashMap(); 
            String strSiteId  =  "";
            String strSiteName  = ""; 
            for (int i=0; i<objRespPagoList.size(); i++){
               hsMapLista = (HashMap)objRespPagoList.get(i);
               strSiteName = (String)hsMapLista.get("wv_swsitename");
               strSiteId = (String)hsMapLista.get("wn_swsiteid");                       
              %> 
               parent.mainFrame.AddNewOption(formCurrent.cmb_ItemRespPago, "<%=strSiteId%>", "<%=strSiteName%>");              
              <%            
            }
      }
      %>
    }
    </script>
    <script defer>    
    <%if ( objRespPagoList != null && objRespPagoList.size() > 0 ){%>       
      fxLoadRespPago();       
   <%}%>   
    </script>
