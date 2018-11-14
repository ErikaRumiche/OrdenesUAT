package pe.com.nextel.bean;

import java.io.Serializable;

import pe.com.nextel.util.GenericObject;


public class ProcCompMasBean extends GenericObject implements Serializable {
    
    // campos para el reporte de procesos compuestos masivos
    private static final long serialVersionUID = 1L;
    private String solicitudProcesosMasivos;
    private String procesosMasivos;
    private String fechaDiferida;
    private String fechaProceso;
    private String estadoProceso;
    private String mensajeError;
    private String resumenProceso;    
    private String nextel;
    private String razonSocial;
    private String usuarioCreacion;
    private String fechaCreacion;
    private String usuarioModificacion;
    private String fechaModificacion;
    private String contractNumber;
        
    public ProcCompMasBean() {
        
    }
    
    public void setSolicitudProcesosMasivos(String solicitudProcesosMasivos) {    
      this.solicitudProcesosMasivos=solicitudProcesosMasivos;
    }
    
    public String getSolicitudProcesosMasivos() {
      return solicitudProcesosMasivos;
    }

    public void setProcesosMasivos(String procesosMasivos) {  
      this.procesosMasivos=procesosMasivos;
    }
    
    public String getProcesosMasivos() {  
      return procesosMasivos;
    }

    public void setFechaProceso(String fechaProceso) {
      this.fechaProceso=fechaProceso;
    }
    
    public String getFechaProceso() {
      return fechaProceso;
    }

    public void setEstadoProceso(String estadoProceso){
      this.estadoProceso=estadoProceso;
    }
    
    public String getEstadoProceso(){
      return estadoProceso;
    }

    public void setMensajeError(String mensajeError){
      this.mensajeError=mensajeError;
    }
    
    public String getMensajeError(){
      return mensajeError;
    }

    public void setResumenProceso(String resumenProceso){
      this.resumenProceso=resumenProceso;
    }
    
    public String getResumenProceso(){
      return resumenProceso;
    }        
    
    public String getNextel(){
      return nextel;
    }
    
    public void setNextel(String nextel) {    
      this.nextel=nextel;
    }
    
    public String getRazonSocial() {
      return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {  
      this.razonSocial=razonSocial;
    }
    
    public String getUsuarioCreacion() {  
      return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
      this.usuarioCreacion=usuarioCreacion;
    }
    
    public String getFechaCreacion() {
      return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion){
      this.fechaCreacion=fechaCreacion;
    }
    
    public String getUsuarioModificacion(){
      return usuarioModificacion;
    }
    
    public void setUsuarioModificacion(String detalladoCodCliente){
      this.usuarioModificacion=detalladoCodCliente;
    }
    
    public String getFechaModificacion(){
      return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion){
      this.fechaModificacion=fechaModificacion;
    }
    
    public String getFechaDiferida(){
      return fechaDiferida;
    }

    public void setFechaDiferida(String fechaDiferida){
      this.fechaDiferida=fechaDiferida;
    }
    public String getContractNumber(){
      return contractNumber;
    }

    public void setContractNumber(String contractNumber){
      this.contractNumber=contractNumber;
    }
    
    
    
    
    
}