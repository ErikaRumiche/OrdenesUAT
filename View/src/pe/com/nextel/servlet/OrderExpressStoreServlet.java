package pe.com.nextel.servlet;


import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.pattern.IntegerPatternConverter;
import pe.com.nextel.bean.OrderBean;
import pe.com.nextel.bean.OrderDetailBean;
import pe.com.nextel.service.OrderExpressStoreService;
import org.apache.log4j.Logger;

import pe.com.nextel.service.NewOrderService;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;


public class OrderExpressStoreServlet extends GenericServlet {

    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
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
           System.out.println("enrro al processRequest");       
            String action = (String) request.getParameter("method");                  
          
            if (action.equalsIgnoreCase("getServiceList")) {
                orderById(request, response);
            }else if(action.equalsIgnoreCase("saveOrderPaymentTE") ){
                saveOrderPaymentTE(request, response);
			}else if(action.equalsIgnoreCase("saveOrderPaymentTETempVep") ){
            saveOrderPaymentTETempVep(request, response);
			/* AYATACO - Inicio */
            }else if(action.equalsIgnoreCase("validateOrderExist")){
                validateOrderExist(request, response);
			/* AYATACO - Fin */
            }

    }
    //JLIMAYMANTA
    public void orderById(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        logger.info("********************INICIO OrderExpressStoreServlet > orderById********************");
            response.setCharacterEncoding("utf8");
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            try {    //validamos que sea de una orden de tienda expres
                String orderIds = request.getParameter("orderIdv");
                String userLogin = request.getParameter("userLogin");
                //INICIO: AMENDEZ | PRY-1141
                String paymenttype = request.getParameter("paymenttype");
                int iPaymenttype=Integer.parseInt(paymenttype);
                //FIN: AMENDEZ | PRY-1141
                        
                long orderIdi=Integer.parseInt(orderIds);

                logger.info("orderIds  : "+orderIds);
                logger.info("userLogin : "+userLogin);

                OrderExpressStoreService orderExpressStoreService=new OrderExpressStoreService();
                OrderDetailBean  orderDetailBean = orderExpressStoreService.getSearchOrderById(orderIdi,userLogin,iPaymenttype);

                Gson gson;
                gson = new Gson();

                String orderDetailBeans = gson.toJson(orderDetailBean);
                logger.info("{\"OrderDetailBean\":"+orderDetailBeans+"}");
                out.write("{\"OrderDetailBean\":"+orderDetailBeans+"}");
                out.close();
                            
            } catch (Exception exception) {
                System.out.print("error en OrderExpressStoreServlet ");
            }
        logger.info("********************FIN OrderExpressStoreServlet > orderById********************");
            
        }
    
    public void saveOrderPaymentTE(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        logger.info("********************INICIO OrderExpressStoreServlet > saveOrderPaymentTE********************");
        response.setCharacterEncoding("utf8");
        response.setContentType("text/html;charset=UTF-8");
        OrderExpressStoreService orderExpressStoreService=new OrderExpressStoreService();
        HashMap hshResult = new HashMap();
        String    strMessage = null;
        try {
            String flagSave = request.getParameter("hdnFlagSave");
            String hdnOrden = request.getParameter("hdnOrden");
            String hdnMonto = request.getParameter("hdnMonto");
            String hdnRa = request.getParameter("hdnRa");
            String hdnVoucher = request.getParameter("hdnVoucher");
            String hdnComentario = request.getParameter("hdnComentario");
            String hdnNumLoginn = request.getParameter("hdnNumLogin");
            String hdnUser = request.getParameter("hdnUser");
            //INICIO: AMENDEZ | PRY-1141
            String hdnPaymentType = request.getParameter("hdnPaymentType");
            String hdnPaymentOrderQuotaId = request.getParameter("hdnPaymentOrderQuotaId");
            int iPaymentType=Integer.parseInt(hdnPaymentType);
            long lPaymentOrderQuotaId=Long.parseLong(hdnPaymentOrderQuotaId);
            //FIN: AMENDEZ | PRY-1141

            long orderIdi=Integer.parseInt(hdnOrden);
            double monto=Double.valueOf(hdnMonto);

            logger.info("flagSave       :  "+flagSave);
            logger.info("hdnOrden       :  "+hdnOrden);
            logger.info("hdnMonto       :  "+hdnMonto);
            logger.info("hdnRa          :  "+hdnRa);
            logger.info("hdnVoucher     :  "+hdnVoucher);
            logger.info("hdnComentario  :  "+hdnComentario);
            logger.info("hdnNumLoginn   :  "+hdnNumLoginn);
            logger.info("hdnUser        :  "+hdnUser);
            //INICIO: AMENDEZ | PRY-1141
            logger.info("hdnPaymentType         :  "+hdnPaymentType);
            logger.info("hdnPaymentOrderQuotaId :  "+hdnPaymentOrderQuotaId);
            //FIN: AMENDEZ | PRY-1141
            logger.info("------Valores Convertidos------ ");
            logger.info("orderIdi   :  "+orderIdi);
            logger.info("monto      :  "+monto);

            //INICIO: AMENDEZ | PRY-1141
            logger.info("iPaymentType              :  "+iPaymentType);
            logger.info("lPaymentOrderQuotaId      :  "+lPaymentOrderQuotaId);
            //FIN: AMENDEZ | PRY-1141

            hshResult = orderExpressStoreService.saveOrderPaymentTE(orderIdi,monto,hdnRa,hdnVoucher,hdnComentario,hdnNumLoginn,hdnUser,iPaymentType,lPaymentOrderQuotaId);
            strMessage = (String)hshResult.get("strMessage");

            if(strMessage ==null){
                strMessage=Constante.OPERATION_STATUS_OK;
            }
            logger.info("strMessage      :  "+strMessage);
            request.getSession().setAttribute("strMessage",strMessage);

            RequestDispatcher rd=request.getRequestDispatcher("htdocs/JP_ORDER_PAYMENT_SEARCH/JP_ORDER_PAYMENT_TE_ShowPage.jsp");
            rd.forward(request, response);
        } catch (Exception exception) {
            logger.error("error en OrderExpressStoreServlet saveOrderPaymentTE ",exception);
        }

        logger.info("********************FIN OrderExpressStoreServlet > saveOrderPaymentTE********************");
    }

    public void saveOrderPaymentTETempVep(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("utf8");
        response.setContentType("text/html;charset=UTF-8");
        OrderExpressStoreService orderExpressStoreService=new OrderExpressStoreService();
        HashMap hshResult = new HashMap();
        String    strMessage = null;
        try {
            String flagSave = request.getParameter("hdnFlagSave");
            String hdnOrden = request.getParameter("hdnOrden");
            String hdnMonto = request.getParameter("hdnMonto");
            String hdnRa = request.getParameter("hdnRa");
            String hdnVoucher = request.getParameter("hdnVoucher");
            String hdnComentario = request.getParameter("hdnComentario");
            String hdnNumLoginn = request.getParameter("hdnNumLogin");
            // long hdnNumLogin=Integer.parseInt(hdnNumLoginn);
            String hdnUser = request.getParameter("hdnUser");

            //EFLORES 23/08/2017
            String hdnFlgVep = request.getParameter("hdnFlgVep");

            String hdnMontoFinanciado = hdnFlgVep.equals("0")||hdnFlgVep.equals("")?null:request.getParameter("hdnMontoFinanciado");
            String hdnNumCuotas = hdnFlgVep.equals("0")||hdnFlgVep.equals("")?null:request.getParameter("hdnNumCuotas");
            String strCuotaInicial = hdnFlgVep.equals("0")||hdnFlgVep.equals("")?null:request.getParameter("txtCuotaInicial");

            Double txtCuotaInicial =(strCuotaInicial==null || strCuotaInicial.equals(""))?null:Double.parseDouble(strCuotaInicial);

            System.out.println("[OrderExpressStoreServlet][saveOrderPaymentTE] hdnFlgVep "+hdnFlgVep);
            System.out.println("[OrderExpressStoreServlet][saveOrderPaymentTE] hdnMontoFinanciado "+hdnMontoFinanciado);
            System.out.println("[OrderExpressStoreServlet][saveOrderPaymentTE] hdnNumCuotas "+hdnNumCuotas);
            System.out.println("[OrderExpressStoreServlet][saveOrderPaymentTE] txtCuotaInicial "+txtCuotaInicial);
            // Fin EFLORES 23/08/2017

            long orderIdi=Integer.parseInt(hdnOrden);
            double monto=Double.valueOf(hdnMonto);
            //EFLORES 25/08/2017
            Integer flgVep=(hdnFlgVep==null || hdnFlgVep.equals(""))?null:MiUtil.parseInt(hdnFlgVep);
            Double montofinanc =(hdnMontoFinanciado==null || hdnMontoFinanciado.equals(""))?null:MiUtil.parseDouble(hdnMontoFinanciado);
            Integer numcuotas =(hdnNumCuotas==null || hdnNumCuotas.equals(""))?null:MiUtil.parseInt(hdnNumCuotas);
            //Fin EFLORES 25/08/2017

            hshResult = orderExpressStoreService.saveOrderPaymentTE(orderIdi,monto,hdnRa,hdnVoucher,hdnComentario,hdnNumLoginn,hdnUser,flgVep,txtCuotaInicial,montofinanc,numcuotas);
            strMessage = (String)hshResult.get("strMessage");

            if(strMessage ==null){
                strMessage=Constante.OPERATION_STATUS_OK;
            }
            request.getSession().setAttribute("strMessage",strMessage);

            RequestDispatcher rd=request.getRequestDispatcher("htdocs/JP_ORDER_PAYMENT_SEARCH/JP_ORDER_PAYMENT_TE_ShowPage.jsp");
            rd.forward(request, response);
        } catch (Exception exception) {
            exception.printStackTrace();
                        System.out.print("error en OrderExpressStoreServlet saveOrderPaymentTE "+exception);                
                }
           
            
        }

    /** AYATACO - Inicio */
    public void validateOrderExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("******************** INICIO OrderExpressStoreServlet > validateOrderExist ********************");
        response.setCharacterEncoding("utf8");
        response.setContentType("text/html;charset=UTF-8");
        OrderExpressStoreService orderExpressStoreService = new OrderExpressStoreService();
        HashMap hshResultDao = new HashMap();
        String strMessage = null;
        Integer cantOrder = 0;
        PrintWriter out = response.getWriter();

        try {

            String npsource = request.getParameter("npsource");
            String npsourceid = request.getParameter("npsourceid");

            logger.info("npsource ------> " + npsource);
            logger.info("npsourceid ------> " + npsourceid);

            Gson gson;
            gson = new Gson();

            hshResultDao = orderExpressStoreService.validateOrderExist(npsource, Integer.parseInt(npsourceid));
            out.write(gson.toJson(hshResultDao));

        } catch (Exception exception) {
            logger.error("Error en OrderExpressStoreServlet validateOrderExist ",exception);
        }
        logger.info("******************** FIN OrderExpressStoreServlet > validateOrderExist ********************");
    }
    /** AYATACO - Fin */
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void executeDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
   
}