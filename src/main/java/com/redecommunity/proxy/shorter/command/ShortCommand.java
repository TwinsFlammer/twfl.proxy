package com.redecommunity.proxy.shorter.command;

import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.util.Helper;
import com.redecommunity.proxy.shorter.command.arguments.ShortDisableCommand;
import com.redecommunity.proxy.shorter.command.arguments.ShortEnableCommand;
import com.redecommunity.proxy.shorter.data.ShortedURL;

/**
 * Created by @SrGutyerrez
 */
public class ShortCommand extends CustomCommand {
    public static final String COMMAND_NAME = "short";

    public ShortCommand() {
        super(ShortCommand.COMMAND_NAME, CommandRestriction.IN_GAME, "helper");

        this.addArgument(
                new ShortDisableCommand(),
                new ShortEnableCommand()
        );
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length < 1 || args.length < 2) {
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

        ShortedURL shortedURL = new ShortedURL(
                0,
                link,
                name,
                user.getId(),
                System.currentTimeMillis(),
                true
        );


        user.sendMessage(
                String.format(
                        language.getMessage("shortener.shorted"),
                        shortedURL.getUrl(),
                        shortedURL.getName()
                )
        );
    }
}
