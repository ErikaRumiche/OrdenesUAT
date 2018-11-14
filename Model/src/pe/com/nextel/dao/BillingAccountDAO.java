package pe.com.nextel.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import pe.com.nextel.bean.BaAssignmentBean;
import pe.com.nextel.bean.BillingAccountBean;
import pe.com.nextel.bean.BillingContactBean;
import pe.com.nextel.bean.CoAssignmentBean;
import pe.com.nextel.bean.CustomerBean;
import pe.com.nextel.util.MiUtil;


//import pe.com.nextel.util.DbmsOutput;

public class BillingAccountDAO extends GenericDAO{
    private static String strError;

    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - INICIO
     *  REALIZADO POR: Lee Rosales Crispín
     *  FECHA: 28/08/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
     
  /**
   * Motivo:  Obtiene un listado de los bilings account
   * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
   * <br>Fecha: 01/08/2007
   * 
   * @return     HashMap 
   * @param      iNpcustomerid    
   * @param      strMessage
   * @throws java.sql.SQLException
   * @throws java.lang.Exception
   * @param longContractId
   */
  public HashMap getCoAssignmentBillingByContract(long longContractId)  throws Exception, SQLException {
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    Connection conn = null;
    String strMessage=null;
    HashMap hshResultado=new HashMap();
    ArrayList listado = new ArrayList();
    CoAssignmentBean objCoAssignmentBean = null;
    try{
      conn = Proveedor.getConnection();    
      String strSql = "BEGIN WEBSALES.NP_BSCS_UTIL04_PKG.SP_GET_BILLING_BY_CONTRACT( ? , ? , ? ); end;";    
      cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
      cstmt.setLong(1, longContractId);
      cstmt.registerOutParameter(2, OracleTypes.CURSOR);
      cstmt.registerOutParameter(3, Types.VARCHAR);
      cstmt.execute();
      
      strMessage = cstmt.getString(3);
      if( strMessage == null ){
        rs = (ResultSet)cstmt.getObject(2);
        
        while (rs.next()) {                     
            objCoAssignmentBean = new CoAssignmentBean(); 
            objCoAssignmentBean.setNpbscssncode(rs.getString("codigo"));
            objCoAssignmentBean.setNporigsitedesc(rs.getString("descripcion"));
            listado.add(objCoAssignmentBean);
        }
      }                            
      hshResultado.put("objArrayList",listado);
      hshResultado.put("strMessage",strMessage);  
    }catch (Exception e) {
      logger.error(formatException(e));      
      hshResultado.put("strMessage", e.getMessage()); 
    }
    finally{
      try{
        closeObjectsDatabase(conn, cstmt, rs);  
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }          
   return hshResultado;
  }
     
  /**
   * Motivo:  Obtiene un listado de los bilings account
   * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
   * <br>Fecha: 01/08/2007
   * 
   * @param      iNpcustomerid    
   * @param      strMessage
   * @return     HashMap 
  */
  public HashMap getCoAssignmentSiteOrig(String strPhone)  throws Exception, SQLException {
    OracleCallableStatement cstmt = null;
    Connection conn = null;
    String strMessage=null;
    HashMap hshResultado=new HashMap();
    CoAssignmentBean objCoAssignmentBean = null;
    try{
      conn = Proveedor.getConnection();    
      String strSql = "BEGIN WEBSALES.NP_BSCS_UTIL04_PKG.SP_GET_PAYMNTRESP_BY_PHONE( ? , ? , ? , ? , ?, ? ); end;";
      
      cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
      cstmt.setString(1, strPhone);
      cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
      cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
      cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
      cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
      cstmt.registerOutParameter(6, OracleTypes.VARCHAR);
      
      cstmt.execute();      
      strMessage = cstmt.getString(6);
      
      if( strMessage == null ){        
        objCoAssignmentBean = new CoAssignmentBean();
        objCoAssignmentBean.setNpbscscontractId(cstmt.getString(2));
        objCoAssignmentBean.setNpbscspaymntrespcustomeridId(cstmt.getString(3));
        objCoAssignmentBean.setNporigsitedesc(cstmt.getString(4));
        objCoAssignmentBean.setNpcustcode(cstmt.getString(5));
      }
      hshResultado.put("objBean",objCoAssignmentBean);
      hshResultado.put("strMessage",strMessage);
    }catch (Exception e) {
      logger.error(formatException(e));
      hshResultado.put("strMessage", e.getMessage()); 
    }
    finally{
      try{
        closeObjectsDatabase(conn, cstmt, null);  
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }              
   return hshResultado;    
  }
     
   /**
   * Motivo:  Obtiene un listado de los bilings account
   * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
   * <br>Fecha: 01/08/2007
   * 
   * @param      iNpcustomerid    
   * @param      strMessage
   * @return     HashMap 
  */
  public HashMap getCoAssignmentList(long longOrderId)  throws Exception, SQLException {
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    Connection conn = null;
    String strMessage=null;
    HashMap hshResultado=new HashMap();
    ArrayList listado = new ArrayList();
    CoAssignmentBean objCoAssignmentBean = null;
    try{
      conn = Proveedor.getConnection();    
      String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_ORDER_CO_ASSIGNMENT( ? , ? , ? ); end;";
      
      cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
      cstmt.setLong(1, longOrderId);
      cstmt.registerOutParameter(2, OracleTypes.CURSOR);
      cstmt.registerOutParameter(3, Types.VARCHAR);
      cstmt.execute();
      strMessage = cstmt.getString(3);
      
      if( strMessage == null ){
        rs = (ResultSet)cstmt.getObject(2);
        
        while (rs.next()) {
            objCoAssignmentBean = new CoAssignmentBean();
            objCoAssignmentBean.setNpcoassignmentid(rs.getLong("npcoassignmentid"));
            objCoAssignmentBean.setNporderid(rs.getLong("nporderid"));
            objCoAssignmentBean.setNpphone(rs.getString("npphone"));
            objCoAssignmentBean.setNpbscscontractId(rs.getString("npbscscontract_id"));
            objCoAssignmentBean.setNpbscssncode(rs.getString("npbscssncode"));
            objCoAssignmentBean.setNpbscspaymntrespcustomeridId(rs.getString("npbscspaymntrespcustomerid_id"));
            objCoAssignmentBean.setNpbscspaymntrespcustname(rs.getString("npbscspaymntrespcustname"));
            objCoAssignmentBean.setNpnewsiteid(rs.getString("npnewsiteid"));
            objCoAssignmentBean.setNptypesite(rs.getString("nptypesite"));
            objCoAssignmentBean.setNpbscsbillingAccountId(rs.getString("npbscsbilling_account_id"));
            objCoAssignmentBean.setNpbillaccnewid(rs.getString("npbillaccnewid"));
            listado.add(objCoAssignmentBean);
        }
      }
                      
      hshResultado.put("objArrayList",listado);
      hshResultado.put("strMessage",strMessage);
    }catch (Exception e) {
      logger.error(formatException(e));
      hshResultado.put("strMessage", e.getMessage()); 
    }
    finally{
      try{
        closeObjectsDatabase(conn, cstmt, rs);  
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }     
    return hshResultado;  
  }

     
 /**
   * Motivo:  Inserta en la tabla ORDERS.NP_CO_ASSIGNMENT
   * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
   * <br>Fecha: 27/10/2007
   * 
   * @return     HashMap 
   * @param      BillingAccountBean     BillingAccountBean
   * @param      Connection             conn
   * @throws java.sql.SQLException
   * @throws java.lang.Exception
   * @param conn
   * @param objCoAssignmentBean
   */
    
	public String insAssignmentBillAccount(CoAssignmentBean objCoAssignmentBean,Connection conn)  throws Exception,SQLException {

   OracleCallableStatement cstmt = null;
   String strMessage = null;
   
   String strSql = "BEGIN NP_ORDERS06_PKG.SP_INS_ORDER_CO_ASSIGNMENT( ?, ?, ?, ?, ?, ?, ?, ? , ? ); end;";
	try{
		cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
		
		cstmt.setLong(1,objCoAssignmentBean.getNporderid());
		cstmt.setString(2,objCoAssignmentBean.getNpphone());
		
		//NpbscscontractId
		if( (objCoAssignmentBean.getNpbscscontractId() == null) || (objCoAssignmentBean.getNpbscscontractId().equals("")))
		  cstmt.setNull(3, OracleTypes.NUMBER);
		else
		  cstmt.setLong(3, Long.parseLong(objCoAssignmentBean.getNpbscscontractId()));
			
		//Npbscssncode	
		if( (objCoAssignmentBean.getNpbscssncode() == null) || (objCoAssignmentBean.getNpbscssncode().equals("")))
		  cstmt.setNull(4, OracleTypes.NUMBER);
		else
		  cstmt.setLong(4, Long.parseLong(objCoAssignmentBean.getNpbscssncode()));
			
		//getNpbscspaymntrespcustomeridId	
		if( (objCoAssignmentBean.getNpbscspaymntrespcustomeridId() == null) || (objCoAssignmentBean.getNpbscspaymntrespcustomeridId().equals("")))
		  cstmt.setNull(5, OracleTypes.NUMBER);
		else
		  cstmt.setLong(5, Long.parseLong(objCoAssignmentBean.getNpbscspaymntrespcustomeridId()));
			
		//getNpnewsiteid	
		if( objCoAssignmentBean.getNpnewsiteid() == null || objCoAssignmentBean.getNpnewsiteid().equals("") || (MiUtil.parseInt(objCoAssignmentBean.getNpnewsiteid())==0))
		  cstmt.setNull(6, OracleTypes.NUMBER);
		else
		  cstmt.setLong(6, Long.parseLong(objCoAssignmentBean.getNpnewsiteid()));
	  
		//getNpbscsbillingAccountId	
		if( (objCoAssignmentBean.getNpbscsbillingAccountId() == null) || (objCoAssignmentBean.getNpbscsbillingAccountId().equals("")))
		  cstmt.setNull(7, OracleTypes.NUMBER);
		else
		  cstmt.setLong(7, Long.parseLong(objCoAssignmentBean.getNpbscsbillingAccountId()));
	  
		//getNpbillaccnewid	
		if( (objCoAssignmentBean.getNpbillaccnewid() == null) || (objCoAssignmentBean.getNpbillaccnewid().equals("")))
		  cstmt.setNull(8, OracleTypes.NUMBER);
		else
		  cstmt.setLong(8, Long.parseLong(objCoAssignmentBean.getNpbillaccnewid()));
			
		cstmt.registerOutParameter(9, Types.VARCHAR);
		cstmt.execute();
		strMessage = cstmt.getString(9);
	}		
   catch (Exception e) {
		throw new Exception(e);
   }
   finally{   
		closeObjectsDatabase(null,cstmt,null);
   }     
   return strMessage;     

  }
    
 /**
   * Motivo:  Actualiza en la tabla ORDERS.NP_CO_ASSIGNMENT
   * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
   * <br>Fecha: 27/10/2007
   * 
   * @return     HashMap 
   * @param      BillingAccountBean     BillingAccountBean
   * @param      Connection             conn
   * @throws java.sql.SQLException
   * @throws java.lang.Exception
   * @param conn
   * @param objCoAssignmentBean
   */
    
  public String updAssignmentBillAccount(CoAssignmentBean objCoAssignmentBean,Connection conn)  throws Exception,SQLException {
   
   OracleCallableStatement cstmt = null;
   String strMessage = null;   
   
   String strSql = "BEGIN NP_ORDERS06_PKG.SP_UPD_ORDER_CO_ASSIGNMENT( ?, ?, ?, ?, ?, ?, ?, ?, ? ); end;";
   
	try{
		cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
		
		cstmt.setLong(1,objCoAssignmentBean.getNpcoassignmentid());
		cstmt.setString(2,objCoAssignmentBean.getNpphone());
		 
		 //NpbscscontractId
		 if( (objCoAssignmentBean.getNpbscscontractId() == null) || (objCoAssignmentBean.getNpbscscontractId().equals("")))
			cstmt.setNull(3, OracleTypes.NUMBER);
		 else
			cstmt.setLong(3, Long.parseLong(objCoAssignmentBean.getNpbscscontractId()));
			
		 //Npbscssncode	
		 if( (objCoAssignmentBean.getNpbscssncode() == null) || (objCoAssignmentBean.getNpbscssncode().equals("")))
			cstmt.setNull(4, OracleTypes.NUMBER);
		 else
			cstmt.setLong(4, Long.parseLong(objCoAssignmentBean.getNpbscssncode()));
			
		 //getNpbscspaymntrespcustomeridId	
		 if( (objCoAssignmentBean.getNpbscspaymntrespcustomeridId() == null) || (objCoAssignmentBean.getNpbscspaymntrespcustomeridId().equals("")))
			cstmt.setNull(5, OracleTypes.NUMBER);
		 else
			cstmt.setLong(5, Long.parseLong(objCoAssignmentBean.getNpbscspaymntrespcustomeridId()));
			
		//getNpnewsiteid	
		 if( (objCoAssignmentBean.getNpnewsiteid() == null) || (objCoAssignmentBean.getNpnewsiteid().equals("")) || (objCoAssignmentBean.getNpnewsiteid().equals("0")))
			cstmt.setNull(6, OracleTypes.NUMBER);
		 else
			cstmt.setLong(6, Long.parseLong(objCoAssignmentBean.getNpnewsiteid()));
	
		//getNpbscsbillingAccountId	
		 if( (objCoAssignmentBean.getNpbscsbillingAccountId() == null) || (objCoAssignmentBean.getNpbscsbillingAccountId().equals("")))
			cstmt.setNull(7, OracleTypes.NUMBER);
		 else
			cstmt.setLong(7, Long.parseLong(objCoAssignmentBean.getNpbscsbillingAccountId()));
	
		 //getNpbillaccnewid	
		 if( (objCoAssignmentBean.getNpbillaccnewid() == null) || (objCoAssignmentBean.getNpbillaccnewid().equals("")))
			cstmt.setNull(8, OracleTypes.NUMBER);
		 else
			cstmt.setLong(8, Long.parseLong(objCoAssignmentBean.getNpbillaccnewid()));
		 
		cstmt.registerOutParameter(9, Types.CHAR);
		cstmt.execute();
		strMessage = cstmt.getString(9);
	}
   catch (Exception e) {
		throw new Exception(e);
   }
   finally{	
		closeObjectsDatabase(null,cstmt,null);     
	}
   return strMessage;     
  }
    
 /**
   * Motivo:  Inserta en la tabla ORDERS.NP_CO_ASSIGNMENT
   * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
   * <br>Fecha: 27/10/2007
   * 
   * @return     HashMap 
   * @param      BillingAccountBean     BillingAccountBean
   * @param      Connection             conn
   * @throws java.sql.SQLException
   * @throws java.lang.Exception
   * @param conn
   * @param objCoAssignmentBean
   */
    
  public String deleteAssignmentBillAccount(CoAssignmentBean objCoAssignmentBean,Connection conn)  throws Exception,SQLException {
   
   OracleCallableStatement cstmt = null;
   String strMessage = null;   
   
   String strSql = "BEGIN NP_ORDERS06_PKG.SP_DEL_ORDER_CO_ASSIGNMENT( ?, ? ); end;";
   
	try{
		cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
		
		cstmt.setLong(1,objCoAssignmentBean.getNpcoassignmentid());
		cstmt.registerOutParameter(2, Types.CHAR);
		cstmt.execute();
		
		strMessage = cstmt.getString(2);
	}
   catch (Exception e) {
		throw new Exception(e);
   }
   finally{	
		closeObjectsDatabase(null,cstmt,null);
	}
   return strMessage;     
  }
    
  /**
   * Motivo:  Obtiene un listado de los bilings account
   * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
   * <br>Fecha: 01/08/2007
   * 
   * @return     HashMap 
   * @throws java.sql.SQLException
   * @throws java.lang.Exception
   * @param longOrderId
   * @param longCustomerId
   * @param longSiteId
   */
  public HashMap getAccountList(long longSiteId, long longCustomerId, long longOrderId)  throws Exception, SQLException {
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    Connection conn = null;
    String strMessage=null;
    HashMap hshResultado=new HashMap();
    ArrayList listado = new ArrayList();
    BillingAccountBean objAccount = null;
    System.out.println("****longSiteId Lee Rosales****: "+longSiteId);
    System.out.println("****longCustomerId****: "+longCustomerId);
    System.out.println("****longOrderId****: "+longOrderId);
    try{
      conn = Proveedor.getConnection();     
      String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_BA_NEW( ? , ? , ? , ? , ? ); end;";                    
      
      cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
      cstmt.setLong(1, longSiteId);
      cstmt.setLong(2, longCustomerId);        
      cstmt.setLong(3, longOrderId);
      cstmt.registerOutParameter(5, Types.CHAR);
      cstmt.registerOutParameter(4, OracleTypes.CURSOR);
      cstmt.execute();
      strMessage = cstmt.getString(5);
      
      if( strMessage == null ){
        rs = (ResultSet)cstmt.getObject(4);
        while (rs.next()) {
          objAccount = new BillingAccountBean();
          objAccount.setNpBillaccountNewId(rs.getLong("npbillaccnewid"));
          objAccount.setNpBillacCName(rs.getString("npbillaccname")); 
          objAccount.setNpSiteId(rs.getInt("npsiteid")); 
          listado.add(objAccount);
        }
      }
      
      //closeObjectsDatabase(conn,cstmt,rs);
      
      hshResultado.put("objArrayList",listado);
      hshResultado.put("strMessage",strMessage);  
    }catch (Exception e) {
      logger.error(formatException(e));
      hshResultado.put("strMessage", e.getMessage()); 
    }
    finally{
      try{
        closeObjectsDatabase(conn, cstmt, rs);  
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }               
    return hshResultado;
  }

    
  /**
   * Motivo: Registra la asignación de responsables de pago y cuentas de facturación
   * a un cliente para una determinada orden
   * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
   * <br>Fecha: 01/08/2007
   * 
   * @return     ArrayList 
   * @throws java.sql.SQLException
   * @throws java.lang.Exception
   * @param conn
   * @param baAssignmentBean
   */
	public String insertarAssignementAccount(BaAssignmentBean baAssignmentBean,Connection conn) throws Exception, SQLException {
		String strMessage      =   "";
		OracleCallableStatement cstmt = null;
    
		String strSql = "BEGIN NP_ORDERS06_PKG.SP_INS_ORDER_ITEM_ASSIGNMENT( ? , ? , ? , ? , ?,"+
                                                                       " ? , ? , ? , ?,  ?,"+
                                                                       " ?); end;";
		try{
																									
		 cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
		 cstmt.setLong(1, baAssignmentBean.getNporderid());
		 cstmt.setLong(2, baAssignmentBean.getNpdeviceid());
		 cstmt.setString(3, baAssignmentBean.getNpserviceid());
		 cstmt.setString(4,baAssignmentBean.getNpchargetype());
		 cstmt.setString(5, baAssignmentBean.getBillingAccountId());
		 cstmt.setString(6, baAssignmentBean.getNpbillaccnewid());
		 cstmt.setString(7, baAssignmentBean.getNpactivesiteid());
		 cstmt.setString(8, baAssignmentBean.getNpunknowsiteid());
		 cstmt.setDate(9, baAssignmentBean.getNpcreateddate());
		 cstmt.setString(10,baAssignmentBean.getNpcreatedby());
		 cstmt.registerOutParameter(11, Types.VARCHAR);
		 
		 cstmt.executeUpdate();
		 
		 strMessage = cstmt.getString(11);
		} 
		catch (Exception e) {
			throw new Exception(e);
		}
		finally{
			closeObjectsDatabase(null,cstmt,null);
		}			    		
		return strMessage;
  }
    
  /**
   * Motivo:  Obtiene un listado de los bilings account
   * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
   * <br>Fecha: 01/08/2007
   * 
   * @throws java.sql.SQLException
   * @throws java.lang.Exception
   * @return HashMap
   * @param iNpcustomerid
   */
  public HashMap getBillingAccountList(long iNpcustomerid) throws Exception,SQLException {
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    Connection conn = null;
    ArrayList listado = new ArrayList();
    String strMessage = null;
    BillingAccountBean objAccount = null;
    HashMap objHashMap = new HashMap();       
    try{
      conn = Proveedor.getConnection();    
      String strSql = "BEGIN NP_ORDERS06_PKG.SP_GET_ACCOUNT_BILLING( ? , ? , ? ); end;";
      
      cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
      cstmt.setLong(1, iNpcustomerid);
      cstmt.registerOutParameter(2, OracleTypes.CURSOR);
      cstmt.registerOutParameter(3, Types.CHAR);
      System.out.println("*********************************************************************");
      cstmt.execute();
      
      strMessage = cstmt.getString(3);
      
      if( strMessage == null){
        rs = (ResultSet)cstmt.getObject(2);
        
        while (rs.next()) {
            objAccount = new BillingAccountBean();
            objAccount.setNpBillaccountNewId(rs.getLong("BA_ID"));
            objAccount.setNpBillacCName(rs.getString("FACTURA"));            
            objAccount.setNpType(rs.getString("TIPO"));
            objAccount.setNpBillacCName2(rs.getString("billing_account_name"));
            listado.add(objAccount);
        }
      }
                  
      objHashMap.put("strMessage",strMessage);
      objHashMap.put("objArrayList",listado);
    }catch (Exception e) {
      logger.error(formatException(e));
      objHashMap.put("strMessage",e.getMessage());
    }
    finally{
      try{
        closeObjectsDatabase(conn, cstmt, rs);          
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }    
    
    
    return objHashMap;
  }


    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - FIN
     *  REALIZADO POR: Lee Rosales Crispín
     *  FECHA: 28/08/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
     
     
     
     /***********************************************************************
    ***********************************************************************
    ***********************************************************************
    *  INTEGRACION DE ORDENES Y RETAIL - INICIO
    *  REALIZADO POR: Carmen Gremios Cornelio
    *  FECHA: 28/08/2007
    ***********************************************************************
    ***********************************************************************
    ***********************************************************************/
    
  /**
   * Motivo:  Obtiene un listado de los bilings account
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 01/08/2007
   * 
   * @return     HashMap 
   * @throws java.sql.SQLException
   * @throws java.lang.Exception
   * @param lOrderId
   * @param lObjectId
   * @param strObjectType
   */
  public HashMap getAccountList(String strObjectType,long lObjectId,long lOrderId)  throws Exception, SQLException {
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   Connection conn = null;
   String strMessage=null;
   HashMap hshResultado=new HashMap();
   ArrayList listado = new ArrayList();
   BillingAccountBean objAccount = null;
   BillingContactBean objContact = null;  
   try{
    conn = Proveedor.getConnection();
     
     String strSql = "BEGIN NP_ORDERS07_PKG.SP_GET_BA_NEW_LST( ? , ? , ? , ? , ? ); end;";
     
     cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
     cstmt.setString(1, strObjectType);
     cstmt.setLong(2, lObjectId);
     cstmt.setLong(3, lOrderId);
     cstmt.registerOutParameter(4, Types.CHAR);
     cstmt.registerOutParameter(5, OracleTypes.CURSOR);
     cstmt.execute();
     strMessage = cstmt.getString(4);
     
     if( strMessage == null ){
       rs = (ResultSet)cstmt.getObject(5);
       while (rs.next()) {            
          objContact = new BillingContactBean();            
          objAccount = new BillingAccountBean();
          objAccount.setNpBillaccountNewId(rs.getLong(1));
          objContact.setNpbillaccnewid(rs.getLong(1));
          objAccount.setNpBillacCName(rs.getString(2));            
          objAccount.setNpBscsCustomerId(rs.getString(3));
          objContact.setNpTitle(rs.getString(4));
          objContact.setNpfname(rs.getString(5));
          objContact.setNplname(rs.getString(6));
          objContact.setNpjobtitle(rs.getString(7));
          objContact.setNpphonearea(rs.getString(8));
          objContact.setNpphone(rs.getString(9));            
          objContact.setNpaddress1(rs.getString(10)); 
          objContact.setNpaddress2(rs.getString(11)); 
          objContact.setNpcity(rs.getString(12));             
          objContact.setNpstate(rs.getString(13)); 
          objContact.setNpdepartment(rs.getString(14)); 
          objContact.setNpzipcode(rs.getString(15));    
          objAccount.setObjBillingContactB(objContact);
          objAccount.setNpBillaccountId(rs.getLong(18));    
          objAccount.setNpBscsSeq(rs.getString(19));//npbscsseq
          listado.add(objAccount);
       }
     }
              
     hshResultado.put("objCuentas",listado);
     hshResultado.put("strMessage",strMessage);  
   }catch (Exception e) {
      logger.error(formatException(e));
      hshResultado.put("strMessage", e.getMessage()); 
    }
    finally{
      try{
        closeObjectsDatabase(conn, cstmt, rs);  
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }    

   return hshResultado;
  }
    
  /**
   * Motivo:  Inserta un  billing account
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 20/09/2007
   * @throws java.sql.SQLException
   * @throws java.lang.Exception
   * @return String
   * @param conn
   * @param objAccount
   */
	public String insBillAccount(BillingAccountBean objAccount,java.sql.Connection conn)  throws Exception, SQLException {
		OracleCallableStatement cstmt = null;
		String strMessage = null;   
		BillingContactBean objContact=null;
		objContact=objAccount.getObjBillingContactB();       
		//DbmsOutput dbmsOutput = null;
		String strSql = 
		"BEGIN NP_ORDERS07_PKG.SP_INS_BA_NEW( ?, ?, ?, ?, ?, ?, ?, ?," +
                                    "?, ?, ?, ?, ?, ?, ?, ?," +
                                    "?, ?, ?, ?, ?); end;";
		try{												
			//dbmsOutput = new DbmsOutput(conn);
			//dbmsOutput.enable(1000000);												  
			cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
			  
			cstmt.setString(1, objAccount.getNpBillacCName());
			  
			if (objAccount.getObjCustomerB().getSwCustomerId()==0)
			  cstmt.setNull(2,Types.INTEGER);
			else
			  cstmt.setLong(2, objAccount.getObjCustomerB().getSwCustomerId());     
			  
			cstmt.setString(3, objContact.getNpTitle());
			cstmt.setString(4, objContact.getNpfname());
			cstmt.setString(5, objContact.getNplname());
			cstmt.setString(6, objContact.getNpjobtitle());
			cstmt.setString(7, objContact.getNpphonearea());
			cstmt.setString(8, objContact.getNpphone());
			cstmt.setString(9, objContact.getNpaddress1());
			cstmt.setString(10, objContact.getNpaddress2());
			cstmt.setString(11, objContact.getNpcity());
			cstmt.setString(12, objContact.getNpstate());            
			cstmt.setString(13, objContact.getNpdepartment());
			cstmt.setString(14, objContact.getNpzipcode());
			cstmt.setString(15, objAccount.getNpCreatedby());
			  
			if (objAccount.getNpOrderId()==0)
			 cstmt.setNull(16,Types.INTEGER);
			else
			 cstmt.setLong(16, objAccount.getNpOrderId());   
			  
			if (objAccount.getNpSiteId()==0)
			 cstmt.setNull(17,Types.INTEGER);
			else
			 cstmt.setLong(17, objAccount.getNpSiteId());        
			  
			cstmt.setString(18, objAccount.getNpBscsCustomerId());
			cstmt.setString(19, objAccount.getNpBscsSeq());
			cstmt.setLong(20,objAccount.getNpBillaccountNewId() );
			cstmt.registerOutParameter(21, Types.CHAR);
			cstmt.execute();
			//dbmsOutput.show();
			//dbmsOutput.close();
			strMessage = cstmt.getString(21);
		}
		catch (Exception e) {
			throw new Exception(e);	
		}
		finally{
			closeObjectsDatabase(null,cstmt,null);		
		}
   
		return strMessage;       
  }
    
  /**
  * Motivo:  Actualiza un billing account
  * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
  * <br>Fecha: 21/09/2007      
  * @param      objAccount
  * @param      strSwtype
  * @return     String 
  */
  public String updBillAccount(BillingAccountBean objAccount,java.sql.Connection conn ) throws SQLException {
   OracleCallableStatement cstmt = null;
   String strMessage =null;   
   BillingContactBean objContact=null;
   objContact=objAccount.getObjBillingContactB();
   //DbmsOutput dbmsOutput = null;
   try{
     String strSql = "BEGIN NP_ORDERS07_PKG.SP_UPD_BA_NEW(  ?, ?, ?, ?, ?, ?, ?, ?," +
                                                     "?, ?, ?, ?, ?, ?, ?, ?," +
                                                     "?, ?, ?, ?); end;";
       
       long npBillaccountNewId=objAccount.getNpBillaccountNewId();
       System.out.println("npBillaccountNewId (antes)"+ npBillaccountNewId);
       npBillaccountNewId=npBillaccountNewId-1;
       System.out.println("npBillaccountNewId (nuevo)"+ npBillaccountNewId);
       //dbmsOutput = new DbmsOutput(conn);
       //dbmsOutput.enable(1000000);		 
       cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
       cstmt.setLong(1, objAccount.getNpBillaccountNewId());
		 //cstmt.setLong(1, npBillaccountNewId);
       cstmt.setString(2, objAccount.getNpBillacCName());
       
       if (objAccount.getObjCustomerB().getSwCustomerId()==0)
          cstmt.setNull(3,Types.INTEGER);
       else
          cstmt.setLong(3, objAccount.getObjCustomerB().getSwCustomerId());     
         
       cstmt.setString(4, objContact.getNpTitle());
       cstmt.setString(5, objContact.getNpfname());
       cstmt.setString(6, objContact.getNplname());
       cstmt.setString(7, objContact.getNpjobtitle());
       cstmt.setString(8, objContact.getNpphonearea());
       cstmt.setString(9, objContact.getNpphone());
       cstmt.setString(10, objContact.getNpaddress1());
       cstmt.setString(11, objContact.getNpaddress2());
       cstmt.setString(12, objContact.getNpcity());
       cstmt.setString(13, objContact.getNpstate());            
       cstmt.setString(14, objContact.getNpdepartment());
       cstmt.setString(15, objContact.getNpzipcode());        
         
       if (objAccount.getNpOrderId()==0)
          cstmt.setNull(16,Types.INTEGER);
       else
          cstmt.setLong(16, objAccount.getNpOrderId());         
       
       if (objAccount.getNpSiteId()==0)
          cstmt.setNull(17,Types.INTEGER);
       else
          cstmt.setLong(17, objAccount.getNpSiteId());
       
       cstmt.setString(18, objAccount.getNpBscsCustomerId());
       cstmt.setString(19, objAccount.getNpBscsSeq());
         
       cstmt.registerOutParameter(20, Types.CHAR);
       cstmt.execute();
       //dbmsOutput.show();
		   //dbmsOutput.close();
       strMessage = cstmt.getString(20);
   }catch (Exception e) {
      logger.error(formatException(e));
      strMessage = e.getMessage(); 
    }
    finally{
      try{
         closeObjectsDatabase(null,cstmt,null); 
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }    

   return strMessage;     
  }
    
  /**
  * Motivo:  Elimina un Billing Account conestado 'Solicitado' de la tabla orders.np_ba_new
  * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
  * <br>Fecha: 25/09/2007
  *      
  * @param      lNewBillAccId
  * @return     String 
  */
  public String delBillAccount(long lNewBillAccId,java.sql.Connection conn) throws SQLException {
   OracleCallableStatement cstmt = null;
   String strMessage = null;
   try{
    String strSql = "BEGIN NP_ORDERS07_PKG.SP_DEL_BA_NEW( ?, ?); end;";
   
     cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
     cstmt.setLong(1, lNewBillAccId);    
     cstmt.registerOutParameter(2, Types.CHAR);
     cstmt.execute();
     strMessage = cstmt.getString(2);  
   }catch (Exception e) {
      logger.error(formatException(e));
      strMessage = e.getMessage(); 
    }
    finally{
      try{
         closeObjectsDatabase(null,cstmt,null); 
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }    
          
   return strMessage;     
    
  }
    
  /**
  * Motivo:  Elimina un Billing Account conestado 'Solicitado' de la tabla orders.np_co_assignment
  * <br>Realizado por: <a href="mailto:hugo.moreno@nextel.com.pe">Hugo Moreno</a>
  * <br>Fecha: 25/09/2007
  *      
  * @param      lNewBillAccId
  * @return     String 
  */
  public String delBillAccountAssign(long lNewBillAccId,long lOrderId,java.sql.Connection conn) throws Exception, SQLException {
   OracleCallableStatement cstmt = null;
   String strMessage = null;   
    
   //conn = Proveedor.getConnection();
   String strSql = "BEGIN NP_ORDERS06_PKG.SP_DEL_BA( ?, ?,?); end;";
   try{
		cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
		cstmt.setLong(1, lNewBillAccId); 
		cstmt.setLong(2, lOrderId); 
		cstmt.registerOutParameter(3, Types.CHAR);
		cstmt.execute();
		strMessage = cstmt.getString(3);
	}
	catch(Exception e){
		throw new Exception(e);	
   }
	finally{
		closeObjectsDatabase(null,cstmt,null);
   }
   return strMessage;     
  
  }
    
    
    
/**
* Motivo:  Obtiene un listado de contactos a los cuales se les puede crear una factura (Billing Account)
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 22/09/2007
* 
* @param      iNpcustomerid    
* @return     HashMap 
*/
 public HashMap getContactBillCreateList(long lNpcustomerid,long lNpSiteId)  throws Exception, SQLException {
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   Connection conn = null;
   String strMessage=null;
   HashMap hshReturn=new HashMap();
   ArrayList listado = new ArrayList();
   BillingContactBean objContactB = null;
   BillingAccountBean objAccount=null;
   CustomerBean objCustomer = null;
   try{
    conn = Proveedor.getConnection();
   
     String strSql = "BEGIN NP_ORDERS03_PKG.SP_GET_CONTACT_TO_CREATE_BILL( ?, ?, ?, ? ); end;";
     
     cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
     if (lNpcustomerid==0)
        cstmt.setNull(1, Types.INTEGER);
     else  
        cstmt.setLong(1, lNpcustomerid);
     if (lNpSiteId==0)  
        cstmt.setNull(2, Types.INTEGER);
     else  
        cstmt.setLong(2, lNpSiteId);        
     
     cstmt.registerOutParameter(3, OracleTypes.CURSOR);
     cstmt.registerOutParameter(4, Types.CHAR);
     cstmt.execute();
     strMessage = cstmt.getString(4);
     
     if( strMessage == null ){
       rs = (ResultSet)cstmt.getObject(3);
       while (rs.next()) {
          objContactB = new BillingContactBean();      
          objAccount = new BillingAccountBean();            
          objContactB.setNpfname(rs.getString(3));
          objContactB.setNpaddress1(rs.getString(4)); 
          objContactB.setNpTypeContact(rs.getString(5));
          objAccount.setNpBscsCustomerId(rs.getString(1));
          objAccount.setNpBscsSeq(rs.getString(2));
          objAccount.setObjBillingContactB(objContactB);         
           
          listado.add(objAccount);
       }
     }         
     hshReturn.put("strMessage",strMessage);
     hshReturn.put("arrListado",listado);  
   }catch (Exception e) {
      logger.error(formatException(e));
      hshReturn.put("strMessage",e.getMessage());
    }
    finally{
      try{
        closeObjectsDatabase(conn, cstmt, rs);  
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }    
   
   return hshReturn;      

}
        
/**
* Motivo:  Obtiene el detalle de un billing accoutn
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 23/09/2007
* 
* @param      iNpcustomerid        
* @return     HashMap 
*/
public HashMap getNewContactBilling(long lNpbillaccnewid)  
throws Exception,  SQLException {
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   Connection conn = null;        
   String strMessage=null;
   HashMap hshResult=new HashMap();
   BillingContactBean objContact = new BillingContactBean();
   BillingAccountBean objAccount = new BillingAccountBean();            
   CustomerBean objCustomer = null;
   try{
    conn = Proveedor.getConnection();
     
     String strSql = "BEGIN NP_ORDERS07_PKG.SP_GET_BA_NEW( ?, ?, ? ); end;";
     
     cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
     
     if (lNpbillaccnewid==0)
        cstmt.setNull(1, Types.INTEGER);
     else  
        cstmt.setLong(1, lNpbillaccnewid);  
     
     cstmt.registerOutParameter(2, Types.CHAR);  
     cstmt.registerOutParameter(3, OracleTypes.CURSOR);        
     cstmt.execute();
     strMessage = cstmt.getString(2);
     
     if( strMessage == null ){
       rs = (ResultSet)cstmt.getObject(3);
       if (rs.next()) {     
          objAccount.setNpBillaccountNewId(rs.getLong(1));            
          objAccount.setNpBillacCName(rs.getString(2));            
          objAccount.setNpBscsCustomerId(rs.getString(3));
          objContact.setNpTitle(rs.getString(4));
          objContact.setNpfname(rs.getString(5));
          objContact.setNplname(rs.getString(6));
          objContact.setNpjobtitle(rs.getString(7));
          objContact.setNpphonearea(rs.getString(8));
          objContact.setNpphone(rs.getString(9));            
          objContact.setNpaddress1(rs.getString(10)); 
          objContact.setNpaddress2(rs.getString(11)); 
          objContact.setNpcity(rs.getString(12));             
          objContact.setNpstate(rs.getString(13)); 
          objContact.setNpdepartment(rs.getString(14)); 
          objContact.setNpzipcode(rs.getString(15));  
          objAccount.setObjBillingContactB(objContact);                    
       }
     }
       
     hshResult.put("strMessage",strMessage);
     hshResult.put("objAccount",objAccount);         
   }catch (Exception e) {
      logger.error(formatException(e));
      hshResult.put("strMessage", e.getMessage()); 
    }
    finally{
      try{
        closeObjectsDatabase(conn, cstmt, rs);  
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }   
   
   return hshResult;      

}
    
  /**
  * Motivo:  Obtiene un listado de los bilings account
  * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
  * <br>Fecha: 17/10/2007   
  * @return     long 
  */
  public long getNewBillAccId()  throws Exception, SQLException {   
   Connection conn = null; 
   OracleCallableStatement cstmt = null;
   long lNewBillAccId=0;
   try{
     String strSql = " { ? = call NP_ORDERS07_PKG.FX_GET_NPBILLACCNEWID } ";  
   
     conn = Proveedor.getConnection();      
     cstmt = (OracleCallableStatement)conn.prepareCall(strSql);       
     cstmt.registerOutParameter(1,OracleTypes.NUMBER);                  
     cstmt.executeUpdate();
     
     lNewBillAccId = cstmt.getInt(1);
   }catch (Exception e) {
      logger.error(formatException(e));
    }
    finally{
      try{
        closeObjectsDatabase(conn, cstmt, null);  
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    } 
  
   return lNewBillAccId;
  }           
    /***********************************************************************
    ***********************************************************************
    ***********************************************************************
    *  INTEGRACION DE ORDENES Y RETAIL - FIN
    *  REALIZADO POR: Carmen Gremios Cornelio
    *  FECHA: 28/08/2007
    ***********************************************************************
    ***********************************************************************
    ***********************************************************************/
    
    
   /**
   * Motivo:  Obtiene un listado de los bilings account en base al customerid del cliente en CRM
   * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
   * <br>Fecha: 09/12/2007
   * 
   * @param      iNpcustomerid
   * @param      iNpsiteid
   * @param      strMessage
   * @return     ArrayList 
  */
  public HashMap getBillingAccountListNew(long iNpcustomerid,long iNpsiteid) throws Exception,SQLException {
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    Connection conn = null;
    ArrayList listado = new ArrayList();
    String strMessage = null;
    BillingAccountBean objAccount = null;
    HashMap objHashMap = new HashMap();
    try{
      conn = Proveedor.getConnection();
    
      String strSql = "BEGIN NP_ORDERS16_PKG.SP_GET_ACCOUNT_BILLING_NEW( ? , ? , ?, ? ); end;";
      
      cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
      cstmt.setLong(1, iNpcustomerid);
      if( iNpsiteid == 0 )
      cstmt.setNull(2,OracleTypes.NUMBER);
      else
      cstmt.setLong(2,iNpsiteid);
      cstmt.registerOutParameter(3, OracleTypes.CURSOR);
      cstmt.registerOutParameter(4, Types.CHAR);
      
      cstmt.execute();        
      strMessage = cstmt.getString(4);
      
      if( strMessage ==  null ){
        rs = (ResultSet)cstmt.getObject(3);
        while (rs.next()) {
          objAccount = new BillingAccountBean();
          objAccount.setNpBillaccountNewId(rs.getLong("BA_ID"));
          objAccount.setNpBillacCName2(rs.getString("BA_NAME"));
          listado.add(objAccount);
        }
      }           
      objHashMap.put("strMessage",strMessage);
      objHashMap.put("objArrayList",listado);  
    }catch (Exception e) {
      logger.error(formatException(e));
      objHashMap.put("strMessage",e.getMessage());
    }
    finally{
      try{
        closeObjectsDatabase(conn, cstmt, rs);  
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }        
    
    return objHashMap;
  }
  
  /***************************************************************************************
   * *************************************************************************************
   * *************************************************************************************
   */
}
    

         

