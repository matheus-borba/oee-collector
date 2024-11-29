package com.connector.rabbitmq;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import com.connector.dto.MachineDTO;
import com.connector.repository.IMachineRepository;
import com.connector.utils.MessageFunctions;

import io.smallrye.reactive.messaging.annotations.Blocking;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
@ApplicationScoped
public class SaveMachineConsumer {

	@Inject
	IMachineRepository machineRepository;
	// Aqui é onde os dados das maquinas são recebidos do rabbitMQ e tratados de forma assíncrona
	@Incoming("receive-machines")
	@Blocking
	public CompletionStage<Void> processMessage(Message<JsonObject> message) {
		try {
			MachineDTO machineDto = MessageFunctions.messageToObject(message, MachineDTO.class);
			CompletableFuture.runAsync(() -> {
				try {
					this.machineRepository.save(machineDto);
				} catch (Exception e) {
					log.errorv("Error while saving machine in receive-machines: {0}", e.getMessage());
				}
			});
		} catch (Exception e) {
			log.errorv("Error while processing machine in receive-machines: {0}", e.getMessage());
			return message.nack(e);
		}
		return message.ack();
	}
}
