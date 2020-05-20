package br.com.twinsflammer.proxy.commands.defaults.players.ignore;

import br.com.twinsflammer.proxy.commands.defaults.players.ignore.arguments.IgnoreAddCommand;
import br.com.twinsflammer.proxy.commands.defaults.players.ignore.arguments.IgnoreListCommand;
import br.com.twinsflammer.proxy.commands.defaults.players.ignore.arguments.IgnoreRemoveCommand;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.api.bungeecord.commands.CustomCommand;
import br.com.twinsflammer.api.bungeecord.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.user.data.User;

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
