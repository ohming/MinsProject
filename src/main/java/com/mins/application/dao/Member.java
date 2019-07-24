package com.mins.application.dao;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "member")
public class Member {

	@Id
	private String cust_id;

	@JsonIgnore
	@Column(nullable = false)
	private String cust_pass;

	@Column(nullable = false)
	private Timestamp regdate;

	public Member() {
		super();
	}

	public Member(String cust_id, String cust_pass, Timestamp regdate) {
		super();
		this.cust_id = cust_id;
		this.cust_pass = cust_pass;
		this.regdate = regdate;
	}


	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	public String getCust_pass() {
		return cust_pass;
	}

	public void setCust_pass(String cust_pass) {
		this.cust_pass = cust_pass;
	}

	public Timestamp getRegdate() {
		return regdate;
	}

	public void setRegdate(Timestamp regdate) {
		this.regdate = regdate;
	}

}
