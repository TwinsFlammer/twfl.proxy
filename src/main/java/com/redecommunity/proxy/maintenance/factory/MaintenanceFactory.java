package com.redecommunity.proxy.maintenance.factory;

import com.redecommunity.common.shared.databases.redis.data.Redis;
import com.redecommunity.common.shared.databases.redis.manager.RedisManager;
import com.redecommunity.proxy.connection.listeners.motd.data.MOTD;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisDataException;

/**
 * Created by @SrGutyerrez
 */
public class MaintenanceFactory {
    public static Boolean inMaintenance() {
        Redis redis = RedisManager.getDefaultRedis();

        try (Jedis jedis = redis.getJedisPool().getResource()) {
            if (jedis.hexists("maintenance", "general")) {
                String serialized = jedis.hget("maintenance", "general");

                JSONObject jsonObject = (JSONObject) JSONValue.parse(serialized);

                return (Boolean) jsonObject.get("status");
            }
        } catch (JedisDataException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    public static void setMaintenance(Boolean status) {
        Redis redis = RedisManager.getDefaultRedis();

        try (Jedis jedis = redis.getJedisPool().getResource()) {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("status", status);

            jedis.hset("maintenance", "general", jsonObject.toString());
        } catch (JedisDataException exception) {
            exception.printStackTrace();
        }
    }

    public static MOTD getMaintenanceMOTD() {
        return new MOTD(
                0,
                "§cRede Community §f- FACTIONS §7(1.8.*)",
                "§cEstamos em manutenção no momento, aguarde.",
                true
        );
    }
}
