<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.ArrayList" %>
<%

/*Recuperando Parametros de Input*/
  Hashtable hshtinputNewSection = null;
  HashMap   objHashMapList = null;
  ArrayList arrLista = null;
  GeneralService  objGeneralService = new GeneralService();
  String    strCustomerId= "",
            strSiteId ="",
            strCodBSCS = "",
            strSpecificationId = "",
            strDivisionId = "",
            nameHtmlItem  = "",
            nameNpTable   = "",
            objReadOnly   = "",
            objValidate   = "";
            
  hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
  
  nameHtmlItem = MiUtil.getString((String)request.getParameter("nameObjectHtml"));
  nameNpTable  = MiUtil.getString((String)request.getParameter("tablename"));
  objReadOnly  = MiUtil.getString((String)request.getParameter("strObjectReadOnly"));
  objValidate  = MiUtil.getString((String)request.getParameter("strObjectValidate")); 
  
  if( objReadOnly.equals("S") ) objReadOnly = "disabled"; else objReadOnly = "";
  if( objValidate.equals("S") ) objValidate = "*"; else objValidate = "";

%>

  <td class="CellLabel" align="left">&nbsp;<font color="red"><%=objValidate%>&nbsp;&nbsp;</font>Descripción&nbsp;</td>
  <td class="CellContent" >&nbsp;
  <textarea name="txt_ItemDescription" cols="50" rows="4" <%=objReadOnly%> ></textarea>
  </td>
