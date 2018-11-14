package pe.com.nextel.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import pe.com.nextel.bean.DominioBean;
import pe.com.nextel.bean.EquipmentDamage;
import pe.com.nextel.bean.FormatBean;
import pe.com.nextel.bean.OrderBean;
import pe.com.nextel.bean.RepairBean;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;


//import oracle.jdbc.OracleCallableStatement;
/**
 * 
 */
public class RepairDAO extends GenericDAO {
    
	public HashMap getRepairByOrder(long lOrderId) throws SQLException, Exception {
		HashMap objHashMap = new HashMap();
		RepairBean objRepairBean = new RepairBean();
		String strMessage;
		Connection conn = null;
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;
		String strSql = "BEGIN ORDERS.NP_REPAIR01_PKG.SP_GET_REPAIR_LST(?, ?, ?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
         cstmt.setLong(1, lOrderId);
         cstmt.registerOutParameter(2, OracleTypes.CURSOR);
         cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
         cstmt.executeQuery();
         
         strMessage = cstmt.getString(3);
         
      if   (StringUtils.isBlank(strMessage) )
        {    
        
        rs = (ResultSet)cstmt.getObject(2);
            if(rs.next()) {
               int i = 1;
                        
               //DATOS REFERENTES A LA REPARACION
            objRepairBean.setNprepairid(MiUtil.parseLong(StringUtils.defaultString(rs.getString("nprepairid"),"0")));                        
            objRepairBean.setNpphone(StringUtils.defaultString(rs.getString("npphone"),""));
            objRepairBean.setNpimei(StringUtils.defaultString(rs.getString("npimei"),""));
            objRepairBean.setNpsim(StringUtils.defaultString(rs.getString("npsim"),""));
            objRepairBean.setNpimeisn(StringUtils.defaultString(rs.getString("npimeisn"),""));
            objRepairBean.setNpwarrantnextel(StringUtils.defaultString(rs.getString("npwarrantnextel"),""));
            objRepairBean.setNpequipment(StringUtils.defaultString(rs.getString("npequipment"),""));            
            objRepairBean.setNpmodel(StringUtils.defaultString(rs.getString("npmodel"),""));                                    
            objRepairBean.setNpaccessorytype(StringUtils.defaultString(rs.getString("npaccessorytype"),""));
            objRepairBean.setNpaccessorymodel(StringUtils.defaultString(rs.getString("npaccessorymodel"),""));
            objRepairBean.setNpfrequency(StringUtils.defaultString(rs.getString("npfrequency"),""));            
            objRepairBean.setNpcontactfirstname(StringUtils.defaultString(rs.getString("npcontactfirstname"),""));
            objRepairBean.setNpcontactlastname(StringUtils.defaultString(rs.getString("npcontactlastname"),""));
            objRepairBean.setNpcollectcontact(StringUtils.defaultString(rs.getString("npcollectcontact"),""));
            objRepairBean.setNpcollectaddress(StringUtils.defaultString(rs.getString("npcollectaddress"),""));
            objRepairBean.setNpservicetype(StringUtils.defaultString(rs.getString("npservicetype"),""));            
            objRepairBean.setNpservicio(StringUtils.defaultString(rs.getString("npservicecode"),""));                                             
            objRepairBean.setNpfailureid(MiUtil.parseLong(StringUtils.defaultString(rs.getString("npfailureid"),"0")));
            objRepairBean.setNpresolutionid(MiUtil.parseLong(StringUtils.defaultString(rs.getString("npresolutionid"),"0")));
            objRepairBean.setNpendsituation(StringUtils.defaultString(rs.getString("npendsituation"),""));
            objRepairBean.setNpstatus(StringUtils.defaultString(rs.getString("npstatus"),""));
            objRepairBean.setNpcodeosiptel(MiUtil.parseLong(StringUtils.defaultString(rs.getString("npcodeosiptel"),"")));
            objRepairBean.setNprepairtype(StringUtils.defaultString(rs.getString("NPREPAIRTYPE"),""));
            objRepairBean.setNpreception(StringUtils.defaultString(rs.getString("npreception"),""));
            //DATOS REFERENTES A LA ORDEN
            objRepairBean.setNporderid(MiUtil.parseLong(StringUtils.defaultString(rs.getString("nporderid"),"0")));
            objRepairBean.getOrderBean().setNpOrderId(objRepairBean.getNporderid());
            objRepairBean.getOrderBean().setNpCarrierId(MiUtil.parseLong(StringUtils.defaultString(rs.getString("npcarrierid"),"0")));
            objRepairBean.getOrderBean().setNpDescription(StringUtils.defaultString(rs.getString("npdescription"),""));
            objRepairBean.getOrderBean().setNpCreatedBy(StringUtils.defaultString(rs.getString("npcreatedby"),""));                                    
            objRepairBean.getOrderBean().setNpCreatedDate(rs.getTimestamp("npcreateddate"));
            objRepairBean.getOrderBean().setNpActivationDate(rs.getTimestamp("npActivationDate"));
            objRepairBean.getOrderBean().setNpDeliveryDate(rs.getTimestamp("npdeliverydate"));
            objRepairBean.getOrderBean().setNpClosedDate(rs.getTimestamp("npcloseddate"));
            objRepairBean.getOrderBean().setNpCustomerId(rs.getLong("npcustomerid"));         
            objRepairBean.getOrderBean().setNpCreatedBy(StringUtils.defaultString(rs.getString("npcreatedby")));         
            objRepairBean.getOrderBean().setNpSignDate(rs.getTimestamp("npsigndate"));        
            objRepairBean.setNpdescriptionnextel(rs.getString("npdescriptionnextel"));                                                                           
            objRepairBean.getOrderBean().setNpUserName1(rs.getString("npusername1"));
            objRepairBean.getOrderBean().setNpStatus(rs.getString("nporderstatus"));                     
            /*objRepairBean.getOrderBean().setFechaActivacion(rs.getTimestamp("npfechaActivacion"));                                 
            objRepairBean.getOrderBean().setNpprovider(rs.getString("npprovider"));*/            
            objRepairBean.setNpfechaActivacion(StringUtils.defaultString(rs.getString("npfechaActivacion"),""));                                                                     
            objRepairBean.setNpprovider(rs.getString("npprovider"));                         
            objRepairBean.setNpgarantia_extendida(StringUtils.defaultString(rs.getString("npgarantia_extendida"),""));            
            objRepairBean.setNpgarantia_bounce(StringUtils.defaultString(rs.getString("npgarantia_bounce"),""));
            objRepairBean.setNpgarantia_refurbish(StringUtils.defaultString(rs.getString("npgarantia_refurbish"),""));            
            objRepairBean.setNpgarantia_fabricante(StringUtils.defaultString(rs.getString("npgarantia_fabricante"),""));                        
            objRepairBean.setNpmesesAdicional(MiUtil.parseLong(StringUtils.defaultString(rs.getString("npmesesAdicional"),"0")));              
            objRepairBean.setNpproviderid(MiUtil.parseLong(StringUtils.defaultString(rs.getString("npproviderid"),"0")));                                
            //objRepairBean.setNpproviderid(StringUtils.defaultString(rs.getString("npproviderid"),""));                                            
            objRepairBean.setNpitemid(MiUtil.parseLong(StringUtils.defaultString(rs.getString("npitemid"),"0")));            
            objRepairBean.setNpgarantia_reparacion(StringUtils.defaultString(rs.getString("npgarantia_reparacion"),""));                                                         
            objRepairBean.setNpgarantia_Truebounce(StringUtils.defaultString(rs.getString("nptruebounce"),""));                                                
            objRepairBean.setNpfechaActOracle(StringUtils.defaultString(rs.getString("npfechaActOracle"),""));                                                              
            objRepairBean.setNpdiagncode(StringUtils.defaultString(rs.getString("npdiagncode"),""));                  
            objRepairBean.setNpbuildingid(StringUtils.defaultString(rs.getString("npbuildingid"),""));                                                                                 
            objRepairBean.setNpdescservicecode(StringUtils.defaultString(rs.getString("npdescservicecode"),""));                                              
            objRepairBean.setNpdesctypeservicecode(StringUtils.defaultString(rs.getString("nptypedescservicecode"),""));                            
            objRepairBean.setNporiginal(StringUtils.defaultString(rs.getString("nporiginal"),""));                                                     
            
            objRepairBean.setNptimervalue(MiUtil.getString(rs.getInt("nptimervalue")));                                              
            objRepairBean.setNpselldateacc(StringUtils.defaultString(rs.getString("npselldateacc"),""));
            
            // nuevos datos agregados para SSTT 2.0
            
            objRepairBean.setNptechnology(StringUtils.defaultString(rs.getString("nptechnology"),""));
            
            objRepairBean.setNPEQUIPGAMA(StringUtils.defaultString(rs.getString("npequipgama"),""));
            objRepairBean.setNPINDRETAIL(StringUtils.defaultString(rs.getString("npindretail"),""));
            objRepairBean.setNPFABRICATOR(StringUtils.defaultString(rs.getString("npfabricator"),""));
            objRepairBean.setNPREPAIRCENTER(StringUtils.defaultString(rs.getString("nprepaircenter"),""));
            objRepairBean.setNPWARRANSER(StringUtils.defaultString(rs.getString("npwarranser"),""));
            objRepairBean.setNPWARRANOCUR(StringUtils.defaultString(rs.getString("npwarranocur"),""));
            
            objRepairBean.setNPFABRICATORID(StringUtils.defaultString(rs.getString("npfabricatorid"),""));
            objRepairBean.setNPREPAIRCENTERID(StringUtils.defaultString(rs.getString("nprepaircenterid"),""));
            
            objRepairBean.setNPFINALSTATE(StringUtils.defaultString(rs.getString("NPFINALSTATE"),""));
            objRepairBean.setNPEQUIPSO(StringUtils.defaultString(rs.getString("NPEQUIPSO"),""));
            objRepairBean.setNpguaranteeExtFact(StringUtils.defaultString(rs.getString("npguaranteeExtFact"),""));//FPICOY Setear en el bean si el equipo tiene garantia extendida
            // nuevos datos agregados para nacionalización
            objRepairBean.setNpmodelnew(StringUtils.defaultString(rs.getString("NPMODELNEW"),""));
            objRepairBean.setNpmarca(StringUtils.defaultString(rs.getString("NPMARCA"),""));
            
            //PORTEGA PROYECTO AEP
            objRepairBean.setNpemailcontact( StringUtils.defaultString(rs.getString("npemailcontact"),"") );
            objRepairBean.setNpphonecontact( StringUtils.defaultString(rs.getString("npphonecontact"),"") );
            objRepairBean.setNpwarrantdap( StringUtils.defaultString(rs.getString("npwarrantdap"),"") );
            objRepairBean.setNpstorepickup( StringUtils.defaultString(rs.getString("npstorepickup"),"") );
            //PORTEGA AEP
            objRepairBean.setNpnotifpref( StringUtils.defaultString(rs.getString("npnotifpref"),"") );
            objRepairBean.setNprepairid_bof(MiUtil.parseLong(StringUtils.defaultString(rs.getString("nprepairid_bof"),"0"))); 
            objRepairBean.setNPDIAGNOSTICDETAIL( StringUtils.defaultString(rs.getString("npdiagnosticdetail"),"") );
            objRepairBean.setNPASSESSOROBSERVATION( StringUtils.defaultString(rs.getString("npassessorobservation"),"") );

            objRepairBean.setReadOnlyFromRepairCenter( StringUtils.defaultString(rs.getString("WV_VALUE_READONLY"),"") );  
            
            
            System.out.println("[RepairDAO - NPEQUIPGAMA - "+objRepairBean.getNPEQUIPGAMA()+"]");
            System.out.println("[RepairDAO - NPINDRETAIL - "+objRepairBean.getNPINDRETAIL()+"]");
            System.out.println("[RepairDAO - NPFABRICATOR - "+objRepairBean.getNPFABRICATOR()+"]");
            System.out.println("[RepairDAO - NPREPAIRCENTER - "+objRepairBean.getNPREPAIRCENTER()+"]");
            System.out.println("[RepairDAO - NPWARRANSER - "+objRepairBean.getNPWARRANSER()+"]");
            System.out.println("[RepairDAO - NPWARRANOCUR - "+objRepairBean.getNPWARRANOCUR()+"]");
            System.out.println("[RepairDAO - NPFABRICATORID - "+objRepairBean.getNPFABRICATORID()+"]");
            System.out.println("[RepairDAO - NPREPAIRCENTERID - "+objRepairBean.getNPREPAIRCENTERID()+"]");
            System.out.println("[RepairDAO - NPMODELNEW - "+objRepairBean.getNpmodelnew()+"]");
            System.out.println("[RepairDAO - NPMARCA - "+objRepairBean.getNpmarca()+"]");
            System.out.println("[RepairDAO - NPREPAIRTYPE - "+objRepairBean.getNprepairtype()+"]");
            System.out.println("[RepairDAO - NPSERVICECODE - "+objRepairBean.getNpservicecode()+"]");
            System.out.println("[RepairDAO - NPSERVICETYPE - "+objRepairBean.getNpservicetype()+"]");
            System.out.println("[RepairDAO - NPSERVICIO - "+objRepairBean.getNpservicio()+"]");
            System.out.println("[RepairDAO - NPDESCSERVICECODE - "+objRepairBean.getNpdescservicecode()+"]");
            System.out.println("[RepairDAO - npguaranteeExtFact - "+objRepairBean.getNpguaranteeExtFact()+"]");
            System.out.println("[RepairDAO - nprepairid_bof - "+objRepairBean.getNprepairid_bof()+"]");
            System.out.println("[RepairDAO - npdiagnosticdetail - "+objRepairBean.getNPDIAGNOSTICDETAIL()+"]");
            System.out.println("[RepairDAO - npassessorobservation - "+objRepairBean.getNPASSESSOROBSERVATION()+"]");
            
            } // agregado hoy
            
         }
      }
     /* catch (Exception e) {
         throw new Exception(e);*/
      
