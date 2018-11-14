<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.*" %>
<%@ page import="pe.com.nextel.util.*" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pe.com.nextel.bean.*" %>
<%
    Hashtable hshtinputNewSection = null;
    GeneralService   objGeneralService   = new GeneralService();
    CustomerService   objCustomerService   = new CustomerService();
    String type_window =  MiUtil.getString((String)request.getAttribute("type_window"));
    boolean disabled = false;
    String    strCodigoCliente= "",
            strnpSite ="",
            strCodBSCS = "",
            hdnSpecification = "",
            strDivision = "",
            strObjectReadOnly = "",
            strCodLevel = "";
    strObjectReadOnly = request.getParameter("strObjectReadOnly");
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

    HashMap objHashMap;
    /*INICIO ADT-BLC-083 --LHUAPAYA*/
    if(Constante.SPEC_BOLSA_CREAR == Integer.parseInt(hdnSpecification) || Constante.SPEC_BOLSA_UPGRADE == Integer.parseInt(hdnSpecification) || Constante.SPEC_BOLSA_DOWNGRADE == Integer.parseInt(hdnSpecification)|| Constante.SPEC_BOLSA_DESACTIVAR == Integer.parseInt(hdnSpecification)){
        objHashMap = objGeneralService.getDominioList(Constante.ORDERS_BC_LEVEL);
    }else{/*FIN ADT-BLC-083 --LHUAPAYA*/
        objHashMap = objGeneralService.getDominioList(Constante.ORDERS_LEVEL);
    }

    if( objHashMap.get("strMessage")!=null ) throw new Exception((String)objHashMap.get("strMessage"));

    ArrayList objArrayList = (ArrayList)objHashMap.get("arrDominioList");

    HashMap  objHash =  objCustomerService.getCustomerSites(MiUtil.parseLong(strCodigoCliente),null);
    ArrayList lista_sites = (ArrayList)objHash.get("objArrayList");
    if(lista_sites.size()==0){
        strCodLevel = Constante.LEVEL_ONE_CODE;
    }
    /*INICIO LHUAPAYA  [ADT-BCL-083]*/
    if(MiUtil.parseInt(hdnSpecification) == Constante.SPEC_BOLSA_CREAR || MiUtil.parseInt(hdnSpecification) == Constante.SPEC_BOLSA_DESACTIVAR || MiUtil.parseInt(hdnSpecification) == Constante.SPEC_BOLSA_UPGRADE || MiUtil.parseInt(hdnSpecification) == Constante.SPEC_BOLSA_DOWNGRADE){
        strCodLevel = Constante.LEVEL_ONE_CODE;
    }
    /*FIN LHUAPAYA  [ADT-BCL-083]*/
%>
<%
    if("S".equals(strObjectReadOnly)){
%>
<select disabled="disabled" name="<%=nameHtmlItem%>">
        <%}else{%>
    <select name="<%=nameHtmlItem%>">
        <%}%>
        <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
        <%
            if ( objArrayList != null && objArrayList.size() > 0 ){
                for( int i=0; i<objArrayList.size();i++ ){
                    DominioBean dominio = (DominioBean)objArrayList.get(i);
                    if(strCodLevel.equals(dominio.getValor()) && ("2021".equals(hdnSpecification) || "2082".equals(hdnSpecification) || "2083".equals(hdnSpecification) || "2084".equals(hdnSpecification) || "2085".equals(hdnSpecification))){ //INICIO LHUAPAYA  [ADT-BCL-083]
                        if(objArrayList.size() > 1) { // INICIO LHUAPAYA  [ADT-BCL-083]
                            objArrayList.remove(i+1);
                        }
        %>
        <option selected="selected" value='<%=dominio.getValor()%>'>
            <%=dominio.getDescripcion()%>
        </option>
        <%
        }else{
        %>
        <option value='<%=dominio.getValor()%>'>
            <%=dominio.getDescripcion()%>
        </option>
        <%
                    }
                }
            }
        %>

    </select>