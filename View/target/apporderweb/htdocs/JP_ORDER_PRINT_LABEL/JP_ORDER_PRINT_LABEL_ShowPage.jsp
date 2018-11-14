<%System.out.println("[JP_ORDER_PRINT_LABEL][Inicio]");%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderUser" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="pe.com.nextel.bean.DominioBean" %>
<%@ page import="pe.com.nextel.service.SessionService" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.exception.SessionException" %>

<%@ page import="pe.com.nextel.util.GenericObject" %>
<%@ page import="pe.com.nextel.service.OrderPrintLabelService" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<c:set var="contextPath" value="${pageContext.servletContext.contextPath}"/>
<c:set var="pathURL">
    <c:out value="${pageContext.request.scheme}://" /><c:out value="${pageContext.request.remoteHost}:" /><c:out value="${pageContext.request.serverPort}" /><c:out value="${pageContext.servletContext.contextPath}" />
</c:set>
<%!
    protected static Logger logger = Logger.getLogger(HttpServlet.class);

    protected String formatException(Throwable e) {
        return GenericObject.formatException(e);
    }
%>

<%
    String msg=null;

    String userLogin=null;
    try{
        String strPortletPagePathContext="";
        String strSessionId1 = "";

        msg = session.getAttribute("strMessage")==null?"":(String)session.getAttribute("strMessage");


        PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
        strPortletPagePathContext = "/" + StringUtils.substringAfter(
                StringUtils.substringAfter(pReq.getParameter("_page_url"),pReq.getServerName()),"/");
        ProviderUser objetoUsuario1 = pReq.getUser();
        strSessionId1=objetoUsuario1.getPortalSessionId();


        System.out.println(":::[flag==null] :::::Sesion capturada : " + objetoUsuario1.getName() + " - " + strSessionId1 + " - " + pReq.getUser());
        System.out.println("*****************************************************:::strPortletPagePathContext : " + strPortletPagePathContext );
        userLogin=objetoUsuario1.getName();
        System.out.println("=========userLoginuserLogin=========="+userLogin);
        System.out.println("Sesi?n capturada despu?s del request: " + strSessionId1);
        PortalSessionBean portalSessionBean = (PortalSessionBean) SessionService.getUserSession(strSessionId1);
        System.out.println("==========================Usuario de Logeo============================" + portalSessionBean.getLogin());
        String usuarioLogin=portalSessionBean.getLogin();
        System.out.println("usuario login ============: " + usuarioLogin);
        String wsLogin=portalSessionBean.getLogin();
        int buildingId=portalSessionBean.getBuildingid();

%>
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/jQuery-min.js"></script>
<script language="JavaScript" type="text/JavaScript">
    function selydesAllChk(form,activa)
    {
        for(i=0;i<form.elements.length;i++)
            if(form.elements[i].type=="checkbox")
                form.elements[i].checked=activa;
    }
</script>

<style>
    #container{
        overflow-y: scroll;
    }
