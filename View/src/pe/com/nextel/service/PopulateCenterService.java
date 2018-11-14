package pe.com.nextel.service;


import org.apache.log4j.Logger;
import pe.com.nextel.bean.PopulateCenterBean;
import pe.com.nextel.ejb.SEJBPopulateCenterRemote;
import pe.com.nextel.ejb.SEJBPopulateCenterRemoteHome;
import pe.com.nextel.util.MiUtil;

import javax.naming.Context;
import javax.rmi.PortableRemoteObject;
import java.util.HashMap;


/**
 * Created by JCASTILLO on 04/04/2017.
 * [PRY-0787]
 */
public class PopulateCenterService extends GenericService {


    private static final Logger logger = Logger.getLogger(PopulateCenterService.class);
    public static SEJBPopulateCenterRemote getSEJBPopulateCenterRemote() {
        SEJBPopulateCenterRemote sejbPopulateCenterRemote=null;
        try{
            final Context context = MiUtil.getInitialContext();
            final SEJBPopulateCenterRemoteHome sejbPopulateCenterRemoteHome =
                    (SEJBPopulateCenterRemoteHome) PortableRemoteObject.narrow( context.lookup( "SEJBPopulateCenter" ), SEJBPopulateCenterRemoteHome.class );
            sejbPopulateCenterRemote = sejbPopulateCenterRemoteHome.create();

            return sejbPopulateCenterRemote;
        }catch(Exception ex) {
            logger.error("Exception",ex);

        }
        return sejbPopulateCenterRemote;
    }

    public HashMap savePopulateCenter(PopulateCenterBean populateCenterBean,String user){
        logger.info("savePopulateCenter: Inicio:"+populateCenterBean);
        HashMap hashMap= new HashMap();
        try {
            hashMap=getSEJBPopulateCenterRemote().savePopulateCenter(populateCenterBean,user);
        }catch(Throwable t){
            manageCatch(hashMap, t);
        }
        logger.info("savePopulateCenter: Fin:"+hashMap);
        return hashMap;
    }

    public HashMap getPopulateCenterDetail(String orderId){
        logger.info("getPopulateCenterDetail: Inicio:"+orderId);
        HashMap hashMap= new HashMap();
        try {
            hashMap=getSEJBPopulateCenterRemote().getPopulateCenterDetail(MiUtil.getLong(orderId));
        }catch(Throwable t){
            manageCatch(hashMap, t);
        }
        logger.info("getPopulateCenterDetail: Fin:"+hashMap);
        return hashMap;
    }

    public HashMap getPopulateCenter(String orderId){
        logger.info("getPopulateCenter: Inicio:"+orderId);
        HashMap hashMap= new HashMap();
        try {
            hashMap=getSEJBPopulateCenterRemote().getPopulateCenter(MiUtil.getLong(orderId));
        }catch(Throwable t){
            manageCatch(hashMap, t);
        }
        logger.info("getPopulateCenter: Fin:"+hashMap);
        return hashMap;
    }

}

