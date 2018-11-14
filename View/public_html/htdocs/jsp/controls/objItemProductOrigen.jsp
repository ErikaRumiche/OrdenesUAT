<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pe.com.nextel.bean.ProductBean" %>
<%@ page import="pe.com.nextel.bean.PlanTarifarioBean" %>
<%
    Hashtable hshtinputNewSection = null;
    NewOrderService objNewOrderService = new NewOrderService();
    String type_window  =  MiUtil.getString((String)request.getAttribute("type_window"));
    String objTypeEvent =  MiUtil.getString((String)request.getAttribute("objTypeEvent"));
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

    String strProductName = "";
    String strProductId   = "";
    String strMinuteRate  = "";
    String strPriceByMinute  = "";
    String strSolutionId = "";
    String strLevel = "";
    String message="";
    String strPlantarifario = "";
    String strDescripcionPlanTarifario = "";
    String strProductLineId = "";

    if(type_window.equals("DETAIL") || type_window.equals("EDIT")){
        String[] paramNpobjitemheaderid = request.getParameterValues("a");
        String[] paramNpobjitemvalue    = request.getParameterValues("b");

        for(int i=0;i<paramNpobjitemheaderid.length; i++){
            if( paramNpobjitemheaderid[i].equals("51") ) {
                strProductLineId = MiUtil.getString(paramNpobjitemvalue[i]);
            }
        }

        HashMap objHashMap = objNewOrderService.getBolsaCelulares(MiUtil.parseLong(strnpSite), MiUtil.parseLong(strCodigoCliente), MiUtil.parseLong(strProductLineId));
        //HashMap objHashMap = objNewOrderService.getProductBolsa(MiUtil.parseLong(strCodigoCliente),MiUtil.parseLong(strnpSite),MiUtil.parseLong(strSolutionId));
        if( objHashMap.get("message")!=null ) throw new Exception((String)objHashMap.get("message"));

        ProductBean objProductBean = (ProductBean)objHashMap.get("objProductBean");
        strProductName = MiUtil.getString(objProductBean.getNpproductname());
        strProductId   = MiUtil.getString(objProductBean.getNpproductid());

    }

%>

<tr>
    <td class="CellLabel" align="left" valign="top"><font color="red">&nbsp;*&nbsp;</font>Producto Origen</td>
    <td align="left" class="CellContent" >
        &nbsp;&nbsp;<input type="text" name="<%=nameHtmlItem%>" maxlength="30" value="<%=strProductName%>" readonly >
    </td>
</tr>

<script>
    function fxLoadProductBolsaOrigen(){

        <%if( type_window.equals("DETAIL") || type_window.equals("EDIT")){%>
            formCurrent.cmb_ItemProductoOrigen.value = '<%=strProductName%>';

        <%}%>


    }
</script>