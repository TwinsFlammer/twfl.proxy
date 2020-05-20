package br.com.twinsflammer.proxy.commands.defaults.players.discord;

import br.com.twinsflammer.api.bungeecord.commands.CustomCommand;
import br.com.twinsflammer.api.bungeecord.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;

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
