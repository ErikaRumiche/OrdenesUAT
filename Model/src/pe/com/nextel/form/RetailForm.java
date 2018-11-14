package pe.com.nextel.form;

import java.io.Serializable;


public class RetailForm extends GenericForm implements Serializable{
    private static final long serialVersionUID = 1L;
    private String cmbTipoDocumento;
    private String hdnTipoDocumento;
    private String txtNroDocumento;
    private String txtCodigoBSCS;
    private String txtTipoCliente;
    private String cmbCuenta;
    private String hdnCuenta;
    private String txtRazonSocial;
    private String cmbTituloCliente;
    private String hdnTituloCliente;
    private String txtNombres;
    private String txtApellidos;
    private String[] txtDireccion = new String[2];
    private String cmbDepartamento;
    private String hdnDepartamento;
    private String cmbProvincia;
    private String hdnProvincia;
    private String cmbDistrito;
    private String hdnDistrito;
    private String txtTelefono;
    private String cmbGiro;
    private String hdnGiro;
    private String cmbSubGiro;
    private String hdnSubGiro;
    ////////////////////////////////////////////////
    private String txtNroSolicitud;
    private String cmbTienda;
    private String hdnTienda;
    private String txtObservaciones;
	private String hdnLogin;
    ////////////////////////////////////////////////
    private String[] hdnItem;
    private String[] txtIMEI;
    private String[] txtEquipo;
    private String[] txtServicio;
    private String[] txtPlanTarifario;
	private String[] hdnPlanTarifario;
    private String[] cmbKit;
    private String[] hdnKit;
    private String[] txtContrato;
    private String[] txtNextel;
    private String[] txtModelo;
    private String[] txtSIM;
     ////////////////////////////////////////////////
    private String[] txtNroVoucher;

    public RetailForm() {
    }

    public String getCmbTipoDocumento() {
        return cmbTipoDocumento;
    }

    public void setCmbTipoDocumento(String cmbTipoDocumento) {
        this.cmbTipoDocumento = cmbTipoDocumento;
    }

    public String getHdnTipoDocumento() {
        return hdnTipoDocumento;
    }

    public void setHdnTipoDocumento(String hdnTipoDocumento) {
        this.hdnTipoDocumento = hdnTipoDocumento;
    }

    public String getTxtNroDocumento() {
        return txtNroDocumento;
    }

    public void setTxtNroDocumento(String txtNroDocumento) {
        this.txtNroDocumento = txtNroDocumento;
    }

    public String getTxtCodigoBSCS() {
        return txtCodigoBSCS;
    }

    public void setTxtCodigoBSCS(String txtCodigoBSCS) {
        this.txtCodigoBSCS = txtCodigoBSCS;
    }

    public String getTxtTipoCliente() {
        return txtTipoCliente;
    }

    public void setTxtTipoCliente(String txtTipoCliente) {
        this.txtTipoCliente = txtTipoCliente;
    }

    public String getCmbCuenta() {
        return cmbCuenta;
    }

    public void setCmbCuenta(String cmbCuenta) {
        this.cmbCuenta = cmbCuenta;
    }

    public String getHdnCuenta() {
        return hdnCuenta;
    }

    public void setHdnCuenta(String hdnCuenta) {
        this.hdnCuenta = hdnCuenta;
    }

    public String getTxtRazonSocial() {
        return txtRazonSocial;
    }

    public void setTxtRazonSocial(String txtRazonSocial) {
        this.txtRazonSocial = txtRazonSocial;
    }

    public String getCmbTituloCliente() {
        return cmbTituloCliente;
    }

    public void setCmbTituloCliente(String cmbTituloCliente) {
        this.cmbTituloCliente = cmbTituloCliente;
    }

    public String getHdnTituloCliente() {
        return hdnTituloCliente;
    }

    public void setHdnTituloCliente(String hdnTituloCliente) {
        this.hdnTituloCliente = hdnTituloCliente;
    }

    public String getTxtNombres() {
        return txtNombres;
    }

    public void setTxtNombres(String txtNombres) {
        this.txtNombres = txtNombres;
    }

    public String getTxtApellidos() {
        return txtApellidos;
    }

    public void setTxtApellidos(String txtApellidos) {
        this.txtApellidos = txtApellidos;
    }

    public String[] getTxtDireccion() {
        return txtDireccion;
    }

    public void setTxtDireccion(String[] txtDireccion) {
        this.txtDireccion = txtDireccion;
    }

    public String getCmbDepartamento() {
        return cmbDepartamento;
    }

    public void setCmbDepartamento(String cmbDepartamento) {
        this.cmbDepartamento = cmbDepartamento;
    }

    public String getHdnDepartamento() {
        return hdnDepartamento;
    }

    public void setHdnDepartamento(String hdnDepartamento) {
        this.hdnDepartamento = hdnDepartamento;
    }

    public String getCmbProvincia() {
        return cmbProvincia;
    }

