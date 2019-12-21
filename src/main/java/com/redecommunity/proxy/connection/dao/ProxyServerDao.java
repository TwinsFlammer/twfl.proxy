package com.redecommunity.proxy.connection.dao;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.redecommunity.common.shared.Common;
import com.redecommunity.common.shared.databases.redis.data.Redis;
import com.redecommunity.proxy.connection.data.ProxyServer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisDataException;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by @SrGutyerrez
 */
public class ProxyServerDao {
    private Redis redis = Common.getInstance().getDatabaseManager().getRedisManager().getDatabase("general");

    public <T extends ProxyServer> T findOne(Integer id) {
        return this.findOne("proxy_" + id);
    }

    private <T extends ProxyServer> T findOne(String field) {
        try (Jedis jedis = this.redis.getJedisPool().getResource()) {
            String value = jedis.hget("proxies", field);

            JSONObject object = (JSONObject) JSONValue.parse(value);

            Integer proxyId = (Integer) object.get("proxy_id");
            String name = (String) object.get("name");

            JSONArray playersId = (JSONArray) object.get("players_id");

            Collection<Integer> users = Lists.newArrayList();

            playersId.forEach(o -> users.add((Integer) o));

            Boolean online = (Boolean) object.get("status");

            return (T) new ProxyServer(proxyId, name, online, users);
        } catch (JedisDataException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public <T extends ProxyServer> Set<T> findAll() {
        try (Jedis jedis = this.redis.getJedisPool().getResource()) {
            Map<String, String> value = jedis.hgetAll("proxies");

            Set<T> proxies = Sets.newConcurrentHashSet();

            value.forEach((key, value1) -> {
                ProxyServer proxy = this.findOne(value1);

                proxies.add((T) proxy);
            });
            return proxies;
        } catch (JedisDataException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public <T extends ProxyServer> void insert(T proxyServer) {
        try (Jedis jedis = this.redis.getJedisPool().getResource()) {
            jedis.hset(
                    "proxies",
                    "proxy_" + proxyServer.getId(),
                    proxyServer.toJSONString()
            );
        } catch (JedisDataException exception) {
            exception.printStackTrace();
        }
    }

    @Deprecated
    public <T extends ProxyServer> void update(T proxyServer) {
        this.insert(proxyServer);
    }
}
