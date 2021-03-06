package br.com.twinsflammer.proxy.connection.listeners.motd.manager;

import com.google.common.collect.Lists;
import br.com.twinsflammer.proxy.connection.listeners.motd.dao.MOTDDao;
import br.com.twinsflammer.proxy.connection.listeners.motd.data.MOTD;
import br.com.twinsflammer.common.shared.Common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by @SrGutyerrez
 */
public class MOTDManager {
    private static List<MOTD> MOTDS = Lists.newArrayList();

    public MOTDManager() {
        MOTDDao motdDao = new MOTDDao();

        Common.getInstance().getScheduler().scheduleAtFixedRate(
                () -> {
                    MOTDManager.MOTDS = Lists.newArrayList(motdDao.findAll());
                },
                0,
                5,
                TimeUnit.SECONDS
        );
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
