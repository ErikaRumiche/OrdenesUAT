<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="pe.com.nextel.service.EditOrderService"%>
<%@ page import="pe.com.nextel.service.SessionService"%>
<%@ page import="pe.com.nextel.service.RepairService"%>
<%@ page import="pe.com.nextel.bean.DominioBean"%>
<%@ page import="pe.com.nextel.bean.PortalSessionBean"%>
<%@ page import="pe.com.nextel.bean.OrderBean"%>
<%@ page import="pe.com.nextel.bean.RepairBean"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="pe.com.nextel.bean.FormatBean"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Date"%>
<!--------------------- EditOnDisplaySectionRepair.jsp ------------------>
<%
try {   
   
   ArrayList valoresCombo = new ArrayList();
      
   Hashtable hshParam=(Hashtable)request.getAttribute("hshtInputDetailSection");  
   if (hshParam==null) hshParam=new Hashtable();
   long lCustomerId=MiUtil.parseLong((String)hshParam.get("strCustomerId"));
   long lSiteId=MiUtil.parseLong((String)hshParam.get("strSiteId"));    
   long lOrderId=MiUtil.parseLong((String)hshParam.get("strOrderId"));
   int intRepairId = 0;
  
  //PORTEGA  
  HashMap hshDataOtherFailureMap          = new HashMap();
  HashMap hshDataOtherFailureResultadoMap = new HashMap();
  String strDetDiagInterna="";
  String strObsAsesorInterna="";
  String strDetResolucionInterna="";
  String strTipoFallaCentro = "";
  String strdiagCentro = "";
  String strresolCentro = "";
  String strdetDiagCentro = "";
  String strdetResolCentro="";
   
   String strSessionId=MiUtil.getString((String)hshParam.get("strSessionId"));
   String strTypeCustomer=MiUtil.getString((String)hshParam.get("strTypeCompany"));   
   int iSpecificationId=MiUtil.parseInt((String)hshParam.get("strSpecificationId"));
      
   PortalSessionBean objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
   String strLogin=objPortalSesBean.getLogin();
   String strOrderId = StringUtils.defaultString(request.getParameter(Constante.PARAM_ORDER_ID),"0"); 
   
        //Inicio TIENDA EXPRESS - Se requiere identificar la tienda del usuario logeado , asi como si es una TE   
        //18/06/2014 JRAMIREZ  
        int iBuildingIdUser_=0;  
        int iIsTiendaExpress=0;        
        
        try{
        iBuildingIdUser_=objPortalSesBean.getBuildingid();    
        }catch(Exception e){
           iBuildingIdUser_=0;
        }
        
        if(iBuildingIdUser_ !=0){ 
        SessionService objSessionService  = new SessionService(); 
        iIsTiendaExpress=objSessionService.getExistTiendaExpress(iBuildingIdUser_);          
        }     
        //Fin TIENDA EXPRESS
   
   String strRutaContext = "/apprepairweb";
            
   lOrderId =  Long.parseLong(strOrderId);
   
   String strMessage=null;
	 HashMap hshComboMap = new HashMap();
         HashMap hshCmbPrefNotif = new HashMap();
         HashMap hshTienda = new HashMap();
	 ArrayList arrComboList = new ArrayList();
   GeneralService objGeneralService = new GeneralService();
   EditOrderService objOrderService= new EditOrderService();
	 RepairService objRepairService = new RepairService();   
   
   HashMap hshData=null;
   HashMap hshOrder=null;
   HashMap hshScreenField=null;
   OrderBean objOrderBean=null;   
   String strPlanTarifario=null;
   String strBlackList=null;
   String strFechaEmision=null;   
   ArrayList arrRepuesto=null;
   int intCantReparaciones = 0;
   
   //Trayendo la data de la Reparacion
   HashMap hshDataMap = objGeneralService.getDominioList("MODALITY_SELL");
   ArrayList arrModalitySellList = (ArrayList) hshDataMap.get("arrDominioList");
   
	HashMap hshRepairMap = (HashMap) objRepairService.getRepairByOrder(lOrderId);
	RepairBean objRepairBean = (RepairBean) hshRepairMap.get("objRepairBean");
        
        
   if (objRepairBean==null) objRepairBean=new RepairBean();
   objOrderBean=objRepairBean.getOrderBean();
   if (objOrderBean==null) objOrderBean=new OrderBean();            
   strMessage = (String) hshRepairMap.get(Constante.MESSAGE_OUTPUT);
   if (strMessage!=null)
      throw new Exception(strMessage); 
      
   //Trae el Plan tarifario y el IMEI
   hshRepairMap= objGeneralService.getImeiDetail(objRepairBean);
   strMessage=(String)hshRepairMap.get(Constante.MESSAGE_OUTPUT);   
   if (strMessage!=null)
      throw new Exception(strMessage);
   strPlanTarifario=MiUtil.getString((String)hshRepairMap.get("wv_plantarifario"));
   strBlackList=MiUtil.getString((String)hshRepairMap.get("wv_imeiBListMsg"));

   //Trae la fecha de emision de la Orden
   hshRepairMap = objGeneralService.getFechaEmision(objRepairBean.getNpimei());
   
   String strImei = MiUtil.getString(objRepairBean.getNpimei());   
   
   //BEGIN YRUIZ 21/01/2014
   /*Carga el Tipo de Plan asociado al Equipo*/
   String strPhone = MiUtil.getString(objRepairBean.getNpphone());        
   String strPlanType = objRepairService.getPlanType(strPhone, strImei);
   //END YRUIZ
   String strStatusRepair = MiUtil.getString(objRepairBean.getNpstatus());
   
  /*CARGA EL COD EQUIPO DESDE EL IMEI*/
  
  HashMap hshEquipment = (HashMap) objRepairService.getCodEquipFromImei(objRepairBean.getNpimei());  
  strMessage=(String)hshEquipment.get("strMessage");
  if (strMessage!=null)
      throw new Exception(strMessage);     
  
  String strCodEquip = (String) hshEquipment.get("strCodEquip");

   strMessage=(String)hshRepairMap.get(Constante.MESSAGE_OUTPUT);
   if (strMessage!=null)
      throw new Exception(strMessage);
   strFechaEmision=(String)hshRepairMap.get("strFechaEmision");   

   //Manejo dinamico de controles   
   hshOrder=objOrderService.getOrderScreenField(lOrderId,Constante.PAGE_ORDER_EDIT);
   strMessage=(String)hshOrder.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);  
   
   hshScreenField= (HashMap)hshOrder.get("hshData"); 
  
  
   
   
   //----PORTEGA
   intRepairId=Integer.parseInt(""+(objRepairBean.getNprepairid_bof()==null || objRepairBean.getNprepairid_bof().equals("")?"0":objRepairBean.getNprepairid_bof()));
    System.out.println("intRepairId :"+intRepairId+"-"+strLogin);
    HashMap hshDataAux = objRepairService.getNpRepairBOF(intRepairId, strLogin);

    int intRepairId_repair=Integer.parseInt(""+objRepairBean.getNprepairid());
    
    //SETEANDO VALORES DESDE BOF
  
  String strRepairTypeId = (((String) hshDataAux.get("NPREPAIRTYPE"))==null?"":((String) hshDataAux.get("NPREPAIRTYPE")));
  strTipoFallaCentro = (((String) hshDataAux.get("NPRCFAILURETYPES"))==null?"":((String) hshDataAux.get("NPRCFAILURETYPES")));
  strdiagCentro = (((String) hshDataAux.get("NPRCDIAGNOSIS"))==null?"":((String) hshDataAux.get("NPRCDIAGNOSIS")));
  strresolCentro = (((String) hshDataAux.get("NPRCRESOLUTION"))==null?"":((String) hshDataAux.get("NPRCRESOLUTION")));
  strdetDiagCentro = (((String) hshDataAux.get("NPRCDIAGNOSISDET"))==null?"":((String) hshDataAux.get("NPRCDIAGNOSISDET")));
  strdetResolCentro = (((String) hshDataAux.get("NPRCRESOLUTIONDET"))==null?"":((String) hshDataAux.get("NPRCRESOLUTIONDET")));
  
  //estado orden externa
  String strUserResponse = objRepairService.getDescriptionUserResponse(((String) hshDataAux.get("NPUSERRESPONSE"))==null?"":((String) hshDataAux.get("NPUSERRESPONSE")));
  //estado cotizacion
  String strRepairStat = (((String) hshDataAux.get("NPREPAIRSTAT"))==null?"":((String) hshDataAux.get("NPREPAIRSTAT")));
  
  System.out.println("strdiagCentro: "+strdiagCentro);
  System.out.println("strdetDiagCentro: "+strdetDiagCentro);
  
  //seteando desde REPAIR
  strDetDiagInterna=objRepairBean.getNPDIAGNOSTICDETAIL();
  strObsAsesorInterna=objRepairBean.getNPASSESSOROBSERVATION();
  System.out.println("strDetDiagInterna: "+strDetDiagInterna);
  System.out.println("strObsAsesorInterna: "+strObsAsesorInterna);
  
    //PORTEGA---------------------------------------
  
   // Obtener el RepaceType	
	 String strReplaceType = null;
   String strOrderIdRT = (String)hshParam.get("strOrderId");
   strReplaceType = objRepairService.getReplaceType(strOrderIdRT);            
   int iReplaceType = MiUtil.parseInt(strReplaceType);
   System.out.println("iReplaceType --> "+iReplaceType);
   System.out.println("objRepairBean.getNpphone()-->"+objRepairBean.getNpphone());      
   System.out.println("objRepairBean.getNpimei()-->"+objRepairBean.getNpimei());
   System.out.println("iSpecificationId-->"+iSpecificationId);
   System.out.println("lOrderId-->"+lOrderId);
   System.out.println("lSiteId-->"+lSiteId);
   System.out.println("lCustomerId-->"+lCustomerId);
   System.out.println("strSessionId-->"+strSessionId);   
   System.out.println("Cosntes-->"+  Constante.SPEC_LOAD_CMB_PROCESS);
   System.out.println("----------------------------------------------------------------");
   System.out.println("strPhone-->"+strPhone);
   System.out.println("strPlanType-->"+strPlanType);
   System.out.println("strImei-->"+strImei);
   System.out.println("----------------------------------------------------------------");
   
   System.out.println("-----Fin EditOnDisplaySectionRepair.jsp-------------");       
   
   String strModeloSOS = objRepairBean.getNpmodel();
   String strNpServicioNac = null;   
   if ((objRepairBean.getNpdescservicecode()).equals("Nacionalización de Equipo")){
      strNpServicioNac = objRepairBean.getNpdescservicecode();
   }   
   System.out.println("----------------------------------------------------------------");
   System.out.println("strNpServicioNac-->"+strNpServicioNac);
   System.out.println("----------------------------------------------------------------");
   
   String strModelEquipment = objRepairBean.getNpmodel();
   // Variable para el modelo del equipo usada solo para nacionalización.
   String strModelNac = null;
   
   /*
    * Si se va a nacionaliazar, remueve la "e" final en el caso de los modelos seleccionados del combo.
    */
   
   if (strNpServicioNac != null) {
      // El campo npmodel guarda el modelo seleccionado del combo. Este tiene una "e" al final.
      int lastCharIndex = strModelEquipment.length() - 1;
      int indexOfE = strModelEquipment.toLowerCase().indexOf("e", lastCharIndex - 1);
      // Si tiene la "e" al final se remueve.
      if (indexOfE > -1) {
          strModelNac = strModelEquipment.substring(0, lastCharIndex);
      }
   }
   
   // Si strModelEquipment esta vacío es que se ha ingresado el modelo en la caja de texto nuevo modelo.
   if (objRepairBean.getNpmodel().trim().length()<1){
      strModelEquipment = objRepairBean.getNpmodelnew(); 
      strModelNac = strModelEquipment;
   }
   
   System.out.println("----------------------------------------------------------------");
   System.out.println("[strModelEquipment : "+strModelEquipment+"]");
   System.out.println("iLongCadenaModelo : "+strModelEquipment.length());
   System.out.println("----------------------------------------------------------------");
  
   String strShowLabelIN = objRepairService.getValidInternetNextel(lOrderId); 
   System.out.println("strShowLabelIN-->"+  strShowLabelIN);
    
   
    
   // MMONTOYA - Despacho en tienda
   
   /*    
    * Lista de modelos.
    */
   HashMap hshModels = (HashMap) objRepairService.getModels(objOrderBean.getNpOrderId());   
   ArrayList arrModelosList = (ArrayList)hshModels.get("arrListado"); 
   
   /* 
    * Lista de tipo de equipos.
    */
   ArrayList arrTiposList = new ArrayList();
   
   DominioBean tipo = new DominioBean();
   tipo.setValor("Nuevos");
   tipo.setDescripcion("Nuevos");
   arrTiposList.add(tipo);
   
   tipo = new DominioBean();
   tipo.setValor("Usados");
   tipo.setDescripcion("Usados");
   arrTiposList.add(tipo);   
   
   /* 
    * Lista de opción con accesorios.
    */
   ArrayList arrAccList = new ArrayList();
   
   DominioBean flagAcc = new DominioBean();
   flagAcc.setValor("Si");
   flagAcc.setDescripcion("Si");
   arrAccList.add(tipo);
   
   flagAcc = new DominioBean();
   flagAcc.setValor("No");
   flagAcc.setDescripcion("No");
   arrAccList.add(tipo);

    //JVERGARA
    String strRutaContextDigit=request.getContextPath();
    String actionURL_DigitalDocumentServlet = strRutaContextDigit+"/digitalDocumentServlet";

    System.out.println("actionURL_DigitalDocumentServlet: "+actionURL_DigitalDocumentServlet);
%>
<!--------------------- EditOnDisplaySectionRepair.jsp ------------------>
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></script>
<style type="text/css">
.divReparacionesRAE{
	display: inline;
}
.imgIcons{
    height: 15px;
}
</style>

