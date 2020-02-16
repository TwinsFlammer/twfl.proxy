package com.redefocus.proxy.commands.defaults.staff;

import com.redefocus.api.bungeecord.commands.CustomCommand;
import com.redefocus.api.bungeecord.commands.enums.CommandRestriction;
import com.redefocus.common.shared.language.enums.Language;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.preference.Preference;
import com.redefocus.common.shared.util.Helper;
import com.redefocus.proxy.Proxy;

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
                Helper.colorize(broadcastMessage),
                Preference.CHAT_STAFF
        );
    }
}
