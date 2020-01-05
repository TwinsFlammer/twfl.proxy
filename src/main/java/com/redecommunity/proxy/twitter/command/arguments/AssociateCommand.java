package com.redecommunity.proxy.twitter.command.arguments;

import com.redecommunity.api.bungeecord.commands.CustomArgumentCommand;
import com.redecommunity.api.bungeecord.util.JSONText;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.twitter.manager.TwitterManager;
import twitter4j.TwitterException;

import java.net.MalformedURLException;
import java.net.URL;

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

        }
    }
}
