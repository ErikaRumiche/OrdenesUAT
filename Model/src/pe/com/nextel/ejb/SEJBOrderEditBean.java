package pe.com.nextel.ejb;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Date;
import java.util.Vector;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import pe.com.nextel.bean.*;
import pe.com.nextel.dao.AgreementDAO;
import pe.com.nextel.dao.ApportionmentDAO;
import pe.com.nextel.dao.CategoryDAO;
import pe.com.nextel.dao.ExceptionDAO;
import pe.com.nextel.dao.GeneralDAO;
import pe.com.nextel.dao.ItemDAO;
import pe.com.nextel.dao.OrderDAO;
import pe.com.nextel.dao.ProductDAO;
import pe.com.nextel.dao.ProposedDAO;
import pe.com.nextel.dao.Proveedor;
import pe.com.nextel.dao.RejectDAO;
import pe.com.nextel.dao.SalesDataDAO;
import pe.com.nextel.dao.SiteDAO;
import pe.com.nextel.exception.UserException;
import pe.com.nextel.section.sectionChangeCustomerInfo.SectionChangeCustomerInfoEvents;
import pe.com.nextel.section.sectionItem.SectionItemEvents;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;
import pe.com.portability.dao.PortabilityGeneralDAO;
import pe.com.portability.dao.PortabilityOrderDAO;

public class SEJBOrderEditBean implements SessionBean {

   private static Logger logger = Logger.getLogger(SEJBOrderEditBean.class);
   private SessionContext _context;
   private OrderDAO objOrderDAO=null;
   private RejectDAO objRejectDAO=null;    
   private GeneralDAO objGeneralDAO=null;
   private CategoryDAO objCategoryDAO=null;
   private ExceptionDAO      objExceptionDAO       = null;
   private ItemDAO  objItemDAO = null;
   private ProductDAO  objProductDAO = null;
   private ProposedDAO       objProposedDAO        = null;
   private PortabilityOrderDAO objPortabilityOrderDAO = null;
   private PortabilityGeneralDAO objPortabilityGeneralDAO = null;
   private AgreementDAO      objAgreementDAO       = null;
   private SalesDataDAO objSalesDataDAO = null;
   ApportionmentDAO apportionmentDAO = null;
   SEJBApportionmentBean sejbApportionmentBean = null;
   private SiteDAO objSiteDAO = null;
   
   public void ejbCreate() {
      objOrderDAO=new OrderDAO();
      objRejectDAO=new RejectDAO();  
      objCategoryDAO=new CategoryDAO();
      objGeneralDAO= new GeneralDAO();
      objExceptionDAO= new ExceptionDAO();
      objItemDAO= new ItemDAO();
      objProductDAO= new ProductDAO();
      objProposedDAO = new ProposedDAO();
      objPortabilityGeneralDAO  = new PortabilityGeneralDAO();
      objPortabilityOrderDAO = new PortabilityOrderDAO();
      objAgreementDAO     = new AgreementDAO();
      objSalesDataDAO    = new SalesDataDAO();
      apportionmentDAO = new ApportionmentDAO();
      sejbApportionmentBean = new SEJBApportionmentBean();
      objSiteDAO = new SiteDAO();
   }
   
   public void setSessionContext(SessionContext context) throws EJBException {
   _context = context;
   }
   
   public void ejbRemove() throws EJBException {
   }
   
   public void ejbActivate() throws EJBException {
   }
   
   public void ejbPassivate() throws EJBException {
   } 
   
/***********************************************************************
***********************************************************************
***********************************************************************
*  INTEGRACION DE ORDENES Y RETAIL - INICIO
*  REALIZADO POR: Carmen Gremios
*  FECHA: 13/09/2007
***********************************************************************
***********************************************************************
***********************************************************************/

   public int getTimeStamp(long lOrderId)
   throws Exception, SQLException {
      return objOrderDAO.getTimeStamp(lOrderId);
   }
   
   public String getRichExclusivity(long lCustomerId)
   throws Exception, SQLException {
      return objOrderDAO.getRichExclusivity(lCustomerId);
   }
   
   public HashMap getVendedorRegionId(int iVendedorId) 
   throws Exception, SQLException {
      return objOrderDAO.getVendedorRegionId(iVendedorId);
   }
   
   public String doValidateSalesName(long lCustomerId,int iSiteId,int iSpecialtyId,String strLogin,int iVendedorId,String strVendedorName,int iUserId,int IAppId)
   throws Exception, SQLException {
      return objOrderDAO.doValidateSalesName(lCustomerId,iSiteId,iSpecialtyId,strLogin,iVendedorId,strVendedorName,iUserId,IAppId);
   }
   
   public HashMap getOppOwnershipChngFlag(long lCustomerId,int iSiteId,String strAccMngmnt,int iVendedorId)
   throws Exception, SQLException {
      return objOrderDAO.getOppOwnershipChngFlag(lCustomerId,iSiteId,strAccMngmnt,iVendedorId);
   }
   
   public String getDealer(int iVendedorId) 
   throws Exception,SQLException {
      return objOrderDAO.getDealer(iVendedorId);
   }
   
   public HashMap getOrder(long lNpOrderid) 
   throws Exception, SQLException {
      return objOrderDAO.getOrder(lNpOrderid);
   }
   
   public HashMap getBuildingName(long intBuildingid, String strLogin) 
   throws Exception {        
      return objOrderDAO.getBuildingName(intBuildingid,strLogin); 
   }
   
   public HashMap getDispatchPlaceList(int intSpecialtyId) 
   throws Exception {        
      return objOrderDAO.getDispatchPlaceList(intSpecialtyId);
   }
   
   public HashMap getOrderScreenField(long lOrderId,String strPage) 
   throws Exception, SQLException {
      return objOrderDAO.getOrderScreenField(lOrderId,strPage);
   }    
   
   public HashMap getRejectList(long lNpOrderid)
   throws Exception {
      return objRejectDAO.getRejectList(lNpOrderid);
   }
   
   public  HashMap getBankPaymentDet(int iCodeBank,String strCodeService, String strRuc,
                          String strOperationNumber,String strDateVoucher) 
   throws Exception, SQLException {      
      return objOrderDAO.getBankPaymentDet(iCodeBank,strCodeService,strRuc,strOperationNumber,strDateVoucher);               
   }  
        
  //CPUENTE6
   public String doValidateEquipmentReplacement(long lOrderId)
   throws Exception, SQLException{
      return objOrderDAO.doValidateEquipmentReplacement(lOrderId);
   }
        
