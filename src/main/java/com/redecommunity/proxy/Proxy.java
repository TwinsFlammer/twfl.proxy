package com.redecommunity.proxy;

import com.redecommunity.api.bungeecord.CommunityPlugin;
import com.redecommunity.common.shared.server.data.Server;
import com.redecommunity.common.shared.server.manager.ServerManager;
import com.redecommunity.proxy.configuration.ProxyConfiguration;
import com.redecommunity.proxy.connection.manager.ProxyServerManager;
import com.redecommunity.proxy.manager.StartManager;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;

/**
 * Created by @SrGutyerrez
 */
public class Proxy extends CommunityPlugin {
    private static Proxy instance;

    public Proxy() {
        Proxy.instance = this;
    }

    private ClassLoader classLoader = this.getClass().getClassLoader();

    @Getter
    private Integer id;
    @Getter
    private String name;

    @Override
    public void onEnablePlugin() {
        ProxyConfiguration proxyConfiguration = new ProxyConfiguration();

        this.id = proxyConfiguration.getId();
        this.name = proxyConfiguration.getName();

        new StartManager();
    }

    @Override
    public void onDisablePlugin() {

    }

    public static Proxy getInstance() {
        return Proxy.instance;
    }

    public static Collection<Integer> getUsers() {
        return ProxyServerManager.getUsers();
    }


    public InputStream getResource(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        } else {
            try {
                URL url = this.classLoader.getResource(filename);
                if (url == null) {
                    return null;
                } else {
                    URLConnection connection = url.openConnection();
                    connection.setUseCaches(false);
                    return connection.getInputStream();
                }
            } catch (IOException var4) {
                var4.printStackTrace();
                return null;
            }
        }
    }

    public static ProxyServer getProxyServer() {
        return ProxyServer.getInstance();
    }

    public static Server getLobby() {
        return ServerManager.getLobbies()
                .stream()
                .filter(Server::isOnline)
                .min((server1, server2) -> server2.getPlayerCount().compareTo(server1.getPlayerCount()))
                .orElse(null);
    }

    public static ServerInfo constructServerInfo(Server server) {
        return Proxy.getProxyServer().constructServerInfo(
                server.getName(),
                server.getInetSocketAddress(),
                server.getDisplayName(),
                false
        );
    }
}
