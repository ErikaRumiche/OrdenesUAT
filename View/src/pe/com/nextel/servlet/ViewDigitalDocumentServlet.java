package pe.com.nextel.servlet;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import pe.com.nextel.service.DigitalDocumentService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;
import pe.com.nextel.util.Constante;

/**
 * Created by Administrador on 03/05/2017.
 */
public class ViewDigitalDocumentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger= Logger.getLogger(DigitalDocumentServlet.class);
    private static final Gson gson=new Gson();
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    private static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
    DigitalDocumentService digitalDocumentService = new DigitalDocumentService();

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            String myaction=request.getParameter("myaction");
            System.out.println("[ViewDigitalDocumentServlet][doGet], inicio");
            System.out.println("[ViewDigitalDocumentServlet][doGet], action: " + myaction);
            if(myaction!=null) {
                if (myaction.equals("loadPopupDocDig")) {
                    PrintWriter out = response.getWriter();
                    try {
                        out.println("<html>");
                        out.println("<head><title>Documentos Digitales</title></head>");
                        out.println("<script >");
                        loadPopupDocDigitales(request, response, out);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        out.println("</script>");
                        out.close();
                    }
                }else if(myaction.equals("verDocDig")){
                    verDocumentoDigital(request, response);
                }else if(myaction.equals("getDocGenList")) {
                    response.setContentType(CONTENT_TYPE_JSON);
                    viewDocumentList(request, response, Constante.DIGITAL_DOCUMENT_GENERATED);
                }else if(myaction.equals("getDocChrList")) {
                    response.setContentType(CONTENT_TYPE_JSON);
                    viewDocumentList(request, response, Constante.ATTACHE_DIGITAL_DOCUMENTS);
                }else if(myaction.equals("getAttDocListFlag")) {
                    response.setContentType(CONTENT_TYPE_JSON);
                    getAttDocListFlag(request, response);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getWriter().write(e.getMessage());
            response.flushBuffer();
        }
    }

    private void loadPopupDocDigitales(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception,ServletException,IOException {
        System.out.println("[ViewDigitalDocumentServlet][loadPopupDocDigitales], INICIO");
        try {
            String datosDocDig = request.getParameter("datosDocDig");
            String url = "JP_DIGITAL_DOCUMENTS_LISTShowPage.jsp?datosDocDig="+datosDocDig;
            String winUrl = "htdocs/JP_DIGITAL_DOCUMENTS_LIST/PopUpFrameDocDig.jsp?av_url=" + url;
            out.println("window.open(\""+winUrl+"\", \"DocumentosDigitales\",\"toolbar = no, location = no, directories = no, status = no, menubar = no, scrollbars = yes, resizable = yes, screenX = 60, top = 40, left = 60, screenY = 40, width = 710,height=410\"); ");
        }catch (Exception e){
            out.println("alert(\"Ocurrió un error al cargar el modulo de Documentos Digitales\")");
        }finally {
            System.out.println("[ViewDigitalDocumentServlet][loadPopupDocDigitales], FIN");
        }
    }

    private void verDocumentoDigital(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
        logger.info("[ViewDigitalDocumentServlet][verDocumentoDigital], INICIO");
        try {
            String idAceptaDoc = request.getParameter("idAceptaDoc");
            String tipoCarga = request.getParameter("tipoCarga");
            String userLogin = request.getParameter("userLogin");
            String trxId = request.getParameter("trxId");
            int source = Integer.parseInt(request.getParameter("source"));

            logger.info("[ViewDigitalDocumentServlet][verDocumentoDigital] idAceptaDoc=" + idAceptaDoc + " tipoCarga=" + tipoCarga  + " userLogin=" + userLogin + " trxId=" + trxId + " source=" + source);

            Map<String, Object> mapResult = digitalDocumentService.verDocumentoDigital(idAceptaDoc,userLogin,trxId,source);

            if(mapResult.get("contenidoFile") != null){
                byte[] bytes = (byte[])mapResult.get("contenidoFile");
                String mimeType = (String)mapResult.get("mimeType");
                String fileName = (String)mapResult.get("fileName");

                if (tipoCarga.equals(Constante.DIGITAL_DOCUMENT_GENERATED)){
                    if (mimeType.equals(Constante.DOCUMENT_MIME_TYPE_PDF)) {
                        fileName = fileName + "." + Constante.DOCUMENT_EXT_PDF;
                    }else if(mimeType.equals(Constante.DOCUMENT_MIME_TYPE_JPEG) || mimeType.equals(Constante.DOCUMENT_MIME_TYPE_PJPEG)) {
                        fileName = fileName + "." + Constante.DOCUMENT_EXT_JPG;
                    } else{
                        fileName = fileName + "." + Constante.DOCUMENT_EXT_PDF;
                    }
                }

                System.out.println("[ViewDigitalDocumentServlet][verDocumentoDigital], fileName= "+ fileName +" mimeType= "+mimeType);

                response.setContentType((String)mapResult.get("mimeType"));
                response.setHeader("Content-Disposition","attachment;filename="+fileName);

                InputStream in = new ByteArrayInputStream(bytes);
                ServletOutputStream out = response.getOutputStream();
                byte[] outputByte = new byte[4096];
                while(in.read(outputByte, 0, 4096) != -1){
                    out.write(outputByte, 0, 4096);
                }
                in.close();
                out.flush();
                out.close();
            }
        }catch(Exception e) {
            System.out.println("[ViewDigitalDocumentServlet][verDocumentoDigital], Exception:"+e.getMessage());
        }finally{
            System.out.println("[ViewDigitalDocumentServlet][verDocumentoDigital], FIN");
        }
    }

    private void viewDocumentList(HttpServletRequest request, HttpServletResponse response, String tipoCarga)throws ServletException,IOException{
        PrintWriter out = response.getWriter();
        String strOrderId = request.getParameter("npOrderId");
        String strIncidentId = request.getParameter("npIncidentId");
        String userLogin = request.getParameter("userLogin");
        String customerDoctype = request.getParameter("strCustomerDocType");
        String customerNumDoc = request.getParameter("strCustomerNumDoc");

        Map<String, Object> mapResult =null;
        logger.info("[ViewDigitalDocumentServlet][viewDocumentList], INICIO");
        logger.info("[ViewDigitalDocumentServlet][viewDocumentList], strCustomerNumDoc = " + customerNumDoc);
        try {
            DigitalDocumentService service = new DigitalDocumentService();
            mapResult = service.getListDigitalDocuments(strOrderId,strIncidentId,customerNumDoc,"",userLogin,tipoCarga);
            logger.info("lista final: " + mapResult.get("documentList"));
        }catch(Exception e) {
            logger.info("[ViewDigitalDocumentServlet][viewDocumentList], Exception:" + e.getMessage());
            mapResult.put("statusOrden", Constante.COD_ERROR_DOC_LIST);
            mapResult.put(Constante.MESSAGE_OUTPUT,e.getMessage());
        }finally{
            logger.info("[ViewDigitalDocumentServlet][viewDocumentList], Result: "+gson.toJson(mapResult));
            logger.info("[ViewDigitalDocumentServlet][viewDocumentList], FIN");
            out.write(gson.toJson(mapResult));
        }
    }

    private void getAttDocListFlag(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        logger.info("[ViewDigitalDocumentServlet][getAttDocListFlag], INICIO");
        PrintWriter out = response.getWriter();
        int orderid, incidentid;
        orderid = Integer.parseInt(request.getParameter("nporderid"));
        incidentid = Integer.parseInt(request.getParameter("npincidentid"));
        Map<String, Object> mapResult =null;
        logger.info("[ViewDigitalDocumentServlet][getAttDocListFlag], orderid: " + orderid + " incidentid: " + incidentid);
        mapResult = digitalDocumentService.getAttDocListFlag(orderid, incidentid);
        out.write(gson.toJson(mapResult));
    }
}