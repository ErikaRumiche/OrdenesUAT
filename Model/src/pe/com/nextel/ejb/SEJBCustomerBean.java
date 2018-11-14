package pe.com.nextel.ejb;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import pe.com.nextel.bean.AddressWriteStruct;
import pe.com.nextel.bean.BillingAccountWriteStruct;
import pe.com.nextel.bean.CustomerBean;
import pe.com.nextel.bean.CustomerStruct;
import pe.com.nextel.bean.MessageVO;
import pe.com.nextel.bo.AddressWriteManage;
import pe.com.nextel.bo.BillingAccountManage;
import pe.com.nextel.bo.CustomerManage;
import pe.com.nextel.dao.AccountDAO;
import pe.com.nextel.dao.CustomerDAO;
import pe.com.nextel.dao.FraudDAO;
import pe.com.nextel.dao.ProposedDAO;
import pe.com.nextel.dao.Proveedor;
import pe.com.nextel.dao.SubRegCustomerInfoDAO;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.GenericObject;
import pe.com.nextel.util.MiUtil;

import wsp.customerread.proxy.BPEL_CustomerReadPortClient;
import wsp.customerread.proxy.be.CustomerReadBeanIn;
import wsp.customerread.proxy.be.CustomerReadBeanOut;




/*
import wsp.contractcreate.proxy.BPEL_ContractCreatePortClient;
import wsp.contractcreate.proxy.BPEL_ContractCreateProcessRequest;

import wsp.contractcreate.proxy.BPEL_ContractCreateProcessResponse;
import wsp.contractcreate.proxy.ContractCreateIn;
import wsp.contractcreate.proxy.ContractCreateOut;
import wsp.contractcreate.proxy.ContractDeviceAddIn;
import wsp.contractcreate.proxy.ContractNewIn;
import wsp.contractcreate.proxy.ContractServiceResourcesWriteIn;
import wsp.contractcreate.proxy.DirectoryNumbersCSRWIn;*/
/*import wsp.customerhierarchywrite.proxy.BPEL_CustHierarchyWritePortClient;
import wsp.customerhierarchywrite.proxy.types.customerhierarchywrite.types.CustomerHierarchyWriteIn;
import wsp.customerhierarchywrite.proxy.types.customerhierarchywrite.types.CustomerHierarchyWriteOut;
import wsp.customerhierarchywrite.proxy.types.customerhierarchywrite.types.WriteCustomerHierarchyElement;
import wsp.customerhierarchywrite.proxy.types.customerhierarchywrite.types.WriteCustomerHierarchyResponseElement;
*/

public class SEJBCustomerBean extends GenericObject implements SessionBean {

    private SessionContext _context;
    private CustomerDAO objCustomerDAO = null;
    private AccountDAO objAccountDAO = null;
    private FraudDAO   objFraudDAO  = null;
    private ProposedDAO objProposedDAO= null;
    private SubRegCustomerInfoDAO objSubRegCustomerInfoDAO= null;
   

    public void ejbCreate() {
		objCustomerDAO = new CustomerDAO();
		objAccountDAO = new AccountDAO();
    objFraudDAO  = new FraudDAO();
		objProposedDAO = new ProposedDAO();
    objSubRegCustomerInfoDAO = new SubRegCustomerInfoDAO();
    
    }

    public void setSessionContext(SessionContext context) throws EJBException {
        _context = context;
    }

    public void ejbRemove() throws EJBException {
        System.out.println("[SEJBCustomerBean][ejbRemove()]");
    }

    public void ejbActivate() throws EJBException {
        System.out.println("[SEJBCustomerBean][ejbActivate()]");
    }

    public void ejbPassivate() throws EJBException {
        System.out.println("[SEJBCustomerBean][ejbPassivate()]");
    }
    
    public Hashtable CustomerDAOgetCompanyInfo(long codCliente) throws Exception,SQLException {
        /**
         * Incluir reglas de negocio
         */
        return objCustomerDAO.getCompanyInfo(codCliente);
    }

  public HashMap CustomerDAOgetCustomerAddress(long intObjectId, long longRegionId, String strObjectType, String strGeneratortype) throws Exception,SQLException {
        return objCustomerDAO.getCustomerAddress(intObjectId,longRegionId,strObjectType,strGeneratortype);
  }