<script type="text/javascript" >
  var numImeiCambio = 0;
  var numImeiCliente = 0; 
  

	function fxSectionRepairFinalStatus() {
      return true;
   }
	
	function fxSectionRepairValidate() {      
      var vForm = document.frmdatos;        
      
      //El proceso es obligatorio
      if (vForm.cmbProceso.value == ""){
         alert("Debe elegir un proceso");
         vForm.cmbProceso.focus();
				 return false;
      }
	
	  //El Estado de la Reparación es obligatorio
      if (vForm.cmbEstadoReparacion.value == ""){
         alert("Debe elegir un Estado de la Reparación");
         vForm.cmbEstadoReparacion.focus();
		     return false;
      }
    
     /*JOYOLAR - 25/03/2008 - Valida el ingreso del motorizado si esta visible
     COR0942*/
     if ( dvMotorizado.style.display==""){
        if (vForm.cmbMotorizado.value == "" ){
          alert("Debe seleccionar el campo Motorizado");
          vForm.cmbMotorizado.focus();
          return false;
        }
    }
    /*si es SERVCIO_TECNICO_ACCESORIOS*/
      if (<%=iSpecificationId%> == <%=Constante.SPEC_SERVCIO_TECNICO_ACCESORIOS%>){
        /*JOYOLAR - 25/03/2008 - Valida el ingreso del tipo accesorio
        COR0943*/
        if ( dvBySpecTipoAccesorio.style.display==""){
          if (vForm.cmbTipoAccesorio.value == "" ){
            alert("Debe seleccionar el campo Tipo Accesorio");
            vForm.cmbTipoAccesorio.focus();
            return false;
          };
        };

        /*JOYOLAR - 25/03/2008 - Valida el ingreso de semana fabricacion
        COR0944 */
        if ( dvBySpecSemFrabic.style.display==""){
          if (vForm.txtSemFabricacion.value == "" ){
            alert("Debe ingresar el campo Sem. Fabricación");
            vForm.txtSemFabricacion.focus();
            return false;
          };
          //if ((vForm.txtSemFabricacion.value.length <4) || (vForm.txtSemFabricacion.value.length >5)) {
          //  alert("Sólo se permite ingresar 4 ó 5 caracteres");
          //  vForm.txtSemFabricacion.focus();
          //  vForm.txtSemFabricacion.value="";
          //  return;
          //};
        };

       /*JOYOLAR - 25/03/2008 - Valida el ingreso del modelo accesorio
       COR0945*/
        if ( dvBySpecModelAccesorio.style.display==""){
          if (vForm.cmbModeloAccesorio.value == "" ){
            alert("Debe seleccionar el campo Modelo Accesorio");
            vForm.cmbModeloAccesorio.focus();
            return false;
          };
        };
     };

    /*JOYOLAR - 27/03/2008 - Valida el ingreso del Tipo Servicio
    COR0997 - COR1021
    */
    //if (<%=iSpecificationId%> == <%=Constante.SPEC_SERVCIO_TECNICO_ACCESORIOS%> || <%=iSpecificationId%> == <%=Constante.SPEC_SERVCIO_TECNICO_CASORAPIDO%> || <%=iSpecificationId%> == <%=Constante.SPEC_SERVCIO_TECNICO_REPARACION%> ) {
	//la validación aplica para las 4 categorias de servicio tecnico
        if  ( (vForm.lstFallaResultado.length ==0) && (vForm.cmbProceso.value != "SERVICIO")  ) { //(vForm.cmbTipoServicio.value == "" && vForm.cmbTipoFalla.value == "" ){ el servicio ya no es obligatorio
          //alert("Debe elegir un Tipo Servicio ó un Tipo Falla");
          alert("Debe elegir una Falla");
          //vForm.cmbTipoServicio.focus();
          vForm.cmbTipoFalla.focus();
          return false;
        };
    //};

     /*JOYOLAR - 25/03/2008 - Valida el ingreso de la situacion
     COR0948 - PARA TODAS LAS SUBCATEGORIAS*/

     if (vForm.cmbEstadoReparacion.value == "CERRADO" ){
      if (vForm.cmbSituacion.value == "" ){
        if(cmbProceso == "REP"){
         alert("Debe seleccionar una Situación");
         vForm.cmbSituacion.focus();
         return false;
         }
      };                              
     };
      
      //Validando Fecha de entrega
   /*   var wv_fecha_entrega  = form.txtFechaRecepcion.value.substring(0,10);
      var wv_hora_entrega   = form.txtFechaRecepcion.value.substring(11,16);
   
      if (vForm.txtFechaRecepcion.value == ""){
         alert("Debe ingresar la fecha de recepción");         
         vForm.txtFechaRecepcion.select();
         return false;
      };      
      if (!isValidDate(wv_fecha_entrega) || !isValidHour(wv_hora_entrega) ){
         form.txtFechaRecepcion.select();
         return false;
      };*/
    
      /*
      JPEREZ: Se modifica para validar que se ingrese el mismo número de IMEIs de Cambio y de Cliente
      (Incidencia 2602)
      */
      var cmbProceso = vForm.cmbProceso.value;      
      var cmbEstadoRep = vForm.cmbEstadoReparacion.value;
      
       
      var flagTipImei = 0;
      numImeiCambio = 0;
      numImeiCliente = 0;
      
      
      if ( (cmbProceso != null)){
        if ( fxValidateProceso(cmbProceso, vForm.txtAlquilado.value) ){
          //var total = eval(tblListaPrestamosCambios.rows.length - vForm.hdnCount.value);
          var total = eval(tblListaPrestamosCambios.rows.length);
          var flagActivate = 0;
          if(vForm.hdnCount.value > 0 && vForm.hdnFlagValidateTypeImei.value == "1")              
            flagActivate = 1;                                           
        
         <%      
            long strRepId = objRepairBean.getNprepairid() ;   
            String strImeis =  objRepairBean.getNpimei();
            
            //String intFlag= null;
            
            hshRepairMap = objRepairService.getDocGenerate(strImeis,strRepId);
            String intFlag = (String)hshRepairMap.get("intRepairid"); 
            
            HashMap hshRepairMap1;
                        
            hshRepairMap1 = objRepairService.getDocGenClose(strImeis,strRepId);
            String intFlagC = (String)hshRepairMap1.get("intRepairid"); 
            
            //HashMap hshRepairMap = (HashMap) objRepairService.getRepairByOrder(lOrderId);
            
                        
         %>                                                                                       
                
          //if(total <= 2 || <%=intFlag%> == 0 || cmbEstadoRep!="ANULADO" ) {
          
          //if ((total <= 2 || <%=intFlag%> == 0) || (cmbEstadoRep == "ANULADO" && <%=intFlag%> == 3)) {
          //  alert("Debe agregar al menos un Item con Tipo de Imei 'CAMBIO' y un Item con Tipo de Imei 'CLIENTE' ");
          //  return false;
          //}  
                                               
          if ( (total >= 2) && ( <%=intFlagC%> == 0) && (cmbEstadoRep == "CERRADO")  ) {           
            alert("Debe de generar Documentos, antes de cerrar!");
            return false;          
          }          
          
                                                                                                 
          if (<%=intFlag%> == 0 && cmbEstadoRep == "ANULADO") {           
            alert("No se puede anular, Documentos Generados!");
            return false;          
          }
                              
          if ( (total <= 2) && (cmbEstadoRep != "ANULADO") && (cmbEstadoRep != "CERRADO") && (cmbProceso == "REX" || cmbProceso == "STP") ) {
            alert("Debe agregar al menos un Item con Tipo de Imei 'CAMBIO' y un Item con Tipo de Imei 'CLIENTE' ");
            return false;
          }else{                     
          }          
          
          if (total>2){            
            for(var i = 0; i<total-1; i++){                
              if( (vForm.txtImeiLista[i] != null) && (vForm.txtImeiLista[i].value=="") ){
                alert("Debe ingresar el IMEI en la sección de Cambios");
                vForm.txtImeiLista[i].focus();
                return false;
              }              
              if( (vForm.cmbTipoImeiLista[i] != null) && (vForm.cmbTipoImeiLista[i].value!="") ){                        
                if ( (vForm.cmbTipoImeiLista[i].value == "CAMBIO") || (vForm.cmbTipoImeiLista[i].value == "DEVOLUCION") || (vForm.cmbTipoImeiLista[i].value == "PRESTAMO") )
                  numImeiCambio++;
                if (vForm.cmbTipoImeiLista[i].value == "CLIENTE")
                  numImeiCliente++;
              }
            }//fin de for
            if (numImeiCambio != numImeiCliente){
              alert("Debe ingresar la misma cantidad de IMEIs de CAMBIO / PRESTAMO y de CLIENTE");
              return false;
            }
          } //fin  if (total>2...         
        } // fin de if fxValidateProceso
      } //fin de if cmbProceso != null      
      
      return true;
     
   }   
   
   //Modificado, Tomás Mogrovejo Acosta 23/06/2009
   function fxValidateProceso(Proceso, Alquilado){
      if (Proceso == "ALQUILER_REX" || Proceso == "REX" || Proceso == "STP" || Proceso == "STP_REPO" || Proceso == "ACC" || Proceso == "REP" )
        return true;
      else
        return false;
   }

   function fxConcatIdRepuestos(){
      var vForm = document.frmdatos;      
      var x=document.getElementById("cmbRepuestoDestino");
      var cadena="";
      for (i=0;i<x.length;i++){   
         cadena=x.options[i].value+","+cadena;
      }
      vForm.hdnRepuestoDestinoId.value=cadena;     
   }   

	function fxShowMotorizado() {
		vForm = document.frmdatos;   
		var index = vForm.cmbRecepcion.selectedIndex-1;
		var valor = aTag1[index].tag;      
		if (valor=='D') {        
			dvMotorizado.style.display = "";     
			dvMotorizadoName.style.display = "";     
			vForm.cmbMotorizado.value = "";
		} else {
			dvMotorizado.style.display = "none";
			dvMotorizadoName.style.display = "none";         
		}
	}   

   function fxLoadServices() {
      vForm = document.frmdatos;
      var index = vForm.cmbTipoServicio.value;      
      DeleteSelectOptions(document.frmdatos.cmbServicio);      
      if (index != "") {         
         var url = "<%=request.getContextPath()%>/repairServlet?hdnMethod=loadServices&cmbTipoServicio="+index;
         parent.bottomFrame.location.replace(url);                        
       }else{       
      DeleteSelectOptions(document.frmdatos.cmbServicio);      
      }                                             
   }
   
</script>
<script type="text/javascript">
   
   var aTag1 = new Array();
      
   function fxMakeTag(tag){
      this.tag     =   tag;
   }   	            
   
   function fxAddSelectedClient(fuente, destino, texto){
                     
      if (document.frmdatos.txtGarantiaTrueBounce.value != 'Si'){   
         fxGenerateTrueBounce();          
         }
      
      if(fxVerifyMultipledLines(fuente) > 1){
          var i=0;
          while (i<fuente.options.length){
              if(fxVerifyMultipledLines(fuente) > 1){
                  if(fuente.options[i].selected == true) {
                      try {
                          var indice1 = i;
                          var indice2 = destino.length;

                          if (texto == 'X'){
                            //fuente.options[indice1].text = fuente.options[indice1].text.substring(0,fuente.options[indice1].text.length-2);
                            fuente.options[indice1].text = fuente.options[indice1].text.substring(0,fuente.options[indice1].text.length-2);
                          }
                          if (texto == 'N' || texto == 'U'){
                            fuente.options[indice1].text = fuente.options[indice1].text + ' ' +texto;
                          }

                          option = new Option(fuente.options[indice1].text, fuente.options[indice1].value);
                          destino.options[indice2] = option;
                          fuente.options[indice1] = null;
                      }catch(exception) {
                          alert(exception.description);
                      }
                  }else{
                      i++;
                  }
              }else{
                  i=fuente.options.length;
              }
          }
      }
      if(fxVerifyMultipledLines(fuente) <= 1){
          fxManageSwapSelect(fuente, destino, texto);
      }
   
      //Calcula Hidden y Total
      fxGeneraHidden();      
  }
      
   
   function fxGeneraHidden(){
      // Limpia los hidden 
      document.frmdatos.hdnListaFallasSeleccionadas.value = "";
      document.frmdatos.hdnListaOtrasFallasSeleccionadas.value = "";
      var lista = "";
      var lstFallasSeleccionadas = document.frmdatos.lstFallaResultado;
      var lstOtrasFallasSeleccionadas = document.frmdatos.lstOtrasFallasResultado;
      //llenando lista fallas      
      for (var i = 0; i < lstFallasSeleccionadas.options.length; i++){
        lista = lista + lstFallasSeleccionadas.options[i].value + "|";
      }
      document.frmdatos.hdnListaFallasSeleccionadas.value = lista;
      //llenando lista otra fallas
      lista = "";
      for (var i = 0; i < lstOtrasFallasSeleccionadas.options.length; i++){
        lista = lista + lstOtrasFallasSeleccionadas.options[i].value + "|";
      }
      document.frmdatos.hdnListaOtrasFallasSeleccionadas.value = lista;
   }
      
      
   function roundX(number,X) {       
      X = (!X ? 2 : X); 
      return Math.round(number*Math.pow(10,X))/Math.pow(10,X); 
   }

   	
	function fxVerifyMultipledLines(fuente) {
		var cont = 0;
		for(i=0;i<fuente.options.length;i++) {
			if(fuente.options[i].selected == true) {
				cont++;
			}
		}
		return cont;
	}
	
	function fxManageSwapSelect(fuente, destino, texto){
      try {
          var indice1 = fuente.options.selectedIndex;
          try {
              if(indice1 == -1){
                  fuente.options[0].selected=true;
                  indice1 = fuente.options.selectedIndex;
              }
          }catch(exception){
          }
          var indice2 = destino.length;

          if (texto == 'X'){
            fuente.options[indice1].text = fuente.options[indice1].text.substring(0,fuente.options[indice1].text.length-2);
          }
          if (texto == 'N' || texto == 'U'){
            fuente.options[indice1].text = fuente.options[indice1].text + ' ' +texto;
          }

          option = new Option(fuente.options[indice1].text, fuente.options[indice1].value);
          destino.options[indice2]=option;
          fuente.options[indice1] = null;
          try {
              fuente.options[indice1].selected=true;
          }catch(exception){
              fuente.options[indice1-1].selected=true;
          }
      }catch(exception){
      }
  }
	
  function fxCargaHiddenFlagValidateTypeImei(){
    document.frmdatos.hdnFlagValidateTypeImei.value="1";    
  }
  
	function fxTrim(s) {
		while (s.length>0 && (s.charAt(0)==" "||s.charCodeAt(0)==10||s.charCodeAt(0)==13)) {
			s=s.substring(1, s.length);
		}
		while (s.length>0 && (s.charAt(s.length-1)==" "||s.charCodeAt(s.length-1)==10||s.charCodeAt(s.length-1)==13)) {
			s=s.substring(0, s.length-1);
		}
		return s; 
	}
      

    function fxFillFailureList(){
      vForm = document.frmdatos;
      intTipoFallaId = vForm.cmbTipoFalla.value;
      intRepairId = vForm.hndReparacionId.value;
      strRepairTypeId = "OA";
      //console.log('hdnListaFallasSeleccionadas: '+document.frmdatos.lstFalla);
      deleteAllValues(document.frmdatos.lstFalla);
      strListFallasSeleccionadas = vForm.hdnListaFallasSeleccionadas.value;
      //deleteAllValues(document.frmdatos.lstFallaResultado);
      
      if (intTipoFallaId!=""){      
         var url = "<%=request.getContextPath()%>/repairServlet?hdnMethod=getFailureList&intFailureId="+intTipoFallaId+"&intRepairId="+intRepairId+"&strRepairTypeId="+strRepairTypeId+"&strListFallasSeleccionadas="+strListFallasSeleccionadas;
         parent.bottomFrame.location.replace(url);
      }    
   }    

    
   function fxGetDiagnosisDescription(){
      vForm = document.frmdatos;
      diagnosisId = vForm.cmbDiagnostico.value;
    
      if (diagnosisId!=""){
         var url = "<%=request.getContextPath()%>/repairServlet?hdnMethod=getDiagnosisDescription&intDiagnosisId="+diagnosisId;
         parent.bottomFrame.location.replace(url);
      }else{
         document.frmdatos.txtNextelDiagnostico.value ='';
      }
   }
          
   function loadHistoryRepair(param1) {

               var frameUrl = "/portal/pls/portal/WEBCCARE.NPAC_SERVICIOTECNICO_PL_PKG.PL_ST_HISTORY_REPAIR"
                             +"?v_param1="+ param1
                             ;
              // alert("frameUrl: "+frameUrl);
               var winUrl = "/portal/pls/portal/WEBSALES.NPSL_NEW_GENERAL_PL_PKG.PL_FRAME?av_url="+escape(frameUrl);
               var popupWin = window.open(winUrl,"detalleHistoryFromOrder","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=no,width=1100, height=550, left=50, top=100,screenX=50,screenY=100,modal=yes");
            }
  
  
