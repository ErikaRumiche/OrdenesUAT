<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pe.com.nextel.bean.EquipmentBean" %>
<%
/*
Recuperando Parametros de Input
*/
  
  Hashtable hshtinputNewSection = null;
  String    strCodigoCliente= "",
            strnpSite ="",
            strCodBSCS = "",
            hdnSpecification = "",
            strDivisionId = "";
            
  hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
  
  if ( hshtinputNewSection != null ) {
    strCodigoCliente        =   (String)hshtinputNewSection.get("strCodigoCliente");
    strnpSite               =   (String)hshtinputNewSection.get("strnpSite");
    strCodBSCS              =   (String)hshtinputNewSection.get("strCodBSCS");
    hdnSpecification        =   (String)hshtinputNewSection.get("hdnSpecification");
    strDivisionId           =   (String)hshtinputNewSection.get("strDivision");
   
    
    request.setAttribute("hshtInputNewSection",hshtinputNewSection);
  }
  
  String nameHtmlItem = (String)request.getParameter("nameObjectHtml");
  if ( nameHtmlItem==null ) nameHtmlItem = "";

  String strMessage = "";
  String strErrorLocal = "";
  String ownhandset = "S";
  String consignmen = "";
  
  consignmen  = NewOrderService.SpecificationDAOgetConsigmentValue(MiUtil.parseInt(hdnSpecification),strMessage);

  ArrayList objArrayList = NewOrderService.EquipmentDAOgetProductList(ownhandset,consignmen ,strMessage);
  
  if ( objArrayList != null ){
    strMessage = "La data traida está vacía" ;
  }
  
%>

<select name="<%=nameHtmlItem%>" > 
       <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
         <% 
            if ( objArrayList != null && objArrayList.size() > 0 ){
               EquipmentBean equipmentBean = null;           
               for( int i=0; i<objArrayList.size();i++ ){ 
                    equipmentBean = new EquipmentBean();
                    equipmentBean = (EquipmentBean)objArrayList.get(i);
                  
                  //if ( productBean.getNpstatus().equals("A") ) {
          %>
          
          <option value='<%=equipmentBean.getNpequipmentid()%>'>
                         <%=equipmentBean.getNpequipmentdesc()%>
          </option>
          <%
                  //}
                }
            }
          %>