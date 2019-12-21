package com.redecommunity.proxy.connection.listeners.players;

import com.redecommunity.proxy.Proxy;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Created by @SrGutyerrez
 */
public class ProxyServerPingListener implements Listener {
    @EventHandler
    public void onPing(ProxyPingEvent event) {
        ServerPing serverPing = event.getResponse();

        ServerPing.Players players = serverPing.getPlayers();

        players.setOnline(Proxy.getUsers().size());

        event.setResponse(serverPing);
    }
}
