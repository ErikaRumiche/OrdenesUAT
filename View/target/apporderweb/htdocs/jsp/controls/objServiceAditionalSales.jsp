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
   //int intSSAAType  = 0;
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
         if( paramNpobjitemheaderid[i].equals("15")|| paramNpobjitemheaderid[i].equals("9") ) //DLAZO //EZM Compatibilidad Modelo-Plan-Servicio
         strModel = paramNpobjitemvalue[i]; 
      }   
  }
  
  System.out.println("[objServiceAditional][strPlan]"+strPlan);
  System.out.println("[objServiceAditional][strPlanAux]"+strPlanAux);
  System.out.println("[strSolution  ********************][strSolution]"+strSolution);
  if (MiUtil.parseInt(hdnSpecification)==Constante.KN_ACT_CAMB_PLAN_BA || MiUtil.parseInt(hdnSpecification)==Constante.KN_ACT_DES_SERVICIOS_BA){
     strType = "A";
  }
  
  //DLAZO - PRUEBA DE FILTRADO DE SUSCRIPCIONES (Cambio de Parametros)
  //johncmb inicio 
  if( MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA ||
      MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_NUEVA  || 
      MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_TDE    || 
      MiUtil.parseInt(hdnSpecification) == Constante.SPEC_REPOSICION_PREPAGO_TDE || //Se agrega reposicion prepago tde - TDECONV034
      MiUtil.parseInt(hdnSpecification) == Constante.SPEC_SSAA_SUSCRIPCIONES     || 
      MiUtil.parseInt(hdnSpecification) == Constante.SPEC_SSAA_PROMOTIONS 
  ) {
     strSSAAType  = "0,1,2,3,5"; }
  else{
     strSSAAType  = "0,1,5"; 
  }   
  //johncmb modificacion importante
  if(strPlan != null && !("".equals(strPlan)) ){
    System.out.println("[objServiceAditional][strMessage][strPlan !=null]"+strPlan);
    HashMap hServiceList  = objGeneralService.getServiceList(MiUtil.parseInt(strSolution), MiUtil.parseInt(strPlan), MiUtil.parseInt(strModel), strSSAAType, strType);
    System.out.println("[objServiceAditional][strMessage]"+strMessage);
    strMessage = (String)hServiceList.get("strMessage");
    System.out.println("[objArrayList.size***********]"+objArrayList.size());
    objArrayList =(ArrayList)hServiceList.get("objServiceList"); 
  }else{
    System.out.println("[objServiceAditional][strMessage][strPlanAux ==null]"+strPlanAux);
    HashMap hServiceList  = objGeneralService.getServiceList(MiUtil.parseInt(strSolution), MiUtil.parseInt(strPlanAux), MiUtil.parseInt(strModel), strSSAAType, strType);
    System.out.println("[objServiceAditional][strMessage]"+strMessage);
    strMessage = (String)hServiceList.get("strMessage");
    System.out.println("[objArrayList.size***********]"+objArrayList.size());
    objArrayList =(ArrayList)hServiceList.get("objServiceList"); 
  }  
  //johncmb fin  
  //johncmb inicio
