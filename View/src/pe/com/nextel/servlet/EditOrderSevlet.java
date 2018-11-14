package pe.com.nextel.servlet;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import pe.com.nextel.bean.*;
import pe.com.nextel.dao.GeneralDAO;
import pe.com.nextel.exception.ExceptionBpel;
import pe.com.nextel.form.CommentForm;
import pe.com.nextel.service.*;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class EditOrderSevlet  extends GenericServlet {
    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - INICIO
     *  REALIZADO POR: Carmen Gremios
     *  FECHA: 13/09/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/

   private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
   protected CreditEvaluationService objCreditEvaluationService = new CreditEvaluationService();
   private PopulateCenterService populateCenterService = new PopulateCenterService();
   private DigitalDocumentService digitalDocumentService = new DigitalDocumentService();
   
   public void init(ServletConfig config) throws ServletException {
      super.init(config);
   }
   
   public void doGet(HttpServletRequest request, 
      HttpServletResponse response) throws ServletException, IOException {response.setContentType(CONTENT_TYPE);
      PrintWriter out = response.getWriter();        
      String strMyaction = request.getParameter("myaction");
      System.out.println("llego al servlet-->"+strMyaction);
   
      if ( strMyaction != null ) {
      
         if (strMyaction.equals("updateOrden")){
            doUpdateOrder(request, response,out);
         } 
         else if (strMyaction.equals("ValidateVaucher")) {                            
            doValidateVoucher(request, response);
         }
         else if (strMyaction.equals("ValidateSalesMan")){
            doValidateSalesMan(request, response);                
         }   
         else if (strMyaction.equals("LoadUbigeo")){                
            doLoadUbigeo(request, response); 
         }
         else if (strMyaction.equals("LoadUbigeo2")){                
            doLoadUbigeo2(request, response); 
         }
         else if (strMyaction.equals("InsertSite")){                
            doInsertSite(request, response); 
         }
         else if (strMyaction.equals("UpdateSite")){                
            doUpdateSite(request, response); 
         }            
         else if (strMyaction.equals("DeleteSite")){                
            doDeleteSite(request, response); 
         }
         else if (strMyaction.equals("InsertAddress")){                
            doInsertAddress(request, response); 
         } 
         else if (strMyaction.equals("UpdateAddress")){                
            doUpdateAddress(request, response); 
         } 
         else if (strMyaction.equals("DeleteAddress")){                
            doDeleteAddress(request, response); 
         } 
         else if (strMyaction.equals("InsertContact")){                
            doInsertContact(request, response); 
         } 
         else if (strMyaction.equals("UpdateContact")){                
            doUpdateContact(request, response); 
         }
         else if (strMyaction.equals("DeleteContact")){                
            doDeleteContact(request, response); 
         }     
         else if (strMyaction.equals("InsertBillAcc")){                
            doInsertBillAcount(request, response); 
         }
         else if (strMyaction.equals("UpdateBillAcc")){                
            doUpdateBillAcount(request, response); 
         }
         else if (strMyaction.equals("DeleteBillAcc")){                
            doDeleteBillAccount(request, response); 
         }
         else if (strMyaction.equals("generateDocument")){                
            doGenerateDocument(request, response); 
         }
         else if (strMyaction.equals("autorizacionDevolucion")){                
            doAutorizacionDevolucion(request, response); 
         }           
         else if (strMyaction.equals("generatePayOrder")){                
            doGeneratePayOrder(request, response); 
         }         
         else if (strMyaction.equals("parteIngreso")){                
            doParteIngreso(request, response); 
         }                  
         else if (strMyaction.equals("loadInboxSaltar")){                
            doLoadInboxSaltar(request, response); 
         }
         else if (strMyaction.equals("updateException")){                
            doUpdateException(request, response); 
         } 
         else if (strMyaction.equals("updateOrdenDetail")){                
            doUpdateOrderDetail(request, response); 
         } 
          else if (strMyaction.equals("generateDocumentDetail")){                
            doGenerateDocumentDetail(request, response); 
         }
         else if (strMyaction.equals("replaceHandset")){                
            doReplaceHandset(request, response); 
         }
         else if(strMyaction.equals("evaluarOrden")){
            doEvaluarOrden(request, response);
        }
        else if (strMyaction.equals("validatePhoneNumber")){
           getStatusNumber(request, response);
        }
        else if (strMyaction.equals("validaDiasSuspensionEdit")){
           validaDiasSuspensionEdit(request, response, out);
        }
        else if(strMyaction.equals("ValidationProposedOrder")){
          doValidateProposedOrder(request,response,out);
        }
        else if(strMyaction.equals("ValidationEquipReplacement")){
          doValidateEquipmentReplacement(request,response);
        }
        else if(strMyaction.equals("generaGuia")){
          doGenerateGuiaRemision(request,response);
        }
        //PHIDALGO
        else if(strMyaction.equals("ValidateBudget")){
          doValidateBudget(request,response);
        }
      /*  else if(strMyaction.equals("InsertSalesData")){
          doInsertSalesData(request,response);
        }*/
        //EXTERNO.JNINO
        else if (strMyaction.equals("suspEquipos")){                
          doSuspenderEquipos(request, response); 
        } 
        else if (strMyaction.equals("ValidateIsTiendaExpress")){                
            doValidateIsTiendaExpress(request, response); 
          } //TIENDA EXPRESS
         //JLIMAYMANTA
       else if (strMyaction.equals("ValidateSubcategoria")){                
              doValidateSubcategoria(request, response); 
        }
       else if(strMyaction.equals("ValidateOrderExist")){
              doValidateOrderExist(request,response);
          }
       else if(strMyaction.equals("GetBuildingByOrder")){
              doGetBuildingByOrder(request,response);
          }
       else if(strMyaction.equals("GetValueLimitModel")){
             doGetValueLimitModel(request, response);
      }
       else if(strMyaction.equals("GetValidarDireccionRiesgo")){
             getValidarDireccionRiesgo(request, response);
      }
       else if(strMyaction.equals("GetInsertLogValidateAddress")){
             getInsertLogValidateAddress(request, response);
      } 
       else if(strMyaction.equals("GetValidateNumSolicitud")){
            doValidateNumSolicitud(request, response);
      }        else if(strMyaction.equals("ValidateUltimaPreevaluacion")){ //REQ-0204 EFLORES
             doValidateUltimaPreevaluacion(request, response);
      }
      //INICIO: PRY-0864 | AMENDEZ
      else if(strMyaction.equals("validateOrderVepCI")){
          validateOrderVepCI(request,response);
      }
      //FIN: PRY-0864 | AMENDEZ

      //INICIO: PRY-0980 AMENDEZ
      else if(strMyaction.equals("validatePaymentTermsCI")){
        validatePaymentTermsCI(request,response);
      }
      //FIN: PRY-0980 | AMENDEZ
      //INI PRY-1062 AMATEOM
      else if (strMyaction.equals("validatePreevaluationCDM")) {
          doValidatePreevaluationCDM(request, response);
      }
      //FIN PRY-1062 AMATEOM
      //PRY-1049| INICIO: AMENDEZ
      else if(strMyaction.equals("loadUseAddressInOrder")){
          loadUseAddressInOrder(request,response);
      }else if(strMyaction.equals("validateConfigBafi2300")){
          validateConfigBafi2300(request,response);
      }
      //PRY-1049 | FIN: AMENDEZ
      //PRY-1093 | INICIO: JCURI
      else if (strMyaction.equals("getCarrierPlaceOfficeList")) {
    	  getCarrierPlaceOfficeList(request, response);
      }
      //PRY-1093 | FIN: JCURI
         //INI PRY-1037  KPEREZ
         else if (strMyaction.equals("validateSimManager")) {
             doValidateSimManager(request, response);
         }
          //FIN PRY-1037  KPEREZ
      //INICIO: PRY-1200 | AMENDEZ
      else if(strMyaction.equals("validateSpecificationVep")){
        validateSpecificationVep(request,response);
      }
      //FIN: PRY-1200 | AMENDEZ
        else if(strMyaction.equals("validarNuevoRespPago")){
             validarNuevoRespPago(request,response);
        }
        else if(strMyaction.equals("validarEspecResPago")){
             validarEspecResPago(request, response);
        }
      }
   }
   
   public void doPost(HttpServletRequest request, 
                 HttpServletResponse response) throws ServletException, IOException {response.setContentType(CONTENT_TYPE);
      doGet(request,response);
   }
   
   /**
    * CPUENTE6
   * Motivo: Validar una orden de reposicion en estado ALMACEN_TIENDA
   * <br>Realizado por: <a href="mailto:carlos.puente@nextel.com.pe">CPUENTE</a>
   * <br>Fecha: 19/10/2009 
   * @param     request     
   * @param     response             
   */   
  public String doValidateEquipmentReplacement(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{    
   
    String strMessage = null;  
    long lOrderId=(request.getParameter("hdnOrderId")==null?0:MiUtil.parseLong(request.getParameter("hdnOrderId")));
    EditOrderService objEditOrderService= new EditOrderService();
    
    try{             
      
       strMessage= objEditOrderService.doValidateEquipmentReplacement(lOrderId);
       strMessage= strMessage!= null? strMessage:null;
       //System.out.print("LA PTMR: " + strMessage);
       PrintWriter out = response.getWriter() ;
       out.print( strMessage );
        }catch(Exception e){
           e.printStackTrace();
           strMessage = e.getMessage();
        } 
        return strMessage;
   } 
       
               
  /**
  * Motivo: Actualiza las ordenes
  * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
  * <br>Modificado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
  * <br>Fecha: 08/09/2007 
  * @param     request     
  * @param     response             
  */      
  public void doUpdateOrder(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws ServletException, IOException{
    logger.info("*************** INICIO EditOrderSevlet > doUpdateOrder ***************");
    HashMap hshResult=new HashMap();
    HashMap hashData=new HashMap();
    String strMessage = null;        
    String strOrderId=null;
    String strSaveOption=null;
    //String strOrderPermEdit=null;
    String strMesgAsignado=null; 
    String strInboxId=null;
    String strCurrentStatus=null;
    
    SpecificationBean objSpecificationBean=null;
    String strCustomerId=null;    
	  String strDivisionId=null;
    String strNpBpelconversationid=null;
    String strActionName=null;
    String strNextInboxName=null;
    String strNextInboxNamePA=null;//JCURI    
    String strNextStatus=null;
    String strOldInboxName=null;
    String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
    String fechaFirma = "";
    String strCurrentInbox = "";
    String strActionDescription = "";
    String strCreatedBy = "";
    String strCreditAction = "";
    String strSpecificationId= "";//CPUENTE5
    String noInvokeBPELProrra = null;//JCURI
    HashMap hshValidateBlack  = null;


      logger.info("Ingreso doUpdate -> Usuario entrante PortalSessionId -> "+strSessionId);
    
    int iErrorNumber=-1;
    String strBpelError="0";
    String strLogin = null;
    int lLoginBuilding = 0;
    PortalSessionBean objPortalSesBean = null;
    try{
    
      objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
      
      if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
        throw new Exception("La sesión del usuario ha expirado por favor vuelva a registrarse. Inicie Login");
      
      strLogin = objPortalSesBean.getLogin();
      lLoginBuilding = objPortalSesBean.getBuildingid();  
      
      objHashMap =null;
      objHashMap = getParameterNames(request);
      
      long lOrderId =(request.getParameter("hdnOrderId")==null?0:MiUtil.parseLong(request.getParameter("hdnOrderId")));         
      String strTransportista=request.getParameter("cmbTransportista");
      strCurrentInbox=(request.getParameter("txtEstadoOrden")==null?"":request.getParameter("txtEstadoOrden"));
      strActionDescription=(request.getParameter("cmbAction")==null?"":request.getParameter("cmbAction"));
      strCreatedBy = request.getParameter("hdnSessionLogin");
      NewOrderService objOrderServiceTransaction = new NewOrderService();
      
        //Inicio  : RPASCACIO N_SD000246338

        java.util.Date fechaDataOrder = new java.util.Date();
        logger.info("[LOG_PROCESS_INFO] [UPDATE_ORDER] [N_SD000246338]:  " +
                " \n- Orden: " +lOrderId +
                " \n- Lugar de Despacho : " + request.getParameter("hdnLugarDespacho2") +
                " \n- Forma de Pago : " + request.getParameter("hdnFormaPago2") +
                " \n- Fecha y Hora de Proceso  : " + fechaDataOrder +
                " \n- Nombre del Servlet : EditOrderServlet");

        //Fin : RPASCACIO


      
      if(strActionDescription.equals(Constante.ACTION_INBOX_AVANZAR) && (strCurrentInbox.equals(Constante.INBOX_ADM_VENTAS) || strCurrentInbox.equals(Constante.INBOX_BACKOFFICE) || strCurrentInbox.equals(Constante.INBOX_CALLCENTER) || strCurrentInbox.equals(Constante.INBOX_BAGLOCK))){

          logger.info("strCurrentInbox : " + strCurrentInbox);
          logger.info("strActionName : " + strActionDescription);
         
        
        HashMap objHasvalidate = new HashMap();
        fechaFirma = (String)objHashMap.getParameter("txtFechaHoraFirma");
        objHasvalidate = objOrderServiceTransaction.getValidateFechaFirma(lOrderId,fechaFirma);
        strMessage         =  (String)objHasvalidate.get("strMessage");
        if (strMessage!=null)
          throw new Exception(strMessage);          
      }
      /*
       * JCASAS - Se registra Motivo de Rechazo - 30/06/2009
       * JCASAS - Solo cuando el flag de nuevo proceso de creditos este habilitado - 21/07/2009
       */

   /*   HashMap hshValidateCredit = new HashMap();      
      hshValidateCredit = objCreditEvaluationService.doValidateCredit(lOrderId,"ORDER");
      if((String)hshValidateCredit.get("strMessage")!=null){
        throw new Exception(strMessage);
      }
    
      String strValidateCredit = (String)hshValidateCredit.get("flagValidateCredit");
      if(strValidateCredit.equals("1")) {       
        System.out.println("strActionDescription : " + strActionDescription);
        System.out.println("strCreatedBy : " + strCreatedBy);
        if(strActionDescription.equals(Constante.ACTION_INBOX_AVANZAR) && (strCurrentInbox.equals(Constante.INBOX_TIENDA01)|| strCurrentInbox.equals(Constante.INBOX_VENTAS))) {
          strCreditAction=(request.getParameter("cmbCreditAction")==null?"":request.getParameter("cmbCreditAction"));
          if(strCreditAction.equals("RECHAZAR")) {          
            HashMap hshCreditEvaluation = objCreditEvaluationService.doReasonReject(lOrderId, "ORDER", 1, strCreatedBy);
            strMessage =  (String)hshCreditEvaluation.get("strMessage");
            if (strMessage!=null) throw new Exception(strMessage);            
          }
        }
      }*/
      /*
       * RDELOSREYES - Carga Masiva - 31/10/2008
       */
      HashMap hshDataMap = (HashMap) request.getSession(true).getAttribute(Constante.DATA_STRUCT);
      objHashMap.put(Constante.DATA_STRUCT, hshDataMap);
      String hdnFlagMassive = objHashMap.getParameter("hdnFlagMassive");
      if("N".equalsIgnoreCase(hdnFlagMassive)) {
        objHashMap.put(Constante.DATA_STRUCT, null);
      }
      
      
      /*
       * RDELOSREYES - Carga Masiva - 31/10/2008
       */
       
      //Validación de presupuesto.

       //Inicio Se adiciono para VEP
      String strCodBSCS   =   request.getParameter("hdnCodBscsDetail");
      objHashMap.put("hdnCodBscsDetail", strCodBSCS);
      String strSiteId   =   request.getParameter("txtSiteId");
      objHashMap.put("strSiteId", strSiteId);
      String strTotalSalesVep   =   request.getParameter("hdnTotalSalesPriceVEP");
      objHashMap.put("hdnTotalSalesPriceVEP", strTotalSalesVep);
      //Fin Se adiciono para VEP

	  
	  //Inicio se adiciono para Despacho en tienda RMARTINEZ
      String strhdnChkCourier   =   request.getParameter("hdnChkCourier");
      if (strhdnChkCourier != null){
         objHashMap.put("hdnChkCourier", strhdnChkCourier);
      }
      //Fin se adiciono para Despacho en tienda RMARTINEZ
	  
      // MMONTOYA Despacho en tienda
      String hdnDevolverEquipoListaAux = request.getParameter("hdnDevolverEquipoListaAux");
      if (hdnDevolverEquipoListaAux != null) {
          String[] chkDevolverEquipoLista = hdnDevolverEquipoListaAux.split("-");
          objHashMap.put("chkDevolverEquipoLista", chkDevolverEquipoLista);      
      }


        //EFLORES N_SD000349095
        String chkCarpetaDigital = request.getParameter("hdnCarpetaDigital");
        objHashMap.put("chkCarpetaDigital",chkCarpetaDigital);
        //EFLORES


        // INICIO - LHUAPATA [PRY-0710]
        logger.info("Inicio de validacion [PRY-0710]");
        String screenOptionRolModProd = objGeneralService.getValue(Constante.ROL_USER_MOD_PROD_NPTABLE,Constante.ROL_USER_MOD_PROD_NPVALUEDESC_SCREEN_OPTION);
        int intFlagSpecificationModProd = objGeneralService.validateIfNpvalueIsInNptable(Constante.ROL_USER_MOD_PROD_NPTABLE,Constante.ROL_USER_MOD_PROD_NPVALUEDESC_SPECIFICATION,strSpecificationId);
        int intFlagInboxModProd = objGeneralService.validateIfNpvalueIsInNptable(Constante.ROL_USER_MOD_PROD_NPTABLE,Constante.ROL_USER_MOD_PROD_NPVALUEDESC_INBOX,strCurrentInbox);

        HashMap hshUserRolModProd = null;
        hshUserRolModProd = objGeneralService.getRol(MiUtil.parseInt(screenOptionRolModProd), objPortalSesBean.getUserid(),objPortalSesBean.getAppid());
        int intFlagUserRolModProd  = MiUtil.parseInt((String)hshUserRolModProd.get("iRetorno"));
        logger.info("[PRY-0710]"+lOrderId+" El SCREENOPTIONROLMODPROD "+screenOptionRolModProd);
        logger.info("[PRY-0710]"+lOrderId+" Permite Modificacion de Producto (0: No, 1: Si)"+intFlagUserRolModProd);
        logger.info("[PRY-0710]"+lOrderId+" Specificacion : "+strSpecificationId+ " Permiso en specificacion : "+intFlagSpecificationModProd);
        logger.info("[PRY-0710]"+lOrderId+" Status, : "+strCurrentInbox+" Permiso en inbox : "+intFlagInboxModProd);

        //[PRY-0710] Se verifica el Rol y el inbox de portabilidad
        if (intFlagSpecificationModProd == 1 && intFlagInboxModProd == 1 && intFlagUserRolModProd == 1){
            objHashMap.put("edititems","Enabled");
            logger.info("[PRY-0710]"+lOrderId+" Se cambia el status de edicion de items edititems : "+(String)objHashMap.get("edititems"));
        }
        logger.info("Fin de validacion [PRY-0710]");
        // FIN -- LHUAPAYA [PRY-0710]

        //Ini: [TDECONV003] KOTINIANO
        String strFlagMigracion = null;
        if(request.getParameter("hdnFlagMigration") != null) {
            strFlagMigracion = String.valueOf( request.getParameter("hdnFlagMigration")).trim();
        }
        logger.info("[EditOrderServlet]EditOrderServlet/doUpdateOrder hdnFlagMigration->" + strFlagMigracion);
        //Fin: [TDECONV003] KOTINIANO

      HashMap hshRetorno = objOrderService.updOrder(objHashMap,objPortalSesBean);         
      strMessage=(String)hshRetorno.get("strMessage");
        logger.info("[EditOrderServlet][updOrder][strMessage]"+strMessage);
      if (strMessage!=null)
        throw new Exception(strMessage);                
         
      //JRIOS VIDD
        //INICIO PRY-0787 JRIOS INSERTA EN LA DOCUMENT GENERATION


        DocumentGenerationBean documentGenerationBean = objDigitalDocumentService.getDocumentGenerationBean(request, objPortalSesBean);
        DocAssigneeBean docAssigneeBean = objDigitalDocumentService.getDocAssigneeBean(request, objPortalSesBean);

        objDigitalDocumentService.saveDocumentGenerationAndAssigneST(request, objPortalSesBean, docAssigneeBean, documentGenerationBean);

        //SECTION POPULATE CENTER
        if(Constante.FLAG_SECTION_ACTIVE==MiUtil.getInt(request.getParameter("hdnFlgCPUF"))){
            if(Constante.SHOW_TYPE_EDIT.equals(request.getParameter("hdnDisplay"))){
            int cpufResponse=MiUtil.getInt(request.getParameter("hdnCpufResponse"));
            if(cpufResponse!=2){
                PopulateCenterBean populateCenterBean=new PopulateCenterBean();
                populateCenterBean.setResponse(cpufResponse);
                populateCenterBean.setOrderId(lOrderId);
                populateCenterService.savePopulateCenter(populateCenterBean,objPortalSesBean.getLogin());

            }
            }
        }

            //FIN JRIOS

      //JCURI PRY-0890 Mensaje de la Orden de Prorrateo(Pago Anticipado).
      ItemBean objItemProrrateo = (ItemBean)hshRetorno.get("objItemProrrateo");
      System.out.println("[successOrderProrrateo] " + objItemProrrateo);
            
      if(objItemProrrateo!=null) {
    	String message = "Se ha creado la orden de pago anticipado nueva con el numero "+ objItemProrrateo.getNporderid() +" con un monto de "+objItemProrrateo.getNpprice();
      	hshResult.put("messageOrderProrrateo", message);
      }
      
      //Karen Salvador solicita comentar esta sección
      //Validaciones del Blacklist
      //---------------------------
      /*hshValidateBlack =  new HashMap();
      int iReturnBlack = 0;
      hshValidateBlack = objOrderServiceTransaction.getValidateBlacklist(lOrderId);
      iReturnBlack=MiUtil.parseInt((String)hshValidateBlack.get("iResult"));
      String strMessageBlack = (String)hshValidateBlack.get("strMessage");
      if (iReturnBlack>=0){
        if (iReturnBlack>0){
            strMessageBlack = MiUtil.getMessageClean(strMessageBlack);
            out.println("alert('"+strMessageBlack+"')");
        }
      }else{
         if (strMessageBlack!=null)
          throw new Exception(strMessageBlack);   
      }*/
         
      strOrderId=((String)hshRetorno.get("strOrderId")==null?"0":(String)hshRetorno.get("strOrderId"));
      strSaveOption=((String)hshRetorno.get("strSaveOption")==null?"1":(String)hshRetorno.get("strSaveOption"));
      strInboxId=((String)hshRetorno.get("strInboxId")==null?"0":(String)hshRetorno.get("strInboxId"));                  
         
      strCustomerId=(String)hshRetorno.get("strCustomerId");      
      strDivisionId=(String)hshRetorno.get("strDivisionId");
      strNpBpelconversationid=(String)hshRetorno.get("strNpBpelconversationid");
      strActionName=(String)hshRetorno.get("strActionName");      
      strNextInboxName=(String)hshRetorno.get("strNextInboxName");      
      objSpecificationBean=(SpecificationBean)hshRetorno.get("objSpecificationBean");         
      strOldInboxName=(String)hshRetorno.get("strOldInboxName");  
      strSpecificationId= (String)hshRetorno.get("strSpecificationId");//CPUENTE5
      noInvokeBPELProrra=(String)hshRetorno.get("noInvokeBPELProrra");//JCURI      
      strNextInboxNamePA = (String) hshRetorno.get("strNextInboxNamePA");//JCURI      
      System.out.println("[EDITORDERSERVLET][doUpdateOrder][noInvokeBPELProrra] " + noInvokeBPELProrra);
      System.out.println("[EDITORDERSERVLET][doUpdateOrder][strNextInboxNamePA] " + strNextInboxNamePA);
            try{
                if(( strSpecificationId.equals("2068")) || ( strSpecificationId.equals("2069")) ){
                    Date fechaincio = new Date();
                    logger.info("[EDITORDERSERVLET][doUpdateOrder][PM0010354] : INICIO");
                    logger.info("[EDITORDERSERVLET][doUpdateOrder][PM0010354] : OrderId -->" +lOrderId);
                    logger.info("[EDITORDERSERVLET][doUpdateOrder][PM0010354] : Fecha -->" + fechaincio);
                    logger.info("[EDITORDERSERVLET][doUpdateOrder][PM0010354] : Inbox -->" + strCurrentInbox);
                }
            }catch(Exception e){
                strMessage = e.getMessage();
                logger.info("[EDITORDERSERVLET][doUpdateOrder][PM0010354] : ERROR AL INTENTAR PINTAR LOG INICIO "+strMessage);
                logger.error(formatException(e));
            }



      //INICIO DERAZO REQ-0428
      int pn_flag_penalty_func = MiUtil.parseInt(request.getParameter("hdnFlagPenaltyFunct"));
        logger.info("[UpdateOrder] Validar Pago Penalidad al AVANZAR: "+ pn_flag_penalty_func + " orden: "+strOrderId);
      if(pn_flag_penalty_func == 1 && strActionDescription.equals(Constante.ACTION_INBOX_AVANZAR)){
          String msgError;
          msgError = objPenaltyService.updateStatusFastOrder(MiUtil.parseLong(strOrderId),0,strLogin);
          if(msgError != null){
              logger.info("Aviso : La orden de penalidad asociada a la orden "+strOrderId+" si fue anulada");
              logger.info("Mensaje al intentar actualizar: "+msgError);
              msgError = null;
          }
          HashMap hshMapAvanzar = objPenaltyService.verifAddendumPenalty(Constante.OPTION_ADVANCE_ORDER, strOrderId, strLogin);
          msgError = (String)hshMapAvanzar.get(Constante.MESSAGE_OUTPUT);
          if (msgError!=null){
              strBpelError = "1";//Asignamos un error de BPEL para que refresque la pagina
              request.setAttribute("strErrorBPEL",strBpelError);
              throw new Exception(msgError);
          }
      }
      //FIN DERAZO

      boolean bInvokeBPEL=false;
         
      //Logica para determinar si se invoca o no al flujo BPEL
      if (strActionName!=null && !"".equals(strActionName)){
        if (Constante.ACTION_INBOX_BACK.equals(strActionName.toUpperCase())){ //si es retorceder
           if (strNextInboxName!=null && !"".equals(strNextInboxName)){                  
              bInvokeBPEL= true;                                      
           }         
        }else{ //Para cualquier accion diferente a retroceder
        	System.out.println("EditOrderSevlet/doUpdateOrder/noInvokeBPELProrra -> " + noInvokeBPELProrra);
             if(noInvokeBPELProrra != null && noInvokeBPELProrra.equals("S")) { //JCURI
                 bInvokeBPEL= false;
             } else {
                  bInvokeBPEL= true;
             }
         }
      }
         
        //JCASTILLO generate documents
        logger.info("[EditOrderServlet]validacion VIDD: objHashMap: "+objHashMap);
        logger.info("[VIDD]orderId:"+strOrderId+"hshRetorno:"+hshRetorno);
        logger.info("inbox actual:"+strCurrentInbox+"action:"+strActionDescription+"orderId:"+strOrderId);
        if( (strActionDescription.equals(Constante.ACTION_INBOX_AVANZAR) || strActionDescription.equals(Constante.ACTION_INBOX_SALTAR)) && strCurrentInbox.equals(Constante.INBOX_TIENDA01)&&(bInvokeBPEL==true)){
            HashMap hashMapDocumentGeneration;
            String channel =request.getParameter("hdnChannelClient");
            logger.info("channel:"+channel);
            hashMapDocumentGeneration=digitalDocumentService.getDocumentGeneration(strOrderId);

            DocumentGenerationBean documentGeneration= (DocumentGenerationBean) hashMapDocumentGeneration.get("documentGenerationBean");
            if(documentGeneration!=null) {
                logger.info("Se avanza la orden y se evalua para la generacion de documentos:" + strOrderId);
                logger.info("documentGeneration:" + documentGeneration);
                if (Constante.GENERATION_STATUS_IN_PROCESS == documentGeneration.getGenerationStatus()) {
                    strBpelError = "1";
                    request.setAttribute("strErrorBPEL", strBpelError);
                    throw new Exception(Constante.MESSAGE_IN_PROCESS_GENERATION);
                }
                if (Constante.GENERATION_STATUS_INITIAL == documentGeneration.getGenerationStatus() && Constante.TRX_TYPE_PORTABILIDAD_ID != documentGeneration.getTrxType()) {

                    //JVERGARA Valida el tipo de Verificación
                    logger.info("[EditOrderServlet][doUpdateOrder] INICIO Validar Verificación");
                    HashMap objhash;
                    objhash = digitalDocumentService.updateGenerationVI(Integer.parseInt(strOrderId), strLogin);
                    if (StringUtils.isBlank((String) objhash.get(Constante.MESSAGE_OUTPUT))) {
                        logger.info("[EditOrderServlet][doUpdateOrder] Actualizando Generacion de documento si existe OrderId: " + ((DocumentGenerationBean) objhash.get("documentGenerationBean")).getOrderId());
                    } else {
                        logger.info("[EditOrderServlet][doUpdateOrder] Actualizando Generacion de documento si existe " + objhash.get(Constante.MESSAGE_OUTPUT));
                    }
                    logger.info("[EditOrderServlet][doUpdateOrder] FIN Validar Verificación");
                    //JVERGARA Fin
                    HashMap hashMap = digitalDocumentService.generateDocumentsAndSendEmail(strOrderId, strDivisionId, channel, strLogin);
                    logger.info(hashMap);
                    int status = MiUtil.getInt(hashMap.get("status"));
                    hshResult.put("flagGeneration", Constante.FLAG_SECTION_ACTIVE);
                    String message = MiUtil.getString(hashMap.get("message"));
                    logger.info("[EditOrderServlet][doUpdateOrder] Mensaje: " + message + " hashMap: " + hashMap);
                    hshResult.put("messageGeneration", message);
                    if (status != 1) {
                        strBpelError = "1";
                        request.setAttribute("strErrorBPEL", strBpelError);
                        throw new Exception("GENERATION_ERROR");
                    }

                }
            }


        }

        logger.info("bInvokeBPEL-->"+bInvokeBPEL);         
       //Invocacion a BPEL
       if (bInvokeBPEL==true){
        hashData.put("objSpecificationBean",objSpecificationBean);
        hashData.put("strOrderId",strOrderId);
        hashData.put("strCustomerId",strCustomerId);        
        hashData.put("strDivisionId",strDivisionId);
        hashData.put("strNpBpelconversationid",strNpBpelconversationid);
        hashData.put("strActionName",strActionName);
        hashData.put("strNextInboxName",strNextInboxName);         
        hashData.put("strOldInboxName",strOldInboxName);      
        hashData.put("strCurrentInbox",strCurrentInbox);      
        
        WorkflowService objWorkflowService = new WorkflowService();

        logger.info("------doInvokeBPELProcess / INICIO-DATOS PARA BPEL------");
        logger.info("strOrderId-->"+strOrderId);
        logger.info("strCustomerId-->"+strCustomerId);
        logger.info("strDivisionId-->"+strDivisionId);
        logger.info("strNpBpelconversationid-->"+strNpBpelconversationid);
        logger.info("strActionName-->"+strActionName);
        logger.info("EditOrderSevlet strNextInboxName-->"+strNextInboxName);
        logger.info("objPortalSesBean.getLogin()-->"+objPortalSesBean.getLogin());
        logger.info("objSpecificationBean.getNpBpelflowgroup()-->"+objSpecificationBean.getNpBpelflowgroup());
        logger.info("strOldInboxName-->"+strOldInboxName);
        logger.info("------doInvokeBPELProcess / FIN- DATOS PARA BPEL------");
          
        hshRetorno= objWorkflowService.doInvokeBPELProcess(hashData, objPortalSesBean);
          
        strCurrentStatus=(String)hshRetorno.get("strOldInbox");
        strNextStatus=(String)hshRetorno.get("strNextInbox");
        String strAsignadoCredito=null;
        String strAnalistaCredito=null;            
        strMessage=(String)hshRetorno.get("strMessage");

           logger.info("Mensaje de Error del BPEL: "+strMessage);
        
        if (strMessage!=null && strNextStatus!=null){
          iErrorNumber=0;
          throw new ExceptionBpel(strMessage);					
        }else if (strMessage!=null)
          throw new ExceptionBpel(strMessage);
             
          if (Constante.INBOX_ADM_VENTAS.equals(strCurrentStatus) && Constante.INBOX_CREDITO.equals(strNextStatus) && strAnalistaCredito==null && strAsignadoCredito !=null)
             strMesgAsignado=", asignado a " + strAsignadoCredito;          
          
         
          //Invocacion a sp q realiza la Sincronización de las Cuentas de BSCS / CRM al activarse los contratos
          //if (Constante.INBOX_ACTIVACION_PROGRAMACION.equals(strCurrentStatus) && Constante.ACTION_INBOX_AVANZAR.equals(strActionName.toUpperCase())){
          if (Constante.ACTION_INBOX_AVANZAR.equals(strActionName.toUpperCase())){
          
             //strMessage=objOrderService.updSinchronizeActiv(lOrderId);
             hshRetorno=objOrderService.doExecuteActionFromOrder(lOrderId,strCurrentStatus,strNextStatus,strLogin,lLoginBuilding);               
         
             iErrorNumber = MiUtil.parseInt((String)hshRetorno.get("strError"));         
             strMessage=(String)hshRetorno.get("strMessage");     

             if (iErrorNumber < 0){ 
                if (strMessage!=null)
                   //throw new Exception(strMessage);      
            throw new ExceptionBpel(strMessage);
             }
          }      
      }  //fin del if de la invocacion BPEL      

      hshRetorno= objOrderService.updTimeStamp(lOrderId);
      strMessage=(String)hshRetorno.get("strMessage");

      if (strMessage!=null)
        throw new Exception(strMessage);           
    
      }catch(ExceptionBpel ex){
        strMessage = ex.getMessage();
        strBpelError="1";
        logger.error("[doUpdateOrder][Capturado en el catch ExceptionBpel]",ex);
      }catch(Exception e){
        strMessage = e.getMessage();
        logger.error("[doUpdateOrder][Capturado en el catch Exception]",e);
      }



      logger.info("--------------- Inicio EditOrderServlet.java ------------------");
      logger.info("strActionName-->"+strActionName);
      logger.info("strOrderId-->"+strOrderId+"");
      logger.info("strSaveOption-->"+strSaveOption);
      logger.info("strInboxId-->"+strInboxId);
      logger.info("strCurrentStatus-->"+strCurrentStatus);
      logger.info("strMessage-->"+strMessage);
      logger.info("strMesgAsignado-->"+strMesgAsignado);
      logger.info("strNextStatus-->"+strNextStatus);
      logger.info("strOldInboxName-->"+strOldInboxName);
      logger.info("J strNextInboxNamePA-->"+strNextInboxNamePA); 
      logger.info("lLoginBuilding-->"+lLoginBuilding);
      logger.info("strLogin-->"+strLogin);
      logger.info("strSpecificationId-->"+strSpecificationId);
      
        try{
            if(( strSpecificationId.equals("2068")) || ( strSpecificationId.equals("2069")) ){
                Date fechafin = new Date();
                logger.info("[EDITORDERSERVLET][doUpdateOrder][PM0010354] : OrderId -->" +strOrderId);
                logger.info("[EDITORDERSERVLET][doUpdateOrder][PM0010354] : Fecha -->" + fechafin);
                logger.info("[EDITORDERSERVLET][doUpdateOrder][PM0010354] : Inbox -->" + strInboxId);
                logger.info("[EDITORDERSERVLET][doUpdateOrder][PM0010354] : FIN");
            }
        }catch(Exception e){
            logger.error(formatException(e));
            strMessage = e.getMessage();
            logger.info("[EDITORDERSERVLET][doUpdateOrder][PM0010354] : ERROR AL INTENTAR PINTAR LOG FIN "+strMessage);
        }

      logger.info("--------------- Fin EditOrderServlet.java ------------------");
      
      
      hshResult.put("strOrderId",strOrderId);
      hshResult.put("strSaveOption",strSaveOption);
      //hshResult.put("strOrderPermEdit",strOrderPermEdit);
      hshResult.put("strInboxId",strInboxId);
      hshResult.put("strCurrentStatus",strCurrentStatus);
      hshResult.put("strOldInboxName",strOldInboxName);
      hshResult.put("strSpecificationId",strSpecificationId);//CPUENTE5
      
      hshResult.put("strMesgAsig",strMesgAsignado);
      hshResult.put("strMessage",strMessage);         
      hshResult.put("strNextInboxName",strNextStatus);  
      hshResult.put("strNextInboxNamePA",strNextInboxNamePA); //JCURI      
      
      hshResult.put("strErrorNumer",iErrorNumber+"");        
      hshResult.put("strTipoOrigen","UpdOrden");      
      
      request.setAttribute("objResultado",hshResult);
		  request.setAttribute("strErrorBPEL",strBpelError);
      //RDELOSREYES - CARGA MASIVA - 31/10/2008
      request.getSession(true).removeAttribute(Constante.DATA_STRUCT);
      RequestDispatcher rd=request.getRequestDispatcher("PAGEEDIT/ResultEdit.jsp");
      rd.forward(request,response);
      logger.info("*************** FIN EditOrderSevlet > doUpdateOrder ***************");
   } 
  
  
  public void doUpdateOrderDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
   
    HashMap hshResult=new HashMap();
    HashMap hashData=new HashMap();
    String strMessage = null;        
    String strOrderId=null;
    String strSaveOption=null;
    String strInboxId=null;
    String strCurrentStatus=null;
    String strMesgAsignado=null; 
    String strNextStatus=null;
    String strNextInboxName=null;
    String strActionName=null;
    SpecificationBean objSpecificationBean=null;
    String strCustomerId=null;    
	 String strDivisionId=null;
    String strNpBpelconversationid=null;
    String strOldInboxName=null;
    String strCurrentInbox = "";
    int iErrorNumber=-1;
    
    String strUserId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
    System.out.println("strUserId-->"+strUserId);
    
    PortalSessionBean objPortalSesBean = null;
    objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strUserId);
      
    String strLogin = null; 
    long lLoginBuilding = 0; 
    
    try{             
      
      if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
        throw new Exception("La sesión del usuario ha expirado por favor vuelva a registrarse. Inicie Login");
      
      strLogin        = objPortalSesBean.getLogin();
      lLoginBuilding  = objPortalSesBean.getBuildingid();
      
      objHashMap =null;
      objHashMap = getParameterNames(request);
      long lOrderId =(request.getParameter("hdnOrderId")==null?0:MiUtil.parseLong(request.getParameter("hdnOrderId")));
      strCurrentInbox=(request.getParameter("txtEstadoOrden")==null?"":request.getParameter("txtEstadoOrden"));
      
      //Actualiza valores de la orden
      //-----------------------------
      HashMap hshRetorno = objOrderService.updOrderDetail(objHashMap,objPortalSesBean);
      strMessage=(String)hshRetorno.get("strMessage");                   
      if (strMessage!=null)
        throw new Exception(strMessage);  
      
      strOrderId=((String)hshRetorno.get("strOrderId")==null?"0":(String)hshRetorno.get("strOrderId"));      
      strSaveOption=((String)hshRetorno.get("strSaveOption")==null?"1":(String)hshRetorno.get("strSaveOption"));
      strInboxId=((String)hshRetorno.get("strInboxId")==null?"0":(String)hshRetorno.get("strInboxId")); 
      strNextInboxName=(String)hshRetorno.get("strNextInboxName"); 
         
      strActionName=(String)hshRetorno.get("strActionName");
      strCustomerId=(String)hshRetorno.get("strCustomerId");      
		strDivisionId=(String)hshRetorno.get("strDivisionId");
      strNpBpelconversationid=(String)hshRetorno.get("strNpBpelconversationid");  
      objSpecificationBean=(SpecificationBean)hshRetorno.get("objSpecificationBean");
      strOldInboxName=(String)hshRetorno.get("strOldInboxName");   

      //INICIO DERAZO REQ-0428
      int pn_flag_penalty_func = MiUtil.parseInt(request.getParameter("hdnFlagPenaltyFunct"));
      System.out.println("[UpdateOrder] Validar Pago Penalidad al AVANZAR: "+ pn_flag_penalty_func + " orden: "+strOrderId);
      if(pn_flag_penalty_func == 1 && strActionName.equals(Constante.ACTION_INBOX_AVANZAR)){
          int flagAvanzar = 0;
          String msgError;
          msgError = objPenaltyService.updateStatusFastOrder(MiUtil.parseLong(strOrderId),0,strLogin);
          if(msgError != null){
              System.out.println("Aviso : La orden de penalidad asociada a la orden "+strOrderId+" si fue anulada");
              System.out.println("Mensaje al intentar actualizar "+msgError);
              msgError = null;
          }
          HashMap hshMapAvanzar = objPenaltyService.verifAddendumPenalty(Constante.OPTION_ADVANCE_ORDER, strOrderId, strLogin);
          msgError = (String)hshMapAvanzar.get(Constante.MESSAGE_OUTPUT);
          if (msgError!=null)
              throw new Exception(msgError);
      }
      //FIN DERAZO

      boolean bInvokeBPEL=false;
      
      //Logica para determinar si se invoca o no al flujo BPEL
      //------------------------------------------------------
      if (strActionName!=null && !"".equals(strActionName)){
        if (Constante.ACTION_INBOX_BACK.equals(strActionName.toUpperCase())){ //si es retorceder
          if (strNextInboxName!=null && !"".equals(strNextInboxName)){                  
             bInvokeBPEL= true;                                      
          }         
        }else{ //Para cualquier accion diferente a retroceder
             bInvokeBPEL= true;
        }
      }
      
      //Invocacion a BPEL
      //-----------------
      if (bInvokeBPEL==true){
        hashData.put("objSpecificationBean",objSpecificationBean);
        hashData.put("strOrderId",strOrderId);
        hashData.put("strCustomerId",strCustomerId);        
		  hashData.put("strDivisionId",strDivisionId);
        hashData.put("strNpBpelconversationid",strNpBpelconversationid);
        hashData.put("strActionName",strActionName);
        hashData.put("strNextInboxName",strNextInboxName);         
        hashData.put("strOldInboxName",strOldInboxName);      
        hashData.put("strCurrentInbox",strCurrentInbox);      
        WorkflowService objWorkflowService = new WorkflowService();

        System.out.println("------doInvokeBPELProcess / INICIO-DATOS PARA BPEL------");    
        System.out.println("strOrderId-->"+strOrderId);
        System.out.println("strCustomerId-->"+strCustomerId);        
		  System.out.println("strDivisionId-->"+strDivisionId);
        System.out.println("strNpBpelconversationid-->"+strNpBpelconversationid);
        System.out.println("strActionName-->"+strActionName);
        System.out.println("strNextInboxName-->"+strNextInboxName);
        System.out.println("objPortalSesBean.getLogin()-->"+objPortalSesBean.getLogin());         
        System.out.println("objSpecificationBean.getNpBpelflowgroup()-->"+objSpecificationBean.getNpBpelflowgroup());
        System.out.println("strOldInboxName-->"+strOldInboxName);         
        System.out.println("------doInvokeBPELProcess / FIN- DATOS PARA BPEL------");   
          
        hshRetorno= objWorkflowService.doInvokeBPELProcess(hashData, objPortalSesBean);
            
        strCurrentStatus=(String)hshRetorno.get("strOldInbox");
        strNextStatus=(String)hshRetorno.get("strNextInbox");
        String strAsignadoCredito=null;
        String strAnalistaCredito=null;            
        strMessage=(String)hshRetorno.get("strMessage");
        
        System.out.println("Mensaje de Error del BPEL-->"+strMessage);
        if (strMessage!=null && strNextStatus!=null){
          iErrorNumber=0;
          throw new ExceptionBpel(strMessage);					
        }else if (strMessage!=null)
          throw new ExceptionBpel(strMessage);
       
       
        //Invocacion a sp q realiza la Sincronización de las Cuentas de BSCS / CRM al activarse los contratos
        //if (Constante.INBOX_ACTIVACION_PROGRAMACION.equals(strCurrentStatus) && Constante.ACTION_INBOX_AVANZAR.equals(strActionName.toUpperCase())){
        if (Constante.ACTION_INBOX_AVANZAR.equals(strActionName.toUpperCase())){
          //strMessage=objOrderService.updSinchronizeActiv(lOrderId);
          hshRetorno=objOrderService.doExecuteActionFromOrder(lOrderId,strCurrentStatus,strNextStatus,strLogin,lLoginBuilding);               
          iErrorNumber = MiUtil.parseInt((String)hshRetorno.get("strError"));         
          strMessage=(String)hshRetorno.get("strMessage");     
          if (iErrorNumber < 0){
            if (strMessage!=null)
              throw new ExceptionBpel(strMessage);
          }
        }
        
      }//fin del if de la invocacion BPEL     
         
        
        
      //Actualiza el timeStap
      //----------------------
      hshRetorno= objOrderService.updTimeStamp(lOrderId);
      strMessage=(String)hshRetorno.get("strMessage");

      if (strMessage!=null)
        throw new Exception(strMessage); 
            
         

    }catch(Exception e){
      strMessage = e.getMessage();       
      e.printStackTrace();         
			System.out.println("[doUpdateOrderDetail][Capturado en el catch Exception]"+e.getMessage());
    }   
      
      System.out.println("--------------- Inicio EditOrderServlet.java ------------------");    ;
      System.out.println("lOrderId-->"+strOrderId+"");
      System.out.println("strSaveOption:"+strSaveOption);
      System.out.println("strInboxId:"+strInboxId);
      System.out.println("strMessage-->"+strMessage);                
      System.out.println("lLoginBuilding-->"+lLoginBuilding);         
      System.out.println("strLogin-->"+strLogin);               
      System.out.println("--------------- Fin EditOrderServlet.java ------------------");            
      
      hshResult.put("strOrderId",strOrderId);
      hshResult.put("strSaveOption",strSaveOption);
      hshResult.put("strInboxId",strInboxId);
      hshResult.put("strCurrentStatus",strCurrentStatus);
      hshResult.put("strMesgAsig",strMesgAsignado);
      hshResult.put("strMessage",strMessage);         
      hshResult.put("strNextInboxName",strNextStatus);  
      hshResult.put("strErrorNumer",iErrorNumber+"");        
      hshResult.put("strTipoOrigen","UpdOrdenDetail");      
      request.setAttribute("objResultado",hshResult);
      RequestDispatcher rd=request.getRequestDispatcher("PAGEEDIT/ResultEdit.jsp");
      rd.forward(request,response);  
   }
   
   /**
   * Motivo: Guarda Site
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 01/10/2007 
   * @param     request     
   * @param     response             
   */         
  public void doInsertSite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{    
   
    String strMessage = null;        
    String strOrderId=null;   
    String strCustomerId=null;
    String strSiteId=null;        
    SiteBean objSiteBean=null;

    String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
	  String strSpecificationId=(request.getParameter("hdnSpecificationId")==null?"0":request.getParameter("hdnSpecificationId")); //CEM-COR0354
    
    
    
    PortalSessionBean objPortalSesBean = null;
      
    String strLogin = null; 
    long lLoginBuilding = 0; 
    
    try{             
      objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
      
      if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
        throw new Exception("La sesión del usuario ha expirado por favor vuelva a registrarse. Inicie Login");
     
     objHashMap =null; 
     objHashMap = getParameterNames(request);
     if (objHashMap==null)
        System.out.println("objHashMap Es nulo");
     HashMap hshRetorno = objSiteService.insSites(objHashMap,objPortalSesBean);   
     strOrderId=(String)hshRetorno.get("strOrderId");
     strCustomerId=(String)hshRetorno.get("strCustomerId");
     strSiteId=(String)hshRetorno.get("strSiteId");
     objSiteBean= (SiteBean)hshRetorno.get("objSite");
     strMessage=  (String)hshRetorno.get("strMessage");
		 if(logger.isDebugEnabled()){
			logger.debug(" -------------------- INICIO EdittOrderServlet.java / doInsertSite ---------------------- ");
			logger.debug("strOrderId-->"+strOrderId);
			logger.debug("strCustomerId-->"+strCustomerId);
			logger.debug("strSiteId-->"+strSiteId);
			logger.debug("strSpecificationId: "+strSpecificationId);
			logger.debug("strMessage-->"+strMessage);
			logger.debug(" -------------------- FIN EdittOrderServlet.java / doInsertSite ---------------------- ");
		 }		 
      }catch(Exception e){
         e.printStackTrace();
         strMessage = e.getMessage();
      }   
      
      request.setAttribute("objSite",objSiteBean);
	  //INICIO CEM - COR0354
      //RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strSessionId+"|sAction=R&sMensaje="+strMessage);
	  //RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strSessionId+"|sAction=R&sMensaje="+strMessage+"&pCustomerId="+strCustomerId+"&pOrderId="+strOrderId+"&pSiteId="+strSiteId+"&pSpecificationId="+strSpecificationId);
	  RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strSessionId+"|pSpecificationId="+strSpecificationId+"|sAction=R&sMensaje="+strMessage);
	  //FIN CEM - COR0354
      rd.forward(request,response);
   }   
        
   /**
   * Motivo: Guarda una Direccion
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 10/10/2007 
   * @param     request     
   * @param     response             
   */         
  public void doInsertAddress(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
   
    String strMessage = null;        
    String strOrderId=null;   
    String strCustomerId=null;
    String strSiteId=null;  
      
    String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
	  String strSpecificationId=(request.getParameter("hdnSpecificationId")==null?"0":request.getParameter("hdnSpecificationId")); //CEM-COR0354
    
    PortalSessionBean objPortalSesBean = null;
    try{
      objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
      
      if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
        throw new Exception("La sesión del usuario ha expirado por favor vuelva a registrarse. Inicie Login");
        
         objHashMap =null; 
         objHashMap = getParameterNames(request);
         HashMap hshRetorno = objSiteService.insAddress(objHashMap,objPortalSesBean);   
         strOrderId=(String)hshRetorno.get("strOrderId");
         strCustomerId=(String)hshRetorno.get("strCustomerId");
         strSiteId=(String)hshRetorno.get("strSiteId");      
         strMessage=  (String)hshRetorno.get("strMessage");
		 if(logger.isDebugEnabled()){
			logger.debug(" -------------------- INICIO EdittOrderServlet.java / doInsertAddress ---------------------- ");
			logger.debug("strOrderId-->"+strOrderId);
			logger.debug("strCustomerId-->"+strCustomerId);
			logger.debug("strSiteId-->"+strSiteId);
			logger.debug("strSpecificationId: "+strSpecificationId);
			logger.debug("strMessage-->"+strMessage);
			logger.debug(" -------------------- FIN EdittOrderServlet.java / doInsertAddress ---------------------- ");
		 }			 
      }catch(Exception e){
         e.printStackTrace();
         strMessage = e.getMessage();
      }           
      
      //RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strUserId+"&sPage=Dir&sMensaje="+strMessage);
	  RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|pSpecificationId="+strSpecificationId+"|hdnSessionId="+strSessionId+"&sPage=Dir&sMensaje="+strMessage); // CEM - COR0354
      rd.forward(request,response);  
   
   }  
        
   /**
   * Motivo: Actualiza un listado de Direcciones
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 22/10/2007 
   * @param     request     
   * @param     response             
   */         
  public void doUpdateAddress(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
      
    String strMessage = null;        
    String strOrderId=null;   
    String strCustomerId=null;
    String strSiteId=null;    
    
    String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
	  String strSpecificationId=(request.getParameter("hdnSpecificationId")==null?"0":request.getParameter("hdnSpecificationId")); //CEM-COR0354
	  
    PortalSessionBean objPortalSesBean = null;
    try{
      objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
      
      if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
        throw new Exception("La sesión del usuario ha expirado por favor vuelva a registrarse. Inicie Login");
      
         objHashMap =null; 
         objHashMap = getParameterNames(request);    
         HashMap hshRetorno = objSiteService.updAddress(objHashMap,objPortalSesBean);   
         
         strOrderId=(String)hshRetorno.get("strOrderId");
         strCustomerId=(String)hshRetorno.get("strCustomerId");
         strSiteId=(String)hshRetorno.get("strSiteId");      
         strMessage=  (String)hshRetorno.get("strMessage");
		 if(logger.isDebugEnabled()){
			logger.debug(" -------------------- INICIO EdittOrderServlet.java / doUpdateAddress ---------------------- ");
			logger.debug("strSessionId-->"+strSessionId);
			logger.debug("strOrderId-->"+strOrderId);
			logger.debug("strCustomerId-->"+strCustomerId);
			logger.debug("strSiteId-->"+strSiteId);
			logger.debug("strSpecificationId: "+strSpecificationId);
			logger.debug("strMessage-->"+strMessage);
			logger.debug(" -------------------- FIN EdittOrderServlet.java / doUpdateAddress ---------------------- ");
		 }		 
      }catch(Exception e){
         e.printStackTrace();
         strMessage = e.getMessage();
      }
      //RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strUserId+"&nOrigen=1&sPage=Dir&sMensaje="+strMessage);
	  RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|pSpecificationId="+strSpecificationId+"|hdnSessionId="+strSessionId+"&nOrigen=1&sPage=Dir&sMensaje="+strMessage);	  // CEM - COR0354
      rd.forward(request,response);  
   }        
        
   /**
   * Motivo: Borra un Address 
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 11/10/2007 
   * @param     request     
   * @param     response             
   */         
  public void doDeleteAddress(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{    
          
    String strMessage = null;        
    String strOrderId=null;   
    String strCustomerId=null;
    String strSiteId=null;    
    String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));      
    String strSpecificationId=(request.getParameter("hdnSpecificationId")==null?"0":request.getParameter("hdnSpecificationId")); //CEM-COR0354
    
    PortalSessionBean objPortalSesBean = null;
    try{
      objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
      
      if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
        throw new Exception("La sesión del usuario ha expirado por favor vuelva a registrarse. Inicie Login");
    
         objHashMap =null; 
         objHashMap = getParameterNames(request);
         HashMap hshRetorno = objSiteService.delAddress(objHashMap,objPortalSesBean);   
         strOrderId=(String)hshRetorno.get("strOrderId");
         strCustomerId=(String)hshRetorno.get("strCustomerId");
         strSiteId=(String)hshRetorno.get("strSiteId");      
         strMessage=  (String)hshRetorno.get("strMessage");
		 if(logger.isDebugEnabled()){
			logger.debug(" -------------------- INICIO EdittOrderServlet.java / doDeleteAddress ---------------------- ");
			logger.debug("strSessionId-->"+strSessionId);
			logger.debug("strOrderId-->"+strOrderId);
			logger.debug("strCustomerId-->"+strCustomerId);
			logger.debug("strSiteId-->"+strSiteId);
			logger.debug("strSpecificationId: "+strSpecificationId);
			logger.debug("strMessage-->"+strMessage);
			logger.debug(" -------------------- FIN EdittOrderServlet.java / doDeleteAddress ---------------------- ");
		 }			 
      }catch(Exception e){
         e.printStackTrace();
         strMessage = e.getMessage();
      }         
      //RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strUserId+"&nOrigen=2&sPage=Dir&sMensaje="+strMessage);
	  //RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|pSpecificationId="+strSpecificationId+"|hdnSessionId="+strUserId+"&nOrigen=2&sPage=Dir&sMensaje="+strMessage); // CEM - COR0354
	  RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|pSpecificationId="+strSpecificationId+"|hdnSessionId="+strSessionId+"&nOrigen=2&sPage=Dir&sMensaje="+strMessage); // CEM - COR0354	  
      rd.forward(request,response);  
   }         
   
    
        
   /**
   * Motivo: Actualiza Nuevo Site
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 10/10/2007 
   * @param     request     
   * @param     response             
   */         
  public void doUpdateSite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{    
          
    String strMessage = null;        
    String strOrderId=null;   
    String strCustomerId=null;
    String strSiteId=null;       
    SiteBean objSiteBean=null;
    String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
	  String strSpecificationId=(request.getParameter("hdnSpecificationId")==null?"0":request.getParameter("hdnSpecificationId")); //CEM-COR0354
	        
    PortalSessionBean objPortalSesBean = null;
    try{
      objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
      
      if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
        throw new Exception("La sesión del usuario ha expirado por favor vuelva a registrarse. Inicie Login");
    


         objHashMap =null; 
         objHashMap = getParameterNames(request);
         HashMap hshRetorno = objSiteService.updSites(objHashMap,objPortalSesBean);   
         strOrderId=(String)hshRetorno.get("strOrderId");
         strCustomerId=(String)hshRetorno.get("strCustomerId");
         strSiteId=(String)hshRetorno.get("strSiteId");      
         objSiteBean= (SiteBean)hshRetorno.get("objSite");
         strMessage=  (String)hshRetorno.get("strMessage");
		 if(logger.isDebugEnabled()){
			logger.debug(" -------------------- INICIO EdittOrderServlet.java / doUpdateSite ---------------------- ");
			logger.debug("strSessionId-->"+strSessionId);
			logger.debug("strOrderId-->"+strOrderId);
			logger.debug("strCustomerId-->"+strCustomerId);
			logger.debug("strSiteId-->"+strSiteId);
			logger.debug("strSpecificationId: "+strSpecificationId);
			logger.debug("strMessage-->"+strMessage);
			logger.debug(" -------------------- FIN EdittOrderServlet.java / doUpdateSite ---------------------- ");
		 }		 
      }catch(Exception e){
         e.printStackTrace();
         strMessage = e.getMessage();
      }     
      
      request.setAttribute("objSite",objSiteBean);
	  //INICIO CEM - COR0354
      //RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strUserId+"|sAction=R&nOrigen=1&sMensaje="+strMessage);
	  //RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strUserId+"|sAction=R&nOrigen=1&sMensaje="+strMessage+"&pCustomerId="+strCustomerId+"&pOrderId="+strOrderId+"&pSpecificationId="+strSpecificationId);  
	  RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strSessionId+"|pSpecificationId="+strSpecificationId+"|sAction=R&nOrigen=1&sMensaje="+strMessage);	  
	  //FIN CEM - COR0354
      rd.forward(request,response); 
   
   }      
       
   /**
   * Motivo: Borra un Site
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 10/10/2007 
   * @param     request     
   * @param     response             
   */         
   public void doDeleteSite(HttpServletRequest request, 
                  HttpServletResponse response)
   throws ServletException, IOException{    
          
      String strMessage = null; 
      String strMessage1 = null;       
      String strIndId=null;
	  //INICIO CEM - COR0354
	  String strCustomerId=null;
	  String strOrderId=null;
	  String strSiteId=null;
	  String strSpecificationId=null;
	  //FIN CEM - COR0354
      try{
         objHashMap =null; 
         objHashMap = getParameterNames(request);         
         HashMap hshRetorno = objSiteService.delSites(objHashMap);           
         HashMap hshRetorno1 = objSiteService.delSitesAssign(objHashMap);                    
         strIndId=(String)hshRetorno.get("strIndId");      
         strMessage=  (String)hshRetorno.get("strMessage");
         strMessage1=  (String)hshRetorno1.get("strMessage");
		 //INICIO CEM - COR0354
		 strCustomerId=(request.getParameter("pCustomerId")==null?"0":request.getParameter("pCustomerId"));
		 strOrderId=(request.getParameter("pOrderId")==null?"0":request.getParameter("pOrderId"));
		 strSiteId=(request.getParameter("nSiteId")==null?"0":request.getParameter("nSiteId"));		
		 strSpecificationId=(request.getParameter("pSpecificationId")==null?"0":request.getParameter("pSpecificationId"));
		 if(logger.isDebugEnabled()){
			logger.debug(" -------------------- INICIO EdittOrderServlet.java / doDeleteSite ---------------------- ");			
			logger.debug("strOrderId-->"+strOrderId);
			logger.debug("strCustomerId-->"+strCustomerId);
			logger.debug("strSiteId-->"+strSiteId);
			logger.debug("strSpecificationId: "+strSpecificationId);
			logger.debug("strMessage-->"+strMessage);
			logger.debug(" -------------------- FIN EdittOrderServlet.java / doDeleteSite ---------------------- ");
		 }
		 //FIN CEM - COR0354
      }catch(Exception e){
         e.printStackTrace();
         strMessage1 = e.getMessage();
      }     
      //INICIO CEM - COR0354    
      //RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?nOrigen=2&nInd="+ strIndId+"&sMensaje="+strMessage);		  
	  RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?nOrigen=2&nInd="+ strIndId+"&sMensaje="+strMessage+"&pCustomerId="+strCustomerId+"&pOrderId="+strOrderId+"&pSiteId="+strSiteId+"&pSpecificationId="+strSpecificationId);
	  //FIN CEM - COR0354
      rd.forward(request,response);  
   }    
   
   
   /**
   * Motivo: Guarda un Contacto
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 18/10/2007 
   * @param     request     
   * @param     response             
   */         
  public void doInsertContact(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{    
   
    String strMessage = null;        
    String strOrderId=null;   
    String strCustomerId=null;
    String strSiteId=null;   
    
    String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));      
	  String strSpecificationId=(request.getParameter("hdnSpecificationId")==null?"0":request.getParameter("hdnSpecificationId")); //CEM-COR0354
 
    PortalSessionBean objPortalSesBean = null;
    try{
      objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
      
      if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
        throw new Exception("La sesión del usuario ha expirado por favor vuelva a registrarse. Inicie Login");
    
    
         objHashMap =null; 
         objHashMap = getParameterNames(request);
         HashMap hshRetorno = objSiteService.insContact(objHashMap,objPortalSesBean);   
         strOrderId=(String)hshRetorno.get("strOrderId");
         strCustomerId=(String)hshRetorno.get("strCustomerId");
         strSiteId=(String)hshRetorno.get("strSiteId");      
         strMessage=  (String)hshRetorno.get("strMessage");
		 if(logger.isDebugEnabled()){
			logger.debug(" -------------------- INICIO EdittOrderServlet.java / doInsertContact ---------------------- ");
			logger.debug("strSessionId-->"+strSessionId);
			logger.debug("strOrderId-->"+strOrderId);
			logger.debug("strCustomerId-->"+strCustomerId);
			logger.debug("strSiteId-->"+strSiteId);
			logger.debug("strSpecificationId: "+strSpecificationId);
			logger.debug("strMessage-->"+strMessage);
			logger.debug(" -------------------- FIN EdittOrderServlet.java / doInsertContact ---------------------- ");
		 }		 
		 
      }catch(Exception e){
         e.printStackTrace();
         strMessage = e.getMessage();
      } 
      
      //RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strUserId+"&sPage=Contact&sMensaje="+strMessage);
	  RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strSessionId+"|pSpecificationId="+strSpecificationId+"&sPage=Contact&sMensaje="+strMessage); //CEM - COR0354
      rd.forward(request,response);  
   }   
        
   /**
   * Motivo: Actualiza un contacto
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 23/10/2007 
   * @param     request     
   * @param     response             
   */         
   public void doUpdateContact(HttpServletRequest request, 
                  HttpServletResponse response)
   throws ServletException, IOException{    
      String strMessage = null;        
      String strOrderId=null;   
      String strCustomerId=null;
      String strSiteId=null; 
      
      String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));     
	  String strSpecificationId=(request.getParameter("hdnSpecificationId")==null?"0":request.getParameter("hdnSpecificationId")); //CEM-COR0354

 	  if(logger.isDebugEnabled()){
	     logger.debug("Inicio del metodo doUpdateContact");
	     logger.debug("strUserId: "+strSessionId);
		 logger.debug("strSpecificationId: "+strSpecificationId);
	  }	

    PortalSessionBean objPortalSesBean = null;
    try{
      objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
      
      if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
        throw new Exception("La sesión del usuario ha expirado por favor vuelva a registrarse. Inicie Login");
    
         objHashMap =null; 
         objHashMap = getParameterNames(request);
         HashMap hshRetorno = objSiteService.updContact(objHashMap,objPortalSesBean);   
         strOrderId=(String)hshRetorno.get("strOrderId");
         strCustomerId=(String)hshRetorno.get("strCustomerId");
         strSiteId=(String)hshRetorno.get("strSiteId");      
         strMessage=  (String)hshRetorno.get("strMessage");
		 if(logger.isDebugEnabled()){
			logger.debug(" -------------------- INICIO EdittOrderServlet.java / doUpdateContact ---------------------- ");
			logger.debug("strSessionId-->"+strSessionId);
			logger.debug("strOrderId-->"+strOrderId);
			logger.debug("strCustomerId-->"+strCustomerId);
			logger.debug("strSiteId-->"+strSiteId);
			logger.debug("strSpecificationId: "+strSpecificationId);
			logger.debug("strMessage-->"+strMessage);
			logger.debug(" -------------------- FIN EdittOrderServlet.java / doUpdateContact ---------------------- ");
		 }			 
      }catch(Exception e){
         e.printStackTrace();
         strMessage = e.getMessage();
      }          
      //RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strUserId+"|sAction=R&nOrigen=1&sPage=Contact&sMensaje="+strMessage);
	  RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strSessionId+"|pSpecificationId="+strSpecificationId+"|sAction=R&nOrigen=1&sPage=Contact&sMensaje="+strMessage); //CEM - COR0354
	  
      rd.forward(request,response);  
     
   }        
   
   /**
   * Motivo: Borra un Contacto 
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 18/10/2007 
   * @param     request     
   * @param     response             
   */         
  public void doDeleteContact(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{    
  
    String strMessage = null;        
    String strOrderId=null;   
    String strCustomerId=null;
    String strSiteId=null;           
    String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));      
    String strSpecificationId=(request.getParameter("hdnSpecificationId")==null?"0":request.getParameter("hdnSpecificationId")); //CEM-COR0354
  
    PortalSessionBean objPortalSesBean = null;

    try{
      objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
      
      if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
        throw new Exception("La sesión del usuario ha expirado por favor vuelva a registrarse. Inicie Login");
        
         objHashMap =null; 
         objHashMap = getParameterNames(request);
         HashMap hshRetorno = objSiteService.delContact(objHashMap,objPortalSesBean);   
         strOrderId=(String)hshRetorno.get("strOrderId");
         strCustomerId=(String)hshRetorno.get("strCustomerId");
         strSiteId=(String)hshRetorno.get("strSiteId");      
         strMessage=  (String)hshRetorno.get("strMessage");
		 if(logger.isDebugEnabled()){
			logger.debug(" -------------------- INICIO EdittOrderServlet.java / doDeleteContact ---------------------- ");
			logger.debug("strSessionId-->"+strSessionId);
			logger.debug("strOrderId-->"+strOrderId);
			logger.debug("strCustomerId-->"+strCustomerId);
			logger.debug("strSiteId-->"+strSiteId);
			logger.debug("strSpecificationId: "+strSpecificationId);
			logger.debug("strMessage-->"+strMessage);
			logger.debug(" -------------------- FIN EdittOrderServlet.java / doDeleteContact ---------------------- ");
		 }
      }catch(Exception e){
         e.printStackTrace();
         strMessage = e.getMessage();
      }            
      //RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strUserId+"&nOrigen=2&sPage=Contact&sMensaje="+strMessage);
	  RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strSessionId+"|pSpecificationId="+strSpecificationId+"&nOrigen=2&sPage=Contact&sMensaje="+strMessage); //CEM - COR0354
      rd.forward(request,response);  
   }       
   
   /**
   * Motivo: Inserta un prospecto de Cuenta de Facturación
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 24/10/2007 
   * @param     request     
   * @param     response             
   */         
   public void doInsertBillAcount(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException{ 
      String strMessage = null;        
      String strOrderId=null;
      String strCustomerId=null;
      String strSiteId=null;  
      String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));      
	   String strSpecificationId=(request.getParameter("hdnSpecificationId")==null?"0":request.getParameter("hdnSpecificationId")); //CEM-COR0354
      String strFlag=(request.getParameter("hdnFlag")==null?"0":request.getParameter("hdnFlag"));      

    PortalSessionBean objPortalSesBean = null;

    try{
      objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
      
      if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
        throw new Exception("La sesión del usuario ha expirado por favor vuelva a registrarse. Inicie Login");
        
         objHashMap =null;  
         objHashMap = getParameterNames(request);
			System.out.println("objHashMap: "+objHashMap);
         HashMap hshRetorno = objBillAccService.insBillAccount(objHashMap,objPortalSesBean);   
         strOrderId=(String)hshRetorno.get("strOrderId");
         strCustomerId=(String)hshRetorno.get("strCustomerId");
         strSiteId=(String)hshRetorno.get("strSiteId");
         strMessage=(String)hshRetorno.get("strMessage");
		 if(logger.isDebugEnabled()){
			logger.debug(" -------------------- INICIO EdittOrderServlet.java / doInsertBillAcount ---------------------- ");
			logger.debug("strSessionId-->"+strSessionId);
			logger.debug("strOrderId-->"+strOrderId);
			logger.debug("strCustomerId-->"+strCustomerId);
			logger.debug("strSiteId-->"+strSiteId);
			logger.debug("strSpecificationId: "+strSpecificationId);
			logger.debug("strMessage-->"+strMessage);
			logger.debug(" -------------------- FIN EdittOrderServlet.java / doInsertBillAcount ---------------------- ");
		 }		 		 
      }catch(Exception e){
         //e.printStackTrace();
         strMessage = e.getMessage();
      }
	  //INICIO CEM - COR0354
      //RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strUserId+"&nOrigen=0&sPage=BillAcc&sMensaje="+strMessage);
	  //RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strUserId+"&nOrigen=0&sPage=BillAcc&sMensaje="+strMessage+"&pCustomerId="+strCustomerId+"&pOrderId="+strOrderId+"&pSiteId="+strSiteId);	  
	  RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strSessionId+"|pSpecificationId="+strSpecificationId+"|nFlag="+strFlag+"&nOrigen=0&sPage=BillAcc&sMensaje="+strMessage);	  
	  //FIN CEM - COR0354
      rd.forward(request,response);         
   }       
   
   /**
   * Motivo: Actualiza un prospecto de Cuenta de Facturación
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 24/10/2007 
   * @param     request     
   * @param     response             
   */         
   public void doUpdateBillAcount(HttpServletRequest request, 
                     HttpServletResponse response)
   throws ServletException, IOException{    
      String strMessage = null;        
      String strOrderId=null;
      String strCustomerId=null;
      String strSiteId=null;      
      String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));      
      String strSpecificationId=(request.getParameter("hdnSpecificationId")==null?"0":request.getParameter("hdnSpecificationId")); //CEM-COR0354
		String strFlag=(request.getParameter("hdnFlag")==null?"0":request.getParameter("hdnFlag"));
    
    PortalSessionBean objPortalSesBean = null;

    try{
      objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
      
      if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
        throw new Exception("La sesión del usuario ha expirado por favor vuelva a registrarse. Inicie Login");
        
         objHashMap =null;  
         objHashMap = getParameterNames(request);
			System.out.println("objHashMap: "+objHashMap);
         HashMap hshRetorno = objBillAccService.updBillAccount(objHashMap,objPortalSesBean);   
         strOrderId=(String)hshRetorno.get("strOrderId");
         strCustomerId=(String)hshRetorno.get("strCustomerId");
         strSiteId=(String)hshRetorno.get("strSiteId");
         strMessage=(String)hshRetorno.get("strMessage");
		 if(logger.isDebugEnabled()){
			logger.debug(" -------------------- INICIO EdittOrderServlet.java / doUpdateBillAcount ---------------------- ");
			logger.debug("strSessionId-->"+strSessionId);
			logger.debug("strOrderId-->"+strOrderId);
			logger.debug("strCustomerId-->"+strCustomerId);
			logger.debug("strSiteId-->"+strSiteId);
			logger.debug("strSpecificationId: "+strSpecificationId);
			logger.debug("strMessage-->"+strMessage);
			logger.debug(" -------------------- FIN EdittOrderServlet.java / doUpdateBillAcount ---------------------- ");
		 }		 
      }catch(Exception e){
         //e.printStackTrace();
         strMessage = e.getMessage();
      }           

	  //INICIO CEM - COR0354      
	  //RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strUserId+"&nOrigen=1&sPage=BillAcc&sMensaje="+strMessage);
      //RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strUserId+"&nOrigen=1&sPage=BillAcc&sMensaje="+strMessage+"&pCustomerId="+strCustomerId+"&pOrderId="+strOrderId+"&pSiteId="+strSiteId);
	  RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strSessionId+"|pSpecificationId="+strSpecificationId+"|nFlag="+strFlag+"&nOrigen=1&sPage=BillAcc&sMensaje="+strMessage); // CEM - COR0354	  
	  //FIN CEM - COR0354      
      rd.forward(request,response);         
   }             
   
   /**
   * Motivo: Borra un Contacto 
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 24/10/2007 
   * @param     request     
   * @param     response             
   */         
   public void doDeleteBillAccount(HttpServletRequest request, 
               HttpServletResponse response)
   throws ServletException, IOException{    
   
      String strMessage = null;        
      String strOrderId=null;
      String strCustomerId=null;
      String strSiteId=null;     
      String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));      
      String strSpecificationId=(request.getParameter("hdnSpecificationId")==null?"0":request.getParameter("hdnSpecificationId")); //CEM-COR0354
		System.out.println("request.getParameter(hdnFlag)"+request.getParameter("hdnFlag"));
		//String strFlag=(request.getParameter("hdnFlag")==null?"0":request.getParameter("hdnFlag"));
		String strFlag=(request.getParameter("hdnFlag")==null?"1":request.getParameter("hdnFlag"));

    PortalSessionBean objPortalSesBean = null;

    try{
      objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
      
      if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
        throw new Exception("La sesión del usuario ha expirado por favor vuelva a registrarse. Inicie Login");
            
         objHashMap =null;  
         objHashMap = getParameterNames(request);			
         HashMap hshRetorno = objBillAccService.delBillAccount(objHashMap,objPortalSesBean);   
         HashMap hshRetorno1 = objBillAccService.delBillAccountAssign(objHashMap,objPortalSesBean);   
         strOrderId=(String)hshRetorno.get("strOrderId");
         strCustomerId=(String)hshRetorno.get("strCustomerId");
         strSiteId=(String)hshRetorno.get("strSiteId");
         strMessage=(String)hshRetorno.get("strMessage");
		 if(logger.isDebugEnabled()){
			logger.debug(" -------------------- INICIO EdittOrderServlet.java / doDeleteBillAccount ---------------------- ");
			//logger.debug("objHashMap-->"+objHashMap);
			logger.debug("strFlag-->"+strFlag);
			logger.debug("strSessionId-->"+strSessionId);
			logger.debug("strOrderId-->"+strOrderId);
			logger.debug("strCustomerId-->"+strCustomerId);
			logger.debug("strSiteId-->"+strSiteId);
			logger.debug("strSpecificationId: "+strSpecificationId);
			logger.debug("strMessage-->"+strMessage);
			logger.debug(" -------------------- FIN EdittOrderServlet.java / doDeleteBillAccount ---------------------- ");
		 }		 
      }catch(Exception e){
         //e.printStackTrace();
         strMessage = e.getMessage();
      }
	  //INICIO CEM - COR0354
      //RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strUserId+"&nOrigen=2&sPage=BillAcc&sMensaje="+strMessage);
	  //RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strUserId+"&nOrigen=2&sPage=BillAcc&sMensaje="+strMessage+"&pCustomerId="+strCustomerId+"&pOrderId="+strOrderId+"&pSiteId="+strSiteId);	  
	  //RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strUserId+"|pSpecificationId="+strSpecificationId+"&nOrigen=2&sPage=BillAcc&sMensaje="+strMessage+"&pCustomerId="+strCustomerId+"&pOrderId="+strOrderId+"&pSiteId="+strSiteId);
	  RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strSessionId+"|pSpecificationId="+strSpecificationId+"|nFlag="+strFlag+"&nOrigen=2&sPage=BillAcc&sMensaje="+strMessage+"&pCustomerId="+strCustomerId+"&pOrderId="+strOrderId+"&pSiteId="+strSiteId);
	  
	  //FIN CEM - COR0354
      rd.forward(request,response);  
   }    
       
   /**
   * Motivo: Carga Combo Provincia y Distrito
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 19/09/2007 
   * @param     request     
   * @param     response             
   */         
   public void doLoadUbigeo(HttpServletRequest request, 
            HttpServletResponse response)
   throws ServletException, IOException{           
   
      int iTipo=(request.getParameter("nTipo")==null?-1:Integer.parseInt(request.getParameter("nTipo")));
      String strDptoId=(request.getParameter("sDepId")==null?"00":request.getParameter("sDepId"));
      String strProvId=(request.getParameter("sProvId")==null?"00":request.getParameter("sProvId"));
      String strCodigo=(request.getParameter("sCodName")==null?"nombre":request.getParameter("sCodName"));
      String strMessage=null;
      // 1: Carga Provincia  2: Carga Distrito 
      System.out.println(" -------------------- INICIO EdittOrderServlet.java / loadUbideo ---------------------- ");
      System.out.println("iTipo-->"+String.valueOf(iTipo));
      System.out.println("strDepId-->"+strDptoId);
      System.out.println("strProvId-->"+strProvId);
      System.out.println(" -------------------- FIN EdittOrderServlet.java / loadUbideo ---------------------- ");      
      GeneralService objGeneralS=new GeneralService();     
      
      ArrayList arrLista=new ArrayList();
      HashMap hshData=new HashMap();
      if (iTipo==1){
         hshData=objGeneralS.getUbigeoList(strDptoId,null,String.valueOf(iTipo));
         strMessage=(String)hshData.get("strMessage");
         arrLista=(ArrayList)hshData.get("arrListado");
      }else if (iTipo ==2){
         hshData=objGeneralS.getUbigeoList(strDptoId,strProvId,String.valueOf(iTipo));
         strMessage=(String)hshData.get("strMessage");
         arrLista=(ArrayList)hshData.get("arrListado");
      }
      request.setAttribute("listUbigeo",arrLista);          
      RequestDispatcher rd=request.getRequestDispatcher("GENERALPAGE/PrintUbigeo.jsp?sCodName="+strCodigo+"&sMensaje="+strMessage);        
      rd.forward(request,response);  
   }    
   
   /**
   * Motivo: Carga Combo Provincia y Distrito
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 19/10/2007 
   * @param     request     
   * @param     response             
   */         
   public void doLoadUbigeo2(HttpServletRequest request, 
         HttpServletResponse response)
   throws ServletException, IOException{           
   
      int iTipo=(request.getParameter("nTipo")==null?-1:Integer.parseInt(request.getParameter("nTipo")));
      int iDptoId=(request.getParameter("sDepId")==null?0:MiUtil.parseInt(request.getParameter("sDepId")));
      int iProvId=(request.getParameter("sProvId")==null?0:MiUtil.parseInt(request.getParameter("sProvId")));
      int iDistId=(request.getParameter("sDistId")==null?0:MiUtil.parseInt(request.getParameter("sDistId")));
      
      String strCodigo=(request.getParameter("sCodName")==null?"nombre":request.getParameter("sCodName"));
      String strMessage="";
      System.out.println(" -------------------- INICIO EdittOrderServlet.java / loadUbideo2 ---------------------- ");      
      // 1: Carga Provincia  2: Carga Distrito 
      System.out.println("iTipo-->"+String.valueOf(iTipo));
      System.out.println("iDptoId-->"+iDptoId);
      System.out.println("iProvId-->"+iProvId);
      System.out.println("iDistId-->"+iDistId);
      
      System.out.println(" -------------------- FIN EdittOrderServlet.java / loadUbideo2 ---------------------- ");      
      GeneralService objGeneralS=new GeneralService();     
      //getUbigeoList(int iDptoId,int iProvId,String sFlag)
      HashMap hshResultado=null;
      ArrayList arrLista=new ArrayList();
      
      if (iTipo==1){             
         hshResultado=objGeneralS.getUbigeoList(iDptoId,0,String.valueOf(iTipo));
         strMessage=(String)hshResultado.get("strMessage");
         if (strMessage==null)                
            arrLista=(ArrayList)hshResultado.get("arrUbigeoList");
      }else if (iTipo ==2){
         hshResultado=objGeneralS.getUbigeoList(iDptoId,iProvId,String.valueOf(iTipo));             
         strMessage=(String)hshResultado.get("strMessage");
         if (strMessage==null)                
            arrLista=(ArrayList)hshResultado.get("arrUbigeoList");             
      }      
      
      request.setAttribute("listUbigeo",arrLista); 
      
      RequestDispatcher rd=request.getRequestDispatcher("GENERALPAGE/PrintUbigeo.jsp?sCodName="+strCodigo+"&sMensaje="+strMessage+"&iDptoId="+iDptoId+"&iProvId="+iProvId+"&iDistId="+iDistId);        
      rd.forward(request,response);  
   }   
   
   /**
   * Motivo: Valida el voucher
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 13/09/2007 
   * @param     request     
   * @param     response             
   */         
   public void doValidateVoucher(HttpServletRequest request, 
           HttpServletResponse response)
   throws ServletException, IOException{    
   
      EditOrderService objOrderS=new EditOrderService();
      int iBank=(request.getParameter("idBank")==null?0:Integer.parseInt(request.getParameter("idBank")));
      String strRuc=(request.getParameter("nroRuc")==null?"":request.getParameter("nroRuc"));
      String strNroOper=(request.getParameter("nroVoucher")==null?"":request.getParameter("nroVoucher"));
      String strFecha=(request.getParameter("FechaPago")==null?"":request.getParameter("FechaPago"));
      strFecha=strFecha.trim();
      String strMessage=null;
      
      System.out.println(" -------------------- INICIO EdittOrderServlet.java / doValidateVoucher ---------------------- ");      
      System.out.println("iBank-->"+iBank);
      System.out.println("strRuc-->"+strRuc);
      System.out.println("strNroOper-->"+strNroOper);
      System.out.println("strFecha-->"+strFecha);
      
      System.out.println(" -------------------- FIN EdittOrderServlet.java / doValidateVoucher ---------------------- ");      
      
      String strCodeService=Constante.CODE_SERVICE;
      HashMap hshResult=new HashMap();
      
      /* an_bank_code            IN NUMBER,
      av_codigo_deudor        IN VARCHAR2, ruc
      av_numero_operacion     IN VARCHAR2,nro de voucher
      an_importe_pagado       OUT NUMBER,
      ad_fecha_pago           IN DATE,fecha
      an_importe_disponible   OUT NUMBER,
      an_status               OUT NUMBER,
      av_message              OUT VARCHAR2*/
      
      /* hshMap.put("status","1");
      hshMap.put("impDisp","1000");
      hshMap.put("impPag","800");     
      hshMap.put("fecha","15/09/2007");*/
      
      String strStatus= "0";
      String strImpDisp=null;
      String strImpPagado=null;
      
      hshResult=objOrderS.getBankPaymentDet(iBank,strCodeService,strRuc,strNroOper,strFecha);
      strMessage= (String)hshResult.get("strMessage");
      if (strMessage==null){          
         strStatus="1";
         strImpDisp = MiUtil.getString((String)hshResult.get("lImportDispon"));
         strImpPagado = MiUtil.getString((String)hshResult.get("lImportPay"));

      }
   
      RequestDispatcher rd=request.getRequestDispatcher("PAGEEDIT/ValidateVoucher.jsp?nStatus="+strStatus+"&nImpDisp="+strImpDisp+"&nImpPagado="+strImpPagado+"&sFecha="+strFecha+"&sMensaje="+strMessage);
      rd.forward(request,response);  
   }      

   /**
   * Motivo: Modifica el campo vendedor de las ordenes utilizando Reglas de Ordenes 
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 14/09/2007 
   * @param     request     
   * @param     response             
   */         
   public void doValidateSalesMan(HttpServletRequest request, 
               HttpServletResponse response)
   throws ServletException, IOException{ 
      
      long lCustomerId=(request.getParameter("an_swcustomerid")==null?0:Long.parseLong(request.getParameter("an_swcustomerid")));
      int iSiteId=(request.getParameter("an_swsiteid")==null?0:Integer.parseInt(request.getParameter("an_swsiteid")));
      int iVendedorId=(request.getParameter("an_vendedorid")==null?0:Integer.parseInt(request.getParameter("an_vendedorid")));
      String strVendedorName=(request.getParameter("av_vendedorname")==null?"":request.getParameter("av_vendedorname"));
      int iSpacialtyId=(request.getParameter("an_swspecialtyid")==null?0:Integer.parseInt(request.getParameter("an_swspecialtyid")));
      String strAlcExclusivity=(request.getParameter("an_alcExclusivity")==null?"":request.getParameter("an_alcExclusivity"));
      int iUnknwnSiteId =(request.getParameter("an_unknwnSiteId")==null?0:Integer.parseInt(request.getParameter("an_unknwnSiteId")));
      int iUserId =(request.getParameter("iUserId")==null?0:Integer.parseInt(request.getParameter("iUserId")));
      int iAppId =(request.getParameter("iAppId")==null?0:Integer.parseInt(request.getParameter("iAppId")));
      
      //Campos que vendran de la sesion   
      String strUserId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
      System.out.println("strUserId-->"+strUserId);
      
      PortalSessionBean objPortalSesBean = null;
      objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strUserId);     
      String strLogin= objPortalSesBean.getLogin();//"DTEODOSIO"; (pSesion.getAttribute("login")==null?"":(String)pSesion.getAttribute("login"));
      
      HashMap hshData=null;
      String strMsgValidation =null;
      String strMessage="";      
      String strDealer="";
      int iSellerRegionId=-1; 
      int iAccion=-1;
      int  iOption=-1;
      EditOrderService objOrderS=new EditOrderService();
      
      // PROCEDURE PL_CHANGE_SALESMAN     
      strMsgValidation= objOrderS.doValidateSalesName(lCustomerId,iSiteId,iSpacialtyId,strLogin,iVendedorId,strVendedorName,iUserId,iAppId);
      System.out.println("strMsgValidation-->"+strMsgValidation);
      if (strMsgValidation!=null){          
         iAccion=1;         
         RequestDispatcher rd=request.getRequestDispatcher("PAGEEDIT/ValidateSalesMan.jsp?nAction="+iAccion+"&sMsgError="+strMsgValidation);
         rd.forward(request,response);  
      }else{              
         // obtengo la region del consultor que ingresa la orden NP_ORDERS02_PKG.SP_GET_VENDEDOR_REGION  
         System.out.println("iVendedorId: "+iVendedorId);
         hshData=objOrderS.getVendedorRegionId(iVendedorId);
         strMessage =(String)hshData.get("strMessage");
         iSellerRegionId=MiUtil.parseInt((String)hshData.get("iRegionSellerId"));
         iAccion=2;
         
         if (strMessage!=null){                                
            iAccion=3;                  
         }                
            
         int iFlag=0; 
         hshData= objOrderS.getOppOwnershipChngFlag(lCustomerId,iSiteId,strAlcExclusivity, iVendedorId);
         strMessage =(String)hshData.get("strMessage");
         iFlag=MiUtil.parseInt((String)hshData.get("iFlag"));
         if (iFlag!=-1){
            if (iFlag== 1){ 
               strDealer=  MiUtil.getString(objOrderS.getDealer(iVendedorId));                     
               iOption=1;                    
            }else{
               strDealer=  MiUtil.getString(objOrderS.getDealer(iVendedorId));   
               iOption=2;                 
            } 
         }else{      
            iOption=3; 
         }
         RequestDispatcher rd=request.getRequestDispatcher("PAGEEDIT/ValidateSalesMan.jsp?nAction="+iAccion+"&sDealer="+strDealer+"&nSellerRegionId="+iSellerRegionId+"&sMsgError="+strMsgValidation+"&sMensaje="+strMessage);
         rd.forward(request,response);  
      }                  
   }

   /**
   * Motivo: Genera un Documento
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 16/11/2007 
   * @param     request     
   * @param     response             
   */         
  public void doGenerateDocument(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{    
      
    HashMap hshResult=new HashMap();
    String strMessage = null;   
    int iErrorNumber=-1;
    long lOrderId=(request.getParameter("hdnOrderId")==null?0:MiUtil.parseLong(request.getParameter("hdnOrderId")));
    String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
      
    String strOrigen=Constante.NAME_ORIGEN_FFPEDIDOS;
    String strLogin=null;
    int iBuildingId=0;      
      
    PortalSessionBean objPortalSesBean = null;

    try{
      objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
      
      if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
        throw new Exception("La sesión del usuario ha expirado por favor vuelva a registrarse. Inicie Login");
      
      strLogin    = objPortalSesBean.getLogin();
      iBuildingId = objPortalSesBean.getBuildingid();       
      
                  
         HashMap hshRetorno = objOrderService.generateDocumentInvBill(lOrderId,strOrigen,strLogin,iBuildingId);   
         iErrorNumber = MiUtil.parseInt((String)hshRetorno.get("strError"));         
         strMessage=(String)hshRetorno.get("strMessage");
      
      }catch(Exception e){
         //e.printStackTrace();
         strMessage = e.getMessage();
      }         

      System.out.println("-----------------Inicio EditOrderServlet.java / doGenerateDocument-------------------------");      
      System.out.println("lOrderId-->"+lOrderId);
      System.out.println("strSessionId-->"+strSessionId);
      System.out.println("strLogin-->"+strLogin);
      System.out.println("iBuildingId-->"+iBuildingId);
      System.out.println("iErrorNumber-->"+iErrorNumber);      
      System.out.println("strMessage-->"+strMessage);    
      System.out.println("strOrigen-->"+strOrigen);          
      System.out.println("-----------------Fin EditOrderServlet.java / doGenerateDocument-------------------------");      
      
      hshResult.put("strOrderId",lOrderId+"");
      hshResult.put("strMessage",strMessage); 
      hshResult.put("strErrorNumer",iErrorNumber+"");       
      hshResult.put("strTipoOrigen","GenerateDocument");      
      
      request.setAttribute("objResultado",hshResult);      
      RequestDispatcher rd=request.getRequestDispatcher("PAGEEDIT/ResultEdit.jsp");
      rd.forward(request,response);  
   }       
   
   
      /**
   * Motivo: Genera un Documento (Factura desde el detalle)
   * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
   * <br>Fecha: 16/11/2007 
   * @param     request     
   * @param     response             
   */         
   public void doGenerateDocumentDetail(HttpServletRequest request, 
               HttpServletResponse response)
   throws ServletException, IOException{    
      
    HashMap hshResult=new HashMap();
    String strMessage = null;   
    int iErrorNumber=-1;
    long lOrderId=(request.getParameter("hdnOrderId")==null?0:MiUtil.parseLong(request.getParameter("hdnOrderId")));
    String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
           
    String strOrigen=Constante.NAME_ORIGEN_FFPEDIDOS;
    String strLogin = null;
    int iBuildingId = 0;
      
    PortalSessionBean objPortalSesBean = null;

    try{
      objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
      
      if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
        throw new Exception("La sesión del usuario ha expirado por favor vuelva a registrarse. Inicie Login");
      
      strLogin    = objPortalSesBean.getLogin();
      iBuildingId = objPortalSesBean.getBuildingid();      
      
      HashMap hshRetorno = objOrderService.generateDocumentInvBillDetail(lOrderId,strOrigen,strLogin,iBuildingId);   
      iErrorNumber = MiUtil.parseInt((String)hshRetorno.get("strError"));         
      strMessage=(String)hshRetorno.get("strMessage");
      
    }catch(Exception e){
      //e.printStackTrace();
      strMessage = e.getMessage();
    }

    System.out.println("-----------------Inicio EditOrderServlet.java / doGenerateDocumentDetail-------------------------");      
    System.out.println("lOrderId-->"+lOrderId);
    System.out.println("strSessionId-->"+strSessionId);
    System.out.println("strLogin-->"+strLogin);
    System.out.println("iBuildingId-->"+iBuildingId);
    System.out.println("iErrorNumber-->"+iErrorNumber);      
    System.out.println("strMessage-->"+strMessage);    
    System.out.println("strOrigen-->"+strOrigen);          
    System.out.println("-----------------Fin EditOrderServlet.java / doGenerateDocument-------------------------");      
    
    hshResult.put("strOrderId",lOrderId+"");
    hshResult.put("strMessage",strMessage); 
    hshResult.put("strErrorNumer",iErrorNumber+"");       
    hshResult.put("strTipoOrigen","GenerateDocumentDetail");      

    request.setAttribute("objResultado",hshResult);      
    RequestDispatcher rd=request.getRequestDispatcher("PAGEEDIT/ResultEdit.jsp");
    rd.forward(request,response);  
  }       
  
   /**
   * Motivo: Genera una Orden de Pago
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 16/11/2007 
   * @param     request     
   * @param     response             
   */         
   public void doGeneratePayOrder(HttpServletRequest request, 
               HttpServletResponse response)
   throws ServletException, IOException{    
   
      HashMap hshResult=new HashMap();
      String strMessage = null;   
      int iErrorNumber=-1;
      long lOrderId=(request.getParameter("hdnOrderId")==null?0:MiUtil.parseLong(request.getParameter("hdnOrderId")));
      String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
       
    String strLogin   = null;
    int   iBuildingId = 0;
	  //String strNumPaymentOrderId=""; //CEM - COR0720
    PortalSessionBean objPortalSesBean = null;

    try{
      objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
      
      if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
        throw new Exception("La sesión del usuario ha expirado por favor vuelva a registrarse. Inicie Login");
              
      strLogin    = objPortalSesBean.getLogin();
      iBuildingId = objPortalSesBean.getBuildingid();  
    
      HashMap hshRetorno = objOrderService.generatePayamentOrder(lOrderId,strLogin,iBuildingId);   
      iErrorNumber = MiUtil.parseInt((String)hshRetorno.get("strError"));         
      strMessage=(String)hshRetorno.get("strMessage");           
    }catch(Exception e){
      strMessage = e.getMessage();
    }
     
    System.out.println("-----------------Inicio EditOrderServlet.java / doGeneratePayOrder-------------------------");      
    System.out.println("lOrderId-->"+lOrderId);
    System.out.println("strSessionId-->"+strSessionId);
    System.out.println("strLogin-->"+strLogin);
    System.out.println("iBuildingId-->"+iBuildingId);
    System.out.println("iErrorNumber-->"+iErrorNumber);      
    System.out.println("strMessage-->"+strMessage);      
    System.out.println("-----------------Fin EditOrderServlet.java / doGeneratePayOrder-------------------------");      

    hshResult.put("strMessage",strMessage); 
    hshResult.put("strErrorNumer",iErrorNumber+"");       
    hshResult.put("strTipoOrigen","Otros");      
  //hshResult.put("strAddItemPayOrder",strNumPaymentOrderId); // CEM - COR0720	  
    
    request.setAttribute("objResultado",hshResult); 
    RequestDispatcher rd=request.getRequestDispatcher("PAGEEDIT/ResultEdit.jsp");
    rd.forward(request,response);  
  }          
   

   /**
   * Motivo: Verifica 
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 16/11/2007 
   * @param     request     
   * @param     response             
   */         
  public void doParteIngreso(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{    
   
    HashMap hshResult=new HashMap();
    String strMessage = null;    
    String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
    String strPIType=(request.getParameter("hdnPIType")==null?"0":request.getParameter("hdnPIType")); 
    long lOrderId=(request.getParameter("hdnOrderId")==null?0:MiUtil.parseLong(request.getParameter("hdnOrderId")));
    String strOrderId = (request.getParameter("hdnOrderId")==null?"0":request.getParameter("hdnOrderId"));
    System.out.println("strSessionId-->"+strSessionId);
    //Inicio JNINO - 09/02/2011 
    String strSepecificationId = (request.getParameter("hdnSubCategoria")==null?"0":request.getParameter("hdnSubCategoria"));      
    //Fin JNINO - 09/02/2011 
    
    String strLogin = null;
    long lLoginBuilding = 0;
    int iErrorNumber=0;
    
    HashMap hshData =new HashMap();
    
    PortalSessionBean objPortalSesBean = null;

    try{
      objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
      
      if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
        throw new Exception("La sesión del usuario ha expirado por favor vuelva a registrarse. Inicie Login");
      
      strLogin = objPortalSesBean.getLogin();
      lLoginBuilding = objPortalSesBean.getBuildingid();       
      
      //Inicio  JNINO - 09/02/2011, Ordenes devolución DEMO - TEST    
      if (strSepecificationId.equals("2077") || strSepecificationId.equals("2078")){
          int iUserId = objPortalSesBean.getUserid();                  
          String strUserId= Integer.toString(iUserId);
            
          EditOrderService objOrderServiceTransaction = new EditOrderService();
          HashMap objHashMap = objOrderServiceTransaction.doGetEquipmentStatus(lOrderId,strUserId); // ,lOrderId0          
          String strResult = "";
          strResult = (String) objHashMap.get("strResult");
          strMessage = (String) objHashMap.get("strMessage");          

          System.out.println("strMessage : "+ strMessage);
          System.out.println("strResult : "+ strResult);          
            
          if (strResult.equals("Activo")){
             iErrorNumber = 2;                                    
             strMessage = "Verifique que los Items no se encuentren en estado: " + strResult ;             
          }
          
          if (strMessage == null || strMessage == ""){
      hshData = objOrderService.doGenerarParteIngreso(lOrderId,Constante.NAME_ORIGEN_FFPEDIDOS,strLogin,lLoginBuilding,strPIType);            
      iErrorNumber=MiUtil.parseInt((String)hshData.get("strError"));
      strMessage =(String)hshData.get("strMessage");
          }
       //Fin  JNINO - 09/02/2011          
      }else{
          hshData = objOrderService.doGenerarParteIngreso(lOrderId,Constante.NAME_ORIGEN_FFPEDIDOS,strLogin,lLoginBuilding,strPIType);            
          iErrorNumber=MiUtil.parseInt((String)hshData.get("strError"));
          strMessage =(String)hshData.get("strMessage");
      }      
      
    }catch(Exception e){
      strMessage = e.getMessage();
    }  
    
    System.out.println("-----------------Inicio EditOrderServlet.java / doParteIngreso-------------------------");      
    System.out.println("lOrderId-->"+lOrderId);
    System.out.println("strSessionId-->"+strSessionId);
    System.out.println("strMessage-->"+strMessage);  
    System.out.println("iErrorNumber-->"+iErrorNumber);        
    System.out.println("-----------------Fin EditOrderServlet.java / doParteIngreso-------------------------");      
 
    hshResult.put("strOrderId",strOrderId);
    hshResult.put("strMessage",strMessage); 
    hshResult.put("strErrorNumer",iErrorNumber+"");       
    hshResult.put("strTipoOrigen","ParteIngreso");       
    hshResult.put("strPIType",strPIType);      
    request.setAttribute("objResultado",hshResult); 
    RequestDispatcher rd=request.getRequestDispatcher("PAGEEDIT/ResultEdit.jsp");
    rd.forward(request,response);  
   }       

   /**
   * Motivo: AutorizacionDevolucion
   * <br>Realizado por: <a href="mailto:edgar.jara@nextel.com.pe">Edgar Jara</a>
   * <br>Fecha: 29/08/2007 
   * @param     request     
   * @param     response             
   */         
   public void doAutorizacionDevolucion(HttpServletRequest request, 
               HttpServletResponse response)
   throws ServletException, IOException{    
   
      HashMap hshResult=new HashMap();
      String strMessage = null;  
      String strSuccessMessage= null; 
      PortalSessionBean objPortalSesBean = null;
      String strUserId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
      long lOrderId=(request.getParameter("hdnOrderId")==null?0:MiUtil.parseLong(request.getParameter("hdnOrderId")));
      String strOrderId = (request.getParameter("hdnOrderId")==null?"0":request.getParameter("hdnOrderId"));
      System.out.println("strUserId-->"+strUserId);
      
      //PRY-0890 JCURI
      String strSpecificationId = (request.getParameter("hdnSubCategoria")==null?"":request.getParameter("hdnSubCategoria"));
      String strActionName=(request.getParameter("cmbAction")==null?"":request.getParameter("cmbAction"));
      String strCurrentInbox=(request.getParameter("txtEstadoOrden")==null?"":request.getParameter("txtEstadoOrden"));
      long lCustomerId = (request.getParameter("txtCompanyId")==null?0:MiUtil.parseLong(request.getParameter("txtCompanyId")));
      objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strUserId);
      String strLogin = objPortalSesBean.getLogin(); 
      System.out.println("doAutorizacionDevolucion [strSpecificationId] "+strSpecificationId + " [strCurrentInbox] " +strCurrentInbox + " [strActionName] " + strActionName + " [lCustomerId] " + lCustomerId + " [strUser] " + strLogin);
      
      HashMap hshData =new HashMap();
      HashMap hshDataPA =new HashMap();
      try{        
         hshData = objOrderService.doAutorizacionDevolucion(lOrderId);            
         strMessage =(String)hshData.get("strMessage");
         strSuccessMessage =(String)hshData.get("strSuccesMessage");
      
         //PRY-0890 JCURI
        	 try {
        		 System.out.println("generateOrderPAExtorno [INI]");
				 hshDataPA = objOrderService.generateOrderPAExtorno(lOrderId, lCustomerId, strSpecificationId, strCurrentInbox, strLogin);
        		 System.out.println("generateOrderPAExtorno [FIN] " + hshDataPA.get("strMessage"));
        		 
        		 if(hshDataPA.get("strMessage")!=null) {
    			 strSuccessMessage = strSuccessMessage + "\n" +(String) hshDataPA.get("strMessage");
        		 }
			} catch (Exception e) {
				System.out.println("generateOrderPAExtorno [ERROR]" + e.toString());
				strSuccessMessage = strSuccessMessage + " - [PA] " + e.toString();
			}
      
      }catch(Exception e){
         //e.printStackTrace();
         strMessage = e.getMessage();
          System.out.println("catch" + e.getMessage() );
      }         
      System.out.println("-----------------Inicio EditOrderServlet.java / doAutorizacionDevolucion-------------------------");      
      System.out.println("lOrderId-->"+lOrderId);   
      System.out.println("-----------------Fin EditOrderServlet.java / doAutorizacionDevolucion-------------------------");      
      System.out.println("strmessage : " + strMessage);         
      hshResult.put("strOrderId",strOrderId);
      hshResult.put("strMessage",strMessage); 
      hshResult.put("strSuccessMessage",strSuccessMessage); 
      hshResult.put("strTipoOrigen","autorizacionDevolucion");       
      
      
      request.setAttribute("objResultado",hshResult); 
      RequestDispatcher rd=request.getRequestDispatcher("PAGEEDIT/ResultEdit.jsp");
      rd.forward(request,response);  
   }      


   /**
   * Motivo: Carga en un listado de Inboxs a los que se puede avanzar
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 07/12/2007 
   * @param     request     
   * @param     response             
   */         
   public void doLoadInboxSaltar(HttpServletRequest request,HttpServletResponse response)
   throws ServletException, IOException{           
   
      int iSpecificationId=(request.getParameter("hdnSubCategoria")==null?0:Integer.parseInt(request.getParameter("hdnSubCategoria")));      
      String strCurrentInbox=(request.getParameter("txtEstadoOrden")==null?"":request.getParameter("txtEstadoOrden"));
      String strComboName=(request.getParameter("strComboName")==null?"":request.getParameter("strComboName"));
      String strFormName=(request.getParameter("strFormName")==null?"":request.getParameter("strFormName"));
      String strValorName=(request.getParameter("strValorName")==null?"":request.getParameter("strValorName"));
      String strDescripcionName=(request.getParameter("strDescripcionName")==null?"":request.getParameter("strDescripcionName"));
      String strMessage=null;
      HashMap hshResult=new HashMap();      
           
      HashMap hshResultado=null;
      ArrayList arrLista=new ArrayList();

      try{
         hshResultado=objOrderService.getSpecificationStatus(iSpecificationId,strCurrentInbox);
         strMessage=(String)hshResultado.get("strMessage");
         if (strMessage==null)
            arrLista=(ArrayList)hshResultado.get("objLista");     //inbox
      
      }catch(Exception e){         
         strMessage = e.getMessage();
      }        

      System.out.println(" -------------------- INICIO EditOrderServlet.java / doLoadInboxSaltar ---------------------- ");            
      System.out.println("iSpecificationId-->"+iSpecificationId);
      System.out.println("strCurrentInbox-->"+strCurrentInbox);      
      System.out.println("strComboName-->"+strComboName);      
      System.out.println("strFormName-->"+strFormName);      
      System.out.println("strValorName-->"+strValorName);      
      System.out.println("strDescripcionName-->"+strDescripcionName);      
      System.out.println("arrListado-->"+arrLista);      
      System.out.println(" -------------------- FIN EditOrderServlet.java / doLoadInboxSaltar ---------------------- ");       
      
      hshResult.put("strComboName",strComboName);       
      hshResult.put("strFormName",strFormName);       
      hshResult.put("strValorName",strValorName);       
      hshResult.put("strDescripcionName",strDescripcionName);       
      hshResult.put("strMessage",strMessage);       
      hshResult.put("objLista",arrLista);        
      request.setAttribute("objResultado",hshResult);       
      RequestDispatcher rd=request.getRequestDispatcher("PAGEEDIT/PrintCombo.jsp");
      rd.forward(request,response);  
   }      
   
   /**
   * @see GenericServlet#executeDefault(HttpServletRequest, HttpServletResponse)
   */
   public void executeDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                                                                                    IOException {
      try {
         logger.debug("Antes de hacer el sendRedirect");
         response.sendRedirect("/portal/page/portal/orders/RETAIL_NEW");
      } catch (Exception e) {
         logger.error(formatException(e));
      }
   }     
 
  /***********************************************************************
   ***********************************************************************
   ***********************************************************************
   *  INTEGRACION DE ORDENES Y RETAIL - FIN
   *  REALIZADO POR: Carmen Gremios
   *  FECHA: 13/09/2007
   ***********************************************************************
   ***********************************************************************
   ***********************************************************************/     
   
      /***********************************************************************
   ***********************************************************************
   ***********************************************************************
   *  APROBACION DE EXCEPCIONES - INICIO
   *  REALIZADO POR: Jorge Pérez
   *  FECHA: 09/12/2007
   ***********************************************************************
   ***********************************************************************
   ***********************************************************************/     
 
   /**
   * Motivo: Actualiza las Excepciones (Aprueba o desaprueba) 
   * <br>Realizado por: <a href="mailto:jorge.perez@nextel.com.pe">Jorge Pérez</a>
   * <br>Fecha: 09/12/2007 
   * @throws java.io.IOException
   * @throws javax.servlet.ServletException
   * @param response
   * @param request
   */ 
 
   public void doUpdateException(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException {
      System.out.println("=============en doUpdateException================");
      HashMap hshResult           = new HashMap();
      HashMap hashData            = new HashMap(); 
      HashMap hshOrder            = new HashMap();  
      HashMap hshSpecification    = new HashMap();  
      HashMap hshExceptionApprove = new HashMap();
      GeneralService objGeneralService     = new GeneralService();      
      SessionService objSessionService     = new SessionService();    
      EditOrderService objOrderEditService = new EditOrderService();
      String strUserId  = "";
      String strExceptionStatus = "";
      CustomerBean csbCustomer = null;      
      System.out.println("hdnUserId===="+request.getParameter("hdnUserId"));
      System.out.println("hndnppedidoid===="+request.getParameter("hndnppedidoid"));
      String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
      System.out.println("sessionId === " + strSessionId);
      strUserId = request.getParameter("hdnUserId");
      PrintWriter out = response.getWriter();      
      int iErrorNumber=-1;            
      String strOrderId = "";            
		String strDivisionId = "";
      String strNpBpelconversationid = "";
      String strActionName = "";
      String strNextInboxName = "";
      String strNextStatus = "";
      String strInboxName = "";
      String strInboxId = "";
      String strUrl        = "";
      StringBuffer strOutHTML = new StringBuffer();
      SpecificationBean objSpecificationBean=null;
      OrderBean objOrder = new OrderBean();
            
      int    iStatus  = -1;   
      String strStatus = null;
      String strMessage = null;
      String strOldInbox = "";
    
    PortalSessionBean objPortalSesBean = null;
    RequestHashMap objHashMap =  new RequestHashMap();
    try{
      objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
      
      if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
        throw new Exception("La sesión del usuario ha expirado por favor vuelva a registrarse. Inicie Login");
        
      
    
        
        long lLoginBuilding = objPortalSesBean.getBuildingid();
        System.out.println("lLoginBuilding === " + lLoginBuilding);
        System.out.println("getCreatedby === " + objPortalSesBean.getCreatedby());
        System.out.println("getSessionID === " + objPortalSesBean.getSessionID());
        System.out.println("getUserid === " + objPortalSesBean.getUserid());
        objHashMap = getParameterNames(request);
        objHashMap.put("hdnSessionLogin",strUserId);        
        System.out.println("antes de llamar a updateExceptionApprove");
        System.out.println("objHashMap==="+objHashMap);
        hshResult = objGeneralService.updateExceptionApprove(objHashMap);
        System.out.println("luego de llamar a updateExceptionApprove");
        System.out.println("hshResult==="+hshResult);
        if (hshResult != null) {
          strMessage = (String)hshResult.get("strMessage");
                  
          if (strMessage != null){
            System.out.println("strMessage==="+strMessage);
            throw new Exception(strMessage);
          }
                  
          iStatus   = MiUtil.parseInt((String)hshResult.get("status"));
          if (iStatus != -1){          
           //Obtiene los datos de la Orden
           strOrderId = request.getParameter("hndnppedidoid");
           hshOrder = objOrderEditService.getOrder(MiUtil.parseLong(strOrderId));        
           strMessage = (String)hshOrder.get("strMessage");         
           objOrder   = (OrderBean)hshOrder.get("objResume");
           strInboxName = request.getParameter("hndinboxname");
           strInboxId   = request.getParameter("hndinboxid");
                      
           if (strMessage != null){          
            throw new Exception(strMessage);
           } 
           objOrder   = (OrderBean)hshOrder.get("objResume");
           
           if (iStatus == 0 || iStatus == 1){
             strMessage=(String)hshResult.get("strMessage"); //LMM SAR_0037-186400
             strNextStatus = (String)hshResult.get("strNextInbox"); //LMM SAR_0037-186400
             strOldInbox = (String)hshResult.get("strOldInbox"); //LMM SAR_0037-186400
             if (strMessage!=null)
              throw new Exception(strMessage);                          
             
             System.out.println("strNextInbox==="+strNextStatus+" strOldInbox: "+strOldInbox);           
             hshExceptionApprove.put("strStatus", MiUtil.getString(""+iStatus));
             hshExceptionApprove.put("strOrderId", strOrderId);           
             hshExceptionApprove.put("strInboxId", strInboxId);
             hshExceptionApprove.put("strInboxName", strOldInbox);
             
             System.out.println("strStatus "+ iStatus);
             System.out.println("strOrderId "+ strOrderId);
             System.out.println("strInboxId "+ strInboxId);
             System.out.println("strInboxName "+ strInboxName);
             
             //Procesos post-bpel
             
             System.out.println("====Procesos post-BPEL======");           
             hshResult=objOrderService.doExecuteActionFromOrder(MiUtil.parseLong(strOrderId),strOldInbox,strNextStatus,strUserId,lLoginBuilding);
             iErrorNumber = MiUtil.parseInt((String)hshResult.get("strError"));         
             strMessage=(String)hshResult.get("strMessage");          
             if (strMessage != null){ 
                System.out.println("Luego de procesos post-bpel==="+strMessage);
                strMessage = "Hubo un error al enviar el XML a Ingeniería: "+strMessage;
                hshExceptionApprove.put("strXMLMessage", strMessage);
             }   
             else
                hshExceptionApprove.put("strXMLMessage", null);
           }else{
              //Caso en que la excepción fue aprobada pero la orden aún tiene excepciones pendientes  .
              System.out.println("===La orden aún tiene excepciones pendientes===");
              hshExceptionApprove.put("strStatus", MiUtil.getString(""+iStatus));
              hshExceptionApprove.put("strOrderId", strOrderId);
              hshExceptionApprove.put("strInboxId", strInboxId);
              hshExceptionApprove.put("strInboxName", strOldInbox);
           }
           
        }else{
           hshExceptionApprove.put("strStatus", MiUtil.getString(""+iStatus));
           hshExceptionApprove.put("strOrderId", strOrderId);                                               
           System.out.println("strStatus "+ iStatus);
           System.out.println("strOrderId "+ strOrderId);                        
        }

          request.setAttribute("objResultado",hshExceptionApprove); 
          RequestDispatcher rd=request.getRequestDispatcher("PAGEEDIT/ResultExceptionApprove.jsp");
          rd.forward(request,response);  
                     
        }        
                
      }catch(Exception e){
          e.printStackTrace();
          strMessage = e.getMessage();
          System.out.println("doUpdateException:-->"+strMessage);
          out.println("<script>");
          out.println("alert(' "+MiUtil.getMessageClean(strMessage)+"')");         
          out.println("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');");          
          out.println("</script>");
      }
    
    }
   
  /***********************************************************************
   ***********************************************************************
   ***********************************************************************
   *  APROBACION DE EXCEPCIONES - FIN
   *  REALIZADO POR: Jorge Pérez
   *  FECHA: 09/12/2007
   ***********************************************************************
   ***********************************************************************
   ***********************************************************************/   

   
   /**
   * Motivo: Reemplazar Equipos 
   * <br>Realizado por: <a href="mailto:fanny.najarro@nextel.com.pe">Fanny Najarro</a>
   * <br>Fecha: 02/06/2009 
   * @throws java.io.IOException
   * @throws javax.servlet.ServletException
   * @param response
   * @param request
   */ 
   public void doReplaceHandset(HttpServletRequest request,HttpServletResponse response)
   throws ServletException, IOException{ 
      //Logica para bpel_workflow.spi_gen_replace_simimei_order 
      HashMap hshResult=new HashMap();
      String strMessage = "";
      String strOrderId = (String)request.getParameter("strOrderId");
      String strUser    = (String)request.getParameter("strUserName");
      String strAppId   = (String)request.getParameter("appCode"); 
      String strSpecificationId= (String)request.getParameter("strSpecificationId"); 
      
      try{
          
          AutomatizacionService objAutomatizacionService = new AutomatizacionService();
          HashMap hshAutomatizacion = new HashMap();
          
          hshAutomatizacion = objAutomatizacionService.doReplaceHandset(Long.parseLong(strOrderId),strUser,strAppId);
          strMessage = (String)hshAutomatizacion.get("strMessage");
          System.out.println("Mensaje Activación IMEI/SIM -> "+strMessage);
                
      }catch(Exception e){
        strMessage = e.getMessage();       
        System.out.println("[doReplaceHandset][Capturado en el catch Exception]"+e.getMessage());
      }
     
      hshResult.put("strOrderId",strOrderId);
      hshResult.put("strMessage",strMessage); 
      hshResult.put("strTipoOrigen","replaceHandset"); 
      
      request.setAttribute("objResultado",hshResult);      
      RequestDispatcher rd=request.getRequestDispatcher("PAGEEDIT/ResultEdit.jsp");
      rd.forward(request,response);
      
   }
   

  /**
   * <br>Realizado por: <a href="mailto:jose.casas@nextel.com.pe">José Casas</a>  
   * <br>Fecha: 26/06/2009
   * <br>José Casas       26/06/2009  Creación   
   * @param     request     
   * @param     response
  */      
  public void doEvaluarOrden(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
      logger.info("****************** INICIO EditOrderServlet > doEvaluarOrden******************");
        try {
                Date fechaInicio = new Date();
                System.out.println("[EditOrderServlet] [doEvaluarOrden] [PM0010354] INCIO DEL METODO doEvaluarOrden");
                System.out.println("[EditOrderServlet] [doEvaluarOrden] [PM0010354] Fecha y Hora Inicio --> " + fechaInicio);
        }catch(Exception e) {
                System.out.println("[EditOrderServlet] [doEvaluarOrden] [PM0010354] Error LOG INICIO");
        }


    long lOrderId = 0;   
    String strMessage = null;    
    PortalSessionBean objPortalSesBean = null;
    CreditEvaluationService objCreditEvaluationService = new CreditEvaluationService();    

    HashMap hshCreditEvaluation = new HashMap();
    HashMap hshValidateCredit = new HashMap();
    RequestHashMap objHashMap =  new RequestHashMap();

    CreditEvaluationBean objCreditEvaluationBean = new CreditEvaluationBean();    
    ArrayList arrRuleResult = null;
      
    try{
      String strSessionId = (request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
      objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
      if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
        throw new Exception("La sesión del usuario ha expirado por favor vuelva a registrarse. Inicie Login");

      String strFlagValidateCredit = "0";
      lOrderId =(request.getParameter("hdnOrderId")==null?0:MiUtil.parseLong(request.getParameter("hdnOrderId")));
      hshValidateCredit = objCreditEvaluationService.doValidateCredit(lOrderId,"ORDER");
      if((String)hshValidateCredit.get("strMessage")!=null){
        throw new Exception(strMessage);
      }
      String strValidateCredit = (String)hshValidateCredit.get("flagValidateCredit");
      if(strValidateCredit.equals("1")) {    
        strFlagValidateCredit = "1";
        objHashMap = getParameterNames(request);
        objHashMap.put("hdnSessionLogin",objPortalSesBean.getLogin());
        objHashMap.put("hdnSessionUserid",objPortalSesBean.getUserid()+"");
        objHashMap.put("hdnSessionAppid",objPortalSesBean.getAppId()+"");         
        
        hshCreditEvaluation = objCreditEvaluationService.doEvaluateOrder(objHashMap);
        strMessage =  (String)hshCreditEvaluation.get("strMessage");
        if (strMessage!=null) throw new Exception(strMessage);
        
        String strOrderId = (String)objHashMap.getParameter("hdnOrderId");
        HashMap hshData = new HashMap();      
        hshData = (HashMap)objCreditEvaluationService.getCreditEvaluationData(MiUtil.parseLong(strOrderId),"ORDER");
        strMessage=(String)hshData.get("strMessage");
        if (strMessage!=null) throw new Exception(strMessage);
        objCreditEvaluationBean = (CreditEvaluationBean)hshData.get("objCreditEvaluationBean");

        hshData = (HashMap)objCreditEvaluationService.getRuleResult(objCreditEvaluationBean.getnpCreditEvaluationId(),"20");
        strMessage=(String)hshData.get("strMessage");
        if (strMessage!=null) throw new Exception(strMessage);
        arrRuleResult=(ArrayList)hshData.get("arrRuleResult");    
      }

      request.removeAttribute("objCreditEvaluationBean");
      request.setAttribute("objCreditEvaluationBean",objCreditEvaluationBean);
      request.removeAttribute("arrRuleResult");
      request.setAttribute("arrRuleResult",arrRuleResult);          
      request.removeAttribute("strFlagValidateCredit");
      request.setAttribute("strFlagValidateCredit",strFlagValidateCredit);      

      RequestDispatcher dispatcher = null;
      String messageError = null;
      try {
        dispatcher = getServletContext().getRequestDispatcher("/htdocs/jsp/Order_CreditEvaluation.jsp");

                try {
                    Date fechaFin = new Date();
                    System.out.println("[EditOrderServlet] [doEvaluarOrden] [PM0010354] Fecha y Hora Inicio --> " + fechaFin);
                    System.out.println("[EditOrderServlet] [doEvaluarOrden] [PM0010354] FIN DEL METODO doEvaluarOrden");
                }catch(Exception e) {
                    System.out.println("[EditOrderServlet] [doEvaluarOrden] [PM0010354] Error LOG FIN");
                }

        dispatcher.forward(request,response);
      } catch (ServletException e) {
        e.printStackTrace();
        messageError  = "[ServletException][OrderServlet][doEvaluarOrden]["+e.getClass()+" "+e.getMessage()+"]";
        throw new Exception(messageError);
      } catch (IOException e) {
        e.printStackTrace();
        messageError  = "[ServletException][IOException][doEvaluarOrden]["+e.getClass()+" "+e.getMessage()+"]";
        throw new Exception(messageError);
      } catch (Exception e) {
        e.printStackTrace();
        messageError  = "[ServletException][Exception][loadSpecificationsSections]["+e.getClass()+" "+e.getMessage()+"]";
        throw new Exception(messageError);
      }
    } catch(Exception e){
      strMessage = e.getMessage();
      System.out.println("[doUpdateOrder][Capturado en el catch Exception]"+e.getMessage());
    }
      logger.info("****************** FIN EditOrderServlet > doEvaluarOrden******************");
  }
  
  /**
   * Motivo: Obtener el Estado del Número de Teléfono.
   * <br>Realizado por: <a href="mailto:fanny.najarro@nextel.com.pe">Fanny Najarro</a>
   * <br>Fecha: 09/07/2009 
   * @throws java.io.IOException
   * @throws javax.servlet.ServletException
   * @param response
   * @param request
   */ 
  public void getStatusNumber(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException {
      PrintWriter out = response.getWriter();  
      String strPhoneNumber   = request.getParameter("phoneNumber")== null ? "" : request.getParameter("phoneNumber");
      String strAppId         = request.getParameter("strAppId")== null ? "" : request.getParameter("strAppId");
      String strOrderId       = request.getParameter("strOrderId")== null ? "" : request.getParameter("strOrderId");
      String strVal_i         = request.getParameter("strval_i")== null ? "" : request.getParameter("strval_i");
      String strVal_j         = request.getParameter("strval_j")== null ? "" : request.getParameter("strval_j");
      String strItemId        = request.getParameter("strItemId")== null ? "" : request.getParameter("strItemId");
      String strItemDeviceId  = request.getParameter("strItemDeviceId")== null ? "" : request.getParameter("strItemDeviceId");
      
      
      HashMap hshResult=new HashMap();
      String strMessage = "";
      String strStatusNumber  = "";
      String strBlockedNumber = "";
      String strAppCod  = "";
      String indice     = "";
      PortalSessionBean objPortalSesBean = null;
      AutomatizacionService objAutomatizacionService = null;
      GeneralService objGeneralService = null;
    
      try{    
          
          objGeneralService = new GeneralService();
          strPhoneNumber    = objGeneralService.GeneralDAOgetWorldNumber(strPhoneNumber,"COUNTRY");
          System.out.println("[EditOrderServlet][getStatusNumber]strPhoneNumber: "+strPhoneNumber);
          System.out.println("[EditOrderServlet][getStatusNumber]strAppId: "+strAppId);
          System.out.println("[EditOrderServlet][getStatusNumber]strVal_i: "+strVal_i);
          System.out.println("[EditOrderServlet][getStatusNumber]strVal_j: "+strVal_j);
          System.out.println("[EditOrderServlet][getStatusNumber]strItemId: "+strItemId);
          System.out.println("[EditOrderServlet][getStatusNumber]strItemDeviceId: "+strItemDeviceId);
          indice = strVal_i+"_"+strVal_j;
          System.out.println("[EditOrderServlet][getStatusNumber]indice: "+indice);
              
          objAutomatizacionService = new AutomatizacionService();
          HashMap hshAutomatizacion = new HashMap();
          
          hshAutomatizacion = objAutomatizacionService.getStatusNumber(strAppId,strPhoneNumber);
          
          strMessage = (String)hshAutomatizacion.get("strMessage");
          System.out.println("[EditOrderServlet][getStatusNumber]Message:  "+strMessage);
          
          if(hshAutomatizacion.get("strMessage")==null){
              strStatusNumber  = (String)hshAutomatizacion.get("strStatusNumber");
              strBlockedNumber = (String)hshAutomatizacion.get("strBlockedNumber");
              System.out.println("[EditOrderServlet][getStatusNumber]strStatusNumber: "+strStatusNumber);
              System.out.println("[EditOrderServlet][getStatusNumber]strBlockedNumber: "+strBlockedNumber);
              
              if(strStatusNumber.equals(strBlockedNumber) ){
                strMessage = objAutomatizacionService.doUpdatePhoneItemDevice(Long.parseLong(strOrderId), 
                Long.parseLong(strItemId), Long.parseLong(strItemDeviceId), strPhoneNumber);
                if(strMessage != null){
                  out.println("<script language='JavaScript' src='"+Constante.PATH_APPORDER_SERVER + "/Resource/library.js'></script>");
                  out.println("<script language='JavaScript' src='"+Constante.PATH_APPORDER_SERVER+"/Resource/BasicOperations.js'></script>");
                  out.println("<script>");
                  out.println("alert('"+MiUtil.getMessageClean(strMessage)+"');"); 
                  out.println("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
                  out.println("</script>");   
                }
              }
          
              if(!strStatusNumber.equals(strBlockedNumber) ){
                  strMessage ="Número de Teléfono no Permitido";
                  out.println("<script language='JavaScript' src='"+ Constante.PATH_APPORDER_SERVER + "/Resource/library.js'></script>");
                  out.println("<script language='JavaScript' src='"+Constante.PATH_APPORDER_SERVER+"/Resource/BasicOperations.js'></script>");
                  out.println("<script>");
                  out.println("alert('"+MiUtil.getMessageClean(strMessage)+"');");   
                  out.println("form        = parent.mainFrame.document.frmdatos;"); 
                  out.println("vDoc        = parent.mainFrame;     ");  
                  out.println("vDoc.fxObjectConvert('item_imei_numTel_"+indice+"','');"); 
                  out.println("form        = parent.mainFrame.document.frmdatos;"); 
                  out.println("try{"); 
                  out.println("form.item_imei_numTel_"+indice+".focus();");
                  out.println("}catch(e){}"); 
                  out.println("try{"); 
                  out.println("form.txt_ItemNewNumber.focus();"); 
                  out.println("}catch(e){}"); 
                  out.println(" location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
                  out.println("</script>");              
                  
              }              
          }
          
          else if(hshAutomatizacion.get("strMessage")!= null ){             
             out.println("<script language='JavaScript' src='"+ Constante.PATH_APPORDER_SERVER + "/Resource/library.js'></script>");
             out.println("<script language='JavaScript' src='"+Constante.PATH_APPORDER_SERVER+"/Resource/BasicOperations.js'></script>");
             out.println("<script>");
             out.println("alert('"+MiUtil.getMessageClean((String)hshAutomatizacion.get("strMessage"))+"');");   
             out.println("form        = parent.mainFrame.document.frmdatos;"); 
             out.println("vDoc        = parent.mainFrame;     ");  
             out.println("vDoc.fxObjectConvert('item_imei_numTel_"+indice+"','');"); 
             out.println("form        = parent.mainFrame.document.frmdatos;"); 
             out.println("try{");
             out.println("form.item_imei_numTel_"+indice+".focus();");
             out.println("}catch(e){}");
             out.println("try{"); 
             out.println("form.txt_ItemNewNumber.focus();"); 
             out.println("}catch(e){}");              
             out.println(" location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
             out.println("</script>");             
          }
         
      }catch(Exception e){
        strMessage = e.getMessage();       
        System.out.println("[EditOrderServlet][getStatusNumber][Capturado en el catch Exception]"+e.getMessage());
      }
      
    }
    
    
    public void validaDiasSuspensionEdit(HttpServletRequest request, HttpServletResponse response,PrintWriter out) throws ServletException,IOException{
    String        strNpOrderId      = request.getParameter("paramNpOrderId");
    String        strNpScheduleDate   = request.getParameter("npScheduleDate");
    String        strNpScheduleDate2  = request.getParameter("npScheduleDate2");
    
    System.out.println("[ServiceServlet][validaDiasSuspensionEdit]");
    System.out.println("[validaDiasSuspensionEdit]strPhoneNumber:"+strNpOrderId);
    System.out.println("[validaDiasSuspensionEdit]strNpScheduleDate:"+strNpScheduleDate);
    System.out.println("[validaDiasSuspensionEdit]strNpScheduleDate2:"+strNpScheduleDate2);
	 
    EditOrderService objEditOrderService = new EditOrderService();    

    System.out.println("[validaDetailPhone]OrderDAOvalidaDiasSuspensionEdit -Inicio-"+strNpOrderId);
    HashMap objHashMap = objEditOrderService.OrderDAOvalidaDiasSuspensionEdit(strNpOrderId,strNpScheduleDate, strNpScheduleDate2);
    System.out.println("[loadDetailPhone]ProductDAOvalidaDiasSuspension -Fin-"+strNpOrderId);
    
    if( objHashMap.get("strMessage") != null ){       
       out.println("<SCRIPT>alert('"+MiUtil.getMessageClean((String)objHashMap.get("strMessage"))+"');</SCRIPT>");       
    }
    
    
  }
  /**
   * <br>Realizado por: <a href="mailto:cesar.barzola@nextel.com.pe">Cesar Barzola</a>
   * <br>Cesar Barzola     07/08/2009  Creación
   * @see pe.com.nextel.dao.ProposedDAO#doValidateProposedOrder(long,long,long,long,String)      
   */
  public void doValidateProposedOrder(HttpServletRequest request, HttpServletResponse response, PrintWriter out)throws  ServletException, IOException{
        try {
            Date fechaInicio = new Date();
            System.out.println("[EditOrderServlet] [doValidateProposedOrder] [PM0010354] INICIO DEL METODO doValidateProposedOrder");
            System.out.println("[EditOrderServlet] [doValidateProposedOrder] [PM0010354] Fecha y Hora Inicio --> " + fechaInicio);
        }catch(Exception e) {
            System.out.println("[EditOrderServlet] [doValidateProposedOrder] [PM0010354] Error LOG INICIO");
        }
  
     String cadena=(request.getParameter("hdnTrama")==null?"":request.getParameter("hdnTrama"));
     String strCustomerId =(request.getParameter("txtCompanyId")==null?"":request.getParameter("txtCompanyId"));
     String strProposedId =(request.getParameter("hdnProposedId")==null?"":request.getParameter("hdnProposedId"));
     String strSpecificationId=(request.getParameter("hdnSubCategoria")==null?"":request.getParameter("hdnSubCategoria"));
     String strSellerId=(request.getParameter("hdnSellerId")==null?"":request.getParameter("hdnSellerId"));
     String strOrderId=(request.getParameter("hdnOrderId")==null?"":request.getParameter("hdnOrderId"));
     
      EditOrderService objEditOrderService= new EditOrderService();
      HashMap hshValidationProp= objEditOrderService.getValidationProposed(MiUtil.parseLong(strOrderId),MiUtil.parseLong(strProposedId),MiUtil.parseLong(strCustomerId),MiUtil.parseLong(strSpecificationId),MiUtil.parseLong(strSellerId),cadena);
      if(hshValidationProp.get(Constante.MESSAGE_OUTPUT)!=null)
      {      
      request.setAttribute(Constante.MESSAGE_OUTPUT,(String)hshValidationProp.get(Constante.MESSAGE_OUTPUT));
      }

        try {
            Date fechaFin = new Date();
            System.out.println("[EditOrderServlet] [doValidateProposedOrder] [PM0010354] Fecha y Hora Inicio --> " + fechaFin);
            System.out.println("[EditOrderServlet] [doValidateProposedOrder] [PM0010354] FIN DEL METODO doValidateProposedOrder");
        }catch(Exception e) {
            System.out.println("[EditOrderServlet] [doValidateProposedOrder] [PM0010354] Error LOG FIN");
        }

      request.getRequestDispatcher("pages/validateProposed.jsp").forward(request, response);
      
  }
  
   /**
    * PHIDALGO
   * Motivo: Validar si una orden tiene presupuesto
   * <br>Fecha: 24/11/2010 
   * @param     request     
   * @param     response             
   */   
  public String doValidateBudget(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{    
   
        try {
               Date fechaInicio = new Date();
               System.out.println("[EditOrderServlet] [doValidateBudget] [PM0010354] INICIO DEL METODO doValidateBudget");
               System.out.println("[EditOrderServlet] [doValidateBudget] [PM0010354] Fecha y Hora Inicio -->" + fechaInicio);
        }catch (Exception e){
                System.out.println("[EditOrderServlet] [doValidateBudget] [PM0010354] Error LOG INICIO" );
        }

      String strMessage = null;
      OrderBean orderBean = null;
      PortalSessionBean portalBean = null;
      ItemBean itemBean = null;
      ItemDeviceBean itemDeviceBean = null;
      String strCurrentInbox = null;
      String strSessionId = null;
      String strCustomerId = null;
      String strCreatedBy = null;
      String strCreatedDateOrder = null;
      String strSpecificationId = null;
      long lLugarDespacho = 0;
      long lOrderId = 0;
      int iSiteId = 0;
      EditOrderService objEditOrderService= new EditOrderService();
      NewOrderService  objNewOrderService  = new NewOrderService();
      
      lOrderId=(request.getParameter("hdnOrderId")==null?0:MiUtil.parseLong(request.getParameter("hdnOrderId")));
      strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
      strCurrentInbox=(request.getParameter("txtEstadoOrden")==null?"":request.getParameter("txtEstadoOrden"));
      strCustomerId =(request.getParameter("txtCompanyId")==null?"":request.getParameter("txtCompanyId"));
      iSiteId=(request.getParameter("an_swsiteid")==null?0:Integer.parseInt(request.getParameter("an_swsiteid")));
      strSpecificationId=(request.getParameter("hdnSubCategoria")==null?"":request.getParameter("hdnSubCategoria"));
      strCreatedBy = (request.getParameter("hdnCreateBy")==null?"":request.getParameter("hdnCreateBy"));
      strCreatedDateOrder = (request.getParameter("hdnFechaCreacionOrden")==null?"":request.getParameter("hdnFechaCreacionOrden"));
      lLugarDespacho = (request.getParameter("hdnLugarDespacho")==null?0:MiUtil.parseLong(request.getParameter("hdnLugarDespacho")));
      try{
          HashMap  objItemDeviceMap = new HashMap();
          HashMap  objHashItemOrder = objNewOrderService.ItemDAOgetItemOrder(lOrderId);
          ArrayList objItemOrder = (ArrayList)objHashItemOrder.get("objArrayList");
          
          //se obtiene los imeis para convertir en un map para enviar a la consulta de presupuesto.
          ArrayList objItemDeviceList = objNewOrderService.ItemDAOgetItemDeviceOrder(lOrderId);
          if( objItemDeviceList != null && objItemDeviceList.size() > 0 ){
              for( int i=0; i<objItemDeviceList.size(); i++){
                  itemDeviceBean = (ItemDeviceBean)objItemDeviceList.get(i);
                  objItemDeviceMap.put(MiUtil.getString(itemDeviceBean.getNpitemid()),itemDeviceBean);
              }
          }      
          portalBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
          orderBean = new OrderBean();
          orderBean.setNpGeneratorType(Constante.GENERATOR_TYPE_ORDER);
          orderBean.setNpOrderId(lOrderId);
          orderBean.setNpStatus(strCurrentInbox);
          orderBean.setNpCustomerId(MiUtil.parseInt(strCustomerId));
          orderBean.setNpSiteId(iSiteId);
          orderBean.setNpSpecificationId(MiUtil.parseInt(strSpecificationId));
          orderBean.setNpCreatedBy(strCreatedBy);
          orderBean.setNpCreatedDate(MiUtil.toFechaHora(strCreatedDateOrder,"dd/MM/yyyy"));
          orderBean.setNpDispatchPlaceId(lLugarDespacho);
  
          strMessage= objEditOrderService.doValidateBudget(orderBean, portalBean, objItemOrder,objItemDeviceMap);
          PrintWriter out = response.getWriter();
          out.print(strMessage);
      }catch(Exception e){
             logger.error(e);
             strMessage = e.getMessage();

            Date fechaFin = new Date();
            System.out.println("[EditOrderServlet] [doValidateBudget] [PM0010354] Exception: -->" + strMessage );
            System.out.println("[EditOrderServlet] [doValidateBudget] [PM0010354] Fecha y Hora de Fin -->" + fechaFin );
      }

        try {
                Date fechaFin = new Date();
                System.out.println("[EditOrderServlet] [doValidateBudget] [PM0010354] Fecha y Hora Fin -->" + fechaFin);
                System.out.println("[EditOrderServlet] [doValidateBudget] [PM0010354] FIN DEL METODO doValidateBudget");
        }catch (Exception e){
                System.out.println("[EditOrderServlet] [doValidateBudget] [PM0010354] Error LOG FIN" );
        }


      return strMessage;
   } 

  
   /****************************************************************************************************************************
   * Motivo: Inserta las Aplicaciones de Venta Data en una Orden
   * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
   * <br>Fecha: 06/05/2010
   * @param response
   * @param request
   *****************************************************************************************************************************/ 
 /*  public void doInsertSalesData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{    
   
    String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
	  String strCustomerId=(request.getParameter("hdnCustomerId")==null?"0":request.getParameter("hdnCustomerId"));
    String strDivisionId=(request.getParameter("hdnDivisionId")==null?"0":request.getParameter("hdnDivisionId"));

    PortalSessionBean objPortalSesBean = null;
      
    String strLogin = null; 
   
    try{             
      objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
      
      if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
        throw new Exception("La sesión del usuario ha expirado por favor vuelva a registrarse. Inicie Login");
     
     objHashMap =null; 
     objHashMap = getParameterNames(request);
     if (objHashMap==null)
        System.out.println("objHashMap Es nulo");
  /*
     HashMap hshRetorno = objSiteService.insSites(objHashMap,objPortalSesBean);   
     strOrderId=(String)hshRetorno.get("strOrderId");
     strCustomerId=(String)hshRetorno.get("strCustomerId");
     strSiteId=(String)hshRetorno.get("strSiteId");
     objSiteBean= (SiteBean)hshRetorno.get("objSite");
     strMessage=  (String)hshRetorno.get("strMessage");
		 if(logger.isDebugEnabled()){
			logger.debug(" -------------------- INICIO EdittOrderServlet.java / doInsertSite ---------------------- ");
			logger.debug("strOrderId-->"+strOrderId);
			logger.debug("strCustomerId-->"+strCustomerId);
			logger.debug("strSiteId-->"+strSiteId);
			logger.debug("strSpecificationId: "+strSpecificationId);
			logger.debug("strMessage-->"+strMessage);
			logger.debug(" -------------------- FIN EdittOrderServlet.java / doInsertSite ---------------------- ");
		 }		 
      }catch(Exception e){
         e.printStackTrace();
         strMessage = e.getMessage();
      }   
      
      request.setAttribute("objSite",objSiteBean);
	  //INICIO CEM - COR0354
      //RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strSessionId+"|sAction=R&sMensaje="+strMessage);
	  //RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strSessionId+"|sAction=R&sMensaje="+strMessage+"&pCustomerId="+strCustomerId+"&pOrderId="+strOrderId+"&pSiteId="+strSiteId+"&pSpecificationId="+strSpecificationId);
	  RequestDispatcher rd=request.getRequestDispatcher("DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ResultSite.jsp?sParametro=?nCustomerId="+strCustomerId+"|nOrderId="+strOrderId+"|nSiteId="+strSiteId+"|hdnSessionId="+strSessionId+"|pSpecificationId="+strSpecificationId+"|sAction=R&sMensaje="+strMessage);
	  //FIN CEM - COR0354
      rd.forward(request,response);
   }  
    */
/**
   * <br>Realizado por: <a href="mailto:michael.valle@hp.com">Michael Valle</a>
   * <br>Michael Valle     12/11/2010  Creación
   * @see pe.com.nextel.dao.OrderDAO#doGenerateGuiaRemision(long,String)      
   */
  public void doGenerateGuiaRemision(HttpServletRequest request, HttpServletResponse response)throws  ServletException, IOException{
  
     HashMap hshResult = new HashMap();
     String strMessage = null; 
     int iErrorNumber=-1;
     long lOrderId=(request.getParameter("hdnOrderId")==null?0:MiUtil.parseLong(request.getParameter("hdnOrderId")));
     String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
    
     String strLogin   = null;
     int   iBuildingId = 0;
     PortalSessionBean objPortalSesBean = null;
     
      try{
      objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
      
      if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
        throw new Exception("La sesión del usuario ha expirado por favor vuelva a registrarse. Inicie Login");
              
      strLogin    = objPortalSesBean.getLogin();
      iBuildingId = objPortalSesBean.getBuildingid();  
  
      HashMap hshRetorno = objOrderService.doGenerateGuiaRemision(lOrderId,strLogin);   
      iErrorNumber = MiUtil.parseInt((String)hshRetorno.get("iError"));         
      strMessage=(String)hshRetorno.get("strMessage");           
    }catch(Exception e){
      strMessage = e.getMessage();
}

    System.out.println("-----------------Inicio EditOrderServlet.java / doGeneratePayOrder-------------------------");      
    System.out.println("lOrderId-->"+lOrderId);
    System.out.println("strSessionId-->"+strSessionId);
    System.out.println("strLogin-->"+strLogin);
    System.out.println("iBuildingId-->"+iBuildingId);
    System.out.println("iErrorNumber-->"+iErrorNumber);      
    System.out.println("strMessage-->"+strMessage);      
    System.out.println("-----------------Fin EditOrderServlet.java / doGeneratePayOrder-------------------------");      

    hshResult.put("strOrderId",MiUtil.getString(lOrderId));
    hshResult.put("strMessage",strMessage); 
    hshResult.put("strErrorNumer",iErrorNumber+"");       
    hshResult.put("strTipoOrigen","guiaRemision");      
  //hshResult.put("strAddItemPayOrder",strNumPaymentOrderId); // CEM - COR0720	  
    
    request.setAttribute("objResultado",hshResult); 
    RequestDispatcher rd=request.getRequestDispatcher("PAGEEDIT/ResultEdit.jsp");
    rd.forward(request,response);  
     

  }    
  
  
   /**
   * Motivo: Suspención de equipos de una Orden
   * <br>Realizado por: <a href="mailto:joseph.nino-de-guzman-diaz@hp.com">Joseph Niño de Guzmán</a>
   * <br>Fecha: 29/11/2010
   * @param     request     
   * @param     response             
   */         
  public void doSuspenderEquipos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{    
   
    HashMap hshResult=new HashMap();
    String strValor = null;    
    String strMessage = null;    
    String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));    
    long lOrderId=(request.getParameter("hdnOrderId")==null?0:MiUtil.parseLong(request.getParameter("hdnOrderId")));
    String strOrderId = (request.getParameter("hdnOrderId")==null?"0":request.getParameter("hdnOrderId"));
    System.out.println("strSessionId-->"+strSessionId);
    
    String strLogin = null;
    long lLoginBuilding = 0;
    int iErrorNumber=0;
    
    HashMap hshData =new HashMap();
    
    PortalSessionBean objPortalSesBean = null;

    try{
      objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
      
      if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
        throw new Exception("La sesión del usuario ha expirado por favor vuelva a registrarse. Inicie Login");
      
      strLogin = objPortalSesBean.getLogin();
      lLoginBuilding = objPortalSesBean.getBuildingid();
      
      hshData = objOrderService.doGenerarSuspenderEquipos(lOrderId);          
          
      strMessage =(String)hshData.get("strMessage");
      
    }catch(Exception e){
      strMessage = e.getMessage();
    }  
    
    System.out.println("-----------------Inicio EditOrderServlet.java / doSuspenderEquipos-------------------------");
    System.out.println("lOrderId-->"+lOrderId);
    System.out.println("strSessionId-->"+strSessionId);
    System.out.println("strMessage-->"+strMessage);    
    System.out.println("-----------------Fin EditOrderServlet.java / doSuspenderEquipos-------------------------");      
    
    hshResult.put("strOrderId",strOrderId);
    hshResult.put("strMessage",strMessage); 
    
    request.setAttribute("objResultado",hshResult); 
    RequestDispatcher rd=request.getRequestDispatcher("PAGEEDIT/ResultEdit.jsp");
    rd.forward(request,response);  
    
   }
   
   
   
    /**
     * JRAMIREZ
    * Motivo: Validar si es una Tienda Express
    * <br>Realizado por: <a href="mailto:carlos.puente@nextel.com.pe">CPUENTE</a>
    * <br>Fecha: 10/06/2014
    * @param     request     
    * @param     response             
    */   
    public int doValidateIsTiendaExpress(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    
     int intMessage =0;  
     String sbuildingId=request.getParameter("ibuildingId");
     SessionService objSessionService= new SessionService();
     
     try{             
        int ibuildingId=Integer.valueOf(sbuildingId).intValue();         
        intMessage= objSessionService.getExistTiendaExpress(ibuildingId);        
        PrintWriter out = response.getWriter() ;
        out.print(intMessage);
         }catch(Exception e){
             intMessage =0;
         } 
         return intMessage;
    } 
     
    /**
     * JLIMAYMANTA
    * Motivo: Validar si la subcategoria contempla pago al contado
    * <br>Realizado por: <a href="mailto:carlos.puente@nextel.com.pe">CPUENTE</a>
    * <br>Fecha: 01/07/2014
    * @param     request     
    * @param     response             
    */   
    public int doValidateSubcategoria(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    
     int intMessage =0;  
     String subcategoriaid=request.getParameter("subcategoriaid");
     SessionService objSessionService= new SessionService();
     
     try{             
       
        intMessage= objSessionService.doValidateSubcategoria(Integer.valueOf(subcategoriaid).intValue());        
        PrintWriter out = response.getWriter() ;
        out.print(intMessage);
         }catch(Exception e){
            e.printStackTrace();
             intMessage =0;
         } 
         return intMessage;
    } 
    
    /**
     * JRAMIREZ
    * Motivo: Validar si la orden existe -  Tienda Express
    * <br>Realizado por: <a href="mailto:carlos.puente@nextel.com.pe">CPUENTE</a>
    * <br>Fecha: 21/07/2014
    * @param     request     
    * @param     response             
    */   
    public int doValidateOrderExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    
     int intMessage =0;  
     String sorderId=request.getParameter("iorderId");
     GeneralService objGeneralService= new GeneralService();
     
     try{             
        int iorderId=Integer.valueOf(sorderId).intValue();
        intMessage= objGeneralService.getOrderExist(iorderId);        
        PrintWriter out = response.getWriter() ;
        out.print(intMessage);
         }catch(Exception e){
             intMessage =0;
         } 
         return intMessage;
    } 
    
    /**
     * JRAMIREZ
    * Motivo: Obtiene el buildingid de una orden - Tienda Express
    * <br>Realizado por: <a href="mailto:carlos.puente@nextel.com.pe">CPUENTE</a>
    * <br>Fecha: 21/07/2014
    * @param     request     
    * @param     response             
    */   
    public int doGetBuildingByOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    
     int intMessage =0;  
     String sorderId=request.getParameter("iorderId");
     GeneralService objGeneralService= new GeneralService();
     
     try{             
        int iorderId=Integer.valueOf(sorderId).intValue();
        intMessage= objGeneralService.getBuildingidByOrderid(iorderId);        
        PrintWriter out = response.getWriter() ;
        out.print(intMessage);
         }catch(Exception e){
             intMessage =0;
         } 
         return intMessage;
    } 
     

    /**
     *
     * Motivo: Obtener Valores para validar la cantidad de modelos ingresados
     * <br>Realizado por: EPENA EPV</a>
     * <br>Fecha: 22/06/2015
     * @param     request
     * @param     response
     */
    public String doGetValueLimitModel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try{
            Date  fechaInicio = new Date();
            System.out.println("[EditOrderServlet] [doGetValueLimitModel] [PM0010354] INICIO  DEL METODO doGetValueLimitModel");
            System.out.println("[EditOrderServlet] [doGetValueLimitModel] [PM0010354] Hora de Inicio del Log -->"+fechaInicio);
        }catch (Exception e){
            System.out.println("[EditOrderServlet] [doGetValueLimitModel] [PM0010354] Error LOG INICIO -->");
        }

        String subcategoriaModel=request.getParameter("subcategoriaModel");
        System.out.println("Entrando [EditOrderServlet] [doGetValueLimitModel]");
        GeneralService objGeneralService = new GeneralService();
        System.out.println("[EditOrderServlet] [doGetValueLimitModel] subcategoriaModel: "+subcategoriaModel);
        HashMap hshData=null;
        String strMessage=null, strValue=null, strFlag=null;
        String concat;

        try{

            hshData=objGeneralService.getValueLimitModel("ORDER_MODEL_LIMIT", subcategoriaModel);
            strMessage=(String)hshData.get("strMessage");
            System.out.println(" [EditOrderServlet] [doGetValueLimitModel] strMessage: "+strMessage);
            strValue=(String)hshData.get("strValue");
            System.out.println(" [EditOrderServlet] [doGetValueLimitModel] strValue: "+strValue);
            strFlag=(String)hshData.get("strFlag");
            System.out.println(" [EditOrderServlet] [doGetValueLimitModel] strFlag: "+strFlag);
        }catch(Exception e)
        {
            System.out.println(" [EditOrderServlet] [doGetValueLimitModel] Exception: "+e.getMessage());
}
        concat=strFlag+","+strValue+","+strMessage;

        System.out.println("[EditOrderServlet] [doGetValueLimitModel] concat: "+concat);
        PrintWriter out = response.getWriter() ;
        out.print(concat);
        try{
                Date  fechaFin = new Date();
                System.out.println("[EditOrderServlet] [doGetValueLimitModel] [PM0010354] Fecha y Hora Fin -->" + fechaFin);
                System.out.println("[EditOrderServlet] [doGetValueLimitModel] [PM0010354] FIN DEL METODO doGetValueLimitModel ");
        }catch (Exception e){
                System.out.println("[EditOrderServlet] [doGetValueLimitModel] [PM0010354] Error LOG FIN -->");
        }

        return concat;
    }
public void getValidarDireccionRiesgo(HttpServletRequest request,
                                                HttpServletResponse response) throws ServletException, IOException {
        HashMap hshResult=new HashMap(); 
        String strMessage = null;
        CustomerService objCustomerService = new CustomerService();           
        
        String txtAddress = (request.getParameter("txtAddress")==null?"":request.getParameter("txtAddress"));     
        
        String txtReference = (request.getParameter("txtReference")==null?"":request.getParameter("txtReference"));
        String txtSpecificationid = (request.getParameter("specificationid")==null?"":request.getParameter("specificationid"));

        String sAddressToValidate = txtAddress.trim().toUpperCase().replaceAll("^\\s","") + 
                                    txtReference.trim().toUpperCase().replaceAll("^\\s","");
        
        String jUbigeoTemp =(request.getParameter("cmbDist")==null?"0":request.getParameter("cmbDist"));       
        
        try{
            
            Boolean validacion=false;
            System.out.println("*******************");
            Double key=0.0;
            Boolean value=false;
            
            //Trayendo el listado de documentos de pago
            GeneralDAO  objGeneralDAO =new GeneralDAO();
            HashMap hshData =new  HashMap();
            hshData= objGeneralDAO.getTableList("VALIDATE_ADDRESS_BY_TYPEORDER_LST","1");
            
            ArrayList arrValidateBySpec=(ArrayList)hshData.get("arrTableList");   
            
            if (arrValidateBySpec==null) arrValidateBySpec = new ArrayList();
            System.out.println("Tamaño del ArrayList: " + arrValidateBySpec.size());         
            
            Boolean bValidateAddressByTypeOrder = false;
            
            for (int i=0;i<arrValidateBySpec.size();i++){
               HashMap hshValidateBySpec=new HashMap();
               hshValidateBySpec=(HashMap)arrValidateBySpec.get(i);

                String sNpTag1;
                sNpTag1 = (String)hshValidateBySpec.get("wv_npTag1");
                if (sNpTag1.trim().equalsIgnoreCase(txtSpecificationid.trim())){
                    bValidateAddressByTypeOrder=true;
                }
            }
            if (bValidateAddressByTypeOrder){              
                if(jUbigeoTemp=="0"){
                    String strDpto = (request.getParameter("strDpto")==null?"":request.getParameter("strDpto"));
                    String strProv = (request.getParameter("strProv")==null?"":request.getParameter("strProv"));
                    String strDist = (request.getParameter("strDist")==null?"":request.getParameter("strDist"));
                    if(strDpto!="" && strProv!="" && strDist!=""){
                        String strDptoId =null;
                        String strProvId =null;
                        ArrayList      arrLista          = null;
                        ArrayList      arrListaProv          = null;
                        ArrayList      arrListaDist          = null;
                        
                        HashMap        hshUbigeoList     = null;
                        HashMap        hshResultado     = null;
                        hshUbigeoList = objGeneralService.getUbigeoList(0,0,"0"); 
                        strMessage = (String)hshUbigeoList.get("strMessage");
                        if (strMessage!=null)
                          throw new Exception(strMessage);
                        arrLista = (ArrayList)hshUbigeoList.get("arrUbigeoList");
                        
                        for( int i=1; i<=arrLista.size();i++ ){ 
                          HashMap h = (HashMap)arrLista.get(i-1);
                          if(MiUtil.getString((String)h.get("nombre")).equals(strDpto)){
                            strDptoId = MiUtil.getString((String)h.get("ubigeo"));
                          }
                        }
                        
                        hshResultado=objGeneralService.getUbigeoList(MiUtil.parseInt(strDptoId),0,"1");             
                        strMessage=(String)hshResultado.get("strMessage");
                        if (strMessage==null)                
                            arrListaProv=(ArrayList)hshResultado.get("arrUbigeoList");  
    
                        for( int i=1; i<=arrListaProv.size();i++ ){ 
                            HashMap h = (HashMap)arrListaProv.get(i-1);
                            if(MiUtil.getString((String)h.get("nombre")).equals(strProv)){
                              strProvId = MiUtil.getString((String)h.get("ubigeo"));
                            }
                        }
                        hshResultado=objGeneralService.getUbigeoList(MiUtil.parseInt(strDptoId),MiUtil.parseInt(strProvId),"2");             
                          strMessage=(String)hshResultado.get("strMessage");
                          if (strMessage==null)                
                            arrListaDist=(ArrayList)hshResultado.get("arrUbigeoList");  
                           
                          for( int i=1; i<=arrListaDist.size();i++ ){ 
                            HashMap h = (HashMap)arrListaDist.get(i-1);
                            if(MiUtil.getString((String)h.get("nombre")).equals(strDist)){
                              jUbigeoTemp = MiUtil.getString((String)h.get("ubigeo"));
                            }
                          }
                    }
                }
                
                Map<Double,Boolean> mapValidacion=objCustomerService.getValidateAddress(sAddressToValidate,jUbigeoTemp,"PORTAL");
                for(Map.Entry<Double,Boolean> entry : mapValidacion.entrySet()) {
                    key = entry.getKey();
                    value = entry.getValue();
                    System.out.println(key + " => " + value);
                }
            }
            hshResult.put("flag",value);                
            hshResult.put("correlacion",key);
            hshResult.put("direccion",sAddressToValidate);
            response.setContentType("text/json");
            response.setCharacterEncoding("ISO-8859-1");
            response.getWriter().write(new Gson().toJson(hshResult));
            response.getWriter().flush();
        }catch(Exception e){
            System.out.println("[EditOrderServlet] [getValidarDireccionRiesgo] LOG -->"+e.getMessage());
        }  
    } 

public void getInsertLogValidateAddress(HttpServletRequest request,
                                                HttpServletResponse response) throws ServletException, IOException {
        try{

        CustomerService objCustomerService = new CustomerService();
       
        String sDireccion = (request.getParameter("direccion")).trim().toUpperCase().replaceAll("^\\s","");
           
        Double dCorrelacion = Double.parseDouble((String)request.getParameter("correlacion"));
        Integer lIdCliente = Integer.parseInt(request.getParameter("npidcliente")==null?"0":request.getParameter("npidcliente"));
        String sNumDoc = (request.getParameter("npnumdoc")==null?"":request.getParameter("npnumdoc"));
        Integer lNumOrder = Integer.parseInt(request.getParameter("npnumorder")==null?"0":request.getParameter("npnumorder"));
       
        String sCreatedBy=request.getParameter("createdBy");
        objCustomerService.insLogValidateAddress("ORDERS",dCorrelacion,sCreatedBy,sDireccion,lIdCliente, sNumDoc,lNumOrder);
        
            String hdnUserId="";
            String hdnAppId="";
            
        CommentForm objCommentForm = new CommentForm();
            objCommentForm.setHdnAppId(hdnAppId);
            objCommentForm.setHdnUserId(hdnUserId);
            objCommentForm.setHdnLogin(sCreatedBy);
            objCommentForm.setHdnOrderId(String.valueOf(lNumOrder));
            objCommentForm.setHdnFlagSave("true");
            objCommentForm.setTxtSubject("Alerta de posibilidad de dirección de fraude");
            objCommentForm.setTxtDescription(sDireccion +", % de probabilidad "+dCorrelacion);
            objOrderTabsService.addComment(objCommentForm);
        
        Map<String,Object> respuestaJson = new HashMap<String,Object>();
        respuestaJson.put("flag",true);
        response.setContentType("text/json");
        response.setCharacterEncoding("ISO-8859-1");
        response.getWriter().write(new Gson().toJson(respuestaJson));
        response.getWriter().flush();
        }catch(Exception e){
            System.out.println("[EditOrderServlet] [getInsertLogValidateAddress] LOG -->"+e.getMessage());
        }
    }

    public String doValidateNumSolicitud(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{


        String ValidateNumSolicitud =(request.getParameter("numSolicitud")==null?"":request.getParameter("numSolicitud")); 
        String sSpecificationId     =(request.getParameter("specificationid")==null?"0":request.getParameter("specificationid")); 
        
        System.out.println("Entrando [EditOrderServlet] [doValidateNumSolicitud]");
        GeneralService objGeneralService = new GeneralService();
        System.out.println("[EditOrderServlet] [doValidateNumSolicitud] numSolicitud: "+ValidateNumSolicitud);
        System.out.println("[EditOrderServlet] [doValidateNumSolicitud] specificationid: "+sSpecificationId);
        Boolean validate=false;
        String concat="";
        try{
            //Trayendo el listado de documentos de pago
            GeneralDAO  objGeneralDAO =new GeneralDAO();
            HashMap hshData =new  HashMap();
            hshData= objGeneralDAO.getTableList("VALIDATE_ORDERNUMBER_BY_TYPEORDER_LST","1");
            
            ArrayList arrValidateBySpec=(ArrayList)hshData.get("arrTableList");   
            
            if (arrValidateBySpec==null) arrValidateBySpec = new ArrayList();
            System.out.println("Tamaño del ArrayList: " + arrValidateBySpec.size());                                 
            
            int iFlagColumnOrderNumber=0;
            for (int i=0;i<arrValidateBySpec.size();i++){
               HashMap hshValidateBySpec=new HashMap();
               hshValidateBySpec=(HashMap)arrValidateBySpec.get(i);
               String sNpTag1=null;
               sNpTag1 = (String)hshValidateBySpec.get("wv_npTag1");
                if (sNpTag1.trim().equalsIgnoreCase(sSpecificationId.trim())){
                    iFlagColumnOrderNumber=1;
                }
            }
            System.out.println("[EditOrderServlet] [doValidateNumSolicitud] iFlagColumnOrderNumber: "+iFlagColumnOrderNumber);
            if (ValidateNumSolicitud!=null && !ValidateNumSolicitud.equalsIgnoreCase("")){            
                validate=objGeneralService.validateNumSolicitudRetail(ValidateNumSolicitud,iFlagColumnOrderNumber);
                System.out.println(" [EditOrderServlet] [doValidateNumSolicitud] validate: "+validate);
            }
        }catch(Exception e)
        {
            System.out.println(" [EditOrderServlet] [doValidateNumSolicitud] Exception: "+e.getMessage());
}
        concat=ValidateNumSolicitud+","+validate;

        System.out.println("[EditOrderServlet] [doValidateNumSolicitud] concat: "+concat);
        PrintWriter out = response.getWriter() ;
        out.print(concat);

        return concat;
    }

    //EFLORES 29/12/2015 Requerimiento REQ-0204
    public String doValidateUltimaPreevaluacion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        System.out.println("[EditOrderServlet][doValidateUltimaPreevaluacion] Inicio de método Invocado");
        String valor = "1|Generico";
        String strCustomerId = request.getParameter("customerId");
        System.out.println("[EditOrderServlet][doValidateUltimaPreevaluacion] CustomerId = "+strCustomerId);
        String userLogin = request.getParameter("userLogin");
        System.out.println("[EditOrderServlet][doValidateUltimaPreevaluacion] UserLogin = "+userLogin);
        String strNumeros   = request.getParameter("cadenaNumeros");
        System.out.println("[EditOrderServlet][doValidateUltimaPreevaluacion] cadenaNumeros = "+strNumeros);
        String strModalidad   = request.getParameter("cadenaModalidad");
        System.out.println("[EditOrderServlet][doValidateUltimaPreevaluacion] cadenaModalidad = "+strModalidad); //JBALCAZAR PRY-1055       
        String strCategoryId   = request.getParameter("categoryId");
        System.out.println("[EditOrderServlet][doValidateUltimaPreevaluacion] categoryId = "+strCategoryId);
        System.out.println("[EditOrderServlet][doValidateUltimaPreevaluacion] Ejecuta Metodo doValidateUltimaPreevaluacion");
        valor = objOrderService.doValidateUltimaPreevaluacion(strCustomerId,strCategoryId,strNumeros,strModalidad,userLogin);
        System.out.println("[EditOrderServlet][doValidateUltimaPreevaluacion] Fin Ejecuta Metodo doValidateUltimaPreevaluacion");

        PrintWriter out = response.getWriter() ;
        out.print(valor+"");
        System.out.println("[EditOrderServlet][doValidateUltimaPreevaluacion] Fin de método Invocado");
        return valor;
    }

    /**
     * @author AMENDEZ
     * @project PRY-0864
     * Metodo   Evalua el monto inicial en casos de persona natural y persona juridica
     *          flag vep activo, cliente es ruc20(Juridico) y monto inicial no obligatorio: 1
     *          flag vep activo y cliente no es ruc20(Natural) y monto inicial es obligatorio: 2
     *          errores generales -1
     * @return
     */
    public String validateOrderVepCI(HttpServletRequest request,
                                  HttpServletResponse response) {

        logger.info("*************** INICIO EditOrderSevlet > validateOrderVepCI ***************");
        String strMessage ="";

        String strNporderid=request.getParameter("nporderid");
        String strNpvepquantityquota=request.getParameter("npvepquantityquota");
        String strNpinitialquota=request.getParameter("npinitialquota");
        String strNpspecificationid=request.getParameter("npspecificationid");
        String strSwcustomerid=request.getParameter("swcustomerid");
        String strTotalsalesprice=request.getParameter("totalsalesprice");
        String strNpvep=request.getParameter("npvep");
        String strNptype=request.getParameter("nptype");
        String strNpPaymentTermsIQ=request.getParameter("nppaymenttermsiq");

        NewOrderService objNewOrderService= new NewOrderService();

        try{
            long nporderid= MiUtil.parseLong(strNporderid);
            int npvepquantityquota= MiUtil.parseInt(strNpvepquantityquota);
            double npinitialquota= MiUtil.parseDouble(strNpinitialquota);
            int npspecificationid= MiUtil.parseInt(strNpspecificationid);
            long swcustomerid= MiUtil.parseLong(strSwcustomerid);
            double totalsalesprice = MiUtil.parseDouble(strTotalsalesprice);
            int npvep= MiUtil.parseInt(strNpvep);
            String nptype=strNptype;
            int nppaymenttermsiq=MiUtil.parseInt(strNpPaymentTermsIQ);

            logger.info("nporderid            : "+nporderid);
            logger.info("npvepquantityquota   : "+npvepquantityquota);
            logger.info("npinitialquota       : "+npinitialquota);
            logger.info("npspecificationid    : "+npspecificationid);
            logger.info("swcustomerid         : "+swcustomerid);
            logger.info("totalsalesprice      : "+totalsalesprice);
            logger.info("npvep                : "+npvep);
            logger.info("nptype               : "+nptype);
            logger.info("nppaymenttermsiq     : "+nppaymenttermsiq);
            strMessage= objNewOrderService.validateOrderVepCI(nporderid,npvepquantityquota,npinitialquota,npspecificationid,swcustomerid,totalsalesprice,npvep,nptype,nppaymenttermsiq);

            logger.info("strMessage: "+strMessage);
            PrintWriter out = response.getWriter() ;
            out.print(strMessage);
        }catch(Exception e){
            strMessage =e.getMessage();
            logger.error("ERROR Exception: ",e);
        }
        logger.info("*************** FIN EditOrderSevlet > validateOrderVepCI ***************");
        return strMessage;

    }

    /**
     * @author AMENDEZ
     * @project PRY-0864
     * Metodo   Evalua el monto inicial en casos de persona natural y persona juridica
     *          flag vep activo, cliente es ruc20(Juridico) y monto inicial no obligatorio: 1
     *          flag vep activo y cliente no es ruc20(Natural) y monto inicial es obligatorio: 2
     *          errores generales -1
     * @return
     */
    public String validatePaymentTermsCI(HttpServletRequest request,
                                     HttpServletResponse response) {

        logger.info("*************** INICIO EditOrderSevlet > validatePaymentTermsCI ***************");
        String strMessage ="";

        String strSwcustomerid=request.getParameter("npcustomerId");
        String strUserId=request.getParameter("npuserid");
        String strNpvep=request.getParameter("npvep");

        NewOrderService objNewOrderService= new NewOrderService();

        try{
            long swcustomerid= MiUtil.parseLong(strSwcustomerid);
            long npuserid= MiUtil.parseLong(strUserId);
            int npvep= MiUtil.parseInt(strNpvep);

            logger.info("swcustomerid   : "+swcustomerid);
            logger.info("npuserid       : "+npuserid);
            logger.info("npvep          : "+npvep);

            strMessage= ""+objNewOrderService.validatePaymentTermsCI(swcustomerid,npuserid,npvep);

            logger.info("strMessage: "+strMessage);
            PrintWriter out = response.getWriter() ;
            out.print(strMessage);
        }catch(Exception e){
            strMessage =e.getMessage();
            logger.error("ERROR Exception: ",e);
        }
        logger.info("*************** FIN EditOrderSevlet > validatePaymentTermsCI ***************");
        return strMessage;

    }   

/**
     * @author AMATEOM
     * @project PRY-1062
     * Metodo   Validad la ultima preevaluacion para Cambio de Modelo
     * @return
     */
    public void doValidatePreevaluationCDM(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        PrintWriter out = response.getWriter();
        Long customerId = Long.valueOf(Long.parseLong(String.valueOf(request.getParameter("customerId"))));
        String strMessage = "";

        Gson gson = new Gson();
        try
        {
            HashMap objReturn = new NewOrderService().doValidatePreevaluationCDM(customerId);

            strMessage = (String)objReturn.get("strMessage");

            String msg = gson.toJson(strMessage);
            out.write("{\"strMessage\":" + msg + "}");

            out.close();
        }
        catch (Exception e)
        {
            String msg = gson.toJson("Error EditOrderSevlet.doValidatePreevaluationCDM" + e.getMessage());
            out.write("{\"strMessage\":" + msg + "}");
            logger.info("ERROR AL VALIDAR  validatePreevaluationCDM value: " + e.getMessage());
        }
    }
    /*
    Method :    loadUseAddressInOrder
    Project:    PRY-1049
    Purpose:    Carga la dirección de uso de la pre evaluación realizada al cliente en la tabla de direcciones
    de la orden
    Developer       Fecha       Comentario
    =============   ==========  ======================================================================
    Daniel Olano    22/03/2018  Creación
    */
    public void loadUseAddressInOrder(HttpServletRequest request,
                               HttpServletResponse response) throws ServletException, IOException {


        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String strMessage ="";
        logger.info("*************** INICIO EditOrderSevlet > loadUseAddressInOrder ***************");
        try {

            String strCustomerId    = request.getParameter("customerId");
            String strBuildingId =  request.getParameter("npbuidingid");

            int buildingId = MiUtil.parseInt(strBuildingId);
            long customerid= MiUtil.parseLong(strCustomerId);
            logger.info("customerid      : "+customerid);
            logger.info("buildingId      : "+buildingId);
            EditOrderService editOrderService = new EditOrderService();
            HashMap resultMap = editOrderService.loadUseAddressInOrder(buildingId, customerid);

            Gson gson = new Gson();
            String json = gson.toJson(resultMap);
            out.write("{\"addressUse\":"+json+"}");
            logger.info("json      : "+json);
        }catch(Exception ex){
            strMessage =ex.getMessage();
            logger.error("ERROR Exception: ",ex);
        }finally {
            out.close();
        }
        logger.info("*************** FIN EditOrderSevlet > loadUseAddressInOrder ***************");
    }

    /**
     * Purpose: Valida que exista una configuracion para bafi 2300 segun modalidad, solucion y linea producto
     * Developer       Fecha       Comentario
     * =============   ==========  ======================================================================
     * AMENDEZ         22/03/2018  [EST-1098]Creacion
     */
    public String validateConfigBafi2300(HttpServletRequest request,
                                         HttpServletResponse response) {

        logger.info("*************** INICIO EditOrderSevlet > validateConfigBafi2300 ***************");
        String strMessage ="";

        String av_npmodality=request.getParameter("av_npmodality");
        String av_npsolutionid=request.getParameter("av_npsolutionid");
        String av_npproductlineid=request.getParameter("av_npproductlineid");

        NewOrderService objNewOrderService= new NewOrderService();

        try{

            logger.info("av_npmodality       : "+av_npmodality);
            logger.info("av_npsolutionid     : "+av_npsolutionid);
            logger.info("av_npproductlineid  : "+av_npproductlineid);

            strMessage= ""+objNewOrderService.validateConfigBafi2300(av_npmodality,av_npsolutionid,av_npproductlineid);

            logger.info("strMessage: "+strMessage);
            PrintWriter out = response.getWriter() ;
            out.print(strMessage);
        }catch(Exception e){
            strMessage =e.getMessage();
            logger.error("ERROR Exception: ",e);
        }
        logger.info("*************** FIN EditOrderSevlet > validateConfigBafi2300 ***************");
        return strMessage;

    }
	
	/***********************************************************************************************************************************
     * <br>Autor  : JCURI
     * <br>project: PRY-1093
     * <br>Motivo : Obtiene lista de transportistas
     * <br>Fecha  : 23/05/2018      
     / ************************************************************************************************************************************/
    public void getCarrierPlaceOfficeList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("EditOrderSevlet/getCarrierPlaceOfficeList");

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		HashMap respJson;
		try {
			int intLugarDespacho = Integer.parseInt(StringUtils.defaultString(request.getParameter("cmbLugarDespacho")));
			String strParamName = StringUtils.defaultString(request.getParameter("strParamName"));
			String strParamStatus = StringUtils.defaultString(request.getParameter("strParamStatus"));
			
			respJson = objOrderService.getCarrierPlaceOfficeList(intLugarDespacho, strParamName, strParamStatus);
			
			Gson gson = new Gson();
			String responseJson = gson.toJson(respJson);
			logger.info("EditOrderSevlet/getCarrierPlaceOfficeList [responseJson] " + responseJson);
			
			out.write(responseJson);
		} catch (Exception e) {
			e.printStackTrace();
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
			response.getWriter().write(e.getMessage());
			response.flushBuffer();
		} finally {
			out.close();
		}
	}
	
    /**
     * @author KPEREZ
     * @project PRY-1037
     * Metodo
     * @return
     */
    public void doValidateSimManager(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
		
        String[] strItemProduct = request.getParameterValues("hdnItemValuetxtItemProduct");


        if( strItemProduct != null && strItemProduct.length>0 && strItemProduct[strItemProduct.length-1]!= "") {
          
			PrintWriter out = response.getWriter();
			String[] pv_item_Product_Val = request.getParameterValues("hdnItemValuetxtItemProduct");
			String strMessage = "";

			Gson gson = new Gson();
			try
			{
				HashMap objReturn = new NewOrderService().doValidateSimManager(pv_item_Product_Val);

				strMessage = (String)objReturn.get("strMessage");

				String msg = gson.toJson(strMessage);
				out.write("{\"strMessage\":" + msg + "}");

				out.close();
			}
			catch (Exception e)
			{
				String msg = gson.toJson("Error EditOrderSevlet.doValidateSimManager" + e.getMessage());
				out.write("{\"strMessage\":" + msg + "}");
				logger.info("ERROR AL VALIDAR  validateSimManager value: " + e.getMessage());
			}
		
		}
    }


    /**
     * @author AMENDEZ
     * @project PRY-1137
     * Metodo   Valida si la especificacion esta configurada para VEP
     *          flag 1, Aplica
     *          flag 0, No aplica
     *          flag -1, Errores
     * @return
     */
    public String validateSpecificationVep(HttpServletRequest request,
                                           HttpServletResponse response) {

        logger.info("*************** INICIO EditOrderSevlet > validateSpecificationVep ***************");
        String strMessage ="";

        String strNpspecificationid=request.getParameter("npspecificationid");

        NewOrderService objNewOrderService= new NewOrderService();

        try{
            int npspecificationid= MiUtil.parseInt(strNpspecificationid);
            logger.info("npspecificationid    : "+npspecificationid);

            strMessage= objNewOrderService.validateSpecificationVep(npspecificationid);

            logger.info("strMessage: "+strMessage);
            PrintWriter out = response.getWriter() ;
            out.print(strMessage);
        }catch(Exception e){
            strMessage =e.getMessage();
            logger.error("ERROR Exception: ",e);
        }
        logger.info("*************** FIN EditOrderSevlet > validateSpecificationVep ***************");
        return strMessage;

    }

    /*PBI000000042016*/
    public void validarNuevoRespPago(HttpServletRequest request,
                                     HttpServletResponse response) throws ServletException, IOException {
        System.out.println("[GeneralServlet]Inicio - validarNuevoRespPago");
        SiteService siteService = new SiteService();
        Long orderId = Long.parseLong((String)request.getParameter("ordenId"));
        Long siteId = null;
        siteId = siteService.getUnknownSite(orderId);
        if(siteId == null){
            siteId = 0L;
        }

        response.getWriter().print(siteId);
        response.getWriter().flush();
    }

    /*PBI000000042016*/
    public void  validarEspecResPago(HttpServletRequest request,
                                     HttpServletResponse response) throws ServletException, IOException {

        System.out.println("[GeneralServlet]Inicio - validarEspecResPago");
        String especificacionId = (String)request.getParameter("especificacionId");

        System.out.println("especificacionId: " + especificacionId);
        GeneralService objGeneralService = new GeneralService();
        HashMap mapEspecificacionResPago = objGeneralService.getTableList("SINC_RESP_SPEC", "a");
        ArrayList <HashMap> arrEspecificacionResPago = (ArrayList <HashMap>)mapEspecificacionResPago.get("arrTableList");
        int contador=0;
        if (arrEspecificacionResPago!=null){
            for(HashMap config: arrEspecificacionResPago){
                System.out.println("especificacion configurada: " + (String)config.get("wv_npValue"));
                if(especificacionId.equals((String)config.get("wv_npValue"))){
                    contador++;
                }
            }
        }
        System.out.println("contador " + contador);
        response.setContentType("text/xml");
        response.getWriter().print(contador);
        response.getWriter().flush();
    }
} 