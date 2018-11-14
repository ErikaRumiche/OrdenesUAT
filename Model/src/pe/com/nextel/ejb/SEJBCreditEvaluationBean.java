package pe.com.nextel.ejb;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import org.apache.log4j.Logger;
import pe.com.nextel.bean.ItemBean;
import pe.com.nextel.bean.ItemServiceBean;
import pe.com.nextel.bean.OrderBean;
import pe.com.nextel.dao.CreditEvaluationDAO;
import pe.com.nextel.dao.InstallmentSalesDAO;
import pe.com.nextel.dao.Proveedor;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;


public class SEJBCreditEvaluationBean implements SessionBean 
{

  private CreditEvaluationDAO objCreditEvaluationDAO = null;
  private SessionContext _context;
  private static Logger logger = Logger.getLogger(SEJBCreditEvaluationBean.class);
  
  public void ejbCreate()  {
    objCreditEvaluationDAO = new CreditEvaluationDAO();
  }

  public void ejbActivate() throws EJBException {
    System.out.println("[SEJBCreditEvaluationBean][ejbActivate()]");
  }

  public void ejbPassivate() throws EJBException {
    System.out.println("[SEJBCreditEvaluationBean][ejbPassivate()]");
  }

  public void ejbRemove() throws EJBException {
    System.out.println("[SEJBCreditEvaluationBean][ejbRemove()]");
  }

  public void setSessionContext(SessionContext context) throws EJBException {
    _context = context;
  }
  
  public HashMap getCreditEvaluationData(long lSourceId, String strSource) throws Exception {    
    return objCreditEvaluationDAO.getCreditEvaluationData(lSourceId, strSource);
  }
  
  public HashMap getRuleResult(long lCreditEvaluationId,String strSource) throws SQLException {
    return objCreditEvaluationDAO.getRuleResult(lCreditEvaluationId, strSource);
  }

  public HashMap doEvaluateOrder(RequestHashMap objHashMap) throws SQLException {
    OrderBean objOrderBean = new OrderBean();
    String strMessage = null;
    HashMap hshResultCreditEvaluation = new HashMap();
    HashMap hshData = new HashMap();
    try {
      hshData = getOrderData(objHashMap);
      hshResultCreditEvaluation = objCreditEvaluationDAO.doEvaluateOrder(hshData);
    } catch (Exception ex) {
      ex.printStackTrace();
      strMessage  = MiUtil.getMessageClean("[Exception][doSaveOrder]["+ex.getClass() + " " + ex.getMessage()+"]");
      hshResultCreditEvaluation.put("strMessage",strMessage);
      return hshResultCreditEvaluation;
    }
    return hshResultCreditEvaluation;
  }

  /**
   * EFLORES PM0011359 Se saca una copia del metodo doEvaluateOrder para definir un commit manual para VEP
   * @param objHashMap
   * @return
   * @throws SQLException
   */
   
