<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="pe.com.nextel.bean.DominioBean"%>

<%  
        String strInfDetail = (String) request.getAttribute("strInfDetail");
        HashMap hshDataMap = (HashMap) request.getAttribute("hshDataMapKit");
        ArrayList arrKitsList = new ArrayList();
        DominioBean dominio = new DominioBean();
        String strDatoV = null;
        String strDatoT = null;
    	
        String strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
        System.out.println("En el loadRetailKitInfo.jsp");		 
        System.out.println("strInfDetail->"+strInfDetail);
%>

<html>
  <head>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsItemsIXEdit.js"></script>
    <script type="text/javascript">
        
		function fxCargaKitPrepago(){
    
       <%  
        arrKitsList = new ArrayList();
        if(StringUtils.isNotBlank(strInfDetail)) {
         	arrKitsList = (ArrayList) hshDataMap.get("arrKitsList");
			
          if(StringUtils.isBlank(strMessage)) {
          
              for(int i=1; i<=Integer.parseInt(strInfDetail); i++) {%>
                parent.mainFrame.document.getElementById("cmbKit"+<%=i%>).disabled = false;                
                <%                   
                 for(int j = 0; j < arrKitsList.size(); j++) {
                  	dominio = (DominioBean) arrKitsList.get(j);
                    strDatoV = dominio.getValor();  
                    strDatoT = dominio.getDescripcion();  %>
            
                    var len = parent.mainFrame.document.getElementById("cmbKit"+<%=i%>).length++;
        
                    parent.mainFrame.document.getElementById("cmbKit"+<%=i%>).options[len].value = "<%=strDatoV%>";
                    parent.mainFrame.document.getElementById("cmbKit"+<%=i%>).options[len].text = "<%=strDatoT%>";
                 <%}%>
                 parent.mainFrame.document.getElementById("cmbKit"+<%=i%>).disabled = true;
                 <%
               }
            }
        }
    %>
    }
		
    </script>
    
    <script "language=javascript">
    
      fxCargaKitPrepago(); 
    </script>
    
  </head>
  <body bgcolor="#AABBCC">
  </body>
</html>