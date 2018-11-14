package pe.com.nextel.dao;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;
import org.apache.log4j.Logger;
import pe.com.nextel.bean.PopulateCenterBean;
import pe.com.nextel.bean.PopulateCenterDetBean;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by JCASTILLO on 04/04/2017.
 * [PRY-0787]
 */
public class PopulateCenterDAO extends GenericDAO {

    private static final Logger logger = Logger.getLogger(PopulateCenterDAO.class);

    public HashMap savePopulateCenter(PopulateCenterBean populateCenterBean,String user) throws Exception {
        HashMap  objHashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage;
        STRUCT stcItemResource;
        STRUCT stcPopulateCenter;
        ArrayList arrItemResource    = new ArrayList();
        try {
            String sqlStr = "BEGIN ORDERS.SPI_INS_CPUF_DETAIL(?,?,?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            StructDescriptor sdPopulateCenter = StructDescriptor.createDescriptor("ORDERS.TO_CPUF", conn);
            Object[] objPopulateCenter= {
                    populateCenterBean.getCpufId(),populateCenterBean.getOrderId(),populateCenterBean.getResponse(),populateCenterBean.getAcceptterms()
            };
            stcPopulateCenter = new STRUCT(sdPopulateCenter, conn, objPopulateCenter);
            StructDescriptor sdPopulateCenterDet = StructDescriptor.createDescriptor("ORDERS.TO_CPUF_DET", conn);
            ArrayDescriptor adItemResource = ArrayDescriptor.createDescriptor("ORDERS.TT_CPUF_DET", conn);
            for(PopulateCenterDetBean p:populateCenterBean.getPopulateCenterDetBeanList()){
                Object[] objItemResource = {
                        p.getUbigeo(),null,null,null,p.getCpufCode(),
                        p.getIsHome(),
                        p.getIsCoverage(),
                };
                stcItemResource = new STRUCT(sdPopulateCenterDet, conn, objItemResource);
                arrItemResource.add(stcItemResource);
            }
            ARRAY array = new ARRAY(adItemResource, conn, arrItemResource.toArray());
            cstmt.setSTRUCT(1,stcPopulateCenter);
            cstmt.setArray(2,array);
            cstmt.setString(3,user);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            strMessage = cstmt.getString(4);
        }catch(Exception e){
            logger.error("Exception",e);
            strMessage = e.getMessage();
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
        return objHashMap;
    }

    public HashMap getPopulateCenter(Long orderId) throws Exception {
        HashMap  objHashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage;
        PopulateCenterBean populateCenterBean=new PopulateCenterBean();
        try {
            String sqlStr = "BEGIN ORDERS.SPI_GET_CPUF(?,?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1,orderId);
            cstmt.registerOutParameter(2,OracleTypes.STRUCT,"ORDERS.TO_CPUF");
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            strMessage=cstmt.getString(3);
            if(strMessage == null){
                STRUCT struct = (STRUCT)cstmt.getObject(2);
                Object[] populateCenterTO = struct.getAttributes();
                populateCenterBean.setCpufId(MiUtil.getInt(populateCenterTO[0]));
                populateCenterBean.setOrderId(MiUtil.getLong(populateCenterTO[1]));
                populateCenterBean.setResponse(MiUtil.getInt(populateCenterTO[2]));
                populateCenterBean.setAcceptterms(MiUtil.getInt(populateCenterTO[3]));
                objHashMap.put("populateCenterBean",populateCenterBean);
            }
        }catch(Exception e){
            logger.error("Exception",e);
            strMessage = e.getMessage();
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
        return objHashMap;
    }

    public HashMap getPopulateCenterDetail(Long orderId) throws Exception {
        HashMap  objHashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage;
        PopulateCenterBean populateCenterBean=new PopulateCenterBean();
        List<PopulateCenterDetBean> populateCenterDetBeanList= new ArrayList<PopulateCenterDetBean>();
        try {
            String sqlStr = "BEGIN ORDERS.SPI_GET_CPUF_DETAIL(?,?,?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1,orderId);
            cstmt.registerOutParameter(2,OracleTypes.STRUCT,"ORDERS.TO_CPUF");
            cstmt.registerOutParameter(3,OracleTypes.ARRAY,"ORDERS.TT_CPUF_DET");
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            strMessage=cstmt.getString(4);
            if(strMessage == null){
                STRUCT struct = (STRUCT)cstmt.getObject(2);
                Object[] populateCenterTO = struct.getAttributes();
                populateCenterBean.setCpufId(MiUtil.getInt(populateCenterTO[0]));
                populateCenterBean.setOrderId(MiUtil.getLong(populateCenterTO[1]));
                populateCenterBean.setResponse(MiUtil.getInt(populateCenterTO[2]));
                populateCenterBean.setAcceptterms(MiUtil.getInteger(populateCenterTO[3]));
                ARRAY pendingArray = cstmt.getARRAY(3);
                if(pendingArray != null){
                    for (int i = 0; i < pendingArray.getOracleArray().length; i++) {
                        PopulateCenterDetBean populateCenterDetBean=new PopulateCenterDetBean();
                        Object[] object = ((STRUCT) pendingArray.getOracleArray()[i]).getAttributes();
                        populateCenterDetBean.setUbigeo((String) object[0]);
                        populateCenterDetBean.setDepartment((String) object[1]);
                        populateCenterDetBean.setProvince((String) object[2]);
                        populateCenterDetBean.setDistrict((String) object[3]);
                        populateCenterDetBean.setCpufCode((String)object[4]);
                        populateCenterDetBean.setIsHome(MiUtil.getInteger(object[5]));
                        populateCenterDetBean.setIsCoverage(MiUtil.getInteger(object[6]));
                        populateCenterDetBeanList.add(populateCenterDetBean);
                    }
                    populateCenterBean.setPopulateCenterDetBeanList(populateCenterDetBeanList);
                }
                objHashMap.put("populateCenterBean",populateCenterBean);
            }

            strMessage = cstmt.getString(2);
        }catch(Exception e){
            logger.error("Exception",e);
            strMessage = e.getMessage();
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
        return objHashMap;
    }

}