package com.redefocus.proxy.twitter.command.arguments;

import com.google.common.collect.Maps;
import com.redefocus.proxy.twitter.command.TwitterCommand;
import com.redefocus.api.bungeecord.commands.CustomArgumentCommand;
import com.redefocus.api.bungeecord.util.JSONText;
import com.redefocus.common.shared.language.enums.Language;
import com.redefocus.common.shared.permissions.user.dao.UserDao;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.twitter.storage.TwitterStorage;
import com.redefocus.common.shared.twitter.manager.TwitterManager;
import org.json.simple.JSONObject;
import twitter4j.Twitter;
import twitter4j.TwitterException;
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
                        .text("§eNós geramos um link para você associar seu perfil do twitter.")
                        .next()
                        .text("\n")
                        .next()
                        .text("§ePara ir até ele, clique ")
                        .next()
                        .text("§6§lAQUI")
                        .clickOpenURL(url.toString())
                        .next()
                        .text("§r")
                        .next()
                        .text("§e para abrir o link em seu navegador.")
                        .next()
                        .send(user);
            } catch (TwitterException | MalformedURLException exception) {
                user.sendMessage(
                        language.getMessage("twitter.an_internal_error_occurred")
                );
            }
        } else if (args.length == 1) {
            String code = args[0];

            TwitterStorage twitterStorage = new TwitterStorage();

            JSONObject jsonObject = twitterStorage.findOne("user_id", user.getId());

            if (jsonObject == null) {
                user.sendMessage(
                        language.getMessage("twitter.association_expired")
                );
                return;
            }

            String requestCode = (String) jsonObject.get("generated_pin");
            String oAuthVerifier = (String) jsonObject.get("oauth_verifier");

            if (!code.equals(requestCode)) {
                user.sendMessage(
                        language.getMessage("twitter.invalid_code")
                );
                return;
            }

            twitterStorage.delete("user_id", user.getId());

            try {
                Twitter twitter = TwitterManager.getDefaultTwitter();

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

                AccessToken accessToken = twitter.getOAuthAccessToken(
                        requestToken,
                        oAuthVerifier
                );

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
                TwitterManager.removeRequestToken(user.getId());
            } catch (TwitterException exception) {
                TwitterManager.removeRequestToken(user.getId());
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
