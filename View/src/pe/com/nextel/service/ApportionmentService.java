package pe.com.nextel.service;

import java.util.HashMap;

import javax.naming.Context;
import javax.rmi.PortableRemoteObject;

import pe.com.nextel.bean.RequestApportionmentBean;
import pe.com.nextel.ejb.SEJBApportionmentRemote;
import pe.com.nextel.ejb.SEJBApportionmentRemoteHome;
import pe.com.nextel.util.MiUtil;

public class ApportionmentService extends GenericService {

	public static SEJBApportionmentRemote getSEJBApportionmentRemote() {
		try {
			final Context context = MiUtil.getInitialContext();
			final SEJBApportionmentRemoteHome SEJBPenaltyRemoteHome = (SEJBApportionmentRemoteHome) PortableRemoteObject.narrow(context.lookup(
					"SEJBApportionment"), SEJBApportionmentRemoteHome.class);
			SEJBApportionmentRemote SEJBApportionmentRemote;
			SEJBApportionmentRemote = SEJBPenaltyRemoteHome.create();
			
			return SEJBApportionmentRemote;
		} catch (Exception e) {
			logger.error(formatException(e));
			return null;
		}
	}
	
	
    public HashMap getCalculatePayment(RequestApportionmentBean request) {
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBApportionmentRemote().getCalculatePayment(request);
        }
        catch(Throwable t) {
            manageCatch(hshDataMap, t);
            System.out.println("Error [SQLException][ApportionmentService][getCalculatePayment]["+t.getMessage()+"]");
        }
        return hshDataMap;
    }

}
