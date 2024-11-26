package com.connector.dto;

import java.io.Serializable;

import com.connector.model.Machine;

import jakarta.json.bind.annotation.JsonbProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class MachineDTO implements Serializable {

	@JsonbProperty("id")
	private Integer id;
	@JsonbProperty("name")
	private String name;
	@JsonbProperty("type")
	private String type;
	@JsonbProperty("productionCapacity")
	private Integer productionCapacity;
	@JsonbProperty("plannedProductionTime")
	private Integer plannedProductionTime;
	@JsonbProperty("location")
	private String location;
	@JsonbProperty("status")
	private String status;
	@JsonbProperty("oeePercentage")
	private Double oeePercentage;
	@JsonbProperty("factoryId")
	private Integer factoryId;

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
			.withFactoryId(entity.getFactory().getId())
			.build();
	}
}
