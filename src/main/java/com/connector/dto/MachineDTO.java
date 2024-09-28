package com.connector.dto;

import java.io.Serializable;

import com.connector.model.Machine;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class MachineDTO implements Serializable {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("type")
	private String type;

	@JsonProperty("productionCapacity")
	private Integer productionCapacity;

	@JsonProperty("plannedProductionTime")
	private Integer plannedProductionTime;

	@JsonProperty("location")
	private String location;

	@JsonProperty("status")
	private String status;

	@JsonProperty("oeePercentage")
	private Double oeePercentage;

	public Machine toEntity() {
		return Machine.builder()
			.withId(this.id)
			.withName(this.name)
			.withType(this.type)
			.withProductionCapacity(this.productionCapacity)
			.withPlannedProductionTime(this.plannedProductionTime)
			.withLocation(this.location)
			.withStatus(this.status)
			.withOeePercentage(this.oeePercentage)
			.build();
	}

	public static MachineDTO fromEntity(Machine entity) {
		return MachineDTO.builder()
			.withId(entity.getId())
			.withName(entity.getName())
			.withType(entity.getType())
			.withProductionCapacity(entity.getProductionCapacity())
			.withPlannedProductionTime(entity.getPlannedProductionTime())
			.withLocation(entity.getLocation())
			.withStatus(entity.getStatus())
			.withOeePercentage(entity.getOeePercentage())
			.build();
	}
}
