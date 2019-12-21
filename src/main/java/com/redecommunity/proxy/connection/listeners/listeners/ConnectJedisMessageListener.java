package com.redecommunity.proxy.connection.listeners.listeners;

import com.redecommunity.common.shared.databases.redis.handler.JedisMessageListener;
import com.redecommunity.common.shared.databases.redis.handler.annonation.ChannelName;
import com.redecommunity.common.shared.databases.redis.handler.event.JedisMessageEvent;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.server.data.Server;
import com.redecommunity.common.shared.server.manager.ServerManager;
import com.redecommunity.common.shared.util.Constants;
import com.redecommunity.proxy.Proxy;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Created by @SrGutyerrez
 */
public class ConnectJedisMessageListener implements JedisMessageListener {
    @ChannelName(name = Constants.CONNECT_CHANNEL)
    public void onMessage(JedisMessageEvent event) {
        String message = event.getMessage();

        JSONObject jsonObject = (JSONObject) JSONValue.parse(message);

        String platform = (String) jsonObject.get("platform");
        Integer userId = ((Long) jsonObject.get("user_id")).intValue();
        Integer serverId = ((Long) jsonObject.get("server_id")).intValue();

        User user = UserManager.getUser(userId);

        ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(user.getUniqueId());

        if (proxiedPlayer == null) return;

        Server server = ServerManager.getServer(serverId);
        ServerInfo serverInfo = Proxy.constructServerInfo(server);

        proxiedPlayer.connect(serverInfo);
    }
}
