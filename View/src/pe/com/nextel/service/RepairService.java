package pe.com.nextel.service;

import com.google.gson.Gson;

import java.awt.Font;
import java.awt.image.BufferedImage;

import java.io.ByteArrayOutputStream;

import pe.com.nextel.bean.RepairBean;
import pe.com.nextel.ejb.SEJBGeneralRemote;
import pe.com.nextel.ejb.SEJBGeneralRemoteHome;
import pe.com.nextel.ejb.SEJBOrderNewRemote;
import pe.com.nextel.ejb.SEJBOrderNewRemoteHome;
import pe.com.nextel.ejb.SEJBRepairRemote;
import pe.com.nextel.ejb.SEJBRepairRemoteHome;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

import javax.imageio.ImageIO;

import javax.naming.Context;

import javax.rmi.PortableRemoteObject;

import net.sourceforge.jbarcodebean.JBarcodeBean;
import net.sourceforge.jbarcodebean.model.Code128;

import pe.com.nextel.bean.EquipmentDamage;


public class RepairService extends GenericService {
    public static SEJBGeneralRemote getSEJBGeneralRemote() {
        try {
            final Context context = MiUtil.getInitialContext();
            final SEJBGeneralRemoteHome sEJBGeneralRemoteHome = (SEJBGeneralRemoteHome) PortableRemoteObject.narrow(context.lookup(
                        "SEJBGeneral"), SEJBGeneralRemoteHome.class);
            SEJBGeneralRemote sEJBGeneralRemote;
            sEJBGeneralRemote = sEJBGeneralRemoteHome.create();

            return sEJBGeneralRemote;
        } catch (Exception ex) {
            System.out.println(
                "Exception : [GeneralService][getSEJBGeneralRemote][" +
                ex.getMessage() + "]");

            return null;
        }
    }

    public static SEJBOrderNewRemote getSEJBOrderNewRemote() {
        try {
            final Context context = MiUtil.getInitialContext();
            final SEJBOrderNewRemoteHome SEJBOrderNewRemoteHome = (SEJBOrderNewRemoteHome) PortableRemoteObject.narrow(context.lookup(
                        "SEJBOrderNew"), SEJBOrderNewRemoteHome.class);
            SEJBOrderNewRemote SEJBOrderNewRemote;
            SEJBOrderNewRemote = SEJBOrderNewRemoteHome.create();

            return SEJBOrderNewRemote;
        } catch (Exception e) {
            logger.error(formatException(e));

            return null;
        }
    }

    public static SEJBRepairRemote getSEJBRepairRemote() {
        try {
            final Context context = MiUtil.getInitialContext();
            final SEJBRepairRemoteHome SEJBRepairRemoteHome = (SEJBRepairRemoteHome) PortableRemoteObject.narrow(context.lookup(
                        "SEJBRepair"), SEJBRepairRemoteHome.class);
            SEJBRepairRemote SEJBRepairRemote;
            SEJBRepairRemote = SEJBRepairRemoteHome.create();

            return SEJBRepairRemote;
        } catch (Exception e) {
            logger.error(formatException(e));

            return null;
        }
    }

    public HashMap getRepairByOrder(long lOrderId) {
        HashMap hshData = new HashMap();

        try {
            hshData = getSEJBOrderNewRemote().getRepairByOrder(lOrderId);
        } catch (Throwable t) {
            manageCatch(hshData, t);
        }

        return hshData;
    }

    public HashMap getRepairReplaceList(String strRepairId) {
        HashMap hshData = new HashMap();

        try {
            return getSEJBOrderNewRemote().getRepairReplaceList(strRepairId);
        } catch (SQLException sqle) {
            logger.error(formatException(sqle));
            hshData.put(Constante.MESSAGE_OUTPUT, formatException(sqle));
        } catch (RemoteException re) {
            logger.error(formatException(re));
            hshData.put(Constante.MESSAGE_OUTPUT, formatException(re));
        } catch (Exception e) {
            logger.error(formatException(e));
            hshData.put(Constante.MESSAGE_OUTPUT, formatException(e));
        }

        return hshData;
    }

    public HashMap generateDocument(HashMap hshParametrosMap) {
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBOrderNewRemote().generateDocument(hshParametrosMap);
        } catch (Throwable t) {
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }

