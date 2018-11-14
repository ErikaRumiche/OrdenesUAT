<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="pe.com.nextel.bean.ItemBean"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="pe.com.nextel.util.Constante"%>

<html>
  <head>
    <script type="text/javascript">
 <%  
   HashMap hValidatePhone = (HashMap)request.getAttribute("hValidatePhone");
   long lngSpecificationId   = MiUtil.parseLong((String)hValidatePhone.get("strSpecificationId"));   
   String strControlName     = (String)hValidatePhone.get("strControlName");   
   String strPhoneStatus     = (String)hValidatePhone.get("strPhoneStatus");
   String   strMessage       = (String)hValidatePhone.get("strMessage");
   if (strMessage!=null){%>
      alert('<%=MiUtil.getMessageClean(strMessage)%>');      
      try{
        parent.mainFrame.frmdatos.<%=strControlName%>.focus();
      }catch(e){}
 <%}%>
    location.replace('<%=Constante.PATH_APPORDER_SERVER%>+"/Bottom.html')
    </script>
  </head>
</html>

   
  
      