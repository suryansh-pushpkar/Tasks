package com.lib.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins")
public class Admin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false)
	private String name;

	@Column(unique = true ,nullable = false)
	private String mail;

	@Column(nullable = false)
	private String password;

	@Column(unique = true, nullable = false)
	private String membershipNo;

	@Column(length = 125, nullable = false)
	private String address;

	@OneToOne(mappedBy = "owner")
	private Library library;
	@Column(nullable = false)

	private String role = "ADMIN";

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Library getLibrary() {
		return library;
	}

	public void setLibrary(Library library) {
		this.library = library;
	}

}