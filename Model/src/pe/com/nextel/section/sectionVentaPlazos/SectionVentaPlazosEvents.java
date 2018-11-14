package pe.com.nextel.section.sectionVentaPlazos;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import pe.com.nextel.dao.InstallmentSalesDAO;
import pe.com.nextel.dao.OrderDAO;
import pe.com.nextel.bean.OrderBean;
import pe.com.nextel.util.GenericObject;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;

public class SectionVentaPlazosEvents extends GenericObject{


    /**
    * Motivo: Evento de actualización de Items VEP de la orden en tablas del esquema INVOICE
    * <br>Realizado por: <a href="mailto:rensso.martinez@hp.com">Rensso Martinez</a>
    * <br>Fecha: 29/11/2012
    * @param     RequestHashMap request  
    * @param     Connection conn       
    * @return    String resultInsertItems 
    */ 
    public String updateVentaPlazos(RequestHashMap request,Connection conn) throws Exception, SQLException{
        logger.info("************************ INICIO SectionVentaPlazosEvents > updateVentaPlazos(RequestHashMap request,Connection conn) ************************");
	
        String resultInsertItems = null;
        String hdnNumeroOrder=null;
        try{ 
            OrderDAO objOrderDAO = new OrderDAO();
            hdnNumeroOrder=request.getParameter("hdnNumeroOrder");
        
            logger.info("hdnNumeroOrder: "+hdnNumeroOrder);
          
            HashMap objHashOrder = objOrderDAO.getOrder(MiUtil.parseLong(hdnNumeroOrder));
            OrderBean objOrderBean = (OrderBean)objHashOrder.get("objResume");
            logger.info("objOrderBean.getNpInvoicesGenerated(): " + objOrderBean.getNpInvoicesGenerated());
          
          if ( (objOrderBean.getNpInvoicesGenerated().equals("null") || objOrderBean.getNpInvoicesGenerated()==null) && !objOrderBean.getNpInvoicesGenerated().equals("S")  ){
             resultInsertItems = actualizarVep(request,conn);
          }

        }catch(Exception ex){
            logger.error("ERROR updateVentaPlazos",ex);
            resultInsertItems= MiUtil.getMessageClean("[Exception][updateVentaPlazos]["+ex.getClass() + " " + ex.getMessage()+ " - Caused by " + ex.getCause() + "]");
        }
        logger.info("************************ FIN SectionVentaPlazosEvents > updateVentaPlazos(RequestHashMap request,Connection conn) ************************");
        return resultInsertItems;
    }  
    
    
    /**
    * Motivo: Evento de actualización de Items VEP de la orden en tablas del esquema INVOICE
    * <br>Realizado por: <a href="mailto:rensso.martinez@hp.com">Rensso Martinez</a>
    * <br>Fecha: 29/11/2012
    * @param     RequestHashMap request  
    * @param     Connection conn       
    * @return    String resultInsertItems 
    */ 
    public String actualizarVep(RequestHashMap request,Connection conn)  throws Exception, SQLException{
        logger.info("************************ INICIO actualizarVep(RequestHashMap request,Connection conn) ************************");
        InstallmentSalesDAO objInstallmentSalesDAO = new InstallmentSalesDAO();

        String strError="";
        try{
        
        long lOrderId         = MiUtil.parseLong(request.getParameter("hdnNumeroOrder"));
        long lCustomerId      = (request.getParameter("txtCompanyId")==null?0:MiUtil.parseLong(request.getParameter("txtCompanyId")));
        long lSiteId          = (request.getParameter("strSiteId")==null?0:MiUtil.parseLong(request.getParameter("strSiteId")));
        long lCustomerbscsId  = 0L;
        String strCodBSCS     = (String)request.getParameter("hdnCodBscsDetail");
        long lquotaNumber     = (request.getParameter("cmbVepNumCuotas")==null?0:MiUtil.parseInt(request.getParameter("cmbVepNumCuotas")));
        double dAmountTotal   = (request.getParameter("hdnTotalSalesPriceVEP")==null?0:MiUtil.parseDouble(request.getParameter("hdnTotalSalesPriceVEP"))); //0
        String  strOrigenStatus   = "ORDER_NEW";
        String strLogin       = request.getParameter("hdnSessionLogin");
        double    dInitialQuota    = MiUtil.parseDouble(request.getParameter("txtCuotaInicial"));
        
        //INICIO: PRY-0980 | AMENDEZ
        String strNpPaymentTermsIQ       = request.getParameter("txtPaymentTermsIQ");
        int intNpPaymentTermsIQ    =MiUtil.parseInt(strNpPaymentTermsIQ);
        //FIN: PRY-0980 | AMENDEZ
        
        String strInvoicesGenerated = request.getParameter("hdnSessionLogin");
        
        logger.info("lOrderId         : "+lOrderId);
        logger.info("lCustomerId      : "+lCustomerId);
        logger.info("lSiteId          : "+lOrderId);
        logger.info("strCodBSCS       : "+lOrderId);
        logger.info("lquotaNumber     : "+lquotaNumber);
        logger.info("dAmountTotal     : "+dAmountTotal);
        logger.info("strOrigenStatus  : "+strOrigenStatus);
        logger.info("dInitialQuota    : "+dInitialQuota);
        logger.info("strNpPaymentTermsIQ    : "+strNpPaymentTermsIQ);
        //HashMap objHashOrder = objOrderDAO.getOrder(lOrderId);
        //OrderBean objOrderBean = new OrderBean();
        //objOrderBean = (OrderBean)objHashOrder.get("objResume");
        
        //if (!objOrderBean.getNpInvoicesGenerated().equals("S")){
          HashMap objReturnDelete = objInstallmentSalesDAO.doDeleteInstallmentSales(lOrderId);
          strError = (String)objReturnDelete.get("strMessage");
          if (strError!=null)
          {
            throw new Exception(strError);
          }
          
          HashMap objReturnSave = objInstallmentSalesDAO.doCreateInstallmentSales(lOrderId,
                                                                                  lCustomerId,
                                                                                  lSiteId,
                                                                                  lCustomerbscsId,
                                                                                  strCodBSCS,
                                                                                  lquotaNumber,
                                                                                  dAmountTotal,
                                                                                  strOrigenStatus,
                                                                                  strLogin,
                                                                                  dInitialQuota,
                                                                                  //INICIO: PRY-0980 | AMENDEZ
                                                                                  intNpPaymentTermsIQ
                                                                                  //FIN: PRY-0980 | AMENDEZ
                                                                                    );
          strError = (String)objReturnDelete.get("strMessage");
          if (strError!=null){
            throw new Exception(strError);
          }
          
        //}   
          
        }catch(Exception e) {
            logger.error("ERROR actualizarVep",e);
          throw e;
        }
        logger.info("************************ FIN actualizarVep(RequestHashMap request,Connection conn) ************************");
        return strError;
    }


  
}