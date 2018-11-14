<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.text.NumberFormat"%>
<%@ page import="pe.com.nextel.service.*" %>
<%@ page import="pe.com.nextel.bean.*" %>
<%
try{
   NewOrderService  objNewOrderService  = new NewOrderService();
   SessionService   objSession          = new SessionService();
   GeneralService   objGeneralService   = new GeneralService();
   EditOrderService objOrderService     = new EditOrderService();
   AutomatizacionService objAutomatService = new AutomatizacionService();
   NumberFormat formatter = new DecimalFormat("#0.00");     
   ProductBean objProdBean = new ProductBean();
   OrderBean   objOrderBean = null;
   Integer strGrupo = null;
   Constante constante=new Constante();

   Hashtable hshtinputNewSection = null;
   HashMap   hshSpecification = new HashMap();
   HashMap  hshData=null;
   HashMap hshScreenField=null;
   HashMap hshOrder=null;
	HashMap hshItemEditable=null;
   HashMap hshAutomat = null;
   ArrayList objItemHEader = new ArrayList();
   String    strCustomerId= "",
             strnpSite ="",
             strCodBSCS = "",
             hdnSpecification = "",
             strDivision ="",
             strOrderId  = "",
             strStatus   = "",
             strMessage  = "",
             strSiteOppId = "",
             strUnknwnSiteId = "",
             strSalesStuctOrigenId = "",
             strScheduleDate=null //<!-- jsalazar - modif hpptt # 1 - 29/09/2010 -->
             ;
            
   String    strSessionId=null;
   //String  strPaymentStatus = null;
   int   strValiditem=0;
   strGrupo = new Integer(0);
   //double    dPaymentTotal;
   String	strNumPaymentOrderId=""; //CEM COR0323
   String	strNumGuideNumber=""; 	 //CEM COR0426
   String   strDeleteItem="";
   String strFlagModality="";
  boolean	bolInboxConExcepcion=false;
  boolean bShowExceptionButton = false,        
             flgSA = false,
             flgIMEIS = false;
   SpecificationBean objSpecificationBean= null;
   long     lOrderId = 0; //DLAZO
   
   String strTypeDocument = ""; //PRY-0762 JQUISPE
   String strDocument = ""; //PRY-0762 JQUISPE
   boolean isRentaAdelantada = false; //PRY-0762 JQUISPE
   boolean isProrrateo = false; //PRY-0890 JBALCAZAR
   String strProrrateo = ""; //PRY-0890 JBALCAZAR
   String strOrderchildid = ""; //PRY-0890 JBALCAZAR
   String strOrderparentId = ""; // PRY-0890 JBALCAZAR 
   String strPaymentTotalProrrateo = ""; // PRY-0890 JBALCAZAR
   String strIsPostPago = ""; // PRY-0890 JBALCAZAR
   String strGeneratorType = ""; //JBALCAZAR PRY-1002
   String strRutaContext=request.getContextPath();
   String strURLApportionmentServlet =strRutaContext+"/apportionmentServlet";
   System.out.println("[EditOnDisplaySectionItems.jsp] apportionmentServlet: "+strURLApportionmentServlet);
   
   hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputEditSection");
   
   /*Obtener npvalue del Estado de ejecucion del Item*/ // JSALAZAR 01022011
   String strStatusItemExecution  = objGeneralService.getValue(Constante.ESTADO_EJECUCION_ITEM_NPTABLE, Constante.ESTADO_EJECUCION_ITEM_NPDESC);

   if ( hshtinputNewSection != null ) {
      strCustomerId           =   MiUtil.getString((String)hshtinputNewSection.get("strCustomerId"));
      strnpSite               =   MiUtil.getString((String)hshtinputNewSection.get("strSiteId"));
      System.out.println("Edit OnDisplaySectionItems strProrrateo---------------"+strnpSite); ///JBALCAZAR PRY-1002      
      strCodBSCS              =   MiUtil.getString((String)hshtinputNewSection.get("strCodBSCS"));
      hdnSpecification        =   MiUtil.getString((String)hshtinputNewSection.get("strSpecificationId"));
      strDivision             =   MiUtil.getString((String)hshtinputNewSection.get("strDivisionId"));
      strOrderId              =   MiUtil.getString((String)hshtinputNewSection.get("strOrderId"));
      strSessionId            =   MiUtil.getString((String)hshtinputNewSection.get("strSessionId"));
      strStatus               =   MiUtil.getString((String)hshtinputNewSection.get("strStatus"));  
      strSalesStuctOrigenId   =	  MiUtil.getString((String)hshtinputNewSection.get("strSalesStuctOrigenId"));
      
      
      strSiteOppId            =   MiUtil.getString((String)hshtinputNewSection.get("strSiteOppId")).equals("null")?"":MiUtil.getString((String)hshtinputNewSection.get("strSiteOppId"));
      strUnknwnSiteId         =   MiUtil.getString((String)hshtinputNewSection.get("strUnknwnSiteId")).equals("null")?"":MiUtil.getString((String)hshtinputNewSection.get("strUnknwnSiteId"));;
      //<!-- jsalazar - modif hpptt # 1 - 29/09/2010 - inicio-->
      if( hshtinputNewSection.get("strScheduleDate")!=null){
      strScheduleDate         =   MiUtil.getString((String)hshtinputNewSection.get("strScheduleDate"));
      System.out.println("Edit OnDisplaySectionItems strScheduleDate---------------"+strScheduleDate);
      }
      //<!-- jsalazar - modif hpptt # 1 - 29/09/2010 - fin-->
      
      lOrderId                =   MiUtil.parseLong((String)hshtinputNewSection.get("strOrderId")); //DLAZO
      
      strDocument         =   (String)hshtinputNewSection.get("strDocument"); //PRY-0762 JQUISPE
      strTypeDocument         =   (String)hshtinputNewSection.get("strTypeDocument"); //PRY-0762 JQUISPE
      System.out.println("Edit OnDisplaySectionItems strDocument---------------"+strDocument);
      System.out.println("Edit OnDisplaySectionItems strTypeDocument---------------"+strTypeDocument);      
      
      strGeneratorType         =   (String)hshtinputNewSection.get("strGeneratorType"); //JBALCAZAR PRY-1002      
      System.out.println("Edit OnDisplaySectionItems strGeneratorType---------------"+strGeneratorType);
      //PRY-0890 JBALCAZAR
      if( hshtinputNewSection.get("strProrrateo")!=null){
      strProrrateo         =   (String)hshtinputNewSection.get("strProrrateo");
      System.out.println("Edit OnDisplaySectionItems strProrrateo---------------"+strProrrateo);
      }

      if( hshtinputNewSection.get("strOrderchildid")!=null){
      strOrderchildid         =   (String)hshtinputNewSection.get("strOrderchildid");
      System.out.println("Edit OnDisplaySectionItems strOrderchildid---------------"+strOrderchildid);
      }      

      if( hshtinputNewSection.get("strOrderparentId")!=null){
    	  strOrderparentId         =   (String)hshtinputNewSection.get("strOrderparentId");
          System.out.println("Edit OnDisplaySectionItems strOrderparentId---------------"+strOrderparentId);
      }

      if( hshtinputNewSection.get("strPaymentTotalProrrateo")!=null){
    	  strPaymentTotalProrrateo         =   (String)hshtinputNewSection.get("strPaymentTotalProrrateo");
          System.out.println("Edit OnDisplaySectionItems strPaymentTotalProrrateo---------------"+strPaymentTotalProrrateo);
      }       
                                 
      objItemHEader           =   objNewOrderService.ItemDAOgetHeaderSpecGrp(MiUtil.parseInt(hdnSpecification));
      
      //JQUISPE PRY-0762 Renta Adelantada Inicio
      HashMap hshRentaAdelantada = objNewOrderService.getOrdenRentaAdelantada(lOrderId);
      strMessage = (String)hshRentaAdelantada.get("strMessage");
      if (strMessage!=null){
    	  throw new Exception(strMessage);
      }
      
      OrderRentaAdelantadaBean objOrderRentaAdelantadaBean = (OrderRentaAdelantadaBean)hshRentaAdelantada.get("objOrderRentaAdelantadaBean");
      if(objOrderRentaAdelantadaBean.getNpOrderRefRentaAdelantadaId() != 0){
    	  isRentaAdelantada = true;
      }
      //JQUISPE PRY-0762 Renta Adelantada Fin
      
      request.setAttribute("objItemHEader",objItemHEader);
      request.setAttribute("strDivisionId",strDivision);
      
		/*Autor: 	RDELOSREYES
			Motivo: Se emplea para saber cual es la página invocada, la cual se usa en el JSP objItemImeiService.jsp.
			Fecha:	29/01/2008
		*/
		request.setAttribute("pageSource", Constante.PAGE_ORDER_EDIT);
		request.setAttribute("CategoriaId", hdnSpecification);
		//Validar aquellos inbox en los cuales son editable los items
		//así la orden este cancelada o se haya generado factura/guía
      hshItemEditable=objGeneralService.getValueNpTable(Constante.EXCEP_INBOX_ITEM_EDITABLE);
      strMessage=(String)hshItemEditable.get("strMessage");
      if (strMessage!=null)
         throw new Exception(strMessage);
		ArrayList     objArrayItemEditable = (ArrayList)hshItemEditable.get("objArrayList");
		if (objArrayItemEditable.size()!=0){
			//bolInboxConExcepcion=objArrayItemEditable.contains(strStatus);
      bolInboxConExcepcion=MiUtil.contentStringInArrayListOfNpTableBean(objArrayItemEditable,strStatus); 
		}
		System.out.println("bolInboxConExcepcion:"+bolInboxConExcepcion);
      //Inicio Manejo dinamico de controles   		
      hshData=objOrderService.getOrderScreenField(MiUtil.parseLong(strOrderId),Constante.PAGE_ORDER_EDIT);
      strMessage=(String)hshData.get("strMessage");
      if (strMessage!=null)
         throw new Exception(strMessage);  
         
    hshScreenField= (HashMap)hshData.get("hshData");
		System.out.println("[EditOnDisplaySectionItems.jsp]");
		System.out.println("strStatus: "+strStatus);
		System.out.println("newitems: "+(String)hshScreenField.get("newitems"));
		System.out.println("edititems: "+(String)hshScreenField.get("edititems"));
		System.out.println("deleteitems: "+(String)hshScreenField.get("deleteitems"));
                System.out.println("strSalesStuctOrigenId: "+strSalesStuctOrigenId);
    
		strDeleteItem=(String)hshScreenField.get("deleteitems");
		System.out.println("******************************************************");
	  
      request.setAttribute("objScreenField",hshScreenField);
      //Fin Manejo dinámico de controles    
      
      hshSpecification = objGeneralService.getSpecificationDetail(MiUtil.parseLong(hdnSpecification));
      strMessage = (String)hshSpecification.get("strMessage");
      objSpecificationBean = (SpecificationBean)hshSpecification.get("objSpecifBean");
      
      strValiditem=objSpecificationBean.getNpValiditem();     
      
      if (!MiUtil.getString(strMessage).equals("")){%>
      <script>
        alert("<%=strMessage%>")
      </script>
      <%}else
      bShowExceptionButton = MiUtil.getString(objSpecificationBean.getNpExceptionDetail()).equals("S");

   }
             PortalSessionBean sessionUser  = objSession.getUserSession(strSessionId);
   
      	//JBALCAZAR PRY-1002               
          isProrrateo = (Boolean)objNewOrderService.getObtenerFlagActivoProrrateo(hdnSpecification,strTypeDocument ,strDocument,strGeneratorType,sessionUser.getLogin()); 
          System.out.println("[EditOnDisplaySectionItems.jsp] - isProrrateo:"+isProrrateo+" USERID:"+sessionUser.getLogin());  
          

          //[PRY-0710] Rol Modificacion de Producto EFLORES
          String screenOptionRolModProd = objGeneralService.getValue(Constante.ROL_USER_MOD_PROD_NPTABLE,Constante.ROL_USER_MOD_PROD_NPVALUEDESC_SCREEN_OPTION);
          int intFlagSpecificationModProd = objGeneralService.validateIfNpvalueIsInNptable(Constante.ROL_USER_MOD_PROD_NPTABLE,Constante.ROL_USER_MOD_PROD_NPVALUEDESC_SPECIFICATION,hdnSpecification);
          int intFlagInboxModProd = objGeneralService.validateIfNpvalueIsInNptable(Constante.ROL_USER_MOD_PROD_NPTABLE,Constante.ROL_USER_MOD_PROD_NPVALUEDESC_INBOX,strStatus);
   
          HashMap hshUserRolModProd = null;
          hshUserRolModProd = objGeneralService.getRol(MiUtil.parseInt(screenOptionRolModProd),sessionUser.getUserid(),sessionUser.getAppId());
          int intFlagUserRolModProd  = MiUtil.parseInt((String)hshUserRolModProd.get("iRetorno"));
          System.out.println("[PRY-0710]"+lOrderId+" El SCREENOPTIONROLMODPROD "+screenOptionRolModProd);
          System.out.println("[PRY-0710]"+lOrderId+" Permite Modificacion de Producto (0: No, 1: Si)"+intFlagUserRolModProd);
          System.out.println("[PRY-0710]"+lOrderId+" Specificacion : "+hdnSpecification+ " Permiso en specificacion : "+intFlagSpecificationModProd);
          System.out.println("[PRY-0710]"+lOrderId+" Status, : "+strStatus+" Permiso en inbox : "+intFlagInboxModProd);

          //[PRY-0710] Se verifica el Rol y el inbox de portabilidad
          if (intFlagSpecificationModProd == 1 && intFlagInboxModProd == 1 && intFlagUserRolModProd == 1){
              hshScreenField.put("edititems","Enabled");
              System.out.println("[PRY-0710]"+lOrderId+" Se cambia el status de edicion de items edititems : "+(String)hshScreenField.get("edititems"));
          }
   
  //RDELOSREYES - 28/10/2008 - Carga Masiva IMEI/SIM
  boolean bShowLinkUploadMassive = "Enabled".equals(MiUtil.getString((String)hshScreenField.get("loadMassiveImeiSim"))); //SCREENFIELD
  //boolean bShowLinkUploadMassive = true;  
  
  HashMap     objHashItemOrder     = objNewOrderService.ItemDAOgetItemOrder(MiUtil.parseLong(strOrderId));
  HashMap     hshProdBolsa         = objNewOrderService.getProductBolsa(MiUtil.parseLong(strCustomerId),MiUtil.parseLong(strnpSite));
  //INICIO CEM COR0323
  HashMap     hshPaymentOrderId    = objNewOrderService.ItemDAOhasPaymentOrderId(MiUtil.parseLong(strOrderId));
  strMessage 			= (String)hshPaymentOrderId.get("strMessage");
  
  //INICIO - DLAZO
  HashMap hshProcessType = objGeneralService.getProcessTypeByOrderId(lOrderId);
  String strProcessType = (String)(hshProcessType.get("strProcessType")==null?"":hshProcessType.get("strProcessType"));
  //FIN -DLAZO
  
  /*Datos de la orden*/
  /*hshOrder = objOrderService.getOrder(MiUtil.parseLong(strOrderId));
  strMessage=(String)hshOrder.get("strMessage");   
  if (strMessage!=null)
     throw new Exception(strMessage);                 
  objOrderBean=(OrderBean)hshOrder.get("objResume");
  dPaymentTotal = objOrderBean.getNpPaymentTotal();
  strPaymentStatus = objOrderBean.getNpPaymentStatus();
  */
  
  if ( hshPaymentOrderId.get("strMessage") != null ) {
  %>
  <script>alert("<%=hshPaymentOrderId.get("strMessage")%>")</script>
  <%}  
  strNumPaymentOrderId 	= (String)hshPaymentOrderId.get("strNumPaymentOrderId");														
  //INICIO CEM COR0426
  strNumGuideNumber		= (String)hshPaymentOrderId.get("strNumGuideNumber"); //CEM COR0426    
   
  if (!(strNumPaymentOrderId.equals(Constante.FIELD_ONLY_READ))){	 
	 hshtinputNewSection.put("strFieldReadOnly",MiUtil.getString(strNumPaymentOrderId));
  }
  else if (!(strNumGuideNumber.equals(Constante.FIELD_ONLY_READ))){	 
	 hshtinputNewSection.put("strFieldReadOnly",MiUtil.getString(strNumGuideNumber));
  }
  else {
	 hshtinputNewSection.put("strFieldReadOnly",Constante.FIELD_ONLY_READ);	 
  }
  //FIN CEM COR0426
  //FIN CEM COR0323  
  if ( objHashItemOrder.get("strMessage") != null ) {
  %>
  <script>alert("<%=objHashItemOrder.get("strMessage")%>")</script>
  <%}
  ArrayList objItemOrder = (ArrayList)objHashItemOrder.get("objArrayList");
  
   //LLamada al metodo validacion de inbox.
   //--------------------------------------
   hshAutomat = objAutomatService.getOrderSpecificationInbox(Long.parseLong(strOrderId));
   strMessage = (String)hshAutomat.get("strMessage");
   if (strMessage!=null){
   %>
     <script>alert("<%=hshAutomat.get("strMessage")%>")</script>
   <%
   }
   String strResult = (String)hshAutomat.get("strResult");
   
  //Volumen de Orden
  int volumeCount = 0;//Cantidad de items con promoción de volumen de orden
  volumeCount = objOrderService.getOrderVolumeCount(Integer.parseInt(strOrderId));

  //INICIO DERAZO REQ-0428
  PenaltyService penaltyService = new PenaltyService();
  HashMap hshMapResultFunct = penaltyService.getFlagShowPenaltyFunctionality(strOrderId, hdnSpecification, strStatus, sessionUser.getLogin());
  int flagPenaltyFunct = (Integer)hshMapResultFunct.get("flag");

  System.out.println("[EditOnDisplaySectionItems.jsp] flagPenaltyFunct: "+flagPenaltyFunct + "-strSpecification: "+hdnSpecification+"-strStatus:"+strStatus);

  ArrayList<ListBoxBean> msgList = (ArrayList<ListBoxBean>)hshMapResultFunct.get("msgList");

  if(msgList != null){
      System.out.println("[EditOnDisplaySectionItems.jsp] msgList size: "+msgList.size());
  }

  int flagShowPayPenalty = 0;

  if(flagPenaltyFunct == 1){
      //Validamos si se muestra o no el link para pagar penalidad
      HashMap hshMapResultSP = penaltyService.verifAddendumPenalty(Constante.OPTION_SHOW_PAY_PENALTY, strOrderId, "");
      flagShowPayPenalty = (Integer)hshMapResultSP.get("FLAG_AVANZAR");

      System.out.println("[EditOnDisplaySectionItems.jsp] flagShowPayPenalty: "+flagShowPayPenalty);
  }
  //FIN DERAZO REQ-0428
  %>
  
  <script defer>
  //DERAZO REQ-0428 Variable global para Pago de Penalidad
  <%
  if(hshScreenField.get("edititems") != null &&  "Enabled".equals(hshScreenField.get("edititems"))){
  %>
  var vform=document.frmdatos;
  vform.hdnFlagEditOrdenPort.value="Enabled";
  <%}%>
  var flagShowPayPenalty = <%=flagShowPayPenalty%>;
  var flagPenaltyFunct = <%=flagPenaltyFunct%>;
  var arrMsgPenaltyFunct = [];
  <%
  if (msgList != null){
      for (int i=0; i<msgList.size(); i++) {
         ListBoxBean listBoxBean = (ListBoxBean)msgList.get(i); %>
         arrMsgPenaltyFunct[<%=listBoxBean.getIdListBox()%>] = "<%= listBoxBean.getDescListBox() %>";
      <%
      }
  }%>
  //FIN DERAZO
  var vctItemHeaderOrder = new Vector();
  var vItemsBorradoItems = new Vector();
  var vflagEnabled = false;
  
  function fxSearchVectorElement(vector, element){
    for (i =0; i < vector.size() ; i++){
        var objVector = vector.elementAt(i);
        if (objVector.objectDesc == element)
          return i;
    }
    return -1;
   }
  
  function fxPreChangeItemNewDetailOrderEdit(object, strFilter){
    var form = document.frmdatos;
    var dispatchPlace = "";
    var position = fxSearchVectorElement(object, "strDispatchPlace");
    /*Obtiene el lugar de despacho.
    Si existe el combo Lugar de atención, toma el valor seleccionado, en caso contrario el valor
    del campo hdnLugarDespachoAux o la Tienda (para Reparaciones)*/
    try{
       if (form.cmbLugarAtencion!=null)
          dispatchPlace = form.cmbLugarAtencion[form.cmbLugarAtencion.selectedIndex].value;                              
       else if (form.hdnLugarDespachoAux!=null)
          dispatchPlace = form.hdnLugarDespachoAux.value;
       else if (form.cmbTienda!=null)
          dispatchPlace = form.cmbTienda.value;
       if (dispatchPlace == "") {
          alert("Seleccione un lugar de despacho");
          return;
       }
       if ( position == -1  )
          object.addElement( new fxMakeOrderObejct( "strDispatchPlace",dispatchPlace) );
       else
         object.setElementAt( new fxMakeOrderObejct( "strDispatchPlace",dispatchPlace), position );
    }catch(e){}
    
    // MMONTOYA [ADT-RCT-092 Roaming con corte]
    if (!validateProcessDate(<%=MiUtil.parseInt(hdnSpecification)%>)) {
        return;
    }
    
    if( strFilter == 'ADDIMEI' )
      fxChangeItemNewDetailOrderEditByLoadMasiveIMEIS(object);
    else
      fxChangeItemNewDetailOrderEdit(object);
  }
  
  
  function fxChangeItemNewDetailOrderEditByLoadMasiveIMEIS(vctrOrden) {
    var form = document.frmdatos;
    var subjObj = null;
    var urlPage = "?";
    
    try{       
      var result=fxValidateActionItem('A');//ACCION ELIMINAR
      if (result>0){
        return false;
      }
    }catch(e){
    }
    
    for( d = 0 ; d < vctrOrden.size(); d++ ){
      var objVector = vctrOrden.elementAt(d);
      urlPage = urlPage + objVector.objectDesc + "=" + objVector.objectValue + "&";
    }
        
    var frameUrl = "PopUpOrderLoadIMEI.jsp"+urlPage+"type_window=NEW&objTypeEvent=EDIT";
        
    var winUrl = appContext+"/DYNAMICSECTION/DynamicSectionItems/DynamicSectionItemsPage/PopUpOrderFrame.jsp?av_url="+escape(frameUrl);
    var popupWin = window.open(winUrl, "Orden_Item","status=yes, location=0, width=620, height=600, left=300, top=30, screenX=50, screenY=100");
  }
  
  //RDELOSREYES - 28/10/2008 - Carga Masiva IMEI/SIM
  function fxLoadUploadMassive() {
    strUrl = "<%=request.getContextPath()%>/DYNAMICSECTION/DynamicSectionItems/DynamicSectionItemsPage/PopUpUploadMassive.jsp?"+
              "hdnOrderId="+<%=strOrderId%>+"&cmbLugarAtencion="+document.frmdatos.cmbLugarAtencion.value;
    var winUrl = "<%=request.getContextPath()%>/DYNAMICSECTION/DynamicSectionItems/DynamicSectionItemsPage/PopUpOrderFrame.jsp?av_url="+escape(strUrl);
    window.open(winUrl,"Carga_Masiva_IMEI_SIM","toolbar=no,location=0,modal=yes,titlebar=0,directories=no,status=yes,menubar=0,scrollbars=no,screenX=100,top=80,left=100,screenY=80,width=900,height=500");
    //window.showModelessDialog(winUrl,"Carga_Masiva_IMEI_SIM","dialogWidth: 900px; dialogHeight: 500px; status: 0; help: 0");
  }
  
function fxSectionBolsaCreacionValidate(){
  return true; 
} 
  
  </script>
      
  <input type="hidden" name="hdnNumeroOrder" value="<%=strOrderId%>">
  <input type="hidden" name="hdnSessionLogin" value="<%=sessionUser.getLogin()%>">
  <input type="hidden" name="hdnFlagMassive" value="N"/>
  <input type="hidden" name="hdnTotalSalesPriceVEP" value="0">
  <input type="hidden" name="hdnItemSoluciones" >  <!-- las soluciones agregadas en el item  -->
  <input type="hidden" name="hdnBiometricAnular" value=""><!--  hiden anular OrdenBiometrica-->
  <input type="hidden" name="hdnFlagPenaltyFunct" value="<%=flagPenaltyFunct%>"><!--DERAZO REQ-0428 Se agrega hidden con flag-->
  <input type="hidden" name="hdnFlagEditOrdenPort" value="">
      
      <!--Botón Excepciones -->
      <table border="0" cellspacing="2" cellpadding="0" height="5" width="100%"><tr align="center">
      <%
      if (bShowExceptionButton){
      %>
        <td width="50%" align="left">
          <div id ="divException">
             <table border="0" cellspacing="2" cellpadding="0" height="5" width="99%" ><tr align="center"><td></td></tr></table>
             <table width="8%" border="0" cellspacing="2" cellpadding="0">
                <tr><td name="CellException"><a href="#" onclick="fxException();return false"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/btnException.gif" border="0"></a></td></tr>
             </table>
          </div>
        </td>
      <%}%>
      
      <%
      //RDELOSREYES - 28/10/2008 - Carga Masiva IMEI/SIM
      if (bShowLinkUploadMassive) {
      %>
      <!--RDELOSREYES - 28/10/2008 - Carga Masiva IMEI/SIM-->
      <td width="50%" align="right">
        <div id ="divLinkUploadMassive">
             <table border="0" cellspacing="2" cellpadding="0" height="5" width="99%" ><tr align="center"><td></td></tr></table>
             <table width="80%" border="0" cellspacing="2" cellpadding="0">
                <tr><td align="right"><a href="#" onclick="fxLoadUploadMassive();return false;"><font size="3" face="Arial"><b><i>Carga Masiva IMEI/SIM</i></b></font></a></td></tr>
             </table>
        </div>
      </td>
      <%
      }
      %>
      </tr></table>
      <!-- LINK PARA AGREGAR ITEMS NUEVOS-->
  
<TABLE height=5 cellSpacing=2 cellPadding=0 width="99%" border=0>
  <TBODY>
   <TR>
    <TD width="40%" align=left>    
      <div id="divItemCreate">
         <table id="item_crear" name="item_crear" width="4%" border="0" cellspacing="2" cellpadding="0">
         <tr align="center">
         
			<%if( "Enabled".equals(MiUtil.getString((String)hshScreenField.get("newitems"))) ){%>		 
            <td id="CellAddItem" name="CellAddItem" align="left" valign="middle" colspan="4" width="50%">&nbsp; 
               <!--JPEREZ: Se llama antes a una función para poder enviar el lugar de despacho -->
               <a href="javascript:fxPreChangeItemNewDetailOrderEdit(parent.mainFrame.vctParametersOrder,'ADDITEM');" > <!-- onmouseover=window.status='Agregar Item'; onmouseout=window.status=';>-->
               <img name="item_img_crear" ALT="Precio de Lista" src="<%=Constante.PATH_APPORDER_SERVER%>/images/crear.gif" border="no"></a>
            </td>
			<%} // CGC - COR0386%>
      
      <%if( (MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_NUEVA || MiUtil.parseInt(hdnSpecification) == Constante.SPEC_REPOSICION_PREPAGO_TDE || MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_TDE) && "Enabled".equals(MiUtil.getString((String)hshScreenField.get("newitems"))) ){%>		 
            <td id="CellAddItemLoadIMEI" name="CellAddItemLoadIMEI" align="left" valign="middle" colspan="4" >&nbsp; 
               <a href="javascript:fxPreChangeItemNewDetailOrderEdit(parent.mainFrame.vctParametersOrder,'ADDIMEI');" > <!-- onmouseover=window.status='Agregar Item'; onmouseout=window.status=';>-->
               <img name="item_img_crear_load_imei" ALT="Agregar ITEMS para Carga de SIM Masivo" src="<%=Constante.PATH_APPORDER_SERVER%>/images/add_reminder.gif" border="no"></a>
            </td>
      <%}%>
      
      <!--DERAZO: Se agrega link para pagar penalidad por adendas-->
      <td id="p_penalidad" style="display: none;" align="middle" valign="middle" colspan="85" width="50%">
         <a  style="font-size:84%;" href="javascript:fxPayPenalty();">Pagar penalidad por adendas</a>
      </td>
      
      <%if( "Enabled".equals(MiUtil.getString((String)hshScreenField.get("createdocumentrepair"))) ){%>
            <td width="50%">
              <table align="left">
                <tr>
                  <td align="right">
                    <input name="btnCreateDocRep" type="button" value="Generar Documento" onclick="javascript:fxGenerateOrderRepair();">
                  </td>
                </tr>
              </table>
            </td>
            
      <%}%>
      
      <%if( "Enabled".equals(MiUtil.getString((String)hshScreenField.get("ordervolume"))) ){%>
            <td width="50%">
              <table align="left">
                <tr>
                  <td align="right">
                    <input type="button" id="btnEvaluarVO" value="Evaluar Promoción por V.O." onClick="javascript: fxEvaluateVO();" disabled>
                  </td>
                </tr>
              </table>
            </td>
            
      <%}%>
      
         </tr>
         </table>
      </div>
      <div id="divItemCrear">
      </div>
 	</TD>
    <TD width="40%" align=right> 	       
	<div id="divBtnProrrateo" style="display:none">
    <table cellspacing="0" cellpadding="2" width="99%" align="center" border="0">
        <tbody>
            <tr valign="top">
                <td width="100%" align="center">
                    <table cellspacing="0" cellpadding="0" width="100%" border="0">
                        <tbody>
                            <tr>
                                <td width="27%" align="left">&nbsp;</td>
                                <td width="70%" align="right">
                                    <table cellspacing="0" cellpadding="0" width="42%" border="0">
                                     <tbody>
                                      <tr>
                                      <td width="40%"></td>
                                      <td width="45%">
                                      <input type="hidden" id="v_savePagoAnticipado"  name="v_savePagoAnticipado" value="0">
                                      <INPUT type="button" id="btnCalculoPagoAnticipado" name="btnCalculoPagoAnticipado" onclick="ShowCalculateApportionment();" value="Calcular monto anticipado">
                                      </td>
                                      </tr>
                                      </tbody>
                                    </table>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
        </tbody>
    </table>
	</div>
    </TD>
    </TR>
   </TBODY>
</TABLE>	  	
       
      <INPUT type="hidden" name="hdnObjetType" value="ORDER">      
      <INPUT type="hidden" name="hdnPerformance_flag_period" value="n">
      <input type="hidden" name="hdnFlagItemImeis" value="0">
      <input type="hidden" name="hdnFlagItemSims" value="0">
      
      <input type="hidden" name="hdn_item_imei_grupo" value="<%=strGrupo%>"> <!-- Hidden que indica el # de grupos de item_pedido ingresados -->
      <input type="hidden" name="hdn_item_imei_selecc" value=""> <!-- Hidden que guarda el # del item_imei a trabajar -->
      
      <input type="hidden" name="hdn_validitem" value="<%=strValiditem%>"> <!--Hidden que envia valor npvaliditem del procedimiento SP_GET_CATEGORY_DETAIL -->
       
      <INPUT type="hidden" name="hdnItemSelected">       
      <input type="hidden" name="hdnItemSoluciones" >  <!-- las soluciones agregadas en el item  -->
      <%
          ItemBean itemBean = null; 
          if( objItemOrder!=null && objItemOrder.size()>0 ){
            for(int i=0; i<objItemOrder.size(); i++ ){
              itemBean  = new ItemBean();
              itemBean  = (ItemBean)objItemOrder.get(i);
              strFlagModality=MiUtil.getString(itemBean.getNpmodalitysell());
              
              //Se agregó para mantener la Modalidad Propio no solo cuando el item de salida propio sea el último item de la lista
              //Necesario para la visualización de los campos Nuevo Numero y Estado de Operación de Automatización de Ventas
              if(MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA ||
                  MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_NUEVA ||
                  MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_TDE   ||
                  MiUtil.parseInt(hdnSpecification) == Constante.SPEC_REPOSICION_PREPAGO_TDE){//Se agrego subcategoria reposicion prepago tde - TDECONV034
                                
                if( ( Constante.MODALITY_PROPIO.equals(strFlagModality)) || (strFlagModality.equals("Alquiler en Cliente"))){
                
                  break;
                }
              }
            }
          }
      %> 
      
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
                                    <input type="hidden" name="hdnItemRequeridoFlag" value="0">
                                    <input type="hidden" name="hdnItemBorrados" value="">
                                    <table id="items_table" border="0" cellpadding="0" cellspacing="1" width="100%" >
                                       <!-- Aquí debe de codificarse la invocación de los procedures para traer la metadata
                                            de los campos
                                       -->  
                                       <tr>
                                          <td align="center" class="CellLabel">&nbsp;&nbsp;&nbsp;</td>
                                          <%
                                          
                                          if( objItemHEader != null && objItemHEader.size() > 0 ){
                                          
                                          NpObjHeaderSpecgrp objnpObjHeaderSpecgrp = null;
                                          String strStyleLabel = "";
                                          for ( int i=0 ; i<objItemHEader.size(); i++ ){
                                          String valueVisible = "";
                                          objnpObjHeaderSpecgrp = new NpObjHeaderSpecgrp();
                                          
                                          objnpObjHeaderSpecgrp =  (NpObjHeaderSpecgrp)objItemHEader.get(i);
                                          if( objnpObjHeaderSpecgrp.getNpobjitemheaderid()==25 )
                                                  flgSA = true;
                                          if( objnpObjHeaderSpecgrp.getNpobjitemheaderid()==72 )
                                                  flgIMEIS = true;
                                          
                                           if ( objnpObjHeaderSpecgrp.getNpdisplay().equals("N") ){
                                           //No muestro
                                              valueVisible = "style='display:none'";
                                            }  else{
                                                  //Expeciones para no visualizar un campo a pesar que la configuración indique que  se debe visualizar 
                                                   /*if( (MiUtil.parseInt(hdnSpecification) == Constante.SPEC_CAMBIO_MODELO || 
                                                          MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA ||
                                                          MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_NUEVA )
                                                          && (objnpObjHeaderSpecgrp.getNpobjitemheaderid()==58  )                                                          
                                                          && ( !strFlagModality.equals(Constante.MODALITY_PROPIO) && !strFlagModality.equals("Alquiler en Cliente") )                                                             
                                                    )                                                      
                                                          valueVisible = "style='display:none'"; 
                                                    */
                                                  if( (MiUtil.parseInt(hdnSpecification) == Constante.SPEC_CAMBIO_MODELO     || 
                                                          MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA ||
                                                          MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_NUEVA  ||
                                                          MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_TDE    || 
                                                          MiUtil.parseInt(hdnSpecification) == Constante.SPEC_REPOSICION_PREPAGO_TDE) 
                                                          //Se agrego subcategoria reposicion prepago tde - TDECONV034
                                                          && ( objnpObjHeaderSpecgrp.getNpobjitemheaderid()==96 )
                                                          && (!"s".equals(strResult) ||                                                                                                                                                                               
                                                          (!strFlagModality.equals(Constante.MODALITY_PROPIO) && !strFlagModality.equals("Alquiler en Cliente") ) )                                                          
                                                    )                                                       
                                                          valueVisible = "style='display:none'";                                                          
                                                  //INICIO - DLAZO
                                                  if(strProcessType.equals(Constante.TIPO_ORDEN_INTERNA_DEACTIVATE)){
                                                    if(objnpObjHeaderSpecgrp.getNpobjitemheaderid()==125 && (!strStatus.equals(Constante.INBOX_PROCESOS_AUTOMATICOS) && 
                                                        !strStatus.equals(Constante.INBOX_ACTIVACION_PROGRAMACION) && !strStatus.equals(Constante.INBOX_CALLCENTER))){ //Fecha de desactivacion
                                                          valueVisible = "style='display:none'";
                                                    }
                                                  }else{
                                                    if(objnpObjHeaderSpecgrp.getNpobjitemheaderid()==125)
                                                      valueVisible = "style='display:none'";
                                                  }//FIN - DLAZO
                                                  
                                                  //JQUISPE PRY-0762
                                                  if(!isRentaAdelantada){
                                                  	if("txtTotalRA".equals(objnpObjHeaderSpecgrp.getNphtmlname()) ||
                                                       "txtCantidadRA".equals(objnpObjHeaderSpecgrp.getNphtmlname())){
                                                  		 valueVisible = "style='display:none'";
                                                  		 objnpObjHeaderSpecgrp.setNpdisplay("N");
                                           }
                                                  }
                                                  
                                                  //PRY-0890 JBALCAZAR
                                                  if(!isProrrateo){
                                                  	if("txtMontoProrrateo".equals(objnpObjHeaderSpecgrp.getNphtmlname())){
                                                      		 valueVisible = "style='display:none'";
                                                      		 objnpObjHeaderSpecgrp.setNpdisplay("N");
                                                           }                                                    	
                                                  }                                                  
                                           }
                                          %>
                                          
                                          <%
                                           if(  (MiUtil.parseInt(hdnSpecification) == Constante.SPEC_CAMBIO_MODELO ||
                                                MiUtil.parseInt(hdnSpecification) == Constante.SPEC_REPOSICION
                                                ) && (objnpObjHeaderSpecgrp.getNpobjitemheaderid() == 12  ||
																		objnpObjHeaderSpecgrp.getNpobjitemheaderid() == 52 )
                                            ){%>
                                          <td align="center" class="CellLabel" <%=valueVisible%> >&nbsp;<b><font style="color: rgb(190, 0, 0);" > <%=objnpObjHeaderSpecgrp.getNpobjitemname()%></font></b>&nbsp;</td>
                                            <%}else{%>
                                          <td id="Cabecera<%=objnpObjHeaderSpecgrp.getNpobjitemheaderid()%>" align="center" class="CellLabel" <%=valueVisible%> >&nbsp;<%=objnpObjHeaderSpecgrp.getNpobjitemname()%>&nbsp;</td>
                                          <%}
                                          %>

                                          <%}%>
                                          <td align="center" class="CellLabel">&nbsp;Eliminar&nbsp;</td>
                                          <%}%>
                                          
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
         </div>
  <table border="0" cellspacing="2" cellpadding="0" height="5" width="99%">
         <tr align="center">
            <td></td>
         </tr>
      </table>
      
      <!-- Totales de todos los items -->
      <div id="divLeyendaTotal">
         <table border="0" width="99%" cellpadding="2" cellspacing="0" align="center">
            <tr valign="top">
               <td width="100%" align="center">
                  <table id="tabLeyendaTotal" width="100%" border="0" cellspacing="0" cellpadding="0">
                     <tr>
                        <td width="47%" align="left">&nbsp;</td>
                        <td width="50%" align="right">
                           <table border="0" width="42%" cellspacing="0" cellpadding="0"  CLASS="RegionBorder" >
                              <tr>
                                 <td align="center" width="50%" class="CellNbg"><b>Total PEN</b></td><!--FES Se cambió US$ por PEN -->
                                 <td align="right" width="50%" class="CellNbg">
                                    <input type="hidden" name="wn_items" value="">
                                    <input type="text" name="txtTotalSalesPrice" value="" size="8" style="text-align:right" readOnly >
                                 </td>
                              </tr>
                           </table>
                           <table id="TotalVOSalesPrice" border="0" width="42%" cellspacing="0" cellpadding="0" class="RegionBorder" style='display:none'>
                              <tr>
                                 <td align="center" width="50%" class="CellNbg"><b>Total V.O. PEN</b></td><!--FES Se cambió US$ por PEN -->
                                 <td align="right" width="50%" class="CellNbg">
                                    <input type="text" name="txtTotalVOSalesPrice" value="" size="8" style="text-align:right" readOnly >
                                 </td>
                              </tr>
                           </table>
                        </td>
                        <td width="3%" align="right">&nbsp;</td>
                     </tr>
                  </table>
               </td>
            </tr>
         </table>
      </div>   
  
<!--INICIO JBALCAZAR--> 
<div id="divProrrateo" style="DISPLAY: none">
    <table cellspacing="0" cellpadding="2" width="99%" align="center" border="0">
        <tbody>
            <tr valign="top">
                <td width="100%" align="center">
                    <table cellspacing="0" cellpadding="0" width="100%" border="0">
                        <tbody>
                            <tr>
                                <td width="27%" align="left">
                                <div id="itemsProrrateo" name="itemsProrrateo" style="DISPLAY: none">
                                </div>
                                </td>
                                <td width="70%" align="right">
                                <div style="WIDTH: 50%">
								 <fieldset class="RegionBorder"> 
								 <legend class="CellNbg" align="center">Pago Anticipado</legend>                                
                                    <table  cellspacing="0" cellpadding="0" width="100%" border="0">
                                        <tbody>
                                            <tr>
                                                <td class="CellNbg" width="60%" align="center" colspan="4"><b>¿Aplica pago anticipado de prorrateo?</b></td>
                                            </tr>
                                            <tr>
                                                <td width="40%" align="center">&nbsp;</td>
                                                <td class="CellNbg" width="10%">
                                                    <INPUT type="radio" id="textApportionmentS"  value="S" name="textApportionment" onclick="comprobarProrrateo(this.value);">Si</td>
                                                <td class="CellNbg" width="10%">
                                                    <INPUT type="radio" id="textApportionmentN"  value="N" name="textApportionment" onclick="comprobarProrrateo(this.value);" >No</td>
													<INPUT type="hidden" id="hdnApportionment" name="hdnApportionment" value ="<%=strProrrateo%>"/>
													<INPUT type="hidden" id="hdnIsApportionment" name="hdnIsApportionment" value ="<%=isProrrateo%>"/>
													<INPUT type="hidden" id="hdnIsClientPostPago" name="hdnIsClientPostPago" value="" />
                                                <td width="40%" align="center">&nbsp;</td>												
                                            </tr>
                                            <tr>
                                                <td width="40%" align="right">
                                                </td>
                                                <td width="10%" align="right">
                                                </td>

                                                <td class="CellLabel" width="40%" align="center">#Ord. Pago Anticipado</td>
                                                <td width="10%" align="center"></td>
                                            </tr>
                                            <tr>

                                                <td width="40%" align="rigth">
                                                    <table width="95%" class="RegionBorder" border="0" cellspacing="0" cellpadding="0">
                                                        <tbody>
                                                            <tr>
                                                                <td class="CellNbg" width="90%" align="rigth"><b>Total Pago Anticipado</b>
                                                                </td>
                                                                <td class="CellNbg" width="50%" align="right">                                                                    
                                                                    <input type="text"  id="txtTotalPriceApportionment" name="txtTotalPriceApportionment" value ="<%=strPaymentTotalProrrateo%>" size="8" style="text-align:right;" readOnly >
                                                                </td>

                                                            </tr>
                                                        </tbody>
                                                    </table>
                                                </td>
                                                <td width="10%" align="rigth"></td>
                                                <td width="40%" class="CellContent" align="center">
                                                    <input id="txtOrderProrrateo" name="txtOrderProrrateo" value ="<%=strOrderchildid%>"  style="TEXT-ALIGN: right" readonly size="25">
                                                </td>
                                                <td width="10%" align="center"></td>
                                            </tr>
                                            <tr>
                                                <td width="60%" align="center" colspan="4">
                                                   <b><span id="spnMsjProrrateo" name="spnMsjProrrateo"></span></b>                                                                                                     
                                                </td>
                                            </tr>                                            
                                        </tbody>                                    
                                    </table>
                                    </fieldset>
                                    </div>
									<br>
                                                    <table width="50%" class="RegionBorder" border="0" cellspacing="0" cellpadding="0">
                                                        <tbody>
                                                            <tr>
                                                                <td width="90%	" align="rigth" class="CellNbg"><b>Monto Total (Equipo + Pago Anticipado)</b>
                                                                </td>
                                                                <td width="50%" align="right" class="CellNbg">
                                                                    <input type="text" id="txtTotalPrice" name="txtTotalPrice"  value="" size="25" style="text-align:right;" readOnly>
                                                                </td>

                                                            </tr>
                                                        </tbody>
                                                    </table>

                                </td>
                                <td width="3%" align="right">&nbsp;</td> 
                            </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
        </tbody>
    </table>    
</div>
            
<!--FIN JBALCAZAR-->
  
  <%if(MiUtil.parseInt(hdnSpecification) == Constante.SPEC_SSAA_SUSCRIPCIONES){%>
    <jsp:include page="../../htdocs/jsp/controls/objItemSubscription.jsp" flush="true" />
  <%}else if(MiUtil.parseInt(hdnSpecification) == Constante.SPEC_SSAA_PROMOTIONS){%>
    <jsp:include page="../../htdocs/jsp/controls/objItemPromotion.jsp" flush="true" />
  <%}else if(MiUtil.parseInt(hdnSpecification) != Constante.SPEC_ACTIVAR_PAQUETES_ROAMING){%> <!-- MMONTOYA [ADT-RCT-092 Roaming con corte] -->
    <jsp:include page="../../htdocs/jsp/controls/objItemImeiService.jsp" flush="true" />
  <%}%>
  
  <script language="javascript" DEFER>
  var appContext = "<%=request.getContextPath()%>";
  var vctItemsMainFrameOrder = new Vector();
  var vctItemsMainImei       = new Vector();
  var vctParametersOrder     = new Vector();
  
  var vIdModelos              = new Vector();
  
  function fxSectionNameFinalStatus(){
    return true;
  }
  
  </script>
  
<SCRIPT DEFER>
/*Se agrego los atributos contractNumber y RequestId para el requerimientos N_O000039261
 * Autor: jcastillo@practiaconsulting.com
   Fecha: 25/02/2014
 */
function fxMakeItemImei2(objImeiId, objImeiSubId, objImeiNum, objImeiItem, objImei, objSim, objProduct, objProductId, objPlan, objBad, objCheck, objModality, objWarrant, objNumTel, objErrorEstOp, objItemDeviceId, objContractNumber, objRequestId){
    this.objImeiId          =   objImeiId;
    this.objImeiSubId       =   objImeiSubId;
    this.objImeiNum         =   objImeiNum;
    this.objImeiItem        =   objImeiItem;
    this.objImei            =   objImei;
    this.objSim             =   objSim;
    this.objProduct         =   objProduct;
    this.objProductId       =   objProductId;
    this.objPlan            =   objPlan;
    this.objBad             =   objBad;
    this.objCheck           =   objCheck;
    this.objModality        =   objModality;
    this.objWarrant         =   objWarrant;
    this.objNumTel          =   objNumTel;
    this.objErrorEstOp      =   objErrorEstOp;
    this.objItemDeviceId    =   objItemDeviceId;
    this.objContractNumber  =   objContractNumber;
    this.objRequestId	=   objRequestId;
}

  //Invocación de Pop-up Excepciones  
  function fxException(){    
    var specid = "<%=hdnSpecification%>";
    //se cambio el screen field "items" por "edititems" 	 
    <%if ("Enabled".equals(MiUtil.getString((String)hshScreenField.get("edititems")))){%>
			if ( (dPaymentTotal > 0) && (strPaymentStatus.toUpperCase() == "CANCELADO" )){
				strAction = "VIEW";  
			}else{
				strAction = "EDIT";
			}
    <%}else{%>
        strAction = "VIEW";
    <%}%>	 
    fxEditException("<%=strSessionId%>", strAction, specid);        
  }
  
  function CargaService(objTable){
  CargaServiciosItem(objTable.parentNode.parentNode.parentNode.rowIndex);
  }
  
  function fxGetRowIndex(objTable){
    try{
      var index_table = objTable.parentNode.parentNode.parentNode.rowIndex;
    }catch(e){
      var index_table = objTable.parentNode.rowIndex;
    }
    return index_table;
  }
  
  function fxChangeItemVector(objTable,objVctItemsMainFrameOrder){
      ChangeItemEditDetailOrder(objTable,objVctItemsMainFrameOrder);
  }
  
  function fxaddElementsItemsMain(vctItemOrderMain){
    var vctItemsAuxOrder = new Vector();  
    
    var strcadena = "";
    
    //alert("vctItemOrderMain : " + vctItemOrderMain.size() ) ;
  
    for( x = 0; x < vctItemOrderMain.size(); x++ ){       
        var objMake = new fxMakeOrderItem(
        "'"+vctItemOrderMain.elementAt(x).npobjitemheaderid+"'",
        "'"+vctItemOrderMain.elementAt(x).npobjspecgrpid+"'",
        "'"+vctItemOrderMain.elementAt(x).npobjitemid+"'",
        "'"+vctItemOrderMain.elementAt(x).npobjitemname+"'",
        "'"+vctItemOrderMain.elementAt(x).namehtmlheader+"'",
        "'"+vctItemOrderMain.elementAt(x).namehtmlitem+"'",
        "'"+vctItemOrderMain.elementAt(x).npcontroltype+"'",
        "'"+vctItemOrderMain.elementAt(x).npdefaultvalue+"'",
        "'"+vctItemOrderMain.elementAt(x).npsourceprogram+"'",
        "'"+vctItemOrderMain.elementAt(x).npspecificationgrpid+"'",
        "'"+vctItemOrderMain.elementAt(x).npdisplay+"'",
        "'"+vctItemOrderMain.elementAt(x).npobjreadonly+"'",
        "'"+vctItemOrderMain.elementAt(x).npobjitemvalue+"'",
        "'"+vctItemOrderMain.elementAt(x).npobjitemvaluedesc+"'"
        );
        
        vctItemsAuxOrder.addElement(objMake);
			
    }
  
    vctItemsMainFrameOrder.addElement(vctItemsAuxOrder);
  }
  
  
  /*
  Developer: Lee Rosales
  Objetivo : Carga los IMEIS que vienen de la Base de Datos
  */
  function fxLoadImeisDB(){
  <%
  ArrayList objArrayList = objNewOrderService.ItemDAOgetItemDeviceOrder(MiUtil.parseLong(strOrderId));  
  ItemDeviceBean itemDeviceBean = null;
  int countAux = 1;
  long lngItemId   = 0;
  
  if( objArrayList != null && objArrayList.size() > 0 ){
  
    for( int i=0; i<objArrayList.size(); i++){
        itemDeviceBean = (ItemDeviceBean)objArrayList.get(i);
        
        if( lngItemId == itemDeviceBean.getNpitemid() )
          countAux++;
        else
          countAux = 1;
        lngItemId      =  itemDeviceBean.getNpitemid();
  
  %>
  
  var vctItemsImei       = new Vector();
  
 vctItemsImei.addElement(  
  new fxMakeItemImei2( <%=itemDeviceBean.getNpcantidad()%>,<%=itemDeviceBean.getNpcantidad()%>,<%=itemDeviceBean.getNpcantidad()%> + "-" + "<%=(countAux)%>" , 
                     "<%=itemDeviceBean.getNpitemid()%>",  "<%=MiUtil.getString(itemDeviceBean.getNpimeinumber())%>",  
                     "<%=MiUtil.getString(itemDeviceBean.getNpsimnumber())%>",  "<%=MiUtil.getString(itemDeviceBean.getNpproductname())%>",  
                     "<%=itemDeviceBean.getNpproductid()%>",  "<%=MiUtil.getString(itemDeviceBean.getNpplanname())%>",  
                     "<%=MiUtil.getString(itemDeviceBean.getNpbadimei())%>", "<%=MiUtil.getString(itemDeviceBean.getNpcheckimei())%>",
                     "<%=MiUtil.getString(itemDeviceBean.getNpmodality())%>","<%=MiUtil.getString(itemDeviceBean.getNpwarrant())%>",
                     "<%=MiUtil.getString(itemDeviceBean.getNpphone())%>","<%=MiUtil.getString(itemDeviceBean.getNperrorautom())%>",
                     "<%=MiUtil.getString(itemDeviceBean.getNpitemdeviceid())%>","<%=itemDeviceBean.getNpcontractnumber()%>",
                     "<%=MiUtil.getString(itemDeviceBean.getNprequestid()+"")%>"));
                     //Adiciono el phone, errorAutom, itemDeviceid    
  
  fxAddRowTableImeis( "table_imeis" , vctItemsImei.elementAt(0) , 9 , -1);
  
  vctItemsMainImei.addElement(vctItemsImei);
  <% }%>
  <%}%>
  
  }
  
  </script>
  
  <script>
    var wn_flag_sales = 0;   // flag que se activa para obtener la lista de vendedores una vez por busqueda      
    var deleteItemEnabled = true;  //permite eliminar un item, si es false no.
    var promptWindow; 
  </script>
  
  <script DEFER>
  
  
  /*Se Modifica Para la Carga de Servicios a nivel de cabecera <ITEM_NEW>*/
  function fxCargaServiciosItemItem(objTable){   
   var form = document.frmdatos;
   var strCategoryId = '<%=MiUtil.parseInt(hdnSpecification)%>';
   var strCustomerId = '<%=MiUtil.parseInt(strCustomerId)%>';
   var lorderId      = <%=lOrderId%>;
   var servicios_item_send = "";
   var phonenumber = "";
   var count_items     = getNumRows("items_table");
   var oindexRow       = objTable;
   var currentIndex    = oindexRow - 1;
   
   if( strCategoryId == <%=Constante.SPEC_TRASLADO%> && strDivisionId==<%=Constante.KN_BANDA_ANCHA%> ){
    try{
      var strAddressId = "";
      
      if(count_items < 2){
        strAddressId = form.hdnItemValuetxtItemAddressInstall.value;
      }else{
        strAddressId = form.hdnItemValuetxtItemAddressInstall[currentIndex].value;
      }
      
      var param="<%=request.getContextPath()%>/pages/loadContractByAddress.jsp?strAddressId="+strAddressId+"&strCustomerId="+strCustomerId;
      var url="<%=request.getContextPath()%>/pages/loadFrameContract.jsp?av_url="+escape(param);
      window.open(url ,"WindowContractByAddress","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=no,screenX=60,top=40,left=60,screenY=40,width=520,height=200");

    }catch(e){
    
    }
   
   }else{
   
   try{
     if(strCategoryId == <%=Constante.SPEC_SSAA_SUSCRIPCIONES%>){
      form.hdnItemSelectSubscription.value =  oindexRow;
      servicios_item = oindexRow - 1;
    }else if(strCategoryId == <%=Constante.SPEC_SSAA_PROMOTIONS%>){
      form.hdnItemSelectPromotion.value =  oindexRow;
      servicios_item = oindexRow - 1;
    }else{
     form.hdnItemSelectService.value =  oindexRow;
     servicios_item = oindexRow - 1;
    }
     
     //Servicios
     if(count_items < 2){
        servicios_item_send = form.item_services.value;
     }else{
        servicios_item_send = form.item_services[servicios_item].value;
     }
     
     //Telefonos
     if(count_items < 2){
        if(form.hdnItemValuetxtItemPhone == undefined){
            phonenumber = 0;
        }else{
            phonenumber = form.hdnItemValuetxtItemPhone.value;
        }
     }else{
        if(form.hdnItemValuetxtItemPhone == undefined){
            phonenumber = 0;
        }else{
            phonenumber = form.hdnItemValuetxtItemPhone[servicios_item].value;
        }
     }
     
     //Planes
     if(count_items < 2){
        if(form.hdnItemValuetxtItemRatePlan == undefined){
            //
            if(form.hdnItemValuetxtItemNewRatePlan == undefined){
              strPlanId = 0;
            }else{
              strPlanId = form.hdnItemValuetxtItemNewRatePlanId.value;
            }
        }else{
            strPlanId = form.hdnItemValuetxtItemRatePlan.value;
        }
     }else{
        if(form.hdnItemValuetxtItemRatePlan == undefined){
            if(form.hdnItemValuetxtItemNewRatePlan == undefined){
              strPlanId = 0;
            }else{
              strPlanId = form.hdnItemValuetxtItemNewRatePlanId[servicios_item].value;
            }
        }else{
            strPlanId = form.hdnItemValuetxtItemRatePlan[servicios_item].value;
        }
     }
     
     //Solución
     if(count_items < 2){
        if(form.hdnItemValuetxtItemSolution == undefined){
            strSolutionId = 0;
        }else{
            strSolutionId = form.hdnItemValuetxtItemSolution.value;
        }
     }else{
        if(form.hdnItemValuetxtItemSolution == undefined){
            strSolutionId = 0;
        }else{
            strSolutionId = form.hdnItemValuetxtItemSolution[servicios_item].value;
        }
     }
     //alert("strPlanId: "+strPlanId+" - "+"strSolutionId: "+strSolutionId);
     var a=appContext+"/serviceservlet?myaction=loadServiceItems&servicios_item="+servicios_item_send+"&hdnSpecification=<%=hdnSpecification%>&cmb_ItemPlanTarifario="+strPlanId+"&cmb_ItemSolution="+strSolutionId+"&phonenumber="+phonenumber+"&lorderId="+lorderId+"";     
     form.action = a;
     form.submit();
   }catch(e){
     alert("Para esta categoría no se aplican Servicios Adicionales");
   }
   }
   
  }

  //INICIO DERAZO REQ-0428
  function fxPayPenalty() {
      var phones = "";
      var vctItemsOrder = parent.mainFrame.vctItemsMainFrameOrder;
      for( i = 0 ; i < vctItemsOrder.size();  i ++ ) {
          var vctAuxRead = vctItemsOrder.elementAt(i);
          for( j = 0 ; j < vctAuxRead.size();  j ++ ) {
              var objAuxRead = vctAuxRead.elementAt(j);

              if(objAuxRead.npobjitemheaderid == "'3'"){
                  var strTelefono = replace(objAuxRead.npobjitemvalue,"'","");
                  phones = phones+strTelefono+"|";
              }
          }
      }

      if(phones != ""){
          phones = phones.substring(0, phones.length-1);
      }
      var url = "<%=request.getContextPath()%>/htdocs/jsp/controls/POPUP/popUpPagarPenalidad.jsp?phones="+phones+"&customerId=<%=strCustomerId%>&strOrderId="+"<%=strOrderId%>&hdnUserId=<%=strSessionId%>&nroRuc="+document.frmdatos.txtRucDisabled.value;
      WinAsist = window.open(url,"PagarPenalidad","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=yes,screenX=100,top=80,left=150,screenY=80,width=1000,height=500,modal=yes");
  }
  //FIN DERAZO

  //<!-- jsalazar - modif hpptt # 1 - 29/09/2010 -inicio-->
  // Retorna verdadero si el la fecha strFechaF es mayor que la fecha year_I
  // de lo contrario retorna falso
    function comparaFechas(strFechaI,strFechaF){
        //Fecha Actual            
        var wv_fProceso = new Date();
        var day_I    = parseFloat(strFechaI.substring(0,2));
        var month_I  = parseFloat(strFechaI.substring(3,5));
        var year_I   = parseFloat(strFechaI.substring(6,10));

        //Fecha Proceso        
        var day_F  = parseFloat(strFechaF.substring(0,2));
        var month_F  = parseFloat(strFechaF.substring(3,5));
        var year_F   = parseFloat(strFechaF.substring(6,10));

          if (year_F < year_I){
             return false;
          };
          if (month_F < month_I){
             return false;
          };
          if (day_F <= day_I){
             return false;
          };
          
          return true;
   }
  //<!-- jsalazar - modif hpptt # 1 - 29/09/2010 -fin-->
  
  function fxAssignmentBillingAcount(rowIndex,vctAuxItemsMain,vctAuxParameters){
      
   var servicios_item_send = "";
   form = document.frmdatos;
   /* Guardamos el numero del Item que se visualiza sus servicios */
   try{
      form.hdnItemSelectService.value =  rowIndex;
      servicios_item = rowIndex - 1;
      if (servicios_item == 0)
         if (form.item_services[servicios_item] == null)
            servicios_item_send = form.item_services.value;
         else
            servicios_item_send = form.item_services[servicios_item].value;
      else
         servicios_item_send = form.item_services[servicios_item].value;
      
      var a="<%=request.getContextPath()%>/orderservlet?myaction=loadAssignmentBillingAccount&servicios_item="+servicios_item_send;
      a = a + "&txtCompanyId="+form.txtCompanyId.value+"&rowIndex="+rowIndex;
      
      try{
      var responsablePagoValue = form.cmbResponsablePago.value;
      var responsablePagoDesc  = form.cmbResponsablePago.options[form.cmbResponsablePago.selectedIndex].text;
      
      a = a + "&responsablePagoValue="+responsablePagoValue;
      a = a + "&responsablePagoDesc="+responsablePagoDesc;
      }catch(e){}
      
      if( vctItemsMainFrameOrder.size() == 1 ) 
         a = a + "&item_billingAccount="+form.item_billingAccount.value;
      else
         a = a + "&item_billingAccount="+form.item_billingAccount[rowIndex - 1].value;
      
      //alert(a);
      var winUrl = "<%=request.getContextPath()%>/DYNAMICSECTION/DynamicSectionItems/DynamicSectionItemsPage/PopUpOrderFrame.jsp?av_url="+escape(a);
      var popupWin = window.open(winUrl, "Orden_Item_Assignment","status=yes, location=0, width=730, height=450, left=300, top=30, screenX=50, screenY=100");
    }catch(e){
     alert("Para esta categoría no se requiere configurar Billing Account");
    }         
  }
  
  function fxTransferVectorItems(vctItemOrderMain,aux_index){
  
  var vctItemsAuxOrder = new Vector();  
    
    var strcadena = "";
    
    for( x = 0; x < vctItemOrderMain.size(); x++ ){
        
        var objMake = new fxMakeOrderItem(
        "'"+vctItemOrderMain.elementAt(x).npobjitemheaderid+"'",
        "'"+vctItemOrderMain.elementAt(x).npobjspecgrpid+"'",
        "'"+vctItemOrderMain.elementAt(x).npobjitemid+"'",
        "'"+vctItemOrderMain.elementAt(x).npobjitemname+"'",
        "'"+vctItemOrderMain.elementAt(x).namehtmlheader+"'",
        "'"+vctItemOrderMain.elementAt(x).namehtmlitem+"'",
        "'"+vctItemOrderMain.elementAt(x).npcontroltype+"'",
        "'"+vctItemOrderMain.elementAt(x).npdefaultvalue+"'",
        "'"+vctItemOrderMain.elementAt(x).npsourceprogram+"'",
        "'"+vctItemOrderMain.elementAt(x).npspecificationgrpid+"'",
        "'"+vctItemOrderMain.elementAt(x).npdisplay+"'",
        "'"+vctItemOrderMain.elementAt(x).npobjreadonly+"'",
        "'"+vctItemOrderMain.elementAt(x).npobjitemvalue+"'",
        "'"+vctItemOrderMain.elementAt(x).npobjitemvaluedesc+"'"
        );
        
        vctItemsAuxOrder.addElement(objMake);
        
    } 
    
    vctItemsMainFrameOrder.setElementAt(vctItemsAuxOrder,aux_index);
  }
  
  function fxMakeOrderObejct(objectDesc,objectValue){
        this.objectDesc         =   objectDesc;
        this.objectValue        =   objectValue;
  }
  
  function fxDeleteItemEdit(CellObject,item_id,item_current,v_showDeleteButton){
   //v_showDeleteButton   0: Consulta    1: Editable 
   var form = document.frmdatos;
   var item_pedido_numero = CellObject.parentNode.parentNode.parentNode.rowIndex;
   
	/*Valida si la orden ya fue cancelada en CAJA y no se refresco la pantalla de la orden
	  (aun le apareceria como Pendiente)
	 */
    try{
      var result=fxValidateActionItem('E');      
      if (result> 0){        
        //alert("La orden ya fue cancelada (Refresque su pantalla). No se puede eliminar el item");
        return false;        
      }
    }
    catch(e){}
   /**
   Comentado por conveniencia
   **/
   if (v_showDeleteButton==0){
      alert("No puede eliminar el item");
      return;
   }
   //alert("item_pedido_numero : " + item_pedido_numero)
   var item_promo_header_id = CellObject.parentNode.parentNode.childNodes.item(3).getAttribute("value");

      if (confirm("Desea eliminar el item?")){
         if (deleteItemEnabled){
            var table = document.all ? document.all["items_table"]:document.getElementById("items_table");
            
            try{
            //Volumen de Orden
            <%if( "Enabled".equals(MiUtil.getString((String)hshScreenField.get("ordervolume"))) ){%>
              form.txtTotalVOSalesPrice.value = "";
              if(table.rows <= 2){
                document.getElementById("btnEvaluarVO").disabled = true;
              }
              else{
                //alert("Volver a Evaluar Promocion por Volumen de Orden");
                if (form.chkVepFlag != undefined){
                   if (form.chkVepFlag.checked){
                      document.getElementById("btnEvaluarVO").disabled = true;
                   }else{
                      document.getElementById("btnEvaluarVO").disabled = false;
                      document.getElementById("btnEvaluarVO").focus();
                   }
                }else{
                document.getElementById("btnEvaluarVO").disabled = false;
                document.getElementById("btnEvaluarVO").focus();
                }
                
                if(aplicoVO != "0"){
                  aplicoVO = "2";
                }
              }
            <%}%>
            
            }catch(e){}
            
            wb_existItem=(wn_items>=1)?false:wb_existItem;
            wb_existItem=(wn_items>2)?true:false;
            
            wn_items=(table.rows.length-1);
            //alert("Cantidad de Items : " + wn_items + " item_id : " + item_id)
            var itemIdTable = "";
            
            if( wn_items == 1 ){
               itemIdTable = form.hdnItemId.value;
               if( undefined == itemIdTable ){
                try{
                  itemIdTable = form.hdnItemId[0].value;
                }catch(e){}
               }
            }else{
               itemIdTable = form.hdnItemId[item_id-1].value;
            }

            if (item_id > 0)
               vItemsBorradoItems.addElement(itemIdTable);
            
            table.deleteRow(CellObject.parentNode.parentNode.parentNode.rowIndex);
            try{
            DeleteItemImeis(CellObject.parentNode.parentNode.parentNode.rowIndex);
            }catch(e){}
            
            //alert("Cantidad de Items Antes    : " + vctItemsMainFrameOrder.size() + " item_id : " + item_id);
            //var deleteIndex = item_id-1;
            vctItemsMainFrameOrder.removeElementAt(item_id);
            
            //alert("Cantidad de Items Después  : " + vctItemsMainFrameOrder.size() );  
            
            /*if (parseInt(document.frmdatos.hdnItemSelectService.value) == parseInt(item_pedido_numero) ){
               deleteAllValues(document.frmdatos.cmbSelectedServices);
            }*/
            
            vIdModelos.removeElementAt(item_current);
            
            try{
              /*Eliminar los IMEIS*/
               countImeis = table_imeis.rows.length;

               for( x = 0,u = 0; x <countImeis; u++){
                  var rowTable = table_imeis.rows[x];
                  if( item_current == rowTable.id ){
                      table_imeis.deleteRow(x);
                      countImeis = table_imeis.rows.length;
                      continue;
                  }else{
                    x++;
                  }
               }
             }catch(e){}
             
             fxCalculateTotal();
             
             //PRY-0890 JBALCAZAR
             try{
              	$("#items_table tr td div input[name='txtMontoProrrateo']").each(function () {
            		$(this).val("");
                });            	 
             }catch(e){}             
             
         }else {
            alert("No puede eliminar, cierre antes la ventana de edición de Items");
         };
         
      }   
  }
  
  //function fxValidatePaymentStatus(){
  function fxValidateActionItem(operacion){
    var sPermitir;
    var url="hdnNumeroOrder="+"<%=strOrderId%>"+"&operacion="+operacion+"&estadoPagoActual="+strPaymentStatus.toUpperCase();        
    var parametros = fxRequestXML("generalServlet","requestActionItem",url);
    
    if (parametros ==null ){      
      alert("Error al realizar la operación");
      return sPermitir=2; //codigo de error en la ejecución 
    }
    else{
      var iPermitir;
      var token=parametros.indexOf("*");
      sPermitir=parametros.substring(0,token);
      var sMensaje=parametros.substring(token+1,parametros.length);      
      iPermitir=parseInt(sPermitir);
      if (iPermitir>0){
        alert(sMensaje); // mensaje de error
      }      
      return iPermitir;  
    }      
  }
  
   function fxChangeItemEditDetailOrder(item_index,vctItemsMainFrameOrder,vctrAuxOrden) { 
   var vctItemsEdit = new Vector();
   var vctItemsEditAux = new Vector();
   var form = document.frmdatos;
   var num_rows = getNumRows("items_table");
   var vctAuxRead = new Vector();
  
   vctAuxRead = vctItemsMainFrameOrder.elementAt(item_index-1);
   
   form.hdnItemSelected.value = item_index-1;
   //alert("vctAuxRead : " + vctAuxRead.size())
  
   var strUrlItemHeaderId   = "";
   var strUrlItemDesc       = "";
   var strUrlItemValue      = "";
   
     for( i = 0 ; i < vctAuxRead.size();  i ++ ) {
       var objAuxRead = vctAuxRead.elementAt(i);
       //alert("Valor : |" + objAuxRead.npobjitemheaderid+"|");  
       
       <% for ( int i=0 ; i<objItemHEader.size(); i++ ){
        NpObjHeaderSpecgrp objnpObjHeSpec = new NpObjHeaderSpecgrp();
          objnpObjHeSpec =  (NpObjHeaderSpecgrp)objItemHEader.get(i);%>
          
        
          if( replace(objAuxRead.npobjitemheaderid,"'","") =="<%=objnpObjHeSpec.getNpobjitemheaderid()%>"){              
              //strUrlItemHeaderId += "a='"+replace(objAuxRead.npobjitemheaderid,"'","")+"'&";
              //strUrlItemHeaderId += "b='"+replace(objAuxRead.npobjitemvalue,"'","")+"'&";              
              strUrlItemHeaderId += "a="+replace(objAuxRead.npobjitemheaderid,"'","")+"&";
              strUrlItemHeaderId += "b="+replace(replace(objAuxRead.npobjitemvalue,"'",""),"%","<%=Constante.CONVERT_PORCENT%>")+"&";              
              continue;
          }
      <%}%>
     }

   /*JPEREZ: Se agrega el lugar de despacho de la orden */
   var strDispatchPlace = "";     
   try{
      if (form.cmbLugarAtencion!=null)
         strDispatchPlace = form.cmbLugarAtencion[form.cmbLugarAtencion.selectedIndex].value;            
      else if (form.hdnLugarDespachoAux!=null)
        strDispatchPlace = form.hdnLugarDespachoAux.value;            
      else if (form.cmbTienda!=null)          
          strDispatchPlace = form.cmbTienda[form.cmbTienda.selectedIndex].value;
      if (strDispatchPlace == ""){
        alert ("Debe seleccionar un lugar de despacho 1");
        return;
      }    
   }catch(e){}   
   var position = fxSearchVectorElement(vctrAuxOrden, "strDispatchPlace");  
   if (position==-1){
      vctrAuxOrden.addElement( new fxMakeOrderObejct( "strDispatchPlace",strDispatchPlace) );
   }else
      vctrAuxOrden.setElementAt( new fxMakeOrderObejct( "strDispatchPlace",strDispatchPlace), position );                      
   
   var urlPage = "&";
      for( d = 0 ; d < vctrAuxOrden.size(); d++ ){
       var objVector = vctrAuxOrden.elementAt(d);        
        urlPage = urlPage + objVector.objectDesc + "=" + objVector.objectValue + "&";
      }
      
      var itemId = 0;
      if( num_rows == 1 ) itemId = form.hdnItemId.value;
      else  itemId = form.hdnItemId[item_index-1].value;

      //var result=fxValidatePaymentStatus();   
      <%

        //[PRY-0710] Se fuerza a la edicion en caso el rol tenga el permiso especifico de modificacion de producto
      if (intFlagSpecificationModProd == 1 && intFlagInboxModProd == 1 && intFlagUserRolModProd == 1){
      %>
                var result = 0;
       <%
              }else{%>
      var result=fxValidateActionItem('M');
<% } %>
      var type_window='';      
      if (result==0){
        type_window	= "EDIT"
      }
      else{
        type_window	= "DETAIL"
        //alert("La orden ya fue cancelada (Refresque su pantalla). El item se mostrará en modo consulta");
      }                  
      //<!-- jsalazar - modif hpptt # 1 - 29/09/2010 - inicio-->      
       var today='<%=MiUtil.getDate(MiUtil.getDatePlSql(),"dd/MM/yyyy")%>';
       var res=false;
      <% if(strScheduleDate!=null 
      && new Integer(hdnSpecification).intValue() == Constante.SPEC_SERVICIOS_ADICIONALES[0]
     // && Constante.INBOX_PROCESOS_AUTOMATICOS.equals(strStatus)
       ){ %> 
       var varScheduleDate= '<%=strScheduleDate%>';
       res=comparaFechas(varScheduleDate,today);
       <%}%>
       if(res){
       type_window="DETAIL";
       }
      //<!-- jsalazar - modif hpptt # 1 - 29/09/2010 - fin-->
      
      
      if(document.forms[0].chkVepFlag != null && document.forms[0].cmbVepNumCuotas!= null) {
        urlPage = urlPage + "strnpnumcuotas" + "=" + document.forms[0].cmbVepNumCuotas.value   + "&";
        urlPage = urlPage +  "strflagvep" + "=" + document.forms[0].chkVepFlag.value + "&"
      }
      
      if (document.forms[0].chkVepFlag !=undefined){
         if (document.forms[0].chkVepFlag.checked == true && document.forms[0].cmbVepNumCuotas.value== ""){
            alert("Debe seleccionar el número de cuotas de venta a plazos antes de agregar los ítems");
            return;
         }
      }      
      
      var frameUrl = appContext+"/DYNAMICSECTION/DynamicSectionItems/DynamicSectionItemsPage/PopUpOrder.jsp?type_window="+type_window+"&objTypeEvent=EDIT&strItemId="+itemId+urlPage+strUrlItemHeaderId+"&item_index="+(item_index-1);
       //[PRY-0710] Se agrega parametros get con los permisos para usarlos en el popup
       <% if (intFlagSpecificationModProd == 1 && intFlagInboxModProd == 1 && intFlagUserRolModProd == 1){ %>
            frameUrl = frameUrl + "&strModProd=1";
       <% }%>
      var winUrl = appContext+"/DYNAMICSECTION/DynamicSectionItems/DynamicSectionItemsPage/PopUpOrderFrame.jsp?av_url="+escape(frameUrl);            
      //RDELOSREYES - 20/08/2008 - Redimension del Popup - Incidencia #5112
      var popupWin = window.open(winUrl, "Orden_Item","status=yes, location=0, width=550, height=600, left=200, top=30, screenX=50, screenY=100");
  }
  
   function fxChangeItemEditDetailOrderDetail(item_index,vctItemsMainFrameOrder,vctrAuxOrden) {
   
   var vctItemsEdit = new Vector();
   var vctItemsEditAux = new Vector();
   var form = document.frmdatos;
   var num_rows = getNumRows("items_table");
   var vctAuxRead = new Vector();
   
   vctAuxRead = vctItemsMainFrameOrder.elementAt(item_index-1);
   form.hdnItemSelected.value = item_index-1;
   
   var strUrlItemHeaderId   = "";
   var strUrlItemDesc       = "";
   var strUrlItemValue      = "";
   
     for( i = 0 ; i < vctAuxRead.size();  i ++ ) {
       var objAuxRead = vctAuxRead.elementAt(i);
      
    <% for ( int i=0 ; i<objItemHEader.size(); i++ ){
       NpObjHeaderSpecgrp objnpObjHeSpec = new NpObjHeaderSpecgrp();
        objnpObjHeSpec =  (NpObjHeaderSpecgrp)objItemHEader.get(i);%>
        
       if( replace(objAuxRead.npobjitemheaderid,"'","") =="<%=objnpObjHeSpec.getNpobjitemheaderid()%>"){
          strUrlItemHeaderId += "a="+replace(objAuxRead.npobjitemheaderid,"'","")+"&";
          //strUrlItemHeaderId += "b="+replace(objAuxRead.npobjitemvalue,"'","")+"&";
          strUrlItemHeaderId += "b="+replace(replace(objAuxRead.npobjitemvalue,"'",""),"%","<%=Constante.CONVERT_PORCENT%>")+"&";              
        }
        
    <%}%>
    
    }

   /*JPEREZ: Se agrega el lugar de despacho de la orden */
   var strDispatchPlace = "";
   try{
      if (form.cmbLugarAtencion!=null)
         strDispatchPlace = form.cmbLugarAtencion[form.cmbLugarAtencion.selectedIndex].value;            
      else if (form.hdnLugarDespachoAux!=null)
         strDispatchPlace = form.hdnLugarDespachoAux.value;
      else if (form.cmbTienda!=null)
          strDispatchPlace = form.cmbTienda[form.cmbTienda.selectedIndex].value;   
      if (strDispatchPlace == ""){
        alert ("Debe seleccionar un lugar de despacho");
        return;
      }
   }catch(e){}
   var position = fxSearchVectorElement(vctrAuxOrden, "strDispatchPlace");  
   if (position==-1){
      vctrAuxOrden.addElement( new fxMakeOrderObejct( "strDispatchPlace",strDispatchPlace) );
   }else
      vctrAuxOrden.setElementAt( new fxMakeOrderObejct( "strDispatchPlace",strDispatchPlace), position );                       
          
   var urlPage = "&";
      for( d = 0 ; d < vctrAuxOrden.size(); d++ ){
       var objVector = vctrAuxOrden.elementAt(d);
        
        urlPage = urlPage + objVector.objectDesc + "=" + objVector.objectValue + "&";
      }
      
      var itemId = 0;
      if( num_rows == 1 ) itemId = form.hdnItemId.value;
      else  itemId = form.hdnItemId[item_index-1].value;
                      
      var frameUrl = appContext+"/DYNAMICSECTION/DynamicSectionItems/DynamicSectionItemsPage/PopUpOrder.jsp?type_window=DETAIL&objTypeEvent=EDIT&strItemId="+itemId+urlPage+strUrlItemHeaderId+"&item_index="+(item_index-1);
      
      var winUrl = appContext+"/DYNAMICSECTION/DynamicSectionItems/DynamicSectionItemsPage/PopUpOrderFrame.jsp?av_url="+escape(frameUrl);
		//RDELOSREYES - 20/08/2008 - Redimension del Popup - Incidencia #5112
      var popupWin = window.open(winUrl, "Orden_Item","status=yes, location=0, width=550, height=600, left=200, top=30, screenX=50, screenY=100");
   }
   
    /*jsalazar - modif hpptt # 1 - 29/09/2010 - inicio*/
  
  function fxItemListFailedExecution(item_index,vctItemsMainFrameOrder,vctrAuxOrden) {   
   var vctItemsEdit = new Vector();
   var vctItemsEditAux = new Vector();
   var form = document.frmdatos;
   var num_rows = getNumRows("items_table");
   var vctAuxRead = new Vector();

   vctAuxRead = vctItemsMainFrameOrder.elementAt(item_index-1);          
   form.hdnItemSelected.value = item_index-1;   
  
   var strUrlItemHeaderId   = "";
   var strUrlItemDesc       = "";
   var strUrlItemValue      = "";
   var strTelefono          = "";
   
   
     for( i = 0 ; i < vctAuxRead.size();  i ++ ) {
       var objAuxRead = vctAuxRead.elementAt(i);
       
       <% for ( int i=0 ; i<objItemHEader.size(); i++ ){
        NpObjHeaderSpecgrp objnpObjHeSpec = new NpObjHeaderSpecgrp();
          objnpObjHeSpec =  (NpObjHeaderSpecgrp)objItemHEader.get(i);%>
          
          if( replace(objAuxRead.npobjitemheaderid,"'","") =="<%=objnpObjHeSpec.getNpobjitemheaderid()%>"){
              strUrlItemHeaderId += "a="+replace(objAuxRead.npobjitemheaderid,"'","")+"&";
              strUrlItemHeaderId += "b="+replace(replace(objAuxRead.npobjitemvalue,"'",""),"%","<%=Constante.CONVERT_PORCENT%>")+"&";
              if( objAuxRead.npobjitemheaderid == "'3'"){
                strTelefono = replace(objAuxRead.npobjitemvalue,"'","");
              }
              continue;
          }
      <%}%>
     }
     
   var urlPage = "&";
      for( d = 0 ; d < vctrAuxOrden.size(); d++ ){
       var objVector = vctrAuxOrden.elementAt(d);
        
        urlPage = urlPage + objVector.objectDesc + "=" + objVector.objectValue + "&";
      }
      
      var orderid = form.hdnNumeroOrder.value;
      var itemId = 0;
      if( num_rows == 1 ) itemId = form.hdnItemId.value;
      else  itemId = form.hdnItemId[item_index-1].value;
      
      var frameUrl = appContext+"/DYNAMICSECTION/DynamicSectionItems/DynamicSectionItemsPage/PopUpOrderPendingExecution.jsp?type_window=DETAIL&objTypeEvent=EDIT&strOrderId="+orderid+"&strItemId="+itemId+"&strTelefono="+strTelefono+urlPage+strUrlItemHeaderId+"&item_index="+(item_index-1);
      var winUrl = appContext+"/DYNAMICSECTION/DynamicSectionItems/DynamicSectionItemsPage/PopUpOrderFrame.jsp?av_url="+escape(frameUrl);
      var popupWin = window.open(winUrl, "Orden_Item","status=yes, location=0, width=900, height=350, left=200, top=30, screenX=50, screenY=100");
  }
  
  /*jsalazar - modif hpptt # 1 - 29/09/2010 - fin*/
   
  function fxGenerateOrderRepair() {
      var vForm = document.frmdatos;
      if( items_table.rows.length < 2 ){
         alert("Debe agregar al menos un Item");
         return false;
      }
      if (form.hdnChangedOrder.value=="S"){
			alert("La orden ha sido modificada, primero deberá grabarla y luego generar los comprobantes");
         return;
      }
      vForm.target = "bottomFrame";
      vForm.btnCreateDocRep.disabled = true;        
      vForm.action= "<%=request.getContextPath()%>/repairServlet?hdnMethod=generateDocumentRepair";
      vForm.submit();     
	}
  </script>
  
  <script DEFER>
  //Invoca al PopUp para la edición de un Item
  function fxAddRowOrderItemsGlobalEdit(vctItemOrder){      
    fxaddElementsItemsMain(vctItemOrder);
    form = parent.mainFrame.document.frmdatos;
    var index_arg = 1;
    var elemText = "";
    var cantElement;
    //GGRANADOS Por el momento luego cambiar por Constante.ITEM_DEVICE_PROPIO
    var ITEM_DEVICE_PROPIO = 0;
    
    var valuevisible = "";
    var descvisible = "";
    var typeControl = "";
    var cell;
    
    var contentHidden = "";
    var numPaymentOrderId= "<%=strNumPaymentOrderId%>"; //CEM COR0323
    var numGuideNumber= "<%=strNumGuideNumber%>"; 		//CEM COR0426
    cantElement = vctItemOrder.size();
    
    var flagvalida96 = "N";//Debido a que en la fxLoadData() se usa 2 veces el codigo 96
    
    try{
      //Volumen de Orden
      <%if( "Enabled".equals(MiUtil.getString((String)hshScreenField.get("ordervolume"))) ){%>
        if(itemsCargados){
          //alert("Volver a Evaluar Promocion por Volumen de Orden");
          if (form.chkVepFlag != undefined){
             if (form.chkVepFlag.checked){
                document.getElementById("btnEvaluarVO").disabled = true;
             }else{
          document.getElementById("btnEvaluarVO").disabled = false;
             }
          }else{
             document.getElementById("btnEvaluarVO").disabled = false;
          }
          
          form.txtTotalVOSalesPrice.value = "";
          if(aplicoVO == "1" || aplicoVO == "2"){
            aplicoVO = "2";
          }
          if(aplicoVO == "3"){
            aplicoVO = "4";
          }
        }
      <%}%>
    }catch(e){}
    
        if (cantElement > 1) {/* los argumentos pasados.. a partir del segundo. */
          
          var row   = items_table.insertRow(-1);
          
          elemText =  "";
          var new_grupo = parseInt(form.hdn_item_imei_grupo.value) + parseInt(1);
          form.hdn_item_imei_grupo.value = new_grupo;
          
          // Inicio CGC - COR0386
          var v_pagpermitida;          
          var v_bolpermit; 
		    var v_delpermit;
          <%
			 			 
          if ("Enabled".equals(MiUtil.getString((String)hshScreenField.get("edititems")))){
            System.out.println("[PRY-0710] Orden "+lOrderId+" Puede Editar los items");
          %> // CGC - COR0386

				var bolInboxConExcepcion="<%=bolInboxConExcepcion%>";
				if (bolInboxConExcepcion=="true"){		
					v_pagpermitida="fxChangeItemEditDetailOrder";
					v_bolpermit=1; // Edición
					v_delpermit="<%=strDeleteItem%>";				
				}
				else{
					if ( (dPaymentTotal > 0) && (strPaymentStatus.toUpperCase() == "CANCELADO" )){					
                        <%
                          //PRY-0710 Agrega rol para modificacion de producto
                        if (intFlagSpecificationModProd == 1 && intFlagInboxModProd == 1 && intFlagUserRolModProd == 1)  {
                         %>
                            v_pagpermitida="fxChangeItemEditDetailOrder";
                            v_bolpermit=1; // Edición
                            v_delpermit="<%=strDeleteItem%>";
                        <% }else{ %>
						v_pagpermitida="fxChangeItemEditDetailOrderDetail";          
						v_bolpermit=2; // Consulta
						v_delpermit="<%=strDeleteItem%>";        				
                        <% } %>

					}else{
						v_pagpermitida="fxChangeItemEditDetailOrder";
						v_bolpermit=1; // Edición
						v_delpermit="<%=strDeleteItem%>";
				}		
           }         
         <%}else{
            System.out.println("[PRY-0710] Orden "+lOrderId+" NO Puede Editar los items");
         %>
            v_pagpermitida="fxChangeItemEditDetailOrderDetail";          
            v_bolpermit=2; // Consulta
			   v_delpermit="<%=strDeleteItem%>";
          <%}%>
          // Fin CGC - COR0386
          
          /*Agrego la primera línea*/
          var cellPrinc = row.insertCell(index_arg - 1);
          elemText      = "<div id=\'contTable\' align='center' class='CellContent' >"+
                          "<input type=\'radio\' name=\'item_chek\' onclick=\'javascript:fxCargaServiciosItemItem(fxGetRowIndex(this));\'>"+
                          "<a href=\'javascript:;\' onclick=\'javascript:" + v_pagpermitida + "(fxGetRowIndex(this),parent.mainFrame.vctItemsMainFrameOrder,parent.mainFrame.vctParametersOrder);\' ALT=\'Editar Item\'><font color=\'brown\' size=\'2\' face=\'Arial\' ><b>"+ form.hdn_item_imei_grupo.value +"</b></font></a> "+
                          "<a href=\'javascript:;\' onclick=\'javascript:fxAssignmentBillingAcount(fxGetRowIndex(this),parent.mainFrame.vctItemsMainFrameOrder,parent.mainFrame.vctParametersOrder);\' ALT=\'Editar Item\'>Billing</a> "+
                          "<input type=\'hidden\' name='hdnIndice' value="+ document.frmdatos.hdn_item_imei_grupo.value +" >" +
                          "<input type=\'hidden\' name='hdnFlagSave' value='"+ vctItemOrder.elementAt(0).npobjitemflagsave +"' >" +
                          "<input type=\'hidden\' name='hdnItemId' value='"+ vctItemOrder.elementAt(0).npitemid +"' >" +
                          "</div>";
          //alert("Agregando un Item : " + vctItemOrder.elementAt(0).npitemid);
          cellPrinc.innerHTML = elemText;
          
          index_arg++;
          
          elemText = "";
          
          
          for( i = 0; i < vctItemHeaderOrder.size(); i++ ){
            
            var objItemHeader = vctItemHeaderOrder.elementAt(i);            
            var valFlag = false;
            //Siempre mostrar Text y si coinciden asignar el Valor
              for( j = 0; j < vctItemOrder.size(); j++ ){

              var objItem = vctItemOrder.elementAt(j);
              
              //alert("[i][" + i + "][j]["+j+"] -> ( objItemHeader " + objItemHeader.npobjitemheaderid + " == objItem " + objItem.npobjitemheaderid + " ) -> " + objItem.nphtmlname );
                <%
                if( MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA || MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PORTABILIDAD_POSTPAGO){
                %>
                  if(objItemHeader.npobjitemheaderid == 126){
                    cell = row.insertCell(index_arg - 1);
                    cell.innerHTML =  "<div align='center'class='CellContent'>" + 
                                      "<input type='text' name='"+ trim(objItemHeader.nphtmlname) + document.frmdatos.hdn_item_imei_grupo.value +"' size='12' style='text-align:right' readOnly>" +
                                      "<input type='hidden' name='hdnRentVO" + + document.frmdatos.hdn_item_imei_grupo.value + "'>" + 
                                      "<input type='hidden' name='hdnPromoIdVO" + + document.frmdatos.hdn_item_imei_grupo.value + "'></div>";
                    cell.id = "Celda"+objItemHeader.npobjitemheaderid+document.frmdatos.hdn_item_imei_grupo.value;
                    if(aplicoVO == "0" || aplicoVO == "3" || aplicoVO == "4"){
                      cell.style.display = "none";
                    }                    
                    index_arg++;
                    break;
                  }
                  if(objItemHeader.npobjitemheaderid == 127){
                    cell = row.insertCell(index_arg - 1);
                    cell.innerHTML = "<div align='center'class='CellContent'><input type='checkbox' name='"+ trim(objItemHeader.nphtmlname) + document.frmdatos.hdn_item_imei_grupo.value+ "' onclick='javascript: fxCalculateTotalVO();' disabled></div>";
                    cell.id = "Celda"+objItemHeader.npobjitemheaderid+document.frmdatos.hdn_item_imei_grupo.value;
                    if(aplicoVO == "0" || aplicoVO == "3" || aplicoVO == "4"){
                      cell.style.display = "none";
                    }
                    index_arg++;
                    break;
                  }
                <%
                }
                %>
              
                if ( objItemHeader.npobjitemheaderid == objItem.npobjitemheaderid ) {
                
                    valuevisible = " value='"+ objItem.npobjitemvalue + "' ";
                    descvisible  = " value='"+ objItem.npobjitemvaluedesc + "' ";
                    //alert("valuevisible " + valuevisible);
                    //alert("descvisible " + descvisible);
                    //INICIO - DLAZO
                    if(objItemHeader.npobjitemheaderid==125){
                      if("<%=strProcessType%>" == "<%=Constante.TIPO_ORDEN_INTERNA_DEACTIVATE%>"){
                        if(("<%=strStatus%>" != "<%=Constante.INBOX_PROCESOS_AUTOMATICOS%>" && "<%=strStatus%>" != "<%=Constante.INBOX_ACTIVACION_PROGRAMACION%>" && "<%=strStatus%>" != "<%=Constante.INBOX_CALLCENTER%>")){
                          objItemHeader.npdisplay = "N";
                        }
                      }else{
                        objItemHeader.npdisplay = "N";
                      }
                    }
                    //FIN - DLAZO
                    if ( objItemHeader.npdisplay == "N" ) {
                        contentHidden += "<input type='hidden' name='hdnItemHeaderId' value='"+objItemHeader.npobjitemheaderid+"' >";
                        contentHidden += "<input type='hidden' name='hdnItemValue"+trim(objItemHeader.nphtmlname)+"' "+valuevisible+" >";
                        contentHidden += "<input type='hidden' name='"+ trim(objItemHeader.nphtmlname) +"' " + descvisible + " > ";
                    }else{
                    <%
                        if( (  MiUtil.parseInt(hdnSpecification) == Constante.SPEC_CAMBIO_MODELO || 
                            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA   ||
                            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_NUEVA    ||
                            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_TDE      ||
                            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_REPOSICION_PREPAGO_TDE )
                            //Se agrego subcategoria reposicion prepago tde - TDECONV034                      
                            && (!"s".equals(strResult) || (!strFlagModality.equals(Constante.MODALITY_PROPIO) && !strFlagModality.equals("Alquiler en Cliente")))
                          ) { %>                   
                         
                         if(/*objItemHeader.npobjitemheaderid == 58 ||*/ objItemHeader.npobjitemheaderid == 96){
                            continue;
                         }
                         
                    <%   }else if((  MiUtil.parseInt(hdnSpecification) == Constante.SPEC_CAMBIO_MODELO || 
                            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA ||
                            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_NUEVA ||
                            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_TDE ||
                            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_REPOSICION_PREPAGO_TDE )                            
                            && ("s".equals(strResult) && (strFlagModality.equals(Constante.MODALITY_PROPIO)|| strFlagModality.equals("Alquiler en Cliente")   )    )){%>
                                                
                         if(objItemHeader.npobjitemheaderid == 96){
                            
                            if(flagvalida96 == "S"){
                               continue;
                            }
                         
                            descvisible = objItem.npobjitemvaluedesc;                                                        
                            if (descvisible == 'S')
                               descvisible = "<%=Constante.OPERATION_STATUS_ERROR%>";
                            else if (descvisible == 'N') 
                               descvisible = "<%=Constante.OPERATION_STATUS_OK%>";
                            else if(descvisible == 'I') 
                               descvisible = "<%=Constante.OPERATION_STATUS_INCOMPLETO%>";
                            else if(descvisible == '' || descvisible == null )
                               descvisible = "";
                              
                              
                            cell = row.insertCell(index_arg - 1);
                            elemText += "<div id='"+ objItemHeader.nphtmlname +"'  align='center' class='CellContent' > ";
                            elemText += "<input type='hidden' name='hdnItemHeaderId' value='"+objItemHeader.npobjitemheaderid+"' >";
                            elemText += "<input type='hidden' name='hdnItemValue"+trim(objItemHeader.nphtmlname)+"' "+valuevisible+" >";
                            elemText += "<input type='text'   name='"+ trim(objItemHeader.nphtmlname) +"' ";                              
                            //CLOZZA}                            
                            if(descvisible==""){                             
                               elemText += " value='" + descvisible + "' style='text-align:center' size='15' readonly>";
                            }else if(descvisible == "<%=Constante.OPERATION_STATUS_ERROR%>"){                           
                               elemText += " value='" + descvisible + "'  style='text-align:center; color:red; text-decoration:underline' "+
                               " size='15' onclick='fxShowErrorImeiSim(this.value,"+objItem.npitemid+","+ITEM_DEVICE_PROPIO+");' readonly>";                            
                            }else if(descvisible == "<%=Constante.OPERATION_STATUS_INCOMPLETO%>"){                            
                               elemText += " value='" + descvisible + "' style='text-align:center; color:red; text-decoration:underline' "+
                               " size='15' onclick='fxShowErrorImeiSim(this.value,"+objItem.npitemid+","+ITEM_DEVICE_PROPIO+");' readonly>";
                            }else if(descvisible == "<%=Constante.OPERATION_STATUS_OK%>"){                           
                               elemText += " value='" + descvisible + "' style='color:green' style='text-align:center' size='15' readonly>";
                            }                                                        
                            //alert("desvisible:" + descvisible+",Error elemText Cesar " + elemText);                            
                            cell.innerHTML = elemText;
                                                        
                            elemText =  "";
                            index_arg++;
                            flagvalida96 = "S";                            
                            continue;
                         }
                    
                    <%}%>
                        cell = row.insertCell(index_arg - 1);
                        elemText += "<div id='"+ objItemHeader.nphtmlname +"'  align='center' class='CellContent' > ";
                        elemText += "<input type='hidden' name='hdnItemHeaderId' value='"+objItemHeader.npobjitemheaderid+"' >";
                        elemText += "<input type='hidden' name='hdnItemValue"+trim(objItemHeader.nphtmlname)+"' "+valuevisible+" >";
                        if (objItemHeader.npobjitemheaderid != '<%=strStatusItemExecution%>'){ /*jsalazar - modif hpptt # 1 - 29/09/2010 01022011*/
                        elemText += "<input type='text'   name='"+ trim(objItemHeader.nphtmlname) +"' ";
                        elemText += descvisible;
                        if  ( (objItemHeader.npobjitemheaderid==2) || (objItemHeader.npobjitemheaderid==5) || (objItemHeader.npobjitemheaderid==4) )
                           elemText += " size='13' style='text-align:right' readOnly></div>";
                        else{
                           if( (objItemHeader.npobjitemheaderid==10) || (objItemHeader.npobjitemheaderid==16) )
                              elemText += " size='22' style='text-align:right' readOnly></div>";
                           else{
                              if( (objItemHeader.npobjitemheaderid==3) || (objItemHeader.npobjitemheaderid==11) )
                                 elemText += " size='10' style='text-align:right' readOnly></div>";
                              else {
                                  if(objItemHeader.npobjitemheaderid==51){
                                      <%if(MiUtil.parseInt(hdnSpecification) == Constante.SPEC_BOLSA_CREAR || MiUtil.parseInt(hdnSpecification) == Constante.SPEC_BOLSA_UPGRADE || MiUtil.parseInt(hdnSpecification) == Constante.SPEC_BOLSA_DOWNGRADE || MiUtil.parseInt(hdnSpecification) == Constante.SPEC_BOLSA_DESACTIVAR ){%> // LHUAPAYA  [ADT-BCL-083]
                                      elemText += " size='35' style='text-align:right' readOnly></div>";
                                      <%}else{%>
                                 elemText += " size='12' style='text-align:right' readOnly></div>";
                                      <%}%>
                                  }else {
                                      elemText += " size='12' style='text-align:right' readOnly><" + "/div>";
                                  }
                              }
                           }
                        }
                        /*jsalazar - modif hpptt # 1 - 29/09/2010 - inicio*/
                        }
                        else{
                          if (objItem.npobjitemvalue == 4 || objItem.npobjitemvalue == 8) {
                            elemText += "<a href=\'javascript:;\' onclick=\'javascript:fxItemListFailedExecution(fxGetRowIndex(this),parent.mainFrame.vctItemsMainFrameOrder,parent.mainFrame.vctParametersOrder);\' ALT=\'Editar Item\'><font color=\'red\' face=\'Arial\' ><b>"+ objItem.npobjitemvaluedesc +"</b></font></a>";
                          } else if(objItem.npobjitemvalue == 0 || objItem.npobjitemvalue == 1) {
                            elemText += "<a href=\'javascript:;\' onclick=\'javascript:fxItemListFailedExecution(fxGetRowIndex(this),parent.mainFrame.vctItemsMainFrameOrder,parent.mainFrame.vctParametersOrder);\' ALT=\'Editar Item\'><b>"+ objItem.npobjitemvaluedesc +"</b></a>";                          
                          } else{
                            elemText += "<font color=\'black\' face=\'Arial\' >"+ objItem.npobjitemvaluedesc +"</font>";
                          }                                                    
                        }
                        /*jsalazar - modif hpptt # 1 - 29/09/2010 - fin*/
                        cell.innerHTML = elemText;
                        elemText =  "";
                        index_arg++;
                    }
                    valFlag = true;
                    break;
                   
                }//Fin del If
               
             }//Fin del For
           if(!valFlag){
             //alert("[i][" + i + "] -> objItemHeader " + objItemHeader.npobjitemheaderid  + " --> " + objItemHeader.nphtmlname);  
             contentHidden += "<input type='hidden' name='hdnItemHeaderId' value='"+objItemHeader.npobjitemheaderid+"' >";
             contentHidden += "<input type='hidden' name='hdnItemValue"+trim(objItemHeader.nphtmlname)+"'  >";
             contentHidden += "<input type='hidden' name='"+ trim(objItemHeader.nphtmlname) +"'  > ";         
           }
           
          }//Fin del For
            
            var cellDelete = row.insertCell(index_arg-1);            
            var showDeleteButton=0;             
            
            //if (v_bolpermit==1){ // Pregunta si se puede editar
            //   showDeleteButton=1; // Editable
            //}            
            if (v_delpermit=='Enabled'){
			   showDeleteButton=1; // Editable
			}
			
            /* Boton de Borrar Item */
            elemText =                           "<input type='hidden' name='item_adde_period' value=''>" +
                                                 "<input type='hidden' name='item_adde_type' value=''>" +
                                                 "<input type='hidden' name='item_own_equip_new' value=''>" +
                                                 "<input type='hidden' name='hdnnpPromoHeaderId' value=''>" +
                                                 "<input type='hidden' name='hdnnpPromoDetailId' value=''>" +
                                                 "<input type='hidden' name='hdnnpPromoHeaderName' value=''>" +
                                                 "<input type='hidden' name='hdnnpOracleCode' value=''>" +
                                                 "<input type='hidden' name='hdnnpRealImei' value=''>" +
                                                 
                                                 "<input type='hidden' name='item_performance_flag' value='n'>" +
                                                 "<input type='hidden' name='item_perform_flag_serv' value='n'>" +
                                                 "<input type='hidden' name='hdnmodality' value=''>" +
                                                 "<input type='hidden' name='hdnreplacementmode' value=''>" +
                                                 "<input type='hidden' name='hdnnewmodality' value=''>" +
                                                 "<input type='hidden' name='item_excepBasicRentDesc'>" +
                                                 "<input type='hidden' name='item_excepBasicRentException'>" +
                                                
                                                 "<input type='hidden' name='item_excepRentDesc'>" +
                                                 "<input type='hidden' name='item_excepRentException'>" +
                                                
                                                 "<input type='hidden' name='item_excepServiceId'>" +
                                                 "<input type='hidden' name='item_excepServiceDiscount'>" +
                                                 
                                                 //"<input type='hidden' name='item_billingAccount'>" +
                                                
                                                 "<input type='hidden' name='item_excepMinAddConexDirecChecked'>" +
                                                 "<input type='hidden' name='item_excepMinAddInterConexChecked'>" +
                                                 "<input type='hidden' name='item_ptnId'>" + //Reserva de Numeros FPICOY 17/01/2011
                                                 
            
                        "<div id='eliminar' align='center' class='CellContent' >" +
                        "<a href='javascript:fVoid()' onclick='javascript:fxDeleteItemEdit(this,this.parentNode.parentNode.parentNode.rowIndex,(" + (form.hdn_item_imei_grupo.value) + "),"+ showDeleteButton + ");'  ><img src='<%=Constante.PATH_APPORDER_SERVER%>/images/Eliminar.gif' border='no' ALT='Borrar Item'></a>"+
                        "</div>";						

            cellDelete.innerHTML = elemText+contentHidden;
            
       }//Fin del If CantElements
       
        wn_items = (items_table.rows.length -1); // numero de items 
        
          if ( wn_items > 1 ){
              wb_existItem =true;
          }
    
    fxCalculateTotal();
    
    <%if( flgIMEIS ){%>
    if( vflagEnabled )
    fxLoadImeis(form.hdn_item_imei_grupo.value);
    <%}%>
  
    //DERAZO REQ-0428
    if(flagShowPayPenalty == 1){
        document.getElementById("p_penalidad").style.display = "inline-block";
  }
  }
  
  </script>
  
  <script defer>
  
  function fxEditRowOrderItemsGlobal(vctItemOrder,itemEdit_Index,isChangeGuide,isChangePlanRate){
    form = document.frmdatos;

    try{
      form.item_services.value = GetSelectedServices();
    }catch(e){}
    
    var objvctItemHeaderOrder = vctItemHeaderOrder;
    var item_index = itemEdit_Index;
    
    var quantityCurrent = "";
    var quantityNew     = "";
    
    var strProductLineId= "";
    var strProductId    = "";
    var strPlanName     = "";
    var strProductName  = "";
    var strModality     = "";
    var strFlgProductGN = "";
    var strPhoneNumber  = "";//Reserva de Numeros Golden - FPICOY - 24/01/2011
    
    try{
      //Volumen de Orden
      <%if( "Enabled".equals(MiUtil.getString((String)hshScreenField.get("ordervolume"))) ){%>
        //alert("Volver a Evaluar Promocion por Volumen de Orden");
        if (form.chkVepFlag != undefined){
           if (form.chkVepFlag.checked){
              document.getElementById("btnEvaluarVO").disabled = true;
           }else{
        document.getElementById("btnEvaluarVO").disabled = false;
        document.getElementById("btnEvaluarVO").focus();
           }
        }else{
           document.getElementById("btnEvaluarVO").disabled = false;
           document.getElementById("btnEvaluarVO").focus();
        }
        form.txtTotalVOSalesPrice.value = "";
        if(aplicoVO != "0"){
          aplicoVO = "2";
        }
      <%}%>
      
    }catch(e){}
    
    //alert("Entramos");
    <%if( flgIMEIS ){%>
      
    try{
    
      for(j=0; j<vctItemOrder.size(); j++){
        //Obtenemos la cantidad
        if( vctItemOrder.elementAt(j).npobjitemid == 20 )
          quantityNew   =   vctItemOrder.elementAt(j).npobjitemvalue;
        //Obtenemos el productId
        if( vctItemOrder.elementAt(j).npobjitemid == 8 )
          strProductId  =   vctItemOrder.elementAt(j).npobjitemvalue;
        //Obtenemos el planName
        if( vctItemOrder.elementAt(j).npobjitemid == 9 )
          strPlanName   =   vctItemOrder.elementAt(j).npobjitemvaluedesc;
        //Obtenemos el productName
        if( vctItemOrder.elementAt(j).npobjitemid == 8 )
          strProductName  =   vctItemOrder.elementAt(j).npobjitemvaluedesc;
        //Obtenemos la modalidad
        if( vctItemOrder.elementAt(j).npobjitemid == 1 )
          strModality  =   vctItemOrder.elementAt(j).npobjitemvalue;
        //Obtenemos el productLineId
        if( vctItemOrder.elementAt(j).npobjitemid == 39 )
          strProductLineId  = vctItemOrder.elementAt(j).npobjitemvalue;
        //Obtenemos el garantia nextel
        if( vctItemOrder.elementAt(j).npobjitemid == 40 )
          strFlgProductGN  = vctItemOrder.elementAt(j).npobjitemvalue;   
        //Obtenemos la cadena de telefonos - FPICOY - 24/01/2011
        if( strProductLineId==23 && vctItemOrder.elementAt(j).npobjitemid == 113)
          strPhoneNumber  = vctItemOrder.elementAt(j).npobjitemvalue;
      }
		
      
      //alert(quantityNew + " - " + strProductId + " - " + strPlanName + " - " + strProductName + " - " + strModality);
      
      //Obtenemos la cantidad actual
      if( items_table.rows.length == 2 ){
        quantityCurrent = eval("form.txtItemQuantity.value");
        hdnIndice       = eval("form.hdnIndice.value");
      }else{
        quantityCurrent = eval("form.txtItemQuantity["+itemEdit_Index+"].value");
        hdnIndice       = eval("form.hdnIndice["+itemEdit_Index+"].value");
      }
    
    }catch(e){}
    
    <%}%>
    
    //Se setean los valores que vienen desde el item 
    //a la cabecera
    for( i = 0; i < objvctItemHeaderOrder.size(); i++ ){
          
          var objItemHeader = objvctItemHeaderOrder.elementAt(i);
        
            for( x = 0; x < vctItemOrder.size(); x++ ){
                
                var objItem = vctItemOrder.elementAt(x);
            
                if ( objItemHeader.npobjitemheaderid == objItem.npobjitemheaderid ) {
                  
                  var nameHTMLInput = vctItemOrder.elementAt(x).namehtmlitem;
                  
                  if( items_table.rows.length == 2 ){
                    var strConcatValue = "form.hdnItemValue"+trim(objItemHeader.nphtmlname)+".value = '" + objItem.npobjitemvalue + "'";
                    var strConcatDescr = "form."+trim(objItemHeader.nphtmlname)+".value = '" + objItem.npobjitemvaluedesc + "'";
                  }else{
                    var strConcatValue = "form.hdnItemValue"+trim(objItemHeader.nphtmlname)+"["+item_index+"].value = '" + objItem.npobjitemvalue + "'";
                    var strConcatDescr = "form."+trim(objItemHeader.nphtmlname)+"["+item_index+"].value = '" + objItem.npobjitemvaluedesc + "'";
                  }
                  
                  eval(strConcatValue);
                  eval(strConcatDescr);
                  
                  break;
                  
               }
            }
      }
    <%if( flgIMEIS ){%>
    //try{
    /*if( isChangeGuide == 1 ){
      alert("hubieron cambios que afectan a la guía");
    }else if( isChangePlanRate == 1 ){
      alert("hubieron cambios que NO afectan a la guía");
      if( isChangePlanRate == 1 ){
        alert("hubieron cambios en Cambio de Plan");
      }
    }*/
    
    if( isChangeGuide == 1 ){
      //Se agregó el parametro strPhoneNumber - RHUACANI-ASISTP - 24/10/2010
      fxRecalculateItemImeis(itemEdit_Index+1,hdnIndice,"table_imeis",quantityCurrent,quantityNew,strModality,strProductId,strPlanName,strProductName,strProductLineId,strFlgProductGN,strPhoneNumber);
    }else if( isChangePlanRate == 1 ){
      //alert("Reconstruir los IMEIS con el nombre del PLAN");
      fxReBuildingItemImeisRatePlan(itemEdit_Index+1,hdnIndice,"table_imeis",quantityCurrent,quantityNew,strModality,strProductId,strPlanName,strProductName,strProductLineId,strFlgProductGN);
    }
    //}
    //catch(e){}
    <%}%>

    fxCalculateTotal();
  }
  
  function fxReBuildingItemImeisRatePlan(itemId,groupid,tableID,quantCurrent,quantNew,strModality,strProductId,strPlanName,strProductName,strProductLineId,strFlgProductGN){
    var table    = document.all ? document.all[tableID]:document.getElementById(tableID);
    
    if( strModality != "Propio" && strModality != "Alquiler en Cliente" ){
     
      if( table.rows.length == 2 ){
        form.item_imei_plan.value = strPlanName;
      }else{
        for(i=1; i<table.rows.length; i++){
          if( table.rows[i].id == groupid ){
            form.item_imei_plan[i-1].value = strPlanName;
          }
        }
      }
      
    }
    
  }
  
  function fxRecalculateItemImeis(itemId,groupid,tableID,quantCurrent,quantNew,strModality,strProductId,strPlanName,strProductName,strProductLineId,strFlgProductGN,strPhoneNumber){
    var table    = document.all ? document.all[tableID]:document.getElementById(tableID);
    var indMayor = 0;
    var indMenor = 0;
    var dif      = 0;
    //alert("Entramos OK 2");
    //Si los valores han cambiado
    //if( quantCurrent != quantNew ){
    
       //Se agregan Imeis
       if( quantCurrent < quantNew ){
         dif = parseInt(quantNew) - parseInt(quantCurrent);
       }  
         //Obtener el indice Mayor
         for(i=1; i<table.rows.length; i++){
           if( table.rows[i].id == groupid ){
              indMayor = i;
           }
         }
         
         //Obtener el indice Menor
         for(i=1; i<table.rows.length; i++){
           if( table.rows[i].id == groupid ){
              indMenor = i;
              break;
           }
         }
       //alert("Entramos OK 3");
       //Se deben de agregar IMEIS
       fxAddImei(itemId,groupid,dif,indMayor,indMenor,quantNew,quantCurrent,strModality,strProductId,strPlanName,strProductName,strProductLineId,strFlgProductGN,strPhoneNumber);
       //alert("Entramos OK 4");
       //}
       //Fin de agregar IMEIS
    //}
    
  }//fxRecalculateItemImeis
  
  /*Developer : Lee Rosales
    Motivo    : Agrega IMEIS a la tabla table_imeis al haberse
    modificado la cantidad de de productos que se agregaron 
    incialmente
  */
  function fxAddImei(itemid,groupId,dif,indMayor,indMenor,intQuantity,qCurrent,strModality,strProductId,strPlanName,strProductName,strProductLineId,strFlgProductGN,strPhoneNumber){
    form = document.frmdatos;
    var cntItems       = items_table.rows.length;
    var intModalityID  = "";
    
    //Verificar que tipo de modalidadId es
    if( strModality == "Venta" || strModality == "Prestamo" || strModality == "Asignacion" ) 
      intModalityID = 20;
    else if( strModality == "Alquiler" ) 
      intModalityID = 10;
    
    if( strModality == "Propio" || strModality == "Alquiler en Cliente" ){
      fxDeleteImeiTable(groupId);
      return;
    }
    
    try{
    form.btnUpdOrder.disabled = true;
    }catch(e){}
    
    try{
    form.btnUpdOrderInbox.disabled = true;
    }catch(e){}
    //Se agregó el parametro strPhoneNumber - RHUACANI-ASISTP - 24/10/2010
    var a = appContext+"/itemServlet?hdnMethod=doGetImeisByProductEdit&strProductId="+strProductId+"&intModalityID="+intModalityID+"&strModality="+strModality+"&strQuantity="+intQuantity+"&strGroupId="+groupId+"&strPlanName='"+strPlanName+"'&strProductName=&strIndMax="+indMayor+"&strIndMin="+indMenor+"&strDiference="+dif+"&intQuantityCurrent="+qCurrent+"&intProductLineId="+strProductLineId+"&strFlgProductGN="+strFlgProductGN+"&strSalesStuctOrigenId=<%=strSalesStuctOrigenId%>"+"&strPhoneNumber="+strPhoneNumber;
    
    //var a = appContext+"/itemServlet?hdnMethod=doGetImeisByProductEdit&strProductId="+strProductId+"&intModalityID="+intModalityID+"&strModality="+strModality+"&strQuantity="+intQuantity+"&strGroupId="+groupId+"&strPlanName='"+strPlanName+"'&strProductName='"+strProductName+"'&strIndMax="+indMayor+"&strIndMin="+indMenor+"&strDiference="+dif+"&intQuantityCurrent="+qCurrent+"&intProductLineId="+strProductLineId+"&strFlgProductGN="+strFlgProductGN;
    
    //var a = appContext+"/itemServlet?hdnMethod=doGetImeisByProductEdit&strProductId="+strProductId+"&intModalityID="+intModalityID+"&strModality="+strModality+"&strQuantity="+intQuantity+"&strGroupId="+groupId+"&strPlanName='"+strPlanName+"'&strProductName='"+strProductName+"'&strIndMax="+indMayor+"&strIndMin="+indMenor+"&strDiference="+dif+"&intQuantityCurrent="+qCurrent+"&intProductLineId="+strProductLineId+"&strFlgProductGN="+strFlgProductGN;
    //alert(a);
    form.action = a;
    form.submit();
  }//fxAddImei
  
  function fxCalculateTotal(){
    var form = parent.mainFrame.document.frmdatos;
    var totalPrice = "0.0";
    var statusOrden = null;
    
    var totalPriceVEP = 0.0;
    var totalVEP; 
    
    if( (items_table.rows.length -1) == 1 ){
      try{ 
			if(form.txtitemTotal.value==undefined || form.txtitemTotal.value=='undefined') {
				form.txtitemTotal[0].value = parseFloat(round_decimals(form.txtitemTotal[0].value ,2));
				totalPrice = form.txtitemTotal[0].value;
			} else {
				form.txtitemTotal.value = parseFloat(round_decimals(form.txtitemTotal.value ,2));
				totalPrice = form.txtitemTotal.value;				
			}
      }catch(e){
        totalPrice = "0.0";
      }
        
      try {
        if ((form.txtItemVentaVEP.value == "1") && (form.txtItemModality.value == "Venta")){              
              totalPriceVEP = parseFloat(form.txtitemTotal.value);
        }
      } catch(e){ totalPriceVEP = "0.0";}
          
    }else{
      try{
        for(k=0; k<form.txtitemTotal.length; k++)
        {
           form.txtitemTotal[k].value = parseFloat(round_decimals(form.txtitemTotal[k].value,2));
           totalPrice = parseFloat(totalPrice) + parseFloat(form.txtitemTotal[k].value);

           // MMONTOYA
           try {
           if ((form.txtItemVentaVEP[k].value == "1") && (form.txtItemModality[k].value == "Venta")){              
              totalPriceVEP += parseFloat(form.txtitemTotal[k].value);
        }    
           } catch(e) {}
        }    
        
      }catch(e){
        totalPrice = "0.0";
      }
           
    }

    form.hdnTotalSalesPriceVEP.value = totalPriceVEP;
    var total = parseFloat(totalPrice);
    total= parseFloat(round_decimals(total,2));
    form.txtTotalSalesPrice.value = total.toFixed(2);
    form.txtImporteFacturaTotal.value = total.toFixed(2);
    if (strPaymentStatus.toUpperCase() == "PENDIENTE" ){        
        //form.txtSaldo.value = total.toFixed(2);
        form.txtSaldo.value = form.txtImporteFacturaTotal.value - form.txtnpTotalPayment.value;
        form.txtSaldo.value = parseFloat(round_decimals(form.txtSaldo.value,2));
    }
    
	try {
		if(form.txtTotalSalesPrice.value==undefined || form.txtTotalSalesPrice.value=="undefined") {
			if(form.txtitemTotal.value==undefined) {
				form.txtTotalSalesPrice.value = form.txtitemTotal[0].value;
        form.txtImporteFacturaTotal.value = form.txtitemTotal[0].value;
         if (strPaymentStatus.toUpperCase() == "PENDIENTE" ){        
            //form.txtSaldo.value = form.txtitemTotal[0].value;
          form.txtSaldo.value = form.txtImporteFacturaTotal.value - form.txtnpTotalPayment.value;
          form.txtSaldo.value = parseFloat(round_decimals(form.txtSaldo.value,2));
         }
			} else {
				form.txtTotalSalesPrice.value = form.txtitemTotal.value;
        form.txtImporteFacturaTotal.value = form.txtitemTotal.value;
         if (strPaymentStatus.toUpperCase() == "PENDIENTE" ){        
            //form.txtSaldo.value = form.txtitemTotal.value;
          form.txtSaldo.value = form.txtImporteFacturaTotal.value - form.txtnpTotalPayment.value;
          form.txtSaldo.value = parseFloat(round_decimals(form.txtSaldo.value,2));
         }
			}
		}
	} catch(exception) {
		form.txtTotalSalesPrice.value = "0.0";
    form.txtSaldo.value = "0.0";
    form.txtImporteFacturaTotal.value  = "0.0";
	}
  
  }
 
  function CargaServiciosItem(oindexRow){  
   var servicios_item_send = "";
   form = document.frmdatos;
   /* Guardamos el numero del Item que se visualiza sus servicios */
   form.hdnItemSelectService.value =  oindexRow;
   servicios_item = oindexRow - 1;
   if (servicios_item == 0)
      if (form.item_services[servicios_item] == null)
         servicios_item_send = form.item_services.value;
      else
         servicios_item_send = form.item_services[servicios_item].value;
   else
      servicios_item_send = form.item_services[servicios_item].value;
   
   var a="serviceservlet?myaction=loadServiceItems&servicios_item="+servicios_item_send;
   form.action = a;
   
   form.submit();
  }
  </script>
 
  <script>
  
  
  function fxValidNumRowsItem(){
  
    if (parseInt(document.frmdatos.hdn_validitem.value) == 1 ){
      return true;               
    }
    else
      return false;
  }
  
  function fxLoadImeis(groupId){
    form = document.frmdatos;
    
    var cntItems       = items_table.rows.length;
    var intQuantity    = 0;
    var intProductId   = 0;
    var strModality    = "";
    var intModalityID  = "";
    var strPlanName    = "";
    //var strProductName = "";
    var intProductLineId  = 0;
	 var strFlgProductGN = "";
   var strPhoneNumber = ""; //Reserva de Numeros Golden - RHUACANI-ASISTP - 24/10/2010
    
    //Si solo hay un item
    if( cntItems == 2 ){
      intQuantity     = form.hdnItemValuetxtItemQuantity.value;
      intProductId    = form.hdnItemValuetxtItemProduct.value;
      strModality     = form.hdnItemValuetxtItemModality.value;
      intProductLineId= form.hdnItemValuetxtItemProductLine.value;
      strPlanName     = form.txtItemRatePlan!=null?form.txtItemRatePlan.value:'';
      //strProductName  = form.txtItemProduct.value;
		strFlgProductGN  = form.txtItemFlgProductGN!=null?form.txtItemFlgProductGN.value:'';
    strPhoneNumber   = form.hdnItemValuecmb_ItemAssignedNumber!=null?form.hdnItemValuecmb_ItemAssignedNumber.value:'';//Reserva de Numeros Golden - RHUACANI-ASISTP - 24/10/2010
    //Si hay mas de un item
    }else{
      intQuantity     = form.hdnItemValuetxtItemQuantity[(cntItems-2)].value;
      intProductId    = form.hdnItemValuetxtItemProduct[(cntItems-2)].value;
      strModality     = form.hdnItemValuetxtItemModality[(cntItems-2)].value;
      intProductLineId= form.hdnItemValuetxtItemProductLine[(cntItems-2)].value;
      strPlanName     = form.txtItemRatePlan!=null?form.txtItemRatePlan[(cntItems-2)].value:'';
      //strProductName  = form.txtItemProduct[(cntItems-2)].value;
		strFlgProductGN  = form.txtItemFlgProductGN!=null?form.txtItemFlgProductGN[(cntItems-2)].value:'';
    strPhoneNumber   = form.hdnItemValuecmb_ItemAssignedNumber!=null?form.hdnItemValuecmb_ItemAssignedNumber[(cntItems-2)].value:'';//Reserva de Numeros Golden - RHUACANI-ASISTP - 24/10/2010
    }
    
    if( strModality == "Propio" || strModality == "Alquiler en Cliente") return;
    
    try{
    form.btnUpdOrder.disabled = true;
    }catch(e){}
    
    try{
    form.btnUpdOrderInbox.disabled = true;
    }catch(e){}
  
    //Verificar que tipo de modalidadId es
    if( strModality == "Venta" || strModality == "Prestamo" || strModality == "Asignacion" ) 
      intModalityID = 20;
    else if( strModality == "Alquiler" ) 
      intModalityID = 10;
    
    //var a = appContext+"/itemServlet?hdnMethod=doGetImeisByProduct&strProductId="+intProductId+"&intModalityID="+intModalityID+"&strModality="+strModality+"&strQuantity="+intQuantity+"&strGroupId="+groupId+"&strPlanName="+strPlanName+"&strProductName=&intProductLineId="+intProductLineId+"&strFlgProductGN="+strFlgProductGN;
    var a = appContext+"/itemServlet?hdnMethod=doGetImeisByProduct&strProductId="+intProductId+"&intModalityID="+intModalityID+"&strModality="+strModality+"&strQuantity="+intQuantity+"&strGroupId="+groupId+"&strPlanName='"+strPlanName+"'&strProductName=&intProductLineId="+intProductLineId+"&strFlgProductGN="+strFlgProductGN+"&strSalesStuctOrigenId=<%=strSalesStuctOrigenId%>"+"&strPhoneNumber="+strPhoneNumber;
    //alert(a);
    form.action = a;
    form.submit();
    
  }
  </script>
  
  <script DEFER>
  
  function fxSectionNameValidate(){
    var form = document.frmdatos;

    //Validar el numero de item requeridos para grabar la orden
    if (parseInt(document.frmdatos.hdn_validitem.value) == 1 ){
      if (document.frmdatos.cmbAction.value != "<%=Constante.ACTION_INBOX_ANULAR%>"){
        if( items_table.rows.length < 2 ){
          alert("Debe agregar al menos un Item");
          return false;
        } else{ //<!-- jsalazar - modif hpptt # 1 - 25/05/2011 -fin-->
      
    <% if(MiUtil.parseInt(hdnSpecification) == Constante.SPEC_SSAA_SUSCRIPCIONES){%>
      if(items_table.rows.length < 3){
       var fechaItem=form.hdnItemValuetxtItemActivationDate.value;
       var fechaProceso=form.txtFechaProceso.value;
        if(fechaProceso==""){
         alert("Debe ingresar 'Fecha Proc. Aut.'");
         return false;
        }
        if(fechaItem ==""){
         alert("Debe ingresar 'Fecha Activación' del Item");
         return false;
         }
        if(!validaFechasIguales(fechaItem,fechaProceso)){ 
         alert(" 'Fecha Activación' del Item  debe ser igual a 'Fecha Proc. Aut.' ");
         return false;
        }
      }
    <%}%>
    //<!-- jsalazar - modif hpptt # 1 - 25/05/2011 -fin-->
        }	
      } 	
    }
    wv_item_id = "";
    for(i=0; i<vItemsBorradoItems.size(); i++){
       if( vItemsBorradoItems.elementAt(i)  != undefined ) 
            wv_item_id += "|" + vItemsBorradoItems.elementAt(i);
    };
    
    form.hdnItemBorrados.value = wv_item_id;
    
    return true;
  }
  
  //<!-- jsalazar - modif hpptt # 1 - 25/05/2011 -inicio-->
  // Retorna verdadero si las fecha strFechaF es igual a la fecha strFechaI
  // de lo contrario retorna falso
  function validaFechasIguales(strFechaI,strFechaF){
        //Fecha strFechaI            
        var wv_fProceso = new Date();
        var day_I    = parseFloat(strFechaI.substring(0,2));
        var month_I  = parseFloat(strFechaI.substring(3,5));
        var year_I   = parseFloat(strFechaI.substring(6,10));

        //Fecha strFechaF        
        var day_F  = parseFloat(strFechaF.substring(0,2));
        var month_F  = parseFloat(strFechaF.substring(3,5));
        var year_F   = parseFloat(strFechaF.substring(6,10));
          
        if (year_F == year_I && month_F == month_I && day_F == day_I){
           return true;
        };
          
          return false;
   }
  //<!-- jsalazar suscripciones -->25/05/2011- fin-->
  
  
  function fxLoadData(){
  
  vctItemsMainFrameOrder.removeElementAll();
  vctItemsMainImei.removeElementAll();
  vctItemHeaderOrder.removeElementAll();
  vctParametersOrder.removeElementAll();
  
  /*****Carga de la data*****/
  
  <%
   if ( hshtinputNewSection != null ) {
    Enumeration objNameSection  = hshtinputNewSection.keys();
    Enumeration objValueSection = hshtinputNewSection.elements();

    while( objNameSection.hasMoreElements() || objValueSection.hasMoreElements() ){
    String nameObject  = objNameSection.nextElement().toString();
    String valueObject = objValueSection.nextElement().toString();
   %>
    vctParametersOrder.addElement( new fxMakeOrderObejct( '<%=nameObject%>','<%=valueObject%>') );
   <%}}%>
   
   <%
   if( objItemHEader != null && objItemHEader.size() > 0 ){
                                          
       NpObjHeaderSpecgrp objnpObjHeaderSpecgrpAux = null;
       
       for ( int j=0 ; j<objItemHEader.size(); j++ ){
       String  strCadena = "'";                                          
           objnpObjHeaderSpecgrpAux = new NpObjHeaderSpecgrp();
           objnpObjHeaderSpecgrpAux =  (NpObjHeaderSpecgrp)objItemHEader.get(j);
           
      strCadena += objnpObjHeaderSpecgrpAux.getNpspecificationgrpid() + "' , '";
      strCadena += objnpObjHeaderSpecgrpAux.getNpobjitemheaderid() + "' , '";
      strCadena += objnpObjHeaderSpecgrpAux.getNpobjitemname() + "' , '";
      strCadena += objnpObjHeaderSpecgrpAux.getNpdisplay() + "' , '";
      strCadena += objnpObjHeaderSpecgrpAux.getNpcreatedby() + "' , ' ";
      strCadena += objnpObjHeaderSpecgrpAux.getNpcreateddate() + "' , ' ";
      strCadena += objnpObjHeaderSpecgrpAux.getNpmodifiedby() + "' , ' ";
      strCadena += objnpObjHeaderSpecgrpAux.getNpmodifieddate() + "' , ' ";
      strCadena += objnpObjHeaderSpecgrpAux.getNphtmlname() + "' , ' ";
      strCadena += "' ";
   %>
   vctItemHeaderOrder.addElement(new fxMakeOrderHeaderItem(<%=strCadena%>));
   <%
      }//Fin del For
   }//Fin del If
   %>
   
   
   
  <%
 // ItemBean itemBean = null;
  String   objValue = "";
  String   objDescr = "";
  String   itemId   = "";
  
  if( objItemOrder!=null && objItemOrder.size()>0 ){
    for(int i=0; i<objItemOrder.size(); i++ ){
      itemBean  = new ItemBean();
      itemBean  = (ItemBean)objItemOrder.get(i);
      objValue = "";
      objDescr = "";
      
      itemId    =   ""+MiUtil.getString(itemBean.getNpitemid());
      
      objValue  =   ""+MiUtil.getString(itemBean.getNpmodalitysell());
      objDescr  =   ""+MiUtil.getString(itemBean.getNpmodalitysell());
    %>
    
      var vctOrderItem = new Vector();
      //Modalidad de Salida
      vctOrderItem.addElement(new fxMakeOrderItem('1','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//SIM Generic   
      objValue  =   MiUtil.getString(itemBean.getNpownimeinumber());
      objDescr  =   MiUtil.getString(itemBean.getNpownimeinumber());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('2','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%//Telefono  
      objValue  =   ""+MiUtil.getString(itemBean.getNpphone());
      objDescr  =   ""+MiUtil.getString(itemBean.getNpphone());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('3','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//IMEI
      objValue  =   MiUtil.getString(itemBean.getNpimeinumber());
      objDescr  =   MiUtil.getString(itemBean.getNpimeinumber());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('4','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%//SIM
      objValue  =   MiUtil.getString(itemBean.getNpsiminumber());
      objDescr  =   MiUtil.getString(itemBean.getNpsiminumber());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('5','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%//Modelo del Equipo (Deprecated)
      objValue  =   ""+MiUtil.getString(itemBean.getNpmodalitysell());
      objDescr  =   ""+MiUtil.getString(itemBean.getNpmodalitysell());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('7','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Linea de Producto
      objValue  =   ""+itemBean.getNpproductlineid();
      objDescr  =   ""+itemBean.getNpproductlinename();
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('8','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Producto  
      objValue  =   ""+itemBean.getNpproductid();
      objDescr  =   ""+itemBean.getNpproductname();
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('9','','','','','','','','','','','','<%=objValue%>',"<%=objDescr%>",'A','<%=itemId%>'));
    
    <%//Plan Tarifario 
      objValue  =   ""+itemBean.getNpplanid();
      objDescr  =   ""+itemBean.getNpplanname();
      objDescr  =   objDescr.equals("null")?"":objDescr;
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('10','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Equipo  
      objValue  =   MiUtil.getString(itemBean.getNpequipment());
      objDescr  =   MiUtil.getString(itemBean.getNpequipment());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('11','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Devolución de Equipo
      objValue  =   MiUtil.getString(itemBean.getNpequipmentreturn());
      objDescr  =   MiUtil.getString(itemBean.getNpequipmentreturndesc());//MiUtil.getString(itemBean.getNpequipmentreturn());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('12','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Pendiente de Recojo
      objValue  =   MiUtil.getString(itemBean.getNpequipmentnotyetgiveback());
      objDescr  =   MiUtil.getString(itemBean.getNpequipmentnotyetgiveback());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('13','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Fecha de Devolución
      objValue  =   MiUtil.getDate(itemBean.getNpequipmentreturndate(),"dd/MM/yyyy");
      objDescr  =   MiUtil.getDate(itemBean.getNpequipmentreturndate(),"dd/MM/yyyy");
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('14','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Modelo
      objValue  =   MiUtil.getString(itemBean.getNpmodelid());// jsalazar servicios adicionales 06/12/2010
      objDescr  =   MiUtil.getString(itemBean.getNpmodel());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('15','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Nuevo Plan Tarifario
      objValue  =   MiUtil.getString(itemBean.getNporiginalplanname());
      objDescr  =   MiUtil.getString(itemBean.getNporiginalplanname());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('16','','','','','','','','','','','','<%=objValue%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%//Nuevo Numero
      objValue  =   MiUtil.getString(itemBean.getNpphone());
      objDescr  =   MiUtil.getString(itemBean.getNpphone());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('17','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Precio Cta Inscripcion
      String priceCtaInscripcion = "";
      if( !MiUtil.getString(itemBean.getNpprice()).equals("") ) {        
        priceCtaInscripcion = ""+formatter.format(MiUtil.parseDouble(MiUtil.getString(itemBean.getNpprice())));
      }
      objValue  =   priceCtaInscripcion;
      objDescr  =   priceCtaInscripcion;
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('18','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Precio Excepcion
      objValue  =   MiUtil.getString(itemBean.getNppriceexception());
      objDescr  =   MiUtil.getString(itemBean.getNppriceexception());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('19','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%//Renta  
      objValue  =   MiUtil.getString(itemBean.getNprent());
      objDescr  =   MiUtil.getString(itemBean.getNprent());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('20','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Cantidad
      objValue  =   MiUtil.getString(itemBean.getNpquantity());
      objDescr  =   MiUtil.getString(itemBean.getNpquantity());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('21','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Total
      //Si hay precio
      double dblPrecio      =  0.0;
      if( ! MiUtil.getString(itemBean.getNpprice()).equals("") ){
        //Si hay precio de excepción
        if( ! MiUtil.getString(itemBean.getNppriceexception()).equals("") ){
          dblPrecio = MiUtil.parseDouble(itemBean.getNppriceexception());
        }else{
          dblPrecio = MiUtil.parseDouble(itemBean.getNpprice());
        }
      }
      
      int intQuantity   = itemBean.getNpquantity();
      
      if( intQuantity == 0 )
        if( dblPrecio != 0.0 )
            intQuantity   = 1;
      
      objValue  =   ""+(dblPrecio*intQuantity);
      objDescr  =   ""+(dblPrecio*intQuantity);
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('22','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Promocion
      objValue  =   ""+itemBean.getNppromotionid();
      objDescr  =   ""+itemBean.getNppromotionid();
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('23','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Servi Adicionales
      objValue  =   MiUtil.getString(itemBean.getNpitemservices());
      objDescr  =   MiUtil.getString(itemBean.getNpitemservices());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('25','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Contrato a asociar
      objValue  =   MiUtil.getString(itemBean.getNpsharedinstal());
      objDescr  =   MiUtil.getString(itemBean.getNpsharedinstaldesc());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('26','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Nro Contrato
      objValue  =   itemBean.getNpcontractnumber()==0?"":""+itemBean.getNpcontractnumber();
      objDescr  =   itemBean.getNpcontractnumber()==0?"":""+itemBean.getNpcontractnumber();
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('27','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Nro Instalacion compartida
      objValue  =   MiUtil.getString(itemBean.getNpfirstcontract());
      objDescr  =   MiUtil.getString(itemBean.getNpfirstcontractdesc());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('28','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
     
    <%//Instalación Compartida
      objValue  =   ""+itemBean.getNpsharedinstalationid();
      objDescr  =   ""+itemBean.getNpsharedinstalationid();
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('29','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Dirección de Instalación
      objValue  =   ""+itemBean.getNpinstallationaddressid();
      objDescr  =   ""+itemBean.getNpinstallationaddressid();
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('30','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Cargo de Instalación
      objValue  =   MiUtil.getString(itemBean.getNpinstalationprice());
      objDescr  =   MiUtil.getString(itemBean.getNpinstalationprice());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('31','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Cargo de Instalación de Excepción
      objValue  =   MiUtil.getString(itemBean.getNpinstalationexception());
      objDescr  =   MiUtil.getString(itemBean.getNpinstalationexception());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('32','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Tipo Enlace
      objValue  =   MiUtil.getString(itemBean.getNplinktype());
      objDescr  =   MiUtil.getString(itemBean.getNplinktypedesc());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('33','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Tipo Nodo
      objValue  =   ""+itemBean.getNpnetworkhosttype();
      objDescr  =   MiUtil.getString(itemBean.getNpnetworkhosttypedesc());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('34','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Fecha Factibilidad
      objValue  =   MiUtil.toFechaHora(itemBean.getNpfeasibilityprogdate());
      objDescr  =   MiUtil.toFechaHora(itemBean.getNpfeasibilityprogdate());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('35','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%//Factibilidad
      objValue  =   MiUtil.getString(itemBean.getNpfeasibility());
      objDescr  =   MiUtil.getString(itemBean.getNpfeasibilitydesc());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('36','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Instalación
      objValue  =   MiUtil.getString(itemBean.getNpinstalation());
      objDescr  =   MiUtil.getString(itemBean.getNpinstalation());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('37','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Fecha de Instalación
      objValue  =   MiUtil.toFechaHora(itemBean.getNpinstalationprogdate());
      objDescr  =   MiUtil.toFechaHora(itemBean.getNpinstalationprogdate());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('38','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));

    <%//Billing Account
      objValue  =   MiUtil.getString(itemBean.getNpitembillingaccount());
      objDescr  =   MiUtil.getString(itemBean.getNpitembillingaccount());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('39','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    /*Campos de Excepciones*/        
    <%//Excepción Item
       objValue = MiUtil.getString(itemBean.getNpexception());
       objDescr = MiUtil.getString(itemBean.getNpexception());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('40','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));    
    <%//Minutos adicionales CD
      if (itemBean.getNpexceptionminadidispatch() != null){
         objValue = ""+(itemBean.getNpexceptionminadidispatch().compareTo("S")==0?"Y":itemBean.getNpexceptionminadidispatch());
         objDescr = ""+(itemBean.getNpexceptionminadidispatch().compareTo("S")==0?"Y":itemBean.getNpexceptionminadidispatch());
      }
      else{
         objValue = "";
         objDescr = "";
      }
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('45','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Minutos adicionales IT
       if (itemBean.getNpexceptionminaditelephony()!=null){
         objValue = ""+(itemBean.getNpexceptionminaditelephony().compareTo("S")==0?"Y":itemBean.getNpexceptionminaditelephony());
         objDescr = ""+(itemBean.getNpexceptionminaditelephony().compareTo("S")==0?"Y":itemBean.getNpexceptionminaditelephony());
       }else{
          objValue = "";
          objDescr = "";
       }
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('46','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Excepción alquiler
       objValue = MiUtil.getString(itemBean.getNpexceptionrent());
       objDescr = MiUtil.getString(itemBean.getNpexceptionrent());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('43','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));      
    
    <%//Descuento Excepción alquiler
       objValue = ""+itemBean.getNpexceptionrentdiscount();
       objDescr = ""+itemBean.getNpexceptionrentdiscount();
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('44','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));          
    
    <%//Excepción renta
       objValue = MiUtil.getString(itemBean.getNpexceptionrevenue());
       objDescr = MiUtil.getString(itemBean.getNpexceptionrevenue());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('41','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));                
    
    <%//Descuento Excepción renta
       objValue = ""+itemBean.getNpexceptionrevenuediscount();
       objDescr = ""+itemBean.getNpexceptionrevenuediscount();
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('42','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));                            
    
    <%//Servicios gratuitos
       objValue = MiUtil.getString(itemBean.getNpitemfreeservices());
       objDescr = MiUtil.getString(itemBean.getNpitemfreeservices());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('48','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));                      
    
    <%//Descuento Servicios gratuitos
       objValue = ""+itemBean.getNpitemservicescost();
       objDescr = ""+itemBean.getNpitemservicescost();
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('49','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));                            
    
    <%//Ocurrencia
      objValue  =   ""+itemBean.getNpoccurrence();
      objDescr  =   ""+itemBean.getNpoccurrence();
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('53','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%//Moneda
      objValue  =   MiUtil.getString(itemBean.getNpcurrency());
      objDescr  =   MiUtil.getString(itemBean.getNpcurrency());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('54','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%//Linea de Producto
      objValue  =   ""+itemBean.getNpproductlineid();
      objDescr  =   MiUtil.getString(itemBean.getNpproductlinename());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('51','','','','','','','','','','','','<%=objValue%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%//Garantia
      objValue  =   MiUtil.getString(itemBean.getNpwarrant());
      objDescr  =   MiUtil.getString(itemBean.getNpwarrantdesc()); //MiUtil.getString(itemBean.getNpwarrant());//
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('52','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%//Plantilla de adenda
      objValue  = MiUtil.getString(itemBean.getNpitemaddendumtemplates());
      objDescr  = MiUtil.getString(itemBean.getNpitemaddendumtemplates());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('47','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%//Promoción Id
      objValue  =   ""+itemBean.getNppromotionid();
      objDescr  =   ""+itemBean.getNppromotionid();
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('55','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%//Nuevo Plan TarifarioId
      objValue  =   ""+itemBean.getNporiginalplanid();
      objDescr  =   ""+itemBean.getNporiginalplanid();
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('57','','','','','','','','','','','','<%=objValue%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
   <%//Nuevo Número -- Excepción de Visualización para las categorias de Ventas Móviles Prepago y Postpago >  /**KSALVADOR > AUTOMATIZACION*/
      objValue  =   MiUtil.getString(itemBean.getNpnewphone());
      objDescr  =   MiUtil.getString(itemBean.getNpnewphone());
        /*  
       if( (  ( MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA ||
                MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_NUEVA )                 
                && (strFlagModality.equals(Constante.MODALITY_PROPIO) || strFlagModality.equals("Alquiler en Cliente")   )                
                )
                || (MiUtil.parseInt(hdnSpecification) != Constante.SPEC_POSTPAGO_VENTA &&
                    MiUtil.parseInt(hdnSpecification) != Constante.SPEC_PREPAGO_NUEVA)
            ) {*/
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('58','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    <%//}%>
    
    <%//Dirección de Origen / Instalacion
      objValue  =   ""+itemBean.getNpinstallationaddressid();
      objDescr  =   ""+itemBean.getNpinstallationaddressid();
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('59','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Dirección Destino
      objValue  =   MiUtil.getString(itemBean.getNpnewaddress());
      objDescr  =   MiUtil.getString(itemBean.getNpnewaddress());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('60','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%//Contacto
      objValue  =   MiUtil.getString(itemBean.getNpcontactname());
      objDescr  =   MiUtil.getString(itemBean.getNpcontactname());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('61','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%//Teléfono Principal
      objValue  =   MiUtil.getString(itemBean.getNpphonenumber1());
      objDescr  =   MiUtil.getString(itemBean.getNpphonenumber1());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('62','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%//Teléfono Auxiliar
      objValue  =   MiUtil.getString(itemBean.getNpphonenumber2());
      objDescr  =   MiUtil.getString(itemBean.getNpphonenumber2());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('63','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%//Costo Aditional
      objValue  =   MiUtil.getString(itemBean.getNpaditionalcost());
      objDescr  =   MiUtil.getString(itemBean.getNpaditionalcost());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('66','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%//Responsable Area
      objValue  =   ""+itemBean.getNparearespdev();
      objDescr  =   MiUtil.getString(itemBean.getNparearespdevname());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('70','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%//Responsable Devolución
      objValue  =   ""+itemBean.getNpprovidergrpiddev();
      objDescr  =   MiUtil.getString(itemBean.getNpprovidergrpiddevname());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('71','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%//Producto Bolsa
      objValue  =   ""+itemBean.getNporiginalproductid();
      objDescr  =   MiUtil.getString(itemBean.getNporiginalproductname());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('73','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%//Producto Bolsa Descripción
      objValue  =   ""+itemBean.getNporiginalproductid();
      objDescr  =   ""+itemBean.getNporiginalproductid();
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('74','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%//Minutos Contratados
      objValue  =   ""+itemBean.getNpminutesrate();
      objDescr  =   ""+itemBean.getNpminutesrate();
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('75','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%// Precio por Minutos
      objValue  =   ""+itemBean.getNpminuteprice();
      objDescr  =   ""+itemBean.getNpminuteprice();
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('76','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%// Minutos Contratados Prod
      if( hshProdBolsa.get("strMessage") == null ){
      objProdBean = (ProductBean)hshProdBolsa.get("objProductBean");
      objValue  =   ""+objProdBean.getNpminute();
      objDescr  =   ""+objProdBean.getNpminute();
      }else{
      objValue  =   "";
      objDescr  =   "";
      }
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('79','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    
    <%//Description
      objValue  =   MiUtil.getString(itemBean.getNpdescription());
      objDescr  =   MiUtil.getString(itemBean.getNpdescription());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('80','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
     /*Inicio - Adición de Banda Ancha - Cambio de Plan Tarifario*/
    <%//Servicio Principal Original
      objValue  =   MiUtil.getString(itemBean.getNporigmainservicedesc());
      objDescr  =   MiUtil.getString(itemBean.getNporigmainservicedesc());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('82','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%//Servicio Principal Original Id
      objValue  =   MiUtil.getString(itemBean.getNporigmainservice());
      objDescr  =   MiUtil.getString(itemBean.getNporigmainservice());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('84','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%//Nuevo Servicio Principal
      objValue  =   MiUtil.getString(itemBean.getNpnewmainservice());
      objDescr  =   MiUtil.getString(itemBean.getNpnewmainservicedesc());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('83','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%//Modelo
      objValue  =   MiUtil.getString(itemBean.getNpmodel());
      objDescr  =   MiUtil.getString(itemBean.getNpmodel());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('85','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%// Precio por Minutos
      if( hshProdBolsa.get("strMessage") == null ){
      objProdBean = (ProductBean)hshProdBolsa.get("objProductBean");
      objValue  =   ""+objProdBean.getNpminuteprice();
      objDescr  =   ""+objProdBean.getNpminuteprice();
      }else{
      objValue  =   "";
      objDescr  =   "";
      }
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('86','','','','','','','','','','','','<%=MiUtil.getString(objValue)%>','<%=MiUtil.getString(objDescr)%>','A','<%=itemId%>'));
    
    <%//Fecha Fin de Servicio
      objValue  =   MiUtil.toFecha(itemBean.getNpendservicedate());
      objDescr  =   MiUtil.toFecha(itemBean.getNpendservicedate());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('69','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Referencia de Teléfono
      objValue  =   MiUtil.getString(itemBean.getNpreferencephone());
      objDescr  =   MiUtil.getString(itemBean.getNpreferencephone());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('68','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
        
    /*Fin - Adición de Banda Ancha - Cambio de Plan Tarifario*/

    /*Inicio Acuerdos Comerciales*/
    <%    
      String priceOriginal = "";
      if( !MiUtil.getString(itemBean.getNporiginalprice()).equals("") ) {        
        priceOriginal = ""+MiUtil.parseDouble(MiUtil.getString(itemBean.getNporiginalprice()));
      }
      objValue  =   priceOriginal;
      objDescr  =   priceOriginal;
      //objValue  =   MiUtil.getString(itemBean.getNporiginalprice());
      //objDescr  =   MiUtil.getString(itemBean.getNporiginalprice());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('89','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));      
    <%
      objValue  =   MiUtil.getString(itemBean.getNppricetype());
      objDescr  =   MiUtil.getString(itemBean .getNppricetype());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('90','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    <%
      objValue  =   MiUtil.getString(itemBean.getNppricetypeid());
      objDescr  =   MiUtil.getString(itemBean.getNppricetypeid());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('91','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    <%
      objValue  =   MiUtil.getString(itemBean.getNppricetypeitemid());
      objDescr  =   MiUtil.getString(itemBean.getNppricetypeitemid());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('92','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    /*Fin Acuerdos Comerciales*/
    
     <%//Solución
      objValue  =   MiUtil.getString(itemBean.getNpsolutionid());
      objDescr  =   MiUtil.getString(itemBean.getNpsolutionname());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('93','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
     
    <%
      objValue  =   MiUtil.getString(itemBean.getNpfixedphone());
      objDescr  =   MiUtil.getString(itemBean.getNpfixedphone());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('94','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));

     <%//Estado de la Automatización /**KSALVADOR > AUTOMATIZACION*/
      objValue  =   MiUtil.getString(itemBean.getNpitemoperationstatus());
      objDescr  =   MiUtil.getString(itemBean.getNpitemoperationstatus());
      
       if( (  MiUtil.parseInt(hdnSpecification) == Constante.SPEC_CAMBIO_MODELO  || 
              MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA ||
              MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_NUEVA  ||
              MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_TDE    || 
              MiUtil.parseInt(hdnSpecification) == Constante.SPEC_REPOSICION_PREPAGO_TDE) 
              //Se agrego subcategoria reposicion prepago tde - TDECONV034                        
              && ( strFlagModality.equals(Constante.MODALITY_PROPIO) || strFlagModality.equals("Alquiler en Cliente")  )              
              && ("s".equals(strResult))
              || (MiUtil.parseInt(hdnSpecification) != Constante.SPEC_POSTPAGO_VENTA &&
                  MiUtil.parseInt(hdnSpecification) != Constante.SPEC_PREPAGO_NUEVA  &&
                  MiUtil.parseInt(hdnSpecification) != Constante.SPEC_CAMBIO_MODELO  &&
                  MiUtil.parseInt(hdnSpecification) != Constante.SPEC_PREPAGO_TDE    &&
                  MiUtil.parseInt(hdnSpecification) != Constante.SPEC_REPOSICION_PREPAGO_TDE)              
            ) {
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('96','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    <%}%>
    

    <%
      objValue  =   MiUtil.getString(itemBean.getNplocution());
      objDescr  =   MiUtil.getString(itemBean.getNplocution());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('105','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
	  /*Inicio Responsable de Pago*/
    <%
      objValue  =   MiUtil.getString(itemBean.getNpsiteid());
      objDescr  =   MiUtil.getString(itemBean.getNpsitename());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('97','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
      /*Fin Responsable de Pago*/
      
      /*Incio Contratos de Referencia*/
      <%
      objValue  =   MiUtil.getString(itemBean.getNptfrefphonenumber());
      objDescr  =   MiUtil.getString(itemBean.getNptfrefphonenumber());
      %>
      vctOrderItem.addElement(new fxMakeOrderItem('99','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
      <%
      objValue  =   MiUtil.getString(itemBean.getNpinternetrefcontract());
      objDescr  =   MiUtil.getString(itemBean.getNpinternetrefcontract());
      %>
      vctOrderItem.addElement(new fxMakeOrderItem('100','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));      
      <%
      objValue  =   MiUtil.getString(itemBean.getNpdatosrefcontract());
      objDescr  =   MiUtil.getString(itemBean.getNpdatosrefcontract());
      %>
      vctOrderItem.addElement(new fxMakeOrderItem('101','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
      <%
      objValue  =   MiUtil.getString(itemBean.getNpcontractref());
      objDescr  =   MiUtil.getString(itemBean.getNpcontractref());
      %>
      vctOrderItem.addElement(new fxMakeOrderItem('111','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
      /*Fin Contratos de Referencia*/
      
      //Agregado por RMARTINEZ 05-06-09 para Suspensiones 
    
      //para suspecion Definitiva Automática
    <%
      objValue  =   MiUtil.getString(itemBean.getNpestadoitemId());
      objDescr  =   MiUtil.getString(itemBean.getNpestadoitemDesc());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('95','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));    
      
    <%
      objValue  =   MiUtil.getString(itemBean.getNpestadoproceso());
      objDescr  =   MiUtil.getString(itemBean.getNpestadoproceso());
    %>
         
      vctOrderItem.addElement(new fxMakeOrderItem('96','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));      
      
      //Ajustes para suspecion temporal Automática
    <%
      objValue  =   MiUtil.getString(itemBean.getNpcobro());
      objDescr  =   MiUtil.getString(itemBean.getNpcobro());
    %>
         
      vctOrderItem.addElement(new fxMakeOrderItem('102','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));                
      
    <%
      objValue  =   MiUtil.getString(itemBean.getNpcontractnumber());
      objDescr  =   MiUtil.getString(itemBean.getNpcontractnumber());
    %>
         
      vctOrderItem.addElement(new fxMakeOrderItem('106','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>')); 
    //Fin Agregado por RMARTINEZ 05-06-09
    
     <%//Tipo de IP
      objValue  =   MiUtil.getString(itemBean.getNptypeip());
      objDescr  =   MiUtil.getString(itemBean.getNptypeipdesc());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('110','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
     
     //CBARZOLA:Cambio de Modelo- Distintas Tecnologias -Solucion Original(Id,Name)
     <%
      objValue  =   MiUtil.getString(itemBean.getNporiginalsolutionname());
      objDescr  =   MiUtil.getString(itemBean.getNporiginalsolutionname());
     %>
      vctOrderItem.addElement(new fxMakeOrderItem('103','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
     <%
      objValue  =   MiUtil.getString(itemBean.getNporiginalsolutionid());
      objDescr  =   MiUtil.getString(itemBean.getNporiginalsolutionid());
     %> 
     vctOrderItem.addElement(new fxMakeOrderItem('104','','','','','','','','','','','','<%=objDescr%>','<%=objDescr%>','A','<%=itemId%>'));
     
     //CPUENTE2 CAP & CAL
     <%
      objValue  =   MiUtil.getString(itemBean.getNpequipmenttime());
      objDescr  =   MiUtil.getString(itemBean.getNpequipmenttime());
     %> 
     vctOrderItem.addElement(new fxMakeOrderItem('112','','','','','','','','','','','','<%=objDescr%>','<%=objDescr%>','A','<%=itemId%>'));
     <%
      objValue  =   MiUtil.getString(itemBean.getNpestadoContrato());
      objDescr  =   MiUtil.getString(itemBean.getNpestadoContrato());
     %> 
     vctOrderItem.addElement(new fxMakeOrderItem('113','','','','','','','','','','','','<%=objDescr%>','<%=objDescr%>','A','<%=itemId%>'));
     <%
      objValue  =   MiUtil.getString(itemBean.getNpmotivoEstado());
      objDescr  =   MiUtil.getString(itemBean.getNpmotivoEstado());
     %> 
     vctOrderItem.addElement(new fxMakeOrderItem('114','','','','','','','','','','','','<%=objDescr%>','<%=objDescr%>','A','<%=itemId%>'));
     <%
      objValue  =   MiUtil.getString(itemBean.getNpfechaCambioEstado());
      objDescr  =   MiUtil.getString(itemBean.getNpfechaCambioEstado());
     %> 
     vctOrderItem.addElement(new fxMakeOrderItem('115','','','','','','','','','','','','<%=objDescr%>','<%=objDescr%>','A','<%=itemId%>'));
     <%
      objValue  =   MiUtil.getString(itemBean.getNpreasonchange());
      objDescr  =   MiUtil.getString(itemBean.getNpreasonchange());
     %> 
     vctOrderItem.addElement(new fxMakeOrderItem('116','','','','','','','','','','','','<%=objDescr%>','<%=objDescr%>','A','<%=itemId%>'));
     <%
      objValue  =   MiUtil.getString(itemBean.getNpownequipment());
      objDescr  =   MiUtil.getString(itemBean.getNpownequipment());
     %> 
     vctOrderItem.addElement(new fxMakeOrderItem('117','','','','','','','','','','','','<%=objDescr%>','<%=objDescr%>','A','<%=itemId%>'));
    //CPUENTE2 CAP & CAL 
     <%
      objValue  =   MiUtil.getString(itemBean.getNpLevel());
      objDescr  =   MiUtil.getString(itemBean.getNpLevelDesc());
     %> 
     vctOrderItem.addElement(new fxMakeOrderItem('118','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
     <%//Fecha de activación del SSAA - Suscripciones
      objValue  =   MiUtil.toFecha(itemBean.getNpactivationdate());
      objDescr  =   MiUtil.toFecha(itemBean.getNpactivationdate());
     %>
     
     vctOrderItem.addElement(new fxMakeOrderItem('119','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
     
     <%//DLAZO - Suscripciones
      objValue  =   MiUtil.getString(itemBean.getNpitemservices());
      objDescr  =   MiUtil.getString(itemBean.getNpitemservices());
     %>  
      
      vctOrderItem.addElement(new fxMakeOrderItem('120','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
      
    /*jsalazar - modif hpptt # 1 - 29/09/2010 - 01022011 inicio*/

    <%
      objValue  =   MiUtil.getString(itemBean.getNpimeicustomer());
      objDescr  =   MiUtil.getString(itemBean.getNpimeicustomer());
     %> 
     vctOrderItem.addElement(new fxMakeOrderItem('121','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));

     <%
      objValue  =   MiUtil.getString(itemBean.getNpownimeisim());
      objDescr  =   MiUtil.getString(itemBean.getNpownimeisim());
     %> 
     vctOrderItem.addElement(new fxMakeOrderItem('122','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));


    <%
      objValue  =   MiUtil.getString(itemBean.getNpestadoitemId());
      objDescr  =   MiUtil.getString(itemBean.getNpestadoitemDesc());
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('<%=strStatusItemExecution%>','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));    
    /*jsalazar - modif hpptt # 1 - 29/09/2010 - fin*/
    
    <%//Fecha de desactivación del SSAA - Suscripciones
      objValue  =   MiUtil.toFecha(itemBean.getNpdeactivationdate());
      objDescr  =   MiUtil.toFecha(itemBean.getNpdeactivationdate());
     %>
     
     vctOrderItem.addElement(new fxMakeOrderItem('125','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
     
     <%//Modelo - Por Garantia Extendida - 15/11/2010 - FPICOY
       objValue  =   MiUtil.getString(itemBean.getNpproductmodelid());
       objDescr  =   objValue;
     %>  
     vctOrderItem.addElement(new fxMakeOrderItem('124','','','','','','','','','','','','<%=objValue%>',"<%=objDescr%>",'A','<%=itemId%>'));
     
     <%//Modelo - Por Reserva de Numero - 15/11/2010 - FPICOY
       objValue  =   MiUtil.getString(itemBean.getCadNumberReserve());
       objDescr  =   objValue;
     %>  
     vctOrderItem.addElement(new fxMakeOrderItem('123','','','','','','','','','','','','<%=objValue%>',"<%=objDescr%>",'A','<%=itemId%>'));
     
     //---------------------------
     <%//Flag Vep - 03/12/2012 - RMARTINEZ
       objValue  =   MiUtil.getString(itemBean.getNpVepItem());
       objDescr  =   objValue;
     %>  
     vctOrderItem.addElement(new fxMakeOrderItem('131','','','','','','','','','','','','<%=objValue%>',"<%=objDescr%>",'A','<%=itemId%>'));
     
     <%//Cuota Item Vep - 03/12/2012 - RMARTINEZ
       objValue  =   ""+itemBean.getNpVepTotalPrice();
       objDescr  =   objValue;
     %>  
     vctOrderItem.addElement(new fxMakeOrderItem('132','','','','','','','','','','','','<%=objValue%>',"<%=objDescr%>",'A','<%=itemId%>'));   
     
     
     //-----------------------------
     
     <% // MMONTOYA - Despacho en tienda
       objValue  =   MiUtil.getString(itemBean.getNpproductstatus());
       objDescr  =   objValue;
     %> 
     vctOrderItem.addElement(new fxMakeOrderItem('135','','','','','','','','','','','','<%=objValue%>',"<%=objDescr%>",'A','<%=itemId%>'));
     
     <% // MMONTOYA - Despacho en tienda
       objValue  =   MiUtil.getString(itemBean.getNpchanged());
       objDescr  =   objValue;
     %> 
     vctOrderItem.addElement(new fxMakeOrderItem('136','','','','','','','','','','','','<%=objValue%>',"<%=objDescr%>",'A','<%=itemId%>'));
     
     <% // MMONTOYA - Despacho en tienda
       objValue  =   MiUtil.getString(itemBean.getNpflagaccessory());
       objDescr  =   objValue;
     %> 
     vctOrderItem.addElement(new fxMakeOrderItem('137','','','','','','','','','','','','<%=objValue%>',"<%=objDescr%>",'A','<%=itemId%>'));

     <% // MMONTOYA - ADT-RCT-092
       objValue  =   MiUtil.getString(itemBean.getNpserviceROA());
       objDescr  =   objValue;
     %>
     vctOrderItem.addElement(new fxMakeOrderItem('139','','','','','','','','','','','','<%=objValue%>',"<%=objDescr%>",'A','<%=itemId%>'));
     
      <%//LHUAPAYA - ADT-BCL-083
       objValue  =   ""+itemBean.getNpproductid();
       objDescr  =   ""+itemBean.getNpproductname();
      %>
      vctOrderItem.addElement(new fxMakeOrderItem('138','','','','','','','','','','','','<%=objValue%>',"<%=objDescr%>",'A','<%=itemId%>'));

     <% // MMONTOYA - ADT-RCT-092
       objValue  =   MiUtil.getString(itemBean.getNpservplantype());
       objDescr  =   objValue;
     %>
     vctOrderItem.addElement(new fxMakeOrderItem('140','','','','','','','','','','','','<%=objValue%>',"<%=objDescr%>",'A','<%=itemId%>'));

     <% // MMONTOYA - ADT-RCT-092
       objValue  =   MiUtil.getString(itemBean.getNpservbagcode());
       objDescr  =   objValue;
     %>
     vctOrderItem.addElement(new fxMakeOrderItem('141','','','','','','','','','','','','<%=objValue%>',"<%=objDescr%>",'A','<%=itemId%>'));

     <% // MMONTOYA - ADT-RCT-092
       objValue  =   MiUtil.getString(itemBean.getNpservbagtype());
       objDescr  =   objValue;
     %>
     vctOrderItem.addElement(new fxMakeOrderItem('142','','','','','','','','','','','','<%=objValue%>',"<%=objDescr%>",'A','<%=itemId%>'));

     <% // MMONTOYA - ADT-RCT-092
       objValue  =   MiUtil.toFecha(itemBean.getNpservvalidactivationdate());
       objDescr  =   objValue;
     %>
     vctOrderItem.addElement(new fxMakeOrderItem('143','','','','','','','','','','','','<%=objValue%>',"<%=objDescr%>",'A','<%=itemId%>'));

     <% // MMONTOYA - ADT-RCT-092
       objValue  =   MiUtil.getString(itemBean.getNpservvalidity());
       objDescr  =   objValue;
     %>
     vctOrderItem.addElement(new fxMakeOrderItem('145','','','','','','','','','','','','<%=objValue%>',"<%=objDescr%>",'A','<%=itemId%>'));
     

      <% // LHUAPAYA - ADT-BCL-083
    objValue  =   MiUtil.getString(itemBean.getNptypeproductBC());
    objDescr  =   objValue;
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('146','','','','','','','','','','','','<%=objValue%>',"<%=objDescr%>",'A','<%=itemId%>'));

      <% //PRY-0721 DERAZO Se carga la region del Item para mostrar en el detalle
        objValue  =   MiUtil.getString(itemBean.getNpzonacoberturaid());
        objDescr  =   MiUtil.getString(itemBean.getNpnombrezona());
      %>
      vctOrderItem.addElement(new fxMakeOrderItem('150','','','','','','','','','','','','<%=objValue%>',"<%=objDescr%>",'A','<%=itemId%>'));

      <%//PRY-0762 JQUISPE Se carga la cantidad de Renta Adelantada del Item para mostrar en el detalle
      Integer cantidadRentaAdelantada = itemBean.getNpcantidadRentaAdelantada();
      if(isRentaAdelantada && cantidadRentaAdelantada != null){
         objValue  =   MiUtil.getString(cantidadRentaAdelantada);
         objDescr  =   MiUtil.getString(cantidadRentaAdelantada);	
      %>
         vctOrderItem.addElement(new fxMakeOrderItem('148','','','','','','','','','','','','<%=objValue%>',"<%=objDescr%>",'A','<%=itemId%>'));
      <%
        }        
      %>

      <%//PRY-0762 JQUISPE Se carga el total de Renta Adelantada del Item para mostrar en el detalle
        String totalRentaAdelantada = itemBean.getNptotalRentaAdelantada();
        if(isRentaAdelantada && totalRentaAdelantada != null){
           objValue  =   MiUtil.getString(totalRentaAdelantada);
           objDescr  =   MiUtil.getString(totalRentaAdelantada);
      %>
         vctOrderItem.addElement(new fxMakeOrderItem('149','','','','','','','','','','','','<%=objValue%>',"<%=objDescr%>",'A','<%=itemId%>'));
      <%
       }        
      %>

       <%//CDM + CDP EFLORES Item Mantener SIM PRY-0817
       long keepSim = itemBean.getNpkeepSIM();
       objValue  =   MiUtil.getString(keepSim);
       objDescr  =   MiUtil.getString(keepSim);
       %>

      vctOrderItem.addElement(new fxMakeOrderItem('151','','','','','','','','','','','','<%=objValue%>',"<%=objDescr%>",'A','<%=itemId%>'));
      
<%//BAFI2 EFLORES
         objValue  =   MiUtil.getString(itemBean.getNpProvinceZoneId());
         objDescr  =   MiUtil.getString(itemBean.getNpNameProvinceZone());
       %>
      vctOrderItem.addElement(new fxMakeOrderItem('152','','','','','','','','','','','','<%=objValue%>',"<%=objDescr%>",'A','<%=itemId%>'));

      <%//BAFI2 EFLORES
        objValue  =   MiUtil.getString(itemBean.getNpDistrictZoneId());
        objDescr  =   MiUtil.getString(itemBean.getNpNameDistrictZone());
      %>
      vctOrderItem.addElement(new fxMakeOrderItem('153','','','','','','','','','','','','<%=objValue%>',"<%=objDescr%>",'A','<%=itemId%>'));

      <%//INICIO Monto Prorrateo PRY-0890
      if(isProrrateo){    	  
      objValue  =   MiUtil.getString(itemBean.getNpprorrateoPrice());
      objDescr  =   MiUtil.getString(itemBean.getNpprorrateoPrice());
      %>      
      vctOrderItem.addElement(new fxMakeOrderItem('154','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));      
      <%
      } //FIN Monto Prorrateo PRY-0890      
      %>
      
      <% //INICIO DERAZO TDECONV003-2 Se carga el IMEI FullStack del Item para mostrar en el detalle
        objValue  =   MiUtil.getString(itemBean.getNpOwnImeiFS());
        objDescr  =   MiUtil.getString(itemBean.getNpOwnImeiFS());
      %>
      vctOrderItem.addElement(new fxMakeOrderItem('165','','','','','','','','','','','','<%=objValue%>',"<%=objDescr%>",'A','<%=itemId%>'));

      fxAddRowOrderItemsGlobalEdit(vctOrderItem);
  <%      
    }
  }
  %>
    try{
      itemsCargados = true;
      document.getElementById("btnEvaluarVO").disabled = true;
    }catch(e){}
          
    try{

    	// PRY-0890 JBALCAZAR    
	    <%if(isProrrateo){%>
	  	var hdnProrrateo = $("input[name='hdnApportionment']").val();
	    var temptotalprorrateo = $("#txtTotalPriceApportionment").val();
	    var temptotalitems = $("#divLeyendaTotal tr td input[name='txtTotalSalesPrice']").val();
	    var totalprorrateo = 0;
	    var totalitems = 0;  
		var statusOrden = $("#txtEstadoOrden").val();
		
	    document.getElementById("divProrrateo").style.display = 'block';
	    document.getElementById("divBtnProrrateo").style.display = 'block';
	    $("#tabLeyendaTotal  tr td:nth-child(1)").width('46%');
	    $("#tabLeyendaTotal  tr td:nth-child(2)").width('44%');
	    $("#tabLeyendaTotal  tr td:nth-child(3)").width('10%');
	          

	  	  if(hdnProrrateo != 'S'){		  		 
	  		  document.getElementById("textApportionmentN").checked = true;
	  		  document.getElementById('btnCalculoPagoAnticipado').disabled = true;
	  		  $("input[name='hdnApportionment']").val('N');
	  		  
	  	  	$("#items_table tr td div input[name='txtMontoProrrateo']").each(function () {		
		  		$(this).val("0.00");
		      });
	  	  $("#txtTotalPriceApportionment").val("0.00");
	  	  
	  	  }else if(hdnProrrateo =='S'){
	  		  document.getElementById("textApportionmentS").checked = true;
	  		  document.getElementById('btnCalculoPagoAnticipado').disabled = false;		  		  
	  	  }
	  	  
		  	var suma = "0.0";
		  	$("#items_table tr td div input[name='txtMontoProrrateo']").each(function () {
		  		if($(this).val()==0){		
		  		$(this).val("0.00");
		  		}
				 suma = parseFloat(suma) + parseFloat($(this).val());		  		
		      });
			$("#txtTotalPriceApportionment").val(parseFloat(suma).toFixed(0));
		  	
	//  Valida PostPago	    
	    <%
	    boolean	validaPostPago=false;
	    validaPostPago = (Boolean)objOrderService.isClientPostPago(MiUtil.parseLong(strOrderId));
	    if(validaPostPago){	    	
	    %>
	    	document.getElementById("spnMsjProrrateo").innerHTML ="NO APLICA PARA PAGO ANTICIPADO";	
		    document.getElementById("textApportionmentS").checked = false;
		    document.getElementById("textApportionmentN").checked = false;
		    document.getElementById("textApportionmentS").disabled = true;
		    document.getElementById("textApportionmentN").disabled = true;		    
		    $("input[name='hdnApportionment']").val('N');
		    document.getElementById('btnCalculoPagoAnticipado').disabled = true;
	      	$("#items_table tr td div input[name='txtMontoProrrateo']").each(function () {
	      		$(this).val("");
	          });	      	
	      	$("#items_table tr td div input[name='hdnItemValuetxtMontoProrrateo']").each(function () {
	    		$(this).val("");
	        });	
	    	 $("#txtTotalPriceApportionment").val("");	      	
	   	<% 	 
	    }
	    %> 
	        
	// Fin Valida PostPago
	//Validacion Tienda01
	  if(statusOrden != 'TIENDA01'){
		  document.getElementById('btnCalculoPagoAnticipado').disabled = true;
		    document.getElementById("textApportionmentS").disabled = true;
		    document.getElementById("textApportionmentN").disabled = true;	  
	  }
	
	
	// Fin Validacion Tienda01
	

	  	 if(temptotalprorrateo == 'undefined' || temptotalprorrateo == undefined || temptotalprorrateo == ''){
	  			totalprorrateo = 0;
	  	 }else{	        						
	  			totalprorrateo = $("#txtTotalPriceApportionment").val();
	  		   }

	  	if(temptotalitems == 'undefined' || temptotalitems == undefined || temptotalitems == ''){
	  			totalitems = 0;
	  	}else{	
	  			totalitems = $("#divLeyendaTotal tr td input[name='txtTotalSalesPrice']").val();
	  		   }
	  				  	
	    		        					
	  		$("#txtTotalPrice").val((parseFloat(totalprorrateo)+parseFloat(totalitems)).toFixed(2));
	    
	    <%}%> 	
	  
    }catch(e){}
    
  }

  </script>
  
  <SCRIPT DEFER>
  
  //Invoca al PopUp para la edición de un Item
  function fxAddRowOrderItemsGlobalEditBySimPropio(vctItemOrder,intQuantity,arrSIMPropio){
    
    form = parent.mainFrame.document.frmdatos;
    var index_arg = 1;
    var elemText = "";
    var cantElement;
    
    var valuevisible = "";
    var descvisible = "";
    var typeControl = "";
    var cell;
    
    var contentHidden = "";
    var numPaymentOrderId= "<%=strNumPaymentOrderId%>"; //CEM COR0323
    var numGuideNumber= "<%=strNumGuideNumber%>"; 		//CEM COR0426
    cantElement = vctItemOrder.size();
    
        if (cantElement > 1) {/* los argumentos pasados.. a partir del segundo. */
        
          for( f=0; f<intQuantity; f++){
          
          fxaddElementsItemsMain(vctItemOrder);
          
          var row   = items_table.insertRow(-1);
          
          elemText =  "";
          var new_grupo = parseInt(form.hdn_item_imei_grupo.value) + parseInt(1);
          form.hdn_item_imei_grupo.value = new_grupo;
          
          // Inicio CGC - COR0386
          var v_pagpermitida;          
          var v_bolpermit; 
          var v_delpermit;
          <%
			 			 
          if ("Enabled".equals(MiUtil.getString((String)hshScreenField.get("edititems")))){
            System.out.println("[PRY-0710] Orden "+lOrderId+" Puede Editar los items");
          %> // CGC - COR0386

				var bolInboxConExcepcion="<%=bolInboxConExcepcion%>";
				if (bolInboxConExcepcion=="true"){		
					v_pagpermitida="fxChangeItemEditDetailOrder";
					v_bolpermit=1; // Edición
					v_delpermit="<%=strDeleteItem%>";				
				}
				else{
					if ( (dPaymentTotal > 0) && (strPaymentStatus.toUpperCase() == "CANCELADO" )){					
                        <% // PRY-0710 Cambio para obtener rol para modificar Producto
                        if (intFlagSpecificationModProd == 1 && intFlagInboxModProd == 1 && intFlagUserRolModProd == 1)  {
                             %>
                                v_pagpermitida="fxChangeItemEditDetailOrder";
                                v_bolpermit=1; // Edición
                                v_delpermit="<%=strDeleteItem%>";
                            <% }else{%>
						v_pagpermitida="fxChangeItemEditDetailOrderDetail";          
						v_bolpermit=2; // Consulta
						v_delpermit="<%=strDeleteItem%>";        				
                            <% } %>
					}else{
						v_pagpermitida="fxChangeItemEditDetailOrder";
						v_bolpermit=1; // Edición
						v_delpermit="<%=strDeleteItem%>";
				}		
           }         
         <%}else{
         System.out.println("[PRY-0710] Orden "+lOrderId+" NO Puede Editar los items");
         %>
            v_pagpermitida="fxChangeItemEditDetailOrderDetail";          
            v_bolpermit=2; // Consulta
			   v_delpermit="<%=strDeleteItem%>";
          <%}%>
          // Fin CGC - COR0386
          
          /*Agrego la primera línea*/
          var cellPrinc = row.insertCell(index_arg - 1);
          elemText      = "<div id=\'contTable\' align='center' class='CellContent' >"+
                          "<input type=\'radio\' name=\'item_chek\' onclick=\'javascript:fxCargaServiciosItemItem(fxGetRowIndex(this));\'>"+
                          "<a href=\'javascript:;\' onclick=\'javascript:" + v_pagpermitida + "(fxGetRowIndex(this),parent.mainFrame.vctItemsMainFrameOrder,parent.mainFrame.vctParametersOrder);\' ALT=\'Editar Item\'><font color=\'brown\' size=\'2\' face=\'Arial\' ><b>"+ form.hdn_item_imei_grupo.value +"<"+"/b><"+"/font></"+"a> "+
                          "<a href=\'javascript:;\' onclick=\'javascript:fxAssignmentBillingAcount(fxGetRowIndex(this),parent.mainFrame.vctItemsMainFrameOrder,parent.mainFrame.vctParametersOrder);\' ALT=\'Editar Item\'>Billing<"+"/a> "+
                          "<input type=\'hidden\' name='hdnIndice' value="+ document.frmdatos.hdn_item_imei_grupo.value +" >" +
                          "<input type=\'hidden\' name='hdnFlagSave' value='"+ vctItemOrder.elementAt(0).npobjitemflagsave +"' >" +
                          "<input type=\'hidden\' name='hdnItemId' value='"+ vctItemOrder.elementAt(0).npitemid +"' >" +
                          "<"+"/div>";
          //alert("Agregando un Item : " + vctItemOrder.elementAt(0).npitemid);          
          cellPrinc.innerHTML = elemText;
          
          index_arg++;
          
          elemText = "";
          
          for( i = 0; i < vctItemHeaderOrder.size(); i++ ){

            var objItemHeader = vctItemHeaderOrder.elementAt(i);
            var valFlag = false;
            //Siempre mostrar Text y si coinciden asignar el Valor
              for( j = 0; j < vctItemOrder.size(); j++ ){

              var objItem = vctItemOrder.elementAt(j);
              
              //alert("[i][" + i + "][j]["+j+"] -> ( objItemHeader " + objItemHeader.npobjitemheaderid + " == objItem " + objItem.npobjitemheaderid + " ) -> " + objItem.nphtmlname );
              
              if( vctItemOrder.elementAt(j).npobjitemheaderid == 2 ){
                vctItemOrder.elementAt(j).npobjitemvalue = arrSIMPropio[f];
                vctItemOrder.elementAt(j).npobjitemvaluedesc = arrSIMPropio[f];
              }
              
                if ( objItemHeader.npobjitemheaderid == objItem.npobjitemheaderid ) {
                
                    valuevisible = " value='"+ objItem.npobjitemvalue + "' ";
                    descvisible  = " value='"+ objItem.npobjitemvaluedesc + "' ";												
                    if ( objItemHeader.npdisplay == "N" ) {
                        contentHidden += "<input type='hidden' name='hdnItemHeaderId' value='"+objItemHeader.npobjitemheaderid+"' >";
                        contentHidden += "<input type='hidden' name='hdnItemValue"+trim(objItemHeader.nphtmlname)+"' "+valuevisible+" >";
                        contentHidden += "<input type='hidden' name='"+ trim(objItemHeader.nphtmlname) +"' " + descvisible + " > ";
                    }else{
                        //alert("HTML : " +objItemHeader.nphtmlname + " index_arg " + index_arg)
                        cell = row.insertCell(index_arg - 1);
                        elemText += "<div id='"+ objItemHeader.nphtmlname +"'  align='center' class='CellContent' > ";
                        elemText += "<input type='hidden' name='hdnItemHeaderId' value='"+objItemHeader.npobjitemheaderid+"' >";
                        elemText += "<input type='hidden' name='hdnItemValue"+trim(objItemHeader.nphtmlname)+"' "+valuevisible+" >";
                        elemText += "<input type='text'   name='"+ trim(objItemHeader.nphtmlname) +"' ";
                        elemText += descvisible;
                        if  ( (objItemHeader.npobjitemheaderid==2) || (objItemHeader.npobjitemheaderid==5) || (objItemHeader.npobjitemheaderid==4) ) {
                           elemText += " size='13' style='text-align:right' readOnly><"+"/div>";
                        } else if (objItemHeader.npobjitemheaderid==139){ // MMONTOYA [ADT-RCT-092 Roaming con corte]
                           elemText += " size='35' style='text-align:right' readOnly></div>";
                        } else{
                           if( (objItemHeader.npobjitemheaderid==10) || (objItemHeader.npobjitemheaderid==16) )
                              elemText += " size='22' style='text-align:right' readOnly><"+"/div>";
                           else{
                              if( (objItemHeader.npobjitemheaderid==3) || (objItemHeader.npobjitemheaderid==11) )
                                 elemText += " size='10' style='text-align:right' readOnly><"+"/div>";
                              else {
                                  if(objItemHeader.npobjitemheaderid==51){
                                      <%if(MiUtil.parseInt(hdnSpecification) == Constante.SPEC_BOLSA_CREAR || MiUtil.parseInt(hdnSpecification) == Constante.SPEC_BOLSA_UPGRADE || MiUtil.parseInt(hdnSpecification) == Constante.SPEC_BOLSA_DOWNGRADE || MiUtil.parseInt(hdnSpecification) == Constante.SPEC_BOLSA_DESACTIVAR ){%> // LHUAPAYA  [ADT-BCL-083]
                                      elemText += " size='35' style='text-align:right' readOnly></div>";
                                      <%}else{%>
                                      elemText += " size='12' style='text-align:right' readOnly></div>";
                                      <%}%>
                                  }else {
                                      elemText += " size='12' style='text-align:right' readOnly><" + "/div>";
                                  }
                              }
                           }
                        }
                        cell.innerHTML = elemText;
                        elemText =  "";
                        index_arg++;
                    }
                    valFlag = true;
                    break;
                   
                }//Fin del If
               
             }//Fin del For
           if(!valFlag){
             //alert("[i][" + i + "] -> objItemHeader " + objItemHeader.npobjitemheaderid  + " --> " + objItemHeader.nphtmlname);  
             contentHidden += "<input type='hidden' name='hdnItemHeaderId' value='"+objItemHeader.npobjitemheaderid+"' >";
             contentHidden += "<input type='hidden' name='hdnItemValue"+trim(objItemHeader.nphtmlname)+"'  >";
             contentHidden += "<input type='hidden' name='"+ trim(objItemHeader.nphtmlname) +"'  > ";         
           }
           
          }//Fin del For
            
            var cellDelete = row.insertCell(index_arg-1);            
            var showDeleteButton=0;             
            
            //if (v_bolpermit==1){ // Pregunta si se puede editar
            //   showDeleteButton=1; // Editable
            //}            
            if (v_delpermit=='Enabled'){
			   showDeleteButton=1; // Editable
			}
			
            /* Boton de Borrar Item */
            elemText =                           "<input type='hidden' name='item_adde_period' value=''>" +
                                                 "<input type='hidden' name='item_adde_type' value=''>" +
                                                 "<input type='hidden' name='item_own_equip_new' value=''>" +
                                                 "<input type='hidden' name='hdnnpPromoHeaderId' value=''>" +
                                                 "<input type='hidden' name='hdnnpPromoDetailId' value=''>" +
                                                 "<input type='hidden' name='hdnnpPromoHeaderName' value=''>" +
                                                 "<input type='hidden' name='hdnnpOracleCode' value=''>" +
                                                 "<input type='hidden' name='hdnnpRealImei' value=''>" +
                                                 
                                                 "<input type='hidden' name='item_performance_flag' value='n'>" +
                                                 "<input type='hidden' name='item_perform_flag_serv' value='n'>" +
                                                 "<input type='hidden' name='hdnmodality' value=''>" +
                                                 "<input type='hidden' name='hdnreplacementmode' value=''>" +
                                                 "<input type='hidden' name='hdnnewmodality' value=''>" +
                                                 "<input type='hidden' name='item_excepBasicRentDesc'>" +
                                                 "<input type='hidden' name='item_excepBasicRentException'>" +
                                                
                                                 "<input type='hidden' name='item_excepRentDesc'>" +
                                                 "<input type='hidden' name='item_excepRentException'>" +
                                                
                                                 "<input type='hidden' name='item_excepServiceId'>" +
                                                 "<input type='hidden' name='item_excepServiceDiscount'>" +
                                                 
                                                 //"<input type='hidden' name='item_billingAccount'>" +
                                                
                                                 "<input type='hidden' name='item_excepMinAddConexDirecChecked'>" +
                                                 "<input type='hidden' name='item_excepMinAddInterConexChecked'>" +
                                                 
            
                        "<div id='eliminar' align='center' class='CellContent' >" +
                        "<a href='javascript:fVoid()' onclick='javascript:fxDeleteItemEdit(this,this.parentNode.parentNode.parentNode.rowIndex,(" + (form.hdn_item_imei_grupo.value) + "),"+ showDeleteButton + ");'  ><img src='<%=Constante.PATH_APPORDER_SERVER%>/images/Eliminar.gif' border='no' ALT='Borrar Item'><"+"/a>"+
                        "<"+"/div>";						

            cellDelete.innerHTML = elemText+contentHidden;
            
            index_arg = 1;
            
         }//Fin For
         
       }//Fin del If CantElements
       
        wn_items = (items_table.rows.length -1); // numero de items 
        
          if ( wn_items > 1 ){
              wb_existItem =true;
          }
    

    fxCalculateTotal();
  
  }
  
  function fxSectionItemsOnload(){
  
    fxLoadData();
    vflagEnabled = true;
    try{
    fxLoadImeisDB();
    }catch(e){}
    try{
    fxValidNumRowsItem();
    }catch(e){}
    
    try{
     fxOnLoadImeiService();
    }catch(e){}
    
    try{
       fxOnLoadServicesCM();
    }catch(e){}    
  }
  
   //Function que direcciona a una ventana popUpShowError
   function fxShowErrorImeiSim(error, itid, itdev){
      var errorEstOp   = error;
      var itemId       = itid;
      var itemDeviceId = itdev;        
    	   var winUrl = "<%=request.getContextPath()%>/DYNAMICSECTION/DynamicSectionItems/DynamicSectionItemsPage/PopUpErrorOperationImeiSim.jsp?"+
         "strOrderId="+<%=strOrderId%>+"&strItemId="+itemId+"&strItemDeviceId="+itemDeviceId;
         window.open(winUrl,"Error_Operación_IMEI_SIM","toolbar=no,location=0,modal=yes,titlebar=0,directories=no,status=yes,menubar=0,scrollbars=no,screenX=100,top=350,left=500,screenY=80,width=250,height=250");        
   }  
   
   function fxOnLoadServicesCM(){
      
      var servicios_item_sendCM = "";
      var count_itemsCM     = getNumRows("items_table");
      var values;
           
      <%
      if( objItemOrder!=null && objItemOrder.size()>0 ){
        ServiciosBean serviciosBean = new ServiciosBean();
        //objGeneralService = new GeneralService();
        for(int i=0; i<objItemOrder.size(); i++ ){
          itemBean  = (ItemBean)objItemOrder.get(i);
          
          %> 
          
         if(count_itemsCM < 2){
            servicios_item_sendCM = document.frmdatos.item_services.value;
         }else{
            servicios_item_sendCM = document.frmdatos.item_services['<%=i%>'].value;
         }
         //alert("servicios_item_sendCM2 " + servicios_item_sendCM);
         
         <%
            for(int j=0; j<itemBean.getNpServiceItemList().size(); j++ ){
               serviciosBean = (ServiciosBean)itemBean.getNpServiceItemList().get(j);
               serviciosBean = objGeneralService.getDetailService(serviciosBean.getNpservicioid());               
         %>
              //alert("servicioid " + '<%=serviciosBean.getNpservicioid()%>');
              if ( servicios_item_sendCM.indexOf('<%=serviciosBean.getNpservicioid()%>')>-1 ){
                 var wv_indice = servicios_item_sendCM.indexOf('<%=serviciosBean.getNpservicioid()%>');
                 var wv_long   = ('<%=serviciosBean.getNpservicioid()%>' + "|").length;
                 var wv_servicio_act = servicios_item_sendCM.substring(wv_indice + wv_long ,wv_indice + wv_long + 1);
                 var wv_servicio_new = servicios_item_sendCM.substring(wv_indice + wv_long + 2 ,wv_indice + wv_long + 3);
                 
                 if(wv_servicio_act == 'S' && wv_servicio_new == 'N' & "ALQ" == '<%=serviciosBean.getNpexcludingind()%>'){
                    vIdModelos.addElement('<%=serviciosBean.getNpservicioid()%>'+'|'+'<%=serviciosBean.getNpnomserv()%>'+'|'+'<%=serviciosBean.getNpnomcorserv()%>'+'|'+'<%=itemBean.getNporiginalproductid()%>');
                 }else if(wv_servicio_act == 'S' && wv_servicio_new == 'S' & "ALQ" == '<%=serviciosBean.getNpexcludingind()%>'){                
                    vIdModelos.addElement('<%=serviciosBean.getNpservicioid()%>'+'|'+'<%=serviciosBean.getNpnomserv()%>'+'|'+'<%=serviciosBean.getNpnomcorserv()%>'+'|'+'<%=itemBean.getNporiginalproductid()%>');
                 }
              }
         <%      
            }
         %>

      <%
        }
      } %>   
       
   }   

  //Agregado por César Lozza 11/11/2010 
  //Volumen de Orden
  
  //Estados
  //0: No se aplicó evaluación de volumen de orden
  //1: Se aplicó evaluación de volumen de orden
  //2: Se tiene volver a hacer la evaluación de volumen de orden
  //3: La orden cargada al inicio tiene items con promocion de volumen de orden
  //4: Se tiene volver a hacer la evaluación de volumen de orden (no mostrar las columnas de VO al agregar items)
  var aplicoVO = "0";
  
  //Indica si termino la carga inicial de items de la orden
  var itemsCargados = false;
  
  <%if (volumeCount > 0){%>
    aplicoVO = "3";
  <%}%>
  
  function fxEvaluateVO(){
          
    var a = appContext+"/itemServlet?hdnMethod=evaluateOrderVolume&strCustomerId=<%=strCustomerId%>&strSpecificationId=<%=hdnSpecification%>&hdnOrderId=<%=strOrderId%>&type_window=<%=Constante.PAGE_ORDER_EDIT%>";
    form.action = a;
    form.submit();
  }
  
  function fxShowColumnsVO(flag){ 
  
    var display;
    
    if(flag){
      display = "";
    }
    else{
      display = "none";
    }
    
    document.getElementById('TotalVOSalesPrice').style.display = display;
        
    document.getElementById("Cabecera126").style.display = display;
    document.getElementById("Cabecera127").style.display = display;
    
    var cntItems          = items_table.rows.length;
    
    if( cntItems == 2 ){    
      indice      = form.hdnIndice.value;
      document.getElementById("Celda126"+indice).style.display = display;
      document.getElementById("Celda127"+indice).style.display = display;
    }else{    
      for(i = 0; i < cntItems-1; i++){
        indice      = form.hdnIndice[i].value;
        document.getElementById("Celda126"+indice).style.display = display;
        document.getElementById("Celda127"+indice).style.display = display;
      }      
    }
  }
  
  function fxCalculateTotalVO(){
    var form = parent.mainFrame.document.frmdatos;
    var total = "0";
    var priceVO = "0.0";
    var totalItem;
    var cantidad;
    var indice;
    var checkAplicarVO;
    
    if( (items_table.rows.length -1) == 1 ){    
      
      if(form.hdnIndice.value == undefined){
        indice = form.hdnIndice[0].value;
      }
      else{
        indice = form.hdnIndice.value;
      }
      
      checkAplicarVO = document.getElementById('chkAplicarVO'+indice);
      
      if(checkAplicarVO != null && checkAplicarVO.checked){
        priceVO = document.getElementById('txtItemPriceCtaInscripVO'+indice).value;
                
        if(form.txtItemQuantity.value == undefined){
          cantidad = form.txtItemQuantity[0].value;
        }
        else{
          cantidad = form.txtItemQuantity.value;
        }
        totalItem = parseFloat(priceVO) * parseInt(cantidad);
      }else{
        form.txtitemTotal.value = parseFloat(form.txtitemTotal.value);
        totalItem = form.txtitemTotal.value;        
      }
      
      total = totalItem;
    }else if((items_table.rows.length -1) > 1 ){
    
      for(i=0; i<form.txtitemTotal.length; i++){
      
        indice = form.hdnIndice[i].value;        
        checkAplicarVO = document.getElementById('chkAplicarVO'+indice);
        
        if(checkAplicarVO != null && checkAplicarVO.checked){
          priceVO = document.getElementById('txtItemPriceCtaInscripVO'+indice).value;
          cantidad = form.txtItemQuantity[i].value;
          totalItem = parseFloat(priceVO, 2) * parseInt(cantidad);
        }else{
          form.txtitemTotal[i].value = parseFloat(form.txtitemTotal[i].value, 2);
          totalItem = form.txtitemTotal[i].value;          
        }
        total = parseFloat(total) + parseFloat(totalItem);
      }
    }    
    form.txtTotalVOSalesPrice.value = round_decimals(total,2);
  }
  
  function fxIsOrderVolumeChecked(){
  
    var form = parent.mainFrame.document.frmdatos;    
    var indice;
    var checkAplicarVO;
    
    if( (items_table.rows.length -1) == 1 ){    
      
      if(form.hdnIndice.value == undefined){
        indice = form.hdnIndice[0].value;
      }
      else{
        indice = form.hdnIndice.value;
      }      
      checkAplicarVO = document.getElementById('chkAplicarVO'+indice);
      
      if(checkAplicarVO != null && checkAplicarVO.checked){
        return true;
      }
    }else if((items_table.rows.length -1) > 1 ){
    
      for(i=0; i<items_table.rows.length -1; i++){
      
        indice = form.hdnIndice[i].value;        
        checkAplicarVO = document.getElementById('chkAplicarVO'+indice);
        
        if(checkAplicarVO != null && checkAplicarVO.checked){
          return true;
        }
      }
    }
    return false;
  }
  
  function fxApplyOrderVolume(){
  
    var form = parent.mainFrame.document.frmdatos;    
    var indice;
    var checkAplicarVO;
    var priceVO;
    var rentVO;
    var promoIdVO;
    
    if( (items_table.rows.length -1) == 1 ){    
      
      if(form.hdnIndice.value == undefined){
        indice = form.hdnIndice[0].value;
      }
      else{
        indice = form.hdnIndice.value;
      }      
      checkAplicarVO = document.getElementById('chkAplicarVO'+indice);
      
      if(checkAplicarVO != null && checkAplicarVO.checked){
        priceVO = document.getElementById('txtItemPriceCtaInscripVO'+indice).value;
        rentVO = document.getElementById('hdnRentVO'+indice).value;
        promoIdVO = document.getElementById('hdnPromoIdVO'+indice).value;
                
        if(form.txtItemPriceCtaInscrip.value == undefined){
          form.txtItemPriceCtaInscrip[0].value = priceVO;
        }
        else{
          form.txtItemPriceCtaInscrip.value = priceVO;
        }
        form.hdnItemValuetxtItemPriceCtaInscrip.value = priceVO;
        form.hdnItemValuetxtItemRentEquipment.value = rentVO;
        form.hdnItemValuetxtItemPromotioId.value = promoIdVO;
      }
    }else if((items_table.rows.length -1) > 1 ){
    
      for(i=0; i<items_table.rows.length -1; i++){
      
        indice = form.hdnIndice[i].value;        
        checkAplicarVO = document.getElementById('chkAplicarVO'+indice);
        
        if(checkAplicarVO != null && checkAplicarVO.checked){
          priceVO = document.getElementById('txtItemPriceCtaInscripVO'+indice).value;
          rentVO = document.getElementById('hdnRentVO'+indice).value;
          promoIdVO = document.getElementById('hdnPromoIdVO'+indice).value;
          
          form.txtItemPriceCtaInscrip[i].value = priceVO;
          form.hdnItemValuetxtItemPriceCtaInscrip[i].value = priceVO;
          form.hdnItemValuetxtItemRentEquipment[i].value = rentVO;
          form.hdnItemValuetxtItemPromotioId[i].value = promoIdVO;
        }
      }
    }

    fxCalculateTotal();
  }
  
  function fxCalculateTotalItems(){
    var precioCta = "0";
    var precioExc = "";
    var cantidad  = "1";
    
    if( (items_table.rows.length -1) == 1 ){    
      
      if(form.txtItemPriceCtaInscrip.value == undefined){
        precioCta = form.txtItemPriceCtaInscrip[0].value;
      }
      else{
        precioCta = form.txtItemPriceCtaInscrip.value;
      }
      precioExc = form.txtItemPriceException.value;
      cantidad = form.txtItemQuantity.value;
      
      if( precioExc != "" ){
        precioCta = precioExc;
      }        
        
      form.txtitemTotal.value = parseFloat(precioCta) * parseInt(cantidad);
      
    }else if((items_table.rows.length -1) > 1 ){
    
      for(i=0; i<items_table.rows.length -1; i++){
        precioCta = form.txtItemPriceCtaInscrip[i].value;
        precioExc = form.txtItemPriceException[i].value;
        cantidad = form.txtItemQuantity[i].value;    
        
        if( precioExc != "" ){
          precioCta = precioExc;
        }          
          
        form.txtitemTotal[i].value = parseFloat(precioCta) * parseInt(cantidad);
      }
    }    
  }

  //INICIO JBALCAZAR     
  function ShowCalculateApportionment(){
		
         if( items_table.rows.length < 2 ){
	     alert("Debe agregar al menos un Item para calcular Pago Anticipado");
	     return false;
	     }else{ 	    
			  		    	    
	    	    $("input[name='btnCalculoPagoAnticipado']").attr("disabled", true);
	    	 	var codigo_plan = [];
			    var pv_item_pk = [];
			    var pv_cantidad_item = [];			 
			    
			    //capturamos los planes
			    $("#items_table tr td div input[name='hdnItemValuetxtItemRatePlan']").each(function () {
			    	codigo_plan.push($(this).val());
			    });
			    //capturamos los id de los item
			    $("#items_table tr td div input[name='hdnIndice']").each(function () {
			    	pv_item_pk.push($(this).val());
			    });

			    //capturamos los id de los item
			    $("#items_table tr td div input[name='hdnItemValuetxtItemQuantity']").each(function () {
			    	pv_cantidad_item.push($(this).val());
			    });	    
			    
			    var prorrateo = {};
			    prorrateo.nroDocument= "<%=strDocument%>";
			    prorrateo.typeDocument= "<%=strTypeDocument%>";			    
			    prorrateo.user= "<%=sessionUser.getLogin()%>";
			    prorrateo.customerId= "<%=strCustomerId%>";	
			    prorrateo.siteId= "<%=strnpSite%>";			    
			    prorrateo.orderId = "<%=lOrderId%>";
			    prorrateo.accion ="EDITACTION";			    
			    prorrateo.items = [];
		
			    var miJson = '{';
			    miJson +='"nroDocument":';
			    miJson += '"'+ prorrateo.nroDocument+'",';
			    miJson +='"typeDocument":"'+ prorrateo.typeDocument+'",';	 ////JBALCAZAR PRY-1002			    
			    miJson +='"user":"'+ prorrateo.user+'",';
			    miJson +='"customerId":"'+ prorrateo.customerId+'",';
			    miJson +='"siteId":"'+ prorrateo.siteId+'",';	 ///JBALCAZAR PRY-1002			    
			    miJson +='"orderId":"'+ prorrateo.orderId+'",';			    
			    miJson +='"accion":"'+ prorrateo.accion+'",';			    
			          
			    miJson += '"items":[';
			    var obj; 
			    for(var i = 0; i < pv_item_pk.length; i++){
			    	obj = {};
				    obj.itemId = pv_item_pk[i];
			    	obj.planId = codigo_plan[i];
			 	    prorrateo.items[i] = obj; 
			 	    
			 	   miJson += '{';
			 	   miJson += '"itemId":';
			 	   miJson += '"'+pv_item_pk[i]+'",';
			 	   miJson += '"planId":';
			 	   miJson += '"'+codigo_plan[i]+'",';
			 	   miJson += '"quantity":';
			 	   miJson += '"'+pv_cantidad_item[i]+'"';	 	   
			 	   
			 	   miJson += '}';
			 	   
			 	   if(i+1 !=pv_item_pk.length){
			 		  miJson += ',';
			 	   }
			    }
			 
			    miJson += ']';
			    miJson += '}';
			    
			    document.frmdatos.hdnChangedOrder.value="S";
			    
			    $.ajax({
			        url:'<%=strURLApportionmentServlet%>',
			        type:"POST",
			        dataType:'json',
			        data: {
			        	"hdnMethod": "calculatePayment",
			        	"myDataP": miJson
			        	},        
			        success:function(data){

			        	//almacena los montos de prorrateo. 
			        	var montosprorrateo = [];
			        	$("#items_table tr td div input[name='txtMontoProrrateo']").each(function () {
			        		montosprorrateo.push($(this).val());
			            }); 
			        	var statuss= data.status;			        	
			        	if(statuss != 0)
			        	{		
			        			$("#v_savePagoAnticipado").val("0");			        		
			        			if(data.message == "cliente post-pago"){
			        			document.getElementById("spnMsjProrrateo").innerHTML ="NO APLICA PARA PAGO ANTICIPADO";
			             	    $("#textApportionmentS").attr("disabled", true);
			             	    $("#textApportionmentN").attr("disabled", true);
			             	    
						      	$("#items_table tr td div input[name='txtMontoProrrateo']").each(function () {
						      		$(this).val("");
						          });
						      	
						      	$("#items_table tr td div input[name='hdnItemValuetxtMontoProrrateo']").each(function () {
						    		$(this).val("");
						        })
						        
						        $("#txtTotalPriceApportionment").val("");			             	    
						      	$("#hdnIsClientPostPago").val("S");
						      	document.getElementById("textApportionmentN").checked = false;
						      	document.getElementById("textApportionmentS").checked = false;						      	
			        		}else{
				        		alert("Error al calcular el monto anticipado , seguir con la venta/portabilidad sin la creacion del pago anticipado");
						      	$("#items_table tr td div input[name='txtMontoProrrateo']").each(function () {
						      		$(this).val("0.00");
						          });
						      	
						      	$("#items_table tr td div input[name='hdnItemValuetxtMontoProrrateo']").each(function () {
						    		$(this).val("0.00");
						        })
						        
						        $("#txtTotalPriceApportionment").val('0.00'); 
								document.getElementById("textApportionmentN").checked = true;	 					      	
			        		}
			        		
							document.getElementById("hdnApportionment").value="N";
				    		document.getElementById("itemsProrrateo").innerHTML="";
				    		$("#txtTotalPrice").val(parseFloat($("#divLeyendaTotal tr td input[name='txtTotalSalesPrice']").val()).toFixed(2));
				    		
			        	}else
			        	{
			        		$("#v_savePagoAnticipado").val("1");
			        		//obtenemos los item y seteamos el monto anticipado				        	
				        	var listitem = data.data.items;
				        	var totalprorrateo = 0;
				        	var totalitems = 0;
				        	if(listitem.length>0){				        	
					        	for(var i=0;i<listitem.length;i++)
					        	{	        		
		        		   			$("#items_table tr td div input[name='txtMontoProrrateo']").eq(i).val(listitem[i].roundedPrice);
						   			$("#items_table tr td div input[name='hdnItemValuetxtMontoProrrateo']").eq(i).val(listitem[i].roundedPrice); 
					        	}
					        	$("#txtTotalPriceApportionment").val(data.data.mount);
					        	
	        					var temptotalprorrateo = $("#txtTotalPriceApportionment").val();
	        					if(temptotalprorrateo == 'undefined' || temptotalprorrateo == undefined || temptotalprorrateo == ''){
	        						totalprorrateo = 0;
	        					}else{	        						
	        						totalprorrateo = $("#txtTotalPriceApportionment").val();
	        					}
	        					
	        					var temptotalitems = $("#divLeyendaTotal tr td input[name='txtTotalSalesPrice']").val();

	        					if(temptotalitems == 'undefined' || temptotalitems == undefined || temptotalitems == ''){
	        						totalitems = 0;
	        					}else{
	        						totalitems = $("#divLeyendaTotal tr td input[name='txtTotalSalesPrice']").val();
	        					}
	        						        					
					        	$("#txtTotalPrice").val((parseFloat(totalprorrateo)+parseFloat(totalitems)).toFixed(2));					        						     
					     
								///// ARMAMOS SECCION CON LOS VALORES GENERADOS
					    		document.getElementById("itemsProrrateo").innerHTML="";
					    		var tableProrrateo = '';	
					    		tableProrrateo +='<table cellpadding="0" cellspacing="0" border="1" class="display" name="tableItemProrrateo" id="tableItemProrrateo">';
					    		tableProrrateo +=     '<tr>'+
					    								 '<td>TRX</td>'+
					    					             '<td>ID ITEM</td>'+
					    					             '<td>CANTIDAD</td>'+					    					             
					    					             '<td>PRICE</td>'+					    					             
					    					             '<td>PRICE IGV </td>'+
					    					             '<td>Price(R)</td>'+
					    					             '<td>TEMP ID</td>'+
					    					             '<td>CICL ORGN</td>'+
					    					             '<td>CICL DEST</td>'+
					    					             '<td>IGV</td>'+
					    					            '</tr>' ;
					        
					    		for(var i in listitem){		
					    			 tableProrrateo += '<tr>'+
					    			 					   '<td><INPUT style="TEXT-ALIGN: right" readOnly size=6 id="itemProrrateoTrxId"  name="itemProrrateoTrxId" value ='+listitem[i].trxId+'></td>'+
					    						           '<td><INPUT style="TEXT-ALIGN: right" readOnly size=6 id="itemProrrateoIndice"  name="itemProrrateoIndice" value ='+listitem[i].itemId+'></td>'+
					    						           '<td><INPUT style="TEXT-ALIGN: right" readOnly size=6 id="itemProrrateoQuantity"  name="itemProrrateoQuantity" value ='+listitem[i].quantity+'></td>'+					    						           
					    						           '<td><INPUT style="TEXT-ALIGN: right" readOnly size=6 id="itemProrrateoPrice"  name="itemProrrateoPrice" value ='+listitem[i].price+'></td>'+					    						       
					    						           '<td><INPUT style="TEXT-ALIGN: right" readOnly size=6 id="itemProrrateoPriceIgv"  name="itemProrrateoPriceIgv" value ='+listitem[i].priceIgv+'></td>'+
					    						           '<td><INPUT style="TEXT-ALIGN: right" readOnly size=6 id="itemProrrateoRoundedPrice"  name="itemProrrateoRoundedPrice" value ='+listitem[i].roundedPrice+'></td>'+					    						           
					    						           '<td><INPUT style="TEXT-ALIGN: right" readOnly size=6 id="itemProrrateoTemplateid"  name="itemProrrateoTemplateid" value ='+listitem[i].templateId+'></td>'+
					    						           '<td><INPUT style="TEXT-ALIGN: right" readOnly size=6 id="itemProrrateoCicloO"  name="itemProrrateoCicloO" value ='+listitem[i].cicloOrigen+'></td>'+
					    						           '<td><INPUT style="TEXT-ALIGN: right" readOnly size=6 id="itemProrrateoCicloD"  name="itemProrrateoCicloD" value ='+listitem[i].cicloDestino+'></td>'+
					    						           '<td><INPUT style="TEXT-ALIGN: right" readOnly size=6 id="itemProrrateoIgv"  name="itemProrrateoIgv" value ='+listitem[i].igv+'></td>'+
					    						           '</tr>';	        		
	        					}
	        	
					    		tableProrrateo +='</table>';
								document.getElementById("itemsProrrateo").innerHTML=tableProrrateo;
				        	}  
					        $("input[name='btnCalculoPagoAnticipado']").removeAttr("disabled");				        	
			        	}			        			
			        },
			        error:function(xhr, ajaxOptions, thrownError){
			            if (xhr.status === 0) {
			                alert('Not connect: Verify Network.');
			            } else if (xhr.status == 404) {
			                alert('Requested page not found [404]');
			            } else if (xhr.status == 500) {
			                alert('Internal Server Error [500].');
			            } else if (xhr.status === 'parsererror') {
			                alert('Requested JSON parse failed.');
			            } else if (xhr.status === 'timeout') {
			                alert('Time out error.');
			            } else if (xhr.status === 'abort') {
			                alert('Ajax request aborted.');
			            } else {
			                alert('Uncaught Error: ' + xhr.responseText);
			            }
				        $("input[name='btnCalculoPagoAnticipado']").removeAttr("disabled");			            
			        }
			    });			    
	 	}	
  }  

  function comprobarProrrateo(obj){	

  	if(obj=='N'){
        document.getElementById('btnCalculoPagoAnticipado').disabled = true;
        document.getElementById("hdnApportionment").value="N";
      	$("#items_table tr td div input[name='txtMontoProrrateo']").each(function () {
      		$(this).val("0.00");
          });
      	
      	$("#items_table tr td div input[name='hdnItemValuetxtMontoProrrateo']").each(function () {
    		$(this).val("0.00");
        });
      	
        $("#txtTotalPriceApportionment").val('0.00');
        $("#txtTotalPrice").val(parseFloat($("#divLeyendaTotal tr td input[name='txtTotalSalesPrice']").val()).toFixed(2));
        $("#v_savePagoAnticipado").val("0");
        alert('No aplica pago anticipado, el monto de prorrateo sera cargado en el primer recibo.');
		document.getElementById("itemsProrrateo").innerHTML="";        
  	}
  	else if(obj=='S'){
          document.getElementById('btnCalculoPagoAnticipado').disabled = false;
        document.getElementById("hdnApportionment").value="S";
  	}
  	
  }
  
//Fin JB

  function  editOrderBiometric(resultado){
      if(resultado=="ok"){
          fxDoSubmit("updateOrden");
      }else
      if(resultado=='ANULAR'){
          var vform=document.frmdatos;
          vform.hdnBiometricAnular.value='<%=constante.ACTION_INBOX_ANULAR %>';
          vform.cmbAction.value='<%=constante.ACTION_INBOX_ANULAR %>';
          fxDoSubmit("updateOrden");
      }
  }

   
  function redirectOrder(idorder){
      parent.bottomFrame.location.replace('/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid='+idorder+'&av_execframe=BOTTOMFRAME');
  }
   
  </SCRIPT>

<%      
}catch(Exception  ex){                
   System.out.println("Error try catch-->"+MiUtil.getMessageClean(ex.getMessage())); 
%>
<script>
   alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}
%>  