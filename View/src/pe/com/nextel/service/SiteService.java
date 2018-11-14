package pe.com.nextel.service;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.HashMap;

import javax.naming.Context;

import javax.rmi.PortableRemoteObject;

import pe.com.nextel.bean.AddressObjectBean;
import pe.com.nextel.bean.PortalSessionBean;
import pe.com.nextel.ejb.SEJBSiteRemote;
import pe.com.nextel.ejb.SEJBSiteRemoteHome;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;


public class SiteService {
  
  /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - INICIO
     *  REALIZADO POR: Carmen Gremios
     *  FECHA: 01/10/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/

     /**
      * Motivo: Obtiene la instancia del EJB remoto
      * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
      * <br>Fecha: 13/09/2007
      * @return    SEJBSiteRemote
     */    
     public static SEJBSiteRemote getSEJBSiteRemote() {
          try{
              final Context context = MiUtil.getInitialContext();
              final SEJBSiteRemoteHome sEJBSiteRemoteHome = 
                  (SEJBSiteRemoteHome) PortableRemoteObject.narrow( context.lookup( "SEJBSite" ), SEJBSiteRemoteHome.class );
              SEJBSiteRemote sEJBBillingAccountRemote;
              sEJBBillingAccountRemote = sEJBSiteRemoteHome.create();             
              return sEJBBillingAccountRemote;
          }catch(Exception ex) {
              System.out.println("Exception : [SiteService][getSEJBSiteRemote]["+ex.getMessage()+"]");
              return null;
          }
          
      }      

 /**
 * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
 * <br>Fecha: 11/07/2007
 * @see pe.com.nextel.dao.SiteDAO#getSiteData(long)      
 */        
  public HashMap getSiteData(long iSiteid) {
      
      try{
          return getSEJBSiteRemote().getSiteData(iSiteid); 
      }catch(RemoteException re){
           System.out.println("SiteService getSiteData \nMensaje:" + re.getMessage()+"\n");
           HashMap hshData=new HashMap();
           hshData.put("strMessage",re.getMessage());             
           return hshData;  
       }catch(Exception ex){
           System.out.println("SiteService getSiteData \nMensaje:" + ex.getMessage()+"\n");            
           HashMap hshData=new HashMap();
           hshData.put("strMessage",ex.getMessage());             
           return hshData;  
       }
   }
    
  /**
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha: 01/10/2007
     * @see pe.com.nextel.dao.SiteDAO#getSiteData(int,String,int,int,int,String)      
  */        
  public HashMap getSiteDetail(long intCustomerId,long lSiteId) {

      try{
          return getSEJBSiteRemote().getSiteDetail(intCustomerId, lSiteId); 
      }catch(RemoteException re){
           System.out.println("SiteService getSiteDetail Remote\nMensaje:" + re.getMessage()+"\n");
           HashMap hshData=new HashMap();
           hshData.put("strMessage",re.getMessage());             
           return hshData;  
       }catch(Exception ex){
           System.out.println("SiteService getSiteDetail Exception\nMensaje:" + ex.getMessage()+"\n");            
           HashMap hshData=new HashMap();
           hshData.put("strMessage",ex.getMessage());             
           return hshData;    
       }
  }
 
  /**
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha:02/10/2007
   * @see pe.com.nextel.dao.OrderDAO#getAddressList(int,String)      
 */        
   public HashMap getAddressList(String strObjectType,long iObjectId,int iResultado) {     
       try{        
           return getSEJBSiteRemote().getAddressList(strObjectType,iObjectId,iResultado);     
       }catch(RemoteException re){
           System.out.println("SiteService getAddressList \nMensaje:" + re.getMessage()+"\n");
           HashMap hshData=new HashMap();
           hshData.put("strMessage",re.getMessage());             
           return hshData;  
       }catch(Exception ex){
           System.out.println("SiteService getAddressList \nMensaje:" + ex.getMessage()+"\n");
           HashMap hshData=new HashMap();
           hshData.put("strMessage",ex.getMessage());             
           return hshData;  
       }
   }
     
