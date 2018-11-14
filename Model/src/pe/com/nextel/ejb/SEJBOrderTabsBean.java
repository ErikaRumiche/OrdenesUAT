package pe.com.nextel.ejb;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import pe.com.nextel.bean.CommentBean;
import pe.com.nextel.dao.GeneralDAO;
import pe.com.nextel.dao.OrderDAO;
import pe.com.nextel.util.MiUtil;


public class SEJBOrderTabsBean implements SessionBean {

	private SessionContext _context;
    OrderDAO objOrderDAO = null;
	
	public void ejbCreate() {
		objOrderDAO = new OrderDAO();
	}

	public void ejbActivate() {
	}

	public void ejbPassivate() {
	}

	public void ejbRemove() {
	}

	public void setSessionContext(SessionContext ctx) {
		_context = ctx;
	}

	/**
	 * 
	 * @throws java.lang.Exception
	 * @throws java.sql.SQLException
	 * @return 
	 * @param lOrderId
	 */
	public ArrayList getHistoryActionListByOrder(long lOrderId) throws SQLException, Exception {
		return objOrderDAO.getHistoryActionListByOrder(lOrderId);
	}

	/**
	 * 
	 * @throws java.lang.Exception
	 * @throws java.sql.SQLException
	 * @return 
	 * @param lOrderId
	 */
	public ArrayList getHistoryApproveListByOrder(long lOrderId) throws SQLException, Exception {
		return objOrderDAO.getHistoryApproveListByOrder(lOrderId);
	}
	
	/**
	 * 
	 * @throws java.lang.Exception
	 * @throws java.sql.SQLException
	 * @return 
	 * @param lOrderId
	 */
	public ArrayList getCommentByOrderList(long lOrderId) throws SQLException, Exception {
		return objOrderDAO.getCommentByOrderList(lOrderId);
	}
	
	/**
	 * 
	 * @throws java.lang.Exception
	 * @throws java.sql.SQLException
	 * @param objCommentBean
	 */
	public void addComment(CommentBean objCommentBean) throws SQLException, Exception {
		objOrderDAO.addComment(objCommentBean);
	}
	
	/**
	 * 
	 * @throws java.lang.Exception
	 * @throws java.sql.SQLException
	 * @return 
	 * @param lOrderId
	 */
	public HashMap getInventoryList(long lOrderId) throws SQLException, Exception {
		return objOrderDAO.getInventoryList(lOrderId);
	}
	
	/**
	 * 
	 * @throws java.lang.Exception
	 * @throws java.sql.SQLException
	 * @return 
	 * @param lOrderId
	 */
	public HashMap getInvoiceList(long lOrderId)throws SQLException, Exception {
		return objOrderDAO.getInvoiceList(lOrderId);
	}
   
