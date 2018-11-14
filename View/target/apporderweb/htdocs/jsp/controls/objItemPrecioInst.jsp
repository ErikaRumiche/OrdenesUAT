<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.bean.TableBean" %>
<%@ page import="pe.com.nextel.bean.SpecificationBean" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.ArrayList" %>
<%
/*Recuperando Parametros de Input*/
  Hashtable hshtinputNewSection = null;
  String    strSpecificationId = "";
  GeneralService objGeneralService =  new GeneralService();
  hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
  
  if ( hshtinputNewSection != null ) {
    strSpecificationId      =   (String)hshtinputNewSection.get("hdnSpecification");
  }
  
  HashMap   objHashMapList = null;
  ArrayList arrLista = null;
  TableBean objTableBean = null;
  NewOrderService  objNewOrderService = new NewOrderService();
  String    nameHtmlItem  = "",
            nameNpTable   = "",
            nameLabelItem = "",
            nameValueTable = "",
            nameEstado     = "";
  
  objHashMapList    =     objGeneralService.getSpecificationDetail(MiUtil.parseLong(strSpecificationId));
  nameHtmlItem      =     MiUtil.getString((String)request.getParameter("nameObjectHtml"));
  
  if( objHashMapList != null )
    if( (String)objHashMapList.get("strMessage")!=null )
        throw new Exception(MiUtil.getMessageClean((String)objHashMapList.get("strMessage")));
  SpecificationBean objSpecificationBean;
  objSpecificationBean  = (SpecificationBean)objHashMapList.get("objSpecifBean");
  
  if( !objSpecificationBean.getNpType().equals("TRASLADO") ){
    nameLabelItem = "Cargo Instalación";
  }else{
    nameLabelItem = "Precio Traslado (PEN)";//FES Se cambió US$ por PEN
  
    nameNpTable  = "INSTALL_PRICE";
    nameEstado   = "readonly";     
    
    objHashMapList = objNewOrderService.getTableValue(nameNpTable);
    
    if( objHashMapList != null ){
      if( (String)objHashMapList.get("strMessage")!=null )
        throw new Exception(MiUtil.getMessageClean((String)objHashMapList.get("strMessage")));
    }else
        throw new Exception("Hubieron errores obteniendo datos para " + nameNpTable);
        
    objTableBean    = (TableBean)objHashMapList.get("objTableBean");
    nameValueTable  = objTableBean.getNpValue();
  }
 %>

<tr>
  <td class="CellLabel" align="left" valign="top">
    <font color="red">&nbsp;*&nbsp;</font>&nbsp;<%=nameLabelItem%></td>
    <td align="left" class="CellContent">&nbsp;
      <input type="TEXT"  value="<%=nameValueTable%>"  name="<%=nameHtmlItem%>"  <%=nameEstado%>>
    </td>
           
</tr>    
<!--txt_ItemPriceTransfer-->

   