  public HashMap updOrder(RequestHashMap request,PortalSessionBean objPortalSesBean) throws Exception,SQLException{
    logger.info("************************ INICIO SEJBOrderEditBean > updOrder(RequestHashMap request,PortalSessionBean objPortalSesBean)************************");
    String   strMessage = null;    
    HashMap  hshResultado=new HashMap();
      OrderBean     objOrderBean = new OrderBean();
      RejectBean    objRejectBean = new RejectBean();
      BudgetBean    budgetBean = new BudgetBean();
      SpecificationBean objSpecificationBean = new SpecificationBean();
      
       Connection conn = null;
       
      try {
         conn = Proveedor.getConnection();        
         //Desactivar el commit automatico de la conexion obtenida
         conn.setAutoCommit(false);     
         
         

            HashMap hshData=null;
            //odubock
            java.sql.Timestamp tsFechaHoraEntrega = null;
            java.sql.Timestamp tsSignDate = null;
             /*jsalazar - modif hpptt # 1 - 30/12/2010 inicio*/
            String strnpdateFinProg=(String)request.getParameter("hdnFechaFinProg");
            java.sql.Date dtScheduleDate2 =null;
             /*jsalazar - modif hpptt # 1 - 30/12/2010 fin */
            //Campos que vendran de la sesion         
            String strPeronId= objPortalSesBean.getPersonid()+"";//"139261"
            String strUserName=objPortalSesBean.getLogin();
            long lOrderId =(request.getParameter("hdnOrderId")==null?0:MiUtil.parseLong(request.getParameter("hdnOrderId")));   
          
            logger.info("lOrderId: " + lOrderId);
          
            long lCustomerId = (request.getParameter("txtCompanyId")==null?0:MiUtil.parseLong(request.getParameter("txtCompanyId")));
            String strOrderCode=(request.getParameter("txtNumSolicitud")==null?"":request.getParameter("txtNumSolicitud"));
            int iDispatchPlaceId =(request.getParameter("hdnLugarDespachoAux")==null?0:MiUtil.parseInt(request.getParameter("hdnLugarDespachoAux")));
            //JOR - 01/04/2008 Captura el TiendaId que se selecciona cuando la orden es SERVICIO TECNICO
            int iTiendaId =(request.getParameter("hdnTiendaId")==null?0:MiUtil.parseInt(request.getParameter("hdnTiendaId")));
            String strPaymentTerms=(request.getParameter("hdnFormaPagoAux")==null?"":request.getParameter("hdnFormaPagoAux"));
            int iCarrierId =(request.getParameter("hdnTransportistaAux")==null?0:MiUtil.parseInt(request.getParameter("hdnTransportistaAux")));
            String strDescription=(request.getParameter("txtDetalle")==null?"":request.getParameter("txtDetalle"));            
            java.sql.Date dtPaymentFutureDate=MiUtil.toFecha(request.getParameter("txtFechaProbablePago"),"dd/MM/yyyy");
            java.sql.Date dtScheduleDate=MiUtil.toFecha(request.getParameter("hdnFechaProceso"),"dd/MM/yyyy");
            //java.sql.Date dtScheduleDate2 = MiUtil.toFecha(request.getParameter("txtFechaReconexion"), "dd/MM/yyyy"); //agregado por rmartinez 16-06-2009
             /*jsalazar - modif hpptt # 1 - 30/12/2010 - inicio*/
            int iSubCategoria =request.getParameter("hdnSubCategoria")==null?0:MiUtil.parseInt(request.getParameter("hdnSubCategoria"));
            if(Constante.SPEC_SERVICIOS_ADICIONALES[0]==iSubCategoria){
            dtScheduleDate2 =MiUtil.toFecha(strnpdateFinProg, "dd/MM/yyyy");
            }else{
            dtScheduleDate2 = MiUtil.toFecha(request.getParameter("txtFechaReconexion"), "dd/MM/yyyy"); //agregado por rmartinez 16-06-2009
            }
           /*jsalazar - modif hpptt # 1 - 30/12/2010 - Fin*/
            
            float fPaymentAmount = (request.getParameter("hdnTotalPaymentOrig")==null?0:Float.parseFloat(request.getParameter("hdnTotalPaymentOrig")));      
            String strImporteFactura = (request.getParameter("txtImporteFacturaTotal")==null?"0":request.getParameter("txtImporteFacturaTotal"));
            float fPaymentTotal = Float.parseFloat(strImporteFactura);
            double lPaymentTotal = MiUtil.parseDouble(strImporteFactura);  
            String motivoRech =(request.getParameter("motivoRech"));
            if("".equals(motivoRech)){
              motivoRech = null;
            }
            long budgetReasonId =(request.getParameter("budgetReasonId")==null?0:MiUtil.parseLong(request.getParameter("budgetReasonId")));
                 
            
            if(request.getParameter("hdnFechaHoraFirma") != null){
              if(request.getParameter("hdnFechaHoraFirma").equals("")){
                 tsSignDate = null;
              }else{
                 tsSignDate =MiUtil.toFechaHora(request.getParameter("hdnFechaHoraFirma"),"dd/MM/yyyy HH:mm");
              }
            }else{
              tsSignDate = null;
            }
            String strDealer = (request.getParameter("txtDealer")==null?"":request.getParameter("txtDealer"));
            logger.info("[strDealer]: "+strDealer);
            String strSalesmanName=(request.getParameter("txtVendedor")==null?"":request.getParameter("txtVendedor"));
            int iProviderGrpId=(request.getParameter("hdnVendedorAux")==null?0:MiUtil.parseInt(request.getParameter("hdnVendedorAux")));         
            String strReject = (request.getParameter("hdnRejects")==null?"":request.getParameter("hdnRejects"));                        
            int iTimeStamp = (request.getParameter("hdnTimeStamp")==null?0:MiUtil.parseInt(request.getParameter("hdnTimeStamp")));            
            int iVendedorId = (request.getParameter("hdnVendedor")==null?0:MiUtil.parseInt(request.getParameter("hdnVendedor")));            
            String strSaveOption = (request.getParameter("v_saveOption")==null?"":request.getParameter("v_saveOption"));            
            String strVendedorValue = (request.getParameter("cmbVendedor")==null?"":request.getParameter("cmbVendedor"));

            logger.info("El hdnLugarDespachoAux que viene de JP_ORDER_EDIT_STAR_ShowPage.jsp = "+request.getParameter("hdnLugarDespachoAux"));
            logger.info("El hdnFormaPagoAux que viene de JP_ORDER_EDIT_STAR_ShowPage.jsp = "+request.getParameter("hdnFormaPagoAux"));
            logger.info("El hdnVendedor que viene de JP_ORDER_EDIT_STAR_ShowPage.jsp = "+request.getParameter("cmbVendedor"));
                        
            String strDeliveryAddress = request.getParameter("hdnDeliveryAddress");
            String strDeliveryCity    =  request.getParameter("hdnDeliveryCity");
            String strDeliveryProvince = request.getParameter("hdnDeliveryProvince");
            String strDeliveryState   = request.getParameter("hdnDeliveryState");  
            String strReference = request.getParameter("txtReference"); // [N_O000017567] MMONTOYA

            // [N_O000017567] MMONTOYA
            // Inicio datos del contacto
            String strContactFirstName = request.getParameter("txtContactFirstName");
            String strContactLastName = request.getParameter("txtContactLastName");
            String strContactDocumentType = request.getParameter("cmbContactDocumentType");
            String strContactDocumentNumber = request.getParameter("txtContactDocumentNumber");
            String strContactPhoneNumber = request.getParameter("txtContactPhoneNumber");
            //INICIO DERAZO REQ-0940
            String strContactEmail = request.getParameter("txtContactEmail");
            String strCheckNotification = request.getParameter("hdnContacNotification");
            //FIN DERAZO
            // Fin datos del contacto

            String strPaymentStatus =(request.getParameter("hdnPagoEstado")==null?"":request.getParameter("hdnPagoEstado")); 
            String strPaymentOrderStatus =(request.getParameter("hdnOrdenPagoAnular")==null?"":request.getParameter("hdnOrdenPagoAnular")); 
            
            
            String strRepresentanteCC = MiUtil.getString((String)request.getParameter("hdnRepresentanteCC"));  
            
            /* Inicio Data */
            String strnpprovidergrpiddata   = (String)request.getParameter("hdnVendedorDataId");
            /* Fin Data */

          //EFLORES N_SD000349095
          String strCarpetaDigital=MiUtil.getString(request.getParameter("chkCarpetaDigital"));
          //EFLORES
            
          //Ini: TDECONV003 KOTINIANO
          String strFlagMigracion=MiUtil.getString(request.getParameter("hdnFlagMigration"));
          //Fin: TDECONV003 KOTINIANO

            //Datos para el WORKFLOW //
            String strCategory=(request.getParameter("txtCategoria")==null?"":request.getParameter("txtCategoria"));                        
            long lDivisionId=(request.getParameter("hdnDivisionId")==null?0:MiUtil.parseLong(request.getParameter("hdnDivisionId")));
            int iPriceType=0;
            int iInboxId=0;
            String strCurrentInbox=(request.getParameter("txtEstadoOrden")==null?"":request.getParameter("txtEstadoOrden"));
            int iInstBepelOrder=(request.getParameter("hdnNpBpelinstanceid")==null?0:MiUtil.parseInt(request.getParameter("hdnNpBpelinstanceid")));            
            String strNpBpelconversationid= (request.getParameter("hdnNpBpelconversationid")==null?"":request.getParameter("hdnNpBpelconversationid"));            
            String strActionName=(request.getParameter("cmbAction")==null?"":request.getParameter("cmbAction"));            
            String strNextInboxName=(request.getParameter("hdnInboxBack")==null?"":request.getParameter("hdnInboxBack"));            

            if(request.getParameter("hdnFechaHoraEntrega") != null && !Constante.ACTION_INBOX_ANULAR.equals(strActionName)){
               if(request.getParameter("hdnFechaHoraEntrega").equals("")){
                  tsFechaHoraEntrega = null;
               }else{
                  tsFechaHoraEntrega =MiUtil.toFechaHora(request.getParameter("hdnFechaHoraEntrega"),"dd/MM/yyyy HH:mm");
               }
            }else{
               tsFechaHoraEntrega = null;
            }
            //Se usdara tambien para secciones dinamicas
            String strSpecificationId = (request.getParameter("hdnSubCategoria")==null?"":request.getParameter("hdnSubCategoria"));
            
            hshData=objCategoryDAO.getSpecificationDetail(MiUtil.parseLong(strSpecificationId));
            logger.info("SEJBOrderEditBean/updOrder/getSpecificationDetail/->"+strMessage);
            strMessage=(String)hshData.get("strMessage");
            if (strMessage!=null)
               throw new Exception(strMessage);
               
            objSpecificationBean=(SpecificationBean)hshData.get("objSpecifBean");
            
            String txtfechaproceso=(String)request.getParameter("txtFechaProceso"); //CBARZOLA 20/08/2009:solucion momentania
            if((txtfechaproceso==null)||(txtfechaproceso.equals("")))
            {
              txtfechaproceso=(String)request.getParameter("hdnFechaProceso");
            }


          //INICIO: PRY-1200 | AMENDEZ
          String strTotalSalesPriceVEP = null;
          String strInicialQuota="";
          String strNpPaymentTermsIQ="";
          String strVepFlag="";
          String strVepNumCuotas="";
          int iFlagVep=0;
          Integer iNumCuotasVEP=null;
          Double dTotalSalesPriceVEP=null;
          Double dInicialQuotaVEP=null;
          Integer iNpPaymentTermsIQVEP=null;

          Object objVepFlag=request.getParameter("chkVepFlag");
          if(objVepFlag!=null){
              strVepFlag=(String)objVepFlag;
              iFlagVep=MiUtil.parseInt(strVepFlag);
          }

          Object objVepNumCuotas =request.getParameter("cmbVepNumCuotas");
          if( iFlagVep== 1 && objVepNumCuotas!=null){
              strVepNumCuotas=(String)objVepNumCuotas;
              iNumCuotasVEP=MiUtil.parseInt(strVepNumCuotas);
          }

          Object objTotalSalesPriceVEP=request.getParameter("hdnTotalSalesPriceVEP");
          if ( iFlagVep== 1 && objTotalSalesPriceVEP != null) {
              strTotalSalesPriceVEP = (String) objTotalSalesPriceVEP;
              dTotalSalesPriceVEP=MiUtil.parseDouble(strTotalSalesPriceVEP);
          }

            Object objCuotaInicial=request.getParameter("txtCuotaInicial");
          if( iFlagVep== 1 && objCuotaInicial!=null){
                strInicialQuota=(String)objCuotaInicial;
              dInicialQuotaVEP=MiUtil.parseDouble(strInicialQuota);
            }
            
          Object objPaymentTermsIQ=request.getParameter("txtPaymentTermsIQ");
          if( iFlagVep== 1 && objPaymentTermsIQ!=null){
              strNpPaymentTermsIQ=(String)objPaymentTermsIQ;
              iNpPaymentTermsIQVEP=MiUtil.parseInt(strNpPaymentTermsIQ);
          }
          //FIN: PRY-1200 | AMENDEZ
            
			      /*Inicio CEM*/
			      strMessage=objOrderDAO.validateDateProcess(lOrderId,strUserName,objSpecificationBean.getNpSpecificationId(),txtfechaproceso,strCurrentInbox,strActionName);
            logger.info("SEJBOrderEditBean/updOrder/validateDateProcess/->"+strMessage);
            if (strMessage!=null)
               throw new Exception(strMessage); 			
           
            /*Fin CEM*/
            
            //Validación de Acuerdos Comerciales
            strMessage=agreementValidations(request,conn);
            logger.info("SEJBOrderEditBean/updOrder/agreementValidations/->"+strMessage);
            if (strMessage!=null)
               throw new Exception(strMessage); 
            
            int iTimeStampBD=objOrderDAO.getTimeStamp(lOrderId);  
            if (iTimeStampBD!=iTimeStamp)
               throw new Exception("ERRORTIMESTAMP");          
            
            java.sql.Date dCancel=null;     
            
            
            //Entra al metodo de pago en banco por voucher (Validacion si el voucher es nullo o vacio dentro del metodo)
            //String strNroVoucher=strNroVoucher=request.getParameter("hdnPagoNroVoucher");
            strMessage = doExecutePaymentVoucher(request,objPortalSesBean);
            logger.info("SEJBOrderEditBean/updOrder/doExecutePaymentVoucher/->"+strMessage);
            if (strMessage!=null)
               throw new Exception(strMessage);   
               
            objOrderBean.setNpOrderId(lOrderId);
            objOrderBean.setNpOrderCode(strOrderCode);
            objOrderBean.setNpDispatchPlaceId(iDispatchPlaceId);
            objOrderBean.setNpPaymentTerms(strPaymentTerms);
            objOrderBean.setNpCarrierId(iCarrierId);
            objOrderBean.setNpDescription(strDescription);
            objOrderBean.setNpPaymentFutureDate(dtPaymentFutureDate);
            objOrderBean.setNpScheduleDate(dtScheduleDate);
            objOrderBean.setNpScheduleDate2(dtScheduleDate2); //agregado por rmartinez 16-06-2009
            objOrderBean.setNpPaymentAmount(fPaymentAmount);
            objOrderBean.setNpPaymentTotal(fPaymentTotal);
            objOrderBean.setNpSignDate(tsSignDate);
            objOrderBean.setNpSalesmanName(strSalesmanName);
            objOrderBean.setNpDealerName(strDealer);            
            
            objOrderBean.setNpModificationBy(strUserName);
            if (strSpecificationId.equals("2028") || strSpecificationId.equals("2029") || strSpecificationId.equals("2030") || strSpecificationId.equals("2031") ){
                objOrderBean.setNpProviderGrpId(MiUtil.parseInt(strVendedorValue));
            }else{
                objOrderBean.setNpProviderGrpId(iProviderGrpId);
            }
            objOrderBean.setNpModificationDate(MiUtil.getDatePlSql());
            objOrderBean.setNpTimeStamp(iTimeStamp);
            objOrderBean.setNpDeliveryDate(tsFechaHoraEntrega);
            objOrderBean.setNpShipToAddress1(strDeliveryAddress);
            objOrderBean.setNpShipToCity(strDeliveryCity);
            objOrderBean.setNpShipToProvince(strDeliveryProvince);
            objOrderBean.setNpShipToState(strDeliveryState);
            objOrderBean.setNpShipToReference(strReference); // [N_O000017567] MMONTOYA

            // [N_O000017567] MMONTOYA
            // Inicio datos del contacto
            OrderContactBean orderContactBean = new OrderContactBean();
            orderContactBean.setFirstName(strContactFirstName);
            orderContactBean.setLastName(strContactLastName);
            orderContactBean.setDocumentType(strContactDocumentType);
            orderContactBean.setDocumentNumber(strContactDocumentNumber);
            orderContactBean.setPhoneNumber(strContactPhoneNumber);
            //INICIO DERAZO REQ-0940
            orderContactBean.setEmail(strContactEmail);
            orderContactBean.setCheckNotification(strCheckNotification);
            System.out.println("orderContactBean.getEmail:" + orderContactBean.getEmail());
            System.out.println("orderContactBean.getCheckNotification:" + orderContactBean.getCheckNotification());
            //FIN DERAZO
            objOrderBean.setOrderContactBean(orderContactBean);
            // Fin datos del contacto

            //JOR - Se carga el campo Building ID
            objOrderBean.setNpBuildingId(iTiendaId);
            
            //Representante CC asignado por BackOffice
            objOrderBean.setNpUserName1(strRepresentanteCC);
            /* Inicio Data */
            objOrderBean.setNpProviderGrpIdData( MiUtil.parseLong(strnpprovidergrpiddata) );
            logger.info("getNpProviderGrpIdData===="+objOrderBean.getNpProviderGrpIdData());
            /* Fin Data */
            

            
            //[Despacho en Tienda] PCASTILLO
            objOrderBean.setNpFlagCourier(MiUtil.parseInt((String)request.getParameter("hdnChkCourier")));
            logger.info("hdnChkCourier: "+objOrderBean.getNpFlagCourier());

              //EFLORES N_SD000349095
              objOrderBean.setNpCarpetaDigital(strCarpetaDigital);
              //EFLORES

          //INICIO: PRY-1200 | AMENDEZ
          objOrderBean.setNpFlagVep(iFlagVep);
          objOrderBean.setNpNumCuotas(iNumCuotasVEP);
          objOrderBean.setNpAmountVep(dTotalSalesPriceVEP);
          objOrderBean.setInitialQuota(dInicialQuotaVEP);
          objOrderBean.setNpPaymentTermsIQ(iNpPaymentTermsIQVEP);
          //FIN: PRY-1200 | AMENDEZ

          //Ini: TDECONV003 KOTINIANO
          objOrderBean.setNpFlagMigracion(strFlagMigracion);
          //Fin: TDECONV003 KOTINIANO

            iInboxId=objOrderDAO.getGetInbox(strCurrentInbox);

            if (iInboxId<0)
               throw new Exception("La Orden no tiene estado");

            /**POR KSALVADOR
            * Anulación de Ordendes de Pago generadas en Cashdesk 
            * Fecha:02/03/2009
            * Fecha:15/03/2008
            * Fecha:10/10/2008 : Se elimina la anulación de OP desde ordenes, se realizará al momento de generar la OP y la cancelar la orden
            * * */
              //Verificamos que el flag de anulación de Orden de Pago sea "S"
              //-------------------------------------------------------------
             if  ( (strPaymentOrderStatus.equals("S")) &&  (!strPaymentTerms.equalsIgnoreCase("CONTADO"))) {
                 strMessage= objOrderDAO.updPaymentOrderAnul(strUserName, lOrderId,conn);
                 if (strMessage!=null)
                   throw new Exception(strMessage); 
                 logger.info("SEJBOrderEditBean/updOrder/Fin de Anulación de Orden de Pago");
             }
             
             
            
            //Actualización del Cliente en el INBOX DE EDICION , para la categoria 2015
            //--------------------------------------------------------------------------
            if ( (strCurrentInbox.equals(Constante.INBOX_EDICION)) && (strSpecificationId.equals("2015")) ){
                logger.info("SEJBOrderEditBean/updOrder/Inicio Actualización de Datos del cliente");
                logger.info("Empieza actualización de datos del cliente al cerrar la orden ");
                SectionChangeCustomerInfoEvents objChangeCustomer=new SectionChangeCustomerInfoEvents();
                strMessage=objChangeCustomer.updChangeCustomerInfo(request,conn);
                logger.info("SEJBOrderEditBean/updOrder/updChangeCustomerInfo/->"+strMessage);
                 if (strMessage!=null)
                      throw new Exception(strMessage); 
            }
            logger.info("SEJBOrderEditBean/updOrder/Fin Actualización de Datos del cliente");
            
            //Se actualiza la Orden   
            logger.info("SEJBOrderEditBean/updOrder/updOrder/Inicio Actualización de la Orden");

            try{
                if((strSpecificationId.equals("2068")) || (strSpecificationId.equals("2069"))){
            Date fechaincio = new Date();
                    logger.info("[SEJBORDEREDITBEAN][updOrder][PM0010354] : INICIO");
                    logger.info("[SEJBORDEREDITBEAN][updOrder][PM0010354] : OrderId -->" +lOrderId);
                    logger.info("[SEJBORDEREDITBEAN][updOrder][PM0010354] : Fecha -->" + fechaincio);
                    logger.info("[SEJBORDEREDITBEAN][updOrder][PM0010354] : Inbox -->" + strCurrentInbox);
                    logger.info("[SEJBORDEREDITBEAN][updOrder][PM0010354] : SpecificationId -->" + strSpecificationId);
                }
            }catch(Exception e){
                strMessage = e.getMessage();
                logger.info("[SEJBORDEREDITBEAN][updOrder][PM0010354] : ERROR AL INTENTAR PINTAR LOG INICIO -->" + strMessage);
            }



            logger.info("objOrderBean.toString() " + objOrderBean.toString());
            strMessage=objOrderDAO.updOrder(objOrderBean,conn);      
            //int iOrderPermEdit=0;
            if (strMessage!=null)
               throw new Exception(strMessage);
               logger.info("SEJBOrderEditBean/updOrder/Fin Actualización de la Orden");
            //Presupuesto
            if(Constante.INBOX_PRESUPUESTO.equals(strCurrentInbox)){
              budgetBean.setNpgeneratorid(lOrderId);
              budgetBean.setNpgeneratortype(Constante.GENERATOR_TYPE_ORDER);
              budgetBean.setNpapprovalaction(strActionName);
              budgetBean.setNpBudgetReasonId(budgetReasonId);
              budgetBean.setNpdescriptionReason(motivoRech);
              strMessage=objOrderDAO.updApprovalAction(budgetBean, objPortalSesBean);
              if (strMessage!=null){
                 throw new Exception(strMessage);
              }
            }
            
            //PRY-0890 JCURI PRORRATEO
            HashMap  hshResultadoPA = validarPagoAnticipado(hshResultado, request, objOrderBean, strActionName, strSpecificationId, strActionName, lCustomerId, lDivisionId, strUserName, strCurrentInbox, conn);
            String invokeSectionDynamics= (String) hshResultadoPA.get("invokeSectionDynamics");
            logger.info("SEJBOrderEditBean/updOrder [invokeSectionDynamics] " + invokeSectionDynamics);
            ItemBean objItemProrrateo = (ItemBean) hshResultado.get("objItemProrrateo");
            String strNextInboxNamePA = (String) hshResultado.get("strNextInboxNamePA");
            logger.info("SEJBOrderEditBean/updOrder/strNextInboxNamePA/->"+strNextInboxNamePA);            
            
            if("S".equals(invokeSectionDynamics)){
            //Secciones Dinamicas
            logger.info("SEJBOrderEditBean/updOrder/invokeSectionDynamics/Inicio Actualización de las Sección Dinámica -> " + objSpecificationBean.getNpSpecification());
            strMessage  = invokeSectionDynamics(MiUtil.parseInt(strSpecificationId),request,conn);
            logger.info("SEJBOrderEditBean/updOrder/invokeSectionDynamics/->"+strMessage);
            if (strMessage!=null)
               throw new Exception(strMessage);
    
                logger.info("SEJBOrderEditBean/updOrder/invokeSectionDynamics/Fin Actualización de las Sección Dinámica -> " + objSpecificationBean.getNpSpecification());
                }

            strMessage = objOrderDAO.updOpportunityUnits(lOrderId,conn);
            logger.info("SEJBOrderEditBean/updOrder/updOpportunityUnits/->"+strMessage);
            if (strMessage!=null){
               throw new Exception(strMessage);
            }
            
            //Actualiza excepciones de la orden
            //JPEREZ
            logger.info("Actualiza excepciones de la orden");
            strMessage=objOrderDAO.updateException(objOrderBean,conn);
            logger.info("SEJBOrderEditBean/updOrder/updateException/->"+strMessage);
            if (strMessage!=null)
               throw new Exception(strMessage);
               
            
            //JPEREZ  - Periodos de Excepciones            
            request.put("hdnSessionLogin", strUserName);
            String strBeginPeriods  =   (String)request.get("hdnPeriodoIni");
            String strEndPeriods    =   (String)request.get("hdnPeriodoFin");
            if ( (strBeginPeriods.compareTo("null")!=0) && (strEndPeriods.compareTo("null")!=0) ){
              strMessage = doDeleteOrderPeriod(request, conn);
              if (strMessage!=null){
                if (conn != null) conn.rollback();
                 throw new Exception(strMessage);
              }
              strMessage = doUpdateOrderPeriod(request, conn);
              if (strMessage!=null){
                 if (conn != null) conn.rollback();
                 throw new Exception(strMessage);
              }
            }
            
            //Validaciones para Ventas Data
             //-----------------------------
           String strStatusSalesData=(request.getParameter("hdnStatusData")==null?"":request.getParameter("hdnStatusData"));
            if (strStatusSalesData!=""){
              
              //Actualiza estado de la aplicación
              //---------------------------------
              strMessage = objSalesDataDAO.doUpdateSalesDataStatus(lOrderId,lCustomerId,lDivisionId,12,strStatusSalesData, strUserName, conn);
              if (strMessage!=null){
                 if (conn != null) conn.rollback();
                 throw new Exception(strMessage);
              }
             
              
              //Validaciones de Ventas Data
              //---------------------------
              strMessage = objSalesDataDAO.getValidateServiceApp(lOrderId,lCustomerId,lDivisionId, conn);
              if (strMessage!=null){
                 if (conn != null) conn.rollback();
                 throw new Exception(strMessage);
              }
              
            }

            //PBI000000042016
            String especId   = (request.get("hdnSpecification")==null?"":(String)request.get("hdnSpecification"));
            Long longEspecId  = MiUtil.parseLong(especId);

            HashMap mapEspecificacionResPago = objGeneralDAO.getTableList("SINC_RESP_SPEC", "a");
            ArrayList <HashMap> arrEspecificacionResPago = (ArrayList <HashMap>)mapEspecificacionResPago.get("arrTableList");

            if(arrEspecificacionResPago != null && arrEspecificacionResPago.size()>0) {
              for (int i = 0; i < arrEspecificacionResPago.size(); i++) {
                  if (arrEspecificacionResPago.get(i).get("wv_npValue") == longEspecId) {
                      logger.info("PBI000000042016 del site de la Orden");
                      updEditSiteOrdenVep(request, conn);
                  }
              }
             }

            //Inicio Reserva de Numeros Golden - 15/11/2010 - FPICOY
            if (Integer.parseInt(strSpecificationId)==Constante.SPEC_POSTPAGO_PORTA || Integer.parseInt(strSpecificationId)==Constante.SPEC_CAMBIO_NUMERO){ 
              hshResultado=this.reserveGoldenNumber(request,conn,Integer.parseInt(strSpecificationId));
              if( ((String)hshResultado.get("strMessage"))!=null){
                return hshResultado;
              }
            }
            //Fin Reserva de Numeros Golden - 15/11/2010 - FPICOY
            System.out.println("Prueba de cambio");
            if (strMessage!=null && !strMessage.startsWith("dbmessage"))
               strMessage="dbmessage"+strMessage;
            else{
                
               /*Si se confirma que la operación va a proceder con el commit, se invoca a rechazos*/ 
               String[] strRejectArray;
               strRejectArray = StringUtils.splitPreserveAllTokens(strReject, "|");   
               
               logger.info("Longitud del vector de rechazos - Lee Rosales-->"+strRejectArray!=null?strRejectArray.length:0);
               
               logger.info("SEJBOrderEditBean/updOrder/Inicio Rechazos");
               //SECCION RECHAZOS - Incio MMONTOYA
               try {
                   for(int i = 0; i < strRejectArray.length-1; i=i+11){                 
                      objRejectBean=new RejectBean();            
                      if ("".equals(strRejectArray[i]) || strRejectArray[i]==null){ //nuevo rechazo
                         objRejectBean.setNpOrderId(lOrderId);
                         objRejectBean.setNpReason(strRejectArray[i+2]);
                         objRejectBean.setNpComment(strRejectArray[i+3]); 
                         objRejectBean.setNpStatus(strRejectArray[i+4]);
                         objRejectBean.setNpCreatedBy(strRejectArray[i+5]);
                         objRejectBean.setNpInbox(strRejectArray[i+9]);
                         strMessage=objRejectDAO.insReject(objRejectBean,conn);
                         if (strMessage!=null)
                            break;
                      }else{//rechazo modificado
                         if ("true".equals(strRejectArray[i+10])){
                            objRejectBean.setNpRejectId(Integer.parseInt(strRejectArray[i]));
                            objRejectBean.setNpStatus(strRejectArray[i+4]);
                            objRejectBean.setNpModifiedBy(strRejectArray[i+7]);
                            strMessage=objRejectDAO.updReject(objRejectBean,conn);
                            if (strMessage!=null)
                               break;
                         }
                      }
                   } 
               } catch (Exception e) {                
                   e.printStackTrace();
                   throw e;
               }
               // Fin MMONTOYA

                logger.info("SEJBOrderEditBean/updOrder/insReject/updReject/->"+strMessage);
               if (strMessage!=null)
                  throw new Exception(strMessage);

                logger.info("SEJBOrderEditBean/updOrder/Fin Rechazos");
                
            
               conn.commit();            
            }
            
            hshResultado.put("strOrderId",lOrderId+"");
            hshResultado.put("strSaveOption",strSaveOption); 
            hshResultado.put("strInboxId",iInboxId+""); 
            hshResultado.put("strOldInboxName",strCurrentInbox);      
            hshResultado.put("strMessage",strMessage);
            //CPUENTE5
            hshResultado.put("strSpecificationId",strSpecificationId);
            
            //Datos para BPEL
            hshResultado.put("strCustomerId",lCustomerId+"");               
            hshResultado.put("strDivisionId",lDivisionId+"");
            hshResultado.put("strNpBpelconversationid",strNpBpelconversationid);   
            hshResultado.put("strActionName",strActionName);   
            hshResultado.put("strNextInboxName",strNextInboxName);   
            hshResultado.put("objSpecificationBean",objSpecificationBean);
            hshResultado.put("objItemProrrateo",objItemProrrateo); //JCURI PRY-0890
            hshResultado.put("strNextInboxNamePA",strNextInboxNamePA); //JCURI PRY-0890

            logger.info("--------------- Inicio SEJBOrderEditBean.java ------------------");
            logger.info("strOrderId-->"+lOrderId+"");
            logger.info("lDivisionId-->"+lDivisionId+"");
            logger.info("strSaveOption-->"+strSaveOption);
            logger.info("strInboxId-->"+iInboxId+"");
            logger.info("strCurrentStatus-->"+strCurrentInbox);
            logger.info("strNextInboxName-->"+(String) hshResultado.get("strNextInboxName"));
            logger.info("strNextInboxNamePA-->"+(String) hshResultado.get("strNextInboxNamePA"));
            
            logger.info("strMessage-->"+strMessage);
            logger.info("strSpecificationId-->"+strSpecificationId);//CPUENTE5
            logger.info("--------------- Fin SEJBOrderEditBean.java ------------------");
         
            try{
                if((strSpecificationId.equals("2068")) || (strSpecificationId.equals("2069"))){
                    Date fechaFin = new Date();
                    logger.info("[SEJBORDEREDITBEAN][updOrder][PM0010354] : Fecha -->" + fechaFin);
                    logger.info("[SEJBORDEREDITBEAN][updOrder][PM0010354] : FIN");
                }
         } catch (Exception e) {
                strMessage = e.getMessage();
                logger.info("[SEJBORDEREDITBEAN][updOrder][PM0010354] : ERROR AL INTENTAR PINTAR LOG FIN -->" + strMessage);
            }
         
         
        } 
      catch (UserException ex) {
    	  if (conn != null) conn.rollback();
          strMessage= ex.getMessage();
          hshResultado.put("strMessage",strMessage);
      }
        catch (Exception e) {
            if (conn != null) conn.rollback();
               strMessage= e.getMessage();
               e.printStackTrace();

               strMessage= "[Exception][Fallo la conexion]["+e.getClass() + " " + e.getMessage()+"][updOrder]";
          logger.error(strMessage);
               hshResultado.put("strMessage",strMessage);			   
			   
         } finally {
		 
          if (conn != null){
                conn.close();              
             }           
        }
    
      logger.info("************************ FIN SEJBOrderEditBean > updOrder(RequestHashMap request,PortalSessionBean objPortalSesBean)************************");
      return hshResultado;
   }

/***********************************************************************
***********************************************************************
***********************************************************************
*  ACTUALIZAR ORDEN DESDE DETAIL
*  REALIZADO POR: Karen Salvador
*  FECHA: 02/01/2008
***********************************************************************
***********************************************************************
***********************************************************************/
 public HashMap updOrderDetail(RequestHashMap request,PortalSessionBean objPortalSesBean)throws Exception,SQLException{    
      String   strMessage = null;    
      HashMap  hshResultado=new HashMap(); 
      System.out.println("------------------------------ Inicio UpdOrderDetail / SEJBOrderEditBean.java --------------------");
      
       Connection conn =null;
      try {
          
        //Obtener la conexion del pool de conexiones (DataSource)
         conn = Proveedor.getConnection();        
         //Desactivar el commit automatico de la conexion obtenida
         conn.setAutoCommit(false);     
                        
           long lOrderId =(request.getParameter("hdnOrderId")==null?0:MiUtil.parseLong(request.getParameter("hdnOrderId")));   
           String strSaveOption = (request.getParameter("v_saveOption")==null?"":request.getParameter("v_saveOption")); 
           long lCustomerId = (request.getParameter("txtCompanyId")==null?0:MiUtil.parseLong(request.getParameter("txtCompanyId")));
           
           
           //Datos para el WORKFLOW
           //----------------------
           String strCategory=(request.getParameter("txtCategoria")==null?"":request.getParameter("txtCategoria"));                       
			     long lDivisionId=(request.getParameter("hdnDivisionId")==null?0:MiUtil.parseLong(request.getParameter("hdnDivisionId")));
           int iPriceType=0;
           int iInboxId=0;
           String strCurrentInbox=(request.getParameter("txtEstadoOrden")==null?"":request.getParameter("txtEstadoOrden"));
           int iInstBepelOrder=(request.getParameter("hdnNpBpelinstanceid")==null?0:MiUtil.parseInt(request.getParameter("hdnNpBpelinstanceid")));            
           String strNpBpelconversationid= (request.getParameter("hdnNpBpelconversationid")==null?"":request.getParameter("hdnNpBpelconversationid"));            
           String strActionName=(request.getParameter("cmbAction")==null?"":request.getParameter("cmbAction"));            
           String strNextInboxName=(request.getParameter("hdnInboxBack")==null?"":request.getParameter("hdnInboxBack"));    
           String strSpecificationId = (request.getParameter("hdnSubCategoria")==null?"":request.getParameter("hdnSubCategoria")); 
           SpecificationBean objSpecificationBean=new SpecificationBean();
           HashMap hshData=null;
           
                    
           hshData=objCategoryDAO.getSpecificationDetail(MiUtil.parseLong(strSpecificationId));
           strMessage=(String)hshData.get("strMessage");
            if (strMessage!=null)
               throw new Exception(strMessage);            
            objSpecificationBean=(SpecificationBean)hshData.get("objSpecifBean");
            
           //Verificamos si la Orden se encuentra en capa
           //--------------------------------------------
           if(strCurrentInbox.equals(Constante.INBOX_PROCESOS_AUTOMATICOS)){
              hshData=objOrderDAO.getSHedulestatus(lOrderId);
              strMessage=(String)hshData.get("strMessage");
                if (strMessage!=null)
                    throw new Exception(strMessage);            
           }
             
           //Entra al metodo de pago en banco por voucher (Validacion si el voucher es nullo o vacio dentro del metodo)
           //------------------------------------------------------------------------------------------------------------
           strMessage = doExecutePaymentVoucher(request,objPortalSesBean);
           System.out.println("[updOrderDetail] strMessage:"+strMessage);
            if (strMessage!=null)
               throw new Exception(strMessage); 
           
           
           //Se verifica si se debe actualizae
           //------------------------------------------------
           String strValue=null;
           HashMap hshPermissionDocument = objPortabilityGeneralDAO.getSectionDocumentValidate("SECTION_DOCUMENT",strCurrentInbox,strValue); 
           strMessage=(String)hshPermissionDocument.get("strMessage");
           if (strMessage!=null)
              throw new Exception(strMessage);  
           strValue = (String)hshPermissionDocument.get("strValue");
          
          //Se actualiza la Sección Dinámica de Portabilidad
          //------------------------------------------------
          if (strValue!=null){
          
              String strDocument=(request.getParameter("txtDocumento")==null?request.getParameter("hdnDocumento"):request.getParameter("txtDocumento"));
              String strTypeDocument=(request.getParameter("cmbDocumento")==null?request.getParameter("hdnTypeDocumento"):request.getParameter("cmbDocumento"));
              
               //Validaciones de los valores antes de la actualización
               //-----------------------------------------------------
              strMessage = objPortabilityOrderDAO.getValidateDocument(lOrderId,strDocument,strTypeDocument,conn);
              if (strMessage!=null)
                throw new Exception(strMessage); 
                  
              strMessage = objPortabilityOrderDAO.updatePortabilityOrderDetail(lOrderId, strDocument,strTypeDocument);
               if (strMessage!=null)
                 throw new Exception(strMessage); 
          }
          
            //Datos para la ORDEN
            //-------------------
            hshResultado.put("strOrderId",lOrderId+"");
            hshResultado.put("strSaveOption",strSaveOption); 
            hshResultado.put("strInboxId",iInboxId+""); 
            hshResultado.put("strOldInboxName",strCurrentInbox);      
            hshResultado.put("strMessage",strMessage);
            
            //Datos para BPEL
            //----------------
            hshResultado.put("strCustomerId",lCustomerId+"");            
				    hshResultado.put("strDivisionId",lDivisionId+"");
            hshResultado.put("strNpBpelconversationid",strNpBpelconversationid);
            hshResultado.put("strActionName",strActionName);
            hshResultado.put("strNextInboxName",strNextInboxName);
            hshResultado.put("objSpecificationBean",objSpecificationBean);
            
         
            System.out.println("--------------- Inicio SEJBOrderEditBean.java ------------------");         
            System.out.println("strOrderId-->"+lOrderId+"");         
            System.out.println("strSaveOption-->"+strSaveOption);                            
            System.out.println("strInboxId-->"+iInboxId+"");         
            System.out.println("strCurrentStatus-->"+strCurrentInbox);         
            System.out.println("strMessage-->"+strMessage);                
            System.out.println("--------------- Fin SEJBOrderEditBean.java ------------------");    
            
        } catch (Exception e) {
                if (conn != null){
                  conn.close();                
                }
               strMessage= e.getMessage();
               //e.printStackTrace();      
               strMessage= "[Exception][Fallo la actualización]["+ e.getMessage()+"]";
               hshResultado.put("strMessage",strMessage);
         } finally {   
            if (conn != null){
              conn.close();             
            }
         } 
     
  
      
      return hshResultado;
   }
   
       
/***********************************************************************
***********************************************************************
***********************************************************************
*  PAGO EN BANCO CON VOUCHER
*  REALIZADO POR: Karen Salvador
*  FECHA: 31/01/2008
***********************************************************************
***********************************************************************
***********************************************************************/
   
public String doExecutePaymentVoucher(RequestHashMap request,PortalSessionBean objPortalSesBean){
   
    String strMessage=null;
    HashMap  hshResultado=new HashMap();   
    String strNroVoucher=strNroVoucher=request.getParameter("hdnPagoNroVoucher");
  
    
    if (strNroVoucher!=null && !"".equals(strNroVoucher)){   
        
        System.out.println("------------------------------ Inicio doExecutePaymentVoucher / SEJBOrderEditBean.java --------------------");        
        long lOrderId =(request.getParameter("hdnOrderId")==null?0:MiUtil.parseLong(request.getParameter("hdnOrderId")));  
        String strUserName=objPortalSesBean.getLogin();    
        double  lPaymentTotal = (request.getParameter("txtImporteFactura")==null?0:Double.valueOf(request.getParameter("txtImporteFactura")).doubleValue());
        double lPaymentAmount = (request.getParameter("txtnpTotalPayment")==null?0:Double.valueOf(request.getParameter("txtnpTotalPayment")).doubleValue());      
    
        System.out.println("[doExecutePaymentVoucher] lPaymentTotal:"+lPaymentTotal);
        System.out.println("[doExecutePaymentVoucher] lPaymentAmount:"+lPaymentAmount);

        try{
          Connection conn = null;
              try {      
                  
                  //Obtener la conexion del pool de conexiones (DataSource)
                  conn = Proveedor.getConnection();        
                  //Desactivar el commit automatico de la conexion obtenida
                  conn.setAutoCommit(false);     
               
                   int iCodeBank =(request.getParameter("hdnPagoBanco")==null?0:MiUtil.parseInt(request.getParameter("hdnPagoBanco")));         
                   String strRuc=(request.getParameter("txtRucDisabled")==null?"":request.getParameter("txtRucDisabled"));                     
                   strNroVoucher=strNroVoucher.toString();
                   String strFechaPago=(request.getParameter("hdnPagoFecha")==null?"":request.getParameter("hdnPagoFecha")); 
                   
                   if (lPaymentTotal > 0 && lPaymentTotal>=lPaymentAmount){
                   
                   System.out.println("[doExecutePaymentVoucher] strNroVoucher:"+strNroVoucher);
                   System.out.println("[doExecutePaymentVoucher] iCodeBank:"+iCodeBank);
                   System.out.println("[doExecutePaymentVoucher] strFechaPago:"+strFechaPago);
                   System.out.println("[doExecutePaymentVoucher] lPaymentTotal:"+lPaymentTotal);
                   System.out.println("[doExecutePaymentVoucher] Constante.CODE_SERVICE:"+Constante.CODE_SERVICE);
                   System.out.println("[doExecutePaymentVoucher] lOrderId"+lOrderId);
                   System.out.println("[doExecutePaymentVoucher] strUserName"+strUserName);
                   System.out.println("[doExecutePaymentVoucher] lPaymentAmount"+lPaymentAmount);
                   
                   HashMap hshResult=objOrderDAO.updBankPayment(strNroVoucher,iCodeBank,strFechaPago,lPaymentTotal,Constante.CODE_SERVICE,
                                                                   lOrderId, strUserName, lPaymentAmount,conn);  
                                                                   
                   strMessage =(String)hshResult.get("strMessage");  
                      if (strMessage!=null)
                       throw new Exception(strMessage);
               
                     
                    }
                 }catch (Exception e){
                    if (conn != null) conn.rollback();
                       strMessage= e.getMessage();
                       e.printStackTrace();               
                 } finally {
                    if (conn != null){
                      conn.close();                     
                    }
                 } 
               
            } catch (Exception e) { //Error al obtener la conexion
                e.printStackTrace();
                strMessage= "[Exception][Fallo la conexion]["+e.getClass() + " " + e.getMessage()+"][doExecutePaymentVoucher]";
          }
          
          }else{
            strMessage=null;
          }
    
    return  strMessage;
    
   }
   
   
   
