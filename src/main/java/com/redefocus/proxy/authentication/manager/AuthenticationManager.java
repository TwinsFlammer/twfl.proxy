package com.redefocus.proxy.authentication.manager;

import com.google.common.collect.Maps;
import com.redefocus.common.shared.language.enums.Language;
import com.redefocus.common.shared.server.data.Server;
import com.redefocus.proxy.Proxy;
import com.redefocus.common.shared.Common;
import com.redefocus.common.shared.permissions.user.data.User;

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
        user.setLogged(true);

        user.sendTitle(
                "§a§lAutênticado!",
                "§fRedirecionando...",
                0,
                60,
                0
        );

        Common.getInstance().getScheduler().schedule(
                () -> {
                    AuthenticationManager.sendUserToLobby(user);
                },
                3,
                TimeUnit.SECONDS
        );
    }

    private static void sendUserToLobby(User user) {
        Language language = user.getLanguage();

        Server server = Proxy.getLobby();

        if (server == null) {
            user.kick(
                    language.getMessage("messages.default_commands.not_have_lobby_live")
            );
            return;
        }

        user.connect(server);
    }
}
