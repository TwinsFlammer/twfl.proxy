package com.redecommunity.proxy.authentication.manager;

import com.google.common.collect.Maps;
import com.redecommunity.common.shared.permissions.user.data.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by @SrGutyerrez
 */
public class AttemptManager {
    private static HashMap<Integer, Integer> attempts = Maps.newHashMap();

    public static HashMap<Integer, Integer> getAttempts() {
        return AttemptManager.attempts;
    }

    public static Integer removeAttempt(Integer userId) {
        return AttemptManager.attempts.remove(userId);
    }

    public static Integer getPasswordAttempt(Integer id) {
        return AttemptManager.attempts
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(id))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(null);
    }

    public static Integer getPasswordAttempt(User user) {
        return AttemptManager.getPasswordAttempt(user.getId());
    }

    public static Integer setPasswordAttempt(User user, Integer attempt) {
        return AttemptManager.attempts.put(user.getId(), attempt);
    }
}
