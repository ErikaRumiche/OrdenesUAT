<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%
String nameHtmlItem = (String)request.getParameter("nameObjectHtml");
if ( nameHtmlItem==null ) nameHtmlItem = "";
%>
<select name="<%=nameHtmlItem%>" >
<option value="Adenda">Opci�n 1</option>
<option>Opci�n 2</option>
<option>Opci�n 3</option>
<option>Opci�n 4</option>
<option>Opci�n 5</option>
</select>