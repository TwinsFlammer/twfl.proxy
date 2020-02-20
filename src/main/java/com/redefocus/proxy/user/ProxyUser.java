package com.redefocus.proxy.user;

import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.permissions.user.group.data.UserGroup;
import com.redefocus.common.shared.preference.Preference;
import com.redefocus.common.shared.report.data.ReportReason;
import com.redefocus.common.shared.skin.data.Skin;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by @SrGutyerrez
 */
public class ProxyUser extends User {
    public ProxyUser(Integer id, String name, String displayName, UUID uniqueId, String email, String password, Long discordId, Boolean twoFactorAuthenticationEnabled, String twoFactorAuthenticationCode, Long createdAt, Long firstLogin, Long lastLogin, String lastAddress, Integer lastLobbyId, Integer languageId, String twitterAccessToken, String twitterTokenSecret, Collection<UserGroup> groups, List<Preference> preferences, List<Integer> friends, List<Integer> ignored, List<ReportReason> reports, List<Skin> skins, Boolean logged, Boolean changingSkin) {
        super(id, name, displayName, uniqueId, email, password, discordId, twoFactorAuthenticationEnabled, twoFactorAuthenticationCode, createdAt, firstLogin, lastLogin, lastAddress, lastLobbyId, languageId, twitterAccessToken, twitterTokenSecret, groups, preferences, friends, ignored, reports, skins, logged, changingSkin);
    }

    public Boolean isRegistered() {
        return this.getPassword() != null;
    }
}
