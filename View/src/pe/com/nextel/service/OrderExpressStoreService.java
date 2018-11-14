package pe.com.nextel.service;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.naming.Context;
import javax.rmi.PortableRemoteObject;

import pe.com.nextel.bean.OrderDetailBean;
import pe.com.nextel.bean.DominioBean;
import pe.com.nextel.ejb.SEJBOrderSearchRemote;
import pe.com.nextel.ejb.SEJBOrderSearchRemoteHome;
import pe.com.nextel.util.MiUtil;

public class OrderExpressStoreService extends GenericService implements Serializable{   
    
    public static SEJBOrderSearchRemote getSEJBOrderSearchRemote() {
    
      Context context = null;
      SEJBOrderSearchRemoteHome sEJBOrderSearchRemoteHome = null;
      SEJBOrderSearchRemote aux = null;
      try{
        context = MiUtil.getInitialContext();
        sEJBOrderSearchRemoteHome = 
              (SEJBOrderSearchRemoteHome) PortableRemoteObject.narrow( context.lookup( "SEJBOrderSearch" ), SEJBOrderSearchRemoteHome.class );
        aux = sEJBOrderSearchRemoteHome.create();
      }catch(Exception ex) {
          logger.error("Exception : [NewOrderService][getSEJBOrderSearchRemote]",ex);
      } 
      return aux;
    }

    public static OrderDetailBean getSearchOrderById(long orderIdi,String userLogin,int paymenttype){
        logger.info("***********************INICIO OrderExpressStoreService > getSearchOrderById(long orderIdi,String userLogin) ***********************");
        OrderDetailBean orderDetailBean = new OrderDetailBean();
        try {
            logger.info("=======orderIdi recuperado en el servelt OrderExpressStoreService======== ["+orderIdi+"]");
            logger.info("=======userLogin recuperado en el servelt OrderExpressStoreService======== ["+userLogin+"]");
            logger.info("=======paymenttype recuperado en el servelt OrderExpressStoreService======== ["+paymenttype+"]");
            orderDetailBean= getSEJBOrderSearchRemote().getSearchOrderById(orderIdi,userLogin,paymenttype);
        }catch (SQLException e) {
            logger.error("Error [SQLException][OrderExpressStoreService][getSearchOrderById]",e);
            orderDetailBean.setMessage(e.getMessage());
        }
        catch (RemoteException e) {
            logger.error("Error [RemoteException][OrderExpressStoreService][getSearchOrderById]",e);
            orderDetailBean.setMessage(e.getMessage());
        }
        catch (Exception e) {
            logger.error("Error [Exception][OrderExpressStoreService][getSearchOrderById]",e);
            orderDetailBean.setMessage(e.getMessage());
        }
        logger.info("***********************FIN OrderExpressStoreService > getSearchOrderById(long orderIdi,String userLogin) ***********************");
        return orderDetailBean;
    }
    
  
    public HashMap saveOrderPaymentTE(long orderIdi,double monto,String hdnRa,String hdnVoucher,String hdnComentario,String hdnNumLogin,String hdnUser, int paymentType,long paymentOrderQuotaId) {
        logger.info("***********************INICIO OrderExpressStoreService > saveOrderPaymentTE(long orderIdi,double monto,String hdnRa,String hdnVoucher,String hdnComentario,String hdnNumLogin,String hdnUser) ***********************");
        HashMap hshDataMap = new HashMap();
        try {
            logger.info("orderIdi       :  "+orderIdi);
            logger.info("monto          :  "+monto);
            logger.info("hdnRa          :  "+hdnRa);
            logger.info("hdnVoucher     :  "+hdnVoucher);
            logger.info("hdnComentario  :  "+hdnComentario);
            logger.info("hdnNumLogin    :  "+hdnNumLogin);
            logger.info("hdnUser        :  "+hdnUser);
            logger.info("paymentType            :  "+paymentType);
            logger.info("paymentOrderQuotaId    :  "+paymentOrderQuotaId);
            return getSEJBOrderSearchRemote().saveOrderPaymentTE(orderIdi,monto,hdnRa,hdnVoucher,hdnComentario,hdnNumLogin,hdnUser,paymentType,paymentOrderQuotaId);
        }catch(Throwable t){
            logger.error(t);
            manageCatch(hshDataMap, t);
        }
        logger.info("***********************FIN OrderExpressStoreService > saveOrderPaymentTE(long orderIdi,double monto,String hdnRa,String hdnVoucher,String hdnComentario,String hdnNumLogin,String hdnUser) ***********************");
        return hshDataMap;
    } 
    
