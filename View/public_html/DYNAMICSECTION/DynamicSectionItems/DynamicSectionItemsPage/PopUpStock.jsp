<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.*"%>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>

<%
try{
    String strModel = request.getParameter("strModel")==null?"":request.getParameter("strModel");
    System.out.println("Modelo: " + strModel);
%>

  <head>
    <title>Tiendas con Stock - <%=strModel%></title>
    <link type="text/css" rel="stylesheet"
          href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"/>
    <style type="CDATA">
        .show   { display:inline}
        .hidden { display:none }
    </style>
    <script>
        function fx_cerrar(){
            parent.close();
        }
    </script>
  </head>

  <body>
  <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
       <tr class="PortletHeaderColor">
          <td class="LeftCurve" valign="top" align="left" width="10%">&nbsp;&nbsp;</td>
          <td class="PortletHeaderColor" align="center" valign="top">
            <font class="PortletHeaderText">Tiendas con Stock - <%=strModel%></font>
          </td>
          <td align="right" class="RightCurve" width="10%" >&nbsp;&nbsp;</td>
       </tr>
    </table>
  <table cellspacing="0" cellpadding="0" border="0" align="center"
               width="100%">
    <tr>
        <th class="CellLabel" width="30%" bgcolor="#ffffff">
          Tienda
        </th>
        <th class="CellLabel" width="30%" bgcolor="#ffffff">
          Segmento
        </th>
        <th class="CellLabel" width="20%" bgcolor="#ffffff">
          C&oacute;digo
        </th>
        <th class="CellLabel" width="20%" bgcolor="#ffffff">
          Cantidad
        </th>
    </tr>
    
<%
    if(strModel != null){
        //llamada a las clases
        GeneralService objGeneralService = new GeneralService();
        HashMap objHashMap = objGeneralService.getStockModel(strModel);
        ArrayList arrStockModelList = (ArrayList) objHashMap.get("arrStockModel");
        String strMessage = (String) objHashMap.get(Constante.MESSAGE_OUTPUT);
	
        if(strMessage!=null) {
          throw new Exception(strMessage);
        }
        else{
            System.out.println("Tamaño del ArrayList: " + arrStockModelList.size());
            
            if(arrStockModelList != null && arrStockModelList.size() > 0) {
                int size = 0;
                size = arrStockModelList.size();
                for(int i = 0; i < size; i++){
                    HashMap obj_Almacen = new HashMap();
                    //BigDecimal obj_decimal = new BigDecimal(5,0);
                    obj_Almacen = (HashMap)arrStockModelList.get(i);
                    String flex = (String)obj_Almacen.get("flex_value");
                    String name = (String)obj_Almacen.get("name");
                    String codigo=(String)obj_Almacen.get("codigo");
                    String stock= (String)obj_Almacen.get("stock");
                    
                    System.out.println("Stock : " + stock);
                    int modulo = i + 1;
                    if(modulo%2==0){
%>
      <tr>
        <td class="CellLabelContent" width="30%"><font face="ms sans serif"><%= flex %></font></td>
        <td class="CellLabelContent" width="30%"><font face="ms sans serif"><%= name %></font></td>
        <td class="CellLabelContent" width="20%"><font face="ms sans serif"><%= codigo %></font></td>
        <td class="CellLabelContent" width="20%"><font face="ms sans serif"><%= stock %></font></td>
      </tr>
<%
                    }else{
%>
      <tr>
        <td class="CellContent" width="30%"><font face="ms sans serif"><%= flex %></font></td>
        <td class="CellContent" width="30%"><font face="ms sans serif"><%= name %></font></td>
        <td class="CellContent" width="20%"><font face="ms sans serif"><%= codigo %></font></td>
        <td class="CellContent" width="20%"><font face="ms sans serif"><%= stock %></font></td>
      </tr>
<%
                    }
                }
            }
            else 
            {
%>
            <tr>
              <td class="CellContent" align="CENTER" colspan="4"><font color="red">No existe stock para el modelo <%=strModel%>.</font></td>
            </tr>
<%
            }
        }
    }
%>
    </table>
    <div align="center">
        <p>
          <input type="button" name="btnAceptar" value=" Aceptar "
                 onclick="javascript:fx_cerrar();"/>
        </p>
    </div>
    </body>
<%
} 
catch(Exception e) {
	e.printStackTrace();
	out.println("<script>alert('" + MiUtil.getMessageClean(e.getMessage()) + "');</script>");
}%>
</html>