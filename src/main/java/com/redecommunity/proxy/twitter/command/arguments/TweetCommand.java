package com.redecommunity.proxy.twitter.command.arguments;

import com.redecommunity.api.bungeecord.commands.CustomArgumentCommand;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;

/**
 * Created by @SrGutyerrez
 */
public class TweetCommand extends CustomArgumentCommand {
    public TweetCommand() {
        super(0, "tuitar");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        user.sendMessage(
                language.getMessage("twitter.not_implemented")
        );
    }
}
