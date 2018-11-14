package pe.com.nextel.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;

import pe.com.nextel.form.OrderSearchForm;
import pe.com.nextel.form.PCMForm;
import pe.com.nextel.form.RetentionForm;
import pe.com.nextel.service.OrderSearchService;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;


///////////////////////////////////////////////////////////


public class OrderSearchServlet extends GenericServlet {

  /**
   * Motivo:  Método que se invoca al hacer click en el Botón Buscar.
   * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
   * <br>Fecha: 22/08/2007        
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  public void searchOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    if (logger.isDebugEnabled())
    logger.debug("--Inicio--");
    try {
      logger.debug("Cambios Fuertes - RCDLRP - RDELOSREYES");
      OrderSearchForm objOrderSearchForm = new OrderSearchForm();
      objOrderSearchForm = (OrderSearchForm) objOrderSearchForm.populateForm(request);
      objOrderSearchForm.setHdnNumPagina(StringUtils.defaultString(request.getParameter("hdnNumPagina"),"1"));
      objOrderSearchForm.setHdnNumRegistros(StringUtils.defaultString(request.getParameter("hdnNumRegistros"),"15"));
      request.setAttribute("objOrderSearchForm", objOrderSearchForm);
      if(logger.isDebugEnabled()) {
        logger.debug(objOrderSearchForm);
        logger.debug("Antes de hacer el getRequestDispatcher");
      }
      long lOrderId = MiUtil.parseLong(objOrderSearchForm.getTxtNroOrden());
      HashMap hshDataMap = objOrderSearchService.existOrder(lOrderId);
      Boolean flagExistOrder = (Boolean) hshDataMap.get("flagExistOrder");
      String strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
      if(StringUtils.isNotBlank(strMessage)) {
        throw new ServletException(strMessage);
      }
      if("1".equals(objOrderSearchForm.getChkFastSearch()) && StringUtils.isNotBlank(objOrderSearchForm.getTxtNroOrden()) && flagExistOrder.booleanValue()) {
        PrintWriter out = response.getWriter();
        logger.debug("Antes de hacer el sendRedirect");
        String strUrl="/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid="+objOrderSearchForm.getTxtNroOrden()+"&av_execframe=BOTTOMFRAME";
        out.print("<script>parent.bottomFrame.location.replace('"+strUrl+"');</script>");
      } else {
        request.getRequestDispatcher("pages/manageForm.jsp").forward(request, response);
      }
    } catch(Exception e) {
      logger.error(formatException(e));
    }
  }
	
	/**
     * Motivo:  Método que se invoca al seleccionar el Combo de Division de Negocio
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 23/09/2007        
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
  public void loadSolution(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String cmbDivisionNegocio = StringUtils.defaultString(request.getParameter("cmbDivisionNegocio"), "0");
    long lDivisionId = Long.parseLong(cmbDivisionNegocio);
    if(logger.isDebugEnabled())
      logger.debug("lDivisionId: "+lDivisionId);
    try{
      HashMap hshDataMap = objGeneralService.getSolucionList(lDivisionId);
      request.setAttribute(Constante.DATA_STRUCT, hshDataMap);
      if(logger.isDebugEnabled())
        logger.debug("Antes de hacer el getRequestDispatcher");
      request.getRequestDispatcher("pages/loadSolution.jsp").forward(request, response);
    }catch(Exception e){
      logger.error(formatException(e));
    }
  }
	
	/**
     * Motivo:  Método que se invoca al seleccionar el Combo de Solucion de Negocio
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 23/09/2007        
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void loadCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                                                                                             IOException {
        String cmbSolucionNegocio = StringUtils.defaultString(request.getParameter("cmbSolucionNegocio"), "0");
        long lSolutionId = Long.parseLong(cmbSolucionNegocio);
        if(logger.isDebugEnabled())
			logger.debug("lSolutionId: "+lSolutionId);
        try {
            HashMap hshDataMap = objGeneralService.getCategoryList(lSolutionId);
            request.setAttribute(Constante.DATA_STRUCT, hshDataMap);
            if(logger.isDebugEnabled())
				logger.debug("Antes de hacer el getRequestDispatcher");
            request.getRequestDispatcher("pages/loadCategory.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error(formatException(e));
        }
    }
	
	/**
     * Motivo:  Método que se invoca al seleccionar el Combo de Categoria
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 23/09/2007        
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void loadSubCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                                                                                             IOException {
        String cmbSolucionNegocio = StringUtils.defaultString(request.getParameter("cmbSolucionNegocio"), "0");
        long lSolutionId = Long.parseLong(cmbSolucionNegocio);
        if(logger.isDebugEnabled())
			logger.debug("lSolutionId: "+lSolutionId);
        String cmbCategoria = StringUtils.defaultString(request.getParameter("cmbCategoria"));
        if(logger.isDebugEnabled())
			logger.debug("cmbCategoria: " + cmbCategoria);
        try {
			HashMap hshDataMap = objGeneralService.getSubCategoryList(cmbCategoria, lSolutionId);
            request.setAttribute(Constante.DATA_STRUCT, hshDataMap);
            if(logger.isDebugEnabled())
				logger.debug("Antes de hacer el getRequestDispatcher");
            request.getRequestDispatcher("pages/loadSubCategory.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error(formatException(e));
        }
    }

    /**
     * @see GenericServlet#executeDefault(HttpServletRequest, HttpServletResponse)
     */
    protected void executeDefault(HttpServletRequest request, HttpServletResponse response) {
        try {
			PrintWriter out = response.getWriter();
            logger.debug("Antes de hacer el sendRedirect");
            String strUrl="/portal/page/portal/orders/ORDER_SEARCH";
            out.print("<script>parent.mainFrame.location.replace('"+strUrl+"');</script>");
        } catch (Exception e) {
            logger.error(formatException(e));
        }
    }
    
    
    
