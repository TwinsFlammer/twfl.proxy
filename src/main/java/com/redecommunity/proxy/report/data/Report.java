package com.redecommunity.proxy.report.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * Created by @SrGutyerrez
 */
@RequiredArgsConstructor
@Getter
public class Report {
    private final Integer userId, targetId;
    private final Long time;

    public Boolean canReportAgain() {
        return System.currentTimeMillis() >= (this.time + TimeUnit.MINUTES.toMillis(15));
    }
}
