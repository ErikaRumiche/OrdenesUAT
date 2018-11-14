<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.service.SessionService" %>
<%@ page import="java.util.List" %>
<%@ page import="pe.com.nextel.bean.*" %>
<%

/*Recuperando Parametros de Input*/
   NewOrderService objNewOrderService = new NewOrderService();
   GeneralService objGeneralService = new GeneralService();
   Hashtable hshtinputNewSection = null;
   //HashMap hServiceList = null;
   String    strCodigoCliente= "",
             hdnSpecification = "";
   int iResultado = 0;

   hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
   
   if ( hshtinputNewSection != null ) {
     strCodigoCliente        =   (String)hshtinputNewSection.get("strCodigoCliente");
     hdnSpecification        =   (String)hshtinputNewSection.get("hdnSpecification");
     request.setAttribute("hshtInputNewSection",hshtinputNewSection);
   }
   
    HashMap oHashMap=objGeneralService.getDataNpTable("VALIDATE_SERVICE_CORE",hdnSpecification);
		ArrayList     objArrayList1 = (ArrayList)oHashMap.get("objArrayList");
		System.out.println("hdnSpecification"+hdnSpecification);
		System.out.println("objArrayList.size()"+objArrayList1.size());
		if (objArrayList1.size()!=0){
			iResultado=1;
		}
   
   String nameHtmlItem = (String)request.getParameter("nameObjectHtml");
   if ( nameHtmlItem==null ) nameHtmlItem = "";
   
   ArrayList objArrayList        = new ArrayList();
   ArrayList objArrayListDefault = new ArrayList();
   String    strMessage = null;
   String    strObjectId = "FFPEDIDOS";
   
   String strPlan = "";
   String strSolution = "";
   String strType = null;
   String strPlanAux = "";
   String strModel = "";
   String strSSAAType  = null;//johncmb
   
   
   String[] paramNpobjitemvalue      = request.getParameterValues("b");
   String[] paramNpobjitemheaderid   = request.getParameterValues("a");
   if(paramNpobjitemheaderid != null){
      for(int i=0;i<paramNpobjitemheaderid.length; i++){
      // Plan Tarifario
         if( paramNpobjitemheaderid[i].equals("10") )
         strPlan = paramNpobjitemvalue[i];  
      // Solución
         if( paramNpobjitemheaderid[i].equals("93") )
         strSolution = paramNpobjitemvalue[i];  
      //Plan tarifario origianlid:ayuda a cargar los servicios core de este plan 
         if( paramNpobjitemheaderid[i].equals("57") )
         strPlanAux = paramNpobjitemvalue[i]; 
      //Modelo
         if( paramNpobjitemheaderid[i].equals("15") ) //DLAZO 
         { 
           strModel = paramNpobjitemvalue[i]; 
         } 
        if( paramNpobjitemheaderid[i].equals("9")&& !(MiUtil.parseInt(hdnSpecification) == Constante.SPEC_TRANSFERENCIA)  ) //DLAZO //EZM Compatibilidad Modelo-Plan-Servicio
         { 
           strModel = paramNpobjitemvalue[i]; 
         }          
         
      } 
      
  }
  
  System.out.println("[objServiceAditional][strPlan]"+strPlan);
  System.out.println("[objServiceAditional][strPlanAux]"+strPlanAux);
  System.out.println("[strSolution********************][strSolution]"+strSolution);
  if (MiUtil.parseInt(hdnSpecification)==Constante.KN_ACT_CAMB_PLAN_BA || MiUtil.parseInt(hdnSpecification)==Constante.KN_ACT_DES_SERVICIOS_BA){
     strType = "A";
  }
  
  //DLAZO - PRUEBA DE FILTRADO DE SUSCRIPCIONES (Cambio de Parametros)
  //johncmb inicio 
  if( MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA ||
      MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_NUEVA || 
      MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_TDE ||
      MiUtil.parseInt(hdnSpecification) == Constante.SPEC_REPOSICION_PREPAGO_TDE ||//Se agrega reposicion prepago tde - TDECONV034
      MiUtil.parseInt(hdnSpecification) == Constante.SPEC_SSAA_SUSCRIPCIONES || 
      MiUtil.parseInt(hdnSpecification) == Constante.SPEC_SSAA_PROMOTIONS 
  ) {
     strSSAAType  = "0,1,2,3,5"; }
  else{
     strSSAAType  = "0,1,5"; 
  }   
  
  if(strPlan != null && !("".equals(strPlan)) ){
    System.out.println("[objServiceAditional][strMessage][strPlan !=null]"+strPlan);
    HashMap hServiceList  = objGeneralService.getServiceList(MiUtil.parseInt(strSolution), MiUtil.parseInt(strPlan), MiUtil.parseInt(strModel), strSSAAType, strType);
    System.out.println("[objServiceAditional][strMessage]"+strMessage);
    strMessage = (String)hServiceList.get("strMessage");          
    System.out.println("[objArrayList.size***********]"+objArrayList.size());
    objArrayList =(ArrayList)hServiceList.get("objServiceList"); 
  }else{
    HashMap hServiceList  = objGeneralService.getServiceList(MiUtil.parseInt(strSolution), MiUtil.parseInt(strPlanAux), MiUtil.parseInt(strModel), strSSAAType, strType);
    System.out.println("[objServiceAditional][strMessage]"+strMessage);
    strMessage = (String)hServiceList.get("strMessage");
    System.out.println("[objArrayList.size***********]"+objArrayList.size());
  objArrayList =(ArrayList)hServiceList.get("objServiceList");  
  }  
  
  HashMap hNpTableList  = objGeneralService.getValueNpTable("SOLUTION_NON_COMPATIBILITY");
  ArrayList objNpTableList =(ArrayList)hNpTableList.get("objArrayList");
  boolean isSolNonComp = false;
  TableBean nptableBean = null;
  for (int k=0;k<objNpTableList.size();k++) {
    nptableBean = (TableBean)objNpTableList.get(k);
    if (nptableBean.getNpValue().equals(strSolution)) {
      isSolNonComp = true;
      break;
    }
  }
  if (isSolNonComp && objArrayList.size()==0) {
     HashMap hServiceList  = objGeneralService.getServiceList(MiUtil.parseInt(strSolution), 0, 0, strSSAAType, strType);
     System.out.println("[objServiceAditional isSolNonComp][strMessage]"+strMessage);
     strMessage = (String)hServiceList.get("strMessage");
     System.out.println("[objArrayList.size isSolNonComp ***********]"+objArrayList.size());
     objArrayList =(ArrayList)hServiceList.get("objServiceList"); 
  }
  //johncmb fin   
  //objArrayList          = objNewOrderService.ServiceDAOgetServiceAllList(MiUtil.parseInt(strSolution),strMessage);
  objArrayListDefault   = objNewOrderService.ServiceDAOgetServiceDefaultList(strObjectId,MiUtil.parseInt(hdnSpecification),strMessage);

  //EFLORES [PRY-1112] Agrega servicio adicional para filtrar
    int iUserId = 0;
    int iAppId = 0;
    String strSessionId       = (String)request.getParameter("strSessionId");
    PortalSessionBean objSessionBean = (PortalSessionBean) SessionService.getUserSession(strSessionId);
    iUserId = objSessionBean.getUserid();
    iAppId  = objSessionBean.getAppId();
    System.out.println("[PRY-1112][objServiceAditional] strSessionId "+strSessionId);
    System.out.println("[PRY-1112][objServiceAditional] iUserId "+iUserId);
    HashMap hExcludingServiceList = objGeneralService.getExcludingAditionalServices();
    List<ExcludingServiceBean> lstExcludingServices = (List<ExcludingServiceBean>) hExcludingServiceList.get("objServiceList");
    //FIN EFLORES

  String wv_item_services = request.getParameter("wv_item_services")==null?"":(String)request.getParameter("wv_item_services");
  
  HashMap hpCategoryCambioModeloId = objGeneralService.getValueByConfiguration("SPECIFICATION_SERVICE_MSJ");
   
   String strCategoryCambioModeloId = "2009";
   
   if(hpCategoryCambioModeloId != null) {
    
     if(hpCategoryCambioModeloId.get("objConfigurationBean") != null) {
     
     ConfigurationBean configurationBean = (ConfigurationBean)hpCategoryCambioModeloId.get("objConfigurationBean");
     
     strCategoryCambioModeloId = configurationBean.getNpValueDesc();
    
    }
    
   }
  
