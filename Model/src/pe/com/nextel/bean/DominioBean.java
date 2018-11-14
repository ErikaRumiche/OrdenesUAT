package pe.com.nextel.bean;

import java.io.Serializable;

import pe.com.nextel.util.GenericObject;


public class DominioBean extends GenericObject implements Serializable {
    
    private String valor;
    private String descripcion;
    private String descripcion_aux;
    private String param1;
    private String param2;
    private String param3;
    private String status;
    
    public DominioBean() {
        
    }


    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }


  public void setDescripcion_aux(String descripcion_aux)
  {
    this.descripcion_aux = descripcion_aux;
  }


  public String getDescripcion_aux()
  {
    return descripcion_aux;
  }


  public void setParam1(String param1)
  {
    this.param1 = param1;
  }


  public String getParam1()
  {
    return param1;
  }


  public void setParam2(String param2)
  {
    this.param2 = param2;
  }


  public String getParam2()
  {
    return param2;
  }


  public void setParam3(String param3)
  {
    this.param3 = param3;
  }


  public String getParam3()
  {
    return param3;
  }


  public void setStatus(String status)
  {
    this.status = status;
  }


  public String getStatus()
  {
    return status;
  }
}
