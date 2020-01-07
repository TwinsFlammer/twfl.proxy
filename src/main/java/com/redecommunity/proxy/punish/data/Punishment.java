package com.redecommunity.proxy.punish.data;

import com.google.common.collect.Maps;
import com.redecommunity.common.shared.permissions.group.data.Group;
import com.redecommunity.common.shared.permissions.group.manager.GroupManager;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.proxy.Proxy;
import com.redecommunity.proxy.punish.dao.PunishmentDao;
import com.redecommunity.proxy.punish.manager.PunishReasonManager;
import com.redecommunity.proxy.punish.manager.PunishmentManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by @SrGutyerrez
 */
@AllArgsConstructor
public class Punishment {
    @Getter
    private final Integer id,
            userId,
            stafferId,
            reasonId;

    @Getter
    private Integer revokeUserId,
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
            endTime;

    @Getter
    private Long revokeTime;

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
        if (this.revokeUserId != null) return ChatColor.GRAY;
        return this.startTime == null ? ChatColor.GOLD : this.status ? ChatColor.GREEN : ChatColor.RED;
    }

    public PunishReason getPunishReason() {
        return PunishReasonManager.getPunishReason(this.reasonId);
    }

    private Duration getDuration() {
        PunishReason punishReason = this.getPunishReason();

        List<Punishment> punishments = PunishmentManager.getPunishments(this.userId);

        Integer count = (int) punishments.stream()
                .filter(Objects::nonNull)
                .filter(punishment -> punishment.getPunishReason().isSimilar(punishReason))
                .count();

        List<Duration> durations = punishReason.getDurations();

        Duration duration = durations.get((durations.size() < count ? durations.size() : count) - 1);

        return duration;
    }

    public void revoke(User user, RevokeReason revokeReason) {
        this.status = false;
        this.revokeUserId = user.getId();
        this.revokeReasonId = revokeReason.getId();
        this.revokeTime = System.currentTimeMillis();

        HashMap<String, Object> keys = Maps.newHashMap();

        keys.put("status", this.status);
        keys.put("revoke_user_id", this.revokeUserId);
        keys.put("revoke_reason_id", this.revokeReasonId);
        keys.put("revoke_time", this.revokeTime);

        PunishmentDao punishmentDao = new PunishmentDao();

        punishmentDao.update(
                keys,
                "id",
                this.id
        );
    }

    public void broadcast() {
        User user = UserManager.getUser(this.userId),
                staffer = UserManager.getUser(this.stafferId);

        PunishReason punishReason = this.getPunishReason();
        Duration duration = this.getDuration();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\n")
                .append("§c * ")
                .append(user.getDisplayName())
                .append(" foi ")
                .append(duration.getPunishType().getDisplayName())
                .append(" por ")
                .append(staffer.getDisplayName())
                .append("\n")
                .append("§c * Motivo: ")
                .append(punishReason.getDisplayName())
                .append(this.proof == null || this.proof.isEmpty() ? " - " + this.proof : "")
                .append("\n");

        if (duration.isTemporary()) stringBuilder.append("§c * Duração: ")
                .append(duration.getTimeTypeDisplayName())
                .append("\n");

        Group group = this.hidden ? GroupManager.getGroup("manager") : GroupManager.getGroup("default");

        Proxy.broadcastMessage(
                group,
                stringBuilder.toString()
        );
    }
}
