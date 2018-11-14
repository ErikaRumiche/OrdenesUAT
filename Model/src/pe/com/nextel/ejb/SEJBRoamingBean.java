package pe.com.nextel.ejb;

import org.apache.log4j.Logger;
import pe.com.entel.esb.data.generico.entelgenericheader.v1.HeaderRequestType;
import pe.com.entel.esb.data.generico.entelgenericheader.v1.HeaderResponseType;
import pe.com.entel.esb.data.generico.responsestatus.v1.ResponseStatus;
import pe.com.entel.esb.message.gestionbolsasroaming.v1.*;
import pe.com.entel.esb.venta.gestionbolsasroaming.v1.EntelFaultMessage;
import pe.com.entel.esb.venta.gestionbolsasroaming.v1.GestionBolsasRoamingPT;
import pe.com.entel.esb.venta.gestionbolsasroaming.v1.GestionBolsasRoaming_SOAP11BindingQSService;
import pe.com.nextel.bean.RoamingServiceBean;
import pe.com.nextel.dao.RoamingDAO;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.*;

import static pe.com.nextel.util.GenericObject.formatException;

/**
 * Created by montoymi on 12/08/2015.
 * [ADT-RCT-092 Roaming con corte]
 */
public class SEJBRoamingBean implements SessionBean {
    // Constantes para la invocación de los servicios web.
    public static final String CODIGO_PAIS_CRM = "CRM";
    public static final String CANAL_CRM = "CRM";

    protected static Logger logger = Logger.getLogger(SEJBRoamingBean.class);

    private RoamingDAO objRoamingDAO = null;
    private DataSource ds = null;

