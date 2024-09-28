package com.connector.api;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.connector.dto.MachineDTO;
import com.connector.model.Machine;
import com.connector.repository.IMachineRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/api/machine")
@Produces( MediaType.APPLICATION_JSON )
@Consumes( MediaType.APPLICATION_JSON )
public class MachineAPI {

	@Inject
	IMachineRepository machineRepository;

	@GET
	public Response getMachines() {
		List<MachineDTO> machineDTOs = fetchAllMachines();
		return buildJsonResponse(machineDTOs);
	}

	@GET
	@Path("/{id}")
	public Response getMachineById(@PathParam("id") Integer id) {
		Optional<Machine> optMachine = machineRepository.findByMachineId(id);
		if (optMachine.isPresent()) {
			return buildJsonResponse(MachineDTO.fromEntity(optMachine.get()));
		}
		return Response.status(Status.NOT_FOUND).build();
	}

	private List<MachineDTO> fetchAllMachines() {
		return machineRepository.listAllMachines().stream()
			.map(MachineDTO::fromEntity)
			.collect(Collectors.toList());
	}

	private Response buildJsonResponse(Object data) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(data);
			return Response.ok(json).build();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Erro ao processar JSON").build();
		}
	}
}
