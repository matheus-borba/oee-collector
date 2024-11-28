package com.connector.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class ResponseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer status;
	private String body;
	private String entityTag;
	private Date date;
	private String tookSeconds;
}
