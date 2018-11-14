package pe.com.nextel.bean;

import java.io.Serializable;

public class PaymentBean implements Serializable{
  private static final long serialVersionUID = 1L;
   private long npPaymentorderId;
   private String npName;
   private String npRuc;   
   private String estado;      
   private String moneda;         
   private double npTotalAmount;            
   private double npDeudaAmount;               
   private String npConceptName;               
   private java.sql.Date npCreatedDate;                  
   private int voucher;
   private double monto;
   private String tienda;
   private String caja;   

   public PaymentBean()
   {
   }


   public void setNpPaymentorderId(long npPaymentorderId)
   {
      this.npPaymentorderId = npPaymentorderId;
   }


   public long getNpPaymentorderId()
   {
      return npPaymentorderId;
   }


   public void setNpName(String npName)
   {
      this.npName = npName;
   }


   public String getNpName()
   {
      return npName;
   }


   public void setNpRuc(String npRuc)
   {
      this.npRuc = npRuc;
   }


   public String getNpRuc()
   {
      return npRuc;
   }


   public void setEstado(String estado)
   {
      this.estado = estado;
   }


   public String getEstado()
   {
      return estado;
   }


   public void setMoneda(String moneda)
   {
      this.moneda = moneda;
   }


   public String getMoneda()
   {
      return moneda;
   }


   public void setNpTotalAmount(double npTotalAmount)
   {
      this.npTotalAmount = npTotalAmount;
   }


   public double getNpTotalAmount()
   {
      return npTotalAmount;
   }


   public void setNpDeudaAmount(double npDeudaAmount)
   {
      this.npDeudaAmount = npDeudaAmount;
   }


   public double getNpDeudaAmount()
   {
      return npDeudaAmount;
   }


   public void setNpConceptName(String npConceptName)
   {
      this.npConceptName = npConceptName;
   }


   public String getNpConceptName()
   {
      return npConceptName;
   }


   public void setNpCreatedDate(java.sql.Date npCreatedDate)
   {
      this.npCreatedDate = npCreatedDate;
   }


   public java.sql.Date getNpCreatedDate()
   {
      return npCreatedDate;
   }


   public void setVoucher(int voucher)
   {
      this.voucher = voucher;
   }


   public int getVoucher()
   {
      return voucher;
   }


   public void setMonto(double monto)
   {
      this.monto = monto;
   }


   public double getMonto()
   {
      return monto;
   }


   public void setTienda(String tienda)
   {
      this.tienda = tienda;
   }


   public String getTienda()
   {
      return tienda;
   }


   public void setCaja(String caja)
   {
      this.caja = caja;
   }


   public String getCaja()
   {
      return caja;
   }
}