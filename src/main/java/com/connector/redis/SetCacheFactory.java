package com.connector.redis;

import java.util.List;

import com.connector.enums.CacheKeys;
import com.connector.model.Factory;
import com.connector.utils.JsonUtil;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SetCacheFactory {
    
    private final ValueCommands<String, String> redis;

    public SetCacheFactory(RedisDataSource source) throws Exception {
        redis = source.value( String.class );
    }

    public void execute(List<Factory> factories) {
        String json = JsonUtil.toJson(factories);
        redis.set( CacheKeys.ALL_ACTIVE_FACTORIES.name(), json);
    }

    public void clearCache() throws Exception {
        redis.set(CacheKeys.ALL_ACTIVE_FACTORIES.name(), "");
    }
}
