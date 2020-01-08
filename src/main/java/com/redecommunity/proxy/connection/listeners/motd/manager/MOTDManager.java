package com.redecommunity.proxy.connection.listeners.motd.manager;

import com.google.common.collect.Lists;
import com.redecommunity.proxy.connection.listeners.motd.dao.MOTDDao;
import com.redecommunity.proxy.connection.listeners.motd.data.MOTD;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * Created by @SrGutyerrez
 */
public class MOTDManager {
    private static List<MOTD> MOTDS = Lists.newArrayList();

    public MOTDManager() {
        MOTDDao motdDao = new MOTDDao();

        Set<MOTD> MOTDS = motdDao.findAll();

        MOTDManager.MOTDS.addAll(MOTDS);
    }

    public static MOTD getCurrentMotd() {
        return MOTDManager.MOTDS
                .stream()
                .filter(MOTD::isActive)
                .findFirst()
                .orElse(null);
    }

    public static MOTD toMotd(ResultSet resultSet) throws SQLException {
        return new MOTD(
                resultSet.getInt("id"),
                resultSet.getString("first_line"),
                resultSet.getString("second_line"),
                resultSet.getBoolean("active")
        );
    }
}