%>
<script>
var wv_item_services = "<%=wv_item_services%>";
</script>
<td  class="" colspan="2"  align="left">

         <!--Linea de separación -->
         <table><tr align="center"><td></td></tr></table>
         <div id="services">
            
            <!-- Servicios Disponibles y Seleccionados -->
            <div id="tableServiciosAdicionales">
            <table border="0" cellspacing="0" cellpadding="0" id="tbCabeceraServiciosAdicionales">
              <tr class="PortletHeaderColor">
                 <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="16"></td>
                 <td class="SubSectionTitle" align="left" valign="top">Servicios Adicionales</td>
                 <td class="SubSectionTitleRightCurve" valign="top" align="right" width="12"></td>
              </tr>
            </table>
            <table border="1" width="100%" cellpadding="2" cellspacing="0" class="RegionBorder">
               <tr align="center">
                  <td class="RegionHeaderColor" valign="top">
                     <table border="0" cellspacing="0" cellpadding="0" width="100%">
                        <tr >
                           <td class="CellLabel" align="center">Disponibles</td>
                           <td class="CellLabel"></td>
                           <td class="CellLabel" align="center">Seleccionados</td>
                           <td class="CellLabel"> <div id="dServiceDetail"></div>
                           </td>
                        </tr>
                        <tr>
                           <td class="CellLabel" width="25%">&nbsp;</td>
                           <td class="CellLabel" width="25%">&nbsp;</td>
                           <td class="CellLabel" width="50%" colspan="2">
                           <table width="100%" border="0">
                             <tr class="CellLabel" align="center">
                                <td class="CellLabel" align="center">
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Servicio&nbsp;&nbsp;&nbsp;&nbsp;
                                </td>
                                <td class="CellLabel" align="right" colspan="4">
                                &nbsp;|&nbsp;Act.&nbsp;|&nbsp;Modf.&nbsp;
                                </td>
                              </tr>
                           </table>
                           </td>
                        </tr>
                           <!--<td class="CellLabel" align="right"  colspan="30">
                              &nbsp;&nbsp;&nbsp;&nbsp;Servicio&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;Act.&nbsp;|&nbsp;Modf.&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                           </td>-->
                        </tr>
                        <tr>
                           <td class="CellContent" align="center">
                              <select name="cmbAvailableServices" size="10" multiple ondblclick="javascript:fxAddService();">
                              </select>
                              <!--<input type="hidden" value="" name="hdnItemServicesAditional">-->
                              <input type="hidden" value="" name="item_services">
                           </td>
                           <td class="CellContent" align="center">
                              <input type="button" value="&gt;&gt;" name="baddService" onclick="javascript:fxAddService();">
                              <input type="hidden" name="hdnFlagServicio" value="0"> <!-- Almacena el Flag de modificacion de los Servicios de Items -->
                              <br>
                           </td>                                                            
                           <td class="CellContent" align="center" colspan="2">
                                <select name="cmbSelectedServices" size="10" ondblclick="javascript:fxEliminaServicio(this);"  multiple style="font-family: Courier New; font-size: 9 pt;" >
                              </select>
                           </td>
                        </tr>
                     </table>
                  </td>
               </tr>
            </table>
            </div>
         </div>   <!-- de " div Servicios" -->    

</td>
         
