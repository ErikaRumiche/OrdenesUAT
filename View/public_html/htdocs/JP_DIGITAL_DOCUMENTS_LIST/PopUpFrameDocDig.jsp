<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<html>
    <%
        String av_url = (String)request.getParameter("av_url");
    %>
    <head>
        <title>DOCUMENTOS DIGITALES</title>
	    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <frameset rows="99%,1%" frameborder="NO" border="0" framespacing="0">
        <frame name="mainFrame" scrolling="AUTO" noresize src="<%=av_url%>">
        <frame name="bottomFrame" scrolling="NO" noresize src="<%=Constante.PATH_APPORDER_SERVER%>/Bottom.html">
    </frameset>
    <noframes>
        <body bgcolor="#FFFFFF">
        </body>
    </noframes>
</html>