package com.redecommunity.proxy.twitter.command;

import com.redecommunity.proxy.twitter.command.arguments.*;
import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.proxy.twitter.command.arguments.*;

/**
 * Created by @SrGutyerrez
 */
public class TwitterCommand extends CustomCommand {
    public static final String COMMAND_NAME = "twitter";

    public TwitterCommand() {
        super(TwitterCommand.COMMAND_NAME, CommandRestriction.IN_GAME, "default");

        this.addArgument(
                new AssociateCommand(),
                new DisassociateCommand(),
                new FollowCommand(),
                new InfoCommand(),
                new TweetCommand()
        );
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        user.sendMessage(
                language.getMessage("twitter.usage")
        );
    }

    public static void sendUsage(User user, String usage) {
        Language language = user.getLanguage();

        user.sendMessage(
                String.format(
                        language.getMessage("messages.default_commands.invalid_usage"),
                        TwitterCommand.COMMAND_NAME,
                        usage
                )
        );
    }
}