      catch(Exception e){
      e.printStackTrace();
      logger.error(formatException(e));
      throw new Exception(e);   
                  
      }
      finally{
         closeObjectsDatabase(conn,cstmt,rs); 
      }
		  objHashMap.put("objRepairBean", objRepairBean);
      /*objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);*/
      objHashMap.put("strMessage", strMessage);
		return objHashMap;
	}

   /**
   * Motivo: Genera una orden Interna
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 26/11/2007
   * @param     RepairBean        
   * @return    HashMap 
   */   
   public  HashMap generateOrderInterna(RepairBean objRepairBean,String strLogin)
   throws SQLException, Exception{
      
      OracleCallableStatement cstmt = null;        
      String strMessage=null;
      Connection conn=null;
      HashMap hshData=new HashMap();
      long lRepairId=0;
      OrderBean objOrderBean=objRepairBean.getOrderBean();
      String strSql = 
      "BEGIN ORDERS.NP_ORDERS15_PKG.SP_INS_REPAIR_INTERNAL( ?, ?, ?, ?, ? ); END;";                
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
         cstmt.setLong(1, objOrderBean.getNpOrderId() );
         cstmt.setLong(2, objRepairBean.getNprepairid());
         cstmt.setString(3, strLogin);      
         cstmt.registerOutParameter(4, Types.NUMERIC);
         cstmt.registerOutParameter(5, Types.CHAR);
         
         cstmt.executeUpdate();
         lRepairId = cstmt.getLong(4);
         strMessage = cstmt.getString(5);
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn,cstmt,null); 
      }
      hshData.put("strMessage",strMessage);
      hshData.put("strRepairId",lRepairId+"");
      
      return hshData;
   }
   
   /**
   * Motivo: Verifica si el usuario tiene permiso para una determinada sección
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 26/11/2007
   * @param     String  
   * @return    String 
   */   
   public String getFechaEmision(String strImei) 
   throws SQLException, Exception
   {
      String strFechaEmision=null;
      Connection conn=null;
      OracleCallableStatement cstmt = null;      
      
      String sqlStr = " { ? = call WEBCCARE.NPAC_SERVICIOTECNICO_PKG.FX_FECHA_EMISION( ? ) } "; 
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         
         cstmt.registerOutParameter(1, Types.CHAR);
         cstmt.setString(2, strImei);       
         cstmt.execute();
         
         strFechaEmision = cstmt.getString(1); 
      }
      catch (Exception e) {
         throw new Exception(e);
      }     
      finally{
         closeObjectsDatabase(conn,cstmt,null); 
      }
      return strFechaEmision;
   }  
     


   /**
   * Motivo: Genera una orden Externa
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 26/11/2007
   * @param     RepairBean        
   * @return    HashMap 
   */   
   public  HashMap generateOrderExterna(RepairBean objRepairBean,String strLogin)
   throws SQLException, Exception{
      
      OracleCallableStatement cstmt = null;        
      String strMessage=null;
      Connection conn=null;
      HashMap hshData=new HashMap();
      long lRepairId=0;
      OrderBean objOrderBean=objRepairBean.getOrderBean();
      String strSql = 
      "BEGIN ORDERS.NP_ORDERS15_PKG.SP_INS_REPAIR_EXTERNAL( ?, ?, ?, ?, ?); END;";         
      try{                                                
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
         cstmt.setLong(1, objOrderBean.getNpOrderId() );
         cstmt.setLong(2, objRepairBean.getNprepairid());
         cstmt.setString(3, strLogin);      
         cstmt.registerOutParameter(4, Types.NUMERIC);
         cstmt.registerOutParameter(5, Types.CHAR);
         
         cstmt.executeUpdate();
         lRepairId = cstmt.getLong(4);
         strMessage = cstmt.getString(5);
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn,cstmt,null); 
      }
      hshData.put("strMessage",strMessage);
      hshData.put("strRepairId",lRepairId+"");      
      return hshData;
   }

   /**
   * Motivo: Genera una orden Externa
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 26/11/2007
   * @param     RepairBean        
   * @return    HashMap 
   */   
  public  HashMap getImeiDetail(RepairBean objRepairBean) throws Exception {
    OracleCallableStatement cstmt = null;      
    String strMessage=null;    
    Connection conn=null;
    HashMap hshData=new HashMap();
    ResultSet rs = null;
    
    String strSql = 
"BEGIN WEBCCARE.NPAC_SERVICIOTECNICO_PKG.SP_GET_IMEI_DETAIL( ?, ?, ?, ? ); END;";    
    try{
       conn = Proveedor.getConnection();
       System.out.println("RepairDAO getImeiDetail - getNpphone: "+objRepairBean.getNpphone());
       System.out.println("RepairDAO getImeiDetail - getNpimei: "+objRepairBean.getNpimei());
       cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
       cstmt.setString(1, objRepairBean.getNpphone());
       cstmt.setString(2, objRepairBean.getNpimei());
       cstmt.registerOutParameter(3, OracleTypes.CURSOR);
       cstmt.registerOutParameter(4, Types.CHAR);
       cstmt.execute();
       
       strMessage = cstmt.getString(4);
       if( strMessage == null ){
         rs = (ResultSet)cstmt.getCursor(3);
         if (rs.next()) {
           hshData.put("wv_serie",rs.getString(8));
           hshData.put("wv_sim",rs.getString(3));
           hshData.put("wv_plantarifario",rs.getString(10));
           hshData.put("wv_imeiBListMsg",rs.getString(20));
           hshData.put("wv_type",rs.getString(21));
         }
      }      
    }catch(Exception e){
      throw new Exception(e);
    }finally{
      closeObjectsDatabase(conn,cstmt,rs);       
    }
    
    hshData.put("strMessage",strMessage);
    return hshData;
}
  
   /**
    * Motivo: Devuelve el cod equipo a partir de un imei
    * Realizado por: Juan Oyola
    * Fecha: 24/07/2008
    * */
    
    public HashMap getCodEquipFromImei(String strImei) throws SQLException, Exception{
      OracleCallableStatement cstmt = null;
      String strMessage = null;
      String strCodEquip = null;
      Connection conn = null;
      HashMap hshDataMap = new HashMap();
      String strSql ="BEGIN ORDERS.NP_REPAIR01_PKG.SP_GET_EQUIP_ID_FROM_IMEI(?,?,?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
         cstmt.setString(1, strImei);
         cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
         cstmt.execute();
         strMessage = cstmt.getString(3);
         strCodEquip  = cstmt.getString(2);
         hshDataMap.put("strMessage",strMessage);
         hshDataMap.put("strCodEquip",strCodEquip);
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn,cstmt,null); 
      }
      return hshDataMap;
    }
    
    /*public static void main(String[] args) throws SQLException, Exception {
      RepairDAO repairDAO = new RepairDAO();
      logger.debug(repairDAO.getCodEquipFromImei("001700204193740"));
    }*/

   /**
   * Motivo: Busca la serie de acuerdo al Imei enviado
   * <br>Realizado por: <a href="mailto:oliver.dubock@nextel.com.pe">Oliver Dubock</a>
   * <br>Fecha: 14/05/2008
   * @param     RepairBean        
   * @return    HashMap 
   */   
   public  HashMap getImeiDetailTab(RepairBean objRepairBean)
   throws SQLException, Exception{
      
      OracleCallableStatement cstmt = null;
      String strMessage=null,strSerie=null;
      Connection conn=null;
      HashMap hshData=new HashMap();  
      
      String strSql ="BEGIN WEBSALES.NP_BSCS_UTIL04_PKG.SP_GET_SERIAL_NUMBER(?,?,?); END;";               
      try{
         conn 	= Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(strSql);          
         cstmt.setString(1, objRepairBean.getNpimei());      
         cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
         
         cstmt.execute();
         strSerie  = cstmt.getString(2);
         strMessage = cstmt.getString(3);
			if(logger.isDebugEnabled()) {
				logger.debug("strSerie: "+strSerie);
				logger.debug("strMessage: "+strMessage);				
			}			
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn,cstmt,null);      
      }
      hshData.put("strMessage",strMessage);
      hshData.put("strSerie",strSerie);
      return hshData;
   }  

	/**
     * Motivo: Segun el strGeneralOption, se obtiene una lista para cargar los Combos de Reparaciones
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha: 27/11/2007
     * @param     String
     * @param     String
     * @return		ArrayList  
     */
	public HashMap getGeneralOptionList(String strGeneralOption,long lValue) throws SQLException, Exception {
		HashMap hshDataMap = new HashMap();
		Connection conn = null; 
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;
		String strMessage = null;
		ArrayList arrDominioLista = new ArrayList();
      HashMap hshData=null;
      
		String strSql =	"BEGIN WEBCCARE.NPAC_SERVICIOTECNICO_PKG.SP_GET_GENERAL_OPTION_LIST2(?, ?, ?, ?); END;";
      try{
         conn = Proveedor.getConnection();          
         cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
         cstmt.setString(1, strGeneralOption);
         cstmt.setLong(2, lValue);      
         cstmt.registerOutParameter(3, OracleTypes.CURSOR);
         cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
         cstmt.execute();
         rs = (ResultSet)cstmt.getObject(3);
         strMessage = cstmt.getString(4);
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
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn,cstmt,rs); 
		}
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		hshDataMap.put("arrDominioLista", arrDominioLista);
		return hshDataMap;
	}

   /**
   * Motivo: Actualiza los datos de una reparación
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 27/11/2007
   * @param     RepairBean
   * @param     String
   * @param     Connection
   * @return    String 
   */   
   public String updRepair(RepairBean objRepairBean,String strTramaRepuestosId,String strLogin,Connection conn)
   throws SQLException, Exception{
      
      OracleCallableStatement cstmt = null;	  
      OrderBean objOrderBean=null;
      objOrderBean=objRepairBean.getOrderBean();
      String strMessage=null;
      int i=0;
      System.out.println("RepairDAO updRepair");
      String strSql = 
      "BEGIN ORDERS.NP_ORDERS15_PKG.SP_UPD_REPAIR(?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                                                 "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                                                 "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                                                 "?, ?, ?, ?, ?, ?, ?, ?); END;";    //38
      System.out.println("objRepairBean.getNpresolutionid(): "+objRepairBean.getNpresolutionid());
      try{  
         cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
         cstmt.setLong(++i, objOrderBean.getNpOrderId());
         cstmt.setLong(++i, objRepairBean.getNprepairid());
         cstmt.setString(++i, objRepairBean.getNpphone());
         cstmt.setString(++i, objRepairBean.getNpimei());
         cstmt.setString(++i, objRepairBean.getNpsim());//5
         cstmt.setString(++i, objRepairBean.getNpimeisn());         
         cstmt.setString(++i, objRepairBean.getNpmodel());                              
         cstmt.setString(++i, objRepairBean.getNpwarrantnextel());
         cstmt.setString(++i, objRepairBean.getNpequipment());
         cstmt.setString(++i, objRepairBean.getNpaccessorytype());//10
         cstmt.setString(++i, objRepairBean.getNpaccessorymodel());
         cstmt.setString(++i, objRepairBean.getNpfrequency());
         cstmt.setString(++i, objRepairBean.getNpreception());
         cstmt.setString(++i, objOrderBean.getNpCarrierId()+"");   
         cstmt.setString(++i, objRepairBean.getNprepairtype());   //15      
         cstmt.setString(++i, objRepairBean.getNpcodeosiptel()+"");
         cstmt.setTimestamp(++i, objOrderBean.getNpSignDate());
         cstmt.setString(++i, objRepairBean.getNpcontactfirstname());
         cstmt.setString(++i, objRepairBean.getNpcontactlastname());         
         cstmt.setString(++i, objRepairBean.getNpcollectcontact());//20
         cstmt.setString(++i, objRepairBean.getNpcollectaddress());
         cstmt.setString(++i, objOrderBean.getNpDescription());
         cstmt.setString(++i, objRepairBean.getNpservicetype());
         cstmt.setLong(++i, objRepairBean.getNpfailureid());
         cstmt.setLong(++i, objRepairBean.getNpresolutionid());//25
         cstmt.setString(++i, objRepairBean.getNpendsituation());
         cstmt.setString(++i, objRepairBean.getNpstatus());
         cstmt.setString(++i, objOrderBean.getNpUserName1());      
         cstmt.setString(++i, objRepairBean.getNpdescriptionnextel());
         cstmt.setString(++i, strTramaRepuestosId);//30
         cstmt.setString(++i, strLogin);
         cstmt.setString(++i, objRepairBean.getNpservicio());        

         cstmt.setString(++i, objRepairBean.getNpemailcontact());
         cstmt.setString(++i, objRepairBean.getNpphonecontact());
         cstmt.setString(++i, objRepairBean.getNpnotifpref());
         cstmt.setInt(++i, Integer.parseInt((objRepairBean.getNpstorepickup()==null || objRepairBean.getNpstorepickup().isEmpty()?"0":objRepairBean.getNpstorepickup())));
         cstmt.setString(++i, objRepairBean.getNPREPAIRCENTERID());
          
         cstmt.registerOutParameter(++i, Types.CHAR);
         cstmt.executeUpdate();
         strMessage = cstmt.getString(i);      
      }
      catch (Exception e) {
          e.printStackTrace();
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(null,cstmt,null); 
      }
      return strMessage;
   }   

	/**
     * Motivo: Obtiene el listado de Repuestos de una Reparacion
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha: 28/11/2007
     * @param     long
     * @param     String     
     * @param     String     
     * @return		ArrayList    
     */
	public HashMap getRepuestoDetail(long lObjectId,String strObjectType,String strLogin) throws SQLException, Exception {
		HashMap hshDataMap = new HashMap();
		Connection conn = null; 
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;
		String strMessage = null;
		ArrayList arrDominioLista = new ArrayList();
      HashMap hshData=null;
      
		String strSql =	"BEGIN WEBCCARE.NPAC_SERVICIOTECNICO_PKG.SP_GET_REPUESTO_DETAIL(?, ?, ?, ?, ?); END;";
      try{
         conn = Proveedor.getConnection();          
         cstmt = (OracleCallableStatement) conn.prepareCall(strSql);     
         cstmt.setLong(1, lObjectId);
         cstmt.setString(2, strObjectType);      
         cstmt.setString(3, strLogin);      
         cstmt.registerOutParameter(4, OracleTypes.CURSOR);
         cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
         cstmt.execute();
         rs = (ResultSet)cstmt.getObject(4);
         strMessage = cstmt.getString(5);
         while(rs.next()) {
            hshData = new HashMap();			         
            hshData.put("value",rs.getString("npreprepuestoid"));         
            hshData.put("descripcion",rs.getString("npdes"));
            arrDominioLista.add(hshData);
         }
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
            closeObjectsDatabase(conn,cstmt,rs); 
      }		
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		hshDataMap.put("arrListado", arrDominioLista);
		return hshDataMap;
	}   
   
   public String insertRepair(RepairBean objRepairBean, OrderBean objOrder) {
        String strSql = "BEGIN NP_ORDERS01_PKG.SP_INS_REPAIR(? , ? , ? , ? , ?); END;";
		return strSql;
    }
      
    public int updateRepair(RepairBean objRepairBean) {
        String strSql = "BEGIN NP_ORDERS01_PKG.SP_UPD_REPAIR(? , ? , ? , ? , ?); end;";
		return 0;
	}
	
	/**
     * Motivo: 
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 26/11/2007
     * 
     * @return		ArrayList de DominioBean      
     */
	public HashMap getFlagGenerateDoc(long lOrderId) throws SQLException, Exception {
		HashMap objHashMap = new HashMap();
		ArrayList arrProcessList = new ArrayList();
		String strFlag;
		String strMessage;
		Connection conn = null;
		OracleCallableStatement cstmt = null;
		HashMap hshData=null;
		String sqlStr = "BEGIN ORDERS.NP_ORDERS15_PKG.SP_GET_GENERATE_DOC(?, ?, ?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.setLong(1, lOrderId);
         cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
         cstmt.executeQuery();
         strFlag = cstmt.getString(2);
         strMessage = cstmt.getString(3);
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
            closeObjectsDatabase(conn,cstmt,null); 
      }
		objHashMap.put("strFlag", strFlag);
		objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		return objHashMap;
    }
	
	/**
     * Motivo: 
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 26/11/2007
     * 
     * @return		ArrayList de DominioBean      
     */
	public HashMap getRepairReplaceList(String strRepairId) throws SQLException, Exception {
		HashMap objHashMap = new HashMap();
		ArrayList arrRepairReplaceList = new ArrayList();
		String strMessage;
		Connection conn = null;
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;
		String sqlStr = "BEGIN ORDERS.NP_REPAIR01_PKG.SP_GET_REPAIR_REPLACE_LST(?, ?, ?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.setString(1, strRepairId);
         cstmt.registerOutParameter(2, OracleTypes.CURSOR);
         cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
         cstmt.executeQuery();
         rs = (ResultSet)cstmt.getObject(2);
         strMessage = cstmt.getString(3);
         while(rs.next()) {
            HashMap hshRepairReplaceMap = new HashMap();
            hshRepairReplaceMap.put("nprepairid", StringUtils.defaultString(rs.getString("nprepairid"),""));
            hshRepairReplaceMap.put("npseq", StringUtils.defaultString(rs.getString("npseq"),""));
            hshRepairReplaceMap.put("npmodalitysell", StringUtils.defaultString(rs.getString("npmodalitysell"),""));
            hshRepairReplaceMap.put("npimei", StringUtils.defaultString(rs.getString("npimei"),""));
            hshRepairReplaceMap.put("npimeisn", StringUtils.defaultString(rs.getString("npimeisn"),""));
            hshRepairReplaceMap.put("npsim", StringUtils.defaultString(rs.getString("npsim"),""));
            hshRepairReplaceMap.put("npdocumenttype", StringUtils.defaultString(rs.getString("npdocumenttype"),""));
            hshRepairReplaceMap.put("npdocumentnumber", StringUtils.defaultString(rs.getString("npdocumentnumber"),""));
            hshRepairReplaceMap.put("npcreateddate", rs.getTimestamp("npcreateddate"));
            hshRepairReplaceMap.put("npcreatedby", StringUtils.defaultString(rs.getString("npcreatedby"),""));
            hshRepairReplaceMap.put("npcreatedoc", StringUtils.defaultString(rs.getString("npcreatedoc"),""));
            hshRepairReplaceMap.put("npreplistid", StringUtils.defaultString(rs.getString("npreplistid"),""));
            hshRepairReplaceMap.put("npstatus", StringUtils.defaultString(rs.getString("npstatus"),""));
            
            // MMONTOYA - Despacho en tienda
            hshRepairReplaceMap.put("npproductid", StringUtils.defaultString(rs.getString("npproductid"),""));
            hshRepairReplaceMap.put("npproductstatus", StringUtils.defaultString(rs.getString("npproductstatus"),""));
            hshRepairReplaceMap.put("npflagaccessory", StringUtils.defaultString(rs.getString("npflagaccessory"),""));
            hshRepairReplaceMap.put("npproductname", StringUtils.defaultString(rs.getString("npproductname"),""));
            hshRepairReplaceMap.put("npreturnequipment", StringUtils.defaultString(rs.getString("npreturnequipment"),""));
            
            arrRepairReplaceList.add(hshRepairReplaceMap);
         }
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn,cstmt,rs);
      }
		objHashMap.put("arrRepairReplaceList", arrRepairReplaceList);
		objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		return objHashMap;
	}
	
	public HashMap generateDocument(HashMap hshParametrosMap, Connection conn) throws Exception, SQLException {
		System.out.println("DAO: --generateDocument(HashMap hshParametrosMap, Connection conn) --");
		String hdnOrderId = (String) hshParametrosMap.get("hdnOrderId");
    
    System.out.println("hdnOrderId = " + hdnOrderId);
		String hdnRepairId = (String) hshParametrosMap.get("hdnRepairId");
    if("".equals(MiUtil.trimNotNull(hdnRepairId))){
         hdnRepairId = null;
    }
    
    System.out.println("hdnRepairId = " + hdnRepairId);
    String hdnReplaceType = (String) hshParametrosMap.get("hdnReplaceType");
    System.out.println("hdnReplaceType = " + hdnReplaceType);
		String hdnLogin = (String) hshParametrosMap.get("hdnLogin");
    System.out.println("hdnLogin = " + hdnLogin);
		String hdnFlagGenerate = (String) hshParametrosMap.get("hdnFlagGenerate");
    System.out.println("hdnFlagGenerate = " + hdnFlagGenerate);
    String strProceso = (String) hshParametrosMap.get("strProceso");
    System.out.println("strProceso = " + strProceso);
		String[] hdnRepListId = (String[]) hshParametrosMap.get("hdnRepListId");
    
    if(hdnRepListId != null){
        for(int i=0; i<hdnRepListId.length; i++){
            System.out.println("hdnRepListId["+i+"]" + hdnRepListId[i]);
        }
    }
		String[] txtImeiLista = (String[]) hshParametrosMap.get("txtImeiLista");
    if(txtImeiLista != null){
        for(int i=0; i<txtImeiLista.length; i++){
            System.out.println("txtImeiLista["+i+"]" + txtImeiLista[i]);
        }
    }
    String[] txtSimLista = (String[]) hshParametrosMap.get("txtSimLista");
    if(txtSimLista != null){
        for(int i=0; i<txtSimLista.length; i++){
            System.out.println("txtSimLista["+i+"]" + txtSimLista[i]);
        }
    }
		String[] txtSerieLista = (String[]) hshParametrosMap.get("txtSerieLista");
    if(txtSerieLista != null){
        for(int i=0; i<txtSerieLista.length; i++){
            System.out.println("txtSerieLista["+i+"]" + txtSerieLista[i]);
        }
    }
		String[] cmbTipoImeiLista = (String[]) hshParametrosMap.get("cmbTipoImeiLista");
    if(cmbTipoImeiLista != null){
        for(int i=0; i<cmbTipoImeiLista.length; i++){
            System.out.println("cmbTipoImeiLista["+i+"]" + cmbTipoImeiLista[i]);
        }
    }
		String[] chkCrearDocLista = (String[]) hshParametrosMap.get("chkCrearDocLista");
    if(chkCrearDocLista != null){
        for(int i=0; i<chkCrearDocLista.length; i++){
            System.out.println("chkCrearDocLista["+i+"]" + chkCrearDocLista[i]);
        }
    }

    // MMONTOYA - Despacho en tienda
    String[] cmbModeloLista = (String[]) hshParametrosMap.get("cmbModeloLista");
    if(cmbModeloLista != null){
        for(int i=0; i<cmbModeloLista.length; i++){
            System.out.println("cmbModeloLista["+i+"]" + cmbModeloLista[i]);
        }
    }
    String[] cmbTipoLista = (String[]) hshParametrosMap.get("cmbTipoLista");
    if(cmbTipoLista != null){
        for(int i=0; i<cmbTipoLista.length; i++){
            System.out.println("cmbTipoLista["+i+"]" + cmbTipoLista[i]);
        }
    }
    String[] cmbFlagAccesorioLista = (String[]) hshParametrosMap.get("cmbFlagAccesorioLista");
    if(cmbFlagAccesorioLista != null){
        for(int i=0; i<cmbFlagAccesorioLista.length; i++){
            System.out.println("cmbFlagAccesorioLista["+i+"]" + cmbFlagAccesorioLista[i]);
        }
    }
    String[] chkDevolverEquipoLista = (String[]) hshParametrosMap.get("chkDevolverEquipoLista");
    if(chkDevolverEquipoLista != null){
        for(int i=0; i<chkDevolverEquipoLista.length; i++){
            System.out.println("chkDevolverEquipoLista["+i+"]" + chkDevolverEquipoLista[i]);
        }
    }
        
    int intFlagUno = 0;
    int intFlagDos = 0;
		HashMap hshDataMap = new HashMap();
		ArrayList arrRepairReplaceList = new ArrayList();
		OracleCallableStatement cstmt = null;		
		String strMessage;
		System.out.println("[RepairDAO.java][generateDocument]-Inicio");
    System.out.println("[hdnReplaceType][hdnReplaceType]-hdnReplaceType: "+hdnReplaceType);
    System.out.println("[strProceso][strProceso]-strProceso: "+strProceso);
    try{
      if(!ArrayUtils.isEmpty(hdnRepListId)) {
        String[] strCrearDocLista = new String[hdnRepListId.length];
        String sqlStr = "BEGIN ORDERS.NP_REPAIR01_PKG.SP_SAVE_REPAIR_REPLACE_LST(?, ?, ?, ?, ?, ?); END;";
        //Mejora - Si la conexión es nula debemos cerrar en este método la
        //conexión
        if(conn==null){
          conn = Proveedor.getConnection();
          //Encendemos el flag
          intFlagUno = 1;
        }

        cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
        StructDescriptor sdRepairReplace = StructDescriptor.createDescriptor("ORDERS.TO_REPAIR_REPLACE", conn);
        ArrayDescriptor adRepairReplace = ArrayDescriptor.createDescriptor("ORDERS.TT_REPAIR_REPLACE_LST", conn);
        if(!ArrayUtils.isEmpty(chkCrearDocLista)) {
          for(int g = 0; g < chkCrearDocLista.length; g++) {
            System.out.println("chkCrearDocLista["+g+"]:->"+chkCrearDocLista[g]);
            if (chkCrearDocLista[g].equals("0"))
              //strCrearDocLista[Integer.parseInt(chkCrearDocLista[g])] = "N";
              strCrearDocLista[g] = "N";
            else
              strCrearDocLista[g] = "S";
              //strCrearDocLista[Integer.parseInt(chkCrearDocLista[g])] = "S";
          }
        }
        
        // Preparar arreglo de strDevolverEquipo
        String[] strDevolverEquipo = new String[hdnRepListId.length];
        
        if(!ArrayUtils.isEmpty(chkDevolverEquipoLista)) {
          for(int g = 0; g < chkDevolverEquipoLista.length; g++) {            
            if (chkDevolverEquipoLista[g].equals("0"))              
              strDevolverEquipo[g] = "N";
            else
              strDevolverEquipo[g] = "S";          
          }
        }        
        // Fin Preparar arreglo de strDevolverEquipo        
        
        logger.debug("strCrearDocLista: "+ArrayUtils.toString(strCrearDocLista));
        for(int i = 0; i < txtImeiLista.length; i++) {
          System.out.println("hdnRepListId["+i+"]: "+hdnRepListId[i]);
          
          String _cmbTipoImeiLista = MiUtil.trimNotNull(cmbTipoImeiLista[i]);
          String _txtImeiLista = MiUtil.trimNotNull(txtImeiLista[i]);
          String _txtSerieLista = MiUtil.trimNotNull(txtSerieLista[i]);
          String _strCrearDocLista = MiUtil.trimNotNull(strCrearDocLista[i]);
          String _hdnRepListId = MiUtil.trimNotNull(hdnRepListId[i]);
          String _txtSimLista = MiUtil.trimNotNull(txtSimLista[i]);
          String _cmbModeloLista = MiUtil.trimNotNull(cmbModeloLista[i]);
          String _cmbTipoLista = MiUtil.trimNotNull(cmbTipoLista[i]);
          String _cmbFlagAccesorioLista = MiUtil.trimNotNull(cmbFlagAccesorioLista[i]);
          String _strDevolverEquipo = MiUtil.trimNotNull(strDevolverEquipo[i]);
          
          
          if("".equals(_cmbTipoImeiLista)){_cmbTipoImeiLista = null;}
          if("".equals(_txtImeiLista)){_txtImeiLista = null;}
          if("".equals(_txtSerieLista)){_txtSerieLista = null;}
          if("".equals(_strCrearDocLista)){_strCrearDocLista = null;}
          if("".equals(_hdnRepListId)){_hdnRepListId = null;}
          if("".equals(_txtSimLista)){_txtSimLista = null;}
          if("".equals(_cmbModeloLista)){_cmbModeloLista = null;}
          if("".equals(_cmbTipoLista)){_cmbTipoLista = null;}
          if("".equals(_cmbFlagAccesorioLista)){_cmbFlagAccesorioLista = null;}
          if("".equals(_strDevolverEquipo)){_strDevolverEquipo = null;}
          
          System.out.println("_cmbTipoImeiLista = " + _cmbTipoImeiLista);
          System.out.println("_txtImeiLista = " + _txtImeiLista);
          System.out.println("_txtSerieLista = " + _txtSerieLista);
          System.out.println("_strCrearDocLista = " + _strCrearDocLista);
          System.out.println("_hdnRepListId = " + _hdnRepListId);          
          System.out.println("_txtSimLista = " + _txtSimLista);
          System.out.println("_cmbModeloLista = " + _cmbModeloLista);
          System.out.println("_cmbTipoLista = " + _cmbTipoLista);
          System.out.println("_cmbFlagAccesorioLista = " + _cmbFlagAccesorioLista);          
          System.out.println("_strDevolverEquipo = " + _strDevolverEquipo);
      
          System.out.println("hdnRepairId = " + hdnRepairId);
          System.out.println("cmbTipoImeiLista["+i+"] = " + cmbTipoImeiLista[i]);
          System.out.println("txtImeiLista["+i+"] = " + txtImeiLista[i]);
          System.out.println("txtSerieLista["+i+"] = " + txtImeiLista[i]);
          System.out.println("hdnLogin = " + hdnLogin);
          System.out.println("strCrearDocLista["+i+"] = " + strCrearDocLista[i]);
          System.out.println("hdnRepListId["+i+"] = " + hdnRepListId[i]);
          System.out.println("txtSimLista["+i+"] = " + txtSimLista[i]);
          System.out.println("cmbModeloLista["+i+"] = " + cmbModeloLista[i]);
          System.out.println("cmbTipoLista["+i+"] = " + cmbTipoLista[i]);
          System.out.println("cmbFlagAccesorioLista["+i+"] = " + cmbFlagAccesorioLista[i]);
          System.out.println("strDevolverEquipo["+i+"] = " + strDevolverEquipo[i]);
          
          Object[] objRepairReplace = {	hdnRepairId,
                          null,
                          cmbTipoImeiLista[i], //_cmbTipoImeiLista, 
                          txtImeiLista[i], //_txtImeiLista, //
                          txtSerieLista[i], //txtSimLista[i], _txtSerieLista, //
                          null,
                          null,
                          null,
                          hdnLogin,
                          strCrearDocLista[i],//_strCrearDocLista, //
                          StringUtils.isBlank(hdnRepListId[i])?null:hdnRepListId[i],//_hdnRepListId, //
                          null,
                          txtSimLista[i],//_txtSimLista, //

                          StringUtils.isBlank(cmbModeloLista[i]) ? null : cmbModeloLista[i], // Si no se ha seleccionado el modelo, no debe ser cadena vacía porque el tipo de dato ORACLE es NUMBER. La cadena vacía hace que ocurra una excepción.//_cmbModeloLista, //
                          cmbTipoLista[i],//_cmbTipoLista, //
                          cmbFlagAccesorioLista[i], //_cmbFlagAccesorioLista, //
                          strDevolverEquipo[i] //_strDevolverEquipo //
                        };
          System.out.println("paso 1.");
          //logger.debug("objRepairReplace["+i+"]: "+ArrayUtils.toString(objRepairReplace));
          STRUCT stcRepairReplace = new STRUCT(sdRepairReplace, conn, objRepairReplace);
          System.out.println("paso 2.");
          arrRepairReplaceList.add(stcRepairReplace);
          System.out.println("paso 3.");
        }
        ARRAY aryRepairReplace = new ARRAY(adRepairReplace, conn, arrRepairReplaceList.toArray());
        cstmt.setString(1, hdnOrderId);
        cstmt.setString(2, hdnFlagGenerate);
        cstmt.setString(3, hdnReplaceType);
        cstmt.setString(4, strProceso);
        cstmt.setARRAY(5, aryRepairReplace);
        cstmt.registerOutParameter(5, OracleTypes.ARRAY, "ORDERS.TT_REPAIR_REPLACE_LST");
        cstmt.registerOutParameter(6, OracleTypes.VARCHAR);
        cstmt.execute();
        strMessage = cstmt.getString(6);
        System.out.println("[RepairDAO.java][generateDocument]-hdnOrderId: "+hdnOrderId);
        System.out.println("[RepairDAO.java][generateDocument]-strMessage: "+strMessage);
        if(StringUtils.isBlank(strMessage)) {
          aryRepairReplace = (ARRAY)cstmt.getObject(5);
          for (int i = 0; i < aryRepairReplace.length(); i++) {
            STRUCT stcRepairReplace = (STRUCT) aryRepairReplace.getOracleArray()[i];
              //logger.debug("Fecha: "+stcRepairReplace.getAttributes()[7]);
          }
        }
        else {
          System.out.println("===============>strMessage: "+strMessage);
        }
        
        System.out.println("--generateDocument(HashMap hshParametrosMap, Connection conn) --");
        //objHashMap.put("arrRepairReplaceList", arrRepairReplaceList);
        hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
      } 
      else {
        if(logger.isDebugEnabled()) {
          logger.debug("Se procedera a eliminar");
        }
        String sqlStr = "BEGIN ORDERS.NP_REPAIR01_PKG.SP_DEL_ALL_REPAIR_REPLACE_LIST(?, ?); END;";
        
        //Mejora - Si la conexión es nula debemos cerrar en este método la
        //conexión
        if(conn==null){
          conn = Proveedor.getConnection();
          //Encendemos el flag
          intFlagDos = 1;
        }
        
        //dbmsOutput = new DbmsOutput(conn);
        //dbmsOutput.enable(1000000);
        cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
        cstmt.setString(1, hdnOrderId);
        cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
        cstmt.execute();			
        strMessage = cstmt.getString(2);
        if(StringUtils.isBlank(strMessage)) {
          logger.debug("Se eliminaron con éxito");
        }else{
          System.out.println("===============>strMessage: "+strMessage);
        }
        hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
      }
    }catch(Exception ex){
      ex.printStackTrace();
      throw new Exception(ex);
    }finally{
      if( intFlagUno == 1 ){
        closeObjectsDatabase(conn,cstmt,null);
      }else{
        closeObjectsDatabase(null,cstmt,null);
      }
      if( intFlagDos == 1 ){
        closeObjectsDatabase(conn,cstmt,null);
      }else{
        closeObjectsDatabase(null,cstmt,null);
      }
    }
    System.out.println("[RepairDAO.java][generateDocument]-Fin");
		return hshDataMap;
	}
	

