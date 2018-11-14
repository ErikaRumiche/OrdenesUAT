package pe.com.nextel.bean;

import java.io.Serializable;

import pe.com.nextel.util.GenericObject;


public class ProductPriceBean extends GenericObject implements Serializable{

  private static final long serialVersionUID = 1L;
   private long npproductid;
   private long npsolutionid;
   private double nporiginalprice;
   private double nppriceonetime;
   private double nppricerecurring;
   private String npcurrency;
   private String npobjecttype; //Tipo de Precio: PROMOTION, AGREEMENT, ORIGINAL
   private long npobjectid;  // Código del tipo de precio
   private long npobjectitemid; //Código del item del acuerdo
   


  public void setNpproductid(long npproductid)
  {
    this.npproductid = npproductid;
  }


  public long getNpproductid()
  {
    return npproductid;
  }


  public void setNpsolutionid(long npsolutionid)
  {
    this.npsolutionid = npsolutionid;
  }


  public long getNpsolutionid()
  {
    return npsolutionid;
  }


  public void setNporiginalprice(double nporiginalprice)
  {
    this.nporiginalprice = nporiginalprice;
  }


  public double getNporiginalprice()
  {
    return nporiginalprice;
  }


  public void setNppriceonetime(double nppriceonetime)
  {
    this.nppriceonetime = nppriceonetime;
  }


  public double getNppriceonetime()
  {
    return nppriceonetime;
  }


  public void setNppricerecurring(double nppricerecurring)
  {
    this.nppricerecurring = nppricerecurring;
  }


  public double getNppricerecurring()
  {
    return nppricerecurring;
  }


  public void setNpcurrency(String npcurrency)
  {
    this.npcurrency = npcurrency;
  }


  public String getNpcurrency()
  {
    return npcurrency;
  }


  public void setNpobjecttype(String npobjecttype)
  {
    this.npobjecttype = npobjecttype;
  }


  public String getNpobjecttype()
  {
    return npobjecttype;
  }


  public void setNpobjectid(long npobjectid)
  {
    this.npobjectid = npobjectid;
  }


  public long getNpobjectid()
  {
    return npobjectid;
  }


  public void setNpobjectitemid(long npobjectitemid)
  {
    this.npobjectitemid = npobjectitemid;
  }


  public long getNpobjectitemid()
  {
    return npobjectitemid;
  }

}