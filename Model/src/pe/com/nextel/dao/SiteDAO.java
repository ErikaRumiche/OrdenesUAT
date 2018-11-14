package pe.com.nextel.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import pe.com.nextel.bean.AddressObjectBean;
import pe.com.nextel.bean.ContactObjectBean;
import pe.com.nextel.bean.SiteBean;
import pe.com.nextel.bean.SolutionGroupBean;


//import oracle.jdbc.OracleCallableStatement;



public class SiteDAO extends GenericDAO{

   /**
   Method : Obtiene los sites solicitados en el CRM de un determinado cliente
   Purpose: Inserta una nueva orden.
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Lee Rosales     21/09/2007  Creación
   */

  public HashMap getSiteSolicitedList(long longNpOrderId) throws Exception,SQLException{

    ArrayList list = new ArrayList();
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    Connection conn=null; 
    HashMap objHashMap = new HashMap();       
    SiteBean objSiteBean = null;
    String strMessage = null;    
    String sqlStr = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_SITE_SOLICITD(?, ?, ?); END;";
    try{
       conn = Proveedor.getConnection();
       cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
       cstmt.setLong(1, longNpOrderId);
       cstmt.registerOutParameter(2, OracleTypes.CURSOR);
       cstmt.registerOutParameter(3, Types.VARCHAR);
       cstmt.executeUpdate();
       strMessage = cstmt.getString(3);    
       if( strMessage == null ){
          rs = (ResultSet)cstmt.getObject(2);
          while (rs.next()) {
             objSiteBean = new SiteBean();
             objSiteBean.setSwSiteId(rs.getLong("swsiteid"));
             objSiteBean.setSwSiteName(rs.getString("RESP_PAGO"));
             objSiteBean.setSwRegionName(rs.getString("REGION"));
             objSiteBean.setNpStatus(rs.getString("ESTADO"));
             list.add(objSiteBean);
          }
        }                    
    }
    catch (Exception e) {
         throw new Exception(e);
    }
    finally{ 
      closeObjectsDatabase(conn,cstmt,rs);
    }    
    objHashMap.put("strMessage",strMessage);
    objHashMap.put("objArrayList",list);
    return objHashMap;
      
  }
    
  /**
   Method : getSiteExistsList
   Purpose: Obtiene los sites existentenes en BSCS de un determinado cliente
   Developer       	  Fecha       Comentario
   =============   	  ==========  ======================================================================
   Lee Rosales     	  21/09/2007  Creación
   Cristian Espinoza	28/03/2008	Devolver los sites existentes, según correspondan al CustomerId o al SiteId
   */
    
  public HashMap getSiteExistsList(long longNpCustomerId,String strObjectType) throws Exception,SQLException {

    ArrayList list = new ArrayList();
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    Connection conn=null; 
    SiteBean objSiteBean = null;
    String strMessage = null;
    HashMap objHashMap = new HashMap();
    String sqlStr = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_SITE_EXISTS(?, ?, ?,?); END;";
    try{
       conn = Proveedor.getConnection();
       cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
       System.out.println("longNpCustomerId: "+longNpCustomerId+"");
       System.out.println("strObjectType: "+strObjectType);
       cstmt.setLong(1, longNpCustomerId);
       cstmt.setString(2, strObjectType);
       cstmt.registerOutParameter(3, OracleTypes.CURSOR);
       cstmt.registerOutParameter(4, Types.VARCHAR);   
       cstmt.executeUpdate();   
       strMessage = cstmt.getString(4);       
       if( strMessage == null ){
          rs = (ResultSet)cstmt.getObject(3);
          while (rs.next()) {
             objSiteBean = new SiteBean();
             objSiteBean.setSwSiteId(rs.getLong("swsiteid"));
             objSiteBean.setSwSiteName(rs.getString("RESP_PAGO"));
             objSiteBean.setSwRegionName(rs.getString("REGION"));
             objSiteBean.setNpStatus(rs.getString("ESTADO"));
             list.add(objSiteBean);
          }       
       }
    }
    catch (Exception e) {
         throw new Exception(e);
    }
    finally{
       closeObjectsDatabase(conn,cstmt,rs);
    }     
    objHashMap.put("strMessage",strMessage);
    objHashMap.put("objArrayList",list);  
    return objHashMap;  
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
* Motivo: Obtiene los datos del Site.
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 27/07/2007
* @param     iSiteid  
* @param     strCreatedby        
* @param     iUserid    
* @param     iAppid    
* @param     iRegionid    
* @return    HashMap 
*/
public HashMap getSiteData(long iSiteid) throws SQLException, Exception{
   
   HashMap hshData=new HashMap();
   String strMessage=null;
   SiteBean objSite=new SiteBean();        
   ResultSet rs = null;
   Connection conn = null;
   OracleCallableStatement cstmt = null;   
   String sqlStr = "BEGIN WEBSALES.SPI_GET_SITE_DATA3( ?, ?, ?); END;";           
   try{
      conn = Proveedor.getConnection();   
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);   
      cstmt.setLong(1, iSiteid);      
      cstmt.registerOutParameter(2, OracleTypes.CURSOR);
      cstmt.registerOutParameter(3, Types.VARCHAR);
      cstmt.execute();   
      strMessage = cstmt.getString(3);   
      rs = (ResultSet)cstmt.getObject(2);      
      if (rs.next()) {
         objSite.setNpCodBscs(rs.getString("npcodbscs"));
         objSite.setSwRegionId(rs.getLong("swregionid"));
         objSite.setNpLineaCredito(rs.getLong("nplineacredito"));
         objSite.setSwRegionName(rs.getString("regionname"));   
         objSite.setSwCreatedBy(rs.getString("swcreatedby"));
         objSite.setSwSiteName(rs.getString("swsitename"));
         objSite.setSwcustomerid(rs.getInt("swcustomerid"));
      }
   }
   catch (Exception e) {
      throw new Exception(e);
   }
   finally{
      closeObjectsDatabase(conn,cstmt,rs);
   }
   hshData.put("strMessage",strMessage);
   hshData.put("objSite",objSite);
   
   return hshData;
}
    
/**
* Motivo: Obtener la lista de Sites del cliente (Responsables de pago)
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 27/08/2007
* @param     intCustomerId  
* @param     strEstadoSite   
* @return    HashMap 
*/
public HashMap getCustomerSites(long intCustomerId,String strEstadoSite) throws SQLException, Exception{
   HashMap hshDta=new HashMap();
   String strMessage=null;
   ArrayList list = new ArrayList();        
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   Connection conn=null;
   HashMap h=null;
   String sqlStr = "BEGIN WEBSALES.SPI_GET_SITE_LIST2(?, ?, ?, ?); END;";
   try{
      conn = Proveedor.getConnection();
      
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, intCustomerId);
      cstmt.setString(2, strEstadoSite);
      cstmt.registerOutParameter(3, Types.CHAR);
      cstmt.registerOutParameter(4, OracleTypes.CURSOR);
      
      cstmt.execute();
      strMessage = cstmt.getString(3);
      //rs = (ResultSet)cstmt.getObject(4);
      rs = (ResultSet)cstmt.getObject(4);
      
      while (rs.next()) {
         h= new HashMap();
         h.put("swsiteid",  rs.getLong(1) + "");
         h.put("swsitename",rs.getString(2));
         h.put("npcodbscs", rs.getString(3));                
         h.put("regionid", rs.getLong(4)+"");       
         h.put("regionname", rs.getString(5));                
         h.put("status", rs.getString(6));  
         h.put("createdby", rs.getString(7));                
         h.put("lineacredito", rs.getLong(8)+"");                
         list.add(h);
      }
   }
   catch (Exception e) {
      throw new Exception(e);
   }
   finally{
      closeObjectsDatabase(conn,cstmt,rs);
   }   
   hshDta.put("arrCustomerSite",list);                     
   hshDta.put("strMessage",strMessage);                      
   
