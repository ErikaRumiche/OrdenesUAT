package pe.com.nextel.section.sectionRepair;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import pe.com.nextel.bean.OrderBean;
import pe.com.nextel.bean.RepairBean;
import pe.com.nextel.dao.RepairDAO;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.GenericObject;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;


/**
 * Motivo: Clase de Secciones de Dinámicas que ejecuta eventos referidos a las Reparaciones (Save, Update, Delete).
 * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
 * <br>Fecha: 09/11/2007
 * @see SectionRepairEvents
 */
public class SectionRepairEvents extends GenericObject {
	
	/**
	 * Motivo: Evento de actualizacion de Items de la orden
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 08/11/2007
	 * @throws java.sql.SQLException
	 * @throws java.lang.Exception
	 * @return 
	 * @param request - Objeto RequestHashMap (HashMap que contiene los parámetros obtenidos por medio de request)
	 * @param conn - Objeto Connection (Conexión a la Base de Datos)
	 */
    public String updateSectionRepair(RequestHashMap request, Connection conn) throws SQLException, Exception {
		if(logger.isDebugEnabled())
			logger.debug("updateSectionRepair");
        String resultInsertItems =  "";
        System.out.println("**************************************************\n**************************************************\n");    
        System.out.println("**************************************************\n**************************************************\n");    
        System.out.println("**************************************************\n ============updateSectionRepair========================");        
        //Ini Valores agregados TMOGROVEJO             
        RepairDAO objRepairDAO = new RepairDAO();
        RepairBean objRepairBean = new RepairBean();
        OrderBean objOrderBean = new OrderBean();                      
        String strMessage = null;        
        String strLogin=request.getParameter("hdnLogin");        
        String strFecActiv = request.getParameter("txtFecActiv") ;      
        String strProveedor = request.getParameter("txtProveedor"); 
        long strProveedorId = MiUtil.parseLong(request.getParameter("hdnProveedorId"));
        String strWarrantFabrica = request.getParameter("hdnGarantiaFabrica");   
        String strWarrantExt = request.getParameter("hdnGarantiaExt");   
        String strWarrantBounce = request.getParameter("hdnGarantiaBounce");
        String strWarrantRefurbish = request.getParameter("hdnGarantiaRefurbish");           
        long  lMesesAdional = MiUtil.parseLong(request.getParameter("txtMesesAdicional"));                                                      
        String strListaFallasSeleccionadas = request.getParameter("hdnListaFallasSeleccionadas"); // Revisar
        String strListaRepuestosSeleccionados = request.getParameter("hdnListaRepuestosSeleccionados"); // Revisar
        String strListaRepuestosNuevosUsados = request.getParameter("hdnListaRepuestosNuevosUsados"); // Revisar
        String strServicio = request.getParameter("cmbServicio");           
        String strFnc = request.getParameter("chkFnc");               
        String strReparacionSinCosto = request.getParameter("chkReparacionSinCosto");    
        long lOrderId = MiUtil.parseLong(request.getParameter("hdnOrderId"));
        long lRepairId = MiUtil.parseLong(request.getParameter("hndReparacionId"));                               
        String strWarrantReparacion = request.getParameter("hdnGarantiaReparacion");
        String strWarrantTrueBounce = request.getParameter("hdnGarantiaTrueBounce"); //por el momento guardare la garantia de reparacion        
        String strDiagnostico = request.getParameter("cmbDiagnostico");        
        String strOriginal = request.getParameter("hdnOriginal");        
        String strTimerValue = request.getParameter("txtTimerValue");                        
        String strFechaVenta  = request.getParameter("txtFechaVenta");      
        String strInventoryCode = request.getParameter("txtCodEquipo"); 
        String strEstFinEquip  = request.getParameter("cmbEstFinEquip");      
        String strEquipSO = request.getParameter("cmbEquipSO");                                 
        String strListaOtrasFallasSeleccionadas = request.getParameter("hdnListaOtrasFallasSeleccionadas"); // PORTEGA
        String strDetDiagnostico = request.getParameter("txtDetDiagnostico"); // PORTEGA
        String strObsAsesor = request.getParameter("txtObsAsesor"); // PORTEGA
                                                                                                          
        objOrderBean.setNpCreatedBy(strLogin);
        objRepairBean.setNpfechaActivacion(strFecActiv);
        objRepairBean.setNpprovider(strProveedor);
        objRepairBean.setNpproviderid(strProveedorId);
        objRepairBean.setNpgarantia_fabricante(strWarrantFabrica);
        objRepairBean.setNpgarantia_extendida(strWarrantExt);
        objRepairBean.setNpgarantia_bounce(strWarrantBounce);
        objRepairBean.setNpgarantia_refurbish(strWarrantRefurbish);                 
        objRepairBean.setNpgarantia_reparacion(strWarrantReparacion); 
        objRepairBean.setNpgarantia_truebounce(strWarrantTrueBounce);        
        objRepairBean.setNpmesesAdicional(lMesesAdional);                     
        objRepairBean.setNpfallas_seleccionadas(strListaFallasSeleccionadas);         
        objRepairBean.setNprepuestos_seleccionados(strListaRepuestosSeleccionados);      
        objRepairBean.setNprepuestos_n_u(strListaRepuestosNuevosUsados);         
        objRepairBean.setNpservicio(strServicio);
        objRepairBean.setNpFnc(strFnc);       
        objRepairBean.setNprRepSinCosto(strReparacionSinCosto);
        objOrderBean.setNpOrderId(lOrderId);
        objRepairBean.setNprepairid(lRepairId);        
        objRepairBean.setNpdiagncode(strDiagnostico);                                
        objRepairBean.setNporiginal(strOriginal);                     
        objRepairBean.setNptimervalue(strTimerValue);        
        objRepairBean.setNpselldateacc(strFechaVenta);        
        objRepairBean.setNpinventorycode(strInventoryCode);               
        objRepairBean.setOrderBean(objOrderBean);                          
        objRepairBean.setNPEQUIPSO(strEquipSO);               
        objRepairBean.setNPFINALSTATE(strEstFinEquip);      
        objRepairBean.setNpotras_fallas_seleccionadas(strListaOtrasFallasSeleccionadas);
        objRepairBean.setNPDIAGNOSTICDETAIL(strDetDiagnostico);
        objRepairBean.setNPASSESSOROBSERVATION(strObsAsesor);
        strMessage = objRepairDAO.updateRepairPlus(objRepairBean,strLogin,conn); //-- Invocación a New Process   
                        

        //Fin Valores agregados TMOGROVEJO                       
        resultInsertItems = updateRepair(request, conn);                             
        return resultInsertItems;                        
	}
	
   
	public String updateRepair(RequestHashMap request, Connection conn) throws SQLException, Exception {
   
   
		RepairDAO objRepairDAO = new RepairDAO();
		RepairBean objRepairBean = new RepairBean();
		OrderBean objOrderBean = new OrderBean();      
    System.out.println("**********************Inicio updateRepair**********************");
		String strMessage = null;
	
		//Valores que se recuperan de la seccion Repair.
		String strLogin=request.getParameter("hdnLogin");      
		long lOrderId = MiUtil.parseLong(request.getParameter("hdnOrderId"));  
    String hdnReplaceType = request.getParameter("hdnReplaceType");
		//System.out.println("hdnReplaceType: "+hdnReplaceType);
    long lRepairId = MiUtil.parseLong(request.getParameter("hndReparacionId"));        
		String strPhonde = request.getParameter("txtNextel");      
		String strImei = request.getParameter("txtImei");
		String strSim = request.getParameter("txtSim");  
		String strImeisn = request.getParameter("txtSerie");      
		String strModel = request.getParameter("cmbModelo");
		String strWarrantNextel = request.getParameter("hdnGarantia");       
            
		String strEquipment = request.getParameter("txtAlquilado");
		if(strEquipment != null){
			//if(strEquipment.equals("Si")){
         if(strEquipment.equals("Alquiler")){
				strEquipment = Constante.TIPO_ALQUILER;
			}else{
				strEquipment = Constante.TIPO_CLIENTE;
			}
		}
		String strAccesoryType = request.getParameter("cmbTipoAccesorio");
		String strAccesoryModel = request.getParameter("cmbModeloAccesorio");
		String strFrequency = request.getParameter("txtSemFabricacion");
		String strReception = request.getParameter("cmbRecepcion");      
		long lCarrierId = MiUtil.parseLong(request.getParameter("cmbMotorizado")); 
		

		String strRepairType = request.getParameter("cmbProceso");
    System.out.println("cmbProceso(cmbProceso)-->"+strRepairType);
		if (strRepairType==null || strRepairType.equals("")){			
			strRepairType = request.getParameter("hdnProceso");
      
      System.out.println("cmbProceso(hdnProceso)-->-->"+strRepairType);
		}
    if (strRepairType==null || strRepairType.equals("")){			
			strRepairType = request.getParameter("hdnTipoProceso");
			System.out.println("cmbProceso(hdnTipoProceso)-->"+strRepairType);
		}
    System.out.println("cmbProceso(Final)-->"+strRepairType);
    
    long lCodigoOsiptel = MiUtil.parseLong(request.getParameter("txtCodigoOsiptel"));
		java.sql.Timestamp tsSignDate =MiUtil.toFechaHora(request.getParameter("txtFechaRecepcion"),"dd/MM/yyyy HH:mm");
		String strFirstName = request.getParameter("txtContactoNombre");
		String strLastName = request.getParameter("txtContactoApellido");
		String strCollectContact = request.getParameter("txtContactoRecojo");
		String strCollectAddress = request.getParameter("txtDireccionRecojo");      
		String strReporteCliente = request.getParameter("txtReporteCliente");      
		//String strServiceType = request.getParameter("cmbTipoServicio");    
    String strServiceType = request.getParameter("hdnServiceType");      // modificado para recibir el valor correcto, no nulo
    String strServiceCode = request.getParameter("hdnServiceCode");      // modificado para recibir el valor correcto, no nulo    
		long lTipoFalla = MiUtil.parseLong(request.getParameter("cmbTipoFalla"));
		long lResolutionId = MiUtil.parseLong(request.getParameter("cmbCodigoResolucion"));
		String strEndSituation = request.getParameter("hdnSituacion");    
		String strStatus = request.getParameter("cmbEstadoReparacion");    
		String setNpUserName1 = request.getParameter("cmbTecnicoResponsable");    
		String setDescriptionNextel = request.getParameter("txtNextelDiagnostico");    
		String strRepuestoDestino= request.getParameter("hdnRepuestoDestinoId");   
	    String strCorreoElectronico="";
	    String strTelefonoContacto="";
	    String strPrefNotif="";
	    String strTiendaRecojo="";
	    String strRepairCenter="";
        try{
		System.out.println("txtCorreoElectronico-->"+request.getParameter("txtCorreoElectronico"));
		System.out.println("txtTelefonoContacto-->"+request.getParameter("txtTelefonoContacto"));
		System.out.println("cmbPrefNotif-->"+request.getParameter("cmbPrefNotif"));
		System.out.println("cmbRepairCenter-->"+request.getParameter("cmbRepairCenter"));
	    strCorreoElectronico=request.getParameter("txtCorreoElectronico");
	    strTelefonoContacto=request.getParameter("txtTelefonoContacto");
	    strPrefNotif=request.getParameter("cmbPrefNotif");
	    strRepairCenter=request.getParameter("cmbRepairCenter");
		System.out.println("cmbTiendaRecojo-->"+request.getParameter("cmbTiendaRecojo"));
	    strTiendaRecojo=request.getParameter("cmbTiendaRecojo");
            
        }catch(Exception e){
            e.printStackTrace();
        }
            
		System.out.println("---------- Inicio SectionRepairEvents updateRepair---------------");      
		System.out.println("strRepuestoDestino-->"+strRepuestoDestino);
		System.out.println("strLogin-->"+strLogin);
		System.out.println("lOrderId-->"+lOrderId);
		System.out.println("strLogin-->"+strLogin);
		System.out.println("lRepairId-->"+lRepairId);
		System.out.println("strPhonde-->"+strPhonde);
		System.out.println("strImei-->"+strImei);
		System.out.println("strSim-->"+strSim);
		System.out.println("strImeisn-->"+strImeisn);
		System.out.println("strModel-->"+strModel);
		System.out.println("strWarrantNextel-->"+strWarrantNextel);
		System.out.println("strEquipment-->"+strEquipment);
		System.out.println("strAccesoryType-->"+strAccesoryType);
		System.out.println("strAccesoryModel-->"+strAccesoryModel);
		System.out.println("strFrequency-->"+strFrequency);
		System.out.println("strReception-->"+strReception);
		System.out.println("lCarrierId-->"+lCarrierId);
		System.out.println("strRepairType-->"+strRepairType);
		System.out.println("lCodigoOsiptel-->"+lCodigoOsiptel);
		System.out.println("tsSignDate-->"+tsSignDate);
		System.out.println("strFirstName-->"+strFirstName);
		System.out.println("strLastName-->"+strLastName);
		System.out.println("strFirstName-->"+strFirstName);
		System.out.println("strCollectContact-->"+strCollectContact);
		System.out.println("strCollectAddress-->"+strCollectAddress);
		System.out.println("strReporteCliente-->"+strReporteCliente);
		System.out.println("strServiceType-->"+strServiceType);
		System.out.println("lTipoFalla-->"+lTipoFalla);
		System.out.println("lResolutionId-->"+lResolutionId); //
		System.out.println("strEndSituation-->"+strEndSituation);
		System.out.println("strStatus-->"+strStatus);
		System.out.println("setNpUserName1-->"+setNpUserName1);
		System.out.println("setDescriptionNextel-->"+setDescriptionNextel);    
		System.out.println("strCorreoElectronico-->"+strCorreoElectronico); 
		System.out.println("strTelefonoContacto-->"+strTelefonoContacto); 
		System.out.println("strPrefNotif-->"+strPrefNotif); 
		System.out.println("strTiendaRecojo-->"+strTiendaRecojo);   
		System.out.println("strRepairCenter-->"+strRepairCenter);  
		//System.out.println("strReplaceType-->"+strReplaceType);      
		System.out.println("---------- Fin SectionRepairEvents / updateRepair---------------");
			
		objOrderBean.setNpOrderId(lOrderId);
		objOrderBean.setNpCarrierId(lCarrierId);
		objRepairBean.setNprepairid(lRepairId);
		objRepairBean.setNpphone(strPhonde);
		objRepairBean.setNpimei(strImei);
		objRepairBean.setNpsim(strSim);
		objRepairBean.setNpimeisn(strImeisn);
		objRepairBean.setNpmodel(strModel);
		objRepairBean.setNpwarrantnextel(strWarrantNextel);
      
      
		objRepairBean.setNpequipment(strEquipment);
		objRepairBean.setNpaccessorytype(strAccesoryType);
		objRepairBean.setNpaccessorymodel(strAccesoryModel);
		objRepairBean.setNpfrequency(strFrequency);
		objRepairBean.setNpreception(strReception);
		objRepairBean.setNprepairtype(strRepairType);
		objRepairBean.setNpcodeosiptel(lCodigoOsiptel);
		objOrderBean.setNpSignDate(tsSignDate);
		objRepairBean.setNpcontactfirstname(strFirstName);
		objRepairBean.setNpcontactlastname(strLastName);
		objRepairBean.setNpcollectcontact(strCollectContact);
		objRepairBean.setNpcollectaddress(strCollectAddress);
		objOrderBean.setNpDescription(strReporteCliente);
		objRepairBean.setNpservicetype(strServiceType);
		objRepairBean.setNpfailureid(lTipoFalla);
		objRepairBean.setNpresolutionid(lResolutionId);
		objRepairBean.setNpendsituation(strEndSituation);
		objRepairBean.setNpstatus(strStatus);
		objOrderBean.setNpUserName1(setNpUserName1);
		objOrderBean.setNpCreatedBy(strLogin);		
		objRepairBean.setNpdescriptionnextel(setDescriptionNextel);
                
		objRepairBean.setNpemailcontact(strCorreoElectronico);
		objRepairBean.setNpphonecontact(strTelefonoContacto);
		objRepairBean.setNpnotifpref(strPrefNotif);
                objRepairBean.setNpstorepickup(strTiendaRecojo);
                objRepairBean.setNPREPAIRCENTERID(strRepairCenter);
    objRepairBean.setNpservicio(strServiceCode);
    objRepairBean.setOrderBean(objOrderBean);      
	  
		HashMap hshRepairReplaceChangeMap = fillRepairReplaceChangeList(request,conn);
		
    System.out.println("hshRepairReplaceChangeMap"+hshRepairReplaceChangeMap);
		String[] txtImeiLista = (String[]) hshRepairReplaceChangeMap.get("txtImeiLista");
    
		if(!ArrayUtils.isEmpty(txtImeiLista)) {
			System.out.println("Se invoka a valImeiPrestamoCambio");
			strMessage=objRepairDAO.valImeiPrestamoCambio(conn, objRepairBean, hshRepairReplaceChangeMap); 
		}		   
				
		if(StringUtils.isBlank(strMessage)) {
			System.out.println("antes de la invocar objRepairDAO.updRepair");
			strMessage = objRepairDAO.updRepair(objRepairBean,strRepuestoDestino,strLogin,conn);	
         System.out.println("despues de la invocar objRepairDAO.updRepair:"+strMessage+"-"+objRepairBean.getNprepairtype());
			if(StringUtils.isBlank(strMessage)) {
			   strMessage = updateRepairReplaceList(request, conn);
			}
		}
		
		System.out.println("strMessage-->"+strMessage);
		System.out.println("---------- Fin SectionRepairEvents / updateRepair---------------");      
		return strMessage;
	}
	
