package br.com.twinsflammer.proxy;

import br.com.twinsflammer.proxy.authentication.manager.AuthenticationManager;
import br.com.twinsflammer.proxy.cloudflare.api.CloudFlareAPI;
import br.com.twinsflammer.proxy.cloudflare.data.DNSRecord;
import br.com.twinsflammer.proxy.configuration.ProxyConfiguration;
import br.com.twinsflammer.proxy.manager.StartManager;
import br.com.twinsflammer.proxy.punish.manager.PunishmentManager;
import com.google.common.collect.Sets;
import br.com.twinsflammer.api.bungeecord.TwinsPlugin;
import br.com.twinsflammer.api.shared.connection.dao.ProxyServerDao;
import br.com.twinsflammer.api.shared.connection.data.ProxyServer;
import br.com.twinsflammer.api.shared.connection.manager.ProxyServerManager;
import br.com.twinsflammer.common.shared.Common;
import br.com.twinsflammer.common.shared.permissions.group.data.Group;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.common.shared.preference.Preference;
import br.com.twinsflammer.common.shared.server.data.Server;
import br.com.twinsflammer.common.shared.server.manager.ServerManager;
import br.com.twinsflammer.common.shared.twitter.manager.TwitterManager;
import br.com.twinsflammer.common.shared.util.Constants;
import br.com.twinsflammer.common.shared.util.Printer;
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
public class Proxy extends TwinsPlugin {
    private static Proxy instance;

    public Proxy() {
        Proxy.instance = this;
    }

    private ClassLoader classLoader = this.getClass().getClassLoader();

    @Getter
    private static ProxyConfiguration proxyConfiguration;

    @Getter
    private Integer id;
    @Getter
    private String name, address;

    @Getter
    private Integer port;

    @Override
    public void onEnablePlugin() {
        Proxy.proxyConfiguration = new ProxyConfiguration();

        this.id = Proxy.proxyConfiguration.getId();
        this.name = Proxy.proxyConfiguration.getName();
        this.address = Proxy.proxyConfiguration.getAddress();
        this.port = this.getProxy().getConfig().getListeners()
                .stream()
                .findFirst()
                .get()
                .getQueryPort();

        new StartManager();

        this.markOnline();
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
        Proxy.broadcastMessage(group, message, new Preference[]{});
    }

    public static void broadcastMessage(Group group, String message, Preference... preferences) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("group_id", group.getId());
        jsonObject.put("message", message);

        JSONArray jsonArray = new JSONArray();

        Arrays.stream(preferences).forEach(preference -> jsonArray.add(preference.toString()));

        jsonObject.put("preferences", jsonArray);

        Common.getInstance().getDatabaseManager()
                .getRedisManager()
                .getDatabases()
                .values()
                .forEach(redis -> redis.sendMessage(
                        Constants.BROADCAST_MESSAGE,
                        jsonObject.toString()
                ));
    }

    private void markOnline() {
        DNSRecord dnsRecord = Proxy.getDNSRecord();

        if (dnsRecord == null) {
            if (CloudFlareAPI.createRecord(
                    CloudFlareAPI.DNSType.SRV,
                    Common.SERVER_URL,
                    Common.SERVER_URL,
                    this.port
            )) {
                Printer.INFO.coloredPrint("&aCreation of an DNS Record for this proxy successful.");
            } else {
                Printer.INFO.coloredPrint("&cAn internal error ocurred while creating DNS Record for this proxy, shutting down.");

                net.md_5.bungee.api.ProxyServer.getInstance().stop();
            }
        } else {
            Printer.INFO.coloredPrint("&cDNS record already created, please verify that.");
        }
    }

    private static DNSRecord getDNSRecord() {
        return CloudFlareAPI.listDNSRecords()
                .stream()
                .filter(dnsRecord1 -> dnsRecord1.getType().equalsIgnoreCase("SRV"))
                .filter(dnsRecord1 -> dnsRecord1.getValue().equals("0\t" + Proxy.getInstance().getPort() + "\t" + Common.SERVER_URL))
                .findFirst()
                .orElse(null);
    }

    public static void setOffline() {
        ProxyServer proxyServer = Proxy.getCurrentProxy();

        proxyServer.setStatus(false);

        proxyServer.getUsersId().forEach(userId -> {
            User user = UserManager.getUser(userId);

            user.setLogged(false);
        });

        proxyServer.setUsersId(Sets.newHashSet());

        ProxyServerDao proxyServerDao = new ProxyServerDao();

        proxyServerDao.update(proxyServer);

        DNSRecord dnsRecord = Proxy.getDNSRecord();

        if (dnsRecord != null) {
            Printer.INFO.coloredPrint("&eDNS Record found, deleting it.");

            if (CloudFlareAPI.deleteRecord(dnsRecord.getId())) {
                Printer.INFO.coloredPrint("&eDNS Record created successful.");
            } else {
                Printer.INFO.coloredPrint("&cAn internal error ocurred while deleting DNS Record for this proxy, shutting down.");

                net.md_5.bungee.api.ProxyServer.getInstance().stop();
            }
        } else {
            Printer.INFO.coloredPrint("&cDNS record already deleted, what's happening?");
        }
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
        AuthenticationManager.removeAttempt(user.getId());
        UserManager.removeUser(user.getId());
    }
}
