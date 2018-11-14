package pe.com.nextel.form;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;


public class OrderSearchForm extends GenericForm implements Serializable {
  private static final long serialVersionUID = 1L;
    private String hdnCustomerId;
    private String hdnCustomerName;
    private String hdnRuc;
    private String hdnCustomerIdbscs;
    private String hdnStatusCollection;
    private String hdnTypecia;
    private String hdnNumber;
    private String hdnCodbscs;
    private String cmbCriterion;
    private String txtNumber;
    private String txtCodBscs;
    private String txtRuc;
    private String txtCustomerName;
    private String txtNroOrden;
    private String txtNroSolicitud;
    private String cmbEstadoOrden;
    private String hdnEstadoOrden;
    private String cmbDivisionNegocio;
    private String hdnDivisionNegocio;
    private String cmbSolucionNegocio;
    private String hdnSolucionNegocio;
    private String cmbCategoria;
    private String hdnCategoria;
    private String cmbSubCategoria;
    private String hdnSubCategoria;
    //private String cmbZona;
    //private String hdnZona;
    //private String cmbCoordinador;
    //private String hdnCoordinador;
    //private String cmbSupervisor;
    //private String hdnSupervisor;
    //private String cmbConsultorEjecutivo;
    //private String hdnConsultorEjecutivo;
    private String cmbRegion;
    private String hdnRegion;
    private String cmbTienda;
    private String hdnTienda;
    private String txtCreateDateFrom;
    private String txtCreateDateTill;
    private String txtCreadoPor;
    private String txtNroReparacion;
    private String txtNextel;
    private String cmbTipoServicio;
    private String hdnTipoServicio;
    private String cmbModelo;
    private String hdnModelo;
    private String cmbTipoFalla;
    private String hdnTipoFalla;
    private String txtImei;
    private String cmbSituacion;
    private String hdnSituacion;
    private String cmbTecnicoResponsable;
    private String hdnTecnicoResponsable;
    private String cmbEstadoReparacion;
    private String hdnEstadoReparacion;
    private String cmbTipoProceso;
    private String hdnTipoProceso;
	 
    
    private String txtImeiCambio;
    private String txtImeiPrestamo;
    private String chkReparacion;
    
    private String hdnLogin;
    private String hdnUserId;
    private String hdnNumRegistros;
    private String hdnNumPagina;
    
    private int iFlag;
    
    private String txtNroGuia;
    private String chkFastSearch;

    //Campos para Jerarquía
    private String hdnSalesstructid;
    private String  hdnProviderid;
    
    private String strUnidadJerarquica;
    private String strRepresentante;
    private String txtProposedId;//CBARZOLA 02/08/2009

    // INICIO - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 30/09/2010
    private String  cmbTipoEquipo;    
    // FIN - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 30/09/2010
    
    private String chkNumberSearch; // MFAUDA 15/02/2012

    private String cmbRangoCuenta; // OCRUCES 29/10/2012
    
    private String cmbMarcaDap; //POrtega
    private String cmbEstadoOrdenExterna; //POrtega
    private String cmbRespuestaCotizacion; //POrtega
    private String cmbSituacionEquipo; //POrtega
    private String cmbTiendaRecojo; //POrtega

    private String cmbSegmentoCompania; // PZACARIAS 21/01/2017
    private String hdnSegmentoCompania;

    private String cmbWebPayment; // PRY-1239 18/09/2018 PCACERES

    /**
   * Motivo:  Inserta el detalle de la orden generada desde campaña
   * <br>Realizado por: <a href="mailto:percy.hidalgo@nextel.com.pe">Percy Hidalgo</a>
   * <br>Fecha: 30/09/2010 
   * */

    public OrderSearchForm() {
    }

      public void setTxtNroGuia(String txtNroGuia) {
        this.txtNroGuia = txtNroGuia;
    }

    public String getTxtNroGuia() {
        return txtNroGuia;
    }

    public void setHdnCustomerId(String hdnCustomerId) {
        this.hdnCustomerId = hdnCustomerId;
    }

    public String getHdnCustomerId() {
        return hdnCustomerId;
    }

    public void setHdnCustomerName(String hdnCustomerName) {
        this.hdnCustomerName = hdnCustomerName;
    }

