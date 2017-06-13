package model;

public class User {
	private int id;			    //ID
	private String name;		//User name
	private String password;	//Password
	private String secPassword; //secondary password
	private String problem1;
	private String answer1;
	private String problem2;
	private String answer2;
	private int del;			//Delete status(0-Not deleted, 1-Deleted)
	
	
	/**
	 * No-arg constructor assigns initial values to object attributes
	 */
	public User(){
		this.id = 0;
		this.name = "";
		this.password = "";
		this.secPassword="";
		this.problem1="";
		this.answer1="";
		this.problem2="";
		this.answer2="";
		this.del = 0;
		
	}

	/*
	 *  Provide setter and getter methods for attributes
	 *  setter is used for setting the attribute's value, getter is used for getting the attribute's value
	 */
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

	public void setProblem1(String problem)
	{
		
	}
	
	public void setProblem2(String problem)
	{
		
	}
	
	public String getProblem1()
	{
		return problem1;
	}
	
	public String getProblem2()
	{
		return problem2;
	}
	
	public String getAnswer1()
	{
		return answer1;
	}
	
	public String getAnswer2()
	{
		return answer2;
	}
}
