package com.connector.dto;

import java.io.Serializable;

import com.connector.model.Production;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class ProductionDTO implements Serializable {

	private Integer id;
	private Integer productionTime;
	private Integer itemsProduced;
	private Integer defectiveItems;
	private Long productionDate;
	private String shift;
	private Integer machineId;
	private Double oeePercentage;

	public Production toEntity() {
		return Production.builder()
			.withId(this.id)
			.withProductionTime(this.productionTime)
			.withItemsProduced(this.itemsProduced)
			.withDefectiveItems(this.defectiveItems)
			.withProductionDate(this.productionDate)
			.withShift(this.shift)
			.withOeePercentage(this.oeePercentage)
			.build();
	}

	public static ProductionDTO fromEntity(Production entity) {
		return ProductionDTO.builder()
			.withId(entity.getId())
			.withProductionTime(entity.getProductionTime())
			.withItemsProduced(entity.getItemsProduced())
			.withDefectiveItems(entity.getDefectiveItems())
			.withProductionDate(entity.getProductionDate())
			.withShift(entity.getShift())
			.withMachineId(entity.getMachine().getId())
			.withOeePercentage(entity.getOeePercentage())
			.build();
	}
}
