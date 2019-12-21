package com.redecommunity.proxy.listeners.general;

import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.server.data.Server;
import com.redecommunity.common.shared.server.manager.ServerManager;
import com.redecommunity.proxy.Proxy;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Created by @SrGutyerrez
 */
public class ProxiedPlayerServerConnectListener implements Listener {
    @EventHandler
    public void onConnect(ServerConnectEvent event) {
        ProxiedPlayer proxiedPlayer = event.getPlayer();

        User user = UserManager.getUser(proxiedPlayer.getUniqueId());

        if (proxiedPlayer.getServer() == null) return;

        ServerInfo serverInfo = proxiedPlayer.getServer().getInfo();

        String name = serverInfo.getName();

        Server server = ServerManager.getServer(name);

        user.setServer(
                Proxy.getInstance().getId(),
                "none",
                server
        );
    }
}