    public String getHdnCustomerName() {
        return hdnCustomerName;
    }

    public void setHdnRuc(String hdnRuc) {
        this.hdnRuc = hdnRuc;
    }

    public String getHdnRuc() {
        return hdnRuc;
    }

    public void setHdnCustomerIdbscs(String hdnCustomerIdbscs) {
        this.hdnCustomerIdbscs = hdnCustomerIdbscs;
    }

    public String getHdnCustomerIdbscs() {
        return hdnCustomerIdbscs;
    }

    public void setHdnStatusCollection(String hdnStatusCollection) {
        this.hdnStatusCollection = hdnStatusCollection;
    }

    public String getHdnStatusCollection() {
        return hdnStatusCollection;
    }

    public void setHdnTypecia(String hdnTypecia) {
        this.hdnTypecia = hdnTypecia;
    }

    public String getHdnTypecia() {
        return hdnTypecia;
    }

    public void setHdnNumber(String hdnNumber) {
        this.hdnNumber = hdnNumber;
    }

    public String getHdnNumber() {
        return hdnNumber;
    }

    public void setHdnCodbscs(String hdnCodbscs) {
        this.hdnCodbscs = hdnCodbscs;
    }

    public String getHdnCodbscs() {
        return hdnCodbscs;
    }

    public void setCmbCriterion(String cmbCriterion) {
        this.cmbCriterion = cmbCriterion;
    }

    public String getCmbCriterion() {
        return cmbCriterion;
    }

    public void setTxtNumber(String txtNumber) {
        this.txtNumber = txtNumber;
    }

    public String getTxtNumber() {
        return txtNumber;
    }

    public void setTxtCodBscs(String txtCodBscs) {
        this.txtCodBscs = txtCodBscs;
    }

    public String getTxtCodBscs() {
        return txtCodBscs;
    }

    public void setTxtRuc(String txtRuc) {
        this.txtRuc = txtRuc;
    }

    public String getTxtRuc() {
        return txtRuc;
    }

    public void setTxtCustomerName(String txtCustomerName) {
        this.txtCustomerName = txtCustomerName;
    }

    public String getTxtCustomerName() {
        return txtCustomerName;
    }

    public void setTxtNroOrden(String txtNroOrden) {
        this.txtNroOrden = txtNroOrden;
    }

    public String getTxtNroOrden() {
        return txtNroOrden;
    }

    public void setTxtNroSolicitud(String txtNroSolicitud) {
        this.txtNroSolicitud = txtNroSolicitud;
    }

    public String getTxtNroSolicitud() {
        return txtNroSolicitud;
    }

    public void setCmbEstadoOrden(String cmbEstadoOrden) {
        this.cmbEstadoOrden = cmbEstadoOrden;
    }

    public String getCmbEstadoOrden() {
        return cmbEstadoOrden;
    }

    public void setHdnEstadoOrden(String hdnEstadoOrden) {
        this.hdnEstadoOrden = hdnEstadoOrden;
    }

    public String getHdnEstadoOrden() {
        return hdnEstadoOrden;
    }

    public void setHdnDivisionNegocio(String hdnDivisionNegocio) {
        this.hdnDivisionNegocio = hdnDivisionNegocio;
    }

    public String getHdnDivisionNegocio() {
        return hdnDivisionNegocio;
    }

    public void setCmbCategoria(String cmbCategoria) {
        this.cmbCategoria = cmbCategoria;
    }

    public String getCmbCategoria() {
        return cmbCategoria;
    }

    public void setHdnCategoria(String hdnCategoria) {
        this.hdnCategoria = hdnCategoria;
    }

    public String getHdnCategoria() {
        return hdnCategoria;
    }

    public void setCmbSubCategoria(String cmbSubCategoria) {
        this.cmbSubCategoria = cmbSubCategoria;
    }

    public String getCmbSubCategoria() {
        return cmbSubCategoria;
    }

    public void setHdnSubCategoria(String hdnSubCategoria) {
        this.hdnSubCategoria = hdnSubCategoria;
    }

    public String getHdnSubCategoria() {
        return hdnSubCategoria;
    }

    public void setCmbRegion(String cmbRegion) {
        this.cmbRegion = cmbRegion;
    }

    public String getCmbRegion() {
        return cmbRegion;
    }

