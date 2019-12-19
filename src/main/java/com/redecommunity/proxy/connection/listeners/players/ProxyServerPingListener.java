package com.redecommunity.proxy.connection.listeners.players;

import com.redecommunity.proxy.Proxy;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Created by @SrGutyerrez
 */
public class ProxyServerPingListener implements Listener {
    @EventHandler
    public void onPing(ServerPing event) {
        ServerPing.Players players = event.getPlayers();

        players.setOnline(Proxy.getUsers().size());
    }
}
