
package pe.com.nextel.ejb;

import org.apache.log4j.Logger;
import pe.com.entel.integracion.contractfs.wsp_contractfs.dto.ContractResponse;
import pe.com.entel.integracion.contractfs.wsp_contractfs.impl.ContractServiceImpl;
import pe.com.nextel.bean.RequestContractFSBean;
import pe.com.nextel.util.Constante;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Código: TDECONV003-8
 * <br>Realizado por: PZACARIAS
 * <br>Fecha: 18/06/2018
 */
public class SEJBContractFSBean implements SessionBean {
	/**
	 *
	 */
	private static Logger logger = Logger.getLogger(SEJBContractFSBean.class);
	private static final long serialVersionUID = 1L;
	private SessionContext _context;

	public void ejbCreate() {
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

	public SEJBContractFSBean() {
	}

	public HashMap getSIM_MSISDN_FS(RequestContractFSBean request) throws SQLException, Exception, RemoteException {
		HashMap response = new HashMap();

		try{
			logger.info("[SEJBContractFSBean] [getSIM_MSISDN_FS] Entradas al servicio ContractFS: ");
			logger.info("[SEJBContractFSBean] [getSIM_MSISDN_FS] sn = " + request.getSn());
			ContractServiceImpl contractServiceImpl = new ContractServiceImpl();
			ContractResponse contractResponse = contractServiceImpl.getContract(request.getSn());

			logger.info("[SEJBContractFSBean] [getSIM_MSISDN_FS] Salidas del servicio ContractFS: ");
			logger.info("[SEJBContractFSBean] [getSIM_MSISDN_FS] status = " + contractResponse.getResultStatus());
			logger.info("[SEJBContractFSBean] [getSIM_MSISDN_FS] message = " + contractResponse.getErrorDescription());
			logger.info("[SEJBContractFSBean] [getSIM_MSISDN_FS] sim = " + contractResponse.getSIM());
			logger.info("[SEJBContractFSBean] [getSIM_MSISDN_FS] contractStatus = " + contractResponse.getContractStatus());
			logger.info("[SEJBContractFSBean] [getSIM_MSISDN_FS] plmnCode = " + contractResponse.getOperatorPlmnCode());

			response.put("status", contractResponse.getResultStatus());
			response.put("message", contractResponse.getErrorDescription());
			response.put("sim", contractResponse.getSIM());
			response.put("contractStatus", contractResponse.getContractStatus());
			response.put("plmnCode", contractResponse.getOperatorPlmnCode());
		}catch(Exception e){
			logger.error("[SEJBContractFSBean] [getSIM_MSISDN_FS] Excepción del servicio ContractFS: "+e.getMessage());
		}

		return response;
	}

}