   /**
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha:02/10/2007
     * @see pe.com.nextel.dao.SiteDAO#getCheckMandatoryAddress(long,long)      
   */        
     public HashMap getCheckMandatoryAddress(long lCustomerId,long lSiteId){     
         try{        
             return getSEJBSiteRemote().getCheckMandatoryAddress(lCustomerId,lSiteId);     
         }catch(RemoteException re){
             System.out.println("SiteService getCheckMandatoryAddress RemoteException\nMensaje:" + re.getMessage()+"\n");
             HashMap hshData=new HashMap();
             hshData.put("strMessage",re.getMessage());             
             return hshData;        
         }catch(Exception ex){
             System.out.println("SiteService getCheckMandatoryAddress Exception\nMensaje:" + ex.getMessage()+"\n");
             HashMap hshData=new HashMap();
             hshData.put("strMessage",ex.getMessage());             
             return hshData;   
         }
     }
     
   /**
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha:02/10/2007
     * @see pe.com.nextel.dao.SiteDAO#getSiteType(long,String,String)      
   */        
     public HashMap getSiteType(long lSiteId){              
         try{        
             return getSEJBSiteRemote().getSiteType(lSiteId);                 
         }catch(RemoteException re){
             System.out.println("SiteService getSiteType \nMensaje:" + re.getMessage()+"\n");
             HashMap hshData=new HashMap();
             hshData.put("strMessage",re.getMessage());             
             return hshData;   
         }catch(Exception ex){
             System.out.println("SiteService getSiteType \nMensaje:" + ex.getMessage()+"\n");
             HashMap hshData=new HashMap();
             hshData.put("strMessage",ex.getMessage());             
             return hshData;   
         }
     }
 
    /**
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha:02/10/2007
     * @see pe.com.nextel.dao.SiteDAO#getAddressList(String,long,String,String)      
   */        
     public HashMap getAddressOrContactList(String strObjectType,long lObjectId,String strSearchType){              
         try{        
             return getSEJBSiteRemote().getAddressOrContactList(strObjectType,lObjectId,strSearchType);                 
         }catch(RemoteException re){
             System.out.println("SiteService getAddressOrContactList \nMensaje:" + re.getMessage()+"\n");
             HashMap hshAuxiliar=new HashMap();
             hshAuxiliar.put("strMessage",re.getMessage());        
             return hshAuxiliar;
         }catch(Exception ex){
             System.out.println("SiteService getAddressOrContactList \nMensaje:" + ex.getMessage()+"\n");
             HashMap hshAuxiliar=new HashMap();
             hshAuxiliar.put("strMessage",ex.getMessage());        
             return hshAuxiliar;             
         }
     } 

   /**
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha:02/10/2007
     * @see pe.com.nextel.dao.SiteDAO#doAddrContValidation(long,long,String,String,String)      
   */        
     public String doAddrContValidation(long lCustomerId,long lUnknwnSiteId){     
         try{        
             return getSEJBSiteRemote().doAddrContValidation(lCustomerId,lUnknwnSiteId);     
         }catch(RemoteException re){
             System.out.println("SiteService doAddrContValidation \nMensaje:" + re.getMessage()+"\n");             
             return null;
         }catch(Exception ex){
             System.out.println("SiteService doAddrContValidation \nMensaje:" + ex.getMessage()+"\n");
             return null;
         }
     } 

   /**
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha:12/10/2007
     * @see pe.com.nextel.dao.SiteDAO#getNoChangeAddress(long,long)      
   */        
     public HashMap getNoChangeAddress(long lCustomerId,long lSiteId){     
         try{        
             return getSEJBSiteRemote().getNoChangeAddress(lCustomerId,lSiteId);     
         }catch(RemoteException re){
             System.out.println("SiteService getNoChangeAddress \nMensaje:" + re.getMessage()+"\n");
             HashMap hshData=new HashMap();
             hshData.put("strMessage",re.getMessage());
             return hshData;       
         }catch(Exception ex){
             System.out.println("SiteService getNoChangeAddress \nMensaje:" + ex.getMessage()+"\n");
             HashMap hshData=new HashMap();
             hshData.put("strMessage",ex.getMessage());
             return hshData;    
         }
     }
     
