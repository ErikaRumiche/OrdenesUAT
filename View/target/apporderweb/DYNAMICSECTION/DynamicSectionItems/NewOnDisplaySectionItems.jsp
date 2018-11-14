<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="pe.com.nextel.bean.NpObjHeaderSpecgrp" %>
<%@ page import="pe.com.nextel.bean.SpecificationBean" %>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.bean.ListBoxBean" %>
<%@ page import="pe.com.nextel.service.*" %>

<%
  Hashtable hshtinputNewSection = null;
  HashMap   hshSpecification = new HashMap();
  NewOrderService objNewOrderServiceItems = new NewOrderService();
  GeneralService objGeneralService   = new GeneralService();
  EditOrderService objOrderService=new EditOrderService();
  ArrayList objItemHEader = new ArrayList();
  
  HashMap hshScreenField=new HashMap();
  String    strCustomerId= "",
            strnpSite ="",
            strCodBSCS = "",
            hdnSpecification = "",
            strTypeCompany = "",
            strMessage  = "",
            strSessionId = "",
            strSiteOppId 	= "",
            strUnknwnSiteId = "",
            strGeneratorId  = "",
            strGeneratorType = "",
            strDivision = "",
            strSalesStuctOrigenId = "",
            strOrderId = "";
	
   boolean   bShowExceptionButton = false,
             flgSA = false,
             flgIMEIS = false;
   SpecificationBean objSpecificationBean= null;
   int   strValiditem=0;
   long     lOrderId = 0; //DLAZO
            
   String strTypeDocument = ""; //PRY-0762 JQUISPE
   String strDocument = ""; //PRY-0762 JQUISPE
   boolean isRentaAdelantada = false; //PRY-0762 JQUISPE
   boolean isProrrateo = false; //PRY-0890 JCURI
   String strRutaContext=request.getContextPath();
   String strURLApportionmentServlet =strRutaContext+"/apportionmentServlet";
   System.out.println("[NewOnDisplaySectionItems.jsp] apportionmentServlet: "+strURLApportionmentServlet);
   
   hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
   /*Obtener npvalue del Estado de ejecucion del Item*/ // JSALAZAR 01022011
   String strStatusItemExecution  = objGeneralService.getValue(Constante.ESTADO_EJECUCION_ITEM_NPTABLE, Constante.ESTADO_EJECUCION_ITEM_NPDESC);
  
	if ( hshtinputNewSection != null ) {
		strCustomerId           =   (String)hshtinputNewSection.get("strCustomerId");
		strnpSite               =   (String)hshtinputNewSection.get("strSiteId");
		strCodBSCS              =   (String)hshtinputNewSection.get("strCodBSCS");
		hdnSpecification        =   (String)hshtinputNewSection.get("strSpecificationId");
		strDivision             =   (String)hshtinputNewSection.get("strDivisionId");
		strTypeCompany          =   (String)hshtinputNewSection.get("strTypeCompany");
		strSessionId            =   (String)hshtinputNewSection.get("strSessionId");    
		strSiteOppId            =   (String)hshtinputNewSection.get("strSiteOppId");
		strUnknwnSiteId         =   (String)hshtinputNewSection.get("strUnknwnSiteId");		
		objItemHEader           =   objNewOrderServiceItems.ItemDAOgetHeaderSpecGrp(MiUtil.parseInt(hdnSpecification));
		strGeneratorId      		= 	(String)hshtinputNewSection.get("strGeneratorId");
		strGeneratorType  		  =	 (String)hshtinputNewSection.get("strGeneratorType");	
                strSalesStuctOrigenId   =	 (String)hshtinputNewSection.get("strSalesStuctOrigenId");
                lOrderId                =   MiUtil.parseLong((String)hshtinputNewSection.get("strOrderId")); //DLAZO
        strOrderId              =   (String)hshtinputNewSection.get("strOrderId");
                
        strDocument         =   (String)hshtinputNewSection.get("strDocument"); //PRY-0762 JQUISPE
        strTypeDocument         =   (String)hshtinputNewSection.get("strTypeDocument"); //PRY-0762 JQUISPE
        
        //JQUISPE PRY-0762 Renta Adelantada
        HashMap hshValidaRentaAdelantada = objNewOrderServiceItems.validarRentaAdelantada(hdnSpecification, MiUtil.parseLong(strCustomerId),strTypeDocument, strDocument, null);        
        strMessage = (String)hshValidaRentaAdelantada.get("strMessage");
        if (strMessage!=null){
      	  throw new Exception(strMessage);
        }        
        isRentaAdelantada = ((Boolean)hshValidaRentaAdelantada.get("resultado")).booleanValue();     
		
		System.out.println("[NewOnDisplaySectionItems.jsp]strGeneratorId: "+strGeneratorId);
		System.out.println("[NewOnDisplaySectionItems.jsp]strGeneratorType: "+strGeneratorType);
    System.out.println("[NewOnDisplaySectionItems.jsp]strSalesStuctOrigenId: "+strSalesStuctOrigenId);
    	System.out.println("[NewOnDisplaySectionItems.jsp]strTypeDocument: "+strTypeDocument); //PRY-0762 JQUISPE
    	System.out.println("[NewOnDisplaySectionItems.jsp]strDocument: "+strDocument); //PRY-0762 JQUISPE
	    System.out.println("[NewOnDisplaySectionItems.jsp]strnpSite: "+strnpSite); ///JBALCAZAR PRY-1002    
	    System.out.println("[NewOnDisplaySectionItems.jsp]strSessionId: "+strSessionId); ///JBALCAZAR PRY-1002        
		request.setAttribute("strDivisionId",strDivision);
		request.setAttribute("objItemHEader",objItemHEader);
		request.setAttribute("objScreenField",hshScreenField);
    request.setAttribute("hdnSpecification",hdnSpecification);//Reserva de Numeros Golden - RHUACANI-ASISTP - 24/10/2010
		/*	Autor: 	RDELOSREYES
		Motivo: Lo uso en objItemImeiService.jsp para saber cual es la página que la invoca.
		Fecha:	29/01/2008
		*/
		request.setAttribute("pageSource", Constante.PAGE_ORDER_NEW);
    
		hshSpecification = objGeneralService.getSpecificationDetail(MiUtil.parseLong(hdnSpecification));  
		 objSpecificationBean = (SpecificationBean)hshSpecification.get("objSpecifBean");    
    
		strValiditem = objSpecificationBean.getNpValiditem();
    
		if (!MiUtil.getString(strMessage).equals("") ){%>
			<script>
				alert("<%=strMessage%>")
			</script>
		<%}else if (objSpecificationBean != null){                  
			bShowExceptionButton = MiUtil.getString(objSpecificationBean.getNpExceptionDetail()).equals("S");      
		}         


  }
  
 

 
  SessionService objSession  = new SessionService();
  
  PortalSessionBean objSessionUser  = objSession.getUserSession(strSessionId);
  
	//JBALCAZAR PRY-1002               
  isProrrateo = (Boolean)objNewOrderServiceItems.getObtenerFlagActivoProrrateo(hdnSpecification,strTypeDocument ,strDocument,strGeneratorType,objSessionUser.getLogin()); 
  System.out.println("[NewOnDisplaySectionItems.jsp] - isProrrateo:"+isProrrateo+" USERID:"+objSessionUser.getLogin());  
	
    //INICIO DERAZO REQ-0428
    PenaltyService penaltyService = new PenaltyService();
    HashMap hshMapResultFunct = penaltyService.getFlagShowPenaltyFunctionality(strOrderId, hdnSpecification, "", objSessionUser.getLogin());
    int flagPenaltyFunct = (Integer)hshMapResultFunct.get("flag");

    System.out.println("[NewOnDisplaySectionItems.jsp] flagPenaltyFunct: "+flagPenaltyFunct + "-strSpecification: "+hdnSpecification);

    ArrayList<ListBoxBean> msgList = (ArrayList<ListBoxBean>)hshMapResultFunct.get("msgList");

    if(msgList != null){
        System.out.println("[NewOnDisplaySectionItems.jsp] msgList size: "+msgList.size());
    }
    //FIN DERAZO REQ-0428
  %>
      <!--Botón Excepciones   -->
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
<TABLE height=5 cellSpacing=2 cellPadding=0 width="99%" border=0>
  <TBODY>
   <TR>
    <TD width="40%" align=left>             
      <div id="divItemCrear">
         <table id="item_crear" name="item_crear" width="4%" border="0" cellspacing="2" cellpadding="0">
         <tr align="center">
            <td id="CellAddItem" name="CellAddItem" align="left" valign="middle" colspan="4" >&nbsp; 
               <a href="javascript:fxPreChangeItemNewDetailOrder(parent.mainFrame.vctParametersOrder,'ADDITEM');" > <!-- onmouseover=window.status='Agregar Item'; onmouseout=window.status=';>-->
               <img name="item_img_crear" ALT="Precio de Lista" src="<%=Constante.PATH_APPORDER_SERVER%>/images/crear.gif" border="no"></a>
            </td>
            <%
              if( MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA || MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PORTABILIDAD_POSTPAGO){
            %>
            <td id="CellPromo" name="CellPromo" align="left" valign="middle" colspan="4" >&nbsp; 
               <input type="button" id="btnEvaluarVO" value="Evaluar Promoción por V.O." onClick="javascript: fxEvaluateVO();" disabled>
            </td>
            <%
              }
            %>
         <!--</tr>
         </table>
         
         
         
         <table id="item_crear_load_imei" name="item_crear_load_imei" width="8%" border="0" cellspacing="2" cellpadding="0">
         <tr align="center">-->
         <%
         

         if( MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_NUEVA || MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_TDE
         ||  MiUtil.parseInt(hdnSpecification) == Constante.SPEC_REPOSICION_PREPAGO_TDE ){//Se agrego subcategoria reposicion prepago tde - TDECONV034
         
         int intFlagUserAdmVentas = 0;
         
         HashMap hshUserRolAdmVentas = null;
         hshUserRolAdmVentas = objGeneralService.getRol(3758, objSessionUser.getUserid(), objSessionUser.getAppId());  
         strMessage=(String)hshUserRolAdmVentas.get("strMessage");
         /*if (strMessage!=null)
            throw new Exception(strMessage);*/
         
         intFlagUserAdmVentas  = MiUtil.parseInt((String)hshUserRolAdmVentas.get("iRetorno"));
         System.out.println("Tiene Permiso : " + intFlagUserAdmVentas);
         
         if( intFlagUserAdmVentas == 1 ){ %>
            <td id="CellAddItemLoadIMEI" name="CellAddItemLoadIMEI" align="left" valign="middle" colspan="4" >&nbsp; 
               <a href="javascript:fxPreChangeItemNewDetailOrder(parent.mainFrame.vctParametersOrder,'ADDIMEI');" > <!-- onmouseover=window.status='Agregar Item'; onmouseout=window.status=';>-->
               <img name="item_img_crear_load_imei" ALT="Agregar ITEMS para Carga de SIM Masivo" src="<%=Constante.PATH_APPORDER_SERVER%>/images/add_reminder.gif" border="no"></a>
            </td>
        <%}%>
        
        <%}%>
        
         </tr>
         </table>
         
         
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
		                                <td width="27%" align="left"></td>
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
      <INPUT type="hidden" name="hdnPeriodoIni">
      <INPUT type="hidden" name="hdnPeriodoFin">
      <INPUT type="hidden" name="hdnPerformance_flag_period" value="n">
      <input type="hidden" name="hdnFlagItemImeis" value="0">
      <input type="hidden" name="hdnFlagItemSims" value="0">
      <input type="hidden" name="hdnTotalSalesPriceVEP" value="0">
      <input type="hidden" name="hdnFlagPenaltyFunct" value="<%=flagPenaltyFunct%>"><!--DERAZO REQ-0428 Se agrega hidden con flag-->
      
      <input type="hidden" name="hdn_item_imei_grupo" value="0"> <!-- Hidden que indica el # de grupos de item_pedido ingresados -->
      <input type="hidden" name="hdn_item_imei_selecc" value=""> <!-- Hidden que guarda el # del item_imei a trabajar -->
      
      <input type="hidden" name="hdn_validitem" value="<%=strValiditem%>"> <!--Hidden que envia valor npvaliditem del procedimiento SP_GET_CATEGORY_DETAIL -->
      
      <INPUT type="hidden" name="hdnItemSelected">

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
                                       <tr>
                                          <td align="center" class="CellLabel">&nbsp;&nbsp;&nbsp;</td>
                                          <%
                                          if( objItemHEader != null && objItemHEader.size() > 0 ){
                                            NpObjHeaderSpecgrp objnpObjHeaderSpecgrp = null;
                                              for ( int i=0 ; i<objItemHEader.size(); i++ ){
                                                String valueVisible = "";
                                                objnpObjHeaderSpecgrp = new NpObjHeaderSpecgrp();
                                                objnpObjHeaderSpecgrp = (NpObjHeaderSpecgrp)objItemHEader.get(i);
                                                //<!-- jsalazar - modif hpptt # 1 - 27/09/2010 01022011- inicio-->
                                                if( objnpObjHeaderSpecgrp.getNpobjitemheaderid()== MiUtil.parseInt(strStatusItemExecution) ){
                                                   valueVisible = "style='display:none'";                                                
                                                  }
                                                //<!-- jsalazar - modif hpptt # 1 - 27/09/2010 - fin-->
                                                if( objnpObjHeaderSpecgrp.getNpobjitemheaderid()==25 )
                                                  flgSA = true;
                                                if( objnpObjHeaderSpecgrp.getNpobjitemheaderid()==72 ) 
                                                  flgIMEIS = true;
                                                if ( objnpObjHeaderSpecgrp.getNpdisplay().equals("N") ){
                                                //No muestro
                                                    valueVisible = "style='display:none'";                                                
                                                }  else{
                                                  //Expeciones para no visualizar un campo a pesar que la configuración indique que  se debe visualizar 
                                                   if( (MiUtil.parseInt(hdnSpecification) == Constante.SPEC_CAMBIO_MODELO  || 
                                                        MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA ||
                                                        MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_NUEVA  ||
                                                        MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_TDE    ||
                                                        MiUtil.parseInt(hdnSpecification) == Constante.SPEC_REPOSICION_PREPAGO_TDE) //Se agrego subcategoria reposicion prepago tde - TDECONV034
                                                        &&
                                                       (objnpObjHeaderSpecgrp.getNpobjitemheaderid()==96 )
                                                    )
                                                        valueVisible = "style='display:none'"; 
                                                    
                                                    if(objnpObjHeaderSpecgrp.getNpobjitemheaderid()==125){ //Fecha de desactivacion
                                                        valueVisible = "style='display:none'";
                                                    }
                                                    
                                                    //JQUISPE PRY-0762
                                                    if(!isRentaAdelantada){
                                                    	if("txtTotalRA".equals(objnpObjHeaderSpecgrp.getNphtmlname()) ||
                                                          	"txtCantidadRA".equals(objnpObjHeaderSpecgrp.getNphtmlname())){
                                                    		 valueVisible = "style='display:none'";
                                                    		 objnpObjHeaderSpecgrp.setNpdisplay("N");
                                                         }
                                                    }
                                                    
                                                    //PRY - 0890 JBALCAZAR
                                                    if(!isProrrateo){
                                                    	if("txtMontoProrrateo".equals(objnpObjHeaderSpecgrp.getNphtmlname())){
                                                        		 valueVisible = "style='display:none'";
                                                        		 objnpObjHeaderSpecgrp.setNpdisplay("N");
                                                             }                                                    	
                                                    }
                                           }                                                
                                          //System.out.println("" + objnpObjHeaderSpecgrp.getNphtmlname()+ " --> " + valueVisible);
                                          %>
                                          <td id="Cabecera<%=objnpObjHeaderSpecgrp.getNpobjitemheaderid()%>" align="center" <%=valueVisible%> class="CellLabel">&nbsp;<%=objnpObjHeaderSpecgrp.getNpobjitemname()%>&nbsp;</td>
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
                                <div style="WIDTH: 65%">
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
                                                    <INPUT type="radio" id="textApportionmentN"  value="N" name="textApportionment"  onclick ="comprobarProrrateo(this.value);">No</td>
													<INPUT type="hidden" id="hdnApportionment" name="hdnApportionment"/>
													<INPUT type="hidden" id="hdnIsApportionment" name="hdnIsApportionment" value ="<%=isProrrateo%>"/>
													<INPUT type="hidden" id="hdnIsClientPostPago" name="hdnIsClientPostPago" value=""/>
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
                                                                    <input type="text"  id="txtTotalPriceApportionment" name="txtTotalPriceApportionment" value="" size="8" style="text-align:right;" readOnly >
                                                                </td>

                                                            </tr>
                                                        </tbody>
                                                    </table>
                                                </td>
                                                <td width="10%" align="rigth"></td>
                                                <td width="40%" class="CellContent" align="center">
                                                    <input id="txtOrderProrrateo" name="txtOrderProrrateo" style="TEXT-ALIGN: right" readonly size="25">
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
                                                    <table width="65%" class="RegionBorder" border="0" cellspacing="0" cellpadding="0">
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
  var vctItemsMainFrameOrder  = new Vector();
  var vctItemsMainImei        = new Vector();
  var vctItemHeaderOrder      = new Vector();
  var vctParametersOrder      = new Vector();
  var vIdModelos              = new Vector();
    
  var wn_flag_sales = 0;   // flag que se activa para obtener la lista de vendedores una vez por busqueda      
  var deleteItemEnabled = true;  //permite eliminar un item, si es false no.
  var promptWindow;
  
  <%if(isProrrateo){%>
  document.getElementById("divProrrateo").style.display = 'block';
  document.getElementById("divBtnProrrateo").style.display = 'block';
  document.getElementById("textApportionmentS").checked = true;
  document.getElementById("hdnApportionment").value="S";
  $("#tabLeyendaTotal  tr td:nth-child(1)").width('47%');
  $("#tabLeyendaTotal  tr td:nth-child(2)").width('44%');
  $("#tabLeyendaTotal  tr td:nth-child(3)").width('9%');
    
  <%}%>  
  
    //DERAZO REQ-0428 Variable global para Pago de Penalidad
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

   function fxSearchVectorElement(vector, element){
      for (i =0; i < vector.size() ; i++){
        var objVector = vector.elementAt(i);
        if (objVector.objectDesc == element)
          return i;
      }
      return -1;
   }
  
  function fxPreChangeItemNewDetailOrder(object, strFilter){
    var form = document.frmdatos;
    var numRespPago=0;
     
    //var bCheckResponsable = <%=MiUtil.contentInArray(MiUtil.parseInt(hdnSpecification), Constante.SPEC_CHECK_RESP_PAGO)%>
    var respPagoSelected="";

    try{
       /*JPEREZ: Se envía el valor seleccionado como Lugar de Despacho*/
      if ( (form.cmbLugarAtencion!=null) && (form.cmbLugarAtencion.value!="")  ){
        var dispatchPlace = form.cmbLugarAtencion[form.cmbLugarAtencion.selectedIndex].value;
        var position = fxSearchVectorElement(object, "strDispatchPlace");
        if ( position == -1  )
          object.addElement( new fxMakeOrderObejct( "strDispatchPlace",dispatchPlace) );
        else
          object.setElementAt( new fxMakeOrderObejct( "strDispatchPlace",dispatchPlace), position );
      }
      
      /*Agregado por RMARTINEZ: 02-09-2009 para validar el ingreso necesario para la fecha de suspension
        para la validación de los dias de suspension para los telefenos a suspender*/        
      <%
      if (MiUtil.parseInt(hdnSpecification) == Constante.SPEC_SUSPENSIONES[0]){ %>        
        if (!fxValidacionesSuspensionTemporal()) return;                 
      <%}%>
      
    }catch(e){}

    // MMONTOYA [ADT-RCT-092 Roaming con corte]
    if (!validateProcessDate(<%=MiUtil.parseInt(hdnSpecification)%>)) {
        return;
    }

    if (fxValidateSelectionRPag('newitems')==false){
      if( strFilter == 'ADDIMEI' )
        fxChangeItemNewDetailOrderByLoadMasiveIMEIS(object);
      else 
        fxChangeItemNewDetailOrder(object);
    }
    else{
      return;
    }
  }
  
  function fxChangeItemNewDetailOrderByLoadMasiveIMEIS(vctrOrden) {
     var form = document.frmdatos;
     var producttype;
     var handsetallowed;
     var mainobjecttype;
     var producthandling;
     var rateplanhandling;
     var additionalservice;
     var consignmentallowed;
     var pricetype;
     var servicetype;
     var additionalobjecttype;
     var mainobjectvalidation;
     var additionalobjectvalid;
     var flagaddenda;
  
     var bUnknwnSiteFlg = null;
     try {
        bUnknwnSiteFlg = form.chkUnkwnSite.checked;
     }catch(e) {
        bUnknwnSiteFlg = false;
     }
  
     // Si hay Sites, e sobligatorio seleccionar
     if (form.hdnClientSitesFlag.value == "1") {
        if (form.cmbSite.value == ""  && form.cmbSite.length > 1 ) {
           if (!bUnknwnSiteFlg) {
              alert("Debe seleccionar un Site");
              form.cmbSite.focus();
              return;
           }
        }
     }
     
     if (form.cmbDivision.value == ""){
        alert("Debe seleccionar una División");
        form.cmbDivision.focus();
        return;
     };
  
     if (form.cmbCategoria.value == ""){
        alert("Debe seleccionar una Categoria");
        form.cmbCategoria.focus();
        return;
     };
  
     if (form.cmbSubCategoria.value == ""){
        alert("Debe seleccionar una Sub Categoria");
        form.cmbSubCategoria.focus();
        return;
     };
    
    try{
       if ( (form.cmbLugarAtencion != null) && (form.cmbLugarAtencion.value == "") ){          
          alert("Debe seleccionar un Lugar de Despacho");
          form.cmbLugarAtencion.focus();
          return;
       }else if ( (form.cmbTienda != null) && (form.cmbTienda.value == "") ){          
            alert("Debe seleccionar una tienda");
            form.cmbTienda.focus();
            return;
       }
     }catch(e){}
             
     if (form.cmbSubCategoria.value != "") {
        var subjObj = null;
        var urlPage = "?";
        for( d = 0 ; d < vctrOrden.size(); d++ ){
         var objVector = vctrOrden.elementAt(d);
          urlPage = urlPage + objVector.objectDesc + "=" + objVector.objectValue + "&";
        }
        
        var frameUrl = "PopUpOrderLoadIMEI.jsp"+urlPage+"type_window=NEW&objTypeEvent=NEW";
        
        var winUrl = appContext+"/DYNAMICSECTION/DynamicSectionItems/DynamicSectionItemsPage/PopUpOrderFrame.jsp?av_url="+escape(frameUrl);
        //RDELOSREYES - 20/08/2008 - Redimension del Popup - Incidencia #5112
        var popupWin = window.open(winUrl, "Orden_Item_Load_IMEI","status=yes, location=0, width=550, height=600, left=300, top=30, screenX=50, screenY=100");
        //var popupWin = window.open(winUrl, "Orden_Item","status=yes, location=0, width=450, height=540, left=300, top=30, screenX=50, screenY=100");
     };
  }
  
   
	function fxSelectResposablePago(opcion){    
   //Pendiente optimizar - se repite en NewOnDisplaySectionItems
		var form = document.frmdatos;
		<%	String strOrigenOrden="";
			String strCampo1="";
			if (strGeneratorType.equals(Constante.GENERATOR_TYPE_OPP)){
		%>	
				var sOrigenOrden  = "<%=Constante.GENERATOR_TYPE_OPP%>";
				var sCampo1			= "<%=strGeneratorId%>";
		<%	}
			else{
				if (strGeneratorType.equals(Constante.GENERATOR_TYPE_INC)){
		%>	
					var sOrigenOrden  = "<%=Constante.GENERATOR_TYPE_INC%>";
		<%		}
				else{
		%>		
					var sOrigenOrden  = "ORD"; //opcion Ordenes CREAR
		<%		}
		%>		
				var sCampo1			= form.hdnNumeroOrder.value;				
		<% }		
		%>
		
		var url="category="+"<%=hdnSpecification%>"+"&origen="+sOrigenOrden+"&campo1="+sCampo1+"&opcion="+opcion+"&strCustomerId=<%=strCustomerId%>";
		url = url + "&strTypeCompany=<%=strTypeCompany%>";
		var iPermitir = fxRequestXML("generalServlet","requestRespPago",url);
      if (iPermitir ==null )
         iPermitir=-1;
      return iPermitir;		
	}   
    
  
  function fxSectionNameValidate(){
    if( fxValidNumRowsItem() )
    if( items_table.rows.length < 2 ){
    alert("Debe agregar al menos un Item");
    return false;
    //<!-- jsalazar - modif hpptt # 1 - 25/05/2011 -inicio-->
    //se agrego validacion para suscripciones
    }else{ 
     <% if(MiUtil.parseInt(hdnSpecification) == Constante.SPEC_SSAA_RENOV_BCKT_SCP || MiUtil.parseInt(hdnSpecification) == Constante.SPEC_SSAA_RENOV_BCKT_CCP){%>
         if(form.txtFechaProceso.value==""){
            var fecha = new Date();
            var day = "";
            var month = "";
            if (fecha.getDate() <10) {
              day = "0" + fecha.getDate();
            } else {
              day = "" + fecha.getDate();
            }
            if ((fecha.getMonth() +1) <10) {
              month = "0" + (fecha.getMonth() +1);
            } else {
              month = "" + (fecha.getMonth() +1);
            }
            form.txtFechaProceso.value=day + "/" + month + "/" + fecha.getFullYear();
         }
    <%}%> 
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
     
    return true;
  }
  
  function fxLoadHeader(){
  
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
      //System.out.println("strCadena " + strCadena);
   %>
   vctItemHeaderOrder.addElement(new fxMakeOrderHeaderItem(<%=strCadena%>));
   <% }//Fin del For  
   }//Fin del If
   %>
   
   }
   
   function fxLoadParameters(){
   <%
   if ( hshtinputNewSection != null ) {
    Enumeration objName  = hshtinputNewSection.keys();
    Enumeration objValue = hshtinputNewSection.elements();

    while( objName.hasMoreElements() || objValue.hasMoreElements() ){
    String nameObject  = objName.nextElement().toString();
    String valueObject = objValue.nextElement().toString();
   %>
    vctParametersOrder.addElement( new fxMakeOrderObejct( '<%=nameObject%>','<%=valueObject%>') );
    <%}}%>  
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
  
   </script>
  
 <script defer>
 
   //Invocación de Pop-up Excepciones
   function fxException(){
      var specid = "<%=hdnSpecification%>";
      fxSetException("<%=strSessionId%>", specid);      
   } 
 
   function fxDeleteItemNew(CellObject,item_id,item_current){
   var form = document.frmdatos;
   var item_pedido_numero = CellObject.parentNode.parentNode.parentNode.rowIndex;
  
   //alert("item_pedido_numero : " + item_pedido_numero)
   var item_promo_header_id = CellObject.parentNode.parentNode.childNodes.item(3).getAttribute("value");

      if (confirm("Desea eliminar el item?")){
         if (deleteItemEnabled){
            var table = document.all ? document.all["items_table"]:document.getElementById("items_table");
            
            try{
              //Volumen de Orden
              if(aplicoVO != "0"){
                form.txtTotalVOSalesPrice.value = "";
                //Si no quedará ningun item
                if(table.rows <= 2){
                  document.getElementById("btnEvaluarVO").disabled = true;
                }
                else{
                  aplicoVO = "2";
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
              }
              }
            }catch(e){}
            
            wb_existItem=(wn_items>=1)?false:wb_existItem;
            wb_existItem=(wn_items>2)?true:false;
            
            wn_items=(table.rows.length-1);
            //alert("Cantidad de Items : " + wn_items + " item_id : " + item_id)
            
            table.deleteRow(CellObject.parentNode.parentNode.parentNode.rowIndex);
            try{
            fxDeleteItemImeis(CellObject.parentNode.parentNode.parentNode.rowIndex);
            }catch(e){}
            
            vctItemsMainFrameOrder.removeElementAt(item_id);
              
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

               	$("input[name='txtTotalPriceApportionment']").each(function () {
             		$(this).val("");
                 });

               	$("input[name='txtTotalPrice']").each(function () {
             		$(this).val("");
                 });
               	
              }catch(e){}            	              
             
         }else {
            alert("No puede eliminar, cierre antes la ventana de edición de Items");
         };
         
      }
   
  }
  
  
  function fxValidNumRowsItem(){
    if (parseInt(document.frmdatos.hdn_validitem.value) == 1 ){
      return true;               
    }
    else
      return false;
  }
  
  /*
  Se cambiará la lógica para cargar IMEIS, 
  ya que pueden haber productos KIT
  */
  function fxLoadImeis(groupId){
    form = document.frmdatos;
    //alert("Entramos " + fxLoadImeis + " GroupID " + groupId)
    var cntItems          = items_table.rows.length;
    var intQuantity       = 0;
    var intProductId      = 0;
    var strModality       = "";
    var intModalityID     = "";
    var strPlanName       = "";
    //var strProductName    = "";
    var intProductLineId  = 0;
    var strFlgProductGN = "";
    var strPhoneNumber = ""; //Reserva de Numeros Golden - RHUACANI-ASISTP - 24/10/2010
    //alert("cntItems : " + cntItems);
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
    
    //alert("Antes de deshabilitar el botón")
    form.btnSaveOrder.disabled = true;
    
    //Verificar que tipo de modalidadId es
    if( strModality == "Venta" || strModality == "Prestamo" || strModality == "Asignacion" ) 
      intModalityID = 20;
    else if( strModality == "Alquiler" ) 
      intModalityID = 10;
    //alert("A punto de generar los IMEIS");   
    //strPlanName = myTrim(strPlanName);    
    strPlanName = strPlanName.replace(/^\s*|\s*$/g,"");
    //Se agregó el parametro strPhoneNumber - RHUACANI-ASISTP - 24/10/2010
    var a = appContext+"/itemServlet?hdnMethod=doGetImeisByProduct&strProductId="+intProductId+"&intModalityID="+intModalityID+"&strModality="+strModality+"&strQuantity="+intQuantity+"&strGroupId="+groupId+"&strPlanName='"+strPlanName+"'&strProductName=&intProductLineId="+intProductLineId+"&strFlgProductGN="+strFlgProductGN+"&strSalesStuctOrigenId=<%=strSalesStuctOrigenId%>"+"&strPhoneNumber="+strPhoneNumber;
    //alert("Desde NewOnDisplaySectionItems.jsp=== " + a);
    form.action = a;
    form.submit();
    
  }
  
  function fxLoadContractByAddress(){
    
  }
  
  /*Se Modifica Para la Carga de Servicios a nivel de cabecera <ITEM_NEW>*/
  function fxCargaServiciosItemItem(objTable){   
   var form = document.frmdatos;
   var strCategoryId = '<%=MiUtil.parseInt(hdnSpecification)%>';
   var strDivisionId = '<%=MiUtil.parseInt(strDivision)%>';
   var strCustomerId = '<%=MiUtil.parseInt(strCustomerId)%>';
   var lorderId      = <%=lOrderId%>;
   var servicios_item_send = "";
   var phonenumber = "";
   var count_items     = getNumRows("items_table");
   var oindexRow       = objTable;
   var currentIndex    = oindexRow - 1;
	if( strCategoryId == <%=Constante.SPEC_TRASLADO%> && (strDivisionId==<%=Constante.KN_BANDA_ANCHA%>) ){
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
     //alert("servicios_item_send: " + servicios_item_send);
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
    //Mandar la orden los numeros telefonicos
	   var a=appContext+"/serviceservlet?myaction=loadServiceItems&servicios_item="+servicios_item_send+"&hdnSpecification=<%=hdnSpecification%>&cmb_ItemPlanTarifario="+strPlanId+"&cmb_ItemSolution="+strSolutionId+"&phonenumber="+phonenumber+"&lorderId="+lorderId+"";     //"&lorderId="+lorderId+
     form.action = a;
     
     form.submit();
   }catch(e){
      alert("Para esta categoría no se aplican Servicios Adicionales");
   }
   }
  }
  
  //Invoca al PopUp para la creación de un Item
   function fxAddRowOrderItemsGlobal(vctItemOrder){
    fxaddElementsItemsMain(vctItemOrder);
    form = parent.mainFrame.document.frmdatos;
    var index_arg = 1;
    var elemText = "";
    var cantElement;
    var valFlag = false;
    var valFlagNothing = false;
    var valuevisible = "";
    var descvisible = "";
    var typeControl = "";
    var cell;
    var contentHidden = "";
    var styleDisplay = "style='text-align:right'";
    var elementCurrent = vctItemsMainFrameOrder.size();
    
    try{
      //Volumen de Orden
      //document.getElementById("btnEvaluarVO").disabled = false;
      if (form.chkVepFlag != undefined){
         if (form.chkVepFlag.checked){
            document.getElementById("btnEvaluarVO").disabled = true;
            //document.getElementById("btnEvaluarVO").focus();
         }else{
      document.getElementById("btnEvaluarVO").disabled = false;
         }
      }else{
         document.getElementById("btnEvaluarVO").disabled = false;
      }
      
      if(aplicoVO != "0"){
        form.txtTotalVOSalesPrice.value = "";
        aplicoVO = "2";
        //alert("Volver a Evaluar Promocion por Volumen de Orden");
        //document.getElementById("btnEvaluarVO").focus();
      }
    }catch(e){}
    
    cantElement = vctItemOrder.size();
    
        if (cantElement > 1) {/* los argumentos pasados.. a partir del segundo. */
          
          var row   = items_table.insertRow(-1);
          
          elemText =  "";
          
          var new_grupo = parseInt(form.hdn_item_imei_grupo.value) + parseInt(1);
          form.hdn_item_imei_grupo.value = new_grupo;
                
          /*Agrego la primera línea*/
          var cellPrinc = row.insertCell(index_arg - 1);
          elemText      = "<div id=\'contTable\' align='center' class='CellContent' >"+
                          "<input type=\'radio\' name=\'item_chek\' onclick=\'javascript:fxCargaServiciosItemItem(fxGetRowIndex(this));\'>"+
                          "<a href=\'javascript:;\' onclick=\'javascript:fxChangeItemEditDetailOrderNew(fxGetRowIndex(this),parent.mainFrame.vctItemsMainFrameOrder,parent.mainFrame.vctParametersOrder);\' ALT=\'Editar Item\'><font color=\'brown\' size=\'2\' face=\'Arial\' ><b>"+ form.hdn_item_imei_grupo.value +"</b></font></a> "+
                          "<a href=\'javascript:;\' onclick=\'javascript:fxAssignmentBillingAcount(fxGetRowIndex(this),parent.mainFrame.vctItemsMainFrameOrder,parent.mainFrame.vctParametersOrder);\' ALT=\'Editar Item\'>Billing</a> "+
                          "<input type=\'hidden\' name='hdnIndice' value="+ document.frmdatos.hdn_item_imei_grupo.value +" >" +
                          "<input type=\'hidden\' name='hdnItemId' >" +
                          "</div>";
                         
          cellPrinc.innerHTML = elemText;
          
          index_arg++;
          
          elemText = "";
         
          for( i = 0; i < vctItemHeaderOrder.size(); i++ ){

            var objItemHeader = vctItemHeaderOrder.elementAt(i);
            var valFlag = false;
            //alert("i : " + i + " -> " + objItemHeader.npobjitemheaderid + " -> " + objItemHeader.nphtmlname );
            
            //Siempre mostrar Text y si coinciden asignar el Valor
              for( j = 0; j < vctItemOrder.size(); j++ ){

              var objItem = vctItemOrder.elementAt(j);

              
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
                    if(aplicoVO == "0"){
                      cell.style.display = "none";
                    }                    
                    index_arg++;
                    break;
                  }
                  if(objItemHeader.npobjitemheaderid == 127){
                    cell = row.insertCell(index_arg - 1);
                    cell.innerHTML = "<div align='center'class='CellContent'><input type='checkbox' name='"+ trim(objItemHeader.nphtmlname) + document.frmdatos.hdn_item_imei_grupo.value+ "' onclick='javascript: fxCalculateTotalVO();' disabled></div>";
                    cell.id = "Celda"+objItemHeader.npobjitemheaderid+document.frmdatos.hdn_item_imei_grupo.value;
                    if(aplicoVO == "0"){
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
                    if(objItemHeader.npobjitemheaderid==125){
                      objItemHeader.npdisplay = "N";
                    }

                    if ( objItemHeader.npdisplay == "N"     ) {
                        contentHidden += "<input type='hidden' name='hdnItemHeaderId' value='"+objItemHeader.npobjitemheaderid+"' >";
                        contentHidden += "<input type='hidden' name='hdnItemValue"+trim(objItemHeader.nphtmlname)+"' "+valuevisible+" >";
                        contentHidden += "<input type='hidden' name='"+ trim(objItemHeader.nphtmlname) +"' " + descvisible + " > ";
                        //alert("contentHidden : " + contentHidden );
                    }else{
                    
                    <%
                        if( ( MiUtil.parseInt(hdnSpecification)  == Constante.SPEC_POSTPAGO_VENTA ||
                             MiUtil.parseInt(hdnSpecification)   == Constante.SPEC_PREPAGO_NUEVA  ||
                             MiUtil.parseInt(hdnSpecification)   == Constante.SPEC_PREPAGO_TDE    ||
                             MiUtil.parseInt(hdnSpecification)   == Constante.SPEC_REPOSICION_PREPAGO_TDE)
                          ) { %>                          
                         if(objItemHeader.npobjitemheaderid == 96){
                            continue;
                         }
                         
                    <%}%>                    
                    
                        //alert("HTML : " +objItemHeader.npobjitemheaderid + " index_arg " + index_arg)
                      if(objItemHeader.npobjitemheaderid!=125){
                        cell = row.insertCell(index_arg - 1);
                        elemText += "<div id='"+ objItemHeader.nphtmlname +"'  align='center' class='CellContent' > ";
                        elemText += "<input type='hidden' name='hdnItemHeaderId' value='"+objItemHeader.npobjitemheaderid+"' >";
                        elemText += "<input type='hidden' name='hdnItemValue"+trim(objItemHeader.nphtmlname)+"' "+valuevisible+" >";
                        elemText += "<input type='text'   name='"+ trim(objItemHeader.nphtmlname) +"' ";
                        elemText += descvisible;
                        if  ( (objItemHeader.npobjitemheaderid==2) || (objItemHeader.npobjitemheaderid==5) || (objItemHeader.npobjitemheaderid==4) ){
                           elemText += " size='13' style='text-align:right' readOnly></div>";
                        } else if (objItemHeader.npobjitemheaderid==139){ // MMONTOYA [ADT-RCT-092 Roaming con corte]
                            elemText += " size='35' style='text-align:right' readOnly></div>";
                        }else{
                           if( (objItemHeader.npobjitemheaderid==10) || (objItemHeader.npobjitemheaderid==16) )
                              elemText += " size='22' style='text-align:right' readOnly></div>";
                           else{
                              if( (objItemHeader.npobjitemheaderid==3) || (objItemHeader.npobjitemheaderid==11) )
                                 elemText += " size='10' style='text-align:right' readOnly></div>";
                                        else{
                                            if(objItemHeader.npobjitemheaderid==51){
                                                <%if(MiUtil.parseInt(hdnSpecification) == Constante.SPEC_BOLSA_CREAR || MiUtil.parseInt(hdnSpecification) == Constante.SPEC_BOLSA_UPGRADE || MiUtil.parseInt(hdnSpecification) == Constante.SPEC_BOLSA_DOWNGRADE || MiUtil.parseInt(hdnSpecification) == Constante.SPEC_BOLSA_DESACTIVAR ){%> // LHUAPAYA  [ADT-BCL-083]
                                                elemText += " size='35' style='text-align:right' readOnly></div>";
                                                <%}else{%>
                                 elemText += " size='12' style='text-align:right' readOnly></div>";
                                                <%}%>
                                            }else {
                                                elemText += " size='12' style='text-align:right' readOnly></div>";
                           }  
                        }
                                    }
                                }
                        //alert("elemText : " + elemText );
                        cell.innerHTML = elemText;
                        elemText =  "";
                        index_arg++;
                      }
                    }
                    valFlag = true;
                    break;
                   
                }//Fin del If
               
             }//Fin del For
              if(!valFlag){
               //alert("[i][" + i + "] -> objItemHeader " + objItemHeader.npobjitemheaderid  + " --> " + objItemHeader.nphtmlname);  
               contentHidden += "<input type='hidden' name='hdnItemHeaderId' value='"+objItemHeader.npobjitemheaderid+"' >";
               contentHidden += "<input type='hidden' name='hdnItemValue"+trim(objItemHeader.nphtmlname)+"'  >";
               contentHidden += "<input type='hidden' name='"+ trim(objItemHeader.nphtmlname) +"' > ";         
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
                                                 "<input type='hidden' name='item_ptnId'>" + //Reserva de Numeros FPICOY 17/01/2011
                                                 
            
                        "<div id='eliminar' align='center' class='CellContent' >" +
                        "<a href='javascript:fVoid()' onclick='javascript:fxDeleteItemNew(this,this.parentNode.parentNode.parentNode.rowIndex,(" + (form.hdn_item_imei_grupo.value) + "));'  ><img src='<%=Constante.PATH_APPORDER_SERVER%>/images/Eliminar.gif' border='no' ALT='Borrar Item'></a>"+
                        "</div>";
            //alert(""+contentHidden)            
            cellDelete.innerHTML = elemText+contentHidden;
            
       }//Fin del If CantElements
       
        wn_items = (items_table.rows.length -1); // numero de items 
         
          if ( wn_items > 1 ){
              wb_existItem =true;
          }
          
    fxCalculateTotal();
    
    /*
    Se debería de considerar un flag para 
    que entre a la sección de IMEI's
    */
    <%if( flgIMEIS ){%>
    fxLoadImeis(form.hdn_item_imei_grupo.value);
    <%}%>
  
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
    var strPhoneNumber  = "";//Reserva de Numeros Golden - RHUACANI-ASISTP - 24/10/2010
    
    try{
      //Volumen de Orden
      if(aplicoVO != "0"){
        form.txtTotalVOSalesPrice.value = "";
        aplicoVO = "2";
        //alert("Volver a Evaluar Promocion por Volumen de Orden");
      }

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

    }catch(e){}
    
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
        //Obtenemos la cadena de telefonos - RHUACANI-ASISTP - 24/10/2010
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
    
    }catch(e){
    alert("" +e.message)
    }
    
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
    /*
    if( isChangeGuide == 1 ){
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
    
    //}catch(e){alert("" +e.message)}
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

  //Se agregó el parametro strPhoneNumber - RHUACANI-ASISTP - 24/10/2010
  function fxRecalculateItemImeis(itemId,groupid,tableID,quantCurrent,quantNew,strModality,strProductId,strPlanName,strProductName,strProductLineId,strFlgProductGN,strPhoneNumber){
    var table    = document.all ? document.all[tableID]:document.getElementById(tableID);
    var indMayor = 0;
    var indMenor = 0;
    var dif      = 0;
    
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
       
       //Se deben de agregar IMEIS
       //Se agregó el parametro strPhoneNumber - RHUACANI-ASISTP - 24/10/2010
       fxAddImei(itemId,groupid,dif,indMayor,indMenor,quantNew,quantCurrent,strModality,strProductId,strPlanName,strProductName,strProductLineId,strFlgProductGN,strPhoneNumber);
       //}
       //Fin de agregar IMEIS
    //}
    
  }//fxRecalculateItemImeis
  
  /*Developer : Lee Rosales
    Motivo    : Agrega IMEIS a la tabla table_imeis al haberse
    modificado la cantidad de de productos que se agregaron 
    incialmente
    Modificacion: Se agregó el parametro strPhoneNumber - RHUACANI-ASISTP - 24/10/2010
  */
  function fxAddImei(itemid,groupId,dif,indMayor,indMenor,intQuantity,qCurrent,strModality,strProductId,strPlanName,strProductName,strProductLineId,strFlgProductGN,strPhoneNumber){
    form = document.frmdatos;
    var cntItems       = items_table.rows.length;
    var intModalityID  = "";
    
    if( strModality == "Propio" || strModality == "Alquiler en Cliente" ){
      fxDeleteImeiTable(groupId);
      return;
    }
    
    //Verificar que tipo de modalidadId es
    if( strModality == "Venta" || strModality == "Prestamo" || strModality == "Asignacion" ) 
      intModalityID = 20;
    else if( strModality == "Alquiler" ) 
      intModalityID = 10;
    
    form.btnSaveOrder.disabled = true;
    
    //Se agregó el parametro strPhoneNumber - RHUACANI-ASISTP - 24/10/2010
    var a = appContext+"/itemServlet?hdnMethod=doGetImeisByProductEdit&strProductId="+strProductId+"&intModalityID="+intModalityID+"&strModality="+strModality+"&strQuantity="+intQuantity+"&strGroupId="+groupId+"&strPlanName='"+strPlanName+"'&strProductName=&strIndMax="+indMayor+"&strIndMin="+indMenor+"&strDiference="+dif+"&intQuantityCurrent="+qCurrent+"&intProductLineId="+strProductLineId+"&strFlgProductGN="+strFlgProductGN+"&strSalesStuctOrigenId=<%=strSalesStuctOrigenId%>"+"&strPhoneNumber="+strPhoneNumber;
    form.action = a;
    form.submit();
  }//fxAddImei
  
  function fxCalculateTotal(){
    var form = parent.mainFrame.document.frmdatos;
    var totalPriceVEP = "0.0";
    var totalVEP; 
    var totalPrice = "0.0";
    var total; 
       
    if( (items_table.rows.length -1) == 1 ){
      try{
        if(form.txtitemTotal.value == undefined){
          form.txtitemTotal[0].value = parseFloat(round_decimals(form.txtitemTotal[0].value ,2));
          totalPrice = form.txtitemTotal[0].value;
        }else{
          form.txtitemTotal.value = parseFloat(round_decimals(form.txtitemTotal.value ,2));
          totalPrice = form.txtitemTotal.value;
        }
        total= parseFloat(totalPrice);
        totalPrice= parseFloat(round_decimals(total,2));        
        
      }catch(e){
        totalPrice = "0.0";
      }
      try{
          if(form.txtitemTotalVEP.value == undefined){
            form.txtitemTotalVEP[0].value = parseFloat(round_decimals(form.txtitemTotalVEP[0].value ,2));
            totalPriceVEP = form.txtitemTotalVEP[0].value;
          }else{
            form.txtitemTotalVEP.value = parseFloat(round_decimals(form.txtitemTotalVEP.value ,2));
            totalPriceVEP = form.txtitemTotalVEP.value;
          }
          totalVEP= parseFloat(totalPriceVEP);
          totalPriceVEP= parseFloat(round_decimals(totalVEP,2));        
        }catch(e){
          totalPriceVEP = "0.0";
        }      
	  
    }else{
      try{
			for(k=0; k<form.txtitemTotal.length; k++){
				form.txtitemTotal[k].value = parseFloat(round_decimals(form.txtitemTotal[k].value,2));
				totalPrice = parseFloat(totalPrice) + parseFloat(form.txtitemTotal[k].value);
				total= parseFloat(totalPrice);
				totalPrice= parseFloat(round_decimals(total,2));
			}
      }catch(e){
        totalPrice = "0.0";
      }
	  
      try{
        for(k=0; k<form.txtitemTotalVEP.length; k++){
          form.txtitemTotalVEP[k].value = parseFloat(round_decimals(form.txtitemTotalVEP[k].value,2));
          totalPriceVEP = parseFloat(totalPriceVEP) + parseFloat(form.txtitemTotalVEP[k].value);
          totalVEP= parseFloat(totalPriceVEP);
          totalPriceVEP= parseFloat(round_decimals(totalVEP,2));
        }
        }catch(e){
          totalPriceVEP = "0.0";
    }
	  
    }
    
    form.txtTotalSalesPrice.value = totalPrice;
    //INICIO: PRY-0864 | AMENDEZ
    //form.hdnTotalSalesPriceVEP.value = totalPriceVEP;
    form.hdnTotalSalesPriceVEP.value = totalPrice;
    var totalPriceNewVEP;

      try {
          if(form.txtTotalSalesPrice.value==undefined || form.txtTotalSalesPrice.value=="undefined") {
              if(form.txtitemTotal.value==undefined) {
                  form.txtTotalSalesPrice.value = form.txtitemTotal[0].value;
                  totalPriceNewVEP = form.txtitemTotal[0].value;
              } else {
                  form.txtTotalSalesPrice.value = form.txtitemTotal.value;
                  totalPriceNewVEP = form.txtitemTotal.value;
              }
          }
      } catch(exception) {
          form.txtTotalSalesPrice.value = "0.0";
          totalPriceNewVEP = "0.00";
      }

      try {
          if(form.hdnTotalSalesPriceVEP.value==undefined || form.hdnTotalSalesPriceVEP.value=="undefined") {
              if(form.txtitemTotalVEP.value==undefined) {
                  //form.hdnTotalSalesPriceVEP.value = form.txtitemTotalVEP[0].value;
                  form.hdnTotalSalesPriceVEP.value = totalPriceNewVEP;
              } else {
                  //form.hdnTotalSalesPriceVEP.value = form.txtitemTotalVEP.value;
                  form.hdnTotalSalesPriceVEP.value = totalPriceNewVEP;
              }
          }
      } catch(exception) {
          //form.hdnTotalSalesPriceVEP.value = "0.0";
          form.hdnTotalSalesPriceVEP.value = totalPriceNewVEP;
      }
    //FIN: PRY-0864 | AMENDEZ
	

  }
   
  function fxGetRowIndex(objTable){
    try{
      var index_table = objTable.parentNode.parentNode.parentNode.rowIndex;
    }catch(e){
      var index_table = objTable.parentNode.rowIndex;
    }
    return index_table;
  }
  
   function fxDeleteRowsTableImei(){
     var count = table_imeis.rows.length ;
       for(i=0; i< (count-1); i++){
         table_imeis.deleteRow(1);
       }
   } 
   
   
   /**Inovación a la sección de Asignación de Servicios**/
   function fxAssignmentBillingAcount(rowIndex,vctAuxItemsMain,vctAuxParameters){
    
   var servicios_item_send = "";
   var equipoDesc = "";
   var equipoId   = "";
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
      
      var a=appContext+"/orderservlet?myaction=loadAssignmentBillingAccount&servicios_item="+servicios_item_send;
      a = a + "&txtCompanyId="+form.txtCompanyId.value+"&rowIndex="+rowIndex;
      
      var responsablePagoValue = form.cmbResponsablePago.value;
      var responsablePagoDesc  = form.cmbResponsablePago.options[form.cmbResponsablePago.selectedIndex].text;
      
      a = a + "&responsablePagoValue="+responsablePagoValue;
      a = a + "&responsablePagoDesc="+responsablePagoDesc;
      
      if( vctItemsMainFrameOrder.size() == 1 ){
         a             = a + "&item_billingAccount="+form.item_billingAccount.value;
         equipoDesc    = form.txtItemProduct.value;
         equipoId      = form.hdnItemValuetxtItemProduct.value;
         a             = a + "&equipoDesc="+equipoDesc+"&equipoId="+equipoId;
      }else{
         a = a + "&item_billingAccount="+form.item_billingAccount[rowIndex - 1].value;
         equipoDesc    = form.txtItemProduct[rowIndex - 1].value;
         equipoId      = form.hdnItemValuetxtItemProduct[rowIndex - 1].value;
         a             = a + "&equipoDesc="+equipoDesc+"&equipoId="+equipoId;
      }
      
      /**Envia el número de la orden*/
      a = a + "&strOrderId="+form.hdnNumeroOrder.value;
      
      var winUrl = appContext+"/DYNAMICSECTION/DynamicSectionItems/DynamicSectionItemsPage/PopUpOrderFrame.jsp?av_url="+escape(a);
      var popupWin = window.open(winUrl, "Orden_Item_Assignment","status=yes, location=0, width=800, height=450, left=300, top=30, screenX=50, screenY=100");
    }catch(e){
     alert("Para esta categoría no se requiere configurar Billing Account");
    }      
  }
  
  //Invoca al PopUp para la creación de un Item
  function fxAddRowOrderItemsGlobalBySimPropio(vctItemOrder,intQuantity,arrSIMPropio){
    
    form = parent.mainFrame.document.frmdatos;
    var index_arg = 1;
    var elemText = "";
    var cantElement;
    var valFlag = false;
    var valFlagNothing = false;
    var valuevisible = "";
    var descvisible = "";
    var typeControl = "";
    var cell;
    var contentHidden = "";
    var elementCurrent = vctItemsMainFrameOrder.size();
    
    cantElement = vctItemOrder.size();
    
    var arrVariables = new Array();
    
      if (cantElement > 1) {/* los argumentos pasados.. a partir del segundo. */
      
        /*****for( f=0; f<intQuantity; f++){
          //alert("Entramos -> Fila ("+(f+1)+") ");
          //***fxaddElementsItemsMain(vctItemOrder);***///
          
          //*************var row   = items_table.insertRow(-1);*************************//
          //var new_grupo = parseInt(form.hdn_item_imei_grupo.value) + parseInt(1);
          
          //form.hdn_item_imei_grupo.value = new_grupo;
          
          /*Agrego la primera columna*/
          //*************var cellPrinc = row.insertCell(index_arg - 1);*************************//
          
          elemText      = "";
          elemText      = "<div id=\'contTable\' align='center' class='CellContent' >"+
                          "<input type=\'radio\' name=\'item_chek\' onclick=\'javascript:fxCargaServiciosItemItem(fxGetRowIndex(this));\'>"+
                          "<a href=\'javascript:;\' onclick=\'javascript:fxChangeItemEditDetailOrderNew(fxGetRowIndex(this),parent.mainFrame.vctItemsMainFrameOrder,parent.mainFrame.vctParametersOrder);\' ALT=\'Editar Item\'><font color=\'brown\' size=\'2\' face=\'Arial\' ><b>"+ form.hdn_item_imei_grupo.value +"<"+"/b><"+"/font><"+"/a> "+
                          "<a href=\'javascript:;\' onclick=\'javascript:fxAssignmentBillingAcount(fxGetRowIndex(this),parent.mainFrame.vctItemsMainFrameOrder,parent.mainFrame.vctParametersOrder);\' ALT=\'Editar Item\'>Billing<"+"/a> "+
                          "<input type=\'hidden\' name='hdnIndice' value="+ document.frmdatos.hdn_item_imei_grupo.value +" >" +
                          "<input type=\'hidden\' name='hdnItemId' >" +
                          "<"+"/div>";
          
          /*Ejecuto la primera columna de la fila*/
          //*************cellPrinc.innerHTML = elemText;*************/
          
          /**Asignamos la construcción de la columna de Enlaces y ID's**/
          arrVariables[index_arg] = elemText;
          
          index_arg++;
          elemText = "";
          
          for( i = 0; i < vctItemHeaderOrder.size(); i++ ){
          
            var objItemHeader = vctItemHeaderOrder.elementAt(i);
            var valFlag = false;
            
            //Siempre mostrar Text y si coinciden asignar el Valor
            for( j = 0; j < vctItemOrder.size(); j++ ){
              
              var objItem = vctItemOrder.elementAt(j);
              
              /*
              if( vctItemOrder.elementAt(j).npobjitemheaderid == 2 ){
                vctItemOrder.elementAt(j).npobjitemvalue = arrSIMPropio[f];
                vctItemOrder.elementAt(j).npobjitemvaluedesc = arrSIMPropio[f];
              }*/
              
              if( objItemHeader.npobjitemheaderid == objItem.npobjitemheaderid ) {
                
                    valuevisible = " value='"+ objItem.npobjitemvalue + "' ";
                    descvisible  = " value='"+ objItem.npobjitemvaluedesc + "' ";
                    
                    if ( objItemHeader.npdisplay == "N" ) {
                        contentHidden += "<input type='hidden' name='hdnItemHeaderId' value='"+objItemHeader.npobjitemheaderid+"' >";
                        contentHidden += "<input type='hidden' name='hdnItemValue"+trim(objItemHeader.nphtmlname)+"' "+valuevisible+" >";
                        contentHidden += "<input type='hidden' name='"+ trim(objItemHeader.nphtmlname) +"' " + descvisible + " > ";
                        //alert("contentHidden : " + contentHidden );
                    }else{
                        //alert("HTML : " +objItemHeader.nphtmlname + " index_arg " + index_arg)
                        elemText += "<div id='"+ objItemHeader.nphtmlname +"'  align='center' class='CellContent' > ";
                        elemText += "<input type='hidden' name='hdnItemHeaderId' value='"+objItemHeader.npobjitemheaderid+"' >";
                        elemText += "<input type='hidden' name='hdnItemValue"+trim(objItemHeader.nphtmlname)+"' "+valuevisible+" >";
                        elemText += "<input type='text'   name='"+ trim(objItemHeader.nphtmlname) +"' ";
                        elemText += descvisible;
                        
                        elemText += fxGetCustomizeSize(objItemHeader.npobjitemheaderid);
                        
                        /*Agrego la siguiente columna*/
                        //*************cell = row.insertCell(index_arg - 1);************/
                        /*Ejecuto la primera columna de la fila*/
                        //alert("HTML : " +elemText)
                        //*************cell.innerHTML = elemText;***************/
                        /**Asignamos la construcción de la columna de Enlaces y ID's**/
                        arrVariables[index_arg] = elemText;
                        
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
               contentHidden += "<input type='hidden' name='"+ trim(objItemHeader.nphtmlname) +"' > ";         
             }  
             
          }//Fin del For
            
            /* Boton de Borrar Item */
            elemText  =  fxGetHiddenItem();
            elemText +=  "<div id='eliminar' align='center' class='CellContent' >" +
                         "<a href='javascript:fVoid()' onclick='javascript:fxDeleteItemNew(this,this.parentNode.parentNode.parentNode.rowIndex,(" + (form.hdn_item_imei_grupo.value) + "));'  ><img src='<%=Constante.PATH_APPORDER_SERVER%>/images/Eliminar.gif' border='no' ALT='Borrar Item'><"+"/a>"+
                         "<"+"/div>";
            
            //*************var cellDelete = row.insertCell(index_arg-1);***********/
            //*************cellDelete.innerHTML = elemText+contentHidden;**************/
            
            /**Asignamos la construcción de la columna de Enlaces y ID's**/
            arrVariables[index_arg] = elemText+contentHidden;
            
            index_arg = 1;
         /**********}//Fin For***/
         
       }//Fin del If CantElements
       
       wn_items = (items_table.rows.length -1); // numero de items 
       
       if ( wn_items > 1 ){
         wb_existItem =true;
       }
       
       //alert("Analizando el Vector");
       
       for( f=0; f<intQuantity; f++){
       
       var index_arg_aux = 1;
       var strElemAux    = "";
       var row_aux;
       var cellPrincAux;
       
       var new_grupo_aux = parseInt(document.frmdatos.hdn_item_imei_grupo.value) + parseInt(1);
       var objVctItemOrderAux = null;
       objVctItemOrderAux = vctItemOrder;
       
       for( t = 0; t < objVctItemOrderAux.size(); t++ ){
         var objObjectItemAux = objVctItemOrderAux.elementAt(t);
         
         if( objVctItemOrderAux.elementAt(t).npobjitemheaderid == 2 ){
           objVctItemOrderAux.elementAt(t).npobjitemvalue = arrSIMPropio[f];
           objVctItemOrderAux.elementAt(t).npobjitemvaluedesc = arrSIMPropio[f];
         }
              
       }
       
       fxaddElementsItemsMain(objVctItemOrderAux);
       
       form.hdn_item_imei_grupo.value = new_grupo_aux;
       
         for(u=1; u<arrVariables.length; u++){
           //alert("Analizando el Vector : " + arrVariables[u]);
          
           //Agregar la primera fila y primera columna
           if( u == 1 ){
             row_aux          = items_table.insertRow(-1);
             cellPrincAux     = row_aux.insertCell(index_arg_aux - 1);
             
             strElemAux      = "";
             //strElemAux      = arrVariables[u];
             strElemAux      = "<div id=\'contTable\' align='center' class='CellContent' >"+
                               "<input type=\'radio\' name=\'item_chek\' onclick=\'javascript:fxCargaServiciosItemItem(fxGetRowIndex(this));\'>"+
                               "<a href=\'javascript:;\' onclick=\'javascript:fxChangeItemEditDetailOrderNew(fxGetRowIndex(this),parent.mainFrame.vctItemsMainFrameOrder,parent.mainFrame.vctParametersOrder);\' ALT=\'Editar Item\'><font color=\'brown\' size=\'2\' face=\'Arial\' ><b>"+ form.hdn_item_imei_grupo.value +"<"+"/b><"+"/font><"+"/a> "+
                               "<a href=\'javascript:;\' onclick=\'javascript:fxAssignmentBillingAcount(fxGetRowIndex(this),parent.mainFrame.vctItemsMainFrameOrder,parent.mainFrame.vctParametersOrder);\' ALT=\'Editar Item\'>Billing<"+"/a> "+
                               "<input type=\'hidden\' name='hdnIndice' value="+ document.frmdatos.hdn_item_imei_grupo.value +" >" +
                               "<input type=\'hidden\' name='hdnItemId' >" +
                               "<"+"/div>";
             cellPrincAux.innerHTML = strElemAux;
             index_arg_aux++;
           }else if(u == 3){
             cellPrincAux     = row_aux.insertCell(index_arg_aux - 1);
             //alert(arrVariables[u]);
             strElemAux      = "";
             strElemAux      = "<div id=' txtItemGenericSIMIMEI'  align='center' class='CellContent' >"+
                               "<input type='hidden' name='hdnItemHeaderId' value='2' >"+
                               "<input type='hidden' name='hdnItemValuetxtItemGenericSIMIMEI'  value='"+arrSIMPropio[f]+"'  >"+
                               "<input type='text'   name='txtItemGenericSIMIMEI'  value='"+arrSIMPropio[f]+"'  size='13' style='text-align:right' readOnly><"+"/div>";
             cellPrincAux.innerHTML = strElemAux;
             index_arg_aux++;
           }else{
             cellPrincAux     = row_aux.insertCell(index_arg_aux - 1);
             
             strElemAux      = "";
             strElemAux      = arrVariables[u];
             cellPrincAux.innerHTML = strElemAux;
             index_arg_aux++;
           }
         }
       }//Fin For
       
       fxCalculateTotal();
  }
  
  function fxGetCustomizeSize(intHeaderId){
  var strElementText = "";
    if( (intHeaderId==2) || (intHeaderId==5) || (intHeaderId==4) )
      strElementText = " size='13' style='text-align:right' readOnly><"+"/div>";
    else{
      if( (intHeaderId==10) || (intHeaderId==16) )
        strElementText = " size='22' style='text-align:right' readOnly><"+"/div>";
      else{
        if( (intHeaderId==3) || (intHeaderId==11) )
          strElementText = " size='10' style='text-align:right' readOnly><"+"/div>";
        else 
          strElementText = " size='12' style='text-align:right' readOnly><"+"/div>";
      }  
    }
    return strElementText;
  }
  
  function fxGetHiddenItem(){
  
  var strElementHidden = "<input type='hidden' name='item_adde_period' value=''>" +
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
                         
                         "<input type='hidden' name='item_excepMinAddConexDirecChecked'>" +
                         "<input type='hidden' name='item_excepMinAddInterConexChecked'>";
                         
    return strElementHidden;
  }
  
  function fxSectionItemsOnload(){
     
     vctItemsMainFrameOrder.removeElementAll();
     vctItemsMainImei.removeElementAll();
     vctItemHeaderOrder.removeElementAll();
     vctParametersOrder.removeElementAll();
    
     fxLoadHeader();
     fxLoadParameters();
     fxValidNumRowsItem();
     
     try{
     fxOnLoadImeiService();
     }catch(e){}

   }
   
   function fxValidateFixedPhone(solutionValue){
      var form            = document.frmdatos;
      var varItemSolution = form.hdnItemValuetxtItemSolution;
      
      try{
        if (varItemSolution.length == 'undefined' || varItemSolution.length == undefined){
          if (solutionValue == 6 && varItemSolution.value != 6){
            alert("No se puede ingresar un item con solución Telefonia fija");
            return false;
          }else if (solutionValue != 6 && varItemSolution.value == 6){
            alert("No se puede ingresar un item con solución diferente Telefonia fija");
            return false;
          }
        }else{
          for (i= 0; i<varItemSolution.length;i++){
            if (solutionValue == 6 && varItemSolution[i].value != 6){
              alert("No se puede ingresar un item con solución Telefonia fija");
              return false;
            }else if (solutionValue != 6 && varItemSolution[i].value == 6){
              alert("No se puede ingresar un item con solución diferente Telefonia fija");
              return false;
            }
          }
        }
      }catch(e){return true;}
      return true;
   }
   
  /*Inicio JPEREZ*/
  function myTrim(s){
    var l=0; 
    var r=s.length -1;
    
    while(l < s.length && s[l] == undefined){	
      l++; 
    }
    while(r > l && s[r] == undefined){	
      r-=1;	
    }
    return s.substring(l, r+1);
  }

  /*Fin JPEREZ */
  
  /*Agregado por rmartinez_07-09-2009*/
  function fxValidacionesSuspensionTemporal(){
   /*Carga los valores iniciales para que si la confirmacion de la modificación 
     de la fecha es negativo; entonces retome sus valores iniciales.
   */   
   
   form.hdnFechaProceso.value = form.txtFechaProceso.value;
   if (form.txtFechaReconexion != undefined){
      form.hdnFechaReconexion.value = form.txtFechaReconexion.value;      
   }
   
   //Valida que la suspension tempotral tenga fecha de Proceso.
   if (form.txtFechaProceso.value == ""){
      alert("Debe ingresar la fecha de proceso para esta categoría.");
      return false;
   }
   
   if (form.txtFechaReconexion.value == ""){
      alert("Debe ingresar la fecha de reconexión para esta categoría.");
      return false;
   }         
   
   return true;
  }
  /*Agregado por rmartinez_07-09-2009*/
  
  //Agregado por César Lozza 02/11/2010 
  //Volumen de Orden
  
  //Estados
  //0: No se aplicó evaluación de volumen de orden
  //1: Se aplicó evaluación de volumen de orden
  //2: Se tiene volver a hacer la evaluación de volumen de orden
  var aplicoVO = "0";
  
  function fxEvaluateVO(){
          
    var a = appContext+"/itemServlet?hdnMethod=evaluateOrderVolume&strCustomerId=<%=strCustomerId%>&strSpecificationId=<%=hdnSpecification%>&type_window=<%=Constante.PAGE_ORDER_NEW%>";
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

	    // capturamos los id de los item  
	    $("#items_table tr td div input[name='hdnItemValuetxtItemQuantity']").each(function () {
	    	pv_cantidad_item.push($(this).val());
	    });	    
	    
	    var prorrateo = {};
			    prorrateo.nroDocument= "<%=strDocument%>";
			    prorrateo.typeDocument= "<%=strTypeDocument%>";			    
			    prorrateo.user= "<%=objSessionUser.getLogin()%>";
			    prorrateo.customerId= "<%=strCustomerId%>";
			    prorrateo.siteId= "<%=strnpSite%>";			    
			    prorrateo.orderId = "<%=lOrderId%>";
			    prorrateo.accion ="NEWACTION";
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
	    	obj.cantidadItem = pv_cantidad_item[i];
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
				        	//obtenemos los item y seteamos el monto anticipado
				        	$("#v_savePagoAnticipado").val("1");
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

  function redirectOrder(idorder){
      parent.bottomFrame.location.replace('/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid='+idorder+'&av_execframe=BOTTOMFRAME');
  }
  
 </script>