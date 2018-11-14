<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.StringTokenizer" %>
<%@ page import="pe.com.nextel.bean.ServiciosBean" %>
<%@ page import="pe.com.nextel.bean.BillingAccountBean" %>
<%@ page import="pe.com.nextel.bean.CoAssignmentBean" %>
<%@ page import="pe.com.nextel.bean.SiteBean" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.service.NewOrderService"%>
<%@ page import="pe.com.nextel.service.CustomerService"%>
<%@ page import="pe.com.nextel.service.BillingAccountService"%>

<%
    //EFH001 Añadido Código para control de sesión.
    System.out.println("--------- Order_AsignacionResponsables.jsp ---------");
    String urlIngreso = request.getRequestURL()+(request.getQueryString() != null ? "?" + request.getQueryString() : "");
    String jspIngreso = "Order_AsignacionResponsables.jsp";
    String ipIngreso =  request.getRemoteAddr();
    String  contexto = request.getContextPath();
    //FIN
%>
<html>
  <head>
    <link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
    <script language="javascript" src="/web_operacion/js/syncscroll.js"></script>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXNew.js"></script>
    <!--<script language="javascript" src="websales/Resource/FunctionsOrderEdit.js"></script>-->
      <script language="javaScript">
          //EFH001 Añadido Código para control de sesión.
          if (parent.opener==undefined||parent.opener== null){

              window.location.replace("<%=contexto%>"+"/htdocs/jp_session_validate/Session_Validate.jsp"+"?urlIngreso="+"<%=urlIngreso%>"+"&jspIngreso="+"<%=jspIngreso%>"+"&ipIngreso="+"<%=ipIngreso%>");
          }
          //FIN
      </script>
    <title>untitled</title>
  </head>
  <!--<body onload="javascript:alert('Tamaño del arreglo : ' + parent.opener.apNewBillAcc.length);">-->
  <%!
  HashMap   objHashMap = new HashMap();
  ArrayList objArraySiteExistsList    = new ArrayList();
  ArrayList objArraySiteSolicitedList = new ArrayList();
  ArrayList objBillingBySiteExists    = null;
  ArrayList objBillingBySiteSolicited = new ArrayList();
  String    strCustomerStruct         = "";
  String    strBillingAssignment      = "";
  String    strTypeBill               = "";
  String    strTypeSite               = "";
  String    strNewSiteId              = "";
  String    strBillingSiteId          = "";
  CoAssignmentBean  objCoAssignmentBean = new CoAssignmentBean();
  
  %>
  <%
  ArrayList objArrayBillingAccount  = (ArrayList)request.getAttribute("objArrayBillingAccount");
  Vector    la_servicesid           = (Vector)request.getAttribute("la_servicesid");
  Vector    vctItemBillingAccount   = (Vector)request.getAttribute("vctItemBillingAccount");
  String    strCodigoCliente        = MiUtil.getString(""+request.getAttribute("strCodigoCliente"));
  String    strResponsablePagoValue = MiUtil.getString(""+request.getAttribute("strResponsablePagoValue"));
  String    strResponsablePagoDesc  = MiUtil.getString((String)request.getAttribute("strResponsablePagoDesc"));
  String    strEquipoDescription    = MiUtil.getString((String)request.getAttribute("strEquipoDescription"));
  String    strEquipoId             = MiUtil.getString((String)request.getAttribute("strEquipoId"));
  String    strOrderId              = MiUtil.getString((String)request.getAttribute("strOrderId"));
  
  int       intIndex                = MiUtil.parseInt((String)request.getAttribute("index"));
  %>
  
  <%
    /**Obtenemos los responsables de pago existentes y solitados**/
    /**Obtenemos los Sites existentes del Customer**/
    objArraySiteExistsList = (ArrayList)request.getAttribute("objArraySiteExistsList");
    
    if( objArraySiteExistsList!=null ) strCustomerStruct = objArraySiteExistsList.size()==0?"FLAT":"LARGE";
    
    /**Obtenemos los Sites solicitados**/
    objArraySiteSolicitedList = (ArrayList)request.getAttribute("objArraySiteSolicitedList");
    
    /**Obtenemos los valores asignados si es que ya fue asignado**/
    strBillingAssignment            = MiUtil.getString((String)request.getParameter("item_billingAccount"));
    if( !strBillingAssignment.equals("") ){
      StringTokenizer strMyString     = new StringTokenizer(strBillingAssignment,"|");
      strTypeSite           =   MiUtil.getString(strMyString.nextToken());
      strNewSiteId          =   MiUtil.getString(strMyString.nextToken());
      strTypeBill           =   MiUtil.getString(strMyString.nextToken());
      strBillingSiteId      =   MiUtil.getString(strMyString.nextToken());
      
      if( strTypeSite.equals("E") ){
        long lngAuxCustIdBSCS   = (new CustomerService()).getCustomerIdBSCS(MiUtil.parseLong(strNewSiteId),Constante.CUSTOMERTYPE_SITE);
        HashMap objBilling = (new BillingAccountService()).BillingAccountDAOgetBillingAccountList(lngAuxCustIdBSCS);
        objBillingBySiteExists = (ArrayList)objBilling.get("objArrayList");
      
        ArrayList objAuxBillingBySite = new ArrayList();  
        objBilling = (new BillingAccountService()).BillingAccountDAOgetAccountList(MiUtil.parseLong(strNewSiteId),MiUtil.parseLong(strCodigoCliente),MiUtil.parseLong(strOrderId));
        objBillingBySiteSolicited = (ArrayList)objBilling.get("objArrayList");
      
      }else if( strTypeSite.equals("S") ){
        HashMap objBilling = (new BillingAccountService()).BillingAccountDAOgetAccountList(MiUtil.parseLong(strNewSiteId),MiUtil.parseLong(strCodigoCliente),MiUtil.parseLong(strOrderId));
        objBillingBySiteSolicited = (ArrayList)objBilling.get("objArrayList");
      }else{
        long lngAuxCustIdBSCS   = (new CustomerService()).getCustomerIdBSCS(MiUtil.parseLong(strCodigoCliente),Constante.CUSTOMERTYPE_CUSTOMER);	
        //long lngAuxCustIdBSCS   = (new CustomerService()).getCustomerIdBSCS(MiUtil.parseLong(objCoAssignmentBean.getNpnewsiteid()),Constante.CUSTOMERTYPE_SITE);		 
        HashMap objBilling = (new BillingAccountService()).BillingAccountDAOgetBillingAccountList(lngAuxCustIdBSCS);
        objBillingBySiteExists = (ArrayList)objBilling.get("objArrayList");
        
        ArrayList objAuxBillingBySite = new ArrayList();  
        objBilling = (new BillingAccountService()).BillingAccountDAOgetAccountList(MiUtil.parseLong(strNewSiteId),MiUtil.parseLong(strCodigoCliente),MiUtil.parseLong(strOrderId));
        objBillingBySiteSolicited = (ArrayList)objBilling.get("objArrayList");
      }
    }
    
    
  %>
  
  <body onload="javascript:fxloadBody();" >
  <form name="frmdatos" >
  
  <div id="divTableItems">
         <table border="1" width="99%" cellpadding="0" cellspacing="0" class="regionborder" align="center">
            <tr valign="top">
               <td class="regionheadercolor" width="100%" align="center">
                  <table border="0" width="100%" cellspacing="0" cellpadding="0">
                     <tr>
                        <td width="100%">
                           <table border="0" width="100%" cellspacing="0" cellpadding="0">
                              <tr>
                                 <td width="100%">
  
  <table  width='100%' border='0' cellspacing='1' cellpadding='0' >
  <!--Cabecera-->
  <tr  valign="top" height="20"  class="CellLabel" align="center" >
    <td align="center">&nbsp;Equipo&nbsp;</td>
    <!--<td>&nbsp;Serv. Adic.&nbsp;</td>-->
    <!--<td>&nbsp;Cargo&nbsp;</td>-->
    <td>&nbsp;Responsable Pago&nbsp;</td>
    <td>&nbsp;Factura&nbsp;</td>
  </tr>
  
  <!--Nivel de Asignación-->
  <tr  class='CellContent' align='left' >
  <!--Nombre del Equipo-->
  <!--<td>&nbsp;<a href="javascript:fxChangeDivEquipment('<%=strEquipoDescription%>');"><%=strEquipoDescription%></a>&nbsp;</td>-->
  <td align="center">&nbsp;<%=strEquipoDescription%>&nbsp;</td>
  <!--Serv. Adicionales-->
  <!--<td>&nbsp;</td>-->
  <!--Cargo-->
  <!--<td>&nbsp;</td>-->
  <!--Responsable de Pago-->
  <td>&nbsp;
  <select name="cmbSiteEquipment" onchange="javascript:fxChangeSiteEquipment(this)" >
      <option value="-2">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      </option>
  </select>
  &nbsp;</td>
  <!--Cuentas de Facturación-->
  <td>&nbsp;
  <select name="cmbFacturaEquipment" ><!--onchange="javascript:fxChangeEquipment()" >-->
      <option value="-2">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      </option>
  </select>
  &nbsp;</td>
  
  </tr>
              
  <%
    /****/
    ServiciosBean objServiciosBean = null;
      //for(int i=0; i<la_servicesid.size(); i++){
      for(int i=0; i<0; i++){
        objServiciosBean = (ServiciosBean)la_servicesid.elementAt(i);
  %>
    <!--A nivel de servicio-->
    <tr  class='CellContent' align='left' id="<%=strEquipoDescription+i%>" style="display:none" >
    <td>&nbsp;</td>
    <td><a href="javascript:fxChangeDiv('<%=objServiciosBean.getNpservicioid()%>');">&nbsp;<%=objServiciosBean.getNpnomserv()%>&nbsp;</a></td>
    <td>&nbsp;</td>
    <td>&nbsp;
      <select name="cmbSiteEquipment<%=objServiciosBean.getNpservicioid()%>" onchange="javascript:fxChangeSiteEquipment(this,<%=objServiciosBean.getNpservicioid()%>)" >
          <option value="-2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
      </select>
    &nbsp;</td>
    <td>&nbsp;
      <!--<select name="cmbFactura<%=objServiciosBean.getNpservicioid()%>" onchange="javascript:fxChangeService(this,<%=objServiciosBean.getNpservicioid()%>)" >-->
      <select name="cmbFactura<%=objServiciosBean.getNpservicioid()%>" >
          <option value="-2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
      </select>
    &nbsp;</td>
    </tr>
              
    <!--A nivel de servicio (Único, Recurrente, Excesos)-->
      
      <!--Unico-->
      <tr  class='CellContent' align='left' id="<%=objServiciosBean.getNpservicioid()+"a"%>" style="display:none">
      <td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;Unico&nbsp;</td>
      <td>&nbsp;
      <select name="cmbSiteEquipmentUnico<%=objServiciosBean.getNpservicioid()%>" onchange="javascript:fxChangeSiteEquipment(this,<%=objServiciosBean.getNpservicioid()%>)" >
          <option value="-2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
      </select>
      &nbsp;</td>
      <td>&nbsp;
      <!--<select name="cmbFacturaUnico<%=objServiciosBean.getNpservicioid()%>" onchange="javascript:fxChangeServiceNode(this,<%=objServiciosBean.getNpservicioid()%>)" >-->
      <select name="cmbFacturaUnico<%=objServiciosBean.getNpservicioid()%>"  >
          <option value="-2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
      </select>
      &nbsp;</td>
      </tr>
      
      <!--Recurrente-->
      <tr  class='CellContent' align='left' id="<%=objServiciosBean.getNpservicioid()+"b"%>" style="display:none">
      <td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;Recurrente&nbsp;</td>
      <td>&nbsp;
      <select name="cmbSiteEquipmentRecurrente<%=objServiciosBean.getNpservicioid()%>" onchange="javascript:fxChangeSiteEquipment(this,<%=objServiciosBean.getNpservicioid()%>)" >
          <option value="-2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
      </select>
      &nbsp;</td>
      <td>&nbsp;
      <!--<select name="cmbFacturaRecurrente<%=objServiciosBean.getNpservicioid()%>" onchange="javascript:fxChangeServiceNode(this,<%=objServiciosBean.getNpservicioid()%>)" >-->
      <select name="cmbFacturaRecurrente<%=objServiciosBean.getNpservicioid()%>" >
          <option value="-2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
      </select>
      &nbsp;</td>
      </tr>
      
      <!--Excesos-->
      <tr  class='CellContent' align='left' id="<%=objServiciosBean.getNpservicioid()+"c"%>" style="display:none">
      <td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;Excesos&nbsp;</td>
      <td>&nbsp;
      <select name="cmbSiteEquipmentExcesos<%=objServiciosBean.getNpservicioid()%>" onchange="javascript:fxChangeSiteEquipment(this,<%=objServiciosBean.getNpservicioid()%>)" >
          <option value="-2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
      </select>
      &nbsp;</td>
      <td>&nbsp;
      <!--<select name="cmbFacturaExcesos<%=objServiciosBean.getNpservicioid()%>" onchange="javascript:fxChangeServiceNode(this,<%=objServiciosBean.getNpservicioid()%>)" >-->
      <select name="cmbFacturaExcesos<%=objServiciosBean.getNpservicioid()%>" >
          <option value="-2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
      </select>
      &nbsp;</td>
      </tr>
              
    <%
      }
    %>
  
  </table>
                                    </td>
                                 </tr>
                              </table>
                           </td>
                        </tr>
                     </table>
                  </td>
               </tr>
            </table>
         </div>
  
  
  <table width="100%" align="center" ><tr width="100%" align="center"  ><td width="100%"  align="center"  ><input type="button" value="Asignar" onclick="javascript:fxSaveAssignment();">&nbsp;&nbsp;&nbsp;<input type="button" value="Cancelar" onclick="javascript:parent.mainFrame.parent.window.close();" ></td></tr></table>
  </form>
  </body>
  
