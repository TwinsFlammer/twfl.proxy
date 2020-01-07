package com.redecommunity.proxy.punish.data;

import com.google.common.collect.Maps;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.proxy.punish.dao.PunishmentDao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;

import java.util.HashMap;

/**
 * Created by @SrGutyerrez
 */
@AllArgsConstructor
public class Punishment {
    @Getter
    private final Integer id,
            userId,
            stafferId,
            reasonId,
            revokerId,
            revokeReasonId;

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

    public Boolean isTemporary() {
        return this.endTime != null;
    }

    public Boolean isActive() {
        if (this.isTemporary() && System.currentTimeMillis() >= this.endTime) {
            this.status = false;

            HashMap<String, Object> keys = Maps.newHashMap();

            keys.put("status", this.status);

            PunishmentDao punishmentDao = new PunishmentDao();

            punishmentDao.update(
                    keys,
                    "id",
                    this.id
            );
        }
        return this.status;
    }

    public ChatColor getColor() {
        if (this.revokerId != null) return ChatColor.GRAY;
        return this.startTime == null ? ChatColor.GOLD : this.status ? ChatColor.GREEN : ChatColor.RED;
    }

    public void revoke(User user, RevokeReason revokeReason) {

    }

    public void broadcast() {
        HashMap<String, Object> keys = Maps.newHashMap();

        keys.put("status", this.status);
        keys.put("revoker_id", this.revokerId);
        keys.put("revoke_motive_id", this.revokeReasonId);
        keys.put("revoke_time", this.revokeTime);

        PunishmentDao punishmentDao = new PunishmentDao();

        punishmentDao.update(
                keys,
                "id",
                this.id
        );
    }
}
