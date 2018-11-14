package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.HashMap;

import javax.ejb.EJBObject;

import pe.com.nextel.bean.OrderBean;
import pe.com.nextel.bean.OrderDetailBean;
import pe.com.nextel.form.OrderSearchForm;
import pe.com.nextel.form.PCMForm;
import pe.com.nextel.form.RetentionForm;


public interface SEJBOrderSearchRemote extends EJBObject {
    HashMap getOrderList(OrderSearchForm objOrderSearchForm)
        throws SQLException, RemoteException, Exception;
		
	//LSILVA : HPPTT - Internal Order List - Obtiene la lista de Órdenes internas asociadas a una Orden principal
    HashMap getInternalOrderList(long plParentOrderId, long plNumRegistros, long plNumPagina)
    	throws SQLException, RemoteException, Exception;
    
	//LSILVA : HPPTT - Parent Order List - Obtiene la lista de Órdenes padres de una Orden dada. La primera Orden
    //de la lista es la más ancestral.
    HashMap getParentOrderList(long plOrderId) throws SQLException, RemoteException, Exception;
	
    HashMap existOrder(long lOrderId)
        throws SQLException, RemoteException, Exception;

    //AGAMARRA
    HashMap getDataField(int an_ruleid, String av_retriverepresentative,
        int an_salesstructid, int an_providergrpid)
        throws SQLException, RemoteException, Exception;

    //AGAMARRA
    String checkStructRule(int ruleid, String strNpsalesstructid)
        throws SQLException, Exception, RemoteException;

    HashMap getFinalSuspensionList(HashMap hshParameters)
        throws SQLException, RemoteException, Exception;

    HashMap getFinalSuspDetailList(HashMap hshParameters)
        throws SQLException, RemoteException, Exception;

    HashMap getCalArea() throws SQLException, RemoteException, Exception;

    HashMap getSuspensionReason()
        throws SQLException, RemoteException, Exception;

    HashMap getRetentionTool() throws SQLException, RemoteException, Exception;

    HashMap getClientType() throws SQLException, RemoteException, Exception;

    //HashMap getGeneralSuspensionList(HashMap hshParameters) throws SQLException, RemoteException, Exception;
    HashMap getDetailedSuspensionList(HashMap hshParameters)
        throws SQLException, RemoteException, Exception;

    HashMap getEstadoPMC(String dominioTabla)
        throws SQLException, RemoteException, Exception;

    HashMap getActionID(String dominioTabla)
        throws SQLException, RemoteException, Exception;


    HashMap getRetentionList(RetentionForm retentionForm)
        throws SQLException, RemoteException, Exception;

    HashMap getPCMList(PCMForm pcmForm)
        throws SQLException, RemoteException, Exception;

    //AGAMARRA
    int getParentForAssist(int an_salesstructdefaultid)
        throws SQLException, Exception, RemoteException;

    //AGAMARRA
    int getPrvdStructAssist(int an_salesstructdefaultid)
        throws SQLException, Exception, RemoteException;

    //AGAMARRA
    String getLastPosition(int an_salesstructdefaultid)
        throws SQLException, Exception, RemoteException;

    HashMap getSuspensionType() 
        throws  SQLException, Exception, RemoteException;

  HashMap getItemServicePendingList(long lOrderId, long lItemId) throws RemoteException, Exception, SQLException;
  
  //JLIMAYMANTA
  //INICIO: AMENDEZ | PRY-1141
  OrderDetailBean getSearchOrderById(long orderIdi,String userLogin,int paymenttype) throws SQLException, Exception, RemoteException;
  //FIN: AMENDEZ | PRY-1141
  
    //JLIMAYMANTA
    //INICIO: AMENDEZ | PRY-1141
    HashMap saveOrderPaymentTE(long orderIdi,double monto,String hdnRa,String hdnVoucher,String hdnComentario,String hdnNumLogin,String hdnUser,int paymenttype,long paymentOrderQuotaId)throws SQLException, Exception, RemoteException;
    //FIN: AMENDEZ | PRY-1141

    //EFLORES 25/08/2017
    HashMap saveOrderPaymentTE(long orderIdi,double monto,String hdnRa,String hdnVoucher,String hdnComentario,String hdnNumLogin,String hdnUser,Integer hdnFlgVep,Double txtCuotaInicial, Double hdnMontoFinanciado,Integer hdnNumCuotas)throws SQLException, Exception, RemoteException;
    /* AYATACO - Inicio */
    HashMap validateOrderExist(String npsource, int npsourceid)throws SQLException, Exception, RemoteException;
    /* AYATACO - Fin */

    /**
     * @author AMENDEZ
     * @project PRY-1141
     * Metodo   Lista tipos de pago para registro de pago de TPF
     * @return
     */
    public HashMap lstPaymentType() throws RemoteException;
}
