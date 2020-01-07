package com.redecommunity.proxy.punish.manager;

import com.google.common.collect.Lists;
import com.redecommunity.proxy.punish.data.RevokeMotive;

import java.util.List;
import java.util.Objects;

/**
 * Created by @SrGutyerrez
 */
public class RevokeMotiveManager {
    private final static List<RevokeMotive> revokeMotives = Lists.newArrayList();

    public static List<RevokeMotive> getRevokeMotives() {
        return RevokeMotiveManager.revokeMotives;
    }

    public static RevokeMotive getRevokeMotive(Integer id) {
        return RevokeMotiveManager.revokeMotives
                .stream()
                .filter(Objects::nonNull)
                .filter(revokeMotive -> revokeMotive.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
