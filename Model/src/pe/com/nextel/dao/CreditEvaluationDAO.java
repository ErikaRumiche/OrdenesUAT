package pe.com.nextel.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import pe.com.nextel.bean.CreditEvaluationBean;
import pe.com.nextel.bean.ItemBean;
import pe.com.nextel.bean.ItemServiceBean;
import pe.com.nextel.bean.OrderBean;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;


public class CreditEvaluationDAO extends GenericDAO {

  /**
  * Motivo: Obteniene el detalle de una evaluacion Crediticia
  * <br>Realizado por: <a href="mailto:jose.casas@nextel.com.pe">José Casas</a>
  * <br>Fecha: 05/06/2009
  * @param     lSourceId  Ej. 27300
  * @param     strSource  Ej. "ORDEN"
  * @return    HashMap 
  */
  public  HashMap getCreditEvaluationData(long lSourceId, String strSource) throws SQLException {
    CreditEvaluationBean objCreditEvaluationBean = new CreditEvaluationBean();        
    String strMessage=null;
    HashMap hshData=new HashMap();
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    Connection conn=null;
    try{
      String sqlStr =  "BEGIN CREDIT.SPI_GET_CREDIT_EVALUATION_DET(?, ?, ?, ?); END;";
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, lSourceId);
      cstmt.setString(2, strSource);
      cstmt.registerOutParameter(3, OracleTypes.CURSOR);
      cstmt.registerOutParameter(4, Types.CHAR);
      cstmt.execute();
      strMessage = cstmt.getString(4);
      System.out.println("[getCreditEvaluationData]: " + strMessage);
      if( strMessage == null ){
        rs = cstmt.getCursor(3);
        if (rs.next()) {
          objCreditEvaluationBean.setnpCreditEvaluationId(rs.getLong("npcreditevaluationid"));
          objCreditEvaluationBean.setnpGuarantee(rs.getDouble("npguarantee"));
          objCreditEvaluationBean.setnpGuaranteeCurrency(rs.getString("npguaranteecurrency"));
            //objCustomerBean.setNpTipoDoc(rs.getString("nptipodoc"));
        }
      }
      hshData.put("objCreditEvaluationBean",objCreditEvaluationBean);
      hshData.put("strMessage",strMessage);  
    } catch (Exception e) {          
      logger.error(formatException(e));
      hshData.put("strMessage",e.getMessage());  
    } finally{
      try{
        closeObjectsDatabase(conn, cstmt, rs);  
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }
    return hshData;
  }
  