  public HashMap CustomerDAOgetAddressDelivery(long idObjectId, String strObjectType, String strGeneratorType, String strRegionId)  throws Exception,SQLException {
        return objCustomerDAO.getAddressDelivery(idObjectId,strObjectType,strGeneratorType,strRegionId);
  }

  public int CustomerDAOgetUnknowSite(long intObjectId, String strObjectType)  throws Exception,SQLException {
        return objCustomerDAO.getUnknowSite(intObjectId,strObjectType);
  }
  
  public HashMap CustomerDAOgetCustomerData(long iCustomerId, String strSwcreatedBy, long iRegionid,int iUserId,int iAppId,String strGeneratorId,String strGeneratorType) throws Exception,SQLException {
        return objCustomerDAO.getCustomerData(iCustomerId,strSwcreatedBy,iRegionid,iUserId,iAppId,strGeneratorId,strGeneratorType);
  }
  
  public HashMap getCustomerSites(long intCustomerId,String strEstadoSite) throws Exception,SQLException {
        return objCustomerDAO.getCustomerSites(intCustomerId, strEstadoSite);
  }
  
  public HashMap getCustomerSearch(String strTipoDocumento, 
                                               String strNumeroDocumento,
                                               String strRazonSocial,
                                               String strNombreCliente,
                                               String strTipoCliente,
                                               long intCustomerId,
                                               long intRegionId,
                                               String strSessionCode,
                                               int intSessionLevel,
                                               int lPageSelected,
                                               int iProGrpId,
                                               String strLogin,
                                               int intSalesStructId,
                                               int iUserId1,
                                               int iAppId1
                                               )  throws Exception, SQLException{

        System.out.println("iProGrpId:"+iProGrpId);  
        return objCustomerDAO.
              getCustomerSearch(strTipoDocumento,strNumeroDocumento,strRazonSocial,
              strNombreCliente,strTipoCliente,intCustomerId,intRegionId,strSessionCode,
              intSessionLevel,lPageSelected,iProGrpId,strLogin, intSalesStructId,
              iUserId1, iAppId1);
  }
  
