<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pe.com.nextel.bean.ProductBean" %>
<%
/*
Recuperando Parametros de Input
*/
  
  Hashtable hshtinputNewSection = null;
  String    strCodigoCliente= "",
            strnpSite ="",
            strCodBSCS = "",
            hdnSpecification = "",
            strDivision = "";
            
  hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
  
  if ( hshtinputNewSection != null ) {
    strCodigoCliente        =   (String)hshtinputNewSection.get("strCodigoCliente");
    strnpSite               =   (String)hshtinputNewSection.get("strnpSite");
    strCodBSCS              =   (String)hshtinputNewSection.get("strCodBSCS");
    hdnSpecification        =   (String)hshtinputNewSection.get("hdnSpecification");
    strDivision             =   (String)hshtinputNewSection.get("strDivision");
    request.setAttribute("hshtInputNewSection",hshtinputNewSection);
  }
  
  String nameHtmlItem = (String)request.getParameter("nameObjectHtml");
  if ( nameHtmlItem==null ) nameHtmlItem = "";
  
  String idSpecificationId = (String)request.getAttribute("idSpecificationId");
  String strMessage = "";
  String strErrorLocal = "";
  ProductBean pBean = null;
  ArrayList objArrayList = NewOrderService.ProductDAOgetProductList(pBean,strMessage);
  
  if ( objArrayList != null ){
    strErrorLocal = "La data traida está vacía" ;
  }
  
%>
    <select name="<%=nameHtmlItem%>" > 
       <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
         <% 
            if ( objArrayList != null && objArrayList.size() > 0 ){
               ProductBean productBean = null;           
               for( int i=0; i<objArrayList.size();i++ ){ 
                    productBean = new ProductBean();
                    productBean = (ProductBean)objArrayList.get(i);
                  
                  if ( productBean.getNpstatus().equals("A") ) {
          %>
          
          <option value='<%=productBean.getNpproductid()%>'>
                         <%=productBean.getNpname()%>
          </option>
          <%
                  }
                }
            }
          %>
                         
     