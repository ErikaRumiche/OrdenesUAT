package pe.com.nextel.service;

import pe.com.nextel.bean.IsolatedVerifConfigBean;
import pe.com.nextel.bean.IsolatedVerificationBean;
import pe.com.nextel.ejb.SEJBIsolatedVerificationRemote;
import pe.com.nextel.ejb.SEJBIsolatedVerificationRemoteHome;
import pe.com.nextel.util.MiUtil;

import javax.naming.Context;
import javax.rmi.PortableRemoteObject;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IsolatedVerificationService extends GenericService implements VerificationService{

    public static SEJBIsolatedVerificationRemote getSEJBIsolatedVerificationRemote() {
        try{
            final Context context = MiUtil.getInitialContext();
            final SEJBIsolatedVerificationRemoteHome sEJBIsolatedVerificationRemoteHome =
                    (SEJBIsolatedVerificationRemoteHome) PortableRemoteObject.narrow(context.lookup("SEJBIsolatedVerification"), SEJBIsolatedVerificationRemoteHome.class);
            SEJBIsolatedVerificationRemote sEJBIsolatedVerificationRemote;
            sEJBIsolatedVerificationRemote = sEJBIsolatedVerificationRemoteHome.create();

            return sEJBIsolatedVerificationRemote;
        }catch(Exception ex) {
            System.out.println("Exception : [IdentificationService][getSEJBIsolatedVerificationRemote]["+ex.getMessage()+"]");
            return null;
        }

    }

    public List<IsolatedVerifConfigBean> getViaConfig(String strCustomerId) {
        List<IsolatedVerifConfigBean> specLst = new ArrayList<IsolatedVerifConfigBean>();
        try {
            specLst = getSEJBIsolatedVerificationRemote().getViaConfig(strCustomerId);
        }catch (SQLException e) {
            System.out.println("[SQLException][IsolatedVerificationService][getViaConfig][" + e.getMessage() + "]["+e.getClass()+"]");
        }catch (RemoteException e) {
            System.out.println("[RemoteException][IsolatedVerificationService][getViaConfig][" + e.getMessage() + "]["+e.getClass()+"]");
        }catch(Exception e){
            System.out.println("[Exception][IsolatedVerificationService][getViaConfig][" + e.getMessage() + "]["+e.getClass()+"]");
        }
        return specLst;
    }




    public List<IsolatedVerificationBean> getViaCustomer(Integer customerId, Integer verificationId, Integer orderId, Integer specificationId) {
        List<IsolatedVerificationBean> listIsolatedVerificationBean = new ArrayList<IsolatedVerificationBean>();
        try{
            listIsolatedVerificationBean = getSEJBIsolatedVerificationRemote().getViaCustomer(customerId, verificationId, orderId, specificationId);

            if(listIsolatedVerificationBean != null) {
                formatearFechas(listIsolatedVerificationBean);
            }
        }catch (SQLException e) {
            System.out.println("[SQLException][IsolatedVerificationService][getViaCustomer][" + e.getMessage() + "]["+e.getClass()+"]");
        }catch (RemoteException e) {
            System.out.println("[RemoteException][IsolatedVerificationService][getViaCustomer][" + e.getMessage() + "]["+e.getClass()+"]");
        }catch(Exception e){
            System.out.println("[Exception][IsolatedVerificationService][getViaCustomer][" + e.getMessage() + "]["+e.getClass()+"]");
        }
        return listIsolatedVerificationBean;
    }

    private void formatearFechas(List<IsolatedVerificationBean> listIsolatedVerificationBean) {
        for(IsolatedVerificationBean isolatedVerificationBean: listIsolatedVerificationBean) {
            Date date_of_use = isolatedVerificationBean.getNp_date_of_use();

            if(date_of_use != null) {
                isolatedVerificationBean.setDate_of_use(MiUtil.getDate(date_of_use, "dd/MM/yyyy hh:mm:ss a"));
            } else {
                isolatedVerificationBean.setDate_of_use("");
            }

            Date date_end_validity = isolatedVerificationBean.getNp_date_end_validity();

            if(date_end_validity != null) {
                isolatedVerificationBean.setDate_end_validity(MiUtil.getDate(date_end_validity, "dd/MM/yyyy hh:mm:ss a"));
            } else {
                isolatedVerificationBean.setDate_end_validity("");
            }

        }
    }

    public void updViaCustomer(Integer npverificationid, String nptypetransaction, Integer nptransaction, String npmodifiedby){
        try{
            getSEJBIsolatedVerificationRemote().updViaCustomer(npverificationid, nptypetransaction, nptransaction, npmodifiedby);
        }catch (SQLException e) {
            System.out.println("[SQLException][IsolatedVerificationService][updViaCustomer][" + e.getMessage() + "]["+e.getClass()+"]");
        }catch (RemoteException e) {
            System.out.println("[RemoteException][IsolatedVerificationService][updViaCustomer][" + e.getMessage() + "]["+e.getClass()+"]");
        }catch(Exception e){
            System.out.println("[Exception][IsolatedVerificationService][updViaCustomer][" + e.getMessage() + "]["+e.getClass()+"]");
        }
    }

}
