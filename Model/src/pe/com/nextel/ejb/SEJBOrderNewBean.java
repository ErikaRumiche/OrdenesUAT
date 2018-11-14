package pe.com.nextel.ejb;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import org.apache.log4j.Logger;
import pe.com.nextel.bean.*;
import pe.com.nextel.dao.AgreementDAO;
import pe.com.nextel.dao.ApportionmentDAO;
import pe.com.nextel.dao.CategoryDAO;
import pe.com.nextel.dao.CreditEvaluationDAO;
import pe.com.nextel.dao.EquipmentDAO;
import pe.com.nextel.dao.ExceptionDAO;
import pe.com.nextel.dao.FraudDAO;
import pe.com.nextel.dao.GeneralDAO;
import pe.com.nextel.dao.InstallmentSalesDAO;
import pe.com.nextel.dao.ItemDAO;
import pe.com.nextel.dao.MassiveOrderDAO;
import pe.com.nextel.dao.OrderDAO;
import pe.com.nextel.dao.PlanDAO;
import pe.com.nextel.dao.ProductDAO;
import pe.com.nextel.dao.ProductLineDAO;
import pe.com.nextel.dao.ProposedDAO;
import pe.com.nextel.dao.Proveedor;
import pe.com.nextel.dao.RepairDAO;
import pe.com.nextel.dao.SalesDataDAO;
import pe.com.nextel.dao.ServiceDAO;
import pe.com.nextel.dao.SiteDAO;
import pe.com.nextel.dao.SpecificationDAO;
import pe.com.nextel.section.sectionItem.SectionItemEvents;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;

public class SEJBOrderNewBean implements SessionBean {

    private static Logger logger = Logger.getLogger(SEJBOrderNewBean.class);

  private SessionContext _context;
  
  OrderDAO          objOrderDAO           = null;
  CategoryDAO       objCategoryDAO        = null;
  PlanDAO           objPlanDAO            = null;
  ProductDAO        objProductDAO         = null;
  ProductLineDAO    objProductLineDAO     = null;
  SpecificationDAO  objSpecificationDAO   = null;
  ItemDAO           objItemDAO            = null;
  EquipmentDAO      objEquipmentDAO       = null;
  ServiceDAO        objServiceDAO         = null;
  SiteDAO           objSiteDAO            = null;
  ExceptionDAO      objExceptionDAO       = null;
  RepairDAO		      objRepairDAO			    = null;
  FraudDAO          objFraudDAO           = null;
  ProposedDAO       objProposedDAO        = null;
  MassiveOrderDAO   objMassiveOrderDAO    = null;
  GeneralDAO        objGeneralDAO         = null;
  AgreementDAO      objAgreementDAO       = null;
  SalesDataDAO      objSalesDataDAO       = null;
  InstallmentSalesDAO objInstallmentSalesDAO = null;
  ApportionmentDAO apportionmentDAO = null;
  SEJBApportionmentBean sejbApportionmentBean = null;
  
  public void ejbCreate() {
    /*Creamos las intancias a los DAO's*/
    objOrderDAO         = new OrderDAO();
    objCategoryDAO      = new CategoryDAO();
    objPlanDAO          = new PlanDAO();
    objProductDAO       = new ProductDAO();
    objProductLineDAO   = new ProductLineDAO();
    objSpecificationDAO = new SpecificationDAO();
    objItemDAO          = new ItemDAO();
    objEquipmentDAO     = new EquipmentDAO();
    objServiceDAO       = new ServiceDAO();
    objSiteDAO          = new SiteDAO();
    objExceptionDAO     = new ExceptionDAO();
    objRepairDAO		     = new RepairDAO();
    objFraudDAO         = new FraudDAO();
    objProposedDAO      = new ProposedDAO();
    objMassiveOrderDAO  = new MassiveOrderDAO();
    objGeneralDAO       = new GeneralDAO();
    objAgreementDAO     = new AgreementDAO();
    objSalesDataDAO     = new SalesDataDAO();
    objInstallmentSalesDAO = new InstallmentSalesDAO();
    apportionmentDAO = new ApportionmentDAO();
    sejbApportionmentBean = new SEJBApportionmentBean();
  }

  public void setSessionContext(SessionContext context) throws EJBException {
      _context = context;
  }

  public void ejbRemove() throws EJBException {
  System.out.println("[SEJBOrderNewBean][ejbRemove()]");
  }

  public void ejbActivate() throws EJBException {
  System.out.println("[SEJBOrderNewBean][ejbRemove()]");
  }

  public void ejbPassivate() throws EJBException {
  System.out.println("[SEJBOrderNewBean][ejbRemove()]");
  }
    
public HashMap doSaveVep(RequestHashMap objHashMap) {
  logger.info("************************** INICIO SEJBOrderNewBean > doSaveVep (RequestHashMap objHashMap)**************************");
  HashMap    hshResultSaveOrder = new HashMap();
      OrderBean orderBean = null;
      String strMessage=null;
      String strCreatedby = null;
      String strCodBSCS = null;
      try {
        HashMap hshData=null;
        orderBean = getOrderData(objHashMap);        
        strCreatedby  = (String)objHashMap.getParameter("hdnSessionLogin");
        strCodBSCS    = (String)objHashMap.getParameter("strCodBSCS");
        long lOrderId = MiUtil.parseLong(((String)objHashMap.get("hdnNumeroOrder")));
        logger.info("strCreatedby    : "+strCreatedby);
        logger.info("strCodBSCS      : "+strCodBSCS);
        logger.info("lOrderId        : "+lOrderId);
        //Inicio: PRY-0864 | AMENDEZ
        HashMap hshInstallmentSales = objInstallmentSalesDAO.doCreateInstallmentSales(lOrderId,                   // long lOrderId,
                                       orderBean.getNpCustomerId(),// long lCustomerId,
                                       orderBean.getNpSiteId(),    // long lSiteId, 
                                       0L,                         // long lCustomerbscsId,
                                       strCodBSCS,                 // String strCodbscs,
                                       orderBean.getNpNumCuotas(), // long lquotaNumber,
                                       orderBean.getNpAmountVep(), // double dAmountTotal,
                                       "ORDER_NEW",  // String  strOrigenStatus,
                                       strCreatedby,
                                       orderBean.getInitialQuota(),
                                       //INICIO: PRY-0980 | AMENDEZ
                                       orderBean.getNpPaymentTermsIQ()
                                       //FIN: PRY-0980 | AMENDEZ
                                       );

          //Fin: PRY-0864 | AMENDEZ
          strMessage=(String)hshInstallmentSales.get("strMessage");
          if( strMessage!=null){
            hshResultSaveOrder.put("strMessage",strMessage);
            return hshResultSaveOrder;
          }
      }catch(Exception e) {
          hshResultSaveOrder.put("strMessage",e.getMessage());
      }

    logger.info("************************** FIN SEJBOrderNewBean > doSaveVep (RequestHashMap objHashMap)**************************");
     return hshResultSaveOrder;
  }
  
  public HashMap doDeleteVep(RequestHashMap objHashMap)
  {
      HashMap    hshResultDeleteVep = new HashMap();
      OrderBean orderBean = null;
      String strMessage=null;
      String strCreatedby = null;
      try {
        HashMap hshData = null;                  
        orderBean    = getOrderData(objHashMap);
        strCreatedby = (String)objHashMap.getParameter("hdnSessionLogin");
        HashMap hshInstallmentSales = objInstallmentSalesDAO.doDeleteInstallmentSales(orderBean.getNpOrderId());
        strMessage=(String)hshInstallmentSales.get("strMessage");
        if( strMessage!=null){
           hshResultDeleteVep.put("strMessage",strMessage);
           return hshResultDeleteVep;
        }
          
      }catch(Exception e)
        {
        e.printStackTrace();
          hshResultDeleteVep.put("strMessage",e.getMessage());
        }
     return hshResultDeleteVep;
  }
  
  public HashMap doValidateExistVep(RequestHashMap objHashMap)
  {
      HashMap    hshResult = new HashMap();
      OrderBean orderBean = null;
      String strMessage=null;
      String strCreatedby = null;
      try {
        HashMap hshData = null;                  
        orderBean    = getOrderData(objHashMap);
        strCreatedby = (String)objHashMap.getParameter("hdnSessionLogin");
        HashMap hshInstallmentSales = objInstallmentSalesDAO.doValidateExistInstallmentSales(orderBean.getNpOrderId());
        strMessage=(String)hshInstallmentSales.get("strMessage");
        if( strMessage!=null){
           hshResult.put("strMessage",strMessage);
           return hshResult;
        }
        
        hshResult.put("strIsVep",(String)hshInstallmentSales.get("strIsVep"));
      }catch(Exception e)
        {
        e.printStackTrace();
          hshResult.put("strMessage",e.getMessage());
        }
     return hshResult;
  }
    
