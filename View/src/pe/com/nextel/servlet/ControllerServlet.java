package pe.com.nextel.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pe.com.nextel.util.Constante;


public class ControllerServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        System.out.println("Servlet - Si funcas Galindo Gana");
    }

    public void doGet(HttpServletRequest request, 
                      HttpServletResponse response) throws ServletException, IOException {
                      response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        
        // Funcionalidad de Busqueda
        String strCodigoCliente = null;
        String strNombreCliente = null;
        
        // Funcionalidad de Grabacion
        String strCustomerId = null;
        String strStatus = null;
        String strUsuario = null;

        int intNuevaOrden = 0;
        int intTipo = 0;

        String strMensaje = null;

        String strMyaction = request.getParameter("myaction");

        // BUSCAR
        // Valores de salida
         
        out.println("<script >");

        if ( strMyaction != null ) {

            if(strMyaction.equals("buscar") ) {
            
                strCodigoCliente = request.getParameter("txtCodigoCliente");
                /*
                Customer c = new Customer();
                c.setCustomerId(Integer.parseInt(v_codigoCliente));
                
                CustomerDao.ubicar(c);
                 strNombreCliente = c.getName();
                */
                strCodigoCliente = "123456";
                strNombreCliente = "IBM DEL PERU";
                
                out.println("parent.mainFrame.frmdatos.txtCustomerId.value ='"    + strCodigoCliente + "';");
                out.println("parent.mainFrame.frmdatos.txtNombreCliente.value ='" + strNombreCliente + "';");

               
            }
            else if(strMyaction.equals("grabar") ) {
            
                strCustomerId = request.getParameter("txtCustomerId");
                strCodigoCliente = strCustomerId;
                strNombreCliente = request.getParameter("txtNombreCliente");
                
                strStatus = request.getParameter("txtStatus");
                strUsuario = request.getParameter("txtUsuario");
                intTipo = Integer.parseInt(request.getParameter("txtTipo"));       
                       
                /*
                Order o = new Order();
                
                o.setCustomerId(Integer.parseInt(v_customerId));
                o.setTypeId(n_tipo);
                o.setStatus(v_status);
                o.setUser(v_usuario);
                
                intNuevaOrden = OrderDao.grabar(o) ;
                */
                
                intNuevaOrden = 20 ; 

                out.println("parent.mainFrame.frmdatos.txtNuevaOrden.value ='" + intNuevaOrden + "" + "';");

            }
            
            else if(strMyaction.equals("limpiar") ) {
            
                out.println("parent.mainFrame.frmdatos.txtCodigoCliente.value ='';");
                out.println("parent.mainFrame.frmdatos.txtCustomerId.value ='';");
                out.println("parent.mainFrame.frmdatos.txtNombreCliente.value ='';");
                out.println("parent.mainFrame.frmdatos.txtTipo.value ='';");
                out.println("parent.mainFrame.frmdatos.txtStatus.value ='';");
                out.println("parent.mainFrame.frmdatos.txtUsuario.value ='';");
                out.println("parent.mainFrame.frmdatos.txtNuevaOrden.value ='';");

            }

        }
        
        out.println("location.replace('"+Constante.PATH_APPORDER_LOCAL+"/Bottom.html');");
                 
        out.println("</script>");
        
        out.close();
        
        /*
        String ruta = "/htdocs/jp_order_new_test/JP_ORDER_NEW_TEST_ShowPage.jsp";
        this.getServletContext().getRequestDispatcher(ruta).forward(request, response);
        */
    }
    

    public void doPut(HttpServletRequest request, 
                      HttpServletResponse response) throws ServletException, 
                                                           IOException {
    }
}
