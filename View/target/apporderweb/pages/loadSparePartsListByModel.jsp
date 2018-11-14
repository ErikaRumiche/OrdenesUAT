<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="pe.com.nextel.bean.DominioBean"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
    <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
    <script type="text/javascript">

   // DeleteSelectOptions(parent.mainFrame.document.frmdatos.lstRepuestos);

    <%
		HashMap hshDataMap = (HashMap) request.getAttribute(Constante.DATA_STRUCT);
		String strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
    String strListName = (String) request.getAttribute("objName");

    //strListName = "parent.mainFrame.document.frmdatos."+strListName;

    %>

    var control = parent.mainFrame.document.getElementById('<%=strListName%>');
    //DeleteSelectOptions(parent.mainFrame.document.frmdatos.lstFalla);
    //DeleteSelectOptions(control);

    <%
    /*
		if(StringUtils.isBlank(strMessage)) {
			ArrayList uLista = (ArrayList) hshDataMap.get("arrListado");
			Iterator iterator = uLista.iterator();
			while(iterator.hasNext()) {
        DominioBean dominio = new DominioBean();
        dominio = (DominioBean) iterator.next();
        
        %>
        fxAddNewOption(parent.mainFrame.document.frmdatos.lstRepuestos,'<%=(String) dominio.getValor()%>','<%=(String) dominio.getDescripcion() %>'); 
        <%
      }
    }
    */
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