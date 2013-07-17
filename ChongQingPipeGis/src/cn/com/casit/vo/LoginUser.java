package cn.com.casit.vo;

public class LoginUser {
	
	String name;
	private String confirmPass;
	private boolean unconfirmed;
	private boolean inblacklist;
	String loginname;
	String loginpassword;
	
	private int id;
	
	private String phone;
	private String email;
	private RoleBean role;
	private int department;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getLoginpassword() {
		return loginpassword;
	}
	public void setLoginpassword(String loginpassword) {
		this.loginpassword = loginpassword;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getConfirmPass() {
		return confirmPass;
	}
	public void setConfirmPass(String confirmPass) {
		this.confirmPass = confirmPass;
	}
	public boolean isUnconfirmed() {
		return unconfirmed;
	}
	public void setUnconfirmed(boolean unconfirmed) {
		this.unconfirmed = unconfirmed;
	}
	public boolean isInblacklist() {
		return inblacklist;
	}
	public void setInblacklist(boolean inblacklist) {
		this.inblacklist = inblacklist;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public RoleBean getRole() {
		return role;
	}
	public void setRole(RoleBean role) {
		this.role = role;
	}
	public int getDepartment() {
		return department;
	}
	public void setDepartment(int department) {
		this.department = department;
	}


}
