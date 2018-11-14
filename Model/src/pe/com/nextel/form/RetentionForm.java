package pe.com.nextel.form;


public class RetentionForm extends GenericForm {


  private static final long serialVersionUID = 1L;
  private String hdnLogin;
  private String txtFechaDesde;
  private String txtFechaHasta;
  private String hdnAreaCal;
  private String hdnSuspensionReason;  
  private String hdnNumRegistros;
  private String hdnNumPagina;  


  public RetentionForm() {
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

  public void setHdnAreaCal(String hdnAreaCal) {
    this.hdnAreaCal = hdnAreaCal;
  }

  public String getHdnAreaCal() {
    return hdnAreaCal;
  }

  public void setHdnSuspensionReason(String hdnSuspensionReason) {
    this.hdnSuspensionReason = hdnSuspensionReason;
  }

  public String getHdnSuspensionReason() {
    return hdnSuspensionReason;
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