  public HashMap doEvaluateOrderCreate(RequestHashMap objHashMap) throws SQLException {
    logger.info("************************ INICIO SEJBCreditEvaluationBean > doEvaluateOrderCreate(RequestHashMap objHashMap)************************");
    InstallmentSalesDAO objInstallmentSalesDAO = new InstallmentSalesDAO();
    OrderBean objOrderBean = new OrderBean();
    String strMessage = null;
    HashMap hshResultCreditEvaluation = new HashMap();
    HashMap hshData = new HashMap();
    try {
      Connection conn = Proveedor.getConnection();
      conn.setAutoCommit(false);
      hshData = getOrderData(objHashMap);
      objOrderBean = (OrderBean)hshData.get("orderBean");
      //Validacion de cuota VEP EFLORES PM0011359
      Constante.VEP_OPERATIONS_ENUM result = (Constante.VEP_OPERATIONS_ENUM) objHashMap.get(Constante.VEP_OPERATIONS);
      if(result != null){
          logger.info("[PM0011359][SEJBCreditEvaluationBean][doEvaluateOrderCreate] Valor de OPERACION VEP A REALIZAR = "+result);
        if(result == Constante.VEP_OPERATIONS_ENUM.DELETE_AND_SAVE_VEP){
          hshResultCreditEvaluation = objInstallmentSalesDAO.doDeleteInstallmentSales(objOrderBean.getNpOrderId(),conn);
          if(((String) hshResultCreditEvaluation.get("strMessage"))!=null){
            if (conn != null) conn.rollback();
            return  hshResultCreditEvaluation;
          }
          String strCreatedby  = (String)objHashMap.getParameter("hdnSessionLogin");
          String strCodBSCS    = (String)objHashMap.getParameter("strCodBSCS");
          long lOrderId = MiUtil.parseLong(((String)objHashMap.get("hdnNumeroOrder")));
          logger.info("[PM0011359][SEJBCreditEvaluationBean][doEvaluateOrderCreate] Invocando doCreateInstallmentSales");
          logger.info("[PM0011359][SEJBCreditEvaluationBean][doEvaluateOrderCreate] lOrderId = "+lOrderId);
          logger.info("[PM0011359][SEJBCreditEvaluationBean][doEvaluateOrderCreate] NpCustomerId = "+objOrderBean.getNpCustomerId());
          logger.info("[PM0011359][SEJBCreditEvaluationBean][doEvaluateOrderCreate] NpSiteId = "+objOrderBean.getNpSiteId());
          logger.info("[PM0011359][SEJBCreditEvaluationBean][doEvaluateOrderCreate] OL");
          logger.info("[PM0011359][SEJBCreditEvaluationBean][doEvaluateOrderCreate] CodBscs = "+strCodBSCS);
          logger.info("[PM0011359][SEJBCreditEvaluationBean][doEvaluateOrderCreate] NumCuotas = "+objOrderBean.getNpNumCuotas());
          logger.info("[PM0011359][SEJBCreditEvaluationBean][doEvaluateOrderCreate] AmountVep = "+objOrderBean.getNpAmountVep());
          logger.info("[PM0011359][SEJBCreditEvaluationBean][doEvaluateOrderCreate] ORDER_NEW");
          logger.info("[PM0011359][SEJBCreditEvaluationBean][doEvaluateOrderCreate] CreatedBy = "+strCreatedby);

          hshResultCreditEvaluation = objInstallmentSalesDAO.doCreateInstallmentSales(
                  lOrderId,objOrderBean.getNpCustomerId(),
                  objOrderBean.getNpSiteId(),
                  0L,
                  strCodBSCS,
                  objOrderBean.getNpNumCuotas(),
                  objOrderBean.getNpAmountVep(),
                  "ORDER_NEW",
                  strCreatedby,
                  objOrderBean.getInitialQuota(),
                  //INICIO: PRY-0980 | AMENDEZ
                  objOrderBean.getNpPaymentTermsIQ(),
                  //FIN: PRY-0980 | AMENDEZ
                  conn);

          if(((String) hshResultCreditEvaluation.get("strMessage"))!=null){
            if (conn != null) conn.rollback();
            return  hshResultCreditEvaluation;
          }
          logger.info("[PM0011359][SEJBCreditEvaluationBean][doEvaluateOrderCreate] DELETE_AND_SAVE_VEP Ejecucion de comando");
        }else if(result == Constante.VEP_OPERATIONS_ENUM.DELETE_VEP){
          logger.info("[PM0011359][SEJBCreditEvaluationBean][doEvaluateOrderCreate] SAVE_VEP Ejecucion de comando");
          hshResultCreditEvaluation = objInstallmentSalesDAO.doDeleteInstallmentSales(objOrderBean.getNpOrderId(),conn);
          if(((String) hshResultCreditEvaluation.get("strMessage"))!=null){
            if (conn != null) conn.rollback();
            return  hshResultCreditEvaluation;
          }
        }
      }

      hshResultCreditEvaluation = objCreditEvaluationDAO.doEvaluateOrder(hshData,conn);
      if((String)hshResultCreditEvaluation.get("strMessage")!=null){
        if (conn != null) conn.rollback();
        return hshResultCreditEvaluation;
      }
      //Confirmar la transaccion en la BD
      conn.commit();
    } catch (Exception ex) {
      logger.error(ex);
      ex.printStackTrace();
      strMessage  = MiUtil.getMessageClean("[Exception][doSaveOrder]["+ex.getClass() + " " + ex.getMessage()+"]");
      hshResultCreditEvaluation.put("strMessage",strMessage);
      return hshResultCreditEvaluation;
    }

    logger.info("************************ FIN SEJBCreditEvaluationBean > doEvaluateOrderCreate(RequestHashMap objHashMap)************************");
    return hshResultCreditEvaluation;

  }
  
