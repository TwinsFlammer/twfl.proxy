package com.redefocus.proxy.commands.defaults.staff.group;

import com.redefocus.api.bungeecord.commands.CustomCommand;
import com.redefocus.api.bungeecord.commands.enums.CommandRestriction;
import com.redefocus.common.shared.language.enums.Language;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.proxy.commands.defaults.staff.group.arguments.GroupAddCommand;
import com.redefocus.proxy.commands.defaults.staff.group.arguments.GroupRemoveCommand;

/**
 * Created by @SrGutyerrez
 */
public class GroupCommand extends CustomCommand {
    public static final String COMMAND_NAME = "grupo";

    public GroupCommand() {
        super(GroupCommand.COMMAND_NAME, CommandRestriction.ALL, "Manager");

        this.addArgument(
                new GroupAddCommand(),
                new GroupRemoveCommand()
        );
    }

    @Override
    public void onCommand(User user, String[] strings) {
        Language language = user.getLanguage();

        user.sendMessage(
            language.getMessage("messages.default_commands.groups.help_message")
        );
    }
}
