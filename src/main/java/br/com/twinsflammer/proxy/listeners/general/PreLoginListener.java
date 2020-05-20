package br.com.twinsflammer.proxy.listeners.general;

import br.com.twinsflammer.proxy.maintenance.factory.MaintenanceFactory;
import br.com.twinsflammer.proxy.punish.manager.PunishmentManager;
import br.com.twinsflammer.proxy.util.Messages;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.user.dao.UserDao;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.group.dao.UserGroupDao;
import br.com.twinsflammer.common.shared.permissions.user.group.data.UserGroup;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.common.shared.skin.data.Skin;
import br.com.twinsflammer.common.shared.skin.factory.SkinFactory;
import br.com.twinsflammer.common.shared.util.Helper;
import br.com.twinsflammer.proxy.Proxy;
import br.com.twinsflammer.proxy.punish.data.Duration;
import br.com.twinsflammer.proxy.punish.data.PunishReason;
import br.com.twinsflammer.proxy.punish.data.Punishment;
import br.com.twinsflammer.proxy.punish.data.enums.PunishType;
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

        User user = UserManager.getUser(pendingConnection.getName(), true);

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
                                    punishment.getFancyProof(),
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
