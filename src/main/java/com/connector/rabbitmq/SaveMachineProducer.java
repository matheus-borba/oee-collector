package com.connector.rabbitmq;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;

import com.connector.dto.MachineDTO;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
@ApplicationScoped
public class SaveMachineProducer {

	@Inject
	@Channel("send-machines")
	@OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 10000)
	Emitter<MachineDTO> statusRequestEmitter;

	//Método onde é realizado o envio das maquinas ao RABBITMQ na fila "send-machines"
	public void publish(MachineDTO machineDTO) {
		try {
			this.statusRequestEmitter.send(Message.of(machineDTO));
		} catch (Exception e) {
			log.errorv("Error while sending MachineDTO to RabbitMQ Queue {0}", e.getMessage());
		}
	}
}
