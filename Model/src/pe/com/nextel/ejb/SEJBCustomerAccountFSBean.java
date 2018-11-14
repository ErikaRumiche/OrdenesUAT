
package pe.com.nextel.ejb;

import org.apache.log4j.Logger;
import pe.com.entel.integracion.customeraccountfs.wsp_customeraccountfs.dto.CustomerAccountResponse;
import pe.com.entel.integracion.customeraccountfs.wsp_customeraccountfs.impl.CustomerAccountServiceImpl;
import pe.com.nextel.bean.RequestCustomerAccountFSBean;
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
public class SEJBCustomerAccountFSBean implements SessionBean {
	/**
	 *
	 */
	private static Logger logger = Logger.getLogger(SEJBCustomerAccountFSBean.class);
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

	public SEJBCustomerAccountFSBean() {
	}

	public HashMap getTipDocFS(RequestCustomerAccountFSBean request) throws SQLException, Exception, RemoteException {
		HashMap response = new HashMap();

		try{
			logger.info("[SEJBCustomerAccountFSBean] [getTipDocFS] Entradas al servicio CustomerAccountFS: ");
			logger.info("[SEJBCustomerAccountFSBean] [getTipDocFS] sn = " + request.getSn());
			CustomerAccountServiceImpl customerAccountServiceImpl = new CustomerAccountServiceImpl();
			CustomerAccountResponse customerAccountResponse = customerAccountServiceImpl.getCustomerAccount(request.getSn());

			logger.info("[SEJBCustomerAccountFSBean] [getTipDocFS] Salidas del servicio CustomerAccountFS: ");
			logger.info("[SEJBCustomerAccountFSBean] [getTipDocFS] status = " + customerAccountResponse.getResultStatus());
			logger.info("[SEJBCustomerAccountFSBean] [getTipDocFS] message = " + customerAccountResponse.getErrorDescription());
			logger.info("[SEJBCustomerAccountFSBean] [getTipDocFS] tipDoc = " + customerAccountResponse.getTipoDocumento());

			response.put("status", customerAccountResponse.getResultStatus());
			response.put("message", customerAccountResponse.getErrorDescription());
			response.put("tipDoc", customerAccountResponse.getTipoDocumento());
		}catch(Exception e){
			logger.error("[SEJBCustomerAccountFSBean] [getTipDocFS] Excepción del servicio CustomerAccountFS: "+e.getMessage());
		}

		return response;
	}

}