</style>
<form name="frmdatos" id="frmdatos" method="POST" action='<c:out value="${pageContext.request.contextPath}"/>/orderprintservlet'>

    <input type="hidden" name="method" />
    <input type="hidden" name="hdnFlagSave" />
    <input type="hidden" name="hdnOrden" />
    <input type="hidden" name="hdnNumLogin" value="<%=pReq.getUser()%>"/>
    <input type="hidden" name="hdnUser" value="<%=portalSessionBean.getLogin()%>"/>

    <input type="hidden" name="hdnCutomerId" />
    <input type="hidden" name="hdnCutomerName" />
    <input type="hidden" name="hdnSpecificationId" />

    <table align=center width="100%">
        <tr>
            <td align="center">
                <table border=0 class="CTable" align="left" width="100%">
                    <tr>
                        <td align="center" width="35%">&nbsp;</td>
                        <td align="center" width="30%">
                            <table align="center" cellspacing="1" cellpadding="0" width="100%" >
                                <tr>
                                    <td align="left">
                                        <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
                                            <tr>
                                                <td> <input type="hidden" id="hdnACUM" name="hdnACUM" ></td>
                                            </tr>
                                            <tr>
                                                <td class="SectionTitleLeftCurve" width="10">&nbsp;&nbsp;</td>
                                                <td class="SectionTitle">&nbsp;&nbsp;CONSULTA DE ORDEN</td>
                                                <td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
                                            </tr>
                                        </table>
                                        <table width="100%" border="0" cellspacing="1" cellpadding="1" class="RegionBorder">
                                            <tr>
                                                <td class="CellLabel">&nbsp;&nbsp;N° Orden </td>
                                                <td class="CellContent">
                                                    <input type="text" id="txtorderid" name="txtorderid" size="20" maxlength="16">
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td align="center">
                                        <table align="center" cellspacing="1" cellpadding="0" width="100%" >
                                            <tr>
                                                <td>&nbsp;</td>
                                            </tr>
                                            <tr>
                                                <td align="center" width="100%">
                                                    <input type="button" id="btbuscar" name="btbuscar" value=" Buscar ">
                                                </td>

                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="35%">&nbsp;</td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <table border=0 class="CTable" align=center>
                    <tr>
                        <td>
                            <table align="center" cellspacing="1" cellpadding="0" width="100%">
                                <tr>
                                    <td>
                                        <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
                                            <tr>
                                                <td class="SectionTitleLeftCurve" width="10">&nbsp;&nbsp;</td>
                                                <td class="SectionTitle">&nbsp;&nbsp;RESULTADO DE LA BÚSQUEDA DE ÍTEMS DE LA ORDEN</td>
                                                <td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
                                            </tr>
                                        </table>

                                        <table border="1" cellpadding="0" cellspacing="0" class="RegionBorder" align="center">
                                            <tr valign="top">
                                                <td class="RegionHeaderColor" align="left">
                                                    <table id="headerRowNew" width="820">
                                                        <tr id="newTableHeader"><th align="center" class="CellLabel" width="50" >&nbsp;N°</th>
                                                            <th align="center" class="CellLabel" width="250" >&nbsp;Razón Social</th>
                                                            <th align="center" class="CellLabel" width="150" >&nbsp;IMEI</th>
                                                            <th align="center" class="CellLabel" width="200" ><b>&nbsp;Plan</b></th>
                                                            <th align="center" class="CellLabel" width="120" >&nbsp;Teléfono</th>
                                                            <th align="center" class="CellLabel" width="50" ><input type=checkbox id="chckAll" name="chckAll" onclick="selydesAllChk(this.form,this.checked);"></th></tr>
                                                    </table>
                                                    <div id="container">
                                                        <table name="grid" id="grid" cellPadding="2" cellSpacing="2" width="820" >
                                                            <tr id="headerRow">

                                                            </tr>
                                                        </table>
                                                    </div>

                                                </td>
                                            </tr>
                                        </table>

                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            <td/>
        <tr/>
        <tr>
            <th  align="center"><input type="button" id="btimprimir" name="btimprimir" value="Imprimir"></th>
        </tr>
    </table>
</form>

<SCRIPT language="javascript">
    function addRow(tableID,index,id, razon, imei, plan, telefono) {

        var table =  document.getElementById(tableID); //document.getElementById(tableID).getElementsByTagName('tbody')[0];

        var rowCount = table.rows.length;
        var row = table.insertRow(rowCount);

        var cell1 = row.insertCell(0);
        cell1.className = "CellContent";
        cell1.align = "center";
        cell1.width = 50;
        cell1.innerHTML = index;

        var cell2 = row.insertCell(1);
        cell2.className = "CellContent";
        cell2.align = "center";
        cell2.width = 250;
        cell2.innerHTML = razon;

        var cell3 = row.insertCell(2);
        cell3.className = "CellContent";
        cell3.align = "center";
        cell3.width = 150;
        cell3.innerHTML = imei;

        var cell4 = row.insertCell(3);
        cell4.className = "CellContent";
        cell4.align = "center";
        cell4.width = 200;
        cell4.innerHTML = plan;

        var cell5 = row.insertCell(4);
        cell5.className = "CellContent";
        cell5.align = "center";
        cell5.width = 120;
        cell5.innerHTML = telefono;

        var cell6 = row.insertCell(5);
        var element1 = document.createElement("input");
        element1.type = "checkbox";
        element1.name = "chkbox[]";
        element1.id   = "chkbox[]";
        element1.value = id;
        cell6.className = "CellContent";
        cell6.align = "center";
        cell6.width = 50;
        cell6.appendChild(element1);

    }