//  HashMap getServiceGroupLst(){
    HashMap objHashMap = objGeneralService.getServiceGroupLst();
  //if( objHashMap.get("strMessage")!=null ) //throw new Exception((String)objHashMap.get("strMessage"));
   NpTableBean npTableBeanGroup = new NpTableBean();  
  
  ArrayList objArrayListGroup = (ArrayList)objHashMap.get("objServiceGroupList");

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
<input type='hidden' name='hinicio' value=''> <!-- johncmb--->
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
            <table border="0" width="100%" cellpadding="2" cellspacing="0" class="RegionBorder">
             <!-- inicio johncmb ------------->
             <tr align="center">
                  <td  valign="top">
                  <table border="0" cellspacing="0" cellpadding="0" width="100%">
                        <tr >
                           <td class="CellLabel" align="center">Servicios</td>
                           <td class="CellLabel"></td>
                           <td class="CellLabel" align="center">
                           
              <select name="cmbSevices"  onchange="fxShowServiceSP(this)"> 
              <option value="-1"> </option> 
              <!-- inicio--> 
              <%
              if ( objArrayListGroup != null && objArrayListGroup.size() > 0 ){

               for( int i=0; i<objArrayListGroup.size();i++ ){ 
               
                    npTableBeanGroup = new NpTableBean();
                    
                    npTableBeanGroup = (NpTableBean)objArrayListGroup.get(i);
             %>
          
          
          <%if (MiUtil.parseInt(hdnSpecification) == Constante.SPEC_SSAA_SUSCRIPCIONES ) 
          { 
           if("2".equals(npTableBeanGroup.getNpvalue())){    
           %>        
              <option value='<%=npTableBeanGroup.getNpvalue()%>'>
                             <%=npTableBeanGroup.getNpvaluedesc()%>
              </option>
           <%
           }
          }
          else if( MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA ||
                   MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_NUEVA  ||
                   MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_TDE    ||
                   MiUtil.parseInt(hdnSpecification) == Constante.SPEC_REPOSICION_PREPAGO_TDE //Se agrega reposicion prepago tde - TDECONV034
          ) { 
          %>        
              <option value='<%=npTableBeanGroup.getNpvalue()%>'>
                             <%=npTableBeanGroup.getNpvaluedesc()%>
              </option>
          <%
           
          } else{
          if("0".equals(npTableBeanGroup.getNpvalue())||"1".equals(npTableBeanGroup.getNpvalue())||"5".equals(npTableBeanGroup.getNpvalue())){    
          %>        
              <option value='<%=npTableBeanGroup.getNpvalue()%>'>
                             <%=npTableBeanGroup.getNpvaluedesc()%>
              </option>
          <%
           }
         }
         %>
              <%
                    }
                }
                %>
              <!-- fin  -->
              
              </select>
              
                           </td>
                           <td class="CellLabel"> <div id="dServiceDetail"></div>
                           </td>
                        </tr>
                    </table>
                  </td>
             </tr>   
            <!--  fin johncmb ------------ -->
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
   //  johncmb  inicio--->
    //------------------------------------------------------------------//
    var cmbServiciosval = "";
    var varLocal = "";
   //Creamos las una clase DatosCombo  
   function DatoCombo(id,texto){ 
    this.texto = texto; 
    this.id = id;  
   } 
  //arrays temporales para guardar los valores del combo cmbAvailableService cuando esta completo y cuando esta filtrado
   var aTmpAvailableService  = new Array();
   var aTmpAvailableServiceFil   = new Array();
  
   
   //Funcion que pasa los valores del combo al array
   function SA_ComboToArray(pcmb,parray){ 
    var tamanho=parray.length;
    for (var i=0; i<pcmb.options.length; i++){
     dato = new DatoCombo( pcmb.options[i].value,pcmb.options[i].text); 
     parray[i]=dato;
    }
   }
    
    //Funcion que pasa los valores del array al combo
   function SA_ArrayToCombo(pcmb,parray){ 
    for (var i=0;i<parray.length;i++){
     fxAddToList(pcmb, parray[i].texto, parray[i].id);
    }
   }
  // Salvamos el valor del combo filtrado en un array  y ponemos todos los servicios
  //disponibles original
 function  salvaCmbFil_RecServOriginal(){
       var idsel=0;
       aTmpAvailableServiceFil=new Array();
       //buscamos el id del elemento seleccionado en el combo filtrado
        for (var i=0; i<form.cmbAvailableServices.options.length; i++){
            if (form.cmbAvailableServices.options[i].selected){
                idsel = form.cmbAvailableServices.options[i].value;
                break;
            }
        }
        
        //salvamos el valor del combo filtrado en el array correspondiente
        SA_ComboToArray(form.cmbAvailableServices,aTmpAvailableServiceFil);
        //Limpiamos el combo
        deleteAllValues(form.cmbAvailableServices);
        //cargamos el combo con el valor del array con todos los elementos
        SA_ArrayToCombo(form.cmbAvailableServices,aTmpAvailableService);
        //En el combo buscamos id que corresponda al id del combo filtrado , y lo ponemos como seleccionado
        for (var j=0; j<form.cmbAvailableServices.options.length; j++){
            if (form.cmbAvailableServices.options[j].value==idsel){
                form.cmbAvailableServices.options[j].selected=true;
                break;
            }
        }
        return idsel;
 }

