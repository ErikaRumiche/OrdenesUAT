package pe.com.nextel.ejb;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import pe.com.entel.esb.srv_customer.srv_adjustment.v1.Adjustment;
import pe.com.entel.esb.srv_customer.srv_adjustment.v1.AdjustmentPort;
import pe.com.entel.esb.srv_customer.srv_adjustment.v1.CalcularProrratedAmountRequest;
import pe.com.entel.esb.srv_customer.srv_adjustment.v1.CalcularProrratedAmountResponse;
import pe.com.entel.esb.srv_customer.srv_adjustment.v1.Item;
import pe.com.entel.esb.srv_customer.srv_adjustment.v1.ItemResult;
import pe.com.nextel.bean.ApportionmentBean;
import pe.com.nextel.bean.RequestApportionmentBean;
import pe.com.nextel.dao.ApportionmentDAO;
import pe.com.nextel.util.MiUtil;

public class SEJBApportionmentBean implements SessionBean {
	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(SEJBApportionmentBean.class);
	private static final long serialVersionUID = 1L;
	private SessionContext _context;
	ApportionmentDAO apportionmentDAO = null;
	
	public void ejbCreate() {
		apportionmentDAO  = new ApportionmentDAO();		
	}

	public void setSessionContext(SessionContext ctx) throws EJBException,
			RemoteException {
		_context = ctx;
	}

	public void ejbRemove() throws EJBException, RemoteException {
	}

	public void ejbActivate() throws EJBException, RemoteException {
	}

	public void ejbPassivate() throws EJBException, RemoteException {
	}
	
	public SEJBApportionmentBean() {
		apportionmentDAO  = new ApportionmentDAO();	
	}

