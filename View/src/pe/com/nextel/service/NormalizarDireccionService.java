package pe.com.nextel.service;

import org.apache.commons.lang.StringUtils;
import pe.com.entel.esb.message.normalizaciondireccion.normalizardireccion.v1.*;
import pe.com.nextel.bean.*;
import pe.com.nextel.ejb.SEJBNormalizarDireccionRemote;
import pe.com.nextel.ejb.SEJBNormalizarDireccionRemoteHome;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;

import javax.naming.Context;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Administrador on 30/09/2015.
 */
public class NormalizarDireccionService {
    public static SEJBNormalizarDireccionRemote getSEJBNormalizarDireccionRemote() {
        SEJBNormalizarDireccionRemote sejbNormalizarDireccionRemote = null;
        try {
            final Context context = MiUtil.getInitialContext();
            final SEJBNormalizarDireccionRemoteHome sejbBiometricaNewRemoteHome =
                    (SEJBNormalizarDireccionRemoteHome) PortableRemoteObject.narrow(context.lookup("SEJBNormalizarDireccion"), SEJBNormalizarDireccionRemoteHome.class);
            sejbNormalizarDireccionRemote = sejbBiometricaNewRemoteHome.create();

            return sejbNormalizarDireccionRemote;
        } catch (Exception ex) {
            System.out.println("Exception : [NormalizarDireccionService][getSEJBNormalizarDireccionRemote][" + ex.getMessage() + "]");
        }
        return sejbNormalizarDireccionRemote;
    }

    public String datosOrdenId(long ordenId){
        String result = null;
        try{
            EditOrderService objOrderService= new EditOrderService();
            HashMap hshOrder=objOrderService.getOrder(ordenId);

            String strMessage=(String)hshOrder.get("strMessage");
            if (strMessage==null) {
                OrderBean objOrderBean = (OrderBean) hshOrder.get("objResume");
                if (objOrderBean != null) {
                    String finNormDivision = MiUtil.emptyValObjTrim(objOrderBean.getNpDivisionId());
                    String finNormCategoria = MiUtil.emptyValObjTrim(objOrderBean.getNpType());
                    String finNormSubCategoria = MiUtil.emptyValObjTrim(objOrderBean.getNpSpecificationId());
                    long finNormCodCliente = objOrderBean.getCsbCustomer().getSwCustomerId();
                    String finNormCodPromotor = MiUtil.emptyValObjTrim(objOrderBean.getNpProviderGrpId());

                    result = finNormDivision+"-"+finNormCategoria+"-"+finNormSubCategoria+"-"+finNormCodCliente+"-"+finNormCodPromotor+"-"+ordenId;
                }
            }
        }catch(Exception e){
            String strMessage = "[NormalizarDireccionService][datosOrdenId]: "+e.getMessage();
            System.out.println(strMessage);
        }

        return result;
    }

