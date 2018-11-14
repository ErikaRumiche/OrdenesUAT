package pe.com.nextel.bean;

import java.io.Serializable;

import java.util.ArrayList;

import pe.com.nextel.util.GenericObject;


public class LoadMassiveItemBean extends GenericObject implements Serializable {
  private static final long serialVersionUID = 1L;
	private Long lOrderId;
	private Long lDispatchPlace;
	private ArrayList imei = new ArrayList();
	private ArrayList sim = new ArrayList();
	private ArrayList message = new ArrayList();
  private Long lCantErroneos = new Long(0);

	public Long getLOrderId() {
		return lOrderId;
	}

	public void setLOrderId(Long orderId) {
		this.lOrderId = orderId;
	}

	public Long getLDispatchPlace() {
		return lDispatchPlace;
	}

	public void setLDispatchPlace(Long dispatchPlace) {
		this.lDispatchPlace = dispatchPlace;
	}

	public ArrayList getImei() {
		return imei;
	}

	public void setImei(ArrayList imei) {
		this.imei = imei;
	}

	public ArrayList getSim() {
		return sim;
	}

	public void setSim(ArrayList sim) {
		this.sim = sim;
	}

	public ArrayList getMessage() {
		return message;
	}

	public void setMessage(ArrayList message) {
		this.message = message;
	}

	public String getImei(int index) {
		return (String) imei.get(index);
	}

	public String getSim(int index) {
		return (String) sim.get(index);
	}

	public String getMessage(int index) {
		return (String) message.get(index);
	}
  
	public Long getLCantErroneos() {
		return lCantErroneos;
	}

	public void setLCantErroneos(Long lCantErroneos) {
		this.lCantErroneos = lCantErroneos;
	}
  
  public void agregarErroneo() {
    this.lCantErroneos = new Long(this.lCantErroneos.longValue() + 1);
  }
  
  public String[] getArrayImei() {
    String[] arrImei = new String[imei.size()];
    for(int i=0; i<imei.size(); i++) {
      arrImei[i] = getImei(i);
    }
    return arrImei;
  }
  
  public String[] getArraySim() {
    String[] arrSim = new String[sim.size()];
    for(int i=0; i<sim.size(); i++) {
      arrSim[i] = getSim(i);
    }
    return arrSim;
  }

}
