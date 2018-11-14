package pe.com.nextel.bean;

import java.io.Serializable;

import pe.com.nextel.util.GenericObject;


public class SubRegCustomerInfoBean extends GenericObject implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String strRazonSoc;
  private long orderId;
  private String strLogin;
  private long customerId;
  private String status;
  private String statusOrder;
  private String strPhone;
  
  public SubRegCustomerInfoBean()
  {
  }


  public void setStrRazonSoc(String strRazonSoc)
  {
    this.strRazonSoc = strRazonSoc;
  }


  public String getStrRazonSoc()
  {
    return strRazonSoc;
  }


  public void setOrderId(long orderId)
  {
    this.orderId = orderId;
  }


  public long getOrderId()
  {
    return orderId;
  }


  public void setStrLogin(String strLogin)
  {
    this.strLogin = strLogin;
  }


  public String getStrLogin()
  {
    return strLogin;
  }


  public void setCustomerId(long customerId)
  {
    this.customerId = customerId;
  }


  public long getCustomerId()
  {
    return customerId;
  }


  public void setStatus(String status)
  {
    this.status = status;
  }


  public String getStatus()
  {
    return status;
  }


  public void setStatusOrder(String statusOrder)
  {
    this.statusOrder = statusOrder;
  }


  public String getStatusOrder()
  {
    return statusOrder;
  }


  public void setStrPhone(String strPhone)
  {
    this.strPhone = strPhone;
  }


  public String getStrPhone()
  {
    return strPhone;
  }

}