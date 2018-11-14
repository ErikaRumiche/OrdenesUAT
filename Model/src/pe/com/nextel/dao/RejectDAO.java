package pe.com.nextel.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import pe.com.nextel.bean.RejectBean;


//import oracle.jdbc.OracleCallableStatement;

public class RejectDAO extends GenericDAO {
    
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
* Motivo: Obteniene el listado de los rechazos de la Orden
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 11/07/2007
* @param     lNpOrderid     Es el Id de la Orden
* @return    HashMap 
*/      
public  HashMap getRejectList(long lNpOrderid) throws SQLException, Exception{

   OracleCallableStatement cstmt = null;       
   ResultSet rs = null;        
   RejectBean rjbRechazos =null;
   String strMessage=null;
   Connection conn =null;
   ArrayList arrLista=new ArrayList();       
   HashMap hshRetorno=new HashMap();
   String strSql = "BEGIN NP_ORDERS01_PKG.SP_GET_REJECT_LST( ? , ? , ? ); end;";
   try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
      cstmt.setLong(1, lNpOrderid);
      cstmt.registerOutParameter(2, Types.CHAR);
      cstmt.registerOutParameter(3, OracleTypes.CURSOR);
      cstmt.execute();
      strMessage = cstmt.getString(2);
      rs = (ResultSet)cstmt.getObject(3);
      while (rs.next()) {   
         rjbRechazos = new RejectBean();
         rjbRechazos.setNpReason(rs.getString("npreason")); //npreason
         rjbRechazos.setNpStatus(rs.getString("npstatus")); //npstatus
         rjbRechazos.setNpCreatedBy(rs.getString("npcreatedby")); //npcreatedby
         rjbRechazos.setNpCreatedDate(rs.getDate("npcreateddate")); //npcreateddate
         rjbRechazos.setNpModifiedBy(rs.getString("npmodifiedby")); //npmodifiedby
         rjbRechazos.setNpModifiedDate(rs.getDate("npmodifieddate"));//npmodifieddate
         rjbRechazos.setNpRejectId(rs.getInt("nprejectid")); //nprejectid
         rjbRechazos.setNpOrderId(rs.getLong("nporderid")); //nporderid
         rjbRechazos.setNpComment(rs.getString("npcomment"));//npcomment
         rjbRechazos.setNpInbox(rs.getString("npinbox"));//npinbox            
         
         arrLista.add(rjbRechazos);
      }
   }
   catch (Exception e) {
      throw new Exception(e);
   }
   finally{
		closeObjectsDatabase(conn,cstmt,rs);
      /*if (rs != null)
         rs.close();
      if (cstmt != null)
         cstmt.close();   
      if (conn != null)
         conn.close();*/
   }
   hshRetorno.put("strMessage",strMessage);
   hshRetorno.put("arrRechazos",arrLista);
   return hshRetorno;

}
    

/**
* Motivo: Inserta un rechazo de la Orden 
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 06/09/2007
* @param     objReject      Objeto que contiene los datpos del Rechazo
* @return    String         Retorna el mensaje del procedimiento invocado
*/      
public  String insReject(RejectBean objReject,Connection conn) throws SQLException, Exception {

   OracleCallableStatement cstmt = null;        
   String strMessage=null;
   String strSql = "BEGIN ORDERS.NP_ORDERS07_PKG.SP_INS_REJECT( ?, ?, ?, ?, ?, ?, ?, ?); END;";    
    //System.out.println("Llamando al:  ORDERS.SPI_INS_REJECT_BY_ORDER");
   try{
      cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
      cstmt.setLong(1, objReject.getNpOrderId());
      cstmt.setString(2, objReject.getNpReason());
      cstmt.setString(3, objReject.getNpComment());
      cstmt.setString(4, objReject.getNpStatus());
      cstmt.setString(5, objReject.getNpCreatedBy());//objOrder.getNpCarrierId());    
      cstmt.setString(6, objReject.getNpInbox());
      cstmt.registerOutParameter(7, Types.NUMERIC);        
      cstmt.registerOutParameter(8, Types.CHAR);
      int i=cstmt.executeUpdate();
      objReject.setNpRejectId(cstmt.getInt(7));
      strMessage = cstmt.getString(8);
      System.out.println("Resultado de la insercion nUEVA-->"+i);
   }
   catch (Exception e) {
      throw new Exception(e);
   }
   finally{
		closeObjectsDatabase(null,cstmt,null);
      /*if (cstmt != null)
         cstmt.close();*/
   }
   return strMessage;
}
    

/**
 * Motivo: Actualiza un Rechazo de la Orden
 * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
 * <br>Fecha: 06/09/2007
 * @param     objReject      Objeto que contiene los datpos del Rechazo       
 * @return    String         Retorna el mensaje del procedimiento invocado
*/  
public  String updReject(RejectBean objReject,Connection conn) 
throws SQLException, Exception{

   OracleCallableStatement cstmt = null;        
   String strMessage="";
   String strSql = "BEGIN ORDERS.NP_ORDERS07_PKG.SP_UPDATE_REJECT( ?, ?, ?, ?); END;";               
   try{  
      cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
      
      cstmt.setInt(1, objReject.getNpRejectId());        
      cstmt.setString(2, objReject.getNpStatus());
      cstmt.setString(3, objReject.getNpModifiedBy());                     
      cstmt.registerOutParameter(4, Types.CHAR);
      int i=cstmt.executeUpdate();        
      strMessage = cstmt.getString(4);
      
      System.out.println("Resultado de la actualizacion-->"+i);
   }
   catch (Exception e) {
      throw new Exception(e);
   }
   finally{
		closeObjectsDatabase(null,cstmt,null);
      /*if (cstmt != null)
         cstmt.close();*/
   }
   return strMessage;
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
}
