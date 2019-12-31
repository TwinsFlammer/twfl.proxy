package com.redecommunity.proxy.connection.manager;

import com.google.common.collect.Lists;
import com.redecommunity.common.shared.Common;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.proxy.Proxy;
import com.redecommunity.proxy.connection.dao.ProxyServerDao;
import com.redecommunity.proxy.connection.data.ProxyServer;
import com.redecommunity.proxy.connection.runnable.ProxyServerRefreshRunnable;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by @SrGutyerrez
 */
public class ProxyServerManager {
    private static List<ProxyServer> proxies = Lists.newArrayList();

    public ProxyServerManager() {
        ProxyServerDao proxyServerDao = new ProxyServerDao();

        ProxyServer proxyServer = new ProxyServer(
                Proxy.getInstance().getId(),
                Proxy.getInstance().getName(),
                true,
                Lists.newArrayList()
        );

        proxyServerDao.insert(proxyServer);

        ProxyServerManager.proxies.add(proxyServer);

        Common.getInstance().getScheduler().scheduleWithFixedDelay(
                new ProxyServerRefreshRunnable(),
                0,
                1,
                TimeUnit.SECONDS
        );
    }

    public static Integer getProxyCount() {
        return ProxyServerManager.proxies.size();
    }

    public static Integer getProxyCountOnline() {
        return (int) ProxyServerManager.proxies
                .stream()
                .filter(ProxyServer::isOnline)
                .count();
    }

    public static Integer getCurrentProxyPlayerCount() {
        return ProxyServerManager.getCurrentProxy().getPlayerCount();
    }

    public static void setOffline() {
        ProxyServer proxyServer = ProxyServerManager.getCurrentProxy();

        proxyServer.setStatus(false);
        proxyServer.setUsers(Lists.newArrayList());

        ProxyServerDao proxyServerDao = new ProxyServerDao();

        proxyServerDao.update(proxyServer);
    }

    public static ProxyServer getProxyServer(Integer id) {
        return ProxyServerManager.proxies
                .stream()
                .filter(proxyServer -> proxyServer.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static ProxyServer getProxyServer(String name) {
        return ProxyServerManager.proxies
                .stream()
                .filter(proxyServer -> proxyServer.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public static Boolean addProxyServer(ProxyServer proxyServer) {
        return ProxyServerManager.proxies.add(proxyServer);
    }

    public static List<User> getUsers() {
        List<User> users = Lists.newArrayList();

        ProxyServerManager.proxies.forEach(proxyServer -> users.addAll(proxyServer.getUsers()));

        return users;
    }

    public static ProxyServer getCurrentProxy() {
        return ProxyServerManager.proxies
                .stream()
                .filter(Objects::nonNull)
                .filter(proxyServer -> proxyServer.getId().equals(Proxy.getInstance().getId()))
                .findFirst()
                .orElse(null);
    }
}
