package com.connector.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class RequestDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String methodName;
	private String url;
	private String body;
	private String bodyTypeName;
	private Map<String, List<String>> headers;
	private Date date;
}
