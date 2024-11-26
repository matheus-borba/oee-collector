package com.connector.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
@Entity
@Table(name = "factory")
public class Factory implements Serializable {
	
	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "location", columnDefinition = "text")
	private String location;

	@Column(name = "manager_name")
	private String managerName;

	@Column(name = "contact_email")
	private String contactEmail;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "status")
	private Boolean status;
}
