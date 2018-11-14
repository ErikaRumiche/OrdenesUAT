<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.bean.TableBean" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.ArrayList" %>
<%
/*Recuperando Parametros de Input*/
  HashMap   objHashMapList = null;
  ArrayList arrLista = null;
  TableBean objTableBean = null;
  NewOrderService  objNewOrderService = new NewOrderService();
  String    nameHtmlItem  = "",
            nameNpTable   = "";
            
  nameHtmlItem = MiUtil.getString((String)request.getParameter("nameObjectHtml"));
  nameNpTable  = MiUtil.getString((String)request.getParameter("tablename"));
  
  objHashMapList = objNewOrderService.getTableValue(nameNpTable);
  
  if( objHashMapList != null ){
    if( (String)objHashMapList.get("strMessage")!=null )
      throw new Exception(MiUtil.getMessageClean((String)objHashMapList.get("strMessage")));
  }else
      throw new Exception("Hubieron errores obteniendo datos para " + nameNpTable);
      
  objTableBean = (TableBean)objHashMapList.get("objTableBean"); 
 %>

<tr >
  <td class="CellLabel" align="left" valign="top">
    <font color="red">&nbsp;*&nbsp;</font>Precio Traslado&nbsp;</td>
    <td align="left" class="CellContent">&nbsp;
      <input type="TEXT"  value="<%=objTableBean.getNpValue()%>"  name="txt_ItemPriceTransfer"  readonly>
    </td>
           
</tr>    


   