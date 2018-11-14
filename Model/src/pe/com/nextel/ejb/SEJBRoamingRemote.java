package pe.com.nextel.ejb;

import javax.ejb.EJBObject;
import java.util.HashMap;

/**
 * Created by montoymi on 12/08/2015.
 * [ADT-RCT-092 Roaming con corte]
 */
public interface SEJBRoamingRemote extends EJBObject {
    HashMap loadRoamingServices(String phone, long planId) throws Exception;

    HashMap validateRecurrentRoamingService(String phone, String activationDate, int validity, long orderId, String bagCode) throws Exception; //CFERNANDEZ [PRY-0858] - Se agrega el parametro: bagCode

}
