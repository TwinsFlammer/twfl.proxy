package com.redecommunity.proxy.punish.manager;

import com.redecommunity.proxy.punish.data.Punishment;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by @SrGutyerrez
 */
public class PunishmentManager {
    public static Punishment toPunishment(ResultSet resultSet) throws SQLException {
        return new Punishment(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getInt("staffer_id"),
                resultSet.getInt("motive_id"),
                resultSet.getInt("revoker_id"),
                resultSet.getInt("revoker_motive_id"),
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
