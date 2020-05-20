package br.com.twinsflammer.proxy.commands.defaults.players.ignore.arguments;

import br.com.twinsflammer.api.bungeecord.commands.CustomArgumentCommand;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.proxy.commands.defaults.players.ignore.IgnoreCommand;

/**
 * Created by @SrGutyerrez
 */
public class IgnoreRemoveCommand extends CustomArgumentCommand {
    public IgnoreRemoveCommand() {
        super(0, "remover");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length != 1) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            IgnoreCommand.COMMAND_NAME,
                            "remover <usuário>"
                    )
            );
            return;
        }

        String targetName = args[0];

        User user1 = UserManager.getUser(targetName);

        if (user1 == null) {
            user.sendMessage(
                    language.getMessage("messages.player.invalid_player")
            );
            return;
        }

        if (!user.isIgnoring(user1)) {
            user.sendMessage(
                    language.getMessage("ignore.not_ignoring")
            );
            return;
        }

        user.unIgnore(user1);

        user.sendMessage(
                String.format(
                        language.getMessage("ignore.un_ignored_user"),
                        user1.getDisplayName()
                )
        );
    }
}
