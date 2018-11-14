package pe.com.nextel.bean;

import java.io.Serializable;

public class AddressObjectBean  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private long swobjectid;
      private String swobjecttype;
      private long swregionid;
      private String swregionname;
      private String swaddress1;
      private String swaddress2;
      private String swaddress3;
      private String swaddress4;
      private String swcountry;
      private String swstate;
      private String swprovince;
      private String swcity;
      private int npdepartamentoid;
      private int npprovinciaid;
      private int npdistritoid;
      private String swzip;             
      private String swnote;
      private int flagAddress; //NBRAVO
      private String swcreatedby; 
      private long addressId;
      private String swdepid; 
      private long  npnumregisters;
      private long  npnumpage;
      
      private String npdistrictname;
      private long   npdistrictid;
      
      private String npdepartmentname;
      private long   npdepartmentid;
      
      private String npprovincename;
      private long   npprovinceid;
      
      
    public void setSwdepid(String swdepid)
  {
    this.swdepid = swdepid;
  }


  public String getSwdepid()
  {
    return swdepid;
  }

  public void setSwobjectid(long swobjectid)
  {
    this.swobjectid = swobjectid;
  }


  public long getSwobjectid()
  {
    return swobjectid;
  }


  public void setSwobjecttype(String swobjecttype)
  {
    this.swobjecttype = swobjecttype;
  }


  public String getSwobjecttype()
  {
    return swobjecttype;
  }


  public void setSwregionid(long swregionid)
  {
    this.swregionid = swregionid;
  }


  public long getSwregionid()
  {
    return swregionid;
  }


  public void setSwaddress1(String swaddress1)
  {
    this.swaddress1 = swaddress1;
  }


  public String getSwaddress1()
  {
    return swaddress1;
  }


  public void setSwaddress2(String swaddress2)
  {
    this.swaddress2 = swaddress2;
  }


  public String getSwaddress2()
  {
    return swaddress2;
  }


  public void setSwaddress3(String swaddress3)
  {
    this.swaddress3 = swaddress3;
  }


  public String getSwaddress3()
  {
    return swaddress3;
  }


  public void setSwcountry(String swcountry)
  {
    this.swcountry = swcountry;
  }


  public String getSwcountry()
  {
    return swcountry;
  }


  public void setSwstate(String swstate)
  {
    this.swstate = swstate;
  }


  public String getSwstate()
  {
    return swstate;
  }


  public void setSwprovince(String swprovince)
  {
    this.swprovince = swprovince;
  }


  public String getSwprovince()
  {
    return swprovince;
  }


  public void setSwcity(String swcity)
  {
    this.swcity = swcity;
  }


  public String getSwcity()
  {
    return swcity;
  }

  public void setSwzip(String swzip)
  {
    this.swzip = swzip;
  }


  public String getSwzip()
  {
    return swzip;
  }


  public void setSwnote(String swnote)
  {
    this.swnote = swnote;
  }


  public String getSwnote()
  {
    return swnote;
  }


  public void setSwcreatedby(String swcreatedby)
  {
    this.swcreatedby = swcreatedby;
  }


  public String getSwcreatedby()
  {
    return swcreatedby;
  }


  public void setAddressId(long addressId)
  {
    this.addressId = addressId;
  }


  public long getAddressId()
  {
    return addressId;
  }


  public void setNpdepartamentoid(int npdepartamentoid)
  {
    this.npdepartamentoid = npdepartamentoid;
  }


  public int getNpdepartamentoid()
  {
    return npdepartamentoid;
  }


  public void setNpprovinciaid(int npprovinciaid)
  {
    this.npprovinciaid = npprovinciaid;
  }


  public int getNpprovinciaid()
  {
    return npprovinciaid;
  }


  public void setNpdistritoid(int npdistritoid)
  {
    this.npdistritoid = npdistritoid;
  }


  public int getNpdistritoid()
  {
    return npdistritoid;
  }


   public void setSwaddress4(String swaddress4)
   {
      this.swaddress4 = swaddress4;
   }


   public String getSwaddress4()
   {
      return swaddress4;
   }


   public void setSwregionname(String swregionname)
   {
      this.swregionname = swregionname;
   }


   public String getSwregionname()
   {
      return swregionname;
   }


  public void setNpnumregisters(long npnumregisters)
  {
    this.npnumregisters = npnumregisters;
  }


  public long getNpnumregisters()
  {
    return npnumregisters;
  }


  public void setNpnumpage(long npnumpage)
  {
    this.npnumpage = npnumpage;
  }


  public long getNpnumpage()
  {
    return npnumpage;
  }


  public void setNpdistrictname(String npdistrictname)
  {
    this.npdistrictname = npdistrictname;
  }


  public String getNpdistrictname()
  {
    return npdistrictname;
  }


  public void setNpdistrictid(long npdistrictid)
  {
    this.npdistrictid = npdistrictid;
  }


  public long getNpdistrictid()
  {
    return npdistrictid;
  }


  public void setNpdepartmentname(String npdepartmentname)
  {
    this.npdepartmentname = npdepartmentname;
  }


  public String getNpdepartmentname()
  {
    return npdepartmentname;
  }


  public void setNpdepartmentid(long npdepartmentid)
  {
    this.npdepartmentid = npdepartmentid;
  }


  public long getNpdepartmentid()
  {
    return npdepartmentid;
  }


  public void setNpprovincename(String npprovincename)
  {
    this.npprovincename = npprovincename;
  }


  public String getNpprovincename()
  {
    return npprovincename;
  }


  public void setNpprovinceid(long npprovinceid)
  {
    this.npprovinceid = npprovinceid;
  }


  public long getNpprovinceid()
  {
    return npprovinceid;
  }


  public void setFlagAddress(int flagAddress)
  {
    this.flagAddress = flagAddress;
  }


  public int getFlagAddress()
  {
    return flagAddress;
  }


}