package com.redecommunity.proxy.connection.listeners.motd.manager;

import com.google.common.collect.Lists;
import com.redecommunity.proxy.connection.listeners.motd.dao.MotdDao;
import com.redecommunity.proxy.connection.listeners.motd.data.Motd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * Created by @SrGutyerrez
 */
public class MotdManager {
    private static List<Motd> motds = Lists.newArrayList();

    public MotdManager() {
        MotdDao motdDao = new MotdDao();

        Set<Motd> motds = motdDao.findAll();

        MotdManager.motds.addAll(motds);
    }

    public static Motd getCurrentMotd() {
        return MotdManager.motds
                .stream()
                .filter(Motd::isActive)
                .findFirst()
                .orElse(null);
    }

    public static Motd toMotd(ResultSet resultSet) throws SQLException {
        return new Motd(
                resultSet.getInt("id"),
                resultSet.getString("first_line"),
                resultSet.getString("second_line"),
                resultSet.getBoolean("active")
        );
    }
}
