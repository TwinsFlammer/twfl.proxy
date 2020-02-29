package com.redecommunity.proxy.listeners.general;

import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.server.data.Server;
import com.redecommunity.common.shared.server.manager.ServerManager;
import com.redecommunity.common.shared.util.Helper;
import com.redecommunity.proxy.Proxy;
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
