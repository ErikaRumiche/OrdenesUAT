package pe.com.nextel.ejb;

import pe.com.entel.esb.message.normalizaciondireccion.normalizardireccion.v1.NormalizarDireccionRequest;
import pe.com.nextel.bean.ComplDireccionUpBean;
import pe.com.nextel.bean.DireccionNormLogBean;
import pe.com.nextel.bean.DireccionNormWSBean;

import javax.ejb.EJBObject;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrador on 21/12/2015.
 */
public interface SEJBNormalizarDireccionRemote extends EJBObject {
    public Map<String, Object> consultaNormalizarDireccion(NormalizarDireccionRequest request) throws RemoteException, Exception;
    public Map<String, String> getNPValueXNameAndDesc(String strTableName, String strValueDesc) throws SQLException, Exception;
    public String insertNormalizacionLog(DireccionNormLogBean bean) throws SQLException, Exception;
    public HashMap getTableList(String strParamName, String strParamStatus) throws SQLException, Exception;
    public Map<String, Object> getDepProvDist(int tipoBusqueda, String codDep, String codProv) throws SQLException, Exception;
    public Map<String, String> getUbigeoIneiXUbigeoReniec(String ubigeoReniec) throws SQLException, Exception;
    public Map<String, String> getUbigeoIneiXUbigeoId(BigDecimal ubigeoId) throws SQLException, Exception;
    public Map<String, Object> getDataReniecXUbigeoInei(String ubigeoInei) throws SQLException, Exception;
    public Map<String, Object> getCustomerAddressNorm(long intObjectId, long longRegionId, String strObjectType,
                                                      String strGeneratortype) throws Exception, SQLException;
    public String updateAddressNormalize(BigDecimal idAddress, ComplDireccionUpBean objCompl, DireccionNormWSBean bean)
            throws Exception, SQLException;
    public  Map<String, String> getNPTableXTableAndValue(String strTableName, String strValue)
            throws Exception, SQLException;

    public Map<String, String> getCustomerDataClient(long intObjectId) throws Exception, SQLException;

}