  /**
   * Motivo : Registra la orden y las secciones relacionadas a la categoría 
   *          seleccionada
   * @return HashMap
   * @param objHashMap
   */
  public HashMap doSaveOrder(RequestHashMap objHashMap){
      logger.info("************************** INICIO SEJBOrderNewBean > doSaveOrder (RequestHashMap objHashMap)**************************");
      String     strMessage = null;
      HashMap    hshResultSaveOrder = new HashMap();
      int        intSpecificationId;
  
  try {
      //Obtener la conexion del pool de conexiones (DataSource)
      Connection conn = Proveedor.getConnection();
      
      //Desactivar el commit automatico de la conexion obtenida
      conn.setAutoCommit(false);
      
      OrderBean orderBean = null;
      try {
        SpecificationBean objSpecificationBean=null;
        HashMap hshData=null;                  
        orderBean = getOrderData(objHashMap);
        logger.info("orderBean.toString(): "+orderBean.toString());

          //Validacion de cuota VEP EFLORES PM0011359
          Constante.VEP_OPERATIONS_ENUM result = (Constante.VEP_OPERATIONS_ENUM) objHashMap.get(Constante.VEP_OPERATIONS);
          if(result != null){
              logger.info("[PM0011359][SEJBOrderNewBean][doSaveOrder] Valor de OPERACION VEP A REALIZAR = "+result);
              if(result == Constante.VEP_OPERATIONS_ENUM.DELETE_AND_SAVE_VEP){
                  hshResultSaveOrder = objInstallmentSalesDAO.doDeleteInstallmentSales(orderBean.getNpOrderId(),conn);
                  if(((String)hshResultSaveOrder.get("strMessage"))!=null){
                      if (conn != null) conn.rollback();
                      return hshResultSaveOrder;
                  }
                  String strCreatedby  = (String)objHashMap.getParameter("hdnSessionLogin");
                  String strCodBSCS    = (String)objHashMap.getParameter("strCodBSCS");
                  long lOrderId = MiUtil.parseLong(((String)objHashMap.get("hdnNumeroOrder")));
                  logger.info("[PM0011359][SEJBOrderNewBean][doSaveOrder] Invocando doCreateInstallmentSales");
                  logger.info("[PM0011359][SEJBOrderNewBean][doSaveOrder] lOrderId = "+lOrderId);
                  logger.info("[PM0011359][SEJBOrderNewBean][doSaveOrder] NpCustomerId = "+orderBean.getNpCustomerId());
                  logger.info("[PM0011359][SEJBOrderNewBean][doSaveOrder] NpSiteId = "+orderBean.getNpSiteId());
                  logger.info("[PM0011359][SEJBOrderNewBean][doSaveOrder] OL");
                  logger.info("[PM0011359][SEJBOrderNewBean][doSaveOrder] CodBscs = "+strCodBSCS);
                  logger.info("[PM0011359][SEJBOrderNewBean][doSaveOrder] NumCuotas = "+orderBean.getNpNumCuotas());
                  logger.info("[PM0011359][SEJBOrderNewBean][doSaveOrder] AmountVep = "+orderBean.getNpAmountVep());
                  logger.info("[PM0011359][SEJBOrderNewBean][doSaveOrder] ORDER_NEW");
                  logger.info("[PM0011359][SEJBOrderNewBean][doSaveOrder] CreatedBy = "+strCreatedby);

                  //Inicio: PRY-0864 | AMENDEZ
                  hshResultSaveOrder = objInstallmentSalesDAO.doCreateInstallmentSales(
                          lOrderId,
                          orderBean.getNpCustomerId(),
                          orderBean.getNpSiteId(),
                          0L,
                          strCodBSCS,
                          orderBean.getNpNumCuotas(),
                          orderBean.getNpAmountVep(),
                          "ORDER_NEW",
                          strCreatedby,
                          orderBean.getInitialQuota(),
                          //INICIO: PRY-0980 | AMENDEZ
                          orderBean.getNpPaymentTermsIQ(),
                          //FIN: PRY-0980 | AMENDEZ
                          conn);
                  //Fin: PRY-0864 | AMENDEZ

                  if(((String)hshResultSaveOrder.get("strMessage"))!=null){
                      if (conn != null) conn.rollback();
                      return hshResultSaveOrder;
                  }
                  logger.info("[PM0011359][SEJBOrderNewBean][doSaveOrder] DELETE_AND_SAVE_VEP Ejecucion de comando");
              }else if(result == Constante.VEP_OPERATIONS_ENUM.DELETE_VEP){
                  logger.info("[PM0011359][SEJBOrderNewBean][doSaveOrder] SAVE_VEP Ejecucion de comando");
                  hshResultSaveOrder = objInstallmentSalesDAO.doDeleteInstallmentSales(orderBean.getNpOrderId(),conn);
                  if(((String)hshResultSaveOrder.get("strMessage"))!=null){
                      if (conn != null) conn.rollback();
                      return hshResultSaveOrder;
                  }
              }
          }
        
        hshResultSaveOrder=OrderValidations(MiUtil.parseLong((String)objHashMap.getParameter("hdnSessionUserid")) ,orderBean,conn);
        if( ((String)hshResultSaveOrder.get("strMessage"))!=null){
            return hshResultSaveOrder;
        }
        
        //Validación de Acuerdos Comerciales
        hshResultSaveOrder=agreementValidations(objHashMap,orderBean,conn);
        if( ((String)hshResultSaveOrder.get("strMessage"))!=null){
            return hshResultSaveOrder;
        }
        
        //Inicio Reserva de Numeros Golden - 15/11/2010 - FPICOY
        intSpecificationId = Integer.parseInt(objHashMap.getParameter("cmbSubCategoria")!=null?(String)objHashMap.getParameter("cmbSubCategoria"):"0");
        if ((intSpecificationId==Constante.SPEC_POSTPAGO_VENTA || intSpecificationId==Constante.SPEC_CAMBIO_NUMERO)) {
           hshResultSaveOrder=this.reserveGoldenNumber(objHashMap,conn,intSpecificationId);
           if( ((String)hshResultSaveOrder.get("strMessage"))!=null){
              return hshResultSaveOrder;
           }
        }
        //Fin Reserva de Numeros Golden - 15/11/2010 - FPICOY
        
        //Se graba la orden
        logger.info("===============REGISTRO DE LA ORDEN ==> "+orderBean.getNpOrderId() +" === USUARIO ==> "+orderBean.getNpCreatedBy() +" =======================================");
        
        hshResultSaveOrder = objOrderDAO.getOrderInsertar(orderBean,conn);
       logger.info("SEJBOrderNewBean/doSaveOrder/getOrderInsertar/->"+(String)hshResultSaveOrder.get("strMessage"));
        if( ((String)hshResultSaveOrder.get("strMessage"))!=null){
          if (conn != null) conn.rollback();
          return hshResultSaveOrder;
        }
        
        //PBI000000042016
        String especId   = (objHashMap.get("hdnSpecification")==null?"":(String)objHashMap.get("hdnSpecification"));

        HashMap mapEspecificacionResPago = objGeneralDAO.getTableList("SINC_RESP_SPEC", "a");
        ArrayList <HashMap> arrEspecificacionResPago = (ArrayList <HashMap>)mapEspecificacionResPago.get("arrTableList");

        if(arrEspecificacionResPago != null && arrEspecificacionResPago.size()>0) {
           for (int i = 0; i < arrEspecificacionResPago.size(); i++) {
               if(((String)arrEspecificacionResPago.get(i).get("wv_npValue")).equals(especId)){
                   Long siteUnknownId = objSiteDAO.getUnknownSite(orderBean.getNpOrderId(), null);
                   Long siteId = orderBean.getNpSiteId();
                   if(siteId == null){
                       siteId = 0L;
                   }
                   if(siteUnknownId != null && siteUnknownId != 0){
                       strMessage = objOrderDAO.updSiteTmpOrdenVep(orderBean.getNpOrderId(),siteUnknownId,siteId,conn);
                   }else{
                       strMessage = objOrderDAO.updSiteTmpOrdenVep(orderBean.getNpOrderId(),siteId,siteId,conn);
                   }

                   logger.info("PBI000000042016: orderId: " + orderBean.getNpOrderId());
                   logger.info("PBI000000042016: siteId: " + orderBean.getNpSiteId());
                   logger.info("PBI000000042016: siteUnknownId : " + siteUnknownId);

                   if (strMessage!=null){
                       throw new Exception(strMessage);
                   }
               }
           }
        }

        //PRY-0762 MVERA
        HashMap hshResultSaveOrderRA = this.createOrdenRentaAdelantada(objHashMap, orderBean, conn);
        logger.info("doSaveOrder[SEJBOrderNewBean]: hshResultSaveOrderRA >"+hshResultSaveOrderRA.get("strMessage"));
        if( ((String)hshResultSaveOrderRA.get("strMessage"))!=null){
            if (conn != null) conn.rollback();
            return hshResultSaveOrder;
         }else{
        	 ItemBean objItemRentaAdelantada = (ItemBean) hshResultSaveOrderRA.get("objItemRentaAdelantada");
        	 hshResultSaveOrder.put("objItemRentaAdelantada", objItemRentaAdelantada);
        	 OrderBean objOrderRentaAdelantada = (OrderBean)hshResultSaveOrderRA.get("objOrderRentaAdelantada");
        	 hshResultSaveOrder.put("objOrderRentaAdelantada", objOrderRentaAdelantada);        	 
        	 SpecificationBean objSpecificationRentaAdelantada = (SpecificationBean)hshResultSaveOrderRA.get("objSpecificationRentaAdelantada");
        	 hshResultSaveOrder.put("objSpecificationRentaAdelantada", objSpecificationRentaAdelantada);
         }
                
    /*    Se está comentando esta parte de este codigo temporalmente por motivo de que
     *    falta definir funcionalmente como va ser la creación de ordenes desde campañas (phidalgo)
        System.out.println("El origen= "+orderBean.getNpOrigen());
        //se graba el detalle de la orden generada desde la campaña.      
        if(Constante.NAME_CAMP_DET.equals(orderBean.getNpOrigen())){
          System.out.println("Se inicia con la inserción del detalle de la orden creada desde una campaña");
          hshResultSaveOrder = objOrderDAO.getOrderCampaniaInsertar(orderBean, conn);
           if( ((String)hshResultSaveOrder.get("strMessage"))!=null){
            if (conn != null){
              conn.rollback();
            }
            return hshResultSaveOrder;
          }
        }
       */
        logger.info("doSaveOrder[SEJBOrderNewBean]: strGeneratorType >" +orderBean.getNpOrigen() + " / strSpecificationId > "+orderBean.getNpSpecificationId());
        if(( orderBean.getNpSpecificationId()== Constante.SPEC_CDI_VIAJERO) && (orderBean.getNpOrigen().equals(Constante.NAME_ORIGEN_FFPEDIDOS))){
            //Genera Incidente
            //KSALVADOR
            logger.info("Genera Incidente desde la orden");
            strMessage=insIncidentWeb(objHashMap,conn);
            logger.info("SEJBOrderEditBean/updOrder/insIncidentWeb/->"+strMessage);
            if( strMessage!=null){
              if (conn != null) conn.rollback();
              hshResultSaveOrder.put("strMessage",strMessage);
              return hshResultSaveOrder;
            }
         }
         
         if((( orderBean.getNpSpecificationId()== Constante.SPEC_CAMBIAR_PLAN_TARIFARIO) || ( orderBean.getNpSpecificationId()== Constante.SPEC_CAMBIAR_ESTRUCT_CUENTA) ||
            ( orderBean.getNpSpecificationId()== Constante.SPEC_TRANSFERENCIA) || ( orderBean.getNpSpecificationId()== Constante.SPEC_ACTIVAR_DESACTIVAR_SERVICIOS) )  && 
            (orderBean.getNpOrigen().equals(Constante.NAME_ORIGEN_MASSIVE))){
            //Genera Incidente Massive
            //HRENGIFO
            logger.info("Genera Incidente Massive desde la Orden - HRM");
            strMessage=insertIncidentMassive(objHashMap, conn);
            logger.info("SEJBOrderNewBean/doSaveOrder/insertIncidentMassive/->"+strMessage);
            if( strMessage!=null){
              if (conn != null) conn.rollback();
              hshResultSaveOrder.put("strMessage",strMessage);
              return hshResultSaveOrder;
            }
         }  
         
        //graba los periodos de excepciones
        logger.info("SEJBOrderNewBean/doSaveOrder/Eliminación de los peridodos de excepciones");
        strMessage = doDeleteOrderPeriod(objHashMap, conn);
        logger.info("SEJBOrderNewBean/doSaveOrder/doDeleteOrderPeriod/->"+strMessage);
        if( strMessage!=null){
          if (conn != null) conn.rollback();
          hshResultSaveOrder.put("strMessage",strMessage);
          return hshResultSaveOrder;
        }

        logger.info("SEJBOrderNewBean/doSaveOrder/Registro de los peridodos de excepciones");
        strMessage = doSaveOrderPeriod(objHashMap, conn);
        System.out.println("SEJBOrderNewBean/doSaveOrder/doSaveOrderPeriod/->"+strMessage);
        if( strMessage!=null){
          if (conn != null) conn.rollback();
          hshResultSaveOrder.put("strMessage",strMessage);
          return hshResultSaveOrder;
        }
        
        String strSpecificationId   = (objHashMap.get("hdnSpecification")==null?"":(String)objHashMap.get("hdnSpecification"));          
        long   longSpecificationId  = MiUtil.parseLong(strSpecificationId);
        String isClientPostPago     = String.valueOf(objHashMap.get("hdnIsClientPostPago"));
        
      //PRY-0890 JCURI PRORRATEO
        HashMap  hshResultadoPA = validarPagoAnticipado(hshResultSaveOrder, objHashMap, orderBean, intSpecificationId, conn);
        String invokeSectionDynamics= (String) hshResultadoPA.get("invokeSectionDynamics");
        System.out.println("SEJBOrderEditBean/updOrder [invokeSectionDynamics] " + invokeSectionDynamics);

        if("S".equals(invokeSectionDynamics)){
        logger.info("SEJBOrderNewBean/doSaveOrder/invokeSectionDynamics/Inicio de Registro de las Secciones Dinámicas");
        strMessage  = invokeSectionDynamics(longSpecificationId,objHashMap,conn,orderBean);
        logger.info("SEJBOrderNewBean/doSaveOrder/invokeSectionDynamics/->"+strMessage);
        if( strMessage!=null){
          if (conn != null) conn.rollback();
          hshResultSaveOrder.put("strMessage",strMessage);
          return hshResultSaveOrder;
        }
                  logger.info("SEJBOrderNewBean/doSaveOrder/invokeSectionDynamics/Inicio de Registro de las Secciones Dinámicas");
        }

        //PRY-0890 JCURI PRORRATEO
        if("S".equals(isClientPostPago)) {
        	 strMessage = objOrderDAO.insertClientPostPago(orderBean.getNpOrderId(), isClientPostPago, "No aplica para pago anticipado", orderBean.getNpCreatedBy(), conn);
             if( strMessage!=null){
                 if (conn != null) conn.rollback();
                 hshResultSaveOrder.put("strMessage",strMessage);
                 return hshResultSaveOrder;
             }
        }   
        logger.info("SEJBOrderNewBean/doSaveOrder/updOpportunityUnits/Inicio de Registro de Oportunidades");
        strMessage = objOrderDAO.updOpportunityUnits(MiUtil.parseLong(((String)objHashMap.get("hdnNumeroOrder"))),conn);
        logger.info("SEJBOrderNewBean/doSaveOrder/updOpportunityUnits/->"+strMessage);
        if( strMessage!=null){
          if (conn != null) conn.rollback();
          hshResultSaveOrder.put("strMessage",strMessage);
          return hshResultSaveOrder;
        }
       
        //Inicio Mod CGC  - Datos para BPEL
        hshData=objCategoryDAO.getSpecificationDetail(longSpecificationId,conn);
        strMessage=(String)hshData.get("strMessage");
        if( strMessage!=null){
          if (conn != null) conn.rollback();
          hshResultSaveOrder.put("strMessage",strMessage);
          return hshResultSaveOrder;
        }
        
        //JCASAS Valida si debe contar con Evaluación de Créditos        
        HashMap hshValidateCredit = new HashMap();
        CreditEvaluationDAO objCreditEvaluationDAO = new CreditEvaluationDAO();
        long lOrderId=MiUtil.parseLong((String)objHashMap.get("hdnNumeroOrder"));
        hshValidateCredit = objCreditEvaluationDAO.doValidateCredit(lOrderId,"ORDER");
        if((String)hshValidateCredit.get("strMessage")!=null){
          throw new Exception(strMessage);
        }
        String strValidateCredit = (String)hshValidateCredit.get("flagValidateCredit");
        if(strValidateCredit.equals("1")) {
          HashMap hshResultCustomerScore = objOrderDAO.validateCustomerScore(orderBean.getNpOrderId(), orderBean.getNpCustomerId(),orderBean.getNpSpecificationId(),orderBean.getNpCreatedBy(), conn);
          if((String)hshResultCustomerScore.get("strMessage")!=null){
            if(conn !=null) conn.rollback();
            hshResultSaveOrder.put("strMessage",(String)hshResultCustomerScore.get("strMessage"));
            return hshResultSaveOrder;
          } else {
            String respCustomerScore=(String)hshResultCustomerScore.get("flagCustomerScore");
            logger.info("SEJBOrderNewBean/doSaveOrder/validateCustomerScore/respCustomerScore-> "+respCustomerScore);
            if(respCustomerScore.equals("0")) {
              if(conn !=null)
                conn.rollback();
              hshResultSaveOrder.put("strMessage","El cliente requiere evaluación");
              return hshResultSaveOrder;
            }
          }
        }
        
        //Validaciones para Ventas Data
        //-----------------------------
        String strStatusSalesData =((String)objHashMap.getParameter("hdnStatusData")==null?"":(String)objHashMap.getParameter("hdnStatusData"));
        if (strStatusSalesData!=""){
        
            //Inserta las aplicaciones activas del cliente
            //--------------------------------------------
            strMessage = objSalesDataDAO.insSalesDataActive(orderBean.getNpOrderId(),orderBean.getNpCustomerId(),orderBean.getNpDivisionId(),12, orderBean.getNpCreatedBy(),conn);
            if( strMessage!=null){
              if (conn != null) conn.rollback();
              hshResultSaveOrder.put("strMessage",strMessage);
              return hshResultSaveOrder;
            }
            
             //Actualiza estado de la aplicación
            //---------------------------------
            strMessage = objSalesDataDAO.doUpdateSalesDataStatus(orderBean.getNpOrderId(),orderBean.getNpCustomerId(),orderBean.getNpDivisionId(),12,strStatusSalesData, orderBean.getNpCreatedBy(), conn);
            if( strMessage!=null){
              if (conn != null) conn.rollback();
              hshResultSaveOrder.put("strMessage",strMessage);
              return hshResultSaveOrder;
            }
            
            //Validaciones de Ventas Data
            //---------------------------
            strMessage = objSalesDataDAO.getValidateServiceApp(orderBean.getNpOrderId(),orderBean.getNpCustomerId(),orderBean.getNpDivisionId(), conn);
            if( strMessage!=null){
              if (conn != null) conn.rollback();
              hshResultSaveOrder.put("strMessage",strMessage);
              return hshResultSaveOrder;
            }
        }
        objSpecificationBean=(SpecificationBean)hshData.get("objSpecifBean");
        
        hshResultSaveOrder.put("objSpecificationBean",objSpecificationBean);         
        hshResultSaveOrder.put("strOrderId",orderBean.getNpOrderId()+"");
        hshResultSaveOrder.put("strCustomerId",orderBean.getNpCustomerId()+"");
        //hshResultSaveOrder.put("strSolutionId",orderBean.getNpSolutionId()+"");
		    hshResultSaveOrder.put("strDivisionId",orderBean.getNpDivisionId()+"");
        
        //Confirmar la transaccion en la BD
        conn.commit();
        logger.info("===============FIN DEL REGISTRO DE LA ORDEN ==> "+orderBean.getNpOrderId() +" === USUARIO ==> "+orderBean.getNpCreatedBy() +" =======================================");
        // Fin Mod CGC  - Datos para BPEL          
        return hshResultSaveOrder;
      } catch (Exception e) {
        //Si existe error, deshacer los cambios en la BD
        if (conn != null) conn.rollback();
        logger.info("===============ERROR AL CREAR LA ORDEN ==> "+orderBean.getNpOrderId() +" === USUARIO ==> "+orderBean.getNpCreatedBy() +" =======================================");
        e.printStackTrace();
        strMessage  = MiUtil.getMessageClean("[Exception][doSaveOrder]["+e.getClass() + " " + e.getMessage()+ " - Caused by " + e.getCause() + "]");
        hshResultSaveOrder.put("strMessage",strMessage);
        logger.info("************************** FIN SEJBOrderNewBean > doSaveOrder (RequestHashMap objHashMap)**************************");
        return hshResultSaveOrder;
      } 
      finally {
        //Finalmente, pase lo que pase, cerrar la conexion
        try{
          if (conn != null){
            conn.close();
            conn = null;
          }
        }catch (Exception exConn) {
          exConn.printStackTrace();
          try{
            if (conn != null){
              conn.close();
              conn = null;
            }
          }catch (Exception exConnAux) {
            exConnAux.printStackTrace();
              try{
              if (conn != null){
                conn.close();
                conn = null;
              }
              }catch(Exception exConnAuxiliar){
                exConnAuxiliar.printStackTrace();  
              }
          }
        
        }
        
      }

    } catch (Exception ex) {
      ex.printStackTrace();
      strMessage  = MiUtil.getMessageClean("[Exception][doSaveOrder]["+ex.getClass() + " " + ex.getMessage()+"]");
      hshResultSaveOrder.put("strMessage",strMessage);
      return hshResultSaveOrder;
    }

  }
    