public HashMap reportRepair(String strReportName, long lRepairId) throws SQLException, Exception {
                        HashMap hshRepairReportMap = new HashMap();
                        String strMessage;
                        Connection conn = null;
                        OracleCallableStatement cstmt = null;
                        ResultSet rs = null;
                        //ORDERS.NP_REPORT_INSTALACION_PKG.SP_GET_REPORT_ENVIO_CLAIM_LIST(wv_report_name,'', '','','',an_repairid,'','',wc_list,wv_message);
                        String sqlStr = "BEGIN ORDERS.NP_REPORT_INSTALACION_PKG.SP_GET_REPORT_ENVIO_CLAIM_LIST(?, ?, ?, ?, ?, ?, ?, ?, ?, ?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         int i = 0;
         cstmt.setString(++i, strReportName);
         cstmt.setString(++i, "");
         cstmt.setString(++i, "");
         cstmt.setString(++i, "");
         cstmt.setString(++i, "");
         cstmt.setLong(++i, lRepairId);
         cstmt.setString(++i, "");
         cstmt.setString(++i, "");
         cstmt.registerOutParameter(++i, OracleTypes.CURSOR);
         cstmt.registerOutParameter(++i, OracleTypes.VARCHAR);
         cstmt.executeQuery();
         strMessage = cstmt.getString(i);
         rs = (ResultSet)cstmt.getObject(--i);
         if(rs.next()) {
            i = 1;
            hshRepairReportMap.put("npRepairId", StringUtils.defaultString(rs.getString(i++),""));
            hshRepairReportMap.put("npOrderId", StringUtils.defaultString(rs.getString(i++),""));
            hshRepairReportMap.put("npRepairType", StringUtils.defaultString(rs.getString(i++),""));      
            hshRepairReportMap.put("swname", StringUtils.defaultString(rs.getString(i++),""));
            hshRepairReportMap.put("npContactName", StringUtils.defaultString(rs.getString(i++),""));
            hshRepairReportMap.put("npReception", StringUtils.defaultString(rs.getString(i++),""));
            hshRepairReportMap.put("npSignDate", MiUtil.getDate(rs.getTimestamp(i),"dd/MM/yyyy"));
            hshRepairReportMap.put("npSignTime", MiUtil.getDate(rs.getTimestamp(i++),"hh:mm a"));
            hshRepairReportMap.put("npDescription", StringUtils.defaultString(rs.getString(i++),""));
            hshRepairReportMap.put("npImeiSn", StringUtils.defaultString(rs.getString(i++),""));
            hshRepairReportMap.put("npModel", StringUtils.defaultString(rs.getString(i++),""));
            hshRepairReportMap.put("npImei", StringUtils.defaultString(rs.getString(i++),""));
            hshRepairReportMap.put("npphone", StringUtils.defaultString(rs.getString(i++),""));
            hshRepairReportMap.put("npAccessoryType", StringUtils.defaultString(rs.getString(i++),""));
            hshRepairReportMap.put("npfrequency", StringUtils.defaultString(rs.getString(i++),""));
            hshRepairReportMap.put("npDescriptionNextel", StringUtils.defaultString(rs.getString(i++),""));
            hshRepairReportMap.put("npActivationDate", StringUtils.defaultString(rs.getString(i++),""));
            hshRepairReportMap.put("npDeliveryDate", StringUtils.defaultString(rs.getString(i++),""));
            hshRepairReportMap.put("npDescriptionFactory", StringUtils.defaultString(rs.getString(i++),""));
            hshRepairReportMap.put("nxgarantianxt", StringUtils.defaultString(rs.getString(i++),""));
            hshRepairReportMap.put("nxgarantiamtr", StringUtils.defaultString(rs.getString(i++),""));
            hshRepairReportMap.put("npequipmentrent", StringUtils.defaultString(rs.getString(i++),""));
            hshRepairReportMap.put("npcreatedby", StringUtils.defaultString(rs.getString(i++),""));
            hshRepairReportMap.put("npSim", StringUtils.defaultString(rs.getString(i++),""));
            hshRepairReportMap.put("npCreatedDate", MiUtil.getDate(rs.getTimestamp(i),"dd/MM/yyyy"));
            hshRepairReportMap.put("npCreatedTime", MiUtil.getDate(rs.getTimestamp(i++),"hh:mm a"));
            hshRepairReportMap.put("contact_phone", StringUtils.defaultString(rs.getString(i++)));
            hshRepairReportMap.put("est_eqp", StringUtils.defaultString(rs.getString(i++)));
            hshRepairReportMap.put("npFabricante", StringUtils.defaultString(rs.getString(i++)));
         }
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn,cstmt,rs);
      }
                        hshRepairReportMap.put(Constante.MESSAGE_OUTPUT, strMessage);
                        return hshRepairReportMap;
   }


   
   public HashMap getLastImeiRepair(long lRepairId) throws SQLException, Exception {
                        HashMap hshRepairListMap = new HashMap();
                        String strMessage;
                        Connection conn = null;
                        OracleCallableStatement cstmt = null;
                        //ORDERS.NP_REPORT_INSTALACION_PKG.SP_GET_LAST_IMEI_REPAIR (an_nprepairId, av_npimei, av_npmodalitysell, av_npimeisn, av_message)
                        String sqlStr = "BEGIN ORDERS.NP_REPORT_INSTALACION_PKG.SP_GET_LAST_IMEI_REPAIR (?,?, ?, ?, ?, ?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         int i = 0;
         cstmt.setLong(++i, lRepairId);
         cstmt.registerOutParameter(++i, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(++i, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(++i, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(++i, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(++i, OracleTypes.VARCHAR);
         cstmt.executeQuery();
         strMessage = cstmt.getString(i);
         hshRepairListMap.put("modelo", StringUtils.defaultString(cstmt.getString(--i)));
         hshRepairListMap.put("serie", StringUtils.defaultString(cstmt.getString(--i)));
         hshRepairListMap.put("tipo", StringUtils.defaultString(cstmt.getString(--i)));
         hshRepairListMap.put("imei", StringUtils.defaultString(cstmt.getString(--i)));
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn,cstmt,null);
      }
                        hshRepairListMap.put(Constante.MESSAGE_OUTPUT, strMessage);
                        return hshRepairListMap;
   }

   /* public HashMap generateDocumentRepair(HashMap hshParametrosMap, Connection conn) throws SQLException, Exception {
		
    HashMap hshDataMap = new HashMap();      
		OracleCallableStatement cstmt = null;
    try{	
		String hdnOrderId = (String) hshParametrosMap.get("hdnOrderId");
		String hdnLogin = (String) hshParametrosMap.get("hdnLogin");			
		String strMessage;
		int    iError;
		String sqlStr = "BEGIN ORDERS.NP_REPAIR01_PKG.SP_GENERATE_DOCUMENT_REPAIR (?, ?, ?,?); END;";		
		cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
		cstmt.setLong(1,MiUtil.parseLong(hdnOrderId));
		cstmt.setString(2,hdnLogin);
		cstmt.registerOutParameter(3, Types.CHAR);
		cstmt.registerOutParameter(4, Types.INTEGER);	 
		cstmt.executeQuery();
    
		strMessage      = cstmt.getString(3);		
		iError			 = cstmt.getInt(4);	
		
		if(logger.isDebugEnabled()) {
			logger.debug("strMessage: "+strMessage);
			logger.debug("iError: "+iError);
		}
		hshDataMap.put(Constante.MESSAGE_OUTPUT,strMessage);
		hshDataMap.put("iError",iError+"");
		
    }catch(Exception ex){
      throw new Exception(ex);
    }finally{
      closeObjectsDatabase(null,cstmt,null);
    }
      		
		return hshDataMap;
	}*/
   
   public HashMap generateDocumentRepair(HashMap hshParametrosMap) throws SQLException, Exception {
			
		String hdnOrderId = (String) hshParametrosMap.get("hdnOrderId");
		String hdnLogin = (String) hshParametrosMap.get("hdnLogin");
		HashMap hshDataMap = new HashMap();
      Connection conn = null;
		OracleCallableStatement cstmt = null;
		
		String strMessage;
		int    iError;
		String sqlStr = "BEGIN ORDERS.NP_REPAIR01_PKG.SP_GENERATE_DOCUMENT_REPAIR (?, ?, ?,?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.setLong(1,MiUtil.parseLong(hdnOrderId));
         cstmt.setString(2,hdnLogin);
         cstmt.registerOutParameter(3, Types.CHAR);
         cstmt.registerOutParameter(4, Types.INTEGER);	 
         cstmt.executeQuery();
       
         strMessage      = cstmt.getString(3);		
         iError			 = cstmt.getInt(4);	
         
         if(logger.isDebugEnabled()) {
            logger.debug("strMessage: "+strMessage);
            logger.debug("iError: "+iError);
         }
         hshDataMap.put(Constante.MESSAGE_OUTPUT,strMessage);
         hshDataMap.put("iError",iError+"");
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn,cstmt,null);
      }
		return hshDataMap;
	}
	
  public ARRAY fillArrayRepairReplaceChange (HashMap hshParametrosMap, Connection conn, int iOpcion) throws SQLException, Exception {
  
		System.out.println("[fillArrayRepairReplaceChange] Inicio ");
		ArrayList arrRepairReplaceList = new ArrayList();
		System.out.println("Inicio fillArrayRepairReplaceChange ... ");
		//Obtener datos a mapear
		String hdnRepairId = (String) hshParametrosMap.get("hdnRepairId");
		String[] hdnRepListId = (String[]) hshParametrosMap.get("hdnRepListId");
		String[] txtImeiLista = (String[]) hshParametrosMap.get("txtImeiLista");
    String[] txtSimLista = (String[]) hshParametrosMap.get("txtSimLista");
    String[] txtSerieLista = (String[]) hshParametrosMap.get("txtSerieLista");
    
		String[] cmbTipoImeiLista = (String[]) hshParametrosMap.get("cmbTipoImeiLista");
		String[] chkCrearDocLista = (String[]) hshParametrosMap.get("chkCrearDocLista");
  
		if(!ArrayUtils.isEmpty(hdnRepListId)) {

			StructDescriptor sdRepairReplace = StructDescriptor.createDescriptor("ORDERS.TO_REPAIR_REPLACE", conn);
			ArrayDescriptor  adRepairReplace = ArrayDescriptor.createDescriptor("ORDERS.TT_REPAIR_REPLACE_LST", conn);
      if (iOpcion==1){ //Opcion 1 registra en el objeto solo el imei y tipo de imei
        if(!ArrayUtils.isEmpty(txtSimLista)) {
            //System.out.println("IF !ArrayUtils.isEmpty(txtSimLista)");
            for(int i = 0; i < txtImeiLista.length; i++) {
                Object[] objRepairReplaceChange = {	
                    hdnRepairId,
                    null,					
                    cmbTipoImeiLista[i],	//Tipo IMEI 
                    txtImeiLista[i],		//IMEI
                    txtSerieLista[i], 					
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    txtSimLista[i],
                    null, // MMONTOYA - Despacho en tienda. Se agregan 4 parámetros null 
                    null,
                    null,
                    null
                    };	
                logger.debug("objRepairReplace["+i+"]: "+ArrayUtils.toString(objRepairReplaceChange));
                STRUCT stcRepairReplace = new STRUCT(sdRepairReplace, conn, objRepairReplaceChange);
                arrRepairReplaceList.add(stcRepairReplace);		    
            }
          ARRAY aryRepairReplace = new ARRAY(adRepairReplace, conn, arrRepairReplaceList.toArray()); 
          return aryRepairReplace;
		    		
        }else
        {   //System.out.println("ELSE !ArrayUtils.isEmpty(txtSimLista)");
            for(int i = 0; i < txtImeiLista.length; i++) {
                //System.out.println("txtSimLista[i]: "+txtSimLista[i]);
                Object[] objRepairReplaceChange = {	
                    hdnRepairId,
                    null,					
                    cmbTipoImeiLista[i],	//Tipo IMEI 
                    txtImeiLista[i],		//IMEI
                    txtSerieLista[i], 					
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                    };	
                logger.debug("objRepairReplace["+i+"]: "+ArrayUtils.toString(objRepairReplaceChange));
                STRUCT stcRepairReplace = new STRUCT(sdRepairReplace, conn, objRepairReplaceChange);
                arrRepairReplaceList.add(stcRepairReplace);		    
            }
          ARRAY aryRepairReplace = new ARRAY(adRepairReplace, conn, arrRepairReplaceList.toArray()); 
          //System.out.println("ARRAY");
          return aryRepairReplace;
		    }
      }
    }
		System.out.println("No hay elementos");
		return null;
	}


   public String valImeiPrestamoCambio(Connection conn, RepairBean objRepairBean,HashMap hshParametrosMap)
   throws SQLException, Exception {

   OracleCallableStatement cstmt = null;	
   String strMessage=null;
   
   try{
      
      OrderBean objOrderBean=null;
      objOrderBean=objRepairBean.getOrderBean();	
      
      System.out.println("1 Probando Generación de guia!!! "+objRepairBean.getNpstatus());
      System.out.println("2 Probando Generación de guia!!! "+objRepairBean.getNprepairtype());
      System.out.println("3 Probando Generación de guia!!! "+objRepairBean.getNprepairtype());
      
      String strSql = 
      "BEGIN ORDERS.NP_ORDERS15_PKG.SP_VAL_IMEI_PRESTAMOCAMBIO(?, ?, ?, ?, ?); END;";    
      
      cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
		System.out.println("cargando aryRepairReplace");
		ARRAY aryRepairReplace = fillArrayRepairReplaceChange(hshParametrosMap, conn,1);   
		cstmt.setObject(1, (ARRAY)aryRepairReplace);
		cstmt.setString(2, objRepairBean.getNpstatus());
		cstmt.setString(3, objRepairBean.getNprepairtype());
		cstmt.setString(4, objRepairBean.getNpequipment()); //Alquiler - Cliente
		cstmt.registerOutParameter(5, Types.CHAR); 
      cstmt.executeUpdate();
		strMessage = cstmt.getString(5);
      
   }catch(Exception ex){
      ex.printStackTrace();
      throw new Exception(ex);
   }finally{
      closeObjectsDatabase(null,cstmt,null);
   }
    return strMessage;	  
   }

   public String valImeiPrestamoCambio(RepairBean objRepairBean,HashMap hshParametrosMap)
   throws SQLException, Exception {

      OracleCallableStatement cstmt = null;
      Connection conn = null;		
      String strMessage=null;
      OrderBean objOrderBean=null;
      objOrderBean=objRepairBean.getOrderBean();	  
      
      
      System.out.println("1 Probando Generación de guia!!! "+objRepairBean.getNpstatus());
      System.out.println("2 Probando Generación de guia!!! "+objRepairBean.getNprepairtype());
      System.out.println("3 Probando Generación de guia!!! "+objRepairBean.getNpequipment());
      
      
      String strSql = 
      "BEGIN ORDERS.NP_ORDERS15_PKG.SP_VAL_IMEI_PRESTAMOCAMBIO(?, ?, ?, ?, ?); END;";    
      try{
        conn = Proveedor.getConnection();      
        cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
        System.out.println("cargando aryRepairReplace");
        System.out.println("objRepairBean.getNpequipment()"+objRepairBean.getNpequipment());
        ARRAY aryRepairReplace = fillArrayRepairReplaceChange(hshParametrosMap, conn,1);   
        cstmt.setObject(1, (ARRAY)aryRepairReplace);
        cstmt.setString(2, objRepairBean.getNpstatus());
        cstmt.setString(3, objRepairBean.getNprepairtype());
        cstmt.setString(4, objRepairBean.getNpequipment()); //Alquiler - Cliente               
        cstmt.registerOutParameter(5, Types.CHAR); 
        cstmt.executeUpdate();
        strMessage = cstmt.getString(5);
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn,cstmt,null);
      }      
      return strMessage;	  
   }


     // PCASTILLO - Despacho en Tienda - Validación de Stock
  public ARRAY fillArrayRepairReplaceStock (HashMap hshParametrosMap, Connection conn) throws SQLException, Exception {
  
		System.out.println("[fillArrayRepairReplaceStock] Inicio ");
		ArrayList arrRepairReplaceList = new ArrayList();
		System.out.println("Inicio fillArrayRepairReplaceStock ... ");
		//Obtener datos a mapear
		String hdnRepairId = (String) hshParametrosMap.get("hdnRepairId");
    System.out.println("hdnRepairId = " + hdnRepairId);
    System.out.println("1.");
		String[] hdnRepListId = (String[]) hshParametrosMap.get("hdnRepListId");
    System.out.println("hdnRepListId = " + hdnRepListId);
    System.out.println("2.");
		String[] txtImeiLista = (String[]) hshParametrosMap.get("txtImeiLista");
    System.out.println("txtImeiLista = " + txtImeiLista);
    System.out.println("3.");
    String[] txtSimLista = (String[]) hshParametrosMap.get("txtSimLista");
    System.out.println("txtSimLista = " + txtSimLista);
    System.out.println("4.");
    String[] txtSerieLista = (String[]) hshParametrosMap.get("txtSerieLista");
    System.out.println("txtSerieLista = " + txtSerieLista);
    System.out.println("5.");
    
    String[] txtTipoLista = (String[]) hshParametrosMap.get("cmbTipoLista");
    System.out.println("txtTipoLista = " + txtTipoLista);
    System.out.println("6.");
    String[] txtModeloLista = (String[]) hshParametrosMap.get("cmbModeloLista");
    System.out.println("txtModeloLista = " + txtModeloLista);
    System.out.println("7.");
		String[] cmbTipoImeiLista = (String[]) hshParametrosMap.get("cmbTipoImeiLista");
    System.out.println("cmbTipoImeiLista = " + cmbTipoImeiLista);
    System.out.println("8.");
		String[] txtCrearDocLista = (String[]) hshParametrosMap.get("chkCrearDocLista");
    System.out.println("txtCrearDocLista = " + txtCrearDocLista);
    System.out.println("9.");
    //String[] txtDevolverEquipoLista = (String[]) hshParametrosMap.get("chkDevolverEquipoLista");
    //String hdnDevolverEquipoListaAux = (String) hshParametrosMap.get("hdnDevolverEquipoListaAux");
    //String  txtDevolverEquipo = null;
    try{
        System.out.println("hdnRepListId = " + hdnRepListId);
        if(!ArrayUtils.isEmpty(hdnRepListId)) {
    
          StructDescriptor sdRepairReplace = StructDescriptor.createDescriptor("ORDERS.TO_REPAIR_REPLACE", conn);
          ArrayDescriptor  adRepairReplace = ArrayDescriptor.createDescriptor("ORDERS.TT_REPAIR_REPLACE_LST", conn);
            if(!ArrayUtils.isEmpty(txtSimLista)) {
                System.out.println("txtImeiLista.length: "+txtImeiLista.length);
                for(int i = 0; i < txtImeiLista.length; i++) {  
                    System.out.println("g g g ");
                    
                    String strhdnRepairId = hdnRepairId; 
                    String strcmbTipoImeiLista = MiUtil.trimNotNull(cmbTipoImeiLista[i]); 
                    String strtxtImeiLista = MiUtil.trimNotNull(txtImeiLista[i]); 
                    String strtxtSerieLista = MiUtil.trimNotNull(txtSerieLista[i]); 
                    String strtxtCrearDocLista = MiUtil.trimNotNull(txtCrearDocLista[i]);
                    String strtxtSimLista = MiUtil.trimNotNull(txtSimLista[i]);
                    String strtxtModeloLista = MiUtil.trimNotNull(txtModeloLista[i]);
                    String strtxtTipoLista = MiUtil.trimNotNull(txtTipoLista[i]);
                    
                    if("".equals(strhdnRepairId)){strhdnRepairId =  null;}
                    if("".equals(strcmbTipoImeiLista)){strcmbTipoImeiLista =  null;}
                    if("".equals(strtxtImeiLista)){strtxtImeiLista =  null;}
                    if("".equals(strtxtSerieLista)){strtxtSerieLista =  null;}
                    if("".equals(strtxtCrearDocLista)){strtxtCrearDocLista =  null;}
                    if("".equals(strtxtSimLista)){strtxtSimLista =  null;}
                    if("".equals(strtxtModeloLista)){strtxtModeloLista =  null;}
                    if("".equals(strtxtTipoLista)){strtxtTipoLista =  null;}
                    
                    Object[] objRepairReplaceChange = {	
                        strhdnRepairId,
                        null,
                        strcmbTipoImeiLista,	//Tipo IMEI 
                        strtxtImeiLista,		//IMEI
                        strtxtSerieLista, 					
                        null,
                        null,
                        null,
                        null,
                        strtxtCrearDocLista,
                        null,
                        null,
                        strtxtSimLista,
                        strtxtModeloLista, // MMONTOYA - Despacho en tienda. Se agregan 4 parámetros null 
                        strtxtTipoLista,
                        null,
                        null
                        };    
                    System.out.println("10.");
                    logger.debug("objRepairReplace["+i+"]: "+ArrayUtils.toString(objRepairReplaceChange));
                    System.out.println("11.");
                    STRUCT stcRepairReplace = new STRUCT(sdRepairReplace, conn, objRepairReplaceChange);
                    System.out.println("12.");
                    arrRepairReplaceList.add(stcRepairReplace);	
                    System.out.println("13.");
                }
              System.out.println("14.");
              ARRAY aryRepairReplace = new ARRAY(adRepairReplace, conn, arrRepairReplaceList.toArray()); 
              System.out.println("15.");
              return aryRepairReplace;
          }
        }
    }catch(Exception e){
        System.out.println("Error - fillArrayRepairReplaceStock: " + e.getMessage());
        e.getStackTrace();
    }
		System.out.println("No hay elementos");
		return null;
	}


  // PCASTILLO - Despacho en Tienda - Validación de Stock
   public HashMap valStockPrestCambio(RepairBean objRepairBean,HashMap hshParametrosMap)
   throws SQLException, Exception {

      OracleCallableStatement cstmt = null;
      Connection conn = null;
      HashMap hshDataMap=new HashMap();
      String strMessage=null;
      String strFlagStock=null;
      String strStockMessage=null;
      OrderBean objOrderBean=null;
      objOrderBean=objRepairBean.getOrderBean();
      
      System.out.println("DAO: ---valStockPrestCambio---");
      System.out.println("1 Probando valStockPrestCambio!!! "+objRepairBean.getNpstatus());
      System.out.println("2 Probando valStockPrestCambio!!! "+objRepairBean.getNprepairtype());
      System.out.println("3 Probando valStockPrestCambio!!! "+objRepairBean.getNpequipment());
      
      String hdnDevolverEquipoListaAux = (String) hshParametrosMap.get("hdnDevolverEquipoListaAux");
      //aseguramos que la variable hdnDevolverEquipoListaAux sea una cadean vacia
      if(hdnDevolverEquipoListaAux == null || "".equals(MiUtil.trimNotNull(hdnDevolverEquipoListaAux))){
        hdnDevolverEquipoListaAux = "";
      }else{
        hdnDevolverEquipoListaAux = hdnDevolverEquipoListaAux.replaceAll("-","");
      }
      System.out.println("4 Probando hdnDevolverEquipoListaAux!!! "+hdnDevolverEquipoListaAux);
      String[] hdnTipoDocumentoAux = (String[]) hshParametrosMap.get("hdnTipoDocumentoAux");
      System.out.println("hdnTipoDocumentoAux: "+hdnTipoDocumentoAux);
      String txtTipoDocumentoAux = "";
      
      if(hdnTipoDocumentoAux != null) {
          //System.out.println("hdnTipoDocumentoAux.length: "+hdnTipoDocumentoAux.length);
          for(int i = 0; i < hdnTipoDocumentoAux.length; i++) {  
              System.out.println("DAO Stock hdnTipoDocumentoAux["+i+"]: " + hdnTipoDocumentoAux[i]);
              if (hdnTipoDocumentoAux[i].equals("GR")) {
                  txtTipoDocumentoAux = txtTipoDocumentoAux + "1";
              }else{
                  txtTipoDocumentoAux = txtTipoDocumentoAux + "0";
              }
          }
      }
      System.out.println("DAO Stock txtTipoDocumentoAux: " + txtTipoDocumentoAux);
      String strSql = "BEGIN ORDERS.NP_ORDERS16_PKG.SP_GET_STOCK_MESSAGE_SS(?, ?, ?, ?, ?, ?); END;";
      
      try{
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement)conn.prepareCall(strSql);        
        ARRAY aryRepairReplace = fillArrayRepairReplaceStock(hshParametrosMap, conn);
        cstmt.setObject(1, (ARRAY)aryRepairReplace); 
        cstmt.setString(2, hdnDevolverEquipoListaAux);
        cstmt.setString(3, txtTipoDocumentoAux);
        cstmt.registerOutParameter(4, Types.CHAR); 
        cstmt.registerOutParameter(5, Types.CHAR); 
        cstmt.registerOutParameter(6, Types.CHAR); 
        cstmt.executeUpdate();
        strStockMessage = cstmt.getString(4);
        System.out.println("Inicio resultados...");
        hshDataMap.put("strStockMessage", strStockMessage);
        strFlagStock = cstmt.getString(5);
        hshDataMap.put("strFlagStock", strFlagStock);
        strMessage = cstmt.getString(6);
        hshDataMap.put("strMessage", strMessage);
        System.out.println("Fin resultados...");
      }
      catch (Exception e) {
         System.out.println("Error - valStockPrestCambio: " + e.getMessage());
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn,cstmt,null);
      }      
      return hshDataMap;
   }



   /**
   * Motivo: Obtiene lista de resoluciones de la reparacion
   * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
   * <br>Fecha: 10/03/2009
   * @param     strValue
   * @return    HashMap 
   */

   public HashMap getResolution(String strFabricatorId) throws SQLException, Exception{
      ArrayList arrDominioLista = new ArrayList();
      HashMap hshData=new HashMap();
      HashMap hshDataMap=new HashMap();
      String strMessage=null;
      Connection conn = null;
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;
      System.out.println("RepairDAO getResolution - strFabricatorId: "+strFabricatorId);
      String sqlStr = "BEGIN REPAIR.NP_GENERAL_PKG.SP_GET_RESOLUTION(? , ? , ?); END;";
   
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         cstmt.setString(1, strFabricatorId);
         cstmt.registerOutParameter(2, OracleTypes.CURSOR);
         cstmt.registerOutParameter(3, Types.CHAR);
         cstmt.execute();
         rs = (ResultSet)cstmt.getObject(2);
         strMessage = cstmt.getString(3);
         System.out.println("RepairDAO getResolution - strMessage: "+strMessage);
         while(rs.next()) {
            hshData =new HashMap();       
            System.out.println("RepairDAO getResolution - rs.getString(1): "+rs.getString(1));
            hshData.put("id",rs.getString(1));	
            hshData.put("valor",rs.getString(2));			
            arrDominioLista.add(hshData);
         }
      }catch(Exception e){
         logger.error(formatException(e));
         throw new Exception(e);
      }finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
      hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
      hshDataMap.put("arrDominioLista", arrDominioLista);
      return hshDataMap;

   }



   /**
   * Motivo: Obtiene lista de diagnosticos
   * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
   * <br>Fecha: 10/03/2009
   * @param     strValue
   * @return    HashMap 
   */

   public HashMap getDiagnosis(String intProviderId) throws SQLException, Exception{
      ArrayList list = new ArrayList();
      HashMap hshData=new HashMap();
      String strMessage=null;
      Connection conn = null;
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;

      String sqlStr = "BEGIN REPAIR.NP_GENERAL_PKG.SP_GET_DIAGNOSIS(? , ? , ?); END;";
   
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         
         cstmt.setString(1, intProviderId);
         
         cstmt.registerOutParameter(2, OracleTypes.CURSOR);
         cstmt.registerOutParameter(3, Types.CHAR);
         cstmt.execute();
         rs = (ResultSet)cstmt.getObject(2);
         strMessage = cstmt.getString(3);

         while (rs.next()) {
            DominioBean dominio = new DominioBean();          
            dominio.setValor(StringUtils.defaultString(rs.getString(1)));
            dominio.setDescripcion(StringUtils.defaultString(rs.getString(2)));
            list.add(dominio);
         }
      }catch(Exception e){
         logger.error(formatException(e));
         throw new Exception(e);
      }finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
      hshData.put("strMessage",strMessage);
      hshData.put("arrListado",list);
      return hshData;
   }


