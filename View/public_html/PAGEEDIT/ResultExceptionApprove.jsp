<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="java.util.*"%>

<%
try{
  System.out.println("=========ResultExceptionApprove.jsp==========");
  Object obj=request.getAttribute("objResultado");
  HashMap hshResult=null; 
  
   if (obj!=null)  
      hshResult=(HashMap)request.getAttribute("objResultado");
   else 
      hshResult=new HashMap();
      
  String strOrderId=MiUtil.getString((String)hshResult.get("strOrderId"));
  String strStatus = MiUtil.getString((String)hshResult.get("strStatus"));
  String strMensajeXML = MiUtil.getString((String)hshResult.get("strXMLMessage")); 
  System.out.println("strStatus==="+strStatus);
  System.out.println("strMensajeXML==="+strMensajeXML);
      
  String strInboxId=MiUtil.getString((String)hshResult.get("strInboxId"));
  String strInboxName=MiUtil.getString((String)hshResult.get("strInboxName")); 
  String strUrl="";    
  String strRutaContext= request.getContextPath();    
  StringBuffer strOutHTML=new StringBuffer();
  String strExceptionStatus = "";
  
  if ( MiUtil.parseInt(strStatus) ==0)
    strExceptionStatus = "La excepción fue rechazada";
  else if (MiUtil.parseInt(strStatus) ==1)
    strExceptionStatus = "La excepción fue aprobada";
  else if (MiUtil.parseInt(strStatus) ==2 || MiUtil.parseInt(strStatus) ==3)
    strExceptionStatus = "La excepción fue aprobada";  //Quedan excepciones por aprobar
  else
    strExceptionStatus = "Se produjo un error durante el proceso";
    
  strExceptionStatus = strExceptionStatus + '\n' + strMensajeXML;    
  strOutHTML.append("alert('"+ MiUtil.getMessageClean(strExceptionStatus) + "'); \n ");
  if (MiUtil.parseInt(strStatus) != -1){
    strUrl="/portal/page/portal/nextel/INBOX_LIST?InboxID="+strInboxId+"&InboxName="+strInboxName;
    strOutHTML.append("parent.mainFrame.location.replace('"+strUrl+"');");
    strOutHTML.append("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');");    
  }
  out.println("<script>");    
  out.print(strOutHTML.toString());
  out.println("</script>");
        
}catch(Exception  ex){  
   ex.printStackTrace();
   System.out.println("Error try catch-->"+ex.getMessage());
%>
<script>
   alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>ResultExceptionApprove</title>
  </head>
  <body>
  </body>
</html>