   public String invokeSectionDynamics(long strSpecificationId, RequestHashMap objHashMap,Connection conn) throws Exception{
      logger.info("************************ INICIO SEJBOrderEditBean > invokeSectionDynamics(long strSpecificationId, RequestHashMap objHashMap,Connection conn)************************");
      String strNameMethod="";
      String strNameClass="";
      String strMessage = null;
      Method method;
      Object objeto;
      Class clase;
      Class clase1;
      Class clase2;
      Class clase3;
      
      ArrayList arrLista = new ArrayList();
      SectionDinamicBean sectionDinamicBean = new SectionDinamicBean();
      HashMap hshData=null;
      
      hshData = objCategoryDAO.getSpecificationData(strSpecificationId, null);       
        
      strMessage=(String)hshData.get("strMessage");   
      if (strMessage!=null)
         throw new Exception(strMessage);                  
               
      arrLista = (ArrayList)hshData.get("objArrayList");   
      
      logger.info("strSpecificationId: "+strSpecificationId);  
      logger.info("objHashMap: "+objHashMap);     
      
      for(int i=0;i< arrLista.size();i++ ){
         logger.info("[EDIT][invokeSectionDynamics] Npeventname : " + sectionDinamicBean.getNpeventname());
         sectionDinamicBean = (SectionDinamicBean)arrLista.get(i);
         if (Constante.EDIT_ON_SAVE.equals(MiUtil.getString(sectionDinamicBean.getNpeventname()))){
            logger.info("[EDIT][invokeSectionDynamics] [EDIT_ON_SAVE] : " + sectionDinamicBean.getNpeventname());
        	 
            strNameMethod    =   MiUtil.getString(sectionDinamicBean.getNpeventhandler());
            strNameClass     =   MiUtil.getString(sectionDinamicBean.getNpobjectname());     
            
            clase=Class.forName(strNameClass);
            System.out.println(" Nombre de la clase " + strNameClass);                        
            System.out.println(" Nombre del metodo " + strNameMethod);                        
            clase1=Class.forName("pe.com.nextel.util.RequestHashMap");
            clase2=Class.forName("java.sql.Connection");
            objeto=clase.newInstance();
            
            method = clase.getMethod(strNameMethod,new Class[] { clase1, clase2});                   
            strMessage =(String)method.invoke(objeto, new Object[] { objHashMap , conn});  
            
            if( strMessage!=null){            
               break;
            }
         } //Fin del if
      }//Fin del for
      logger.info("************************ FIN SEJBOrderEditBean > invokeSectionDynamics(long strSpecificationId, RequestHashMap objHashMap,Connection conn)************************");
      return strMessage;
   
   }
  
  
  
  /*  public String insReject(RejectBean objReject) 
    throws Exception {        
        return objRejectDAO.insReject(objReject);
    }
    
    public String updReject(RejectBean objRejectB) 
    throws Exception {      
       return objRejectDAO.updReject(objRejectB);               
    }   

   public HashMap updPaymentBank(int iCodeBank,String strCodeService,String strRuc,String strNroVoucher,String strFechaPago)
    throws Exception {      
       return objOrderDAO.updPaymentBank(iCodeBank,strCodeService,strRuc,strNroVoucher,strFechaPago);               
    }  */

   /* public String updOrder(OrderBean objOrderB) 
    throws Exception {  
        return objOrderDAO.updOrder(objOrderB) ;
    }*/
    
    /*public  HashMap updBankPayment(String strOperationNumber, int iCodeBank,String strDatePayment,
                                   long lMontoTotal,String strCodeService)     
    throws Exception {      
       return objOrderDAO.updBankPayment(strOperationNumber,iCodeBank,strDatePayment,lMontoTotal,strCodeService);               
    }  */  
    public HashMap getPaymentListBySource(String strSource,long lSourceId)      
    throws Exception, SQLException {      
       return objOrderDAO.getPaymentListBySource(strSource,lSourceId);               
    }

    public HashMap generateDocumentInvBill(long lOrderId,String strOrigen,String strLogin,int iBuilding)      
    throws Exception,SQLException {      
       return objOrderDAO.generateDocumentInvBill(lOrderId,strOrigen,strLogin,iBuilding);             
    }
    