    public HashMap reportRepair(String strReportName, long lRepairId) {
        HashMap hshData = new HashMap();

        try {
            hshData = getSEJBOrderNewRemote().reportRepair(strReportName,
                    lRepairId);
        } catch (SQLException sqle) {
            logger.error(formatException(sqle));
            hshData.put(Constante.MESSAGE_OUTPUT, formatException(sqle));
        } catch (RemoteException re) {
            logger.error(formatException(re));
            hshData.put(Constante.MESSAGE_OUTPUT, formatException(re));
        } catch (Exception e) {
            logger.error(formatException(e));
            hshData.put(Constante.MESSAGE_OUTPUT, formatException(e));
        }

        return hshData;
    }

    public HashMap getLastImeiRepair(long lRepairId) {
        HashMap hshData = new HashMap();

        try {
            hshData = getSEJBOrderNewRemote().getLastImeiRepair(lRepairId);
        } catch (SQLException sqle) {
            logger.error(formatException(sqle));
            hshData.put(Constante.MESSAGE_OUTPUT, formatException(sqle));
        } catch (RemoteException re) {
            logger.error(formatException(re));
            hshData.put(Constante.MESSAGE_OUTPUT, formatException(re));
        } catch (Exception e) {
            logger.error(formatException(e));
            hshData.put(Constante.MESSAGE_OUTPUT, formatException(e));
        }

        return hshData;
    }

    public HashMap generateDocumentRepair(HashMap hshParametrosMap) {
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBOrderNewRemote().generateDocumentRepair(hshParametrosMap);
        } catch (Throwable t) {
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }

    /* JOYOLAR 24/07/2008 */
    public HashMap getCodEquipFromImei(String strImei) {
        HashMap hshData = new HashMap();

        try {
            hshData = getSEJBOrderNewRemote().getCodEquipFromImei(strImei);
        } catch (SQLException sqle) {
            logger.error(formatException(sqle));
            hshData.put(Constante.MESSAGE_OUTPUT, formatException(sqle));
        } catch (RemoteException re) {
            logger.error(formatException(re));
            hshData.put(Constante.MESSAGE_OUTPUT, formatException(re));
        } catch (Exception e) {
            logger.error(formatException(e));
            hshData.put(Constante.MESSAGE_OUTPUT, formatException(e));
        }

        return hshData;
    }

    public String valImeiPrestamoCambio(RepairBean objRepairBean,
        HashMap hshParametrosMap) {
        try {
            return getSEJBOrderNewRemote().valImeiPrestamoCambio(objRepairBean,
                hshParametrosMap);
        } catch (RemoteException re) {
            System.out.println(
                "RepairService valImeiPrestamoCambio Remote\nMensaje:" +
                re.getMessage() + "\n");

            String sMensaje =
                "RepairService valImeiPrestamoCambio Remote\nMensaje:" +
                re.getMessage() + "\n";
            re.printStackTrace();

            return sMensaje;
        } catch (Exception ex) {
            System.out.println(
                "RepairService valImeiPrestamoCambio Exception\nMensaje:" +
                ex.getMessage() + "\n");

            String sMensaje =
                "RepairService valImeiPrestamoCambio Exception\nMensaje:" +
                ex.getMessage() + "\n";
            ex.printStackTrace();

            return sMensaje;
        }
    }

  // PCASTILLO - Despacho en Tienda - Validaci鏮 de Stock
    public HashMap valStockPrestCambio(RepairBean objRepairBean, HashMap hshParametrosMap) {
        
        HashMap hshData = new HashMap();
        try {
            return getSEJBOrderNewRemote().valStockPrestCambio(objRepairBean,
                hshParametrosMap);
        } catch (SQLException sqle) {
            logger.error(formatException(sqle));
            hshData.put("strMessage", formatException(sqle));
        } catch (RemoteException re) {
            logger.error(formatException(re));
            hshData.put("strMessage", formatException(re));
        } catch (Exception e) {
            logger.error(formatException(e));
            hshData.put("strMessage", formatException(e));
        }

        return hshData;
    }

