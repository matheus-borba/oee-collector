package com.connector.restclient;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.eclipse.microprofile.context.ManagedExecutor;

import com.connector.dto.RequestDTO;
import com.connector.dto.ResponseDTO;
import com.connector.model.AuditionRequestResponse;
import com.connector.service.AuditionService;

import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import jakarta.ws.rs.core.EntityTag;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
@Provider
@Blocking
public class ClientFilter implements ClientRequestFilter, ClientResponseFilter {
	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
	private static final String DATE_PATTERN = "EEE MMM d HH:mm:ss yyyy";
	private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("America/Sao_Paulo");
	protected static final String HEADER_ORIGIN_ACTION = "origin-action";

	@Inject
	AuditionService auditionService;

	@Inject
	ManagedExecutor managedExecutor;

	//Provider que executa ao realizar uma requisição a API MOCK - Salva os dados na tabela de auditoria
	@Override
	public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
		try {
			ResponseDTO response = this.createResponse(responseContext);
			RequestDTO request = this.createRequest(requestContext);
			Long tookSeconds = null;
			if (!Objects.isNull(response.getDate()) && !Objects.isNull(request.getDate())) {
				tookSeconds = Math.abs(response.getDate().getTime() - request.getDate().getTime());
				tookSeconds = TimeUnit.MILLISECONDS.toSeconds(tookSeconds);
			}
			response.setTookSeconds(Objects.isNull(tookSeconds) ? "" + tookSeconds + "s" : "< 1s");
			managedExecutor.execute(() -> this.audidRequestResponse(request, response));
		} catch (Exception e) {
			log.errorv("Error while intercepting client requests {0}", e);
		}
	}

	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.US);
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone(DEFAULT_ZONE_ID));
		requestContext.getHeaders().add("date", simpleDateFormat.format(new Date()));
	}

	public void audidRequestResponse(RequestDTO request, ResponseDTO response) {
		List<String> headers = request.getHeaders().get(HEADER_ORIGIN_ACTION);
		String[] originActions = headers.get(0).split("-");
		String method = originActions[0];
		String factoryId = originActions[1];
		AuditionRequestResponse audition = AuditionRequestResponse.builder()
				.withRequest(request)
				.withRequestMethod(request.getMethodName())
				.withRequestUrl(request.getUrl())
				.withDescription(method)
				.withResponse(response)
				.withResponseStatus(response.getStatus())
				.withFactoryId(Integer.parseInt(factoryId))
				.withTookSeconds(response.getTookSeconds())
				.build();
		auditionService.saveAudition(audition);
	}

	private ResponseDTO createResponse(ClientResponseContext context) throws IOException {
		String body = null;
		String entityTag = null;
		if (context.hasEntity()) {
			InputStream entityStream = context.getEntityStream();
			StringBuilder bodyBuilder = new StringBuilder();
			context.setEntityStream(this.inBoundEntity(entityStream, bodyBuilder));
			body = bodyBuilder.toString();
			entityTag = (String)Optional.ofNullable(context.getEntityTag()).map(EntityTag::getValue).orElse((String)null);
		}
		return ResponseDTO.builder()
				.withStatus(context.getStatus())
				.withBody(body)
				.withEntityTag(entityTag)
				.withDate(context.getDate())      
				.build();
	}

	@SuppressWarnings("rawtypes")
	private RequestDTO createRequest(ClientRequestContext context) throws IOException, ParseException {
		URI uri = context.getUri();
		Map<String, List<String>> headers = this.extractHeaders(context.getStringHeaders());
		String body = null;
		String bodyTypeName = null;
		Object entity = null;
		boolean hasEntity = context.hasEntity() && !((entity = context.getEntity()) instanceof Normalizer.Form);
		if (hasEntity) {
			Jsonb jsonb = JsonbBuilder.create();
			body = jsonb.toJson(entity);
			bodyTypeName = entity.getClass().getName();
		}

		String requestDate = (String)((List)headers.get("date")).get(0);
		SimpleDateFormat dateParser = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy", Locale.US);
		dateParser.setTimeZone(TimeZone.getTimeZone(DEFAULT_ZONE_ID));
		return RequestDTO.builder().withUrl(uri.toString()).withBody(body).withBodyTypeName(bodyTypeName).withMethodName(context.getMethod()).withDate(dateParser.parse(requestDate)).withHeaders(headers).build();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map<String, List<String>> extractHeaders(MultivaluedMap<String, String> multivalueHeaderMap) {
		Map<String, List<String>> headers = new HashMap<>();
		multivalueHeaderMap.keySet().stream().forEach((key) ->{
			headers.put(key, (List)multivalueHeaderMap.get(key));
		});
		return headers;
	}

	private InputStream inBoundEntity(InputStream stream, StringBuilder builder) throws IOException {
		if (!((InputStream)stream).markSupported()) {
			stream = new BufferedInputStream((InputStream)stream);
		}

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		try {
			byte[] data = new byte[16384];

			int nRead;
			while((nRead = ((InputStream)stream).read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
			builder.append(new String(data, 0, nRead, DEFAULT_CHARSET));
			}

			ByteArrayInputStream retorno = new ByteArrayInputStream(buffer.toByteArray());
			retorno.mark(0);
			return retorno;
		} catch (IOException var7) {
			var7.printStackTrace();
			return new BufferedInputStream((InputStream)stream);
		}
	}
}
