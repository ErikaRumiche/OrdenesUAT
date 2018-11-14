package pe.com.nextel.dao;

import java.lang.Exception;
import java.lang.String;
import java.lang.System;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import oracle.sql.ARRAY;
import oracle.sql.NUMBER;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import org.apache.commons.lang.StringUtils;

import pe.com.nextel.bean.*;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;


/**
 * Motivo: Clase DAO que contiene acceso a la BBDD comunes para Órdenes y Retail.
 * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>,
 * <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>,
 * <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
 * <br>Fecha: 28/08/2007
 * @see GenericDAO
 */
public class GeneralDAO extends GenericDAO {

  /***********************************************************************     
   ***********************************************************************
   *  ORDENES - INICIO
   *  REALIZADO POR: Lee Rosales Crispin
   *  FECHA: 30/10/2007
   ***********************************************************************     
   ***********************************************************************/ 
   
   /**
    * Motivo: Obtiene el formato de un número telefónico según Renumbering, Worldnumber y digito adicional
    * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
    * <br>Fecha: 16/12/2007
    * @param     long lngSalesmanId   
    * @return    HashMap objHashMap 
    */
    public HashMap getDealerBySalesman(long lngSalesmanId) throws Exception,SQLException{
       HashMap  objHashMap = new HashMap();
       Connection conn = null; 
       OracleCallableStatement cstm = null;
       String strMessage = null;
       String strDealerName = "";     
       String sqlStr = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_DEALER_BY_SALESMAN(?,?,?); END;";
       try{   
          conn = Proveedor.getConnection();          
          cstm = (OracleCallableStatement) conn.prepareCall(sqlStr);                   
          cstm.setLong(1,lngSalesmanId);
          cstm.registerOutParameter(2,OracleTypes.VARCHAR);
          cstm.registerOutParameter(3,OracleTypes.VARCHAR);         
          cstm.executeUpdate();          
          strMessage    = cstm.getString(3);
          strDealerName = cstm.getString(2);
          
          objHashMap.put("strMessage",strMessage);
          objHashMap.put("strDealerName",strDealerName);
       }
       catch(Exception e){
          throw new Exception(e);
       }
       finally{
			closeObjectsDatabase(conn, cstm, null);
          /*if (cstm != null)
              cstm.close();            
          if (conn != null)
              conn.close();*/
       }
       return objHashMap;
    }    
    
   /**
    * Motivo: Obtiene el formato de un número telefónico según Renumbering, Worldnumber y digito adicional
    * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
    * <br>Fecha: 30/10/2007
    * @param     String strPhone  
    * @param     String strType       
    * @return    String strPhoneValue 
   **/
    public String getWorldNumber(String strPhone,String strType) throws Exception,SQLException{
       
       Connection conn = null; 
       OracleCallableStatement cstm = null;
       String strPhoneValue = "";
     
       String sqlStr = "{ ? = call WEBSALES.FXI_WORLD_NUMBER(?,?)}";
       try{   
          conn = Proveedor.getConnection();
             
          cstm = (OracleCallableStatement) conn.prepareCall(sqlStr);
                     
          cstm.registerOutParameter(1,Types.VARCHAR);
          cstm.setString(2,strPhone);
          cstm.setString(3,strType);
             
          cstm.executeUpdate();
             
          strPhoneValue = cstm.getString(1);
       }
       catch(Exception e){
          throw new Exception(e);
       }
       finally{
			closeObjectsDatabase(conn, cstm, null);
          /*if (cstm != null)
             cstm.close();            
          if (conn != null)
             conn.close();*/
       }
       return strPhoneValue;                                                                
    }    
    
    /**
       Method : getComboList
       Purpose: Lista un combo con parámetros de la np_table
       Developer       Fecha       Comentario
       =============   ==========  ======================================================================
       Lee Rosales     26/06/2007  Creación
    */
    public HashMap getComboList(String av_datatable)  throws Exception,SQLException {
       HashMap objHashMap = new HashMap();
       ArrayList list = new ArrayList();
       Connection conn = null; 
       ResultSet rs=null;
       String strMessage  = null;
       OracleCallableStatement cstm = null;                 
       String sqlStr = "BEGIN WEBSALES.NPSL_GENERAL_PKG.SP_GET_NPTABLE_LIST(?,?,?); END;";
       try{
          conn = Proveedor.getConnection();          
          cstm = (OracleCallableStatement) conn.prepareCall(sqlStr);                   
          cstm.setString(1,av_datatable);
          cstm.registerOutParameter(2, Types.CHAR);
          cstm.registerOutParameter(3, OracleTypes.CURSOR);          
          cstm.executeUpdate();
          rs = (ResultSet)cstm.getObject(3);
          strMessage = cstm.getString(2);          
           while (rs.next()) {                
               HashMap h = new HashMap();             
               h.put("wn_npvalue",rs.getString(1)+"");                 
               h.put("wv_npvaluedesc",rs.getString(2)==null?"":rs.getString(2));                         
               h.put("wv_mensaje","");                   
               list.add(h);          
           }
        }                    
        catch(Exception e){
           throw new Exception(e);
        }
        finally{
			closeObjectsDatabase(conn, cstm, rs);
           /*if (rs != null)
               rs.close(); 
           if (cstm != null)
               cstm.close();            
           if (conn != null)
               conn.close();*/
          }          
       objHashMap.put("objArrayList",list);
       objHashMap.put("strMessage",strMessage);
     
       return objHashMap;
                                                                       
    }    

    /**
       Method : getComboRegionList
       Purpose: Lista un combo de region List
       Developer       Fecha       Comentario
       =============   ==========  ======================================================================
       Lee Rosales     28/06/2007  Creación
    */
	public HashMap getComboRegionList() throws SQLException, Exception {
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
		ArrayList arrComboRegionList = new ArrayList();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage = null;         
        String sqlStr = "BEGIN WEBSALES.NPSL_GENERAL_PKG.SP_GET_REGION_LIST(?,?); END;";
        try{
           conn = Proveedor.getConnection();
           cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
           cstmt.registerOutParameter(1, OracleTypes.CURSOR);
           cstmt.registerOutParameter(2, Types.CHAR);
           cstmt.executeUpdate();
           rs = (ResultSet)cstmt.getObject(1);
           strMessage = cstmt.getString(2);
           while (rs.next()) {
              DominioBean objDominioBean = new DominioBean(); 
              int i = 1;
              objDominioBean.setValor(StringUtils.defaultString(rs.getString(i++)));              
              objDominioBean.setDescripcion(StringUtils.defaultString(rs.getString(i++)));    
              arrComboRegionList.add(objDominioBean);
           }
        }
        catch(Exception e){
           throw new Exception(e);
        }
        finally{
		    closeObjectsDatabase(conn, cstmt, rs);
        }
		hshDataMap.put("arrComboRegionList", arrComboRegionList);
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;                                                             
    }    
    
  /***********************************************************************     
   ***********************************************************************
   *  ORDENES - FIN
   *  REALIZADO POR: Lee Rosales Crispin
   *  FECHA: 30/10/2007
   ***********************************************************************     
   ***********************************************************************/ 
    

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
* Motivo: Obteniene un listado de valores de la tabla np_table según el nombre de la tabla y el status
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 11/07/2007
* @param     strParamName   Ej. PAY_FORM
* @param     strParamStatus Ej. 1       
* @return    ArrayList 
*/
public  HashMap getTableList(String strParamName, 
                                String strParamStatus) throws SQLException, Exception{
                                
   ArrayList list = new ArrayList();
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   Connection conn=null;
   String strMessage = null;
   HashMap hshData =new  HashMap();
   HashMap h=null;
   String sqlStr = "BEGIN WEBSALES.SPI_GET_PARAMS_LIST(?, ?, ?, ?); END;";
   try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);      
      cstmt.setString(1, strParamName);
      cstmt.setString(2, strParamStatus);
      cstmt.registerOutParameter(3, OracleTypes.CURSOR);
      cstmt.registerOutParameter(4, Types.CHAR);      
      cstmt.execute();      
      strMessage = cstmt.getString(4);      
      if( strMessage == null){      
         rs = (ResultSet)cstmt.getObject(3);         
         while (rs.next()) {         
            h = new HashMap();
            h.put("wv_npValue", rs.getString(1));
            h.put("wv_npValueDesc", rs.getString(2));
            h.put("wv_npTag1", rs.getString(3));
            h.put("wv_npTag2", rs.getString(4));
            h.put("wn_npOrder", rs.getInt(5) + "");            
            list.add(h);
         }      
      }
   }
   catch(Exception e){
      throw new Exception(e);
   }
   finally{
		closeObjectsDatabase(conn, cstmt, rs);
      /*if (rs != null)
         rs.close();
      if (cstmt != null)
         cstmt.close();   
      if (conn != null)
      conn.close();
		*/
   }
   if (strMessage!=null && strMessage.startsWith(Constante.SUCCESS_ORA_RESULT))
      strMessage=null;      
   
   hshData.put("strMessage",strMessage);
   hshData.put("arrTableList",list);
   
   return hshData;
}
            

/**
* Motivo: Obteniene el listado de los vendedores
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 11/07/2007
* @param     iUserId       Ej. PAY_FORM
* @param     iAppId        Ej. 1       
* @return    ArrayList 
*/
  public  HashMap getSalesList(long iUserId, int iAppId)  throws SQLException, Exception{
    ArrayList list = new ArrayList();
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    String strMessage=null;
    HashMap h =null;
    HashMap hshData=new HashMap();
    String sqlStr = "BEGIN ORDERS.NP_ORDERS01_PKG.SP_GET_SALES_LIST(?, ?, ?, ?); END;";
    try{
       conn = Proveedor.getConnection();       
       cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
       cstmt.setLong(1, iUserId);
       cstmt.setInt(2, iAppId);      
       cstmt.registerOutParameter(3, Types.CHAR);
       cstmt.registerOutParameter(4, OracleTypes.CURSOR);       
       cstmt.executeUpdate();       
       strMessage = cstmt.getString(3);
       rs = (ResultSet)cstmt.getObject(4);       
       while (rs.next()) {
         h = new HashMap();
         h.put("wn_npCampo01", rs.getInt(1) + "");
         h.put("wv_npCampo02", rs.getString(2));
         h.put("wv_npCampo03", rs.getString(3));                         
         list.add(h);
       }
    }
    catch(Exception e){
       throw new Exception(e);
    }
    finally{
		closeObjectsDatabase(conn, cstmt, rs);
    }   
    if (strMessage!=null && strMessage.startsWith(Constante.SUCCESS_ORA_RESULT))
      strMessage=null;
    
    hshData.put("strMessage",strMessage);
    hshData.put("arrSalesList",list);
    
    return hshData;
  }
    
/**
* Motivo: Obteniene el listado de los Representantes de Call Center
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 16/07/2007      
* @return    HashMap 
*/
public  HashMap getRepresentantesCCList()
throws SQLException, Exception{

   ArrayList list = new ArrayList();   
   HashMap hshData=new HashMap();
   HashMap h=null;
   String strMessage=null;
   Connection conn = null;
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   
   String sqlStr = "BEGIN WEBSALES.SPI_GET_CALLCENTER_LST(?, ?); END;";
   try{
      conn = Proveedor.getConnection();
      
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      
      cstmt.registerOutParameter(1, OracleTypes.CURSOR);
      cstmt.registerOutParameter(2, Types.CHAR);
      
      cstmt.execute();
      
      strMessage = cstmt.getString(2);
      rs = (ResultSet)cstmt.getObject(1);
      
      while (rs.next()) {
         h = new HashMap();           
         h.put("wn_dealer", rs.getString(1));           
        
         list.add(h);
      }
   }
   catch(Exception e){
      throw new Exception(e);
   }
   finally{
		closeObjectsDatabase(conn, cstmt, rs);
      /*if (rs != null)
         rs.close();
      if (cstmt != null)
         cstmt.close();   
      if (conn != null)
         conn.close();*/
   }   
   if (strMessage!=null && strMessage.startsWith(Constante.SUCCESS_ORA_RESULT))
      strMessage=null;
   hshData.put("strMessage",strMessage);
   hshData.put("arrRepreCCList",list);
   return hshData;
}
    
    
/**
* Motivo: Obtener la lista de Cargos
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 18/09/2007      
* @return    HashMap 
*/
public  HashMap getTitleList()
throws SQLException, Exception{
   HashMap hshData=new HashMap();
   String strMessage=null;
   HashMap h=null;
   ArrayList list = new ArrayList();
   Connection conn = null;
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   
   String sqlStr = "BEGIN WEBSALES.SPI_GET_TITLE_LIST(?, ?); END;";  
   try{
      conn = Proveedor.getConnection();
      
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      
      cstmt.registerOutParameter(1, OracleTypes.CURSOR);
      cstmt.registerOutParameter(2, Types.CHAR);
      
      cstmt.execute();
      
      strMessage = cstmt.getString(2);
      rs = (ResultSet)cstmt.getObject(1);
      
      while (rs.next()) {
         h = new HashMap();           
         h.put("jobtitlteId", rs.getString(1));           
         h.put("descripcion", rs.getString(2));           
         
         list.add(h);
      }
   }
   catch(Exception e){
       throw new Exception(e);   
   }
   finally{
		closeObjectsDatabase(conn, cstmt, rs);
      /*if (rs != null)
         rs.close();
      if (cstmt != null)
         cstmt.close();   
      if (conn != null)
         conn.close();*/
   }      
   if (strMessage!=null && strMessage.startsWith(Constante.SUCCESS_ORA_RESULT))
      strMessage=null;      
   
   hshData.put("strMessage",strMessage);
   hshData.put("arrTitleList",list);
   
   return hshData;
}


    
/**
* Motivo: Obtener Descripcion de Cargo
* <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
* <br>Fecha: 08/04/2008      
* @return    HashMap 
*/
public  String getTitle(String strJobTitleId)
throws SQLException, Exception{
   String strMessage=null;
   String strJobTitle=null;
   Connection conn = null;
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   
   String sqlStr = "BEGIN WEBSALES.SPI_GET_TITLE_LIST(?, ?); END;";  
   try{
      conn = Proveedor.getConnection();      
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);      
      cstmt.registerOutParameter(1, OracleTypes.CURSOR);
      cstmt.registerOutParameter(2, Types.CHAR);      
      cstmt.execute();      
      strMessage = cstmt.getString(2);
      rs = (ResultSet)cstmt.getObject(1);      
      while (rs.next()) {
         if(rs.getString(1).equals(strJobTitleId)){
           strJobTitle=rs.getString(2);
         }        
      }
   }
   catch(Exception e){
       throw new Exception(e);
   }
   finally{
		closeObjectsDatabase(conn, cstmt, rs);
      /*if (rs != null)
         rs.close();
      if (cstmt != null)
         cstmt.close();   
      if (conn != null)
         conn.close();*/
   }      
   return strJobTitle;

}

    
/**
* Motivo: Obtener la lista de Departamentos, Provincias y Distritos
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 18/09/2007
* @return    HashMap 
*/
public  HashMap getDistDeparProvList()
throws SQLException, Exception{
   HashMap hshData=new HashMap();
   String strMessage=null;
   HashMap h=null;
   ArrayList list = new ArrayList();
   Connection conn = null;
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   
   String sqlStr = "BEGIN WEBSALES.SPI_GET_DISTRICT_LIST2(?, ?); END;";
   try{
      conn = Proveedor.getConnection();      
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);      
      cstmt.registerOutParameter(1, OracleTypes.CURSOR);
      cstmt.registerOutParameter(2, Types.CHAR);      
      cstmt.execute();      
      strMessage = cstmt.getString(2);
      rs = (ResultSet)cstmt.getObject(1);      
      while (rs.next()) {
         h = new HashMap();           
         h.put("dpto", rs.getString(1));           
         h.put("prov", rs.getString(2));  
         h.put("dist", rs.getString(3));  
         h.put("dptoid", rs.getInt(4)+"");  
         h.put("provid", rs.getInt(5)+"");  
         h.put("distid", rs.getInt(6)+"");  
         h.put("cpostid", rs.getString(7));          
         list.add(h);
      }
   }
   catch(Exception e){
      throw new Exception(e);
   }
   finally{
		closeObjectsDatabase(conn, cstmt, rs);
      /*if (rs != null)
         rs.close();
      if (cstmt != null)
         cstmt.close();   
      if (conn != null)
         conn.close();*/
   }   
   if (strMessage!=null && strMessage.startsWith(Constante.SUCCESS_ORA_RESULT))
      strMessage=null;
      
   hshData.put("strMessage",strMessage);
   hshData.put("arrListado",list);
   
   return hshData;
}
    
/**
* Motivo: Obtiene lista de provincia y distrito según dpto dado el tipo de elemnto a listar '1': Provincia  '2': Distrito
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 19/09/2007
* @param     sDptoId
* @param     sProvId
* @param     sFlag
* @return    ArrayList 
*/
public  HashMap getUbigeoList(String sDptoId,String sProvId,String sFlag)
throws SQLException, Exception{

   ArrayList list = new ArrayList();
   HashMap hshData=new HashMap();
   String strMessage=null;   
   Connection conn = null;
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   
   String sqlStr = "BEGIN WEBSALES.SPI_GET_PROV_DIST_LIST2(?, ?, ?, ?, ?); END;";
   try{
      conn = Proveedor.getConnection();      
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);      
      cstmt.setString(1, sDptoId);
      cstmt.setString(2, sProvId);
      cstmt.setString(3, sFlag);
      cstmt.registerOutParameter(4, OracleTypes.CURSOR);
      cstmt.registerOutParameter(5, Types.CHAR);      
      cstmt.execute();      
      strMessage = cstmt.getString(5);
      rs = (ResultSet)cstmt.getObject(4);      
      while (rs.next()) {      
         HashMap h = new HashMap();           
         h.put("codigo", rs.getString(1));           
         h.put("nombre", rs.getString(2));             
         h.put("cpostid", rs.getString(3));  
         list.add(h);
      }
   }
   catch(Exception e){
       throw new Exception(e);
   }
   finally{
		closeObjectsDatabase(conn, cstmt, rs);
      /*if (rs != null)
         rs.close();
      if (cstmt != null)
         cstmt.close();   
      if (conn != null)
         conn.close();*/
   }      
   if (strMessage!=null && strMessage.startsWith(Constante.SUCCESS_ORA_RESULT))
      strMessage=null;      
      
   hshData.put("strMessage",strMessage);
   hshData.put("arrListado",list);   
   
   return hshData;
}

/**
* Motivo: Obtiene lista de provincia y distrito según dpto dado el tipo de elemnto a listar '1': Provincia  '2': Distrito
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 19/10/2007
* @param     iDptoId
* @param     iProvId
* @param     sFlag
* @return    HashMap 
*/
public  HashMap getUbigeoList(int iDptoId,int iProvId,String sFlag)
throws SQLException, Exception{

   ArrayList list = new ArrayList();
   
   Connection conn = null;
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   HashMap hshResult=new HashMap();
   String strMessage=null;
   String sqlStr = "BEGIN WEBSALES.SPI_GET_PROV_DIST_LIST3(?, ?, ?, ?, ?); END;";
   try{
      conn = Proveedor.getConnection();
      
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      
      cstmt.setInt(1, iDptoId);
      cstmt.setInt(2, iProvId);
      cstmt.setString(3, sFlag);
      cstmt.registerOutParameter(4, OracleTypes.CURSOR);
      cstmt.registerOutParameter(5, Types.CHAR);
      
      cstmt.execute();
      
      strMessage = cstmt.getString(5);
      rs = (ResultSet)cstmt.getObject(4);
      
      while (rs.next()) {
         
         HashMap h = new HashMap();           
         h.put("ubigeo", rs.getString(1));           
         h.put("codigo", rs.getString(2));           
         h.put("nombre", rs.getString(3));             
         h.put("cpostid", rs.getString(4));    
         h.put("regionid", rs.getString(5)); 
         list.add(h);
      }
   }
   catch(Exception e){
      throw new Exception(e);
   }
   finally{
		closeObjectsDatabase(conn, cstmt, rs);
      /*if (rs != null)
         rs.close();
      if (cstmt != null)
         cstmt.close();   
      if (conn != null)
         conn.close();*/
   }      
   if (strMessage!=null && strMessage.startsWith(Constante.SUCCESS_ORA_RESULT))
      strMessage=null;      
   
   hshResult.put("arrUbigeoList",list);
   hshResult.put("strMessage",strMessage);       
   return hshResult;
}
    
    
/**
* Motivo: Obtiene un listado de Codigos de Area
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 20/09/2007
* @param     strAreaName
* @param     strAreaCode
* @param     iCount      
* @return    HashMap 
*/
public  HashMap getAreaCodeList(String strAreaName,String strAreaCode)
throws SQLException, Exception{

   ArrayList list = new ArrayList();
   HashMap hshData=new HashMap();
   String strMessage=null;          
   Connection conn = null;
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   int iCount=0;   
   String sqlStr = "BEGIN WEBSALES.SPI_GET_AREA_CODE_LIST(?, ?, ?, ?, ?); END;";
   try{
      conn = Proveedor.getConnection();   
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);   
      cstmt.setString(1, strAreaName);
      cstmt.setString(2, strAreaCode);
      cstmt.registerOutParameter(3, OracleTypes.CURSOR);
      cstmt.registerOutParameter(4, Types.NUMERIC);
      cstmt.registerOutParameter(5, Types.CHAR);      
      cstmt.execute();
      iCount=cstmt.getInt(4);
      strMessage = cstmt.getString(5);
      rs = (ResultSet)cstmt.getObject(3);      
      while (rs.next()) {         
         HashMap h = new HashMap();           
         h.put("city", rs.getString(1));           
         h.put("areaCode", rs.getString(2));      
         list.add(h);
      }
   }
   catch(Exception e){
      throw new Exception(e);
   }
   finally{
		closeObjectsDatabase(conn, cstmt, rs);
      /*if (rs != null)
         rs.close();
      if (cstmt != null)
         cstmt.close();   
      if (conn != null)
         conn.close();*/
   }
   if (strMessage!=null && strMessage.startsWith(Constante.SUCCESS_ORA_RESULT))
      strMessage=null;   
   hshData.put("arrAreaCodeList",list);
   hshData.put("strMessage",strMessage);       
   
   return hshData;
}
    
/**
* Motivo: Obtiene un listado de Region
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 28/09/2007      
* @return    HashMap 
*/
public  HashMap getRegionList()
throws SQLException, Exception{
   HashMap hshData=new HashMap();
   String strMessage=null;    
   ArrayList list = new ArrayList();
   Connection conn = null;
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;       
   
   String sqlStr = "BEGIN WEBSALES.SPI_GET_REGION_LIST(?, ?); END;";
   try{
      conn = Proveedor.getConnection();
      
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      
      cstmt.registerOutParameter(1, OracleTypes.CURSOR);
      cstmt.registerOutParameter(2, Types.CHAR);
      
      cstmt.execute();
      strMessage = cstmt.getString(2);
      rs = (ResultSet)cstmt.getObject(1);
      
      while (rs.next()) {
         HashMap h = new HashMap();           
         h.put("swregionid", rs.getLong(1)+"");           
         h.put("swname", rs.getString(2));      
         list.add(h);
      }
   }
   catch(Exception e){
      throw new Exception(e);
   }
   finally{
		closeObjectsDatabase(conn, cstmt, rs);
      /*if (rs != null)
         rs.close();
      if (cstmt != null)
         cstmt.close();   
      if (conn != null)
         conn.close();   */
   }   
   if (strMessage!=null && strMessage.startsWith(Constante.SUCCESS_ORA_RESULT))
      strMessage=null;   
   hshData.put("arrRegionList",list);
   hshData.put("strMessage",strMessage);   
   return hshData;
}
    
    
/**
* Motivo: Obtiene el nombre de la Region dado el Id de la Region, lo consulta de la tabla swbapps.sw_region
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 01/10/2007
* @param     lDepartmentId
* @return    String
*/
public String getDepartmentName(String strCode) 
throws SQLException, Exception{
   Connection conn = null;
   OracleCallableStatement cstmt = null;        
   String strNombre=null;
   
   String sqlStr = "BEGIN WEBSALES.SPI_GET_DEPARTAMENTO_FROM_CODE(?, ?); END;";
   try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
      cstmt.setString(1, strCode);
      cstmt.registerOutParameter(2, OracleTypes.VARCHAR);            
      cstmt.executeQuery();          	
      strNombre = cstmt.getString(2);			       
   }
   catch(Exception e){
      throw new Exception(e);
   }
   finally{
		closeObjectsDatabase(conn, cstmt, null);
      /*if (cstmt != null)
         cstmt.close();
      if (conn != null)
         conn.close();*/
   }
   return strNombre;
}
    
