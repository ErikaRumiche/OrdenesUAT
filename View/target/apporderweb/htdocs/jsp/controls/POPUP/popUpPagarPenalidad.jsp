<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.util.GenericObject" %>
<%@ page import="pe.com.nextel.exception.SessionException" %>
<%@ page import="java.util.List" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.service.*" %>
<%@ page import="pe.com.nextel.bean.*" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net/"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<c:set var="contextPath" value="${pageContext.servletContext.contextPath}"/>
<c:set var="pathURL">
    <c:out value="${pageContext.request.scheme}://" /><c:out value="${pageContext.request.remoteHost}:" /><c:out value="${pageContext.request.serverPort}" /><c:out value="${pageContext.servletContext.contextPath}" />
</c:set>
<%
try {
    System.out.println("----------------popUpPagarPenalidad-------------------");

    String strUserId = request.getParameter("hdnUserId");
    System.out.println("Sesión capturada después del resuest : " + strUserId );
    PortalSessionBean portalSessionBean1 = (PortalSessionBean)SessionService.getUserSession(strUserId);
    if(portalSessionBean1==null) {
        System.out.println("No se encontró la sesión de Java ->" + strUserId);
        throw new SessionException("La sesión finalizó");
    }
    System.out.println("AppId : " + portalSessionBean1.getAppId());
    
    double montoTotal = 0.0;

    String strVctTelefono = request.getParameter("phones");
    String strCustomerId = request.getParameter("customerId");
    String strOrderId = request.getParameter("strOrderId");
    String strNroRuc     = request.getParameter("nroRuc");

    System.out.println("customerId:"+strCustomerId);
    System.out.println("strOrderId:"+strOrderId);
    System.out.println("strUserId:"+strUserId);
    System.out.println("strNroRuc:"+strNroRuc);
    int flagGenerarOrden = 0;
    int flagAnularOrden = 0;
    //int cantSites = 0; //Cantidad de sites para determinar si el cliente es flat o large
    List<Integer> combos = new ArrayList<Integer>(); //Restriccion para mostrar tipo de pago
    String statusOrden = ""; //Estatus de la orden de pago de penalidad

    CustomerService customerService = new CustomerService();
    PenaltyService penaltyService = new PenaltyService();
    GeneralService generalService = new GeneralService();
    EditOrderService editOrderService = new EditOrderService();

    //Obtenemos la cantidad de sites de un cliente
    //int tempSites = Integer.parseInt(strCustomerId);
    //cantSites = customerService.getSitesByCustomer(tempSites).size();
    HashMap map = null;
 /*   if("20".equals(strNroRuc.substring(0,2))){ //Cliente large
        System.out.println("[popUpPagarPenalidad] Cliente tipo LARGE, lista de select");
        map = penaltyService.getConfigurationList(Constante.PENALTY_CHK_LARGE);
    }else{
        System.out.println("[popUpPagarPenalidad] Cliente tipo FLAT lista de select");
        map = penaltyService.getConfigurationList(Constante.PENALTY_CHK_FLAT);
    }
 
 */
 	map = penaltyService.getConfigurationListBoxList(strNroRuc, portalSessionBean1.getUserid(), portalSessionBean1.getAppId());
 
    List<ListBoxBean> LBean = (List<ListBoxBean>)map.get("listBoxList");
    for(ListBoxBean bean : LBean){
        System.out.println("----"+bean.getIdListBox());
        combos.add(bean.getIdListBox());
    }
    ArrayList<TableBean> paymentMethodList = new ArrayList<TableBean>();
        System.out.println("telefonos:"+strVctTelefono);

        HashMap objHashMap = penaltyService.getPenaltyListByPhone("ORDER",strOrderId, strCustomerId);

        ArrayList<PenaltyBean> penaltyList = (ArrayList<PenaltyBean>)objHashMap.get("penalidadList");
        long fastOrderId = MiUtil.parseLong(objHashMap.get("fastOrderId").toString()); //Obtiene el fastorderid Generado
        String strMessage = (String)objHashMap.get(Constante.MESSAGE_OUTPUT);
        if(strMessage!=null) {
            throw new Exception(strMessage);
        }

        //valida que la orden de pago no se haya anulado por otro lado
        System.out.println("fastOrderId > 0 : "+fastOrderId);
        if(fastOrderId > 0){
            System.out.println("strOrderId  : "+strOrderId);
            System.out.println("fastOrderId  : "+fastOrderId);
            System.out.println("portalSessionBean1.getLogin() : "+portalSessionBean1.getLogin());

            strMessage = penaltyService.updateStatusFastOrder(Long.parseLong(strOrderId),fastOrderId,portalSessionBean1.getLogin());
            if(strMessage!=null) {
                throw new Exception(strMessage);
            }

            objHashMap = penaltyService.getPenaltyListByPhone("ORDER",strOrderId, strCustomerId);
            penaltyList = (ArrayList<PenaltyBean>)objHashMap.get("penalidadList");
            fastOrderId = MiUtil.parseLong(objHashMap.get("fastOrderId").toString()); //Obtiene el fastorderid Generado
            strMessage = (String)objHashMap.get(Constante.MESSAGE_OUTPUT);
            if(strMessage!=null) {
                throw new Exception(strMessage);
        }
        }

        //Si no se limpio la relacion de la orden entonces se pinta el estatus de la misma
        if(fastOrderId > 0){
            HashMap fOrder = editOrderService.getOrder(fastOrderId);
            strMessage = (String)fOrder.get(Constante.MESSAGE_OUTPUT);
            if(strMessage!=null) {
                throw new Exception(strMessage);
            }
            OrderBean order = (OrderBean)fOrder.get("objResume");
            statusOrden = MiUtil.getStrClean(order.getNpPaymentStatus());
        }


        if(penaltyList != null){

            //Se valida si el boton generar orden de penalidad o anular orden estaran activos
            HashMap hshMapButtons = penaltyService.verifAddendumPenalty(Constante.OPTION_EDIT_ORDER, strOrderId, "");
            flagGenerarOrden = (Integer)hshMapButtons.get("FLAG_GENERAR_ORDEN_PAGO");
            flagAnularOrden = (Integer)hshMapButtons.get("FLAG_ANULAR");

            request.setAttribute("penaltyList", penaltyList);
            System.out.println("num penalidades:"+penaltyList.size());

            HashMap objHashMapMotive = penaltyService.getConfigurationList(Constante.CONFIG_MOTIVE_LIST);
            ArrayList<ListBoxBean> motiveList = (ArrayList<ListBoxBean>)objHashMapMotive.get("listBoxList");
            String strMessageMotive = (String)objHashMap.get(Constante.MESSAGE_OUTPUT);
            if(strMessageMotive!=null) {
                throw new Exception(strMessageMotive);
            }

            if(motiveList != null){
                request.setAttribute("motiveList", motiveList);
                System.out.println("num motivos:"+motiveList.size());
            }

            //Calculamos el monto total
            for(int i=0; i<penaltyList.size();i++){
                montoTotal = montoTotal + penaltyList.get(i).getMontoFinal();
            }

            request.setAttribute("montoTotal", String.format("%.2f", montoTotal));

            HashMap objHashMapPayMethod = generalService.getValueNpTable(Constante.NPTABLE_TIPO_CARGO);
            paymentMethodList = (ArrayList<TableBean>)objHashMapPayMethod.get("objArrayList");
            String strMessagePayMethod = (String)objHashMap.get(Constante.MESSAGE_OUTPUT);
            if(strMessagePayMethod!=null) {
                throw new Exception(strMessagePayMethod);
            }

            if(paymentMethodList != null){
                request.setAttribute("paymentMethodList", paymentMethodList);
                System.out.println("num metodos de pago:"+paymentMethodList.size());
            }
        }
%>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/ua.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/jquery-1.10.2.js"></script>
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
<style>
    table.orders {
        border: 0px;
        width: 100%;
        margin: 1px 0 1px 0 !important;
        font-weight: normal;
        font-size: 8pt;
        color: #000000;
        font-family: Arial, Helvetica;
    }
    th {
        padding: 2px 4px 2px 4px !important;
        text-align: center;
        vertical-align: top;
        font-weight: bold;
    }
    thead tr {
        background-color: #E8E4B7
    }
    tr.odd {
        background-color: #F5F5EB
    }
    tr.even {
        background-color: #E8E4B7
    }
</style>
<script>
    function fxInputTabEnter() {
        return ((window.event.keyCode == 9) || (window.event.keyCode == 13));
    }
    function fxValidateAmount(number, pos){
        var flag = true;

        if(number == ''){
            alert("Debe ingresar un numero");
            jQuery("#montoFinal"+pos).focus();
            return false;
        }
        else{
            if(number==0 ||  parseFloat(number)<parseFloat(jQuery("#penalidad"+pos).val())) {
                if(jQuery("#motivoSelect"+pos).val() == ""){
                    alert("Elegir Motivo");
                    jQuery("#motivoSelect"+pos).focus();
                    return false;
                }
            }else{
                if(parseFloat(number)>parseFloat(jQuery("#penalidad"+pos).val())){
                    alert("No puede ingresar un monto mayor a la penalidad");
                    jQuery("#montoFinal"+pos).val(jQuery("#penalidad"+pos).val());
                    jQuery("#montoFinal"+pos).focus();
                    return false;
                }
            }
        }

        return flag;
    }
    //INICIO JGABRIEL
    function fxGeneratePayOrder(evt){
        var total = 0;
        var val=true;
        //validaciones de items de adenda
        jQuery('.montoFinal').each(function(index, value){
            val = fxValidateAmount(jQuery("#montoFinal" + (index+1)).val(), (index+1));
            if(!val) return false;
        });
        if(val){
            if(jQuery("#paymentMethodSelect").val() == ""){
                val= false;
                alert("Elegir Metodo de pago");
                jQuery("#paymentMethodSelect").focus();
            }
        }

        if(val){
        $("#btnGenerarPago").prop( "disabled", true );
        var url_server = '<c:out value="${pageContext.request.contextPath}"/>/penaltyservlet';
        $.post(url_server,
                $.param($("#frmdatos2").serializeArray()),
                function(data, status){
                    var resultData = data.split("|");

                    if(resultData[0] == 1){
                        alert("Se creo la orden "+resultData[1]+ " correctamente");
                        $("#ordenPenalidad").val(resultData[1]);
                        document.getElementById('btnAnular').style.display = 'block';
                        document.getElementById('btnCerrarPago').style.display = 'block';
                            $("#btnAnular").prop( "disabled", false );
                            $("#btnCerrarPago").prop( "disabled", false );
                    }
                    else{
                        alert(resultData[1]);
                    }
                }
        );
        }
        return true;
    }
    //FIN JGABRIEL

    //EFLORES REQ-0428
    function fxAnularOrden(){
        if(confirm("¿Esta seguro que desea Anular la orden ?")){
        $("#btnAnular").prop( "disabled", true );
        var url_server = '<c:out value="${pageContext.request.contextPath}"/>/penaltyservlet';
        $.ajax({
            url:url_server,
            type:"POST",
            dataType:'json',
            data:{
                "method": "anulatePaymentOrder",
                "strOrderId": <%=strOrderId%>,
                "strSessionId":"<%=strUserId%>"
            },
            success:function(data){
                if(!(typeof data.strMessage === 'undefined')) {
                    alert(data.strMessage);
                        $("#btnGenerarPago").prop( "disabled", true );
                        $("#btnAnular").prop( "disabled", true );
                        $("#btnCerrarPago").prop( "disabled", true );
                }
            },
            error:function(xhr, ajaxOptions, thrownError){
                if (xhr.status === 0) {
                    alert('Not connect: Verify Network.');
                } else if (xhr.status == 404) {
                    alert('Requested page not found [404]');
                } else if (xhr.status == 500) {
                    alert('Internal Server Error [500].');
                } else if (xhr.status === 'parsererror') {
                    alert('Requested JSON parse failed.');
                } else if (xhr.status === 'timeout') {
                    alert('Time out error.');
                } else if (xhr.status === 'abort') {
                    alert('Ajax request aborted.');
                } else {
                    alert('Uncaught Error: ' + xhr.responseText);
                }
            }
        });
        }
    }
    function fxClosePayOrder(){
        if(confirm("¿Esta seguro que desea Cerrar la orden ?")){
            $("#btnCerrarPago").prop( "disabled", true );
            var url_server = '<c:out value="${pageContext.request.contextPath}"/>/penaltyservlet';
            $.ajax({
                url:url_server,
                type:"POST",
                dataType:'json',
                data:{
                    "method": "closePaymentOrder",
                    "strOrderId": <%=strOrderId%>,
                    "strSessionId":"<%=strUserId%>"
                },
                success:function(data){
                    if(!(typeof data.strMessage === 'undefined')) {
                        alert(data.strMessage);
                        $("#btnAnular").prop( "disabled", true );
                        $("#btnGenerarPago").prop( "disabled", true );
                    }
                },
                error:function(xhr, ajaxOptions, thrownError){
                    if (xhr.status === 0) {
                        alert('Not connect: Verify Network.');
                    } else if (xhr.status == 404) {
                        alert('Requested page not found [404]');
                    } else if (xhr.status == 500) {
                        alert('Internal Server Error [500].');
                    } else if (xhr.status === 'parsererror') {
                        alert('Requested JSON parse failed.');
                    } else if (xhr.status === 'timeout') {
                        alert('Time out error.');
                    } else if (xhr.status === 'abort') {
                        alert('Ajax request aborted.');
                    } else {
                        alert('Uncaught Error: ' + xhr.responseText);
                    }
                }
            });
        }
    }
    //EFLORES
</script>
<form name="frmdatos2" id="frmdatos2" method="POST" action='<c:out value="${pageContext.request.contextPath}"/>/penaltyservlet'>
    <input type="hidden" name="method" value="doGeneratePayOrder" />
    <input type="hidden" name="hdnOrdenId" value="<%=strOrderId%>" />
    <input type="hidden" name="hdnUserId" value="<%=strUserId%>"/>
    <input type="hidden" name="hdnSessionId" value="<%=strUserId%>" />
    <input type="hidden" name="hdnCustomerId" value="<%=strCustomerId%>" />


    <table  align="center" width="100%" border="0">
    <tr><td>
        <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
            <tr class="PortletHeaderColor">
                <td class="LeftCurve" valign="top" align="left" width="10">&nbsp;&nbsp;</td>
                <td class="PortletHeaderColor" align="left" valign="top">
                    <font class="PortletHeaderText">
                        Pagar Penalidad
                    </font>
                </td>
                <td align="right" class="RightCurve" width="10%" >&nbsp;&nbsp;</td>
            </tr>
        </table>
    </td></tr>
    <!--Detalle de la penalidad-->
    <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
        <tr>
            <td>
                <display:table name="penaltyList" id="penalty" class="orders" style="width: 100%">
                    <display:column  title="Adenda" style="text-align:right">
                        <input type="hidden" name="hndItemPenaltyId" value ="<c:out value="${penalty.numItemPenaltyId}"/>"/>
                        <c:out value="${penalty.numAdenda}"/>
                    </display:column>
                    <display:column property="telefono" title="Telefono" style="text-align:center"/>
                    <display:column property="fechaInicio" title="F. Inicio." style="text-align:center"/>
                    <display:column property="fechaFin" title="F. Fin" style="text-align:center"/>
                    <display:column property="diasTotales" title="Dias Totales" style="text-align:right"/>
                    <display:column property="diasEfectivos" title="Dias Efectivos" style="text-align:right"/>
                    <display:column property="montoBeneficio" title="Monto Beneficio" style="text-align:right"/>
                    <display:column title="Motivo de Descuento" style="text-align:center">
                       <c:choose>
                           <c:when test="${penalty.habilitado == 1}">
                               <select name='motivoSelect' id = "motivoSelect<c:out value='${penalty_rowNum}'/>" style="padding: 0">
                                   <option value=""></option>
                                   <c:forEach items="${motiveList}" var="motive">
                                       <option value='<c:out value="${motive.idListBox}"/>'><c:out value="${motive.descListBox}"/></option>
                                   </c:forEach>
                               </select>
                           </c:when>
                           <c:otherwise>

                               <c:forEach items="${motiveList}" var="motive">
                                   <c:if test="${motive.idListBox == penalty.motivo}">
                                   <c:out value="${motive.descListBox}"/>
                                   </c:if>
                               </c:forEach>

                           </c:otherwise>
                       </c:choose>

                    </display:column>
                    <display:column  title="Penalidad" style="text-align:right">
                        <input type="text" readonly="readonly" id = "penalidad<c:out value='${penalty_rowNum}'/>" value ="<c:out value="${penalty.penalidad}"/>" style="padding: 0;text-align:right"/>
                    </display:column>
                    <display:column title="Monto Final" style="text-align:center">
                        <c:choose>
                            <c:when test="${penalty.habilitado == 1}">
                                <input type="text"  name="montoFinal" id="montoFinal<c:out value='${penalty_rowNum}'/>" class="montoFinal" style="padding: 0;text-align:right"
                                       value="<c:out value="${penalty.montoFinal}"/>"   />
                                <input type="hidden" id="montoActual<c:out value='${penalty_rowNum}'/>" name="montoActual" value="<c:out value="${penalty.montoFinal}"/>">
                            </c:when>
                            <c:otherwise>
                                <input type="text" readonly="readonly" value="<c:out value="${penalty.montoFinal}"/>" style="padding: 0;text-align:right"/>
                            </c:otherwise>
                        </c:choose>
                    </display:column>
                </display:table>
            </td>
        </tr>
        <tr>
            <% if (penaltyList.size()>0){ %>
            <table align="right" width="33%" border="0">
                <tr>
                    <td>&nbsp;</td>
                    <td class="CellLabel" align="left" valign="middle">
                        Total a pagar
                    </td>
                    <td align="center" class="CellContent">
                        <input type="text" id="montoTotal" name="montoTotal" style="padding: 0;text-align:right" readonly
                               value="<c:out value="${montoTotal}"/>"/>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td class="CellLabel" align="left" valign="middle">
                        Forma de pago
                    </td>
                    <td align="center" class="CellContent">
                        <select name='paymentMethodSelect' id='paymentMethodSelect' <% if(flagGenerarOrden == 0){%> disabled="disabled" <%} %> style="padding: 0; width:87%">
						    <option value=""></option>
                            <% for (TableBean bean : paymentMethodList){
                                 if(combos.contains(Integer.parseInt(bean.getNpValue()))){%>
                                <option value='<%= bean.getNpValue()  %>'><%= bean.getNpValueDesc() %></option>
                                <% }}%>
                        </select>
                    </td>
                </tr>
                <tr/><tr/><tr/><tr/><tr/><tr/>
                <tr>
                    <td align="center" valign="middle">
                        <input id="btnAnular" name="btnAnular" <% if(flagAnularOrden == 0){%> disabled="disabled" <%} %> <% if(flagAnularOrden == 0){%> disabled="disabled" <%} %> type="button" value="Anular Orden de Penalidad" style="width:100%" onclick="javascript:fxAnularOrden();">
                    </td>
                    <td>
                        <input id="btnGenerarPago" name="btnGenerarPago" <% if(flagGenerarOrden == 0){%> disabled="disabled" <%} %> type="button" value="Generar Orden de Penalidad" style="width:100%" onclick="javascript:fxGeneratePayOrder();">
                    </td>
                    <td>
                        <input id="btnCerrarPago" name="btnCerrarPago" <% if(flagGenerarOrden != 0){%> disabled="disabled" <%} %> type="button" value="Cerrar Orden de Penalidad" style="width:100%" onclick="javascript:fxClosePayOrder();">
                    </td>
                </tr>
                <tr/>
                <tr>
                    <td>&nbsp;</td>
                    <td class="CellLabel" align="left" valign="middle">
                        Orden Penalidad
                    </td>
                    <td align="center" class="CellContent">
                        <input type="text" name="ordenPenalidad" id="ordenPenalidad" style="padding: 0;text-align:right" readonly
                               value="<%= fastOrderId %>"/>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td class="CellLabel" align="left" valign="middle">
                        Estado de pago
                    </td>
                    <td align="center" class="CellContent">
                        <%= statusOrden %>
                    </td>
                </tr>
            </table>
            <% }%>
        </tr>
    </table>
</table>
</form>
<script>
    var fOId="<%= fastOrderId %>";
    if(fOId==0 || fOId==""){
        document.getElementById('btnAnular').style.display = 'none';
        document.getElementById('btnCerrarPago').style.display = 'none';
    }

    $(window).load(function(){
        $('.montoFinal').keypress(function(event) {
            if(event.which < 46 || event.which >= 58 || event.which == 47) {
                event.preventDefault();
            }

            if(event.which == 46 && $(this).val().indexOf('.') != -1) {
                this.value = '' ;
            }
        });
        $(".montoFinal").keyup(function(){
            var total = 0;
            jQuery('.montoFinal').each(function(){
                total += parseFloat(jQuery(this).val());
            });
            jQuery("#montoTotal").val(total.toFixed(2));
        });
        $(".montoFinal").blur(function(){
            if($.trim($(this).val())==""){
                $(this).val("0");
                $(".montoFinal").trigger( "keyup");
            }
        });
        $(".montoFinal").focus(function() {
            $(this).select();
            $(".montoFinal").trigger( "keyup");
        });
    });
</script>
<%} catch(Exception e) {
	e.printStackTrace();
    out.println("<script>alert('Ocurrio un error');</script>");
%>
<div style="display: none;">
    <%=e.getMessage()%>
</div>
<%}%>