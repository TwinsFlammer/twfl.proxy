package com.redecommunity.proxy.punish.data;

import com.redecommunity.proxy.punish.data.enums.PunishType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * Created by @SrGutyerrez
 */
@RequiredArgsConstructor
@Getter
public class Duration {
    private final Integer duration;
    private final TimeUnit timeType;
    private final PunishType punishType;

    public String getTimeTypeDisplayName() {
        switch (this.timeType) {
            case DAYS:
                return "dias";
            case HOURS:
                return "horas";
            case MINUTES:
                return "minutos";
            default:
                return "indefinido";
        }
    }

    public Long getEndTime() {
        return System.currentTimeMillis() + timeType.toMillis(this.duration);
    }

    public Boolean isTemporary() {
        return this.punishType != PunishType.BAN;
    }
}
