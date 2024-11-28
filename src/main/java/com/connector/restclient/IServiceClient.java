package com.connector.restclient;

import java.util.List;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.annotation.RegisterProviders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.connector.dto.MachineDTO;
import com.connector.dto.ProductionDTO;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(configKey = "sensordata")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RegisterProviders({
		@RegisterProvider(ClientFilter.class)
})
public interface IServiceClient {

	static final String HEADER_ORIGIN_ACTION = "origin-action";

	@GET
	@Path("/machine.json")
	List<MachineDTO> getAllMachines(
			@QueryParam("key") String apiKey,
			@HeaderParam(HEADER_ORIGIN_ACTION) String originAction
	);

	@GET
	@Path("/production.json")
	List<ProductionDTO> getAllProductions(
			@QueryParam("key") String apiKey,
			@HeaderParam(HEADER_ORIGIN_ACTION) String originAction
	);
}