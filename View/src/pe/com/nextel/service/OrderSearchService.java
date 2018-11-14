package pe.com.nextel.service;

import java.util.HashMap;

import javax.naming.Context;

import javax.rmi.PortableRemoteObject;

import org.apache.commons.lang.StringUtils;

import pe.com.nextel.ejb.SEJBOrderSearchRemote;
import pe.com.nextel.ejb.SEJBOrderSearchRemoteHome;
import pe.com.nextel.form.OrderSearchForm;
import pe.com.nextel.form.PCMForm;
import pe.com.nextel.form.RetentionForm;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;


public class OrderSearchService extends GenericService {

	private static String newLine = "<br>";
	private static String tabSpace = "&nbsp;&nbsp;&nbsp;&nbsp;";

    public OrderSearchService() {
    }
    
	public static SEJBOrderSearchRemote getSEJBOrderSearchRemote() {
		logger.debug("Entrando al getSEJBOrderSearchRemote()");
		try {
			final Context context = MiUtil.getInitialContext();
      final SEJBOrderSearchRemoteHome sEJBOrderSearchRemoteHome = (SEJBOrderSearchRemoteHome) PortableRemoteObject.narrow( context.lookup( "SEJBOrderSearch" ), SEJBOrderSearchRemoteHome.class );
      SEJBOrderSearchRemote sEJBOrderSearchRemote = sEJBOrderSearchRemoteHome.create();
      return sEJBOrderSearchRemote;
		} catch(Exception e) {
			logger.error(formatException(e));
			return null;
        }
	}
	
