package com.connector.utils;

import com.connector.dto.RequestDTO;
import com.google.gson.Gson;

import jakarta.persistence.AttributeConverter;

public class RequestConverter implements AttributeConverter<RequestDTO, String> {

	private static final Gson GSON = new Gson();

	@Override
	public String convertToDatabaseColumn(RequestDTO attribute) {
		return GSON.toJson(attribute);
	}

	@Override
	public RequestDTO convertToEntityAttribute(String dbData) {
		if(ApoioUtil.isEmpty(dbData)) {
			return null;
		}
		return GSON.fromJson(dbData, RequestDTO.class);
	}
}
