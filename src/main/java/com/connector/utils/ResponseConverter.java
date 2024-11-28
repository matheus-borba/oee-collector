package com.connector.utils;

import com.connector.dto.ResponseDTO;
import com.google.gson.Gson;

import jakarta.persistence.AttributeConverter;

public class ResponseConverter implements AttributeConverter<ResponseDTO, String> {

	private static final Gson GSON = new Gson();

	@Override
	public String convertToDatabaseColumn(ResponseDTO attribute) {
		return GSON.toJson(attribute);
	}

	@Override
	public ResponseDTO convertToEntityAttribute(String dbData) {
		if(ApoioUtil.isEmpty(dbData)) {
			return null;
		}
		return GSON.fromJson(dbData, ResponseDTO.class);
	}
}
