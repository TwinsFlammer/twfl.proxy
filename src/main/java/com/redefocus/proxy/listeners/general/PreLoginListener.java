package com.redefocus.proxy.listeners.general;

import com.redefocus.common.shared.language.enums.Language;
import com.redefocus.common.shared.permissions.user.dao.UserDao;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.permissions.user.group.dao.UserGroupDao;
import com.redefocus.common.shared.permissions.user.group.data.UserGroup;
import com.redefocus.common.shared.permissions.user.manager.UserManager;
import com.redefocus.common.shared.skin.data.Skin;
import com.redefocus.common.shared.skin.factory.SkinFactory;
import com.redefocus.common.shared.util.Helper;
import com.redefocus.proxy.Proxy;
import com.redefocus.proxy.maintenance.factory.MaintenanceFactory;
import com.redefocus.proxy.punish.data.Duration;
import com.redefocus.proxy.punish.data.PunishReason;
import com.redefocus.proxy.punish.data.Punishment;
import com.redefocus.proxy.punish.data.enums.PunishType;
import com.redefocus.proxy.punish.manager.PunishmentManager;
import com.redefocus.proxy.util.Messages;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import org.apache.commons.io.Charsets;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * Created by @SrGutyerrez
 */
public class PreLoginListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PreLoginEvent event) {
        PendingConnection pendingConnection = event.getConnection();

        if (!pendingConnection.getName().matches("[a-zA-Z0-9-_]*")) {
            event.setCancelReason(Messages.INVALID_USERNAME);
            event.setCancelled(true);
            return;
        }

        User user = UserManager.getUser(pendingConnection.getName());

        if (user == null) {
            UserDao userDao = new UserDao();

            user = UserManager.generateUser(
                    pendingConnection.getName(),
                    this.generateUUID(pendingConnection.getName())
            );

            userDao.insert(user);

            user = UserManager.getUser(pendingConnection.getName());

            if (user == null) {
                event.setCancelReason(Messages.INVALID_USER);
                event.setCancelled(true);
                return;
            }

            Skin skin = SkinFactory.getSkin(user.getDisplayName());

            if (skin != null) user.setSkin(skin);
        }

        UserGroupDao userGroupDao = new UserGroupDao();

        Set<UserGroup> userGroups = userGroupDao.findAll(
                user.getId(),
                ""
        );

        user.getGroups().addAll(userGroups);

        Punishment punishment = PunishmentManager.getPunishments(user)
                .stream()
                .filter(Objects::nonNull)
                .filter(Punishment::isActive)
                .filter(Punishment::isBan)
                .findFirst()
                .orElse(null);

        PunishmentManager.getPunishments(user)
                .stream()
                .filter(Objects::nonNull)
                .filter(this.predicate(Punishment::isStarted).negate())
                .forEach(Punishment::start);

        if (punishment != null) {
            Language language = user.getLanguage();

            Duration duration = punishment.getDuration();
            PunishType punishType = duration.getPunishType();
            PunishReason punishReason = punishment.getPunishReason();

            event.setCancelReason(
                    Helper.colorize(
                            String.format(
                                    language.getMessage("punishment.kick_message"),
                                    punishType.getDisplayName(),
                                    punishReason.getDisplayName(),
                                    punishment.getProof(),
                                    punishment.getStaffer().getDisplayName(),
                                    punishment.getId()
                            )
                    )
            );
            event.setCancelled(true);

            Proxy.unloadUser(user);
            return;
        }

        if (MaintenanceFactory.inMaintenance() && !user.hasGroup("helper")) {
            event.setCancelReason(Messages.IN_MAINTENANCE);
            event.setCancelled(true);

            Proxy.unloadUser(user);
            return;
        }
    }

    private <T> Predicate<T> predicate(Predicate<T> predicate) {
        return predicate;
    }

    private UUID generateUUID(String username) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(Charsets.UTF_8));
    }
}
