package pe.com.nextel.ejb;

import java.sql.SQLException;

import java.util.HashMap;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;
import pe.com.nextel.bean.OrderBean;
import pe.com.nextel.bean.OrderDetailBean;
import pe.com.nextel.dao.ItemDAO;
import pe.com.nextel.dao.JerarquiaDAO;
import pe.com.nextel.dao.OrderDAO;
import pe.com.nextel.form.OrderSearchForm;
import pe.com.nextel.form.PCMForm;
import pe.com.nextel.form.RetentionForm;


public class SEJBOrderSearchBean implements SessionBean 
{
	private SessionContext _context;
    OrderDAO objOrderDAO = null;
	JerarquiaDAO objJerarquiaDAO = null;
    ItemDAO objItemDAO = null; /*jsalazar - modif hpptt # 1 - 29/09/2010 -*/ 
    protected static Logger logger = Logger.getLogger(SEJBOrderSearchBean.class);
	
	public void ejbCreate() {
		objOrderDAO = new OrderDAO();
		objJerarquiaDAO = new JerarquiaDAO();
	    /*jsalazar - modif hpptt # 1 - 29/09/2010 - inicio*/  
	    objItemDAO = new ItemDAO();
	    /*jsalazar - modif hpptt # 1 - 29/09/2010 - fin*/  
	}
	
	public void ejbActivate() {
	}

	public void ejbPassivate() {
	}

	public void ejbRemove() {
	}

	public void setSessionContext(SessionContext ctx) {
		_context = ctx;
	}

	public HashMap getOrderList(OrderSearchForm objOrderSearchForm) throws SQLException, Exception {	
		return objOrderDAO.getOrderList(objOrderSearchForm);
	}

	//LSILVA : HPPTT - Internal Order List - Obtiene la lista de Ordenes internas asociadas a una Orden principal
	public HashMap getInternalOrderList(long plParentOrderId, long plNumRegistros, long plNumPagina) throws SQLException, Exception {	
		return objOrderDAO.getInternalOrderList(plParentOrderId, plNumRegistros, plNumPagina);
	}

	//LSILVA : HPPTT - Parent Order List - Obtiene la lista de Ordenes padres de una Orden dada La primera Orden de la lista
	//es la mas ancestral.
	public HashMap getParentOrderList(long plOrderId) throws SQLException, Exception {	
		return objOrderDAO.getParentOrderList(plOrderId);
	}
	
  public HashMap existOrder(long lOrderId) throws SQLException, Exception {
    return objOrderDAO.existOrder(lOrderId);
  }
  
  //AGAMARRA
  public HashMap getDataField(int an_ruleid, String av_retriverepresentative, 
    int an_salesstructid, int an_providergrpid) throws SQLException, Exception{
      return objJerarquiaDAO.getDataField(an_ruleid, av_retriverepresentative, an_salesstructid, an_providergrpid);
  }
  
  //AGAMARRA
  public String checkStructRule(int ruleid, String strNpsalesstructid) throws  SQLException, Exception{
    return objJerarquiaDAO.checkStructRule(ruleid, strNpsalesstructid);
  }
  
  public HashMap getFinalSuspensionList(HashMap hshParameters) throws SQLException, Exception{
    return objOrderDAO.getFinalSuspensionList(hshParameters);
  }
   
  public HashMap getFinalSuspDetailList(HashMap hshParameters) throws SQLException, Exception{
    return objOrderDAO.getFinalSuspDetailList(hshParameters);
  }
     
  public HashMap getCalArea() throws SQLException, Exception {
		return objOrderDAO.getCalArea();
	}
  
  public HashMap getSuspensionReason() throws SQLException, Exception {
		return objOrderDAO.getSuspensionReason();
	}
  
  public HashMap getRetentionTool() throws SQLException, Exception {
		return objOrderDAO.getRetentionTool();
	}
  
  public HashMap getClientType() throws SQLException, Exception {
		return objOrderDAO.getClientType();
	}
  
  //public HashMap getGeneralSuspensionList(HashMap hshParameters) throws SQLException, Exception{
  //  return objOrderDAO.getGeneralSuspensionList(hshParameters);
  //}
  
  public HashMap getDetailedSuspensionList(HashMap hshParameters) throws SQLException, Exception{
    return objOrderDAO.getDetailedSuspensionList(hshParameters);
  }  
  
  public HashMap getEstadoPMC(String dominioTabla) throws SQLException, Exception{
    return objOrderDAO.getEstadoPMC(dominioTabla);
  }
  
  public HashMap getActionID(String dominioTabla) throws SQLException, Exception{
    return objOrderDAO.getActionID(dominioTabla);
  }
  
  public HashMap getRetentionList(RetentionForm retentionForm) throws SQLException, Exception {	
		return objOrderDAO.getRetentionList(retentionForm);
	}
  
  public HashMap getPCMList(PCMForm pcmForm) throws SQLException, Exception {	
		return objOrderDAO.getPCMList(pcmForm);
	}
  
  //AGAMARRA
  public int getParentForAssist(int an_salesstructdefaultid) throws  SQLException, Exception{
    return objJerarquiaDAO.getParentForAssist(an_salesstructdefaultid);

  }
  
  //AGAMARRA
  public int getPrvdStructAssist(int an_salesstructdefaultid) throws  SQLException, Exception{
    return objJerarquiaDAO.getPrvdStructAssist(an_salesstructdefaultid);
  }
  
