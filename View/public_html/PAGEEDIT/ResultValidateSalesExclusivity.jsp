<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="java.util.*"%>

<%
try{
  System.out.println("=========ResultValidateSalesExclusivity.jsp==========");  
  HashMap hshResult=null;   
  hshResult=(HashMap)request.getAttribute("objHashMap");
   
  String strSalesmanId  = MiUtil.getString((String)hshResult.get("strSalesmanId"));
  String strCompanyId   = MiUtil.getString((String)hshResult.get("strCompanyId"));
  String strSiteId      = MiUtil.getString((String)hshResult.get("strSiteId")); 
  String strExclusivity = MiUtil.getString((String)hshResult.get("strExclusivity"));       
  String strMessage     = MiUtil.getString((String)hshResult.get("strMessage"));
  StringBuffer  strOutHTML=new StringBuffer();      
  System.out.println("strExclusivity==="+strExclusivity);  
  System.out.println("strMessage==="+strMessage);  
  if ( strExclusivity.equalsIgnoreCase("N") ){
    //No puede seleccionar otro vendedor data  
    strOutHTML.append("alert('"+ MiUtil.getMessageClean(strMessage) + "'); \n ");
    strOutHTML.append("parent.mainFrame.document.frmdatos.cmbVendedorData.value = parent.mainFrame.document.frmdatos.hdnVendedorDataId.value; \n");     
    strOutHTML.append("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');"); 
    System.out.println("strOutHTML==="+strOutHTML);
    out.println("<script>");    
    out.print(strOutHTML.toString());
    out.println("</script>");
  }else if  ( strExclusivity.equalsIgnoreCase("X") && strMessage!= null  ){
    //Error
    throw new Exception(MiUtil.getMessageClean(strMessage));
  }else if (strExclusivity.equalsIgnoreCase("S")){
    strOutHTML.append("parent.mainFrame.document.frmdatos.hdnVendedorDataId.value ="+strSalesmanId+"; \n");    
    strOutHTML.append("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');");
    out.println("<script>"); 
    out.print(strOutHTML.toString());
    out.println("</script>");
  }
  System.out.println("========= Fin ResultValidateSalesExclusivity.jsp==========");        
}catch(Exception  ex){  
   ex.printStackTrace();
   System.out.println("Error try catch-->"+ex.getMessage());
  %>
  <script>
     alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
     location.replace("<%=Constante.PATH_APPORDER_SERVER%>/Bottom.html");
  </script>
  <%
}
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>ResultValidateSalesExclusivity</title>
  </head>
  <body>
  </body>
</html>