    public void setHdnRegion(String hdnRegion) {
        this.hdnRegion = hdnRegion;
    }

    public String getHdnRegion() {
        return hdnRegion;
    }

    public void setCmbTienda(String cmbTienda) {
        this.cmbTienda = cmbTienda;
    }

    public String getCmbTienda() {
        return cmbTienda;
    }

    public void setHdnTienda(String hdnTienda) {
        this.hdnTienda = hdnTienda;
    }

    public String getHdnTienda() {
        return hdnTienda;
    }

    public void setTxtCreateDateFrom(String txtCreateDateFrom) {
        this.txtCreateDateFrom = txtCreateDateFrom;
    }

    public String getTxtCreateDateFrom() {
        return txtCreateDateFrom;
    }

    public void setTxtCreateDateTill(String txtCreateDateTill) {
        this.txtCreateDateTill = txtCreateDateTill;
    }

    public String getTxtCreateDateTill() {
        return txtCreateDateTill;
    }

    public void setTxtCreadoPor(String txtCreadoPor) {
        this.txtCreadoPor = txtCreadoPor;
    }

    public String getTxtCreadoPor() {
        return txtCreadoPor;
    }

    public void setTxtNroReparacion(String txtNroReparacion) {
        this.txtNroReparacion = txtNroReparacion;
    }

    public String getTxtNroReparacion() {
        return txtNroReparacion;
    }

    public void setTxtNextel(String txtNextel) {
        this.txtNextel = txtNextel;
    }

    public String getTxtNextel() {
        return txtNextel;
    }

    public void setCmbTipoServicio(String cmbTipoServicio) {
        this.cmbTipoServicio = cmbTipoServicio;
    }

    public String getCmbTipoServicio() {
        return cmbTipoServicio;
    }

    public void setHdnTipoServicio(String hdnTipoServicio) {
        this.hdnTipoServicio = hdnTipoServicio;
    }

    public String getHdnTipoServicio() {
        return hdnTipoServicio;
    }

    public void setCmbModelo(String cmbModelo) {
        this.cmbModelo = cmbModelo;
    }

    public String getCmbModelo() {
        return cmbModelo;
    }

    public void setHdnModelo(String hdnModelo) {
        this.hdnModelo = hdnModelo;
    }

    public String getHdnModelo() {
        return hdnModelo;
    }

    public void setCmbTipoFalla(String cmbTipoFalla) {
        this.cmbTipoFalla = cmbTipoFalla;
    }

    public String getCmbTipoFalla() {
        return cmbTipoFalla;
    }

    public void setHdnTipoFalla(String hdnTipoFalla) {
        this.hdnTipoFalla = hdnTipoFalla;
    }

    public String getHdnTipoFalla() {
        return hdnTipoFalla;
    }

    public void setTxtImei(String txtImei) {
        this.txtImei = txtImei;
    }

    public String getTxtImei() {
        return txtImei;
    }

    public void setCmbSituacion(String cmbSituacion) {
        this.cmbSituacion = cmbSituacion;
    }

    public String getCmbSituacion() {
        return cmbSituacion;
    }

    public void setHdnSituacion(String hdnSituacion) {
        this.hdnSituacion = hdnSituacion;
    }

    public String getHdnSituacion() {
        return hdnSituacion;
    }

    public void setCmbTecnicoResponsable(String cmbTecnicoResponsable) {
        this.cmbTecnicoResponsable = cmbTecnicoResponsable;
    }

    public String getCmbTecnicoResponsable() {
        return cmbTecnicoResponsable;
    }

    public void setHdnTecnicoResponsable(String hdnTecnicoResponsable) {
        this.hdnTecnicoResponsable = hdnTecnicoResponsable;
    }

    public String getHdnTecnicoResponsable() {
        return hdnTecnicoResponsable;
    }

    public void setCmbEstadoReparacion(String cmbEstadoReparacion) {
        this.cmbEstadoReparacion = cmbEstadoReparacion;
    }

    public String getCmbEstadoReparacion() {
        return cmbEstadoReparacion;
    }

    public void setHdnEstadoReparacion(String hdnEstadoReparacion) {
        this.hdnEstadoReparacion = hdnEstadoReparacion;
    }

    public String getHdnEstadoReparacion() {
        return hdnEstadoReparacion;
    }