    public HashMap generateDocumentInvBillDetail(long lOrderId,String strOrigen,String strLogin,int iBuilding)      
    throws Exception, SQLException {      
       return objOrderDAO.generateDocumentInvBillDetail(lOrderId,strOrigen,strLogin,iBuilding);             
    }

    public HashMap generatePayamentOrder(long lOrderId,String strLogin,int iBuilding)
    throws Exception,SQLException {      
       return objOrderDAO.generatePayamentOrder(lOrderId,strLogin,iBuilding);               
    }    
    
   public String updSinchronizeActiv(long lOrderId, String strLogin)     
   throws Exception,SQLException {      
    return objOrderDAO.updSinchronizeActiv(lOrderId, strLogin);               
   }   

   public String insNotificationAction(long lOrderId, String strStatus, String strLogin)     
   throws Exception, SQLException {      
    return objOrderDAO.insNotificationAction(lOrderId, strStatus, strLogin);               
   }  

   public String getShowButtom(long lSpecificationId) 
   throws Exception, SQLException {      
    return objOrderDAO.getShowButtom(lSpecificationId);               
   }

   public HashMap doExecuteActionFromOrder(long lOrderId,String strStatusOld,String strStatusNew,String strLogin,long lLoginBuilding)        
   throws Exception, SQLException {           
      return objOrderDAO.doExecuteActionFromOrder(lOrderId,strStatusOld,strStatusNew,strLogin,lLoginBuilding);
   }

   public HashMap doGenerarParteIngreso(long lOrderId,String strOrigen,String strLogin,long lLoginBuilding,String strPIType)        
   throws Exception, SQLException {      
      return objOrderDAO.doGenerarParteIngreso(lOrderId,strOrigen,strLogin,lLoginBuilding,strPIType);
   }
   
   public String getAutorizacionDevolucion(long lOrderId,int iUserId,int iAppId) 
   throws SQLException,Exception {      
    return objOrderDAO.getAuthorizationReturn(lOrderId,iUserId,iAppId);               
   }

   public float getKitEquipmentPrice(long npProduct,String npModality, long lSalesStructOrigenId)  
   throws SQLException,Exception {      
    return objProductDAO.getKitEquipmentPrice(npProduct,npModality, lSalesStructOrigenId);               
   }

   public HashMap doAutorizacionDevolucion(long lOrderId)        
   throws Exception, SQLException {      
      return objOrderDAO.doAutorizacionDevolucion(lOrderId);
   }   


   /**
   * Motivo: Convierte una trama de Inbox en un listado de Inbox
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 10/09/2007
   * @param     strNpBpelbackinboxs    Trama de Inbox
   * @return    ArrayList
   */    
   
 /*  public ArrayList getInboxList(String strNpBpelbackinboxs){      
      String strInbox="";
      ArrayList arrLista =new ArrayList();
      HashMap hshMap=new HashMap();
      StringTokenizer stkInbox = new StringTokenizer(strNpBpelbackinboxs, Constante.DIVISOR);  
      while(stkInbox.hasMoreTokens()) {            
         hshMap=new HashMap();
         strInbox =  stkInbox.nextToken().toString();                   
         hshMap.put("datoBPel",strInbox);            
         arrLista.add(hshMap);
      }   
      return arrLista;
   } 
 */  
     public ArrayList getInboxList(String strNpBpelbackinboxs, String strSpecification) throws SQLException, Exception{      

         ArrayList arrLista =new ArrayList();
         ArrayList listAux = new ArrayList();
         HashMap hshMap= null;
         String strInbox="";
         boolean flag = false;
         
         HashMap hshData;                  
         TableBean tableBean=new TableBean();         
         hshData = objGeneralDAO.getValueNpConfig(Constante.NOT_BACK_INBOX ,strSpecification);         
         ArrayList arrNpConfiguration = new ArrayList();
         arrNpConfiguration = (ArrayList)hshData.get("objArrayList");                                     
         StringTokenizer stkInbox = new StringTokenizer(strNpBpelbackinboxs, Constante.DIVISOR);  

         while(stkInbox.hasMoreTokens()) {            
            strInbox =  stkInbox.nextToken().toString();
            System.out.println("strInbox :" + strInbox);
            listAux.add(strInbox);
         }    

         String strInbNew = null;

          for(int i = 0; i<listAux.size();i++){
             String strInboxAux = (String)listAux.get(i);
             if(!arrLista.isEmpty()){
               hshMap=new HashMap();
               hshMap.put("datoBPel",strInboxAux); 
               for(int k = 0; k < arrLista.size() ; k++){
                  HashMap hasMapInb = (HashMap)arrLista.get(k);
                  String strInboxHash = (String)hasMapInb.get("datoBPel");
                  if(strInboxAux.equals(strInboxHash)){
                      System.out.println("Inbox Coincidente : "+ strInboxAux);
                      flag = true;
                      break;
                  }
               }

               if(!flag)
                 arrLista.add(hshMap);
             }else{
                  hshMap=new HashMap();
                  hshMap.put("datoBPel",strInboxAux);            
                  arrLista.add(hshMap);
             }
          }            
              
              System.out.println("Tamaño de la lista Antes : " + arrLista.size());
             
              for(int c = 0; c < arrNpConfiguration.size();c++){                                             
                tableBean = (TableBean)arrNpConfiguration.get(c);                                                                                                                
                for(int j=0;j<arrLista.size();j++){
                  HashMap hshLista = (HashMap)arrLista.get(j);
                  String strInboxHash = (String)hshLista.get("datoBPel");                   
                  if (strInboxHash.equals(tableBean.getNpValueDesc())){                  
                    arrLista.remove(j);
                    break;                
                  }
                }
              }
                          
           System.out.println("Tamaño de la lista Despues : " + arrLista.size());  
              return arrLista;
   }


   
   public  HashMap getActionList(int iSpecificationId,String strInbox,String strBpelbackinboxs,long lOrderId)
   throws Exception, SQLException {      
      ArrayList objInboxBack=new ArrayList();     
      HashMap objData =new HashMap();
		HashMap hshMap =new HashMap();
      String strMessage=null;      
      
      //objInboxBack = getInboxList(strBpelbackinboxs);         
      hshMap = getIsFirstInbox(lOrderId);
		strMessage =(String)hshMap.get("strMessage"); 
		System.out.println("[SEJBOrderEditBean][getActionList]iSpecificationId: "+iSpecificationId+"");
		System.out.println("[SEJBOrderEditBean][getActionList]strInbox: "+strInbox);
		System.out.println("[SEJBOrderEditBean][getActionList]strBpelbackinboxs: "+strBpelbackinboxs);
		System.out.println("[SEJBOrderEditBean][getActionList]lOrderId: "+lOrderId+"");
		System.out.println("[SEJBOrderEditBean][getIsFirstInbox]strMessage: "+strMessage);
 			
      if (strMessage==null){
			String sFirstInbox =(String)hshMap.get("sFirstInbox");
			System.out.println("[SEJBOrderEditBean][getNumAction]strNumAction: "+sFirstInbox);
			if(sFirstInbox.equals("1")){ //Es el primer inbox
				//if (objInboxBack!=null && objInboxBack.size()==0){
				// Entonces debemos excluir del listado las opciones: RECHAZAR y RETROCEDER, para esto enviamos como inbox: "PRIMER_INBOX"
				System.out.println("[SEJBOrderEditBean][getActionList]Primer Inbox");
				if(strInbox.equals(Constante.INBOX_ADM_VENTAS)|| strInbox.equals(Constante.INBOX_CREDITO) )
					objData=objOrderDAO.getActionList(iSpecificationId,Constante.ACTION_INBOX_PRIMER_INBOX_REC,strInbox);
				else
					objData=objOrderDAO.getActionList(iSpecificationId,Constante.ACTION_INBOX_PRIMER_INBOX,strInbox);
			}
			else{
				System.out.println("[SEJBOrderEditBean][getActionList]No es primer Inbox");
				objData=objOrderDAO.getActionList(iSpecificationId,strInbox,strInbox);      
			}      
		}
		else{
			objData.put("strMessage",(String)hshMap.get("strMessage"));
		}
      return objData;
   }
   
   public HashMap getListaAccion(ArrayList objLista,int lSpecificationId,String strBPElConversationId,String strStatus)        
   throws Exception {    
   //System.out.println("INGRESA objLista-->"+objLista+" lSpecificationId-->"+lSpecificationId+" strBPElConversationId-->"+strBPElConversationId+" ->"+strStatus);
      ArrayList objListaResult=new ArrayList();      
      HashMap hshData =new HashMap();    
      strStatus=MiUtil.getString(strStatus);
      HashMap hshRows =null;   
      String strDescription=null;
     
      if (strBPElConversationId==null){  // Si aun no se creo el id del BPEL entonces solo deberia aparecer la opcion de AVANZAR
         hshData.put("wv_npValue",Constante.ACTION_INBOX_AVANZAR.toUpperCase());
         hshData.put("wv_npValueDesc",MiUtil.initCap(Constante.ACTION_INBOX_AVANZAR));
		   objListaResult.add(hshData); 
         //INICIO CEM - COR0379
         hshData=new HashMap();		 
         hshData.put("wv_npValue",Constante.ACTION_INBOX_BAGLOCK.toUpperCase());		 
         hshData.put("wv_npValueDesc",MiUtil.initCap(Constante.ACTION_INBOX_RECHAZAR));
         objListaResult.add(hshData);		 
         hshData=new HashMap();
         hshData.put("wv_npValue",Constante.ACTION_INBOX_ANULAR.toUpperCase());
         hshData.put("wv_npValueDesc",MiUtil.initCap(Constante.ACTION_INBOX_ANULAR));		 
         objListaResult.add(hshData);
         //FIN CEM - COR0379  		 
      }else{
         if (objLista!=null){          
            for(int k=0;k<objLista.size();k++){
               hshData=new HashMap();
               hshRows=(HashMap)objLista.get(k);
               strDescription=MiUtil.getString((String)hshRows.get("wv_npValueDesc"));
               if (  Constante.INBOX_BAGLOCK.equals(strStatus.toUpperCase()) &&
                    (Constante.ACTION_INBOX_BACK.equals((strDescription.toUpperCase())) || 
                     Constante.ACTION_INBOX_RECHAZAR.equals((strDescription.toUpperCase()))
                    ) 
                  )
               {
                 // Si el inbox es BAGLOCK y la acccion es INBOX_BACK e INBOX_RECHAZAR entonces no incluir esas acciones.
               }else if (lSpecificationId==Constante.SPEC_CAMBIAR_DATOS_CLIENTE && 
                      Constante.ACTION_INBOX_AVANZAR.equals((strDescription.toUpperCase())))
               {
                     hshData.put("wv_npValue",Constante.ACTION_INBOX_SALTAR.toUpperCase());
                     hshData.put("wv_npValueDesc",MiUtil.initCap(Constante.ACTION_INBOX_IR_A));             
                     objListaResult.add(hshData);
               }else if (lSpecificationId==Constante.SPEC_CAMBIAR_PLAN_TARIFARIO &&
                         Constante.ACTION_INBOX_AVANZAR.equals((strDescription.toUpperCase())) &&
                         (Constante.INBOX_ACTIVACION_PROGRAMACION.equals((strStatus.toUpperCase())) || 
                          Constante.INBOX_BACKOFFICE.equals((strStatus.toUpperCase())) || 
                          Constante.INBOX_CALLCENTER.equals((strStatus.toUpperCase())) 
                         )
                        )
               {
                     hshData.put("wv_npValue",Constante.ACTION_INBOX_SALTAR.toUpperCase());
                     hshData.put("wv_npValueDesc",MiUtil.initCap(Constante.ACTION_INBOX_IR_A));              
                     objListaResult.add(hshData);
               }else if (lSpecificationId==Constante.SPEC_ACTIVAR_DESACTIVAR_SERVICIOS && 
                         Constante.ACTION_INBOX_AVANZAR.equals((strDescription.toUpperCase())) &&
                         (Constante.INBOX_ACTIVACION_PROGRAMACION.equals((strStatus.toUpperCase())) ||                           
                          Constante.INBOX_CALLCENTER.equals((strStatus.toUpperCase())) 
                         )
                        )
               {
                     hshData.put("wv_npValue",Constante.ACTION_INBOX_SALTAR.toUpperCase());
                     hshData.put("wv_npValueDesc",MiUtil.initCap(Constante.ACTION_INBOX_IR_A));                             
                     objListaResult.add(hshData);
               }else{
                     hshData.put("wv_npValue",hshRows.get("wv_npValue"));
                     hshData.put("wv_npValueDesc",hshRows.get("wv_npValueDesc"));
                     objListaResult.add(hshData);
               }                  
            }                                
         }         
      }
      
      hshData=new HashMap();
      hshData.put("objListado",objListaResult);
      return hshData;
   }


   public HashMap getSpecificationStatus(int iSpecificationId,String strCurrentInbox)       
   throws Exception {      
      return objOrderDAO.getSpecificationStatus(iSpecificationId,strCurrentInbox);
   }   

   public int getFlagModifiySalesName(int iSpecificationId,int iVendedorId,int iUserId,int iAppId)   
   throws Exception, SQLException{        
      return objOrderDAO.getFlagModifiySalesName(iSpecificationId,iVendedorId,iUserId,iAppId);
   }   
   
   public  HashMap getCarrierList(String strParamName, String strParamStatus) 
   throws Exception,SQLException {      
      return objOrderDAO.getCarrierList(strParamName,strParamStatus);
   }   
   
   public  HashMap updTimeStamp(long lOrderId)
   throws Exception,SQLException {      
      return objOrderDAO.updTimeStamp(lOrderId);
   }   

   public HashMap getPayFormList(int iSpecificationId,long lCustomerId)
   throws Exception,SQLException {      
      return objOrderDAO.getPayFormList(iSpecificationId,lCustomerId);
   }
   
