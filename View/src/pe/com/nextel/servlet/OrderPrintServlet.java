package pe.com.nextel.servlet;

import org.apache.commons.lang.StringUtils;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import pe.com.nextel.exception.BpelException;
import pe.com.nextel.exception.UserException;
import pe.com.nextel.form.OrderSearchForm;
import pe.com.nextel.form.PCMForm;
import pe.com.nextel.form.RetentionForm;
import pe.com.nextel.service.OrderExpressStoreService;
import pe.com.nextel.service.OrderPrintLabelService;
import pe.com.nextel.service.OrderSearchService;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;


public class OrderPrintServlet extends HttpServlet {

    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

        String metodo = request.getParameter("method");

        String resul;
        Method method = null;
        try {
            System.out.println("[OrderPrintServlet]doGet  return  metodo");
            method = this.getClass().getMethod(metodo, new Class[] { HttpServletRequest.class, HttpServletResponse.class });
            resul = "" + (String) method.invoke(this, new Object[] { request, response });
            //System.out.println("Resultado-->"+resul);
        }catch(InvocationTargetException ite){
            String strMessage = ite.getTargetException().getMessage();
            resul = "Error [OrderPrintServlet][doGet][" + ite.getMessage() + "]";
        }catch (Exception ex) {
            resul = "Error [OrderPrintServlet][doGet][" + ex.getMessage() + "]";
        }
        String cad = resul.substring(0,resul.length() - 4);
        response.getWriter().write(cad);
        System.out.println("[OrderPrintServlet]Fin - doGet");
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        response.setContentType(CONTENT_TYPE);
        System.out.println("[OrderPrintServlet]Inicio - doPost");
        doGet(request, response);
        System.out.println("[OrderPrintServlet]Fin - doPost");
    }


    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("entro al processRequest");
        String action = (String) request.getParameter("method");

        //if (action.equalsIgnoreCase("validaOrderPrintLabel")) {
        validaOrderPrintLabel(request, response);
        //}

    }

    /**
     * Motivo:  Metodo que se invoca al hacer click en el Boton Buscar.
     * <br>Realizado por: <a href="mailto:jorge.gabriel@teamsoft.com.pe">Jorge Gabriel</a>
     * <br>Fecha: 07/08/2015
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void validaOrderPrintLabel(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
            String orderIds = request.getParameter("orderIdv");

            long orderIdi=Integer.parseInt(orderIds);
            ArrayList itemsList = new ArrayList();

            System.out.println("=======orderId recuperado en el servelt OrderPrintServlet metodo validaOrderPrintLabel======== ["+orderIdi+"]");

            OrderPrintLabelService orderPrintLabelService=new OrderPrintLabelService();
            HashMap hshDataMap  = orderPrintLabelService.valOrderPrintLabel(orderIdi);
            String strMessage = (String) hshDataMap.get("strMessage");

            Gson gson;
            gson = new Gson();

            if(StringUtils.isNotBlank(strMessage)) {
                //throw new ServletException(strMessage);
                strMessage = gson.toJson(strMessage);
                out.write("{\"strMessage\":"+strMessage+"}");
            }else{
                itemsList = (ArrayList) hshDataMap.get("objArrayList");

                String orderDetailBeans = gson.toJson(itemsList);
                out.write("{\"OrderDetailBean\":"+orderDetailBeans+"}");

            }
            out.close();
        } catch (Exception exception) {
            out.print("error en OrderPrintServlet metodo validaOrderPrintLabel ");
        }
    }

    /**
     * Motivo:  Metodo que se invoca al hacer click en el Boton Buscar.
     * <br>Realizado por: <a href="mailto:jorge.gabriel@teamsoft.com.pe">Jorge Gabriel</a>
     * <br>Fecha: 07/08/2015
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void sendPrintLabels(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String orderIds = request.getParameter("orderId");
            String customerIds = request.getParameter("customerId");
            String customerName = request.getParameter("customerName");
            String specificationIds = request.getParameter("specificationId");
            String itemsId = request.getParameter("itemsId");

            String buildingIds = request.getParameter("buildingId");
            String wsLogin = request.getParameter("wsLogin");

            long orderId=Integer.parseInt(orderIds);
            long customerId=Integer.parseInt(customerIds);
            long specificationId=Integer.parseInt(specificationIds);
            long buildingId=Integer.parseInt(buildingIds);

            System.out.println("=======orderId recuperado en el servelt OrderPrintServlet metodo sendPrintLabels======== ["+orderIds+"]");

            OrderPrintLabelService orderPrintLabelService=new OrderPrintLabelService();
            String strMessage = orderPrintLabelService.sendPrintLabels( orderId,  specificationId ,  itemsId,  customerId,  customerName,wsLogin,buildingId);

            if(strMessage==null || strMessage.isEmpty()) strMessage="Se envío a imprimir exitosamente";
            out.write(strMessage);
            out.close();
        } catch (Exception exception) {
            out.print("error en OrderPrintServlet metodo sendPrintLabels ");
        }
    }
}