// Salvamos el valor del combo original  en un array  y ponemos los
// servicios filtrados
 function  salvaCmbOriginal_RecServFil(){
        aTmpAvailableService=new Array();
        //salvamos el valor del combo filtrado en el array correspondiente
        SA_ComboToArray(form.cmbAvailableServices,aTmpAvailableService);
        //Limpiamos el combo
        deleteAllValues(form.cmbAvailableServices);
        //cargamos el combo con el valor del array filtrado
        SA_ArrayToCombo(form.cmbAvailableServices,aTmpAvailableServiceFil);
 }
 //Elemina el elemento del array por id
 function eliminaElemetoDeArray(parray,pid){
     var aTemp= new Array();
     var n=0;
     for (var i=0; i<parray.length; i++){
          if (parray[i].id!=pid){
            aTemp[n]= parray[i];
            n++;
          }
     }
     parray = new Array(); 
     for (var j=0; j<aTemp.length; j++){
          parray[j]= aTemp[j];
     }
  return   parray;    
 }
 
 //Agrega un elemenento al array
 function agregaElemetoDeArray(parray,pid){
     var aTemp= new Array();
     var n=0;
     
       for (var i=0; i<parray.length; i++){
            if (parray[i].id!=pid){
            aTemp[n]= parray[i];
            n++;
            }
        }
      parray = new Array(); 
      //alert("PASO --aTemp[0].id -->"+aTemp[1].texto);
        for (var j=0; j<aTemp.length; j++){
           // if (aTemp[j].id!=pid){
            parray[j]= aTemp[j];
           // }
        }
         //alert("Al final --aTemp[0].id -->"+parray[1].texto);
  return   parray;    
 }
//-----------------------------------

//Filtra los servicios y los guarda en aTmpAvailableServiceFil
function fxObtenerServiciosFitrados(){
var j=0;
var wn_cantServ=vServicio.size();
aTmpAvailableServiceFil=new Array();
// Verificamos que cmbServiciosval tenga un valor distinto de 0 lo cual
//indica la selección de un criterio del filtro 

 //if(cmbServiciosval!="0"){
 if(cmbServiciosval!="-1"){
 //Recorremos el vertor vServicio y buscamos los elementos que correpondan
 // al grupo que indicque la variable cmbServiciosval, cada vez que se encuentre 
 // un item lo buscamos en el combo cmbAvailableServices y copiamos sus valores 
 // en el array aTmpAvailableServiceFil
 
    for(var i=0; i<wn_cantServ; i++){
        myObjServicio = vServicio.elementAt(i);
        
        if(trim(myObjServicio.group)==cmbServiciosval){
        //Recorremos el combo buscando la opcion que cumpla la condicion  
          for(var k=0;k<form.cmbAvailableServices.options.length;k++){
              if(form.cmbAvailableServices.options[k].value==myObjServicio.nameShort){
               dac=new DatoCombo(form.cmbAvailableServices.options[k].value,form.cmbAvailableServices.options[k].text);
               aTmpAvailableServiceFil[j]=dac;     
               j=j+1;
               break;
              }
          }
        }
    }
 }else{  
 // si cmbServiciosval es 0 indica que no hay filtro y se tiene que
 // cargar todos los valores del combo cmbAvailableServices y los copiamos en 
 // aTmpAvailableServiceFil
    for(var n=0;n<aTmpAvailableService.length;n++ ){
     aTmpAvailableServiceFil[n]=aTmpAvailableService[n];
    }
  }
}

function fxClicLocal(){
varLocal="L";
}
function fxShowServiceSP(objThis){

   var vsele="";
  // boolean bPlanValido=false;
   // Verificamos que el combo cmb_ItemPlanTarifario tengo un valor seleccionado
   //johncmb temporal se modifico para mc inicio
   if(document.getElementById("cmb_ItemPlanTarifario")!=null){
      if(form.cmb_ItemPlanTarifario.selectedIndex!=0){
         bPlanValido=true;  
      }else{
      bPlanValido=false;  
      }
   }else{
    bPlanValido=true;
   }
  //johncmb temporal se modifico para mc fin
    if(bPlanValido){ // //johncmb temporal se modifico  bPlanValido
    vsele=trim(objThis.value);
    //salvamos el valor del del combo cmbServicio en cmbServiciosval
    cmbServiciosval=vsele;
    //  Si el array(aTmpAvailableService) tiene datos entonces
    //  lo cargamos al combo
    if(aTmpAvailableService.length>0){
    deleteAllValues(form.cmbAvailableServices);
    SA_ArrayToCombo(form.cmbAvailableServices,aTmpAvailableService);
    }
    //LLena el array aTmpAvailableServiceFil con los servicios filtrados
    fxObtenerServiciosFitrados();
    // Salvamos el valor del combo cmbAvailableServices  en elarray aTmpAvailableService
    // y ponemos los  servicios filtrados el combo cmbAvailableServices
    salvaCmbOriginal_RecServFil();
    } else {
      alert("Debe seleccionar un plan Tarifario ");
      form.cmbSevices.selectedIndex=0;
    }
  }

 //------------------------------------------------------------------//
 
