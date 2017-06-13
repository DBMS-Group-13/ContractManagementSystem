package model;

import java.util.Date;

public class ConDistribute {
	private int id;
	private String conName; 	//Contract name
	private String draft;
	private String csign;
	private String approve;
	private String sign;
	private Date drafTime;	  //Draft time
	
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
	
	public String getDraft()
	{
		return draft;
	}
	
	public void setDraft(String draft)
	{
		this.draft=draft;
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
	
	public Date getDrafTime() {
		return drafTime;
	}

	public void setDrafTime(Date drafTime) {
		this.drafTime = drafTime;
	}
	
}
