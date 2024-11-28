package com.connector.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.connector.dto.RequestDTO;
import com.connector.dto.ResponseDTO;
import com.connector.utils.RequestConverter;
import com.connector.utils.ResponseConverter;
import com.connector.utils.TimeFunctions;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
@Entity
@Table(name = "audition_request_response")
public class AuditionRequestResponse implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Convert(converter = RequestConverter.class)
	@Column(name = "request", columnDefinition = "text", nullable = false)
	public RequestDTO request;

	@Column(name = "description")
	public String description;

	@Column(name = "factory_id")
	public Integer factoryId;

	@Column(name = "request_method")
	public String requestMethod;

	@Column(name = "request_url")
	public String requestUrl;

	@Convert(converter = ResponseConverter.class)
	@Column(name = "response", columnDefinition = "text", nullable = false)
	public ResponseDTO response;

	@Column(name = "response_status")
	public Integer responseStatus;

	@Column(name = "took_seconds")
	public String tookSeconds;

	@Builder.Default
	@Column(name = "created_at", columnDefinition = "TIMESTAMP")
	private LocalDateTime createdAt = TimeFunctions.defaultLocalDateTimeNow();
}
