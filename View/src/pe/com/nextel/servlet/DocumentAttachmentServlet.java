package pe.com.nextel.servlet;

import com.google.gson.Gson;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import pe.com.nextel.service.DigitalDocumentService;
import pe.com.nextel.util.MiUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

/**
 * Created by HP on 28/04/2017.
 */
public class DocumentAttachmentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger= Logger.getLogger(DigitalDocumentServlet.class);
    private static final Gson gson=new Gson();
    private static final String CONTENT_TYPE_TEXT = "text/html; charset=UTF-8";
    private static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
    DigitalDocumentService digitalDocumentService = new DigitalDocumentService();

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE_TEXT);
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        logger.info("[DocumentAttachmentServlet][setDocumentAttachment], INICIO");
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletContext servletContext = this.getServletConfig().getServletContext();
            //File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
            //factory.setRepository(repository);
            response.setCharacterEncoding("UTF-8");
            String message="OK";
            PrintWriter out = response.getWriter();
            out.write(message);
            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);
            // Parse the request
            List<FileItem> items = upload.parseRequest(request);
            for(FileItem fileItem:items){
                logger.info("Nombre Campo: "+fileItem.getFieldName()+" Nombre: "+fileItem.getName()+" ContenctType "+
                        fileItem.getContentType()+" inMemory: "+fileItem.isInMemory()+ " Size: "+fileItem.getSize());
            }

            String trxId = MiUtil.getString(request.getParameter("trxId"));
            String source = MiUtil.getString(request.getParameter("source"));
            String userLogin = MiUtil.getString(request.getParameter("userLogin"));
            String customerDoctype = MiUtil.getString(request.getParameter("strCustomerDocType"));
            String customerNumDoc = MiUtil.getString(request.getParameter("strCustomerNumDoc"));


            logger.info("Obtener datos para el servicio Servlet : " +
                    " \"trxId\":\""+trxId+"\",\n" +
                    " \"source\":\""+source+"\",\n" +
                    " \"userLogin\":\""+userLogin+"\",\n" +
                    " \"strCustomerDocType\":\""+customerDoctype+"\",\n" +
                    " \"strCustomerNumDoc\":\""+customerNumDoc+"\"");


            HashMap hashMapDoc = digitalDocumentService.sendAttachedDocuments(items,source,trxId,userLogin,customerDoctype,customerNumDoc);
            out.write(gson.toJson(hashMapDoc));
            out.close();
        }catch (FileUploadException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("[DocumentAttachmentServlet][setDocumentAttachment], Fin");
    }
}
