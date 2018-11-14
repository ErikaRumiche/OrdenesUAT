package pe.com.nextel.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface SEJBGeneralLocalHome extends EJBLocalHome {
  SEJBGeneralLocal create() throws CreateException;
}