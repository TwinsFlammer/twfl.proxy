package br.com.twinsflammer.proxy.announcement.command;

import br.com.twinsflammer.proxy.announcement.command.arguments.AnnouncementDisableCommand;
import br.com.twinsflammer.proxy.announcement.command.arguments.AnnouncementEnableCommand;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.api.bungeecord.commands.CustomCommand;
import br.com.twinsflammer.api.bungeecord.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.proxy.announcement.command.arguments.AnnouncementListCommand;

/**
 * Created by @SrGutyerrez
 */
public class AnnouncementCommand extends CustomCommand {
    public static final String COMMAND_NAME = "anúncio";

    public AnnouncementCommand() {
        super(AnnouncementCommand.COMMAND_NAME, CommandRestriction.IN_GAME, GroupNames.MANAGER);

        this.addArgument(
                new AnnouncementDisableCommand(),
                new AnnouncementEnableCommand(),
                new AnnouncementListCommand()
        );
    }

    @Override
    public void onCommand(User user, String[] strings) {
        Language language = user.getLanguage();

        user.sendMessage(
                language.getMessage("announcement.usage")
        );
    }
}
