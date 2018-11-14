<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%
String nameHtmlItem = (String)request.getParameter("nameObjectHtml");
if ( nameHtmlItem==null ) nameHtmlItem = "";
%>
<select name="<%=nameHtmlItem%>" >
<option value="Adenda">Opción 1</option>
<option>Opción 2</option>
<option>Opción 3</option>
<option>Opción 4</option>
<option>Opción 5</option>
</select>