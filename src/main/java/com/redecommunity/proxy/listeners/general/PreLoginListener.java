package com.redecommunity.proxy.listeners.general;

import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.dao.UserDao;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.util.Helper;
import com.redecommunity.proxy.Proxy;
import com.redecommunity.proxy.maintenance.factory.MaintenanceFactory;
import com.redecommunity.proxy.punish.data.Duration;
import com.redecommunity.proxy.punish.data.PunishReason;
import com.redecommunity.proxy.punish.data.Punishment;
import com.redecommunity.proxy.punish.data.enums.PunishType;
import com.redecommunity.proxy.punish.manager.PunishmentManager;
import com.redecommunity.proxy.util.Messages;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import org.apache.commons.io.Charsets;

import java.util.Objects;
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
        }

        user = UserManager.getUser(pendingConnection.getName());

        if (user == null) {
            event.setCancelReason(Messages.INVALID_USER);
            event.setCancelled(true);
            return;
        }

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