    public boolean validarDatos(String datosNorm)throws ServletException,IOException{
        boolean isDataOk = false;
        try{
            String[] dataNorm = datosNorm.split("-");
            String finNormDivision = dataNorm[0];
            String finNormCategoria = dataNorm[1];
            String finNormSubCategoria = dataNorm[2];
            long finNormCodCliente = Long.parseLong(dataNorm[3]);

            String strDivision = getValueDescXTableAndValue(Constante.NORMALIZACION_VENTAS,
                    Constante.DIVISION);
            if(strDivision!=null){
                String [] arrayDivision = strDivision.split(";");
                boolean flagContinuar = false;
                for(int i=0; i<arrayDivision.length; i++){
                    if(finNormDivision.equals(arrayDivision[i].trim())){
                        flagContinuar = true;
                        System.out.println("divion ok");
                        break;
                    }
                }

                if(flagContinuar){
                    String strCategoria = getValueDescXTableAndValue(Constante.NORMALIZACION_VENTAS,
                            Constante.CATEGORIA);
                    if(strCategoria!=null){
                        flagContinuar = false;
                        String [] arrayCategoria = strCategoria.split(";");
                        for(int j=0; j<arrayCategoria.length; j++){
                            if(finNormCategoria.equals(arrayCategoria[j].trim())){
                                flagContinuar = true;
                                System.out.println("categoria ok");
                                break;
                            }
                        }
                    }

                    if(flagContinuar){
                        String strSubCategoria = getValueDescXTableAndValue(Constante.NORMALIZACION_VENTAS,
                                Constante.SUBCATEGORIA);
                        if(strSubCategoria!=null){
                            flagContinuar = false;
                            String [] arraySubCategoria = strSubCategoria.split(";");
                            for(int k=0; k<arraySubCategoria.length; k++){
                                if(finNormSubCategoria.equals(arraySubCategoria[k].trim())){
                                    flagContinuar = true;
                                    System.out.println("subcategoria ok");
                                    break;
                                }
                            }

                            if(flagContinuar){
                                String tipoCliente = MiUtil.valText(getTipoCliente(finNormCodCliente));
                                if(tipoCliente.equalsIgnoreCase(Constante.CLIENTE_PROSPECT)){
                                	System.out.println("cliente ok");

                                    long longRegionId=11111;
                                    String strObjectType="CUSTOMER";
                                    String strGeneratortype = "INC";

                                    List<DireccionClienteBean> listDir = obtenerDireccionCliente(finNormCodCliente, longRegionId, strObjectType, strGeneratortype);
                                    if(listDir!=null) {
                                        System.out.println("obtuvo direccion");
                                        for(int i=0; i<listDir.size(); i++){
                                            DireccionClienteBean ob = listDir.get(i);
                                            if(ob.getEstado() == null){
                                                System.out.println("obtuvo direccion para normalizar");
                                                isDataOk = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }catch(Exception e){
            String strMessage = "[NormalizarDireccionService][validarDatos]: "+e.getMessage();
            System.out.println("strMessage:"+strMessage);
        }

        return isDataOk;
    }

    private String getTipoCliente(long codCliente){
        String tipoCliente = null;
        try {
            Map<String, String> map = getSEJBNormalizarDireccionRemote().getCustomerDataClient(codCliente);
            tipoCliente = map.get("typeclient");
        } catch (SQLException e) {
            String strMessage = "[NormalizarDireccionService][getTipoCliente]: "+e.getMessage();
            System.out.println("strMessage:"+strMessage);
        } catch (Exception e) {
            String strMessage = "[NormalizarDireccionService][getTipoCliente]: "+e.getMessage();
            System.out.println("strMessage:"+strMessage);
        }

        return tipoCliente;
    }

    public String getUbigeoIneiXUbigeoReniec(String ubigeoReniec){
        String ubigeoInei = null;
        try {
            Map<String, String> map = getSEJBNormalizarDireccionRemote().getUbigeoIneiXUbigeoReniec(ubigeoReniec);
            ubigeoInei = map.get("ubigeoinei");
        } catch (SQLException e) {
            String strMessage = "[NormalizarDireccionService][getUbigeoIneiXUbigeoReniec]: "+e.getMessage();
            System.out.println("strMessage:"+strMessage);
        } catch (Exception e) {
            String strMessage = "[NormalizarDireccionService][getUbigeoIneiXUbigeoReniec]: "+e.getMessage();
            System.out.println("strMessage:"+strMessage);
        }

        return ubigeoInei;
    }

    public String getValueDescXTableAndValue(String strTableName, String strValue){
        String value = null;
        try {
            Map<String, String> mapValueDesc = getSEJBNormalizarDireccionRemote().getNPTableXTableAndValue(strTableName, strValue);
            value = mapValueDesc.get("strValueDesc");
        } catch (SQLException e) {
            String strMessage = "[NormalizarDireccionService][getValueDescXTableAndValue]: "+e.getMessage();
            System.out.println("strMessage:"+strMessage);
        } catch (Exception e) {
            String strMessage = "[NormalizarDireccionService][getValueDescXTableAndValue]: "+e.getMessage();
            System.out.println("strMessage:"+strMessage);
        }

        return value;
    }

    public String getUbigeoIneiXUbigeoId(BigDecimal ubigeoId){
        String ubigeoInei = null;
        try {
            Map<String, String> mapTipoVia = getSEJBNormalizarDireccionRemote().getUbigeoIneiXUbigeoId(ubigeoId);
            ubigeoInei = mapTipoVia.get("ubigeoinei");
        } catch (SQLException e) {
            String strMessage = "[NormalizarDireccionService][getUbigeoIneiXUbigeoId]: "+e.getMessage();
            System.out.println("strMessage:"+strMessage);
        } catch (Exception e) {
            String strMessage = "[NormalizarDireccionService][getUbigeoIneiXUbigeoId]: "+e.getMessage();
            System.out.println("strMessage:"+strMessage);
        }

        return ubigeoInei;
    }

    public Map<String, String> getDataReniecXUbigeoInei(String ubigeoInei){
        Map<String, String> mapDatos = null;
        try {
            Map<String, Object> mapTipoVia = getSEJBNormalizarDireccionRemote().getDataReniecXUbigeoInei(ubigeoInei);
            mapDatos = (HashMap<String, String>)(mapTipoVia.get("result"));
        } catch (SQLException e) {
            String strMessage = "[NormalizarDireccionService][getDataReniecXUbigeoInei]: "+e.getMessage();
            System.out.println("strMessage:"+strMessage);
        } catch (Exception e) {
            String strMessage = "[NormalizarDireccionService][getDataReniecXUbigeoInei]: "+e.getMessage();
            System.out.println("strMessage:"+strMessage);
        }

        return mapDatos;
    }

    public ArrayList obtenerListTipo(String tipo){
        ArrayList objNpTableList = null;
        try {
            HashMap hNpTableList = new  HashMap();
            String dir = "";
            if(Constante.TIPOZONA.equals(tipo)){
                hNpTableList = getSEJBNormalizarDireccionRemote().getTableList(Constante.TIPOZONA, Constante.CADENA_VALOR_UNO);
            }else if(Constante.TIPOVIA.equals(tipo)){
                hNpTableList  = getSEJBNormalizarDireccionRemote().getTableList(Constante.TIPOVIA, Constante.CADENA_VALOR_UNO);
            }else if(Constante.TIPOINTERIOR.equals(tipo)){
                hNpTableList  = getSEJBNormalizarDireccionRemote().getTableList(Constante.TIPOINTERIOR, Constante.CADENA_VALOR_UNO);
            }

            objNpTableList =(ArrayList)hNpTableList.get("arrTableList");

        } catch (SQLException e) {
            String strMessage = "[NormalizarDireccionService][obtenerListTipo]: "+e.getMessage();
            System.out.println("strMessage:"+strMessage);
        } catch (Exception e) {
            String strMessage = "[NormalizarDireccionService][obtenerListTipo]: "+e.getMessage();
            System.out.println("strMessage:"+strMessage);
        }

        return objNpTableList;
    }

    public List<NewUbigeoBean> obtenerListDepProvDist(int tipo, String codDep, String codProv){
        List<NewUbigeoBean> list = null;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if(Constante.FLAG_UBIGEO_DEPARTAMENTO == tipo){
                map = getSEJBNormalizarDireccionRemote().getDepProvDist(Constante.FLAG_UBIGEO_DEPARTAMENTO, Constante.CADENA_VACIA, Constante.CADENA_VACIA);
            }else if(Constante.FLAG_UBIGEO_PROVINCIA == tipo){
                map = getSEJBNormalizarDireccionRemote().getDepProvDist(Constante.FLAG_UBIGEO_PROVINCIA, codDep, Constante.CADENA_VACIA);
            }else if(Constante.FLAG_UBIGEO_DISTRITO == tipo){
                map = getSEJBNormalizarDireccionRemote().getDepProvDist(Constante.FLAG_UBIGEO_DISTRITO, codDep, codProv);
            }

            list = (ArrayList<NewUbigeoBean>)map.get("strLista");
        } catch (SQLException e) {
            String strMessage = "[NormalizarDireccionService][obtenerListDepProvDist]: "+e.getMessage();
            System.out.println("strMessage:"+strMessage);
        } catch (Exception e) {
            String strMessage = "[NormalizarDireccionService][obtenerListDepProvDist]: "+e.getMessage();
            System.out.println("strMessage:"+strMessage);
        }

        return list;
    }

    public List<DireccionClienteBean> obtenerDireccionCliente(long intObjectId, long longRegionId, String strObjectType,
                                                              String strGeneratortype){
        List<DireccionClienteBean> list = null;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = getSEJBNormalizarDireccionRemote().getCustomerAddressNorm(intObjectId, longRegionId, strObjectType, strGeneratortype);
            list = (ArrayList<DireccionClienteBean>)map.get("objArrayList");
        } catch (SQLException e) {
            String strMessage = "[NormalizarDireccionService][obtenerDireccionCliente]: "+e.getMessage();
            System.out.println("strMessage:"+strMessage);
        } catch (Exception e) {
            String strMessage = "[NormalizarDireccionService][obtenerDireccionCliente]: "+e.getMessage();
            System.out.println("strMessage:"+strMessage);
        }

        return list;
    }

    public Map<String, Object> getDireccionNormalizada(DireccionJsonBean direccionJ, String ubigeoInei) throws Exception{
        Map<String, Object> mapResult = new HashMap<String, Object>();

        Map<String, Object> mapDatos = getDatosAutorizacionYPaquete();

        NormalizarDireccionRequest request = new NormalizarDireccionRequest();
        request.setUserName((mapDatos.get("usuario")==null) ? "" : (String)mapDatos.get("usuario"));
        request.setPassword((mapDatos.get("password")==null) ? "" : (String) mapDatos.get("password"));
        request.setIdPaquete((mapDatos.get("paquete")==null) ? -1 : ((Integer)mapDatos.get("paquete")).intValue());
        request.setUsarReferencia(((Boolean) mapDatos.get("referencia")).booleanValue());
        request.setCodigoUnico(direccionJ.getCodigoUnico());
        request.setDireccion(direccionJ.getDireccion());
        request.setUbigeo(ubigeoInei);

        String strXMLReq = MiUtil.transfromarAnyObjectToXmlText(request);
        long timeInicioException = System.currentTimeMillis();
        try{
            Map<String, Object> obDirNor = getSEJBNormalizarDireccionRemote().consultaNormalizarDireccion(request);
            String messageError = (String)obDirNor.get("messageError");
            String tRespOSB = MiUtil.emptyValObjTrim(obDirNor.get("tRespOSB"));

            NormalizarDireccionResponse response = null;
            String strXMLResp = null;
            Integer flagGeo = null;
            String motivoNoGeo = null;
            Integer numOcurrencia = null;
            if(StringUtils.isBlank(messageError)){
                response = (NormalizarDireccionResponse)obDirNor.get("result");
                strXMLResp = MiUtil.transfromarAnyObjectToXmlText(response);

                if(response!=null){
                    BAStructAddressToProcess geocodificarDireccionResult = response.getGeocodificarDireccionResult();
                    flagGeo = geocodificarDireccionResult.getFlagGeocodificacion();
                    motivoNoGeo = geocodificarDireccionResult.getMotivoNoGeocodificacion();
                    numOcurrencia = geocodificarDireccionResult.getNumeroRespuestas();

                    String tRespAna = geocodificarDireccionResult.getTimeResultAnalytics();

                    int flagGeoLog = flagGeo;
                    if(flagGeo < 0){
                        flagGeoLog = Constante.VALUE_FLAG_NEGATIVE;
                    }

                    String resultInsertOK = insertNormalizacionLog(direccionJ, strXMLReq, strXMLResp, flagGeoLog, motivoNoGeo,
                            numOcurrencia, new Date(), Constante.CADENA_VACIA, tRespOSB, tRespAna);

                    System.out.println("[NormalizarDireccionService][getDireccionNormalizada] resultInsertLogOK: "+resultInsertOK);

                    ArrayOfBAStructGeocodedAddress objList = geocodificarDireccionResult.getListOfPuntosGeocodificados();
                    List<BAStructGeocodedAddress> lista =  objList.getPuntoGeocodificado();

                    List<DireccionNormWSBean> listBean = new ArrayList<DireccionNormWSBean>();
                    for(BAStructGeocodedAddress ob : lista){
                        DireccionNormWSBean bean = new DireccionNormWSBean();
                        bean.setDireccionNormalizada(MiUtil.valText(ob.getDireccionGeocodificada()));
                        bean.setTipoVia(MiUtil.valText(ob.getTipoVia()));
                        bean.setNombreVia(MiUtil.valText(ob.getNombreVia()));
                        bean.setNumeroPuerta(MiUtil.valText(ob.getNumeroPuerta1()));
                        bean.setManzana(MiUtil.valText(ob.getManzana()));
                        bean.setNombreUbanizacion((MiUtil.emptyValConcat(ob.getTipoUrbanizacion()) + MiUtil.valText(ob.getNombreUrbanizacion())).trim());
                        bean.setTipoInterior(MiUtil.valText(ob.getTipoInterior()));
                        bean.setNumeroInterior(MiUtil.valText(ob.getNumeroInterior()));
                        bean.setLote(MiUtil.valText(ob.getLote()));
                        bean.setTipoZona(MiUtil.valText(ob.getTipoUrbanizacion()));
                        bean.setNombreZona(MiUtil.valText(ob.getNombreUrbanizacion()));
                        bean.setReferencia(MiUtil.valText(ob.getReferencia()));
                        bean.setGeolocalizacionX(new BigDecimal(ob.getXGeocodificado()));
                        bean.setGeolocalizacionY(new BigDecimal(ob.getYGeocodificado()));
                        bean.setUbigeoInei(MiUtil.valText(ob.getUbigeoGeocodificado()));

                        listBean.add(bean);
                    }

                    mapResult.put("listaBean", listBean);
                    mapResult.put("flagGeo", flagGeo);
                    mapResult.put("motivoNoGeo", motivoNoGeo);
                    mapResult.put("numOcurrencia", numOcurrencia);
                }else{
                    String resultInsertNOK1 = insertNormalizacionLog(direccionJ, strXMLReq, strXMLResp, null, null, null, new Date(),
                            Constante.MESSAGE_RESPONSE_NULL, tRespOSB, Constante.CADENA_VACIA);
                	
                    System.out.println("[NormalizarDireccionService][getDireccionNormalizada] resultInsertNOK1: "+resultInsertNOK1);
                	
                    mapResult.put("flagGeo", Constante.RESPONSE_IS_NULL);
                    mapResult.put("numOcurrencia", 0);
                    mapResult.put("motivoNoGeo", "");
                }
            }else{
                String messageError2 = (String)obDirNor.get("messageError2");
                if(messageError2.length() > 299){
                    messageError2 = messageError2.substring(0, messageError2.length() - (messageError2.length() - 299));
                }
                String resultInsertNOK2 = insertNormalizacionLog(direccionJ, strXMLReq, null, null, null, null, new Date(),
                        messageError2, tRespOSB, Constante.CADENA_VACIA);
                
                System.out.println("[NormalizarDireccionService][getDireccionNormalizada] resultInsertNOK2: "+resultInsertNOK2);

                if(messageError2.toUpperCase().indexOf(Constante.ERROR_EXCEPTION_TIMEOUT) >= 0){
                	mapResult.put("flagGeo", Constante.CONSULTA_EXCEPTION_TIMEOUT);
                }else{
                	mapResult.put("flagGeo", Constante.CONSULTA_EXCEPTION_DISPONIBILIDAD);
                }

                mapResult.put("messageError", messageError);
                mapResult.put("numOcurrencia", 0);
                mapResult.put("motivoNoGeo", "");
            }
        }catch(RemoteException e){
            System.out.println("[NormalizarDireccionService][getDireccionNormalizada]: "+e.getMessage());

            String messageError2 = (e.getCause()==null ? e.getMessage() : e.getCause().toString());
            long resultTimeException = System.currentTimeMillis() - timeInicioException;

            String resultInsertException = insertNormalizacionLog(direccionJ, strXMLReq, null, null, null, null, new Date(),
                    messageError2, String.valueOf(resultTimeException), Constante.CADENA_VACIA);

            System.out.println("[NormalizarDireccionService][getDireccionNormalizada] resultInsertException: "+resultInsertException);

            String messageError = "";
            try {
                Map<String, String> msg = getSEJBNormalizarDireccionRemote().getNPTableXTableAndValue(Constante.MENSAJE_SUB_ALERTA_NORMALIZACION,
                        Constante.MENSAJE_SUB_ALERTA_POR_DISPONIBILIDAD);
                messageError = msg.get("strValueDesc");
            } catch (Exception ex) {
                System.out.println("[NormalizarDireccionService][getDireccionNormalizada], Exception SubAlerta: "+e.getMessage());
            }

            mapResult.put("flagGeo", Constante.CONSULTA_EXCEPTION_DISPONIBILIDAD);
            mapResult.put("messageError", messageError);
            mapResult.put("numOcurrencia", 0);
            mapResult.put("motivoNoGeo", "");
        }catch(Exception e) {
            String strMessage = "[NormalizarDireccionService][getDireccionNormalizada]: "+e.getMessage();
            System.out.println("strMessage:"+strMessage);
            throw new Exception(e);
        }

        return mapResult;
    }

    private String insertNormalizacionLog(DireccionJsonBean dataDireccion, String request, String response, Integer flagGeo,
                                          String motivoNoGeo, Integer numOcurrencia, Date fechaHora, String messageError, String tRespOSB, String tRespAna){
        DireccionNormLogBean obj = new DireccionNormLogBean();
        obj.setCodigoCliente(dataDireccion.getCodigoCliente());
        obj.setTipoDireccion(dataDireccion.getTipoDireccion()+"0");
        obj.setDireccionAntigua(dataDireccion.getDireccion());
        obj.setRequest(request);
        obj.setResponse(response);
        obj.setUsuarioApp(dataDireccion.getUsuarioApp());
        obj.setNombreAplicacion(dataDireccion.getNombreAplicacion());
        obj.setFechaHora(fechaHora);
        obj.setNumeroOcurrencia((numOcurrencia==null) ? "" : String.valueOf(numOcurrencia));
        obj.setNumeroPedido(dataDireccion.getNumeroPedido());
        obj.setTiempoRespOSB(tRespOSB);
        obj.setTiempoRespWS(tRespAna);
        obj.setFlagGeocodificacion((flagGeo == null) ? "" : String.valueOf(flagGeo));
        obj.setMotivoNoGeocodificacion(motivoNoGeo);
        obj.setIdDireccionAntigua(dataDireccion.getIdDireccion());
        obj.setDescExcepcion(messageError);

        String strMessage = null;
        try{
            strMessage = getSEJBNormalizarDireccionRemote().insertNormalizacionLog(obj);
        }catch(SQLException e){
            strMessage = "[NormalizarDireccionService][insertNormalizacionLog]: "+e.getMessage();
            System.out.println("strMessage:"+strMessage);
        }catch (Exception e){
            strMessage = "[NormalizarDireccionService][insertNormalizacionLog]: "+e.getMessage();
            System.out.println("strMessage:"+strMessage);
        }

        return strMessage;
    }

    public String updateAddressNormalize(BigDecimal idAddress, ComplDireccionUpBean objCompl, DireccionNormWSBean bean)
            throws Exception, SQLException {
        String result = null;
        try{
            result = getSEJBNormalizarDireccionRemote().updateAddressNormalize(idAddress, objCompl, bean);
        }catch(Exception e) {
            result = "[NormalizarDireccionService][updateAddressNormalize]: "+e.getMessage();
            System.out.println("strMessage:"+result);
        }

        System.out.println("updateAddressNormalize result: "+result);

        return result;
    }

    public boolean normalizacionIsActive() throws Exception{
        boolean active = false;
        try{
            System.out.println("[NormalizarDireccionService][normalizacionIsActive] - consultando estado normalizacion");
            Map<String, String> oHashMapUser=getSEJBNormalizarDireccionRemote().getNPValueXNameAndDesc(Constante.NORMALIZACION_SWITCH_ON_OFF, Constante.NORMALIZACION_ACTIVA_DESACTIVA);
            if(StringUtils.isBlank(oHashMapUser.get("strMessage"))){
                String normalizacionEstado = oHashMapUser.get("strTableValue");
                if(Constante.CADENA_VALOR_UNO.equals(normalizacionEstado)){
                    active = true;
                    System.out.println("normalizacion active");
                }else{
                    System.out.println("normalizacion no active");
                }
            }
        }catch(Exception e){
            System.out.println("[NormalizarDireccionService][normalizacionIsActive] Exception: "+e.getMessage());
        }

        return active;
    }

    private Map<String, Object> getDatosAutorizacionYPaquete(){
        String strNormalizacionUser = null;
        String strNormalizacionPass = null;
        Integer normalizacionPaq = null;
        Boolean strReferencia = Boolean.FALSE;

        try{
            Map<String, String> oHashMapUser=getSEJBNormalizarDireccionRemote().getNPValueXNameAndDesc(Constante.NORMALIZACION_DIR_AUTH, Constante.NORMALIZACION_DIR_AUTH_USUARIO);
            if(StringUtils.isBlank(oHashMapUser.get("strMessage"))){
                strNormalizacionUser = oHashMapUser.get("strTableValue");
            }

            Map<String, String> oHashMapPass=getSEJBNormalizarDireccionRemote().getNPValueXNameAndDesc(Constante.NORMALIZACION_DIR_AUTH, Constante.NORMALIZACION_DIR_AUTH_PASSWORD);
            if(StringUtils.isBlank(oHashMapPass.get("strMessage"))){
                strNormalizacionPass = oHashMapPass.get("strTableValue");
            }

            Map<String, String> oHashMapPaq=getSEJBNormalizarDireccionRemote().getNPValueXNameAndDesc(Constante.NORMALIZACION_DIR_AUTH, Constante.NORMALIZACION_DIR_AUTH_PAQUETE);
            if(StringUtils.isBlank(oHashMapPaq.get("strMessage"))){
                String valorPaquete = (oHashMapPaq.get("strTableValue")==null) ? "-1" : oHashMapPaq.get("strTableValue").trim();
                normalizacionPaq = new Integer(Integer.parseInt(valorPaquete));
            }

            Map<String, String> oHashMapRef=getSEJBNormalizarDireccionRemote().getNPValueXNameAndDesc(Constante.NORMALIZACION_REFERENCIA_SWITCH, Constante.REFERENCIA_ACTIVA_DESACTIVA);
            if(StringUtils.isBlank(oHashMapRef.get("strMessage"))){
                String ref = oHashMapRef.get("strTableValue");
                strReferencia = ref.equals(Constante.CADENA_VALOR_UNO) ? Boolean.TRUE : Boolean.FALSE;
            }
        }catch(Exception e){
            System.out.println("[NormalizarDireccionService][getDatosAutorizacionYPaquete]: "+e.getMessage());
        }

        Map<String, Object> mapResult = new HashMap<String, Object>();
        mapResult.put("usuario", strNormalizacionUser);
        mapResult.put("password", strNormalizacionPass);
        mapResult.put("paquete", normalizacionPaq);
        mapResult.put("referencia", strReferencia);

        return mapResult;
    }
}