</SCRIPT>
<script type="text/javascript">

    function validaOrderPrintLabel()
    {
        var orderId=document.getElementById('txtorderid').value;
        $("#grid tr:has(td)").remove();
        $("#chckAll").removeAttr("checked");

        if (orderId == null){

            alert ('Ingrese un valor');

        }else{

            if(orderId.length == 0){

                alert ('Ingrese un valor');
            }else{

                orderId= orderId.replace(/\s+/g, ''); //Elimina los espacios en blanco

                if(orderId.length == 0){

                    alert ('Ingrese un valor');

                }else{

                    var url_server = '<c:out value="${pageContext.request.contextPath}"/>/orderprintservlet';
                    var params = 'method=validaOrderPrintLabel&orderIdv='+orderId+'&userLogin='+'<%=userLogin%>';
                    if( isNaN(orderId) )
                    {
                        document.frmdatos.txtorderid.select();
                        document.frmdatos.txtorderid.focus();
                        alert("[ERROR] El campo debe ser numerico...");
                    }else{
                        $.ajax({
                            url: url_server,
                            dataType:'json',
                            data: params,
                            type: "POST",
                            success:function(data) {
                                if(!(typeof data.strMessage === 'undefined')){
                                    alert(data.strMessage);
                                }else{
                                    var count = 0;
                                    var customername= "";
                                    var customernameid="";
                                    var specificationid="";
                                    // 2. recorre el listado de item y crea cada fila
                                    $.each( data.OrderDetailBean, function( key, val ) {
                                        if(customername=="" && customernameid=="" && specificationid==""){
                                            customername=val.npsolutionname;
                                            customernameid=val.npwarrant;
                                            specificationid=val.npAplicarVO;
                                        }
                                        addRow("grid",(count+1), val.npitemid, customername, val.npimeinumber, val.npplanname, val.npphone);
                                        count++;
                                    });
                                    document.getElementById('hdnCutomerId').value=customernameid;
                                    document.getElementById('hdnCutomerName').value=customername;
                                    document.getElementById('hdnSpecificationId').value=specificationid;
                                    /*if(count==0){
                                     alert('La categoría de la orden: '+orderId+' no pertenece a la impresión de etiquetas');
                                     }
                                     else */
                                    if(count>15){
                                        $("#container").css("height", "408px");
                                    }else{
                                        $("#container").css("height", "");
                                    }
                                }

                            },
                            error:function(data,status,er) {
                                //alert("error: "+data+" status: "+status+" er:"+er);
                                $("#grid tr:has(td)").remove();
                                if (data.status === 0) {
                                    alert('Not connect: Verify Network.');
                                } else if (data.status == 404) {
                                    alert('Requested page not found [404]');
                                } else if (data.status == 500) {
                                    alert('Internal Server Error [500].');
                                } else if (status === 'parsererror') {
                                    alert('Requested JSON parse failed.');
                                } else if (status === 'timeout') {
                                    alert('Time out error.');
                                } else if (status === 'abort') {
                                    alert('Ajax request aborted.');
                                } else {
                                    alert('Uncaught Error: ' + data.responseText);
                                }
                            }
                        });
                    }

                }

            }

        }
    }

    $(function(){
        $("#btbuscar").click(function(){
            validaOrderPrintLabel();
        });
        $('#txtorderid').keypress(function (e) {

            if(e.which == 13)
            {   e.preventDefault();
                validaOrderPrintLabel();
            }
        });
        $("#btimprimir").click(function(){
            var checked = []
            var checkedBoxes = $("input[name='chkbox[]']:checked");
            if(checkedBoxes.length <= 0) {
                alert('No se ha seleccionado ningún registo para la impresión');
                return false;
            }else{
                var orderId=document.getElementById('txtorderid').value;
                var customerId=document.getElementById('hdnCutomerId').value;
                var customerName=document.getElementById('hdnCutomerName').value;
                var specificationId=document.getElementById('hdnSpecificationId').value;
                var url_server = '<c:out value="${pageContext.request.contextPath}"/>/orderprintservlet';

                $(checkedBoxes).each(function ()
                {
                    checked.push($(this).val());
                });

                //manda a imprimir
                $.post(url_server,
                        {
                            method: "sendPrintLabels",
                            orderId: orderId,
                            customerId:customerId,
                            customerName:customerName,
                            specificationId:specificationId,
                            itemsId: ""+checked.toString()+"",
                            wsLogin:"<%=wsLogin%>",
                            buildingId:<%=buildingId%>
                        },
                        function(data, status){
                            alert(data);
                        });
            }

        });
    });
</script>
<%
    }catch(SessionException se) {
        se.printStackTrace();
        System.out.println("[JP_ORDER_PRINT_LABEL][Finalizó la sesión]");
	/*out.println("<script>alert('Su sesión ha finalizado. Ingrese nuevamente');</script>");
	String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";*/
        //out.print("<script>parent.mainFrame.location.replace('"+strUrl+"');</script>");
    }catch(Exception e) {
        System.out.println("<script>alert('"+MiUtil.getMessageClean(e.getClass() + " - " + e.getMessage())+"');</script>");
        logger.error(formatException(e));
    }%>