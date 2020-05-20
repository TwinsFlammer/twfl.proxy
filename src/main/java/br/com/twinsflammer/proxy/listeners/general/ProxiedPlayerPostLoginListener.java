package br.com.twinsflammer.proxy.listeners.general;

import br.com.twinsflammer.proxy.listeners.general.tablist.data.TabList;
import br.com.twinsflammer.proxy.listeners.general.tablist.manager.TabListManager;
import br.com.twinsflammer.api.shared.connection.dao.ProxyServerDao;
import br.com.twinsflammer.api.shared.connection.data.ProxyServer;
import br.com.twinsflammer.common.shared.Common;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.common.shared.skin.data.Skin;
import br.com.twinsflammer.proxy.Proxy;
import br.com.twinsflammer.proxy.skin.handler.SkinHandler;
import br.com.twinsflammer.proxy.util.Messages;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.apache.commons.io.Charsets;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

        ProxyServer proxyServer = Proxy.getCurrentProxy();

        proxyServer.getUsersId().removeIf(userId -> userId.equals(user.getId()));

        proxyServer.getUsersId().add(user.getId());

        ProxyServerDao proxyServerDao = new ProxyServerDao();

        proxyServerDao.update(proxyServer);

        TabList tabList = user.hasGroup(GroupNames.MANAGER) ? TabListManager.getStaffTabList(user) : TabListManager.getCurrentTabList(user);

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

        Common.getInstance().getScheduler().schedule(
                () -> {
                    user.sendTitle(
                            Messages.PREFIX,
                            "§fUtilize " + (user.isRegistered() ? "/logar <senha>" : "/registrar <senha> <e-mail>"),
                            0,
                            Integer.MAX_VALUE,
                            0
                    );
                },
                1,
                TimeUnit.SECONDS
        );
    }

    private Boolean isValidUUID(UUID uuid, String username) {
        return uuid.equals(UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(Charsets.UTF_8)));
    }
}
