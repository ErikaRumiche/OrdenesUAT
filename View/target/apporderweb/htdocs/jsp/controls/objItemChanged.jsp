<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pe.com.nextel.bean.EquipmentBean" %>
<%      
  String nameHtmlItem = (String)request.getParameter("nameObjectHtml");
  if ( nameHtmlItem==null ) nameHtmlItem = "";
  
  System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<Combo Con Cambio/Sin Cambio>>>>>>>>>>>>>>>>>>>>>>>>>>>");
%>

<select name="<%=nameHtmlItem%>"> 
   <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
   <option value="Si">Si</option>
   <option value="No">No</option>
</select>