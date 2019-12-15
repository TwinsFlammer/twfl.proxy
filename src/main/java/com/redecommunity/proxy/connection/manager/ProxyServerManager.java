package com.redecommunity.proxy.connection.manager;

import com.google.common.collect.Lists;
import com.redecommunity.proxy.connection.data.ProxyServer;

import java.util.List;

/**
 * Created by @SrGutyerrez
 */
public class ProxyServerManager {
    private static List<ProxyServer> proxies = Lists.newArrayList();

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
}
