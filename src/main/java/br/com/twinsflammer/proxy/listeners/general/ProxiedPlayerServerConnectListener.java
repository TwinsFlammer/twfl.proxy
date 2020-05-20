package br.com.twinsflammer.proxy.listeners.general;

import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.common.shared.server.data.Server;
import br.com.twinsflammer.common.shared.server.manager.ServerManager;
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

        ServerInfo serverInfo = event.getTarget();

        Server server = ServerManager.getServer(serverInfo.getName());

        if (server == null || server.isLoginServer()) return;

        User user = UserManager.getUser(proxiedPlayer.getUniqueId());

        if (!user.isLogged()) event.setCancelled(true);
    }
}
