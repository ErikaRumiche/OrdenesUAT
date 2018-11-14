package pe.com.nextel.service;

import javax.naming.Context;

import javax.rmi.PortableRemoteObject;

import pe.com.nextel.ejb.SEJBCustomerRemote;
import pe.com.nextel.ejb.SEJBCustomerRemoteHome;
import pe.com.nextel.util.MiUtil;


//import wsp.contractcreate.proxy.ContractCreateOut;

/**
 * Motivo:  Clase Service que contendrá el algoritmo de Creación de Clientes
 * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>,
 * <br>Fecha: 01/10/2007
 * @see GenericService
 */
public class AccountService extends GenericService {
	
	public static SEJBCustomerRemote getSEJBCustomerRemote() {
    try {
      final Context context = MiUtil.getInitialContext();
      final SEJBCustomerRemoteHome sEJBCustomerRemoteHome = 
            (SEJBCustomerRemoteHome) PortableRemoteObject.narrow( context.lookup( "SEJBCustomer" ), SEJBCustomerRemoteHome.class );
      SEJBCustomerRemote sEJBCustomerRemote;
                         sEJBCustomerRemote = sEJBCustomerRemoteHome.create();
      return sEJBCustomerRemote;
    }catch(Exception e) {
      logger.error(formatException(e));
		return null;
    }
	}
	
	/**
   * Motivo: Creación de las cuentas del cliente según una orden, mediante BPEL
   * <br>Realizado por: <a href="mailto:cristian.espinoza@nextel.com.pe">Cristian Espinoza</a>
   * <br>Fecha: 26/02/2008
	 * 
	 * @param lOrderId Id de la Orden
	 */
  /*public HashMap createCustomerBSCS(long lOrderId) {
    HashMap hshDataMap = new HashMap();
      try {
        hshDataMap = getSEJBCustomerRemote().createCustomerBSCS(lOrderId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;	
  }*/
  
  /**
   * Motivo: Algoritmo de Creación de las Contratos BSCS
   * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
   * <br>Fecha: 16/12/2007
	 * 
	 * @param lOrderId Id de la Orden
	 */	
	/*public HashMap createContractBSCS(HashMap hshParametrosMap, RetailForm retailForm) {
		HashMap   hshDataMap  = new HashMap();
		String    strErrMsg   = null;
		ArrayList arrContractInfoList = new ArrayList();
		try {
			//SEJBCustomerRemote objSEJBCustomerRemote = getSEJBCustomerRemote();
			for (int i = 0; i < retailForm.getHdnItem().length; i++) {
				if (StringUtils.isNotBlank(retailForm.getHdnItem()[i])) {
					hshParametrosMap.put("strPlanId", retailForm.getHdnPlanTarifario()[i]);
					hshParametrosMap.put("strImei", retailForm.getTxtIMEI()[i]);
					HashMap hshCreateContractMap = getSEJBCustomerRemote().getCreateContract(hshParametrosMap);
					if(logger.isDebugEnabled()) {
						logger.debug("Iniciando la invocación del createContract - tiene invocación BPEL ");
					}
					ContractCreateOut objContractCreateOut = getSEJBCustomerRemote().createContract(hshCreateContractMap);
					String strCodError = objContractCreateOut.getCodError();
					strErrMsg = objContractCreateOut.getErrMsg();
					if(StringUtils.isBlank(strCodError) || strCodError.equals("0")) {
						//Asignando los valores de Salida
						strErrMsg=null;
						HashMap hshContractInfoMap = new HashMap();
						hshContractInfoMap.put("strRadio", StringUtils.defaultString(objContractCreateOut.getDispatch()));
						hshContractInfoMap.put("strTelefono", StringUtils.defaultString(objContractCreateOut.getTelefonia()));
						hshContractInfoMap.put("lContratoId", objContractCreateOut.getCONTRACT_NEW_OUT().getCO_ID());
						hshContractInfoMap.put("hdnItem", retailForm.getHdnItem()[i]);
						arrContractInfoList.add(hshContractInfoMap);
					} else {
						if(logger.isDebugEnabled()) {
							logger.debug("strCodError: "+strCodError);
							logger.debug("strErrMsg: "+strErrMsg);
						}
						throw new BpelException(strCodError, strErrMsg);
					}
				}
			}
			hshDataMap.put("arrContractInfoList", arrContractInfoList);
			hshDataMap.put("strMessage", strErrMsg);
		} catch (Throwable t) {
			strErrMsg="Error en la creación de contratos en BSCS";			
			//manageCatch(hshDataMap, t);
			hshDataMap.put("strMessage", strErrMsg);
		}
		
		return hshDataMap;
	}*/
	


}