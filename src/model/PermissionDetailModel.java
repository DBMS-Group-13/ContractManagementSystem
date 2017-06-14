package model;

public class PermissionDetailModel {
	private int userId;
	private boolean systemMge;
	private boolean contractMge;
	private boolean draft;
	private boolean csign;
	private boolean approve;
	private boolean sign;
	
	public  PermissionDetailModel() {
		this.userId=0;
		this.systemMge=false;
		this.contractMge=false;
		this.draft=false;
		this.csign=false;
		this.approve=false;
		this.sign=false;
	}
	
	public int getUserId()
	{
		return this.userId;
	}
	
	public void setUserId(int userId)
	{
		this.userId=userId;
	}
	
	public boolean getSystemMge()
	{
		return this.systemMge;
	}
	
	public void setSystemMge(boolean systemMge)
	{
		this.systemMge=systemMge;
	}
	
	public boolean getContractMge()
	{
		return this.contractMge;
	}
	
	public void setContractMge(boolean contractMge)
	{
		this.contractMge=contractMge;
	}
	
	public boolean getDraft()
	{
		return this.draft;
	}
	
	public void setDraft(boolean draft)
	{
		this.draft=draft;
	}
	
	public boolean getCsign()
	{
		return this.csign;
	}
	
	public void setCsign(boolean csign)
	{
		this.csign=csign;
	}
	
	public boolean getApprove()
	{
		return this.approve;
	}
	
	public void setApprove(boolean approve)
	{
		this.approve=approve;
	}
	
	public boolean getSign()
	{
		return this.sign;
	}
	
	public void setSign(boolean sign)
	{
		this.sign=sign;
	}

}
