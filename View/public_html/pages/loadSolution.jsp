<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="pe.com.nextel.bean.DominioBean"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%--@ page import="org.apache.commons.lang.ArrayUtils"--%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
    <script type="text/javascript">
    DeleteSelectOptions(parent.mainFrame.document.formdatos.cmbSolucionNegocio);
		DeleteSelectOptions(parent.mainFrame.document.formdatos.cmbCategoria);
		DeleteSelectOptions(parent.mainFrame.document.formdatos.cmbSubCategoria);
        
    <%  HashMap hshDataMap = (HashMap) request.getAttribute(Constante.DATA_STRUCT);
		System.out.println("hshDataMap: "+hshDataMap);
		String strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
		if(StringUtils.isBlank(strMessage)) {
			ArrayList arrSolucionList = (ArrayList) hshDataMap.get("arrSolucionList");
        	Iterator iterator = arrSolucionList.iterator();
        	while(iterator.hasNext()) {
            	DominioBean dominio = (DominioBean) iterator.next();
    %>			fxAddNewOption(parent.mainFrame.document.formdatos.cmbSolucionNegocio,'<%=dominio.getValor()%>','<%=dominio.getDescripcion()%>');
    <%  	}
		} else {
	%>		alert('<%="Hubo error en la carga de la Solución: "+MiUtil.getMessageClean(strMessage)%>');
	<%	}
    %>
    
    function fxAddNewOption(TheCmb, Value, Description) {
        var option = new Option(Description, Value);
        var i = TheCmb.options.length;
        TheCmb.options[i] = option;
    }
    
    </script>
  </head>
  <body bgcolor="#AABBCC">
  </body>
</html>