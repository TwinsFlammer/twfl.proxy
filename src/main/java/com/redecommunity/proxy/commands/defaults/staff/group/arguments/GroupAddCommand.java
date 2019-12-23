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
import org.json.simple.JSONObject;

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

        if (args.length != 5) {
            this.sendUsage(
                    language,
                    user
            );
            return;
        }

        String targetName = args[1];
        String targetGroupName = args[2];
        String targetServerPreId = args[3];
        String targetPreTime = args[4];

        User user1 = UserManager.getUser(targetName);

        if (user1 == null) {
            user.sendMessage(
                    language.getMessage("messages.player.invalid_player")
            );
            return;
        }

        if (!user1.isOnline()) {
            user.sendMessage(
                    language.getMessage("messages.player.player_offline")
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

        Long duration = TimeUnit.DAYS.toMillis(Long.parseLong(targetPreTime));

        UserGroupDao userGroupDao = new UserGroupDao();

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("user_id", user1.getId());
        jsonObject.put("group_id", group.getId());
        jsonObject.put("server_id", server == null ? 0 : server.getId());
        jsonObject.put("duration", duration);

//        userGroupDao.insert(jsonObject);

        user.sendMessage(
                language.getMessage("messages.default_commands.groups.user_added_to_group")
        );

        UserGroup userGroup = new UserGroup(
                group,
                server,
                duration
        );

        if (user.isOnline()) user.getGroups().add(userGroup);
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