/**
* Motivo: Obtiene lista de fallas dado un tipo de falla
* <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
* <br>Fecha: 11/03/2009
* @param     strValue
* @return    HashMap 
*/
public HashMap getFailureList(int intValue, int intRepairId, String strRepairTypeId) throws SQLException, Exception{
   ArrayList list = new ArrayList();
   HashMap hshData=new HashMap();
   String strMessage=null;
   Connection conn = null;
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;   
   String sqlStr = "BEGIN REPAIR.NP_WARRANTY_EQUIP.SP_GET_ALL_FAILURE_LIST(?, ?, ?, ?, ?); END;";
   
   try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, intValue);
      cstmt.setLong(2, intRepairId);
      cstmt.setString(3,strRepairTypeId);
      cstmt.registerOutParameter(4, OracleTypes.CURSOR);
      cstmt.registerOutParameter(5, Types.CHAR);
      cstmt.execute();
      rs = (ResultSet)cstmt.getObject(4);
      strMessage = cstmt.getString(5);

      if (!StringUtils.isNotBlank(strMessage)){
        while (rs.next()) {
            DominioBean dominio = new DominioBean();          
            dominio.setValor(StringUtils.defaultString(rs.getString(2)));
            dominio.setDescripcion(StringUtils.defaultString(rs.getString(1)));
            list.add(dominio);
        }
      }      
   }catch(Exception e){
      logger.error(formatException(e));
      throw new Exception(e);
   }finally{
      closeObjectsDatabase(conn, cstmt, rs);
   }
   hshData.put("strMessage",strMessage);
   hshData.put("arrListado",list);
   return hshData;
  }



public String getDiagnosisDescription(int intValue) throws SQLException, Exception{
    String strDiagnosisDesc = null;
    String strMessage = null;
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    
    String sqlStr = "BEGIN REPAIR.NP_WARRANTY_EQUIP.SP_GET_DIAGNOSIS_DESC(?, ?, ?); END;";

    try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, intValue);
      cstmt.registerOutParameter(2, Types.CHAR);
      cstmt.registerOutParameter(3, Types.CHAR);
      cstmt.execute();

      strDiagnosisDesc = cstmt.getString(2);
      strMessage = cstmt.getString(3);

    }catch(Exception e){
      logger.error(formatException(e));
      throw new Exception(e);
   }finally{
      closeObjectsDatabase(conn, cstmt, rs);
   }
    return strDiagnosisDesc;
  }




/**
* Motivo: Obtiene lista de repuestos para un modelo
* <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
* <br>Fecha: 12/03/2009
* @param     strValue
* @return    HashMap 
*/
   public HashMap getSparePartsListByModel(String strValue, int intRepairId, String strRepairTypeId) throws SQLException, Exception{
   ArrayList list = new ArrayList();
   HashMap hshData=new HashMap();
   String strMessage=null;
   Connection conn = null;
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;

   String sqlStr = "BEGIN REPAIR.NP_WARRANTY_EQUIP.SP_GET_SPAREPART_LIST(?, ? , ? , ?, ?, ?); END;";
   
   try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setString(1, strValue);
      cstmt.setInt(2, intRepairId);
      cstmt.setString(3, strRepairTypeId);
      
      cstmt.setInt(4 , 0);
      
      cstmt.registerOutParameter(5, OracleTypes.CURSOR);
      cstmt.registerOutParameter(6, Types.CHAR);
      cstmt.execute();
      rs = (ResultSet)cstmt.getObject(5);
      strMessage = cstmt.getString(6);

      if (!StringUtils.isNotBlank(strMessage)){
        while (rs.next()) {
            DominioBean dominio = new DominioBean();          
            dominio.setValor(StringUtils.defaultString(rs.getString(2)));
            dominio.setDescripcion(StringUtils.defaultString(rs.getString(1)));
            list.add(dominio);
        }
      }
   }catch(Exception e){
      logger.error(formatException(e));
      throw new Exception(e);
   }finally{
      closeObjectsDatabase(conn, cstmt, rs);
   }
   hshData.put("strMessage",strMessage);
   hshData.put("arrListado",list);
   return hshData;
  }




