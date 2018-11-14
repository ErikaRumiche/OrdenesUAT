package pe.com.nextel.service;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.Context;

import javax.rmi.PortableRemoteObject;

import pe.com.nextel.bean.CommentBean;
import pe.com.nextel.ejb.SEJBOrderTabsRemote;
import pe.com.nextel.ejb.SEJBOrderTabsRemoteHome;
import pe.com.nextel.form.CommentForm;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;

public class OrderTabsService extends GenericService {
	
	/**
	 * 
	 * @return 
	 */
	public static SEJBOrderTabsRemote getSEJBOrderTabsRemote() {
		logger.debug("Entrando al getSEJBOrderSearchRemote()");
		try {
			final Context context = MiUtil.getInitialContext();
            final SEJBOrderTabsRemoteHome sEJBOrderTabsRemoteHome = (SEJBOrderTabsRemoteHome) PortableRemoteObject.narrow( context.lookup( "SEJBOrderTabs" ), SEJBOrderTabsRemoteHome.class );
            SEJBOrderTabsRemote sEJBOrderTabsRemote = sEJBOrderTabsRemoteHome.create();
            return sEJBOrderTabsRemote;
		} catch(Exception e) {
			logger.error(formatException(e));
			return null;
        }
	}
	
	/**
	 * 
	 * @return 
	 * @param lOrderId
	 */
	public ArrayList getHistoryActionListByOrder(long lOrderId) {
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		ArrayList arrHistoryActionList = new ArrayList();
        try {
			logger.debug("Mostrando::: "+lOrderId);
			arrHistoryActionList = getSEJBOrderTabsRemote().getHistoryActionListByOrder(lOrderId);
			logger.debug("Mostrando numero de Historias de Accion::: "+arrHistoryActionList.size());
			logger.debug("--Fin--");
			return arrHistoryActionList;
        }catch (SQLException sqle) {
			logger.error(formatException(sqle));
        } catch (Exception e) {
			logger.error(formatException(e));
        }
		return arrHistoryActionList;
    }
	
	/**
	 * 
	 * @return 
	 * @param lOrderId
	 */
	public ArrayList getHistoryApproveListByOrder(long lOrderId) {
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		ArrayList arrHistoryApproveList = new ArrayList();
        try {
			logger.debug("Mostrando::: "+lOrderId);
			arrHistoryApproveList = getSEJBOrderTabsRemote().getHistoryApproveListByOrder(lOrderId);
			logger.debug("Mostrando numero de Historias de Aprobacion::: "+arrHistoryApproveList.size());
			logger.debug("--Fin--");
			return arrHistoryApproveList;
        } catch (SQLException sqle) {
			logger.error(formatException(sqle));
        } catch (Exception e) {
			logger.error(formatException(e));
        }
		return arrHistoryApproveList;
    }
	
	/**
	 * 
	 * @return 
	 * @param lOrderId
	 */
	public ArrayList getCommentByOrderList(long lOrderId) {
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		ArrayList arrCommentList = new ArrayList();
        try {
			arrCommentList = getSEJBOrderTabsRemote().getCommentByOrderList(lOrderId);
			logger.debug("Mostrando numero de Comentarios::: "+arrCommentList.size());
			logger.debug("--Fin--");
			return arrCommentList;
        } catch (SQLException sqle) {
			logger.error(formatException(sqle));
        } catch (Exception e) {
			logger.error(formatException(e));
        }
		return arrCommentList;
    }
	
	/**
	 * 
	 * @param objCommentForm
	 */
	public void addComment(CommentForm objCommentForm) {
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
        try {
			CommentBean objCommentBean = new CommentBean();
			objCommentBean.setNpOrderId(Long.parseLong(objCommentForm.getHdnOrderId()));
			objCommentBean.setNpAction(Constante.COMMENT_ACTION);
			objCommentBean.setNpSubject(objCommentForm.getTxtSubject());
			objCommentBean.setNpComment(objCommentForm.getTxtDescription());
			objCommentBean.setNpCreatedBy(objCommentForm.getHdnLogin());
			getSEJBOrderTabsRemote().addComment(objCommentBean);
        } catch (SQLException sqle) {
			logger.error(formatException(sqle));
        } catch (Exception e) {
			logger.error(formatException(e));
        }
		logger.debug("--Fin--");
    }
	
	/**
	 * 
	 * @return 
	 * @param lOrderId
	 */
	public HashMap getInvoiceList(long lOrderId) {
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshInvoiceMap = new HashMap();
        try {
			hshInvoiceMap = getSEJBOrderTabsRemote().getInvoiceList(lOrderId);
			logger.debug("--Fin--");
			return hshInvoiceMap;
        } catch (SQLException sqle) {
			logger.error(formatException(sqle));
        } catch (Exception e) {
			logger.error(formatException(e));
        }
		return hshInvoiceMap;
    } 

	/**
	 * 
	 * @return 
	 * @param lOrderId
	 */
	public HashMap getInvoicetoShowList(long lOrderId) {
      HashMap hshInvoiceMap = new HashMap();
      ArrayList arrLista =new ArrayList();
      try {  
         arrLista = getSEJBOrderTabsRemote().getInvoicetoShowList(lOrderId);
         hshInvoiceMap.put("arrInvoiceToShowList",arrLista);     
      } catch (SQLException sqle) {
         hshInvoiceMap.put("arrInvoiceToShowList",arrLista);     
         hshInvoiceMap.put("strMessage",sqle.getMessage());
      } catch (Exception e) {
         hshInvoiceMap.put("arrInvoiceToShowList",arrLista);
         hshInvoiceMap.put("strMessage",e.getMessage());
      }      
      return hshInvoiceMap;
    } 
    
	/**
	 * 
	 * @return 
	 * @param lOrderId
	 */
	public HashMap getInventoryOrder(long lOrderId) {      
      
      HashMap hshInventory = new HashMap();
      try {
         hshInventory = getSEJBOrderTabsRemote().getInventoryOrder(lOrderId);
      return hshInventory;
      } catch (SQLException sqle) {
         logger.error(formatException(sqle));
      } catch (Exception e) {
         logger.error(formatException(e));
      }
      return hshInventory;
    }     

   /**
	 * 
	 * @return 
	 * @param lOrderId
	 */
	public HashMap getInventoryPIOrder(long lOrderId) {      
      
      HashMap hshInventory = new HashMap();
      try {
         hshInventory = getSEJBOrderTabsRemote().getInventoryPIOrder(lOrderId);
      return hshInventory;
      } catch (SQLException sqle) {
         logger.error(formatException(sqle));
      } catch (Exception e) {
         logger.error(formatException(e));
      }
      return hshInventory;
    }     
	
	/**
	 * 
	 * @return 
	 * @param lOrderId
	 */
	public ArrayList getRequestOLListByOrder(long lOrderId) {
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		ArrayList arrRequestOLList = new ArrayList();
        try {
			logger.debug("Mostrando::: "+lOrderId);
			arrRequestOLList = getSEJBOrderTabsRemote().getRequestOLListByOrder(lOrderId);
			logger.debug("Mostrando numero de Solicitudes a Almacen::: "+arrRequestOLList.size());
			logger.debug("--Fin--");
			return arrRequestOLList;
        }catch (SQLException sqle) {
			logger.error(formatException(sqle));
        } catch (Exception e) {
			logger.error(formatException(e));
        }
		return arrRequestOLList;
    }
	
}