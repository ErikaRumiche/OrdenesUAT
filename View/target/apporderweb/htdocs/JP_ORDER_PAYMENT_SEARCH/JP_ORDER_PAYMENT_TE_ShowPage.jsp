<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderUser" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="pe.com.nextel.service.SessionService" %>
<%@ page import="pe.com.nextel.exception.SessionException" %>
<%@ page import="pe.com.nextel.util.GenericObject" %>
<%@ page import="pe.com.nextel.bean.FormatBean"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pe.com.nextel.bean.DominioBean" %>
<%@ page import="java.util.List" %>
<%@ page import="pe.com.nextel.service.OrderExpressStoreService" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<c:set var="contextPath" value="${pageContext.servletContext.contextPath}"/>
<c:set var="pathURL">
    <c:out value="${pageContext.request.scheme}://" /><c:out value="${pageContext.request.remoteHost}:" /><c:out value="${pageContext.request.serverPort}" /><c:out value="${pageContext.servletContext.contextPath}" />
</c:set>

<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/css/salesweb.css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/jQuery-min.js"></script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!
    protected static Logger logger = Logger.getLogger(HttpServlet.class);

    protected String formatException(Throwable e) {
        return GenericObject.formatException(e);
    }
%>
<%
    String msg=null;

    String userLogin=null;
    GeneralService generalService = new GeneralService();
    String method="saveOrderPaymentTE";
    List<DominioBean> lstPaymentType;
    OrderExpressStoreService orderExpressStoreService=new OrderExpressStoreService();
    try{
        String strPortletPagePathContext="";
        String strSessionId1 = "";

        msg = session.getAttribute("strMessage")==null?"":(String)session.getAttribute("strMessage");

        PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
        strPortletPagePathContext = "/" + StringUtils.substringAfter(StringUtils.substringAfter(pReq.getParameter("_page_url"),pReq.getServerName()),"/");
        ProviderUser objetoUsuario1 = pReq.getUser();
        strSessionId1=objetoUsuario1.getPortalSessionId();

        logger.info(":::[flag==null] :::::Sesion capturada : " + objetoUsuario1.getName() + " - " + strSessionId1 + " - " + pReq.getUser());
        logger.info("*****************************************************:::strPortletPagePathContext : " + strPortletPagePathContext );
        userLogin=objetoUsuario1.getName();
        logger.info("=========userLoginuserLogin=========="+userLogin);
        logger.info("Sesion capturada despues del request: " + strSessionId1);
        PortalSessionBean portalSessionBean = (PortalSessionBean) SessionService.getUserSession(strSessionId1);
        logger.info("==========================Usuario de Logeo============================"+portalSessionBean.getLogin());
        String usuarioLogin=portalSessionBean.getLogin();
        logger.info("usuario login ============: " + usuarioLogin);



%>
<form name="frmdatos" id="frmdatos" method="POST" action='<c:out value="${pageContext.request.contextPath}"/>/orderexpressstoreservlet' target="bottomFrame">
    <input type="hidden" name="method" />
    <input type="hidden" name="hdnFlagSave" />
    <input type="hidden" name="hdnOrden" />
    <input type="hidden" name="hdnMonto" />
    <input type="hidden" name="hdnRa" />
    <input type="hidden" name="hdnVoucher" />
    <input type="hidden" name="hdnComentario" />
    <input type="hidden" name="hdnNumLogin" value="<%=pReq.getUser()%>"/>
    <input type="hidden" name="hdnUser" value="<%=portalSessionBean.getLogin()%>"/>
    <input type="hidden" name="hdnFlgVep" value=""/>
    <input type="hidden" name="hdnMontoFinanciado" value=""/>
    <input type="hidden" name="hdnNumCuotas" value=""/>
    <!--INICIO: AMENDEZ | PRY-1141-->
    <input type="hidden" name="hdnPaymentType" value=""/>
    <input type="hidden" name="hdnPaymentOrderQuotaId" value=""/>
    <!--FIN: AMENDEZ | PRY-1141-->

    <br>
    <table BORDER="0" class="PortletHeaderColor" CELLSPACING="0" CELLPADDING="0" WIDTH="100%">
        <tr class="PortletHeaderColor">
            <td class="SectionTitleLeftCurve" width="10">&nbsp;</td>
            <td class="SectionTitle">Caja Tienda Express</td>
            <td class="SectionTitleRightCurve" width="8">&nbsp;</td>
        </tr>
    </table>
    <table width="100%" align="center" border="0" cellspacing="1" cellpadding="1">
        <tr>
            <!--INICIO: AMENDEZ | PRY-1141-->
            <tr>
                <td class="CellLabel" align="left" valign="middle" width="25%" height="10">&nbsp;Tipo de pago</td>
                <td class="CellContent" valign="middle" width="25%" height="10">
                    <select name="cmbPaymentType" id="cmbPaymentType" >
                        <%  lstPaymentType= orderExpressStoreService.lstPaymentType();
                            for(DominioBean bean:lstPaymentType){
                                String dominio=bean.getValor();
                                String descripcion=bean.getDescripcion();
                        %>
                        <option value='<%=dominio%>'><%=descripcion%></option>
                        <%
                            }
                        %>
                    </select>
                </td>
                <td class="CellLabel" align="left" valign="middle" width="25%" height="10">&nbsp;</td>
                <td class="CellContent" valign="middle" width="25%" height="10">&nbsp;</td>
            </tr>
            <!--FIN: AMENDEZ | PRY-1141-->

        <tr>
            <td class="CellLabel" align="left" valign="middle" width="25%" height="10">&nbsp;Orden</td>
            <td class="CellContent" valign="middle" width="25%" height="10">
                <input type="text" id="v_num_order" name="v_num_order" size="20"/>
            </td>
            <td class="CellLabel" align="left" valign="middle" width="25%" height="10">
                <input type="button" id="v_buscar" name="v_buscar" value="Buscar" style="width: 100px"/>
            </td>
            <td class="CellContent" valign="middle" width="25%" height="10">&nbsp;</td>
        </tr>
        <tr>
            <td class="CellLabel" align="left" valign="middle" width="25%" height="10">&nbsp;Orden </td>
            <td class="CellContent" valign="middle" width="25%" height="10">
                <label id="txtOrden" size="20"> </label>
            </td>
            <td class="CellLabel" align="left" valign="middle" width="25%" height="10">&nbsp;Importe a Pagar &#40; sin RA &#41; </td><!-- EFLORES 22/08/2017 Encierra txt en Span-->
            <td class="CellContent" valign="middle" width="25%" height="10">
                <input type="text" id="txtMonto" name="txtMonto" size="20" />
            </td>
        </tr>
        <tr>
            <td class="CellLabel" align="left" valign="middle" width="25%" height="10">&nbsp;Raz&oacute;n Social </td>
            <td class="CellContent" valign="middle" width="25%" height="10">
                <label id="v_raz_social" size="50"></label>
            </td>
            <td class="CellLabel" align="left" valign="middle" width="25%" height="10">
                <!-- DESPIRITU PRY-0762 ini -->
                <!--  &nbsp;Renta Adelantada &#40; &#35;RA &#41; -->
                <!-- DESPIRITU PRY-0762 fin -->
            </td>
            <td class="CellContent" valign="middle" width="25%" height="10">
                <!-- DESPIRITU PRY-0762 ini -->
                <!--  input type="text" id="txtRa" name="txtRa" size="20" /-->
                <!-- DESPIRITU PRY-0762 fin -->
            </td>
        </tr>
        <tr>
            <td class="CellLabel" align="left" valign="middle" width="25%" height="10">&nbsp;Tienda</td>
            <td class="CellContent" valign="middle" width="25%" height="10">
                <label id="v_tienda" size="50"></label>
            </td>
            <td class="CellLabel" align="left" valign="middle" width="25%" height="10">&nbsp;Voucher de TE </td>
            <td class="CellContent" valign="middle" width="25%" height="10">
                <input type="text" id="txtVoucher" name="txtVoucher" size="20" />
            </td>
        </tr>
        <tr>
            <td class="CellLabel" align="left" valign="middle" width="25%" height="10">&nbsp;Comentario</td>
            <td class="CellContent" valign="middle" width="25%" height="10">
                <textarea  id="txtComentario" name="txtComentario" rows="2" cols="40" ></textarea>
            </td>
            <td class="CellLabel" align="left" valign="middle" width="25%" height="10">&nbsp;Vep</td><!-- EFLORES 22/08/2017 Se agrega el flag de vep-->
            <td class="CellContent" valign="middle" width="25%" height="10">&nbsp;
                <input type="checkbox" id="flgVep" name="flgVep" />
            </td>


            <td class="CellLabel" align="left" valign="middle" width="25%" height="10">&nbsp;</td><!-- EFLORES 22/08/2017 Se agrega el flag de vep-->
            <td class="CellContent" valign="middle" width="25%" height="10">&nbsp;</td>
        </tr>
        <tr>
            <td class="CellLabel" align="left" valign="middle" width="25%" height="25">&nbsp;Descripci&oacute;n</td>
            <td class="CellContent" valign="middle" width="25%" height="10">
                <label id="v_desc" ></label>
            </td>
                <td class="CellLabel" align="left" valign="middle" width="25%" height="10">&nbsp;
                    <span id="spImporte">Monto Cuota Inicial VEP </span>
                </td>
                <td class="CellContent" valign="middle" width="25%" height="10">
                    <input type="text" id="txtCuotaInicial" name="txtCuotaInicial" readonly />
                </td>
                <td class="CellLabel" align="left" valign="middle" width="25%" height="10">&nbsp;</td>
                <td class="CellContent" valign="middle" width="25%" height="10">&nbsp;</td>

        </tr>
            <tr id="trOrderVep">
                <td class="CellLabel" align="left" valign="middle" width="25%" height="25">&nbsp;Monto Total VEP</td>
                <td class="CellContent" valign="middle" width="25%" height="10">
                    <input type="text" id="txtMontoAFinanciar" name="txtMontoAFinanciar" readonly/>
                </td>
                <td class="CellLabel" align="left" valign="middle" width="25%" height="10">&nbsp;Nro Cuotas VEP</td>
                <td class="CellContent" valign="middle" width="25%" height="10">
                    <input type="text" id="cmbNumeroCuotas" name="cmbNumeroCuotas" readonly/>
                </td>
            </tr>
        </tr>
        <tr>
            <td align="center" colspan="4" class="CellContent" width="100%">
                <br>
                <input type="button" id="btnSaveOrderPaymentTE" name="btnSaveOrderPaymentTE" value="Grabar" onclick="javascript:fxSaveOrderPaymentTE()" disabled />
            </td>
        </tr>
    </table>
</form>
<%}catch(Exception ex){
    System.out.println("Address New try catch-->"+MiUtil.getMessageClean(ex.getMessage()));
%>
<script>
    parent.mainFrame.document.getElementById('txtOrden').value="";
    parent.mainFrame.document.getElementById('v_raz_social').value="";
    parent.mainFrame.document.getElementById('v_tienda').value="";
    parent.mainFrame.document.getElementById('v_desc').value="";
</script>
<%
    }%>
