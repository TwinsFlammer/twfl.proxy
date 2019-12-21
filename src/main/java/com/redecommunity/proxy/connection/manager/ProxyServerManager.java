package com.redecommunity.proxy.connection.manager;

import com.google.common.collect.Lists;
import com.redecommunity.proxy.Proxy;
import com.redecommunity.proxy.connection.dao.ProxyServerDao;
import com.redecommunity.proxy.connection.data.ProxyServer;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
                false,
                Lists.newArrayList()
        );

        proxyServerDao.insert(proxyServer);

        ProxyServerManager.proxies.add(proxyServer);
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

    public static List<Integer> getUsers() {
        List<Integer> users = Lists.newArrayList();

        ProxyServerManager.proxies.forEach(proxyServer -> users.addAll(proxyServer.getUsersId()));

        return users;
    }

    public static ProxyServer getCurrentProxy() {
        return ProxyServerManager.proxies
                .stream()
                .filter(Objects::nonNull)
                .filter(proxyServer -> proxyServer.getId() == Proxy.getInstance().getId())
                .findFirst()
                .orElse(null);
    }
}
