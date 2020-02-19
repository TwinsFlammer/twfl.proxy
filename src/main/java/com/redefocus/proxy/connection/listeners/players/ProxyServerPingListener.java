package com.redefocus.proxy.connection.listeners.players;

import com.redefocus.proxy.Proxy;
import com.redefocus.proxy.connection.listeners.motd.data.MOTD;
import com.redefocus.proxy.connection.listeners.motd.manager.MOTDManager;
import com.redefocus.proxy.maintenance.factory.MaintenanceFactory;
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

        MOTD currentMOTD = MOTDManager.getCurrentMotd();

        MOTD motd = MaintenanceFactory.inMaintenance() ? MaintenanceFactory.getMaintenanceMOTD(currentMOTD) : currentMOTD;

        if (motd == null) return;

        serverPing.setDescription(motd.getLine1() + "\n" + motd.getLine2());

        event.setResponse(serverPing);
    }
}
