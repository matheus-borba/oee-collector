package com.connector.api;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

import com.connector.dto.ProductionDTO;
import com.connector.model.Production;
import com.connector.repository.IProductionRepository;
import com.connector.restclient.ServerFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/api/production")
@Produces( MediaType.APPLICATION_JSON )
@Consumes( MediaType.APPLICATION_JSON )
@RegisterProvider(ServerFilter.class)
@RolesAllowed("ADMIN")
@SecurityScheme(securitySchemeName = "Basic Auth", type = SecuritySchemeType.HTTP, scheme = "basic")
@SecurityRequirement(name = "Basic Auth")
public class ProductionAPI {
	
	@Inject
	IProductionRepository productionRepository;

	@GET
	public Response getProduction() {
		List<ProductionDTO> productionDTO = fetchAllProductions();
		return buildJsonResponse(productionDTO);
	}

	@GET
	@Path("/{id}")
	public Response getProductionById(@PathParam("id") Integer id) {
		Optional<Production> optProduction = productionRepository.findByProductionId(id);
		if (optProduction.isPresent()) {
			return buildJsonResponse(ProductionDTO.fromEntity(optProduction.get()));
		}
		return Response.status(Status.NOT_FOUND).build();
	}

	@GET
	@Path("/machine/{id}")
	public Response getProductionByMachineId(@PathParam("id") Integer id) {
		List<Production> productions = productionRepository.listByIdMachine(id);
		return buildJsonResponse(productions);
	}

	private List<ProductionDTO> fetchAllProductions() {
	return productionRepository.listAllProductions().stream()
		.map(ProductionDTO::fromEntity)
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