    public void setCmbProvincia(String cmbProvincia) {
        this.cmbProvincia = cmbProvincia;
    }

    public String getHdnProvincia() {
        return hdnProvincia;
    }

    public void setHdnProvincia(String hdnProvincia) {
        this.hdnProvincia = hdnProvincia;
    }

    public String getCmbDistrito() {
        return cmbDistrito;
    }

    public void setCmbDistrito(String cmbDistrito) {
        this.cmbDistrito = cmbDistrito;
    }

    public String getHdnDistrito() {
        return hdnDistrito;
    }

    public void setHdnDistrito(String hdnDistrito) {
        this.hdnDistrito = hdnDistrito;
    }

    public String getTxtTelefono() {
        return txtTelefono;
    }

    public void setTxtTelefono(String txtTelefono) {
        this.txtTelefono = txtTelefono;
    }

    public String getCmbGiro() {
        return cmbGiro;
    }

    public void setCmbGiro(String cmbGiro) {
        this.cmbGiro = cmbGiro;
    }

    public String getHdnGiro() {
        return hdnGiro;
    }

    public void setHdnGiro(String hdnGiro) {
        this.hdnGiro = hdnGiro;
    }

    public String getCmbSubGiro() {
        return cmbSubGiro;
    }

    public void setCmbSubGiro(String cmbSubGiro) {
        this.cmbSubGiro = cmbSubGiro;
    }

    public String getHdnSubGiro() {
        return hdnSubGiro;
    }

    public void setHdnSubGiro(String hdnSubGiro) {
        this.hdnSubGiro = hdnSubGiro;
    }

    public String getTxtNroSolicitud() {
        return txtNroSolicitud;
    }

    public void setTxtNroSolicitud(String txtNroSolicitud) {
        this.txtNroSolicitud = txtNroSolicitud;
    }

    public String getCmbTienda() {
        return cmbTienda;
    }

    public void setCmbTienda(String cmbTienda) {
        this.cmbTienda = cmbTienda;
    }

    public String getHdnTienda() {
        return hdnTienda;
    }

    public void setHdnTienda(String hdnTienda) {
        this.hdnTienda = hdnTienda;
    }

    public String getTxtObservaciones() {
        return txtObservaciones;
    }

    public void setTxtObservaciones(String txtObservaciones) {
        this.txtObservaciones = txtObservaciones;
    }
	
    public String getHdnLogin() {
        return hdnLogin;
    }

    public void setHdnLogin(String hdnLogin) {
        this.hdnLogin = hdnLogin;
    }

    public String[] getTxtIMEI() {
        return txtIMEI;
    }

    public void setTxtIMEI(String[] txtIMEI) {
        this.txtIMEI = txtIMEI;
    }

    public String[] getTxtEquipo() {
        return txtEquipo;
    }

    public void setTxtEquipo(String[] txtEquipo) {
        this.txtEquipo = txtEquipo;
    }

    public String[] getTxtServicio() {
        return txtServicio;
    }

    public void setTxtServicio(String[] txtServicio) {
        this.txtServicio = txtServicio;
    }

    public void setTxtPlanTarifario(String[] txtPlanTarifario) {
        this.txtPlanTarifario = txtPlanTarifario;
    }

    public String[] getTxtPlanTarifario() {
        return txtPlanTarifario;
    }

    public void setHdnPlanTarifario(String[] hdnPlanTarifario) {
        this.hdnPlanTarifario = hdnPlanTarifario;
    }

    public String[] getHdnPlanTarifario() {
        return hdnPlanTarifario;
    }

    public void setCmbKit(String[] cmbKit) {
        this.cmbKit = cmbKit;
    }

    public String[] getCmbKit() {
        return cmbKit;
    }

    public void setHdnKit(String[] hdnKit) {
        this.hdnKit = hdnKit;
    }

    public String[] getHdnKit() {
        return hdnKit;
    }

    public void setTxtContrato(String[] txtContrato) {
        this.txtContrato = txtContrato;
    }

    public String[] getTxtContrato() {
        return txtContrato;
    }

    public void setTxtNextel(String[] txtNextel) {
        this.txtNextel = txtNextel;
    }

    public String[] getTxtNextel() {
        return txtNextel;
    }

    public void setTxtModelo(String[] txtModelo) {
        this.txtModelo = txtModelo;
    }

    public String[] getTxtModelo() {
        return txtModelo;
    }

    public void setTxtSIM(String[] txtSIM) {
        this.txtSIM = txtSIM;
    }

    public String[] getTxtSIM() {
        return txtSIM;
    }

    public void setTxtNroVoucher(String[] txtNroVoucher) {
        this.txtNroVoucher = txtNroVoucher;
    }

    public String[] getTxtNroVoucher() {
        return txtNroVoucher;
    }

    public void setHdnItem(String[] hdnItem) {
        this.hdnItem = hdnItem;
    }

    public String[] getHdnItem() {
        return hdnItem;
    }
}
