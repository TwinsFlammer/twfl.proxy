package br.com.twinsflammer.proxy.listeners.general.broadcast;

import br.com.twinsflammer.common.shared.databases.redis.handler.JedisMessageListener;
import br.com.twinsflammer.common.shared.databases.redis.handler.annonation.ChannelName;
import br.com.twinsflammer.common.shared.databases.redis.handler.event.JedisMessageEvent;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.common.shared.preference.Preference;
import br.com.twinsflammer.common.shared.util.Constants;
import br.com.twinsflammer.common.shared.util.Helper;
import net.md_5.bungee.api.ProxyServer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Created by @SrGutyerrez
 */
public class BroadcastJedisMessageListener implements JedisMessageListener {
    @ChannelName(name = Constants.BROADCAST_MESSAGE)
    public void onMessage(JedisMessageEvent event) {
        String message = event.getMessage();

        JSONObject jsonObject = (JSONObject) JSONValue.parse(message);

        Integer groupId = ((Long) jsonObject.get("group_id")).intValue();
        String value = (String) jsonObject.get("message");

        JSONArray jsonArray = (JSONArray) jsonObject.get("preferences");

        Preference[] preferences = new Preference[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            Object object = jsonArray.get(i);

            String preferenceName = (String) object;

            Preference preference = Preference.valueOf(preferenceName);

            preferences[i] = preference;
        }

        ProxyServer.getInstance().getPlayers().forEach(proxiedPlayer -> {
            User user = UserManager.getUser(proxiedPlayer.getUniqueId());

            if (user.hasGroup(groupId) && user.isLogged()) {

                Integer disabledCount = 0;

                for (Preference preference : preferences)
                    if (user.isDisabled(preference)) disabledCount++;

                if (disabledCount == 0) proxiedPlayer.sendMessage(Helper.colorize(value));
            }
        });
    }
}