   return hshDta;
}  
/**
* Motivo: Inserta un Site
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 01/10/2007
* @param     objSiteBean  
* @param     lOrderId
* @param     strLogin   
* @param     iAppId    
* @return    HashMap 
*/
public HashMap insSites(SiteBean objSiteBean,long lOrderId,String strLogin,int iAppId,Connection conn) throws Exception,SQLException {
  HashMap hshData=new HashMap();
  OracleCallableStatement cstmt = null;
  try{
    
    long lSiteId=0;
    

    String strMessage=null; 
    String sqlStr = "BEGIN WEBSALES.SPI_INSERT_UNKNOWN_SITE(?, ?, ?, ?, " + 
                                                     "?, ?, ?, ?, " +
                                                     "?, ?, ?, ?, ?, ?); END;";

    cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
    cstmt.setLong(1, objSiteBean.getSwcustomerid());               
    cstmt.setNull(2,Types.INTEGER); 
    if (lOrderId==0)
      cstmt.setNull(3,Types.INTEGER);
    else
      cstmt.setLong(3, lOrderId); 
    cstmt.setString(4, objSiteBean.getSwSiteName());
    cstmt.setLong(5,objSiteBean.getSwRegionId());
    cstmt.setString(6,objSiteBean.getSwofficephone());
    cstmt.setString(7,objSiteBean.getSwofficephonearea());
    cstmt.setString(8,objSiteBean.getSwfax());
    cstmt.setString(9,objSiteBean.getSwfaxarea());
    cstmt.setString(10,strLogin);
    cstmt.setInt(11,iAppId);
    cstmt.setLong(12, objSiteBean.getNpsolutiongroupid());
  
    cstmt.registerOutParameter(13, OracleTypes.NUMERIC);
    cstmt.registerOutParameter(14, Types.VARCHAR);          
   
    int i=cstmt.executeUpdate();  
    strMessage = cstmt.getString(14);
    lSiteId = cstmt.getLong(13);            
   
    System.out.println("COdigo del Site DAO -->"+lSiteId + " cod de transaccion->"+ i);    
    System.out.println("Mensaj de Error -->"+strMessage);    
   
    hshData.put("strMessage",strMessage);
    hshData.put("lSiteId",lSiteId+"");
  
  }catch(Exception ex){
    throw new Exception(ex);
  }finally{
    closeObjectsDatabase(null,cstmt,null);
  }
  
  return hshData;
}
       
/**
* Motivo: Actualiza un Site
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 10/10/2007
* @param     objSiteBean  
* @param     strLogin   
* @param     iAppId    
* @return    HashMap 
*/
public HashMap updSites(SiteBean objSiteBean,String strLogin,int iAppId,Connection conn) throws Exception,SQLException {
   HashMap hshData=new HashMap();
   String strMessage=null;
   OracleCallableStatement cstmt = null;
   
   try{
   //Connection conn=null;   
   String sqlStr = "BEGIN WEBSALES.SPI_UPDATE_UNKNOWN_SITE(?, ?, ?, ?, ?," + 
                                                "?, ?, ?, ?, ? ); END;";
   
   //conn = Proveedor.getConnection();
   cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
   cstmt.setLong(1, objSiteBean.getSwSiteId());                    
   cstmt.setString(2, objSiteBean.getSwSiteName());
   cstmt.setLong(3,objSiteBean.getSwRegionId());               
   cstmt.setString(4,objSiteBean.getSwofficephone());
   cstmt.setString(5,objSiteBean.getSwfax());
   cstmt.setString(6,objSiteBean.getSwofficephonearea());               
   cstmt.setString(7,objSiteBean.getSwfaxarea());
   cstmt.setInt(8,iAppId);
   cstmt.setString(9,strLogin);                              
   cstmt.registerOutParameter(10, Types.VARCHAR);               
  
   int i=cstmt.executeUpdate();
   strMessage = cstmt.getString(10);               
   
   System.out.println("Mensaj de Error Actualizacion -->"+strMessage);                 
         
   hshData.put("strMessage",strMessage);
   hshData.put("iReturn",i+"");
   
   }catch(Exception ex){
     throw new Exception(ex);
   }finally{
     closeObjectsDatabase(null,cstmt,null);
   }
   return hshData;
}
       
/**
* Motivo: Elimina un Site
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 10/10/2007
* @param     lUnkwSiteId  
* @return    HashMap 
*/
public HashMap delSites(long lUnkwSiteId,Connection conn) throws Exception,SQLException {

   String  strMessage=null;
   HashMap hshData =new HashMap();
   OracleCallableStatement cstmt = null;
   int i = 0;
   try{
   
   System.out.println("********************DAO lUnkwSiteId: "+lUnkwSiteId);
   
   String sqlStr = "BEGIN WEBSALES.SPI_DELETE_UNKNWN_SITE(?, ? ); END;";
   
   cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
   cstmt.setLong(1, lUnkwSiteId);                   
   cstmt.registerOutParameter(2, Types.VARCHAR);               
   
   i  = cstmt.executeUpdate();
   strMessage = cstmt.getString(2);               
   
   System.out.println("Mensaj de Eliminacion  -->"+strMessage);    
   
   }catch(Exception ex){
     throw new Exception(ex);
   }finally{
     closeObjectsDatabase(null,cstmt,null);
   }
   
   hshData.put("strMessage",strMessage);
   hshData.put("iReturn",i+"");
   
   return hshData;
}      
       
       
/**
* Motivo: Elimina un Site en la orders.np_co_assignment
* <br>Realizado por: <a href="mailto:hugo.moreno@nextel.com.pe">Hugo Moreno</a>
* <br>Fecha: 03/04/2008
* @param     lNewBillAccId  
* @param     lOrderId  
* @return    HashMap 
*/
public HashMap delSitesAssign(long lUnkwSiteId,long lOrderId,Connection conn) 
throws Exception,SQLException {
   String  strMessage=null;
   HashMap hshData =new HashMap();
   OracleCallableStatement cstmt = null;
   int i = 0;
   try{
   //Connection conn=null;
   System.out.println("-----------------DAO lNewBillAccId: "+lUnkwSiteId);
   System.out.println("-----------------DAO lOrderId: "+lOrderId);
   String sqlStr = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_DEL_SITE_BA(?,?,?); END;";
   
   //conn = Proveedor.getConnection();
   cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);   
   cstmt.setLong(1, lUnkwSiteId);                   
   cstmt.setLong(2, lOrderId);                   
   cstmt.registerOutParameter(3, Types.VARCHAR);               
   
   i=cstmt.executeUpdate();
   strMessage = cstmt.getString(3);               
   
   System.out.println("Mensaj de Eliminacion  -->"+strMessage);    
   
   }catch(Exception ex){  
     throw new Exception(ex);
   }finally{
     closeObjectsDatabase(null,cstmt,null);
   }
   hshData.put("strMessage",strMessage);
   hshData.put("iReturn",i+"");
   
   return hshData;
}             
       
/**
* Motivo: Obtener la lista de Sites del cliente (Responsables de pago)
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 01/10/2007
* @param     intCustomerId  
* @param     lSiteId   
* @return    HashMap 
*/
public HashMap getSiteDetail(long lCustomerId,long lSiteId) throws SQLException, Exception{       
   String strMessage=null;
   HashMap hshData=new HashMap();
   HashMap h = new HashMap();
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   Connection conn=null;
   SiteBean objSiteBean =new SiteBean();
   String sqlStr = "BEGIN WEBSALES.SPI_GET_SITE_DETAIL(?, ?, ?, ?); END;";
   try{
      conn = Proveedor.getConnection();   
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, lCustomerId);
      if (lSiteId==0)
         cstmt.setNull(2,Types.INTEGER);
      else   
         cstmt.setLong(2, lSiteId);
      cstmt.registerOutParameter(3, OracleTypes.CURSOR);
      cstmt.registerOutParameter(4, Types.CHAR);               
      
      cstmt.execute();
      strMessage = cstmt.getString(4);
      rs = (ResultSet)cstmt.getObject(3);
      
      if (rs.next()) {
   
         h.put("swsiteid",  rs.getLong(1) + "");
         h.put("swsitename",rs.getString(2));
         h.put("npcodbscs", rs.getString(3));                     
         h.put("npganadop", rs.getString(4));                
         h.put("swofficephonearea", rs.getString(5));                
         h.put("swofficephone", rs.getString(6));                
         h.put("swfaxarea", rs.getString(7));                
         h.put("swfax", rs.getString(8));                
         
         h.put("nppartnercodbscs", rs.getString(9));       
         h.put("swregionid", rs.getLong(10)+"");                
         h.put("region", rs.getString(11));  
         h.put("swtype", rs.getString(12));                
         h.put("npdivisionid", rs.getInt(13)+"");                
         h.put("npname", rs.getString(14));                
      }
   }
   catch (Exception e) {
      throw new Exception(e);
   }
   finally{
      closeObjectsDatabase(conn,cstmt,rs);
   }         
   hshData.put("hshData",h);      
   hshData.put("strMessage",strMessage);      
      
   return hshData;
}  
       
