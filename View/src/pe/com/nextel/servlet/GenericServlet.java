package pe.com.nextel.servlet;

import java.io.IOException;
import java.io.Serializable;

import java.lang.reflect.InvocationTargetException;

import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import pe.com.nextel.bean.PortalSessionBean;
import pe.com.nextel.service.*;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.GenericObject;
import pe.com.nextel.util.RequestHashMap;


public abstract class GenericServlet extends HttpServlet implements Serializable {
  
	protected static Logger logger = Logger.getLogger(GenericServlet.class);
	protected CustomerService objCustomerService = new CustomerService();
	protected GeneralService objGeneralService = new GeneralService();
	protected RetailService objRetailService = new RetailService();
	protected OrderSearchService objOrderSearchService = new OrderSearchService();
	protected OrderTabsService objOrderTabsService = new OrderTabsService();
	protected EditOrderService objOrderService = new EditOrderService();  
	protected RequestHashMap objHashMap = new RequestHashMap();
	protected BillingAccountService objBillAccService = new BillingAccountService();   
	protected SiteService objSiteService = new SiteService();
	protected AccountService objAccountService = new AccountService();
	protected RepairService objRepairService = new RepairService();
	protected PortalSessionBean objPortalSesBean = new PortalSessionBean();
  protected SalesDataService objSalesDataService = new SalesDataService();
    //DERAZO REQ-0428
    protected PenaltyService objPenaltyService = new PenaltyService();
    //JCASTILLO
    protected DigitalDocumentService objDigitalDocumentService  = new DigitalDocumentService();

	public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected String formatException(Throwable e) {
        return GenericObject.formatException(e);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processGenericServlet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processGenericServlet(request, response);
    }

    protected void processGenericServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strMetodo = StringUtils.defaultString(request.getParameter(Constante.SERVLET_METHOD));
        try {
            /*if(logger.isDebugEnabled())
				logger.debug("strMetodo: "+strMetodo);*/
            if (StringUtils.isNotBlank(strMetodo)) {
                Class[] parametros = new Class[] {HttpServletRequest.class, HttpServletResponse.class};
                /*if(logger.isDebugEnabled())
					logger.debug("oClass:::" + ArrayUtils.toString(parametros));*/
                getClass().getMethod(strMetodo, parametros).invoke(this, new Object[] {request, response});
                /*if(logger.isDebugEnabled())
					logger.debug("Ejecuto correctamente el metodo");*/
            } else {
                executeDefault(request, response);
            }
        } catch (NoSuchMethodException nsme) {
            logger.error(formatException(nsme));
            executeDefault(request, response);
        } catch (IllegalAccessException iae) {
            logger.error(formatException(iae));
            executeDefault(request, response);
        } catch (InvocationTargetException ite) {
            logger.error(formatException(ite));
            logger.error(ite.getTargetException());
            executeDefault(request, response);
        }
    }

    public RequestHashMap getParameterNames(HttpServletRequest request) {
        RequestHashMap objHashMap =  new RequestHashMap();
        Enumeration paramNames = request.getParameterNames();
            //Mientras hayan parametros
            while(paramNames.hasMoreElements()) {
            //Recogemos el nombre del parametro
            String paramName = (String)paramNames.nextElement();
            
            //System.out.println("Nombre parametro : " + paramName + "");
            
            //objHashMap.put("paramName",paramName);
            
            //Obtengo los valores del parametro ( Pueden venir mas de uno )
            String[] paramValues = request.getParameterValues(paramName);
            //if( paramName.equals("hdnOrigFactura") )
              //System.out.println("Nombre parametro : " + paramName + "");
            
            //System.out.println("Cantidad de valores : " + paramValues.length + "");
            
              //Si solo viene un valor no es un arreglo
              if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                /*if (paramValue.trim().length() == 0)
                  objHashMap.put(paramName,null);
                else*/
                  objHashMap.put(paramName,paramValue);
                  
              } else {
                //Es un arreglo de String
                String[] paramValue = new String[paramValues.length];
                for(int i=0; i<paramValues.length; i++) {
                  paramValue[i] = paramValues[i];
                }
                objHashMap.put(paramName,paramValue);
              }
            }
                  
                  
        return objHashMap; 
    }

	protected abstract void executeDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
