package pe.com.nextel.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pe.com.nextel.exception.CustomException;
import pe.com.nextel.form.CommentForm;
import pe.com.nextel.util.Constante;


public class OrderTabsServlet extends GenericServlet {
	
	/**
     * Motivo:  Método que se invoca al agregar un Comentario
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 13/09/2007        
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addComment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (logger.isDebugEnabled())
            logger.debug("--Inicio--");
        PrintWriter out = response.getWriter();
        CommentForm objCommentForm = new CommentForm();
        try {

            objCommentForm = (CommentForm) objCommentForm.populateForm(request);
            objOrderTabsService.addComment(objCommentForm);
            int appId = Integer.parseInt(objCommentForm.getHdnAppId());
            int userId = Integer.parseInt(objCommentForm.getHdnUserId()); 
            int orderId = Integer.parseInt(objCommentForm.getHdnOrderId());
            String url = null;
            System.out.println("[OrderTabsServlet][addComment] : " + "("+appId+","+","+userId+","+orderId+")");
            url = objGeneralService.getURLRedirect(appId, userId, orderId, Constante.TAB_NOTAS_ORDER);
            System.out.println("[OrderTabsServlet][addComment][url]=" + url);
            out.print("<script>location.href=\"" + url + "\";</script>");   

            /*if (logger.isDebugEnabled())
                    logger.debug("Antes de hacer el sendRedirect");
            
            String strUrl="/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid="+objCommentForm.getHdnOrderId()+"&av_execframe=MAINFRAME";            
            out.print("<script>location.replace('"+strUrl+"');</script>");*/
			
        }catch (CustomException e){
            out.print("<script>alert(\"" +e.getMessage()+  "\");");
            out.print("location.replace(\"/websales/Bottom.html\");</script>");
            
        }
         catch (Exception e) {
            logger.error("[OrderTabsServlet][addComment] : " +formatException(e));
        }
    }
    public void cancel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        CommentForm objCommentForm = new CommentForm();
        try {

            objCommentForm = (CommentForm) objCommentForm.populateForm(request);
            int appId = Integer.parseInt(objCommentForm.getHdnAppId());
            int userId = Integer.parseInt(objCommentForm.getHdnUserId()); 
            int orderId = Integer.parseInt(objCommentForm.getHdnOrderId());
            
            String url = null;
            System.out.println("[OrderTabsServlet][cancel] : " + "("+appId+","+","+userId+","+orderId+")");
            url = objGeneralService.getURLRedirect(appId, userId, orderId, Constante.TAB_NOTAS_ORDER);
            System.out.println("[OrderTabsServlet][cancel][url]=" + url);
            out.print("<script>location.href=\"" + url + "\";</script>");   
            
        }catch (CustomException e){
            out.print("<script>alert(\"" +e.getMessage()+  "\");");
            out.print("location.replace(\"/websales/Bottom.html\");</script>");
        }
         catch (Exception e) {
            logger.error("[OrderTabsServlet][cancel] : "+formatException(e));
        }
    }	
	/**
     * @see GenericServlet#executeDefault(HttpServletRequest, HttpServletResponse)
     */
    protected void executeDefault(HttpServletRequest request, HttpServletResponse response) {
        try {
			PrintWriter out = response.getWriter();
            logger.debug("Antes de hacer el sendRedirect");
			String strUrl="/portal/page/portal/orders/ORDER_ADD_COMMENT";
            out.print("<script>parent.mainFrame.location.replace('"+strUrl+"');</script>");
            //response.sendRedirect("https://172.20.1.114/portal/page/portal/orders/ORDER_ADD_COMMENT");
        } catch (Exception e) {
            logger.error(formatException(e));
        }
    }
	
}