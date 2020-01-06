package com.redecommunity.proxy.twitter.command.arguments;

import com.redecommunity.api.bungeecord.commands.CustomArgumentCommand;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.proxy.twitter.command.TwitterConstants;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.Arrays;

/**
 * Created by @SrGutyerrez
 */
public class FollowMastersCommand extends CustomArgumentCommand {
    public FollowMastersCommand() {
        super(0, "seguirmasters");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (!user.isTwitterAssociated()) {
            user.sendMessage(
                    language.getMessage("twitter.not_associated")
            );
            return;
        }

        Twitter twitter = user.getTwitter();

        Arrays.stream(TwitterConstants.MASTER_USERS).forEach(masterUser -> {
            try {
                twitter.createFriendship(masterUser);
            } catch (TwitterException exception) {
                user.sendMessage(
                        String.format(
                                language.getMessage("twitter.can\'t_follow_user"),
                                masterUser
                        )
                );
            }
        });

        user.sendMessage(
                language.getMessage("twitter.followed_masters")
        );
    }
}