   /*JPEREZ: Excepciones - Inicio*/
   public String doDeleteOrderPeriod(RequestHashMap request, Connection conn) throws SQLException,Exception{      
      String strOrderId      = "";
      String strMessage      = null;
      strOrderId       =   (String)request.get("hdnNumeroOrder");
      strMessage = objExceptionDAO.deleteOrderPeriod(Long.parseLong(strOrderId), conn);
      return strMessage;   
   }
   public String doUpdateOrderPeriod(RequestHashMap request, Connection conn) throws SQLException,Exception{      
      String strBeginPeriods = "", 
             strEndPeriods   = "",
             strOrderId      = "",
             strCreatedBy    = "",
             strMessage      = null;
         
      System.out.println("===========doUpdateOrderPeriod =============");  
      strBeginPeriods  =   (String)request.get("hdnPeriodoIni");
      strEndPeriods    =   (String)request.get("hdnPeriodoFin");
      strOrderId       =   (String)request.get("hdnOrderId");
      strCreatedBy     =   (String)request.get("hdnSessionLogin");      
      if (strBeginPeriods!=null){
         StringTokenizer tkBeginPeriod   = new StringTokenizer(strBeginPeriods,"|");
         StringTokenizer tkEndPeriod      = new StringTokenizer(strEndPeriods,"|");
        
         while (tkBeginPeriod.hasMoreTokens()) {
            String strBeginPeriod = tkBeginPeriod.nextToken();
            String strEndPeriod   = tkEndPeriod.nextToken();                 
            strMessage = objExceptionDAO.insertOrderPeriod(Long.parseLong(strOrderId), strBeginPeriod, strEndPeriod, strCreatedBy, conn );            
            if (strMessage!=null){
              System.out.println("===========strMessage "+strMessage);    
              return strMessage;                
            }
               
         }         
      }             
      return strMessage;                
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

     public HashMap OrderPassForInventory(long lOrderId) throws Exception, SQLException{
       return objOrderDAO.OrderPassForInventory(lOrderId);
     }	 

   public HashMap getOrderDetailFlow(long lOrderId,String strLogin)        
   throws Exception, SQLException {      
      return objOrderDAO.getOrderDetailFlow(lOrderId,strLogin);
   }

	public HashMap getIsFirstInbox(long lOrderId) throws Exception, SQLException{  
		return objOrderDAO.getIsFirstInbox(lOrderId);
	}
  
  public HashMap getImeiSimMatch(long lSpecificationId ,String strImei, String strSim)throws SQLException, Exception{
    return objItemDAO.getImeiSimMatch(lSpecificationId,strImei,strSim);
  }

  public HashMap getActionItem (long lOrderId,String sOperacionItem, String estadoPagoActual)  throws SQLException, Exception{
    return objItemDAO.getActionItem(lOrderId,sOperacionItem,estadoPagoActual);
  }  
  
  /***********************************************************************
   ***********************************************************************
   ***********************************************************************
   *  CARGA MASIVA IMEI/SIM - INICIO
   *  REALIZADO POR: Richard De los Reyes
   *  FECHA: 31/10/2008
   ***********************************************************************
   ***********************************************************************
   ***********************************************************************/
   
  public HashMap validateMassiveImeiSim (LoadMassiveItemBean loadMassiveItemBean) throws SQLException, Exception{
    return objOrderDAO.validateMassiveImeiSim(loadMassiveItemBean);
  }
  

   public HashMap doSetOrderPayPend(String av_constOrder, long lOrderId)  throws SQLException,Exception {      
       return objOrderDAO.doSetOrderPayPend(av_constOrder,lOrderId);
   }          
   
   public String doSetOrderPayCancel(String av_constOrder, long lOrderId)        
   throws SQLException,Exception {      
      
      int lPaymentCount=0;
      String strMessage;
      String strCount;
      HashMap  hshData=new HashMap();
      hshData=objOrderDAO.doSetOrderPayCancel(av_constOrder,lOrderId);
      strMessage =(String)hshData.get("strMessage");
      strCount =(String)hshData.get("strCount");
      if (strMessage!=null)
          throw new Exception(strMessage);
      
      return strCount;
   }   

   public HashMap getGuarantee(long lSourceid, String strSource, long lConceptid) throws SQLException, Exception {
      return objOrderDAO.getGuarantee(lSourceid, strSource, lConceptid);
   }   
   
  /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:rensso.martinez@nextel.com.pe">Rensso Martinezr</a>
 * <br>Motivo: Valida que los contratos de una orden no esten suspendidos mas de 60 días.
 * <br>Fecha: 24/07/2009
 * @see pe.com.nextel.dao.OrderDAO#validaDiasSuspension(String, String, String)      
 ************************************************************************************************************************************/      
  public HashMap OrderDAOvalidaDiasSuspensionEdit(int iNpOrderId, String strNpScheduleDate, String strNpScheduleDate2) throws Exception, SQLException{
    return objOrderDAO.validaDiasSuspensionEdit(iNpOrderId,strNpScheduleDate,strNpScheduleDate2);
  }
/***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:cesar.barzola@nextel.com.pe">Cesar Barzola</a>
 * <br>Fecha: 06/08/2009
 * @see pe.com.nextel.dao.ProposedDAO#getValidationProposed(String,String)      
/************************************************************************************************************************************/
public HashMap getValidationProposed(long lOrderId,long lProposedId,long lCustomerId,long lSpecification,long lSellerId,String strTrama)throws  SQLException,Exception
{
 return objProposedDAO.getValidationProposed(lOrderId,lProposedId,lCustomerId,lSpecification,lSellerId, strTrama);
}

/***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:julio.herrera@nextel.com.pe">Julio Herrera</a>
 * <br>Motivo: Devuelve la cantidad de días calendario entre una fecha dada y el parametro
 * <br>Fecha: 03/11/2009      
/ ************************************************************************************************************************************/

public int getAmountCalendarDays(String npCreateDate, int plazo) throws SQLException,Exception
{
  return  objOrderDAO.getAmountCalendarDays(npCreateDate,plazo);
}

/***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:julio.herrera@nextel.com.pe">Julio Herrera</a>
 * <br>Motivo: Devuelve la fecha final de un intervalo de periodo de 5 dias
 * <br>Fecha: 03/11/2009      
/ ************************************************************************************************************************************/

public String getFechaFinalIntervalo(String npCreateDate, int plazo) throws SQLException,Exception
{
  return  objOrderDAO.getFechaFinalIntervalo(npCreateDate,plazo);
}


  public String agreementValidations(RequestHashMap request, Connection conn)throws Exception, SQLException{

      String[]  price_type    = request.getParameterValues("txtItemPriceType");
      //String[]  price_type_id = request.getParameterValues("txtItemPriceTypeId");
      String[]  price_type_id = request.getParameterValues("txtItemPriceTypeItemId");
      String[]  quantity      = request.getParameterValues("txtItemQuantity");      
      
      long lCustomerId = (request.getParameter("txtCompanyId")==null?0:MiUtil.parseLong(request.getParameter("txtCompanyId")));
      long lSiteId = (request.getParameter("cmbResponsablePago")==null?0:MiUtil.parseLong(request.getParameter("cmbResponsablePago")));
      long lOrderId =(request.getParameter("hdnOrderId")==null?0:MiUtil.parseLong(request.getParameter("hdnOrderId"))); 
      
      ArrayList arrListItem   = new ArrayList();
      HashMap hshResultValidation = new HashMap();
      OrderBean objOrder = new OrderBean();
      
      int count = 0;
      String aux_type;
      String aux_typeid;
      String strMessage =  null;
      
      if (price_type_id != null && price_type != null){
        //Ordenamos los arreglos
        if(price_type_id.length>0){
          for(int i=0; i<price_type_id.length-1; i++){
            for (int j=i; j<price_type_id.length; j++){
              if(MiUtil.parseInt(price_type_id[i])>MiUtil.parseInt(price_type_id[j])){
                aux_type   = price_type[i];
                aux_typeid = price_type_id[i];
              
                price_type[i]    = price_type[j];
                price_type_id[i] = price_type_id[j];
                
                price_type[j]    = aux_type;
                price_type_id[j] = aux_typeid;
              }
            } 
          }
        }
        
        String typeId = "";
        String flagFrstTime = "";
      
        //Contamos la cantidad de items por cada tipo de AC 
        if(price_type.length>0){
          for(int i=0; i<price_type.length; i++){
            if("AC".equals(price_type[i])){
              if(typeId.equals(price_type_id[i])){
                  count = count + MiUtil.parseInt(quantity[i]); 
              }else{
                if ("1".equals(flagFrstTime)){
                  HashMap objHashMap = new HashMap();
                  objHashMap.put("item_type_count",MiUtil.getString(count));
                  objHashMap.put("item_type_id",typeId);  
                  arrListItem.add(objHashMap);
                }else{
                  flagFrstTime = "1";
                }
                typeId = price_type_id[i];  
                count = MiUtil.parseInt(quantity[i]); 
              }
            }
          }
          if ("1".equals(flagFrstTime)){
            HashMap objHashMap = new HashMap();
            objHashMap.put("item_type_count",MiUtil.getString(count));
            objHashMap.put("item_type_id",typeId); 
            arrListItem.add(objHashMap);
          }
        }
      
        objOrder.setNpCustomerId(lCustomerId);
        objOrder.setNpSiteId(lSiteId);
        objOrder.setNpOrderId(lOrderId);
        hshResultValidation = objAgreementDAO.validateAgreement(arrListItem,objOrder,conn);
        strMessage = (String)hshResultValidation.get("strMessage");
      }
      return strMessage;
  }

  public HashMap ItemDAOdoValidateIMEICustomer(String strIMEI) throws Exception, SQLException
  {
    return objItemDAO.doValidateIMEICustomer(strIMEI);
  }

  /*################AÑADIDO EXTERNO.MVALLE 17/11/2010################*/
  
  public HashMap OrderDAOdoGenerateGuiaRemision(long lngOrderId, String strLogin) throws Exception, SQLException
  {
    return objOrderDAO.doGenerateGuiaRemision(lngOrderId, strLogin);
  }
  
 /*#####################AÑADIDO EXTERNO.JNINO 24/11/2010################*/
  public HashMap doGenerarSuspenderEquipos(long lOrderId)        
  throws Exception, SQLException {      
      return objOrderDAO.doGenerarSuspenderEquipos(lOrderId);
  }
 /*#################FIN AÑADIDO EXTERNO.JNINO 24/11/2010################*/   
 
  /*####################AÑADIDO EXTERNO.JNINO 29/11/2010################*/  
  public HashMap doGetEquipmentStatus(long lOrderId,String strUserId)        
  throws Exception, SQLException {      
      return objOrderDAO.doGetEquipmentStatus(lOrderId,strUserId);
  }  
 /*#################FIN AÑADIDO EXTERNO.JNINO 29/11/2010################*/ 
 
  public String doValidateBudget(OrderBean orderBean, PortalSessionBean portalBean, ArrayList itemOrderList, HashMap objItemDeviceMap) throws Exception, SQLException{
      return objOrderDAO.doValidateBudget(orderBean,portalBean,itemOrderList,objItemDeviceMap);
  }
  
  public Map budgetsAvailableChannels(String channelType, BudgetBean budgetBean) throws Exception, SQLException{ 
      Map budgetsMap = new HashMap();
      List budgetsList = null;
      if("Comercial".equals(channelType)){
          budgetsList = objOrderDAO.budgetsCommercialChannels(budgetBean);
          budgetsMap.put("budgetsCommercialList",budgetsList);
      }else{
          budgetsList = objOrderDAO.budgetsReserveChannels(budgetBean);
          budgetsMap.put("budgetsReserveList",budgetsList);
      }
       return budgetsMap;
  }
  
  public Map doGetBudgetReasons()  throws Exception, SQLException {
      return objOrderDAO.doGetBudgetReasons();
  }
  
  public Map doGetLastReasonDescription(BudgetBean budgetBean) throws Exception,SQLException {
      return objOrderDAO.doGetLastReasonDescription(budgetBean);
  } 

  /*************************************************************************************************************
   * <br>Realizado por: <a href="mailto:cesar.lozza@hp.com">César Lozza</a>
   * <br>Motivo: Método que obtiene la cantidad de items a los cuales se aplicó promoción por volumen de orden
   * <br>Fecha: 20/12/2010
  / ************************************************************************************************************/
  public int getOrderVolumeCount(int orderId) throws Exception, SQLException
  {
    return  objOrderDAO.getOrderVolumeCount(orderId);
  }
  
  /***********************************************************************************************************************************
   * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
   * <br>Fecha: 15/12/2010  
 ************************************************************************************************************************************/ 
  public HashMap reserveGoldenNumber(RequestHashMap objHashMap, Connection conn, int intSpecificationId) throws  SQLException, Exception {
    HashMap hshResultReserve= new HashMap();
    HashMap objResource = null;
    String[]  pv_strUfmi = null;
    String[]  pv_strUfmiId = null;
    ArrayList list = new ArrayList();
    if (intSpecificationId==Constante.SPEC_POSTPAGO_PORTA) {
       pv_strUfmi = objHashMap.getParameterValues("hdnUfmi");
       pv_strUfmiId = objHashMap.getParameterValues("hdnUfmiId");
          for( int i = 0; i < pv_strUfmiId.length; i++ ){
             if (!"".equals(pv_strUfmiId[i].trim()) && !"undefined".equals(pv_strUfmiId[i].trim())) {          
               objResource = new HashMap();
               objResource.put("wv_ufmi_dn_num",MiUtil.getStringObject(pv_strUfmi, i));
               System.out.println("El hdnUfmi en EditOrder es-------------------------------------------------------------------->" + pv_strUfmi[i]);
               objResource.put("wn_ufmi_dn_id",MiUtil.getStringObject(pv_strUfmiId, i));
               System.out.println("El hdnUfmiId en EditOrder es-------------------------------------------------------------------->" + pv_strUfmiId[i]);  
               objResource.put("wv_status","f");
               list.add(objResource);
             }             
          }
    }  else if (intSpecificationId==Constante.SPEC_CAMBIO_NUMERO) {
       String[] pv_item_Ptn_Id = objHashMap.getParameterValues("item_ptnId");
       String[]  pv_item_New_Number = objHashMap.getParameterValues("txtItemNewNumber");
        if (pv_item_Ptn_Id.length>0) {
          for( int i = 0; i < pv_item_Ptn_Id.length; i++ ){
             System.out.println("wn_ptn_dn_id --------->" + MiUtil.getStringObject(pv_item_Ptn_Id, i));
             if (!"".equals(pv_item_Ptn_Id[i].trim()) && !"undefined".equals(pv_item_Ptn_Id[i].trim())) {   
               objResource = new HashMap();
               objResource.put("wn_ptn_dn_id",MiUtil.getStringObject(pv_item_Ptn_Id, i));
               System.out.println("wn_ptn_dn_id --------->" + MiUtil.getStringObject(pv_item_Ptn_Id, i));
               objResource.put("wv_ptn_dn_num",MiUtil.getStringObject(pv_item_New_Number, i));
               System.out.println("wv_ptn_dn_num --------->" + MiUtil.getStringObject(pv_item_New_Number, i));
               objResource.put("wv_status","f");
               list.add(objResource);
            }
          } 
       }
    }
    if (list.size()>0) {
      hshResultReserve=objItemDAO.reserveGoldenNumber(list,intSpecificationId);
      System.out.println("el strMessage en EditreserveGoldenNumber es ---->" + hshResultReserve.get("strMessage"));    
      if (hshResultReserve.get("strMessage")!=null){
          if (hshResultReserve.get("strMessageError")!=null){
            String message="No se pudo grabar la Orden.\\nLos siguientes numeros no se encuentran disponibles:\\n";
            System.out.println("No se puede Grabar xq hay numeros q ya no estan disponibles ----->");
            hshResultReserve.put("strMessage",message.concat(hshResultReserve.get("strMessageError").toString()));
          }
      }
    }
    return hshResultReserve;
  }
  
  public int getEnabledCourier(long npOrderId) throws Exception, SQLException {
      return objOrderDAO.getEnabledCourier(npOrderId);
  } 

    /*************************************************************************************************************
     * <br>Realizado por: <a href="mailto:miguel.montoya@hp.com">Miguel Ángel Montoya</a>
     * <br>Motivo:  Retorna true si la orden requiere verificación de documentos.
     * <br>Fecha: 07/11/2013
    / ************************************************************************************************************/
    public boolean requiresDocumentVerification(long npOrderId) throws Exception, SQLException {
        return objOrderDAO.requiresDocumentVerification(npOrderId);
    }    
 
    /**EFLORES
     * <br>Realizado por: <a href="mailto:eddy.flores@hpe.com">Eddy Flores</a>
     * <br>Fecha: 23/09/2015
     * @return Integer valor 0 : El numero no se encuentra en la lista, 1 : El numero se encuentra en la lista.
     * @retun String mensaje de log para error
     */
    public String doValidateUltimaPreevaluacion(String customerid,String categoryId,String cadenaNumeros,String cadenaModalidad, String userLogin) throws Exception,SQLException {
        return objOrderDAO.doValidateUltimaPreevaluacion(customerid,categoryId,cadenaNumeros,cadenaModalidad,userLogin); //JBALCAZAR PRY-1055 
    }
    
    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:jorge.curi@tcs.com">JCURI</a>
     * <br>Fecha: 19/07/2017
     ************************************************************************************************************************************/
    public String updStatusOrderProrra(String hdnSpecification, long orderId, String userCreate, Connection conn) throws SQLException, Exception{
    	System.out.println("SEJBOrderEditBean/updOrder/updStatusOrderProrra [data] -> [orderId]" + orderId + " [hdnSpecification] " + hdnSpecification);
    	String strMessage = null;
    	HashMap hashMapDAO = null;
	    Integer specification = MiUtil.parseInt(hdnSpecification);
	    
	    strMessage  = objOrderDAO.updateCreateOrderProrratero(0L, orderId, "", 0L, new BigDecimal(0), userCreate, Constante.ORDER_STATUS_ANULADO, 2, conn);
		if(strMessage != null ) {
			throw new Exception(strMessage);
		}
    	return "S";
    } 
    
    private HashMap createUpdOrdenProrrateo(RequestHashMap objHashMap,OrderBean orderCreadaBean, int specificationId, String apportionment, String tipoDocumento,String numeroDocumento, long customerId,  String user, String actionName, String strCurrentInbox, Connection conn) throws Exception {
    	System.out.println("[INI] SEJBOrderEditBean/updOrder/createUpdOrdenProrrateo/createUpdOrdenProrrateo");
    	HashMap objResultado=new HashMap();    	
    	String strMessage = null;
		OrderBean orderSearchBean = null;
		boolean conftipodoc = false ; //JBALCAZAR PRY-1002
		long orderParentId= orderCreadaBean.getNpOrderId();
		String strChangedOrder  = MiUtil.getString(objHashMap.getParameter("hdnChangedOrder"));
		String pv_items_borrados = objHashMap.getParameter("hdnItemBorrados");
	     
	    System.out.println("SEJBOrderEditBean/createUpdOrdenProrrateo [orderParentId]" + orderParentId + " [strChangedOrder] " + strChangedOrder + " [pv_items_borrados] " + pv_items_borrados + " [strCurrentInbox] " + strCurrentInbox);				
			
	    //validacion de inbox diferente a TIENDA01 cuando no es PA.
	    if (strCurrentInbox != null && !"TIENDA01".equals(strCurrentInbox.trim().toUpperCase())){	    	
	    	objResultado.put("invokeSectionDynamics","S");	    	
	  		return objResultado;
	  	}
	    
	    //seccion dinamica
	    System.out.println("SEJBOrderEditBean/createUpdOrdenProrrateo/ INICIO SECCION DINAMICA");
	  	strMessage = insertSectionDynamicsProrrateo(specificationId, objHashMap, conn);
	  	if( strMessage!=null){
	  		throw new Exception(strMessage);
	    }
	  	System.out.println("SEJBOrderEditBean/createUpdOrdenProrrateo/ FIN SECCION DINAMICA");
	  	
	  	if("N".equals(apportionment)) {
		  	//SETEAR VALOR 0 A ITEMS
			System.out.println("SEJBOrderEditBean/updOrder/updateCreateOrderProrratero-setitem/orderParentId -> "+orderParentId +" [CreatedBy] " + user + "indicador 3");	
		    strMessage  = objOrderDAO.updateCreateOrderProrratero(orderParentId, 0L, "N", 0L, new BigDecimal(0), user, Constante.ORDER_STATUS_ANULADO, 3, conn);
			if(strMessage != null ){
				System.out.println("[ERROR-updateCreateOrderProrratero-setitem] " + strMessage);
				throw new UserException(strMessage);
			}
			
			//JCURI : PRY-1002 - INIT   
			HashMap hshResultvalidar = objOrderDAO.validarExisteNoProrrateo(orderParentId);
			strMessage = (String) hshResultvalidar.get("strMessage");
			logger.info("SEJBOrderEditBean/validarExisteNoProrrateo [strMessage]" + strMessage);
    		if(strMessage != null ) {
    			throw new Exception(strMessage);
    		}
    		
    		int status = (Integer) hshResultvalidar.get("statusExists");
    		logger.info("SEJBOrderEditBean/validarExisteNoProrrateo [status]" + status);
    		if(status == 0) {
    			invocarCalculoProrrateo(objHashMap, orderCreadaBean, conn);
    		}
     		//JCURI : PRY-1002 - END  
						
		 }
	  	
	  	if( pv_items_borrados!= null){
	  		StringTokenizer strTokenizer =  new StringTokenizer(pv_items_borrados,"|");
	        Vector vctDeleted = new Vector();
	                   
	        while(strTokenizer.hasMoreElements()){
	              String str = (String)strTokenizer.nextElement();
	              vctDeleted.addElement(""+str);
	        }
	        if(vctDeleted != null && vctDeleted.size() > 0) {
	        	strChangedOrder = "S";
	        }	  		
	  	}
	  	System.out.println("SEJBOrderEditBean/createUpdOrdenProrrateo [orderParentId]" + orderParentId + " [strChangedOrderModi] " + strChangedOrder);
	  	
	  	if(!"S".equals(strChangedOrder)) {
	  		return objResultado;
	  	}
	  	System.out.println("SEJBOrderEditBean/createUpdOrdenProrrateo/ INICIO CREACION PAGO ANTICIPADO");
		
		// crear items orden padre
		List<ItemBean> listItemSave = new ArrayList<ItemBean>();
		strMessage = saveUpdateItems(objHashMap, conn, orderCreadaBean, listItemSave);
		if(strMessage != null ){
			throw new Exception(strMessage);
		}		
		if(listItemSave == null || listItemSave.size() == 0) {
			throw new Exception("Error al crear item(s)");
		}		
		System.out.println("SEJBOrderEditBean/updOrder/createUpdOrdenProrrateo/listItemSave->" + listItemSave.size());
		
		//Eliminar orden de prorrateo(ANULADO)
		System.out.println("SEJBOrderEditBean/updOrder/updateCreateOrderProrratero/orderParentId -> "+orderParentId +" [CreatedBy] " + user);	
    	strMessage  = objOrderDAO.updateCreateOrderProrratero(orderParentId, 0L, "N", 0L, new BigDecimal(0), user, Constante.ORDER_STATUS_ANULADO, 3, conn);
    	if(strMessage != null ){
    		System.out.println("[ERROR-updateCreateOrderProrratero ]" + strMessage);
			throw new UserException(strMessage);
		}
		
    	orderSearchBean = orderCreadaBean;
	        
	    System.out.println("SEJBOrderEditBean/updOrder/createUpdOrdenProrrateo");
	    System.out.println("[tipoDocumento] " + tipoDocumento + "[numeroDocumento] " + numeroDocumento + " [apportionment] " +apportionment + " [specificationId] " + specificationId + " [customerId ] " + customerId + " [user] "  + user);
	    
	     //JBALCAZAR PRY-1002
	   // SEJBOrderNewBean sjeborder = new SEJBOrderNewBean();
	    
	    conftipodoc = this.ConfiguracionTipoDocumentoProrrateo(tipoDocumento, numeroDocumento);
	    System.out.println("SEJBOrderEditBean/updOrder/conftipodoc2 :"+conftipodoc);
	    
	    if(conftipodoc &&  "S".equals(apportionment)) {
			
			HashMap hashMapDAO=new HashMap();
			hashMapDAO = objGeneralDAO.getTableList("ORDEN_PRORRATEO","1");
	    	strMessage = (String) hashMapDAO.get("strMessage");
		    if(strMessage != null) {
		    	throw new Exception(strMessage);
		    }	    
		    
		    List arrTableList = (List) hashMapDAO.get("arrTableList");
		    String flagActivo = MiUtil.getNpTable(arrTableList , "FLAG_ACTIVO").get("wv_npValueDesc");
		    
	    	if("1".equalsIgnoreCase(flagActivo)) {					
				// crear indicencia orden hijo
	    		hashMapDAO = objOrderDAO.createIncidentPRORRA(customerId, user, conn);
	    		strMessage = (String) hashMapDAO.get("strMessage");
	    		if(strMessage != null ){
	    			throw new Exception(strMessage);
	    		}	    		
	    		
	    		Hashtable hshResult = objOrderDAO.getOrderIdNew();
	    		
	    		System.out.println("SEJBOrderEditBean/updOrder/createUpdOrdenProrrateo/OrderDAOgetOrderIdNew->"+hshResult.get("wn_orderid"));
	    		System.out.println("SEJBOrderEditBean/updOrder/createUpdOrdenProrrateo/wv_message->"+hshResult.get("wv_message"));
	    		if(hshResult.get("wv_message")!=null && !"null".equals(hshResult.get("wv_message"))) {
	    			objResultado.put("strMessage", hshResult.get("wv_message"));
	    			throw new Exception(hshResult.get("wv_message").toString());
	    		}
	    		
				System.out.println("SEJBOrderEditBean/updOrder/createUpdOrdenProrrateo->set data PA");
	    		OrderBean orderBean = getDataOrderForProrrateo(orderSearchBean);
	    		
	    		orderBean.setNpGeneratorType("INC");
    			orderBean.setNpGeneratorId(Long.parseLong(String.valueOf(hashMapDAO.get("npincidentid"))));
    			orderBean.setNpOrderId(Long.parseLong(String.valueOf(hshResult.get("wn_orderid"))));
    			orderBean.setNpOrderCode("P"+orderBean.getNpOrderId());
			    orderBean.setNpSpecification( MiUtil.getNpTable(arrTableList , "NPSPECIFICATION").get("wv_npValueDesc") );
			    orderBean.setNpSpecificationId( Integer.parseInt(MiUtil.getNpTable(arrTableList , "NPSPECIFICATIONID").get("wv_npValueDesc")) );
			    orderBean.setNpType(MiUtil.getNpTable(arrTableList , "NPTYPE").get("wv_npValueDesc") );
			    orderBean.setNpPaymentTerms(Constante.PAYFORM_CONTADO);
			    orderBean.setNpPaymentStatus(Constante.ESTADO_DE_PAGO_ORDEN_PENDIENTE);
			    orderBean.setNpSignDate(null);
			    
			    System.out.println("SEJBOrderEditBean/updOrder/createUpdOrdenProrrateo/getOrderInsertar->antes de dao");
			    System.out.println("[SEJBOrderEditBean][orderBean] : " + orderBean.toString());
			    
			    HashMap hshResultSaveOrder = objOrderDAO.getOrderInsertar(orderBean,conn);
		        System.out.println("SEJBOrderEditBean/updOrder/createUpdOrdenProrrateo->"+(String)hshResultSaveOrder.get("strMessage"));
		        strMessage = (String) hshResultSaveOrder.get("strMessage");
		        if( strMessage != null) {
		        	throw new Exception(strMessage);
		        }
		        	
		        System.out.println("SEJBOrderEditBean/updOrder/createUpdOrdenProrrateo/crea items->setea bean");
		        ItemBean  itemBean    =   new ItemBean();
		        
		        itemBean.setNporderid(orderBean.getNpOrderId());
		        itemBean.setNpproductlineid(Long.parseLong(MiUtil.getNpTable(arrTableList , "NPPRODUCTLINEID").get("wv_npValueDesc")));
		        itemBean.setNpproductid(Long.parseLong(MiUtil.getNpTable(arrTableList , "NPPRODUCTID").get("wv_npValueDesc")));
		        itemBean.setNpmodalitysell(MiUtil.getNpTable(arrTableList , "NPMODALITYSELL").get("wv_npValueDesc"));
		        itemBean.setNpquantity(Integer.parseInt(MiUtil.getNpTable(arrTableList , "NPQUANTITY").get("wv_npValueDesc")));
		        
		        itemBean.setNpcurrency(MiUtil.getNpTable(arrTableList , "NPCURRENCY").get("wv_npValueDesc"));
		        itemBean.setNpkit(MiUtil.getNpTable(arrTableList , "NPKIT").get("wv_npValueDesc"));					        
		        itemBean.setNppricetype(MiUtil.getNpTable(arrTableList , "NPPRICETYPE").get("NPVALUEDESC"));
		        itemBean.setNppricetypeid(Long.parseLong(MiUtil.getNpTable(arrTableList , "NPPRICETYPEID").get("wv_npValueDesc")));
		        itemBean.setNpsolutionid(Long.parseLong(MiUtil.getNpTable(arrTableList , "NPSOLUTIONID").get("wv_npValueDesc")));
		        itemBean.setNpmodificationdate(MiUtil.getDateBD("dd/MM/yyyy"));
		        itemBean.setNpmodificationby(orderBean.getNpCreatedBy() );
		        itemBean.setNpcreateddate(MiUtil.getDateBD("dd/MM/yyyy"));
		        itemBean.setNpcreatedby(orderBean.getNpCreatedBy());
		        
		        itemBean.setNppriceexception("");
		        itemBean.setNprent("");
		        itemBean.setNpdiscount("");
		        itemBean.setNpinstalationprice("");
		        itemBean.setNpinstalationexception("");
		        itemBean.setNpaditionalcost("");
		        itemBean.setNptotalRentaAdelantada("");
		        
				BigDecimal montoTotalProrrateo = MiUtil.getBigDecimal(objHashMap.getParameter("txtTotalPriceApportionment"));	
				System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenProrrateo/items txtTotalPriceApportionment ->"+montoTotalProrrateo);				
				itemBean.setNpprice(String.valueOf(montoTotalProrrateo));
		        
		        String resultTransaction = objItemDAO.doSaveItem(itemBean,conn); 
		        System.out.println("SEJBOrderEditBean/updOrder/createUpdOrdenProrrateo/items->"+resultTransaction);	        
		        
		        objResultado.put("strMessage", resultTransaction);					        
		        objResultado.put("npOrderIdSon", orderBean.getNpOrderId());
		        objResultado.put("objItemProrrateo", itemBean);
		        objResultado.put("objOrderProrrateo", orderBean);
		        
		        strMessage = (String)objResultado.get("strMessage");					        
		        if(strMessage != null){
		        	throw new Exception(strMessage);
		        }
		        
		        //BPEL_PRORRA
		        HashMap hshResultSpecification = objCategoryDAO.getSpecificationDetail(orderBean.getNpSpecificationId(),conn);					        
			    strMessage=(String)hshResultSpecification.get("strMessage");
			    if(strMessage != null){
		        	throw new Exception(strMessage);
		        }
			    SpecificationBean objSpecificationProrrateo=(SpecificationBean)hshResultSpecification.get("objSpecifBean");
		    	objResultado.put("objSpecificationProrrateo",objSpecificationProrrateo); 
		        
		        // actualiza orden prorrateo
	        	long orderChildId= orderBean.getNpOrderId();
	        	long itemId= itemBean.getNpitemid();
	        	String prorrateo = "S";
	        	
	        	System.out.println("SEJBOrderEditBean/updOrder/createUpdOrdenProrrateo [orderParentId] "+orderParentId + " [orderChildId] " + orderChildId + " [itemId] " + itemId);
	        	strMessage  = objOrderDAO.updateCreateOrderProrratero(orderParentId, orderChildId, prorrateo, itemId, montoTotalProrrateo, orderBean.getNpCreatedBy(),"",1, conn);
	    		if(strMessage != null ){
	    			throw new Exception(strMessage);
	    		}
	    		
	    		//DATOS DEL PRORRATEO
	    		SectionItemEvents sectionItemEvent = new SectionItemEvents(); 
	    		List<ApportionmentBean> list = sectionItemEvent.getDataItemApportionment(objHashMap, listItemSave);
	    		if(list!=null && list.size() > 0) {
	    			System.out.println("SEJBOrderEditBean/updOrder/saveItemApi [INI]");
	    			for(ApportionmentBean bean : list) {
	    				apportionmentDAO.saveItemApi(bean, orderBean.getNpCreatedBy(), conn);
	    				System.out.println("SEJBOrderEditBean/createUpdOrdenProrrateo [saveItemApi-strMessage] " + strMessage);
    					if(strMessage != null ) {
    		    			throw new Exception(strMessage);
    		    		}
	    			}	    			
	    		} else {
	    			throw new Exception("ERROR : No se obtuvo valor del servicio web");
	    		}
	    		objResultado.put("successOrderProrrateo", "S");
	    		
	    	} else {
    			throw new Exception("La opcion de pago anticipado fue desactivado");
    		}//end flagActivo	    	
	    	
		 }
		System.out.println("[END] SEJBOrderEditBean/updOrder/createUpdOrdenProrrateo/createUpdOrdenProrrateo");
	    return objResultado;
	}    
    
    public boolean ConfiguracionTipoDocumentoProrrateo(String tipoDocumento, String numeroDocumento) throws SQLException, Exception{
    	System.out.println("SEJBOrderNewBean/doSaveOrder/Configuracion2 TipoDocumentoProrrateo :" +tipoDocumento + "numeroDocumento: "+numeroDocumento );
    	
		boolean flag = false;
		boolean isPrefijoNumDocNoValido = false;
		String tipo_doc = tipoDocumento;
		String numero_doc = numeroDocumento;
		String doc_validos;
	    String strMessage = null;
	    List arrTableList = new ArrayList();

		Map<String, String> map = new HashMap<String, String>();

		HashMap datosConfigPA = objGeneralDAO.getTableList("CONF_TIPO_DOC_PRORRATEO","1");

    	strMessage = (String) datosConfigPA.get("strMessage");
	    if(strMessage != null){
	    	throw new Exception(strMessage);
	    }

	     arrTableList = (List)datosConfigPA.get("arrTableList");	
	    	    
	     doc_validos = MiUtil.getNpTable(arrTableList , "PREFIJO_NUM_DOC_NO_VALIDO").get("wv_npValueDesc");
	     	        	
		String[] listaPrefijoNumDoc = doc_validos.split(",");
		System.out.println("doc__validos2 :" + doc_validos);
		
		for(int i=0;i<=arrTableList.size();i++){
			
            map=(HashMap)arrTableList.get(i);
            	
	         if(map.get("wv_npValueDesc").equals(tipo_doc)){
	        	 
					System.out.println(" map.get(wv_npValueDesc) = " + map.get("wv_npValueDesc"));	
				    if(map.get("wv_npValueDesc").equals("RUC")){
						System.out.println(" RUC=====> " + map.get("wv_npValueDesc"));
						
						for (String prefijoNumDoc : listaPrefijoNumDoc) {

							if (numero_doc.startsWith(prefijoNumDoc)) {
								System.out.println("prefijoNumDoc2::::" + prefijoNumDoc);
								System.out.println("Doc RUC2::::" + tipo_doc);
								flag = true;
								break;
							}
						}
						
				    }else {
				    	flag = true;
				    }
				    break;
	         }  						
		}
		
		System.out.println("numero de doc2 :" + numero_doc);

		return flag;

    }  
    
    
    
    public String saveUpdateItems(RequestHashMap request,Connection conn,OrderBean orderBean, List<ItemBean> listItemSave) throws Exception{
    	System.out.println("SEJBOrderEditBean/updOrder/saveUpdateItems -> orderBeanId : " + orderBean.getNpOrderId());
    	SectionItemEvents sectionItemEvent = new SectionItemEvents();    	
    	String strMessage = sectionItemEvent.actualizarItems(request, conn, listItemSave);
    	System.out.println("SEJBOrderEditBean/updOrder/saveUpdateItems -> strMessage : " + strMessage);
    	if( strMessage!=null){
            if (conn != null) conn.rollback();
            return strMessage;
        }
    	System.out.println("SEJBOrderEditBean/updOrder/saveUpdateItems -> listItemSave : " + listItemSave);
    	
    	validarItemsProrrateo(request, listItemSave);
    	
    	System.out.println("SEJBOrderEditBean/updOrder/validarItemsProrrateo -> listItemSave.size : " + listItemSave.size());
    	return null;
    }
    
    public String insertSectionDynamicsProrrateo(long strSpecificationId, RequestHashMap objHashMap,Connection conn) throws Exception{
    	System.out.println("SEJBOrderEditBean/insertSectionDynamics [INI]");
    	String strNpeventName="";
        String strNameMethod="";
        String strNameClass="";
        String strMessage = null;
        Method method;
        Object objeto;
        Class clase;
        Class clase1;
        Class clase2;
        Class clase3;
        
        try {
        	ArrayList arrLista = new ArrayList();
            SectionDinamicBean sectionDinamicBean = new SectionDinamicBean();
            HashMap hshData=null;
            System.out.println("SEJBOrderEditBean/insertSectionDynamics [strSpecificationId] " + strSpecificationId);
            hshData = objCategoryDAO.getSpecificationData(strSpecificationId, null);       
              
            strMessage=(String)hshData.get("strMessage");   
            if (strMessage!=null)
               throw new Exception(strMessage);                  
                     
            arrLista = (ArrayList)hshData.get("objArrayList");
            System.out.println("SEJBOrderEditBean/insertSectionDynamics [lista.size] " + arrLista.size());
            
            for(int i=0;i< arrLista.size();i++ ){
            	try {
            		sectionDinamicBean = (SectionDinamicBean)arrLista.get(i);
                    strNpeventName     =   MiUtil.getString(sectionDinamicBean.getNpeventname());
                    strNameMethod      =   MiUtil.getString(sectionDinamicBean.getNpeventhandler());
                    strNameClass       =   MiUtil.getString(sectionDinamicBean.getNpobjectname());
                    System.out.println("SEJBOrderEditBean/insertSectionDynamics [strNpeventName] " + strNpeventName + " [strNameMethod] " + strNameMethod + " [strNameClass] " + strNameClass);
                    
                    if (Constante.EDIT_ON_SAVE.equals(strNpeventName) && !"updateSection1".equals(strNameMethod)
                  		  && !"pe.com.nextel.section.sectionItem.SectionItemEvents".equals(strNameClass)){
                 	  clase=Class.forName(strNameClass);
                       System.out.println(" Nombre de la clase " + strNameClass);                        
                       System.out.println(" Nombre del metodo " + strNameMethod);                        
                       clase1=Class.forName("pe.com.nextel.util.RequestHashMap");
                       clase2=Class.forName("java.sql.Connection");
                       objeto=clase.newInstance();
                       
                       method = clase.getMethod(strNameMethod,new Class[] { clase1, clase2});                   
                       strMessage =(String)method.invoke(objeto, new Object[] { objHashMap , conn});  
                       System.out.println("SEJBOrderEditBean/insertSectionDynamics [INSERT-FIN]-[strMessage] " + strMessage);
                       if(strMessage != null) {
                     	  break;
                       }
                    }
    			} catch (Exception e) {
    				System.out.println("[insertSectionDynamicsProrrateo][Exception] " + e.getMessage());
    			}
            	System.out.println("[insertSectionDynamicsProrrateo][SALIO] " + i);
            }
		} catch (Exception e) {
			System.out.println("[insertSectionDynamicsProrrateo][Exception][GENERAL] " + e.getMessage());
		}        
        System.out.println("SEJBOrderEditBean/insertSectionDynamics [FINISH]-[strMessage] " + strMessage);
        return strMessage;     
     }
    
    public void validarItemsProrrateo(RequestHashMap request, List<ItemBean> listItemSave) throws Exception {
    	System.out.println("SEJBOrderEditBean/validarItemsProrrateo [INI]");
        String    strChangedOrder               = MiUtil.getString(request.getParameter("hdnChangedOrder"));
        
        HashMap hshDataMap = (HashMap) request.get(Constante.DATA_STRUCT);
        if(hshDataMap!=null) {
          LoadMassiveItemBean loadMassiveItemBean = (LoadMassiveItemBean) MapUtils.getObject(hshDataMap, "loadMassiveItemBean");
          if(loadMassiveItemBean!=null) {
            strChangedOrder = "S";
          }
        }
        String    pn_order_id                   = request.getParameter("hdnNumeroOrder");
        String[]  pv_item_cantidad              = request.getParameterValues("hdnIndice");
        String[]  pv_item_flag_save             = request.getParameterValues("hdnFlagSave");
                
        System.out.println("SEJBOrderEditBean/validarItemsProrrateo [pn_order_id] " + pn_order_id + " [strChangedOrder] " + strChangedOrder);
        
        ItemBean  itemBean  =  new ItemBean();       
        if( pv_item_cantidad != null ){
         int cantItems = pv_item_cantidad.length;   
         for(int i=0; i<cantItems; i++) {
        	 String itemFlagSave = MiUtil.getStringObject(pv_item_flag_save,i);
        	 System.out.println("SEJBOrderEditBean/validarItemsProrrateo [itemFlagSave] " + itemFlagSave);
        	 
              if( MiUtil.getStringObject(pv_item_flag_save,i).equals("N") ){
            	  System.out.println("[N]");            	  
              } else if( MiUtil.getStringObject(pv_item_flag_save,i).equals("A") && strChangedOrder.equals("S") ){
            	  System.out.println("[A]");
              } else {
            	  String[]  pv_item_pk                      = request.getParameterValues("hdnItemId");
            	  System.out.println("[ItemId] ---> " + MiUtil.parseLong(pv_item_pk[i]));
            	  itemBean.setNpitemid(MiUtil.parseLong(pv_item_pk[i]));
            	  listItemSave.add(itemBean);
              }
              
         	}
        } 
        System.out.println("SEJBOrderEditBean/validarItemsProrrateo [FINISH]");        
    }
    public boolean isClientPostPago(long npOrderId) throws Exception, SQLException{
    	System.out.println("SEJBOrderEditBean/isClientPostPago [orderId]" + npOrderId );
    	try {
			HashMap response = objOrderDAO.getClientPostPago(npOrderId);
			String strMessage = (String)response.get("strMessage");
			if(strMessage != null ) {
    			throw new Exception(strMessage);
    		}
			List<HashMap<String,String>> list = (List)  response.get("objResume");
			
			if(list != null && list.size() > 0) {
				String isPostPago = (String) list.get(0).get("wv_postPago");
				if("S".equals(isPostPago)){
					return true;
				}
			}
		} catch (Exception e) {
			System.out.println("[ERROR] [SEJBOrderEditBean/isClientPostPago] " + e.getMessage());
		}
    	return false;
    }
    
    public HashMap validarPagoAnticipado(HashMap  hshResultado, RequestHashMap request,OrderBean orderCreadaBean, String strActionName, String strSpecificationId, String actionName, long lCustomerId, long lDivisionId, String strUserName, String strCurrentInbox, Connection conn) throws Exception {
    	System.out.println("SEJBOrderEditBean/updOrder/validarPagoAnticipado [INI]");
    	HashMap hshResultadoPA = new HashMap(); 
    	String hdnOrderProrrateo = (request.get("hdnOrderProrrateo") == null ? null : (String) request.get("hdnOrderProrrateo"));
        String apportionment = (request.get("hdnApportionment") == null ? null : (String) request.get("hdnApportionment"));
        String hdnTipoDocument = (request.get("hdnTipoDocumento") == null ? null : (String) request.get("hdnTipoDocumento"));
        String hdnNumeDocument = (request.get("hdnNumDocumento") == null ? null : (String) request.get("hdnNumDocumento")); //JBALCAZAR PRY-1002        
        
        System.out.println("SEJBOrderEditBean/updOrder/data -> [lOrderId] " + orderCreadaBean.getNpOrderId() + " [strActionName] " + strActionName + " [hdnOrderProrrateo] " + hdnOrderProrrateo + " [apportionment] " + apportionment + " [strSpecificationId] " + strSpecificationId + "[hdnTipoDocument] " + hdnTipoDocument + " [strCurrentInbox] " + strCurrentInbox);
        
        if((MiUtil.parseInt(strSpecificationId)==Constante.SPEC_POSTPAGO_VENTA || MiUtil.parseInt(strSpecificationId)==Constante.SPEC_POSTPAGO_PORTA) && !Constante.ACTION_INBOX_ANULAR.equals(strActionName)){
    		orderCreadaBean.setNpCreatedBy(orderCreadaBean.getNpModificationBy());
    		orderCreadaBean.setNpCreatedDate(MiUtil.getTimeStampBD("dd/MM/yyyy"));
    		orderCreadaBean.setNpCustomerId(lCustomerId);
    		orderCreadaBean.setNpDivisionId(lDivisionId);
     		
    		hshResultadoPA = this.createUpdOrdenProrrateo(request, orderCreadaBean, MiUtil.parseInt(strSpecificationId), apportionment, hdnTipoDocument,hdnNumeDocument, lCustomerId, strUserName, strActionName, strCurrentInbox, conn);
             System.out.println("updOrder[SEJBOrderEditBean]: hshResultSaveOrderProrra >" + hshResultadoPA.get("strMessage"));
             if( ((String)hshResultadoPA.get("strMessage")) != null){
            	 if (conn != null) conn.rollback();
                 return hshResultado;
              }else{
             	 ItemBean objItemProrrateo = (ItemBean) hshResultadoPA.get("objItemProrrateo");
             	 hshResultado.put("objItemProrrateo", objItemProrrateo);
             	 OrderBean objOrderProrrateo = (OrderBean)hshResultadoPA.get("objOrderProrrateo");
             	 hshResultado.put("objOrderProrrateo", objOrderProrrateo);             	 
             	 return hshResultadoPA;
             }             
         } else if("SCHILD".equals(hdnOrderProrrateo) && Constante.ACTION_INBOX_ANULAR.equals(strActionName)) {
     		System.out.println("SEJBOrderEditBean/updOrder/[SCHILD]" + hdnOrderProrrateo);
     		String bInvokeBPELProrra = updStatusOrderProrra(strSpecificationId, orderCreadaBean.getNpOrderId(), strUserName, conn);
       	    hshResultado.put("noInvokeBPELProrra",bInvokeBPELProrra);
       	    hshResultado.put("strNextInboxNamePA","Estado " + Constante.ORDER_STATUS_ANULADO);
       	    System.out.println("SEJBOrderEditBean/strNextInboxNamePA :" + (String) hshResultado.get("strNextInboxNamePA"));       	    
     	} else {
     		//Secciones dinamicas
     		hshResultadoPA.put("invokeSectionDynamics","S");
     		System.out.println("updOrder[SEJBOrderEditBean]: invokeSectionDynamics > S");
     	}
        System.out.println("SEJBOrderEditBean/updOrder/validarPagoAnticipado [FIN]");
        return hshResultadoPA;
    }
    
    public HashMap generateOrderPAExtorno(long orderId, long customerId, String strSpecificationId, String strCurrentInbox, String user) throws Exception, SQLException {
    	System.out.println("[INI] SEJBOrderEditBean/updOrder/generateOrderPAExtorno [orderId] " + orderId + " [customerId] " +customerId + " [strSpecificationId] " + strSpecificationId + " [strCurrentInbox] " +strCurrentInbox + " [user] " + user);
    	HashMap hshResultado=new HashMap();
    	HashMap hashMapDAO=new HashMap();
    	HashMap objResultado=new HashMap(); 
    	List arrTableList = null;
    	long orderParentId=new Long(0);
    	String strMessage = null;
    	Connection conn = null;
    	
    	try {    		    		
    		hashMapDAO = getOrder(orderId);
        	OrderBean orderSearchBean   = (OrderBean)hashMapDAO.get("objResume");
        	strMessage = (String) hashMapDAO.get("strMessage");
    	    if(strMessage != null) {
    	    	throw new Exception(strMessage);
    	    }    	
    	            	
        	String orderParentIdTmp = orderSearchBean.getNpOrderParentId();
        	String orderChildIdOld = orderSearchBean.getNpOrderChildId();
        	System.out.println("SEJBOrderEditBean/updOrder/generateOrderPAExtorno [orderParentIdTmp] " + orderParentIdTmp + " [orderChildIdOld] " + orderChildIdOld);
        	
        	if((orderParentIdTmp == null || orderParentIdTmp.equals("")) && (orderChildIdOld==null || orderChildIdOld.equals(""))) {
        		return objResultado;
        	} else {        		
        		hashMapDAO = objGeneralDAO.getTableList("ORDEN_PRORRATEO","1");
            	strMessage = (String) hashMapDAO.get("strMessage");
        	    if(strMessage != null) {
        	    	throw new Exception(strMessage);
        	    }
        	    
            	arrTableList = (List) hashMapDAO.get("arrTableList");
            	if(arrTableList.size()==0){
            		return objResultado;
            	}
            	
            	String specificationPA = MiUtil.getNpTable(arrTableList , "NPSPECIFICATIONID").get("wv_npValueDesc");
            	System.out.println("SEJBOrderEditBean/updOrder/generateOrderPAExtorno [specificationPA] " + specificationPA);
            	
        		// PORTABILIDAD
        		if(orderChildIdOld !=null && !orderChildIdOld.equals("")) {
        			// orden Portabilidad
        			if(MiUtil.parseInt(strSpecificationId)==Constante.SPEC_POSTPAGO_PORTA && Constante.INBOX_PORTABILIDAD_NUMERICA.equals(strCurrentInbox)){
        				orderParentId = orderId;
        				System.out.println("SEJBOrderEditBean/updOrder/generateOrderPAExtorno PORTABILIDAD [orderParentId] " + orderParentId);
            		} else {
            			return objResultado;
            		}
        		// PAGO ANTICIPADO	
        		} else if(orderParentIdTmp!= null && !orderParentIdTmp.equals("")) {
        			if(strSpecificationId.equals(specificationPA)) {
        				orderParentId = MiUtil.parseLong(orderParentIdTmp);
        				orderChildIdOld = orderId + "";
        				System.out.println("SEJBOrderEditBean/updOrder/generateOrderPAExtorno PA [orderParentId] " + orderParentId + " [orderChildIdOld] " + orderChildIdOld);
        			} else {
        				return objResultado;
        			}        			
        		} else {
        			return objResultado;
        		}        		
        	}
        	
        	System.out.println("SEJBOrderEditBean/updOrder/generateOrderPAExtorno-inicia proceso [orderParentId] " + orderParentId + " [orderChildIdOld]" + orderChildIdOld);
        	
    		hashMapDAO  = objOrderDAO.validarPortabilidad(orderParentId);
    		System.out.println("SEJBOrderEditBean/updOrder/generateOrderPAExtorno/validarPortabilidad [strMessage]" + hashMapDAO.get("strMessage") + " [aplicaPortabilidad] " + hashMapDAO.get("aplicaPortabilidad") + " [cantItems] " + hashMapDAO.get("cantItems"));
    		strMessage = (String) hashMapDAO.get("strMessage");
    		System.out.println("SEJBOrderEditBean/updOrder/generateOrderPAExtorno/validarPortabilidad [strMessage] " + strMessage);
    		if(strMessage != null ){
    			System.out.println("SEJBOrderEditBean/updOrder/generateOrderPAExtorno/validarPortabilidad [throw new Exception] " + strMessage);
    			throw new Exception(strMessage);
    		}    		
    		int aplicaPortabilidad = hashMapDAO.get("aplicaPortabilidad") == null ? 0 : (Integer) hashMapDAO.get("aplicaPortabilidad");
    		int cantitems = hashMapDAO.get("cantItems") == null ? -1 : (Integer) hashMapDAO.get("cantItems");    			
			
    		System.out.println("SEJBOrderEditBean/updOrder/generateOrderPAExtorno/validarPortabilidad [aplicaPortabilidad] " + aplicaPortabilidad);
    		if(aplicaPortabilidad == 0 && cantitems != 0) {
    			 return hshResultado;
    		}
    		System.out.println("SEJBOrderEditBean/updOrder/generateOrderPAExtorno paso validarPortabilidad");
    		
            conn = Proveedor.getConnection();
            conn.setAutoCommit(false);  
            
        	if(cantitems == 0) {
        		hshResultado = autorizarAnularOrdenPA(MiUtil.parseLong(orderChildIdOld), user, conn);
        		return hshResultado;
			}
        	
        	System.out.println("SEJBOrderEditBean/updOrder/generateOrderPAExtorno [orderParentId] " + orderParentId +" [orderChildIdOld] " + orderChildIdOld);
        	
        	// crear indicencia orden hijo
    		hashMapDAO = objOrderDAO.createIncidentPRORRA(customerId, user, conn);
    		strMessage = (String) hashMapDAO.get("strMessage");
    		if(strMessage != null ){
    			throw new Exception(strMessage);
    		}	    		
    		
    		Hashtable hshResult = objOrderDAO.getOrderIdNew();
    		
    		System.out.println("SEJBOrderEditBean/updOrder/generateOrderPAExtorno/OrderDAOgetOrderIdNew->"+hshResult.get("wn_orderid"));
    		System.out.println("SEJBOrderEditBean/updOrder/generateOrderPAExtorno/wv_message->"+hshResult.get("wv_message"));
    		if(hshResult.get("wv_message")!=null && !"null".equals(hshResult.get("wv_message"))) {
    			objResultado.put("strMessage", hshResult.get("wv_message"));
    			throw new Exception(hshResult.get("wv_message").toString());
    		}
    		
			System.out.println("SEJBOrderEditBean/updOrder/generateOrderPAExtorno->set data PA");
    		OrderBean orderBean = getDataOrderForProrrateo(orderSearchBean);
    		
    		orderBean.setNpGeneratorType("INC");
			orderBean.setNpGeneratorId(Long.parseLong(String.valueOf(hashMapDAO.get("npincidentid"))));
			orderBean.setNpOrderId(Long.parseLong(String.valueOf(hshResult.get("wn_orderid"))));
			orderBean.setNpOrderCode("P"+orderBean.getNpOrderId());
		    orderBean.setNpSpecification( MiUtil.getNpTable(arrTableList , "NPSPECIFICATION").get("wv_npValueDesc") );
		    orderBean.setNpSpecificationId( Integer.parseInt(MiUtil.getNpTable(arrTableList , "NPSPECIFICATIONID").get("wv_npValueDesc")) );
		    orderBean.setNpType(MiUtil.getNpTable(arrTableList , "NPTYPE").get("wv_npValueDesc") );
		    orderBean.setNpPaymentTerms(Constante.PAYFORM_CONTADO);
		    orderBean.setNpPaymentStatus(Constante.ESTADO_DE_PAGO_ORDEN_PENDIENTE);
		    orderBean.setNpSignDate(null);
		    orderBean.setNpCustomerId(customerId);
		    
		    System.out.println("SEJBOrderEditBean/updOrder/generateOrderPAExtorno/getOrderInsertar->antes de dao");
		    System.out.println("[SEJBOrderEditBean][orderBean] : " + orderBean.toString());
		    
		    HashMap hshResultSaveOrder = objOrderDAO.getOrderInsertar(orderBean,conn);
	        System.out.println("SEJBOrderEditBean/updOrder/generateOrderPAExtorno->"+(String)hshResultSaveOrder.get("strMessage"));
	        strMessage = (String) hshResultSaveOrder.get("strMessage");
	        if( strMessage != null) {
	        	throw new Exception(strMessage);
	        }
	        	
	        System.out.println("SEJBOrderEditBean/updOrder/generateOrderPAExtorno/crea items->setea bean");
	        ItemBean  itemBean    =   new ItemBean();
	        
	        itemBean.setNporderid(orderBean.getNpOrderId());
	        itemBean.setNpproductlineid(Long.parseLong(MiUtil.getNpTable(arrTableList , "NPPRODUCTLINEID").get("wv_npValueDesc")));
	        itemBean.setNpproductid(Long.parseLong(MiUtil.getNpTable(arrTableList , "NPPRODUCTID").get("wv_npValueDesc")));
	        itemBean.setNpmodalitysell(MiUtil.getNpTable(arrTableList , "NPMODALITYSELL").get("wv_npValueDesc"));
	        itemBean.setNpquantity(Integer.parseInt(MiUtil.getNpTable(arrTableList , "NPQUANTITY").get("wv_npValueDesc")));
	        
	        itemBean.setNpcurrency(MiUtil.getNpTable(arrTableList , "NPCURRENCY").get("wv_npValueDesc"));
	        itemBean.setNpkit(MiUtil.getNpTable(arrTableList , "NPKIT").get("wv_npValueDesc"));					        
	        itemBean.setNppricetype(MiUtil.getNpTable(arrTableList , "NPPRICETYPE").get("NPVALUEDESC"));
	        itemBean.setNppricetypeid(Long.parseLong(MiUtil.getNpTable(arrTableList , "NPPRICETYPEID").get("wv_npValueDesc")));
	        itemBean.setNpsolutionid(Long.parseLong(MiUtil.getNpTable(arrTableList , "NPSOLUTIONID").get("wv_npValueDesc")));
	        itemBean.setNpmodificationdate(MiUtil.getDateBD("dd/MM/yyyy"));
	        itemBean.setNpmodificationby(orderBean.getNpCreatedBy() );
	        itemBean.setNpcreateddate(MiUtil.getDateBD("dd/MM/yyyy"));
	        itemBean.setNpcreatedby(orderBean.getNpCreatedBy());
	        
	        itemBean.setNppriceexception("");
	        itemBean.setNprent("");
	        itemBean.setNpdiscount("");
	        itemBean.setNpinstalationprice("");
	        itemBean.setNpinstalationexception("");
	        itemBean.setNpaditionalcost("");
	        itemBean.setNptotalRentaAdelantada("");
	        
			BigDecimal montoTotalProrrateo = new BigDecimal(0);	
			
			 //OBTENER ITEM
			System.out.println("SEJBOrderEditBean/updOrder/generateOrderPAExtorno/obtener items [orderParentId] " + orderParentId);
    		HashMap hshResultItem = objItemDAO.getItemOrder(orderParentId);
    		strMessage=(String) hshResultItem.get("strMessage");
            if(strMessage != null) {
	        	throw new Exception(strMessage);
	        }
            System.out.println("SEJBOrderEditBean/updOrder/generateOrderPAExtorno/obtener items [objArrayList.size] " + ((ArrayList)hshResultItem.get("objArrayList")).size());
            
            List<ItemBean> listItemSave = new ArrayList<ItemBean>();
            for (int i=0; i<((ArrayList)hshResultItem.get("objArrayList")).size();i++) {
            	ItemBean itemBeanParent = (ItemBean)((ArrayList)hshResultItem.get("objArrayList")).get(i);
            	System.out.println("SEJBOrderEditBean/updOrder/generateOrderPAExtorno/obtener item [itemBeanParent] " + itemBeanParent.getNpitemid());
            	
            	HashMap hshResultApi = objOrderDAO.getApiProrra(itemBeanParent.getNpitemid());
            	strMessage=(String) hshResultApi.get("strMessage");
                if(strMessage != null) {
    	        	throw new Exception(strMessage);
    	        }
                
                for (int j=0; j<((ArrayList)hshResultApi.get("objArrayList")).size();j++) {
                	ApportionmentBean apportionmentBean =  (ApportionmentBean)((ArrayList)hshResultApi.get("objArrayList")).get(j);
                	apportionmentBean.setItemId(itemBeanParent.getNpitemid());                	
                	apportionmentBean.setQuantity(itemBeanParent.getNpquantity());
                	
                	int roundedPrice = MiUtil.calculateRoundPA(apportionmentBean.getIgv(), MiUtil.parseDouble(apportionmentBean.getPrice()), apportionmentBean.getQuantity());
                	apportionmentBean.setRoundedPrice(roundedPrice+"");                    	
                	montoTotalProrrateo = montoTotalProrrateo.add(new BigDecimal(roundedPrice));
                	
                	strMessage = apportionmentDAO.saveItemApi(apportionmentBean, orderBean.getNpCreatedBy(), conn);
    				System.out.println("SEJBOrderEditBean/createUpdOrdenProrrateo [saveItemApi-strMessage] " + strMessage);
					if(strMessage != null ) {
						System.out.println("[[strMessage]] " + strMessage);
		    			throw new Exception(strMessage);
		    		}
                }
            }
			
			System.out.println("SEJBOrderNewBean/doSaveOrder/generateOrderPAExtorno/items montoTotalProrrateo ->"+montoTotalProrrateo);				
			itemBean.setNpprice(String.valueOf(montoTotalProrrateo));
	        
	        String resultTransaction = objItemDAO.doSaveItem(itemBean,conn); 
	        System.out.println("SEJBOrderEditBean/updOrder/generateOrderPAExtorno/items->"+resultTransaction);	        
	        
	        objResultado.put("strMessage", resultTransaction);					        
	        objResultado.put("npOrderIdSon", orderBean.getNpOrderId());
	        objResultado.put("objItemProrrateo", itemBean);
	        objResultado.put("objOrderProrrateo", orderBean);
	        
	        strMessage = (String)objResultado.get("strMessage");					        
	        if(strMessage != null){
	        	throw new Exception(strMessage);
	        }
	           
	        // actualiza orden prorrateo
        	long orderChildId= orderBean.getNpOrderId();
        	long itemId= itemBean.getNpitemid();
        	String prorrateo = "S";
        	
        	System.out.println("SEJBOrderEditBean/updOrder/createUpdOrdenProrrateo [orderParentId] "+orderParentId + " [orderChildId] " + orderChildId + " [itemId] " + itemId);
        	strMessage  = objOrderDAO.updateCreateOrderProrratero(orderParentId, orderChildId, prorrateo, itemId, montoTotalProrrateo, orderBean.getNpCreatedBy(),Constante.ORDER_STATUS_ANULADO,4, conn);
    		if(strMessage != null ){
    			throw new Exception(strMessage);
    		}
    		
    		// autorizar extorno
    		System.out.println("SEJBOrderEditBean/updOrder/generateOrderPAExtorno Autorizar extorno [orderChildIdOld] " + orderChildIdOld);
    		hashMapDAO = doAutorizacionDevolucion(MiUtil.parseLong(orderChildIdOld));
    		strMessage =(String)hashMapDAO.get("strMessage");
    		if(strMessage != null) {
    			System.out.println("SEJBOrderEditBean/updOrder/generateOrderPAExtorno [ERROR] " + strMessage);
    			strMessage= "Ocurrio un error al autorizar el extorno de la orden " + orderId;
    	    	throw new Exception(strMessage );
    	    }
    		
            String message = "Se autorizó el extorno y se anuló la orden de pago anticipado "+orderChildIdOld+" y se creó la nueva orden de pago anticipado "+orderChildId+" con un monto de "+montoTotalProrrateo+".";
            
            hshResultado.put("strMessage",message);             
            conn.commit();     
    	}catch (Exception e) {
    		if (conn != null) conn.rollback();
    		strMessage= e.getMessage();
    		System.out.println("SEJBOrderEditBean/updOrder/generateOrderPAExtorno [strMessage-ERROR]" + strMessage);       
    		strMessage= "Ocurrio un error al autorizar el extorno de la orden de Pago Anticipado ";
    		hshResultado.put("strMessage",strMessage);
		   
	     } finally {		 
	    	 if (conn != null){
	    		 conn.close();              
	    	 }
	    }
    	
    	System.out.println("SEJBOrderEditBean/updOrder/generateOrderPAExtorno [FIN]");
    	return hshResultado;
    }
    
    //PRY 0890-JCURI
    private HashMap autorizarAnularOrdenPA(long orderId, String user, Connection conn) throws Exception {
    	System.out.println("[INI] SEJBOrderEditBean/updOrder/autorizarAnularOrdenPA [orderId] " + orderId + " [user] " + user);
    	//EXTORNAR
    	HashMap hashMapDAO=new HashMap();
    	HashMap hshResultado=new HashMap();
    	
		hashMapDAO = doAutorizacionDevolucion(orderId);
		String strMessage =(String)hashMapDAO.get("strMessage");
		if(strMessage != null) {
			System.out.println("SEJBOrderEditBean/updOrder/autorizarAnularOrdenPA [ERROR] " + strMessage);
			strMessage= "Ocurrio un error al autorizar el extorno de la orden de pago anticipado " + orderId+".";
	    	throw new Exception(strMessage );
	    }
		//ANULAR PA
		strMessage  = objOrderDAO.updateCreateOrderProrratero(0L, orderId, "", 0L, new BigDecimal(0), user, Constante.ORDER_STATUS_ANULADO, 2, conn);
		if(strMessage != null ) {
				throw new Exception(strMessage);
		}
		strMessage= "Se autorizó el extorno y se anuló la orden de pago anticipado " + orderId;        
        hshResultado.put("strMessage",strMessage);
        System.out.println("[fin] SEJBOrderEditBean/updOrder/autorizarAnularOrdenPA [orderId] " + orderId + " [user] " + user + " [strMessage] " + strMessage);
        return hshResultado;
    }
    
    public OrderBean getDataOrderForProrrateo(OrderBean order) {
    	OrderBean orderPANewBean = new OrderBean();    	
        orderPANewBean.setNpCustomerId(order.getNpCustomerId());
        orderPANewBean.setNpSiteId(order.getNpSiteId());
        orderPANewBean.setNpCompanyType(order.getNpCompanyType());
        orderPANewBean.setNpProviderGrpId(order.getNpProviderGrpId());
        orderPANewBean.setNpSalesmanName(order.getNpSalesmanName());      
        orderPANewBean.setNpBuildingId(order.getNpBuildingId());
        orderPANewBean.setNpRegionId(order.getNpRegionId());
        orderPANewBean.setNpShipToAddress1(order.getNpShipToAddress1());
        orderPANewBean.setNpShipToAddress2(order.getNpShipToAddress2());
        orderPANewBean.setNpShipToCity(order.getNpShipToCity());
        orderPANewBean.setNpShipToProvince(order.getNpShipToProvince());
        orderPANewBean.setNpShipToState(order.getNpShipToState());
        orderPANewBean.setNpShipToZip(order.getNpShipToZip());
        orderPANewBean.setNpDispatchPlaceId(order.getNpDispatchPlaceId());
        orderPANewBean.setNpCarrierId(order.getNpCarrierId());
        orderPANewBean.setNpTodoId(order.getNpTodoId());
        orderPANewBean.setNpOrigen(order.getNpOrigen());
        orderPANewBean.setNpDescription(order.getNpDescription());
        orderPANewBean.setNpUserName1(order.getNpUserName1());
        orderPANewBean.setNpUserName2(order.getNpUserName2());
        orderPANewBean.setNpUserName3(order.getNpUserName3());        
        orderPANewBean.setNpPaymentStatus(order.getNpPaymentStatus());
        orderPANewBean.setNpPaymentFutureDate(order.getNpPaymentFutureDate());
        orderPANewBean.setNpSignDate(order.getNpSignDate());
        orderPANewBean.setNpScheduleDate(order.getNpScheduleDate());
        orderPANewBean.setNpScheduleDate2(order.getNpScheduleDate2());
        orderPANewBean.setNpExceptionApprove(0);
        orderPANewBean.setNpExceptionInstallation(0);
        orderPANewBean.setNpExceptionPrice(0);
        orderPANewBean.setNpExceptionPlan(0);
        orderPANewBean.setNpExceptionWarrant(0);
        orderPANewBean.setNpExceptionRevenue(0);
        orderPANewBean.setNpExceptionRevenueAmount(0);
        orderPANewBean.setNpExcepcionBillCycle(null);
        orderPANewBean.setNpCreatedBy(order.getNpCreatedBy());
        orderPANewBean.setNpDivisionId(order.getNpDivisionId());
    	orderPANewBean.setNpproposedid(order.getNpproposedid());        
        orderPANewBean.setSalesStructureOriginalId(order.getSalesStructureOriginalId());        
        orderPANewBean.setNpProviderGrpIdData(order.getNpProviderGrpIdData());
        orderPANewBean.setNpCustomerScoreId(order.getNpCustomerScoreId());
        orderPANewBean.setNpShipToReference(order.getNpShipToReference());
        orderPANewBean.setOrderContactBean(order.getOrderContactBean());    	
    	return orderPANewBean;
    }

    private void invocarCalculoProrrateo(RequestHashMap objHashMap, OrderBean orderBean, Connection conn) throws Exception {
    	logger.info("SEJBOrderEditBean/invocarCalculoProrrateo [INICIO]");
    	RequestApportionmentBean request = new RequestApportionmentBean();
    	HashMap responseMap = new HashMap();
    	String strMessage = null;
    	String strCicloAnterior = null;
    	String strCicloNuevo = null;
    	int estadoSW = 1;
    	try {
    		String strTipoDocumento = String.valueOf(objHashMap.get("hdnTypeDocument"));
    		String strNroDocumento = String.valueOf(objHashMap.get("hdnDocument"));
    		String strCreatedby = (String)objHashMap.getParameter("hdnSessionLogin");
    		String[]  pv_item_PlanTarifario_Val = objHashMap.getParameterValues("hdnItemValuetxtItemRatePlan");
    		
    		request.setNroDocument(strNroDocumento);
    		request.setCustomerId(orderBean.getNpCustomerId()+"");
    		request.setTrxId(MiUtil.generateTrackingID());
    		request.setOrderId(orderBean.getNpOrderId()+"");
    		request.setAccion("NEDIACTION");
    		request.setUser(strCreatedby);
    		    		
    		List<ApportionmentBean> items = new ArrayList<ApportionmentBean>();
    		ApportionmentBean itemBean = new ApportionmentBean();
    		itemBean.setPlanId(MiUtil.getStringObject(pv_item_PlanTarifario_Val,0));
    		items.add(itemBean);
    		request.setItems(items);
    		
    		try {
    			responseMap = sejbApportionmentBean.getCalculatePayment(request);
        		long status = (Long) responseMap.get("status");
    			String message = (String) responseMap.get("message");
    			logger.info("SEJBOrderEditBean/invocarCalculoProrrateo [OrderId]" + request.getOrderId() + " [status] " + status + " [message] " + message);
    			
    			if(status==0L || status==-1L) {
    				strCicloAnterior = (String) responseMap.get("OldBillCycle");
    				strCicloNuevo = (String) responseMap.get("NewBillCycle");
    				estadoSW = 0;
    			}
			} catch (Exception e) {
				logger.info("SEJBOrderEditBean/invocarCalculoProrrateo/sejbApportionmentBean.getCalculatePayment [ERROR] " + e.getMessage());
			}    		
			
			strMessage  = objOrderDAO.insertNoProrrateo(orderBean.getNpOrderId(), request.getTrxId(), strTipoDocumento, strNroDocumento, strCicloAnterior, strCicloNuevo, estadoSW, strCreatedby, conn);
    		if(strMessage != null ) {
    			throw new Exception(strMessage);
    		}
    		
    		logger.info("SEJBOrderEditBean/invocarCalculoProrrateo [FIN]");
		} catch (Exception e) {
			logger.info("SEJBOrderEditBean/invocarCalculoProrrateo [ERROR] " + e.getMessage());
			throw new Exception(e);
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
    public HashMap loadUseAddressInOrder(long buildingId, long customerId) throws Exception, SQLException{
        logger.info("******************************** INICIO SEJBOrderEditBean > loadUseAddressInOrder********************************");
        HashMap resultMap = new HashMap();
        try {
            logger.info("buildingId: "+buildingId);
            logger.info("customerId: "+customerId);

            // Validar si se realizo una pre evaluacion al cliente
            resultMap = objOrderDAO.validateLastCustomerPreEvaluation(customerId,buildingId);
            logger.info("resultMap validateLastCustomerPreEvaluation: "+resultMap.toString());

        }catch (Exception ex ){
            logger.error("Exception: ",ex);
            resultMap.put("strMessage", ex.getMessage());
        }

        logger.info("******************************** FIN SEJBOrderEditBean > loadUseAddressInOrder********************************");
        return resultMap;
    }
    
	/*
    Method :    getEditOrderScreenField
    Project:    PRY-1081
    Purpose:    Se agrega al getOrderScreenField la funcionalidad de visualizacion restringida 
				del boton Generar Documentos en INBOX TIENDA01 
    Developer       Fecha       Comentario
    =============   ==========  ======================================================================
    CDIAZ    	   09/04/2018  Creación
    */ 
    public HashMap getEditOrderScreenField(long lOrderId, String strPage, int iSpecificationId, String strInbox, int iUserId, int iAppId) throws Exception, SQLException {
    	logger.info("SEJBOrderEditBean/getEditOrderScreenField [lOrderId] " + lOrderId + " [strPage] " + strPage + " [iSpecificationId] " + iSpecificationId + " [strInbox] " + strInbox + " [iUserId] " + iUserId + " [iAppId] " + iAppId);
		   
    	HashMap hshRetorno = new HashMap();
		hshRetorno = objOrderDAO.getOrderScreenField(lOrderId,strPage);
		String strMessage = (String) hshRetorno.get("strMessage");
		
		logger.info("SEJBOrderEditBean/getEditOrderScreenField [strMessage] " + strMessage);
		
		if(Constante.INBOX_TIENDA01.equals(strInbox.trim())) {
			HashMap hshRetornoValidate = new HashMap();
			hshRetornoValidate = objOrderDAO.validarGenerarDocumento(lOrderId, iSpecificationId, strInbox, iUserId, iAppId);
			   
			String strMessageValidate = (String) hshRetornoValidate.get("strMessage");
			logger.info("SEJBOrderEditBean/getEditOrderScreenField/validarGenerarDocumento [strMessage] " + strMessageValidate);
			   
			if ( strMessageValidate != null  ) {
				   throw new Exception(strMessageValidate);
	      	}
	    	  
			String strAction = (String) hshRetornoValidate.get("strAction");
			logger.info("SEJBOrderEditBean/getEditOrderScreenField/validarGenerarDocumento [strAction] " + strAction);
			   
			if(strAction != null) {
				HashMap hshData = (HashMap) hshRetorno.get("hshData");
				if(strAction.equals("1")) {
					hshData.put("createdocument", "Disabled");
				} 
			}
		}		   
		logger.info("SEJBOrderEditBean/getEditOrderScreenField/[FIN]");
	    return hshRetorno;
	}    
	
	/*
    Method :    getCarrierPlaceOfficeList
    Project:    PRY-1093
    Purpose:    Valida la activacion del check de courier
    Developer       Fecha       Comentario
    =============   ==========  ======================================================================
    JCURI    	    09/04/2018  Creación
    */
    public HashMap getCarrierPlaceOfficeList(int strParamDispatch, String strParamName, String strParamStatus) throws Exception, SQLException {
    	logger.info("SEJBOrderEditBean/getCarrierPlaceOfficeList [strParamDispatch] " + strParamDispatch + " [strParamName] " + strParamName + " [strParamStatus] " + strParamStatus);
    	return objOrderDAO.getCarrierPlaceOfficeList(strParamDispatch, strParamName, strParamStatus);
    }
    
    /*
    Method :    validateStoreRegion
    Project:    PRY-1093
    Purpose:    Valida si la orden es delivery region
    Developer       Fecha       Comentario
    =============   ==========  ======================================================================
    JCURI    	    01/06/2018  Creación
    */
    public boolean validateStoreRegion(long npOrderId) throws Exception {
    	logger.info("SEJBOrderEditBean/validateStoreRegion [npOrderId] " + npOrderId);
    	HashMap hshRetorno = new HashMap();
		hshRetorno = objOrderDAO.validateStoreRegion(npOrderId);
		String strMessage = (String) hshRetorno.get("strMessage");
		if ( strMessage != null  ) {
			logger.info("SEJBOrderEditBean/validateStoreRegion/[FIN] [strMessage] "  + strMessage);
			return false;
      	} else {
      		int validateActive = (Integer) hshRetorno.get("validateActive");
      		logger.info("SEJBOrderEditBean/validateStoreRegion/[FIN] [validateActive] "  + validateActive);
      		return validateActive == 1 ? true : false;
      	}		
    }
    
    public void updEditSiteOrdenVep (RequestHashMap request, Connection conn) throws Exception{
        String   strMessage = null;
        OrderBean objOrder = new OrderBean();
        long lOrderId =(request.getParameter("hdnOrderId")==null?0:MiUtil.parseLong(request.getParameter("hdnOrderId")));
        objOrder.setNpOrderId(lOrderId);

        Long siteUnknownId = objSiteDAO.getUnknownSite(lOrderId, conn);

        if(siteUnknownId != null && siteUnknownId != 0){
            strMessage = objOrderDAO.updSiteTmpOrdenVep(objOrder.getNpOrderId(),siteUnknownId,0L,conn);
        }else{
            strMessage = objOrderDAO.updSiteTmpOrdenVep(objOrder.getNpOrderId(),0L,0L,conn);
        }
        logger.info("PBI000000042016: siteUnknownId: " + siteUnknownId);
        logger.info("PBI000000042016: orderId: " + lOrderId);

        if (strMessage!=null){
            throw new Exception(strMessage);
        }
    }
}