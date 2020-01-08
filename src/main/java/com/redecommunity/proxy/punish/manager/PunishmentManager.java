package com.redecommunity.proxy.punish.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.proxy.punish.dao.PunishmentDao;
import com.redecommunity.proxy.punish.data.Duration;
import com.redecommunity.proxy.punish.data.PunishReason;
import com.redecommunity.proxy.punish.data.Punishment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by @SrGutyerrez
 */
public class PunishmentManager {
    private static HashMap<Integer, List<Punishment>> punishments = Maps.newHashMap();

    public static List<Punishment> getPunishments(Integer userId) {
        return PunishmentManager.punishments.containsKey(userId) ? PunishmentManager.punishments.get(userId) : PunishmentManager.findAll(userId);
    }

    public static List<Punishment> getPunishments(User user) {
        return PunishmentManager.getPunishments(user.getId());
    }

    public static List<Punishment> clearPunishments(Integer userId) {
        return PunishmentManager.punishments.remove(userId);
    }

    public static List<Punishment> clearPunishments(User user) {
        return PunishmentManager.clearPunishments(user.getId());
    }

    private static List<Punishment> findAll(Integer userId) {
        PunishmentDao punishmentDao = new PunishmentDao();

        HashMap<String, Integer> keys = Maps.newHashMap();

        keys.put("user_id", userId);

        List<Punishment> punishments = Lists.newArrayList(punishmentDao.findAll(keys));

        PunishmentManager.punishments.put(userId, punishments);

        return punishments;
    }

    public static Duration getDuration(User user, PunishReason punishReason) {
        List<Punishment> punishments = PunishmentManager.getPunishments(user);

        Integer count = (int) punishments.stream()
                .filter(Objects::nonNull)
                .filter(punishment -> punishment.getPunishReason().isSimilar(punishReason))
                .count();

        List<Duration> durations = punishReason.getDurations();

        return durations.get(durations.size() < count ? durations.size() - 1 : count);
    }

    public static Punishment generatePunishment(User staffer, User user, PunishReason punishReason, String proof, Boolean hidden) {
        Duration duration = PunishmentManager.getDuration(user, punishReason);
        Long endTime = duration.isTemporary() ? duration.getEndTime() : null;

        return new Punishment(
                0,
                user.getId(),
                staffer.getId(),
                punishReason.getId(),
                null,
                null,
                hidden,
                null,
                true,
                proof,
                System.currentTimeMillis(),
                null,
                endTime,
                null
        );
    }

    public static Punishment toPunishment(ResultSet resultSet) throws SQLException {
        return new Punishment(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getInt("staffer_id"),
                resultSet.getInt("reason_id"),
                resultSet.getInt("revoker_id"),
                resultSet.getInt("revoke_reason_id"),
                resultSet.getBoolean("hidden"),
                resultSet.getBoolean("perpetual"),
                resultSet.getBoolean("status"),
                resultSet.getString("proof"),
                resultSet.getLong("time"),
                resultSet.getLong("start_time"),
                resultSet.getLong("end_time"),
                resultSet.getLong("revoker_time")
        );
    }
}
