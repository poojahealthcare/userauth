package com.hca.oauth.model;

public class ResetPassword {
	
	private String email;
	private String cpwd;
	private String npwd;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCpwd() {
		return cpwd;
	}
	public void setCpwd(String cpwd) {
		this.cpwd = cpwd;
	}
	public String getNpwd() {
		return npwd;
	}
	public void setNpwd(String npwd) {
		this.npwd = npwd;
	}
	
	public String toString() {
		return "email = "+email+" cpwd="+cpwd+" npwd="+npwd;
	}
	

}
