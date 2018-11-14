package pe.com.nextel.service;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.naming.Context;
import javax.rmi.PortableRemoteObject;

import pe.com.nextel.bean.ItemBean;
import pe.com.nextel.bean.ItemDeviceBean;
import pe.com.nextel.bean.ItemServiceBean;
import pe.com.nextel.bean.OrderBean;
import pe.com.nextel.bean.PlanTarifarioBean;
import pe.com.nextel.bean.ProductBean;
import pe.com.nextel.bean.ServiciosBean;
import pe.com.nextel.ejb.SEJBOrderNewRemote;
import pe.com.nextel.ejb.SEJBOrderNewRemoteHome;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;

/*JOYOLAR 04/08/2008
 * */
import pe.com.nextel.exception.UserException;

/**
 * Developer : Lee Rosales
 * Objetivo  : Interface que provee los servicios del EJB
 *             para ser consumidos por la capa Controller
 */
 
public class NewOrderService extends GenericService implements Serializable{

    public static SEJBOrderNewRemote getSEJBOrderNewRemote() {
        try{
            final Context context = MiUtil.getInitialContext();
            final SEJBOrderNewRemoteHome sEJBOrderNewRemoteHome =
                    (SEJBOrderNewRemoteHome) PortableRemoteObject.narrow( context.lookup( "SEJBOrderNew" ), SEJBOrderNewRemoteHome.class );
            SEJBOrderNewRemote sEJBOrderNewRemote;
            sEJBOrderNewRemote = sEJBOrderNewRemoteHome.create();

            return sEJBOrderNewRemote;
        }catch(Exception ex) {
            System.out.println("Exception : [NewOrderService][getSEJBOrderNewRemote]["+ex.getMessage()+"]");
            return null;
        }

    }

