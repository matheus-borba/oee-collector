package com.connector.redis;

import java.util.List;
import java.util.Objects;

import com.connector.model.Factory;
import com.connector.utils.JsonUtil;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
@ApplicationScoped
public class GetCacheFactory {

	private final ValueCommands<String, String> redis;
	private static final String ALL_ACTIVE_FACTORIES = "ALL_ACTIVE_FACTORIES";

	public GetCacheFactory(RedisDataSource ds) {
		redis = ds.value(String.class);
	}

	//Método que busca as fabricas ativas no cache do Redis
	public List<Factory> execute() {
		String json = redis.get(ALL_ACTIVE_FACTORIES);
		if ( Objects.isNull(json)) {
			return List.of();
		}
		log.info(json);
		return JsonUtil.fromJsonList(json, Factory.class);
	}
}
