package pe.com.nextel.bean;

public class PenaltyBean {
    private String numItemPenaltyId;
    private String numAdenda;
    private String telefono;
    private String fechaInicio;
    private String fechaFin;
    private int diasTotales;
    private int diasEfectivos;
    private double penalidad;
    private double montoFinal;
    private long fastOrderId;
    private int plazo;
    private String nomEquipo;
    private int habilitado;
    private int motivo;
    private String numImeiId;
    private double montoBeneficio;

    public PenaltyBean(){
    }

    public String getNumAdenda() {
        return numAdenda;
    }

    public void setNumAdenda(String numAdenda) {
        this.numAdenda = numAdenda;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getDiasTotales() {
        return diasTotales;
    }

    public void setDiasTotales(int diasTotales) {
        this.diasTotales = diasTotales;
    }

    public int getDiasEfectivos() {
        return diasEfectivos;
    }

    public void setDiasEfectivos(int diasEfectivos) {
        this.diasEfectivos = diasEfectivos;
    }

    public double getPenalidad() {
        return penalidad;
    }

    public void setPenalidad(double penalidad) {
        this.penalidad = penalidad;
    }

    public double getMontoFinal() {
        return montoFinal;
    }

    public void setMontoFinal(double montoFinal) {
        this.montoFinal = montoFinal;
    }

    public int getPlazo() {
        return plazo;
    }

    public void setPlazo(int plazo) {
        this.plazo = plazo;
    }

    public String getNomEquipo() {
        return nomEquipo;
    }

    public void setNomEquipo(String nomEquipo) {
        this.nomEquipo = nomEquipo;
    }

    public int getHabilitado() {return habilitado;}

    public void setHabilitado(int habilitado) {this.habilitado = habilitado;}

    public long getFastOrderId() {return fastOrderId;}

    public void setFastOrderId(long fastOrderId) {this.fastOrderId = fastOrderId;}

    public int getMotivo() {
        return motivo;
    }

    public void setMotivo(int motivo) {
        this.motivo = motivo;
    }

    public String getNumItemPenaltyId() {
        return numItemPenaltyId;
    }

    public void setNumItemPenaltyId(String numItemPenaltyId) {
        this.numItemPenaltyId = numItemPenaltyId;
    }

    public String getNumImeiId() {
        return numImeiId;
    }

    public void setNumImeiId(String numImeiId) {
        this.numImeiId = numImeiId;
    }

    public double getMontoBeneficio() {return montoBeneficio;}

    public void setMontoBeneficio(double montoBeneficio) {this.montoBeneficio = montoBeneficio;}
}
