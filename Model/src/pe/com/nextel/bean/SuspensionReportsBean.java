package pe.com.nextel.bean;

import java.io.Serializable;

import pe.com.nextel.util.GenericObject;


public class SuspensionReportsBean extends GenericObject implements Serializable {
  private static final long serialVersionUID = 1L;
    // campos para el reporte general
    private String generalMotivo;
    private String generalArea;
    private String generalAsesor;
    private String generalProgramadas;
    private String generalRetenidas;
    private String generalEfectividad;
    
    // campos para el reporte detallado
    
    private String detalladoArea;
    private String detalladoAsesor;
    private String detalladoTipoCliente;
    private String detalladoNCorporativo;
    private String detalladoCodCliente;
    private String detalladoCuenta;
    private String detalladoNombreCliente;
    private String detalladoFechaRegistro;
    private String detalladoFechaEjecucion;
    private String detalladoMotivo;
    private String detalladoHerramienta;
    private String detalladoTelefono;
    private String detalladoEstado;
        
    public SuspensionReportsBean() {
        
    }
    
    public void setGeneralMotivo(String generalMotivo) {    
      this.generalMotivo=generalMotivo;
    }
    
    public String getGeneralMotivo() {
      return generalMotivo;
    }

    public void setGeneralArea(String generalArea) {  
      this.generalArea=generalArea;
    }
    
    public String getGeneralArea() {  
      return generalArea;
    }

    public void setGeneralAsesor(String generalAsesor) {
      this.generalAsesor=generalAsesor;
    }
    
    public String getGeneralAsesor() {
      return generalAsesor;
    }

    public void setGeneralProgramadas(String generalProgramadas){
      this.generalProgramadas=generalProgramadas;
    }
    
    public String getGeneralProgramadas(){
      return generalProgramadas;
    }

    public void setGeneralRetenidas(String generalRetenidas){
      this.generalRetenidas=generalRetenidas;
    }
    
    public String getGeneralRetenidas(){
      return generalRetenidas;
    }

    public void setGeneralEfectividad(String generalEfectividad){
      this.generalEfectividad=generalEfectividad;
    }
    
    public String getGeneralEfectividad(){
      return generalEfectividad;
    }        
    
    public String getDetalladoArea(){
      return detalladoArea;
    }
    
    public void setDetalladoArea(String detalladoArea) {    
      this.detalladoArea=detalladoArea;
    }
    
    public String getDetalladoAsesor() {
      return detalladoAsesor;
    }

    public void setDetalladoAsesor(String detalladoAsesor) {  
      this.detalladoAsesor=detalladoAsesor;
    }
    
    public String getDetalladoTipoCliente() {  
      return detalladoTipoCliente;
    }

    public void setDetalladoTipoCliente(String detalladoTipoCliente) {
      this.detalladoTipoCliente=detalladoTipoCliente;
    }
    
    public String getDetalladoNCorporativo() {
      return detalladoNCorporativo;
    }

    public void setDetalladoNCorporativo(String detalladoNCorporativo){
      this.detalladoNCorporativo=detalladoNCorporativo;
    }
    
    public String getDetalladoCodCliente(){
      return detalladoCodCliente;
    }
    
    public void setDetalladoCodCliente(String detalladoCodCliente){
      this.detalladoCodCliente=detalladoCodCliente;
    }
    
    public String getDetalladoCuenta(){
      return detalladoCuenta;
    }

    public void setDetalladoCuenta(String detalladoCuenta){
      this.detalladoCuenta=detalladoCuenta;
    }
    
    public void setDetalladoNombreCliente(String detalladoNombreCliente){
      this.detalladoNombreCliente=detalladoNombreCliente;
    }
    
    public String getDetalladoNombreCliente(){
      return detalladoNombreCliente;
    }
    
    public void setDetalladoFechaRegistro(String detalladoFechaRegistro){
      this.detalladoFechaRegistro=detalladoFechaRegistro;
    }
    
    public String getDetalladoFechaRegistro(){
      return detalladoFechaRegistro;
    }

    public void setDetalladoFechaEjecucion(String detalladoFechaEjecucion){
      this.detalladoFechaEjecucion=detalladoFechaEjecucion;
    }
    
    public String getDetalladoFechaEjecucion(){
      return detalladoFechaEjecucion;
    }
    
    public void setDetalladoMotivo(String detalladoMotivo){
      this.detalladoMotivo=detalladoMotivo;
    }
    
    public String getDetalladoMotivo(){
      return detalladoMotivo;
    }
    
    public void setDetalladoHerramienta(String detalladoHerramienta){
      this.detalladoHerramienta=detalladoHerramienta;
    }
    
    public String getDetalladoHerramienta(){
      return detalladoHerramienta;
    }
    
    public void setDetalladoTelefono(String detalladoTelefono){
      this.detalladoTelefono=detalladoTelefono;
    }
    
    public String getDetalladoTelefono(){
      return detalladoTelefono;
    }
    
    public void setDetalladoEstado(String detalladoEstado){
      this.detalladoEstado=detalladoEstado;
    }
    
    public String getDetalladoEstado(){
      return detalladoEstado;
    }

    
}