<script>
  var vctSitesEquipment       = new Vector();
  var vctBillingAccount       = new Vector();
  var vctItemBillingAccount   = new Vector();
  var vctServices             = new Vector();
  var flgComponents           = true;
  
  try{
  var arrFacturas             = parent.opener.apNewBillAcc;
  }catch(e){flgComponents=false}
  
  function fxSetValues(){
    var form = document.frmdatos;
    var result = "";
    if( parent.opener.vctItemsMainFrameOrder.size() == 1 ){
      result =parent.opener.frmdatos.item_billingAccount.value;
    }else if ( parent.opener.vctItemsMainFrameOrder.size() > 1 ){
      result = parent.opener.frmdatos.item_billingAccount[<%=intIndex-1%>].value;
    }
    
    /**Hubieron Cambios**/
    if( result != "" ){
    var myString = result;
    var tokens = myString.tokenize("|", "|", true);
    
      if( tokens.length > 0 ){
        //Site
        form.cmbSiteEquipment.value = tokens[1];
        //Billing Account  
        form.cmbFacturaEquipment.value = tokens[3];
        //alert(""+tokens[2])
        //fxObjectConvert('txt_ItemPriceCtaInscrip',round_decimals(tokens[1],2)); 
      }
    }
  }
  
  function fxSetValues01(){
    form = document.frmdatos;
    var flgEquipment  = "";
    var valueItemBilling = "";
  
    //Entra solo si ya se estableció las cuentas de facturación
    for( i=0, j=1; j<=(vctItemBillingAccount.size()/16); i=i+16,j++ ){
        
        //Si es la primera vez
        if( j == 1 ){
          valueItemBilling     =  vctItemBillingAccount.elementAt(i+2);
          //alert(valueItemBilling);
          if( valueItemBilling != "-2" ){
            var strCadenaFactura = "form.cmbFacturaEquipment.value";
            
            eval(strCadenaFactura + " = "  + valueItemBilling);
            //break;
          }
        
        }
        
        flgEquipment   =    vctItemBillingAccount.elementAt(i+5);
        
        if( flgEquipment != "-2" ){
                          
            //Obtenemos el SA
            idService         = vctItemBillingAccount.elementAt(i+6);
            valueItemBilling  =  vctItemBillingAccount.elementAt(i+5);
            
            var strCadenaFactura = "form.cmbFactura"+idService+".value";
            //alert(strCadenaFactura + " = "  + valueItemBilling);
            eval(strCadenaFactura + " = "  + valueItemBilling);
          
        }else{
            //Obtenemos el SA para Unico
            idService         = vctItemBillingAccount.elementAt(i+6);
            valueItemBilling  =  vctItemBillingAccount.elementAt(i+9);                  
            var strCadenaFactura = "form.cmbFactura" + "Unico" + idService+".value";
            //alert(strCadenaFactura + " = "  + valueItemBilling);
            eval(strCadenaFactura + " = "  + valueItemBilling);
            
            
            //Obtenemos el SA para Recurrente
            idService         = vctItemBillingAccount.elementAt(i+6);
            valueItemBilling  =  vctItemBillingAccount.elementAt(i+12);                  
            var strCadenaFactura = "form.cmbFactura" + "Recurrente" + idService+".value";
            //alert(strCadenaFactura + " = "  + valueItemBilling);
            eval(strCadenaFactura + " = "  + valueItemBilling);
            
            //Obtenemos el SA para Excesos
            idService         = vctItemBillingAccount.elementAt(i+6);
            valueItemBilling  =  vctItemBillingAccount.elementAt(i+15);                  
            var strCadenaFactura = "form.cmbFactura" + "Excesos" + idService+".value";
            //alert(strCadenaFactura + " = "  + valueItemBilling);
            eval(strCadenaFactura + " = "  + valueItemBilling);
        }
      
    }
    
  }
  
  function fxChangeSiteEquipment(objObjectSelectSite){
    var form      = document.frmdatos;
    var objName   = objObjectSelectSite.name;
    var objValue  = objObjectSelectSite.value;
    var url       = '';
    //alert(objName)
    if( objName == 'cmbSiteEquipment' )
      objName      = "cmbFacturaEquipment";
    else
      objName      = "cmbFactura"+objName.substring(objName.indexOf('cmbSiteEquipment')+16,objName.length);
    //alert(objName)
    /*Si se ha seleccionado algún site*/
    if(objValue!=''){
      url = "<%=request.getContextPath()%>/customerservlet?myaction=loadBillingAccountBySiteId&strSiteId="+objValue+"&strNameObject="+objName+"&strCustomerId=<%=strCodigoCliente%>&strOrderId=<%=strOrderId%>&strPage=loadAssigment";      
      //alert(url)
      parent.bottomFrame.location.replace(url);
    }
    
  
  }
  
  function fxChangeEquipment(){
  //form  = document.frmdatos;
  //Verificamos que el combo no esté seleccioando
  
    /*if( !form.cmbFacturaEquipment.options[0].selected ){
  
      //var rpta = confirm("Usted está a punto de resetear los campos de servicios adicionales. ¿Desea continuar?");
      rpta = true;
      if( rpta ){
      
        for( a=0; a<vctServices.size(); a++ ){
            var objService = vctServices.elementAt(a)
            var strCadenaFactura = "form.cmbFactura";
                strCadenaFactura += objService.id;
            
            eval(strCadenaFactura+".options[0].selected = true")
        
        }
      
      }else{
        form.cmbFacturaEquipment.options[0].selected = true;
      }
    }else{
        form.cmbFacturaEquipment.options[0].selected = true;
    }*/
  
  }
  
  function fxChangeService(objCombo,idServicio){
  
  form  = document.frmdatos;
  //Verificamos que el combo no esté seleccioando
  
    if( !objCombo.options[0].selected ){
    
      //var rpta = confirm("Usted está a punto de resetear los campos de servicios adicionales. ¿Desea continuar?");
      rpta = true;
      if( rpta ){
      
          //Para Único
          var strCadenaFactura = "form.cmbFactura" + "Unico";
              strCadenaFactura += idServicio;
            
          eval(strCadenaFactura+".options[0].selected = true")
          
          //Para Recurrente
          strCadenaFactura = "form.cmbFactura" + "Recurrente";
              strCadenaFactura += idServicio;
            
          eval(strCadenaFactura+".options[0].selected = true")
          
          //Para Excesos
          strCadenaFactura = "form.cmbFactura" + "Excesos";
              strCadenaFactura += idServicio;
            
          eval(strCadenaFactura+".options[0].selected = true")
          
          //eval("form.cmbFacturaEquipment.options[0].selected = true")
          
       
      }else{
        objCombo.options[0].selected = true;
      }
    }else{
        objCombo.options[0].selected = true;
    }
  
  }
  
  
  
  function fxChangeServiceNode(objCombo,idServicio){
  
  form  = document.frmdatos;
  //Verificamos que el combo no esté seleccioando
  
    if( !objCombo.options[0].selected ){
    
      //var rpta = confirm("Usted está a punto de resetear los campos de servicios adicionales. ¿Desea continuar?");
      rpta = true;
      if( rpta ){
      
          //Para Único
          var strCadenaFactura = "form.cmbFactura";
              strCadenaFactura += idServicio;
            
          eval(strCadenaFactura+".options[0].selected = true")
          
      }else{
        objCombo.options[0].selected = true;
      }
    }else{
        objCombo.options[0].selected = true;
    }
  
  }
  
  function fxChargeSelects(objSelect){
    //alert(objSelect.length);
    var objSelectAux = eval(objSelect);
    for( i=0; i<vctBillingAccount.size();i++ ){ 
      var objMakeBA = vctBillingAccount.elementAt(i);
      opcion=new Option("Existente - " + objMakeBA.objBillingName,objMakeBA.objBillingId);
      //alert(opcion);
      //alert((i+1));
      objSelectAux.options[(i+1)]=opcion;
    }
  
    //Agrego las facturas 
    var initSelect = 0;
    
    initSelect = objSelectAux.length;
    if(flgComponents){
      try{
        for(j=0,x=initSelect; j<arrFacturas.length; x++,j++){
          opcion=new Option("Solicitado - " + arrFacturas[j].billAccName+'',''+arrFacturas[j].billAccId+'');
          objSelectAux.options[x]=opcion;  
        }
      }catch(e){}
    }
  }
  
  
  function fxChangeDiv(objId){
      viewHide(objId+"a");
      viewHide(objId+"b");
      viewHide(objId+"c");
  }
  
  function viewHide(objId){
  
  var targetElement = document.getElementById(objId);
  var obj           = null;
  
  //alert(targetElement.style.display);
    if( targetElement.style.display == 'none' ){
        targetElement.style.display = '';
    }else{
        targetElement.style.display ='none';
    }
    //alert(objId);
    /*var obj=null;
    var targetElement = document.getElementById(objId);
    if (obj!=null)
      obj.style.display='none';
    obj=targetElement;
    targetElement.style.display = "";*/
  }
  
  function fxChangeDivEquipment(objId){
    //Además hay que ocultar los Tipos de Cargo
    for(i=0; i<vctServices.size(); i++){
      viewHide(objId+i);
      //viewHide(objId+i+"a");
      //viewHide(objId+i+"b");
      //viewHide(objId+i+"c");
      
    }
   
    
  }