    /**
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha:15/10/2007
     * @see pe.com.nextel.dao.SiteDAO#getTypeContacts(long,long)      
   */        
     public HashMap getTypeContacts(long lCustomerId,long lSiteId){              
       HashMap hshData1=null;
         try{        
             return getSEJBSiteRemote().getTypeContacts(lCustomerId,lSiteId);                 
         }catch(RemoteException re){
             System.out.println("SiteService getTypeContacts \nMensaje:" + re.getMessage()+"\n");             
             HashMap hshData=new HashMap();
             hshData.put("strMessage",re.getMessage());
             return hshData;
         }catch(Exception ex){
             System.out.println("SiteService getTypeContacts \nMensaje:" + ex.getMessage()+"\n");             
             HashMap hshData=new HashMap();
             hshData.put("strMessage",ex.getMessage());
             return hshData;
         }
     }      

   /**
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha:17/10/2007
     * @see pe.com.nextel.dao.SiteDAO#getCheckUniqueContacts(long,long)      
   */        
     public HashMap getCheckUniqueContacts(long lCustomerId,long lSiteId){ 
         
         try{        
             return getSEJBSiteRemote().getCheckUniqueContacts(lCustomerId,lSiteId);                 
         }catch(RemoteException re){
             System.out.println("SiteService getCheckUniqueContacts \nMensaje:" + re.getMessage()+"\n");             
             HashMap hshData=new HashMap();
             hshData.put("strMessage",re.getMessage());
             return hshData;
         }catch(Exception ex){
             System.out.println("SiteService getCheckUniqueContacts \nMensaje:" + ex.getMessage()+"\n");             
             HashMap hshData=new HashMap();
             hshData.put("strMessage",ex.getMessage());
             return hshData;
         }
     }        
     
     
   /**
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha:17/10/2007
     * @see pe.com.nextel.dao.SiteDAO#getCheckMandatoryContacts(long,long)      
   */        
     public HashMap getCheckMandatoryContacts(long lCustomerId,long lSiteId)
     {       
         try{        
             return getSEJBSiteRemote().getCheckMandatoryContacts(lCustomerId,lSiteId);                 
         }catch(RemoteException re){
             System.out.println("SiteService getCheckMandatoryContacts \nMensaje:" + re.getMessage()+"\n");             
             HashMap hshData=new HashMap();
             hshData.put("strMessage",re.getMessage());
             return hshData;
         }catch(Exception ex){
             System.out.println("SiteService getCheckMandatoryContacts \nMensaje:" + ex.getMessage()+"\n");             
             HashMap hshData=new HashMap();
             hshData.put("strMessage",ex.getMessage());             
             return hshData;
         }
     }        

   /**
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha:17/10/2007
     * @see pe.com.nextel.dao.SiteDAO#getCheckNoChangeContacts(long,long)      
   */        
     public HashMap getCheckNoChangeContacts(long lCustomerId,long lSiteId)
     {       
         try{        
             return getSEJBSiteRemote().getCheckNoChangeContacts(lCustomerId,lSiteId);                 
         }catch(RemoteException re){
             System.out.println("SiteService getCheckNoChangeContacts \nMensaje:" + re.getMessage()+"\n");             
             HashMap hshData=new HashMap();
             hshData.put("strMessage",re.getMessage());
             return hshData;
         }catch(Exception ex){
             System.out.println("SiteService getCheckNoChangeContacts \nMensaje:" + ex.getMessage()+"\n");             
             HashMap hshData=new HashMap();
             hshData.put("strMessage",ex.getMessage());             
             return hshData;
         }
     }    
     
     /**
         * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
         * <br>Fecha: 09/10/2007
         * @see pe.com.nextel.dao.SiteDAO#getCheckUniqueAddress(long,long,ArrayList,ArrayList,String)      
     */      
      public HashMap getCheckUniqueAddress(long lCustomerId,long lSiteId ){
          
          try{        
               return getSEJBSiteRemote().getCheckUniqueAddress(lCustomerId, lSiteId);
             
          }catch(RemoteException re){
               System.out.println("SiteService getCheckUniqueAddress \nMensaje:" + re.getMessage()+"\n");               
             HashMap hshData=new HashMap();
             hshData.put("strMessage",re.getMessage());             
             return hshData;
           }catch(Exception ex){
               System.out.println("SiteService getCheckUniqueAddress \nMensaje:" + ex.getMessage()+"\n");                           
             HashMap hshData=new HashMap();
             hshData.put("strMessage",ex.getMessage());             
             return hshData;             
           }
      }  
  
