<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderSession" %>
<%@ page import="oracle.portal.provider.v2.url.UrlUtils" %>
<%@ page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil" %>
<%@ page import="pe.com.nextel.bean.*"%>
<%@ page import="pe.com.nextel.service.*"%>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="java.util.*"%>
<%
try{
    request.setCharacterEncoding("UTF-8");
   String         strMessage        = null;
   int            flagConsultor     = 0;
   GeneralService objGeneralService = new GeneralService();
   ArrayList      arrLista          = null;
   HashMap        hshUbigeoList     = null;
   HashMap        hshResultado      = null;
   HashMap        objHshMap         = null;
   ArrayList      arrListaProv      = new ArrayList();   
   ArrayList      arrListaDist      = new ArrayList();
   ArrayList      arrListaAmb       = new ArrayList();
   String         strRegionIdIni    = "";
   
   String strSessionId  = (request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
   String strDirEntrega = (request.getParameter("strDirEntrega")==null?"":request.getParameter("strDirEntrega"));
   String strDpto       = (request.getParameter("strDpto")==null?"":request.getParameter("strDpto"));
   String strDptoId     = (request.getParameter("strDptoId")==null?"":request.getParameter("strDptoId"));
   String strProv       = (request.getParameter("strProv")==null?"":request.getParameter("strProv"));
   String strProvId     = (request.getParameter("strProvId")==null?"":request.getParameter("strProvId"));
   String strDist       = (request.getParameter("strDist")==null?"":request.getParameter("strDist"));
   String strDistId     = (request.getParameter("strDistId")==null?"":request.getParameter("strDistId"));
   String strRegionId   = (request.getParameter("regionAddressId")==null?"":request.getParameter("regionAddressId"));
   String strUserId     = (request.getParameter("userId")==null?"":request.getParameter("userId"));
   String strReference  = (request.getParameter("strReference")==null?"":request.getParameter("strReference")); // [N_O000017567] MMONTOYA
      String strSpecification  = (request.getParameter("strSpecification")==null?"":request.getParameter("strSpecification")); //[SP3103 VALIDACION DIRECCIONES RIESGO] DIANA LLANOS
   String strUserName  = (request.getParameter("strUserName")==null?"":request.getParameter("strUserName")); //[SP3103 VALIDACION DIRECCIONES RIESGO] DIANA LLANOS
    String strOrderId  = (request.getParameter("strOrderId")==null?"0":request.getParameter("strOrderId")); //[SP3103 VALIDACION DIRECCIONES RIESGO] DIANA LLANOS
    String strCompanyId  = (request.getParameter("strCompanyId")==null?"0":request.getParameter("strCompanyId")); //[SP3103 VALIDACION DIRECCIONES RIESGO] DIANA LLANOS
   
   String strURLOrderServlet = request.getContextPath()+"/editordersevlet";   

   
   objHshMap  = objGeneralService.GeneralDAOgetConsultor(MiUtil.parseInt(strUserId));
   strMessage = (String)objHshMap.get("strMessage");
   if (strMessage!=null)
     throw new Exception(strMessage);   
  
   flagConsultor = MiUtil.parseInt((String)objHshMap.get("respuesta"));

   if (flagConsultor>0 ){
    objHshMap  = objGeneralService.GeneralDAOgetAmbitSalesList("REGION",MiUtil.parseInt(strRegionId));
    strMessage = (String)objHshMap.get("strMessage");
    if (strMessage!=null)
       throw new Exception(strMessage);   
  
    arrListaAmb = (ArrayList)objHshMap.get("arrAmbitSalesList");
  }

%>    
    
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Dirección</title>
  </head>
  <link   rel="stylesheet"      href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">
  <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>         
  <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
  <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXEdit.js"></script>
  <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/jquery-1.10.2.js"></script>
  <body onLoad="javascript:inicio();">
    <table width="100%" border="0" cellspacing="1" cellpadding="0" class="RegionBorder">
    <form method="post" name="formdatos" target="bottomFrame" >
      <tr>
        <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
          <tr class="PortletHeaderColor">
            <td class="LeftCurve" valign="top" align="left" width="10">&nbsp;&nbsp;</td>
            <td class="PortletHeaderColor" align="left" valign="top">
              <font class="PortletHeaderText">Edición de Direcciones</font></td>
            <td align="right" class="RightCurve" width="10%" >&nbsp;&nbsp;</td>
          </tr>
        </table>
      </tr>
      <tr>
        <!-- Inicio tabla de direccion-->
        <table width="100%" border="0" cellspacing="1" cellpadding="0" class="RegionBorder">
          <tr>
            <td class="CellLabel" valign="top"><font class="Required">*</font>Dirección&nbsp;</td>
            <td class="CellContent" valign="top" colspan="3">
              <input type="text" name="txtAddress" SIZE="90" VALUE="" onchange="this.value=trim(this.value.toUpperCase());" MAXLENGTH="149"><BR>
            </td>
          </tr>
          <tr>                
            <td class="CellLabel" valign="top" width="120"><font class="Required">*</font>Departamento&nbsp;</td>
            <td class="CellContent" valign="top" width="300">                
              <select name="cmbDpto" onChange="fxLoadPlace('1');fxSetRegion(this);javascript:document.formdatos.hdnDpto.value=this[this.selectedIndex].text;">                                                           
                <% hshUbigeoList = objGeneralService.getUbigeoList(0,0,"0"); 
                   strMessage = (String)hshUbigeoList.get("strMessage");
                   if (strMessage!=null)
                     throw new Exception(strMessage);
                   arrLista = (ArrayList)hshUbigeoList.get("arrUbigeoList");
                %>
                <%=MiUtil.buildCombo(arrLista,"ubigeo","nombre")%>                    
              </select>
              <input type="hidden" name="hdnDpto">
            </td>              
          </tr>
          <tr>
            <td class="CellLabel" VALIGN="TOP" WIDTH="110"><font class="Required">*</font>Provincia&nbsp;</td>
            <td class="CellContent" VALIGN="TOP">
              <select name="cmbProv" onChange="fxLoadPlace('2');javascript:document.formdatos.hdnProv.value=this[this.selectedIndex].text">                                                                            
                <option value="0"> </option>
              </select>  
              <input type="hidden" name="hdnProv">
            </td>
          </tr>
          <tr>
            <td class="CellLabel" valign="top"><font class="Required">*</font>Distrito&nbsp;</td>
            <td class="CellContent" valign="top">
              <select name="cmbDist" onChange="javascript:document.formdatos.hdnDist.value=this[this.selectedIndex].text">                                                                              
                <option value="0"> </option>
              </select> 
              <input type="hidden" name="hdnDist">
            </td>              
          </tr>
          <!-- Inicio [N_O000017567] MMONTOYA -->
          <tr>
            <td class="CellLabel" valign="top"><font class="Required">*</font>Referencia&nbsp;</td>
            <td class="CellContent" valign="top" colspan="3">
              <input type="text" name="txtReference" SIZE="90" VALUE="" onchange="this.value=trim(this.value.toUpperCase());" MAXLENGTH="140"><BR>
            </td>
          </tr>
          <!-- Fin [N_O000017567] MMONTOYA -->
        </table>       
      </tr>
      <br>
      <tr>
        <table align="center" width="50%" cellpadding="0" cellspacing="1" >
          <tr>
            <td align="right">
              <input type="button" value="   Aceptar   " name="btnsubmit" onclick="javascript:insertAddress();">
            </td>
            <td align="center">
              <input type="button" value="   Limpiar   " name="btnsubmit" onclick="javascript:limpiarForm();">
            </td>
            <td align="left">
              <input type="button" value="   Cancelar   " name="btnCancelar" onclick="parent.close();">
            </td>
          </tr>
        </table>
      </tr>
      <tr>
        <input type="hidden" name="hdnSessionId"       value="<%=strSessionId%>">
        <input type="hidden" name="flg_enabled"   value="1">
        <input type="hidden" name="hdnRegionId"   value="">
        <input type="hidden" name="myaction"/>
      </tr>
    </form>
    </table>
  </body>
</html>

<script language="JavaScript">
  function inicio(){
    form = document.formdatos;
    form.txtAddress.value = '<%=strDirEntrega%>';
    
    <%
    for( int i=1; i<=arrLista.size();i++ ){ 
      HashMap h = (HashMap)arrLista.get(i-1);

      if(MiUtil.getString((String)h.get("nombre")).equals(strDpto)){
        strDptoId = MiUtil.getString((String)h.get("ubigeo"));
        strRegionIdIni = MiUtil.getString((String)h.get("regionid"));
      }
    }
    %>    
    form.cmbDpto.value = '<%=strDptoId%>';
    form.hdnDpto.value = '<%=strDpto%>';
    form.hdnRegionId.value = '<%=strRegionIdIni%>';
    <%
      hshResultado=objGeneralService.getUbigeoList(MiUtil.parseInt(strDptoId),0,"1");             
      strMessage=(String)hshResultado.get("strMessage");
      if (strMessage==null)                
        arrListaProv=(ArrayList)hshResultado.get("arrUbigeoList");  

        for( int i=1; i<=arrListaProv.size();i++ ){ 
          HashMap h = (HashMap)arrListaProv.get(i-1);
    %>
          opcion = new Option('<%=MiUtil.getString((String)h.get("nombre"))%>','<%=MiUtil.getString((String)h.get("ubigeo"))%>');      
          form.cmbProv.options['<%=i%>']=opcion;  
    <%
        if(MiUtil.getString((String)h.get("nombre")).equals(strProv)){
          strProvId = MiUtil.getString((String)h.get("ubigeo"));
        }
    }
    %>
      form.cmbProv.value = '<%=strProvId%>';
      form.hdnProv.value = '<%=strProv%>';
    <%
      hshResultado=objGeneralService.getUbigeoList(MiUtil.parseInt(strDptoId),MiUtil.parseInt(strProvId),"2");             
      strMessage=(String)hshResultado.get("strMessage");
      if (strMessage==null)                
        arrListaDist=(ArrayList)hshResultado.get("arrUbigeoList");  
       
      for( int i=1; i<=arrListaDist.size();i++ ){ 
        HashMap h = (HashMap)arrListaDist.get(i-1);
    %>
        opcion = new Option('<%=MiUtil.getString((String)h.get("nombre"))%>','<%=MiUtil.getString((String)h.get("ubigeo"))%>');      
        form.cmbDist.options['<%=i%>']=opcion;  
    <%
        if(MiUtil.getString((String)h.get("nombre")).equals(strDist)){
          strDistId = MiUtil.getString((String)h.get("ubigeo"));
        }
      }
    %>
      form.cmbDist.value = '<%=strDistId%>';
      form.hdnDist.value = '<%=strDist%>';

      form.txtReference.value = '<%=strReference%>'; // [N_O000017567] MMONTOYA
  }
  

  function limpiarForm(){
    form = document.formdatos;
    form.txtAddress.value = '';
    form.cmbDpto.value = '';
    form.hdnDpto.value = '';
    form.cmbProv.value = '';
    form.hdnProv.value = '';
    form.cmbDist.value = '';
    form.hdnDist.value = '';
    form.txtReference.value = ''; // [N_O000017567] MMONTOYA
  }
  
  function validateForm(){
    formVal = document.formdatos;
    var flagAmbit = "0";
    if (formVal.txtAddress.value == ''){
      alert("El campo Dirección es obligatorio");
      formVal.txtAddress.focus();
      return false;
    }
    if (formVal.cmbDpto.value == '' || formVal.cmbDpto.value == '0'){
      alert("El campo Departamento es obligatorio");
      formVal.cmbDpto.focus();
      return false;
    }
    if (formVal.cmbProv.value == '' || formVal.cmbProv.value == '0'){
      alert("El campo Provincia es obligatorio");
      formVal.cmbProv.focus();
      return false;
    }
    if (formVal.cmbDist.value == '' || formVal.cmbDist.value == '0'){
      alert("El campo Distrito es obligatorio");
      formVal.cmbDist.focus();
      return false;
    }
      //EPV
      /*
      if (formVal.txtReference.value == ''){
          alert("El campo Referencia es obligatorio");
          formVal.txtReference.focus();
          return false;
      }
    */
    //EPV
    <%if(flagConsultor>0){
        for( int i=1; i<=arrListaAmb.size();i++ ){ 
          HashMap h = (HashMap)arrListaAmb.get(i-1);
    %>  
          if(formVal.hdnRegionId.value=='<%=MiUtil.getString((String)h.get("swregionid"))%>'){
            flagAmbit = "1";
          }
    <%
        }
    %>
        if (flagAmbit=="0"){
          alert("La región no pertenece al ambito del consultor");
          return false;
        }
    <%}%>
    // validar DIRECCIÓN FRAUDULENTA

        var validadireccion = ValidarDireccionRiesgo();
        if(validadireccion==false){
            return false;
        }else{
            return true;
        }
    
    //return true;
  }
  function fxLoadPlace(tipo) {                 
    Form = document.formdatos;      
    var depId= Form.cmbDpto.value; 
    var provId= Form.cmbProv.value; 
    if (tipo==1 && depId==""){ //  0: Carga Departamentos 1: Carga Provincia  2: Carga Distrito                                                 
      deleteOptionIE(document.formdatos.cmbDist); 
      deleteOptionIE(document.formdatos.cmbProv);             
    }else if (tipo==2 && (provId=="" || provId=="0")){
      deleteOptionIE(document.formdatos.cmbDist); 
    }else{ 
      deleteOptionIE(document.formdatos.cmbDist); 
      var url = "<%=strURLOrderServlet%>"+"?sDepId="+depId+"&sProvId="+provId+"&nTipo="+tipo+"&sCodName=ubigeo&myaction=LoadUbigeo2"; 
      parent.bottomFrame.location.replace(url);            
    }
  }
    //INI FBERNALES
function ValidarDireccionRiesgo(){
    var url_server = '<%=strURLOrderServlet%>';
    Form = document.formdatos;      
    var cmbDist= Form.cmbDist.value; 
    var txtAddress= Form.txtAddress.value; 
    var txtReference= Form.txtReference.value;
    //var hdnSessionId= Form.hdnSessionId.value;
    var params = 'myaction=GetValidarDireccionRiesgo&txtAddress='+txtAddress.replace(/\s/gi, "")+'&txtReference='+txtReference.replace(/\s/gi, "")+'&cmbDist='+cmbDist+'&specificationid='+'<%=strSpecification%>';//
   var direccion2=txtAddress+ " "+txtReference;
    var retorno = false;
    $.ajax({
        url: url_server, 
        data: params,
        async: false,
        type: "POST", 
        success:function(data){
            if(data.flag){
                var confirma = confirm("La direccion ingresada "+ direccion2 +" esta registrada en la base datos de fraude.\n ¿Desea continuar?");
                insLogValidateAddress(data.correlacion,data.direccion,"<%=strUserName%>","<%=strOrderId%>","<%=strCompanyId%>");
                if(confirma){                    
                    retorno= true;
                }else{
                    retorno= false;
                }                    
            }else{
                retorno= true;
            }
        },
        error: function(){
            alert("Se produjo un error al intentar validar la dirección");   
        }
    });  
    return retorno;
}
// 
function insLogValidateAddress(correlacion,direccion,usuario,orderId,companyId){
    var url_server = '<%=strURLOrderServlet%>';
    var params = 'myaction=GetInsertLogValidateAddress&direccion='+direccion+'&correlacion='+correlacion+'&createdBy='+usuario+'&npnumorder='+orderId+'&npidcliente='+companyId;
   
    var retorno = true;
    $.ajax({
        url: url_server, 
        data: params,
        async: false,
        type: "POST",
        success:function(data){                  
                retorno= true;
        },
        error: function(){
            alert("Se produjo un error al intentar validar la dirección");  
        }
    });   
    return retorno;
}
  //FIN FBERNALES 
  function insertAddress() {        
    frmCurrent = document.formdatos;
    form = parent.opener.parent.mainFrame.document.frmdatos;
    
    if (validateForm()){
        if(frmCurrent.txtAddress.value != "null"){
           form.txtDirEntrega.value        = frmCurrent.txtAddress.value;
           form.hdnDeliveryAddress.value   = frmCurrent.txtAddress.value;
        }
        else{
           form.txtDirEntrega.value    = "";
           form.hdnDeliveryAddress.value   = "";
        }        
        if (frmCurrent.hdnDist.value != "null"){         
           form.txtCity.value              = frmCurrent.hdnDist.value;
           form.hdnDeliveryCity.value      = frmCurrent.hdnDist.value;
           form.hdnDeliveryCityId.value    = frmCurrent.cmbDist.value;
        }
        else{           
           form.txtCity.value           = "";
           form.hdnDeliveryCity.value   = "";
           form.hdnDeliveryCityId.value = "";
        }
        if (frmCurrent.hdnProv.value != "null"){
           form.txtProvince.value           = frmCurrent.hdnProv.value;
           form.hdnDeliveryProvince.value   = frmCurrent.hdnProv.value;
           form.hdnDeliveryProvinceId.value = frmCurrent.cmbProv.value;
        }
        else{
           form.txtProvince.value           = "";
           form.hdnDeliveryProvince.value   = "";
           form.hdnDeliveryProvinceId.value = "";
        }
        if (frmCurrent.hdnDpto.value != "null"){
           form.txtState.value             = frmCurrent.hdnDpto.value;
           form.hdnDeliveryState.value     = frmCurrent.hdnDpto.value;  
           form.hdnDeliveryStateId.value   = frmCurrent.cmbDpto.value;  
        }
        else{
           form.txtState.value           = "";
           form.hdnDeliveryState.value   = "";  
           form.hdnDeliveryStateId.value = "";
        }

        // [N_O000017567] MMONTOYA
        if (frmCurrent.txtReference.value != "null"){
           form.txtReference.value = frmCurrent.txtReference.value;
        }
        else{
           form.txtReference.value = "";
        }
                        
        parent.close();
      }
    }
  function fxSetRegion(obj){
    frmCurrent = document.formdatos;
    <%
      for( int i=1; i<=arrLista.size();i++ ){ 
        HashMap h = (HashMap)arrLista.get(i-1);
    %>
        if(obj.value=='<%=MiUtil.getString((String)h.get("ubigeo"))%>'){
          frmCurrent.hdnRegionId.value = '<%=MiUtil.getString((String)h.get("regionid"))%>';
        }
    <%
      }
    %>
  }
</script>
<%}catch(Exception ex){
  System.out.println("Address New try catch-->"+MiUtil.getMessageClean(ex.getMessage())); 
%>
<script>
  alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}%>