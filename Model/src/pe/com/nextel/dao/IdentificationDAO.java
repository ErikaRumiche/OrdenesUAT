package pe.com.nextel.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import pe.com.nextel.bean.BiometricValidationBean;
import pe.com.nextel.bean.ExternalValidationBean;
import pe.com.nextel.bean.IdentityVerificationDetailBean;
import pe.com.nextel.bean.NoBiometricValidationBean;

public class IdentificationDAO extends GenericDAO{

    private static Logger logger = Logger.getLogger(IdentificationDAO.class);

    public IdentityVerificationDetailBean getIdentityVerificationDetail(long lOrderId) throws SQLException,Exception{
        Connection conn=null;

        IdentityVerificationDetailBean identityVerificationDetailBean = null;
        List<BiometricValidationBean> listBiometricValidation = null;
        List<NoBiometricValidationBean> listNoBiometricValidation = null;
        List<ExternalValidationBean> listExternalValidation = null;

        ResultSet rs = null;
        CallableStatement stmt = null;
        String storedProcedure = "begin CUSTOMER_AUTHENTICATION.NP_AUTHENTICATION_VIA_PKG.sp_get_ident_verif_detail_crm(?,?,?,?,?,?); end;";

        String av_message = null;

        try{
            conn = Proveedor.getConnection();
            stmt = conn.prepareCall(storedProcedure);
            stmt.setString(1, String.valueOf(lOrderId));
            stmt.registerOutParameter(2, OracleTypes.VARCHAR);
            stmt.registerOutParameter(3,OracleTypes.CURSOR);
            stmt.registerOutParameter(4,OracleTypes.CURSOR);
            stmt.registerOutParameter(5,OracleTypes.CURSOR);
            stmt.registerOutParameter(6,OracleTypes.CURSOR);
            stmt.execute();
            av_message=stmt.getString(2);

            if(av_message == null || av_message.isEmpty()) {

                // Obteniendo el detalle de la verificación de identidad
                rs = (ResultSet)stmt.getObject(3);

                identityVerificationDetailBean = new IdentityVerificationDetailBean();

                if(rs.next()){
                    identityVerificationDetailBean.setNombres(rs.getString(1));
                    identityVerificationDetailBean.setApellidos(rs.getString(2));
                    identityVerificationDetailBean.setTipoDocumento(rs.getString(3));
                    identityVerificationDetailBean.setNroDocumento(rs.getString(4));
                    identityVerificationDetailBean.setTipoVerificacionExitosa(rs.getString(5));
                    identityVerificationDetailBean.setRegistradoPor(rs.getString(6));
                    identityVerificationDetailBean.setModificadoPor(rs.getString(7));
                }
                rs.close();

                // Obteniendo el historial de validaciones biométricas
                rs = (ResultSet)stmt.getObject(4);

                listBiometricValidation = new ArrayList<BiometricValidationBean>();

                while(rs.next()){
                    BiometricValidationBean biometricValidationBean = new BiometricValidationBean();
                    biometricValidationBean.setVerificationResult(rs.getString(1));
                    biometricValidationBean.setSource(rs.getString(2));
                    biometricValidationBean.setVerificationDate(rs.getTimestamp(3));
                    biometricValidationBean.setVerificationMotive(rs.getString(4));
                    listBiometricValidation.add(biometricValidationBean);
                }
                rs.close();
                identityVerificationDetailBean.setListBiometricValidation(listBiometricValidation);

                // Obteniendo el historial de validaciones no biométricas
                rs = (ResultSet)stmt.getObject(5);

                listNoBiometricValidation = new ArrayList<NoBiometricValidationBean>();

                while(rs.next()){
                    NoBiometricValidationBean noBiometricValidationBean = new NoBiometricValidationBean();
                    noBiometricValidationBean.setVerificationResult(rs.getString(1));
                    noBiometricValidationBean.setSource(rs.getString(2));
                    noBiometricValidationBean.setVerificationDate(rs.getTimestamp(3));
                    noBiometricValidationBean.setNroPreguntaAcertada(rs.getString(4));
                    noBiometricValidationBean.setVerificationMotive(rs.getString(5));
                    listNoBiometricValidation.add(noBiometricValidationBean);
                }
                identityVerificationDetailBean.setListNoBiometricValidation(listNoBiometricValidation);
                
                // Obteniendo el historial de validaciones no biométricas
                rs = (ResultSet)stmt.getObject(6);

                listExternalValidation = new ArrayList<ExternalValidationBean>();

                while(rs.next()){
                	ExternalValidationBean externalValidationBean = new ExternalValidationBean();
                	externalValidationBean.setVerificationResult(rs.getString(1));
                	externalValidationBean.setSource(rs.getString(2));
                	externalValidationBean.setVerificationDate(rs.getTimestamp(3));
                	externalValidationBean.setDocumentNumber(rs.getString(4));
                	externalValidationBean.setFullName(rs.getString(5));
                	externalValidationBean.setProvider(rs.getString(6));
                	externalValidationBean.setCodeVerificationExt(rs.getString(7));
                	listExternalValidation.add(externalValidationBean);
                }
                identityVerificationDetailBean.setListExternalValidation(listExternalValidation);

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

        return identityVerificationDetailBean;
    }

}
