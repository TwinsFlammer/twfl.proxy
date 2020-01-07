package com.redecommunity.proxy.punish.manager;

import com.google.common.collect.Lists;
import com.redecommunity.proxy.punish.data.RevokeReason;

import java.util.List;
import java.util.Objects;

/**
 * Created by @SrGutyerrez
 */
public class RevokeReasonManager {
    private final static List<RevokeReason> REVOKE_REASONS = Lists.newArrayList();

    public static List<RevokeReason> getRevokeReasons() {
        return RevokeReasonManager.REVOKE_REASONS;
    }

    public static RevokeReason getRevokeMotive(Integer id) {
        return RevokeReasonManager.REVOKE_REASONS
                .stream()
                .filter(Objects::nonNull)
                .filter(revokeReason -> revokeReason.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