    /**
    * @see pe.com.nextel.dao.RepairDAO#getResolution()
    */
    public HashMap getResolution(String strFabricatorId) {
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBGeneralRemote().getResolution(strFabricatorId);
        } catch (Throwable t) {
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }

    /**
    * @see pe.com.nextel.dao.RepairDAO#getDiagnosis()
    */
    public HashMap getDiagnosis(String intProviderId) {
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBGeneralRemote().getDiagnosis(intProviderId);
        } catch (Throwable t) {
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }

    /**
    * @see pe.com.nextel.dao.RepairDAO#getFailureList()
    */
    public HashMap getFailureList(int intValue, int intRepairId,
        String strRepairTypeId) {
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBGeneralRemote().getFailureList(intValue,
                    intRepairId, strRepairTypeId);
        } catch (Throwable t) {
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }

    /**
     * @see pe.com.nextel.dao.RepairDAO#getDiagnosis()
     */
    public String getDiagnosisDescription(int intValue) {
        String strDiagnosisDescription = null;

        try {
            strDiagnosisDescription = getSEJBGeneralRemote()
                                          .getDiagnosisDescription(intValue);
        } catch (Exception e) {
            logger.error(formatException(e));
        }

        return strDiagnosisDescription;
    }

    /**
    * @see pe.com.nextel.dao.RepairDAO#getSparePartListByModel()
    */

    /*public HashMap getSparePartsListByModel(String strValue) {
      HashMap hshDataMap = new HashMap();
      try{
        hshDataMap = getSEJBGeneralRemote().getSparePartsListByModel(strValue);
      }catch (Throwable t){
        manageCatch(hshDataMap, t);
      }
      return hshDataMap;
    }*/
    public HashMap getSparePartsListByModel(String strValue, int intRepairId,
        String strRepairTypeId) {
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBGeneralRemote().getSparePartsListByModel(strValue,
                    intRepairId, strRepairTypeId);
        } catch (Throwable t) {
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }

    /**
    * @see pe.com.nextel.dao.RepairDAO#getSelectedFailureList(intValue)
    */
    public HashMap getSelectedFailureList(int intValue, String strRepairTypeId) {
        HashMap hsdDataMap = new HashMap();

        try {
            hsdDataMap = getSEJBGeneralRemote().getSelectedFailureList(intValue,
                    strRepairTypeId);
        } catch (Throwable t) {
            manageCatch(hsdDataMap, t);
        }

        return hsdDataMap;
    }

    /**
        * @see pe.com.nextel.dao.GeneralDAO#getModelList()
        */
    public HashMap getTypeServices() {
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBGeneralRemote().getTypeServices();
        } catch (Throwable t) {
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }

    /**
        * @see pe.com.nextel.dao.GeneralDAO#getModelList()
        */
    public HashMap getloadServices(String strTipoReparacion) {
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBGeneralRemote().getloadServices(strTipoReparacion);
        } catch (Throwable t) {
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }

    /**
    * @see pe.com.nextel.dao.RepairDAO#newOrderReparation(objHashMap)
    */
    public HashMap newOrderReparation(HashMap objHashMap) {
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBGeneralRemote().newOrderReparation(objHashMap);
        } catch (Throwable t) {
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }

    /**
     * @see pe.com.nextel.dao.RepairDAO#getSelectedSpareList(intValue, String strRepairTypeId)
     */
    public HashMap getSelectedSpareList(int intValue, String strRepairTypeId) {
        HashMap hsdDataMap = new HashMap();

        try {
            hsdDataMap = getSEJBGeneralRemote().getSelectedSpareList(intValue,
                    strRepairTypeId);
        } catch (Throwable t) {
            manageCatch(hsdDataMap, t);
        }

        return hsdDataMap;
    }

    /**
    * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tom嫳 Mogrovejo</a>
    * <br>Fecha: 02/04/2009
    * @see pe.com.nextel.dao.GeneralDAO#getImeiDetail(RepairBean)
    */
    public HashMap /*String*/ validateTrueBounce(String strImei,
        long intFallaid, String strBounce) {
        /*String strValidateTruebounce = null;*/
        HashMap hshDataMap = new HashMap();

        try {
            /*strValidateTruebounce = getSEJBGeneralRemote().validateTrueBounce(strImei,intFallaid,strBounce);*/
            hshDataMap = getSEJBGeneralRemote().validateTrueBounce(strImei,
                    intFallaid, strBounce);
        } catch (Exception e) {
            logger.error(formatException(e));
        }

        return hshDataMap /*strValidateTruebounce*/;
    }