/**
* Motivo: Obtiene lista de fallas seleccionadas para una reparacion
* <br>Realizado por: <a href="mailto:juan.oyola@nextel.com.pe">Juan Oyola</a>
* <br>Fecha: 11/03/2009
* @param     strValue
* @return    HashMap 
*/

  public HashMap getSelectedFailureList(int intRepairId, String strRepairTypeId) throws SQLException, Exception{
   ArrayList list = new ArrayList();
   HashMap hshData=new HashMap();
   String strMessage=null;
   Connection conn = null;
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;

   String sqlStr = "BEGIN REPAIR.NP_WARRANTY_EQUIP.SP_GET_REPAIR_FAIL_LIST(?, ?, ?, ?); END;";

   try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setInt(1, intRepairId);
      cstmt.setString(2, strRepairTypeId);
      cstmt.registerOutParameter(3, OracleTypes.CURSOR);
      cstmt.registerOutParameter(4, Types.CHAR);
      cstmt.execute();
      rs = (ResultSet)cstmt.getObject(3);
      strMessage = cstmt.getString(4);

      if (!StringUtils.isNotBlank(strMessage)){
        while (rs.next()){
            DominioBean dominio = new DominioBean();          
            dominio.setValor(StringUtils.defaultString(rs.getString(2)));
            dominio.setDescripcion(StringUtils.defaultString(rs.getString(1)));
            list.add(dominio);
        }
      }
   }catch(Exception e){
      logger.error(formatException(e));
      throw new Exception(e);
   }finally{
      closeObjectsDatabase(conn, cstmt, rs);
   }
   hshData.put("strMessage",strMessage);
   hshData.put("arrListado",list);
   return hshData;
  }


/**
     * Motivo: Obtiene la Lista de los Tipos de Servicios
     * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
     * <br>Fecha: 17/03/2009
     * 
     * @return		ArrayList de DominioBean      
     */               
     
	public HashMap getTypeServices() throws SQLException, Exception {
		HashMap objHashMap = new HashMap();
		ArrayList arrTypeServices = new ArrayList();
		String strMessage;
		Connection conn = null;
		HashMap hshData=new HashMap();
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;
		      
      String sqlStr = "BEGIN ORDERS.NP_REPAIR01_PKG.SP_GET_TYPE_SERVICES(?, ?); END;";
      
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
            arrTypeServices.add(dominio);
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
		objHashMap.put("arrTypeServices", arrTypeServices);
		objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		return objHashMap;
    }
	

/**
* Motivo: Obtiene los servicios para un tipo de servicio
* <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
* <br>Fecha: 17/03/2009
* @param     strValue
* @return    HashMap 
*/

public HashMap getloadServices(String strTypeServices) throws SQLException, Exception{
   ArrayList list = new ArrayList();
   HashMap hshData=new HashMap();
   String strMessage=null;
   Connection conn = null;
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;

   String sqlStr = "BEGIN ORDERS.NP_REPAIR01_PKG.SP_GET_SERVICES(?,?, ?); END;";

   try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);            
      cstmt.setString(1, strTypeServices);                        
      cstmt.registerOutParameter(2, OracleTypes.CURSOR);
      cstmt.registerOutParameter(3, Types.CHAR);
      cstmt.execute();
      rs = (ResultSet)cstmt.getObject(2);
      strMessage = cstmt.getString(3);

      if (!StringUtils.isNotBlank(strMessage)){
        while (rs.next()) {
            DominioBean dominio = new DominioBean();
            dominio.setValor(StringUtils.defaultString(rs.getString(1)));
            dominio.setDescripcion(StringUtils.defaultString(rs.getString(2)));
            list.add(dominio);
        }
      }
   }catch(Exception e){
      logger.error(formatException(e));
      throw new Exception(e);
   }finally{
      closeObjectsDatabase(conn, cstmt, rs);
   }
   hshData.put("strMessage",strMessage);
   hshData.put("arrListado",list);
   return hshData;
}



/**
* Motivo: Realiza el proceso de actualizacion desde la pagina reparacion, actualiza reparacion, elimina e inserta los detalles respectivos
* <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
* <br>Fecha: 18/03/2009
* @param     strValue
* @return    HashMap 
*/

   public String updateRepairPlus(RepairBean objRepairBean,String strLogin,java.sql.Connection conn) throws Exception, SQLException{


      OracleCallableStatement cstmt = null;	  
      OrderBean objOrderBean=null;
      objOrderBean=objRepairBean.getOrderBean();
      String strMessage=null;
      int i=0;
      System.out.println("+++++++++++++++ORDERS.NP_REPAIR01_PKG.SP_GEN_NP_REPAIR_PLUS");    
      System.out.println("Order ID -->"+String.valueOf(objOrderBean.getNpOrderId())  );                                                                                                                                                                                                       
      System.out.println("Repair ID -->"+String.valueOf(objRepairBean.getNprepairid()) );                                              
      System.out.println("Servicio -->"+objRepairBean.getNpservicio() );                                                           
      System.out.println("Fecha Activación -->"+objRepairBean.getNpfechaActivacion() );                                                            
      System.out.println("Provider -->"+objRepairBean.getNpprovider() );
      System.out.println("ProviderId -->"+objRepairBean.getNpproviderid() );
      System.out.println("MesesAdicional -->"+String.valueOf(objRepairBean.getNpmesesAdicional()) );                                               
      System.out.println("Garantia Extendida -->"+objRepairBean.getNpgarantia_extendida() );                                                              
      System.out.println("Garantia Bounce -->"+objRepairBean.getNpgarantia_bounce() );                                                                
      System.out.println("Garantia Refurbish -->"+objRepairBean.getNpgarantia_refurbish() );                                                 
      System.out.println("Garantia Fabricante -->"+objRepairBean.getNpgarantia_fabricante() );                                               
      System.out.println("Login -->"+strLogin );                                
      System.out.println("FNC -->"+objRepairBean.getNpFnc() );                                                                                          
      System.out.println("Sin Costo -->"+objRepairBean.getNprRepSinCosto() );                                                
      System.out.println("Garantia Reparación -->"+objRepairBean.getNpgarantia_reparacion() );
      System.out.println("Garantia TrueBounce -->"+objRepairBean.getNpgarantia_truebounce() );      
      System.out.println("Código diagnostico -->"+objRepairBean.getNpdiagncode() );
      System.out.println("Estado Final del Equipo -->"+objRepairBean.getNPFINALSTATE() );      
      System.out.println("Sistema Operativo -->"+objRepairBean.getNPEQUIPSO() );   
      System.out.println("OTRAS FALLAS SELECCIONADAS -->"+objRepairBean.getNpotras_fallas_seleccionadas() );   
      System.out.println("detalle diagnostico -->"+objRepairBean.getNPDIAGNOSTICDETAIL() );     
      System.out.println("observacion asesor -->"+objRepairBean.getNPASSESSOROBSERVATION() );  
                                                
      String sqlStr = "BEGIN ORDERS.NP_REPAIR01_PKG.SP_GEN_NP_REPAIR_PLUS(?, ?, ?, ?, ?, ?); END;";
      try{                                                   
         Object[] objOrderRepar                = {                                                                                                                                                      
                                                  String.valueOf(objOrderBean.getNpOrderId()),                                                                                                                                                                                                       
                                                  String.valueOf(objRepairBean.getNprepairid()),                                                 
                                                  objRepairBean.getNpservicio(),                                                                   
                                                  objRepairBean.getNpfechaActivacion(),                                                            
                                                  String.valueOf(objRepairBean.getNpproviderid()),   //5                                                                
                                                  String.valueOf(objRepairBean.getNpmesesAdicional()),                                                  
                                                  objRepairBean.getNpgarantia_extendida(),                                                              
                                                  objRepairBean.getNpgarantia_bounce(),                                                                 
                                                  objRepairBean.getNpgarantia_refurbish(),                                                    
                                                  objRepairBean.getNpgarantia_fabricante(),  //10                                                
                                                  strLogin,                                                  
                                                  objRepairBean.getNpFnc(),                                                                                                    
                                                  objRepairBean.getNprRepSinCosto(),                                                  
                                                  objRepairBean.getNpgarantia_reparacion(),
                                                  objRepairBean.getNpgarantia_truebounce(),  //15                                                
                                                  objRepairBean.getNpdiagncode(),
                                                  objRepairBean.getNporiginal(),
                                                  objRepairBean.getNptimervalue(),
                                                  objRepairBean.getNpselldateacc(),                             
                                                  objRepairBean.getNpinventorycode(),
                                                  objRepairBean.getNPFINALSTATE(),
                                                  objRepairBean.getNPEQUIPSO(),
                                                  objRepairBean.getNPDIAGNOSTICDETAIL(),
                                                  objRepairBean.getNPASSESSOROBSERVATION()                                                  
                                               };                                                                                              
                                                                                                                                                                
      StructDescriptor sdOrderReparation   = StructDescriptor.createDescriptor("ORDERS.TO_REPAIR_PLUS", conn);
      STRUCT stcOrderReparation            = new STRUCT(sdOrderReparation, conn, objOrderRepar);
            
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

      cstmt.setSTRUCT(++i,stcOrderReparation);
      cstmt.setString(++i,objRepairBean.getNpfallas_seleccionadas()); //Códigos de las fallas seleccionadas
      cstmt.setString(++i,objRepairBean.getNprepuestos_seleccionados()); //Codigos de repuestos seleccionados
      cstmt.setString(++i,objRepairBean.getNprepuestos_n_u()); //Indicadores de los repuestos Nuevos o Usados (N|U)
      cstmt.setString(++i, objRepairBean.getNpotras_fallas_seleccionadas()); //codigos de OTRAS fallas seleccionadas
      cstmt.registerOutParameter(++i, Types.CHAR);
      cstmt.executeUpdate();
      
      strMessage = cstmt.getString(i);      
      System.out.println("updateRepairPlus strMessage: "+strMessage);
      }
      catch (Exception e) {
         logger.error(formatException(e));
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(null,cstmt,null); 
      }
      return strMessage;
   }


/**
* Motivo: Inserta registro de reparacion 
* <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
* <br>Fecha: 19/03/2009
* @param     strValue
* @return    HashMap
*/

  public HashMap newOrderReparation(HashMap objHashMap) throws SQLException, Exception {
    HashMap hshData=new HashMap();
    String strMessage=null;
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    int intReparationId = 0;

    try{
      conn = Proveedor.getConnection();
      String sqlStr = "BEGIN REPAIR.NP_MANT_REPAIR_BOF.SP_INS_ORDER_REPAIRBOF(?, ?, ?); END;";
      
      String strReparationId      = (String) objHashMap.get("hdnRepairId");      
      String strhdnOrderId        = (String) objHashMap.get("hdnOrderId");      
      String strRepairType        = (String) objHashMap.get("cmbProceso"); 
      String strImei              = (String) objHashMap.get("txtImei");          
      String strSim               = (String) objHashMap.get("txtSim");       
      String strSerie             = (String) objHashMap.get("txtSerie");    
      String strTipoAccesorio     = (String) objHashMap.get("cmbTipoAccesorio");   
      String strModeloAccesorio   = (String) objHashMap.get("cmbModeloAccesorio");
      String strModel             = (String) objHashMap.get("cmbModelo");    
      String strCodeOsiptel       = (String) objHashMap.get("txtCodigoOsiptel");         
      String strProveedor         = (String) objHashMap.get("hdnProveedorId");            
      //String strProveedor         = (String) objHashMap.get("txtProveedor");            
      String strEquipment         = (String) objHashMap.get("txtAlquilado");             
      String strFnc               = (String) objHashMap.get("chkFnc");              
      String strGarantiaFabrica   = (String) objHashMap.get("hdnGarantiaFabrica");   
      String strGarantiaExtend    = (String) objHashMap.get("hdnGarantiaExt");                   
      String strGarantiaBounce    = (String) objHashMap.get("hdnGarantiaBounce");        
      String strGarantiaRefurbish = (String) objHashMap.get("hdnGarantiaRefurbish");                  
      String strMesesAdional      = (String) objHashMap.get("txtMesesAdicional"); 
      String strFecActiv          = (String) objHashMap.get("txtFecActiv");         
      String strFecOracle         = (String) objHashMap.get("txtFechaEmision");       
      String strResolucion        = (String) objHashMap.get("txtResolucion"); 
      String strLogin             = (String) objHashMap.get("hdnLogin");                    
      String strNumTel            = (String) objHashMap.get("txtNextel");      
      String strItemid            = (String) objHashMap.get("hdnItemid");      
      //String strResolucionid      = (String) objHashMap.get("cmbCodigoResolucion");                      
      String strTipoServicio      = (String) objHashMap.get("cmbTipoServicio");      
      String strSituacion         = (String) objHashMap.get("cmbSituacion");          
      String strCodEquipo         = (String) objHashMap.get("txtCodEquipo");         
      String strRecepcion         = (String) objHashMap.get("cmbRecepcion");      
      String strBuildingid        = (String) objHashMap.get("hdnBuildingId");              
      String strResolucionid       = (String) objHashMap.get("cmbDiagnostico");         
      String strTimerValue         =  (String) objHashMap.get("txtTimerValue");      
      String strSemFabricacion     =  (String) objHashMap.get("txtSemFabricacion");
      String strFechaVenta     =  (String) objHashMap.get("txtFechaVenta");
      
      String strNPTECNOLOGY      = (String) objHashMap.get("txtTecnologia");
      String strNPFABRICATOR     = (String) objHashMap.get("hdnFabricanteId");
      String strNPREPAIRCENTER   = (String) objHashMap.get("hdnCentroReparacionId");
      String strGarantiaExtFabrica   = (String) objHashMap.get("hdnGarantiaExtFabrica");
      String strDAP              = (String) objHashMap.get("hdnDAP");
      String strGarantiaReparacion   = (String) objHashMap.get("hdnGarantiaReparacion");
      String strReporteCliente   = (String) objHashMap.get("txtReporteCliente");
      String strNpdescriptionnextel   = (String) objHashMap.get("txtNextelDiagnostico");
      
      //Registrar en el log los parámetros que recibo el método  newOrderReparation  (Clase DAO ? Aplicación de Ordenes).
      //ocruces - N_O000044181 Ordenes de reparación se están duplicando
      System.out.println("[RepairDAO.newOrderReparation][strReparationId]=["+strReparationId+"]");
      System.out.println("[RepairDAO.newOrderReparation][strhdnOrderId]=["+strhdnOrderId+"]");
      System.out.println("[RepairDAO.newOrderReparation][strRepairType]=["+strRepairType+"]");
      System.out.println("[RepairDAO.newOrderReparation][strImei]=["+strImei+"]");
      System.out.println("[RepairDAO.newOrderReparation][strSim]=["+strSim+"]");
      System.out.println("[RepairDAO.newOrderReparation][strSerie]=["+strSerie+"]");
      System.out.println("[RepairDAO.newOrderReparation][strTipoAccesorio]=["+strTipoAccesorio+"]");
      System.out.println("[RepairDAO.newOrderReparation][strModeloAccesorio]=["+strModeloAccesorio+"]");
      System.out.println("[RepairDAO.newOrderReparation][strModel]=["+strModel+"]");
      System.out.println("[RepairDAO.newOrderReparation][strCodeOsiptel]=["+strCodeOsiptel+"]");
      System.out.println("[RepairDAO.newOrderReparation][strProveedor]=["+strProveedor+"]");
      System.out.println("[RepairDAO.newOrderReparation][strEquipment]=["+strEquipment+"]");
      System.out.println("[RepairDAO.newOrderReparation][strFnc]=["+strFnc+"]");
      System.out.println("[RepairDAO.newOrderReparation][strGarantiaFabrica]=["+strGarantiaFabrica+"]");
      System.out.println("[RepairDAO.newOrderReparation][strGarantiaExtend]=["+strGarantiaExtend+"]");
      System.out.println("[RepairDAO.newOrderReparation][strGarantiaBounce]=["+strGarantiaBounce+"]");
      System.out.println("[RepairDAO.newOrderReparation][strGarantiaRefurbish]=["+strGarantiaRefurbish+"]");
      System.out.println("[RepairDAO.newOrderReparation][strMesesAdional]=["+strMesesAdional+"]");
      System.out.println("[RepairDAO.newOrderReparation][strFecActiv]=["+strFecActiv+"]");
      System.out.println("[RepairDAO.newOrderReparation][strFecOracle]=["+strFecOracle+"]");
      System.out.println("[RepairDAO.newOrderReparation][strResolucion]=["+strResolucion+"]");
      System.out.println("[RepairDAO.newOrderReparation][strLogin]=["+strLogin+"]");
      System.out.println("[RepairDAO.newOrderReparation][strNumTel]=["+strNumTel+"]");
      System.out.println("[RepairDAO.newOrderReparation][strItemid]=["+strItemid+"]");
      System.out.println("[RepairDAO.newOrderReparation][strTipoServicio]=["+strTipoServicio+"]");
      System.out.println("[RepairDAO.newOrderReparation][strSituacion]=["+strSituacion+"]");
      System.out.println("[RepairDAO.newOrderReparation][strCodEquipo]=["+strCodEquipo+"]");
      System.out.println("[RepairDAO.newOrderReparation][strRecepcion]=["+strRecepcion+"]");
      System.out.println("[RepairDAO.newOrderReparation][strBuildingid]=["+strBuildingid+"]");
      System.out.println("[RepairDAO.newOrderReparation][strResolucionid]=["+strResolucionid+"]");
      System.out.println("[RepairDAO.newOrderReparation][strTimerValue]=["+strTimerValue+"]");
      System.out.println("[RepairDAO.newOrderReparation][strSemFabricacion]=["+strSemFabricacion+"]");
      System.out.println("[RepairDAO.newOrderReparation][strFechaVenta]=["+strFechaVenta+"]");
      System.out.println("[RepairDAO.newOrderReparation][strNPTECNOLOGY]=["+strNPTECNOLOGY+"]");
      System.out.println("[RepairDAO.newOrderReparation][strNPFABRICATOR]=["+strNPFABRICATOR+"]");
      System.out.println("[RepairDAO.newOrderReparation][strNPREPAIRCENTER]=["+strNPREPAIRCENTER+"]");
      System.out.println("[RepairDAO.newOrderReparation][strGarantiaExtFabrica]=["+strGarantiaExtFabrica+"]");
      System.out.println("[RepairDAO.newOrderReparation][strDAP]=["+strDAP+"]");
      System.out.println("[RepairDAO.newOrderReparation][strGarantiaReparacion]=["+strGarantiaReparacion+"]");
      System.out.println("[RepairDAO.newOrderReparation][strReporteCliente]=["+strReporteCliente+"]");
      System.out.println("[RepairDAO.newOrderReparation][strNpdescriptionnextel]=["+strNpdescriptionnextel+"]");
      //ocruces - N_O000044181 Ordenes de reparación se están duplicando
      
      System.out.println("[RepairDAO.newOrderReparation][hdnDAP]["+strDAP+"][hdnGarantiaReparacion]["+strGarantiaReparacion+"]");
      
      Object[] objOrderRepar = { strhdnOrderId, //wn_nporderid  1
                                 strItemid,     //wn_npitemid
                                 strRepairType, //wv_nprepprocess
                                 strImei,       //wv_npimei
                                 strNumTel,     //wv_npfononumber   5
                                 strSim,        //wv_npsim
                                 strSerie,      //wv_npnroserie
                                 strTipoAccesorio,//wv_npaccetype
                                 strModeloAccesorio,//wn_npaccecode
                                 strModel,      //wv_npmodelo   10
                                 
                                 strCodEquipo,  //wv_npinventorycode    Código de inventario                                                                                                   
                                 strCodeOsiptel,//wv_nposiptelcode
                                 strProveedor,  //wv_npbrandid
                                 strEquipment,  //wv_npmodality
                                 strFnc,        //wv_npfndind       15
                                 strGarantiaFabrica,//wv_npwarrfactory
                                 strGarantiaExtend, //wv_npwarrextended
                                 strGarantiaBounce, //wv_npbounce
                                 strGarantiaRefurbish,  //wv_npwarrrefur
                                 strMesesAdional,   //wn_npextendedmonth 20
                                 
                                 strFecActiv,       //wd_npactivedate
                                 strFecOracle,      //wd_nporacledate                                                           
                                 null,              //wv_npsevicetype
                                 strTipoServicio,   //wn_npservicecode
                                 strReporteCliente, //wv_npclientmanif      25  
                                 null,   //wn_npresolucode       26
                                 strSituacion,      //wv_npendsituation     revisar xsiacaso                                 
                                 null,              //wv_npnexteldiag    bien                                 
                                 null,              //wv_npequipresta       por preguntar Iris a Usuario
                                 null,              //wv_nprepairstaten     el Dao pasa como Pendiente 30
                                 
                                 strLogin,          //wv_npcreatedby    31                                 
                                 strRecepcion,      //wv_nprecepcion
                                 strBuildingid,     //wv_npbuildingid
                                 strResolucionid,    //wv_npdiagncode
                                 
                                 strTimerValue,     //wv_NPTIMERVALUE   35
                                 strFechaVenta,     //wv_NPSELLDATEACC  aqui  debe ir la fecha de Venta
                                 strSemFabricacion, //WV_NPFREQUENCY
                                 null,              //wv_NPREPAIRTYPE   38
                                 strNPTECNOLOGY,    //wv_NPTECNOLOGY
                                 strNPFABRICATOR,   //wv_NPFABRICATOR   40
                                 strNPREPAIRCENTER, //wv_NPREPAIRCENTER
                                 strGarantiaExtFabrica,     //wv_npextguaranteefac  42
                                 
                                 //PORTEGA
                                strGarantiaReparacion, //        WV_NPWARRREPAIR
                                null, //        wv_npwarrcr
                                null, //        wv_npuserresponse       45
                                 
                                0, //        wn_npcrcost
                                null, //        wv_npcolor
                                null, //        wv_nprcfailuretypes
                                null, //        wv_nprcdiagnosis
                                null, //        wv_nprcdiagnosisdet     50
                                 
                                null, //        wv_nprcresolution
                                null, //        wv_nprcresolutiondet
                                null, //        wv_npsubsidycode
                                strDAP, //        wv_npwarrantdap 54
                                strNpdescriptionnextel // wv_npdescriptionnextel 55
                                };
                                                                                                
      StructDescriptor sdOrderReparation = StructDescriptor.createDescriptor("REPAIR.TO_ORDER_REPAIRBOF", conn);
      STRUCT stcOrderReparation = new STRUCT(sdOrderReparation, conn, objOrderRepar);    

      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

      cstmt.setSTRUCT(1,stcOrderReparation);
      cstmt.registerOutParameter(2, OracleTypes.NUMBER);
      cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
      cstmt.execute();      
      strMessage = cstmt.getString(3);

      if(!StringUtils.isNotBlank(strMessage)){
        intReparationId = cstmt.getInt(2);
        strReparationId = MiUtil.getString(intReparationId);
        hshData.put("intReparationId",strReparationId);
      }    
    }catch(Exception e){
      e.printStackTrace();
      logger.error(formatException(e));
      throw new Exception(e);
    }finally{
      closeObjectsDatabase(conn, cstmt, null);
    }
    hshData.put(Constante.MESSAGE_OUTPUT,strMessage);
    return hshData;
  }


  /**
   * Motivo: Obtiene lista de repuestos seleccionados para una reparacion
   * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
   * <br>Fecha: 23/03/2009
   * @param     strValue
   * @return    HashMap
   */
  public HashMap getSelectedSpareList(int intRepairId, String strRepairTypeId) throws SQLException, Exception {
    ArrayList list = new ArrayList();
    HashMap hshData = new HashMap();
    String strMessage = null;
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;

    String sqlStr = "BEGIN REPAIR.NP_WARRANTY_EQUIP.SP_GET_REPAIR_SPARE_LIST(?, ?, ?, ?, ?); END;";

    try {
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
      cstmt.setInt(1, intRepairId);
      cstmt.setString(2, strRepairTypeId);
      cstmt.setInt(3, 0); //Ord Atencion 
      cstmt.registerOutParameter(4, OracleTypes.CURSOR);
      cstmt.registerOutParameter(5, Types.CHAR);
      cstmt.execute();

      strMessage = cstmt.getString(5);

      if (!StringUtils.isNotBlank(strMessage)) {
        rs = (ResultSet) cstmt.getObject(4);

        while (rs.next()) {
          DominioBean dominio = new DominioBean();
          dominio.setValor(StringUtils.defaultString(rs.getString(2)));
          dominio.setDescripcion(StringUtils.defaultString(rs.getString(1)));
          list.add(dominio);
        }
      }
    } catch (Exception e) {
      logger.error(formatException(e));
      throw new Exception(e);
    } finally {
      closeObjectsDatabase(conn, cstmt, rs);
    }
    hshData.put("strMessage", strMessage);
    hshData.put("arrListado", list);
    return hshData;
  }



