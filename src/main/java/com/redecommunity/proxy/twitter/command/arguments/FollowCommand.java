package com.redecommunity.proxy.twitter.command.arguments;

import com.redecommunity.api.bungeecord.commands.CustomArgumentCommand;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.proxy.twitter.command.TwitterCommand;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by @SrGutyerrez
 */
public class FollowCommand extends CustomArgumentCommand {
    public FollowCommand() {
        super(0, "seguir");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length != 1) {
            TwitterCommand.sendUsage(
                    user,
                    "seguir <usuário>"
            );
            return;
        }

        if (!user.isTwitterAssociated()) {
            user.sendMessage(
                    language.getMessage("twitter.not_associated")
            );
            return;
        }

        String targetName = args[0];

        User user1 = UserManager.getUser(targetName);

        if (!user1.isTwitterAssociated()) {
            user.sendMessage(
                    language.getMessage("twitter.user_not_associated_an_twitter_account")
            );
            return;
        }

        Twitter userTwitter = user.getTwitter();
        Twitter targetTwitter = user1.getTwitter();

        try {
            Long targetTwitterId = targetTwitter.getId();

            userTwitter.createFriendship(targetTwitterId);

            user.sendMessage(
                    String.format(
                            language.getMessage("twitter.followed_user"),
                            user1.getDisplayName()
                    )
            );
        } catch (TwitterException exception) {
            exception.printStackTrace();
        }
    }
}
