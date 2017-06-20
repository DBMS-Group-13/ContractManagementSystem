package model;

public class User {
	private int id;			    //ID
	private String name;		//User name
	private String password;	//Password
	private String secPassword; //secondary password
	private String email;
	private String token;
	private Long activateTime;
	private String createdate;
	private int status;
	private int del;			//Delete status(0-Not deleted, 1-Deleted)
	
	

	public User(){
		this.id = 0;
		this.name = "";
		this.password = "-1";
		this.secPassword="";
		this.email = "";
		this.token = "";
		this.activateTime = Long.parseLong("0");
		this.createdate = "";
		this.status = 0;
		this.del = 0;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getDel() {
		return del;
	}

	public void setDel(int del) {
		this.del = del;
	}

	public String getSecPassword()
	{
		return secPassword;
	}
	
	public void setSecPassword(String secPassword)
	{
		this.secPassword=secPassword;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getEmail()
	{
		return email;
	}
	public void setToken(String token)
	{
		this.token = token;
	}
	public String getToken()
	{
		return token;
	}
	public void setActivateTime(Long activateTime)
	{
		this.activateTime = activateTime;
	}
	public Long getActivateTime()
	{
		return activateTime;
	}
	public void setCreateDate(String createdate)
	{
		this.createdate = createdate;
	}
	public String getCreateDate()
	{
		return createdate;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	public int getStatus()
	{
		return status;
	}
	
}