/**
* Motivo: Obtener la lista de direcciones del UnkwonSite (Nuevo Responsables de pago)
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 02/10/2007
* @param     strObjectType  
* @param     iObjectId   
* @param     iResultado   
* @return    ArrayList 
*/
public HashMap getAddressList(String strObjectType,long lObjectId,int iResultado) throws SQLException, Exception{
   String strMessage=null;
   HashMap hshData=new HashMap();
   ArrayList list = new ArrayList();        
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   Connection conn=null;
   HashMap h=null;
   String sqlStr = "BEGIN WEBSALES.SPI_GET_ADDRESS_LIST(?, ?, ?, ?, ?); END;";
   try{
      conn = Proveedor.getConnection();      
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setString(1, strObjectType);
      cstmt.setLong(2, lObjectId);               
      cstmt.registerOutParameter(3, OracleTypes.CURSOR);               
      cstmt.registerOutParameter(4, Types.VARCHAR);
      cstmt.registerOutParameter(5, Types.INTEGER);      
      cstmt.execute();
      strMessage = cstmt.getString(4);
      rs = (ResultSet)cstmt.getObject(3);      
      while (rs.next()) {
         h = new HashMap();
         h.put("swaddressid",  rs.getLong(1) + "");
         h.put("swaddress1",rs.getString(2));
         h.put("swaddress2", rs.getString(3));                
         h.put("swaddress3", rs.getString(4));                      
         h.put("swzip", rs.getString(5));  
         h.put("departamento", rs.getString(6));                
         h.put("provincia", rs.getString(7));                
         h.put("distrito", rs.getString(8));                
         h.put("swregionid", rs.getLong(9)+"");                
         h.put("swnote", rs.getString(10));                
         h.put("npdepartamentoid", rs.getLong(11)+"");                
         h.put("npprovinciaid", rs.getLong(12)+"");                
         h.put("npdistritoid", rs.getLong(13)+"");                
         h.put("tiposDireccion", rs.getString(14));                
         h.put("ORDEN", rs.getInt(15)+"");                               
         h.put("flagAddress", rs.getInt(16)+"");  // NBRAVO e
         list.add(h);
      }
   }
   catch (Exception e) {
      throw new Exception(e);
   }
   finally{
      closeObjectsDatabase(conn,cstmt,rs);
   }      
   hshData.put("arrAddress",list);            
   hshData.put("strMessage",strMessage);      
   
   return hshData;
}
      
/**
* Motivo: Obtiene los contactos obligatorios(arreglo) y su flag correspondiente(arreglo) que inidica si tiene o tiene el contacto creado.
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 02/10/2007
* @param     lCustomerId  
* @param     lSiteId   
* @param     HashMap           
*/
public HashMap getCheckMandatoryAddress(long lCustomerId,long lSiteId) throws SQLException, Exception{       

   OracleCallableStatement cstmt = null;         
   Connection conn=null;
   ResultSet rs1 = null;
   ResultSet rs2 = null;           
   String strMessage=null;
   HashMap h=null;
   HashMap hshResultado=new HashMap();
   ArrayList aAddrType=new ArrayList();
   ArrayList aAddrTypeFlg=new ArrayList();
   String sqlStr = "BEGIN WEBSALES.SPI_CHECK_MANDATORY_ADDRESSES2(?, ?, ?, ?, ?); END;";   
   try{
      conn = Proveedor.getConnection();   
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, lCustomerId);                      
      cstmt.setLong(2, lSiteId);                                 
      cstmt.registerOutParameter(3, OracleTypes.CURSOR);
      cstmt.registerOutParameter(4, OracleTypes.CURSOR);           
      cstmt.registerOutParameter(5, Types.VARCHAR);
      
      cstmt.execute();
      rs1 = (ResultSet)cstmt.getObject(3);
      rs2 = (ResultSet)cstmt.getObject(4);
      strMessage = cstmt.getString(5);              
      System.out.println("SITEDao/getCheckMandatoryAddress-->"+strMessage+" lCustomerId->"+lCustomerId+" lSiteId-->"+lSiteId);
      while (rs1.next()) {
         h = new HashMap();
         h.put("addstype",  rs1.getString(1));                                           
         aAddrType.add(h);
      }
      
      while (rs2.next()) {
         h = new HashMap();
         h.put("addrtypeflag",  rs2.getString(1));                                       
         aAddrTypeFlg.add(h);
      }
   }
   catch (Exception e) {
      throw new Exception(e);
   }
   finally{
      closeObjectsDatabase(conn,cstmt,rs1);
      closeObjectsDatabase(null,null,rs1);
   }   
   hshResultado.put("objAddrType",aAddrType);                  
   hshResultado.put("objAddrTypeFlg",aAddrTypeFlg);                  
   hshResultado.put("strMessage",strMessage);             
   
   return hshResultado;
}
      
/**
* Motivo: Obtiene un listado de  direcciones (strSearchType='TIPODIRECCION') o conatctos (strSearchType='TIPOCONTACTO') 
* de un site (strObjectType='SITE') o cliente (strObjectType='CUSTOMER') 
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 02/10/2007
* @param     strObjectType  
* @param     lObjectId   
* @param     strSearchType   Ej: TIPODIRECCION       
* @return    HashMap 
*/
public HashMap getAddressOrContactList(String strObjectType,long lObjectId,String strSearchType) 
throws SQLException, Exception{       
   HashMap hshResultado =new HashMap();
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   ArrayList list = new ArrayList();  
   Connection conn=null;
   HashMap h=null;
   String sqlStr = "BEGIN WEBSALES.SPI_GET_ADDRESS_LIST2(?, ?, ?, ?, ?); END;";
   try{
      String strMessage=null;
      conn = Proveedor.getConnection();
      
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setString(1, strObjectType);                      
      cstmt.setLong(2, lObjectId);            
      cstmt.setString(3, strSearchType);              
      cstmt.registerOutParameter(4, OracleTypes.CURSOR);
      //cstmt.registerOutParameter(5, Types.INTEGER);     
      cstmt.registerOutParameter(5, Types.VARCHAR);                      
      
      cstmt.execute();            
      rs=(ResultSet)cstmt.getObject(4);                                
      strMessage = cstmt.getString(5);              
      if ("TIPODIRECCION".equals(strSearchType)){
         while (rs.next()) {
            h = new HashMap();
            h.put("swaddressid",  rs.getLong(1) + "");
            h.put("swaddress1",rs.getString(2));
            h.put("swaddress2", rs.getString(3));                
            h.put("swaddress3", rs.getString(4));                      
            h.put("swzip", rs.getString(5));  //
            h.put("departamento", rs.getString(6));                
            h.put("provincia", rs.getString(7));                
            h.put("distrito", rs.getString(8));                
            h.put("swregionid", rs.getLong(9)+"");                
            h.put("swnote", rs.getString(10));                
            h.put("npdepartamentoid", rs.getLong(11)+"");                
            h.put("npprovinciaid", rs.getLong(12)+"");                
            h.put("npdistritoid", rs.getLong(13)+"");                
            h.put("domicilio", rs.getString(14));                
            h.put("facturacion", rs.getString(15));                
            h.put("entrega", rs.getString(16));              
            h.put("comunicacion", rs.getString(17));         
            h.put("otra", rs.getString(18));                
            h.put("instalacion", rs.getString(19));         
            h.put("correspondencia", rs.getString(20));                                        
            h.put("flagAddress", rs.getLong(21)+"");
            list.add(h);
         }
      }else{   
      
         while (rs.next()) {
            h = new HashMap();
            h.put("swcustomerid",  rs.getLong("swcustomerid") + "");
            h.put("swpersonid",rs.getLong("swpersonid")+"");
            h.put("swlastname", rs.getString("swlastname"));                
            h.put("swfirstname", rs.getString("swfirstname"));                      
            h.put("swmiddlename", rs.getString("swmiddlename"));  //
            h.put("swtitle", rs.getString("swtitle"));                
            h.put("mail", rs.getString("mail"));                                 
            h.put("phonearea", rs.getString("phonearea"));                
            h.put("phone", rs.getString("phone"));                
            h.put("faxarea", rs.getString("faxarea"));                
            h.put("fax", rs.getString("fax"));                
            h.put("anexo", rs.getString("anexo"));                
            h.put("swjobtitleid", rs.getLong("swjobtitleid")+"");                
            h.put("swdescription", rs.getString("swdescription"));                
            h.put("GerentG", rs.getString("GerentG"));              
            h.put("Usuario", rs.getString("Usuario"));         
            h.put("Facturacion", rs.getString("Facturacion"));                
            h.put("Ventas", rs.getString("Ventas"));         
            h.put("Sistemas", rs.getString("Sistemas"));                                        
            h.put("Comunicaciones", rs.getString("Comunicaciones"));                                        
            h.put("Data", rs.getString("Data"));                                                                                                         
            h.put("Cobranza", rs.getString("Cobranza"));                       
            
            list.add(h);
         }          
      
      }
      hshResultado.put("objArrayList",list);
      hshResultado.put("strMessage",strMessage);
   }
   catch (Exception e) {
      throw new Exception(e);
   }
   finally{
      closeObjectsDatabase(conn,cstmt,rs);
   }   
   return hshResultado;
}  
  
         
/**
* Motivo: Retorna 'Customer' o 'Prospect'. 
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 02/10/2007
* @param     lSiteId   
* @param     strSiteType    
* @param     HashMap           
*/
public HashMap getSiteType(long lSiteId) throws SQLException, Exception{
   String strMessage=null;
   HashMap hshData=new HashMap();
   String strSiteType=null;    
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   Connection conn=null;
   
   String sqlStr = "BEGIN WEBSALES.SPI_GET_SITE_TYPE(?, ?, ?); END;";
   try{
      conn = Proveedor.getConnection();
      
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);                                 
      cstmt.setLong(1, lSiteId);                                 
      cstmt.registerOutParameter(2, Types.VARCHAR);
      cstmt.registerOutParameter(3, Types.VARCHAR);                      
      
      cstmt.execute();
      strSiteType = cstmt.getString(2);           
      strMessage = cstmt.getString(3);              
   }
   catch (Exception e) {
      throw new Exception(e);
   }
   finally{
      closeObjectsDatabase(conn,cstmt,rs);
   }   
   hshData.put("strMessage",strMessage);
   hshData.put("strSiteType",strSiteType);
   return hshData;
} 
      