<script type="text/javascript">
    var flg="S";
    $(function(){
        $("#v_buscar").click(function(){
            var messageInfo='';
            var orderId=document.getElementById('v_num_order').value;
            //INICIO: AMENDEZ | PRY-1141
            var paymenttype=document.getElementById("cmbPaymentType").value;
            //FIN: AMENDEZ | PRY-1141

            var url_server = '<c:out value="${pageContext.request.contextPath}"/>/orderexpressstoreservlet';
            var params = 'method=getServiceList&orderIdv='+orderId+'&userLogin='+'<%=userLogin%>'+'&paymenttype='+paymenttype;

            if( isNaN(orderId) ) {
                document.frmdatos.v_num_order.select();
                document.frmdatos.v_num_order.focus();
                document.getElementById('txtOrden').innerHTML="";
                document.getElementById('v_raz_social').innerHTML="";
                document.getElementById('v_tienda').innerHTML="";
                document.getElementById('v_desc').innerHTML="";
                $("#txtMonto").val("0");

                $("#flgVep")[0].checked = false;
                $("#flgVep").attr("disabled",true);
                $("#txtMontoAFinanciar").val("0");
                $("#txtCuotaInicial").val("0");
                $("#txtMontoAFinanciar").attr("disabled",true);
                $("#txtCuotaInicial").attr("disabled",true);
                $("#cmbNumeroCuotas").attr("disabled",true);
                $("#trOrderVep").hide();
                $("#spImporte").hide();
                $("#txtCuotaInicial").hide();
                $("#txtMonto").removeAttr("readonly");

                $("#btnSaveOrderPaymentTE").attr("disabled",true);
                alert("[ERROR] El campo debe ser numerico...");
            }else if( orderId =="" ) {
                document.frmdatos.v_num_order.select();
                document.frmdatos.v_num_order.focus();
                document.getElementById('txtOrden').innerHTML="";
                document.getElementById('v_raz_social').innerHTML="";
                document.getElementById('v_tienda').innerHTML="";
                document.getElementById('v_desc').innerHTML="";
                $("#txtMonto").val("0");

                $("#flgVep")[0].checked = false;
                $("#flgVep").attr("disabled",true);
                $("#txtMontoAFinanciar").val("0");
                $("#txtCuotaInicial").val("0");
                $("#txtMontoAFinanciar").attr("disabled",true);
                $("#txtCuotaInicial").attr("disabled",true);
                $("#cmbNumeroCuotas").attr("disabled",true);
                $("#trOrderVep").hide();
                $("#spImporte").hide();
                $("#txtCuotaInicial").hide();
                $("#txtMonto").removeAttr("readonly");

                $("#btnSaveOrderPaymentTE").attr("disabled",true);
                alert("El campo N° de orden no debe estar vacio");
            }else{
                $.ajax({
                    url: url_server,
                    dataType:'json',
                    data: params,
                    type: "POST",
                    success:function(data){
                        if(data.OrderDetailBean!=null){
                            messageInfo=data.OrderDetailBean.message;

                            if(messageInfo != 'SUCCESSFUL' ){
                                document.getElementById('txtOrden').innerHTML="";
                                document.getElementById('v_raz_social').innerHTML="";
                                document.getElementById('v_tienda').innerHTML="";
                                document.getElementById('v_desc').innerHTML="";
                                $("#txtMonto").val("0");

                                $("#flgVep")[0].checked = false;
                                $("#flgVep").attr("disabled",true);
                                $("#txtMontoAFinanciar").val("0");
                                $("#txtCuotaInicial").val("0");
                                $("#txtMontoAFinanciar").attr("disabled",true);
                                $("#txtCuotaInicial").attr("disabled",true);
                                $("#cmbNumeroCuotas").attr("disabled",true);
                                $("#trOrderVep").hide();
                                $("#spImporte").hide();
                                $("#txtCuotaInicial").hide();
                                $("#txtMonto").removeAttr("readonly");

                                $("#btnSaveOrderPaymentTE").attr("disabled",true);
                                alert(messageInfo);
                            }else{

                                if(data.OrderDetailBean.npTypeService==="<%=Constante.TYPE_SERVICE_TE%>"){
                                    if(data.OrderDetailBean.n_isSamebuilding==0){
                                        alert("El pago debe ser en la misma Tienda Express");
                                        flg="N";
                                    }else{
                                        //INICIO: AMENDEZ | PRY-1141
                                        $("#btnSaveOrderPaymentTE").removeAttr("disabled");
                                        $("#cmbPaymentType").attr("disabled",true);
                                        //FIN: AMENDEZ | PRY-1141

                                        flg="S";
                                        if(data.OrderDetailBean.npOrderId===undefined ){
                                            document.getElementById('txtOrden').innerHTML="";
                                        }else{
                                            document.getElementById('txtOrden').innerHTML= data.OrderDetailBean.npOrderId;
                                            np_existOrder= data.OrderDetailBean.npOrderId;
                                        }
                                        if(data.OrderDetailBean.swName===undefined ){
                                            document.getElementById('v_raz_social').innerHTML= "";
                                        }else{
                                            document.getElementById('v_raz_social').innerHTML= data.OrderDetailBean.swName;
                                        }
                                        if(data.OrderDetailBean.npShortName===undefined ){
                                            document.getElementById('v_tienda').innerHTML= "";
                                        }else{
                                            document.getElementById('v_tienda').innerHTML= data.OrderDetailBean.npShortName;
                                        }
                                        if(data.OrderDetailBean.npDescription===undefined ){
                                            document.getElementById('v_desc').innerHTML="";
                                        }else{
                                            document.getElementById('v_desc').innerHTML= data.OrderDetailBean.npDescription;
                                        }

                                        //INICIO
                                        if(data.OrderDetailBean.npvep===undefined ){

                                            //document.getElementById('flgVep').innerHTML="";
                                        }else{
                                            $("#flgVep")[0].checked = true;
                                            var vep = data.OrderDetailBean.npvep;
                                            if ( vep == '1' ){
                                                $("#flgVep")[0].checked = true;
                                                $("#flgVep").attr("disabled",true);
                                                document.frmdatos.hdnFlgVep.value = vep;
                                                $("#txtMontoAFinanciar").removeAttr("disabled");
                                                $("#txtCuotaInicial").removeAttr("disabled");
                                                $("#cmbNumeroCuotas").removeAttr("disabled");
                                                $("#trOrderVep").show();
                                                $("#spImporte").show();
                                                $("#txtCuotaInicial").show();
                                                $("#txtMonto").attr('readonly', true);

                                                alert("Para una Orden de Venta a Plazos el campo Importe a Pagar será de solo lectura");
                                            }else{
                                                $("#flgVep")[0].checked = false;

                                                document.frmdatos.hdnFlgVep.value = vep;
                                                $("#flgVep").removeAttr("disabled");
                                                $("#txtMontoAFinanciar").val("0");
                                                $("#txtCuotaInicial").val("0");
                                                $("#txtMontoAFinanciar").attr("disabled",true);
                                                $("#txtCuotaInicial").attr("disabled",true);
                                                $("#cmbNumeroCuotas").attr("disabled",true);
                                                $("#trOrderVep").hide();
                                                $("#spImporte").hide();
                                                $("#txtCuotaInicial").hide();
                                                $("#txtMonto").removeAttr("readonly");
                                            }

                                        }
                                        if(data.OrderDetailBean.npinitialquota===undefined ){
                                            document.getElementById('txtCuotaInicial').value="";
                                            document.getElementById('txtMonto').value="";
                                        }else {
                                            document.getElementById('txtCuotaInicial').value = data.OrderDetailBean.npinitialquota;
                                            document.getElementById('txtMonto').value = data.OrderDetailBean.npinitialquota;
                                        }
                                        if(data.OrderDetailBean.npvepquantityquota===undefined ){
                                            document.getElementById('cmbNumeroCuotas').value="0";
                                        }else{
                                            document.getElementById('cmbNumeroCuotas').value= data.OrderDetailBean.npvepquantityquota;
                                        }
                                        if(data.OrderDetailBean.npamountfinanced===undefined ){
                                            document.getElementById('txtMontoAFinanciar').value="0";
                                        }else{
                                            document.getElementById('txtMontoAFinanciar').value= data.OrderDetailBean.npamountfinanced;
                                        }
                                        //FIN

                                        //INICIO: AMENDEZ | PRY-1141
                                        if(data.OrderDetailBean.nppaymentorderquotaid===undefined ){
                                            document.frmdatos.hdnPaymentOrderQuotaId.value = "0";
                                        }else {
                                            document.frmdatos.hdnPaymentOrderQuotaId.value = data.OrderDetailBean.nppaymentorderquotaid;
                                        }

                                        if ( paymenttype == "2" ){
                                            if(data.OrderDetailBean.npqa===undefined ){
                                                document.getElementById('txtMonto').value="";
                                            }else {
                                                document.getElementById('txtMonto').value = data.OrderDetailBean.npqa;
                                            }
                                        }
                                        //FIN: AMENDEZ | PRY-1141

                                    }

                                }else{
                                    alert("La Orden debe ser de Tienda Express");
                                }
                            }


                        }else{

                            document.getElementById('txtOrden').innerHTML="";
                            document.getElementById('v_raz_social').innerHTML="";
                            document.getElementById('v_tienda').innerHTML="";
                            document.getElementById('v_desc').innerHTML="";
                            $("#txtMonto").val("0");

                            $("#flgVep")[0].checked = false;
                            $("#flgVep").attr("disabled",true);
                            $("#txtMontoAFinanciar").val("0");
                            $("#txtCuotaInicial").val("0");
                            $("#txtMontoAFinanciar").attr("disabled",true);
                            $("#txtCuotaInicial").attr("disabled",true);
                            $("#cmbNumeroCuotas").attr("disabled",true);
                            $("#trOrderVep").hide();
                            $("#spImporte").hide();
                            $("#txtCuotaInicial").hide();
                            $("#txtMonto").removeAttr("readonly");

                            $("#btnSaveOrderPaymentTE").attr("disabled",true);
                            alert("No Existe la Orden "+orderId);
                        }
                    },
                    error:function(data){
                        document.getElementById('txtOrden').innerHTML="";
                        document.getElementById('v_raz_social').innerHTML="";
                        document.getElementById('v_tienda').innerHTML="";
                        document.getElementById('v_desc').innerHTML="";
                        $("#txtMonto").val("0");

                        $("#flgVep")[0].checked = false;
                        $("#flgVep").attr("disabled",true);
                        $("#txtMontoAFinanciar").val("0");
                        $("#txtCuotaInicial").val("0");
                        $("#txtMontoAFinanciar").attr("disabled",true);
                        $("#txtCuotaInicial").attr("disabled",true);
                        $("#cmbNumeroCuotas").attr("disabled",true);
                        $("#trOrderVep").hide();
                        $("#spImporte").hide();
                        $("#txtCuotaInicial").hide();
                        $("#txtMonto").removeAttr("readonly");

                        $("#btnSaveOrderPaymentTE").attr("disabled",true);
                        alert("No Existe la Orden Nro "+orderId);
                    }
                });
            }
        });
    });
