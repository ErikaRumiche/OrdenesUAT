<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%
String nameHtmlItem = (String)request.getParameter("nameObjectHtml");
if ( nameHtmlItem==null ) nameHtmlItem = "";
%>

<tr>
  <td class="CellLabel" align="left" valign="top">
    &nbsp;&nbsp;&nbsp;&nbsp;Locución
  </td>
  <td align="left" class="CellContent">
  &nbsp;
<select name="<%=nameHtmlItem%>" >
<option value="SI">SI</option>
<option value="NO">NO</option>
</select>

   </td>
</tr>
