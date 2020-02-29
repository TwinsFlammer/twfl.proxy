package com.redecommunity.proxy.commands.defaults.players.ignore;

import com.redecommunity.proxy.commands.defaults.players.ignore.arguments.IgnoreAddCommand;
import com.redecommunity.proxy.commands.defaults.players.ignore.arguments.IgnoreListCommand;
import com.redecommunity.proxy.commands.defaults.players.ignore.arguments.IgnoreRemoveCommand;
import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;

/**
 * Created by @SrGutyerrez
 */
public class IgnoreCommand extends CustomCommand {
    public static final String COMMAND_NAME = "ignorar";

    public IgnoreCommand() {
        super(IgnoreCommand.COMMAND_NAME, CommandRestriction.IN_GAME, "default");

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
