<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Upload de Archivos</title>
  <link rel="stylesheet" href="websales/Resource/salesweb.css">
  <script LANGUAGE="JavaScript" src="websales/Resource/library.js"></script>
  <script language="JavaScript">
    function fxAceptar() {
      window.parent.opener.document.frmdatos.v_saveOption.value = 1;
      window.parent.opener.document.frmdatos.hdnFlagMassive.value = "S";
      window.parent.opener.fxDoSubmitMassive('updateOrden');
      window.parent.close();
    }
  </script>
</head>
<body>
<%
  String hdnOrderId = request.getParameter("hdnOrderId");
  String cmbLugarAtencion = request.getParameter("cmbLugarAtencion");
%>
<form name="frmdatos" action="<%=request.getContextPath()%>/uploadServlet?hdnMethod=uploadFile&hdnOrderId=<%=hdnOrderId%>&cmbLugarAtencion=<%=cmbLugarAtencion%>" method="POST" target="validateMassiveFrame" enctype="multipart/form-data">
  <input type="file" name="fileImeiSim" size="50" style="font-family: Tahoma, Verdana, Arial;"/>
  <input type="submit" value="Cargar" />
  <p></p>
  <iframe name="validateMassiveFrame" width="100%" height="320" frameborder="1" src="../../../pages/buildTableImeiSim.jsp" scrolling="yes"></iframe>
  <p></p>
  <table align="center">
    <tr>
      <td>
        <input type="button" name="btnAceptar" value="Aceptar" disabled="disabled" onclick="fxAceptar()"/>
        <input type="button" name="btnCancelar" value="Cancelar" onclick="parent.window.close();"/>
      </td>
    </tr>
  </table>  
</form>
</body>
</html>