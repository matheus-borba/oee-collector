package com.connector.redis;

import java.util.ArrayList;
import java.util.List;

import com.connector.enums.CacheKeys;
import com.connector.model.Factory;
import com.connector.utils.ApoioUtil;
import com.connector.utils.JsonUtil;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
@ApplicationScoped
public class GetCacheFactory {
    
    private final ValueCommands<String, String> redis;

    public GetCacheFactory(RedisDataSource source) {
        redis = source.value(String.class);
    }

    public List<Factory> execute(Void param) {
        try {         
            String json = redis.get(CacheKeys.ALL_ACTIVE_FACTORIES.name());
            if(ApoioUtil.isEmpty(json)) {
                return new ArrayList<>();
            }
            return JsonUtil.fromJsonList(json, Factory.class);
        } catch (Exception e) {
            log.info(e);
            return new ArrayList<>();
        }
    }
}