/**
* Motivo: Obtiene el nombre de la Region dado el Id de la Region, consulta de la tabla swbapps.np_ubigeo
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 03/10/2007
* @param     lDepartmentId
* @return    String
*/
public String getRegionName(long lRegionId) 
throws SQLException, Exception{
   Connection conn = null;
   OracleCallableStatement cstmt = null;        
   String strNombre=null;   
   String sqlStr = "BEGIN WEBSALES.SPI_REGION_NAME(?, ?); END;";
   try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
      cstmt.setLong(1, lRegionId);
      cstmt.registerOutParameter(2, OracleTypes.VARCHAR);            
      cstmt.executeQuery();          	
      strNombre = cstmt.getString(2);			       
   }
   catch(Exception e){
      throw new Exception(e);
   }
   finally{
		closeObjectsDatabase(conn, cstmt, null);
      /*if (cstmt != null)
         cstmt.close();
      if (conn != null)
         conn.close();*/
   }
   return strNombre;
}

/**
* Motivo: Devuelve la valoración del cliente 
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 08/11/2007
* @param     long    
* @return    int 
*/
public int getCustomerValue(long lCustomerId) 
throws SQLException, Exception
{
   int iReturnValue = 0;
   Connection conn=null;
   OracleCallableStatement cstmt = null;
   
   String sqlStr = "BEGIN WEBSALES.SPI_GET_CUST_VALUE( ?, ?); END;";
   try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);   
      cstmt.setLong(1, lCustomerId);
      cstmt.registerOutParameter(2, Types.NUMERIC);          
      cstmt.execute();
      
      iReturnValue = cstmt.getInt(2); 
   }
   catch(Exception e){
      throw new Exception(e);
   }   
   finally{
		closeObjectsDatabase(conn, cstmt, null);
      /*if(cstmt != null)
         cstmt.close();
      if(conn != null)
         conn.close();  */
   }
   return iReturnValue;
}  

   /**
   * Motivo: Verifica si el usuario tiene permiso para una determinada sección
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 24/07/2007
   * @param     iScreenoptionid  
   * @param     iUserid        
   * @param     iAppid    
   * @return    int 
   */
   
   public int getCountCases(String strType, String strValue) 
   throws SQLException,Exception
   {
      int iReturnValue = 0;
      Connection conn=null;
      OracleCallableStatement cstmt = null;
      
      String sqlStr = " { ? = call ORDERS.NP_REPAIR01_PKG.FX_GET_COUNT_CASES( ?, ? ) } ";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         
         cstmt.registerOutParameter(1, Types.NUMERIC);          
         cstmt.setString(2, strType);
         cstmt.setString(3, strValue);       
         
         cstmt.execute();
         
         iReturnValue = cstmt.getInt(1); 
      }
      catch(Exception e){
         throw new Exception(e);
      }      
      finally{
			closeObjectsDatabase(conn, cstmt, null);
         /*if(cstmt != null)
            cstmt.close();
         if(conn != null)
            conn.close();  */
      }
      return iReturnValue;
   }

   /**
    * Motivo: Obtiene el nombre de la tienda dado el buildingid
    * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
    * <br>Fecha: 07/02/2008
    * @param     int iBuildingid  
    * @return	  String
    */
    public String getBuildingName(int iBuildingid) throws Exception,SQLException{
       
       Connection conn = null; 
       OracleCallableStatement cstm = null;
       String strBuildingName = "";     
       String sqlStr = "{ ? = call WEBSALES.NPSL_GENERAL_PKG.FX_TIENDA_NAME(?)}";
       try{
          conn = Proveedor.getConnection();          
          cstm = (OracleCallableStatement) conn.prepareCall(sqlStr);                   
          cstm.registerOutParameter(1,Types.VARCHAR);
          cstm.setInt(2,iBuildingid);                    
          cstm.executeUpdate();          
          strBuildingName = cstm.getString(1);
       }
       catch(Exception e){
          throw new Exception(e);
       }
       finally{
			closeObjectsDatabase(conn, cstm, null);
          /*if(cstm != null)
              cstm.close();
          if(conn != null)
             conn.close(); */
       }
       return strBuildingName;                                                                
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
     *  FECHA: 28/08/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/

    /**
     * Motivo:  Obtiene el "npvaluedesc" teniendo como dato de entrada
     *          el campo "npvalue" y el campo "nptable" en la tabla SWBAPPS.NP_TABLE
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 19/07/2007
     * 
     * @param      strValue     Ej: 40 (Préstamo)
     * @param      strTable     Ej: MODALITY_TYPES
     * @return     CustomerBean que servirá para validar si el Cliente existe o no 
     */
    public HashMap getDescriptionByValue(String strValue, String strTable) throws SQLException, Exception {		
      HashMap hshDataMap = new HashMap();
      String strDescription = null;
      Connection conn = null;
      OracleCallableStatement cstmt = null;
      String strMessage = null;
      String sqlStr = "BEGIN SWBAPPS.NP_TABLE01_PKG.SP_GET_VALUEDESC(?,?,?,?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.setString(1, strValue);
         cstmt.setString(2, strTable);
         cstmt.registerOutParameter(3, Types.CHAR);
         cstmt.registerOutParameter(4, Types.CHAR);
         cstmt.executeQuery();
         strMessage = cstmt.getString(3);
         strDescription = cstmt.getString(4);
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, null);
      }
      hshDataMap.put("strDescription", strDescription);
      hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
      return hshDataMap;
    }

    /**
     * Motivo:  Obtiene una lista de Dominios según el "nptable" de la tabla SWBAPPS.NP_TABLE
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 19/08/2007
     * 
     * @param      dominioTabla Ej: MODALITY_TYPES
     * @return     ArrayList de DominioBean
     */
    public HashMap getDominioList(String dominioTabla) throws SQLException, Exception {
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
      ArrayList arrDominioList = new ArrayList();
      Connection conn = null;
      ResultSet rs = null;
      OracleCallableStatement cstmt = null;
		String strMessage = null;
		String sqlStr = "BEGIN WEBSALES.NPSL_GENERAL_PKG.SP_GET_NPTABLE_LIST(?,?,?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.setString(1, dominioTabla);
         cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(3, OracleTypes.CURSOR);
         cstmt.executeQuery();
         rs = (ResultSet)cstmt.getObject(3);
         strMessage = cstmt.getString(2);
         if(StringUtils.isBlank(strMessage)) {
            while (rs.next()) {
               DominioBean dominio = new DominioBean();
               int i = 1;
               dominio.setValor(StringUtils.defaultString(rs.getString(i++)));
               dominio.setDescripcion(StringUtils.defaultString(rs.getString(i++)));
               arrDominioList.add(dominio);
            }
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
		hshDataMap.put("arrDominioList", arrDominioList);
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;
    }

    /**
     * Motivo:  Obtiene una lista de Dominios según el "configuration" de la tabla REPAIR.NP_CONFIGURATION
     * <br>Realizado por: <a href="mailto:paolo.ortega@teamsoft.com.pe">Paolo Ortega</a>
     * <br>Fecha: 19/08/2007
     * 
     * @param      av_param Ej: parametro de busqueda
     * @return     ArrayList de DominioBean
     */
    public HashMap getRepairConfiguration(String av_param) throws SQLException, Exception {
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
      ArrayList arrDominioList = new ArrayList();
      Connection conn = null;
      ResultSet rs = null;
      OracleCallableStatement cstmt = null;
		String strMessage = null;
		String sqlStr = "BEGIN REPAIR.SPI_GET_CONFIGURATION(?,?,?,?,? ,?,?,?,?,?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.setString(1, av_param);
         cstmt.setString(2, Constante.RESPUESTA_COTIZACION_TYPE_OBJECT);
         cstmt.setString(3, Constante.RESPUESTA_COTIZACION_VALID_STATUS);
         cstmt.setString(4, Constante.RESPUESTA_COTIZACION_ORDER_BY);
         cstmt.setString(5, Constante.RESPUESTA_COTIZACION_CAMPO1);
         cstmt.setString(6, Constante.RESPUESTA_COTIZACION_CAMPO2);
         cstmt.setString(7, "");
         cstmt.setString(8, "");
         cstmt.registerOutParameter(9, OracleTypes.CURSOR);
         cstmt.registerOutParameter(10, OracleTypes.VARCHAR);
         cstmt.executeQuery();
         rs = (ResultSet)cstmt.getObject(9);
         strMessage = cstmt.getString(10);
         if(StringUtils.isBlank(strMessage)) {
            while (rs.next()) {
               DominioBean dominio = new DominioBean();
               dominio.setValor(StringUtils.defaultString(rs.getString(1)));
               dominio.setDescripcion(StringUtils.defaultString(rs.getString(2)));
               arrDominioList.add(dominio);
            }
         }
      }
      catch(Exception e){
        e.printStackTrace();
        throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
		hshDataMap.put("arrDominioList", arrDominioList);
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;
    }

    /**
     * Motivo:  Obtiene la lista de Planes Tarifarios.
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 19/08/2007
     * 
     * @return     ArrayList de DominioBean
     */
    public HashMap getRatePlanList() throws SQLException, Exception {
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
        ArrayList arrRatePlanList = new ArrayList();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage = null;
        String sqlStr = "BEGIN WEBSALES.NP_CUSTOMER_DATA_PKG.SP_RATE_PLAN_LIST(?,?); END;";
        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            rs = (ResultSet)cstmt.getObject(1);
            strMessage = cstmt.getString(2);
            if (StringUtils.isBlank(strMessage)) {
               while (rs.next()) {
                   DominioBean dominio = new DominioBean();
                   int i = 1;
                   dominio.setValor(StringUtils.defaultString(rs.getString(i++)));
                   dominio.setDescripcion(StringUtils.defaultString(rs.getString(i++)));
                   arrRatePlanList.add(dominio);
               }
            }
        }
        catch(Exception e){
           throw new Exception(e);
        }
        finally{
          closeObjectsDatabase(conn, cstmt, rs);
        }
		hshDataMap.put("arrRatePlanList", arrRatePlanList);
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;
    }

    /**
     * Motivo:  Obtiene la lista de Departamentos, Provincia y Distrito del Perú.
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 19/08/2007
     * 
     * @return     ArrayList de DominioBean
     */
    public HashMap getUbigeoList(UbigeoBean objUbigeo) throws SQLException, Exception {
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
        ArrayList arrUbigeoList = new ArrayList();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage = null;
        String sqlStr = "BEGIN WEBSALES.NP_CUSTOMER_DATA_PKG.SP_UBIGEO_LIST(?,?,?,?,?); END;";
        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, objUbigeo.getDepartamento());
            cstmt.setString(2, objUbigeo.getProvincia());
            cstmt.setString(3, objUbigeo.getDistrito());
            cstmt.registerOutParameter(4, OracleTypes.CURSOR);
            cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            rs = (ResultSet)cstmt.getObject(4);
            strMessage = cstmt.getString(5);
            if (StringUtils.isBlank(strMessage)) {
                  while (rs.next()) {
                      UbigeoBean objUbigeoBean = new UbigeoBean();
                      int i = 1;
                      objUbigeoBean.setDepartamento(StringUtils.defaultString(rs.getString(i++)));
                      objUbigeoBean.setProvincia(StringUtils.defaultString(rs.getString(i++)));
                      objUbigeoBean.setDistrito(StringUtils.defaultString(rs.getString(i++)));
                      objUbigeoBean.setNombre(StringUtils.defaultString(rs.getString(i++)));
                      objUbigeoBean.setId(rs.getInt(i++));
                      arrUbigeoList.add(objUbigeoBean);
                  }
              }
        }
       catch(Exception e){
          throw new Exception(e);
       }
       finally{
         closeObjectsDatabase(conn, cstmt, rs);
       }
		hshDataMap.put("arrUbigeoList", arrUbigeoList);
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;
    }

    /**
     * Motivo:  Obtiene la lista de Giros (Actividades)
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 26/08/2007
     * 
     * @return     ArrayList de DominioBean
     */
    public HashMap getGiroList() throws SQLException, Exception {
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
        ArrayList arrGiroList = new ArrayList();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage = null;
        String sqlStr = "BEGIN WEBSALES.NP_CUSTOMER_DATA_PKG.SP_ACTIVITIES_BUSINESS_LST(?,?); END;";
        try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.registerOutParameter(1, OracleTypes.CURSOR);
         cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
         cstmt.executeUpdate();
         rs = (ResultSet)cstmt.getObject(1);
         strMessage = cstmt.getString(2);
         if (StringUtils.isBlank(strMessage)) {
               while (rs.next()) {
                   DominioBean dominio = new DominioBean();
                   int i = 1;
                   dominio.setValor(StringUtils.defaultString(rs.getString(i++), ""));
                   dominio.setDescripcion(StringUtils.defaultString(rs.getString(i++), ""));
                   arrGiroList.add(dominio);
               }
           }
        }
       catch(Exception e){
         throw new Exception(e);
       }
       finally{
		   closeObjectsDatabase(conn, cstmt, rs);
       }
		hshDataMap.put("arrGiroList", arrGiroList);
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;
    }

    /**
     * Motivo:  Obtiene la lista de SubGiros (Sub-Actividades) dado un Giro.
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 26/08/2007
     * 
     * @return     ArrayList de DominioBean
     */
    public HashMap getSubGirosByGiroList(long giroId) throws SQLException, Exception {
        if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
		ArrayList arrSubGiroList = new ArrayList();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage = null;
        String sqlStr = "BEGIN WEBSALES.NP_CUSTOMER_DATA_PKG.SP_SUB_ACTIVITIES_BUSINESS_LST(?,?,?); END;";
        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1, giroId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.execute();
            rs = (ResultSet)cstmt.getObject(2);
            strMessage = cstmt.getString(3);
            if (StringUtils.isBlank(strMessage)) {
                  while (rs.next()) {
                      DominioBean dominio = new DominioBean();
                      int i = 1;
                      dominio.setValor(StringUtils.defaultString(rs.getString(i++)));
                      dominio.setDescripcion(StringUtils.defaultString(rs.getString(i++)));
                      arrSubGiroList.add(dominio);
                  }
           }
        }
        catch(Exception e){
           throw new Exception(e);
        }
        finally{
          closeObjectsDatabase(conn, cstmt, rs);
        }
		hshDataMap.put("arrSubGiroList", arrSubGiroList);
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;
    }

    /**
     * Motivo:  Obtiene la lista de SubGiros (Sub-Actividades) dado un Giro.
     * <br>Realizado por: <a href="mailto:oliver.dubock@nextel.com.pe">Oliver Dubock</a>
     * <br>Fecha: 07/07/2008
     * 
     * @return     ArrayList de DominioBean
     */
    public HashMap getSubGirosByIndustry(long giroId) throws SQLException, Exception {
        if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
		ArrayList arrSubGiroList = new ArrayList();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage = null;
        String sqlStr = "BEGIN WEBSALES.NP_CUSTOMER_DATA_PKG.SP_SUB_ACT_BUSINESS_LST2(?,?,?); END;";
        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1, giroId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.execute();
            rs = (ResultSet)cstmt.getObject(2);
            strMessage = cstmt.getString(3);
            if (StringUtils.isBlank(strMessage)) {
                  while (rs.next()) {
                      DominioBean dominio = new DominioBean();
                      int i = 1;
                      dominio.setValor(StringUtils.defaultString(rs.getString(i++)));
                      dominio.setDescripcion(StringUtils.defaultString(rs.getString(i++)));
                      arrSubGiroList.add(dominio);
                  }
              }
        }
        catch(Exception e){
           throw new Exception(e);
        }
        finally{
		     closeObjectsDatabase(conn, cstmt, rs);
        }
		hshDataMap.put("arrSubGiroList", arrSubGiroList);
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;
    }

    /**
     * Motivo: Obtiene la Lista de Kits
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 12/09/2007
     * @return    ArrayList de DominioBean
     */
    public HashMap getKitsList(String strTiendaRetail) throws SQLException, Exception {
        if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
		ArrayList arrKitsList = new ArrayList();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        //DbmsOutput dbmsOutput = null;
		String strMessage = null;
        String sqlStr = "BEGIN ORDERS.NP_RETAIL01_PKG.SP_GET_KITS_LST(?,?,?); END;";
        try{
            conn = Proveedor.getConnection();
            //dbmsOutput = new DbmsOutput(conn);
            //dbmsOutput.enable(1000000);
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, strTiendaRetail);
            cstmt.registerOutParameter(2, OracleTypes.ARRAY, "ORDERS.TT_DOMINIO_LST");
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.execute();
            //dbmsOutput.show();
            //dbmsOutput.close();
            strMessage = cstmt.getString(3);
            ARRAY aryDominioList = (ARRAY)cstmt.getObject(2);
              if (StringUtils.isBlank(strMessage)) {
               for (int i = 0; i < aryDominioList.getOracleArray().length; i++) {
                  STRUCT stcDominio = (STRUCT) aryDominioList.getOracleArray()[i];
                  DominioBean objDominioBean = new DominioBean();
                  objDominioBean.setValor(MiUtil.defaultString(stcDominio.getOracleAttributes()[0], ""));
                  objDominioBean.setDescripcion(MiUtil.defaultString(stcDominio.getOracleAttributes()[1], ""));
                  arrKitsList.add(objDominioBean);
               }
              }
        }
        catch(Exception e){
           throw new Exception(e);
       }
       finally{
		    closeObjectsDatabase(conn, cstmt, rs);
       }
		hshDataMap.put("arrKitsList", arrKitsList);
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;
    }

    /**
     * Motivo:  Obtiene en una Lista la información asociada a un Kit (Productos)
     *          Conceptos:
     *          <pre>
     *              1. El Kit es un Producto.
     *              2. Un Kit puede estar compuesto (por ejemplo) de un Equipo y un Servicio.
     *              3. El Servicio tiene asociado un Plan.
     *          </pre>
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 12/09/2007
     * @return    ArrayList de ProductBean
     */
    public HashMap getProductsByKitList(long lKitId) throws SQLException, Exception {
        if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
		ArrayList arrProductsKitsList = new ArrayList();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        //DbmsOutput dbmsOutput = null;
		String strMessage = null;
        String sqlStr = "BEGIN ORDERS.NP_RETAIL01_PKG.SP_GET_KIT_PRODUCTS_LST(?, ?, ?); END;";
        try{
              conn = Proveedor.getConnection();
            //dbmsOutput = new DbmsOutput(conn);
            //dbmsOutput.enable(1000000);
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1, lKitId);
            cstmt.registerOutParameter(2, OracleTypes.ARRAY, "ORDERS.TT_PRODUCT_LST");
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.execute();
            //dbmsOutput.show();
            //dbmsOutput.close();
            strMessage = cstmt.getString(3);
            ARRAY aryProductList = (ARRAY)cstmt.getObject(2);
            if (StringUtils.isBlank(strMessage)) {
                  for (int i = 0; i < aryProductList.getOracleArray().length; i++) {
                      STRUCT stcProducto = (STRUCT) aryProductList.getOracleArray()[i];
                      ProductBean objProductBean = new ProductBean();
                      ProductLineBean objProductLineBean = new ProductLineBean();
                      int a = 0;
                      NUMBER nmbProductId = ((NUMBER) stcProducto.getAttributes()[a++]);
                      objProductBean.setNpproductid(nmbProductId != null ? nmbProductId.longValue() : 0);
                      NUMBER nmbProductLineId = ((NUMBER) stcProducto.getOracleAttributes()[a++]);
                      objProductBean.setNpproductlineid(nmbProductLineId != null ? nmbProductLineId.longValue() : 0);
                      objProductLineBean.setNpName(stcProducto.getOracleAttributes()[a++].toString());
                      objProductBean.setNpname(stcProducto.getOracleAttributes()[a++].toString());
                      objProductBean.setNpnote(stcProducto.getOracleAttributes()[a++].toString());
                      NUMBER nmbCost = ((NUMBER) stcProducto.getOracleAttributes()[a++]);
                      objProductBean.setNpcost(nmbCost != null ? nmbCost.floatValue() : 0);
                      objProductBean.setNpcurrency(stcProducto.getOracleAttributes()[a++].toString());
                      NUMBER nmbPlanId = ((NUMBER) stcProducto.getOracleAttributes()[a++]);
                      objProductBean.setNpplanid(nmbPlanId != null ? nmbPlanId.longValue() : 0);
                      arrProductsKitsList.add(objProductBean);
                  }
            }
        }
        catch(Exception e){
           throw new Exception(e);
        }
        finally{
 		     closeObjectsDatabase(conn, cstmt, rs);
        }
		hshDataMap.put("arrProductsKitsList", arrProductsKitsList);
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;
    }
	
	/**
     * Motivo:  Obtiene en una Lista la información asociada a un Kit (Productos)
     *          Conceptos:
     *          <pre>
     *              1. El Kit es un Producto.<br>
     *              2. Un Kit puede estar compuesto (por ejemplo) de un Equipo y un Servicio.<br>
     *              3. El Servicio tiene asociado un Plan.
     *          </pre>
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 19/11/2007
     * @return	HashMap que contiene: MESSAGE_OUTPUT, Detalle del Kit, Lista de Detalles del Producto.
     */
    public HashMap getKitDetail(long lKitId, String strModalidad,  long lngSalesStructOrigenId) throws SQLException, Exception {
        if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
		ArrayList arrProductDetailList = new ArrayList();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        //DbmsOutput dbmsOutput = null;
		String strMessage = null;
      HashMap hshKitMap = new HashMap(); 
        String sqlStr = "BEGIN PRODUCT.SPI_GET_KIT_DET(?,?,?,?,?,?); END;";
        try{
              conn = Proveedor.getConnection();
            //dbmsOutput = new DbmsOutput(conn);
            //dbmsOutput.enable(1000000);
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1, lKitId);
            cstmt.setString(2, strModalidad);
            cstmt.setLong(3, lngSalesStructOrigenId);
            cstmt.registerOutParameter(4, OracleTypes.STRUCT, "PRODUCT.TR_PRODUCT_KIT_DET");
            cstmt.registerOutParameter(5, OracleTypes.ARRAY, "PRODUCT.TT_PRODUCT_KIT_PRODUCT_DET_LST");
            cstmt.registerOutParameter(6, OracleTypes.VARCHAR);
            cstmt.execute();
            //dbmsOutput.show();
            //dbmsOutput.close();
            strMessage = cstmt.getString(6);
          if( strMessage == null ){
            STRUCT stcKitDetail = (STRUCT)cstmt.getObject(4);
            ARRAY aryProductDetailList = (ARRAY)cstmt.getObject(5);
            int c = 0;
            
            hshKitMap.put("kitId", MiUtil.defaultBigDecimal(stcKitDetail.getAttributes()[c++], null));
            hshKitMap.put("kitName", MiUtil.defaultString(stcKitDetail.getAttributes()[c++], ""));
            hshKitMap.put("priceType", MiUtil.defaultString(stcKitDetail.getAttributes()[c++], ""));
            hshKitMap.put("modality", MiUtil.defaultString(stcKitDetail.getAttributes()[c++], ""));
            hshKitMap.put("currency", MiUtil.defaultString(stcKitDetail.getAttributes()[c++], ""));
            hshKitMap.put("kitPrice", MiUtil.defaultBigDecimal(stcKitDetail.getAttributes()[c++], null));
            for (int i = 0; i < aryProductDetailList.getOracleArray().length; i++) {
              STRUCT stcProduct = (STRUCT) aryProductDetailList.getOracleArray()[i];
              HashMap hshProductMap = new HashMap();
              c = 0;
              hshProductMap.put("kitId", MiUtil.defaultBigDecimal(stcProduct.getAttributes()[c++], null));
              hshProductMap.put("kitName", MiUtil.defaultString(stcProduct.getAttributes()[c++], ""));
              hshProductMap.put("productId", MiUtil.defaultBigDecimal(stcProduct.getAttributes()[c++], null));
              hshProductMap.put("productLineId", MiUtil.defaultBigDecimal(stcProduct.getAttributes()[c++], null));
              hshProductMap.put("productName", MiUtil.defaultString(stcProduct.getAttributes()[c++], ""));
              hshProductMap.put("inventoryCode", MiUtil.defaultString(stcProduct.getAttributes()[c++], ""));
              hshProductMap.put("planId", MiUtil.defaultBigDecimal(stcProduct.getAttributes()[c++], null));
              hshProductMap.put("relationType", MiUtil.defaultString(stcProduct.getAttributes()[c++], ""));
              hshProductMap.put("units", MiUtil.defaultBigDecimal(stcProduct.getAttributes()[c++], null));
              hshProductMap.put("productPriceId", MiUtil.defaultBigDecimal(stcProduct.getAttributes()[c++], null));
              hshProductMap.put("priceType", MiUtil.defaultString(stcProduct.getAttributes()[c++], ""));
              hshProductMap.put("transactionId", MiUtil.defaultBigDecimal(stcProduct.getAttributes()[c++], null));
              hshProductMap.put("modality", MiUtil.defaultString(stcProduct.getAttributes()[c++], ""));
              hshProductMap.put("currency", MiUtil.defaultString(stcProduct.getAttributes()[c++], ""));
              hshProductMap.put("priceOneTime", MiUtil.defaultBigDecimal(stcProduct.getAttributes()[c++], null));
              arrProductDetailList.add(hshProductMap);
            }
          
          }
        }
        
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
	 	   closeObjectsDatabase(conn, cstmt, null);
      }
		hshDataMap.put("hshKitMap", hshKitMap);
		hshDataMap.put("arrProductDetailList", arrProductDetailList);
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;
    }

	/**
     * Motivo:  Obtiene en una Lista la información asociada a un Pack Sim (Producto Sim)
     *          Conceptos:
     *          <pre>
     *              1. El Pack es un Sim que se vende como un producto prepago<br>
     *          </pre>
     * <br>Realizado por: <a href="mailto:edgar.jara@nextel.com.pe">Edgar Jara</a>
     * <br>Fecha: 18/12/2007
     * @return	HashMap que contiene: MESSAGE_OUTPUT, Detalle del Pack
     */
    public HashMap getPackDetail(String strSim, String strPin) throws SQLException, Exception {
        if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
		ArrayList arrProductDetailList = new ArrayList();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        //DbmsOutput dbmsOutput = null;
		String strMessage = null;
    int error=0;
      HashMap hshPackMap = new HashMap(); 
        String sqlStr = "BEGIN ORDERS.SPI_GET_PACK_DETAIL(?,?,?,?,?); END;";
        try{
              conn = Proveedor.getConnection();
            //dbmsOutput = new DbmsOutput(conn);
            //dbmsOutput.enable(1000000);
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, strSim);
            cstmt.setString(2, strPin);
            cstmt.registerOutParameter(3, OracleTypes.STRUCT, "TR_PACK_DET");
            cstmt.registerOutParameter(4, OracleTypes.NUMBER);
            cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
            cstmt.execute();
            //dbmsOutput.show();
            //dbmsOutput.close();
            strMessage = cstmt.getString(5);
            error = (int)cstmt.getInt(4);
          if( strMessage == null ){
            STRUCT stcPackDetail = (STRUCT)cstmt.getObject(3);
            int c = 0;
            
            hshPackMap.put("itemId", MiUtil.defaultBigDecimal(stcPackDetail.getAttributes()[c++], null));
            hshPackMap.put("cardNumber", MiUtil.defaultString(stcPackDetail.getAttributes()[c++], ""));
            hshPackMap.put("simNumber", MiUtil.defaultString(stcPackDetail.getAttributes()[c++], ""));
            hshPackMap.put("pinNumber", MiUtil.defaultString(stcPackDetail.getAttributes()[c++], ""));
            hshPackMap.put("producId", MiUtil.defaultBigDecimal(stcPackDetail.getAttributes()[c++], null));
            hshPackMap.put("productLineId", MiUtil.defaultBigDecimal(stcPackDetail.getAttributes()[c++],null));            
            hshPackMap.put("productName", MiUtil.defaultString(stcPackDetail.getAttributes()[c++], ""));
            hshPackMap.put("planId", MiUtil.defaultBigDecimal(stcPackDetail.getAttributes()[c++],null));            
            hshPackMap.put("planName", MiUtil.defaultString(stcPackDetail.getAttributes()[c++], ""));            
            hshPackMap.put("npTemplate", MiUtil.defaultString(stcPackDetail.getAttributes()[c++], ""));
          
          }
        }
        
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
	 	   closeObjectsDatabase(conn, cstmt, null);
      }
    String strError=Integer.toString(error);
		hshDataMap.put("strError", strError );
    hshDataMap.put("hshPackMap", hshPackMap);
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
    return hshDataMap;
    }    
	
	/**
     * Motivo:  Obtiene la descripción del Plan
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 19/11/2007
     * @return	HashMap que contiene: MESSAGE_OUTPUT, String de la Descripción del Plan
     */
    public HashMap getPlanTarifarioNombre(long lPlanId) throws SQLException, Exception {
        if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
		String strPlanTarifarioNombre = null;
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        //DbmsOutput dbmsOutput = null;
		String strMessage = null;
        String sqlStr = "BEGIN ORDERS.NP_RETAIL01_PKG.SP_GET_PLAN_TARIFARIO_DET(?,?,?); END;";
        try{
           conn = Proveedor.getConnection();
         //dbmsOutput = new DbmsOutput(conn);
         //dbmsOutput.enable(1000000);
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.setLong(1, lPlanId);
         cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
         cstmt.execute();
         strMessage = cstmt.getString(2);
         strPlanTarifarioNombre = cstmt.getString(3);
        }
        catch(Exception e){
           throw new Exception(e);
        }
        finally{
		    closeObjectsDatabase(conn, cstmt, null);
        }
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		hshDataMap.put("strPlanTarifarioNombre", strPlanTarifarioNombre);
        return hshDataMap;
    }
	
	/**
     * Motivo:  
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 23/09/2007
     * 
     * @param		strNombreOpcion     Ej: ServiciosReparacion
     * @return		ArrayList de DominioBean      
     */
    public HashMap getComboReparacionList(String strNombreOpcion) throws SQLException, Exception {
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
		ArrayList arrComboReparacionList = new ArrayList();
		Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
		String strMessage = null;
		String sqlStr = "BEGIN ORDERS.NP_ORDERS03_PKG.SP_GET_GENERAL_OPTION_LIST(?,?,?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.setString(1, strNombreOpcion);
         cstmt.registerOutParameter(2, OracleTypes.CURSOR);
         cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
         cstmt.executeQuery();
         rs = (ResultSet)cstmt.getObject(2);
         strMessage = cstmt.getString(3);
         if (StringUtils.isBlank(strMessage)) {
            while (rs.next()) {
               DominioBean dominio = new DominioBean();
               int i = 1;
               dominio.setValor(StringUtils.defaultString(rs.getString(1)));
               try {
                  dominio.setDescripcion(StringUtils.defaultString(rs.getString(2)));
               }
               catch(SQLException sqle) {
                  dominio.setDescripcion(StringUtils.defaultString(rs.getString(1)));
               }

                System.out.println("Dominio " + strNombreOpcion + ": " + dominio.getValor() + dominio.getDescripcion());

               arrComboReparacionList.add(dominio);
            }
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
		   closeObjectsDatabase(conn, cstmt, rs);
      }
		hshDataMap.put("arrComboReparacionList", arrComboReparacionList);
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;
    }
	
	/**
     * Motivo: Obtiene la Lista de Estado de Orden
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 12/09/2007
     * @return    ArrayList de DominioBean
     */
    public HashMap getEstadoOrdenList() throws SQLException, Exception {
        if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
		ArrayList arrEstadoOrdenList = new ArrayList();
		Connection conn = null;
        OracleCallableStatement cstmt = null;
        //ResultSet rs = null;
		//DbmsOutput dbmsOutput = null;
		String strMessage = null;
        String sqlStr = "BEGIN ORDERS.NP_ORDERS08_PKG.SP_GET_ORDER_STATUS_LST(?,?,?); END;";
        try{
            conn = Proveedor.getConnection();
            //dbmsOutput = new DbmsOutput(conn);
            //dbmsOutput.enable(1000000);
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            StructDescriptor sdOrder = StructDescriptor.createDescriptor("ORDERS.TO_ORDER_STATUS", conn);
            Object[] objOrder = { null,
                             "FFPEDIDOS",
                             null,
                             "a",
                             null,
                             null};
            STRUCT sOrder = new STRUCT(sdOrder, conn, objOrder);
            //cstmt.setObject(1, (STRUCT)sOrder);
          cstmt.setSTRUCT(1, sOrder);
            cstmt.registerOutParameter(2, OracleTypes.ARRAY, "ORDERS.TT_ORDER_STATUS_LST");
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.execute();
            //dbmsOutput.show();
            //dbmsOutput.close();
            strMessage = cstmt.getString(3);
            ARRAY aryDominioList = (ARRAY)cstmt.getObject(2);
            if (StringUtils.isBlank(strMessage)) {
                  for (int i = 0; i < aryDominioList.getOracleArray().length; i++) {
                      STRUCT stcDominio = (STRUCT) aryDominioList.getOracleArray()[i];
                      DominioBean objDominioBean = new DominioBean();
                      objDominioBean.setValor(stcDominio.getOracleAttributes()[2].toString());
                      objDominioBean.setDescripcion(stcDominio.getOracleAttributes()[2].toString());
                  if(!isOrdenesEstadoExcluidos(objDominioBean.getDescripcion())) {
                     arrEstadoOrdenList.add(objDominioBean);
                  }
                  }
              }
        }
        catch(Exception e){
           throw new Exception(e);
        }
        finally{
          closeObjectsDatabase(conn, cstmt, null);
        }
		hshDataMap.put("arrEstadoOrdenList", arrEstadoOrdenList);
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;
    }
	
	private boolean isOrdenesEstadoExcluidos(String strDescripcion) {
		return strDescripcion.equals(Constante.ORDER_STATUS_EN_PROCESO);
	}
	
	/**
     * Motivo: Obtiene la Lista de Soluciones de Negocio
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 23/09/2007
     * @return    ArrayList de DominioBean
     */
    public HashMap getDivisionList() throws SQLException, Exception {
        if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
		ArrayList arrDivisionList = new ArrayList();
		Connection conn = null;
        OracleCallableStatement cstmt = null;
        //ResultSet rs = null;
		//DbmsOutput dbmsOutput = null;
		String strMessage = null;
        String sqlStr = "BEGIN ORDERS.NP_ORDERS08_PKG.SP_GET_DIVISION_LST(?,?); END;";
        try{
            conn = Proveedor.getConnection();
            //dbmsOutput = new DbmsOutput(conn);
            //dbmsOutput.enable(1000000);
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.registerOutParameter(1, OracleTypes.ARRAY, "ORDERS.TT_DIVISION_LST");
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.execute();
            //dbmsOutput.show();
            //dbmsOutput.close();
            strMessage = cstmt.getString(2);
            ARRAY aryDominioList = (ARRAY)cstmt.getObject(1);
            if (StringUtils.isBlank(strMessage)) {
                  for (int i = 0; i < aryDominioList.getOracleArray().length; i++) {
                      STRUCT stcDominio = (STRUCT) aryDominioList.getOracleArray()[i];
                      DominioBean objDominioBean = new DominioBean();
                  NUMBER nmbDivisionId = ((NUMBER) stcDominio.getOracleAttributes()[0]);
                      objDominioBean.setValor(""+(nmbDivisionId != null ? nmbDivisionId.longValue() : 0));
                      objDominioBean.setDescripcion(stcDominio.getOracleAttributes()[1].toString());
                      arrDivisionList.add(i, objDominioBean);
                  }
              }
        }
        catch(Exception e){
         throw new Exception(e);
       }
       finally{
          closeObjectsDatabase(conn, cstmt, null);
       }
		hshDataMap.put("arrDivisionList", arrDivisionList);
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;
    }
	
	/**
     * Motivo: Obtiene la Lista de Soluciones de Negocio
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 23/09/2007
     * @return    ArrayList de DominioBean
     */
    public HashMap getSolucionList(long lDivisionId) throws SQLException, Exception {
        if(logger.isDebugEnabled()) 			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
		ArrayList arrSolucionList = new ArrayList();
		Connection conn = null;
        OracleCallableStatement cstmt = null;
        //ResultSet rs = null;
		//DbmsOutput dbmsOutput = null;
		String strMessage = null;
        String sqlStr = "BEGIN ORDERS.NP_ORDERS08_PKG.SP_GET_SOLUTION_LST(?, ?, ?); END;";
        try{
            conn = Proveedor.getConnection();
            //dbmsOutput = new DbmsOutput(conn);
            //dbmsOutput.enable(1000000);
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            StructDescriptor sdSolution = StructDescriptor.createDescriptor("ORDERS.TO_SOLUTION", conn);
            Object[] objSolution = { null,
                             String.valueOf(lDivisionId),
                             null,
                             null,
                             null,
                             null,
                             null,
                             "a",
                             null,
                             null,
                             null};
            STRUCT sSolution = new STRUCT(sdSolution, conn, objSolution);
            //cstmt.setObject(1, (STRUCT)sSolution);
          cstmt.setSTRUCT(1, sSolution);
            cstmt.registerOutParameter(2, OracleTypes.ARRAY, "ORDERS.TT_SOLUTION_LST");
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.execute();
            //dbmsOutput.show();
            //dbmsOutput.close();
            strMessage = cstmt.getString(3);
            ARRAY aryDominioList = (ARRAY)cstmt.getObject(2);
            if (StringUtils.isBlank(strMessage)) {
               for (int i = 0; i < aryDominioList.getOracleArray().length; i++) {
                  STRUCT stcDominio = (STRUCT) aryDominioList.getOracleArray()[i];
                  DominioBean objDominioBean = new DominioBean();
                  NUMBER nmbDivisionId = ((NUMBER) stcDominio.getOracleAttributes()[0]);
                  objDominioBean.setValor(""+(nmbDivisionId != null ? nmbDivisionId.longValue() : 0));
                  objDominioBean.setDescripcion(stcDominio.getOracleAttributes()[2].toString());
                  arrSolucionList.add(objDominioBean);
               }
            }
        }
        catch(Exception e){
           throw new Exception(e);
        }
        finally{
           closeObjectsDatabase(conn, cstmt, null);
        }
		hshDataMap.put("arrSolucionList", arrSolucionList);
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;
    }
	
	/**
     * Motivo:	
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 23/09/2007
     * 
     * @param		strNombreOpcion     Ej: ServiciosReparacion
     * @return		ArrayList de DominioBean      
     */
    public HashMap getCategoryList(long lSolutionId) throws SQLException, Exception {
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
		ArrayList arrCategoryList = new ArrayList();
		Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
		//DbmsOutput dbmsOutput = null;
		String strMessage = null;
		String sqlStr = "BEGIN ORDERS.NP_ORDERS01_PKG.SP_GET_CATEGORY_LST(?,?,?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.setLong(1, lSolutionId);
         cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(3, OracleTypes.CURSOR);
         cstmt.executeQuery();
         rs = (ResultSet)cstmt.getObject(3);
         strMessage = cstmt.getString(2);
         if (StringUtils.isBlank(strMessage)) {
            while (rs.next()) {
               DominioBean dominio = new DominioBean();
               int i = 1;
               dominio.setValor(StringUtils.defaultString(rs.getString(1)));
               try {
                  dominio.setDescripcion(StringUtils.defaultString(rs.getString(2)));
               }
               catch(SQLException sqle) {
                  dominio.setDescripcion(StringUtils.defaultString(rs.getString(1)));
               }
               arrCategoryList.add(dominio);
            }
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
        closeObjectsDatabase(conn, cstmt, rs);
      }
		hshDataMap.put("arrCategoryList", arrCategoryList);
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;
    }
	
	/**
     * Motivo:  
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 23/09/2007
     * 
     * @param		strNombreOpcion     Ej: ServiciosReparacion
     * @return		ArrayList de DominioBean      
     */
    public HashMap getSubCategoryList(String strCategoria, long lSolutionId) throws SQLException, Exception {
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
		ArrayList arrSubCategoryList = new ArrayList();
		Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
		//DbmsOutput dbmsOutput = null;
		String strMessage = null;
		String sqlStr = "BEGIN ORDERS.NP_ORDERS01_PKG.SP_GET_SUBCATEGORY_LST(?,?,?,?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.setString(1, strCategoria);
         cstmt.setLong(2, lSolutionId);
         cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(4, OracleTypes.CURSOR);
         cstmt.executeQuery();
         rs = (ResultSet)cstmt.getObject(4);
         strMessage = cstmt.getString(3);
         if (StringUtils.isBlank(strMessage)) {
               while (rs.next()) {
                   DominioBean dominio = new DominioBean();
                   int i = 1;
                   dominio.setValor(StringUtils.defaultString(rs.getString(1)));
               try {
                  dominio.setDescripcion(StringUtils.defaultString(rs.getString(2)));
               }
               catch(SQLException sqle) {
                  dominio.setDescripcion(StringUtils.defaultString(rs.getString(1)));
               }
                   arrSubCategoryList.add(dominio);
               }
           }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
        closeObjectsDatabase(conn, cstmt, rs);
      }
		hshDataMap.put("arrSubCategoryList", arrSubCategoryList);
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;
    }
	
	/**
     * Motivo: Obtiene la Lista de Zonas, según el Bussinnes Unit.
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 23/09/2007
     * 
     * @param		strNombreOpcion     Ej: ServiciosReparacion
     * @return		ArrayList de DominioBean      
     */
    public HashMap getZoneList(long lBusinessUnitId) throws SQLException, Exception {
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
		ArrayList arrZoneList = new ArrayList();
		Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
		//DbmsOutput dbmsOutput = null;
		String strMessage = null;
		String sqlStr = "BEGIN WEBSALES.SPI_ZONES_LIST(?,?,?); END;";
      try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1, lBusinessUnitId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            rs = (ResultSet)cstmt.getObject(2);
            strMessage = cstmt.getString(3);
            if (StringUtils.isBlank(strMessage)) {
                  while (rs.next()) {
                      DominioBean dominio = new DominioBean();
                      int i = 1;
                      dominio.setValor(StringUtils.defaultString(rs.getString(1)));
                  try {
                     dominio.setDescripcion(StringUtils.defaultString(rs.getString(2)));
                  }
                  catch(SQLException sqle) {
                     dominio.setDescripcion(StringUtils.defaultString(rs.getString(1)));
                  }
                      arrZoneList.add(dominio);
                  }
            }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
        closeObjectsDatabase(conn, cstmt, rs);
      }
		hshDataMap.put("arrZoneList", arrZoneList);
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;
    }
	
	/**
     * Motivo: Obtener el Listado de Tiendas (Edificios). Ejm: TIENDA01, TIENDA02,... TIENDA09
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 08/11/2007
	 * 
	 * @throws java.lang.Exception
	 * @throws java.sql.SQLException
	 * @return HashMap - Estructura que contiene Información relacionada al vendedor
	 * 					 (Zona, Coordinador, Supervisor, Consultor/Ejecutivo)
	 * @param strLevel		Valor de Sesión: 8
	 * @param strCode		Valor de Sesión: D
	 * @param strBusUnitId	Unidad de Negocio: 13
	 * @param lVendedorId	Id del Vendedor: 56
	 * @param iFlagVendedor	Puede ser 0 o 1.
	 */
	public HashMap getCodigosJerarquiaVentasMap(String strLevel, String strCode, String strBusUnitId, long lVendedorId, int iFlagVendedor) throws SQLException, Exception {
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
		ArrayList arrZoneList = new ArrayList();
		Connection conn = null;
        OracleCallableStatement cstmt = null;
        //ResultSet rs = null;
		//DbmsOutput dbmsOutput = null;
		String strMessage = null;
		String sqlStr = "BEGIN WEBSALES.NPSL_NEW_GENERAL_PKG.SP_GET_SALES_HIERARCHY_CODES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); END;";
      try{
         conn = Proveedor.getConnection();
           cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
           cstmt.setString( 1, strLevel);
         cstmt.setString( 2, strCode);
         cstmt.setString( 3, strBusUnitId);
         cstmt.setString(10, String.valueOf(lVendedorId));
         cstmt.setString(12, String.valueOf(iFlagVendedor));
           cstmt.registerOutParameter( 4, OracleTypes.VARCHAR);
         cstmt.registerOutParameter( 5, OracleTypes.VARCHAR);
         cstmt.registerOutParameter( 6, OracleTypes.VARCHAR);
         cstmt.registerOutParameter( 7, OracleTypes.VARCHAR);
         cstmt.registerOutParameter( 8, OracleTypes.VARCHAR);
         cstmt.registerOutParameter( 9, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(10, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(11, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(12, OracleTypes.VARCHAR);
         cstmt.execute();
         int i = 4;
         hshDataMap.put("strZonaId", StringUtils.defaultString(cstmt.getString(i++)));
         hshDataMap.put("strZonaNombre", StringUtils.defaultString(cstmt.getString(i++)));
         hshDataMap.put("strCoordinadorId", StringUtils.defaultString(cstmt.getString(i++)));
         hshDataMap.put("strCoordinadorNombre", StringUtils.defaultString(cstmt.getString(i++)));
         hshDataMap.put("strSupervisorId", StringUtils.defaultString(cstmt.getString(i++)));
         hshDataMap.put("strSupervisorNombre", StringUtils.defaultString(cstmt.getString(i++)));
         hshDataMap.put("strConsultorEjecutivoId", StringUtils.defaultString(cstmt.getString(i++)));
         hshDataMap.put("strConsultorEjecutivoNombre", StringUtils.defaultString(cstmt.getString(i++)));
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, null);
      }
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;
	}
	
	/**
     * Motivo: Obtiene la Lista de Zonas, según el Bussinnes Unit.
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 23/09/2007
     * 
     * @param		strNombreOpcion     Ej: ServiciosReparacion
     * @return		ArrayList de DominioBean      
     */
    public HashMap getBuildingList(String strTipo) throws SQLException, Exception {
        logger.info("************************** INICIO GeneralDAO > getBuildingList**************************");
		HashMap objHashMap = new HashMap();
		ArrayList arrBuildingList = new ArrayList();
		String strMessage;
		Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
		String sqlStr = "BEGIN WEBSALES.SPI_GET_BUILDING_LIST4(?,?,?); END;";
      try{
         logger.info("strTipo : "+strTipo);
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.setString(1, strTipo);
         cstmt.registerOutParameter(2, OracleTypes.CURSOR);
         cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
         cstmt.executeQuery();
         rs = (ResultSet)cstmt.getObject(2);
         strMessage = cstmt.getString(3);
         
         if( strMessage != null && !strMessage.equals("") )
             strMessage = "DAO: "+strMessage;
          
         while (rs.next()) {
            DominioBean dominio = new DominioBean();
            int i = 1;
            dominio.setValor(StringUtils.defaultString(rs.getString(2), ""));
            dominio.setDescripcion(StringUtils.defaultString(rs.getString(1), ""));
            arrBuildingList.add(dominio);
         }
      }
      catch(Exception e){
         logger.error(e);
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
        objHashMap.put("arrBuildingList", arrBuildingList);
        objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        logger.info("************************** FIN GeneralDAO > getBuildingList**************************");
	return objHashMap;
    }
	
	/**
     * Motivo: Obtiene la Lista de Modelos (Productos)
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 09/10/2007
     * 
     * @return		ArrayList de DominioBean      
     */
	public HashMap getModelList() throws SQLException, Exception {
		HashMap objHashMap = new HashMap();
		ArrayList arrModelList = new ArrayList();
		String strMessage;
		Connection conn = null;
		HashMap hshData=new HashMap();
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;
		String sqlStr = "BEGIN WEBCCARE.NPAC_SERVICIOTECNICO_PKG.SP_GET_MODEL_LIST(?, ?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.registerOutParameter(1, OracleTypes.CURSOR);
         cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
         cstmt.executeQuery();
         rs = (ResultSet)cstmt.getObject(1);
         strMessage = cstmt.getString(2);
         while (rs.next()) {
            hshData=new HashMap();
            hshData.put("strValor",rs.getString(1));			
            arrModelList.add(hshData);
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
		objHashMap.put("arrModelList", arrModelList);
		objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		return objHashMap;
    }
	
	/**
     * Motivo: Segun el strCodeSet, se obtiene una lista para cargar los Combos de Reparaciones
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 09/10/2007
     * 
     * @return		ArrayList de DominioBean      
     */
	public HashMap getCodeSetList(String strCodeSet) throws SQLException, Exception {
		HashMap hshDataMap = new HashMap();
		Connection conn = null; 
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;
		String strMessage = null;
		ArrayList arrDominioLista = new ArrayList();
		HashMap hshData=null;
		String strSql =	"BEGIN WEBCCARE.NPAC_SERVICIOTECNICO_PKG.SP_GET_CODESET_LIST(?, ?, ?); END;";
      try{
         conn = Proveedor.getConnection();          
         cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
         cstmt.setString(1, strCodeSet);
         cstmt.registerOutParameter(2, OracleTypes.CURSOR);
         cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
         cstmt.execute();
         rs = (ResultSet)cstmt.getObject(2);
         strMessage = cstmt.getString(3);
         while(rs.next()) {
            hshData =new HashMap();       
            
            hshData.put("valor",rs.getString(1));			
            arrDominioLista.add(hshData);
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		hshDataMap.put("arrDominioLista", arrDominioLista);
		return hshDataMap;
	}
	
	/**
     * Motivo: Segun el strGeneralOption, se obtiene una lista para cargar los Combos de Reparaciones
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 09/10/2007
     * 
     * @return		ArrayList de DominioBean      
     */
	public HashMap getGeneralOptionList(String strGeneralOption) throws SQLException, Exception {
		HashMap hshDataMap = new HashMap();
		Connection conn = null; 
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;
		String strMessage = null;
		ArrayList arrDominioLista = new ArrayList();
		HashMap hshData=null;
		String strSql =	"BEGIN WEBCCARE.NPAC_SERVICIOTECNICO_PKG.SP_GET_GENERAL_OPTION_LIST(?, ?, ?); END;";
      try{
         conn = Proveedor.getConnection();          
         cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
         cstmt.setString(1, strGeneralOption);
         cstmt.registerOutParameter(2, OracleTypes.CURSOR);
         cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
         cstmt.execute();
         rs = (ResultSet)cstmt.getObject(2);
         strMessage = cstmt.getString(3);
         while(rs.next()) {
            hshData = new HashMap();			
            try{
               hshData.put("value",rs.getString(1));
            }catch(SQLException sqle) {
               hshData.put("value",rs.getLong(1)+"");
            }
            hshData.put("descripcion",rs.getString(2));
            arrDominioLista.add(hshData);
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		hshDataMap.put("arrDominioLista", arrDominioLista);
		return hshDataMap;
	}
	
	/**
     * Motivo: Obtiene la Lista de Procesos
     * <br/>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br/>JPEREZ: Se agrega parámetro
     * <br/>Fecha: 09/10/2007
     * 
     * @return		ArrayList de DominioBean      
     */
	public HashMap getProcessList(String strEquipment) throws SQLException, Exception {
		HashMap objHashMap = new HashMap();
		ArrayList arrProcessList = new ArrayList();
		String strMessage;
		Connection conn = null;
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;
		HashMap hshData=null;
		String sqlStr = "BEGIN ORDERS.NP_ORDERS15_PKG.SP_GET_PROCESS_NAME_LIST(?,?,?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.setString(1, strEquipment);
         cstmt.registerOutParameter(2, OracleTypes.CURSOR);
         cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
         cstmt.executeQuery();
         rs = (ResultSet)cstmt.getObject(2);
         strMessage = cstmt.getString(3);
         while (rs.next()) {			
            hshData =new HashMap();
            hshData.put("valor",rs.getString(1));						
            arrProcessList.add(hshData);
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{      
         closeObjectsDatabase(conn, cstmt, rs);
      }
		objHashMap.put("arrProcessList", arrProcessList);
		objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);    
		return objHashMap;
    }
	
	/**
     * Motivo: Obtiene la Lista de Detalles de la Reposición
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 11/12/2007
     * 
     * @return		HashMap      
     */
	public HashMap getDetalleReposicionByTelefono(String strTelefono) throws SQLException, Exception {
		HashMap objHashMap = new HashMap();
		ArrayList arrDetalleReposicionList = new ArrayList();
		String strMessage;
		Connection conn = null;
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;
		String sqlStr = "BEGIN ORDERS.NP_ORDERS10_PKG.SP_GET_GUARANTEE_PHONE(?, ?, ?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.setString(1, strTelefono);
         cstmt.registerOutParameter(2, OracleTypes.CURSOR);
         cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
         cstmt.executeQuery();
         rs = (ResultSet)cstmt.getObject(2);
         strMessage = cstmt.getString(3);
         while (rs.next()) {			
            int i = 0;
            HashMap hshDetalleReposicion = new HashMap();
            hshDetalleReposicion.put("npcoid", StringUtils.defaultString(rs.getString(++i)));
            hshDetalleReposicion.put("fec_act_gn", MiUtil.getDate(rs.getTimestamp(++i),"dd/MM/yyyy"));
            hshDetalleReposicion.put("fec_reposicion", MiUtil.getDate(rs.getTimestamp(++i),"dd/MM/yyyy"));
            hshDetalleReposicion.put("fec_ini_period", MiUtil.getDate(rs.getTimestamp(++i),"dd/MM/yyyy"));
            hshDetalleReposicion.put("fec_fin_period", MiUtil.getDate(rs.getTimestamp(++i),"dd/MM/yyyy"));
            arrDetalleReposicionList.add(hshDetalleReposicion);
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
		objHashMap.put("arrDetalleReposicionList", arrDetalleReposicionList);
		objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		return objHashMap;
    }
	
	/**
     * Motivo: Obtiene la Lista de Tiendas (Retail)
     * <br>Modificado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 12/09/2007
     * @return    ArrayList de HashMap (Tienda)
     */
    public HashMap getRetailList() throws SQLException, Exception {
		HashMap hshDataMap = new HashMap();
        ArrayList arrRetailList = new ArrayList();
        Connection conn = null;
        ResultSet rs = null;
        OracleCallableStatement cstmt = null;
		String strMessage = null;
		String sqlStr = "BEGIN SWBAPPS.NP_ORDERS_RETAIL01_PKG.SP_GET_RETAIL_LIST(?,?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.registerOutParameter(1, OracleTypes.CURSOR);
         cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
         cstmt.executeUpdate();
         rs = (ResultSet)cstmt.getObject(1);
         strMessage = cstmt.getString(2);
         if(StringUtils.isBlank(strMessage)) {
            while (rs.next()) {
               DominioBean objDominioBean = new DominioBean();
               objDominioBean.setValor(StringUtils.defaultString(rs.getString(1)));
               objDominioBean.setDescripcion(StringUtils.defaultString(rs.getString(2)));
               arrRetailList.add(objDominioBean);
            }
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
		hshDataMap.put("arrRetailList", arrRetailList);
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;
    }
	
	/**
     * Motivo: Valida que un IMEI exista en BSCS (Retail)
     * <br>Modificado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 17/12/2007
     * @return    HashMap
     */
    public HashMap getInfoImei(String strImei) throws SQLException, Exception {
		HashMap hshDataMap = new HashMap();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
		String strMessage = null;
		String sqlStr = "BEGIN WEBSALES.NP_BSCS_UTIL04_PKG.SP_GET_VALIDATE_IMEI(?,?,?,?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.setString(1, strImei);
         cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
         cstmt.executeUpdate();
         strMessage = cstmt.getString(4);
         if(StringUtils.isBlank(strMessage)) {
            hshDataMap.put("equipmentType", StringUtils.defaultString(cstmt.getString(2)));
            hshDataMap.put("serialNumber", StringUtils.defaultString(cstmt.getString(3)));
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, null);
      }
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;
    }

    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - FIN
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 28/08/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
	 
    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  ITEMS DE SECCIONES DINANICAS - INICIO
     *  REALIZADO POR: ISRAEL RONDON
     *  FECHA: 25/10/2007
     *  MODIFICACIÓN : EVELYN OCAMPO
     *  FECHA: 04/03/2008
     *  SE INCLUYE EL PARÁMETRO DIRECCIÓN DE INSTALACIÓN COMPARTIDA PARA LAS
     *  BÚSQUEDAS.
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
     
    /**
      * Motivo: Obtiene lista de direcciones de Instalación Compartida según dpto, prov, dist, id y nombre de Intalación Compart 
      * <br>Realizado por: <a href="mailto:jose.rondon@nextel.com.pe">Israel Rondón</a>
      * <br>Fecha: 22/10/2007
      * @param     iDptoId
      * @param     iProvId
      * @param     iDistId
      * @param     iSharedInstallId
      * @param     sSharedInstallName
      * @param     sSharedInstallAddress
      * @param     strMsgError   Contiene el mensaje de error si existiera
      * @return    HashMap 
    */
    public  HashMap getSharedInstalation(AddressObjectBean objAddressObjB)
    throws Exception,SQLException {
       long numTotalRegisters = 0;
       ArrayList  list = new ArrayList();
       HashMap hshResult=new HashMap();
       String strMessage=null;
       //DbmsOutput dbmsOutput = null;
       Connection conn = null;
       OracleCallableStatement cstmt = null;
       ResultSet rs = null;
       String sqlStr = "BEGIN ORDERS.NP_ORDERS12_PKG.SP_GET_SHARED_INSTALATION_LST(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); END;";
       try{               
          conn  = Proveedor.getConnection();
          //dbmsOutput = new DbmsOutput(conn);
          //dbmsOutput.enable(1000000);
          cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
          
          
          if( objAddressObjB.getAddressId() == 0 )
           cstmt.setNull(1,OracleTypes.NUMBER);
          else
           cstmt.setLong(1,objAddressObjB.getAddressId());
          
          cstmt.setString(2, objAddressObjB.getSwaddress1());
          cstmt.setString(3, objAddressObjB.getSwaddress2());
          cstmt.setString(4, objAddressObjB.getNpdepartmentname());
          cstmt.setString(5, objAddressObjB.getNpprovincename());
          cstmt.setString(6, objAddressObjB.getNpdistrictname());
          cstmt.setLong(7, objAddressObjB.getNpnumregisters());
          cstmt.setLong(8, objAddressObjB.getNpnumpage());       
          cstmt.registerOutParameter(9,OracleTypes.NUMBER);
          cstmt.registerOutParameter(10,OracleTypes.CURSOR);
          cstmt.registerOutParameter(11,OracleTypes.VARCHAR);
          
          cstmt.execute();
          //dbmsOutput.show();
          
          strMessage = cstmt.getString(11);
          
          if( strMessage == null ){
            numTotalRegisters = cstmt.getInt(9);
            rs = (ResultSet)cstmt.getObject(10);
            while (rs.next()) {
                HashMap h = new HashMap();           
                h.put("SharedInstallId", rs.getInt("SharedInstallId")+"");
                h.put("SharedInstallName", rs.getString("SharedInstallName"));           
                h.put("Address", rs.getString("Address"));             
                h.put("Departamento", rs.getString("Departamento"));  
                h.put("Provincia",rs.getString("Provincia"));
                h.put("Distrito",rs.getString("Distrito"));
                list.add(h);
            }
          }
       }
       catch(Exception e){
         throw new Exception(e);
      }
      finally{
       closeObjectsDatabase(conn, cstmt, rs);
      }
       hshResult.put("numTotalRegisters",""+numTotalRegisters);
       hshResult.put("objSharedInstallList",list);
       hshResult.put("strMessage",strMessage);       
       return hshResult;
    }


    /**
      * Motivo: Obtiene lista de direcciones segun Codigo del cliente o de Site, tipo de dirección , tipo CUSTOMER O SITE 
      * <br>Realizado por: <a href="mailto:jose.rondon@nextel.com.pe">Israel Rondón</a>
      * <br>Fecha: 23/10/2007
      * @param     iswObjectId        -- [SWCUSTOMERID|SITEID] 
      * @param     sTipoDirCustomer   -- [10 Dir.Empresa|70 Dir.Correspondencia|60 Dir.Intalación]
      * @param     sSwObjectType      -- [CUSTOMER|SITE]
      * @param     strMsgError   Contiene el mensaje de error si existiera
      * @return    HashMap 
    */
    public  HashMap getAddress(int iswObjectId,String sTipoDirCustomer,String sSwObjectType,long lngSpecificationId)
    throws SQLException, Exception{

       ArrayList list = new ArrayList();
       Connection conn = null;
       OracleCallableStatement cstmt = null;
       ResultSet rs = null;
       HashMap hshResult=new HashMap();
       String strMessage=null;
              
       String sqlStr = "BEGIN ORDERS.NP_ORDERS12_PKG.SP_GET_ADDRESS(?, ?, ?, ?, ?, ?); END;";
       try{
          conn = Proveedor.getConnection();
   
          cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
          
          cstmt.setInt(1, iswObjectId);
          cstmt.setString(2, sTipoDirCustomer);
          cstmt.setString(3, sSwObjectType);
          cstmt.setLong(4, lngSpecificationId);
          
          cstmt.registerOutParameter(5, OracleTypes.CURSOR);
          cstmt.registerOutParameter(6, OracleTypes.VARCHAR);
          
          cstmt.execute();
   
          strMessage = cstmt.getString(6);
          rs = (ResultSet)cstmt.getObject(5);
   
          while (rs.next()) {
   
              HashMap h = new HashMap();
              h.put("swaddressid", rs.getInt("swaddressid")+"");
              h.put("swaddress1", rs.getString("swaddress1"));
              h.put("swaddress2", rs.getString("swaddress2"));           
              h.put("Departamento", rs.getString("Departamento"));  
              h.put("Provincia",rs.getString("Provincia"));
              h.put("Distrito",rs.getString("Distrito"));
              list.add(h);
          }
       }
       catch(Exception e){
         throw new Exception(e);
       }
       finally{
         closeObjectsDatabase(conn, cstmt, rs);
       }
       hshResult.put("objAddressList",list);
       hshResult.put("strMessage",strMessage);       
       return hshResult;
    }
    
    /**
      * Motivo: Obtiene direcciones a partir de del id de la dirección
      * <br>Realizado por: <a href="mailto:jose.rondon@nextel.com.pe">Israel Rondón</a>
      * <br>Fecha: 25/10/2007
      * @param     iswAddressId         IN NUMBER,    -- [Id de la Dirección]
      * @param     sAddresstype         IN VARCHAR2,  -- [DIRECCION][COMPARTIDA]
      * @param     strMsgError   Contiene el mensaje de error si existiera
      * @return    HashMap 
    */
    public  HashMap getAddressPuntual(int iswAddressId,String sAddresstype)
    throws SQLException, Exception {

       String sswAddressName  = new String();
       Connection conn = null;
       OracleCallableStatement cstmt = null;
       HashMap hshResult=new HashMap();
       String strMessage=null;
       String sqlStr = "BEGIN ORDERS.NP_ORDERS12_PKG.SP_GET_ADDRESS_PUNTUAL(?, ?, ?, ?); END;";
       try{
          conn = Proveedor.getConnection();
          cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         
          cstmt.setInt(1, iswAddressId);
          cstmt.setString(2, sAddresstype);
          cstmt.registerOutParameter(3, Types.CHAR);
          cstmt.registerOutParameter(4, Types.CHAR);
          
          cstmt.execute();
          sswAddressName = cstmt.getString(3);
          strMessage = cstmt.getString(4);
       }
       catch(Exception e){
         throw new Exception(e);
       }
       finally{
			closeObjectsDatabase(conn, cstmt, null);
          /*if (cstmt != null)
              cstmt.close();
          if (conn != null)
              conn.close();*/
       }     
       hshResult.put("sswAddressName",sswAddressName);
       hshResult.put("strMessage",strMessage);       
       return hshResult;
    }
  
  /*Inicio cambios JPEREZ*/
    /**
   * Motivo: Verifica si el codigo de contrato existe para un usuario N
   * <br>Realizado por: <a href="mailto:jose.rondon@nextel.com.pe">Israel Rondón</a>
   * <br>Fecha: 29/10/2007
   * 
   * @throws java.lang.Exception
   * @throws java.sql.SQLException
   * @return 
   * @param lngInstallAddressId
   * @param lngSolutionId
   * @param lngSpecificationId
   * @param lngContractId
   * @param lngCustomerID
   */
    public  HashMap getValidateContract(long lngCustomerID, long lngContractId , long lngSpecificationId, long lngSolutionId, long lngInstallAddressId ) throws SQLException, Exception{

       String sValidContract  = new String();
       ItemBean objItemBean = new ItemBean();
       Connection conn = null;
       OracleCallableStatement cstmt = null;
       HashMap hshResult=new HashMap();
       String strMessage = null;
       String sqlStr = "BEGIN ORDERS.NP_ORDERS12_PKG.SP_VALIDATE_CONTRACT(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); END;";
       try{
          conn = Proveedor.getConnection();
          cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
          cstmt.setLong(1, lngCustomerID);
          cstmt.setLong(2, lngContractId);
          cstmt.setLong(3, lngSpecificationId);
          cstmt.setLong(4, lngInstallAddressId);
          
          cstmt.registerOutParameter(5, OracleTypes.NUMBER);
          cstmt.registerOutParameter(6, OracleTypes.NUMBER);
          cstmt.registerOutParameter(7, OracleTypes.VARCHAR);
          cstmt.registerOutParameter(8, OracleTypes.NUMBER);
          cstmt.registerOutParameter(9, OracleTypes.VARCHAR);
          cstmt.registerOutParameter(10, OracleTypes.VARCHAR);
          cstmt.registerOutParameter(11, OracleTypes.VARCHAR);
          
          cstmt.executeUpdate();
          
          strMessage = cstmt.getString(11);
          if( strMessage == null ){
           objItemBean.setNpsolutionid(cstmt.getLong(5));  //EZUBIAURR 27/12 
           objItemBean.setNporiginalplanid(cstmt.getLong(6));
           objItemBean.setNporiginalplanname(cstmt.getString(9));
           objItemBean.setNporigmainservice(cstmt.getLong(8));
           objItemBean.setNporigmainservicedesc(cstmt.getString(7));
           objItemBean.setNpitemservices(cstmt.getString(10));
          }
       }
       catch(Exception e){
         throw new Exception(e);
       }
       finally{
			closeObjectsDatabase(conn, cstmt, null);
       }         
       hshResult.put("objItemBean",objItemBean);
       hshResult.put("strMessage",strMessage);       
       return hshResult;
    }
    
        /**
      * Motivo: Verifica el teléfono (VoIP) es del cliente
      * <br/>Realizado por: <a href="mailto:jorge.perez@nextel.com.pe">Jorge Pérez</a>
      * <br/>Fecha: 27/05/2009
    */
    public  HashMap getValidatePhoneVoIp(long lngCustomerID, String strPhoneNumber , long lngSpecificationId,long lngSolutionId, long lngInstallAddressId ) throws SQLException, Exception{
              
       Connection conn = null;
       OracleCallableStatement cstmt = null;
       HashMap hshResult=new HashMap();
       String strMessage = null;
       String sqlStr = "BEGIN ORDERS.NP_ORDERS22_PKG.SP_VALIDATE_PHONE_VOIP(?,?,?,?,?,?,?); END;";
       try{
          conn = Proveedor.getConnection();
          cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
          cstmt.setLong(1, lngCustomerID);
          cstmt.setString(2, strPhoneNumber);
          cstmt.setLong(3, lngSpecificationId);
          if (lngSolutionId == 0)
            cstmt.setLong(4, lngSolutionId);
          else
            cstmt.setNull(4,OracleTypes.NUMBER);
          if (lngInstallAddressId == 0)
            cstmt.setLong(5, lngInstallAddressId);
          else 
            cstmt.setNull(5,OracleTypes.NUMBER);
          cstmt.registerOutParameter(6, OracleTypes.VARCHAR);
          cstmt.registerOutParameter(7, OracleTypes.VARCHAR);
         
          cstmt.executeUpdate();
          
          strMessage = cstmt.getString(7);
          if( strMessage == null ){
           hshResult.put("strPhoneStatus",cstmt.getString(6));
          }         
       }
       catch(Exception e){
         throw new Exception(e);
       }
       finally{
			closeObjectsDatabase(conn, cstmt, null);
       }         
       
       hshResult.put("strMessage",strMessage);       
       return hshResult;
    }    
   /*Fin cambios JPEREZ*/ 
   
  /**
    * Motivo: Verifica si el codigo de contrato existe para un usuario N
    * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
    * <br>Fecha: 06/11/2007
    * @param     iPlanId        IN NUMBER,    -- [Customer id]
    * @param     strMsgError    Contiene el mensaje de error si existiera
    * @return    HashMap 
  */
  //johncmb inicio se modifico el parametro de entrada getServiceList
  public  HashMap getServiceList(int intSolutionId, int intPlanId, int intProductId, String strSSAAType,String strType) throws SQLException, Exception{
    System.out.println("[GeneralDAO][getServiceList][intSolutionId]"+intSolutionId);
    System.out.println("[GeneralDAO][getServiceList][intPlanId]"+intPlanId);
    System.out.println("[GeneralDAO][getServiceList][intProductId]"+intProductId);
    System.out.println("[GeneralDAO][getServiceList][intSSAAType]"+strSSAAType);     
     ArrayList list = null;
     ServiciosBean  serviciosBean = null;
     ResultSet rs = null;
     Connection conn = null;
     OracleCallableStatement cstmt = null;
     HashMap hshResult=new HashMap();
     String strMessage=null;
     String sqlStr = "BEGIN ORDERS.NP_ORDERS12_PKG.SP_GET_SERVICE_LIST(?, ?, ?, ?, ?, ?, ?); END;";//johncmb    
     
     
     try{
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
       
        cstmt.setInt(1, intSolutionId);                      //[NPPLANCODE]
        cstmt.setInt(2, intPlanId);                      //[NPSOLUTIONID]
        cstmt.setInt(3, intProductId);                      //[NPSOLUTIONID]
        cstmt.setString(4, strSSAAType);                      //[NPSOLUTIONID] johncmb
        cstmt.setString(5, strType);
        cstmt.registerOutParameter(6,OracleTypes.CURSOR);
        cstmt.registerOutParameter(7, Types.CHAR);
        cstmt.execute();
   
        strMessage = cstmt.getString(7);
        if( strMessage == null){
          rs = (ResultSet)cstmt.getCursor(6);
          list = new ArrayList();       
          while (rs.next()) {           
            serviciosBean = new ServiciosBean();                
            serviciosBean.setNpservicioid(rs.getLong("npservicioid"));
            serviciosBean.setNpnomserv(rs.getString("npnomserv"));
            serviciosBean.setNpnomcorserv(rs.getString("npnomcorserv"));
            serviciosBean.setNpexcludingind(rs.getString("npexcludingind"));               
            serviciosBean.setNpduration(rs.getInt("npduration"));
            serviciosBean.setNpgroup(rs.getString("npgroup")); // johncmb  inicio
            list.add(serviciosBean);           
          }
        }
             
     }catch(Exception e){
         throw new Exception(e);
     }finally{
        closeObjectsDatabase(conn, cstmt, rs);
     }
     
     hshResult.put("objServiceList",list);
     hshResult.put("strMessage",strMessage);       
     return hshResult;
  }
  
  /**
   Method : getOrderDeact
   Purpose: Obtiene la orden de desactivacion del servicio de suscripciones.
   Developer                Fecha       Comentario
   =====================    ==========  ======================================================================
   Lazo de la Vega David    23/09/2010  Creación
   */
   
  public  HashMap getOrderDeact(long intOrderId, String strPhone, long intServiceId) throws SQLException, Exception{
     
     System.out.println("[GeneralDAO][getServiceList][intOrderId]"+intOrderId);
     System.out.println("[GeneralDAO][getServiceList][strPhone]"+strPhone);
     System.out.println("[GeneralDAO][getServiceList][intServiceId]"+intServiceId);  
     long orderDeact = 0;
     Connection conn = null;
     OracleCallableStatement cstmt = null;
     HashMap hshResult=new HashMap();
     String strMessage=null;
     String sqlStr = "BEGIN ORDERS.NP_ORDERS12_PKG.SP_GET_ORDER_DEACTIVATION(?, ?, ?, ?, ?); END;";
     try{
        conn = Proveedor.getConnection(); 
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
       
        cstmt.setLong(1, intOrderId); 
        cstmt.setString(2, strPhone); 
        cstmt.setLong(3, intServiceId);
        cstmt.registerOutParameter(4,Types.NUMERIC);
        cstmt.registerOutParameter(5, Types.CHAR);
        cstmt.execute();
   
        strMessage = cstmt.getString(5);
        if( strMessage == null){
          orderDeact = cstmt.getLong(4);          
        }
        
        System.out.println("[getOrderDeactList][orderDeact] "+orderDeact); 

     }catch(Exception e){
         throw new Exception(e);
     }finally{
        closeObjectsDatabase(conn, cstmt, null);
     }
     
     hshResult.put("orderDeact",""+orderDeact);
     hshResult.put("strMessage",strMessage);
     return hshResult;
  }
  
  /**
   Method : getServiceActive
   Purpose: Obtiene un list con el detalle de los servicios activos por número de telefono.
   Developer                Fecha       Comentario
   =====================    ==========  ======================================================================
   Lazo de la Vega David    01/10/2010  Creación
   */
   public  HashMap getServiceActive(String strPhone, long lCustomerId, long lSpecificationId, long lSiteId) throws SQLException, Exception{
     
     System.out.println("[GeneralDAO][getServiceActive][strPhone]"+strPhone);
     System.out.println("[GeneralDAO][getServiceActive][lCustomerId]"+lCustomerId);
     System.out.println("[GeneralDAO][getServiceActive][lSpecificationId]"+lSpecificationId);  
     System.out.println("[GeneralDAO][getServiceActive][lSiteId]"+lSiteId);
     
     Connection conn = null;
     OracleCallableStatement cstmt = null;
     HashMap hshResult=new HashMap();
     String strMessage=null;
     String strSSAAContrato="";
     String sqlStr = "BEGIN ORDERS.NP_ORDER_LOGIC_ACT_DEACT_PKG.SP_GET_SERVICE_ACTIVE(?, ?, ?, ?, ?, ?); END;";
     try{
        conn = Proveedor.getConnection(); 
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        
        cstmt.setString(1, strPhone); 
        cstmt.setLong(2, lCustomerId); 
        cstmt.setLong(3, lSpecificationId);
        cstmt.setLong(4, lSiteId);
        cstmt.registerOutParameter(5,Types.CHAR);
        cstmt.registerOutParameter(6, Types.CHAR);
        cstmt.execute();
        
        strMessage = cstmt.getString(6);
        if( strMessage == null){
          strSSAAContrato = cstmt.getString(5);          
        }
        
     }catch(Exception e){
         throw new Exception(e);
     }finally{
        closeObjectsDatabase(conn, cstmt, null);
     }
     
     hshResult.put("strSSAAContrato",strSSAAContrato);
     hshResult.put("strMessage",strMessage);
     return hshResult;
  }
  
  /**
   Method : getSubscriptionList
   Purpose: Obtiene un lista de todos los servicios para suscripciones.
   Developer                Fecha       Comentario
   =====================    ==========  ======================================================================
   Lazo de la Vega David    01/10/2010  Creación
   */
  public HashMap getSubscriptionList(String strprocessing, String strgroup) throws SQLException, Exception{
    
     System.out.println("[GeneralDAO][getSubscriptionList][strprocessing]"+strprocessing);  
     System.out.println("[GeneralDAO][getSubscriptionList][strgroup]"+strgroup);
     
     ArrayList list = null;
     ServiciosBean  serviciosBean = null;
     ResultSet rs = null;
     Connection conn = null;
     OracleCallableStatement cstmt = null;
     HashMap hshResult=new HashMap();
     String strMessage=null;
     String strSSAAContrato="";
     String sqlStr = "BEGIN ORDERS.NP_ORDER_LOGIC_ACT_DEACT_PKG.SP_GET_SUPSCRIPTION_LIST(?, ?, ?); END;";
     try{
        conn = Proveedor.getConnection(); 
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        
        //cstmt.setString(1, strprocessing); 
        cstmt.setString(1, strgroup);
        cstmt.registerOutParameter(2,OracleTypes.CURSOR);
        cstmt.registerOutParameter(3, Types.CHAR);
        cstmt.execute();
        
        strMessage = cstmt.getString(3);
        if( strMessage == null){
          rs = (ResultSet)cstmt.getCursor(2);
          list = new ArrayList();       
          while (rs.next()) {           
            serviciosBean = new ServiciosBean();                
            serviciosBean.setNpservicioid(rs.getLong("npservicioid"));
            serviciosBean.setNpnomserv(rs.getString("npnomserv"));
            serviciosBean.setNpnomcorserv(rs.getString("npnomcorserv"));
            serviciosBean.setNpsalesstartdate(rs.getDate("npsalesstartdate")); 
            serviciosBean.setNpduration(rs.getInt("npduration"));
            list.add(serviciosBean);           
          }
        }
        
        }catch(Exception e){
             throw new Exception(e);
         }finally{
            closeObjectsDatabase(conn, cstmt, rs);
         }
         
         hshResult.put("objSubscriptionList",list);
         hshResult.put("strMessage",strMessage);       
         return hshResult;
    
  } 
  
  public HashMap getItem(long lServicioId, String strPhone) throws SQLException, Exception{
    
    System.out.println("[GeneralDAO][getSubscriptionList][lServicioId]"+lServicioId);  

    ItemBean  itemBean = null;
    ResultSet rs = null;
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    HashMap hshResult=new HashMap();
    String strMessage=null;
    String sqlStr = "BEGIN ORDERS.NP_ORDER_LOGIC_ACT_DEACT_PKG.SP_GET_ITEM(?, ?, ?, ?); END;";
    try{
      conn = Proveedor.getConnection(); 
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        
      cstmt.setLong(1, lServicioId); 
      cstmt.setString(2, strPhone);
      cstmt.registerOutParameter(3,OracleTypes.CURSOR);
      cstmt.registerOutParameter(4, Types.CHAR);
      cstmt.execute();
      
      strMessage = cstmt.getString(4);
      if( strMessage == null){
        rs = (ResultSet)cstmt.getCursor(3);
        if (rs.next()) {           
          itemBean = new ItemBean();                
          itemBean.setNpitemid(rs.getLong("npitemid"));
          itemBean.setNpphone(rs.getString("npphone"));
          itemBean.setNpactivationdate(rs.getDate("npactivationdate"));
          itemBean.setNpdeactivationdate(rs.getDate("npdeactivationdate")); 
          itemBean.setNpmissingdays(rs.getString("missingdays"));
        }
      }
      
    }catch(Exception e){
         throw new Exception(e);
     }finally{
        closeObjectsDatabase(conn, cstmt, rs);
     }
     
     hshResult.put("itemBean",itemBean);
     hshResult.put("strMessage",strMessage);       
     return hshResult;
  
  }
  
  /**
   Method : getProcessTypeByOrderId
   Purpose: Obtiene processType de la orden que indica si es una Orden de Activación o Desactivación.
   Developer                Fecha       Comentario
   =====================    ==========  ======================================================================
   Lazo de la Vega David    28/12/2010  Creación
   */
   public HashMap getProcessTypeByOrderId(long lOrderId) throws SQLException, Exception{
    
      System.out.println("[GeneralDAO][getProcessTypeByOrderId][lOrderId]"+lOrderId);
      ResultSet rs = null;
      Connection conn = null;
      OracleCallableStatement cstmt = null;
      HashMap hshResult = new HashMap();
      String strMessage = null;
      String strProcessType = "";
      String sqlStr = "BEGIN ORDERS.NP_ORDER_LOGIC_ACT_DEACT_PKG.SP_GET_PROCESSTYPE_ORDER(?, ?, ?); END;";
      try{
        conn = Proveedor.getConnection(); 
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        
        cstmt.setLong(1, lOrderId);
        cstmt.registerOutParameter(2, Types.CHAR);
        cstmt.registerOutParameter(3, Types.CHAR);
        cstmt.execute();
        
        strMessage = cstmt.getString(3);
        if( strMessage == null){
          strProcessType = cstmt.getString(2);          
        }
        
     }catch(Exception e){
         throw new Exception(e);
     }finally{
        closeObjectsDatabase(conn, cstmt, null);
     }
     
     hshResult.put("strProcessType",strProcessType);
     hshResult.put("strMessage",strMessage);
     return hshResult;
    
   }
  
    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  ITEMS DE SECCIONES DINAMICAS - FIN
     *  REALIZADO POR: ISRAEL RONDON
     *  FECHA: 25/10/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
     
   /***********************************************************************
   ***********************************************************************
   ***********************************************************************
   *  EXCEPCIONES - INICIO
   *  REALIZADO POR: Jorge Pérez
   *  FECHA: 26/10/2007
   ***********************************************************************
   ***********************************************************************
   ***********************************************************************/
   
   /**
    * Motivo: Obtiene datos de servicios determinados. Los devuelve en un arreglo de objetos
    * <br>Realizado por: <a href="mailto:jorge.perez@nextel.com.pe">Jorge Pérez</a>
    * <br>Fecha: 25/10/2007
    * @param pstrServicesIds
    * @return HashMap
    * @throws java.sql.SQLException
    */     
   public HashMap getServiceList(String pstrServicesIds) throws Exception, SQLException{
      HashMap hashMap = new HashMap();        
      Connection conn = null; 
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;
      String strMessage = null;
      ArrayList list = new ArrayList();
      
      String sqlStr = "BEGIN ORDERS.SPI_GET_SERVICE_LIST(?, ?, ?); END;";  
      try{
         conn = Proveedor.getConnection();          
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         cstmt.setString(1, pstrServicesIds);
         cstmt.registerOutParameter(2, OracleTypes.CURSOR);
         cstmt.registerOutParameter(3, Types.CHAR);
            
         cstmt.executeUpdate();
            
         rs = (ResultSet)cstmt.getObject(2);
         strMessage = cstmt.getString(3);
             
         while ( rs.next() ){
            ServiciosBean servicioBean = new ServiciosBean();
            servicioBean.setNpnomcorserv(MiUtil.getString(rs.getString("npnomcorserv")));
            servicioBean.setNpservicioid(rs.getLong("npservicioid"));
            servicioBean.setNpnomserv(MiUtil.getString(rs.getString("npnomserv")));         
            servicioBean.setNpexcludingflg(MiUtil.getString(rs.getString("npexcludingflg")));
            servicioBean.setNpexcludingind(MiUtil.getString(rs.getString("npexcludingind")));
            list.add(servicioBean);
         }
         hashMap.put("objArrayList", list);
         hashMap.put("strMessage", strMessage);
      }
      catch(Exception e){
         throw new Exception(e);
     }
     finally{
		closeObjectsDatabase(conn, cstmt, rs);
      /*if (rs != null)
         rs.close();   
      if (cstmt != null)
         cstmt.close();        
      if (conn != null)
         conn.close();*/
     } 
      return hashMap;
   }  
   /***********************************************************************     
   ***********************************************************************
   *  EXCEPCIONES - FIN
   *  REALIZADO POR: Jorge Pérez
   *  FECHA: 26/10/2007
   ***********************************************************************     
   ***********************************************************************/ 
   
   /***********************************************************************     
   ***********************************************************************
   *  ADENDAS - INICIO
   *  REALIZADO POR: Estefanía Gamonal
   *  FECHA: 28/11/2007
   ***********************************************************************     
   ***********************************************************************/ 
      
    /**
     * Motivo:  Obtiene el "npvalue" teniendo como dato de entrada
     *          el campo "npvaluedesc" y el campo "nptable" en la tabla SWBAPPS.NP_TABLE
     * <br>Realizado por: <a href="mailto:estefania.gamonal@nextel.com.pe">Estefanía Gamonal</a>
     * <br>Fecha: 19/07/2007
     * 
     * @param      strValueDesc     Ej: Promoción
     * @param      strTable         Ej: TIPOADENDA
     * @return     strValue         Ej: 1
     */
    public String getValue(String strTable, String strValueDesc)  throws Exception, SQLException{
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String result = "";
        String strMessage = null;
        
        String sqlStr = "BEGIN SWBAPPS.SPI_GET_VALUE2(?,?,?,?); END;";
        try{
           conn = Proveedor.getConnection();
           cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
           cstmt.setString(1, strTable);
           cstmt.setString(2, strValueDesc);
           cstmt.registerOutParameter(3, Types.CHAR);
           cstmt.registerOutParameter(4, Types.CHAR);
           cstmt.executeQuery();
           strMessage = cstmt.getString(3);
           if( strMessage == null)
             result = cstmt.getString(4);
           else
             result = Constante.NPERROR;
        }
        catch(Exception e){
         throw new Exception(e);
     }
     finally{
		closeObjectsDatabase(conn, cstmt, null);
        /*if(cstmt!=null)
          cstmt.close();
        if(conn!=null)
          conn.close();*/
     }   
        return result;
    }
   /***********************************************************************     
   ***********************************************************************
   *  ADENDAS - FIN
   *  REALIZADO POR: Estefanía Gamonal
   *  FECHA: 28/11/2007
   ***********************************************************************     
   ***********************************************************************/ 

	/**
     * Motivo: Obtiene la data según el np_table
     * <br>Realizado por: <a href="mailto:cristian.espinoza@nextel.com.pe">Cristian Espinoza</a>
     * <br>Fecha: 20/02/2008
     * 
     * @param  strTableName, strValueDesc
     * @return HashMap
     * @throws java.sql.SQLException      
     */
    public HashMap getDataNpTable(String strTableName, String strValueDesc) throws SQLException, Exception {


		HashMap hshDataMap = new HashMap();
		ArrayList arrNpTable = new ArrayList();
		Connection conn = null;
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
		//DbmsOutput dbmsOutput = null;
		String strMessage = null;
		String sqlStr = "BEGIN SWBAPPS.SPI_GET_NP_TABLE_X_NPVALUEDESC(?,?,?,?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);		
         cstmt.setString(1,strTableName);
         cstmt.setString(2,strValueDesc);
         cstmt.registerOutParameter(3, OracleTypes.CURSOR);
         cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
         cstmt.executeQuery();
         
         strMessage = cstmt.getString(4);
       
         if (StringUtils.isBlank(strMessage)) {	
            rs = (ResultSet)cstmt.getObject(3);
            
            while (rs.next()) {
                  NpTableBean nptableBean = new NpTableBean();
                  nptableBean.setNpvalue((rs.getString("npvalue")));
                  nptableBean.setNptag1((rs.getString("nptag1")));
                  nptableBean.setNptag2((rs.getString("nptag2")));
                  arrNpTable.add(nptableBean);			                
            }
         }
      }
      catch(Exception e){
         throw new Exception(e);
     }
     finally{
       closeObjectsDatabase(conn, cstmt, rs);
     }
      hshDataMap.put("objArrayList", arrNpTable);
      hshDataMap.put("strMessage", strMessage);		
      hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
      
      return hshDataMap;
    }    
    
   /**
    * Motivo: Resuelve la validación de un vendedor correspondiente a cierto cliente
    * <br>Realizado por: <a href="mailto:oliver.dubock@nextel.com.pe">Odubock</a>
    * <br>Fecha: 28/02/2008
    * @param     long    lngCustomerId
    * @param     long    lngSiteId
    * @param     long    lngSalesmanId
    * @param     string  strVendedor
    * @return    HashMap objHashMap 
    */
    public HashMap getValidateSalesman(long lngCustomerId,long lngSiteId,long lngSalesmanId,String strVendedor,int iUserId,int iAppId) throws Exception,SQLException{
       HashMap  objHashMap = new HashMap();
       Connection conn = null; 
       OracleCallableStatement cstm = null;
       String strMessage,strVendedorDefect = null;
     
          String sqlStr = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_VALIDATE_SALES_NAME(?,?,?,?,?,?,?,?); END;";
          try{
             conn = Proveedor.getConnection();          
             cstm = (OracleCallableStatement) conn.prepareCall(sqlStr);
             
             if( lngCustomerId == 0 )
               cstm.setNull(1,OracleTypes.NUMBER);
             else
               cstm.setLong(1,lngCustomerId);
             
             if( lngSiteId == 0 )
               cstm.setNull(2,OracleTypes.NUMBER);
             else
               cstm.setLong(2,lngSiteId);
             
             cstm.setLong(3,lngSalesmanId);
             cstm.setString(4,strVendedor);
             cstm.setInt(5,iUserId);
             cstm.setInt(6,iAppId);
             cstm.registerOutParameter(7,OracleTypes.INTEGER);
             cstm.registerOutParameter(8,OracleTypes.VARCHAR);
            
             cstm.executeUpdate();
             strVendedorDefect = cstm.getString(7);
             strMessage        = cstm.getString(8);
             objHashMap.put("strVendDefect",strVendedorDefect);
             objHashMap.put("strMessage2",strMessage);
          }
          catch(Exception e){
             throw new Exception(e);
          }
          finally{
            closeObjectsDatabase(conn, cstm, null);
          }
       return objHashMap;
    }
    
    /**
    * Motivo: Obtiene el permiso para crear Sites según el customer y vendedor
    * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
    * <br>Fecha: 30/10/2007
    * @param     long lngSellerId  
    * @param     long lngCustomerId    
    * @param     long lngSiteId    
    * @return    HashMap HashMap 
   **/
    public HashMap getCheckPermission(long lngSellerId,long lngCustomerId, long lngSiteId) throws Exception,SQLException{
       
      Connection conn = null; 
      OracleCallableStatement cstm = null;
      String strMessage = null;
      HashMap objHashMap =  new HashMap();
      int intResult = -1;
      String sqlStr = "{ ? = call WEBSALES.FXI_CHECK_PERMISSION(?,?,?,?)}";
      try{  
         conn = Proveedor.getConnection();
           
         cstm = (OracleCallableStatement) conn.prepareCall(sqlStr);
                    
         cstm.registerOutParameter(1,OracleTypes.NUMBER);

         if( lngSellerId == 0 )
           cstm.setNull(2,OracleTypes.NUMBER);
         else
           cstm.setLong(2,lngSellerId);
         
         if( lngCustomerId==0 )
           cstm.setNull(3,OracleTypes.NUMBER);
         else
           cstm.setLong(3,lngCustomerId);
         
         if( lngSiteId == 0 )
           cstm.setNull(4,OracleTypes.NUMBER);
         else
           cstm.setLong(4,lngSiteId);
           
         cstm.registerOutParameter(5,OracleTypes.VARCHAR);
           
         cstm.executeUpdate();
           
         intResult = cstm.getInt(1);
      }
      catch(Exception e){
         throw new Exception(e);
     }
     finally{
      closeObjectsDatabase(conn, cstm, null);
     }  
      objHashMap.put("strMessage",strMessage);
      objHashMap.put("intResult",""+intResult);
      
      return objHashMap;                                                                
    }    
 
 /**
* Motivo: Devuelve el tipo de oportunidad
* <br>Realizado por: <a href="mailto:hugo.moreno@nextel.com.pe">Hugo Moreno</a>
* <br>Fecha: 20/05/2008
* @param     long    
* @return    int 
*/
   public HashMap getNpopportunitytypeid(long lOppontunityId) 
   throws SQLException, Exception
   {
   int iReturnValue = 0;
   int iSalesId = 0;
   String strMessage = null;
   HashMap objHashMap =  new HashMap();
   Connection conn=null;
   OracleCallableStatement cstmt = null;
   
   String sqlStr = "BEGIN WEBSALES.NPSL_OPPORTUNITY_PKG.SP_GET_NPOPPORTUNITYTYPEID( ?, ?, ?, ?); END;";
   try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      System.out.println("[GeneralDAO][lOppontunityId]"+lOppontunityId);
      cstmt.setLong(1, lOppontunityId);
      cstmt.registerOutParameter(2, Types.NUMERIC);
      cstmt.registerOutParameter(3, Types.NUMERIC);
      cstmt.registerOutParameter(4, Types.VARCHAR);          
      cstmt.execute();
      
      iReturnValue = cstmt.getInt(2); 
      iSalesId = cstmt.getInt(3); 
      strMessage = cstmt.getString(4); 
      System.out.println("[GeneralDAO][iReturnValue]"+iReturnValue);
      System.out.println("[GeneralDAO][iSalesId]"+iSalesId);
      System.out.println("[GeneralDAO][strMessage]"+strMessage);
   }   
   catch(Exception e){
      throw new Exception(e);
   }
   finally{
		closeObjectsDatabase(conn, cstmt, null);
      /*if(cstmt != null)
         cstmt.close();
      if(conn != null)
         conn.close();  */
   }
   objHashMap.put("iReturnValue",""+iReturnValue);
   objHashMap.put("iSalesId",""+iSalesId);
   objHashMap.put("strMessage",strMessage);
   return objHashMap;
}  

/**
* Motivo: Dado el tipo de oportunidad verifica si se necesita autorización para la creación de sites
* <br>Realizado por: <a href="mailto:hugo.moreno@nextel.com.pe">Hugo Moreno</a>
* <br>Fecha: 20/05/2008
* @param     long, int    
* @return    int 
*/
   public int getValidateAuthorization(long lOppontunityId, int iOppType, int iSalesID) 
   throws SQLException, Exception
   {
   int iReturnValue = 0;
   Connection conn=null;
   OracleCallableStatement cstmt = null;
   
   String sqlStr = "BEGIN WEBSALES.NPSL_OPPORTUNITY_PKG.SP_VALIDATE_AUTHORIZATION( ?, ?, ?, ?); END;";
   try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      
      cstmt.setLong(1, lOppontunityId);
      cstmt.setInt(2, iOppType);
      cstmt.setInt(3, iSalesID);
      cstmt.registerOutParameter(4, Types.NUMERIC);          
      cstmt.execute();
      
      iReturnValue = cstmt.getInt(4); 
   }
   catch(Exception e){
      throw new Exception(e);
   }
   finally{   
		closeObjectsDatabase(conn, cstmt, null);
      /*if(cstmt != null)
         cstmt.close();
      if(conn != null)
         conn.close();  */
   }   
   return iReturnValue;
}  

	/**
	Method : selectResponsablePago
	Purpose: Validar si debe seleccionar necesariamente el responsable de pago (en el caso de cuentas large)
				para la categoria especificada.
	Developer              Fecha       Comentario
	=============          ==========  ======================================================================
	Cristian Espinoza      05/06/2008  Creación
	*/

	public HashMap selectResponsablePago(long lSpecificationId, String strOrigenOrden,long lValor,String strOpcion,long lngCustomerId,String strTypeCustomer)  
    throws SQLException, Exception {

		ArrayList list = new ArrayList();
      Connection conn = null; 
      OracleCallableStatement cstmt = null;      
      String strMessage = null;
      AddressObjectBean objAddressObjectBean = null;
      //DbmsOutput dbmsOutput = null;
		int iSelectRP;
      String sqlStr = "BEGIN ORDERS.NP_ORDERS07_PKG.SP_SELECT_RESPONSABLE_PAGO(?, ?, ?, ?, ?, ?, ?, ?); END;";		
      try{
         conn = Proveedor.getConnection();      
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);      		
         //dbmsOutput = new DbmsOutput(conn);
         //dbmsOutput.enable(1000000);		
         cstmt.setLong(1,lSpecificationId);
         cstmt.setString(2,strOpcion);
         cstmt.setString(3,strOrigenOrden);
         cstmt.setLong(4,lValor);
         cstmt.setLong(5,lngCustomerId);
         cstmt.setString(6,strTypeCustomer);
         cstmt.registerOutParameter(7, OracleTypes.NUMBER);		
         cstmt.registerOutParameter(8, OracleTypes.VARCHAR);      
         cstmt.executeUpdate();
         //dbmsOutput.show();
         //dbmsOutput.close();
         strMessage = cstmt.getString(8);              
         iSelectRP = cstmt.getInt(7);  
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, null);        
      }
		if (logger.isDebugEnabled()){
			logger.debug("lSpecificationId: "+lSpecificationId);
			logger.debug("strOrigenOrden: "+strOrigenOrden);
			logger.debug("lValor: "+lValor);
			logger.debug("strOpcion: "+strOpcion);			
      logger.debug("lngCustomerId: "+lngCustomerId);
			logger.debug("strTypeCustomer: "+strTypeCustomer);			
			logger.debug("strSelectRP: "+iSelectRP+"");
			logger.debug("strMessage: "+strMessage);
		}		
      HashMap hshResultado  = new HashMap();    
      hshResultado.put("strSelectRP",iSelectRP+"");
      hshResultado.put("strMessage",strMessage);
         
      return hshResultado;
	
    }     

	/**
	Method : getObjectTypeUrl
	Purpose: Obtiene el URL que enlaza al objeto creador de una orden
	Developer              Fecha       Comentario
	=============          ==========  ======================================================================
	JPEREZ                 11/06/2008  Creación
	*/
  public HashMap getObjectTypeUrl(String strObjectType)throws Exception, SQLException{
      HashMap hashMap = new HashMap(); 
      Connection conn = null;
      OracleCallableStatement cstmt = null;     
      String strMessage = null;
      String strName  = null;
      String strUrl   = null;
      String sqlStr = "BEGIN ORDERS.NP_ORDERS09_PKG.SP_GET_OBJECT_TYPE_URL(?,?,?,?) ; END;";            
      try{
         conn  = Proveedor.getConnection();      
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         cstmt.setString(1, strObjectType);
         cstmt.registerOutParameter(2, Types.CHAR);
         cstmt.registerOutParameter(3, Types.CHAR);
         cstmt.registerOutParameter(4, Types.CHAR);
         cstmt.executeQuery();
         strMessage   = cstmt.getString(4);
         
         if (strMessage == null){        
           strName = cstmt.getString(2);        
           strUrl = cstmt.getString(3);       
         }
         hashMap.put("strMessage",strMessage);
         hashMap.put("strName",strName);
         hashMap.put("strUrl",strUrl);      
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
			closeObjectsDatabase(conn, cstmt, null);
         /*if (cstmt != null)
            cstmt.close();        
         if (conn != null)
            conn.close();*/
      }
      return hashMap;
  }


	/**
	Method : getUserInboxByLogin
	Purpose: Obtiene los inbox del usuario
	Developer              Fecha       Comentario
	=============          ==========  ======================================================================
	Cristian Espinoza      17/06/2008  Creación
	*/

	public HashMap getUserInboxByLogin(String strLogin)  throws SQLException, Exception {
		
      Connection conn = null; 
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;
		HashMap hshDataMap = new HashMap();
      ArrayList arrUserInbox = new ArrayList();
      String strMessage = null;      
      //DbmsOutput dbmsOutput = null;		
	
      String sqlStr = "BEGIN ORDERS.NP_ORDERS07_PKG.SP_GET_USER_INBOX(?, ?, ?); END;";
		try{      
         conn = Proveedor.getConnection();      
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
               
         //dbmsOutput = new DbmsOutput(conn);
         //dbmsOutput.enable(1000000);		
         cstmt.setString(1,strLogin);
         cstmt.registerOutParameter(2, OracleTypes.CURSOR);
         cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
         cstmt.executeQuery();		
         //dbmsOutput.show();
         //dbmsOutput.close();
         strMessage  = cstmt.getString(3);
        
         if( strMessage == null ){
            rs = (ResultSet)cstmt.getObject(2);
            while (rs.next()) {
               System.out.println((String)rs.getString("npinboxname"));
               arrUserInbox.add((String)rs.getString("npinboxname"));
            }
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, rs);      
		}
		if (logger.isDebugEnabled()){
			logger.debug("strLogin: "+strLogin);
			logger.debug("strMessage: "+strMessage);
		}					
		hshDataMap.put("arrUserInbox", arrUserInbox);
		hshDataMap.put("strMessage", strMessage);
            
      return hshDataMap;
	
    }     

	/**
	Method : getNameServerReport
	Purpose: Obteniene el nombre del servidor de Oracle Report
	Developer           Fecha       Comentario
	=============       ==========  ======================================================================
	Tomas Mogrovejo     20/06/2008  Creación
	*/

  public HashMap getNameServerReport(String strCategory, String strParameterName)  throws SQLException, Exception {

      Connection conn = null; 
      OracleCallableStatement cstmt = null;      
      String strMessage = null;
      String strNamePrint=null;
      //DbmsOutput dbmsOutput = null;
      
      String sqlStr = "BEGIN WEBSALES.SPI_GET_NAME_PARAMETER(?, ?, ?, ?); END;";
		try{
         conn = Proveedor.getConnection();      
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
               
         //dbmsOutput = new DbmsOutput(conn);
         //dbmsOutput.enable(1000000);		
         cstmt.setString(1,strCategory); // CATEGORIA 
         cstmt.setString(2,strParameterName); // NOMBRE DEL PARAMETRO
         cstmt.registerOutParameter(3, OracleTypes.VARCHAR);		
         cstmt.registerOutParameter(4, OracleTypes.VARCHAR); //NOMBRE DEL SERVIDOR DE REPORTES
               
         cstmt.executeUpdate();
         //dbmsOutput.show();
         //dbmsOutput.close();
         strMessage = cstmt.getString(3);
         
         if( strMessage == null ){
           strNamePrint=cstmt.getString(4);
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, null);  
      }
      if (logger.isDebugEnabled()){
        logger.debug("strCategory: "+strCategory);
        logger.debug("strParameterName: "+strParameterName);
        logger.debug("strMessage: "+strMessage);
        logger.debug("strNamePrint: "+strNamePrint);			
      }		
      HashMap hshResultado  = new HashMap();    
      hshResultado.put("strNameServerReport",strNamePrint);
      hshResultado.put("strMessage",strMessage);
         
      return hshResultado;
	
    }     

	/**
     * Motivo: Obtiene el npvalue y el npvaluedesc según el np_table
     * <br>Realizado por: <a href="mailto:cristian.espinoza@nextel.com.pe">Cristian Espinoza</a>
     * <br>Fecha: 20/02/2008
     * 
     * @param  strTableName
     * @return HashMap
     * @throws java.sql.SQLException 
     */
    public HashMap getValueNpTable(String strTableName) throws SQLException, Exception {


		HashMap hshDataMap = new HashMap();
		ArrayList arrNpTable = new ArrayList();
		Connection conn = null;
		OracleCallableStatement cstmt = null;		
    TableBean nptableBean = null;
		ResultSet rs = null;
		//DbmsOutput dbmsOutput = null;
		String strMessage = null;
		String sqlStr = "BEGIN SWBAPPS.SPI_GET_VALUEDESC_X_NPTABLE(?,?,?); END;";
      try{
         conn = Proveedor.getConnection();
         //dbmsOutput = new DbmsOutput(conn);
         //dbmsOutput.enable(1000000);		
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);		
         cstmt.setString(1,strTableName);		
         cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(3, OracleTypes.CURSOR);		
         cstmt.executeQuery();
         //dbmsOutput.show();
         //dbmsOutput.close();
         strMessage = cstmt.getString(2);
         System.out.println("strTableName:"+strTableName);
         if (StringUtils.isBlank(strMessage)) {	
            rs = (ResultSet)cstmt.getObject(3);      
            while (rs.next()) {
               nptableBean = new TableBean();
               nptableBean.setNpValue((rs.getString("npvalue")));
               nptableBean.setNpValueDesc((rs.getString("npvaluedesc")));
               arrNpTable.add(nptableBean);		                
            }
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
      hshDataMap.put("objArrayList", arrNpTable);
      hshDataMap.put("strMessage", strMessage);		
         
      return hshDataMap;
    }    

    /**
     * Motivo: Obtiene los formatos de la categoria
     * <br>Realizado por: <a href="mailto:martin.mverae@nextel.com.pe">Martin Vera Espinoza</a>
     * <br>Fecha: 20/08/2008
     * 
     * @param  lSpecificationId
     * @return HashMap
     * @throws java.sql.SQLException 
     */
    public HashMap getFormatBySpecification(long lSpecificationId) throws SQLException, Exception {


		HashMap hshDataMap = new HashMap();
		ArrayList arrFormats = new ArrayList();
    FormatBean formatBean = null;
		Connection conn = null;
		OracleCallableStatement cstmt = null;		
		ResultSet rs = null;
		//DbmsOutput dbmsOutput = null;
		String strMessage = null;
		String sqlStr = "BEGIN ORDERS.NP_ORDERS20_PKG.SP_GET_FORMAT_LST(?,?,?); END;";
      try{
         conn = Proveedor.getConnection();
         //dbmsOutput = new DbmsOutput(conn);
         //dbmsOutput.enable(1000000);		
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);		
         cstmt.setLong(1,lSpecificationId);		
         cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(3, OracleTypes.CURSOR);		
         cstmt.executeQuery();
         //dbmsOutput.show();
         //dbmsOutput.close();
         strMessage = cstmt.getString(2);
         
         if (StringUtils.isBlank(strMessage)) {
            rs = (ResultSet)cstmt.getObject(3);
            while (rs.next()) {				
              formatBean = new FormatBean();
              formatBean.setNpformid(rs.getLong("npformid"));
              formatBean.setNpformname(rs.getString("npformname"));
              formatBean.setNpprogramname(rs.getString("npprogramname"));
              formatBean.setNptemplatename(rs.getString("nptemplatename"));
              arrFormats.add(formatBean);			                
            }
         }
         hshDataMap.put("objArrayList", arrFormats);
         hshDataMap.put("strMessage", strMessage);
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
      return hshDataMap;
    }
    
    
	/**
     * Motivo: Obtiene el nombre del vendedor
     * <br>Realizado por: <a href="mailto:jorge.perez@nextel.com.pe">Jorge Pérez</a>
     * <br>Fecha: 06/10/2008
     */
   public HashMap getSalesmanName(long lSalesmanId) throws SQLException, Exception{
     HashMap hshDataMap = new HashMap();
     Connection conn = null;
		 OracleCallableStatement cstmt = null;				 
     String strMessage = null;
     String strName    = null;
     String sqlStr = "BEGIN ORDERS.NP_ORDERS13_PKG.SP_GET_SALESMAN_NAME(?,?,?); END;";
     try{
       conn = Proveedor.getConnection();   
       cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);		
       cstmt.setLong(1,lSalesmanId);		
       cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
       cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
       cstmt.executeQuery();
       strMessage = cstmt.getString(3);
       if (strMessage==null)
         strName = cstmt.getString(2);
       hshDataMap.put("strName", strName);
       hshDataMap.put("strMessage", strMessage);  
       
     }catch(Exception e){
         throw new Exception(e);
     }finally{
         closeObjectsDatabase(conn, cstmt, null);
     }
     return hshDataMap;
   }

    public HashMap getListaEquipPendRecojo(long customerId, long siteId) throws SQLException, Exception{
  
      Connection conn = null;
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;
      HashMap hshDataMap = new HashMap();
      HashMap objInventoryPIMap=null;
      ArrayList arrEquipPendRecojoList = new ArrayList();
      String strMessage;      
      logger.debug("customerId:"+customerId);
      logger.debug("siteId:"+siteId);
      try{
        String sqlStr = "BEGIN CONTROL_EQUIPMENT.SPI_GET_EQUIPOS_PEND_RECOJO(?,?,?,?); END;";
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
        cstmt.setLong(1, customerId);
         if( siteId == 0 )
            cstmt.setNull(2,OracleTypes.NUMBER);
         else
            cstmt.setLong(2,siteId);        
        cstmt.registerOutParameter(3, OracleTypes.CURSOR);
        cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
        cstmt.executeQuery();
        strMessage = cstmt.getString(4);
        if (strMessage == null){
          rs = (ResultSet)cstmt.getObject(3);
          int i=0;
          while(rs.next()) {
            HashMap objHashMap = new HashMap();
            i=i+1;
            objHashMap.put("rownum", i+"");
            objHashMap.put("telefono", rs.getString(1)==null?"":rs.getString(1));
            objHashMap.put("imei", rs.getString(2)==null?"":rs.getString(2));
            objHashMap.put("sim",  rs.getString(3)==null?"":rs.getString(3));
            objHashMap.put("modelo", rs.getString(4)==null?"":rs.getString(4));
            objHashMap.put("equipo",rs.getString(5)==null?"":rs.getString(5));
            objHashMap.put("estado",rs.getString(6)==null?"":rs.getString(6));
            objHashMap.put("idmodelo",""+rs.getLong(7));//jsalazar 08/07/2011 Penalidad 
            arrEquipPendRecojoList.add(objHashMap);            
           }
        }
           
        hshDataMap.put("arrEquipPendRecojoList", arrEquipPendRecojoList);
        hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage); 
      }catch (Exception e) {
        logger.error(formatException(e));
         hshDataMap.put(Constante.MESSAGE_OUTPUT, e.getMessage());  
      }
      finally{
        closeObjectsDatabase(conn, cstmt, rs);
      }        
      return hshDataMap;
  }
  
  /**
     * Motivo:  Obtiene el "nptag1" teniendo como dato de entrada
     *          el campo "npvalue" y el campo "nptable" en la tabla SWBAPPS.NP_TABLE y donde se controla el NO_DATA_FOUND.
     * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
     * <br>Fecha: 09/06/2009
     */
    public HashMap getValueTag1(String strValue, String strTable) throws SQLException, Exception {		
      HashMap hshDataMap = new HashMap();
      String strDescription = null;
      Connection conn = null;
      OracleCallableStatement cstmt = null;
      String strMessage = null;
      String strTag1 = null;
      String sqlStr = "BEGIN SWBAPPS.SPI_GET_VALUE_TAG1(?,?,?,?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.setString(1, strValue);
         cstmt.setString(2, strTable);
         cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
         cstmt.executeQuery();
         strTag1= cstmt.getString(3);
         strMessage = cstmt.getString(4);
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, null);
      }
      hshDataMap.put("strTag1", strTag1);
      hshDataMap.put("strMessage",strMessage);
      return hshDataMap;
    }
    
    
      
  /**
     * Motivo:  Obtiene los persmisos para obtener servicios de arrendamiento y mensajeria
     * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
     * <br>Fecha: 01/07/2009
     * Modificado: Daniel Gutierrez Tagle  --- Se agrego el parametro strServiceMsjId para obtener el id del servicio de mensajeria -- 23/09/2010
     */
    public HashMap  getPermissionServiceDefault(long lspecificationid, String strObjectItem, String strModality, int iModelId, int iProductId,String strServiceMsjId) 
    throws SQLException, Exception {		
      HashMap hshDataMap = new HashMap();
      String strDescription = null;
      Connection conn = null;
      OracleCallableStatement cstmt = null;
      String strMessage = null;
      int ipermission_alq = 0;
      int ipermission_msj = 0;
      String sqlStr = "BEGIN ORDERS.NP_ORDER_AUTOMATIZACION_PKG.SP_VALIDATE_SERVICE_DEFAULT(?,?,?,?,?,?,?,?,?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.setLong(1, lspecificationid);
         cstmt.setString(2, strObjectItem);
         cstmt.setString(3, strModality);
         cstmt.setInt(4, iModelId);
         cstmt.setInt(5, iProductId);
         cstmt.setString(6, strServiceMsjId);
         cstmt.registerOutParameter(7, OracleTypes.NUMBER);
         cstmt.registerOutParameter(8, OracleTypes.NUMBER);
         cstmt.registerOutParameter(9, OracleTypes.VARCHAR);
         cstmt.executeQuery();
         ipermission_alq = cstmt.getInt(7);
         ipermission_msj = cstmt.getInt(8);
         strMessage = cstmt.getString(9);
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, null);
      }
      hshDataMap.put("ipermission_alq", ipermission_alq+"");
      hshDataMap.put("ipermission_msj", ipermission_msj+"");
      hshDataMap.put("strMessage",strMessage);
      return hshDataMap;
    }
    /**
     * Motivo:  Obtiene el "npstatus" teniendo como dato de entrada
     *          el campo "nptable" y el campo "npvalue" en la tabla SWBAPPS.NP_TABLE
     * <br>Realizado por: <a href="mailto:cesar.barzola@nextel.com.pe">Cesar Barzola</a>
     * <br>Fecha: 15/06/2009
     * 
     * @param      strTable                Ej: VALIDATE_SOLUTION
     * @param      strValue                Ej: 2065
     * @return     objHashMapStatus        Ej: 1
     */
     public HashMap getStatusByTable(String strTable,String strValue)throws SQLException,Exception{
       HashMap  objHashMap = new HashMap();
       Connection conn = null; 
       OracleCallableStatement cstm = null;
       String strMessage = null; 
       String strStatus=null;
       String sqlStr = "BEGIN SWBAPPS.SPI_GET_NP_TABLE_STATUS(?, ?, ?, ?); END;";
       try{   
          conn = Proveedor.getConnection();          
          cstm = (OracleCallableStatement) conn.prepareCall(sqlStr);                   
          cstm.setString(1,strTable);
          cstm.setString(2,strValue);
          cstm.registerOutParameter(3,OracleTypes.VARCHAR);
          cstm.registerOutParameter(4,OracleTypes.VARCHAR);         
          cstm.execute(); 
          strStatus     = cstm.getString(3);
          strMessage    = cstm.getString(4);
          if(strMessage==null)
          {
            if(strStatus==null){
              objHashMap.put("strStatus","0");
              }
              else {
                objHashMap.put("strStatus",strStatus);
                }
          }
          objHashMap.put("strMessage",strMessage); 
       }
       catch(Exception e){
          throw new Exception(e);
       }
       finally{
          closeObjectsDatabase(conn, cstm, null);
       }
       return objHashMap;
     }
     
  
  /*Incio Data*/
  /**
   * Motivo:  Obtiene el listado de vendedores de Data     
     * <br>Realizado por: Jorge Pérez
     * <br>Fecha: 26/08/2009
   * @throws java.lang.Exception
   * @throws java.sql.SQLException
   * @return 
   * @param iAppId
   * @param iUserId
   */
  public  HashMap getSalesDataList(long lngRule)throws SQLException,Exception{
    HashMap hshDataMap = new HashMap();    
    HashMap h =null;
    ArrayList list = new ArrayList();
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    String strMessage = null;    
    ResultSet rs = null;    
    String sqlStr = "BEGIN ORDERS.NP_ORDERS29_PKG.SP_GET_SALES_DATA_LIST(?, ?, ?, ?); END;";
    try{
       conn = Proveedor.getConnection();       
       cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
       cstmt.setLong(1, lngRule);          
       cstmt.registerOutParameter(2, OracleTypes.CURSOR);
       cstmt.registerOutParameter(3, OracleTypes.NUMBER);
       cstmt.registerOutParameter(4, Types.CHAR);
              
       cstmt.executeUpdate();       
       strMessage = cstmt.getString(4);       
       rs = (ResultSet)cstmt.getObject(2);       
       while (rs.next()) {
         h = new HashMap();
         h.put("npprovidergrpid", rs.getInt(1) + "");
         h.put("npsalesstructid", rs.getString(2));
         h.put("swname", rs.getString(3));                         
         list.add(h);
       }
    }
    catch(Exception e){
       throw new Exception(e);
    }
    finally{
      closeObjectsDatabase(conn, cstmt, rs);
    }
    
    hshDataMap.put("strMessage",strMessage);    
    hshDataMap.put("arrSalesDataList",list);
    
    return hshDataMap;
    
  }
  
  /**
    * Motivo:  Obtiene el flag que indica si se debe mostrar o no el combo Vendedor Data
   * <br>Realizado por: Jorge Pérez
   * <br>Fecha: 26/08/2009
   * @return 
   * @param strObjectId
   * @param strObjectType
   * @param lngSpecificationId
   */
  public  HashMap getSalesDataShow(long lngSpecificationId, String strObjectType, long lngObjectId)throws SQLException,Exception{
    HashMap hshDataMap = new HashMap();
    int iShowDataFields  = 0;
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    String strMessage = null;
    String sqlStr = "BEGIN ORDERS.NP_ORDERS29_PKG.SP_SALES_DATA_SHOW(?, ?, ?, ?, ?); END;";
    try{
      conn = Proveedor.getConnection();       
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, lngSpecificationId);
      cstmt.setString(2, strObjectType);
      cstmt.setLong(3, lngObjectId);      
      cstmt.registerOutParameter(4, OracleTypes.NUMBER);
      cstmt.registerOutParameter(5, Types.CHAR);
      cstmt.executeUpdate();       
      strMessage = cstmt.getString(5);      
      if (strMessage == null){
        iShowDataFields = cstmt.getInt(4);          
      }
      hshDataMap.put("strMessage",strMessage); 
      hshDataMap.put("strShowDataFields", ""+iShowDataFields); 
      
    }catch(Exception e){
       throw new Exception(e);
    }
    finally{
      closeObjectsDatabase(conn, cstmt, null);
    }
        
    return hshDataMap;
  }
  
  /**
   * Motivo:  Obtiene el flag que indica si el cliente está en exclusividad
   * <br>Realizado por: Jorge Pérez
   * <br>Fecha: 27/08/2009 
   * @throws java.lang.Exception
   * @throws java.sql.SQLException
   * @return 
   * @param lngSalesmanId
   * @param lngWinnerTypeId
   * @param lngSiteId
   * @param lngCustomerId
   */
  public HashMap validateSalesExclusivity(long lngCustomerId, long lngSiteId, long lngWinnerTypeId, long lngSalesmanId)throws SQLException,Exception {
    HashMap hshDataMap = new HashMap();
    String strExclusivity = "";
    int iProviderExclusivity  = 0;
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    String strMessage = null;
    String sqlStr = "BEGIN ORDERS.NP_ORDERS29_PKG.SP_CHANGE_DATA_SELLER(?, ?, ?, ?, ?, ?, ?); END;";
    try{
      conn = Proveedor.getConnection();       
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, lngCustomerId);
      if (lngSiteId == 0)
        cstmt.setNull(2,Types.INTEGER);
      else
        cstmt.setLong(2, lngSiteId);
      
      cstmt.setLong(3, lngWinnerTypeId);  
      
      cstmt.setLong(4, lngSalesmanId); 
                  
      cstmt.registerOutParameter(5, Types.CHAR);
      cstmt.registerOutParameter(6, Types.INTEGER);
      cstmt.registerOutParameter(7, Types.CHAR);
      cstmt.executeUpdate();       
      strMessage = cstmt.getString(7);      
      iProviderExclusivity = cstmt.getInt(6);
      strExclusivity = cstmt.getString(5);                 
      hshDataMap.put("strMessage",strMessage); 
      hshDataMap.put("strExclusivity", ""+strExclusivity); 
      hshDataMap.put("strProviderExclusivity", ""+iProviderExclusivity);
      
    }catch(Exception e){
       throw new Exception(e);
    }
    finally{
      closeObjectsDatabase(conn, cstmt, null);
    }        
    return hshDataMap;
  }
  
  /**
   * Motivo:  Obtiene el listado de vendedores de la Orden     
   * <br>Realizado por: Jorge Pérez
   * <br>Fecha: 26/08/2009
   * 
   * @throws java.lang.Exception
   * @throws java.sql.SQLException
   * @return 
   * @param lNpOrderid
   */
  public  HashMap getOrderSellersList(long lNpOrderid)throws SQLException,Exception{
    HashMap hshDataMap = new HashMap();    
    HashMap h =null;
    ArrayList list = new ArrayList();
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    String strMessage = null;    
    ResultSet rs = null;    
    String sqlStr = "BEGIN ORDERS.NP_ORDERS29_PKG.SP_GET_ORDER_SELLERS(?, ?, ?); END;";
    System.out.println("lNpOrderid: "+lNpOrderid);
    try{
       conn = Proveedor.getConnection();       
       cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
       cstmt.setLong(1, lNpOrderid);          
       cstmt.registerOutParameter(2, OracleTypes.CURSOR);       
       cstmt.registerOutParameter(3, Types.CHAR);
              
       cstmt.executeUpdate();       
       strMessage = cstmt.getString(3);       
       rs = (ResultSet)cstmt.getObject(2);       
       while (rs.next()) {
         h = new HashMap();
         h.put("npprovidergrpid", rs.getInt(1) + "");
         h.put("npsalesstructid", rs.getString(2));
         h.put("npwinnertypeid", rs.getInt(3) + "");         
         h.put("swname", rs.getString(4));                         
         list.add(h);
       }
    }
    catch(Exception e){
       throw new Exception(e);
    }
    finally{
      closeObjectsDatabase(conn, cstmt, rs);
    }
    
    hshDataMap.put("strMessage",strMessage);    
    hshDataMap.put("arrOrdersSellersList",list);
    
    return hshDataMap;
    
  }
  
   /**
   * Motivo:  Obtiene el vendedor de la orden de un tipo determinado
   * <br>Realizado por: Jorge Pérez
   * <br>Fecha: 26/08/2009
   * 
   * @throws java.lang.Exception
   * @throws java.sql.SQLException
   * @return 
   * @param lngType
   * @param lNpOrderid
   */
  public  HashMap getOrderSellerByType(long lNpOrderid, long lngType)throws SQLException,Exception{
    HashMap hshDataMap = new HashMap();
    int iSellerId = 0;
    String strSellerName = "";
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    String strMessage = null;
    String sqlStr = "BEGIN ORDERS.NP_ORDERS29_PKG.SP_GET_ORDER_SELLER(?, ?, ?, ?, ?); END;";
    try{
      conn = Proveedor.getConnection();       
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, lNpOrderid);
      cstmt.setLong(2, lngType);        
      cstmt.registerOutParameter(3, OracleTypes.NUMBER);      
      cstmt.registerOutParameter(4, Types.CHAR);
      cstmt.registerOutParameter(5, Types.CHAR);
      cstmt.executeUpdate();       
      strMessage = cstmt.getString(5);      
      if (strMessage == null){
        iSellerId = cstmt.getInt(3);          
        strSellerName = cstmt.getString(4);
      }
      hshDataMap.put("strMessage",strMessage); 
      hshDataMap.put("strSellerId", ""+iSellerId); 
      hshDataMap.put("strSellerName", strSellerName); 
      
    }catch(Exception e){
       throw new Exception(e);
    }
    finally{
      closeObjectsDatabase(conn, cstmt, null);
    }
        
    return hshDataMap;
  }
  
  /**
   * Motivo:  Valida que la Unidad Jerarquíca de un usuario tenga una regla
   * <br>Realizado por: Jorge Pérez
   * <br>Fecha: 01/09/2009
   * @throws java.lang.Exception
   * @throws java.sql.SQLException
   * @return 
   * @param lngUserId
   * @param lngSalesRuleId
   */
  public  HashMap validateStructRule(long lngSalesRuleId, long lngSalesStrucId)throws SQLException,Exception{
    HashMap hshDataMap = new HashMap();   
    String strResultValidate = "0";
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    String strMessage = null;
    String sqlStr = "BEGIN ORDERS.NP_ORDERS29_PKG.SP_CHECK_STRUCT_RULE(?, ?, ?, ?); END;";
    try{
      conn = Proveedor.getConnection();       
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, lngSalesRuleId);
      cstmt.setLong(2, lngSalesStrucId);                
      cstmt.registerOutParameter(3, Types.CHAR);
      cstmt.registerOutParameter(4, Types.CHAR);
      cstmt.executeUpdate();       
      strMessage = cstmt.getString(4);      
      if (strMessage == null){                 
        strResultValidate = cstmt.getString(3);
      }
      hshDataMap.put("strMessage",strMessage);       
      hshDataMap.put("strResultValidate", strResultValidate); 
      
    }catch(Exception e){
       throw new Exception(e);
    }
    finally{
      closeObjectsDatabase(conn, cstmt, null);
    }
        
    return hshDataMap;
  }


