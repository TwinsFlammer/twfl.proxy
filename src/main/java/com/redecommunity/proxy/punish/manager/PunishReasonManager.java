package com.redecommunity.proxy.punish.manager;

import com.google.common.collect.Lists;
import com.redecommunity.common.shared.permissions.group.data.Group;
import com.redecommunity.common.shared.permissions.group.manager.GroupManager;
import com.redecommunity.proxy.punish.data.Duration;
import com.redecommunity.proxy.punish.data.PunishReason;
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
public class PunishReasonManager {
    private static List<PunishReason> punishReasons = Lists.newArrayList();

    public static List<PunishReason> getPunishReasons() {
        return PunishReasonManager.punishReasons
                .stream()
                .sorted((punishReason1, punishReason2) -> punishReason2.getName().compareTo(punishReason1.getName()))
                .collect(Collectors.toList());
    }

    public static PunishReason getPunishMotive(String name) {
        return PunishReasonManager.punishReasons
                .stream()
                .filter(punishReason -> punishReason.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public static PunishReason getPunishMotive(Integer id) {
        return PunishReasonManager.punishReasons
                .stream()
                .filter(punishReason -> punishReason.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static PunishReason toMotive(ResultSet resultSet) throws SQLException {
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

        Optional<PunishReason> similarMotive = PunishReasonManager.getSimilarPunishMotive(name);

        PunishReason punishReason = similarMotive.isPresent() ? PunishReasonManager.getPunishMotive(name) : new PunishReason(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("display_name"),
                resultSet.getString("description"),
                group,
                durations
        );

        if (similarMotive.isPresent()) punishReason.getDurations().add(duration1);

        return punishReason;
    }

    private static Optional<PunishReason> getSimilarPunishMotive(String name) {
        return PunishReasonManager.punishReasons
                .stream()
                .filter(punishReason -> punishReason.getName().equalsIgnoreCase(name))
                .findFirst();
    }
}