	public HashMap getOrdersList(OrderSearchForm objOrderSearchForm) {
		System.out.println("[INICIO OrderSearchService.java getOrdersList]");
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");         
		HashMap hshDataMap = new HashMap();
        try {
        	chooseOrdersReturnList(objOrderSearchForm);
         System.out.println("[OrderSearchService.java][antes de invocar]");
			hshDataMap = getSEJBOrderSearchRemote().getOrderList(objOrderSearchForm);
         System.out.println("[OrderSearchService.java][despues de invocar]");
        } catch (Throwable t) {
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
    }

	//LSILVA : HPPTT - Internal Order List - Obtiene la lista de Órdenes internas asociadas a una Orden principal
	public HashMap getInternalOrderList(long plParentOrderId, long plNumRegistros, long plNumPagina) {
		System.out.println("[INICIO OrderSearchService.java getInternalOrderList]");
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");         
		HashMap hshDataMap = new HashMap();
        try {
        	System.out.println("[OrderSearchService.java][antes de invocar]");
			hshDataMap = getSEJBOrderSearchRemote().getInternalOrderList(plParentOrderId, plNumRegistros, plNumPagina);
			System.out.println("[OrderSearchService.java][despues de invocar]");
        } catch (Throwable t) {
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
    }
	
	//LSILVA : HPPTT - Parent Order List - Obtiene la lista de Órdenes padres de una Orden dada. La primera Orden
    //de la lista es la más ancestral.
	public HashMap getParentOrderList(long plOrderId) {
		System.out.println("[INICIO OrderSearchService.java getParentOrderList]");
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");         
		HashMap hshDataMap = new HashMap();
        try {
        	System.out.println("[OrderSearchService.java][antes de invocar]");
			hshDataMap = getSEJBOrderSearchRemote().getParentOrderList(plOrderId);
			System.out.println("[OrderSearchService.java][despues de invocar]");
        } catch (Throwable t) {
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
    }	
	
	private void chooseOrdersReturnList(OrderSearchForm objOrderSearchForm) {
  
    System.out.println("getTxtNroOrden : "+objOrderSearchForm.getTxtNroOrden());
    System.out.println("getTxtNextel : "+objOrderSearchForm.getTxtNextel());
    System.out.println("getCmbTipoServicio : "+objOrderSearchForm.getCmbTipoServicio());
    System.out.println("getCmbModelo : "+objOrderSearchForm.getCmbModelo());
    System.out.println("getCmbTipoFalla : "+objOrderSearchForm.getCmbTipoFalla());
    System.out.println("getTxtImei : "+objOrderSearchForm.getTxtImei());
    System.out.println("getCmbSituacion : "+objOrderSearchForm.getCmbSituacion());
    System.out.println("getCmbTecnicoResponsable : "+objOrderSearchForm.getCmbTecnicoResponsable());
    System.out.println("getCmbEstadoReparacion : "+objOrderSearchForm.getCmbEstadoReparacion());
    System.out.println("getCmbCategoria : "+objOrderSearchForm.getCmbCategoria());
    System.out.println("getCmbSituacion : "+objOrderSearchForm.getCmbSituacion());
    System.out.println("getCmbTipoProceso : "+objOrderSearchForm.getCmbTipoProceso());
    System.out.println("getTxtImeiCambio : "+objOrderSearchForm.getTxtImeiCambio());
    System.out.println("getTxtImeiPrestamo : "+objOrderSearchForm.getTxtImeiPrestamo());
    System.out.println("getTxtNroGuia : "+objOrderSearchForm.getTxtNroGuia());
    
    System.out.println("esBusquedaReparacion : "+objOrderSearchForm.esBusquedaReparacion());
        
		if(objOrderSearchForm.esBusquedaReparacion()) {
			objOrderSearchForm.setIFlag(Constante.FLAG_BUSQUEDA_REPARACION);
		}
		else {
			objOrderSearchForm.setIFlag(Constante.FLAG_BUSQUEDA_ORDEN);
		}
    
    System.out.println("getIFlag : "+objOrderSearchForm.getIFlag());
	}
	
	public String buildOptionsSelected(OrderSearchForm objOrderSearchForm) {
		StringBuffer sbfCadena = new StringBuffer();
		StringBuffer sbfTemporal;
    
    sbfTemporal = new StringBuffer();
    boolean priority=false;
    System.out.println("getTxtNroOrden : "+objOrderSearchForm.getTxtNroOrden());
    System.out.println("getTxtNroReparacion : "+objOrderSearchForm.getTxtNroReparacion());
    
    if(StringUtils.isNotEmpty(objOrderSearchForm.getTxtNroOrden())) {
      sbfTemporal.append("N° Orden: ").append(objOrderSearchForm.getTxtNroOrden()).append(tabSpace);
      priority=true;
      if(StringUtils.isNotEmpty(sbfTemporal.toString())) {
        sbfCadena.append(sbfTemporal).append(newLine);
        sbfTemporal = new StringBuffer();
      }
		}
    if((StringUtils.isNotEmpty(objOrderSearchForm.getTxtProposedId()))&&(!priority))
    {
      sbfTemporal.append("N° Propuesta: ").append(objOrderSearchForm.getTxtProposedId()).append(tabSpace);
      priority=true;
      if(StringUtils.isNotEmpty(sbfTemporal.toString())) {
        sbfCadena.append(sbfTemporal).append(newLine);
        sbfTemporal = new StringBuffer();
      }
    }
    if(!priority){
      if(StringUtils.isNotEmpty(objOrderSearchForm.getHdnCustomerId())) {
        sbfCadena.append("Id del Customer: ").append(objOrderSearchForm.getHdnCustomerId()).append(tabSpace).append(newLine);
      }
      if(StringUtils.isNotEmpty(objOrderSearchForm.getHdnRuc())) {
        sbfTemporal.append("RUC/DNI/Otro: ").append(objOrderSearchForm.getHdnRuc()).append(tabSpace);
      }
      if(StringUtils.isNotEmpty(objOrderSearchForm.getHdnCustomerName())) {
        sbfTemporal.append("Raz&oacute;n Social: ").append(objOrderSearchForm.getHdnCustomerName()).append(tabSpace);
      }
      if(StringUtils.isNotEmpty(sbfTemporal.toString())) {
        sbfCadena.append(sbfTemporal).append(newLine);
        sbfTemporal = new StringBuffer();
      }
      if(StringUtils.isNotEmpty(objOrderSearchForm.getTxtNroSolicitud())) {
        sbfTemporal.append("N° Solicitud: ").append(objOrderSearchForm.getTxtNroSolicitud()).append(tabSpace);
      }
      if(StringUtils.isNotEmpty(sbfTemporal.toString())) {
        sbfCadena.append(sbfTemporal).append(newLine);
        sbfTemporal = new StringBuffer();
      }
      if(StringUtils.isNotEmpty(objOrderSearchForm.getCmbDivisionNegocio())) {
        sbfTemporal.append("Divisi&oacute;n de Negocio: ").append(objOrderSearchForm.getHdnDivisionNegocio()).append(tabSpace);
      }
      if(StringUtils.isNotEmpty(objOrderSearchForm.getCmbSolucionNegocio())) {
        sbfTemporal.append("Soluci&oacute;n de Negocio: ").append(objOrderSearchForm.getHdnSolucionNegocio()).append(tabSpace);
      }
      if(StringUtils.isNotEmpty(sbfTemporal.toString())) {
        sbfCadena.append(sbfTemporal).append(newLine);
        sbfTemporal = new StringBuffer();
      }
      if(StringUtils.isNotEmpty(objOrderSearchForm.getCmbCategoria())) {
        sbfTemporal.append("Categor&iacute;a: ").append(objOrderSearchForm.getCmbCategoria()).append(tabSpace);
      }
      if(StringUtils.isNotEmpty(objOrderSearchForm.getHdnSubCategoria())) {
        sbfTemporal.append("Sub Categor&iacute;a: ").append(objOrderSearchForm.getHdnSubCategoria()).append(tabSpace);
      }
      if(StringUtils.isNotEmpty(sbfTemporal.toString())) {
        sbfCadena.append(sbfTemporal).append(newLine);
        sbfTemporal = new StringBuffer();
      }
      /*if(StringUtils.isNotEmpty(objOrderSearchForm.getCmbZona())) {
        sbfTemporal.append("Zona: ").append(objOrderSearchForm.getHdnZona()).append(tabSpace);
      }
      if(StringUtils.isNotEmpty(objOrderSearchForm.getCmbCoordinador())) {
        sbfTemporal.append("Coordinador: ").append(objOrderSearchForm.getHdnCoordinador()).append(tabSpace);
      }*/
      if(StringUtils.isNotEmpty(sbfTemporal.toString())) {
        sbfCadena.append(sbfTemporal).append(newLine);
        sbfTemporal = new StringBuffer();
      }
      /*if(StringUtils.isNotEmpty(objOrderSearchForm.getCmbSupervisor())) {
        sbfTemporal.append("Supervisor: ").append(objOrderSearchForm.getHdnSupervisor()).append(tabSpace);
      }
      if(StringUtils.isNotEmpty(objOrderSearchForm.getCmbConsultorEjecutivo())) {
        sbfTemporal.append("Consultor/Ejecutivo: ").append(objOrderSearchForm.getHdnConsultorEjecutivo()).append(tabSpace);
      }*/
      if(StringUtils.isNotEmpty(sbfTemporal.toString())) {
        sbfCadena.append(sbfTemporal).append(newLine);
        sbfTemporal = new StringBuffer();
      }
      if(StringUtils.isNotEmpty(objOrderSearchForm.getHdnRegion())) {
        sbfTemporal.append("Regi&oacute;n: ").append(objOrderSearchForm.getHdnRegion()).append(tabSpace);
      }
      if(StringUtils.isNotEmpty(objOrderSearchForm.getHdnTienda())) {
        sbfTemporal.append("Tienda: ").append(objOrderSearchForm.getHdnTienda()).append(tabSpace);
      }
      if(StringUtils.isNotEmpty(sbfTemporal.toString())) {
        sbfCadena.append(sbfTemporal).append(newLine);
        sbfTemporal = new StringBuffer();
      }
      if(StringUtils.isNotEmpty(objOrderSearchForm.getCmbEstadoOrden())) {
        sbfTemporal.append("Estado de la Orden: ").append(objOrderSearchForm.getCmbEstadoOrden()).append(tabSpace);
      }
      if(StringUtils.isNotEmpty(objOrderSearchForm.getTxtCreadoPor())) {
        sbfTemporal.append("Creado por: ").append(objOrderSearchForm.getTxtCreadoPor()).append(tabSpace);
      }
      if(StringUtils.isNotEmpty(objOrderSearchForm.getTxtNroGuia())) {
        sbfTemporal.append("Nro. Guia: ").append(objOrderSearchForm.getTxtNroGuia()).append(tabSpace);
      }
      if(StringUtils.isNotEmpty(sbfTemporal.toString())) {
        sbfCadena.append(sbfTemporal).append(newLine);
        sbfTemporal = new StringBuffer();
      }
      if(StringUtils.isNotEmpty(objOrderSearchForm.getTxtCreateDateFrom())) {
        sbfTemporal.append("Fecha de Creaci&oacute;n - Desde: ").append(objOrderSearchForm.getTxtCreateDateFrom()).append(tabSpace);
      }
      if(StringUtils.isNotEmpty(objOrderSearchForm.getTxtCreateDateTill())) {
        sbfTemporal.append("Fecha de Creaci&oacute;n - Hasta: ").append(objOrderSearchForm.getTxtCreateDateTill()).append(tabSpace);
      }
      if(StringUtils.isNotEmpty(sbfTemporal.toString())) {
        sbfCadena.append(sbfTemporal).append(newLine);
        sbfTemporal = new StringBuffer();
      }
      if(StringUtils.isNotEmpty(objOrderSearchForm.getTxtNroReparacion())) {
        sbfTemporal.append("N° Reparaci&oacute;n: ").append(objOrderSearchForm.getTxtNroReparacion()).append(tabSpace);
      }
      if(StringUtils.isNotEmpty(objOrderSearchForm.getTxtNextel())) {
        sbfTemporal.append("Nextel: ").append(objOrderSearchForm.getTxtNextel()).append(tabSpace);
      }
      if(StringUtils.isNotEmpty(sbfTemporal.toString())) {
        sbfCadena.append(sbfTemporal).append(newLine);
        sbfTemporal = new StringBuffer();
      }
      if(StringUtils.isNotEmpty(objOrderSearchForm.getCmbTipoServicio())) {
        sbfTemporal.append("Tipo Servicio: ").append(objOrderSearchForm.getCmbTipoServicio()).append(tabSpace);
      }
    
      // INICIO - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 30/09/2010
      if(StringUtils.isNotEmpty(objOrderSearchForm.getCmbTipoEquipo ())) {
        sbfTemporal.append("Tipo de Equipo: ").append(objOrderSearchForm. getCmbTipoEquipo ()).append(tabSpace);
      }
     // FIN - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 30/09/2010
      
      if(StringUtils.isNotEmpty(objOrderSearchForm.getCmbModelo())) {
        sbfTemporal.append("Modelo: ").append(objOrderSearchForm.getCmbModelo()).append(tabSpace);
      }
      if(StringUtils.isNotEmpty(sbfTemporal.toString())) {
        sbfCadena.append(sbfTemporal).append(newLine);
        sbfTemporal = new StringBuffer();
      }
      if(StringUtils.isNotEmpty(objOrderSearchForm.getHdnTipoFalla())) {
        sbfTemporal.append("Tipo Falla: ").append(objOrderSearchForm.getHdnTipoFalla()).append(tabSpace);
      }
      if(StringUtils.isNotEmpty(objOrderSearchForm.getTxtImei())) {
        sbfTemporal.append("Imei: ").append(objOrderSearchForm.getTxtImei()).append(tabSpace);
      }
      if(StringUtils.isNotEmpty(sbfTemporal.toString())) {
        sbfCadena.append(sbfTemporal).append(newLine);
        sbfTemporal = new StringBuffer();
      }
      if(StringUtils.isNotEmpty(objOrderSearchForm.getTxtImeiCambio())) {
        sbfTemporal.append("Imei Cambio: ").append(objOrderSearchForm.getTxtImeiCambio()).append(tabSpace);
      }
      if(StringUtils.isNotEmpty(objOrderSearchForm.getTxtImeiPrestamo())) {
        sbfTemporal.append("Imei Prestamo: ").append(objOrderSearchForm.getTxtImeiPrestamo()).append(tabSpace);
      }
      if(StringUtils.isNotEmpty(sbfTemporal.toString())) {
        sbfCadena.append(sbfTemporal).append(newLine);
        sbfTemporal = new StringBuffer();
      }
      if(StringUtils.isNotEmpty(objOrderSearchForm.getCmbSituacion())) {
        sbfTemporal.append("Situaci&oacute;n: ").append(objOrderSearchForm.getCmbSituacion()).append(tabSpace);
      }
      if(StringUtils.isNotEmpty(objOrderSearchForm.getCmbTecnicoResponsable())) {
        sbfTemporal.append("Técnico Responsable: ").append(objOrderSearchForm.getCmbTecnicoResponsable()).append(tabSpace);
      }
      if(StringUtils.isNotEmpty(sbfTemporal.toString())) {
        sbfCadena.append(sbfTemporal).append(newLine);
        sbfTemporal = new StringBuffer();
      }
      if(StringUtils.isNotEmpty(objOrderSearchForm.getCmbEstadoReparacion())) {
        sbfCadena.append("Estado de la Reparaci&oacute;n: ").append(objOrderSearchForm.getCmbEstadoReparacion()).append(tabSpace);
      }
		
      //AGAMARRA----------------------------
      if(StringUtils.isNotEmpty(sbfTemporal.toString())) {
        //En una nueva línea
        sbfCadena.append(sbfTemporal).append(newLine);
        sbfTemporal = new StringBuffer();
      }
	  
	  System.out.println("Pintando filtro SalesStruct");
      if(objOrderSearchForm.getHdnSalesstructid()!=null &&
        StringUtils.isNotEmpty(objOrderSearchForm.getHdnSalesstructid()) && 

        Integer.parseInt(objOrderSearchForm.getHdnSalesstructid())!=0 ) {
        //sbfTemporal.append("SalesStructId: ").append(objOrderSearchForm.getHdnSalesstructid()).append(tabSpace);
        sbfTemporal.append("Unidad Jer&aacute;rquica: ").append(objOrderSearchForm.getStrUnidadJerarquica()).append(tabSpace);
      }
	  
      System.out.println("objOrderSearchForm.getHdnProviderid()="+objOrderSearchForm.getHdnProviderid());
      if(objOrderSearchForm.getHdnProviderid()!=null){
        if(StringUtils.isNotEmpty(objOrderSearchForm.getHdnProviderid()) &&
          Integer.parseInt(objOrderSearchForm.getHdnProviderid())!=0) {
          System.out.println("Entró - Jerarquía");
          //sbfTemporal.append("ProviderId: ").append(objOrderSearchForm.getHdnProviderid()).append(tabSpace);
          sbfTemporal.append("Representante: ").append(objOrderSearchForm.getStrRepresentante()).append(tabSpace);
        }
      }

        //pzacarias
        if(StringUtils.isNotEmpty(objOrderSearchForm.getHdnSegmentoCompania())) {
            sbfTemporal.append("segmento: ").append(objOrderSearchForm.getHdnSegmentoCompania()).append(tabSpace);
        }

      sbfCadena.append(sbfTemporal).append(newLine);
      //-------------------------------------
    }
		return sbfCadena.toString();
	}
  
  public HashMap existOrder(long lOrderId) {
    if(logger.isDebugEnabled()) {
      logger.debug("--Inicio--");
    }
    HashMap hshDataMap = new HashMap();
    try {
      System.out.println("[OrderSearchService.java][antes de invocar]");
      hshDataMap = getSEJBOrderSearchRemote().existOrder(lOrderId);
      System.out.println("[OrderSearchService.java][despues de invocar]");
    } catch (Throwable t) {
      manageCatch(hshDataMap, t);
		}
		return hshDataMap;
  }
    
  //AGAMARRA
  public HashMap getDataField(int an_ruleid, String av_retriverepresentative, int an_salesstructid, int an_providergrpid){
    try {
      return(getSEJBOrderSearchRemote().getDataField(an_ruleid, av_retriverepresentative, an_salesstructid, an_providergrpid));
    } catch (Throwable t) {
      return(null);
		}
  }
  
  //AGAMARRA
  public String checkStructRule(int ruleid, String strNpsalesstructid){
    try {
      return(getSEJBOrderSearchRemote().checkStructRule(ruleid, strNpsalesstructid));
    } catch (Throwable t) {
      return("N");
		}
  }
  
  
  public HashMap getfinalSuspensionList(HashMap hshParameters){
      HashMap hshDataMap = new HashMap();
      try{
         hshDataMap = getSEJBOrderSearchRemote().getFinalSuspensionList(hshParameters);
      }catch (Throwable t){
         manageCatch(hshDataMap, t);
      }
      return hshDataMap;
   }
   
  public HashMap getfinalSuspDetailList(HashMap hshParameters){
      HashMap hshDataMap = new HashMap();
      try{
         hshDataMap = getSEJBOrderSearchRemote().getFinalSuspDetailList(hshParameters);
      }catch (Throwable t){
         manageCatch(hshDataMap, t);
      }
      return hshDataMap;
   }
   
  /**
   * @see pe.com.nextel.dao.OrderDAO#getCalArea()
   */
	public HashMap getCalArea() {
		HashMap hshDataMap = new HashMap();
		try {			
         hshDataMap = getSEJBOrderSearchRemote().getCalArea();
        } catch(Throwable t){
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
	}
  
   /**
   * @see pe.com.nextel.dao.OrderDAO#getSuspensionReason()
   */
	public HashMap getSuspensionReason() {
		HashMap hshDataMap = new HashMap();
		try {			
         hshDataMap = getSEJBOrderSearchRemote().getSuspensionReason();
        } catch(Throwable t){
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
	}
  
   /**
   * @see pe.com.nextel.dao.OrderDAO#getRetentionTool()
   */
	public HashMap getRetentionTool() {
		HashMap hshDataMap = new HashMap();
		try {			
         hshDataMap = getSEJBOrderSearchRemote().getRetentionTool();
        } catch(Throwable t){
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
	}
  
   /**
   * @see pe.com.nextel.dao.OrderDAO#getClientType()
   */
	public HashMap getClientType() {
		HashMap hshDataMap = new HashMap();
		try {			
         hshDataMap = getSEJBOrderSearchRemote().getClientType();
        } catch(Throwable t){
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
	}
  
  /**
   * @see pe.com.nextel.dao.OrderDAO#getGeneralSuspensionList()
   */
	//public HashMap getGeneralSuspensionList(HashMap hshParameters) {
	//	HashMap hshDataMap = new HashMap();
	//	try {			
  //       hshDataMap = getSEJBOrderSearchRemote().getGeneralSuspensionList(hshParameters);
  //      } catch(Throwable t){
	//		manageCatch(hshDataMap, t);
	//	}
	//	return hshDataMap;
	//}
  
  /**
   * @see pe.com.nextel.dao.OrderDAO#getDetailedSuspensionList()
   */
	public HashMap getDetailedSuspensionList(HashMap hshParameters) {
		HashMap hshDataMap = new HashMap();
		try {			
         hshDataMap = getSEJBOrderSearchRemote().getDetailedSuspensionList(hshParameters);
        } catch(Throwable t){
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
	}
  
 /**
   * @see pe.com.nextel.dao.OrderDAO#getEstadoPMC()
   */
	public HashMap getEstadoPMC(String dominioTabla) {
		HashMap hshDataMap = new HashMap();
		try {			
         hshDataMap = getSEJBOrderSearchRemote().getEstadoPMC(dominioTabla);
        } catch(Throwable t){
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
	}
  
  /**
   * @see pe.com.nextel.dao.OrderDAO#getActionID()
   */
	public HashMap getActionID(String dominioTabla) {
		HashMap hshDataMap = new HashMap();
		try {			
         hshDataMap = getSEJBOrderSearchRemote().getActionID(dominioTabla);
        } catch(Throwable t){
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
	}
  
   
  public HashMap getRetentionList(RetentionForm retentionForm) {
    HashMap hshDataMap = new HashMap();
    try {
      hshDataMap = getSEJBOrderSearchRemote().getRetentionList(retentionForm);
    } catch (Throwable t) {
      manageCatch(hshDataMap, t);
    }
    return hshDataMap;
  }
    
  public HashMap getPCMList(PCMForm pcmForm) {
    HashMap hshDataMap = new HashMap();
    try {
      hshDataMap = getSEJBOrderSearchRemote().getPCMList(pcmForm);
    } catch (Throwable t) {
      manageCatch(hshDataMap, t);
    }
    return hshDataMap;
  }
  
  //AGAMARRA
  public int getParentForAssist(int an_salesstructdefaultid){
    try {
      return(getSEJBOrderSearchRemote().getParentForAssist(an_salesstructdefaultid));
    } catch (Throwable t) {
      t.printStackTrace();
      return(0);
		}
  }
  
  //AGAMARRA
  public int getPrvdStructAssist(int an_salesstructdefaultid){
    try {
      return(getSEJBOrderSearchRemote().getPrvdStructAssist(an_salesstructdefaultid));
    } catch (Throwable t) {
      return(0);
		}
  }
  
  //AGAMARRA
  public String getLastPosition(int an_salesstructdefaultid){
    try {
      return(getSEJBOrderSearchRemote().getLastPosition(an_salesstructdefaultid));
    } catch (Throwable t) {
      return(null);
		}
  }
  
  /**
   * @return
   * @throws Exception
   */
  public HashMap getSuspensionType() throws Exception {
      HashMap hshDataMap = null;
      try {            
          hshDataMap = getSEJBOrderSearchRemote().getSuspensionType();
      } catch (Throwable t) {
          manageCatch(hshDataMap, t);
      }
      return hshDataMap;
  }
  
}
