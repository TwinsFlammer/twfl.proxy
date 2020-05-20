package br.com.twinsflammer.proxy.commands.defaults.staff.group;

import br.com.twinsflammer.proxy.commands.defaults.staff.group.arguments.GroupAddCommand;
import br.com.twinsflammer.proxy.commands.defaults.staff.group.arguments.GroupRemoveCommand;
import br.com.twinsflammer.api.bungeecord.commands.CustomCommand;
import br.com.twinsflammer.api.bungeecord.commands.enums.CommandRestriction;
import br.com.twinsflammer.api.bungeecord.util.JSONText;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.group.data.Group;
import br.com.twinsflammer.common.shared.permissions.group.manager.GroupManager;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.common.shared.server.data.Server;

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
