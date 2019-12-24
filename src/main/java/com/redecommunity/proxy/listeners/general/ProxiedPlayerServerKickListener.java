package com.redecommunity.proxy.listeners.general;

import com.redecommunity.common.shared.server.data.Server;
import com.redecommunity.common.shared.server.manager.ServerManager;
import com.redecommunity.proxy.Proxy;
import com.redecommunity.proxy.util.Messages;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Created by @SrGutyerrez
 */
public class ProxiedPlayerServerKickListener implements Listener {
    @EventHandler
    public void onKick(ServerKickEvent event) {
        ProxiedPlayer proxiedPlayer = event.getPlayer();

        ServerInfo serverInfo = event.getKickedFrom();

        Server server = ServerManager.getServer(serverInfo.getName());

        if (server == null || server.isLobby()) return;

        Server server1 = Proxy.getLobby();

        if (server1 == null) {
            event.setKickReason(Messages.DONT_HAVE_LOBBY);
            return;
        }

        ServerInfo serverInfo1 = Proxy.constructServerInfo(server);

        event.setCancelled(true);
        event.setCancelServer(serverInfo1);
    }
}
