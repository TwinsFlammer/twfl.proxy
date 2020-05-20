package br.com.twinsflammer.proxy.connection.listeners.players;

import br.com.twinsflammer.proxy.connection.listeners.motd.data.MOTD;
import br.com.twinsflammer.proxy.connection.listeners.motd.manager.MOTDManager;
import br.com.twinsflammer.proxy.maintenance.factory.MaintenanceFactory;
import br.com.twinsflammer.proxy.Proxy;
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

        if (serverPing == null) return;

        ServerPing.Players players = serverPing.getPlayers();

        players.setOnline(Proxy.getUsers().size());

        MOTD currentMOTD = MOTDManager.getCurrentMotd();

        MOTD motd = MaintenanceFactory.inMaintenance() ? MaintenanceFactory.getMaintenanceMOTD(currentMOTD) : currentMOTD;

        if (motd == null) return;

        serverPing.setDescription(motd.getLine1() + "\n" + motd.getLine2());

        event.setResponse(serverPing);
    }
}
