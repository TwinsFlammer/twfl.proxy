package com.redecommunity.proxy.twitter.command.arguments;

import com.google.common.collect.Maps;
import com.redecommunity.api.bungeecord.commands.CustomArgumentCommand;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.dao.UserDao;
import com.redecommunity.common.shared.permissions.user.data.User;

import java.util.HashMap;

/**
 * Created by @SrGutyerrez
 */
public class DisassociateCommand extends CustomArgumentCommand {
    public DisassociateCommand() {
        super(0, "desassociar");
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

        user.setTwitterTokenSecret(null);
        user.setTwitterAccessToken(null);

        UserDao userDao = new UserDao();

        HashMap<String, String> keys = Maps.newHashMap();

        keys.put("twitter_access_token", null);
        keys.put("twitter_token_secret", null);

        userDao.update(
                keys,
                "id",
                user.getId()
        );

        user.sendMessage(
                language.getMessage("twitter.disassociated")
        );
    }
}
