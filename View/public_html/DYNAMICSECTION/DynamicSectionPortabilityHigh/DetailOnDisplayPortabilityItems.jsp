<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.portability.service.PortabilityOrderService"%>
<%@ page import="pe.com.portability.bean.PortabilityOrderBean"%>
<%@ page import="pe.com.nextel.bean.DominioBean"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="pe.com.nextel.service.SessionService"%>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="pe.com.nextel.bean.PortalSessionBean"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.text.DecimalFormat"%>


<%
  try{
  
    System.out.println("****************************************************************");
    System.out.println("DetailOnDisplayPortabilityItems.jsp");
    System.out.println("****************************************************************");

    Hashtable hshParam=(Hashtable)request.getAttribute("hshtInputDetailSection");  
    if (hshParam==null) hshParam=new Hashtable();
    long lCustomerId=MiUtil.parseLong((String)hshParam.get("strCustomerId"));
    long lSiteId=MiUtil.parseLong((String)hshParam.get("strSiteId"));       
    long lOrderId=MiUtil.parseLong((String)hshParam.get("strOrderId"));         
    String strSessionId=MiUtil.getString((String)hshParam.get("strSessionId"));  
         
    PortalSessionBean objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
    String strLogin=objPortalSesBean.getLogin();
    
    int  iUserId  = objPortalSesBean.getUserid();   
    int  iAppId   = objPortalSesBean.getAppId();  
      
    
    String strMessage=null;
    HashMap hshItemPortab = new HashMap();
    HashMap hshModalidadCont = new HashMap();
    HashMap hshPortabItem = new HashMap();
    ArrayList arrItemPortabList = null;
    ArrayList arrmodalityContList = null;
    ArrayList arrPortabItemList = null;
    ArrayList arrSubsanacionList = null;
    HashMap hshOrderPortability=null;
    HashMap hshScreenField=null;

    PortabilityOrderService objPOSItemPort = new PortabilityOrderService();
    PortabilityOrderService objPOSPortabItem = new PortabilityOrderService();
    PortabilityOrderService objPOSModality = new PortabilityOrderService();
    PortabilityOrderService objPSInbox = new PortabilityOrderService();
    PortabilityOrderService objPortaOrderService = new PortabilityOrderService();
    
    GeneralService   objGeneralService   = new GeneralService();  
    PortabilityOrderBean objPOBean = null;
    PortabilityOrderBean objPODevBean = null;
    PortabilityOrderBean objPOrderBean = null;
    DominioBean objDominioBean = null;
    
    String npPosition = "";
    String npPortabOrderId = "";
    String npItemId = "";
    String npItemDeviceId = "";
    String npAplicationId = "";
    String npPhoneNumber = "";
    String npModalityCont = "";
    String npShippingDate = "";
    String npLastState = "";
    String npErrorInteg = "";
    String npReasonReject = "";
    String npScheduleDead = "";
    String npExecDeadLine = "";
    String npExecutionDate = "";
    String npCorrected = "";
    String npState = "";
    String npOrderParent = "";
    String npContract = "";
    
    HashMap hshStatus = (HashMap)objPOSItemPort.getStatusOrder(lOrderId);
    String npStatus = ((String)hshStatus.get("strStatus")).trim();
    
    if(npStatus.equals("TIENDA01") || npStatus.equals("ADM_VENTAS") || npStatus.equals("VENTAS")){
      //Obtengo los Items de la np_Item
      hshItemPortab = (HashMap)objPOSItemPort.getItemsPortabList(lOrderId); //1 2554386
      arrItemPortabList = new ArrayList();
      arrItemPortabList = (ArrayList)hshItemPortab.get("objItemList");
    }else{
      //Obtengo los Items de la np_portability_item
      hshItemPortab = (HashMap)objPOSItemPort.getItemsPortLst(lOrderId);
      arrItemPortabList = new ArrayList();
      arrItemPortabList = (ArrayList)hshItemPortab.get("objItemList");
    }
  
    if(arrItemPortabList.size()>0){
      objPOrderBean = new PortabilityOrderBean();
      objPOrderBean = (PortabilityOrderBean)arrItemPortabList.get(0);
      npOrderParent = (objPOrderBean.getNpOrderParentId()!=null?objPOrderBean.getNpOrderParentId():"");
    }
    
    HashMap hshInboxDoc = objPSInbox.getInboxOrder(lOrderId);
    String strInbox = (String)hshInboxDoc.get("strInboxStatus");
    
    //Evaluamos ScreenOption para visualizar los botones:
    //---------------------------------------------------
     int iPermission = 0;
     HashMap hshDataPermission  = null;
     hshDataPermission  = objGeneralService.getRol(Constante.SCRN_OPTTO_MESSAGE_PORTAB, iUserId, iAppId);
     iPermission  = MiUtil.parseInt((String)hshDataPermission.get("iRetorno"));
     
    //Manejo dinamico de controles   
    //----------------------------
    hshOrderPortability=objPortaOrderService.getOrderScreenFieldPorta(lOrderId,Constante.PAGE_ORDER_DETAIL);
    strMessage=(String)hshOrderPortability.get("strMessage");
    if (strMessage!=null)
       throw new Exception(strMessage);
    
    hshScreenField= (HashMap)hshOrderPortability.get("hshData");   

    //Ini TMOGROVEJO 23/11/2009
    int intValidationInboxUser = objPOSPortabItem.getValInboxEditableUser(npStatus,strLogin );
    //Fin TMOGROVEJO 23/11/2009
    int resultSP = objPOSPortabItem.getFlagSendSP(lOrderId);
    int resultPP = objPOSPortabItem.getFlagSendPP(lOrderId);
%>

<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></script>
<script type="text/javascript" defer>
  
  
     loadSection();       
     
     function loadSection(){
       var vForm = document.frmdatos;        
       vForm.hdntblListaPortabilityHigh.value = tableItemsPortab.rows.length; 
       var numFilas = eval(tableItemsPortab.rows.length);
       var npestado = vForm.txtEstadoOrden.value;
       if(npestado != 'TIENDA01' && npestado != 'ADM_VENTAS' && npestado != 'VENTAS'){
        if (numFilas == 2){
          vForm.txtphonenumber.readOnly = true;
          if(vForm.hdnOrdenParentId.value == ''){
            dvByTxtModalityCont.style.display="";
            dvByCmbModalityCont.style.display="none";
          }else{
            dvByTxtModalityCont.style.display="none";
            dvByCmbModalityCont.style.display="";
          }
        }else if(numFilas > 2){
          for(var i=0; i<numFilas-1; i++){
             vForm.txtphonenumber[i].readOnly = true;
             if(vForm.hdnOrdenParentId.value == ''){
              dvByTxtModalityCont[i].style.display="";
              dvByCmbModalityCont[i].style.display="none";
             }else{
              dvByTxtModalityCont[i].style.display="none";
              dvByCmbModalityCont[i].style.display="";
             }
          }
        }
       }else{
        if (numFilas == 2){
          dvByTxtModalityCont.style.display="none";
          dvByCmbModalityCont.style.display="";
        }else if(numFilas > 2){
          for(var i=0; i<numFilas-1; i++){
             dvByTxtModalityCont[i].style.display="none";
             dvByCmbModalityCont[i].style.display="";
          }
        }
       }
     }

  
  
  function fxSendMessage(messageType){
    form = document.frmdatos;
    var url = "<%=request.getContextPath()%>/portabilityOrderServlet?strOrderId=<%=lOrderId%>&strLogin=<%=strLogin%>&strCustomerId=<%=lCustomerId%>&messageType="+messageType+"&strPortabilityType=ALTA&hdnMethod=sendPortabilityMessages";
    parent.bottomFrame.location.replace(url);
  }
     
  function fxSubsanarDeuda(phoneNumber, applicationId, portabItemId, amountDue, currencyType, currencyDesc, npAssignorId){
      var url = "<%=request.getContextPath()%>/DYNAMICSECTION/DynamicSectionPortabilityHigh/SubSanacionPortability.jsp?npPhoneNumber=" + phoneNumber +
              "&npAplicationId=" + applicationId + "&npPortabItemId=" + portabItemId + "&npAmountDue=" + amountDue + "&npCurrencyType=" + currencyType +
              "&npCurrencyDesc=" + currencyDesc + "&npAssignorId=" + npAssignorId;
      window.open(url,"WinListPagos","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=yes,screenX=100,top=80,left=150,screenY=80,width=700,height=500,modal=yes");
  };
     
   function fxSavePortabilityStatus(){
      var vForm = document.frmdatos; 
      var numFilas = eval(tableItemsPortab.rows.length);      
      if (numFilas == 2){            
        if ( (vForm.cmbmodalidadcont.value == "") && (vForm.cmbCorrected.value!= "i")  ){
           alert("Debe seleccionar la Modalidad Origen");
           vForm.cmbmodalidadcont.focus();
           return;
        }                        
        var strItemId = vForm.hddItemId.value;
        var strItemDeviceId = vForm.hdnItemDeviceId.value;
        var strModalidadCont = vForm.cmbmodalidadcont.value;
        var strStatusPortab = vForm.cmbCorrected.value;
        var numberFilas = 1;
        var indice = 1;
        var cadena = strItemDeviceId+"-"+strStatusPortab+"-"+numberFilas+"-"+indice+"-"+strModalidadCont+"-"+strItemId;                                                           
        vForm.arrStatus.value=cadena;
        vForm.target="bottomFrame";        
        /*Ini Se agrego la orden como parametro TM 16/11/2009 */
        vForm.action="<%=request.getContextPath()%>/portabilityOrderServlet?hdnMethod=updateStatusPortabilityItem&numFilas="+numberFilas  +"&strOrderId=<%=lOrderId%>";                           
        /*Fin Se agrego la orden como parametro TM 16/11/2009 */
        vForm.submit();
      }else if(numFilas > 2){
        for(var i=0; i<numFilas-1; i++){
          if ( (vForm.cmbmodalidadcont[i].value == "" ) && (vForm.cmbCorrected[i].value!= "i") ){
             alert("Debe seleccionar la Modalidad Origen");
             vForm.cmbmodalidadcont[i].focus();
             return;
          }                            
          var strItemId = vForm.hddItemId[i].value;
          var strItemDeviceId = vForm.hdnItemDeviceId[i].value;
          var strModalidadCont = vForm.cmbmodalidadcont[i].value;
          var strStatusPortab = vForm.cmbCorrected[i].value;
          var indice = i + 1;
          var numberFilas = numFilas - 1;
          var cadena = strItemDeviceId+"-"+strStatusPortab+"-"+numberFilas+"-"+indice+"-"+strModalidadCont+"-"+strItemId;
          vForm.arrStatus[i].value=cadena;
        }
        vForm.target="bottomFrame";
        vForm.action="<%=request.getContextPath()%>/portabilityOrderServlet?hdnMethod=updateStatusPortabilityItem";
        vForm.submit();
      }
      
    }
   
</script>

  <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
    <input type="hidden" name="hdntblListaPortabilityHigh">
    
    <%if (iPermission==1){%>
    <tr align="center">
      <td>
        <table border="0" cellspacing="0" cellpadding="0" align="left">
          <tr>
            <td>
                 <%if ("Enabled".equals(MiUtil.getString((String)hshScreenField.get("sendsp"))) && (resultSP!=1)  ){
                 %>
                  <input type="button" name="btnSendMessagesSP" value="Envio SP" onclick="fxSendMessage('SP')"/>
                 <%}%>
            </td>
            <td>
               <%if ("Enabled".equals(MiUtil.getString((String)hshScreenField.get("sendpp"))) && (resultPP!=1) ){%>
                  <input type="button" name="btnSendMessagesPP" value="Envio PP" onclick="fxSendMessage('PP')"/>
                <%}%>
            </td>
            <!--<td>
                <%if ("Enabled".equals(MiUtil.getString((String)hshScreenField.get("sendsvp"))) ){%>
                  <input type="button" name="btnSendMessagesSVP" value="Envio SVP" onclick="fxSendMessage('SVP')"/>
                <%}%>
            </td>-->
          </tr>
        </table>
        <!-- Comentado por P1D - LROSALES
        <table border="0" cellspacing="0" cellpadding="0" align="right">
          <tr>
            <td>
               <%if ("Enabled".equals(MiUtil.getString((String)hshScreenField.get("createsubsanacion"))) ){%>
                <input type="button" name="btnSavePortabStatus" value="Grabar Subsanación" onclick="fxSavePortabilityStatus()"/>
                <%}%>  
            </td>
          </tr>
        </table>
        -->
      </td>
    </tr>
    <%}%>
    <tr>
      <td align="left">
        <table border="0" cellspacing="0" cellpadding="0" align="left">
          <tr>
            <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
            <td class="SubSectionTitle" align="left" valign="top">Portabilidad Numérica - Alta - Detalle</td>
            <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"> &nbsp;</td>
             <input type="hidden" name="hdnLogin" value="">
             <input type="hidden" name="hdnCustomerId" value="">
             <input type="hidden" name="hdnOrdenParentId" value="<%=npOrderParent%>">
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td align="center">         
        <table id="tableItemsPortab" border="0" cellspacing="1" cellpadding="2" width="100%" class="RegionBorder">
          <tr>
            <td class="CellLabel" align="center" width="5%">&nbsp;N°</td>               
            <td class="CellLabel" align="center" width="10%">&nbsp;Teléfono</td>
            <td class="CellLabel" align="center" width="10%">&nbsp;Modalidad Origen</td>
            <td class="CellLabel" align="center" width="6%">&nbsp;Id. Solicitud</td>
            <td class="CellLabel" align="center" width="20%">&nbsp;Est. Portabilidad</td>               
            <td class="CellLabel" align="center" width="20%">&nbsp;Error Integridad</td>               
            <td class="CellLabel" align="center" width="20%">&nbsp;Motivo Rechazo</td>
            <!--LROSALES-P1D-->
            <td class="CellLabel" align="center" width="20%">&nbsp;Monto Adeudado</td>
            <td class="CellLabel" align="center" width="20%">&nbsp;Tipo Moneda</td>
            <td class="CellLabel" align="center" width="20%">&nbsp;Fec. Venc. Ult. Recibo</td>
            <td class="CellLabel" align="center" width="20%">&nbsp;Fecha de Ejecución</td>
            <td class="CellLabel" align="center" width="20%">&nbsp;Subsanacion Deuda</td>
            <!--LROSALES-P1D-->
            <!--
            <td class="CellLabel" align="center" width="5%">&nbsp;Fec. Lim. Prog.</td>					
            <!--<td class="CellLabel" align="center" width="5%">&nbsp;Fec. Lim. Ejec.</td>
            <td class="CellLabel" align="center" width="5%">&nbsp;Fecha Ejecución</td>
            <td class="CellLabel" align="center" width="5%">&nbsp;Programación BSCS</td>
            
            <!-- Se agrego campo de telefono en formato world number -->             
            <!--<td class="CellLabel" align="center" width="5%">Num. a Programar</td>   -->                                 
            <!--
           <%
            if(!npOrderParent.equals("")){
          %>
            <td class="CellLabel" align="center" width="6%">&nbsp;Subsanación</td>
         <%}%>-->
          </tr>
          <%
            if(arrItemPortabList.size()>0){
              for(int i=0; i<arrItemPortabList.size(); i++){
                objPOBean = new PortabilityOrderBean();
                objPOBean = (PortabilityOrderBean)arrItemPortabList.get(i);
                
                if(npStatus.equals("TIENDA01") || npStatus.equals("ADM_VENTAS") || npStatus.equals("VENTAS")){
                
                  if((!((String)objPOBean.getNpModalitySell()).equals("Propio") && !((String)objPOBean.getNpModalitySell()).equals("Alquiler en Cliente")) && objPOBean.getNpProductLineId() != 11){
                    //Obtengo los Items Device de la np_Portability_Item
                    hshPortabItem = (HashMap)objPOSPortabItem.getPortabItemDevList1(objPOBean.getNpItemid()); //2
                    arrPortabItemList = new ArrayList();
                    arrPortabItemList = (ArrayList)hshPortabItem.get("objArrayList");
                    
                  }else{
                    //Obtengo los Items Device de la np_Portability_Item
                    hshPortabItem = (HashMap)objPOSPortabItem.getPortabItemList1(objPOBean.getNpItemid()); //2
                    arrPortabItemList = new ArrayList();
                    arrPortabItemList = (ArrayList)hshPortabItem.get("objArrayList");
                  }
                  
                }else{
                
                  hshPortabItem = (HashMap)objPOSPortabItem.getPortabItemPortabList(objPOBean.getNpItemid()); //2
                  arrPortabItemList = new ArrayList();
                  arrPortabItemList = (ArrayList)hshPortabItem.get("objArrayList");
                
                }
                
                if(arrPortabItemList.size() > 0){                
                  for(int j=0; j<arrPortabItemList.size(); j++){
                    objPODevBean = new PortabilityOrderBean();
                    objPODevBean = (PortabilityOrderBean)arrPortabItemList.get(j);                    
                    npPosition = (i+1) + "-" + (j+1);                    
                    npContract = (objPODevBean.getNpContract()!=null?objPODevBean.getNpContract():"");
                    npModalityCont = (objPODevBean.getNpModalityContract()!=null?objPODevBean.getNpModalityContract():"");
                    
          %>
          <tr>
            <td class="CellContent" align="center">
              <input type="hidden" name="arrStatus">
              <input type="hidden" name="hddPortabOrderid" value="<%=objPODevBean.getNpPortabOrderId()%>">
              <input type="hidden" name="hddItemId" value="<%=objPODevBean.getNpItemid()%>">
              <%
              if(npStatus.equals("TIENDA01") || npStatus.equals("ADM_VENTAS") || npStatus.equals("VENTAS")){
                if((!((String)objPOBean.getNpModalitySell()).equals("Propio") && !((String)objPOBean.getNpModalitySell()).equals("Alquiler en Cliente")) && objPOBean.getNpProductLineId() != 11){
              %>
                <input type="hidden" name="hdnItemDeviceId" value="<%=objPODevBean.getNpItemDeviceId()%>">
                <input type="text" name="txtPosition" size="3" value="<%=npPosition%>" style="text-align:center;border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent" readonly>
              </td> 
              <%
                }else{
              %>
                <input type="hidden" name="hdnItemDeviceId" value="">
                <input type="text" name="txtPosition" size="3" value="<%=npPosition%>" style="text-align:center;border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent" readonly>
              </td> 
              <%}
              }else{
              %>
                <input type="hidden" name="hdnItemDeviceId" value="<%=objPODevBean.getNpItemDeviceId()%>">
                <input type="text" name="txtPosition" size="3" value="<%=npPosition%>" style="text-align:center;border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent" readonly>
              </td>
              <%
              }%>
            <!--Campo Teléfono-->
            <td class="CellContent" align="center">
              <input type="text" name="txtphonenumber" size="12" value="<%=(objPODevBean.getNpPhoneNumber()!=null?objPODevBean.getNpPhoneNumber():"")%>" style="text-align:center;border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent" readonly>
            </td>                     
            <!--<td class="CellContent" align="center"><input type="hidden" name="cmbmodalidadcont" value="<%=objPODevBean.getNpModalityContract()%>">
                                                   <%=(String)(objPODevBean.getNpContract()!=null?objPODevBean.getNpContract():"")%></td>-->            
            <%            
              int intValMsgSub = objPOSPortabItem.getValMsgSub(lOrderId,objPODevBean.getNpItemid());
              if(!npOrderParent.equals("") && strInbox.equals("PORTABILIDAD_NUMERICA")&& (iPermission==1) && (intValMsgSub==1)  ){
              hshModalidadCont = (HashMap)objPOSModality.getDominioList("PORTABILITY_CONTRACT_MODALITY");
              arrmodalityContList = (ArrayList)hshModalidadCont.get("arrDominioList");
              strMessage = (String) hshModalidadCont.get(Constante.MESSAGE_OUTPUT);
            %>
            <!--Campo Modalidad Origen-->
            <td class="CellContent" align="center">
              <div id="dvByCmbModalityCont" style=display:'none'>
                <select name="cmbmodalidadcont">
                <%=MiUtil.buildComboSelected(arrmodalityContList, MiUtil.getString((String) npModalityCont))%>
                </select>
              </div> 
              <div id="dvByTxtModalityCont" style=display:'none'>
                <input type="text" name="txtmodalidadcont" size="15" value="<%=npContract%>" style="text-align:center;border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent" readonly>
              </div> 
              <input type="hidden" name="hdnmodalidadcont" value="<%=npModalityCont%>">
            </td>
             <%
              }else{
              %>
                <td class="CellContent" align="center">
                  <div id="dvByCmbModalityCont" style=display:'none'>
                  <input type="hidden" name="cmbmodalidadcont" value="<%=objPODevBean.getNpModalityContract()%>">
                                                   <%=(String)(objPODevBean.getNpContract()!=null?objPODevBean.getNpContract():"")%>
                  </div> 
                   <div id="dvByTxtModalityCont" style=display:'none'>
                    <input type="text" name="txtmodalidadcont" size="15" value="<%=npContract%>" style="text-align:center;border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent" readonly>
                  </div> 
              </td>
              <%
              }
              %>
            <!--Campo Id. Solicitud-->
            <td class="CellContent" align="center">
              <input type="text" name="txtAplicationId" value="<%=(String)(objPODevBean.getNpApplicationId()!=null?objPODevBean.getNpApplicationId():"")%>" style="text-align:center;border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent" readonly>
            </td>
            <!--Campo Estado Portabilidad-->
            <td class="CellContent" align="center"><%=(objPODevBean.getNpLastStateDesc()!=null?objPODevBean.getNpLastStateDesc():"")%></td>               
            <!--Campo Error Integridad-->
            <td class="CellContent" align="center"><%=(objPODevBean.getNpErrorIntegrityDesc()!=null?objPODevBean.getNpErrorIntegrityDesc():"")%></td>               
            <!--Campo Motivo Rechazo-->
            <td class="CellContent" align="center"><%=(objPODevBean.getNpReasonRejectionDesc()!=null?objPODevBean.getNpReasonRejectionDesc():"")%></td>
            <!--Campo Monto Adeudado-->
            <td class="CellContent" align="center"><%= new DecimalFormat("#.00").format(objPODevBean.getNpAmountDue())%></td>
            <!--Campo Tipo Moneda-->
            <td class="CellContent" align="center"><%=MiUtil.getString(objPODevBean.getNpCurrencyDesc())%></td>
            <!--Campo Fec. Venc. Ult. Recibo-->
            <td class="CellContent" align="center"><%=MiUtil.getDate(objPODevBean.getNpExpirationDateReceipt(),"dd/MM/yyyy")%></td>
            <!--Campo Fecha Ejecución-->
            <td class="CellContent" align="center"><%=objPODevBean.getNpExecutionDate()%></td>
            <!--Campo Subsanacion Deuda-->
            <%
              if(npStatus.equals("PORTABILIDAD_NUMERICA") && Constante.MOTIVO_RECHAZO_DEUDA.equals(objPODevBean.getNpReasonRejection())){%>
                <td class="CellContent" align="center">&nbsp;<a href="javascript:fxSubsanarDeuda('<%=objPODevBean.getNpPhoneNumber()%>','<%=objPODevBean.getNpApplicationId()%>','<%=objPODevBean.getNpPortabItemId()%>','<%=objPODevBean.getNpAmountDue()%>','<%=objPODevBean.getNpCurrencyType()%>','<%=objPODevBean.getNpCurrencyDesc()%>','<%=objPODevBean.getNpAssignor()%>');" >Subsanar Deuda</a></td>
              <%} else{%>
                <td class="CellContent" align="center"></td>
              <%}
            %>
            
            <!--<td class="CellContent" align="center">
              <input type="text" name="txtExecDeadLine" value="<%=(objPODevBean.getNpExecutionDeadline()!=null?objPODevBean.getNpExecutionDeadline():"")%>" style="text-align:center;border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent" readonly>
            </td>-->
            <!--<td class="CellContent" align="center">
              <input type="text" name="txtExecutionDate" value="<%=(objPODevBean.getNpExecutionDate()!=null?objPODevBean.getNpExecutionDate():"")%>" style="text-align:center;border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent" readonly>
            </td>
            <td class="CellContent" align="center"><%=objPODevBean.getNpScheduleDBscs()%></td>            
            <td class="CellContent" align="center"><%=objPODevBean.getNpPhoneNumberWN()%></td>
            -->
          <!--
          <%
            if(!npOrderParent.equals("")){   
                     
            hshModalidadCont = (HashMap)objPOSModality.getDominioList("PORTAB_CORRECTED");
            arrSubsanacionList = (ArrayList)hshModalidadCont.get("arrDominioList");
            strMessage = (String) hshModalidadCont.get(Constante.MESSAGE_OUTPUT);
                      
            if( npStatus.equals("PORTABILIDAD_NUMERICA") && (iPermission==1) && (intValMsgSub==1)  ){
          %>
          
            <td class="CellContent" align="center"><!--<%=(objPODevBean.getNpStateDesc()!=null?objPODevBean.getNpStateDesc():"")%>-->
              <!--<select name="cmbCorrected">
                <%=MiUtil.buildComboSelected(arrSubsanacionList, MiUtil.getString((String) objPODevBean.getNpState()))%>
              </select>
              <input type="hidden" name="hdnCorrected" value="<%=objPODevBean.getNpState()%>">
            </td>
          <%
          }else{%>  
          
            <td class="CellContent" align="center"><!--<%=(objPODevBean.getNpStateDesc()!=null?objPODevBean.getNpStateDesc():"")%>-->               
               <!--<div id="dvByCmbCorrected" style=display:"none">               
                  <select name="cmbCorrected">
                  <%=MiUtil.buildComboSelected(arrSubsanacionList, MiUtil.getString((String) objPODevBean.getNpState()))%>
                  </select>
                     <input type="hidden" name="hdnCorrected" value="<%=objPODevBean.getNpState()%>">               
               </div>

               <div id="dvByTxtCorrected" style=display:"">
                  <%=(objPODevBean.getNpStateDesc()!=null?objPODevBean.getNpStateDesc():"")%>
               </div> 
            </td>                                                               
                                                               
          <%}}%>--> 
          </tr>
          <%
                }
                }
              }
            }
          %>
        </table>
      </td>
    </tr>
  </table>
  <%
  } catch (Exception e) {
    e.printStackTrace();
    System.out.println("Mensaje::::" + e.getMessage() + "<br>");
    System.out.println("Causa::::" + e.getCause() + "<br>");
    for (int i = 0; i < e.getStackTrace().length; i++) {
      System.out.println("    " + e.getStackTrace()[i] + "<br>");
	  }
  }%>