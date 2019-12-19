package com.redecommunity.proxy.listeners.general;

import com.redecommunity.common.shared.permissions.user.dao.UserDao;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.server.data.Server;
import com.redecommunity.common.shared.server.manager.ServerManager;
import com.redecommunity.proxy.Proxy;
import com.redecommunity.proxy.util.Messages;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.apache.commons.io.Charsets;

import java.util.UUID;

/**
 * Created by @SrGutyerrez
 */
public class ProxiedPlayerPostLoginListener implements Listener {
    @EventHandler
    public void onLoin(PostLoginEvent event) {
        ProxiedPlayer proxiedPlayer = event.getPlayer();

        if (!this.isValidUUID(proxiedPlayer.getUniqueId(), proxiedPlayer.getName())) {
            proxiedPlayer.disconnect(Messages.INVALID_UUID_MESSAGE);
            return;
        }

        User user = UserManager.getUser(proxiedPlayer.getUniqueId());

        UserDao userDao = new UserDao();

        if (user == null) {
            user = UserManager.generateUser(
                    proxiedPlayer.getName(),
                    proxiedPlayer.getUniqueId()
            );

            userDao.insert(user);
        }

        user = UserManager.getUser(proxiedPlayer.getUniqueId());

        if (user == null) {
            proxiedPlayer.disconnect(Messages.INVALID_USER);
            return;
        }

        ServerInfo serverInfo = proxiedPlayer.getServer().getInfo();

        String name = serverInfo.getName();

        Server server = ServerManager.getServer(name);

        user.setServer(
                Proxy.getInstance().getId(),
                "none",
                server
        );
    }

    private Boolean isValidUUID(UUID uuid, String username) {
        return uuid.equals(UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(Charsets.UTF_8)));
    }
}
