package com.redefocus.proxy.listeners.general;

import com.redefocus.api.shared.connection.dao.ProxyServerDao;
import com.redefocus.api.shared.connection.data.ProxyServer;
import com.redefocus.common.shared.language.enums.Language;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.permissions.user.manager.UserManager;
import com.redefocus.common.shared.skin.data.Skin;
import com.redefocus.proxy.Proxy;
import com.redefocus.proxy.listeners.general.tablist.data.TabList;
import com.redefocus.proxy.listeners.general.tablist.manager.TabListManager;
import com.redefocus.proxy.skin.handler.SkinHandler;
import com.redefocus.proxy.user.ProxyUser;
import com.redefocus.proxy.util.Messages;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
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

        if (!proxiedPlayer.isConnected()) return;

        if (!this.isValidUUID(proxiedPlayer.getUniqueId(), proxiedPlayer.getName())) {
            proxiedPlayer.disconnect(Messages.INVALID_UUID_MESSAGE);
            return;
        }

        ProxyUser proxyUser = (ProxyUser) UserManager.getUser(proxiedPlayer.getUniqueId());
        Language language = proxyUser.getLanguage();

        if (proxyUser.isConsole()) {
            proxiedPlayer.disconnect(
                    language.getMessage("errors.console_player")
            );
            return;
        }

        ProxyServer proxyServer = Proxy.getCurrentProxy();

        proxyServer.getUsers().add(proxyUser);

        ProxyServerDao proxyServerDao = new ProxyServerDao();

        proxyServerDao.update(proxyServer);

        TabList tabList = proxyUser.hasGroup("manager") ? TabListManager.getStaffTabList(proxyUser) : TabListManager.getCurrentTabList(proxyUser);

        if (tabList != null)
            proxiedPlayer.setTabHeader(
                    new BaseComponent[]{
                            new TextComponent(tabList.getHeader())
                    },
                    new BaseComponent[]{
                            new TextComponent(tabList.getFooter())
                    }
            );

        Skin skin = proxyUser.getSkin();

        if (skin != null) {
            PendingConnection pendingConnection = proxiedPlayer.getPendingConnection();

            SkinHandler.changeLoginProperties(
                    pendingConnection,
                    skin
            );
        }

        proxyUser.sendTitle(
                Messages.PREFIX,
                "§fUtilize " + (proxyUser.isRegistered() ? "/logar <senha>" : "/registrar <senha> <e-mail>"),
                0,
                Integer.MAX_VALUE,
                0
        );
    }

    private Boolean isValidUUID(UUID uuid, String username) {
        return uuid.equals(UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(Charsets.UTF_8)));
    }
}
