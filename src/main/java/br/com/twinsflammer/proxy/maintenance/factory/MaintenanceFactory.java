package br.com.twinsflammer.proxy.maintenance.factory;

import br.com.twinsflammer.common.shared.databases.redis.data.Redis;
import br.com.twinsflammer.common.shared.databases.redis.manager.RedisManager;
import br.com.twinsflammer.proxy.connection.listeners.motd.data.MOTD;
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

    public static MOTD getMaintenanceMOTD(MOTD defaultMOTD) {
        return new MOTD(
                0,
                defaultMOTD == null ? "" : defaultMOTD.getLine1(),
                "§cEstamos em manutenção no momento, aguarde.",
                true
        );
    }
}
