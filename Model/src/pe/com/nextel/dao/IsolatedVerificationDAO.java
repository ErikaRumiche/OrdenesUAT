package pe.com.nextel.dao;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;
import org.apache.log4j.Logger;
import pe.com.nextel.bean.IsolatedVerifConfigBean;
import pe.com.nextel.bean.IsolatedVerificationBean;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IsolatedVerificationDAO extends GenericDAO{

    private static Logger logger = Logger.getLogger(IsolatedVerificationDAO.class);

    public List<IsolatedVerifConfigBean> getViaConfig(String strCustomerId) throws SQLException,Exception{
        Connection conn=null;

        List<IsolatedVerifConfigBean> viaConfig = null;

        ResultSet rs = null;
        CallableStatement stmt = null;
        String storedProcedure = "begin CUSTOMER_AUTHENTICATION.SPI_GET_VIA_CONFIG(?,?,?); end;";

        String av_message = null;

        try{
            conn = Proveedor.getConnection();
            stmt = conn.prepareCall(storedProcedure);
            stmt.setLong(1, Long.parseLong(strCustomerId));
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.registerOutParameter(3, OracleTypes.VARCHAR);
            stmt.execute();
            av_message=stmt.getString(3);

            if(av_message == null || av_message.isEmpty()) {
                rs = (ResultSet)stmt.getObject(2);

                viaConfig = new ArrayList<IsolatedVerifConfigBean>();

                while(rs.next()){
                    IsolatedVerifConfigBean isolatedVerifConfigBean = new IsolatedVerifConfigBean();
                    isolatedVerifConfigBean.setSpec(rs.getInt(1));
                    isolatedVerifConfigBean.setIsCompany(rs.getInt(2));
                    viaConfig.add(isolatedVerifConfigBean);
                }

            } else {
                System.out.println("Error: " + av_message);
                throw new SQLException("Error: " + av_message);
            }
        }catch (SQLException s) {
            System.out.println(s);
            throw new SQLException(s);
        }catch (Exception e){
            System.out.println(e);
            throw new SQLException(e);
        }finally{
            //Se limpian las variables de conexión
            closeObjectsDatabase(conn, stmt, rs);
        }

        return viaConfig;
    }

    public List<IsolatedVerificationBean> getViaCustomer(Integer customerId, Integer verificationId, Integer orderId, Integer specificationId) throws Exception {
        Connection conn=null;
        System.out.println("[IsolatedVerificationDAO]getViaCustomer(), customerId: "+customerId+", verificationId: "+verificationId+", orderId: "+orderId+", specificationId: "+specificationId);
        List<IsolatedVerificationBean> listIsolatedVerificationBean = null;

        ResultSet rs = null;
        CallableStatement stmt = null;
        String storedProcedure = "begin customer_authentication.spi_get_via_customer(?, ?, ?, ?, ?, ?); end;";

        String av_message = null;

        try{
            conn = Proveedor.getConnection();
            stmt = conn.prepareCall(storedProcedure);
            stmt.setInt(1, customerId);
            stmt.setObject(2, verificationId);
            stmt.setObject(3, orderId);
            stmt.setInt(4, specificationId);
            stmt.registerOutParameter(5, OracleTypes.CURSOR);
            stmt.registerOutParameter(6, OracleTypes.VARCHAR);
            stmt.execute();
            av_message=stmt.getString(6);

            if(av_message == null || av_message.isEmpty()) {

                rs = (ResultSet)stmt.getObject(5);

                listIsolatedVerificationBean = new ArrayList<IsolatedVerificationBean>();

                while(rs.next()){
                    IsolatedVerificationBean isolatedVerificationBean = new IsolatedVerificationBean();
                    isolatedVerificationBean.setNpverificationid(rs.getInt(1));
                    isolatedVerificationBean.setNptipodocument(rs.getString(2));
                    isolatedVerificationBean.setNpnrodocument(rs.getString(3));
                    isolatedVerificationBean.setCustomer_name(rs.getString(4));
                    isolatedVerificationBean.setNp_date_end_validity(rs.getTimestamp(5));
                    isolatedVerificationBean.setNp_date_of_use(rs.getTimestamp(6));
                    isolatedVerificationBean.setNptransaction(rs.getInt(7));
                    isolatedVerificationBean.setNptypetransaction(rs.getString(8));
                    //JVERGARA agregar el tipo de verificacion
                    isolatedVerificationBean.setNpverificationtype(rs.getString(9));
                    //JVERGARA FIN
                    System.out.println("getViaCustomer(), isolatedVerificationBean: "+isolatedVerificationBean);
                    listIsolatedVerificationBean.add(isolatedVerificationBean);
                }

            } else {
                System.out.println("Error: " + av_message);
                throw new SQLException("Error: " + av_message);
            }
        }catch (SQLException s) {
            s.printStackTrace();
            System.out.println(s);
            throw new SQLException(s);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e);
            throw new SQLException(e);
        }finally{
            //Se limpian las variables de conexión
            closeObjectsDatabase(conn, stmt, rs);
        }

        return listIsolatedVerificationBean;
    }

    public void updViaCustomer(Integer npverificationid, String nptypetransaction, Integer nptransaction, String npmodifiedby) throws Exception {
        Connection conn=null;
        System.out.println("[IsolatedVerificationDAO]updViaCustomer(), npverificationid: "+npverificationid+", nptypetransaction: "+nptypetransaction
                +", nptransaction: "+nptransaction+", npmodifiedby: "+npmodifiedby);

        ResultSet rs = null;
        OracleCallableStatement cstmt = null;
        String storedProcedure = "begin customer_authentication.spi_upd_via_customer(?, ?); end;";

        String av_message = null;

        try{
            conn = Proveedor.getConnection();

            cstmt = (OracleCallableStatement)conn.prepareCall(storedProcedure);

            StructDescriptor sdVerification = StructDescriptor.createDescriptor("CUSTOMER_AUTHENTICATION.TO_VERIFICATION", conn);

            Object[] objVerification = {
                    npverificationid,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    npmodifiedby,
                    null,
                    nptransaction,
                    nptypetransaction
            };

            STRUCT stcVerification = new STRUCT(sdVerification, conn, objVerification);

            cstmt.setSTRUCT(1, stcVerification);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.execute();
            av_message=cstmt.getString(2);

            if(av_message != null && !av_message.isEmpty()) {
                System.out.println("Error: " + av_message);
                throw new SQLException("Error: " + av_message);
            }
        }catch (SQLException s) {
            s.printStackTrace();
            throw new SQLException(s);
        }catch (Exception e){
            e.printStackTrace();
            throw new SQLException(e);
        }finally{
            //Se limpian las variables de conexión
            closeObjectsDatabase(conn, cstmt, rs);
        }
    }
 
}
