package pe.com.nextel.bean;

import java.io.Serializable;

import pe.com.nextel.util.GenericObject;


public class CreditEvaluationBean extends GenericObject implements Serializable{
  private static final long serialVersionUID = 1L;
  private long npCreditEvaluationId;
  private double npGuarantee;
  private String npGuaranteeCurrency;
  
  public void setnpCreditEvaluationId(long npCreditEvaluationId) {
    this.npCreditEvaluationId = npCreditEvaluationId;
  }
  public long getnpCreditEvaluationId() {
    return this.npCreditEvaluationId;
  }
  
  public void setnpGuarantee(double npGuarantee) {
    this.npGuarantee = npGuarantee;
  }
  public double getnpGuarantee() {
    return this.npGuarantee;
  }
  
  public void setnpGuaranteeCurrency(String npGuaranteeCurrency) {
    this.npGuaranteeCurrency = npGuaranteeCurrency;
  }
  public String getnpGuaranteeCurrency() {
    return this.npGuaranteeCurrency;
  }  
}