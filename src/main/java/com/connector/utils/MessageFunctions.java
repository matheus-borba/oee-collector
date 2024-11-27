package com.connector.utils;

import org.eclipse.microprofile.reactive.messaging.Message;

import io.vertx.core.json.JsonObject;

public class MessageFunctions {

	public static <T> T messageToObject(Message<JsonObject> message, Class<T> clazz) {
		JsonObject payload = message.getPayload();
		return payload.mapTo(clazz);
	}
}
