package pe.com.nextel.service;

import java.util.HashMap;

import javax.naming.Context;

import javax.rmi.PortableRemoteObject;

import pe.com.nextel.ejb.SEJBCustomerRemote;
import pe.com.nextel.ejb.SEJBCustomerRemoteHome;
import pe.com.nextel.ejb.SEJBRetailRemote;
import pe.com.nextel.ejb.SEJBRetailRemoteHome;
import pe.com.nextel.form.RetailForm;
import pe.com.nextel.util.MiUtil;


/**
 * Motivo: Clase Service que contendrá lógica de negocio referente a Retail.
 *         Utilizado directamente por JP_ORDER_RETAIL_ShowPage.jsp
 * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
 * <br>Fecha: 11/09/2007
 * @see GenericService
 */
public class RetailService extends GenericService {

	public static SEJBRetailRemote getSEJBGeneralRemote() {
         try{
             final Context context = MiUtil.getInitialContext();
             final SEJBRetailRemoteHome sEJBRetailRemoteHome = 
                 (SEJBRetailRemoteHome) PortableRemoteObject.narrow( context.lookup( "SEJBRetail" ), SEJBRetailRemoteHome.class );
             SEJBRetailRemote sEJBRetailRemote;
             sEJBRetailRemote = sEJBRetailRemoteHome.create();
             return sEJBRetailRemote;
         }	catch(Exception e) {
			logger.error(formatException(e));
			return null;
         }
	}
	
	public static SEJBCustomerRemote getSEJBCustomerRemote() {
         try{
             final Context context = MiUtil.getInitialContext();
             final SEJBCustomerRemoteHome sEJBCustomerRemoteHome = 
                 (SEJBCustomerRemoteHome) PortableRemoteObject.narrow( context.lookup( "SEJBCustomer" ), SEJBCustomerRemoteHome.class );
             SEJBCustomerRemote sEJBCustomerRemote;
             sEJBCustomerRemote = sEJBCustomerRemoteHome.create();
             return sEJBCustomerRemote;
         }	catch(Exception e) {
			logger.error(formatException(e));
			return null;
         }
	}

    /**
     * @see pe.com.nextel.dao.RetailDAO#newOrderRetail(RetailForm, String)
     */
    public HashMap newOrderRetail(RetailForm retailForm) {
        if (logger.isDebugEnabled())
            logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBGeneralRemote().newOrderRetail(retailForm);
        }
		catch(Throwable t) {
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
    }
    
    /**
     * @see pe.com.nextel.dao.RetailDAO#updPhoneItem(long, String)
     */
    public String updPhoneItem(long lItemId,String strPhone) {
       if (logger.isDebugEnabled())
           logger.debug("--Inicio--");
       String strResult = null;		
       try {
          strResult = getSEJBGeneralRemote().updPhoneItem(lItemId, strPhone);
       }
		 catch(Exception e) {
			logger.error(formatException(e));
		 }
		 return strResult;
    }
    
    /**
     * @see pe.com.nextel.dao.RetailDAO#updContractItemDev(long, long)
     */
    public String updContractItemDev(long lItemDevId,long iCoId) {
       if (logger.isDebugEnabled())
           logger.debug("--Inicio--");
       String strResult = null;		
       try {
          strResult = getSEJBGeneralRemote().updContractItemDev(lItemDevId, iCoId);
       }
		 catch(Exception e) {
			logger.error(formatException(e));
		 }
		 return strResult;
    }
    
    /**
     * @see pe.com.nextel.dao.SiteDAO#getSiteidByCodbscs(String)
     */
    public int getSiteidByCodbscs(String strcodBscs) {
       if (logger.isDebugEnabled())
           logger.debug("--Inicio--");
       int iResult = 0;		
       System.out.println("[RetailService][strcodBscs]"+strcodBscs);
       try {
          iResult = getSEJBGeneralRemote().getSiteidByCodbscs(strcodBscs);
       }
		 catch(Exception e) {
			logger.error(formatException(e));
		 }
		 return iResult;
    }
    
}
