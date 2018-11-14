<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="pe.com.portability.service.PortabilityOrderService" %>
<%@ page import="pe.com.portability.service.PortabilityGeneralService" %>
<%@ page import="pe.com.portability.bean.PortabilityParticipantBean" %>
<%@ page import="pe.com.portability.bean.PortabilityOrderBean" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="pe.com.nextel.service.SessionService" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="pe.com.nextel.bean.DominioBean" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%
    try {
        Hashtable hshParam = (Hashtable) request.getAttribute("hshtInputEditSection");
        if (hshParam == null) hshParam = new Hashtable();
        long lCustomerId = MiUtil.parseLong((String) hshParam.get("strCustomerId"));
        long lSiteId = MiUtil.parseLong((String) hshParam.get("strSiteId"));
        long lOrderId = MiUtil.parseLong((String) hshParam.get("strOrderId"));
        String strSessionId = MiUtil.getString((String) hshParam.get("strSessionId"));
        String strDocumentini = MiUtil.getString((String) hshParam.get("strDocument"));
        String strTypeDocumentini = MiUtil.getString((String) hshParam.get("strTypeDocument"));
        String strStatus = MiUtil.getString((String) hshParam.get("strStatus"));

        // MMONTOYA [ADT-FMO-088 Fijo Móvil Fase I]
        int specificationId = Integer.parseInt((String)hshParam.get("strSpecificationId"));
        int divisionId = Integer.parseInt((String)hshParam.get("strDivisionId"));

        PortalSessionBean objPortalSesBean = (PortalSessionBean) SessionService.getUserSession(strSessionId);
        String strLogin = objPortalSesBean.getLogin();
        String npAssignor = "";
        String npAssignorDesc = "";
        String strMessage = null;
        String ruta = "";
        String npOrderParent = "";  //DLAZO
        ArrayList arrassignortList = null;
        ArrayList arrPortabOrderList = null;
        ArrayList arrUploadDoc = null;
        ArrayList arrOrderParentList = null;  //DLAZO
        HashMap hshAssignor = new HashMap();
        HashMap hshPortabOrder = new HashMap();
        HashMap hshUploadDoc = new HashMap();
        HashMap hshOrderNew = new HashMap();
        HashMap hshOrderParentChild = new HashMap();
        HashMap hshOrderParent = new HashMap();   //DLAZO
        DominioBean dominio = new DominioBean();
        PortabilityParticipantBean objParticipantBean = null;
        PortabilityOrderBean objPortabOrderBean = new PortabilityOrderBean();
        PortabilityOrderBean objPortaParentChildBean = new PortabilityOrderBean();
        PortabilityOrderBean objPOBean = null;  //DLAZO
        String strDocument = null;
        String strTypeDocument = null;

        String strCustomerType = null;
        String strScheduleDays = null;
        long lItems = 0;

        PortabilityOrderService objPortabilityOrderService = new PortabilityOrderService();
        arrassignortList = new ArrayList();
        hshAssignor = (HashMap) objPortabilityOrderService.getParticipantList(specificationId, divisionId);
        arrassignortList = (ArrayList) hshAssignor.get("objParticipantList");

        arrPortabOrderList = new ArrayList();
        hshPortabOrder = (HashMap) objPortabilityOrderService.getPortabOrderList(lOrderId);
        arrPortabOrderList = (ArrayList) hshPortabOrder.get("objPortabOrderList");
        if (arrPortabOrderList.size() > 0) {
            objPortabOrderBean = (PortabilityOrderBean) arrPortabOrderList.get(0);
            npAssignor = (String) objPortabOrderBean.getNpAssignor();
            npAssignorDesc = (objPortabOrderBean.getNpAssignorDesc() != null ? objPortabOrderBean.getNpAssignorDesc() : "");
            strDocument = (String) objPortabOrderBean.getNpDocument();
            strTypeDocument = (String) objPortabOrderBean.getNpTypeDocument();
            strCustomerType = (String) objPortabOrderBean.getNpCustomerType();
            strScheduleDays = (String) objPortabOrderBean.getNpScheduleDays();
        }

        //Codigo para obtener el Id de la Orden Padre.
        //Inicio - DLAZO
        hshOrderParent = (HashMap) objPortabilityOrderService.getItemsPortabList(lOrderId);
        arrOrderParentList = new ArrayList();
        arrOrderParentList = (ArrayList) hshOrderParent.get("objItemList");
        objPOBean = new PortabilityOrderBean();
        if (arrOrderParentList.size() > 0) {
            objPOBean = (PortabilityOrderBean) arrOrderParentList.get(0);
            npOrderParent = (objPOBean.getNpOrderParentId() != null ? objPOBean.getNpOrderParentId() : "");
        }
        //Fin - DLAZO

        //Obtenemos la cantidad de items
        NewOrderService objNewOrderService = new NewOrderService();
        ArrayList objArrayList = objNewOrderService.ItemDAOgetItemDeviceOrder(lOrderId);
        long canItems = objArrayList.size();

        //Valores por defecto
        if (strCustomerType==null) {
            if (arrOrderParentList.size() > 10 || canItems > 10) {
                strCustomerType = Constante.CLIENTE_ESPECIAL_TIPOE; //"Especial";
            } else {
                strCustomerType = Constante.CLIENTE_ESPECIAL_TIPON; //"Normal";
            }
        }
        lItems = arrOrderParentList.size();

        hshUploadDoc = (HashMap) objPortabilityOrderService.getDominioList("PORTAB_UPLOAD_HIGH");
        arrUploadDoc = new ArrayList();
        arrUploadDoc = (ArrayList) hshUploadDoc.get("arrDominioList");
        if (arrUploadDoc.size() > 0) {
            dominio = (DominioBean) arrUploadDoc.get(0);
            ruta = (String) dominio.getDescripcion();
            System.out.println("Ruta Upload Portabilidad " + ruta);
        }

        //Si la orden tiene una orden padre, devolvera el orderId de la orden padre.
        hshOrderNew = (HashMap) objPortabilityOrderService.getValOrdenHija(lOrderId);
        long lOrderIdNew = MiUtil.parseLong((String) hshOrderNew.get("lngValor"));

        //Encontramos la referencia de la Orden si es Hija ó Padre
        //--------------------------------------------------------
        hshOrderParentChild = (HashMap) objPortabilityOrderService.getParentChildOrder(lOrderId);
        strMessage = (String) hshOrderParentChild.get("strMessage");
        if (strMessage != null)
            throw new Exception(strMessage);

        objPortaParentChildBean = (PortabilityOrderBean) hshOrderParentChild.get("objParentChildList");
        String strTitleOrder = objPortaParentChildBean.getNpTitleOrdeParentChild() == null ? "" : objPortaParentChildBean.getNpTitleOrdeParentChild();
        long lnOrderidParentChild = objPortaParentChildBean.getNpOrderidParentChild();
        String strOrderidParentChild = MiUtil.getString(lnOrderidParentChild) == "0" ? "" : MiUtil.getString(lnOrderidParentChild);

        //Obtiene los valores del combo Documentos
        //-----------------------------------------
        HashMap hshDocument = objPortabilityOrderService.getDocumentList("DOCUMENT_CUSTOMER");
        strMessage = (String) hshDocument.get("strMessage");
        if (strMessage != null)
            throw new Exception(strMessage);
        ArrayList arrDocument = (ArrayList) hshDocument.get("arrDocumentList");

        //Obtiene los valores del combo tipo de cliente
        //-----------------------------------------
        HashMap hshCustomerType = objPortabilityOrderService.getDocumentList("CUSTOMER_TYPE");
        strMessage = (String) hshCustomerType.get("strMessage");
        if (strMessage != null)
            throw new Exception(strMessage);
        ArrayList arrCustomerType = (ArrayList) hshCustomerType.get("arrDocumentList");

        //Obtiene los valores del combo dias para programar
        //-----------------------------------------
        HashMap hshScheduleDays = objPortabilityOrderService.getDocumentList("DAYS_TO_SCHEDULE");
        strMessage = (String) hshScheduleDays.get("strMessage");
        if (strMessage != null)
            throw new Exception(strMessage);
        ArrayList arrScheduleDays = (ArrayList) hshScheduleDays.get("arrDocumentList");

        //Verifica si la seccion de Documentos puede visualizarse
        //-------------------------------------------------------
        String strValue = null;
        PortabilityGeneralService objPortabilityGeneralService = new PortabilityGeneralService();
        HashMap hshPermissionDocument = objPortabilityGeneralService.getSectionDocumentValidate("SECTION_DOCUMENT", strStatus, strValue);
        strMessage = (String) hshPermissionDocument.get("strMessage");
        if (strMessage != null)
            throw new Exception(strMessage);
        strValue = (String) hshPermissionDocument.get("strValue");

%>


<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXEdit.js"></script>
<script type="text/javascript" defer>
    function fxLoadPortabilityAltaDoc() {
        var vForm = document.frmdatos;
        var strCustomerName = vForm.hdnCustName.value;
        <%if(npOrderParent != ""){%>
        var strUrl = "<%=ruta%>/Document.jsp?strIdClient=<%=lCustomerId%>&strNameClient=" + strCustomerName + "&strIdOrder=<%=npOrderParent%>&strCreator=PORTABILITY&strView=Editar";
        var winUrl = "<%=request.getContextPath()%>/DYNAMICSECTION/DynamicSectionPortabilityHigh/PopUpOrderFrame.jsp?av_url=" + escape(strUrl);
        window.open(winUrl, "Portabilidad_Alta", "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,screenX=60,top=40,left=60,screenY=40,width=850,height=600");
        <%}else{%>
        var strUrl = "<%=ruta%>/Document.jsp?strIdClient=<%=lCustomerId%>&strNameClient=" + strCustomerName + "&strIdOrder=<%=lOrderId%>&strCreator=PORTABILITY&strView=Editar";
        var winUrl = "<%=request.getContextPath()%>/DYNAMICSECTION/DynamicSectionPortabilityHigh/PopUpOrderFrame.jsp?av_url=" + escape(strUrl);
        window.open(winUrl, "Portabilidad_Alta", "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,screenX=60,top=40,left=60,screenY=40,width=850,height=600");
        <%}%>
    }

    function fxSectionPortHighHeaderValidate() {
        var vForm = document.frmdatos;
        if (vForm.cmbAssignor.value == "") {
            alert("Debe ingresar el Cedente ");
            vForm.cmbAssignor.focus();
            return false;
        }
        return true;
    }

    loadSection();

    function loadSection() {
        var vForm = document.frmdatos;
        var npestado = vForm.txtEstadoOrden.value;
        if (npestado != 'TIENDA01' && npestado != 'ADM_VENTAS' && npestado != 'VENTAS') {
            vForm.txtAssignor.style.display = "";
            vForm.cmbAssignor.style.display = "none";
        } else {
            vForm.txtAssignor.style.display = "none";
            vForm.cmbAssignor.style.display = "";
        }
    }


    function getShowParentPorta(strOrderId) {
        var strUrl = "/portal/page/portal/orders/ORDER_EDIT?an_nporderid=" + strOrderId;
        var url = "/portal/pls/portal/WEBSALES.NPSL_GENERAL_PL_PKG.WINDOW_FRAME?av_title=" + escape("PORTAL NEXTEL") + "&av_url=" + escape(strUrl);
        window.open(url, "WinCustomer", "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,screenX=60,top=40,left=60,screenY=40,width=850,height=600");
        return;
    }

    function getShowDocumment(strDocument, strTypeDocument) {

        var vForm = document.frmdatos;

        if (strTypeDocument == vForm.cmbDocumento.value) {
            vForm.txtDocumento.value = strDocument;
            vForm.cmbDocumento.value = strTypeDocument;
            // vForm.txtDocumento.disabled=true;
        } else {
            vForm.txtDocumento.value = "";
            // vForm.txtDocumento.disabled=false;
        }
    }

    function fxValidateDocument(objValue, strDocument, strTypeDocument) {

        form = document.frmdatos;
        v_typedoc = form.cmbDocumento.value;
        
        if ((objValue.length)!=0){
          if ((objValue.length) != 8 && (v_typedoc == "DNI")) {
              alert("Debe ingresar un numero de 8 digitos")
              if (strTypeDocument != v_typedoc) {
                  form.txtDocumento.value = "";
              } else {
                  form.txtDocumento.value = strDocument;
              }
          }
          if ((objValue.length) != 11 && (v_typedoc == "RUC")) {
              alert("Debe ingresar un numero de 11 digitos")
              if (strTypeDocument != v_typedoc) {
                  form.txtDocumento.value = "";
              } else {
                  form.txtDocumento.value = strDocument;
              }
          }
        }else{
          alert("Debe ingresar un numero de documento");
          form.txtDocumento.focus();
          return false;
        }
        
        return true;
    }

    fxDisableEspecialClientInfo();

    function fxDisableEspecialClientInfo() {
        form = document.frmdatos;
        var npestado = form.txtEstadoOrden.value;
        if (npestado != '<%=Constante.INBOX_TIENDA01%>' && npestado != '<%=Constante.INBOX_VENTAS%>') {
            form.cmbCustomerType.disabled = true;
            form.cmbScheduleDays.disabled = true;
        }
    }


    function fxShowScheduleDays() {
        form = document.frmdatos;
        v_custtype = form.cmbCustomerType.value;
        if (v_custtype == "<%=Constante.CLIENTE_ESPECIAL_TIPOE%>") {
            form.cmbScheduleDays.disabled = false;
            form.cmbScheduleDays.value = "<%=Constante.CLIENTE_ESPECIAL_DIASXDEFECTO%>";
        } else {
            form.cmbScheduleDays.disabled = true;
            form.cmbScheduleDays.value = "";
        }
    }

</script>
<table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
    <tr>
        <td align="left">
            <table border="0" cellspacing="0" cellpadding="0" align="left">
                <tr>
                    <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
                    <td class="SubSectionTitle" align="left" valign="top">Portabilidad Numérica - Alta</td>
                    <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"> &nbsp;</td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table border="0" width="100%" cellpadding="2" cellspacing="1" class="RegionBorder">
                <tr>
                    <td class="CellLabel" align="center" width="20%">Nombre Cedente</td>
                    <td class="CellContent" align="left" width="20%">
                        <select name="cmbAssignor">
                            <%=MiUtil.buildComboSelectedAssig(arrassignortList, MiUtil.getString((String) npAssignor))%>
                        </select>
                        <input type="text" name="txtAssignor" size="20" value="<%=npAssignorDesc%>" readonly>
                    </td>
                    <!--<td class="CellLabel" align="center" width="15%">Documentos Postpago</td> LROSALES-P1D -->
                    <!--td class="CellLabel" align="center" width="15%"></td-->
                    <!--td class="CellContent" align="center"  width="15%"-->
                    <!--<a href="javascript:fxLoadPortabilityAltaDoc();">Documentos </a> LROSALES-P1D-->
                    <!--/td-->
                    <td class="CellLabel" align="center" width="15%"><%=strTitleOrder%>
                    </td>
                    <%if (!MiUtil.getString(strOrderidParentChild).equals("")) {%>
                    <td class="CellContent" align="center" width="15%"><a
                            href="javascript:getShowParentPorta('<%=strOrderidParentChild%>')"><%=strOrderidParentChild%>
                    </a></td>
                    <%} else {%>
                    <td class="CellContent" width="15%">&nbsp;</td>
                    <%}%>
                    <input type=hidden name='nroItems' value="<%=canItems%>">
                    <%if ((lItems > 10) || (canItems > 10)) {%>
                    <td class="CellLabel" align="center" width="15%">Tipo Cliente</td>
                    <td class="CellContent" align="left" width="15%">
                        <%if (strCustomerType != null) {%>
                        <select name="cmbCustomerType" id="customer_type" onchange="javascript:fxShowScheduleDays();">
                            <%= MiUtil.buildComboSelected(arrCustomerType, "swvalue", "swdescription", strCustomerType)%>
                        </select>
                        <%} else {%>
                        <select name="cmbCustomerType" id="customer_type" onchange="javascript:fxShowScheduleDays();">
                            <%= MiUtil.buildCombo(arrCustomerType, "swvalue", "swdescription")%>
                        </select>
                        <%}%>
                    </td>
                    <%} else {%>
                    <td class="CellLabel" align="center" width="15%">Tipo Cliente</td>
                    <td class="CellContent" align="left" width="15%">
                        <select name="cmbCustomerType" id="customer_type" disabled>
                            <!--%= MiUtil.buildComboSelected(arrCustomerType,"swvalue","swdescription",Constante.CLIENTE_ESPECIAL_TIPON)%-->
                            <%= MiUtil.buildComboSelected(arrCustomerType, "swvalue", "swdescription", strCustomerType)%>
                        </select>
                        <input type=hidden name='cmbCustomerType' value="<%=strCustomerType%>">
                    </td>
                    <%}%>
                </tr>
                <tr height="20px">
                    <td class="CellLabel" align="center" width="20%">Tipo de Documento</td>
                    <td class="CellContent" align="left" width="20%">
                        <input type=hidden name='hdnTypeDocumento' value="<%=strTypeDocument%>">
                        <%if (strValue != null) {%>
                        <select name="cmbDocumento"
                                onchange="javascript:getShowDocumment('<%=strDocument%>','<%=strTypeDocument%>');">
                            <%= MiUtil.buildComboSelected(arrDocument, "swvalue", "swdescription", strTypeDocument)%>
                        </select>
                        <%} else {%>
                        <select name="cmbDocumento"
                                onchange="javascript:getShowDocumment('<%=strDocument%>','<%=strTypeDocument%>');"
                                disabled>
                            <%= MiUtil.buildComboSelected(arrDocument, "swvalue", "swdescription", strTypeDocument)%>
                        </select>
                        <%}%>
                    </td>
                    <td class="CellLabel" align="center" width="15%">Número de Documento</td>
                    <td class="CellContent" align="center" width="15%">
                        <input type=hidden name='hdnDocumento' value="<%=strDocument%>">
                        <%if (strValue != null) {%>
                        <input type="text" name="txtDocumento" size="15"
                               onchange="javascript:fxValidateDocument(trim(this.value),'<%=strDocument%>','<%=strTypeDocument%>');"
                               onKeyPress="return AcceptNumber(event)" value="<%=strDocument%>"></td>
                    <%} else {%>
                    <input type="text" name="txtDocumento" size="15"
                           onchange="javascript:fxValidateDocument(trim(this.value),'<%=strDocument%>','<%=strTypeDocument%>');"
                           onKeyPress="return AcceptNumber(event)" value="<%=strDocument%>" disabled></td>
                    <%}%>
                    <%if ((lItems > 10) || (canItems > 10)) {%>
                    <td class="CellLabel" align="center" width="15%">Dias para programar</td>
                    <td class="CellContent" align="left" width="15%">
                        <%if (strScheduleDays != null) {%>
                        <%if (strCustomerType.equals(Constante.CLIENTE_ESPECIAL_TIPOE)) {%>
                        <select name="cmbScheduleDays">
                            <%= MiUtil.buildComboSelected(arrScheduleDays, "swvalue", "swdescription", strScheduleDays)%>
                        </select>
                        <%} else {%>
                        <select name="cmbScheduleDays" disabled>
                            <%= MiUtil.buildComboSelected(arrScheduleDays, "swvalue", "swdescription", strScheduleDays)%>
                        </select>
                        <%}%>
                        <%} else {%>
                        <%if (strCustomerType.equals(Constante.CLIENTE_ESPECIAL_TIPOE)) {%>
                        <select name="cmbScheduleDays">
                            <%= MiUtil.buildComboSelected(arrScheduleDays, "swvalue", "swdescription", Constante.CLIENTE_ESPECIAL_DIASXDEFECTO)%>
                        </select>
                        <%} else {%>
                        <select name="cmbScheduleDays" disabled>
                            <%= MiUtil.buildCombo(arrScheduleDays, "swvalue", "swdescription")%>
                        </select>
                        <%}%>
                        <%}%>
                    </td>
                    <%} else {%>
                    <td class="CellLabel" align="center" width="15%">Dias para programar</td>
                    <td class="CellContent" align="left" width="15%">
                        <select name="cmbScheduleDays" disabled>
                            <%= MiUtil.buildCombo(arrScheduleDays, "swvalue", "swdescription")%>
                            <!--%= MiUtil.buildComboSelected(arrScheduleDays, "swvalue","swdescription",strScheduleDays)%-->
                        </select>
                        <!--input type=hidden name='cmbScheduleDays' value=""-->
                    </td>
                    <%}%>

                </tr>
            </table>
        </td>
    </tr>
</table>
<br>

<%
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Mensaje::::" + e.getMessage() + "<br>");
        System.out.println("Causa::::" + e.getCause() + "<br>");
        for (int i = 0; i < e.getStackTrace().length; i++) {
            System.out.println("    " + e.getStackTrace()[i] + "<br>");
        }
    }%>