    public void setIFlag(int iFlag) {
      this.iFlag = iFlag;
    }
  
    public int getIFlag() {
      return iFlag;
    }
    
    public void setHdnLogin(String hdnLogin) {
      this.hdnLogin = hdnLogin;
    }
  
    public String getHdnLogin()	{
      return hdnLogin;
    }
  
    public void setHdnUserId(String hdnUserId) {
      this.hdnUserId = hdnUserId;
    }
  
    public String getHdnUserId() {
      return hdnUserId;
    }
    
    public void setCmbSolucionNegocio(String cmbSolucionNegocio) {
      this.cmbSolucionNegocio = cmbSolucionNegocio;
    }
  
    public String getCmbSolucionNegocio() {
      return cmbSolucionNegocio;
    }
  
    public void setHdnSolucionNegocio(String hdnSolucionNegocio) {
      this.hdnSolucionNegocio = hdnSolucionNegocio;
    }
  
    public String getHdnSolucionNegocio() {
      return hdnSolucionNegocio;
    }
    
    public String getHdnNumPagina() {
      return hdnNumPagina;
    }
  
    public void setHdnNumPagina(String hdnNumPagina) {
      this.hdnNumPagina = hdnNumPagina;
    }
  
    public String getHdnNumRegistros() {
      return hdnNumRegistros;
    }
  
    public void setHdnNumRegistros(String hdnNumRegistros) {
      this.hdnNumRegistros = hdnNumRegistros;
    }
    
    public boolean esBusquedaReparacion() {
      boolean bFlagReparacion = false;
      bFlagReparacion = (StringUtils.isNotEmpty(txtNroReparacion)
              || StringUtils.isNotEmpty(txtNextel)
              || StringUtils.isNotEmpty(cmbTipoServicio)
              || StringUtils.isNotEmpty(cmbTipoProceso)
              || StringUtils.isNotEmpty(cmbModelo)
              || StringUtils.isNotEmpty(cmbTipoFalla)
              || StringUtils.isNotEmpty(txtImei)
              || StringUtils.isNotEmpty(cmbSituacion)
              || StringUtils.isNotEmpty(cmbTecnicoResponsable)
              || StringUtils.isNotEmpty(cmbEstadoReparacion)
              || StringUtils.isNotEmpty(txtImeiCambio)
			  || StringUtils.isNotEmpty(txtImeiPrestamo)
              // INICIO - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 18/11/2010
              || StringUtils.isNotEmpty(cmbTipoEquipo));
              // INICIO - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 18/11/2010
      return ("SERVICIO TECNICO".equals(cmbCategoria) || bFlagReparacion || "1".equalsIgnoreCase(chkReparacion));
    }
  
  
    public void setTxtImeiCambio(String txtImeiCambio)
    {
      this.txtImeiCambio = txtImeiCambio;
    }
  
  
    public String getTxtImeiCambio()
    {
      return txtImeiCambio;
    }
  
  
    public void setTxtImeiPrestamo(String txtImeiPrestamo)
    {
      this.txtImeiPrestamo = txtImeiPrestamo;
    }
  
  
    public String getTxtImeiPrestamo()
    {
      return txtImeiPrestamo;
    }
  
  
    public void setChkReparacion(String chkReparacion)
    {
      if(chkReparacion == null){
        chkReparacion="";
      }
      this.chkReparacion = chkReparacion;
    }
  
  
    public String getChkReparacion()
    {
      return chkReparacion;
    }
  
  
    public void setCmbTipoProceso(String cmbTipoProceso)
    {
      this.cmbTipoProceso = cmbTipoProceso;
    }
  
  
    public String getCmbTipoProceso()
    {
      return cmbTipoProceso;
    }
  
  
    public void setHdnTipoProceso(String hdnTipoProceso)
    {
      this.hdnTipoProceso = hdnTipoProceso;
    }
  
  
    public String getHdnTipoProceso()
    {
      return hdnTipoProceso;
    }
    
    public String getChkFastSearch() {
      return chkFastSearch;
    }
    
    public void setChkFastSearch(String chkFastSearch) {
      if(chkFastSearch == null){
        chkFastSearch = "";
      }
      this.chkFastSearch = chkFastSearch; 
    }
	