/**
* Motivo: Valida las direcciones y contactos obligatorios.
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 04/10/2007
* @param     lCustomerId   
* @param     lUnknwnSiteId    
* @param     strMessage    
* @return    String
*/
public String doAddrContValidation(long lCustomerId,long lUnknwnSiteId) 
throws SQLException, Exception{

   String strSiteType=null;    
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   Connection conn=null;   
   String strMessage=null;
   
   String sqlStr = "BEGIN WEBSALES.SPI_ADDR_CONT_VALIDATION(?, ?, ?); END;";
   try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);                                 
      cstmt.setLong(1, lCustomerId);                                 
      cstmt.setLong(2, lUnknwnSiteId);                                            
      cstmt.registerOutParameter(3, Types.VARCHAR);                      
      
      cstmt.execute();           
      strMessage = cstmt.getString(3);              
      System.out.println("strMessage1: "+strMessage);
   }
   catch (Exception e) {
      throw new Exception(e);
   }
   finally{
      closeObjectsDatabase(conn,cstmt,rs);
   }   
   return strMessage;
} 

/**
* Motivo: Obtiene todos los tipos de direcciones existentes. 
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 09/10/2007
* @param     lCustomerId    
* @param     lSiteId
* @return    HashMap
*/     
public  HashMap getTypeAddresses(long lCustomerId,long lSiteId)   
throws SQLException, Exception{
ArrayList list = new ArrayList();

   Connection conn = null;
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;        
   HashMap h=null;
   HashMap hshResult=new HashMap();
   String strMessage=null;
   String sqlStr = "BEGIN WEBSALES.SPI_GET_TYPE_ADDRESSES2(?, ?, ?, ?); END;";
   try{
      conn = Proveedor.getConnection();
      
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      
      cstmt.setLong(1, lCustomerId);
      cstmt.setLong(2, lSiteId);
      cstmt.registerOutParameter(3, OracleTypes.CURSOR);           
      cstmt.registerOutParameter(4, OracleTypes.VARCHAR);    
      cstmt.execute();
      
      rs = (ResultSet)cstmt.getObject(3);
      strMessage= cstmt.getString(4);
      
      while (rs.next()) {
         h = new HashMap();
         h.put("value", rs.getString(1));
         h.put("valuedesc", rs.getString(2));    
         list.add(h);
      }
   }
   catch (Exception e) {
      throw new Exception(e);
   }
   finally{
    closeObjectsDatabase(conn,cstmt,rs);
   }
   hshResult.put("strMessage",strMessage);
   hshResult.put("arrListado",list);
   return hshResult;
}
/**
* Motivo: Obtiene las direcciones unicas(arreglo) y su flag correspondiente(arreglo) que inidica si tiene o tiene la direccion creada.
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 09/10/2007
* @param     lCustomerId    
* @param     lSiteId
* @param     arrAddrtype  
* @param     arrAddrtypeFlag   
* @return    HashMap
*/     
public HashMap getCheckUniqueAddress(long lCustomerId,long lSiteId)   
throws SQLException, Exception{ 
   String strMessage=null;
   HashMap hshData=new HashMap();
   ArrayList aAddTypeFlg=new ArrayList();
   ArrayList aAddType=new ArrayList();
   Connection conn = null;
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;     
   ResultSet rs1 = null;     
   HashMap h=null;
   HashMap h1=null;
   String sqlStr = "BEGIN WEBSALES.SPI_CHECK_UNIQUE_ADDRESSES2( ?, ?, ?, ?, ?); END;";
   try{
      conn = Proveedor.getConnection();
      System.out.println("Inicio lCustomerId:   -- >" + lCustomerId);
      System.out.println("INICIO lSiteId:   -- >" + lSiteId);
       
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, lCustomerId);
      cstmt.setLong(2, lSiteId);     
      cstmt.registerOutParameter(3, OracleTypes.CURSOR);   
      cstmt.registerOutParameter(4, OracleTypes.CURSOR);   
      cstmt.registerOutParameter(5, Types.VARCHAR);    
      cstmt.execute();
      
      strMessage=  cstmt.getString(5);
      
      System.out.println("*nMensaje:   -- >" + strMessage);
      System.out.println("*lCustomerId:   -- >" + lCustomerId);
      System.out.println("*lSiteId:   -- >" + lSiteId);
      
     if(strMessage == null)
     { 
      rs = (ResultSet)cstmt.getObject(3);  
      rs1 = (ResultSet)cstmt.getObject(4);      
     
        while (rs.next()) {
           h = new HashMap();
           h.put("addrtype", rs.getString("dir"));            
           aAddType.add(h);
        }
     
        while (rs1.next()) {
           h1 = new HashMap();
           h1.put("addrtypeflag", rs1.getString("dir"));            
           aAddTypeFlg.add(h1);
        }   
     }
   }
   catch (Exception e) {
   System.out.println("Error " + e.getMessage());
      throw new Exception(e);
   }
   finally{
      closeObjectsDatabase(conn,cstmt,rs);
      System.out.println("Cerrand conexion 1");      
      closeObjectsDatabase(null,null,rs1);
      System.out.println("Cerrand conexion 2");            
   }   
   hshData.put("arrTypeFlg",aAddTypeFlg);
   hshData.put("arrType",aAddType);
   hshData.put("strMessage",strMessage);
   
   return hshData;
}