  public HashMap getOrderData(RequestHashMap request) throws Exception{
    // Variables para funcionalidad de grabación
    String  strOrderId ="",            
            strCodigoCliente= "",
            strnpSite ="",
            strnpdivisionid = "",
            strnpspecificationid = "",
            strStatus= "",
            strCreatedby = "",
            strNpPaymentTerms = "",
            strNpProviderGrpId = ""
            //INICIO: PRY-1200 | AMENDEZ
            ,strVepFlag=""
            ,strVepNumCuotas = ""
            ,strTotalSalesPriceVEP = ""
            ,strInicialQuota=""
            ,strNpPaymentTermsIQ=""
            //FIN: PRY-1200 | AMENDEZ
            ;

    Date   df = null;
    HashMap hshResult = new HashMap();
    HashMap hshAux = null;
    
    strOrderId              =   (String)request.getParameter("hdnNumeroOrder");
    strCodigoCliente        =   (String)request.getParameter("txtCompanyId");
    strnpSite               =   (String)request.getParameter("hdnSite");
    strnpdivisionid         =   (String)request.getParameter("cmbDivision")==null?(String)request.getParameter("hdnDivisionId"):(String)request.getParameter("cmbDivision");
    strnpspecificationid    =   (String)request.getParameter("hdnSpecification")==null?(String)request.getParameter("hdnSubCategoria"):(String)request.getParameter("hdnSpecification");
    strCreatedby            =   (String)request.getParameter("hdnSessionLogin");
    strNpPaymentTerms       =   (String)request.getParameter("cmbFormaPago");
    strNpProviderGrpId      =   (String)request.getParameter("hdnVendedorId");

    OrderBean orderBean = new OrderBean();        
    
    orderBean.setNpOrderId(MiUtil.parseInt(strOrderId));
    orderBean.setNpCustomerId(MiUtil.parseLong(strCodigoCliente));
    orderBean.setNpCreatedDate(MiUtil.getTimeStampBD("dd/MM/yyyy"));
    orderBean.setNpCreatedBy(strCreatedby);        
    orderBean.setNpSiteId(MiUtil.parseLong(strnpSite));
    orderBean.setNpSpecificationId(MiUtil.parseInt(strnpspecificationid));
    orderBean.setNpDivisionId(MiUtil.parseInt(strnpdivisionid));
    orderBean.setNpPaymentTerms(strNpPaymentTerms);
    orderBean.setNpProviderGrpId(MiUtil.parseInt(strNpProviderGrpId));

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

    orderBean.setNpFlagVep(iFlagVep);
    orderBean.setNpNumCuotas(iNumCuotasVEP);
    orderBean.setNpAmountVep(dTotalSalesPriceVEP);
    orderBean.setInitialQuota(dInicialQuotaVEP);
    orderBean.setNpPaymentTermsIQ(iNpPaymentTermsIQVEP);
    //FIN: PRY-1200 | AMENDEZ

      hshResult.put("orderBean",orderBean);
    
    ItemBean itemBean = null;
    String[]  pv_item_pk = request.getParameterValues("hdnItemId");
    String[]  pv_item_solution_Val = request.getParameterValues("hdnItemValuetxtItemSolution");
    String[]  pv_item_modality_Val = request.getParameterValues("hdnItemValuetxtItemEquipment") != null ? request.getParameterValues("hdnItemValuetxtItemEquipment"):request.getParameterValues("hdnItemValuetxtItemModality");
    //Se realiza este cambio para el Proyecto de Venta a Plazos, ya que se requiere que se inserte la modalidad de salida
    // y no el valor del npqequipment
    if (orderBean.getNpSpecificationId() == Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS){
       pv_item_modality_Val = request.getParameterValues("hdnItemValuetxtItemModality");
    }
    String[]  pv_item_PlanTarifario_Val = request.getParameterValues("hdnItemValuetxtItemRatePlan");
    String[]  pv_item_NewPlanTarifarioId_Val  = request.getParameterValues("hdnItemValuetxtItemNewRatePlanId");
    String[]  pv_item_Product_Val = request.getParameterValues("hdnItemValuetxtItemProduct");
    String[]  pv_item_Quantity_Val = request.getParameterValues("hdnItemValuetxtItemQuantity");
    String[]  pv_item_PriceCtaInscrip_Val = request.getParameterValues("hdnItemValuetxtItemPriceCtaInscrip");    
    String[]  pv_item_PriceException_Val = request.getParameterValues("hdnItemValuetxtItemPriceException");
    String[]  pv_item_ItemPhone = request.getParameterValues("hdnItemValuetxtItemPhone");    
    
    ItemServiceBean itemServiceBean = null;
    String[] pv_item_services = request.getParameterValues("item_services");
    Vector la_services  = null;
    
    ArrayList arrItems = null;
    ArrayList arrItemsServices = null;
    
    arrItems = new ArrayList();

    if( pv_item_pk != null ){
    
      int cantItems = pv_item_pk.length;
      for(int i=0; i<cantItems; i++){       
        hshAux = new HashMap();
        arrItemsServices = new ArrayList();
        itemBean = new ItemBean();
        la_services  = new Vector();
        
        itemBean.setNpsolutionid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_solution_Val,i)));
        itemBean.setNpmodalitysell(MiUtil.getStringObject(pv_item_modality_Val,i));
        
