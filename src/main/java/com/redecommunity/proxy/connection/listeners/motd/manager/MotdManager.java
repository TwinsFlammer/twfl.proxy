package com.redecommunity.proxy.connection.listeners.motd.manager;

import com.google.common.collect.Lists;
import com.redecommunity.proxy.connection.listeners.motd.data.Motd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by @SrGutyerrez
 */
public class MotdManager {
    private static List<Motd> motds = Lists.newArrayList();

    public static Motd toMotd(ResultSet resultSet) throws SQLException {
        return new Motd(
                resultSet.getInt("id"),
                resultSet.getString("first_line"),
                resultSet.getString("second_line"),
                resultSet.getBoolean("active")
        );
    }
}
