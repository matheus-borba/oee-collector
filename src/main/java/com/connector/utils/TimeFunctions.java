package com.connector.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeFunctions {

	private TimeFunctions() {
		throw new IllegalStateException( "Utility class" );
	}

	private static final ZoneId DEFAULT_ZONE_ID = ZoneId
			.of( "America/Sao_Paulo" );

	public static LocalDateTime defaultLocalDateTimeNow() {
		return LocalDateTime.now( DEFAULT_ZONE_ID );
	}
}
