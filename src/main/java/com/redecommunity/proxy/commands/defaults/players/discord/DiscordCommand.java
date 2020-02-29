package com.redecommunity.proxy.commands.defaults.players.discord;

import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.group.GroupNames;
import com.redecommunity.common.shared.permissions.user.data.User;

/**
 * Created by @SrGutyerrez
 */
public class DiscordCommand extends CustomCommand {
    public DiscordCommand() {
        super("discord", CommandRestriction.IN_GAME, GroupNames.DEFAULT);
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
