package com.connector.service;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.connector.dto.MachineDTO;
import com.connector.dto.ProductionDTO;
import com.connector.model.Factory;
import com.connector.repository.IMachineRepository;
import com.connector.repository.IProductionRepository;
import com.connector.restclient.IServiceClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
@ApplicationScoped
public class ApiService {

	@Inject
	@RestClient
	IServiceClient client;

	@Inject
	IMachineRepository machineRepository;

	@Inject
	IProductionRepository productionRepository;

	public void syncronizeAllMachines(Factory factory) throws Exception {
		try {
			log.info("Starting - Getting Machines");
			List<MachineDTO> machines = this.client.getAllMachines("feebcda0");
			machines.forEach(machine -> {
				machine.setFactoryId(factory.getId());
				this.machineRepository.save(machine);
			});
			log.info("Complete - Getting machines");
		} catch (Exception e) {
			log.info(e);
		}
	}

	public void syncronizeAllProduction() throws Exception {
		try {
			log.info("Starting - Getting Prodution");
			List<ProductionDTO> productions = this.client.getAllProductions("feebcda0");
			productions.forEach(production -> {
				this.productionRepository.save(production);
			});
			log.info("Complete - Getting Prodution");
		} catch (Exception e) {
			log.info(e);
		}
	}
}
