<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%
    Map map = request.getParameterMap();
    String params = "&";
    for (Iterator iterator = map.keySet().iterator(); iterator.hasNext(); ) {
        Object key = iterator.next();
        String keyStr = (String) key;
        String[] value = (String[]) map.get(keyStr);
        if( ! (((String)key!=null)?(String)key:"").equals("av_url") )
            params = params + (String)key +"="+ ((value != null)?value[0]:null) + "&";
    }

    System.out.println("params" + params);

    String av_url;
    av_url  = (String)request.getParameter("av_url");

    av_url = av_url + params;
    System.out.println("FRAME - av_url: "+av_url);
    try{
%>
<html>
<head>
    <title>VERIFICACI&Oacute;N BIOM&Eacute;TRICA / NO BIOM&Eacute;TRICA</title>
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
<% }catch(Exception ex){
    ex.printStackTrace();
    System.out.println("PopUpVerificacion: " + ex.getMessage());
}
%>
