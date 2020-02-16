package com.redefocus.proxy.shorter.command.arguments;

import com.google.common.collect.Maps;
import com.redefocus.api.bungeecord.commands.CustomArgumentCommand;
import com.redefocus.common.shared.language.enums.Language;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.proxy.shorter.command.ShortCommand;
import com.redefocus.proxy.shorter.dao.ShortedURLDao;
import com.redefocus.proxy.shorter.data.ShortedURL;
import com.redefocus.proxy.shorter.manager.ShortedURLManager;

import java.util.HashMap;

/**
 * Created by @SrGutyerrez
 */
public class ShortDisableCommand extends CustomArgumentCommand {
    public ShortDisableCommand() {
        super(0, "disable");
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

        keys.put("active", false);

        shortedURLDao.update(keys, "id", shortedURL.getId());

        shortedURL.setActive(false);

        user.sendMessage(
                language.getMessage("shortener.disabled")
        );
    }
}
