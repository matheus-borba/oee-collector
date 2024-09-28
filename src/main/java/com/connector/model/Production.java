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
@Table(name = "production")
public class Production implements Serializable {

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "production_time")
	private Integer productionTime;

	@Column(name = "items_produced")
	private Integer itemsProduced;

	@Column(name = "defective_items")
	private Integer defectiveItems;

	@Column(name = "production_date")
	private Long productionDate;

	@Column(name = "shift")
	private String shift;

	@Column(name = "oee_percentage")
	private Double oeePercentage;

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name="machine_id", referencedColumnName = "id")
	private Machine machine;
}
