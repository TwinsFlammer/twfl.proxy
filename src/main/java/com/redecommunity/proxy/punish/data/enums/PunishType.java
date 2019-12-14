package com.redecommunity.proxy.punish.data.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by @SrGutyerrez
 */
@RequiredArgsConstructor
@Getter
public enum PunishType {
    IP_BAN("§cBan eterno", "eternamente banido"),
    BAN("§cBan eterno", "banido"),
    TEMP_BAN("§eBan temporário", "temporariamente banido"),
    MUTE("§aMute temporário", "temporariamente silenciado");

    private final String name, displayName;

    public Boolean isTemporary() {
        return this != BAN;
    }
}