    /**
     * @see pe.com.nextel.dao.RepairDAO#getRepairCount(strValue)
     */
    public HashMap getRepairCount(String strValue) {
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBGeneralRemote().getRepairCount(strValue);
        } catch (Throwable t) {
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }

    /**
    * @see pe.com.nextel.dao.RepairDAO#getSmallRepairDetail(strValue)
    */
    public HashMap getSmallRepairDetail(String strValue) {
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBGeneralRemote().getSmallRepairDetail(strValue);
        } catch (Throwable t) {
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }

    /**
    * @see pe.com.nextel.dao.RepairDAO#getNpRepairBOF(intValue)
    */
    public HashMap getNpRepairBOF(int intValue, String strLogin) {
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBGeneralRemote().getNpRepairBOF(intValue,
                    strLogin);
        } catch (Throwable t) {
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }
    
    public String getDescriptionUserResponse(String valor){
    System.out.println("getDescriptionUserResponse valor){: " + valor);     
        if(valor==null || valor.isEmpty())return "";
        switch(Integer.parseInt(valor)){
        case 0: return Constante.ESTADO_COTIZACION_PENDIENTE;
        case 1: return Constante.ESTADO_COTIZACION_ESPERA;
        case 2: return Constante.ESTADO_COTIZACION_APROBADO;
        case 3: return Constante.ESTADO_COTIZACION_RECHAZADO;
        case 4: return Constante.ESTADO_COTIZACION_DEVOLUCION;
        default: return "";
        }
    }

    /**
    * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tom嫳 Mogrovejo</a>
    * <br>Fecha: 06/04/2009
    * @see pe.com.nextel.dao.GeneralDAO#activateEquipment(RepairBean)
    */
    public HashMap activateEquipment(String strImei, String strImeiNuevo,
        String strSim, String strSimNuevo, String strReplaceType) {
        String strMessage = null;
        HashMap hshData = new HashMap();

        try {
            hshData = getSEJBGeneralRemote().activateEquipment(strImei,
                    strImeiNuevo, strSim, strSimNuevo, strReplaceType);
        } catch (Exception e) {
            logger.error(formatException(e));
        }

        return hshData;
    }

    /**
    * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tom嫳 Mogrovejo</a>
    * <br>Fecha: 14/04/2009
    * @see pe.com.nextel.dao.GeneralDAO#getRepairid(RepairBean)
    */
    public HashMap getRepairid(String strImei) {
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBGeneralRemote().getRepairid(strImei);
        } catch (Throwable t) {
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }

    /**
    * @see pe.com.nextel.dao.RepairDAO#GenerateInvDoc(HashMap hshParameters)
    */
    public HashMap GenerateInvDoc(HashMap hshParameters) {
        HashMap hshResult = new HashMap();

        try {
            hshResult = getSEJBGeneralRemote().GenerateInvDoc(hshParameters);
        } catch (Throwable t) {
            manageCatch(hshResult, t);
        }

        return hshResult;
    }

    /**
      * @see pe.com.nextel.dao.GeneralDAO#getModelList()
      */
    public HashMap getServices() {
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBGeneralRemote().getServices();
        } catch (Throwable t) {
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }

    /**
      * @see pe.com.nextel.dao.RepairDAO#getAccesories()
      */
    public HashMap getAccesories() {
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBGeneralRemote().getAccesories();
        } catch (Throwable t) {
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }

    /**
      * @see pe.com.nextel.dao.RepairDAO#getAccesoryModels(String strAccesoryType)
      */
    public HashMap getAccesoryModels(String strAccesoryType) {
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBGeneralRemote().getAccesoryModels(strAccesoryType);
        } catch (Throwable t) {
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }

    /**
    * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tom嫳 Mogrovejo</a>
    * <br>Fecha: 09/06/2009
    * @see pe.com.nextel.dao.GeneralDAO#getRepairid(RepairBean)
    */
    public HashMap getDocGenerate(String strImei, long strRepairId) {
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBGeneralRemote().getDocGenerate(strImei,
                    strRepairId);
        } catch (Throwable t) {
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }

