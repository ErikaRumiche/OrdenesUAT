package pe.com.nextel.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import pe.com.nextel.bean.SectionDinamicBean;
import pe.com.nextel.bean.SpecificationBean;


public class CategoryDAO extends GenericDAO{
  
/**
  Method : getSpecificationData
  Purpose: Obtener la lista de SubCategorias
  Developer       Fecha       Comentario
  =============   ==========  ======================================================================
  Lee Rosales     01/08/2007  Creación
  MMora           23/09/2009  Modificación Masivos
 */
     
 public HashMap getSpecificationData(long idSpecification , String strGeneratorType) throws Exception,SQLException{
    
  ArrayList list = new ArrayList();
  HashMap hshResultado  = new HashMap();
  Connection conn = null; 
  OracleCallableStatement cstmt = null;
  ResultSet rs = null;
  String strMessage = null;
  
  try{
    //String strSql = "BEGIN ORDERS.NP_PRUEBA_MASSIVE.SP_GET_SPEC_SECTION_LST(?, ?, ?, ?); END;";
    String strSql = "BEGIN ORDERS.NP_ORDERS01_PKG.SP_GET_SPEC_SECTION_LST(?, ?, ?, ?); END;";
    conn = Proveedor.getConnection();
    //cstmt = ((ResultSet)cstmt.getObjectCallableStatement)conn.prepareCall(strSql);
    cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
    
    cstmt.setLong(1,idSpecification);
    cstmt.setString(2,strGeneratorType);
    cstmt.registerOutParameter(3, Types.CHAR);
    cstmt.registerOutParameter(4, OracleTypes.CURSOR );
   
    cstmt.executeUpdate();
   
    strMessage = cstmt.getString(3); 
    
    if( strMessage == null ){
      rs = (ResultSet)cstmt.getObject(4);        
  
      while (rs.next()) {
       SectionDinamicBean scb = new  SectionDinamicBean();
       scb.setNppagesectionid(rs.getLong(1));
       scb.setNppagenpname(rs.getString(2));
       scb.setNpeventname(rs.getString(3));
       scb.setNpeventhandler(rs.getString(4));
       scb.setNptypeobject(rs.getString(5));
       scb.setNpobjectname(rs.getString(6));
       scb.setNpbusinessobject(rs.getString(7));
       scb.setNpSpecificationId(idSpecification);
       list.add(scb);
      }
    }
        
    hshResultado.put("objArrayList",list);
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
* Motivo: Obteniene el detalle de la categoria
* <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
* <br>Fecha: 03/07/2007
* @param     lSpecificationId
* @param     conn
* @return    SpecificationBean
*/   
  public HashMap getSpecificationDetail(long lSpecificationid, Connection conn) throws Exception,SQLException{
  
   pe.com.nextel.bean.CustomerBean custbCustomer = null;
   //Connection conn = null;
   //(ResultSet)cstmt.getObjectCallableStatement cstmt = null;
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   String strMessage=null;
   HashMap hshData=new HashMap();
   SpecificationBean objSpecificationB = new SpecificationBean();
   String sqlStr = "BEGIN NP_ORDERS07_PKG.SP_GET_CATEGORY_DETAIL(?, ?, ?); END;";
	try{
		cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
		cstmt.setLong(1, lSpecificationid);
		cstmt.registerOutParameter(2, 1);
		cstmt.registerOutParameter(3, -10);
		cstmt.execute();
		
		strMessage = cstmt.getString(2);
		
		if( strMessage == null ){
		 rs = (ResultSet)cstmt.getObject(3);
		 if(rs.next()){
			objSpecificationB.setNpSpecificationId(rs.getLong(1));
			objSpecificationB.setNpType(rs.getString(2));
			objSpecificationB.setNpSpecification(rs.getString(3));
			objSpecificationB.setNpAllowUpdateOwnerBy(rs.getString(4));
			objSpecificationB.setNpAllowAdditionalService(rs.getString(5));
			objSpecificationB.setNpAllowConsignment(rs.getString(6));
			objSpecificationB.setNpAllowbagpooling(rs.getString(7));
			objSpecificationB.setNpAllowInputDatasite(rs.getString(8));
			objSpecificationB.setNpRasprocess(rs.getString(9));
			objSpecificationB.setNpGrossprocess(rs.getString(10));
			objSpecificationB.setNpUpdatesalesbscs(rs.getString(11));
			objSpecificationB.setNpExceptionvalidation(rs.getInt(12));
			objSpecificationB.setNpExceptionType(rs.getString(13));
			objSpecificationB.setNpExceptionDetail(rs.getString(14));
			objSpecificationB.setNpInvoice(rs.getInt(15));
			objSpecificationB.setNpPicking(rs.getInt(16));
			objSpecificationB.setNpTelecomunication(rs.getString(17));
			objSpecificationB.setNpBpelflowgroup(rs.getString(18));
			objSpecificationB.setNpPricetype(rs.getString(19));
			objSpecificationB.setNpValiditem(rs.getInt(20));
		 }
		}
	}
	catch(Exception e){
		throw new Exception(e);
	}
	finally{
		closeObjectsDatabase(null,cstmt,rs);
	}	
   hshData.put("objSpecifBean",objSpecificationB);
   hshData.put("strMessage",strMessage);
   return hshData;
  }


    
/**
* Motivo: Obteniene el detalle de la categoria
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 03/07/2007
* @param     lSolutionId           
* @return    SpecificationBean
*/   
  public HashMap getSpecificationDetail(long lSpecificationid) throws SQLException{
  
   pe.com.nextel.bean.CustomerBean custbCustomer = null;
   Connection conn = null;
   //(ResultSet)cstmt.getObjectCallableStatement cstmt = null;
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   String strMessage=null;
   HashMap hshData=new HashMap();
   SpecificationBean objSpecificationB = new SpecificationBean();
   try{
     String sqlStr = "BEGIN NP_ORDERS07_PKG.SP_GET_CATEGORY_DETAIL(?, ?, ?); END;";
     conn = Proveedor.getConnection();
     //cstmt = ((ResultSet)cstmt.getObjectCallableStatement)conn.prepareCall(sqlStr);
     cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
     cstmt.setLong(1, lSpecificationid);
     cstmt.registerOutParameter(2, 1);
     cstmt.registerOutParameter(3, -10);
     cstmt.execute();
     
     strMessage = cstmt.getString(2);
     
     if( strMessage == null ){
      rs = (ResultSet)cstmt.getObject(3);
      if(rs.next()){
        objSpecificationB.setNpSpecificationId(rs.getLong(1));
        objSpecificationB.setNpType(rs.getString(2));
        objSpecificationB.setNpSpecification(rs.getString(3));
        objSpecificationB.setNpAllowUpdateOwnerBy(rs.getString(4));
        objSpecificationB.setNpAllowAdditionalService(rs.getString(5));
        objSpecificationB.setNpAllowConsignment(rs.getString(6));
        objSpecificationB.setNpAllowbagpooling(rs.getString(7));
        objSpecificationB.setNpAllowInputDatasite(rs.getString(8));
        objSpecificationB.setNpRasprocess(rs.getString(9));
        objSpecificationB.setNpGrossprocess(rs.getString(10));
        objSpecificationB.setNpUpdatesalesbscs(rs.getString(11));
        objSpecificationB.setNpExceptionvalidation(rs.getInt(12));
        objSpecificationB.setNpExceptionType(rs.getString(13));
        objSpecificationB.setNpExceptionDetail(rs.getString(14));
        objSpecificationB.setNpInvoice(rs.getInt(15));
        objSpecificationB.setNpPicking(rs.getInt(16));
        objSpecificationB.setNpTelecomunication(rs.getString(17));
        objSpecificationB.setNpBpelflowgroup(rs.getString(18));
        objSpecificationB.setNpPricetype(rs.getString(19));
        objSpecificationB.setNpValiditem(rs.getInt(20));
      }
     }
     hshData.put("objSpecifBean",objSpecificationB);
     hshData.put("strMessage",strMessage);
   }catch (Exception e) {
      logger.error(formatException(e));      
      hshData.put("strMessage", e.getMessage()); 
    }
    finally{
      try{
        closeObjectsDatabase(conn, cstmt, rs);  
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    } 
   
   return hshData;
  }

}
