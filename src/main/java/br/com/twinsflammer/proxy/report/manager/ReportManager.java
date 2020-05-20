package br.com.twinsflammer.proxy.report.manager;

import com.google.common.collect.Lists;
import br.com.twinsflammer.proxy.report.data.Report;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Created by @SrGutyerrez
 */
public class ReportManager {
    private static List<Report> reports = Lists.newArrayList();

    public static List<Report> getReports() {
        return ReportManager.reports;
    }

    public static void addReport(Report report) {
        ReportManager.reports.add(report);
    }

    public static Report getLastReport(Integer userId, Integer targetId) {
        ReportManager.clear();

        return ReportManager.reports
                .stream()
                .filter(Objects::nonNull)
                .filter(report -> report.getUserId().equals(userId))
                .filter(report -> report.getTargetId().equals(targetId))
                .filter(ReportManager.predicate(Report::canReportAgain).negate())
                .findFirst()
                .orElse(null);
    }

    private static void clear() {
        ReportManager.reports.removeIf(Report::canReportAgain);
    }

    private static <T> Predicate<T> predicate(Predicate<T> predicate) {
        return predicate;
    }
}
