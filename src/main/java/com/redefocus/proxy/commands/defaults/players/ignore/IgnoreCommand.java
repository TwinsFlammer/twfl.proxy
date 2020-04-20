package com.redefocus.proxy.commands.defaults.players.ignore;

import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.proxy.commands.defaults.players.ignore.arguments.IgnoreAddCommand;
import com.redefocus.proxy.commands.defaults.players.ignore.arguments.IgnoreListCommand;
import com.redefocus.proxy.commands.defaults.players.ignore.arguments.IgnoreRemoveCommand;
import com.redefocus.api.bungeecord.commands.CustomCommand;
import com.redefocus.api.bungeecord.commands.enums.CommandRestriction;
import com.redefocus.common.shared.language.enums.Language;
import com.redefocus.common.shared.permissions.user.data.User;

/**
 * Created by @SrGutyerrez
 */
public class IgnoreCommand extends CustomCommand {
    public static final String COMMAND_NAME = "ignorar";

    public IgnoreCommand() {
        super(IgnoreCommand.COMMAND_NAME, CommandRestriction.IN_GAME, GroupNames.DEFAULT);

        this.addArgument(
                new IgnoreAddCommand(),
                new IgnoreListCommand(),
                new IgnoreRemoveCommand()
        );
    }

    @Override
    public void onCommand(User user, String[] strings) {
        Language language = user.getLanguage();

        user.sendMessage(
                language.getMessage("ignore.usage")
        );
    }
}