</script>
<input type="hidden" name="hndReparacionId" value="<%=objRepairBean.getNprepairid()%>">
<input type="hidden" name="hdnRepairId" value="<%=objRepairBean.getNprepairid()%>">

<input type="hidden" name="hdnBuildingId" value="<%=objRepairBean.getNpbuildingid()%>">

<input type="hidden" name="hdnObjectAllId">
<input type="hidden" name="hdnLogin" value="<%=strLogin%>">

<input type="hidden" name="hdnDocumentParentType" id="hdnDocumentParentType" value="1"/>
<input type="hidden" name="hdnTransferReason" id="hdnTransferReason" value="1"/>

<input type="hidden" name="hdnRepuestoDestinoId">
<input type="hidden" name="hdnCrearDocListaAux">
<input type="hidden" name="hdnDevolverEquipoListaAux">
<input type="hidden" name="hdnTipoProceso">

<input type="hidden" name="hdnModelo" value="<%=objRepairBean.getNpmodel()%>">

<input type="hidden" name="hdnProcesoAux" value="<%=objRepairBean.getNprepairtype()%>">

<input type="hidden" name="hdnTechnology" value="<%=objRepairBean.getNptechnology()%>">

<input type="hidden" name="hdnShowLabelIN" value="<%=strShowLabelIN%>">

<!-- JVERGARA VIA hidden values INICIO -->
<input type="hidden" name="hdnVIAStatus" id="hdnVIAStatus" value="">
<!--
    0: Detener generacion
    1: Generar firma digital válida para VIA
    2: Consultar al usuario que se van a generar firma Manual.
    3: Generar con firma manual sin consultar
-->
<input type="hidden" name="hdnVIAMessage" id="hdnVIAMessage" value="">
<!-- JVERGARA VIA hidden values FIN -->

