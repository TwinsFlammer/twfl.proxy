package br.com.twinsflammer.proxy.twitter.command.arguments;

import br.com.twinsflammer.api.bungeecord.commands.CustomArgumentCommand;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by @SrGutyerrez
 */
public class TweetCommand extends CustomArgumentCommand {
    public TweetCommand() {
        super(0, "tuitar");
    }

    private final String DEFAULT_TWEET_TEXT = "Ah, slá eu me sinto meio sem sentido atualmente...";

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        Twitter twitter = user.getTwitter();

        try {
            twitter.updateStatus(this.DEFAULT_TWEET_TEXT);

            user.sendMessage(
                    language.getMessage("twitter.server_message_tweeted")
            );
        } catch (TwitterException exception) {
            user.sendMessage(
                    language.getMessage("twitter.an_internal_error_occurred")
            );
        }
    }
}
