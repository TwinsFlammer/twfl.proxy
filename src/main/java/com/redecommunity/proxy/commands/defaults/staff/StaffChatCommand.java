package com.redecommunity.proxy.commands.defaults.staff;

import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.api.bungeecord.util.JSONText;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.util.Helper;
import com.redecommunity.proxy.Proxy;

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

        String broadcastMessage = String.format(
                language.getMessage("staff_chat.format"),
                user.getServer().getDisplayName(),
                user.getPrefix() + user.getDisplayName(),
                message
        );

        Proxy.broadcastMessage(
                this.getGroup(),
                broadcastMessage
        );
    }
}
