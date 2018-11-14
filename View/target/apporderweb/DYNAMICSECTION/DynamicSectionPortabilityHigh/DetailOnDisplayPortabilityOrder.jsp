<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="pe.com.portability.service.PortabilityOrderService"%>
<%@ page import="pe.com.portability.bean.PortabilityParticipantBean"%>
<%@ page import="pe.com.portability.bean.PortabilityOrderBean"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="pe.com.nextel.service.SessionService"%>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="pe.com.portability.service.PortabilityGeneralService"%>
<%@ page import="pe.com.nextel.bean.PortalSessionBean"%>
<%@ page import="pe.com.nextel.bean.DominioBean"%>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%
    try{
        Hashtable hshParam=(Hashtable)request.getAttribute("hshtInputDetailSection");
        if (hshParam==null) hshParam=new Hashtable();
        long lCustomerId=MiUtil.parseLong((String)hshParam.get("strCustomerId"));
        long lSiteId=MiUtil.parseLong((String)hshParam.get("strSiteId"));
        long lOrderId=MiUtil.parseLong((String)hshParam.get("strOrderId"));
        String strSessionId=MiUtil.getString((String)hshParam.get("strSessionId"));
        String strDocumentini =MiUtil.getString((String)hshParam.get("strDocument"));
        String strTypeDocumentini =MiUtil.getString((String)hshParam.get("strTypeDocument"));
        String strStatus =MiUtil.getString((String)hshParam.get("strStatus"));

        // MMONTOYA [ADT-FMO-088 Fijo Móvil Fase I]
        int specificationId = Integer.parseInt((String)hshParam.get("strSpecificationId"));
        int divisionId = Integer.parseInt((String)hshParam.get("strDivisionId"));

        PortalSessionBean objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
        String strLogin=objPortalSesBean.getLogin();
        int lUserId = objPortalSesBean.getUserid();
        int valInbox = 0;

        String strMessage=null;
        String npOrderParent = "";
        ArrayList arrassignortList = null;
        ArrayList arrPortabOrderList = null;
        ArrayList arrUploadDoc = null;
        ArrayList arrOrderParentList = null;
        String ruta = "";
        HashMap hshAssignor = new HashMap();
        HashMap hshPortabOrder = new HashMap();
        HashMap hshUploadDoc = new HashMap();
        HashMap hshOrderNew = new HashMap();
        HashMap hshOrderParentChild = new HashMap();
        HashMap hshOrderParent = new HashMap();
        DominioBean dominio = new DominioBean();
        PortabilityParticipantBean objParticipantBean = null;
        PortabilityOrderBean objPortabOrderBean = new PortabilityOrderBean();
        PortabilityOrderBean objPortaParentChildBean= new PortabilityOrderBean();
        PortabilityOrderBean objPOBean = null;

        PortabilityOrderService objPortabilityOrderService = new PortabilityOrderService();
        PortabilityOrderService objPOSItemPort = new PortabilityOrderService();
        arrassignortList = new ArrayList();
        hshAssignor = (HashMap)objPortabilityOrderService.getParticipantList(specificationId, divisionId);
        arrassignortList = (ArrayList)hshAssignor.get("objParticipantList");

        arrPortabOrderList = new ArrayList();
        hshPortabOrder = (HashMap)objPortabilityOrderService.getPortabOrderList(lOrderId);
        arrPortabOrderList = (ArrayList)hshPortabOrder.get("objPortabOrderList");
        objPortabOrderBean = (PortabilityOrderBean)arrPortabOrderList.get(0);
        String npAssignor = (String)objPortabOrderBean.getNpAssignor();
        String strDocument = (String)objPortabOrderBean.getNpDocument();
        String strTypeDocument = (String)objPortabOrderBean.getNpTypeDocument();

        String strCustomerType = (String)objPortabOrderBean.getNpCustomerType();
        String strScheduleDays = (String)objPortabOrderBean.getNpScheduleDays();

        //Codigo para obtener el Id de la Orden Padre.
        //Inicio - DLAZO
        hshOrderParent = (HashMap)objPortabilityOrderService.getItemsPortabList(lOrderId);
        arrOrderParentList = new ArrayList();
        arrOrderParentList = (ArrayList)hshOrderParent.get("objItemList");
        objPOBean = new PortabilityOrderBean();

        if(arrOrderParentList.size()>0){
            objPOBean = (PortabilityOrderBean)arrOrderParentList.get(0);
            npOrderParent = (objPOBean.getNpOrderParentId()!=null?objPOBean.getNpOrderParentId():"");
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

        hshUploadDoc = (HashMap)objPortabilityOrderService.getDominioList("PORTAB_UPLOAD_HIGH");
        arrUploadDoc = new ArrayList();
        arrUploadDoc = (ArrayList)hshUploadDoc.get("arrDominioList");
        if(arrUploadDoc.size()>0){
            dominio = (DominioBean)arrUploadDoc.get(0);
            ruta = (String)dominio.getDescripcion();
            System.out.println("Ruta Upload Portabilidad "+ruta);
        }
        //Si la orden tiene orderParentId, obtiene el orderParentId de lo contrario el orderId.
        hshOrderNew = (HashMap)objPortabilityOrderService.getValOrdenHija(lOrderId);
        long lOrderIdNew =MiUtil.parseLong((String)hshOrderNew.get("lngValor"));

        //Encontramos la referencia de la Orden si es Hija ó Padre
        //--------------------------------------------------------
        hshOrderParentChild  = (HashMap)objPortabilityOrderService.getParentChildOrder(lOrderId);
        strMessage         =  (String)hshOrderParentChild.get("strMessage");
        if (strMessage!=null)
            throw new Exception(strMessage);

        objPortaParentChildBean=(PortabilityOrderBean)hshOrderParentChild.get("objParentChildList");
        String strTitleOrder= objPortaParentChildBean.getNpTitleOrdeParentChild()==null?"":objPortaParentChildBean.getNpTitleOrdeParentChild();
        long lnOrderidParentChild = objPortaParentChildBean.getNpOrderidParentChild();
        String strOrderidParentChild = MiUtil.getString(lnOrderidParentChild)=="0"?"":MiUtil.getString(lnOrderidParentChild);

        //Obtiene el nombre del inbox de la orden
        //----------------------------------------
        HashMap hshStatus = (HashMap)objPOSItemPort.getStatusOrder(lOrderId);
        String npStatus = ((String)hshStatus.get("strStatus")).trim();

        if(npStatus.equals("PORTABILIDAD_NUMERICA")){
            valInbox = objPOSItemPort.getValidInboxContent(lUserId,npStatus,1);
        }

        //Obtiene los valores del combo Documentos
        //-----------------------------------------
        HashMap hshDocument = objPortabilityOrderService.getDocumentList("DOCUMENT_CUSTOMER");
        strMessage=(String)hshDocument.get("strMessage");
        if (strMessage!=null)
            throw new Exception(strMessage);
        ArrayList arrDocument=(ArrayList)hshDocument.get("arrDocumentList");

        //Obtiene los valores del combo tipo de cliente
        //-----------------------------------------
        HashMap hshCustomerType = objPortabilityOrderService.getDocumentList("CUSTOMER_TYPE");
        strMessage=(String)hshCustomerType.get("strMessage");
        if (strMessage!=null)
            throw new Exception(strMessage);
        ArrayList arrCustomerType=(ArrayList)hshCustomerType.get("arrDocumentList");

        //Obtiene los valores del combo dias para programar
        //-----------------------------------------
        HashMap hshScheduleDays = objPortabilityOrderService.getDocumentList("DAYS_TO_SCHEDULE");
        strMessage=(String)hshScheduleDays.get("strMessage");
        if (strMessage!=null)
            throw new Exception(strMessage);
        ArrayList arrScheduleDays=(ArrayList)hshScheduleDays.get("arrDocumentList");

        //Verifica si la seccion de Documentos puede visualizarse
        //-------------------------------------------------------
        String strValue=null;
        PortabilityGeneralService objPortabilityGeneralService = new PortabilityGeneralService();
        HashMap hshPermissionDocument = objPortabilityGeneralService.getSectionDocumentValidate("SECTION_DOCUMENT",strStatus,strValue);
        strMessage=(String)hshPermissionDocument.get("strMessage");
        if (strMessage!=null)
            throw new Exception(strMessage);
        strValue = (String)hshPermissionDocument.get("strValue");

%>

<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXEdit.js"></script>
<script type="text/javascript" >
    function fxLoadPortabilityAltaDoc() {
        var vForm = document.frmdatos;
        var strCustomerName = vForm.hdnCustName.value;
        <%
        if(valInbox == 1){
          if(npOrderParent != ""){%>
        var strUrl = "<%=ruta%>/Document.jsp?strIdClient=<%=lCustomerId%>&strNameClient="+strCustomerName+"&strIdOrder=<%=npOrderParent%>&strCreator=PORTABILITY&strView=Editar";
        var winUrl = "<%=request.getContextPath()%>/DYNAMICSECTION/DynamicSectionPortabilityHigh/PopUpOrderFrame.jsp?av_url="+escape(strUrl);
        window.open(winUrl, "Portabilidad_Alta","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,screenX=60,top=40,left=60,screenY=40,width=850,height=600");
        <%}else{%>
        var strUrl = "<%=ruta%>/Document.jsp?strIdClient=<%=lCustomerId%>&strNameClient="+strCustomerName+"&strIdOrder=<%=lOrderId%>&strCreator=PORTABILITY&strView=Editar";
        var winUrl = "<%=request.getContextPath()%>/DYNAMICSECTION/DynamicSectionPortabilityHigh/PopUpOrderFrame.jsp?av_url="+escape(strUrl);
        window.open(winUrl, "Portabilidad_Alta","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,screenX=60,top=40,left=60,screenY=40,width=850,height=600");
        <%}
        }else{
          if(npOrderParent != ""){%>
        var strUrl = "<%=ruta%>/Document.jsp?strIdClient=<%=lCustomerId%>&strNameClient="+strCustomerName+"&strIdOrder=<%=npOrderParent%>&strCreator=PORTABILITY&strView=Consult";
        var winUrl = "<%=request.getContextPath()%>/DYNAMICSECTION/DynamicSectionPortabilityHigh/PopUpOrderFrame.jsp?av_url="+escape(strUrl);
        window.open(winUrl, "Portabilidad_Alta","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,screenX=60,top=40,left=60,screenY=40,width=850,height=600");
        <%}else{%>
        var strUrl = "<%=ruta%>/Document.jsp?strIdClient=<%=lCustomerId%>&strNameClient="+strCustomerName+"&strIdOrder=<%=lOrderId%>&strCreator=PORTABILITY&strView=Consult";
        var winUrl = "<%=request.getContextPath()%>/DYNAMICSECTION/DynamicSectionPortabilityHigh/PopUpOrderFrame.jsp?av_url="+escape(strUrl);
        window.open(winUrl, "Portabilidad_Alta","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,screenX=60,top=40,left=60,screenY=40,width=850,height=600");
        <%}
        }%>
    }

    function getShowDocumment (strDocument,strTypeDocument){

        var vForm = document.frmdatos;

        if(strTypeDocument==vForm.cmbDocumento.value ){
            vForm.txtDocumento.value =strDocument;
            vForm.cmbDocumento.value =strTypeDocument;
            // vForm.txtDocumento.disabled=true;
        }else{
            vForm.txtDocumento.value = "";
            // vForm.txtDocumento.disabled=false;
        }
    }

    function fxValidateDocument(objValue,strDocument,strTypeDocument){

        form = document.frmdatos;
        v_typedoc = form.cmbDocumento.value;
        
        if ((objValue.length)!=0){
          if( (objValue.length)!=8 && (v_typedoc=="DNI") ){
              alert("Debe ingresar un numero de 8 digitos")
              if(strTypeDocument != v_typedoc){
                  form.txtDocumento.value="";
              }else{
                  form.txtDocumento.value=strDocument;
              }
          }
          if ( (objValue.length)!=11 && (v_typedoc=="RUC") ){
              alert("Debe ingresar un numero de 11 digitos")
              if(strTypeDocument != v_typedoc){
                  form.txtDocumento.value="";
              }else{
                  form.txtDocumento.value=strDocument;
              }
          }
        }else{
          alert("Debe ingresar un numero de documento");
          form.txtDocumento.focus();
          return false;
        }
        
        return true;
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
            <table  border="0" width="100%" cellpadding="2" cellspacing="1" class="RegionBorder">
                <tr>
                    <td class="CellLabel" align="center" width="20%">Nombre Cedente
                    </td>
                    <td class="CellContent" align="center" width="20%">
                        <select name = "cmbAssignor" disabled align="left" width="15%">
                            <%=MiUtil.buildComboSelectedAssig(arrassignortList, MiUtil.getString((String) npAssignor))%>
                        </select>
                    </td>
                    <!--<td class="CellLabel" align="center" width="15%">Documentos Postpago</td>-->
                    <!--td class="CellLabel" align="center" width="15%">&nbsp;</td-->
                    <!--td class="CellContent" align="center" width="15%">&nbsp;-->
                    <!--<a href="javascript:fxLoadPortabilityAltaDoc();">
                    Documentos
                    </a>-->
                    <!--/td-->
                    <td class="CellLabel" align="center" width="15%"><%=strTitleOrder%></td>
                    <%if ( !MiUtil.getString(strOrderidParentChild).equals("") ){%>
                    <td class="CellContent" align="center" ><a href="javascript:getShowParentPorta('<%=strOrderidParentChild%>')"><%=strOrderidParentChild%></a></td>
                    <%}else{%>
                    <td class="CellContent" width="15%">&nbsp;</td>
                    <%}%>
                    <td class="CellLabel" align="center" width="15%">Tipo Cliente</td>
                    <td class="CellContent" align="left" width="15%">
                        <select name = "cmbCustomerType" disabled>
                            <%= MiUtil.buildComboSelected(arrCustomerType,"swvalue","swdescription",strCustomerType)%>
                        </select></td>
                </tr>
                <tr height="20px">
                    <td class="CellLabel" align="center" width="15%">Tipo de Documento</td>
                    <td class="CellContent" align="left" width="15%">
                        <input type=hidden name='hdnTypeDocumento'  value="<%=strTypeDocument%>">
                        <%if (strValue!=null && valInbox ==1){%>
                        <select name = "cmbDocumento" onchange="javascript:getShowDocumment('<%=strDocument%>','<%=strTypeDocument%>');" >
                            <%= MiUtil.buildComboSelected(arrDocument,"swvalue","swdescription",strTypeDocument)%>
                        </select>
                        <%}else{%>
                        <select name = "cmbDocumento" onchange="javascript:getShowDocumment('<%=strDocument%>','<%=strTypeDocument%>');" disabled >
                            <%= MiUtil.buildComboSelected(arrDocument,"swvalue","swdescription",strTypeDocument)%>
                        </select>
                        <%}%>
                    </td>
                    <td class="CellLabel" align="center" width="15%">Número de Documento</td>
                    <td class="CellContent">
                        <input type=hidden name='hdnDocumento'  value="<%=strDocument%>">
                        <%if (strValue!=null && valInbox==1){%>
                        <input type="text" name="txtDocumento" size="15"  onchange="javascript:fxValidateDocument(trim(this.value),'<%=strDocument%>','<%=strTypeDocument%>');"  onKeyPress="return AcceptNumber(event)"  value="<%=strDocument%>" ></td>
                    <%}else{%>
                    <input type="text" name="txtDocumento" size="15"  onchange="javascript:fxValidateDocument(trim(this.value),'<%=strDocument%>','<%=strTypeDocument%>');"  onKeyPress="return AcceptNumber(event)"  value="<%=strDocument%>" disabled></td>
                    <%}%>
                    <td class="CellLabel" align="center" width="15%">Dias para programar</td>
                    <td class="CellContent" align="left" width="15%">
                        <%if (strScheduleDays!=null){%>
                        <select name = "cmbScheduleDays" disabled>
                            <%= MiUtil.buildComboSelected(arrScheduleDays,"swvalue","swdescription",strScheduleDays)%>
                        </select>
                        <%}else {%>
                        <select name = "cmbScheduleDays" disabled>
                            <%= MiUtil.buildCombo(arrScheduleDays, "swvalue","swdescription")%>
                        </select>
                        <%}%>
                    </td>
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

<script DEFER>

    function getShowParentPorta(strOrderId){
        var strUrl="/portal/page/portal/orders/ORDER_DETAIL?an_nporderid="+strOrderId;
        var  url = "/portal/pls/portal/WEBSALES.NPSL_GENERAL_PL_PKG.WINDOW_FRAME?av_title="+escape("PORTAL NEXTEL")+"&av_url="+escape(strUrl);
        window.open(url,"WinCustomer","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,screenX=60,top=40,left=60,screenY=40,width=850,height=600");
        return;
    }
</script>