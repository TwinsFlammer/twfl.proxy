package br.com.twinsflammer.proxy.punish.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.proxy.punish.dao.PunishmentDao;
import br.com.twinsflammer.proxy.punish.data.Duration;
import br.com.twinsflammer.proxy.punish.data.PunishReason;
import br.com.twinsflammer.proxy.punish.data.Punishment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by @SrGutyerrez
 */
public class PunishmentManager {
    public static Set<Punishment> getPunishments(Integer userId) {
        PunishmentDao punishmentDao = new PunishmentDao();

        HashMap<String, Object> keys = Maps.newHashMap();

        keys.put("user_id", userId);

        Set<Punishment> punishments = punishmentDao.findAll(keys);

        return punishments;
    }

    public static Set<Punishment> getPunishments(User user) {
        return PunishmentManager.getPunishments(user.getId());
    }

    public static Punishment getPunishment(Integer id) {
        PunishmentDao punishmentDao = new PunishmentDao();

        HashMap<String, Object> keys = Maps.newHashMap();

        keys.put("id", id);

        return punishmentDao.findOne(keys);
    }

    private static Set<Punishment> findAll(Integer userId) {
        PunishmentDao punishmentDao = new PunishmentDao();

        HashMap<String, Integer> keys = Maps.newHashMap();

        keys.put("user_id", userId);

        return punishmentDao.findAll(keys);
    }

    public static Duration getDuration(Integer punishCount, PunishReason punishReason) {
        List<Duration> durations = punishReason.getDurations();

        return durations.size() <= punishCount ? durations.get(durations.size() - 1) : durations.get(punishCount);
    }

    public static Punishment generatePunishment(User staffer, User user, PunishReason punishReason, String proof, Boolean hidden) {
        Set<Punishment> punishments = PunishmentManager.getPunishments(user);

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
                count,
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
