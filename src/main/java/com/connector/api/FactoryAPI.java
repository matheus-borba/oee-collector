package com.connector.api;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

import com.connector.dto.FactoryDTO;
import com.connector.model.Factory;
import com.connector.repository.IFactoryRepository;
import com.connector.restclient.ServerFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
@Path("/api/factory")
@Produces( MediaType.APPLICATION_JSON )
@Consumes( MediaType.APPLICATION_JSON )
@RegisterProvider(ServerFilter.class)
@RolesAllowed("ADMIN")
@SecurityScheme(securitySchemeName = "Basic Auth", type = SecuritySchemeType.HTTP, scheme = "basic")
@SecurityRequirement(name = "Basic Auth")
public class FactoryAPI {

	@Inject
	IFactoryRepository factoryRepository;

	@POST
	public Response createFactory(FactoryDTO factory) {
		try {
			factoryRepository.save(factory);
			return Response.status(Status.CREATED).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST)
					.entity("Error saving factory: " + e.getMessage())
					.build();
		}
	}

	@GET
	public Response getFactories() {
		List<Factory> factories = this.factoryRepository.listAllFactories();
		return buildJsonResponse(factories);
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