 /**
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha: 09/10/2007
     * @see pe.com.nextel.dao.SiteDAO#getTypeAddresses(long,long)      
 */      
  public HashMap getTypeAddresses(long lCustomerId,long lSiteId){
    try{        
      return getSEJBSiteRemote().getTypeAddresses(lCustomerId, lSiteId);
    }catch(RemoteException re){
      System.out.println("SiteService getTypeAddresses \nMensaje:" + re.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",re.getMessage()); 
      return hshData;   
    }catch(Exception ex){
      System.out.println("SiteService getTypeAddresses \nMensaje:" + ex.getMessage()+"\n");            
      HashMap hshData=new HashMap();
      hshData.put("strMessage",ex.getMessage()); 
      return hshData;     
    }
  }  

     /**
       * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
       * <br>Fecha:22/10/2007
       * @see pe.com.nextel.dao.SiteDAO#validateRegionDelivAddr(AddressObjectBean)      
     */        
       public HashMap validateRegionDelivAddr(AddressObjectBean objAddressBean){     
           try{        
               return getSEJBSiteRemote().validateRegionDelivAddr(objAddressBean);     
           }catch(RemoteException re){
               System.out.println("SiteService validateRegionDelivAddr \nMensaje:" + re.getMessage()+"\n");             
               HashMap hshData=new HashMap();
               hshData.put("strMessage",re.getMessage());             
               return hshData;
           }catch(Exception ex){        
               System.out.println("SiteService validateRegionDelivAddr \nMensaje:" + ex.getMessage()+"\n");
               HashMap hshData=new HashMap();
               hshData.put("strMessage",ex.getMessage());             
               return hshData;
           }
       }      


    
/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* @see pe.com.nextel.dao.SiteDAO#insSites(SiteBean,String,int,String)      
*/     
public HashMap insSites(RequestHashMap request,PortalSessionBean objPortalSesBean) throws Exception{           
   return getSEJBSiteRemote().insSites(request,objPortalSesBean);   
}      

/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* @see pe.com.nextel.dao.SiteDAO#insAddress(AddressObjectBean,String,String)      
*/     
public HashMap insAddress(RequestHashMap request,PortalSessionBean objPortalSesBean)throws Exception{             
   return getSEJBSiteRemote().insAddress(request,objPortalSesBean);  
} 

/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha:22/10/2007
* @see pe.com.nextel.dao.SiteDAO#updAddress(AddressObjectBean,int,String,int)      
*/        
public HashMap updAddress( RequestHashMap request,PortalSessionBean objPortalSesBean)throws Exception{       
   return getSEJBSiteRemote().updAddress(request,objPortalSesBean);     
} 

/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha:11/10/2007
* @see pe.com.nextel.dao.SiteDAO#delAddress(long,long,String,String,long,long,String)      
*/        
public HashMap delAddress(RequestHashMap request,PortalSessionBean objPortalSesBean)throws Exception{                  
   return getSEJBSiteRemote().delAddress(request,objPortalSesBean);
}  

/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha:10/10/2007
* @see pe.com.nextel.dao.SiteDAO#updSites(SiteBean,String,int,String)      
*/        
public HashMap updSites(RequestHashMap request,PortalSessionBean objPortalSesBean)throws Exception{            
   return getSEJBSiteRemote().updSites(request,objPortalSesBean);  
}  

/**
* <br>Realizado por: <a href="mailto:hugo.moreno@nextel.com.pe">Hugo Moreno</a>
* <br>Fecha:03/04/2008
* @see pe.com.nextel.dao.SiteDAO#delSitesAssign(long,long,String)      
*/        
public HashMap delSitesAssign(RequestHashMap request)throws Exception{         
   return getSEJBSiteRemote().delSitesAssign(request);     
}  

/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha:10/10/2007
* @see pe.com.nextel.dao.SiteDAO#delSites(long,String)      
*/        
public HashMap delSites(RequestHashMap request)throws Exception{         
   return getSEJBSiteRemote().delSites(request);     
}  

/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha:18/10/2007
* @see pe.com.nextel.dao.SiteDAO#insContact(AddressObjectBean,String,String)      
*/     
public HashMap insContact(RequestHashMap request,PortalSessionBean objPortalSesBean)throws Exception{    
   return getSEJBSiteRemote().insContact(request,objPortalSesBean);
} 

/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha:23/10/2007
* @see pe.com.nextel.dao.SiteDAO#updContact(ContactObjectBean,int,String,int)      
*/        
public HashMap updContact(RequestHashMap request,PortalSessionBean objPortalSesBean)throws Exception{        
   return getSEJBSiteRemote().updContact(request,objPortalSesBean);     
}

/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha:18/10/2007
* @see pe.com.nextel.dao.SiteDAO#delContact(ContactObjectBean)      
*/        
public HashMap delContact(RequestHashMap request,PortalSessionBean objPortalSesBean)throws Exception{    
   return getSEJBSiteRemote().delContact(request,objPortalSesBean);     
} 
 /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - FIN
     *  REALIZADO POR: Carmen Gremios
     *  FECHA: 01/10/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/ 
     
     /**
       * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
       * <br>Fecha:09/12/2007
       * @see pe.com.nextel.dao.SiteDAO#getLongMaxSite(String namesite)      
     */        
    
      public HashMap getLongMaxSite() throws Exception{                  
      return getSEJBSiteRemote().getLongMaxSite();
      }  

   /**
       * <br>Realizado por: <a href="mailto:hugo.moreno@nextel.com.pe">Hugo Moreno</a>
       * <br>Fecha: 20/05/2008
       * @see pe.com.nextel.dao.SiteDAO
    */
    public int getSourceSite(long lSiteId) throws Exception{
    
      try {
         return getSEJBSiteRemote().getSourceSite(lSiteId);        
      }  catch (RemoteException ex) {             
            System.out.println("[RemoteException][GeneralService][getValue][" + ex.getMessage() + "]["+ex.getClass()+"]");                     
            return -1;
      } catch (SQLException ex) {
            System.out.println("[SQLException][GeneralService][getValue][" + ex.getMessage() + "]["+ex.getClass()+"]");                     
            return -1;
      } catch (Exception ex) {
            System.out.println("[Exception][GeneralService][getValue][" + ex.getMessage() + "]["+ex.getClass()+"]");                     
            return -1;
      }	  
    }
    
     /**
     * <br/>Realizado por: <a href="mailto:jorge.perez@nextel.com.pe">Jorge Pérez</a>
     * <br/>Fecha: 15/05/2009
     *    
  */        
  public HashMap getSiteSolutionGroup(long lSiteId) {

      try{
          return getSEJBSiteRemote().getSiteSolutionGroup(lSiteId); 
      }catch(RemoteException re){
           System.out.println("SiteService getSiteSolutionGroup Remote\nMensaje:" + re.getMessage()+"\n");
           HashMap hshData=new HashMap();
           hshData.put("strMessage", re.getMessage());
           return hshData;  
       }catch(Exception ex){
           System.out.println("SiteService getSiteSolutionGroup Exception\nMensaje:" + ex.getMessage()+"\n");            
           HashMap hshData=new HashMap();
           hshData.put("strMessage", ex.getMessage());
           return hshData;    
       }
  }
  
     /**
     * <br/>Realizado por: <a href="mailto:jorge.perez@nextel.com.pe">Jorge Pérez</a>
     * <br/>Fecha: 10/07/2009
     *    
  */        
  public HashMap getSpecSolutionGroup(long lSpecificationId) {

      try{
          return getSEJBSiteRemote().getSpecSolutionGroup(lSpecificationId);
      }catch(RemoteException re){
           System.out.println("SiteService getSpecSolutionGroup Remote\nMensaje:" + re.getMessage()+"\n");
           HashMap hshData=new HashMap();
           hshData.put("strMessage",re.getMessage());             
           return hshData;  
       }catch(Exception ex){
           System.out.println("SiteService getSpecSolutionGroup Exception\nMensaje:" + ex.getMessage()+"\n");            
           HashMap hshData=new HashMap();
           hshData.put("strMessage",ex.getMessage());             
           return hshData;    
       }
  }

    /*PBI000000042016*/
    public Long getUnknownSite(long lSiteId){
        try{
            return getSEJBSiteRemote().getUnknownSite(lSiteId);
        }catch(RemoteException re){
            System.out.println("SiteService getUnknownSite Remote\nMensaje:" + re.getMessage()+"\n");
            HashMap hshData=new HashMap();
            hshData.put("strMessage",re.getMessage());
            return null;
        }catch(Exception ex){
            System.out.println("SiteService getUnknownSite Exception\nMensaje:" + ex.getMessage()+"\n");
            HashMap hshData=new HashMap();
            hshData.put("strMessage",ex.getMessage());
            return null;
        }
    }
    
}