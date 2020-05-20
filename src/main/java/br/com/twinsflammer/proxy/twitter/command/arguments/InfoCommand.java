package br.com.twinsflammer.proxy.twitter.command.arguments;

import br.com.twinsflammer.api.bungeecord.commands.CustomArgumentCommand;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.user.data.User;

/**
 * Created by @SrGutyerrez
 */
public class InfoCommand extends CustomArgumentCommand {
    public InfoCommand() {
        super(0, "info");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        user.sendMessage(
                language.getMessage("twitter.not_implemented")
        );
    }
}
