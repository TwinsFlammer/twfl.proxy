package com.redecommunity.proxy.announcement.command;

import com.redecommunity.common.shared.permissions.group.GroupNames;
import com.redecommunity.proxy.announcement.command.arguments.AnnouncementDisableCommand;
import com.redecommunity.proxy.announcement.command.arguments.AnnouncementEnableCommand;
import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.proxy.announcement.command.arguments.AnnouncementListCommand;

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
