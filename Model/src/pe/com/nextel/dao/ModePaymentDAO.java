package pe.com.nextel.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import pe.com.nextel.bean.ModePaymentBean;


public class ModePaymentDAO  extends GenericDAO {
    
    /**
     Method : getModePaymentList
     Purpose: Obtener las formas de pago
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     David Coca      01/08/2007  Creación
     */
    public static ArrayList getModePaymentList(String strParamName, String strParamStatus) {
      
      ArrayList list = new ArrayList();        
      Connection conn = null; 
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;      
      String strMensaje = null;        
      String strSql = "BEGIN WEBSALES.SPI_GET_PARAMS_LIST(?, ?, ?, ?); END;";        
      try {        
        conn = Proveedor.getConnection();          
        cstmt = (OracleCallableStatement)conn.prepareCall(strSql);        
        cstmt.setString(1, strParamName);
        cstmt.setString(2, strParamStatus);          
        cstmt.registerOutParameter(3, OracleTypes.CURSOR );
        cstmt.registerOutParameter(4, Types.CHAR);          
        cstmt.executeUpdate();        
        rs = (ResultSet)cstmt.getObject(3);    
        strMensaje = cstmt.getString(4);                 
        while (rs.next()) {    
          ModePaymentBean mb = new ModePaymentBean();          
          mb.setNpValue(rs.getString(1));
          mb.setNpValueDesc(rs.getString(2));
          mb.setNpTag1(rs.getString(3));
          mb.setNpTag2(rs.getString(4));
          mb.setNpOrder(rs.getInt(5));            
          list.add(mb);          
        }        
        
        
      } catch (SQLException e) {
        System.out.println("getModePaymentList nError Nro.: "+e.getErrorCode() + " nMensaje:   -- >" + e.getMessage()+"\n");
        System.out.println("e.getErrorCode()  : "+e.getErrorCode() );
        System.out.println("strMensaje  : "+ strMensaje );
                
      } finally {
			try{
				closeObjectsDatabase(conn, cstmt, rs);
			}catch (Exception e) {
				logger.error(formatException(e));
			}
        /*if (cstmt != null) { 
           try {
              cstmt.close();
              conn.close();
           }catch (SQLException e) { 
              e.printStackTrace();
           }        
        }*/
     }         
    return list;
    }      
}
