package pe.com.nextel.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;


public interface SEJBOrderEditLocalHome extends EJBLocalHome {
    SEJBOrderEditLocal create() throws CreateException;
    
}