    public void ejbCreate() {
        objRoamingDAO = new RoamingDAO();

        try {
            Context context = new InitialContext();
            Properties properties = new Properties();
            properties.load(new FileInputStream(Constante.FILE_PROPERTIES));
            ds = (DataSource) context.lookup(properties.getProperty("JNDI.DATASOURCE"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setSessionContext(SessionContext sessionContext) throws EJBException, RemoteException {

    }

    @Override
    public void ejbRemove() throws EJBException, RemoteException {

    }

    @Override
    public void ejbActivate() throws EJBException, RemoteException {

    }

    @Override
    public void ejbPassivate() throws EJBException, RemoteException {

    }

    public static String buildServiceDescription(String serviceName, BigDecimal price) {
        DecimalFormat df = new DecimalFormat("#,###,##0.00");
        return serviceName + " - S/. " + df.format(price);
    }

    public HashMap loadRoamingServices(String phone, long planId) {
        System.out.println("Inicio SEJBRoamingBean.loadRoamingServices - phone: " + phone + ", planId: " + planId);

        HashMap hshDataMap = new HashMap();

        /*
         * Obtiene los servicios CRM.
         */

        try {
            hshDataMap = objRoamingDAO.selectServicesByPhone(planId);
            if (hshDataMap.get("strMessage") != null) {
                String message = hshDataMap.get("strMessage").toString();
                throw new Exception(message);
            }
        } catch (Exception e) {
            logger.error(formatException(e));
            return hshDataMap;
        }

        List<RoamingServiceBean> serviceBeans = (List) hshDataMap.get("serviceBeans");
        if (serviceBeans.size() == 0) {
            hshDataMap.put("strMessage", "El plan no tiene paquetes de datos disponibles en el Catálogo de Productos.");
            return hshDataMap;
        }

        /*
         * Obtiene las bolsas disponibles en Portal Cautivo.
         */

        ConsultarBolsasDisponiblesResponse response = consultarBolsasDisponibles(phone);
        //ConsultarBolsasDisponiblesResponse response = consultarBolsasDisponibles_HardCode(phone);
        if (response.getResponseStatus().getDescripcionRespuesta() != null && !response.getResponseStatus().getDescripcionRespuesta().equalsIgnoreCase("OK")) {
            hshDataMap.put("strMessage", response.getResponseStatus().getDescripcionRespuesta());
            return hshDataMap;
        }

        for (InfoBolsaType bolsa : response.getBolsa()) {
            // Se excluyen las bosas cuyo tipo de pago no es cargo al recibo.
            if (bolsa.getInformacionPago().getCargoRecibo() == 0) {
                continue;
            }

            // Completa la información de los servicios CRM en base a la información de servicio web.
            for (RoamingServiceBean serviceBean : serviceBeans) {
                if (bolsa.getCodigo().equals(serviceBean.getBagCode())) {
                    serviceBean.setNpnomserv(buildServiceDescription(serviceBean.getNpnomserv(), bolsa.getValor()));
                    serviceBean.setBagType(bolsa.getTipoBolsa());
                    serviceBean.setPrice(bolsa.getValor());
                    serviceBean.setPlanType(response.getTipoPlan());
                    serviceBean.setValidity(bolsa.getVigencia());
                    break;
                }
            }
        }

        /*
         * Remueve los servicios que no han sido devueltos por el servicio web.
         */

        for (Iterator<RoamingServiceBean> it = serviceBeans.iterator(); it.hasNext(); ) {
            RoamingServiceBean serviceBean = it.next();
            if (serviceBean.getPrice() == null) {
                it.remove();
            }
        }

        if (serviceBeans.size() == 0) {
            hshDataMap.put("strMessage", "Cliente sin paquetes disponibles para la venta");
            return hshDataMap;
        }

        return hshDataMap;
    }

    public HashMap validateRecurrentRoamingService(String phone, String activationDate, int validity, long orderId, String bagCode) { // CFERNANDEZ [PRY-0858] - Se agrego el parametro: bagCode

        System.out.println("Inicio SEJBRoamingBean.validateRecurrentRoamingService - phone: " + phone + ", activationDate: " + activationDate + ", validity: " + validity + ", orderId: " + orderId + ", bagCode: " + bagCode);

       // CFERNANDEZ [PRY-0858] - Inicio
         /*
         * Valida en Portal Cautivo.
         */
         /* ConsultarBolsaActivaResponse response = consultarBolsaActiva(phone, activationDate);
            if (response.getResponseStatus().getDescripcionRespuesta() != null && !response.getResponseStatus().getDescripcionRespuesta().equalsIgnoreCase("OK")) {
                hshDataMap.put("strMessage", response.getResponseStatus().getDescripcionRespuesta());
                return hshDataMap;
            }
            if (response.getBolsaActiva() == 1) {
                hshDataMap.put("strMessage", "Equipo tiene paquete de datos roaming recurrente hasta " + response.getFechaVencimiento());
                return hshDataMap;
            }   */

             /*
             * Valida en CRM.
             */

         /*  try {
                hshDataMap = objRoamingDAO.validateRecurrentRoamingService(phone, activationDate, validity, orderId);
                if (hshDataMap.get("strMessage") != null) {
                    String message = hshDataMap.get("strMessage").toString();
                    throw new Exception(message);
                }
            } catch (Exception e) {
                logger.error(formatException(e));
                return hshDataMap;
            }

            if (hshDataMap.get("endScheduleDate") != null) {
                String endScheduleDate = hshDataMap.get("endScheduleDate").toString();
                if (endScheduleDate != null) {
                    hshDataMap.put("strMessage", "Equipo tiene paquete de datos roaming recurrente programado hasta " + endScheduleDate);
                    return hshDataMap;
                }
            } */

        // CFERNANDEZ [PRY-0858] - Fin

        /* Obtenemos la lista de ordenes en proceso en  CRM. */
        HashMap hshDataMap = new HashMap();

        try {
            System.out.println("Inicio objRoamingDAO.obtenerOrderRoamingService - phone: " + phone + ", orderId: " + orderId);

            hshDataMap = objRoamingDAO.obtenerOrderRoamingService(phone, orderId);
            if (hshDataMap.get("strMessage") != null) {
                String message = hshDataMap.get("strMessage").toString();
                throw new Exception(message);
            }
        } catch (Exception e) {
            logger.error(formatException(e));
            return hshDataMap;
        }

        /* Invoca al WS del Portal Cautivo para validar la lista de Ordenes en Proceso. */
        ValidarOrdenesResponse response = validarOrdenes(hshDataMap, phone, orderId, bagCode, activationDate);
            if(response != null){
                if (response.getResponseStatus().getCodigoRespuesta() != null && !response.getResponseStatus().getCodigoRespuesta().equals("0")) {
                    hshDataMap.put("strMessage", response.getResponseStatus().getDescripcionRespuesta());
                    logger.info("Fin SEJBRoamingBean.validarOrdenes - response: " + MiUtil.transfromarAnyObjectToXmlText(response));
                    return hshDataMap;
                }
            }else{
                hshDataMap.put("strMessage", Constante.ERROR_WS_PORTAL_CAUTIVO);
                logger.info("Fin SEJBRoamingBean.validarOrdenes - response: " + MiUtil.transfromarAnyObjectToXmlText(response));
                return hshDataMap;
            }

        logger.info("Fin SEJBRoamingBean.validarOrdenes - response: " + MiUtil.transfromarAnyObjectToXmlText(response));

        return hshDataMap;
    }

    /**
     * Obtiene una lista de bolsas disponibles.
     *
     * @param phone
     * @return
     * @throws Exception
     */
    private ConsultarBolsasDisponiblesResponse consultarBolsasDisponibles(String phone) {
        System.out.println("Inicio SEJBRoamingBean.consultarBolsasDisponibles - phone: " + phone);

        Long msisdn = Long.parseLong(phone);

        GestionBolsasRoaming_SOAP11BindingQSService gestionBolsasRoaming_SOAP11BindingQSService = new GestionBolsasRoaming_SOAP11BindingQSService();
        GestionBolsasRoamingPT gestionBolsasRoamingPT = gestionBolsasRoaming_SOAP11BindingQSService.getGestionBolsasRoaming_SOAP11BindingQSPort();

        ConsultarBolsasDisponiblesRequest request = new ConsultarBolsasDisponiblesRequest();
        request.setMsisdn(msisdn);
        request.setCodigoPais(CODIGO_PAIS_CRM);

        HeaderRequestType headerRequest = new HeaderRequestType();
        headerRequest.setCanal(CANAL_CRM);

        Holder<HeaderResponseType> holder = new Holder<HeaderResponseType>();

        try {
            return gestionBolsasRoamingPT.consultarBolsasDisponibles(request, headerRequest, holder);
        } catch (EntelFaultMessage e) {
            logger.error(formatException(e));
            return null;
        }
    }

    /**
     * Obtiene el flag que indica si el teléfono ya cuenta con el servicio de bolsa activa.
     *
     * @param phone
     * @param activationDate
     * @return
     */
    private ConsultarBolsaActivaResponse consultarBolsaActiva(String phone, String activationDate) {
        System.out.println("Inicio SEJBRoamingBean.consultarBolsaActiva - phone: " + phone + ", fechaActivacion: " + activationDate);

        Long msisdn = Long.parseLong(phone);

        GestionBolsasRoaming_SOAP11BindingQSService gestionBolsasRoaming_SOAP11BindingQSService = new GestionBolsasRoaming_SOAP11BindingQSService();
        GestionBolsasRoamingPT gestionBolsasRoamingPT = gestionBolsasRoaming_SOAP11BindingQSService.getGestionBolsasRoaming_SOAP11BindingQSPort();

        ConsultarBolsaActivaRequest request = new ConsultarBolsaActivaRequest();
        request.setMsisdn(msisdn);
        request.setFechaActivacion(activationDate);

        HeaderRequestType headerRequest = new HeaderRequestType();
        headerRequest.setCanal(CANAL_CRM);

        Holder<HeaderResponseType> holder = new Holder<HeaderResponseType>();

        try {
            return gestionBolsasRoamingPT.consultarBolsaActiva(request, headerRequest, holder);
        } catch (EntelFaultMessage e) {
            logger.error(formatException(e));
            return null;
        }
    }

    private ConsultarBolsasDisponiblesResponse consultarBolsasDisponibles_HardCode(String phone) {
        ConsultarBolsasDisponiblesResponse response = consultarBolsasDisponibles(phone);
        if (response.getBolsa().size() == 0) {
            System.out.println("Inicio consultarBolsasDisponibles_HardCode");

            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setCodigoRespuesta("0");
            response.setResponseStatus(responseStatus);

            List<InfoBolsaType> bolsas = new ArrayList<InfoBolsaType>();

            // Bolsa 1.
            InfoBolsaType bolsa = new InfoBolsaType();

            InfoPagoBolsaType infoPagoBolsaType = new InfoPagoBolsaType();
            infoPagoBolsaType.setCargoRecibo(1);

            bolsa.setInformacionPago(infoPagoBolsaType);
            bolsa.setCodigo("P17");
            bolsa.setValor(new BigDecimal(5));
            bolsa.setTipoBolsa("ONESHOT");

            bolsas.add(bolsa);

            // Bolsa 2.
            bolsa = new InfoBolsaType();

            infoPagoBolsaType = new InfoPagoBolsaType();
            infoPagoBolsaType.setCargoRecibo(1);

            bolsa.setInformacionPago(infoPagoBolsaType);
            bolsa.setCodigo("P11");
            bolsa.setValor(new BigDecimal(4));
            bolsa.setTipoBolsa("REC");

            bolsas.add(bolsa);

            // Bolsa 3.
            bolsa = new InfoBolsaType();

            infoPagoBolsaType = new InfoPagoBolsaType();
            infoPagoBolsaType.setCargoRecibo(1);

            bolsa.setInformacionPago(infoPagoBolsaType);
            bolsa.setCodigo("P18");
            bolsa.setValor(new BigDecimal(10));
            bolsa.setTipoBolsa("REC");

            bolsas.add(bolsa);

            response.getBolsa().addAll(bolsas);
        }

        return response;
    }

    /**
     * CFERNANDEZ [PRY-0858 - Paquete Ilimitado Whatsapp Roaming]
     * @param hshDataMap, msisdn, idOrders
     * @return
     * @throws Exception
     */
    private ValidarOrdenesResponse validarOrdenes(HashMap hshDataMap, String phone, long idOrders, String bagCode, String activationDate) {

        System.out.println("objRoamingDAO.validarOrdenes - hshDataMap: " + hshDataMap.size() + ", phone: " + phone + ", orderId: " + idOrders + ", bagCode: " + bagCode + ", activationDate: " + activationDate);

        Long msisdn = Long.parseLong(phone);
        String orderId = String.valueOf(idOrders);

        GregorianCalendar gregory = new GregorianCalendar();
        Date fechaActivation = null;

        fechaActivation = MiUtil.toFecha(activationDate,"dd/MM/yyyy");
        gregory.setTime(fechaActivation);

        XMLGregorianCalendar date;

        GestionBolsasRoaming_SOAP11BindingQSService gestionBolsasRoaming_SOAP11BindingQSService = new GestionBolsasRoaming_SOAP11BindingQSService();
        GestionBolsasRoamingPT gestionBolsasRoamingPT = gestionBolsasRoaming_SOAP11BindingQSService.getGestionBolsasRoaming_SOAP11BindingQSPort();

        try {

            date = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);

            ValidarOrdenesRequest request = new ValidarOrdenesRequest();

            request.setMsisdn(msisdn);

            InfoOrdenType ordenType = new InfoOrdenType();

            ordenType.setCodigoBolsa(bagCode);
            ordenType.setFechaActivacion(date);
            ordenType.setOrdenId(orderId);

            request.setNuevaOrden(ordenType);

            OrdenesProgramadasType ordenesProgramadas = new OrdenesProgramadasType();

            ArrayList arrayListOder = (ArrayList)hshDataMap.get("arrOrdersList");
            logger.info("Ordenes en proceso: " + arrayListOder.size());

            Date fechaActProg = null;
            XMLGregorianCalendar dateProg;
            GregorianCalendar gregoryProg = new GregorianCalendar();

            if ( arrayListOder != null && arrayListOder.size() > 0 ){
                for(int i=0; i<arrayListOder.size();i++){

                    InfoOrdenType ordenTypeProg = new InfoOrdenType();

                    HashMap ordenProg = (HashMap)arrayListOder.get(i);

                    fechaActProg = (Date)ordenProg.get("scheduledate");
                    gregoryProg.setTime(fechaActProg);
                    dateProg = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregoryProg);

                    ordenTypeProg.setOrdenId(ordenProg.get("idOrder").toString());
                    ordenTypeProg.setCodigoBolsa(ordenProg.get("bagCode").toString());
                    ordenTypeProg.setFechaActivacion(dateProg);

                    ordenesProgramadas.getOrden().add(ordenTypeProg);
                }
            }

            request.setOrdenesProgramadas(ordenesProgramadas);

            HeaderRequestType headerRequest = new HeaderRequestType();
            headerRequest.setCanal(CANAL_CRM);

            Holder<HeaderResponseType> holder = new Holder<HeaderResponseType>();

            logger.info("Inicio SEJBRoamingBean.validarOrdenes - request: " + MiUtil.transfromarAnyObjectToXmlText(request));

            return gestionBolsasRoamingPT.validarOrdenes(request, headerRequest, holder);

        }catch(DatatypeConfigurationException e){
            logger.error(formatException(e));
            return null;
        }catch (EntelFaultMessage e) {
            logger.error(formatException(e));
            return null;
        }
    }
}