    public OrderBean getOrderData(RequestHashMap request) throws Exception{
        // Variables para funcionalidad de grabación
        logger.info("************************** INICIO SEJBOrderNewBean > getOrderData (RequestHashMap request)**************************");
        String  strOrderId ="",
                strCreatedby = "",
                strDato= "",
                strCodigoCliente= "",
                strMessage = "",
                strTipo= "",
                strStatus= "",
                strnpUser  = "",
                strnpsiteid = "",
                strnpprovidergrpid = "",
                strnpsalesmanname = "",  
                strnpdealername = "",   
                strnpsolutionid = "",
                strnpspecificationid = "",
                strnpbuildingid = "",
                strnpregionid = "",
                strnpSite ="",
                strnpdescription = "",
                strnpcategoria = "",
                strnphdnRegionId = "",
                strnpnumSolicutd = "",
                strtxtEstadoPago = "",
                strnpcmbFormaPago = "",
                strnpdispatchet = "",
                hdncmbCategoria = "",
                hdncmbSubCategoria = "",
                hdncmbVendedor = "",
                hdnExceptionInstallation = "",
                hdnExceptionPrice = "",
                hdnExceptionPlan = "",
                hdnExceptionWarrant = "",
                hdnExceptionRevenue = "",
                hdnExceptionRevenueAmount = "",
                hdnExceptionBillCycle = "",
                strDeliveryAddress = "",
                strDeliveryCity = "",
                strDeliveryProvince = "",
                strDeliveryState = "",
                strGeneratorType = "",
                strOrigenType = "",
                strGeneratorId = "",
                strnpdateprocess ="",
                strnpfuturedate ="",
                strnpsigndate ="",
                strhdnnpdispatchet="",
                strnpPaymentRespId = "",
					      strnpdivisionid = "",
                strnpdatereconex ="",
                strnpproposedid="", //CBARZOLA
                strnpprovidergrpiddata = "",
                hdnSalesStructOrigenId = "",
                strnpdateFinProg="",
                strCarpetaDigital = ""

                //INICIO: PRY-0864 | AMENDEZ
                ,strVepFlag=""
                ,strVepNumCuotas = ""
                ,strTotalSalesPriceVEP = ""
                ,strInicialQuota=""
                ,strNpPaymentTermsIQ="",
                //FIN: PRY-0980 | AMENDEZ

                //INICIO: EST-1098 | AMENDEZ
                strNpuseinfulladdress="",
                strNpdepartmentuseaddress="",
                strNpprovinceuseaddress="",
                strNpdistrictuseaddress="",
                strNpflagcoverage="",
                //FIN: EST-1098 | AMENDEZ

                // BEGIN: PRY-1049 | DOLANO-0002
                strVchHomeServiceZone = "",
                // END: PRY-1049 | DOLANO-0002

                 strFlagMigracion = "";//[TDECONV003] KOTINIANO
        Date   df = null;
        
        /**1.1 Sección de Captura de parametros.
        //------------------------------------
        */ 
        strOrderId              =   (String)request.getParameter("hdnNumeroOrder");
        strCreatedby            =   (String)request.getParameter("hdnSessionLogin");
        strCodigoCliente        =   (String)request.getParameter("txtCompanyId");
		
        //strnpsolutionid         =   (String)request.getParameter("cmbSolucion");
		  strnpdivisionid         =   (String)request.getParameter("cmbDivision");
        strnpspecificationid    =   (String)request.getParameter("cmbSubCategoria");
        strnpcategoria          =   (String)request.getParameter("cmbCategoria");
        strnpprovidergrpid      =   (String)request.getParameter("hdnVendedorId");
        strnpSite               =   (String)request.getParameter("hdnSite");
        strnpUser               =   (String)request.getParameter("hdnSessionUserid");
        strnpspecificationid    =   (String)request.getParameter("hdnSpecification");
        strnphdnRegionId        =   (String)request.getParameter("hdnRegionId");
        strnpdescription        =   (String)request.getParameter("txtDetalle");
        strnpnumSolicutd        =   (String)request.getParameter("txtNumSolicitud");
        strnpcmbFormaPago       =   (String)request.getParameter("cmbFormaPago");
        strnpdispatchet         =   (String)request.getParameter("cmbLugarAtencion");
        hdncmbCategoria         =   (String)request.getParameter("hdncmbCategoria");
        hdncmbSubCategoria      =   (String)request.getParameter("hdncmbSubCategoria");
        hdncmbVendedor          =   (String)request.getParameter("hdncmbVendedor");
        strnpdealername         =   (String)request.getParameter("txtDealer");
        strtxtEstadoPago        =   (String)request.getParameter("txtEstadoPago");
        strnpbuildingid         =   (String)request.getParameter("hdnBuildingId");
        strnpdateprocess        =   (String)request.getParameter("txtFechaProceso");
        strnpfuturedate         =   (String)request.getParameter("txtFechaProbablePago");
        strnpsigndate           =   (String)request.getParameter("txtFechaHoraFirma");
        strhdnnpdispatchet      =   (String)request.getParameter("hdnLugarDespacho");
        strnpdatereconex        =   (String)request.getParameter("txtFechaReconexion"); //rmartinez 15-06-2009
        strnpdateFinProg        =   (String)request.getParameter("txtFechaFinProg"); //*jsalazar - modif hpptt # 1 - 27/09/2010 */        
        
        //Para la dirección de entrega
        strDeliveryAddress      =   (String)request.getParameter("hdnDeliveryAddress");
        strDeliveryCity         =   (String)request.getParameter("hdnDeliveryCity");
        strDeliveryProvince     =   (String)request.getParameter("hdnDeliveryProvince");
        strDeliveryState        =   (String)request.getParameter("hdnDeliveryState");
        
        strGeneratorType        =   (String)request.getParameter("hdnGeneratorType");
        
        strOrigenType           =   (String)request.getParameter("hdnOrigenType");
        strGeneratorId          =   (String)request.getParameter("hdnGeneratorId");
        
        /*Campos de Excepciones. */
        hdnExceptionInstallation = (String)request.get("hdnExceptionInstallation");
        hdnExceptionPrice        = (String)request.get("hdnExceptionPrice");
        hdnExceptionPlan         = (String)request.get("hdnExceptionPlan");
        hdnExceptionWarrant      = (String)request.get("hdnExceptionWarrant");
        hdnExceptionRevenue      = (String)request.get("hdnExceptionRevenue");
        hdnExceptionRevenueAmount= (String)request.get("hdnExceptionRevenueAmount");
        hdnExceptionBillCycle    = (String)request.get("hdnExceptionBillCycle");
        strnpproposedid          = (String)request.get("txtPropuesta");//CBARZOLA
        strnpprovidergrpiddata   = (String)request.getParameter("hdnVendedorDataId");
        hdnSalesStructOrigenId   = (String)request.get("hdnSalesStructOrigenId");
        
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
        //EFH Carpeta digital
        strCarpetaDigital = request.getParameter("hdnCarpetaDigital");          

        //Ini: [TDECONV003] KOTINIANO
        strFlagMigracion =  request.getParameter("hdnFlagMigration");
        //Fin: [TDECONV003] KOTINIANO
         
        /*MVERAE Validacion de Responsable de Pago para CAMBIO y DESACTIVACION 
         Se comento por cambio sobre la incidencia 4885*/
        /*strnpPaymentRespId       =   (String)request.getParameter("hdnPaymentRespId");
        if(strnpspecificationid.trim().equalsIgnoreCase("2022") || strnpspecificationid.trim().equalsIgnoreCase("2023")){
            //Si no selecciono responsable de pago, se setea como responsable de la orden al Responsable de Pago de la Bolsa
            if(!MiUtil.isNotNull(strnpSite)){
                strnpSite = strnpPaymentRespId;
            }
        }*/
        /* ------------------------------------------------------------------- */

        //INICIO: PRY-1200 | AMENDEZ
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

        Object objVepNumCuotas=request.getParameter("cmbVepNumCuotas");
        if (iFlagVep == 1 && objVepNumCuotas != null) {
            strVepNumCuotas = (String) objVepNumCuotas;
            iNumCuotasVEP=MiUtil.parseInt(strVepNumCuotas);
        }

        Object objTotalSalesPriceVEP=request.getParameter("hdnTotalSalesPriceVEP");
        if (iFlagVep == 1 && objTotalSalesPriceVEP != null) {
            strTotalSalesPriceVEP = (String) objTotalSalesPriceVEP;
            dTotalSalesPriceVEP=MiUtil.parseDouble(strTotalSalesPriceVEP);
        }

        Object objCuotaInicial=request.getParameter("txtCuotaInicial");
        if(iFlagVep == 1 && objCuotaInicial!=null){
            strInicialQuota=(String)objCuotaInicial;
            dInicialQuotaVEP=MiUtil.parseDouble(strInicialQuota);
        }
        
        Object objPaymentTermsIQ=request.getParameter("txtPaymentTermsIQ");
        if(iFlagVep == 1 && objPaymentTermsIQ!=null){
            strNpPaymentTermsIQ=(String)objPaymentTermsIQ;
            iNpPaymentTermsIQVEP=MiUtil.parseInt(strNpPaymentTermsIQ);
        }
        //FIN: PRY-1200 | AMENDEZ

        //INICIO: EST-1098 | AMENDEZ
        Object objNpuseinfulladdress=request.getParameter("hdnUseFullAddress");
        if(objNpuseinfulladdress!=null){
            strNpuseinfulladdress=(String)objNpuseinfulladdress;
        }

        Object objpdepartmentuseaddress=request.getParameter("hdnRegion");
        if(objpdepartmentuseaddress!=null){
            strNpdepartmentuseaddress=(String)objpdepartmentuseaddress;
        }

        Object objNpprovinceuseaddress=request.getParameter("hdnProvince");
        if(objNpprovinceuseaddress!=null){
            strNpprovinceuseaddress=(String)objNpprovinceuseaddress;
        }

        Object objNpdistrictuseaddress=request.getParameter("hdnDistrict");
        if(objNpdistrictuseaddress!=null){
            strNpdistrictuseaddress=(String)objNpdistrictuseaddress;
        }

        Object objNpflagcoverage=request.getParameter("hdnCobertura");
        if(objNpflagcoverage!=null){
            strNpflagcoverage=(String)objNpflagcoverage;
        }
        //FIN: EST-1098 | AMENDEZ

        // BEGIN: PRY-1049 | DOLANO-0002
        Object objVchHomeServiceZone = request.getParameter("hdnHomeServiceZone");
        if(objVchHomeServiceZone != null){
            strVchHomeServiceZone = (String) objVchHomeServiceZone;
        }
        // END: PRY-1049 | DOLANO-0002

        OrderBean orderBean = new OrderBean();
        
        orderBean.setNpOrderId(MiUtil.parseInt(strOrderId));
        orderBean.setNpCustomerId(MiUtil.parseLong(strCodigoCliente));
        orderBean.setNpCreatedDate(MiUtil.getTimeStampBD("dd/MM/yyyy"));
        orderBean.setNpCreatedBy(strCreatedby);
        
        orderBean.setNpBuildingId(MiUtil.parseLong(strnpbuildingid));
        orderBean.setNpSiteId(MiUtil.parseLong(strnpSite));
        orderBean.setNpSpecificationId(MiUtil.parseInt(strnpspecificationid));
        //orderBean.setNpSolutionId(MiUtil.parseInt(strnpsolutionid));
		  orderBean.setNpDivisionId(MiUtil.parseInt(strnpdivisionid));
        orderBean.setNpSalesmanName(hdncmbVendedor);
        orderBean.setNpDealerName(strnpdealername);
        orderBean.setNpType(hdncmbCategoria);
        orderBean.setNpSpecification(hdncmbSubCategoria);
        orderBean.setNpProviderGrpId(MiUtil.parseInt(strnpprovidergrpid));
        orderBean.setNpDescription(strnpdescription);
        orderBean.setNpRegionId(MiUtil.parseInt(strnphdnRegionId));
        orderBean.setNpOrderCode(strnpnumSolicutd);
        orderBean.setNpPaymentTerms(strnpcmbFormaPago);
        orderBean.setNpPaymentStatus(strtxtEstadoPago);
        if(strnpdispatchet==null)
          orderBean.setNpDispatchPlaceId(MiUtil.parseInt(strhdnnpdispatchet));
        else{
          orderBean.setNpDispatchPlaceId(MiUtil.parseInt(strnpdispatchet));
        }
        orderBean.setNpPaymentFutureDate(MiUtil.toFecha(strnpfuturedate,"dd/MM/yyyy"));
        
        /*jsalazar - modif hpptt # 1 - 27/09/2010 - inicio*/
        if(!("".equals(strnpdateprocess.trim()))){
        orderBean.setNpScheduleDate(MiUtil.toFecha(strnpdateprocess,"dd/MM/yyyy"));
        }
        /*jsalazar - modif hpptt # 1 - 27/09/2010 - fin*/
        orderBean.setNpSignDate(MiUtil.toFechaHora(strnpsigndate,"dd/MM/yyyy HH:mm"));
        /*jsalazar - modif hpptt # 1 - 27/09/2010 - inicio*/
        if(Constante.SPEC_SERVICIOS_ADICIONALES[0]== MiUtil.parseInt(strnpspecificationid)){
        orderBean.setNpScheduleDate2(MiUtil.toFecha(strnpdateFinProg, "dd/MM/yyyy"));
        }else{
        orderBean.setNpScheduleDate2(MiUtil.toFecha(strnpdatereconex, "dd/MM/yyyy"));
        }
        /*jsalazar - modif hpptt # 1 - 27/09/2010 - Fin*/
        
        
        orderBean.setNpShipToAddress1(strDeliveryAddress);
        orderBean.setNpShipToCity(strDeliveryCity);
        orderBean.setNpShipToProvince(strDeliveryProvince);
        orderBean.setNpShipToState(strDeliveryState);
        
        orderBean.setNpGeneratorType(strGeneratorType);
        orderBean.setNpGeneratorId(MiUtil.parseLong(strGeneratorId));
        orderBean.setNpOrigen(strOrigenType); 
        
        orderBean.setNpExceptionInstallation(MiUtil.parseInt(hdnExceptionInstallation));
        orderBean.setNpExcepcionBillCycle(hdnExceptionBillCycle);
        orderBean.setNpExceptionPlan(MiUtil.parseInt(hdnExceptionPlan));
        orderBean.setNpExceptionPrice(MiUtil.parseInt(hdnExceptionPrice));
        orderBean.setNpExcepcionBillCycle(hdnExceptionBillCycle);
        orderBean.setNpExceptionRevenueAmount(MiUtil.parseDouble(hdnExceptionRevenueAmount));
        orderBean.setNpExceptionRevenue(MiUtil.parseInt(hdnExceptionRevenue));
        orderBean.setNpproposedid(MiUtil.parseLong(strnpproposedid));
        orderBean.setNpProviderGrpIdData(MiUtil.parseLong(strnpprovidergrpiddata));        
        orderBean.setSalesStructureOriginalId(MiUtil.parseLong(hdnSalesStructOrigenId));
        
        orderBean.setNpFlagCourier(MiUtil.parseInt(request.getParameter("hdnChkCourier")));                
        orderBean.setNpCustomerScoreId(MiUtil.parseLong(request.getParameter("customerscoreid")));
        
        orderBean.setNpShipToReference(strReference); // [N_O000017567] MMONTOYA
        
        orderBean.setNpReasonCdmId(MiUtil.parseInt(request.getParameter("cmbReasonCdm"))); // [reason Change of model] CDIAZ

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
        //FIN DERAZO
        orderBean.setOrderContactBean(orderContactBean);
        // Fin datos del contacto
        //EFH Carpeta digital
        orderBean.setNpCarpetaDigital(strCarpetaDigital);
        //INICIO: PRY-1200 | AMENDEZ
        orderBean.setNpFlagVep(iFlagVep);
        orderBean.setNpNumCuotas(iNumCuotasVEP);
        orderBean.setNpAmountVep(dTotalSalesPriceVEP);
        orderBean.setInitialQuota(dInicialQuotaVEP);
        orderBean.setNpPaymentTermsIQ(iNpPaymentTermsIQVEP);
        //FIN: PRY-1200 | AMENDEZ

         //Ini: [TDECONV003] KOTINIANO
        orderBean.setNpFlagMigracion(strFlagMigracion);
        //Fin: [TDECONV003] KOTINIANO

        //INICIO: EST-1098 | AMENDEZ
        orderBean.setNpuseinfulladdress(strNpuseinfulladdress);
        orderBean.setNpprovinceuseaddress(strNpprovinceuseaddress);
        orderBean.setNpdepartmentuseaddress(strNpdepartmentuseaddress);
        orderBean.setNpdistrictuseaddress(strNpdistrictuseaddress);
        orderBean.setNpflagcoverage(MiUtil.parseInt(strNpflagcoverage));
        //FIN: EST-1098 | AMENDEZ

        // BEGIN: PRY-1049 | DOLANO-0002
        orderBean.setVchHomeServiceZone(strVchHomeServiceZone);
        // END: PRY-1049 | DOLANO-0002

logger.info("************************** FIN SEJBOrderNewBean > getOrderData (RequestHashMap request)**************************");
         return orderBean;
         
  }
  
  public String invokeSectionDynamics(long strSpecificationId, RequestHashMap objHashMap,Connection conn,OrderBean orderBean) throws Exception{
    logger.info("************************** INICIO SEJBOrderNewBean > invokeSectionDynamics (long strSpecificationId, RequestHashMap objHashMap,Connection conn,OrderBean orderBean) **************************");    
    logger.info(" ========INICIO DE LAS SECCIONES DINÁMICAS #Orden -> "+orderBean.getNpOrderId() +" - Usuario -> "+orderBean.getNpCreatedBy()+" - EVENTO ON_SAVE====SPECIFICATIONID "+strSpecificationId+"=======");
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
    
    
    String hdnOrigenTypeMassive = objHashMap.getParameter("hdnOrigenType");
    
    logger.info("hdnOrigenTypeMassive HRM2==> "+hdnOrigenTypeMassive );
    logger.info("[EDIT][invokeSectionDynamics] strSpecificationId : " + strSpecificationId);
    logger.info("[EDIT][invokeSectionDynamics] hdnOrigenTypeMassive : " + hdnOrigenTypeMassive);
    // Se agrego el Nuevo parametro para masivos
    HashMap objHashMapSpec = objCategoryDAO.getSpecificationData(strSpecificationId, hdnOrigenTypeMassive);       
    
    if( objHashMapSpec.get("strMessage")!=null ) return (String)objHashMapSpec.get("strMessage");
    
    arrLista = (ArrayList)objHashMapSpec.get("objArrayList");
    System.out.println("[EDIT][invokeSectionDynamics] " + arrLista.size());
    for(int i=0;i< arrLista.size();i++ ){
      sectionDinamicBean = (SectionDinamicBean)arrLista.get(i);
      logger.info("[EDIT][invokeSectionDynamics] Npeventname : " + sectionDinamicBean.getNpeventname());  
      if (Constante.NEW_ON_SAVE.equals(MiUtil.getString(sectionDinamicBean.getNpeventname()))){
    	logger.info("[EDIT][invokeSectionDynamics] [EDIT_ON_SAVE] : " + sectionDinamicBean.getNpeventname());
        strNameMethod    =   MiUtil.getString(sectionDinamicBean.getNpeventhandler());
        strNameClass     =   MiUtil.getString(sectionDinamicBean.getNpobjectname());     
            
        clase=Class.forName(strNameClass);
        logger.info("== Nombre de la clase para el registro " + strNameClass);
        logger.info("== Nombre del método invocado " + strNameMethod);
        clase1=Class.forName("pe.com.nextel.util.RequestHashMap");
        clase2=Class.forName("java.sql.Connection");
        objeto=clase.newInstance();
        
        method = clase.getMethod(strNameMethod,new Class[] { clase1, clase2});                   
        strMessage =(String)method.invoke(objeto, new Object[] { objHashMap , conn});  
        
        if( strMessage!=null){
          if (conn != null) conn.rollback();
          return strMessage;
        }

      } //Fin del if
    }//Fin del for
    
    logger.info(" ========FIN DE LAS SECCIONES DINÁMICAS #Orden -> "+orderBean.getNpOrderId() +" - Usuario -> "+orderBean.getNpCreatedBy()+" - EVENTO ON_SAVE====SPECIFICATIONID "+strSpecificationId+"=======");
    logger.info("************************** FIN SEJBOrderNewBean > invokeSectionDynamics (long strSpecificationId, RequestHashMap objHashMap,Connection conn,OrderBean orderBean) **************************");    
  return strMessage;
  
  }
    
    public ArrayList OrderDAOgetSolutionList(String idSolution) throws Exception, SQLException{
        return objOrderDAO.getSolutionList("");
    }

    public ArrayList OrderDAOgetCategoryList(int idSolution) throws Exception, SQLException{
        return objOrderDAO.getCategoryList(idSolution);
    }

    public HashMap CategoryDAOgetSpecificationData(int idSpecification, String strGeneratorType) throws Exception, SQLException{
        return objCategoryDAO.getSpecificationData(idSpecification, strGeneratorType);
    }

    public ArrayList ItemDAOgetHeaderSpecGrp(int idSpecificationId) throws Exception, SQLException{
        return objItemDAO.getHeaderSpecGrp(idSpecificationId);
    }

    public ArrayList ItemDAOgetItemHeaderSpecGrp(int idSpecificationId) throws Exception, SQLException{
        return objItemDAO.getItemHeaderSpecGrp(idSpecificationId);
    }

    public Hashtable OrderDAOgetOrderIdNew() throws Exception, SQLException  {
        return objOrderDAO.getOrderIdNew();
    }

    public ArrayList OrderDAOgetSalesList(int intUserId, int intAppId) throws Exception, SQLException  {
        return objOrderDAO.getSalesList(intUserId,intAppId);
    }

