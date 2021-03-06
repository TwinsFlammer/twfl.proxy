package br.com.twinsflammer.proxy.connection.listeners.listeners;

import br.com.twinsflammer.proxy.Proxy;
import br.com.twinsflammer.common.shared.databases.redis.handler.JedisMessageListener;
import br.com.twinsflammer.common.shared.databases.redis.handler.annonation.ChannelName;
import br.com.twinsflammer.common.shared.databases.redis.handler.event.JedisMessageEvent;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.common.shared.server.data.Server;
import br.com.twinsflammer.common.shared.server.manager.ServerManager;
import br.com.twinsflammer.common.shared.util.Constants;
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

        if (user == null) return;

        Language language = user.getLanguage();

        ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(user.getUniqueId());

        if (proxiedPlayer == null) return;

        Server currentServer = user.getServer();
        Server connectServer = ServerManager.getServer(serverId);

        if (connectServer == null) {
            proxiedPlayer.sendMessage(
                    language.getMessage("messages.default_commands.server.unknown_server")
            );
            return;
        }

        if (currentServer != null && connectServer != null && currentServer.isSimilar(connectServer)) {
            proxiedPlayer.sendMessage(
                    language.getMessage("messages.default_commands.server.already_connected")
            );
            return;
        }

        ServerInfo serverInfo = Proxy.constructServerInfo(connectServer);

        proxiedPlayer.connect(serverInfo);
    }
}
