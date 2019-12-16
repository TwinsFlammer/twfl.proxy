package com.redecommunity.proxy.punish.manager;

import com.google.common.collect.Lists;
import com.redecommunity.common.shared.permissions.group.data.Group;
import com.redecommunity.common.shared.permissions.group.manager.GroupManager;
import com.redecommunity.proxy.punish.data.Duration;
import com.redecommunity.proxy.punish.data.Motive;
import com.redecommunity.proxy.punish.data.enums.PunishType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by @SrGutyerrez
 */
public class MotiveManager {
    private static List<Motive> motives = Lists.newArrayList();

    public static List<Motive> getMotives() {
        return MotiveManager.motives
                .stream()
                .sorted((motive1, motive2) -> motive2.getName().compareTo(motive1.getName()))
                .collect(Collectors.toList());
    }

    public static Motive getMotive(String name) {
        return MotiveManager.motives
                .stream()
                .filter(motive -> motive.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public static Motive toMotive(ResultSet resultSet) throws SQLException {
        String name = resultSet.getString("name");

        Integer groupId = resultSet.getInt("group_id");

        Group group = GroupManager.getGroup(groupId);

        Integer duration = resultSet.getInt("duration");
        String preTimeUnit = resultSet.getString("duration_type");
        String type = resultSet.getString("type");

        TimeUnit timeType = TimeUnit.valueOf(preTimeUnit);
        PunishType punishType = PunishType.valueOf(type);

        Duration duration1 = new Duration(duration, timeType, punishType);

        List<Duration> durations = Lists.newArrayList();

        durations.add(duration1);

        Optional<Motive> optional = MotiveManager.getSimilarMotive(name);

        Motive motive = optional.isPresent() ?
                MotiveManager.getMotive(name)
                :
                new Motive(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("display_name"),
                        resultSet.getString("description"),
                        group,
                        durations
                );

        if (optional.isPresent()) motive.getDurations().add(duration1);

        return motive;
    }

    private static Optional<Motive> getSimilarMotive(String name) {
        return MotiveManager.motives
                .stream()
                .filter(motive -> motive.getName().equalsIgnoreCase(name))
                .findFirst();
    }
}
