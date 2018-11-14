<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="pe.com.nextel.bean.NpObjHeaderSpecgrp" %>
<%@ page import="pe.com.nextel.bean.ItemDeviceBean" %>
<%@ page import="pe.com.nextel.bean.ItemBean" %>
<%@ page import="pe.com.nextel.bean.ProductBean" %>
<%@ page import="pe.com.nextel.bean.OrderBean" %>
<%@ page import="pe.com.nextel.bean.SpecificationBean" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.service.SessionService" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="pe.com.nextel.service.MassiveOrderService" %>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.text.NumberFormat"%>
<%@ page import="pe.com.nextel.bean.ServiciosBean" %>
<%
try{
   NewOrderService      objNewOrderService          = new NewOrderService();
   SessionService       objSession                  = new SessionService();
   GeneralService       objGeneralService           = new GeneralService();
   MassiveOrderService  objItemMassiveOrderService  = new MassiveOrderService();
   System.out.println("valores de entrada00================================");
   NumberFormat formatter = new DecimalFormat("#0.00");     
   ProductBean objProdBean = new ProductBean();
   OrderBean   objOrderBean = null;  
   
   Hashtable hshtinputNewSection = null;   
   HashMap   hshData=null;
   HashMap   hshScreenField=null;
   HashMap   hshOrder=null;
	 HashMap   hshItemEditable=null;   
   ArrayList objArrayListServiceBySolution = new ArrayList();
   HashMap   objHashItemOrder   = new HashMap();
   HashMap   objHashGeneric     = new HashMap();
   
   ArrayList objArrayItemOrder  = new ArrayList();
   ArrayList objArrayGeneric    = new ArrayList();
   
   String    strCustomerId= "",
             strnpSite ="",
             strCodBSCS = "",
             strSpecificationId = "",
             strDivision ="",
             strOrderId  = "",
             strStatus   = "",
             strMessage  = "",
             strSiteOppId = "",
             strUnknwnSiteId = "",
             strGeneratorType= "",
             strCompanyType = "";
            
   String    strSessionId="";
   int       strValiditem=0;
   String    strNumPaymentOrderId=""; 
   String	   strNumGuideNumber=""; 	 
   String    strDeleteItem="";

   long      lGeneratorId;
   String    strCustomerType="";
            
   SpecificationBean objSpecificationBean= null;
   
   String strPlan = "";
   MassiveOrderService objMassiveOrderService = new MassiveOrderService();
   ArrayList objArrayList        = new ArrayList();
   
   String strRutaContext   = request.getContextPath();
   String actionURL_MassiveOrderServlet      = strRutaContext+"/massiveorderServlet";
   //String URL_Order_CompaniaMassiveBuscar           = strRutaContext+"/htdocs/jsp/Order_CompaniaMassiveBuscar.jsp";
   String URL_Order_CompaniaMassiveBuscar           = strRutaContext+"/DYNAMICSECTION/DynamicSectionItemsMassive/DynamicSectionItemsMassivePage/Order_CompaniaMassiveBuscar.jsp";
   
   hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
   String URL_SSAA         = request.getContextPath()+"/DYNAMICSECTION/DynamicSectionItemsMassive/DynamicSectionItemsMassivePage/PopUpServices.jsp";
   
   if ( hshtinputNewSection != null ) {      
      strCustomerId           =   MiUtil.getString((String)hshtinputNewSection.get("strCustomerId"));
      strnpSite               =   MiUtil.getString((String)hshtinputNewSection.get("strSiteId"));
      strCodBSCS              =   MiUtil.getString((String)hshtinputNewSection.get("strCodBSCS"));
      strSpecificationId      =   MiUtil.getString((String)hshtinputNewSection.get("strSpecificationId"));
      strDivision             =   MiUtil.getString((String)hshtinputNewSection.get("strDivisionId"));
      strOrderId              =   MiUtil.getString((String)hshtinputNewSection.get("strOrderId"));
      strSessionId            =   MiUtil.getString((String)hshtinputNewSection.get("strSessionId"));
      strStatus               =   MiUtil.getString((String)hshtinputNewSection.get("strStatus")); 
      strGeneratorType        =   MiUtil.getString((String)hshtinputNewSection.get("strGeneratorType"));
      lGeneratorId            =   MiUtil.parseLong((String)hshtinputNewSection.get("strGeneratorId"));        
      System.out.println("strCustomerId=============="+strCustomerId);
      System.out.println("strOrderId=============="+strOrderId);
      System.out.println("strnpSite=============="+strnpSite);
      if(strnpSite.equals("")) strnpSite="0";
      
      if(strnpSite.equals("0")) {
		    strCustomerType=Constante.CUSTOMERTYPE_CUSTOMER; // Si el valor es 0 no ingreso Site en la pantalla principal		    
	    }
	    else{
		    strCustomerType=Constante.CUSTOMERTYPE_SITE;		    
	    }	
      
   }
   
   PortalSessionBean objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
   if( objPortalSesBean == null ){
     throw new Exception("No se encontró la sesión del usuario");
   }
      
   
   strCompanyType    = (String)request.getParameter("strTypeCompany");
   
   /* Se Obtiene el numero el maximos numero de registros que se podra almacenar */
   int numMaxRecord = 0;
   objHashGeneric = objGeneralService.getTableList("MASSIVE_MAX_RECORDS","1"); 
   strMessage=(String)objHashGeneric.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage); 
      
    objArrayGeneric=(ArrayList)objHashGeneric.get("arrTableList");
    //if( objArrayGeneric != null ){
    if( objArrayGeneric.size() > 0 ){
        HashMap hsMapMaxRecord = new HashMap();               
        for(int i=0; i<objArrayGeneric.size(); i++ ) {  
                hsMapMaxRecord = (HashMap)objArrayGeneric.get(i);                  
                System.out.println("Valor de HM "+hsMapMaxRecord.get("wv_npValue"));                   
                numMaxRecord = Integer.parseInt((String)hsMapMaxRecord.get("wv_npValue"));                   
        }
      }else {
            // Valor por default
              numMaxRecord = Constante.MASSIVE_MAX_RECORD_DEFAULT;
      }
   
  
   %>
  
  <link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
  <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
  <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
  <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXEdit.js"></script>
  
  <br>
  <table border="0" cellspacing="0" cellpadding="0" id="tbCabeceraServiciosAdicionales">
    <tr class="PortletHeaderColor">
      <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="16"></td>
      <td class="SubSectionTitle" align="left" valign="top">Razón Social Cedente</td>
      <td class="SubSectionTitleRightCurve" valign="top" align="right" width="12"></td>
    </tr>
  </table>
  
  <input type="HIDDEN" name="hdnCompanyaName">
  <input type="hidden" name="hdnCustId1">
  <input type="HIDDEN" name="hdnSpecificationId" value="<%=strSpecificationId%>">
  <input type="HIDDEN" name="hdnCompanyType" value="<%=strCompanyType%>">
  <input type="HIDDEN" name="hdnDivisionId" value="<%=strDivision%>">
  <input type="HIDDEN" name="hdnNpSiteId" value="<%=strnpSite%>">
  <input type="HIDDEN" name="hdnNpOrderId" value="<%=strOrderId%>">
  <input type="HIDDEN" name="hdnCantSiteAcep" value="">
  
  <table border="1" width="80%" cellpadding="2" cellspacing="0" class="RegionBorder">
    <tr align="center">
      <td class="RegionHeaderColor" valign="top">
        <table border="0" cellspacing="1" cellpadding="0" width="100%">
          <tr>
            <td class="CellLabel" align="left"><font color="red"><b>*</b></font>          
            <a href="javascript:searchConpanya();">&nbsp;Razón Social</a>                    
            </td>
            <td align="left" class="CellLabel" ></td>
            <td align="left" class="CellLabel" >Responsable de Pago&nbsp;</td>
            <td align="left" class="CellLabel" ></td>
          </tr>
          <tr>
            <td width="40%" class="CellContent" align="left">&nbsp;<input type="text" value="" style="text-transform:uppercase" name="txtCompanya" size="50" maxlength="100" onKeyDown="javascript:checkKey_BuscarClienteMasivo('txtCompany',event,11);" > 
            </td>
            <td class="CellContent" align="left"><a href="javascript:getCustomerDetailMassive2()"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" alt="Detalle de cliente" width="15" height="15" border="0"></a></td>
            
            <td width="40%" align="left" class="CellContent">&nbsp;
              <select name="cmbResponsablePagoMasivos" onchange="javascript:fxChangeSite();" >
                <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
              </select> 
               <input type="hidden" name="hdnResponsablePagoMasivos"> 
            </td>
            
            <td align="center" class="CellContent">
              <input type="button" value="Buscar" name="btnSearchMassive" onclick="javascript:fxSearchMassiveTransfer();">
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
            
  <br>
  <div id="idItemsMassiveTransfer">
      
  </div>
   <br>
   
   
   
