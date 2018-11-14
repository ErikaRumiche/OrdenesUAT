package pe.com.nextel.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface SEJBOrderRALocalHome extends EJBLocalHome {
    SEJBOrderRALocal create() throws CreateException;
}
