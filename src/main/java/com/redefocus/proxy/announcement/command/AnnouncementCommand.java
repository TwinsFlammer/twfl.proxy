package com.redefocus.proxy.announcement.command;

import com.redefocus.api.bungeecord.commands.CustomCommand;
import com.redefocus.api.bungeecord.commands.enums.CommandRestriction;
import com.redefocus.common.shared.language.enums.Language;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.proxy.announcement.command.arguments.AnnouncementDisableCommand;
import com.redefocus.proxy.announcement.command.arguments.AnnouncementEnableCommand;
import com.redefocus.proxy.announcement.command.arguments.AnnouncementListCommand;

/**
 * Created by @SrGutyerrez
 */
public class AnnouncementCommand extends CustomCommand {
    public static final String COMMAND_NAME = "anúncio";

    public AnnouncementCommand() {
        super(AnnouncementCommand.COMMAND_NAME, CommandRestriction.IN_GAME, "manager");

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
