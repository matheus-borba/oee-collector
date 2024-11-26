package com.connector.scheduler;

import java.util.List;

import com.connector.model.Factory;
import com.connector.redis.GetCacheFactory;
import com.connector.redis.SetCacheFactory;
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

	@Inject
	GetCacheFactory getCacheFactory;

	@Inject
	SetCacheFactory setCacheFactory;

	@Scheduled(
		concurrentExecution = ConcurrentExecution.SKIP,
		every = "24h",
		identity = "sync-all-machines"
	)
	public void syncronizeAllMachines() {
		log.info("Starting - Getting Machines from the Production System");
		List<Factory> factories = this.getCacheFactory.execute(null);
		if(factories.isEmpty()) {
			log.info( "Complete - There're any factory to sync");
			factories = this.factoryRepository.listAllActiveFactories();
			this.setCacheFactory.execute(factories);
		}
		factories.forEach( factory -> {
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
