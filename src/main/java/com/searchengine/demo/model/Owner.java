package com.searchengine.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "owners")
public class Owner {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 80)
	private String firstName;

	@Column(nullable = false, length = 80)
	private String lastName;

	@Column(nullable = false, unique = true, length = 160)
	private String email;

	@Column(length = 40)
	private String phone;

	protected Owner() {
	}

	public Owner(String firstName, String lastName, String email, String phone) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
	}

	public String getFullName() {
		return getFirstName() + " " + getLastName();
	}
}
