package com.redecommunity.proxy.listeners.general;

import com.google.common.collect.Maps;
import com.redecommunity.common.shared.permissions.user.dao.UserDao;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.twitter.manager.TwitterManager;
import com.redecommunity.proxy.account.manager.AttemptManager;
import com.redecommunity.proxy.connection.dao.ProxyServerDao;
import com.redecommunity.proxy.connection.data.ProxyServer;
import com.redecommunity.proxy.connection.manager.ProxyServerManager;
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

        ProxyServer proxyServer = ProxyServerManager.getCurrentProxy();

        proxyServer.getUsers().remove(user);

        ProxyServerDao proxyServerDao = new ProxyServerDao();

        proxyServerDao.update(proxyServer);

        user.setOffline();
        user.setLogged(false);

        TwitterManager.removeRequestToken(user.getId());
        AttemptManager.removeAttempt(user.getId());
    }
}
