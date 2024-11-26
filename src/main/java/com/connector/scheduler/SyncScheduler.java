package com.connector.scheduler;

import com.connector.repository.IFactoryRepository;
import com.connector.service.ApiService;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.Scheduled.ConcurrentExecution;
import jakarta.inject.Inject;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
public class SyncScheduler {

	@Inject
	ApiService service;

	@Inject
	IFactoryRepository factoryRepository;

	@Scheduled(
		concurrentExecution = ConcurrentExecution.SKIP,
		every = "24h",
		identity = "sync-all-machines"
	)
	public void syncronizeAllMachines() {
		log.info("Starting - Getting Machines from the Production System");
		this.factoryRepository.listAllActiveFactories().forEach( factory -> {
			try {
				this.service.syncronizeAllMachines(factory);
				log.infov( "Complete - Getting Machines from the Factory {0}", factory.getId());
			} catch (Exception e) {
				log.errorv("Error while getting Machines from the Factory {0}", factory.getId());
			}
		});
	}

	@Scheduled(
		concurrentExecution = ConcurrentExecution.SKIP,
		every = "24h",
		delayed = "5m",
		identity = "sync-all-productions"
	)
	public void syncronizeAllProductions() {
		log.info( "Starting - Getting Machines from the Production System");
		try {
			this.service.syncronizeAllProduction();
			log.info( "Complete - Getting Machines from the Production System");
		} catch (Exception e) {
			log.errorv("Error while getting Machines from de Production System");
		}
	}
}
