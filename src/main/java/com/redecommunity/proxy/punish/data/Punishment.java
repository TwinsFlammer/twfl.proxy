package com.redecommunity.proxy.punish.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;

/**
 * Created by @SrGutyerrez
 */
@AllArgsConstructor
public class Punishment {
    @Getter
    private final Integer id,
            userId,
            stafferId,
            motiveId,
            revokerId,
            revokeMotiveId;

    private final Boolean hidden,
            perpetual;

    @Getter
    @Setter
    private Boolean status;

    @Getter
    private final String proof;

    @Getter
    private final Long time,
            startTime,
            endTime,
            revokeTime;

    public Boolean isHidden() {
        return this.hidden;
    }

    public Boolean isPerpetual() {
        return this.perpetual;
    }

    public Boolean isActive() {
        if (System.currentTimeMillis() >= this.endTime) {
            this.status = false;
            // TODO not implemented yet
        }

        return this.status;
    }

    public ChatColor getColor() {
        if (this.revokerId != null) return ChatColor.GRAY;
        return this.startTime == null ? ChatColor.GOLD : this.status ? ChatColor.GREEN : ChatColor.RED;
    }
}