//-- johncmb fin--
   
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
        if(serviciosBean.getNpgroup()!=null){%>
     
      vServicio.addElement(new ServicioJS("<%=serviciosBean.getNpservicioid()%>","<%=serviciosBean.getNpnomserv()%>","<%=serviciosBean.getNpnomcorserv()%>","<%=MiUtil.getString(serviciosBean.getNpexcludingind())%>","<%=MiUtil.getString(serviciosBean.getNpgroup())%>"));                   
      <%}else{%>
      vServicio.addElement(new Servicio("<%=serviciosBean.getNpservicioid()%>","<%=serviciosBean.getNpnomserv()%>","<%=serviciosBean.getNpnomcorserv()%>","<%=MiUtil.getString(serviciosBean.getNpexcludingind())%>"));
      <%}
 
      %>
 // johncmb   fin -->  
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
 //  johncmb agregado  ----inicio
   if(cmbServiciosval!=""){
     var dato =salvaCmbFil_RecServOriginal();
   }
   //johncmb fin
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
                    //johncmb inicio
                    if(cmbServiciosval!=""){
                      salvaCmbOriginal_RecServFil();
                    }
                    // johncmb fin
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
                    //johncmb inicio
                    if(cmbServiciosval!=""){
                      salvaCmbOriginal_RecServFil();
                    }
                    // johncmb fin

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
       //johncmb inicio --->
       if(cmbServiciosval!=""){
        aTmpAvailableServiceFil=eliminaElemetoDeArray(aTmpAvailableServiceFil,dato);
        salvaCmbOriginal_RecServFil();
       }
       //johncmb fin --->
       return;
   } /* end _function addService*/
   
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
    if (objThis !=  null){
       txt_value_elimina = objThis.value;
    }else{
       txt_value_elimina = form.cmbSelectedServices.value;
    }
   // agregado por johncmb ----> inicio
    if(cmbServiciosval!=""){
     var dato =salvaCmbFil_RecServOriginal();
   }
   //fin johncmb -----> fin
    
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
                       //johncmb inicio
                       if(cmbServiciosval!=""){
                         fxObtenerServiciosFitrados();
                         salvaCmbOriginal_RecServFil();
                       }
                       //johncmb fin
                        alert("No se puede eliminar este Servicio de Arrendamiento."); 
                        return;
                     }else if(count_modify_new < 2 && objServicioArr.modify_new == 'S'){
                       //johncmb inicio
                       if(cmbServiciosval!=""){
                         fxObtenerServiciosFitrados();
                         salvaCmbOriginal_RecServFil();
                       }
                       //johncmb fin
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
                     //johncmb inicio
                     if(cmbServiciosval!=""){
                       fxObtenerServiciosFitrados();
                       salvaCmbOriginal_RecServFil();
                      }
                      //johncmb fin
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
          }
       };
    };

    if (txt_elimina_modify_new == "N"){      
       nameRol = getRol(form.cmbSelectedServices.value);
       if (nameRol!="" && valueExistsInArray(aRolesSelected,nameRol)){
          //johncmb inicio
         if(cmbServiciosval!=""){
           fxObtenerServiciosFitrados();
           salvaCmbOriginal_RecServFil();
          }
          //johncmb fin
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
               
               if (objServicioArr.active_new == "S"){
                  AddNewOption(form.cmbSelectedServices, objServicioArr.nameShort, txt_servicio_2 + txt_separacion + "x" + txt_separacion +" ");
               }else{
                  AddNewOption(form.cmbSelectedServices, objServicioArr.nameShort, txt_servicio_2 + txt_separacion + " " + txt_separacion +"x");
               }
            }
          }
      }
   }
   
    form.hdnFlagServicio.value = "1";
    bServiceModified = true;
    //johncmb inicio
    if(cmbServiciosval!=""){
      fxObtenerServiciosFitrados();
      salvaCmbOriginal_RecServFil();
    }
    //fin johncmb
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