<table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
   <tr>
      <td align="left">
         <table border="0" cellspacing="0" cellpadding="0" align="left">
				<tr>
					<td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
					<td class="SubSectionTitle" align="left" valign="top">Equipo</td>
					<td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"> &nbsp;</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td align="center">         
			<table border="0" cellspacing="1" cellpadding="2" width="100%" class="RegionBorder">
          <tr>
              <!-- INICIO - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 4/10/2010 --> 
              <td class="CellLabel" align="center" width="13%"><font color="red"><b>*</b></font>&nbsp;Nextel / Tipo de Equipo</td>
              <!-- FIN - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 4/10/2010 --> 
              <td class="CellLabel" align="center">SIM</td>
              <td class="CellLabel" align="center">Modalidad</td>
              <td class="CellLabel" align="center">Fecha Ingreso</td>
              <td class="CellLabel" align="center">Fecha Activación</td>
              <td class="CellLabel" align="center">Plan Tarifario</td>					
              <td class="CellLabel" align="center">Retail</td>
              <td class="CellLabel" align="center"><font color="red"><b>*</b></font>&nbsp;Recepción</td>
              <td class="CellLabel" align="center"><div id="dvMotorizadoName" style=display:'none'>Motorizado</div></td>
          </tr>
          <tr>
              <td class="CellContent">
                  <input type="text" name="txtNextel" value="<%=MiUtil.getString(objRepairBean.getNpphone())%>" size="10" maxlength="12" readonly="readonly"/>&nbsp;&nbsp;&nbsp;
                  <!-- INICIO - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 4/10/2010 --> 
                  <input type="text" name="txtTecnologia" value="<%=MiUtil.getString(objRepairBean.getNptechnology())%>" size="20" maxlength="20" readonly="readonly"/>
                  <!-- FIN - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 4/10/2010 --> 
              </td>               
              <td class="CellContent">
                  <input type="text" name="txtSim" value="<%=MiUtil.getString(objRepairBean.getNpsim())%>" maxlength="20" readonly="readonly"/>
              </td>
              <td class="CellContent">
                  <input type="text" name="txtAlquilado" value="<%=MiUtil.getString(objRepairBean.getNpequipment())%>" size="10" maxlength="10" readonly="readonly"/>
              </td>
              <td class="CellContent">
                  <input type="text" name="txtFechaEmision" size="10" maxlength="10" readonly="readonly" value="<%= objRepairBean.getNpfechaActOracle() %>"/>   
              </td>                                             
              <td class="CellContent">
                  <input type="text" name="txtFecActiv" size="10" maxlength="10" readonly="readonly" value="<%= objRepairBean.getNpfechaActivacion() %>"/>
              </td>
              <td class="CellContent">
                  <input type="text" name="txtPlanTarifario" maxlength="15" readonly="readonly" value="<%=strPlanTarifario%>"/>
              </td>
              <td class="CellContent">
                  <input type="text" name="txtIndRetail" value="<%=objRepairBean.getNPINDRETAIL()%>" size="10" maxlength="10" readonly="readonly"/>
              </td>
              <td class="CellContent" >               
                  <select name="cmbRecepcion" style="width: 75%" onchange="javascript:fxShowMotorizado();" disabled/>
                     <option value=""></option>
                     <% hshComboMap = objGeneralService.getTableList("REPAIR_RECEPCION","1");
                     strMessage = (String)hshComboMap.get(Constante.MESSAGE_OUTPUT);
                     if (strMessage!=null)
                        throw new Exception(strMessage);

                     arrComboList = (ArrayList)hshComboMap.get("arrTableList");
                     int iLongitud=0;
                     if (arrComboList!=null) iLongitud=arrComboList.size();
                        hshComboMap=null;
                     for(int i=0;i<iLongitud;i++){
                        hshComboMap=(HashMap)arrComboList.get(i);
                        System.out.println("Recepcion-->"+hshComboMap.get("wv_npValue"));
                     %>                
                     <option value="<%=MiUtil.getString((String)hshComboMap.get("wv_npValue"))%>">
                        <%=MiUtil.getString((String)hshComboMap.get("wv_npValueDesc"))%></option>                       
                     <script>                
                        aTag1[<%=i%>]=new fxMakeTag('<%=MiUtil.getString((String)hshComboMap.get("wv_npTag1"))%>');
                     </script> 
                     <%
                     }
                     %>                
                  </select>
                  <script>
                     document.frmdatos.cmbRecepcion.value="<%=objRepairBean.getNpreception()%>";
                  </script>
              </td>
              <td class="CellContent">
                  <div id="dvMotorizado" >
                      <select name="cmbMotorizado" disabled/>							
                           <%
                           hshComboMap = objGeneralService.getGeneralOptionList("REPAIR_MOTORISTA");
                           strMessage = (String)hshComboMap.get(Constante.MESSAGE_OUTPUT);
                           if (strMessage!=null)
                             throw new Exception(strMessage);                        
                           arrComboList = (ArrayList)hshComboMap.get("arrDominioLista");                    
                           %>                  
                           <%=MiUtil.buildComboSelected(arrComboList,"value","descripcion",objOrderBean.getNpCarrierId()+"")%>											                                       
                      </select>
                  </div>
                  <script>
                      fxShowMotorizado();
                      if ( dvMotorizado.style.display==""){
                          document.frmdatos.cmbMotorizado.value="<%=objOrderBean.getNpCarrierId()%>";                     
                      }
                  </script>                                                               
              </td>
          </tr>
          <tr>
              <td class="CellLabel" align="center" width="13%"><font color="red"><b>*</b></font>&nbsp;Modelo / Gama</td>               
              <td class="CellLabel" align="center"><font color="red"><b>*</b></font>&nbsp;IMEI</td>
              <td class="CellLabel" align="center" width="13%"><font color="red"><b>*</b></font>&nbsp;Nro. de Serie</td>
              <td class="CellLabel" align="center">Código Equipo</td>
              <td class="CellLabel" align="center">Tipo Teléfono</td>
              <td class="CellLabel" align="center">Fabricante</td>
              <td class="CellLabel" align="center">Proveedor</td>
              <td class="CellLabel" align="center">Centro Reparación</td>
              <td class="CellLabel" align="center"></td>
          </tr>
          <tr>
              <td class="CellContent"  >
                  <input type="text" name="txtModelo" value="<%=strModelEquipment%>" size="8" maxlength="8" readonly="readonly"/>
                  <input type="text" name="txtGama" value="<%=objRepairBean.getNPEQUIPGAMA()%>" size="6" maxlength="5" readonly="readonly"/>
              </td>
              <td class="CellContent">
                  <input type="text" name="txtImei" value="<%=MiUtil.getString(objRepairBean.getNpimei())%>" maxlength="15" readonly="readonly"/>
              </td>
              <td class="CellContent">
                  <input type="text" name="txtSerie" value="<%=MiUtil.getString(objRepairBean.getNpimeisn())%>" size="15" maxlength="15" readonly="readonly"/>
              </td>
              <td class="CellContent">
                  <input type="text" name="txtCodEquipo" value="<%=strCodEquip%>" size="15" maxlength="15" readonly="readonly"/>
              </td>
              <td class="CellContent">
                  <input type="text" name="txtOriginal" value="<%=MiUtil.toTypePhone(objRepairBean.getNporiginal())%>" size="10" maxlength="12" readonly="readonly" />
                  <input type="hidden" name="hdnOriginal" value="<%=objRepairBean.getNporiginal()%>" >                                                            
              </td>    
              <td class="CellContent">
                  <input type="text" name="txtFabricante" maxlength="10" readonly="readonly" value="<%=objRepairBean.getNPFABRICATOR()%>"/>
                  <input type="hidden" name="hdnFabricanteId" value="<%=objRepairBean.getNPFABRICATORID()%>" >
              </td>
              <td class="CellContent">
                  <input type="text" name="txtProveedor" size="25" maxlength="10" readonly="readonly" value="<%= MiUtil.getString(objRepairBean.getNpprovider())%>"/>
                  <input type="hidden" name="hdnProveedorId" value="<%=objRepairBean.getNpproviderid()%>" >                  
              </td>                                                         
              <td class="CellContent">
                  <input type="text" name="txtCentroReparacion" size="25" maxlength="10" readonly="readonly" value="<%=objRepairBean.getNPREPAIRCENTER()%>"/>
                  <input type="hidden" name="hdnCentroReparacionId" value="<%=objRepairBean.getNPREPAIRCENTERID()%>" >
              </td>   
              <td class="CellContent">
              </td>
          </tr>
        <!-- Cambios para el proyecto RepAdmEquip -->
          <tr>
              <td class="CellLabel" align="center">D.A.P.</td>
              <td class="CellLabel" align="center">Garantia Fabrica</td>
              <td class="CellLabel" align="center">Garantia Reparación</td>
              <td class="CellLabel" align="center" nowrap="nowrap">Garantia Refurbish</td>
              <td class="CellLabel" align="center">Bounce</td>
              <td class="CellLabel" align="center" nowrap="nowrap">GN | Ocurr.</td>
              <td class="CellLabel" align="center">Tipo Contrato</td>
              <td class="CellLabel" align="center">&nbsp;</td>
              <td class="CellLabel" align="center">&nbsp;</td>
        </tr>
        <tr>
               <td class="CellContent" align="center">
                <% if( MiUtil.toAnswer(objRepairBean.getNpwarrantdap()).equals("No") ){ %>
                  <img class="imgIcons" src="<%=Constante.PATH_APPORDER_SERVER%>/images/error_icon.gif">
                <% }else{ %>
                  <img class="imgIcons" src="<%=Constante.PATH_APPORDER_SERVER%>/images/check_icon.gif">
                <% } %>
                  <input type="hidden" name="hdnDAP" value="<%=objRepairBean.getNpwarrantdap()%>" >
              </td>
              <td class="CellContent" align="center">
                <% if( MiUtil.toAnswer(objRepairBean.getNpgarantia_fabricante()).equals("No") ){ %>
                  <img class="imgIcons" src="<%=Constante.PATH_APPORDER_SERVER%>/images/error_icon.gif">
                <% }else{ %>
                  <img class="imgIcons" src="<%=Constante.PATH_APPORDER_SERVER%>/images/check_icon.gif">
                <% } %>
                  <input type="hidden" name="hdnGarantiaFabrica" value="<%=objRepairBean.getNpgarantia_fabricante()%>" >
              </td>
              <td class="CellContent" align="center">
                <% if( MiUtil.toAnswer(objRepairBean.getNpgarantia_reparacion()).equals("No") ){ %>
                  <img class="imgIcons" src="<%=Constante.PATH_APPORDER_SERVER%>/images/error_icon.gif">
                <% }else{ %>
                  <img class="imgIcons" src="<%=Constante.PATH_APPORDER_SERVER%>/images/check_icon.gif">
                <% } %>
                  <input type="hidden" name="hdnGarantiaReparacion" value="<%=objRepairBean.getNpgarantia_reparacion()%>" >
              </td>
              <td class="CellContent" align="center">
                <% if( MiUtil.toAnswer(objRepairBean.getNpgarantia_refurbish()).equals("No") ){ %>
                  <img class="imgIcons" src="<%=Constante.PATH_APPORDER_SERVER%>/images/error_icon.gif">
                <% }else{ %>
                  <img class="imgIcons" src="<%=Constante.PATH_APPORDER_SERVER%>/images/check_icon.gif">
                <% } %>
                  <input type="hidden" name="hdnGarantiaRefurbish" value="<%=objRepairBean.getNpgarantia_refurbish()%>" >
              </td>
              <td class="CellContent" align="center">
                <% if( MiUtil.toAnswer(objRepairBean.getNpgarantia_bounce()).equals("No") ){ %>
                  <img class="imgIcons" src="<%=Constante.PATH_APPORDER_SERVER%>/images/error_icon.gif">
                <% }else{ %>
                  <img class="imgIcons" src="<%=Constante.PATH_APPORDER_SERVER%>/images/check_icon.gif">
                <% } %>
                  <input type="hidden" name="hdnGarantiaBounce" value="<%=objRepairBean.getNpgarantia_bounce()%>" >
              </td>
              <td class="CellContent" nowrap="nowrap" align="center">
                <% if( MiUtil.toAnswer(objRepairBean.getNpwarrantnextel()).equals("No") ){ %>
                  <img class="imgIcons" src="<%=Constante.PATH_APPORDER_SERVER%>/images/error_icon.gif">
                <% }else{ %>
                  <img class="imgIcons" src="<%=Constante.PATH_APPORDER_SERVER%>/images/check_icon.gif">
                <% } %>
                  <input type="hidden" name="hdnGarantia" value="<%=objRepairBean.getNpwarrantnextel()%>" >
                  <!-- valor de prueba de la nueva cajita, primera linea caja, segunda el hidden -->
                  <input type="text" name="txtOcurrencia" value="<%=objRepairBean.getNPWARRANOCUR()%>" size="2" readonly="readonly" />
              </td>
              <td class="CellContent">
                  <input type="text" name="txtTipoContrato" value="<%=MiUtil.toAnswer(objRepairBean.getNpgarantia_truebounce())%>" size="20" maxlength="12" readonly="readonly" />                  
                  <input type="hidden" name="hdnTipoContrato" >
              </td>
              <td class="CellContent"></td>
              <td class="CellContent"></td>
        </tr>
        <tr>
              <td class="CellLabel" align="center">N° Vis.Id / N° Vis.IMEI</td>
              <td class="CellLabel" align="center">N° Visitas Cliente</td>
              <td class="CellLabel" align="center">Blacklist</td>
              <td class="CellLabel" colspan="2" align="center">Contacto Recojo</td>
              <td class="CellLabel" align="center" colspan="2">Direcci&oacute;n Recojo</td> 
              <td class="CellLabel" align="center">Código OSIPTEL</td>
              <td class="CellLabel" align="center"></td>
				</tr>
        <tr>
              <td class="CellContent">
                  <% 
                     hshData=objGeneralService.getCountCases("Usuario",objRepairBean.getNpphone());
                     strMessage=(String)hshData.get("strMessage");
                     if (strMessage!=null)
                        throw new Exception(strMessage); 
                     int iCasoUsuario=MiUtil.parseInt((String)hshData.get("iRetorno"));     
                  %>
                  <input type="text" name="txtCasosUsuario" size="5" maxlength="4" readonly="readonly" value="<%=iCasoUsuario%>"/>&nbsp;
                  <%
                     hshData=objGeneralService.getCountCases("Equipo",objRepairBean.getNpimei());
                     strMessage=(String)hshData.get("strMessage");
                     if (strMessage!=null)
                        throw new Exception(strMessage); 
                     int iCasoEquipo=MiUtil.parseInt((String)hshData.get("iRetorno"));     
                  %>
                  <input type="text" name="txtCasosEquipo" size="5" maxlength="4" readonly="readonly" value="<%=iCasoEquipo%>"/>
              </td>
              <td class="CellContent">
                  <%
                      hshData=objGeneralService.getCountCases("Customer",lCustomerId+"");
                      strMessage=(String)hshData.get("strMessage");
                      if (strMessage!=null)
                        throw new Exception(strMessage); 
                      int iCasoCustomer=MiUtil.parseInt((String)hshData.get("iRetorno"));                  
                  %>               
                  <input type="text" name="txtCasosCliente" maxlength="15" readonly="readonly" value="<%=iCasoCustomer%>"/>
              </td>
              <td class="CellContent">
                  <input type="text" name="txtBlackList" size="15" maxlength="15" readonly="readonly" value="<%=strBlackList%>"/>    
              </td>
              <td class="CellContent" colspan="2">
                  <input type="text" name="txtContactoRecojo" size="40"  onchange="this.value=trim(this.value.toUpperCase());" value="<%=MiUtil.getString(objRepairBean.getNpcollectcontact())%>"/>
              </td>
              <td class="CellContent" colspan="2">
                  <textarea name="txtDireccionRecojo" cols="40" rows="1" onchange="this.value=trim(this.value.toUpperCase());"><%=MiUtil.getString(objRepairBean.getNpcollectaddress())%></textarea>
              </td>
              <td class="CellContent">
                  <input type="text" name="txtCodigoOsiptel" maxlength="15" readonly="readonly" value="<%=MiUtil.getString(objRepairBean.getNpcodeosiptel())%>"/>
              </td>
              <td class="CellContent">
              </td>
        </tr>

			</table>
		</td>
      <tr>
            
	<tr>
		<td height="15px">
		</td>
	</tr>
	<tr>
		<td align="left">
			<table border="0" cellspacing="0" cellpadding="0" align="left">
				<tr>
              <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="30"></td>
              <input type="hidden" name="hdnMethod" value=""/>              
              <input type="hidden" name="hdnListaFallasSeleccionadas" value=""/>
              <input type="hidden" name="hdnListaOtrasFallasSeleccionadas" value=""/>
              <input type="hidden" name="hdnListaRepuestosSeleccionados" value=""/>
              <input type="hidden" name="hdnListaRepuestosNuevosUsados" value=""/>                 
              <input type="hidden" name="hdnItemid" value="<%=objRepairBean.getNpitemid()%>" />              
              <td class="SubSectionTitle" align="left" valign="top">Reparaci&oacute;n</td>
              <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"> &nbsp;</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td align="center">
            <table border="0" cellspacing="1" cellpadding="2" width="100%" class="RegionBorder">
            <tr>
                                <td class="CellLabel" align="center" width="10%"><font color="red"><b>*</b></font>&nbsp;Proceso</td>
                               <td class="CellLabel" align="center" width="14%">Estado Rep.</td>          
                               <td class="CellLabel" align="center" width="12%">Nombre del Contacto</td>
                                <td class="CellLabel" align="center" width="12%">Apellido del Contacto</td>                                                                                    
                                
                                <td class="CellLabel" align="center" width="19%">
                                    <div class="divReparacionesRAE">Correo Electr&oacute;nico&nbsp;del&nbsp;Contacto</div>
                                </td>
                                <td class="CellLabel" align="center" width="10%">
                                    <div class="divReparacionesRAE">Tel&eacute;fono&nbsp;del&nbsp;Contacto</div>
                                </td>
                                <td class="CellLabel" align="left" width="15%">
                                    <div class="divReparacionesRAE">Preferencias&nbsp;de&nbsp;Notificaci&oacute;n</div>
                                </td>
            </tr>
            <tr>
                               <td class="CellContent" align="center">
						<select id="cmbProceso" name="cmbProceso" style="width: 75%" disabled>
							<%    
              
							hshComboMap = objGeneralService.getProcessList( MiUtil.getString(objRepairBean.getNpequipment())  );
                     strMessage = (String)hshComboMap.get(Constante.MESSAGE_OUTPUT);
                     if (strMessage!=null)
                        throw new Exception(strMessage);
                        
                     arrComboList = (ArrayList)hshComboMap.get("arrProcessList");
                     //String repairtype = MiUtil.getString(objRepairBean.getNprepairtype());
                     //if (MiUtil.getString(objRepairBean.getNprepairtype()) == "REX") 
                        //MiUtil.buildComboSelected(arrComboList,"valor","valor",MiUtil.getString(objRepairBean.getNprepairtype())
                       // objRepairBean.setNprepairtype("REP");
                     
                     %>                  
	                  <%=MiUtil.buildComboSelected(arrComboList,"valor","valor",MiUtil.getString(objRepairBean.getNprepairtype()))%>		
						</select>
            <input type="hidden" id="hdnProceso" name="hdnProceso" value="<%=objRepairBean.getNprepairtype()%>" />
            <input type="hidden" id="hdnServiceType" name="hdnServiceType" value="<%=objRepairBean.getNpservicetype()%>" />
            <input type="hidden" id="hdnServiceCode" name="hdnServiceCode" value="<%=objRepairBean.getNpservicio()%>" />
					</td>
               
               <td class="CellContent" align="center">
               
                                                <!-- [AEP] MMONTOYA -->
						<!--<select name="cmbEstadoReparacion" style="width: 75%" onchange="fxEstadoReparacion();" >-->
                                                <select name="cmbEstadoReparacion" style="width: 75%" disabled>
							<%    
							hshComboMap = objGeneralService.getCodeSetList("Repair Status");
                     strMessage = (String)hshComboMap.get(Constante.MESSAGE_OUTPUT);
                     if (strMessage!=null)
                        throw new Exception(strMessage);
                        
                     arrComboList = (ArrayList)hshComboMap.get("arrDominioLista");
                     %>   
                     <%=MiUtil.buildComboSelected(arrComboList,"valor","valor",objRepairBean.getNpstatus())%>	
						</select>
            <input type="hidden" id="hdnEstadoReparacion" name="hdnEstadoReparacion" value="<%=objRepairBean.getNpstatus()%>" />
					</td>                                                  
                                                                                                                  
                                                    
                <td class="CellContent" align="center">
                        <input type="text" name="txtContactoNombre" size="20" maxlength="15" value="<%=MiUtil.getString(objRepairBean.getNpcontactfirstname())%>" readonly/> 
                </td>

                <td class="CellContent" align="center">
                        <input type="text" name="txtContactoApellido" size="20" maxlength="15" value="<%=MiUtil.getString(objRepairBean.getNpcontactlastname())%>" readonly/>
                </td>
                <td class="CellContent" align="center">
                      <div class="divReparacionesRAE">
                        <input type="text" name="txtCorreoElectronico" size="35" maxlength="200" value="<%=MiUtil.getString( objRepairBean.getNpemailcontact() )%>" disabled/> 
                      </div>
                </td>

                <td class="CellContent" align="center">
                        <div class="divReparacionesRAE">
                            <input type="text" name="txtTelefonoContacto" size="20" maxlength="15" value="<%=MiUtil.getString( objRepairBean.getNpphonecontact() )%>" disabled/>
                        </div>
                </td>

                <td class="CellContent" align="center" >
                  <div class="divReparacionesRAE">
                    <select id="cmbPrefNotif" name="cmbPrefNotif" style="width: 100%;" disabled>
                        <%
                         hshCmbPrefNotif = objRepairService.getConfigurations(Constante.KV_ST_NOTIFICATION_PREFERENCES);
                         strMessage = (String)hshCmbPrefNotif.get("strMessage");
                         
                         valoresCombo = (ArrayList)hshCmbPrefNotif.get("arrListado");
                         if(valoresCombo != null){
                        System.out.println(" ==>> cmbTipoEquipo: "+valoresCombo.size());
                        %>                                                    
                          <%
                          for(int i=0;i<valoresCombo.size();i++){
                          DominioBean dominio = (DominioBean)valoresCombo.get(i);
                              if(dominio.getValor().equals(MiUtil.getString( objRepairBean.getNpnotifpref() ))){
                              %>
                                 <OPTION VALUE="<%=dominio.getValor()%>" selected="selected"> <%=dominio.getDescripcion()%></OPTION>
                              <%}else{
                              %>
                                 <OPTION VALUE="<%=dominio.getValor()%>"> <%=dominio.getDescripcion()%></OPTION>
                              <%
                              }                              
                          }%>

                         <%}%>
                    </select>
                    
                  </div>
              </td>
            </tr>        
            
            <tr>
               
               <td class="CellLabel" align="center" >Creado Por</td>
               <td class="CellLabel" align="center" >Fecha de Creación</td>               
					
               <td class="CellLabel" align="center"></td>
               
               <td class="CellLabel" align="center">
                    <div id="dvBySpecLabelTimerValue" style='display:none'>Timer Value</div>
                </td>
               <% if (strShowLabelIN.compareTo("1") == 0){
               %>
                    <td class="CellLabel" align="center">Estado Final del Servicio</td>
                    <td class="CellLabel" align="center">Sistema Operativo</td>
               <%}else{%>
                    <td class="CellLabel" align="center"></td>
                    <td class="CellLabel" align="center"></td>
               <%}%>
               <td class="CellLabel" align="center">Tienda de Recojo</td>               
            </tr>

            <tr>
               
               <td class="CellContent" align="center"><%=MiUtil.getString(objOrderBean.getNpCreatedBy())%>
						<!--input type="text" name="txtCreadoPor" size="15" maxlength="15" readonly="readonly" value=""/-->
					</td>
               
					<td class="CellContent" align="center"><%=MiUtil.toFechaHora(objOrderBean.getNpCreatedDate())%>
						<!--input type="text" name="txtFechaCreacion" size="15" maxlength="15" readonly="readonly" value=""/--> 
					</td>   
               
              
              <% if (objRepairBean.getNpstatus().equals("CERRADO")  ){  %> 	                  	               
                     		            
                  <td class="CellContent" align="center">   
                     <a href="javascript:fxRepair()">Orden de Reparación</a>
                  </td>               
                                                           
              <%} %>
                                                           
               <td class="CellContent" align="center">					                                    
                  <div id="dvBySpectxtTimerValue" style='display:none'>
                  <input type="text" name="txtTimerValue" size="20" maxlength="15"  value="<%=MiUtil.getString(objRepairBean.getNptimervalue())%>" onKeypress="if (event.keyCode < 44 || event.keyCode > 57 ||  event.keyCode==45 || event.keyCode==47) event.returnValue = false;" />
                  <!-- campo de la tabla -->
                  </div>
               </td>
             <% if (strShowLabelIN.compareTo("1") == 0){
               %>
               <td class="CellContent" align="center" >
               <select name="cmbEstFinEquip" style="width: 50%" disabled>
							<%
                     hshComboMap = objRepairService.getFinalEstateList();
                     strMessage = (String)hshComboMap.get(Constante.MESSAGE_OUTPUT);
                     if (strMessage!=null)
                        throw new Exception(strMessage);
                        
                     arrComboList = (ArrayList)hshComboMap.get("arrDominioLista");
                     %>
                     <%=MiUtil.buildComboSelected(arrComboList,"id","valor",objRepairBean.getNPFINALSTATE())%>
						</select>
					</td>
               <td class="CellContent" align="center" >
               <select name="cmbEquipSO" style="width: 50%" disabled >
							<%
                     hshComboMap = objRepairService.getEquipSOList();
                     strMessage = (String)hshComboMap.get(Constante.MESSAGE_OUTPUT);
                     if (strMessage!=null)
                        throw new Exception(strMessage);
                    
                     arrComboList = (ArrayList)hshComboMap.get("arrDominioLista");
                     %>
                     <%=MiUtil.buildComboSelected(arrComboList,"id","valor",objRepairBean.getNPEQUIPSO())%>
						</select>
					</td>
          <%}else{%>
              <td class="CellContent">
					</td>
              <td class="CellContent">
					</td>
          <%}%>
               <td class="CellContent" align="center">
                <select id="cmbTiendaRecojo" name="cmbTiendaRecojo" disabled>
                    <%	hshDataMap = objGeneralService.getBuildingList(Constante.BUILDING_TIPO_FISICA);
                    valoresCombo = (ArrayList) hshDataMap.get("arrBuildingList");
                    strMessage = (String) hshDataMap.get("strMessage");
                    if (strMessage==null){
                      for(int i=0;i<valoresCombo.size();i++){
                        DominioBean dominio = (DominioBean) valoresCombo.get(i);
                        if(dominio.getValor().equals(MiUtil.getString(objRepairBean.getNpstorepickup()))){ %>                        
                        <option value='<%=dominio.getValor()%>' selected="selected"><%=dominio.getDescripcion()%></option>
                      <%
                        }else{
                            %>
                        <option value='<%=dominio.getValor()%>'><%=dominio.getDescripcion()%></option>
                      <%
                        }
                      }
                    }%>
                </select>
                 </td>
            </tr>
            <tr>
               
               <td class="CellLabel" align="center" >Estado OE</td>
               <td class="CellLabel" align="center" >Estado de Cotizaci&oacute;n</td>
               <td class="CellLabel" align="center" ></td>
               <td id="tr_accesorios" class="CellLabel" align="center">
			   <div id="dvBySpecTipoAccesorioName" style='display:none'><font color="red"><b>*</b></font>&nbsp;Tipo Accesorio</div></td>
			   
               <td id="tr_accesorios" class="CellLabel" align="center">
               <div id="dvBySpecModelAccesorioName" style='display:none'><font color="red"><b>*</b></font>&nbsp;Modelo Accesorio</div></td>
               <td id="tr_accesorios" class="CellLabel" align="center">
               <div id="dvBySpecSemFrabicName" style='display:none'><font color="red"><b>*</b></font>&nbsp;Sem. Fabricación</div></td>
               <td id="tr_accesorios" class="CellLabel" align="center"><div id="dvBySpecFechaVenta" style=display:'none'><font color="red"><b>*</b></font>&nbsp;Fecha de Venta</div></td>
               
            </tr>
            <tr>
               
               <td class="CellContent" align="center" >
               <input type="text" name="txtEstadoOrdenExterna" size="20" maxlength="15" value="<%=strRepairStat%>" disabled="disabled"/>
                </td>
               <td class="CellContent" align="center" >
               <input type="text" name="txtEstadoCotizacion" size="20" maxlength="15" value="<%=strUserResponse%>" disabled="disabled"/>
                </td>               
                            
               <td class="CellContent" align="center"></td>               
               <td class="CellContent" align="center">
                <div id="dvBySpecTipoAccesorio" style='display:none'>
                    <select name="cmbTipoAccesorio" style="width: 100%" disabled>
                      <% hshComboMap = objRepairService.getAccesories();
                                                     strMessage = (String)hshComboMap.get(Constante.MESSAGE_OUTPUT);
                                                     if (strMessage!=null)
                                                                                    throw new Exception(strMessage);
                                                     
                       arrComboList = (ArrayList)hshComboMap.get("arrAccesories");
                      %>
                      <%=MiUtil.buildComboSelected(arrComboList,MiUtil.getString(objRepairBean.getNpaccessorytype())) %>
                      </select>
                      </div>
                            <div id="dvBySpecGenerateRA" style=display:'none'>
                              <input type="button" name="btnGenerateRA" value="Generar Modelo" style="width: 125px" disabled />
                            </div>
                </td>
               
               <td class="CellContent" align="center">
			   <div id="dvBySpecModelAccesorio" style='display:none'>
                              <select name="cmbModeloAccesorio" style="width: 100%" disabled>                  
                              <% hshComboMap = objRepairService.getAccesoryModels(objRepairBean.getNpaccessorytype());
                                     strMessage = (String)hshComboMap.get(Constante.MESSAGE_OUTPUT);
                                     if (strMessage!=null)
                                     throw new Exception(strMessage);                     
                                     arrComboList = (ArrayList)hshComboMap.get("arrAccesoryModels");
                              %>
                              <%=MiUtil.buildComboSelected(arrComboList,MiUtil.getString(objRepairBean.getNpaccessorymodel())) %>
                                                                                                            <option value=""></option>
                              </select>
                              </div>						
                                    <div id="dvBySpecNacionalizacion" style='display:none'>
                                      <input type="button" name="btnNacionalizar" value="Nacionalizar" style="width: 125px" disabled/>
                                    </div>
			   </td>               
               <td class="CellContent" align="center">
                     <div id="dvBySpecSemFrabic" style='display:none'>
				  <input type="text" name="txtSemFabricacion" size="8" maxlength="8" value="<%=MiUtil.getString(objRepairBean.getNpfrequency())%>" disabled/>
				  </div><div id="dvBySpecSemFrabicRule" style=display:'none'><font size="1"> </font></div>         
			   </td>               
               <td class="CellContent" align="center">
				  <div id="dvBySpecFechaV" style='display:none'>                                   
                 
                  <input type="text" name="txtFechaVenta" size="10" maxlength="10" disabled onblur="this.value=fxTrim(this.value);" value="<%=objRepairBean.getNpselldateacc()%>" onKeypress="if (event.keyCode < 44 || event.keyCode > 57 || event.keyCode==45) event.returnValue = false;" />
                     <a href="#" onclick="return false" onmouseover="window.status='Fecha Desde';return true;" onmouseout="window.status='';return true;">
                     <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/show-calendar.gif" width="24" height="22" border="0"></a>&nbsp;<BR><i></i>
      
                 
                  </div><div id="dvBySpecFechaVent" style=display:'none'><font size="1">DD/MM/YYYY  </font></div>
               </td>               
            </tr>
            <tr>
                <td colspan="7">&nbsp;</td>
            </tr>
        </table>
        
                       
        
 <!-- inicio FALLAS OTRRAS FALLAS, PORTEGA -->
 
   <table border="0" cellspacing="1" cellpadding="1" align="center" width="100%">
    <tr>
      <td height="15px"></td>
    </tr>
    <tr>
      <td align="left">
        <table border="0" cellspacing="0" cellpadding="0" align="left" >
          <tr>
            <td class="SectionTitleLeftCurve" width="8">&nbsp;&nbsp;</td>
            <td class="SectionTitle" width="100" align="center">Tratamiento</td>
            <td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
          </tr>
        </table>
      </td>      
    </tr>
  </table>
  <table border="0" cellspacing="1" cellpadding="1" align="center" class="RegionBorder" width="100%">
    <tr>
      <td height="10px"></td>
      <td height="10px"></td>
    </tr>
    <tr id="tr_prueba">
      <td align="left" width="100%" height="16" valign="top">
        <table border="0" cellspacing="0" cellpadding="0" align="left" width="100%">
            <tr>
                <td valign="top">
                    <table border="0" cellspacing="0" cellpadding="0" align="left" width="100%">
                        <tr>
                            <td>
                                <table border="0" cellspacing="0" cellpadding="0" align="left">
                                  <tr>
                                    <td class="SectionTitleLeftCurve" width="8">&nbsp;&nbsp;</td>
                                    <td class="SectionTitle" align="center">Reporte del Cliente</td>
                                    <td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
                                  </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td align="left" width="100%">
                                <table border="0" cellspacing="0" cellpadding="0" align="left" class="RegionBorder" HEIGHT ="100%" width="100%">
                                  <tr>
                                    <td class="CellLabel" align="Left" width="40%">&nbsp;&nbsp;&nbsp;Tipo de Falla</td>
                                    
                                    <td class="CellContent" width="60%">
                                        <select name="cmbTipoFalla" disabled / >
                                        <%
                                          hshDataMap = objGeneralService.getDominioList("TIPODEFALLA");
                                          valoresCombo = (ArrayList) hshDataMap.get("arrDominioList");
                                          strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
                                          if (StringUtils.isBlank(strMessage)){%>
                                          <%=
                                            MiUtil.buildComboSelected(valoresCombo, "1")
                                          %>
                                          <%}%>
                                          </select>
                                          <input type="checkbox" id="chkFnc" value= "Y" name="chkFnc" disabled/>FNC 
                                      </td>                    
                                  </tr>
                                  <tr>
                                    <td colspan="2">
                                      <table border="0" cellspacing="0" cellpadding="0" align="left" HEIGHT ="100%" width="1">
                                        <tr>
                                          <td class="CellLabel" align="Left" >
                                            <select name= "lstFalla" id="lstFalla" multiple="" size="10" style="width: 280px;">
                                            </select>
                                          </td>
                                          <td class="CellContent" align="Center" width="1%">
                                            <input type="button" name="btn1" value = " > " style="width: 20px;" disabled />
                                            <input type="button" name="btn2" value = " < " style="width: 20px;" disabled />
                                          </td>
                                          <td class="CellLabel" align="Left" >
                                            <select name = "lstFallaResultado" id="lstFallaResultado" multiple="" size="10" style="width: 280px;">
                                            </select>
                                          </td>
                                        </tr>
                                      </table>        
                                    </td>
                                  </tr>
                                  <tr>
                                    <td class="CellLabel" align="Left" >&nbsp;&nbsp;&nbsp;Reporte del Cliente</td>
                                    <td class="CellLabel" align="Left"></td>
                                  </tr>
                                  <tr>
                                    <td class="CellLabel" align="Left" colspan="2">
                                      <TEXTAREA COLS="130" ROWS="12" NAME="txtReporteCliente" readonly><%=MiUtil.getString(objOrderBean.getNpDescription())%></textarea>
                                    </td>
                                  </tr>
                                </table>
                              </td>
                        </tr>
                    </table>
                </td>
                <td>
                    <table>
                        <tr>
                            <td>
                                <table border="0" cellspacing="0" cellpadding="0" align="left">
                                  <tr>
                                    <td class="SectionTitleLeftCurve" width="8">&nbsp;&nbsp;</td>
                                    <td class="SectionTitle" width="100" align="center">Otras&nbsp;Fallas</td>
                                    <td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
                                  </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td align="left" width="50%">
                                <table border="0" cellspacing="0" cellpadding="0" align="left" class="RegionBorder" HEIGHT ="100%" width="100%">
                                  <tr>
                                    <td>
                                        <table border="0" cellspacing="0" cellpadding="0" align="left" HEIGHT ="100%" width="1">
                                        <tr>
                                          <td class="CellLabel" align="Left" >
                                            <select name= "lstOtrasFallas" id="lstOtrasFallas" multiple="" size="10" style="width: 280px;">
                                            </select>
                                          </td>
                                          <td class="CellContent" align="Center" width="1%">
                                            <input type="button" name="btn3" value = " > " style="width: 20px;" disabled />
                                            <input type="button" name="btn4" value = " < " style="width: 20px;" disabled />
                                          </td>
                                          <td class="CellLabel" align="Left" >
                                            <select name = "lstOtrasFallasResultado" id="lstOtrasFallasResultado" multiple="" size="10" style="width: 280px;">
                                            </select>
                                          </td>
                                        </tr>
                                      </table>
                                      <!-- OTRAS FALLAS -->
                                            <script type="text/javascript">

                                                DeleteSelectOptions(document.frmdatos.lstOtrasFallas);
                                                DeleteSelectOptions(document.frmdatos.lstOtrasFallasResultado);
                                                
                                             <% 
                                                hshDataOtherFailureMap = objRepairService.getOtherFailureList(intRepairId_repair, strRepairTypeId);
                                                hshDataOtherFailureResultadoMap = objRepairService.getSelectedOtherFailureList(intRepairId_repair, strRepairTypeId);
                                                
                                                strMessage = (String)hshDataOtherFailureMap.get(Constante.MESSAGE_OUTPUT);
                            
                                                //hshDataOtherFailureMap = (HashMap) request.getAttribute(Constante.DATA_STRUCT); //lista de fallas    
                                            //HashMap hshDataMapResultado = (HashMap) request.getAttribute("hshDataMapAux"); //lista de fallas seleccionadas
                                        
                                                if(StringUtils.isBlank(strMessage)) {
                                                        ArrayList uLista = (ArrayList) hshDataOtherFailureMap.get("arrListado");
                                                        Iterator iterator = uLista.iterator();
                                                        while(iterator.hasNext()) {
                                                            DominioBean dominio = new DominioBean();
                                                            dominio = (DominioBean) iterator.next();
                                                            
                                                            %>
                                                            fxAddNewOption(document.frmdatos.lstOtrasFallas,'<%=(String) dominio.getValor()%>','<%=(String) dominio.getDescripcion() %>'); 
                                                            <%
                                                        }
                                                }
                                                
                                                strMessage = (String)hshDataOtherFailureResultadoMap.get(Constante.MESSAGE_OUTPUT);
                            
                                                //hshDataOtherFailureMap = (HashMap) request.getAttribute(Constante.DATA_STRUCT); //lista de fallas    
                                            //HashMap hshDataMapResultado = (HashMap) request.getAttribute("hshDataMapAux"); //lista de fallas seleccionadas
                                        
                                                if(StringUtils.isBlank(strMessage)) {
                                                        ArrayList uLista = (ArrayList) hshDataOtherFailureResultadoMap.get("arrListado");
                                                        Iterator iterator = uLista.iterator();
                                                        while(iterator.hasNext()) {
                                                            DominioBean dominio = new DominioBean();
                                                            dominio = (DominioBean) iterator.next();
                                                            
                                                            %>
                                                            fxAddNewOption(document.frmdatos.lstOtrasFallasResultado,'<%=(String) dominio.getValor()%>','<%=(String) dominio.getDescripcion() %>'); 
                                                            <%
                                                        }
                                                }
                                            
                                            %>
                                            function fxAddNewOption(TheCmb, Value, Description) {
                                                var option = new Option(Description, Value);
                                                var i = TheCmb.options.length;
                                                TheCmb.options[i] = option;
                                            }
                                            
                                            </script>
                                          <!-- -->
                                    </td>
                                  </tr>
                                  <tr>
                                    <td>
                                    <table border="0" cellspacing="0" cellpadding="0" align="left">
                                      <tr>
                                        <td class="SectionTitleLeftCurve" width="8">&nbsp;&nbsp;</td>
                                        <td class="SectionTitle" width="100" align="center">Evaluaci&oacute;n&nbsp;Interna</td>
                                        <td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
                                      </tr>
                                    </table>
                                </td>
                                </tr>
                                <tr>
                                  <td align="left" width="100%">
                                    
                                    <table width="100%" border="0" cellspacing="0" cellpadding="0" align="left">
                                        <tr>
                                            <td class="CellLabel" colspan="2" align="Left">
                                                &nbsp;Detalle&nbsp;Diagn&oacute;stico:&nbsp;
                                            </td>
                                            <td class="CellLabel" align="Left">
                                                &nbsp;Observaciones&nbsp;del&nbsp;Asesor:&nbsp;
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="CellLabel" colspan="2"
                                                height="15">
                                                <TEXTAREA COLS="60" ROWS="3" NAME="txtDetDiagnostico" readonly><%=strDetDiagInterna%></TEXTAREA>
                                            </td>
                                            <td class="CellLabel" rowspan="7">
                                                <TEXTAREA COLS="60" ROWS="11" NAME="txtObsAsesor" readonly><%=strObsAsesorInterna%></TEXTAREA>
                                            </td>
                                        </tr>
                                        <tr id="trResolucion">
                                            <td class="CellLabel" align="Left">&nbsp;&nbsp;&nbsp;Resoluci&oacute;n</td>                                            
                                              <td class="CellLabel" >
                                              <select name="cmbDiagnostico" disabled>
                                               <%                   
                                               String strProviderId =  String.valueOf(objRepairBean.getNpprovider()); // 02/04/2009                              
                                               //hshDataMap = objRepairService.getDiagnosisLevel(strProviderId,Constante.DIAGNOSIS_LEVEL_1);
                                               String strFabricatorDiag =  String.valueOf(objRepairBean.getNPFABRICATORID()); // 05/04/2011
                                               hshDataMap = objRepairService.getDiagnosisLevel(strFabricatorDiag,Constante.DIAGNOSIS_LEVEL_1);
                                               valoresCombo = (ArrayList) hshDataMap.get("arrListado");
                                               strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
                                               if (StringUtils.isBlank(strMessage)){%>
                                                  <%=MiUtil.buildComboSelected(valoresCombo,objRepairBean.getNpdiagncode())%>
                                                  <%}%>
                                               </select>  
                                              </td>
                                        </tr>
                                        <tr>
                                            <td class="CellLabel" colspan="2">
                                                &nbsp;Detalle&nbsp;Resoluci&oacute;n&nbsp;
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="CellLabel" colspan="2">
                                                <TEXTAREA COLS="60" ROWS="5" NAME="txtNextelDiagnostico" readonly><%=MiUtil.getString(objRepairBean.getNpdescriptionnextel())%></TEXTAREA>
                                            </td>
                                        </tr>
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
      </td>
    </tr>
   <!-- </table>
    <table border="0" cellspacing="0" cellpadding="0" align="left" > -->
    <tr>
      <td align="left">
        <table border="0" cellspacing="0" cellpadding="0" align="left" width="100%" >
            <tr>
					<td width="75%">
						<table border="0" cellspacing="0" cellpadding="0" align="left" style="margin-right: 10px; padding-right: 3px;" width="100%" >
							<tr>
								<td colspan="4">
									<table border="0" cellspacing="0" cellpadding="0" align="left" >
									  <tr>
										<td class="SectionTitleLeftCurve" width="8">&nbsp;&nbsp;</td>
										<td class="SectionTitle" align="center">Centro Reparaci&oacute;n</td>
										<td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
									  </tr>
									</table>
								</td>
							</tr>
							<tr>
								<td class="CellLabel" colspan="4">&nbsp;&nbsp;</td>
							</tr>
							<tr>
								<td class="CellLabel">Tipo de Falla:</td>
								<td class="CellLabel"><TEXTAREA COLS="65" ROWS="2" NAME="txtTipoFalla" readonly><%=strTipoFallaCentro%></TEXTAREA></td>
								<td class="CellLabel">&nbsp;&nbsp;</td>
								<td class="CellLabel">&nbsp;&nbsp;</td>
							</tr>
							<tr>
								<td class="CellLabel" height="44">Diagn&oacute;stico:</td>
								<td class="CellLabel" height="44"><input id="txtDiagnosticoCentro" name="txtDiagnosticoCentro" size="63" type="text" value="<%=strdiagCentro%>" readonly/></td>
								<td class="CellLabel">Resoluci&oacute;n:</td>
								<td class="CellLabel"><input id="txtResolucionCentro" name="txtResolucionCentro" size="63" type="text" value="<%=strresolCentro%>" readonly/></td>
							</tr>
							<tr>
								<td class="CellLabel">Detalle de Diagn&oacute;stico:</td>
								<td class="CellLabel">
									<TEXTAREA COLS="65" ROWS="4" NAME="txtDetalleDiagnosticoCentro" readonly><%=strdetDiagCentro%></TEXTAREA>
								</td>
								<td class="CellLabel" rowspan="2" style="vertical-align: top;">Detalle de Resoluci&oacute;n:</td>
								<td class="CellLabel" rowspan="2" style="vertical-align: top;"><TEXTAREA COLS="65" ROWS="4" NAME="txtDetalleResolucionCentro" readonly><%=strdetResolCentro%></TEXTAREA></td>
							</tr>
							<tr>
								<td class="CellLabel" colspan="4">&nbsp;&nbsp;</td>
							</tr>
						</table>
					</td>
				  <td id="td_prueba1" style="vertical-align: top;" width="25%" height="100%">
					<table border="0" cellspacing="0" cellpadding="0" align="left" width="100%" style="margin-left: 10px;  padding-left: 3px;">
					  <tr>
						<td height="100%">
							<table border="0" cellspacing="0" cellpadding="0" align="left" >
							  <tr>
								<td class="SectionTitleLeftCurve" width="8">&nbsp;&nbsp;</td>
								<td class="SectionTitle" align="center">Situaci&oacute;n Equipo</td>
								<td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
							  </tr>
							</table>
						</td>
					  </tr>
                                          <tr>
                                                <td class="CellLabel">&nbsp;&nbsp;</td>
                                          </tr>
					 <tr>
						<td class="CellLabel" height="165" style="vertical-align: top;" align="center">
                                                    <select name="cmbSituacion" style="width: 250px;" disabled>
                                                       <%hshDataMap = objGeneralService.getDominioList("REPAIR_ORE_SITUACION_EQUIPOS");
                                                       valoresCombo = (ArrayList) hshDataMap.get("arrDominioList");
                                                       strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
                                                       if (StringUtils.isBlank(strMessage)){%>
                                                          <%=MiUtil.buildComboSelected(valoresCombo, objRepairBean.getNpendsituation())%>
                                                          <%}%>
                                                    </select>
						</td>
					 </tr>
					</table>
					
				  </td>
            </tr>
        </table>
      </td>
    </tr>  
    <tr>
    <td align="center">
	<table width="100%">
	<tr>
		  <td class="CellLabel" align="Left" width="50%" >      
			<div id="dvBySpecTipoProceso" style=display:'none'><input type="button" name="btnEmitirFormato" value = "    Generar Reparación    " style="width: 175px;" onclick="javascript:fxGenerateRepair();"/></div>
		  </td>  
				  
		  <td class="CellLabel" align="Left" width="50%" >
			<input type="button" name="btnImprimirFormato" value="Imp. Formato con Prestamo" style="width: 175px" onclick="javascript:fxImprimirFormato();"/>
			<input type="button" name="btnImprimirFormato2" value="Imp. Formato sin Prestamo" style="width: 175px" onclick="javascript:fxImprimirFormato2();"/>
		  </td>
	</tr>
	</table>
    </td>
    </tr>
    <tr>
		<td align="left">			
         <%-- [AEP] MMONTOYA <% if ("Enabled".equals(MiUtil.getString((String)hshScreenField.get("createPayOrderRepair")))){ %>                
					<input name="btnGenerarOrdenPago" type="button" value="Generar Orden Pago" onclick="javascript:fxGenerarOrdenPago();">
			<%	}%> --%>
		</td>
    </tr>	
  </table>

 <!-- FIN FALLAS OTRRAS FALLAS, PORTEGA -->
                             
        <br>                                                           
                </td>
	</tr>
	<tr>
		<td height="15px">
		</td>
	</tr>
	
