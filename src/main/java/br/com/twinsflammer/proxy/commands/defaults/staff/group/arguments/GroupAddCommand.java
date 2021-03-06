package br.com.twinsflammer.proxy.commands.defaults.staff.group.arguments;

import br.com.twinsflammer.proxy.commands.defaults.staff.group.GroupCommand;
import br.com.twinsflammer.api.bungeecord.commands.CustomArgumentCommand;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.group.data.Group;
import br.com.twinsflammer.common.shared.permissions.group.manager.GroupManager;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.group.dao.UserGroupDao;
import br.com.twinsflammer.common.shared.permissions.user.group.data.UserGroup;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.common.shared.server.data.Server;
import br.com.twinsflammer.common.shared.server.manager.ServerManager;
import br.com.twinsflammer.common.shared.util.Helper;
import br.com.twinsflammer.proxy.commands.defaults.staff.group.handler.GroupHandler;

import java.util.concurrent.TimeUnit;

/**
 * Created by @SrGutyerrez
 */
public class GroupAddCommand extends CustomArgumentCommand {
    public GroupAddCommand() {
        super(0, "adicionar");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length != 4) {
            this.sendUsage(
                    language,
                    user
            );
            return;
        }

        String targetName = args[0];
        String targetGroupName = args[1];
        String targetServerPreId = args[2];
        String targetPreTime = args[3];

        User user1 = UserManager.getUser(targetName);

        if (user1 == null) {
            user.sendMessage(
                    language.getMessage("messages.player.invalid_player")
            );
            return;
        }

        if (!Helper.isInteger(targetServerPreId)) {
            this.sendUsage(
                    language,
                    user
            );
            return;
        }

        Group group = GroupManager.getGroup(targetGroupName);

        if (group == null) {
            user.sendMessage(
                    language.getMessage("messages.default_commands.groups.unknown_group")
            );
            return;
        }

        if (!user.hasGroup(group)) {
            user.sendMessage(
                    language.getMessage("messages.default_commands.groups.insufficient_group")
            );
            return;
        }

        if (!Helper.isInteger(targetPreTime)) {
            user.sendMessage(
                    language.getMessage("messages.default_commands.groups.invalid_duration")
            );
            return;
        }

        Integer serverId = Integer.parseInt(targetServerPreId);

        Server server = ServerManager.getServer(serverId);

        if (server == null && serverId != 0) {
            user.sendMessage(
                    language.getMessage("messages.default_commands.groups.invalid_server")
            );
            return;
        }

        Long duration = Integer.parseInt(targetPreTime) == -1 ? -1 : TimeUnit.DAYS.toMillis(Long.parseLong(targetPreTime));

        UserGroupDao userGroupDao = new UserGroupDao();

        user.sendMessage(
                String.format(
                        language.getMessage("messages.default_commands.groups.user_added_to_group"),
                        user1.getDisplayName(),
                        group.getName(),
                        duration == -1 ? "infinitos" : targetPreTime
                )
        );

        UserGroup userGroup = new UserGroup(
                group,
                server,
                duration
        );

        userGroupDao.insert(
                user1,
                userGroup
        );

        GroupHandler.update(
                user1,
                group,
                serverId,
                duration,
                false
        );
    }

    private void sendUsage(Language language, User user) {
        user.sendMessage(
                String.format(
                        language.getMessage("messages.default_commands.invalid_usage"),
                        GroupCommand.COMMAND_NAME,
                        this.getName() + " <usuário> <grupo> <servidor> <tempo>"
                )
        );
    }
}
