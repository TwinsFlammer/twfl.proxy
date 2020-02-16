package com.redefocus.proxy.twitter.command.arguments;

import com.redefocus.api.bungeecord.commands.CustomArgumentCommand;
import com.redefocus.common.shared.language.enums.Language;
import com.redefocus.common.shared.permissions.user.data.User;

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
