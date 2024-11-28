package com.connector.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.json.bind.Jsonb;

public class JsonUtil {

	private static final Jsonb jsonb = CDI.current().select( Jsonb.class ).get();

	public static String toJson( Object object ) {
		return jsonb.toJson( object );
	}

	public static < T > T fromJson( String json, Class< T > clazz ) {
		return jsonb.fromJson( json, clazz );
	}

	public static < T > List< T > fromJsonList( String json, Class< T > clazz ) {
		Type listType = new ListParameterizedType( clazz );
		return jsonb.fromJson( json, listType );
	}

	static class ListParameterizedType implements ParameterizedType {
		private Type type;

		ListParameterizedType( Type type ) {
			this.type = type;
		}

		@Override
		public Type[] getActualTypeArguments() {
			return new Type[] { type };
		}

		@Override
		public Type getRawType() {
			return List.class;
		}

		@Override
		public Type getOwnerType() {
			return null;
		}
	}

}
