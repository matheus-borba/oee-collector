package com.connector.rabbitmq;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;

import com.connector.dto.ProductionDTO;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
@ApplicationScoped
public class SaveProductionProducer {

	@Inject
	@Channel("send-productions")
	@OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 10000)
	Emitter<ProductionDTO> statusRequestEmitter;

	//Método onde é realizado o envio das produções ao RABBITMQ pela fila "send-production"
	public void publish(ProductionDTO productionDTO) {
		try {
			this.statusRequestEmitter.send(Message.of(productionDTO));
		} catch (Exception e) {
			log.errorv("Error while sending ProductionDTO to RabbitMQ Queue {0}", e.getMessage());
		}
	}
}
