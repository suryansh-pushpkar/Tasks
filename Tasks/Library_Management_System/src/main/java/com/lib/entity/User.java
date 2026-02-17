package com.lib.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private String name;

	@Column(unique = true, nullable = false)
	private String mail;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, unique = true)
	private String membershipNo;

	private String role = "USER";

	private boolean status = true;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<IssueRecord> issueHistory;

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

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMembershipNo() {
		return membershipNo;
	}

	public void setMembershipNo(String membershipNo) {
		this.membershipNo = membershipNo;
	}

	public String getRole() {
		return role;
	}

	

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public List<IssueRecord> getIssueHistory() {
		return issueHistory;
	}

	public void setIssueHistory(List<IssueRecord> issueHistory) {
		this.issueHistory = issueHistory;
	}

}