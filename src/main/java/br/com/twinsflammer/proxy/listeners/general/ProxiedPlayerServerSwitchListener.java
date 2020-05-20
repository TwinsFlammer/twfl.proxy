package br.com.twinsflammer.proxy.listeners.general;

import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.common.shared.server.data.Server;
import br.com.twinsflammer.common.shared.server.manager.ServerManager;
import br.com.twinsflammer.common.shared.util.Helper;
import br.com.twinsflammer.proxy.Proxy;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Created by @SrGutyerrez
 */
public class ProxiedPlayerServerSwitchListener implements Listener {
    @EventHandler
    public void onConnect(ServerSwitchEvent event) {
        ProxiedPlayer proxiedPlayer = event.getPlayer();

        String hostString = proxiedPlayer.getPendingConnection().getVirtualHost().getHostString();

        User user = UserManager.getUser(proxiedPlayer.getUniqueId());

        if (proxiedPlayer.getServer() == null) return;

        ServerInfo serverInfo = proxiedPlayer.getServer().getInfo();

        String name = serverInfo.getName();

        Server server = ServerManager.getServer(name);

        if (server == null) {
            Language language = user.getLanguage();

            proxiedPlayer.disconnect(
                    Helper.colorize(language.getMessage("errors.invalid_server"))
            );
            return;
        }

        user.setServer(
                Proxy.getInstance().getId(),
                hostString,
                server
        );
    }
}
