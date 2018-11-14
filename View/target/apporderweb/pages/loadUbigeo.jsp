<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="pe.com.nextel.bean.UbigeoBean"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
    <script type="text/javascript">
		DeleteSelectOptions(parent.mainFrame.document.frmdatos.cmbDistrito);
    <%  String strJerarquia = (String) request.getAttribute("strJerarquia");
        if(strJerarquia.equals(Constante.JERARQUIA_PROVINCIA)) { %>
            DeleteSelectOptions(parent.mainFrame.document.frmdatos.cmbProvincia);
    <%  }
		HashMap hshDataMap = (HashMap) request.getAttribute(Constante.DATA_STRUCT);
		String strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
		if(StringUtils.isBlank(strMessage)) {
			ArrayList ubigeoList = (ArrayList) hshDataMap.get("arrUbigeoList");
			Iterator iterator = ubigeoList.iterator();
			while(iterator.hasNext()) {
				UbigeoBean ubigeo = (UbigeoBean) iterator.next();
				if(strJerarquia.equals(Constante.JERARQUIA_PROVINCIA)) {
	%>				fxAddNewOption(parent.mainFrame.document.frmdatos.cmbProvincia,'<%=ubigeo.getProvincia()%>','<%=ubigeo.getNombre()%>');
    <%      	}
				if(strJerarquia.equals(Constante.JERARQUIA_DISTRITO)) {
    %>				fxAddNewOption(parent.mainFrame.document.frmdatos.cmbDistrito,'<%=ubigeo.getDistrito()%>','<%=ubigeo.getNombre()%>');
    <%      	}
			}
		} else {
	%>
			alert("Error al cargar el combo: <%=strMessage%>");
	<%
		}
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