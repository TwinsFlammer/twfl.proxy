package br.com.twinsflammer.proxy.twitter.command;

import br.com.twinsflammer.proxy.twitter.command.arguments.*;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.proxy.twitter.command.arguments.*;
import br.com.twinsflammer.api.bungeecord.commands.CustomCommand;
import br.com.twinsflammer.api.bungeecord.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.user.data.User;

/**
 * Created by @SrGutyerrez
 */
public class TwitterCommand extends CustomCommand {
    public static final String COMMAND_NAME = "twitter";

    public TwitterCommand() {
        super(TwitterCommand.COMMAND_NAME, CommandRestriction.IN_GAME, GroupNames.DEFAULT);

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
