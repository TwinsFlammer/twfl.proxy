package com.redefocus.proxy.authentication.manager;

import com.google.common.collect.Maps;
import com.redefocus.common.shared.Common;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.proxy.authentication.commands.LoginCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by @SrGutyerrez
 */
public class AuthenticationManager {
    private static HashMap<Integer, Integer> attempts = Maps.newHashMap();

    public static HashMap<Integer, Integer> getAttempts() {
        return AuthenticationManager.attempts;
    }

    public static Integer removeAttempt(Integer userId) {
        return AuthenticationManager.attempts.remove(userId);
    }

    public static Integer getPasswordAttempt(Integer id) {
        return AuthenticationManager.attempts
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(id))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(null);
    }

    public static Integer getPasswordAttempt(User user) {
        return AuthenticationManager.getPasswordAttempt(user.getId());
    }

    public static Integer setPasswordAttempt(User user, Integer attempt) {
        return AuthenticationManager.attempts.put(user.getId(), attempt);
    }

    public static void authenticate(User user) {
        user.sendTitle(
                "§aAutênticado!",
                "§fRedirecionando...",
                0,
                60,
                0
        );

        Common.getInstance().getScheduler().schedule(
                () -> {
                    LoginCommand.sendUserToLobby(user);
                },
                3,
                TimeUnit.SECONDS
        );
    }
}
