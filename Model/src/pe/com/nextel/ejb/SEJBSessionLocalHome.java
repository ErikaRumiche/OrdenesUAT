package pe.com.nextel.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface SEJBSessionLocalHome extends EJBLocalHome {
    SEJBSessionLocal create() throws CreateException;
}
