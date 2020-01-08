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
                return (this.duration > 1 ? "dias" : "dia");
            case HOURS:
                return (this.duration > 1 ? "horas" : "hora");
            case MINUTES:
                return (this.duration > 1 ? "minutos" : "minuto");
            case SECONDS:
                return (this.duration > 1 ? "segundos" : "segundo");
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