<script language="javascript" DEFER>
  //var vplan =  new Vector();
  var vServicio  = new Vector();
  var vServicioPorSolucion  = new Vector();
  var str_list_act = "";
  var str_list_des = "";
  var str_list_final = "";
  var str_list_services = "";
  
  function getCustomerDetailMassive2(){
    form = document.frmdatos;
    customerId = form.hdnCustId1.value;
       
    if (customerId=="" || customerId=="0" ){
       alert("Antes de ver el detalle debe de haber elegido un cliente");
       return;
    }
    url = "/portal/page/portal/nextel/CUST_DETAIL?customerId=" + customerId;
    url = "/portal/pls/portal/WEBSALES.NPSL_GENERAL_PL_PKG.WINDOW_FRAME?av_title="+escape("PORTAL NEXTEL")+"&av_url="+escape(url);
    window.open(url,"WinCustomer","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,screenX=60,top=40,left=60,screenY=40,width=850,height=600");
    return;
  }
  
  function searchCustomerMassive(){
		form = document.frmdatos;
		v_companhia = form.txtCompanya.value;	
    
		var v_parametros = "?sUrl=<%=URL_Order_CompaniaMassiveBuscar%>"+"¿hdnSessionLevel1=" + "<%=objPortalSesBean.getLevel()%>"+"|hdnSessionCode1="+"<%=objPortalSesBean.getCode()%>"+"|hdnSessionLogin1="+"<%=objPortalSesBean.getLogin()%>"+"|txtCompanya="+v_companhia+"|hdnProGrpId1="+"<%=objPortalSesBean.getProviderGrpId()%>";
		url= "<%=request.getContextPath()%>/htdocs/jsp/PopUpCompanyFrame.jsp"+ v_parametros;	
		form.txtCompanya.value=""; //(similar a Dia a Dia - limpiar el campo)
		WinAsist = window.open(url,"WinSearchCustomer","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=no,screenX=100,top=80,left=100,screenY=80,width=700,height=500,modal=yes");
  }
  
  function searchConpanya(){
		form = document.frmdatos;
		v_companhia = form.txtCompanya.value;    
    
		var v_parametros = "?sUrl=<%=URL_Order_CompaniaMassiveBuscar%>"+"¿hdnSessionLevel1=" + "<%=objPortalSesBean.getLevel()%>"+"|hdnSessionCode1="+"<%=objPortalSesBean.getCode()%>"+"|hdnSessionLogin1="+"<%=objPortalSesBean.getLogin()%>"+"|hdnProGrpId1="+"<%=objPortalSesBean.getProviderGrpId()%>";
		url= "<%=request.getContextPath()%>/htdocs/jsp/PopUpCompanyFrame.jsp"+ v_parametros;        
		WinAsist_Inc = window.open(url,"WinAsistCompanySearch","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=no,screenX=100,top=80,left=100,screenY=80,width=700,height=500,modal=yes");
  }
   
  function fxMassiveServlet(){
    var index = table_assignment.rows.length;
    
    /*if (index > 1){
      for(var i=0; i < index - 1  ; i++){  
        if (form.chkPhoneNumber[i].disabled == false && form.chkPhoneNumber[i].checked == true){   
          form.txtItemActSSAA[i].value = "";
          form.txtItemMsjResSSAA[i].value = "";
          form.hdnItemMsjResSSAA[i].value = "ERROR";
          form.txtItemDesSSAA[i].value = "";
        }
      }
    }*/
      
    document.frmdatos.myaction.value = 'obtenerServicios';       
    form.action="<%=actionURL_MassiveOrderServlet%>";
    document.frmdatos.submit();              
  }
  
  function fxSetListItemService(listActive){     
    var arrActive;
    var str="";
    
    if (listActive!= "" ){     
      arrActive = listActive.split("|");
     
      for(i=0; i<arrActive.length; i++){       
        if(arrActive[i]!="S")
          str = str + "|"+ arrActive[i];        
      }
     str = str.substring(1);
    }

    return str;
  }
  
  //Verifica que el ssaa petenezca a la solución del item seleccionado   
  function fxVerifyActiveSSAA(iSolId,listActivados){
    var nLength = vServicioPorSolucion.size();
    var arr_strActivados;
    var strEstado;
    var strMessageSol = "";
         
    arr_strActivados = listActivados.split("|");    
    for (m=0;m<arr_strActivados.length;m++){               
      strEstado = "NO";
      for(r=0; r<nLength ;r++){         
        objServicioPorSolucion = vServicioPorSolucion.elementAt(r);

        if(objServicioPorSolucion.idSol == iSolId){
          if(arr_strActivados[m] == objServicioPorSolucion.idServ){
            strEstado = "SI";                
            break;
          }
        }       
         
      }   
      
      if (strEstado != "SI"){               
        strMessageSol = strMessageSol + "," + fxGetNameSSAA(arr_strActivados[m]); //objServicioPorSolucion.nameShort;
      }
       
    }     
    //alert("strMessageSol=="+strMessageSol);
    return strMessageSol.substring(1);
  }
  
  //Verifica que los ssaa a activar no sean excluyentes
   function fxVerifyExcludeSSAA(iSolId,listActivados,listActivos){
     var arr_strActivados; //ssaa que se van a activar
     var arr_strActivos; //ssaa con los que cuenta el equipo
     var arr_ServiciosTipo; // arreglo de servicios con el campo exluding
     var intTotal;
     var strEstado;
     var strMessage = "";
             
     arr_strActivados = listActivados.split("|");
     arr_strActivos = listActivos.split("|");
     
     intTotal = arr_strActivados.length + arr_strActivos.length;
     arr_ServiciosTipo = new Array(intTotal);
     
     var j = 0;
     for(i=0; i<arr_strActivados.length; i++){ 
       arr_ServiciosTipo[j] = new Array(2);
       arr_ServiciosTipo[j][0] = arr_strActivados[i];
       arr_ServiciosTipo[j][1] = '';
       j++;
     }
     for(i=0; i<arr_strActivos.length; i++){ 
       arr_ServiciosTipo[j] = new Array(2);              
       arr_ServiciosTipo[j][0] = arr_strActivos[i];
       arr_ServiciosTipo[j][1] = '';
       j++;
     }
     
     //obtenemos el excluding del servicio
     for(i=0; i<arr_ServiciosTipo.length; i++)     
       arr_ServiciosTipo[i][1] = fxGetServiceRol(iSolId, arr_ServiciosTipo[i][0]);        
     
     //strEstado = "";    
     for(i=0;i< arr_ServiciosTipo.length - 1; i++){ 
       strEstado = "NO";
       for(j=i+1; j<arr_ServiciosTipo.length; j++){         
         if(arr_ServiciosTipo[i][1] == arr_ServiciosTipo[j][1] ){
           //strEstado = "Ya existe un Servicio Excluyente para " + arr_ServiciosTipo[i][1];
           //break;
           //strEstado =  strEstado + "," + arr_ServiciosTipo[i][1];
           strEstado = "SI";
           break;
         }
       }
       /*if(strEstado !=  "SI")
         break;    */   
         if (strEstado ==  "SI" && arr_ServiciosTipo[i][1] == ""){
            if ( arr_ServiciosTipo[i][0] == arr_ServiciosTipo[j][0])
               strMessage =  strMessage + "," + fxGetNameSSAA(arr_ServiciosTipo[i][0]);
         }
         if (strEstado ==  "SI" && arr_ServiciosTipo[i][1] != ""){
            strMessage =  strMessage + "," + arr_ServiciosTipo[i][1];
         }
         
     }

     //return strEstado.substring(1);
     return strMessage.substring(1);
   }
  
  //Actualiza la lista de ssaa a activar que se vizualizará en el pop up
  function fxUpdateSSAAActive(listActivos,listActivar){//ssaa actuales, ssaa a activar
    var arrActivar;
     
    arrActivar = listActivar.split("|");

    for (i=0; i<arrActivar.length; i++){
      listActivos = listActivos + "|"+arrActivar[i] + "|"+"N"+"|"+"S";
    }
    str_list_final = listActivos;
     
  }
  
  //Funcion que devuelve el campo excluding de un servicio
  function fxGetServiceRol(iSolId, iServicioId){
    var nLength = vServicioPorSolucion.size();     
    var strExclude = "NADA";
     
    for(k=0; k<nLength; k++){ 
      objServicioPorSolucion = vServicioPorSolucion.elementAt(k);
      if(objServicioPorSolucion.idSol == iSolId && objServicioPorSolucion.idServ == iServicioId)
        strExclude = objServicioPorSolucion.exclude;
    }
     
    return strExclude;    
  }
   
  function fxVerifyDesactiveSSAA(listDesactivados,listActivos){
    var arrDesactivados;
    var arrActivos;
    var strEstado;
    var strMessage="";

    arrDesactivados = listDesactivados.split("|");
    arrActivos = listActivos.split("|");
    //strEstado = "NO";
    for (i=0; i< arrDesactivados.length ; i++){  
      strEstado = "NO";
      for (j=0; j< arrActivos.length; j++){
        if(arrDesactivados[i] == arrActivos[j]){
           strEstado = "SI";            
           break;
        }
      }
      //if ( strEstado == "NO"){
      if ( strEstado != "SI"){
        //strMessage = "Servicio: "+fxGetNameSSAA(arrDesactivados[i])+ " no se encuentra activo";
        //break;
        strMessage = strMessage + "," + fxGetNameSSAA(arrDesactivados[i]);
      }
    }
     
    return strMessage.substring(1);
  }
   
  //Función que devuelve el nombre del servicio adicional 
  function fxGetNameSSAA(idServ){
    var nCant = vServicio.size();
    var strName="";
     
    for (n=0; n<nCant; n++){                    
      objServicio = vServicio.elementAt(n);
      
      if(objServicio.id == idServ){
        strName = objServicio.nameShort;
        break;
      }       
    }
     
    return strName;
  }
  
     //Actualiza la lista de ssaa a desactivar que se vizualizará en el pop up
  function fxUpdateSSAADesactive(listFinal,listDesactivar){
    var arrActivados;
    var arrDesactivados;
    var listActive="";
   
    arrActivados    = listFinal.split("|");
    arrDesactivados = listDesactivar.split("|");
   
    for (i=0; i<arrDesactivados.length; i++){    
      for (j=0; j<arrActivados.length; j++){
        if(arrDesactivados[i] == arrActivados[j]){
          arrActivados[j+2] = "N";
        }
      }
    }
     
    for(i=0; i<arrActivados.length; i++){
      listActive = listActive + "|" + arrActivados[i];
    }     
     
    str_list_final = listActive.substring(1);
     
  }
    
  function fxApplyServiceMassive(){
     var form = document.frmdatos; 
     var index = table_assignment.rows.length;
     var bError;
     var faltaTransfer = false;
     str_list_act = "";
     str_list_des = "";
     str_list_services;

     
     if (document.frmdatos.cmbActiveServicesMassive.length < 1 &&
       document.frmdatos.cmbDesactiveServicesMassive.length < 1){
       alert("Debe seleccionar por lo menos un Servicio Disponible a [ACTIVAR] ó [DESACTIVAR]");
       return false;
     }
     

     //Obtenemos los SSAA a Activar Masivamente
     if (document.frmdatos.cmbActiveServicesMassive.length > 0){
       for(i=0; i< document.frmdatos.cmbActiveServicesMassive.length; i++)
         str_list_act = str_list_act + "|" + form.cmbActiveServicesMassive.options[i].value;
       str_list_act = str_list_act.substring(1);
       //alert("SSAA Active========================="+str_list_act);
     }
        
     //Obtenemos los SSAA a Desactivar Masivamente
     if (document.frmdatos.cmbDesactiveServicesMassive.length > 0){
       for(i=0; i< document.frmdatos.cmbDesactiveServicesMassive.length; i++)
         str_list_des = str_list_des + "|" + form.cmbDesactiveServicesMassive.options[i].value;
       str_list_des = str_list_des.substring(1);
       //alert("SSAA Desactive================="+str_list_des)
     }
     
     if (index > 1){
       for(var i=0; i < index - 1  ; i++){   
         bError = false;
         str="";
         str_list_final="";
         str_list_services="";
         if (form.chkPhoneNumber[i].disabled == false && form.chkPhoneNumber[i].checked == true){ 
           form.txtItemActSSAA[i].value = "";
           form.txtItemDesSSAA[i].value = "";
           form.txtItemMsjResSSAA[i].value = "";
           form.hdnItemMsjResSSAA[i].value = "ERROR";
           form.hdnItemServicesNew[i].value = form.hdnItemServices[i].value;
           
           if (form.hdnItemServices[i].value == ""){
             //alert("Falta Aplicar [TRANSFERENCIA]");
             faltaTransfer = true;
             //return false;
           }
           
           if (form.hdnItemServices[i].value != ""){           
             str = fxSetListItemService(form.hdnItemServices[i].value);//se obtiene lista con formato: 180|370
             //alert("str==============================>"+str);
             if (document.frmdatos.cmbActiveServicesMassive.length > 0){       
               //Verificamos que los ssaa a activar pertenezcan a la solucion del item
               strMessage = fxVerifyActiveSSAA(form.hdnItemSolutionId[i].value,str_list_act);
               if (strMessage == ""){
               //if (fxVerifyActiveSSAA(form.hdnItemSolutionId[i].value,str_list_act)){
                 form.txtItemActSSAA[i].value = "       SI";
                 form.txtItemActSSAA[i].style.color = "blue";
                 //if (form.txtItemDesSSAA[i].value == "       NO")
                 //  form.txtItemDesSSAA[i].value = "";
                 //alert("entraa SUCESS SOLUTION==");
                 //Luego verificamos que no sean Excluyentes  
                 strMessage = fxVerifyExcludeSSAA(form.hdnItemSolutionId[i].value,str_list_act,str);//form.hdnItemServices[i].value
                // if( strMessage == "SI"){
                 if( strMessage == ""){
                   //Informativo
                   form.txtItemActSSAA[i].value = "       SI";
                   form.txtItemActSSAA[i].style.color = "blue";
                   //if (form.txtItemDesSSAA[i].value == "       NO")
                   //form.txtItemDesSSAA[i].value = "";
                  
                   form.txtItemMsjResSSAA[i].value = "SUCCESS";
                   form.hdnItemMsjResSSAA[i].value = "SUCCESS";
                   form.txtItemMsjResSSAA[i].style.color = "blue";
                   //alert("strMessage================>"+strMessage);
                   //si todo ha salido ok se actualiza los ssaa a mostrar en el pop up
                   fxUpdateSSAAActive(form.hdnItemServices[i].value,str_list_act);//lista de ssaa que tiene el equipo,lista de ssaa a acivar
                   form.hdnItemServicesNew[i].value = str_list_final;
                   //alert("final=================="+form.hdnItemServicesNew[i].value);
                 }else{
                   form.txtItemActSSAA[i].value = "       NO";
                   form.txtItemActSSAA[i].style.color = "red";
                                    
                   //alert("entraa ERROR EXCLUDE==");
                   form.txtItemMsjResSSAA[i].value = "ERROR : "+ strMessage;                
                   form.txtItemMsjResSSAA[i].style.color = "red";
                   bError = true;
                   //si hay error al momento de levantar el pop up sólo se vizualiza los ssaa que tiene el equipo
                   form.hdnItemServicesNew[i].value = form.hdnItemServices[i].value;
                 }
               }
               else{
                 //alert("entraa ERROR SOLUTION==");
                 form.txtItemActSSAA[i].value = "       NO";
                 form.txtItemActSSAA[i].style.color = "red";
                 
                 form.txtItemMsjResSSAA[i].value = "ERROR : "+ strMessage; //"ERROR: Servicio Adicional no petenerce a la Solución del Equipo";            
                 form.txtItemMsjResSSAA[i].style.color = "red";
                 bError = true;
                 //si hay error al momento de levantar el pop up sólo se vizualiza los ssaa que tiene el equipo
                 form.hdnItemServicesNew[i].value = form.hdnItemServices[i].value;
               }
               
             }
             
             if (!bError){           
               if (document.frmdatos.cmbDesactiveServicesMassive.length > 0){
                 str = fxSetListItemService(form.hdnItemServices[i].value);//se envia la lista original de ssaa que tiene el equipo
                                                                          //se obtiene lista con formato: 180|370
                 
                 //Verificamos que los ssaa a desactivar pertenezcan al equipo seleccionado   
                 var strMessageDes = fxVerifyDesactiveSSAA(str_list_des,str);//form.hdnItemServices[i].value
                 if( strMessageDes == ""){
                   form.txtItemDesSSAA[i].value = "       SI";
                   form.txtItemDesSSAA[i].style.color = "blue"; 
                   if (form.txtItemActSSAA[i].value == "       NO")
                   form.txtItemDesSSAA[i].value = "";
                   
                   form.txtItemMsjResSSAA[i].value = "SUCCESS";
                   form.hdnItemMsjResSSAA[i].value = "SUCCESS";
                   form.txtItemMsjResSSAA[i].style.color = "blue"; 
                   
                   //si todo ha salido ok se actualiza los ssaa a mostrar en el pop up
                   fxUpdateSSAADesactive(form.hdnItemServicesNew[i].value,str_list_des);
                   form.hdnItemServicesNew[i].value = str_list_final;
                   //alert("al final de desactivar========"+form.hdnItemServicesNew[i].value);
                 }else{
                   form.txtItemDesSSAA[i].value = "       NO";
                   form.txtItemDesSSAA[i].style.color = "red";   
                   if (form.txtItemActSSAA[i].value == "       SI")
                   form.txtItemActSSAA[i].value = "";
                   //form.txtItemActSSAA[i].value = "";
                   
                   form.txtItemMsjResSSAA[i].value = "ERROR: "+strMessageDes;                 
                   form.txtItemMsjResSSAA[i].style.color = "red";
                   bError = true;
                   //si hay error al momento de levantar el pop up sólo se vizualiza los ssaa que tiene el equipo
                   form.hdnItemServicesNew[i].value = form.hdnItemServices[i].value;
                 }
               }
             }
             
           }else{//Caso cuando no no se selecciona equipos 
             /*form.txtItemActSSAA[i].value = "";
             form.txtItemDesSSAA[i].value = "";
             form.txtItemMsjResSSAA[i].value = "";
             form.hdnItemMsjResSSAA[i].value = "OFF";*/
           }
         }  // end form.hdnItemServices[i].value != ""
       }
     }
     
     if (faltaTransfer == true){
        alert("Falta Aplicar [TRANSFERENCIA] a algunos Equipos Seleccionados.");
     }
     
  }

  
  function fxAddServiceMassive(listsource,listdestiny){
     for (i=eval("document.frmdatos."+listsource+".options.length")-1;i>=0;i--){
       if (eval("document.frmdatos." + listsource + ".options[i].selected")){
         marca = eval("document.frmdatos." + listsource + ".options[i].text");
         codigo = eval("document.frmdatos." + listsource + ".options[i].value");
         control = eval("document.frmdatos." + listdestiny);
         var option1 = new Option(marca,codigo,control.length);
         control.options.add(option1);
         expression = "document.frmdatos." + listsource + ".options.remove(" + i + ")";
         eval(expression);
       }
     }
  }
  
  function fxCleanServiceMassive(listsource,listdestinyactive,listdestinydesactive){
    var index = table_assignment.rows.length;
      
     if (index > 1){
       for(var i=0; i < index - 1  ; i++){  
         if (form.chkPhoneNumber[i].disabled == false && form.chkPhoneNumber[i].checked == true){   
           form.txtItemActSSAA[i].value = "";
           form.txtItemMsjResSSAA[i].value = "";
           form.hdnItemMsjResSSAA[i].value = "ERROR";
           form.txtItemDesSSAA[i].value = "";
           form.hdnItemServicesNew[i].value = form.hdnItemServices[i].value;
         }
       }
     }
  }
  
  function fxCleanPlan(){
    var index = table_assignment.rows.length;
    
    if (index > 1){
      for(var i=0; i < index - 1  ; i++){  
        if (form.chkPhoneNumber[i].disabled == false && form.chkPhoneNumber[i].checked == true){   
          form.cmbNewPlanTarifario[i].value = "";
          form.hdncmbNewPlanTarifario[i].value = "";
          form.txtItemPriceExc[i].value = "";
          form.hdnItemNewPlanId[i].value = form.hdnItemOrigPlanId[i].value;
          form.hdnItemNewPlanNameId[i].value = form.txtItemOrigPlan[i].value;
        }
      }
    }
  }
  
  function fxAplicarPlan(){    
    var form = document.frmdatos; 
    var index = table_assignment.rows.length;
    var faltaTrans = false;
    
    if (index > 1){              
      for (var i =0; i < index - 1 ; i++){
  
        if (form.chkPhoneNumber[i].disabled == false && form.chkPhoneNumber[i].checked == true ){ 
          
          if (form.hdnGetServicio[i].value == "true" && form.txtItemLine[i].value == "Transferencia"){
                if (form.cmb_PlanTarifario.value!= ""){
                  //form.cmbNewPlanTarifario[i].value = form.cmb_PlanTarifario.value;
                  //form.hdncmbNewPlanTarifario[i].value = form.cmbNewPlanTarifario[i].value;
                  ///form.hdnItemNewPlanId[i].value = form.cmbNewPlanTarifario[i].value;
                  form.hdnItemNewPlanId[i].value = form.cmb_PlanTarifario.value;
                  //form.hdnItemNewPlanNameId[i].value = form.cmbNewPlanTarifario[i].options[form.cmbNewPlanTarifario[i].selectedIndex].text;                   
                  form.hdnItemNewPlanNameId[i].value = form.cmb_PlanTarifario.options[form.cmb_PlanTarifario.selectedIndex].text;   
                  form.cmbNewPlanTarifario[i].value = form.cmb_PlanTarifario.options[form.cmb_PlanTarifario.selectedIndex].text;   
                  //form.cmbNewPlanTarifario[i].selected;            
                  form.txtItemPriceExc[i].value = form.txtPriceExcep.value;
                  
                  if (form.hdnItemNewPlanId[i].value == ""){                   
                    form.hdnItemNewPlanId[i].value = form.hdnItemOrigPlanId[i].value;
                    form.hdnItemNewPlanNameId[i].value = form.txtItemOrigPlan[i].value;                     
                  }
                  
                }else{
                  form.txtItemPriceExc[i].value = form.txtPriceExcep.value;
                }
            }else {
               
                faltaTrans = true;
            
            }
        }
      }             
    }  
    
        if (faltaTrans == true){        
              alert('Falta Aplicar [TRANSFERENCIA] a algunos Equipos Seleccionados.');
        }
        
  }
  
  /*function checkedItem( objValue, objIndex ){       
    var form = document.frmdatos;
    var index = objIndex - 1;
    var numRow = table_assignment.rows.length;
    
    if (numRow > 2){      
      if (form.chkPhoneNumber[index].checked == true){
        form.hdncmbNewPlanTarifario[index].value = form.cmbNewPlanTarifario[index].value;
        form.hdnItemNewPlanId[index].value = form.cmbNewPlanTarifario[index].value;
        form.hdnItemNewPlanNameId[index].value = form.cmbNewPlanTarifario[index].options[form.cmbNewPlanTarifario[index].selectedIndex].text; 
      }else{
        alert("Seleccione el Equipo");
        //form.cmbNewPlanTarifario[index].value = "";
        //form.hdncmbNewPlanTarifario[index].value = "";
        form.hdnItemNewPlanId[i].value = form.hdnItemOrigPlanId[index].value;
        form.hdnItemNewPlanNameId[i].value = form.txtItemOrigPlan[index].value;
        return false;
      } 
    }
  }  */
  
  function fxCheckedItem( objValue, objIndex ){       
    var form = document.frmdatos;
    var index = objIndex - 1;
    var numRow = table_assignment.rows.length;
          
    if (numRow > 2){
      if (objValue == true){
        form.hdnchkPhone[index].value = "on";
      }else{
        form.hdnchkPhone[index].value = "off";
      }  
    }
  }      
    
  function fxShowSSAA(obj,rowIndex){  
    var form = document.frmdatos;
    var list_act    = form.hdnItemServicesNew[rowIndex-1].value;
    var solutionId  = form.hdnItemSolutionId[rowIndex-1].value;
    var ItemActSSAA = form.txtItemActSSAA[rowIndex-1].value;
    var ItemDesSSAA = form.txtItemDesSSAA[rowIndex-1].value;
     
    if (list_act == ""){
      alert("Falta Aplicar [TRANSFERENCIA]");
      return false;
    }
     
    url= "<%=URL_SSAA%>?strListAct="+list_act+"&strIndex="+rowIndex+"&strSolutionId="+solutionId;     
    var popupWin = window.open(url, "WinAsist2001","status=yes, location=0, width=550, scrollbars=yes, height=400, left=300, top=30, screenX=50, screenY=100");
  }
  
  function fxLimpiarCesion(){
    var form = document.frmdatos; 
    var index = table_assignment.rows.length;
    var count = 0;
    
    if (index > 1){       
      for (var i =0; i < index - 1; i++){
        if (form.chkPhoneNumber[i].disabled == false && form.chkPhoneNumber[i].checked == true ){          
          form.txtItemLine[i].value = "";
          form.hdnItemLine[i].value = "ERROR";
          form.txtItemPrice[i].value = "";
          form.txtItemPriceExc[i].value = "";
          form.txtItemMsjResSSAA[i].value = "";
          form.txtItemActSSAA[i].value = "";
          form.txtItemDesSSAA[i].value = "";
          /*form.cmbNewPlanTarifario[i].value = null;
          form.hdncmbNewPlanTarifario[i].value = null;*/
          form.cmbNewPlanTarifario[i].value = "";
          form.hdncmbNewPlanTarifario[i].value = "";
          form.hdnItemServicesNew[i].value = form.hdnItemServices[i].value;
          
          form.hdnItemNewPlanId[i].value = form.hdnItemOrigPlanId[i].value;
          form.hdnItemNewPlanNameId[i].value = form.txtItemOrigPlan[i].value;
        }
        
        if (form.hdnItemLine[i].value == "Transferencia"){                   
          count++;                        
        }
        
      }
      
      form.txtNumRecord.value = '  '+count;
      
    }
  }
  
  function fxAplicarCesion(){
    var form = document.frmdatos; 
    var index = table_assignment.rows.length;
    var count = 0;
        
    //if (index > 1){       
    if (index > 2){       
      for (var i =0; i < index - 1; i++){
        if (form.chkPhoneNumber[i].disabled == false && form.chkPhoneNumber[i].checked == true ){            
          form.txtItemLine[i].value = "Transferencia";
          form.hdnItemLine[i].value = "Transferencia";
          form.txtItemPrice[i].value = form.hdnItemPrice[i].value;
        }
        
        if(form.hdnItemLine[i].value == "Transferencia"){
          count++;  
        }
      
      }  
      
      form.txtNumRecord.value = '  '+count;
      
      fxMassiveServlet();
    }
  }
  
  function checkedAll () {         
    var form = document.frmdatos; 
    var index = table_assignment.rows.length;
      
    //if (index > 1){
    if (index > 2){
      if (form.chkPhoneNumberAll.checked == true){          
        for (var i =0; i < index - 1; i++){
          if (form.chkPhoneNumber[i].disabled == false){
            form.hdnchkPhone[i].value = "on";
            form.chkPhoneNumber[i].checked = true;             
            //form.txtItemLine[i].value = "Transferencia";
            //form.hdnItemLine[i].value = "Transferencia";
            //form.txtItemPrice[i].value = form.hdnItemPrice[i].value;
          }
        }
      }else{
        for (var i =0; i < index - 1; i++){  
          form.hdnchkPhone[i].value = "off"; 
          form.chkPhoneNumber[i].checked = false;  
          //form.txtItemLine[i].value = "";
          //form.hdnItemLine[i].value = "ERROR"
          //form.txtItemPrice[i].value = "";
        }    
      }
    }
  }
  
  function fxInputTabEnter() {
	  return ((window.event.keyCode == 9) || (window.event.keyCode == 13));
	}
   
  function checkKey_BuscarClienteMasivo(tipo,e,num1){   
  
    if(fxInputTabEnter()) {
      form = document.frmdatos;
      form.txtCompanya.value      = trim(form.txtCompanya.value.toUpperCase());
      form.hdnCompanyaName.value   = trim(form.txtCompanya.value.toUpperCase());
      
      var company = form.hdnCompanyaName.value;
      form.hdnCantSiteAcep.value = form.cmbResponsablePago.length - 1;
      
      if (num1==10 || num1==11) {
        if(tipo == "txtDato") {
          doSubmit("buscarCliente"); 
        }
          
        if(tipo == "txtCompany") {
              
          if( document.frmdatos.txtCompanya.value != "" ){         
            form.myaction.value = 'buscarCliente';
            form.detallemyaction.value = 'compania';
            form.action="<%=actionURL_MassiveOrderServlet%>";
            form.submit();
          }else{
            doSubmit("limpiarCliente");
          }
            
        }
          
      } 
    }
    
  }
  
  function fxSearchMassiveTransfer(){
    var form = document.frmdatos;
    var countSites = form.cmbResponsablePagoMasivos.length - 1;          
    var companya = form.txtCompanya.value;
    
    if (companya != "" &&  countSites != "0"){
      // if(form.cmbResponsablePagoMasivos.value != ""){
          form.hdnResponsablePagoMasivos.value = form.cmbResponsablePagoMasivos.value;          
          form.myaction.value = 'buscarCliente';
          form.detallemyaction.value = 'siteId';
          form.action="<%=actionURL_MassiveOrderServlet%>";
          form.submit();
       /*}else{
          alert("Debe Seleccionar Responsable Pago del Cedente.");
          return false;
       }*/
    
    }
    if(companya != "" &&  countSites == "0"){       
       form.myaction.value = 'buscarCliente';
       form.detallemyaction.value = 'companiaName';
       form.action="<%=actionURL_MassiveOrderServlet%>";
       form.submit();
    }
    
  }
  
  function fxChangeSite(){
     var form = document.frmdatos;
     var countSites = form.cmbResponsablePagoMasivos.length - 1;          
     var companya = form.txtCompanya.value;
    
     if (companya != "" &&  countSites != "0"){
        form.myaction.value = 'limpiarCliente';
        //form.detallemyaction.value = 'companiaName';
        form.action="<%=actionURL_MassiveOrderServlet%>";
        form.submit();
     }
  }
 
</script> 

<script defer>

  function fxValidateMassiveDataTransfer(){
    var form = document.frmdatos; 
    var index = table_assignment.rows.length - 2;
    var lastIndex = table_assignment.rows.length - 2;
    var rowIndex = table_assignment.rows.length;
    var numMaxItems = <%=numMaxRecord%>;
    
    if (rowIndex > 2){
    
      if(form.txtNumRecord.value > numMaxItems){
        alert('No se puede enviar mas de ['+numMaxItems+'] Registros');
        return false;
      }
      
      for (var i =0; i <= index ; i++){         
        if (form.txtItemLine[i].value != "" && form.txtItemLine[i].value == "Transferencia" && form.hdnItemServices[i].value != ""){    
          return true;
        }else{                       
           if ( i == lastIndex ){
           alert ("Debe Aplicar [TRANSFERENCIA]");
           return false;
           }
        }
      }
    }else{
       alert('Solo se permite Órdenes para Clientes Mayores');
       return false;
    }
  }
  
   
  
</script>


<%      
}catch(Exception  ex){                
   System.out.println("Error try catch -->"+MiUtil.getMessageClean(ex.getMessage())); 
   ex.printStackTrace();
%>
<script>
   alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}
%>  