    public HashMap OrderDAOgetDispatchPlaceList(int intSpecialtyId) throws SQLException, Exception  {
        return objOrderDAO.getDispatchPlaceList(intSpecialtyId);
    }

    public HashMap OrderDAOgetBuildingName(int intBuildingid, String strLogin) throws Exception, SQLException  {
        return objOrderDAO.getBuildingName(intBuildingid, strLogin);
    }
    
    //JLIMAYMANTA,RETORNA IDENTIFICADOR DE TIENDA EXPRESS
    
    public HashMap OrderDAOgetBuildingTS(int intBuildingid, String strLogin) throws Exception, SQLException  {
        return objOrderDAO.OrderDAOgetBuildingTS(intBuildingid, strLogin);
    }

    public ArrayList OrderDAOgetModePaymentList(String strParamName, String strParamStatus) throws Exception, SQLException  {
        return objOrderDAO.getModePaymentList( strParamName,  strParamStatus);
    }

    public HashMap OrderDAOgetModalityList(long intSpecificationId, String strEquipment, String strWarrant, String strEquipmentReturn) throws Exception,SQLException{
        return objOrderDAO.getModalityList(intSpecificationId,strEquipment,strWarrant,strEquipmentReturn);
    }

    public ArrayList ProductLineDAOgetProductLineValueList(int iProductLineId, String strMessage) throws SQLException, Exception{
        return objProductLineDAO.getProductLineValueList(iProductLineId,strMessage);
    }

    public ArrayList ProductDAOgetProductList(ProductBean productBean, String strMessage) throws SQLException, Exception{
        return objProductDAO.getProductList(productBean,strMessage);
    }

    public HashMap PlanDAOgetPlanList(PlanTarifarioBean planTarifarioBean, String type) throws SQLException, Exception{
        logger.info("******************************** INICIO SEJBOrderNewBean > PlanDAOgetPlanList ********************************");
        HashMap objResultado=new HashMap();
        try {
            Set<Integer> valuesFlagCoverage = new HashSet<Integer>(Arrays.asList( 1, 0));
            String strNpmodality=planTarifarioBean.getNpmodality();
            String strNpsolutionid=""+planTarifarioBean.getNpsolutionidbafi();
            String strNpproductlineid=""+planTarifarioBean.getNpproductlineid();
            long productId=planTarifarioBean.getNpproductid();
            int flagcoverage=planTarifarioBean.getFlagCoverage();
            int configbafi= objOrderDAO.validateConfigBafi2300(strNpmodality,strNpsolutionid,strNpproductlineid);

            logger.info("strNpmodality      : "+strNpmodality);
            logger.info("strNpsolutionid    : "+strNpsolutionid);
            logger.info("strNpproductlineid : "+strNpproductlineid);
            logger.info("productId          : "+productId);
            logger.info("flagcoverage       : "+flagcoverage);
            logger.info("configbafi         : "+configbafi);
            if(valuesFlagCoverage.contains(flagcoverage) && configbafi==1){
                ProductBean objProductBean=new ProductBean();
                objProductBean.setFlagCoverage(flagcoverage);
                objProductBean.setNpsolutionid(MiUtil.parseInt(strNpsolutionid));
                objProductBean.setNpmodality(strNpmodality);
                objProductBean.setNpproductid(productId);
                objProductBean.setNpproductlineid(MiUtil.parseLong(strNpproductlineid));
                objProductBean.setNpcategoryid(planTarifarioBean.getNpspecificationid());
                objResultado=objProductDAO.getProductPlanListBafi(objProductBean);
            }else{
                objResultado=objPlanDAO.getPlanList(planTarifarioBean,type);
            }

        }catch (Exception e){

        }

        logger.info("******************************** FIN SEJBOrderNewBean > PlanDAOgetPlanList ********************************");
        return objResultado;
    }

    public ArrayList EquipmentDAOgetProductList(String ownhandset, String consignmen, String strMessage) throws SQLException, Exception{
        return objEquipmentDAO.getProductList(ownhandset,consignmen,strMessage);
    }

    public String SpecificationDAOgetConsigmentValue(int intSpecificationID, String strMessage) throws SQLException, Exception{
        return objSpecificationDAO.getConsigmentValue(intSpecificationID,strMessage);
    }

    public ArrayList ServiceDAOgetServiceAllList(int intSolutionId, String strMessage) throws SQLException, Exception{
        return objServiceDAO.getServiceAllList(intSolutionId,strMessage);
    }
    
    public ArrayList ServiceDAOgetServiceDefaultList(String strObjectType, int intSpecificationId, String strMessage) throws SQLException, Exception{
        return objServiceDAO.getServiceDefaultList(strObjectType,intSpecificationId,strMessage);
    }
    
    public ArrayList OrderDAOgetSubCategoryList(String strCategory) throws Exception, SQLException{
        return objOrderDAO.getSubCategoryList(strCategory);
    }
    
    public ArrayList ItemDAOgetItemDeviceOrder(long intOrderId) throws SQLException{
        return objItemDAO.getItemDeviceOrder(intOrderId);
    }
    
    public HashMap ItemDAOgetItemOrder(long nporderid) throws Exception,SQLException{
        HashMap mapResult = new HashMap();
        ItemBean itemBean = null;
        ArrayList listDeviceBean = null;
        ItemDeviceBean itemDeviceBean = null;
        String cadNumberReserve = "";
        mapResult = objItemDAO.getItemOrder(nporderid);
        System.out.println("mapResult----->" + mapResult);
        if (mapResult!=null && mapResult.get("existNumberReserve")!=null && Constante.ANSWER_YES.equals(mapResult.get("existNumberReserve").toString())) {
           for (int i=0; i<((ArrayList)mapResult.get("objArrayList")).size();i++) {
              itemBean = (ItemBean)((ArrayList)mapResult.get("objArrayList")).get(i);
              if (Constante.PRODUCT_LINE_KIT_GOLDEN.equals(String.valueOf(itemBean.getNpproductlineid()))) {
                 listDeviceBean = objItemDAO.getItemDeviceOrder(nporderid);
                 cadNumberReserve = "";
                 for (int j=0; j<listDeviceBean.size();j++) {
                    itemDeviceBean = (ItemDeviceBean)listDeviceBean.get(j);
                    if (itemBean.getNpitemid()==itemDeviceBean.getNpitemid() && itemDeviceBean.getNpphone()!=null) {
                       cadNumberReserve = cadNumberReserve.concat(itemDeviceBean.getNpphone()).concat(",1,2,");
                    }
                 }
                 System.out.println("cadNumberReserve----->" + cadNumberReserve);
                 itemBean.setCadNumberReserve(cadNumberReserve);
              }
           }
        }
        return mapResult;
    }
    
    public String ItemDAOgetItemOrderDelete(ItemBean itemBean,Connection conn) throws Exception,SQLException{
        return objItemDAO.getItemOrderDelete(itemBean,conn);
    }
    
    public String ItemDAOgetItemUpdate(ItemBean itemBean,Connection conn) throws Exception,SQLException{
        return objItemDAO.doUpdateItem(itemBean,conn);
    }
    
    public ServiciosBean ServiceDAOgetServiceDescription(int intServicioId, String strMessage) throws SQLException, Exception{
        return objServiceDAO.getServiceDescription(intServicioId,strMessage);
    }
    
    public String ItemDAOgetItemImeiAssignementBADelete(ItemBean itemBean,Connection conn) throws Exception, SQLException{
        return objItemDAO.getItemImeiAssignementBADelete(itemBean,conn);
    }
    
    
    public HashMap OrderDAOgetOrderInsertar(OrderBean orderBean,Connection conn) throws Exception,SQLException{
        return objOrderDAO.getOrderInsertar(orderBean,conn);
    }
    
    public String ItemDAOgetItemInsertar(ItemBean itemBean,Connection conn) throws Exception,SQLException{
        return objItemDAO.doSaveItem(itemBean,conn);
    }
    
    public String ItemDAOgetItemServiceInsertar(ItemServiceBean itemServiceBean,Connection conn) throws Exception,SQLException{
        return objItemDAO.getItemServiceInsertar(itemServiceBean,conn);
    }
    
    public String ItemDAOgetItemInsertDevices(ItemDeviceBean itemDeviceBean,String strNextInbox ,Connection conn) throws Exception,SQLException{
        return objItemDAO.getItemInsertDevices(itemDeviceBean,strNextInbox,conn);
    }

    public HashMap SiteDAOgetSiteSolicitedList(long longNpOrderId) throws Exception, SQLException{
        return objSiteDAO.getSiteSolicitedList(longNpOrderId);
    }
    //CEM se agrego el parametro: strObjectType
    public HashMap SiteDAOgetSiteExistsList(long longNpCustomerId,String strObjectType) throws Exception, SQLException {
        return objSiteDAO.getSiteExistsList(longNpCustomerId,strObjectType);
    }
    
    public HashMap SpecificationDAOgetSpecificationUserList(long customerId, String strLogin, String strGeneratorType, String strOpportunityTypeId, String strFlagGenerator, long lngDivisionId, long lngSpecificationId, long lngGeneratorId) throws Exception, SQLException {
        return objSpecificationDAO.getSpecificationUserList(customerId,  strLogin,  strGeneratorType,  strOpportunityTypeId,  strFlagGenerator,  lngDivisionId,  lngSpecificationId, lngGeneratorId);
    }
    
    public HashMap SpecificationDAOgetSpecificationUserList(long userId, long lngDivisionId, long lngSpecificationId, String strObjectType, String strFlagGenerator  ) throws Exception, SQLException {
        return objSpecificationDAO.getSpecificationUserList(userId,lngDivisionId,lngSpecificationId,strObjectType,strFlagGenerator);
    }
    
    public HashMap OrderDAOgetAddendasList(int id_prom, int id_plan, int id_specification, int id_kit) throws SQLException,Exception{
        return objOrderDAO.getAddendasList(id_prom, id_plan, id_specification, id_kit);
    } 

    public String OrderDAOgetNpAllowAdenda(int id_specification) throws SQLException,Exception{
        return objOrderDAO.getNpAllowAdenda(id_specification);
    } 
        
    public String OrderDAOgetValidationAdenda(int id_specification) throws SQLException,Exception{

        return objOrderDAO.getAdendaValidation(id_specification);
    }
        
    public HashMap OrderDAOgetTemplateOrder(int id_order, int id_item) throws SQLException,Exception{
        return objOrderDAO.getTemplateOrder(id_order, id_item);
    }
    
    public HashMap OrderDAOgetProductPriceType(ProductBean objProductBean) throws SQLException,Exception{
        return objProductDAO.getProductPriceList(objProductBean);
    }
    
    public HashMap OrderDAOgetNumAddendumAct(int id_customer, String id_num_nextel) throws SQLException,Exception{
        return objOrderDAO.getNumAddendumAct(id_customer, id_num_nextel);
    }    
    
    public HashMap OrderDAOgetNumAddendumActSpec(int id_customer, String id_num_nextel, String id_specification) throws SQLException,Exception{
        System.out.println("[SEJBOrderNewBean.java.OrderDAOgetNumAddendumActSpec]: Ejecutando este metodo");
        return objOrderDAO.getNumAddendumActSpec(id_customer, id_num_nextel, id_specification);
    }

    /*JPEREZ: Excepciones - Inicio*/
   public String doDeleteOrderPeriod(RequestHashMap request, Connection conn) throws SQLException,Exception{      
      String strOrderId      = "";
      String strMessage      = null;
      strOrderId       =   (String)request.get("hdnNumeroOrder");
      strMessage = objExceptionDAO.deleteOrderPeriod(Long.parseLong(strOrderId), conn);
      return strMessage;   
   }
      
   public String doSaveOrderPeriod(RequestHashMap request, Connection conn) throws SQLException,Exception{      
      String strBeginPeriods = "", 
             strEndPeriods   = "",
             strOrderId      = "",
             strCreatedBy    = "",
             strMessage      = null;
         
        
      strBeginPeriods  =   (String)request.get("hdnPeriodoIni");
      strEndPeriods    =   (String)request.get("hdnPeriodoFin");
      strOrderId       =   (String)request.get("hdnNumeroOrder");
      strCreatedBy     =   (String)request.get("hdnSessionLogin");      
      if (strBeginPeriods!=null){
         System.out.println("strBeginPeriods === "+strBeginPeriods);
         StringTokenizer tkBeginPeriod   = new StringTokenizer(strBeginPeriods,"|");
         StringTokenizer tkEndPeriod      = new StringTokenizer(strEndPeriods,"|");
        
         while (tkBeginPeriod.hasMoreTokens()) {
            String strBeginPeriod = tkBeginPeriod.nextToken();
            String strEndPeriod   = tkEndPeriod.nextToken();        
            System.out.println("strBeginPeriod === "+strBeginPeriod);
            strMessage = objExceptionDAO.insertOrderPeriod(Long.parseLong(strOrderId), strBeginPeriod, strEndPeriod, strCreatedBy, conn );            
            if (strMessage!=null){
              System.out.println("===========strMessage "+strMessage);    
              break;                 
            }
               
         }         
      }             
      return strMessage;                
   }

  public HashMap ProductLineDAOgetProductLineSpecList(long longSolutionId, long longSpecificationId, String strObjectType, long longProductLineId) throws Exception, SQLException{
    return objProductLineDAO.getProductLineSpecList(longSolutionId,longSpecificationId, strObjectType, longProductLineId);
  }
  
  public HashMap ProductDAOgetDetailByPhone(String strPhoneNumber,long longCustomerId,long longSiteId,long lSepecificationId) throws Exception, SQLException{
    return objProductDAO.getDetailByPhone(strPhoneNumber,longCustomerId,longSiteId,lSepecificationId);
  }

  public HashMap ProductDAOgetDetailByPhoneBySpecification(String strPhoneNumber,long longCustomerId,long longSiteId,long lSpecificationId,long lOrderId) throws Exception, SQLException{
    return objProductDAO.getDetailByPhoneBySpecification(strPhoneNumber,longCustomerId,longSiteId,lSpecificationId,lOrderId);
  }
   
  public HashMap ProductDAOgetProductDetailImei(String strImei, long longCustomerId, long lSpecificationId) throws Exception, SQLException{
		return objProductDAO.getProductDetailImei(strImei,longCustomerId,lSpecificationId);
	}
  
    //[TDECONV003-1] EFLORES Se agrega un nuevo parametro
  public HashMap ProductDAOgetDetailByImeiBySpecification(String strImei,long longCustomerId,long lSpecificationId,String strModalitySell, String strFlagMigration) throws Exception, SQLException{
    return objProductDAO.getDetailByImeiBySpecification(strImei,longCustomerId,lSpecificationId,strModalitySell,strFlagMigration);//[TDECONV003-1]
  }  
  
  public HashMap getResponsibleAreaList() throws Exception, SQLException{
    return objOrderDAO.getResponsibleAreaList();
  }
 //Inicio CEM COR0323
  public HashMap ItemDAOhasPaymentOrderId (long nporderid)  throws Exception, SQLException{
    return objItemDAO.hasPaymentOrderId(nporderid);
  }
  //Fin CEM COR0323
    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - INICIO
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 28/08/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
	 
	public HashMap getRepairReplaceList(String strRepairId) throws SQLException, Exception {
		return objRepairDAO.getRepairReplaceList(strRepairId);
	}
	
	public HashMap generateDocument(HashMap hshParametrosMap) throws SQLException, Exception {
		//Connection conn = Proveedor.getConnection();
		return objRepairDAO.generateDocument(hshParametrosMap, null);
	}

	public HashMap reportRepair(String strReportName, long lRepairId) throws SQLException, Exception {
		return objRepairDAO.reportRepair(strReportName, lRepairId);
	}
	
	public HashMap getLastImeiRepair(long lRepairId) throws SQLException, Exception {
		return objRepairDAO.getLastImeiRepair(lRepairId);
	}

  public HashMap getResponsibleDevList() throws Exception, SQLException{
    return objOrderDAO.getResponsibleDevList();
  }

  public HashMap getProductType(ProductBean objProductBean) throws  Exception, SQLException{
    HashMap objMapaResultado = null;
    if (objProductBean!=null && objProductBean.getNpproductid()>0.0 && (Constante.PRODUCT_LINE_KIT_GAR_EXT.equals(String.valueOf(objProductBean.getNpproductlineid()))
        || Constante.PRODUCT_LINE_KIT_GOLDEN.equals(String.valueOf(objProductBean.getNpproductlineid())))) {
      objMapaResultado = objProductDAO.getProductListByModelId(objProductBean);
      
    } else if (objProductBean!=null && objProductBean.getNpplanid()>0.0 && objProductBean.getNpcategoryid()!=2009) {//EZUBIAURR Se agrega condición para categoria de cambio de modelo (2009)
      objMapaResultado = objProductDAO.getProductListByModelId(objProductBean);
        }else  if(objProductBean != null && (objProductBean.getNpcategoryid() == Constante.SPEC_BOLSA_CREAR || objProductBean.getNpcategoryid() == Constante.SPEC_BOLSA_DOWNGRADE || objProductBean.getNpcategoryid() == Constante.SPEC_BOLSA_UPGRADE)){ // ADT-BCL-083 - LHUAPAYA
            objMapaResultado = objProductDAO.getProductBCL(objProductBean);
    } else {
      objMapaResultado = objProductDAO.getProductList(objProductBean);
    }
    return objMapaResultado;
  }

  public HashMap getProductBolsa(long lngCustomerId,long lngSiteId) throws Exception, SQLException{
    return objProductDAO.getProductBolsa(lngCustomerId,lngSiteId);
  }
  