</script>
<script>
    function fxSaveOrderPaymentTE(){
        //INICIO: AMENDEZ | PRY-1141
        var paymenttype=document.getElementById("cmbPaymentType").value;
        //FIN: AMENDEZ | PRY-1141		
	/** AYATACO - Inicio */
        document.getElementById('btnSaveOrderPaymentTE').disabled = true;
	/** AYATACO - Fin */
        if(fxValidateOrderPaymentTE()){
	    /** AYATACO - Inicio */
            if(fxValidateOrderCant(paymenttype)) {
	    /** AYATACO - Fin */
                document.frmdatos.method.value = "<%=method%>";
                document.frmdatos.hdnFlagSave.value = "S";
                document.frmdatos.hdnOrden.value = document.getElementById('v_num_order').value;
                document.frmdatos.hdnMonto.value = document.getElementById('txtMonto').value;
                // DESPIRITU PRY-0762 ini
                //document.frmdatos.hdnRa.value=document.getElementById('txtRa').value;
                // DESPIRITU PRY-0762 fin
                document.frmdatos.hdnVoucher.value = document.getElementById('txtVoucher').value;
                document.frmdatos.hdnComentario.value = document.getElementById('txtComentario').value;
//EFLORES 22/08/2017
                document.frmdatos.hdnMontoFinanciado.value = document.getElementById("txtMontoAFinanciar").value;
                document.frmdatos.hdnNumCuotas.value = document.getElementById("cmbNumeroCuotas").value;
                //FIN EFLORES

            //INICIO: AMENDEZ | PRY-1141
            document.frmdatos.hdnPaymentType.value = document.getElementById("cmbPaymentType").value;
            //FIN: AMENDEZ | PRY-1141
                if (flg != "N") {
                    document.frmdatos.submit();
                }
            }
	/** AYATACO - Inicio */
        }
	/** AYATACO - Fin */
    }

    function fxValidateOrderPaymentTE(){

        //validar si la orden a grabar es de tienda Express
        //NO PERMITIR EL PAGO sie campo voucer esta vacio
        //var txtVoucher = document.frmdatos.txtVoucher.value;
        var txtVoucher=document.getElementById('txtVoucher').value;
        // DESPIRITU PRY-0762 ini
        // var txtRa=document.getElementById('txtRa').value;
        // DESPIRITU PRY-0762 fin
        var txtComentario=document.getElementById('txtComentario').value;
        var txtMonto=document.getElementById('txtMonto').value;

        if(fxTrim(txtMonto)=="" || document.getElementById('txtMonto').value=='0') {
            document.frmdatos.txtMonto.select();
            document.frmdatos.txtMonto.focus();
            alert("Ingrese Importe a Pagar ");
            return false;
        }

        if(fxTrim(txtVoucher)=="") {
            document.frmdatos.txtVoucher.select();
            document.frmdatos.txtVoucher.focus();
            alert("Ingrese Voucher de TE");
            return false;
        }

        if(txtComentario.length >200){
            document.frmdatos.txtComentario.select();
            document.frmdatos.txtComentario.focus();
            alert("El comentario tiene longitud mayor a la permitida (200 caracteres).");
            return false;
        }

        // DESPIRITU PRY-0762 ini
        /*
        if(fxTrim(txtRa)=="") {
            var r = confirm("Renta Adelantada vacía,desea continuar con el Pago!");
            if (r == true) {
                return true;
            } else {
                document.frmdatos.txtRa.select();
                document.frmdatos.txtRa.focus();
                return false;
            }
          }

          */
        // DESPIRITU PRY-0762 fin
        return true;
    }
    /** AYATACO - Inicio */
    function fxValidateOrderCant(paymenttype){
        var form = document.frmdatos;
        var npsourceid = form.v_num_order;
        var url_server = '<c:out value="${pageContext.request.contextPath}"/>/orderexpressstoreservlet';
        var params = 'method=validateOrderExist&npsource='+paymenttype+'&npsourceid='+npsourceid.value;

        $.ajax({
            url: url_server,
            type: "POST",
            async: false,
            cache: false,
            data: params,
            dataType: "json",
            success: function(dataResponse) {
                var mensaje = dataResponse.strMessage;
                if (mensaje == null || mensaje == "") {
                    var cantItems = dataResponse.itemsCount;
                    if (cantItems > 0) {
                        alert("La orden ya tiene un pago registrado");
                        document.getElementById('btnSaveOrderPaymentTE').disabled = false;
                        return false;
                    }
                } else {
                    alert("Ocurrió un error: " + mensaje);
                    return false;
                }
            },

            error:function(data){
                alert("Error al obtener información de la orden");
                document.getElementById('btnSaveOrderPaymentTE').disabled = false;
                return false;
            }
        });
        return true;
    }
    /** AYATACO - Fin */

    function fxTrim(s) {
        while (s.length>0 && (s.charAt(0)==" "||s.charCodeAt(0)==10||s.charCodeAt(0)==13)) {
            s=s.substring(1, s.length);
        }
        while (s.length>0 && (s.charAt(s.length-1)==" "||s.charCodeAt(s.length-1)==10||s.charCodeAt(s.length-1)==13)) {
            s=s.substring(0, s.length-1);
        }
        return s;
    }


    if('<%=msg%>'!=''){

        if('<%=Constante.OPERATION_STATUS_OK%>'=='<%=msg%>'){
            alert('Se registro correctamente el pago en caja Tienda Express');
        }else{
            alert('<%=msg%>');
        }

        parent.mainFrame.document.getElementById('txtComentario').value="";
        parent.mainFrame.document.getElementById('txtMonto').value="";
        //parent.mainFrame.document.getElementById('txtRa').value="";
        parent.mainFrame.document.getElementById('txtVoucher').value="";
	/** AYATACO - Inicio */
        parent.mainFrame.document.getElementById('btnSaveOrderPaymentTE').disabled = false;
	/** AYATACO - Fin */
        $("#flgVep")[0].checked = false;
        $("#flgVep").attr("disabled",true);
        $("#txtMontoAFinanciar").val("0");
        $("#txtCuotaInicial").val("0");
        $("#txtMontoAFinanciar").attr("disabled",true);
        $("#txtCuotaInicial").attr("disabled",true);
        $("#cmbNumeroCuotas").attr("disabled",true);
        $("#trOrderVep").hide();
        $("#spImporte").hide();
        $("#txtCuotaInicial").hide();
        $("#txtMonto").removeAttr("readonly");
    }
