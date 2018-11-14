package pe.com.portability.util;

import java.io.File;
import java.lang.Exception;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import oracle.jdbc.driver.Const;
import pe.com.nextel.portability.ws.*;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.GenericObject;
import pe.com.nextel.util.MiUtil;
import pe.com.portability.dao.PortabilityOrderDAO;

public class PortabilityUtil extends GenericObject {
    public PortabilityUtil() {
    }

    public HashMap wsPortabilityNumbers(String strOrderId, String strCustomerId, String strLogin, String strMessageType, String strPortabilityType) {
        String strMessage = "";
        String strNumber = "";
        PortabilityOrderDAO objPortability = new PortabilityOrderDAO();
        if (logger.isDebugEnabled()) {
            logger.debug("OrderId :" + strOrderId);
            logger.debug("CustomerId :" + strCustomerId);
            logger.debug("Login :" + strLogin);
            logger.debug("MessageType :" + strMessageType);
            logger.debug("PortabilityType :" + strPortabilityType);
        }
        ArrayList arrListado = null;
        HashMap result = new HashMap();
        try {
            WSGetProcessMsgPortability stub = new WSGetProcessMsgPortabilityService().getWSGetProcessMsgPortabilityPort();
            CreateMessagePortabilityResponse response;
            CreateMessagePortabilityRequest element = new CreateMessagePortabilityRequest();

            element.setStrPortabilityType(strPortabilityType);
            element.setStrOrderId(strOrderId);
            element.setStrCustomerId(strCustomerId);
            element.setStrMessageType(strMessageType);
            element.setStrLoginId(strLogin);

            response = stub.createMessage(element);
            strMessage = response.getStrRespSOAP();
            if (logger.isDebugEnabled()) {
                logger.debug("strMessage :" + strMessage);
            }
            
            if(strMessage != null){
                System.out.println("response.getStrRespSOAP() tiene valor");
                if (!strMessage.equalsIgnoreCase("ack")) {
                    strMessage = "Los teléfonos no pudieron ser procesados";
                    result.put("strMessage", strMessage);
                }
            } else{
                System.out.println("response.getStrRespSOAP() esta null");
                logger.info("response.getStrRespSOAP() esta null");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Motivo: Envío de subsanación de portabilidad
     * <br>Realizado por: <a href="mailto:gfarfan@soaint.com">Gabriel Farfán</a>
     * <br>Fecha: 04/09/2018
     * param     sendMessageBE
     * return    HashMap
     */
    public HashMap wsPortabilitySubSanacion(SendMessageBE sendMessageBE) {
        String strMessage = "";
        HashMap result = new HashMap();
        try {
            WSGetProcessMsgPortability stub = new WSGetProcessMsgPortabilityService().getWSGetProcessMsgPortabilityPort();
            ProcessMsgPortabilityRequest element = new ProcessMsgPortabilityRequest();
            ProcessMsgPortabilityResponse response;

            logger.info("URL WS_GetProcessMsgPortability :" + new WSGetProcessMsgPortabilityService().getWSDLDocumentLocation().getHost() + "/" +
                    new WSGetProcessMsgPortabilityService().getWSDLDocumentLocation().getPath());

            sendMessageBE.setStrRemitente(Constante.REMITENTO_PORTABILIDAD);
            sendMessageBE.setStrMessageTypeId(Constante.TIPO_MENSAJE_PORTABILIDAD);
            sendMessageBE.setStrIdMessage(Constante.ID_MENSAJE_PORTABILIDAD);
            sendMessageBE.setStrOrigin(Constante.ORIGEN_PORTABILIDAD);

            element.setSendMessageBE(sendMessageBE);

            logger.info("WS_GetProcessMsgPortability REQUEST: " + MiUtil.transfromarAnyObjectToXmlText(element));
            response = stub.getProcessMsgPortability(element);
            logger.info("WS_GetProcessMsgPortability RESPONSE: " + MiUtil.transfromarAnyObjectToXmlText(response));

            if(response.getMessage() != null){
                strMessage = response.getMessage();
                logger.info("strMessage :" + strMessage);
                result.put("strMessage", strMessage);
            } else{
                logger.info("response.getMessage() esta null");
            }

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            strMessage = "Error al invocar al servicio WS_GetProcessMsgPortability";
            result.put("strMessage", strMessage);
            e.printStackTrace();
        }
        return result;
    }
}