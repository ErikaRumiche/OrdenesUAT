<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.Hashtable"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="pe.com.nextel.service.*"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<% 

Hashtable hshtinputNewSection = null;
String    type_window =  MiUtil.getString((String)request.getAttribute("type_window"));
System.out.println("DubockProduct : " + type_window);
  String    strCodigoCliente= "",
            strnpSite ="",
            strCodBSCS = "",
            hdnSpecification = "",
            strDivisionId ="",
            strSessionId = "";
            
  hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
  
  if ( hshtinputNewSection != null ) {
    strCodigoCliente        =   (String)hshtinputNewSection.get("strCodigoCliente");
    strnpSite               =   (String)hshtinputNewSection.get("strnpSite");
    strCodBSCS              =   (String)hshtinputNewSection.get("strCodBSCS");
    hdnSpecification        =   (String)hshtinputNewSection.get("hdnSpecification");
    strDivisionId           =   (String)hshtinputNewSection.get("strDivision"); 
    strSessionId            =   (String)hshtinputNewSection.get("strSessionId");
  }

%>
<script>
  function fxShowProductList(codProducto, descProducto){
    formCurrent = parent.mainFrame.frmdatos;
    if(formCurrent.cmb_ItemModality.value == ""){
      alert("Debe seleccionar antes una Modalidad de Salida");
      formCurrent.cmb_ItemModality.focus();
      return;
    }
    var modalidad = formCurrent.cmb_ItemModality.value;
    if(formCurrent.cmb_ItemProductLine.value == ""){
      alert("Debe seleccionar antes una Linea de Producto");
      formCurrent.cmb_ItemProductLine.focus();
      return;    
    }
    var lineaProduct = formCurrent.cmb_ItemProductLine.value;
    var url = "<%=request.getContextPath()%>/itemServlet?hdnMethod=getLoadProductoWhitFilter&itemModalidad="+modalidad+"&lineProduct="+lineaProduct;
    url+="&productId="+codProducto +"&specificationId="+<%=hdnSpecification%>+"&productDesc="+descProducto;
	  parent.bottomFrame.location.replace(url);    
  }
</script>
<tr>
  <td class="CellLabel" align="left" valing="top">
    <font color="red">&nbsp;*&nbsp;</font>Filtro Producto
  </td>
  <td class="CellContent" valing="top">
  <table>
  <td class="CellContent"><font style=font-weight:bold>&nbsp;Código:</font></td>
  <td class="CellContent"><input type="text" name="txtCodProduct" size="10" maxlength="30"></td>
  <td class="CellContent"><font style=font-weight:bold>&nbsp;Descripción:</font></td>
  <td class="CellContent"><input type="text" name="txtDescProduct" size="20" maxlength="30">
     <a href="javascript:fxShowProductList(document.frmdatos.txtCodProduct.value,document.frmdatos.txtDescProduct.value);">
     <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" width="15" height="15" border="0"></a>
     <input type="hidden" name="txtItemFilterProduct">
  </td> 
  </table>
  </td>
</tr>
