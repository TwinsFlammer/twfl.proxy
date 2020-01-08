package com.redecommunity.proxy.connection.listeners.players;

import com.redecommunity.proxy.Proxy;
import com.redecommunity.proxy.connection.listeners.motd.data.Motd;
import com.redecommunity.proxy.connection.listeners.motd.manager.MotdManager;
import com.redecommunity.proxy.maintenance.factory.MaintenanceFactory;
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

        Motd motd = MaintenanceFactory.inMaintenance() ? MaintenanceFactory.getMaintenanceMOTD() : MotdManager.getCurrentMotd();

        if (motd == null) return;

        serverPing.setDescription(motd.getLine1() + "\n" + motd.getLine2());

        event.setResponse(serverPing);
    }
}
