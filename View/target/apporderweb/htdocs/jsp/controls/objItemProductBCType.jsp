<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pe.com.nextel.bean.EquipmentBean" %>
<%@ page import="pe.com.nextel.util.Constante" %>

<%
    Hashtable hshtinputNewSection = null;
    String    hdnSpecification = "";
    hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
    int intSpecificationId       = Integer.parseInt(request.getParameter("strSpecificationId"));//ADT-BCL-083
    if ( hshtinputNewSection != null ) {
        hdnSpecification        =   (String)hshtinputNewSection.get("hdnSpecification");
    }
    String nameHtmlItem = (String)request.getParameter("nameObjectHtml");
    if ( nameHtmlItem==null ) nameHtmlItem = "";
    String    type_window =  MiUtil.getString((String)request.getAttribute("type_window"));
    String strObjectReadOnly = request.getParameter("strObjectReadOnly");
  /*
  String nameHtmlItem = (String)request.getParameter("nameObjectHtml");
  if ( nameHtmlItem==null ) nameHtmlItem = "";

  Hashtable hshtinputNewSection = (Hashtable)request.getAttribute("hshtInputNewSection");
  String hdnSpecification = (String)hshtinputNewSection.get("hdnSpecification");
  boolean isOrderReposicion = hdnSpecification.equals(String.valueOf(Constante.SPEC_REPOSICION));

  System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<Combo Tipo de almac?n>>>>>>>>>>>>>>>>>>>>>>>>>>>"); */
%>

<%
if("S".equals(strObjectReadOnly)){
%>
<select disabled="disabled" name="<%=nameHtmlItem%>">
    <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
    <option value="1" selected="selected">Recurrente</option>
    <option value="2">No recurrente</option>
<%}else{%>
<select name="<%=nameHtmlItem%>" onchange="fxChangeTypes(this.value);">
    <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
    <option value="1">Recurrente</option>
    <option value="2">No recurrente</option>
<%}%>
</select>


<script language="javascript">

    function fxChangeTypes(object){
        var form = parent.mainFrame.frmdatos;
        var solution = form.cmb_ItemSolution[form.cmb_ItemSolution.selectedIndex].value;
        var modality = form.cmb_ItemModality.value;
        var origPlanId = form.hdnOriginalPlanId.value;
        var productLine = form.cmb_ItemProductLine.value;
        var typeproduct = object;
        var url = "<%=request.getContextPath()%>/itemServlet?strModality="+modality+"&strProductLineId="+productLine+"&strSpecificationId=<%=hdnSpecification%>&strSolutionId="+solution+"&strPlanId="+origPlanId+"&strTypeProduct="+typeproduct+"&hdnMethod=getProducTypeList";
        parent.bottomFrame.location.replace(url);
    }

</script>

    <script language="javascript">
        function fxLoadEditProductBCType(){
            <%if( !type_window.equals("NEW") && Integer.parseInt(hdnSpecification) == 2082){
            String[] paramNpobjitemheaderid = request.getParameterValues("a");
            String[] paramNpobjitemvalue    = request.getParameterValues("b");
             String strProductType          = "";
             for(int i=0;i<paramNpobjitemheaderid.length; i++){
                if( paramNpobjitemheaderid[i].equals("146") ) {
                    strProductType = MiUtil.getString(paramNpobjitemvalue[i]);
            }
        }
            %>
            parent.mainFrame.frmdatos.cmb_itemType.value = <%=strProductType%>;
            <%}%>

    }
</script>