  //AGAMARRA
  public String getLastPosition(int an_salesstructdefaultid) throws  SQLException, Exception{
    return objJerarquiaDAO.getLastPosition(an_salesstructdefaultid);
  }
  /**
  *  Method : getSuspensionType
  *  Purpose: Obtiene los tipos de Suspension.
  *  Developer       Fecha       Comentario
  *  =============   ==========  ======================================================================
  *  Miguel Jurado   15/10/2009  Creacion
  **/
  public HashMap getSuspensionType() throws  SQLException, Exception {
    return objOrderDAO.getSuspensionType("SUSPENSION_TYPE", null, null, "1", null, null, null);         
  }

   /*jsalazar - modif hpptt # 1 - 29/09/2010 - inicio*/ 
  public HashMap getItemServicePendingList(long lOrderId, long lItemId) throws Exception, SQLException
  {
    return objItemDAO.getItemServicePendingList(lOrderId, lItemId);
  }
  /*jsalazar - modif hpptt # 1 - 29/09/2010 - fin*/ 
  
  //JLIMAYMANTA
  public OrderDetailBean getSearchOrderById(long orderIdi,String userLogin,int paymenttype) throws  SQLException, Exception{
      logger.info("SEJBOrderSearchBean ][getSearchOrderById][orderIdi][" +orderIdi+"]"); 
      logger.info("SEJBOrderSearchBean ][getSearchOrderById][userLogin][" +userLogin+"]");
      //INICIO: AMENDEZ | PRY-1141
      logger.info("SEJBOrderSearchBean ][getSearchOrderById][paymenttype][" +paymenttype+"]");  
      //FIN: AMENDEZ | PRY-1141
    return objOrderDAO.getSearchOrderById(orderIdi,userLogin,paymenttype);
  }
    //JLIMAYMANTA
    public HashMap saveOrderPaymentTE(long orderIdi,double monto,String hdnRa,String hdnVoucher,String hdnComentario,String hdnNumLogin,String hdnUser,int paymenttype,long paymentOrderQuotaId)
  throws Exception {
        //INICIO: AMENDEZ | PRY-1141
        logger.info("SEJBOrderSearchBean ][saveOrderPaymentTE][orderIdi][" +orderIdi+"]"); 
        logger.info("SEJBOrderSearchBean ][saveOrderPaymentTE][monto][" +monto+"]"); 
        logger.info("SEJBOrderSearchBean ][saveOrderPaymentTE][hdnRa][" +hdnRa+"]"); 
        logger.info("SEJBOrderSearchBean ][saveOrderPaymentTE][hdnVoucher][" +hdnVoucher+"]"); 
        logger.info("SEJBOrderSearchBean ][saveOrderPaymentTE][hdnComentario][" +hdnComentario+"]");
        logger.info("SEJBOrderSearchBean ][saveOrderPaymentTE][hdnNumLogin][" +hdnNumLogin+"]"); 
        logger.info("SEJBOrderSearchBean ][saveOrderPaymentTE][hdnUser][" +hdnUser+"]"); 
        logger.info("SEJBOrderSearchBean ][saveOrderPaymentTE][paymenttype][" +paymenttype+"]"); 
        logger.info("SEJBOrderSearchBean ][saveOrderPaymentTE][paymentOrderQuotaId][" +paymentOrderQuotaId+"]"); 
        //FIN: AMENDEZ | PRY-1141
        return objOrderDAO.saveOrderPaymentTE(orderIdi,monto,hdnRa,hdnVoucher,hdnComentario,hdnNumLogin,hdnUser,paymenttype,paymentOrderQuotaId);
  }   
    //EFLORES Se agregan parametros adicionales
    public HashMap saveOrderPaymentTE(long orderIdi,double monto,String hdnRa,String hdnVoucher,String hdnComentario,String hdnNumLogin,String hdnUser,Integer hdnFlgVep,Double txtCuotaInicial, Double hdnMontoFinanciado,Integer hdnNumCuotas)
            throws Exception {
        return objOrderDAO.saveOrderPaymentTE(orderIdi,monto,hdnRa,hdnVoucher,hdnComentario,hdnNumLogin,hdnUser,hdnFlgVep,txtCuotaInicial, hdnMontoFinanciado,hdnNumCuotas);
  }
    /* AYATACO - Inicio */
    public HashMap validateOrderExist(String npsource, int npsourceid)throws SQLException, Exception {
        return objOrderDAO.validateOrderExist(npsource, npsourceid);
    }
    /* AYATACO - Fin */
 

    /**
     * @author AMENDEZ
     * @project PRY-1141
     * Metodo   Lista tipos de pago para registro de pago de TPF
     * @return
     */
    public HashMap lstPaymentType() {
        logger.info("*************************** INICIO SEJBOrderSearchBean > lstPaymentType ***************************");
        HashMap hshMap = new HashMap();

        try{
            hshMap =  objOrderDAO.lstPaymentType();

        }catch (Exception e){
            hshMap.put("arrayList",null);
            hshMap.put("strMessage",e.getMessage());
            logger.error(e);
        }
        logger.info("*************************** FIN SEJBOrderSearchBean > lstPaymentType ***************************");
        return hshMap;
    } 
}