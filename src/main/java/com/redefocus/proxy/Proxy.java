package com.redefocus.proxy;

import com.google.common.collect.Lists;
import com.redefocus.api.bungeecord.FocusPlugin;
import com.redefocus.api.shared.connection.dao.ProxyServerDao;
import com.redefocus.api.shared.connection.data.ProxyServer;
import com.redefocus.api.shared.connection.manager.ProxyServerManager;
import com.redefocus.common.shared.Common;
import com.redefocus.common.shared.permissions.group.data.Group;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.permissions.user.manager.UserManager;
import com.redefocus.common.shared.preference.Preference;
import com.redefocus.common.shared.server.data.Server;
import com.redefocus.common.shared.server.manager.ServerManager;
import com.redefocus.common.shared.twitter.manager.TwitterManager;
import com.redefocus.common.shared.util.Constants;
import com.redefocus.proxy.authentication.manager.AttemptManager;
import com.redefocus.proxy.configuration.ProxyConfiguration;
import com.redefocus.proxy.manager.StartManager;
import com.redefocus.proxy.punish.manager.PunishmentManager;
import lombok.Getter;
import net.md_5.bungee.api.config.ServerInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * Created by @SrGutyerrez
 */
public class Proxy extends FocusPlugin {
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
        Proxy.setOffline();
    }

    public static Proxy getInstance() {
        return Proxy.instance;
    }

    public static Collection<User> getUsers() {
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

    public static void broadcastMessage(Group group, String message) {
        Proxy.broadcastMessage(group, message, new Preference[] {});
    }

    public static void broadcastMessage(Group group, String message, Preference... preferences) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("group_id", group.getId());
        jsonObject.put("message", message);

        JSONArray jsonArray = new JSONArray();

        Arrays.stream(preferences).forEach(preference -> jsonArray.add(preference.toString()));

        jsonObject.put("preferences", jsonArray);

        Common.getInstance().getDatabaseManager().getRedisManager().getDatabases().values()
                .forEach(redis -> redis.sendMessage(
                        Constants.BROADCAST_MESSAGE,
                        jsonObject.toString()
                ));
    }

    public static void setOffline() {
        ProxyServer proxyServer = Proxy.getCurrentProxy();

        proxyServer.setStatus(false);
        proxyServer.setUsers(Lists.newArrayList());

        ProxyServerDao proxyServerDao = new ProxyServerDao();

        proxyServerDao.update(proxyServer);
    }

    public static Integer getCurrentProxyPlayerCount() {
        return Proxy.getCurrentProxy().getPlayerCount();
    }

    public static Server getLobby() {
        return ServerManager.getLobbies()
                .stream()
                .filter(Server::isOnline)
                .min((server1, server2) -> server2.getPlayerCount().compareTo(server1.getPlayerCount()))
                .orElse(null);
    }

    public static ServerInfo constructServerInfo(Server server) {
        return net.md_5.bungee.api.ProxyServer.getInstance().constructServerInfo(
                server.getName(),
                server.getInetSocketAddress(),
                server.getDisplayName(),
                false
        );
    }

    public static ProxyServer getCurrentProxy() {
        return ProxyServerManager.getProxies()
                .stream()
                .filter(Objects::nonNull)
                .filter(proxyServer -> proxyServer.getId().equals(Proxy.getInstance().getId()))
                .findFirst()
                .orElse(null);
    }

    public static void unloadUser(User user) {
        user.setOffline();
        user.setLogged(false);
        TwitterManager.removeRequestToken(user.getId());
        AttemptManager.removeAttempt(user.getId());
        UserManager.removeUser(user.getId());
        PunishmentManager.clearPunishments(user.getId());
    }
}