    /**
    * @see pe.com.nextel.dao.RepairDAO#getDiagnosisLevel()
    */
    public HashMap getDiagnosisLevel(String strProviderId, int intLevel) {
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBGeneralRemote().getDiagnosisLevel(strProviderId,
                    intLevel);
        } catch (Throwable t) {
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }

    /**
    * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tom嫳 Mogrovejo</a>
    * <br>Fecha: 09/06/2009
    * @see pe.com.nextel.dao.GeneralDAO#getRepairid(RepairBean)
    */
    public HashMap getDocGenClose(String strImei, long strRepairId) {
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBGeneralRemote().getDocGenClose(strImei,
                    strRepairId);
        } catch (Throwable t) {
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }

    /**
    * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tom嫳 Mogrovejo</a>
    * <br>Fecha: 16/06/2009
    * @see pe.com.nextel.dao.GeneralDAO#getImeiDetail(RepairBean)
    */
    public HashMap validateStock(long intFallaid, long intSpecification,
        String strLogin) {
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBGeneralRemote().validateStock(intFallaid,
                    intSpecification, strLogin);
        } catch (Exception e) {
            logger.error(formatException(e));
        }

        return hshDataMap;
    }

    public HashMap validateSim(String strSim) {
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBGeneralRemote().validateSim(strSim);
        } catch (Throwable t) {
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }

    public String getReplaceType(String orderId) {
        String strReplaceType = null;

        try {
            strReplaceType = getSEJBGeneralRemote().getReplaceType(orderId);
        } catch (Exception e) {
            logger.error(formatException(e));
        }

        return strReplaceType;
    }

    public String getValidateActiveEquipment(String imei, String sim,
        String replaceType) {
        String strActive = null;

        try {
            strActive = getSEJBGeneralRemote().getValidateActiveEquipment(imei,
                    sim, replaceType);
        } catch (Exception e) {
            logger.error(formatException(e));
        }

        return strActive;
    }

    public String getValidInternetNextel(long orderId) {
        String strShow = null;

        try {
            strShow = getSEJBGeneralRemote().getValidInternetNextel(orderId);
        } catch (Exception e) {
            logger.error(formatException(e));
        }

        return strShow;
    }

    public HashMap getFinalEstateList() {
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBGeneralRemote().getFinalEstateList();
        } catch (Throwable t) {
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }

    public HashMap getEquipSOList() {
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBGeneralRemote().getEquipSOList();
        } catch (Throwable t) {
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }

    public int validateChangeProcess(long orderId) {
        int iValidateChangeProcess = 0;

        try {
            iValidateChangeProcess = getSEJBGeneralRemote()
                                         .validateChangeProcess(orderId);
        } catch (Exception e) {
            logger.error(formatException(e));
        }

        return iValidateChangeProcess;
    }

    public String getTechnologyByImei(String imei) {
        String strTechnology = "";

        try {
            strTechnology = getSEJBGeneralRemote().getTechnologyByImei(imei);
        } catch (Exception e) {
            logger.error(formatException(e));
        }

        return strTechnology;
    }

    /**
    * @see pe.com.nextel.dao.RepairDAO#validateSpecOrders(HashMap hshParameters)
    */
    public HashMap validateSpecOrders(HashMap hshParameters) {
        HashMap hshResult = new HashMap();

        try {
            hshResult = getSEJBGeneralRemote().validateSpecOrders(hshParameters);
        } catch (Throwable t) {
            manageCatch(hshResult, t);
        }

        return hshResult;
    }

    /**
     * @see pe.com.nextel.dao.RepairDAO#generateNacionalizacion(HashMap hshParameters)
     */
    public HashMap generateNacionalizacion(HashMap hshParameters) {
        HashMap hshResult = new HashMap();

        try {
            hshResult = getSEJBRepairRemote().generateNacionalizacion(hshParameters);
        } catch (Throwable t) {
            manageCatch(hshResult, t);
        }

        return hshResult;
    }

    /**
      * @see pe.com.nextel.dao.RepairDAO#generateRAFile(HashMap hshParameters)
      */
    public HashMap generateRAFile(HashMap hshParameters) {
        HashMap hshResult = new HashMap();

        try {
            hshResult = getSEJBRepairRemote().generateRAFile(hshParameters);
        } catch (Throwable t) {
            manageCatch(hshResult, t);
        }

        return hshResult;
    }

    /**
     * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
     * <br>Fecha: 15/11/2010
     * @see pe.com.nextel.dao.ItemDAO#transferExtendedGuarantee(strImei,strImeiNuevo)
     */
    public HashMap transferExtendedGuarantee(String strImei, String strImeiNuevo) {
        HashMap hshData = new HashMap();

        try {
            hshData = getSEJBOrderNewRemote().transferExtendedGuarantee(strImei,
                    strImeiNuevo);
        } catch (Exception e) {
            logger.error(formatException(e));
        }

        return hshData;
    }
    
    /**
     * <br>Realizado por: <a href="mailto:miguel.montoya@hp.com">Miguel 聲gel Montoya</a>
     * <br>Fecha: 12/07/2012
     * @see pe.com.nextel.dao.RepairDAO#getBscsModelId(modelName)
     */
    public HashMap getBscsModelId(String modelName) {
        HashMap hshData = new HashMap();

        try {
            hshData = getSEJBRepairRemote().getBscsModelId(modelName);
        } catch (Exception e) {
            logger.error(formatException(e));
        }

        return hshData;
    }
    
    /**
     * <br>Realizado por: <a href="mailto:miguel.montoya@hp.com">Miguel 聲gel Montoya</a>
     * <br>Fecha: 12/07/2012
     * @see pe.com.nextel.dao.RepairDAO#existsImei(imei)
     */
    public HashMap existsImei(String imei) {
        HashMap hshData = new HashMap();

        try {
            hshData = getSEJBRepairRemote().existsImei(imei);
        } catch (Exception e) {
            logger.error(formatException(e));
        }

        return hshData;
    }
    
    /**
     * <br>Realizado por: <a href="mailto:miguel.montoya@hp.com">Miguel 聲gel Montoya</a>
     * <br>Fecha: 13/07/2012
     */
    public HashMap registerEquipmentNew(String imei, String numSerie, String modelId) {
        HashMap hshData = new HashMap();

        try {
            hshData = getSEJBRepairRemote().registerEquipmentNew(imei, numSerie, modelId);
        } catch (Exception e) {
            logger.error(formatException(e));
        }

        return hshData;
    }
    
    /**
     * <br>Realizado por: <a href="mailto:miguel.montoya@hp.com">Miguel 聲gel Montoya</a>
     * <br>Fecha: 13/07/2012
     */
    public HashMap getModels(long orderId) {
        HashMap hshResult = new HashMap();

        try {
            hshResult = getSEJBRepairRemote().getModels(orderId);
        } catch (Throwable t) {
            manageCatch(hshResult, t);
        }

        return hshResult;
    }
    
    /**
     * <br>Realizado por: <a href="mailto:yimy.ruiz@asistp.com">Yimy Ruiz</a>
     * <br>Fecha: 15/11/2010
     * @see pe.com.nextel.dao.ItemDAO#transferExtendedGuarantee(strImei,strImeiNuevo)
     */
    public String getPlanType(String strPhone, String strImei) {
        String strPlanType=null;

        try {
            strPlanType = this.getSEJBRepairRemote().getPlanType(strPhone, strImei);
        } catch (Exception e) {
            logger.error(formatException(e));
        }
        return strPlanType;
    }
    
    public HashMap getConfigurations(String strParam) {          
      HashMap hshDataMap = new HashMap();
        try {
          return getSEJBGeneralRemote().getConfigurations(strParam); 
        }catch(Throwable t){
          manageCatch(hshDataMap, t);
        }
      return hshDataMap; 
    }
    
    public HashMap getComboTiendaList() {          
      HashMap hshDataMap = new HashMap();
        try {
          return getSEJBGeneralRemote().getComboTiendaList(); 
        }catch(Throwable t){
          manageCatch(hshDataMap, t);
        }
      return hshDataMap; 
    }

        public byte[][] getBarCodeImg(String[] codes, int ancho_img, int alto_img, int text_font) throws Exception {
                byte[][] resultByte=null;
                if(codes.length>0){
                        resultByte=new byte[codes.length][];
                        int i=0;
                        for (String code : codes) {
                                resultByte[i]=getByteFromBarCodeImage(code, ancho_img, alto_img, text_font);
                                i++;
                        }
                }
                
                return resultByte;
        }
            
        public byte[] getByteFromBarCodeImage(String code, int ancho_img, int alto_img, int text_font) throws Exception{
                JBarcodeBean barcode = new JBarcodeBean();
                System.out.println("code ingresado: "+code);
                // nuestro tipo de codigo de barra
                barcode.setCodeType(new Code128());
                // barcode.setCodeType(new Code39());

                // nuestro valor a codificar y algunas configuraciones mas
                barcode.setCode(code);
                barcode.setCheckDigit(true);
                barcode.setBarcodeHeight(30);
                if(text_font>0)barcode.setFont(new Font("Arial",0,text_font));
                barcode.setLabelPosition (JBarcodeBean.LABEL_BOTTOM);

                BufferedImage bufferedImage = barcode.draw(new BufferedImage(ancho_img, alto_img,
                                BufferedImage.TYPE_INT_RGB));

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "jpg", baos);
                baos.flush();
                byte[] imageInByte = baos.toByteArray();
                baos.close();
                return imageInByte;
        }

    /**
      * @see pe.com.nextel.dao.RepairDAO#getDataLoanForReport(String repairId)
      */
    public HashMap getDataLoanForReport(String repairId) {
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBGeneralRemote().getDataLoanForReport(repairId);
        } catch (Throwable t) {
            t.printStackTrace();
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }

    /**
      * @see pe.com.nextel.dao.RepairDAO#getDamageList(String repairId)
      */
    public HashMap getDamageList(String repairListId, String model) {
        HashMap hshDataMap = new HashMap();
        List<EquipmentDamage> lista = new ArrayList<EquipmentDamage>();

        try {
            hshDataMap = getSEJBGeneralRemote().getDamageList(repairListId, model);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return hshDataMap;
    }
    
    /**
     * Motivo: Obtiene una lista de los centros de Reparaciones
     * <br>Realizado por: <a href="mailto:paolo.ortega@teamsoft.com.pe">Paolo Ortega Ramirez</a>
     * <br>Fecha: 20/05/2014
     * @return    HashMap
     */
    public HashMap getCentroReparacion(){
        
        HashMap hshDataMap = new HashMap();

        try {

            hshDataMap = getSEJBRepairRemote().getCentroReparacion();

        } catch (Throwable t) {
            t.printStackTrace();
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }
    
     /**
      * Motivo: Obtiene la cantidad de reparaciones
      * <br>Realizado por: <a href="mailto:carlos.delossantos@teamsoft.com.pe">Carlos De los santos</a>
      * <br>Fecha: 27/05/2014
      * @return    Int
      */
    public int getCantReparaciones(String strIMEI){
        
        int cant = 0;

        try {

            cant = getSEJBRepairRemote().getCantReparaciones(strIMEI);

        } catch (Throwable t) {
            t.printStackTrace();
        }

        return cant;
    }

    /**
     * @see pe.com.nextel.dao.RepairDAO#getOtherFailureList(int intRepairId, String strRepairTypeId)
     */
    public HashMap getOtherFailureList(int intRepairId, String strRepairTypeId) throws SQLException, Exception {
        HashMap hsdDataMap = new HashMap();
        try {
            hsdDataMap = getSEJBRepairRemote().getOtherFailureList(intRepairId, strRepairTypeId);
        } catch (Throwable t) {
            manageCatch(hsdDataMap, t);
        }
        return hsdDataMap;
    }
    
    /**
     * @see pe.com.nextel.dao.RepairDAO#getSelectedOtherFailureList(int intRepairId, String strRepairTypeId)
     */
    public HashMap getSelectedOtherFailureList(int intRepairId, String strRepairTypeId) {
        HashMap hsdDataMap = new HashMap();
        try {
            hsdDataMap = getSEJBRepairRemote().getSelectedOtherFailureList(intRepairId, strRepairTypeId);
        } catch (Throwable t) {
            manageCatch(hsdDataMap, t);
        }
        return hsdDataMap;
    }
}
