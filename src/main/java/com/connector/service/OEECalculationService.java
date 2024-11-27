package com.connector.service;

import java.util.List;

import com.connector.dto.MachineDTO;
import com.connector.dto.ProductionDTO;
import com.connector.model.Machine;
import com.connector.model.Production;
import com.connector.rabbitmq.SaveMachineProducer;
import com.connector.rabbitmq.SaveProductionProducer;
import com.connector.repository.IProductionRepository;
import com.connector.utils.OEECalculator;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
@ApplicationScoped
public class OEECalculationService {

	private final IProductionRepository productionRepository;
	private final SaveProductionProducer productionProducer;
	private final SaveMachineProducer machineProducer;
	private final OEECalculator oeeCalculator;

	@Inject
	public OEECalculationService(
			IProductionRepository productionRepository,
			SaveProductionProducer productionProducer,
			SaveMachineProducer machineProducer,
			OEECalculator oeeCalculator) {
		this.productionRepository = productionRepository;
		this.productionProducer = productionProducer;
		this.machineProducer = machineProducer;
		this.oeeCalculator = oeeCalculator;
	}

	public void calculateOEE(Production production) throws Exception {
		Machine machine = production.getMachine();
		
		double availability = oeeCalculator.calculateAvailability(production.getProductionTime(), machine.getPlannedProductionTime());
		double performance = oeeCalculator.calculatePerformance(production.getItemsProduced(), machine.getProductionCapacity());
		double quality = oeeCalculator.calculateQuality(production.getItemsProduced(), production.getDefectiveItems());

		double oee = oeeCalculator.calculateOEE(availability, performance, quality);
		double oeeFinal = oeeCalculator.roundToTwoDecimalPlaces(oee);

		ProductionDTO dto = ProductionDTO.fromEntity(production);
		dto.setOeePercentage(oeeFinal);
		productionProducer.publish(dto);
	}

	public void calculateOEEMachine(Machine machine) throws Exception {
		List<Production> productions = productionRepository.findByMachine(machine);
		if (productions.isEmpty()) {
			log.infov("There's no production for machine id {0}", machine.getId());
			return;
		}

		double totalOee = productions.stream()
			.mapToDouble(Production::getOeePercentage)
			.sum();
		double averageOee = totalOee / productions.size();
		double averageOeeFinal = oeeCalculator.roundToTwoDecimalPlaces(averageOee);

		MachineDTO dto = MachineDTO.fromEntity(machine);
		dto.setOeePercentage(averageOeeFinal);
		machineProducer.publish(dto);
	}
}