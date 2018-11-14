package pe.com.nextel.bean;

import java.io.Serializable;

public class ContractServiceBscsBean implements Serializable
{ 
  private static final long serialVersionUID = 1L;
  private int hiddenId;
  private String serviceCode;
  private String strMessage;
  private String strTypeError;

  public void setHiddenId(int hiddenId)
  {
    this.hiddenId = hiddenId;
  }

  public int getHiddenId()
  {
    return hiddenId;
  }

  public void setServiceCode(String serviceCode)
  {
    this.serviceCode = serviceCode;
  }

  public String getServiceCode()
  {
    return serviceCode;
  }


  public void setStrMessage(String strMessage)
  {
    this.strMessage = strMessage;
  }


  public String getStrMessage()
  {
    return strMessage;
  }


  public void setStrTypeError(String strTypeError)
  {
    this.strTypeError = strTypeError;
  }


  public String getStrTypeError()
  {
    return strTypeError;
  }
 
}