package com.redecommunity.proxy.twitter.command.arguments;

import com.google.common.collect.Maps;
import com.redecommunity.api.bungeecord.commands.CustomArgumentCommand;
import com.redecommunity.api.bungeecord.util.JSONText;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.dao.UserDao;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.twitter.database.TwitterDatabase;
import com.redecommunity.common.shared.twitter.manager.TwitterManager;
import com.redecommunity.proxy.twitter.command.TwitterCommand;
import org.json.simple.JSONObject;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by @SrGutyerrez
 */
public class AssociateCommand extends CustomArgumentCommand {
    public AssociateCommand() {
        super(0, "associar");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (user.isTwitterAssociated()) {
            user.sendMessage(
                    language.getMessage("twitter.already_associated")
            );
            return;
        }

        if (args.length == 0) {
            try {
                URL url = TwitterManager.getAuthorizationURL(user);

                JSONText jsonText = new JSONText();

                jsonText.next()
                        .text("§eNós geramos um link para que você possa associar seu twitter a sua conta.")
                        .next()
                        .text("§ePara ir até ele, clique ")
                        .next()
                        .text("§6§lAQUI")
                        .clickOpenURL(url.toString())
                        .next()
                        .text("§r")
                        .next()
                        .text("§e para abrir o link em seu navegador padrão.")
                        .next()
                        .send(user);
            } catch (TwitterException | MalformedURLException exception) {
                user.sendMessage(
                        language.getMessage("twitter.an_internal_error_occurred")
                );
            }
        } else if (args.length == 1) {
            String code = args[0];

            TwitterDatabase twitterDatabase = new TwitterDatabase();

            JSONObject jsonObject = twitterDatabase.findOne("user_id", user.getId());

            String requestCode = (String) jsonObject.get("request_code");

            if (!code.equals(requestCode)) {
                user.sendMessage(
                        language.getMessage("twitter.invalid_code")
                );
                return;
            }

            try {
                Twitter twitter = new TwitterFactory().getInstance();

                twitter.setOAuthConsumer(
                        TwitterManager.oAuthConsumerKey,
                        TwitterManager.oAuthConsumerSecret
                );

                RequestToken requestToken = TwitterManager.getRequestToken(user.getId());

                if (requestToken == null) {
                    user.sendMessage(
                            language.getMessage("twitter.association_expired")
                    );
                    return;
                }

                AccessToken accessToken = twitter.getOAuthAccessToken(requestToken);

                user.setTwitterAccessToken(accessToken.getToken());
                user.setTwitterTokenSecret(accessToken.getTokenSecret());

                UserDao userDao = new UserDao();

                HashMap<String, String> keys = Maps.newHashMap();

                keys.put("twitter_access_token", accessToken.getToken());
                keys.put("twitter_token_secret", accessToken.getTokenSecret());

                userDao.update(
                        keys,
                        "id",
                        user.getId()
                );

                user.sendMessage(
                        language.getMessage("twitter.associated")
                );

                twitterDatabase.delete("user_id", user.getId());

                TwitterManager.removeRequestToken(user.getId());
            } catch (TwitterException exception) {
                user.sendMessage(
                        language.getMessage("twitter.association_error_occurred")
                );
            }
        } else {
            TwitterCommand.sendUsage(
                    user,
                    "associar <código pin>"
            );
            return;
        }
    }
}