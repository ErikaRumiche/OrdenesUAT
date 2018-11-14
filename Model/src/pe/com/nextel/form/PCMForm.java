package pe.com.nextel.form;


public class PCMForm extends GenericForm {

  private static final long serialVersionUID = 1L;
  private String hdnLogin;
  private String txtFechaDesde;
  private String txtFechaHasta;  
  
  private String hdnProceso;
  private String hdnEstado;  
  private String txtTelefono;
  private String txtRazonSocial;  
  
  private String hdnNumRegistros;
  private String hdnNumPagina;  

  public PCMForm() {
    }

  public void setHdnLogin(String hdnLogin) {
    this.hdnLogin = hdnLogin;
  }

  public String getHdnLogin() {
    return hdnLogin;
  }

  public void setTxtFechaDesde(String txtFechaDesde) {
    this.txtFechaDesde = txtFechaDesde;
  }

  public String getTxtFechaDesde() {
    return txtFechaDesde;
  }

  public void setTxtFechaHasta(String txtFechaHasta) {
    this.txtFechaHasta = txtFechaHasta;
  }

  public String getTxtFechaHasta() {
    return txtFechaHasta;
  }
  
  public void setHdnProceso(String hdnProceso) {
    this.hdnProceso = hdnProceso;
  }

  public String getHdnProceso() {
    return hdnProceso;
  }
  
  public void setHdnEstado(String hdnEstado) {
    this.hdnEstado = hdnEstado;
  }

  public String getHdnEstado() {
    return hdnEstado;
  }

  public void setTxtTelefono(String txtTelefono) {
    this.txtTelefono = txtTelefono;
  }

  public String getTxtTelefono() {
    return txtTelefono;
  }
  
  public void setTxtRazonSocial(String txtRazonSocial) {
    this.txtRazonSocial = txtRazonSocial;
  }

  public String getTxtRazonSocial() {
    return txtRazonSocial;
  }
    
  public void setHdnNumRegistros(String hdnNumRegistros) {
    this.hdnNumRegistros = hdnNumRegistros;
  }

  public String getHdnNumRegistros() {
    return hdnNumRegistros;
  }

  public void setHdnNumPagina(String hdnNumPagina) {
    this.hdnNumPagina = hdnNumPagina;
  }

  public String getHdnNumPagina() {
    return hdnNumPagina;
  }

}