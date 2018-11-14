<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pe.com.nextel.bean.DominioBean" %>
<%
  Hashtable hshtinputNewSection = null;
  NewOrderService objNewOrderService = new NewOrderService();
  String type_window =  (String)request.getAttribute("type_window");
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
  HashMap objHashMap = objNewOrderService.getResponsibleDevList();
  if( objHashMap.get("strMessage")!=null ) throw new Exception((String)objHashMap.get("strMessage"));
  
  ArrayList objArrayList = (ArrayList)objHashMap.get("objArrayList");
  
  DominioBean objDominioBean = null;
  
%>
    <select name="<%=nameHtmlItem%>" > 
       <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
         <% 
            if ( objArrayList != null && objArrayList.size() > 0 ){
               
               for( int i=0; i<objArrayList.size();i++ ){ 
                    objDominioBean = new DominioBean();
                    objDominioBean = (DominioBean)objArrayList.get(i);
         %>
         
          <option value='<%=objDominioBean.getValor()%>'>
                         <%=objDominioBean.getDescripcion()%>
          </option>
          <%
                }
            }
          %>
     </select>
     