  /**
  * Motivo: Obteniene el detalle del cliente dado el CustomerId
  * <br>Realizado por: <a href="mailto:jose.casas@nextel.com.pe">José Casas</a>
  * <br>Fecha: 05/06/2009
  * @param     lCreditEvaluationId  Ej. 27300
  * @param     strSource  Ej. "20" - Orden
  * @return    HashMap 
  */
  public HashMap getRuleResult(long lCreditEvaluationId, String strSource) throws SQLException {    
    ArrayList list = new ArrayList();        
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    HashMap hshData=new HashMap();
    Connection conn=null;
    String strMessage=null;
    try{
      String sqlStr = "BEGIN CREDIT.SPI_GET_RULE_RESULT(?, ?, ?, ?); END;";
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, lCreditEvaluationId);
      cstmt.setString(2, strSource);
      cstmt.registerOutParameter(3, OracleTypes.CURSOR);
      cstmt.registerOutParameter(4, Types.VARCHAR);
      cstmt.executeUpdate();        
      strMessage = cstmt.getString(4);
      if (strMessage ==null){
        rs = (ResultSet)cstmt.getObject(3);
        while (rs.next()) {  
          HashMap h = new HashMap();
          h.put("npdescription",  rs.getString(1));
          h.put("npcomments",rs.getString(2));          
          list.add(h);
        }
      }
      hshData.put("strMessage",strMessage);
      hshData.put("arrRuleResult",list);
    } catch (Exception e) {
      System.out.println("JL:" + e.getMessage()+"\n");
      logger.error(formatException(e));
      hshData.put("strMessage",e.getMessage());
    } finally {
      try{
        closeObjectsDatabase(conn, cstmt, rs);  
      } catch (Exception e) {
        logger.error(formatException(e));
      }
    }
    return hshData;
  }
  

  /**
  * Motivo: Realiza la Evaluación Crediticia de una Orden
  * <br>Realizado por: <a href="mailto:jose.casas@nextel.com.pe">José Casas</a>
  * <br>Fecha: 09/06/2009
  * @param     lSourceId  Ej. 27300
  * @param     strSource  Ej. "ORDEN"
  * @return    HashMap 
  */
  public  HashMap doEvaluateOrder(HashMap hshData) throws SQLException {
    logger.info("****************** INICIO CreditEvaluationDAO > doEvaluateOrder(HashMap hshData)******************");
    String strMessage=null;    
    OracleCallableStatement cstmt = null;    
    Connection conn=null;
    OrderBean objOrderBean = new OrderBean();
    OrderDAO objOrderDao = new OrderDAO();
    ItemBean objItemBean = new ItemBean();
    ItemServiceBean objItemServiceBean = new ItemServiceBean();
    ArrayList arrItemDataList = new ArrayList();
    ArrayList arrItemServiceDataList = new ArrayList();
    HashMap hshItemData = new HashMap();
    int npProviderGrpId = 0;
    int npsalesstructid = 0;
    //INICIO: PRY-1062_3168 | AMENDEZ
    double nppaymenttotal = 0.00;
    //FIN: PRY-1062_3168 | AMENDEZ
    
    try{
      String sqlStr =  "BEGIN CREDIT.SPI_CREDIT_EVALUATE_ORDER(?, ?); END;";
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      
      objOrderBean = (OrderBean) hshData.get("orderBean");
      logger.info("objOrderBean.toString(): "+objOrderBean.toString());
      arrItemDataList = (ArrayList) hshData.get("arrItems");
      
      ArrayList arrItemList = new ArrayList();
      ArrayList arrItemServiceList = new ArrayList();

      StructDescriptor sdItem = StructDescriptor.createDescriptor("CREDIT.TO_CREDIT_ITEM", conn);
      ArrayDescriptor adItemList = ArrayDescriptor.createDescriptor("CREDIT.TT_CREDIT_ITEM_LST", conn);
      
      StructDescriptor sdItemService = StructDescriptor.createDescriptor("CREDIT.TO_CREDIT_ITEM_SERVICE", conn);
      ArrayDescriptor adItemServiceList = ArrayDescriptor.createDescriptor("CREDIT.TT_CREDIT_ITEM_SERVICE_LST", conn);
      logger.info("arrItemDataList.size() "+arrItemDataList.size());
      for (int i = 0; i < arrItemDataList.size(); i++) {
      arrItemServiceList = new ArrayList();
        hshItemData = (HashMap)arrItemDataList.get(i);
        objItemBean = (ItemBean) hshItemData.get("itemBean");        
        
        arrItemServiceDataList = (ArrayList)hshItemData.get("arrItemsServices");
        logger.info("arrItemServiceDataList.size() "+arrItemServiceDataList.size());
        for (int y = 0; y < arrItemServiceDataList.size(); y++) {
          objItemServiceBean = (ItemServiceBean)arrItemServiceDataList.get(y);

          logger.info("objItemServiceBean.getNpserviceid() "+objItemServiceBean.getNpserviceid());
          logger.info("objItemServiceBean.getNpaction() "+objItemServiceBean.getNpaction());
          
          Object[] objItemService = {
                    String.valueOf(objItemServiceBean.getNpserviceid()),
                    objItemServiceBean.getNpaction()
                
          }; 
          STRUCT stcItemService = new STRUCT(sdItemService, conn, objItemService);
          arrItemServiceList.add(stcItemService);
        }        
        ARRAY aryItemServiceList = new ARRAY(adItemServiceList, conn, arrItemServiceList.toArray());
        logger.info("Fin Servicios");

        logger.info("objItemBean.getNpsolutionid()     : "+objItemBean.getNpsolutionid());
        logger.info("objItemBean.getNpmodalitysell()   : "+objItemBean.getNpmodalitysell());
        logger.info("objItemBean.getNpplanid()         : "+objItemBean.getNpplanid());
        logger.info("objItemBean.getNporiginalplanid() : "+objItemBean.getNporiginalplanid());
        logger.info("objItemBean.getNpproductid()      : "+objItemBean.getNpproductid());
        logger.info("objItemBean.getNpquantity()       : "+objItemBean.getNpquantity());
        logger.info("objItemBean.getNpphone()          : "+objItemBean.getNpphone());
        logger.info("objItemBean.getNpprice()          : "+objItemBean.getNpprice());
        logger.info("objItemBean.getNppriceexception() : "+objItemBean.getNppriceexception());
        
        double dprice = 0;
        if (objItemBean.getNppriceexception() != null ) {
          dprice = MiUtil.parseDouble(objItemBean.getNppriceexception());
        }else {
          dprice = MiUtil.parseDouble(objItemBean.getNpprice()); 
        }
        logger.info("dprice : "+dprice);
        //INICIO: PRY-1062_3168 | AMENDEZ
        nppaymenttotal=nppaymenttotal+dprice;
        //FIN: PRY-1062_3168 | AMENDEZ
        Object[] objItem = {
                  String.valueOf(objItemBean.getNpsolutionid()),
                  objItemBean.getNpmodalitysell(),
                  String.valueOf(objItemBean.getNpplanid()),
                  String.valueOf(objItemBean.getNporiginalplanid()),
                  String.valueOf(objItemBean.getNpproductid()),
                  String.valueOf(objItemBean.getNpquantity()),
                  String.valueOf(dprice),
                  String.valueOf(objItemBean.getNpphone()),
                  aryItemServiceList
                };
        STRUCT stcItem = new STRUCT(sdItem, conn, objItem);
        arrItemList.add(stcItem);
      }
      logger.info("Fin Items");
      ARRAY aryItemList = new ARRAY(adItemList, conn, arrItemList.toArray());

      npProviderGrpId = objOrderBean.getNpProviderGrpId();
      
      try{
        npsalesstructid = objOrderDao.getSalesStructId(npProviderGrpId);
      } catch (Exception e) {
        npsalesstructid = 0;  
      }

      logger.info("objOrderBean.getNpOrderId()         : "+objOrderBean.getNpOrderId());
      logger.info("objOrderBean.getNpCustomerId()      : "+objOrderBean.getNpCustomerId());
      logger.info("objOrderBean.getNpSiteId()          : "+objOrderBean.getNpSiteId());
      logger.info("objOrderBean.getNpDivisionId()      : "+objOrderBean.getNpDivisionId());
      logger.info("objOrderBean.getNpSpecificationId() : "+objOrderBean.getNpSpecificationId());
      logger.info("objOrderBean.getNpPaymentTerms()    : "+objOrderBean.getNpPaymentTerms());
      logger.info("objOrderBean.getnPSalesStructId()   : "+npsalesstructid);
      logger.info("objOrderBean.getNpCustomerScoreId() : "+objOrderBean.getNpCustomerScoreId());
        
      StructDescriptor sdOrder = StructDescriptor.createDescriptor("CREDIT.TO_CREDIT_ORDER", conn);

      //INICIO: PRY-0864 | AMENDEZ
      String strNpPaymentTerms=null;
      int flagVep=objOrderBean.getNpFlagVep();
      //INICIO: PRY-1062_3168 | AMENDEZ
      int specificationid=objOrderBean.getNpSpecificationId();
      //FIN: PRY-1062_3168 | AMENDEZ
      if(flagVep==1){
          strNpPaymentTerms=objOrderBean.getNpPaymentTerms()+"|"+
                            objOrderBean.getInitialQuota()+"|"+
                            objOrderBean.getNpAmountVep()+"|"+
                            objOrderBean.getNpNumCuotas()+"|"+
                            //INICIO: PRY-1062_3168 | AMENDEZ
                            objOrderBean.getNpFlagVep()+"|"+
                            objOrderBean.getNpPaymentTermsIQ()+"|"+
                            nppaymenttotal;

      }else if (flagVep == 0 && specificationid==2065){
          strNpPaymentTerms=objOrderBean.getNpPaymentTerms()+"|"+
                  0+"|"+
                  0+"|"+
                  0+"|"+
                  objOrderBean.getNpFlagVep()+"|"+
                  0+"|"+
                  nppaymenttotal;
                            //FIN: PRY-1062_3168 | AMENDEZ
      }else{
          strNpPaymentTerms=objOrderBean.getNpPaymentTerms();
      }
      //FIN: PRY-0864 | AMENDEZ


        //INICIO: PRY-1062_3168 | AMENDEZ
        logger.info("flagVep : "+flagVep);
        logger.info("nppaymenttotal    : "+nppaymenttotal);
        logger.info("specificationid   : "+specificationid);
        logger.info("strNpPaymentTerms : "+strNpPaymentTerms);
        //FIN: PRY-1062_3168 | AMENDEZ

			Object[] objOrder = { 
                  "ORDER",
                  String.valueOf(objOrderBean.getNpOrderId()),
                  String.valueOf(objOrderBean.getNpCustomerId()),
                  String.valueOf(objOrderBean.getNpSiteId()),
                  String.valueOf(objOrderBean.getNpDivisionId()),
                  String.valueOf(objOrderBean.getNpSpecificationId()),
                  String.valueOf(objOrderBean.getNpCustomerScoreId()), // [PPNN] MMONTOYA
                  objOrderBean.getNpCreatedBy(),
                  //INICIO: PRY-0864 | AMENDEZ
                  strNpPaymentTerms,
                  //FIN: PRY-0864 | AMENDEZ
                  npsalesstructid==0?"":String.valueOf(npsalesstructid),
                  aryItemList 
			};
      logger.info("Fin Order");
	  STRUCT stcOrder = new STRUCT(sdOrder, conn, objOrder);
      cstmt.setSTRUCT(1, stcOrder);           
      cstmt.registerOutParameter(2, Types.CHAR);
      cstmt.execute();
      strMessage = cstmt.getString(2);
      logger.info("[DoEvaluateOrder]: " + strMessage);
      hshData.put("strMessage",strMessage);  
    } catch (Exception e) {
      hshData.put("strMessage",e.getMessage());
      logger.error(formatException(e));
    } finally{
      try{
        closeObjectsDatabase(conn, cstmt, null);  
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }
    logger.info("****************** FIN CreditEvaluationDAO > doEvaluateOrder(HashMap hshData)******************");
    return hshData;
  }

  /**
   * EFLORES PM0011359 Sobrecarga de metodo
   * @param hshData
   * @return
   * @throws SQLException
   */
  public  HashMap doEvaluateOrder(HashMap hshData,Connection conn) throws SQLException {
    logger.info("****************** INICIO CreditEvaluationDAO > doEvaluateOrder(HashMap hshData,Connection conn)******************");
    String strMessage=null;
    OracleCallableStatement cstmt = null;
    OrderBean objOrderBean = new OrderBean();
    OrderDAO objOrderDao = new OrderDAO();
    ItemBean objItemBean = new ItemBean();
    ItemServiceBean objItemServiceBean = new ItemServiceBean();
    ArrayList arrItemDataList = new ArrayList();
    ArrayList arrItemServiceDataList = new ArrayList();
    HashMap hshItemData = new HashMap();
    int npProviderGrpId = 0;
    int npsalesstructid = 0;
    //INICIO: PRY-1062_3168 | AMENDEZ
    double nppaymenttotal = 0.00;
    //FIN: PRY-1062_3168 | AMENDEZ

    try{
      String sqlStr =  "BEGIN CREDIT.SPI_CREDIT_EVALUATE_ORDER(?, ?); END;";
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

      objOrderBean = (OrderBean) hshData.get("orderBean");
      arrItemDataList = (ArrayList) hshData.get("arrItems");

      ArrayList arrItemList = new ArrayList();
      ArrayList arrItemServiceList = new ArrayList();

      StructDescriptor sdItem = StructDescriptor.createDescriptor("CREDIT.TO_CREDIT_ITEM", conn);
      ArrayDescriptor adItemList = ArrayDescriptor.createDescriptor("CREDIT.TT_CREDIT_ITEM_LST", conn);

      StructDescriptor sdItemService = StructDescriptor.createDescriptor("CREDIT.TO_CREDIT_ITEM_SERVICE", conn);
      ArrayDescriptor adItemServiceList = ArrayDescriptor.createDescriptor("CREDIT.TT_CREDIT_ITEM_SERVICE_LST", conn);
      logger.info("arrItemDataList.size() "+arrItemDataList.size());
      for (int i = 0; i < arrItemDataList.size(); i++) {
        arrItemServiceList = new ArrayList();
        hshItemData = (HashMap)arrItemDataList.get(i);
        objItemBean = (ItemBean) hshItemData.get("itemBean");

        arrItemServiceDataList = (ArrayList)hshItemData.get("arrItemsServices");
        logger.info("arrItemServiceDataList.size() "+arrItemServiceDataList.size());
        for (int y = 0; y < arrItemServiceDataList.size(); y++) {
          objItemServiceBean = (ItemServiceBean)arrItemServiceDataList.get(y);

          logger.info("objItemServiceBean.getNpserviceid() "+objItemServiceBean.getNpserviceid());
          logger.info("objItemServiceBean.getNpaction() "+objItemServiceBean.getNpaction());

          Object[] objItemService = {
                  String.valueOf(objItemServiceBean.getNpserviceid()),
                  objItemServiceBean.getNpaction()

          };
          STRUCT stcItemService = new STRUCT(sdItemService, conn, objItemService);
          arrItemServiceList.add(stcItemService);
        }
        ARRAY aryItemServiceList = new ARRAY(adItemServiceList, conn, arrItemServiceList.toArray());
        logger.info("Fin Servicios");

        logger.info("objItemBean.getNpsolutionid()     : "+objItemBean.getNpsolutionid());
        logger.info("objItemBean.getNpmodalitysell()   : "+objItemBean.getNpmodalitysell());
        logger.info("objItemBean.getNpplanid()         : "+objItemBean.getNpplanid());
        logger.info("objItemBean.getNporiginalplanid() : "+objItemBean.getNporiginalplanid());
        logger.info("objItemBean.getNpproductid()      : "+objItemBean.getNpproductid());
        logger.info("objItemBean.getNpquantity()       : "+objItemBean.getNpquantity());
        logger.info("objItemBean.getNpphone()          : "+objItemBean.getNpphone());
        logger.info("objItemBean.getNpprice()          : "+objItemBean.getNpprice());
        logger.info("objItemBean.getNppriceexception() : "+objItemBean.getNppriceexception());

        double dprice = 0;
        if (objItemBean.getNppriceexception() != null ) {
          dprice = MiUtil.parseDouble(objItemBean.getNppriceexception());
        }else {
          dprice = MiUtil.parseDouble(objItemBean.getNpprice());
        }
        logger.info("dprice : "+dprice);
        //INICIO: PRY-1062_3168 | AMENDEZ
        nppaymenttotal=nppaymenttotal+dprice;
        //FIN: PRY-1062_3168 | AMENDEZ
        Object[] objItem = {
                String.valueOf(objItemBean.getNpsolutionid()),
                objItemBean.getNpmodalitysell(),
                String.valueOf(objItemBean.getNpplanid()),
                String.valueOf(objItemBean.getNporiginalplanid()),
                String.valueOf(objItemBean.getNpproductid()),
                String.valueOf(objItemBean.getNpquantity()),
                String.valueOf(dprice),
                String.valueOf(objItemBean.getNpphone()),
                aryItemServiceList
        };
        STRUCT stcItem = new STRUCT(sdItem, conn, objItem);
        arrItemList.add(stcItem);
      }
      logger.info("Fin Items");
      ARRAY aryItemList = new ARRAY(adItemList, conn, arrItemList.toArray());

      npProviderGrpId = objOrderBean.getNpProviderGrpId();

      try{
        npsalesstructid = objOrderDao.getSalesStructId(npProviderGrpId);
      }catch(Exception e) {
        npsalesstructid = 0;
      }

        logger.info("objOrderBean.getNpOrderId()         : "+objOrderBean.getNpOrderId());
        logger.info("objOrderBean.getNpCustomerId()      : "+objOrderBean.getNpCustomerId());
        logger.info("objOrderBean.getNpSiteId()          : "+objOrderBean.getNpSiteId());
        logger.info("objOrderBean.getNpDivisionId()      : "+objOrderBean.getNpDivisionId());
        logger.info("objOrderBean.getNpSpecificationId() : "+objOrderBean.getNpSpecificationId());
        logger.info("objOrderBean.getNpPaymentTerms()    : "+objOrderBean.getNpPaymentTerms());
        logger.info("objOrderBean.getnPSalesStructId()   : "+npsalesstructid);
        logger.info("objOrderBean.getNpCustomerScoreId() : "+objOrderBean.getNpCustomerScoreId());

        StructDescriptor sdOrder = StructDescriptor.createDescriptor("CREDIT.TO_CREDIT_ORDER", conn);
      //INICIO: PRY-0864 | AMENDEZ
      String strNpPaymentTerms=null;
      int flagVep=objOrderBean.getNpFlagVep();
      //INICIO: PRY-1062_3168 | AMENDEZ
      int specificationid=objOrderBean.getNpSpecificationId();
      //FIN: PRY-1062_3168 | AMENDEZ
      if(flagVep==1){
          strNpPaymentTerms=objOrderBean.getNpPaymentTerms()+"|"+
                            objOrderBean.getInitialQuota()+"|"+
                            objOrderBean.getNpAmountVep()+"|"+
                            objOrderBean.getNpNumCuotas()+"|"+
                            //INICIO: PRY-1062_3168 | AMENDEZ
                            objOrderBean.getNpFlagVep()+"|"+
                            objOrderBean.getNpPaymentTermsIQ()+"|"+
                            nppaymenttotal;

      }else if (flagVep == 0 && specificationid==2065){
          strNpPaymentTerms=objOrderBean.getNpPaymentTerms()+"|"+
                  0+"|"+
                  0+"|"+
                  0+"|"+
                  objOrderBean.getNpFlagVep()+"|"+
                  0+"|"+
                  nppaymenttotal;
                            //FIN: PRY-1062_3168 | AMENDEZ
      }else{
          strNpPaymentTerms=objOrderBean.getNpPaymentTerms();
      }
      //FIN: PRY-0864 | AMENDEZ

      //INICIO: PRY-1062_3168 | AMENDEZ
        logger.info("flagVep : "+flagVep);
        logger.info("nppaymenttotal    : "+nppaymenttotal);
        logger.info("specificationid   : "+specificationid);
      logger.info("strNpPaymentTerms : "+strNpPaymentTerms);
      //FIN: PRY-1062_3168 | AMENDEZ

        Object[] objOrder = {
              "ORDER",
              String.valueOf(objOrderBean.getNpOrderId()),
              String.valueOf(objOrderBean.getNpCustomerId()),
              String.valueOf(objOrderBean.getNpSiteId()),
              String.valueOf(objOrderBean.getNpDivisionId()),
              String.valueOf(objOrderBean.getNpSpecificationId()),
              String.valueOf(objOrderBean.getNpCustomerScoreId()), // [PPNN] MMONTOYA
              objOrderBean.getNpCreatedBy(),
              //INICIO: PRY-0864 | AMENDEZ
              strNpPaymentTerms,
              //FIN: PRY-0864 | AMENDEZ
              npsalesstructid==0?"":String.valueOf(npsalesstructid),
              aryItemList
      };
      logger.info("Fin Order");
      STRUCT stcOrder = new STRUCT(sdOrder, conn, objOrder);
      cstmt.setSTRUCT(1, stcOrder);
      cstmt.registerOutParameter(2, Types.CHAR);
      cstmt.execute();
      strMessage = cstmt.getString(2);
      logger.info("[DoEvaluateOrder]: " + strMessage);
      hshData.put("strMessage",strMessage);
    } catch (Exception e) {
      hshData.put("strMessage",e.getMessage());
      logger.error(formatException(e));
    } finally{
      try{
        closeObjectsDatabase(conn, cstmt, null);
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }
    logger.info("****************** FIN CreditEvaluationDAO > doEvaluateOrder(HashMap hshData,Connection conn)******************");
    return hshData;
  }
  
  /**
  * Motivo: Obteniene el detalle de una evaluacion Crediticia
  * <br>Realizado por: <a href="mailto:jose.casas@nextel.com.pe">José Casas</a>
  * <br>Fecha: 05/06/2009
  * @param     lSourceId  Ej. 27300
  * @param     strSource  Ej. "ORDEN"
  * @return    HashMap 
  */
  public  HashMap doReasonReject(long lSourceId, String strSource, int iflag, String strCreatedby) throws SQLException {
    CreditEvaluationBean objCreditEvaluationBean = new CreditEvaluationBean();        
    String strMessage=null;
    HashMap hshData=new HashMap();
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    Connection conn=null;
    try{
      String sqlStr =  "BEGIN CREDIT.SPI_INS_CREDIT_REASON_REJECT(?, ?, ?, ?, ?); END;";
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setString(1, strSource);
      cstmt.setLong(2, lSourceId);
      cstmt.setInt(3, iflag);
      cstmt.setString(4, strCreatedby);      
      cstmt.registerOutParameter(5, Types.CHAR);
      cstmt.execute();
      strMessage = cstmt.getString(5);
      System.out.println("[doReasonReject]: " + strMessage);
      hshData.put("strMessage",strMessage);
    } catch (Exception e) {          
      logger.error(formatException(e));
      hshData.put("strMessage",e.getMessage());  
    } finally{
      try{
        closeObjectsDatabase(conn, cstmt, rs);  
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }
    return hshData;
  } 
  

  /**
  * Motivo: Valida si sigue el nuevo flujo de Creditos
  * <br>Realizado por: <a href="mailto:jose.casas@nextel.com.pe">José Casas</a>
  * <br>Fecha: 05/06/2009
  * @param     lSourceId  Ej. 27300
  * @param     strSource  Ej. "20" - Orden
  * @return    HashMap 
  */
  public HashMap doValidateCredit(long lSourceId, String strSource) throws SQLException {    
    ArrayList list = new ArrayList();        
    OracleCallableStatement cstmt = null;    
    HashMap hshData=new HashMap();
    Connection conn=null;
    String strMessage=null;
    try{
      String sqlStr = "BEGIN CREDIT.SPI_VER_CREDIT_EVAL_STATUS(?, ?, ?, ?); END;";
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setString(1, strSource);
      cstmt.setLong(2, lSourceId);      
      cstmt.registerOutParameter(3, OracleTypes.NUMBER);
      cstmt.registerOutParameter(4, Types.VARCHAR);
      cstmt.executeUpdate();        
      strMessage = cstmt.getString(4);
      hshData.put("flagValidateCredit",cstmt.getInt(3)+"");        
      hshData.put("strMessage",strMessage);      
    } catch (Exception e) {      
      logger.error(formatException(e));
      hshData.put("strMessage",e.getMessage());
    } finally {
      try{
        closeObjectsDatabase(conn, cstmt, null);  
      } catch (Exception e) {
        logger.error(formatException(e));
      }
    }
    return hshData;
  }  
  
    /**
    * <br>Realizado por:  <a href="rensso.martinez@hp.com">Rensso Martínez</a>
    * <br>Fecha: 10/04/2014    
    */ 
    public HashMap getLastCustomerScore(long lnorderid) throws SQLException {    
      ArrayList list = new ArrayList();        
      OracleCallableStatement cstmt = null;    
      HashMap hshData=new HashMap();
      Connection conn=null;
      String strMessage=null;
      try{
        String sqlStr = "BEGIN CREDIT.SPI_GET_LAST_CUSTOMERSCOREID(?, ?, ?, ?); END;";
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        cstmt.setLong(1, lnorderid);   
        cstmt.setString(2, Constante.GENERATOR_TYPE_ORDER); 
        cstmt.registerOutParameter(3, OracleTypes.NUMBER);
        cstmt.registerOutParameter(4, Types.VARCHAR);
          
        cstmt.executeUpdate();
          
        strMessage = cstmt.getString(4);
        hshData.put("customerscoreid",cstmt.getInt(3)+"");        
        hshData.put("strMessage",strMessage);      
      } catch (Exception e) {      
        logger.error(formatException(e));
        hshData.put("strMessage",e.getMessage());
      } finally {
        try{
          closeObjectsDatabase(conn, cstmt, null);  
        } catch (Exception e) {
          logger.error(formatException(e));
        }
      }
      return hshData;
    }
}
