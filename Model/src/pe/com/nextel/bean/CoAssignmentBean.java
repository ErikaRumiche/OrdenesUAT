package pe.com.nextel.bean;

import java.io.Serializable;

import java.util.ArrayList;

import pe.com.nextel.util.GenericObject;


public class CoAssignmentBean extends GenericObject implements Serializable{
  private static final long serialVersionUID = 1L;
  private long   npcoassignmentid;
  private long   nporderid;
	private String npphone;
	private String npnewsiteid;
	private String npbscscontractId;
	private String npbscssncode;
	private String npbscspaymntrespcustomeridId;
  private String npbscspaymntrespcustname;
	private String npbscsbillingAccountId;
	private String npbillaccnewid;
  private String nporigsitedesc;
  private String npcustcode;
  private String nptypesite;
  private ArrayList npBillingBySite;

  public String getNpbillaccnewid() {
    return this.npbillaccnewid;
  }
  
  public String getNpbscsbillingAccountId() {
    return this.npbscsbillingAccountId;
  }
  
  public String getNpbscscontractId() {
    return this.npbscscontractId;
  }
  
  public String getNpbscspaymntrespcustomeridId() {
    return this.npbscspaymntrespcustomeridId;
  }
  
  public String getNpbscssncode() {
    return this.npbscssncode;
  }
  
  public long getNpcoassignmentid() {
    return this.npcoassignmentid;
  }
  
  public String getNpnewsiteid() {
    return this.npnewsiteid;
  }
  
  public long getNporderid() {
    return this.nporderid;
  }
  
  public String getNpphone() {
    return this.npphone;
  }
  
  public void setNpbillaccnewid(String npbillaccnewid) {
    this.npbillaccnewid = npbillaccnewid;
  }
  
  public void setNpbscsbillingAccountId(String npbscsbillingAccountId) {
    this.npbscsbillingAccountId = npbscsbillingAccountId;
  }
  
  public void setNpbscscontractId(String npbscscontractId) {
    this.npbscscontractId = npbscscontractId;
  }
  
  public void setNpbscspaymntrespcustomeridId(String npbscspaymntrespcustomeridId) {
    this.npbscspaymntrespcustomeridId = npbscspaymntrespcustomeridId;
  }
  
  public void setNpbscssncode(String npbscssncode) {
    this.npbscssncode = npbscssncode;
  }
  
  public void setNpcoassignmentid(long npcoassignmentid) {
    this.npcoassignmentid = npcoassignmentid;
  }
  
  public void setNpnewsiteid(String npnewsiteid) {
    this.npnewsiteid = npnewsiteid;
  }
  
  public void setNporderid(long nporderid) {
    this.nporderid = nporderid;
  }
  
  public void setNpphone(String npphone) {
    this.npphone = npphone;
  }


  public void setNporigsitedesc(String nporigsitedesc)
  {
    this.nporigsitedesc = nporigsitedesc;
  }


  public String getNporigsitedesc()
  {
    return nporigsitedesc;
  }


  public void setNpcustcode(String npcustcode)
  {
    this.npcustcode = npcustcode;
  }


  public String getNpcustcode()
  {
    return npcustcode;
  }


  public void setNpbscspaymntrespcustname(String npbscspaymntrespcustname)
  {
    this.npbscspaymntrespcustname = npbscspaymntrespcustname;
  }


  public String getNpbscspaymntrespcustname()
  {
    return npbscspaymntrespcustname;
  }


  public void setNpBillingBySite(ArrayList npBillingBySite)
  {
    this.npBillingBySite = npBillingBySite;
  }


  public ArrayList getNpBillingBySite()
  {
    return npBillingBySite;
  }


  public void setNptypesite(String nptypesite)
  {
    this.nptypesite = nptypesite;
  }


  public String getNptypesite()
  {
    return nptypesite;
  }
}
