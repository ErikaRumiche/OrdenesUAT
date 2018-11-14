<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.bean.*" %>
<%@ page import="oracle.portal.provider.v2.ProviderSession" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.service.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.lang.Double" %>
<%@ page import="java.util.*"%>

<%
   try{
      String   strCustomerId= "",
               strnpSite ="",               
               strOppId = "",               
               strCategory = "",
               strOrderId  = "",               
               strPerformFlagPeriod = "",
               strOppType           = "",
               strFrom              ="",
               strAction            ="",
               strAppId             ="",
               //strSolutionId        ="",
               strBeginPeriod       ="",
               strEndPeriod         ="",
               strSpecId            ="",
               strSessionId         ="",
					strDivisionId			="";
                    
      strCustomerId    = (String)request.getParameter("strCustomerId"); //"152894";       
      if (strCustomerId.compareTo("")== 0){
        throw new Exception("No se envió el parámetro strCustomerId");
      }
      strnpSite        = (String)request.getParameter("strnpSite");
      strOrderId       = (String)request.getParameter("strOrderId");   //"2001412";
      
      if ( (strFrom.compareTo("INC") != 0) && (strOrderId.compareTo("")== 0) ){
        throw new Exception("No se envió el parámetro strOrderId");
      }
      
      strCategory      = (String)request.getParameter("strCategory");      
      strOppId         = (String)request.getParameter("strOppId");
      strOppType       = (String)request.getParameter("strOppType"); 
      strPerformFlagPeriod = (String)request.getParameter("strPerformFlagPeriod");
      strFrom          = (String)request.getParameter("strFrom"); 
      strAction        = (String)request.getParameter("strAction"); //VIEW, NEW, EDIT
      strSessionId     = (String)request.getParameter("strSessionId");
      strAppId         = (String)request.getParameter("strAppId");
      //strSolutionId    = (String)request.getParameter("strSolutionId");
		strDivisionId    = (String)request.getParameter("strDivisionId");
      strSpecId        = (String)request.getParameter("strSpecId");
      //Periodos de las excepciones (para edición)
      strBeginPeriod    = (String)request.getParameter("strBeginPeriod"); //"01/11/2007|01/12/2007";  // //Periodos de inicio (separados por |)
      strEndPeriod      = (String)request.getParameter("strEndPeriod"); //"30/11/2007|31/12/2007"; //   //Periodos de fin (separados por |)
      
      int iAppId = 0;
      if (strAppId == null) strAppId = "0";
      if ( (strAppId.compareTo("0")== 0)){
        if (strSessionId.compareTo("")== 0){
          throw new Exception("No se envió el parámetro strSessionId");
        }
        //Código de la aplicación    
        System.out.println("[PopUpException]Sesión a consultar : " + strSessionId);
        PortalSessionBean objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
        System.out.println("[PopUpException]Sesión a consultar : " + objPortalSesBean);
        if( objPortalSesBean == null ){
          throw new Exception("No se encontró la sesión del usuario");
        }
        System.out.println("");
        iAppId = objPortalSesBean.getAppId();
      }else{
        iAppId = MiUtil.parseInt(strAppId);
      }
                  
      String objectType  = request.getParameter("objectType")==null?"":(String)request.getParameter("objectType");
           
      //Itemes
      String[] pstrItemId             =  request.getParameterValues("pstrItemId"); //{"780"};
      String[] pstrItemDesc           =  request.getParameterValues("pstrItemDesc");//{"i710"}; //
      String[] pstrItemQty            =  request.getParameterValues("pstrItemQty");
      String[] pstrItemPlanId         =  request.getParameterValues("pstrItemPlanId");   //
      String[] pstrItemPlanDesc       =  request.getParameterValues("pstrItemPlanDesc"); //{"plan 1", "plan 2"}; //
      String[] pstrItemSaleModality   = request.getParameterValues("pstrItemSaleModality");
      String[] pstrItemCategory       = request.getParameterValues("pstrItemCategory"); //{"ADICION"}; //            
      String[] pstrItemServIds        = request.getParameterValues("pstrItemServIds"); //{"110|N|S|120|N|S"};       //  //Ids de los servicios, separados por un #
      
      //Excepciones de ítem (para edición)
      String[] pstrItemRevenueExc     = request.getParameterValues("pstrItemRevenueExc"); //{"10"};   // Renta básica
      String[] pstrItemRentExc        = request.getParameterValues("pstrItemRentExc"); //{"5"};       // Alquiler
      String[] pstrItemMinAdiCD       = request.getParameterValues("pstrItemMinAdiCD"); //{"20"};     // Minutos adicionales Conexión directa
      String[] pstrItemMinAdiIT       = request.getParameterValues("pstrItemMinAdiIT"); //{"20"};    // Minutos adicionales Telefonía
      
      // Servicios del ítem ya marcados como gratuitos (para edición) 
      //Ejm: {"110|120", "120"}: El ítem 1 tiene los serv 110 y 120 y el ítem 2 el serv 120
      String[] pstrItemExcServiceId       = request.getParameterValues("pstrItemExcServiceId");  //{"110"}; //request.getParameterValues("pstrItemExcServiceId"); 
      String[] pstrItemExcServiceDiscnt   = request.getParameterValues("pstrItemExcServiceDiscnt");  //{"10"};//request.getParameterValues("pstrItemExcServiceDiscnt");                           
      String strBillCycle     = "";
      String strBillCycleDsc  = "";
      String strMessage       = null;
      GeneralService gService = new GeneralService();
      EditOrderService objEditOrderService = new EditOrderService();
      HashMap hshData         = new HashMap();
      HashMap hshService      = new HashMap();
      HashMap hshServiceCost  = new HashMap();
      HashMap hshAccessFee    = new HashMap();      
      HashMap hshRentFee      = new HashMap();
      HashMap hshOrder        = new HashMap();
      
      boolean bOrdenFromSalesRep = false;            
      boolean bIsInternet        = false;
      boolean bIsRent            = false; //bAlquilerShow
      boolean bShowProduct       = false;
      int nAlquilerShow          = 0;
      ArrayList aServices        = new ArrayList();
      float[] afAccessFee        = null;
      float[] afRentFee          = null;
      float fServiceCost         = 0;
      int iExceptionApprove      = 0;
      OrderBean objOrderBean     =null;
      
      if (pstrItemPlanId == null){
        throw new Exception("La orden no tiene planes. Revise");
      }
      if ( (strFrom.compareTo("INC") != 0) && (strAction.compareTo("EDIT")==0) ){
        hshOrder = objEditOrderService.getOrder(MiUtil.parseLong(strOrderId));
        strMessage=(String)hshOrder.get("strMessage");   
        if (strMessage!=null)
          throw new Exception(strMessage);
        objOrderBean=(OrderBean)hshOrder.get("objResume"); 
        iExceptionApprove = objOrderBean.getNpExceptionApprove();
        if ( (iExceptionApprove == 2) || ((iExceptionApprove == 3) ))
          strAction = "VIEW";  
      }
                 
      if (strAction == null)
        strAction = "VIEW";
                           
      if ( pstrItemDesc == null)
        bShowProduct = false;
      else
        for (int i = 0 ; i < pstrItemDesc.length; i ++){
          if (pstrItemDesc[i].compareTo("") != 0)
            bShowProduct = true;
            break;
        }
      
      /*Usar constante*/
		/*Inicio - Cambio de la solución por división*/
      /*if (strSolutionId == null) strSolutionId = "0";
      if ( strSolutionId.compareTo("4")==0)
        bIsInternet = true;*/
      if (strDivisionId == null) strDivisionId = "0";
		if (strDivisionId.compareTo(Constante.KN_BANDA_ANCHA+"")==0) //Ahora se valida por la division      
        bIsInternet = true;		  
     /*Fin - Cambio de la solución por división*/
	  
      /*if ( pstrItemDesc == null)
        bShowProduct = false;
      else if ( (pstrItemDesc.length == 1) && (pstrItemDesc[0].compareTo("")==0)) {
        bShowProduct = false;
      }else if ( pstrItemDesc.length == 0)
        bShowProduct = false;                     
      else
        bShowProduct = true;*/
        
      //Revisar que strOppType no debe ser null (en caso no tenga oportunidad dejar en 0)    
      if (strOppType == null)
         strOppType = "0";  
                              
      if (strFrom.compareTo("INC") == 0)
         strFrom = "Incidente";
      else
         strFrom = "Orden";

      if (bIsInternet){
         bIsRent = false;  //Si la orden es de Internet, no se muestran los campos de alquiler
         nAlquilerShow = 0;
      }else{
         for(int i=0; i < pstrItemSaleModality.length; i++){
            //Si alguno de los ítemes es de alquiler, se muestran los campos de alquiler                     
            if (pstrItemSaleModality[i].compareTo("Alquiler")==0){               
               bIsRent = true;  
               nAlquilerShow = 1;
               break;
            }   
         }
      }      
      if ( (strOppId != null) && (strOppId.compareTo("")!=0) ){
         //Orden generada por un consultor
         bOrdenFromSalesRep = true;
      }
      //Se obtienen los servicios      
      hshService = gService.getServiceList(pstrItemServIds, bOrdenFromSalesRep);
      if (hshService!=null && hshService.size() > 0){
         aServices         = (ArrayList)hshService.get("objArrayList");         
         strMessage        =  (String)hshService.get("strMessage");         
         if (strMessage!= null){         
            //System.out.println("strMessage luego de llamar a getServiceList ==="+strMessage);
            throw new Exception(strMessage);
        } 
      }
                  
      /*hshData = gService.getCustomerBillCycle(Integer.parseInt(strCustomerId), Integer.parseInt(strOrderId));      
      if (hshData!=null && hshData.size() > 0 ) {         
         strBillCycle      =  MiUtil.getString((String)hshData.get("wv_billcycle"));
         strBillCycleDsc   =  MiUtil.getString((String)hshData.get("wv_billcycledsc"));       
         strMessage        =  (String)hshData.get("wv_message");          
         if (strMessage!= null){         
            throw new Exception(strMessage);
         } 
      } */     
      //Renta de planes}      
      hshAccessFee = gService.getAccessFee(iAppId, pstrItemPlanId, pstrItemServIds, bOrdenFromSalesRep );
      if (hshAccessFee!=null && hshAccessFee.size() > 0 ) {                                  
         afAccessFee        = (float[])hshAccessFee.get("afAccessFee");
         strMessage        =  (String)hshAccessFee.get("strMessage");
         if (strMessage!= null){         
            throw new Exception(strMessage);
         }  
      }
      
      if (bIsRent){
         //Precio de alquiler
         hshRentFee = gService.getRentFee(pstrItemId, MiUtil.parseInt(strSpecId));         
         if (hshRentFee!=null && hshRentFee.size() > 0 ) {                        
            strMessage        =  (String)hshRentFee.get("strMessage");            
            if (strMessage!= null){
               throw new Exception(strMessage);
            }  
            afRentFee        = (float[])hshRentFee.get("afRentFee");                              
         }
      }                  
    
%>
   <html>
      <head>         
         <title>Excepciones</title>
         <link type="text/css" rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"/>
         <style type="CDATA">
            .show   { display:inline}
            .hidden { display:none }
         </style>
       
         <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
         <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
         <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsItemsIXEdit.js"></script>
         <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXNew.js"></script>
         <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsItemsException.js"></script>
         <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/DateTimeBasicOperations.js"></script>
         <script language="JavaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></script>         
         <script>
            window.focus();            
            var strChangePeriod = "<%=strPerformFlagPeriod%>";
            var validatePeriod = true;
            var wn_opportunity_type = <%=strOppType%>;
            
            function fxClearAll(){               
               var form = document.frmdatos;
               var itemQty = form.hdnItemQty.value;
               var cols = form.hdnServiceColsQty.value;
               var i = 0;
               var j = 0;
               if (itemQty == 1){
                  try{
                     form.txtBasicRentException.value = "";
                     calcDiscountBasicRent(i+1);
                     form.txtRentException.value = "";
                     calcDiscountRent(i+1);
                  }catch(e){}
                  for (j=1; j<=cols; j++){
                     eval("form.hdnServiceDiscount"+j+".value=\"\"");
                     eval("form.hdnServicioChecked"+j+".value=\"\"");
                     eval("form.chkServicio"+j+".checked=false");
                  }
                  form.hdnMinAddConexDirecChecked.value = "";
                  form.chkMinAddConexDirec.checked=false;
                  form.hdnMinAddInterConexChecked.value = "";
                  form.chkMinAddInterConex.checked=false;
                  calcBasicRent(form);
                  <%
                  if (bIsRent){
                     %>calcRent(form);<%
                  }%>   
                                      
                  calcServiceDiscount();
                  calcGeneralDiscount();
                  
               }else{
                  for(i=0;i< itemQty ;i++){
                     try{
                        form.txtBasicRentException[i].value = "";
                        calcDiscountBasicRent(i+1);
                        form.txtRentException[i].value = "";
                        calcDiscountRent(i+1);
                     }catch(e){}
                     for (j=1; j<=cols; j++){
                        eval("form.hdnServiceDiscount"+j+"[i].value=\"\"");
                        eval("form.hdnServicioChecked"+j+"[i].value=\"\"");
                        eval("form.chkServicio"+j+"[i].checked=false");
                     }
                     form.hdnMinAddConexDirecChecked[i].value = "";
                     form.chkMinAddConexDirec[i].checked=false;
                     form.hdnMinAddInterConexChecked[i].value = "";
                     form.chkMinAddInterConex[i].checked=false;
                  }
                  calcBasicRent(form);
                  <%
                  if (bIsRent){
                     %>calcRent(form);<%
                  }%>   
                  calcServiceDiscount();
                  calcGeneralDiscount();
            }
            
            var table = document.all ? document.all["tblPeriodo"]:document.getElementById("tblPeriodo");
            for(i=table.rows.length;i>1 ;i--){
               table.deleteRow(i-1);
            }
            form.hdnPeriodoQty.value = 0;
            
            strChangePeriod = "s";
            validatePeriod = false;
         }                          
            
         </script>
      </head>  
      <body>
         <form method="post" name="frmdatos">
           <table border="0" cellspacing="0" cellpadding="0" width="100%">
               <tr class="PortletHeaderColor">
                  <td class="LeftCurve" valign="top" align=LEFT width="10" height="10">&nbsp;&nbsp;</td>
                  <td class="PortletHeaderColor" align=LEFT valign="top" width="100%"><font class="PortletHeaderText"><%=strFrom%> > Excepción</font></td>
                  <td align="right" class="RightCurve" width="10">&nbsp;&nbsp;</td>
               </tr>
            </table>
            <input type="hidden" name="hdnArrendEnabled" value="<%=nAlquilerShow%>">
            <input type="hidden" name="hdnDataChanged" value="0">
            <input type="hidden" name="hdnItemQty" value="<%=pstrItemId.length%>">
            <input type="hidden" name="hdnServiceColsQty" value="<%=aServices.size()%>">
            <table name="table_items_exception_rb" id="table_items_exception_rb" border="0" cellPadding="1" cellSpacing="1" width="100%" class="RegionBorder">       
               <tr>
                  <td align="center" class="CellLabel" rowspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                  <%
                  if (!bIsInternet){%>
                     <td align="center" class="CellLabel" rowspan="2">CATEGORÍA</td>
                  <%}
                  else{%>
                     <td align="center" class="CellLabel" rowspan="2">SOLUCIÓN</td>
                  <%}%>
                  <%if (bShowProduct){%>
                    <td align="center" class="CellLabel" rowspan="2">PRODUCTO</td>
                  <%}%>
                  <td align="center" class="CellLabel" rowspan="2">CANT.</td>
                  <td align="center" class="CellLabel" rowspan="2">PLAN</td>
                  <td align="center" class="CellLabel" colspan="3">RENTA BÁSICA UNIT.</td>
                  <%
                  if (bIsRent)
                     %><td align="center" class="CellLabel" colspan="3">ALQUILER MENSUAL UNIT.</td>
                  <%                                       
                  if ( aServices.size()>0 ){
                  %>
                     <td align="center" class="CellLabel" colspan="<%=aServices.size()%>">SERVICIOS ADICIONALES</td>
                  <%
                  }
                  if (!bIsInternet){%>                                    
                     <td align="center" class="CellLabel" colspan="2">MIN. ADIC. TOTALES</td>
                  <%}%>   
               </tr>
               <tr>
                  <td align="center" class="CellLabel">&nbsp;Precio&nbsp;</td>
                  <td align="center" class="CellLabel">&nbsp;Dscto.&nbsp;</td>
                  <td align="center" class="CellLabel">&nbsp;Final&nbsp;</td>
                  <%
                  if (bIsRent){%>
                     <td align="center" class="CellLabel">&nbsp;Precio&nbsp;</td>
                     <td align="center" class="CellLabel">&nbsp;Dscto.&nbsp;</td>
                     <td align="center" class="CellLabel">&nbsp;Final&nbsp;</td>
                  <%
                  }                                    
                  //bucle para mostrar los nombres de servicios
                  
                  for (int i=0; i < aServices.size(); i++){
                     ServiciosBean objServicio  = (ServiciosBean)aServices.get(i);                     
                     //Si es banda ancha se debe revisar qué servicios mostrar (depende si es alquiler)
                  %><td align="center" class="CellLabel" ><%=objServicio.getNpnomcorserv()%></td><%
                  }
                  
                  if (!bIsInternet){%> 
                  <!-- para móviles -->
                     <td align="center" class="CellLabel">ConexDirect</td>
                     <td align="center" class="CellLabel">InterConex</td>
                  <%}%>
               </tr>
               <!-- Itemes -->
               <%
               String strItemShow = "";
               int    iNotPrepaidItems = 0;
               for (int i=0; i< pstrItemId.length; i++){
                  //Si la categoría es prepago:
                     //strItemShow = "hidden";
                     //<input type="hidden" name="hdnEnabled" value="0">
                  //Si no es prepago:
                     strItemShow = "CellContent";
                     iNotPrepaidItems++;
                     %><input type="hidden" name="hdnEnabled" value="1"><%
                     //Obtener el precio de renta
                     
                  %>
                  <tr class="<%=strItemShow%>">
                     <td align="center" class="CellContent"><%=iNotPrepaidItems%></td>
                     <td><%=strCategory%><input type="hidden" name="hdnCategory" value="<%=strCategory%>"></td>                     
                     <%if (bShowProduct){%>
                        <td><%=pstrItemDesc[i]%><input type="hidden" name="hdnProducto" value="<%=pstrItemDesc[i]%>"></td>
                     <%}else{%>
                        <input type="hidden" name="hdnProducto" value="<%=pstrItemDesc[i]%>">
                     <%}%>
                     <td align="center"><%=pstrItemQty[i]%><input type="hidden" name="hdnQty" value="<%=pstrItemQty[i]%>"></td>
                     <td><%=pstrItemPlanDesc[i]%><input type="hidden" name="hdnPlanId" value="<%=pstrItemPlanDesc[i]%>"</td>
                     <%
                     DecimalFormat decimalFormat = new DecimalFormat("###.##");                                           
                     %>
                     <td align="right"><%=decimalFormat.format(afAccessFee[i])%><input type="hidden" name="hdnBasicRentPrice" value="<%=decimalFormat.format(afAccessFee[i])%>"></td>
                     <td align="right"><div id="divBasicRentDesc"></div><input type="hidden" name="hdnBasicRentDesc"></td>                    
                     <td align="right"><input type="text" name="txtBasicRentException"  size="4" onchange="return calcDiscountBasicRent('<%=i+1%>')" class="right"></td>
                     <%if (strAction.compareTo("VIEW")==0){%>
                      <script>
                        <%if (i>0){ %>
                          document.frmdatos.txtBasicRentException[<%=i%>].disabled = "true";
                       <%}else{%>
                          document.frmdatos.txtBasicRentException.disabled = "true";
                       <%}%>
                        
                      </script>
                     <%}%>
                     
                     <%                                
                     if (bIsRent){          
                        %>
                        <td align="right"><%=afRentFee[i]%><input type="hidden" name="hdnRentPrice" value="<%=afRentFee[i]%>"></td>
                        <td align="right"><div id="divRentDesc"></div><input type="hidden" name="hdnRentDesc"></td>
                        <td align="right">
                        <input type="text" name="txtRentException" size="4" onchange="return calcDiscountRent(<%=i+1%>)" class="right">
                        <%if (strAction.compareTo("VIEW")==0){%>
                          <script>
                             <%if (i>0){ %>
                                document.frmdatos.txtRentException[<%=i%>].disabled = "true";
                             <%}else{%>
                                document.frmdatos.txtRentException.disabled = "true";
                             <%}%>
                            
                          </script>
                        <%}%>
                        <%                                                
                     }else{%>                                                
                        <div class="ocultar" id="divRentDesc"></div>
                        <input type="hidden" name="txtRentException" size="4" >   
                        <input type="hidden" name="hdnRentPrice" value="">
                        <input type="hidden" name="hdnRentDesc">                        
                     <%}                                                                                             
                                                                                                                                             
                     //Servicios                                          
                     for (int j=0; j < aServices.size(); j++){
                        %><td align="center"><%
                        //Si es de Banda Ancha revisar qué servicios deben mostrarse (caso de alquiler)
                        ServiciosBean objService = (ServiciosBean)aServices.get(j);
                        
                        Double dServiceId = new Double(objService.getNpservicioid());                        
                        //Sólo se muestra una casilla si el ítem cuenta con dicho servicio
                        //aServices tiene todos los servicios de la orden, 
                        //pero puede que el ítem 1 no tenga el servicio A y si lo tenga el ítem 2
                        
                        String strTemp ="";
                        strTemp = String.valueOf(dServiceId.intValue()) + "|";
                                                
                        /*
                        //El id de los servicios está llegando con punto decimal!  Ejem: 140.0|
                        if (strAction.compareTo("NEW")==0)
                           strTemp = String.valueOf(dServiceId.intValue()) + ".0|";
                        else
                           strTemp = String.valueOf(dServiceId.intValue()) + "|";
                        */
                        //System.out.println( "getNpexcludingind()==="+objService.getNpexcludingind() );
                        String chkServicio = "chkServicio"+(j+1);                        
                        if (  ( pstrItemServIds[i].indexOf(strTemp) == -1) 
                            || (objService.getNpexcludingind().compareToIgnoreCase("ALQ")==0)
                            || (objService.getNpexcludingind().compareToIgnoreCase("INT")==0)){%>
                           <input type="checkbox" name="<%=chkServicio%>" class="ocultar">
                        <%
                        }else{%>
                           <input type="checkbox" name="<%=chkServicio%>" onclick="markService(<%=j+1%>,<%=i%>)">                           
                        <%
                        }
                        if (strAction.compareTo("VIEW")==0){%>
                          <script>
                            <%if (i>0){ %>
                              document.frmdatos.<%=chkServicio%>[<%=i%>].disabled = "true";
                            <%}else{%>
                              document.frmdatos.<%=chkServicio%>.disabled = "true";
                            <%}%>
                            
                          </script>
                        <%}
                                                
                        DecimalFormat decFormatServId = new DecimalFormat("###");  
                        String  strServiceId  = decFormatServId.format(objService.getNpservicioid());
                        hshServiceCost = gService.getServiceCost(Integer.parseInt(pstrItemPlanId[i]), Integer.parseInt(strServiceId));
                        
                        if ( (hshServiceCost != null) && (hshServiceCost.size() > 0 ) ){
                           fServiceCost        = ((Float)hshServiceCost.get("fServiceCost")).floatValue();
                           strMessage        =  (String)hshServiceCost.get("strMessage");
                           if (strMessage!= null){         
                              System.out.println("strMessage==="+strMessage);
                              throw new Exception(strMessage);
                           } 
                        }
                        %>
                        <input type="hidden" name="hdnServiceId<%=j+1%>" value="<%=strServiceId%>">
                        <input type="hidden" name="hdnServicioChecked<%=j+1%>">
                        <input type="hidden" name="hdnServiceDiscount<%=j+1%>" value="<%=fServiceCost%>">
                     </td>
                     <%   
                     }
                     //Si es de móviles:
                     if (!bIsInternet){%>
                        <td align="center"><input type="checkbox" name="chkMinAddConexDirec"  value="Y" onclick="markMinAdd(this,<%=i%>)"><input type="hidden" name="hdnMinAddConexDirecChecked"></td>
                        <td align="center"><input type="checkbox" name="chkMinAddInterConex"  value="Y" onclick="markMinAdd(this,<%=i%>)"><input type="hidden" name="hdnMinAddInterConexChecked"></td>
                     <%
                      if (strAction.compareTo("VIEW")==0){%>
                        <script>
                          <%if (i>0){ %>
                             document.frmdatos.chkMinAddConexDirec[<%=i%>].disabled = "true";
                             document.frmdatos.chkMinAddInterConex[<%=i%>].disabled = "true";
                          <%}else{%>
                            document.frmdatos.chkMinAddConexDirec.disabled = "true";
                            document.frmdatos.chkMinAddInterConex.disabled = "true";
                          <%}%>
                          
                        </script>
                      <%}
                     
                     }
                     %>
                  </tr>   
                  <%      
               }
               %>
               <tr>
                  <td align="right" class="CellLabel" colspan="5">Proyección Descuento respecto a un Ciclo Fact.&nbsp;&nbsp;</td>
                  <td align="right" class="CellLabel"><div id="divTotalBasicRent"></div></td>
                  <td align="right" class="CellLabel"><div id="divTotalBasicRentDesc"></div></td>
                  <td align="right" class="CellLabel"><div id="divTotalBasicRentException"></div></td>
                  <%
                  if (bIsRent){%>                     
                     <td align="right" class="CellLabel"><div id="divTotalRent"></div></td>
                     <td align="right" class="CellLabel"><div id="divTotalRentDesc"></div></td>
                     <td align="right" class="CellLabel"><div id="divTotalRentException"></div></td>                  
                  <%
                  }                  
                  
                  //servicios adicionales                  
                  for (int i=0; i<aServices.size(); i++){
                     String name = "divTotalServiceDiscount"+(i+1);
                     %><td align="right" class="CellLabel"><div id="<%=name%>"></div></td>
                  <%   
                  }
                  if (!bIsInternet){%>                  
                     <!-- Si es de móviles -->
                     <td align="right" class="CellLabel">&nbsp;&nbsp;&nbsp;</td>
                     <td align="right" class="CellLabel">&nbsp;&nbsp;&nbsp;</td>
                 <%}%> 
               </tr>
               <tr>
                  <td align="right" class="CellLabel" colspan="5">Total de Descuentos&nbsp;&nbsp;</td>
                  <td align="right" class="CellLabel"><div id="divTotalDiscount"></div></td>
               </tr>
            </table>
            
            <!--Linea de separación -->
            <table width="100%" align="left">
            <tr>
                <td width="100%" class="CellContent" align="left"><font color="#FFOOOO"><B><div>La renta de los planes tarifarios incluyen IGV y son referenciales</div></B></font></td>
            </tr>
            </td>
            
            <table><tr align="center"><td>&nbsp;</td></tr></table>
            <table border="0" cellspacing="0" cellpadding="0">       
               <tr>
                  <td width="300">
                     <input type="hidden" name="hdnPeriodoQty" value="0">
                     <%if (strAction.compareTo("VIEW")!=0){%>
                        <a href="javascript:AddItemPeriodo('tblPeriodo');">
                          <img name="item_img_crear" alt="Agregar Periodo" src="<%=Constante.PATH_APPORDER_SERVER%>/images/crear.gif" border="no" onclick="strChangePeriod='s';">
                        </a>
                     <%}%> 
                     <table id="tblPeriodo" border="0" cellPadding="1" cellSpacing="1" class="RegionBorder">
                        <tr>
                           <td width="50" align="center" class="CellLabel">Periodo</td> 
                           <td width="100" align="center" class="CellLabel">Desde<BR><small>DD/MM/YYYYY</small></td>
                           <td width="100" align="center" class="CellLabel">Hasta<BR><small>DD/MM/YYYYY</small></td>
                           <td width="40" align="center" class="CellLabel">Días</td>
                           <td width="10" align="center" class="CellLabel">&nbsp;</td>
                        </tr>
                     </table>
                  </td>
                  <td align="center"></td>
               </tr>
            </table>
            <!--Linea de separación -->
            <table><tr align="center"><td></td></tr></table>
          
            <table border=0 cellPadding="0" cellSpacing="0" width="100%">
               <tr align="center">
                  <td width="10%">
                     <!--CUADRO RESULMEN -->
                     <table width="350" border=0 cellPadding="0" cellSpacing="0">
                        <tr>
                           <td width="200">
                              <table width="200" height="100%" class="RegionBorder" border="0" cellPadding="1" cellSpacing="1">
                                 <tr>
                                    <td width="140" class="CellLabel">Descuento Real</td>
                                    <td width="60" class="CellContent" align="center"><font color="#FFOOOO"><B><div id="divTotalGeneralDiscount">0.00</div></B></font></td>
                                 </tr>
                                 <!--<tr>
                                    <td class="CellLabel">Ciclo de Facturación</td>
                                    <td class="CellContent" align="center"><%=strBillCycle%></td>
                                    
                                 </tr>
                                 -->
                                 <input type="hidden" name="hdnExceptionBillCycle" value"0">
                              </table>
                           </td>
                           <td width="150">
                              <table width="150">
                                 <tr><td class="BannerSecondaryText">&nbsp;</td></tr>
                                 <tr><td class="BannerSecondaryText"><%=strBillCycleDsc%></td></tr>
                              </table>
                           </td>
                        </tr>
                     </table>
                  </td>   
                  <!--CUADRO RESULMEN -->
                  <%if (strAction.compareTo("VIEW")!=0){%>
                    <td align="center">&nbsp;                             
                      <input type="button" name="btnCalcular" value=" Calcular Descuento " onclick="javascript:calcDiscount();">                             
                    </td>
                  <%}%>
               </tr>
            </table>
            <table border="0" cellspacing="0" cellpadding="0" align="center">
               <tr>
                  <%if (strAction.compareTo("VIEW")!=0){%>
                    <td align="center"><input type="button" name="btnAceptar" value=" Aceptar " onclick="javascript:fxSendItemExceptiontoOrder();" style="display:none"></td>
                    <td align="center"><input type="button" name="btnLimpiar" value=" Limpiar " onclick="javascript:fxClearAll();" style="display:none"></td>
                  <%}%>
                  <td align="center"><input type="button" name="btnCerrar"  value=" Cerrar  " onclick="javascript:top.close();"></td>
               </tr>         
            </table>
         </form>                  
      </body>
      <script>
            
            /* Permite cargar los campos de la orden. */ 
            function fxLoadField(){
               var form = document.frmdatos;
               <%
               for (int i=0; i < pstrItemId.length; i++){%>
                  /* Coloca la Renta Basica */                   
                  loadRents(<%=i%>, '<%=pstrItemRevenueExc[i]%>', '<%=pstrItemRentExc[i]%>');<%
                  /* Marcando Servicios Adicionales */
                  if (pstrItemExcServiceId[i] != null){
                     StringTokenizer tkItemExcSrvId   = new StringTokenizer(pstrItemExcServiceId[i],"|");
                     StringTokenizer tkItemExcSrvDisc = new StringTokenizer(pstrItemExcServiceDiscnt[i],"|");
                     while ( tkItemExcSrvId.hasMoreTokens() ){
                        String strServId   = tkItemExcSrvId.nextToken();
                        String strServDisc = tkItemExcSrvDisc.nextToken();                        
                        %>
                        loadService(<%=i%>, '<%=strServId%>', '<%=strServDisc%>');
                        <%
                     } 
                  }
                  /* Marcando Minutos Adicionales */      
                   
                  if ( (pstrItemMinAdiCD != null) || (pstrItemMinAdiIT != null) ){
                     pstrItemMinAdiCD[i] = pstrItemMinAdiCD[i].compareTo("Y")==0?pstrItemMinAdiCD[i]:"N"; 
                     pstrItemMinAdiIT[i] = pstrItemMinAdiIT[i].compareTo("Y")==0?pstrItemMinAdiIT[i]:"N"; 
                                                              
                     %>loadMinAdd(<%=i%>,'<%=pstrItemMinAdiCD[i]%>', '<%=pstrItemMinAdiIT[i]%>');<%
                  }   
               }
               /* Cargando Periodos de Excepciones*/               
               if ( (strBeginPeriod.compareTo("null") != 0) && (strEndPeriod.compareTo("null") != 0) ){
                 strBeginPeriod = MiUtil.getMessageClean(strBeginPeriod);
                 strEndPeriod = MiUtil.getMessageClean(strEndPeriod);
                 StringTokenizer tkBeginPeriod = new StringTokenizer(strBeginPeriod, "|");
                 StringTokenizer tkEndPeriod = new StringTokenizer(strEndPeriod, "|");
                 int j = 0;
                 while ( tkBeginPeriod.hasMoreTokens() ){%>                   
                    strBegin = "<%=tkBeginPeriod.nextToken()%>";
                    strEnd =  "<%=tkEndPeriod.nextToken()%>";                                       
                    <%if (strAction.compareTo("VIEW")==0){%>
                      AddItemPeriodoForView("tblPeriodo");
                    <%}else{%>                        
                      AddItemPeriodo("tblPeriodo");
                    <%}%>                       
                    loadPeriodo(<%=j%>, strBegin, strEnd);
                    <% j++;
                 }
                } 
                %>
              
               
               calcBasicRent(form);
               <%
               if (bIsRent){
               %> calcRent(form);<%
               }               
               %>
               calcServiceDiscount();
               calcGeneralDiscount();
            }
            
            function fxSendItemExceptiontoOrder(){               
               var form        = document.frmdatos;
               var form_order  = parent.opener.document.frmdatos;
               var nun_rows    = parent.opener.getNumRows("items_table");
               var num_srvs    = form.hdnServiceColsQty.value;
               var num_periodo = form.hdnPeriodoQty.value;
               var vItemRevenueExc = new Vector();
               var iIsRent= <%=nAlquilerShow%>;
               /* Valido que el Botón de Calculo haya terminado de procesar */
               if (form.hdnDataChanged.value=="3"){
                  alert("Espere un momento mientras se realiza el cálculo");
                  return;
               }
               
               /* Valido que el Botón de Calculo de descuento sea presionado si ha habido algún cambio. */
               if (form.hdnDataChanged.value=="1"){
                  alert("Por favor primero presione el botón \""+form.btnCalcular.value+"\"");
                  form.btnCalcular.focus();
                  return;
               }
               
               /* Valido los periodos ingresados */
               if (validatePeriod&&!validatePeriodo(form)){
                  return;
               }               
               
               if (nun_rows==1){
                  /*Enviando Campos a la Orden - Campos de Renta Basica */                 
                  SetParentItemField(form_order.txtItemExceptionRevenueDisc, form.hdnBasicRentDesc, form_order.txtItemException, -1);
                  SetParentItemField(form_order.txtItemExceptionRevenue, form.txtBasicRentException, form_order.txtItemException, -1);                                    
                  if ( (iIsRent) && (form.txtRentException != null) ){
                     /* Enviando Campos a la Orden - Campos de Renta de Alquiler */                    
                     SetParentItemField(form_order.txtItemExceptionRentDisc, form.hdnRentDesc, form_order.txtItemException, -1);
                     SetParentItemField(form_order.txtItemExceptionRent, form.txtRentException, form_order.txtItemException, -1);                    
                  }
                  
                  /* Enviando Campos a la Orden - Campos de Servicios */
                  var strServiciosId  = "";
                  var strServiceDiscount  = "";
                  for (var j=1;j<=num_srvs;j++) {
                     if (eval("form.hdnServicioChecked"+j+".value==\"Y\"")) {
                        strServiciosId     = strServiciosId     + "|" + eval("form.hdnServiceId"+j+".value");
                        strServiceDiscount = strServiceDiscount + "|" + eval("form.hdnServiceDiscount"+j+".value");
                     }
                  }
                  SetParentItemServField(form_order.txtItemExceptionServiceId, strServiciosId, form_order.item_perform_flag_serv, -1);
                  SetParentItemServField(form_order.txtItemExceptionServiceDisc, strServiceDiscount, form_order.item_perform_flag_serv, -1);                  
                  
                  /* Enviando Campos a la Orden - Minutos Adicionales */
                  if ( form.hdnMinAddConexDirecChecked != null ){                          
                     SetParentItemField(form_order.txtItemExceptionMinAdiCD, form.hdnMinAddConexDirecChecked, form_order.txtItemException, -1);
                     SetParentItemField(form_order.txtItemExceptionMinAdiIT, form.hdnMinAddInterConexChecked, form_order.txtItemException, -1);
                  }
                  
               }
               else {                  
                  for (var i=0;i<nun_rows;i++) {                     
                     /*Enviando Campos a la Orden - Campos de Renta Basica */                       
                     SetParentItemField(form_order.txtItemExceptionRevenueDisc[i], form.hdnBasicRentDesc[i], form_order.txtItemException, i);                     
                     SetParentItemField(form_order.txtItemExceptionRevenue[i], form.txtBasicRentException[i], form_order.txtItemException, i);                        
                     if ( (iIsRent) && (form.txtRentException != null) ){                                             
                        /* Enviando Campos a la Orden - Campos de Renta de Alquiler */                    
                        SetParentItemField(form_order.txtItemExceptionRentDisc[i], form.hdnRentDesc[i], form_order.txtItemException, i);
                        SetParentItemField(form_order.txtItemExceptionRent[i], form.txtRentException[i], form_order.txtItemException, i);                                            
                     }                     
                     /* Enviando Campos a la Orden - Campos de Servicios */
                 
                     var strServiciosId  = "";
                     var strServiceDiscount  = "";
                     for (var j=1;j<=num_srvs;j++) {
                        if (eval("form.hdnServicioChecked"+j+"["+i+"].value==\"Y\"")) {                           
                           strServiciosId     = strServiciosId     + "|" + eval("form.hdnServiceId"+j+"["+i+"].value");
                           strServiceDiscount = strServiceDiscount + "|" + eval("form.hdnServiceDiscount"+j+"["+i+"].value");
                        }
                     }                     
                     SetParentItemServField(form_order.txtItemExceptionServiceId[i], strServiciosId, form_order.item_perform_flag_serv, i);
                     SetParentItemServField(form_order.txtItemExceptionServiceDisc[i], strServiceDiscount, form_order.item_perform_flag_serv, i);
                     
                     if ( form.hdnMinAddConexDirecChecked != null ){               
                        /* Enviando Campos a la Orden - Minutos Adicionales */                                    
                        SetParentItemField(form_order.txtItemExceptionMinAdiCD[i], form.hdnMinAddConexDirecChecked[i], form_order.txtItemException, i);
                        SetParentItemField(form_order.txtItemExceptionMinAdiIT[i], form.hdnMinAddInterConexChecked[i], form_order.txtItemException, i);
                     }
                  }
               }               
               /*Se agregan los periodos de excepciones*/
               var strPeriodoIni = "";
               var strPeriodoFin = "";
               if (num_periodo=="1"){
                  strPeriodoIni  = strPeriodoIni + "|" + form.txtPeriodoIni.value;
                  strPeriodoFin  = strPeriodoFin + "|" + form.txtPeriodoFin.value;
               }else{
                  for (var i=0; i<num_periodo; i++) {
                     strPeriodoIni  = strPeriodoIni + "|" + eval("form.txtPeriodoIni["+i+"].value");
                     strPeriodoFin  = strPeriodoFin + "|" + eval("form.txtPeriodoFin["+i+"].value");
                  }
               }
               form_order.hdnPeriodoIni.value = strPeriodoIni;
               form_order.hdnPeriodoFin.value = strPeriodoFin;
               
               if (form_order.hdnPerformance_flag_period!=null){
                  form_order.hdnPerformance_flag_period.value = strChangePeriod
               }
               
               if (form_order.hdnDescuentoRentaGratisNew!=null){
                  form_order.hdnDescuentoRentaGratisNew.value = divTotalGeneralDiscount.innerText;
               } 
               form_order.hdnExceptionRevenueAmount.value =  divTotalBasicRentDesc.innerText;
               
               /*El flag de excepción se setea */
               if (divTotalBasicRentDesc.innerText > 0)
                  form_order.hdnExceptionRevenue.value = 1;
                  
               form_order.hdnExceptionTotal.value = divTotalDiscount.innerText;
               form_order.hdnExceptionBillCycle.value= form.hdnExceptionBillCycle.value;
               form_order.hdnChangedOrder.value ="S";
               top.close();
            } 
      fxLoadField();
      </script>
   </html>
<%} catch (Exception  e) {         
     //e.printStackTrace();
     System.out.println("PopUpException.jsp: " + e.getMessage() );
    %>
      <script>
         alert("<%=MiUtil.getMessageClean(e.getMessage())%>");         
         //top.close();
      </script>
   <% 
    out.close();
   }
%>