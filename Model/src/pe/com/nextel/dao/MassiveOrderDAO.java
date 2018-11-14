package pe.com.nextel.dao;

import java.lang.Exception;
import java.lang.String;
import java.lang.System;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import oracle.sql.ARRAY;
import oracle.sql.STRUCT;

import org.apache.commons.lang.StringUtils;

import pe.com.nextel.bean.ItemBean;
import pe.com.nextel.bean.ItemServiceBean;
import pe.com.nextel.bean.ServiciosBean;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;


public class MassiveOrderDAO extends GenericDAO{

    /**
   Method : getItemOrder
   Purpose: Obtiene los Items asociados a un cliente
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Evelyn Ocampo   08/07/2009  Creación
   */
  public HashMap getItemOrder( long intCustomerId, long intSiteId, long intSpecification, long intCustomeridAcept)  throws Exception, SQLException {
    
    Connection conn = null; 
    ResultSet rs=null;
    OracleCallableStatement cstm = null;
   	ArrayList arrItemsList = new ArrayList();
    ItemBean itemBean = null;
    String strMessage = null;
    HashMap objHashMapResultado = new HashMap();    
       
    
    System.out.println("intCustomerId==============="+intCustomerId);
    try{
      String strSql = "BEGIN ORDERS.SPI_GET_ITEMS_MASSIVE(?, ?, ?, ?, ?, ?, ?); END;";
      conn = Proveedor.getConnection();
      
      cstm = (OracleCallableStatement) conn.prepareCall(strSql);
          
      cstm.setLong(1,intCustomerId);
      cstm.setLong(2,intSiteId);
      cstm.setLong(3,intSpecification);
      cstm.setLong(4,intCustomeridAcept);
      cstm.registerOutParameter(5, OracleTypes.ARRAY, "ORDERS.TT_ITEMS_MASSIVE");
      cstm.registerOutParameter(6, OracleTypes.NUMBER);
      cstm.registerOutParameter(7, Types.VARCHAR);
          
      cstm.execute();
      
      strMessage  = cstm.getString(7);
      
      if(StringUtils.isNotBlank(strMessage)){
        throw new Exception(strMessage);
      }
      
      
      ARRAY aryItemsList = (ARRAY)cstm.getObject(5);
      OracleResultSet adrs = (OracleResultSet) aryItemsList.getResultSet();
      logger.debug("LENGTH: "+aryItemsList.getOracleArray().length);
      while(adrs.next()) {        
        STRUCT stcItemsOrder = adrs.getSTRUCT(2);
        ItemBean objItemBean = new ItemBean();    
        
        objItemBean.setNpphone(MiUtil.defaultString(stcItemsOrder.getAttributes()[0], "")); 
        objItemBean.setNpsiminumber(MiUtil.defaultString(stcItemsOrder.getAttributes()[1], ""));        
        objItemBean.setNpmodel(MiUtil.defaultString(stcItemsOrder.getAttributes()[3], ""));
        objItemBean.setNpcontractnumber(MiUtil.defaultBigDecimal(stcItemsOrder.getAttributes()[4], new BigDecimal(0)).intValue()); 
        //objItemBean.setNpplanid(MiUtil.defaultBigDecimal(stcItemsOrder.getAttributes()[5], new BigDecimal(0)).longValue());
        objItemBean.setNporiginalplanname(MiUtil.defaultString(stcItemsOrder.getAttributes()[6], ""));
        objItemBean.setNpcontractstatus(MiUtil.defaultString(stcItemsOrder.getAttributes()[7], ""));  
        objItemBean.setNpoccurrence(MiUtil.parseInt(MiUtil.defaultString(stcItemsOrder.getAttributes()[10], "")));        
        objItemBean.setNpimeinumber(MiUtil.defaultString(stcItemsOrder.getAttributes()[12], ""));        
        objItemBean.setNpmodalitysell(MiUtil.defaultString(stcItemsOrder.getAttributes()[13], ""));
        objItemBean.setNpsolutionid(MiUtil.defaultBigDecimal(stcItemsOrder.getAttributes()[14], new BigDecimal(0)).longValue());
        objItemBean.setNporiginalplanid(MiUtil.defaultBigDecimal(stcItemsOrder.getAttributes()[15], new BigDecimal(0)).longValue());
        objItemBean.setNpsolutionname(MiUtil.defaultString(stcItemsOrder.getAttributes()[16], ""));
        objItemBean.setNpradio(MiUtil.defaultString(stcItemsOrder.getAttributes()[17], ""));
        //objItemBean.setNpproductlineid(MiUtil.defaultBigDecimal(stcItemsOrder.getAttributes()[18], new BigDecimal(0)).longValue());
        objItemBean.setNporiginalproductlineid(MiUtil.defaultBigDecimal(stcItemsOrder.getAttributes()[18], new BigDecimal(0)).longValue());
        objItemBean.setNpprice(MiUtil.defaultString(stcItemsOrder.getAttributes()[19], "")); 
        objItemBean.setNprent(MiUtil.defaultString(stcItemsOrder.getAttributes()[20], "")); 
        objItemBean.setNpcurrency(MiUtil.defaultString(stcItemsOrder.getAttributes()[21], "")); 
        objItemBean.setNpquantity(MiUtil.defaultBigDecimal(stcItemsOrder.getAttributes()[22], new BigDecimal(0)).intValue()); 
        objItemBean.setNporiginalprice(MiUtil.defaultString(stcItemsOrder.getAttributes()[23], "")); 
        objItemBean.setNppricetype(MiUtil.defaultString(stcItemsOrder.getAttributes()[24], ""));
        objItemBean.setNppricetypeid(MiUtil.defaultBigDecimal(stcItemsOrder.getAttributes()[25], new BigDecimal(0)).intValue()); 
        objItemBean.setNppricetypeitemid(MiUtil.defaultBigDecimal(stcItemsOrder.getAttributes()[26], new BigDecimal(0)).intValue());         
        //objItemBean.setNpproductid(MiUtil.defaultBigDecimal(stcItemsOrder.getAttributes()[27], new BigDecimal(0)).longValue());
        objItemBean.setNporiginalproductid(MiUtil.defaultBigDecimal(stcItemsOrder.getAttributes()[27], new BigDecimal(0)).longValue());
        objItemBean.setNppayrespname(MiUtil.defaultString(stcItemsOrder.getAttributes()[28], "")); 
        objItemBean.setNpcustcode(MiUtil.defaultString(stcItemsOrder.getAttributes()[29], "")); 
        objItemBean.setNpequipment(MiUtil.defaultString(stcItemsOrder.getAttributes()[30], ""));
        objItemBean.setNpwarrant(MiUtil.defaultString(stcItemsOrder.getAttributes()[31], "")); 
        objItemBean.setNpssaacontratado(MiUtil.defaultString(stcItemsOrder.getAttributes()[32], ""));        
        objItemBean.setNpbscspaymntrespcustomeridId(MiUtil.defaultBigDecimal(stcItemsOrder.getAttributes()[33], new BigDecimal(0)).longValue());
        objItemBean.setNpproductlinename(MiUtil.defaultString(stcItemsOrder.getAttributes()[34], ""));                  
        objItemBean.setNpproductlineid(MiUtil.defaultBigDecimal(stcItemsOrder.getAttributes()[35], new BigDecimal(0)).longValue());
        objItemBean.setNpproductid(MiUtil.defaultBigDecimal(stcItemsOrder.getAttributes()[36], new BigDecimal(0)).longValue());
        objItemBean.setNpcountreasonId(MiUtil.defaultBigDecimal(stcItemsOrder.getAttributes()[37], new BigDecimal(0)).longValue());
        //objItemBean.setNpcountreasonId(1);
        arrItemsList.add(objItemBean);
      }
      
    }catch (Exception e) {
      logger.error(formatException(e));
      objHashMapResultado.put("strMessage",e.getMessage());
    }finally{
      try{
        closeObjectsDatabase(conn,cstm,rs); 
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }         
    objHashMapResultado.put(Constante.MESSAGE_OUTPUT, strMessage);
    objHashMapResultado.put("objArrayList", arrItemsList);
    return objHashMapResultado;            
  }
  
  
  
/**
    * Motivo: Registra el item de la orden
    * <br>Realizado por: <a href="mailto:evelyn.ocampo@nextel.com.pe">Evelyn Ocampo</a>
    * <br>Fecha: 10/07/2009
    * @param    ItemBean itemBean   
    * @param    Connection conn 
    */
  public String doSaveItemMassive(ItemBean itemBean, Connection conn) throws Exception, SQLException { 
       
    String strMessage = null; 
    int    intResultTransaction = 0,
           intOrderId = 0,
           iException = 0;
           
    long   itemId = 0;
    OracleCallableStatement cstmt = null;
    
    try{      
      String strSql = "BEGIN ORDERS.NP_MASSIVE_ORDER_PKG.SP_INS_ITEM_MASSIVE(    ?,    ?,    ?,     ?,    ?, " + //5   
                                                                "   ?,    ?,    ?,     ?,    ?, " + //10
                                                                "   ?,    ?,    ?,     ?,    ?, " + //15
                                                                "   ?,    ?,    ?,     ?,    ?, " + //20
                                                                "   ?,    ?,    ?,     ?,    ?, " + //25
                                                                "   ?,    ?,    ?,     ?,    ?, " + //30
                                                                "   ?,    ?,    ?,     ?,    ?, " + //35
                                                                "   ?,    ?,    ?,     ?,    ?, " + //40
                                                                "   ?,    ?,    ?,     ?,    ?, " + //45
                                                                "   ?,    ?,    ?,     ?,    ?, " + //50
                                                                "   ?,    ?,    ?,     ?,    ?, " + //55
                                                                "   ?,    ?,    ?,     ?,    ?, " + //60
                                                                "   ?,    ?,    ?,     ?,    ?, " + //65
                                                                "   ?,    ?,    ?,     ?,    ?, " + //70
                                                                "   ?,    ?,    ?,     ?,    ?, " +  //75
                                                                "   ?,    ?,    ?,     ?,    ?, " +  //80
                                                                "   ?,    ?,    ?); END;";           //83 se agrego un Item
        
        
        
        cstmt = (OracleCallableStatement)conn.prepareCall(strSql); 
        
        cstmt.setLong(1, itemBean.getNporderid());
        cstmt.setLong(2, itemBean.getNpsolutionid());
        cstmt.setString(3, itemBean.getNpmodalitysell());
        cstmt.setString(4, itemBean.getNpequipment());
        cstmt.setString(5, itemBean.getNpphone());
        cstmt.setString(6, itemBean.getNpserialnumber());
        cstmt.setString(7, itemBean.getNpimeinumber());
        cstmt.setString(8, itemBean.getNpsiminumber());
        
        if( itemBean.getNpplanid() == 0 )
          cstmt.setNull(9, OracleTypes.NUMBER);
        else
          cstmt.setDouble(9, itemBean.getNpplanid());
        
        if( itemBean.getNpproductlineid() == 0 )
          cstmt.setNull(10, OracleTypes.NUMBER);
        else
          cstmt.setLong(10, itemBean.getNpproductlineid());
        
        if( itemBean.getNpproductid() == 0 )
          cstmt.setNull(11, OracleTypes.NUMBER);
        else
          cstmt.setLong(11, itemBean.getNpproductid());
        
        cstmt.setInt(12, itemBean.getNpquantity());
        
        if( itemBean.getNpprice().equals("") )
          cstmt.setNull(13, OracleTypes.NUMBER);
        else
          cstmt.setDouble(13, MiUtil.parseDouble(itemBean.getNpprice()));
        
        if( itemBean.getNppriceexception().equals("") )
          cstmt.setNull(14, OracleTypes.NUMBER);
        else
          cstmt.setDouble(14, MiUtil.parseDouble(itemBean.getNppriceexception()));
        
        //Renta
        if( itemBean.getNprent().equals(""))
          cstmt.setNull(15, OracleTypes.NUMBER );
        else
          cstmt.setDouble(15, MiUtil.parseDouble(itemBean.getNprent()));
        
        //Descuento
        if( itemBean.getNpdiscount().equals(""))
          cstmt.setNull(16, OracleTypes.NUMBER );
        else
          cstmt.setDouble(16, MiUtil.parseDouble(itemBean.getNpdiscount()));
        
        cstmt.setString(17, itemBean.getNpcurrency());
        cstmt.setString(18, itemBean.getNpwarrant());
        
        if( itemBean.getNpoccurrence() == 0 )
          cstmt.setNull(19,OracleTypes.NUMBER);
        else
          cstmt.setInt(19, itemBean.getNpoccurrence());
        
        if( itemBean.getNppromotionid() == 0 )
          cstmt.setNull(20,OracleTypes.NUMBER);
        else
          cstmt.setLong(20, itemBean.getNppromotionid());
          
        if( itemBean.getNpaddendumid() == 0 )
          cstmt.setNull(21,OracleTypes.NUMBER);
        else
          cstmt.setLong(21, itemBean.getNpaddendumid());
        
        if ( itemBean.getNpownimeinumber() == "")
          cstmt.setNull(22,OracleTypes.NUMBER);
        else 
          cstmt.setString(22, itemBean.getNpownimeinumber());
        
        if ( itemBean.getNpinventorycode() == "")
          cstmt.setNull(23,OracleTypes.NUMBER);
        else 
          cstmt.setString(23, itemBean.getNpinventorycode());
        
        if ( itemBean.getNpequipmentreturn() == "")
          cstmt.setNull(24,OracleTypes.NUMBER);
        else 
          cstmt.setString(24, itemBean.getNpequipmentreturn());
        
        if ( itemBean.getNpequipmentnotyetgiveback() == "")
          cstmt.setNull(25,OracleTypes.NUMBER);
        else 
          cstmt.setString(25, itemBean.getNpequipmentnotyetgiveback());
          
        cstmt.setDate(26, itemBean.getNpequipmentreturndate());
        
        //Excepciones
        if ( itemBean.getNpexception() == "")
          cstmt.setNull(27,OracleTypes.NUMBER);
        else 
          cstmt.setString(27, itemBean.getNpexception());   
          
        if ( itemBean.getNpexceptionrevenue() == "")
          cstmt.setNull(28,OracleTypes.NUMBER);
        else 
          cstmt.setString(28, itemBean.getNpexceptionrevenue());          
          
        if (itemBean.getNpexceptionrevenuediscount()==0) 
          cstmt.setNull(29,OracleTypes.NUMBER);
        else 
          cstmt.setDouble(29, itemBean.getNpexceptionrevenuediscount());
        
        if ( itemBean.getNpexceptionrent() == "")
          cstmt.setNull(30,OracleTypes.NUMBER);
        else 
          cstmt.setString(30, itemBean.getNpexceptionrent());  
                  
        if (itemBean.getNpexceptionrentdiscount()==0)
          cstmt.setNull(31,OracleTypes.NUMBER);
        else 
          cstmt.setDouble(31, itemBean.getNpexceptionrentdiscount());
        
        if ( itemBean.getNpexceptionminadidispatch() == "")
          cstmt.setNull(32,OracleTypes.NUMBER);
        else 
          cstmt.setString(32, itemBean.getNpexceptionminadidispatch());  
           
        if ( itemBean.getNpexceptionminaditelephony() == "")
          cstmt.setNull(33,OracleTypes.NUMBER);
        else 
          cstmt.setString(33, itemBean.getNpexceptionminaditelephony());
                  
        
        //Fin de Excepciones
        if( itemBean.getNpinstallationaddressid() == 0)
          cstmt.setNull(34, OracleTypes.NUMBER);
        else
          cstmt.setLong(34, itemBean.getNpinstallationaddressid());
          
        cstmt.setDate(35, itemBean.getNpmodificationdate());
        cstmt.setString(36, itemBean.getNpmodificationby());
        cstmt.setDate(37, itemBean.getNpcreateddate());
        cstmt.setString(38, itemBean.getNpcreatedby());
        
        if (itemBean.getNpconceptid()==0)
          cstmt.setNull(39, OracleTypes.NUMBER);
        else 
          cstmt.setLong(39, itemBean.getNpconceptid());
          
        cstmt.setString(40, itemBean.getNpplanname());
        
        /*Inicio - Banda Ancha*/
        if( itemBean.getNpsharedinstalationid() == 0)          
          cstmt.setNull(41, OracleTypes.NUMBER);
        else
          cstmt.setLong(41, itemBean.getNpsharedinstalationid());
        
        cstmt.setString(42, itemBean.getNpsharedinstal());
        
        if( itemBean.getNpcontractnumber() == 0)
          cstmt.setNull(43, OracleTypes.NUMBER);
        else
          cstmt.setInt(43, itemBean.getNpcontractnumber());
        
        if( itemBean.getNpfirstcontract() == "")
          cstmt.setNull(44, OracleTypes.NUMBER);
        else
          cstmt.setString(44, itemBean.getNpfirstcontract());
        
        //Precio de Instalación
        if( itemBean.getNpinstalationprice().equals(""))
          cstmt.setNull(45, OracleTypes.NUMBER);
        else
          cstmt.setDouble(45, MiUtil.parseDouble(itemBean.getNpinstalationprice()));
        
        //Precio de Instalación de Excepción
        if( itemBean.getNpinstalationexception().equals(""))
          cstmt.setNull(46, OracleTypes.NUMBER);
        else
          cstmt.setDouble(46, MiUtil.parseDouble(itemBean.getNpinstalationexception()));
        
        if( itemBean.getNplinktype().equals(""))
          cstmt.setNull(47, OracleTypes.NUMBER);
        else
          cstmt.setString(47, itemBean.getNplinktype());
        
        if( itemBean.getNpnetworkhosttype() == 0)
          cstmt.setNull(48, OracleTypes.NUMBER);
        else          
          cstmt.setDouble(48, itemBean.getNpnetworkhosttype());
          
        
        cstmt.setTimestamp(49, itemBean.getNpfeasibilityprogdate());

        cstmt.setString(50, itemBean.getNpfeasibility());
        cstmt.setString(51, itemBean.getNpinstalation());
        cstmt.setTimestamp(52, itemBean.getNpinstalationprogdate());
        
        /*Fin - Banda Ancha*/
        if( itemBean.getNporiginalplanid() == 0)
          cstmt.setNull(53, OracleTypes.NUMBER);
        else
          cstmt.setLong(53, itemBean.getNporiginalplanid());
        
        cstmt.setString(54, itemBean.getNporiginalplanname());
        cstmt.setString(55, itemBean.getNpmodel());
        cstmt.setString(56, itemBean.getNpnewphone());
        
        if( itemBean.getNparearespdev() == 0)
          cstmt.setNull(57, OracleTypes.NUMBER);
        else
          cstmt.setLong(57, itemBean.getNparearespdev());
        
        if( itemBean.getNpprovidergrpiddev() == 0)  
          cstmt.setNull(58, OracleTypes.NUMBER);
        else
          cstmt.setLong(58, itemBean.getNpprovidergrpiddev());
        
        if( itemBean.getNporiginalproductid() == 0)  
          cstmt.setNull(59, OracleTypes.NUMBER);
        else
          cstmt.setLong(59, itemBean.getNporiginalproductid());
        
        /**Traslado (Inicio) - Banda Ancha**/
        if( itemBean.getNpnewaddress() == 0 )
          cstmt.setNull(60,OracleTypes.NUMBER);
        else
          cstmt.setLong(60,itemBean.getNpnewaddress());
        
        cstmt.setString(61,itemBean.getNpcontactname());
        
        cstmt.setString(62,itemBean.getNpphonenumber1());
        cstmt.setString(63,itemBean.getNpphonenumber2());
        
        if( itemBean.getNpaditionalcost().equals("") )
          cstmt.setNull(64,OracleTypes.NUMBER);
        else
          cstmt.setDouble(64,MiUtil.parseDouble(itemBean.getNpaditionalcost()));
        
        cstmt.setString(65,itemBean.getNpdescription());
        
        /**Traslado (Fin) - Banda Ancha**/
        
        cstmt.setString(66, itemBean.getNpminutesrate());
        
        /**Banda Ancha - Plan Tarifario (Inicio)**/
        
        if( itemBean.getNporigmainservice() == 0 )
          cstmt.setNull(67,OracleTypes.NUMBER);
        else
          cstmt.setLong(67,itemBean.getNporigmainservice());
        
        if( itemBean.getNpnewmainservice() == 0 )
          cstmt.setNull(68,OracleTypes.NUMBER);
        else
          cstmt.setLong(68,itemBean.getNpnewmainservice());
        
        /**Banda Ancha - Plan Tarifario (Fin)**/
        
        cstmt.setDate(69,itemBean.getNpendservicedate());
        cstmt.setString(70,itemBean.getNpreferencephone());
        
        /**Acuerdos Comerciales(Inicio)**/
        if ( itemBean.getNporiginalprice() == "")
          cstmt.setNull(71, OracleTypes.NUMBER);
        else
          cstmt.setDouble(71, MiUtil.parseDouble(itemBean.getNporiginalprice()));
                  
        if ( itemBean.getNppricetype() == "")
          cstmt.setNull(72, OracleTypes.NUMBER);
        else
          cstmt.setString(72,itemBean.getNppricetype());
        
        /*if( itemBean.getNppricetypeid() == 0 )
          cstmt.setNull(73,OracleTypes.NUMBER);
        else*/
          cstmt.setLong(73,itemBean.getNppricetypeid());
        
        /*if( itemBean.getNppricetypeitemid() == 0 )
          cstmt.setNull(74,OracleTypes.NUMBER);
        else*/
          cstmt.setLong(74,itemBean.getNppricetypeitemid());                  
        
        /**Acuerdos Comerciales(Fin)**/
        
        cstmt.setString(75,itemBean.getNpfixedphone());
        cstmt.setString(76,itemBean.getNplocution());
        
        /*Incio Responsable de Pago*/            
        if (itemBean.getNpsiteid() == 0 )                
          cstmt.setNull(77, OracleTypes.NUMBER);          
        else          
          cstmt.setLong(77,itemBean.getNpsiteid());        
                  
        /*Fin Responsable de Pago*/
        
        /*Inicio Contratos de Referencia*/       
        if (itemBean.getNpinternetrefcontract() == 0)
          cstmt.setNull(78, OracleTypes.NUMBER);
        else
          cstmt.setLong(78,itemBean.getNpinternetrefcontract()); 
          
        if (itemBean.getNpdatosrefcontract() == 0)
          cstmt.setNull(79, OracleTypes.NUMBER);
        else
          cstmt.setLong(79,itemBean.getNpdatosrefcontract());
          
        cstmt.setString(80,itemBean.getNptfrefphonenumber());                    
        /*Fin Contratos de Referencia*/
        /**Suspensiones Definitivas**/
        if (MiUtil.parseInt(itemBean.getNpestadoitemId())==0)
          cstmt.setNull(81, OracleTypes.NUMBER);
        else 
          cstmt.setInt(81,MiUtil.parseInt(itemBean.getNpestadoitemId()));
        /**Suspensiones Definitivas**/        
        cstmt.registerOutParameter(82, OracleTypes.NUMBER);
        cstmt.registerOutParameter(83, OracleTypes.VARCHAR);
        
        logger.debug(itemBean);
        
        cstmt.executeUpdate();
        
        //dbmsOutput.show();
        //dbmsOutput.close();
        
        strMessage = cstmt.getString(83);
        if(strMessage!=null) return strMessage;
        
        itemId     = MiUtil.parseLong(""+cstmt.getLong(82));
        itemBean.setNpitemid(itemId);
              
    }catch (Exception e) {
      logger.error(formatException(e));              
      strMessage = e.getClass() + " - " + e.getMessage();
    }finally{
      try{
        closeObjectsDatabase(null,cstmt,null); 
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }        
      
    return strMessage;
  }
  
  
  /**
    * Motivo: Registra las incidencias para el modulo de Masivos.    
    * <br>Realizado por: <a href="mailto:magally.mora@nextel.com.pe">Magally Mora</a>
    * <br>Fecha: 23/07/2009
    */  
public  HashMap getPlanList()
throws SQLException, Exception{
   HashMap hshData=new HashMap();
   String strMessage=null;
   HashMap h=null;
   ArrayList list = new ArrayList();
   Connection conn = null;
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   String sqlStr = "BEGIN ORDERS.NP_ORDERS01_PKG.SP_GET_PLANS_LST(?, ?, ?, ?, ?, ?); END;";
  
   try{
      conn = Proveedor.getConnection();
      
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      
    
       
        cstmt.setString(1,"0");
        cstmt.setString(2,"CUSTOMER");
        cstmt.setInt(3,OracleTypes.NUMBER);
        cstmt.setInt(4,OracleTypes.NUMBER);
        cstmt.registerOutParameter(5, OracleTypes.CURSOR );
        cstmt.registerOutParameter(6, Types.VARCHAR);
      
      cstmt.execute();
      
      strMessage = cstmt.getString(6);
      rs = (ResultSet)cstmt.getObject(5);
      
      while (rs.next()) {
         h = new HashMap();           
         h.put("descripcion", rs.getString(1));           
         h.put("tmcode", rs.getString(2));
         h.put("nivel", rs.getString(2));  
         
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
    * Motivo: Obtiene los servicios asociados a una solución de negocio.
    * Pedidos Masivos(Cambio de Plan, SSAA, Transferencia de Equipos)
    * <br>Realizado por: <a href="mailto:evelyn.ocampo@nextel.com.pe">Evelyn Ocampo</a>
    * <br>Fecha: 14/07/2009
    * @param     iDivisionId        IN NUMBER,
    * @param     strMsgError    Contiene el mensaje de error si existiera
    * @return    HashMap 
  */
  public  HashMap getServiceList(int iDivisionId, int iPlanId) throws SQLException, Exception{
 
     ArrayList list = null;
     ServiciosBean  serviciosBean = null;
     ResultSet rs = null;
     Connection conn = null;
     OracleCallableStatement cstmt = null;
     HashMap hshResult=new HashMap();
     String strMessage=null;
     String sqlStr = "BEGIN ORDERS.NP_MASSIVE_ORDER01_PKG.SP_GET_SERVICE_ALL_LIST(?, ?, ?, ?); END;";
     try{
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
       
        cstmt.setInt(1, iDivisionId);   
        cstmt.setInt(2, iPlanId); 
        cstmt.registerOutParameter(3,OracleTypes.CURSOR);
        cstmt.registerOutParameter(4, Types.CHAR);
        cstmt.execute();
   
        strMessage = cstmt.getString(4);
        if( strMessage == null){
          rs = (ResultSet)cstmt.getCursor(3);
          list = new ArrayList();       
          while (rs.next()) {           
            serviciosBean = new ServiciosBean();                
            serviciosBean.setNpservicioid(rs.getLong("npservicioid"));
            serviciosBean.setNpnomserv(rs.getString("npnomserv"));
            serviciosBean.setNpnomcorserv(rs.getString("npnomcorserv"));
            serviciosBean.setNpexcludingind(rs.getString("npexcludingind"));      
            //serviciosBean.setNpsolutionid(rs.getInt("npobjectid"));   
            list.add(serviciosBean);           
          }
        }
        
        System.out.println("[getServiceList][size]"+list.size()); 
     }
     catch(Exception e){
        e.printStackTrace();
         throw new Exception(e);         
     }
     finally{
		   closeObjectsDatabase(conn, cstmt, rs);
     }
     hshResult.put("objServiceList",list);
     hshResult.put("strMessage",strMessage);       
     return hshResult;
  }
  
      /**
    * Motivo: Obtiene los servicios adicionales dada la división de negocio.
    * Pedidos Masivos(Cambio de Plan, SSAA, Transferencia de Equipos)
    * <br>Realizado por: <a href="mailto:evelyn.ocampo@nextel.com.pe">Evelyn Ocampo</a>
    * <br>Fecha: 14/07/2009
    * @param     iDivisionId        IN NUMBER,
    * @param     strMsgError    Contiene el mensaje de error si existiera
    * @return    HashMap 
  */
  public  HashMap getServiceListBySolution(int iDivisionId) throws SQLException, Exception{
 
     ArrayList list = null;
     ServiciosBean  serviciosBean = null;
     ResultSet rs = null;
     Connection conn = null;
     OracleCallableStatement cstmt = null;
     HashMap hshResult=new HashMap();
     String strMessage=null;
     String sqlStr = "BEGIN ORDERS.NP_MASSIVE_ORDER01_PKG.SP_GET_SERVICE_BY_SOLUTION( ?, ?, ?); END;";
     try{
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
       
        cstmt.setInt(1, iDivisionId);   
        cstmt.registerOutParameter(2,OracleTypes.CURSOR);
        cstmt.registerOutParameter(3, Types.CHAR);
        cstmt.execute();
   
        strMessage = cstmt.getString(3);
        if( strMessage == null){
          rs = (ResultSet)cstmt.getCursor(2);
          list = new ArrayList();       
          while (rs.next()) {           
            serviciosBean = new ServiciosBean();   
            serviciosBean.setNpsolutionid(rs.getInt("npobjectid")); 
            serviciosBean.setNpservicioid(rs.getLong("npservicioid"));            
            //serviciosBean.setNpnomserv(rs.getString("npnomserv"));
            serviciosBean.setNpexcludingind(rs.getString("npexcludingind"));              
            serviciosBean.setNpnomcorserv(rs.getString("npnomcorserv"));
            list.add(serviciosBean);           
          }
        }
        
        System.out.println("[getServiceListBySolution][size]"+list.size()); 
     }
     catch(Exception e){
        e.printStackTrace();
         throw new Exception(e);         
     }
     finally{
		   closeObjectsDatabase(conn, cstmt, rs);
     }
     hshResult.put("objServiceBySolutionList",list);
     hshResult.put("strMessage",strMessage);       
     return hshResult;
  }
  
  /**
    * Motivo: Obtiene los servicios asociados a una solución de negocio.
    * Pedidos Masivos(Cambio de Plan, SSAA, Transferencia de Equipos)
    * <br>Realizado por: <a href="mailto:evelyn.ocampo@nextel.com.pe">Evelyn Ocampo</a>
    * <br>Fecha: 14/07/2009
    * @param     iDivisionId        IN NUMBER,
    * @param     strMsgError    Contiene el mensaje de error si existiera
    * @return    HashMap 
  */
  public  HashMap getServiceItemListBySolution(int iSolutionId) throws SQLException, Exception{
     System.out.println("Entrando getServiceItemListBySolution===============");
     ArrayList list = null;
     ServiciosBean  serviciosBean = null;
     ResultSet rs = null;
     Connection conn = null;
     OracleCallableStatement cstmt = null;
     HashMap hshResult=new HashMap();
     String strMessage=null;
     String sqlStr = "BEGIN ORDERS.NP_MASSIVE_ORDER01_PKG.SP_GET_ALLSERVICE_BY_SOLUTION( ?, ?, ?); END;";
     try{
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
       
        cstmt.setInt(1, iSolutionId);   
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
            serviciosBean.setNpexcludingind(rs.getString("npexcludingind"));      
            list.add(serviciosBean);           
          }
        }
        
        System.out.println("[getServiceItemListBySolution][size]"+list.size()); 
     }
     catch(Exception e){
        e.printStackTrace();
         throw new Exception(e);         
     }
     finally{
		   closeObjectsDatabase(conn, cstmt, rs);
     }
     hshResult.put("objArrayList",list);
     hshResult.put("strMessage",strMessage);       
     return hshResult;
  }
  
    
  public String getItemMassiveServiceInsertar(ItemServiceBean itemServiceBean, Connection conn) throws Exception, SQLException { 
      
      String strMensaje = null; 
      int    intResultTransaction = 0;
      OracleCallableStatement cstmt = null;
      int intOrderId = 0;
      try{
        String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_INS_ORDER_ITEM_SERVICE(    ?,    ?,    ?,     ?,    ?, " + //5
                                                                                "   ?,    ?); END;";                //7                                       
          
       
            cstmt = (OracleCallableStatement)conn.prepareCall(strSql); 
            
            cstmt.setLong(1, itemServiceBean.getNpitemid());
            cstmt.setLong(2, itemServiceBean.getNpserviceid());
            cstmt.setString(3, itemServiceBean.getNpservicetype());
            if (itemServiceBean.getNpserviceprice() == 0 )                
              cstmt.setNull(4, OracleTypes.NUMBER);          
            else          
              cstmt.setDouble(4,itemServiceBean.getNpserviceprice()); 
          
           // cstmt.setDouble(4, itemServiceBean.getNpserviceprice());
            if (itemServiceBean.getNpservicefree() == "" )                
              cstmt.setNull(5, OracleTypes.NUMBER);          
            else          
              cstmt.setString(5,itemServiceBean.getNpservicefree()); 
              
            //cstmt.setString(5, itemServiceBean.getNpservicefree());
            cstmt.setString(6, itemServiceBean.getNpaction());
            cstmt.registerOutParameter(7, OracleTypes.VARCHAR);
                  
            cstmt.executeUpdate();
            strMensaje = cstmt.getString(7); 
            
            if( strMensaje!=null )
              return strMensaje;  
      }catch (Exception e) {
        logger.error(formatException(e));              
        strMensaje = e.getMessage();
      }finally{
        try{
          closeObjectsDatabase(null,cstmt,null); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
      }     
      return strMensaje;
    }
    
    
    /**
    * Motivo: Registra las incidencias para el modulo de Masivos.    
    * <br>Realizado por: <a href="mailto:henry.rengifo@nextel.com.pe">Henry Rengifo</a>
    * <br>Fecha: 27/07/2009
    * @param     nCustomerid        IN NUMBER,
    * @param     nSpecification     IN NUMBER,
    * @param     nOrderid           IN NUMBER,
    * @param     nUserId            IN NUMBER,
    * @param     strLogin           IN VARCHAR2,
    * @return    strMsgError        Contiene el mensaje de error si existiera    
  */  
    public String insertIncidentMassive(long nCustomerid, long nSpecification, 
                                        long nOrderid, long nUserId, String strLogin, Connection conn) throws Exception, SQLException { 
      
      String strMensaje = null;       
      OracleCallableStatement cstmt = null;            
      try{    
        
        System.out.println("============== Inicio - insertIncidentMassive ==================");
        System.out.println("nCustomerid    ["+nCustomerid+"]");
        System.out.println("nSpecification ["+nSpecification+"]");
        System.out.println("nOrderid       ["+nOrderid+"]");
        System.out.println("nUserId        ["+nUserId+"]");
        System.out.println("strLogin        ["+strLogin+"]");        
      
        String strSql = "BEGIN ORDERS.NP_MASSIVE_ORDER_PKG.SP_INS_INCIDENT_MASSIVE( ?, ?, ?, ?, ?, ?); END;";                                                     
        cstmt = (OracleCallableStatement)conn.prepareCall(strSql); 
            
            cstmt.setLong(1, nCustomerid);
            cstmt.setLong(2, nSpecification);            
            cstmt.setLong(3, nOrderid);
            cstmt.setLong(4, nUserId);
            cstmt.setString(5, strLogin);
            cstmt.registerOutParameter(6, OracleTypes.VARCHAR);                  
            cstmt.executeUpdate();            
            strMensaje = cstmt.getString(6); 
      }catch (Exception e) {                      
            strMensaje = e.getMessage();
      }
      finally{
          try{
            closeObjectsDatabase(null,cstmt,null); 
          }catch (Exception e) {
            logger.error(formatException(e));
          }
      }        
      System.out.println("==============Fin - insertIncidentMassive ==================");
      return strMensaje;
    }
    
    /**
    * Motivo: Se Obtiene los servicios de cada numero telefonico.    
    * <br>Realizado por: <a href="mailto:henry.rengifo@nextel.com.pe">Henry Rengifo</a>
    * <br>Fecha: 05/08/2009
    * @param     numContract        IN NUMBER,
    * @return    HashMap            Contiene los servicios   
  */  
    public HashMap getCommercialService(long numContract) throws Exception, SQLException {   
      Connection conn = null; 
      String strMensaje           = null;       
      HashMap hshResult=new HashMap();
      int    intResultTransaction = 0;
      OracleCallableStatement cstmt = null;
      int intOrderId = 0;
      try{
      
        System.out.println("Contrato desde Dao ==> "+numContract);
        String strSql = "BEGIN ORDERS.NP_MASSIVE_ORDER_PKG.SP_GET_COMMERCIAL_SERVICE( ?,  ?,   ?,  ?); END; ";        
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement)conn.prepareCall(strSql); 

            cstmt.setLong(1, numContract);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);               

            cstmt.executeUpdate();

            strMensaje = cstmt.getString(4); 

            System.out.println("Valor del Servicio == > "+ cstmt.getString(2));

            if( strMensaje ==null ){
              hshResult.put("objCommercialService",cstmt.getString(2)); 
            }                

      }catch (Exception e) {
        logger.error(formatException(e));              
        strMensaje = e.getMessage();
      }finally{
        try{
          //closeObjectsDatabase(null,cstmt,null); 
          closeObjectsDatabase(conn,cstmt,null);
        }catch (Exception e) {
          logger.error(formatException(e));
        }
      }     
      return hshResult;
    }
    
   /**
      * Motivo: Valida el que los telefonos no esten asociados a ordenes
      * <br>Realizado por: <a href="mailto:henry.rengifo@nextel.com.pe">Henry Rengifo</a>
      * <br>Fecha: 20/08/2009
    */  
      public  HashMap getValidateByPhone(long npphone, long intSpecification, long nOrderid) throws SQLException, Exception{
         
         ArrayList list = null;         
         //ResultSet rs = null;
         Connection conn = null;
         OracleCallableStatement cstmt = null;
         HashMap hshResult=new HashMap();
         String strMessage =null;
         String strType_error = null;
         
         System.out.println("=========== Inicio - getValidateByPhone ================");
         System.out.println("npphone          ["+npphone+"]");
         System.out.println("intSpecification ["+intSpecification+"]");
         System.out.println("nOrderid         ["+nOrderid+"]");                  
         
         String sqlStr = "BEGIN ORDERS.NP_ORDERS07_PKG.SP_SPEC_VALIDATE_BY_PHONE( ?, ?, ?, ? , ?); END;";
         try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
           
            cstmt.setLong(1, npphone);
            cstmt.setLong(2, intSpecification);
            cstmt.setLong(3, nOrderid);
            cstmt.registerOutParameter(4, Types.VARCHAR);
            cstmt.registerOutParameter(5, Types.VARCHAR);
            cstmt.execute();
       
            strType_error = cstmt.getString(4); 
            strMessage    = cstmt.getString(5); 
            
            System.out.println(npphone +"strMessage ["+strMessage+"] strType_error ["+strType_error+"]");
            
         }
         catch(Exception e){
            e.printStackTrace();
             throw new Exception(e);         
         }
         finally{
           closeObjectsDatabase(conn, cstmt, null);
           //closeObjectsDatabase(conn, cstmt, rs);           
         }
         hshResult.put("strTypeError",strType_error);
         hshResult.put("strMessage",strMessage);     
         
         System.out.println("=========== Fin - getValidateByPhone ================");
         return hshResult;
      }

}