/**
   * Motivo: Verfifica si la falla es True Bounce
   * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
   * <br>Fecha: 02/04/2009
   * @param     strValue
   * @return    HashMap 
   */

   public HashMap/*String*/  validateTrueBounce(String strImei,long intFallaid, String strBounce) throws SQLException, Exception{      
      String strMessage=null;
      Connection conn = null;
      OracleCallableStatement cstmt = null;   
      ResultSet rs = null;
      String strValor = null;
      
      HashMap hshData = new HashMap(); //agregado
      
      String sqlStr = "BEGIN REPAIR.NP_WARRANTY_EQUIP.SP_WARRANTY_TRUE_BOUNCE(? , ? , ? , ? , ?); END;";
            
      try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setString(1, strImei);
      cstmt.setLong(2, intFallaid);
      cstmt.setString(3, strBounce);      
      
      cstmt.registerOutParameter(4, Types.CHAR);
      cstmt.registerOutParameter(5, Types.CHAR);
      cstmt.execute();

      strValor = cstmt.getString(4);
      strMessage = cstmt.getString(5);

    }catch(Exception e){
         logger.error(formatException(e));
         throw new Exception(e);
      }finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
      
      hshData.put("strValor", strValor);
      hshData.put("strMessage", strMessage);
      
    return hshData/*strValor*/;
  }
      

  
  
      

  /**
   * Motivo: Obtiene el detalle de la ventana de Abrir reparacion para un IMEI como resultado de la opción Buscar
   * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomas Mogrovejo</a>
   * <br>Fecha: 04/04/2009
   * @param     strValue
   * @return    HashMap
   */
  public HashMap getRepairCount(String strValue) throws SQLException, Exception {
    ArrayList list = new ArrayList();
    HashMap hshData = new HashMap();
    String strMessage = null;
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    int intNroReg = 0;
    int intNpRepairId = 0;

    String sqlStr = "BEGIN REPAIR.NP_MANT_REPAIR_BOF.SP_GET_REPAIR_COUNT(?, ?, ?, ?); END;";

    try {
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
      cstmt.setString(1, strValue);
      cstmt.registerOutParameter(2, Types.INTEGER); // NRO DE REPARACIONES PARA EL IMEI
      cstmt.registerOutParameter(3, Types.INTEGER); // CODIGO REPARACION PARA EL IMEI
      cstmt.registerOutParameter(4, Types.CHAR);
      cstmt.execute();
      strMessage = cstmt.getString(4);

      if (!StringUtils.isNotBlank(strMessage)) {
        intNroReg = MiUtil.parseInt(cstmt.getString(2));
        hshData.put("intNroReg", MiUtil.getString(intNroReg));
        if (intNroReg == 1) {
          intNpRepairId = MiUtil.parseInt(cstmt.getString(3));
        }
      }
    } catch (Exception e) {
      logger.error(formatException(e));
      throw new Exception(e);
    } finally {
      closeObjectsDatabase(conn, cstmt, rs);
    }
    hshData.put("intNpRepairId", MiUtil.getString(intNpRepairId));
    hshData.put("strMessage", strMessage);
    return hshData;
  }


