package com.redefocus.proxy.shorter.command;

import com.redefocus.api.bungeecord.commands.CustomCommand;
import com.redefocus.api.bungeecord.commands.enums.CommandRestriction;
import com.redefocus.common.shared.language.enums.Language;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.util.Helper;
import com.redefocus.proxy.shorter.command.arguments.ShortDisableCommand;
import com.redefocus.proxy.shorter.command.arguments.ShortEnableCommand;
import com.redefocus.proxy.shorter.dao.ShortedURLDao;
import com.redefocus.proxy.shorter.data.ShortedURL;
import com.redefocus.proxy.shorter.manager.ShortedURLManager;

/**
 * Created by @SrGutyerrez
 */
public class ShortCommand extends CustomCommand {
    public static final String COMMAND_NAME = "short";

    public ShortCommand() {
        super(ShortCommand.COMMAND_NAME, CommandRestriction.ALL, GroupNames.HELPER);

        this.addArgument(
                new ShortDisableCommand(),
                new ShortEnableCommand()
        );
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length == 0 || args.length > 2) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            this.getName(),
                            "<link>"
                    )
            );
            return;
        }

        String link = args[0];

        if (!Helper.isURLValid(link)) {
            user.sendMessage(
                    language.getMessage("shortener.invalid_url")
            );
            return;
        }

        String name = args.length == 2 ? args[1] : Helper.generateString(8);

        if (args.length == 2 && !user.hasGroup("manager")) {
            user.sendMessage(
                    language.getMessage("shortener.too_many_arguments")
            );
            return;
        }

        if (ShortedURLManager.getShortedURL(name) != null) {
            user.sendMessage(
                    language.getMessage("shortener.already_shorted_with_this_name")
            );
            return;
        }

        ShortedURL shortedURL = new ShortedURL(
                0,
                link,
                name,
                user.getId(),
                System.currentTimeMillis(),
                true
        );

        ShortedURLDao shortedURLDao = new ShortedURLDao();

        shortedURLDao.insert(shortedURL);

        user.sendMessage(
                String.format(
                        language.getMessage("shortener.shorted"),
                        shortedURL.getUrl(),
                        shortedURL.getName()
                )
        );
    }
}
