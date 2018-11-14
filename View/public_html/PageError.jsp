<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" import="java.io.CharArrayWriter, java.io.PrintWriter"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Test Compania</title>
    <link type="text/css" rel="stylesheet"
          href="websales/Resource/salesweb.css"/>
  </head>
  <body>
  
  <table  border="0" width="100%" cellpadding="2" cellspacing="1" class="RegionBorder">
     
     <tr>
        
        
        <TD NOWRAP class="PortletHeaderColor" width="100%"><font class="PortletHeaderText">
  
  El error ocurrido es : <br/></font></td></tr>
  <tr><td class="RegionHeaderColor" width="100%">
    <%
      if (exception != null) 
      { 
        out.println(exception.getMessage());
        CharArrayWriter charArrayWriter = new CharArrayWriter(); 
        PrintWriter printWriter = new PrintWriter(charArrayWriter, true); 
        exception.printStackTrace(printWriter); 
        out.println(charArrayWriter.toString()); 
      } 
    %>
    </td></tr></table>
    </body>
</html>