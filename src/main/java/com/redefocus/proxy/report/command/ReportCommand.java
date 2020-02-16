package com.redefocus.proxy.report.command;

import com.redefocus.api.bungeecord.commands.CustomCommand;
import com.redefocus.api.bungeecord.commands.enums.CommandRestriction;
import com.redefocus.api.bungeecord.util.JSONText;
import com.redefocus.common.shared.language.enums.Language;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.group.manager.GroupManager;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.permissions.user.manager.UserManager;
import com.redefocus.common.shared.report.data.ReportReason;
import com.redefocus.common.shared.report.manager.ReportReasonManager;
import com.redefocus.proxy.Proxy;
import com.redefocus.proxy.report.data.Report;
import com.redefocus.proxy.report.manager.ReportManager;

import java.util.List;

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

        if (args.length == 1) {
            String targetName = args[0];

            User user1 = UserManager.getUser(targetName);

            if (user1 == null) {
                user.sendMessage(
                        language.getMessage("messages.player.invalid_player")
                );
                return;
            }

            JSONText jsonText = new JSONText();

            List<ReportReason> reportReasons = ReportReasonManager.getReportReasons();

            jsonText.next()
                    .text("\n")
                    .next()
                    .text("§eLista de infração disponíveís (" + reportReasons.size() + ")")
                    .next()
                    .text("\n\n");

            reportReasons.forEach(reportReason -> {
                jsonText.next()
                        .text(" - ")
                        .next()
                        .text(reportReason.getDisplayName())
                        .hoverText("§e" + reportReason.getDisplayName() +
                                "\n\n" +
                                reportReason.getDescription()
                        )
                        .clickSuggest("/report " + user1.getDisplayName() + " " + reportReason.getName())
                        .next()
                        .text("\n");
            });

            if (reportReasons.isEmpty()) jsonText.next()
                    .text("   --/--");

            jsonText.next()
                    .send(user);
            return;
        } else if (args.length == 2) {
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

            Report report = ReportManager.getLastReport(
                    user.getId(),
                    user1.getId()
            );

            if (report != null) {
                user.sendMessage(
                        language.getMessage("report.already_report_moments_ago")
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

            Report report1 = new Report(
                    user.getId(),
                    user1.getId(),
                    System.currentTimeMillis()
            );

            ReportManager.addReport(report1);
            return;
        } else {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            this.getName(),
                            "<usuário> <motivo>"
                    )
            );
            return;
        }
    }
}