    public HashMap getBolsaCreacionN2(long lngCustomerId,long lngSiteId) throws Exception, SQLException{
    return objProductDAO.getBolsaCreacionN2(lngCustomerId,lngSiteId);
  }
  
  public HashMap getProductDetail(long longProductId) throws Exception, SQLException{
    return objProductDAO.getProductDetail(longProductId);
  }

  public HashMap getSpecificationDate(long lngSpecificationId, String strBillcycle) throws Exception, SQLException{
    return objOrderDAO.getSpecificationDate(lngSpecificationId,strBillcycle);
  }

  public HashMap getFlagEmail(long lngSpecificationId, long lngHdnIUserId) throws Exception, SQLException{//jtorresc 09/12/2011
    return objOrderDAO.getFlagEmail(lngSpecificationId,lngHdnIUserId);
  }

  public HashMap getTableValue(String strNameTable) throws Exception, SQLException{
    return objProductDAO.getTableValue(strNameTable);
  }

	/**
	 * @see pe.com.nextel.dao.ItemDAO#doValidateIMEI(HashMap, Connection)
	 */
	public String doValidateIMEI(HashMap hshItemDeviceMap) throws SQLException, Exception {
		return objItemDAO.doValidateIMEI(hshItemDeviceMap, null);	
	}
	
	/**
	 * @see pe.com.nextel.dao.ItemDAO#getInboxGenerateGuide(Connection)
	 */
	public HashMap getInboxGenerateGuide() throws SQLException, Exception {
		return objItemDAO.getInboxGenerateGuide(null);
	}

  public HashMap getServiceRentList(long lngPlanId) throws Exception, SQLException{
    return objServiceDAO.getServiceRentList(lngPlanId);
  }
  
  public HashMap getModelList() throws Exception,SQLException{
    return objProductDAO.getModelList();
  } 
  
  public HashMap getModelListByCategory (int specId) throws Exception,SQLException {
      return objProductDAO.getModelListByCategory(specId);
	 }
  

  public HashMap getProductLineDetail(long lngProductLineId) throws Exception, SQLException{
    return objProductLineDAO.getProductLineDetail(lngProductLineId);
  }
 
    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - FIN
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 28/08/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
     
    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - INICIO
     *  REALIZADO POR: Carmen Gremios
     *  FECHA: 13/02/2008
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
     
   public HashMap doValidateIMEI(String strIMEI)
   throws Exception {      
      return objItemDAO.doValidateIMEI(strIMEI);
   }          
   
    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - FIN
     *  REALIZADO POR: Carmen Gremios
     *  FECHA: 13/02/2008
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
    
    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
      *  CAP & CAL INICIO
      *  REALIZADO POR: CPUENTE
      *  FECHA: 28/08/2009
      ***********************************************************************
      ***********************************************************************
      ***********************************************************************/
      
     public HashMap doValidateOwnEquipment(String strIMEI)
     throws Exception {
       return objItemDAO.doValidateOwnEquipment(strIMEI);
     }
     
     /***********************************************************************
      ***********************************************************************
      ***********************************************************************
      *  CAP & CAL FIN
      *  REALIZADO POR: CPUENTE
      *  FECHA: 28/08/2009
      ***********************************************************************
      ***********************************************************************
      ***********************************************************************/
     
    
    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  VALIDACION DE STOCK - INICIO
     *  REALIZADO POR: Jorge Pérez
     *  FECHA: 03/03/2008
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
     
     /**
      * @see pe.com.nextel.dao.ProductDAO#getValidateStock(int idSpecification)
     */
     public HashMap getValidateStock(int idSpecification, int iDispatchPlace) throws Exception, SQLException{
       return objProductDAO.getValidateStock(idSpecification, iDispatchPlace);
     }
     
     /**
      * @see pe.com.nextel.dao.ProductDAO#getStockMessage(int idSpecification, long lProductId, int iBuildingid, String strSaleModality, long lSalesStructOrigenId, String strTipo)
     */     
     public HashMap getStockMessage(int idSpecification, long lProductId, int iBuildingid, String strSaleModality,long lSalesStructOrigenId, String strTipo)
     throws Exception, SQLException{
       return objProductDAO.getStockMessage(idSpecification,lProductId,iBuildingid,strSaleModality,lSalesStructOrigenId, strTipo);
     }


    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  VALIDACION DE STOCK - FIN
     *  REALIZADO POR: Jorge Pérez
     *  FECHA: 03/03/2008
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/ 
     
     /**
      * @see pe.com.nextel.dao.ItemDAO#doValidateSIM(String strSIM)
     */
     public HashMap doValidateSIM(String strSIM) throws Exception, SQLException{
       return objItemDAO.doValidateSIM(strSIM);
     } 
     

  public long getUnkownSiteIdByOportunity(long lngOportunityId) throws Exception, SQLException
  {
    return objOrderDAO.getUnkownSiteIdByOportunity(lngOportunityId);
  }
  
  public HashMap getCustSiteIdByOportunity(long lngOportunityId) throws Exception, SQLException{
    return objOrderDAO.getCustSiteIdByOportunity(lngOportunityId);
  }
    
   /**
    * @see pe.com.nextel.dao.OrderDAO#getValidateFechaFirma(int idSpecification)
    * @autor Opdubock
   */
   public HashMap getValidateFechaFirma(long lngOrderId, String strFechaFirma) throws Exception, SQLException{
     return objOrderDAO.getValidateFechaFirma(lngOrderId,strFechaFirma);
   }
	 public HashMap generateDocumentRepair(HashMap hshParametrosMap) throws SQLException, Exception {
		 //Connection conn = ds.getConnection();
		 return objRepairDAO.generateDocumentRepair(hshParametrosMap);
	 }       
   public HashMap getTipoPlantillaAdenda(String strNumeroNextel, int iTemplateId) throws Exception, SQLException{
     return objItemDAO.getTipoPlantillaAdenda(strNumeroNextel,iTemplateId);
   }    
   
   /**
	 * @see pe.com.nextel.dao.OrderDAO#getValidateAdministrator(HashMap, Connection)
	 */
	public String getValidateAdministrator(long lOrderId) throws SQLException, Exception {
		return objOrderDAO.getValidateAdministrator(lOrderId);	
	}

  public HashMap getRepairByOrder(long lOrderId) throws  Exception, SQLException{
    return objRepairDAO.getRepairByOrder(lOrderId);
  }

  public HashMap getCodEquipFromImei(String strImei) throws  Exception, SQLException{
    return objRepairDAO.getCodEquipFromImei(strImei);
  }

  public HashMap getAllowedSpecification(long lspecificationId, long lcustomerid) throws  Exception, SQLException{
    return objOrderDAO.getAllowedSpecification(lspecificationId, lcustomerid);
  }

  public String valImeiPrestamoCambio(RepairBean objRepairBean,HashMap hshParametrosMap) throws  Exception, SQLException{
    return objRepairDAO.valImeiPrestamoCambio(objRepairBean, hshParametrosMap);
  }

  // PCASTILLO - Despacho en Tienda - Validación de Stock
  public HashMap valStockPrestCambio(RepairBean objRepairBean,HashMap hshParametrosMap) throws  Exception, SQLException{
    return objRepairDAO.valStockPrestCambio(objRepairBean, hshParametrosMap);
  }


  /**
   * Motivo : Cuenta las notas registrada para una determinada orden
   * @see pe.com.nextel.dao.OrderDAO#getNoteCount(long lOrderId)
   * @autor JOyola
   * @return HashMap
   * @param lOrderId
   */
  public HashMap getNoteCount(long lOrderId) throws  Exception, SQLException{
    return objOrderDAO.getNoteCount(lOrderId);
  }

  public HashMap doValidateMassiveSim(long lngCustomerId, long lngSpecificationId, String strModality, String[] strSim) throws  Exception, SQLException{
    return objEquipmentDAO.doValidateMassiveSim(lngCustomerId, lngSpecificationId, strModality, strSim);
  }
  
  
  /**
   * Motivo : Cuenta las notas registrada para una determinada orden
   * @see pe.com.nextel.dao.OrderDAO#getNoteCount(long lOrderId)
   * @autor KSalvador
   * @return HashMap
   * @param lOrderId
   */
   public String insIncidentWeb(RequestHashMap request, Connection conn) throws SQLException,Exception{      
      
        String  strOrderId ="",
                strCreatedby = "",
                strnpUserId  = "",
                strnpbuildingiId = "",
                strnpAppId = "";    
      String strMessage      = null;
      
      strOrderId       =   (String)request.get("hdnNumeroOrder");
      strnpUserId      =   (String)request.getParameter("hdnSessionUserid");
      strCreatedby     =   (String)request.getParameter("hdnSessionLogin");
      strnpAppId       =   (String)request.getParameter("hdnSessionAppid");
      strnpbuildingiId =   (String)request.getParameter("hdnBuildingId");
           
           
      strMessage = objOrderDAO.insIncidentWeb(Long.parseLong(strOrderId),Long.parseLong(strnpUserId),strCreatedby,Long.parseLong(strnpAppId),
                                              Long.parseLong(strnpbuildingiId),conn);
      return strMessage;   
   }
   
   
   /**
   * Motivo : Registra las incidencias para el modulo de Masivos. 
   * @autor HRengifo
   * @return String
   * @param request
   * @param conn
   */
   public String insertIncidentMassive(RequestHashMap request, Connection conn) throws SQLException,Exception{      
      
        String strMessage   = null;
        long nCustomerid    = Long.parseLong((String)request.getParameter("txtCompanyId")==null?"0":(String)request.getParameter("txtCompanyId"));
        long nSpecification = Long.parseLong((String)request.getParameter("cmbSubCategoria")==null?"0":(String)request.getParameter("cmbSubCategoria"));
        long nOrderId       = Long.parseLong((String)request.getParameter("hdnNumeroOrder")==null?"0":(String)request.getParameter("hdnNumeroOrder"));
        long nUserId        = Long.parseLong((String)request.getParameter("hdnSessionUserid")==null?"0":(String)request.getParameter("hdnSessionUserid"));
        String strLogin     = (String)request.getParameter("hdnSessionLogin");
        
        strMessage = objMassiveOrderDAO.insertIncidentMassive (nCustomerid, nSpecification, nOrderId, nUserId, strLogin,conn);
     
        return strMessage;   
   }
   
   
   /**
   * Motivo : VALIDACION DE COMISSION
   * @see pe.com.nextel.dao.ProductDAO#getComissionMessage(int intServicioId)
   * REALIZADO POR: Ruth Polo
   * FECHA: 13/01/2009
   */ 
     public HashMap getComissionMessage(int intServicioId)
     throws Exception, SQLException{
       return objProductDAO.getComissionMessage(intServicioId);
     }
     
 /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
 * <br>Fecha: 22/04/2009
 * @see pe.com.nextel.dao.SpecificationDAO#getSolutionSpecificationList(long)      
 ************************************************************************************************************************************/ 
  public HashMap getSolutionSpecificationList(long lngSpecificationId, long lnDivisionId, long lngSiteId) throws  Exception, SQLException{
    return objSpecificationDAO.getSolutionSpecificationList(lngSpecificationId,lnDivisionId,lngSiteId);
  }
         
 /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
 * <br>Fecha: 22/04/2009
 * @see pe.com.nextel.dao.SpecificationDAO#getSolutionSpecificationList(long)      
 ************************************************************************************************************************************/ 
  public HashMap getSolutionType() throws  Exception, SQLException{
    return objSpecificationDAO.getSolutionType();
  }
  /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:cesar.barzola@nextel.com.pe">Cesar Barzola</a>
 * <br>Fecha: 01/07/2009   
 * @see pe.com.nextel.dao.SpecificationDAO#getSolutionSpecificationList(long)
 ************************************************************************************************************************************/ 
  public HashMap OrderValidations(long lUserId, OrderBean objOrder, Connection conn)throws Exception, SQLException
{
     // lUserId=218198;
      HashMap hshResultValidation= new HashMap();
      //CBARZOLA- validacion que el usuario coordinador o supervisor tenga  necesariamente una propuesta
      HashMap hshResultCoord=objGeneralDAO.validateUserRol(lUserId,6,"I");
      if (hshResultCoord.get(Constante.MESSAGE_OUTPUT)!=null){
        hshResultValidation.put("strMessage",(String)hshResultCoord.get("strMessage"));
      }
      HashMap hshResultSupervisor=objGeneralDAO.validateUserRol(lUserId,7,"I");
      if (hshResultSupervisor.get(Constante.MESSAGE_OUTPUT)!=null){
        hshResultValidation.put("strMessage",(String)hshResultSupervisor.get("strMessage"));
      }
      int intresprolcoord= MiUtil.parseInt((String)hshResultCoord.get("respuesta"));
      System.out.println("intresprolcoord"+intresprolcoord);
      int intresprolsuperv= MiUtil.parseInt((String)hshResultSupervisor.get("respuesta"));
       System.out.println("intresprolsuperv"+intresprolsuperv);
      System.out.println("objOrder.getNpproposedid()"+objOrder.getNpproposedid());
      
      String strGeneratorType= objOrder.getNpGeneratorType();
      if(  (!strGeneratorType.equals(Constante.GENERATOR_TYPE_OPP)) && (intresprolcoord > 0 || intresprolsuperv > 0) )
      {
       if  (objOrder.getNpproposedid()==0)
       {
          hshResultValidation.put("strMessage","Debe seleccionar una propuesta"); 
       }
      }
      //CBARZOLA- fin de validacion
        //CBARZOLA Valida si el cliente es fraudulento
        HashMap hshResultValidationFraud=objFraudDAO.getVerificationFraudOrder( objOrder.getNpCustomerId(),objOrder.getNpSpecificationId(),objOrder.getNpType(),
                                                                                objOrder.getNpSpecification(),objOrder.getNpCreatedBy(),conn);                                                                      
        
        if((String)hshResultValidationFraud.get("strMessage")!=null){// EN CASO EXISTA ALGUN ERROR EN LA CONSULTA
            if(conn !=null)conn.rollback();
            hshResultValidation.put("strMessage",(String)hshResultValidationFraud.get("strMessage"));        
        }
        else
        {     
          conn.commit();
          String respValidation=(String)hshResultValidationFraud.get("flagFraud");
            if(respValidation.equals("S")) {// EN CASO DE QUE LA PERSONA SEA FRAUDULENTA(BlackList)              
               hshResultValidation.put("strMessage",(String)hshResultValidationFraud.get("strMessageFraud"));
             }               
        } 
        // fin - evaluacion si el cliente es fraudulento.
    return hshResultValidation;

}


/***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:jose.casas@nextel.com.pe">José Casas</a>
 * <br>Fecha: 16/06/2009
 * @see pe.com.nextel.ejb.SEJBOrderNewBean#getFistStatus(long)      
 ************************************************************************************************************************************/ 
  public HashMap getFistStatus(RequestHashMap objHashMap){
    String strMessage = null;
    HashMap hshFirstStatus = new HashMap();    
    try {
      //Obtener la conexion del pool de conexiones (DataSource)
      Connection conn = Proveedor.getConnection();      
      //Desactivar el commit automatico de la conexion obtenida
      conn.setAutoCommit(false);
      try {
        hshFirstStatus = objOrderDAO.getFistStatus((String)objHashMap.getParameter("hdnNumeroOrder"),(String)objHashMap.getParameter("txtCompanyId"),(String)objHashMap.getParameter("hdnSpecification"),(String)objHashMap.getParameter("cmbCreditAction"),(String)objHashMap.getParameter("hdnOrigenType"),(String)objHashMap.getParameter("hdnSessionLogin"),(String)objHashMap.getParameter("hdnGeneratorId"),conn);        
        System.out.println("SEJBOrderNewBean/getFistStatus/strMessage/->"+(String)hshFirstStatus.get("strMessage"));
        if( ((String)hshFirstStatus.get("strMessage"))!=null){
          if (conn != null) conn.rollback();
          return hshFirstStatus;
        }
        //Confirmar la transaccion en la BD
        conn.commit();        
        return hshFirstStatus;
      } catch (Exception e) {
        //Si existe error, deshacer los cambios en la BD
        if (conn != null) conn.rollback();
        e.printStackTrace();
        strMessage  = MiUtil.getMessageClean("[Exception][doSaveOrder]["+e.getClass() + " " + e.getMessage()+ " - Caused by " + e.getCause() + "]");
        hshFirstStatus.put("strMessage",strMessage);
        return hshFirstStatus;
      } 
      finally {
        //Finalmente, pase lo que pase, cerrar la conexion
        if (conn != null){
          conn.close();
          conn = null;
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      strMessage  = MiUtil.getMessageClean("[Exception][doSaveOrder]["+ex.getClass() + " " + ex.getMessage()+"]");
      hshFirstStatus.put("strMessage",strMessage);
      return hshFirstStatus;
    }
  }

 /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:rensso.martinez@nextel.com.pe">Rensso Martinezr</a>
 * <br>Motivo: Valida los que un contrato no este suspendido mas de 60 días.
 * <br>Fecha: 24/06/2009
 * @see pe.com.nextel.dao.ProductDAO#validaDiasSuspension(String, String, String)      
 ************************************************************************************************************************************/      
  public HashMap ProductDAOvalidaDiasSuspension(String strPhoneNumber, long lSpecificationId, String strNpScheduleDate, String strNpScheduleDate2) throws Exception, SQLException{
    return objProductDAO.validaDiasSuspension(strPhoneNumber,lSpecificationId,strNpScheduleDate,strNpScheduleDate2);
  }
  
   /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:rensso.martinez@nextel.com.pe">Rensso Martinez</a>
 * <br>Motivo: Obtiene el listado de los Estados del Item para las Suspensiones definitivas.
 * <br>Fecha: 28/06/2009
 * @see pe.com.nextel.dao.ItemDAO#getStatusItemList(String)      
 ************************************************************************************************************************************/    
  public HashMap ItemDAOgetStatusItemList(String nameTable, String nptag1, String nptag2) throws SQLException, Exception  {
        return objItemDAO.getStatusItemList(nameTable, nptag1, nptag2);
  }
  
/***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
 * <br>Fecha: 22/04/2009
 * @see pe.com.nextel.dao.ServiceDAO#getProductServiceDefaultList(long,int,int,int,int)      
 ************************************************************************************************************************************/ 
  public HashMap getProductServiceDefaultList (long lspecificationid,int iProductId,int iplanId,int iPermission_alq, int iPermission_msj) throws  Exception, SQLException{
    return objServiceDAO.getProductServiceDefaultList(lspecificationid,iProductId,iplanId,iPermission_alq,iPermission_msj);
  }
  /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:cesar.barzola@nextel.com.pe">Cesar Barzola</a>
 * <br>Fecha: 19/07/2009
 * @see pe.com.nextel.dao.OrderDAO#getOthersSolutionsbySubMarket(long,long)      
 ************************************************************************************************************************************/ 
  public HashMap getOthersSolutionsbySubMarket(long lspecificationId, long lsolutionId) throws  SQLException,Exception
  {
    return objOrderDAO.getOthersSolutionsbySubMarket(lspecificationId,lsolutionId);
  }
  
 /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:reinhard.arana@nextel.com.pe">Reinhard Arana</a>
 * <br>Fecha: 21/07/2009
 * @see pe.com.nextel.dao.ProductDAO#getProductBolsa(long,long,long)      
 ************************************************************************************************************************************/ 
  public HashMap getProductBolsa(long lngCustomerId,long lngSiteId, long lngSolutionId) throws Exception, SQLException{
    return objProductDAO.getProductBolsa(lngCustomerId,lngSiteId,lngSolutionId);
  } 
/***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:cesar.barzola@nextel.com.pe">Cesar Barzola</a>
 * <br>Fecha: 21/07/2009
 * @see pe.com.nextel.dao.ProposedDAO#getProposedList(Long,Long,Long,Long)      
/************************************************************************************************************************************/
public  HashMap  getProposedList(long lCustomerId,long lSite,long lSpecificationId,long lSellerId)throws  SQLException,Exception
 {
    return objProposedDAO.getProposedList(lCustomerId,lSite,lSpecificationId,lSellerId);
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
 * <br>Realizado por: <a href="mailto:cesar.barzola@nextel.com.pe">Cesar Barzola</a>
 * <br>Fecha: 12/07/2009
 * @see pe.com.nextel.dao.ServiceDAO#getCoreService_by_Plan(long)      
 ************************************************************************************************************************************/ 
public HashMap ServiceDAOgetCoreServicebyPlan(long lplanId) throws Exception, SQLException{
        return objServiceDAO.getCoreService_by_Plan(lplanId);
    }


  public HashMap agreementValidations(RequestHashMap request, OrderBean objOrder, Connection conn)throws Exception, SQLException{

      String[]  price_type    = request.getParameterValues("txtItemPriceType");
      //String[]  price_type_id = request.getParameterValues("txtItemPriceTypeId");
      String[]  price_type_id = request.getParameterValues("txtItemPriceTypeItemId");
      String[]  quantity      = request.getParameterValues("txtItemQuantity");
      ArrayList arrListItem   = new ArrayList();
      HashMap hshResultValidation = new HashMap();
      int count = 0;
      String aux_type;
      String aux_typeid;
      
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
                  System.out.println("item_type_count : " + MiUtil.getString(count));
                  System.out.println("item_type_id : " + typeId);
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
            System.out.println("item_type_count : " + MiUtil.getString(count));
            System.out.println("item_type_id : " + typeId);
            arrListItem.add(objHashMap);
          }
        }
      
        hshResultValidation = objAgreementDAO.validateAgreement(arrListItem,objOrder,conn);
      }
      return hshResultValidation;
  }
  
