package com.redefocus.proxy.punish.data;

import com.google.common.collect.Maps;
import com.redefocus.proxy.punish.dao.PunishmentDao;
import com.redefocus.proxy.punish.data.enums.PunishType;
import com.redefocus.proxy.punish.manager.PunishReasonManager;
import com.redefocus.proxy.punish.manager.PunishmentManager;
import com.redefocus.common.shared.language.enums.Language;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.group.data.Group;
import com.redefocus.common.shared.permissions.group.manager.GroupManager;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.permissions.user.manager.UserManager;
import com.redefocus.proxy.Proxy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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
            count;

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
    private final Long time;
    @Getter
    private Long startTime;
    @Getter
    private final Long endTime;

    @Getter
    private Long revokeTime;

    public Boolean isHidden() {
        return this.hidden;
    }

    public Boolean isPerpetual() {
        return this.perpetual;
    }

    public Boolean isTemporary() {
        return this.endTime != null && this.endTime != 0;
    }

    public Boolean isBan() {
        Duration duration = this.getDuration();

        return duration.getPunishType() != PunishType.MUTE;
    }

    public Boolean isActive() {
        if (this.isTemporary() && this.status && this.isFinalized()) {
            this.status = false;

            this.update(UpdateType.FINALIZED);
        }

        return this.status;
    }

    public Boolean isStarted() {
        return this.startTime != null && this.startTime != 0;
    }

    public Boolean isRevoked() {
        return this.revokeTime != null && this.revokeTime != 0;
    }

    public Boolean isFinalized() {
        return System.currentTimeMillis() >= this.endTime;
    }

    public Boolean hasValidProof() {
        return this.proof != null && !this.proof.isEmpty() && !this.proof.equalsIgnoreCase("null");
    }

    public Boolean canRevokeBy(User user) {
        if (user.hasGroup(GroupNames.MANAGER)) return true;
        else if (user.hasGroup(GroupNames.ADMINISTRATOR)) {
            return (this.time + TimeUnit.HOURS.toMillis(14)) >= System.currentTimeMillis();
        } else if (user.hasGroup(GroupNames.MODERATOR) && this.stafferId.equals(user.getId())) {
            return (this.time + TimeUnit.HOURS.toMillis(7)) >= System.currentTimeMillis();
        } else if (user.hasGroup(GroupNames.HELPER) && this.stafferId.equals(user.getId())) {
            return (this.time + TimeUnit.HOURS.toMillis(3)) >= System.currentTimeMillis();
        }
        return false;
    }

    public ChatColor getColor() {
        if (this.revokeUserId != null && this.revokeUserId != 0) return ChatColor.GRAY;
        return !this.isStarted() && !this.isFinalized() ? ChatColor.YELLOW : this.status ? ChatColor.GREEN : ChatColor.RED;
    }

    public String getFancyProof() {
        return this.hasValidProof() ? " - " + this.proof : "";
    }

    public String getDate() {
        return this.getSimpleDateFormat().format(this.time);
    }

    public String getStartDate() {
        return this.isStarted() || this.isFinalized() ? this.getSimpleDateFormat().format(this.startTime) : "[AGUARDANDO INÍCIO]";
    }

    public String getRevokeDate() {
        return this.getSimpleDateFormat().format(this.revokeTime);
    }

    private SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat("dd/MM/YYYY HH:mm");
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
        return PunishmentManager.getDuration(this.count, this.getPunishReason());
    }

    public void revoke(User user, RevokeReason revokeReason) {
        this.status = false;
        this.revokeUserId = user.getId();
        this.revokeReasonId = revokeReason.getId();
        this.revokeTime = System.currentTimeMillis();

        this.update(UpdateType.REVOKED);

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\n")
                .append("§e * ")
                .append(this.getRevoker().getDisplayName())
                .append(" revogou uma punição de ")
                .append(this.getUser().getDisplayName())
                .append(".")
                .append("\n")
                .append("§e * Aplicada inicialmente por: ")
                .append(this.getStaffer().getDisplayName())
                .append("\n")
                .append("§e * Motivo: ")
                .append(revokeReason.getDisplayName())
                .append("\n");

        Group group = GroupManager.getGroup("helper");

        Proxy.broadcastMessage(
                group,
                stringBuilder.toString()
        );
    }

    public void broadcast() {
        User user = this.getUser();
        Language language = user.getLanguage();

        PunishReason punishReason = this.getPunishReason();
        Duration duration = this.getDuration();

        PunishType punishType = duration.getPunishType();

        if (user.isOnline() && punishType != PunishType.MUTE) {
            this.update(UpdateType.START);

            user.kick(
                    String.format(
                            language.getMessage("punishment.kick_message"),
                            punishType.getDisplayName(),
                            punishReason.getDisplayName(),
                            this.hasValidProof() ? this.proof : "",
                            this.getStaffer().getDisplayName(),
                            this.id
                    )
            );
        }

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
                .append(this.hasValidProof() ? " - " : "");

        if (this.hasValidProof())
            stringBuilder.append(this.getProof());

        stringBuilder.append("\n");

        if (duration.isTemporary()) stringBuilder.append("§c * Duração: ")
                .append(duration.getTimeTypeDisplayName())
                .append("\n");

        Group group = this.hidden ? GroupManager.getGroup(GroupNames.DIRECTOR) : GroupManager.getGroup("default");

        Proxy.broadcastMessage(
                group,
                stringBuilder.toString()
        );
    }

    public void start() {
        this.startTime = System.currentTimeMillis();

        this.update(UpdateType.START);
    }

    private void update(UpdateType updateType) {
        PunishmentDao punishmentDao = new PunishmentDao();

        switch (updateType) {
            case FINALIZED: {
                HashMap<String, Boolean> keys = Maps.newHashMap();

                keys.put("status", this.status);

                punishmentDao.update(
                        keys,
                        "id",
                        this.id
                );
                return;
            }
            case REVOKED: {
                HashMap<String, Object> keys = Maps.newHashMap();

                keys.put("status", this.status);
                keys.put("revoke_user_id", this.revokeUserId);
                keys.put("revoke_reason_id", this.revokeReasonId);
                keys.put("revoke_time", this.revokeTime);

                punishmentDao.update(
                        keys,
                        "id",
                        this.id
                );
                return;
            }
            case START: {
                HashMap<String, Object> keys = Maps.newHashMap();

                keys.put("start_time", this.startTime);

                punishmentDao.update(
                        keys,
                        "id",
                        this.id
                );
                return;
            }
        }
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

    enum UpdateType {
        FINALIZED,
        REVOKED,
        START;
    }
}
