package com.connector.service;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.connector.dto.MachineDTO;
import com.connector.dto.ProductionDTO;
import com.connector.model.Factory;
import com.connector.rabbitmq.SaveMachineProducer;
import com.connector.rabbitmq.SaveProductionProducer;
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
	SaveMachineProducer machineProducer;

	@Inject
	SaveProductionProducer productionProducer;

	public void syncronizeAllMachines(Factory factory) throws Exception {
		try {
			log.info("Starting - Getting Machines");
			String originAction = "GET_MACHINES-" + factory.getId(); 
			List<MachineDTO> machines = this.client.getAllMachines("feebcda0", originAction);
			machines.forEach(machine -> {
				machine.setFactoryId(factory.getId());
				this.machineProducer.publish(machine);
			});
			log.info("Complete - Getting machines");
		} catch (Exception e) {
			log.info(e);
		}
	}

	public void syncronizeAllProduction(Factory factory) throws Exception {
		try {
			log.info("Starting - Getting Prodution");
			String originAction = "GET_PRODUCTIONS-" + factory.getId(); 
			List<ProductionDTO> productions = this.client.getAllProductions("feebcda0", originAction);
			productions.forEach(production -> {
				this.productionProducer.publish(production);;
			});
			log.info("Complete - Getting Prodution");
		} catch (Exception e) {
			log.info(e);
		}
	}
}