</script>

<script>
  //Permite generar la cadena que concatena las asignaciones realizadas
  function fxSaveAssignment(){
  //alert("Entramoss")
    var form = document.frmdatos;
    var typebill  = "";
    var typesite  = "";
    var siteId    = "";
    var billingId = "";
    var result    = "";
    var siteDesc  = "";
    var billDesc  = "";
    
    siteDesc  = form.cmbSiteEquipment.options[form.cmbSiteEquipment.selectedIndex].text;
    /**Obtenemos el SiteId y BillingAccountId**/
    if( siteDesc.indexOf("Solicitado") != -1 )
        typesite = "S";
    else if( siteDesc.indexOf("Existente") != -1 )
        typesite = "E";
    else
        typesite = "N";
    
    billDesc  = form.cmbFacturaEquipment.options[form.cmbFacturaEquipment.selectedIndex].text;
    /**Obtenemos el SiteId y BillingAccountId**/
    if( billDesc.indexOf("Solicitado") != -1 )
        typebill = "S";
    else if( billDesc.indexOf("Existente") != -1 )
        typebill = "E";
    else
        typebill = "N";
        
    siteId    = form.cmbSiteEquipment.value;
    billingId = form.cmbFacturaEquipment.value; 
    //item_billingAccount
    result = "|"+typesite+"|"+siteId+"|"+typebill+"|"+billingId+"|";
    
    //alert(result);
    if( parent.opener.vctItemsMainFrameOrder.size() == 1 ){
      parent.opener.frmdatos.item_billingAccount.value=result;
    
      try{
        parent.opener.frmdatos.hdnFlagSave.value = "A";
      }catch(e){}
    
    }else if ( parent.opener.vctItemsMainFrameOrder.size() > 1 ){
    
      parent.opener.frmdatos.item_billingAccount[<%=intIndex-1%>].value=result;
      try{
        parent.opener.frmdatos.hdnFlagSave[<%=intIndex-1%>].value = "A";
      }catch(e){}
    }
    
    try{
      parent.opener.document.frmdatos.hdnChangedOrder.value="S";
    }catch(e){}
    
    //alert(result);
    parent.mainFrame.parent.window.close();
  }
  
  function fxSaveAssignment01(){
    form = document.frmdatos;
    var vctAssignmentAccount = new Vector();

    //Si hay servicios adicionales
    if( vctServices.size() > 0 ) {
    
      for( a=0; a<vctServices.size(); a++ ){
      
        var objMakeBillingAccount = new fxMakeBillingAccount();
    
        //Cargamos el valor del Equipo
        objMakeBillingAccount.objEquipmentId  = form.cmbFacturaEquipment.value;
        objMakeBillingAccount.objEquipment    = form.cmbFacturaEquipment.options[form.cmbFacturaEquipment.selectedIndex].text;
    
        var objService = vctServices.elementAt(a)
        var strCadenaFactura = "form.cmbFactura";
            strCadenaFactura += objService.id;
            
            objMakeBillingAccount.objServId     =   objService.id;
            
            objMakeBillingAccount.objServBillId =   eval(strCadenaFactura+".value");
            objMakeBillingAccount.objServDesc   =   eval(strCadenaFactura+".options["+strCadenaFactura+".selectedIndex].text");
            
            //Cargamos data de Unico, Recurrente, Excesos
            strCadenaFacturaUnico       = "form.cmbFactura" + "Unico" + objService.id;
            objMakeBillingAccount.objChargeUniqueId     =   eval(strCadenaFacturaUnico+".value");
            objMakeBillingAccount.objChargeUnique       =   eval(strCadenaFacturaUnico+".options["+strCadenaFacturaUnico+".selectedIndex].text");
            
            strCadenaFacturaRecurrente  = "form.cmbFactura" + "Recurrente" + objService.id;
            objMakeBillingAccount.objChargeRecurrentId  =   eval(strCadenaFacturaRecurrente+".value");
            objMakeBillingAccount.objChargeRecurrent    =   eval(strCadenaFacturaRecurrente+".options["+strCadenaFacturaRecurrente+".selectedIndex].text");
            
            strCadenaFacturaExcesos     = "form.cmbFactura" + "Excesos" + objService.id;
            objMakeBillingAccount.objChargeExcesoId     =   eval(strCadenaFacturaExcesos+".value");
            objMakeBillingAccount.objChargeExceso       =   eval(strCadenaFacturaExcesos+".options["+strCadenaFacturaExcesos+".selectedIndex].text");

            vctAssignmentAccount.addElement(objMakeBillingAccount);
      }

    }
    
    fxConvertToCharacter(vctAssignmentAccount);
}

  function fxConvertToCharacter(vctAssignmentAccount){
    var strCadenaMain = "";
    var strCadena     = "";
    var objMakeBillingAccount = null;
    
    for(i=0; i<vctAssignmentAccount.size(); i++){
    
      objMakeBillingAccount = vctAssignmentAccount.elementAt(i);
      
      //Seteamos el Valor del Equipo
      //strCadena = objMakeBillingAccount.objEquipment;
      strCadena =   "";
      strCadena +=  "|EQP";
      //Flag que indica si es a nivel de Equipo
      //strCadena +=  fxGetLevelSave(objMakeBillingAccount.objEquipmentId);
      strCadena +=  fxGetSource(objMakeBillingAccount.objEquipment);
      strCadena +=  objMakeBillingAccount.objEquipmentId;
      
      strCadena +=  "|SRV";
      //Flag que indica si es a nivel de Servicio Adicional 
      strCadena +=  fxGetSource(objMakeBillingAccount.objServDesc);//fxGetLevelSave(objMakeBillingAccount.objServBillId);
      strCadena +=  objMakeBillingAccount.objServBillId;
      //Indica el Id del Servicio Adicional
      strCadena +=  "|";
      strCadena +=  objMakeBillingAccount.objServId;
      
      strCadena +=  "|SRVUN";
      //Si no es Existente
      strCadena +=  fxGetSource(objMakeBillingAccount.objChargeUnique);
      strCadena +=  objMakeBillingAccount.objChargeUniqueId;
      
      strCadena +=  "|SRVRE";
      //Si no es Existente
      strCadena +=  fxGetSource(objMakeBillingAccount.objChargeRecurrent);
      strCadena +=  objMakeBillingAccount.objChargeRecurrentId;
      
      strCadena +=  "|SRVEX";
      //Si no es Existente
      strCadena +=  fxGetSource(objMakeBillingAccount.objChargeExceso);
      strCadena +=  objMakeBillingAccount.objChargeExcesoId;
      
      strCadenaMain += strCadena
      
  }
  
  alert("Asignación strCadenaMain : " + strCadenaMain)
  //alert("Cantidad Items : " + parent.opener.vctItemsMainFrameOrder.size())
  
  if( parent.opener.vctItemsMainFrameOrder.size() == 1 ){
  
    parent.opener.frmdatos.item_billingAccount.value=strCadenaMain;
    
    try{
    parent.opener.frmdatos.hdnFlagSave.value = "A";
    }catch(e){}
  
  }else if ( parent.opener.vctItemsMainFrameOrder.size() > 1 ){
  
    parent.opener.frmdatos.item_billingAccount[<%=intIndex-1%>].value=strCadenaMain;
    try{
    parent.opener.frmdatos.hdnFlagSave[<%=intIndex-1%>].value = "A";
    }catch(e){}
    
  }else{
    alert("Se editaron los items mientras se escribiía en esta sección.");
  }
  
  //alert(strCadenaMain);
  
  //parent.mainFrame.parent.window.close();

}

  function fxGetSource(objAuxMakeBillingAccount){
    if(  objAuxMakeBillingAccount.indexOf("Existente") == -1 )
        return  "|S|";
    else
        return  "|E|";
  }

  function fxGetLevelSave(objAuxMakeBillingAccount){
  //Si el inidice es -2 indica que no es a su nivel
    if(  objAuxMakeBillingAccount == -2 )
      return "|N|";
    else
      return "|S|";
  }
    
