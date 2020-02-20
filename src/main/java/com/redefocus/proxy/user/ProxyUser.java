package com.redefocus.proxy.user;

import com.redefocus.common.shared.permissions.user.data.User;

/**
 * Created by @SrGutyerrez
 */
public class ProxyUser extends User {
    public ProxyUser(User user) {
        super(
                user.getId(),
                user.getName(),
                user.getDisplayName(),
                user.getUniqueId(),
                user.getEmail(),
                user.getPassword(),
                user.getDiscordId(),
                user.isTwoFactorAuthenticationEnabled(),
                user.getTwoFactorAuthenticationCode(),
                user.getCreatedAt(),
                user.getFirstLogin(),
                user.getLastLogin(),
                user.getLastAddress(),
                user.getLastLobbyId(),
                user.getLanguageId(),
                user.getTwitterAccessToken(),
                user.getTwitterTokenSecret(),
                user.getGroups(),
                user.getPreferences(),
                user.getFriends(),
                user.getIgnored(),
                user.getReports(),
                user.getSkins(),
                user.isLogged(),
                user.isChangingSkin()
        );
    }

    public Boolean isRegistered() {
        return this.getPassword() != null;
    }
}
