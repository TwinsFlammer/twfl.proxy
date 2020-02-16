package com.redefocus.proxy.commands.defaults.players.discord;

import com.redefocus.api.bungeecord.commands.CustomCommand;
import com.redefocus.api.bungeecord.commands.enums.CommandRestriction;
import com.redefocus.common.shared.language.enums.Language;
import com.redefocus.common.shared.permissions.user.data.User;

/**
 * Created by @SrGutyerrez
 */
public class DiscordCommand extends CustomCommand {
    public DiscordCommand() {
        super("discord", CommandRestriction.IN_GAME, "default");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (user.getDiscordId() != null && user.getDiscordId() != 0) {
            user.sendMessage(
                    language.getMessage("discord.associated")
            );
            return;
        }
    }
}
