package br.com.twinsflammer.proxy.listeners.general.kick;

import br.com.twinsflammer.common.shared.databases.redis.handler.JedisMessageListener;
import br.com.twinsflammer.common.shared.databases.redis.handler.annonation.ChannelName;
import br.com.twinsflammer.common.shared.databases.redis.handler.event.JedisMessageEvent;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.common.shared.util.Constants;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Created by @SrGutyerrez
 */
public class KickChannelJedisMessageListener implements JedisMessageListener {
    @ChannelName(name = Constants.KICK_CHANNEL)
    public void onMessage(JedisMessageEvent event) {
        String message = event.getMessage();

        JSONObject jsonObject = (JSONObject) JSONValue.parse(message);

        Integer userId = ((Long) jsonObject.get("user_id")).intValue();
        String reason = (String) jsonObject.get("reason");

        User user = UserManager.getUser(userId);

        ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(user.getUniqueId());

        if (proxiedPlayer != null) {
            Connection connection = proxiedPlayer.getPendingConnection();

            connection.disconnect(new TextComponent(reason));
        }
    }
}
