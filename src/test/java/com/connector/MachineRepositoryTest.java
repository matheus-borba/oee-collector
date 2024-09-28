package com.connector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.connector.dto.MachineDTO;
import com.connector.model.Machine;
import com.connector.repository.IMachineRepository;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
@TestInstance( Lifecycle.PER_CLASS )
public class MachineRepositoryTest {
	
	@Inject
	IMachineRepository repository;

	@Test
	void createMachineSuccess() {
		MachineDTO dto = MachineDTO.builder()
				.withId(9999)
				.withName("Maquina1")
				.withType("Produção")
				.withPlannedProductionTime(18)
				.withProductionCapacity(8000)
				.withStatus("Operando")
				.withLocation("Endereço")
				.build();
		this.repository.save(dto);
		Optional<Machine> machine = this.repository.findByMachineId(dto.getId());
		assertEquals(machine.isPresent(), true);
	}

	@Test
	void createMachineFailure() {
		MachineDTO dto = MachineDTO.builder()
				.withId(null)
				.withName(null)
				.withType(null)
				.withPlannedProductionTime(null)
				.withProductionCapacity(null)
				.withStatus(null)
				.withLocation(null)
				.build();
		
		assertThrows( IllegalArgumentException.class, () -> this.repository.save( dto ) );
	}

	@AfterAll
	void deleteMachine() {
		this.repository.deleteByMachineId(9999);
	}
}
