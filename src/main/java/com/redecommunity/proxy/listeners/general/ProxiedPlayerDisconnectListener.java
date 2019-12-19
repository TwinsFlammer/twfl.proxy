package com.redecommunity.proxy.listeners.general;

import com.google.common.collect.Maps;
import com.redecommunity.common.shared.permissions.user.dao.UserDao;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;

/**
 * Created by @SrGutyerrez
 */
public class ProxiedPlayerDisconnectListener implements Listener {
    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer proxiedPlayer = event.getPlayer();

        User user = UserManager.getUser(proxiedPlayer.getUniqueId());

        if (user == null) return;

        user.setLastAddress(proxiedPlayer.getAddress().getAddress().getHostAddress());

        UserDao userDao = new UserDao();

        HashMap<String, Object> keys = Maps.newHashMap();

        keys.put("last_address", user.getLastAddress());

        userDao.update(keys, "id", user.getId());
    }
}