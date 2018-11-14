package pe.com.nextel.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface SEJBOrderNewLocalHome extends EJBLocalHome {
    SEJBOrderNewLocal create() throws CreateException;
}