  /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:percy.hidalgo@nextel.com.pe">Percy Hidalgo</a>
 * <br>Fecha: 15/04/2010   
 ************************************************************************************************************************************/    
 public HashMap getOriginalPlan(PlanTarifarioBean planTarifarioBean) throws SQLException, Exception{
        return objPlanDAO.getOriginalPlan(planTarifarioBean);
    }

/***********************************************************************************************************************************
   * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
   * <br>Fecha: 28/09/2010   
  ************************************************************************************************************************************/ 
  public HashMap getProductModelList(ProductBean objProductBean) throws  SQLException, Exception {
    return objProductDAO.getProductModelList(objProductBean);
  }

/***********************************************************************************************************************************
   * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
   * <br>Fecha: 08/10/2010   
  ************************************************************************************************************************************/ 
  public HashMap getProductPlanList(ProductBean objProductBean) throws  SQLException, Exception {
      logger.info("******************************** INICIO SEJBOrderNewBean > getProductPlanList ********************************");
      HashMap objResultado=new HashMap();
      try{
          Set<Integer> valuesFlagCoverage = new HashSet<Integer>(Arrays.asList( 1, 0));
          String strNpmodality=objProductBean.getNpmodality();
          String strNpsolutionid=""+objProductBean.getNpsolutionid();
          String strNpproductlineid=""+objProductBean.getNpproductlineid();
          int flagcoverage=objProductBean.getFlagCoverage();
          int configbafi= objOrderDAO.validateConfigBafi2300(strNpmodality,strNpsolutionid,strNpproductlineid);

          logger.info("strNpmodality      : "+strNpmodality);
          logger.info("strNpsolutionid    : "+strNpsolutionid);
          logger.info("strNpproductlineid : "+strNpproductlineid);
          logger.info("flagcoverage       : "+flagcoverage);
          logger.info("configbafi         : "+configbafi);

          if(valuesFlagCoverage.contains(flagcoverage) && configbafi==1){
              objResultado=objProductDAO.getProductPlanListBafi(objProductBean);
          }else{
              objResultado=objProductDAO.getProductPlanList(objProductBean);
          }

      }catch(Exception e){
          logger.error("Error getProductPlanList: ",e);
      }
      logger.info("******************************** FIN SEJBOrderNewBean > getProductPlanList ********************************");
    return objResultado;
  }

/***********************************************************************************************************************************
   * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
   * <br>Fecha: 08/10/2010   
  ************************************************************************************************************************************/ 
  public HashMap getValidateServSelectedList(String strServices, String strServicesDesc, String strPlanId, String strProduct) throws  Exception, SQLException {//EZUBIAURR 28/02/11
    HashMap objResultado=new HashMap();
    HashMap objServicebyPlan=null;
    HashMap objServicebyProduct=null;
    
    objServicebyPlan = objServiceDAO.getValidateServListByPlan(strServices, strPlanId);
    objServicebyProduct = objServiceDAO.getValidateServListByProduct(strServices, strServicesDesc, strPlanId, strProduct);
    //EZM 23/12/10 Compatibilidad Modelo-Plan-Servicio (FindBugs Correction)
    if (objServicebyPlan!=null && ((ArrayList)objServicebyPlan.get("objServiciosBeanList")).size()>0) {
      objServicebyPlan.put("strMessageValid","Existen Servicios no compatibles con el Plan:");
      objResultado.put("objServicebyPlan",objServicebyPlan);
    }
    
    if (objServicebyProduct!=null && ((ArrayList)objServicebyProduct.get("objServiciosBeanList")).size()>0) {
      objServicebyProduct.put("strMessageValid","Existen Servicios no compatibles con el Producto");
      objResultado.put("objServicebyProduct",objServicebyProduct);
    } 
    System.out.println("----------------->1");
    return objResultado;
  }

  /***********************************************************************************************************************************
   * <br>Realizado por: <a href="mailto:ronald.huacani@asistp.com">Ronald Huacani</a>
   * <br>Fecha: 15/10/2010  
 ************************************************************************************************************************************/ 
  public HashMap getNumberGolden(String sCodApp, long lDnType, long lNpcode, String sDnNum, long lTmCode, String sExcluded, String sQuantity, String sPortabilidad) throws   SQLException, Exception {
    return objItemDAO.getNumberGolden(sCodApp, lDnType, lNpcode, sDnNum, lTmCode, sExcluded, sQuantity, sPortabilidad);
  }
  
  /***********************************************************************************************************************************
   * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
   * <br>Fecha: 15/11/2010  
 ************************************************************************************************************************************/ 
  public HashMap transferExtendedGuarantee(String strImei,String strImeiNuevo) throws   SQLException, Exception {
    return objItemDAO.transferExtendedGuarantee(strImei, strImeiNuevo);
  }
  
  /***********************************************************************************************************************************
  * <br>Realizado por: <a href="mailto:cesar.lozza@hp.com">César Lozza</a>
  * <br>Fecha: 09/11/2010   
  ************************************************************************************************************************************/    
  public HashMap evaluateOrderVolume(int customerId, int specificationId, String typeWindow, List itemsList) throws Exception, SQLException  {
    return objOrderDAO.evaluateOrderVolume(customerId, specificationId, typeWindow, itemsList);
  }
  
  /***********************************************************************************************************************************
   * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
   * <br>Fecha: 15/11/2010  
 ************************************************************************************************************************************/ 
  public HashMap reserveGoldenNumber(RequestHashMap objHashMap, Connection conn, int intSpecificationId) throws  SQLException, Exception {
    HashMap hshResultReserve= new HashMap();
    HashMap objResource = null;
    String[]  pv_item_Ptn_Id = null;
    String[]  modality = null;
    boolean esVenta=false;
    modality = objHashMap.getParameterValues("txtItemModality");
    for (int j = 0; j < modality.length; j++) {
       if (Constante.MODALITY_VENTA.equals(modality[j])) {
          esVenta = true;
          break;
       }
    }
    //System.out.println("modality---->" + modality);
    if (esVenta) {
    
    ArrayList list = new ArrayList();
    System.out.println("el objHashMap es " + objHashMap);
    if (intSpecificationId==2011) {
       pv_item_Ptn_Id = objHashMap.getParameterValues("item_ptnId");
       String[]  pv_item_New_Number = objHashMap.getParameterValues("txtItemNewNumber");
          for( int i = 0; i < pv_item_Ptn_Id.length; i++ ){
             if (!"".equals(pv_item_Ptn_Id[i].trim()) && !"undefined".equals(pv_item_Ptn_Id[i].trim())) {   
                objResource = new HashMap();
                objResource.put("wn_ptn_dn_id",MiUtil.getStringObject(pv_item_Ptn_Id, i));
                objResource.put("wv_ptn_dn_num",MiUtil.getStringObject(pv_item_New_Number, i));
                objResource.put("wv_status","f");
                list.add(objResource);
             }      
          }
    }  else {
       pv_item_Ptn_Id = objHashMap.getParameterValues("hidden_imei_ptnid");
       String[]  pv_item_Ufmi_Id = objHashMap.getParameterValues("hidden_imei_ufmiid");
       String[]  pv_item_Num_Tel = objHashMap.getParameterValues("hdnNumTel");
       for( int i = 0; i < pv_item_Num_Tel.length; i++ ){
             if (!"".equals(pv_item_Ptn_Id[i].trim()) && !"undefined".equals(pv_item_Ptn_Id[i].trim())) {          
               objResource = new HashMap();
               objResource.put("wn_ptn_dn_id",MiUtil.getStringObject(pv_item_Ptn_Id, i));
               System.out.println("wn_ptn_dn_id --------->" + MiUtil.getStringObject(pv_item_Ptn_Id, i));
               objResource.put("wv_ptn_dn_num",MiUtil.getStringObject(pv_item_Num_Tel, i));
               System.out.println("wv_ptn_dn_num --------->" + MiUtil.getStringObject(pv_item_Num_Tel, i));
               objResource.put("wn_ufmi_dn_id",MiUtil.getStringObject(pv_item_Ufmi_Id, i));
               System.out.println("wn_ufmi_dn_id --------->" + MiUtil.getStringObject(pv_item_Ufmi_Id, i));
               objResource.put("wv_status","f");
               list.add(objResource);
             }      
       }
    }
    if (list.size()>0) {
      hshResultReserve=objItemDAO.reserveGoldenNumber(list,intSpecificationId);
      System.out.println("el strMessage en reserveGoldenNumber es ---->" + hshResultReserve.get("strMessage"));    
      if (hshResultReserve.get("strMessage")!=null){
          if (hshResultReserve.get("strMessageError")!=null){
            String message="No se pudo grabar la Orden.\\nLos siguientes numeros no se encuentran disponibles:\\n";
            System.out.println("No se puede Grabar xq hay numeros q ya no estan disponibles ----->");
            hshResultReserve.put("strMessage",message.concat(hshResultReserve.get("strMessageError").toString()));
          }
      }
    }
    
    }
    return hshResultReserve;
  }

  /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
 * <br>Fecha: 02/11/2010
 * @see pe.com.nextel.dao.OrderDAO#getValidateBlacklist(long)      
 ************************************************************************************************************************************/ 
  public HashMap getValidateBlacklist (long lOrderId) throws  Exception, SQLException{
    return objOrderDAO.getValidateBlacklist(lOrderId);
  }
  
  /***********************************************************************************************************************************
   * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
   * <br>Fecha: 23/11/2011   
  ************************************************************************************************************************************/ 
  public HashMap getServiceDefaultListByPlan(long lPlanId) throws  SQLException, Exception {
    return objServiceDAO.getServiceDefaultListByPlan(lPlanId);
  }
  
  /***********************************************************************************************************************************
   * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
   * <br>Fecha: 05/12/2011   
  ************************************************************************************************************************************/ 
  public String getTypePlan(long lPlanId) throws  SQLException, Exception {
    return objPlanDAO.getTypePlan(lPlanId);
  }
  
