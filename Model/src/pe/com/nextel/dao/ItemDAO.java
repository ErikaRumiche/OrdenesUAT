package pe.com.nextel.dao;
/*
 La clase Order deberia contener un objeto list de ITEMS
  */
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import org.apache.commons.lang.StringUtils;

import pe.com.nextel.bean.BaAssignmentBean;
import pe.com.nextel.bean.ItemBean;
import pe.com.nextel.bean.ItemDeviceBean;
import pe.com.nextel.bean.ItemServiceBean;
import pe.com.nextel.bean.NpObjHeaderSpecgrp;
import pe.com.nextel.bean.NpObjItemSpecgrp;
import pe.com.nextel.bean.ServiciosBean;
import pe.com.nextel.bean.SpecificationBean;
import pe.com.nextel.ejb.SEJBRoamingBean;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;


/*jsalazar - modif hpptt # 1 - 29/09/2010 - inicio*/
/*jsalazar - modif hpptt # 1 - 29/09/2010 - fin*/
//import pe.com.nextel.util.DbmsOutput;
//import oracle.jdbc.OracleCallableStatement;



public class ItemDAO extends GenericDAO{
    private String strError;

  /**
    * Motivo: Registra el item de la orden
    * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
    * <br>Fecha: 11/09/2007
    * @param    ItemBean itemBean
    * @param    Connection conn
    */
  public String doSaveItem(ItemBean itemBean, Connection conn) throws Exception, SQLException {

    String strMessage = null;
    int    intResultTransaction = 0,
           intOrderId = 0,
           iException = 0;

    long   itemId = 0;
    logger.debug("[Input][itemBean]: " + itemBean.toString());
    OracleCallableStatement cstmt = null;
    OracleCallableStatement cstmt1 = null;
    ResultSet rs = null;
    long  Npcategoryid = 0;
    
    try{      
	
      //<!--START MSOTO: 15-07-2014 SAR N_O000027196 Portabilidad Prepago--> 
      String sqlStr = "SELECT npspecificationid FROM orders.np_order  WHERE nporderid = ?";
      cstmt1 = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt1.setLong(1, itemBean.getNporderid());
      rs = cstmt1.executeQuery();
      if (rs!=null &&  rs.next())
        Npcategoryid = rs.getLong("npspecificationid");
	
      String strSql = "BEGIN ORDERS.NP_ORDERS01_PKG.SP_INS_ITEM(    ?,    ?,    ?,     ?,    ?, " + //5   
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
                                                                "   ?,    ?,    ?,     ?,    ?,"  +  //85
                                                                "   ?,    ?,    ?,     ?,    ?, " +  //90 -- CPUENTE CAP & CAL
                                                                "   ?,    ?,    ?,     ?,    ?, " + //95
                                                                "   ?,    ?,    ?,     ?,    ?, " + //100   // MMONTOYA - Despacho en tienda. Se agregó 3 parámetros.
                                                                "   ?,    ?,    ?,     ?,    ?, " + //104 params // MMONTOYA - ADT-RCT-092 Roaming con corte. Se agregaron 5 parámetros
                                                                                                    //105 params // PRY-0721 DERAZO Se agrega un parametro mas npzonacoberturaid
							        "   ?,    ?,    ?,     ?,    ?," +                    //106 params //  Se agrega un  parametro PRY-0817
								                                          //108 params // PRY-0762 JQUISPE Se agrega dos parametros para Renta Adelantada
                                                                                                          //110 Se añade dos parametros mas EFLORES BAFI2  
                                                                "   ?, ?); END;";//111 PRY-0890  
                                                                                 // 112 DERAZO TDECONV003-2                     
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

        /*if( itemBean.getNpprice().equals("") )
          cstmt.setNull(13, OracleTypes.NUMBER);
        else
          cstmt.setDouble(13, MiUtil.parseDouble(itemBean.getNpprice()));*/ //rmartinez - 15-06-2009

        if( itemBean.getNpprice().equals("") ){
          if (itemBean.getNpcobro().equals("") )
            cstmt.setNull(13, OracleTypes.NUMBER);
          else
            cstmt.setDouble(13, MiUtil.parseDouble(itemBean.getNpcobro()));
        }else
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

        cstmt.setString(22, itemBean.getNpownimeinumber());
        cstmt.setString(23, itemBean.getNpinventorycode());
        cstmt.setString(24, itemBean.getNpequipmentreturn());
        cstmt.setString(25, itemBean.getNpequipmentnotyetgiveback());
        cstmt.setDate(26, itemBean.getNpequipmentreturndate());

        //Excepciones

        cstmt.setString(28, itemBean.getNpexceptionrevenue());
        cstmt.setDouble(29, itemBean.getNpexceptionrevenuediscount());
        cstmt.setString(30, itemBean.getNpexceptionrent());
        cstmt.setDouble(31, itemBean.getNpexceptionrentdiscount());
        cstmt.setString(32, itemBean.getNpexceptionminadidispatch());
        cstmt.setString(33, itemBean.getNpexceptionminaditelephony());
        cstmt.setString(27, itemBean.getNpexception());

        //Fin de Excepciones
        if( itemBean.getNpinstallationaddressid() == 0)
          cstmt.setNull(34, OracleTypes.NUMBER);
        else
          cstmt.setLong(34, itemBean.getNpinstallationaddressid());

        cstmt.setDate(35, itemBean.getNpmodificationdate());
        cstmt.setString(36, itemBean.getNpmodificationby());
        cstmt.setDate(37, itemBean.getNpcreateddate());
        cstmt.setString(38, itemBean.getNpcreatedby());
        cstmt.setLong(39, itemBean.getNpconceptid());
        cstmt.setString(40, itemBean.getNpplanname());

        /*Inicio - Banda Ancha*/
        if( itemBean.getNpsharedinstalationid() == 0)
          //cstmt.setLong(41, OracleTypes.NUMBER);
      cstmt.setNull(41, OracleTypes.NUMBER);
        else
          cstmt.setLong(41, itemBean.getNpsharedinstalationid());

        cstmt.setString(42, itemBean.getNpsharedinstal());

        if( itemBean.getNpcontractnumber() == 0)
          cstmt.setNull(43, OracleTypes.NUMBER);
        else
          cstmt.setInt(43, itemBean.getNpcontractnumber());

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

        cstmt.setString(47, itemBean.getNplinktype());
        cstmt.setDouble(48, itemBean.getNpnetworkhosttype());
        cstmt.setTimestamp(49, itemBean.getNpfeasibilityprogdate());
        //cstmt.setDate(49, itemBean.getNpfeasibilityprogdate());
        cstmt.setString(50, itemBean.getNpfeasibility());
        cstmt.setString(51, itemBean.getNpinstalation());
        cstmt.setTimestamp(52, itemBean.getNpinstalationprogdate());

        /*Fin - Banda Ancha*/

        if( itemBean.getNporiginalplanid() == 0)
          cstmt.setNull(53, OracleTypes.NUMBER);
        else
          cstmt.setLong(53, itemBean.getNporiginalplanid());

        cstmt.setString(54, itemBean.getNporiginalplanname());
		
		//<!--START MSOTO: 15-07-2014 SAR N_O000027196 Portabilidad Prepago--> //Se agrega cateogria prepago tde TDECONV029 
        if(Npcategoryid == 2002 || Npcategoryid == 2069 || Npcategoryid == Constante.SPEC_PREPAGO_TDE
        ||  Npcategoryid == Constante.SPEC_REPOSICION_PREPAGO_TDE )  {
          cstmt.setNull(55,OracleTypes.NUMBER);
        }
        else{
          cstmt.setString(55, itemBean.getNpmodel());
        }     
		
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
        //cstmt.setString(71,itemBean.getNporiginalprice());
        cstmt.setDouble(71, MiUtil.parseDouble(itemBean.getNporiginalprice()));
        cstmt.setString(72,itemBean.getNppricetype());
        cstmt.setLong(73,itemBean.getNppricetypeid());
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
        cstmt.setInt(81,MiUtil.parseInt(itemBean.getNpestadoitemId()));
        /**Suspensiones Definitivas**/
        cstmt.setInt(82,itemBean.getNptypeip());

        if (itemBean.getNpcontractref() == 0)
          cstmt.setNull(83, OracleTypes.NUMBER);
        else
          cstmt.setLong(83, itemBean.getNpcontractref());
        /**CBARZOLA:Cambio de Modelo - Distintas Tecnologias(inicio)**/
        if(itemBean.getNporiginalsolutionid()==0)
          cstmt.setNull(84,OracleTypes.NUMBER);
        else
          cstmt.setLong(84,itemBean.getNporiginalsolutionid());
        /**Cambio de Modelo - Distintas Tecnologias(inicio)**/
        /*Valores de Salida*/
        
		//CPUENTE CAP & CAL - INICIO
        cstmt.setInt(85,itemBean.getNpequipmenttime());
           
        cstmt.setString(86,itemBean.getNpreasonchange());
        cstmt.setInt(87,itemBean.getNpownequipment());
        //CPUENTE CAP & CAL - FIN
        //PHIDALGO   
        /**Cambio Bolsa insertar nivel de orden*/
        if(itemBean.getNpLevel()==0){
          cstmt.setNull(88,OracleTypes.NUMBER);
        }else{
          cstmt.setInt(88,itemBean.getNpLevel());
        }
        
        cstmt.setDate(89, itemBean.getNpactivationdate());
        cstmt.setDate(90, itemBean.getNpdeactivationdate());
        cstmt.setString(91,itemBean.getNpimeicustomer());
        cstmt.setString(92,itemBean.getNpownimeisim());
        
         if(itemBean.getNpVepItem()!= 0) {
            cstmt.setInt(93,itemBean.getNpVepItem());
         } else {
            cstmt.setInt(93, OracleTypes.NULL);
         }
         
         if(itemBean.getNpVepTotalPrice()!=0) {
            cstmt.setDouble(94,itemBean.getNpVepTotalPrice());
         } else {
            cstmt.setDouble(94, OracleTypes.NULL);
         }
         
        // MMONTOYA - Despacho en tienda
        cstmt.setString(95, itemBean.getNpproductstatus());
        cstmt.setString(96, itemBean.getNpchanged()); 
        cstmt.setString(97, itemBean.getNpflagaccessory());
        
        // ADT-RCT-092 Roaming con corte
        if (itemBean.getNpservplantype()!= 0) {
            cstmt.setInt(98, itemBean.getNpservplantype());
        } else {
            cstmt.setInt(98, OracleTypes.NULL);
        }

        // ADT-RCT-092 Roaming con corte
        cstmt.setString(99, itemBean.getNpservbagcode());
        cstmt.setString(100, itemBean.getNpservbagtype());
        cstmt.setDate(101, itemBean.getNpservvalidactivationdate());
        cstmt.setInt(102, itemBean.getNpservvalidity());
                
        //PRY-0721 DERAZO Se agrega campo npzonacoberturaid
        if(itemBean.getNpzonacoberturaid() == 0)
            cstmt.setNull(103, OracleTypes.NUMBER);
        else
            cstmt.setDouble(103, itemBean.getNpzonacoberturaid());
        
        //PRY-0762 JQUISPE Se agrega campo npcantidadRentaAdelantada
        cstmt.setObject(104, itemBean.getNpcantidadRentaAdelantada(), OracleTypes.NUMBER);
        
        //PRY-0762 JQUISPE Se agrega campo nptotalRentaAdelantada
        if( itemBean.getNptotalRentaAdelantada().equals("") ){
            cstmt.setNull(105, OracleTypes.NUMBER);
        }else{
            cstmt.setBigDecimal(105, MiUtil.getBigDecimal(itemBean.getNptotalRentaAdelantada()));
        }
        
          //CDM+CDP EFLORES Se agrega nuevo campo npkeepsim
       if(itemBean.getNpkeepSIM() == 0){
         cstmt.setNull(106,OracleTypes.NUMBER);
       }else{
         cstmt.setInt(106,itemBean.getNpkeepSIM());
       }
        
       //EFLORES BAFI2
      if(itemBean.getNpProvinceZoneId() == 0)
        cstmt.setNull(107,OracleTypes.NUMBER);
      else
        cstmt.setDouble(107,itemBean.getNpProvinceZoneId());

      //EFLORES BAFI2
      if(itemBean.getNpDistrictZoneId() == 0)
        cstmt.setNull(108,OracleTypes.NUMBER);
      else
        cstmt.setDouble(108,itemBean.getNpDistrictZoneId());
	   

      //JBALCAZAR
      if(itemBean.getNpprorrateoPrice()==null || itemBean.getNpprorrateoPrice().equals("")){
    	  cstmt.setNull(109, OracleTypes.NUMBER);
      }else{
    	  cstmt.setBigDecimal(109,MiUtil.getBigDecimal(itemBean.getNpprorrateoPrice()));  
      }
      
      
      /** JCURI
       * PARA NO AFECTAR LA FUNCIONALIDAD DE RENTA ADELANTADA Y 
       * OTRAS FUNCIONALIDADES, VALIDAR LOS ATRIBUTOS DE NULL Y VACIO 
       * EJM: itemBean.getNpprorrateoPrice()==null || itemBean.getNpprorrateoPrice().equals("")
       * */      
      
//DERAZO TDECONV003-2 Se setea Imei FS
        cstmt.setString(110, itemBean.getNpOwnImeiFS());

        cstmt.registerOutParameter(111, OracleTypes.NUMBER);
        cstmt.registerOutParameter(112, OracleTypes.VARCHAR);
        
        logger.debug(itemBean);

        cstmt.executeUpdate();

        //dbmsOutput.show();
        //dbmsOutput.close();
        
        strMessage = cstmt.getString(112);
        if(strMessage!=null) return strMessage;
        
        itemId     = MiUtil.parseLong(""+cstmt.getLong(111));
        itemBean.setNpitemid(itemId);

    }catch (Exception e) {
      e.printStackTrace();
      logger.error(formatException(e));
      strMessage = e.getClass() + " - " + e.getMessage();
    }finally{
      try{
        closeObjectsDatabase(null,cstmt,rs);
		//<!--START MSOTO: 15-07-2014 SAR N_O000027196 Portabilidad Prepago--> 
		closeObjectsDatabase(null,cstmt1,null); 		
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }

    return strMessage;
  }

  /**
    * Motivo: Actualiza el item de la orden
    * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
    * <br>Fecha: 11/09/2007
    * @param    ItemBean itemBean
    * @param    Connection conn
    */
  public String doUpdateItem(ItemBean itemBean,Connection conn) throws Exception, SQLException {
      System.out.println("MMONTOYA -> doUpdateItem");

    OracleCallableStatement cstmt = null;

    String strMessage = null;
    int    intResultTransaction = 0,
           intOrderId = 0;

    long   itemId = 0;
    logger.debug("[Input][itemBean]: " + itemBean.toString());
    try{
      String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_UPD_ITEM(    ?,    ?,    ?,     ?,    ?, " + //5
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
                                                                "   ?,    ?,    ?,     ?,    ?, " + //75
                                                                "   ?,    ?,    ?,     ?,    ?, " + //80
                                                                "   ?,    ?,    ?,     ?,    ?, " + //85
                                                                "   ?,    ?,    ?,     ?,    ?, " + //90
                                                                "   ?,    ?,    ?,     ?,    ?, " + //95
                                                                "   ?,    ?,    ?,     ?,    ?, " + //100
                                                                "   ?,    ?,    ?,     ?,    ?, " + //101 // MMONTOYA - Despacho en tienda. Se agregó 3 parámetros. ADT-RCT-092 Roaming con corte. Se agregaron 5 parámetros.
                                                                                                    //102 // PRY-0721 DERAZO Se agrega un parametro mas npzonacoberturaid, Se agrega un paramatero para Mantener SIM PRY-0817
								                                    //105 // PRY-0762 JQUISPE Se agrega dos parametros para Renta Adelantada.
                                                                "   ?,    ?,    ?,     ?); END;";                //107 Se agregan dos parametros mas EFLORES BAFI12 //105 // PRY-0762 JQUISPE Se agrega dos parametros para Renta Adelantada.//108 PRY 0890 //109 DERAZO TDECONV003-2


        logger.info("[ORDERS.NP_ORDERS06_PKG.SP_UPD_ITEM]");
        cstmt = (OracleCallableStatement)conn.prepareCall(strSql);

        cstmt.setLong(1, itemBean.getNporderid());
        cstmt.setLong(2, itemBean.getNpsolutionid());
        cstmt.setString(3, itemBean.getNpmodalitysell());
        cstmt.setString(4, itemBean.getNpequipment());
        cstmt.setString(5, itemBean.getNpphone());
        cstmt.setString(6, itemBean.getNpserialnumber());
        cstmt.setString(7, itemBean.getNpimeinumber());
        cstmt.setString(8, itemBean.getNpsiminumber());

        //Plan Tarifario
        if( itemBean.getNpplanid() == 0 )
          cstmt.setNull(9, OracleTypes.NUMBER);
        else
          cstmt.setLong(9, itemBean.getNpplanid());

        //Linea de Producto
        if( itemBean.getNpproductlineid() == 0 )
          cstmt.setNull(10, OracleTypes.NUMBER);
        else
          cstmt.setLong(10, itemBean.getNpproductlineid());

        //Producto
        if( itemBean.getNpproductid() == 0 )
          cstmt.setNull(11, OracleTypes.NUMBER);
        else
          cstmt.setLong(11, itemBean.getNpproductid());

        //Cantidad
        cstmt.setInt(12, itemBean.getNpquantity());

        //Precio Cta/Inscripción
        /*if( itemBean.getNpprice().equals("") )
          cstmt.setNull(13, OracleTypes.NUMBER);
        else
          cstmt.setDouble(13, MiUtil.parseDouble(itemBean.getNpprice()));*/ //rmartinez - 15-06-2009

        if( itemBean.getNpprice().equals("") )
          if (itemBean.getNpcobro().equals("") )
            cstmt.setNull(13, OracleTypes.NUMBER);
          else
            cstmt.setDouble(13, MiUtil.parseDouble(itemBean.getNpcobro()));
        else
          cstmt.setDouble(13, MiUtil.parseDouble(itemBean.getNpprice()));

        //Precio Excepción
        if( itemBean.getNppriceexception().equals("") )
          cstmt.setNull(14, OracleTypes.NUMBER);
        else
          cstmt.setDouble(14, MiUtil.parseDouble(itemBean.getNppriceexception()));

        //Renta
        if( itemBean.getNprent().equals("") )
          cstmt.setNull(15, OracleTypes.NUMBER);
        else
          cstmt.setDouble(15, MiUtil.parseDouble(itemBean.getNprent()));

        //Descuento
        if( itemBean.getNpdiscount().equals("") )
          cstmt.setNull(16, OracleTypes.NUMBER);
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

        cstmt.setString(22, itemBean.getNpownimeinumber());
        cstmt.setString(23, itemBean.getNpinventorycode());
        cstmt.setString(24, itemBean.getNpequipmentreturn());
        cstmt.setString(25, itemBean.getNpequipmentnotyetgiveback());
        cstmt.setDate(26, itemBean.getNpequipmentreturndate());
        cstmt.setString(27, itemBean.getNpexception());
        cstmt.setString(28, itemBean.getNpexceptionrevenue());
        cstmt.setDouble(29, itemBean.getNpexceptionrevenuediscount());
        cstmt.setString(30, itemBean.getNpexceptionrent());
        cstmt.setDouble(31, itemBean.getNpexceptionrentdiscount());
        cstmt.setString(32, itemBean.getNpexceptionminadidispatch());
        cstmt.setString(33, itemBean.getNpexceptionminaditelephony());

        //Dirección de Instalación
        if( itemBean.getNpinstallationaddressid() == 0)
          cstmt.setNull(34, OracleTypes.NUMBER);
        else
          cstmt.setLong(34, itemBean.getNpinstallationaddressid());

        cstmt.setDate(35, itemBean.getNpmodificationdate());
        cstmt.setString(36, itemBean.getNpmodificationby());
        cstmt.setDate(37, itemBean.getNpcreateddate());
        cstmt.setString(38, itemBean.getNpcreatedby());
        cstmt.setLong(39, itemBean.getNpconceptid());
        cstmt.setString(40, itemBean.getNpplanname());
        cstmt.setString(41, itemBean.getNpmodel());
        cstmt.setString(42,itemBean.getNpnewphone());
        cstmt.setLong(43,itemBean.getNparearespdev());
        cstmt.setLong(44,itemBean.getNpprovidergrpiddev());

        //Producto Original
        if( itemBean.getNporiginalproductid() == 0 )
          cstmt.setNull(45,OracleTypes.NUMBER);
        else
          cstmt.setLong(45,itemBean.getNporiginalproductid());

        cstmt.setString(46,itemBean.getNpminutesrate());

        /**Inicio - Banda Ancha**/
        if( itemBean.getNpsharedinstalationid() == 0 )
          cstmt.setNull(47, OracleTypes.NUMBER);
        else
          cstmt.setLong(47, itemBean.getNpsharedinstalationid());

        cstmt.setString(48, itemBean.getNpsharedinstal());

        if( itemBean.getNpcontractnumber() == 0 )
          cstmt.setNull(49, OracleTypes.NUMBER);
        else
          cstmt.setInt(49, itemBean.getNpcontractnumber());

        cstmt.setString(50, itemBean.getNpfirstcontract());

        //Precio de Instalación
        if( itemBean.getNpinstalationprice().equals("") )
          cstmt.setNull(51, OracleTypes.NUMBER);
        else
          cstmt.setDouble(51, MiUtil.parseDouble(itemBean.getNpinstalationprice()));

        //Precio de Instalación de Excepción
        if( itemBean.getNpinstalationexception().equals("") )
          cstmt.setNull(52, OracleTypes.NUMBER);
        else
          cstmt.setDouble(52, MiUtil.parseDouble(itemBean.getNpinstalationexception()));

        cstmt.setString(53, itemBean.getNplinktype());
        cstmt.setDouble(54, itemBean.getNpnetworkhosttype());

        if( itemBean.getNpfeasibilityprogdate() == null )
          cstmt.setNull(55, Types.VARCHAR );
        else
          cstmt.setTimestamp(55, itemBean.getNpfeasibilityprogdate());

        if( MiUtil.getString(itemBean.getNpfeasibility()).equals("") )
          cstmt.setNull(56, OracleTypes.NUMBER);
        else
          cstmt.setString(56, itemBean.getNpfeasibility());

        if( MiUtil.getString(itemBean.getNpinstalation()).equals("") )
          cstmt.setNull(57, OracleTypes.NUMBER);
        else
          cstmt.setString(57, itemBean.getNpinstalation());

        if( itemBean.getNpinstalationprogdate() == null )
          cstmt.setNull(58, Types.VARCHAR );
        else
          cstmt.setTimestamp(58, itemBean.getNpinstalationprogdate());
        /**Fin - Banda Ancha**/

        /**Traslado (Inicio) - Banda Ancha**/
        if( itemBean.getNpnewaddress() == 0 )
          cstmt.setNull(59,OracleTypes.NUMBER);
        else
          cstmt.setLong(59,itemBean.getNpnewaddress());

        cstmt.setString(60,itemBean.getNpcontactname());

        cstmt.setString(61,itemBean.getNpphonenumber1());
        cstmt.setString(62,itemBean.getNpphonenumber2());

        if( itemBean.getNpaditionalcost().equals("") )
          cstmt.setNull(63,OracleTypes.NUMBER);
        else
          cstmt.setDouble(63,MiUtil.parseDouble(itemBean.getNpaditionalcost()));

        cstmt.setString(64,itemBean.getNpdescription());
        /**Traslado (Fin) - Banda Ancha**/

        /**Cambio de Plan Tarifario (Inicio) - Banda Ancha**/
        if( itemBean.getNporigmainservice() == 0 )
          cstmt.setNull(65,OracleTypes.NUMBER);
        else
          cstmt.setLong(65,itemBean.getNporigmainservice());

        if( itemBean.getNpnewmainservice() == 0 )
          cstmt.setNull(66,OracleTypes.NUMBER);
        else
          cstmt.setLong(66,itemBean.getNpnewmainservice());

        /**Cambio de Plan Tarifario (Fin) - Banda Ancha**/

        cstmt.setDate(67,itemBean.getNpendservicedate());
        cstmt.setString(68,itemBean.getNpreferencephone());

        cstmt.setLong(69,itemBean.getNpitemid());

        /**Acuerdos Comerciales**/
        cstmt.setDouble(70,MiUtil.parseDouble(itemBean.getNporiginalprice()));
        cstmt.setString(71,itemBean.getNppricetype());
        cstmt.setLong(72,itemBean.getNppricetypeid());
        cstmt.setLong(73,itemBean.getNppricetypeitemid());
        /**Acuerdos Comerciales**/

        cstmt.setString(74,itemBean.getNpfixedphone());
        cstmt.setString(75,itemBean.getNplocution());

        /*Incio Responsable de Pago*/
        if (itemBean.getNpsiteid() == 0 )
          cstmt.setNull(76, OracleTypes.NUMBER);
        else
          cstmt.setLong(76,itemBean.getNpsiteid());
        /*Fin Responsable de Pago*/

        /*Inicio Contratos de Referencia*/
        if (itemBean.getNpinternetrefcontract() == 0)
          cstmt.setNull(77, OracleTypes.NUMBER);
        else
          cstmt.setLong(77,itemBean.getNpinternetrefcontract());
        if (itemBean.getNpdatosrefcontract() == 0)
          cstmt.setNull(78, OracleTypes.NUMBER);
        else
          cstmt.setLong(78,itemBean.getNpdatosrefcontract());
        cstmt.setString(79,itemBean.getNptfrefphonenumber());
        /*Fin Contratos de Referencia*/

        /*Suspensiones Definitivas*/
        cstmt.setInt(80, MiUtil.parseInt(itemBean.getNpestadoitemId()));
        cstmt.setInt(81, itemBean.getNptypeip());

        if ( itemBean.getNpcontractref() == 0 )
          cstmt.setNull(82, OracleTypes.NUMBER);
        else
          cstmt.setLong(82, itemBean.getNpcontractref());
       /**CBARZOLA:Cambio de Modelo - Distintas Tecnologias*/
        if(itemBean.getNporiginalsolutionid()==0)
          cstmt.setNull(83,OracleTypes.NUMBER);
        else
          cstmt.setLong(83,itemBean.getNporiginalsolutionid());
        /**Cambio de Modelo - Distintas Tecnologias*/
		/**Cambio Bolsa insertar nivel de orden*/
        if(itemBean.getNpLevel()==0){
          cstmt.setNull(84,OracleTypes.NUMBER);
        }else{
          cstmt.setInt(84,itemBean.getNpLevel());
        }
        
        cstmt.setString(85,itemBean.getNpimeicustomer());
        cstmt.setString(86,itemBean.getNpownimeisim());
        
        //DLAZO - SUSCRIPCIONES
        cstmt.setDate(87, itemBean.getNpactivationdate());
        cstmt.setDate(88, itemBean.getNpdeactivationdate());
        cstmt.setString(89,itemBean.getNpreasonchange());
        cstmt.setInt(90,itemBean.getNpownequipment());
        
        if(itemBean.getNpVepItem()!= 0) {
            cstmt.setInt(91,itemBean.getNpVepItem());
         } else {
            cstmt.setInt(91, OracleTypes.NULL);
         }
         
         if(itemBean.getNpVepTotalPrice()!=0) {
            cstmt.setDouble(92,itemBean.getNpVepTotalPrice());
         } else {
            cstmt.setDouble(92, OracleTypes.NULL);
         }

        // MMONTOYA - Despacho en tienda
        cstmt.setString(93, itemBean.getNpproductstatus());
        cstmt.setString(94, itemBean.getNpchanged()); 
        cstmt.setString(95, itemBean.getNpflagaccessory()); 
                
        // ADT-RCT-092 Roaming con corte
        if (itemBean.getNpservplantype()!= 0) {
            cstmt.setInt(96, itemBean.getNpservplantype());
        } else {
            cstmt.setInt(96, OracleTypes.NULL);
        }

        // ADT-RCT-092 Roaming con corte
        cstmt.setString(97, itemBean.getNpservbagcode());
        cstmt.setString(98, itemBean.getNpservbagtype());
        cstmt.setDate(99, itemBean.getNpservvalidactivationdate());
        cstmt.setInt(100, itemBean.getNpservvalidity());

        //PRY-0721 DERAZO Se agrega campo npzonacoberturaid
        cstmt.setInt(101, itemBean.getNpzonacoberturaid());

        //PRY-0762 JQUISPE Se agrega campo npcantidadRentaAdelantada
        cstmt.setObject(102, itemBean.getNpcantidadRentaAdelantada(), OracleTypes.NUMBER);
        
        //PRY-0762 JQUISPE Se agrega campo nptotalRentaAdelantada
        if( itemBean.getNptotalRentaAdelantada().equals("") ){
            cstmt.setNull(103, OracleTypes.NUMBER);
        }else{
            cstmt.setBigDecimal(103, MiUtil.getBigDecimal(itemBean.getNptotalRentaAdelantada()));
        }

       if( itemBean.getNpkeepSIM() == 0 ) {
          cstmt.setNull(104, OracleTypes.NUMBER);
        }else{
          cstmt.setInt(104, itemBean.getNpkeepSIM());
        }
        
        //EFLORES BAFI2
        cstmt.setInt(105,itemBean.getNpProvinceZoneId());
        cstmt.setInt(106,itemBean.getNpDistrictZoneId());

        //PRY-0890 
        if(itemBean.getNpprorrateoPrice()==null || itemBean.getNpprorrateoPrice().equals("") ){
            cstmt.setNull(107, OracleTypes.NUMBER);
        }else{
            cstmt.setBigDecimal(107, MiUtil.getBigDecimal(itemBean.getNpprorrateoPrice()));
        }        

        //DERAZO TDECONV003-2
        cstmt.setString(108, itemBean.getNpOwnImeiFS());

        cstmt.registerOutParameter(109, OracleTypes.VARCHAR);

        cstmt.executeUpdate();
        //dbmsOutput.show();
        //dbmsOutput.close();

        strMessage = cstmt.getString(109);
        logger.info("[Output][strMessage]: " + strMessage);
      }catch (Exception e) {
          e.printStackTrace();
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
     Method : getItemImeiAssignementBADelete
     Purpose: Inserta el item de la orden.
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Lee Rosales     11/09/2007  Creación
     */

    public String getItemImeiAssignementBADelete(ItemBean itemDeviceBean,Connection conn) throws Exception,SQLException {

      String strMensaje = null;
      int    intResultTransaction = 0;
      long   itemId = 0;
      //Connection conn = null;
      OracleCallableStatement cstmt = null;
      int intOrderId = 0;
      logger.debug("[Input][itemDeviceBean]: " + itemDeviceBean.toString());
      
      try{
          String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_DEL_ASSIGNMENT_BA(    ?,    ?,    ? ); END;";
          logger.info("ORDERS.NP_ORDERS06_PKG.SP_DEL_ASSIGNMENT_BA");
          cstmt = (OracleCallableStatement)conn.prepareCall(strSql);

          cstmt.setLong(1, itemDeviceBean.getNpitemid());
          cstmt.setLong(2, itemDeviceBean.getNporderid());

          cstmt.registerOutParameter(3, Types.VARCHAR);
          intResultTransaction  =   cstmt.executeUpdate();
          strMensaje = cstmt.getString(3);
          logger.info("[Output][strMensaje]: " + strMensaje);

          System.out.println("intResultTransaction : " + intResultTransaction);
          System.out.println("strMensaje : " + strMensaje);

          if( strMensaje!=null){
            return Constante.NPERROR + strMensaje;
          }else{
            if( intResultTransaction > 0 )
            return null;
            else
            return Constante.NPERROR + "[No se eliminarón los items]";
          }
      }catch (Exception e) {
        logger.error(formatException(e));
         return Constante.NPERROR + e.getMessage();
      }finally{
        try{
           closeObjectsDatabase(null,cstmt,null);
        }catch (Exception e) {
          logger.error(formatException(e));
        }
      }

    }

    /**
     Method : delItemServiceAditional
     Purpose: Inserta el item de la orden.
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Lee Rosales     11/09/2007  Creación
     */

    public String delItemServiceAditional(ItemBean itemDeviceBean,Connection conn) throws Exception,SQLException {

      String strMensaje = null;
      int    intResultTransaction = 0;
      long   itemId = 0;
      //Connection conn = null;
      OracleCallableStatement cstmt = null;
      int intOrderId = 0;
      logger.debug("[Input][itemDeviceBean]: " + itemDeviceBean.toString());
      
      try{
        String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_DEL_SERVICE_ADITIONAL(    ?,    ?,    ? ); END;";

        cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
        cstmt.setLong(1, itemDeviceBean.getNpitemid());
        cstmt.setLong(2, itemDeviceBean.getNporderid());
        cstmt.registerOutParameter(3, Types.VARCHAR);
        intResultTransaction  =   cstmt.executeUpdate();
        strMensaje = cstmt.getString(3);
        logger.info("[Output][strMensaje]: " + strMensaje);
        
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
     Method : getItemImeiOrderDelete
     Purpose: Inserta el item de la orden.
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Lee Rosales     11/09/2007  Creación
     */

    public String delItemImeiDelete(ItemBean itemDeviceBean,Connection conn) throws Exception,SQLException {

      String strMensaje = null;
      int    intResultTransaction = 0;
      long   itemId = 0;
      //Connection conn = null;
      OracleCallableStatement cstmt = null;
      int intOrderId = 0;
      logger.debug("[Input][itemDeviceBean]: " + itemDeviceBean.toString());
      
      try{
        String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_DEL_ORDER_ITEM_IMEI(    ?,    ?,    ? ); END;";

        cstmt = (OracleCallableStatement)conn.prepareCall(strSql);

        cstmt.setLong(1, itemDeviceBean.getNpitemid());
        cstmt.setLong(2, itemDeviceBean.getNporderid());

        cstmt.registerOutParameter(3, Types.VARCHAR);

        intResultTransaction  =   cstmt.executeUpdate();
        strMensaje = cstmt.getString(3);
        logger.info("[Output][strMensaje]: " + strMensaje);
        
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
     Method : getItemDeviceOrder
     Purpose: Obtiene el Header y el Value de los objetos ITEM.
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Lee Rosales     11/09/2007  Creación
     Fanny Najarro   10/06/2009  Modificación para Automatización.
     */
    public ArrayList getItemDeviceOrder(long intOrderId)  throws SQLException {

        Connection conn = null;
        ResultSet rs=null;
        OracleCallableStatement cstm = null;
        ArrayList list  = null;
        ItemDeviceBean itemDeviceBean = null;
        String strMensaje = null;
        list = new ArrayList();
        try{
            String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_ITEM_DEVICE_ORDER(?,?,?); END;";
            conn = Proveedor.getConnection();
            cstm = (OracleCallableStatement) conn.prepareCall(strSql);

            cstm.setLong(1,intOrderId);
            cstm.registerOutParameter(2, OracleTypes.CURSOR);
            cstm.registerOutParameter(3, Types.CHAR);

            cstm.execute();
            strMensaje  = cstm.getString(3);
            if  ( strMensaje == null ){
              rs = (ResultSet)cstm.getObject(2);
              while(rs.next()){
                itemDeviceBean = new ItemDeviceBean();
                itemDeviceBean.setNpitemdeviceid(rs.getLong("npitemdeviceid"));
                itemDeviceBean.setNpitemid(rs.getInt("npitemid"));
                itemDeviceBean.setNporderid(rs.getInt("nporderid"));
                itemDeviceBean.setNpserialnumber(rs.getString("npserialnumber"));
                itemDeviceBean.setNpimeinumber(rs.getString("npimeinumber"));
                itemDeviceBean.setNpsimnumber(rs.getString("npsimnumber"));
                itemDeviceBean.setNpcheckimei(rs.getString("npcheckimei"));
                itemDeviceBean.setNpbadimei(rs.getString("npbadimei"));
                itemDeviceBean.setNpcontractnumber(rs.getDouble("npcontractnumber"));
                itemDeviceBean.setNpcontractstatus(rs.getString("npcontractstatus"));
                itemDeviceBean.setNpcreateddate(rs.getDate("npcreateddate"));
                itemDeviceBean.setNpcreatedby(rs.getString("npcreatedby"));
                itemDeviceBean.setNpcantidad(rs.getInt("cantidad"));
                itemDeviceBean.setNpplanname(rs.getString("npplanname"));
                itemDeviceBean.setNpproductid(rs.getLong("npproductid"));
                itemDeviceBean.setNpproductname(rs.getString("npproductname"));
                itemDeviceBean.setNpmodality(rs.getString("npmodality"));
                itemDeviceBean.setNpphone(rs.getString("npphone"));
                itemDeviceBean.setNprequestid(rs.getLong("nprequestid"));
                itemDeviceBean.setNperrorautom(rs.getString("nperrorautom"));
                itemDeviceBean.setNpwarrant(rs.getString("npwarrant")==null?"":rs.getString("npwarrant"));
                list.add(itemDeviceBean);
              }
            }
             logger.info("DAO:Campo List.size->"+list.size());
             String phone=null;
             String error=null;
             for( int i=0; i<list.size(); i++){
                  itemDeviceBean = (ItemDeviceBean)list.get(i);
                  phone = itemDeviceBean.getNpphone();
                  error = itemDeviceBean.getNperrorautom();
             }
             logger.info("Item_DAO:Campo Phone->"+phone);
             logger.info("Item_DAO:Campo Error->"+error);
        }catch (Exception e) {
          logger.error(formatException(e));

        }finally{
          try{
             closeObjectsDatabase(conn,cstm,rs);
          }catch (Exception e) {
            logger.error(formatException(e));
          }
        }
          return list;
    }

    /**
     Method : getItemInsertDevices
     Purpose: Inserta el item de la orden.
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Lee Rosales     11/09/2007  Creación
     */

    public String getItemInsertDevices(ItemDeviceBean itemDeviceBean,String strNextInbox ,Connection conn) throws Exception, SQLException {

      String strMessage = null;
      int    intResultTransaction = 0;
      long   itemId = 0;
      //Connection conn = null;
      OracleCallableStatement cstmt = null;
      int intOrderId = 0;
      logger.debug("[Input][itemDeviceBean]: " + itemDeviceBean.toString());
      logger.debug("[Input][strNextInbox]: " + strNextInbox);
      
      try{
          String strSql = "BEGIN ORDERS.NP_ORDERS08_PKG.SP_INS_VALIDATE_IMEIS(   ?,    ?,    ?,     ?,    ?, " + //5
                                                                         "   ?,    ?,    ?,     ?,    ?, " + //10
                                                                         "   ?,    ?,    ?,     ?,    ?,   ?, ?, ?,?); END;";   //19
          
          logger.info("[ORDERS.NP_ORDERS08_PKG.SP_INS_VALIDATE_IMEIS]");
          //BUSCAR ERROR
            System.out.println("[ItemDAO] getItemInsertDevices : getNpitemid -->" +itemDeviceBean.getNpitemid());
            System.out.println("[ItemDAO] getItemInsertDevices : getNporderid -->" +itemDeviceBean.getNporderid());
            System.out.println("[ItemDAO] getItemInsertDevices : getNpproductid -->" +itemDeviceBean.getNpproductid());
            System.out.println("[ItemDAO] getItemInsertDevices : getNpserialnumber -->" +itemDeviceBean.getNpserialnumber());
            System.out.println("[ItemDAO] getItemInsertDevices : getNpimeinumber -->" +itemDeviceBean.getNpimeinumber());
            System.out.println("[ItemDAO] getItemInsertDevices : getNpsimnumber -->" +itemDeviceBean.getNpsimnumber());
            System.out.println("[ItemDAO] getItemInsertDevices : getNpcheckimei -->" +itemDeviceBean.getNpcheckimei());
            System.out.println("[ItemDAO] getItemInsertDevices : getNpbadimei -->" +itemDeviceBean.getNpbadimei());
            System.out.println("[ItemDAO] getItemInsertDevices : getNpinventorycode -->" +itemDeviceBean.getNpinventorycode());
            System.out.println("[ItemDAO] getItemInsertDevices : getNpcontractnumber -->" +itemDeviceBean.getNpcontractnumber());
            System.out.println("[ItemDAO] getItemInsertDevices : getNpcontractstatus -->" +itemDeviceBean.getNpcontractstatus());
            System.out.println("[ItemDAO] getItemInsertDevices : getNpcreateddate -->" +itemDeviceBean.getNpcreateddate());
            System.out.println("[ItemDAO] getItemInsertDevices : getNpcreatedby -->" +itemDeviceBean.getNpcreatedby());
            System.out.println("[ItemDAO] getItemInsertDevices : strNextInbox -->" +strNextInbox);
            System.out.println("[ItemDAO] getItemInsertDevices : getStrImeiChange -->" +itemDeviceBean.getStrImeiChange());
            System.out.println("[ItemDAO] getItemInsertDevices : getStrSimChange -->" +itemDeviceBean.getStrSimChange());
            System.out.println("[ItemDAO] getItemInsertDevices : getNpphone -->" +itemDeviceBean.getNpphone());
            
          cstmt = (OracleCallableStatement)conn.prepareCall(strSql);

          cstmt.setLong(1, itemDeviceBean.getNpitemid());
          cstmt.setLong(2, itemDeviceBean.getNporderid());
          cstmt.setLong(3, itemDeviceBean.getNpproductid());
          cstmt.setString(4, itemDeviceBean.getNpserialnumber());
          cstmt.setString(5, itemDeviceBean.getNpimeinumber());
          cstmt.setString(6, itemDeviceBean.getNpsimnumber());
          cstmt.setString(7, itemDeviceBean.getNpcheckimei());
          cstmt.setString(8, itemDeviceBean.getNpbadimei());
          cstmt.setString(9, itemDeviceBean.getNpinventorycode());
          cstmt.setDouble(10, itemDeviceBean.getNpcontractnumber());
          cstmt.setString(11, itemDeviceBean.getNpcontractstatus());
          cstmt.setDate(12, itemDeviceBean.getNpcreateddate());
          cstmt.setString(13, itemDeviceBean.getNpcreatedby());
          cstmt.setString(14, strNextInbox);
          cstmt.setString(15,  itemDeviceBean.getStrImeiChange() );
          cstmt.setString(16,  itemDeviceBean.getStrSimChange() );
          cstmt.setString(17,  itemDeviceBean.getNpphone() );
          cstmt.setLong(18, itemDeviceBean.getNprequestid());
          
          cstmt.registerOutParameter(19, Types.VARCHAR);
          
          intResultTransaction  =   cstmt.executeUpdate();

          strMessage = cstmt.getString(19);
          
          logger.info("[Output][strMessage]: " + strMessage);
          
      }catch (Exception e) {
          logger.error(formatException(e));
          strMessage = e.getMessage();
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
     Method : getItemOrderDelete
     Purpose: Inserta el item de la orden.
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Lee Rosales     11/09/2007  Creación
     */

    public String getItemOrderDelete(ItemBean itemBean, Connection conn) throws Exception,SQLException {

      String strMensaje = null;
      int    intResultTransaction = 0;
      long   itemId = 0;
      //Connection conn = null;
      OracleCallableStatement cstmt = null;
      int intOrderId = 0;
      logger.debug("[Input][itemBean]: " + itemBean);
      
      try{
        String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_DEL_ITEM(    ?,    ?,    ? ); END;";
        
        //BUSCAR ERROR
          logger.info("[Package]: " + strSql);
          System.out.println("[ItemDAO] getItemOrderDelete : getNpitemid -->" +itemBean.getNpitemid());
          System.out.println("[ItemDAO] getItemOrderDelete : getNporderid -->" +itemBean.getNporderid());
          
          cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
          cstmt.setLong(1, itemBean.getNpitemid());
          cstmt.setLong(2, itemBean.getNporderid());
          cstmt.registerOutParameter(3, Types.VARCHAR);
          intResultTransaction  =   cstmt.executeUpdate();
          strMensaje = cstmt.getString(3);
          
          System.out.println("intResultTransaction : " + intResultTransaction);
         // System.out.println("strMensaje : " + strMensaje);
          logger.info("[Output][strMensaje]: " + strMensaje);

          if( strMensaje!=null){
            return Constante.NPERROR + strMensaje;
          }else{
            if( intResultTransaction > 0 )
              return null;
            else
              return Constante.NPERROR + "[No se eliminarón los items]";
          }
      }catch (Exception e) {
          logger.error(formatException(e));
          return Constante.NPERROR + e.getMessage();
      }finally{
        try{
           closeObjectsDatabase(null,cstmt,null);
         }catch (Exception e) {
           logger.error(formatException(e));
         }
      }

    }


  /**
   Method : getItemOrder
   Purpose: Obtiene los Items asociados a una órden
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Lee Rosales     11/09/2007  Creación
   Jorge Pérez     10/11/2007  Se agrega campo para los servicios gratuitos
   Lee Rosales     14/01/2008  Se agregaron los campos para Banda Ancha
   Karen Salvador  24/04/2009  Se agrega el campo de solución
   Karen Salvador  14/07/2009  Se agrega el campo del ip para la automatización
   Cesar Barzola   16/07/2009  Se agrego el campo solucionoriginal
   Frank Picoy     15/11/2010  Se agrego el campo modelo
   Miguel Montoya  07/08/2015  Se agregó el campo npserviceROA
   Miguel Montoya  05/10/2015  Se agregaron campos adicionales
   Daniel Erazo    17/02/2017  PRY-0721: Se agrega 2 campos para la region
   Daniel Erazo    28/11/2017  TDECONV003-2: Se agrega un campo para Imei FullStack
   */
  public HashMap getItemOrder(long intOrderId)  throws Exception, SQLException {

    Connection conn = null;
    ResultSet rs=null;
    OracleCallableStatement cstm = null;
    OracleCallableStatement cstm1= null;
    ArrayList list  = null;
    ItemBean itemBean = null;
    String strMessage = null;
    HashMap objHashMapResultado = new HashMap();
    ServiceDAO  objServiceDAO = new ServiceDAO();
    String existNumberReserve = Constante.ANSWER_NOT;
    try{
      String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_ITEM_ORDER(?,?,?); END;";
      conn = Proveedor.getConnection();
      cstm = (OracleCallableStatement) conn.prepareCall(strSql);

      cstm.setLong(1,intOrderId);
      cstm.registerOutParameter(2, OracleTypes.CURSOR);
      cstm.registerOutParameter(3, Types.CHAR);

      cstm.execute();
      strMessage  = cstm.getString(3);

      if( strMessage == null){
        rs = (ResultSet)cstm.getCursor(2);
        list = new ArrayList();

        while(rs.next()){
          itemBean = new ItemBean();
          itemBean.setNpitemid(rs.getLong("npitemid"));
          itemBean.setNporderid(rs.getInt("nporderid"));
          itemBean.setNpsolutionid(rs.getInt("npsolutionid"));
          itemBean.setNpsolutionname(rs.getString("npsolutionname"));
          itemBean.setNpmodalitysell(rs.getString("npmodalitysell"));
          itemBean.setNpphone(rs.getString("npphone"));
          itemBean.setNpnewphone(rs.getString("npnewphone"));
          itemBean.setNpownimeinumber(rs.getString("npownimeinumber"));
          itemBean.setNpserialnumber(rs.getString("npserialnumber"));
          itemBean.setNpimeinumber(rs.getString("npimeinumber"));
          itemBean.setNpsiminumber(rs.getString("npsimnumber"));
          itemBean.setNpplanid(rs.getLong("npplanid"));
          itemBean.setNpplanname(rs.getString("npplanname"));
          itemBean.setNpmodel(rs.getString("npmodel"));
          itemBean.setNpmodelid(rs.getString("nporiginalproductid"));//jsalazar 06/12/2010 servicios adicionales
          itemBean.setNporiginalplanid(rs.getLong("nporiginalplanid"));
          itemBean.setNporiginalplanname(rs.getString("nporiginalplanname"));
          itemBean.setNpproductlineid(rs.getInt("npproductlineid"));
          itemBean.setNpproductlinename(rs.getString("npproductlinename"));
          itemBean.setNporiginalproductid(rs.getLong("nporiginalproductid"));
          itemBean.setNporiginalproductname(rs.getString("nporiginalproductname"));
          itemBean.setNpproductid(rs.getInt("npproductid"));
          itemBean.setNpproductname(rs.getString("npproductname"));
          itemBean.setNpquantity(rs.getInt("npquantity"));
          itemBean.setNpprice(rs.getString("npprice"));
          itemBean.setNppriceexception(rs.getString("nppriceexception"));

          itemBean.setNprent(rs.getString("nprent"));
          itemBean.setNpdiscount(rs.getString("npdiscount"));

          itemBean.setNpcurrency(rs.getString("npcurrency"));
          itemBean.setNpwarrant(rs.getString("npwarrant"));
          itemBean.setNpwarrantdesc(rs.getString("npwarrantdesc"));
          itemBean.setNpoccurrence(rs.getInt("npoccurrence"));
          itemBean.setNparearespdev(rs.getLong("nparearespdev"));
          itemBean.setNparearespdevname(rs.getString("nparearespdevname"));
          itemBean.setNpprovidergrpiddev(rs.getLong("npprovidergrpiddev"));
          itemBean.setNpprovidergrpiddevname(rs.getString("npprovidergrpiddevname"));
          itemBean.setNppromotionid(rs.getInt("nppromotionid"));
          itemBean.setNpaddendumid(rs.getInt("npaddendumid"));
          itemBean.setNpinventorycode(rs.getString("npinventorycode"));
          itemBean.setNpequipment(rs.getString("npequipment"));
          itemBean.setNpnewmainservice(rs.getInt("npnewmainservice"));
          itemBean.setNpequipmentreturn(rs.getString("npequipmentreturn"));
          itemBean.setNpequipmentreturndesc(rs.getString("npequipmentreturndesc"));
          itemBean.setNpequipmentnotyetgiveback(rs.getString("npequipmentnotyetgiveback"));
          itemBean.setNpequipmentreturndate(rs.getDate("npequipmentreturndate"));
          itemBean.setNpexception(rs.getString("npexception"));
          itemBean.setNpexceptionrevenue(rs.getString("npexceptionrevenue"));
          itemBean.setNpexceptionrevenuediscount(rs.getInt("npexceptionrevenuediscount"));
          itemBean.setNpexceptionrent(rs.getString("npexceptionrent"));
          itemBean.setNpexceptionrentdiscount(rs.getInt("npexceptionrentdiscount"));
          itemBean.setNpexceptionminadidispatch(rs.getString("npexceptionminadidispatch"));
          itemBean.setNpexceptionminaditelephony(rs.getString("npexceptionminaditelephony"));
          itemBean.setNpinstallationaddressid(rs.getInt("npinstallationaddressid"));
          itemBean.setNpsharedinstalationid(rs.getInt("npsharedinstalationid"));
          itemBean.setNpmodificationdate(rs.getDate("npmodificationdate"));
          itemBean.setNpmodificationby(rs.getString("npmodificationby"));
          itemBean.setNpcreateddate(rs.getDate("npcreateddate"));
          itemBean.setNpcreatedby(rs.getString("npcreatedby"));
          itemBean.setNpitemservices(rs.getString("service"));
          itemBean.setNpminutesrate(rs.getString("npminutesrate"));
          //itemBean.setNporiginalproductname(rs.getString("nporiginalproductname"));

          /* Inicio - Banda Ancha */
          itemBean.setNpsharedinstal(rs.getString("npsharedinstal"));
          itemBean.setNpsharedinstaldesc(rs.getString("npsharedinstaldesc"));
          itemBean.setNpfirstcontract(rs.getString("npfirstcontract"));
          itemBean.setNpfirstcontractdesc(rs.getString("npfirstcontractdesc"));
          itemBean.setNpcontractnumber(rs.getInt("npcontractnumber"));
          itemBean.setNpsharedinstalationid(rs.getLong("npsharedinstalationid"));
          itemBean.setNpinstallationaddressid(rs.getLong("npinstallationaddressid"));
          itemBean.setNpinstalationprice(rs.getString("npinstalationprice"));
          itemBean.setNpinstalationexception(rs.getString("npinstalationexception"));
          itemBean.setNplinktype(rs.getString("nplinktype"));
          itemBean.setNplinktypedesc(rs.getString("nplinktypedesc"));
          itemBean.setNpnetworkhosttype(rs.getInt("npnetworkhosttype"));
          itemBean.setNpnetworkhosttypedesc(rs.getString("npnetworkhosttypedesc"));
          itemBean.setNpfeasibilityprogdate(rs.getTimestamp("npfeasibilityprogdate"));
          itemBean.setNpfeasibility(rs.getString("npfeasibility"));
          itemBean.setNpinstalation(rs.getString("npinstalation"));
          itemBean.setNpinstalationprogdate(rs.getTimestamp("npinstalationprogdate"));

          itemBean.setNpfeasibilitydesc(rs.getString("npfeasibilitydesc"));
          /* Fin - Banda Ancha */

          /**Cambio - Bolsa**/
          itemBean.setNpminuteprice(rs.getDouble("nppriceminute"));

          /**Inicio Traslado - Banda Ancha**/
          itemBean.setNpnewaddress(rs.getLong("npnewaddress"));
          itemBean.setNpcontactname(rs.getString("npcontactname"));
          itemBean.setNpphonenumber1(rs.getString("npphonenumber1"));
          itemBean.setNpphonenumber2(rs.getString("npphonenumber2"));
          itemBean.setNpaditionalcost(rs.getString("npaditionalcost"));
          itemBean.setNpdescription(rs.getString("npdescription"));
          /**Fin Traslado - Banda Ancha**/

          /**Inicio Cambio de Plan Tarifario - Banda Ancha**/
          itemBean.setNpnewmainservice(rs.getLong("npnewmainservice"));
          itemBean.setNpnewmainservicedesc(rs.getString("npnewmainservicedesc"));
          itemBean.setNporigmainservice(rs.getLong("nporigmainservice"));
          itemBean.setNporigmainservicedesc(rs.getString("nporigmainservicedesc"));
          /**Fin Cambio de Plan Tarifario - Banda Ancha**/

          itemBean.setNpendservicedate(rs.getDate("npendservicedate"));
          itemBean.setNpreferencephone(rs.getString("npreferencephone"));

          String strItemBilling  = new String();
          strItemBilling     =     getItemOrderAssignementLogic01(rs.getLong("npitemid"));
          itemBean.setNpitembillingaccount(strItemBilling);

          /*JPEREZ: Excepciones - Inicio*/
          itemBean.setNpitemfreeservices(rs.getString("freeservice"));
          itemBean.setNpitemservicescost(rs.getString("servicecost"));
          /*JPEREZ: Excepciones - Fin*/

          /** Lista de plantillas de adendas **/
          itemBean.setNpitemaddendumtemplates(rs.getString("npitemaddendumtemplates"));

          /**Acuerdos Comerciales**/
          itemBean.setNporiginalprice(rs.getString("nporiginalprice"));
          itemBean.setNppricetype(rs.getString("nppricetype"));
          itemBean.setNppricetypeid(rs.getLong("nppricetypeid"));
          itemBean.setNppricetypeitemid(rs.getLong("nppricetypeitemid"));
          /**Acuerdos Comerciales**/

          itemBean.setNpfixedphone(rs.getString("npfixedphone"));
          itemBean.setNplocution(rs.getString("nplocution"));

          /*Incio Responsable de Pago*/
          itemBean.setNpsiteid(rs.getLong("npsiteid"));
          itemBean.setNpsitename(rs.getString("npsitename"));
          /*Fin Responsable de Pago*/

          /*Incio Contratos de Referencia*/
          itemBean.setNpinternetrefcontract(rs.getLong("npinternetrefcontract"));
          itemBean.setNpdatosrefcontract(rs.getLong("npdatosrefcontract"));
          itemBean.setNptfrefphonenumber(rs.getString("nptfrefphonenumber"));
          itemBean.setNpcontractref(rs.getLong("npcontractref"));
          /*Fin Contratos de Referencia*/

          /**Suspencion definitiva**/
          itemBean.setNpestadoitemId(rs.getString("npestadoitemId"));
          itemBean.setNpestadoitemDesc(rs.getString("npestadoitemDesc"));
          itemBean.setNpestadoproceso(rs.getString("nperrorautom"));
          /**Fin Suspencion definitiva**/

          /**Suspencion TEMPORAL**/
          itemBean.setNpcobro(rs.getString("npprice"));
          /**Fin Suspencion TEMPORAL**/

          itemBean.setNptypeip(rs.getInt("nptypeip"));
          itemBean.setNptypeipdesc(rs.getString("nptypeipdesc"));

          /**CBARZOLA:Cambio de Modelo - Distintas Tecnologias*/
          itemBean.setNporiginalsolutionid(rs.getInt("nporiginalsolutionid"));
          itemBean.setNporiginalsolutionname(rs.getString("nporiginalsolutionname"));
          /**Cambio de Modelo - Distintas Tecnologias*/

          itemBean.setNpitemoperationstatus(rs.getString("nperrorautom"));

          itemBean.setNpServiceItemList(objServiceDAO.getServiceItemList(itemBean.getNpitemid()));

		  //CPUENTE CAP & CAL
          itemBean.setNpequipmenttime(rs.getInt("npequipmenttime"));
          itemBean.setNpreasonchange(rs.getString("npreasonchange"));
          itemBean.setNpownequipment(rs.getInt("npownequipstate"));
          //CPUENTE CAP & CAL

		  //phidalgo
          itemBean.setNpLevel(rs.getInt("nplevel"));
          itemBean.setNpLevelDesc(rs.getString("npleveldesc"));

          itemBean.setNpimeicustomer(rs.getString("npimeicustomer"));
          itemBean.setNpownimeisim(rs.getString("npownimeisim"));

          //

            /*ADT-BCL-083*/
            itemBean.setNptypeproductBC(rs.getInt("nptypeproduct"));
          //EFLORES CDM+CDP 20/03/2017 PRY-0817 
          itemBean.setNpkeepSIM(rs.getInt("npkeepsim"));

          //PRY-0721 DERAZO Se obtiene la region
          itemBean.setNpzonacoberturaid(rs.getInt("npzonacoberturaid"));
          itemBean.setNpnombrezona(rs.getString("npnombrezona"));
          
          //PRY-0762 JQUISPE Se obtiene la Renta Adelantada
		  String cantidadRentaAdelantada = rs.getString("npcantidadRentaAdelantada");
		  if(cantidadRentaAdelantada != null){
			  itemBean.setNpcantidadRentaAdelantada(Integer.valueOf(cantidadRentaAdelantada));
		  }
		  itemBean.setNptotalRentaAdelantada(rs.getString("nptotalRentaAdelantada"));
          
          //EFLORES BAFI2
          itemBean.setNpProvinceZoneId(rs.getInt("npProvinceZoneId"));
          itemBean.setNpNameProvinceZone(rs.getString("npNameProvinceZone"));
          itemBean.setNpDistrictZoneId(rs.getInt("npDistrictZoneId"));
          itemBean.setNpNameDistrictZone(rs.getString("npNameDistrictZone"));
  
          //PRY-0890 JBALCAZAR Se obtiene el monto del prorrateo
		  itemBean.setNpprorrateoPrice(rs.getString("npprorrateoprice"));          
		  System.out.println("ItemDao/npprorrateoprice --->" + rs.getString("npprorrateoprice"));  
		  
          //DERAZO TDECONV003-2 Se obtiene Imei FS
          itemBean.setNpOwnImeiFS(rs.getString("npOwnImeiNumberFS"));
          System.out.println("ItemDao/npOwnImeiNumberFS --->" + rs.getString("npOwnImeiNumberFS"));          

          //CPUENTE2 CAP & CAL
          strSql = "BEGIN ORDERS.NP_REPOS_UPGRADE_PKG.SP_OBTIENE_DATOS_CONTRATO(?,?,?,?,?); END;";
          cstm1 = (OracleCallableStatement) conn.prepareCall(strSql);

          cstm1.setInt(1, itemBean.getNpcontractnumber());
          cstm1.registerOutParameter(2, Types.VARCHAR);
          cstm1.registerOutParameter(3, Types.VARCHAR);
          cstm1.registerOutParameter(4, Types.VARCHAR);
          cstm1.registerOutParameter(5, Types.VARCHAR);

          cstm1.executeUpdate();
          String strMessage2  = cstm1.getString(5);
          //System.out.println("StrMessage2 --->" + strMessage2);
          if( strMessage2 == null)
          {
              itemBean.setNpestadoContrato(cstm1.getString(2));
              itemBean.setNpmotivoEstado(cstm1.getString(3));
              itemBean.setNpfechaCambioEstado(cstm1.getString(4));
          }
          //CPUENTE2 CAP & CAL

          /**DLAZO - SUSCRIPCIONES**/
          itemBean.setNpactivationdate(rs.getDate("npactivationdate"));
          itemBean.setNpdeactivationdate(rs.getDate("npdeactivationdate"));
          /**DLAZO - SUSCRIPCIONES**/

          //Por Garantia Extendida - 15/11/2010 - FPICOY
          itemBean.setNpproductmodelid(rs.getInt("npproductmodelid"));
          //Verifica si en la orden existe algun item con Reserva de Numero Golden - FPICOY
          if (Constante.ANSWER_NOT.equals(existNumberReserve) && Constante.PRODUCT_LINE_KIT_GOLDEN.equals(String.valueOf(itemBean.getNpproductlineid()))) {
             existNumberReserve = Constante.ANSWER_YES;
          }

          itemBean.setNpVepItem(rs.getInt("npvepitem"));
          itemBean.setNpVepTotalPrice(rs.getLong("npveptotalprice"));

          // MMONTOYA - Despacho en tienda
          itemBean.setNpproductstatus(rs.getString("npproductstatus"));
          itemBean.setNpchanged(rs.getString("npchanged"));
          itemBean.setNpflagaccessory(rs.getString("npflagaccessory"));

          // MMONTOYA - ADT-RCT-092
          if (itemBean.getNpprice() != null) {
              itemBean.setNpserviceROA(SEJBRoamingBean.buildServiceDescription(rs.getString("npserviceROA"), BigDecimal.valueOf(Double.parseDouble(itemBean.getNpprice()))));
              itemBean.setNpservplantype(rs.getInt("npservplantype"));
              itemBean.setNpservbagcode(rs.getString("npservbagcode"));
              itemBean.setNpservbagtype(rs.getString("npservbagtype"));
              itemBean.setNpservvalidactivationdate(rs.getDate("npservvalidactivationdate"));
              itemBean.setNpservvalidity(rs.getInt("npservvalidity"));
          }

          list.add(itemBean);
        }

      }
      objHashMapResultado.put("strMessage",strMessage);
      objHashMapResultado.put("objArrayList",list);
      objHashMapResultado.put("existNumberReserve",existNumberReserve);
    }catch (Exception e) {
      logger.error(formatException(e));
      objHashMapResultado.put("strMessage",e.getMessage());
    }finally{
      try{
        closeObjectsDatabase(null, cstm1, null);
        closeObjectsDatabase(conn,cstm,rs);
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }
    return objHashMapResultado;
  }

    public String getItemOrderAssignementLogic01(long intItemId)  throws SQLException, Exception {
      ArrayList objListAssignment = null;
      BaAssignmentBean baAssignmentBean = null;
      String    strResult = "";

      objListAssignment            = getItemOrderAssignementBA(intItemId);

      if( objListAssignment!=null && objListAssignment.size()>0 ){
         baAssignmentBean = new BaAssignmentBean();
         baAssignmentBean = (BaAssignmentBean)objListAssignment.get(0);

         if( !MiUtil.getString(baAssignmentBean.getNpactivesiteid()).equals("") )
          strResult  = "|E|"+baAssignmentBean.getNpactivesiteid();
         else if( !MiUtil.getString(baAssignmentBean.getNpunknowsiteid()).equals("") )
          strResult  = "|S|"+baAssignmentBean.getNpunknowsiteid();
         else
          strResult  = "|N|-2";


         if( !MiUtil.getString(baAssignmentBean.getBillingAccountId()).equals("") )
          strResult  += "|E|"+baAssignmentBean.getBillingAccountId();
         else if( !MiUtil.getString(baAssignmentBean.getNpbillaccnewid()).equals("") )
          strResult  += "|S|"+baAssignmentBean.getNpbillaccnewid();
         else
          strResult  += "|N|-2";

          strResult  += "|";

      }
      return strResult;
    }
    public String getItemOrderAssignementLogic(long intItemId)  throws Exception, SQLException {
        int         countServices = 0;
        ArrayList   list          = null;
        ArrayList   listService   = null;
        ServiceDAO  objServiceDAO = new ServiceDAO();
        String      cadenaOutPut  = new String();

        listService     = objServiceDAO.getServiceItemList(intItemId);
        //countServices   = listService.size();

        list            = getItemOrderAssignementBA(intItemId);

        //1° Se Descarta si se tiene asignado a Primer Nivel
        String accountBillingExistente  = null;
        String accountBillingSolicitada = null;
        String flgLevelEquipment        = null;
        BaAssignmentBean  baAssignmentBean = null;
        for( int i = 0; i < list.size(); i++ ){
            baAssignmentBean = new BaAssignmentBean();
            baAssignmentBean = (BaAssignmentBean)list.get(i);

            if( baAssignmentBean.getNpserviceid() != null || baAssignmentBean.getNpchargetype() != null ){
                if( baAssignmentBean.getNpbillaccnewid() != null )//Es Solicitada
                    cadenaOutPut += "|EQP|S|" + baAssignmentBean.getNpbillaccnewid();
                else if( baAssignmentBean.getBillingAccountId() != null ) //Es Existente
                    cadenaOutPut += "|EQP|E|" + baAssignmentBean.getBillingAccountId();
                break;
            }
        }

        if ( cadenaOutPut.trim().equals("") )
            cadenaOutPut += "|EQP|S|-2";

        flgLevelEquipment = cadenaOutPut;

        //2° Se descarat si se tiene asignado a Segundo Nivel
        ServiciosBean objServiceBean      = null;

        for( int i = 0; i < listService.size(); i++ ){
            String flgLevelService            = "";
            String flgLevelServiceUnique      = "";
            String flgLevelServiceRecurrent   = "";
            String flgLevelServiceExceso      = "";
            objServiceBean  = (ServiciosBean)listService.get(i);

            for( int j = 0; j < list.size(); j++ ){
              baAssignmentBean = new BaAssignmentBean();
              baAssignmentBean = (BaAssignmentBean)list.get(j);

                if ( objServiceBean.getNpservicioid() == MiUtil.parseLong(baAssignmentBean.getNpserviceid()) ) {
                    if( baAssignmentBean.getNpchargetype() == null ){
                        if( baAssignmentBean.getNpbillaccnewid() != null )//Es Solicitada
                            flgLevelService += "|SRV|S|" + baAssignmentBean.getNpbillaccnewid() + "|" + objServiceBean.getNpservicioid();
                        else if( baAssignmentBean.getBillingAccountId() != null ) //Es Existente
                            flgLevelService += "|SRV|E|" + baAssignmentBean.getBillingAccountId() + "|" + objServiceBean.getNpservicioid();;
                    }

                    if( baAssignmentBean.getNpchargetype() != null ){

                      //Para el caso de Único
                      if( baAssignmentBean.getNpchargetype().equals("Unico") )
                        if( baAssignmentBean.getNpbillaccnewid() != null )//Es Solicitada
                            flgLevelServiceUnique += "|SRVUN|S|" + baAssignmentBean.getNpbillaccnewid();
                        else if( baAssignmentBean.getBillingAccountId() != null ) //Es Existente
                            flgLevelServiceUnique += "|SRVUN|E|" + baAssignmentBean.getBillingAccountId();

                      //Para el caso de Único
                      if( baAssignmentBean.getNpchargetype().equals("Recurrente") )
                        if( baAssignmentBean.getNpbillaccnewid() != null )//Es Solicitada
                            flgLevelServiceRecurrent += "|SRVRE|S|" + baAssignmentBean.getNpbillaccnewid();
                        else if( baAssignmentBean.getBillingAccountId() != null ) //Es Existente
                            flgLevelServiceRecurrent += "|SRVRE|E|" + baAssignmentBean.getBillingAccountId();

                      //Para el caso de Único
                      if( baAssignmentBean.getNpchargetype().equals("Excesos") )
                        if( baAssignmentBean.getNpbillaccnewid() != null )//Es Solicitada
                            flgLevelServiceExceso += "|SRVEX|S|" + baAssignmentBean.getNpbillaccnewid();
                        else if( baAssignmentBean.getBillingAccountId() != null ) //Es Existente
                            flgLevelServiceExceso += "|SRVEX|E|" + baAssignmentBean.getBillingAccountId();

                    }


                }


            }

             //3° Se descarta si se tiene asignado tercer nivel

            if(  flgLevelService.equals("") )
              cadenaOutPut += "|SRV|S|-2|"+objServiceBean.getNpservicioid();
            else
              cadenaOutPut += flgLevelService;

            //Unico
            if(  flgLevelServiceUnique.equals("") )
              cadenaOutPut += "|SRVUN|S|-2";
            else
              cadenaOutPut += flgLevelServiceUnique;

            //Recurrente
            if(  flgLevelServiceRecurrent.equals("") )
              cadenaOutPut += "|SRVRE|S|-2";
            else
              cadenaOutPut += flgLevelServiceRecurrent;

            //Excesos
            if(  flgLevelServiceExceso.equals("") )
              cadenaOutPut += "|SRVEX|S|-2";
            else
              cadenaOutPut += flgLevelServiceExceso;

            cadenaOutPut += flgLevelEquipment;

        }
        return cadenaOutPut;
    }

    public ArrayList getItemOrderAssignementBA(long intItemId)  throws Exception, SQLException {

        Connection conn = null;
        ResultSet rs=null;
        OracleCallableStatement cstmt = null;
        ArrayList list  = null;
        BaAssignmentBean baAssignmentBean = null;
        String strMensaje = null;
        try{
          String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_ASSIGNMENT_BILLING_ACC(?,?,?); END;";
          conn = Proveedor.getConnection();
          cstmt = (OracleCallableStatement) conn.prepareCall(strSql);

          cstmt.setLong(1,intItemId);
          cstmt.registerOutParameter(2, OracleTypes.CURSOR);
          cstmt.registerOutParameter(3, Types.CHAR);

          cstmt.execute();
          strMensaje  = cstmt.getString(3);
          if (strMensaje == null){
            rs = (ResultSet)cstmt.getObject(2);
            list = new ArrayList();
            if(rs.next()){
               baAssignmentBean = new BaAssignmentBean();
               baAssignmentBean.setNpdeviceid(rs.getInt("npdeviceid"));
               baAssignmentBean.setNporderid(rs.getInt("nporderid"));
               baAssignmentBean.setNpserviceid(rs.getString("npserviceid"));
               baAssignmentBean.setNpchargetype(rs.getString("npchargetype"));
               baAssignmentBean.setBillingAccountId(rs.getString("billing_account_id"));
               baAssignmentBean.setNpbillaccnewid(rs.getString("npbillaccnewid"));
               baAssignmentBean.setNpactivesiteid(rs.getString("npactivesiteid"));
               baAssignmentBean.setNpunknowsiteid(rs.getString("npunknowsiteid"));
               baAssignmentBean.setNpcreateddate(rs.getDate("npcreateddate"));
               baAssignmentBean.setNpcreatedby(rs.getString("npcreatedby"));
               list.add(baAssignmentBean);
            }
          }else{
            throw new Exception(strMensaje);
          }

        }catch (Exception e) {
          logger.error(formatException(e));
          throw new Exception(e);
        }finally{
          try{
            closeObjectsDatabase(conn,cstmt,rs);
          }catch (Exception e) {
            logger.error(formatException(e));
          }
        }

        return list;
    }


    /**
     Method : getItemHeaderSpecGrp
     Purpose: Obtiene el Header y el Value de los objetos ITEM.
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Lee Rosales     11/09/2007  Creación
     */
    public ArrayList getItemHeaderSpecGrp(int intSpecificationId)  throws Exception, SQLException {

        Connection conn = null;
        ResultSet rs=null;
        OracleCallableStatement cstm = null;
        ArrayList list  = null;
        NpObjItemSpecgrp npObjItemSpecgrp = null;
        String strMensaje = null;
        try
        {
          String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_ITEM_HEADER_SPECGRP(?,?,?); end;";
          conn = Proveedor.getConnection();
          cstm = (OracleCallableStatement) conn.prepareCall(strSql);

          cstm.setInt(1,intSpecificationId);
          cstm.registerOutParameter(2, OracleTypes.CURSOR);
          cstm.registerOutParameter(3, Types.CHAR);

          cstm.execute();

          strMensaje  = cstm.getString(3);
          if (strMensaje == null){
            rs = (ResultSet)cstm.getObject(2);
            list = new ArrayList();

            while(rs.next()){
                npObjItemSpecgrp = new NpObjItemSpecgrp();
                npObjItemSpecgrp.setNpobjspecgrpid(MiUtil.parseInt(rs.getString("npobjspecgrpid")));
                npObjItemSpecgrp.setNpobjitemid(MiUtil.parseInt(rs.getString("npobjitemid")));
                npObjItemSpecgrp.setNpobjitemname(rs.getString("npobjitemname"));
                //npObjItemSpecgrp.setNpnamehtmlheader(rs.getString("namehtmlheader"));
                npObjItemSpecgrp.setNpnamehtmlitem(rs.getString("nphtmlname"));
                npObjItemSpecgrp.setNpobjitemcontroltype(rs.getString("npcontroltype"));
                npObjItemSpecgrp.setNpdefaultvalue(rs.getString("npdefaultvalue"));
                npObjItemSpecgrp.setNpsourceprogram(rs.getString("npsourceprogram"));
                npObjItemSpecgrp.setNpspecificationgrpid(rs.getInt("npspecificationgrpid"));
                npObjItemSpecgrp.setNpdisplay(rs.getString("npdisplay"));
                npObjItemSpecgrp.setNpobjreadonly(rs.getString("npobjreadonly"));
                npObjItemSpecgrp.setNpobjitemheaderid(rs.getInt("npobjitemheaderid"));
                npObjItemSpecgrp.setNpdatatype(rs.getString("npdatatype"));
                npObjItemSpecgrp.setNpvalidateflg(rs.getString("npvalidateflg"));
                npObjItemSpecgrp.setNplength(rs.getInt("nplength"));
                list.add(npObjItemSpecgrp);
            }
          }

        }catch (Exception e) {
          logger.error(formatException(e));
          throw new Exception(e);
        }finally{
          try{
            closeObjectsDatabase(conn,cstm,rs);
          }catch (Exception e) {
            logger.error(formatException(e));
          }
        }

        return list;
    }

    /**
     Method : getHeaderSpecGrp
     Purpose: Obtiene el Header y el Value de los objetos ITEM.
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Lee Rosales     11/09/2007  Creación
     */
    public ArrayList getHeaderSpecGrp(int intSpecificationId)  throws Exception, SQLException {

        Connection conn = null;
        ResultSet rs=null;
        OracleCallableStatement cstm = null;
        ArrayList list  = null;
        NpObjHeaderSpecgrp objnpObjHeaderSpecgrp = null;
        String strMensaje = null;
        try{
            String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_HEADER_SPECGRP(?,?,?); end;";
            conn = Proveedor.getConnection();
            cstm = (OracleCallableStatement) conn.prepareCall(strSql);

            cstm.setInt(1,intSpecificationId);
            cstm.registerOutParameter(2, OracleTypes.CURSOR);
            cstm.registerOutParameter(3, Types.CHAR);

            cstm.execute();

            strMensaje  = cstm.getString(3);
            if ( strMensaje == null){
              rs = (ResultSet)cstm.getObject(2);
              list = new ArrayList();

              while(rs.next()){
                  objnpObjHeaderSpecgrp = new NpObjHeaderSpecgrp();
                  objnpObjHeaderSpecgrp.setNpspecificationgrpid(MiUtil.parseInt(rs.getString("npspecificationgrpid")));
                  objnpObjHeaderSpecgrp.setNpobjitemheaderid(MiUtil.parseInt(rs.getString("npobjitemheaderid")));
                  objnpObjHeaderSpecgrp.setNpobjitemname(rs.getString("npobjitemname"));
                  objnpObjHeaderSpecgrp.setNpdisplay(rs.getString("npdisplay"));
                  objnpObjHeaderSpecgrp.setNpcreatedby(rs.getString("npcreatedby"));
                  objnpObjHeaderSpecgrp.setNpobjitemheaderid(MiUtil.parseInt(rs.getString("npobjitemheaderid")));
                  objnpObjHeaderSpecgrp.setNphtmlname(rs.getString("nphtmlname"));
                  list.add(objnpObjHeaderSpecgrp);
              }
            }else{
              logger.error(strMensaje);
              throw new Exception(strMensaje);
            }

        }catch (Exception e) {
          logger.error(formatException(e));
          throw new Exception(e);
        }finally{
          try{
            closeObjectsDatabase(conn,cstm,rs);
          }catch (Exception e) {
            logger.error(formatException(e));
          }
        }

          return list;
    }




    public String getItemServiceInsertar(ItemServiceBean itemServiceBean, Connection conn) throws Exception, SQLException {

      String strMensaje = null;
      int    intResultTransaction = 0;
      //Connection conn = null;
      OracleCallableStatement cstmt = null;
      int intOrderId = 0;
      logger.debug("[Input][itemServiceBean]: " + itemServiceBean.toString());
      
      try{
        String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_INS_ORDER_ITEM_SERVICE(    ?,    ?,    ?,     ?,    ?, " + //5
                                                                                "   ?,    ?); END;";                //7


            cstmt = (OracleCallableStatement)conn.prepareCall(strSql);

            cstmt.setLong(1, itemServiceBean.getNpitemid());
            cstmt.setLong(2, itemServiceBean.getNpserviceid());
            cstmt.setString(3, itemServiceBean.getNpservicetype());
            cstmt.setDouble(4, itemServiceBean.getNpserviceprice());
            cstmt.setString(5, itemServiceBean.getNpservicefree());
            cstmt.setString(6, itemServiceBean.getNpaction());
            cstmt.registerOutParameter(7, OracleTypes.VARCHAR);

            cstmt.executeUpdate();
            strMensaje = cstmt.getString(7);
            
            logger.info("[Output][strMensaje]: " + strMensaje);

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

    public void setStrError(String strError) {
        this.strError = strError;
    }

    public String getStrError() {
        return strError;
    }

  /**
   Method : hasPaymentOrderId
   Purpose: Indica si a la orden ya se le genero los documentos de pago
   Developer       		Fecha       Comentario
   =============   		==========  ======================================================================
   Cristian Espinoza    19/01/2008  Creación
   */

    public HashMap hasPaymentOrderId(long nporderid) throws Exception, SQLException {

		String strMessage = null;
    HashMap objHashMapResultado = new HashMap();
		int    intResultTransaction = 0;
		Connection conn = null;
		OracleCallableStatement cstmt = null;
		int intOrderId = 0;
		long lNumPaymentOrderId;
		long lNumGuideNumber;


		String strSql = "BEGIN ORDERS.NP_ORDERS15_PKG.SP_HAS_PAYMENTORDERID(    ?,    ?,	?,  ?); END;"; //CEM COR426
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(strSql);

         cstmt.setLong(1, nporderid);
         cstmt.registerOutParameter(2, OracleTypes.NUMBER);
         cstmt.registerOutParameter(3, OracleTypes.NUMBER);
         cstmt.registerOutParameter(4, OracleTypes.VARCHAR);//CEM COR426

         cstmt.executeUpdate();
         lNumPaymentOrderId = cstmt.getInt(2);
         lNumGuideNumber	   = cstmt.getInt(3);
         strMessage = cstmt.getString(4); //CEM COR426
         objHashMapResultado.put("strMessage",strMessage);
          objHashMapResultado.put("strNumPaymentOrderId",lNumPaymentOrderId + "");
        objHashMapResultado.put("strNumGuideNumber",lNumGuideNumber + "");//CEM COR426
      }catch(Exception e){
         objHashMapResultado.put("strMessage",e.getMessage());
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null);
        }catch (Exception e) {
          logger.error(formatException(e));
        }
      }

		return objHashMapResultado;
    }

	/***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - INICIO
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 17/01/2008
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/

	/**
     * Motivo: Valida que el IMEI exista y si no es así envía un mensaje.
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 17/01/2008
     *
     * @param		hshItemDeviceMap     HashMap con información del ItemDevice
	 * @param		conn				 Connection (en caso que no se invoque desde SectionItemsEvents se pone null)
     * @return		String vacío o con un mensaje
     */
    public String doValidateIMEI_BK(HashMap hshItemDeviceMap, Connection conn) throws SQLException, Exception {
      String strMensaje = null;
      OracleCallableStatement cstmt = null;
      int iOrderId = 0;
      int iResultTransaction = 0;
      boolean bCreateOurConnection = false;
      try{
        String strSql = "BEGIN ORDERS.NP_ORDERS03_PKG.SP_VALIDATE_IMEI(?,?,?,?,?,?,?,?); END;";
        if(conn == null) {
          conn = Proveedor.getConnection();
          bCreateOurConnection = true;
        }
        cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
        int i = 0;
        cstmt.setString(++i, (String) hshItemDeviceMap.get("imei"));
        cstmt.setString(++i, (String) hshItemDeviceMap.get("lugarDespacho"));
        cstmt.setString(++i, (String) hshItemDeviceMap.get("producto"));
        cstmt.setString(++i, (String) hshItemDeviceMap.get("modalidad"));
        cstmt.setString(++i, (String) hshItemDeviceMap.get("subCategoria"));
        cstmt.setString(++i, (String) hshItemDeviceMap.get("garantia"));
        cstmt.setString(++i, (String) hshItemDeviceMap.get("tipoSalida"));
        cstmt.registerOutParameter(++i, OracleTypes.VARCHAR);
        cstmt.executeUpdate();
        strMensaje = cstmt.getString(i);
      }catch(Exception e){
         strMensaje = e.getMessage();
      }finally{
        try{
				if (bCreateOurConnection=true)
					closeObjectsDatabase(conn,cstmt,null);
				else
					closeObjectsDatabase(null,cstmt,null);
        }catch (Exception e) {
          logger.error(formatException(e));
        }
      }
      return strMensaje;
    }


    public String doValidateIMEI(HashMap hshItemDeviceMap, Connection conn1) throws SQLException, Exception {

      String strMensaje = null;
      int    intResultTransaction = 0;
      Connection conn = null;
      OracleCallableStatement cstmt = null;

      int intOrderId = 0;
      try{
          conn = Proveedor.getConnection();
          String strSql = "BEGIN ORDERS.NP_ORDERS03_PKG.SP_VALIDATE_IMEI(?,?,?,?,?,?,?,?); END;";
          cstmt = (OracleCallableStatement) conn.prepareCall(strSql);

            System.out.println("VALORES PARA VALIDAR IMEI");
            System.out.println("imei:"+ hshItemDeviceMap.get("imei"));
            System.out.println("lugarDespacho:"+ hshItemDeviceMap.get("lugarDespacho"));
            System.out.println("producto:"+ hshItemDeviceMap.get("producto"));
            System.out.println("modalidad:"+ hshItemDeviceMap.get("modalidad"));
            System.out.println("subCategoria:"+ hshItemDeviceMap.get("subCategoria"));
            System.out.println("garantia:"+ hshItemDeviceMap.get("garantia"));
            System.out.println("tipoSalida:"+ hshItemDeviceMap.get("tipoSalida"));

            cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
            cstmt.setString(1,  (String) hshItemDeviceMap.get("imei"));
            cstmt.setLong(2, MiUtil.parseLong((String) hshItemDeviceMap.get("lugarDespacho")));
            cstmt.setLong(3, MiUtil.parseLong((String) hshItemDeviceMap.get("producto")));
            cstmt.setString(4, (String) hshItemDeviceMap.get("modalidad"));
            cstmt.setLong(5, MiUtil.parseLong((String) hshItemDeviceMap.get("subCategoria")));
            cstmt.setString(6, (String) hshItemDeviceMap.get("garantia"));
            cstmt.setString(7, (String) hshItemDeviceMap.get("tipoSalida"));

            cstmt.registerOutParameter(8, OracleTypes.VARCHAR);
            logger.debug(hshItemDeviceMap);
            cstmt.executeUpdate();

            strMensaje = cstmt.getString(8);

      }catch(Exception e){
         strMensaje = e.getMessage();
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null);
        }catch (Exception e) {
          logger.error(formatException(e));
        }
      }

      return strMensaje;
    }

    /*
    public static void main(String[] args ) {
    try{
    System.out.println("Entramos");
    ItemDAO objItemDAO = new ItemDAO();
    HashMap hshItemDeviceMap = new HashMap();

    hshItemDeviceMap.put("imei","000100141704310");
    hshItemDeviceMap.put("lugarDespacho","21");
    hshItemDeviceMap.put("producto","354");
    hshItemDeviceMap.put("modalidad","Prestamo");
    hshItemDeviceMap.put("subCategoria","2037");
    hshItemDeviceMap.put("garantia",null);
    hshItemDeviceMap.put("tipoSalida",null);

    String strMessage  = objItemDAO.doValidateIMEI(hshItemDeviceMap,null);

    System.out.println("strMessage : " +  strMessage);
    }catch(Exception e){}

    }
    */



	/**
     * Motivo: Recupera la Lista de INBOX que pueden Generar Guías.
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 26/01/2008
     *
	 * @param		conn	Connection (en caso que no se invoque desde SectionItemsEvents se recibe null)
     * @return		HashMap que contiene:<br>
	 * 					1. ArrayList de String (Inbox)<br>
	 *                  2. String strMessage en caso hubiera error.
     */
	public HashMap getInboxGenerateGuide(Connection conn) throws SQLException, Exception {
		HashMap hshDataMap = new HashMap();
		ArrayList arrInboxList = new ArrayList();
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
		boolean bCreateOurConnection = false;
    try{
      String strSql = "BEGIN ORDERS.NP_ORDERS08_PKG.SP_GET_GENERATE_GUIDE(?,?); END;";
      String strMessage = null;
      if(conn == null) {
        conn = Proveedor.getConnection();
        bCreateOurConnection = true;
      }
      cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
      cstmt.registerOutParameter(1, OracleTypes.CURSOR);
      cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
      cstmt.executeUpdate();
      strMessage = cstmt.getString(2);
      if(StringUtils.isBlank(strMessage)) {
        rs = (ResultSet)cstmt.getObject(1);
        while(rs.next()) {
          arrInboxList.add(StringUtils.defaultString(rs.getString(1)));
        }
      }
      hshDataMap.put("arrInboxList", arrInboxList);
      hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
    }catch(Exception e){
         hshDataMap.put(Constante.MESSAGE_OUTPUT, e.getMessage());
    }finally{
      try{
        if (bCreateOurConnection=true)
          closeObjectsDatabase(conn,cstmt,rs);
        else
          closeObjectsDatabase(null,cstmt,rs);
      }catch (Exception e) {
          logger.error(formatException(e));
        }
      }

		return hshDataMap;
	}

    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - FIN
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 17/01/2008
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/

    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - INICIO
     *  REALIZADO POR: Carmen Gremios
     *  FECHA: 13/02/2008
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/

   /**
   * Motivo: Verifica el IMEI.
             0: IMEI disponible
             1: IMEI no existe en tablas
             2: IMEI existe como 'No Disponible'
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 13/02/2008
   * @param     strIMEI
   * @return    String
   */
   public HashMap doValidateIMEI(String strIMEI) throws Exception,SQLException{

      Connection conn = null;
      OracleCallableStatement cstmt = null;
      String strReturn = null;
      String strMessage = null;
      HashMap hshData=new HashMap();
      try{
        String sqlStr = "{ ? = call ORDERS.NP_ORDERS07_PKG.FX_VALIDATE_IMEI(?,?)}";

        conn = Proveedor.getConnection();

        cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

        cstmt.registerOutParameter(1,Types.VARCHAR);
        cstmt.setString(2,strIMEI);
        cstmt.registerOutParameter(3,Types.VARCHAR);

        cstmt.executeUpdate();
        strReturn = cstmt.getString(1);
        strMessage = cstmt.getString(3);

        hshData.put("strReturn",strReturn);
        hshData.put("strMessage",strMessage);
      }catch(Exception e){
         hshData.put("strMessage",e.getMessage());
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null);
          }catch (Exception e) {
            logger.error(formatException(e));
          }
        }

      return hshData;
   }

    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - FIN
     *  REALIZADO POR: Carmen Gremios
     *  FECHA: 13/02/2008
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
   
   
    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  CAP & CAL INICIO
     *  REALIZADO POR: CPUENTE
     *  FECHA: 28/08/2009
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
    /**
    * Motivo: Verifica si el equipo es propio y su estado.
    * <br>Realizado por: <a href="mailto:carlos.puente@nextel.com.pe">CPUENTE</a>
    * <br>Fecha: 28/09/2009 
    * @param     strIMEI  
    * @return    String 
    */
        public HashMap doValidateOwnEquipment(String strIMEI) throws Exception,SQLException{
    
       Connection conn = null; 
       OracleCallableStatement cstmt = null;
       int iReturn= 0; 
       String strMessage = null;
       HashMap hshData=new HashMap();
       try{
         String sqlStr = "{ call ORDERS.NP_REPOS_UPGRADE_PKG.SP_VALIDA_EQUIP_PROPIO(?,?,?)}"; 
       
         conn = Proveedor.getConnection();
         
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
             
         cstmt.setString(1,strIMEI);
         cstmt.registerOutParameter(2,OracleTypes.NUMBER);
         cstmt.registerOutParameter(3,OracleTypes.VARCHAR);
         
         cstmt.executeUpdate();    
         iReturn = cstmt.getInt(2);   
         strMessage = cstmt.getString(3);   
              
         hshData.put("strReturn",iReturn + "");
         hshData.put("strMessage",strMessage);      
       }catch(Exception e){
          hshData.put("strMessage",e.getMessage()); 
       }finally{
         try{
           closeObjectsDatabase(conn,cstmt,null); 
           }catch (Exception e) {
             logger.error(formatException(e));
           }
         }

       return hshData;                                                                
    }      
    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  CAP & CAL INICIO
     *  REALIZADO POR: CPUENTE
     *  FECHA: 28/08/2009
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
     
     /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  VALIDACION DE SIM - INICIO
     *  REALIZADO POR: Jorge Pérez
     *  FECHA: 08/03/2008
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/

   /**
   * Purpose: Valida la existencia de un SIM y obtiene su estado
   * Developer       Fecha       Comentario
   * =============   ==========  ======================================================================
   * JPEREZ          03/03/2008  Creación
   * @throws java.sql.SQLException
   * @throws java.lang.Exception
   * @return
   * @param strSIM
   */
     public HashMap doValidateSIM(String strSIM)throws Exception,SQLException{
      Connection conn = null;
      OracleCallableStatement cstmt = null;
      String strStatus  = null;
      String strMessage = null;
      HashMap hshData   = new HashMap();
      try{
        String sqlStr = "BEGIN WEBSALES.NP_BSCS_UTIL04_PKG.SP_VALIDATE_SIM(?,?,?); END;";

        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

        cstmt.setString(1, strSIM);
        cstmt.registerOutParameter(2, Types.CHAR);
        cstmt.registerOutParameter(3, Types.CHAR);
        cstmt.executeUpdate();

        strStatus            = cstmt.getString(2);
        strMessage            = cstmt.getString(3);

        hshData.put("strMessage",strMessage);
        hshData.put("strStatus",strStatus);
      }catch(Exception e){
         hshData.put("strMessage",e.getMessage());
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null);
          }catch (Exception e) {
            logger.error(formatException(e));
          }
      }
      return hshData;

     }

     /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  VALIDACION DE SIM - FIN
     *  REALIZADO POR: Jorge Pérez
     *  FECHA: 08/03/2008
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/

   /**
   * Purpose: Devuelve el tipo de Plantilla (Nueva o Renovación)
   * Developer       Fecha       Comentario
   * =============   ==========  ======================================================================
   * Odubock         25/04/2008  Creación
   * @throws java.sql.SQLException
   * @throws java.lang.Exception
   * @return
   * @param strSIM
   */
     public HashMap getTipoPlantillaAdenda(String strNumeroNextel, int iTemplateId)throws Exception,SQLException{
      Connection conn = null;
      OracleCallableStatement cstmt = null;
      int strFlagTipoAdend;
      String strMessage = null;
      HashMap hshData   = new HashMap();
      try{
        String sqlStr = "BEGIN ADDENDUM.SPI_GET_TEMPLATE_TYPE(?,?,?,?); END;";

        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

        cstmt.setString(1, strNumeroNextel);
        cstmt.setInt(2, iTemplateId);
        cstmt.registerOutParameter(3, OracleTypes.NUMBER);
        cstmt.registerOutParameter(4, Types.VARCHAR);
        cstmt.executeUpdate();

        strFlagTipoAdend        = cstmt.getInt(3);
        strMessage              = cstmt.getString(4);

        hshData.put("strMessage",strMessage);
        hshData.put("strFlagTipoAdend",strFlagTipoAdend+"");
      }catch(Exception e){
         hshData.put("strMessage",e.getMessage());
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null);
          }catch (Exception e) {
            logger.error(formatException(e));
          }
      }

      return hshData;
     }

     /**
   * Motivo: Valida que un IMEI y un SIM correspondan en BSCS
   * <br>Realizado por: <a href="mailto:jorge.perez@nextel.com.pe">Jorge Pérez</a>
   * <br>Fecha: 24/07/2008
   *
   * @throws java.lang.Exception
   * @throws java.sql.SQLException
   * @return
   * @param strSim
   * @param strImei
   * @param lSpecificationId
   */
    public HashMap getImeiSimMatch(long lSpecificationId ,String strImei, String strSim)throws SQLException, Exception{
      //DbmsOutput dbmsOutput = null;
      Connection conn = null;
      OracleCallableStatement cstm = null;
      String strMessage=null;
      HashMap hshRetorno = new HashMap();
      try {
        String sqlStr = "BEGIN ORDERS.NP_ORDERS13_PKG.SP_VALIDATE_IMEI_SIM_MATCH(?,?,?,?,?); END;";
        int iMatch  = -1;
        conn = Proveedor.getConnection();
        //dbmsOutput = new DbmsOutput(conn);
        //dbmsOutput.enable(1000000);
        cstm = (OracleCallableStatement) conn.prepareCall(sqlStr);
        cstm.setLong(1, lSpecificationId);
        cstm.setString(2, strImei);
        cstm.setString(3, strSim);
        cstm.registerOutParameter(4, OracleTypes.NUMBER);
        cstm.registerOutParameter(5, OracleTypes.VARCHAR);
        cstm.execute();
        //dbmsOutput.show();
        //dbmsOutput.close();
        strMessage = cstm.getString(5);
        iMatch = cstm.getInt(4); //Incluso si ocurre una excepción en BD, devuelve un valor
        if (logger.isDebugEnabled()) {
          logger.debug("strMessage: " + strMessage);
          logger.debug("sMatch: " + iMatch);
        }
        hshRetorno.put("sMatch",iMatch+"");
        hshRetorno.put("strMessage",strMessage);
      }catch(Exception e){
         hshRetorno.put("strMessage",e.getMessage());
      }finally{
        try{
          closeObjectsDatabase(conn,cstm,null);
          }catch (Exception e) {
            logger.error(formatException(e));
          }
      }

      System.out.println("-----Fin getImeiSimMatch-----");
      return hshRetorno;
     }


  /**
   Method : getActionItem
   Purpose: Trae el permiso para modificar, agregar o eliminar un item
   Developer       		Fecha       Comentario
   =============   		==========  ======================================================================
    Karen Salvador    02/10/2008  Creación
   */

    public HashMap getActionItem(long nporderid, String npaction, String npestadoPagoActual) throws Exception, SQLException {

		String strMessage = null;
    HashMap objHashMapResultado = new HashMap();
		Connection conn = null;
		OracleCallableStatement cstmt = null;
    String permission = null;

    try{

        String strSql = "BEGIN ORDERS.NP_ORDERS16_PKG.SP_GET_ACTION_ITEM( ?, ?,	?,?,?); END;";
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(strSql);

         cstmt.setLong(1, nporderid);
         cstmt.setString(2, npaction);
         cstmt.setString(3, npestadoPagoActual);
         cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(5, OracleTypes.VARCHAR);

         cstmt.executeUpdate();
         permission	   = cstmt.getString(4);
         strMessage = cstmt.getString(5);
         objHashMapResultado.put("strPermission",permission);
         objHashMapResultado.put("strMessage",strMessage);

      }catch(Exception e){
         objHashMapResultado.put("strMessage",e.getMessage());
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null);
        }catch (Exception e) {
          logger.error(formatException(e));
        }
      }

      return objHashMapResultado;
    }

    /**
   Method : getActionItem
   Purpose: Trae el permiso para modificar, agregar o eliminar un item
   Developer       		Fecha       Comentario
   =============   		==========  ======================================================================
    Rensso Martinez    28/06/2009  Creación
   */

  public HashMap getStatusItemList(String nameTable, String nptag1, String nptag2) throws Exception, SQLException {

		String strMessage = null;
    HashMap objHashMapResultado = new HashMap();
		Connection conn = null;
		OracleCallableStatement cstmt = null;
    ResultSet rs=null;
    ArrayList list  = null;


    try{

        String strSql = "BEGIN ORDERS.NP_ORDERS23_PKG.SP_GET_STATUS_ITEM( ?, ?,	?, ?, ?); END;";
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(strSql);

         cstmt.setString(1, nameTable);
         cstmt.setString(2, nptag1);
         cstmt.setString(3, nptag2);
         cstmt.registerOutParameter(4, OracleTypes.CURSOR);
         cstmt.registerOutParameter(5, OracleTypes.VARCHAR);

         cstmt.executeUpdate();
         strMessage = cstmt.getString(5);

         //objHashMapResultado.put("strMessage",strMessage);


           if  ( strMessage == null ){
              rs = (ResultSet)cstmt.getObject(4);
              list = new ArrayList();
              while(rs.next()){
                SpecificationBean ItemSpecificationBean= new SpecificationBean();
                ItemSpecificationBean.setNpEstadoItemId  (rs.getString("npvalue"));
                ItemSpecificationBean.setNpEstadoItemName(rs.getString("npvaluedesc"));
                list.add(ItemSpecificationBean);
              }
            }

            objHashMapResultado.put("objArrayList",list);

      }catch(Exception e){
         objHashMapResultado.put("strMessage",e.getMessage());
         System.out.println("[Exception][ItemDAO][getStatusItemList]"+e.getMessage());
         throw new Exception(e);

      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null);
        }catch (Exception e) {
          logger.error(formatException(e));
        }
      }
      objHashMapResultado.put("strMessage",strMessage);
      return objHashMapResultado;
    }
    
    /*jsalazar - modif hpptt # 1 - 29/09/2010 - inicio*/

  public HashMap getItemServicePendingList(long lOrderId, long lItemId) throws Exception, SQLException {
    String strMessage = "entro";
    HashMap objHashMapResultado = new HashMap();
		Connection conn = null;
		OracleCallableStatement cstmt = null;
    ArrayList list  = new ArrayList();
    try{
        String strSql = "BEGIN ORDERS.NP_ADITIONAL_SERVICE_ORDER_PKG.SP_GET_EXEC_STATUS_DETAIL_ITEM(?, ?,	?, ?); END;";
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
        cstmt.setLong(1, lOrderId);
        cstmt.setLong(2, lItemId);
        cstmt.registerOutParameter(3, OracleTypes.ARRAY, "TT_EXEC_STATUS_DETAIL_ITEM");
        cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
        
        cstmt.execute();
        strMessage = cstmt.getString(4);  
        
        if(strMessage == null){
          ARRAY pendingArray = cstmt.getARRAY(3);
          if(pendingArray != null){
           for (int i = 0; i < pendingArray.getOracleArray().length; i++) {
              Object[] object = ((STRUCT) pendingArray.getOracleArray()[i]).getAttributes();
              list.add(object); 
            }
          }
        }
      objHashMapResultado.put("objArrayList",list);
      cstmt.close();
    }catch(Exception e){
         objHashMapResultado.put("strMessage",e.getMessage());
         System.out.println("[Exception][ItemDAO][getItemServicePendingList]"+e.getMessage());
         /*throw new Exception(e);*/
    }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null);
        }catch (Exception e) {
          logger.error(formatException(e));
        }
      }
      objHashMapResultado.put("strMessage",strMessage);
      return objHashMapResultado;
  }
