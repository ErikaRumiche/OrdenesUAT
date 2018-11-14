package pe.com.nextel.ejb;

import pe.com.nextel.bean.PopulateCenterBean;

import javax.ejb.EJBObject;
import java.util.HashMap;

/**
 * Created by JCASTILLO on 04/04/2017.
 * [PRY-0787]
 */
public interface SEJBPopulateCenterRemote extends EJBObject {

    public HashMap savePopulateCenter(PopulateCenterBean populateCenterBean,String user) throws Exception;
    public HashMap getPopulateCenterDetail(Long orderId) throws Exception;
    public HashMap getPopulateCenter(Long orderId) throws Exception;
}
