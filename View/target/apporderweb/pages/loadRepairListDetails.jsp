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
    DeleteSelectOptions(parent.mainFrame.document.frmdatos.lstFallaResultado);
    //DeleteSelectOptions(parent.mainFrame.document.frmdatos.lstRepuestos);
    //DeleteSelectOptions(parent.mainFrame.document.frmdatos.lstRepuestosResultado);

    <%
    System.out.println("loadRepairListDetails.jsp");
		HashMap hshDataMap = (HashMap) request.getAttribute(Constante.DATA_STRUCT); //lista de fallas    
    HashMap hshDataMapResultado = (HashMap) request.getAttribute("hshDataMapAux"); //lista de fallas seleccionadas
    //HashMap hshListaRepuestos = (HashMap) request.getAttribute("hshDataRepuestos"); //lista de repuestos
    String strmostrarRepuestoResultado = (String) request.getAttribute("mostrarRepuestoResultado");
    
    HashMap hshDataRepuestosResultado = (HashMap) request.getAttribute("hshDataRepuestosResultado"); //lista de repuestos seleccionados

		String strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);

		if(StringUtils.isBlank(strMessage)) {

      // Lista de Fallas Seleccionadas
      %>
      var strListFallasSeleccionadas = "";
      <%
      ArrayList uListaAux = (ArrayList) hshDataMapResultado.get("arrListado");
			Iterator iteratorAux = uListaAux.iterator();
			while(iteratorAux.hasNext()) {
        DominioBean dominio = new DominioBean();
        dominio = (DominioBean) iteratorAux.next();
        %>
          fxAddNewOption(parent.mainFrame.document.frmdatos.lstFallaResultado,'<%=(String) dominio.getValor()%>','<%=(String) dominio.getDescripcion() %>'); 
          strListFallasSeleccionadas = strListFallasSeleccionadas+'<%=(String) dominio.getValor()%>'+"|";
          
        <%
      }
      
      // Lista de Fallas 

			ArrayList uLista = (ArrayList) hshDataMap.get("arrListado");
			Iterator iterator = uLista.iterator();
			while(iterator.hasNext()) {
        DominioBean dominio = new DominioBean();
        dominio = (DominioBean) iterator.next();
        
        %>
          valor = '<%=(String) dominio.getValor()%>';
          pos = strListFallasSeleccionadas.indexOf(valor);
          if (pos < 0){
            fxAddNewOption(parent.mainFrame.document.frmdatos.lstFalla,'<%=(String) dominio.getValor()%>','<%=(String) dominio.getDescripcion() %>'); 
          }
        <%
      }

      // Lista de Repuestos
/*
      ArrayList uListaRepuestos = (ArrayList) hshListaRepuestos.get("arrListado");
			Iterator iteratorRepuestos = uListaRepuestos.iterator();
			while(iteratorRepuestos.hasNext()) {
        DominioBean dominio = new DominioBean();
        dominio = (DominioBean) iteratorRepuestos.next();
        %>
        fxAddNewOption(parent.mainFrame.document.frmdatos.lstRepuestos,'<%=(String) dominio.getValor()%>','<%=(String) dominio.getDescripcion() %>'); 
        <%
        
      }
*/
      // Lista de Repuestos Seleccionados

         /*
         ArrayList uListaRepuestosResultado = (ArrayList) hshDataRepuestosResultado.get("arrListado");
            Iterator iteratorRepuestosResultado = uListaRepuestosResultado.iterator();
            while(iteratorRepuestosResultado.hasNext()) {
         DominioBean dominio = new DominioBean();
         dominio = (DominioBean) iteratorRepuestosResultado.next();
         %>
        
        
         fxAddNewOption(parent.mainFrame.document.frmdatos.lstRepuestosResultado,'<%=(String) dominio.getValor()%>','<%=(String) dominio.getDescripcion() %>');
      
         <%
         
         } */
      %>
      
      
      
      
      
      
      
      
        parent.mainFrame.fxGeneraHidden();
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