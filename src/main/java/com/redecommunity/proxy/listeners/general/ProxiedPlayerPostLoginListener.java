package com.redecommunity.proxy.listeners.general;

import com.google.common.collect.Lists;
import com.redecommunity.common.shared.permissions.user.dao.UserDao;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.proxy.util.Messages;
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
            user = new User(
                    null,
                    proxiedPlayer.getName().toLowerCase(),
                    proxiedPlayer.getName(),
                    proxiedPlayer.getUniqueId(),
                    null,
                    null,
                    System.currentTimeMillis(),
                    null,
                    null,
                    null,
                    null,
                    1,
                    Lists.newArrayList()
            );

            userDao.insert(user);
        }

        UserManager.getUser(proxiedPlayer.getUniqueId());
    }

    private Boolean isValidUUID(UUID uuid, String username) {
        return uuid.equals(UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(Charsets.UTF_8)));
    }
}
