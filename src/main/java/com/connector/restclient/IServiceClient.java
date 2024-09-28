package com.connector.restclient;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.connector.dto.MachineDTO;
import com.connector.dto.ProductionDTO;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(configKey = "sensordata")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface IServiceClient {

	@GET
	@Path("/machine.json")
	List<MachineDTO> getAllMachines(
			@QueryParam("key") String apiKey
	);

	@GET
	@Path("/production.json")
	List<ProductionDTO> getAllProductions(
			@QueryParam("key") String apiKey
	);
}