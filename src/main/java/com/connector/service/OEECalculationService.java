package com.connector.service;

import java.util.List;

import com.connector.dto.MachineDTO;
import com.connector.dto.ProductionDTO;
import com.connector.model.Machine;
import com.connector.model.Production;
import com.connector.repository.IMachineRepository;
import com.connector.repository.IProductionRepository;
import com.connector.utils.OEECalculator;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
@ApplicationScoped
public class OEECalculationService {

	private final IProductionRepository productionRepository;
	private final IMachineRepository machineRepository;
	private final OEECalculator oeeCalculator;

	@Inject
	public OEECalculationService(IProductionRepository productionRepository, IMachineRepository machineRepository, OEECalculator oeeCalculator) {
		this.productionRepository = productionRepository;
		this.machineRepository = machineRepository;
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
		productionRepository.save(dto);
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
		machineRepository.save(dto);
	}
}