/**
* Motivo: Inserta un Address relacionado a un Site
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 11/10/2007
* @param     AddressObjectBean  
* @param     String
* @param     String  
* @return    HashMap 
*/
public HashMap insAddress(AddressObjectBean objAddressBean,String strTrama,Connection conn) 
throws Exception,SQLException {


   String strMessage=null;
   HashMap hshData=new HashMap();
   long lAddressId=0;
   OracleCallableStatement cstmt = null;
   //Connection conn=null;
   
 try{  
   
   String sqlStr = "BEGIN WEBSALES.SPI_CUSTOMER_ADDRESS_NEW_SAVE(?, ?, ?, ?, ?," + 
                                                "?, ?, ?, ?, ?," +
                                                "?, ?, ?, ?, ?," +
                                                "?, ?, ?, ?, ? ); END;"; //NBRAVO se agrego uno
   
   //conn = Proveedor.getConnection();
   //conn.setAutoCommit(false);
   cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
   cstmt.setLong(1, objAddressBean.getSwobjectid());               
   cstmt.setString(2,objAddressBean.getSwobjecttype()); 
   cstmt.setLong(3,objAddressBean.getSwregionid());
   cstmt.setString(4,objAddressBean.getSwaddress1());
   cstmt.setString(5,objAddressBean.getSwaddress2());
   cstmt.setString(6,objAddressBean.getSwaddress3());
   cstmt.setString(7,objAddressBean.getSwcountry());
   cstmt.setString(8,objAddressBean.getSwstate());
   cstmt.setString(9,objAddressBean.getSwprovince());
   cstmt.setString(10,objAddressBean.getSwcity());
   cstmt.setInt(11,objAddressBean.getNpdepartamentoid());
   cstmt.setInt(12,objAddressBean.getNpprovinciaid());
   cstmt.setInt(13,objAddressBean.getNpdistritoid());
   cstmt.setString(14,objAddressBean.getSwzip());
   cstmt.setString(15,objAddressBean.getSwnote());
   cstmt.setInt(16,objAddressBean.getFlagAddress()); //agregado y modificado NBRAVO
   cstmt.setString(17,objAddressBean.getSwcreatedby());
   cstmt.setString(18,strTrama);               
   cstmt.registerOutParameter(19, Types.NUMERIC);               
   cstmt.registerOutParameter(20, Types.VARCHAR);         
   
   int i=cstmt.executeUpdate();
   strMessage = cstmt.getString(20);
   lAddressId  = cstmt.getLong(19);
         
   System.out.println(" -------------------- INICIO SITEDAO ---------------------- ");
   System.out.println("Codigo del Direccion -->"+lAddressId + " cod de transaccion->"+ i);    
   System.out.println("Mensaj de Error -->"+strMessage);    
   System.out.println("-*--*-objAddressBean.getFlagAddress() -->"+objAddressBean.getFlagAddress());  
   System.out.println(" -------------------- FIN SITEDAO ---------------------- ");
 }catch(Exception ex){
   throw new Exception(ex);
 }finally{
  closeObjectsDatabase(null,cstmt,null);
 }
   hshData.put("lAddressId",lAddressId+"");
   hshData.put("strMessage",strMessage);
   
   return hshData;
}       
       

/**
* Motivo: Actualiza una dirección
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 22/10/2007
* @param     AddressObjectBean  
* @param     int
* @param     String  
* @param     int
* @return    HashMap 
*/
public HashMap updAddress(AddressObjectBean objAddressBean ,int iEdicion,String strTramaAddresType,int iIndice,Connection conn)
throws Exception,SQLException {

   int iRetorno=-1;
   OracleCallableStatement cstmt = null;
   //Connection conn=null;
   String strMessage=null;
   
   HashMap hshResult=new HashMap();
   
   try{
   String sqlStr = "BEGIN WEBSALES.SPI_CUSTOMER_ADDRESS_UPDATE(?, ?, ?, ?, ?," + 
                                                    "?, ?, ?, ?, ?," +
                                                    "?, ?, ?, ?, ?," +
                                                    "?, ?, ?, ?, ?, ? ,?); END;"; // SE AGREGO UNO NBRAVO
   
   //conn = Proveedor.getConnection();
   cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
   cstmt.setLong(1, objAddressBean.getAddressId());               
   cstmt.setLong(2, objAddressBean.getSwobjectid());               
   cstmt.setString(3,objAddressBean.getSwobjecttype()); 
   cstmt.setLong(4,objAddressBean.getSwregionid());
   cstmt.setString(5,objAddressBean.getSwaddress1());
   cstmt.setString(6,objAddressBean.getSwaddress2());
   cstmt.setString(7,objAddressBean.getSwaddress3());
   cstmt.setString(8,objAddressBean.getSwstate());          
   cstmt.setString(9,objAddressBean.getSwprovince());               
   cstmt.setString(10,objAddressBean.getSwcity());
   cstmt.setInt(11,objAddressBean.getNpdepartamentoid());
   cstmt.setInt(12,objAddressBean.getNpprovinciaid());
   cstmt.setInt(13,objAddressBean.getNpdistritoid());
   cstmt.setString(14,objAddressBean.getSwzip());
   cstmt.setString(15,objAddressBean.getSwnote());
   cstmt.setInt(16,objAddressBean.getFlagAddress()); // NBRAVO
   cstmt.setString(17,objAddressBean.getSwcreatedby());
   cstmt.setInt(18,iEdicion);
   cstmt.setString(19,strTramaAddresType);               
   cstmt.setInt(20,iIndice);
   cstmt.registerOutParameter(21, Types.NUMERIC);               
   cstmt.registerOutParameter(22, Types.VARCHAR);               
   
   
   int i=cstmt.executeUpdate();
   strMessage = cstmt.getString(22);
   iRetorno  = cstmt.getInt(21);            
   
   System.out.println("iRetorno -->"+iRetorno );    
   System.out.println("Mensaj de Error -->"+strMessage);    
   
   }catch(Exception ex){         
     throw new Exception(ex);
   }finally{
     closeObjectsDatabase(null,cstmt,null);
   }
   hshResult.put("strMessage",strMessage);
   hshResult.put("iFlgRetorno",iRetorno+"");
   
   return hshResult;
}          
       
/**
* Motivo: Elimina un Address
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 11/10/2007
* @param     lAddressId  
* @param     lObjectId
* @param     strObjectType
* @param     strUser  
* @param     lCustomerId 
* @param     lSiteId
* @param     strMessage
* @return    String 
*/ 
public String delAddress(long lAddressId ,long lObjectId,String strObjectType,String strUser,long lCustomerId, long lSiteId,Connection conn) 
throws Exception, SQLException {

   OracleCallableStatement cstmt = null;
   String strMessage=null;
   
   try{
   
   String sqlStr = "BEGIN WEBSALES.SPI_CUSTOMER_ADDRESS_DELETE(?, ?, ?, ?, ?, ?, ? ); END;";
   
   //conn = Proveedor.getConnection();
   cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
   cstmt.setLong(1, lAddressId);                   
   cstmt.setLong(2, lObjectId);                   
   cstmt.setString(3, strObjectType);                 
   cstmt.setString(4, strUser);                   
   cstmt.setLong(5, lCustomerId);                   
   cstmt.setLong(6, lSiteId);                       
   cstmt.registerOutParameter(7, Types.VARCHAR);               
   
   int i=cstmt.executeUpdate();
   strMessage=cstmt.getString(7);               
   
   System.out.println("Mensaj de Eliminacion  -->"+strMessage);    
   
   }catch(Exception ex){
     throw new Exception(ex);
   }finally{
     closeObjectsDatabase(null,cstmt,null);
   }           
 
   return strMessage;
}   
 
/**
* Motivo: Actualiza una dirección
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 22/10/2007
* @param     AddressObjectBean  
* @return    HashMap 
*/
public HashMap validateRegionDelivAddr(AddressObjectBean objAddressBean) throws SQLException, Exception {

   int iRetorno=-1;
   OracleCallableStatement cstmt = null;
   Connection conn=null;           
   HashMap hshResult=new HashMap();
   String sqlStr = "BEGIN WEBSALES.SPI_VALIDATE_REGION_DELIV_ADDR(?, ?, ? ); END;";
   try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);               
      cstmt.setLong(1, objAddressBean.getSwobjectid());               
      cstmt.setString(2,objAddressBean.getSwobjecttype()); 
      cstmt.registerOutParameter(3, Types.NUMERIC);                                                 
      cstmt.execute();           
      iRetorno  = cstmt.getInt(3);            
      System.out.println("iRetorno -->"+iRetorno );               
   }   
   catch(Exception e){
      throw new Exception(e);
   }
   finally{
      closeObjectsDatabase(conn,cstmt,null);                              
   }
   hshResult.put("iRetorno",iRetorno+"");
   
   return hshResult;
}     
       
/**
* Motivo: Obtiene las direcciones no intercambiables(arreglo) y su flag correspondiente(arreglo) que inidica si tiene o tiene la direccion creada.
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 12/10/2007
* @param     lCustomerId  
* @param     lSiteId   
* @param     HashMap           
*/
public HashMap getNoChangeAddress(long lCustomerId,long lSiteId) 
throws SQLException, Exception{       

   OracleCallableStatement cstmt = null;         
   Connection conn=null;
   ResultSet rs1 = null;
   ResultSet rs2 = null;
   HashMap h=null;
   HashMap hshResultado=new HashMap();
   String strMessage=null;
   ArrayList aNoChangeType=new ArrayList();
   ArrayList aNoChangeTypeFlg=new ArrayList();
   String sqlStr = "BEGIN WEBSALES.SPI_CHECK_NOCHANGE_ADDRESSES2(?, ?, ?, ?, ?); END;";
   try{
      conn = Proveedor.getConnection();
      
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, lCustomerId);                      
      cstmt.setLong(2, lSiteId);                                 
      cstmt.registerOutParameter(3, OracleTypes.CURSOR);
      cstmt.registerOutParameter(4, OracleTypes.CURSOR);           
      cstmt.registerOutParameter(5, Types.VARCHAR);
      
      cstmt.execute();
      rs1 = (ResultSet)cstmt.getObject(3);
      rs2 = (ResultSet)cstmt.getObject(4);
      strMessage = cstmt.getString(5);              
      
      while (rs1.next()) {
         h = new HashMap();
         h.put("addstype",  rs1.getString(1));                                           
         aNoChangeType.add(h);
      }
      
      while (rs2.next()) {
         h = new HashMap();
         h.put("addrtypeflag",  rs2.getString(1));                                       
         aNoChangeTypeFlg.add(h);
      }
   }
   catch (Exception e) {
      throw new Exception(e);
   }
   finally{
    closeObjectsDatabase(conn,cstmt,rs1);
    closeObjectsDatabase(null,null,rs2);          
   }   
   hshResultado.put("objAddrType",aNoChangeType);
   hshResultado.put("objAddrTypeFlg",aNoChangeTypeFlg);
   hshResultado.put("strMessage",strMessage);
   
   return hshResultado;
}


