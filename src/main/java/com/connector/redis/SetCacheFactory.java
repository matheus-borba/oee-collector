package com.connector.redis;

import java.util.List;

import com.connector.model.Factory;
import com.connector.repository.IFactoryRepository;
import com.connector.utils.JsonUtil;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SetCacheFactory {

	private final ValueCommands<String, String> redis;
	private static final String ALL_ACTIVE_FACTORIES = "ALL_ACTIVE_FACTORIES";

	@Inject
	IFactoryRepository factoryRepository;

	public SetCacheFactory(RedisDataSource ds) {
		redis = ds.value(String.class);
	}

	public void execute() {
		List<Factory> factories = this.factoryRepository.listAllActiveFactories();
		String json = JsonUtil.toJson(factories);
		redis.set(ALL_ACTIVE_FACTORIES, json);
	}
}