	public String updateRepairReplaceList(RequestHashMap request, Connection conn) throws SQLException, Exception {
		RepairDAO objRepairDAO = new RepairDAO();
		
    String hdnOrderId = request.getParameter("hdnOrderId");
		String hdnRepairId = request.getParameter("hdnRepairId");
    String hdnReplaceType = request.getParameter("hdnReplaceType");
		System.out.println("************************Inicio updateRepairReplaceList***********************");
    System.out.println("hdnReplaceType: "+hdnReplaceType); 
    //String hdnFlagGenerate = StringUtils.defaultString(request.getParameter("hdnFlagGenerate"), "N");
		String strRepairType = request.getParameter("cmbProceso");
    System.out.println("cmbProceso(cmbProceso)-->"+strRepairType);
		if (strRepairType==null || strRepairType.equals("")){			
			strRepairType = request.getParameter("hdnProceso");
      System.out.println("cmbProceso(hdnProceso)-->-->"+strRepairType);
		}
    if (strRepairType==null || strRepairType.equals("")){			
			strRepairType = request.getParameter("hdnTipoProceso");
			System.out.println("cmbProceso(hdnTipoProceso)-->"+strRepairType);
		}
    
    System.out.println("cmbProceso(Final)-->"+strRepairType);
    String hdnFlagGenerate = "N";// cuando se graba la orden no debe generar documentos
		String hdnLogin = request.getParameter("hdnLogin");
		String[] hdnRepListId = request.getParameterValues("hdnRepListId");
		String[] chkIndiceLista = request.getParameterValues("chkIndiceLista");
		String[] txtImeiLista = request.getParameterValues("txtImeiLista");
    String[] txtSimLista = request.getParameterValues("txtSimLista");
		String[] txtSerieLista = request.getParameterValues("txtSerieLista");
		String[] cmbTipoImeiLista = request.getParameterValues("cmbTipoImeiLista");
		String[] chkCrearDocLista = request.getParameterValues("chkCrearDocLista");
        
        // MMONTOYA - Despacho en tienda
        String[] cmbModeloLista = request.getParameterValues("cmbModeloLista");
        String[] cmbTipoLista = request.getParameterValues("cmbTipoLista");
        String[] cmbFlagAccesorioLista = request.getParameterValues("cmbFlagAccesorioLista");
        String[] chkDevolverEquipoLista = request.getParameterValues("chkDevolverEquipoLista");
        
		logger.debug("Mostrando la Reparación Id: "+hdnRepairId+"\t - Usuario: "+hdnLogin+"\t - FlagGenerate: "+hdnFlagGenerate);
		int i = 0;
		logger.debug("Mira hdnRepListId: "+(++i)+").- "+ArrayUtils.toString(hdnRepListId));
		logger.debug("Mira chkIndiceLista: "+(++i)+").- "+ArrayUtils.toString(txtImeiLista));
		logger.debug("Mira txtImeiLista: "+(++i)+").- "+ArrayUtils.toString(txtSerieLista));
		logger.debug("Mira txtSimLista: "+(++i)+").- "+ArrayUtils.toString(txtSimLista));
		logger.debug("Mira txtSerieLista: "+(++i)+").- "+ArrayUtils.toString(cmbTipoImeiLista));
		logger.debug("Mira chkCrearDocLista: "+(++i)+").- "+ArrayUtils.toString(chkCrearDocLista));
		HashMap hshParametrosMap = new HashMap();
		hshParametrosMap.put("hdnOrderId", hdnOrderId);
		hshParametrosMap.put("hdnRepairId", hdnRepairId);
		//hshParametrosMap.put("hdnProceso", strRepairType);
		hshParametrosMap.put("hdnLogin", hdnLogin);
		hshParametrosMap.put("hdnFlagGenerate", hdnFlagGenerate);
		hshParametrosMap.put("hdnRepListId", hdnRepListId);
		hshParametrosMap.put("txtImeiLista", txtImeiLista);
		hshParametrosMap.put("txtSimLista", txtSimLista);
		hshParametrosMap.put("txtSerieLista", txtSerieLista);
		hshParametrosMap.put("cmbTipoImeiLista", cmbTipoImeiLista);
		hshParametrosMap.put("chkCrearDocLista", chkCrearDocLista);
    hshParametrosMap.put("hdnReplaceType", hdnReplaceType);
    
        // MMONTOYA - Despacho en tienda
        hshParametrosMap.put("cmbModeloLista", cmbModeloLista);
        hshParametrosMap.put("cmbTipoLista", cmbTipoLista);
        hshParametrosMap.put("cmbFlagAccesorioLista", cmbFlagAccesorioLista);
        hshParametrosMap.put("chkDevolverEquipoLista", chkDevolverEquipoLista);
    
		HashMap hshDataMap = objRepairDAO.generateDocument(hshParametrosMap, conn);
		String strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
		if(StringUtils.isNotBlank(strMessage)) {
			logger.error("strMessage: "+strMessage);
		}
		return strMessage;
	}

