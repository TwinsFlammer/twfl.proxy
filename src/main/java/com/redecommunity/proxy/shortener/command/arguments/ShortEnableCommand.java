package com.redecommunity.proxy.shortener.command.arguments;

import com.google.common.collect.Maps;
import com.redecommunity.api.bungeecord.commands.CustomArgumentCommand;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.proxy.shortener.command.ShortCommand;
import com.redecommunity.proxy.shortener.dao.ShortedURLDao;
import com.redecommunity.proxy.shortener.data.ShortedURL;
import com.redecommunity.proxy.shortener.manager.ShortedURLManager;

import java.util.HashMap;

/**
 * Created by @SrGutyerrez
 */
public class ShortEnableCommand extends CustomArgumentCommand {
    public ShortEnableCommand() {
        super(0, "enable");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length != 1) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            ShortCommand.COMMAND_NAME,
                            "<nome>"
                    )
            );
        }

        String name = args[0];

        ShortedURL shortedURL = ShortedURLManager.getShortedURL(name);

        if (shortedURL == null) {
            user.sendMessage(
                    language.getMessage("shortener.unknown_shorted_url")
            );
            return;
        }

        ShortedURLDao shortedURLDao = new ShortedURLDao();

        HashMap<String, Object> keys = Maps.newHashMap();

        keys.put("active", true);

        shortedURLDao.update(keys, "id", shortedURL.getId());

        shortedURL.setActive(true);

        user.sendMessage(
                language.getMessage("shortener.disabled")
        );
    }
}
