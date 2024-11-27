package com.connector.rabbitmq;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import com.connector.dto.ProductionDTO;
import com.connector.repository.IProductionRepository;
import com.connector.utils.MessageFunctions;

import io.smallrye.reactive.messaging.annotations.Blocking;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
@ApplicationScoped
public class SaveProductionConsumer {

	@Inject
	IProductionRepository productionRepository;

	@Incoming("receive-productions")
	@Blocking
	public CompletionStage<Void> processMessage(Message<JsonObject> message) {
		try {
			ProductionDTO productionDTO = MessageFunctions.messageToObject(message, ProductionDTO.class);
			CompletableFuture.runAsync(() -> {
				try {
					this.productionRepository.save(productionDTO);
				} catch (Exception e) {
					log.errorv("Error while saving production in receive-productions: {0}", e.getMessage());
				}
			});
		} catch (Exception e) {
			log.errorv("Error while processing production in receive-productions: {0}", e.getMessage());
			return message.nack(e);
		}
		return message.ack();
	}
}
