package pe.com.nextel.ejb;

import pe.com.entel.esb.message.normalizaciondireccion.normalizardireccion.v1.NormalizarDireccionRequest;
import pe.com.entel.esb.message.normalizaciondireccion.normalizardireccion.v1.NormalizarDireccionResponse;
import pe.com.entel.esb.venta.normalizaciondireccion.v1.EntelFault;
import pe.com.entel.esb.venta.normalizaciondireccion.v1.NormalizacionDireccionPort;
import pe.com.entel.esb.venta.normalizaciondireccion.v1.NormalizacionDireccionSOAP11BindingQSService;
import pe.com.nextel.bean.ComplDireccionUpBean;
import pe.com.nextel.bean.DireccionNormLogBean;
import pe.com.nextel.bean.DireccionNormWSBean;
import pe.com.nextel.dao.GeneralDAO;
import pe.com.nextel.dao.NormalizacionDireccionDAO;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.GenericObject;
import pe.com.nextel.util.MiUtil;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrador on 21/12/2015.
 */
public class SEJBNormalizarDireccionBean extends GenericObject implements SessionBean {
    private SessionContext _context;
    private GeneralDAO objGeneralDAO   = null;
    private NormalizacionDireccionDAO objNormaDirDAO   = null;

    public void ejbCreate() {
        objGeneralDAO = new GeneralDAO();
        objNormaDirDAO = new NormalizacionDireccionDAO();
    }

    public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {
        _context = ctx;
    }

    public void ejbRemove() throws EJBException, RemoteException {
        System.out.println("[SEJBNormalizarDireccionBean][ejbRemove()]");
    }

    public void ejbActivate() throws EJBException, RemoteException {
        System.out.println("[SEJBNormalizarDireccionBean][ejbActivate()]");
    }

    public void ejbPassivate() throws EJBException, RemoteException {
        System.out.println("[SEJBNormalizarDireccionBean][ejbPassivate()]");
    }

    public Map<String, Object> consultaNormalizarDireccion(NormalizarDireccionRequest request) throws RemoteException, Exception{
        Map<String, Object> hshResultMap = new HashMap<String, Object>();
        System.out.println("[SEJBNormalizarDireccionBean][consultaNormalizarDireccion] - INICIO");
        String messageError = "";
        String messageError2 = "";

        long timeInicio = System.currentTimeMillis();
        try{
            NormalizacionDireccionSOAP11BindingQSService service = new NormalizacionDireccionSOAP11BindingQSService();
            NormalizacionDireccionPort port = service.getNormalizacionDireccionSOAP11BindingQSPort();

            System.out.println("[SEJBNormalizarDireccionBean][consultaNormalizarDireccion] XML [REQUEST]: " + MiUtil.transfromarAnyObjectToXmlText(request) );

            System.out.println("[SEJBNormalizarDireccionBean][consultaNormalizarDireccion] - consultando OSB");
            NormalizarDireccionResponse response = port.normalizarDireccion(request);

            hshResultMap.put("result", response);
            System.out.println("[SEJBNormalizarDireccionBean][consultaNormalizarDireccion] - consulta OSB OK");
        }catch(EntelFault e){
            String descripError = (e.getFaultInfo() == null) ? "" : e.getFaultInfo().getDescripcionError();
            descripError = (descripError == null) ? "" : descripError;
            messageError2 = descripError;

            if(descripError.toUpperCase().indexOf(Constante.ERROR_EXCEPTION_TIMEOUT) >= 0){
                try {
                    Map<String, String> msg = objGeneralDAO.getNPTableXTableAndValue(Constante.MENSAJE_SUB_ALERTA_NORMALIZACION,
                            Constante.MENSAJE_SUB_ALERTA_POR_TIMEOUT);
                    messageError = msg.get("strValueDesc");
                } catch (Exception ex) {
                    System.out.println("[SEJBNormalizarDireccionBean][getValueDescXTableAndValue], Exception: "+e.getMessage());
                }
            }else{
                try {
                    Map<String, String> msg = objGeneralDAO.getNPTableXTableAndValue(Constante.MENSAJE_SUB_ALERTA_NORMALIZACION,
                            Constante.MENSAJE_SUB_ALERTA_POR_DISPONIBILIDAD);
                    messageError = msg.get("strValueDesc");
                } catch (Exception ex) {
                    System.out.println("[SEJBNormalizarDireccionBean][getValueDescXTableAndValue], Exception: "+e.getMessage());
                }
            }

            String codError = (e.getFaultInfo() == null) ? "" : e.getFaultInfo().getCodigoError();
            codError = (codError == null) ? "" : codError;

            System.out.println("[SEJBNormalizarDireccionBean][consultaNormalizarDireccion]. Error EntelFault, al invocar el Servicio OSB NormalizarDirecciones: "+codError +" - "+descripError);
        }catch(Exception e){
            messageError2 = (e.getCause()==null ? e.getMessage() : e.getCause().toString());
            try {
                Map<String, String> msg = objGeneralDAO.getNPTableXTableAndValue(Constante.MENSAJE_SUB_ALERTA_NORMALIZACION,
                        Constante.MENSAJE_SUB_ALERTA_POR_DISPONIBILIDAD);
                messageError = msg.get("strValueDesc");
            } catch (Exception ex) {
                System.out.println("[SEJBNormalizarDireccionBean][getValueDescXTableAndValue], Exception: "+e.getMessage());
            }

            System.out.println("[SEJBNormalizarDireccionBean][consultaNormalizarDireccion], exception1: "+messageError);
            System.out.println("[SEJBNormalizarDireccionBean][consultaNormalizarDireccion], exception2: "+messageError2);
        }finally{
            hshResultMap.put("tRespOSB", System.currentTimeMillis() - timeInicio);
            System.out.println("[SEJBNormalizarDireccionBean][consultaNormalizarDireccion] - map messageError: "+messageError);
            System.out.println("[SEJBNormalizarDireccionBean][consultaNormalizarDireccion] - map result: "+ MiUtil.transfromarAnyObjectToXmlText((NormalizarDireccionResponse)hshResultMap.get("result")));
        }

        hshResultMap.put("messageError", messageError);
        hshResultMap.put("messageError2", messageError2);

        return hshResultMap;
    }