</script>

<script defer>
  function fxloadBody(){
  
  form = document.frmdatos;
  //Llenamos la data la factura de Equipment
  //fxChargeSelects(form.cmbFacturaEquipment)
  
  //Llenamos la data de los Servicios Adicionales
  //alert(vctServices.size());
  /*
  if( vctServices.size() > 0 ) {  
    for( a=0; a<vctServices.size(); a++ ){
      var objService = vctServices.elementAt(a)
      var strCadenaFactura = "form.cmbFactura";
          strCadenaFactura += objService.id;
          eval("fxChargeSelects(strCadenaFactura)");
          
          //Añadimos data a Unico, Recurrente, Excesos
          strCadenaFacturaUnico       = "form.cmbFactura" + "Unico" + objService.id;
          eval("fxChargeSelects(strCadenaFacturaUnico)");
          strCadenaFacturaRecurrente  = "form.cmbFactura" + "Recurrente" + objService.id;
          eval("fxChargeSelects(strCadenaFacturaRecurrente)");
          strCadenaFacturaExcesos     = "form.cmbFactura" + "Excesos" + objService.id;
          eval("fxChargeSelects(strCadenaFacturaExcesos)");
    }
 
  }else{
     alert("No se han agregado Servicios Adicionales");
  }
  */
  /**Cargan los Responsables de Pago**/
  fxLoadResponsablePagos();
  fxLoadSiteWithHTML();
  fxLoadBillingAccount();
  /**Fin Carga Responsables de Pago**/
  //fxLoadSiteByService();
  fxSetValues();
  
  }


  function fxLoadResponsablePagos(){
   <% for(int i=0; ((objArraySiteExistsList != null) && (i<objArraySiteExistsList.size())); i++ ) {
            SiteBean objSiteBean =  objSiteBean = new SiteBean();
            objSiteBean = (SiteBean)objArraySiteExistsList.get(i);%>
        vctSitesEquipment.addElement(new fxMakeSite("<%=objSiteBean.getSwSiteId()%>","<%=objSiteBean.getSwSiteName()%>","<%=objSiteBean.getSwRegionName()%>","<%=objSiteBean.getNpStatus()%>"));
   <% }%>
   
   <% for(int i=0; ((objArraySiteSolicitedList != null) && (i<objArraySiteSolicitedList.size())); i++ ) {
            SiteBean  objSiteBean = new SiteBean();
            objSiteBean = (SiteBean)objArraySiteSolicitedList.get(i);%>
        vctSitesEquipment.addElement(new fxMakeSite("<%=(objSiteBean.getSwSiteId())%>","<%=objSiteBean.getSwSiteName()%>","<%=objSiteBean.getSwRegionName()%>","<%=objSiteBean.getNpStatus()%>"));
   <% }%>
 }
 
 
  function fxLoadBillingAccount(){
    var form = document.frmdatos;
  <%for(int j=0; ((objBillingBySiteExists!=null) && (j<objBillingBySiteExists.size())); j++ ){
      BillingAccountBean objBillAccBean = new BillingAccountBean();
      objBillAccBean = (BillingAccountBean)objBillingBySiteExists.get(j);%>
   parent.mainFrame.AddNewOption(form.cmbFacturaEquipment,"<%=objBillAccBean.getNpBillaccountNewId()%>","Existente - <%=MiUtil.getString(objBillAccBean.getNpBillacCName())%>");
  <% }%>
 
  <%for(int j=0; ((objBillingBySiteSolicited!=null) && (j<objBillingBySiteSolicited.size())); j++ ){
      BillingAccountBean objBillAccBean = new BillingAccountBean();
      objBillAccBean = (BillingAccountBean)objBillingBySiteSolicited.get(j);%>
   parent.mainFrame.AddNewOption(form.cmbFacturaEquipment,"<%=objBillAccBean.getNpBillaccountNewId()%>","Solicitado - <%=MiUtil.getString(objBillAccBean.getNpBillacCName())%>");
  <% }%>
  }
 
  function fxLoadChargeSiteData(objSelect){
    var form = parent.mainFrame.document.frmdatos;
  
    var objSite = null;
     for(i=0; i<vctSitesEquipment.size(); i++){
        objSite =  vctSitesEquipment.elementAt(i);
        opcion=new Option( objSite.siteStatus + " - " + objSite.siteName , objSite.siteId )
        /**Cargamos todos responsables de pago*/
        eval(objSelect+".options[i+1]=opcion;");
     }
  }
  
  function fxLoadSiteWithHTML(){
    fxLoadChargeSiteData("form.cmbSiteEquipment")
  }
  
  function fxLoadSiteByService(){
    fxLoadChargeSiteData("form.cmbSiteEquipment")
    if( vctServices.size() > 0 ) {  
      for( a=0; a<vctServices.size(); a++ ){
        var objService = vctServices.elementAt(a)
        var strCadenaSite = "form.cmbSiteEquipment";
            strCadenaSite += objService.id;
            eval("fxLoadChargeSiteData(strCadenaSite)");
            
            //Añadimos data a Unico, Recurrente, Excesos
            strCadenaSiteUnico       = "form.cmbSiteEquipment" + "Unico" + objService.id;
            eval("fxLoadChargeSiteData(strCadenaSiteUnico)");
            strCadenaSiteRecurrente  = "form.cmbSiteEquipment" + "Recurrente" + objService.id;
            eval("fxLoadChargeSiteData(strCadenaSiteRecurrente)");
            strCadenaSiteExcesos     = "form.cmbSiteEquipment" + "Excesos" + objService.id;
            eval("fxLoadChargeSiteData(strCadenaSiteExcesos)");
      }
    }
  }
  
</script>
  
</html>
