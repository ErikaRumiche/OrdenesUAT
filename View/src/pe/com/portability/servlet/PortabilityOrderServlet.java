package pe.com.portability.servlet;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;

import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.google.gson.Gson;
import com.oreilly.servlet.MultipartRequest;
import org.apache.commons.lang.StringUtils;

import org.apache.soap.encoding.soapenc.Base64;
import pe.com.entel.integracion.processportednumber.client.ProcessPortedNumber;
import pe.com.entel.integracion.processportednumber.client.ProcessPortedNumberRequest;
import pe.com.entel.integracion.processportednumber.client.ProcessPortedNumberResponse;
import pe.com.entel.integracion.processportednumber.client.ProcessPortedNumber_Service;
import pe.com.nextel.portability.ws.AssociatedKey;
import pe.com.nextel.portability.ws.SendMessageBE;
import pe.com.nextel.servlet.GenericServlet;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;
import pe.com.portability.bean.PortabilityOrderBean;
import pe.com.portability.service.PortabilityOrderService;


public class PortabilityOrderServlet extends GenericServlet {


    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    private final static String PROTOCOL = "file://";

/*
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doWork( request , response );
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doWork( request , response );
    }
    
    public void doWork(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter() ;
        
        out.print( "<script>alert('PortabilityOrderServlet');</script>" );
        
    }
  */

