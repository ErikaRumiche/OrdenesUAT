package pe.com.nextel.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface SEJBBillingAccountLocalHome extends EJBLocalHome {
    SEJBBillingAccountLocal create() throws CreateException;
}
