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
<%@ page import="pe.com.nextel.bean.ServiciosBean" %>
<%@ page import="pe.com.nextel.bean.SpecificationBean" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.service.SessionService" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="pe.com.nextel.service.EditOrderService" %>
<%@ page import="pe.com.nextel.service.AutomatizacionService"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.text.NumberFormat"%>
<%@page import="pe.com.nextel.bean.OrderRentaAdelantadaBean"%>
<%
try{
   NewOrderService  objNewOrderService  = new NewOrderService();
   SessionService   objSession          = new SessionService();
   GeneralService   objGeneralService   = new GeneralService();
   EditOrderService objOrderService     = new EditOrderService();
   AutomatizacionService objAutomatService = new AutomatizacionService();
   NumberFormat formatter = new DecimalFormat("#0.00");     
   ProductBean objProdBean = new ProductBean();
   Integer strGrupo = null;
   
   Hashtable hshtinputNewSection = null;
   HashMap   hshSpecification = new HashMap();
   HashMap   hshData=null;
   HashMap   hshScreenField=null;
   HashMap   hshAutomat = null;
   ArrayList objItemHEader = new ArrayList();
   String    strCustomerId= "",
             strnpSite ="",
             strCodBSCS = "",
             hdnSpecification = "",
             strDivision ="",
             strOrderId  = "",
             strMessage  = "",
             strSiteOppId = "",
             strUnknwnSiteId = "",
             strEstadoOrden = "",
             strCreatedBy = "",
             strLogin = "",
             strStatus   = "";
   String    strSessionId=null;
   String    strFlagModality="";
   strGrupo = new Integer(0);
   
   boolean   bShowExceptionButton = false, 
            flgSA = false,
            bShowAgreementButton = false;
   SpecificationBean objSpecificationBean= null;
   long     lOrderId = 0; //DLAZO
   
   String strTypeDocument = ""; //PRY-0762 JQUISPE
   String strDocument = ""; //PRY-0762 JQUISPE
   boolean isRentaAdelantada = false; //PRY-0762 JQUISPE
   boolean isProrrateo = false; //PRY-0890 JBALCAZAR
   String strProrrateo = ""; //PRY-0890 JBALCAZAR
   String strOrderchildid = ""; //PRY-0890 JBALCAZAR
   String strOrderparentId = ""; //PRY-0890 JBALCAZAR
   String strPaymentTotalProrrateo = ""; //PRY-0890 JBALCAZAR 
   String strGeneratorId = ""; //PRY-0890 JBALCAZAR
   String strGeneratorType = ""; //PRY-0890 JBALCAZAR   
   
   
   hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputDetailSection");
   
   /*Obtener npvalue del Estado de ejecucion del Item*/ // JSALAZAR 01022011
   String strStatusItemExecution  = objGeneralService.getValue(Constante.ESTADO_EJECUCION_ITEM_NPTABLE, Constante.ESTADO_EJECUCION_ITEM_NPDESC);
   
   if ( hshtinputNewSection != null ) {
      strCustomerId           =   (String)hshtinputNewSection.get("strCustomerId");
      strnpSite               =   (String)hshtinputNewSection.get("strSiteId");
      strCodBSCS              =   (String)hshtinputNewSection.get("strCodBSCS");
      hdnSpecification        =   (String)hshtinputNewSection.get("strSpecificationId");
      strDivision             =   MiUtil.getString((String)hshtinputNewSection.get("strDivisionId"));
      strOrderId              =   (String)hshtinputNewSection.get("strOrderId");
      strSessionId            =   (String)hshtinputNewSection.get("strSessionId");
      
      strSiteOppId            =   (String)hshtinputNewSection.get("strSiteOppId");
      strUnknwnSiteId         =   (String)hshtinputNewSection.get("strUnknwnSiteId");
      strStatus               =   MiUtil.getString((String)hshtinputNewSection.get("strStatus")); 
      //Inicio Acuerdos Comerciales
      strEstadoOrden          =   (String)hshtinputNewSection.get("strEstadoOrden");
      strLogin                =   (String)hshtinputNewSection.get("strLogin");
      strCreatedBy            =   (String)hshtinputNewSection.get("strCreatedBy");
      //Fin Acuerdos Comerciales
      
      lOrderId                =   MiUtil.parseLong((String)hshtinputNewSection.get("strOrderId")); //DLAZO
      
      strDocument         =   (String)hshtinputNewSection.get("strDocument"); //PRY-0762 JQUISPE
      strTypeDocument         =   (String)hshtinputNewSection.get("strTypeDocument"); //PRY-0762 JQUISPE
      System.out.println("DETAIL OnDisplaySectionItems strTypeDocument---------------"+strTypeDocument);
      
      //PRY-0890 JBALCAZAR
      if( hshtinputNewSection.get("strProrrateo")!=null){
      strProrrateo         =   (String)hshtinputNewSection.get("strProrrateo");
      System.out.println("DETAIL OnDisplaySectionItems strProrrateo---------------"+strProrrateo);
      }

      if( hshtinputNewSection.get("strOrderchildid")!=null){
      strOrderchildid         =   (String)hshtinputNewSection.get("strOrderchildid");
      System.out.println("DETAIL OnDisplaySectionItems strOrderchildid---------------"+strOrderchildid);
      }      

      if( hshtinputNewSection.get("strOrderparentId")!=null){
    	  strOrderparentId         =   (String)hshtinputNewSection.get("strOrderparentId");
          System.out.println("DETAIL OnDisplaySectionItems strOrderparentId---------------"+strOrderparentId);
      }
      
      if( hshtinputNewSection.get("strPaymentTotalProrrateo")!=null){
    	  strPaymentTotalProrrateo         =   (String)hshtinputNewSection.get("strPaymentTotalProrrateo");
          System.out.println("DETAIL OnDisplaySectionItems strPaymentTotalProrrateo---------------"+strPaymentTotalProrrateo);
      }       
      
      if( hshtinputNewSection.get("strGeneratorId")!=null){
    	  strGeneratorId         =   (String)hshtinputNewSection.get("strGeneratorId");
          System.out.println("DETAIL OnDisplaySectionItems strGeneratorId---------------"+strGeneratorId);
      }       

      if( hshtinputNewSection.get("strGeneratorType")!=null){
    	  strGeneratorType         =   (String)hshtinputNewSection.get("strGeneratorType");
          System.out.println("DETAIL OnDisplaySectionItems strGeneratorType---------------"+strGeneratorType);
      }      
            
      objItemHEader           =   objNewOrderService.ItemDAOgetHeaderSpecGrp(MiUtil.parseInt(hdnSpecification));
      
      //JQUISPE PRY-0762 Renta Adelantada Inicio
      HashMap hshRentaAdelantada = objNewOrderService.getOrdenRentaAdelantada(lOrderId);
      strMessage = (String)hshRentaAdelantada.get("strMessage");
      if (strMessage!=null){
    	  throw new Exception(strMessage);
      }
      //JQUISPE PRY-0762 Renta Adelantada
      OrderRentaAdelantadaBean objOrderRentaAdelantadaBean = (OrderRentaAdelantadaBean)hshRentaAdelantada.get("objOrderRentaAdelantadaBean");
      if(objOrderRentaAdelantadaBean.getNpOrderRefRentaAdelantadaId() != 0){
    	  isRentaAdelantada = true;
      }
      //JQUISPE PRY-0762 Renta Adelantada Fin
      
      
      request.setAttribute("objItemHEader",objItemHEader);
      request.setAttribute("strDivisionId",strDivision);

		/*	Autor: 	RDELOSREYES
			Motivo: Lo uso en objItemImeiService.jsp para saber cual es la página que la invoca.
			Fecha:	29/01/2008
		*/
		hshData=objOrderService.getOrderScreenField(MiUtil.parseLong(strOrderId),Constante.PAGE_ORDER_DETAIL);
      strMessage=(String)hshData.get("strMessage");
      if (strMessage!=null)
      throw new Exception(strMessage);  
      
      hshScreenField= (HashMap)hshData.get("hshData"); 
      
      request.setAttribute("objScreenField",hshScreenField);
	  request.setAttribute("pageSource", Constante.PAGE_ORDER_DETAIL);
      //Fin Manejo dinámico de controles
       
      hshSpecification = objGeneralService.getSpecificationDetail(MiUtil.parseLong(hdnSpecification));
      strMessage = (String)hshSpecification.get("strMessage");
      objSpecificationBean = (SpecificationBean)hshSpecification.get("objSpecifBean");
      
      if (!MiUtil.getString(strMessage).equals("")){%>
         <script>
         alert("<%=strMessage%>")
         </script>
      <%}else
         bShowExceptionButton = MiUtil.getString(objSpecificationBean.getNpExceptionDetail()).equals("S");
   }
   //Inicio Acuerdos Comerciales
   if (Constante.INBOX_ADM_VENTAS.equals(strEstadoOrden)&& strOrderId != null && strLogin.equals(strCreatedBy) && MiUtil.parseInt(hdnSpecification) != Constante.SPEC_PORTABILIDAD[0] && MiUtil.parseInt(hdnSpecification) != Constante.SPEC_PORTABILIDAD[1] ) {
      bShowAgreementButton = true;
   }
   //Fin Acuerdos Comerciales
   
   HashMap     objHashItemOrder     = objNewOrderService.ItemDAOgetItemOrder(MiUtil.parseLong(strOrderId));
   HashMap     hshProdBolsa         = objNewOrderService.getProductBolsa(MiUtil.parseLong(strCustomerId),MiUtil.parseLong(strnpSite));
   
   //INICIO - DLAZO
   HashMap hshProcessType = objGeneralService.getProcessTypeByOrderId(lOrderId);
   String strProcessType = (String)(hshProcessType.get("strProcessType")==null?"":hshProcessType.get("strProcessType"));
   //FIN -DLAZO
   
    
   if ( objHashItemOrder.get("strMessage") != null ) {%>
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
   
   PortalSessionBean sessionUser  = objSession.getUserSession(strSessionId);
   
  %>
  
  <script defer>
  var vctItemHeaderOrder = new Vector();
  var vItemsBorradoItems = new Vector();
  var vflagEnabled = false;
  
  </script>
      
  <input type="hidden" name="hdnNumeroOrder" value="<%=strOrderId%>">
  <input type="hidden" name="hdnSessionLogin" value="<%=sessionUser.getLogin()%>">
      
      <!--Botón Excepciones -->
      <%
      if (bShowExceptionButton){
      %>
        <div id ="divException">
           <table border="0" cellspacing="2" cellpadding="0" height="5" width="99%" ><tr align="center"><td></td></tr></table>
           <table width="8%" border="0" cellspacing="2" cellpadding="0">
              <tr><td name="CellException"><a href="#" onclick="fxException();return false"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/btnException.gif" border="0"></a></td></tr>
           </table>
        </div>
      <%}%>
      
      <!-- Inicio Acuerdos Comerciales-->
      <%
      if (bShowAgreementButton){
      %>
      <div id ="divTradeAgreement">
           <table border="0" cellspacing="2" cellpadding="0" height="5" width="99%" ><tr align="center"><td></td></tr></table>
           <table width="8%" border="0" cellspacing="2" cellpadding="0">
              <tr>
                <td name="CellException">
                  <input type="button" onclick="fxOpenTradeAgreement();"  value=" Acuerdos Comerciales ">
                  <!--<a href="#" onclick="fxOpenTradeAgreement();"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/btnException.gif" border="0"></a>-->
                </td>
              </tr>
           </table>
      </div>
      <br>
      <%}%>
      <!-- Fin Acuerdos Comerciales-->
      
 <TABLE height=5 cellSpacing=2 cellPadding=0 width="99%" border=0>
  <TBODY>
   <TR>
    <TD width="40%" align=left>    
          
      <!-- LINK PARA AGREGAR ITEMS NUEVOS-->
      <div id="divItemCreate">
         
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
                                      <INPUT type="button" id="btnCalculoPagoAnticipado" name="btnCalculoPagoAnticipado" value="Calcular monto anticipado" disabled>
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
      
      <INPUT type="hidden" name="hdnItemSelected">       
       <%
          ItemBean itemBean = null; 
          if( objItemOrder!=null && objItemOrder.size()>0 ){
            for(int i=0; i<objItemOrder.size(); i++ ){
              itemBean  = new ItemBean();
              itemBean  = (ItemBean)objItemOrder.get(i);
              strFlagModality=MiUtil.getString(itemBean.getNpmodalitysell());
              
              //Se agregó para mantener la Modalidad Propio no solo cuando el item de salida propio sea el último item de la lista
              //Necesario para la visualización de los campos Nuevo Numero y Estado de Operación de Automatización de Ventas
              if( MiUtil.parseInt(hdnSpecification)  == Constante.SPEC_POSTPAGO_VENTA ||
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
                                          for ( int i=0 ; i<objItemHEader.size(); i++ ){
                                          String valueVisible = "";
                                          objnpObjHeaderSpecgrp = new NpObjHeaderSpecgrp();
                                          
                                          objnpObjHeaderSpecgrp =  (NpObjHeaderSpecgrp)objItemHEader.get(i);
                                          if( objnpObjHeaderSpecgrp.getNpobjitemheaderid()==25 )
                                                  flgSA = true;
                                          
                                           if ( objnpObjHeaderSpecgrp.getNpdisplay().equals("N") ){
                                           //No muestro
                                              valueVisible = "style='display:none'";
                                            }  else{
                                                  //Expeciones para no visualizar un campo a pesar que la configuración indique que  se debe visualizar 
                                                   /*if( (MiUtil.parseInt(hdnSpecification) == Constante.SPEC_CAMBIO_MODELO || 
                                                          MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA ||
                                                          MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_NUEVA )
                                                          && (objnpObjHeaderSpecgrp.getNpobjitemheaderid()==58 )
                                                          && ( !strFlagModality.equals(Constante.MODALITY_PROPIO) && !strFlagModality.equals("Alquiler en Cliente") )   
                                                    )
                                                          valueVisible = "style='display:none'"; 
                                                    */      
                                                   if( (MiUtil.parseInt(hdnSpecification) == Constante.SPEC_CAMBIO_MODELO || 
                                                          MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA ||
                                                          MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_NUEVA ||
                                                          MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_TDE   ||
                                                          MiUtil.parseInt(hdnSpecification) == Constante.SPEC_REPOSICION_PREPAGO_TDE) //Se agrego subcategoria reposicion prepago tde - TDECONV034
                                                          && (objnpObjHeaderSpecgrp.getNpobjitemheaderid()==96 )
                                                          && (!"s".equals(strResult) ||                                                           
                                                          (!strFlagModality.equals(Constante.MODALITY_PROPIO) && !strFlagModality.equals("Alquiler en Cliente")   ) )                                                    
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
                                                  
                                                  //PRY - 0890 JBALCAZAR PRY-1002
                                                  if(!( (MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA || 
                                                          MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PORTABILIDAD_POSTPAGO ||
                                                          MiUtil.parseInt(hdnSpecification) == Constante.SPEC_VENTA_MOVILES_RETAIL_POSTPAGO))){
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
                                          <td align="center" class="CellLabel" <%=valueVisible%> >&nbsp;<%=objnpObjHeaderSpecgrp.getNpobjitemname()%>&nbsp;</td>
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
                                                    <INPUT type="radio" id="textApportionmentS"  value="S" name="textApportionment" disabled>Si</td>
                                                <td class="CellNbg" width="10%">
                                                    <INPUT type="radio" id="textApportionmentN"  value="N" name="textApportionment" disabled>No</td>
													<INPUT type="hidden" id="hdnApportionment" name="hdnApportionment" value ="<%=strProrrateo%>"/>
													<INPUT type="hidden" id="hdnIsApportionment" name="hdnIsApportionment" value ="<%=isProrrateo%>"/>
													<INPUT type="hidden" id="hdnGeneratorType" name="hdnGeneratorType" value ="<%=strGeneratorType%>"/>													
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
  
   //Invocación de Pop-up Excepciones
  function fxException(){
    var specid = "<%=hdnSpecification%>";
    fxEditException("<%=strSessionId%>", "VIEW", specid);     
  }
  
  function fxSectionNameFinalStatus(){
    return true;
  }
  
  </script>
  
<SCRIPT DEFER>
  
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
  new fxMakeItemImei( <%=itemDeviceBean.getNpcantidad()%>,<%=itemDeviceBean.getNpcantidad()%>,<%=itemDeviceBean.getNpcantidad()%> + "-" + "<%=(countAux)%>" , 
                     "<%=itemDeviceBean.getNpitemid()%>",  "<%=MiUtil.getString(itemDeviceBean.getNpimeinumber())%>",  
                     "<%=MiUtil.getString(itemDeviceBean.getNpsimnumber())%>",  "<%=MiUtil.getString(itemDeviceBean.getNpproductname())%>",  
                     "<%=itemDeviceBean.getNpproductid()%>",  "<%=MiUtil.getString(itemDeviceBean.getNpplanname())%>",  
                     "<%=MiUtil.getString(itemDeviceBean.getNpbadimei())%>", "<%=MiUtil.getString(itemDeviceBean.getNpcheckimei())%>",
                     "<%=MiUtil.getString(itemDeviceBean.getNpmodality())%>","<%=MiUtil.getString(itemDeviceBean.getNpwarrant())%>",
                      "<%=MiUtil.getString(itemDeviceBean.getNpphone())%>","<%=MiUtil.getString(itemDeviceBean.getNperrorautom())%>",
                     "<%=MiUtil.getString(itemDeviceBean.getNpitemdeviceid())%>"));
                     //Adiciono el phone, errorAutom, itemDeviceid  
                     

  fxAddRowTableImeis( "table_imeis" , vctItemsImei.elementAt(0) , 9 , -1);

  vctItemsMainImei.addElement(vctItemsImei);
  <%}%>
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
      //Si la orden de traslado ya está cerrada, se usa la nueva dirección de instalación (dirección destino)
      if(count_items < 2){        
        if (form.txtEstadoOrden.value == "<%=Constante.ORDER_STATUS_CERRADO%>"){
          strAddressId = form.hdnItemValuetxtItemDestinyAddress.value;
        }else{
          strAddressId = form.hdnItemValuetxtItemAddressInstall.value;
        }                
      }else{
        if (form.txtEstadoOrden.value == "<%=Constante.ORDER_STATUS_CERRADO%>"){
          strAddressId = form.hdnItemValuetxtItemDestinyAddress[currentIndex].value;
        }else{      
          strAddressId = form.hdnItemValuetxtItemAddressInstall[currentIndex].value;
        }
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
     
     var a=appContext+"/serviceservlet?myaction=loadServiceItems&servicios_item="+servicios_item_send+"&hdnSpecification=<%=hdnSpecification%>&cmb_ItemPlanTarifario="+strPlanId+"&cmb_ItemSolution="+strSolutionId+"&phonenumber="+phonenumber+"&lorderId="+lorderId+"";     
   
     form.action = a;
     
     form.submit();
   }catch(e){
     alert("Para esta categoría no se aplican Servicios Adicionales");
   }
   }
   
  }
  
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
      
      /**Envia el número de la orden*/
      a = a + "&strOrderId=<%=strOrderId%>";
      
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
  
  function fxDeleteItemDetail(){
      alert("No puede eliminar, se encuentra en modo consulta");
  }
  
  function fxChangeItemEditDetailOrderDetail(item_index,vctItemsMainFrameOrder,vctrAuxOrden) {   
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
       
       <% for ( int i=0 ; i<objItemHEader.size(); i++ ){
        NpObjHeaderSpecgrp objnpObjHeSpec = new NpObjHeaderSpecgrp();
          objnpObjHeSpec =  (NpObjHeaderSpecgrp)objItemHEader.get(i);%>
          
        
          if( replace(objAuxRead.npobjitemheaderid,"'","") =="<%=objnpObjHeSpec.getNpobjitemheaderid()%>"){
              
              strUrlItemHeaderId += "a="+replace(objAuxRead.npobjitemheaderid,"'","")+"&";
              //strUrlItemHeaderId += "b="+replace(objAuxRead.npobjitemvalue,"'","")+"&";
              strUrlItemHeaderId += "b="+replace(replace(objAuxRead.npobjitemvalue,"'",""),"%","<%=Constante.CONVERT_PORCENT%>")+"&";              
              continue;
          }
      <%}%>
     }
     
     //alert("strUrlItemHeaderId : " + strUrlItemHeaderId)
     
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
  
  /*jsalazarl - modif hpptt # 1 - 27/09/2010 - inicio*/
  
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
  
  /*jsalazar - modif hpptt # 1 - 27/09/2010 - fin*/
  
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
    
    cantElement = vctItemOrder.size();
    
    var flagvalida96 = "N";//Debido a que en la fxLoadData() se usa 2 veces el codigo 96
    
        if (cantElement > 1) {/* los argumentos pasados.. a partir del segundo. */
          
          var row   = items_table.insertRow(-1);
          
          elemText =  "";
          var new_grupo = parseInt(form.hdn_item_imei_grupo.value) + parseInt(1);
          form.hdn_item_imei_grupo.value = new_grupo;
          
          /*Agrego la primera línea*/
          var cellPrinc = row.insertCell(index_arg - 1);
          elemText      = "<div id=\'contTable\' align='center' class='CellContent' >"+
                          "<input type=\'radio\' name=\'item_chek\' onclick=\'javascript:fxCargaServiciosItemItem(fxGetRowIndex(this));\'>"+
                          "<a href=\'javascript:;\' onclick=\'javascript:fxChangeItemEditDetailOrderDetail(fxGetRowIndex(this),parent.mainFrame.vctItemsMainFrameOrder,parent.mainFrame.vctParametersOrder);\' ALT=\'Editar Item\'><font color=\'brown\' size=\'2\' face=\'Arial\' ><b>"+ (items_table.rows.length-1) +"</b></font></a> "+
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
              
              
                if ( objItemHeader.npobjitemheaderid == objItem.npobjitemheaderid ) {
                
                    valuevisible = " value='"+ objItem.npobjitemvalue + "' ";
                    descvisible  = " value='"+ objItem.npobjitemvaluedesc + "' ";
                    
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
                        if((MiUtil.parseInt(hdnSpecification) == Constante.SPEC_CAMBIO_MODELO  || 
                            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA ||
                            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_NUEVA  ||
                            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_TDE    ||
                            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_REPOSICION_PREPAGO_TDE) 
                            //Se agrego subcategoria reposicion prepago tde - TDECONV034
                            && (!"s".equals(strResult) || (!strFlagModality.equals(Constante.MODALITY_PROPIO) && !strFlagModality.equals("Alquiler en Cliente")  )   )
                          ) { %>                   
                         
                         if(/*objItemHeader.npobjitemheaderid == 58 ||*/ objItemHeader.npobjitemheaderid == 96){
                            continue;
                         }
                         
                    <%   }else if((  MiUtil.parseInt(hdnSpecification) == Constante.SPEC_CAMBIO_MODELO || 
                            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA ||
                            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_NUEVA ||
                            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_TDE ||
                            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_REPOSICION_PREPAGO_TDE)//Se agrego subcategoria reposicion prepago tde - TDECONV034
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
                        if (objItemHeader.npobjitemheaderid != '<%=strStatusItemExecution%>'){ /*jsalazar - modif hpptt # 1 - 29/09/2010 - 01022011*/
                        elemText += "<input type='text'   name='"+ trim(objItemHeader.nphtmlname) +"' ";
                        elemText += descvisible;
                        elemText += descvisible;
                        if  ( (objItemHeader.npobjitemheaderid==2) || (objItemHeader.npobjitemheaderid==5) || (objItemHeader.npobjitemheaderid==4) ) {
                           elemText += " size='13' style='text-align:right' readOnly></div>";
                        } else if (objItemHeader.npobjitemheaderid==139){ // MMONTOYA [ADT-RCT-092 Roaming con corte]
                           elemText += " size='35' style='text-align:right' readOnly></div>";
                        } else{
                          if( (objItemHeader.npobjitemheaderid==10) || (objItemHeader.npobjitemheaderid==16) )
                             elemText += " size='22' style='text-align:right' readOnly> </div>";
                          else{
                             if ( (objItemHeader.npobjitemheaderid==3) || (objItemHeader.npobjitemheaderid==11) )
                                elemText += " size='10' style='text-align:right' readOnly> </div>";
                             else {
                                 if(objItemHeader.npobjitemheaderid==51){
                                     <%if(MiUtil.parseInt(hdnSpecification) == Constante.SPEC_BOLSA_CREAR || MiUtil.parseInt(hdnSpecification) == Constante.SPEC_BOLSA_UPGRADE || MiUtil.parseInt(hdnSpecification) == Constante.SPEC_BOLSA_DOWNGRADE || MiUtil.parseInt(hdnSpecification) == Constante.SPEC_BOLSA_DESACTIVAR ){%> // LHUAPAYA  [ADT-BCL-083]
                                     elemText += " size='35' style='text-align:right' readOnly></div>";
                                     <%}else{%>
                                     elemText += " size='12' style='text-align:right' readOnly></div>";
                                     <%}%>
                                 }else {
                                     elemText += " size='12' style='text-align:right' readOnly> </div>";
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
                        "<a href='javascript:fVoid()' onclick='javascript:fxDeleteItemDetail();'  ><img src='<%=Constante.PATH_APPORDER_SERVER%>/images/Eliminar.gif' border='no' ALT='Borrar Item'></a>"+
                        "</div>";
                        
            cellDelete.innerHTML = elemText+contentHidden;
            
       }//Fin del If CantElements
       
        wn_items = (items_table.rows.length -1); // numero de items 
        //alert("Valor wn_items : " + wn_items);  
          if ( wn_items > 1 ){
              wb_existItem =true;
          }
    
    fxCalculateTotal();
    
    <% if(isProrrateo){ %>	
    fxCargarMontoProrrateo();
    <% } %>      
    
    if( vflagEnabled )
    fxLoadImeis(form.hdn_item_imei_grupo.value);
  
  }
  
  function fxEditRowOrderItemsGlobal(vctItemOrder,itemEdit_Index){
  
  form = document.frmdatos;
  
      try{
        form.item_services.value = GetSelectedServices();
      }catch(e){}
      
      var objvctItemHeaderOrder = vctItemHeaderOrder;
      var item_index = itemEdit_Index;
      
      
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
    fxCalculateTotal();
    
    <% if(isProrrateo){ %>	
    fxCargarMontoProrrateo();
    <% } %>     
  }
  
  function fxCalculateTotal(){
    var form = parent.mainFrame.document.frmdatos;
    var totalPrice = "0.0";
    if( (items_table.rows.length -1) == 1 ){
      try{
        //form.txtitemTotal.value = parseFloat(round_decimals(form.txtitemTotal.value ,2));
        totalPrice = form.txtitemTotal.value;
      }catch(e){
        totalPrice = "0.0";
      }
    }else{
      try{
        for(k=0; k<form.txtitemTotal.length; k++)
           //form.txtitemTotal[k].value = parseFloat(round_decimals(form.txtitemTotal[k].value,2));
           totalPrice = parseFloat(totalPrice) + parseFloat(form.txtitemTotal[k].value);
      }catch(e){
        totalPrice = "0.0";
      }
    }
    form.txtTotalSalesPrice.value = round_decimals(parseFloat(totalPrice),2);
	try {
		if(form.txtTotalSalesPrice.value==undefined || form.txtTotalSalesPrice.value=="undefined") {
			if(form.txtitemTotal.value==undefined) {
				form.txtTotalSalesPrice.value = form.txtitemTotal[0].value;
			} else {
				form.txtTotalSalesPrice.value = form.txtitemTotal.value;
			}
		}
	} catch(exception) {
		form.txtTotalSalesPrice.value = "0.0";
	}
  
  }
   
//PRY-0890    JBALCAZAR   	 	  	
  function fxCargarMontoProrrateo(){
		var montosprorrateo = [];
	  	var TotalApportionment = 0;

	  	try{  	
			$("#items_table tr td div input[name='txtMontoProrrateo']").each(function () {
						
				if($(this).val() == "undefined" || $(this).val() == undefined || $(this).val() == "" ){
					montosprorrateo.push(0);
			
				}else{
					montosprorrateo.push($(this).val());			
				}	 
		    });
				
			for(var i=0 ;i<montosprorrateo.length;i++){
				TotalApportionment += parseInt(montosprorrateo[i]);
			}	
			
			if(TotalApportionment>0){
			$("#txtTotalPriceApportionment").val(TotalApportionment);	
			}
			

			$("#txtTotalPrice").val(parseFloat($("#divLeyendaTotal tr td input[name='txtTotalSalesPrice']").val()).toFixed(2));				
		
		 }catch(e){}	
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
  function deleteItem(CellObject,item_id,item_current){
   var form = document.frmdatos;
   var item_pedido_numero = CellObject.parentNode.parentNode.parentNode.rowIndex;
   /* inicioFelipe */
   var item_promo_header_id = CellObject.parentNode.parentNode.childNodes.item(3).getAttribute("value");

   /* finFelipe */
      if (confirm("Desea eliminar el item?")){
         if (deleteItemEnabled){
            var table = document.all ? document.all["items_table"]:document.getElementById("items_table");
            wb_existItem=(wn_items>=1)?false:wb_existItem;
            wb_existItem=(wn_items>2)?true:false;
            //DeleteItemImeis(item_pedido_numero);
            var itemIdTable = form.hdnItemId[item_id-1].value;
            //Eliminar también del Vector Padre
            //***PENDIENTE**///
            table.deleteRow(CellObject.parentNode.parentNode.parentNode.rowIndex);
            wn_items=(table.rows.length-1);
            //alert("item_id : " + item_id)
            //Obtener el ItemId 
            
            if (item_id > 0) {
               vItemsBorradoItems.addElement(itemIdTable);
               //alert("Una vez")
            };
            
            if (parseInt(document.frmdatos.hdnItemSelectService.value) == parseInt(item_pedido_numero) ){
               deleteAllValues(document.frmdatos.cmbSelectedServices);
            }
            
            /*Eliminar los IMEIS*/
             countImeis = table_imeis.rows.length;
             
             var deleteIndex = item_current-1;
             vctItemsMainFrameOrder.removeElementAt(deleteIndex);
            
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
             /* FIN DE ELIMINAR LOS IMEIS */
             fxCalculateTotal();
         }else {
            alert("No puede eliminar, cierre antes la ventana de edición de Items");
         };
         return false;
      }
   /*}      */
   
   //alert("vItemsBorrado : " + vItemsBorrado.size());
  }
  </script>
  
  <script DEFER>
  
  function fxSectionNameValidate(){
    var form = document.frmdatos;
    if( items_table.rows.length < 2 ){
      alert("Debe agregar al menos un Item");
      return false;
    }
    
    wv_item_id = "";
    for(i=0; i<vItemsBorradoItems.size(); i++){
       if( vItemsBorradoItems.elementAt(i)  != undefined ) 
            wv_item_id += "|" + vItemsBorradoItems.elementAt(i);
    };
    
    form.hdnItemBorrados.value = wv_item_id;
    
    return true;
  }
  
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
  //ItemBean itemBean = null;
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
    
    <%//Modelo del Equipo (Duda)
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
      objDescr = objDescr.equals("null")?"":objDescr;
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('10','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Equipo  
      objValue  =   ""+MiUtil.getString(itemBean.getNpequipment());
      objDescr  =   ""+MiUtil.getString(itemBean.getNpequipment());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('11','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Devolución de Equipo
      objValue  =   MiUtil.getString(itemBean.getNpequipmentreturn());
      objDescr  =   MiUtil.getString(itemBean.getNpequipmentreturndesc());//MiUtil.getString(itemBean.getNpequipmentreturn());
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('12','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Pendiente de Recojo
      objValue  =   ""+itemBean.getNpequipmentnotyetgiveback();
      objDescr  =   ""+itemBean.getNpequipmentnotyetgiveback();
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
      var totalItem="0.0";
      totalItem=round_decimals(parseFloat(<%=objValue%>),2);
      vctOrderItem.addElement(new fxMakeOrderItem('22','','','','','','','','','','','',totalItem,totalItem,'A','<%=itemId%>'));
    
    <%//Promocion
      objValue  =   ""+itemBean.getNppromotionid();
      objDescr  =   ""+itemBean.getNppromotionid();
    %>  
      vctOrderItem.addElement(new fxMakeOrderItem('23','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    <%//Servi Adicionales
      objValue  =   ""+itemBean.getNpitemservices();
      objDescr  =   ""+itemBean.getNpitemservices();
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
      objValue  =   ""+itemBean.getNpitembillingaccount();
      objDescr  =   ""+itemBean.getNpitembillingaccount();
    %>
      vctOrderItem.addElement(new fxMakeOrderItem('39','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    /*Campos de Excepciones*/        
    <%//Excepción Item
       objValue = ""+itemBean.getNpexception();
       objDescr = ""+itemBean.getNpexception();
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
       objValue = ""+itemBean.getNpitemfreeservices();
       objDescr = ""+itemBean.getNpitemfreeservices();
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
                && (      strFlagModality.equals(Constante.MODALITY_PROPIO) || strFlagModality.equals("Alquiler en Cliente")   )
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
      
       if((  MiUtil.parseInt(hdnSpecification) == Constante.SPEC_CAMBIO_MODELO  || 
              MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA ||
              MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_NUEVA  ||
              MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_TDE    ||
              MiUtil.parseInt(hdnSpecification) == Constante.SPEC_REPOSICION_PREPAGO_TDE) 
              //Se agrego subcategoria reposicion prepago tde - TDECONV034
              && ( strFlagModality.equals(Constante.MODALITY_PROPIO) || strFlagModality.equals("Alquiler en Cliente"))
              && ("s".equals(strResult))
              || (MiUtil.parseInt(hdnSpecification) != Constante.SPEC_POSTPAGO_VENTA &&
                  MiUtil.parseInt(hdnSpecification) != Constante.SPEC_PREPAGO_NUEVA &&
                  MiUtil.parseInt(hdnSpecification) != Constante.SPEC_CAMBIO_MODELO &&
                  MiUtil.parseInt(hdnSpecification) != Constante.SPEC_PREPAGO_TDE   &&
                  MiUtil.parseInt(hdnSpecification) != Constante.SPEC_REPOSICION_PREPAGO_TDE) //Se agrego subcategoria reposicion prepago tde - TDECONV034
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
      objDescr  =    MiUtil.getString(itemBean.getNpsitename());
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
      
            //Agregado por RMARTINEZ 05-06-09 Ajustes para suspecion Definitiva Automática
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
      //FIn Agregado por RMARTINEZ 05-06-09 Ajustes para suspecion Definitiva Automática  
      
      
   ///Ajustes para suspension temporal Automática
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
    //CBARZOLA:Cambio de Modelo - Distintas tecnologias -Solucion Original(Id,Name)
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
     
     <%//DLAZO - Fecha de activación del SSAA - Suscripciones
      objValue  =   MiUtil.getString(itemBean.getNpitemservices());
      objDescr  =   MiUtil.getString(itemBean.getNpitemservices());
     %>  
      
      vctOrderItem.addElement(new fxMakeOrderItem('120','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
    
    /*jsalazar - modif hpptt # 1 - 29/09/2010 -  01022011 inicio*/
    
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
    
    <%//DLAZO - Fecha de desactivación del SSAA - Suscripciones
      objValue  =   MiUtil.toFecha(itemBean.getNpdeactivationdate());
      objDescr  =   MiUtil.toFecha(itemBean.getNpdeactivationdate());
     %>
     
     vctOrderItem.addElement(new fxMakeOrderItem('125','','','','','','','','','','','','<%=objValue%>','<%=objDescr%>','A','<%=itemId%>'));
     
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

      <%// LHUAPAYA - ADT-BCL-083
       objValue  =   ""+itemBean.getNpproductid();
       objDescr  =   ""+itemBean.getNpproductname();
      %>
      vctOrderItem.addElement(new fxMakeOrderItem('138','','','','','','','','','','','','<%=objValue%>',"<%=objDescr%>",'A','<%=itemId%>'));
     <% // MMONTOYA - ADT-RCT-092
       objValue  =   MiUtil.getString(itemBean.getNpserviceROA());
       objDescr  =   objValue;
     %>
     vctOrderItem.addElement(new fxMakeOrderItem('139','','','','','','','','','','','','<%=objValue%>',"<%=objDescr%>",'A','<%=itemId%>'));

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
      <% //PRY-0721 DERAZO Se carga la region del Item para mostrar en el detalle
         objValue  =   MiUtil.getString(itemBean.getNpzonacoberturaid());
         objDescr  =   MiUtil.getString(itemBean.getNpnombrezona());
       %>
      vctOrderItem.addElement(new fxMakeOrderItem('150','','','','','','','','','','','','<%=objValue%>',"<%=objDescr%>",'A','<%=itemId%>'));

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

      
      <%//INICIO Monto Prorrateo PRY-0890 JBALCAZAR PRY-1002
      if( (MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA || 
              MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PORTABILIDAD_POSTPAGO ||
              MiUtil.parseInt(hdnSpecification) == Constante.SPEC_VENTA_MOVILES_RETAIL_POSTPAGO)){    	  
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
	  <%//INICIO Monto Prorrateo PRY-0890 JBALCAZAR PRY-1002
	  if( (MiUtil.parseInt(hdnSpecification) == Constante.SPEC_VENTA_MOVILES_RETAIL_POSTPAGO || 
		  MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PORTABILIDAD_POSTPAGO) ){ 
	   %>   
		  
		  var hdnGeneratorType = $("input[name='hdnGeneratorType']").val();
		if(hdnGeneratorType == 'RETAIL'){		  
	   	  $("#tabLeyendaTotal  tr td:nth-child(1)").width('43%');
		  $("#tabLeyendaTotal  tr td:nth-child(2)").width('44%');
		  $("#tabLeyendaTotal  tr td:nth-child(3)").width('13%');
			var sumatotalitems = $("#divLeyendaTotal tr td input[name='txtTotalSalesPrice']").val();
			
			if(sumatotalitems == 'undefined' || sumatotalitems == undefined || sumatotalitems == ''){
				sumatotalitems = 0;
			}else{
				sumatotalitems = $("#divLeyendaTotal tr td input[name='txtTotalSalesPrice']").val();
			}
			
		  	var suma = "0.0";		  	
	  	  	$("#items_table tr td div input[name='txtMontoProrrateo']").each(function () {
		  		if($(this).val()==0 || $(this).val()==''){		
		  		$(this).val("0.00");
		  		}
				 suma = parseFloat(suma) + parseFloat($(this).val());		  		
		      });
	  	  	
	  	  $("#divLeyendaTotal tr td input[name='txtTotalSalesPrice']").val((parseFloat(suma)+parseFloat(sumatotalitems)).toFixed(2));			
			}		  
	   <%	 
		  }
	   	%> 
	   	
	   	
	  <% if((MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA || 
              MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PORTABILIDAD_POSTPAGO) ){ %>   	  	 	  
	  
      var hdnGeneratorType = $("input[name='hdnGeneratorType']").val();
	  if(hdnGeneratorType == 'INC'){      
      $("#tabLeyendaTotal  tr td:nth-child(1)").width('47%');
	  $("#tabLeyendaTotal  tr td:nth-child(2)").width('44%');
	  $("#tabLeyendaTotal  tr td:nth-child(3)").width('9%');
	  
	  var hdnProrrateo = $("input[name='hdnApportionment']").val();  
	  var temptotalprorrateo = $("#txtTotalPriceApportionment").val();
	  var temptotalitems = $("#divLeyendaTotal tr td input[name='txtTotalSalesPrice']").val();	  
	  document.getElementById("divProrrateo").style.display = 'block';
	  document.getElementById("divBtnProrrateo").style.display = 'block';
	  	  if(hdnProrrateo != 'S'){
	  		document.getElementById("textApportionmentN").checked = true;	  		
	  		$("input[name='hdnApportionment']").val('N');
	  		
	  	  	$("#items_table tr td div input[name='txtMontoProrrateo']").each(function () {		
		  		$(this).val("0.00");
		      });
	  	  $("#txtTotalPriceApportionment").val("0.00");
	  	  
	  	  }else if(hdnProrrateo =='S'){
	  		  document.getElementById("textApportionmentS").checked = true;	  		
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
	        
	//		  
	  
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
	  
	  }			
		
	  <% } 		   	
	  
	  %> 		  
	  
    }catch(e){}
  
  }

  function fxOpenTradeAgreement(){
    var page_params = escape("generatorid=<%=strOrderId%>&generatortype=ORDERS");
    var page_url = "/portal/page/portal/trade_agreement/TRADE_AGREEMENT_NEW";
    var url = "/portal/pls/portal/WEBSALES.NPSL_NEW_GENERAL_PL_PKG.PL_FRAME?av_page_url="+page_url+"&av_page_params="+page_params;
    WinAsist = window.open(url,"Acuerdos_Comerciales","toolbar=yes,location=0,directories=no,status=no,menubar=yes,scrollbars=yes,resizable=yes,screenX=800,top=800,left=200,screenY=300,width=1000,height=500,modal=yes");
  }
  </script>
  
  <SCRIPT DEFER>
  
  function fxSectionItemsOnload(){
  
    fxLoadData();
    vflagEnabled = true;
    try{
    fxLoadImeisDB();
    }catch(e){}
    
    try{
     fxOnLoadImeiService();
    }catch(e){}
    
    try{
       fxOnLoadServicesCM();
    }catch(e){}      
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
        
         <%
            for(int j=0; j<itemBean.getNpServiceItemList().size(); j++ ){
               serviciosBean = (ServiciosBean)itemBean.getNpServiceItemList().get(j);
               serviciosBean = objGeneralService.getDetailService(serviciosBean.getNpservicioid());               
         %>
              
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