        itemBean.setNpplanid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_PlanTarifario_Val,i)));
        itemBean.setNporiginalplanid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_NewPlanTarifarioId_Val,i)));        
        itemBean.setNpproductid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_Product_Val,i)));
        itemBean.setNpquantity(MiUtil.parseInt(MiUtil.getStringObject(pv_item_Quantity_Val,i).equals("")?"1":MiUtil.getStringObject(pv_item_Quantity_Val,i)));
        itemBean.setNpprice(MiUtil.getStringObject(pv_item_PriceCtaInscrip_Val,i).trim().equalsIgnoreCase("")?"0.00":MiUtil.defaultString(MiUtil.getStringObject(pv_item_PriceCtaInscrip_Val,i),"0"));
        
        if (!MiUtil.getStringObject(pv_item_PriceException_Val,i).trim().equalsIgnoreCase(""))
        {
          itemBean.setNppriceexception(MiUtil.defaultString(MiUtil.getStringObject(pv_item_PriceException_Val,i),"0"));
        }                
        
        
        itemBean.setNpphone(MiUtil.getStringObject(pv_item_ItemPhone,i).trim());
        itemBean.setNpcreateddate(MiUtil.getDateBD("dd/MM/yyyy"));
        itemBean.setNpcreatedby(strCreatedby);        
        hshAux.put("itemBean",itemBean);
        String strCadena = MiUtil.getStringObject(pv_item_services,i);
        StringTokenizer tokens  = new StringTokenizer(strCadena,"|");
		  String    wv_npflagservicio_ant      = "";
        String    wv_npflagservicio_act      = "";
        String    resultFlafservice          = "";		  
        while(tokens.hasMoreTokens()){
          String aux = tokens.nextToken();
          la_services.addElement((String)aux);
        }
        for( int n = 0,j = 1;  j <= (la_services.size()/3); n=n+3,j++ ) {
        
          if (  ((String)la_services.elementAt(n+1)).equals("S") ) {
             wv_npflagservicio_ant = "X";
          }
          else {
             wv_npflagservicio_ant = ""; 
          }
          
          if (  ((String)la_services.elementAt(n+2)).equals("S") ) {
             wv_npflagservicio_act = "X";
          }
          else {
             wv_npflagservicio_act = ""; 
          }
          
          if ( wv_npflagservicio_ant.equals("X") && wv_npflagservicio_act.equals("X") ){
              resultFlafservice = "Contratado";
          }else if( wv_npflagservicio_ant.equals("X") && wv_npflagservicio_act.equals("") ){
              resultFlafservice = "Remover";
          }else if( wv_npflagservicio_ant.equals("") && wv_npflagservicio_act.equals("X") ){
              resultFlafservice = "Solicitado";
          }else{
              resultFlafservice = "";
          }		  
		  
          itemServiceBean = new ItemServiceBean();
          itemServiceBean.setNpserviceid(MiUtil.parseLong((String)la_services.elementAt(n)));    
          itemServiceBean.setNpaction(resultFlafservice);		
          arrItemsServices.add(itemServiceBean);
        }
        hshAux.put("arrItemsServices",arrItemsServices);
        arrItems.add(hshAux);
      }
    }
    hshResult.put("arrItems",arrItems);
    return hshResult;
  }

 /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
 * <br>Fecha: 22/04/2009
 * @see pe.com.nextel.dao.SpecificationDAO#getSolutionSpecificationList(long)      
 ************************************************************************************************************************************/ 
  public HashMap doReasonReject(long lSourceId, String strSource, int iflag, String strCreatedby) throws  Exception, SQLException{
    return objCreditEvaluationDAO.doReasonReject(lSourceId, strSource, iflag, strCreatedby);
  }  


  public HashMap doValidateCredit(long lSourceId,String strSource) throws Exception, SQLException {
    return objCreditEvaluationDAO.doValidateCredit(lSourceId, strSource);
  }
  
  public HashMap getLastCustomerScore(long lnorderid) throws Exception, SQLException {    
    return objCreditEvaluationDAO.getLastCustomerScore(lnorderid);
  }
}
