package com.redecommunity.proxy.listeners.general;

import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.skin.data.Skin;
import com.redecommunity.proxy.connection.dao.ProxyServerDao;
import com.redecommunity.proxy.connection.data.ProxyServer;
import com.redecommunity.proxy.connection.manager.ProxyServerManager;
import com.redecommunity.proxy.listeners.general.tablist.data.TabList;
import com.redecommunity.proxy.listeners.general.tablist.manager.TabListManager;
import com.redecommunity.proxy.skin.handler.SkinHandler;
import com.redecommunity.proxy.util.Messages;
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

        User user = UserManager.getUser(proxiedPlayer.getUniqueId());
        Language language = user.getLanguage();

        if (user.isConsole()) {
            proxiedPlayer.disconnect(
                    language.getMessage("errors.console_player")
            );
            return;
        }

        ProxyServer proxyServer = ProxyServerManager.getCurrentProxy();

        proxyServer.getUsers().add(user);

        ProxyServerDao proxyServerDao = new ProxyServerDao();

        proxyServerDao.update(proxyServer);

        TabList tabList = user.hasGroup("manager") ? TabListManager.getStaffTabList(user) : TabListManager.getCurrentTabList(user);

        if (tabList != null)
            proxiedPlayer.setTabHeader(
                    new BaseComponent[]{
                            new TextComponent(tabList.getHeader())
                    },
                    new BaseComponent[]{
                            new TextComponent(tabList.getFooter())
                    }
            );

        Skin skin = user.getSkin();

        if (skin != null) {
            PendingConnection pendingConnection = proxiedPlayer.getPendingConnection();

            SkinHandler.changeLoginProperties(
                    pendingConnection,
                    skin
            );
        }
    }

    private Boolean isValidUUID(UUID uuid, String username) {
        return uuid.equals(UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(Charsets.UTF_8)));
    }
}