  public void setCmbDivisionNegocio(String cmbDivisionNegocio)
  {
    this.cmbDivisionNegocio = cmbDivisionNegocio;
  }


  public String getCmbDivisionNegocio()
  {
    return cmbDivisionNegocio;
  }


  public void setHdnSalesstructid(String hdnSalesstructid)
  {
    this.hdnSalesstructid = hdnSalesstructid;
  }


  public String getHdnSalesstructid()
  {
    return hdnSalesstructid;
  }


  public void setHdnProviderid(String hdnProviderid)
  {
    this.hdnProviderid = hdnProviderid;
  }


  public String getHdnProviderid()
  {
    return hdnProviderid;
  }


  public void setStrUnidadJerarquica(String strUnidadJerarquica)
  {
    this.strUnidadJerarquica = strUnidadJerarquica;
  }


  public String getStrUnidadJerarquica()
  {
    return strUnidadJerarquica;
  }


  public void setStrRepresentante(String strRepresentante)
  {
    this.strRepresentante = strRepresentante;
  }


  public String getStrRepresentante()
  {
    return strRepresentante;
  }


  public void setTxtProposedId(String txtProposedId)
  {
    this.txtProposedId = txtProposedId;
  }


  public String getTxtProposedId()
  {
    return txtProposedId;
  }

  // INICIO - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 30/09/2010
   public void setCmbTipoEquipo(String cmbTipoEquipo)
  {
    this.cmbTipoEquipo = cmbTipoEquipo;
  }

  public String getCmbTipoEquipo()
  {
    return cmbTipoEquipo;
  }     
  // FIN - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 30/09/2010
  
  public void setChkNumberSearch(String chkNumberSearch) {
    if(chkNumberSearch==null) 
      chkNumberSearch="0";
    this.chkNumberSearch = chkNumberSearch;
  }

  public String getChkNumberSearch() {
    return chkNumberSearch;
  }

  public String getcmbRangoCuenta() // OCRUCES 29/10/2012
    {
      return cmbRangoCuenta;
    } 
    
     public void  setcmbRangoCuenta(String v) // OCRUCES 29/10/2012
    {
       cmbRangoCuenta = v;
    }

    public void setCmbMarcaDap(String cmbMarcaDap) {
        this.cmbMarcaDap = cmbMarcaDap;
    }

    public String getCmbMarcaDap() {
        return cmbMarcaDap;
    }

    public void setCmbEstadoOrdenExterna(String cmbEstadoOrdenExterna) {
        this.cmbEstadoOrdenExterna = cmbEstadoOrdenExterna;
    }

    public String getCmbEstadoOrdenExterna() {
        return cmbEstadoOrdenExterna;
    }

    public void setCmbRespuestaCotizacion(String cmbRespuestaCotizacion) {
        this.cmbRespuestaCotizacion = cmbRespuestaCotizacion;
    }

    public String getCmbRespuestaCotizacion() {
        return cmbRespuestaCotizacion;
    }

    public void setCmbSituacionEquipo(String cmbSituacionEquipo) {
        this.cmbSituacionEquipo = cmbSituacionEquipo;
    }

    public String getCmbSituacionEquipo() {
        return cmbSituacionEquipo;
    }

    public void setCmbTiendaRecojo(String cmbTiendaRecojo) {
        this.cmbTiendaRecojo = cmbTiendaRecojo;
    }

    public String getCmbTiendaRecojo() {
        return cmbTiendaRecojo;
    }

    public String getCmbSegmentoCompania() {
        return cmbSegmentoCompania;
    }

    public void setCmbSegmentoCompania(String cmbSegmentoCompania) {
        this.cmbSegmentoCompania = cmbSegmentoCompania;
    }

    public String getHdnSegmentoCompania() {
        return hdnSegmentoCompania;
    }

    public void setHdnSegmentoCompania(String hdnSegmentoCompania) {
        this.hdnSegmentoCompania = hdnSegmentoCompania;
    }

    // INICIO PRY-1239 18/09/2018 PCACERES
    public void setCmbWebPayment(String cmbWebPayment) { this.cmbWebPayment = cmbWebPayment; }

    public String getCmbWebPayment() {    return cmbWebPayment;  }
    // FIN PRY-1239 18/09/2018 PCACERES
}
