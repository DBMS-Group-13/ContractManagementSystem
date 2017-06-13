package model;

public class ConDistribute {
	private int id;
	private String conName; 	//Contract name
	private String csign;
	private String approve;
	private String sign;
	
	public ConDistribute()
	{
		this.id=0;
		this.conName="";
		this.csign="";
		this.approve="";
		this.sign="";
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id=id;
	}
	
	public String getConName()
	{
		return conName;
	}
	
	public void setConName(String conName)
	{
		this.conName=conName;
	}
	
	public String getCsign()
	{
		return csign;
	}
	
	public void setCsign(String csign)
	{
		this.csign=csign;
	}
	
	public String getApprove()
	{
		return approve;
	}
	
	public void setApprove(String approve)
	{
		this.approve=approve;
	}
	
	public String getSign()
	{
		return sign;
	}
	
	public void setSign(String sign)
	{
		this.sign=sign;
	}
}
