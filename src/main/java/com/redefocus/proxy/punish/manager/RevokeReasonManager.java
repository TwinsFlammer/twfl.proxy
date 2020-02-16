package com.redefocus.proxy.punish.manager;

import com.google.common.collect.Lists;
import com.redefocus.common.shared.permissions.group.data.Group;
import com.redefocus.common.shared.permissions.group.manager.GroupManager;
import com.redefocus.proxy.punish.dao.RevokeReasonDao;
import com.redefocus.proxy.punish.data.RevokeReason;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * Created by @SrGutyerrez
 */
public class RevokeReasonManager {
    private final static List<RevokeReason> reasons = Lists.newArrayList();

    public RevokeReasonManager() {
        RevokeReasonDao revokeReasonDao = new RevokeReasonDao();

        RevokeReasonManager.reasons.addAll(revokeReasonDao.findAll());
    }

    public static List<RevokeReason> getReasons() {
        return RevokeReasonManager.reasons;
    }

    public static RevokeReason getRevokeReason(Integer id) {
        return RevokeReasonManager.reasons
                .stream()
                .filter(Objects::nonNull)
                .filter(revokeReason -> revokeReason.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static RevokeReason getRevokeReason(String name) {
        return RevokeReasonManager.reasons
                .stream()
                .filter(Objects::nonNull)
                .filter(revokeReason -> revokeReason.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public static RevokeReason toRevokeReason(ResultSet resultSet) throws SQLException {
        Integer groupId = resultSet.getInt("group_id");
        Group group = GroupManager.getGroup(groupId);

        return new RevokeReason(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("display_name"),
                resultSet.getString("description"),
                group
        );
    }
}
