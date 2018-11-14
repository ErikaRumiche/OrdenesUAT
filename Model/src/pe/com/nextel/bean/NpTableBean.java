package pe.com.nextel.bean;

import java.io.Serializable;

public class NpTableBean implements Serializable{
  private static final long serialVersionUID = 1L;
	private String nptable;
	private String npvalue;
	private String npvaluedesc;
	private String npstatus;
	private String nptag1;
	private String nptag2;
	private int    nporder;
	
	public NpTableBean()
	{
	}


	public void setNptable(String nptable)
	{
		this.nptable = nptable;
	}


	public String getNptable()
	{
		return nptable;
	}


	public void setNpvalue(String npvalue)
	{
		this.npvalue = npvalue;
	}


	public String getNpvalue()
	{
		return npvalue;
	}


	public void setNpvaluedesc(String npvaluedesc)
	{
		this.npvaluedesc = npvaluedesc;
	}


	public String getNpvaluedesc()
	{
		return npvaluedesc;
	}


	public void setNpstatus(String npstatus)
	{
		this.npstatus = npstatus;
	}


	public String getNpstatus()
	{
		return npstatus;
	}


	public void setNptag1(String nptag1)
	{
		this.nptag1 = nptag1;
	}


	public String getNptag1()
	{
		return nptag1;
	}


	public void setNptag2(String nptag2)
	{
		this.nptag2 = nptag2;
	}


	public String getNptag2()
	{
		return nptag2;
	}


	public void setNporder(int nporder)
	{
		this.nporder = nporder;
	}


	public int getNporder()
	{
		return nporder;
	}
}