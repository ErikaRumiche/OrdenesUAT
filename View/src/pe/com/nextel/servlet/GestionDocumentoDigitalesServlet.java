package pe.com.nextel.servlet;

import pe.com.nextel.service.GestionDocumentoDigitalesService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by LROQUE on 28/10/2016.
 * [PM0011173] LROQUE
 */
public class GestionDocumentoDigitalesServlet extends GenericServlet {
    private static final long serialVersionUID = 1L;

    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    GestionDocumentoDigitalesService gestionDocumentoDigitalesService = new GestionDocumentoDigitalesService();

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
            System.out.println("[GestionDocumentoDigitalesServlet][doGet], inicio");
            System.out.println("[GestionDocumentoDigitalesServlet][doGet], action: " + myaction);
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

    private void verDocumentoDigital(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
        System.out.println("[GestionDocumentoDigitalesServlet][verDocumentoDigital], INICIO");
        try {
            String idAceptaDoc = request.getParameter("idAceptaDoc");
            String userLogin = request.getParameter("userLogin");

            Map<String, Object> mapResult = gestionDocumentoDigitalesService.verDocumentoDigital(idAceptaDoc, userLogin);

            if(mapResult.get("contenidoPDF") != null){
                byte[] bytes = (byte[])mapResult.get("contenidoPDF");

                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition","attachment;filename=documento.pdf");

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
            System.out.println("[GestionDocumentoDigitalesServlet][verDocumentoDigital], Exception:"+e.getMessage());
        }finally{
            System.out.println("[GestionDocumentoDigitalesServlet][verDocumentoDigital], FIN");
        }
    }

    private void loadPopupDocDigitales(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception,ServletException,IOException {
        System.out.println("[GestionDocumentoDigitalesServlet][loadPopupDocDigitales], INICIO");
        try {
            String datosDocDig = request.getParameter("datosDocDig");
            String url = "JP_DIGITAL_DOCUMENTS_LISTShowPage.jsp?datosDocDig="+datosDocDig;
            String winUrl = "htdocs/JP_DIGITAL_DOCUMENTS_LIST/PopUpFrameDocDig.jsp?av_url=" + url;
            out.println("window.open(\""+winUrl+"\", \"DocumentosDigitales\",\"toolbar = no, location = no, directories = no, status = no, menubar = no, scrollbars = yes, resizable = yes, screenX = 60, top = 40, left = 60, screenY = 40, width = 710,height=410\"); ");
        }catch (Exception e){
            out.println("alert(\"Ocurrió un error al cargar el modulo de Documentos Digitales\")");
        }finally {
            System.out.println("[GestionDocumentoDigitalesServlet][loadPopupDocDigitales], FIN");
        }
    }

    @Override
    protected void executeDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
