package com.redecommunity.proxy.punish.manager;

import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.proxy.punish.data.PunishReason;
import com.redecommunity.proxy.punish.data.Punishment;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by @SrGutyerrez
 */
public class PunishmentManager {
    public static Punishment generatePunishment(User staffer, User user, PunishReason punishReason, String proof, Boolean hidden) {
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
                null,
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