</script>
<script type="text/javascript">
    function validar(idcampo) {
        var exp_reg  = /^[a-z\d\u00C0-\u00ff]+$/i; // expresión regular para letras(máy o minus), acentuadas o no, y números
        var verifica = exp_reg.test(idcampo);
        if (verifica == true){
            return true;
        }else {
            document.frmdatos.txtVoucher.select();
            document.frmdatos.txtVoucher.focus();
            alert("El voucher debe ser alfanumerico");
            return false;
        }
    }
    //EFLORES 22/08/2017
    function valRegExp(value,regexp){
        var verif = regexp.test(value);
        if(verif == true){
            return true;
        }
        return false;
    }

    //portega
    function generateVepAmount(obj){
        if($("#"+obj.id).val()=='')return false;
        if ($("#"+obj.id).val()<1){
            alert('Monto Cuota Inicial VEP no permitido');
            $("#"+obj.id).val("").focus();
            $("#txtMontoAFinanciar").val("");
            return false;
        }
        if ($("#txtMonto").val()==null ||$("#txtMonto").val() ==''){
            alert('Debe ingresar primero un Importe a Pagar');
            $("#"+obj.id).val("");
            $("#txtMonto").focus();
            $("#txtMontoAFinanciar").val("");
            return false;
        }
        if (Number($("#"+obj.id).val()) >= Number($("#txtMonto").val())){
            alert('Monto Cuota Inicial VEP debe ser menor que el Importe a Pagar');
            $("#"+obj.id).val("").focus();
            $("#txtMontoAFinanciar").val("");
            return false;
        }
        //calculo automatico del monto total VEP
        $("#txtMontoAFinanciar").val(Number($("#txtMonto").val())-Number($("#"+obj.id).val()));
    }
