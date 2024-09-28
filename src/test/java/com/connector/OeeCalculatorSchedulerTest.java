package com.connector;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.connector.model.Machine;
import com.connector.model.Production;
import com.connector.repository.IMachineRepository;
import com.connector.repository.IProductionRepository;
import com.connector.scheduler.OeeCalculatorScheduler;
import com.connector.service.OEECalculationService;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class OeeCalculatorSchedulerTest {

	@InjectMocks
	private OeeCalculatorScheduler oeeCalculatorScheduler;

	@Mock
	private OEECalculationService oeeCalculationService;

	@Mock
	private IProductionRepository productionRepository;

	@Mock
	private IMachineRepository machineRepository;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testCalculateOEEforProduction() throws Exception {
		var production1 = new Production();
		var production2 = new Production();  
		when(productionRepository.listProductionWithoutOEE()).thenReturn(Arrays.asList(production1, production2));

		oeeCalculatorScheduler.calculateOEEforProduction();

		verify(oeeCalculationService, atLeastOnce()).calculateOEE(production1);
		verify(oeeCalculationService, atLeastOnce()).calculateOEE(production2);
	}

	@Test
	public void testCalculateOEEforMachine() throws Exception {
		var machine1 = new Machine();
		var machine2 = new Machine();
		when(machineRepository.listAllMachines()).thenReturn(Arrays.asList(machine1, machine2));

		oeeCalculatorScheduler.calculateOEEforMachine();

		verify(oeeCalculationService, atLeastOnce()).calculateOEEMachine(machine1);
		verify(oeeCalculationService, atLeastOnce()).calculateOEEMachine(machine2);
	}

	@Test
	public void testCalculateOEEforProductionFailure() throws Exception {
		var production1 = new Production();
		var production2 = new Production();
		List<Production> productions = Arrays.asList(production1, production2);
		
		when(productionRepository.listProductionWithoutOEE()).thenReturn(productions);

		doThrow(new RuntimeException("Erro ao calcular OEE")).when(oeeCalculationService).calculateOEE(production1);

		oeeCalculatorScheduler.calculateOEEforProduction();

		verify(oeeCalculationService, atLeastOnce()).calculateOEE(production1);
		verify(oeeCalculationService, atLeastOnce()).calculateOEE(production2);
	}

	@Test
	public void testCalculateOEEforMachineFailure() throws Exception {
		var machine1 = new Machine();
		var machine2 = new Machine();
		List<Machine> machines = Arrays.asList(machine1, machine2);

		when(machineRepository.listAllMachines()).thenReturn(machines);

		doThrow(new RuntimeException("Erro ao calcular OEE para máquina")).when(oeeCalculationService).calculateOEEMachine(machine1);

		oeeCalculatorScheduler.calculateOEEforMachine();

		verify(oeeCalculationService, atLeastOnce()).calculateOEEMachine(machine1);
		verify(oeeCalculationService, atLeastOnce()).calculateOEEMachine(machine2);
	}

}
