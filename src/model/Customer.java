package model;


public class Customer {

	private int id;			    //ID
	private String num;			//Customer number
	private String name;		//Customer name
	private String address;		//Customer address
	private String tel;			//Customer phone
	private String fax;			//Customer fax
	private String code;		//Customer postcode
	private String bank;		//Bank name
	private String account;		//Bank account
	private int del;			//Delete status(0-Not deleted, 1-Deleted)
	
	
	public Customer(){
		this.id = 0;
		this.num = "";
		this.name = "";
		this.address = "";
		this.tel = "";
		this.fax = "";
		this.code = "";
		this.bank = "";
		this.account = "";
		this.del = 0;
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String accout) {
		this.account = accout;
	}

	public int getDel() {
		return del;
	}

	public void setDel(int del) {
		this.del = del;
	}
}