/**
* Motivo: Obtiene los contactos obligatorios(cursor) y su flag correspondiente(cursor) que inidica si tiene o tiene el contacto creado.
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 17/10/2007
* @param     lCustomerId  
* @param     lSiteId  
* @param     HashMap           
*/
public HashMap getCheckMandatoryContacts(long lCustomerId,long lSiteId) 
throws SQLException, Exception{       

   OracleCallableStatement cstmt = null;                    
   Connection conn=null;
   ResultSet rs1 = null;
   ResultSet rs2 = null;
   HashMap h=null;
   HashMap hshResult=new HashMap();
   ArrayList aContactType=new ArrayList();
   ArrayList aContactTypeFlg=new ArrayList();
   String strMessage=null;
   String sqlStr = "BEGIN WEBSALES.SPI_CHECK_MANDATORY_CONTACTS2(?, ?, ?, ?, ?); END;";
   try{
      conn = Proveedor.getConnection();
      
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, lCustomerId);                      
      cstmt.setLong(2, lSiteId);                                 
      cstmt.registerOutParameter(3, OracleTypes.CURSOR);
      cstmt.registerOutParameter(4, OracleTypes.CURSOR);           
      cstmt.registerOutParameter(5, Types.VARCHAR);
      
      cstmt.execute();
      rs1 = (ResultSet)cstmt.getObject(3);
      rs2 = (ResultSet)cstmt.getObject(4);
      strMessage = cstmt.getString(5);              
      
      while (rs1.next()) {
         h = new HashMap();
         h.put("contacttype",  rs1.getString(1));                                           
         aContactType.add(h);
      }
      
      while (rs2.next()) {
         h = new HashMap();
         h.put("contacttypeflg",  rs2.getString(1));                                       
         aContactTypeFlg.add(h);
      }
   }
   catch (Exception e) {
       throw new Exception(e);
   }   
   finally{
    closeObjectsDatabase(conn,cstmt,rs1);
    closeObjectsDatabase(null,null,rs2);           
   }   
   hshResult.put("objContactType",aContactType);
   hshResult.put("objContactTypeFlg",aContactTypeFlg);
   hshResult.put("strMessage",strMessage);
   
   return hshResult;

}      
      
/**
* Motivo: Obtiene los contactos unicos(cursor) y su flag correspondiente(cursor) que inidica si tiene o tiene el contacto creado.
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 17/10/2007
* @param     lCustomerId  
* @param     lSiteId  
* @param     HashMap           
*/
public HashMap getCheckUniqueContacts(long lCustomerId,long lSiteId) 
throws SQLException, Exception{       

   OracleCallableStatement cstmt = null;                    
   Connection conn=null;
   ResultSet rs1 = null;
   ResultSet rs2 = null;
   HashMap h=null;
   HashMap hshResult=new HashMap();
   ArrayList aContactType=new ArrayList();
   ArrayList aContactTypeFlg=new ArrayList();
   String strMessage=null;
   String sqlStr = "BEGIN WEBSALES.SPI_CHECK_UNIQUE_CONTACTS2(?, ?, ?, ?, ?); END;";
   try{
      conn = Proveedor.getConnection();
      
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, lCustomerId);                      
      cstmt.setLong(2, lSiteId);                                 
      cstmt.registerOutParameter(3, OracleTypes.CURSOR);
      cstmt.registerOutParameter(4, OracleTypes.CURSOR);           
      cstmt.registerOutParameter(5, Types.VARCHAR);
      
      cstmt.execute();
      rs1 = (ResultSet)cstmt.getObject(3);
      rs2 = (ResultSet)cstmt.getObject(4);
      strMessage = cstmt.getString(5);              
      
      while (rs1.next()) {
         h = new HashMap();
         h.put("contacttype",  rs1.getString(1));                                           
         aContactType.add(h);
      }
      
      while (rs2.next()) {
         h = new HashMap();
         h.put("contacttypeflg",  rs2.getString(1));                                       
         aContactTypeFlg.add(h);
      }
   } 
   catch (Exception e) {
      throw new Exception(e);
   }
   finally{
    closeObjectsDatabase(conn,cstmt,rs1);
    closeObjectsDatabase(null,null,rs2);  
   }   
   hshResult.put("objContactType",aContactType);
   hshResult.put("objContactTypeFlg",aContactTypeFlg);
   hshResult.put("strMessage",strMessage);
   
   return hshResult;
}               
      
/**
* Motivo: Obtiene todos los tipos de contactos existentes en un cursor
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 17/10/2007
* @param     lCustomerId  
* @param     lSiteId    
* @return    HashMap 
*/
public HashMap getTypeContacts(long lCustomerId,long lSiteId) 
throws SQLException, Exception{

   ArrayList list = new ArrayList();        
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   Connection conn=null; 
   HashMap h=null;
   HashMap hshResult=new HashMap();
   String strMessage=null;
   String sqlStr = "BEGIN WEBSALES.SPI_GET_TYPE_CONTACTS2(?, ?, ?, ?); END;";
   try{
      conn = Proveedor.getConnection();
      
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, lCustomerId);                      
      cstmt.setLong(2, lSiteId);                                 
      cstmt.registerOutParameter(3, OracleTypes.CURSOR);  
      cstmt.registerOutParameter(4, Types.VARCHAR);
      
      cstmt.execute();      
      rs = (ResultSet)cstmt.getObject(3);
      strMessage = cstmt.getString(4);
      
      while (rs.next()) {
         h = new HashMap();
         h.put("contactid", rs.getString("npvalue"));                                           
         h.put("contact", rs.getString("npvaluedesc"));                                           
         list.add(h);
      }
   }
   catch (Exception e) {
       throw new Exception(e);
   }
   finally{
    closeObjectsDatabase(conn,cstmt,rs);
   }
   hshResult.put("objContacttype",list);      
   hshResult.put("strMessage",strMessage);      
   
   return hshResult;
}
      
/**
* Motivo: Obtiene todos los tipos de contactos que no son intercambiables, en un cursor.
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 17/10/2007
* @param     lCustomerId  
* @param     lSiteId    
* @return    HashMap 
*/
public HashMap getNoChangeContacts(long lCustomerId,long lSiteId) 
throws SQLException, Exception{

   ArrayList list = new ArrayList();        
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   Connection conn=null;
   HashMap h=null;
   HashMap hshResult=new HashMap();
   String sqlStr = "BEGIN WEBSALES.SPI_GET_NOCHANGE_CONTACTS(?, ?, ?); END;";
   try{
      conn = Proveedor.getConnection();
      
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, lCustomerId);                      
      cstmt.setLong(2, lSiteId);                                 
      cstmt.registerOutParameter(3, OracleTypes.CURSOR);       
      
      cstmt.execute();
      rs = (ResultSet)cstmt.getObject(3);
       
      while (rs.next()) {
         h = new HashMap();
         h.put("contact", rs.getString(1));                                           
         list.add(h);
      }
   }
   catch (Exception e) {
      throw new Exception(e);
   }
   finally{
    closeObjectsDatabase(conn,cstmt,rs);      
   }   
   hshResult.put("objContacttype",list);           
   
   return hshResult;
}    