	/**
	 * 
	 * @throws java.lang.Exception
	 * @throws java.sql.SQLException
	 * @return ArrayList
	 * @param lOrderId
	 */
	public ArrayList getInvoicetoShowList(long lOrderId)throws SQLException, Exception {
      
      GeneralDAO  objGeneralDAO =new GeneralDAO();
      //HashMap hshInvoiceToShowMap = new HashMap();
       HashMap hshInvoiceToShowMap =null;
      ArrayList arrInvoiceToShowList=new ArrayList();   
      
      HashMap hshData=null;
      String strMessage=null;
      
      HashMap hshInvoiceMap = getInvoiceList(lOrderId);
      strMessage = (String)hshInvoiceMap.get("strMessage");
      ArrayList arrInvoiceList = (ArrayList) hshInvoiceMap.get("arrInvoiceList");
      if (strMessage!=null)
         throw new Exception(strMessage);           

      //Trayendo el listado de documentos de pago
      hshData= objGeneralDAO.getTableList("PAYMENT_ORDER_DOC","1");
      strMessage=(String)hshData.get("strMessage");
      if (strMessage!=null)
         throw new Exception(strMessage);  
      
      ArrayList arrDocpagos=(ArrayList)hshData.get("arrTableList");
      
      //Trayendo el listado de estados del documento
      hshData=null;
      hshData= objGeneralDAO.getTableList("NPSALETRX_NPSTATUSDOC","1");
      strMessage=(String)hshData.get("strMessage");
      if (strMessage!=null)
         throw new Exception(strMessage);  
      
      ArrayList arrStatusDoc=(ArrayList)hshData.get("arrTableList");   
      
      if (arrInvoiceList==null) arrInvoiceList = new ArrayList();
      System.out.println("Tamaño del ArrayList: " + arrInvoiceList.size());         

      if (arrInvoiceList==null) arrInvoiceList = new ArrayList();
      System.out.println("Tamaño del ArrayList: " + arrInvoiceList.size());
      
      String strTipoDoc=null;
      String strBuildingName=null;
      String strStatusDoc=null;
      String strStatusDocDes=null;
      String strPaymentFormDes=null;
      int iBuildingId;
      int iStatusDocDesId;
      int iPaymentFormId;
    
      for (int i=0;i<arrInvoiceList.size();i++){
         hshInvoiceMap=(HashMap)arrInvoiceList.get(i);  
         hshInvoiceToShowMap = new HashMap();
         iBuildingId=MiUtil.parseInt((String)hshInvoiceMap.get("NPBUILDINGID"));
         strBuildingName=objGeneralDAO.getBuildingName(iBuildingId);   
         hshInvoiceToShowMap.put("NPSALETRXID",hshInvoiceMap.get("NPSALETRXID"));   
         hshInvoiceToShowMap.put("NPSALETRXNUMBER",hshInvoiceMap.get("NPSALETRXNUMBER"));        
         //strBuildingName=MiUtil.getString((String)hshData.get("strBuldingName"));
         hshInvoiceToShowMap.put("BUILDINGNAME",strBuildingName);        
         hshInvoiceToShowMap.put("NPCREATIONDATE",hshInvoiceMap.get("NPCREATIONDATE"));
         iStatusDocDesId = MiUtil.parseInt((String)hshInvoiceMap.get("NPPRINTSTATUS"));
         if (iStatusDocDesId == 1 ) {
            strStatusDocDes="Pendiente";
         }else{
            strStatusDocDes="Impreso";
         }        
         
         hshInvoiceToShowMap.put("NPPRINTSTATUS",strStatusDocDes);
         
         iPaymentFormId = MiUtil.parseInt((String)hshInvoiceMap.get("NPPAYMENTMETHOD"));
         if (iPaymentFormId == 1)
            strPaymentFormDes="Contado";
         else if (iPaymentFormId == 2)
            strPaymentFormDes="Crédito";
         else
            strPaymentFormDes="Cargo en el Recibo";
         
         hshInvoiceToShowMap.put("NPPAYMENTMETHOD",strPaymentFormDes);
         
         hshInvoiceToShowMap.put("NPSALETRXCODETYPE",MiUtil.getString((String)hshInvoiceMap.get("NPSALETRXTYPE")));
         
         strTipoDoc =MiUtil.getString((String)hshInvoiceMap.get("NPSALETRXTYPE"));
         strTipoDoc = MiUtil.getString(MiUtil.getDescripcion(arrDocpagos,"wv_npValue","wv_npValueDesc",strTipoDoc));
         hshInvoiceToShowMap.put("NPSALETRXTYPE",strTipoDoc);
         
         hshInvoiceToShowMap.put("NPORDERINVOICEAMOUNT",MiUtil.getString((String)hshInvoiceMap.get("NPORDERINVOICEAMOUNT")));
         hshInvoiceToShowMap.put("NPCURRENCYCODTYPE",MiUtil.getString((String)hshInvoiceMap.get("NPCURRENCYCODTYPE")));
         hshInvoiceToShowMap.put("NPCUSTOMERNAME",MiUtil.getString((String)hshInvoiceMap.get("NPCUSTOMERNAME")));
         hshInvoiceToShowMap.put("NPCUSTOMERTAXNUMBER",MiUtil.getString((String)hshInvoiceMap.get("NPCUSTOMERTAXNUMBER")));
         
         strStatusDoc = MiUtil.getDescripcion(arrStatusDoc,"wv_npValue","wv_npValueDesc",(String)hshInvoiceMap.get("NPSTATUSDOC"));      
         hshInvoiceToShowMap.put("NPSTATUSDOC",strStatusDoc);
         hshInvoiceToShowMap.put("NPFELPREFIX",hshInvoiceMap.get("NPFELPREFIX"));
         hshInvoiceToShowMap.put("NPSTATUSSUNAT", hshInvoiceMap.get("NPSTATUSSUNAT"));

         arrInvoiceToShowList.add(hshInvoiceToShowMap);
         System.out.println("-----------------INICIO / Listado a mostrar ---------------------------");
         System.out.println("Tienda --> "+(i+1)+"-->"+hshInvoiceToShowMap.get("Tienda"));
         System.out.println("Fecha de Creación --> "+(i+1)+"-->"+hshInvoiceToShowMap.get("Fecha de Creación"));
         System.out.println("Tipo Documento --> "+(i+1)+"-->"+hshInvoiceToShowMap.get("Tipo Documento"));
         System.out.println("Monto de Venta --> "+(i+1)+"-->"+hshInvoiceToShowMap.get("Monto de Venta"));
         System.out.println("Tipo Moneda --> "+(i+1)+"-->"+hshInvoiceToShowMap.get("Tipo Moneda"));
         System.out.println("Nombre del Cliente --> "+(i+1)+"-->"+hshInvoiceToShowMap.get("Nombre del Cliente"));
         System.out.println("Ruc Cliente --> "+(i+1)+"-->"+hshInvoiceToShowMap.get("Ruc Cliente"));
         System.out.println("Estado del Documento --> "+(i+1)+"-->"+hshInvoiceToShowMap.get("Estado del Documento"));
         System.out.println("-----------------FIN / Listado a mostrar ---------------------------");
      }
      return arrInvoiceToShowList;   
	}   

	/**
	 * 
	 * @throws java.lang.Exception
	 * @throws java.sql.SQLException
	 * @return HashMap
	 * @param lOrderId
	 */
	public HashMap getInventoryOrder(long lOrderId)throws SQLException, Exception {
		return objOrderDAO.getInventoryOrder(lOrderId);
	}   
   
   /**
	 * 
	 * @throws java.lang.Exception
	 * @throws java.sql.SQLException
	 * @return HashMap
	 * @param lOrderId
	 */
	public HashMap getInventoryPIOrder(long lOrderId)throws SQLException, Exception {
		return objOrderDAO.getInventoryPIOrder(lOrderId);
	}   

	/**
	 * 
	 * @throws java.lang.Exception
	 * @throws java.sql.SQLException
	 * @return 
	 * @param lOrderId
	 */
	public ArrayList getRequestOLListByOrder(long lOrderId) throws SQLException, Exception {
		return objOrderDAO.getRequestOLListByOrder(lOrderId);
	}
}