 public HashMap CustomerDAOgetCustomerJava(long longAppId, String strPhoneNumber, long longContractId, String strIMEI, String strSIM,String strNumeroRadio) throws  SQLException, Exception{
    return objCustomerDAO.getCustomerJava(longAppId,strPhoneNumber,longContractId,strIMEI,strSIM,strNumeroRadio);
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

public HashMap getCustomerData(long iNpCustomerId)
throws Exception 
{
   return objCustomerDAO.getCustomerData(iNpCustomerId); 
}

public HashMap getCustomerData2(long iCustomerId, String strSwcreatedBy, long iRegionid) throws Exception {
   return objCustomerDAO.getCustomerData2(iCustomerId,strSwcreatedBy,iRegionid);
}


public HashMap getCustomerContacts2(long intObjectId, String strObjectType) throws Exception {
   return objCustomerDAO.getCustomerContacts2(intObjectId, strObjectType);
}

public HashMap getCustomerAddress2(long intObjectId, long longRegionId,String strObjectType) throws Exception {
   return objCustomerDAO.getCustomerAddress2(intObjectId, longRegionId,strObjectType);
}

public HashMap getCustomerSitesList(long lCustomerId,long lOrderId,long lOportunityId,String strEstadoSite) throws SQLException {
   return objCustomerDAO.getCustomerSitesList(lCustomerId,lOrderId, lOportunityId,strEstadoSite);
}   

public String getCustomerType(long lCustomerId) 
throws SQLException{
   return objCustomerDAO.getCustomerType(lCustomerId);
} 

public ArrayList getCustomerContactAll(long lCustomerId, int iResultado) 
throws SQLException{
   return objCustomerDAO.getCustomerContactAll(lCustomerId,iResultado);
}  

public ArrayList getSiteContactAll(long lCustomerId,long lSiteId, int iResultado)   
throws SQLException{
   return objCustomerDAO.getSiteContactAll(lCustomerId,lSiteId,iResultado);
}

public HashMap getDeliveryAddress(long lObjetId,String strObjectType) 
throws SQLException{
   return objCustomerDAO.getDeliveryAddress(lObjetId,strObjectType);
}    

public HashMap getAddressByRegion(long lObjectId,String strObjectType,long lSellerRegId) 
throws SQLException{
   return objCustomerDAO.getAddressByRegion(lObjectId,strObjectType,lSellerRegId);
}    

public HashMap getAddress(long lObjectTypeId, String strObjectType,String strType) 
throws SQLException{
   return objCustomerDAO.getAddress(lObjectTypeId,strObjectType,strType);
}  

public HashMap getContact(long lObjectTypeId, String strObjectType,String strType)
throws SQLException{
   return objCustomerDAO.getContact(lObjectTypeId,strObjectType,strType);
}  

public HashMap getAddressChange(long lOrderId, int iTypeObject,long lObjectId)
throws SQLException{
   return objCustomerDAO.getAddressChange(lOrderId,iTypeObject,lObjectId);
}

public HashMap getContactChange(long lOrderId, int iTypeObject,long lObjectId)
throws SQLException{
   return objCustomerDAO.getContactChange(lOrderId,iTypeObject,lObjectId);
}

public HashMap getHeaderChange(long lOrderId, int iTypeObject,long lObjectId)
throws SQLException{
   return objCustomerDAO.getHeaderChange(lOrderId,iTypeObject,lObjectId);
}

public long getCustomerIdBSCS(long lCustomerId, String strObjectType)
throws SQLException{
   return objCustomerDAO.getCustomerIdBSCS(lCustomerId,strObjectType);
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
public Hashtable CustomerDAOgetCustomerIdCrmByBSCS(long intCodigoCliente) throws Exception,SQLException{
   return objCustomerDAO.getCustomerIdCrmByBSCS(intCodigoCliente);
}

public Hashtable CustomerDAOgetCustPatternQty(String strNombreCompania) throws Exception,SQLException{
   return objCustomerDAO.getCustPatternQty(strNombreCompania);
}

public Hashtable CustomerDAOgetCustRucQty(String strRuc) throws Exception,SQLException{
   return objCustomerDAO.getCustRucQty(strRuc);
}

public Hashtable CustomerDAOgetSiteData(long intSiteid, String strCreatedby, long intUserid, long intAppid, long intRegionid,
                                        String strGeneratortype, String strGeneratorId) throws Exception,SQLException{
   return objCustomerDAO.getSiteData(intSiteid,strCreatedby,intUserid,intAppid,intRegionid,strGeneratortype, strGeneratorId);
}   

	/***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES - INICIO
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 21/11/2007
     ***********************************************************************
     ***********************************************************************
     **********************************************************************/
	public MessageVO generateCustomer(HashMap hshParametrosMap) throws Exception {
		logger.debug("Entro");
    Connection conn = null;
		try {
			Long lCustomerId = new Long(hshParametrosMap.get("customerId").toString());
			CustomerManage objCustomerManage = new CustomerManage();
			CustomerStruct objCustomerStruct = new CustomerStruct();
			
			objCustomerStruct.setWn_npClienteId(new BigDecimal(lCustomerId.longValue()));
			objCustomerStruct.setWn_CS_ID_HIGHT(MiUtil.defaultString(hshParametrosMap.get("csId"),""));
			objCustomerStruct.setWv_TablaCliente((String) hshParametrosMap.get("tablaCliente"));
			objCustomerStruct.setWv_TipoEstructura((String) hshParametrosMap.get("tipoCliente"));
			objCustomerStruct.setWn_Nivel(new BigDecimal((String) hshParametrosMap.get("nivel")));
			objCustomerStruct.setWv_ResponsablePago((String) hshParametrosMap.get("responsablePago"));
			objCustomerStruct.setWn_grupocliente(new BigDecimal((String) hshParametrosMap.get("grupoCliente")));

			String strIdAPP = Constante.APPLICATION_ID_BPEL;
      conn = Proveedor.getConnection();
			MessageVO messageVO = objCustomerManage.createCustomer(objCustomerStruct, strIdAPP, conn);
			return messageVO;
		} catch(ClassCastException cce) {
			cce.printStackTrace();
		}finally{
      if( conn != null ) { conn.close(); conn = null; }
    }
		return null;
	}
	
	public AddressWriteStruct generateAddress(HashMap hshParametrosMap, Long lCustomerId, String strCsId) throws Exception {
		Connection conn = null;
    try {
			logger.info("Entro");
			AddressWriteManage objAddressWriteManage = new AddressWriteManage();
			AddressWriteStruct addressWriteStruct = new AddressWriteStruct();
			addressWriteStruct.setNpBillaccNewId(new BigDecimal(hshParametrosMap.get("billingAccountNewId").toString()));
			addressWriteStruct.setCustomerId(new BigDecimal(lCustomerId.longValue()));
			addressWriteStruct.setCsID(strCsId);
			String strIdAPP = Constante.APPLICATION_ID_BPEL;
      conn = Proveedor.getConnection();
			AddressWriteStruct addressWriteResult  = objAddressWriteManage.createAddress(addressWriteStruct, strIdAPP, conn);
			return addressWriteResult;
		} catch(ClassCastException cce) {
			cce.printStackTrace();
		}finally{
      if( conn != null ) { conn.close(); conn = null; }
    }
		return null;
	}
	
	public BillingAccountWriteStruct generateBillingAccount(HashMap hshParametrosMap, Long lContactSeqNo, String strCsId) throws Exception {
		Connection conn = null;
    try{
    BillingAccountManage billingAccountManage = new BillingAccountManage();
        BillingAccountWriteStruct billingAccountWriteStruct = new BillingAccountWriteStruct();
        billingAccountWriteStruct.setNpBillingAccountName((String) hshParametrosMap.get("billingAccountName"));
        billingAccountWriteStruct.setContactSeqNo(new BigDecimal(lContactSeqNo.longValue()));
        billingAccountWriteStruct.setCsID(new BigDecimal(strCsId));
        String strIdAPP = Constante.APPLICATION_ID_BPEL;
        conn  = Proveedor.getConnection();
        
      return billingAccountManage.createBillingAccount(billingAccountWriteStruct, strIdAPP, conn);
    
    }catch(Exception ex)    {
      ex.printStackTrace();
      return null;
    }finally{
      if( conn != null ) { conn.close(); conn = null; }
    }
	}

	public String synchronizeCustomerBSCS(HashMap hshCustomerMap) throws SQLException, Exception {
		return objAccountDAO.synchronizeCustomerBSCS(hshCustomerMap);
	}
	
	public String synchronizeBillingAccountBSCS(HashMap hshBillingAccountMap) throws SQLException, Exception {
		return objAccountDAO.synchronizeBillingAccountBSCS(hshBillingAccountMap);
	}
	
  /*
	public CustomerHierarchyWriteOut joinCustomersByHierarchy(String strCsIdHight, String strCsId) throws SQLException, Exception {
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		//Se definen los objetos que contendran los datos de entrada 
        WriteCustomerHierarchyElement input = new WriteCustomerHierarchyElement();
        CustomerHierarchyWriteIn in = new CustomerHierarchyWriteIn();
        //Se define el objeto de Salida
        WriteCustomerHierarchyResponseElement resultOUT = new WriteCustomerHierarchyResponseElement();
		CustomerHierarchyWriteOut out = new CustomerHierarchyWriteOut();

        //Seteamos los parametros de input deseados
        //PARAMETROS REQUERIDOS//
        in.setIdApp(Constante.APPLICATION_ID_BPEL); //Identificador de Aplicacion
        //PARAMETROS ADICIONALES//
        in.setCS_ID(new Long(strCsIdHight));
        in.setCS_ID_HIGH(new Long(strCsId));

        //Seteamos la variable input
        input.setCustomerHierarchyWriteIn(in);        
        //Se define el objeto "Invoker" que hara la llamada del Proceso BPEL 
        BPEL_CustHierarchyWritePortClient invokeCustomerHierarchyWrite = new BPEL_CustHierarchyWritePortClient();
        System.out.println("Call Proxy: "+invokeCustomerHierarchyWrite.getEndpoint());

        //Se utiliza el metodo "process" que llamara al proceso BPEL y se almacena en la variable resultOUT
        long ini = System.currentTimeMillis();
        resultOUT = invokeCustomerHierarchyWrite.process(input);
        long fin = System.currentTimeMillis();
        long tot = 0;
        tot = fin - ini;
		if(logger.isDebugEnabled())
			logger.debug("Tiempo Invoke: " + tot);

        //Validamos Codigo de Error: coderror = 0 significa OK, caso contrario es un error
        if (resultOUT.getResult().getCodError().equals("0")) {
            //Imprimimos la respuesta
            //LECTURA DE VALORES DE AUDITORIA//
            logger.debug("ID_BPEL: " + resultOUT.getResult().getIdBpel());
        } else {
            logger.error("[CodError] " + resultOUT.getResult().getCodError());
            logger.error("[ErrMsg] " + resultOUT.getResult().getErrMsg());
        }
		return out;
	}*/
	
	public CustomerReadBeanOut customerRead(String strCsid) throws SQLException, Exception {
        //Se definen los objetos que contendran los datos de entrada 
        
        CustomerReadBeanIn in = new CustomerReadBeanIn();
	CustomerReadBeanOut out = new CustomerReadBeanOut();
        //Se define el objeto que almacenara el retorno
        
        //Seteamos los parametros de input deseados
        //PARAMETROS REQUERIDOS//
        in.setIdApp(Constante.APPLICATION_ID_BPEL); //Identificador de Aplicacion
        //PARAMETROS ADICIONALES//
        in.setCS_ID(new Long(strCsid));
        //Seteamos la variable input

        //Se define el objeto "Invoker" que hara la llamada del Proceso BPEL 
        BPEL_CustomerReadPortClient invokeCustomerRead = new BPEL_CustomerReadPortClient();
        //Se utiliza el metodo "process" que llamara al proceso BPEL y se almacena en la variable resultOUT
        long ini = System.currentTimeMillis();
        out = invokeCustomerRead.process(in);
        long fin = System.currentTimeMillis();
        long tot = 0;
        tot = fin - ini;
		if(logger.isDebugEnabled())
			logger.debug("Tiempo Invoke: " + tot);

        //Validamos Codigo de Error: coderror = 0 significa OK, caso contrario es un error
        if (out.getCodError().equals("0")) {
            //Imprimimos la respuesta
            //LECTURA DE VALORES DE AUDITORIA//
            logger.debug("ID_BPEL: " + out.getIdBpel());
            logger.debug("CS_CODE: " + out.getCS_CODE());
        } else {
            logger.error("[CodError] " +out.getCodError());
            logger.error("[ErrMsg] " + out.getErrMsg());
        }
            return out;
	}
	/*
	public ContractCreateOut createContract(HashMap hshParametrosMap) throws SQLException, Exception {
        //Se definen los objetos que contendran los datos de entrada 
        BPEL_ContractCreateProcessRequest input = new BPEL_ContractCreateProcessRequest();
        ContractCreateIn in = new ContractCreateIn();
        //Se define el objeto que almacenara el retorno
        BPEL_ContractCreateProcessResponse resultOUT = new BPEL_ContractCreateProcessResponse();
		
        //Seteamos los parametros de input deseados
        //PARAMETROS REQUERIDOS//
        in.setIdApp(Constante.APPLICATION_ID_BPEL); //Identificador de Aplicacion
		
        //PARAMETROS ADICIONALES//
        ContractNewIn contractNewIn = new ContractNewIn();
        ContractDeviceAddIn contractDeviceAddIn = new ContractDeviceAddIn();
        ContractServiceResourcesWriteIn[] listContractServiceResourcesWriteIn = new ContractServiceResourcesWriteIn[1];
        contractNewIn.setCS_ID(new Long((String) hshParametrosMap.get("csId"))); 
		contractDeviceAddIn.setEQ_NUM((String) hshParametrosMap.get("eqNum"));

		DirectoryNumbersCSRWIn[] listaDirectoryNumbersCSRWIn1 = new DirectoryNumbersCSRWIn[1];
		DirectoryNumbersCSRWIn directoryNumbersCSRWIn1 = new DirectoryNumbersCSRWIn();
		directoryNumbersCSRWIn1.setDN_TYPE(new Long((String) hshParametrosMap.get("dnType")));
		listaDirectoryNumbersCSRWIn1[0] = directoryNumbersCSRWIn1;
		ContractServiceResourcesWriteIn contractServiceResourcesWriteIn1 = new ContractServiceResourcesWriteIn();
		contractServiceResourcesWriteIn1.setDirectory_numbers(listaDirectoryNumbersCSRWIn1);
		listContractServiceResourcesWriteIn[0] = contractServiceResourcesWriteIn1;
		in.setCONTRACT_NEW_IN(contractNewIn);
        in.setCONTRACT_DEVICE_ADD_IN(contractDeviceAddIn);
        //in.setCONTRACT_SERVICES_ADD_IN(contractServicesAddIn);
        in.setLIST_CONTRACT_SERVICE_RESOURCES_WRITE_IN(listContractServiceResourcesWriteIn);
        //Seteamos la variable input
        input.setContractCreateIn(in);
        input.setCO_ID_TEMPLATE(new Long((String) hshParametrosMap.get("coIdTemplate")));

        //Se define el objeto "Invoker" que hara la llamada del Proceso BPEL 
        BPEL_ContractCreatePortClient invokeContractCreate = new BPEL_ContractCreatePortClient();

        //Se utiliza el metodo "process" que llamara al proceso BPEL y se almacena en la variable resultOUT
        long ini = System.currentTimeMillis();
        resultOUT = invokeContractCreate.process(input);
        long fin = System.currentTimeMillis();
        long tot = 0;
        tot = fin - ini;
        if(logger.isDebugEnabled())
			logger.debug("Tiempo Invoke: " + tot);
        //Validamos Codigo de Error: coderror = 0 significa OK, caso contrario es un error
        if (resultOUT.getResult().getCodError().equals("0")) {
            //Imprimimos la respuesta
            //LECTURA DE VALORES DE AUDITORIA//
            System.out.println("ID_BPEL: " +resultOUT.getResult().getIdBpel());
            System.out.println("CO_ID: " + resultOUT.getResult().getCONTRACT_NEW_OUT().getCO_ID());
            System.out.println("Dispatch: " +  resultOUT.getResult().getDispatch());
            System.out.println("Telefonia: " + resultOUT.getResult().getTelefonia());
                               
        } else {
            System.out.println("ID_BPEL: " +resultOUT.getResult().getIdBpel());
            System.out.println("[CodError] " + resultOUT.getResult().getCodError());
            System.out.println("[ErrMsg] " + resultOUT.getResult().getErrMsg());
        }		
		return resultOUT.getResult();
	}
*/
  public HashMap getSourceAddress(long lngCustomerId, long lngItemId) throws  SQLException, Exception{
    return objCustomerDAO.getSourceAddress(lngCustomerId, lngItemId);
  }

  public HashMap getDestinyAddress(long lngCustomerId) throws  SQLException, Exception{
    return objCustomerDAO.getDestinyAddress(lngCustomerId);
  }

  public HashMap getCustomerContactsByType(long lngCustomerId, String strType) throws  SQLException, Exception{
    return objCustomerDAO.getCustomerContactsByType(lngCustomerId,strType);
  }

  //Inicio CEM
  public HashMap CustomerDAOgetInfoCustomer(long lOrderId) throws  SQLException, Exception{
    return objCustomerDAO.getInfoCustomer(lOrderId);
  }	
  //Fin CEM

  //Inicio CEM
	/*public HashMap createCustomerBSCS(long lOrderId) throws  SQLException, Exception{
		HashMap hshData=new HashMap();
		String strMessage=null;
		OrderWorkFlow orderWorkFlow = new OrderWorkFlow();								
		orderWorkFlow.setNpOrderId(String.valueOf(lOrderId));			
		orderWorkFlow.setNpSolutionId(Constante.SOLUCION_PREPAGO); //2
		OrdersWorkFlowManageBO ordersWorkFlowManageBO = new OrdersWorkFlowManageBO();												
		strMessage = ordersWorkFlowManageBO.createNewCustomer(orderWorkFlow);
		if(logger.isDebugEnabled())
			logger.debug("SEJBCustomerBean-createCustomerBSCS: Resultados de la invocación a BPEL(createNewCustomer)");
		if (strMessage==null){
			if(logger.isDebugEnabled())
				logger.debug("Se ejecuto correctamente - createNewCustomer-> null");								
			HashMap hshCustomerMap=objCustomerDAO.getInfoCustomer(lOrderId);
			String strMsg=(String)hshCustomerMap.get(strMessage);
			if(StringUtils.isBlank(strMessage)) {
				hshData.put("strCustCodeBSCS", hshCustomerMap.get("strCustCodeBSCS"));
				hshData.put("strCustomerIdBSCS", hshCustomerMap.get("strCustomerIdBSCS"));
				hshData.put("strCustomerId", hshCustomerMap.get("strCustomerId"));
				hshData.put("strOrderId", lOrderId+"");			
			}
			else{
				strMessage="[SEJBCustomerBean][createCustomerBSCS][getInfoCustomer][" + strMsg+"]";  		
			}
		}	
		else{	//if (strMessage!=null){		
			if(logger.isDebugEnabled())
				logger.debug("Hubo errores en la ejecución del createNewCustomer");		
			strMessage="Error en la creación del cliente en BSCS";	
			hshData.put("strMessageCustCodeBSCS", strMessage);			
		}	
		hshData.put("strMessage", strMessage);
    return hshData;
  }	
  //Fin CEM
  */
  
  //Odubock
  public HashMap CustomerDAOgetValidateDniRucEquals(String strNumDoc, String strTipoDoc) throws  SQLException, Exception{
    return objCustomerDAO.getValidateDniRucEquals(strNumDoc,strTipoDoc);
  }

  public ArrayList getSitesByCustomer(long customerId) throws  SQLException, Exception{
    return objCustomerDAO.getSitesByCustomer(customerId);
  }

  public CustomerBean getCustomerInfo(long customerId) throws  Exception, SQLException{
    return objCustomerDAO.getCustomerInfo(customerId);
  }

  public HashMap getAddressesByCustomer(long customerId) throws  Exception, SQLException{
    return objCustomerDAO.getAddressesByCustomer(customerId);
  }

  public HashMap getExisteOrdenProspect(long customerId) throws  Exception, SQLException{
    return objCustomerDAO.getExisteOrdenProspect(customerId);
  }

  public CustomerBean getValidateCustomer(String tipoDocumento, String nroDocumento) throws  Exception, SQLException{
    return objCustomerDAO.getValidateCustomer(tipoDocumento,nroDocumento);
  }

  public HashMap getCreateContract(HashMap hshParametrosMap) throws  SQLException, Exception{
    return objAccountDAO.getCreateContract(hshParametrosMap);
  }

	public HashMap getCustomerDataDetail(long iCustomerId, String strSwcreatedBy, long iRegionid, long lOrderId) throws Exception {
		return objCustomerDAO.getCustomerDataDetail(iCustomerId,strSwcreatedBy,iRegionid,lOrderId);
	}
  
	public HashMap getNumAddressId (String strTipoObjeto, long lCodigo, String strTipoDireccion, long lRegionId) throws Exception {
		return objCustomerDAO.getNumAddressId (strTipoObjeto,lCodigo,strTipoDireccion,lRegionId);
	}  

  public byte getIsSiteBA(long lngCustomerId) throws Exception, SQLException{
    return objCustomerDAO.getIsSiteBA(lngCustomerId);
  }
  
  public HashMap CustomerDAOgetValidateDocument(String strNumDoc, String strTipoDoc) throws  SQLException, Exception{
    return objCustomerDAO.getValidateDocument(strNumDoc,strTipoDoc);
  }
  
	/***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES - FIN
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 21/11/2007
     ***********************************************************************
     ***********************************************************************
     **********************************************************************/


/***********************************************************************
***********************************************************************
***********************************************************************
*  INVOCACION DE METODO ROL - INICIO
*  REALIZADO POR: Ruth Polo
*  FECHA: 09/01/2009
***********************************************************************
***********************************************************************
***********************************************************************/  
  
    
  public long CustomerDAOgetRol(long intScreenoptionid, long intUserid, long intAppid)  throws Exception,SQLException{
		return objCustomerDAO.getRol(intScreenoptionid, intUserid, intAppid);
  }

  public HashMap getValidationFraudCustomer(String strDocCustomer) throws  SQLException, Exception
  {
    return objFraudDAO.getVerificationFraudCustomer(strDocCustomer);
  }
    
   
 
/***********************************************************************
***********************************************************************
***********************************************************************
*  INVOCACION DE METODO ROL - FIN
*  REALIZADO POR: Ruth Polo
*  FECHA: 09/01/2009
***********************************************************************
***********************************************************************
***********************************************************************/  
  /*Inicio Responsable de Pago*/
  public HashMap CustomerDAOgetCustomerSitesBySolution(long intCustomerId, int iSolution, int iSpecification) throws SQLException, Exception{
    return objCustomerDAO.getCustomerSitesBySolution(intCustomerId, iSolution, iSpecification);
  }
  /*Fin Responsable de Pago*/


  /**
  Method : getContactList
  Purpose: Obtener el detalle del contacto de un tipo determinado cliente
  Developer       		Fecha       Comentario
  =============   		==========  ======================================================================
  Evelyn Ocampo     	11/06/2009  Creación
  */ 
  public HashMap getContactList(long lObjectTypeId, String strObjectType, String strType, long lSiteId) throws Exception {
		return objCustomerDAO.getContactList(lObjectTypeId,strObjectType,strType, lSiteId);
	}
  
  /**
  Method : getContactChangeList
  Purpose: Obtener el detalle del contacto de un tipo determinado cliente de la tabla swbapps.np_contact_change
  Developer       		Fecha       Comentario
  =============   		==========  ======================================================================
  Evelyn Ocampo     	17/06/2009  Creación
  */ 
  public HashMap getContactChangeList(long lOrderId,int iItemid,int iObjectType,long lObjectId, String strContactType) throws Exception {
		return objCustomerDAO.getContactChangeList(lOrderId,iItemid,iObjectType,lObjectId,strContactType);
	}
   /**
  Method : getContactChangeList
  Purpose: Obtener el detalle del contacto de un tipo determinado cliente de la tabla swbapps.np_contact_change
  Developer       		Fecha       Comentario
  =============   		==========  ======================================================================
  Cesar Barzola     	10/11/2009  Creación
  */
  public HashMap getValidationCustomer(long lUserId,long lCustomerId) throws Exception{
    return objProposedDAO.getValidationCustomer(lUserId,lCustomerId);
  }

  /**
  Method : getSubRegOrder
  Purpose: Obtener los datos de modificacion de la orden de Subsanasion/Regularizacion en modo edicion
  Developer       		Fecha       Comentario
  =============   		==========  ======================================================================
  Martin Vera       	08/01/2010  Creación
  */
  public HashMap getSubRegOrder(long lOrderId) throws Exception{
    return objSubRegCustomerInfoDAO.getOrderSubReg(lOrderId);
  }
  
  /**
  Method : getSubRegOrder
  Purpose: Obtener el numero de telefono relacionado con el incidente de regularizacion/subsanacion
  Developer       		Fecha       Comentario
  =============   		==========  ======================================================================
  Martin Vera       	08/01/2010  Creación
  */
  public HashMap getPhoneSubReg(long lCustomerId,long lIncidentId) throws Exception{
    return objSubRegCustomerInfoDAO.getPhoneSubReg(lCustomerId,lIncidentId);
  }
  
    /**
      Method : validateUbigeo
      Purpose: 
      Developer       		Fecha       Comentario
      =============   		==========  ======================================================================
      DGUTIERREZ     	   21/07/2010  Creación 
       */

  
  public String validateUbigeo(String state, String province, String city)throws Exception {
  
   return objCustomerDAO.validateUbigeo(state,province,city);
  
  }
    /**
    Method : getValidateAddress
    Purpose:
    Developer                 Fecha       Comentario
    =============             ==========  ======================================================================
    FBERNALES                22/12/2015  Creación
    */  
    public Map<Double,Boolean> getValidateAddress(String sAddress,String sUbigeo, String sAplicacion )throws Exception {
    
        return objCustomerDAO.getValidateAddress(sAddress, sUbigeo, sAplicacion);
    
    }
    public void insLogValidateAddress(String sIdApp,Double dCorrelacion, String sCreatedBy,String sDireccion,Integer lIdCliente, String sNumDoc, Integer lNumOrder)throws Exception {
       objCustomerDAO.insLogValidateAddress(sIdApp, dCorrelacion, sCreatedBy, sDireccion,lIdCliente, sNumDoc,lNumOrder);
    }
}