/**
* Motivo: Obtiene los contactos unicos(cursor) y su flag correspondiente(cursor) que inidica si tiene o tiene el contacto creado.
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 17/10/2007
* @param     lCustomerId  
* @param     lSiteId  
* @param     HashMap           
*/
public HashMap getCheckNoChangeContacts(long lCustomerId,long lSiteId) 
throws SQLException, Exception{       

   OracleCallableStatement cstmt = null;                    
   Connection conn=null;
   ResultSet rs1 = null;
   ResultSet rs2 = null;
   HashMap h=null;
   HashMap hshResult=new HashMap();
   ArrayList aContactType=new ArrayList();
   ArrayList aContactTypeFlg=new ArrayList();
   String strMessage=null;
   String sqlStr = "BEGIN WEBSALES.SPI_CHECK_NOCHANGE_CONTACTS2(?, ?, ?, ?, ?); END;";
   try{
      conn = Proveedor.getConnection();      
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, lCustomerId);                      
      cstmt.setLong(2, lSiteId);                                 
      cstmt.registerOutParameter(3, OracleTypes.CURSOR);
      cstmt.registerOutParameter(4, OracleTypes.CURSOR);           
      cstmt.registerOutParameter(5, Types.VARCHAR);      
      cstmt.execute();
      rs1 = (ResultSet)cstmt.getObject(3);
      rs2 = (ResultSet)cstmt.getObject(4);
      strMessage = cstmt.getString(5);                    
      while (rs1.next()) {
         h = new HashMap();
         h.put("contacttype",  rs1.getString(1));                                           
         aContactType.add(h);
      }      
      while (rs2.next()) {
         h = new HashMap();
         h.put("contacttypeflg",  rs2.getString(1));                                       
         aContactTypeFlg.add(h);
      }
   }
   catch (Exception e) {
      throw new Exception(e);
   }
   finally{
     closeObjectsDatabase(conn,cstmt,rs1); 
     closeObjectsDatabase(null,null,rs2); 
   }   
   hshResult.put("objContactType",aContactType);
   hshResult.put("objContactTypeFlg",aContactTypeFlg);
   hshResult.put("strMessage",strMessage);
   
   return hshResult;

}    
 
/**
* Motivo: Inserta un Contacto relacionado a un Site
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 11/10/2007
* @param     ContactObjectBean  
* @param     String
* @param     String  
* @return    HashMap 
*/
public HashMap insContact(ContactObjectBean objContactBean,String strTrama,Connection conn) 
throws Exception,SQLException {
   HashMap hshResult=new HashMap();
   String strMessage=null;
   long lPersonId=0;
   OracleCallableStatement cstmt = null;
   //Connection conn=null;
   
   try{
   String sqlStr = "BEGIN WEBSALES.SPI_INSERT_CONTACT2(?, ?, ?, ?, ?," + 
                                         "?, ?, ?, ?, ?," +
                                         "?, ?, ?, ?, ?," +
                                         "?, ?, ?); END;";
   
   //conn = Proveedor.getConnection();
   cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
   cstmt.setLong(1, objContactBean.getSwjobtitleid());               
   cstmt.setString(2,objContactBean.getSwjobtitle()); 
   cstmt.setLong(3,objContactBean.getSwcustomerid());
   cstmt.setLong(4,objContactBean.getSwsiteid());
   cstmt.setString(5,objContactBean.getSwemailaddress());
   cstmt.setString(6,objContactBean.getSwfirstname());
   cstmt.setString(7,objContactBean.getSwlastname());
   cstmt.setString(8,objContactBean.getSwmiddlename());
   cstmt.setString(9,objContactBean.getSwcreatedby());
   cstmt.setString(10,objContactBean.getSwtitle());
   cstmt.setString(11,objContactBean.getSwofficephonearea());
   cstmt.setString(12,objContactBean.getSwofficephone());
   cstmt.setString(13,objContactBean.getSwfaxarea());
   cstmt.setString(14,objContactBean.getSwfax());
   cstmt.setString(15,objContactBean.getSwofficephoneext());               
   cstmt.setString(16,strTrama);               
   cstmt.registerOutParameter(17, Types.NUMERIC);               
   cstmt.registerOutParameter(18, Types.VARCHAR);               
   
   
   int i=cstmt.executeUpdate();
   strMessage = cstmt.getString(18);
   lPersonId  = cstmt.getLong(17);            
   
   System.out.println("COdigo del Contacto DAO -->"+lPersonId + " cod de transaccion->"+ i);    
   System.out.println("Mensaj de Error -->"+strMessage);    
   
   
   }catch(Exception ex){
     throw new Exception(ex);
   }finally{
     closeObjectsDatabase(null,cstmt,null);
   }
   
   hshResult.put("lPersonId",lPersonId+"");                   
   hshResult.put("strMessage",strMessage);
   
   return hshResult;
}    
       

/**
* Motivo: Actualiza un contacto
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 23/10/2007
* @param     ContactObjectBean  
* @param     int
* @param     String  
* @param     int
* @return    HashMap 
*/
public HashMap updContact(ContactObjectBean objContactBean,int iEdicion,String strTramaContactType,int iIndice,Connection conn)
throws Exception,SQLException {

   int iRetorno=-1;
   OracleCallableStatement cstmt = null;
   //Connection conn=null;
   String strMessage=null;
   HashMap hshResult=new HashMap();
   String sqlStr = "BEGIN WEBSALES.SPI_CUSTOMER_CONTACT_UPDATE(?, ?, ?, ?, ?," + 
                                                       "?, ?, ?, ?, ?," +
                                                       "?, ?, ?, ?, ?," +
                                                       "?, ?, ?, ? ); END;";
   
   try{
   //conn = Proveedor.getConnection();
   cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
   cstmt.setLong(1, objContactBean.getSwpersonid());               
   cstmt.setLong(2, objContactBean.getSwjobtitleid());               
   cstmt.setString(3,objContactBean.getSwjobtitle()); 
   cstmt.setString(4,objContactBean.getSwtitle());
   cstmt.setString(5,objContactBean.getSwfirstname());
   cstmt.setString(6,objContactBean.getSwlastname());
   cstmt.setString(7,objContactBean.getSwmiddlename());
   cstmt.setString(8,objContactBean.getSwemailaddress());
   cstmt.setString(9,objContactBean.getSwofficephonearea());
   cstmt.setString(10,objContactBean.getSwofficephone());
   cstmt.setString(11,objContactBean.getSwfaxarea());
   cstmt.setString(12,objContactBean.getSwfax());
   cstmt.setString(13,objContactBean.getSwofficephoneext());  
   cstmt.setString(14,strTramaContactType);    
   cstmt.setString(15,objContactBean.getSwcreatedby());
   cstmt.setInt(16,iEdicion);
   cstmt.setInt(17,iIndice);            
   cstmt.registerOutParameter(18, Types.VARCHAR);               
   cstmt.registerOutParameter(19, Types.NUMERIC);       
   
   
   int i=cstmt.executeUpdate();
   strMessage = cstmt.getString(18);
   iRetorno  = cstmt.getInt(19);            
   
   System.out.println("iRetorno -->"+iRetorno );    
   System.out.println("Mensaj de Error -->"+strMessage);    
   
   }catch(Exception ex){           
     throw new Exception(ex);
   }finally{                      
     closeObjectsDatabase(null,cstmt,null);
   }
   hshResult.put("strMessage",strMessage);
   hshResult.put("iFlgRetorno",iRetorno+"");
   
   return hshResult;
}       

