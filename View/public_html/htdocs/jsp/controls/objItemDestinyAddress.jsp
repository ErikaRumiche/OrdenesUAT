<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>

<%
  String nameHtmlItem     = MiUtil.getString((String)request.getParameter("nameObjectHtml"));  
%>
    <select name="<%=nameHtmlItem%>" > 
       <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
     </select>
