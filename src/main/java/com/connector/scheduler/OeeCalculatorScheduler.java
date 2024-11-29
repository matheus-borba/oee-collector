package com.connector.scheduler;

import com.connector.repository.IMachineRepository;
import com.connector.repository.IProductionRepository;
import com.connector.service.OEECalculationService;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.Scheduled.ConcurrentExecution;
import jakarta.inject.Inject;
import lombok.extern.jbosslog.JBossLog;

//Timers que buscam as produções e maquinas para calcular sua OEE
@JBossLog
public class OeeCalculatorScheduler {

	@Inject
	OEECalculationService oeeCalculationService;

	@Inject
	IProductionRepository productionRepository;

	@Inject
	IMachineRepository machineRepository;

	@Scheduled(
		concurrentExecution = ConcurrentExecution.SKIP,
		every = "24h",
		delayed = "10m",
		identity = "calculate-oee-for-productions"
	)
	public void calculateOEEforProduction() {
		log.info("Starting - Getting Machines from the Production System");
		this.productionRepository.listProductionWithoutOEE().forEach(production -> {
			try {
				oeeCalculationService.calculateOEE(production);
			} catch (Exception e) {
				log.error(e);
			}
		});
	}

	@Scheduled(
		concurrentExecution = ConcurrentExecution.SKIP,
		every = "24h",
		delayed = "15m",
		identity = "syncronize-oee-for-machines"
	)
	public void calculateOEEforMachine() {
		this.machineRepository.listAllMachines().forEach(machine -> {
			try {
				oeeCalculationService.calculateOEEMachine(machine);
			} catch (Exception e) {
				log.error(e);
			}
		});
	}
}
