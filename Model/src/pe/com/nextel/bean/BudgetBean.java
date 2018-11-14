package pe.com.nextel.bean;

import java.io.Serializable;

public class BudgetBean implements Serializable{
  private static final long serialVersionUID = 1L;
    private int npBudgetId;
    private int npYear;
    private int npMonth;
    private String npFlagReserved;
    private String npDescription;
    private String npName;
    private String npMonthDescription;
    private long npgeneratorid;
    private String npgeneratortype;
    private double npBudgetAmount;
    private double npConsumedAmount;
    private double residue;
    private double presupuesto;
    private String npdescriptionReason;
    private long npBudgetResultid;
    private long npBudgetReasonId;
    private String npapprovalaction;

  public void setNpBudgetId(int npBudgetId)
  {
    this.npBudgetId = npBudgetId;
  }


  public int getNpBudgetId()
  {
    return npBudgetId;
  }


  public void setNpYear(int npYear)
  {
    this.npYear = npYear;
  }


  public int getNpYear()
  {
    return npYear;
  }


  public void setNpMonth(int npMonth)
  {
    this.npMonth = npMonth;
  }


  public int getNpMonth()
  {
    return npMonth;
  }


  public void setNpFlagReserved(String npFlagReserved)
  {
    this.npFlagReserved = npFlagReserved;
  }


  public String getNpFlagReserved()
  {
    return npFlagReserved;
  }


  public void setNpDescription(String npDescription)
  {
    this.npDescription = npDescription;
  }


  public String getNpDescription()
  {
    return npDescription;
  }


  public void setNpName(String npName)
  {
    this.npName = npName;
  }

  public String getNpName()
  {
    return npName;
  }

  public void setNpMonthDescription(String npMonthDescription)
  {
    this.npMonthDescription = npMonthDescription;
  }


  public String getNpMonthDescription()
  {
    return npMonthDescription;
  }

  public void setNpgeneratortype(String npgeneratortype)
  {
    this.npgeneratortype = npgeneratortype;
  }


  public String getNpgeneratortype()
  {
    return npgeneratortype;
  }


  public void setNpgeneratorid(long npgeneratorid)
  {
    this.npgeneratorid = npgeneratorid;
  }


  public long getNpgeneratorid()
  {
    return npgeneratorid;
  }


  public void setNpBudgetAmount(double npBudgetAmount)
  {
    this.npBudgetAmount = npBudgetAmount;
  }


  public double getNpBudgetAmount()
  {
    return npBudgetAmount;
  }


  public void setNpConsumedAmount(double npConsumedAmount)
  {
    this.npConsumedAmount = npConsumedAmount;
  }


  public double getNpConsumedAmount()
  {
    return npConsumedAmount;
  }


  public void setResidue(double residue)
  {
    this.residue = residue;
  }


  public double getResidue()
  {
    return residue;
  }


  public void setPresupuesto(double presupuesto)
  {
    this.presupuesto = presupuesto;
  }


  public double getPresupuesto()
  {
    return presupuesto;
  }


  public void setNpdescriptionReason(String npdescriptionReason)
  {
    this.npdescriptionReason = npdescriptionReason;
  }


  public String getNpdescriptionReason()
  {
    return npdescriptionReason;
  }


  public void setNpBudgetResultid(long npBudgetResultid)
  {
    this.npBudgetResultid = npBudgetResultid;
  }


  public long getNpBudgetResultid()
  {
    return npBudgetResultid;
  }


  public void setNpBudgetReasonId(long npBudgetReasonId)
  {
    this.npBudgetReasonId = npBudgetReasonId;
  }


  public long getNpBudgetReasonId()
  {
    return npBudgetReasonId;
  }


  public void setNpapprovalaction(String npapprovalaction)
  {
    this.npapprovalaction = npapprovalaction;
  }


  public String getNpapprovalaction()
  {
    return npapprovalaction;
  }

    
}