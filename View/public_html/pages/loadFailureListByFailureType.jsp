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

    DeleteSelectOptions(parent.mainFrame.document.frmdatos.lstFalla);
    
    <%
    System.out.println("loadFailureListByFailureType.jsp");
		HashMap hshDataMap = (HashMap) request.getAttribute(Constante.DATA_STRUCT); //lista de fallas    
    String strListFallasSeleccionadas = (String) request.getAttribute("strListFallasSeleccionadas"); //lista de fallas seleccionadas
    %>
    lista = '<%=(String) strListFallasSeleccionadas%>'
    <%
		String strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
    String strValor = null;
		if(StringUtils.isBlank(strMessage)) {
			ArrayList uLista = (ArrayList) hshDataMap.get("arrListado");
			Iterator iterator = uLista.iterator();
			while(iterator.hasNext()) {
        DominioBean dominio = new DominioBean();
        dominio = (DominioBean) iterator.next();
        %>
          valor = '<%=(String) dominio.getValor()%>';
          pos = lista.indexOf(valor);
          if (pos < 0){
            fxAddNewOption(parent.mainFrame.document.frmdatos.lstFalla,'<%=(String) dominio.getValor()%>','<%=(String) dominio.getDescripcion() %>'); 
          }
        <% 
      }
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