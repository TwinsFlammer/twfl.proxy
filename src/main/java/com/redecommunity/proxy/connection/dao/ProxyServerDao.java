package com.redecommunity.proxy.connection.dao;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.redecommunity.common.shared.Common;
import com.redecommunity.common.shared.databases.redis.data.Redis;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.proxy.connection.data.ProxyServer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisDataException;

import java.util.List;
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

            JSONObject jsonObject = (JSONObject) JSONValue.parse(value);

            Integer proxyId = ((Long) jsonObject.get("proxy_id")).intValue();
            String name = (String) jsonObject.get("name");

            JSONArray playersId = (JSONArray) jsonObject.get("users");

            List<User> users = Lists.newArrayList();

            playersId.forEach(o -> {
                User user = (User) o;

                users.add(user);
            });

            Boolean online = (Boolean) jsonObject.get("status");

            return (T) new ProxyServer(proxyId, name, online, users);
        } catch (JedisDataException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public <T extends ProxyServer> Set<T> findAll() {
        try (Jedis jedis = this.redis.getJedisPool().getResource()) {
            Map<String, String> proxies1 = jedis.hgetAll("proxies");

            Set<T> proxies2 = Sets.newConcurrentHashSet();

            proxies1.keySet().forEach(key -> {
                ProxyServer proxy = this.findOne(key);

                proxies2.add((T) proxy);
            });
            return proxies2;
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