    public HashMap saveOrderPaymentTE(long orderIdi,double monto,String hdnRa,String hdnVoucher,String hdnComentario,String hdnNumLogin,String hdnUser,Integer hdnFlgVep,Double txtCuotaInicial, Double hdnMontoFinanciado,Integer hdnNumCuotas) {
        logger.info("***********************INICIO OrderExpressStoreService > saveOrderPaymentTE(long orderIdi,double monto,String hdnRa,String hdnVoucher,String hdnComentario,String hdnNumLogin,String hdnUser,Integer hdnFlgVep,Double txtCuotaInicial, Double hdnMontoFinanciado,Integer hdnNumCuotas) ***********************");
        HashMap hshDataMap = new HashMap();
        try {
            logger.info("orderIdi               :  "+orderIdi);
            logger.info("monto                  :  "+monto);
            logger.info("hdnRa                  :  "+hdnRa);
            logger.info("hdnVoucher             :  "+hdnVoucher);
            logger.info("hdnComentario          :  "+hdnComentario);
            logger.info("hdnNumLogin            :  "+hdnNumLogin);
            logger.info("hdnUser                :  "+hdnUser);
            logger.info("hdnFlgVep              :  "+hdnFlgVep);
            logger.info("txtCuotaInicial        :  "+txtCuotaInicial);
            logger.info("hdnMontoFinanciado     :  "+hdnMontoFinanciado);
            logger.info("hdnNumCuotas           :  "+hdnNumCuotas);
            return getSEJBOrderSearchRemote().saveOrderPaymentTE(orderIdi,monto,hdnRa,hdnVoucher,hdnComentario,hdnNumLogin,hdnUser,hdnFlgVep,txtCuotaInicial, hdnMontoFinanciado,hdnNumCuotas);
        }catch(Throwable t){
            logger.error(t);
            manageCatch(hshDataMap, t);
        }
        logger.info("***********************FIN OrderExpressStoreService > saveOrderPaymentTE(long orderIdi,double monto,String hdnRa,String hdnVoucher,String hdnComentario,String hdnNumLogin,String hdnUser,Integer hdnFlgVep,Double txtCuotaInicial, Double hdnMontoFinanciado,Integer hdnNumCuotas) ***********************");
        return hshDataMap;
    }


    /** AYATACO - Inicio */
    public HashMap validateOrderExist(String npsource, int npsourceid) {
        logger.info("*********************** INICIO OrderExpressStoreService > validateOrderExist(npsource, npsourceid) ***********************");
        HashMap hshDataMap = new HashMap();
        try {
            logger.info("npsource ------> " + npsource);
            logger.info("npsourceid ------> " + npsourceid);
            return getSEJBOrderSearchRemote().validateOrderExist(npsource, npsourceid);
        }catch(Throwable t){
            logger.error(t);
            manageCatch(hshDataMap, t);
        }
        logger.info("*********************** FIN OrderExpressStoreService > validateOrderExist(npsource, npsourceid) ***********************");
        return hshDataMap;
    }
    /** AYATACO - Fin */

    /**
     * @author AMENDEZ
     * @project PRY-1141
     * Metodo   Lista tipos de pago para registro de pago de TPF
     * @return
     */
    public List<DominioBean> lstPaymentType(){
        logger.info("*************************** INICIO GenerateQuotaAdvancePaymentOrderService > getConfConceptPaymentOrderQuota ***************************");

        HashMap hshDataMap = new HashMap();
        List<DominioBean> arrayDominioBean = new ArrayList<DominioBean>();
        try{
            hshDataMap = getSEJBOrderSearchRemote().lstPaymentType();

            Collection coleccion = (Collection)hshDataMap.get("arrayList");
            if (coleccion!=null && (coleccion.size() > 0)) {
                Iterator it = coleccion.iterator();
                HashMap entity = new HashMap();
                while(it.hasNext()){
                    entity = (HashMap)it.next();

                    DominioBean dominioBean = new DominioBean();
                    dominioBean.setValor((String)entity.get("NPPAYMENTTYPEID"));
                    dominioBean.setDescripcion((String)entity.get("NPPAYMENTTYPENAME"));
                    arrayDominioBean.add(dominioBean);
                    dominioBean = null;
                }
            }
        } catch (Exception e){
            logger.error(e);
        }
        logger.info("*************************** FIN GenerateQuotaAdvancePaymentOrderService > getConfConceptPaymentOrderQuota ***************************");
        return arrayDominioBean;
    }

}
