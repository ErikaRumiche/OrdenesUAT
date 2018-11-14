<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="pe.com.nextel.bean.DominioBean"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
    <script type="text/javascript">
    
//getAccesoryModels    
      DeleteSelectOptions(parent.mainFrame.document.frmdatos.cmbModeloAccesorio);      
       
      <%
         HashMap hshDataMap = (HashMap) request.getAttribute(Constante.DATA_STRUCT);
         String strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
      
         if(StringUtils.isBlank(strMessage)) {		      
            ArrayList uListadoModelos = (ArrayList) hshDataMap.get("arrAccesoryModels");
            Iterator iteratorAux = uListadoModelos.iterator();
			
            while(iteratorAux.hasNext()) {
            DominioBean dominio = new DominioBean();
            dominio = (DominioBean) iteratorAux.next();
        
      %>
        fxAddNewOption(parent.mainFrame.document.frmdatos.cmbModeloAccesorio ,'<%=(String) dominio.getValor()%>','<%=(String) dominio.getDescripcion() %>'); 
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
</html>    