  /***********************************************************************************************************************************
   * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
   * <br>Fecha: 20/11/2013   
  ************************************************************************************************************************************/ 
  public HashMap doValidateChangePlanToEmployee(RequestHashMap objHashMap) throws  Exception, SQLException {
    HashMap objResultado=null;
    objResultado = objPlanDAO.doValidateChangePlanToEmployee(objHashMap);

    if (objResultado!=null && "1".equals(objResultado.get("lresult").toString())) {
      objResultado.put("strMessageValid","El numero asignado al empleado no puede cambiar a un plan comercial");
    } 
    return objResultado;
  }

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:lhuapaya@practiaconsulting.com">Luis Huapaya</a>
     * <br>Fecha: 30/07/2015
     ************************************************************************************************************************************/
    public HashMap getBolsaCelulares(long idSite, long customerId,long productLine)throws  SQLException, Exception{
        return objProductDAO.getBolsaCelulares(idSite,customerId,productLine);
    }



    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:lhuapaya@practiaconsulting.com">Luis Huapaya</a>
     * <br>Fecha: 30/07/2015
     ************************************************************************************************************************************/
    public HashMap getAllProductBCL(long idSite, long idCustomer)throws  SQLException, Exception{
        return objProductDAO.getAllProductBCL(idSite,idCustomer);
    }

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:lhuapaya@practiaconsulting.com">Luis Huapaya</a>
     * <br>Fecha: 30/07/2015
     ************************************************************************************************************************************/
    public String doValidatePostVentaBolCel(long orderId)throws  SQLException, Exception{
        return objOrderDAO.doValidatePostVentaBolCel(orderId);
    }
    
    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:martin.vera@tcs.com">Martin Vera</a>
     * <br>Fecha: 21/02/2017
     ************************************************************************************************************************************/
    private HashMap createOrdenRentaAdelantada(RequestHashMap objHashMap,OrderBean orderCreadaBean,Connection conn) throws Exception{
    	HashMap objResultado=new HashMap();
    	HashMap objOrdenRA=new HashMap();
    	
		OrderBean orderBean = null;
	    orderBean = this.getOrderData(objHashMap);
	    
	    String tipoDocumento = String.valueOf(objHashMap.get("hdnTypeDocument"));
	    String numeroDocumento = String.valueOf(objHashMap.get("hdnDocument"));	
	    
	    System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenRentaAdelantada/NpCustomerScoreId->"+orderBean.getNpCustomerScoreId());
	    System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenRentaAdelantada/NpSpecificationId->"+orderBean.getNpSpecificationId());
	    System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenRentaAdelantada/tipoDocumento->"+tipoDocumento);
	    System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenRentaAdelantada/numeroDocumento->"+numeroDocumento);
	    
	    
	    if(this.validarRentaAdelantada(String.valueOf(orderBean.getNpSpecificationId()), orderBean.getNpCustomerId(),tipoDocumento,numeroDocumento,objOrdenRA)){

	    	System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenRentaAdelantada/total->calcula total de RA");
	    	
	       String[]  pv_item_cantidad  = objHashMap.getParameterValues("hdnIndice");
           BigDecimal totalRA = new BigDecimal("0");
           if( pv_item_cantidad != null ){
               int cantItems = pv_item_cantidad.length;
            
               for(int i=0; i<cantItems; i++){
            	  String[] pv_item_totalRa     = objHashMap.getParameterValues("hdnItemValuetxtTotalRA");
            	  totalRA = totalRA.add(MiUtil.getBigDecimal(MiUtil.getStringObject(pv_item_totalRa,i)));
               }
           }
                            
            System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenRentaAdelantada/totalRA->"+totalRA);
            
            if(totalRA.compareTo(BigDecimal.ZERO) != 0){
			
	    	    HashMap datosOrdenRA = (HashMap) objOrdenRA.get("datosOrdenRA");
	    	    List arrTableList = (List)datosOrdenRA.get("arrTableList");
	    	    String strMessage = null;
	    	    
		    	HashMap hshResultIncRA = objOrderDAO.createIncidentRA(orderBean.getNpCustomerId() , orderBean.getNpCreatedBy() , conn);
	    		if(hshResultIncRA.get("strMessage")!=null ){
	    			objResultado.put("strMessage", hshResultIncRA.get("strMessage"));
	    		}else{
		    		Hashtable hshResult = this.OrderDAOgetOrderIdNew();
		    		
		    		System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenRentaAdelantada/createIncidentRA->"+hshResultIncRA.get("npincidentid"));
		    		System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenRentaAdelantada/OrderDAOgetOrderIdNew->"+hshResult.get("wn_orderid"));
		    		System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenRentaAdelantada/wv_message->"+hshResult.get("wv_message"));
		    		if(hshResult.get("wv_message")!=null && !"null".equals(hshResult.get("wv_message"))){
		    			objResultado.put("strMessage", hshResult.get("wv_message"));
		    		}else{
		    			
		    			orderBean.setNpGeneratorType("INC");
		    			orderBean.setNpGeneratorId(Long.parseLong(String.valueOf(hshResultIncRA.get("npincidentid"))));
		    			orderBean.setNpOrderId(Long.parseLong(String.valueOf(hshResult.get("wn_orderid"))));
		    			orderBean.setNpOrderCode("P"+orderBean.getNpOrderId());
					    orderBean.setNpSpecification( MiUtil.getNpTable(arrTableList , "NPSPECIFICATION").get("wv_npValueDesc") );
					    orderBean.setNpSpecificationId( Integer.parseInt(MiUtil.getNpTable(arrTableList , "NPSPECIFICATIONID").get("wv_npValueDesc")) );
					    orderBean.setNpType(MiUtil.getNpTable(arrTableList , "NPTYPE").get("wv_npValueDesc") );
					    
					    System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenRentaAdelantada/getOrderInsertar->antes de dao");
					    
					    HashMap hshResultSaveOrder = objOrderDAO.getOrderInsertar(orderBean,conn);
				        System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenRentaAdelantada->"+(String)hshResultSaveOrder.get("strMessage"));
				        if( ((String)hshResultSaveOrder.get("strMessage"))!=null){
				          if (conn != null) conn.rollback();
				          objResultado.put("strMessage", hshResultSaveOrder.get("strMessage"));
				        }else{
				        	System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenRentaAdelantada/crea items->setea bean");
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
					        
					        
					        itemBean.setNpprice(String.valueOf(totalRA));
					        
					        String resultTransaction = this.ItemDAOgetItemInsertar(itemBean,conn); 
					        System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenRentaAdelantada/items->"+resultTransaction);	        
					        
					        objResultado.put("strMessage", resultTransaction);					        
					        objResultado.put("nporderid", orderBean.getNpOrderId());
					        objResultado.put("objItemRentaAdelantada", itemBean);
					        objResultado.put("objOrderRentaAdelantada", orderBean);
					        
					        strMessage = (String)objResultado.get("strMessage");
					        
					        if(strMessage == null){
					        	 HashMap hshResultSpecification = objCategoryDAO.getSpecificationDetail(orderBean.getNpSpecificationId(),conn);					        
							     strMessage=(String)hshResultSpecification.get("strMessage");
							     if(strMessage == null){
							    	 SpecificationBean objSpecificationRentaAdelantada=(SpecificationBean)hshResultSpecification.get("objSpecifBean");
							    	 objResultado.put("objSpecificationRentaAdelantada",objSpecificationRentaAdelantada);  
							     }else{
							    	 objResultado.put("strMessage", strMessage);
				        }
					        }					        
				        
				        }
				        
				        strMessage = (String)objResultado.get("strMessage");
				        		        	
				        if(strMessage == null){
				        	
				        	OrderRentaAdelantadaBean orderRentaAdelantadaBean = new OrderRentaAdelantadaBean();
				        	orderRentaAdelantadaBean.setNpOrderId(orderCreadaBean.getNpOrderId());
					        	orderRentaAdelantadaBean.setNpOrderRefRentaAdelantadaId(orderBean.getNpOrderId());				        	
				        	orderRentaAdelantadaBean.setNpCreatedBy(orderBean.getNpCreatedBy());
				        	
				        	HashMap hshResultCreacionRA = objOrderDAO.crearOrderRentaAdelantada(orderRentaAdelantadaBean, conn);
				        	strMessage = (String) hshResultCreacionRA.get("strMessage");
					        						        	
					        	if(strMessage != null){					        		
				        	objResultado.put("strMessage", strMessage);
				        }
				        	}	        	       	
				        
				        
		    		}
	    		}
            }		    
		}
	    System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenRentaAdelantada/resultado->"+objResultado.get("strMessage"));
	    System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenRentaAdelantada/nporderid->"+objResultado.get("nporderid"));
	    
	    return objResultado;
	}
    
    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:jhonny.quispe@tcs.com">Jhonny Quispe</a>
     * <br>Fecha: 21/02/2017
     ************************************************************************************************************************************/
    public boolean validarRentaAdelantada(String hdnSpecification, long lngCustomerId,String tipoDocumento, String numeroDocumento, HashMap objOrdenRA ) throws SQLException, Exception{	
	    boolean resultado = false;	    
	    Integer specification = MiUtil.parseInt(hdnSpecification);
	    String strMessage = null;
	    
	    if((specification == Constante.SPEC_POSTPAGO_VENTA) || (specification == Constante.SPEC_POSTPAGO_PORTA)){
	    	
	    	HashMap datosOrdenRA = objGeneralDAO.getTableList("RENTA_ADELANTADA","0");
	    	strMessage = (String) datosOrdenRA.get("strMessage");
		    if(strMessage != null){
		    	throw new Exception(strMessage);
		    }
		    
		    if(objOrdenRA != null){
		    	objOrdenRA.put("datosOrdenRA", datosOrdenRA);
		    }		    
		    
		    List arrTableList = (List)datosOrdenRA.get("arrTableList");
		    String flagRA = MiUtil.getNpTable(arrTableList , "FLAG_ACTIVO").get("wv_npValueDesc");
		    
	    	if("1".equalsIgnoreCase(flagRA)){
	    		
	        		String resultPreEvaluation = null;	        		
	        		boolean flagValidaDocumento = false;
			    
	            		if(Constante.TIPO_DOC_DNI.equals(tipoDocumento) && !MiUtil.cadenaVacia(numeroDocumento)){
            			flagValidaDocumento = true;
	            		}else if(Constante.TIPO_DOC_RUC.equals(tipoDocumento) && !MiUtil.cadenaVacia(numeroDocumento)){
            			String strListaPrefijoNumDoc = MiUtil.getNpTable(arrTableList , "PREFIJO_NUM_DOC_NO_VALIDO").get("wv_npValueDesc");
            			String[] listaPrefijoNumDoc = strListaPrefijoNumDoc.split(",");
            			boolean isPrefijoNumDocNoValido = false;
            			
            			for(String prefijoNumDoc:listaPrefijoNumDoc){
            				if(numeroDocumento.startsWith(prefijoNumDoc)){
            					isPrefijoNumDocNoValido = true;
            					break;
            				}
            			}
            			
            			if(!isPrefijoNumDocNoValido){
            				flagValidaDocumento = true;
	            	    	}
	            		}
            		
            		if(flagValidaDocumento){
            			
            			System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenRentaAdelantada/resultPreEvaluation/lngCustomerId->"+lngCustomerId);
            			HashMap hshResultEvaluacion = objOrderDAO.getPreEvaluacionCliente(lngCustomerId);
            			strMessage = (String)hshResultEvaluacion.get("strMessage");
            			if(strMessage != null){
            		    	throw new Exception(strMessage);
            		    }
            			
            			resultPreEvaluation = (String)hshResultEvaluacion.get("strResultado");
            			System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenRentaAdelantada/resultPreEvaluation/numeroDocumento->"+numeroDocumento);
            			System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenRentaAdelantada/resultPreEvaluation/tipoDocumento->"+tipoDocumento);
            			
            			System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenRentaAdelantada/resultPreEvaluation->"+resultPreEvaluation);
            			if(Constante.CLIENTE_PRE_EVAL_CONDICIONADO.equalsIgnoreCase(resultPreEvaluation)){
            				resultado = true;
	        	}
	        }    
	    	
	        	
	    }
	    
	    }
	    
	    return resultado;
    }

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:jhonny.quispe@tcs.com">Jhonny Quispe</a>
     * <br>Fecha: 07/04/2017
     ************************************************************************************************************************************/
    public HashMap getOrdenRentaAdelantada(long orderId) throws SQLException, Exception{
    	HashMap objResultado=new HashMap();
    	HashMap hshResultOrdenRentaAdelantada = null;
    	String strMessage = null;
    	
    	OrderRentaAdelantadaBean objOrderRentaAdelantadaBean = new OrderRentaAdelantadaBean();
    	objOrderRentaAdelantadaBean.setNpOrderId(orderId);
    	
    	hshResultOrdenRentaAdelantada = objOrderDAO.getOrdenRentaAdelantada(objOrderRentaAdelantadaBean);
    	strMessage = (String) hshResultOrdenRentaAdelantada.get("strMessage");
    	
    	objResultado.put("strMessage", strMessage);
    	objResultado.put("objOrderRentaAdelantadaBean", objOrderRentaAdelantadaBean);
    	return objResultado;
    }

    /**
     * @author AMENDEZ
     * @project PRY-0864
     * Metodo   Evalua mostrar campo precio excepcion en popupitem
     *          flag vep activo y cliente es ruc20(Juridico): 1 --> Se muestra campo de precio excepcion
     *          flag vep inactivo y cliente no es ruc20(Natural): 0 --> No se muestra campo de precio excepcion
     *          errores generales -1
     * @return
     */
    public int evaluateExceptionPriceVep(int npvep,long sw_customerid) throws SQLException, Exception{
        return objInstallmentSalesDAO.evaluateExceptionPriceVep(npvep,sw_customerid);
    }

    /**
     * @author AMENDEZ
     * @project PRY-0864 | PRY-1200
     * Metodo   Evalua el monto inicial en casos de persona natural y persona juridica
     *          flag vep activo, cliente es ruc20(Juridico) y monto inicial no obligatorio: 1
     *          flag vep activo y cliente no es ruc20(Natural) y monto inicial es obligatorio: 2
     *          errores generales -1
     * @return
     */
    public String validateOrderVepCI(long nporderid,int npvepquantityquota,double npinitialquota,int npspecificationid,long swcustomerid,double totalsalesprice,int npvep,String nptype,int nppaymenttermsiq) throws SQLException, Exception{
        return objInstallmentSalesDAO.validateOrderVepCI(nporderid,npvepquantityquota,npinitialquota,npspecificationid,swcustomerid,totalsalesprice,npvep,nptype,nppaymenttermsiq);
    }
   
