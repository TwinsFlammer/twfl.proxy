package com.redecommunity.proxy.punish.data;

import com.google.common.collect.Maps;
import com.redecommunity.common.shared.permissions.group.data.Group;
import com.redecommunity.common.shared.permissions.group.manager.GroupManager;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.proxy.Proxy;
import com.redecommunity.proxy.punish.dao.PunishmentDao;
import com.redecommunity.proxy.punish.data.enums.PunishType;
import com.redecommunity.proxy.punish.manager.PunishReasonManager;
import com.redecommunity.proxy.punish.manager.PunishmentManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import org.json.simple.JSONObject;

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
        return this.endTime != 0;
    }

    public Boolean isBan() {
        Duration duration = this.getDuration();

        return duration.getPunishType() != PunishType.MUTE;
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

        System.out.println(this.status);

        return this.status;
    }

    public Boolean isStarted() {
        return this.startTime != 0;
    }

    public Boolean isRevoked() {
        return this.revokeTime != 0;
    }

    public Boolean hasValidProof() {
        return this.proof != null && !this.proof.isEmpty();
    }

    public ChatColor getColor() {
        if (this.revokeUserId != null) return ChatColor.GRAY;
        return this.startTime == null ? ChatColor.GOLD : this.status ? ChatColor.GREEN : ChatColor.RED;
    }

    public String getProof() {
        return this.hasValidProof() ? " - " + this.proof : "";
    }

    public User getUser() {
        return UserManager.getUser(this.userId);
    }

    public User getStaffer() {
        return UserManager.getUser(this.stafferId);
    }

    public User getRevoker() {
        return UserManager.getUser(this.revokeUserId);
    }

    public PunishReason getPunishReason() {
        return PunishReasonManager.getPunishReason(this.reasonId);
    }

    public Duration getDuration() {
        return PunishmentManager.getDuration(this.getUser(), this.getPunishReason());
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
        PunishReason punishReason = this.getPunishReason();
        Duration duration = this.getDuration();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\n")
                .append("§c * ")
                .append(this.getUser().getDisplayName())
                .append(" foi ")
                .append(duration.getPunishType().getDisplayName())
                .append(" por ")
                .append(this.getStaffer().getDisplayName())
                .append("\n")
                .append("§c * Motivo: ")
                .append(punishReason.getDisplayName())
                .append(this.getProof())
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

    public void start() {
        HashMap<String, Object> keys = Maps.newHashMap();

        keys.put("start_time", System.currentTimeMillis());

        PunishmentDao punishmentDao = new PunishmentDao();

        punishmentDao.update(
                keys,
                "id",
                this.id
        );
    }

    public String toString() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", this.id);
        jsonObject.put("user_id", this.userId);
        jsonObject.put("staffer_id", this.stafferId);
        jsonObject.put("reason_id", this.reasonId);
        jsonObject.put("revoke_user_id", this.revokeUserId);
        jsonObject.put("revoke_reason_id", this.revokeReasonId);
        jsonObject.put("hidden", this.hidden);
        jsonObject.put("perpetual", this.perpetual);
        jsonObject.put("status", this.status);
        jsonObject.put("proof", this.proof);
        jsonObject.put("time", this.time);
        jsonObject.put("start_time", this.startTime);
        jsonObject.put("end_time", this.endTime);
        jsonObject.put("revoke_time", this.revokeTime);

        return jsonObject.toString();
    }
}