/**
* Motivo: Elimina un Contacto
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 18/10/2007
* @param     lPersonId  
* @param     lCustomerId
* @param     lSiteId
* @param     strCreatedBy  
* @return    HashMap 
*/ 
public HashMap delContact(ContactObjectBean objContactBean,Connection conn) 
throws Exception,SQLException { 

   OracleCallableStatement cstmt = null;
   //Connection conn=null;
   String strMessage=null;
   HashMap hshResultado=new HashMap();
   
   try{
   String sqlStr = "BEGIN WEBSALES.SPI_DELETE_CONTACT(?, ?, ?, ?, ?); END;";
   
   //conn = Proveedor.getConnection();
   cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
   cstmt.setLong(1, objContactBean.getSwpersonid());                   
   cstmt.setLong(2, objContactBean.getSwcustomerid());                   
   cstmt.setLong(3, objContactBean.getSwsiteid());                 
   cstmt.setString(4, objContactBean.getSwcreatedby());                          
   cstmt.registerOutParameter(5, Types.VARCHAR);               
   
   int i=cstmt.executeUpdate();
   strMessage=cstmt.getString(5);               
   
   System.out.println("Mensaj de Eliminacion de Contacto -->"+strMessage);    
   
   }catch(Exception ex){           
     throw new Exception(ex);
   }finally{                      
     closeObjectsDatabase(null,cstmt,null);
   }          
                          
   hshResultado.put("strMessage",strMessage);
   return hshResultado;
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

      
/**
* Motivo: Obtiene todos los tipos de contactos que no son intercambiables, en un cursor.
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 17/10/2007
* @param     lCustomerId  
* @param     lSiteId    
* @return    HashMap 
*/

public HashMap getLongMaxSite() throws SQLException, Exception{
   String strMessage=null;
   HashMap hshData=new HashMap();
   String strLongSite=null;    
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   Connection conn=null;
   
   String sqlStr = "BEGIN WEBSALES.SPI_GET_SITE_LONG(?,?); END;";
   try{
      conn = Proveedor.getConnection();
      
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);                                                                 
      cstmt.registerOutParameter(1, Types.VARCHAR);
      cstmt.registerOutParameter(2, Types.VARCHAR);                      
      
      cstmt.execute();
      strLongSite = cstmt.getString(1);           
      strMessage = cstmt.getString(2);              
   }
   catch (Exception e) {
      throw new Exception(e);
   }
   finally{
      closeObjectsDatabase(conn,cstmt,null);
   }      
   hshData.put("strMessage",strMessage);
   hshData.put("strLongSite",strLongSite);
   return hshData;
} 


    /**
   * Motivo: Retorna el id del Site
   * <br>Realizado por: <a href="mailto:hugo.moreno@nextel.com.pe">Hugo Moreno</a>
   * <br>Fecha: 29/04/2008
   * @param     strcodBscs     
   * @return    int 
   */ 
   public  int getSiteidByCodbscs(String strcodBscs)throws SQLException, Exception{
   
      OracleCallableStatement cstmt = null;
      Connection conn=null;     
      int iRetorno=0; 
      System.out.println("[SiteDAO][strcodBscs]"+strcodBscs);
      String sqlStr = "BEGIN WEBSALES.NPSL_SITE_PKG.SP_GET_BSCSID_BY_CODBSCS(?, ?, ?); END;";
      try{
         conn = Proveedor.getConnection();
         
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);       
         
         cstmt.setString(1, strcodBscs);         
         cstmt.registerOutParameter(2, OracleTypes.NUMBER);         
         cstmt.registerOutParameter(3, Types.CHAR);
         
         cstmt.execute();           
         iRetorno = cstmt.getInt(2);       
         System.out.println("[SiteDAO][iRetorno]"+iRetorno);
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn,cstmt,null);  
      }         
      return iRetorno;
   }         
   
   /**
   * Motivo: Obtiene si el origen del Site es una oportunidad o una orden
   * <br>Realizado por: <a href="mailto:hugo.moreno@nextel.com.pe">Hugo Moreno</a>
   * <br>Fecha: 22/05/2008
   * @param     lSiteId           
   * @return    int 
   */ 
   public int getSourceSite(long lSiteId)
   throws SQLException, Exception{
   
      OracleCallableStatement cstmt = null;
      Connection conn=null;
      int iResult = -1;
      String sqlStr = "BEGIN WEBSALES.NPSL_SITE_PKG.SP_GET_SOURCE_SITE(?, ?, ? ); END;";
      try{
         conn = Proveedor.getConnection();
         
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         cstmt.setLong(1, lSiteId);  
         cstmt.registerOutParameter(2, OracleTypes.NUMBER);  
         cstmt.registerOutParameter(3, Types.CHAR);   
         
         cstmt.execute();
         iResult = cstmt.getInt(2);       
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn,cstmt,null);
      }      
      return iResult;
   }    
   
   
  /**
   * Motivo: Obtener el Grupo de Solución de un site
   * <br/>Realizado por: <a href="mailto:jorge.perez@nextel.com.pe">Jorge Pérez</a>
   * <br/>Fecha: 15/05/2009
   * 
   * @throws java.lang.Exception
   * @throws java.sql.SQLException
   * @return 
   * @param lSiteId
   */
  public HashMap getSiteSolutionGroup(long lSiteId) throws SQLException, Exception{       
     String strMessage=null;
     String strSolutionGroup=null;
     String strSolutionGroupId=null;
     HashMap hshData=new HashMap();
     OracleCallableStatement cstmt = null;  
     Connection conn=null;
     SiteBean objSiteBean =new SiteBean();
     String sqlStr = "BEGIN WEBSALES.SPI_GET_SITE_SOLUTION_GROUP(?, ?, ?, ?); END;";
     try{
        conn = Proveedor.getConnection();   
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);      
        if (lSiteId==0)
           cstmt.setNull(1,Types.INTEGER);
        else   
           cstmt.setLong(1, lSiteId);
           
        cstmt.registerOutParameter(2, OracleTypes.NUMBER);
        cstmt.registerOutParameter(3, Types.CHAR);
        cstmt.registerOutParameter(4, Types.CHAR);               
        
        cstmt.execute();      
        
        strMessage = cstmt.getString(4);                        
        
         if( strMessage==null){
           strSolutionGroupId = cstmt.getString(2);  
           strSolutionGroup = cstmt.getString(3); 
         }
       
        hshData.put("strSolutionGroupId",strSolutionGroupId);
        hshData.put("strSolutionGroup",strSolutionGroup);
        hshData.put("strMessage",strMessage);
              
     }
     catch (Exception e) {
        hshData.put("strMessage",e.getMessage()); 
     }
     finally{
         try{
          closeObjectsDatabase(conn,cstmt,null); 
         }catch (Exception e) {
            logger.error(formatException(e));
         }
    }         
      
        
     return hshData;
  }
  
  
    /**
   * Motivo: Obtener los Grupo Solución de una categoría
   * <br/>Realizado por: <a href="mailto:jorge.perez@nextel.com.pe">Jorge Pérez</a>
   * <br/>Fecha: 10/07/2009
   * 
   * @throws java.lang.Exception
   * @throws java.sql.SQLException
   * @return 
   * @param lSpecificationId
   */
  public HashMap getSpecSolutionGroup(long lSpecificationId) throws SQLException, Exception{       
     String strMessage=null;     
     ArrayList list = new ArrayList();
     SolutionGroupBean objSolutionGroupBean = null;
     HashMap hshData=new HashMap();
     ResultSet rs = null;
     OracleCallableStatement cstmt = null;  
     Connection conn=null;
     
     String sqlStr = "BEGIN PRODUCT.NP_PRODUCT03_PKG.SP_GET_SPEC_SOLUTION_GROUP(?, ?, ?); END;";
     try{
        conn = Proveedor.getConnection();   
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);      
          
        cstmt.setLong(1, lSpecificationId);                 
        cstmt.registerOutParameter(2, OracleTypes.CURSOR);     
        cstmt.registerOutParameter(3, Types.CHAR);
        cstmt.execute();      
        
        strMessage = cstmt.getString(3);                        
        
          if( strMessage == null ){
            rs = (ResultSet)cstmt.getObject(2);
          while (rs.next()) {
             objSolutionGroupBean = new SolutionGroupBean();
             objSolutionGroupBean.setNpsolutiongroupid(rs.getLong("npsolutiongroupid"));
             objSolutionGroupBean.setNpname(rs.getString("npname"));
             objSolutionGroupBean.setNpprefix(rs.getString("npprefix"));
             objSolutionGroupBean.setNpdivisionid(rs.getLong("npdivisionid"));
             
             list.add(objSolutionGroupBean);
          }
        }                    
          
        hshData.put("strMessage",strMessage);
        hshData.put("objArrayList",list);
        return hshData;
              
     }
     catch (Exception e) {
        hshData.put("strMessage",e.getMessage()); 
     }
     finally{
         try{
          closeObjectsDatabase(conn,cstmt,rs); 
         }catch (Exception e) {
            logger.error(formatException(e));
         }
    }         
      
        
     return hshData;
  } 

   /**
     * Motivo: Obtiene el nuevo site creado desde una orden.
     * <br>Realizado por: PBI000000042016
     * <br>Fecha: 24/09/2018
     * @param     lSiteId
     * @return    long
     */
    public Long getUnknownSite(long lSiteId, Connection conn){

        OracleCallableStatement cstmt = null;
        Long result = null;
        String sqlStr = "BEGIN WEBSALES.PKG_MS_SINC_RESP.SP_MS_AC_OBTENER_SITE(?, ?, ? ); END;";
        try{
            if(conn == null){
            conn = Proveedor.getConnection();
            }

            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, lSiteId);
            cstmt.registerOutParameter(2, OracleTypes.NUMBER);
            cstmt.registerOutParameter(3, Types.CHAR);

            cstmt.execute();
            result = cstmt.getLong(2);
        }
        catch (Exception e) {
            result = null;
            logger.error(formatException(e));
        }
        finally{
            try{
                closeObjectsDatabase(null,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return result;
    }

}
