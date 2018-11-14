package pe.com.nextel.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.*;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.DATE;
import oracle.sql.NUMBER;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import pe.com.nextel.bean.*;
import pe.com.nextel.bean.BudgetBean;
import pe.com.nextel.bean.CommentBean;
import pe.com.nextel.bean.CustomerBean;
import pe.com.nextel.bean.DominioBean;
import pe.com.nextel.bean.FinalSuspensionBean;
import pe.com.nextel.bean.ItemBean;
import pe.com.nextel.bean.ItemDeviceBean;
import pe.com.nextel.bean.LoadMassiveItemBean;
import pe.com.nextel.bean.ModalityBean;
import pe.com.nextel.bean.OrderBean;
import pe.com.nextel.bean.OrderContactBean;
import pe.com.nextel.bean.OrderDetailBean;
import pe.com.nextel.bean.PaymentBean;
import pe.com.nextel.bean.PortalSessionBean;
import pe.com.nextel.bean.SolutionBean;
import pe.com.nextel.bean.SuspensionReportsBean;
import pe.com.nextel.bean.TemplateAdendumBean;
import pe.com.nextel.form.OrderSearchForm;
import pe.com.nextel.form.PCMForm;
import pe.com.nextel.form.RetentionForm;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.Constante;
import pe.com.nextel.bean.FinalSuspensionBean;
import pe.com.nextel.bean.OrderDetailBean;
import pe.com.nextel.bean.SuspensionReportsBean;
import pe.com.nextel.bean.ProcCompMasBean;
import pe.com.nextel.bean.RepairBean;
import pe.com.nextel.bean.ServiciosBean;
import pe.com.nextel.form.RetentionForm;
import pe.com.nextel.form.PCMForm;

/**
 * Motivo: Clase DAO que contiene acceso a las tablas de Órdenes.
 * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>,
 * <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>,
 * <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
 * <br>Fecha: 28/08/2007
 * @see GenericDAO
 */
 
public class OrderDAO extends GenericDAO {
	
  /**
   Method : getSpecificationDate
   Purpose: Inserta una nueva orden.
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Lee Rosales     21/09/2007  Creación
   PHIDALGO        20/04/2010  Se agrega el parametro cliente de bscs (lngBscscustomerId)
   */

  public HashMap getSpecificationDate(long lngSpecificationId, String strBillcycle) throws Exception,SQLException {

    HashMap objHashMap = new HashMap();
    OracleCallableStatement cstmt = null;
    Connection conn=null; 
    String strMessage = null;
    String strFlgEnabled = "",
    strActivationDate = "";
    try{
      String sqlStr =  "BEGIN ORDERS.SPI_GET_SPECIFI_ACTIVAT_DATE(?, ?, ?, ?, ?); END;";
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      
      cstmt.setLong(1, lngSpecificationId);
      cstmt.setString(2, strBillcycle);
      cstmt.registerOutParameter(3, OracleTypes.DATE);
      cstmt.registerOutParameter(4, OracleTypes.NUMBER);
      cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
      
      cstmt.executeUpdate();
  
      strMessage = cstmt.getString(5);
      if( strMessage==null){
        strActivationDate = MiUtil.getDate(cstmt.getDate(3),"dd/MM/yyyy");
        strFlgEnabled     = cstmt.getString(4);
      }
                 
      objHashMap.put("strMessage",strMessage);
      objHashMap.put("strActivationDate",strActivationDate);
      objHashMap.put("strFlgEnabled",strFlgEnabled);  
    }catch(Exception e){
      objHashMap.put("strMessage",e.getMessage()); 
    }finally{
       try{
        closeObjectsDatabase(conn,cstmt,null); 
       }catch (Exception e) {
          logger.error(formatException(e));
       }
     }    
 
    return objHashMap;
  }

  /**
   Method : getFlagEmail
   Purpose: Obtener el flag de Email para desabilitar el link de Nro de Solicitud.
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   JTORRESC        09/12/2011  Creación   
   */
  public HashMap getFlagEmail(long lngSpecificationId, long lngHdnIUserId) throws Exception,SQLException { //jtorresc 09/12/2011
    HashMap objHashMap = new HashMap();
    OracleCallableStatement cstmt = null;
    Connection conn=null; 
    String strMessage = null;
    long lngFlgEnabledEmail = 0;    
    try{
      String sqlStr =  "BEGIN ORDERS.NP_ORDERS35_PKG.sp_get_flag_email(?, ?, ?,?); END;";
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, lngSpecificationId);
      cstmt.setLong(2, lngHdnIUserId);
      cstmt.registerOutParameter(3, OracleTypes.NUMBER);
      cstmt.registerOutParameter(4, OracleTypes.VARCHAR);      
      cstmt.executeUpdate();
      strMessage = cstmt.getString(4);
      if( strMessage==null){        
        lngFlgEnabledEmail = cstmt.getLong(3);
      }
      objHashMap.put("strMessage",strMessage);      
      objHashMap.put("lngFlgEnabledEmail", String.valueOf(lngFlgEnabledEmail));
    }catch(Exception e){
      objHashMap.put("strMessage",e.getMessage()); 
    }finally{
       try{
        closeObjectsDatabase(conn,cstmt,null); 
       }catch (Exception e) {
          logger.error(formatException(e));
       }
     }    
    return objHashMap;
  }

  /**
   Method : getResponsibleAreaList
   Purpose: Inserta una nueva orden.
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Lee Rosales     21/09/2007  Creación
  */

  public HashMap getResponsibleAreaList() throws Exception,SQLException {

    ArrayList list = new ArrayList();
    HashMap objHashMap = new HashMap();
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    Connection conn=null; 
    DominioBean objDominioBean = null;
    String strMessage = null;
    try{
      String sqlStr = "BEGIN CONTROL_EQUIPMENT.SPI_GET_RESPONSIBLE_AREA(?, ?); END;";
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
  
      cstmt.registerOutParameter(1, OracleTypes.CURSOR);
      cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
      
      cstmt.executeUpdate();
  
      strMessage = cstmt.getString(2);
      
      if( strMessage==null){      
        rs = (ResultSet)cstmt.getObject(1);
  
        while (rs.next()) {
          objDominioBean = new DominioBean();
          objDominioBean.setValor(rs.getString("wn_cod_area"));
          objDominioBean.setDescripcion(rs.getString("wv_nom_area"));
          list.add(objDominioBean); 
        }
      }
           
      objHashMap.put("strMessage",strMessage);
      objHashMap.put("objArrayList",list);  
    }catch(Exception e){
      objHashMap.put("strMessage",e.getMessage()); 
    }finally{
       try{
        closeObjectsDatabase(conn,cstmt,rs); 
       }catch (Exception e) {
          logger.error(formatException(e));
       }
     }    
    return objHashMap;
  }
	
  /**
   * Motivo: Obtener la Lista de Áreas Responsables.
   * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
   * [Creación]
   * <br>Modificado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
   * [Manejo de SQL Types]
   * <br>Fecha: 21/09/2007
   * @return    HashMap que contiene: Lista de Áreas, String vacío o que contiene el mensaje de error. 
  **/
  
  public HashMap getResponsibleAreaList(Connection conn) throws SQLException, Exception {
    HashMap hshDataMap = new HashMap();
    ArrayList arrResponsibleAreaList = new ArrayList();
    OracleCallableStatement cstmt = null;
    String strMessage = null;
    String strSql = "BEGIN CONTROL_EQUIPMENT.NP_CONTROL_EQUIPMENT02_PKG.SP_GET_RESPONSIBLE_AREA(?, ?); END;";
    try{
       cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
       cstmt.registerOutParameter(1, OracleTypes.ARRAY, "CONTROL_EQUIPMENT.TT_AREA_LST");
       cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
       cstmt.execute();
       strMessage = cstmt.getString(2);    
       if (strMessage == null){
          ARRAY aryResponsibleAreaList = (ARRAY)cstmt.getObject(1);
          OracleResultSet adrs = (OracleResultSet) aryResponsibleAreaList.getResultSet();
          while(adrs.next()) {
            STRUCT stcResponsibleArea = adrs.getSTRUCT(2);
            DominioBean objDominioBean = new DominioBean();
            objDominioBean.setValor(MiUtil.defaultString(stcResponsibleArea.getAttributes()[0], ""));
            objDominioBean.setDescripcion(MiUtil.defaultString(stcResponsibleArea.getAttributes()[1], ""));
            arrResponsibleAreaList.add(objDominioBean);
          }
       }else{
         throw new Exception(strMessage);
       }
    }   
    catch (Exception e) {
       throw new Exception(e);
    }
    finally{
       try{
        closeObjectsDatabase(null,cstmt,null); 
       }catch (Exception e) {
          logger.error(formatException(e));
       }
     }    
    hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
    hshDataMap.put("arrResponsibleAreaList", arrResponsibleAreaList);
    return hshDataMap;    
  }
    
  /**
   Method : getResponsibleDevList
   Purpose: Obtiene la lista de responsables de devolución
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Lee Rosales     21/09/2007  Creación
  **/

  public HashMap getResponsibleDevList() throws Exception,SQLException {

    ArrayList list = new ArrayList();
    HashMap objHashMap = new HashMap();
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    Connection conn=null; 
    DominioBean objDominioBean = null;
    String strMessage = null;
    try{
      String sqlStr = "BEGIN CONTROL_EQUIPMENT.SPI_GET_PROVIDERGRP_LST(?, ?); END;";
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      
      cstmt.registerOutParameter(1, OracleTypes.CURSOR);
      cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
      
      cstmt.executeUpdate();
  
      strMessage = cstmt.getString(2);
      if( strMessage==null){      
        rs = (ResultSet)cstmt.getObject(1);
        
        while (rs.next()) {
          objDominioBean = new DominioBean();
          objDominioBean.setValor(rs.getString("swprovidergrpid"));
          objDominioBean.setDescripcion(rs.getString("swname"));
          list.add(objDominioBean);
        }
      }
      
      closeObjectsDatabase(conn,cstmt,rs);
      objHashMap.put("strMessage",strMessage);
      objHashMap.put("objArrayList",list);
    }catch(Exception e){
      objHashMap.put("strMessage",e.getMessage()); 
    }finally{
       try{
        closeObjectsDatabase(conn,cstmt,rs); 
       }catch (Exception e) {
          logger.error(formatException(e));
       }
     }    

    return objHashMap;
  }


   /**
   Method : getModalityList
   Purpose: Inserta una nueva orden.
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Lee Rosales     21/09/2007  Creación
   */

  public HashMap getModalityList(long intSpecificationId, String strEquipment, String strWarrant, String strEquipmentReturn) throws Exception,SQLException {

    ArrayList list = new ArrayList();
    HashMap objHashMap = new HashMap();
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    Connection conn=null; 
    ModalityBean modalityBean = null;
    String strMessage = null;
    try{
      String sqlStr =  "BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_MODALITY_SPEC(?, ?, ?, ?, ?, ?); END;";
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
  
      cstmt.setLong(1, intSpecificationId);
      cstmt.setString(2, strEquipment);
      cstmt.setString(3, strWarrant);
      cstmt.setString(4, strEquipmentReturn);
      cstmt.registerOutParameter(5, OracleTypes.CURSOR);
      cstmt.registerOutParameter(6, OracleTypes.VARCHAR);
      
      cstmt.executeUpdate();
  
      strMessage = cstmt.getString(6);
      
      if( strMessage==null){      
        rs = (ResultSet)cstmt.getObject(5);
  
        while (rs.next()) {
          modalityBean = new ModalityBean();
          modalityBean.setNpmodality(rs.getString("npmodalitysell"));
          modalityBean.setNpstatus(rs.getString("npstatus"));
          list.add(modalityBean);
        }
      }      
      objHashMap.put("strMessage",strMessage);
      objHashMap.put("objArrayList",list);  
    }catch(Exception e){
      objHashMap.put("strMessage",e.getMessage()); 
    }finally{
       try{
        closeObjectsDatabase(conn,cstmt,rs); 
       }catch (Exception e) {
          logger.error(formatException(e));
       }
     }  
    
    
    return objHashMap;
  }

  /**
   * Motivo:  Inserta la orden
   * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
   * <br>Fecha: 22/10/2007
   * 
   * @param      OrderBean orderBean    
   * @param      Connection conn
   * @return     String strMessage 
  */
  public HashMap getOrderInsertar(OrderBean orderBean,Connection conn) throws Exception,SQLException{
    logger.info("************************** INICIO OrderDAO > getOrderInsertar**************************");
    HashMap objHashMap = new HashMap();
    String strMessage = null;          
    OracleCallableStatement cstmt = null;
    
    int intOrderId = 0;
    try{
    //AGAMARRA
    //Se obtiene el npsalesstructid del vendedor
    int npProviderGrpId = orderBean.getNpProviderGrpId();
    int npsalesstructid = getSalesStructId(npProviderGrpId);
    
    logger.info("[Input][OrderBean]: " + orderBean.toString());

    String strSql = "BEGIN ORDERS.NP_ORDERS05_PKG.SP_CREATE_ORDER(    ?,    ?,    ?,    ?,    ?,    ?,    ?, "   + //7
                                                                   "    ?,    ?,    ?,    ?,    ?,    ?,    ?, " + //14
                                                                   "    ?,    ?,    ?,    ?,    ?,    ?,    ?, " + //21
                                                                   "    ?,    ?,    ?,    ?,    ?,    ?,    ?, " + //28
                                                                   "    ?,    ?,    ?,    ?,    ?,    ?,    ?, " + //35
                                                                   "    ?,    ?,    ?,    ?,    ? ,   ?,    ?, " + //42
                                                                   "    ?,    ?,    ?,    ?,    ? ,   ?,    ?, " + //49
                                                                   "    ?,    ?,    ?,    ?,    ? ,   ?,    ?, " + //56
                                                                   "    ?,    ?,    ?,    ?,    ? ,   ?,    ?, " + //63
                                                                   "    ?,    ?,    ?,    ?,    ? ,   ? ," +//64 TDE  //69
                    "   ?,  " + // 70 homeservicezone - DOLANO-0002
                    "    ?,    ?,    ?); END;";  //73  (salida)
        
     
      logger.info("[Crea la Orden][NP_ORDERS05_PKG.SP_CREATE_ORDER]");
      
      cstmt = (OracleCallableStatement)conn.prepareCall(strSql); 
      
      cstmt.setLong(1, orderBean.getNpOrderId()); //01
      cstmt.setLong(2, orderBean.getNpCustomerId());
      
      if( orderBean.getNpSiteId() == 0 )
        cstmt.setNull(3, OracleTypes.NUMBER);
      else
        cstmt.setLong(3, orderBean.getNpSiteId());
      
      cstmt.setInt(4, orderBean.getNpProviderGrpId());
      cstmt.setString(5, orderBean.getNpSalesmanName());
      cstmt.setString(6, orderBean.getNpDealerName());      
      cstmt.setLong(7, orderBean.getNpDivisionId());
      cstmt.setInt(8, orderBean.getNpSpecificationId());
      cstmt.setLong(9, orderBean.getNpBuildingId());
      cstmt.setLong(10, orderBean.getNpRegionId());
      cstmt.setString(11, orderBean.getNpShipToAddress1());
      cstmt.setString(12, orderBean.getNpShipToAddress2());
      cstmt.setString(13, orderBean.getNpShipToCity());
      cstmt.setString(14, orderBean.getNpShipToProvince());
      cstmt.setString(15, orderBean.getNpShipToState());
      cstmt.setString(16, orderBean.getNpShipToZip());
      cstmt.setLong(17, orderBean.getNpDispatchPlaceId());
      if( orderBean.getNpCarrierId() == 0 )
        cstmt.setNull(18, OracleTypes.NUMBER);
      else
        cstmt.setLong(18, orderBean.getNpCarrierId());
      if (orderBean.getNpGeneratorType().equals(Constante.NAME_ORIGEN_MASSIVE)){
            orderBean.setNpGeneratorType(Constante.GENERATOR_TYPE_INC); 
          } 
          
      cstmt.setString(19, orderBean.getNpGeneratorType());
      if( orderBean.getNpGeneratorId()==0 )
      cstmt.setNull(20,OracleTypes.NUMBER);
      else
      cstmt.setLong(20, orderBean.getNpGeneratorId());      
      cstmt.setLong(21, orderBean.getNpTodoId());      
      cstmt.setString(22, orderBean.getNpOrigen());
      
      cstmt.setString(23, orderBean.getNpOrderCode());
      cstmt.setString(24, orderBean.getNpDescription());
      cstmt.setString(25, orderBean.getNpPaymentTerms());
      cstmt.setString(26, orderBean.getNpPaymentStatus());
      cstmt.setDate(27, orderBean.getNpPaymentFutureDate());
      cstmt.setDouble(28, orderBean.getNpPaymentTotal());
      cstmt.setTimestamp(29, orderBean.getNpSignDate());      
      cstmt.setDate(30, orderBean.getNpScheduleDate());
      
      cstmt.setDouble(31, orderBean.getNpExceptionApprove());                //40
      cstmt.setDouble(32, orderBean.getNpExceptionInstallation());
      cstmt.setDouble(33, orderBean.getNpExceptionPrice());
      cstmt.setDouble(34, orderBean.getNpExceptionPlan());
      cstmt.setDouble(35, orderBean.getNpExceptionWarrant());
      cstmt.setDouble(36, orderBean.getNpExceptionRevenue());
      cstmt.setDouble(37, orderBean.getNpExceptionRevenueAmount());
      cstmt.setString(38, orderBean.getNpExcepcionBillCycle());
      
      cstmt.setString(39, orderBean.getNpCreatedBy()); //Login
      
      cstmt.setDate(40, orderBean.getNpScheduleDate2());  //agregado por rmartinez para el registro de la fecha de reconexion
      //AGAMARRA
      if(npsalesstructid!=-1)
        cstmt.setInt(41, npsalesstructid);
      else
        cstmt.setInt(41, OracleTypes.NULL);
      
      if( orderBean.getNpproposedid() == 0 )            //CBARZOLA 30/07/2009
        cstmt.setNull(42, OracleTypes.NUMBER);
      else
        cstmt.setLong(42, orderBean.getNpproposedid());
      
        
      if ( orderBean.getNpProviderGrpIdData() == 0  )
        cstmt.setNull(43, OracleTypes.NULL);        
      else
        cstmt.setLong(43, orderBean.getNpProviderGrpIdData());
              
        cstmt.setNull(44, OracleTypes.NULL);  
        cstmt.setNull(45, OracleTypes.NULL);  
        cstmt.setLong(46, orderBean.getSalesStructureOriginalId());
    
      //INICIO: PRY-1200 | AMENDEZ
          cstmt.setInt(47, orderBean.getNpFlagVep());
          
      if(orderBean.getNpNumCuotas() == null){
        cstmt.setNull(48, OracleTypes.NULL);
      }else{
          cstmt.setInt(48, orderBean.getNpNumCuotas());
      }

      if(orderBean.getNpAmountVep() == null ){
        cstmt.setNull(49, OracleTypes.NULL);
      }else{
          cstmt.setDouble(49, orderBean.getNpAmountVep());
      }
      //INICIO: PRY-1200 | AMENDEZ

      cstmt.setInt(50, orderBean.getNpFlagCourier());
      cstmt.setLong(51, orderBean.getNpCustomerScoreId()); // [PPNN] MMONTOYA
      
      // Inicio [N_O000017567] MMONTOYA
      cstmt.setString(52, orderBean.getNpShipToReference());

      OrderContactBean orderContactBean = orderBean.getOrderContactBean();
      cstmt.setString(53, orderContactBean.getFirstName());
      cstmt.setString(54, orderContactBean.getLastName());
      cstmt.setString(55, orderContactBean.getDocumentType());
      cstmt.setString(56, orderContactBean.getDocumentNumber());
      cstmt.setString(57, orderContactBean.getPhoneNumber());
      // Fin [N_O000017567] MMONTOYA
      // Inicio [N_SD000349095] EFLORES
      cstmt.setString(58,orderBean.getNpCarpetaDigital());
      // Fin [N_SD000349095] EFLORES
      
      // Inicio [reason Change of model] CDIAZ
      if( orderBean.getNpReasonCdmId() > 0 ){
    	  cstmt.setInt(59, orderBean.getNpReasonCdmId());
      }else{
    	  cstmt.setNull(59, OracleTypes.NULL);      
      }
      // Fin [reason Change of model] CDIAZ


        //INICIO: PRY-0864 | AMENDEZ
      if(orderBean.getInitialQuota() == null ){
            cstmt.setNull(60, OracleTypes.NULL);
      }else{
        cstmt.setDouble(60, orderBean.getInitialQuota());
        }
        //FIN: PRY-0864  | AMENDEZ

      //INICIO DERAZO REQ-0940
      cstmt.setString(61, orderContactBean.getEmail());
      cstmt.setString(62, orderContactBean.getCheckNotification());
      //FIN DERAZO

      //INICIO: PRY-0980 | AMENDEZ
      if(orderBean.getNpPaymentTermsIQ() == null ){
        cstmt.setNull(63, OracleTypes.NULL);
      }else{
      cstmt.setInt(63,orderBean.getNpPaymentTermsIQ());
      }
      //FIN: PRY-0980 | AMENDEZ

      // Ini: [TDECONV003] KOTINIANO
      cstmt.setString(64, orderBean.getNpFlagMigracion());
      // Fin: [TDECONV003] KOTINIANO

      //INICIO: EST-1098 | AMENDEZ
      cstmt.setString(65, orderBean.getNpuseinfulladdress());
      cstmt.setString(66, orderBean.getNpdepartmentuseaddress());
      cstmt.setString(67, orderBean.getNpprovinceuseaddress());
      cstmt.setString(68, orderBean.getNpdistrictuseaddress());
      cstmt.setInt(69, orderBean.getNpflagcoverage());
      //FIN: EST-1098 | AMENDEZ

            // BEGIN: PRY-1049 | DOLANO-0002
            cstmt.setString(70, orderBean.getVchHomeServiceZone());
            // END: PRY-1049 | DOLANO-0002

            cstmt.registerOutParameter(71, OracleTypes.NUMBER);  // setNpOrderId
            cstmt.registerOutParameter(72, OracleTypes.VARCHAR); // setNpStatus
            cstmt.registerOutParameter(73, OracleTypes.VARCHAR); // strMessage
      
      cstmt.executeUpdate();
             
            // BEGIN: PRY-1049 | DOLANO-0002 
            strMessage = cstmt.getString(73);
      
      if( strMessage == null ){
                orderBean.setNpOrderId(cstmt.getLong(71));
                orderBean.setNpStatus(cstmt.getString(72));
      }
            // END: PRY-1049 | DOLANO-0002
      
      logger.info("[Output][setNpOrderId]: " + orderBean.getNpOrderId());
      logger.info("[Output][setNpStatus]: " + orderBean.getNpStatus());
            
      objHashMap.put("strMessage",strMessage);
      objHashMap.put("objOrderBean",orderBean);  
      
    }catch(Exception e){
      e.printStackTrace();
      objHashMap.put("strMessage",e.getMessage());
      objHashMap.put("objOrderBean",orderBean);
    }finally{
       try{
        closeObjectsDatabase(null,cstmt,null); 
       }catch (Exception e) {
          logger.error(formatException(e));
       }
     }
    logger.info("************************** FIN OrderDAO > getOrderInsertar**************************");
    return objHashMap;
  }
  
  /**
   Method : getPersonBuilding
   Purpose: Obtener los valores de la tienda y Region de tramite
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   David Coca      06/07/2007  Creación
  **/
  
  public static ArrayList getPersonBuilding(int intPersonid) throws Exception,SQLException {
    
    ArrayList list = new ArrayList();
      
    Connection conn = null; 
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    
    String strMensaje = null;
      
    String sqlStr = "BEGIN ORDERS.NP_ORDERS01_PKG.SP_GET_PERSON_BUILDING(?, ?, ?); END;";
      
    try {
      
      conn = Proveedor.getConnection();
        
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      
      cstmt.setInt(1, intPersonid);
      cstmt.registerOutParameter(2, Types.CHAR);
      cstmt.registerOutParameter(3, OracleTypes.CURSOR);
        
      cstmt.executeUpdate();
      
      strMensaje = cstmt.getString(2); 
      
      if( strMensaje == null ){
        //rs = (ResultSet)cstmt.getObject(3);
        rs = (ResultSet)cstmt.getObject(3);
       
        while (rs.next()) {
          Hashtable h = new Hashtable(); 
          h.put("wn_npuserid",rs.getInt(1)+"");
          h.put("wv_npnombre",rs.getString(2)==null?"":rs.getString(2));
          h.put("wv_npapellido",rs.getString(3)==null?"":rs.getString(3));
          h.put("wv_nplogin",rs.getString(4)==null?"":rs.getString(4));   
          h.put("wn_npbuildingid",rs.getInt(5)+"");           
          h.put("wn_npcode",rs.getInt(6)+"");        
          h.put("wv_npname",rs.getString(7)==null?"":rs.getString(7)); 
          h.put("wn_npactive",rs.getInt(8)+""); 
          h.put("wn_npregionid",rs.getInt(9)+""); 
          h.put("wv_nptype",rs.getString(10)==null?"":rs.getString(10)); 
          h.put("wv_npshortname",rs.getString(11)==null?"":rs.getString(11)); 
          h.put("wv_npprocessgroup",rs.getString(12)==null?"":rs.getString(12)); 
          h.put("wv_mensaje","");    
          list.add(h);
        }
      }else{
        throw new Exception(strMensaje);
      }
      
    } catch(Exception e){
      logger.error(formatException(e));
      System.out.println("getPersonBuilding nError Nro.: "+e.getClass() + " nMensaje:   -- >" + e.getMessage()+"\n");
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
   Method : getSolutionList
   Purpose: Obtener los valores de la tienda y Region de tramite
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   David Coca     11/07/2007  Creación
   Cesar Barzola 20/07/2009   Modificacion  
  **/
  public static ArrayList getSolutionList(String strMessage)  throws Exception, SQLException{
    
    ArrayList list = new ArrayList();
    Connection conn = null; 
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    
    String strMensaje = null;
    try {
      String sqlStr = "BEGIN PRODUCT.SPI_GET_SOLUTION_LST(?, ?); END;";                      
      conn = Proveedor.getConnection();          
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);        
     //cstmt.setString(1,strMessage);
      cstmt.registerOutParameter(1,OracleTypes.VARCHAR);
      cstmt.registerOutParameter(2, OracleTypes.CURSOR );
      cstmt.executeUpdate();    
      strMensaje = cstmt.getString(1); 
      if(strMensaje == null ){
        rs = (ResultSet)cstmt.getObject(2);      
        while (rs.next()) {
          Hashtable h = new Hashtable();
          h.put("wn_npsolutionid",rs.getInt(3)+"");
          h.put("wv_npname",rs.getString(4)==null?"":rs.getString(4));
          h.put("wv_mensaje","");
          list.add(h);
        }
      }    
    }catch(Exception e){
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
   Method : getCategoryList
   Purpose: Obtener la lista de categoria
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   David Coca     11/07/2007  Creación
  **/
  
  public static ArrayList getCategoryList(int intSolutionId)  throws Exception, SQLException{
      
    ArrayList list = new ArrayList();
    Connection conn = null; 
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    
    String strMensaje = null;
    try{
      String sqlStr = "BEGIN ORDERS.NP_ORDERS01_PKG.SP_GET_CATEGORY_LST(?, ?, ?); END;";  
      conn = Proveedor.getConnection();        
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);      
      cstmt.setInt(1,intSolutionId);
      cstmt.registerOutParameter(2, Types.CHAR);
      cstmt.registerOutParameter(3, OracleTypes.CURSOR );        
      cstmt.executeUpdate();
      
      strMensaje = cstmt.getString(2); 
      
      if( strMensaje == null ){      
        rs = (ResultSet)cstmt.getObject(3);        
        while (rs.next()) {
          Hashtable h = new Hashtable(); 
          String val = rs.getString(1);
          h.put("wv_npType",val);
          h.put("wv_message",strMensaje + ""); 
          list.add(h);
        }
      }
    }catch(Exception e){
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
   Method : getSubCategoryList
   Purpose: Obtener la lista de categoria
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Lee Rosales     07/09/2007  Creación
  **/
  
  public static ArrayList getSubCategoryList(String strCategory) throws Exception, SQLException{
    
    ArrayList list = new ArrayList();
    Connection conn = null; 
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    String strMensaje = null;
    try{
      String sqlStr = "BEGIN ORDERS.NP_ORDERS01_PKG.SP_GET_SUBCATEGORY_LST(?, ?, ?); END;";
    
      conn = Proveedor.getConnection();
          
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      
      cstmt.setString(1,strCategory);
      cstmt.registerOutParameter(2, Types.CHAR);
      cstmt.registerOutParameter(3, OracleTypes.CURSOR );
        
      cstmt.executeUpdate();
      
      strMensaje = cstmt.getString(2);
      
      if( strMensaje == null ){      
        rs = (ResultSet)cstmt.getObject(3);        
        while (rs.next()) {
          Hashtable h = new Hashtable(); 
          h.put("wn_npSpecificationId",rs.getInt(1)+"");
          h.put("wv_npSpecification",rs.getString(2));
          h.put("wv_message",strMensaje + "");
          list.add(h);
        }
      }  
    }catch(Exception e){
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
    
    /*
     Method : getModePaymentList
     Purpose: Obtener las formas de pago
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     David Coca     11/07/2007  Creación
     */
    public static ArrayList getModePaymentList(String strParamName, String strParamStatus) throws Exception, SQLException {
      
      ArrayList list = new ArrayList();
        
      Connection conn = null; 
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;
      
      String strMensaje = null;
      try{
        String sqlStr = "BEGIN WEBSALES.SPI_GET_PARAMS_LIST(?, ?, ?, ?); END;";                  
        conn = Proveedor.getConnection();        
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);        
        cstmt.setString(1, strParamName);
        cstmt.setString(2, strParamStatus);
            
        cstmt.registerOutParameter(3, OracleTypes.CURSOR );
        cstmt.registerOutParameter(4, Types.CHAR);
          
        cstmt.executeUpdate();
        strMensaje = cstmt.getString(4); 
        if (strMensaje == null){
           rs = (ResultSet)cstmt.getObject(3);                            
           while (rs.next()) {    
              Hashtable h = new Hashtable();           
              h.put("wv_npValue",rs.getString(1));
              h.put("wv_npValueDesc",rs.getString(2));
              h.put("wv_npTag1",rs.getString(3));
              h.put("wv_npTag2",rs.getString(4));
              h.put("wn_npOrder",rs.getInt(5)+"");
              h.put("wv_message",strMensaje + "");                 
              list.add(h);          
           }
        }                              
         
      }catch(Exception e){
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
    
    /*
     Method : getSalesList
     Purpose: Obtener las formas de pago
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     David Coca     11/07/2007  Creación
     */
    public static ArrayList getSalesList(int intUserId, int intAppId) throws Exception, SQLException{
      
      ArrayList list = new ArrayList();
        
      Connection conn = null; 
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;
      
      String strMensaje = null;
      try{
        String sqlStr = "BEGIN ORDERS.NP_ORDERS01_PKG.SP_GET_SALES_LIST(?, ?, ?, ?); END;";            
        conn = Proveedor.getConnection();          
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        
        cstmt.setInt(1, intUserId);
        cstmt.setInt(2, intAppId);
            
        cstmt.registerOutParameter(3, Types.CHAR);
        cstmt.registerOutParameter(4, OracleTypes.CURSOR );
            
        cstmt.executeUpdate();
          
        strMensaje = cstmt.getString(3); 
        if (strMensaje == null){
           rs = (ResultSet)cstmt.getObject(4);            
           int i =0;
           while (rs.next() && i < 20) {    
              i++;
              Hashtable h = new Hashtable();           
              h.put("wn_npCampo01",rs.getInt(1)+"");
              h.put("wv_npCampo02",rs.getString(2)+"");
              h.put("wv_npCampo03",rs.getString(3)+"");
              h.put("wv_message",strMensaje + "");            
              list.add(h);          
          }   
        }
      }catch(Exception e){
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
     Method : getOrderIdNew
     Purpose: Obteniene el nuevo order id.
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Lee Rosales     21/07/2007  Creación
     */
     
    public static Hashtable getOrderIdNew() throws Exception, SQLException{
      
      Hashtable h = new Hashtable();
        
      Connection conn = null; 
      OracleCallableStatement cstmt = null;      
      String strMensaje = null;
      int intOrderId = 0;        
      String sqlStr = "BEGIN ORDERS.NP_ORDERS01_PKG.SP_GET_ORDERID_NEW( ?, ?); END;";        
      try {        
          conn = Proveedor.getConnection();          
          cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);          
          cstmt.registerOutParameter(1, Types.CHAR);
          cstmt.registerOutParameter(2, Types.NUMERIC);          
          cstmt.executeUpdate();
          strMensaje = cstmt.getString(1); 
          if (strMensaje == null){
             intOrderId = cstmt.getInt(2);           
             h.put("wn_orderid",intOrderId + "");   
             h.put("wv_message",strMensaje + "");   
          }
          cstmt.close();
          conn.close();
          
      } catch (SQLException e) {
        System.out.println("getOrderIdNew nError Nro.: "+e.getErrorCode() + " nMensaje:   -- >" + e.getMessage()+"\n");
        System.out.println("e.getErrorCode()  : "+e.getErrorCode() );
        System.out.println("strMensaje  : "+ strMensaje );
        h.put("wv_message",e.getMessage());         
      } 
      finally {
         try{
          closeObjectsDatabase(conn,cstmt,null); 
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
      return h;
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
   * Motivo: Obteniene el detalle de la Orden
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 11/07/2007
   * @param     lNpOrderid     Es el Id de la Orden       
   * @return    HashMap 
   */
   public HashMap getOrder(long lNpOrderid) throws Exception, SQLException {
      HashMap hshRetorno=new HashMap();
      String strMessage=null;
      OracleCallableStatement cstmt = null;     
      Connection conn=null;
      ResultSet rs = null;
      OrderBean orbResume = new OrderBean();
      CustomerBean csbCustomer = new CustomerBean();
      conn = Proveedor.getConnection();
      
      try{
        String strSql = "BEGIN NP_ORDERS01_PKG.SP_GET_ORDER_DETAIL( ? , ? , ? ); end;";        
		  
        cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
        cstmt.setLong(1, lNpOrderid);
        cstmt.registerOutParameter(2, Types.CHAR);
        cstmt.registerOutParameter(3, OracleTypes.CURSOR);
        cstmt.execute();
        strMessage = cstmt.getString(2);
        if (strMessage == null){
           rs = (ResultSet)cstmt.getObject(3);
           if (rs.next()) {
              orbResume.setNpOrderCode(rs.getString("npOrderCode")); //id solicitud
              orbResume.setNpType(rs.getString("npType")); //categoria
              orbResume.setNpSpecification(rs.getString("npSpecification")); //subcategoria  
              orbResume.setNpCreatedDate(rs.getTimestamp("npCreatedDate")); // Fecha de creacion
              orbResume.setNpOrderId(rs.getInt("npOrderId")); // Orden ID                
              orbResume.setNpStatus(rs.getString("npStatus")); // Estado de la orden                                
              orbResume.setNpSalesmanName(rs.getString("npSalesmanName")); 
              orbResume.setNpDealerName(rs.getString("npDealerName")); 
              orbResume.setNpPaymentStatus(rs.getString("npPaymentStatus"));
              orbResume.setNpPaymentFutureDate(rs.getDate("npPaymentFutureDate")); 
              orbResume.setNpPaymentDate(rs.getDate("npPaymentDate"));  
              orbResume.setNpDescription(rs.getString("npDescription")); 
              orbResume.setNpScheduleDate(rs.getDate("npScheduleDate")); 
              orbResume.setNpScheduleStatus(rs.getInt("npScheduleStatus"));
              //orbResume.setNpSolutionName(rs.getString("npname")); 
              //orbResume.setNpSolutionId(rs.getInt("npsolutionid")); 
              orbResume.setNpSignDate(rs.getTimestamp("npSignDate")); 
              orbResume.setNpWorkFlowId(rs.getInt("npworkflowid")); 
              orbResume.setNpSpecificationId(rs.getInt("npSpecificationId")); 
              orbResume.setNpDeliveryDate(rs.getTimestamp("npDeliveryDate")); 
              orbResume.setNpCreatedBy(rs.getString("npCreatedBy"));
              orbResume.setNpExceptionApprove(rs.getInt("npExceptionApprove")); 
              orbResume.setNpPaymentTerms(rs.getString("npPaymentTerms"));
              orbResume.setNpCreatedDate(rs.getTimestamp("npCreatedDate"));  
              orbResume.setNpPaymentTotal(rs.getDouble("npPaymentTotal")); 
              orbResume.setNpPaymentAmount(rs.getDouble("npPaymentAmount")); 
              orbResume.setNpCarrierId(rs.getInt("npCarrierId")); 
              orbResume.setNpSiteId(rs.getInt("npsiteid")); 
              orbResume.setNpCompanyType(rs.getString("npCompanyType"));
              orbResume.setNpProviderGrpId(rs.getInt("npProviderGrpId"));  
              orbResume.setNpDispatchPlaceId(rs.getInt("npdispatchplaceid")); 
              orbResume.setNpBpelconversationid(rs.getString("npbpelconversationid")); 
              orbResume.setNpBpelinstanceid(rs.getInt("npbpelinstanceid"));
              orbResume.setNpBpelbackinboxs(rs.getString("npbpelbackinboxs"));            
              orbResume.setNpUserName1(rs.getString("npusername1")); //npusername1
              orbResume.setNpCarrierName(rs.getString("npnamecarrier"));
              orbResume.setNpTimeStamp(rs.getInt("npTimeStamp")); //npTimeStamp
              csbCustomer.setSwCustomerId(rs.getInt("npcustomerid")); //id del cliente         
              orbResume.setCsbCustomer(csbCustomer);
              orbResume.setNpBeginPeriod(rs.getString("beginperiod"));
              orbResume.setNpEndPeriod(rs.getString("endperiod"));
              orbResume.setNpExcepcionBillCycle(rs.getString("npexcepcionbillcycle"));
              orbResume.setNpRegionId(rs.getLong("npregionid"));
              orbResume.setNpShipToAddress1(rs.getString("npshiptoaddress1")); 
              orbResume.setNpShipToCity(rs.getString("npshiptocity")); 
              orbResume.setNpShipToProvince(rs.getString("npshiptoprovince"));  
              orbResume.setNpShipToState(rs.getString("npshiptostate"));  
              orbResume.setNpguidegenerated(rs.getString("npguidegenerated"));         
              orbResume.setNpGeneratorType(rs.getString("npgeneratortype"));
              orbResume.setNpGeneratorId(rs.getLong("npgeneratorid"));
              orbResume.setNpBuildingId(rs.getLong("npbuildingid")); //Aumentado por Karen Salvador -- Para Categorias 2028,2029,2030,2031
				      orbResume.setNpstatustmp(rs.getString("npstatustmp"));
				      orbResume.setNpShipToAddress2(rs.getString("npshiptoaddress2"));
              orbResume.setNpDivisionId(rs.getInt("npdivisionid"));
              orbResume.setNpDivisionName(rs.getString("npdivisionname"));
              orbResume.setNpScheduleDate2(rs.getDate("npScheduleDate2"));  // agregado por rmartinez 16-06-2009 - Para Categoria Suspensiones 2062
              orbResume.setNpproposedid(rs.getLong("npproposedid"));//CBARZOLA 27/07/2009
              orbResume.setNpProcesoAutom(rs.getString("npprocesoautom"));//GGRANADOS
              orbResume.setSalesStructureOriginalId(rs.getInt("npsalesstructorigenid"));
              orbResume.setNpFlagVep(rs.getInt("npvep"));
              orbResume.setNpNumCuotas(rs.getInt("npvepquantityquota"));
              orbResume.setNpAmountVep(rs.getDouble("npvepaymenttotal"));
              orbResume.setNpVoucher(rs.getString("npvoucher"));
              orbResume.setNpOrigen(rs.getString("nporigen"));
              //
              //[Despacho en Tienda] PCASTILLO
              orbResume.setNpFlagCourier(rs.getInt("npcourier"));
              

              if( rs.getString("npinvoicesgenerated")==null || rs.getString("npinvoicesgenerated").equals("null")){
                  orbResume.setNpInvoicesGenerated("null");
              }
              else{
                  orbResume.setNpInvoicesGenerated(rs.getString("npinvoicesgenerated"));
              }

              orbResume.getDocVerificationBean().setResultDesc(rs.getString("docverifresultdesc") != null ? rs.getString("docverifresultdesc") : ""); // [PPNN] MMONTOYA                                                
              orbResume.setNpCustomerScoreId(rs.getLong("npcustomerscoreid")); // [PPNN] MMONTOYA

              // Inicio [N_O000017567] MMONTOYA
              orbResume.setNpShipToReference(rs.getString("npshiptoreference"));

              OrderContactBean orderContactBean = new OrderContactBean();
              orderContactBean.setFirstName(rs.getString("npcontactfirstname"));
              orderContactBean.setLastName(rs.getString("npcontactlastname"));
              orderContactBean.setDocumentType(rs.getString("npcontactdoctype"));
              orderContactBean.setDocumentNumber(rs.getString("npcontactdocnumber"));
              orderContactBean.setPhoneNumber(rs.getString("npcontactphonenumber"));
              //INICIO DERAZO REQ-0940
              orderContactBean.setEmail(rs.getString("npemail"));
              orderContactBean.setCheckNotification(rs.getString("npnotification"));
              //FIN DERAZO
              orbResume.setOrderContactBean(orderContactBean);
              // Fin [N_O000017567] MMONTOYA
              // Inicio [N_SD000349095] EFLORES
              orbResume.setNpCarpetaDigital(rs.getString("npcarpetadigital"));
              // Fin
              // Inicio [reason Change of model] CDIAZ
              orbResume.setNpReasonCdmId(rs.getInt("npreasoncdmid"));
              orbResume.setNpReasonCdmName(rs.getString("npreasoncdmname") != null ? rs.getString("npreasoncdmname") : "");
              // Fin [reason Change of model] CDIAZ

             //INICIO: PRY-0864 | AMENDEZ
             orbResume.setInitialQuota(rs.getDouble("npinitialquota"));
             logger.info("[[OrderDAO]] [getInitialQuota] : "  + orbResume.getInitialQuota());
             //FIN: PRY-0864 | AMENDEZ

             //[INICIO] JCURI PRY-0890
              System.out.println("[[nporderparentid]] " + rs.getString("nporderparentid") + "[[nporderchildid]] " + rs.getString("nporderchildid") + "[npstatuschildprorrateo] " + rs.getString("npstatuschildprorrateo"));
              if(rs.getString("nporderchildid") != null && "a".equals(rs.getString("npstatuschildprorrateo")) ) {
            	  orbResume.setNpProrrateo("S");
              }
              orbResume.setNpOrderParentId(rs.getString("nporderparentid") != null ? rs.getString("nporderparentid") : "");          
              orbResume.setNpOrderChildId(rs.getString("nporderchildid") != null ? rs.getString("nporderchildid") : "");
              orbResume.setNpPaymentTotalProrrateo(rs.getString("nppaymenttotalprorrateo") != null ? rs.getString("nppaymenttotalprorrateo") : "");
              System.out.println("[[OrderDAO]] [strProrrateo] "+ orbResume.getNpProrrateo() +" [getNpOrderparentId] " +orbResume.getNpOrderParentId()+" [nporderchildid] "+orbResume.getNpOrderChildId()+" [nppaymenttotalprorrateo] "  + orbResume.getNpPaymentTotalProrrateo());
              //[FIN] JCURI PRY-0890

              //INICIO: PRY-0980 | AMENDEZ
               orbResume.setNpPaymentTermsIQ(rs.getInt("nppaymenttermsiq"));
               logger.info("[[OrderDAO]] [getNpPaymentTermsIQ] : "  + orbResume.getNpPaymentTermsIQ());
              //FIN: PRY-0980 | AMENDEZ

	      // Ini: TDECONV003 KOTINIANO
              orbResume.setNpFlagMigracion(rs.getString("npflagmigracion"));
              // Fin: TDECONV003 KOTINIANO

             //INICIO: EST-1098 | AMENDEZ
             orbResume.setNpuseinfulladdress(rs.getString("useinfulladdress"));
             orbResume.setNpprovinceuseaddress(rs.getString("provinceuseaddress"));
             orbResume.setNpdepartmentuseaddress(rs.getString("departmentuseaddress"));
             orbResume.setNpdistrictuseaddress(rs.getString("districtuseaddress"));
             orbResume.setNpflagcoverage(rs.getInt("flagcoverage"));
             orbResume.setNpzonacoberturaid(rs.getInt("npzonacoberturaid"));
             orbResume.setNpdistrictzoneid(rs.getInt("npdistrictzoneid"));
             orbResume.setNpprovincezoneid(rs.getInt("npprovincezoneid"));
             //FIN: EST-1098 | AMENDEZ

            // BEGIN: PRY-1140 | DOLANO-0003
            orbResume.setVchHomeServiceZone(rs.getString("homeservicezone"));
            orbResume.setVchHomeServiceZoneDesc(rs.getString("homeservicezonedesc"));
            // END: PRY-1140 | DOLANO-0003
               
            // INICIO PRY-1239 21/09/2018 PCACERES               
            orbResume.setVchWebPayment(rs.getString("vchWebPayment"));
            // FIN PRY-1239 21/09/2018 PCACERES
           }
        }                   
        hshRetorno.put("objResume",orbResume);
        hshRetorno.put("strMessage",strMessage);        
      }catch(Exception e){
         hshRetorno.put("strMessage",e.getMessage()); 
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,rs); 
          }catch (Exception e) {
            logger.error(formatException(e));
          }
      }   
      return hshRetorno;
   }
   
   /**
   * Motivo: Actualiza los datos de una orden
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 05/09/2007
   * @param     objOrder          Objeto que contiene los datos de la Orden                      
   * @return    String 
   */   
   public  String updOrder(OrderBean objOrder,Connection conn) throws Exception, SQLException {

      logger.info("************************ INICIO OrderDAO > updOrder************************");
      OracleCallableStatement cstmt = null;        
      String strMessage=null;
      logger.info("[OrderDao][Dealer]"+objOrder.getNpDealerName());
      logger.debug("[Input][objOrder]: " + objOrder.toString());
      
        try{
            if((objOrder.getNpSpecificationId() == 2068) || (objOrder.getNpSpecificationId() == 2069) ){
                java.util.Date fechaInicio = new java.util.Date();
              logger.info("[ORDERDAO][updOrder][PM0010354] : INICIO");
              logger.info("[ORDERDAO][updOrder][PM0010354] : OrderId -->" +objOrder.getNpOrderId());
              logger.info("[ORDERDAO][updOrder][PM0010354] : Fecha -->" + fechaInicio);
            }
        }catch(Exception e){
            logger.error(formatException(e));
            strMessage = e.getMessage();
          logger.error("[ORDERDAO][updOrder][PM0010354] : ERROR AL INTENTAR PINTAR LOG INICIO");
        }

      //AGAMARRA
      //Obtención del SalesStructId
      int npProviderGrpId = objOrder.getNpProviderGrpId();
      int npsalesstructid = getSalesStructId(npProviderGrpId);
    
      //[Despacho en Tienda] PCASTILLO
      int npFlagCourier = objOrder.getNpFlagCourier();
      
      try{
         String strSql =  "BEGIN ORDERS.NP_ORDERS07_PKG.SP_UPD_ORDER( ?, ?, ?, ?, ?, " +//5         
                                                                     "?, ?, ?, ?, ?, " +//10
                                                                     "?, ?, ?, ?, ?, " +//15
                                                                     "?, ?, ?, ?, ?, " +//20
                                                                     "?, ?, ?, ?, ?, " +//25
                                                                     "?, ?, ?, ? ,?, " +//30
                                                                     "?, ?, ?, ?, ?, " +//35
                                                                     "?, ?, ?, ?, ?, " +//40
                                                                     "?, ?, ?); END;";  //43 (42-TDE)

        
        logger.info("[ORDERS.NP_ORDERS07_PKG.SP_UPD_ORDER]");
        //BUSCAR ERROR
        logger.info("[OrderDAO] updOrder : getNpOrderId -->" +objOrder.getNpOrderId());
        logger.info("[OrderDAO] updOrder : getNpProviderGrpId -->" +objOrder.getNpProviderGrpId());
        logger.info("[OrderDAO] updOrder : getNpProviderGrpId -->" +objOrder.getNpProviderGrpId());
        logger.info("[OrderDAO] updOrder : getNpSalesmanName -->" +objOrder.getNpSalesmanName());
        logger.info("[OrderDAO] updOrder : getNpDispatchPlaceId -->" +objOrder.getNpDispatchPlaceId());
        logger.info("[OrderDAO] updOrder : getNpCarrierId -->" +objOrder.getNpCarrierId());
        logger.info("[OrderDAO] updOrder : getNpOrderCode -->" +objOrder.getNpOrderCode());
        logger.info("[OrderDAO] updOrder : getNpDescription -->" +objOrder.getNpDescription());
        logger.info("[OrderDAO] updOrder : getNpPaymentTerms -->" +objOrder.getNpPaymentTerms());
        logger.info("[OrderDAO] updOrder : getNpPaymentAmount -->" +objOrder.getNpPaymentAmount());
        logger.info("[OrderDAO] updOrder : getNpPaymentFutureDate -->" +objOrder.getNpPaymentFutureDate());
        logger.info("[OrderDAO] updOrder : getNpSignDate -->" +objOrder.getNpSignDate());
        logger.info("[OrderDAO] updOrder : getNpScheduleDate -->" +objOrder.getNpScheduleDate());
        logger.info("[OrderDAO] updOrder : getNpModificationBy -->" +objOrder.getNpModificationBy());
        logger.info("[OrderDAO] updOrder : getNpModificationDate -->" +objOrder.getNpModificationDate());
        logger.info("[OrderDAO] updOrder : getNpTimeStamp -->" +objOrder.getNpTimeStamp());
        logger.info("[OrderDAO] updOrder : getNpShipToAddress1 -->" +objOrder.getNpShipToAddress1());
        logger.info("[OrderDAO] updOrder : getNpShipToCity -->" +objOrder.getNpShipToCity());
        logger.info("[OrderDAO] updOrder : getNpShipToProvince -->" +objOrder.getNpShipToProvince());
        logger.info("[OrderDAO] updOrder : getNpShipToState -->" +objOrder.getNpShipToState());
        logger.info("[OrderDAO] updOrder : getNpDeliveryDate -->" +objOrder.getNpDeliveryDate());
        logger.info("[OrderDAO] updOrder : getNpBuildingId -->" +objOrder.getNpBuildingId());
        logger.info("[OrderDAO] updOrder : getNpUserName1 -->" +objOrder.getNpUserName1());
        logger.info("[OrderDAO] updOrder : getNpDealerName -->" +objOrder.getNpDealerName());
        logger.info("[OrderDAO] updOrder : getNpScheduleDate2 -->" +objOrder.getNpScheduleDate2());
        logger.info("[OrderDAO] updOrder : npFlagCourier -->" +objOrder.getNpFlagCourier());
        logger.info("[OrderDAO] updOrder : getNpCarpetaDigital -->" +objOrder.getNpCarpetaDigital());
        logger.info("[OrderDAO] updOrder : getInitialQuota -->" +objOrder.getInitialQuota());
        //INICIO: PRY-0980 | AMENDEZ
        logger.info("[OrderDAO] updOrder : getNpPaymentTermsIQ -->" +objOrder.getNpPaymentTermsIQ());
        //FIN: PRY-0980 | AMENDEZ
	    logger.info("[OrderDAO] updOrder : getNpFlagMigracion -->" +objOrder.getNpFlagMigracion());
        logger.info("[OrderDAO] npsalesstructid :  -->" +npsalesstructid);
        //INICIO DERAZO REQ-0940
        logger.info("[OrderDAO] updOrder : getOrderContactBean -->" + objOrder.getOrderContactBean().toString());
        //FIN DERAZO
            
        cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
        cstmt.setLong(1, objOrder.getNpOrderId());
        cstmt.setInt(2, objOrder.getNpProviderGrpId());
        cstmt.setString(3, objOrder.getNpSalesmanName());           
        cstmt.setLong(4, objOrder.getNpDispatchPlaceId());
        cstmt.setLong(5, objOrder.getNpCarrierId());    
        cstmt.setString(6, objOrder.getNpOrderCode());  
        cstmt.setString(7, objOrder.getNpDescription());             
        cstmt.setString(8, objOrder.getNpPaymentTerms());                    
        cstmt.setDouble(9, objOrder.getNpPaymentAmount());                                         
        cstmt.setDate(10, objOrder.getNpPaymentFutureDate());       
        cstmt.setTimestamp(11, objOrder.getNpSignDate());
        cstmt.setDate(12, objOrder.getNpScheduleDate());    
        cstmt.setString(13, objOrder.getNpModificationBy());
        cstmt.setDate(14, objOrder.getNpModificationDate());  
        cstmt.setInt(15, objOrder.getNpTimeStamp());       
     
        cstmt.setString(16, objOrder.getNpShipToAddress1());   
        cstmt.setString(17, objOrder.getNpShipToCity());
        cstmt.setString(18, objOrder.getNpShipToProvince());
        cstmt.setString(19, objOrder.getNpShipToState());
        cstmt.setTimestamp(20, objOrder.getNpDeliveryDate());   
  
        cstmt.setLong(21, objOrder.getNpBuildingId());
        cstmt.setString(22, objOrder.getNpUserName1());
        cstmt.setString(23, objOrder.getNpDealerName());
        cstmt.setDate(24, objOrder.getNpScheduleDate2());
        
        //AGAMARRA
        cstmt.setInt(25, npsalesstructid);
        
        if ( objOrder.getNpProviderGrpIdData() == 0  )
          cstmt.setNull(26, OracleTypes.NULL);          
        else
          cstmt.setLong(26, objOrder.getNpProviderGrpIdData());          
          
        
          //INICIO: PRY-1200 | AMENDEZ
           cstmt.setInt(27, objOrder.getNpFlagVep());

          if(objOrder.getNpNumCuotas() == null){
              cstmt.setNull(28, OracleTypes.NULL);
          }else{
           cstmt.setInt(28, objOrder.getNpNumCuotas());
          }

          if(objOrder.getNpAmountVep() == null){
              cstmt.setNull(29, OracleTypes.NULL);
          }else{
           cstmt.setDouble(29, objOrder.getNpAmountVep());
          }
          //FIN: PRY-1200 | AMENDEZ

        //[Despacho en Tienda] PCASTILLO
        cstmt.setInt(30, objOrder.getNpFlagCourier());
        
        // Inicio [N_O000017567] MMONTOYA
        cstmt.setString(31, objOrder.getNpShipToReference());

        OrderContactBean orderContactBean = objOrder.getOrderContactBean();
        cstmt.setString(32, orderContactBean.getFirstName());
        cstmt.setString(33, orderContactBean.getLastName());
        cstmt.setString(34, orderContactBean.getDocumentType());
        cstmt.setString(35, orderContactBean.getDocumentNumber());
        cstmt.setString(36, orderContactBean.getPhoneNumber());
        // Fin [N_O000017567] MMONTOYA
        // EFLORES N_SD000349095
        cstmt.setString(37,objOrder.getNpCarpetaDigital());
        // EFLORES
        //INICIO: PRY-0864 | AMENDEZ
          if(objOrder.getInitialQuota() == null){
              cstmt.setNull(38,OracleTypes.NULL);
          }else{
        cstmt.setDouble(38,objOrder.getInitialQuota());
          }
        //FIN: PRY-0864 | AMENDEZ
        //INICIO DERAZO REQ-0940
        cstmt.setString(39, orderContactBean.getEmail());
        cstmt.setString(40, orderContactBean.getCheckNotification());
        //FIN DERAZO
        //INICIO: PRY-0980 | AMENDEZ
          if(objOrder.getNpPaymentTermsIQ() == null){
              cstmt.setNull(41,OracleTypes.NULL);
          }else{
        cstmt.setInt(41,objOrder.getNpPaymentTermsIQ());
          }
        //FIN: PRY-0980 | AMENDEZ

// TDECONV003 KOTINIANO
          cstmt.setString(42,objOrder.getNpFlagMigracion());
          // TDECONV003 KOTINIANO
        cstmt.registerOutParameter(43, OracleTypes.VARCHAR);
       //Inicio  : RPASCACIO N_SD000246338

          java.util.Date fechaDataOrder = new java.util.Date();
        logger.info("[LOG_PROCESS_INFO] [UPDATE_ORDER] [N_SD000246338]:  " +
                  " \n- Orden: " + objOrder.getNpOrderId() +
                  " \n- Lugar de Despacho : " + objOrder.getNpBuildingId() +
                  " \n- Forma de Pago : " + objOrder.getNpPaymentTerms()+
                  " \n- Fecha y Hora de Proceso  : " + fechaDataOrder +
                  " \n- Nombre del Servlet : OrderServlet");

          //Fin : RPASCACIO



        cstmt.registerOutParameter(43, OracleTypes.VARCHAR);
        cstmt.executeUpdate();
        strMessage = cstmt.getString(43);
        
        logger.info("[Output][strMessage]: "+strMessage);
        
      }catch(Exception e){
          e.printStackTrace();
        logger.error("[Exception][OrderDAO][updOrder]"+e.getMessage());
         strMessage = e.getMessage();
      }finally{
        try{
          closeObjectsDatabase(null,cstmt,null); 
          }catch (Exception e) {
            logger.error(formatException(e));
          }
      }   

        try{
            if((objOrder.getNpSpecificationId() == 2068) || (objOrder.getNpSpecificationId() == 2069) ){
        java.util.Date fechaFin = new java.util.Date();
              logger.info("[ORDERDAO][updOrder][PM0010354] : OrderId -->" +objOrder.getNpOrderId());
              logger.info("[ORDERDAO][updOrder][PM0010354] : Fecha -->"+fechaFin);
              logger.info("[ORDERDAO][updOrder][PM0010354] : FIN");
            }
        }catch(Exception e){
            logger.error(formatException(e));
            strMessage = e.getMessage();
          logger.info("[ORDERDAO][updOrder][PM0010354] : ERROR AL INTENTAR PINTAR LOG FIN "+strMessage);
        }
     logger.info("************************ FIN OrderDAO > updOrder************************");
      return strMessage;
   }
   
   
   /**
   * Motivo: Obteniene los valores de la tienda y Region de tramite
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 20/07/2007
   * @param     intBuildingid     
   * @param     strLogin                 
   * @return    HashMap 
   */        
   public  HashMap getBuildingName(long intBuildingid, String strLogin) throws Exception, SQLException {
   
      HashMap h = new HashMap();
      HashMap hshRetorno = new HashMap();
      String strMessage=null;
      OracleCallableStatement cstmt = null;
      String strName = null;
      int intRegionId = 0;
      String strRegionName = null;
      Connection conn=null;
      System.out.println("[OrderDAO][getBuildingName][Inicio]:"+intBuildingid+"-"+strLogin);
      String sqlStr = "BEGIN ORDERS.NP_ORDERS01_PKG.SP_GET_BUILDING_NAME(?, ?, ?, ?, ?, ?); END;";    
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);      
         cstmt.setLong(1, intBuildingid);
         cstmt.setString(2, strLogin);
         cstmt.registerOutParameter(3, Types.CHAR);
         cstmt.registerOutParameter(4, Types.CHAR);
         cstmt.registerOutParameter(5, Types.NUMERIC);
         cstmt.registerOutParameter(6, Types.CHAR);      
         cstmt.executeUpdate();      
         strMessage = cstmt.getString(3);
         if (strMessage == null){
            strName = cstmt.getString(4);
            intRegionId = cstmt.getInt(5);
            strRegionName = cstmt.getString(6);      
            /*Ingresando valores en un HasTable*/              
            h.put("wv_name", strName);
            h.put("wn_regionid", intRegionId + "");
            h.put("wv_regionname", strRegionName);
         }
      }
      catch(Exception e){
         hshRetorno.put("strMessage",e.getMessage());  
      }finally{
			closeObjectsDatabase(conn,cstmt,null); 
         /*if (cstmt != null)
            cstmt.close();
         if (conn != null)
            conn.close();*/
      }
         
      hshRetorno.put("hshData",h);
      hshRetorno.put("strMessage",strMessage);  
      System.out.println("[OrderDAO][getBuildingName][Fin]:"+intBuildingid+"-"+strLogin);
      return hshRetorno;
   }
   
   //JLIMAYMANTA
   /**
   * Motivo: Obteniene los valores de la tienda QUE identifica a Tienda Express
   * <br>Realizado por JLIMAYMANTA
   * <br>Fecha: 20/07/2007
   * @param     intBuildingid     
   * @param     strLogin                 
   * @return    HashMap 
   */        
   public  HashMap OrderDAOgetBuildingTS(long intBuildingid, String strLogin) throws Exception, SQLException {
   
      HashMap h = new HashMap();
      HashMap hshRetorno = new HashMap();
      String strMessage=null;
      OracleCallableStatement cstmt = null;
      String strName = null;
      int intRegionId = 0;
      String strRegionName = null;
      String strTypeService = null;
      Connection conn=null;
      System.out.println("[OrderDAO][OrderDAOgetBuildingTS][Inicio]:"+intBuildingid+"-"+strLogin);
      String sqlStr = "BEGIN ORDERS.NP_TE_PAYMENT_DOCUMENT_PKG.SP_GET_BUILDING_NAME_TE(?, ?, ?, ?, ?, ?, ?); END;";    
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);      
         cstmt.setLong(1, intBuildingid);
         cstmt.setString(2, strLogin);
         cstmt.registerOutParameter(3, Types.CHAR);
         cstmt.registerOutParameter(4, Types.CHAR);
         cstmt.registerOutParameter(5, Types.NUMERIC);
         cstmt.registerOutParameter(6, Types.CHAR); 
         cstmt.registerOutParameter(7, Types.CHAR); 
         cstmt.executeUpdate();      
         strMessage = cstmt.getString(3);
         if (strMessage == null){
            strName = cstmt.getString(4);
            intRegionId = cstmt.getInt(5);
            strRegionName = cstmt.getString(6);
            strTypeService = cstmt.getString(7);
            /*Ingresando valores en un HasTable*/              
            h.put("wv_name", strName);
            h.put("wn_regionid", intRegionId + "");
            h.put("wv_regionname", strRegionName);
            h.put("wv_typeservice", strTypeService);
         }
      }
      catch(Exception e){
         hshRetorno.put("strMessage",e.getMessage());  
      }finally{
                        closeObjectsDatabase(conn,cstmt,null); 
         /*if (cstmt != null)
            cstmt.close();
         if (conn != null)
            conn.close();*/
      }
         
      hshRetorno.put("hshDataTE",h);
      hshRetorno.put("strMessage",strMessage);  
      System.out.println("[OrderDAO][OrderDAOgetBuildingTS][Fin]:"+intBuildingid+"-"+strLogin);
      return hshRetorno;
   }
   
   /**
   * Motivo: Obteniene los lugares de entrega
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 30/07/2007
   * @param     intSpecialtyId             
   * @param     strMsgError       Contiene el mensaje de error, si existiera, al ejecutar el procedimiento almacenado
   * @return    HashMap 
   */   
   public  HashMap getDispatchPlaceList(long intSpecialtyId) throws Exception, SQLException {
   
      ArrayList list = new ArrayList();
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;
      HashMap hshRetorno=new HashMap();
      HashMap h=null;
      Connection conn=null;        
      String strMessage=null;
      try{
        String sqlStr = "BEGIN ORDERS.NP_ORDERS01_PKG.SP_GET_DISPATCH_PLACE_LST(?, ?, ?); END;";
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);      
        cstmt.setLong(1, intSpecialtyId);
        cstmt.registerOutParameter(2, Types.CHAR);
        cstmt.registerOutParameter(3, OracleTypes.CURSOR);      
        cstmt.executeUpdate();      
        strMessage = cstmt.getString(2);
        if (strMessage == null){
           rs = (ResultSet)cstmt.getObject(3);      
           while (rs.next()) {      
              h = new HashMap();      
              h.put("wv_npShortName", rs.getString(1));            
              h.put("wn_npBuildingId", rs.getInt(2) + "");
              h.put("npWorkFlowType", rs.getString(3));            
              h.put("npType", rs.getString(4));            
              list.add(h);      
           }
        }        
        hshRetorno.put("arrData",list);
        hshRetorno.put("strMessage",strMessage);  
      }catch(Exception e){
         hshRetorno.put("strMessage",e.getMessage()); 
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,rs); 
          }catch (Exception e) {
            logger.error(formatException(e));
          }
      }   
      
      return hshRetorno;
   
   }
         
  /**
   * Motivo: Obteniene los estados de los controles de la seccion de ordenes
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 04/09/2007
   * @param     lOrderId       Es el Id de la Orden
   * @param     strMsgError    Contiene el mensaje de error, si existiera, al ejecutar el procedimiento almacenado.
   * @return    HashMap 
  **/ 
  public HashMap getOrderScreenField(long lOrderId,String strPageName) throws SQLException, Exception {
   
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    Connection conn=null;
    String strMessage=null;
    HashMap h = new HashMap();
    HashMap hshRetorno = new HashMap();
    try{
      String sqlStr = "BEGIN NP_ORDERS05_PKG.SP_GET_ORDER_SCREEN_FIELD(?, ?, ?, ? ); END;";    
      conn = Proveedor.getConnection();    
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, lOrderId);        
      cstmt.setString(2, strPageName);        
      cstmt.registerOutParameter(3, OracleTypes.CURSOR);  
      cstmt.registerOutParameter(4, Types.CHAR);
      cstmt.execute();    
      strMessage = cstmt.getString(4);           
      if( strMessage == null ){    
        rs = (ResultSet)cstmt.getObject(3);       
        if (rs.next()) {
           h.put("ordercode",rs.getString("wv_OrderCode"));
           h.put("salesmanname",rs.getString("wv_SalesManName"));
           h.put("signdate",rs.getString("wv_SignDate"));
           h.put("dispatchplace",rs.getString("wv_DispatchPlace"));
           h.put("scheduledate",rs.getString("wv_ScheduleDate"));
           h.put("callcenteruser",rs.getString("wv_CallCenterUser"));
           h.put("paymentterms",rs.getString("wv_PaymentTerms"));
           h.put("paymentfuturedate",rs.getString("wv_PaymentFutureDate"));
           h.put("carrier",rs.getString("wv_Carrier"));        
           h.put("deliverydate",rs.getString("wv_DeliveryDate"));
           h.put("bankpayment",rs.getString("wv_BankPayment"));
           h.put("reasonreject",rs.getString("wv_ReasonReject"));
           h.put("createdocument",rs.getString("wv_CreateDocument"));
           h.put("createpayorder",rs.getString("wv_CreatePayOrder"));  
           h.put("action",rs.getString("wv_Action"));        
           h.put("createparteingreso",rs.getString("wv_ParteIngreso"));    
           h.put("createparteingresobadimei",rs.getString("wv_parteingresobadimei"));
           h.put("excelformat",rs.getString("wv_Excelformat"));    
           h.put("billingacc",rs.getString("wv_BillingAccount"));    
           h.put("resppago",rs.getString("wv_ResponsablePago"));    
           h.put("imeideletebutton",rs.getString("wv_ImeiDeleteButton"));    
           h.put("imeiscanbuttons",rs.getString("wv_ImeiScanButtons"));    
           h.put("imeiItemDevice",rs.getString("wv_ImeiItemDevice"));
           h.put("simItemService",rs.getString("wv_SimItemDevice"));
           h.put("mandatorycarry",rs.getString("wv_MandatoryCarry"));         
           h.put("createPayOrderRepair",rs.getString("wv_CreatePayOrderRepair"));            
           h.put("imeibadbutton",rs.getString("wv_ImeiBadButton"));
           h.put("createdocumentrepair",rs.getString("wv_createdocumentrepair"));
           h.put("newitems",rs.getString("wv_newitems"));
           h.put("edititems",rs.getString("wv_edititems"));
           h.put("deleteitems",rs.getString("wv_deleteitems"));
           h.put("loadMassiveImeiSim", rs.getString("wv_loadMassiveImeiSim"));
           h.put("replaceHandset", rs.getString("wv_replaceHandset"));
           h.put("salesmandata",rs.getString("wv_SalesManData"));
           h.put("adddocument",rs.getString("wv_adddocument"));    
           h.put("salesdata",rs.getString("wv_SalesData"));    
           h.put("guiaremision",rs.getString("wv_guiaremision")); //EXTERNO.MVALLE  18/11/2010
           h.put("parteingresobadaddress",rs.getString("wv_parteingresobadaddress")); //EXTERNO.MVALLE 18/11/2010
           h.put("createsuspequip",rs.getString("wv_suspequip"));  //EXTERNO.JNINO 29/11/2010  
           h.put("ordervolume",rs.getString("wv_ordervolume"));
        }
      }      
      hshRetorno.put("hshData",h);    
      hshRetorno.put("strMessage",strMessage);   
    }catch(Exception e){
         hshRetorno.put("strMessage",e.getMessage()); 
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,rs); 
          }catch (Exception e) {
            logger.error(formatException(e));
          }
    }   
    
    return hshRetorno;
   }
     
   /**
   * Motivo: Se encarga de actualizar los Pagos en Bancos
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 18/10/2007
   * @param     strOperationNumber     
   * @param     iCodeBank                 
   * @param     strDatePayment                 
   * @param     lMontoTotal                 
   * @param     strCodeService                        
   * @return    HashMap 
   */        
   public  HashMap updBankPayment(String strOperationNumber, int iCodeBank,String strDatePayment,
                          double lMontoTotal,String strCodeService, long lOrderId, String strLogin,double lImpPago,
                          Connection conn)                       
      throws SQLException, Exception {
   
      HashMap h = new HashMap();
      OracleCallableStatement cstmt = null;
      String strMessage = null;
      try{
        String sqlStr = "BEGIN ORDERS.SPI_UPD_BANK_PAYMENT(?, ?, ?, ?, ?, ?,?,?,?); END;";
      
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        
        cstmt.setString(1, strOperationNumber);
        cstmt.setInt(2, iCodeBank);
        cstmt.setString(3, strDatePayment);
        cstmt.setDouble(4, lMontoTotal);        
        cstmt.setString(5, strCodeService); 
        cstmt.setLong(6, lOrderId);  
        cstmt.setString(7, strLogin);  
        cstmt.setDouble(8, lImpPago);  
        cstmt.registerOutParameter(9, Types.VARCHAR);
        
        cstmt.executeUpdate();   
        strMessage = cstmt.getString(9);        
                  
        h.put("strMessage", strMessage); 
      }catch(Exception e){
         h.put("strMessage",e.getMessage()); 
      }finally{
        try{
          closeObjectsDatabase(null,cstmt,null); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
     }   
 
      return h;
   } 
       
   /**
   * Motivo: se encarga de obtener el pago realizado en Banco
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 18/10/2007
   * @param     iCodeBank     
   * @param     strCodeService                 
   * @param     strRuc                 
   * @param     strOperationNumber                 
   * @param     strDateVoucher                        
   * @return    HashMap 
   */        
   public  HashMap getBankPaymentDet(int iCodeBank,String strCodeService, String strRuc,
                             String strOperationNumber,String strDateVoucher) 
   throws SQLException, Exception{
   
      HashMap hshRetorno = new HashMap();
      OracleCallableStatement cstmt = null;
      String strMessage = null;        
    /*  long lImportPay=0;
      long lImportDispon=0;*/
      double lImportPay=0.0;
      double lImportDispon=0.0;
      Connection conn=null;
      try{
        String sqlStr = "BEGIN ORDERS.SPI_GET_BANK_PAYMENT_DET(?, ?, ?, ?, ?, ?, ?); END;";      
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);                  
        cstmt.setInt(1, iCodeBank);
        cstmt.setString(2, strCodeService);
        cstmt.setString(3, strOperationNumber);        
        cstmt.setString(4, strDateVoucher);                
        cstmt.registerOutParameter(5, Types.VARCHAR);
        cstmt.registerOutParameter(6, Types.NUMERIC);
        cstmt.registerOutParameter(7, Types.NUMERIC);              
        cstmt.executeUpdate();      
        strMessage = cstmt.getString(5);  
        if (strMessage == null){
           lImportPay = cstmt.getDouble(6);        
           lImportDispon = cstmt.getDouble(7);                       
           hshRetorno.put("lImportPay", lImportPay+"");
           hshRetorno.put("lImportDispon", lImportDispon+"");
           hshRetorno.put("strMessage", strMessage);
        }
        else
          hshRetorno.put("strMessage", strMessage);
      }catch(Exception e){
         hshRetorno.put("strMessage",e.getMessage()); 
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
     }   

      return hshRetorno;
   }         
       
  /**
   * Motivo: Valida el voucher y devuelve datos como el importe pagado en un HashMap
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 13/09/2007
   * @param     iBankCode       Código del Banco
   * @param     strCodDeudor    Código del  Deudor
   * @param     strNroOper      Nro de la Opración
   * @param     dFecha          
   * @param     strMsgError     Contiene el mensaje de error, si existiera, al ejecutar el procedimiento almacenado
   * @return    HashMap 
  */
  public  HashMap getValidateBankPayment(long iBankCode,String strCodDeudor, String strNroOper,java.sql.Date dFecha,String strMsgError)throws Exception, SQLException {
    OracleCallableStatement cstmt = null;
    //ResultSet rs = null;
    Connection conn=null;
    HashMap h = new HashMap();
    try{
      String sqlStr = "BEGIN INVOCAR A API DEL GRUPO DE FINANZAS(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); END;";
  
      conn = Proveedor.getConnection();
  
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, iBankCode);        
      cstmt.setString(2, strCodDeudor);  
      cstmt.setString(3, strNroOper);  
      cstmt.setDate(4, dFecha);        
      cstmt.registerOutParameter(5, Types.CHAR);           
      cstmt.execute();
      
      strMsgError = cstmt.getString(5);
      if( strMsgError == null ){
        h.put("ordercode",cstmt.getString(2));
        h.put("salesmanname",cstmt.getString(3));
        h.put("signdate",cstmt.getString(4));
        h.put("dispatchplace",cstmt.getString(5));
        h.put("scheduledate",cstmt.getString(6));
        h.put("callcenteruser",cstmt.getString(7));
        h.put("paymentterms",cstmt.getString(8));
        h.put("paymentfuturedate",cstmt.getString(9));
        h.put("carrier",cstmt.getString(10));        
        h.put("deliverydate",cstmt.getString(11));
        h.put("bankpayment",cstmt.getString(12));
        h.put("reasonreject",cstmt.getString(13));
        h.put("createdocument",cstmt.getString(14));
      }
      h.put("strMessage",strMsgError); 
    }catch(Exception e){
         h.put("strMessage",e.getMessage()); 
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
     }   

    return h;
   }
   
   /**
   * Motivo: Obtiene el Inbox en el cual se encuentra un pedido
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 14/09/2007
   * @param     strCurrentStatus        
   * @return    int 
   */
   
   public int getGetInbox(String strCurrentStatus) throws SQLException, Exception{
      
      int iReturnValue = -1;
      Connection conn=null;
      OracleCallableStatement cstmt = null;
      try{
        String sqlStr =  " { ? = call NP_ORDERS07_PKG.FX_GET_INBOXID( ? ) } ";
        
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        cstmt.registerOutParameter(1, Types.NUMERIC);   
        cstmt.setString(2, strCurrentStatus);                     
        
        cstmt.execute();
        iReturnValue = cstmt.getInt(1); 
      }catch(Exception e){
         logger.error(formatException(e));
         iReturnValue = -1;
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
     }      
      return iReturnValue;
   }  
       
   /**
   * Motivo: Devuelve el valor del TimeSatmp para una Orden especifica.
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 15/09/2007
   * @param     lOrderId          
   * @return    int 
   */
   
  public int getTimeStamp(long lOrderId) throws SQLException, Exception{
    int iReturnValue =-1;
    Connection conn=null;
    OracleCallableStatement cstmt = null;
    try{
      String sqlStr =  " { ? = call NP_ORDERS07_PKG.FX_GET_TIMESTAMP( ? ) } ";
      
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.registerOutParameter(1, Types.NUMERIC);   
      cstmt.setLong(2, lOrderId);                     
      
      cstmt.execute();
      iReturnValue = cstmt.getInt(1);   
    }catch(Exception e){
        logger.error(formatException(e));
         iReturnValue = -1;
    }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
     }     

    
    return iReturnValue;
  }  
        
   /**
   * Motivo: Retorna el alcance de la exclusividad del cliente
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 15/09/2007
   * @param     lCustomerId       Es el Id del cliente      
   * @return    HashMap 
   */ 
   public  String getRichExclusivity(long lCustomerId)
   throws SQLException, Exception {
   
      OracleCallableStatement cstmt = null;
      Connection conn=null;
      String strExclusivity="";
      try{
        String sqlStr = "BEGIN WEBSALES.SPI_GET_REACH_EXCLUSIVITY(?, ? ); END;";
      
        conn = Proveedor.getConnection();
        
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        cstmt.setLong(1, lCustomerId);  
        cstmt.registerOutParameter(2, Types.CHAR);   
        
        cstmt.execute();
        strExclusivity = cstmt.getString(2);         
      }catch(Exception e){
        logger.error(formatException(e));
        strExclusivity = e.getMessage(); 
    }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
     }    
      
      return strExclusivity;
   }
        
   /**
   * Motivo: Retorna el Id de la region del Vendedor
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 16/09/2007
   * @param     iVendedorId  
   * @param     strMsgError       
   * @return    int 
   */
   public  HashMap getVendedorRegionId(long iVendedorId) throws SQLException, Exception {
   
      OracleCallableStatement cstmt = null;
      Connection conn=null;     
      int iRegionSellerId=-1;
      HashMap hshData=new HashMap();
      String strMessage=null;
      try{
        String sqlStr = "BEGIN NP_ORDERS02_PKG.SP_GET_VENDEDOR_REGION(?, ?, ? ); END;";
      
        conn = Proveedor.getConnection();
        
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        cstmt.setLong(1, iVendedorId);  
        cstmt.registerOutParameter(2, Types.INTEGER);  
        cstmt.registerOutParameter(3, Types.CHAR);
        
        cstmt.execute();
        strMessage = cstmt.getString(3);       
        if (strMessage == null){
           iRegionSellerId = cstmt.getInt(2);             
        }        
        hshData.put("iRegionSellerId",iRegionSellerId+"");
        hshData.put("strMessage",strMessage);  
      }catch(Exception e){
        logger.error(formatException(e));
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
   * Motivo: Valida el nombre del vendedor
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 16/09/2007
   * @param     lCustomerId       
   * @param     iSiteId       
   * @param     iSpecialtyId       
   * @param     strLogin
   * @param     iVendedorId
   * @param     strVendedorName
   * @return    String 
   */ 
   public  String doValidateSalesName(long lCustomerId,long iSiteId,int iSpecialtyId,String strLogin,int iVendedorId,String strVendedorName,int iUserId,int iAppId)
   throws SQLException, Exception {
     
      OracleCallableStatement cstmt = null;
      Connection conn = null;
      String strMessage=null;
      try{
        String sqlStr = "BEGIN NP_ORDERS07_PKG.SP_VALIDATE_SALES_NAME(?, ?, ?, ?, ?, ?, ?, ?, ? ); END;";
      
        conn = Proveedor.getConnection();
        
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        cstmt.setLong(1, lCustomerId);  
        cstmt.setLong(2, iSiteId);  
        cstmt.setInt(3, iSpecialtyId);  
        cstmt.setString(4, strLogin);  
        cstmt.setInt(5, iVendedorId);  
        cstmt.setString(6, strVendedorName);
        cstmt.setInt(7, iUserId);
        cstmt.setInt(8, iAppId);
        cstmt.registerOutParameter(9, Types.CHAR);
        
        cstmt.execute();           
        strMessage = cstmt.getString(9);         
      }catch(Exception e){
        logger.error(formatException(e));
        strMessage = e.getMessage();  
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
     }    

      return strMessage;
   }   
           
   
   /**
   * Motivo: Retorna flag que indica si Vendedor de Orden puede ser cambiado considerando la  Existencia de Oportunidades activas
   *         Si existe Opp.Activa para vendedor dado, o no existe Oportunidad activa, entonces es posisble cambiar vendedor de Orden,
   *         en caso contrario, no permite cambiar.
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 16/09/2007
   * @param     lCustomerId  
   * @param     iSiteId       
   * @param     strAccMngmnt
   * @param     iVendedorId      
   * @param     strMsgError
   * @return     int 
   
   */ 
   public  HashMap getOppOwnershipChngFlag(long lCustomerId,long iSiteId,String strAccMngmnt,int iVendedorId)
   throws SQLException, Exception{
   
      OracleCallableStatement cstmt = null;        
      Connection conn=null;  
      HashMap hshData=new HashMap();
      String strMessage=null;
      int iFlag=-1;
      try{
        String sqlStr = "BEGIN  WEBSALES.SPI_GET_OPP_OWNERSHIP_CHNG_FLG(?, ?, ?, ?, ?, ? ); END;";
      
        conn = Proveedor.getConnection();
        
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        cstmt.setLong(1, lCustomerId);  
        cstmt.setLong(2, iSiteId);  
        cstmt.setString(3, strAccMngmnt);  
        cstmt.setInt(4, iVendedorId);    
        cstmt.registerOutParameter(5, Types.INTEGER);
        cstmt.registerOutParameter(6, Types.CHAR);
        
        cstmt.execute();
        iFlag = cstmt.getInt(5);       
        strMessage = cstmt.getString(6);         
        hshData.put("iFlag",iFlag+"");
        hshData.put("strMessage",strMessage);
      }catch(Exception e){
        logger.error(formatException(e));
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
   * Motivo: Retorna el dealer
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 16/09/2007
   * @param     iVendedorId     
   * @return    String 
   */ 
   public  String getDealer(long iVendedorId)throws SQLException, Exception {
   
      OracleCallableStatement cstmt = null;
      Connection conn=null;     
      String strDealer="";
      try{
        String sqlStr = "BEGIN NP_ORDERS07_PKG.SP_GET_DEALER(?, ?); END;";
      
        conn = Proveedor.getConnection();
        
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);       
        
        cstmt.setLong(1, iVendedorId);         
        cstmt.registerOutParameter(2, Types.CHAR);
        
        cstmt.execute();           
        strDealer = cstmt.getString(2);   
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
      return strDealer;
   }        
   
  /**
   * Motivo: Obteniene los estados de los controles de la seccion de ordenes
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 14/11/2007
   * @param     String       
   * @param     long    
   * @return    HashMap 
  **/ 
  public HashMap getPaymentListBySource(String strSource,long lSourceId) throws SQLException,Exception {
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    Connection conn=null;
    String strMessage=null;
    PaymentBean objPaymentBean =null;   
    ArrayList list=new ArrayList();
    HashMap hshRetorno = new HashMap();
    try{
      String sqlStr = "BEGIN ORDERS.SPI_GET_PAYMENT_LST_BY_SOURCE(?, ?, ?, ?); END;";
    
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setString(1, strSource);        
      cstmt.setLong(2, lSourceId);              
      cstmt.registerOutParameter(3, OracleTypes.CURSOR);
      cstmt.registerOutParameter(4, Types.CHAR);
      cstmt.execute();
      strMessage = cstmt.getString(4);
      
      if( strMessage == null ){             
        rs = (ResultSet)cstmt.getObject(3);
        while (rs.next()) {   
           objPaymentBean = new PaymentBean();
           objPaymentBean.setNpPaymentorderId(rs.getLong("NPPAYMENTORDERID"));            
           objPaymentBean.setNpName(rs.getString("NPNAME"));
           objPaymentBean.setNpRuc(rs.getString("NPRUC"));            
           objPaymentBean.setEstado(rs.getString("ESTADO"));            
           objPaymentBean.setMoneda(rs.getString("MONEDA"));            
           objPaymentBean.setNpTotalAmount(rs.getDouble("NPTOTALAMOUNT"));            
           objPaymentBean.setNpDeudaAmount(rs.getDouble("NPDUEAMOUNT"));            
           objPaymentBean.setNpConceptName(rs.getString("NPCONCEPTNAME"));            
           objPaymentBean.setNpCreatedDate(rs.getDate("NPCREATEDDATE"));            
           objPaymentBean.setVoucher(rs.getInt("VOUCHER"));            
           objPaymentBean.setMonto(rs.getDouble("MONTO"));            
           objPaymentBean.setTienda(rs.getString("TIENDA"));            
           objPaymentBean.setCaja(rs.getString("CAJA"));            
           list.add(objPaymentBean);      
        }      
      }                
      hshRetorno.put("arrPaymentList",list);    
      hshRetorno.put("strMessage",strMessage);   
    }catch(Exception e){
        logger.error(formatException(e));
        hshRetorno.put("strMessage",e.getMessage());  
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,rs); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
     }    
    
    return hshRetorno;
  }
   
   /**
   * Motivo: Genera Comprobante de Pago
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 16/11/2007
   * @param     long       
   * @param     String    
   * @return    HashMap 
   */ 
  public HashMap generateDocumentInvBill(long lOrderId,String strOrigen,String strLogin,int iBuilding)     
  throws SQLException, Exception{
       
    OracleCallableStatement cstmt = null;   
    Connection conn=null;
    String strMessage=null;         
    int iError=0;
    HashMap hshRetorno = new HashMap();   
    System.out.println("[OrderDAO][generateDocumentInvBill][Inicio]");
    try{
      String sqlStr = "BEGIN ORDERS.NP_ORDERS03_PKG.SP_GENERATE_DOCUMENT_INV_BILL(?, ?, ?, ?, ?, ?); END;";
    
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, lOrderId);             
      cstmt.setString(2, strOrigen);        
      cstmt.setString(3, strLogin);        
      cstmt.setInt(4, iBuilding);        
      cstmt.registerOutParameter(5, OracleTypes.NUMBER);           
      cstmt.registerOutParameter(6, OracleTypes.VARCHAR);
      cstmt.execute();
      strMessage = cstmt.getString(6);
      iError = cstmt.getInt(5);
         
      hshRetorno.put("strError",iError+""); 
      hshRetorno.put("strMessage",strMessage);   
    }
    catch(Exception e){
       System.out.println("[Exception][OrderDAO][generateDocumentInvBill]"+e.getMessage());
       hshRetorno.put("strMessage",e.getMessage());  
    }
    finally{
       System.out.println("[Finally][OrderDAO][generateDocumentInvBill][Inicio]");
       try{
          closeObjectsDatabase(conn,cstmt,null);           
       }
       catch (Exception e) {
          logger.error(formatException(e));
       }
       System.out.println("[Finally][OrderDAO][generateDocumentInvBill][Fin]");
    }       
    System.out.println("[OrderDAO][generateDocumentInvBill][Fin]");
    return hshRetorno;
  }
   
   
    /**
   * Motivo: Genera Comprobante de Pago solo por servicios desde el detalle
   * <br>Realizado por: <a href="mailto:karen.salvadors@nextel.com.pe">Karen Salvador</a>
   * <br>Fecha: 23/06/2008
   * @param     long       
   * @param     String    
   * @return    HashMap 
   */ 
   public HashMap generateDocumentInvBillDetail(long lOrderId,String strOrigen,String strLogin,int iBuilding)     
   throws SQLException,Exception {
   
      OracleCallableStatement cstmt = null;   
      Connection conn=null;
      String strMessage=null;
       int iError=0;
      
      HashMap hshRetorno = new HashMap();
      try{
        String sqlStr = "BEGIN ORDERS.NP_ORDERS03_PKG.SP_GENERATE_INVOICE_PAY_BANK(?, ?, ?, ?, ?,?); END;";
      
        System.out.println("Datos : generateDocumentInvBillDetail");
        System.out.println("lOrderId:"+lOrderId);
        System.out.println("strOrigen:"+strOrigen);
        System.out.println("strLogin:"+strLogin);
        System.out.println("iBuilding:"+iBuilding);

        conn = Proveedor.getConnection();
        
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        cstmt.setLong(1, lOrderId);             
        cstmt.setString(2, strOrigen);        
        cstmt.setString(3, strLogin);        
        cstmt.setInt(4, iBuilding);                 
        cstmt.registerOutParameter(5, OracleTypes.NUMBER);           
        cstmt.registerOutParameter(6, OracleTypes.VARCHAR);
        cstmt.execute();     
        iError = cstmt.getInt(5);          
        strMessage = cstmt.getString(6);       
        
        hshRetorno.put("strError",iError+""); 
        hshRetorno.put("strMessage",strMessage);   
      }catch(Exception e){
        logger.error(formatException(e));
        hshRetorno.put("strMessage",e.getMessage());  
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
     }   
      
      System.out.println("-----En generateDocumentInvBillDetail-----");
      return hshRetorno;
   }
   
   
   /**
   * Motivo: Genera Orden de Pago
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 16/11/2007
   * @param     long       
   * @param     String    
   * @return    HashMap 
   */ 
   public HashMap generatePayamentOrder(long lOrderId,String strLogin,int iBuilding)     
   throws SQLException, Exception {
   
      OracleCallableStatement cstmt = null;   
      Connection conn=null;
      String strMessage=null;  
      int iError=0;
      HashMap hshRetorno = new HashMap();
      try{
        String sqlStr = "BEGIN ORDERS.NP_ORDERS03_PKG.SP_GENERATE_PAYMENT_ORDER(?, ?, ?, ?, ?); END;";
      
        conn = Proveedor.getConnection();
        
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        cstmt.setLong(1, lOrderId);             
        cstmt.setString(2, strLogin);             
        cstmt.setInt(3, iBuilding);             
        cstmt.registerOutParameter(4, OracleTypes.NUMERIC);
        cstmt.registerOutParameter(5, Types.CHAR);
        
        cstmt.execute();
        iError = cstmt.getInt(4);     
        strMessage = cstmt.getString(5);       
           
        hshRetorno.put("strError",iError+""); 
        hshRetorno.put("strMessage",strMessage);   
      }catch(Exception e){
        logger.error(formatException(e));
        hshRetorno.put("strMessage",e.getMessage());  
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
     }
      
      System.out.println("-----En generatePayamentOrder-----");
      return hshRetorno;
   }
   
   /**
   * Motivo: Sincronización de las Cuentas de BSCS / CRM al activarse los contratos
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 27/11/2007
   * @param     long       
   * @return    String 
   */ 
   public String updSinchronizeActiv(long lOrderId, String strLogin)     
   throws SQLException, Exception{
   
      OracleCallableStatement cstmt = null;   
      Connection conn=null;
      String strMessage=null; 
      try{
        String sqlStr = "BEGIN ORDERS.NP_ORDERS15_PKG.SP_UPD_SINCHRONIZE_ACTIV(?, ?, ?); END;";
      
        conn = Proveedor.getConnection();
        
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        cstmt.setLong(1, lOrderId);                 
        cstmt.setString(2, strLogin);                 
        cstmt.registerOutParameter(3, Types.CHAR);
        
        cstmt.execute();     
        strMessage = cstmt.getString(3);        
      }catch(Exception e){
        logger.error(formatException(e));
        strMessage = e.getMessage(); 
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
     }

      System.out.println("-----En updSinchronizeActiv----->"+strMessage);
      return strMessage;
   }


   /**
   * Motivo: Sincronización de las Cuentas de BSCS / CRM al activarse los contratos
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 27/11/2007
   * @param     long       
   * @return    String 
   */ 
   public String insNotificationAction(long lOrderId, String strStatus, String strLogin)     
   throws SQLException, Exception{
   
      OracleCallableStatement cstmt = null;   
      Connection conn=null;
      String strMessage=null;
      try{
        String sqlStr = "BEGIN ORDERS.NP_ORDERS05_PKG.SP_UPD_ORDERS_REPAIRS(?, ?, ?, ?); END;";        
        conn = Proveedor.getConnection();      
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        cstmt.setLong(1, lOrderId);
        cstmt.setString(2, strStatus);
        cstmt.setString(3, strLogin);                 
        cstmt.registerOutParameter(4, Types.CHAR);
        
        cstmt.execute();     
        strMessage = cstmt.getString(4);       
              
      }catch(Exception e){
        logger.error(formatException(e));
        strMessage = e.getMessage(); 
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
     }
      
               
      return strMessage;
   }


   /**
   * Motivo: Sincronización de las Cuentas de BSCS / CRM al activarse los contratos
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 27/11/2007
   * @param     long       
   * @param     String
   * @param     String
   * @param     String
   * @param     long       
   * @return    HashMap 
   */ 
   public HashMap doExecuteActionFromOrder(long lOrderId,String strStatusOld,String strStatusNew,String strLogin,long lLoginBuilding)     
   throws SQLException, Exception{
   
      OracleCallableStatement cstmt = null;   
      Connection conn=null;
      String strMessage=null;  
      HashMap hshData=new HashMap();
      int iError=0;
      try{
        String sqlStr = "BEGIN ORDERS.NP_ORDERS03_PKG.SP_EXECUTE_ACTION_FROM_ORDER(?, ?, ?, ?, ?, ?, ?); END;";
      
        conn = Proveedor.getConnection();
        
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        cstmt.setLong(1, lOrderId);                   
        cstmt.setString(2, strStatusOld);  
        cstmt.setString(3, strStatusNew);  
        cstmt.setString(4, strLogin);  
        cstmt.setLong(5, lLoginBuilding);                   
        //cstmt.setInt(6, iError);                
        cstmt.registerOutParameter(6, OracleTypes.NUMBER);
        cstmt.registerOutParameter(7, OracleTypes.VARCHAR);
        
        cstmt.execute();     
        strMessage = cstmt.getString(7);
        iError     = cstmt.getInt(6); 
        hshData.put("strMessage",strMessage);
        hshData.put("strError",iError+"");  
      }catch(Exception e){
        logger.error(formatException(e));
        hshData.put("strError",e.getMessage());
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
   * Motivo: Verifica si el boton se puede mostrar para el speficitationid 
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 27/11/2007
   * @param     long  
   * @return    String 
   */   
   public String getShowButtom(long lSpecificationId) 
   throws SQLException, Exception
   {
      String strShowButtom=null;
      Connection conn=null;
      OracleCallableStatement cstmt = null;      
      try{
        String sqlStr = " { ? = call ORDERS.NP_ORDERS07_PKG.FX_GET_SHOW_BUTTON( ? ) } "; 
        
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        
        cstmt.registerOutParameter(1, Types.CHAR);
        cstmt.setLong(2, lSpecificationId);       
        cstmt.execute();
        
        strShowButtom = cstmt.getString(1);            
      }catch(Exception e){
        logger.error(formatException(e));
        strShowButtom = e.getMessage();
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
     }
            
      return strShowButtom;
   }     
   
   /**
   * Motivo: Sincronización de las Cuentas de BSCS / CRM al activarse los contratos
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 27/11/2007
   * @param     long       
   * @param     String
   * @param     String
   * @param     long       
   * @return    HashMap 
   */ 
   public HashMap doGenerarParteIngreso(long lOrderId,String strOrigen,String strLogin,long lLoginBuilding, String strPIType)     
   throws SQLException, Exception{
   
      OracleCallableStatement cstmt = null;   
      Connection conn=null;
      String strMessage=null;  
      HashMap hshData=new HashMap();
      int iError=0;
      
      String sqlStr = "BEGIN ORDERS.NP_ORDERS03_PKG.SP_GENERAR_PARTE_INGRESO(?, ?, ?, ?, ?, ?,?); END;";
      try{
         conn = Proveedor.getConnection();      
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         cstmt.setLong(1, lOrderId);                   
         cstmt.setString(2, strOrigen);  
         cstmt.setString(3, strLogin);  
         cstmt.setLong(4, lLoginBuilding);
         cstmt.setString(5, strPIType);  
         //cstmt.setInt(6, iError);
         cstmt.registerOutParameter(6, Types.NUMERIC);
         cstmt.registerOutParameter(7, Types.CHAR);
         
         cstmt.execute();     
         iError = cstmt.getInt(6); //EXTERNO.MVALLE AGREGADO
         strMessage = cstmt.getString(7);       
      }
      catch(Exception e){
          hshData.put("strMessage",e.getMessage());
      }
      finally{
         closeObjectsDatabase(conn,cstmt,null);
      } 
      hshData.put("strMessage",strMessage);
      hshData.put("strError",iError+"");
      return hshData;
   }
   
   /**
   * Motivo:    
   * <br>Realizado por: <a href="mailto:edgar.jara@nextel.com.pe">Edgar Jara</a>
   * <br>Fecha: 29/08/2008
   * @param     long       
   */ 
   public HashMap doAutorizacionDevolucion(long lOrderId)     
   throws SQLException, Exception{
       System.out.println("Entro al dao:" +lOrderId );
      OracleCallableStatement cstmt = null;   
      Connection conn=null;
      //
      String strMessage=null;  
      String strSuccesMessage=null;  
      HashMap hshData=new HashMap();
      //int iError=0;
     
      String sqlStr = "BEGIN CASHDESK.SPI_UPD_PAYMENT_ORD_RETURNFLG (?, ?, ?); END;";
      try{
         conn = Proveedor.getConnection();   
         conn.setAutoCommit(false);
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         cstmt.setLong(1, lOrderId);                                 
         cstmt.registerOutParameter(2, Types.CHAR);
         cstmt.registerOutParameter(3, Types.CHAR);
         
         cstmt.execute();     
         strSuccesMessage = cstmt.getString(2);    
         strMessage = cstmt.getString(3); 
         if (strMessage!=null) throw new Exception(strMessage);
         hshData.put("strMessage",strMessage);
         hshData.put("strSuccesMessage",strSuccesMessage);
         conn.commit();
      }
      catch(Exception e){
        conn.rollback();
        hshData.put("strMessage",strSuccesMessage);         
      }
      finally{
         closeObjectsDatabase(conn,cstmt,null);
      } 
      //hshData.put("strError",iError+"");
      return hshData;
   }
   
   
  /**
  * Motivo: Obtiene un listado de Inboxs a los que se puede avanzar 
  * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
  * <br>Fecha: 07/12/2007
  * @param     int       
  * @param     String    
  * @return    HashMap 
  **/ 
  public String getAuthorizationReturn(long lOrderId,int iUserId,int iAppId)     
   throws SQLException {
   
    OracleCallableStatement cstmt = null;   
    Connection conn=null;
    String strMessage=null;        
    String autorizacion="";
    HashMap hshRetorno = new HashMap();
    HashMap hshData = null;
    ArrayList objLista=new ArrayList();
    try{
      String sqlStr = "BEGIN CASHDESK.SPI_VALIDATE_ORDER_REBATES(?, ?, ?, ?, ?); END;";
    
      conn = Proveedor.getConnection();
      
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, lOrderId);                                    
      cstmt.setInt(2, iUserId);
      cstmt.setInt(3, iAppId);
      cstmt.registerOutParameter(4, Types.CHAR);
      cstmt.registerOutParameter(5, Types.CHAR);
      cstmt.execute();
      
      strMessage = cstmt.getString(5);       
      if(strMessage==null){
        autorizacion = cstmt.getString(4);         
      }                 
      
      hshRetorno.put("strMessage",strMessage); 
      hshRetorno.put("autorizaciondevolucion",autorizacion);               
    }catch(Exception e){
        logger.error(formatException(e));
         hshRetorno.put("strMessage",e.getMessage()); 
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
     }
    
    return autorizacion;
  }     

   
  /**
  * Motivo: Obtiene un listado de Inboxs a los que se puede avanzar 
  * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
  * <br>Fecha: 07/12/2007
  * @param     int       
  * @param     String    
  * @return    HashMap 
  **/ 
  public HashMap getSpecificationStatus(int iSpecificationId,String strCurrentInbox)     
   throws SQLException {
   
    OracleCallableStatement cstmt = null;   
    Connection conn=null;
    String strMessage=null;        
    ResultSet rs=null;
    HashMap hshRetorno = new HashMap();
    HashMap hshData = null;
    ArrayList objLista=new ArrayList();
    try{
      String sqlStr = "BEGIN ORDERS.NP_ORDERS07_PKG.SP_GET_SPECIFICATION_STATUS(?, ?, ?, ?); END;";
    
      conn = Proveedor.getConnection();
      
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setInt(1, iSpecificationId);             
      cstmt.setString(2, strCurrentInbox);                         
      cstmt.registerOutParameter(3, OracleTypes.CURSOR);
      cstmt.registerOutParameter(4, Types.CHAR);
      cstmt.execute();
      
      strMessage = cstmt.getString(4);       
      if(strMessage==null){
        rs = (ResultSet)cstmt.getObject(3);         
        while (rs.next()) {
          hshData = new HashMap();
          hshData.put("inbox",rs.getString("npnextinboxs"));         
          objLista.add(hshData);
        }
      }                 
      
      hshRetorno.put("strMessage",strMessage); 
      hshRetorno.put("objLista",objLista);               
    }catch(Exception e){
        logger.error(formatException(e));
         hshRetorno.put("strMessage",e.getMessage()); 
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,rs); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
     }
    
    return hshRetorno;
  }    
   

   /**
   * Motivo: Valida que Vendedor de la orden se pueda modificar
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 18/12/2007
   * @param     int       
   * @param     int    
   * @return    int 
   */ 
   public int getFlagModifiySalesName(int iSpecificationId,int iVendedorId,int iUserId,int iAppId)     
   throws SQLException, Exception{
   
      OracleCallableStatement cstmt = null;   
      Connection conn=null;      
      int iRetorno=0;       
      try{
        String sqlStr = "BEGIN ORDERS.NP_ORDERS07_PKG.SP_FLAG_MODIFY_SALES_NAME(?, ?, ?, ?, ?); END;";
      
        conn = Proveedor.getConnection();
        
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        cstmt.setInt(1, iSpecificationId);             
        cstmt.setInt(2, iVendedorId);                         
        cstmt.setInt(3, iUserId);   
        cstmt.setInt(4, iAppId);   
        cstmt.registerOutParameter(5, OracleTypes.NUMBER);      
        
        cstmt.execute();
        iRetorno = cstmt.getInt(5);                   
      }catch(Exception e){
        logger.error(formatException(e));
        iRetorno=0;    
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
     }
  
      return iRetorno;
   }   

/**
* Motivo: Obteniene un listado de transportistas 
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 21/12/2007
* @param     strParamName   
* @param     strParamStatus  
* @return    ArrayList 
*/
public  HashMap getCarrierList(String strParamName, String strParamStatus) throws SQLException,Exception {
                                
   ArrayList list = new ArrayList();
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   Connection conn=null;
   String strMessage = null;
   HashMap hshData =new  HashMap();
   HashMap h=null;
   try{
    String sqlStr = "BEGIN ORDERS.NP_ORDERS07_PKG.SP_GET_CARRIER_LIST(?, ?, ?, ?); END;";
     conn = Proveedor.getConnection();
     cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
     
     cstmt.setString(1, strParamName);
     cstmt.setString(2, strParamStatus);
     cstmt.registerOutParameter(3, OracleTypes.CURSOR);
     cstmt.registerOutParameter(4, Types.CHAR);
     cstmt.execute();
     
     strMessage = cstmt.getString(4);
     if (strMessage!=null && strMessage.startsWith(Constante.SUCCESS_ORA_RESULT))
        strMessage=null;         
     if( strMessage == null ){
        rs = (ResultSet)cstmt.getObject(3);   
        while (rs.next()) {
           h = new HashMap();
           h.put("carrierid", rs.getString("npcarrierid"));
           h.put("carriername", rs.getString("npname"));      
           list.add(h);
        }
     }        
  
     hshData.put("strMessage",strMessage);
     hshData.put("objListado",list);  
   }catch(Exception e){
        logger.error(formatException(e));
        hshData.put("strMessage",e.getMessage());
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,rs); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
     }
   
   
   return hshData;
}

/**
* Motivo: Se encarga de actualizar en timestamp
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 21/12/2007
* @param     lOrderId     
* @param     strMessage                 
* @param     conn                     
* @return    HashMap 
*/        
public  HashMap updTimeStamp(long lOrderId)//,Connection conn) 
throws SQLException,Exception {

   HashMap h = new HashMap();
   OracleCallableStatement cstmt = null;
   String strMessage = null;                
   Connection conn=null;
   try{
    String sqlStr = "BEGIN NP_ORDERS07_PKG.SP_UPDATE_ORDER_TIMESTAMP(?, ?); END;";
     
     conn = Proveedor.getConnection();
     cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
     
     cstmt.setLong(1, lOrderId);               
     cstmt.registerOutParameter(2, Types.VARCHAR);
     
     cstmt.executeUpdate();   
     strMessage = cstmt.getString(2);           
           
     h.put("strMessage", strMessage);
           
   }catch(Exception e){
        logger.error(formatException(e));
        h.put("strMessage",e.getMessage());
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
     }
 
   return h;
}     


  /**
  * Motivo: Obtiene un listado con las formas de pago por categoría.
  * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
  * <br>Fecha: 15/01/2008
  * @param     iSpecificationId     
  * @param     strMessage                                
  * @return    HashMap 
  */        
  public  HashMap getPayFormList(int iSpecificationId,long lCustomerId) throws SQLException,Exception {

   HashMap h = new HashMap();
   HashMap objResultado = new HashMap();
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   ArrayList list=new ArrayList();
   String strMessage = null;                
   Connection conn=null;
   try{
     String sqlStr = "BEGIN NP_ORDERS07_PKG.SP_GET_PAYFORM_LST(?, ?, ?, ?); END;";
     
     conn = Proveedor.getConnection();
     cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);   
     cstmt.setInt(1, iSpecificationId);
     cstmt.setLong(2, lCustomerId);   
     cstmt.registerOutParameter(3, OracleTypes.CURSOR);
     cstmt.registerOutParameter(4, Types.VARCHAR);
     cstmt.execute();
     
     strMessage = cstmt.getString(4);
     
     if( strMessage == null ){
       rs = (ResultSet)cstmt.getObject(3);
    
       while (rs.next()) {   
          h = new HashMap();
          h.put("value", rs.getString("nppaymentform"));
          h.put("descripcion", rs.getString("nppaymentform"));      
          list.add(h);
       }
     }
     
     objResultado.put("objListado",list);
     objResultado.put("strMessage",strMessage);        
   }catch(Exception e){
        logger.error(formatException(e));
        h.put("strMessage",e.getMessage());
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,rs); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
     }
   
   
   return objResultado;
}    


/**
* Motivo: Obtiene un listado de opciones a mostrar en el combo accion de la página de edición de Ordenes
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 24/01/2008
* @param     iSpecificationId     
* @param     strAction                                
* @return    HashMap 
*/        
public  HashMap getActionList(int iSpecificationId,String strAction,String strCurrentInbox)
throws SQLException,Exception {

   HashMap h = new HashMap();
   HashMap objResultado = new HashMap();
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   ArrayList list=new ArrayList();
   String strMessage = null;                
   Connection conn=null;
   try{
      String sqlStr = "BEGIN NP_ORDERS07_PKG.SP_GET_ACTION_LST(?, ?, ?, ?, ?); END;";
       
       conn = Proveedor.getConnection();
       cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);   
       cstmt.setInt(1, iSpecificationId);
       cstmt.setString(2, strAction);   
       cstmt.setString(3, strCurrentInbox);   
       cstmt.registerOutParameter(4, OracleTypes.CURSOR);
       cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
       cstmt.execute();
       
       strMessage = cstmt.getString(5);
    
       if( strMessage == null ){
         rs = (ResultSet)cstmt.getObject(4);
         while (rs.next()) {   
            h = new HashMap();
            //h.put("value", rs.getLong("NPSPECIFICATIONID")+"");
            h.put("npinbox", rs.getString("NPINBOX"));      
            h.put("npactionvalue", rs.getString("NPACTIONVALUE"));      
            h.put("npactiondesc", rs.getString("NPACTIONDESC"));            
            list.add(h);
         }
       }
       objResultado.put("objListado",list);
       objResultado.put("strMessage",strMessage);           
    }catch(Exception e){
        logger.error(formatException(e));
        objResultado.put("strMessage",e.getMessage());
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,rs); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
     }
   
   
   return objResultado;
  }

     /*Inicio CEM*/
/**
* Motivo: Validar si se ha ingresado la fecha de proceso 
  antes de ingresar al inbox de Procesos Automáticos.
* <br>Realizado por: <a href="mailto:cristian.espinoza@nextel.com.pe">Cristian Espinoza</a>
* <br>Fecha: 13/01/2008
* @param    lOrderId   
* @param    strLogin  
* @param    lSpecificationId 
* @param    strFechaProceso 
* @param    strInbox 
* @param	   strActionName
* @return   String 
*/	 
public String validateDateProcess(long lOrderId, String strLogin,long lSpecificationId,String strFechaProceso, String strInbox,String strActionName) throws SQLException, Exception {

   OracleCallableStatement cstmt = null;
   //ResultSet rs = null;
   Connection conn=null;         
   String strMessage = null;
   
   logger.debug("[Input][lOrderId]: " + lOrderId + "");
   logger.debug("[Input][strLogin]: " + strLogin);
   logger.debug("[Input][lSpecificationId]: " + lSpecificationId + "");
   logger.debug("[Input][strFechaProceso]: " + strFechaProceso);
   logger.debug("[Input][strInbox]: " + strInbox);
   logger.debug("[Input][strActionName]: " + strActionName);
        
   try{
      String sqlStr = "BEGIN NP_ORDERS15_PKG.SP_VALIDATE_DATEPROCESS(?,?,?,?,?,?,?); END;";
      logger.info("[NP_ORDERS15_PKG.SP_VALIDATE_DATEPROCESS]");
     conn = Proveedor.getConnection();
     cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
     
     cstmt.setLong(1, lOrderId);               
     cstmt.setString(2,strLogin);
     cstmt.setLong(3,lSpecificationId);
     cstmt.setString(4,strFechaProceso);
     cstmt.setString(5,strInbox);
     cstmt.setString(6,strActionName);		
     cstmt.registerOutParameter(7, OracleTypes.VARCHAR);
     
     cstmt.executeUpdate();
     strMessage = cstmt.getString(7);  
     logger.info("[Output][strMessage]: " + strMessage);
   } catch(Exception e){
        logger.error(formatException(e));
        strMessage = e.getMessage();
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
     }     
   
   return strMessage;
}
	/*Fin CEM*/


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
     *  FECHA: 25/09/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
     
	/**
	 * 
	 * @param imeiSimBean
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public HashMap validateMassiveImeiSim(LoadMassiveItemBean loadMassiveItemBean) throws SQLException, Exception {
		/*
		if (logger.isDebugEnabled()) {
			logger.debug("--Inicio--");
		}
		*/
		HashMap hshDataMap = new HashMap();
		Connection conn = null;
		OracleCallableStatement cstmt = null;
		String strMessage = null;
		//DbmsOutput dbmsOutput = null;
		try {
			String sqlStr = "BEGIN ORDERS.NP_VALIDATE_MASSIVE_PKG.SP_VALIDATE_MASSIVE_IMEI_SIM(?, ?, ?, ?, ?, ?, ?); END;";
			conn = Proveedor.getConnection();
			//dbmsOutput = new DbmsOutput(conn);
			//dbmsOutput.enable(1000000);
			cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
			ArrayDescriptor adStringList = ArrayDescriptor.createDescriptor("ORDERS.TT_STRING_LST", conn);
			ARRAY arrImei = new ARRAY(adStringList, conn, loadMassiveItemBean.getImei().toArray());
			ARRAY arrSim = new ARRAY(adStringList, conn, loadMassiveItemBean.getSim().toArray());
			cstmt.setLong(1, loadMassiveItemBean.getLOrderId().longValue());
			cstmt.setLong(2, loadMassiveItemBean.getLDispatchPlace().longValue());
			cstmt.setARRAY(3, arrImei);
			cstmt.setARRAY(4, arrSim);
			cstmt.registerOutParameter(5, OracleTypes.ARRAY, "ORDERS.TT_STRING_LST");
      cstmt.registerOutParameter(6, Types.INTEGER);
			cstmt.registerOutParameter(7, OracleTypes.VARCHAR);
			cstmt.executeQuery();
			//dbmsOutput.show();
			strMessage = cstmt.getString(7);
			if(StringUtils.isBlank(strMessage)) {
        loadMassiveItemBean.setLCantErroneos(new Long(cstmt.getLong(6)));
				ARRAY arrMessage = cstmt.getARRAY(5);
				if(arrMessage!=null) {
					for(int i=0; i < arrMessage.getOracleArray().length; i++) {
            if(StringUtils.isBlank((String) loadMassiveItemBean.getMessage().get(i))) {
              loadMassiveItemBean.getMessage().set(i, arrMessage.getOracleArray()[i].toString());
            }
					}
				}
				hshDataMap.put("loadMassiveItemBean", loadMassiveItemBean);
			}
		} finally {
			hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
			//dbmsOutput.close();
			closeObjectsDatabase(conn, cstmt, null);
		}
		return hshDataMap;
	}     
     
  /**
   * Motivo: Verifica si existe una Orden, dado su ID.
   * Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard
	 * De los Reyes</a> <br>
	 * Fecha: 20/10/2008.
	 * 
	 * @param lOrderId ID de la Orden
	 * @return HashMap que contiene un Flag 0 ó 1.
   */
  public HashMap existOrder(long lOrderId) throws SQLException, Exception {
    if (logger.isDebugEnabled()) {
			logger.debug("--Inicio--");
    }
    HashMap hshDataMap = new HashMap();
    Connection conn = null;
		OracleCallableStatement cstmt = null;
    String strMessage = null;
    try{
      String sqlStr = "BEGIN ORDERS.NP_ORDERS05_PKG.SP_GET_ORDER_EXISTS(?, ?, ?); END;";
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
      cstmt.setString(1, String.valueOf(lOrderId));
			cstmt.registerOutParameter(2, OracleTypes.NUMBER);
			cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
      cstmt.executeQuery();
      Boolean flagExistOrder = Boolean.valueOf(cstmt.getBoolean(2));
      strMessage = cstmt.getString(3);
      hshDataMap.put("flagExistOrder", flagExistOrder);
      hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
    } finally {
      closeObjectsDatabase(conn,cstmt,null);
    }
    return hshDataMap;
  }

	/**
	 * Motivo: Obtiene la Lista de Ordenes Internas de una Orden principal<br>
	 * Realizado por: <a href="mailto:luis.silva@asistp.com">Luis Silva</a> <br>
	 * Fecha: 20/10/2010
	 * @param plParentOrderId ID de la Orden principal.
	 * @param plNumRegistros Cantidad de Órdenes a mostrar.
	 * @param plNumPagina Número de página.
	 * @return Listado de Ordenes Internas asociadas a la Orden principal.
	 */
	public HashMap getInternalOrderList(long plParentOrderId, long plNumRegistros, long plNumPagina) throws SQLException,Exception{
		
		HashMap hshDataMap = new HashMap();
		ArrayList arrInternalOrderList = new ArrayList();
		Connection conn = null;
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;
		long lNumTotalRegistros = 0;
		String strMessage = null;
		String sqlStr = null;
		String strTipoOrden = null;
		
		try{
			sqlStr = "BEGIN ORDERS.NP_ORDERS20_PKG.SP_GET_INTERNAL_ORDER_LIST(?, ?, ?, ?, ?, ?, ?); END;";
			conn = Proveedor.getConnection();
		    cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
		    cstmt.setLong(1, plParentOrderId);  //Orden principal
		    cstmt.setLong(2, plNumRegistros);  //Numero de Órdenes a retornar
		    cstmt.setLong(3, plNumPagina);  //Numero de página
			cstmt.registerOutParameter(4, OracleTypes.CURSOR);  //Listado de Órdenes internas
			cstmt.registerOutParameter(5, OracleTypes.VARCHAR); //Total de registros
			cstmt.registerOutParameter(6, OracleTypes.VARCHAR); //Tipo de Órdenes Internas
			cstmt.registerOutParameter(7, OracleTypes.VARCHAR); //Mensaje en caso de error
			cstmt.execute();
			strMessage = cstmt.getString(7);
		    if (StringUtils.isBlank(strMessage)) {
		    	  strTipoOrden = cstmt.getString(6);
		    	  lNumTotalRegistros = cstmt.getLong(5);
            if (lNumTotalRegistros > 0){
                rs = (ResultSet)cstmt.getObject(4);
                if (strTipoOrden.equals(Constante.TIPO_ORDEN_INTERNA_ACTIVATE)){
                  //Recojo las Ordenes de activación
                  while (rs.next()) {
                      HashMap hshInternalOrdenes = new HashMap();
                      hshInternalOrdenes.put("rownum", StringUtils.defaultString(rs.getString("rn")));
                      hshInternalOrdenes.put("nporderid", StringUtils.defaultString(rs.getString("nporderid")));
                      hshInternalOrdenes.put("npordercode", StringUtils.defaultString(rs.getString("npordercode")));
                      hshInternalOrdenes.put("npparentorderid", StringUtils.defaultString(rs.getString("npparentorderid")));
                      hshInternalOrdenes.put("npactivationdate", MiUtil.getDate(rs.getTimestamp("npactivationdate"), "dd/MM/yyyy"));
                      hshInternalOrdenes.put("npprocesstype", StringUtils.defaultString(rs.getString("npprocesstype")));
                      hshInternalOrdenes.put("estadoEjecucion", StringUtils.defaultString(rs.getString("estadoEjecucion")));
                      hshInternalOrdenes.put("npcreateddate", MiUtil.getDate(rs.getTimestamp("npcreateddate"), "dd/MM/yyyy"));
                      arrInternalOrderList.add(hshInternalOrdenes);
                  }	        	
                }else{
                  //Recojo las Ordenes de desactivación
                  while (rs.next()) {
                      HashMap hshInternalOrdenes = new HashMap();
                      hshInternalOrdenes.put("rownum", StringUtils.defaultString(rs.getString("rn")));
                      hshInternalOrdenes.put("nporderid", StringUtils.defaultString(rs.getString("nporderid")));
                      hshInternalOrdenes.put("npordercode", StringUtils.defaultString(rs.getString("npordercode")));
                      hshInternalOrdenes.put("npparentorderid", StringUtils.defaultString(rs.getString("npparentorderid")));
                      hshInternalOrdenes.put("npphone", StringUtils.defaultString(rs.getString("npphone")));
                      hshInternalOrdenes.put("npnomserv", StringUtils.defaultString(rs.getString("npnomserv")));
                      hshInternalOrdenes.put("npactivationdate", MiUtil.getDate(rs.getTimestamp("npactivationdate"), "dd/MM/yyyy"));
                      hshInternalOrdenes.put("npdeactivationdate", MiUtil.getDate(rs.getTimestamp("npdeactivationdate"), "dd/MM/yyyy"));
                      hshInternalOrdenes.put("npprocesstype", StringUtils.defaultString(rs.getString("npprocesstype")));
                      hshInternalOrdenes.put("estadoEjecucion", StringUtils.defaultString(rs.getString("estadoEjecucion")));
                      hshInternalOrdenes.put("npcreateddate", MiUtil.getDate(rs.getTimestamp("npcreateddate"), "dd/MM/yyyy"));
                      arrInternalOrderList.add(hshInternalOrdenes);
                  }
                }
            }
		    }
		    hshDataMap.put("arrInternalOrderList", arrInternalOrderList);
		    hshDataMap.put("lNumTotalRegistros", String.valueOf(lNumTotalRegistros));
		    hshDataMap.put("strTipoOrden", strTipoOrden);
		    hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);  
		}catch(Exception e){
	        logger.error(formatException(e));
	        hshDataMap.put(Constante.MESSAGE_OUTPUT, e.getMessage());  
	    }finally{
	        try{
	          closeObjectsDatabase(conn,cstmt,rs); 
	        }catch (Exception e) {
	          logger.error(formatException(e));
	        }
	    }
	    return hshDataMap;
	}
	
	/**
	 * Motivo: Obtiene la Lista de Ordenes Padres (Jerarquía) una Orden dada<br>
	 * Realizado por: <a href="mailto:luis.silva@asistp.com">Luis Silva</a> <br>
	 * Fecha: 12/11/2010
	 * @param plOrderId ID de la Orden.
	 * @return Listado de Ordenes Padres de la Orden dada como parámetro. La primera
	 * Orden de la lista es la más ancestral.
	 */	
	public HashMap getParentOrderList(long plOrderId){
		HashMap hshDataMap = new HashMap();
		ArrayList arrParentOrderList = new ArrayList();
		Connection conn = null;
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;
		String strMessage = null;
		String sqlStr = null;
		try{
			sqlStr = "BEGIN ORDERS.NP_ORDERS20_PKG.SP_GET_PARENT_ORDER_LIST(?, ?, ?); END;";
			conn = Proveedor.getConnection();
		    cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
		    cstmt.setLong(1, plOrderId);
			cstmt.registerOutParameter(2, OracleTypes.CURSOR);  //Listado de las órdenes padre
			cstmt.registerOutParameter(3, OracleTypes.VARCHAR); //Mensaje en caso de error
			cstmt.execute();
			strMessage = cstmt.getString(3);
		    if (StringUtils.isBlank(strMessage)) {
		    	rs = (ResultSet)cstmt.getObject(2);
		    	while (rs.next()) {
		            HashMap hshInternalOrdenes = new HashMap();
		            hshInternalOrdenes.put("nporderid", StringUtils.defaultString(rs.getString("nporderid")));
		            arrParentOrderList.add(hshInternalOrdenes);		    		
		    	}
		    }
		    hshDataMap.put("arrParentOrderList", arrParentOrderList);
		    hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);  
		}catch(Exception e){
	        logger.error(formatException(e));
	        hshDataMap.put(Constante.MESSAGE_OUTPUT, e.getMessage());  
	    }finally{
	        try{
	          closeObjectsDatabase(conn,cstmt,rs); 
	        }catch (Exception e) {
	          logger.error(formatException(e));
	        }
	    }
	    return hshDataMap;
	}
	
  
	/**
	 * Motivo: Obtiene la Lista de Órdenes <br>
	 * Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard
	 * De los Reyes</a> <br>
	 * Fecha: 12/09/2007.
	 * 
	 * @param objOrderSearchForm
	 * @return ArrayList de Ordenes.
	 */
  public HashMap getOrderList(OrderSearchForm objOrderSearchForm) throws SQLException,Exception{
		if (logger.isDebugEnabled()) {
			logger.debug("--Inicio--");
			logger.debug("Flag de Búsqueda: " + objOrderSearchForm.getIFlag());
			logger.debug("Free Memory: " + Runtime.getRuntime().freeMemory());
			logger.debug("Total Memory: " + Runtime.getRuntime().totalMemory());
			logger.debug("Used Memory: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
		}
		HashMap hshDataMap = new HashMap();
		ArrayList arrOrderList = new ArrayList();
		Connection conn = null;
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;
		String strMessage = null;
    try{
      String sqlStr = "BEGIN ORDERS.NP_ORDERS01_PKG.SP_GET_ORDER_LST(?, ?, ?, ?); END;";
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
      long lNumTotalRegistros = 0;
      int  i = 1;
      StructDescriptor sdOrderSearchForm = StructDescriptor.createDescriptor("ORDERS.TO_ORDER_SEARCH_FORM", conn);        
      String strSalesStructId = objOrderSearchForm.getHdnSalesstructid();
      if(strSalesStructId!=null && strSalesStructId.trim().equals("")){
        strSalesStructId = null;
      }
      
      String strProviderId = objOrderSearchForm.getHdnProviderid();
      if(strProviderId!=null && strProviderId.trim().equals("")){
        strProviderId = null;
      }
	  
      Object[] aoOrderSearchForm = { 
          new Integer(objOrderSearchForm.getIFlag()),

          objOrderSearchForm.getHdnCustomerId(), 
          objOrderSearchForm.getTxtNroOrden(),
          objOrderSearchForm.getTxtNroSolicitud(), 
          objOrderSearchForm.getCmbEstadoOrden(),
          objOrderSearchForm.getCmbDivisionNegocio(), 
          objOrderSearchForm.getCmbSolucionNegocio(),
          objOrderSearchForm.getCmbCategoria(), 
          objOrderSearchForm.getCmbSubCategoria(),
          //objOrderSearchForm.getCmbZona(), 
          //objOrderSearchForm.getCmbConsultorEjecutivo(),
          objOrderSearchForm.getCmbRegion(), 
          
          objOrderSearchForm.getCmbTienda(),
          objOrderSearchForm.getTxtCreadoPor(), 
          objOrderSearchForm.getTxtCreateDateFrom(),
          objOrderSearchForm.getTxtCreateDateTill(), 
          objOrderSearchForm.getTxtNroReparacion(),
          objOrderSearchForm.getTxtNextel(), 
          objOrderSearchForm.getCmbTipoServicio(),
          objOrderSearchForm.getCmbModelo(), 
          objOrderSearchForm.getCmbTipoFalla(),
          objOrderSearchForm.getTxtImei(), 
          
          objOrderSearchForm.getCmbSituacion(),
          objOrderSearchForm.getCmbTecnicoResponsable(), 
          objOrderSearchForm.getCmbEstadoReparacion(),
          objOrderSearchForm.getHdnNumRegistros(), 
          objOrderSearchForm.getHdnNumPagina(),
          objOrderSearchForm.getTxtImeiCambio(),//26
          objOrderSearchForm.getTxtImeiPrestamo(),//27
          objOrderSearchForm.getTxtNroGuia(), 
          objOrderSearchForm.getHdnSubCategoria(),          
          objOrderSearchForm.getCmbTipoProceso(), 
          
          //objOrderSearchForm.getHdnCoordinador(),
          //objOrderSearchForm.getHdnZona(), 
          //objOrderSearchForm.getHdnSupervisor(),
          //objOrderSearchForm.getHdnConsultorEjecutivo(), 
          //objOrderSearchForm.getCmbSupervisor(),
          //objOrderSearchForm.getCmbCoordinador(),
          
          //Nuevos campos para Jerarquía:
          (strSalesStructId==null?"0":new Integer(strSalesStructId)), 
          (strProviderId==null?"0":new Integer(strProviderId)),
          objOrderSearchForm.getTxtProposedId(), //CBARZOLA 02/08/2009
          // INICIO - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 1/10/2010
          objOrderSearchForm. getCmbTipoEquipo(),
          // FIN - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 1/10/2010
          objOrderSearchForm.getChkNumberSearch(), // MFAUDA 15/02/2012
          objOrderSearchForm.getTxtNumber(),        // MFAUDA 15/02/2012          
          objOrderSearchForm.getcmbRangoCuenta(),        // OCRUCES 29/10/2012  
          
          objOrderSearchForm.getCmbMarcaDap() ,        // POrtega 12/03/2014
          objOrderSearchForm.getCmbEstadoOrdenExterna(),        // POrtega 12/03/2014
          objOrderSearchForm.getCmbRespuestaCotizacion(),        // POrtega 12/03/2014
          objOrderSearchForm.getCmbSituacionEquipo(),       // POrtega 12/03/2014
          objOrderSearchForm.getCmbTiendaRecojo(),        // POrtega 12/03/2014

          objOrderSearchForm.getCmbSegmentoCompania(),     // PZacarias 21/01/2017

          objOrderSearchForm.getCmbWebPayment() //PRY-1239 19/09/2018
      };/* */
        System.out.println("===INICIO FOR===");
        int ix=1;
        for(Object obj:aoOrderSearchForm){
            System.out.println("obj2["+ix+"]: "+obj);
            ix++;
        }
            System.out.println("TERMINO FOR");
		//objOrderSearchForm.getCmbTipoProceso(), objOrderSearchForm.getCmbCoordinador()          
      STRUCT stcOrderSearchForm = new STRUCT(sdOrderSearchForm, conn, aoOrderSearchForm);
      cstmt.setSTRUCT(1, stcOrderSearchForm);
      //cstmt.setObject(1, (STRUCT)stcOrderSearchForm);
      cstmt.registerOutParameter(2, OracleTypes.CURSOR);
      cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
      cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
      cstmt.execute();
      strMessage = cstmt.getString(4); 
      if (StringUtils.isBlank(strMessage)) {
        lNumTotalRegistros = cstmt.getLong(3);
        rs = (ResultSet)cstmt.getObject(2);
        if (objOrderSearchForm.getIFlag() == Constante.FLAG_BUSQUEDA_ORDEN) {
          while (rs.next()) {
            HashMap hshOrdenes = new HashMap();
            hshOrdenes.put("rownum", StringUtils.defaultString(rs.getString("rn")));
            hshOrdenes.put("nporderid", StringUtils.defaultString(rs.getString("nporderid")));
            hshOrdenes.put("npordercode", StringUtils.defaultString(rs.getString("npordercode")));
            hshOrdenes.put("swruc", StringUtils.defaultString(rs.getString("swruc")));
            hshOrdenes.put("swname", StringUtils.defaultString(rs.getString("swname")));
            hshOrdenes.put("swsitename", StringUtils.defaultString(rs.getString("swsitename")));
            hshOrdenes.put("npsolutionname", StringUtils.defaultString(rs.getString("npsolutionname")));
            hshOrdenes.put("nptype", StringUtils.defaultString(rs.getString("nptype")));
            hshOrdenes.put("npspecification", StringUtils.defaultString(rs.getString("npspecification")));
            hshOrdenes.put("npstatus", StringUtils.defaultString(rs.getString("npstatus")));
            hshOrdenes.put("npsalesmanname", StringUtils.defaultString(rs.getString("npsalesmanname")));
            hshOrdenes.put("npdealername", StringUtils.defaultString(rs.getString("npdealername")));
            hshOrdenes.put("unitcount", StringUtils.defaultString(rs.getString("unitcount")));
            hshOrdenes.put("npcreatedby", StringUtils.defaultString(rs.getString("npcreatedby")));
            hshOrdenes.put("npcreateddate", MiUtil.getDate(rs.getTimestamp("npcreateddate"), "dd/MM/yyyy"));               
            hshOrdenes.put("vchwebpayment", StringUtils.defaultString(rs.getString("vchwebpayment"))); //PRY-1239 19/09/2018 PCACERES
            arrOrderList.add(hshOrdenes);
          }
        } else if (objOrderSearchForm.getIFlag() == Constante.FLAG_BUSQUEDA_REPARACION) {
          while (rs.next()) {
            HashMap hshOrdenes = new HashMap();
            
            hshOrdenes.put("rownum", StringUtils.defaultString(rs.getString(i++)));
         hshOrdenes.put("nporderid", rs.getString(i++)); // NRO ORDEN
            hshOrdenes.put("swname", rs.getString(i++)); // RAZON // SOCIAL
            hshOrdenes.put("npsolutionid", rs.getString(i++));
            hshOrdenes.put("npsolutionname", rs.getString(i++));
            hshOrdenes.put("nptype", rs.getString(i++)); // CATEGORIA
            hshOrdenes.put("npspecification", rs.getString(i++)); // SUBCATEGORIA
            hshOrdenes.put("npspecificationid", rs.getString(i++));
            hshOrdenes.put("npname", rs.getString(i++)); // TIENDA
            hshOrdenes.put("npstatusorder", rs.getString(i++)); // ESTADO ORDEN
            hshOrdenes.put("nprepairid", rs.getString(i++)); // NRO REPARACION
            hshOrdenes.put("npphone", rs.getString(i++));
            hshOrdenes.put("npmodel", rs.getString(i++));
            // INICIO - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 1/10/2010
            hshOrdenes.put("nptipoequipo", rs.getString(i++));
            // FIN - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 1/10/2010
            hshOrdenes.put("npimei", rs.getString(i++));
            hshOrdenes.put("nprepairtype", rs.getString(i++)); // TIPO REPARACION
            hshOrdenes.put("npstatusrepair", rs.getString(i++)); // ESTADO REPARACION
            hshOrdenes.put("nxfalla", rs.getString(i++)); // FALLA
            hshOrdenes.put("npendsituation", rs.getString(i++)); // SITUACION FINAL
            hshOrdenes.put("npusername1", rs.getString(i++)); // TECNICO RESPONSABLE
            hshOrdenes.put("npcreateddate", MiUtil.toFecha(rs.getDate(i++)));
            hshOrdenes.put("npcreatedby", rs.getString(i++));
            hshOrdenes.put("npimeicambio", rs.getString(i++));
            hshOrdenes.put("npimeiprestamo", rs.getString(i++));					
            arrOrderList.add(hshOrdenes);
            
            i=1;
          }
        }
      }
            
      hshDataMap.put("arrOrderList", arrOrderList);
      hshDataMap.put("lNumTotalRegistros", String.valueOf(lNumTotalRegistros));
      hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);  
    }catch(Exception e){
        logger.error(formatException(e));
        hshDataMap.put(Constante.MESSAGE_OUTPUT, e.getMessage());  
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,rs); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
     }    
		
		if (logger.isDebugEnabled()) {
			logger.debug("Antes del return");
			logger.debug("Free Memory: " + Runtime.getRuntime().freeMemory());
			logger.debug("Total Memory: " + Runtime.getRuntime().totalMemory());
			logger.debug("Used Memory: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
		}
		return hshDataMap;
	}


	/**
     * Motivo: Seguimiento de una Orden. (Historia de Accion)
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 02/10/2007
     * 
     * @param		strOrderId     Ej: Id de la Orden
     * @return		ArrayList de HashMap      
     */
    public ArrayList getHistoryActionListByOrder(long lOrderId)throws Exception,SQLException {
		Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		String error;
        try {
            String sqlStr = "BEGIN ORDERS.NP_ORDERS09_PKG.SP_GET_ORDER_ACTION_HISTORY(?,?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, String.valueOf(lOrderId));
			cstmt.registerOutParameter(2, OracleTypes.CURSOR);
			cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();
			rs = (ResultSet)cstmt.getObject(2);
            error = cstmt.getString(3);
            if (StringUtils.isNotEmpty(error)) {
                throw new SQLException(error);
            }
            while (rs.next()) {
                HashMap objHashMap = new HashMap();
				int i = 1;
				objHashMap.put("swsender", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("swaction", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("swreceiver", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("swmessagetype", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("swdatereceived", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("swactiontaken", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("swdateactiontaken", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("swactiontakenby", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("swpriority", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("swactionrequired", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("swconfirmrequested", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("swnote", StringUtils.defaultString(rs.getString(i++),""));

				arrList.add(objHashMap);
            }
        } catch (SQLException sqle) {
            logger.error(formatException(sqle));
        } finally {
				closeObjectsDatabase(conn,cstmt,rs); 
            /*try {                
               if (rs != null)
                rs.close();     
               if (cstmt != null)
                cstmt.close();               
               if (conn != null)
                conn.close();
                
            } catch (Exception e) {
                logger.error(formatException(e));
            }*/
        }
        return arrList;
    }
	
	/**
     * Motivo: Historial de la Aprobación de una Orden.
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 03/10/2007
     * 
     * @param		strOrderId     Ej: Id de la Orden
     * @return		ArrayList de HashMap      
     */
    public ArrayList getHistoryApproveListByOrder(long lOrderId) throws Exception,SQLException{
		Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		String error;
        try {
            String sqlStr = "BEGIN ORDERS.NP_ORDERS09_PKG.SP_GET_ORDER_APPROVE_HISTORY(?,?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, String.valueOf(lOrderId));
			cstmt.registerOutParameter(2, OracleTypes.CURSOR);
			cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();
			rs = (ResultSet)cstmt.getObject(2);
            error = cstmt.getString(3);
            if (StringUtils.isNotEmpty(error)) {
                throw new SQLException(error);
            }
            while (rs.next()) {
                HashMap objHashMap = new HashMap();
				int i = 1;
				objHashMap.put("swagent", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("swaction", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("swreceiver", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("swmessagetype", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("swdatereceived", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("swactiontaken", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("swdateactiontaken", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("swactiontakenby", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("swpriority", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("swactionrequired", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("swconfirmrequested", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("swnote", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("npvaluedesc", StringUtils.defaultString(rs.getString(i++),""));

				arrList.add(objHashMap);
            }
        } catch (SQLException sqle) {
            logger.error(formatException(sqle));
            throw new Exception(sqle);
        } finally {
				closeObjectsDatabase(conn,cstmt,rs); 
            /*try {
                if (rs != null)
                   rs.close();     
                if (cstmt != null)
                   cstmt.close();               
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
                logger.error(formatException(e));
            }*/
        }
        return arrList;
    }
	
	/**
     * Motivo: Lista de Comentarios
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 03/10/2007
     * 
     * @param		lOrderId     Ej: Id de la Orden
     * @return		ArrayList de CommentBean
	 * @see CommentBean
     */
    public ArrayList getCommentByOrderList(long lOrderId)throws Exception,SQLException{
		Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		String strError;
        try {
            String sqlStr = "BEGIN ORDERS.NP_ORDERS08_PKG.SP_GET_ORDER_COMMENT_LST(?,?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1, lOrderId);
            cstmt.registerOutParameter(2, OracleTypes.ARRAY, "ORDERS.TT_COMMENT_LST");
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            //ARRAY aryComentarioList = cstmt.getARRAY(2);
            ARRAY aryComentarioList = (ARRAY)cstmt.getObject(2);
            strError = cstmt.getString(3);
            if (StringUtils.isNotEmpty(strError)) {
                logger.error(strError);
                return null;
            }
        try {
          for (int i = 0; i < aryComentarioList.getOracleArray().length; i++) {
            int a = 0;
              STRUCT stcComentario = (STRUCT) aryComentarioList.getOracleArray()[i];
              CommentBean objCommentBean = new CommentBean();
              objCommentBean.setNpCommentId(MiUtil.defaultBigDecimal(stcComentario.getAttributes()[a++], new BigDecimal(0)).longValue());
              objCommentBean.setNpOrderId(MiUtil.defaultBigDecimal(stcComentario.getAttributes()[a++], new BigDecimal(0)).longValue());
              objCommentBean.setNpAction(MiUtil.defaultString(stcComentario.getAttributes()[a++], ""));
              objCommentBean.setNpSubject(MiUtil.defaultString(stcComentario.getAttributes()[a++], ""));
              objCommentBean.setNpComment(MiUtil.defaultString(stcComentario.getAttributes()[a++], ""));
              objCommentBean.setNpCreatedDate(MiUtil.defaultString(stcComentario.getAttributes()[a++], ""));
              objCommentBean.setNpCreatedBy(MiUtil.defaultString(stcComentario.getAttributes()[a++], ""));
              arrList.add(i, objCommentBean);
          }
        }
        catch (NullPointerException npe) {
          logger.error(formatException(npe));
        }
      } catch (SQLException sqle) {
            logger.error(formatException(sqle));
        } finally {
				closeObjectsDatabase(conn,cstmt,rs); 				
            /*try {
                if (rs != null)
                   rs.close();     
                if (cstmt != null)
                   cstmt.close();               
                if (conn != null)
                   conn.close();
              } catch (Exception e) {
                logger.error(formatException(e));
            }*/
        }
        return arrList;
    }

  /**
   * Motivo: Agrega un Comentario
   * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
   * <br>Fecha: 04/10/2007
   *
   * @param		objCommentBean - CommentBean, contiene la información del Comentario (Nota)  
  **/
  
  public void addComment(CommentBean objCommentBean) throws Exception,SQLException{
		Connection conn = null;
    OracleCallableStatement cstmt = null;
		String strError;
    try {
    
      String sqlStr = "BEGIN ORDERS.NP_ORDERS08_PKG.SP_INS_ORDER_COMMENT(?,?); END;";
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
			StructDescriptor sdComment = StructDescriptor.createDescriptor("ORDERS.TO_COMMENT", conn);
			Object[] objSolution = { null,
                  String.valueOf(objCommentBean.getNpOrderId()),
								  objCommentBean.getNpAction(),
                  objCommentBean.getNpSubject(),
								  objCommentBean.getNpComment(),
								  null,
								  objCommentBean.getNpCreatedBy()
								  };
                  
			STRUCT stcComment = new STRUCT(sdComment, conn, objSolution);
			cstmt.setSTRUCT(1, stcComment);
      //cstmt.setObject(1, (STRUCT)stcComment);
			cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
      cstmt.executeQuery();
      
			strError = cstmt.getString(2);
      if (StringUtils.isNotEmpty(strError)) {
        logger.error(strError);
      }
      
    } catch (SQLException sqle) {
            logger.error(formatException(sqle));
    } finally {
        try {
            closeObjectsDatabase(conn,cstmt,null);
         } catch (Exception e) {
            logger.error(formatException(e));
        }
    }
    }
	
	/**
     * Motivo: Lista de Documentos de Inventario
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 03/10/2007
     * 
     * @param		lOrderId     Ej: Id de la Orden
     * @return		ArrayList de HashMap      
     */
    public HashMap getInventoryList(long lOrderId) throws SQLException, Exception {
		Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
		HashMap hshDataMap = new HashMap();
		ArrayList arrInventoryList = new ArrayList();
		String strMessage;
    try{
      String sqlStr = "BEGIN INVENTORY.SPI_GET_INV_TX_X_ORDER(?,?,?); END;";
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
      cstmt.setLong(1, lOrderId);
      cstmt.registerOutParameter(2, OracleTypes.CURSOR);
      cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
      cstmt.executeQuery();
        strMessage = cstmt.getString(3);
        if (strMessage == null){
           rs = (ResultSet)cstmt.getObject(2); 		
           while(rs.next()) {
              HashMap objInventoryMap = new HashMap();
              
              arrInventoryList.add(objInventoryMap);
           }
        }
      
      hshDataMap.put("arrInventoryList", arrInventoryList);
      hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);  
    }catch (Exception e) {
      logger.error(formatException(e));
      hshDataMap.put(Constante.MESSAGE_OUTPUT, e.getMessage()); 
    }
    finally{
      try{
         closeObjectsDatabase(conn,cstmt,rs); 
      }catch (Exception e) {
        logger.error(formatException(e));
       }
    }    
    
        return hshDataMap;	
	}
	
	/**
     * Motivo: Lista de Facturas
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 03/10/2007
     * 
     * @param		lOrderId     Ej: Id de la Orden
     * @return		ArrayList de HashMap      
     */
    public HashMap getInvoiceList(long lOrderId) throws SQLException, Exception {	
      Connection conn = null;
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;
      HashMap hshDataMap = new HashMap();
      ArrayList arrInvoiceList = new ArrayList();
      HashMap objInventoryMap =null;
      String strMessage;
      System.out.println("[OrderDAO][getInvoiceList]"+lOrderId);
      try{
        String sqlStr = "BEGIN INVOICE.SPI_GET_SALE_TRX_ORDER(?,?,?); END;";
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
        cstmt.setLong(1, lOrderId);
        cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(3, OracleTypes.CURSOR);
        cstmt.executeQuery();
          strMessage = cstmt.getString(2);
          if (strMessage == null){
             rs = (ResultSet)cstmt.getObject(3); 		
             int i=1;
             while(rs.next()) {
                objInventoryMap = new HashMap();
                objInventoryMap.put("NPSALETRXID", rs.getString("NPSALETRXID"));
                objInventoryMap.put("NPBUILDINGID", rs.getString("NPBUILDINGID")); //
                objInventoryMap.put("NPLASTUPDATEDATE", rs.getString("NPLASTUPDATEDATE"));
                objInventoryMap.put("NPLASTUPDATEDBY", rs.getString("NPLASTUPDATEDBY"));
                objInventoryMap.put("NPCREATIONDATE",MiUtil.toFecha(rs.getDate("NPCREATIONDATE"))); //
                objInventoryMap.put("NPCREATEDBY", rs.getString("NPCREATEDBY"));
                objInventoryMap.put("NPSALETRXTYPE", rs.getString("NPSALETRXTYPE")); //
                objInventoryMap.put("NPSALETRXNUMBER", rs.getString("NPSALETRXNUMBER"));
                objInventoryMap.put("NPNUMERATIONID", rs.getString("NPNUMERATIONID"));
                objInventoryMap.put("NPDOCSERIAL", rs.getString("NPDOCSERIAL"));
                objInventoryMap.put("NPDOCSEQUENCE", rs.getString("NPDOCSEQUENCE")); //--
                objInventoryMap.put("NPORDERID", rs.getString("NPORDERID"));
                objInventoryMap.put("NPSALEAMOUNT", rs.getString("NPSALEAMOUNT")); //
                objInventoryMap.put("NPSALEDATE", rs.getString("NPSALEDATE"));
                objInventoryMap.put("NPCURRENCYCODTYPE", rs.getString("NPCURRENCYCODTYPE")); //
                objInventoryMap.put("GLOSA", rs.getString("NPGLOSA"));
                objInventoryMap.put("NPPAYMENTDATE", rs.getString("NPPAYMENTDATE"));
                objInventoryMap.put("NPSALEDOCREFTYPE", rs.getString("NPSALEDOCREFTYPE"));
                objInventoryMap.put("NPSALEDOCREFTYPE", rs.getString("NPSALEDOCREFID"));
                objInventoryMap.put("NPCUSTOMERID", rs.getString("NPCUSTOMERID"));
                objInventoryMap.put("NPCUSTOMERNAME", rs.getString("NPCUSTOMERNAME")); //
                objInventoryMap.put("NPBILLADDRESSID", rs.getString("NPBILLADDRESSID"));
                objInventoryMap.put("NPBILLADDRESS", rs.getString("NPBILLADDRESS"));
                objInventoryMap.put("NPSHIPADDRESSID", rs.getString("NPSHIPADDRESSID"));
                objInventoryMap.put("NPSHIPADDRESS", rs.getString("NPSHIPADDRESS"));
                objInventoryMap.put("NPSALEPENDAMOUNT", rs.getString("NPSALEPENDAMOUNT"));
                objInventoryMap.put("NPSALETRXDESTTYPE", rs.getString("NPSALETRXDESTTYPE"));
                objInventoryMap.put("NPCUSTOMERTAXNUMBER", rs.getString("NPCUSTOMERTAXNUMBER")); //
                objInventoryMap.put("NPEXCHANGERATE", rs.getString("NPEXCHANGERATE"));
                objInventoryMap.put("NPORDERTAXAMOUNT", rs.getString("NPORDERTAXAMOUNT"));
                objInventoryMap.put("NPORDERINVOICEAMOUNT", rs.getString("NPORDERINVOICEAMOUNT"));
                objInventoryMap.put("NPCUSTOMERADDRESS", rs.getString("NPCUSTOMERADDRESS"));
                objInventoryMap.put("NPTERMS", rs.getString("NPTERMS"));
                objInventoryMap.put("NPDUEDATE", rs.getString("NPDUEDATE"));
                objInventoryMap.put("NPPAYMENTORDER", rs.getString("NPPAYMENTORDER"));
                objInventoryMap.put("NPOFTRANSFERSTATUS", rs.getString("NPOFTRANSFERSTATUS"));
                objInventoryMap.put("NPPAYMENTMETHOD", rs.getString("NPPAYMENTMETHOD"));
                objInventoryMap.put("NPPRINTSTATUS", rs.getString("NPPRINTSTATUS"));
                objInventoryMap.put("NPSTATUSDOC", rs.getString("NPSTATUSDOC")); //
                objInventoryMap.put("NPTYPEOPER", rs.getString("NPTYPEOPER"));
                objInventoryMap.put("NPTRXTYPE", rs.getString("NPTRXTYPE"));
                objInventoryMap.put("NPIDEXPORT", rs.getString("NPIDEXPORT"));
                objInventoryMap.put("NPDESCLOG", rs.getString("NPDESCLOG"));
                objInventoryMap.put("NPCUSTOMERADDRESSID", rs.getString("NPCUSTOMERADDRESSID"));
                objInventoryMap.put("NPGUIAREM", rs.getString("NPGUIAREM"));
                objInventoryMap.put("NPSOURCE", rs.getString("NPSOURCE"));			                                                
                objInventoryMap.put("NPFELPREFIX", rs.getString("NPFELPREFIX"));
				objInventoryMap.put("NPSTATUSSUNAT", rs.getString("NPSTATUSSUNAT"));
                arrInvoiceList.add(objInventoryMap);
             }
          }       
          hshDataMap.put("arrInvoiceList", arrInvoiceList);
          hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);  
      }catch (Exception e) {
        logger.error(formatException(e));
         hshDataMap.put(Constante.MESSAGE_OUTPUT, e.getMessage());  
      }
      finally{
        try{
           closeObjectsDatabase(conn,cstmt,rs); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
      }    
      
      return hshDataMap;
    }
    
     
   /**
       Method : getInventoryOrder
       Purpose: Lista de Guias asociadas a una orden
       Developer       Fecha       Comentario
       =============   ==========  ======================================================================
       Carmen Gremios  08/02/2008  Creación
       Tomás Mogrovejo 25/06/2008  Se agregó las llamadas al Id de la transacción y el tipo de la transacción 
    */
          
    public HashMap getInventoryOrder(long lOrderId) throws SQLException, Exception {	
      Connection conn = null;
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;
		HashMap hshDataMap = new HashMap();
      HashMap objInventoryMap=null;
		ArrayList arrInventoryList = new ArrayList();
		String strMessage;            
    try{
      String sqlStr = "BEGIN INVENTORY.SPI_GET_INV_TX_X_ORDERC(?,?,?); END;";
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
       cstmt.setLong(1, lOrderId);		
      cstmt.registerOutParameter(2, OracleTypes.CURSOR);
        cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
      cstmt.executeQuery();
        strMessage = cstmt.getString(3);
        if (strMessage == null){
           rs = (ResultSet)cstmt.getObject(2); 		
           while(rs.next()) {
              objInventoryMap = new HashMap();                  
              objInventoryMap.put("NPTRANSNNUMBER", rs.getString("NPTRANSNNUMBER")); // Número de Transacción     
              objInventoryMap.put("NPTRANSACTIONDATE", MiUtil.toFecha(rs.getDate("NPTRANSACTIONDATE")));//Fecha de Transacción	 
              objInventoryMap.put("NPCUSTOMERNAME", rs.getString("NPCUSTOMERNAME"));// Nombre de cliente  
              objInventoryMap.put("NPCUSTOMERTAXNUMBER", rs.getString("NPCUSTOMERTAXNUMBER")); // Ruc del cliente  
              String strTypeDoc = rs.getString(5);            
              objInventoryMap.put("WV_NPDOCUMENTPARENTTYPENAME", strTypeDoc);//descripcion Tipos de Documentos de Inventario 
              objInventoryMap.put("NPVALUEDESC", rs.getString("NPVALUEDESC"));//estado del documento de inventario --Agregado por KSALVADOR 
              objInventoryMap.put("STATUSPRINTER", rs.getString(7));//estado de impresión  del documento de inventario --Agregado por KSALVADOR 
              objInventoryMap.put("NPOPERATIONTYPEID",rs.getString("NPOPERATIONTYPEID")); //Tipo de Operacion
              objInventoryMap.put("NPTRANSACTIONID",rs.getString("NPTRANSACTIONID")); //Id de la Transacción
              objInventoryMap.put("NPSALETRXTYPE",rs.getString("NPSALETRXTYPE")); //Tipo de Transacción   
              //objInventoryMap.put("DOCREFNUMBER", rs.getString("npattribute4"));   
              System.out.println("[OrderDAO][getInventoryOrder]"+strTypeDoc);
              if (strTypeDoc.equals("GUIA REMISION"))
                 arrInventoryList.add(objInventoryMap);      
           }
        }
          
        hshDataMap.put("arrInventoryList", arrInventoryList);
        hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);  
    }catch (Exception e) {
        logger.error(formatException(e));
         hshDataMap.put(Constante.MESSAGE_OUTPUT, e.getMessage());  
      }
      finally{
        try{
           closeObjectsDatabase(conn,cstmt,rs); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
      }    
    
      return hshDataMap;  
    }     
    
            
     /**
       Method : getInventoryPIOrder
       Purpose: Lista de Partes de Ingreso asociados a una orden
       Developer       Fecha       Comentario
       =============   ==========  ======================================================================
       Hugo Moreno     15/04/2008  Creación
       Tomás Mogrovejo 25/06/2008  Se agregó las llamadas al Id de la transacción y el tipo de la transacción
    */            
     
    public HashMap getInventoryPIOrder(long lOrderId) throws SQLException, Exception {	
      Connection conn = null;
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;
      HashMap hshDataMap = new HashMap();
      HashMap objInventoryPIMap=null;
      ArrayList arrInventoryPIList = new ArrayList();
      String strMessage;      
      System.out.println("[OrderDAO][getInventoryPIOrder]"+lOrderId);
      try{
        String sqlStr = "BEGIN INVENTORY.SPI_GET_INV_TX_X_ORDERC(?,?,?); END;";
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
        cstmt.setLong(1, lOrderId);		
        cstmt.registerOutParameter(2, OracleTypes.CURSOR);
        cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
        cstmt.executeQuery();
        strMessage = cstmt.getString(3);
        if (strMessage == null){
           rs = (ResultSet)cstmt.getObject(2); 		
           while(rs.next()) {
              objInventoryPIMap = new HashMap();                  
              objInventoryPIMap.put("NPTRANSNNUMBER", rs.getString("NPTRANSNNUMBER")); // Número de Transacción     
              objInventoryPIMap.put("NPTRANSACTIONDATE", MiUtil.toFecha(rs.getDate("NPTRANSACTIONDATE")));//Fecha de Transacción	 
              objInventoryPIMap.put("NPCUSTOMERNAME", rs.getString("NPCUSTOMERNAME"));// Nombre de cliente  
              objInventoryPIMap.put("NPCUSTOMERTAXNUMBER", rs.getString("NPCUSTOMERTAXNUMBER")); // Ruc del cliente  
              String strTypeDoc = rs.getString(5);            
              objInventoryPIMap.put("WV_NPDOCUMENTPARENTTYPENAME", strTypeDoc);//descripcion Tipos de Documentos de Inventario 
              objInventoryPIMap.put("NPVALUEDESC", rs.getString("NPVALUEDESC"));//estado del documento de inventario --Agregado por KSALVADOR 
              objInventoryPIMap.put("STATUSPRINTER", rs.getString(7));//estado de impresión  del documento de inventario --Agregado por KSALVADOR 
              objInventoryPIMap.put("NPOPERATIONTYPEID",rs.getString("NPOPERATIONTYPEID")); //Tipo de Operacion             
              objInventoryPIMap.put("NPTRANSACTIONID",rs.getString("NPTRANSACTIONID")); //Id de la Transacción
              objInventoryPIMap.put("NPSALETRXTYPE",rs.getString("NPSALETRXTYPE")); //Tipo de Transacción 
              //objInventoryPIMap.put("DOCREFNUMBER", rs.getString("npattribute4")); 
              System.out.println("[OrderDAO][getInventoryOrder]"+strTypeDoc);
              if (strTypeDoc.equals("PARTE INGRESO"))         
                 arrInventoryPIList.add(objInventoryPIMap);               
           }
        }
           
        hshDataMap.put("arrInventoryPIList", arrInventoryPIList);
        hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);  
      }catch (Exception e) {
        logger.error(formatException(e));
         hshDataMap.put(Constante.MESSAGE_OUTPUT, e.getMessage());  
      }
      finally{
        try{
           closeObjectsDatabase(conn,cstmt,rs); 
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
     *  FECHA: 25/09/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/

    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - INICIO
     *  REALIZADO POR: José María Espinoza Bueno
     *  FECHA: 24/10/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/

    /**
     Method : getAddendasList
     Purpose: Obtener la lista de plantillas asociadas a un plan o una promoción.
     Developer                        Fecha       Comentario
     =============                  ==========  ======================================================================
     José María Espinoza Bueno.     24/10/2007  Creación
     Estefanía Gamonal              12/06/2008  Se agregó el id de la categoría de la orden como parámetro de entrada.
     */

    public HashMap getAddendasList(int id_prom, int id_plan, int id_specification, int id_kit)  throws SQLException, Exception {      
      ArrayList list                = new ArrayList();
      int ind                       = 0;  
      Connection conn               = null; 
      OracleCallableStatement cstmt = null;
      ResultSet rs                  = null;
      String strMessage             = null;
      HashMap objHashMap            = new HashMap();        
      try{
        String sqlStr = "BEGIN ADDENDUM.SPI_GET_TEMPLATE_LST(?,?,?,?,?,?); END;";    
        conn = Proveedor.getConnection();      
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);      
        cstmt.setLong(++ind,id_prom);
        cstmt.setLong(++ind,id_plan);
        cstmt.setLong(++ind,id_specification);
        cstmt.setLong(++ind,id_kit);
        cstmt.registerOutParameter(++ind, OracleTypes.CURSOR );
        cstmt.registerOutParameter(++ind,OracleTypes.VARCHAR);      
        cstmt.execute();
        strMessage = cstmt.getString(6);
        if(strMessage==null){
           rs = (ResultSet)cstmt.getObject(5);
           while (rs.next()) {      
              TemplateAdendumBean templateAdendumBean = new TemplateAdendumBean();                         
              templateAdendumBean.setNptemplateid(rs.getInt(1));        // Id de plantilla
              templateAdendumBean.setNptemplatedesc(rs.getString(2));   // Descripción de la plantilla
              templateAdendumBean.setNpaddendumterm(rs.getInt(3));      // Plazo de la adenda
              templateAdendumBean.setNpcreateddate(rs.getDate(4));      // Fecha de Creación
              templateAdendumBean.setNptemplatedefa(rs.getString(5));   // plantilla por defecto (si o no) 
              templateAdendumBean.setNpaddendtype(rs.getString(6));     // Tipo de adenda (1=promocion, 2= plan, 3=servicio)
              templateAdendumBean.setNpbenefittype(rs.getString(7));    // Beneficio al que se aplica la adenda: 1= Al precio del equipo 2= A la Cuota de Inscripción , 3= A la Renta del Plan              
              list.add(templateAdendumBean);
           }
        }               
        objHashMap.put("strMessage",strMessage);
        objHashMap.put("objArrayList",list);       
      }catch (Exception e) {
        logger.error(formatException(e));
        objHashMap.put(strMessage, e.getMessage());  
      }
      finally{
        try{
           closeObjectsDatabase(conn,cstmt,rs); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
      }    
      
      return objHashMap;
    }     

    /*
     Method : insertTemplateOrder
     Purpose: Registrar las plantillas de adendas asoaciadas a una orden.
     Developer                       Fecha       Comentario
     =============                   ==========  ======================================================================
     José María Espinoza Bueno.      24/10/2007  Creación
     */

    public String insertTemplateOrder(int num_order, 
                                      int item_id, 
                                      String template_ids, 
                                      String imei,
                                      double descount,
                                      String user, 
                                      Connection conn) throws SQLException, Exception {
      
      ArrayList list = new ArrayList();
      int template_id;   
      int term = 0;
      OracleCallableStatement cstmt = null;
      String strMsg = null;     
      
      logger.debug("[Input][num_order]: " + num_order);
      logger.debug("[Input][item_id]: " + item_id);
      logger.debug("[Input][template_ids]: " + template_ids);
      logger.debug("[Input][imei]: " + imei);
      logger.debug("[Input][descount]: " + descount);
      logger.debug("[Input][user]: " + user);
      
      try{
        String [] cadenas = template_ids.split(";",template_ids.split(";").length);
        for(int j=0 ;j < cadenas.length ; j++){
           int ind = 0;
           String [] template = cadenas[j].split("-",cadenas[j].split("-").length);
           if((j+1) == cadenas.length){             
               template[1] = StringUtils.remove(template[1],";");
           }             
           template_id = MiUtil.parseInt(template[0]);
           term = MiUtil.parseInt(template[1]);         
           String sqlStr = "BEGIN ORDERS.NP_ORDERS13_PKG.SP_TEMPLATE_ORDER_SAVE(?,?,?,?,?,?,?,?); END;";    
           
           logger.info("[ORDERS.NP_ORDERS13_PKG.SP_TEMPLATE_ORDER_SAVE]");
           
           cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);        
           cstmt.setInt(++ind,num_order);
           cstmt.setInt(++ind,item_id);
           cstmt.setInt(++ind,template_id);
           cstmt.setString(++ind,imei);
           cstmt.setInt(++ind,term);
           cstmt.setDouble(++ind,descount);
           cstmt.setString(++ind,user);
           cstmt.registerOutParameter(++ind,OracleTypes.VARCHAR);
           cstmt.executeUpdate();          
           strMsg = cstmt.getString(8); 
           
           logger.info("[Input][strMessage]: " + strMsg);
        }       
      }catch (Exception e) {
        logger.error(formatException(e));
        strMsg = e.getMessage();  
      }
      finally{
        try{
           closeObjectsDatabase(null,cstmt,null); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
      }    
        
      return strMsg;
    }    
    
    
    /*
     Method : getNpAllowAdenda
     Purpose: Obtener un flag para ver si se muestra o no las adendas para una especificacion dada.
     Developer                       Fecha       Comentario
     =============                   ==========  ======================================================================
     José María Espinoza Bueno.      24/10/2007  Creación
     */

    public String getNpAllowAdenda(int id_specification) throws SQLException, Exception {
      
      Connection conn = null;    
      OracleCallableStatement cstmt = null;   
      String strMsg = null;
      String strAllowAdenda = null;
      int ind = 0;      
      try{
        String sqlStr = "BEGIN ORDERS.NP_ORDERS13_PKG.SP_GET_NPALLOWADENDA(?,?,?); END;";    
        conn = Proveedor.getConnection();          
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);      
        cstmt.setInt(++ind,id_specification);
        cstmt.registerOutParameter(++ind,OracleTypes.VARCHAR);
        cstmt.registerOutParameter(++ind,OracleTypes.VARCHAR);
        cstmt.executeUpdate(); 
        strAllowAdenda = cstmt.getString(2); 
        strMsg = cstmt.getString(3);            
        if (strMsg != null){         
           return strMsg;
        }       
      }catch (Exception e) {
        throw new Exception(e);
      }finally{
        try{
           closeObjectsDatabase(conn,cstmt,null); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
      }    
      
      return strAllowAdenda;
    }  
    
  /*
     Method : getAdendaValidation
     Purpose: Obtener un flag para ver si se la seccion de adendas para una especificacion dada se bloquea o no.
     Developer                     Fecha       Comentario
     =============                ==========  ======================================================================
     Javier Villanueva Rangel.    17/02/2015   Creación
     */
    
    public String getAdendaValidation(int id_specification) throws SQLException, Exception {

        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String strMsg = null;
        String strValidationAdenda = null;
        int ind = 0;
        try{
            String sqlStr = "BEGIN ORDERS.NP_ORDERS13_PKG.SP_VALIDATE_ADENDAS(?,?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setInt(++ind,id_specification);
            cstmt.registerOutParameter(++ind,OracleTypes.VARCHAR);
            cstmt.registerOutParameter(++ind,OracleTypes.VARCHAR);
            cstmt.executeUpdate();
            strValidationAdenda = cstmt.getString(2);
            System.out.println("strValidationAdenda: "+strValidationAdenda);
            strMsg = cstmt.getString(3);
            if (strMsg != null){
                return strMsg;
            }
        }catch (Exception e) {
            throw new Exception(e);
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return strValidationAdenda;
    }
    
    
     /*
     Method : getTemplateOrder
     Purpose: Obtener la lista de plantillas de un item de una orden.
     Developer                       Fecha       Comentario
     =============                   ==========  ======================================================================
     José María Espinoza Bueno.      06/11/2007  Creación
     */

    public HashMap getTemplateOrder(int id_order, int id_item)  throws SQLException, Exception {
      
      ArrayList list = new ArrayList();
      int ind = 0;  
      Connection conn = null; 
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;
      String message = "";
      String strMensaje = null;
      HashMap objHashMap = new HashMap();      
      
     System.out.println("[OrderDAO.getTemplateOrder] - id_order - id_item "+"   "+id_order+" - "+id_item);
     try{
        String sqlStr = "BEGIN ORDERS.NP_ORDERS13_PKG.SP_GET_TEMPLATE_ORDER_LST(?,?,?,?); END;";    
        conn = Proveedor.getConnection();
            
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        
        cstmt.setInt(++ind,id_order);
        cstmt.setInt(++ind,id_item);
        cstmt.registerOutParameter(++ind, OracleTypes.CURSOR );
        cstmt.registerOutParameter(++ind,OracleTypes.VARCHAR);
        cstmt.executeUpdate();
        String strMsg = cstmt.getString(4);    
        if (strMsg == null){
           rs = (ResultSet)cstmt.getObject(3);                       
           while (rs.next()) {   
              TemplateAdendumBean templateAdendumBean = new TemplateAdendumBean();                       
              templateAdendumBean.setNptemplateid(rs.getInt(1));        // id de plantilla
              templateAdendumBean.setNpaddendumterm(rs.getInt(2));      // Plazo de la adenda         
              list.add(templateAdendumBean);         
           }      
        }
              
        objHashMap.put("strMessage",strMsg);
        objHashMap.put("objArrayList",list);               
     }catch (Exception e) {
         objHashMap.put("strMessage",e.getMessage());
      }finally{
        try{
           closeObjectsDatabase(conn,cstmt,rs); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
      }    
      
      
      return objHashMap;
    }
    
    /*
     Method : deleteTemplateOrder
     Purpose: Eliminar las plantillas de adendas de un item de una orden
     Developer                       Fecha       Comentario
     =============                   ==========  ======================================================================
     José María Espinoza Bueno.      06/11/2007  Creación
     */

    public String deleteTemplateOrder(int num_order, 
                                      int item_id, 
                                      Connection conn) throws SQLException, Exception {
      
      ArrayList list = new ArrayList();
      int template_id;   
      int term = 0,ind = 0;
      OracleCallableStatement cstmt = null;
      String strMsg = null;
 
      System.out.println("[OrderDAO.deleteTemplateOrder] - num_order - item_id "+"   "+num_order+" - "+item_id);
      try{
        String sqlStr = "BEGIN ORDERS.NP_ORDERS13_PKG.SP_TEMPLATE_ORDER_DELETE(?,?,?); END;";    
         
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        
       logger.info("[ORDERS.NP_ORDERS13_PKG.SP_TEMPLATE_ORDER_DELETE]");
       
        cstmt.setInt(++ind,item_id);
        cstmt.setInt(++ind,num_order);          
        cstmt.registerOutParameter(++ind,OracleTypes.VARCHAR);
        int intResultTransaction = 0;
        intResultTransaction  =   cstmt.executeUpdate();
        strMsg = cstmt.getString(3);     
        
        logger.info("[Output][strMessage]: " + strMsg);        
                        
        if( strMsg!=null){
          return strMsg;
        }else{
          if( intResultTransaction > 0 )
            return null;
          else
            return "[No hubieron actualizaciones]";
        }
      }catch (Exception e) {
         return e.getMessage() ;
      }finally{
        try{
           closeObjectsDatabase(null,cstmt,null); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
      }    
            
    }        

     /*
     Method : getTemplateOrder
     Purpose: Obtiene el número de adendas activas.
     Developer                       Fecha       Comentario
     =============                   ==========  ======================================================================
     José María Espinoza Bueno.      06/11/2007  Creación
     */

    public HashMap getNumAddendumAct(int id_customer, String id_num_nextel)  throws SQLException, Exception {
      
      ArrayList list = new ArrayList();
      int ind = 0;  
      Connection conn = null; 
      OracleCallableStatement cstmt = null;
      String message = "";
      String strMensaje = null;
      HashMap objHashMap = new HashMap();      
      
      System.out.println("[OrderDAO.getNumAddendumAct] - id_customer - id_num_nextel "+"   "+id_customer+" - "+id_num_nextel);
      String sqlStr = "BEGIN ORDERS.NP_ORDERS13_PKG.SP_GET_NUM_ADDENDUM_ACT(?,?,?,?,?); END;";    
      try{
         conn = Proveedor.getConnection();          
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         
         cstmt.setInt(++ind,id_customer);
         cstmt.setString(++ind,id_num_nextel);
         cstmt.setString(++ind,"0");    // Adendas activas, reemplazadas y transferidas.
         cstmt.registerOutParameter(++ind, OracleTypes.NUMBER );
         cstmt.registerOutParameter(++ind,OracleTypes.VARCHAR);
         cstmt.executeUpdate();
         String strMsg = cstmt.getString(5);
         if (strMsg == null){
            int numAddenAct = cstmt.getInt(4);                 
            objHashMap.put("strMessage",strMsg);
            objHashMap.put("numAddenAct",String.valueOf(numAddenAct));
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
        try{
           closeObjectsDatabase(conn,cstmt,null); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
      }
      return objHashMap;
    }

    //EIORTIZ
    public HashMap getNumAddendumActSpec(int id_customer, String id_num_nextel, String id_specification)  throws SQLException, Exception {
        System.out.println("[OrderDAO.java.getNumAddendumActSpec]: Ejecutando este metodo");
        ArrayList list = new ArrayList();
        int ind = 0;
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String message = "";
        String strMensaje = null;
        HashMap objHashMap = new HashMap();

        System.out.println("[OrderDAO.java.getNumAddendumActSpec] - id_customer - id_num_nextel - id_specification"+"   "+id_customer+" - "+id_num_nextel+" - "+id_specification);
        String sqlStr = "BEGIN ORDERS.NP_ORDERS13_PKG.SP_GET_NUM_ADDENDUM_ACT_SPEC(?,?,?,?,?,?); END;";
        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.setInt(++ind,id_customer);
            cstmt.setString(++ind,id_num_nextel);
            cstmt.setString(++ind,"0");    // Adendas activas, reemplazadas y transferidas.
            cstmt.setString(++ind,id_specification);    // Adendas activas, reemplazadas y transferidas.
            cstmt.registerOutParameter(++ind, OracleTypes.NUMBER );
            cstmt.registerOutParameter(++ind,OracleTypes.VARCHAR);
            cstmt.executeUpdate();
            String strMsg = cstmt.getString(6);
            System.out.println("[OrderDAO.java.getNumAddendumActSpec]: strMsg: " + strMsg);
            if (strMsg == null){
                System.out.println("[OrderDAO.java.getNumAddendumActSpec]: Ingreso al if: ");
                int numAddenAct = cstmt.getInt(5);
                System.out.println("[OrderDAO.java.getNumAddendumActSpec]: numAddenAct: " + numAddenAct);
                objHashMap.put("strMessage",strMsg);
                objHashMap.put("numAddenAct",String.valueOf(numAddenAct));
            }
        }
        catch(Exception e){
            throw new Exception(e);
        }
        finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return objHashMap;
    }

     /*
     Method : getProductPriceList
     Purpose: Obtiene el precio de lista de un producto.
     Developer                       Fecha       Comentario
     =============                   ==========  ======================================================================
     Estefanía Gamonal.              27/11/2007  Creación
     */
    public HashMap getProductPriceList(String an_npproductid, String av_npmodality, String an_npoccurrence, String an_npcategoryid, int iSalesStructOriginalId)  throws SQLException, Exception {
      
      ArrayList list = new ArrayList();
      int ind = 0;  
      Connection conn = null; 
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;
      String message = "";
      String strMensaje = null;
      HashMap objHashMap = new HashMap();
      try{
        String sqlStr = "BEGIN PRODUCT.SPI_GET_PRODUCT_ITEM_DET2(?,?,?,?,?); END;";    
        conn = Proveedor.getConnection();          
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);      
        cstmt.setString(++ind,an_npproductid);
        cstmt.setString(++ind,av_npmodality);
        cstmt.setInt(++ind,iSalesStructOriginalId);
        cstmt.registerOutParameter(++ind, OracleTypes.DOUBLE);
        cstmt.registerOutParameter(++ind, OracleTypes.VARCHAR);
        cstmt.executeUpdate();
        String strMsg = cstmt.getString(5); // Mensaje de error
        
        if (strMsg == null){
           double  dblProductPriceList = 0; 
           dblProductPriceList = cstmt.getDouble(4);

           objHashMap.put("strMessage",strMsg);
           objHashMap.put("dblProductPriceList",String.valueOf(dblProductPriceList));           
        }
              
      }catch(Exception e){
         objHashMap.put("strMessage",e.getMessage());
      }
      finally{
        try{
           closeObjectsDatabase(conn,cstmt,rs); 
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
     *  REALIZADO POR: José María Espinoza Bueno
     *  FECHA: 24/10/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/    
     
    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  EXCEPCIONES - INICIO
     *  REALIZADO POR: Jorge Pérez
     *  FECHA: 21/12/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/   
     
     /**
     * Motivo: Actualiza los datos de exceopción de una orden
     * <br>Realizado por: <a href="mailto:jorge.perez@nextel.com.pe">Jorge Pérez</a>
     * <br>Fecha: 05/09/2007
     * @param     objOrder          Objeto que contiene los datos de la Orden                      
     * @return    String 
     */        
     public String updateException(OrderBean objOrder,Connection conn) throws SQLException,Exception{
        OracleCallableStatement cstmt = null;        
        String strMessage=null;
        try{
          String strSql = " BEGIN ORDERS.NP_ORDERS03_PKG.SP_UPDATE_ORDER_EXCEPTIONS(?,?); END;";        
          cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
          cstmt.setLong(1, objOrder.getNpOrderId());        
          cstmt.registerOutParameter(2, Types.CHAR);
          cstmt.executeUpdate();
          strMessage = cstmt.getString(2);                             
        }catch(Exception e){
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
           
     /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  EXCEPCIONES - INICIO
     *  REALIZADO POR: Jorge Pérez
     *  FECHA: 21/12/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/  
     
     
     
     /**
   * Motivo: Obtiene el Inbox en el cual se encuentra un pedido
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 14/09/2007
   * @param     strCurrentStatus        
   * @return    int 
   */
   
   public long getUnkownSiteIdByOportunity(long lngOportunityId) throws Exception,SQLException {
      
      long iReturnValue = 0;
      Connection conn=null;
      OracleCallableStatement cstmt = null;
      try{
        String sqlStr =  " { ? = call WEBSALES.FXI_GET_UNKNWN_SITE_ID( ? ) } ";        
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        cstmt.registerOutParameter(1, OracleTypes.NUMBER);   
        cstmt.setLong(2, lngOportunityId);                           
        cstmt.execute();
        iReturnValue = cstmt.getLong(1);                   
      }catch(Exception e){
         throw new Exception(e);
        }
        finally{
          try{
             closeObjectsDatabase(conn,cstmt,null); 
          }catch (Exception e) {
            logger.error(formatException(e));
          }
        }
      
      return iReturnValue;
   }  
   
   
   public HashMap getCustSiteIdByOportunity(long lngOportunityId) throws Exception,SQLException {
      
      Connection conn=null;
      OracleCallableStatement cstmt = null;
      String strMessage =  null;
      HashMap objHashMap = new HashMap();
      try{
        String strSql = " BEGIN WEBSALES.SPI_GET_OPP_CUST_SITE(?,?,?,?); END;";
        String newSiteId  = null;
          conn = Proveedor.getConnection();
          cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
          cstmt.setLong(1, lngOportunityId);        
          cstmt.registerOutParameter(2, OracleTypes.NUMBER);
          cstmt.registerOutParameter(3, OracleTypes.NUMBER);
          cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
          cstmt.executeUpdate();
          strMessage = cstmt.getString(4);        
          if( strMessage == null ){
              newSiteId =   cstmt.getString(3);
          }        
          /*if (cstmt != null)
            cstmt.close();*/        
          objHashMap.put("strMessage",strMessage);
          objHashMap.put("newSiteId",newSiteId);  
      } catch(Exception e){
        objHashMap.put("strMessage",e.getMessage());
        }
        finally{
          try{
             closeObjectsDatabase(conn,cstmt,null); 
          }catch (Exception e) {
            logger.error(formatException(e));
          }
        }                 
        return objHashMap;
   }  
   

   public String updOpportunityUnits(long lOrderId,Connection conn) throws Exception,SQLException {
      
      //Connection conn=null;
      OracleCallableStatement cstmt = null;
      String strMessage =  null;
      
      logger.debug("[Input][lOrderId]: " + lOrderId + "");
      
      try{
        String strSql = " BEGIN ORDERS.NP_ORDERS05_PKG.SP_UPD_OPPORTUNITY_UNITS(?,?); END;";
        String newSiteId  = null;
        logger.info("[ORDERS.NP_ORDERS05_PKG.SP_UPD_OPPORTUNITY_UNITS]");
              
        cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
        cstmt.setLong(1, lOrderId);                    
        cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
        cstmt.executeUpdate();
        strMessage = cstmt.getString(2);                                   
        //System.out.println(strMessage);
        logger.info("[Output][strMessage]: " + strMessage);
      }catch(Exception e){
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
   * Motivo: encuentra la cantidad de Ordenes CRM en Cashdesk
   * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
   * <br>Fecha: 15/03/2008
   
   */        
    public HashMap getCountPaymentOrder(String av_constOrder, long  lOrderId, Connection conn) throws SQLException, Exception{        
        System.out.println("getCountPaymentOrder:");
        System.out.println("av_constOrder:"+av_constOrder);
        System.out.println("lOrderId:"+lOrderId);        
        OracleCallableStatement cstmt = null; 
        String strMessage=null;
        int strCount=0;
        HashMap objHashMap = new HashMap();
        try{
          String strSql = " BEGIN ORDERS.SPI_GET_COUNT_ORDER_CASHDESK(?,?,?,?); END;";        
          cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
          cstmt.setString(1, av_constOrder);
          cstmt.setLong(2, lOrderId);
          cstmt.registerOutParameter(3, OracleTypes.NUMBER); 
          cstmt.registerOutParameter(4, Types.VARCHAR);     
          cstmt.executeQuery();
          strMessage = cstmt.getString(4);        
          if( strMessage == null ){
            strCount =  cstmt.getInt(3);
          }                                                       
          objHashMap.put("strMessage",strMessage);
          objHashMap.put("strCount",strCount+"");          
        }catch(Exception e){
            objHashMap.put("strMessage",e.getMessage());
          }
          finally{
            try{
               closeObjectsDatabase(null,cstmt,null); 
            }catch (Exception e) {
               logger.error(formatException(e));
            }
          }  
        
        return objHashMap;                
     }
     
        
  /**
   * Motivo: se encarga de cancelar todas las OP asociadas a la ORDEN CRM
   * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
   * <br>Fecha: 15/03/2008
   
   */        
    public String updPaymentOrderStatus(long  lOrderId, String an_user, int an_countpayment, double lPaymentTotal,Connection conn) throws SQLException, Exception{
        
        OracleCallableStatement cstmt = null; 
        String strMessage=null;
        try{
          String strSql = " BEGIN ORDERS.SPI_UPD_PAYMENT_ORDER_CANCEL(?,?,?,?,?); END;";        
          cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
          cstmt.setLong(1, lOrderId);
          cstmt.setString(2, an_user);    
          cstmt.setInt(3, an_countpayment); 
          cstmt.setDouble(4, lPaymentTotal); 
          cstmt.registerOutParameter(5, Types.VARCHAR);     
          cstmt.executeUpdate();
          strMessage = cstmt.getString(5);  
        }catch(Exception e){
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
   * Purpose: Obtiene un flag que indica si es que debe validarse el stock para la especificación
   * Developer       Fecha       Comentario
   * =============   ==========  ======================================================================
   * JPEREZ          03/03/2008  Creación 
   * @throws java.sql.SQLException
   * @throws java.lang.Exception
   * @return 
   * @param idSpecification
   */
    public HashMap getValidateFechaFirma(long lngOrderId, String strFechaFirma) throws Exception, SQLException{
      HashMap hashMap = new HashMap();        
      Connection conn = null; 
      OracleCallableStatement cstmt = null;
      String strMensaje = null;
      String strFlag    = null;
      try{
        String sqlStr = "BEGIN ORDERS.NP_ORDERS01_PKG.SP_VERIF_FIRMA_CLIENTE(?, ?, ?); END;";      
        conn = Proveedor.getConnection();          
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);    
        cstmt.setLong(1,lngOrderId);
        cstmt.setString(2,strFechaFirma);      
        cstmt.registerOutParameter(3, Types.CHAR);
        cstmt.executeUpdate();    
        strMensaje      = cstmt.getString(3);
        hashMap.put("strMessage",strMensaje);             
      }catch(Exception e){
          hashMap.put("strMessage",e.getMessage()); 
      }
      finally{
         try{
          closeObjectsDatabase(conn,cstmt,null); 
       }catch (Exception e) {
          logger.error(formatException(e));
        }
      }     

      return hashMap;      
    }

   /*Inicio CEM*/
   /**
   * Motivo:Determinar si la orden ya estuvo en el inbox de Inventario no permitirá
   		    avanzar la orden del inbox de Instalación
   * <br>Realizado por: <a href="mailto:cristian.espinoza@nextel.com.pe">Cristian Espinoza</a>
   * <br>Fecha: 24/04/2008
   */	 
   public HashMap OrderPassForInventory(long lOrderId) throws SQLException, Exception {

      OracleCallableStatement cstmt = null;
      HashMap hshRetorno = new HashMap();
      ResultSet rs = null;
      Connection conn=null;         
      String strMessage = null;
      try{
        String sqlStr = "BEGIN NP_ORDERS18_PKG.SP_GET_ORDER_INBOX_STATUS(?,?); END;";
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);     
        cstmt.setLong(1, lOrderId);               		
        cstmt.registerOutParameter(2, OracleTypes.VARCHAR);   
        cstmt.executeUpdate();
        strMessage = cstmt.getString(2);
        hshRetorno.put("strMessage",strMessage);  
        System.out.println("[OrderPassForInventory]");
        System.out.println("lOrderId: "+lOrderId);
        System.out.println("strMessage: "+strMessage);        
      }catch(Exception e){
          hshRetorno.put("strMessage",e.getMessage()); 
      }
      finally{
         try{
          closeObjectsDatabase(conn,cstmt,null); 
       }catch (Exception e) {
          logger.error(formatException(e));
        }
      }              
      return hshRetorno;
   }
   /*Fin CEM*/

   /**
   * Motivo: Permite validar que los administradores SLN y AGPS tengan el servicio activo.
   * <br>Realizado por: <a href="mailto:hugo.moreno@nextel.com.pe">Hugo Moreno</a>
   * <br>Fecha: 09/05/2008
   * @param     lCustomerId       Es el Id del cliente      
   * @return    HashMap 
   */ 
   public String getValidateAdministrator(long lOrderId)
   throws SQLException, Exception{
   
      OracleCallableStatement cstmt = null;
      Connection conn=null;
      String strResult="";
      try{
        String sqlStr = "BEGIN ORDERS.NP_ORDERS19_PKG.SP_ADMINISTRATOR_VALID(?, ? ); END;";
      
        conn = Proveedor.getConnection();
        
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        cstmt.setLong(1, lOrderId);  
        cstmt.registerOutParameter(2, Types.CHAR);   
        
        cstmt.execute();
        strResult = cstmt.getString(2);       
      }catch(Exception e){
          strResult = e.getMessage();
      }
      finally{
         try{
          closeObjectsDatabase(conn,cstmt,null); 
       }catch (Exception e) {
          logger.error(formatException(e));
        }
      }   
      
      
      return strResult;
   }

   /**
    * Motivo: Resuelve el detalle de un flujo.
    * <br>Realizado por: <a href="mailto:oliver.dubock@nextel.com.pe">Odubock</a>
    * <br>Fecha: 18/06/2008
    * @param     long    lngCustomerId
    * @param     long    lngSiteId
    * @return    HashMap objHashMap 
    */
    public HashMap getOrderDetailFlow(long lOrderId,String strLogin) throws Exception,SQLException{
       HashMap  objHashMap = new HashMap();
       ResultSet rs = null;
       Connection conn = null; 
       OracleCallableStatement cstm = null;
       String strMessage = null;
       try{
        String sqlStr = "BEGIN ORDERS.NP_ORDERS01_PKG.SP_GET_ORDER_DETAILWORKFLOW(?,?,?,?); END;";
          
         conn = Proveedor.getConnection();          
         cstm = (OracleCallableStatement) conn.prepareCall(sqlStr);
  
         cstm.setLong(1,lOrderId);
         cstm.setString(2,strLogin);
         cstm.registerOutParameter(3, OracleTypes.CURSOR);
         cstm.registerOutParameter(4, OracleTypes.VARCHAR);         
         cstm.executeUpdate();          
         strMessage = cstm.getString(4);
         if(strMessage == null){          
            rs = (ResultSet)cstm.getObject(3);            
            if (rs.next()) {
               objHashMap.put("NPORDERID",rs.getString(1)==null?"":rs.getString(1));              
               objHashMap.put("NPBPELFLOWGROUP",rs.getString(2)==null?"":rs.getString(2));          
               objHashMap.put("NPCREATEDBY",rs.getString(3)==null?"":rs.getString(3));   
               objHashMap.put("NPCUSTOMERID",rs.getString(4)==null?"":rs.getString(4));   
               objHashMap.put("NPSTATUS",rs.getString(5)==null?"":rs.getString(5));                   
            }
         }                 
         objHashMap.put("strMessage",strMessage);  
       }catch(Exception e){
          objHashMap.put("strMessage",e.getMessage());  
        }
        finally{
           try{
            closeObjectsDatabase(conn,cstm,rs); 
         }catch (Exception e) {
            logger.error(formatException(e));
          }
        }   
       
       closeObjectsDatabase(conn, cstm, rs);
       return objHashMap;
    }

  /***
   * Motivo: Determina si se puede crear una orden para un prospect dada una specificationId dada.
   * Fecha: 01/08/2008
   * Realizado por: Juan Oyola
   ***/
  public HashMap getAllowedSpecification(long lspecificationId, long lcustomerid)
  throws SQLException, Exception{

       HashMap  objHashMap = new HashMap();
       Connection conn = null; 
       OracleCallableStatement cstm = null;
       String strMessage = null;
       try{
        String sqlStr = "BEGIN WEBSALES.SPI_GET_ALLOWED_SPECIFICATION(?,?,?); END;";
  
         conn = Proveedor.getConnection();
         cstm = (OracleCallableStatement) conn.prepareCall(sqlStr);
  
         cstm.setLong(1,lspecificationId);
         cstm.setLong(2,lcustomerid);
         cstm.registerOutParameter(3, OracleTypes.VARCHAR);
         cstm.executeUpdate();
         strMessage = cstm.getString(3);
         objHashMap.put("strMessage",strMessage);  
       }catch(Exception e){
          objHashMap.put("strMessage",e.getMessage());  
        }
        finally{
           try{
            closeObjectsDatabase(conn,cstm,null); 
         }catch (Exception e) {
            logger.error(formatException(e));
          }
        }   
       return objHashMap;
  }



   /**
   * Motivo: Obtiene el numero de action (# de inbox por los que paso)
   * <br>Realizado por: <a href="mailto:cristian.espinoza@nextel.com.pe">Cristian Espinoza</a>
   * <br>Fecha: 15/07/2008
   * @param     long  
   * @return    HashMap
   */ 
	public HashMap getIsFirstInbox(long lOrderId)     
	throws SQLException, Exception{
   		
      Connection conn = null; 
      OracleCallableStatement cstm = null;
		String strMessage=null;
		int iFirstInbox=0;
		HashMap hshRetorno = new HashMap();   
    try{
        String sqlStr = "BEGIN ORDERS.NP_ORDERS07_PKG.SP_IS_FIRST_INBOX(?, ?, ?); END;";

        conn = Proveedor.getConnection();
        cstm = (OracleCallableStatement) conn.prepareCall(sqlStr);
        cstm.setLong(1, lOrderId);                     
        cstm.registerOutParameter(2, OracleTypes.NUMBER);           
        cstm.registerOutParameter(3, OracleTypes.VARCHAR);
        cstm.execute();
        strMessage = cstm.getString(3);
        
        if (strMessage==null)
          iFirstInbox = cstm.getInt(2);
          else
          iFirstInbox = -1;
                       
        if (logger.isDebugEnabled()) {			
          logger.debug("strMessage: " + strMessage);
          logger.debug("sFirstInbox: " + iFirstInbox);
        }	
        hshRetorno.put("sFirstInbox",iFirstInbox+""); 
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
		
		System.out.println("-----Fin getNumAction-----");
		return hshRetorno;
  }
  

  public HashMap getNoteCount(long lOrderId) throws SQLException, Exception {
  
      OracleCallableStatement cstmt = null;
      HashMap hshRetorno = new HashMap();
      ResultSet rs = null;
      Connection conn=null;
      String strMessage = null;
      int iTotal;
       
      try{
        String sqlStr = "BEGIN NP_ORDERS08_PKG.SP_GET_NOTE_COUNT(?,?,?); END;";
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);     
        cstmt.setLong(1, lOrderId);
        cstmt.registerOutParameter(2, OracleTypes.NUMBER);
        cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
        cstmt.executeUpdate();
        iTotal = cstmt.getInt(2);
        strMessage = cstmt.getString(3);
        hshRetorno.put("iTotal",new Integer(iTotal));
      }catch(Exception e){
          hshRetorno.put("strMessage",e.getMessage());
      }
      finally{
         try{
          closeObjectsDatabase(conn,cstmt,null); 
          }catch (Exception e) {
            logger.error(formatException(e));
          }
      }
      return hshRetorno;
  } 
  
   /**
   * Motivo:Determinar si la orden ha sido tomada por capa
   * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
   * <br>Fecha: 18/08/2008
   */	 
   public HashMap getSHedulestatus(long lOrderId) throws SQLException, Exception {

      OracleCallableStatement cstmt = null;
      HashMap hshRetorno = new HashMap();
      ResultSet rs = null;
      Connection conn=null;         
      String strMessage = null;
      try{
        String sqlStr = "BEGIN NP_ORDERS16_PKG.SP_GET_SCHEDULESTATUS(?,?); END;";
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);     
        cstmt.setLong(1, lOrderId);               		
        cstmt.registerOutParameter(2, OracleTypes.VARCHAR);   
        cstmt.executeUpdate();
        strMessage = cstmt.getString(2);
        hshRetorno.put("strMessage",strMessage);  
        System.out.println("[getSHedulestatus]");
        System.out.println("lOrderId: "+lOrderId);
        System.out.println("strMessage: "+strMessage);        
      }catch(Exception e){
          hshRetorno.put("strMessage",e.getMessage());
      }
      finally{
         try{
          closeObjectsDatabase(conn,cstmt,null); 
       }catch (Exception e) {
          logger.error(formatException(e));
        }
      }              
      return hshRetorno;
   }
   
  
    
    /**
   * Purpose: Generar un Incidente
   * Developer       Fecha       Comentario
   * =============   ==========  ======================================================================
   * KSALVADOR       01/12/2008  Creación 
   * @throws java.sql.SQLException
   * @throws java.lang.Exception
   * @return 
   * @param idOrden
   */
     public String insIncidentWeb (long lngOrderId, long an_npuserid, String av_login, long an_npappid, 
                                  long an_npbuildingid,Connection conn) throws Exception, SQLException{
     
        OracleCallableStatement cstmt = null;
        String strMessage=null;
        try{
  
          String strSql = "BEGIN INCIDENT_WEB.SPI_CREATE_INCIDENT_FROM_CDI(?, ?, ? , ?,?,?); END;";   
          cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
          cstmt.setLong(1, lngOrderId);
          cstmt.setLong(2, an_npuserid);
          cstmt.setString(3, av_login);
          cstmt.setLong(4, an_npappid);
          cstmt.setLong(5, an_npbuildingid);
          cstmt.registerOutParameter(6, Types.CHAR);
          cstmt.executeUpdate();
          strMessage = cstmt.getString(6);                             
        }catch(Exception e){
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
   * Motivo: Devuelve el valor 1: cuando existe coincidencia y 0:cuando no existe.
   * <br>Realizado por: <a href="mailto:ruth.polo@nextel.com.pe">Ruth Polo</a>
   * <br>Fecha: 12/01/2009
   * @param     lngSpecificationId          
   * @return    int 
   */
   
  public HashMap getValidateSSAA(long anSpecificationId, String av_npphone, long an_npserviceid) throws SQLException, Exception{
    int strCount=0;
    HashMap objHashMap = new HashMap();    
    OracleCallableStatement cstmt = null;
    String strMessage=null;
    Connection conn = null;

    try{
      String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_VALIDATE_ORDEN_SSAA(?,?,?,?,?); END;";              
          conn = Proveedor.getConnection();
          cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
          cstmt.setLong(1, anSpecificationId);
          cstmt.setString(2, av_npphone);    
          cstmt.setLong(3, an_npserviceid); 
          cstmt.registerOutParameter(4, Types.VARCHAR);     
          cstmt.registerOutParameter(5, OracleTypes.NUMBER);               
          cstmt.executeQuery();
          strMessage = cstmt.getString(4);        
          if( strMessage == null ){
            strCount =  cstmt.getInt(5);
          }                                                       
          objHashMap.put("strMessage",strMessage);
          objHashMap.put("strCount",strCount+"");
      }catch(Exception e){
            objHashMap.put("strMessage",e.getMessage());
          }
          finally{
            try{
               closeObjectsDatabase(conn,cstmt,null); 
            }catch (Exception e) {
               logger.error(formatException(e));
            }
          }  
      return objHashMap; 
  }
  
   /**
     * Motivo: Devuelve el valor 0: cuando es un M2M y 1:cuando no es M2M.
     * <br>Realizado por: <a href="mailto:katherine.perez.dxc@viaexperis.com">Katherine Perez</a>
     * <br>Fecha: 24/04/2018
     * @param     an_npitemproductid
     * @return    String
     */

    public HashMap getValidateM2M(int an_npitemproductid) throws SQLException, Exception{

        HashMap objHashMap = new HashMap();
        OracleCallableStatement cstmt = null;
        String strMessage=null;
        Connection conn = null;
        Boolean blbflagM2M=false;
        logger.info("[Input][an_npitemproductid]: " + an_npitemproductid + "");


        try{
            String strSql = "BEGIN ORDERS.SPI_EM_CA_ISPRODM2M(?,?,?); END;";

            logger.info("[ORDERS.SPI_EM_CA_ISPRODM2M]");

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
            cstmt.setInt(1, an_npitemproductid);
            cstmt.registerOutParameter(2, OracleTypes.NUMBER);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            strMessage = cstmt.getString(3);
            blbflagM2M= Boolean.valueOf(cstmt.getBoolean(2));

            logger.info("[Output][bolM2M]: " + blbflagM2M + "");
            logger.info("[Output][strMessage]: " + strMessage);

            objHashMap.put("bolM2M",blbflagM2M);
            objHashMap.put("strMessage",strMessage);

        }catch(Exception e){
            objHashMap.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return objHashMap;
    }  
  
    /** Motivo: Valida que la orden contenga solo productos M2M o !M2M
     * <br> Realizado por: <a href="mailto:katherine.perez.dxc@viaexperis.com">Katherine Perez</a>
     * <br> Fecha: 26/04/2018
     * @param     String[] item_Product_Val
     * @return    HashMap objResultHashMap */

    public HashMap doValidateSimManager(String[] items_Product_Val)
            throws Exception, SQLException
    {
        logger.info("[Inicio] Metodo doValidateSimManager - OrderDao");

        HashMap   hshData= null;
        HashMap objResultHashMap = new HashMap();
        int intItemProductId;
        String strMessage="";

        Boolean bolItem=false;
        Boolean bolPrimerItem=false;

        for(int i=0; i<items_Product_Val.length; i++) {
            //validar si todos los productos son M2M
            intItemProductId    = Integer.parseInt(MiUtil.getStringObject(items_Product_Val,i));

            logger.info("[Metodo doValidateSimManager]  pv_item_Product_Val=" + items_Product_Val );
            logger.info("[Metodo doValidateSimManager]  valor=" + intItemProductId);

            hshData = getValidateM2M(intItemProductId);

            bolItem=(Boolean) hshData.get("bolM2M");
            strMessage=(String)hshData.get("strMessage");

            if(strMessage==null || strMessage.equalsIgnoreCase("")){

                if(i==0){
                    bolPrimerItem=bolItem;
                    strMessage=null;

                    continue;
                }
                if(bolPrimerItem==bolItem){
                    strMessage=null;

                    continue;
                }else{
                    strMessage="La orden SIM Manager solo debe contener productos M2M";

                }
            }else{
                break;
            }
        }

        objResultHashMap.put("strMessage", strMessage);
        logger.info("[ Metodo doValidateSimManager ]  strMessage=  "+strMessage);
        logger.info("[ FIN ]  Metodo  doValidateSimManager");
        return  objResultHashMap;

    }

/**
   * Motivo: Devuelve el valor 1: cuando existe coincidencia y 0:cuando no existe.
   * <br>Realizado por: <a href="mailto:ruth.polo@nextel.com.pe">Ruth Polo</a>
   * <br>Fecha: 12/01/2009
   * @param     lngSpecificationId          
   * @return    int 
   */
   
  public HashMap getVerificateComission(long an_npserviceid) throws SQLException, Exception{
    
    HashMap objHashMap = new HashMap();    
    OracleCallableStatement cstmt = null;
    String strMessage=null;
    String npCommission=null;
    Connection conn = null;

    try{
      String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_VERIFICATE_COMMISSION(?,?,?); END;";              
          conn = Proveedor.getConnection();
          cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
          cstmt.setLong(1, an_npserviceid);
          cstmt.registerOutParameter(2,Types.VARCHAR);    
          cstmt.registerOutParameter(3, Types.VARCHAR); 
          cstmt.executeQuery();
          strMessage = cstmt.getString(2);        
          if( strMessage == null ){
            npCommission =  cstmt.getString(3);
          }                                                       
          objHashMap.put("strMessage",strMessage);
          objHashMap.put("npCommission",npCommission+"");
          
          
      }catch(Exception e){
            objHashMap.put("strMessage",e.getMessage());
          }
          finally{
            try{
               closeObjectsDatabase(conn,cstmt,null); 
            }catch (Exception e) {
               logger.error(formatException(e));
            }
          }  
      return objHashMap; 
  }
  
/**
   * Motivo: Anula las ordenes de pago relacionadas a una odrden.
   * <br>Realizado por: <a href="mailto:patricia.castillo@nextel.com.pe">Patricia Castillo</a>
   * <br>Fecha: 06/02/2009
   * @param     lngSpecificationId          
   * @return    int 
   */
   public String updPaymentOrderAnul(String av_login, long lOrderId,Connection conn) throws SQLException, Exception{        
        System.out.println("updPaymentOrderAnul:");
        System.out.println("av_login:"+av_login);
        System.out.println("lOrderId:"+lOrderId);        
        OracleCallableStatement cstmt = null; 
        String strMessage=null;

        try{
          String strSql = " BEGIN ORDERS.NP_ORDERS03_PKG.SP_UPD_PAYMENT_ORDER_ANUL(?,?,?); END;";        
          cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
          cstmt.setLong(1, lOrderId);
          cstmt.setString(2, av_login);
          cstmt.registerOutParameter(3, Types.VARCHAR);     
          cstmt.executeQuery();
          strMessage = cstmt.getString(3);                                                                       
          
        }catch(Exception e){
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
   * Motivo: Encuentra la cantidad de ordenes de pago canceladas de una orden
   * <br>Realizado por: <a href="mailto:patricia.castillo@nextel.com.pe">Patricia Castillo</a>
   * <br>Fecha: 06/02/2009
   
   */        
    public HashMap doSetOrderPayCancel(String av_constOrder, long  lOrderId) throws SQLException, Exception{        
          
        OracleCallableStatement cstmt = null; 
        String strMessage=null;
        Connection conn=null;
        int strCount=0;
        HashMap objHashMap = new HashMap();
        try{
          String strSql = " BEGIN ORDERS.NP_ORDERS03_PKG.SP_GET_PAY_ORD_CANCELADO(?,?,?,?); END;";        
          conn = Proveedor.getConnection();   
          conn.setAutoCommit(false);
          cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
          cstmt.setString(1, av_constOrder);
          cstmt.setLong(2, lOrderId);
          cstmt.registerOutParameter(3, OracleTypes.NUMBER); 
          cstmt.registerOutParameter(4, Types.VARCHAR);     
          cstmt.executeQuery();
          strMessage = cstmt.getString(4);        
          if( strMessage == null ){
            strCount =  cstmt.getInt(3);
          }                                                       
          objHashMap.put("strMessage",strMessage);
          objHashMap.put("strCount",strCount+"");          
        }catch(Exception e){
            objHashMap.put("strMessage",e.getMessage());
          }
          finally{
            try{
               closeObjectsDatabase(conn,cstmt,null); 
            }catch (Exception e) {
               logger.error(formatException(e));
            }
          }  
        return objHashMap;                
     }
 
    public HashMap doSetOrderPayPend(String av_constOrder, long  lOrderId) throws SQLException, Exception{             
        OracleCallableStatement cstmt = null; 
        String strMessage=null;
        int strCount=0;
        Connection conn=null;
        HashMap objHashMap = new HashMap();
        try{
          String strSql = " BEGIN ORDERS.SPI_GET_COUNT_ORDER_CASHDESK(?,?,?,?); END;";        
          conn = Proveedor.getConnection();   
          conn.setAutoCommit(false);
          cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
          cstmt.setString(1,av_constOrder);
          cstmt.setLong(2, lOrderId);
          cstmt.registerOutParameter(3, OracleTypes.NUMBER); 
          cstmt.registerOutParameter(4, Types.VARCHAR);     
          cstmt.executeQuery();
          strCount =  cstmt.getInt(3);
          strMessage = cstmt.getString(4);
          objHashMap.put("strCount",strCount+""); 
          objHashMap.put("strMessage",strMessage);
        }catch(Exception e){
            logger.error(formatException(e));
            objHashMap.put("strMessage",e.getMessage());
          }
          finally{
            try{
               closeObjectsDatabase(conn,cstmt,null); 
            }catch (Exception e) {
               logger.error(formatException(e));
            }
          }  
        
        return objHashMap;                
     }


/**
* Motivo: Obteniene el importe total y el pagado del Deposito en Garantia
* <br>Realizado por: <a href="mailto:jose.casas@nextel.com.pe">José Casas</a>
* <br>Fecha: 04/06/2009
* <br>Modificado por: <a href="mailto:victor.larosa@nextel.com.pe">Victor La Rosa</a>
* <br>Se cambio a double el monto de la garantia
* <br>Fecha: 01/10/2009
* @param     lNpOrderid     Es el Id de la Orden
* @return    HashMap 
*/ 
   public HashMap getGuarantee(long lSourceid, String strSource, long lConceptid ) throws SQLException, Exception{
   OracleCallableStatement cstmt = null;
   String strMessage=null;
   double lTotalAmount = 0;
   double lDueAmount = 0;
   Connection conn =null;   
   HashMap hshRetorno=new HashMap();
   String strSql = "BEGIN ORDERS.SPI_GET_PAYMENT_ORDER_AMOUNT( ? , ? , ?, ?, ?, ? ); end;";
   try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
      cstmt.setLong(1, lSourceid);
      cstmt.setString(2, strSource);
      cstmt.setLong(3, lConceptid);
      cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
      cstmt.registerOutParameter(5, OracleTypes.NUMBER);
      cstmt.registerOutParameter(6, OracleTypes.NUMBER);
      cstmt.execute();
      strMessage = cstmt.getString(4);
      
      if( strMessage==null){      
        lTotalAmount = cstmt.getDouble(5);
        lDueAmount = cstmt.getDouble(6);
      }
        
   }
   catch (Exception e) {
      throw new Exception(e);
   }
   finally{
		closeObjectsDatabase(conn,cstmt,null);
   }
   hshRetorno.put("strMessage",strMessage);
   hshRetorno.put("lTotalAmount",lTotalAmount+"");
   hshRetorno.put("lDueAmount",lDueAmount+"");
   return hshRetorno;

}

  /**
   Method : validateCustomerScore
   Purpose: Verifica si el cliente debe y tiene una Evaluacion
   Developer        Fecha       Comentario
   =============    ==========  ======================================================================
   José Casas       10/06/2009  Creación
  **/
  public HashMap validateCustomerScore(long lOrderid, long lCustomerid, int intSpecificationId, String strLogin, Connection conn) throws Exception,SQLException
  {
    HashMap objHashMap = new HashMap();
    OracleCallableStatement cstmt = null;
    String strMsgError = null;
    
    logger.debug("[Input][lCustomerid]: " + lCustomerid + "");
    logger.debug("[Input][intSpecificationId]: " + intSpecificationId);
        
    try {
      String sqlStr =  "BEGIN ORDERS.SPI_VERIFY_CUSTOMER_SCORE(?, ?, ? , ?, ?, ?); END;";
      logger.info("[ORDERS.SPI_VERIFY_CUSTOMER_SCORE]");
      
      cstmt=(OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1,lOrderid);
      cstmt.setLong(2,lCustomerid);
      cstmt.setInt(3,intSpecificationId);
      cstmt.setString(4,strLogin);
      cstmt.registerOutParameter(5, OracleTypes.NUMBER);
      cstmt.registerOutParameter(6, OracleTypes.VARCHAR);      
      cstmt.execute();
      strMsgError=cstmt.getString(6);
      
      logger.info("[Output][flagCustomerScore]: " + cstmt.getInt(5) + "");
      logger.info("[Output][strMessage]: " + strMsgError);
            
      objHashMap.put("flagCustomerScore",cstmt.getInt(5)+"");        
      objHashMap.put("strMessage",strMsgError);
      
    } catch(Exception e) {
        logger.error(formatException(e));
    } finally {
        try {
          closeObjectsDatabase(null,cstmt,null);
        } catch(Exception e) {
          logger.error(formatException(e));
        }
    }
    return objHashMap;
  }

  /**
   Method : getFistStatus
   Purpose: Verifica si el cliente debe y tiene una Evaluacion
   Developer        Fecha       Comentario
   =============    ==========  ======================================================================
   José Casas       16/06/2009  Creación
  **/
  public HashMap getFistStatus(String strOrderId, String strCustomerId, String strSpecificationId, String strAccion, String strOrigen, String strCreatedBy, String strGeneratorID, Connection conn) throws Exception,SQLException
  {
    HashMap objHashMap = new HashMap();
    OracleCallableStatement cstmt = null;
    String strMsgError = null;
    try {
      String sqlStr =  "BEGIN ORDERS.NP_ORDERS11_PKG.SP_GET_FIRST_STATUS(?, ?, ? , ?, ?, ?, ?, ?, ?); END;";
      cstmt=(OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setInt(1,MiUtil.parseInt(strOrderId));
      cstmt.setInt(2,MiUtil.parseInt(strCustomerId));
      cstmt.setInt(3,MiUtil.parseInt(strGeneratorID));
      cstmt.setInt(4,MiUtil.parseInt(strSpecificationId));
      cstmt.setString(5,strAccion);
      cstmt.setString(6,strOrigen);
      //cstmt.setString(5,"OPP");
      cstmt.setString(7,strCreatedBy);
      cstmt.registerOutParameter(8, OracleTypes.VARCHAR);
      cstmt.registerOutParameter(9, OracleTypes.VARCHAR);      
      cstmt.execute();
      strMsgError=cstmt.getString(9);
      System.out.println("Orden "+strOrderId);
      System.out.println("Cliente "+strCustomerId);
      System.out.println("Especificacion "+strSpecificationId);
      System.out.println("Accion "+strAccion);
      System.out.println("Origen "+strOrigen);
      objHashMap.put("strFirstStatus",cstmt.getString(8)+"");        
      objHashMap.put("strMessage",strMsgError);
    } catch(Exception e) {
      logger.error(formatException(e));
    } finally {
      try {
        closeObjectsDatabase(null,cstmt,null);
      } catch(Exception e) {
        logger.error(formatException(e));
      }
    }
    return objHashMap;
  }
    /**
   Method : getOthersSolutionsbySubMarket
   Purpose: Lista las solucion de un determinado submarket.
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Cesar Barzola     10/06/2009  Creación
   */
  public HashMap  getOthersSolutionsbySubMarket(long lngSpecificationId,long lngSolutionId)throws SQLException,Exception
  {
    HashMap objHashMap = new HashMap();
    OracleCallableStatement cstmt = null;
    Connection conn=null; 
    String strMessage = null;
    ResultSet rs = null;
    ArrayList list = new ArrayList();
    try{
       String sqlStr =  "BEGIN ORDERS.NP_ORDERS25_PKG.SP_GET_SOLUTIONS_BY_SUBMARKET(?, ?, ? , ?); END;";
       conn = Proveedor.getConnection();
       cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
       cstmt.setLong(1, lngSpecificationId);
       cstmt.setLong(2,lngSolutionId);
       cstmt.registerOutParameter(3, OracleTypes.CURSOR);
       cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
       cstmt.execute();
       
       strMessage = cstmt.getString(4);
       if( strMessage==null)
       {
         rs = (ResultSet)cstmt.getObject(3);
          while (rs.next()) {
           SolutionBean objSolutionBean = new SolutionBean();
           objSolutionBean.setNpsolutionid(rs.getLong(1));      
           objSolutionBean.setNpsolutionname(rs.getString(2));
           list.add(objSolutionBean);
          }
        objHashMap.put("strMessage",strMessage);
        objHashMap.put("objArrayList",list);  
       }
       
    }
     catch(Exception e)
     {
      logger.error(formatException(e));
     }
     finally
     {
        try
        {
          closeObjectsDatabase(conn,cstmt,rs);
        }
        catch(Exception e)
        {
          logger.error(formatException(e));
        }
     }
    return objHashMap;
  }
    /**
	Method : validaDiasSuspension
	Purpose: Valida los que un contrato no este suspendido mas de 60 días.
	Developer       		Fecha       Comentario
	=============   		==========  ======================================================================
	Rensso Martinez   	24/06/2009  Creación
	*/
	public HashMap validaDiasSuspensionEdit(int iNpOrderId,  String strNpScheduleDate, String strNpScheduleDate2) throws Exception,SQLException{
       
	  Connection conn = null; 
		ResultSet rs=null;
		OracleCallableStatement cstmt = null;
		String strMessage = null;
		
		HashMap objHashMapResultado = new HashMap();        
      System.out.println("[OrderDAO][validaDiasSuspensionEdit][Inicio]");      
      System.out.println("[OrderDAO][validaDiasSuspensionEdit][Inicio]:strPhoneNumber:"+iNpOrderId);
      System.out.println("[OrderDAO][validaDiasSuspensionEdit][Inicio]:strNpScheduleDate:"+strNpScheduleDate);
      System.out.println("[OrderDAO][validaDiasSuspensionEdit][Inicio]:strNpScheduleDate2:"+strNpScheduleDate2);
		if(logger.isDebugEnabled()){		
			logger.debug("Inicio - [OrderDAO][validaDiasSuspensionEdit]");
			logger.debug("strPhoneNumber: "+iNpOrderId);
			logger.debug("strNpScheduleDate: "+strNpScheduleDate+"");
			logger.debug("strNpScheduleDate2: "+strNpScheduleDate2+"");
		}	 	  
    
      
		String strSql = "{call ORDERS.NP_ORDERS23_PKG.SP_VALIDATE_DIAS_SUSP_EDIT(?,?,?,?)}";
                          
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
          
         cstmt.setInt(1,iNpOrderId);
         cstmt.setString(2,strNpScheduleDate);
         cstmt.setString(3,strNpScheduleDate2);          			        
         cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
          
         cstmt.executeUpdate();
         strMessage  = cstmt.getString(4);
           
      }
      catch(Exception e){
         System.out.println("[Exception][OrderDAO][validaDiasSuspensionEdit]"+e.getMessage());
         throw new Exception(e);
      }
		finally{
         System.out.println("[Finally][OrderDAO][validaDiasSuspensionEdit][Inicio]");
         closeObjectsDatabase(conn,cstmt,rs);
         System.out.println("[Finally][OrderDAO][validaDiasSuspensionEdit][Fin]");
      }           
      objHashMapResultado.put("strMessage",strMessage);
      System.out.println("[OrderDAO][validaDiasSuspensionEdit][Fin]");
      return objHashMapResultado;                                                                
   }
   
  

 /**
   Method : getFinalSuspensionList
   Purpose: Obtiene la lista de suspensiones definitivas programadas dentro de un rango de fechas
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   DLazo     05/06/2009  Creación
   **/
   public HashMap getFinalSuspensionList(HashMap hshParameters) throws Exception,SQLException {
      
      ArrayList list = new ArrayList();
      HashMap objHashMap = new HashMap();
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;
      Connection conn=null; 
      FinalSuspensionBean objFSBean = null;
      String strMessage = null;
      int i = 1;
      //NP_SUSPENSIONS_PKG.SP_GET_FINAL_SUSPENSION_LST
      try{
         String startdate = (String)hshParameters.get("ad_start_date");
         String enddate = (String)hshParameters.get("ad_end_date");

         String sqlStr =  "BEGIN ORDERS.NP_SUSPENSION_REPORTS.SP_GET_FINAL_SUSPENSION_LST(?, ?, ?, ?); END;";
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         
         cstmt.setString(1, startdate);
         cstmt.setString(2, enddate);
         cstmt.registerOutParameter(3, OracleTypes.CURSOR);
         cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
         cstmt.execute();
         
         strMessage = cstmt.getString(4);
         if (!StringUtils.isNotBlank(strMessage)) {
            rs = (ResultSet) cstmt.getObject(3);
            
            while (rs.next()) {
               objFSBean = new FinalSuspensionBean();

               objFSBean.setNpCustomerId(rs.getLong(i++));
               objFSBean.setNpBscsId(rs.getString(i++));
               objFSBean.setNpName(rs.getString(i++));
               objFSBean.setNpCycle(rs.getString(i++));               
               //objFSBean.setNpCreateSupDate((String)MiUtil.getDate(rs.getTimestamp(i++), "dd/MM/yyyy hh"));
               //objFSBean.setNpCreateSupDate((String)MiUtil.getDate(rs.getTimestamp(i++), "dd/mm/yyyy"));
               objFSBean.setNpCreateSupDate(rs.getString(i++));
               objFSBean.setNpCreatedBy(rs.getString(i++));
               
               //objFSBean.setNpSuspensionDate((String)MiUtil.getDate(rs.getTimestamp(i++), "dd/MM/yyyy"));
               //objFSBean.setNpScoringCust(rs.getString(i++));
               //objFSBean.setNpCustomerType(rs.getString(i++));
               //objFSBean.setNpCustomerState(rs.getString(i++));
               //objFSBean.setNpSuspensionPhone(rs.getString(i++));
               //objFSBean.setNpSuspensionState(rs.getString(i++));
               //objFSBean.setNpGiro(rs.getString(i++));
               //objFSBean.setNpRegion(rs.getString(i++));
               objFSBean.setNpScoringCust(rs.getString(i++));  
               objFSBean.setST_SUSPENSION(rs.getString(i++));
               objFSBean.setNpGiro(rs.getString(i++));
               objFSBean.setTOTAL_ITEMS(rs.getString(i++));
               objFSBean.setNpCustomerType(rs.getString(i++));
               objFSBean.setNPCLOSEDDATE(rs.getString(i++));
                              
               list.add(objFSBean);
               i=1;
            }
         }//(String)MiUtil.getDate(rs.getTimestamp(i++), "dd/MM/yyyy")

         objHashMap.put("strMessage",strMessage);
         objHashMap.put("objArrayList",list);  

      }catch(Exception e){
         objHashMap.put("strMessage",e.getMessage()); 
      }finally{
         try{
            closeObjectsDatabase(conn,cstmt,rs); 
         }catch (Exception e) {
            logger.error(formatException(e));
         }
      }
   
      return objHashMap;
   }
   
      /**
   Method : getFinalSuspensionDetailList
   Purpose: Obtiene la lista de suspensiones definitivas programadas dentro de un rango de fechas - detallado por telefonos
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   JHERRERA        15/06/2009  Creación
   MJURADO         16/10/2009  Modificacion se agrego el typo de suspension
   **/
public HashMap getFinalSuspDetailList(HashMap hshParameters) throws Exception,SQLException {
      
      ArrayList list = new ArrayList();
      HashMap objHashMap = new HashMap();
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;
      Connection conn=null; 
      FinalSuspensionBean objFSBean = null;
      String strMessage = null;
      int i = 1;      
      try{
         String startdate = (String)hshParameters.get("ad_start_date");
         String enddate = (String)hshParameters.get("ad_end_date");
         String strTypeSusp = (String)hshParameters.get("wv_TypeSuspen");

         String sqlStr =  "BEGIN ORDERS.NP_SUSPENSION_REPORTS.SP_GET_FS_DET_LST(?, ?, ?, ?, ?); END;";
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);         
         cstmt.setString(1, startdate);
         cstmt.setString(2, enddate);
         cstmt.setString(3, strTypeSusp);
         
         cstmt.registerOutParameter(4, OracleTypes.CURSOR);
         cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
         cstmt.execute();         
         strMessage = cstmt.getString(5);
         if (!StringUtils.isNotBlank(strMessage)) {
            rs = (ResultSet) cstmt.getObject(4);            
            while (rs.next()) {
               objFSBean = new FinalSuspensionBean();
               objFSBean.setNpCustomerId(rs.getLong(i++));
               objFSBean.setNpBscsId(rs.getString(i++));
               objFSBean.setNpName(rs.getString(i++));
               objFSBean.setNpCycle(rs.getString(i++));               
               //objFSBean.setNpCreateSupDate((String)MiUtil.getDate(rs.getTimestamp(i++), "dd/MM/yyyy hh"));
               //objFSBean.setNpCreateSupDate((String)MiUtil.getDate(rs.getTimestamp(i++), "dd/mm/yyyy"));
               objFSBean.setNpCreateSupDate(rs.getString(i++));
               objFSBean.setNpCreatedBy(rs.getString(i++));               
               //objFSBean.setNpSuspensionDate((String)MiUtil.getDate(rs.getTimestamp(i++), "dd/MM/yyyy"));
               //objFSBean.setNpScoringCust(rs.getString(i++));
               //objFSBean.setNpCustomerType(rs.getString(i++));
               //objFSBean.setNpCustomerState(rs.getString(i++));
               //objFSBean.setNpSuspensionPhone(rs.getString(i++));
               //objFSBean.setNpSuspensionState(rs.getString(i++));
               //objFSBean.setNpGiro(rs.getString(i++));
               //objFSBean.setNpRegion(rs.getString(i++));
               objFSBean.setNpScoringCust(rs.getString(i++));  
               objFSBean.setST_SUSPENSION(rs.getString(i++));
               objFSBean.setNpGiro(rs.getString(i++));
               objFSBean.setTOTAL_ITEMS(rs.getString(i++));
               objFSBean.setNpCustomerType(rs.getString(i++));
               objFSBean.setNPCLOSEDDATE(rs.getString(i++));
               objFSBean.setCO_ID(rs.getString(i++));
               /* Modificacion **/
               objFSBean.setNpCategoriaOrden(rs.getString(i++)); 
               objFSBean.setNpStatusBSCS(rs.getString(i++));
               objFSBean.setNpMotivoBSCS(rs.getString(i++));               
               list.add(objFSBean);
               i=1;
            }
         }//(String)MiUtil.getDate(rs.getTimestamp(i++), "dd/MM/yyyy")

         objHashMap.put("strMessage",strMessage);
         objHashMap.put("objArrayList",list);  

      }catch(Exception e){
         objHashMap.put("strMessage",e.getMessage()); 
      }finally{
         try{
            closeObjectsDatabase(conn,cstmt,rs); 
         }catch (Exception e) {
            logger.error(formatException(e));
         }
      }
   
      return objHashMap;
   }  

   
   /**
  * Motivo: Obtiene la Lista de los Areas de CAL para el reporte de suspensiones
  * <br>Realizado por: <a href="mailto:julio.herrera@nextel.com.pe">Julio Herrera</a>
  * <br>Fecha: 13/07/2009
  * @return	ArrayList de DominioBean      
  */               
     
	public HashMap getCalArea() throws SQLException, Exception {
		HashMap objHashMap = new HashMap();
		ArrayList arrAreas = new ArrayList();
		String strMessage;
		Connection conn = null;
		HashMap hshData=new HashMap();
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;
		      
      String sqlStr = "BEGIN ORDERS.NP_SUSPENSION_REPORTS.SP_GET_CAL_AREA(?, ?); END;";
      
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.registerOutParameter(1, OracleTypes.CURSOR);
         cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
         cstmt.executeQuery();
         rs = (ResultSet)cstmt.getObject(1);
         strMessage = cstmt.getString(2);
         while (rs.next()) {
            DominioBean dominio = new DominioBean();
            dominio.setValor((String)rs.getString(1));
            dominio.setDescripcion((String)rs.getString(2));
            arrAreas.add(dominio);
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
		objHashMap.put("arrAreas", arrAreas);
		objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		return objHashMap;
    }

/**
  * Motivo: Obtiene la Lista de las razones para la suspension de los equipos
  * <br>Realizado por: <a href="mailto:julio.herrera@nextel.com.pe">Julio Herrera</a>
  * <br>Fecha: 13/07/2009
  * @return	ArrayList de DominioBean      
  */               
     
	public HashMap getSuspensionReason() throws SQLException, Exception {
		HashMap objHashMap = new HashMap();
		ArrayList arrReasons = new ArrayList();
		String strMessage;
		Connection conn = null;
		HashMap hshData=new HashMap();
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;
		      
      String sqlStr = "BEGIN ORDERS.NP_SUSPENSION_REPORTS.SP_GET_SUSPENSION_REASON(?, ?); END;";
      
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.registerOutParameter(1, OracleTypes.CURSOR);
         cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
         cstmt.executeQuery();
         rs = (ResultSet)cstmt.getObject(1);
         strMessage = cstmt.getString(2);
         while (rs.next()) {
            DominioBean dominio = new DominioBean();
            dominio.setValor((String)rs.getString(1));
            dominio.setDescripcion((String)rs.getString(2));
            arrReasons.add(dominio);
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
		objHashMap.put("arrReasons", arrReasons);
		objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		return objHashMap;
    }
    
/**
  * Motivo: Obtiene la Lista de herramientas de retencion disponibles
  * <br>Realizado por: <a href="mailto:julio.herrera@nextel.com.pe">Julio Herrera</a>
  * <br>Fecha: 13/07/2009
  * @return	ArrayList de DominioBean      
  */               
     
	public HashMap getRetentionTool() throws SQLException, Exception {
		HashMap objHashMap = new HashMap();
		ArrayList arrTools = new ArrayList();
		String strMessage;
		Connection conn = null;
		HashMap hshData=new HashMap();
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;
		      
      String sqlStr = "BEGIN ORDERS.NP_SUSPENSION_REPORTS.SP_GET_RETENTION_TOOL(?, ?); END;";
      
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.registerOutParameter(1, OracleTypes.CURSOR);
         cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
         cstmt.executeQuery();
         rs = (ResultSet)cstmt.getObject(1);
         strMessage = cstmt.getString(2);
         while (rs.next()) {
            DominioBean dominio = new DominioBean();
            dominio.setValor((String)rs.getString(1));
            dominio.setDescripcion((String)rs.getString(2));
            arrTools.add(dominio);
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
		objHashMap.put("arrTools", arrTools);
		objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		return objHashMap;
    }
  
/**
  * Motivo: Obtiene la Lista de herramientas de retencion disponibles
  * <br>Realizado por: <a href="mailto:julio.herrera@nextel.com.pe">Julio Herrera</a>
  * <br>Fecha: 13/07/2009
  * @return	ArrayList de DominioBean      
  */               
     
	public HashMap getClientType() throws SQLException, Exception {
		HashMap objHashMap = new HashMap();
		ArrayList arrClientTypes = new ArrayList();
		String strMessage;
		Connection conn = null;
		HashMap hshData=new HashMap();
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;
		      
      String sqlStr = "BEGIN ORDERS.NP_SUSPENSION_REPORTS.SP_GET_CLIENT_TYPE(?, ?); END;";
      
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.registerOutParameter(1, OracleTypes.CURSOR);
         cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
         cstmt.executeQuery();
         rs = (ResultSet)cstmt.getObject(1);
         strMessage = cstmt.getString(2);
         while (rs.next()) {
            DominioBean dominio = new DominioBean();
            dominio.setValor((String)rs.getString(1));
            dominio.setDescripcion((String)rs.getString(2));
            arrClientTypes.add(dominio);
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
		objHashMap.put("arrClientTypes", arrClientTypes);
		objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		return objHashMap;
  }
  
  /*public HashMap getGeneralSuspensionListXLS(HashMap hshParameters) throws SQLException, Exception {

    if (logger.isDebugEnabled())
      logger.debug("--Inicio--");

    ArrayList arrListado = new ArrayList();
    HashMap hshResult = new HashMap();
    String strMessage = null;
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    int i=1;
      
    String strFechaDesde = (String)hshParameters.get("as_fechaInicial");
    String strFechaHasta = (String)hshParameters.get("as_fechaFinal");
    String strAreaCal = (String)hshParameters.get("as_areaCal");
    String strSuspensionReason = (String)hshParameters.get("as_suspensionReason"); 
        
    String sqlStr = "BEGIN ORDERS.NP_SUSPENSION_REPORTS.SP_GET_GENERAL_REPORT(?, ?, ?, ?, ?, ?); END;";
        
    try {
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

      cstmt.setString(1, strFechaDesde);//FECHA DE INICIO DEL PERIODO
      cstmt.setString(2, strFechaHasta);//FECHA DE FIN DEL PERIODO
      cstmt.setString(3, strAreaCal);//AREA DE CAL
      cstmt.setString(4, strSuspensionReason);//MOTIVO DE LA SUSPENSION
      cstmt.registerOutParameter(5, OracleTypes.CURSOR); // CURSOR
      cstmt.registerOutParameter(6, Types.CHAR);
      cstmt.execute();
      
      strMessage = cstmt.getString(6);

      if (StringUtils.isBlank(strMessage)) {        
        rs = (ResultSet)cstmt.getObject(5);        
        
        String temp="";
                
        while (rs.next()) {
          System.out.println("ingreso a bucle de suspensiones GENERALES");         
                    
          SuspensionReportsBean srb = new SuspensionReportsBean();
          
          srb.setGeneralMotivo(StringUtils.defaultString(rs.getString(i++)));
          //System.out.println("generalMotivo : "+ srb.getGeneralMotivo());          
          srb.setGeneralArea(StringUtils.defaultString(rs.getString(i++)));
          //System.out.println("generalArea : "+ srb.getGeneralArea());          
          srb.setGeneralAsesor(StringUtils.defaultString(rs.getString(i++)));
          //System.out.println("generalAsesor : "+ srb.getGeneralAsesor());          
          srb.setGeneralProgramadas(StringUtils.defaultString(rs.getString(i++)));
          //System.out.println("generalProgramadas : "+ srb.getGeneralProgramadas());          
          srb.setGeneralRetenidas((String)StringUtils.defaultString(rs.getString(i++)));
          //System.out.println("generalRetenidas : "+ srb.getGeneralRetenidas());          
          srb.setGeneralEfectividad(StringUtils.defaultString(rs.getString(i++)));
          //System.out.println("generalEfectividad : "+ srb.getGeneralEfectividad());
                    
          arrListado.add(srb);          
          i=1;

          }
      }

    } catch (Exception e) {
      logger.error(formatException(e));
      throw new Exception(e);      
    } finally {
      closeObjectsDatabase(conn, cstmt, rs);
    }
    hshResult.put("strMessage", strMessage);
    hshResult.put("arrListado", arrListado);
    return hshResult;
  }*/
  
/*  public HashMap getGeneralSuspensionList(HashMap hshParameters) throws SQLException, Exception {

    if (logger.isDebugEnabled())
      logger.debug("--Inicio--");

    ArrayList arrListado = new ArrayList();
    HashMap hshResult = new HashMap();
    String strMessage = null;
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    int i=1;
      
    String strFechaDesde = (String)hshParameters.get("as_fechaInicial");
    String strFechaHasta = (String)hshParameters.get("as_fechaFinal");
    String strAreaCal = (String)hshParameters.get("as_areaCal");
    String strSuspensionReason = (String)hshParameters.get("as_suspensionReason"); 
        
    String sqlStr = "BEGIN ORDERS.NP_SUSPENSION_REPORTS.SP_GET_GENERAL_REPORT(?, ?, ?, ?, ?, ?); END;";
        
    try {
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

      cstmt.setString(1, strFechaDesde);//FECHA DE INICIO DEL PERIODO
      cstmt.setString(2, strFechaHasta);//FECHA DE FIN DEL PERIODO
      cstmt.setString(3, strAreaCal);//AREA DE CAL
      cstmt.setString(4, strSuspensionReason);//MOTIVO DE LA SUSPENSION
      cstmt.registerOutParameter(5, OracleTypes.CURSOR); // CURSOR
      cstmt.registerOutParameter(6, Types.CHAR);
      cstmt.execute();
      
      strMessage = cstmt.getString(6);

      if (StringUtils.isBlank(strMessage)) {        
        rs = (ResultSet)cstmt.getObject(5);        
        
        String temp="";
                
        while (rs.next()) {
          System.out.println("ingreso a bucle de suspensiones GENERALES");         
                    
          HashMap hshData = new HashMap();
          hshData.put("MOTIVO",rs.getString(1));
          hshData.put("AREA",rs.getString(2));
          hshData.put("ASESOR",rs.getString(3));
          hshData.put("PROGRAMADAS",rs.getString(4));
          hshData.put("RETENIDAS",rs.getString(5));
          hshData.put("EFECTIVIDAD",rs.getString(6));
          
          arrListado.add(hshData);
          
          i=1;

          }
      }

    } catch (Exception e) {
      logger.error(formatException(e));
      throw new Exception(e);      
    } finally {
      closeObjectsDatabase(conn, cstmt, rs);
    }
    hshResult.put("strMessage", strMessage);
    hshResult.put("arrListado", arrListado);
    return hshResult;
  }
  */
  
  
  public HashMap getDetailedSuspensionList(HashMap hshParameters) throws SQLException, Exception {

    if (logger.isDebugEnabled())
      logger.debug("--Inicio--");

    ArrayList arrListado = new ArrayList();
    HashMap hshResult = new HashMap();
    String strMessage = null;
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    int i=1;
      
    String strFechaDesde = (String)hshParameters.get("as_fechaInicial");
    String strFechaHasta = (String)hshParameters.get("as_fechaFinal");
    String strAreaCal = (String)hshParameters.get("as_areaCal");
    String strSuspensionReason = (String)hshParameters.get("as_suspensionReason");
    String strAsesor= (String)hshParameters.get("as_asesor");
    String strTipoCliente = (String)hshParameters.get("as_tipoCliente");
    String strHerramientaRetencion = (String)hshParameters.get("as_retentionTool");
        
    String sqlStr = "BEGIN ORDERS.NP_SUSPENSION_REPORTS.SP_GET_DETAILED_REPORT(?, ?, ?, ?, ?, ?, ?, ?, ?); END;";
        
    try {
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

      cstmt.setString(1, strFechaDesde);//FECHA DE INICIO DEL PERIODO
      cstmt.setString(2, strFechaHasta);//FECHA DE FIN DEL PERIODO
      cstmt.setString(3, strAreaCal);//AREA DE CAL
      cstmt.setString(4, strSuspensionReason);//MOTIVO DE LA SUSPENSION
      cstmt.setString(5, strAsesor);//ASESOR
      cstmt.setString(6, strTipoCliente);//TIPO DE CLIENTE
      cstmt.setString(7, strHerramientaRetencion);//HERRAMIENTA DE RETENCION
      cstmt.registerOutParameter(8, OracleTypes.CURSOR); // CURSOR
      cstmt.registerOutParameter(9, Types.CHAR);
      cstmt.execute();
      
      strMessage = cstmt.getString(9);

      if (StringUtils.isBlank(strMessage)) {        
        rs = (ResultSet)cstmt.getObject(8);
        
        String temp="";
                
        while (rs.next()) {
          System.out.println("ingreso a bucle de suspensiones detalladas");         
                    
          SuspensionReportsBean srb = new SuspensionReportsBean();         
          
          srb.setDetalladoArea(StringUtils.defaultString(rs.getString(i++)));
          srb.setDetalladoAsesor(StringUtils.defaultString(rs.getString(i++)));
          srb.setDetalladoTipoCliente(StringUtils.defaultString(rs.getString(i++)));
          srb.setDetalladoCodCliente(StringUtils.defaultString(rs.getString(i++)));
          srb.setDetalladoCuenta(StringUtils.defaultString(rs.getString(i++)));
          srb.setDetalladoNombreCliente(StringUtils.defaultString(rs.getString(i++)));
          srb.setDetalladoFechaRegistro((String)MiUtil.getDate(rs.getTimestamp(i++), "dd/MM/yyyy"));
          srb.setDetalladoFechaEjecucion((String)MiUtil.getDate(rs.getTimestamp(i++), "dd/MM/yyyy"));
          srb.setDetalladoMotivo(StringUtils.defaultString(rs.getString(i++)));
          srb.setDetalladoHerramienta(StringUtils.defaultString(rs.getString(i++)));
          srb.setDetalladoTelefono(StringUtils.defaultString(rs.getString(i++)));
          srb.setDetalladoEstado(StringUtils.defaultString(rs.getString(i++)));
                              
          arrListado.add(srb);          
          i=1;

          }
      }

    } catch (Exception e) {
      logger.error(formatException(e));
      throw new Exception(e);      
    } finally {
      closeObjectsDatabase(conn, cstmt, rs);
    }
    hshResult.put("strMessage", strMessage);
    hshResult.put("arrListado", arrListado);
    return hshResult;
  }
       
    public HashMap getEstadoPMC(String dominioTabla) throws SQLException, Exception {
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
      ArrayList arrEstadoPMC = new ArrayList();
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
               arrEstadoPMC.add(dominio);
            }
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
		hshDataMap.put("arrEstadoPMC", arrEstadoPMC);
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;
    }

public HashMap getActionID(String dominioTabla) throws SQLException, Exception {
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
      ArrayList arrActionID = new ArrayList();
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
               arrActionID.add(dominio);
            }
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
		 hshDataMap.put("arrActionID", arrActionID);
		 hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;
    }
  
  /**
   * Motivo:  Búsqueda de Retenciones
   * <br>Realizado por: <a href="mailto:julio.herrera@nextel.com.pe">Julio Herrera</a>
   * <br>Fecha: 17/03/2009
   *
   * @return     ArrayList de HashMap - Reparaciones
   */
  public HashMap getRetentionList(RetentionForm retentionForm) throws SQLException, Exception {
    if (logger.isDebugEnabled())
      logger.debug("--Inicio--");
    HashMap hshDataMap = new HashMap();
    ArrayList arrListado = new ArrayList();
    Connection conn = null;
    ResultSet rs = null;
    OracleCallableStatement cstmt = null;
    String strMessage = null;
    String sqlStr = generateCallForPackage("ORDERS.NP_SUSPENSION_REPORTS.SP_GET_GENERAL_REPORT", 6);
    
    //DbmsOutput dbmsOutput = null;
    long numTotalRegistros = 0;
    
    System.out.println("Login " + retentionForm.getHdnLogin());
    
    
    try {
      conn = Proveedor.getConnection();
      //dbmsOutput = new DbmsOutput(conn);
      //dbmsOutput.enable(1000000);
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
      
      //StructDescriptor sdRepairSearchForm = StructDescriptor.createDescriptor("REPAIR.TO_REPAIR_SEARCH_FORM", conn);
      /*Object[] aoRepairSearchForm = {
        repairSearchForm.getHdnLogin(), 
        repairSearchForm.getCmbTipoReparacion(), 
        repairSearchForm.getHdnTipoReparacion(), 
        repairSearchForm.getTxtNroReparacion(),
        repairSearchForm.getCmbProceso(), 
        repairSearchForm.getHdnProceso(), 
        repairSearchForm.getCmbTecnicoResponsable(),
        repairSearchForm.getHdnTecnicoResponsable(), 
        repairSearchForm.getTxtIMEI(), 
        repairSearchForm.getCmbModelo(), 
        repairSearchForm.getHdnModelo(),
        repairSearchForm.getCmbEstado(), 
        repairSearchForm.getHdnEstado(), 
        repairSearchForm.getCmbSituacion(), 
        repairSearchForm.getHdnSituacion(),
        repairSearchForm.getTxtFechaDesde(), 
        repairSearchForm.getTxtFechaHasta(), 
        repairSearchForm.getCmbProveedor(), 
        repairSearchForm.getHdnProveedor(),
        repairSearchForm.getHdnNumRegistros(), 
        repairSearchForm.getHdnNumPagina(),  
        // nuevos parametros
        repairSearchForm.getTxtFechaModifDesde(), 
        repairSearchForm.getTxtFechaModifHasta(), 
        repairSearchForm.getHdnTienda(), 
        repairSearchForm.getTxtIMEICambio(), 
        repairSearchForm.getTxtSERIECambio()
      };*/
      
      String strFechaInicial = retentionForm.getTxtFechaDesde();
      String strFechaFinal = retentionForm.getTxtFechaHasta();
      String strAreaCal = retentionForm.getHdnAreaCal();
      String strSuspensionReason = retentionForm.getHdnSuspensionReason();
               
      System.out.println("strFechaInicial : " + strFechaInicial);
      System.out.println("strFechaFinal : " + strFechaFinal);
      System.out.println("strAreaCal : " + strAreaCal);
      System.out.println("strSuspensionReason : " + strSuspensionReason);
      
      cstmt.setString(1, strFechaInicial);
      cstmt.setString(2, strFechaFinal);
      cstmt.setString(3, strAreaCal);
      cstmt.setString(4, strSuspensionReason);
      cstmt.registerOutParameter(5, OracleTypes.CURSOR);      
      cstmt.registerOutParameter(6, OracleTypes.VARCHAR);
      cstmt.executeQuery();      
      
      strMessage = cstmt.getString(6);
      
      if(StringUtils.isBlank(strMessage)) {
        rs = (ResultSet) cstmt.getObject(5);
        //numTotalRegistros = cstmt.getLong(3);
        
        while (rs.next()) {
          int i = 1;
          HashMap hshData = new HashMap();
          
          hshData.put("MOTIVO",rs.getString(1));
          hshData.put("AREA",rs.getString(2));
          hshData.put("ASESOR",rs.getString(3));
          hshData.put("PROGRAMADAS",rs.getString(4));
          hshData.put("RETENIDAS",rs.getString(5));
          hshData.put("EFECTIVIDAD",rs.getString(6));
                    
          arrListado.add(hshData);
        }
      }
      //hshDataMap.put("numTotalRegistros", String.valueOf(numTotalRegistros));
    } catch (SQLException sqle) {
      logger.error(formatException(sqle));
    } finally {
      closeObjectsDatabase(conn, cstmt, rs);
      //dbmsOutput.close();
      //dbmsOutput = null;
    }
    hshDataMap.put("strMessage", strMessage);
    hshDataMap.put("arrListado", arrListado);
    return hshDataMap;
  }
  
  
  /**
   * Motivo:  Búsqueda de PCM para mostrar en el reporte 
   * <br>Realizado por: <a href="mailto:julio.herrera@nextel.com.pe">Julio Herrera</a>
   * <br>Fecha: 24/07/2009
   *
   * @return     ArrayList de HashMap - Listado PCM
   */
  public HashMap getPCMList(PCMForm pcmForm) throws SQLException, Exception {
    if (logger.isDebugEnabled())
      logger.debug("--Inicio--");
    HashMap hshDataMap = new HashMap();
    ArrayList arrListado = new ArrayList();
    Connection conn = null;
    ResultSet rs = null;
    OracleCallableStatement cstmt = null;
    String strMessage = null;
    String sqlStr = generateCallForPackage("ORDERS.NP_SUSPENSION_REPORTS.SP_GET_AUTOM_PROCESS_SEARCH", 8);
    
    //DbmsOutput dbmsOutput = null;
    long numTotalRegistros = 0;
    
    System.out.println("Login " + pcmForm.getHdnLogin());
    
    try {
      conn = Proveedor.getConnection();
      //dbmsOutput = new DbmsOutput(conn);
      //dbmsOutput.enable(1000000);
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
      
      String strProceso = pcmForm.getHdnProceso();
      String strEstado = pcmForm.getHdnEstado();
      String strTelefono = pcmForm.getTxtTelefono();
      String strFechaDesde = pcmForm.getTxtFechaDesde();
      String strFechaHasta = pcmForm.getTxtFechaHasta();
      String strRazonSocial = pcmForm.getTxtRazonSocial();
                     
      System.out.println("strProceso : " + strProceso);
      System.out.println("strEstado : " + strEstado);
      System.out.println("strTelefono : " + strTelefono);
      System.out.println("strFechaDesde : " + strFechaDesde);
      System.out.println("strFechaHasta : " + strFechaHasta);
      System.out.println("strRazonSocial : " + strRazonSocial);
      
      cstmt.setString(1, strProceso);
      cstmt.setString(2, strEstado);
      cstmt.setString(3, strTelefono);
      cstmt.setString(4, strFechaDesde);
      cstmt.setString(5, strFechaHasta);
      cstmt.setString(6, strRazonSocial);
      cstmt.registerOutParameter(7, OracleTypes.CURSOR);      
      cstmt.registerOutParameter(8, OracleTypes.VARCHAR);
      cstmt.executeQuery();      
      
      strMessage = cstmt.getString(8);
      
      if(StringUtils.isBlank(strMessage)) {
        rs = (ResultSet) cstmt.getObject(7);        
        
        while (rs.next()) {
          int i = 1;
          HashMap hshData = new HashMap();
          
          System.out.println("Entrando al procedure de PCM - SEARCH");          
          
          hshData.put("SOLICITUD",rs.getString(1));
          hshData.put("PROCESO",rs.getString(2));
          hshData.put("FECHAPROGRAMACION",MiUtil.toFecha(rs.getDate(3)));
          hshData.put("FECHAATENCION",MiUtil.toFecha(rs.getDate(4)));
          hshData.put("ESTADO",rs.getString(5));
          hshData.put("MENSAJE",rs.getString(6));
          hshData.put("RESUMEN",rs.getString(7));
          hshData.put("RAZONSOCIAL",rs.getString(8));
          hshData.put("NEXTEL",rs.getString(9));
          hshData.put("USUARIOCREACION",rs.getString(10));
          hshData.put("FECHACREACION",MiUtil.toFecha(rs.getDate(11)));
          hshData.put("USUARIOMODIFICACION",rs.getString(12));
          hshData.put("FECHAMODIFICACION",MiUtil.toFecha(rs.getDate(13)));
          hshData.put("CONTRATO",rs.getString(14));
                    
          arrListado.add(hshData);
        }
      }
      //hshDataMap.put("numTotalRegistros", String.valueOf(numTotalRegistros));
    } catch (SQLException sqle) {
      logger.error(formatException(sqle));
    } finally {
      closeObjectsDatabase(conn, cstmt, rs);
      //dbmsOutput.close();
      //dbmsOutput = null;
    }
    hshDataMap.put("strMessage", strMessage);
    hshDataMap.put("arrListado", arrListado);

    return hshDataMap;
  }


  /**
   * Motivo: Obtiene el SalesStructId en base al ProviderGrpId
   * <br>Realizado por: <a href="mailto:alexis.gamarra@nextel.com.pe">Alexis Gamarra</a>
   * <br>Fecha: 30/07/2009
   * @param     npProviderGrpId        
   * @return    int 
   */
   public int getSalesStructId(int npProviderGrpId) throws SQLException, Exception{
      int iReturnValue = -1;
      Connection conn=null;
      OracleCallableStatement cstmt = null;
      try{
        String sqlStr =  " { ? = call NP_ORDERS05_PKG.FX_GET_SALESSTRUCTID( ? ) } ";
        
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        cstmt.registerOutParameter(1, Types.NUMERIC);
        cstmt.setInt(2, npProviderGrpId);
        
        cstmt.execute();
        iReturnValue = cstmt.getInt(1); 
      }catch(Exception e){
         logger.error(formatException(e));
         iReturnValue = -1;
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
      }
      return iReturnValue;
   }
   
   /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  CAP & CAL INICIO
     *  REALIZADO POR: CPUENTE6
     *  FECHA: 28/08/2009
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
    /**
    * Motivo: Verifica si los items fueron procesados
    * <br>Realizado por: <a href="mailto:carlos.puente@nextel.com.pe">CPUENTE</a>
    * <br>Fecha: 06/10/2009 
    * @param     orderId 
    * @return    String 
    */
        public String doValidateEquipmentReplacement(long orderId) throws Exception,SQLException{
    
       Connection conn = null; 
       OracleCallableStatement cstmt = null;
       String strMessage = null;
       HashMap hshData=new HashMap();
       try{
         String sqlStr = "{ call ORDERS.NP_REPOS_UPGRADE_PKG.SP_VALIDATE_EQUIP_REPLACEMENT(?,?)}"; 
       
         conn = Proveedor.getConnection();
         
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
             
         cstmt.setLong(1,orderId);
         cstmt.registerOutParameter(2,OracleTypes.VARCHAR);
         
         cstmt.executeUpdate();    
         strMessage = cstmt.getString(2);
                  
       }catch(Exception e){
          hshData.put("strMessage",e.getMessage()); 
       }finally{
         try{
           closeObjectsDatabase(conn,cstmt,null); 
           }catch (Exception e) {
             logger.error(formatException(e));
           }
         }

       return strMessage;                                                                
    }      
    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  CAP & CAL INICIO
     *  REALIZADO POR: CPUENTE6
     *  FECHA: 06/10/2009
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
     
     /**
    Method : getDocumentIncident
    Purpose: Obtener los tipos de suspensiones.
    Developer       Fecha       Comentario
    =============   ==========  ======================================================================
    Miguel Jurado   15/10/2009  Creación
     **/
    public static HashMap getSuspensionType(String strNpTable, 
                                              String strNpValue, 
                                              String strNpValueDesc, 
                                              String strNpStatus, 
                                              String strNpTag1, 
                                              String strNpTag2, 
                                              String strNpOrder) throws SQLException, 
                                                                        Exception {
        HashMap hshDataMap = new HashMap();
        HashMap hshtList = null;
        ArrayList arrElementICM = new ArrayList();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMensaje = null;
        try {
            String sqlStr = "BEGIN SWBAPPS.SPI_GET_NPTABLE_LST2(?,?,?,?,?,?,?,?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setString(1, strNpTable);
            cstmt.setString(2, strNpValue);
            cstmt.setString(3, strNpValueDesc);
            cstmt.setString(4, strNpStatus);
            cstmt.setString(5, strNpTag1);
            cstmt.setString(6, strNpTag2);
            cstmt.setString(7, strNpOrder);
            cstmt.registerOutParameter(8, OracleTypes.CURSOR);
            cstmt.registerOutParameter(9, OracleTypes.VARCHAR);
            cstmt.execute();
            strMensaje = cstmt.getString(9);
            if (strMensaje != null) {
                hshDataMap.put(Constante.MESSAGE_OUTPUT, strMensaje);
            } else {
                rs = (ResultSet)cstmt.getObject(8);
                while (rs.next()) {
                    hshtList = new HashMap();
                    hshtList.put("wv_nptable", rs.getString(1));
                    hshtList.put("wv_npvalue", rs.getString(2));
                    hshtList.put("wv_npvaluedesc", rs.getString(3));
                    hshtList.put("wv_npstatus", rs.getString(4));
                    hshtList.put("wv_nptag1", rs.getString(5));
                    hshtList.put("wv_nptag2", rs.getString(6));
                    hshtList.put("wv_nporder", rs.getString(7));
                    arrElementICM.add(hshtList);
                }
                hshDataMap.put("arrElementICM", arrElementICM);
            }
        } catch (Exception e) {
            logger.error(formatException(e));
            throw new Exception(e);
        } finally {
            try {
                closeObjectsDatabase(conn, cstmt, rs);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return hshDataMap;
    }

    
/***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:julio.herrera@nextel.com.pe">Julio Herrera</a>
 * <br>Motivo: Devuelve la cantidad de días calendario entre una fecha dada y el parametro
 * <br>Fecha: 03/11/2009      
/ ************************************************************************************************************************************/

  public int getAmountCalendarDays(String npCreateDate, int plazo) throws SQLException,Exception{
      
      int iReturnDaysAmount = 0;
      String strMessage = null;
      
      Connection conn = null; 
      OracleCallableStatement cstmt = null;
      try{
        String sqlStr = "BEGIN SWBAPPS.SPI_GET_NEW_DIAS_UTILES(?,?,?,?); END;";
        
        conn = Proveedor.getConnection();
        
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);          
        
        cstmt.setString(1, npCreateDate);
        cstmt.setLong(2, plazo);
        cstmt.registerOutParameter(3, Types.NUMERIC);
        cstmt.registerOutParameter(4, Types.VARCHAR);          
        cstmt.execute();
        strMessage = cstmt.getString(4);
        System.out.println("[getAmountCalendarDays] - strMessage          : " + strMessage);
        
        if(strMessage==null){
          iReturnDaysAmount = cstmt.getInt(3);
          System.out.println("[getAmountCalendarDays] - iReturnDaysAmount   : " + iReturnDaysAmount);          
        }
                
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
      return iReturnDaysAmount;
    }

/***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:julio.herrera@nextel.com.pe">Julio Herrera</a>
 * <br>Motivo: Devuelve la fecha final de un intervalo de periodo de 5 dias
 * <br>Fecha: 03/11/2009      
/ ************************************************************************************************************************************/

  public String getFechaFinalIntervalo(String npCreateDate, int plazo) throws SQLException,Exception{
      
      String iReturnFinalDate = null;
      String strMessage = null;
      
      Connection conn = null; 
      OracleCallableStatement cstmt = null;
      try{
        String sqlStr = "BEGIN SWBAPPS.SPI_GET_SUM_FECHA(?,?,?,?); END;";
        
        conn = Proveedor.getConnection();
        
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);          
        
        cstmt.setString(1, npCreateDate);
        cstmt.setLong(2, plazo);
        cstmt.registerOutParameter(3, Types.VARCHAR);
        cstmt.registerOutParameter(4, Types.VARCHAR);          
        cstmt.execute();
        strMessage = cstmt.getString(4);
        System.out.println("[getFechaFinalIntervalo] - strMessage          : " + strMessage);
        
        if(strMessage==null){
          iReturnFinalDate = cstmt.getString(3);
          System.out.println("[getFechaFinalIntervalo] - iReturnFinalDate   : " + iReturnFinalDate);          
        }
                
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
      return iReturnFinalDate;
    }

  /**
   * Motivo:  Inserta el detalle de la orden generada desde campaña
   * <br>Realizado por: <a href="mailto:percy.hidalgo@nextel.com.pe">Percy Hidalgo</a>
   * <br>Fecha: 02/02/2010
   * 
   * @param      OrderBean orderBean    
   * @param      Connection conn
   * @return     String strMessage 
  */
/*    Se está comentando esta parte de este codigo temporalmente por motivo de que
*    falta definir funcionalmente como va ser la creación de ordenes desde campañas (phidalgo)
public HashMap getOrderCampaniaInsertar(OrderBean orderBean,Connection conn) throws Exception,SQLException{      
    HashMap objHashMap = new HashMap();
    String strMessage = null;          
    OracleCallableStatement cstmt = null;

    try{ 
      String strSql = "BEGIN CAMPAIGN_WEB.SPI_REGISTER_GENERATED_OBJ(?,?,?,?,?); END;";               

      cstmt = (OracleCallableStatement)conn.prepareCall(strSql); 
      
      cstmt.setLong(1, orderBean.getNpGeneratorId());  
      cstmt.setString(2, Constante.NAME_ORIGEN_FFPEDIDOS);
      cstmt.setLong(3, orderBean.getNpOrderId());
      cstmt.setString(4, orderBean.getNpCreatedBy());
      cstmt.registerOutParameter(5, OracleTypes.VARCHAR);       
      cstmt.executeUpdate();
             
      strMessage = cstmt.getString(5);
          
      objHashMap.put("strMessage",strMessage);
      objHashMap.put("objOrderBean",orderBean);
      
    }catch(Exception e){
      logger.error(formatException(e));
      objHashMap.put("strMessage",e.getMessage());
    }finally{
       try{
        closeObjectsDatabase(null,cstmt,null); 
       }catch (Exception e) {
          logger.error(formatException(e));
       }
     } 
              
    return objHashMap;
  }
*/   
    
  /*********************************************************************************************
   * Motivo:  Valida si se ingreso o retiro un equipo al Blacklist
   * <br>Realizado por: <a href="mailto:karen.salvador@hp.com">Karen Salvador</a>
   * <br>Fecha: 02/11/2010
   * 
   * @param      long Orderid    
   * @return     String strMessage 
  **********************************************************************************************/
   public HashMap getValidateBlacklist(long lngOrderId) throws Exception,SQLException {

    HashMap objHashMap = new HashMap();
    OracleCallableStatement cstmt = null;
    Connection conn=null; 
    String strMessage = null;
    int  iResult = 0;
    try{
      String sqlStr =  "BEGIN ORDERS.NP_REPOS_UPGRADE_PKG.SP_GET_ITEM_BLACKLIST(?, ?, ?); END;";
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      
      cstmt.setLong(1, lngOrderId);
      cstmt.registerOutParameter(2, OracleTypes.NUMBER);
      cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
      cstmt.executeUpdate();
      iResult=cstmt.getInt(2);
      strMessage=cstmt.getString(3);

      objHashMap.put("iResult",iResult+""); 
      objHashMap.put("strMessage",strMessage);
             
    }catch(Exception e){
      objHashMap.put("strMessage",e.getMessage()); 
    }finally{
       try{
        closeObjectsDatabase(conn,cstmt,null); 
       }catch (Exception e) {
          logger.error(formatException(e));
       }
     }    
    return objHashMap;
  }

  /*********************************************************************************************
   * Motivo:  Genera Guia de Remision
   * <br>Realizado por: <a href="mailto:michael.valle@hp.com">Michael Valle</a>
   * <br>Fecha: 12/11/2010
   * 
   * @param      long lngOrderId    
   * @param      String strLogin    
   * @return     HashMap objHashMap 
  **********************************************************************************************/
   public HashMap doGenerateGuiaRemision(long lngOrderId, String strLogin) throws Exception,SQLException {

    HashMap objHashMap = new HashMap();
    OracleCallableStatement cstmt = null;
    Connection conn=null; 
    String strMessage = null;
    int  iError = 0;
    try{
      String sqlStr =  "BEGIN ORDERS.NP_ORDERS03_PKG.SP_GENERATE_GUIA_REMISION(?, ?, ?, ?); END;";
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      
      cstmt.setLong(1, lngOrderId);
      cstmt.setString(2, strLogin);
      cstmt.registerOutParameter(3, OracleTypes.NUMBER);
      cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
      cstmt.executeUpdate();
      iError=cstmt.getInt(3);
      strMessage=cstmt.getString(4);

      objHashMap.put("iError",iError+""); 
      objHashMap.put("strMessage",strMessage);
             
    }catch(Exception e){
      objHashMap.put("strMessage",e.getMessage()); 
    }finally{
       try{
        closeObjectsDatabase(conn,cstmt,null); 
       }catch (Exception e) {
          logger.error(formatException(e));
       }
     }    
    return objHashMap;
  }
  
  
  /**
    * Motivo: Validar si una orden tiene presupuesto
    * <br>Fecha: 24/11/2010 
    * @param     orderId 
    * @return    String 
    */
    public String doValidateBudget(OrderBean orderBean, PortalSessionBean portalBean, ArrayList itemOrderList, HashMap objItemDeviceMap) throws Exception,SQLException{
    
       Connection conn = null; 
       OracleCallableStatement cstmt = null;
       String strMessage = null;
       String strPrice = null;

       ItemBean itemBean = null;
       ItemDeviceBean itemDeviceBean = null;
       ArrayList arrItemList = new ArrayList();
       
        System.out.println("*********nbravo*************");
       try{

         String sqlStr = "{ call BUDGET_ORDERS.SPI_BUDGET_VALIDATION_PROCESS(?,?,?) }"; 
       
         conn = Proveedor.getConnection();
         
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
        //Struct descriptor para los datos de la orden.
         StructDescriptor sdOrderBudget = StructDescriptor.createDescriptor("BUDGET_ORDERS.TO_BUDGET_ORDER", conn);
         
         //Struct descriptor para los datos del item
         StructDescriptor sdItemBudget = StructDescriptor.createDescriptor("BUDGET_ORDERS.TO_BUDGET_ORDER_ITEM", conn);
         //Array descriptor de los items
         ArrayDescriptor arrayItems = ArrayDescriptor.createDescriptor("BUDGET_ORDERS.TT_BUDGET_ORDER_ITEM_LST", conn);

         for(int i=0; i<itemOrderList.size(); i++ ){
            itemBean  = (ItemBean)itemOrderList.get(i);
            System.out.println("itemBean.getNpprice();: "+itemBean.getNpprice());
            if(objItemDeviceMap!=null && objItemDeviceMap.size()>0){
              itemDeviceBean = (ItemDeviceBean)objItemDeviceMap.get(MiUtil.getString(itemBean.getNpitemid()));
            }else{
              itemDeviceBean = new ItemDeviceBean();
            }

            if (itemBean.getNpprice() ==null || itemBean.getNpprice() =="" ){
                strPrice = "0";
            }else{
                strPrice = itemBean.getNpprice();
            }

            Object[] ObjectItem = { itemBean.getNpphone(),
                                    MiUtil.getString(itemBean.getNpcontractnumber()),
                                    (itemDeviceBean!=null)?itemDeviceBean.getNpimeinumber():"",
                                    MiUtil.getString(itemBean.getNpplanid()),
                                    new Long(itemBean.getNporiginalproductid()),                                 
                                    new Long(itemBean.getNpproductid()),
                                    itemBean.getNpmodalitysell(),
                                    new Long(MiUtil.parseLong(strPrice)),
                                    new Long(itemBean.getNpsolutionid()),
                                    itemBean.getNpwarrant() };
            //STRUCT que contiene los datos del item y se agrega a una lista arrItemList
            STRUCT stcItemBudget = new STRUCT(sdItemBudget, conn, ObjectItem);
            arrItemList.add(stcItemBudget);
          }
         //ARRAY que transforma los arrItemList a un tipo arrayItems para ser agregado como parametro al ObjectOrder
         ARRAY aryItemList = new ARRAY(arrayItems, conn, arrItemList.toArray()); 
         
         Object[] ObjectOrder = {orderBean.getNpGeneratorType(),
                                  new Long(orderBean.getNpOrderId()),
                                  new Long(orderBean.getNpCustomerId()),
                                  new Long(orderBean.getNpSiteId()),
                                  orderBean.getNpStatus(),
                                  new Long(orderBean.getNpSpecificationId()),
                                  new Long(orderBean.getNpDispatchPlaceId()),
                                  new Long(portalBean.getSalesStructId()),
                                  orderBean.getNpCreatedBy(),
                                  MiUtil.toFecha(orderBean.getNpCreatedDate()),
                                  aryItemList};
         
         //STRUC que contiene los datos de la orden y el que se envia al procedure
         STRUCT stcOrderBudget = new STRUCT(sdOrderBudget, conn, ObjectOrder);
         cstmt.setSTRUCT(1, stcOrderBudget);
         cstmt.registerOutParameter(2,OracleTypes.VARCHAR);
         cstmt.registerOutParameter(3,OracleTypes.VARCHAR);
         
         cstmt.executeUpdate();
         strMessage = cstmt.getString(3);
         if(strMessage==null){
          strMessage = cstmt.getString(2);
         }                   
       }catch(Exception e){
          strMessage = e.getMessage();
          logger.error(formatException(e));
       }finally{
         try{
           closeObjectsDatabase(conn,cstmt,null); 
           }catch (Exception e) {
             logger.error(formatException(e));
           }
         }
       return strMessage;                                                                
    }
    
    /**
    * Motivo: obtiene la disponibildad del presupuesto de los canales comerciales
    * <br>Fecha: 02/12/2010 
    * @param     orderId 
    * @return    String 
    */
    public List budgetsCommercialChannels(BudgetBean budgetBean) throws Exception,SQLException{
    
        Connection conn = null; 
        OracleCallableStatement cstmt = null;
        String strMessage = null;
        ArrayList budgetList = new ArrayList();
        BudgetBean budget = null;
        HashMap objHashMap = new HashMap();
       
        try{
            String sqlStr = "";
            sqlStr = "{ call BUDGET_ORDERS.NP_BUDGET_ORDERS01_PKG.SP_BUDGET_INBOX_DETAIL(?,?,?,?) }"; 
       
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, budgetBean.getNpgeneratortype());
            cstmt.setLong(2, budgetBean.getNpgeneratorid());
            cstmt.registerOutParameter(3, OracleTypes.ARRAY, "BUDGET_ORDERS.TT_CHANNEL_BUDGET_DETAIL_LST");
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            
            strMessage=cstmt.getString(4);
            ARRAY arrybudgetList = (ARRAY)cstmt.getObject(3);           
            for(int i=0; i<arrybudgetList.getOracleArray().length; i++){
                int a = 0;
                STRUCT stcBudget = (STRUCT) arrybudgetList.getOracleArray()[i];
                budget = new BudgetBean();
                budget.setNpDescription(stcBudget.getAttributes()[a++].toString());
                budget.setNpName(stcBudget.getAttributes()[a++].toString());
                budget.setNpBudgetAmount(MiUtil.defaultBigDecimal(stcBudget.getAttributes()[a++],new BigDecimal(0)).doubleValue());
                budget.setNpConsumedAmount(MiUtil.defaultBigDecimal(stcBudget.getAttributes()[a++],new BigDecimal(0)).doubleValue());
                budget.setResidue(MiUtil.defaultBigDecimal(stcBudget.getAttributes()[a++],new BigDecimal(0)).doubleValue());
                budget.setPresupuesto(MiUtil.defaultBigDecimal(stcBudget.getAttributes()[a++],new BigDecimal(0)).doubleValue());
                budgetList.add(budget);
            }
            objHashMap.put("strMessage",strMessage);
                            
        }catch(Exception e){
            strMessage = e.getMessage();
            e.printStackTrace();
            throw new Exception(e);
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null); 
            }catch (Exception e) {
                logger.error(formatException(e));
            }
         }
       return budgetList;                                                                
    }
 
    /**
    * Motivo: obtiene la disponibildad del presupuesto de los canales de reserva
    * <br>Fecha: 02/12/2010 
    * @param     orderId 
    * @return    String 
    */
    public List budgetsReserveChannels(BudgetBean budgetBean) throws Exception,SQLException{
    
        Connection conn = null; 
        OracleCallableStatement cstmt = null;
        HashMap objHashMap = new HashMap();
        String strMessage = null;
        ArrayList budgetList = new ArrayList();
        BudgetBean budget = null;
        
        try{
            String sqlStr = "";

            sqlStr = "{ call BUDGET_ORDERS.NP_BUDGET_ORDERS01_PKG.SP_BUDGET_INBOX_DETAIL_RESERVE(?,?,?,?) }"; 
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, budgetBean.getNpgeneratortype());
            cstmt.setLong(2, budgetBean.getNpgeneratorid());
            cstmt.registerOutParameter(3, OracleTypes.ARRAY, "BUDGET_ORDERS.TT_CHANNEL_BUDGET_DETAIL_LST");
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            
            strMessage=cstmt.getString(4);
            ARRAY arrybudgetList = (ARRAY)cstmt.getObject(3);
            for(int i=0; i<arrybudgetList.getOracleArray().length; i++){
                int a = 0;
                STRUCT stcBudget = (STRUCT) arrybudgetList.getOracleArray()[i];
                budget = new BudgetBean();
                budget.setNpDescription(stcBudget.getAttributes()[a++].toString());
                budget.setNpName(stcBudget.getAttributes()[a++].toString());
                budget.setNpBudgetAmount(MiUtil.defaultBigDecimal(stcBudget.getAttributes()[a++],new BigDecimal(0)).doubleValue());
                budget.setNpConsumedAmount(MiUtil.defaultBigDecimal(stcBudget.getAttributes()[a++],new BigDecimal(0)).doubleValue());
                budget.setResidue(MiUtil.defaultBigDecimal(stcBudget.getAttributes()[a++],new BigDecimal(0)).doubleValue());
                budgetList.add(budget);
            }
            objHashMap.put("strMessage",strMessage);
                            
        }catch(Exception e){
            strMessage = e.getMessage();
            throw new Exception(e);
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null); 
            }catch (Exception e) {
                logger.error(formatException(e));
            }
         }
       return budgetList;                                                                
    }
    
   /**
   * Motivo: Suspención de equipos activos de una Orden
   * <br>Realizado por: <a href="mailto:joseph.nino-de-guzman-diaz@hp.com">Joseph Niño de Guzman</a>
   * <br>Fecha: 24/11/2010
   * @param     long        
   * @return    HashMap 
   */ 
   public HashMap doGenerarSuspenderEquipos(long lOrderId)     
   throws SQLException, Exception{
   
      OracleCallableStatement cstmt = null;   
      Connection conn=null;
      String strMessage=null;        
      HashMap hshData=new HashMap();
      int iError=0;
      
      String sqlStr = "BEGIN ORDERS.SPI_SUSPEND_CONTRACT_ITEMS(?, ?); END;";
      
      try{
         conn = Proveedor.getConnection();      
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         cstmt.setLong(1, lOrderId);                                             
         cstmt.registerOutParameter(2, Types.VARCHAR);         
         
         cstmt.execute();
         strMessage = cstmt.getString(2);                   
      }
      catch(Exception e){
          hshData.put("strMessage",e.getMessage());
      }
      finally{
         closeObjectsDatabase(conn,cstmt,null);
      } 
      hshData.put("strMessage",strMessage);      
      return hshData;
   }
   
   /**
   * Motivo: Obtiene el status de equipos de una orden
   * <br>Realizado por: <a href="mailto:joseph.nino-de-guzman-diaz@hp.com">Joseph Niño de Guzman</a>
   * <br>Fecha: 29/11/2010
   * @param     long       
   * @param     String    
   * @return    HashMap 
   */ 
   
   public HashMap doGetEquipmentStatus(long lOrderId,String strUserId)     
   throws SQLException, Exception{
   
      OracleCallableStatement cstmt = null;   
      Connection conn=null;
      String strResult=null;       
      String strMessage=null;        
      HashMap hshData=new HashMap();
      int iError=0;
      
      String sqlStr = "BEGIN BPEL_WORKFLOW.NP_BUSINESS_RULE_PKG.SP_GET_EQUIPMENT_STATUS(?, ?, ?,?); END;";
      
      try{
         conn = Proveedor.getConnection();      
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         cstmt.setLong(1, lOrderId);                                             
         cstmt.setString(2, strUserId);                                                
         cstmt.registerOutParameter(3, Types.VARCHAR);
         cstmt.registerOutParameter(4, Types.VARCHAR);
         
         cstmt.execute();     
         strResult = cstmt.getString(3);       
         strMessage = cstmt.getString(4);       
      }
      catch(Exception e){          
          hshData.put("strMessage",e.getMessage());
      }
      finally{
         closeObjectsDatabase(conn,cstmt,null);
      }       
      hshData.put("strResult",strResult);  
      hshData.put("strMessage",strMessage);
      return hshData;
   } 
   
    /**
    * PHIDALGO
    * Motivo: Obtiene los motivos de rechazo del inbox de Presupuesto
    * <br>Fecha: 20/12/2010 
    * @param     orderId 
    * @return    String 
    */
    public Map doGetBudgetReasons() throws Exception,SQLException{
        
        Connection conn = null; 
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        HashMap objHashMap = new HashMap();
        ArrayList list = new ArrayList();
        BudgetBean budget = null;
        String strMessage = null;
        
        try{
            String sqlStr = "";

            sqlStr = "{ call BUDGET_ORDERS.NP_BUDGET_ORDERS01_PKG.SP_GET_BUDGET_REASONS(?,?) }"; 
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            
            strMessage=cstmt.getString(2);
            if(strMessage==null){
              rs = (ResultSet)cstmt.getObject(1);
              while (rs.next()) {
                  budget = new BudgetBean();
                  budget.setNpBudgetReasonId(rs.getInt(1));
                  budget.setNpdescriptionReason(rs.getString(2));
                  list.add(budget); 
              }
              
            }
            objHashMap.put("strMessage",strMessage);
            objHashMap.put("budgetReasonList",list);
                            
        }catch(Exception e){
            objHashMap.put("strMessage",e.getMessage());
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,rs); 
            }catch (Exception e) {
                logger.error(formatException(e));
            }
         }
       return objHashMap; 
    }
    
   /**
     * PHIDALGO
    * Motivo: obtiene la ultima descripción del rechazo del inbox de Presupuesto
    * <br>Fecha: 02/12/2010 
    * @param     orderId 
    * @return    String 
    */
    public Map doGetLastReasonDescription(BudgetBean budgetBean) throws Exception,SQLException{
        
        Connection conn = null; 
        OracleCallableStatement cstmt = null;
        HashMap objHashMap = new HashMap();
        String strRejectdescription = null;
        String strMessage = null;
        
        try{
            String sqlStr = "";

            sqlStr = "{ call BUDGET_ORDERS.NP_BUDGET_ORDERS01_PKG.SP_GET_LAST_REJECT_DESCRIPT(?,?,?,?) }"; 
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, budgetBean.getNpgeneratortype());                                             
            cstmt.setLong(2, budgetBean.getNpgeneratorid());                                                
            cstmt.registerOutParameter(3, Types.VARCHAR);
            cstmt.registerOutParameter(4, Types.VARCHAR);
            cstmt.executeQuery();
            
            strMessage=cstmt.getString(4);
            strRejectdescription=cstmt.getString(3);
            
            objHashMap.put("strMessage",strMessage);
            objHashMap.put("strRejectdescription",strRejectdescription);
                            
        }catch(Exception e){
            objHashMap.put("strMessage",e.getMessage());
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null); 
            }catch (Exception e) {
                logger.error(formatException(e));
            }
         }
       return objHashMap; 
    }
    
   /**
   * PHIDALGO
   * Motivo: Actualiza los datos desde el inbox de Presupuesto cuando se utiliza la acción Rechazar 
   * <br>Fecha: 06/02/2009
   * @param     lngSpecificationId          
   * @return    int 
   */
   public String updApprovalAction(BudgetBean budgetBean, PortalSessionBean objPortalSesBean) throws SQLException, Exception{        
    
        Connection conn = null; 
        OracleCallableStatement cstmt = null; 
        String strMessage=null;

        try{
          String strSql = "{ call BUDGET_ORDERS.NP_BUDGET_ORDERS01_PKG.SP_UPD_APPROVAL_ACTION(?,?,?,?,?,?,?) }";
          conn = Proveedor.getConnection();
          cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
          cstmt.setString(1, budgetBean.getNpgeneratortype());
          cstmt.setLong(2, budgetBean.getNpgeneratorid());
          cstmt.setString(3, budgetBean.getNpapprovalaction());
          cstmt.setLong(4, budgetBean.getNpBudgetReasonId());
          cstmt.setString(5, budgetBean.getNpdescriptionReason());
          cstmt.setString(6, objPortalSesBean.getLogin());
          cstmt.registerOutParameter(7, Types.VARCHAR);     
          cstmt.executeQuery();
          strMessage = cstmt.getString(7);                                                                       
          
        }catch(Exception e){
            strMessage = e.getMessage();
          }
          finally{
            try{
               closeObjectsDatabase(conn,cstmt,null); 
            }catch (Exception e) {
               logger.error(formatException(e));
            }
          }  
        return strMessage;
     } 

  /**
   * Motivo: Método que realiza la evaluación de volumen de orden
   * <br>Realizado por: <a href="mailto:cesar.lozza@hp.com">César Lozza</a>
   * <br>Fecha: 09/11/2010
   * @param customerId
   * @param specificationId
   * @param typeWindow
   * @param itemsList
   * @return HashMap
   * @throws Exception
   */
  public HashMap evaluateOrderVolume(int customerId, int specificationId, String typeWindow, List itemsList) throws Exception {

    Connection conn = null;
    OracleCallableStatement cstmt = null;
    OracleResultSet adrs = null;
    HashMap objHashMap = new HashMap();
    String strMsg = null;
    String strSql = "BEGIN ORDERS.SPI_GET_ORDER_VOLUME(?,?,?,?,?,?); END;";
    ItemBean itemBean = null;

    try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
      StructDescriptor sdItemBean = StructDescriptor.createDescriptor("ORDERS.TO_ORDER_ITEM", conn);
      ArrayDescriptor  adItemBean = ArrayDescriptor.createDescriptor("ORDERS.TT_ORDER_ITEM_LST", conn);
      ArrayList arrItemBean = new ArrayList();
    
      STRUCT stcItemBean = null;

      for(int i=0; i<itemsList.size(); i++){
        itemBean = (ItemBean)itemsList.get(i);
         Object[] objItemBean = { String.valueOf(itemBean.getNpIndice()), String.valueOf(itemBean.getNpitemid()),
                                  String.valueOf(itemBean.getNporderid()), itemBean.getNpmodalitysell(),
                                  String.valueOf(itemBean.getNpplanid()), String.valueOf(itemBean.getNpproductid()),
                                  String.valueOf(itemBean.getNpquantity()), itemBean.getNpprice(), itemBean.getNprent(),
                                  itemBean.getNpdiscount(), itemBean.getNporiginalprice(), String.valueOf(itemBean.getNpsolutionid()),
                                  String.valueOf(itemBean.getNppromotionid()), null};

        stcItemBean = new STRUCT(sdItemBean, conn, objItemBean);

        arrItemBean.add(stcItemBean);
      }

      ARRAY aryItemBean = new ARRAY(adItemBean, conn, arrItemBean.toArray());

      cstmt.setInt(1, customerId);
      cstmt.setInt(2, specificationId);
      cstmt.setString(3, typeWindow);
      cstmt.setARRAY(4, aryItemBean);
      cstmt.registerOutParameter(5, OracleTypes.ARRAY, "ORDERS.TT_ORDER_ITEM_LST");
      cstmt.registerOutParameter(6, OracleTypes.VARCHAR);

      cstmt.executeUpdate();

      aryItemBean = (ARRAY)cstmt.getObject(5);
      adrs = (OracleResultSet) aryItemBean.getResultSet();
      strMsg = cstmt.getString(6);

      itemsList = new ArrayList();
      while(adrs.next()) {
        stcItemBean = adrs.getSTRUCT(2);
        itemBean = new ItemBean();

        itemBean.setNpIndice(Integer.parseInt(stcItemBean.getAttributes()[0].toString()));
        itemBean.setNpitemid(Integer.parseInt(stcItemBean.getAttributes()[1].toString()));
        itemBean.setNporderid(Integer.parseInt(stcItemBean.getAttributes()[2].toString()));
        itemBean.setNpmodalitysell(stcItemBean.getAttributes()[3].toString());
        itemBean.setNpplanid(Integer.parseInt(stcItemBean.getAttributes()[4].toString()));
        itemBean.setNpproductid(Integer.parseInt(stcItemBean.getAttributes()[5].toString()));
        itemBean.setNpquantity(Integer.parseInt(stcItemBean.getAttributes()[6].toString()));
        itemBean.setNpprice(stcItemBean.getAttributes()[7].toString());
        itemBean.setNprent(stcItemBean.getAttributes()[8].toString());
        if(stcItemBean.getAttributes()[9] != null){
          itemBean.setNpdiscount(stcItemBean.getAttributes()[9].toString());
        }
        itemBean.setNporiginalprice(stcItemBean.getAttributes()[10].toString());
        itemBean.setNpsolutionid(Integer.parseInt(stcItemBean.getAttributes()[11].toString()));
        itemBean.setNppromotionid(Integer.parseInt(stcItemBean.getAttributes()[12].toString()));
        if(stcItemBean.getAttributes()[13] != null){
          itemBean.setNpAplicarVO(stcItemBean.getAttributes()[13].toString());
        }
        else{
          itemBean.setNpAplicarVO(null);
        }

        itemsList.add(itemBean);
      }

      objHashMap.put("strMessage", strMsg);
      objHashMap.put("objArrayList", itemsList);

    }

    catch(SQLException e){
      logger.error(formatException(e));
      objHashMap.put("strMessage",e.getMessage());
   }finally{
      try{
        closeObjectsDatabase(conn,cstmt,adrs);
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }
    return objHashMap;
  }
  
  /**
   * Motivo: Método que obtiene la cantidad de items a los cuales se aplicó promoción por volumen de orden
   * <br>Realizado por: <a href="mailto:cesar.lozza@hp.com">César Lozza</a>
   * <br>Fecha: 20/12/2010
   * @param customerId
   * @param specificationId
   * @param typeWindow
   * @param itemsList
   * @return int
   * @throws Exception
   */
  public int getOrderVolumeCount(int orderId)throws Exception {
  
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    String strSql = null;
    String strMsg = null;
    int itemsCount = 0;
    
    try{
    
      strSql = "BEGIN ORDERS.NP_ORDERS04_PKG.SP_GET_ORDER_VOLUME_COUNT(?,?,?); END;";
        
      conn = Proveedor.getConnection();
        
      cstmt = (OracleCallableStatement)conn.prepareCall(strSql);          
      
      cstmt.setInt(1, orderId);
      cstmt.registerOutParameter(2, Types.NUMERIC);
      cstmt.registerOutParameter(3, Types.VARCHAR);          
      cstmt.execute();
      strMsg = cstmt.getString(3);
      
      if(strMsg==null){
        itemsCount = cstmt.getInt(2);    
      }
    }
    catch(Exception e){
      logger.error(formatException(e));
    }
    finally{
      try{
        closeObjectsDatabase(conn,cstmt,null);
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }
    
    return itemsCount;
  
  }
  
	/**
     * Motivo: Solicitud a almacén. (Historia de Accion)
     * <br>Realizado por: <a>PCASTILLO</a>
     * <br>Fecha: 06/03/2013
     * 
     * @param		strOrderId     Ej: Id de la Orden
     * @return		ArrayList de HashMap      
     */
    public ArrayList getRequestOLListByOrder(long lOrderId)throws Exception,SQLException {
		Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		String error;
        try {
            String sqlStr = "BEGIN ORDERS.NP_ORDERS09_PKG.SP_GET_ORDER_REQUESTOL(?,?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, String.valueOf(lOrderId));
			cstmt.registerOutParameter(2, OracleTypes.CURSOR);
			cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();
			rs = (ResultSet)cstmt.getObject(2);
            error = cstmt.getString(3);
            if (StringUtils.isNotEmpty(error)) {
                throw new SQLException(error);
            }
            while (rs.next()) {
                HashMap objHashMap = new HashMap();
				int i = 1;
				objHashMap.put("nprequestolid", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("nprequestnumber", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("npcreateddate", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("npcreatedby", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("npmodifydate", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("npmodifyby", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("tipo_doc", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("tipo_sol", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("estado", StringUtils.defaultString(rs.getString(i++),""));
				     
				arrList.add(objHashMap);
            }
        } catch (SQLException sqle) {
            logger.error(formatException(sqle));
        } finally {
				closeObjectsDatabase(conn,cstmt,rs);    
        }
        return arrList;
    }

  public int getEnabledCourier(long orderId)throws Exception {
  
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    String strSql = null;
    String strMsg = null;
    int iEnabledCourier = 0;
    
    try{
    
      strSql = "BEGIN ORDERS.NP_ORDERS_REQUEST_PKG.SP_GET_ENABLED_COURIER(?,?,?); END;";
        
      conn = Proveedor.getConnection();
        
      cstmt = (OracleCallableStatement)conn.prepareCall(strSql);          
      
      cstmt.setLong(1, orderId);
      cstmt.registerOutParameter(2, Types.NUMERIC);
      cstmt.registerOutParameter(3, Types.VARCHAR);          
      cstmt.execute();
      strMsg = cstmt.getString(3);
      
      if(strMsg==null){
        iEnabledCourier = cstmt.getInt(2);    
      }
    }
    catch(Exception e){
      logger.error(formatException(e));
    }
    finally{
      try{
        closeObjectsDatabase(conn,cstmt,null);
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }
    
    return iEnabledCourier;
  
  }
  
    /**
    * Motivo: Retorna true si la orden requiere verificación de documentos.
    * <br>Realizado por: <a href="mailto:miguel.montoya@hp.com">Miguel Ángel Montoya</a>
    * <br>Fecha: 07/11/2013
    * @param     orderId        
    * @return    boolean 
    */    
    public boolean requiresDocumentVerification(long orderId) throws SQLException, Exception{
       
       int iReturnValue = -1;
       Connection conn=null;
       OracleCallableStatement cstmt = null;
       try{
         String sqlStr =  " { ? = call CREDIT.FXI_REQUIRES_DOCUMENT_VERIF(?) } ";
         
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         cstmt.registerOutParameter(1, Types.NUMERIC);   
         cstmt.setLong(2, orderId);                     
         
         cstmt.execute();
         iReturnValue = cstmt.getInt(1); 
       }catch(Exception e){
          logger.error(formatException(e));
          iReturnValue = -1;
       }finally{
         try{
           closeObjectsDatabase(conn,cstmt,null); 
         }catch (Exception e) {
           logger.error(formatException(e));
         }
      }      
       return iReturnValue == 1;
    }  
    
    //AMENDEZ | PRY-1141: Se adiciono parametro paymenttype
    public static OrderDetailBean getSearchOrderById(long orderIdi,String userLogin,int paymenttype) throws SQLException, Exception{
      logger.info("********************INICIO OrderDAO > getSearchOrderById********************");
        logger.info("[PASO 01:orderIdi recuperado en OrderDAO ][getSearchOrderById][" +orderIdi+"]");
        logger.info("[PASO 02:userLogin recuperado en OrderDAO ][getSearchOrderById][" +userLogin+"]");
      
        //INICIO: AMENDEZ | PRY-1141
        logger.info("[PASO 02:paymenttype recuperado en OrderDAO ][getSearchOrderById][" +paymenttype+"]");
        //FIN: AMENDEZ | PRY-1141
        Connection conn = null; 
        ResultSet rs=null;
        String strMessage  =null; 
        OracleCallableStatement cstm = null;        
        OrderDetailBean orbResume = null;           
      
      String strSql = "BEGIN ORDERS.NP_TE_PAYMENT_DOCUMENT_PKG.SP_GET_ORDER_DETAIL_TE( ?, ?, ?, ? , ?); END;";
        
        try{
            conn = Proveedor.getConnection();
                       cstm = (OracleCallableStatement) conn.prepareCall(strSql);                     
                       cstm.setLong(1, orderIdi);
                       cstm.setString(2, userLogin);
        cstm.setInt(3, paymenttype);
        cstm.registerOutParameter(4, OracleTypes.VARCHAR);//Types.CHAR
        cstm.registerOutParameter(5, OracleTypes.CURSOR);
                       cstm.execute();                                                              
            
        strMessage = cstm.getString(4);
        logger.info("[PASO 04:orderIdi strMessage ][getSearchOrderById][" +strMessage+"]");
            orbResume = new OrderDetailBean();
            if( strMessage == null){
          rs = (ResultSet)cstm.getObject(5);
              if (rs.next()) {
                orbResume.setNpOrderId(rs.getInt("npOrderId"));
                orbResume.setSwName(rs.getString("swName"));
                orbResume.setNpShortName(rs.getString("npShortName"));
                orbResume.setNpDescription(rs.getString("npDescription"));
                orbResume.setNpTypeService(rs.getString("npTypeService"));
                orbResume.setN_isSamebuilding(rs.getInt("n_isSamebuilding"));
                //INICIO: PRY-0864_2 | AMENDEZ
                orbResume.setNpvep(rs.getInt("npvep"));
                orbResume.setNpvepaymenttotal(rs.getDouble("npvepaymenttotal"));
                orbResume.setNpinitialquota(rs.getDouble("npinitialquota"));
                orbResume.setNpvepquantityquota(rs.getInt("npvepquantityquota"));
                orbResume.setNpamountfinanced(rs.getDouble("npamountfinanced"));
                orbResume.setMessage("SUCCESSFUL");
                //FIN: PRY-0864_2 | AMENDEZ

            //INICIO: AMENDEZ | PRY-1141
            orbResume.setNpqa(rs.getDouble("npqa"));
            orbResume.setNppaymentorderquotaid(rs.getLong("nppaymentorderquotaid"));
            //FIN: AMENDEZ | PRY-1141
           }
        }else{
          orbResume.setMessage(strMessage);
        }
      }catch (Exception e) {
        logger.error("[ERRROR]",e);
        throw new Exception(e);
      }
        finally{
          closeObjectsDatabase(conn,cstm,rs);
        }
      logger.info("********************FIN OrderDAO > getSearchOrderById********************");
        return orbResume;     
    
    } 
    
    //JLIMAYMANTA
  //AMENDEZ | PRY-1141: Se adiciono parametro paymenttype y paymentOrderQuotaId
  public  HashMap saveOrderPaymentTE(long orderIdi,double monto,String hdnRa,String hdnVoucher,String hdnComentario,String hdnNumLogin,String hdnUser,int paymenttype,long paymentOrderQuotaId)
    throws SQLException, Exception{
    
    logger.info("********************INICIO OrderDAO > saveOrderPaymentTE********************");
       OracleCallableStatement cstmt = null;        
       String strMessage=null;
       Connection conn=null;
       HashMap hshData=new HashMap();      
      
    String strSql = "BEGIN ORDERS.NP_TE_PAYMENT_DOCUMENT_PKG.PL_PAYMENT_DOCUMENT_SAVE_TE( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ); END;";
       try{
        logger.info("orderIdi       :  "+orderIdi);
        logger.info("monto          :  "+monto);
        logger.info("hdnRa          :  "+hdnRa);
        logger.info("hdnVoucher     :  "+hdnVoucher);
        logger.info("hdnComentario  :  "+hdnComentario);
        logger.info("hdnNumLogin    :  "+hdnNumLogin);
        logger.info("hdnUser        :  "+hdnUser);
        //INICIO: AMENDEZ | PRY-1141
        logger.info("paymenttype            :  "+paymenttype);
        logger.info("paymentOrderQuotaId    :  "+paymentOrderQuotaId);
        //FIN: AMENDEZ | PRY-1141
          conn = Proveedor.getConnection();
          cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
          cstmt.setLong(1, orderIdi);
          cstmt.setDouble(2, monto);
          cstmt.setString(3, hdnRa); 
          cstmt.setString(4, hdnVoucher); 
          cstmt.setString(5, hdnComentario);
          cstmt.setString(6, hdnNumLogin); 
          cstmt.setString(7, hdnUser);
        //INICIO: AMENDEZ | PRY-1141
          cstmt.setInt(8, paymenttype);
          cstmt.setLong(9, paymentOrderQuotaId);
        //FIN: AMENDEZ | PRY-1141
          cstmt.registerOutParameter(10,OracleTypes.VARCHAR);
          
          cstmt.executeUpdate();         
        strMessage = cstmt.getString(10);
          hshData.put("strMessage",strMessage);        
         logger.info("strMessage        :  "+strMessage);
   
       }
       catch (Exception e) {
           logger.error(formatException(e));
           hshData.put("strMessage",e.getMessage()); 
       }
       finally{
           try{
               closeObjectsDatabase(conn, cstmt, null);  
           }catch (Exception e) {
             logger.error(formatException(e));
           }
       }   
       logger.info("********************FIN OrderDAO > saveOrderPaymentTE********************");
       return hshData;
    }
    //EFLORES 22/08/2017 Agregan parametros para registrarlos
  public  HashMap saveOrderPaymentTE(long orderIdi,double monto,String hdnRa,String hdnVoucher,String hdnComentario,String hdnNumLogin,String hdnUser,Integer hdnFlgVep,
                                     Double txtCuotaInicial, Double hdnMontoFinanciado,Integer hdnNumCuotas)
          throws SQLException, Exception{

    OracleCallableStatement cstmt = null;
    String strMessage=null;
    Connection conn=null;
    HashMap hshData=new HashMap();

    String strSql = "BEGIN ORDERS.NP_TE_PAYMENT_DOCUMENT_PKG.PL_PAYMENT_DOCUMENT_SAVE_TE( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); END;";
    try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
      cstmt.setLong(1, orderIdi);
      cstmt.setDouble(2, monto);
      cstmt.setString(3, hdnRa);
      cstmt.setString(4, hdnVoucher);
      cstmt.setString(5, hdnComentario);
      cstmt.setString(6, hdnNumLogin);
      cstmt.setString(7, hdnUser);
      cstmt.setObject(8,hdnFlgVep);
      cstmt.setObject(9, txtCuotaInicial);
      cstmt.setObject(10,hdnMontoFinanciado);
      cstmt.setObject(11,hdnNumCuotas);
      cstmt.registerOutParameter(12,OracleTypes.VARCHAR);
    
      cstmt.executeUpdate();
      strMessage = cstmt.getString(12);
      hshData.put("strMessage", strMessage);

    }
    catch (Exception e) {
      logger.error(formatException(e));
      hshData.put("strMessage", e.getMessage());
    }
    finally{
      try{
        closeObjectsDatabase(conn, cstmt, null);
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }

    return hshData;
  }

     /**
     Method : valOrderPrintLabel
     *
     * Motivo: Valida y obtiene datos de la orden para impresion de etiquetas.
     * <br>Realizado por: <a href="mailto:jorge.gabriel@teamsoft.com.pe">Jorge Gabriel</a>
     * <br>Fecha: 07/08/2015
     * @return HashMap que conbtiene datos para la impresion de etiqueta.
     */
     public HashMap valOrderPrintLabel(long lOrderId) throws SQLException, Exception {
         if (logger.isDebugEnabled()) {
             logger.debug("--Inicio--");
         }
         HashMap    hshDataMap = new HashMap();
         ArrayList  itemsList = new ArrayList();
         STRUCT     stcItemBean  = null;
         ItemBean   itemBean     = null;
         Connection conn         = null;
         OracleCallableStatement cstmt = null;
         String strMessage = null;
         String specificationid="";
         String customername="";
         String customernameid="";
         try{
             String sqlStr = "BEGIN NP_ORDERS39_PKG.SP_VALIDA_ORDER(?, ?, ?); END;";
             conn =  Proveedor.getConnection();
             cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
             cstmt.setLong(1, lOrderId);
             //cstmt.registerOutParameter(2, OracleTypes.JAVA_STRUCT, "ORDERS.TO_ORDER");
             cstmt.registerOutParameter(2, OracleTypes.STRUCT, "ORDERS.TO_ORDER");

             cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
             cstmt.executeQuery();

             strMessage=cstmt.getString(3);
             if (strMessage == null){
                 oracle.sql.STRUCT td = (oracle.sql.STRUCT)cstmt.getObject(2);
                 Object[] x = td.getAttributes();

                 for(int i=0; i<x.length;i++) System.out.println("x["+i+"]"+x[i]);

                 specificationid = MiUtil.defaultString(x[1], "");
                 customername = MiUtil.defaultString(x[7], "");
                 customernameid = MiUtil.defaultString(x[9], "");

                 System.out.println("specificationid: "+specificationid);
                 System.out.println("customername: "+customername);
                 System.out.println("customernameid: "+customernameid);

                 ARRAY aryResponsibleAreaList = (ARRAY) x[30];
                 OracleResultSet adrs = (OracleResultSet) aryResponsibleAreaList.getResultSet();
                 while(adrs.next()) {

                     stcItemBean = adrs.getSTRUCT(2);

                     itemBean = new ItemBean();
                     itemBean.setNpitemid(Long.valueOf(stcItemBean.getAttributes()[10].toString()));
                     itemBean.setNpimeinumber(MiUtil.defaultString(stcItemBean.getAttributes()[0], ""));
                     itemBean.setNpplanname(MiUtil.defaultString(stcItemBean.getAttributes()[3], ""));
                     itemBean.setNpphone(MiUtil.defaultString(stcItemBean.getAttributes()[8], ""));
                     itemBean.setNporderid(lOrderId);
                     itemBean.setNpwarrant(customernameid);
                     itemBean.setNpsolutionname(customername);
                     itemBean.setNpAplicarVO(specificationid);
                     itemsList.add(itemBean);

                 }
             }
             
             //hshDataMap.put("strMessage",strMessage);
             System.out.println(itemsList.toString());
             hshDataMap.put("objArrayList", itemsList);
             hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
         } finally {
             closeObjectsDatabase(conn,cstmt,null);
         }
         return hshDataMap;
     }


    /**
     Method : sendPrintLabels
     *
     * Motivo: envia los items de la orden a un tabla intermedia y luego manda a imprimir.
     * <br>Realizado por: <a href="mailto:jorge.gabriel@teamsoft.com.pe">Jorge Gabriel</a>
     * <br>Fecha: 09/09/2015
     * @return String   mensaje de error.
     */
    public String sendPrintLabels(long lOrderId, long lSpecificationId, String lItemsIds, long lCustomerId, String lCustomerName,String wsLogin,long buildingId) throws SQLException, Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("--Inicio--");
        }
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage = "";
        try{
            String sqlStr = "BEGIN NP_ORDERS39_PKG.SP_PRINT_LABEL(?, ?, ?, ?, ?, ?, ?, ?); END;";

            conn =  Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

            System.out.println("lOrderId : "+lOrderId);
            System.out.println("lSpecificationId : "+lSpecificationId);
            System.out.println("lItemsIds : "+lItemsIds);
            System.out.println("lCustomerId : "+lCustomerId);
            System.out.println("lCustomerName : "+lCustomerName);
            System.out.println("lOrderId : "+lOrderId);
            System.out.println("wsLogin : "+wsLogin);
            System.out.println("buildingId : "+buildingId);

            cstmt.setLong(1, lOrderId);
            cstmt.setLong(2, lSpecificationId);
            cstmt.setString(3, lItemsIds);
            cstmt.setLong(4, lCustomerId);
            cstmt.setString(5, lCustomerName);
            cstmt.registerOutParameter(6, OracleTypes.VARCHAR);
            cstmt.setString(7, wsLogin);
            cstmt.setLong(8, buildingId);
            cstmt.executeQuery();

            strMessage=cstmt.getString(6);

        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        return strMessage;
    }
    /**
     Method : doValidatePostVenta
     *
     * Motivo: validacion post venta.
     * <br>Realizado por: <a href="mailto:lhuapaya@practiaconsulting.com">Luis Huapaya</a>
     * <br>Fecha: 23/09/2015
     * @return String   mensaje de error.
     */
    public String doValidatePostVentaBolCel(long orderId) throws SQLException, Exception {
       
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage = null;
        try {
            String sqlStr = "BEGIN ORDERS.SPI_VALIDATE_POSTVENTA(?, ?); END;";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

            cstmt.setLong(1, orderId);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);

            cstmt.executeQuery();

            strMessage = cstmt.getString(2);
        }catch(Exception e){
            strMessage = e.getMessage();
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        return strMessage;
    }

    /** EFLORES
     Method : doValidateUltimaPreevaluacion
     *
     * Motivo: Validar que un numero telefonico de la ultima preevaluacion de un cliente se encuentre en
     * la lista de numeros ingresados en una orden de portabilidad postpago npspecificationid = 2068
     * cadenaNumeros ingresa numeros separados por , y '' ejemplo: '1','2','3'...
     * <br>Realizado por: <a href="mailto:eddy.flores@hpe.com">Eddy Flores</a>
     * <br>Fecha: 29/12/2015
     * @return Integer valor 0 : El numero no se encuentra en la lista, 1 : El numero se encuentra en la lista.
     * @retun String mensaje de log para error
     */
    public String doValidateUltimaPreevaluacion(String customerid,String categoryId,String cadenaNumeros,String cadenaModalidad,String userLogin) throws SQLException,Exception {
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        Integer result = 0;
        String strMessage = null; //JBALCAZAR PRY-1055
        System.out.println("doValidateUltimaPreevaluacion cadena :"+cadenaModalidad);
        try {
            Long cusId = Long.parseLong(customerid);
            Long catId = Long.parseLong(categoryId);
            String sqlStr = "BEGIN ORDERS.SPI_VERIF_ULT_PREEVALUACION_2(?, ?, ?, ?, ?, ?, ?); END;";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

            cstmt.setLong(1,cusId);
            cstmt.setLong(2,catId);
            cstmt.setString(3,userLogin);
            cstmt.setString(4, cadenaNumeros);
            cstmt.setString(5, cadenaModalidad);            
            cstmt.registerOutParameter(6, OracleTypes.INTEGER);
            cstmt.registerOutParameter(7, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            strMessage = cstmt.getString(7);
            result = cstmt.getInt(6);
            System.out.println("[OrderDao][doValidateUltimaPreevaluacion]Mensaje de salida "+strMessage);
            System.out.println("[OrderDao][doValidateUltimaPreevaluacion]Resultado "+result);
        }catch(Exception e){
            strMessage = e.getMessage();
            System.out.println(strMessage);
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        return result+"|"+strMessage;
    }
    
    /**
     * Purpose: Generar un Incidente para ordenes de Renta Adelantada
     * Developer       Fecha       Comentario
     * =============   ==========  ======================================================================
     * MVERAE          22/02/2017  Creación 
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     * @return 
     * @param idOrden
     */
       public HashMap createIncidentRA (long an_npcustomerid, String av_login, Connection conn) throws Exception, SQLException{
    	  
 		  HashMap objHashMap = new HashMap();
	      OracleCallableStatement cstmt = null;
	      String strMessage=null;
	      String npincidentid = "0";
	  
		  try{
		
		    String strSql = "BEGIN INCIDENT_WEB.NP_INCIDENT37_PKG.SP_CREATE_INCIDENT_RA(?,?,?,?); END;";
		    cstmt = (OracleCallableStatement)conn.prepareCall(strSql);            
		    cstmt.setLong(1, an_npcustomerid);
		    cstmt.setString(2, av_login);
		    cstmt.registerOutParameter(3, Types.NUMERIC);
		    cstmt.registerOutParameter(4, Types.VARCHAR);
		    cstmt.executeUpdate();
		    
		    npincidentid = cstmt.getString(3);
		    strMessage = cstmt.getString(4);
		    
		  }catch(Exception e){
		   strMessage = e.getMessage();
		  }
		  finally{
		    try{
		       closeObjectsDatabase(null,cstmt,null); 
		    }catch (Exception e) {
		      logger.error(formatException(e));
		    }
		  }
		  
		  objHashMap.put("strMessage", strMessage);
		  objHashMap.put("npincidentid", npincidentid);
	      
	      return objHashMap;
                  
       }
       
     /**
   	 * Purpose: Obtiene el resultado de la pre evaluacion del cliente
   	 * Developer       Fecha       Comentario
   	 * =============   ==========  ======================================================================
   	 * MVERAE          22/02/2017  Creación 
   	 * @throws java.sql.SQLException
   	 * @throws java.lang.Exception
   	 * @return 
   	 * @param idOrden
   	 */
       public String getResultPreEvaluation(long npcustomerscoreid) throws SQLException, Exception{
          
          String resultPreEvaluation = null;
          Connection conn=null;
          OracleCallableStatement cstmt = null;
          try{
            String sqlStr =  " { ? = call CREDIT.NP_CUSTOMER_SCORE_PKG.FX_CREDEXTDATALT_CUSTSCOREID(?,?) } ";
            
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.registerOutParameter(1, Types.VARCHAR);   
            cstmt.setLong(2, npcustomerscoreid);
            cstmt.setString(3, "RESULT");
            
            cstmt.execute();
            resultPreEvaluation = cstmt.getString(1); 
          }catch(Exception e){
             logger.error(formatException(e));
             resultPreEvaluation = null;
          }finally{
            try{
              closeObjectsDatabase(conn,cstmt,null); 
            }catch (Exception e) {
              logger.error(formatException(e));
            }
         }      
          return resultPreEvaluation;
       }  

       /**
        * Motivo:  Crea la Orden de Renta Adelantada
        * <br>Realizado por: <a href="mailto:jhonny.quispe@tcs.com">Jhonny Quispe</a>
        * <br>Fecha: 22/03/2017
        * 
        * @param      OrderRentaAdelantadaBean orderRentaAdelantadaBean    
        * @param      Connection conn
        * @return     HashMap objHashMap 
       */
       public HashMap crearOrderRentaAdelantada(OrderRentaAdelantadaBean orderRentaAdelantadaBean, Connection conn) throws Exception,SQLException{
    	   HashMap objHashMap = new HashMap();
    	    String strMessage = null;          
    	    OracleCallableStatement cstmt = null;    	    
    	    
    	    try{  	    
    	        	    
    	        String strSql = "BEGIN ORDERS.NP_ORDERS05_PKG.SP_CREATE_ORDER_RA(?, ?, ?, ?); END;";
    	        
    	        cstmt = (OracleCallableStatement)conn.prepareCall(strSql); 
    	        
    	        cstmt.setLong(1, orderRentaAdelantadaBean.getNpOrderId());
    	        cstmt.setLong(2, orderRentaAdelantadaBean.getNpOrderRefRentaAdelantadaId());
    	        cstmt.setString(3, orderRentaAdelantadaBean.getNpCreatedBy());
    	        cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
    	        
    	        cstmt.executeUpdate();
    	        strMessage = cstmt.getString(4);
    	        
    	       objHashMap.put("strMessage",strMessage);
    	                    
    	    }catch(Exception e){
    	        e.printStackTrace();
    	        objHashMap.put("strMessage",e.getMessage());    	        
	        }finally{
	          try{
	           closeObjectsDatabase(null,cstmt,null); 
	          }catch (Exception e) {
	             logger.error(formatException(e));
	          }
	       }
    	    
    	    return objHashMap;
       }
       
       /**
        * Motivo:  Crear Inbox
        * <br>Realizado por: <a href="mailto:jhonny.quispe@tcs.com">Jhonny Quispe</a>
        * <br>Fecha: 22/03/2017
        * 
        * @param      long longOrderId
        * @param      HashMap hshInboxMap
        * @param      Connection conn
        * @return     HashMap objHashMap 
       */
       public HashMap crearInbox(long orderId, HashMap hshInboxMap, Connection conn) throws Exception,SQLException{
    	   HashMap objHashMap = new HashMap();
    	    String strMessage = null;          
    	    OracleCallableStatement cstmt = null;    	    
    	    
    	    try{  	    
    	        	    
    	        String strSql = "BEGIN BPEL_WORKFLOW.SPI_INS_INBOX(?, ?, ?, ?, ?, ?, ?, ?); END;";
    	        
    	        cstmt = (OracleCallableStatement)conn.prepareCall(strSql); 
    	            	        
    	        String strBpelConverstionId =  (String) hshInboxMap.get("strBpelConverstionId");
    	        BigDecimal bigDBpelInstanceId = (BigDecimal) hshInboxMap.get("bigDBpelInstanceId");
    	        String strBackInbox = (String) hshInboxMap.get("strBackInbox");
    	        String strInboxOld = (String) hshInboxMap.get("strInboxOld");
    	        String strInboxNew = (String) hshInboxMap.get("strInboxNew");
    	        String strLastUpdateBy = (String) hshInboxMap.get("strLastUpdateBy");
    	        
    	        cstmt.setLong(1, orderId);
    	        cstmt.setString(2, strBpelConverstionId);
    	        cstmt.setBigDecimal(3, bigDBpelInstanceId);
    	        cstmt.setString(4, strBackInbox);
    	        cstmt.setString(5, strInboxOld);
    	        cstmt.setString(6, strInboxNew);
    	        cstmt.setString(7, strLastUpdateBy);
    	        cstmt.registerOutParameter(8, OracleTypes.VARCHAR);
    	        
    	        cstmt.executeUpdate();
    	        strMessage = cstmt.getString(8);
    	        
    	       objHashMap.put("strMessage",strMessage);
    	                    
    	    }catch(Exception e){
    	        e.printStackTrace();
    	        objHashMap.put("strMessage",e.getMessage());    	        
	        }finally{
	          try{
	           closeObjectsDatabase(null,cstmt,null); 
	          }catch (Exception e) {
	             logger.error(formatException(e));
	          }
	       }
    	    
    	    return objHashMap;
       }

       
       /**
        * Motivo:  Obtiene la preevaluacion del cliente
        * <br>Realizado por: <a href="mailto:jhonny.quispe@tcs.com">Jhonny Quispe</a>
        * <br>Fecha: 06/04/2017
        * 
        * @param      String numeroDocumento    
        * @param      String tipoDocumento
        * @return     String tipoOperacion 
       */
       public HashMap getPreEvaluacionCliente(long lngCustomerId) throws Exception,SQLException{
    	   HashMap objHashMap = new HashMap();
    	    String strMessage = null;
    	    Connection conn=null;
    	    OracleCallableStatement cstmt = null;    	    
    	    
    	    try{  	    
    	        	    
    	        String strSql = "BEGIN ORDERS.NP_RENTA_ADELANTADA2_PKG.SP_GET_PREV_EVAL_CLIENTE(?, ?, ?); END;";
    	        String strResultado = null;
    	        
    	        conn = Proveedor.getConnection();
    	        cstmt = (OracleCallableStatement)conn.prepareCall(strSql); 
    	        
    	        cstmt.setLong(1, lngCustomerId);    	        
    	        cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
    	        cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
    	        
    	        cstmt.executeUpdate();
    	        strResultado = cstmt.getString(2);
    	        strMessage = cstmt.getString(3);
    	        
    	       objHashMap.put("strResultado",strResultado);
    	       objHashMap.put("strMessage",strMessage);
    	                    
    	    }catch(Exception e){
    	        e.printStackTrace();
    	        objHashMap.put("strMessage",e.getMessage());    	        
	        }finally{
	          try{
	        	  closeObjectsDatabase(conn,cstmt,null); 
	          }catch (Exception e) {
	             logger.error(formatException(e));
	          }
	       }
    	    
    	    return objHashMap;
       }

       /**
        * Motivo:  Obtiene la preevaluacion del cliente
        * <br>Realizado por: <a href="mailto:jhonny.quispe@tcs.com">Jhonny Quispe</a>
        * <br>Fecha: 06/04/2017
        * 
        * @param      OrderRentaAdelantadaBean orderRentaAdelantadaBean
       */
       public HashMap getOrdenRentaAdelantada(OrderRentaAdelantadaBean orderRentaAdelantadaBean) throws Exception,SQLException{
    	   HashMap objHashMap = new HashMap();
    	    String strMessage = null;    	    
    	    Connection conn=null;
    	    OracleCallableStatement cstmt = null;    	    
    	    
    	    try{  	    
    	        	    
    	        String strSql = "BEGIN ORDERS.NP_ORDERS05_PKG.SP_GET_ORDER_RA(?, ?, ?); END;";    	        
    	        
    	        conn = Proveedor.getConnection();
    	        cstmt = (OracleCallableStatement)conn.prepareCall(strSql); 
    	        
    	        cstmt.setLong(1, orderRentaAdelantadaBean.getNpOrderId());    	       
    	        cstmt.registerOutParameter(2, OracleTypes.NUMBER);
    	        cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
    	        
    	        cstmt.executeUpdate();
    	        
    	        orderRentaAdelantadaBean.setNpOrderRefRentaAdelantadaId(cstmt.getLong(2));
    	        strMessage = cstmt.getString(3);
    	       
    	       objHashMap.put("strMessage",strMessage);
    	                    
    	    }catch(Exception e){
    	        e.printStackTrace();
    	        objHashMap.put("strMessage",e.getMessage());    	        
	        }finally{
	          try{
	        	  closeObjectsDatabase(conn,cstmt,null); 
	          }catch (Exception e) {
	             logger.error(formatException(e));
	          }
	       }
    	    
    	    return objHashMap;
       }


    public String isAuthorizedToDevolution(long npOrderId) throws SQLException,Exception {

        System.out.println("+++++++++++Entrando al OrderDAO.isAuthorizedToDevolution+++++++++++");
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        Integer result = 0;
        String strUserMessage = "";
        String strMessage = null;
        try {
            String sqlStr = "BEGIN CASHDESK.SPI_GET_NUM_PAYMENT_ORD_TO_DEV(?, ?, ?, ?); END;";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

            cstmt.setLong(1, npOrderId);
            cstmt.registerOutParameter(2, OracleTypes.INTEGER);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            strMessage = cstmt.getString(4);

            System.out.println("strMessage=" + strMessage);

            if(StringUtils.isEmpty(strMessage)){
                result = cstmt.getInt(2);
                System.out.println("result antes=" + result);
                if(result > 0){
                    result = 1;
                    strUserMessage = cstmt.getString(3);
                }System.out.println("result después=" + result);
                System.out.println("strUserMessage=" + strUserMessage);
            } else {
                System.out.println("[OrderDao][isAuthorizedToDevolution]Error " + strMessage);
            }

            System.out.println("[OrderDao][isAuthorizedToDevolution]Resultado "+result);
            System.out.println("[OrderDao][isAuthorizedToDevolution]Mensaje al usuario " + strUserMessage);
        }catch(Exception e){
            strMessage = e.getMessage();
            System.out.println("[OrderDao][isAuthorizedToDevolution]Error " + strMessage);
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        return result+"|"+strUserMessage;
    }

  /**
   * @author AMENDEZ
   * @project PRY-0864
   * Metodo   Evalua el monto inicial en casos de ordenes vep
   * @return
   */
  public String validateOrderVepCI(long nporderid,int npvepquantityquota,double npinitialquota,int npspecificationid,long swcustomerid,double totalsalesprice,int npvep,String nptype,int nppaymenttermsiq) throws SQLException, Exception{
    logger.info("************************** INICIO OrderDAO > validateOrderVepCI**************************");
    String sReturnValue = null;
    Connection conn=null;
    OracleCallableStatement cstmt = null;
    try{
      logger.info("nporderid            : "+nporderid);
      logger.info("npvepquantityquota   : "+npvepquantityquota);
      logger.info("npinitialquota       : "+npinitialquota);
      logger.info("npspecificationid    : "+npspecificationid);
      logger.info("swcustomerid         : "+swcustomerid);
      logger.info("totalsalesprice      : "+totalsalesprice);
      logger.info("npvep                : "+npvep);
      logger.info("nptype               : "+nptype);
      //INICIO: PRY-0980 | AMENDEZ
      logger.info("nppaymenttermsiq     : "+nppaymenttermsiq);
      //FIN: PRY-0980 | AMENDEZ

      String sqlStr =  " { ? = call ORDERS.FXI_VALID_QUOTA_VEP( ?, ?, ? ,?, ?, ?, ?, ?, ?) } ";

      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.registerOutParameter(1, Types.VARCHAR);

      cstmt.setLong(2, nporderid);
      cstmt.setInt(3, npvepquantityquota);
      cstmt.setDouble(4,npinitialquota);
      cstmt.setInt(5, npspecificationid);
      cstmt.setLong(6, swcustomerid);
      cstmt.setDouble(7,totalsalesprice);
      cstmt.setInt(8,npvep);
      cstmt.setString(9,nptype);

      //INICIO: PRY-0980 | AMENDEZ
      cstmt.setInt(10,nppaymenttermsiq);
      //FIN: PRY-0980 | AMENDEZ


      cstmt.executeQuery();
      sReturnValue = cstmt.getString(1);
    }catch(Exception e){
      logger.error(formatException(e));
      sReturnValue = e.getMessage();
    }finally{
      try{
        closeObjectsDatabase(conn,cstmt,null);
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }
    logger.info("************************** FIN OrderDAO > validateOrderVepCI**************************");
    return sReturnValue;
  }
  
  /**
     * Purpose: Generar un Incidente para ordenes de Prorrateo
     * Developer       Fecha       Comentario
     * =============   ==========  ======================================================================
     * JCURI          05/07/2017  Creación 
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     * @return 
     * @param idOrden
     */
       public HashMap createIncidentPRORRA (long an_npcustomerid, String av_login, Connection conn) throws Exception, SQLException{
    	   System.out.println("OrderDAO/createIncidentPRORRA [an_npcustomerid] " + an_npcustomerid + " [av_login] " + av_login);
 		  HashMap objHashMap = new HashMap();
	      OracleCallableStatement cstmt = null;
	      String strMessage=null;
	      String npincidentid = "0";
	  
		  try{
		
		    String strSql = "BEGIN INCIDENT_WEB.NP_INCIDENT37_PKG.SP_CREATE_INCIDENT_PRORRA(?,?,?,?); END;";
		    cstmt = (OracleCallableStatement)conn.prepareCall(strSql);            
		    cstmt.setLong(1, an_npcustomerid);
		    cstmt.setString(2, av_login);
		    cstmt.registerOutParameter(3, Types.NUMERIC);
		    cstmt.registerOutParameter(4, Types.VARCHAR);
		    cstmt.executeUpdate();
		    
		    npincidentid = cstmt.getString(3);
		    strMessage = cstmt.getString(4);
		    
		  }catch(Exception e){
		   strMessage = e.getMessage();
		  }
		  finally{
		    try{
		       closeObjectsDatabase(null,cstmt,null); 
		    }catch (Exception e) {
		      logger.error(formatException(e));
		    }
		  }
		  
		  objHashMap.put("strMessage", strMessage);
		  objHashMap.put("npincidentid", npincidentid);
	      
	      return objHashMap;
                  
       }

       
       /**
        * Purpose: Actualizacion de order,item de prorrateo
        * Developer       Fecha       Comentario
        * =============   ==========  ======================================================================
        * JCURI          05/07/2017  Creación 
        * @throws java.sql.SQLException
        * @throws java.lang.Exception
        * @return 
        * @param strMessage
        */
       public String updateCreateOrderProrratero (long orderparentId, long orderId, String prorrateo, long itemId, BigDecimal amountTotal, String login, String status, int indicator, Connection conn) throws Exception, SQLException{
     	  System.out.println("OrderDAO/updateCreateOrderProrratero [orderparentId] "+orderparentId+" [orderId] "+orderId+" [prorrateo] "+prorrateo +" [itemId] "+itemId+" [amountTotal] "+ amountTotal +" [status] "+status + " [indicator] "+indicator);
	   	      OracleCallableStatement cstmt = null;
	   	      String strMessage=null;
	   	  
	   		  try{
	   		
	   		    String strSql = "BEGIN ORDERS.NP_ORDERS07_PKG.SP_UPD_CREATE_ORDER_PRORRA(?,?,?,?,?,?,?,?,?); END;";
	   		    cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
	   		    cstmt.setLong(1, orderparentId);
	   		    cstmt.setLong(2, orderId);
	   		    cstmt.setString(3, prorrateo);
	   		    cstmt.setLong(4, itemId);
	   		    cstmt.setBigDecimal(5, amountTotal);
	   		    cstmt.setString(6, login);
	   		    cstmt.setString(7, status);
	   		    cstmt.setInt(8, indicator);
	   		    cstmt.registerOutParameter(9, Types.VARCHAR);
	   		    
	   		    cstmt.executeUpdate();	   		    
	   		    strMessage = cstmt.getString(9);
	   		    System.out.println("[OrderDAO] strMessage : " + strMessage);
	   		  }catch(Exception e){
	   		   strMessage = e.getMessage();
	   		   System.out.println("[ERROR OrderDAO] " + e.getMessage());
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
        * Purpose: Cliente Post-Pago
        * Developer       Fecha       Comentario
        * =============   ==========  ======================================================================
        * JCURI          16/07/2017  Creación 
        * @throws java.sql.SQLException
        * @throws java.lang.Exception
        * @return 
        * @param strMessage
        */
          public String insertClientPostPago (long orderId, String isPostPago, String description, String login,  Connection conn) throws Exception, SQLException{
     	  System.out.println("OrderDAO/insertClientPostPago [orderId] " + orderId + " [isPostPago] " + isPostPago + " [description] " + description +" [login] "+login);
	   	      OracleCallableStatement cstmt = null;
	   	      String strMessage=null;
	   	  
		   	   	try {
					String sqlStr = "BEGIN ORDERS.SPI_INS_ORDER_DATA(?,?,?,?,?); END;";					
					cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
					cstmt.setLong(1, orderId);
					cstmt.setString(2, isPostPago);
					cstmt.setString(3, description);
					cstmt.setString(4, login);
					cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
					cstmt.executeUpdate();
					strMessage = cstmt.getString(5);
				} catch (Exception e) {
					logger.error(formatException(e));
					throw new Exception(e);
				} finally {
					try {
						closeObjectsDatabase(null,cstmt,null);
					} catch (Exception e) {
						logger.error(formatException(e));
					}
				}
				return strMessage;
          }
          
          /**
           * Purpose: cliente postpago
           * Developer       Fecha       Comentario
           * =============   ==========  ======================================================================
           * JCURI          16/07/2017  Creación 
           * @throws java.sql.SQLException
           * @throws java.lang.Exception
           * @return 
           * @param strMessage
           */
           public HashMap getClientPostPago(long orderid) throws Exception, SQLException {
         	  	ArrayList list = new ArrayList();
                 HashMap hshRetorno=new HashMap();
                 HashMap hshdata= null;
                 String strMessage=null;
                 OracleCallableStatement cstmt = null;     
                 Connection conn=null;
                 ResultSet rs = null;
                 conn = Proveedor.getConnection();
                 
                 try{
                   
                   String strSql = "BEGIN ORDERS.NP_ORDER_PRORRATEO_PKG.SP_GET_ORDER_DATA(?,?,?); END;";
           		  
                   cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
                   cstmt.setLong(1, orderid);
                   cstmt.registerOutParameter(2, Types.CHAR);
                   cstmt.registerOutParameter(3, OracleTypes.CURSOR);
                   cstmt.execute();
                   strMessage = cstmt.getString(2);
                   if (strMessage == null){
                      rs = (ResultSet)cstmt.getObject(3);
                      if (rs.next()) {     
                     	 hshdata = new HashMap();
                     	 hshdata.put("wv_orderId", rs.getLong(1));
                     	 hshdata.put("wv_postPago", rs.getString(2));
                     	 hshdata.put("wv_description", rs.getString(3));
                         
                     	 list.add(hshdata);
                      }
                   }                   
                   hshRetorno.put("objResume",list);
                   hshRetorno.put("strMessage",strMessage);        
                 }catch(Exception e){
                    hshRetorno.put("strMessage",e.getMessage()); 
                 }finally{
                   try{
                     closeObjectsDatabase(conn,cstmt,rs); 
                     }catch (Exception e) {
                       logger.error(formatException(e));
                     }
                 }   
                 return hshRetorno;
              }

           /**
            * Purpose: Obtener datos 
            * Developer       Fecha       Comentario
            * =============   ==========  ======================================================================
            * JCURI          16/07/2017  Creación 
            * @throws java.sql.SQLException
            * @throws java.lang.Exception
            * @return 
            * @param strMessage
            */
            public HashMap getApiProrra(long itemid) throws Exception, SQLException {
            	HashMap objHashMapResultado = new HashMap();
            	Connection conn = null;
                ResultSet rs=null;
                OracleCallableStatement cstm = null;
                OracleCallableStatement cstm1= null;
                String strMessage = null;
                ArrayList list  = null;
                
            	 try{
            	      String strSql = "BEGIN ORDERS.NP_ORDER_PRORRATEO_PKG.SP_GET_ITEM_API_PRORRA(?,?,?); END;";
            	      conn = Proveedor.getConnection();
            	      cstm = (OracleCallableStatement) conn.prepareCall(strSql);

            	      cstm.setLong(1,itemid);
            	      cstm.registerOutParameter(2, OracleTypes.CURSOR);
            	      cstm.registerOutParameter(3, Types.CHAR);

            	      cstm.execute();
            	      strMessage  = cstm.getString(3);

            	      if( strMessage == null){
            	        rs = (ResultSet)cstm.getCursor(2);
            	        list = new ArrayList();

            	        while(rs.next()){
            	        	ApportionmentBean apportionment = new ApportionmentBean();
                   			apportionment.setTrxId(rs.getString("nptrxid"));
                   			apportionment.setItemId(rs.getLong("npitemid"));
                   			apportionment.setTemplateId(rs.getString("nptemplateid"));
                   			apportionment.setCicloOrigen(rs.getString("npbillcycle_origen"));
                   			apportionment.setCicloDestino(rs.getString("npbillcycle_destino"));
                   			apportionment.setPrice(rs.getString("nppriceapi"));                			
                   			apportionment.setPriceIgv(rs.getString("nppriceigv"));
                   			apportionment.setRoundedPrice(rs.getString("nppricetotal"));
                   			apportionment.setIgv(rs.getDouble("npigv"));
                   			apportionment.setQuantity(rs.getInt("npquantity"));
                   			list.add(apportionment);
            	        }
            	      }
            	      objHashMapResultado.put("strMessage",strMessage);
            	      objHashMapResultado.put("objArrayList",list);
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
            
            /**
             * Purpose: Obtener datos 
             * Developer       Fecha       Comentario
             * =============   ==========  ======================================================================
             * JCURI          16/07/2017  Creación 
             * @throws java.sql.SQLException
             * @throws java.lang.Exception
             * @return 
             * @param strMessage
             */
             public HashMap validarPortabilidad(long orderid) throws Exception, SQLException {
            	System.out.println("[INI] OrderDAO/validarPortabilidad [orderid] " + orderid);
             	HashMap objHashMapResultado = new HashMap();
             	Connection conn = null;
                 ResultSet rs=null;
                 OracleCallableStatement cstm = null;
                 OracleCallableStatement cstm1= null;
                 String strMessage = null;
                 int aplica=0;
                 int cantItem=0;
                 
             	 try{
             	      String strSql = "BEGIN ORDERS.NP_ORDER_PRORRATEO_PKG.SP_VALIDAR_PORTABILIDAD(?,?,?,?); END;";
             	      conn = Proveedor.getConnection();
             	      cstm = (OracleCallableStatement) conn.prepareCall(strSql);

             	      cstm.setLong(1,orderid);
             	      cstm.registerOutParameter(2, Types.NUMERIC);
             	      cstm.registerOutParameter(3, Types.NUMERIC);
             	      cstm.registerOutParameter(4, Types.CHAR);

             	      cstm.execute();
             	      aplica  = cstm.getInt(2);
             	     cantItem  = cstm.getInt(3);
             	      strMessage  = cstm.getString(4);
             	      
             	      objHashMapResultado.put("strMessage",strMessage);
             	      objHashMapResultado.put("aplicaPortabilidad",aplica);
             	     objHashMapResultado.put("cantItems",cantItem);
             	     System.out.println("OrderDAO/validarPortabilidad [strMessage] " + strMessage + " [aplicaPortabilidad] " + aplica + " [cantItems] " + cantItem);
             	 }catch (Exception e) {
             	      logger.error(formatException(e));
             	      objHashMapResultado.put("strMessage",e.getMessage());
             	     System.out.println("OrderDAO/validarPortabilidad [ERROR] " + e.getMessage());
             	    }finally{
             	      try{
             	        closeObjectsDatabase(null, cstm1, null);
             	        closeObjectsDatabase(conn,cstm,rs);
             	      }catch (Exception e) {
             	        logger.error(formatException(e));
             	      }
             	    }
             	System.out.println("[FIN] OrderDAO/validarPortabilidad [orderid] " + orderid);
             	return objHashMapResultado;
             }


  /**
   * @author AMENDEZ
   * @project PRY-0980
   * Metodo   Retorna valor para colocar el check por defecto de forma de pago cuota inicial
   * @return
   */
  public int validatePaymentTermsCI(long swcustomerid,long userid,int npvep) throws SQLException, Exception{
    logger.info("************************** INICIO OrderDAO > validatePaymentTermsCI**************************");
    int sReturnValue = 0;
    Connection conn=null;
    OracleCallableStatement cstmt = null;
    try{
      logger.info("swcustomerid         : "+swcustomerid);
      logger.info("userid               : "+userid);
      logger.info("npvep                : "+npvep);

      String sqlStr =  " { ? = call ORDERS.FXI_VALID_PAYMENTTERMSIQ_VEP( ?, ?, ? ) } ";

      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.registerOutParameter(1, Types.INTEGER);

      cstmt.setLong(2, swcustomerid);
      cstmt.setLong(3, userid);
      cstmt.setInt(4,npvep);

      cstmt.executeQuery();
      sReturnValue = cstmt.getInt(1);
    }catch(Exception e){
      logger.error(formatException(e));
    }finally{
      try{
        closeObjectsDatabase(conn,cstmt,null);
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }
    logger.info("************************** FIN OrderDAO > validatePaymentTermsCI**************************");
    return sReturnValue;
  }

  /**
   * @author CMONETEROS
   * @project PRY-1062
   * Metodo   Valida la preevaluacion para Cambio de Modelo.
   * @return
   */
  public HashMap doValidatePreevaluationCDM(long customerId)
          throws Exception, SQLException
  {
    logger.info("******************* INI SEJBOrderNewBean > doValidatePreevaluationCDM  *******************");
    HashMap objHashMap = new HashMap();
    OracleCallableStatement cstmt = null;
    Connection conn = null;
    String strMessage = null;

    logger.info("customerId : " + customerId);

    try
    {
      String sqlStr = "BEGIN CREDIT.SPI_EM_EVAL_PREEVALUATION_CMD(?,?); END;";

      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      logger.info("OrderDAO/doValidatePreevaluationCDM [customerId]: " + customerId);
      cstmt.setLong(1, customerId);

      cstmt.registerOutParameter(2, Types.VARCHAR);

      cstmt.executeUpdate();

      strMessage = cstmt.getString(2);
      logger.info("strMessage : " + strMessage);
      objHashMap.put("strMessage", strMessage);

    }
    catch (Exception e)
    {
      objHashMap.put("strMessage", "Error en OrderDAO/doValidatePreevaluationCDM " + e.getMessage());
      logger.error("Exception : " + e.getMessage());
      throw e;
    }
    finally
    {
      try
      {
        closeObjectsDatabase(conn, cstmt, null);
      }
      catch (Exception e)
      {
        logger.error(formatException(e));
      }
      logger.info("******************* FIN SEJBOrderNewBean > doValidatePreevaluationCDM  *******************");
    }
    return objHashMap;
  }

/**
   * Purpose: inserta datos del ciclo y documento
   * Developer       Fecha       Comentario
   * =============   ==========  ======================================================================
   * JCURI          16/01/2018  Creacion (PRY-1002)
   * @throws java.sql.SQLException
   * @throws java.lang.Exception
   * @return 
   * @param strMessage
   */
  public String insertNoProrrateo (long orderId, String trxid, String tipoDocumento, String nroDocumento, String cicloAnterior, String cicloNuevo, int estadoSW, String login, Connection conn) throws Exception, SQLException{
	  System.out.println("OrderDAO/insertNoProrrateo [orderId] "+orderId+" [trxid] "+trxid +" [tipoDocumento] "+tipoDocumento+" [nroDocumento] "+ nroDocumento +" [cicloAnterior] "+cicloAnterior + " [cicloNuevo] "+cicloNuevo + " [estadoSW]" + estadoSW + " [login] "+login);
  	      OracleCallableStatement cstmt = null;
  	      String strMessage=null;
  	  
  		  try{
  
  		    String strSql = "BEGIN ORDERS.NP_ORDER_PRORRATEO_PKG.SP_EM_AC_INS_NO_PRORRATEO(?,?,?,?,?,?,?,?,?); END;";
  		    cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
  		    cstmt.setLong(1, orderId);
  		    cstmt.setString(2, trxid);
  		    cstmt.setString(3, tipoDocumento);
  		    cstmt.setString(4, nroDocumento);
  		    cstmt.setString(5, cicloAnterior);
  		    cstmt.setString(6, cicloNuevo);
  		    cstmt.setInt(7, estadoSW);
  		    cstmt.setString(8, login);  		
  		    cstmt.registerOutParameter(9, Types.VARCHAR);
  		    
  		    cstmt.executeUpdate();	   		    
  		    strMessage = cstmt.getString(9);
  		    System.out.println("[OrderDAO] strMessage : " + strMessage);
  		  }catch(Exception e){
  		   strMessage = e.getMessage();
  		   System.out.println("[ERROR OrderDAO] " + e.getMessage());
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
   * Purpose: valida orden no prorrateo
   * Developer       Fecha       Comentario
   * =============   ==========  ======================================================================
   * JCURI          16/01/2018  Creacion (PRY-1002)
   * @throws java.sql.SQLException
   * @throws java.lang.Exception
   * @return HashMap
   * @param strMessage
   */
  public HashMap validarExisteNoProrrateo (long orderId) throws Exception, SQLException{
	  System.out.println("OrderDAO/validarExisteNoProrrateo [orderId] "+orderId);
	  HashMap objHashMap = new HashMap();
	    OracleCallableStatement cstmt = null;
	    Connection conn=null; 
	    String strMessage = null;
	    int status = 1;
	    try{
	      String sqlStr =  "BEGIN ORDERS.NP_ORDER_PRORRATEO_PKG.SP_EM_AC_VALIDAEXIST_NOPRORRA(?,?,?); END;";
	      conn = Proveedor.getConnection();
	      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
	      
	      cstmt.setLong(1, orderId);
	      cstmt.registerOutParameter(2, OracleTypes.NUMBER);
	      cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
	      
	      cstmt.executeUpdate();
	  
	      strMessage = cstmt.getString(3);
	      if( strMessage==null){
	    	  status = cstmt.getInt(2);
	      }
 
	      objHashMap.put("strMessage",strMessage);
	      objHashMap.put("statusExists",status); 
	      System.out.println("OrderDAO/validarExisteNoProrrateo [strMessage] : " + strMessage);
	    }catch(Exception e){
	      objHashMap.put("strMessage",e.getMessage()); 
	      System.out.println("OrderDAO/validarExisteNoProrrateo [ERROR] : " + e.getMessage());
	    }finally{
	       try{
	        closeObjectsDatabase(conn,cstmt,null); 
	       }catch (Exception e) {
	          logger.error(formatException(e));
	       }
	     }    
	 
	    return objHashMap;                   
  }  
  
      /**
     * Purpose: Validar que exista ultima pre evaluacion de prefactibilidad de cliente vigente
     * Developer       Fecha       Comentario
     * =============   ==========  ======================================================================
     * DOLANO          22/03/2018  Creacion
     * DOLANO-0002     10/05/2018  Se agrega el parametro de salida HOME_SERVICE_ZONE (PRY-1049 / EST-1098)
     * DOLANO-0003     29/05/2018  Se agrega el parametro de salida descripcion de HOME_SERVICE_ZONE (PRY-1140)
     */
    public HashMap validateLastCustomerPreEvaluation(Long customerId,Long buildingid) throws Exception, SQLException{

        logger.info("***************** INICIO SEJBOrderEditBean > validateLastCustomerPreEvaluation*****************");
        logger.info("customerId: "+customerId);
        logger.info("buildingid: "+buildingid);
        HashMap objHashMap = new HashMap();
        OracleCallableStatement cstmt = null;
        Connection conn=null;
        int an_flagcobertura=-12;
        int an_behavior=-1000;
        try{
            String sqlStr =  "BEGIN ORDERS.PKG_SC_ORDERS_BAFI2300.SP_SC_CA_VALID_LAST_EVAL_LOC(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?" +
                    // BEGIN: PRY-1049 | DOLANO-0002
                    ",?" +
                    // END: PRY-1049 | DOLANO-0002
                    // BEGIN: PRY-1140 | DOLANO-0003
                    ",?" +
                    // END: PRY-1140| DOLANO-0003 
                    ",?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.setLong(1, customerId);
            cstmt.setLong(2,buildingid);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(6, OracleTypes.VARCHAR);

            cstmt.registerOutParameter(7, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(8, OracleTypes.NUMBER);
            cstmt.registerOutParameter(9, OracleTypes.NUMBER);
            cstmt.registerOutParameter(10, OracleTypes.NUMBER);
            cstmt.registerOutParameter(11, OracleTypes.NUMBER);
            cstmt.registerOutParameter(12, OracleTypes.NUMBER);

            // BEGIN: PRY-1049 | DOLANO-0002
            cstmt.registerOutParameter(13, OracleTypes.VARCHAR);
            // END: PRY-1049 | DOLANO-0002

            // BEGIN: PRY-1140 | DOLANO-0003
            cstmt.registerOutParameter(14, OracleTypes.VARCHAR);
            // END: PRY-1140 | DOLANO-0003

            cstmt.registerOutParameter(15, OracleTypes.VARCHAR);
            cstmt.executeUpdate();

            String av_useaddress     = cstmt.getString(3);
            String av_department     = cstmt.getString(4);
            String av_province       = cstmt.getString(5);
            String av_district       = cstmt.getString(6);
            String av_addresstype    = cstmt.getString(7);
            String av_usefulladdress = cstmt.getString(3);
            int an_zonacobid         = cstmt.getInt(8);
            int an_zonacobprov       = cstmt.getInt(9);
            int an_zonacobdist       = cstmt.getInt(10);
            an_flagcobertura         = cstmt.getInt(11);
            an_behavior          = cstmt.getInt(12);

            // BEGIN: PRY-1049 | DOLANO-0002
            String av_homeServiceZone = cstmt.getString(13);
            // END: PRY-1049 | DOLANO-0002

            // BEGIN: PRY-1140 | DOLANO-0003
            String av_homeServiceZoneDesc   = cstmt.getString(14);
            // END: PRY-1140 | DOLANO-0003

            String message           = cstmt.getString(15);

            logger.info("av_useaddress      : "+av_useaddress);
            logger.info("av_department      : "+av_department);
            logger.info("av_province        : "+av_province);
            logger.info("av_district        : "+av_district);
            logger.info("av_addresstype     : "+av_addresstype);
            logger.info("av_usefulladdress  : "+av_usefulladdress);
            logger.info("an_zonacobid       : "+an_zonacobid);
            logger.info("an_zonacobprov     : "+an_zonacobprov);
            logger.info("an_zonacobdist     : "+an_zonacobdist);
            logger.info("an_flagcobertura   : "+an_flagcobertura);
            logger.info("an_behavior        : "+an_behavior);

            // BEGIN: PRY-1049 | DOLANO-0002
            logger.info("av_homeServiceZone : "+av_homeServiceZone);
            // END: PRY-1049 | DOLANO-0002

            // BEGIN: PRY-1140 | DOLANO-0003
            logger.info("av_homeServiceZoneDesc : "+av_homeServiceZoneDesc);
            // END: PRY-1140 | DOLANO-0003

            logger.info("message            : "+message);

            if(message!=null || an_behavior==3 ){
              throw new Exception(message);
            }

            objHashMap.put("useAddress", av_useaddress);
            objHashMap.put("department", av_department);
            objHashMap.put("province", av_province);
            objHashMap.put("district", av_district);
            objHashMap.put("addresstype", av_addresstype);
            objHashMap.put("usefulladdress", av_usefulladdress);
            objHashMap.put("zonacobid", an_zonacobid);
            objHashMap.put("zonacobprov", an_zonacobprov);
            objHashMap.put("zonacobdist", an_zonacobdist);
            objHashMap.put("flagcobertura", an_flagcobertura);
            objHashMap.put("behavior", an_behavior);

            // BEGIN: PRY-1049 | DOLANO-0002
            objHashMap.put("homeServiceZone", av_homeServiceZone);
            // END: PRY-1049 | DOLANO-0002

            // BEGIN: PRY-1140 | DOLANO-0003
            objHashMap.put("homeServiceZoneDesc", av_homeServiceZoneDesc);
            // END: PRY-1140 | DOLANO-0003

            objHashMap.put("message", message);

        }catch(Exception e){
            objHashMap.put("message",e.getMessage());
          objHashMap.put("useAddress", "");
          objHashMap.put("department", "");
          objHashMap.put("province", "");
          objHashMap.put("district", "");
          objHashMap.put("addresstype", "");
          objHashMap.put("usefulladdress", "");
          objHashMap.put("zonacobid", "");
          objHashMap.put("zonacobprov", "");
          objHashMap.put("zonacobdist", "");
          objHashMap.put("behavior", an_behavior);
          objHashMap.put("flagcobertura", an_flagcobertura);

            // BEGIN: PRY-1049 | DOLANO-0002
            objHashMap.put("homeServiceZone", "");
            // END: PRY-1049 | DOLANO-0002

            // BEGIN: PRY-1140 | DOLANO-0003
            objHashMap.put("homeServiceZoneDesc", "");
            // END: PRY-1140 | DOLANO-0003

            logger.error("OrderDAO/validateLastCustomerPreEvaluation [ERROR] : " , e);
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        logger.info("***************** FIN SEJBOrderEditBean > validateLastCustomerPreEvaluation*****************");
        return objHashMap;
    }

  /**
   * Purpose: Valida que exista una configuracion para bafi 2300 segun modalidad, solucion y linea producto
   * Developer       Fecha       Comentario
   * =============   ==========  ======================================================================
   * AMENDEZ         22/03/2018  [PRY-1049]Creacion 
   */   
  public int validateConfigBafi2300(String av_npmodality,String av_npsolutionid,String av_npproductlineid) throws SQLException, Exception{
    logger.info("************************** INICIO OrderDAO > validateConfigBafi2300**************************");
    int sReturnValue = 0;
    Connection conn=null;
    OracleCallableStatement cstmt = null;
    try{

      logger.info("av_npmodality         : "+av_npmodality);
      logger.info("av_npsolutionid       : "+av_npsolutionid);
      logger.info("av_npproductlineid    : "+av_npproductlineid);

      String sqlStr =  " { ? = call  ORDERS.PKG_SC_ORDERS_BAFI2300.FNC_GET_CFG_BAFI2300_PRODPLN( ?, ?, ? ) } ";

      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.registerOutParameter(1, Types.INTEGER);

      cstmt.setString(2, av_npmodality);
      cstmt.setString(3, av_npsolutionid);
      cstmt.setString(4,av_npproductlineid);

      cstmt.executeQuery();
      sReturnValue = cstmt.getInt(1);
    }catch(Exception e){
      logger.error(formatException(e));
    }finally{
      try{
        closeObjectsDatabase(conn,cstmt,null);
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }
    logger.info("************************** FIN OrderDAO > validateConfigBafi2300**************************");
    return sReturnValue;
  }
  /**
   * @author CMONETEROS
   * @project PRY-1062
   * Metodo   Valida que el numero de telefono no esté asociado a una cuota VEP
   * @return
   */
  public HashMap doValidateVEPItem(long customerId, String strPhoneNumber)
          throws Exception, SQLException
  {
    logger.info("******************* INI SEJBOrderNewBean > doValidateVEPItem  *******************");
    HashMap objHashMap = new HashMap();
    OracleCallableStatement cstmt = null;
    Connection conn = null;
    String strMessage = null;

    logger.info("customerId : " + customerId);
    logger.info("strPhoneNumber : " + strPhoneNumber);
    try
    {
      String sqlStr = "BEGIN CREDIT.SPI_EM_VALIDATE_VEP_ITEM(?,?,?); END;";

      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

      cstmt.setLong(1, customerId);
      cstmt.setString(2, strPhoneNumber);
      cstmt.registerOutParameter(3, Types.VARCHAR);

      cstmt.executeUpdate();

      strMessage = cstmt.getString(3);

      objHashMap.put("strMessage", strMessage);

    }
    catch (Exception e)
    {
      objHashMap.put("strMessage", e.getMessage());
      logger.info("Exception : "+ e.getMessage());
    }
    finally
    {
      try
      {
        closeObjectsDatabase(conn, cstmt, null);
      }
      catch (Exception e)
      {
        logger.error(formatException(e));
      }
      logger.info("******************* FIN SEJBOrderNewBean > doValidateVEPItem  *******************");
    }
    return objHashMap;
  }

 /**
   * Purpose: valida activacion de boton generar documento
   * Developer       Fecha       Comentario
   * =============   ==========  ======================================================================
   * CDIAZ          09/04/2018  Creación 
   * @project PRY-1081
   * @throws java.sql.SQLException
   * @throws java.lang.Exception
   * @return 
   * @param strMessage
   */
   public HashMap validarGenerarDocumento(long lOrderId, int iSpecificationId, String strInbox, int iUserId,int iAppId) throws Exception, SQLException {
		logger.info("[INI] OrderDAO/validarGenerarDocumento [lOrdeId] " + lOrderId + " [iSpecificationId] " + iSpecificationId + " [strInbox] " + strInbox + " [iUserId] " + iUserId + " [iAppId] " + iAppId);
		HashMap objHashMapResultado = new HashMap();
		Connection conn = null;
		ResultSet rs=null;
		OracleCallableStatement cstm = null;
		String strMessage = null;
		String strAction = null;
       
		try{
			String strSql = "BEGIN ORDERS.NP_ORDERS05_PKG.SP_DISABLE_GENERATE_DOCUMENT(?,?,?,?,?,?); END;";
			conn = Proveedor.getConnection();
			cstm = (OracleCallableStatement) conn.prepareCall(strSql);

			cstm.setInt(1,iSpecificationId);
			cstm.setString(2,strInbox);
			cstm.setInt(3,iUserId);
			cstm.setInt(4,iAppId);
			cstm.registerOutParameter(5, OracleTypes.NUMBER);
			cstm.registerOutParameter(6, OracleTypes.VARCHAR);

			cstm.execute();
			strAction  = cstm.getString(5);
			strMessage  = cstm.getString(6);
   	      
			objHashMapResultado.put("strAction",strAction);
			objHashMapResultado.put("strMessage",strMessage);

			logger.info("OrderDAO/validarGenerarDocumento [strMessage] " + strMessage + " [strAction] " + strAction);
		}catch (Exception e) {
			logger.error(formatException(e));
			objHashMapResultado.put("strMessage",e.getMessage());
			System.out.println("OrderDAO/validarGenerarDocumento [ERROR] " + e.getMessage());
   	    }finally{
			try{
				closeObjectsDatabase(conn,cstm,rs);
			}catch (Exception e) {
				logger.error(formatException(e));
			}
		}
		logger.info("[FIN] OrderDAO/validarGenerarDocumento [lOrderId] " + lOrderId);
   	return objHashMapResultado;
   }
    /** AYATACO - Inicio */
    /**
     * MÃ©todo que devuelve la cantidad de Ã³rdenes.
     * @param npsource, tipo String.
     * @param npsourceid nÃºmero de orden, tipo int.
     * @return Cantidad de ordenes, tipo HashMap.
     * @throws SQlExceptiom excepciÃ³n generada en caso de error.
     * @throws Exception exception generada en caso de error.
     */
    public HashMap validateOrderExist(String npsource, int npsourceid) throws SQLException, Exception {
        logger.info("************************** INICIO OrderDAO > validateOrderExist **************************");
        HashMap objHashMapResultado = new HashMap();
        int itemsCount = 0;
        String strMessage = null;
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        try{
            logger.info("npsource -----> " + npsource);
            logger.info("npsourceid -----> " + npsourceid);

            String sqlStr =  "BEGIN ORDERS.SPI_COUNT_PAYM_ORD_CANCELADO(?, ?, ?, ?); END;";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.setString(1, npsource);
            cstmt.setInt(2, npsourceid);
            cstmt.registerOutParameter(3, OracleTypes.NUMBER);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);

            cstmt.executeQuery();
            strMessage = cstmt.getString(4);

            if(strMessage==null){
                itemsCount = cstmt.getInt(3);
            }

            objHashMapResultado.put("itemsCount", itemsCount);
            objHashMapResultado.put("strMessage", strMessage);

        }catch(Exception e){
            logger.error(formatException(e));
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        logger.info("************************** FIN OrderDAO > validateOrderExist **************************");
        return objHashMapResultado;
    }
    /** AYATACO - Fin */

  /**
   * @author AMENDEZ
   * @project PRY-1141
   * Metodo   Lista configuraciones necesarias para la orden de pago de cuota adelantada
   * @return
   */
  public HashMap lstPaymentType(){
    logger.info("********************** INICIO InstallmentSalesSearchDAOImpl > lstPaymentType**********************");
    Connection conn = null;


    List lista = new ArrayList();
    ResultSet rs = null;
    HashMap hshResultado  = new HashMap();
    OracleCallableStatement cstmt = null;
    int pos=0;

    try{

        String sqlStr = "BEGIN ORDERS.NP_TE_PAYMENT_DOCUMENT_PKG.SP_EM_AC_GET_CONF_PAYMENTTYPE(?, ?); END;";
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

        cstmt.registerOutParameter(1,OracleTypes.CURSOR);
        cstmt.registerOutParameter(2,OracleTypes.VARCHAR);

      int rowCount = cstmt.executeUpdate();

      rs =cstmt.getCursor(1);
      String strCodError = cstmt.getString(2);

      logger.info("rowCount : "+rowCount);

      if (rowCount == 0){
        hshResultado.put("arrayList",null);
        hshResultado.put("numRows","0");
        hshResultado.put("strMessage","Error no data found ");
        logger.debug("Error no data found ");
        return hshResultado;
      }

      if (strCodError!=null && !strCodError.equalsIgnoreCase("")){
        hshResultado.put("arrayList",null);
        hshResultado.put("numRows","0");
        hshResultado.put("strMessage","Error desde Procedure "+strCodError);
        logger.info("Error desde Procedure "+strCodError);
        return hshResultado;
      }

      int intPos = 1;
      while (rs.next()) {
        HashMap h = new HashMap();
        h.put("NPPAYMENTTYPEID",rs.getString(1)+"");
        h.put("NPPAYMENTTYPENAME",rs.getString(2)+"");
        lista.add(h);
      }

      hshResultado.put("arrayList",lista);
      hshResultado.put("numRows",Integer.valueOf(intPos+""));
      hshResultado.put("strMessage",strCodError);

      logger.info("intPos : "+intPos);

      return hshResultado;

    }catch (SQLException e){
      hshResultado.put("arrayList",null);
      hshResultado.put("numRows","0");
      hshResultado.put("strMessage",e.getMessage());
      logger.error(e);
    } catch (Exception e){
      hshResultado.put("arrayList",null);
      hshResultado.put("numRows","0");
      hshResultado.put("strMessage",e.getMessage());
      logger.error(e);
    }finally {
        try {
            conn.commit();
            closeObjectsDatabase(conn, cstmt, rs);
        } catch (Exception e) {
            logger.error(formatException(e));
        }
    }
    logger.info("********************** FIN InstallmentSalesSearchDAOImpl > lstPaymentType**********************");
    return hshResultado;
  }
  /**
    * Purpose: Obtiene lista de transportistas
    * Developer       Fecha       Comentario
    * =============   ==========  ======================================================================
    * JCURI          09/04/2018  Creación 
    * @project PRY-1093
    * @throws java.sql.SQLException
    * @throws java.lang.Exception
    * @return 
    * @param strMessage
    */
   public HashMap getCarrierPlaceOfficeList(int intParamDispatch, String strParamName, String strParamStatus)
			throws Exception {
	   logger.info("[INI] OrderDAO/getCarrierPlaceOfficeList [intParamDispatch] " + intParamDispatch + " [strParamName] " + strParamName + " [strParamStatus] " + strParamStatus);
		
		ArrayList list = new ArrayList();
		HashMap objHashMap = new HashMap();
		Connection conn = null;
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;		
		HashMap h=null;
		String strMessage = null;

		try {
			String sqlStr = "BEGIN ORDERS.PKG_EM_DELIVERYREGION.SP_EM_CA_CARRIERPLACEOFFICELST(?, ?, ?, ?, ?); END;";
		     conn = Proveedor.getConnection();
		     cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
		     
		     cstmt.setString(1, strParamName);
		     cstmt.setInt(2, intParamDispatch);
		     cstmt.setString(3, strParamStatus);
		     cstmt.registerOutParameter(4, OracleTypes.CURSOR);
		     cstmt.registerOutParameter(5, Types.CHAR);
		     cstmt.execute();
		     
		     strMessage = cstmt.getString(5);
		     if (strMessage!=null && strMessage.startsWith("ORA-0000"))
		        strMessage=null;         
		     if( strMessage == null ){
		        rs = (ResultSet)cstmt.getObject(4);   
		        while (rs.next()) {
		           h = new HashMap();
		           h.put("carrierid", rs.getString("npcarrierid"));
		           h.put("carriername", rs.getString("npname"));      
		           list.add(h);
		        }
		     }        
		  
		     objHashMap.put("strMessage",strMessage);
		     objHashMap.put("objListado",list);

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
	
   /**
   * Purpose: Valida la activacion del check de courier
   * Developer       Fecha       Comentario
   * =============   ==========  ======================================================================
   * JCURI          23/05/2018  Creación 
   * @project PRY-1093
   * @throws java.sql.SQLException
   * @throws java.lang.Exception
   * @return 
   * @param strMessage
   */
  public HashMap activeChkCourier(int iUserId, int iAppId) throws Exception, SQLException{
	  logger.info("OrderDAO/activeChkCourier [iUserId] " + iUserId + "[iAppId] " + iAppId);
	  HashMap objHashMap = new HashMap();
	    OracleCallableStatement cstmt = null;
	    Connection conn=null; 
	    String strMessage = null;
	    int status = 0;
	    try{
	      String sqlStr =  "BEGIN ORDERS.PKG_EM_DELIVERYREGION.SP_EM_CA_VALIDATECHKCOURIER(?,?,?,?); END;";
	      conn = Proveedor.getConnection();
	      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
	      
	      cstmt.setInt(1, iUserId);
	      cstmt.setInt(2, iAppId);
	      cstmt.registerOutParameter(3, OracleTypes.NUMBER);
	      cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
	      
	      cstmt.executeUpdate();
	  
	      strMessage = cstmt.getString(4);
	      if( strMessage==null){
	    	  status = cstmt.getInt(3);
	      }
	                 
	      objHashMap.put("strMessage",strMessage);
	      objHashMap.put("statusActive",status); 
	      
	      System.out.println("OrderDAO/activeChkCourier [strMessage] : " + strMessage);
	    }catch(Exception e){
	      objHashMap.put("strMessage",e.getMessage()); 
	      System.out.println("OrderDAO/activeChkCourier [ERROR] : " + e.getMessage());
	    }finally{
	       try{
	        closeObjectsDatabase(conn,cstmt,null); 
	       }catch (Exception e) {
	          logger.error(formatException(e));
	       }
	     }    
	 
	    return objHashMap; 
  }
  
  /**
   * Purpose: Valida si la orden es tienda region
   * Developer       Fecha       Comentario
   * =============   ==========  ======================================================================
   * JCURI          01/06/2018  Creación 
   * @project PRY-1093
   * @throws java.sql.SQLException
   * @throws java.lang.Exception
   * @return 
   * @param strMessage
   */
  public HashMap validateStoreRegion(long npOrderId) throws Exception, SQLException{
	  logger.info("OrderDAO/validateStoreRegion [npOrderId] " + npOrderId);
	  HashMap objHashMap = new HashMap();
	    OracleCallableStatement cstmt = null;
	    Connection conn=null; 
	    String strMessage = null;
	    int status = 0;
	    try{
	      String sqlStr =  "BEGIN ORDERS.PKG_EM_DELIVERYREGION.SP_EM_CA_VALDISPACHTPROVINCE(?,?,?); END;";
	      conn = Proveedor.getConnection();
	      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
	      
	      cstmt.setLong(1, npOrderId);
	      cstmt.registerOutParameter(2, OracleTypes.NUMBER);
	      cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
	      
	      cstmt.executeUpdate();
	  
	      strMessage = cstmt.getString(3);
	      if( strMessage==null){
	    	  status = cstmt.getInt(2);
	      }
	                 
	      objHashMap.put("strMessage",strMessage);
	      objHashMap.put("validateActive",status); 
	      
	      System.out.println("OrderDAO/validateStoreRegion [strMessage] : " + strMessage);
	    }catch(Exception e){
	      objHashMap.put("strMessage",e.getMessage()); 
	      System.out.println("OrderDAO/validateStoreRegion [ERROR] : " + e.getMessage());
	    }finally{
	       try{
	        closeObjectsDatabase(conn,cstmt,null); 
	       }catch (Exception e) {
	          logger.error(formatException(e));
	       }
	     }    
	 
	    return objHashMap; 
  }

  /**
   * Motivo: Inserta el site temporal de la Orden VEP
   * <br>Realizado por: PBI000000042016
   * <br>Fecha: 10/10/2018
   * @return    String
   */
   public String updSiteTmpOrdenVep(Long orderId, Long siteId, Long siteIdTmp,Connection conn){

        OracleCallableStatement cstmt = null;
        String strMessage=null;

        try{

            String sqlStr = "BEGIN WEBSALES.PKG_MS_SINC_RESP.SP_MS_AC_SITE_TMP_OVEP(?, ?, ?, ? ); END;";

            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, orderId);
            cstmt.setLong(2, siteId);
            cstmt.setLong(3, siteIdTmp);
            cstmt.registerOutParameter(4, Types.VARCHAR);

            cstmt.execute();
            strMessage = cstmt.getString(4);

            logger.debug("Mensaje de Error Actualizacion: "+strMessage);

        }catch(Exception e){
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
   
}