    /**
     * @see GenericServlet#executeDefault(HttpServletRequest, HttpServletResponse)
     */
    public void executeDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        try {
            logger.debug("Antes de hacer el sendRedirect");
            response.sendRedirect("/portal/page/portal/orders/items");
        } catch (Exception e) {
            logger.error(formatException(e));
        }
    }


    /*
     * Motivo:  Método que devuelve la lista de documentos objectables dado un motivo de objeción
     * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
     * <br>Fecha: 17/08/2009
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getAtatchment_by_mo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String strMotivObjec = StringUtils.defaultString(request.getParameter("cmbMotivos"));
        String strIndex = StringUtils.defaultString(request.getParameter("index"), "");
        String strMessage = null;

        PortabilityOrderService objPortabilityOrderService = new PortabilityOrderService();

        try {
            HashMap hshDataMap = objPortabilityOrderService.getAtatchment_by_mo(strMotivObjec);
            strMessage = (String) hshDataMap.get("strMessage");
            if (StringUtils.isNotBlank(strMessage)) {
                request.setAttribute("strMessage", strMessage);
                request.getRequestDispatcher("pages/loadMessage.jsp").forward(request, response);
            } else {
                request.setAttribute("index", strIndex);
                request.setAttribute(Constante.DATA_STRUCT, hshDataMap);
                request.getRequestDispatcher("pages/loadComboDocMotivos.jsp").forward(request, response);
            }
        } catch (Exception e) {
            logger.error(formatException(e));
            strMessage = e.getMessage();
            request.setAttribute("strMessage", strMessage);
            request.getRequestDispatcher("pages/loadMessage.jsp").forward(request, response);
        }
    }

    /**
     * Motivo: Método que invoca al web services de portabilidad (ALTA)
     *
     * @param response
     * @param request
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    public void sendPortabilityMessages(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strOrderId = StringUtils.defaultString(request.getParameter("strOrderId"));
        String strCustomerId = StringUtils.defaultString(request.getParameter("strCustomerId"));
        String strLogin = StringUtils.defaultString(request.getParameter("strLogin"));
        String strMessageType = StringUtils.defaultString(request.getParameter("messageType"));
        String strPortabilityType = StringUtils.defaultString(request.getParameter("strPortabilityType"));
        String strMessage = null;
        HashMap hshWsMap = null;
        PortabilityOrderService objPortabilityOrderService = new PortabilityOrderService();
        HashMap hshResult = new HashMap();

        try {
            hshWsMap = objPortabilityOrderService.wsPortabilityNumbers(strOrderId, strCustomerId, strLogin, strMessageType, strPortabilityType);
            strMessage = (String) hshWsMap.get("strMessage");
            if (StringUtils.isNotBlank(strMessage)) {

                hshResult.put("strMessage", strMessage);
                hshResult.put("strOrderId", strOrderId);
                hshResult.put("strTipoOrigen", "PortAlta");
                request.setAttribute("objResultado", hshResult);
                RequestDispatcher rd = request.getRequestDispatcher("PAGEEDIT/ResultEdit.jsp");
                rd.forward(request, response);

            } else {

                strMessage = "La operación se realizó con exito";
                hshResult.put("strMessage", strMessage);
                hshResult.put("strOrderId", strOrderId);
                hshResult.put("strTipoOrigen", "PortAlta");
                request.setAttribute("objResultado", hshResult);
                RequestDispatcher rd = request.getRequestDispatcher("PAGEEDIT/ResultEdit.jsp");
                rd.forward(request, response);
            }
        } catch (Exception e) {
            logger.error(formatException(e));
            strMessage = e.getMessage();
            request.setAttribute("strMessage", strMessage);
            request.getRequestDispatcher("pages/loadMessage.jsp").forward(request, response);
        }

    }

    /**
     * Motivo: Método que permite validar la longitud de los documentos (BAJA).
     * <br>Realizado por: <a href="mailto:david.lazo@nextel.com.pe">David Lazo de la Vega</a>
     *
     * @param response
     * @param request
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */

    public void validateSizeDocumentLow(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String strMessage = null;
        PortabilityOrderBean objPOBean = new PortabilityOrderBean();
        PortabilityOrderBean objPOrderBean = new PortabilityOrderBean();
        PortabilityOrderService objPortabOrderService = new PortabilityOrderService();
        HashMap hshWsMap = new HashMap();
        String ruta = (String) request.getParameter("rutaFile");
        PortabilityOrderService objPortabilityOrderService = new PortabilityOrderService();

        try {
            int indexExt = ruta.lastIndexOf('.');
            String strExtension = ruta.substring(indexExt + 1);
            System.out.println("Extension " + strExtension);

            //Valida la extension de los archivos a subir.
            HashMap hshExtensionFile = objPortabOrderService.getConfigFile("EXTENSION_DOCUMENT_LOW");
            strMessage = (String) hshExtensionFile.get("strMessage");

            if (!StringUtils.isNotBlank(strMessage)) {
                objPOrderBean = (PortabilityOrderBean) hshExtensionFile.get("objPOBean");
                String extFile = (String) objPOrderBean.getNpconfigFile();
                if (extFile.equalsIgnoreCase(strExtension)) {
                    //Valida el tamaño en KB de los archivos a subir.
                    HashMap hshSizeFile = objPortabOrderService.getConfigFile("SIZE_DOCUMENT_LOW");
                    strMessage = (String) hshSizeFile.get("strMessage");

                    if (!StringUtils.isNotBlank(strMessage)) {
                        objPOBean = (PortabilityOrderBean) hshSizeFile.get("objPOBean");
                        long sizeFile = MiUtil.parseLong(objPOBean.getNpconfigFile());
                        String sFichero = ruta;
                        System.out.println("Ruta " + ruta);
                        System.out.println("Tamaño " + sizeFile);
                        File fichero = new File(sFichero);
                        if (fichero.exists()) {
                            fichero.length();
                            System.out.println("Archivo " + (fichero.length() / 1024));
                            if ((fichero.length() / 1024) >= sizeFile) {
                                request.setAttribute("strMessage", "El archivo excede el tamaño permitido (100 KB)");
                                request.getRequestDispatcher("pages/loadMessage.jsp").forward(request, response);
                            }
                        }
                    } else {
                        strMessage = "Error de configuración";
                        request.setAttribute("strMessage", strMessage);
                        request.getRequestDispatcher("pages/loadMessage.jsp").forward(request, response);
                    }
                } else {
                    request.setAttribute("strMessage", "Sólo de deben cargar archivos (pdf)");
                    request.getRequestDispatcher("pages/loadMessage.jsp").forward(request, response);
                }

            } else {
                strMessage = "Error de configuración";
                request.setAttribute("strMessage", strMessage);
                request.getRequestDispatcher("pages/loadMessage.jsp").forward(request, response);
            }

        } catch (Exception e) {
            logger.error(formatException(e));
            strMessage = e.getMessage();
            request.setAttribute("strMessage", strMessage);
            request.getRequestDispatcher("pages/loadMessage.jsp").forward(request, response);
        }
    }

    /**
     * Motivo: Método que actualiza el estado de la orden de portabilidad(ALTA)
     * <br>Realizado por: <a href="mailto:david.lazo@nextel.com.pe">David Lazo de la Vega</a>
     *
     * @param response
     * @param request
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */

    public void updateStatusPortabilityItem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String strMessage = null;
        String strPhoneNumber = null;
        HashMap hshWsMap = new HashMap();
        long numberFilas = MiUtil.parseLong((String) request.getParameter("numFilas"));
      
      /* Ini Se agrego variable TM 16/11/2009 */
        String strOrderId = StringUtils.defaultString(request.getParameter("strOrderId"));
      /* Fin Se agrego variable TM 16/11/2009 */

        PortabilityOrderService objPortabilityOrderService = new PortabilityOrderService();
        HashMap hshResult = new HashMap();

        try {
        Date fechaInicio = new Date();
            System.out.println("[PORTABILITYORDERSERVLET][updateStatusPortabilityItem][PM0010354] : INICIO");
            System.out.println("[PORTABILITYORDERSERVLET][updateStatusPortabilityItem][PM0010354] : OrderId -->" +strOrderId);
            System.out.println("[PORTABILITYORDERSERVLET][updateStatusPortabilityItem][PM0010354] : Fecha -->" + fechaInicio);
            System.out.println("[PORTABILITYORDERSERVLET][updateStatusPortabilityItem][PM0010354] : Número de Filas -->" + numberFilas);
        }  catch (Exception e) {
            logger.error(formatException(e));
            strMessage = e.getMessage();
            System.out.println("[PORTABILITYORDERSERVLET][updateStatusPortabilityItem][PM0010354] : ERROR AL INTENTAR PINTAR LOG INICIO "+strMessage);
        }

        try {

            if (numberFilas == 1) {
                String arrCadena = (String) request.getParameter("arrStatus");
                //System.out.println(arrCadena);
                String[] arrValores = arrCadena.split("-");
                hshWsMap = objPortabilityOrderService.updateStatusPortability(MiUtil.parseLong(arrValores[5]), MiUtil.parseLong(arrValores[0]), arrValores[1], arrValores[4]);
                strMessage = (String) hshWsMap.get("strMessage");
                if (StringUtils.isNotBlank(strMessage)) {
                    hshWsMap = objPortabilityOrderService.getPhoneNumberItem(MiUtil.parseLong(arrValores[5]), MiUtil.parseLong(arrValores[0]));
                    strPhoneNumber = (String) hshWsMap.get("strPhoneNumber");
                    strMessage = "El número: " + strPhoneNumber + " no se pudo subsanar";
                    hshResult.put("strMessage", strMessage);
                    hshResult.put("strOrderId", strOrderId);
                    hshResult.put("strTipoOrigen", "PortAlta");
                    request.setAttribute("objResultado", hshResult);
                    RequestDispatcher rd = request.getRequestDispatcher("PAGEEDIT/ResultEdit.jsp");
                    rd.forward(request, response);
                } else {
                    strMessage = "La subsanación se realizó con exito";
                    if (MiUtil.parseLong(arrValores[2]) == MiUtil.parseLong(arrValores[3])) {
                        hshResult.put("strMessage", strMessage);
                        hshResult.put("strOrderId", strOrderId);
                        hshResult.put("strTipoOrigen", "PortAlta");
                        request.setAttribute("objResultado", hshResult);
                        RequestDispatcher rd = request.getRequestDispatcher("PAGEEDIT/ResultEdit.jsp");
                        rd.forward(request, response);
                    }
                }
            } else {
                String[] arrCadena = (String[]) request.getParameterValues("arrStatus");
                long lsizeCadena = arrCadena.length;
                for (int i = 0; i < lsizeCadena; i++) {
                    String[] arrValores = arrCadena[i].split("-");
                    hshWsMap = objPortabilityOrderService.updateStatusPortability(MiUtil.parseLong(arrValores[5]), MiUtil.parseLong(arrValores[0]), arrValores[1], arrValores[4]);
                    strMessage = (String) hshWsMap.get("strMessage");
                    if (StringUtils.isNotBlank(strMessage)) {
                        hshWsMap = objPortabilityOrderService.getPhoneNumberItem(MiUtil.parseLong(arrValores[5]), MiUtil.parseLong(arrValores[0]));
                        strPhoneNumber = (String) hshWsMap.get("strPhoneNumber");
                        strMessage = "El número: " + strPhoneNumber + " no se pudo subsanar";
                        hshResult.put("strMessage", strMessage);
                        hshResult.put("strOrderId", strOrderId);
                        hshResult.put("strTipoOrigen", "PortAlta");
                        request.setAttribute("objResultado", hshResult);
                        RequestDispatcher rd = request.getRequestDispatcher("PAGEEDIT/ResultEdit.jsp");
                        rd.forward(request, response);
                    } else {
                        strMessage = "La subsanación se realizó con exito";
                        if (MiUtil.parseLong(arrValores[2]) == MiUtil.parseLong(arrValores[3])) {
                            hshResult.put("strMessage", strMessage);
                            hshResult.put("strOrderId", strOrderId);
                            hshResult.put("strTipoOrigen", "PortAlta");
                            request.setAttribute("objResultado", hshResult);
                            RequestDispatcher rd = request.getRequestDispatcher("PAGEEDIT/ResultEdit.jsp");
                            rd.forward(request, response);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(formatException(e));
            strMessage = e.getMessage();
            request.setAttribute("strMessage", strMessage);
            request.getRequestDispatcher("pages/loadMessage.jsp").forward(request, response);
        }

        try{
        Date fechaFin = new Date();
            System.out.println("[PORTABILITYORDERSERVLET][updateStatusPortabilityItem][PM0010354] : OrderId -->" +strOrderId);
            System.out.println("[PORTABILITYORDERSERVLET][updateStatusPortabilityItem][PM0010354] : Fecha -->" + fechaFin);
            System.out.println("[PORTABILITYORDERSERVLET][updateStatusPortabilityItem][PM0010354] : FIN");
        }  catch (Exception e) {
            logger.error(formatException(e));
            strMessage = e.getMessage();
            System.out.println("[PORTABILITYORDERSERVLET][updateStatusPortabilityItem][PM0010354] : ERROR AL INTENTAR PINTAR LOG FIN "+strMessage);
    }




    }

    /**
     * Motivo: Método que validad si el numero ya a sido portado(ALTA)
     * <br>Realizado por: <a href="mailto:david.lazo@nextel.com.pe">David Lazo de la Vega</a>
     * <br>Modificado por: <a href="mailto:otrillo@soaint.com">Oswaldo Trillo</a>
     * @param response
     * @param request
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    public void checkNumberPorted(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String strOrderId = StringUtils.defaultString(request.getParameter("strOrderId"));
        long lOrderId = MiUtil.parseLong(strOrderId);
        String strMessage = null;
        String strPhoneNumber = null;
        HashMap hshDataMap = new HashMap();
        String phoneNumber = (String) request.getParameter("phoneNumber");
        String index = (String) request.getParameter("index");
        String numberFilas = (String) request.getParameter("numFilas");
        //Ini PM0010311 - OTrillo 24/09/2015
        String fechaCreacionOrden = (String) request.getParameter("fechaCreacionOrden");
        String strAssignorId = (String) request.getParameter("cmbAssignorId");
        String strApplicationId = (String) request.getParameter("applicationId");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        //Fin PM0010311 - OTrillo 24/09/2015

        PortabilityOrderService objPortabilityOrderService = new PortabilityOrderService();
        HashMap hshResult = new HashMap();
        String errCode = null, errMsg = null, strWSEnabled;// PM0010311 - OTrillo 24/09/2015
        String phoneNumbersList = (String)request.getParameter("phoneNumberList");
        String lengthNumberPhone = (String)request.getParameter("lengthNumberPhone");

        try {
            //Ini PM0010311 - OTrillo 24/09/2015
            strWSEnabled = MiUtil.getWSProcessPortedNumberEnabled();
            logger.info("WS_ProcessPortedNumber Enabled : " + MiUtil.getWSProcessPortedNumberEnabled() + "\n");
            request.setAttribute("phoneNumberList", phoneNumbersList);
            request.setAttribute("lengthNumberPhone", lengthNumberPhone);
            if (strWSEnabled.equals("1")){
                Date dtCreatedDate = formatter.parse(fechaCreacionOrden);

                ProcessPortedNumberResponse resp = processPortedNumber(phoneNumber, dtCreatedDate, strAssignorId, strApplicationId);
                logger.info(" ProcessPortedNumberResponse: idTransaction : " + resp.getIdTransaction() + "\n" +
                        " portStatus: " + resp.getPortStatus() + "\n" +
                        " portDate: " + resp.getPortDate() + "\n" +
                        " receCode: " + resp.getReceCode() + "\n" +
                        " donorCode: " + resp.getDonorCode() + "\n" +
                        " errCode: " + resp.getErrCode() + "\n" +
                        " errMsg: " + resp.getErrMsg());
                errCode = resp.getErrCode();
                errMsg  = resp.getErrMsg();

                if (Integer.parseInt(errCode) >0){
                    logger.info("checkNumberPorted errorCode > 0 ");

                    request.setAttribute("numFilas", numberFilas);
                    request.setAttribute("index", index);
                    hshResult.put("strMessage", errMsg);
                    hshResult.put("strOrderId", strOrderId);
                    hshResult.put("strTipoOrigen", "PortValidNumber");
                    request.setAttribute("objResultado", hshResult);
                    RequestDispatcher rd = request.getRequestDispatcher("PAGEEDIT/ResultEdit.jsp");
                    rd.forward(request, response);
                }else if (Integer.parseInt(errCode) <0){
                    logger.info("checkNumberPorted errorCode < 0 ");

                    request.setAttribute("numFilas", numberFilas);
                    request.setAttribute("index", index);
                    hshResult.put("strMessage", Constante.MSG_VALIDATION_ERROR);
                    hshResult.put("strOrderId", strOrderId);
                    hshResult.put("strTipoOrigen", "PortValidNumber");
                    request.setAttribute("objResultado", hshResult);
                    RequestDispatcher rd = request.getRequestDispatcher("PAGEEDIT/ResultEdit.jsp");
                    rd.forward(request, response);
                }else if (Integer.parseInt(errCode) ==0){
                    logger.info("checkNumberPorted errorCode = 0 ");

            hshDataMap = objPortabilityOrderService.checkNumberPorted(phoneNumber, lOrderId);
            strMessage = (String) hshDataMap.get("strMessage");
            if (StringUtils.isNotBlank(strMessage)) {
                request.setAttribute("numFilas", numberFilas);
                request.setAttribute("index", index);
                hshResult.put("strMessage", strMessage);
                hshResult.put("strOrderId", strOrderId);
                hshResult.put("strTipoOrigen", "PortValidNumber");
                request.setAttribute("objResultado", hshResult);
                RequestDispatcher rd = request.getRequestDispatcher("PAGEEDIT/ResultEdit.jsp");
                rd.forward(request, response);
            } else {
                hshResult.put("strMessage", strMessage);
                hshResult.put("strTipoOrigen", "PortValidNumber");
                request.setAttribute("objResultado", hshResult);
                RequestDispatcher rd = request.getRequestDispatcher("PAGEEDIT/ResultEdit.jsp");
                rd.forward(request, response);
            }
                }
                //Fin PM0010311 - OTrillo 24/09/2015
            } else if (strWSEnabled.equals("0")) {
                // Implementación anterior
                hshDataMap = objPortabilityOrderService.checkNumberPorted(phoneNumber, lOrderId);
                strMessage = (String) hshDataMap.get("strMessage");
                if (StringUtils.isNotBlank(strMessage)) {
                    request.setAttribute("numFilas", numberFilas);
                    request.setAttribute("index", index);
                    hshResult.put("strMessage", strMessage);
                    hshResult.put("strOrderId", strOrderId);
                    hshResult.put("strTipoOrigen", "PortValidNumber");
                    request.setAttribute("objResultado", hshResult);
                    RequestDispatcher rd = request.getRequestDispatcher("PAGEEDIT/ResultEdit.jsp");
                    rd.forward(request, response);
                } else {
                    hshResult.put("strMessage", strMessage);
                    hshResult.put("strTipoOrigen", "PortValidNumber");
                    request.setAttribute("objResultado", hshResult);
                    RequestDispatcher rd = request.getRequestDispatcher("PAGEEDIT/ResultEdit.jsp");
                    rd.forward(request, response);
                }
            }


        } catch (Exception e) {
            logger.error(formatException(e));
            strMessage = e.getMessage();
            request.setAttribute("strMessage", strMessage);
            request.getRequestDispatcher("pages/loadMessage.jsp").forward(request, response);
        }
    }
    /**
     * Motivo: Método que invoca al servicio web WS_ProcessPortedNumber que realiza la validacion de portabilidad
     * <br>Realizado por: <a href="otrillo@soaint.com">Oswaldo Trillo</a>
     *
     * @param phoneNumber
     * @param createdDate
     * @param strAssignorId
     * @param strApplicationId
     * @throws Exception
     */
    private ProcessPortedNumberResponse processPortedNumber(String phoneNumber, Date createdDate, String strAssignorId, String strApplicationId){
        //Ini PM0010311 - OTrillo 24/09/2015
        logger.info(" ProcessPortedNumberRequest: phoneNumber : " + phoneNumber + "\n" +
                " createdDate: " + createdDate + "\n" +
                " strAssignorId: " + strAssignorId + "\n" +
                " strApplicationId: " + strApplicationId);

        GregorianCalendar gregory = new GregorianCalendar();
        gregory.setTime(createdDate);

        XMLGregorianCalendar calendar = null;

        ProcessPortedNumberRequest request = new ProcessPortedNumberRequest();
        ProcessPortedNumberResponse response = new ProcessPortedNumberResponse();

        try {
            ProcessPortedNumber_Service service = new ProcessPortedNumber_Service();
            ProcessPortedNumber processPortedNumber = service.getProcessPortedNumberPort();

            calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
            request.setPhoneNumber(phoneNumber);
            request.setOperatorCode(strAssignorId);
            request.setOrderDate(calendar);
            request.setApplication(strApplicationId);

            response = processPortedNumber.processPortedNumber(request);
        }catch (Exception e){
            response.setErrCode("0");
            response.setErrMsg("Servicio web no disponible"); // se modifica requerimiento PM0010311 para que continúe el flujo en caso no este disponible el WS

            logger.error(formatException(e));
        }
        return response;
        //Ini PM0010311 - OTrillo 24/09/2015
    }

    /**
     * Motivo: Método que visualiza popup con documento en pdf.(BAJA)
     * <br>Realizado por: <a href="mailto:david.lazo@nextel.com.pe">David Lazo de la Vega</a>
     *
     * @param response
     * @param request
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    public void uploadDocumentBaja(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String strMessage = null;
        String ruta = "";
        ruta = request.getParameter("hdnruta");

        try {

            File file = getFileFromUri(ruta);
            if (file.exists()) {
                byte[] image = new byte[(int) file.length()];
                FileInputStream fileInputStream = new FileInputStream(file);
                fileInputStream.read(image);
                response.setHeader("Content-Type", "application/pdf");
                OutputStream outStr = response.getOutputStream();
                outStr.write(image);
                outStr.flush();
                outStr.close();
            }

        } catch (Exception e) {
            logger.error(formatException(e));
            strMessage = e.getMessage();
            request.setAttribute("strMessage", strMessage);
            request.getRequestDispatcher("pages/loadMessage.jsp").forward(request, response);
        }
    }

    public static File getFileFromUri(String uri) {
        File file = null;
        String urlString = uri;
        try {
            //  Create URL object from the URL string
            URL url = new URL(PROTOCOL + urlString);
            //  The connection object has information that may be
            //  retrieved without parsing the data returned
            URLConnection conn = url.openConnection();
            conn.connect();
            file = File.createTempFile("tmp", "aaa");
            //  Get an input stream from the URLConnection object
            copy(conn.getInputStream(), file);
        } catch (IOException e) {
            System.out.println(e);
        }
        return file;
    }

    static void copy(InputStream in, File dst) throws IOException {
        OutputStream out = new FileOutputStream(dst);
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public void procesarSubsanacionDeuda(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        MultipartRequest multi = new MultipartRequest(request, ".", 1024 * 1024 * 50);
        String fileName = multi.getFilesystemName("txtfichero");
        String strAssignorId = StringUtils.defaultString(request.getParameter("hdnAssignorId"));
        System.out.println("fileName : " + fileName );

        File file = multi.getFile("txtfichero");
        byte[] dataBytes = extractBytes(file);

        /* Convertir arreglo de bytes a Base64 - Inicio
        String encodeString = Base64.encode(buffer);
        Convertir arreglo de bytes a Base64 - Fin */

        SendMessageBE messageBE = new SendMessageBE();

        AssociatedKey key = new AssociatedKey();
        key.setObjectNumber(StringUtils.defaultString(request.getParameter("hdnPortabItemId")));
        key.setObjectType(Constante.TIPO_OBJETO_PORTABILIDAD);
        messageBE.setKey(key);

        messageBE.setStrEntityPayment(StringUtils.defaultString(request.getParameter("hdnEntidad")));
        messageBE.setStrCurrencyType(StringUtils.defaultString(request.getParameter("hdnMoneda")));
        messageBE.setStrDebtAmount(StringUtils.defaultString(request.getParameter("hdnMonto")));
        messageBE.setStrPaymentOperationNumber(StringUtils.defaultString(request.getParameter("hdnNroTransaccion")));
        messageBE.setStrPaymentDate(formatDateToABDCPFormat(StringUtils.defaultString(request.getParameter("hdnFecha"))));
        messageBE.setStrIdProcess(StringUtils.defaultString(request.getParameter("hdnAplicationId")));
        messageBE.setStrPhoneNumber(StringUtils.defaultString(request.getParameter("hdnPhoneNumber")));
        messageBE.setStrAttachmentDoc(formatNameDocument(fileName, strAssignorId));
        messageBE.setDocument(dataBytes);

        String strMessage = null;
        HashMap hshWsMap = null;
        PortabilityOrderService objPortabilityOrderService = new PortabilityOrderService();

        PrintWriter out = response.getWriter();

        try {
            hshWsMap = objPortabilityOrderService.wsPortabilitySubSanacion(messageBE);
            strMessage = (String) hshWsMap.get("strMessage");

            if (StringUtils.isNotBlank(strMessage)) {
                System.out.println("Mensaje is not blank");
                response.setContentType("text/html");
                out.println("<script type=\"text/javascript\">");
                out.println("alert('" + strMessage + "');");
                out.println("</script>");
                out.println ("<script>");
                out.println ("window.close()");
                out.println ("</script>");
            } else {
                strMessage = "La operación se realizó con éxito";
                response.setContentType("text/html");
                out.println("<script type=\"text/javascript\">");
                out.println("alert('" + strMessage + "');");
                out.println("</script>");
                out.println ("<script>");
                out.println ("window.close()");
                out.println ("</script>");
            }
        } catch (Exception e) {
            logger.error(formatException(e));
            strMessage = e.getMessage();
            response.setContentType("text/html");
            out.println("<script type=\"text/javascript\">");
            out.println("alert('" + strMessage + "');");
            out.println("</script>");
            out.println ("<script>");
            out.println ("window.close()");
            out.println ("</script>");
        }

    }

    private String formatDateToABDCPFormat(String fechaCalendar) {
        DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat targetFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = null;
        try {
            date = originalFormat.parse(fechaCalendar);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);
        if(logger.isDebugEnabled()){
            logger.debug(formattedDate);
            logger.debug("Date : " + formattedDate);
        }
        return formattedDate;
    }

    private String formatNameDocument(String fileName, String strAssignorId){
        //Aplicamos el formato del nombre de la imagen (YYYMMDD)
        Calendar now = Calendar.getInstance();
        DecimalFormat mFormat= new DecimalFormat("00");
        String sNameFile = strAssignorId;
        sNameFile= sNameFile + String.valueOf(now.get(Calendar.YEAR)) +
                String.valueOf(mFormat.format(now.get(Calendar.MONTH)+ 1)) +
                String.valueOf(mFormat.format(now.get(Calendar.DAY_OF_MONTH)));
        //Aplicamos el formato del nombre de la imagen (Correlativo)
        Integer random = (int)(100000 * Math.random());
        sNameFile= sNameFile.trim() + random.toString().trim();
        //Aplicamos el formato del nombre de la imagen (Constante de Acreditacion de Pago)
        sNameFile= sNameFile + Constante.CONSTANTE_ACREDITACION_PAGO;
        // Concatenamos la extencion del archivo
        String sNameExt[] = fileName.split("\\.");
        sNameFile= sNameFile+ "." + sNameExt[1];
        System.out.println("[SubsanacionPortabilidad] Nombre Adjunto: " + sNameFile);
        return sNameFile;
    }

    private byte[] extractBytes (File file) {

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
            }
        } catch (IOException ex) {
            logger.error("Error: " + ex.getMessage());
        }

        byte[] bytes = bos.toByteArray();

        return bytes;
    }
}