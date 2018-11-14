package pe.com.nextel.service;

import pe.com.nextel.bean.IdentityVerificationDetailBean;
import pe.com.nextel.bean.BiometricValidationBean;
import pe.com.nextel.bean.NoBiometricValidationBean;
import pe.com.nextel.ejb.SEJBIdentificationRemote;
import pe.com.nextel.ejb.SEJBIdentificationRemoteHome;
import pe.com.nextel.util.MiUtil;

import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.naming.Context;
import javax.rmi.PortableRemoteObject;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class IdentificationService extends GenericService
{

    public static SEJBIdentificationRemote getSEJBIdentificationRemote() {
        try{
            final Context context = MiUtil.getInitialContext();
            final SEJBIdentificationRemoteHome sEJBOrderNewRemoteHome =
                    (SEJBIdentificationRemoteHome) PortableRemoteObject.narrow(context.lookup("SEJBIdentification"), SEJBIdentificationRemoteHome.class);
            SEJBIdentificationRemote sEJBOrderNewRemote;
            sEJBOrderNewRemote = sEJBOrderNewRemoteHome.create();

            return sEJBOrderNewRemote;
        }catch(Exception ex) {
            System.out.println("Exception : [IdentificationService][getSEJBIdentificationRemote]["+ex.getMessage()+"]");
            return null;
        }

    }

    public IdentityVerificationDetailBean getIdentityVerificationDetail(long lOrderId) {
        IdentityVerificationDetailBean identityVerificationDetailBean = new IdentityVerificationDetailBean();
        try {
            identityVerificationDetailBean = getSEJBIdentificationRemote().getIdentityVerificationDetail(lOrderId);
        }catch (SQLException e) {
            System.out.println("[SQLException][IdentificationService][getIdentityVerificationDetail][" + e.getMessage() + "]["+e.getClass()+"]");
        }catch (RemoteException e) {
            System.out.println("[RemoteException][IdentificationService][getIdentityVerificationDetail][" + e.getMessage() + "]["+e.getClass()+"]");
        }catch(Exception e){
            System.out.println("[Exception][IdentificationService][getIdentityVerificationDetail][" + e.getMessage() + "]["+e.getClass()+"]");
        }
        return identityVerificationDetailBean;
    }
}
