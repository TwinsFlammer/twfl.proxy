package com.redecommunity.proxy.commands.defaults.staff.group;

import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.api.bungeecord.util.JSONText;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.group.GroupNames;
import com.redecommunity.common.shared.permissions.group.data.Group;
import com.redecommunity.common.shared.permissions.group.manager.GroupManager;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.server.data.Server;
import com.redecommunity.proxy.commands.defaults.staff.group.arguments.GroupAddCommand;
import com.redecommunity.proxy.commands.defaults.staff.group.arguments.GroupRemoveCommand;

import java.util.List;

/**
 * Created by @SrGutyerrez
 */
public class GroupCommand extends CustomCommand {
    public static final String COMMAND_NAME = "grupo";

    public GroupCommand() {
        super(GroupCommand.COMMAND_NAME, CommandRestriction.ALL, GroupNames.MANAGER);

        this.addArgument(
                new GroupAddCommand(),
                new GroupRemoveCommand()
        );
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length == 1) {
            String targetUsername = args[0];

            User user1 = UserManager.getUser(targetUsername);

            if (user1 == null) {
                user.sendMessage(
                        language.getMessage("messages.default_commands.users.invalid_user")
                );
                return;
            }

            Server server = user.getServer();
            List<Group> groups = GroupManager.getGroups();

            JSONText jsonText = new JSONText();

            jsonText.text("\n§eLista de grupos disponíveis (" + groups.size() + "):\n\n")
                    .next();

            groups.stream()
                    .filter(group -> !group.isDefault())
                    .sorted((group1, group2) -> group2.getPriority().compareTo(group1.getPriority()))
                    .forEach(group -> {
                        jsonText.text("  - " + group.getFancyPrefix() + " ")
                                .next()
                                .text("§7[Adicionar]")
                                .clickSuggest("/grupo adicionar " + user1.getDisplayName() + " " + group.getName().toUpperCase() + " 0 -1")
                                .next()
                                .text(" ")
                                .next()
                                .text("§7[Remover]")
                                .clickSuggest("/grupo remover " + user1.getDisplayName() + " " + group.getName().toUpperCase() + " 0")
                                .next()
                                .text(" ")
                                .next()
                                .text("§7[" + server.getDisplayName() + "]")
                                .hoverText("§7Clique para " + (user1.hasGroup(group, server) ? "adicionar" : "remover") +
                                        "\n" +
                                        "§7Esse grupo ao usuário " + user1.getDisplayName() + ".")
                                .clickSuggest("/grupo " + (user1.hasGroup(group, server) ? "adicionar" : "remover") + " " + group.getName().toUpperCase() + (user1.hasGroup(group, server) ? "" : " " + server.getId()))
                                .next()
                                .text("\n")
                                .next();
                    });

            jsonText.send(user);
        } else {
            user.sendMessage(
                    language.getMessage("messages.default_commands.groups.help_message")
            );
        }
    }
}
