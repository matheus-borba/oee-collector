package com.connector.dto;

import java.io.Serializable;

import com.connector.model.Factory;

import jakarta.json.bind.annotation.JsonbProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class FactoryDTO implements Serializable {
	
	@JsonbProperty("id")
	private Integer id;
	@JsonbProperty("name")
	private String name;
	@JsonbProperty("location")
	private String location;
	@JsonbProperty("managerName")
	private String managerName;
	@JsonbProperty("contactEmail")
	private String contactEmail;
	@JsonbProperty("phoneNumber")
	private String phoneNumber;
	@JsonbProperty("status")
	private Boolean status;
	
	public Factory toEntity() {
		return Factory.builder()
				.withId(this.id)
				.withName(this.name)
				.withLocation(this.location)
				.withManagerName(this.managerName)
				.withContactEmail(this.contactEmail)
				.withPhoneNumber(this.phoneNumber)
				.withStatus(this.status)
				.build();
	}

	public static FactoryDTO fromEntity(Factory factory) {
		if (factory == null) {
			return null;
		}
		return FactoryDTO.builder()
				.withId(factory.getId())
				.withName(factory.getName())
				.withLocation(factory.getLocation())
				.withManagerName(factory.getManagerName())
				.withContactEmail(factory.getContactEmail())
				.withPhoneNumber(factory.getPhoneNumber())
				.withStatus(factory.getStatus())
				.build();
	}
}