    	/***********************************************************************************************************************************
     * <br>Realizado por: JCURI PRY-0890
     ************************************************************************************************************************************/
    private HashMap createOrdenProrrateo(RequestHashMap objHashMap,OrderBean orderCreadaBean, long strSpecificationId, Connection conn) throws Exception {
    	System.out.println("[INI] SEJBOrderNewBean/doSaveOrder/createOrdenProrrateo/createOrdenProrrateo");
    	HashMap objResultado=new HashMap();    	
    	String strMessage = null;
		OrderBean orderSearchBean = null;
		boolean configtipodoc = false ; //JBALCAZAR PRY-1002
    	boolean configcanal = false ;  //JBALCAZAR PRY-1002  			
		
		// crear items orden padre
		List<ItemBean> listItemSave = new ArrayList<ItemBean>();
		strMessage = saveNewItems(objHashMap, conn, orderCreadaBean, listItemSave);
		if(strMessage != null ){
			throw new Exception(strMessage);
		}		
		if(listItemSave == null || listItemSave.size() == 0) {
			throw new Exception("Error al crear item(s)");
		}
		System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenProrrateo/listItemSave->" + listItemSave.size());
		
		strMessage= insertSectionDynamics(strSpecificationId, objHashMap, conn, orderCreadaBean);
		if( strMessage!=null){
			throw new Exception(strMessage);
         }
		
		orderSearchBean = this.getOrderData(objHashMap);
	    
	    Integer specification = MiUtil.parseInt(String.valueOf(orderSearchBean.getNpSpecificationId()));
	    String dispatchPlace = String.valueOf(orderSearchBean.getNpDispatchPlaceId());
	    String tipoDocumento = String.valueOf(objHashMap.get("hdnTypeDocument"));
	    String numeroDocumento = String.valueOf(objHashMap.get("hdnDocument"));
	    String generatorType = String.valueOf(objHashMap.get("hdnGeneratorType"));
	    String origenType = String.valueOf(objHashMap.get("hdnOrigenType"));	    
	    String createdBy = String.valueOf(objHashMap.get("hdnSessionLogin"));
	    
	    
	    System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenProrrateo/NpCustomerScoreId->"+orderSearchBean.getNpCustomerScoreId());
	    System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenProrrateo/NpSpecificationId->"+specification);
	    System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenProrrateo/dispatchPlace->"+dispatchPlace);
	    System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenProrrateo/tipoDocumento->"+tipoDocumento);
	    System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenProrrateo/numeroDocumento->"+numeroDocumento);
	    System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenProrrateo/generatorType->"+generatorType);//JBALCAZAR PRY-1002
	    System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenProrrateo/generatorType->"+origenType);//JBALCAZAR PRY-1002	    
	    System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenProrrateo/createdBy->"+createdBy);	    
	    
	     configtipodoc = this.ConfiguracionTipoDocumentoProrrateo(tipoDocumento, numeroDocumento);//JBALCAZAR PRY-1002
		    System.out.println("SEJBOrderNewBean/doSaveOrder/obtenerFlagActivoProrrateo/conftipodoc"+configtipodoc);		    
		    configcanal = sejbApportionmentBean.getCanalProrrateo(specification, generatorType, createdBy);	    //JBALCAZAR PRY-1002
	    	System.out.println("SEJBOrderNewBean/doSaveOrder/configcanal -> configcanal : " + configcanal);				     
	     	     
		if(configcanal && configtipodoc && (specification == Constante.SPEC_POSTPAGO_VENTA || specification == Constante.SPEC_POSTPAGO_PORTA)) {
			
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
	    		hashMapDAO = objOrderDAO.createIncidentPRORRA(orderSearchBean.getNpCustomerId(), orderSearchBean.getNpCreatedBy(), conn);
	    		strMessage = (String) hashMapDAO.get("strMessage");
	    		if(strMessage != null ){
	    			throw new Exception(strMessage);
	    		}	    		
	    		
	    		Hashtable hshResult = this.OrderDAOgetOrderIdNew();
	    		
	    		System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenProrrateo/OrderDAOgetOrderIdNew->"+hshResult.get("wn_orderid"));
	    		System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenProrrateo/wv_message->"+hshResult.get("wv_message"));
	    		if(hshResult.get("wv_message")!=null && !"null".equals(hshResult.get("wv_message"))) {
	    			objResultado.put("strMessage", hshResult.get("wv_message"));
	    			throw new Exception(hshResult.get("wv_message").toString());
	    		} 
	    		
                        System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenProrrateo->set data PA");
	    		OrderBean orderBean = getDataOrderForProrrateo(orderSearchBean);
	    		
	    		orderBean.setNpGeneratorType("INC");
    			orderBean.setNpGeneratorId(Long.parseLong(String.valueOf(hashMapDAO.get("npincidentid"))));
    			orderBean.setNpOrderId(Long.parseLong(String.valueOf(hshResult.get("wn_orderid"))));
    			orderBean.setNpOrderCode("P"+orderBean.getNpOrderId());
			    orderBean.setNpSpecification( MiUtil.getNpTable(arrTableList , "NPSPECIFICATION").get("wv_npValueDesc") );
			    orderBean.setNpSpecificationId( Integer.parseInt(MiUtil.getNpTable(arrTableList , "NPSPECIFICATIONID").get("wv_npValueDesc")) );
			    orderBean.setNpType(MiUtil.getNpTable(arrTableList , "NPTYPE").get("wv_npValueDesc") );
			    orderBean.setNpPaymentTerms(Constante.PAYFORM_CONTADO);
			    
			    System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenProrrateo/getOrderInsertar->antes de dao");
			    System.out.println("[SEJBOrderNewBean][orderBean] : " + orderBean.toString());
			    HashMap hshResultSaveOrder = objOrderDAO.getOrderInsertar(orderBean,conn);
		        System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenProrrateo->"+(String)hshResultSaveOrder.get("strMessage"));
		        strMessage = (String) hshResultSaveOrder.get("strMessage");
		        if( strMessage != null) {
		        	throw new Exception(strMessage);
		        }
		        	
		        System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenProrrateo/crea items->setea bean");
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
		        
		        String resultTransaction = this.ItemDAOgetItemInsertar(itemBean,conn); 
		        System.out.println("SEJBOrderNewBean/doSaveOrder/createOrdenProrrateo/items->"+resultTransaction);	        
		        
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
	        	long orderparentId= orderCreadaBean.getNpOrderId();
	        	long orderId= orderBean.getNpOrderId();
	        	long itemId= itemBean.getNpitemid();
	        	String prorrateo = "S";
	        	
	        	System.out.println("SEJBOrderEditBean/updOrder/createUpdOrdenProrrateo [orderParentId] "+orderparentId + " [orderChildId] " + orderId + " [itemId] " + itemId);
	        	strMessage  = objOrderDAO.updateCreateOrderProrratero(orderparentId, orderId, prorrateo, itemId, montoTotalProrrateo, orderBean.getNpCreatedBy(),"",1, conn);
	    		if(strMessage != null ) {
	    			throw new Exception(strMessage);
	    		}
	    		
	    		//DATOS DEL PRORRATEO
	    		SectionItemEvents sectionItemEvent = new SectionItemEvents(); 
	    		List<ApportionmentBean> list = sectionItemEvent.getDataItemApportionment(objHashMap, listItemSave);
	    		if(list!=null && list.size() > 0) {
	    			System.out.println("SEJBOrderEditBean/updOrder/saveItemApi [INI]");
	    			for(ApportionmentBean bean : list) {
	    				strMessage = apportionmentDAO.saveItemApi(bean, orderBean.getNpCreatedBy(), conn);
    					System.out.println("SEJBOrderNewBean/createOrdenProrrateo/saveItemApi [strMessage] " + strMessage);
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
		System.out.println("[END] SEJBOrderNewBean/doSaveOrder/createOrdenProrrateo/createOrdenProrrateo");
	    return objResultado;
	}    
    
    public String saveNewItems(RequestHashMap objHashMap,Connection conn,OrderBean orderBean, List<ItemBean> listItemSave) throws Exception{
    	System.out.println("SEJBOrderNewBean/doSaveOrder/saveNewItems -> orderBeanId : " + orderBean.getNpOrderId());
    	SectionItemEvents sectionItemEvent = new SectionItemEvents();    	
    	String strMessage = sectionItemEvent.grabarItems(objHashMap, conn, listItemSave);
    	System.out.println("SEJBOrderNewBean/doSaveOrder/saveNewItems -> strMessage : " + strMessage);
    	if( strMessage!=null){
            if (conn != null) conn.rollback();
            return strMessage;
        }
    	System.out.println("SEJBOrderNewBean/doSaveOrder/saveNewItems -> listItemSave : " + listItemSave);
    	if(listItemSave == null || listItemSave.size() == 0) {
    		return "Items no registrados";
    	}
    	System.out.println("SEJBOrderNewBean/doSaveOrder/saveNewItems -> listItemSave.size : " + listItemSave.size());
    	return null;
    }
    
    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:jorge.curi@tcs.com">JCURI</a>
     * <br>Fecha: 11/07/2017
     ************************************************************************************************************************************/
    public boolean obtenerFlagActivoProrrateo(String hdnSpecification, String tipoDocumento , String numeroDocumento,String strGeneratorType, String strUser) throws SQLException, Exception{
    	System.out.println("SEJBOrderNewBean/doSaveOrder/obtenerFlagActivoProrrateo -> hdnSpecification:" +hdnSpecification);
    	System.out.println("SEJBOrderNewBean/doSaveOrder/obtenerFlagActivoProrrateo -> tipoDocumento:"+tipoDocumento);
    	System.out.println("SEJBOrderNewBean/doSaveOrder/obtenerFlagActivoProrrateo -> numeroDocumento:"+numeroDocumento);
    	System.out.println("SEJBOrderNewBean/doSaveOrder/obtenerFlagActivoProrrateo -> tipoDocumento:"+strGeneratorType);
    	System.out.println("SEJBOrderNewBean/doSaveOrder/obtenerFlagActivoProrrateo -> numeroDocumento:"+strUser);
    	
    	boolean resultado = false;	 
    	HashMap hashMapDAO = null;
    	boolean configtipodoc = false ; 
    	boolean configcanal = false ;    	
	    Integer specification = MiUtil.parseInt(hdnSpecification);
	    //JBALCAZAR PRY-1002
	    configtipodoc = this.ConfiguracionTipoDocumentoProrrateo(tipoDocumento, numeroDocumento);
	    System.out.println("SEJBOrderNewBean/doSaveOrder/obtenerFlagActivoProrrateo/conftipodoc"+configtipodoc);
	    
	    configcanal = sejbApportionmentBean.getCanalProrrateo(specification, strGeneratorType, strUser);	    
    	System.out.println("SEJBOrderNewBean/doSaveOrder/configcanal -> configcanal : " + configcanal);	    
	    
	    if (configcanal && configtipodoc && (specification==Constante.SPEC_POSTPAGO_VENTA || specification==Constante.SPEC_POSTPAGO_PORTA)) {
	    	hashMapDAO = objGeneralDAO.getDescriptionByValue("FLAG_ACTIVO", "ORDEN_PRORRATEO");
	    	String strMessage = (String) hashMapDAO.get("strMessage");
	    	System.out.println("SEJBOrderNewBean/doSaveOrder/obtenerFlagActivo -> strMessage : " + strMessage);
		    if(strMessage != null) {
		    	return resultado;
		    }		    
		    String strDescription = (String) hashMapDAO.get("strDescription");
		    System.out.println("SEJBOrderNewBean/doSaveOrder/obtenerFlagActivo -> strDescription : " + strDescription);
		    if("1".equals(strDescription.trim())) {
		    	resultado = true;
		    }
	    }
    	return resultado;
    }  
    
    public boolean ConfiguracionTipoDocumentoProrrateo(String tipoDocumento, String numeroDocumento) throws SQLException, Exception{
    	System.out.println("SEJBOrderNewBean/doSaveOrder/Configuracion TipoDocumentoProrrateo :" +tipoDocumento + "numeroDocumento: "+numeroDocumento );
    	
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
		System.out.println("doc__validos :" + doc_validos);
		
		for(int i=0;i<=arrTableList.size();i++){
			
            map=(HashMap)arrTableList.get(i);
            	
	         if(map.get("wv_npValueDesc").equals(tipo_doc)){
	        	 
					System.out.println(" map.get(wv_npValueDesc) = " + map.get("wv_npValueDesc"));	
				    if(map.get("wv_npValueDesc").equals("RUC")){
						System.out.println(" RUC=====> " + map.get("wv_npValueDesc"));
						
						for (String prefijoNumDoc : listaPrefijoNumDoc) {

							if (numero_doc.startsWith(prefijoNumDoc)) {
								System.out.println("prefijoNumDoc::::" + prefijoNumDoc);
								System.out.println("Doc RUC::::" + tipo_doc);
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
		
		System.out.println("numero de doc :" + numero_doc);

		return flag;

    }
    
    

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:jorge.curi@tcs.com">JCURI</a>
     * <br>Fecha: 26/07/2017
     ************************************************************************************************************************************/
    public String insertSectionDynamics(long strSpecificationId, RequestHashMap objHashMap,Connection conn,OrderBean orderBean) throws Exception{
    	System.out.println("SEJBOrderNewBean/insertSectionDynamics [Usuario] "+ orderBean.getNpCreatedBy() +" [OrderId] " + orderBean.getNpOrderId() + " [strSpecificationId] " + strSpecificationId);
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
        
        ArrayList arrLista = new ArrayList();
        SectionDinamicBean sectionDinamicBean = new SectionDinamicBean();
        String hdnOrigenTypeMassive = objHashMap.getParameter("hdnOrigenType");
        
        System.out.println("SEJBOrderNewBean/insertSectionDynamics [strSpecificationId] " + strSpecificationId + "[hdnOrigenTypeMassive] " + hdnOrigenTypeMassive);
        // Se agrego el Nuevo parametro para masivos
        HashMap objHashMapSpec = objCategoryDAO.getSpecificationData(strSpecificationId, hdnOrigenTypeMassive);
        if( objHashMapSpec.get("strMessage") != null ) 
        	return (String)objHashMapSpec.get("strMessage");
        
        arrLista = (ArrayList)objHashMapSpec.get("objArrayList");
        System.out.println("SEJBOrderNewBean/insertSectionDynamics [lista.size] " + arrLista.size());
        
        for(int i=0;i< arrLista.size();i++ ){
          sectionDinamicBean =   (SectionDinamicBean)arrLista.get(i);          
          strNpeventName     =   MiUtil.getString(sectionDinamicBean.getNpeventname());
          strNameMethod      =   MiUtil.getString(sectionDinamicBean.getNpeventhandler());
          strNameClass       =   MiUtil.getString(sectionDinamicBean.getNpobjectname());
          System.out.println("SEJBOrderNewBean/insertSectionDynamics [strNpeventName] " + strNpeventName + " [strNameMethod] " + strNameMethod + " [strNameClass] " + strNameClass);
          
          if (Constante.NEW_ON_SAVE.equals(strNpeventName) && !"saveSection1".equals(strNameMethod)
        		  && !"pe.com.nextel.section.sectionItem.SectionItemEvents".equals(strNameClass)){        	
        	clase=Class.forName(strNameClass);
            System.out.println("== Nombre de la clase para el registro " + strNameClass);
            System.out.println("== Nombre del método invocado " + strNameMethod);
            clase1=Class.forName("pe.com.nextel.util.RequestHashMap");
            clase2=Class.forName("java.sql.Connection");
            objeto=clase.newInstance();
            
            method = clase.getMethod(strNameMethod,new Class[] { clase1, clase2});                   
            strMessage =(String)method.invoke(objeto, new Object[] { objHashMap , conn});  
            
            if( strMessage!=null){
              if (conn != null) conn.rollback();
              return strMessage;
            }

          }
        }
        System.out.println("SEJBOrderNewBean/insertSectionDynamics [FINISH]-[strMessage] " + strMessage);
      return strMessage;      
      }
    
    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:jorge.curi@tcs.com">JCURI</a>
     ************************************************************************************************************************************/
    public HashMap validarPagoAnticipado(HashMap  hshResultado, RequestHashMap request, OrderBean orderCreadaBean, int intSpecificationId, Connection conn) throws Exception {
    	System.out.println("SEJBOrderNewBean/updOrder/validarPagoAnticipado [INICIO]");
    	HashMap hshResultadoPA = new HashMap(); 
    	String strSpecificationId   = (request.get("hdnSpecification")==null?"":(String)request.get("hdnSpecification"));          
        long   longSpecificationId  = MiUtil.parseLong(strSpecificationId);
        String isClientPostPago     = String.valueOf(request.get("hdnIsClientPostPago"));
        
        //PRY-0890 JCURI PRORRATEO
        String apportionment = (request.get("hdnApportionment") == null ? null : (String) request.get("hdnApportionment"));  
        System.out.println("SEJBOrderNewBean/updOrder [lOrderId] " + orderCreadaBean.getNpOrderId() + " [apportionment] " + apportionment + " [intSpecificationId] " + intSpecificationId + " [isClientPostPago] " + isClientPostPago);      
        if (apportionment!=null && "S".equals(apportionment) && 
				(intSpecificationId==Constante.SPEC_POSTPAGO_VENTA || intSpecificationId==Constante.SPEC_POSTPAGO_PORTA)) {
        	
            HashMap hshResultSaveOrderProrra= this.createOrdenProrrateo(request, orderCreadaBean, longSpecificationId, conn);
            System.out.println("doSaveOrder[SEJBOrderNewBean]: hshResultSaveOrderProrra >"+hshResultSaveOrderProrra.get("strMessage"));
            if( ((String)hshResultSaveOrderProrra.get("strMessage"))!=null){
                if (conn != null) conn.rollback();
                return hshResultado;
             }else{
            	 ItemBean objItemRentaProrrateo = (ItemBean) hshResultSaveOrderProrra.get("objItemProrrateo");
            	 hshResultado.put("objItemProrrateo", objItemRentaProrrateo);
            	 OrderBean objOrderProrrateo = (OrderBean)hshResultSaveOrderProrra.get("objOrderProrrateo");
            	 hshResultado.put("objOrderProrrateo", objOrderProrrateo);
            }
        	
        } else {
        	//Secciones dinamicas
     		hshResultadoPA.put("invokeSectionDynamics","S");
     		System.out.println("updOrder[SEJBOrderEditBean]: invokeSectionDynamics > S");
     		     		
     		//JCURI : PRY-1002 - INIT
     		if (apportionment!=null && "N".equals(apportionment) && 
				(intSpecificationId==Constante.SPEC_POSTPAGO_VENTA || intSpecificationId==Constante.SPEC_POSTPAGO_PORTA)) {     			
     			invocarCalculoProrrateo(request, orderCreadaBean, conn);        	
     		}//JCURI : PRY-1002 - END     		
     		
        }
        System.out.println("SEJBOrderNewBean/updOrder/validarPagoAnticipado [FIN]");
        return hshResultadoPA;
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
           
    /**
     * @author AMENDEZ
     * @project PRY-0980 -PRY-1200
     * Metodo   Retorna valor para colocar el check por defecto de forma de pago cuota inicial
     * @return
     */
    public int validatePaymentTermsCI(long swcustomerid,long userid,int npvep) throws Exception{
        return objInstallmentSalesDAO.validatePaymentTermsCI(swcustomerid,userid,npvep);
    }

    private void invocarCalculoProrrateo(RequestHashMap objHashMap, OrderBean orderBean, Connection conn) throws Exception {
    	logger.info("SEJBOrderNewBean/invocarCalculoProrrateo [INICIO]");
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
    		request.setAccion("NNEWACTION");
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
    			logger.info("SEJBOrderNewBean/invocarCalculoProrrateo [OrderId]" + request.getOrderId() + " [status] " + status + " [message] " + message);
    			
    			if(status==0L || status==-1L) {
    				strCicloAnterior = (String) responseMap.get("OldBillCycle");
    				strCicloNuevo = (String) responseMap.get("NewBillCycle");
    				estadoSW = 0;
    			}
			} catch (Exception e) {
				logger.info("SEJBOrderNewBean/invocarCalculoProrrateo/sejbApportionmentBean.getCalculatePayment [ERROR] " + e.getMessage());
			}    		
			
			strMessage  = objOrderDAO.insertNoProrrateo(orderBean.getNpOrderId(), request.getTrxId(), strTipoDocumento, strNroDocumento, strCicloAnterior, strCicloNuevo, estadoSW, strCreatedby, conn);
    		if(strMessage != null ) {
    			throw new Exception(strMessage);
    		}
    		
    		logger.info("SEJBOrderNewBean/invocarCalculoProrrateo [FIN]");
		} catch (Exception e) {
			logger.info("SEJBOrderNewBean/invocarCalculoProrrateo [ERROR] " + e.getMessage());
			throw new Exception(e);
		}
    } 

    /**
     * Purpose: Valida que exista una configuracion para bafi 2300 segun modalidad, solucion y linea producto.
     * Developer       Fecha       Comentario
     * =============   ==========  ======================================================================
     * AMENDEZ         22/03/2018  Creacion
     */
    public int validateConfigBafi2300(String av_npmodality,String av_npsolutionid,String av_npproductlineid) throws Exception{
        return objOrderDAO.validateConfigBafi2300(av_npmodality,av_npsolutionid,av_npproductlineid);
    }
    /**
     * @author CMONETEROS
     * @project PRY-1062
     * Metodo   Valida la preevaluacion para Cambio de Modelo.
     * @return
     */
    public HashMap doValidatePreevaluationCDM(long customerId)
            throws SQLException, Exception
    {
        return this.objOrderDAO.doValidatePreevaluationCDM(customerId);
    }

    /**
     * @author KPEREZ
     * @project PRY-1037
     * Metodo   Valida .
     * @return
     */
    public HashMap doValidateSimManager(String[] item_Product_Val)
            throws SQLException, Exception
    {
        return this.objOrderDAO.doValidateSimManager(item_Product_Val);
    }



    /**
     * @author CMONETEROS
     * @project PRY-1062
     * Metodo   Valida que el numero de telefono no esté asociado a una cuota VEP
     * @return
     */
    public HashMap doValidateVEPItem(long customerId, String strPhoneNumber)
            throws SQLException, Exception {
        return this.objOrderDAO.doValidateVEPItem(customerId, strPhoneNumber);
    }   
      
	/*
    Method :    activeChkCourier
    Project:    PRY-1093
    Purpose:    Valida la activacion del check de courier
    Developer       Fecha       Comentario
    =============   ==========  ======================================================================
    JCURI    	    23/05/2018  Creación
    */
    public boolean activeChkCourier(int iUserId, int iAppId) throws Exception {
    	logger.info("SEJBOrderNewBean/activeChkCourier [INICIO] [iUserId] " + iUserId + "[iAppId] " + iAppId);
    	boolean result = false;
    	HashMap hshResult = objOrderDAO.activeChkCourier(iUserId, iAppId);
    	String strMessage = (String) hshResult.get("strMessage");
    	
    	if(strMessage == null) {
    		int activeChk = (Integer) hshResult.get("statusActive");    		
    		if(activeChk == 1) 
    			result = true;
    	} else {
    		logger.error("SEJBOrderNewBean/activeChkCourier [FIN] [strMessage] " + strMessage);
    	}
    	logger.info("SEJBOrderNewBean/activeChkCourier [FIN] [result] " + result);
    	return result;
    }
    /**
     * @author AMENDEZ
     * @project PRY-1200
     * Metodo   Valida si la especificacion esta configurada para VEP
     *          flag 1, Aplica
     *          flag 0, No aplica
     *          flag -1, Errores
     * @return
     */
    public String validateSpecificationVep(int anum_npspecificationid) throws SQLException, Exception{
        return objInstallmentSalesDAO.validateSpecificationVep(anum_npspecificationid);
    }

    /**
     * @author AMENDEZ
     * @project PRY-1200
     * Metodo   Obtiene valor de configuracion de tablas VEP
     * @return
     */
    public String getConfigValueVEP(String avch_npvaluedesc) throws SQLException, Exception{
        return objInstallmentSalesDAO.getConfigValueVEP(avch_npvaluedesc);
    }
           
} 