/*jsalazar - modif hpptt # 1 - 29/09/2010 - fin*/

  /**
   Method : getNumberGolden
   Purpose: lista un recurso por PTN y UFMI 
   Developer       		Fecha       Comentario
   =============   		==========  ======================================================================
    Ronald Huacani    15/10/2010  Creación
   */
   public HashMap getNumberGolden(String sCodApp, long lDnType,long lNpcode,String sDnNum,long lTmCode, String sExcluded, String sQuantity, String sPortabilidad) throws Exception, SQLException {
    String strMessage = "";
    HashMap objHashMapResultado = new HashMap();
		Connection conn = null;
		OracleCallableStatement cstmt = null;
    ArrayList list  = new ArrayList();
    String searchByPairFlag="TRUE";
    try{
        if(lNpcode==89)
          searchByPairFlag="FALSE";
                
        String strSql = "BEGIN ORDERS.SPI_GET_GOLDEN_NUMBER_LIST(?, ?, ?, ?, ?, "+searchByPairFlag+", TRUE, ?, ?, ?, ?, ?, ?); END;";
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
        cstmt.setString(1, sCodApp);
        cstmt.setLong(2, lDnType);
        cstmt.setLong(3, lNpcode);
        cstmt.setString(4, sDnNum);
        cstmt.setLong(5, lTmCode);
        cstmt.setString(6, sExcluded);
        cstmt.setString(7, sQuantity);
        cstmt.setString(8, sPortabilidad);
        cstmt.registerOutParameter(9, OracleTypes.ARRAY, "ORDERS.TT_RESOURCE_LST");
        cstmt.registerOutParameter(10, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(11, OracleTypes.VARCHAR);
        
        cstmt.execute();
        strMessage = cstmt.getString(11); 
        System.out.println("strMessage:" + strMessage);
        
        if(strMessage == null){
          ARRAY pendingArray = (ARRAY)cstmt.getObject(9);
           
         if(pendingArray != null){
          System.out.println("pendingArray.getOracleArray().length " + pendingArray.getOracleArray().length);
           for (int i = 0; i < pendingArray.getOracleArray().length; i++) {
              STRUCT stcPendingArray = (STRUCT) pendingArray.getOracleArray()[i];
              Object[] object = stcPendingArray.getAttributes();
              list.add(object); 
            }
          }
        }
      objHashMapResultado.put("objArrayList",list);
      cstmt.close();
    }catch(Exception e){
         objHashMapResultado.put("strMessage",e.getMessage());
         System.out.println("[Exception][ItemDAO][getItemServicePendingList]"+e.getMessage());
    }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null);
        }catch (Exception e) {
          logger.error(formatException(e));
        }
      }
      objHashMapResultado.put("strMessage",strMessage);
      return objHashMapResultado;
  }

  /**
   Method : transferExtendedGuarantee
   Purpose: Traslada la garantia extendida al nuevo equipo.
   Developer       		Fecha       Comentario
   =============   		==========  ======================================================================
   Frank Picoy        15/11/2010  Creación
   */
   public HashMap transferExtendedGuarantee(String strImei,String strImeiNuevo) throws SQLException, Exception{      
      String strMessage=null;
      Connection conn = null;
      OracleCallableStatement cstmt = null;   
      ResultSet rs = null;      
      HashMap hshData=new HashMap();
      System.out.println("Imei Nuevo Enviado a SP_ORDERS_EXT_GUARANTEE_SSTT : "+strImeiNuevo);
      System.out.println("Imei Antiguo Enviado a SP_ORDERS_EXT_GUARANTEE_SSTT : "+strImei);
      String sqlStr = "BEGIN ORDERS.NP_ORDERS10_PKG.SP_ORDERS_EXT_GUARANTEE_SSTT(?, ?, ?); END;";
      try{
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        cstmt.setString(1, strImei);   
        cstmt.setString(2, strImeiNuevo);    
        cstmt.registerOutParameter(3, Types.CHAR);
        cstmt.execute();
        strMessage = cstmt.getString(3);                  
     }catch(Exception e){
        logger.error(formatException(e));
        throw new Exception(e);
      }finally{
        closeObjectsDatabase(conn, cstmt, rs);
      }
      hshData.put("strMessage",strMessage);
    return hshData;
  }
  
  /**
   Method : reserveGoldenNumber
   Purpose: Reserva los numeros golden ingresados en la orden
   Developer       		Fecha       Comentario
   =============   		==========  ======================================================================
   Frank Picoy        15/11/2010  Creación
   */
   public HashMap reserveGoldenNumber(ArrayList objArray, int intSpecificationId) throws Exception,SQLException{
    ResultSet rs=null;
    OracleCallableStatement cstmt = null;
    Connection conn = null;
    String strMessage = null;
    String strSuccess = "";
    String strNumberNoAvailable = "";
    boolean successReserve=true;
    HashMap objHashMap = null;
    HashMap   objHashMapResultado = new HashMap();   
    ArrayList arrItemResource    = new ArrayList();
    STRUCT stcItemResource = null;
    STRUCT stcResource = null;
    String strSql = "BEGIN ORDERS.NP_ORDERS07_PKG.SP_RESERVE(?,?,?,?,?,?,?); END;";
    try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
      StructDescriptor sdItemResource = StructDescriptor.createDescriptor("ORDERS.TO_RESOURCE", conn);
      ArrayDescriptor  adItemResource = ArrayDescriptor.createDescriptor("ORDERS.TT_RESOURCE_LST", conn);
      for(int i=0; i<objArray.size(); i++){ 
        objHashMap = (HashMap)objArray.get(i);
        Object[] objItemResource = {	
          objHashMap.get("wv_ufmi_dn_num"), objHashMap.get("wn_ufmi_dn_id"),
          objHashMap.get("wn_ufmi_dn_type"), objHashMap.get("wv_ptn_dn_num"),
          objHashMap.get("wn_ptn_dn_id"), objHashMap.get("wn_ptn_dn_type"),
          objHashMap.get("wv_status"), objHashMap.get("wb_success")
        };
        stcItemResource = new STRUCT(sdItemResource, conn, objItemResource);
        arrItemResource.add(stcItemResource); 
      }
      ARRAY aryItemAgreement = new ARRAY(adItemResource, conn, arrItemResource.toArray());
      cstmt.setString(1, "333333330");//codigo APP
      cstmt.registerOutParameter(2, OracleTypes.ARRAY, "ORDERS.TT_RESOURCE_LST");
      cstmt.setARRAY(2, aryItemAgreement);
      cstmt.setInt(3,intSpecificationId);
      cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
      cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
      cstmt.registerOutParameter(6, OracleTypes.VARCHAR);
      cstmt.registerOutParameter(7, OracleTypes.VARCHAR);
      cstmt.executeUpdate();
      strMessage  = cstmt.getString(7);
      System.out.println("strMessage reserveGoldenNumber----->" + strMessage + "--");
      if (strMessage == null || (strMessage != null && "".equals(strMessage.trim()))){
        ARRAY aryResourceList = (ARRAY)cstmt.getObject(2);
        OracleResultSet adrs = (OracleResultSet) aryResourceList.getResultSet();
        while(adrs.next()) {
          stcResource = adrs.getSTRUCT(2);
          strSuccess = stcResource.getAttributes()[7] + "";
          if ("0".equals(strSuccess)) {
            strNumberNoAvailable = strNumberNoAvailable + stcResource.getAttributes()[3] + "\\n";
            successReserve=false;
          }
          System.out.println("El valor obtenido en wv_ptn_dn_num es ----->" + stcResource.getAttributes()[3]);
          System.out.println("El valor obtenido en wb_success es ----->" + stcResource.getAttributes()[7]);
          System.out.println("El valor obtenido en strNumberNoAvailable es ----->" + strNumberNoAvailable);
        }
        if (!successReserve){
          objHashMapResultado.put("strMessage",strNumberNoAvailable);  
          objHashMapResultado.put("strMessageError",strNumberNoAvailable);  
        }
      } else {
        objHashMapResultado.put("strMessage",strMessage);
      }
    }
    catch(Exception e){
      logger.error(formatException(e));              
      objHashMapResultado.put("strMessage", e.getMessage());
   }finally{
      try{
        closeObjectsDatabase(conn,cstmt,null); 
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    } 
    return objHashMapResultado;                                                                
  }

  public HashMap doValidateIMEICustomer(String strIMEI) throws Exception, SQLException {

		String strMessage = null;
    HashMap objHashMapResultado = new HashMap();
		Connection conn = null;
		OracleCallableStatement cstmt = null;

    try{
      String strSql = "BEGIN WEBSALES.SPI_VALIDATE_IMEI_CUSTOMER(?, ?, ?); END;";
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(strSql);

      cstmt.setString(1, strIMEI);
      cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
      cstmt.registerOutParameter(3, OracleTypes.VARCHAR);

      cstmt.executeUpdate();
      strMessage = cstmt.getString(3);

      if (strMessage == null){
        objHashMapResultado.put("objResult",cstmt.getString(2));
      }

    }catch(Exception e){
      logger.error(formatException(e));
      throw new Exception(e);
    }finally{
      try{
        closeObjectsDatabase(conn,cstmt,null);
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }
    
    objHashMapResultado.put(Constante.MESSAGE_OUTPUT,strMessage);
    return objHashMapResultado;
  }
  
  public String validateInbox(String strCurrentInbox) throws SQLException, Exception{

        String sReturnValue;
        Connection conn=null;
        String strMessage;
        OracleCallableStatement cstmt = null;
    try{
        String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_VALIDATE_IMBOX(?, ?, ?); END;";
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement) conn.prepareCall(strSql);

        cstmt.setString(1, strCurrentInbox);
        cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(3, OracleTypes.VARCHAR);

        cstmt.executeUpdate();
        strMessage = cstmt.getString(3);

        if (strMessage != null){
            sReturnValue = strMessage;
        }else{
            sReturnValue = cstmt.getString(2);
}

    }catch(Exception e){
        logger.error(formatException(e));
        throw new Exception(e);
    }finally{
        try{
            closeObjectsDatabase(conn,cstmt,null);
        }catch (Exception e) {
            logger.error(formatException(e));
        }
    }
    return sReturnValue;
}


    public String updItemDevice(ItemDeviceBean itemDeviceBean, Connection conn) throws Exception,SQLException {

        System.out.println("[ItemDAO] ORDERS.NP_ORDERS06_PKG.SP_UPD_ITEM_DEVICE");

        String strMensaje = null;
        OracleCallableStatement cstmt = null;

        logger.debug("[Input][itemDeviceBean]: " + itemDeviceBean.toString());

        System.out.println("[ItemDAO][updItemDevice] Npitemid: "+itemDeviceBean.getNpitemid());
        System.out.println("[ItemDAO][updItemDevice] Npitemid: "+itemDeviceBean.getNporderid());
        System.out.println("[ItemDAO][updItemDevice] Npitemid: "+itemDeviceBean.getNpcheckimei());

        try{
            String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_UPD_ITEM_DEVICE(?,?,?,?); END;";

            cstmt = (OracleCallableStatement)conn.prepareCall(strSql);

             cstmt.setLong(1, itemDeviceBean.getNpitemid());
            cstmt.setLong(2, itemDeviceBean.getNporderid());
            cstmt.setString(3, itemDeviceBean.getNpcheckimei());
            cstmt.registerOutParameter(4, Types.VARCHAR);
            cstmt.executeUpdate();
            strMensaje = cstmt.getString(4);
            logger.info("[Output][strMensaje]: " + strMensaje);

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
     * Motivo: Inserta cambios en el item de una orden de portabilidad
     * <br>Realizado por: <a href="mailto:luis.fer.huapaya@hpe.com">Luis Huapaya</a>
     * <br>Fecha: 09/12/2016
     * @param    ItemBean itemBean
     * @param    Connection conn
     */
    public String doInsChangeItemPort(ItemBean itemBean,Connection conn) throws Exception, SQLException {
        System.out.println("LHUAPAYA -> doInsChangeItemPort");
 
        OracleCallableStatement cstmt = null;

        String strMessage = null;
        int    intResultTransaction = 0,
                intOrderId = 0;

        long   itemId = 0;
        System.out.println("[Input][itemBean]: " + itemBean.toString());
        try{
            String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_INS_CHANGE_ITEM_PORT(?, ?, ?, ?, ?, ?, ?, ?, ?); END;";


            logger.info("[ORDERS.NP_ORDERS06_PKG.SP_INS_CHANGE_ITEM_PORT]");
            cstmt = (OracleCallableStatement)conn.prepareCall(strSql);

            cstmt.setLong(1, itemBean.getNporderid());

            cstmt.setLong(2,itemBean.getNpitemid());

            //Producto
            if( itemBean.getNpproductid() == 0 )
                cstmt.setNull(3, OracleTypes.NUMBER);
            else
                cstmt.setLong(3, itemBean.getNpproductid());

            cstmt.setString(4,itemBean.getNppricetype());
 
            if( itemBean.getNpprice().equals("") ) cstmt.setNull(5, OracleTypes.NUMBER);
            else  cstmt.setDouble(5, MiUtil.parseDouble(itemBean.getNpprice()));

            //Precio Excepción
            if( itemBean.getNppriceexception().equals("") )
                cstmt.setNull(6, OracleTypes.NUMBER);
            else
                cstmt.setDouble(6, MiUtil.parseDouble(itemBean.getNppriceexception()));

          //Plan Tarifario
          cstmt.setString(7,itemBean.getNpplanname());

            cstmt.setString(8, itemBean.getNpcreatedby());

            cstmt.registerOutParameter(9, Types.VARCHAR);

            cstmt.executeUpdate();

            strMessage = cstmt.getString(9);
            logger.info("[Output][strMessage]: " + strMessage);
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

}