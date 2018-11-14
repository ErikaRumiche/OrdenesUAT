package pe.com.nextel.service;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import java.util.Map;

import javax.naming.Context;

import javax.rmi.PortableRemoteObject;

import org.apache.commons.lang.StringUtils;

import pe.com.nextel.bean.AddressObjectBean;
import pe.com.nextel.bean.CustomerBean;
import pe.com.nextel.ejb.SEJBCustomerRemote;
import pe.com.nextel.ejb.SEJBCustomerRemoteHome;
import pe.com.nextel.util.MiUtil;


/**
 * Motivo: Clase Service que contendrá lógica de negocio referente al Cliente.
 * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>,
 * <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>,
 * <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
 * <br>Fecha: 26/08/2007
 * @see GenericService
 */
public class CustomerService extends GenericService
{


    public static SEJBCustomerRemote getSEJBCustomerRemote() {
         try{
             final Context context = MiUtil.getInitialContext();
             final SEJBCustomerRemoteHome sEJBOrderNewRemoteHome = 
                 (SEJBCustomerRemoteHome) PortableRemoteObject.narrow( context.lookup( "SEJBCustomer" ), SEJBCustomerRemoteHome.class );
             SEJBCustomerRemote sEJBOrderNewRemote;
             sEJBOrderNewRemote = sEJBOrderNewRemoteHome.create();
             
             return sEJBOrderNewRemote;
         }catch(Exception ex) {
             System.out.println("Exception : [CustomerService][getSEJBCustomerRemote]["+ex.getMessage()+"]");
             return null;
         }
         
     }
     public HashMap getValidationFraudCustomer(String strDocCustomer){
      
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();
        try{
          return  getSEJBCustomerRemote().getValidationFraudCustomer(strDocCustomer);
        }
        catch(SQLException e){
          strMessage  = "[SQLException][CustomerService][getValidationFraudCustomer][" + e.getClass() + " " + e.getMessage()+"]";
          objHashMap.put("strMessage",strMessage);
          return objHashMap;
       }
       catch(RemoteException e){
          strMessage  = "[RemoteException][CustomerService][getValidationFraudCustomer][" + e.getClass() + " " + e.getMessage()+"]";
          objHashMap.put("strMessage",strMessage);
          return objHashMap;
       }
       catch(Exception e){
            strMessage  = "[Exception][CustomerService][getValidationFraudCustomer][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
       }
     }
    public HashMap CustomerDAOgetCustomerAddress(long intObjectId, long longRegionId,String strObjectType,String strGeneratortype){
       HashMap objHashMap = new HashMap();
       String  strMessage = new String();
     
        try {
            return getSEJBCustomerRemote().CustomerDAOgetCustomerAddress(intObjectId,longRegionId,strObjectType, strGeneratortype);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][CustomerDAOgetCustomerAddress][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][CustomerDAOgetCustomerAddress][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][CustomerDAOgetCustomerAddress][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }
    
    public HashMap CustomerDAOgetCustomerJava(long longAppId, String strPhoneNumber, long longContractId, String strIMEI,String strSIM,String strNumeroRadio){
       HashMap objHashMap = new HashMap();
       String  strMessage = new String();
     
        try {
            return getSEJBCustomerRemote().CustomerDAOgetCustomerJava(longAppId,strPhoneNumber,longContractId,strIMEI,strSIM,strNumeroRadio);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][CustomerDAOgetCustomerJava][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][CustomerDAOgetCustomerJava][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][CustomerDAOgetCustomerJava][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }
    
    public HashMap CustomerDAOgetAddressDelivery(long intObjectId, String strObjectType, String strGeneratorType, String strRegionId){
       HashMap objHashMap = new HashMap();
       String  strMessage = new String();
     
        try {
            return getSEJBCustomerRemote().CustomerDAOgetAddressDelivery(intObjectId,strObjectType,strGeneratorType,strRegionId);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][CustomerDAOgetAddressDelivery][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][CustomerDAOgetAddressDelivery][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][CustomerDAOgetAddressDelivery][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }
    
    public static int CustomerDAOgetUnknowSite(long intObjectId, String strObjectType){
        try {
            return getSEJBCustomerRemote().CustomerDAOgetUnknowSite(intObjectId,strObjectType);
        }catch (SQLException e) {
            return -1;
        }catch (RemoteException e) {
            return -1;
        }catch(Exception e){
            return -1;
        }
    }
    
    public Hashtable CustomerDAOgetCustomerIdCrmByBSCS(long intCodigoCliente){
        try {
            return getSEJBCustomerRemote().CustomerDAOgetCustomerIdCrmByBSCS( intCodigoCliente);
        }catch (SQLException e) {
            System.out.println("[SQLException][CustomerService][CustomerDAOgetCustomerIdCrmByBSCS][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }catch (RemoteException e) {
            System.out.println("[RemoteException][CustomerService][CustomerDAOgetCustomerIdCrmByBSCS][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }catch(Exception e){
            System.out.println("[Exception][CustomerService][CustomerService][CustomerDAOgetCustomerIdCrmByBSCS][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
    }
    
    public Hashtable CustomerDAOgetCustPatternQty(String strNombreCompania){
        try {
            return getSEJBCustomerRemote().CustomerDAOgetCustPatternQty( strNombreCompania);
        }catch (SQLException e) {
            System.out.println("[SQLException][CustomerService][CustomerDAOgetCustPatternQty][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (RemoteException e) {
            System.out.println("[RemoteException][CustomerService][CustomerDAOgetCustPatternQty][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }catch(Exception e){
            System.out.println("[Exception][CustomerService][CustomerService][CustomerDAOgetCustomerIdCrmByBSCS][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
    }
    
    public Hashtable CustomerDAOgetCustRucQty(String strRuc){
        try {
            return getSEJBCustomerRemote().CustomerDAOgetCustRucQty( strRuc);
        }catch (SQLException e) {
            System.out.println("[SQLException][CustomerService][CustomerDAOgetCustRucQty][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }catch (RemoteException e) {
            System.out.println("[RemoteException][CustomerService][CustomerDAOgetCustRucQty][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }catch(Exception e){
            System.out.println("[Exception][CustomerService][CustomerService][CustomerDAOgetCustomerIdCrmByBSCS][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
    }
    
     public Hashtable CustomerDAOgetSiteData(long intSiteid, String strCreatedby, long intUserid, long intAppid, long intRegionid, String strGeneratortype, String strGeneratorId){
        try {
            return getSEJBCustomerRemote().CustomerDAOgetSiteData( intSiteid,strCreatedby,intUserid,intAppid,intRegionid,strGeneratortype, strGeneratorId);
        }catch (SQLException e) {
            System.out.println("[SQLException][CustomerService][CustomerDAOgetCustRucQty][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }catch (RemoteException e) {
            System.out.println("[RemoteException][CustomerService][CustomerDAOgetCustRucQty][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }catch(Exception e){
            System.out.println("[Exception][CustomerService][CustomerService][CustomerDAOgetCustomerIdCrmByBSCS][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
    }

    public HashMap CustomerDAOgetCustomerData(long iCustomerId,String strSwcreatedBy, long iRegionid,int iUserId,int iAppId,String strGeneratorId,String strGeneratorType) {        
      HashMap   objHashMap   = new HashMap();
      String    strMessage   = new String();
      
      try{
         return getSEJBCustomerRemote().CustomerDAOgetCustomerData(iCustomerId,strSwcreatedBy,iRegionid,iUserId,iAppId,strGeneratorId,strGeneratorType);
      }catch(RemoteException e){
         strMessage  = "[RemoteException][CustomerService][CustomerDAOgetCustomerData][" + e.getClass() + " " + e.getMessage()+"]";
         System.out.println(strMessage);
         objHashMap.put("strMessage",strMessage);
         return objHashMap;
      }catch(Exception e){
         strMessage  = "[Exception][CustomerService][CustomerDAOgetCustomerData][" + e.getClass() + " " + e.getMessage()+"]";
         System.out.println(strMessage);
         objHashMap.put("strMessage",strMessage);
         return objHashMap;
      }
    }      
    
/***********************************************************************
***********************************************************************
***********************************************************************
*  INTEGRACION DE ORDENES Y RETAIL - INICIO
*  REALIZADO POR: Carmen Gremios
*  FECHA: 13/09/2007
***********************************************************************
***********************************************************************
***********************************************************************/


/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* @see pe.com.nextel.dao.CustomerDAO#getCustomerData(int,String,int,String)      
*/     
public HashMap getCustomerData2(long iCustomerId,String strSwcreatedBy, long iRegionid) {        
   try{
      return getSEJBCustomerRemote().getCustomerData2(iCustomerId,strSwcreatedBy,iRegionid);
   }catch(RemoteException re){
      System.out.println("CustomerService getCustomerData2 Remote\nMensaje:" + re.getMessage()+"\n");      
      HashMap hshData=new HashMap();
      hshData.put("strMessage",re.getMessage());             
      re.printStackTrace();
      return hshData;  
   }catch(Exception ex){
      System.out.println("CustomerService getCustomerData2 Exception\nMensaje:" + ex.getMessage()+"\n");                  
      HashMap hshData=new HashMap();
      hshData.put("strMessage",ex.getMessage());             
      ex.printStackTrace();
      return hshData;  
   }
}    

/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* @see pe.com.nextel.dao.CustomerDAO#getCustomerData(int,String)      
*/ 
public HashMap getCustomerData(long iNpCustomerId) { //Detalle Cliente
   try{
      return getSEJBCustomerRemote().getCustomerData(iNpCustomerId);
   }catch(RemoteException re){
      System.out.println("CustomerService getCustomerData \nMensaje:" + re.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",re.getMessage());             
      return hshData;  
   }catch(Exception ex){
      System.out.println("CustomerService getCustomerData \nMensaje:" + ex.getMessage()+"\n");            
      HashMap hshData=new HashMap();
      hshData.put("strMessage",ex.getMessage());             
      return hshData;  
   }
}
     
/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 15/08/2007
* @see pe.com.nextel.dao.CustomerDAO#getCustomerContacts(int,String,String)      
*/ 
public HashMap getCustomerContacts2(long intObjectId, String strObjectType){
   try{
      return getSEJBCustomerRemote().getCustomerContacts2(intObjectId, strObjectType);
   }catch(RemoteException re){
      System.out.println("CustomerService getCustomerContacts2 \nMensaje:" + re.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",re.getMessage());             
      return hshData;  
   }catch(Exception ex){
      System.out.println("CustomerService getCustomerContacts2 \nMensaje:" + ex.getMessage()+"\n");            
      HashMap hshData=new HashMap();
      hshData.put("strMessage",ex.getMessage());             
      return hshData;  
   }
}
    
    

/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 05/08/2007
* @see pe.com.nextel.dao.CustomerDAO#getCustomerAddress(int,String,String)      
*/      
public HashMap getCustomerAddress2(long intObjectId,long longRegionId, String strObjectType){
   try{
      return getSEJBCustomerRemote().getCustomerAddress2(intObjectId,longRegionId, strObjectType);
   }catch(RemoteException re){
      System.out.println("CustomerService getCustomerAddress2 \nMensaje:" + re.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",re.getMessage());             
      return hshData;  
   }catch(Exception ex){
      System.out.println("CustomerService getCustomerAddress2 \nMensaje:" + ex.getMessage()+"\n");            
      HashMap hshData=new HashMap();
      hshData.put("strMessage",ex.getMessage());             
      return hshData;    
   }
}
    
/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 28/09/2007
* @see pe.com.nextel.dao.CustomerDAO#getCustomerSitesList(long,String,String)      
*/      
public HashMap getCustomerSitesList(long lCustomerId,long lOrderId,long lOportunityId,String strEstadoSite){        
   try{        
      return getSEJBCustomerRemote().getCustomerSitesList(lCustomerId, lOrderId,lOportunityId,strEstadoSite);           
   }catch(SQLException sql){
      System.out.println("CustomerService getCustomerSitesList \nMensaje:" + sql.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",sql.getMessage());             
      return hshData;           
   }catch(RemoteException re){
      System.out.println("CustomerService getCustomerSitesList \nMensaje:" + re.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",re.getMessage());             
      return hshData;      
   }catch(Exception ex){
      System.out.println("CustomerService getCustomerSitesList \nMensaje:" + ex.getMessage()+"\n");            
      HashMap hshData=new HashMap();
      hshData.put("strMessage",ex.getMessage());             
      return hshData;    
   }
}
    

    
/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 02/10/2007
* @see pe.com.nextel.dao.CustomerDAO#getCustomerType(long,String,String)      
*/      
public String getCustomerType(long lCustomerId){
   try{
      return getSEJBCustomerRemote().getCustomerType(lCustomerId);
   }catch(RemoteException re){
      System.out.println("CustomerService getCustomerType \nMensaje:" + re.getMessage()+"\n");            
      return "";   
   }catch(Exception ex){
      System.out.println("CustomerService getCustomerType \nMensaje:" + ex.getMessage()+"\n");                         
      return "";   
   }
}

/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 04/10/2007
* @see pe.com.nextel.dao.CustomerDAO#getCustomerContactAll(long,int)      
*/      
public ArrayList getCustomerContactAll(long lCustomerId, int iResultado){
   try{
      return getSEJBCustomerRemote().getCustomerContactAll(lCustomerId, iResultado);
   }catch(RemoteException re){
      System.out.println("CustomerService getCustomerContactAll \nMensaje:" + re.getMessage()+"\n");
      ArrayList arrLista=new ArrayList();
      return arrLista;   
   }catch(Exception ex){
      System.out.println("CustomerService getCustomerContactAll \nMensaje:" + ex.getMessage()+"\n");            
      ArrayList arrLista=new ArrayList();
      return arrLista;     
   }
}    
   
/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 04/10/2007
* @see pe.com.nextel.dao.CustomerDAO#getSiteContactAll(long,long,int)      
*/      
public ArrayList getSiteContactAll(long lCustomerId,long lSiteId, int iResultado){
   try{
      return getSEJBCustomerRemote().getSiteContactAll(lCustomerId, lSiteId,iResultado);
   }catch(RemoteException re){
      System.out.println("CustomerService getSiteContactAll \nMensaje:" + re.getMessage()+"\n");
      ArrayList arrLista=new ArrayList();
      return arrLista;   
   }catch(Exception ex){
      System.out.println("CustomerService getSiteContactAll \nMensaje:" + ex.getMessage()+"\n");            
      ArrayList arrLista=new ArrayList();
      return arrLista;     
   }
}   

/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 04/10/2007
* @see pe.com.nextel.dao.CustomerDAO#getDeliveryAddress(long,String,String)      
*/      
public HashMap getDeliveryAddress(long lObjetId,String strObjectType) {
   try{
      return getSEJBCustomerRemote().getDeliveryAddress(lObjetId,strObjectType);
   }catch(RemoteException re){
      System.out.println("CustomerService getDeliveryAddress \nMensaje:" + re.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",re.getMessage());             
      return hshData;    
   }catch(Exception ex){
      System.out.println("CustomerService getDeliveryAddress \nMensaje:" + ex.getMessage()+"\n");            
      HashMap hshData=new HashMap();
      hshData.put("strMessage",ex.getMessage());             
      return hshData;     
   }
}                            

/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 04/10/2007
* @see pe.com.nextel.dao.CustomerDAO#getDeliveryAddress(long,String,String)      
*/      
public HashMap getAddressByRegion(long lObjetId,String strObjectType,long lSellerRegId) {
   try{
      return getSEJBCustomerRemote().getAddressByRegion(lObjetId,strObjectType,lSellerRegId);
   }catch(RemoteException re){
      System.out.println("CustomerService getAddressByRegion \nMensaje:" + re.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",re.getMessage());             
      return hshData;  
   }catch(Exception ex){
      System.out.println("CustomerService getAddressByRegion \nMensaje:" + ex.getMessage()+"\n");            
      HashMap hshData=new HashMap();
      hshData.put("strMessage",ex.getMessage());             
      return hshData;  
   }
}         

/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 05/11/2007
* @see pe.com.nextel.dao.CustomerDAO#getAddress(long,String,String)      
*/      
public HashMap getAddress(long lObjectTypeId, String strObjectType,String strType) {
   try{
      return getSEJBCustomerRemote().getAddress(lObjectTypeId,strObjectType,strType);
   }catch(RemoteException re){
      System.out.println("CustomerService getAddress \nMensaje:" + re.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",re.getMessage());             
      return hshData;  
   }catch(Exception ex){
      System.out.println("CustomerService getAddress \nMensaje:" + ex.getMessage()+"\n");            
      HashMap hshData=new HashMap();
      hshData.put("strMessage",ex.getMessage());             
      return hshData;  
   }
} 

/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 05/11/2007
* @see pe.com.nextel.dao.CustomerDAO#getContact(long,String,String)      
*/      
public HashMap getContact(long lObjectTypeId, String strObjectType,String strType) {
   try{
      return getSEJBCustomerRemote().getContact(lObjectTypeId,strObjectType,strType);
   }catch(RemoteException re){
      System.out.println("CustomerService getContact \nMensaje:" + re.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",re.getMessage());             
      return hshData;  
   }catch(Exception ex){
      System.out.println("CustomerService getContact \nMensaje:" + ex.getMessage()+"\n");            
      HashMap hshData=new HashMap();
      hshData.put("strMessage",ex.getMessage());             
      return hshData;  
   }
}    

/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 05/11/2007
* @see pe.com.nextel.dao.CustomerDAO#getAddressChange(long,int,long)      
*/      
public HashMap getAddressChange(long lOrderId, int iTypeObject,long lObjectId){
   try{
      return getSEJBCustomerRemote().getAddressChange(lOrderId,iTypeObject,lObjectId);
   }catch(RemoteException re){
      System.out.println("CustomerService getAddressChange \nMensaje:" + re.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",re.getMessage());             
      return hshData;  
   }catch(Exception ex){
      System.out.println("CustomerService getAddressChange \nMensaje:" + ex.getMessage()+"\n");            
      HashMap hshData=new HashMap();
      hshData.put("strMessage",ex.getMessage());             
      return hshData;  
   }
}   

/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 05/11/2007
* @see pe.com.nextel.dao.CustomerDAO#getContactChange(long,int,long)      
*/      
public HashMap getContactChange(long lOrderId, int iTypeObject,long lObjectId){
   try{
      return getSEJBCustomerRemote().getContactChange(lOrderId,iTypeObject,lObjectId);
   }catch(RemoteException re){
      System.out.println("CustomerService getContactChange \nMensaje:" + re.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",re.getMessage());             
      return hshData;  
   }catch(Exception ex){
      System.out.println("CustomerService getContactChange \nMensaje:" + ex.getMessage()+"\n");            
      HashMap hshData=new HashMap();
      hshData.put("strMessage",ex.getMessage());             
      return hshData;  
   }
}  

/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 05/11/2007
* @see pe.com.nextel.dao.CustomerDAO#getHeaderChange(long,int,long)      
*/      
public HashMap getHeaderChange(long lOrderId, int iTypeObject,long lObjectId){
   try{
      return getSEJBCustomerRemote().getHeaderChange(lOrderId,iTypeObject,lObjectId);
   }catch(RemoteException re){
      System.out.println("CustomerService getHeaderChange \nMensaje:" + re.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",re.getMessage());             
      return hshData;  
   }catch(Exception ex){
      System.out.println("CustomerService getHeaderChange \nMensaje:" + ex.getMessage()+"\n");            
      HashMap hshData=new HashMap();
      hshData.put("strMessage",ex.getMessage());             
      return hshData;  
   }
}  


/**
* <br>Realizado por: <a href="mailto:cristian.espinoza@nextel.com.pe">Cristian Espinoza</a>
* <br>Fecha: 27/03/2008
* @see pe.com.nextel.dao.CustomerDAO#getCustomerIdBSCS(long,String)      
*/      
public long getCustomerIdBSCS(long lCustomerId, String strObjectType){
   try{
      return getSEJBCustomerRemote().getCustomerIdBSCS(lCustomerId, strObjectType);
   }catch(RemoteException re){
      System.out.println("CustomerService getCustomerIdBSCS \nMensaje:" + re.getMessage()+"\n");           
      return 0;  
   }catch(Exception ex){
      System.out.println("CustomerService getCustomerIdBSCS \nMensaje:" + ex.getMessage()+"\n");                         
      return 0;  
   }
} 

/***********************************************************************
***********************************************************************
***********************************************************************
*  INTEGRACION DE ORDENES Y RETAIL - FIN
*  REALIZADO POR: Carmen Gremios
*  FECHA: 13/09/2007
***********************************************************************
***********************************************************************
***********************************************************************/    
    
    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - INICIO
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 24/09/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/

    /**
     * @see pe.com.nextel.dao.ClientDAO#getSitesByCustomer(long)
     */
    public ArrayList getSitesByCustomer(long customerId) {
      try {
        return getSEJBCustomerRemote().getSitesByCustomer(customerId);
      }catch(Throwable t){
        return null;    
      }
    }

    /**
     * @see pe.com.nextel.dao.ClientDAO#getCustomerInfo(long)
     */
    public CustomerBean getCustomerInfo(long customerId){
      try {
        return getSEJBCustomerRemote().getCustomerInfo(customerId);
      }catch(Throwable t){
        return null;    
      }
    }

    /**
     * @see pe.com.nextel.dao.ClientDAO#getAddressesByCustomer(long)
     */
  public HashMap getAddressesByCustomer(long customerId) {
    try {
      return  getSEJBCustomerRemote().getAddressesByCustomer(customerId);
    }catch(Throwable t){
        return null;
    }
  }
  
  /**
   * Motivo:  Valida que el Cliente exista, según el <i>tipoDocumento, nroDocumento</i>.
   *          De acuerdo a ello Carga la Información del Cliente.
   * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
   * <br>Fecha: 26/08/2007
   * @param     tipoDocumento  Ej. RUC, DNI
   * @param     nroDocumento  Ej. 20421814041
   * @return    CustomerBean con la Información del Cliente.
   */
  public CustomerBean loadCustomerInfo(String tipoDocumento, String nroDocumento) throws Exception,SQLException{
    CustomerBean customer = new CustomerBean();
    AddressObjectBean beanAddress = new AddressObjectBean();
    String strError = null;
    CustomerBean parametro = getSEJBCustomerRemote().getValidateCustomer(tipoDocumento, nroDocumento);
    customer.setStrMessage(parametro.getStrMessage());

    if ( parametro.getSwCustomerId() > 0){
      HashMap objHashMapExtOrdProsp = getSEJBCustomerRemote().getExisteOrdenProspect(parametro.getSwCustomerId());    

      if(objHashMapExtOrdProsp.get("strMessage")!=null ){
        customer.setStrMessage((String)objHashMapExtOrdProsp.get("strMessage"));
        return customer;
      }
    }

    if ( parametro.getSwCustomerId() > 0){
      customer = getSEJBCustomerRemote().getCustomerInfo(parametro.getSwCustomerId());
      strError = customer.getStrMessage();
        if (StringUtils.isEmpty(strError)){
          customer.setStatus(parametro.getStatus());
          customer.setCuentaList(getSEJBCustomerRemote().getSitesByCustomer(parametro.getSwCustomerId()));
          HashMap objHashMap = getSEJBCustomerRemote().getAddressesByCustomer(parametro.getSwCustomerId());
          if(objHashMap.get("strMessage")!=null )
            customer.setStrMessage((String)objHashMap.get("strMessage"));
          ArrayList objArrayList = (ArrayList)objHashMap.get("objArrayList");
          customer.setDireccionList(objArrayList);
        }
    }else{           
      customer.createProspect();
    }
    return customer;
  }
     
    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - FIN
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 24/09/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
 /**
  * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
  * <br>Fecha: 28/09/2007
  * @see pe.com.nextel.dao.CustomerDAO#getCustomerSites(long,String)      
  */      
  public HashMap getCustomerSites(long intCustomerId,String strEstadoSite){
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBCustomerRemote().getCustomerSites(intCustomerId, strEstadoSite);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }
    
    /**
       * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
       * <br>Fecha: 28/09/2007
       * @see pe.com.nextel.dao.CustomerDAO#getCustomerSearch(long,String)      
    */    
  public HashMap getCustomerSearch(String strTipoDocumento, 
                                   String strNumeroDocumento,
                                   String strRazonSocial,
                                   String strNombreCliente,
                                   String strTipoCliente,
                                   long intCustomerId,
                                   long intRegionId,
                                   String strSessionCode,
                                   int intSessionLevel,
                                   int lPageSelected,
                                   int iProGrpId,
                                   String strLogin,
                                   int intSalesStructId,
                                   int iUserId1,
                                   int iAppId1
                                   ){
    HashMap hshDataMap = new HashMap();
    System.out.println("CustomerService.iProGrpId ---> "+iProGrpId);
      try {
        return(getSEJBCustomerRemote().
            getCustomerSearch(strTipoDocumento,strNumeroDocumento,strRazonSocial,
            strNombreCliente,strTipoCliente,intCustomerId,intRegionId,strSessionCode,
            intSessionLevel,lPageSelected,iProGrpId, strLogin, intSalesStructId,
            iUserId1, iAppId1
            ));
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }  
    
 /**
  * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
  * <br>Fecha: 28/09/2007
  * @see pe.com.nextel.dao.CustomerDAO#getSourceAddress(long,String)      
  */ 
  public HashMap getSourceAddress(long lngCustomerId, long lngItemId){
     HashMap hshDataMap = new HashMap();
      try {
        return getSEJBCustomerRemote().getSourceAddress(lngCustomerId, lngItemId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }
  
 /**
  * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
  * <br>Fecha: 28/09/2007
  * @see pe.com.nextel.dao.CustomerDAO#getDestinyAddress(long,String)      
  */ 
  public HashMap getDestinyAddress(long lngCustomerId){
     HashMap hshDataMap = new HashMap();
      try {
        return getSEJBCustomerRemote().getDestinyAddress(lngCustomerId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }
  
  /**
   * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
   * <br>Fecha: 28/09/2007
   * @see pe.com.nextel.dao.CustomerDAO#getCustomerContactsByType(long,String)      
   */ 
  public HashMap getCustomerContactsByType(long lngCustomerId, String strType){
     HashMap hshDataMap = new HashMap();
      try {
        return getSEJBCustomerRemote().getCustomerContactsByType(lngCustomerId,strType);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }

  /**
   * <br>Realizado por: <a href="mailto:cristian.espinoza@nextel.com.pe">Cristian Espinoza</a>
   * <br>Fecha: 06/02/2008
   * @see pe.com.nextel.dao.CustomerDAO#getInfoCustomer(long)      
   */ 
  public HashMap getInfoCustomer(long lOrderId){
     HashMap hshDataMap = new HashMap();
      try {
        return getSEJBCustomerRemote().CustomerDAOgetInfoCustomer(lOrderId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }
 /**
  * <br>Realizado por: <a href="mailto:oliver.dubock@nextel.com.pe">Oliver Dubock</a>
  * <br>Fecha: 14/05/2008
  * @see pe.com.nextel.dao.CustomerDAO#getValidateDniRucEquals(String,String)      
  */ 
  public HashMap getValidateDniRucEquals(String strNumDoc, String strTipoDoc){
     HashMap hshDataMap = new HashMap();
      try {
        return getSEJBCustomerRemote().CustomerDAOgetValidateDniRucEquals(strNumDoc,strTipoDoc);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }

	/**
	* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
	* @see pe.com.nextel.dao.CustomerDAO#getCustomerData(int,String,int,String)      
	*/     
	public HashMap getCustomerDataDetail(long iCustomerId,String strSwcreatedBy, long iRegionid, long lOrderId) {        
		try{
			return getSEJBCustomerRemote().getCustomerDataDetail(iCustomerId,strSwcreatedBy,iRegionid,lOrderId);
		}
		catch(RemoteException re){
			System.out.println("CustomerService getCustomerDataDetail Remote\nMensaje:" + re.getMessage()+"\n");      
			HashMap hshData=new HashMap();
			hshData.put("strMessage",re.getMessage());             
			re.printStackTrace();
			return hshData;  
		}catch(Exception ex){
			System.out.println("CustomerService getCustomerDataDetail Exception\nMensaje:" + ex.getMessage()+"\n");                  
			HashMap hshData=new HashMap();
			hshData.put("strMessage",ex.getMessage());             
			ex.printStackTrace();
			return hshData;  
		}
}    

	/**
	* <br>Realizado por: <a href="mailto:cristian.espinoza@nextel.com.pe">Cristian Espinoza</a>
	* @see pe.com.nextel.dao.CustomerDAO#getNumAddressId(String,long,String)      
	*/     
	public HashMap getNumAddressId (String strTipoObjeto, long lCodigo, String strTipoDireccion, long lRegionId) {        
		try{
			return getSEJBCustomerRemote().getNumAddressId(strTipoObjeto,lCodigo,strTipoDireccion, lRegionId);
	  }
    catch(SQLException sql){
		  System.out.println("CustomerService getNumAddressId \nMensaje:" + sql.getMessage()+"\n");
		  HashMap hshData=new HashMap();
		  hshData.put("strMessage",sql.getMessage());             
		  return hshData;           
	  }
    catch(RemoteException re){
		  System.out.println("CustomerService getNumAddressId \nMensaje:" + re.getMessage()+"\n");
		  HashMap hshData=new HashMap();
		  hshData.put("strMessage",re.getMessage());             
		  return hshData;      
	  }
    catch(Exception ex){
		  System.out.println("CustomerService getNumAddressId \nMensaje:" + ex.getMessage()+"\n");            
		  HashMap hshData=new HashMap();
		  hshData.put("strMessage",ex.getMessage());             
		  return hshData;    
	  }
  }    
  
  /**
	* <br>Realizado por: <a href="mailto:cristian.espinoza@nextel.com.pe">Cristian Espinoza</a>
	* @see pe.com.nextel.dao.CustomerDAO#getIsSiteBA(String,long,String)      
	*/     
	public byte getIsSiteBA(long lngCustomerId) throws Exception, SQLException{
		try{
			return getSEJBCustomerRemote().getIsSiteBA(lngCustomerId);
	  }catch(SQLException e){
		  System.out.println("CustomerService getIsSiteBA \nMensaje:" + e.getClass() + " " + e.getMessage()+"\n");
		  return -1;
	  }
    catch(RemoteException e){
		  System.out.println("CustomerService getIsSiteBA \nMensaje:" + e.getClass() + " " + e.getMessage()+"\n");
		  return -1;
	  }
    catch(Exception e){
		  System.out.println("CustomerService getIsSiteBA \nMensaje:" + e.getClass() + " " + e.getMessage()+"\n");          
		  return -1;
	  }
  }    
  
  /**
  * <br>Realizado por: <a href="mailto:hugo.moreno@nextel.com.pe">Hugo Moreno</a>
  * <br>Fecha: 13/10/2008
  * @see pe.com.nextel.dao.CustomerDAO#getValidateDocument(String,String)      
  */ 
  public HashMap getValidateDocument(String strNumDoc, String strTipoDoc){
     HashMap hshDataMap = new HashMap();
      try {
        return getSEJBCustomerRemote().CustomerDAOgetValidateDocument(strNumDoc,strTipoDoc);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }
  
  
    /***********************************************************************
     *  ORDENES SSAA - INICIO
     *  REALIZADO POR: Ruth Polo
     *  FECHA: 09/01/2009
     ***********************************************************************/
  public static long CustomerDAOgetRol(long intScreenoptionid, long intUserid, long intAppid){
      try {
          return getSEJBCustomerRemote().CustomerDAOgetRol(intScreenoptionid,intUserid,intAppid);
      }catch (SQLException e) {
          return -1;
      }catch (RemoteException e) {
          return -1;
      }catch(Exception e){
          return -1;
      }
  }

    /***********************************************************************
     *  ORDENES SSAA - FIN
     *  REALIZADO POR: Ruth Polo
     *  FECHA: 09/01/2009
     ***********************************************************************/  
     
     
     
    /*Incio Responsable de Pago*/ 
   /*
   * JPEREZ
   * @return HashMap
   * @param iSolution
   * @param intCustomerId
   */
   
   public HashMap CustomerDAOgetCustomerSitesBySolution(long intCustomerId, int iSolution, int iSpecification){        
   try{        
      return getSEJBCustomerRemote().CustomerDAOgetCustomerSitesBySolution(intCustomerId, iSolution, iSpecification);
   }catch(SQLException sql){
      System.out.println("CustomerService CustomerDAOgetCustomerSitesBySolution \nMensaje:" + sql.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",sql.getMessage());             
      return hshData;           
   }catch(RemoteException re){
      System.out.println("CustomerService CustomerDAOgetCustomerSitesBySolution \nMensaje:" + re.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",re.getMessage());             
      return hshData;      
   }catch(Exception ex){
      System.out.println("CustomerService CustomerDAOgetCustomerSitesBySolution \nMensaje:" + ex.getMessage()+"\n");            
      HashMap hshData=new HashMap();
      hshData.put("strMessage",ex.getMessage());             
      return hshData;    
   }
  }
  /*Fin Responsable de Pago*/ 
    
    /**
	* <br>Realizado por: <a href="mailto:evelyn.ocampo@nextel.com.pe">Evelyn Ocampo</a>
	* @see pe.com.nextel.dao.CustomerDAO#getContactList(long,String,String)      
	*/     
	public HashMap getContactList (long lObjectTypeId, String strObjectType,String strType, long lSiteId) {        
		try{
			return getSEJBCustomerRemote().getContactList(lObjectTypeId,strObjectType,strType, lSiteId);
	  }
    catch(SQLException sql){
		  System.out.println("CustomerService getContactList \nMensaje:" + sql.getMessage()+"\n");
		  HashMap hshData=new HashMap();
		  hshData.put("strMessage",sql.getMessage());             
		  return hshData;           
	  }
    catch(RemoteException re){
		  System.out.println("CustomerService getContactList \nMensaje:" + re.getMessage()+"\n");
		  HashMap hshData=new HashMap();
		  hshData.put("strMessage",re.getMessage());             
		  return hshData;      
	  }
    catch(Exception ex){
		  System.out.println("CustomerService getContactList \nMensaje:" + ex.getMessage()+"\n");            
		  HashMap hshData=new HashMap();
		  hshData.put("strMessage",ex.getMessage());             
		  return hshData;    
	  }
  }   
  
  /**
	* <br>Realizado por: <a href="mailto:evelyn.ocampo@nextel.com.pe">Evelyn Ocampo</a>
	* @see pe.com.nextel.dao.CustomerDAO#getContactList(long,String,String)      
	*/     
	public HashMap getContactChangeList(long lOrderId,int iItemid,int iObjectType,long lObjectId, String strContactType){ 
		try{
			return getSEJBCustomerRemote().getContactChangeList(lOrderId,iItemid,iObjectType,lObjectId,strContactType);
	  }
    catch(SQLException sql){
		  System.out.println("CustomerService getContactList \nMensaje:" + sql.getMessage()+"\n");
		  HashMap hshData=new HashMap();
		  hshData.put("strMessage",sql.getMessage());             
		  return hshData;           
	  }
    catch(RemoteException re){
		  System.out.println("CustomerService getContactList \nMensaje:" + re.getMessage()+"\n");
		  HashMap hshData=new HashMap();
		  hshData.put("strMessage",re.getMessage());             
		  return hshData;      
	  }
    catch(Exception ex){
		  System.out.println("CustomerService getContactList \nMensaje:" + ex.getMessage()+"\n");            
		  HashMap hshData=new HashMap();
		  hshData.put("strMessage",ex.getMessage());             
		  return hshData;    
	  }
  }
  /**
	* <br>Realizado por: <a href="mailto:cesar.barzola@nextel.com.pe">Cesar Barzola</a>
	* @see pe.com.nextel.dao.ProposedDAO#getValidationCustomer(long,long)      
	*/ 
  public HashMap getValidationCustomer(long lUserId,long lCustomerId){
    try{
			return getSEJBCustomerRemote().getValidationCustomer(lUserId,lCustomerId);
	  }catch(SQLException sql){
      System.out.println("CustomerService ProposedDAOgetValidationCustomer \nMensaje:" + sql.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",sql.getMessage());             
      return hshData;           
   }catch(RemoteException re){
      System.out.println("CustomerService ProposedDAOgetValidationCustomer \nMensaje:" + re.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",re.getMessage());             
      return hshData;      
   }catch(Exception ex){
      System.out.println("CustomerService ProposedDAOgetValidationCustomer \nMensaje:" + ex.getMessage()+"\n");            
      HashMap hshData=new HashMap();
      hshData.put("strMessage",ex.getMessage());             
      return hshData;    
   }
    
  }
  
  /**
	* <br>Realizado por: <a href="mailto:martin.verae@nextel.com.pe">Martin Vera Espinoza</a>
	* @see pe.com.nextel.dao.SubRegCustomerInfoDAO#getOrderSubReg(long)      
	*/ 
  public HashMap getSubRegOrder(long lOrderId){
    try{
			return getSEJBCustomerRemote().getSubRegOrder(lOrderId);
	  }catch(SQLException sql){
      System.out.println("CustomerService SubRegCustomerInfoDAOgetOrderSubReg \nMensaje:" + sql.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",sql.getMessage());             
      return hshData;           
   }catch(RemoteException re){
      System.out.println("CustomerService SubRegCustomerInfoDAOgetOrderSubReg \nMensaje:" + re.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",re.getMessage());             
      return hshData;      
   }catch(Exception ex){
      System.out.println("CustomerService SubRegCustomerInfoDAOgetOrderSubReg \nMensaje:" + ex.getMessage()+"\n");            
      HashMap hshData=new HashMap();
      hshData.put("strMessage",ex.getMessage());             
      return hshData;    
   }
    
  }
  
  /**
	* <br>Realizado por: <a href="mailto:martin.verae@nextel.com.pe">Martin Vera Espinoza</a>
	* @see pe.com.nextel.dao.SubRegCustomerInfoDAO#getPhoneSubReg(long,long)      
	*/ 
  public HashMap getPhoneSubReg(long lCustomerId,long lIncidentId){
    try{
			return getSEJBCustomerRemote().getPhoneSubReg(lCustomerId,lIncidentId);
	  }catch(SQLException sql){
      System.out.println("CustomerService SubRegCustomerInfoDAOgetPhoneSubReg \nMensaje:" + sql.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",sql.getMessage());             
      return hshData;           
   }catch(RemoteException re){
      System.out.println("CustomerService SubRegCustomerInfoDAOgetPhoneSubReg \nMensaje:" + re.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",re.getMessage());             
      return hshData;      
   }catch(Exception ex){
      System.out.println("CustomerService SubRegCustomerInfoDAOgetPhoneSubReg \nMensaje:" + ex.getMessage()+"\n");            
      HashMap hshData=new HashMap();
      hshData.put("strMessage",ex.getMessage());             
      return hshData;    
   }
    
  }
  
  /**
      Method : validateUbigeo
      Purpose: 
      Developer       		Fecha       Comentario
      =============   		==========  ======================================================================
      DGUTIERREZ     	   21/07/2010  Creación 
       */

   public HashMap validateUbigeo(String state, String province, String city) {        
      HashMap   objHashMap   = new HashMap();
      String    strMessage   = new String();
      
      try{
         objHashMap.put("strMessage",getSEJBCustomerRemote().validateUbigeo(state,province,city));
      
         return objHashMap;
         
      }catch(RemoteException e){
         strMessage  = "[RemoteException][CustomerService][CustomerDAOgetCustomerData][" + e.getClass() + " " + e.getMessage()+"]";
         System.out.println(strMessage);
         objHashMap.put("strMessage",strMessage);
         return objHashMap;
      }catch(Exception e){
         strMessage  = "[Exception][CustomerService][validateUbigeo][" + e.getClass() + " " + e.getMessage()+"]";
         System.out.println(strMessage);
         objHashMap.put("strMessage",strMessage);
         return objHashMap;
      }
    }      
    /**
        Method : getValidateAddress
        Purpose: 
        Developer                 Fecha       Comentario
        =============             ==========  ======================================================================
        FBERNALES                 22/12/2015  Creación 
         */

    public Map<Double,Boolean> getValidateAddress(String sAddress,String sUbigeo, String sAplicacion ){
        HashMap   objHashMap   = new HashMap();
        String    strMessage   = new String();
        try{
            return getSEJBCustomerRemote().getValidateAddress(sAddress, sUbigeo, sAplicacion);
            
        }catch(RemoteException e){
           strMessage  = "[RemoteException][CustomerService][CustomerDAOgetCustomerData][" + e.getClass() + " " + e.getMessage()+"]";
           System.out.println(strMessage);
           objHashMap.put("strMessage",strMessage);
           return objHashMap;
        }catch(Exception e){
           strMessage  = "[Exception][CustomerService][ValidateAddress][" + e.getClass() + " " + e.getMessage()+"]";
           System.out.println(strMessage);
           objHashMap.put("strMessage",strMessage);
           return objHashMap;
        }
    }
    
    public void insLogValidateAddress(String sIdApp,Double dCorrelacion, String sCreatedBy,String sDireccion,Integer lIdCliente, String sNumDoc, Integer lNumOrder){
        String    strMessage = new String();
      try {
          this.getSEJBCustomerRemote().insLogValidateAddress( sIdApp, dCorrelacion, sCreatedBy, sDireccion,lIdCliente, sNumDoc,lNumOrder);
          } catch(RemoteException e){
           strMessage  = "[RemoteException][CustomerService][CustomerDAOgetCustomerData][" + e.getClass() + " " + e.getMessage()+"]";
           System.out.println(strMessage);
          
        }catch(Exception e){
           strMessage  = "[Exception][CustomerService][ValidateAddress][" + e.getClass() + " " + e.getMessage()+"]";
           System.out.println(strMessage);
         
        }  
    }
  
}
