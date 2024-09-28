package com.connector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.connector.dto.MachineDTO;
import com.connector.dto.ProductionDTO;
import com.connector.model.Production;
import com.connector.repository.IMachineRepository;
import com.connector.repository.IProductionRepository;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
@TestInstance( Lifecycle.PER_CLASS )
public class ProductionRepositoryTest {
		
	@Inject
	IMachineRepository machineRepository;

	@Inject
	IProductionRepository productionRepository;

	@BeforeAll
	void createMachine() {
		MachineDTO machine = MachineDTO.builder()
				.withId(9999)
				.withName("Maquina1")
				.withType("Produção")
				.withPlannedProductionTime(18)
				.withProductionCapacity(8000)
				.withStatus("Operando")
				.withLocation("Endereço")
				.build();
		this.machineRepository.save(machine);
	}

	@Test
	void createProductionSuccess() {
		ProductionDTO productionDTO = ProductionDTO.builder()
				.withId(8888)
				.withMachineId(9999)
				.withProductionTime(17)
				.withItemsProduced(7000)
				.withProductionDate(1727492400000l)
				.withDefectiveItems(100)
				.withShift("Manhã")
				.build();
		
		this.productionRepository.save(productionDTO);
		Optional<Production> production = this.productionRepository.findByProductionId(productionDTO.getId());
		assertEquals(production.isPresent(), true);
	}

	@Test
	void createProductionFailure() {
		ProductionDTO productionDTO = ProductionDTO.builder()
				.withId(null)
				.withMachineId(null)
				.withProductionTime(null)
				.withItemsProduced(null)
				.withProductionDate(null)
				.withDefectiveItems(null)
				.withShift(null)
				.build();
		
		assertThrows( IllegalArgumentException.class, () -> this.productionRepository.save( productionDTO ) );
	}

	@AfterAll
	void deleteMachine() {
		this.productionRepository.deleteProductionById(8888);
		this.machineRepository.deleteByMachineId(9999);
	}
}
