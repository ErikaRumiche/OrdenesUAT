package pe.com.nextel.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pe.com.nextel.bean.DominioBean;
import pe.com.nextel.bean.VerificationBean;
import pe.com.nextel.service.BiometricaService;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Servlet para las paginas de consulta y detalle de Verificacion de Identidad Aislada 
 */
public class BiometricaDetailServlet extends GenericServlet {

    private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    BiometricaService biometricaService= new BiometricaService();
    Constante constante= new Constante();

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        System.out.println("BiometricaDetailServletPost");
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("BiometricaDetailServletGet");
        response.setContentType(CONTENT_TYPE);
        String myaction=request.getParameter("myaction");
        System.out.println("myaction:" + myaction);
        if(myaction!=null) {
            if (myaction.equals("listarVerificaciones")) {
                listarVerificaciones(request, response);
            }else if (myaction.equals("construirComboTipoDoc")){
                construirComboTipoDoc(request, response);
            }else if (myaction.equals("obtenerDetalleVia")) {
                obtenerDetalleVia(request, response);
            }
        }
    }

    /**
     * Realiza la consulta de Verificaciones de Identidad Aisladas
     * <br>Autor: RO
     * <br>Fecha: 20/09/2016
     * */
    private void listarVerificaciones(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("listarVerificaciones");
        String tipoDocumento= request.getParameter("tipoDoc");
        System.out.println("tipoDoc " + tipoDocumento);
        String numeroDocumento=request.getParameter("numDoc");
        System.out.println("numDoc " + numeroDocumento);
        String fechaInicio=request.getParameter("fecIni");
        System.out.println("fecIni " + fechaInicio);
        String fechaFin=request.getParameter("fecFin");
        System.out.println("fecFin " + fechaFin);
        SimpleDateFormat dt = new SimpleDateFormat(DATE_FORMAT);
        PrintWriter out;
        HashMap respuesta;
        try{
            out = response.getWriter();
            respuesta = biometricaService.listarVerificaciones(tipoDocumento, numeroDocumento, fechaInicio, fechaFin);
            List<VerificationBean> listaRespuesta = (ArrayList<VerificationBean>) respuesta.get("objArrayList");
            StringBuilder outMessage = new StringBuilder("");
            outMessage.append("<tr class=\"ListRow0\"><td>Nro.</td><td>Tipo de verificacion</td><td>Tipo Doc.</td><td>Nro. Doc</td><td>Origen</td><td>Nro. Transacción</td><td>Fecha Inicio de Vigencia</td><td>Fecha Fin de Vigencia</td><td>Fecha de Utilizacion</td><td style=\"witdth:5%;\"></td></tr>");
            int index = 1;
            if (listaRespuesta == null || listaRespuesta.isEmpty()){
                outMessage.append("<tr class=\"ListRow1\"><td>No se encontraron registros</td></tr>");
            }else{
                for (VerificationBean verification : listaRespuesta) {
                    outMessage.append("<tr class=\"ListRow1\">");
                    outMessage.append("<td>").append(index++).append("</td>");
                    outMessage.append("<td>").append(MiUtil.valText(verification.getTipoVerificacion())).append("</td>");
                    outMessage.append("<td>").append(MiUtil.valText(verification.getTipoDocumento())).append("</td>");
                    outMessage.append("<td>").append(MiUtil.valText(verification.getNumeroDocumento())).append("</td>");
                    outMessage.append("<td>").append(MiUtil.valText(verification.getOrigen())).append("</td>");
                    outMessage.append("<td>").append(MiUtil.valText(verification.getNumero())).append("</td>");
                    outMessage.append("<td>").append(verification.getFechaInicioVigencia()==null?"":dt.format(verification.getFechaInicioVigencia())).append("</td>");
                    outMessage.append("<td>").append(verification.getFechaFinVIgencia()==null?"":dt.format(verification.getFechaFinVIgencia())).append("</td>");
                    outMessage.append("<td>").append(verification.getFechaUtilizacion()==null?"":dt.format(verification.getFechaUtilizacion())).append("</td>");
                    outMessage.append("<td><a class=\"detalleLink\" href=\"/portal/page/portal/orders/IDENT_VERIF_CONS_DETAIL?identificador="+verification.getVerificationId()+"\">Detalle</a></td>");
                    outMessage.append("</tr>");
                }
            }
            out.print(outMessage);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("[listarVerificaciones:]Message=" + e.getMessage());
        }

        System.out.println("[listarVerificaciones:]Fin");
    }

    /**
     * Obtiene los tipos de documento con sus limites de caracteres configurados
     * <br>Autor: RO
     * <br>Fecha: 20/09/2016
     */
    public void construirComboTipoDoc(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("construirComboTipoDoc");
        HashMap respuesta;
        StringBuilder outMessage = new StringBuilder("<option value=\"\"/>");
        PrintWriter out = null;
        try {
            respuesta = biometricaService.listarTiposDocumentoVerificaciones();
            List<DominioBean> listaDominio = (ArrayList<DominioBean>) respuesta.get("objArrayList");
            for (DominioBean dominio : listaDominio){
                outMessage.append("<option value=\"").append(MiUtil.valText(dominio.getValor())).append("\"");
                outMessage.append(" length=\"").append(MiUtil.valText(dominio.getParam1())).append("\" >");
                outMessage.append(MiUtil.valText(dominio.getDescripcion()));
                outMessage.append("</option>");
            }
            out = response.getWriter();
            out.print(outMessage);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("[listarVerificaciones:]Message=" + e.getMessage());
        }
    }

    /**
     * Obtiene los datos a mostrarse en el detalle de una Verificacion de Identidad Aislada
     */
    public void obtenerDetalleVia(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("obtenerDetalleVia");
        Long identificador= Long.valueOf(request.getParameter("identificador"));
        System.out.println("identificador " + identificador);
        PrintWriter out;
        try {
            HashMap respuesta = biometricaService.obtenerDetalleVia(identificador);
            VerificationBean verificationBean = (VerificationBean) respuesta.get("objBean");
            out = response.getWriter();
            Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();
            String verificationBeanString = gson.toJson(verificationBean);
            System.out.println("{\"verificationDetailBean\":"+verificationBeanString +"}");
            out.write("{\"verificationDetailBean\":" + verificationBeanString + "}");
            out.close();
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("[obtenerDetalleVia:]Message=" + e.getMessage());
        }

    }

    @Override
    protected void executeDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            logger.debug("Antes de hacer el sendRedirect");
        } catch (Exception e) {
            logger.error(formatException(e));
        }
    }
}
