package pe.com.nextel.servlet;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;

import pe.com.nextel.service.UploadService;
import pe.com.nextel.util.Constante;


/**
 * Gestiona el Upload de Archivos
 *
 * @author Richard De los Reyes
 *
 */
public class UploadServlet extends GenericServlet {

  public void uploadFileByMassivePropio(HttpServletRequest request, HttpServletResponse response) throws ServletException, FileUploadException, IOException {
		if (logger.isDebugEnabled())
			logger.debug("Inicio");
		try {
			UploadService uploadService = new UploadService();
			DiskFileUpload diskFileUpload = new DiskFileUpload();
			FileItemFactory factory = diskFileUpload.getFileItemFactory();
			FileUpload upload = new FileUpload(factory);
			List items = upload.parseRequest(request);
			HashMap hshDataMap = uploadService.manageUploadedFilesByMassivePropio(items);
			request.setAttribute(Constante.DATA_STRUCT, hshDataMap);
		} finally {
			request.getRequestDispatcher("pages/buildTableSimByPrepago.jsp").forward(request, response);
			if (logger.isDebugEnabled())
				logger.debug("Fin");
		}
	}

	public void uploadFile(HttpServletRequest request, HttpServletResponse response) throws ServletException, FileUploadException, IOException {
		if (logger.isDebugEnabled())
			logger.debug("Inicio");
		try {
			UploadService uploadService = new UploadService();
			DiskFileUpload diskFileUpload = new DiskFileUpload();
			FileItemFactory factory = diskFileUpload.getFileItemFactory();
			FileUpload upload = new FileUpload(factory);
			List items = upload.parseRequest(request);
			Long lOrderId = Long.valueOf(request.getParameter("hdnOrderId"));
			Long lDispatchPlace = Long.valueOf(request.getParameter("cmbLugarAtencion"));
			HashMap hshDataMap = uploadService.manageUploadedFiles(items, lOrderId, lDispatchPlace);
			request.setAttribute(Constante.DATA_STRUCT, hshDataMap);
      request.getSession(true).setAttribute(Constante.DATA_STRUCT, hshDataMap);
		} finally {
			request.getRequestDispatcher("pages/buildTableImeiSim.jsp").forward(request, response);
			if (logger.isDebugEnabled())
				logger.debug("Fin");
		}
	}

	protected void executeDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("executeDefault");
		}
	}

}
