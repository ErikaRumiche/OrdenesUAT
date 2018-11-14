<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %> 
 <%@page import="pe.com.nextel.util.Constante"%>
 
   <textarea name="txtItemsClearMassiveTransfer">

  </textarea>
  
  <script>
      parent.mainFrame.idItemsMassiveTransfer.innerHTML = "";
      parent.mainFrame.idItemsMassiveTransfer.innerHTML = txtItemsClearMassiveTransfer.value;
      location.replace("<%=Constante.PATH_APPORDER_SERVER%>/Bottom.html");
  </script>