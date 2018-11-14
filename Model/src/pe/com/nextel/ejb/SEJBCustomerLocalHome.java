package pe.com.nextel.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface SEJBCustomerLocalHome extends EJBLocalHome {
    SEJBCustomerLocal create() throws CreateException;
}
