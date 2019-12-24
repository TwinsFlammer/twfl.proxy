package com.redecommunity.proxy.commands.defaults.staff;

import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.util.Helper;
import com.redecommunity.proxy.Proxy;

import java.util.Objects;

/**
 * Created by @SrGutyerrez
 */
public class StaffChatCommand extends CustomCommand {
    public StaffChatCommand() {
        super("s", CommandRestriction.IN_GAME, "helper");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length == 0) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            this.getName(),
                            "<mensagem>"
                    )
            );
            return;
        }

        String message = Helper.toMessage(args);

        Proxy.getUsers()
                .stream()
                .filter(Objects::nonNull)
                .filter(user1 -> user1.hasGroup("helper"))
                .forEach(user1 -> {
                    Language language1 = user.getLanguage();

                    user1.sendMessage(
                            String.format(
                                    language1.getMessage("staff_chat.format"),
                                    user1.getServer().getDisplayName(),
                                    user1.getPrefix() + user1.getDisplayName(),
                                    message
                            )
                    );
                });
    }
}
