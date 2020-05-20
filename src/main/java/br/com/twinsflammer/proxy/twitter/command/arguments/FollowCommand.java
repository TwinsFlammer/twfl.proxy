package br.com.twinsflammer.proxy.twitter.command.arguments;

import br.com.twinsflammer.proxy.twitter.command.TwitterCommand;
import br.com.twinsflammer.proxy.twitter.command.TwitterConstants;
import br.com.twinsflammer.api.bungeecord.commands.CustomArgumentCommand;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
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

        Twitter userTwitter = user.getTwitter();

        if (targetName.equalsIgnoreCase("equipe")) {
            Integer followedUsers = 0;

            for (String masterUser : TwitterConstants.MASTER_USERS) {
                try {
                    userTwitter.createFriendship(masterUser);

                    followedUsers++;
                } catch (TwitterException exception) {
                    user.sendMessage(
                            String.format(
                                    language.getMessage("twitter.can\'t_follow_user"),
                                    masterUser
                            )
                    );
                }
            }
            if (followedUsers != 0) user.sendMessage(
                    language.getMessage("twitter.followed_masters")
            );
            return;
        }

        User user1 = UserManager.getUser(targetName);

        if (!user1.isTwitterAssociated()) {
            user.sendMessage(
                    language.getMessage("twitter.user_not_associated_an_twitter_account")
            );
            return;
        }

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