/**
     * Motivo:  Obtiene el stock por almacen con respecto al modelo solicitado
     * <br>Realizado por: <a href="mailto:mauricio.wong@nextel.com.pe">Mauricio Wong</a>
     * <br>Fecha: 28/08/2009
     * 
     * @param      strModel                Ej: 1116
     */
     public HashMap getStockPorModelo(String strModel) throws SQLException, Exception{
         Connection conn = null;
         OracleCallableStatement cstmt = null;
         //OraclePreparedStatement pstmt = null; 
         ResultSet res = null;
         HashMap hshDataMap = new HashMap();
         ArrayList arrStockModel = new ArrayList();
         String strType = "EQUIPOS";
         String strMessage = null;
         int longitud;
         String sqlStr = "BEGIN INVENTORY.SPI_GET_MODEL_STOCK_JAVA(?,?,?,?); END;";

        try{
            //Definimos los parametros de la consulta
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, strType);
            cstmt.setString(2, strModel);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(4, OracleTypes.ARRAY, "INVENTORY.TT_RECORD_STOCK_FF_LST");
            
            //Ejecutamos el Query
            cstmt.execute(); 
  
            strMessage = cstmt.getString(3);
            ARRAY arList = (ARRAY)cstmt.getObject(4);
            
            if (StringUtils.isBlank(strMessage)){
                
                if (arList!= null && arList.getOracleArray().length>0 )
                {
                  longitud = arList.getOracleArray().length;
                  for (int i = 0; i < longitud; i++) {
                      
                      STRUCT stc = (STRUCT) arList.getOracleArray()[i];
                      HashMap objHashMap = new HashMap();
                          
                      //Asignacion de valores
                      objHashMap.put("flex_value", MiUtil.defaultString(stc.getAttributes()[0], ""));
                      objHashMap.put("name", MiUtil.defaultString(stc.getAttributes()[1], ""));
                      objHashMap.put("codigo",MiUtil.defaultString(stc.getAttributes()[2], ""));
                      objHashMap.put("stock", MiUtil.defaultString(stc.getAttributes()[3], null));
                      
                      arrStockModel.add(objHashMap);  
                  }
                }
            }    
            hshDataMap.put("arrStockModel", arrStockModel);
            hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
            
        }
        catch(Exception e){
            e.printStackTrace();
            //e.getNextException().printStackTrace();
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn, cstmt, null);
        }
        return hshDataMap;
     }
	 
   /**
   * Motivo:  Valida que la orden que se genere desde un incidente Callcenter Mayores/checkdata incidente/serv adic.
   * <br>Realizado por: Fanny Najarro
   * <br>Fecha: 24/09/2009
   * @throws java.lang.Exception
   * @throws java.sql.SQLException
   * @return 
   * @param lngGeneratorId
   * @param strGeneratorType
   * @param lngSpecification
   */
  public  HashMap validateIncident(long lngGeneratorId, String strGeneratorType, long lngSpecification, long lngUserId, 
                            int iAppId, long lngCustomerId, long lngSiteId, long lngWinnerTypeId)
    throws SQLException,Exception{
    
    HashMap hshDataMap = new HashMap();   
    int iResult = 0;
    long lngProvider = 0;
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    String strMessage = null;
    
    logger.debug("lngGeneratorId ==>"+lngGeneratorId);
    logger.debug("strGeneratorType ==>"+strGeneratorType);
    logger.debug("lngSpecification ==>"+lngSpecification);
    logger.debug("lngUserId ==>"+lngUserId);
    logger.debug("iAppId ==>"+iAppId);
    logger.debug("lngCustomerId ==>"+lngCustomerId);
    logger.debug("lngSiteId ==>"+lngSiteId);
    logger.debug("lngWinnerTypeId ==>"+lngWinnerTypeId);
    
    
    
    String sqlStr = "BEGIN ORDERS.NP_ORDERS29_PKG.SP_VALIDATE_INCIDENT(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); END;";
    
    
    try{
      conn = Proveedor.getConnection();       
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, lngGeneratorId);
      cstmt.setString(2, strGeneratorType);
      cstmt.setLong(3, lngSpecification); 
      cstmt.setLong(4, lngCustomerId);
      cstmt.setLong(5, lngSiteId);  
      cstmt.setLong(6, lngWinnerTypeId);
      cstmt.setLong(7, lngUserId); 
      cstmt.setInt(8, iAppId); 
        
      
      cstmt.registerOutParameter(9, OracleTypes.NUMBER);
      cstmt.registerOutParameter(10, OracleTypes.NUMBER);
      cstmt.registerOutParameter(11, OracleTypes.VARCHAR);
      
      cstmt.executeUpdate();   
      
      strMessage = cstmt.getString(11);  
      
      
      if (strMessage == null){  
        iResult = cstmt.getInt(9);
        lngProvider = cstmt.getLong(10);
        
      }
      
      logger.info("[Output][strMessage]: "+strMessage);
      logger.info("[Output][iResult]: "+iResult+"");
      logger.info("[Output][lngProvider]: "+lngProvider+"");
      
      hshDataMap.put("strMessage",strMessage);       
      hshDataMap.put("strResult", iResult+""); 
      hshDataMap.put("strProvider", lngProvider+""); 
      
    }catch(Exception e){
       throw new Exception(e);
    }
    finally{
      closeObjectsDatabase(conn, cstmt, null);
    }
        
    return hshDataMap;
  }
 public  HashMap validateUserRol(long lUserId,int intLevel,String strcode) throws SQLException,Exception
 {
      HashMap objHashMap = new HashMap();
      Connection conn = null;
      OracleCallableStatement cstmt = null;
      String strMsgError = null;
     try{
         conn = Proveedor.getConnection();
         String sqlStr =  "BEGIN WEBSALES.SPI_VALIDATE_ROL(?, ?, ? , ?, ?); END;";
         cstmt=(OracleCallableStatement)conn.prepareCall(sqlStr);
         if(lUserId==0) //an_userid
            cstmt.setNull(1, OracleTypes.NUMBER);
         else
            cstmt.setLong(1,lUserId);
          if(intLevel==0)
            cstmt.setNull(2, OracleTypes.NUMBER);
          else
            cstmt.setInt(2,intLevel);
            
          cstmt.setString(3,strcode);
          cstmt.registerOutParameter(4, OracleTypes.NUMBER);
          cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
          cstmt.execute();
          strMsgError=cstmt.getString(5);
          if(strMsgError==null){
            String respuesta=Long.toString(cstmt.getInt(4));
            objHashMap.put("respuesta",respuesta);
          }
          objHashMap.put(Constante.MESSAGE_OUTPUT,strMsgError);
     }
     catch(Exception e){
      logger.error(formatException(e));
     }
     finally{
        try{
          closeObjectsDatabase(conn,cstmt,null);
        }
        catch(Exception e){
          logger.error(formatException(e)); 
        }
     }
   return objHashMap;
 }

 public  HashMap getConsultor(long lUserId) throws SQLException,Exception
 {
      HashMap objHashMap = new HashMap();
      Connection conn = null;
      OracleCallableStatement cstmt = null;
      String strMsgError = null;
     try{
         conn = Proveedor.getConnection();
         String sqlStr =  "BEGIN WEBSALES.SPI_GET_CONSULTOR(?, ?, ?); END;";
         cstmt=(OracleCallableStatement)conn.prepareCall(sqlStr);
         if(lUserId==0) //an_userid
            cstmt.setNull(1, OracleTypes.NUMBER);
         else
            cstmt.setLong(1,lUserId);
          cstmt.registerOutParameter(2, OracleTypes.NUMBER);
          cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
          cstmt.execute();
          strMsgError=cstmt.getString(3);
          if(strMsgError==null){
            String respuesta=Long.toString(cstmt.getInt(2));
            objHashMap.put("respuesta",respuesta);
          }
          objHashMap.put(Constante.MESSAGE_OUTPUT,strMsgError);
     }
     catch(Exception e){
      logger.error(formatException(e));
     }
     finally{
        try{
          closeObjectsDatabase(conn,cstmt,null);
        }
        catch(Exception e){
          logger.error(formatException(e)); 
        }
     }
   return objHashMap;
 }
	 
  public  HashMap getAmbitSalesList(String codType, int regionId)throws SQLException,Exception{
    HashMap hshDataMap = new HashMap();    
    HashMap h =null;
    ArrayList list = new ArrayList();
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    String strMessage = null;    
    ResultSet rs = null;    
    String sqlStr = "BEGIN WEBSALES.spi_get_ambit_sales_list(?, ?, ?, ?); END;";
    try{
       conn = Proveedor.getConnection();       
       cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
       cstmt.setString(1, codType);
       cstmt.setInt(2,regionId);
       cstmt.registerOutParameter(3, OracleTypes.CURSOR);       
       cstmt.registerOutParameter(4, Types.CHAR);
              
       cstmt.executeUpdate();       
       strMessage = cstmt.getString(4);       
       rs = (ResultSet)cstmt.getObject(3);  
       if("ORA-0000: normal, successful completion".equals(strMessage)){
          strMessage = null;
       }
       if(strMessage==null){
         while (rs.next()) {
           h = new HashMap();
           h.put("swregionid", rs.getInt(1) + "");
           h.put("swname", rs.getString(2));
           list.add(h);
        }
       }
    }
    catch(Exception e){
       throw new Exception(e);
    }
    finally{
      closeObjectsDatabase(conn, cstmt, rs);
    }
    
    hshDataMap.put("strMessage",strMessage);    
    hshDataMap.put("arrAmbitSalesList",list);
    
    return hshDataMap;
    
  }

  
  /*Fin Data*/

  /**
    * Motivo:  Obtiene el flag que indica si se debe mostrar o no el combo Vendedor Data
   * <br>Realizado por: Patricia Castillo
   * <br>Fecha: 26/08/2009
   * @return 
   * @param strObjectId
   * @param strObjectType
   * @param lngSpecificationId
   * 
   *       an_specificationid      IN    NUMBER,
      av_objectType           IN    VARCHAR2,
      an_objectId             IN    NUMBER,
      an_npcustomerid         IN    NUMBER,
      an_npsiteid             IN    NUMBER,
      an_showdatacmb          OUT   NUMBER,
      an_loadsellerdata       OUT   NUMBER,
      an_datasalesprovid      OUT   NUMBER,
      av_message              OUT   VARCHAR2

   */
  public  HashMap getSalesDataShow(long lngSpecificationId, String strObjectType, long lngObjectId, long lngCustomerId, long lngSiteId)throws SQLException,Exception{
    HashMap hshDataMap = new HashMap();
    int iShowDataFields  = 0;
    int iLoadSellerData = 0;
    int iDataSalesProvId = 0;
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    String strMessage = null;
    String strFlagExclusivity = "";
    String strLoadDealer = "";
    String sqlStr = "BEGIN ORDERS.NP_ORDERS29_PKG.SP_SALES_DATA_SHOW(?, ?, ?, ?, ?, ?, ?, ?, ?,?,?); END;";
    try{
      conn = Proveedor.getConnection();       
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, lngSpecificationId);
      cstmt.setString(2, strObjectType);
      cstmt.setLong(3, lngObjectId);      
      cstmt.setLong(4, lngCustomerId);      
      cstmt.setLong(5, lngSiteId);      
      cstmt.registerOutParameter(6, OracleTypes.NUMBER);      
      cstmt.registerOutParameter(7, OracleTypes.NUMBER);      
      cstmt.registerOutParameter(8, OracleTypes.NUMBER);
      cstmt.registerOutParameter(9,OracleTypes.VARCHAR);
      cstmt.registerOutParameter(10, Types.CHAR);
      cstmt.registerOutParameter(11, Types.VARCHAR);
      cstmt.executeUpdate();       
      strMessage = cstmt.getString(11);      
      if (strMessage == null){
        iShowDataFields = cstmt.getInt(6);   
        iLoadSellerData = cstmt.getInt(7);
        iDataSalesProvId = cstmt.getInt(8);
        strLoadDealer = cstmt.getString(9); 
        strFlagExclusivity = cstmt.getString(10); 
      }
      hshDataMap.put("strMessage",strMessage); 
      hshDataMap.put("strShowDataFields", ""+iShowDataFields); 
      hshDataMap.put("strLoadSellerData", ""+iLoadSellerData); 
      hshDataMap.put("strDataSalesProvId", ""+iDataSalesProvId); 
      hshDataMap.put("strFlagExclusivity", ""+strFlagExclusivity);
      hshDataMap.put("strLoadDealer", ""+strLoadDealer);
      
    }catch(Exception e){
       throw new Exception(e);
    }
    finally{
      closeObjectsDatabase(conn, cstmt, null);
    }
        
    return hshDataMap;
  }
  
  
  
  /**
     * Motivo: Obtiene el npvalue y el npvaluedesc según el np_configuration
     * <br>Realizado por: <a href="mailto:tomas.mogrovejo@hp.com">Tomás Mogrovejo</a>
     * <br>Fecha: 19/08/2010
     * 
     * @param  strTableName
     * @return HashMap
     * @throws java.sql.SQLException 
     */
            
  public HashMap getValueNpConfig(String strConfiguration, String strValue) throws SQLException, Exception {

    HashMap hshDataMap = new HashMap();
		ArrayList arrNpTable = new ArrayList();
		Connection conn = null;
		OracleCallableStatement cstmt = null;		
    TableBean nptableBean = null;
		ResultSet rs = null;
		String strMessage = null;
		String sqlStr = "BEGIN ORDERS.NP_ORDERS02_PKG.SP_GET_DESC_X_NPCONF(?,?,?,?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);		
         cstmt.setString(1,strConfiguration);		
         cstmt.setString(2,strValue);		         
         cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(4, OracleTypes.CURSOR);		
         cstmt.executeQuery();
         strMessage = cstmt.getString(3);
         System.out.println("strConfiguration:"+strConfiguration);
         if (StringUtils.isBlank(strMessage)) {	
            rs = (ResultSet)cstmt.getObject(4);      
            while (rs.next()) {
               nptableBean = new TableBean();
               nptableBean.setNpValue((rs.getString("npvalue")));
               nptableBean.setNpValueDesc((rs.getString("npvaluedesc")));
               arrNpTable.add(nptableBean);		                
            }
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
      hshDataMap.put("objArrayList", arrNpTable);
      hshDataMap.put("strMessage", strMessage);		
         
    return hshDataMap;
  }    

  
  /*******************************************************************************************************************************
     * Motivo: Se obtiene la estructura de ventas por defecto
     * <br>Realizado por: <a href="mailto:karen.salvador@hp.com">Karen Salvador</a>
     * <br>Fecha: 09/11/2010
   *******************************************************************************************************************************/
    public HashMap  getSalesStructOrigen(long lSalesStructId, String strCanal) throws SQLException, Exception {		
      HashMap hshDataMap = new HashMap();
      String strDescription = null;
      Connection conn = null;
      OracleCallableStatement cstmt = null;
      String strMessage = null;
      int iSalesStructOrigen = 0;
      String sqlStr = "BEGIN PRODUCT.SPI_GET_SALESSTRUCT_DEFAULT(?,?,?,?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         if(lSalesStructId==0)
            cstmt.setNull(1, OracleTypes.NUMBER);
         else
         cstmt.setLong(1, lSalesStructId);
         cstmt.setString(2, strCanal);
         cstmt.registerOutParameter(3, OracleTypes.NUMBER);
         cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
         cstmt.executeQuery();
         iSalesStructOrigen = cstmt.getInt(3);
         strMessage = cstmt.getString(4);
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, null);
      }
      hshDataMap.put("iSalesStructOrigen", iSalesStructOrigen+"");
      hshDataMap.put("strMessage",strMessage);
      return hshDataMap;
    }
    
    
   /*******************************************************************************************************************************
     * Motivo: Se obtiene la estructura de ventas en base a la estructura de retail
     * <br>Realizado por: <a href="mailto:karen.salvador@hp.com">Karen Salvador</a>
     * <br>Fecha: 09/11/2010
   *******************************************************************************************************************************/
    public HashMap  getSalesStructOrigenxRetail(long lRetailId) throws SQLException, Exception {		
      HashMap hshDataMap = new HashMap();
      String strDescription = null;
      Connection conn = null;
      OracleCallableStatement cstmt = null;
      String strMessage = null;
      int iSalesStructOrigen = 0;
      String sqlStr = "BEGIN ORDERS.SPI_GET_SALESSTRUCTID_RETAILID(?,?,?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.setLong(1, lRetailId);
         cstmt.registerOutParameter(2, OracleTypes.NUMBER);
         cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
         cstmt.executeQuery();
         iSalesStructOrigen = cstmt.getInt(2);
         strMessage = cstmt.getString(3);
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, null);
      }
      hshDataMap.put("iSalesStructOrigen", iSalesStructOrigen+"");
      hshDataMap.put("strMessage",strMessage);
      return hshDataMap;
    }

  	//INICIO - HP Argentina - TIPOS DISPOSITIVO -  PROYECTO HP-PTT
   /**
   * Motivo: getTipoEquipoList
   * <br>Realizado por: <a href="mailto:marcelo.cejas-echeverria@hp.com">Marcelo Cejas Echeverria</a>
   * <br>Fecha: 30/09/2010 
   */  
  public HashMap getTipoEquipoList() throws SQLException, Exception {
		if(logger.isDebugEnabled())
			logger.debug("--Inicio getTipoEquipoList() --");
		HashMap hshDataMap = new HashMap();
		ArrayList arrTipoDispList = new ArrayList();
		Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
		String strMessage = null;
		String sqlStr = "BEGIN SWBAPPS.SPI_GET_NPTABLE_LST2 (?,?,?,?,?,?,?,?,?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.setString(1, "DISPOSITIVE_TYPE_PL");         
         cstmt.setNull(2,OracleTypes.VARCHAR);
         cstmt.setNull(3,OracleTypes.VARCHAR);
         cstmt.setNull(4,OracleTypes.VARCHAR);
         cstmt.setNull(5,OracleTypes.VARCHAR);
         cstmt.setNull(6,OracleTypes.VARCHAR);
         cstmt.setNull(7,OracleTypes.NUMBER);
         cstmt.registerOutParameter(8, OracleTypes.CURSOR);
         cstmt.registerOutParameter(9, OracleTypes.VARCHAR);
         cstmt.executeQuery();
         rs = (ResultSet)cstmt.getObject(8);
         strMessage = cstmt.getString(9);
         if (StringUtils.isBlank(strMessage)) {
            while (rs.next()) {
               DominioBean dominio = new DominioBean();
               int i = 1;
               dominio.setValor(StringUtils.defaultString(rs.getString(2)));
               try {
                  dominio.setDescripcion(StringUtils.defaultString(rs.getString(3)));
               }
               catch(SQLException sqle) {
                  dominio.setDescripcion("");
               }
               arrTipoDispList.add(dominio);
            }
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
		   closeObjectsDatabase(conn, cstmt, rs);
      }
		hshDataMap.put("arrTipoDispList", arrTipoDispList);
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;
    }
    
    
   /**
   * Motivo: getModelsByTypeOfEquipment
   * <br>Realizado por: <a href="mailto:marcelo.cejas-echeverria@hp.com">Marcelo Cejas Echeverria</a>
   * <br>Fecha: 30/09/2010 
   */  
     public HashMap getModelsByTypeOfEquipment(int deviceTypeId) throws SQLException, Exception {
		if(logger.isDebugEnabled())
			logger.debug("--Inicio getModelsByTypeOfEquipment --");
		HashMap hshDataMap = new HashMap();
		ArrayList arrComboModelsByTypeOfEquipmentList = new ArrayList();
		Connection conn = null;
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
		String strMessage = null;
		String sqlStr = "BEGIN ORDERS.NP_ORDERS03_PKG.SP_GET_MODELS_BY_DEVICETYPE(?,?,?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.setInt(1, deviceTypeId);
         cstmt.registerOutParameter(2, OracleTypes.CURSOR);
         cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
         cstmt.executeQuery();
         rs = (ResultSet)cstmt.getObject(2);
         strMessage = cstmt.getString(3);
         if (StringUtils.isBlank(strMessage)) {
            while (rs.next()) {
               DominioBean dominio = new DominioBean();
               int i = 1;
               dominio.setValor(StringUtils.defaultString(rs.getString(1)));
               try {
                  dominio.setDescripcion(StringUtils.defaultString(rs.getString(1)));
               }
               catch(SQLException sqle) {
                  dominio.setDescripcion(StringUtils.defaultString(rs.getString(1)));
               }
               arrComboModelsByTypeOfEquipmentList.add(dominio);
            }
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
		   closeObjectsDatabase(conn, cstmt, rs);
      }
		hshDataMap.put("arrComboModelsByTypeOfEquipmentList", arrComboModelsByTypeOfEquipmentList);
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
    return hshDataMap;
    }
   //FIN - HP Argentina - TIPOS DISPOSITIVO -  PROYECTO HP-PTT

   
     /**
   * Motivo: Obtiene el id canal de atención
   * <br>Realizado por: <a href="mailto:mario.mendoza-ludena@hp.com">Antonio Mendoza</a>
   * <br>Fecha: 05/10/2010
   * @param     intSalesstructid
   */
  public int get_AttChannel_Struct(int intSalesstructid) 
   throws SQLException,Exception
   {
      int iReturnValue = 0;
      Connection conn=null;
      OracleCallableStatement cstmt = null;
            
      String sqlStr = " { ? = call PRODUCT.FXI_GET_ATTCHANNEL_STRUCT( ? ) } ";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         
         cstmt.registerOutParameter(1, Types.NUMERIC);
         cstmt.setInt(2, intSalesstructid);
         
         cstmt.execute();
         
         iReturnValue = cstmt.getInt(1);
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
        closeObjectsDatabase(conn, cstmt, null);        
      }
      return iReturnValue;
   }
   
   /**
   * Motivo: Inserta en la tabla NP_LOG_ITEM
   * <br>Realizado por: <a href="frank.picoy@hp.com">Frank Picoy</a>
   * <br>Fecha: 09/01/2012
   * @param     objLogItem
   */
   public String doSaveLogItem(HashMap objLogItem) throws SQLException,Exception {
      String strResult = Constante.OPERATION_STATUS_ERROR;
      Connection conn = null;
      OracleCallableStatement cstmt = null;
      String strMsgError = null;
     try{
         conn = Proveedor.getConnection();
         String sqlStr =  "BEGIN ORDERS.SPI_INS_NP_LOG_ITEM(?, ?, ? , ?, ?); END;";
         cstmt=(OracleCallableStatement)conn.prepareCall(sqlStr);

         cstmt.setLong(1,MiUtil.parseLong(objLogItem.get("nporderid").toString()));
         cstmt.setString(2,MiUtil.getString(objLogItem.get("npinbox").toString()));
         cstmt.setString(3,MiUtil.getString(objLogItem.get("npdescription").toString()));
         cstmt.setString(4,MiUtil.getString(objLogItem.get("npcreatedby").toString()));
         cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
         cstmt.execute();
         strMsgError=cstmt.getString(5);
         if(strMsgError==null){
            strResult=Constante.OPERATION_STATUS_OK;
         } 
     }   catch(Exception e){
         logger.error(formatException(e));
     }   finally{
         try{
           closeObjectsDatabase(conn,cstmt,null);
         } catch(Exception e){
           logger.error(formatException(e)); 
        }
     }
     return strResult;
  }
   
   
    //JRAMIREZ 21/07/2014 Tienda Express
    public int getOrderExist(int nporderid) throws SQLException, Exception{
     Connection conn = null;
     
     String sqlStr =  " { ? = call ORDERS.NP_TE_ORDER01_PKG.FX_ORDER_EXIST( ? ) } ";
     
     OracleCallableStatement  cstmt = null;
     int intExistOrder =0;
     try{
       conn = Proveedor.getConnection();
       cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
       
       cstmt.registerOutParameter(1, Types.NUMERIC);
       cstmt.setInt(2, nporderid);
       
       cstmt.execute();
       intExistOrder = cstmt.getInt(1);
     }catch (Exception e) {
      logger.error(formatException(e));
      e.printStackTrace();
     }
     finally{
       closeObjectsDatabase(conn, cstmt, null);  
     }                                  
     return intExistOrder;
    }
    
    //JRAMIREZ 21/07/2014 Tienda Express
    public int getBuildingidByOrderid(int nporderid) throws SQLException, Exception{
     Connection conn = null;
     
     String sqlStr =  " { ? = call ORDERS.NP_TE_ORDER01_PKG.FX_GET_BUILDINGID( ? ) } ";
     
     OracleCallableStatement  cstmt = null;
     int intBuildingid =0;
     try{
       conn = Proveedor.getConnection();
       cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
       
       cstmt.registerOutParameter(1, Types.NUMERIC);
       cstmt.setInt(2, nporderid);
       
       cstmt.execute();
       intBuildingid = cstmt.getInt(1);
     }catch (Exception e) {
      logger.error(formatException(e));
      e.printStackTrace();
     }
     finally{
       closeObjectsDatabase(conn, cstmt, null);  
     }                                  
     return intBuildingid;
    }

    /**
     * Motivo: Devuelve parametro para validar Tipo de Operación
     * <br>Realizado por: <a href="mailto:orlando.cruces@hp.com">Orlando Cruces</a>
     * <br>Fecha: 23/03/2015
     * @param     String npconfigurationtype
     * @return	  int
     */
    public String getValidateTypOpe(String npconfigurationtype) throws Exception,SQLException{
        System.out.println("[GeneralDAO][getValidateTypOpe][npconfigurationtype]"+npconfigurationtype);
        Connection conn = null;
        OracleCallableStatement cstm = null;
        String strValidTypOpe = null;
        String sqlStr = "{ ? = call CREDIT.FXI_GET_VALIDATE(?)}";
        try{
            conn = Proveedor.getConnection();
            cstm = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstm.registerOutParameter(1, Types.VARCHAR);
            cstm.setString(2,npconfigurationtype);
            cstm.execute();
            strValidTypOpe = cstm.getString(1);
            System.out.println("[getValidateTypOpe][strValidTypOpe] "+strValidTypOpe);
        }
        catch(Exception e){
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn, cstm, null);
        }
        return strValidTypOpe;
    }

    /**
     * Motivo: Validacion de Tipo de Operación
     * <br>Realizado por: <a href="mailto:orlando.cruces@hp.com">Orlando Cruces</a>
     * <br>Fecha: 23/03/2015
     * @param     Int an_npcustomerid,an_npspecificationid
     * @return	  String
     */
    public HashMap getTypeOpeSpecifications(String strCustomerId) throws SQLException, Exception {
        HashMap hshDataMap = new HashMap();
        ArrayList arrTypeOpeSpecifications = new ArrayList();
        Connection conn = null;
        ResultSet rs = null;
        OracleCallableStatement cstmt = null;
        String strMessage = null;
        String sqlStr = "BEGIN CREDIT.SPI_GET_VALID_TYPE_OPE(?,?,?); END;";
        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, strCustomerId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            rs = (ResultSet)cstmt.getObject(2);
            strMessage = cstmt.getString(3);
            if(StringUtils.isBlank(strMessage)) {
                while (rs.next()) {
                    DominioBean dominio = new DominioBean();
                    int i = 1;
                    dominio.setValor(StringUtils.defaultString(rs.getString(i++)));
                    //System.out.println("[GeneralDAO][getTypeOpeSpecifications] carga ["+(i++)+"]");
                    arrTypeOpeSpecifications.add(dominio);
                }
            }
        }
        catch(Exception e){
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn, cstmt, rs);
        }
        hshDataMap.put("arrTypeOpeSpecifications", arrTypeOpeSpecifications);
        hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;
    }

    /**
     * Motivo:  Obtiene la cantidad maxima de modelos a insertar por subcategoria, ademas del flag que indica si
     *          se va a mostrar un mensaje de advertencia al usuario
     * <br>Realizado por: <a href="mailto:eduardo.pena@teamsoft.com.pe">Eduardo Peña Vilca EPV</a>
     * <br>Fecha: 18/06/2015
     *
     * @param      strTable            Ej: ORDER_MODEL_LIMIT (Préstamo)
     * @param      strSubCategoria     Ej: 208 (Portabilidad Prepago)
     *
     * @return     strValue            Ej: 11 (Maximo de Modelos que se deben registrar)
     * @return     strFlag             Ej:
     */
    public HashMap getValueLimitModel(String strTable,String strSubCategoria) throws SQLException, Exception {


        HashMap hshDataMap = new HashMap();
        String strValue = null;
        String strFlag = null;
        String strMessage = null;
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String sqlStr = "BEGIN SWBAPPS.NP_TABLE01_PKG.SP_GET_VALUE_LIMIT_MODEL(?,?,?,?,?); END;";
        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, strTable);
            cstmt.setString(2, strSubCategoria);
            cstmt.registerOutParameter(3, Types.CHAR);
            cstmt.registerOutParameter(4, Types.CHAR);
            cstmt.registerOutParameter(5, Types.CHAR);
            cstmt.executeQuery();
            strValue = cstmt.getString(3);
            strFlag = cstmt.getString(4);
            strMessage = cstmt.getString(5);
        }
        catch(Exception e){
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn, cstmt, null);
        }
        hshDataMap.put("strValue", strValue);
        hshDataMap.put("strFlag", strFlag);
        hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;
    }

    //EFLORES Requerimiento PM0010274
    /**
     * Proposito: Poder verificar si el usuario cumple con las condiciones para ver un customer:
     * 1) usuario sea consultor, supervisor o coordinador indirecto
     * 2) Si lo es entonces el campo GANADOPOR asociado al costumer debe ser el nplogin o (TIENDA, CALLCENTER, RETAIL)
     * <br>Realizado por: <a href="mailto:lhuapaya@practicaconsulting.com">Luis Huapaya</a>
     * <br>Fecha: 16/07/2015
     * @param     String userId
     * @param     String customerId
     * @return    Boolean
     */
    public String verifyUserCanSeeCustomer(String userId,String objectType,String objectId, String typeMessage) throws Exception {
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String val= "";
        String sqlStr = "{ ? = call SWBAPPS.FXI_USER_CAN_SEE_CUSTOMER(?,?,?,?)}";
        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.registerOutParameter(1, OracleTypes.VARCHAR);
            cstmt.setString(2, userId);
            cstmt.setString(3, objectType);
            cstmt.setString(4,objectId);
            cstmt.setString(5,typeMessage);
            cstmt.execute();
            val = cstmt.getString(1);

        }catch(Exception e){
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn, cstmt, null);
        }
        return val;
    }
    //FIN EFLORES




    /**
     * Motivo: Devuelve los numeros que se encuentran en la blacklist
     * <br>Realizado por: <a href="mailto:lhuapaya@practicaconsulting.com">Luis Huapaya</a>
     * <br>Fecha: 16/07/2015
     * @param     int strnpSite
     * @return    String
     */
    public HashMap getPhoneBlacklist(int strnpSite,int strCustomerId,int type) throws SQLException, Exception {
        // System.out.println("[GeneralDAO.getOfferType] - strCustomerId: "+strCustomerId+", strSpecificationId: "+strSpecificationId+", strOrderId: "+strOrderId);
        HashMap hshDataMap = new HashMap();
        int count;
        String message;
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String strMessage;
        String result   = null;
        String sqlStr = "BEGIN ORDERS.SPI_GET_COUNT_PHONE(?,?,?,?,?); END;";
        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setInt(1, strnpSite);
            cstmt.setInt(2,strCustomerId);
            cstmt.setInt(3,type);
            cstmt.registerOutParameter(4,OracleTypes.NUMBER);
            cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            count = cstmt.getInt(4);
            message = cstmt.getString(5);
            hshDataMap.put("count", count);
            hshDataMap.put("massage", message);
        }
        catch(Exception e){
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn, cstmt, null);
        }
        return hshDataMap;
    }

    /**
     * Motivo: Valida la cantidad de productos de tipo bolsa de celulares
     * <br>Realizado por: <a href="mailto:lhuapaya@practicaconsulting.com">Luis Huapaya</a>
     * <br>Fecha: 16/07/2015
     * @param     int strnpSite
     * @return    String
     */
    public HashMap getValidateCountUnitsBolCel(int specification,int productOrigen, int productDestino) throws SQLException, Exception {
        HashMap hshDataMap = new HashMap();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String strMessage;
        String sqlStr = "BEGIN CELL_BAG.SPI_GET_VALIDATE_BAG_CHANGE(?,?,?,?,?); END;";
        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1,specification);
            cstmt.setLong(2,productOrigen);
            cstmt.setLong(3,productDestino);
            cstmt.registerOutParameter(4,OracleTypes.NUMBER);
            cstmt.registerOutParameter(5,OracleTypes.VARCHAR);
            cstmt.executeQuery();
            strMessage = cstmt.getString(5);
            hshDataMap.put("srtMessage",strMessage);
        } catch(Exception e){
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn, cstmt, null);
        }
        return hshDataMap;
    }

    public ArrayList getTipoDocumentoList() throws SQLException, Exception {


        ArrayList arrDocumentType = new ArrayList();
        HashMap map;

        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;

        String strMessage = null;
        String sqlStr = "BEGIN SWBAPPS.NP_TABLE01_PKG.SP_GET_VALUEDESC_X_NPTABLE (?,?,?); END;";

        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, "DOCUMENT_TYPE");
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);

            cstmt.executeQuery();

            strMessage = cstmt.getString(2);
            rs = (ResultSet)cstmt.getObject(3);

            if (StringUtils.isBlank(strMessage)) {
                while (rs.next()) {

                    map = new HashMap();
                    map.put("description", rs.getString(1));
                    map.put("value", rs.getString(2));
                    arrDocumentType.add(map);

                }
            }
        }
        catch(Exception e){
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn, cstmt, rs);
        }

        return arrDocumentType;
    }

    /**
     * Motivo:  Obtiene las Condiciones de los Reportes de Ordenes de Reparación
     * <br>Realizado por: <a href="mailto:eduardo.pena@teamsoft.com.pe">Eduardo Peña Vilca EPV</a>
     * <br>Fecha: 23/07/2015
     *
     * @param      npTable            Ej: REPORT_REPAIR_CON_PRESTAMO
     *                                     REPORT_REPAIR_SIN_PRESTAMO
     *
     * @return     description            Ej: 11
     * @return     nptag1             Ej: der, izq
     */
    public ArrayList getConditionsReport(String npTable) throws SQLException, Exception {


        ArrayList arrDocumentType = new ArrayList();
        HashMap map;

        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;

        String strMessage = null;
        String sqlStr = "BEGIN SWBAPPS.NP_TABLE01_PKG.SP_GET_DESC_TAG1_X_NPTABLE (?,?,?); END;";

        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, npTable);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);

            cstmt.executeQuery();

            strMessage = cstmt.getString(2);
            rs = (ResultSet)cstmt.getObject(3);

            if (StringUtils.isBlank(strMessage)) {
                while (rs.next()) {

                    map = new HashMap();
                    map.put("description", rs.getString(1));
                    map.put("nptag1", rs.getString(2));
                    arrDocumentType.add(map);

                }
            }
        }
        catch(Exception e){
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn, cstmt, rs);
        }

        return arrDocumentType;
    }
    
    //FBERNALES Requerimiento PM0010503
    /**
     * Proposito: validar s existe el campo numero de solictud en ordenes de Retail
     * @param     String userId
     * @param     String customerId
     * @return    Boolean
     */
    //FBERNALES Requerimiento PM0010503 Validacion del numero de solicitud en Retail
    public Boolean validateNumSolicitudRetail(String sNumeroSolicitud,int iFlagColumnOrderNumber) throws Exception {
        System.out.println("[GeneralDAO][validateNumSolicitudRetail] - INICIO");
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        int count;
        Boolean bValidate=false;
        String npTableValue = null;
        String sqlStr = "BEGIN ORDERS.spi_get_exist_order(?,?,?); END;";
        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, sNumeroSolicitud);
            cstmt.setLong(2, iFlagColumnOrderNumber);
            cstmt.registerOutParameter(3, OracleTypes.NUMBER);
            cstmt.executeQuery();

            count = cstmt.getInt(3);
            System.out.println("[GeneralDAO][validateNumSolicitudRetail] - count = "+count);
            if (count>0) {
                bValidate= true;            
            }else{
                bValidate= false;
            }
        }catch(SQLException e){
            System.out.println("[GeneralDAO][validateNumSolicitudRetail] - SQLException = "+e.getMessage());
            throw new SQLException(e);
        }catch(Exception e){
            System.out.println("[GeneralDAO][validateNumSolicitudRetail] - Exception = "+e.getMessage());
            throw new Exception(e);
        }
        finally{            
            closeObjectsDatabase(conn, cstmt, rs);
        }
        System.out.println("[GeneralDAO][validateNumSolicitudRetail] - bValidate = "+bValidate);
        return bValidate;
    }

   /**
    * Motivo: Obtiene la data según el np_table
    * <br>Realizado por: Luis Roque - GORA
    * <br>Fecha: 22/10/2015
    *
    * @param  strTableName, strValueDesc
    * @return HashMap
    * @throws java.sql.SQLException
    */
   public Map<String, String> getNPValueXNameAndDesc(String strTableName, String strValueDesc) throws SQLException, Exception {
      System.out.println("[GeneralDAO][getNPValueXNameAndDesc] - INICIO");
      Map<String, String> hshDataMap = new HashMap<String, String>();
      Connection conn = null;
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;
      String strMessage = null;
      String npTableValue = null;
      String sqlStr = "BEGIN SWBAPPS.SPI_GET_NP_TABLE_X_NPVALUEDESC(?,?,?,?); END;";
      try {
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.setString(1, strTableName);
         cstmt.setString(2, strValueDesc);
         cstmt.registerOutParameter(3, OracleTypes.CURSOR);
         cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
         cstmt.executeQuery();

         strMessage = cstmt.getString(4);

         if (StringUtils.isBlank(strMessage)) {
            rs = (ResultSet) cstmt.getObject(3);

            while (rs.next()) {
               npTableValue = rs.getString("npvalue");
               System.out.println("[GeneralDAO][getNPValueXNameAndDesc] - npvalue = " + npTableValue);
               break;
            }
         }
      }catch(SQLException e){
         System.out.println("[GeneralDAO][getNPValueXNameAndDesc] - SQLException = "+e.getMessage());
         throw new SQLException(e);
      }catch(Exception e){
         System.out.println("[GeneralDAO][getNPValueXNameAndDesc] - Exception = "+e.getMessage());
         throw new Exception(e);
      }finally{
         try{
            closeObjectsDatabase(conn, cstmt, rs);
         }catch (Exception e) {
            logger.error(formatException(e));
         }
      }
      hshDataMap.put("strTableValue", npTableValue);
      hshDataMap.put("strMessage", strMessage);

      return hshDataMap;
   }

   /**
    * Motivo: Obtiene la data según el nptable y npvalue <br>
    * Realizado por: Luis Roque - GORA <br>
    * Fecha: 22/10/2015
    *
    * @param strTableName, strValue
    * @return HashMap
    * @throws java.sql.SQLException
    */
   public Map<String, String> getNPTableXTableAndValue(String strTableName, String strValue)
           throws SQLException, Exception {
      System.out.println("[GeneralDAO][getNPValueDescXTableAndValue] - INICIO");
      Map<String, String> hshDataMap = new HashMap<String, String>();
      Connection conn = null;
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;
      String strMessage = null;
      try {
         String sqlStr = "BEGIN SWBAPPS.SPI_GET_NP_TABLE_X_NPVALUE(?,?,?,?); END;";
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.setString(1, strTableName);
         cstmt.setString(2, strValue);
         cstmt.registerOutParameter(3, OracleTypes.CURSOR);
         cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
         cstmt.executeQuery();

         strMessage = cstmt.getString(4);

         if (StringUtils.isBlank(strMessage)) {
            rs = (ResultSet) cstmt.getObject(3);
            while (rs.next()) {
               hshDataMap.put("strTable", rs.getString("nptable"));
               hshDataMap.put("strValue", rs.getString("npvalue"));
               hshDataMap.put("strValueDesc", rs.getString("npvaluedesc"));
               hshDataMap.put("strTag1", rs.getString("nptag1"));
               hshDataMap.put("strTag2", rs.getString("nptag2"));
               break;
            }
         }
      } catch (SQLException e) {
         e.printStackTrace();
         System.out.println("[GeneralDAO][getNPValueDescXTableAndValue] - SQLException = " + e.getMessage());
         throw new SQLException(e);
      } catch (Exception e) {
         e.printStackTrace();
         System.out.println("[GeneralDAO][getNPValueDescXTableAndValue] - Exception = " + e.getMessage());
         throw new Exception(e);
      }finally{
         try{
            closeObjectsDatabase(conn, cstmt, rs);
         }catch (Exception e) {
            logger.error(formatException(e));
         }
      }

      hshDataMap.put("strMessage", strMessage);

      return hshDataMap;
   }


   //EFLORES PRY-1112
   /**
    * Motivo: Obtiene lista de tuplas a excluir
    * <br>Realizado por: <a href="mailto:eddy.flores@dxc.com">Eddy Flores</a>
    * <br>Fecha: 06/11/2007
    * @return    HashMap
    */
   public  HashMap getExcludingAditionalServices() throws SQLException, Exception{
      System.out.println("[PRY-1112][GeneralDao][getExcludingAditionalServices] Inicio de procedimiento");
      List<ExcludingServiceBean> list = null;
      Connection conn = null;
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;
      HashMap hshResult=new HashMap();
      String strMessage=null;
      String sqlStr = "BEGIN ORDERS.NP_ORDERS40_PKG.SP_EM_EXCLUDING_SERVICES(?, ?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         cstmt.registerOutParameter(1,OracleTypes.CURSOR);
         cstmt.registerOutParameter(2,OracleTypes.VARCHAR);
         cstmt.execute();
         strMessage = cstmt.getString(2);
         if( strMessage == null){
            System.out.println("[PRY-1112][GeneralDao][getExcludingAditionalServices] Tuplas de servicios excluyentes :");
            rs = (ResultSet)cstmt.getCursor(1);
            list = new ArrayList<ExcludingServiceBean>();
            while (rs.next()) {
               ExcludingServiceBean excludingServiceBean = new ExcludingServiceBean();
               excludingServiceBean.setServicePrincipal(rs.getString("vchserviceprincipal"));
               excludingServiceBean.setServiceExcluded(rs.getString("vchserviceexcluded"));
               excludingServiceBean.setServiceMessage(rs.getString("vchservicemessage"));
               list.add(excludingServiceBean);
            }
         }
      }catch(Exception e){
         throw new Exception(e);
      }finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
      hshResult.put("objServiceList",list);
      hshResult.put(Constante.MESSAGE_OUTPUT,strMessage);
      return hshResult;
   }

   /**
    * Purpose: Listado de lugar de despacho delivery
    * Developer       Fecha       Comentario
    * =============   ==========  ======================================================================
    * JCURI          01/06/2018  Creación 
    * @project PRY-1093
    * @throws java.sql.SQLException
    * @throws java.lang.Exception
    * @return 
    * @param HashMap
    */
   public HashMap lugarDespachoDeliveryList() throws Exception {
 	   logger.info("[INI] OrderDAO/lugarDespachoDeliveryList");
 		
 		ArrayList list = new ArrayList();
 		HashMap objHashMap = new HashMap();
 		Connection conn = null;
 		OracleCallableStatement cstmt = null;
 		ResultSet rs = null;		
 		HashMap h=null;
 		String strMessage = null;

 		try {
 			String sqlStr = "BEGIN ORDERS.PKG_EM_DELIVERYREGION.SP_EM_CA_DISPATCHPLACELIST(?,?); END;";
 		     conn = Proveedor.getConnection();
 		     cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
 		     
 		     cstmt.registerOutParameter(1, OracleTypes.CURSOR);
 		     cstmt.registerOutParameter(2, Types.CHAR);
 		     cstmt.execute();
 		     
 		     strMessage = cstmt.getString(2);
 		     if (strMessage!=null && strMessage.startsWith("ORA-0000"))
 		        strMessage=null;         
 		     if( strMessage == null ){
 		        rs = (ResultSet)cstmt.getObject(1);   
 		        while (rs.next()) {
 		           h = new HashMap();
 		           h.put("buildingid", rs.getString("npbuildingid"));
 		           h.put("buildingname", rs.getString("npname"));      
 		           list.add(h);
 		        }
 		     }        
 		  
 		     objHashMap.put("strMessage",strMessage);
 		     objHashMap.put("objArrayList",list);

 		} catch (Exception e) {
 			throw new Exception(e);
 		} finally {
 			try {
 				conn.commit();
 				closeObjectsDatabase(conn, cstmt, rs);
 			} catch (Exception e) {
 				logger.error(formatException(e));
 			}
 		}
 		return objHashMap;
 	}
}