    /**
   * Motivo:  Método que se invoca al hacer click en el Botón Buscar Retenciones
   * <br>Realizado por: <a href="mailto:julio.herrera@nextel.com.pe">Julio Herrera</a>
   * <br>Fecha: 22/07/2009
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  public void searchRetention(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    if (logger.isDebugEnabled())
      logger.debug("--Inicio--");
    try {
      RetentionForm retentionForm = (RetentionForm) request.getSession(true).getAttribute("retentionForm");
      if (retentionForm == null) {
        retentionForm = new RetentionForm();
        retentionForm = (RetentionForm) retentionForm.populateForm(request);
      }
      String strHdnNumPagina = StringUtils.defaultString(request.getParameter("hdnNumPagina"), "1");
      strHdnNumPagina = strHdnNumPagina.replaceAll(",","");
      retentionForm.setHdnNumPagina(strHdnNumPagina);
      retentionForm.setHdnNumRegistros(StringUtils.defaultString(request.getParameter("hdnNumRegistros"), 
String.valueOf(Constante.NUM_REGISTROS_X_PAGINAS)));
      request.setAttribute("retentionForm", retentionForm);
      request.getSession(true).setAttribute("retentionForm", retentionForm);
      if (logger.isDebugEnabled()) {
        logger.debug("Antes de hacer el getRequestDispatcher");
      }
      String parameterPage = new ParamEncoder("retention").encodeParameterName(TableTagParameters.PARAMETER_PAGE);
      String hdnNumPagina = StringUtils.defaultIfEmpty(retentionForm.getHdnNumPagina(),"1");
      request.getRequestDispatcher("reports/reportRetList.jsp?"+parameterPage+"="+hdnNumPagina).forward(request, response);
      //tagRCDPLRP: request.getRequestDispatcher("pages/redirectSearchRepair.jsp").forward(request, response);
    } catch (Exception e) {
      logger.error(formatException(e));
    }
  }
  
  /**
   * Motivo:  Método que se invoca al hacer click en el Botón de Nueva_Busqueda desde la ventana de resultados de retenciones programadas
   * <br>Realizado por: <a href="mailto:julio.herrera@nextel.com.pe">Julio Herrera</a>
   * <br>Fecha: 22/07/2009
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  public void searchFormRetention(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    if (logger.isDebugEnabled())
      logger.debug("--Inicio--");
    try {
      RetentionForm retentionForm = (RetentionForm) request.getSession(true).getAttribute("retentionForm");
      if (retentionForm == null) {
        retentionForm = new RetentionForm();
        retentionForm = (RetentionForm) retentionForm.populateForm(request);
      }
      retentionForm.setHdnNumPagina(StringUtils.defaultString(request.getParameter("hdnNumPagina"), "1"));
      retentionForm.setHdnNumRegistros(StringUtils.defaultString(request.getParameter("hdnNumRegistros"), 
String.valueOf(Constante.NUM_REGISTROS_X_PAGINAS)));
      request.setAttribute("retentionForm", retentionForm);
      request.getSession(true).removeAttribute("retentionForm");
      if (logger.isDebugEnabled()) {
        logger.debug("Antes de hacer el getRequestDispatcher");
      }
      String parameterPage = new ParamEncoder("repair").encodeParameterName(TableTagParameters.PARAMETER_PAGE);
      String hdnNumPagina = StringUtils.defaultIfEmpty(retentionForm.getHdnNumPagina(),"1");
      //request.getRequestDispatcher("busqueda/repairForm.jsp").forward(request, response);
      request.getRequestDispatcher("reports/suspRetStatsForm.jsp").forward(request, response);
      //tagRCDPLRP: request.getRequestDispatcher("pages/redirectSearchRepair.jsp").forward(request, response);
    } catch (Exception e) {
      logger.error(formatException(e));
    }
  }

  /**
   * Motivo:  Método que se invoca al hacer click en el Botón Buscar desde la ventana de Procesos Compuestos Masivos
   * <br>Realizado por: <a href="mailto:julio.herrera@nextel.com.pe">Julio Herrera</a>
   * <br>Fecha: 23/03/2009
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  public void searchPCM(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      PCMForm pcmForm = (PCMForm) request.getSession(true).getAttribute("pcmForm");
      if (pcmForm == null) {
        pcmForm = new PCMForm();
      }
      pcmForm = (PCMForm) pcmForm.populateForm(request);
      
      pcmForm.setHdnNumPagina((StringUtils.defaultString(request.getParameter("hdnNumPagina"), "1")).replaceAll(",",""));      
      pcmForm.setHdnNumRegistros(StringUtils.defaultString(request.getParameter("hdnNumRegistros"), String.valueOf(Constante.NUM_REGISTROS_X_PAGINAS)));
      
      request.setAttribute("pcmForm", pcmForm);
      request.getSession(true).setAttribute("pcmForm", pcmForm);
      
      OrderSearchService orderSearchService = new OrderSearchService();
      HashMap hshPCMListMap = orderSearchService.getPCMList(pcmForm);
      
      request.setAttribute("hshPCMListMap", hshPCMListMap);
      
      request.getRequestDispatcher("reports/reportPCMList.jsp").forward(request, response);
    } catch (Exception e) {
      logger.error(formatException(e));
    }
  }
  
  
  /**
   * Motivo:  Método que se invoca al hacer click en el Botón de Nueva_Busqueda desde la ventana de resultados del reporte PCM
   * <br>Realizado por: <a href="mailto:julio.herrera@nextel.com.pe">Julio Herrera</a>
   * <br>Fecha: 22/07/2009
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
   
  public void searchFormPCM(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      PCMForm pcmForm = (PCMForm) request.getSession(true).getAttribute("pcmForm");
      if (pcmForm == null) {
        pcmForm = new PCMForm();
        pcmForm = (PCMForm)pcmForm.populateForm(request);
      }
      
      request.setAttribute("pcmForm", pcmForm);
      request.getSession(true).removeAttribute("pcmForm");

      request.getRequestDispatcher("reports/suspPCMForm.jsp").forward(request, response);
    } catch (Exception e) {
      logger.error(formatException(e));
    }
  }
    
    
    
    
    
    
}