</table>
<script>
	
	var strSlash = "/";
   
	function fxLoadCodigoResolucion(cod){
		form = document.frmdatos;    
		deleteOptionIE(form.cmbCodigoResolucion);
		form.hdnObjectAllId.value=cod;      
		form.action="<%=request.getContextPath()%>/repairServlet?hdnMethod=loadCombo&hdnNameField=REPAIR_RESOLUTION_CODE";        
		form.submit();              
	}
   
	function fxGenerateOrderInterna() {
    if (confirm("Recuerde que debe GRABAR LA ORDEN...\nEsta seguro que desea Crear una Orden Interna?")) {    
		if (fxValidaImeiListaPrestamoCambio()==false){
			return;
		}	
        var vForm = document.frmdatos;
        var orderId="<%=lOrderId%>";
        if(orderId!="") {
          vForm.action= "<%=request.getContextPath()%>/repairServlet?hdnMethod=generateOrderInterna";
			 vForm.submit();              
        }
      }
	}
   
	function fxGenerateOrderExterna() {
    if (confirm("Recuerde que debe GRABAR LA ORDEN...\nEsta seguro que desea Crear una Orden Externa?")) {    
		if (fxValidaImeiListaPrestamoCambio()==false){
			return;
		}	
      var vForm = document.frmdatos;
      vForm.action="<%=request.getContextPath()%>/repairServlet?hdnMethod=generateOrderExterna";		
      vForm.submit();
    }
	}
	
	function fxAgregarDocumento() {
		var tabla = document.getElementById("tblListaPrestamosCambios");
		cantFils = tabla.rows.length; 
		cantCols = tabla.rows[0].cells.length;
		nuevaFila = tabla.insertRow(cantFils);
    if(cantFils == 1){
        for(i = 0; i < cantCols; i++) {
            nuevaColumna = nuevaFila.insertCell(i);
            nuevoTexto = fxGetContentDefaultList(i, cantFils);
            nuevaColumna.innerHTML = nuevoTexto;
            nuevaColumna.align = "center";
            nuevaColumna.className = "CellContent";            
        }
            document.frmdatos.cmbTipoImeiLista.value="CLIENTE";
    }else{
        for(i = 0; i < cantCols; i++) {
            nuevaColumna = nuevaFila.insertCell(i);
            nuevoTexto = fxGetContentChangeLoanList(i, cantFils);
            nuevaColumna.innerHTML = nuevoTexto;
            nuevaColumna.align = "center";
            nuevaColumna.className = "CellContent";
        }
     }
	}
	   
      
	function fxGetContentChangeLoanList(index, cantFils) {
		var strContent = "";
		switch(index) {
			case 0:
				strContent = "<input type='checkbox' name='chkIndiceLista'/> <input type='hidden' name='hdnRepListId'/>";
				break;
			case 1:
				strContent = cantFils;
				break;
			case 2:
				try {
					cantInputsByCol = document.frmdatos.txtImeiLista.length;
					if(cantInputsByCol == undefined) {
						cantInputsByCol = 1;
					}
				}
				catch(exception) {
					cantInputsByCol = 0;
				}
        
        if (cantFils == 1)
          strContent = "<input readonly='true' style='background-color:yellow;' type='text' name='txtImeiLista' size='20' onchange='fxLoadImeiInfo(this.value, "+(cantInputsByCol)+")'/>";
        else
          strContent = "<input type='text' name='txtImeiLista' size='20' onchange='fxLoadImeiInfo(this.value, "+(cantInputsByCol)+")'/>";

				break;
			case 3:
				strContent = "<input type='text' name='txtSerieLista' size='15' maxlength='15' readonly='readonly'/>";
				break;
			case 4:
				strContent = "<select name='cmbTipoImeiLista'>"+
					'<%=MiUtil.buildComboSelected(arrModalitySellList, "").replaceAll("\n","")%>'+
					"<"+strSlash+"select>";
				break;
			case 5:
				strContent = "<input type='checkbox' name='chkCrearDocLista'/>";
				break;         
         case 6:
				strContent = "<input type='radio' name='chkActivarEquipo'/>";
				break;
            
			default:
				strContent = "";
				break;
		}
		return strContent;
	}

	function fxGetContentDefaultList(index, cantFils) {
		var strContent = "";
		switch(index) {
			case 0:
				strContent = "<input type='checkbox' name='chkIndiceLista'/> <input type='hidden' name='hdnRepListId'/>";
				break;
			case 1:
				strContent = cantFils;
				break;
			case 2:
				try {
					cantInputsByCol = document.frmdatos.txtImeiLista.length;
					if(cantInputsByCol == undefined) {
						cantInputsByCol = 1;
					}
				}
				catch(exception) {
					cantInputsByCol = 0;
				}

        //if (cantFils != 1)
        //    strContent = "<input type='text' name='txtImeiLista' size='15' maxlength='15' value='<%=MiUtil.getString(objRepairBean.getNpimei())%>'/>";
        //else
            strContent = "<input readonly='true' style='background-color:yellow;' type='text' name='txtImeiLista' size='20' value='<%=MiUtil.getString(objRepairBean.getNpimei())%>'/>";
				break;
			case 3:
				strContent = "<input type='text' name='txtSerieLista' size='15' maxlength='15' readonly='readonly' value='<%=MiUtil.getString(objRepairBean.getNpimeisn())%>'/>";
				break;
			case 4:
				strContent = "<select name='cmbTipoImeiLista'>"+
					'<%=MiUtil.buildComboSelected(arrModalitySellList, "").replaceAll("\n","")%>'+
					"<"+strSlash+"select>";
				break;
			case 5:
				strContent = "<input type='checkbox' name='chkCrearDocLista'/>";
				break;
			default:
				strContent = "";
				break;
		}
		return strContent;
	}
	
	function fxLoadImeiInfo(txtImeiLista, index) {            
      var flagTab = "1";
        if(txtImeiLista != "") {
            var url = "<%=request.getContextPath()%>/repairServlet?hdnMethod=loadImeiInfo&txtImeiLista="+txtImeiLista+"&index="+index+"&flagTab="+flagTab;
            parent.bottomFrame.location.replace(url);
      }
	}
	
  /*JOYOLAR - 12/08/2008
  Se comenta validación para la generación de documentos, la misma se efectuara desde la bd */
	function fxGenerarDocumento() {
		vForm = document.frmdatos;
		cmbProceso = vForm.cmbProceso.value;
		if (fxValidaImeiListaPrestamoCambio()==false){
			return;
		}		 
		if(cmbProceso != ""){
			//if( vForm.txtAlquilado.value=='No'){ Antes
         if( vForm.txtAlquilado.value=='Cliente'){ 
				//if(cmbProceso == "<%=Constante.ST_PROCESO_REX%>"){
				//alert("No se puede Generar Documentos para este tipo de Proceso");
				//alert("No se puede Generar Documentos para equipos propios");
			//return false;
			}
		}
		if (fxTieneFilaTabla()==false){
			return false;
		}
		fxSetValuesChecks();
		vForm.hdnFlagGenerate.value = "S";
		vForm.target = "bottomFrame";
    vForm.btnGenerarDocumento.disabled=true;
		vForm.action = "<%=request.getContextPath()%>/repairServlet?hdnMethod=generateDocument";
		vForm.submit();
	}
	
	function fxSetValuesChecks() {
		vForm = document.frmdatos;
		try {

			var total = eval(tblListaPrestamosCambios.rows.length);	
			var sCreateDoc="";
			if (total==2){			
				if (document.frmdatos.chkCrearDocLista.checked){
					document.frmdatos.chkCrearDocLista.value=1;
					sCreateDoc=sCreateDoc+"1-";
				}
				else{
					document.frmdatos.chkCrearDocLista.value=0;
					sCreateDoc=sCreateDoc+"0-";
				}						
			}			
			else{			
				for(var i = 0; i<total-1; i++){
					if (document.frmdatos.chkCrearDocLista[i].checked){
						document.frmdatos.chkCrearDocLista[i].value=1;
						sCreateDoc=sCreateDoc+"1-";
					}
					else{
						document.frmdatos.chkCrearDocLista[i].value=0;
						sCreateDoc=sCreateDoc+"0-";
					}				
				}
			}	
			vForm.hdnCrearDocListaAux.value=sCreateDoc;
		} catch(exception) {}
	}
	
	function fxTieneFilaTabla() {
		var total = eval(tblListaPrestamosCambios.rows.length);
		if (total<2){
			alert("No hay registros en la tabla de Lista de Préstamos & Cambios");
			return false;
		}	
		else{
			bolSelecciono=false;
			if (total==2){
				if (!document.frmdatos.chkCrearDocLista.disabled){
					if (document.frmdatos.chkCrearDocLista.checked){
						bolSelecciono=true;						
					}							
				}
			}
			else{
				for(var i = 0; i<total-1; i++){
					if (!document.frmdatos.chkCrearDocLista[i].disabled){
						if (document.frmdatos.chkCrearDocLista[i].checked){
							bolSelecciono=true;						
						}			
					}	
				}
			}	
			if (bolSelecciono==false)
				alert("Para generar Documentos, necesita añadir y seleccionar al menos un item de Préstamo & Cambio");
			return bolSelecciono;
		}	
	}
	
	function fxEliminarDocumento() {
		vForm = document.frmdatos;
		acum = 0;
		try{
			var tabla = document.getElementById("tblListaPrestamosCambios");
			chkIndiceLista = vForm.chkIndiceLista;
			numFilas = chkIndiceLista.length;
			for(i = 0; i < numFilas; i++) {
				if(chkIndiceLista[i].checked) {
					acum++;
				}
			}
			if(acum == 1) {
				if (!confirm("Está seguro de eliminar el registro seleccionado?")) {
					return;
				}
			}
			if(acum > 1) {
				if (!confirm("Está seguro de eliminar los registros seleccionados?")) {
					return;
				}
			}
			if(numFilas == undefined) {
				if(chkIndiceLista.checked) {
					if (confirm("Está seguro de eliminar el registro seleccionado?")) {
						fxDeleteRow(tabla, chkIndiceLista);
					}
				}
			}
			else {
				for(i = numFilas - 1 ; i >= 0; i--) {
					fxDeleteRow(tabla, chkIndiceLista[i]);
				}
			}
			fxReAsignIndexForTable();
		} catch(exception) {
			alert("No hay elementos para eliminar en La Lista de Préstamos & Cambios");
		}
	}
	
	function fxDeleteRow(tabla, chkIndiceLista) {
		if(chkIndiceLista.checked) {
			tabla.deleteRow(chkIndiceLista.parentNode.parentNode.rowIndex);
		}
	}
	
	function fxReAsignIndexForTable() {
		var tabla = document.getElementById("tblListaPrestamosCambios");
		cantFils = tabla.rows.length;
		for(i = 1; i < cantFils; i++) {
			nuevaColumna = tabla.rows[i].cells[1];
			nuevoTexto = fxGetContentChangeLoanList(1, i);
			nuevaColumna.innerHTML = nuevoTexto;
		}
	}
	
	function fxCheckAllList() {
		vForm = document.frmdatos;
		try{
			chkIndiceLista = vForm.chkIndiceLista;
			chkTodoLista = vForm.chkTodoLista;
			if(chkIndiceLista.length == undefined) {
				chkIndiceLista.checked = chkTodoLista.checked;
			}
			else {
				for(i = 0 ; i < chkIndiceLista.length; i++) {
					chkIndiceLista[i].checked = chkTodoLista.checked;					
				}
			}
		} catch(exception) {

		}
	}
	
	function fxLoadNextelDiagnostico() {
		var vForm = document.frmdatos;
		var tipoDiagnostico = vForm.cmbDiagnostico.options[vForm.cmbDiagnostico.selectedIndex].value;
      
      alert("tipoDiagnostico ==>" + tipoDiagnostico);
      
		var url = "/portal/pls/portal/WEBCCARE.NPAC_SERVICIOTEC_ORI_PL_PKG.PL_CARGA_NEXTELDIAGNOSTICO?v_tipoDiagnostico=" +tipoDiagnostico;    
		
      alert("url ==>" + url);
      
      parent.bottomFrame.location.replace(url);
	}
   
	
	function fxSectionRepairOnLoad() {   
		return true;
	}

  function contentInArrayStrings(value){
  var arreglo = <%=Constante.SPEC_LOAD_CMB_PROCESS%>;
    for(var i=0;i<arreglo.length;i++){
      if ( value==arreglo[i]){
         return true;         
      }
    }
    return false;    
 } 
	
	function fxChangeProcess() {
		vForm = document.frmdatos;
		var form = document.frmdatos;
		var vTechnology = form.hdnTechnology.value;
    cmbProceso = vForm.cmbProceso.value;
		//dvListaPrestamoCambio.style.display="none";
    
      fxChangeProcessRepairList();	
      fxChangeProcessTimer();
                                          
   				
		if ("ACC" == cmbProceso) {
			//dvBySpecTipoAccesorioName.style.display="";
			//dvBySpecModelAccesorioName.style.display="";  
			//dvBySpecSemFrabicName.style.display="";   
			dvBySpecTipoAccesorio.style.display="";
			dvBySpecModelAccesorio.style.display="";  
			dvBySpecSemFrabic.style.display="";
         dvBySpecSemFrabicRule.style.display=""; 
         
         dvBySpecFechaVenta.style.display="";           
         dvBySpecFechaVent.style.display="";           
         dvBySpecFechaV.style.display="";  
         
		}
		else {    
    
			//dvBySpecTipoAccesorioName.style.display="none";
			//dvBySpecModelAccesorioName.style.display="none";  
			//dvBySpecSemFrabicName.style.display="none";   
			dvBySpecTipoAccesorio.style.display="none";
			dvBySpecModelAccesorio.style.display="none";  
			dvBySpecSemFrabic.style.display="none";
         dvBySpecSemFrabicRule.style.display="none"; 
         
         dvBySpecFechaVenta.style.display="none";           
         dvBySpecFechaVent.style.display="none";           
         dvBySpecFechaV.style.display="none"; 
         
         document.getElementById('tr_accesorios').style.display='none';
		}
	}

	<% if (Constante.SPEC_SERVCIO_TECNICO_ACCESORIOS == iSpecificationId){%>
		dvBySpecTipoAccesorioName.style.display="";   
		dvBySpecModelAccesorioName.style.display="";  
		dvBySpecSemFrabicName.style.display="";        
		dvBySpecTipoAccesorio.style.display="";  
		dvBySpecModelAccesorio.style.display="";  
		dvBySpecSemFrabic.style.display="";
                document.getElementById('tr_accesorios').style.display='';
	<%}%>	   
  
   function fxChangeProcessTimer(){
      vForm = document.frmdatos;
      cmbProceso = vForm.cmbProceso.value;
      if ("STP" == cmbProceso){
         dvBySpecLabelTimerValue.style.display="";
         dvBySpectxtTimerValue.style.display="";     
      } else{
         dvBySpecLabelTimerValue.style.display="none";
         dvBySpectxtTimerValue.style.display="none";      
      }
   }    
       
   
  function fxChangeProcessRepairList(){
      vForm = document.frmdatos;
		  cmbProceso = vForm.cmbProceso.value;
      /*if ("REP" == cmbProceso){
          //alert("pinta");          
          if (vForm.txtImeiLista != undefined){
              dvListaPrestamoCambio.style.display="";
          }
          dvBySpeclstFalla.style.display="";
          dvBySpeclstFallaResultado.style.display="";
          dvBySpecbtnNuevo.style.display="";
          dvBySpecbtnUsado.style.display="";
          dvBySpecbtn3.style.display="";          
          dvBySpecchkReparacionSinCosto.style.display="";
          dvBySpectxtTotalRepuesto.style.display="";
          dvBySpectxtSectionTitleLeftCurve.style.display="";
          dvBySpectxtSectionTitle.style.display="";
          dvBySpectxtSectionTitleRightCurve.style.display="";
          dvBySpecTableCurves.style.display="";           
          dvBySpecTableBlock.style.display="";   
          
          
      } else{
          //alert("no pinta");
          dvBySpeclstFalla.style.display="none";
          dvBySpeclstFallaResultado.style.display="none";
          dvBySpecbtnNuevo.style.display="none";
          dvBySpecbtnUsado.style.display="none";
          dvBySpecbtn3.style.display="none";
          dvBySpecchkReparacionSinCosto.style.display="none";
          dvBySpectxtTotalRepuesto.style.display="none";
          dvBySpectxtSectionTitleRightCurve.style.display="none";
          dvBySpectxtSectionTitle.style.display="none";
          dvBySpectxtSectionTitleRightCurve.style.display="none";         
          dvBySpecTableCurves.style.display="none";          
          dvBySpecTableBlock.style.display="none";          
          
      }      */
  }
  
	function fxImprimirFormato() {
		var form = document.frmdatos;
		var hdnRepairId = form.hdnRepairId.value;
		if (hdnRepairId != "") {
			var url = "<%=request.getContextPath()%>/reports/reportReparaciones.jsp?hdnRepairId="+hdnRepairId+"&iIsTiendaExpress="+<%=iIsTiendaExpress%>;
			ImprimirReporte = window.open(url,"ImprimirReporte","toolbar=no,location=0,directories=no,status=yes,menubar=1,scrollbars=yes,resizable=yes,screenX=100,top=80,left=150,screenY=80,width=770,height=500,modal=yes");
		}
	}
        
        function fxImprimirFormato2() {
		var form = document.frmdatos;
		var hdnRepairId = form.hdnRepairId.value;
		if (hdnRepairId != "") {
			var url = "<%=request.getContextPath()%>/reports/reportReparaciones2.jsp?hdnRepairId="+hdnRepairId+"&iIsTiendaExpress="+<%=iIsTiendaExpress%>;
			ImprimirReporte = window.open(url,"ImprimirReporte2","toolbar=no,location=0,directories=no,status=yes,menubar=1,scrollbars=yes,resizable=yes,screenX=100,top=80,left=150,screenY=80,width=700,height=500,modal=yes");
		}
	}

    //JVERGARA VIDD
    function fxIsolatedVerification(){
        var form = document.frmdatos;
        var check = form.hdnChkAssignee.value;
        var numAssig = "";
        var doctype = "";
        if(check!='') {
            numAssig = form.txtDocNumAssignee.value;
            doctype = form.hdnCmbDocTypeAssigneeText.value;
        }

        $.ajax({
            url:"<%=actionURL_DigitalDocumentServlet%>",
            type:"GET",
            async:false,
            cache:false,
            data:{
                "myaction":"validateVIATechnicalService",
                "strOrderId" :<%=strOrderId%>,
                "lCustomerId":<%=lCustomerId%>,
                "strLogin":<%=strLogin%>,
                "hdnChkAssignee":check,
                "strAssigneeDocNum":numAssig,
                "cmbDocTypeAssignee":doctype
                },
                dataType:"json",
                success:function(data){
                    var message=data.strMessage||'';
                    var result = data.resultado;
                    var resultmessage = data.messagealert||'';
                    if(message==''){
                        form.hdnVIAStatus.value=result;
                        form.hdnVIAMessage.value=resultmessage;
                    }else{
                        form.hdnVIAStatus.value="";
                        form.hdnVIAMessage.value="";
                        alert(message);
                    }

                },
                error:function(xhr,status,error) {
                    alert(xhr.responseText);
                }
            });

    }



    function fxGenerateInvDoc(){

      var vForm = document.frmdatos;

      repair                   = vForm.hndReparacionId.value;
      strRepairTypeId          = "OA";
      strhdnLogin              = vForm.hdnLogin.value;
                  
      strhdnDocumentParentType = vForm.hdnDocumentParentType.value;
      strhdnTransferReason     = vForm.hdnTransferReason.value;
      
      strhdnItemTypeId         = null;//vForm.hdnItemTypeId.value;
      strhdnOrganizationId     = null;//vForm.hdnOrganizationId.value;
      strhdnSubInventoryCode   = null;//vForm.hdnSubInventoryCode.value;

      var url = "<%=request.getContextPath()%>/repairServlet?hdnMethod=GenerateInvDoc"
                    +"&txtNroReparacion="+repair
                    +"&strRepairTypeId="+strRepairTypeId
                    +"&hdnLogin="+strhdnLogin
                    +"&hdnDocumentParentType="+strhdnDocumentParentType
                    +"&hdnTransferReason="+strhdnTransferReason
                    +"&hdnItemTypeId="+strhdnItemTypeId
                    +"&hdnOrganizationId="+strhdnOrganizationId
                    +"&hdnSubInventoryCode="+strhdnSubInventoryCode;

      parent.bottomFrame.location.replace(url);
  }

 
   
	
	function fxTieneDocumentos(){	
		var tblListaPrestamosCambios = document.getElementById("tblListaPrestamosCambios");
      var bolTieneDoc =false;
		var i;
		if (tblListaPrestamosCambios != null){
			var numFilas = tblListaPrestamosCambios.rows.length;
			
			if (numFilas==2){
				if (document.frmdatos.chkCrearDocLista.disabled){
					//alert("La orden ya tiene documentos generados, no se puede cambiar el proceso");					
					document.frmdatos.hdnTipoProceso.value=document.frmdatos.cmbProceso.value;
					document.frmdatos.cmbProceso.disabled=true;
					return;
				}
			}
			else{
				if (numFilas!=1){
					for (i=1; i < numFilas; i++){
						if (document.frmdatos.chkCrearDocLista[i]!=null){
							if (document.frmdatos.chkCrearDocLista[i].disabled){
								bolTieneDoc=true;
								break;
							} //validar si esta desactivado = ya tiene generado el documento
						}	//validar si existe el control
					};// fin del for
					if (bolTieneDoc){
						//alert("La orden ya tiene documentos generados, no se puede cambiar el proceso");						
						document.frmdatos.hdnTipoProceso.value=document.frmdatos.cmbProceso.value;
						document.frmdatos.cmbProceso.disabled=true;
						return;
					}
				}
			}
		}// fin tblListaPrestamosCambios != null				
	}
   
     
  //23/03/2009  
   function fxloadRepairListDetails(){
      intFailureId = vForm.cmbTipoFalla.value;
      intRepairId = vForm.hndReparacionId.value;      
      strModelo = vForm.txtModelo.value;      
      strRepairTypeId = 'OA';
            
      var url = "<%=request.getContextPath()%>/repairServlet?hdnMethod=getRepairListDetails&intFailureId="+intFailureId+"&intRepairId="+intRepairId+"&strModelo="+strModelo+"&strRepairTypeId="+strRepairTypeId;
      parent.bottomFrame.location.replace(url);
    }



	function fxValidaImeiListaPrestamoCambio(){	
		var tblListaPrestamosCambios = document.getElementById("tblListaPrestamosCambios");
		var imeiCliente=document.frmdatos.txtImei.value;
		
		if (tblListaPrestamosCambios != null){
			var numFilas = tblListaPrestamosCambios.rows.length;
			
			if (numFilas==2){
				if (document.frmdatos.cmbTipoImeiLista.value=='DEVOLUCION'){
					if (document.frmdatos.txtImeiLista.value != imeiCliente){
						alert("El imei "+document.frmdatos.txtImeiLista.value +" ingresado para "+ document.frmdatos.cmbTipoImeiLista.value+" no corresponde al imei del cliente");
						document.frmdatos.txtImeiLista.focus();
						document.frmdatos.txtImeiLista.select();
						return false;
					}
				}
				else{
					if (document.frmdatos.cmbTipoImeiLista.value=='CAMBIO' || document.frmdatos.cmbTipoImeiLista.value=='PRESTAMO'){
						if (document.frmdatos.txtImeiLista.value == imeiCliente){
							alert("El imei "+document.frmdatos.txtImeiLista.value +" ingresado para "+ document.frmdatos.cmbTipoImeiLista.value +" corresponde al imei del cliente");
							document.frmdatos.txtImeiLista.focus();
							document.frmdatos.txtImeiLista.select();
							return false;
						}
					}				
				}
			}
			else{				
				if (numFilas!=1){
					//Numero de combos con el tipo de Imei a recorrer
					numFilasTipoImeiLista=document.frmdatos.cmbTipoImeiLista.length;
					for (i=1; i < numFilasTipoImeiLista; i++){
						if (document.frmdatos.cmbTipoImeiLista[i].value=='DEVOLUCION'){
							if (document.frmdatos.txtImeiLista[i].value != imeiCliente){
								alert("El imei "+document.frmdatos.txtImeiLista[i].value +" ingresado para "+ document.frmdatos.cmbTipoImeiLista[i].value +" no corresponde al imei del cliente");
								document.frmdatos.txtImeiLista[i].focus();
								document.frmdatos.txtImeiLista[i].select();
								return false;
							}
						}
						else{
							if (document.frmdatos.cmbTipoImeiLista[i].value=='CAMBIO' || document.frmdatos.cmbTipoImeiLista[i].value=='PRESTAMO'){
								if (document.frmdatos.txtImeiLista[i].value == imeiCliente){
									alert("El imei "+document.frmdatos.txtImeiLista[i].value +" ingresado para "+ document.frmdatos.cmbTipoImeiLista[i].value +" corresponde al imei del cliente");
									document.frmdatos.txtImeiLista[i].focus();
									document.frmdatos.txtImeiLista[i].select();
									return false;
								}
							}												
						}
					};// fin del for
				}
			}
		}// fin tblListaPrestamosCambios != null
		return true;
	}

   function fxGenerateRepair() {
               
		vForm = document.frmdatos;		                     
      cmbProceso = vForm.cmbProceso.value;    // Proceso      
      FechaVenta = vForm.txtFechaVenta.value; // Fecha
      SemFab = vForm.txtSemFabricacion.value // Semana Fabricación     
      vForm.cmbSituacion.value;
      //cmbModeloSOS = "<%=strModeloSOS%>";
      //alert(cmbModelo);
      vForm.btnEmitirFormato.disabled = true; //ocruces - N_O000044181 Ordenes de reparación se están duplicando
      if (cmbProceso == "ACC"){      
         //if (FechaVenta == "" ){
         if (SemFab == "" ){
            alert ("Debe Ingresar la Semana de Fabricación!");            
            vForm.btnEmitirFormato.disabled = false;  //ocruces - N_O000044181 Ordenes de reparación se están duplicando
            vForm.txtSemFabricacion.focus();                     
            return false;                
         }else{
            vForm.target = "bottomFrame";      
            vForm.action = "<%=request.getContextPath()%>/repairServlet?hdnMethod=newOrderReparation";
            vForm.submit();
         }
      }else if(cmbProceso == "REX"){
        vForm.btnEmitirFormato.disabled = true;// mespinoza - PM0010472 Orden de REX no permite crear orden externa
        return false;
      }else if(cmbProceso == "REP") {
            vForm.target = "bottomFrame";
            vForm.action = "<%=request.getContextPath()%>/repairServlet?hdnMethod=newOrderReparation";
            vForm.submit();
      }
      else
      {
         vForm.target = "bottomFrame";      
         vForm.action = "<%=request.getContextPath()%>/repairServlet?hdnMethod=newOrderReparation";
         vForm.submit();   
         }            		
   }
   
   function fxGenerateTrueBounce() {   
      vForm = document.frmdatos;
      imei = vForm.txtImei.value;      
      bounce= vForm.hdnGarantiaBounce.value;   
      falla = document.frmdatos.lstFalla.value;              
            
      var url = "<%=request.getContextPath()%>/repairServlet?hdnMethod=validateTrueBounce&txtImei="+imei+"&falla="+falla+"&bounce="+bounce;      
      parent.bottomFrame.location.replace(url);         
         
	}
   
   function fxValidateSpecProcess() {   
  
      vForm = document.frmdatos;
      
      var fono = vForm.txtNextel.value;
      var new_process = vForm.cmbProceso.value;
      var order_id = "<%=lOrderId%>";
      var old_process="<%=objRepairBean.getNprepairtype()%>";
      
      //alert(fono);
      //alert(new_process);
      //alert(old_process);
      
      var url = "<%=request.getContextPath()%>/repairServlet?hdnMethod=validateSpecProcess&strPhoneNumber="+fono+
                                                                                          "&strProcess="+new_process+
                                                                                          "&strOldProcess="+old_process+
                                                                                          "&strOrderID="+order_id;
      //alert(url);
      parent.bottomFrame.location.replace(url);
	}

   function fxValStock() {   
      vForm = document.frmdatos;      
      repuesto = document.frmdatos.lstRepuestos.value;              
      buildingid = document.frmdatos.hdnBuildingId.value;
                                            
      var url = "<%=request.getContextPath()%>/repairServlet?hdnMethod=validateStock&repuesto="+repuesto+"&specification="+ "<%=iSpecificationId%>" + "&hdnlogin="+buildingid;                  
      parent.bottomFrame.location.replace(url);                  
	}
         
   function fxRepair() {                                                                                                    
      vForm = document.frmdatos;
      imei = vForm.txtImei.value;       
      repair = vForm.hndReparacionId.value;
      
      <%
      hshRepairMap = objRepairService.getRepairid( strImei );      
      String intReparacion= /*MiUtil.parseLong(*/(String)hshRepairMap.get("intRepairid")/*)*/;                     
      %>
                                
      if (<%=intReparacion%> ==0) {  
               
         url = "<%=request.getContextPath()%>/repairServlet?hdnMethod=Busqueda&strRepairId="+"<%=intReparacion%>"+"&hdnlogin="+"<%=strLogin%>";     
         parent.bottomFrame.location.replace(url);                        
         
      }else { 
         /*url = "<%=request.getContextPath()%>/repairServlet?hdnMethod=Busqueda&strRepairId="+"<%=intReparacion%>"+"&hdnlogin="+"<%=strLogin%>";
         parent.mainFrame.location.replace(url); */
                        
         var a = "/portal/page/portal/repair/REPAIR_NEW?strRepairId="+ "<%=intReparacion%>" +  "&strLogin=" + "<%=strLogin%>";                                    
         var winUrl = "<%=strRutaContext%>/htdocs/jp_repair_new/PopUpOrderFrame.jsp?av_url="+escape(a);
         var popupWin = window.open(winUrl, "Orden_Item_Assignment","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,screenX=60,top=40,left=60,screenY=40,width=850,height=600");                                                      
      }        
   }      
               
   function fxActivateEquipment() { 
      vForm = document.frmdatos;
      imei = vForm.txtImei.value;                 
      var Activation      
      activarEquipoValor = ""; 
      imeiNuevo = "";
      if (vForm.chkActivarEquipo.length==undefined) {
         activarEquipoValor = vForm.chkActivarEquipo.value;         
         imeiNuevo = vForm.txtImeiLista.value;         
      } else {
         for(i=0;i<vForm.chkActivarEquipo.length;i++) {
            if(vForm.chkActivarEquipo[i].checked) {
               activarEquipoValor = vForm.chkActivarEquipo[i].value;
               imeiNuevo   = vForm.txtImeiLista[i].value;
            }
         }
      }                        					
	      
      if (activarEquipoValor == "GR"){              
         var url = "<%=request.getContextPath()%>/repairServlet?hdnMethod=activateEquipment&txtImei="+imei+"&imeiNuevo="+imeiNuevo;      
         parent.bottomFrame.location.replace(url);                      
      }
         
      if (activarEquipoValor == "PI"){        
         alert("Este equipo se encuentra en los almacenes!");
         return false;                  
		}
      
      if (activarEquipoValor == "on"){           
         alert("Se necesita generar documento!");
         return false;         
      }
               
   }
     
   function fxValidateFabricationDate(){
      vForm = document.frmdatos;
      strFabricationDate = vForm.txtSemFabricacion.value;
     
      if (strFabricationDate.length<8) {
         alert("Debe ingresar un mínimo de 8 caracteres");
         vForm.txtSemFabricacion.value="";      
         vForm.txtSemFabricacion.focus(); 
      }
   }

  function fxFillSparePartList(){
    vForm = document.frmdatos;
    strModelo = vForm.txtModelo.value;

    //deleteAllValues(document.frmdatos.lstRepuestos);
    //deleteAllValues(document.frmdatos.lstRepuestosResultado);
    
    if (strModelo!=""){
      var url = "<%=request.getContextPath()%>/repairServlet?hdnMethod=getSparePartsListByModel&strModelo="+strModelo;
      parent.bottomFrame.location.replace(url);
    }
  }
 
  function fxValidateTechnologyIDEN(){//MESPINOZA
   vForm = document.frmdatos;
       if(vForm.cmbProceso.value == "REX"){
         vForm.btnEmitirFormato.disabled = true;//PM0010472- Orden de REX permite crear orden externa
       }
   }

  fxFillSparePartList(); 	
  fxChangeProcess();
  fxTieneDocumentos();
  fxValidateTechnologyIDEN(); //-- MESPINOZA -- Valida si la tecnologia es IDEN, y si el proceso es REX
   
  //Carga el detalle de las listas
  fxloadRepairListDetails();
  
  function fxLoadComboAccesoryModels(){
   vForm = document.frmdatos;
   strAccesoryType= vForm.cmbTipoAccesorio.value;
   //alert("Padre : "+strAccesoryType);
   
   if(strAccesoryType!=""){
      var url = "<%=request.getContextPath()%>/repairServlet?hdnMethod=getComboAccessoryModels&cmbTipoAccesorio="+strAccesoryType;
      parent.bottomFrame.location.replace(url);
    }else{
      DeleteSelectOptions(document.frmdatos.cmbModeloAccesorio);
    }
  }
  	function fxCheckImei()	{
      vForm = document.frmdatos;
      try{
        vForm.chkSim.checked=false;
        vForm.chkImeiSim.checked=false;
        vForm.hdnReplaceType.value = "1";
        //alert("hdnReplaceType: "+vForm.hdnReplaceType.value);
      }catch(exception){
      }
  }           
	function fxCheckSim()	{
      vForm = document.frmdatos;
      try{
        vForm.chkImei.checked=false;
        vForm.chkImeiSim.checked=false;
        vForm.hdnReplaceType.value = "2";
        //alert("hdnReplaceType: "+vForm.hdnReplaceType.value);
      }catch(exception){
      }
  }    
	function fxCheckImeiSim()	{
      vForm = document.frmdatos;
      try{
        vForm.chkImei.checked=false;
        vForm.chkSim.checked=false;
        vForm.hdnReplaceType.value = "3";
        //alert("hdnReplaceType: "+vForm.hdnReplaceType.value);
      }catch(exception){
      }
  }



</script>
<%
} catch (Exception e) {
    e.printStackTrace();
    System.out.println("Mensaje::::" + e.getMessage() + "<br>");
    System.out.println("Causa::::" + e.getCause() + "<br>");
    for (int i = 0; i < e.getStackTrace().length; i++) {
        System.out.println("    " + e.getStackTrace()[i] + "<br>");
	}
}%>



