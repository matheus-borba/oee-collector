package com.connector.model;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "machine")
public class Machine implements Serializable {

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name= "type")
	private String type;

	@Column(name = "production_capacity")
	private Integer productionCapacity;

	@Column(name = "planned_production_time")
	private Integer plannedProductionTime;
	
	@Column(name = "location", columnDefinition = "text")
	private String location;

	@Column(name = "status")
	private String status;
	
	@Column(name = "oee_percentage")
	private Double oeePercentage;

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name="factory_id", referencedColumnName = "id")
	private Factory factory;
}