	public HashMap getCalculatePayment(RequestApportionmentBean request) throws SQLException, Exception, RemoteException {
		logger.info("[INI] [" + request.getTrxId()+ "] SEJBApportionmentBean.getCalculatePayment");
		String jsonRequestInput = getTramaGson(request);
		logger.info("[request] " + jsonRequestInput);
		HashMap response = new HashMap();
		String concatPlanId="";
		int mountTotal = 0;
		double igv = 0;
		long status = -1;
		String mesagge = null;
		String jsonRequest = "";
		String jsonResponse = "";
		
		CalcularProrratedAmountResponse responseWS = null;
		CalcularProrratedAmountRequest requestWS = null;
		
		try {
			HashMap objHashMap = new HashMap();
			objHashMap = apportionmentDAO.getIgv();
			String message = (String) objHashMap.get("strMessage");
			if(message!=null){
				throw new Exception(message);
			}
			igv = (Double) objHashMap.get("dlIgv");
		logger.info("SEJBApportionmentBean/getCalculatePayment [igv] " + igv);
			
			List<ApportionmentBean> items = new ArrayList<ApportionmentBean>();
					
			// INVOCAR API PRORRATEO	
			
			Adjustment service = new Adjustment();
			AdjustmentPort port = service.getAdjustmentSOAP11Binding();
			requestWS = new CalcularProrratedAmountRequest();
			
			requestWS.setDocumentType(request.getTypeDocument());
			requestWS.setNumeroDocumento(request.getNroDocument());
			///JBALCAZAR PRY-1002
			objHashMap = apportionmentDAO.getCustomerBscsId(request.getCustomerId(),request.getSiteId());			
			message = (String) objHashMap.get("strMessage");
			if(message!=null){
				throw new Exception(message);
			}
			
			int customerBscsId =   MiUtil.parseInt(objHashMap.get("customerBscsId").toString());
			logger.info("SEJBApportionmentBean/getCalculatePayment [customerBscsId]" + customerBscsId);
			requestWS.setCustomerId(new Long(customerBscsId));
			requestWS.setBillCycle(request.getBillCycle());
			requestWS.setExternalId(request.getTrxId());
			
			for (ApportionmentBean itemBean : request.getItems()) {
				Item item = new Item();
				  objHashMap = apportionmentDAO.getTemplateId(itemBean.getPlanId());
				  logger.info("SEJBApportionmentBean/getCalculatePayment [templateid] " +objHashMap.get("templateid").toString());
				  int templateid =  MiUtil.parseInt(objHashMap.get("templateid").toString());
				  logger.info("SEJBApportionmentBean/getCalculatePayment [templateid] " + templateid); 
				item.setTemplateCoId(new Long(templateid));	
				itemBean.setTemplateId(""+templateid);
				requestWS.getItems().add(item);
				concatPlanId += itemBean.getPlanId() + "|";
			}
			
			jsonRequest = getTramaGson(requestWS);
			logger.info("SEJBApportionmentBean/getCalculatePayment [REQUEST WS] " + jsonRequest);		
			responseWS = port.calcularProrratedAmount(requestWS);
			jsonResponse = getTramaGson(responseWS);
			logger.info("SEJBApportionmentBean/getCalculatePayment [RESPONSE WS] " + jsonResponse);
			
			status = responseWS.getStatus();
			mesagge = responseWS.getMessage();
			boolean postpaid = (Boolean)responseWS.isHasPostpaid();
			logger.info("SEJBApportionmentBean/getCalculatePayment [status] " + status + " [mesagge] " + mesagge + " [postpaid]" + postpaid);
			if(status == 0L && !postpaid) {				
				if(responseWS.getItems() != null && responseWS.getItems().size() > 0) {
					logger.info("SEJBApportionmentBean/getCalculatePayment [Items.size] " + responseWS.getItems().size());
					for(ApportionmentBean itemBean : request.getItems()) {
					for(ItemResult bean : responseWS.getItems()) {
							long templateIdWS = bean.getTemplateCoId();							
							long templateIdBean = new Long(itemBean.getTemplateId());
							logger.info("SEJBApportionmentBean/getCalculatePayment [templateIdBean] " + templateIdBean + " [templateIdWS] " + templateIdWS +" [Quantity] " + itemBean.getQuantity() + " [Amount] " + bean.getAmount());
							if(templateIdWS==templateIdBean) {
								double priceIgv = (bean.getAmount() * igv)/100;
								int roundPrice =(int) Math.ceil(bean.getAmount() + priceIgv);
								roundPrice = roundPrice * itemBean.getQuantity();								
								mountTotal = mountTotal + roundPrice;
								
								itemBean.setTrxId(responseWS.getExternalId());
								itemBean.setCicloOrigen(responseWS.getOldBillCycle()!=null ? responseWS.getOldBillCycle() : "");
								itemBean.setCicloDestino(responseWS.getNewBillCycle());
								itemBean.setPrice(String.valueOf(bean.getAmount()));
								itemBean.setPriceIgv(String.valueOf(priceIgv));
								itemBean.setRoundedPrice(String.valueOf(roundPrice));
								itemBean.setIgv(igv);
								items.add(itemBean);
								break;
							}
						}
					}
					
					if(items.size() == 0) {
						throw new Exception("WS-API no devolvio item(s)");
					}
					response.put("mountTotal",mountTotal);
					response.put("items",items);
					
				} else {
					throw new Exception("WS-API no devolvio item(s)");
				}
			} else {
				if(postpaid) {
					logger.info("[CLIENTE POST-PAGO] " + mesagge);
					status = -1;
					mesagge= "cliente post-pago";					
				}
			}
			response.put("status",status);
			response.put("message",mesagge);
			response.put("postPaid",responseWS.isHasPostpaid());			
			response.put("OldBillCycle",responseWS.getOldBillCycle());
			response.put("NewBillCycle",responseWS.getNewBillCycle());
			
		} catch (Exception e) {
			logger.info(e.getMessage());
			status = -3;
			mesagge = e.getMessage();
			response.put("status",status);
			response.put("message",mesagge);			
		}
		
		try {
			logger.info("[TrxId]" + responseWS.getExternalId()+" [CustomerId] " + request.getCustomerId() + " [concatPlanId] "+ concatPlanId + " [User]" + request.getUser());
			String mesaggeDAO = apportionmentDAO.saveItemApiLog(responseWS.getExternalId(), request.getOrderId(), concatPlanId, String.valueOf(status), mesagge,request.getAccion() , jsonRequest, jsonResponse, request.getUser());
				logger.info("SEJBApportionmentBean/getCalculatePayment [mesaggeDAO] " + mesaggeDAO);
		} catch (Exception e) {
			logger.info("[ERROR] SEJBApportionmentBean/getCalculatePayment/saveItemApiLog : " + e.getMessage());

			concatPlanId = concatPlanId == null ? "" : concatPlanId;
			jsonRequest = jsonRequest == null ? jsonRequestInput : (jsonRequest.trim().length() > 0 ? jsonRequest : jsonRequestInput);
			jsonResponse = jsonResponse == null ? "" : jsonResponse;
			
			String mesaggeDAO = apportionmentDAO.saveItemApiLog(request.getTrxId(), request.getOrderId(), concatPlanId, String.valueOf(status), mesagge,request.getAccion(), jsonRequest, jsonResponse, request.getUser());
			logger.info("SEJBApportionmentBean/getCalculatePayment [mesaggeDAO] " + mesaggeDAO);
		}
		
		logger.info("[END] [" + request.getTrxId()+ "] SEJBApportionmentBean.getCalculatePayment");
		return response;
	}
	
	public boolean getCanalProrrateo(int hdnSpecification, String strGeneratorType, String strUser) throws SQLException, Exception {
		logger.info("SEJBApportionmentBean/getCanalProrrateo -> hdnSpecification:" + hdnSpecification);
		logger.info("SEJBApportionmentBean/getCanalProrrateo -> tipoDocumento:" + strGeneratorType);
		logger.info("SEJBApportionmentBean/getCanalProrrateo -> numeroDocumento:" + strUser);
		
		boolean resultado = false;
		HashMap hashMapDAO = null;
		
		hashMapDAO = this.apportionmentDAO.getCanalProrrateo(hdnSpecification, strGeneratorType, strUser);
		
		String message = (String)hashMapDAO.get("strMessage");
		if (message != null) {
		  throw new Exception(message);
		}
		String defaultImbox = MiUtil.getString(hashMapDAO.get("defaultImbox").toString());
		logger.info("SEJBApportionmentBean/getCalculatePayment [getCanalProrrateo]" + defaultImbox);
		if ("TIENDA01".equals(defaultImbox)) {
		  resultado = true;
		}
		return resultado;
	  }
	
	private String getTramaGson(Object obj) {
		Gson gson = new Gson();
		return gson.toJson(obj);
	}

}