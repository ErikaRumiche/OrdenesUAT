package pe.com.nextel.servlet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;
import pe.com.nextel.bean.PopulateCenterBean;
import pe.com.nextel.bean.UbigeoBean;
import pe.com.nextel.service.GeneralService;
import pe.com.nextel.service.PopulateCenterService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by JCASTILLO on 03/04/2017.
 * [PRY-0787]
 */
public class PopulateCenterServlet extends HttpServlet{
    private static final long serialVersionUID = 1L;
    private static final Logger logger= Logger.getLogger(PopulateCenterServlet.class);
    private static final Gson gson=new Gson();
    private static final String CONTENT_TYPE_TEXT = "text/html; charset=UTF-8";
    private static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
    private PopulateCenterService populateCenterService=new PopulateCenterService();
    GeneralService generalService= new GeneralService();
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE_TEXT);
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE_JSON);
        PrintWriter out = response.getWriter();
        logger.info("[PopulateCenterServlet][doGet], INICIO");
        try{
            String myaction=request.getParameter("myaction");
            logger.info("[PopulateCenterServlet][doGet], action: " + myaction);
            if ("getUbigeoList".equals(myaction)) {
                getUbigeoList(request,response);
            }else if ("savePopulateCenter".equals(myaction)) {
                savePopulateCenter(request, response);
            }else if ("getPopulateCenterDetail".equals(myaction)){
                getPopulateCenterDetail(request,response);
            }else if("getPopulateCenter".equals(myaction))
                getPopulateCenter(request,response);

        }catch(Exception e){
            response.sendError(response.SC_INTERNAL_SERVER_ERROR,e.getMessage());
        }
        finally {
            out.close();
        }
    }


    //JCALDERON PRY 0787
    public void savePopulateCenter(HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        logger.info("[PopulateCenterServlet][savePopulateCenterList], INICIO");
        PrintWriter out = response.getWriter();
        String populateCenterJSON=request.getParameter("data");
        String userLogin=request.getParameter("userLogin");
        logger.info("[PopulateCenterServlet][savePopulateCenterList], json: " + populateCenterJSON);
        Type typePopulateCenterBean = new TypeToken<PopulateCenterBean>(){}.getType();
        PopulateCenterBean populateCenterBean = gson.fromJson(populateCenterJSON, typePopulateCenterBean);
        logger.info(populateCenterBean);
        HashMap hashMap = populateCenterService.savePopulateCenter(populateCenterBean,userLogin);
        out.write(gson.toJson(hashMap));
    }

    public void getUbigeoList(HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
        logger.info("[PopulateCenterServlet][getUbigeoList], INICIO");
        PrintWriter out = response.getWriter();
        String dep=request.getParameter("codDep");
        String prov=request.getParameter("codProv");
        String dist=request.getParameter("codDist");
        UbigeoBean ubigeoBean=new UbigeoBean();
        ubigeoBean.setDepartamento(dep);
        ubigeoBean.setProvincia(prov);
        ubigeoBean.setDistrito(dist);
        HashMap hashMap=generalService.getUbigeoList(ubigeoBean);
        out.write(gson.toJson(hashMap));
        logger.info("[PopulateCenterServlet][getUbigeoList], FIN");
    }
    public void getPopulateCenterDetail(HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
        logger.info("[PopulateCenterServlet][getPopulateCenterDetail], INICIO");
        PrintWriter out = response.getWriter();
        String orderId=request.getParameter("orderId");
        HashMap hashMap=populateCenterService.getPopulateCenterDetail(orderId);
        logger.info(hashMap);
        out.write(gson.toJson(hashMap));
        logger.info("[PopulateCenterServlet][getPopulateCenterDetail], FIN");
    }
    public void getPopulateCenter(HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {
        logger.info("[PopulateCenterServlet][getPopulateCenter], INICIO");
        PrintWriter out = response.getWriter();
        String orderId=request.getParameter("orderId");
        HashMap hashMap=populateCenterService.getPopulateCenter(orderId);
        logger.info(hashMap);
        out.write(gson.toJson(hashMap));
        logger.info("[PopulateCenterServlet][getPopulateCenter], FIN");
    }


}
