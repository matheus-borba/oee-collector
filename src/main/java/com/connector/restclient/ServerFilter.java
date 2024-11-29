package com.connector.restclient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.eclipse.microprofile.context.ManagedExecutor;

import com.connector.dto.RequestDTO;
import com.connector.dto.ResponseDTO;
import com.connector.model.AuditionRequestResponse;
import com.connector.service.AuditionService;

import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
@Provider
@Blocking
public class ServerFilter implements ContainerRequestFilter, ContainerResponseFilter {
	private static final Jsonb jsonb = (Jsonb)CDI.current().select(Jsonb.class, new Annotation[0]).get();
	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;;
	protected static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("America/Sao_Paulo");;

	@Inject
	AuditionService auditionService;

	@Inject
	ManagedExecutor managedExecutor;

	//Provider que executa ao realizar uma requisição ao sistema através dos endopoints - Salva os dados na tabela de auditoria
	public void auditRequestResponse(RequestDTO request, ResponseDTO response) {
	   AuditionRequestResponse audition = AuditionRequestResponse.builder()
				.withRequest(request)
				.withRequestMethod(request.getMethodName())
				.withRequestUrl(request.getUrl())
				.withResponse(response)
				.withResponseStatus(response.getStatus())
				.withTookSeconds(response.getTookSeconds())
				.build();
		auditionService.saveAudition(audition);
	}

	@Transactional(TxType.REQUIRES_NEW)
	public void filter(ContainerRequestContext requestContext) throws IOException {
		try {
			RequestDTO request = this.createRequest(requestContext);
			requestContext.getHeaders().add("audition-entity", jsonb.toJson(request));
		} catch (ParseException | IOException var3) {
			log.errorv("Error while intercepting client requests {0}", var3);
		}

	}

	@SuppressWarnings("rawtypes")
	@Transactional(TxType.REQUIRES_NEW)
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		try {
			Map<String, List<String>> headersRequest = this.extractHeadersRequest(requestContext.getHeaders());
			if (!headersRequest.containsKey("audition-entity")) {
				return;
			}

			String auditionEntityJson = (String)((List)requestContext.getHeaders().get("audition-entity")).get(0);
			RequestDTO request = (RequestDTO)jsonb.fromJson(auditionEntityJson, RequestDTO.class);
			ResponseDTO response = this.createResponse(responseContext);
			Long tookSeconds = null;
			if (!Objects.isNull(response.getDate()) && !Objects.isNull(request.getDate())) {
				tookSeconds = Math.abs(response.getDate().getTime() - request.getDate().getTime());
				tookSeconds = TimeUnit.MILLISECONDS.toSeconds(tookSeconds);
			}

			response.setTookSeconds(Objects.isNull(tookSeconds) ? "" + tookSeconds + "s" : "< 1s");
			this.auditRequestResponse(request, response);
		} catch (ParseException | IOException var8) {
			log.errorv("Error while intercepting client requests {0}", var8);
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, List<String>> extractHeadersRequest(MultivaluedMap<String, String> multivaluedHeaderMap) {
		Map<String, List<String>> headers = new HashMap();
		multivaluedHeaderMap.keySet().stream().forEach((key) -> {
			headers.put(key, (List)multivaluedHeaderMap.get(key));
		});
		return headers;
	}

	private RequestDTO createRequest(ContainerRequestContext context) throws IOException, ParseException {
		Map<String, List<String>> headers = this.extractHeadersRequest(context.getHeaders());
		URI uri = context.getUriInfo().getRequestUri();
		InputStream is = context.getEntityStream();
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		byte[] chunk = new byte[8192];

		int bytesRead;
		while((bytesRead = is.read(chunk)) != -1) {
			buffer.write(chunk, 0, bytesRead);
		}

		byte[] data = buffer.toByteArray();
		ByteArrayInputStream newInputStream = new ByteArrayInputStream(data);
		context.setEntityStream(newInputStream);
		String requestBody = new String(data, DEFAULT_CHARSET);
		return RequestDTO.builder().withUrl(uri.toString()).withBody(requestBody).withMethodName(context.getMethod()).withHeaders(headers).withDate(new Date()).build();
	}

	private ResponseDTO createResponse(ContainerResponseContext context) throws IOException, ParseException {
		String body = null;

		if (context.hasEntity()) {
			Object entity = context.getEntity();

			if (entity instanceof String) {
				body = (String) entity;
			} else if (entity instanceof InputStream inputStream) {
				try (InputStream is = inputStream) {
					byte[] bytes = is.readAllBytes();
					body = new String(bytes, StandardCharsets.UTF_8);
				} catch (IOException var10) {
					log.errorv("Erro convertendo request body {0}", var10);
				}
			}
		}
		Date responseDate = new Date();

		return ResponseDTO.builder()
				.withStatus(context.getStatus())
				.withBody(body)
				.withDate(responseDate)
				.build();
	}

}