<script>
   //Vector de Servicios
   var vServicio            = new Vector();
   // Vector de Servicios por Defecto
   var vServicioPorDefecto  = new Vector();
  //Vector de Servicios por Defecto de Arrendamiento
   var vServicioArren    = new Vector();
   // Vector de Servicios Comerciales Core 
   var vServiciosComercialesCore = new Vector();

   //EFLORES PRY-1112
   var vExcludingServices = new Vector();

   var servId               = new Array();
   var servName             = new Array();
   var servExcluding        = new Array();
   var receivedServID       = new Array();
   
   var servIndex                = 0;
   var alquilerServicesSelected = 0;
   var rentServicesSelected     = 0;
   var rentServicesSelectedEnd  = 0;
   var alquilerServicesSelected1 = 0;
   var alquilerServicesSelected2 = 0;
   var alquilerServicesSelected3 = 0;
   
   var longMaxServices          = 0;
   
   function Servicio(id, name ,nameShort ,exclude) {        
        this.id = id;
        this.name = name;
        this.nameDisplay = (exclude=="")?name:name+" - "+exclude;
        this.nameShort = nameShort;
        this.nameShortDisplay = (exclude=="")?nameShort:nameShort+" - "+exclude;
        this.exclude = exclude;
        this.active_new = "N";
        this.modify_new = "N";        
   }

   //EFLORES PRY-1112
   function ExcludingService(sPrincipal,sExcluded,sMessage){
       this.sPrincipal = sPrincipal;
       this.sExcluded = sExcluded;
       this.sMessage = sMessage;
   }
   //FIN EFLORES PRY-1112

   function fxLoadServiceAditiional(){       

   <%if ( objArrayList != null && objArrayList.size() > 0 ){
      ServiciosBean serviciosBean = null;
      serviciosBean = (ServiciosBean)objArrayList.get(0);
       int longMaxServices = MiUtil.getString(serviciosBean.getNpnomserv()).length();
       for( int j = 1; j < objArrayList.size(); j++ ){
         serviciosBean = (ServiciosBean)objArrayList.get(j);
          if( MiUtil.getString(serviciosBean.getNpnomserv()).length() > longMaxServices )
            longMaxServices = MiUtil.getString(serviciosBean.getNpnomserv()).length();
       }
       
       %>
      longMaxServices = '30<%//=longMaxServices%>';
       <%
       
       for( int i = 0; i < objArrayList.size(); i++ ){
        
          serviciosBean = (ServiciosBean)objArrayList.get(i);
       /*   System.out.println("[fxLoadServiceAditiional][getNpservicioid]"+serviciosBean.getNpservicioid());
          System.out.println("[fxLoadServiceAditiional][getNpnomserv]"+serviciosBean.getNpnomserv());
          System.out.println("[fxLoadServiceAditiional][getNpnomcorserv]"+serviciosBean.getNpnomcorserv());
          System.out.println("[fxLoadServiceAditiional][getNpexcludingind]"+serviciosBean.getNpexcludingind());*/

          %>
       vServicio.addElement(new Servicio("<%=serviciosBean.getNpservicioid()%>","<%=serviciosBean.getNpnomserv()%>","<%=serviciosBean.getNpnomcorserv()%>","<%=MiUtil.getString(serviciosBean.getNpexcludingind())%>"));
       <%}
     }
     
    %>
   <%if( objArrayListDefault != null && objArrayListDefault.size() > 0 ) {
       ServiciosBean serviciosBeanDefault = null;
       for( int j = 0; j < objArrayListDefault.size(); j++ ){
          serviciosBeanDefault = (ServiciosBean)objArrayListDefault.get(j);%>
        vServicioPorDefecto.addElement(new Servicio("<%=serviciosBeanDefault.getNpservicioid()%>","<%=serviciosBeanDefault.getNpnomserv()%>","<%=serviciosBeanDefault.getNpnomcorserv()%>","<%=MiUtil.getString(serviciosBeanDefault.getNpexcludingind())%>"));
       <%}
     }%>

    //EFLORES PRY-1112 Carga de Servicios Excluyentes
       <% if(lstExcludingServices !=  null && lstExcludingServices.size()>0){
       for(ExcludingServiceBean excludingService : lstExcludingServices){ %>
       vExcludingServices.addElement(new ExcludingService("<%=excludingService.getServicePrincipal()%>","<%=excludingService.getServiceExcluded()%>","<%=excludingService.getServiceMessage()%>"));
       <%}}%>
       //FIN EFLORES PRY-1112
   }
   
  /*Developer: Lee Rosales
    Objetivo : Valida y agrega un servicio disponible a la lista de 
               servicios adicionales ( Contratado, Solicitado, Removido )
  */
   function fxAddService() {      
      longMaxServices = 30; // Cambio EZM
      var form = document.frmdatos;
      var wn_cantServ = vServicio.size();
      var objServicio;
      var exclude_serv;
      /*  variable para separacion de las "x" en el combo "cmbSelectedServices"  */
      var txt_separacion="  ";
      //var txt_separacion="--";
      /* tomamos como maximo 10 caracteres (blancos)para almacenar los servicios seleccionados */
      //var txt_vacios_2 = "          ";
      //var txt_vacios_2 = "##########";
      var txt_vacios_2 = "";
      //var txt_vacios_2 = "                    ";
                                                                                        
      /* Busca los roles que ya se seleccionaron */
      var aRolesSelected = new Array();
      var nameRol = "";
      var nameRol_selec = "";
      var idxARoles = 0;
      var i=0;
      /* Limpiamos el comboBox SelectedServices cuando se va a insertar el primer servicio */
      if (form.cmbSelectedServices.length == 1 && form.cmbSelectedServices.options[0].value == "") {
          deleteAllValues(form.cmbSelectedServices);
      }
   
      /* Guardamos los Roles de los servicios seleccionados */
      for (i=0;i<form.cmbSelectedServices.options.length;i++){
           nameRol = getRol(form.cmbSelectedServices.options[i].value);
           if (nameRol!="" && !valueExistsInArray(aRolesSelected,nameRol)){
               for(k=0; k<wn_cantServ; k++){
                   objServicio = vServicio.elementAt(k);
                   if (objServicio.nameShort == form.cmbSelectedServices.options[i].value && objServicio.modify_new == "S" ) {
                       aRolesSelected[idxARoles] = nameRol;
                       idxARoles++;
                   }
                }
            };
       };
       //--------------------------------------------
       //EFLORES PRY-1112 VERIFICAR QUE NO SON SERVICIOS EXCLUYENTES
       if(form.cmbSelectedServices != null){
           var txt_value_inserta = form.cmbAvailableServices.value;
           if(form.cmbSelectedServices.options.length > 0){
               for(m=0;m<form.cmbSelectedServices.options.length;m++){
                   var serv_temp = form.cmbSelectedServices.options[m].value;
                   var modify_new;
                   for(n=0;n<vServicio.size();n++){
                       objTemp = vServicio.elementAt(n);
                       if(objTemp.nameShort == serv_temp){
                           modify_new = objTemp.modify_new;
                           break;
                       }
                   }
                   if(vExcludingServices.size()>0){
                       for(k=0;k<vExcludingServices.size();k++){
                           objExcludingService = vExcludingServices.elementAt(k);
                           if(modify_new!="N" && ((serv_temp == objExcludingService.sPrincipal && txt_value_inserta == objExcludingService.sExcluded)
                               || (serv_temp == objExcludingService.sExcluded && txt_value_inserta == objExcludingService.sPrincipal))){
                               alert(objExcludingService.sMessage);
                               return;
                           }
                           if(serv_temp == txt_value_inserta){
                               alert("No se pueden tener dos servicios con el mismo SNCODE");
                               return;
                           }
                       }
                   }
               }
           }
       }
       //FIN EFLORES PRY-1112
       /* Valido que no existan servicios excluyentes seleccionados en selectedServices */
       for (i=0; i<form.cmbAvailableServices.options.length; i++){
            if (form.cmbAvailableServices.options[i].selected){
                nameRol = getRol(form.cmbAvailableServices.options[i].value);
                if (nameRol!="" && valueExistsInArray(aRolesSelected,nameRol)){
                    alert("Ya existe un Servicio Excluyente seleccionado para "+ nameRol);
                    return;
                };
             };
       };

       /* Valido que no existan servicios excluyente seleccionados en availableServices */
       for (i=0;i<form.cmbAvailableServices.options.length;i++){
            if (form.cmbAvailableServices.options[i].selected){
                nameRol = getRol(form.cmbAvailableServices.options[i].value);
                
                if (nameRol!="" && valueExistsInArray(aRolesSelected,nameRol)){
                    alert("Se han seleccionado varios Servicios Excluyentes de "+nameRol);
                    return;
                }else if (nameRol!="" ){
                     aRolesSelected[idxARoles] = nameRol;
                     idxARoles++;
                }
             }
       }
       /* Pasar los elementos de disponbible a asignado */
       for (i=0;i<form.cmbAvailableServices.options.length;i++){
            if (form.cmbAvailableServices.options[i].selected){
                difServices = 0;
                if( form.cmbAvailableServices.options[i].text.length < longMaxServices )
                  difServices = longMaxServices - form.cmbAvailableServices.options[i].text.length;
                
                for( k = 0; k < difServices; k++ )
                  txt_vacios_2+=" ";
                  //txt_vacios_2+="#";
                
                var txt_texto   =  form.cmbAvailableServices.options[i].text + txt_vacios_2;
                //var txt_texto_2 = txt_texto.substr(0,12) + txt_separacion + " " + txt_separacion + "x";
                var txt_texto_2 = txt_texto.substr(0,longMaxServices) + txt_separacion + " " + txt_separacion + "x";
                //var txt_texto   =  form.cmbAvailableServices.options[i].text + txt_vacios_2;
                //var cantTexto = txt_texto.substr(0,12)
                //var txt_texto_2 = txt_texto.substr(0,12) + txt_separacion + " " + txt_separacion + "x";
                //var txt_texto_2 = txt_texto.substr(0,20) + txt_separacion + " " + txt_separacion + "x";
                fxAddToList(form.cmbSelectedServices, txt_texto_2, form.cmbAvailableServices.options[i].value);
    
                   for(j=0; j<wn_cantServ; j++){
                       objServicio = vServicio.elementAt(j);
                       //alert("Excluding ID : " + objServicio.exclude)
                       if (objServicio.nameShort == form.cmbAvailableServices.options[i].value) {
                           objServicio.modify_new = "S";
                           if( objServicio.exclude == "ALQ" )
                              alquilerServicesSelected++;

                           try{
                            if( objServicio.exclude == "ALQ1" )
                                alquilerServicesSelected1++;
                            if( objServicio.exclude == "ALQ2" )
                                alquilerServicesSelected2++;
                            if( objServicio.exclude == "ALQ3" )
                                alquilerServicesSelected3++;
                           }catch(e){}
                           
                           if( objServicio.exclude == "INT" )
                              rentServicesSelected++;
                           if( objServicio.exclude == "END" )
                              rentServicesSelectedEnd++;
                       }
                   }
             }
       }
      
       deleteSelectedValues(form.cmbAvailableServices);
       form.hdnFlagServicio.value = "1";
       bServiceModified = true;
       
       form.item_services.value = GetSelectedServices();
       
       return;
   } /* end _function addService*/
   
   /*Developer: Frank Picoy
    Objetivo : Agrega servicios por defecto a la lista de servicios adicionales seleccionados
  */
   function fxAddServiceToDefault() {      
      longMaxServices = 30;
      var form = document.frmdatos;
      var wn_cantServ = vServicio.size();
      var objServicio;
      var exclude_serv;
      /*  variable para separacion de las "x" en el combo "cmbSelectedServices"  */
      var txt_separacion="  ";
      var txt_vacios_2 = "";
                                                                                        
      /* Busca los roles que ya se seleccionaron */
      var aRolesSelected = new Array();
      var nameRol = "";
      var nameRol_selec = "";
      var idxARoles = 0;
      var i=0;
      
      /* Limpiamos el comboBox SelectedServices cuando se va a insertar el primer servicio */
      if (form.cmbSelectedServices.length == 1 && form.cmbSelectedServices.options[0].value == "") {
          deleteAllValues(form.cmbSelectedServices);
      }
   
      /* Guardamos los Roles de los servicios seleccionados */
      for (i=0;i<form.cmbSelectedServices.options.length;i++){
           nameRol = getRol(form.cmbSelectedServices.options[i].value);
           if (nameRol!="" && !valueExistsInArray(aRolesSelected,nameRol)){
               for(k=0; k<wn_cantServ; k++){
                   objServicio = vServicio.elementAt(k);
                   if (objServicio.nameShort == form.cmbSelectedServices.options[i].value && objServicio.modify_new == "S" ) {
                       aRolesSelected[idxARoles] = nameRol;
                       idxARoles++;
                   }
                }
            };
       };
       
       var txt_exclude = "";  
       var txt_vacios_3 = "";
       var indice = "";
       var indiceServ = "";
       var arrIndices = new Array();
       var m=0;
       var arrSelectedExcludes = new Array();
       var arrIndicesServ = new Array;
       for(z=0; z<wn_cantServ; z++){
           objServicio = vServicio.elementAt(z);
           for (k=0;k<form.cmbSelectedServices.options.length;k++){
               if ((objServicio.nameShort == form.cmbSelectedServices.options[k].value) && (objServicio.exclude !="")) {
                  arrSelectedExcludes[m]=objServicio.exclude;
                  arrIndices[m]=k;
                  arrIndicesServ[m]=z;
                  m=m+1;
               } 
            }
        }
       /* Pasar los elementos de disponbible a asignado */
       for (i=0;i<form.cmbAvailableServices.options.length;i++){
            if (form.cmbAvailableServices.options[i].selected){
                difServices = 0;
                if( form.cmbAvailableServices.options[i].text.length < longMaxServices )
                  difServices = longMaxServices - form.cmbAvailableServices.options[i].text.length;
                
                for( k = 0; k < difServices; k++ )
                  txt_vacios_2+=" ";
                
                var txt_texto   =  form.cmbAvailableServices.options[i].text + txt_vacios_2;
                var txt_texto_2 = txt_texto.substr(0,longMaxServices) + txt_separacion + " " + txt_separacion + "x";
                 
                for(j=0; j<wn_cantServ; j++){
                    objServicio = vServicio.elementAt(j);
                    if (objServicio.nameShort == form.cmbAvailableServices.options[i].value) {
                           objServicio.modify_new = "S";
                    if( valueExistsInArray(arrSelectedExcludes,objServicio.exclude)){
                         for (k = 0; k < arrSelectedExcludes.length; k++){
                             if (objServicio.exclude==arrSelectedExcludes[k]) {
                                 indice = arrIndices[k];
                                 indiceServ = arrIndicesServ[k];
                                 break;
                             }
                         }
                             
                         if( form.cmbSelectedServices.options[indice].text.length < longMaxServices )
                               difServices = longMaxServices - form.cmbSelectedServices.options[indice].text.length;
                
                         for( k = 0; k < difServices; k++ )
                               txt_vacios_3+=" ";
                             
                         var txt_texto_1   =  form.cmbSelectedServices.options[indice].text + txt_vacios_3;
                         var txt_texto_3 = txt_texto_1.substr(0,longMaxServices) + txt_separacion + "x" + txt_separacion + " ";
                         vServicio.elementAt(indiceServ).modify_new="N";
                         form.cmbSelectedServices.options[indice].text = txt_texto_3;
                             
                    }
                           
                    if( objServicio.exclude == "ALQ" )
                        alquilerServicesSelected++;

                    try{
                        if( objServicio.exclude == "ALQ1" )
                          alquilerServicesSelected1++;
                        if( objServicio.exclude == "ALQ2" )
                          alquilerServicesSelected2++;
                        if( objServicio.exclude == "ALQ3" )
                          alquilerServicesSelected3++;
                    }catch(e){}
                           
                    if( objServicio.exclude == "INT" )
                        rentServicesSelected++;
                    if( objServicio.exclude == "END" )
                        rentServicesSelectedEnd++;
                    }
                }
                fxAddToList(form.cmbSelectedServices, txt_texto_2, form.cmbAvailableServices.options[i].value);
             }
       }
      
       deleteSelectedValues(form.cmbAvailableServices);
       form.hdnFlagServicio.value = "1";
       bServiceModified = true;
       
       form.item_services.value = GetSelectedServices();
       
       return;
   } 
   
   /*Comentado porque no es usado por ninguna funcionalidad - FPICOY 07/09/2011
   function getSelectedIdServices() {
      var str = "";
      var cont=0;
      var wn_cantServ = vServicio.size();
      for(j=0; j<wn_cantServ; j++){
          objServicio = vServicio.elementAt(j);
          if (objServicio.modify_new == "S") {
            //alert('El nombre es--->' + objServicio.nameShort);
            str =  str +  objServicio.id + ",";
            ++cont;
          }
      }
      if (cont>0) {
         str =  str.substr(0,str.length-1);
      }
      return str;
  }*/
   
  function fxAddToList(listField, newText, newValue) {
    var len = listField.length++; /* Incrementa el tamaño de la lista y retorno el tamaño actual*/
    listField.options[len].value = newValue;
    listField.options[len].text  = newText;
  }
  
  function fxLongMaxServices(objArrayList) {
  
    <%
    if ( objArrayList != null && objArrayList.size() > 0 ){
     ServiciosBean serviciosBean = null;
     serviciosBean = (ServiciosBean)objArrayList.get(0);
     int longMaxServices = MiUtil.getString(serviciosBean.getNpnomserv()).length();
     for( int j = 1; j < objArrayList.size(); j++ ){
         serviciosBean = (ServiciosBean)objArrayList.get(j);
          if( MiUtil.getString(serviciosBean.getNpnomserv()).length() > longMaxServices )
            longMaxServices = MiUtil.getString(serviciosBean.getNpnomserv()).length();
     }
     }
     %>
     
     longMaxServices = '30<%//=longMaxServices%>';
  }
   
   
   // Función que elimina un servicio del combo de Servicios Seleccionados
  function fxEliminaServicio(objThis){    
    form = document.frmdatos;
    var hdnSpecification = "<%=hdnSpecification%>";
    var txt_value_elimina;
    var txt_elimina_modify_new = "";
    var txt_modify;
    var aRolesSelected = new Array();
    var nameRol= "";
    var idxARoles = 0;
    var objServicio;
    var wn_cantServ;
    //alert(objThis.value);
    if (objThis !=  null){
       txt_value_elimina = objThis.value;
    }else{
       txt_value_elimina = form.cmbSelectedServices.value;
    }

     //Verificamos si el servicio se puede eliminar
    //--------------------------------------------
    if (vServicioArren.size()>0){
        for (n=0; n<vServicioArren.size();n++){
           objServicioArr = vServicioArren.elementAt(n);
              if(txt_value_elimina == objServicioArr.nameShort){
                  if(objServicioArr.exclude == "ALQ"){
                     count_active_new = 0;
                     count_modify_new = 0;
                     
                     for(j=0; j<vServicioArren.size(); j++){
                       objServicioArr2 = vServicioArren.elementAt(j);
                       if(objServicioArr2.exclude == 'ALQ' && objServicioArr2.active_new == 'S'){
                         count_active_new = count_active_new + 1;
                       }
                     }
                     
                     for(j=0; j<vServicioArren.size(); j++){
                       objServicioArr2 = vServicioArren.elementAt(j);
                       if(objServicioArr2.exclude == 'ALQ' && objServicioArr2.modify_new == 'S'){
                         count_modify_new = count_modify_new + 1;
                       }
                     }
                     
                     if(count_active_new < 2 && objServicioArr.active_new == 'S'){
                        alert("No se puede eliminar este Servicio de Arrendamiento."); 
                        return;
                     }else if(count_modify_new < 2 && objServicioArr.modify_new == 'S'){
                        alert("No se puede eliminar este Servicio de Arrendamiento."); 
                        return;
                     }else{
                        //Armamos nuevamente el vector de vServicioArren
                        //----------------------------------------------
                        //Vector de Servicios por Defecto de Arrendamiento Temporal
                        var vServicioArrenTemp    = new Vector();
                   
                        for (n=0; n<vServicioArren.size();n++){
                          objServicioArr = vServicioArren.elementAt(n);
                          //alert('objServicioArr.name :'+objServicioArr.name);
                          vServicioArrenTemp.addElement(new ServicioArr(objServicioArr.id,objServicioArr.name,objServicioArr.nameShort,objServicioArr.exclude,objServicioArr.active_new,objServicioArr.modify_new));
                        } 
                        parent.mainFrame.vServicioArren.removeElementAll();
                     
                        for (h=0; h<vServicioArrenTemp.size();h++){
                          objServicioArrTemp = vServicioArrenTemp.elementAt(h);
                          if(txt_value_elimina != objServicioArrTemp.nameShort){
                            vServicioArren.addElement(new ServicioArr(objServicioArrTemp.id,objServicioArrTemp.name,objServicioArrTemp.nameShort,objServicioArrTemp.exclude,objServicioArrTemp.active_new,objServicioArrTemp.modify_new));
                          }
                        }
                        vServicioArrenTemp.removeElementAll();
                     }
                  }else if( objServicioArr.exclude == "MSJ" && hdnSpecification=="<%=strCategoryCambioModeloId%>" ){
                     alert("No se puede eliminar un Servicio de Mensajería."); 
                     return;
                  }
                  
              }
        }
    }
    
    wn_cantServ = vServicio.size();

    /*  Inicio: Se valida que no exista un servicio del mismo grupo  */
    for (i=0 ; i<form.cmbSelectedServices.options.length; i++){
       nameRol = getRol(form.cmbSelectedServices.options[i].value);
       if (nameRol!="" && !valueExistsInArray(aRolesSelected,nameRol)){
          for(k=0; k<wn_cantServ; k++){
             objServicio = vServicio.elementAt(k);
             if (objServicio.nameShort == txt_value_elimina ){
                txt_elimina_modify_new = objServicio.modify_new;
             };
             if (objServicio.nameShort == form.cmbSelectedServices.options[i].value && objServicio.modify_new == "S" ) {
                aRolesSelected[idxARoles] = nameRol;
                idxARoles++;
             }
             if (txt_value_elimina == objServicio.nameShort && objServicio.exclude == "MSJ" && hdnSpecification=="<%=strCategoryCambioModeloId%>"){
                alert("No se puede eliminar un Servicio de Mensajería."); 
                return;
             }
          }
       };
    };

    if (txt_elimina_modify_new == "N"){      
       nameRol = getRol(form.cmbSelectedServices.value);
       if (nameRol!="" && valueExistsInArray(aRolesSelected,nameRol)){
          alert("Ya existe un Servicio Excluyente seleccionado para "+ nameRol);
          return;
       };
    };
    /* Fin: Validación de grupo */


   
    //Se eliminan todos los valores de los servicios seleccionados
    //-------------------------------------------------------------
    deleteAllValues(form.cmbSelectedServices);
    
   //Se vuelve a cargar todos los servicios por defecto en base al vector de servicios
   //---------------------------------------------------------------------------------
    /* variable para separacion de las "x" en el combo "cmbSelectedServices" */
    var txt_separacion="  ";
    /* tomamos como maximo 10 caracteres (blancos) para almacenar los servicios seleccionados */
    //var txt_vacios_2 = "          ";
    var txt_vacios_2 = "";
    for(i=0; i<wn_cantServ; i++){
       objServicio = vServicio.elementAt(i);
       
       difServices = 0;
       if( objServicio.name.length < longMaxServices )
         difServices = longMaxServices - objServicio.name.length;
      
       for( k = 0; k < difServices; k++ )
         txt_vacios_2+=" ";
       
       
       /*Antes obtenía el nombre corto*/
       //var txt_servicio = objServicio.nameShortDisplay + txt_vacios_2;
       /*Ahora obtengo el nombre largo*/
       var txt_servicio = objServicio.name + txt_vacios_2;
       
       var txt_servicio_2 = txt_servicio.substr(0,longMaxServices);
       /* Si el Item tiene el servicio actualmente  */
       if (objServicio.active_new == "S"){
       //alert("objServicio.active_new == S");
          if (objServicio.nameShort == txt_value_elimina){
             if (objServicio.modify_new == "S"){ /* Si al Item continua o se le agrega el servicio */
                objServicio.modify_new = "N";                
                AddNewOption(form.cmbSelectedServices, objServicio.nameShort, txt_servicio_2 + txt_separacion + "x" + txt_separacion + " ");                
             }
             else{                
                objServicio.modify_new = "S";
                AddNewOption(form.cmbSelectedServices, objServicio.nameShort, txt_servicio_2 + txt_separacion + "x" + txt_separacion +"x");
             }
          }
          else{
             /* Si al Item continua o se le agrega el servicio */
             if (objServicio.modify_new == "S"){                
                txt_modify ="x";
             }
             else{                  
                txt_modify =" ";
             }
             AddNewOption(form.cmbSelectedServices, objServicio.nameShort, txt_servicio_2 + txt_separacion + "x" + txt_separacion + txt_modify);
          }
       }
       else {            
          //alert("objServicio else")          
          if (objServicio.active_new == "N" && objServicio.modify_new == "S"){            
             if (objServicio.nameShort == txt_value_elimina){
                //alert("objServicio.nameShort == txt_value_elimina")
                objServicio.modify_new = "N";
                
                if( objServicio.exclude == "ALQ" )
                   parent.mainFrame.alquilerServicesSelected--;

                try{
                  if( objServicio.exclude == "ALQ1" )
                     parent.mainFrame.alquilerServicesSelected1--;
                  if( objServicio.exclude == "ALQ2" )
                     parent.mainFrame.alquilerServicesSelected2--;
                  if( objServicio.exclude == "ALQ3" )
                     parent.mainFrame.alquilerServicesSelected3--;
                }catch(e){}
                
                if( objServicio.exclude == "INT" )
                   parent.mainFrame.rentServicesSelected--;
                if( objServicio.exclude == "END" )
                   parent.mainFrame.rentServicesSelectedEnd--;
                
                
                /*Antes mostraba el nombre corto*/
                //option1 =  new Option(objServicio.nameShortDisplay,objServicio.nameShort);
                /*Ahora mostraba el nombre largo*/
                option1 =  new Option(objServicio.name,objServicio.nameShort);
                form.cmbAvailableServices.options[form.cmbAvailableServices.length] = option1;
                //form.cmbAvailableServices.options[form.cmbAvailableServices.length] = new Option("                                ");
             }
             else{                
                AddNewOption(form.cmbSelectedServices, objServicio.nameShort, txt_servicio_2 + txt_separacion + " " + txt_separacion +"x");
             }
          }
       }
    } /* end_for */

    //Se vuelve a cargar todos los servicios por defecto en base al vector de arrendamiento
   //--------------------------------------------------------------------------------------
   var aServiceDefault    = new Array();
   var idxAServDef  = 0
   
   for (i=0;i<parent.mainFrame.frmdatos.cmbSelectedServices.options.length;i++){
       aServiceDefault[idxAServDef] = form.cmbSelectedServices.options[i].value;
       idxAServDef++; 
   }

   if (vServicioArren.size()>0){
       for(i=0; i<vServicioArren.size(); i++){
          objServicioArr = vServicioArren.elementAt(i);
          //alert('objServicioArr.nameShort ANTES : '+objServicioArr.nameShort);
          
            if(txt_value_elimina != objServicioArr.nameShort){
               if(objServicioArr.nameShort!="" && !valueExistsInArray(aServiceDefault,objServicioArr.nameShort)){ 
               /*Ahora obtengo el nombre largo*/
               var txt_servicio = objServicioArr.name + txt_vacios_2;
               var txt_servicio_2 = txt_servicio.substr(0,longMaxServices);
               
               //alert('objServicioArr.nameShort : '+objServicioArr.nameShort);
               //alert('objServicioArr.active_new : '+objServicioArr.active_new);
               if (objServicioArr.active_new == "S" && objServicioArr.modify_new == "S"){
                  AddNewOption(form.cmbSelectedServices, objServicioArr.nameShort, txt_servicio_2 + txt_separacion + "x" + txt_separacion +"x");
               }else if (objServicioArr.active_new == "S"){
                  AddNewOption(form.cmbSelectedServices, objServicioArr.nameShort, txt_servicio_2 + txt_separacion + "x" + txt_separacion +" ");
               }else{
                  AddNewOption(form.cmbSelectedServices, objServicioArr.nameShort, txt_servicio_2 + txt_separacion + " " + txt_separacion +"x");
               }
            }
          } else {
            
                objServicioArr.nameShort = "";
              	objServicioArr.nameShortDisplay = "";
                objServicioArr.nameDisplay = "";
                objServicioArr.name = "";   
                objServicioArr.modify_new = "";
                objServicioArr.id	= "";
                objServicioArr.exclude	= "";
    
                vServicioArren[i] = objServicioArr;
            
              }
      }
   }
   
    form.hdnFlagServicio.value = "1";
    bServiceModified = true;
  }; /* end_function */
  
  //CBARZOLA:Funcion que crea un vector a partir de los servicios adicionales seleccionados
   //se utiliza en la validacion de servicios core.
   function fxGetSelectedServices(cadenaSeleccionados)
   {
    //alert("fxGetSelectedServices:"+cadenaSeleccionados);
    var cadena = cadenaSeleccionados.split("|");
    var grupos=Math.floor(cadena.length/3);
    var vServiciosSelected = new Vector();
	 
    var id="";
    var active_new="";
    var modify_new="";
    for(i=0;i<grupos;i++){
		vid=cadena[i*3+1];
		vactive_new=cadena[i*3+2];
		vmodify_new=cadena[i*3+3];      
      objServicioSelected= new Servicio(vid,'','','');
      objServicioSelected.active_new=vactive_new;
      objServicioSelected.modify_new=vmodify_new;
      vServiciosSelected.addElement(objServicioSelected);
    }
    return vServiciosSelected;
   }
      
  //CBARZOLA:Función para cargar el vector con los servicios Core según plan Tarifario
	function fxLoadServiceComercialCore (){
	vServiciosComercialesCore.removeElementAll();
    <%
	if (strPlan.equals("") ||strPlan.equals("0")){
      strPlan=strPlanAux;
     System.out.println("[objServiceAditional.jsp][fxLoadServiceComercialCore]strPlanAux----->"+strPlanAux);
	}
	System.out.println("[objServiceAditional.jsp][fxLoadServiceComercialCore]strPlan----->"+strPlan);
	//strPlan="1510";
	if(!(strPlan.equals("") || strPlan.equals("0"))&& iResultado==1){
		HashMap hshCoreService    = objNewOrderService.ServiceDAOgetCoreServicebyPlan(MiUtil.parseLong(strPlan));
      ServiciosBean objServiciosBean = null;      
      ArrayList listServices =(ArrayList)hshCoreService.get("objServiciosBean");
      String strMessageService = (String)hshCoreService.get("strMessage");         
        if (strMessageService==null){
           System.out.println("[objSA]size:"+listServices.size());
           for(int i=0;i<listServices.size();i++){
            objServiciosBean = (ServiciosBean)listServices.get(i);
            System.out.println("[objSA]getNpservicioid:"+objServiciosBean.getNpservicioid()+"getNpnomserv:"+objServiciosBean.getNpnomserv()+"getNpnomcorserv:"+objServiciosBean.getNpnomcorserv()+"objServiciosBean:"+objServiciosBean.getNpexcludingind());
      %>
            vServiciosComercialesCore.addElement(new Servicio("<%=objServiciosBean.getNpservicioid()%>","<%=objServiciosBean.getNpnomserv()%>","<%=objServiciosBean.getNpnomcorserv()%>","<%=MiUtil.getString(objServiciosBean.getNpexcludingind())%>"));                   
           <%}
        }else{%>
            alert(" [objServiceAditional.jsp ] <%=MiUtil.getMessageClean(strMessageService)%>");      
        <%}
        }
        %>
  }    
      
   /*Developer: CBARZOLA
    fecha     :05/07/2009
    Objetivo : Valida los servicios comerciales
  */
   function fxValidaServiciosComerciales(cadenaSeleccionados){
		//alert("flag:"+<%=iResultado%>);
		if (<%=iResultado%>==0){
			//alert("No valida"+<%=iResultado%>);
			return false;
		}	
		var vSelectServId = fxGetSelectedServices(cadenaSeleccionados);//vector objetos servicios selec.
		var vServiciosTemp = new Vector();
		var error1 = false;
		var error2 = false;
		var sServicios1 ="";
		var sServicios2 ="";
		var wn_cantServ=vServicio.size()
    
		for(j=0;j<vSelectServId.size();j++){
			objSelectServ=vSelectServId.elementAt(j);
			for(k=0; k<wn_cantServ; k++){
				objServicio = vServicio.elementAt(k);
				if(objSelectServ.id==objServicio.id){      
					objSelectServ.name=objServicio.name;
					objSelectServ.nameDisplay=objServicio.nameDisplay;
					objSelectServ.nameShort=objServicio.nameShort;
					objSelectServ.nameShortDisplay=objServicio.nameShortDisplay;
					objSelectServ.exclude=objServicio.exclude;
					vServiciosTemp.addElement(objSelectServ); 
				}
			}
		}
    
		//alert('vServiciosComercialesCore.size:'+vServiciosComercialesCore.size());
		for(j=0;j<vServiciosTemp.size();j++){
			objServicioTemp=vServiciosTemp.elementAt(j);
			for(k=0;k< vServiciosComercialesCore.size();k++){
				objServicioAux=vServiciosComercialesCore.elementAt(k);
				if(objServicioTemp.id==objServicioAux.id && objServicioTemp.modify_new=='S'){          					
					error1=true;
					sServicios1=sServicios1+objServicioTemp.name+', ';          
				}        
				if(objServicioTemp.exclude != null && objServicioAux.exclude != null  && objServicioTemp.exclude != "" && objServicioAux.exclude != ""  && objServicioTemp.exclude==objServicioAux.exclude && objServicioTemp.modify_new=='S' ){
					error2=true;                                          
					sServicios2=sServicios2+objServicioAux.exclude+', ';
				}
			}
		}      
		if (error1==true){
			//se elimina la ultima coma y el espacio en blanco
			sServicios1=sServicios1.substring(0,sServicios1.length-2);		
			alert('El servicio seleccionado: '+sServicios1+' ya forma parte del plan seleccionado');
			//alert("aqui1"+objServicioTemp.modify_new);
			return true;
		}
		if (error2==true){
			//se elimina la ultima coma y el espacio en blanco
			sServicios2=sServicios2.substring(0,sServicios2.length-2);			
			alert('El plan seleccionado ya cuenta con los servicios excluyentes seleccionados: '+sServicios2);
			//alert("aqui2"+objServicioTemp.modify_new);
			return true;
		}        
   }


</script>