    public Map<String, String> getNPValueXNameAndDesc(String strTableName, String strValueDesc) throws SQLException, Exception {
        return objGeneralDAO.getNPValueXNameAndDesc(strTableName, strValueDesc);
    }

    public String insertNormalizacionLog(DireccionNormLogBean bean) throws SQLException, Exception {
        return objNormaDirDAO.insertNormalizacionLog(bean);
    }

    public HashMap getTableList(String strParamName, String strParamStatus) throws SQLException, Exception {
        return objGeneralDAO.getTableList(strParamName, strParamStatus);
    }

    public Map<String, Object> getDepProvDist(int tipoBusqueda, String codDep, String codProv) throws SQLException, Exception {
        return objNormaDirDAO.getDepProvDist(tipoBusqueda, codDep, codProv);
    }

    public Map<String, String> getUbigeoIneiXUbigeoReniec(String ubigeoReniec) throws SQLException, Exception{
        return objNormaDirDAO.getUbigeoIneiXUbigeoReniec(ubigeoReniec);
    }

    public Map<String, String> getUbigeoIneiXUbigeoId(BigDecimal ubigeoId) throws SQLException, Exception{
        return objNormaDirDAO.getUbigeoIneiXUbigeoId(ubigeoId);
    }

    public Map<String, Object> getDataReniecXUbigeoInei(String ubigeoInei) throws SQLException, Exception{
        return objNormaDirDAO.getDataReniecXUbigeoInei(ubigeoInei);
    }

    public Map<String, Object> getCustomerAddressNorm(long intObjectId, long longRegionId, String strObjectType,
                                                      String strGeneratortype) throws Exception, SQLException{
        return objNormaDirDAO.getCustomerAddressNorm(intObjectId, longRegionId, strObjectType, strGeneratortype);
    }

    public String updateAddressNormalize(BigDecimal idAddress, ComplDireccionUpBean objCompl, DireccionNormWSBean bean)
            throws Exception, SQLException {
        return objNormaDirDAO.updateAddressNormalize(idAddress, objCompl, bean);
    }

    public  Map<String, String> getNPTableXTableAndValue(String strTableName, String strValue)
            throws Exception, SQLException {
        return objGeneralDAO.getNPTableXTableAndValue(strTableName, strValue);
    }

    public Map<String, String> getCustomerDataClient(long intObjectId) throws Exception, SQLException{
        return objNormaDirDAO.getCustomerDataClient(intObjectId);
    }
}