	public HashMap fillRepairReplaceChangeList(RequestHashMap request, Connection conn) throws SQLException, Exception {
				
		String hdnOrderId = request.getParameter("hdnOrderId");
		String hdnRepairId = request.getParameter("hdnRepairId");
		String hdnFlagGenerate = StringUtils.defaultString(request.getParameter("hdnFlagGenerate"), "N");
		String hdnLogin = request.getParameter("hdnLogin");
		String[] hdnRepListId = request.getParameterValues("hdnRepListId");
		String[] chkIndiceLista = request.getParameterValues("chkIndiceLista");
		String[] txtImeiLista = request.getParameterValues("txtImeiLista");
		String[] txtSimLista = request.getParameterValues("txtSimLista");
    String[] txtSerieLista = request.getParameterValues("txtSerieLista");
		String[] cmbTipoImeiLista = request.getParameterValues("cmbTipoImeiLista");
		String[] chkCrearDocLista = request.getParameterValues("chkCrearDocLista");
		
		//Registrando valores en el hashmap
		HashMap hshParametrosMap = new HashMap();
		hshParametrosMap.put("hdnOrderId", hdnOrderId);
		hshParametrosMap.put("hdnRepairId", hdnRepairId);
		hshParametrosMap.put("hdnLogin", hdnLogin);
		hshParametrosMap.put("hdnFlagGenerate", hdnFlagGenerate);
		hshParametrosMap.put("hdnRepListId", hdnRepListId);
		hshParametrosMap.put("txtImeiLista", txtImeiLista);
		hshParametrosMap.put("txtSimLista", txtSimLista);
    hshParametrosMap.put("txtSerieLista", txtSerieLista);
		hshParametrosMap.put("cmbTipoImeiLista", cmbTipoImeiLista);
		hshParametrosMap.put("chkCrearDocLista", chkCrearDocLista);
    			
		//System.out.println("[fillRepairReplaceChangeList] hshParametrosMap" + hshParametrosMap);
		return hshParametrosMap;
	}	
}
