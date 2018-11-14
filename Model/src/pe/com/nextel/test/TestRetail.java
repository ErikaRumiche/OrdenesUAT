package pe.com.nextel.test;

import java.sql.*;

import java.util.*;

import org.apache.commons.lang.*;

import pe.com.nextel.bean.*;
import pe.com.nextel.dao.*;
import pe.com.nextel.form.*;
import pe.com.nextel.util.*;


public class TestRetail extends GenericObject {

	ProductDAO productDAO = new ProductDAO();
	RepairDAO repairDAO = new RepairDAO();
	GeneralDAO generalDAO = new GeneralDAO();
	AccountDAO accountDAO = new AccountDAO();
	RetailDAO retailDAO = new RetailDAO();
	CustomerDAO customerDAO = new CustomerDAO();
	OrderDAO orderDAO = new OrderDAO();
	TestDAO testDAO = new TestDAO();

    public static void main(String[] args) {
		Calendar c1 = Calendar.getInstance();
		c1.add(Calendar.MONTH, -3);
		logger.debug(MiUtil.getDate(c1.getTime(), "dd/MM/yyyy hh:mm:ss a"));
		new TestRetail().testUnitario();
    }
	
	public void testUnitario() {	
		try {
			//logger.debug(repairDAO.getFlagGenerateDoc(2003741));
			/////////////////////////////////////////////////////
			/*
			RepairBean repairBean = new RepairBean();
			repairBean.setNpimei("000510059935710");
			logger.debug(repairDAO.getImeiDetail(repairBean));
			*/
			/////////////////////////////////////////////////////
			//logger.debug(accountDAO.getCustCodeByCsId("230729"));
			/////////////////////////////////////////////////////
			//logger.debug(repairDAO.getRepairReplaceList("1"));
			/////////////////////////////////////////////////////
			/*
			HashMap hshParametrosMap = new HashMap();
			hshParametrosMap.put("hdnOrderId", "2003740");
			hshParametrosMap.put("hdnRepairId", "73487");
			hshParametrosMap.put("hdnLogin", "LDIAZGONZALES");
			hshParametrosMap.put("hdnRepListId", new String[] {"3", "4"});
			hshParametrosMap.put("txtImeiLista", new String[] {"000510059935710", "000510059928710"});
			hshParametrosMap.put("txtSerieLista", new String[] {"364THGB9B5", "364THJ223Z"});
			hshParametrosMap.put("cmbTipoImeiLista", new String[] {"Cambio", "Cliente"});
			hshParametrosMap.put("chkCrearDocLista", new String[] {"0", "1"});
			repairDAO.generateDocument(hshParametrosMap);
			*/
			//////////////////////////////////////////////////////
			/*
			RetailForm retailForm = new RetailForm();
			retailForm.setHdnLogin("RDELOSREYES");
			retailForm.setCmbTipoDocumento("RUC");
			retailForm.setCmbTienda("-1115");
			retailForm.setTxtNroDocumento("20421814041");
			retailForm.setTxtDireccion(new String[2]);
			retailForm.setTxtIMEI(new String[]{"111111111111", null});
			retailForm.setTxtEquipo(new String[]{null, null});
			retailForm.setTxtServicio(new String[]{null, null});
			retailForm.setTxtPlanTarifario(new String[]{null, null});
			retailForm.setHdnPlanTarifario(new String[]{null, null});
			retailForm.setCmbKit(new String[] {"5861"});
			retailForm.setHdnKit(new String[]{null, null});
			retailForm.setTxtContrato(new String[]{null, null});
			retailForm.setTxtNextel(new String[]{null, null});
			retailForm.setTxtModelo(new String[]{null, null});
			retailForm.setTxtSIM(new String[]{null, null});
			retailForm.setHdnItem(new String[] {"0", null});
			
			retailDAO.newOrderRetail(retailForm);
			*/
			//////////////////////////////////////////////////////
			//logger.debug(repairDAO.reportRepair("FORMATO_REPARACION_CLIENTE", 73487));
			//logger.debug(repairDAO.getLastImeiRepair(73487));
			//////////////////////////////////////////////////////
			//logger.debug(generalDAO.getDetalleReposicionByTelefono("98100193"));
			//////////////////////////////////////////////////////
			//PortalSessionBean psb = new PortalSessionBean();
			//PortalSessionDAO.ubicar("98102435", 12, psb);
			//logger.debug(psb);
			//////////////////////////////////////////////////////
			//logger.debug(generalDAO.getProcessList());
			//////////////////////////////////////////////////////
			//logger.debug(generalDAO.getInfoImei("000510061612710"));
			//////////////////////////////////////////////////////
			//logger.debug(customerDAO.getCustomerJava(27, null, 435, null));
			//////////////////////////////////////////////////////
			/*PortalSessionBean psb = new PortalSessionBean();
			PortalSessionDAO.ubicar("98102435", 12, psb);
			logger.debug(psb);
			*/
			/*
			OrderSearchForm objOrderSearchForm = new OrderSearchForm();
			objOrderSearchForm.setIFlag(3);
			//objOrderSearchForm.setTxtCreateDateFrom("26/09/2007");
			//objOrderSearchForm.setTxtCreateDateTill("26/12/2007");
			//objOrderSearchForm.setCmbEstadoOrden("(EN PROCESO)");
			//objOrderSearchForm.setCmbTienda("21");
			logger.debug("Antes de calcular");
			HashMap hshDataMap = testDAO.getOrdersList(objOrderSearchForm);
			String strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
			if(StringUtils.isNotBlank(strMessage)) {
				logger.error(strMessage);
			} else {
				ArrayList arrOrdersList = new ArrayList();
				arrOrdersList = (ArrayList) hshDataMap.get("arrOrdersList");
				logger.debug("Tamaño del arrOrdersList: "+arrOrdersList.size());
				Iterator iterOrders = arrOrdersList.iterator();
				while(iterOrders.hasNext()) {
					int i = 1;
					
				}
				
			}
			*/
			/*
			logger.debug("Antes de calcular");
			ArrayList arrOrdersList = orderDAO.getOrdersList(objOrderSearchForm);
			logger.debug("Tamaño del arrOrdersList: "+arrOrdersList.size());
			*/
			/*
			ItemDAO itemDAO = new ItemDAO();
			HashMap hshItemDeviceMap = new HashMap();
			hshItemDeviceMap.put("imei", "000100651427090");
			hshItemDeviceMap.put("lugarDespacho", "21");
			hshItemDeviceMap.put("producto", "73");
			hshItemDeviceMap.put("modalidad", "Venta");
			
			logger.debug(itemDAO.getInboxGenerateGuide(conn));
			*/
			logger.debug("aca toy1");
			Connection conn = getConnection();
			
			//logger.debug(generalDAO.getKitDetail(4050, "20", conn));
			ProductBean objProductBean = new ProductBean();
			objProductBean.setNpproductid_new(MiUtil.parseLong("354"));
			objProductBean.setNpmodality_new("Venta");
			objProductBean.setNpcategoryid(MiUtil.parseLong("2001"));
			objProductBean.setNpquantity("1");
			//logger.debug(productDAO.getProductPriceList(objProductBean));
			//productDAO.getProductList(objProductBean);
			logger.debug(orderDAO.getResponsibleAreaList(conn));
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	public static Connection getConnection(){
		Connection conn = null;
        try {
			ResourceBundle rb = ResourceBundle.getBundle("pe.com.nextel.resource.conexion");
            Class.forName(rb.getString("driver") );
            String url      = rb.getString("url");
            String usuario  = rb.getString("usuarios");
            String password = rb.getString("clave");
            conn = DriverManager.getConnection(url,usuario,password);
        }
        catch (Exception e) {
			logger.error(e);
        }
        return conn;            
    }
}
