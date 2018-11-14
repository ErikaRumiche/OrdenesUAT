<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="pe.com.nextel.bean.ProductBean"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
		<script type="text/javascript">  
    <%
    ProductBean objProductBean = new ProductBean();
    ArrayList listProduct = (ArrayList)request.getAttribute("listProduct");
    System.out.println("Size de la Lista : " + listProduct.size());
    if(listProduct.size() > 0){
       %>
       parent.mainFrame.DeleteSelectOptions(parent.mainFrame.frmdatos.cmb_ItemProducto);
       parent.mainFrame.vctProduct.removeElementAll();    
       <%
       for( int i=0; i<listProduct.size();i++ ){
         objProductBean = new ProductBean();
         objProductBean = (ProductBean)listProduct.get(i);
         %>  
           parent.mainFrame.vctProduct.addElement(new parent.mainFrame.fxMakeProduct('<%=objProductBean.getNpproductid()%>','<%=objProductBean.getNpminute()%>','<%=objProductBean.getNpminuteprice()%>'));
           parent.mainFrame.AddNewOption(parent.mainFrame.frmdatos.cmb_ItemProducto,'<%=objProductBean.getNpproductid()%>','<%=MiUtil.getMessageClean(objProductBean.getNpproductname())%>');
         <%      
       }
     }else{
     %>
          alert("No se ha encontrado coincidencias de Productos");
     <%
     }
     %>
     location.replace("<%=Constante.PATH_APPORDER_SERVER%>/Bottom.html");
	</script>
	</head>
</html>