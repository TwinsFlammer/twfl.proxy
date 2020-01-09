package com.redecommunity.proxy.report.manager;

import com.google.common.collect.Lists;
import com.redecommunity.proxy.report.data.Report;

import java.util.List;
import java.util.Objects;

/**
 * Created by @SrGutyerrez
 */
public class ReportManager {
    private static List<Report> reports = Lists.newArrayList();

    public static Report getLastReport(Integer userId, Integer targetId) {
        ReportManager.clear();

        return ReportManager.reports
                .stream()
                .filter(Objects::nonNull)
                .filter(report -> report.getUserId().equals(userId))
                .filter(report -> report.getTargetId().equals(targetId))
                .filter(Report::canReportAgain)
                .findFirst()
                .orElse(null);
    }

    private static void clear() {
        ReportManager.reports.removeIf(Report::canReportAgain);
    }
}