</script>
<script>
    $(document).ready(function(){
            //EFLORES 22/08/2017 Se añade el metodo para el checkbox
            $("#trOrderVep").hide();
            $("#spImporte").hide();
            $("#txtCuotaInicial").hide();
            $("#txtMontoAFinanciar").attr("disabled",true);
            $("#cmbNumeroCuotas").attr("disabled",true);
            $("#txtCuotaInicial").attr("disabled",true);
            $("#txtMontoAFinanciar").val("0");
            $("#txtCuotaInicial").val("0");
            $("#cmbNumeroCuotas").val("0");
            $("#flgVep").click(function(e){
                if(this.checked){
                    document.frmdatos.hdnFlgVep.value = "1";
                    $("#txtMontoAFinanciar").val("0");
                    $("#txtCuotaInicial").val("0");
                    $("#txtMontoAFinanciar").attr("disabled",true);
                    $("#txtCuotaInicial").attr("disabled",true);
                    $("#cmbNumeroCuotas").attr("disabled",true);
                    $("#trOrderVep").hide();
                    $("#spImporte").hide();
                    $("#txtCuotaInicial").hide();
                    $("#flgVep")[0].checked = false;
                }else{
                    document.frmdatos.hdnFlgVep.value = "0";
                    $("#txtMontoAFinanciar").val("0");
                    $("#txtCuotaInicial").val("0");
                    $("#txtMontoAFinanciar").attr("disabled",true);
                    $("#txtCuotaInicial").attr("disabled",true);
                    $("#cmbNumeroCuotas").attr("disabled",true);
                    $("#trOrderVep").hide();
                    $("#spImporte").hide();
                    $("#txtCuotaInicial").hide();
                    $("#flgVep")[0].checked = false;
                }
            });
            //EFLORES FIN
    });
</script>