/**
   * Motivo: Obtiene el detalle de la ventana de Abrir reparacion para un IMEI
   * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
   * <br>Fecha: 04/04/2009
   * @param     strValue
   * @return    HashMap
   */
  public HashMap getSmallRepairDetail(String strValue) throws SQLException, Exception {
    ArrayList list = new ArrayList();
    HashMap hshData = new HashMap();
    String strMessage = null;
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    int i = 1;

    String sqlStr = "BEGIN REPAIR.NP_MANT_REPAIR_BOF.SP_GET_SMALL_DETAIL(?, ?, ?); END;";

    try {
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
      cstmt.setString(1, strValue);
      cstmt.registerOutParameter(2, OracleTypes.CURSOR);
      cstmt.registerOutParameter(3, Types.CHAR);
      cstmt.execute();
      strMessage = cstmt.getString(3);

      if (!StringUtils.isNotBlank(strMessage)) {
        rs = (ResultSet) cstmt.getObject(2);

        while (rs.next()) {
          HashMap hspMap = new HashMap();

          hspMap.put("av_modelorig", StringUtils.defaultString(rs.getString(i++)));
          hspMap.put("av_imei", StringUtils.defaultString(rs.getString(i++)));
          hspMap.put("av_garantia_fabricante", StringUtils.defaultString(rs.getString(i++)));
          hspMap.put("av_garantia_extendida", StringUtils.defaultString(rs.getString(i++)));
          hspMap.put("av_garantia_bounce", StringUtils.defaultString(rs.getString(i++)));
          hspMap.put("av_garantia_refurbish", StringUtils.defaultString(rs.getString(i++)));
          hspMap.put("av_provider", StringUtils.defaultString(rs.getString(i++)));
          hspMap.put("an_providerid", MiUtil.getString(rs.getInt(i++)));
          hspMap.put("ad_fechaactoracle", MiUtil.toFecha(rs.getDate(i++)));
          hspMap.put("ad_fechaactivacion", MiUtil.toFecha(rs.getDate(i++)));
          hspMap.put("ad_fechafingarantia", MiUtil.toFecha(rs.getDate(i++)));
          hspMap.put("av_codinventario", StringUtils.defaultString(rs.getString(i++)));
          hspMap.put("an_months", MiUtil.getString(rs.getInt(i++)));

          list.add(hspMap);
        }
      }
    } catch (Exception e) {
      logger.error(formatException(e));
      throw new Exception(e);
    } finally {
      closeObjectsDatabase(conn, cstmt, rs);
    }
    hshData.put("strMessage", strMessage);
    hshData.put("arrListado", list);
    return hshData;
  }



 /**
   * Motivo: Obtiene el detalle de la ventana de Abrir reparacion para un IMEI en la ventana Reparaciones --- Este DAO ES DE PRUEBA LUEGO LO PUEDO  SACAR
   * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
   * <br>Fecha: 04/04/2009
   * @param     strValue
   * @return    HashMap
   */
  public HashMap getNpRepairBOF(int intValue, String strLogin) throws SQLException, Exception {
    ArrayList list = new ArrayList();
    String strMessage = null;
    Connection conn = null;
    HashMap hspMap = new HashMap();
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    int i = 1;

    String sqlStr = "BEGIN REPAIR.NP_MANT_REPAIR_BOF.SP_GET_NP_REPAIR_BOF(?, ?, ?, ?); END;";

    try {
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
      cstmt.setInt(1, intValue);
      cstmt.setString(2, strLogin);
      cstmt.registerOutParameter(3, OracleTypes.CURSOR);
      cstmt.registerOutParameter(4, Types.CHAR);
      cstmt.execute();

      strMessage = cstmt.getString(4);

      if (!StringUtils.isNotBlank(strMessage)) {
        rs = (ResultSet) cstmt.getObject(3);

            while (rs.next()) {
                hspMap.put("NPREPAIRID", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPORDERID", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPITEMID", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPREPAIRTYPE", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPREPPROCESS", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPFONONUMBER", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPSIM", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPNROSERIE", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPACCETYPE", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPACCECODE", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPMODELO", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPINVENTORYCODE", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPOSIPTELCODE", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPBRANDID", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPMODALITY", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPWARRFACTORY", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPWARRREFUR", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPWARREXTENDED", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPBOUNCE", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPEXTENDEDMONTH", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPACTIVEDATE", MiUtil.toFecha(rs.getDate(i++)));
                hspMap.put("NPORACLEDATE", MiUtil.toFecha(rs.getDate(i++)));
                hspMap.put("NPIMEICHANGE", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPSERIECHANGE", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPTECNICCODE", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPREPAIRSTAT", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("TIPO_REPARACION", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("PROVEEDOR", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("npdiagncode", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("RESOLUCION", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("DETALLE_RESOLUCION", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPIMEI", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("ORIGEN_EQUIPO", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("TIPO_FALLA", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPCLIENTMANIF", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPENDSITUATION", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("CASOS_EQUIPO", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("CASOS_USUARIO", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("buildingid", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("recepcion", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPWARRREPAIR", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("CREATEDDATE", MiUtil.toFecha(rs.getDate(i++)));
                hspMap.put("NPFREQUENCY", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPSELLDATEACC", MiUtil.toFecha(rs.getDate(i++)));

                hspMap.put("npfabricator", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("npfabricator_desc", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("nprepaircenter", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("nprepaircenter_desc", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("nptecnology", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPEXTGUARANTEEFAC", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPWARRANTYDAP", StringUtils.defaultString(rs.getString(i++)));
                
                //PORTEGA
                hspMap.put("NPRCFAILURETYPES", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPRCDIAGNOSIS", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPRCDIAGNOSISDET", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPRCRESOLUTION", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPRCRESOLUTIONDET", StringUtils.defaultString(rs.getString(i++)));

                hspMap.put("NPDIAGNOSTICDETAIL_BOF", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPASSESSOROBSERVATION_BOF", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPRESOLUCODE_BOF", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPRCRESOLUTIONDET_BOF", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("WV_VALUE_READONLY_BOF", StringUtils.defaultString(rs.getString(i++)));                
                
                hspMap.put("NPUSERRESPONSE", StringUtils.defaultString(rs.getString(i++)));
                hspMap.put("NPREPAIRSTAT", StringUtils.defaultString(rs.getString(i++)));
                

                System.out.println("RepairDAO getNpRepairBOF - TIPO_REPARACION : " + (String)hspMap.get("TIPO_REPARACION"));

            }
      }
    } catch (Exception e) {
      logger.error(formatException(e));
      throw new Exception(e);
    } finally {
      closeObjectsDatabase(conn, cstmt, rs);
    }
    hspMap.put("strMessage", strMessage);
    hspMap.put("arrListado", list);
    return hspMap;
  }



/**
   * Motivo: Activa equipos desde la sección cambio de IMEI
   * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
   * <br>Fecha: 06/04/2009
   * @param     strImei
   * @return    strMessage 
   */

   public HashMap activateEquipment(String strImei,String strImeiNuevo, String strSim, String strSimNuevo, String strReplaceType) throws SQLException, Exception{      
      String strMessage=null;
      Connection conn = null;
      OracleCallableStatement cstmt = null;   
      ResultSet rs = null;      
      HashMap hshData=new HashMap();
      
      System.out.println("Imei Nuevo Enviado a SP_GEN_ACTIVATE_EQUIPMENT ST: "+strImeiNuevo);
      System.out.println("Imei Antiguo Enviado a SP_GEN_ACTIVATE_EQUIPMENT ST: "+strImei);
      System.out.println("Sim Nuevo Enviado a SP_GEN_ACTIVATE_EQUIPMENT ST: "+strSimNuevo);
      System.out.println("Sim Antiguo Enviado a SP_GEN_ACTIVATE_EQUIPMENT ST: "+strSim);
      System.out.println("Tipo reemplazo Enviado a SP_GEN_ACTIVATE_EQUIPMENT ST: "+strReplaceType);
      
      String sqlStr = "BEGIN ORDERS.NP_REPAIR01_PKG.SP_GEN_ACTIVATE_EQUIPMENT(?, ?, ?, ?, ? ,?); END;";
            
      try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setString(1, strImei);   
      cstmt.setString(2, strImeiNuevo);    
      cstmt.setString(3, strSim);
      cstmt.setString(4, strSimNuevo);
      cstmt.setString(5, strReplaceType);
      cstmt.registerOutParameter(6, Types.CHAR);
      cstmt.execute();
      
      strMessage = cstmt.getString(6);                  

    }catch(Exception e){
         //strMessage = e.getMessage();
         logger.error(formatException(e));
         throw new Exception(e);
      }finally{
         //hshData.put("strMessage",strMessage);
         closeObjectsDatabase(conn, cstmt, rs);
      }
      
      hshData.put("strMessage",strMessage);
         
    return hshData;
  }




/**
   * Motivo: Devuelve la reparación id de acuerdo a una número de Orden
   * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
   * <br>Fecha: 14/04/2009
   * @param     String  
   * @return    String 
   */   
   public HashMap getRepairid(String strImei) 
   throws SQLException, Exception
   {
      String intRepairid=null;
      Connection conn=null;
      OracleCallableStatement cstmt = null;   
      HashMap hshData=new HashMap();
      
      String sqlStr = " { ? = call REPAIR.NP_WARRANTY_EQUIP.FX_GET_REPAIR_BY_ORDER( ? ) } "; 
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         
         cstmt.registerOutParameter(1, Types.NUMERIC);                  
         cstmt.setString(2, strImei);       
         cstmt.execute();
         
         intRepairid = cstmt.getString(1); 
      }
      catch (Exception e) {
         throw new Exception(e);
      }     
      finally{
            closeObjectsDatabase(conn,cstmt,null); 
      }
      
      hshData.put("intRepairid",intRepairid);
      
      return hshData;
   }  

	
   
   /**
   * Motivo: Metodo que genera la guía de remisión.
   * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
   * <br>Fecha: 15/03/2009
   * @param     strValue
   * @param     conn
   * @return    HashMap
   */
  public HashMap GenerateInvDoc(HashMap hshParameters) throws SQLException, Exception {
    HashMap hshResult = new HashMap();
    String strMessage = null;
    OracleCallableStatement cstmt = null;
    Connection conn=null;

    String sqlStr = "BEGIN REPAIR.SPI_GEN_REPAIR_DOC(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); END;";
            
    int iRepairId = MiUtil.parseInt((String) hshParameters.get("an_repairid"));
    String strRepairTypeId = (String) hshParameters.get("strRepairTypeId");
    String strLogin = (String) hshParameters.get("strLogin");
    int iDocumentParentType = MiUtil.parseInt((String) hshParameters.get("iDocumentParentType"));
    int iTransferReason = MiUtil.parseInt((String) hshParameters.get("iTransferReason"));
    String strItemTypeId = (String) hshParameters.get("strItemTypeId");
    int iOrganizationId = MiUtil.parseInt((String) hshParameters.get("iOrganizationId"));
    String strSubInventoryCode = (String) hshParameters.get("strSubInventoryCode");

    String strTransNumber = null;
    int iTransactionId = 0;

    try {
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

      cstmt.setInt(1, iRepairId);
      cstmt.setString(2, strRepairTypeId);
      cstmt.setString(3, strLogin);
      cstmt.setInt(4, iDocumentParentType);
      cstmt.setInt(5, iTransferReason);
      cstmt.setString(6, strItemTypeId);
      cstmt.setInt(7, iOrganizationId);
      cstmt.setString(8, strSubInventoryCode);

      cstmt.registerOutParameter(9, Types.INTEGER); //an_nptransactionid
      cstmt.registerOutParameter(10, Types.CHAR); //av_transnnumber
      cstmt.registerOutParameter(11, Types.CHAR); //av_message

      cstmt.execute();

      strMessage = cstmt.getString(11);

      if (!StringUtils.isNotBlank(strMessage)) {
        iTransactionId = cstmt.getInt(9);
        strTransNumber = cstmt.getString(10);
      }
    } catch (Exception e) {
      logger.error(formatException(e));
      throw new Exception(e);
    } finally {
      closeObjectsDatabase(conn, cstmt, null);
    }
    hshResult.put("iTransactionId", MiUtil.getString(iTransactionId));
    hshResult.put("strTransNumber", strTransNumber);
    hshResult.put("strMessage", strMessage);
    return hshResult;
  }


/**
     * Motivo: Obtiene la Lista de los Tipos de Servicios
     * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
     * <br>Fecha: 16/04/2009
     * 
     * @return		ArrayList de DominioBean      
     */               
     
	public HashMap getServices() throws SQLException, Exception {
		HashMap objHashMap = new HashMap();
		ArrayList arrServices = new ArrayList();
		String strMessage;
		Connection conn = null;
		HashMap hshData=new HashMap();
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;
		      
      String sqlStr = "BEGIN ORDERS.NP_REPAIR01_PKG.SP_GET_SERVICES(?, ?); END;";
      
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
            arrServices.add(dominio);
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
		objHashMap.put("arrServices", arrServices);
		objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		return objHashMap;
    }

/**
     * Motivo: Obtiene la Lista de los Tipos de Accesorios
     * <br>Realizado por: <a href="mailto:juan.oyola@nextel.com.pe">Juan Oyola</a>
     * <br>Fecha: 28/04/2009
     * @return		ArrayList de DominioBean      
     */               
     
	public HashMap getAccesories() throws SQLException, Exception {
		HashMap objHashMap = new HashMap();
		ArrayList arrAccesories = new ArrayList();
		String strMessage;
		Connection conn = null;
		HashMap hshData=new HashMap();
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;
		      
      String sqlStr = "BEGIN ORDERS.NP_REPAIR01_PKG.SP_GET_ACCESORY_TYPES(?, ?); END;";
      
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
            arrAccesories.add(dominio);
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
		objHashMap.put("arrAccesories", arrAccesories);
		objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		return objHashMap;
    }
    
/**
     * Motivo: Obtiene la Lista de los modelos de accesorios de acuerdo al tipo seleccionado
     * <br>Realizado por: <a href="mailto:julio.herrera@nextel.com.pe">Julio Herrera</a>
     * <br>Fecha: 29/04/2009
     * @return		ArrayList de DominioBean      
     */               
     
	public HashMap getAccesoryModels(String strAccesoryType) throws SQLException, Exception {
		HashMap objHashMap = new HashMap();
		ArrayList arrAccesoryModels = new ArrayList();
		String strMessage;
		Connection conn = null;
		HashMap hshData=new HashMap();
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;
		      
      String sqlStr = "BEGIN ORDERS.NP_REPAIR01_PKG.SP_GET_ACCESORIES(?, ?, ?); END;";
      
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.setString(1, strAccesoryType);
         cstmt.registerOutParameter(2, OracleTypes.CURSOR);
         cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
         cstmt.executeQuery();
         
         rs = (ResultSet)cstmt.getObject(2);
         strMessage = cstmt.getString(3);         
         while (rs.next()) {
            DominioBean dominio = new DominioBean();
            dominio.setValor((String)rs.getString(1));
            dominio.setDescripcion((String)rs.getString(2));
            arrAccesoryModels.add(dominio);
         }
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
		objHashMap.put("arrAccesoryModels", arrAccesoryModels);
		objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		return objHashMap;
    }

         
         
   /**
   * Motivo: Valida los documentos generados por reparación
   * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
   * <br>Fecha: 09/06/2009
   * @param     String  
   * @return    String 
   */   
   public HashMap getDocGenerate(String strImei, long strRepairId) 
   throws SQLException, Exception
   {
      String intRepairid=null;
      Connection conn=null;
      OracleCallableStatement cstmt = null;   
      HashMap hshData=new HashMap();
      
      String sqlStr = " { ? = call REPAIR.NP_WARRANTY_EQUIP.FX_VAL_DOC_GENERATE( ? , ? ) } "; 
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         
         cstmt.registerOutParameter(1, Types.NUMERIC);                  
         cstmt.setString(2, strImei); 
         
         cstmt.setLong(3, strRepairId);                         
         cstmt.execute();
         
         intRepairid = cstmt.getString(1); 
      }
      catch (Exception e) {
         throw new Exception(e);
      }     
      finally{
            closeObjectsDatabase(conn,cstmt,null); 
      }
      
      hshData.put("intRepairid",intRepairid);
      
      return hshData;
   }  

   /**
   * Motivo: Valida los Diagnosticos por nivel
   * <br>Realizado por: <a href="mailto:julio.herrera@nextel.com.pe">Julio Herrera</a>
   * <br>Fecha: 10/06/2009
   * @param     String  
   * @return    String 
   */  
   
   public HashMap getDiagnosisLevel(String strProviderId, int intLevel) throws SQLException, Exception{
      ArrayList list = new ArrayList();
      HashMap hshData=new HashMap();
      String strMessage=null;
      Connection conn = null;
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;
 
      String sqlStr = "BEGIN REPAIR.NP_GENERAL_PKG.SP_GET_DIAGNOSIS_LEVEL(? , ? , ? , ?); END;";
  
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);         
         cstmt.setString(1, strProviderId);
         cstmt.setLong(2, intLevel);         
         cstmt.registerOutParameter(3, OracleTypes.CURSOR);
         cstmt.registerOutParameter(4, Types.CHAR);
         
         cstmt.execute();
         rs = (ResultSet)cstmt.getObject(3);
         strMessage = cstmt.getString(4);

         while (rs.next()) {
            DominioBean dominio = new DominioBean();          
            dominio.setValor(StringUtils.defaultString(rs.getString(1)));
            dominio.setDescripcion(StringUtils.defaultString(rs.getString(2)));
            list.add(dominio);
         }
      }catch(Exception e){
         logger.error(formatException(e));
         throw new Exception(e);
      }finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
      hshData.put("strMessage",strMessage);
      hshData.put("arrListado",list);
      return hshData;
   }                  
   
   
   
   /**
   * Motivo: Valida los documentos generados por reparación
   * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
   * <br>Fecha: 09/06/2009
   * @param     String  
   * @return    String 
   */   
   public HashMap getDocGenClose(String strImei, long strRepairId) 
   throws SQLException, Exception
   {
      String intRepairid=null;
      Connection conn=null;
      OracleCallableStatement cstmt = null;   
      HashMap hshData=new HashMap();
      
      String sqlStr = " { ? = call REPAIR.NP_WARRANTY_EQUIP.FX_VAL_DOC_GENCLOSE( ? , ? ) } "; 
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         
         cstmt.registerOutParameter(1, Types.NUMERIC);                  
         cstmt.setString(2, strImei); 
         
         cstmt.setLong(3, strRepairId);                         
         cstmt.execute();
         
         intRepairid = cstmt.getString(1); 
      }
      catch (Exception e) {
         throw new Exception(e);
      }     
      finally{
            closeObjectsDatabase(conn,cstmt,null); 
      }
      
      hshData.put("intRepairid",intRepairid);
      
      return hshData;
   }
   
         
    
    /**
   * Motivo: Verfifica si el repuesto se encuentra en stock
   * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
   * <br>Fecha: 16/06/2009
   * @param     strValue
   * @return    HashMap 
   */
                
  public HashMap validateStock(long intFallaid, long  intSpecification , String strLogin) throws SQLException, Exception{       
   
      String strMessage=null;
      Connection conn = null;
      OracleCallableStatement cstmt = null;   
      ResultSet rs = null;
      String strValor = null;
      
      HashMap hshData = new HashMap(); //agregado
      
      String sqlStr = "BEGIN ORDERS.NP_REPAIR01_PKG.SP_VAL_STOCK(? , ? , ? , ?); END;";
            
      try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);      
      cstmt.setLong(1, intFallaid);
      cstmt.setLong(2, intSpecification);
      cstmt.setString(3, strLogin);
      
      cstmt.registerOutParameter(4, Types.CHAR);
      cstmt.execute();

      strMessage = cstmt.getString(4);

    }catch(Exception e){
         logger.error(formatException(e));
         throw new Exception(e);
      }finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
      
      hshData.put("strMessage", strMessage);
      
    return hshData;
  }                                   

  public HashMap validateSim(String strSim) throws SQLException, Exception{       
   
      String strMessage=null;
      String status_sim=null;
      Connection conn = null;
      OracleCallableStatement cstmt = null;   
      
      HashMap hshData = new HashMap(); //agregado
      
      String sqlStr = "BEGIN ORDERS.SPI_VALIDATE_SIM(? , ? , ?); END;";
            
      try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);      
      cstmt.setString(1, strSim);
      
      cstmt.registerOutParameter(2, Types.CHAR);
      cstmt.registerOutParameter(3, Types.CHAR);
      cstmt.execute();
      
      status_sim = cstmt.getString(2);
      strMessage = cstmt.getString(3);
      
      }catch (Exception e) {
         throw new Exception(e);
      }finally{
         closeObjectsDatabase(conn,cstmt,null); 
      }
      hshData.put("wc_status_sim",status_sim);
      hshData.put("wv_strMessage",strMessage);
      
      return hshData;

  }

public String getReplaceType(String orderId) throws SQLException, Exception{
    String strReplaceType = null;
    String strMessage = null;
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    
    String sqlStr = "BEGIN ORDERS.SPI_GET_REPLACETYPE(?, ?, ?); END;";

    try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setString(1, orderId);
      cstmt.registerOutParameter(2, Types.CHAR);
      cstmt.registerOutParameter(3, Types.CHAR);
      cstmt.execute();

      strReplaceType = cstmt.getString(2);
      strMessage = cstmt.getString(3);

    }catch(Exception e){
      logger.error(formatException(e));
      throw new Exception(e);
   }finally{
      closeObjectsDatabase(conn, cstmt, rs);
   }
    return strReplaceType;
  }

public String getValidateActiveEquipment(String imei, String sim, String replaceType) throws SQLException, Exception{
    String strActive = null;
    String strMessage = null;
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    
    String sqlStr = "BEGIN ORDERS.SPI_VALIDATE_ACTIVE_EQUIPMENT(?, ?, ?, ?, ?); END;";

    try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setString(1, imei);
      cstmt.setString(2, sim);
      cstmt.setString(3, replaceType);
      cstmt.registerOutParameter(4, Types.CHAR);
      cstmt.registerOutParameter(5, Types.CHAR);
      cstmt.execute();
      strActive = cstmt.getString(4);
      strMessage = cstmt.getString(5);
      System.out.println("SPI_VALIDATE_ACTIVE_EQUIPMENT strActive: "+strActive);
      
    }catch(Exception e){
      logger.error(formatException(e));
      throw new Exception(e);
   }finally{
      closeObjectsDatabase(conn, cstmt, rs);
   }
    return strActive;
  }

  public String getValidInternetNextel(long orderId) throws SQLException, Exception{
    String strShow = null;
    String strMessage = null;
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    System.out.println("Inicio RepairDAO getValidInternetNextel - orderId: "+orderId);
    String sqlStr = "BEGIN ORDERS.NP_REPAIR01_PKG.SP_SHOW_FINALSTATE_EQUIPSO(?, ?, ?); END;";

    try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, orderId);
      cstmt.registerOutParameter(2, Types.CHAR);
      cstmt.registerOutParameter(3, Types.CHAR);
      cstmt.execute();
      strShow = cstmt.getString(2);
      strMessage = cstmt.getString(3);
      System.out.println("SP_SHOW_FINALSTATE_EQUIPSO strShow: "+strShow);
      
    }catch(Exception e){
      logger.error(formatException(e));
      throw new Exception(e);
   }finally{
      closeObjectsDatabase(conn, cstmt, rs);
   }
    return strShow;
  }

	public HashMap getFinalEstateList() throws SQLException, Exception {
		HashMap hshDataMap = new HashMap();
		Connection conn = null; 
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;
		String strMessage = null;
		ArrayList arrDominioLista = new ArrayList();
		HashMap hshData=null;
		String strSql =	"BEGIN REPAIR.NP_GENERAL_PKG.SP_GET_FINAL_STATE_LST(?, ?); END;";
      try{
         conn = Proveedor.getConnection();          
         cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
         cstmt.registerOutParameter(1, OracleTypes.CURSOR);
         cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
         cstmt.execute();
         rs = (ResultSet)cstmt.getObject(1);
         strMessage = cstmt.getString(2);
         while(rs.next()) {
            hshData =new HashMap();       
            hshData.put("id",rs.getString(1));	
            hshData.put("valor",rs.getString(2));			
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

	public HashMap getEquipSOList() throws SQLException, Exception {
		HashMap hshDataMap = new HashMap();
		Connection conn = null; 
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;
		String strMessage = null;
		ArrayList arrDominioLista = new ArrayList();
		HashMap hshData=null;
		String strSql =	"BEGIN REPAIR.NP_GENERAL_PKG.SP_GET_EQUIP_SO_LST(?, ?); END;";
      try{
         conn = Proveedor.getConnection();          
         cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
         cstmt.registerOutParameter(1, OracleTypes.CURSOR);
         cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
         cstmt.execute();
         rs = (ResultSet)cstmt.getObject(1);
         strMessage = cstmt.getString(2);
         while(rs.next()) {
            hshData =new HashMap();       
            hshData.put("id",rs.getString(1));	
            hshData.put("valor",rs.getString(2));			
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
  
  public int validateChangeProcess(long orderId) throws SQLException, Exception{
    int iValidateChangeProcess = 0;
    String strMessage = null;
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    
    String sqlStr = "BEGIN ORDERS.NP_REPAIR01_PKG.SP_VALIDATE_CHANGEPROCESS(?, ?, ?); END;";

    try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, orderId);
      cstmt.registerOutParameter(2, Types.NUMERIC);
      cstmt.registerOutParameter(3, Types.VARCHAR);
      cstmt.execute();

      iValidateChangeProcess = cstmt.getInt(2);
      strMessage = cstmt.getString(3);

    }catch(Exception e){
      logger.error(formatException(e));
      throw new Exception(e);
   }finally{
      closeObjectsDatabase(conn, cstmt, rs);
   }
    return iValidateChangeProcess;
  }
  
  public String getTechnologyByImei(String imei) throws SQLException, Exception{
    String strTechnology = "";
    String strMessage = null;
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    System.out.println("RepairDAO getTechnologyByImei imei: "+imei);
    String sqlStr = "BEGIN ORDERS.NP_REPAIR01_PKG.SP_GET_TECHNOLOGY_BY_IMEI(?, ?, ?); END;";

    try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setString(1, imei);
      cstmt.registerOutParameter(2, Types.VARCHAR);
      cstmt.registerOutParameter(3, Types.VARCHAR);
      cstmt.execute();

      strTechnology = cstmt.getString(2);
      strMessage = cstmt.getString(3);
      System.out.println("strTechnology: "+strTechnology);
      System.out.println("strMessage: "+strMessage);
    }catch(Exception e){
      logger.error(formatException(e));
      throw new Exception(e);
   }finally{
      closeObjectsDatabase(conn, cstmt, rs);
   }
    return strTechnology;
  }
  
  
  /**
   * Motivo: Metodo que valida el combo de procesos en la orden S&S
   * <br>Realizado por: <a href="mailto:julio.herrera@hp.pe">Julio Herrera</a>
   * <br>Fecha: 15/03/2009
   * @param     strValue
   * @param     conn
   * @return    HashMap
   */
  public HashMap validateSpecOrders(HashMap hshParameters) throws SQLException, Exception {
    HashMap hshResult = new HashMap();
    String strMessage = null;
    OracleCallableStatement cstmt = null;
    Connection conn=null;

    String sqlStr = "BEGIN ORDERS.NP_REPAIR01_PKG.SP_SPEC_VALIDATE_PROCESS(?, ?, ?, ?, ?); END;";
    
    System.out.println("");
            
    String strPhoneNumber = (String) hshParameters.get("strPhoneNumber");
    System.out.println("[REPAIRDAO - validateSpecProcess - strPhoneNumber : "+strPhoneNumber+"]");
    
    String strProcess = (String) hshParameters.get("strProcess");
    System.out.println("[REPAIRDAO - validateSpecProcess - strProcess : "+strProcess+"]");
    
    String strOldProcess = (String) hshParameters.get("strOldProcess");
    System.out.println("[REPAIRDAO - validateSpecProcess - strOldProcess : "+strOldProcess+"]");
    
    String strOrderID = (String) hshParameters.get("strOrderID");
    int istrOrderID = Integer.parseInt(strOrderID);
    System.out.println("[REPAIRDAO - validateSpecProcess - istrOrderID : "+istrOrderID+"]");
    
    System.out.println("");
    
    String strMessageOrders;

    try {
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

      cstmt.setString(1, strPhoneNumber);
      cstmt.setString(2, strProcess);
      cstmt.setInt(3, istrOrderID);
      cstmt.registerOutParameter(4, Types.CHAR); //av_mensaje_ordenes_previas
      cstmt.registerOutParameter(5, Types.CHAR); //av_message

      cstmt.execute();

      strMessageOrders = cstmt.getString(4);
      strMessage = cstmt.getString(5);
      
      if (!StringUtils.isNotBlank(strMessage)) {
        strMessageOrders = cstmt.getString(4);      
        if (StringUtils.isNotBlank(strMessageOrders)) {
          strMessage = strMessageOrders;
        }        
      }
    } catch (Exception e) {
      logger.error(formatException(e));
      throw new Exception(e);
    } finally {
      closeObjectsDatabase(conn, cstmt, null);
    }    
    hshResult.put("strMessageOrders", strMessageOrders);
    hshResult.put("strMessage", strMessage);
    hshResult.put("strOldProcess", strOldProcess);    
    return hshResult;
  }
  
  /**
   * Motivo: Metodo que ejecuta el api de liberación y asociación de nacionalización
   * <br>Realizado por: <a href="mailto:julio.herrera@hp.pe">Julio Herrera</a>
   * <br>Fecha: 12/11/2010
   * @param     strValue
   * @param     conn
   * @return    HashMap
   */
  public HashMap generateNacionalizacion(HashMap hshParameters) throws SQLException, Exception {
    HashMap hshResult = new HashMap();
    String strMessage = null;
    OracleCallableStatement cstmt = null;
    Connection conn=null;

    String sqlStr = "BEGIN WEBCCARE.SPI_EXEC_NACIONALIZACION(?, ?, ?); END;";
    
    String strImei = (String) hshParameters.get("strImei");
    String strSim = (String) hshParameters.get("strSim");
    System.out.println("");
    System.out.println("[REPAIRDAO - generateNacionalizacion - strImei : "+strImei+"]");
    System.out.println("[REPAIRDAO - generateNacionalizacion - strSim : "+strSim+"]");
    System.out.println("");
    
    try {
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

      cstmt.setString(1, strImei);
      cstmt.setString(2, strSim);
      cstmt.registerOutParameter(3, Types.CHAR); //av_message
      
      cstmt.execute();

      strMessage = cstmt.getString(3);
      System.out.println("strMessage : "+strMessage);
      
    } catch (Exception e) {
      logger.error(formatException(e));
      throw new Exception(e);
    } finally {
      closeObjectsDatabase(conn, cstmt, null);
    }        
    hshResult.put("strMessage", strMessage);
    return hshResult;
  }
  
  /**
   * Motivo: Metodo que ejecuta el api de generación de archivo RA y envío de correos
   * <br>Realizado por: <a href="mailto:julio.herrera@hp.pe">Julio Herrera</a>
   * <br>Fecha: 12/11/2010
   * @param     strValue
   * @param     conn
   * @return    HashMap
   */
  public HashMap generateRAFile(HashMap hshParameters) throws SQLException, Exception {
    HashMap hshResult = new HashMap();
    String strMessage = null;
    OracleCallableStatement cstmt = null;
    Connection conn=null;

    String sqlStr = "BEGIN WEBCCARE.SPI_GENERATE_RA_FILE(?, ?); END;";
    
    String strOrderID = (String) hshParameters.get("strOrderID");
    System.out.println("");
    System.out.println("[REPAIRDAO - generateRAFile - strOrderID : "+strOrderID+"]");
    System.out.println("");
    
    try {
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

      cstmt.setString(1, strOrderID);
      cstmt.registerOutParameter(2, Types.CHAR); //av_message
      
      cstmt.execute();

      strMessage = cstmt.getString(2);
      System.out.println("strMessage : "+strMessage);
      
    } catch (Exception e) {
      logger.error(formatException(e));
      throw new Exception(e);
    } finally {
      closeObjectsDatabase(conn, cstmt, null);
    }        
    hshResult.put("strMessage", strMessage);
    return hshResult;
  }
  
  /**
   * Motivo: Metodo que ejecuta el api que devuelve el id del modelo
   * <br>Realizado por: <a href="mailto:miguel.montoya@hp.pe">Miguel Ángel Montoya</a>
   * <br>Fecha: 11/07/2012
   * @param     modelName  
   * @return    HashMap
   */
  public HashMap getBscsModelId(String modelName) throws Exception {   
    HashMap hshResult = new HashMap();  
    String strModelId = null;
    String strMessage = null;
    OracleCallableStatement cstmt = null;
    Connection conn=null;

    String sqlStr = "BEGIN WEBSALES.SPI_GET_MODELS_E_FROM_BSCS(?, ?, ?); END;";
        
    System.out.println("");
    System.out.println("[REPAIRDAO - getBscsModelId - modelName : "+modelName+"]");
    System.out.println("");
    
    try {
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

      cstmt.setString(1, modelName);
      cstmt.registerOutParameter(2, Types.CHAR);
      cstmt.registerOutParameter(3, Types.CHAR); //av_message
      
      cstmt.execute();

      strModelId = cstmt.getString(2);
      strMessage = cstmt.getString(3);
      System.out.println("strMessage : " + strMessage);
      
    } catch (Exception e) {
      logger.error(formatException(e));
      throw new Exception(e);
    } finally {
      closeObjectsDatabase(conn, cstmt, null);
    }
    
    hshResult.put("strModelId", strModelId);
    hshResult.put("strMessage", strMessage);
    return hshResult;
  }
  
    /**
   * Motivo: Metodo que ejecuta el api que verifica si existe un modelo
   * <br>Realizado por: <a href="mailto:miguel.montoya@hp.pe">Miguel Ángel Montoya</a>
   * <br>Fecha: 11/07/2012
   * @param     modelName  
   * @return    HashMap
   */
  public HashMap existsImei(String imei) throws Exception {   
    HashMap hshResult = new HashMap();  
    boolean existsImei = false;
    String strMessage = null;
    OracleCallableStatement cstmt = null;
    Connection conn=null;

    String sqlStr = "BEGIN WEBSALES.SPI_VALIDATE_IMEI_FROM_BSCS(?, ?, ?); END;";
        
    System.out.println("");
    System.out.println("[REPAIRDAO - existsImei - imei : "+imei+"]");
    System.out.println("");
    
    try {
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

      cstmt.setString(1, imei);
      cstmt.registerOutParameter(2, Types.NUMERIC);
      cstmt.registerOutParameter(3, Types.CHAR); //av_message

      cstmt.execute();

      existsImei = cstmt.getNUMBER(2).intValue() == 1;
      strMessage = cstmt.getString(3);
      System.out.println("strMessage : " + strMessage);
      
    } catch (Exception e) {
      logger.error(formatException(e));
      throw new Exception(e);
    } finally {
      closeObjectsDatabase(conn, cstmt, null);
    }
    
    hshResult.put("existsImei", Boolean.valueOf(existsImei));
    hshResult.put("strMessage", strMessage);
    return hshResult;
  }
  
  /**
   * Motivo: Obtiene lista de modelos segun el product line del modelo dado.
   * <br>Realizado por: <a href="mailto:miguel.montoya@hp.pe">Miguel Ángel</a>
   * <br>Fecha: 10/03/2009
   * @param     strValue
   * @return    HashMap 
   */
   public HashMap getModels(long orderId) throws SQLException, Exception{
      ArrayList list = new ArrayList();
      HashMap hshData=new HashMap();
      String strMessage=null;
      Connection conn = null;
      OracleCallableStatement cstmt = null;

      String sqlStr = "BEGIN ORDERS.SPI_GET_MODEL_LST (?, ?, ?); END;";
   
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         
         cstmt.setLong(1, orderId);         
         cstmt.registerOutParameter(2, OracleTypes.ARRAY, "PRODUCT.TT_PRODUCT_LST");
         cstmt.registerOutParameter(3, Types.CHAR);
         
         cstmt.execute();
                  
         strMessage = cstmt.getString(3);

         ARRAY aryProductList = (ARRAY)cstmt.getObject(2);
         OracleResultSet adrs = (OracleResultSet) aryProductList.getResultSet();
         logger.debug("LENGTH: "+aryProductList.getOracleArray().length);
         
         while(adrs.next()) {
            STRUCT stcProductPrice = adrs.getSTRUCT(2);            
            DominioBean dominio = new DominioBean();          

            dominio.setValor(StringUtils.defaultString(stcProductPrice.getAttributes()[0].toString()));
            dominio.setDescripcion(StringUtils.defaultString(stcProductPrice.getAttributes()[2].toString()));
            list.add(dominio);
         }
      }catch(Exception e){
         logger.error(formatException(e));
         throw new Exception(e);
      }finally{
         closeObjectsDatabase(conn, cstmt, null);
      }
      
      hshData.put("strMessage",strMessage);
      hshData.put("arrListado",list);
      return hshData;
   }
  
    /**
    * Motivo: Obtiene el Tipo de Plan (Prepago / Postago)
    * <br>Realizado por: <a href="mailto:yimy.ruiz@asistp.com">Yimy Ruiz</a>
    * <br>Fecha: 21/01/2014
    * @param     String  strPhone
    * @param     String  strImei
    * @return    String  strPlanType
    */   
    public String getPlanType(String strPhone, String strImei) 
    throws SQLException, Exception
    {
       String strPlanType=null;
       String strMessage=null;
       Connection conn=null;
       OracleCallableStatement cstmt = null;      
       
       String sqlStr = " { call WEBCCARE.NPAC_SERVICIOTECNICO_PKG.SP_GET_PLAN_TYPE(?,?,?,?) } "; 
       try{
          conn = Proveedor.getConnection();
          cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
          
          cstmt.setString(1, strPhone); 
          cstmt.setString(2, strImei); 
          cstmt.registerOutParameter(3, Types.VARCHAR);
          cstmt.registerOutParameter(4, Types.VARCHAR);  
          cstmt.execute();
          
          strPlanType = cstmt.getString(3); 
          strMessage = cstmt.getString(4); 
           
          if (strMessage != null &&  !"".equals(strMessage))
          {
              //System.out.println("RepairDAO.getPlanType() Error:" + strMessage);
              throw new Exception(strMessage);
          }
       }
       catch (Exception e) {
          //System.out.println("RepairDAO.getPlanType() Error:" + e.getMessage());
          throw new Exception(e);
       }     
       finally{
          closeObjectsDatabase(conn,cstmt,null); 
       }
       return strPlanType;
    } 
    
    /**
    * Motivo: Obtener una lista de la tabla REPAIR.NP_CONFIGURATION
    * <br>Realizado por: carlos.delossantos@teamsoft.com.pe
    * <br>Fecha: 19/03/2014
    * @param     String        
    * @return    HashMap 
    */   
    public  HashMap getConfigurations(String strParam) throws SQLException, Exception{
       
       OracleCallableStatement cstmt = null;        
       String strMessage=null;
       Connection conn=null;
       ResultSet rs = null;
       HashMap hshDataMap=new HashMap();
       ArrayList arrDominioLista = new ArrayList();
       DominioBean dominio = null;
       
       String strSql = 
       "BEGIN REPAIR.SPI_GET_CONFIGURATION( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); END;";
       try{                                                
          conn = Proveedor.getConnection();
          cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
          cstmt.setString(1, strParam );
           cstmt.setInt(2, 1 );
           cstmt.setInt(3, 1 );
           cstmt.setString(4, "V1" );
           cstmt.setString(5, "V2" );
           cstmt.setString(6, "V1" );
           cstmt.setString(7, "1" );
           
          cstmt.registerOutParameter(8, OracleTypes.VARCHAR);
          cstmt.registerOutParameter(9, OracleTypes.CURSOR);
          cstmt.registerOutParameter(10, OracleTypes.VARCHAR);
          cstmt.executeUpdate();
           
          rs = (ResultSet)cstmt.getObject(9);
          strMessage = cstmt.getString(10);
           
           while(rs.next()) {
              dominio = new DominioBean();
              dominio.setValor(rs.getString(2));
              dominio.setDescripcion(rs.getString(1));
              arrDominioLista.add(dominio);
           }
           
       }
       catch (Exception e) {
          throw new Exception(e);
       }
       finally{
          closeObjectsDatabase(conn,cstmt,null); 
       }
       hshDataMap.put("strMessage",strMessage);
       hshDataMap.put("arrListado", arrDominioLista);
       return hshDataMap;
    }
    
    public HashMap getComboTiendaList()  throws Exception,SQLException {
       HashMap objHashMap = new HashMap();
       ArrayList list = new ArrayList();
       Connection conn = null; 
       ResultSet rs=null;
       String strMessage  = null;
       OracleCallableStatement cstm = null;     
       FormatBean objFormatBean = null;
       ArrayList objApplication = null;
       String sqlStr = "BEGIN ORDERS.SPI_GET_BUILDING_LIST(?,?); END;";
       try{
          conn = Proveedor.getConnection();          
          cstm = (OracleCallableStatement) conn.prepareCall(sqlStr);                   
          cstm.registerOutParameter(1,OracleTypes.CURSOR);
          cstm.registerOutParameter(2, Types.VARCHAR);         
          cstm.executeUpdate();
          rs = (ResultSet)cstm.getObject(1);
          strMessage = cstm.getString(2);    

           objApplication = new ArrayList();
           while (rs.next()) {
              objFormatBean = new FormatBean();
              objFormatBean.setNpformname(rs.getString(1));
              objFormatBean.setNptemplatename(rs.getString(2)==null?"":rs.getString(2));
              objApplication.add(objFormatBean);               
           }
        }                    
        catch(Exception e){
           throw new Exception(e);
        }
        finally{
                        closeObjectsDatabase(conn, cstm, rs);
        }          
        objHashMap.put("strMessage",strMessage);
        objHashMap.put("objTiendaList",objApplication);
     
       return objHashMap;
                                                                       
    }        
     
    public HashMap getDataLoanForReport(String repairId) throws SQLException, Exception {
                            HashMap hshRepairReportMap = new HashMap();
                            String strMessage="";
                            Connection conn = null;
                            List<String> listAccesorry1=new ArrayList<String>(); 
                            List<String> listAccesorry2=new ArrayList<String>(); 
                            List<String> listFails=new ArrayList<String>(); 
                            OracleCallableStatement cstmt = null;
                            ResultSet rs = null;
                            //ORDERS.NP_REPORT_INSTALACION_PKG.SP_GET_REPORT_ENVIO_CLAIM_LIST(wv_report_name,'', '','','',an_repairid,'','',wc_list,wv_message);
                            String sqlStr = "BEGIN REPAIR.NP_REPAIR03_PKG.SP_GET_DETAIL_REPAIR_REPORT(?, ?, ?, ?, ?, ?); END;";
                  System.out.println("RepairDAO.getDataLoanForReport() repairId:" + repairId);
          try{
             conn = Proveedor.getConnection();
             cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
             int i = 0;
             cstmt.setInt(++i, Integer.parseInt(repairId==null?"0":repairId));
             cstmt.registerOutParameter(++i, OracleTypes.CURSOR);
             cstmt.registerOutParameter(++i, OracleTypes.CURSOR);
             cstmt.registerOutParameter(++i, OracleTypes.CURSOR);
             cstmt.registerOutParameter(++i, OracleTypes.CURSOR);
             cstmt.registerOutParameter(++i, OracleTypes.VARCHAR);
             cstmt.executeQuery();
             strMessage = cstmt.getString(i);
              if (strMessage != null &&  !"".equals(strMessage))
              {
                  System.out.println("RepairDAO.getDataLoanForReport() Error:" + strMessage);
                  throw new Exception(strMessage);
              }
             rs = (ResultSet)cstmt.getObject(--i);
             while(rs.next()) {
                listFails.add(StringUtils.defaultString(rs.getString(1),""));
             }  
             rs = (ResultSet)cstmt.getObject(--i);
             while(rs.next()) {
                listAccesorry2.add(StringUtils.defaultString(rs.getString(1),""));
             }  
             rs = (ResultSet)cstmt.getObject(--i);
             while(rs.next()) {
                listAccesorry1.add(StringUtils.defaultString(rs.getString(1),""));
             }  
             rs = (ResultSet)cstmt.getObject(--i);
             if(rs.next()) {
                i = 1;
                for (int x=1;x<=rs.getMetaData().getColumnCount();x++){
                    String columna=StringUtils.defaultString(rs.getMetaData().getColumnName(x),"") ,value=StringUtils.defaultString(rs.getString(i++),"");
                    hshRepairReportMap.put(columna, value);
                    System.out.println("COLUMNA: "+columna);
                    System.out.println("VALUE: "+value);
                }
             }      
          }
          catch (Exception e) {
              e.printStackTrace();
          }
          finally{
            closeObjectsDatabase(conn,cstmt,rs);
            hshRepairReportMap.put("resultlistAccessory1", listAccesorry1);
            hshRepairReportMap.put("resultlistAccessory2", listAccesorry2);
            hshRepairReportMap.put("resultlistFails", listFails);
            hshRepairReportMap.put(Constante.MESSAGE_OUTPUT, strMessage);
          }
            return hshRepairReportMap;
       }  
     
    public HashMap getDamageList(String repairListId, String model) throws SQLException, Exception {
                            HashMap hshRepairReportMap = new HashMap();
                            List<EquipmentDamage> listDamage = new ArrayList<EquipmentDamage>();
                            String strMessage="", imagen="";
                            Connection conn = null;
                            OracleCallableStatement cstmt = null;
                            ResultSet rs = null;
                            EquipmentDamage damage=null;
                            String sqlStr = "BEGIN REPAIR.NP_REPAIR03_PKG.SP_GET_REPAIR_DAMAGE(?, ?, ?, ?, ?); END;";
                System.out.println("getDamageList.repairListId: "+repairListId);
                int repair_id=Integer.parseInt(repairListId==null || repairListId.equals("")?"0":repairListId);
                System.out.println("getDamageList.repair_id: "+repair_id);
                System.out.println("getDamageList.model: "+model);
          try{
             conn = Proveedor.getConnection();
             cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
             int i = 0;
             cstmt.setInt(++i, repair_id);
             cstmt.setString(++i, model);
             cstmt.registerOutParameter(++i, OracleTypes.CURSOR);
             cstmt.registerOutParameter(++i, OracleTypes.VARCHAR);
             cstmt.registerOutParameter(++i, OracleTypes.VARCHAR);
             cstmt.executeQuery();
             strMessage = cstmt.getString(i);
              if (strMessage != null &&  !"".equals(strMessage))
              {
                  System.out.println("RepairDAO.getDamageList() Error:" + strMessage);
                  throw new Exception(strMessage);
              }
             imagen = cstmt.getString(--i);
             rs = (ResultSet)cstmt.getObject(--i);
             while(rs.next()) {
                i = 1;
                damage=new EquipmentDamage();
                damage.setNprow(StringUtils.defaultString(rs.getString(i++),""));
                damage.setNpcolumn(StringUtils.defaultString(rs.getString(i++),""));
                damage.setNpdamagecode(StringUtils.defaultString(rs.getString(i++),""));
                listDamage.add(damage);
             }    
              
          }
          catch (Exception e) {
              e.printStackTrace();
          }
          finally{
             closeObjectsDatabase(conn,cstmt,rs);
            hshRepairReportMap.put("resultlist", listDamage);
            hshRepairReportMap.put(Constante.MESSAGE_OUTPUT, strMessage);
            hshRepairReportMap.put("imagen_equipo", imagen);
          }
        return hshRepairReportMap;
       }
    
     /**
      * Motivo: Obtiene una lista de los centros de Reparaciones
      * <br>Realizado por: <a href="mailto:paolo.ortega@teamsoft.com.pe">Paolo Ortega Ramirez</a>
      * <br>Fecha: 20/05/2014
      * @return    HashMap
      */
    public HashMap getCentroReparacion() throws SQLException, Exception {
        ArrayList list = new ArrayList();
        String strMessage = null;
        Connection conn = null;
        HashMap hspMap = new HashMap();
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        int i = 1;
        String sqlStr = "BEGIN REPAIR.NP_GENERAL_PKG.SP_GET_CENTRO_REP_LST(?, ?); END;";
        
        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.execute();

            strMessage = cstmt.getString(2);
            
            if (StringUtils.isBlank(strMessage)) {
                rs = (ResultSet)cstmt.getObject(1);

                while (rs.next()) {
                    DominioBean dominio = new DominioBean();

                    dominio.setValor(rs.getString(1));
                    dominio.setDescripcion(rs.getString(2));

                    list.add(dominio);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(formatException(e));
            throw new Exception(e);
        } finally {
            closeObjectsDatabase(conn, cstmt, rs);
        }
        hspMap.put("arrListado", list);
        hspMap.put("strMessage", strMessage);
        return hspMap;
    }
     
    public int getCantReparaciones( String strIMEI) throws SQLException, Exception {
        ArrayList list = new ArrayList();
        String strMessage = null;
        Connection conn = null;
        int cant = 0;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        int i = 1;
        String sqlStr = " { ? = call ORDERS.FN_GET_COUNT_REPAIR(?) } ";
        //System.out.println("getCantReparaciones: strIMEI: "+strIMEI);
        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.registerOutParameter(1, OracleTypes.NUMBER);
            cstmt.setString(2, strIMEI );
            cstmt.execute();

            cant = cstmt.getInt(1);
            
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(formatException(e));
            throw new Exception(e);
        } finally {
            closeObjectsDatabase(conn, cstmt, rs);
        }
        //System.out.println("cant: "+cant);
        
        return cant;
    }
    
    /**
     * Motivo: Obtiene lista de otras fallas
     * <br>Realizado por: <a href="mailto:paolo.ortega@teamsoft.com.ep">Paolo Ortega</a>
     * <br>Fecha: 27/05/2013
     * @param     strValue
     * @return    HashMap
     */
    public HashMap getOtherFailureList(int intRepairId, String strRepairTypeId) throws SQLException, Exception {
        ArrayList list = new ArrayList();
        HashMap hshData = new HashMap();
        String strMessage = null;
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        int i=0;
        System.out.println("Inicio RepairDAO getFailureList...");
        System.out.println("Datos: " + intRepairId + "-" + strRepairTypeId);
        String sqlStr = "BEGIN REPAIR.SPI_GET_OTHER_FAILURE_LIST(?, ?, ?, ?, ?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(++i, intRepairId);
            cstmt.setString(++i, strRepairTypeId);
            cstmt.setLong(++i, 1); //1=ordern cliente, 2=orden reparacion
            cstmt.registerOutParameter(++i, OracleTypes.CURSOR);
            cstmt.registerOutParameter(++i, Types.CHAR);
            cstmt.execute();
            strMessage = cstmt.getString(i);

            if (!StringUtils.isNotBlank(strMessage)) {
                rs = (ResultSet)cstmt.getObject(--i);

                while (rs.next()) {
                    DominioBean dominio = new DominioBean();
                    dominio.setValor(StringUtils.defaultString(rs.getString(2)));
                    dominio.setDescripcion(StringUtils.defaultString(rs.getString(1)));
                    list.add(dominio);
                }
            }
        } catch (Exception e) {
            logger.error(formatException(e));
            throw new Exception(e);
        } finally {
            closeObjectsDatabase(conn, cstmt, rs);
        }
        hshData.put("strMessage", strMessage);
        hshData.put("arrListado", list);
        System.out.println("Fin RepairDAO getFailureList...");
        return hshData;
    }
    
    /**
     * Motivo: Obtiene lista de Otras fallas seleccionadas para una reparacion
     * <br>Realizado por: <a href="mailto:paolo.ortega@teamsoft.com.ep">Paolo Ortega</a>
     * <br>Fecha: 27/05/2013
     * @param     
     * @return    HashMap
     */
    public HashMap getSelectedOtherFailureList(int intRepairId, String strRepairTypeId) throws SQLException, Exception {
        ArrayList list = new ArrayList();
        HashMap hshData = new HashMap();
        String strMessage = null;
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        int i=0;
        System.out.println("getSelectedOtherFailureList: intRepairId: "+intRepairId+", strRepairTypeId:"+strRepairTypeId);
        String sqlStr = "BEGIN REPAIR.SPI_GET_REPAIR_OTHER_FAIL_LIST(?, ?, ?, ?, ?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setInt(++i, intRepairId);
            cstmt.setString(++i, strRepairTypeId);
            cstmt.setLong(++i, 1); //1=ordern cliente, 2=orden reparacion
            cstmt.registerOutParameter(++i, OracleTypes.CURSOR);
            cstmt.registerOutParameter(++i, Types.CHAR);
            cstmt.execute();

            strMessage = cstmt.getString(i);

            if (!StringUtils.isNotBlank(strMessage)) {
                rs = (ResultSet)cstmt.getObject(--i);

                while (rs.next()) {
                    DominioBean dominio = new DominioBean();
                    dominio.setValor(StringUtils.defaultString(rs.getString(2)));
                    dominio.setDescripcion(StringUtils.defaultString(rs.getString(1)));
                    list.add(dominio);
                }
            }
        } catch (Exception e) {
            logger.error(formatException(e));
            throw new Exception(e);
        } finally {
            closeObjectsDatabase(conn, cstmt, rs);
        }
        hshData.put("strMessage", strMessage);
        hshData.put("arrListado", list);
        return hshData;
    }
    
}