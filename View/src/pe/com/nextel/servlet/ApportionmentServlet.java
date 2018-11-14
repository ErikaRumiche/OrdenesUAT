package pe.com.nextel.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import pe.com.nextel.bean.ApportionmentBean;
import pe.com.nextel.bean.RequestApportionmentBean;
import pe.com.nextel.bean.ResponseBean;
import pe.com.nextel.service.ApportionmentService;
import pe.com.nextel.util.MiUtil;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
/**
 * Servlet para calcular prorrateo
 */
public class ApportionmentServlet extends GenericServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ApportionmentService apportionmentService = new ApportionmentService();
	 
	@Override
	protected void executeDefault(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("************** INICIO ApportionmentServlet->executeDefault **************");
	}
	
	public void calculatePayment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("[ApportionmentServlet][calculatePayment], inicio");
    	response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");       
        
        ResponseBean responseBean = new ResponseBean();
        HashMap responseMap = new HashMap();
        RequestApportionmentBean requestBean = new RequestApportionmentBean();
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		JsonElement element = null;
		
		try {
			String json = StringUtils.defaultString(request.getParameter("myDataP"));
			System.out.println("[ApportionmentServlet][calculatePayment], myDataP: "+json);
				
			requestBean = gson.fromJson(json, RequestApportionmentBean.class);
			requestBean.setTrxId(MiUtil.generateTrackingID());
			System.out.println("requestBean " + requestBean.toString());
			
			responseMap = apportionmentService.getCalculatePayment(requestBean);
			long status = (Long) responseMap.get("status");
			String message = (String) responseMap.get("message");
			System.out.println("ApportionmentServlet/calculatePayment [status] " + status + " [message] " + message);
			if(status==0L) {
				System.out.println("[status==0L]");
				int mountTotal = (Integer) responseMap.get("mountTotal");
				List<ApportionmentBean> items = (List<ApportionmentBean>) responseMap.get("items");
				element = new JsonObject();
			    element.getAsJsonObject().addProperty("mount", mountTotal);
			    element.getAsJsonObject().add("items", gson.toJsonTree(items));
			    
			    if(responseMap.get("postPaid") != null)
			    	element.getAsJsonObject().addProperty("postPaid",(Boolean) responseMap.get("postPaid"));
			    
			    responseBean.setData(element);
			    System.out.println("[FIN SET ELEMENT]");
			}
			responseBean.setStatus(status);
			responseBean.setMessage(message);			
		} catch (Exception e) {
			String msg = "No se completó la acción : "	+ e.getMessage();
			System.out.println("[ERROR] " + e.getMessage());
			responseBean.setStatus(new Long(-1));
			responseBean.setMessage(msg);
		} finally {
			String value = gson.toJson(responseBean);
			System.out.println("[ApportionmentServlet][calculatePayment], fin --> " + value);
			out.write(value);
			out.close();
		}
	}	
}