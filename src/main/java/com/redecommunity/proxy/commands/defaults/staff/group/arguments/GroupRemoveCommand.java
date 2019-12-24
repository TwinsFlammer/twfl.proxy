package com.redecommunity.proxy.commands.defaults.staff.group.arguments;

import com.redecommunity.api.bungeecord.commands.CustomArgumentCommand;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.group.data.Group;
import com.redecommunity.common.shared.permissions.group.manager.GroupManager;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.group.dao.UserGroupDao;
import com.redecommunity.common.shared.permissions.user.group.data.UserGroup;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.server.data.Server;
import com.redecommunity.common.shared.server.manager.ServerManager;
import com.redecommunity.common.shared.util.Helper;
import com.redecommunity.proxy.commands.defaults.staff.group.GroupCommand;

import java.util.Objects;
import java.util.Set;

/**
 * Created by @SrGutyerrez
 */
public class GroupRemoveCommand extends CustomArgumentCommand {
    public GroupRemoveCommand() {
        super(0, "remover");
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

        Integer serverId = Integer.parseInt(targetServerPreId);

        Server server = ServerManager.getServer(serverId);

        if (server == null && serverId != 0) {
            user.sendMessage(
                    language.getMessage("messages.default_commands.groups.invalid_server")
            );
            return;
        }

        UserGroupDao userGroupDao = new UserGroupDao();

        if (user1.getGroups().isEmpty()) {
            Set<UserGroup> userGroups = userGroupDao.findAll(
                    user1.getId(),
                    "AND `server_id`=" + (server == null ? 0 : server.getId())
            );

            user1.getGroups().addAll(userGroups);
        }

        UserGroup userGroup = user1.getGroups()
                .stream()
                .filter(Objects::nonNull)
                .filter(userGroup1 -> userGroup1.getGroup().getId().equals(group.getId()))
                .filter(userGroup1 -> serverId == 0 ? userGroup1.getServer() == null : userGroup1.getServer().getId().equals(server.getId()))
                .findFirst()
                .orElse(null);

        if (userGroup == null) {
            user.sendMessage(
                    language.getMessage("messages.default_commands.groups.hasn\'t_that_group")
            );
            return;
        }


        userGroupDao.delete(
                user1,
                userGroup
        );

        user.sendMessage(
                String.format(
                        language.getMessage("messages.default_commands.groups.user_removed_from_group"),
                        user1.getDisplayName(),
                        group.getName()
                )
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
