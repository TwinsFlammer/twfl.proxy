package com.redefocus.proxy.twitter.command;

import com.redefocus.api.bungeecord.commands.CustomCommand;
import com.redefocus.api.bungeecord.commands.enums.CommandRestriction;
import com.redefocus.common.shared.language.enums.Language;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.proxy.twitter.command.arguments.*;
import com.redefocus.proxy.twitter.command.arguments.*;

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
