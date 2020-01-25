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
import java.util.*;

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

    public static Punishment getPunishment(Integer id) {
        for (List<Punishment> punishments : PunishmentManager.punishments.values())
            for (Punishment punishment : punishments)
                if (punishment != null && punishment.getId().equals(id)) return punishment;

        return PunishmentManager.findOne(id);
    }

    public static List<Punishment> clearPunishments(Integer userId) {
        return PunishmentManager.punishments.remove(userId);
    }

    public static List<Punishment> clearPunishments(User user) {
        return PunishmentManager.clearPunishments(user.getId());
    }

    private static Punishment findOne(Integer id) {
        PunishmentDao punishmentDao = new PunishmentDao();

        HashMap<String, Integer> keys = Maps.newHashMap();

        keys.put("id", id);

        return punishmentDao.findOne(keys);
    }

    private static List<Punishment> findAll(Integer userId) {
        PunishmentDao punishmentDao = new PunishmentDao();

        HashMap<String, Integer> keys = Maps.newHashMap();

        keys.put("user_id", userId);

        List<Punishment> punishments = Lists.newArrayList(punishmentDao.findAll(keys));

        PunishmentManager.punishments.put(userId, punishments);

        return punishments;
    }

    public static Duration getDuration(Integer punishCount, PunishReason punishReason) {
        List<Duration> durations = punishReason.getDurations();

        return durations.size() <= punishCount ? durations.get(durations.size() - 1) : durations.get(punishCount);
    }

    public static Punishment generatePunishment(User staffer, User user, PunishReason punishReason, String proof, Boolean hidden) {
        List<Punishment> punishments = PunishmentManager.getPunishments(user);

        Integer count = (int) punishments.stream()
                .filter(Objects::nonNull)
                .filter(punishment -> punishment.getPunishReason().isSimilar(punishReason))
                .count();

        Duration duration = PunishmentManager.getDuration(count, punishReason);
        Long endTime = duration.isTemporary() ? duration.getEndTime() : null;

        return new Punishment(
                0,
                user.getId(),
                staffer.getId(),
                punishReason.getId(),
                count != 0 ? count + 1 : count,
                null,
                null,
                hidden,
                false,
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
                resultSet.getInt("revoke_user_id"),
                resultSet.getInt("revoke_reason_id"),
                resultSet.getInt("count"),
                resultSet.getBoolean("hidden"),
                resultSet.getBoolean("perpetual"),
                resultSet.getBoolean("status"),
                resultSet.getString("proof"),
                resultSet.getLong("time"),
                resultSet.getLong("start_time"),
                resultSet.getLong("end_time"),
                resultSet.getLong("revoke_time")
        );
    }
}
