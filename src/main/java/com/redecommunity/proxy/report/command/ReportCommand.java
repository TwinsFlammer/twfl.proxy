package com.redecommunity.proxy.report.command;

import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.group.GroupNames;
import com.redecommunity.common.shared.permissions.group.manager.GroupManager;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.report.data.ReportReason;
import com.redecommunity.common.shared.report.manager.ReportReasonManager;
import com.redecommunity.proxy.Proxy;

/**
 * Created by @SrGutyerrez
 */
public class ReportCommand extends CustomCommand {
    public ReportCommand() {
        super("report", CommandRestriction.IN_GAME, "default");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length != 2) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            this.getName(),
                            "<usuário> <motivo>"
                    )
            );
            return;
        }

        String targetName = args[0];
        String reportReasonName = args[1];

        User user1 = UserManager.getUser(targetName);

        if (user1 == null) {
            user.sendMessage(
                    language.getMessage("messages.player.invalid_player")
            );
            return;
        }

        ReportReason reportReason = ReportReasonManager.getReportReason(reportReasonName);

        if (reportReason == null) {
            user.sendMessage(
                    language.getMessage("report.invalid_reason")
            );
            return;
        }

        user.report(
                user1,
                reportReason
        );

        Proxy.broadcastMessage(
                GroupManager.getGroup(GroupNames.MODERATOR),
                String.format(
                        language.getMessage("report.report_received"),
                        user.getPrefix() + user.getDisplayName(),
                        user1.getPrefix() + user1.getDisplayName()
                )
        );
    }
}