    public static ArrayList OrderDAOgetSolutionList(String idSolution){
        try {
            return getSEJBOrderNewRemote().OrderDAOgetSolutionList(idSolution);
        }catch (SQLException e) {
            System.out.println("Error [NewOrderService][OrderDAOgetSolutionList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }catch (RemoteException e) {
            return null;
        }
        catch (Exception e) {
            System.out.println("Error [NewOrderService][OrderDAOgetSolutionList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
    }


    public HashMap CategoryDAOgetSpecificationData(int idSpecification, String strGeneratorType){
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBOrderNewRemote().CategoryDAOgetSpecificationData(idSpecification, strGeneratorType);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][CategoryDAOgetSpecificationData][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][CategoryDAOgetSpecificationData][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][CategoryDAOgetSpecificationData][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    public static ArrayList ItemDAOgetHeaderSpecGrp(int idSpecification){
        String  strMessage = new String();
        try {
            return getSEJBOrderNewRemote().ItemDAOgetHeaderSpecGrp(idSpecification);
        }catch (SQLException e) {
            System.out.println("Error [NewOrderService][ItemDAOgetHeaderSpecGrp][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (RemoteException e) {
            return null;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][ItemDAOgetHeaderSpecGrp][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            return null;
        }
    }

    public static ArrayList ItemDAOgetItemHeaderSpecGrp(int idSpecification){
        String  strMessage = new String();
        try {
            return getSEJBOrderNewRemote().ItemDAOgetItemHeaderSpecGrp(idSpecification);
        }catch (SQLException e) {
            System.out.println("Error [NewOrderService][ItemDAOgetItemHeaderSpecGrp][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (RemoteException e) {
            return null;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][ItemDAOgetItemHeaderSpecGrp][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            return null;
        }
    }

    public static Hashtable OrderDAOgetOrderIdNew(){
        try {
            return getSEJBOrderNewRemote().OrderDAOgetOrderIdNew();
        }catch (SQLException e) {
            System.out.println("Error : [" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (RemoteException e) {
            return null;
        }catch (Exception e) {
            System.out.println("Error : [" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
    }

    public static ArrayList OrderDAOgetSalesList(int intUserId, int intAppId){
        try {
            return getSEJBOrderNewRemote().OrderDAOgetSalesList(intUserId,intAppId);
        }catch (SQLException e) {
            System.out.println("Error [NewOrderService][OrderDAOgetSalesList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (RemoteException e) {
            return null;
        }catch (Exception e) {
            System.out.println("Error [NewOrderService][OrderDAOgetSalesList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
    }

    public static HashMap OrderDAOgetDispatchPlaceList(int intSpecialtyId){
        try {
            return getSEJBOrderNewRemote().OrderDAOgetDispatchPlaceList(intSpecialtyId);
        }catch (SQLException e) {
            System.out.println("Error [NewOrderService][OrderDAOgetDispatchPlaceList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (RemoteException e) {
            return null;
        }
        catch (Exception e) {
            System.out.println("Error [NewOrderService][OrderDAOgetDispatchPlaceList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
    }

    public static HashMap OrderDAOgetBuildingName(int intBuildingid, String strLogin){
        try {
            return getSEJBOrderNewRemote().OrderDAOgetBuildingName(intBuildingid, strLogin);
        }catch (SQLException e) {
            System.out.println("Error [SQLException][NewOrderService][OrderDAOgetBuildingName][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (RemoteException e) {
            System.out.println("Error [RemoteException][NewOrderService][OrderDAOgetBuildingName][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (Exception e) {
            System.out.println("Error [SQLException][NewOrderService][OrderDAOgetBuildingName][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
    }

    //JLIMAYMANTA
    //METODO QUE RETORNA EL TIPO DE SERVICIO(TE)
    public static HashMap OrderDAOgetBuildingTS(int intBuildingid, String strLogin){
        try {
            return getSEJBOrderNewRemote().OrderDAOgetBuildingTS(intBuildingid, strLogin);
        }catch (SQLException e) {
            System.out.println("Error [SQLException][NewOrderService][OrderDAOgetBuildingTS][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (RemoteException e) {
            System.out.println("Error [RemoteException][NewOrderService][OrderDAOgetBuildingTS][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (Exception e) {
            System.out.println("Error [SQLException][NewOrderService][OrderDAOgetBuildingTS][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
    }

    public static ArrayList OrderDAOgetModePaymentList(String strParamName, String strParamStatus){
        try {
            return getSEJBOrderNewRemote().OrderDAOgetModePaymentList( strParamName,  strParamStatus);
        }catch (SQLException e) {
            System.out.println("Error [SQLException][NewOrderService][OrderDAOgetModePaymentList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (RemoteException e) {
            System.out.println("Error [RemoteException][NewOrderService][OrderDAOgetModePaymentList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }catch (Exception e) {
            System.out.println("Error [SQLException][NewOrderService][OrderDAOgetModePaymentList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
    }

    public HashMap OrderDAOgetModalityList(long intSpecificationId, String strEquipment, String strWarrant, String strEquipmentReturn){
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBOrderNewRemote().OrderDAOgetModalityList( intSpecificationId, strEquipment, strWarrant, strEquipmentReturn);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][OrderDAOgetModalityList][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][OrderDAOgetModalityList][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][OrderDAOgetModalityList][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    public static ArrayList ProductLineDAOgetProductLineValueList(int iProductLineId, String strMessage){
        try {
            return getSEJBOrderNewRemote().ProductLineDAOgetProductLineValueList( iProductLineId,  strMessage);
        }catch (SQLException e) {
            System.out.println("Error [SQLException][NewOrderService][ProductLineDAOgetProductLineValueList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (RemoteException e) {
            System.out.println("Error [RemoteException][NewOrderService][ProductLineDAOgetProductLineValueList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (Exception e) {
            System.out.println("Error [Exception][NewOrderService][ProductLineDAOgetProductLineValueList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
    }

    public static ArrayList ProductDAOgetProductList(ProductBean productBean, String strMessage){
        try {
            return getSEJBOrderNewRemote().ProductDAOgetProductList( productBean,  strMessage);
        }catch (SQLException e) {
            System.out.println("Error [SQLException][NewOrderService][ProductDAOgetProductList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (RemoteException e) {
            System.out.println("Error [RemoteException][NewOrderService][ProductDAOgetProductList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (Exception e) {
            System.out.println("Error [Exception][NewOrderService][ProductDAOgetProductList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
    }
    public HashMap PlanDAOgetPlanList(PlanTarifarioBean planTarifarioBean, String type){
        HashMap hshDataMap = new HashMap();
        try {
            //hshDataMap = objGeneralDAO.getComboRegionList();
            hshDataMap = getSEJBOrderNewRemote().PlanDAOgetPlanList( planTarifarioBean,  type);
        }catch(Throwable t){
            manageCatch(hshDataMap, t);
        }
        return hshDataMap;
    }

    public static ArrayList EquipmentDAOgetProductList(String ownhandset, String consignmen, String strMessage){
        try {
            return getSEJBOrderNewRemote().EquipmentDAOgetProductList( ownhandset,  consignmen, strMessage);
        }catch (SQLException e) {
            System.out.println("Error [SQLException][NewOrderService][EquipmentDAOgetProductList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (RemoteException e) {
            System.out.println("Error [RemoteException][NewOrderService][EquipmentDAOgetProductList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (Exception e) {
            System.out.println("Error [Exception][NewOrderService][EquipmentDAOgetProductList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
    }

    public static String SpecificationDAOgetConsigmentValue(int intSpecificationID, String strMessage){
        try {
            return getSEJBOrderNewRemote().SpecificationDAOgetConsigmentValue(intSpecificationID,strMessage);
        }catch (SQLException e) {
            System.out.println("Error [SQLException][NewOrderService][SpecificationDAOgetConsigmentValue][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (RemoteException e) {
            System.out.println("Error [RemoteException][NewOrderService][SpecificationDAOgetConsigmentValue][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (Exception e) {
            System.out.println("Error [Exception][NewOrderService][SpecificationDAOgetConsigmentValue][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
    }

    public static ArrayList ServiceDAOgetServiceAllList(int intSolutionId, String strMessage){
        try {
            return getSEJBOrderNewRemote().ServiceDAOgetServiceAllList(intSolutionId,strMessage);
        }catch (SQLException e) {
            System.out.println("Error [SQLException][NewOrderService][OrderDAOgetServiceAllList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (RemoteException e) {
            System.out.println("Error [RemoteException][NewOrderService][OrderDAOgetServiceAllList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (Exception e) {
            System.out.println("Error [Exception][NewOrderService][OrderDAOgetServiceAllList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
    }

    public static ArrayList ServiceDAOgetServiceDefaultList(String strObjectType, int intSpecificationId, String strMessage){
        try {
            return getSEJBOrderNewRemote().ServiceDAOgetServiceDefaultList(strObjectType,intSpecificationId,strMessage);
        }catch (SQLException e) {
            System.out.println("Error [SQLException][NewOrderService][ServiceDAOgetServiceDefaultList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (RemoteException e) {
            System.out.println("Error [RemoteException][NewOrderService][ServiceDAOgetServiceDefaultList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (Exception e) {
            System.out.println("Error [Exception][NewOrderService][ServiceDAOgetServiceDefaultList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
    }

    public static ArrayList OrderDAOgetCategoryList(int idSolution){
        try {
            return getSEJBOrderNewRemote().OrderDAOgetCategoryList(idSolution);
        }catch (SQLException e) {
            System.out.println("Error [SQLException][NewOrderService][OrderDAOgetCategoryList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (RemoteException e) {
            System.out.println("Error [RemoteException][NewOrderService][OrderDAOgetCategoryList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (Exception e) {
            System.out.println("Error [SQLException][NewOrderService][OrderDAOgetCategoryList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
    }

    public static ArrayList OrderDAOgetSubCategoryList(String strCategory){
        try {
            return getSEJBOrderNewRemote().OrderDAOgetSubCategoryList(strCategory);
        }catch (SQLException e) {
            System.out.println("Error [SQLException][NewOrderService][OrderDAOgetSubCategoryList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (RemoteException e) {
            System.out.println("Error [RemoteException][NewOrderService][OrderDAOgetSubCategoryList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (Exception e) {
            System.out.println("Error [SQLException][NewOrderService][OrderDAOgetSubCategoryList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
    }

    public HashMap OrderDAOgetOrderInsertar(OrderBean orderBean,Connection conn){
        HashMap objHashMap = new HashMap();
        String strMessage = null;
        try {
            return getSEJBOrderNewRemote().OrderDAOgetOrderInsertar(orderBean,conn);
        }catch (Exception e) {
            System.out.println("Error [SQLException][NewOrderService][OrderDAOgetOrderInsertar][" + e.getMessage() + "]["+e.getClass()+"]");
            strMessage  = "Error [SQLException][NewOrderService][OrderDAOgetOrderInsertar][" + e.getMessage() + "]["+e.getClass()+"]";
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }

    }

    public static String ItemDAOgetItemServiceInsertar(ItemServiceBean itemServiceBean,Connection conn){

        try {
            //Insertar los Items de Ordenes
            return getSEJBOrderNewRemote().ItemDAOgetItemServiceInsertar(itemServiceBean,conn);
        }catch (Exception e) {
            System.out.println("Error [SQLException][NewOrderService][ItemDAOgetItemServiceInsertar][" + e.getMessage() + "]["+e.getClass()+"]");
            return "Error [SQLException][NewOrderService][ItemDAOgetItemServiceInsertar][" + e.getMessage() + "]["+e.getClass()+"]";
        }

    }

    public static String ItemDAOgetItemInsertar(ItemBean itemBean,Connection conn){

        try {
            //Insertar los Items de Ordenes
            return getSEJBOrderNewRemote().ItemDAOgetItemInsertar(itemBean,conn);
        }catch (Exception e) {
            System.out.println("Error [SQLException][NewOrderService][ItemDAOgetItemInsertar][" + e.getMessage() + "]["+e.getClass()+"]");
            return "Error [SQLException][NewOrderService][ItemDAOgetItemInsertar][" + e.getMessage() + "]["+e.getClass()+"]";
        }

    }

    public HashMap ItemDAOgetItemOrder(long nporderid){
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBOrderNewRemote().ItemDAOgetItemOrder(nporderid);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][ItemDAOgetItemOrder][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][ItemDAOgetItemOrder][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][ItemDAOgetItemOrder][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    public static String ItemDAOgetItemOrderDelete(ItemBean itemBean,Connection conn){

        try {
            //Insertar los Items de Ordenes
            return getSEJBOrderNewRemote().ItemDAOgetItemOrderDelete(itemBean,conn);
        }catch (Exception e) {
            System.out.println("Error [SQLException][NewOrderService][ItemDAOgetItemOrderDelete][" + e.getMessage() + "]["+e.getClass()+"]");
            return "Error [SQLException][NewOrderService][ItemDAOgetItemOrderDelete][" + e.getMessage() + "]["+e.getClass()+"]";
        }

    }

    public static String ItemDAOgetItemUpdate(ItemBean itemBean,Connection conn){

        try {
            //Insertar los Items de Ordenes
            return getSEJBOrderNewRemote().ItemDAOgetItemUpdate(itemBean,conn);
        }catch (Exception e) {
            System.out.println("Error [SQLException][NewOrderService][ItemDAOgetItemOrderDelete][" + e.getMessage() + "]["+e.getClass()+"]");
            return "Error [SQLException][NewOrderService][ItemDAOgetItemOrderDelete][" + e.getMessage() + "]["+e.getClass()+"]";
        }

    }

    public static String ItemDAOgetItemInsertDevices(ItemDeviceBean itemDeviceBean,String strNextInbox,Connection conn){

        try {
            //Insertar los Items de Ordenes
            return getSEJBOrderNewRemote().ItemDAOgetItemInsertDevices(itemDeviceBean,strNextInbox,conn);
        }catch (Exception e){
            System.out.println("Error [Exception][NewOrderService][ItemDAOgetItemInsertDevices][" + e.getMessage() + "]["+e.getClass()+"]");
            return "Error [Exception][NewOrderService][ItemDAOgetItemInsertDevices][" + e.getMessage() + "]["+e.getClass()+"]";
        }

    }

    public static ArrayList ItemDAOgetItemDeviceOrder(long intOrderId){
        try {
            return getSEJBOrderNewRemote().ItemDAOgetItemDeviceOrder(intOrderId);
        }catch (SQLException e) {
            System.out.println("Error [SQLException][NewOrderService][ItemDAOgetItemDeviceOrder][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (RemoteException e) {
            System.out.println("Error [RemoteException][NewOrderService][ItemDAOgetItemDeviceOrder][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
    }

    public static ServiciosBean ServiceDAOgetServiceDescription(int intServicioId, String strMessage){
        try {
            return getSEJBOrderNewRemote().ServiceDAOgetServiceDescription(intServicioId,strMessage);
        }catch (SQLException e) {
            System.out.println("Error [SQLException][NewOrderService][ServiceDAOgetServiceDescription][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (RemoteException e) {
            System.out.println("Error [RemoteException][NewOrderService][ServiceDAOgetServiceDescription][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (Exception e) {
            System.out.println("Error [Exception][NewOrderService][ServiceDAOgetServiceDescription][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
    }

    public String ItemDAOgetItemImeiAssignementBADelete(ItemBean itemBean,Connection conn){

        try {
            //Insertar los Items de Ordenes
            return getSEJBOrderNewRemote().ItemDAOgetItemImeiAssignementBADelete(itemBean,conn);
        }catch (SQLException e) {
            System.out.println("Error [SQLException][NewOrderService][ItemDAOgetItemImeiAssignementBADelete][" + e.getMessage() + "]["+e.getClass()+"]");
            return "Error [SQLException][NewOrderService][ItemDAOgetItemImeiAssignementBADelete][" + e.getMessage() + "]["+e.getClass()+"]";
        }catch (RemoteException e) {
            System.out.println("Error [RemoteException][NewOrderService][ItemDAOgetItemImeiAssignementBADelete][" + e.getMessage() + "]["+e.getClass()+"]");
            return "Error [RemoteException][NewOrderService][ItemDAOgetItemImeiAssignementBADelete][" + e.getMessage() + "]["+e.getClass()+"]";
        }catch (Exception e){
            System.out.println("Error [Exception][NewOrderService][ItemDAOgetItemImeiAssignementBADelete][" + e.getMessage() + "]["+e.getClass()+"]");
            return "Error [Exception][NewOrderService][ItemDAOgetItemImeiAssignementBADelete][" + e.getMessage() + "]["+e.getClass()+"]";
        }

    }


    public HashMap doSaveOrder(RequestHashMap objHashMap) throws Exception{
        return getSEJBOrderNewRemote().doSaveOrder(objHashMap);
    }

    /**
     * @author GGUANILO
     * @throws java.lang.Exception
     * @return
     * @param objHashMap
     */
    public HashMap doSaveVep(RequestHashMap objHashMap) throws Exception{
        return getSEJBOrderNewRemote().doSaveVep(objHashMap);
    }

    /**
     * @author RMARTINEZ
     * @throws java.lang.Exception
     * @return
     * @param objHashMap
     */
    public HashMap doDeleteVep(RequestHashMap objHashMap) throws Exception{
        return getSEJBOrderNewRemote().doDeleteVep(objHashMap);
    }

    /**
     * @author RMARTINEZ
     * @throws java.lang.Exception
     * @return
     * @param objHashMap
     */
    public HashMap doValidateExistVep(RequestHashMap objHashMap) throws Exception{
        return getSEJBOrderNewRemote().doValidateExistVep(objHashMap);
    }

    public HashMap SiteDAOgetSiteSolicitedList(long longNpOrderId) throws Exception,SQLException{
        return getSEJBOrderNewRemote().SiteDAOgetSiteSolicitedList(longNpOrderId);
    }

    //CEM se agrego el parametro: strObjectType
    public HashMap SiteDAOgetSiteExistsList(long longNpCustomerId, String strObjectType) throws Exception,SQLException {
        return getSEJBOrderNewRemote().SiteDAOgetSiteExistsList(longNpCustomerId,strObjectType);
    }

    public HashMap SpecificationDAOgetSpecificationUserList(long userId, long lngDivisionId, long lngSpecificationId, String strObjectType,String strFlagGenerator ) {
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBOrderNewRemote().SpecificationDAOgetSpecificationUserList(userId,lngDivisionId,lngSpecificationId,strObjectType,strFlagGenerator);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][SpecificationDAOgetSpecificationUserList][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][SpecificationDAOgetSpecificationUserList][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][SpecificationDAOgetSpecificationUserList][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }


    public HashMap SpecificationDAOgetSpecificationUserList(long customerId, String strLogin, String strGeneratorType, String strOpportunityTypeId, String strFlagGenerator, long lngDivisionId, long lngSpecificationId, long lngGeneratorId) {
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBOrderNewRemote().SpecificationDAOgetSpecificationUserList( customerId,  strLogin,  strGeneratorType,  strOpportunityTypeId,  strFlagGenerator,  lngDivisionId,  lngSpecificationId, lngGeneratorId);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][SpecificationDAOgetSpecificationUserList][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][SpecificationDAOgetSpecificationUserList][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][SpecificationDAOgetSpecificationUserList][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    public HashMap OrderDAOgetProductPriceType(ProductBean objProductBean) {
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBOrderNewRemote().OrderDAOgetProductPriceType(objProductBean);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][OrderDAOgetProductPriceType][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][OrderDAOgetProductPriceType][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][OrderDAOgetProductPriceType][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    public HashMap ProductLineDAOgetProductLineSpecList(long longSolutionId, long longSpecificationId, String strObjectType, long longProductLineId){
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBOrderNewRemote().ProductLineDAOgetProductLineSpecList(longSolutionId,longSpecificationId, strObjectType, longProductLineId);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][ProductLineDAOgetProductLineSpecList][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][ProductLineDAOgetProductLineSpecList][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][ProductLineDAOgetProductLineSpecList][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    public HashMap ProductDAOgetDetailByPhone(String strPhoneNumber,long longCustomerId,long longSiteId,long lSepecificationId){
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBOrderNewRemote().ProductDAOgetDetailByPhone(strPhoneNumber,longCustomerId,longSiteId,lSepecificationId);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][ProductDAOgetDetailByPhone][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][ProductDAOgetDetailByPhone][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][ProductDAOgetDetailByPhone][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    public HashMap ProductDAOgetDetailByPhoneBySpecification(String strPhoneNumber,long longCustomerId,long longSiteId,long lSpecificationId,long lOrderId){
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBOrderNewRemote().ProductDAOgetDetailByPhoneBySpecification(strPhoneNumber,longCustomerId,longSiteId,lSpecificationId,lOrderId);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][ProductDAOgetDetailPhoneBySpecification][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][ProductDAOgetDetailPhoneBySpecification][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][ProductDAOgetDetailPhoneBySpecification][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    public HashMap ProductDAOgetProductDetailImei(String strImei, long longCustomerId,long lSpecificationId){

        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBOrderNewRemote().ProductDAOgetProductDetailImei(strImei,longCustomerId,lSpecificationId);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][ProductDAOgetProductDetailImei][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][ProductDAOgetProductDetailImei][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][ProductDAOgetProductDetailImei][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    //[TDECONV003-1] EFLORES Agrega nuevo parametro
    public HashMap ProductDAOgetDetailByImeiBySpecification(String strImei,long longCustomerId,long lSpecificationId,String strModalitySell,String strFlagMigration){
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBOrderNewRemote().ProductDAOgetDetailByImeiBySpecification(strImei,longCustomerId,lSpecificationId,strModalitySell,strFlagMigration);//[TDECONV003-1]
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][ProductDAOgetDetailByImeiBySpecification][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][ProductDAOgetDetailByImeiBySpecification][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][ProductDAOgetDetailByImeiBySpecification][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    public HashMap getResponsibleAreaList(){

        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBOrderNewRemote().getResponsibleAreaList();
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][ProductDAOgetProductDetailImei][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][getResponsibleAreaList][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getResponsibleAreaList][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    public HashMap getResponsibleDevList(){
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBOrderNewRemote().getResponsibleDevList();
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][getResponsibleDevList][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][getResponsibleDevList][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getResponsibleDevList][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    public HashMap getProductType(ProductBean objProductBean){
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBOrderNewRemote().getProductType(objProductBean);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][getProductType][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][getProductType][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getProductType][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }


    public HashMap getProductBolsa(long lngCustomerId,long lngSiteId){
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBOrderNewRemote().getProductBolsa(lngCustomerId,lngSiteId);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][getProductBolsa][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][getProductBolsa][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getProductBolsa][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    public HashMap getBolsaCreacionN2(long lngCustomerId,long lngSiteId){
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBOrderNewRemote().getBolsaCreacionN2(lngCustomerId,lngSiteId);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][getBolsaCreacionN2][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][getBolsaCreacionN2][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getBolsaCreacionN2][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    public HashMap getProductDetail(long longProductId) {

        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBOrderNewRemote().getProductDetail(longProductId);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][getProductDetail][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][getProductDetail][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getProductDetail][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    public HashMap getSpecificationDate(long lngSpecificationId, String strBillcycle) {

        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBOrderNewRemote().getSpecificationDate(lngSpecificationId,strBillcycle);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][getSpecificationDate][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][getSpecificationDate][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getSpecificationDate][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    public HashMap getFlagEmail(long lngSpecificationId, long lngHdnIUserId) {//jtorresc 09/12/2011
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();
        try {
            return getSEJBOrderNewRemote().getFlagEmail(lngSpecificationId,lngHdnIUserId);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][getFlagEmail][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][getFlagEmail][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getFlagEmail][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }
    public HashMap getTableValue(String strNameTable) {

        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBOrderNewRemote().getTableValue(strNameTable);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][getTableValue][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][getTableValue][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getTableValue][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    public static HashMap ItemDAOhasPaymentOrderId(long nporderid){

        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBOrderNewRemote().ItemDAOhasPaymentOrderId(nporderid);
        }catch (SQLException e) {
            System.out.println("Error [SQLException][NewOrderService][ItemDAOhasPaymentOrderId][" + e.getMessage() + "]["+e.getClass()+"]");
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
        catch (RemoteException e) {
            System.out.println("Error [RemoteException][NewOrderService][ItemDAOhasPaymentOrderId][" + e.getMessage() + "]["+e.getClass()+"]");
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
        catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][ItemDAOhasPaymentOrderId][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    public HashMap getServiceRentList(long lngPlanId){

        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBOrderNewRemote().getServiceRentList(lngPlanId);
        }catch (SQLException e) {
            System.out.println("[SQLException][NewOrderService][getServiceRentList][" + e.getMessage() + "]["+e.getClass()+"]");
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            System.out.println("[RemoteException][NewOrderService][getServiceRentList][" + e.getMessage() + "]["+e.getClass()+"]");
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getServiceRentList][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }


    public HashMap getModelList() throws Exception,SQLException{
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBOrderNewRemote().getModelList();
        }catch (SQLException e) {
            System.out.println("[SQLException][NewOrderService][getModelList][" + e.getMessage() + "]["+e.getClass()+"]");
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            System.out.println("[RemoteException][NewOrderService][getModelList][" + e.getMessage() + "]["+e.getClass()+"]");
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getModelList][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    public HashMap getModelListByCategory(int specId) throws Exception,SQLException{
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBOrderNewRemote().getModelListByCategory(specId);
        }catch (SQLException e) {
            System.out.println("[SQLException][NewOrderService][getModelListByCategory][" + e.getMessage() + "]["+e.getClass()+"]");
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            System.out.println("[RemoteException][NewOrderService][getModelListByCategory][" + e.getMessage() + "]["+e.getClass()+"]");
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getModelListByCategory][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }


    public HashMap getProductLineDetail(long lngProductLineId){
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBOrderNewRemote().getProductLineDetail(lngProductLineId);
        }catch (SQLException e) {
            System.out.println("[SQLException][NewOrderService][getProductLineDetail][" + e.getMessage() + "]["+e.getClass()+"]");
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            System.out.println("[RemoteException][NewOrderService][getProductLineDetail][" + e.getMessage() + "]["+e.getClass()+"]");
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getProductLineDetail][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    /*JPEREZ: Validación de stock - INICIO*/
    public HashMap getValidateStock(int idSpecification, int iDispatchPlace){
        HashMap objHashMap = new HashMap();
        try{
            return getSEJBOrderNewRemote().getValidateStock(idSpecification, iDispatchPlace);
        }catch (SQLException e) {
            System.out.println("[SQLException][NewOrderService][getValidateStock][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap;
        }catch (RemoteException e) {
            System.out.println("[RemoteException][NewOrderService][getValidateStock][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap;
        }catch (Exception e) {
            System.out.println("[Exception][NewOrderService][getValidateStock][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap;
        }
    }

    public HashMap getStockMessage(int idSpecification, long lProductId, int iBuildingid, String strSaleModality, long lSalesStructOrigenId, String strTipo){
        HashMap objHashMap = new HashMap();
        try{
            return getSEJBOrderNewRemote().getStockMessage(idSpecification,lProductId,iBuildingid,strSaleModality,lSalesStructOrigenId,strTipo);
        }catch (SQLException e) {
            System.out.println("[SQLException][NewOrderService][getStockMessage][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap;
        }catch (RemoteException e) {
            System.out.println("[RemoteException][NewOrderService][getStockMessage][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap;
        }catch (Exception e) {
            System.out.println("[Exception][NewOrderService][getStockMessage][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap;
        }
    }    
    /*JPEREZ: Validación de stock - FIN*/



    public long getUnkownSiteIdByOportunity(long lngOportunityId){

        try {
            return getSEJBOrderNewRemote().getUnkownSiteIdByOportunity(lngOportunityId);
        }catch (SQLException e) {
            System.out.println("[SQLException][NewOrderService][lngOportunityId][" + e.getMessage() + "]["+e.getClass()+"]");
            return 0;
        }catch (RemoteException e) {
            System.out.println("[SQLException][RemoteException][lngOportunityId][" + e.getMessage() + "]["+e.getClass()+"]");
            return 0;
        }catch (Exception e) {
            System.out.println("[SQLException][Exception][lngOportunityId][" + e.getMessage() + "]["+e.getClass()+"]");
            return 0;
        }
    }


    public HashMap getCustSiteIdByOportunity(long lngOportunityId){
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();
        try{
            return getSEJBOrderNewRemote().getCustSiteIdByOportunity(lngOportunityId);
        }catch (SQLException e) {
            System.out.println("[SQLException][NewOrderService][getCustSiteIdByOportunity][" + e.getMessage() + "]["+e.getClass()+"]");
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            System.out.println("[RemoteException][NewOrderService][getCustSiteIdByOportunity][" + e.getMessage() + "]["+e.getClass()+"]");
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getCustSiteIdByOportunity][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    /*Odubock: Validación de fechaFirma - ini*/
    public HashMap getValidateFechaFirma(long lngOrderId, String strFechaFirma){
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();
        try{
            return getSEJBOrderNewRemote().getValidateFechaFirma(lngOrderId,strFechaFirma);
        }catch (SQLException e) {
            System.out.println("[SQLException][NewOrderService][getValidateFechaFirma][" + e.getMessage() + "]["+e.getClass()+"]");
            System.out.println(strMessage);
            objHashMap.put("wv_message",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            System.out.println("[RemoteException][NewOrderService][getValidateFechaFirma][" + e.getMessage() + "]["+e.getClass()+"]");
            System.out.println(strMessage);
            objHashMap.put("wv_message",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getValidateFechaFirma][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("wv_message",strMessage);
            return objHashMap;
        }
    }
    /*Odubock: Validación de fechaFirma - fin*/


    public String getValidateAdministrator(long lOrderId){
        String strMessage = null;
        try {
            return getSEJBOrderNewRemote().getValidateAdministrator(lOrderId);
        }catch (SQLException e) {
            System.out.println("[SQLException][NewOrderService][getValidateAdministrator][" + e.getMessage() + "]["+e.getClass()+"]");
            return strMessage;
        }catch (RemoteException e) {
            System.out.println("[RemoteException][NewOrderService][getValidateAdministrator][" + e.getMessage() + "]["+e.getClass()+"]");
            return strMessage;
        }catch (Exception e) {
            System.out.println("[Exception][NewOrderService][getValidateAdministrator][" + e.getMessage() + "]["+e.getClass()+"]");
            return strMessage;
        }
    }

    public HashMap getNoteCount(long lOrderId) throws Exception,SQLException{
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();
        try{
            return objHashMap = getSEJBOrderNewRemote().getNoteCount(lOrderId);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][getNoteCount][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][getNoteCount][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getNoteCount][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    /*
     * JOYOLAR 01/08/2008
     * */
    public HashMap getAllowedSpecification(long lspecificationId, long lcustomerid) throws Exception,SQLException{
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try{
            return objHashMap = getSEJBOrderNewRemote().getAllowedSpecification(lspecificationId, lcustomerid);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][getAllowedSpecification][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][getAllowedSpecification][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getAllowedSpecification][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }
    /*
     * RPOLO 13/01/2009 verifica si el servicio tiene comision
     * */
    public HashMap getComissionMessage(int intServicioId){
        HashMap objHashMap = new HashMap();
        try{
            return getSEJBOrderNewRemote().getComissionMessage(intServicioId);
        }catch (SQLException e) {
            System.out.println("[SQLException][NewOrderService][getComissionMessage][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap;
        }catch (RemoteException e) {
            System.out.println("[RemoteException][NewOrderService][getComissionMessage][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap;
        }catch (Exception e) {
            System.out.println("[Exception][NewOrderService][getComissionMessage][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap;
        }
    }


    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
     * <br>Motivo: Obtiene el listado de las Soluciones que tienen asignadas una determinada categoría al crear la orden.
     * <br>Fecha: 22/04/2009
     * @see pe.com.nextel.dao.SpecificationDAO#getSolutionSpecificationList(long,long)
     ************************************************************************************************************************************/
    public HashMap getSolutionSpecificationList(long lspecificationId, long ldivisionId, long lsiteId) throws Exception,SQLException{
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();
        try {
            return getSEJBOrderNewRemote().getSolutionSpecificationList(lspecificationId,ldivisionId,lsiteId);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][getSolutionSpecificationList][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        } catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][getSolutionSpecificationList][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        } catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getSolutionSpecificationList][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }



    }


    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
     * <br>Motivo: Obtiene el tipo de  Solución del item
     * <br>Fecha: 22/04/2009
     * @see pe.com.nextel.dao.SpecificationDAO#getSolutionSpecificationList(long,long)
     ************************************************************************************************************************************/
    public HashMap getSolutionType() {
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();
        try {
            return getSEJBOrderNewRemote().getSolutionType();
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][getSolutionType][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        } catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][getSolutionType][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        } catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getSolutionType][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }



    }

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:jose.casas@nextel.com.pe">José Casas</a>
     * <br>Motivo: Obtiene el listado de las Soluciones que tienen asignadas una determinada categoría al crear la orden.
     * <br>Fecha: 16/06/2009
     * @see pe.com.nextel.service.NeworderService#getFistStatus(RequestHashMap)
     ************************************************************************************************************************************/
    public HashMap getFistStatus(RequestHashMap objHashMap) throws Exception, SQLException{
        String  strMessage = new String();
        try {
            return getSEJBOrderNewRemote().getFistStatus(objHashMap);
        } catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][getFistStatus][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        } catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][getFistStatus][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        } catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getFistStatus][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:rensso.martinez@nextel.com.pe">Rensso Martinezr</a>
     * <br>Motivo: Valida los que un contrato no este suspendido mas de 60 días.
     * <br>Fecha: 24/06/2009
     * @see pe.com.nextel.dao.ProductDAO#validaDiasSuspension(String, String, String)
     ************************************************************************************************************************************/
    public HashMap ProductDAOvalidaDiasSuspension(String strPhoneNumber, long lSpecificationId, String strNpScheduleDate, String strNpScheduleDate2){
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBOrderNewRemote().ProductDAOvalidaDiasSuspension(strPhoneNumber, lSpecificationId, strNpScheduleDate,strNpScheduleDate2);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][ProductDAOvalidaDiasSuspension][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][ProductDAOvalidaDiasSuspension][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][ProductDAOvalidaDiasSuspension][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }


    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:rensso.martinez@nextel.com.pe">Rensso Martinez</a>
     * <br>Motivo: Obtiene el listado de los Estados del Item para las Suspensiones definitivas.
     * <br>Fecha: 28/06/2009
     * @see pe.com.nextel.dao.ItemDAO#getStatusItemList(String, String, String)
     ************************************************************************************************************************************/
    public HashMap getStatusItemList(String nameTable, String nptag1, String nptag2) throws Exception,SQLException{
        try {
            return getSEJBOrderNewRemote().ItemDAOgetStatusItemList(nameTable, nptag1, nptag2);
        }catch (SQLException e) {
            System.out.println("Error [SQLException][NewOrderService][getStatusItemList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (RemoteException e) {
            System.out.println("Error [RemoteException][NewOrderService][getStatusItemList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (Exception e) {
            System.out.println("Error [SQLException][NewOrderService][getStatusItemList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
    }

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
     * <br>Motivo: Obtiene el listado de servicios por defecto en función a un producto
     * <br>Fecha: 22/04/2009
     * @see pe.com.nextel.dao.SpecificationDAO#getSolutionSpecificationList(long,int,int,int,int)
     ************************************************************************************************************************************/
    public HashMap getProductServiceDefaultList (long lspecificationid, int iProductId,int iplanId,int iPermission_alq, int iPermission_msj) throws Exception,SQLException{
        HashMap objHashMap = new HashMap();
        try {
            return getSEJBOrderNewRemote().getProductServiceDefaultList(lspecificationid,iProductId,iplanId,iPermission_alq,iPermission_msj);
        }catch (SQLException e) {
            System.out.println("Error [SQLException][NewOrderService][getProductServiceDefaultList][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap;
        }
        catch (RemoteException e) {
            System.out.println("Error [RemoteException][NewOrderService][getProductServiceDefaultList][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap;
        }
        catch (Exception e) {
            System.out.println("Error [SQLException][NewOrderService][getProductServiceDefaultList][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap;
        }
    }
    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:cesar.barzola@nextel.com.pe">Cesar Barzola</a>
     * <br>Motivo: Obtiene las soluciones  para un determinado submarket
     * <br>Fecha: 22/04/2009
     / ************************************************************************************************************************************/
    public static HashMap getOtherSolutionsbySubMarket(long lspecificationid,long lsolutionid)
    {
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();
        try{
            return getSEJBOrderNewRemote().getOthersSolutionsbySubMarket(lspecificationid,lsolutionid);
        }
        catch (SQLException e) {
            strMessage="Error [NewOrderService][getOtherSolutionsbySubMarket][" + e.getMessage() + "]["+e.getClass()+"]";
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage="Error [NewOrderService][getOtherSolutionsbySubMarket][" + e.getMessage() + "]["+e.getClass()+"]";
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
        catch (Exception e) {
            strMessage="Error [NewOrderService][getOtherSolutionsbySubMarket][" + e.getMessage() + "]["+e.getClass()+"]";
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }
    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:cesar.barzola@nextel.com.pe">Cesar Barzola</a>
     * <br>Motivo: Obtiene el nombre de una determinada solucion
     * <br>Fecha: 20/07/2009
     / ************************************************************************************************************************************/
    public static HashMap OrdergetSolutionName(long lidSolution)
    {
        String strNameSolution="";
        String strMessage="";
        HashMap objHashMap = new HashMap();
        try{
            ArrayList objArraySolutionList=(ArrayList)OrderDAOgetSolutionList("");
            for(int i=0;i<objArraySolutionList.size();i++)
            {
                Hashtable hsolution = new Hashtable();
                hsolution=(Hashtable)objArraySolutionList.get(i);
                String solutionid=(String)hsolution.get("wn_npsolutionid");
                if(MiUtil.parseLong(solutionid) == lidSolution)
                {
                    strNameSolution=(String)hsolution.get("wv_npname");
                    break;
                }
            }
            objHashMap.put("strNameSolution",strNameSolution);
        }
        catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][OrdergetSolutionName][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }

        return objHashMap ;
    }

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:reinhard.arana@nextel.com.pe">Reinhard Arana</a>
     * <br>Motivo: Obtiene el el producto bolsa segun customerid, siteid y solutionid
     * <br>Fecha: 21/07/2009
     /************************************************************************************************************************************/
    public HashMap getProductBolsa(long lngCustomerId,long lngSiteId, long lngSolutionId){
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBOrderNewRemote().getProductBolsa(lngCustomerId,lngSiteId,lngSolutionId);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][getProductBolsa][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][getProductBolsa][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getProductBolsa][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }
    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:cesar.barzola@nextel.com.pe">Cesar Barzola</a>
     * <br>Motivo: Obtiene una lista de propuestas
     * <br>Fecha: 27/07/2009
     / ************************************************************************************************************************************/
    public  HashMap  getProposedList(long lCustomerId,long lSite,long lSpecificationId,long lSellerId)
    {
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();
        try{
            return getSEJBOrderNewRemote().getProposedList(lCustomerId,lSite,lSpecificationId,lSellerId);
        }
        catch (SQLException e) {
            strMessage="Error [NewOrderService][getProposedList][" + e.getMessage() + "]["+e.getClass()+"]";
            objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage="Error [NewOrderService][getProposedList][" + e.getMessage() + "]["+e.getClass()+"]";
            objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
            return objHashMap;
        }
        catch (Exception e) {
            strMessage="Error [NewOrderService][getProposedList][" + e.getMessage() + "]["+e.getClass()+"]";
            objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
            return objHashMap;
        }
    }
    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:cesar.barzola@nextel.com.pe">Cesar Barzola</a>
     * <br>Motivo: Valida la Propuesta
     * <br>Fecha: 27/07/2009
     / ************************************************************************************************************************************/
    public  HashMap  getValidationProposed(long lOrderId,long lProposedId,long lCustomerId,long lSpecification,long lSellerId,String strTrama)
    {
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();
        try{
            return getSEJBOrderNewRemote().getValidationProposed(lOrderId,lProposedId,lCustomerId,lSpecification,lSellerId,strTrama);
        }
        catch (SQLException e) {
            strMessage="Error [NewOrderService][getValidationProposed][" + e.getMessage() + "]["+e.getClass()+"]";
            objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage="Error [NewOrderService][getValidationProposed][" + e.getMessage() + "]["+e.getClass()+"]";
            objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
            return objHashMap;
        }
        catch (Exception e) {
            strMessage="Error [NewOrderService][getValidationProposed][" + e.getMessage() + "]["+e.getClass()+"]";
            objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
            return objHashMap;
        }
    }

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:cesar.barzola@nextel.com.pe">Cesar Barzola</a>
     * <br>Motivo: Obtiene los servicios core de un determinado plan
     * <br>Fecha: 12/07/2009
     * @see pe.com.nextel.dao.ServiceDAO#getCoreService_by_Plan(long)
     ************************************************************************************************************************************/
    public  HashMap ServiceDAOgetCoreServicebyPlan(long lPlanId){
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();
        try {
            return getSEJBOrderNewRemote().ServiceDAOgetCoreServicebyPlan(lPlanId);
        }catch (SQLException e) {
            strMessage  = "[RemoteException][NewOrderService][ServiceDAOgetCoreServicebyPlan][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
        catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][ServiceDAOgetCoreServicebyPlan][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
        catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][ServiceDAOgetCoreServicebyPlan][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:percy.hidalgo@nextel.com.pe">Percy Hidalgo</a>
     * <br>Motivo: Obtiene el plan original
     * <br>Fecha: 12/07/2009
     ************************************************************************************************************************************/
    public HashMap getOriginalPlan(PlanTarifarioBean planTarifarioBean){
        HashMap hshDataMap = new HashMap();
        String  strMessage = new String();
        try {
            hshDataMap = getSEJBOrderNewRemote().getOriginalPlan(planTarifarioBean);
        }catch (SQLException e) {
            strMessage  = "[RemoteException][NewOrderService][getOriginalPlan][" + e.getClass() + " " + e.getMessage()+"]";
            hshDataMap.put("strMessage",strMessage);
        }
        catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][getOriginalPlan][" + e.getClass() + " " + e.getMessage()+"]";
            hshDataMap.put("strMessage",strMessage);
        }
        catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getOriginalPlan][" + e.getClass() + " " + e.getMessage()+"]";
            hshDataMap.put("strMessage",strMessage);
        }
        return hshDataMap;
    }

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
     * <br>Motivo: Obtiene la lista de Modelos a partir de una linea de Producto
     * <br>Fecha: 28/09/2010
     ************************************************************************************************************************************/
    public HashMap getProductModelList(ProductBean objProductBean){
        HashMap objHashMap = new HashMap();
        String  strMessage = "";//EZM 23/12/10 Compatibilidad Modelo-Plan-Servicio (FindBugs Correction)
        try {
            return getSEJBOrderNewRemote().getProductModelList(objProductBean);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][ProductDAOgetProductModelList][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][ProductDAOgetProductModelList][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][ProductDAOgetProductModelList][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }
    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
     * <br>Motivo: Obtiene la lista de Planes a partir de una Producto
     * <br>Fecha: 08/10/2010
     ************************************************************************************************************************************/
    public HashMap getProductPlanList(ProductBean objProductBean){
        HashMap objHashMap = new HashMap();
        String  strMessage = ""; //EZM 23/12/10 Compatibilidad Modelo-Plan-Servicio (FindBugs Correction)
        try {
            return getSEJBOrderNewRemote().getProductPlanList(objProductBean);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][getProductPlanList][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][getProductPlanList][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getProductPlanList][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }


    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
     * <br>Motivo: Obtiene la lista de Planes a partir de una Producto
     * <br>Fecha: 08/10/2010
     ************************************************************************************************************************************/
    public HashMap getValidServSelectedList(String strServices, String strServicesDesc, String strPlanId, String strProduct){//EZUBIAURR 28/02/11
        HashMap objHashMap = new HashMap();
        try{
            return getSEJBOrderNewRemote().getValidateServSelectedList(strServices, strServicesDesc, strPlanId, strProduct);//EZUBIAURR 28/02/11
            //return objHashMap;
        }catch (SQLException e) {
            System.out.println("[SQLException][NewOrderService][getStockMessage][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap;
        }catch (RemoteException e) {
            System.out.println("[RemoteException][NewOrderService][getStockMessage][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap;
        }catch (Exception e) {
            System.out.println("[Exception][NewOrderService][getStockMessage][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap;
        }
    }

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:ronald.huacani@asistp.com">Ronald Huacani</a>
     * <br>Motivo: Obtiene la lista de recursos para numeros dorados por PTN y UFMI
     * <br>Fecha: 15/10/2010
     ************************************************************************************************************************************/
    public static HashMap getNumberGolden(String sCodApp, long lDnType,long lNpcode,String sDnNum,long lTmCode, String sExcluded, String sQuantity, String sPortabilidad){
        HashMap shMap = new HashMap();
        try {
            shMap = (HashMap)getSEJBOrderNewRemote().getNumberGolden(sCodApp, lDnType, lNpcode, sDnNum, lTmCode, sExcluded, sQuantity, sPortabilidad);
            return shMap;
        }catch (SQLException e) {
            shMap.put("strMessage",e.getMessage());
            return shMap;
        }catch (RemoteException e) {
            shMap.put("strMessage",e.getMessage());
            return shMap;
        }catch (Exception e) {
            shMap.put("strMessage",e.getMessage());
            return shMap;
        }
    }

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
     * <br>Motivo: Valida si algún item no retiro del Blacklist al IMEI
     * <br>Fecha: 02/11/2010
     * @see pe.com.nextel.dao.OrderDAO#getValidateBlacklist(long)
     ************************************************************************************************************************************/
    public HashMap getValidateBlacklist (long lOrderId) throws Exception,SQLException{
        HashMap objHashMap = new HashMap();
        try{
            return getSEJBOrderNewRemote().getValidateBlacklist(lOrderId);

        }catch (SQLException e) {
            System.out.println("Error [SQLException][NewOrderService][getValidateBlacklist][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap;
        }
        catch (RemoteException e) {
            System.out.println("Error [RemoteException][NewOrderService][getValidateBlacklist][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap;
        }
        catch (Exception e) {
            System.out.println("Error [SQLException][NewOrderService][getValidateBlacklist][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap;
        }
    }

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
     * <br>Motivo: Obtiene los servicios adicionales por defecto de acuerdo al PlanId ingresado
     * <br>Fecha: 23/11/2011
     * @see pe.com.nextel.dao.ServiceDAO#getServiceDefaultListByPlan(long)
     ************************************************************************************************************************************/
    public HashMap getServiceDefaultListByPlan (long lPlanId) throws Exception,SQLException{
        HashMap objHashMap = new HashMap();
        try{
            return getSEJBOrderNewRemote().getServiceDefaultListByPlan(lPlanId);

        }catch (SQLException e) {
            System.out.println("Error [SQLException][NewOrderService][getServiceDefaultListByPlan][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap;
        }
        catch (RemoteException e) {
            System.out.println("Error [RemoteException][NewOrderService][getServiceDefaultListByPlan][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap;
        }
        catch (Exception e) {
            System.out.println("Error [SQLException][NewOrderService][getServiceDefaultListByPlan][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap;
        }
    }

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
     * <br>Motivo: Obtiene el tipo de Plan
     * <br>Fecha: 05/12/2011
     * @see pe.com.nextel.dao.PlanDAO#getTypePlan(long)
     ************************************************************************************************************************************/
    public String getTypePlan (long lPlanId) throws Exception,SQLException{
        try{
            return getSEJBOrderNewRemote().getTypePlan(lPlanId);

        }catch (SQLException e) {
            System.out.println("Error [SQLException][NewOrderService][getTypePlan][" + e.getMessage() + "]["+e.getClass()+"]");
            return e.getMessage();
        }
        catch (RemoteException e) {
            System.out.println("Error [RemoteException][NewOrderService][getTypePlan][" + e.getMessage() + "]["+e.getClass()+"]");
            return e.getMessage();
        }
        catch (Exception e) {
            System.out.println("Error [SQLException][NewOrderService][getTypePlan][" + e.getMessage() + "]["+e.getClass()+"]");
            return e.getMessage();
        }
    }

    /**
     * <br>Realizado por: <a href="mailto:rennso.martinez@hp.com">Rensso Martìnez</a>
     * <br>Fecha: 22/04/2013
     */
    public HashMap showCourierSpecificationsId() {
        GeneralService generalService = new GeneralService();
        HashMap hashMap = generalService.getDominioList("DESPACHO_COURIER");
        return hashMap;
    }

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
     * <br>Motivo: Validar si el plan al que desea cambiarse un empleado es Comercial o no.
     * <br>Fecha: 20/11/2013
     ************************************************************************************************************************************/
    public HashMap doValidateChangePlanToEmployee(RequestHashMap objHashMap){
        HashMap objResultHashMap = new HashMap();
        try {
            return getSEJBOrderNewRemote().doValidateChangePlanToEmployee(objHashMap);
        } catch (SQLException e) {
            System.out.println("[SQLException][NewOrderService][doValidateChangePlanToEmployee][" + e.getMessage() + "]["+e.getClass()+"]");
            objResultHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objResultHashMap;
        } catch (RemoteException e) {
            System.out.println("[RemoteException][NewOrderService][doValidateChangePlanToEmployee][" + e.getMessage() + "]["+e.getClass()+"]");
            objResultHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objResultHashMap;
        } catch (Exception e) {
            System.out.println("[Exception][NewOrderService][doValidateChangePlanToEmployee][" + e.getMessage() + "]["+e.getClass()+"]");
            objResultHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objResultHashMap;
        }
    }

    /**
     * <br>Realizado por: <a href="mailto:orlando.cruces@hp.com">Orlando Cruces</a>
     * <br>Fecha: 25/03/2015
     */
    public HashMap showTypeOpeSpecificationsId(String strCustomerId) {
        GeneralService generalService = new GeneralService();
        HashMap hashMap = generalService.getTypeOpeSpecifications(strCustomerId);
        return hashMap;
    }

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:lhuapaya@practiaconsulting.com">Luis Huapaya</a>
     * <br>Motivo: Obtiene el producto de tipo bolsa de celulares para una linea de producto y del site.
     * <br>Fecha: 30/07/2015
     ************************************************************************************************************************************/

    public HashMap getBolsaCelulares(long siteId,long customerId,long productLine){
        System.out.println("entre al metodo del servicio getBolsaCelulares");
        HashMap objHashMap = new HashMap();
        String strMessage = null;
        try {
            System.out.println("entre al try");
            return getSEJBOrderNewRemote().getBolsaCelulares(siteId,customerId,productLine);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][getBolsaCelulares][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][getBolsaCelulares][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getBolsaCelulares][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }
    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:lhuapaya@practiaconsulting.com">Luis Huapaya</a>
     * <br>Motivo: Obtiene todos los producto de tipo bolsa de celulares para un site.
     * <br>Fecha: 30/07/2015
     ************************************************************************************************************************************/

    public HashMap getAllProductBCL(long siteId,long customerId){
        System.out.println("entre al metodo del servicio getAllProductBCL");
        HashMap objHashMap = new HashMap();
        String strMessage = null;
        try {
            return getSEJBOrderNewRemote().getAllProductBCL(siteId,customerId);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][getBolsaCelulares][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][getBolsaCelulares][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getBolsaCelulares][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:lhuapaya@practiaconsulting.com">Luis Huapaya</a>
     * <br>Motivo: Obtiene todos los producto de tipo bolsa de celulares para un site.
     * <br>Fecha: 30/07/2015
     ************************************************************************************************************************************/

    public String doValidatePostVentaBolCel(long orderId){
        String strMessage = null;
        try {
            System.out.println("entre al try");
            return getSEJBOrderNewRemote().doValidatePostVentaBolCel(orderId);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][doValidatePostVentaBolCel][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            return strMessage;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][doValidatePostVentaBolCel][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            return strMessage;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][doValidatePostVentaBolCel][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            return strMessage;
        }
    }
    
    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:jhonny.quispe@tcs.com">Jhonny Quispe</a>
     * <br>Fecha: 21/02/2017
     ************************************************************************************************************************************/
    public HashMap validarRentaAdelantada(String hdnSpecification, long lngCustomerId, String tipoDocumento, String numeroDocumento, HashMap objOrdenRA ){
    	String strMessage = null;
    	boolean resultado = false;
    	HashMap objHashMap = new HashMap();
        try {            
        	resultado =  getSEJBOrderNewRemote().validarRentaAdelantada(hdnSpecification, lngCustomerId, tipoDocumento, numeroDocumento, objOrdenRA);
        	objHashMap.put("resultado", resultado);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][validarRentaAdelantada][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);            
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][validarRentaAdelantada][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);            
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][validarRentaAdelantada][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);            
        }
        
        objHashMap.put("strMessage", strMessage);
        return objHashMap;
    }
    
    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:jhonny.quispe@tcs.com">Jhonny Quispe</a>
     * <br>Fecha: 07/04/2017
     ************************************************************************************************************************************/
    public HashMap getOrdenRentaAdelantada(long orderId){
    	HashMap objHashMap = new HashMap();
        String strMessage = null;
        
        try {
            return getSEJBOrderNewRemote().getOrdenRentaAdelantada(orderId);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][getOrdenRentaAdelantada][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][getOrdenRentaAdelantada][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getOrdenRentaAdelantada][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:jorge.curi@tcs.com">JCURI</a>
     * <br>Fecha: 11/07/2017
	 * <br>Modificado por: JBALCAZAR PRY-1002</a>
     * <br>Fecha: 22/01/2018
     ************************************************************************************************************************************/
    public boolean getObtenerFlagActivoProrrateo(String hdnSpecification, String tipoDocumento, String numeroDocumento,String strGeneratorType, String strUser){
    	HashMap objHashMap = new HashMap();
        String strMessage = null;
        
        try {
            return getSEJBOrderNewRemote().obtenerFlagActivoProrrateo(hdnSpecification, tipoDocumento , numeroDocumento,strGeneratorType,strUser);
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getObtenerFlagActivoProrrateo][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            return false;
        }
    } 
 
    /**
     * @author AMENDEZ
     * @project PRY-0864
     * Metodo   Evalua mostrar campo precio excepcion en popupitem
     *          flag vep activo y cliente es ruc20(Juridico): 1 --> Se muestra campo de precio excepcion
     *          flag vep inactivo y cliente no es ruc20(Natural): 0 --> No se muestra campo de precio excepcion
     *          errores generales -1
     * @return
     */
    public int evaluateExceptionPriceVep(int npvep,long sw_customerid){
        logger.info("******************* INICIO evaluateExceptionPriceVep  *******************");
        logger.info("npvep: "+npvep);
        logger.info("sw_customerid: "+sw_customerid);
        int evaluate = 0;
        String strMessage=null;
        try {
            evaluate = getSEJBOrderNewRemote().evaluateExceptionPriceVep(npvep,sw_customerid);
        }catch (SQLException e) {
            evaluate=-1;
            strMessage  = "[SQLException][NewOrderService][evaluateExceptionPriceVep]";
            logger.error(strMessage,e);
        }catch (RemoteException e) {
            evaluate=-1;
            strMessage  = "[RemoteException][NewOrderService][evaluateExceptionPriceVep]";
            logger.error(strMessage,e);
        }catch (Exception e) {
            evaluate=-1;
            strMessage  = "[Exception][NewOrderService][evaluateExceptionPriceVep]";
            logger.error(strMessage,e);
        }
        logger.info("evaluate: "+evaluate);
        logger.info("******************* FIN evaluateExceptionPriceVep  *******************");
        return evaluate;
    }

    /**
     * @author AMENDEZ
     * @project PRY-0864
     * Metodo   Evalua el monto inicial en casos de ordenes vep
     * @return
     */
    public String validateOrderVepCI(long nporderid,int npvepquantityquota,double npinitialquota,int npspecificationid,long swcustomerid,double totalsalesprice,int npvep,String nptype,int nppaymenttermsiq){
        logger.info("******************* INICIO validateOrderVepCI  *******************");
        //Se adciona paramtro de forma de pago de cuota inicial
        logger.info("nporderid            : "+nporderid);
        logger.info("npvepquantityquota   : "+npvepquantityquota);
        logger.info("npinitialquota       : "+npinitialquota);
        logger.info("npspecificationid    : "+npspecificationid);
        logger.info("swcustomerid         : "+swcustomerid);
        logger.info("totalsalesprice      : "+totalsalesprice);
        logger.info("npvep                : "+npvep);
        logger.info("nptype               : "+nptype);
        logger.info("nppaymenttermsiq     : "+nppaymenttermsiq);
        String strMessage=null;
        try {
            strMessage= getSEJBOrderNewRemote().validateOrderVepCI(nporderid,npvepquantityquota,npinitialquota,npspecificationid,swcustomerid,totalsalesprice,npvep,nptype,nppaymenttermsiq);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][validateOrderVepCI]";
            logger.error(strMessage,e);
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][validateOrderVepCI]";
            logger.error(strMessage,e);
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][validateOrderVepCI]";
            logger.error(strMessage,e);
        }
        logger.info("******************* FIN validateOrderVepCI  *******************");
        return strMessage;
    }

    /**
     * @author AMENDEZ
     * @project PRY-0864
     * Metodo   Evalua el monto inicial en casos de ordenes vep
     * @return
     */
    public int validatePaymentTermsCI(long swcustomerid,long userid,int npvep){
        logger.info("******************* INICIO NewOrderService > validatePaymentTermsCI  *******************");
        logger.info("swcustomerid  : "+swcustomerid);
        logger.info("userid        : "+userid);
        logger.info("npvep         : "+npvep);
        int result=0;
        try {
            result= getSEJBOrderNewRemote().validatePaymentTermsCI(swcustomerid,userid,npvep);
        }catch (SQLException e) {
            result  = 0;
            logger.error(e);
        }catch (RemoteException e) {
            result  = 0;
            logger.error(e);
        }catch (Exception e) {
            result  = 0;
            logger.error(e);
        }
        logger.info("******************* FIN NewOrderService > validatePaymentTermsCI  *******************");
        return result;
    }
     /**
     * @author CMONETEROS
     * @project PRY-1062
     * Metodo   Valida que el numero de telefono no esté asociado a una cuota VEP
     * @return
     */
    public HashMap doValidateVEPItem(long customerId, String strPhoneNumber)
    {
        HashMap objResultHashMap = new HashMap();
        try
        {
            return getSEJBOrderNewRemote().doValidateVEPItem(customerId, strPhoneNumber);
        }
        catch (SQLException e)
        {
            System.out.println("[SQLException][NewOrderService][doValidateVEPItem][" + e.getMessage() + "][" + e.getClass() + "]");
            objResultHashMap.put("strMessage", e.getClass() + " " + e.getMessage());
            return objResultHashMap;
        }
        catch (RemoteException e)
        {
            System.out.println("[RemoteException][NewOrderService][doValidateVEPItem][" + e.getMessage() + "][" + e.getClass() + "]");
            objResultHashMap.put("strMessage", e.getClass() + " " + e.getMessage());
            return objResultHashMap;
        }
        catch (Exception e)
        {
            System.out.println("[Exception][NewOrderService][doValidateVEPItem][" + e.getMessage() + "][" + e.getClass() + "]");
            objResultHashMap.put("strMessage", e.getClass() + " " + e.getMessage());
        }
        return objResultHashMap;
    }

    /**
     * @author CMONETEROS
     * @project PRY-1062
     * Metodo   Valida la preevaluacion para Cambio de Modelo.
     * @return
     */
    public HashMap doValidatePreevaluationCDM(long customerId)
    {
        HashMap objResultHashMap = new HashMap();
        try
        {
            return getSEJBOrderNewRemote().doValidatePreevaluationCDM(customerId);
        }
        catch (SQLException e)
        {
            logger.info("[SQLException][NewOrderService][doValidatePreevaluationCDM][" + e.getMessage() + "][" + e.getClass() + "]");
            objResultHashMap.put("strMessage", e.getClass() + " " + e.getMessage());
            return objResultHashMap;
        }
        catch (RemoteException e)
        {
            logger.info("[RemoteException][NewOrderService][doValidatePreevaluationCDM][" + e.getMessage() + "][" + e.getClass() + "]");
            objResultHashMap.put("strMessage", e.getClass() + " " + e.getMessage());
            return objResultHashMap;
        }
        catch (Exception e)
        {
            logger.info("[Exception][NewOrderService][doValidatePreevaluationCDM][" + e.getMessage() + "][" + e.getClass() + "]");
            objResultHashMap.put("strMessage", e.getClass() + " " + e.getMessage());
        }
        return objResultHashMap;
    }

    /**
     * Purpose: Valida que exista una configuracion para bafi 2300 segun modalidad, solucion y linea producto
     * Developer       Fecha       Comentario
     * =============   ==========  ======================================================================
     * AMENDEZ         22/03/2018  [EST-1098]Creacion
     */
    public int validateConfigBafi2300(String av_npmodality,String av_npsolutionid,String av_npproductlineid){
        logger.info("******************* INICIO NewOrderService > validateConfigBafi2300  *******************");
        logger.info("av_npmodality          : "+av_npmodality);
        logger.info("av_npsolutionid        : "+av_npsolutionid);
        logger.info("av_npproductlineid     : "+av_npproductlineid);
        int result=0;
        try {
            result= getSEJBOrderNewRemote().validateConfigBafi2300(av_npmodality,av_npsolutionid,av_npproductlineid);
        }catch (SQLException e) {
            result  = 0;
            logger.error(e);
        }catch (RemoteException e) {
            result  = 0;
            logger.error(e);
        }catch (Exception e) {
            result  = 0;
            logger.error(e);
        }
        logger.info("******************* FIN NewOrderService > validateConfigBafi2300  *******************");
        return result;
    }

	/***********************************************************************************************************************************
     * <br>Autor  : JCURI
     * <br>project: PRY-1093
     * <br>Motivo : Valida la activacion del check de courier
     * <br>Fecha  : 23/05/2018      
     / ************************************************************************************************************************************/
    public boolean activeChkCourier(int iUserId, int iAppId) {
        HashMap objResultHashMap = new HashMap();
        try {
            return getSEJBOrderNewRemote().activeChkCourier(iUserId,iAppId);
        } catch (SQLException e) {
            System.out.println("[SQLException][NewOrderService][activeChkCourier][" + e.getMessage() + "]["+e.getClass()+"]");
            objResultHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return false;
        } catch (RemoteException e) {
            System.out.println("[RemoteException][NewOrderService][activeChkCourier][" + e.getMessage() + "]["+e.getClass()+"]");
            objResultHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("[Exception][NewOrderService][activeChkCourier][" + e.getMessage() + "]["+e.getClass()+"]");
            objResultHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return false;
        }
    }

    /**
     * @author KPEREZ
     * @project PRY-1037
     * Metodo   Valida  .
     * @return
     */
    public HashMap doValidateSimManager( String[] item_Product_Val)
    {
        HashMap objResultHashMap = new HashMap();
        try
        {
            return getSEJBOrderNewRemote().doValidateSimManager(item_Product_Val);
        }
        catch (SQLException e)
        {
            logger.info("[SQLException][NewOrderService][doValidateSimManager][" + e.getMessage() + "][" + e.getClass() + "]");
            objResultHashMap.put("strMessage", e.getClass() + " " + e.getMessage());
            return objResultHashMap;
        }
        catch (RemoteException e)
        {
            logger.info("[RemoteException][NewOrderService][doValidateSimManager][" + e.getMessage() + "][" + e.getClass() + "]");
            objResultHashMap.put("strMessage", e.getClass() + " " + e.getMessage());
            return objResultHashMap;
        }
        catch (Exception e)
        {
            logger.info("[Exception][NewOrderService][doValidateSimManager][" + e.getMessage() + "][" + e.getClass() + "]");
            objResultHashMap.put("strMessage", e.getClass() + " " + e.getMessage());
        }
        return objResultHashMap;
    }


    /**
     * @author AMENDEZ
     * @project PRY-1200
     * Metodo   Valida si la especificacion esta configurada para VEP
     *          flag 1, Aplica
     *          flag 0, No aplica
     *          flag -1, Errores
     * @return
     */
    public String validateSpecificationVep(int anum_npspecificationid){
        logger.info("******************* INICIO NewOrderService > validateSpecificationVep  *******************");

        logger.info("anum_npspecificationid   : "+anum_npspecificationid);

        String strMessage=null;
        try {
            strMessage= getSEJBOrderNewRemote().validateSpecificationVep(anum_npspecificationid);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][validateSpecificationVep]";
            logger.error(strMessage,e);
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][validateSpecificationVep]";
            logger.error(strMessage,e);
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][validateSpecificationVep]";
            logger.error(strMessage,e);
        }
        logger.info("******************* FIN NewOrderService > validateSpecificationVep  *******************");
        return strMessage;
    }

    /**
     * @author AMENDEZ
     * @project PRY-1200
     * Metodo   Obtiene valor de configuracion de tablas VEP
     * @return
     */
    public String getConfigValueVEP(String avch_npvaluedesc){
        logger.info("******************* INICIO NewOrderService > getConfigValueVEP(String avch_npvaluedesc)  *******************");

        logger.info("avch_npvaluedesc   : "+avch_npvaluedesc);

        String strMessage=null;
        try {
            strMessage= getSEJBOrderNewRemote().getConfigValueVEP(avch_npvaluedesc);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][getConfigValueVEP]";
            logger.error(strMessage,e);
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][getConfigValueVEP]";
            logger.error(strMessage,e);
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getConfigValueVEP]";
            logger.error(strMessage,e);
        }
        logger.info("******************* FIN NewOrderService > getConfigValueVEP(String avch_npvaluedesc)  *******************");
        return strMessage;
    }

} 